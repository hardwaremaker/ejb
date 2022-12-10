package com.lp.server.lieferschein.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lp.server.lieferschein.ejbfac.AddressBlockFormatted.Tag;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.PaketVersandAntwortDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.schema.postplc.AddressRow;
import com.lp.server.schema.postplc.ArrayOfColloCodeRow;
import com.lp.server.schema.postplc.ArrayOfColloRow;
import com.lp.server.schema.postplc.ColloCodeRow;
import com.lp.server.schema.postplc.ColloRow;
import com.lp.server.schema.postplc.ImportShipment;
import com.lp.server.schema.postplc.ImportShipmentResponse;
import com.lp.server.schema.postplc.ObjectFactory;
import com.lp.server.schema.postplc.PrinterRow;
import com.lp.server.schema.postplc.ShipmentRow;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.PostPlcApiKeyDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.LieferscheinId;
import com.lp.util.Helper;

@Stateless
public class PLCVersandFacLocalBean extends Facade implements IPaketVersandFacLocal {
//	private final static Integer clientID = 21210483;
//	private final static Integer unitID = 2275702;
//	private final static String unitGUID = "c5d53516-64ea-4bdd-a818-85bed53ee777";

	@Override
	public boolean isLieferscheinVersendbar(
			LieferscheinId lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		LieferscheinDto lsDto = getLieferscheinFac()
				.lieferscheinFindByPrimaryKey(lieferscheinId.id());
		LieferartDto lieferartDto = getLocaleFac()
				.lieferartFindByPrimaryKey(lsDto.getLieferartIId(), theClientDto);
		if(Helper.isStringEmpty(lieferartDto.getCExtern())) return false;
		
		PostPlcApiKeyDto apiKey = getParameterFac()
				.getPostPlcApiKey(theClientDto.getMandant());
		return apiKey.isValid();
	}
	
	@Override
	public PaketVersandAntwortDto sendeLieferschein(
			LieferscheinId lieferscheinId, TheClientDto theClientDto) throws RemoteException, DatatypeConfigurationException {
		
		LieferscheinDto lsDto = getLieferscheinFac()
				.lieferscheinFindByPrimaryKey(lieferscheinId.id());
		PostPlcApiKeyDto apiKey = getParameterFac()
				.getPostPlcApiKey(theClientDto.getMandant());
		
		ShipmentRow row = createShipmentRow(apiKey, lsDto, theClientDto);
		try {
			HttpResponse response = post(apiKey.getUri(), lsDto, row);
			return processHttpResponse(response)
					.setLieferscheinId(lieferscheinId);
		} catch(ClientProtocolException e) {
			myLogger.error("protocol:", e);
			return new PaketVersandAntwortDto(e)
					.setLieferscheinId(lieferscheinId);
		} catch(IOException e) {
			myLogger.error("io:", e);
			return new PaketVersandAntwortDto(e)
					.setLieferscheinId(lieferscheinId);
		}
	}
	
	private ShipmentRow createShipmentRow(PostPlcApiKeyDto apiKey, 
			LieferscheinDto lsDto, TheClientDto theClientDto) throws RemoteException, DatatypeConfigurationException {
		ShipmentRow row = new ShipmentRow();
		ObjectFactory of = new ObjectFactory();
		setupCredentials(apiKey, row);
		setupPrinter(lsDto, of, row);
		setupVersandOption(lsDto, of, row);
		setupReference(lsDto, of, row);
		setupCollis(lsDto, of, row);
		setupDeliveryDate(lsDto, of, row);
		setupRecipient(lsDto, of, row, theClientDto);
		setupShipper(lsDto, of, row, theClientDto);
		return row;
	}
		
	private ShipmentRow setupCredentials(PostPlcApiKeyDto apiKey, ShipmentRow row) {
		if(!apiKey.isValid()) {
			myLogger.error("PostPlcApiKey has invalid syntax");
			throw EJBExcFactory.plcApiKeyUngueltigesFormat(apiKey.getParameter());
		}
		
		row.setClientID(apiKey.getClientID());
		row.setOrgUnitGuid(apiKey.getOrgUnitGUID());
		row.setOrgUnitID(apiKey.getOrgUnitID());			
		return row;
	}
	
	private ShipmentRow setupVersandOption(LieferscheinDto lsDto, ObjectFactory of, ShipmentRow row) {
		Lieferart lieferart = em.find(Lieferart.class, lsDto.getLieferartIId());
		if(lieferart == null || Helper.isStringEmpty(lieferart.getCExtern())) {
			throw EJBExcFactory.lieferartUngueltigFuerPlc(lsDto, lieferart);
		}
		row.setDeliveryServiceThirdPartyID(of.createShipmentRowDeliveryServiceThirdPartyID(lieferart.getCExtern()));
		row.setCustomDataBit1(false); // keine Retoursendung
		row.setCustomDataBit2(true); // Fehleretikett drucken 
		return row;
	}
	
	private ShipmentRow setupPrinter(LieferscheinDto lsDto, ObjectFactory of, ShipmentRow row) {
		PrinterRow printerRow = new PrinterRow();
		printerRow.setLabelFormatID(of.createPrinterRowLabelFormatID("100x150"));
		printerRow.setLanguageID(of.createPrinterRowLanguageID("pdf"));
		printerRow.setPaperLayoutID(of.createPrinterRowPaperLayoutID("A5"));
		row.setPrinterObject(of.createShipmentRowPrinterObject(printerRow));
		return row;
	}

	private ShipmentRow setupCollis(LieferscheinDto lsDto, ObjectFactory of, ShipmentRow row) {
		ArrayOfColloRow collis = new ArrayOfColloRow();

		for(int i = 0; i < lsDto.getIAnzahlPakete(); i++) {
			ColloRow collo  = new ColloRow();
			if(i == 0 && lsDto.getFGewichtLieferung() != null && lsDto.getFGewichtLieferung() > 0.005d) {
				String w = lsDto.getFGewichtLieferung().toString();
				collo.setWeight(of.createColloRowWeight(new BigDecimal(w)));
			}
			
			collis.getColloRow().add(collo);
		}

		if(collis.getColloRow().size() == 0) {
			myLogger.warn("Es gibt keine Paketanzahl fuer LS " + lsDto.getCNr() + "! Es wird 1 verwendet.");
		}
	
		row.setColloList(of.createShipmentRowColloList(collis));
		return row;
	}
	
	private ShipmentRow setupDeliveryDate(LieferscheinDto lsDto,
			ObjectFactory of, ShipmentRow row) throws DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();

		if(lsDto.getTLiefertermin() != null) {
			cal.setTimeInMillis(Helper.cutTimestamp(lsDto.getTLiefertermin()).getTime());
			XMLGregorianCalendar c = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			row.setShippingDateTimeFrom(of.createShipmentRowShippingDateTimeFrom(c));
			row.setShippingDateTimeTo(of.createShipmentRowShippingDateTimeTo(c));
		}
		
		return row;
	}
	
	private ShipmentRow setupRecipient(LieferscheinDto lsDto, 
			ObjectFactory of, ShipmentRow row, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
				lsDto.getKundeIIdLieferadresse(), theClientDto);
		Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
				.getLocaleCNrKommunikation());		
		AnsprechpartnerDto ansprechpartnerDto = null;
		if (lsDto.getAnsprechpartnerIId() != null) {
			ansprechpartnerDto = getAnsprechpartnerFac()
					.ansprechpartnerFindByPrimaryKey(
							lsDto.getAnsprechpartnerIId(),
							theClientDto);
		}
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		AddressRow adr = createAddressRow(of, kundeDto.getPartnerDto(),
				ansprechpartnerDto, mandantDto, locDruck, LocaleFac.BELEGART_LIEFERSCHEIN, theClientDto);
		row.setOURecipientAddress(of.createShipmentRowOURecipientAddress(adr));
		return row;
	}
	
	private ShipmentRow setupShipper(LieferscheinDto lsDto, ObjectFactory of, 
			ShipmentRow row, TheClientDto theClientDto) throws RemoteException {
		AnsprechpartnerDto ansprechpartnerDto = null;
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		Locale locDruck = theClientDto.getLocMandant();
		
		AddressRow adr = createAddressRow(of, mandantDto.getPartnerDto(),
				ansprechpartnerDto, mandantDto, locDruck, LocaleFac.BELEGART_LIEFERSCHEIN, theClientDto);
		row.setOUShipperAddress(of.createShipmentRowOUShipperAddress(adr));
		return row;
	}
	
	private AddressRow createAddressRow(ObjectFactory of, PartnerDto partnerDto,
			AnsprechpartnerDto ansprechpartnerDto, MandantDto mandantDto, 
			Locale locDruck, String belegartCnr, TheClientDto theClientDto) throws RemoteException {
		AddressBlockFormatter formatter = new AddressBlockFormatter(
				getSystemFac(), getBenutzerServicesFac(), getParameterFac(), getPartnerFac());
		AddressBlockFormatted address = formatter.formatAdresseFuerAusdruck(
				partnerDto, ansprechpartnerDto, mandantDto, locDruck, LocaleFac.BELEGART_LIEFERSCHEIN);
		AddressRow adr = new AddressRow();
		adr.setName1(of.createAddressRowName1(address.getContentFor(Tag.NAME1)));
		adr.setName2(of.createAddressRowName2(address.getContentFor(Tag.NAME2)));
		adr.setName3(of.createAddressRowName3(address.getContentFor(Tag.NAME3)));

		adr.setAddressLine1(of.createAddressRowAddressLine1(address.getContentFor(Tag.STRASSE)));
		if(partnerDto.getLandplzortDto() != null) {
			LandplzortDto lpoDto = partnerDto.getLandplzortDto();
			adr.setCity(of.createAddressRowCity(lpoDto.getOrtDto().getCName()));
			adr.setPostalCode(of.createAddressRowPostalCode(lpoDto.getCPlz()));
			adr.setCountryID(of.createAddressRowCountryID(lpoDto.getLandDto().getCLkz()));
		}
		
		adr.setTel1(of.createAddressRowTel1(normalizePhoneNumber(partnerDto.getCTelefon())));
		adr.setEmail(of.createAddressRowEmail(partnerDto.getCEmail()));
		return adr;
	}
	
	private String normalizePhoneNumber(String phonenumber) {
		if(Helper.isStringEmpty(phonenumber)) return null;
		
		return phonenumber
				.replace("+", "00")
				.replace("/", "")
				.replaceAll("-",  "");
	}

	private ShipmentRow setupReference(LieferscheinDto lsDto, ObjectFactory of, ShipmentRow row) {
		row.setOUShipperReference1(of.createShipmentRowOUShipperReference1(lsDto.getCNr()));
		return row;
	}

/*	
	private void post0(LieferscheinDto lsDto, ShipmentRow row) {
		try {
			ShippingService service = new ShippingService();
			IShippingService plcService = service.getBasicHttpsBindingIShippingService();
			Holder<String> zplLabelData = new Holder<String>();
			Holder<String> pdfData = new Holder<String>();
			Holder<String> errorCode = new Holder<String>();
			Holder<String> errorMessage = new Holder<String>();
			Holder<String> shipmentDocuments = new Holder<String>();
			Holder<ArrayOfColloRow> shipmentResult = new Holder<ArrayOfColloRow>();

			myLogger.info("plc import starting '" + lsDto.getCNr() + "'...");
			plcService.importShipment(row, shipmentResult, 
					zplLabelData, pdfData, shipmentDocuments, errorCode, errorMessage);
			myLogger.info("plc import done. errorcode='" + errorCode.value + "'");
		} catch(Exception e) {
			myLogger.error("postVersand", e);
		}
	}
*/

	private HttpResponse post(String uri, LieferscheinDto lsDto, ShipmentRow row) throws ClientProtocolException, IOException {
		ObjectFactory of = new ObjectFactory();
		ImportShipment shipment = new ImportShipment();
		shipment.setRow(of.createImportShipmentRow(row));
		String xml = toXmlString(shipment);
		xml = toSoap(xml);
		myLogger.info("plc about to post [" + xml + "]...");

//		HttpPost post = new HttpPost("https://plc.post.at/DataService/Post.Webservice/ShippingService.svc/secure");
		HttpPost post = new HttpPost(uri);
		post.setHeader("Content-Type", "text/xml;charset=UTF-8");
		post.setHeader("SOAPAction", "\"http://post.ondot.at/IShippingService/ImportShipment\"");

		myLogger.info("plc import starting '" + lsDto.getCNr() + "'...");
		byte[] xmlBytes = xml.getBytes("UTF-8");
		post.setEntity(new InputStreamEntity(new ByteArrayInputStream(xmlBytes), xmlBytes.length));
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);
		myLogger.info("plc import done. errorcode='" + response.getStatusLine().getStatusCode() + "'");
		myLogger.warn("response: " + response.toString());
		
		return response;
	}
	
	private PaketVersandAntwortDto processHttpResponse(HttpResponse response) throws IOException {
/*		 
		
		List<String> trackings = new ArrayList<String>();
		trackings.add("" + System.currentTimeMillis());
		PaketVersandAntwortDto dto = new PaketVersandAntwortDto(trackings);
		dto.setRawContent("blafaselblafaselblafasel");
		dto.setPdfContent(
				"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");
		return dto;
*/		
/*	
 */		
		if(response.getStatusLine().getStatusCode() != 200) {
			myLogger.error("Request has not successful (" +
					response.getStatusLine().getStatusCode() + ")");
			myLogger.error("Request answer" + response.getStatusLine().getReasonPhrase());
			return PackageResponseFactory.httpStatusWrong(response);
		}
		
		InputStream is = response.getEntity().getContent();
		String s = streamToString(is);
		myLogger.warn("result: [" + s + "]");
		return handleResponse(s);
					
	}
	
	private PaketVersandAntwortDto handleResponse(String responseContent) {
//		String s = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><ImportShipmentResponse xmlns=\"http://post.ondot.at\"><ImportShipmentResult xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><ColloRow><ColloArticleList i:nil=\"true\"/><ColloCodeList><ColloCodeRow><Code>1028610500000020189906</Code><NumberTypeID>213</NumberTypeID><OUCarrierThirdPartyID>OEPAG-DEF</OUCarrierThirdPartyID></ColloCodeRow></ColloCodeList><Height i:nil=\"true\"/><Length i:nil=\"true\"/><Weight i:nil=\"true\"/><Width i:nil=\"true\"/></ColloRow></ImportShipmentResult><zplLabelData i:nil=\"true\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/><pdfData i:nil=\"true\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/><shipmentDocuments i:nil=\"true\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/><errorCode i:nil=\"true\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/><errorMessage i:nil=\"true\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"/></ImportShipmentResponse></s:Body></s:Envelope>";
		String xml = stripSoapBody(responseContent);
		ImportShipmentResponse r = fromXmlResponse(xml);
		myLogger.info("response = " + (r != null ? r.toString() : "null"));
		PaketVersandAntwortDto response = transform(r).setRawContent(xml);
		return response;
	}
	
	private String streamToString(InputStream stream) {
		 final char[] buffer = new char[4096];
		 final StringBuilder sb = new StringBuilder();
		 try {
			 Reader in = new InputStreamReader(stream, "UTF-8");
			 for(;;) {
				 int bytesRead = in.read(buffer, 0, buffer.length);
				 if(bytesRead < 0) break;
				 sb.append(buffer, 0, bytesRead);
			 }
		 } catch(UnsupportedEncodingException e) {
			 myLogger.error("encoding", e);
		 } catch(IOException e) {
			 myLogger.error("io", e);
		 }
		 return sb.toString();
	}

	private PaketVersandAntwortDto transform(ImportShipmentResponse r) {
		if(r == null) {
			return PackageResponseFactory.shipmentResponseNull();
		}
		
		if(r.getErrorCode().getValue() != null) {
			PaketVersandAntwortDto value = new PaketVersandAntwortDto(
					r.getErrorCode().getValue(), 
					r.getErrorMessage().getValue());
			myLogger.warn("ImportShipmentResponse error '" + value.getErrorCode() + "'.");
			myLogger.warn("ImportShipmentResponse message '" + value.getErrorMessage() + "'.");
			return value;
		}
		
		ArrayOfColloRow colloRows = r.getImportShipmentResult().getValue();
		if(colloRows == null) {
			myLogger.warn("ImportShipmentResponse: no result!");
			return PackageResponseFactory.shipmentResultNull();
		}
		
		List<String> sendungsNummern = new ArrayList<String>();
		for (ColloRow colloRow : colloRows.getColloRow()) { 
			ArrayOfColloCodeRow a = colloRow.getColloCodeList().getValue();
			for(ColloCodeRow codeRow : a.getColloCodeRow()) {
				sendungsNummern.add(codeRow.getCode().getValue());
			}
		}
		
		PaketVersandAntwortDto value = new PaketVersandAntwortDto(sendungsNummern);
		value.setPdfContent(r.getPdfData().getValue());
		return value;
		
//		if(pdf64 != null) {
//			byte[] pdf = Base64.decodeBase64(pdf64.getBytes());		
//		}
	}
	
	private String stripSoapBody(String soapString) {
		int responseIndex = soapString.indexOf("<ImportShipmentResponse xmlns");
		String s = soapString.substring(responseIndex);
		int endBodyIndex = s.indexOf("</s:Body></s:Envelope>");
		s = s.substring(0, endBodyIndex);
		return s;
	}
	
	private ImportShipmentResponse fromXmlResponse(String xmlString) {
		try {
			JAXBContext context = JAXBContext.newInstance(ImportShipmentResponse.class.getPackage().getName());
			Unmarshaller m = context.createUnmarshaller();
			Reader r = new StringReader(xmlString);
			ImportShipmentResponse response = (ImportShipmentResponse)m.unmarshal(r);
			return response;
		} catch (JAXBException e) {
			myLogger.error("unmarshall", e);
		}
		
		return null;
	}
	
	private String toXmlString(ImportShipment shipment) {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(shipment.getClass().getPackage().getName());
			Marshaller m = context.createMarshaller();
			m.marshal(shipment, writer);
			String s = writer.toString();
			return s;
		} catch (JAXBException e) {
			System.out.println("JAXBException" + e.getMessage());
		}

		return null;
	}
	
	private String toSoap(String xml) {
		int index = xml.indexOf("<row>");
		String stripped = xml.substring(index);
		String s = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns=\"http://post.ondot.at\">\n"
				+ "<soapenv:Header/>\n"
				+ "<soapenv:Body>\n"
				+ "<ImportShipment>" + stripped + "\n"
				+ "</soapenv:Body>\n" 
				+ "</soapenv:Envelope>";
		return s;
	}
}
