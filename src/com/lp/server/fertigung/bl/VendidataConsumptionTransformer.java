package com.lp.server.fertigung.bl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.fertigung.service.VendidataArticleConsumption;
import com.lp.server.schema.vendidata.articleconsumption.XMLArticleConsumption;
import com.lp.server.schema.vendidata.articleconsumption.XMLArticleConsumptions;
import com.lp.server.schema.vendidata.commonobjects.XMLBaseArticleData;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataImportExceptionLP;
import com.lp.util.VendidataXMLFieldInstantiator;

public class VendidataConsumptionTransformer {
	
	private VendidataConsumptionUnmarshaller unmarshaller;
	private XMLArticleConsumptions xmlHead;
	private final Integer GueltigeNummerischeTourlaenge = 5; 

	public VendidataConsumptionTransformer() {
		unmarshaller = new VendidataConsumptionUnmarshaller();
		xmlHead = new XMLArticleConsumptions();
	}
	
	public List<VendidataArticleConsumption> transform(String xmlDaten) {
		try {
			xmlHead = unmarshaller.unmarshal(xmlDaten);
		} catch (JAXBException e) {
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_UNMARSHAL_FEHLGESCHLAGEN, e);
		} catch (SAXException e) {
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_UNMARSHAL_FEHLGESCHLAGEN, e);
		}
		
		try {
			VendidataXMLFieldInstantiator<XMLArticleConsumptions> fieldInstatiator = 
					new VendidataXMLFieldInstantiator<XMLArticleConsumptions>();
			xmlHead = fieldInstatiator.instantiateNullFields(xmlHead);
		} catch (Exception e) {
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_FIELD_INSTANZIERUNG_FEHLGESCHLAGEN, e);
		}
		
		return transformImpl();
	}

	private List<VendidataArticleConsumption> transformImpl() {
		List<VendidataArticleConsumption> vacList = new ArrayList<VendidataArticleConsumption>();
		
		for (XMLArticleConsumption xmlAC : xmlHead.getArticleConsumptions()) {
			VendidataArticleConsumption vac = new VendidataArticleConsumption();
			vac.setBuchungsdatum(new Timestamp(xmlAC.getBookingDate().toGregorianCalendar().getTimeInMillis()));
			vac.setAnzahlArtikel(xmlAC.getNumberOfArticles());
			vac.setTourNr(extractTourNummer(xmlAC.getSourceStockID()));
			vac.setDebitorennummer(Integer.toString(xmlAC.getCustomer().getCustomerId()));
			vac.setFourVendingIId(extractArtikelId(xmlAC));
			vac.setArtikelName(extractArtikelName(xmlAC));
			
			vac.setRowId(xmlAC.getRowid());
			vac.setCustomerId(xmlAC.getCustomer().getCustomerId());
			vac.setEmployeeId(xmlAC.getEmployee().getEmployeeID());
			String firstname = xmlAC.getEmployee().getFirstname();
			String lastname = xmlAC.getEmployee().getLastname();
			vac.setEmployeeName(firstname != null ? firstname + " " + lastname : lastname);
			
			vacList.add(vac);
		}
		
		return vacList;
	}

	private Integer extractTourNummer(Integer sourceStockId) {
		if (sourceStockId == null) return null;
		String tourNr = Integer.toString(sourceStockId);
		tourNr = tourNr.length() > GueltigeNummerischeTourlaenge ? 
				tourNr.substring(tourNr.length() - GueltigeNummerischeTourlaenge) : tourNr;

		return Integer.parseInt(tourNr);
	}

	private String extractArtikelName(XMLArticleConsumption xmlAC) {
		for (Object articleData : xmlAC.getArticle().getBaseArticleDataOrSalesArticleData()) {
			if (articleData instanceof XMLBaseArticleData) {
				return ((XMLBaseArticleData) articleData).getBaseArticleName();
			} 
		}
		
		return "";
	}

	private Integer extractArtikelId(XMLArticleConsumption xmlAC) {
		
		for (Object articleData : xmlAC.getArticle().getBaseArticleDataOrSalesArticleData()) {
			if (articleData instanceof XMLBaseArticleData) {
				return ((XMLBaseArticleData) articleData).getBaseArticleId();
			}
		}
		
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_BASEARTIKELID_NICHT_VORHANDEN, "", new Object[] {xmlAC.getRowid()});
	}

}
