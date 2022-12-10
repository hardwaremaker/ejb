/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.ArtikelQuery;
import com.lp.server.artikel.ejb.LagerbewegungQuery;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.WebshopOrderServiceFacLocal;
import com.lp.server.auftrag.service.WebshopPartner;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.KundesokoQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLADDRESS;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLARTICLEPRICE;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLBUYERAID;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLBUYERPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLCONTACT;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLDELIVERYPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLINVOICEPARTY;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERITEMTEXT;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERPARTIES;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERPOSTEXTLIST;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSE;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEHEADTEXTLIST;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEITEM;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLORDERRESPONSEITEMLIST;
import com.lp.server.schema.opentrans.cc.orderresponse.XMLXMLPARTY;
import com.lp.server.system.ejbfac.CleverCureProducer;
import com.lp.server.system.service.HeliumSimpleAuthController;
import com.lp.server.system.service.HeliumTokenAuthController;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.system.service.VersandwegPartnerCCDto;
import com.lp.server.system.service.VersandwegPartnerCCDto.SokoAdresstyp;
import com.lp.server.util.HelperWebshop;
import com.lp.util.Helper;

@Stateless
public class WebshopOrderServiceEjb extends WebshopOrderServiceBase implements WebshopOrderServiceFacLocal {

	private final static int MAX_DESCRIPTION_LENGTH = 40 ;
	
	private HeliumSimpleAuthController authController = null ; 

	@Override
	protected Object unmarshalImpl(String xmlOpenTransOrder) throws SAXException, JAXBException {
		XMLXMLORDERRESPONSE openTransOrder = unmarshal(null, xmlOpenTransOrder, XMLXMLORDERRESPONSE.class) ;
		if(null != openTransOrder) {
			setupFinderFactory(openTransOrder) ;
		}

		return openTransOrder ;
	}

	private void setupFinderFactory(XMLXMLORDERRESPONSE convertedOrder) {
		setOrderAdapter(new CCOrderAdapter(convertedOrder)) ;
		setKundeFinder(new KundeFinder17632(convertedOrder)) ;	
		setAuftragFinder(new AuftragFinder17632(convertedOrder)) ;
	}

	private void setupArtikelFinder(XMLXMLORDERRESPONSEITEM convertedItem) {
		setArtikelFinder(new ArtikelFinder17632(convertedItem)) ;
	}
	
	@Override
	protected KundeDto createEjbCustomerImpl() {
		WebshopPartner webPartner = getKundeFinder().findKunde() ;
		return webPartner != null ? webPartner.getKundeDto() : null ;	
	}


	@Override
	protected AuftragDto createEjbOrderImpl(KundeDto kundeDto) {
		AuftragDto auftragDto = getAuftragFinder().createAuftrag(kundeDto) ;
		return auftragDto ;
	}


	@Override
	protected void createEjbPositionsImpl(KundeDto kundeDto, AuftragDto auftragDto) {
		getAuftragFinder().createPositions(kundeDto, auftragDto) ;
	}


	protected Integer getKundeIIdRechnungsadresse(KundeDto kundeDto, IXmlOrderAdapter orderAdapter) {
		String uidNummer = orderAdapter.getUidNummerFromInvoicePartner() ;
		if(null == uidNummer || 0 == uidNummer.trim().length()) {
			myLogger.info("Es gab keine InvoiceParty, verwende daher die Kundenadresse als Rechnungsadresse") ;
			return kundeDto.getIId() ;
		}
		
		KundeDto invoiceKundeDto = findByUidNummer(uidNummer) ;
		return null == invoiceKundeDto ? kundeDto.getIId() : invoiceKundeDto.getIId() ;
	}
	

	private Double getWechselkursMandantWaehrungzuBelegWaehrung(XMLXMLORDERRESPONSE xmlOrder) {
		return new Double(1.00) ;
	}
	
	private KundeDto createOrGetEjbKunde(XMLXMLORDERRESPONSE xmlOrder) throws RemoteException {
		XMLXMLORDERPARTIES orderParties = xmlOrder
			.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
		XMLXMLBUYERPARTY buyerParty = orderParties.getBUYERPARTY() ;
		PartnerDto[] partners = getPartnerFac().partnerFindByName1(
			buyerParty.getPARTY().getADDRESS().getNAME(), getAuthController().getWebClientDto()) ;
		if(partners.length == 0) {
			XMLXMLADDRESS address = buyerParty.getPARTY().getADDRESS() ;
			PartnerDto partnerDto = new PartnerDto() ;
			partnerDto.setCName1nachnamefirmazeile1(address.getNAME()) ;
			partnerDto.setCKbez(buyerParty.getPARTY().getPARTYID().getValue()) ;
			partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE) ;
			partnerDto.setBVersteckt(false) ;
			partnerDto.setLocaleCNrKommunikation(getAuthController().getWebClientDto().getLocMandantAsString()) ;

			partners = new PartnerDto[] { partnerDto } ;
		}
		
		KundeDto kundeDto = null ;
		
		if(partners[0].getIId() != null) {
			kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
					partners[0].getIId(), getAuthController().getWebClientDto().getMandant(), getAuthController().getWebClientDto()) ;			
		}
		
		if(partners[0].getIId() == null || kundeDto == null) {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
							getAuthController().getWebClientDto().getMandant(), getAuthController().getWebClientDto());
			
			kundeDto = new KundeDto() ;
			kundeDto.setPartnerDto(partners[0]) ;
			kundeDto.setMandantCNr(getAuthController().getWebClientDto().getMandant()) ;
			kundeDto.setCWaehrung(mandantDto.getWaehrungCNr()) ;
			kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz()) ;
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId()) ;
			kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde()) ;
			kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde()) ;
			kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde()) ;
			kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
			kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
			kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
			kundeDto.setBDistributor(Helper.boolean2Short(false));
			kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
			kundeDto.setbIstinteressent(Helper.boolean2Short(false));
			kundeDto.setBLsgewichtangeben(Helper.boolean2Short(false));
			kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));

			// Default 'Akzeptiert Teillieferung'
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(getAuthController().getWebClientDto().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN);					
			kundeDto.setBAkzeptiertteillieferung(new Short(parameter.getCWert()));
			kundeDto.setBLsgewichtangeben(Helper.boolean2Short(false));

			Integer personalId = getAuthController().getWebClientDto().getIDPersonal() ;
			kundeDto.setPersonaliIdProvisionsempfaenger(personalId) ;
			kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());

			kundeDto.setPersonalAnlegenIID(personalId) ;
			kundeDto.setPersonalAendernIID(personalId) ;
			Timestamp t = getTimestamp() ;
			kundeDto.setTAnlegen(t) ;
			kundeDto.setTAendern(t) ;
			Integer kundeId = getKundeFac().createKunde(kundeDto, getAuthController().getWebClientDto()) ;
			kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, getAuthController().getWebClientDto()) ;
		}

		return kundeDto ;
	}
	

	protected HeliumSimpleAuthController getAuthController() {
		if(null == authController) {
//			authController = new HeliumSimpleAuthController(getBenutzerFac(), getMandantFac()) ;
			authController = new HeliumTokenAuthController(
					getBenutzerFac(), getMandantFac(), getPersonalFac()) ;
		}
		return authController ;
	}	
	
	
	class AuftragFinder17632 implements AuftragFinder {
		private Timestamp earliestDeliveryTime = null ;
		private Timestamp latestDeliveryTime = null ;
		private int nachkommastellenPreiseVK = -1 ;
		
		private XMLXMLORDERRESPONSE openTransOrder ;
		private CleverCureProducer ccproducer = new CleverCureProducer() ;
		
		public AuftragFinder17632(XMLXMLORDERRESPONSE convertedOrder) {
			openTransOrder = convertedOrder ;
		}
		
		private XMLXMLORDERRESPONSE getXmlOrder() {
			return openTransOrder ;
		}
		
		private void resetDeliveryTimestamps() {
			earliestDeliveryTime = null ;
			latestDeliveryTime = null ;			
		}
		
		private void updateEarliestDeliveryTime(Timestamp timestamp) {
			if(earliestDeliveryTime == null || earliestDeliveryTime.after(timestamp)) {
				earliestDeliveryTime = timestamp ;
			}
		}
		
		private void updateLatestDeliveryTime(Timestamp timestamp) {
			if(null == latestDeliveryTime || latestDeliveryTime.before(timestamp)) {
				latestDeliveryTime = timestamp ;
			}
		}

		private Timestamp getEarliestDeliveryTime() {
			return null == earliestDeliveryTime ? new Timestamp(System.currentTimeMillis()) : earliestDeliveryTime ;
		}
		
		private Timestamp getLatestDeliveryTime() {
			return null == latestDeliveryTime ? new Timestamp(System.currentTimeMillis()) : latestDeliveryTime ;
		}
	
		private void postMessage(String subject, String message) throws RemoteException {
			VersandauftragDto dto = new VersandauftragDto();
			dto.setCBetreff(subject);
			dto.setCText(message);

			TheClientDto clientDto = getAuthController().getWebClientDto() ;
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					clientDto.getMandant(), clientDto);
			
			ParametermandantDto paramDto = getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_MAILADRESSE_ADMIN, 
					ParameterFac.KATEGORIE_VERSANDAUFTRAG, clientDto.getMandant()) ;
			String absender = "admin@heliumv.com" ;
			if(!isEmptyString(paramDto.getCWert())) {
				absender = paramDto.getCWert() ;
			} else {
				if(mandantDto.getPartnerDto() != null && !isEmptyString(mandantDto.getPartnerDto().getCEmail())) {
					absender = mandantDto.getPartnerDto().getCEmail() ;
				}
			}
			dto.setCAbsenderadresse(absender) ;
			
			String empfaenger = absender ;
			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(clientDto.getIDPersonal(), clientDto) ;
			empfaenger = personalDto.getCEmail() ;
			dto.setCEmpfaenger(empfaenger) ;
			
			getVersandFac().createVersandauftrag(dto, false, clientDto) ;
		}

		private void postMessageAuftragBekannt(String orderCnr, String bestellNr) {
			TheClientDto theClientDto = getAuthController().getWebClientDto() ;
			String subject = getTextRespectUISpr("auft.cc.email.bekannt.header",
					theClientDto.getMandant(), theClientDto.getLocUi(), orderCnr) ;
			String message = getTextRespectUISpr("auft.cc.email.bekannt.message",
					theClientDto.getMandant(), theClientDto.getLocUi(), 
					orderCnr, bestellNr) ;
			
			try {
				postMessage(subject, message);
			} catch(RemoteException e) {}			
		}
	
		private void postMessageAuftragNeu(AuftragDto auftragDto)
				throws RemoteException {
			TheClientDto theClientDto = getAuthController().getWebClientDto() ;
			String subject = getTextRespectUISpr("auft.cc.email.neu.header",
					theClientDto.getMandant(), theClientDto.getLocUi(), auftragDto.getCNr()) ;
			String message = getTextRespectUISpr("auft.cc.email.neu.message",
					theClientDto.getMandant(), theClientDto.getLocUi(), 
					auftragDto.getCNr(), auftragDto.getCBestellnummer(), 
					auftragDto.getDLiefertermin().toString(),
					auftragDto.getDFinaltermin().toString()) ;
			postMessage(subject, message);
		}
		
		@Override
		public AuftragDto[] findAuftrag(KundeDto kundeDto) {
			if(null == kundeDto) return null ;
			
			if(!getOrderAdapter().hasBestellnummer()) {
				myLogger.error("Die XML-Struktur enth\u00E4lt keine auswertbare OrderResponseInfo/OrderId") ;
				return null ;
			}
			
			AuftragDto[] auftragDtos = null ;
			
			try {
				String bestellnr = getOrderAdapter().getBestellnummer() ;
				auftragDtos = getAuftragFac().auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(
						kundeDto.getIId(), getAuthController().getWebClientDto().getMandant(), bestellnr) ;
			} catch(RemoteException e) {				
			}
			
			return auftragDtos ;
		}


		private Partner possibleMatchPartner(XMLXMLADDRESS xmlAddress, List<Partner> partners) {
			String exName2 = ccencode(xmlAddress.getNAME2()) ;
			String exStreet = ccencode(xmlAddress.getSTREET()) ;
			
			for (Partner partner : partners) {
				if(!hvencode(partner.getCName2vornamefirmazeile2()).equalsIgnoreCase(exName2)) {
					continue ;
				}
				if(!hvencode(partner.getCStrasse()).equalsIgnoreCase(exStreet)) {
					continue ;
				}
				
				return partner ;
			}
			
			return null ;
		}


		private String ccencode(String value) {
			return HelperWebshop.removeUmlauts(
					HelperWebshop.reencode(emptyString(value))) ;
		}
		
		private String hvencode(String value) {
			return HelperWebshop.removeUmlauts(emptyString(value)) ;
		}
		
		private void addUnknownAddress(String msg, XMLXMLADDRESS xmlAddress) {
			addChangeEntry(
					new ChangeEntry<String>("", xmlAddress.getNAME(), msg + ":\n" + 
							xmlAddress.getNAME() + "\n" +
							emptyString(xmlAddress.getNAME2()) + "\n" +
							emptyString(xmlAddress.getNAME3()) + "\n" +
							emptyString(xmlAddress.getSTREET()) + "\n" +
							emptyString(xmlAddress.getCCCOUNTRY()) + " " + emptyString(xmlAddress.getZIP()) + " " + emptyString(xmlAddress.getCITY()) + "\n")) ;			
		}
		
		private Integer getKundeIIdLieferadresse(KundeDto kundeDto, XMLXMLORDERRESPONSE xmlOrder) {
			XMLXMLORDERPARTIES orderParties = xmlOrder
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			if(null == orderParties || null == orderParties.getSHIPMENTPARTIES()) return kundeDto.getIId() ;
			
			XMLXMLDELIVERYPARTY deliveryParty = orderParties.getSHIPMENTPARTIES().getDELIVERYPARTY() ;
			if(null == deliveryParty) return kundeDto.getIId() ;

			KundeDto lieferKundeDto = findKundeFromXmlAddress(
					deliveryParty.getPARTY().getADDRESS(), kundeDto, "Die folgende Lieferadresse") ;
			
			return lieferKundeDto == null ? kundeDto.getIId() : lieferKundeDto.getIId() ;			
//			Integer iid = null ;
//			
//			try {
//				XMLXMLADDRESS xmlAddress = deliveryParty.getPARTY().getADDRESS() ;
//
//				List<Partner> partners = PartnerQuery.listByLowerCName1(em, xmlAddress.getNAME());
//				Partner foundPartner = possibleMatchPartner(xmlAddress, partners) ;				
//				if(foundPartner == null) {
//					addUnknownAddress("Die folgende Lieferadresse {1} konnte nicht im System gefunden werden", xmlAddress) ;
//				} else {
//					KundeDto lieferKundeDto = getKundeFac()
//						.kundeFindByiIdPartnercNrMandantOhneExc(foundPartner.getIId(), kundeDto.getMandantCNr(), getAuthController().getWebClientDto()) ;
//					if(lieferKundeDto == null) {
//						addUnknownAddress("Die folgende Lieferadresse {1} existiert als Partner, ein Kunde existiert jedoch nicht", xmlAddress) ;
//					} else {
//						iid = lieferKundeDto.getIId() ;						
//					}
//				}
//			} catch(NoResultException e) {				
//			} catch(RemoteException e) {				
//			}
//			return iid == null ? kundeDto.getIId() : iid ;
		}
		
		private Integer getKundeIIdRechnungadresse(KundeDto kundeDto, IXmlOrderAdapter orderAdapter, XMLXMLORDERRESPONSE xmlOrder) {
			String uidNummer = orderAdapter.getUidNummerFromInvoicePartner() ;
			if(!isEmptyString(uidNummer)) {
				KundeDto invoiceKundeDto = findByUidNummer(uidNummer) ;
				if(invoiceKundeDto != null) return invoiceKundeDto.getIId() ;				
			}
			
			XMLXMLORDERPARTIES orderParties = xmlOrder
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			if(null == orderParties || null == orderParties.getINVOICEPARTY()) return kundeDto.getIId() ;
			
			XMLXMLINVOICEPARTY invoiceParty = orderParties.getINVOICEPARTY() ;
			if(null == invoiceParty) return kundeDto.getIId() ;

			KundeDto rechnungKundeDto = findKundeFromXmlAddress(
					invoiceParty.getPARTY().getADDRESS(), kundeDto, "Die folgende Rechnungsadresse") ;
			
			return rechnungKundeDto == null ? kundeDto.getIId() : rechnungKundeDto.getIId() ;			
		}

		
		private KundeDto findKundeFromXmlAddress(XMLXMLADDRESS xmlAddress, KundeDto baseKundeDto, String prefixMessage) {			
			try {
				List<Partner> partners = PartnerQuery.listByLowerCName1(em, xmlAddress.getNAME());
				Partner foundPartner = possibleMatchPartner(xmlAddress, partners) ;				
				if(foundPartner == null) {
					addUnknownAddress(prefixMessage + 
							" {1} konnte nicht im System gefunden werden", xmlAddress) ;
				} else {
					KundeDto lieferKundeDto = getKundeFac()
						.kundeFindByiIdPartnercNrMandantOhneExc(foundPartner.getIId(),
								baseKundeDto.getMandantCNr(), getAuthController().getWebClientDto()) ;
					if(lieferKundeDto == null) {
						addUnknownAddress(prefixMessage +
								" {1} existiert als Partner, ein Kunde existiert jedoch nicht", xmlAddress);
						return lieferKundeDto;
					}
	
					if(Helper.isTrue(lieferKundeDto.getBVersteckterlieferant())) {
						myLogger.warn("Fuer den Partner (Id:" + foundPartner.getIId() + 
								") gibt es einen versteckten Lieferanten (Id:" + lieferKundeDto.getIId() +"). Nicht gewaehlt.");
						addUnknownAddress(prefixMessage + " {1} existiert als Partner, ein Kunde existiert jedoch nicht", xmlAddress);
						lieferKundeDto = null;
					}
					return lieferKundeDto ;
				}
			} catch(NoResultException e) {				
			} catch(RemoteException e) {				
			}

			return null ;
		}
		
		@Override
		public AuftragDto createAuftrag(KundeDto kundeDto) {
			AuftragDto[] dtos = findAuftrag(kundeDto) ;
			if(dtos != null && dtos.length > 0) {
				myLogger.warn("Der Auftrag '" + dtos[0].getCNr()
						+ "' existiert mit der Bestellnummer '" 
						+ dtos[0].getCBestellnummer() 
						+ "'. Kein weiterer Auftrag angelegt!") ;

				postMessageAuftragBekannt(dtos[0].getCNr(), dtos[0].getCBestellnummer());
				return null ;
			}

			String orderCurrency = getOrderAdapter().getWaehrung() ;
			if(!isValidCurrency(kundeDto, orderCurrency)) {
				addChangeEntry(new ChangeEntry<String>(kundeDto.getCWaehrung(), orderCurrency, 
					"Die Auftragsw\u00E4hrung {1} entspricht nicht der Kundenw\u00E4hrung {0}")) ;
			}
						
			AuftragDto returnDto = null ;

			try {
				AuftragDto auftragDto = buildAuftragDto(kundeDto, getOrderAdapter()) ;
				auftragDto.setKundeIIdRechnungsadresse(getKundeIIdRechnungadresse(kundeDto, getOrderAdapter(), getXmlOrder()));
				auftragDto.setKundeIIdLieferadresse(getKundeIIdLieferadresse(kundeDto, getXmlOrder())) ;				
				auftragDto.setCBezProjektbezeichnung(kundeDto.getCLieferantennr()) ;
				
				ExternerAnsprechpartner ansprPartner = getAnsprechpartnerIIdAuftrag(kundeDto, getXmlOrder()) ;
				auftragDto.setAnsprechpartnerIId(ansprPartner.getAnsprechpartnerIId()) ;
				if(ansprPartner.hasKeinHVAnsprechpartner()) {
					addChangeEntry(new ChangeEntry<String>(ansprPartner.getFremdsystemnr(), "", 
							"F\u00FCr die externe Personalnummer {0} gibt es keinen Ansprechpartner in der Kundenadresse. " +
							"Externe Kontaktinfo:\n" + ansprPartner.getContactInfo())) ;
				}
				
				KundeDto lieferKundeDto = kundeDto ;
				if(!kundeDto.getIId().equals(auftragDto.getKundeIIdLieferadresse())) {
					lieferKundeDto = getKundeFac()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdLieferadresse(), getAuthController().getWebClientDto()) ;
				}
				ansprPartner = getAnsprechpartnerIIdLieferung(lieferKundeDto, getXmlOrder()) ;
				auftragDto.setAnsprechpartnerIIdLieferadresse(ansprPartner.getAnsprechpartnerIId());
				if(ansprPartner.hasKeinHVAnsprechpartner()) {
					addChangeEntry(new ChangeEntry<String>(ansprPartner.getFremdsystemnr(), "", 
							"F\u00FCr die externe Personalnummer {0} gibt es keinen Ansprechpartner in der Lieferadresse. " +
							"Externe Kontaktinfo:\n" + ansprPartner.getContactInfo())) ;
				}				

				KundeDto rechnungsKundeDto = kundeDto ;
				if(!kundeDto.getIId().equals(auftragDto.getKundeIIdRechnungsadresse())) {
					rechnungsKundeDto = getKundeFac()
							.kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse(), getAuthController().getWebClientDto()) ;
				}
				
				ansprPartner = getAnsprechpartnerIIdRechnung(rechnungsKundeDto, getXmlOrder()) ;
				auftragDto.setAnsprechpartnerIIdRechnungsadresse(ansprPartner.getAnsprechpartnerIId());
				if(ansprPartner.hasKeinHVAnsprechpartner()) {
					addChangeEntry(new ChangeEntry<String>(ansprPartner.getFremdsystemnr(), "", 
							"F\u00FCr die externe Personalnummer {0} gibt es keinen Ansprechpartner in der Rechnungsadresse. " +
							"Externe Kontaktinfo:\n" + ansprPartner.getContactInfo())) ;
				}									
				
				Integer iid = getAuftragFac().createAuftrag(auftragDto, getAuthController().getWebClientDto()) ;
				
				returnDto = getAuftragFac().auftragFindByPrimaryKey(iid) ;
			} catch(RemoteException e) {				
			}
			
			return returnDto ;
		}

		
		public class ExternerAnsprechpartner {
			private Integer ansprechpartnerIId ;
			private String fremdsystemnr ;
			private String contactInfo ;
			
			public ExternerAnsprechpartner() {
			}

			public Integer getAnsprechpartnerIId() {
				return ansprechpartnerIId;
			}

			public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
				this.ansprechpartnerIId = ansprechpartnerIId;
			}

			public String getFremdsystemnr() {
				return fremdsystemnr;
			}

			public void setFremdsystemnr(String fremdsystemnr) {
				this.fremdsystemnr = fremdsystemnr;
			}

			public String getContactInfo() {
				return contactInfo;
			}

			public void setContactInfo(String contactInfo) {
				this.contactInfo = contactInfo;
			}
			
			public boolean hasKeinHVAnsprechpartner() {
				return ansprechpartnerIId == null && emptyString(fremdsystemnr).length() > 0 ;
			}
		}

		private ExternerAnsprechpartner getAnsprechpartnerIIdRechnung(KundeDto kundeDto, XMLXMLORDERRESPONSE xmlOrder) throws RemoteException {
			ExternerAnsprechpartner result = new ExternerAnsprechpartner() ;

			XMLXMLORDERPARTIES orderParties = xmlOrder
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			XMLXMLINVOICEPARTY invoiceParty = orderParties.getINVOICEPARTY() ;
			if(null == invoiceParty) return result ;
			
			List<XMLXMLCONTACT> contacts = invoiceParty.getPARTY().getADDRESS().getCONTACT() ;
			if(contacts == null || (contacts.size() == 0)) return result ;
			
			return setupContactInfo(kundeDto, contacts.get(0)) ;
		}
		
		private ExternerAnsprechpartner getAnsprechpartnerIIdLieferung(KundeDto kundeDto, XMLXMLORDERRESPONSE xmlOrder) throws RemoteException {
			ExternerAnsprechpartner result = new ExternerAnsprechpartner() ;

			XMLXMLORDERPARTIES orderParties = xmlOrder
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			XMLXMLDELIVERYPARTY deliveryParty = orderParties.getSHIPMENTPARTIES().getDELIVERYPARTY() ;
			if(null == deliveryParty) return result ;
			
			List<XMLXMLCONTACT> contacts = deliveryParty.getPARTY().getADDRESS().getCONTACT() ;
			if(contacts == null || (contacts.size() == 0)) return result ;
			
			return setupContactInfo(kundeDto, contacts.get(0)) ;
		}		
		private ExternerAnsprechpartner getAnsprechpartnerIIdAuftrag(KundeDto kundeDto, XMLXMLORDERRESPONSE xmlOrder) throws RemoteException {
			ExternerAnsprechpartner result = new ExternerAnsprechpartner() ;

			XMLXMLORDERPARTIES orderParties = xmlOrder
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			XMLXMLBUYERPARTY buyerParty = orderParties.getBUYERPARTY();
			if(null == buyerParty) return result ;
			
			List<XMLXMLCONTACT> contacts = buyerParty.getPARTY().getADDRESS().getCONTACT() ;
			if(contacts == null || (contacts.size() == 0)) return result ;
			
			return setupContactInfo(kundeDto, contacts.get(0)) ;
//			
//			XMLXMLCONTACT contact = contacts.get(0) ;
//			String fremdsystemnr = emptyString(contact.getCCPERSONNELNUMBER()) ;
//			if(fremdsystemnr.length() == 0) return result ;
//
//			result.setFremdsystemnr(fremdsystemnr) ;
//			result.setContactInfo(
//					"Name: '" + contact.getCONTACTNAME() + "'\n" +
//					("Telefon: '" + (contact.getPHONE().size() > 0 ? contact.getPHONE().get(0).getValue() : "nicht definiert")) + "'\n" +
//					"Fax: '" + contact.getFAX() + "'\n" + 
//					"E-Mail: '" + contact.getEMAIL() + "'\n" + 
//					"Mobil: '" + contact.getCCMOBILEPHONE() + "'")  ;
//			
//			AnsprechpartnerDto[] ansprDtos = kundeDto.getAnsprechpartnerDto() ;
//			if(ansprDtos == null ||ansprDtos.length == 0) return result ;
//			
//			for (AnsprechpartnerDto ansprDto : ansprDtos) {
//				if(fremdsystemnr.equals(ansprDto.getCFremdsystemnr())) {
//					result.setAnsprechpartnerIId(ansprDto.getIId());
//					return result ;
//				}
//			}
//			
//			return result ;
		}
		
		private ExternerAnsprechpartner setupContactInfo(KundeDto kundeDto, XMLXMLCONTACT contact) throws RemoteException {
			ExternerAnsprechpartner result = new ExternerAnsprechpartner() ;
			
			String fremdsystemnr = emptyString(contact.getCCPERSONNELNUMBER()) ;
			if(fremdsystemnr.length() == 0) return result ;

			result.setFremdsystemnr(fremdsystemnr) ;
			result.setContactInfo(
					"Name: '" + emptyString(contact.getCONTACTNAME()) + "'\n" +
					("Telefon: '" + (contact.getPHONE().size() > 0 ? contact.getPHONE().get(0).getValue() : "nicht definiert")) + "'\n" +
					"Fax: '" + emptyString(contact.getFAX()) + "'\n" + 
					"E-Mail: '" + emptyString(contact.getEMAIL()) + "'\n" + 
					"Mobil: '" + emptyString(contact.getCCMOBILEPHONE()) + "'")  ;
			
//			AnsprechpartnerDto[] ansprDtos = kundeDto.getAnsprechpartnerDto() ;
			AnsprechpartnerDto[] ansprDtos = getAnsprechpartnerFac().ansprechpartnerFindByPartnerIId(kundeDto.getPartnerIId(), getAuthController().getWebClientDto()) ;
			if(ansprDtos == null ||ansprDtos.length == 0) return result ;
			
			for (AnsprechpartnerDto ansprDto : ansprDtos) {
				if(fremdsystemnr.equals(ansprDto.getCFremdsystemnr())) {
					result.setAnsprechpartnerIId(ansprDto.getIId());
					return result ;
				}
			}
			
			return result ;
		}
		
		private void setBezeichnung(KundeDto kundeDto, AuftragpositionDto positionDto, String shortDescription) {
			if(shortDescription.length() > MAX_DESCRIPTION_LENGTH) {
				// Locale l = new Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation()) ;
				BreakIterator breakIterator = BreakIterator.getWordInstance() ;
				List<String> lines = wrapStringToArray(shortDescription, MAX_DESCRIPTION_LENGTH, breakIterator, true) ;

				positionDto.setCBez(lines.get(0)) ;
				if(lines.size() > 1) {
					positionDto.setCZusatzbez(lines.get(1)) ;
				}
			} else {
				positionDto.setCBez(shortDescription) ;				
			}
		}
		
		private AuftragpositionDto createPositionHandeingabe(KundeDto kundeDto, AuftragDto auftragDto, String cnr, XMLXMLORDERRESPONSEITEM xmlPosition) throws RemoteException {
			AuftragpositionDto positionDto = new AuftragpositionDto() ;
			positionDto.setBelegIId((auftragDto.getIId())) ;
			positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE) ;
			// SP4111
			String shortDescription = xmlPosition.getARTICLEID().getDESCRIPTIONSHORT() ;
			if(shortDescription == null) {
				shortDescription = cnr ;
			}
			setBezeichnung(kundeDto, positionDto, cnr) ;
			
			positionDto.setNMenge(xmlPosition.getQUANTITY()) ;
			positionDto.setNOffeneMenge(xmlPosition.getQUANTITY()) ;
			positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
			
			String einheit = xmlPosition.getCCORDERUNIT() ;
			if(!isValidEinheitCnr(einheit)) {
				String validEinheit = "Stk" ;
				ChangeEntry<?> changeEntry = new ChangeEntry<String>(validEinheit, einheit, "Die Einheit {1} wurde nicht gefunden, stattdessen wird {0} verwendet.") ;
				addChangeEntry(changeEntry) ;
				// createPositionText(auftragDto, changeEntry) ;	
				
				einheit = validEinheit ;
			}
				
			positionDto.setEinheitCNr(einheit) ;
			positionDto.setNEinzelpreis(xmlPosition.getARTICLEPRICE().getPRICEAMOUNT()) ;
			positionDto.setNNettoeinzelpreis(xmlPosition.getARTICLEPRICE().getPRICEAMOUNT()) ;
			positionDto.setNBruttoeinzelpreis(xmlPosition.getARTICLEPRICE().getPRICEAMOUNT()) ;
			// TODO Keine Aufschlaege/Rabatte aus dem Kunden uebernommen  ghp, 26.7.2019
			positionDto.setNNettoeinzelpreisplusversteckteraufschlag(xmlPosition.getARTICLEPRICE().getPRICEAMOUNT());
			positionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(xmlPosition.getARTICLEPRICE().getPRICEAMOUNT());

			positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setBDrucken(Helper.boolean2Short(true)) ;
			positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setFRabattsatz(new Double(0.0)) ;
			positionDto.setFZusatzrabattsatz(new Double(0.0)) ;
			positionDto.setNRabattbetrag(BigDecimal.ZERO) ;
			positionDto.setNMaterialzuschlag(BigDecimal.ZERO) ;
			
//			BigDecimal taxPercent = xmlPosition.getARTICLEPRICE().getTAX() ;
//			Integer xmlMwstSatz = getMwstsatzIIdForTax(taxPercent) ;
//			if(!xmlMwstSatz.equals(kundeDto.getMwstsatzbezIId())) {
//				ChangeEntry<?> changedEntry = new ChangeEntry<Integer>(kundeDto.getMwstsatzbezIId(), xmlMwstSatz, "Der MwSt.Satz {1} ist nicht mit dem Artikel MwstSatz {0} ident, es wird {0} verwendet.") ;
////				createPositionText(auftragDto, changeEntry) ;	
//				addChangeEntry(changedEntry) ;
//			}

			BigDecimal taxPercent = getXmlTaxPercent(xmlPosition.getARTICLEPRICE()) ;
			if(taxPercent != null) {
				Integer xmlMwstSatz = getMwstsatzIIdForTax(taxPercent) ;
				if(kundeDto.getMwstsatzbezIId() != null && !xmlMwstSatz.equals(kundeDto.getMwstsatzbezIId())) {
					ChangeEntry<?> changeEntry = new ChangeEntry<Integer>(kundeDto.getMwstsatzbezIId(), 
							xmlMwstSatz, "Der MwSt.Satz {1} ist nicht mit dem Kunden MwstSatz {0} ident, es wird {0} verwendet.") ;
					addChangeEntry(changeEntry) ;
				}
			}
	
/*			
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
					kundeDto.getMwstsatzbezIId(),getAuthController().getWebClientDto());
*/
			MwstsatzDto mwstsatzDto = getMandantFac().
					mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(), 
							auftragDto.getTBelegdatum(), getAuthController().getWebClientDto());
			
			positionDto.setMwstsatzIId(mwstsatzDto.getIId());
			updateMwstBetraege(positionDto) ;
//			positionDto = (AuftragpositionDto) mwstsatzBestimmenUndNeuBerechnen(positionDto, kundeDto.getIId(), 
//					auftragDto.getTBelegdatum(), getAuthController().getWebClientDto());

			Timestamp earliest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYSTARTDATE()).getTime()) ;
			Timestamp latest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYENDDATE()).getTime()) ;

			updateEarliestDeliveryTime(earliest) ;
			updateLatestDeliveryTime(latest) ;
			positionDto.setTUebersteuerbarerLiefertermin(earliest) ;
			setExternalPositionRef(positionDto, xmlPosition.getLINEITEMID());

			Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, getAuthController().getWebClientDto()) ; 
			return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
		}
		

		private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
			if(priceDto != null && priceDto.nettopreis != null) {
				return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis) ; 
			}
			
			return minimum;		
		}
		
		private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
			BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3()) ;
			if(p != null) return p ;

			p = getMinimumPrice(null, vkPreisDto.getVkpStufe2()) ;
			if(p != null) return p ;

			p = getMinimumPrice(null, vkPreisDto.getVkpStufe1()) ;
			if(p != null) return p ;

			return getMinimumPrice(null, vkPreisDto.getVkpPreisbasis())  ;
			
//			BigDecimal minimum = getMinimumPrice(null, vkPreisDto.getVkpPreisbasis()) ;
//			minimum = getMinimumPrice(minimum, vkPreisDto.getVkpStufe1()) ;
//			minimum = getMinimumPrice(minimum, vkPreisDto.getVkpStufe2()) ;
//			minimum = getMinimumPrice(minimum, vkPreisDto.getVkpStufe3()) ;
//			return minimum ;
		}
		
		private BigDecimal getPrice(KundeDto kundeDto, AuftragDto auftragDto, 
				ArtikelDto itemDto, BigDecimal quantity, Integer mwstsatzId) {
			java.sql.Date d = new java.sql.Date(auftragDto.getDBestelldatum().getTime()) ;
			
			VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
					itemDto.getIId(), kundeDto.getIId(), quantity, d,
					kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), 
					mwstsatzId, auftragDto.getCAuftragswaehrung(), getAuthController().getWebClientDto()) ;

			BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto) ;
			return p ;
		}
		
		private BigDecimal getXmlTaxPercent(XMLXMLARTICLEPRICE xmlArticlePrice) {
			String priceType = xmlArticlePrice.getType() ;
			if(priceType == null || priceType.trim().length() == 0) {
				priceType = "gros_list" ;
			}
			if(priceType.startsWith("net_customer_exp")) return null ;
			if(priceType.startsWith("net_customer")) return null ;
			if(priceType.startsWith("net_list")) return null ;

			return xmlArticlePrice.getTAX().movePointLeft(2) ;
		}
		
		
		private void setExternalPositionRef(AuftragpositionDto positionDto, String positionRefId) {
			ccproducer.setLineItemIdRef(positionDto, positionRefId) ;
		}
	
		private int getIUINachkommastellenPreiseVK() throws RemoteException {
			if (nachkommastellenPreiseVK < 0) {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								getAuthController().getWebClientDto().getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
				nachkommastellenPreiseVK = ((Integer) parameter.getCWertAsObject())
						.intValue();
			}
			return nachkommastellenPreiseVK;
		}

		private BigDecimal calculateMwstbetrag(Integer mwstsatzId, BigDecimal nettoeinzelpreis) throws RemoteException {
			MwstsatzDto mwstSatzDto = getMandantFac()
					.mwstsatzFindByPrimaryKey(mwstsatzId, getAuthController().getWebClientDto());
			BigDecimal mwstBetrag = Helper
					.getProzentWert(nettoeinzelpreis, new BigDecimal(
							mwstSatzDto.getFMwstsatz()), getIUINachkommastellenPreiseVK()) ;
			return mwstBetrag ;
		}

		private void updateMwstBetraege(AuftragpositionDto positionDto)  throws RemoteException {
			BigDecimal mwstBetrag = calculateMwstbetrag(positionDto.getMwstsatzIId(), positionDto.getNNettoeinzelpreis()) ;
			positionDto.setNMwstbetrag(mwstBetrag);
			positionDto.setNBruttoeinzelpreis(positionDto.getNNettoeinzelpreis().add(mwstBetrag));			
		}
		
		private AuftragpositionDto createPositionIdent(KundeDto kundeDto, AuftragDto auftragDto, ArtikelDto itemDto, XMLXMLORDERRESPONSEITEM xmlPosition) throws RemoteException {
			AuftragpositionDto positionDto = new AuftragpositionDto() ;
			positionDto.setBelegIId((auftragDto.getIId())) ;
			positionDto.setArtikelIId(itemDto.getIId()) ;
			positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT) ;
			// positionDto.setCBez(xmlPosition.getARTICLEID().getDESCRIPTIONSHORT()) ;
			positionDto.setNMenge(xmlPosition.getQUANTITY()) ;
			positionDto.setNOffeneMenge(xmlPosition.getQUANTITY()) ;
			positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
			
			String einheit = xmlPosition.getCCORDERUNIT() ;
			if(!isValidEinheitCnr(einheit)) {
				String validEinheit = "Stk" ;
				ChangeEntry<?> changeEntry = new ChangeEntry<String>(validEinheit, einheit,
						"Die Einheit {1} wurde nicht gefunden, stattdessen wird {0} verwendet.") ;
				addChangeEntry(changeEntry) ;
				einheit = validEinheit ;
			}
			
			positionDto.setEinheitCNr(einheit) ;

			BigDecimal vkPrice = null;
			Integer mwstsatzId = getMwstsatzId(itemDto, kundeDto,
					auftragDto.getTBelegdatum(), getAuthController().getWebClientDto());
			if(mwstsatzId != null) {
				vkPrice = getPrice(kundeDto, auftragDto, itemDto, 
						xmlPosition.getQUANTITY(), mwstsatzId) ;
			} else {
				vkPrice = new BigDecimal("0.00");
				ChangeEntry<?> changeEntry = new ChangeEntry<BigDecimal>(vkPrice, vkPrice, 
						"Es konnnte kein Preis ermittelt werden, weil kein Steuersatz ermittelt werden kann. Es wird {0} verwendet.") ;
				addChangeEntry(changeEntry);				
			}
	
			if(vkPrice == null) {
				vkPrice = new BigDecimal("0.00") ;
				ChangeEntry<?> changeEntry = new ChangeEntry<BigDecimal>(vkPrice, vkPrice, 
						"Es konnnte kein Preis ermittelt werden. Es wird {0} verwendet.") ;
				addChangeEntry(changeEntry) ;				
			}
			
			BigDecimal xmlPrice = xmlPosition.getARTICLEPRICE().getPRICEAMOUNT() ;
			
			if(vkPrice.compareTo(xmlPrice) != 0) {
				ChangeEntry<?> changeEntry = new ChangeEntry<BigDecimal>(vkPrice, xmlPrice, 
						"Der angegebene Preis {1} entspricht nicht dem hinterlegtem Preis, stattdessen wird {0} verwendet.") ;
				addChangeEntry(changeEntry) ;
			}
			
			positionDto.setNEinzelpreis(vkPrice);
			positionDto.setNNettoeinzelpreis(vkPrice);
			// TODO Keine Aufschlaege/Rabatte aus dem Kunden uebernommen  ghp, 26.7.2019
			positionDto.setNNettoeinzelpreisplusversteckteraufschlag(vkPrice);
			positionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(vkPrice);
			positionDto.setNBruttoeinzelpreis(vkPrice); 
			positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setBDrucken(Helper.boolean2Short(true)) ;
			positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setFRabattsatz(new Double(0.0)) ;
			positionDto.setFZusatzrabattsatz(new Double(0.0)) ;
			positionDto.setNRabattbetrag(BigDecimal.ZERO) ;
			positionDto.setNMaterialzuschlag(BigDecimal.ZERO) ;
			
//			BigDecimal taxPercent = xmlPosition.getARTICLEPRICE().getTAX() ;
			BigDecimal taxPercent = getXmlTaxPercent(xmlPosition.getARTICLEPRICE()) ;
			if(taxPercent != null) {
				Integer xmlMwstSatz = getMwstsatzIIdForTax(taxPercent) ;
				if(itemDto.getMwstsatzbezIId() != null && !xmlMwstSatz.equals(itemDto.getMwstsatzbezIId())) {
					ChangeEntry<?> changeEntry = new ChangeEntry<Integer>(itemDto.getMwstsatzbezIId(), 
							xmlMwstSatz, "Der MwSt.Satz {1} ist nicht mit dem Artikel MwstSatz {0} ident, es wird {0} verwendet.") ;
					addChangeEntry(changeEntry) ;
				}
			}
			
			positionDto = (AuftragpositionDto) mwstsatzBestimmenUndNeuBerechnen(positionDto, kundeDto.getIId(), 
					auftragDto.getTBelegdatum(), getAuthController().getWebClientDto());

// SP7555 mwstsatzBestimmenUndNeuBerechnen macht die ganze Magie
//			positionDto.setMwstsatzIId(itemDto.getMwstsatzbezIId()) ;
//			updateMwstBetraege(positionDto);
			
			Timestamp earliest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYSTARTDATE()).getTime()) ;
			Timestamp latest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYENDDATE()).getTime()) ;

			updateEarliestDeliveryTime(earliest) ;
			updateLatestDeliveryTime(latest) ;
			positionDto.setTUebersteuerbarerLiefertermin(earliest) ;
			
			setExternalPositionRef(positionDto, xmlPosition.getLINEITEMID());
			
			Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, getAuthController().getWebClientDto()) ; 
			return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
		}
		
		private Integer getMwstsatzId(ArtikelDto itemDto, KundeDto kundeDto, Timestamp belegDatum, TheClientDto theClientDto) {
			boolean bDefaultMwstsatzAusArtikel = getParameterFac()
					.getPositionskontierung(theClientDto.getMandant());

			MwstsatzDto mwstsatzDesKundenDtoZumBelegdatum = getMandantFac()
					.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(), belegDatum);
			if(!bDefaultMwstsatzAusArtikel) {
				return mwstsatzDesKundenDtoZumBelegdatum.getIId();				
			}

			// Wenn Kundensteuersatz = steuerfrei, dann gilt immer der Kundensteuersatz
			if (mwstsatzDesKundenDtoZumBelegdatum.getFMwstsatz().doubleValue() == 0) {
				return mwstsatzDesKundenDtoZumBelegdatum.getIId();
			}
			 
			if (itemDto.getMwstsatzbezIId() != null) {
				MwstsatzDto mwstsatzDesArtikelsZumBelegdatum = getMandantFac()
						.mwstsatzFindZuDatum(itemDto.getMwstsatzbezIId(), belegDatum);
				return mwstsatzDesArtikelsZumBelegdatum.getIId();
			}
			
			return null;
		}
		
		
		private KundeDto getSokoKundeDto(KundeDto auftragsKundeDto, AuftragDto auftragDto) {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					auftragsKundeDto.getPartnerIId(), getAuthController().getWebClientDto()) ;
			
//			VersandwegCCPartnerDto ccPartnerDto = (VersandwegCCPartnerDto)
//					getSystemFac().versandwegPartnerFindByPrimaryKey(
//							partnerDto.getVersandwegIId(), partnerDto.getIId()) ;
			
//			VersandwegPartnerCCDto ccPartnerDto = (VersandwegPartnerCCDto)
//					getSystemFac().versandwegPartnerFindByVersandwegIdPartnerId(
//							partnerDto.getVersandwegIId(), partnerDto.getIId(),
//							getAuthController().getWebClientDto().getMandant()) ;

//			VersandwegPartner versandwegPartner = VersandwegPartnerQuery
//					.findByPartnerIIdVersandwegCnr(em, partnerDto.getIId(), 
//							SystemFac.VersandwegType.CleverCureVerkauf, getAuthController().getWebClientDto().getMandant());

//			Integer versandwegId = versandwegPartner == null ? null : versandwegPartner.getVersandwegId();
//			if(versandwegId == null) {
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
//						partnerDto.getIId().toString());				
//			}
			
			VersandwegPartnerCCDto ccPartnerDto = (VersandwegPartnerCCDto)
					getSystemFac().versandwegPartnerFindByVersandwegCnrPartnerId(
							SystemFac.VersandwegType.CleverCureVerkauf, partnerDto.getIId(),
							getAuthController().getWebClientDto().getMandant());
			
			if(ccPartnerDto.getSokoAdresstypEnum() == SokoAdresstyp.AUFTRAGS_ADRESSE) return auftragsKundeDto ;
			
			Integer kundeId = null ;
			if(ccPartnerDto.getSokoAdresstypEnum() == SokoAdresstyp.LIEFER_ADRESSE) {
				kundeId = auftragDto.getKundeIIdLieferadresse() ;
			}
			if(ccPartnerDto.getSokoAdresstypEnum() == SokoAdresstyp.RECHNUNGS_ADRESSE) {
				kundeId = auftragDto.getKundeIIdRechnungsadresse() ;
			}
			
			KundeDto sokoKundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, getAuthController().getWebClientDto()) ;
			return sokoKundeDto ;
		}
		
		@Override
		public void createPositions(KundeDto kundeDto, AuftragDto auftragDto) {
			try {
				createPositionsForChangeEntries(auftragDto) ;
	
				resetDeliveryTimestamps() ;
				KundeDto sokoKundeDto = getSokoKundeDto(kundeDto, auftragDto) ;

				XMLXMLORDERRESPONSEITEMLIST xmlPositions = getXmlOrder().getORDERRESPONSEITEMLIST() ;
				List<XMLXMLORDERRESPONSEITEM> xmlItems = xmlPositions.getORDERRESPONSEITEM() ;
				for (XMLXMLORDERRESPONSEITEM xmlPosition : xmlItems) {
					List<XMLXMLBUYERAID> buyerIds = xmlPosition.getARTICLEID().getBUYERAID();
//					if(buyerIds.size() == 0) continue ;
					if(buyerIds.size() == 0) {
						String descriptionShort = xmlPosition
								.getARTICLEID().getDESCRIPTIONSHORT();
						if(Helper.isStringEmpty(descriptionShort)) {
							ChangeEntry<?> changeEntry = new ChangeEntry<String>(
									xmlPosition.getLINEITEMID(), "", 
									"Es gibt weder eine kundenspezifische Artikelnummer, noch eine Handeingabe in LINE_ITEM_ID {0}. Bitte .xml prüfen");
							addChangeEntry(changeEntry);
							createPositionsForChangeEntries(auftragDto);
							continue;
						}
					}

					setupArtikelFinder(xmlPosition) ;
					ArtikelDto itemDto = getArtikelFinder().findArtikel(sokoKundeDto, auftragDto) ;
					
//					String itemCnr = buyerIds.get(0).getValue().trim() ;
					if(itemDto == null) {
						String itemCnr = getArtikelFinder().getUsedItemCnr();
						AuftragpositionDto positionDto = createPositionHandeingabe(kundeDto, auftragDto, itemCnr, xmlPosition) ;
						ChangeEntry<?> changeEntry = new ChangeEntry<String>(itemCnr, "", "Der Artikel {0} wurde nicht gefunden, es wurde eine Handeingabe erzeugt.") ;
						addChangeEntry(changeEntry) ;
					} else {
						AuftragpositionDto positionDto = createPositionIdent(kundeDto, auftragDto, itemDto, xmlPosition) ;
					}
					
					XMLXMLORDERPOSTEXTLIST textList = xmlPosition.getORDERPOSTEXTLIST() ;
					if(textList != null) {
						List<XMLXMLORDERITEMTEXT> texts = textList.getORDERITEMTEXT() ;
						for (XMLXMLORDERITEMTEXT itemText : texts) {
							if(!isEmptyString(itemText.getCCTEXTVALUE())) {
								addChangeEntry(new ChangeEntry<String>(
										itemText.getCCTEXTIDENTIFIER(), "", "Bestelltext: {0}, '" + itemText.getCCTEXTVALUE() + "'")) ;
							}
						}					
					}
					
					createPositionsForChangeEntries(auftragDto) ;
				}		
				
				XMLXMLORDERRESPONSEHEADTEXTLIST headTextList = getXmlOrder().getORDERRESPONSEHEADTEXTLIST();
				if(headTextList != null) {
					List<XMLXMLORDERITEMTEXT> texts = headTextList.getORDERITEMTEXT();
					for(XMLXMLORDERITEMTEXT itemText : texts) {						
						if(!isEmptyString(itemText.getCCTEXTVALUE())) {
							addChangeEntry(new ChangeEntry<String>(
									itemText.getCCTEXTIDENTIFIER(), "",
									"Kopftext: {0}, '<style forecolor=\"#ff0000\" isBold=\"true\">" + itemText.getCCTEXTVALUE() + "</style>'")) ;
						}
					}
					createPositionsForChangeEntries(auftragDto) ;
				}
				
				auftragDto.setDLiefertermin(getEarliestDeliveryTime()) ;
				auftragDto.setDFinaltermin(getLatestDeliveryTime()) ;
				
				getAuftragFac().updateAuftragOhneWeitereAktion(auftragDto, getAuthController().getWebClientDto()) ;	
				
				postMessageAuftragNeu(auftragDto);				
   			} catch(RemoteException e) {
			}
		}
	}
	
	class KundeFinder17632 implements KundeFinder {
		private XMLXMLORDERRESPONSE xmlOrder ;
		
		public KundeFinder17632(XMLXMLORDERRESPONSE xmlOrder) {
			this.xmlOrder = xmlOrder ;
		}
		
		private XMLXMLORDERRESPONSE getXmlOrder() {
			return xmlOrder ;
		}
		
		private String extractPartyId(XMLXMLPARTY xmlParty) {
			if(null == xmlParty.getPARTYID() || null == xmlParty.getPARTYID().getValue()) {
				return null ;
			}
			
			return xmlParty.getPARTYID().getValue().trim() ;
		}
		
		
		private String extractUidNummer(XMLXMLPARTY xmlParty) {
			if( null == xmlParty.getADDRESS() || null == xmlParty.getADDRESS().getVATID()) {
				return null ;
			}

			return xmlParty.getADDRESS().getVATID().trim() ;
		}

		
		@Override
		public WebshopPartner findKunde() {
			XMLXMLORDERPARTIES orderParties = getXmlOrder()
					.getORDERRESPONSEHEADER().getORDERRESPONSEINFO().getORDERPARTIES() ;
			XMLXMLBUYERPARTY buyerParty = orderParties.getBUYERPARTY();
			if(null == buyerParty) return null ;
			
			KundeDto kundeDto = findByLieferantenNr(extractPartyId(buyerParty.getPARTY())) ;
			if(kundeDto != null) return new WebshopPartner(kundeDto) ;
			
			kundeDto = findByUidNummer(extractUidNummer(buyerParty.getPARTY())) ;
			return new WebshopPartner(kundeDto) ;
		}

		@Override
		public WebshopPartner findKundeDelivery() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public WebshopPartner findKundeInvoice() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void validateKunde(KundeDto kundeDto) {
			// TODO Auto-generated method stub
		}

		@Override
		public void validateKundeDelivery(KundeDto kundeDto) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void validateKundeInvoice(KundeDto kundeDto) {
			// TODO Auto-generated method stub			
		}	
	}


	class ArtikelFinder17632 implements ArtikelFinder {

		private XMLXMLORDERRESPONSEITEM xmlItem ;
		private String usedItemCnr;
		
		public ArtikelFinder17632(XMLXMLORDERRESPONSEITEM convertedItem) {
			xmlItem = convertedItem ;
		}
		
		private XMLXMLORDERRESPONSEITEM getXmlItem() {
			return xmlItem ;
		}

		public String getUsedItemCnr() {
			return usedItemCnr;
		}
		
		private String mapItemCnrEP(String fremdsystemnr,
				String value, KundeDto kundeDto) {
			if(value.startsWith("Y")) {
				return fremdsystemnr + value.substring(1);
			}

			return null;
		}
	
		private String mapItemCnrPA(String fremdsystemnr, 
				String value, KundeDto kundeDto) {
			// keine "-", " " im Suchstring. Datenbanksuche sucht ohne "-"
//			return (fremdsystemnr + value).replace("-", "").replace(" ", "");
			return fremdsystemnr + value;
		}

		private ArtikelDto findRevisionedItem(List<Artikel> items,
				String prefix, String itemCnr, String indexNr) {
			ArtikelDto itemDto = null;
			String probedRevisions = "";
			List<Artikel> probedItems = new ArrayList<Artikel>();
			for (Artikel item : items) {
				if(!item.getCNr().startsWith(prefix)) {
					// falsch positiv wegen "PAHBS" (als HV Artikel) vs "PA-HBS"
					continue;
				}
				
				if(Helper.isTrue(item.getBVersteckt())) {
					continue;
				}

				if(probedItems.size() > 0) {
					probedRevisions += ", " ;
				}
				probedRevisions += emptyString(item.getCRevision());

				probedItems.add(item);
								
				if(emptyString(item.getCRevision()).equals(indexNr)) {
					itemDto = getArtikelFac().artikelFindByPrimaryKey(
							item.getIId(), getAuthController().getWebClientDto());				
					ChangeEntry<?> changeEntry = new ChangeEntry<String>(
							itemCnr, itemDto.getCNr(), "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
								"' wurde als {0} angefordert/bestellt.");
					addChangeEntry(changeEntry);
					break ;
				}
			}
	
			if(itemDto == null && probedItems.size() > 0) {
				itemDto = getArtikelFac().artikelFindByPrimaryKey(
						probedItems.get(0).getIId(), getAuthController().getWebClientDto()) ;
				ChangeEntry<?> changeEntry = new ChangeEntry<String>(
						itemCnr, itemDto.getCNr(), "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
							"' wurde als {0} angefordert/bestellt.");
				addChangeEntry(changeEntry);
				changeEntry = new ChangeEntry<String>(
						indexNr, probedRevisions, "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
							"' wurde mit Index {0} angefordert, aber nur mit Revision(en) {1} gefunden.") ;
				addChangeEntry(changeEntry) ;					
			}

			return itemDto;
		}
		
		
		private ArtikelDto findInexactItem(String prefix, String mappedItemCnr, String itemCnr, String indexNr, String mandantCnr) {
			if (getMandantFac().hatZusatzfunktionZentralerArtikelstamm(
					getAuthController().getWebClientDto())) {
				mandantCnr = getSystemFac().getHauptmandant();
			}

			String searchItemCnr = mappedItemCnr.replace("-", "").replace(" ", "");
			List<Artikel> items = ArtikelQuery.listArtikelnrSP8207(em, searchItemCnr, mandantCnr);
			ArtikelDto itemDto = findRevisionedItem(items, prefix, itemCnr, indexNr);

			return itemDto;
		}
	
		private ArtikelDto findEPArtikel(String itemCnr, KundeDto kundeDto, String indexNr) throws RemoteException {
			String mappedItemCnr = mapItemCnrEP("EP-", itemCnr, kundeDto);
			if(mappedItemCnr == null) return null;
		
			ArtikelDto itemDto = findInexactItem("EP-", mappedItemCnr,
					itemCnr, indexNr, kundeDto.getMandantCNr());
/*			
			ArtikelDto itemDto = getArtikelFac().artikelFindByCNrOhneExc(
					mappedItemCnr, getAuthController().getWebClientDto());
*/	
			if(itemDto != null && Helper.isTrue(itemDto.getBVersteckt())) {
				// Versteckter Artikel soll letztendlich Handeingabe werden
				myLogger.warn("Der angeforderte Artikel '" + itemCnr + "' -> '" +
						mappedItemCnr + "' ist versteckt (-> Handeingabe).");
				return null;
			}
		
			return itemDto;
		}
	
			
		private ArtikelDto findPAArtikel(String itemCnr, KundeDto kundeDto, String indexNr) {
			String mappedItemCnr = mapItemCnrPA("PA-", itemCnr, kundeDto);
			
			ArtikelDto itemDto = findInexactItem("PA-", mappedItemCnr,
					itemCnr, indexNr, kundeDto.getMandantCNr());
/*			
			String mandantCnr = kundeDto.getMandantCNr();
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					getAuthController().getWebClientDto())) {
				mandantCnr = getSystemFac().getHauptmandant();
			}

			List<Artikel> items = ArtikelQuery.listArtikelnrSP8207(em, mappedItemCnr, mandantCnr);
			ArtikelDto itemDto = null;
			String probedRevisions = "";
			for (Artikel item : items) {
				if(!item.getCNr().startsWith("PA-")) {
					// falsch positiv wegen "PAHBS" (als HV Artikel) vs "PA-HBS"
					continue;
				}
				
				if(Helper.isTrue(item.getBVersteckt())) {
					continue;
				}
				
				if(probedRevisions.length() > 0) {
					probedRevisions += ", " ;
				}

				probedRevisions += emptyString(item.getCRevision());
				if(emptyString(item.getCRevision()).equals(indexNr)) {
					itemDto = getArtikelFac().artikelFindByPrimaryKey(
							item.getIId(), getAuthController().getWebClientDto());				
					ChangeEntry<?> changeEntry = new ChangeEntry<String>(
							itemCnr, itemDto.getCNr(), "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
								"' wurde als {0} angefordert.");
					addChangeEntry(changeEntry);
					break ;
				}
			}

			if(itemDto == null && items.size() > 0) {
				itemDto = getArtikelFac().artikelFindByPrimaryKey(
						items.get(0).getIId(), getAuthController().getWebClientDto()) ;
				ChangeEntry<?> changeEntry = new ChangeEntry<String>(
						itemCnr, itemDto.getCNr(), "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
							"' wurde als {0} angefordert.");
				addChangeEntry(changeEntry);
				changeEntry = new ChangeEntry<String>(
						indexNr, probedRevisions, "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
							"' wurde mit Index {0} angefordert, aber nur mit Revision(en) {1} gefunden.") ;
				addChangeEntry(changeEntry) ;					
			}
*/
			return itemDto;
		}
		
		private ArtikelDto findMappedArtikel(String itemCnr, String indexNr, KundeDto kundeDto) throws RemoteException {
			ArtikelDto itemDto = null;
			String fremdsystemNr = emptyString(kundeDto.getCFremdsystemnr());
			
			if(itemDto == null && fremdsystemNr.contains("EP-")) {
				myLogger.info("Beruecksichtige " + fremdsystemNr + ", Variante EP- bei der Artikelsuche");
				itemDto = findEPArtikel(itemCnr, kundeDto, indexNr);				
			}
			
			if(itemDto == null && fremdsystemNr.contains("PA-")) {
				myLogger.info("Beruecksichtige " + fremdsystemNr + ", Variante PA- bei der Artikelsuche");
				itemDto = findPAArtikel(itemCnr, kundeDto, indexNr);				
			}
			
			if(itemDto != null) {
				ArtikelsperrenDto sperrenDtos[] = getArtikelFac()
						.artikelsperrenFindByArtikelIId(itemDto.getIId()) ;
				if(sperrenDtos != null && sperrenDtos.length > 0) {
					myLogger.warn("Der angeforderte Artikel '" +
							itemCnr + "' -> '" + itemDto.getCNr() + "' ist gesperrt (-> Handeingabe).");							
					return null;
				}					
			}

/*
			if("EP-".equals(fremdsystemNr) || "EP-PA-".equals(fremdsystemNr) || "PA-".equals(fremdsystemNr)) {
				myLogger.info("Beruecksichtige " + fremdsystemNr + " bei der Artikelsuche");
				
				if(fremdsystemNr.startsWith("EP-")) {
					itemDto = findEPArtikel(itemCnr, kundeDto, indexNr);					
				}
				
				if(itemDto == null && ("EP-PA-".equals(fremdsystemNr) || "PA-".equals(fremdsystemNr))) {
					itemDto = findPAArtikel(itemCnr, kundeDto, indexNr);
				}

				if(itemDto != null) {
					ArtikelsperrenDto sperrenDtos[] = getArtikelFac()
							.artikelsperrenFindByArtikelIId(itemDto.getIId()) ;
					if(sperrenDtos != null && sperrenDtos.length > 0) {
						myLogger.warn("Der angeforderte Artikel '" +
								itemDto.getCNr() + "' ist gesperrt (-> Handeingabe).");
						return null;
					}					
				}
			}
*/

/*			
			if("EP-".equals(kundeDto.getCFremdsystemnr())) {
				String mappedItemCnr = mapItemCnrEP(itemCnr, kundeDto);
				itemDto = getArtikelFac().artikelFindByCNrOhneExc(
						mappedItemCnr, getAuthController().getWebClientDto());
				if(itemDto != null && Helper.isTrue(itemDto.getBVersteckt())) {
					// Versteckter Artikel soll letztendlich Handeingabe werden
					myLogger.warn("Der angeforderte Artikel '" + itemCnr + "' -> '" +
							mappedItemCnr + "' ist versteckt (-> Handeingabe).");
					return null;
				}

				if(itemDto != null) {
					ArtikelsperrenDto sperrenDtos[] = getArtikelFac()
							.artikelsperrenFindByArtikelIId(itemDto.getIId()) ;
					if(sperrenDtos != null && sperrenDtos.length > 0) {
						myLogger.warn("Der angeforderte Artikel '" +
								itemDto.getCNr() + "' ist gesperrt (-> Handeingabe).");
						return null;
					}					
				}
			}
*/
			
			if(itemDto == null) {
				itemDto = getArtikelFac().artikelFindByCNrOhneExc(
						itemCnr, getAuthController().getWebClientDto());				
			}
			
			return itemDto;
		}
		
		@Override
		public ArtikelDto findArtikel(KundeDto kundeDto, AuftragDto auftragDto) {
//			if(isEmptyString(getXmlItem().getCCDRAWINGNR())) return null ;

			String itemCnr = "" ;
			boolean hasBuyerAid = false;
			
			// Mit der Buyer_AID versuchen
			List<XMLXMLBUYERAID> xmlBuyerIds= getXmlItem().getARTICLEID().getBUYERAID();
			if(xmlBuyerIds != null && xmlBuyerIds.size() > 0) {
				itemCnr = xmlBuyerIds.get(0).getValue() ;
				hasBuyerAid = true;
			}

			if(isEmptyString(itemCnr)) {
				itemCnr = emptyString(getXmlItem().getARTICLEID().getDESCRIPTIONSHORT());
			}

			if(isEmptyString(itemCnr)) {
				itemCnr = emptyString(getXmlItem().getCCDRAWINGNR()) ;
			}
			
			String indexNr = emptyString(getXmlItem().getCCINDEXNR()) ;
			if("-".equals(indexNr)) {
				indexNr = "" ;
			}
	
			if(isEmptyString(itemCnr)) {
				if(isEmptyString(itemCnr)) return null ;
			}
			
			java.sql.Date sqlDate = new java.sql.Date(auftragDto.getDBestelldatum().getTime()) ;
			
			ArtikelDto itemDto = null ;
			try {
				List<Kundesoko> kundeSokos = KundesokoQuery.listByKundeIIdArtikelnummer(em, kundeDto.getIId(), itemCnr, sqlDate) ;
				String probedRevisions = "" ;
				
				for (Kundesoko kundesoko : kundeSokos) {
					ArtikelDto tempItemDto = getArtikelFac().artikelFindByPrimaryKey(
							kundesoko.getArtikelIId(), getAuthController().getWebClientDto()) ;
					if(probedRevisions.length() > 0) {
						probedRevisions += ", " ;
					}

					probedRevisions += tempItemDto.getCRevision() ;
					if(emptyString(tempItemDto.getCRevision()).equals(indexNr)) {
						itemDto = tempItemDto ;
						
						break ;
					}
				}
				
				if(itemDto == null && kundeSokos.size() > 0) {
					itemDto = getArtikelFac().artikelFindByPrimaryKey(kundeSokos.get(0).getArtikelIId(), getAuthController().getWebClientDto()) ;
					ChangeEntry<?> changeEntry = new ChangeEntry<String>(
							indexNr, probedRevisions, "ACHTUNG! Der Artikel '" + itemDto.getCNr() + 
								"' wurde mit Index {0} angefordert, aber nur mit Revision(en) {1} gefunden.") ;
					addChangeEntry(changeEntry) ;					
				}
			} catch(NoResultException e) {
			} 
			
			try {
				if(itemDto == null && hasBuyerAid) {
					String supplierItemCnr = getXmlItem().getARTICLEID().getSUPPLIERAID();
					if(!Helper.isStringEmpty(supplierItemCnr)) {
						itemDto = getArtikelFac().artikelFindByCNrOhneExc(
								supplierItemCnr, getAuthController().getWebClientDto());
						if(itemDto != null) {
							ChangeEntry<?> changeEntry = new ChangeEntry<String>(
									itemCnr, supplierItemCnr, "ACHTUNG! Der Artikel {0} wurde mittels Lieferantenartikelnummer {1} gefunden.") ;
							addChangeEntry(changeEntry) ;							
						}
					}
				}
				
				if(itemDto == null) {
					itemDto = findMappedArtikel(itemCnr, indexNr, kundeDto);					
				}
				
				if(itemDto != null && indexNr.length() > 0) {
					if(!indexNr.equals(itemDto.getCRevision())) {
						String baseMsg = "ACHTUNG! Der Artikel '" + itemCnr + "' wurde mit Index {0} angefordert, ";
						String msg = isEmptyString(itemDto.getCRevision()) 
								? (baseMsg + "hat aber keine Revision.")
								: (baseMsg + "aber mit Revision {1} gefunden.");
						ChangeEntry<?> changeEntry = new ChangeEntry<String>(
								indexNr, itemDto.getCRevision(), msg) ;
						addChangeEntry(changeEntry) ;					
					}
				}
			} catch(RemoteException e) {				
			}

			try {
				if(itemDto != null) {
					ArtikelsperrenDto sperrenDtos[] = getArtikelFac().artikelsperrenFindByArtikelIId(itemDto.getIId()) ;
					if(sperrenDtos != null && sperrenDtos.length > 0) {
						ChangeEntry<?> changeEntry = new ChangeEntry<String>(
								itemDto.getCNr(), "", "ACHTUNG! Der Artikel {0} ist gesperrt (" + sperrenDtos[0].getCGrund() + ").") ;						
						addChangeEntry(changeEntry) ;
					}
					
					Calendar c = GregorianCalendar.getInstance() ;
					c.setTimeInMillis(System.currentTimeMillis()) ;
					
					int monthsBack = 24 ;
					c.add(Calendar.MONTH, 0 - monthsBack);
					Timestamp youngestDate = new Timestamp(c.getTimeInMillis()) ;
					Long entryCount = LagerbewegungQuery.countByArtikelIIdDate(em, itemDto.getIId(), youngestDate) ;
					
					if(entryCount < 1l) {
						ChangeEntry<?> changeEntry = new ChangeEntry<String>(
								itemDto.getCNr(), "", "Der Artikel {0} wurde seit mehr als " + monthsBack + " Monat(en) nicht mehr verwendet.") ;						
						addChangeEntry(changeEntry) ;						
					}
				}				
			} catch(RemoteException e) {}
			
			if(itemDto == null) {
				String customerItemNr = "'" + itemCnr + "' Revision '" + indexNr + "'" ;
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKeyOhneExc(kundeDto.getPartnerIId(), getAuthController().getWebClientDto()) ;
				String kbez = partnerDto != null ? partnerDto.getCKbez() : "" ;
				ChangeEntry<?> changedEntry = new ChangeEntry<String>(
						customerItemNr, "", "Der Kundenartikel {0} wurde beim Kunden (Kurzbezeichnung) '" + kbez + "' nicht gefunden." ) ;
				addChangeEntry(changedEntry);
			}
			
			usedItemCnr = itemCnr;
			return itemDto ;
		}
	}	
}
