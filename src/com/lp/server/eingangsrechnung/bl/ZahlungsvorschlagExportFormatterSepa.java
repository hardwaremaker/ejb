package com.lp.server.eingangsrechnung.bl;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.eingangsrechnung.service.SepaExportTransformerFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.Iso20022SchemaEnum;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.FacLookup;
import com.lp.util.EJBExceptionLP;

public class ZahlungsvorschlagExportFormatterSepa extends
		ZahlungsvorschlagExportFormatter {

	public ZahlungsvorschlagExportFormatterSepa(TheClientDto theClientDto)
			throws EJBExceptionLP {
		super(theClientDto);
	}
	
	@Override
	public ZahlungsvorschlagExportResult exportiereDaten(ZahlungsvorschlaglaufDto laufDto,
			ZahlungsvorschlagDto[] zahlungsvorschlagDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		
		try {
			Iso20022BankverbindungDto isoBv = getSepaImportFac().iso20022BankverbindungFindByBankverbindungIIdNoExc(
					new BankverbindungId(laufDto.getBankverbindungIId()));
			if (isoBv == null) {
				BankverbindungDto bankverbindungDto = getFinanzFac().bankverbindungFindByPrimaryKey(laufDto.getBankverbindungIId());
				throw EJBExcFactory.bankverbindungKeinIso20022StandardDefiniert(bankverbindungDto);
			}

			Iso20022TransformerFactory iso20022factory = new Iso20022TransformerFactory();
			SepaExportTransformerFac transformer = iso20022factory.getTransformer(
					isoBv.getStandardEnum(), isoBv.getZahlungsauftragSchemaDto().getSchema());
			Object transformedObject = transformer.transform(laufDto, zahlungsvorschlagDtos, theClientDto);
			SepaXmlMarshaller marshaller = iso20022factory.getMarshaller(
					isoBv.getStandardEnum(), isoBv.getZahlungsauftragSchemaDto().getSchema());
			String sepaXml = marshaller.marshal(transformedObject);
			return new ZahlungsvorschlagExportResult(sepaXml);
		} catch (JAXBException e) {
			throw new EJBExceptionLP(e);
		} catch (SAXException e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	@Override
	protected String getXSLFile() {
		return "";
	}

	@Override
	protected String exportiereUeberschrift() throws EJBExceptionLP {
		return "";
	}

	public class Iso20022TransformerFactory {
		public SepaExportTransformerFac getTransformer(Iso20022StandardEnum standardEnum, Iso20022SchemaEnum schemaEnum) {
			if (Iso20022StandardEnum.SEPA.equals(standardEnum)) {
				return getSepaTransformer(schemaEnum);
			}
			
			if (Iso20022StandardEnum.SWISS.equals(standardEnum)) {
				return getSwissTransformer(schemaEnum);
			}
			// TODO
			return null;
		}

		private SepaExportTransformerFac getSwissTransformer(Iso20022SchemaEnum schemaEnum) {
			try {
				InitialContext context = new InitialContext();
				if (Iso20022SchemaEnum.PAIN00100103.equals(schemaEnum)) {
					return FacLookup.lookupLocal(context, SepaExportTransformerPain001V03CH.class, SepaExportTransformerFac.class);
				}
			} catch (NamingException e) {
				throw new EJBExceptionLP(e);
			}
			return null;
		}

		private SepaExportTransformerFac getSepaTransformer(Iso20022SchemaEnum schemaEnum) {
			try {
				InitialContext context = new InitialContext();
				if (Iso20022SchemaEnum.PAIN00100103.equals(schemaEnum)) {
					return FacLookup.lookupLocal(context, SepaExportTransformerPainV03.class, SepaExportTransformerFac.class);
				}
			} catch (NamingException e) {
				throw new EJBExceptionLP(e);
			}
			return null;
		}

		public SepaXmlMarshaller getMarshaller(Iso20022StandardEnum standardEnum, Iso20022SchemaEnum schemaEnum) {
			if (Iso20022StandardEnum.SEPA.equals(standardEnum)) {
				return getSepaMarshaller(schemaEnum);
			}
			
			if (Iso20022StandardEnum.SWISS.equals(standardEnum)) {
				return getSwissMarshaller(schemaEnum);
			}
			// TODO
			return null;
		}

		private SepaXmlMarshaller getSwissMarshaller(Iso20022SchemaEnum schemaEnum) {
			if (Iso20022SchemaEnum.PAIN00100103.equals(schemaEnum)) {
				return new SepaXmlMarshallerPain001V03CH();
			}
			return null;
		}

		private SepaXmlMarshaller getSepaMarshaller(Iso20022SchemaEnum schemaEnum) {
			if (Iso20022SchemaEnum.PAIN00100103.equals(schemaEnum)) {
				return new SepaXmlMarshallerPain001V03();
			}
			return null;
		}
	}
}
