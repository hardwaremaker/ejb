package com.lp.server.rechnung.bl;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.eingangsrechnung.bl.SepaXmlMarshaller;
import com.lp.server.finanz.service.SepaXmlExportResult;
import com.lp.server.rechnung.ejbfac.SepaExportPain008Fac;
import com.lp.server.rechnung.ejbfac.SepaExportPain008V02;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.sepa.errors.SepaException;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.FacLookup;
import com.lp.util.EJBExceptionLP;

public class SepaPain008Exporter implements ISepaPain008Exporter {

	public SepaPain008Exporter() {
	}

	public SepaXmlExportResult doExport(List<LastschriftvorschlagDto> lastschriftvorschlaege, 
			TheClientDto theClientDto) {
		SepaExportPain008Fac transformer = getTransformer();
		
		try {
			Object transformedObject = transformer.transform(lastschriftvorschlaege, theClientDto);
			SepaXmlMarshaller marshaller = new SepaXmlMarshallerPain008V02();
			return new SepaXmlExportResult(marshaller.marshal(transformedObject));
		} catch (SepaException e) {
			SepaXmlExportResult result = new SepaXmlExportResult();
			result.getSepaErrors().add(e);
			return result;
		} catch (JAXBException e) {
			throw new EJBExceptionLP(e);
		} catch (SAXException e) {
			throw new EJBExceptionLP(e);
		}
	}
	
	@Override
	public SepaXmlExportResult checkExport(List<LastschriftvorschlagDto> lastschriftvorschlaege,
			TheClientDto theClientDto) {
		SepaExportPain008Fac transformer = getTransformer();
		
		SepaXmlExportResult result = new SepaXmlExportResult();
		result.setSepaErrors(transformer.checkData(lastschriftvorschlaege, theClientDto));
		return result;
	}
	
	private SepaExportPain008Fac getTransformer() {
		try {
			InitialContext context = new InitialContext();
			SepaExportPain008Fac transformer = FacLookup.lookupLocal(context, SepaExportPain008V02.class, SepaExportPain008Fac.class);
			return transformer;
		} catch (NamingException e) {
			throw new EJBExceptionLP(e);
		}
	}
}
