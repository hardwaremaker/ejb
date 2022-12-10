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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.WebshopOrderServiceFacLocal;
import com.lp.server.auftrag.service.WebshopOrderServiceInterface;
import com.lp.server.auftrag.service.WebshopPartner;
import com.lp.server.partner.ejb.Selektion;
import com.lp.server.partner.ejb.SelektionQuery;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.WebAddress;
import com.lp.server.partner.service.WebPerson;
import com.lp.server.schema.bmecat_2005.BCCONTACTNAME;
import com.lp.server.schema.bmecat_2005.BCINTERNATIONALPID;
import com.lp.server.schema.bmecat_2005.BCSUPPLIERPID;
import com.lp.server.schema.bmecat_2005.TypePARTYID;
import com.lp.server.schema.opentrans_2_1.OT2ADDRESS;
import com.lp.server.schema.opentrans_2_1.OT2CONTACTDETAILS;
import com.lp.server.schema.opentrans_2_1.OT2ORDER;
import com.lp.server.schema.opentrans_2_1.OT2ORDERITEM;
import com.lp.server.schema.opentrans_2_1.OT2PARTY;
import com.lp.server.schema.opentrans_2_1.OT2PRODUCTID;
import com.lp.server.schema.opentrans_2_1.OT2PRODUCTPRICEFIX;
import com.lp.server.schema.opentrans_2_1.OT2TAXDETAILSFIX;
import com.lp.server.system.service.HeliumSimpleAuthController;
import com.lp.server.system.service.HeliumTokenAuthController;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;


@Stateless
@Local(WebshopOrderServiceFacLocal.class)
public class WebshopCustomerOrderServiceEjb extends  WebshopOrderServiceBase implements WebshopOrderServiceInterface {

	@PersistenceContext
	private EntityManager em;

	private HeliumSimpleAuthController authController = null ; 
	
	protected HeliumSimpleAuthController getAuthController() {
		if(null == authController) {
			authController = new HeliumTokenAuthController(
					getBenutzerFac(), getMandantFac(), getPersonalFac()) ;
		}
		return authController ;
	}	
	
	
	@Override
	protected Object unmarshalImpl(String xmlOpenTransOrder)
			throws SAXException, JAXBException {
		OT2ORDER openTransOrder = unmarshal(null, xmlOpenTransOrder, OT2ORDER.class) ;
		if(null != openTransOrder) {
			setupFinderFactory(openTransOrder) ;
		}
		return openTransOrder ;
	}

	private void setupFinderFactory(OT2ORDER convertedOrder) {
		setOrderAdapter(new OT21OrderAdapter(convertedOrder, getWebshopName(), getWebshopIId())) ;
		setKundeFinder(new KundeFinderWebshop(convertedOrder)) ;	
		setAuftragFinder(new AuftragFinderWebshop(convertedOrder, getKundeFinder())) ;
	}

	private void setupArtikelFinder(OT2ORDERITEM convertedItem) {
		setArtikelFinder(new ArtikelFinderWebshop(convertedItem)) ;
	}

	@Override
	protected KundeDto createEjbCustomerImpl() {
		WebshopPartner webshopPartner = getKundeFinder().findKunde() ;
		return webshopPartner != null ? webshopPartner.getKundeDto() : null;	
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

	
	class KundeFinderWebshop implements KundeFinder {
		private OT2ORDER theXmlOrder ;
		private List<OT2PARTY> theXmlParties ;
		
		public final static int ROLE_BUYER = 1 ;
		public final static int ROLE_DELIVERY = 2 ;
		
		public final static int PARTYID_HELIUM = 1 ;
		public final static int PARTYID_EXTERNAL = 2 ;
		
		public KundeFinderWebshop(OT2ORDER convertedOrder) {
			theXmlOrder = convertedOrder ;

			List<OT2PARTY> parties = getXmlOrder().getORDERHEADER().getORDERINFO().getPARTIES().getPARTY() ; 
			theXmlParties = (null == parties) ? new ArrayList<OT2PARTY>() : parties ; 
		}
		
		public OT2ORDER getXmlOrder() {
			return theXmlOrder ;
		}
		
		private List<OT2PARTY> getXmlParties() {
			return theXmlParties ;
		}
		
		
		@Override
		public WebshopPartner findKunde() {
			OT2PARTY party = findKundeParty() ;
			return findKundeImpl(party) ;
		}		

		private OT2PARTY findKundeParty() {
			OT2PARTY party = findBuyer(getXmlParties()) ;
			if(party == null) {
				party = findInvoiceRecepient(getXmlParties()) ;
			}
			return party ;
		}
		
		@Override
		public WebshopPartner findKundeDelivery() {
			OT2PARTY party = findDelivery(getXmlParties()) ;
			return findKundeImpl(party) ;
		}

		@Override
		public WebshopPartner findKundeInvoice() {
			OT2PARTY party = findInvoiceRecepient(getXmlParties()) ;
			return findKundeImpl(party) ;
		}

		
		@Override
		public void validateKunde(KundeDto kundeDto) {
			OT2PARTY party = findKundeParty() ;			
			OT2ADDRESS ot2address = findFirstNonEmptyAddress(party.getADDRESS()) ;
			PartnerDto partnerDto = kundeDto.getPartnerDto() ;
			
			validatePartnerData(partnerDto, ot2address, "Kunde: ") ;
		}

		@Override
		public void validateKundeDelivery(KundeDto kundeDto) {
			OT2PARTY party = findDelivery(getXmlParties()) ;			
			OT2ADDRESS ot2address = findFirstNonEmptyAddress(party.getADDRESS()) ;
			PartnerDto partnerDto = kundeDto.getPartnerDto() ;
			
			validatePartnerData(partnerDto, ot2address, "Lieferadresse: ") ;
		}

		@Override
		public void validateKundeInvoice(KundeDto kundeDto) {
			OT2PARTY party = findInvoiceRecepient(getXmlParties()) ;			
			OT2ADDRESS ot2address = findFirstNonEmptyAddress(party.getADDRESS()) ;
			PartnerDto partnerDto = kundeDto.getPartnerDto() ;
			
			validatePartnerData(partnerDto, ot2address, "Rechnungsadresse: ") ;			
		}

		private void validatePartnerData(PartnerDto partnerDto, OT2ADDRESS ot2address, String identifier) {		
			addChangeEntryIfDifferent(partnerDto.getCUid(), ot2address.getVATID(), 
					identifier + "Die UID von HELIUM ''{0}'' und die des Webshops ''{1}'' unterscheiden sich") ;

			if(ot2address.getSTREET().size() > 0) {
				addChangeEntryIfDifferent(partnerDto.getCStrasse(), ot2address.getSTREET().get(0).getValue(), 
						identifier + "Die Strasse von HELIUM ''{0}'' und die des Webshops ''{1}'' unterscheiden sich") ;
			}
			if(ot2address.getPHONE().size() > 0) {
				addChangeEntryIfDifferent(partnerDto.getCTelefon(), ot2address.getPHONE().get(0).getValue(), 
						identifier + "Die Telefonnr von HELIUM ''{0}'' und die des Webshops ''{1}'' unterscheiden sich") ;
			}
			if(ot2address.getFAX().size() > 0) {
				addChangeEntryIfDifferent(partnerDto.getCFax(), ot2address.getFAX().get(0).getValue(), 
						identifier + "Die Faxnummer von HELIUM ''{0}'' und die des Webshops ''{1}'' unterscheiden sich") ;
			}

			List<Object> emails = ot2address.getEMAILAndPUBLICKEY() ;
			if(emails != null && emails.size() > 0) {
				addChangeEntryIfDifferent(partnerDto.getCEmail(), (String) emails.get(0), 
						identifier + "Die E-Mail Adresse von HELIUM ''{0}'' und die des Webshops ''{1}'' unterscheiden sich") ;
			}
		}
		
		private void addChangeEntryIfDifferent(String heliumValue, String otherValue, String message) {
			if(heliumValue == null && otherValue == null) return ;
			
			String hv = emptyString(heliumValue) ;
			String ext = emptyString(otherValue) ;
			if(!hv.equals(ext)) {
				addChangeEntry(new ChangeEntry<String>(heliumValue, otherValue, message)) ;
			}
		}
		
		private OT2PARTY findBuyer(List<OT2PARTY> xmlParties) {
			for (OT2PARTY ot2party : xmlParties) {				
				for (String role : ot2party.getPARTYROLE()) {
					if("buyer".equals(role)) return ot2party ;
				}
			}
			
			return null ;
		}
		
		private OT2PARTY findDelivery(List<OT2PARTY> xmlParties) {
			for (OT2PARTY ot2party : xmlParties) {				
				for (String role : ot2party.getPARTYROLE()) {
					if("delivery".equals(role)) return ot2party ;
				}
			}
			
			return null ;			
		}
		
		private OT2PARTY findInvoiceRecepient(List<OT2PARTY> xmlParties) {
			for (OT2PARTY ot2party : xmlParties) {				
				for (String role : ot2party.getPARTYROLE()) {
					if("invoice_recipient".equals(role)) return ot2party ;
				}
			}
			
			return null ;			
		}

		
 		private TypePARTYID findPartyId(OT2PARTY xmlParty) {
 			return findPartyIdForTypeImpl(xmlParty, "heliumv") ;
 		}
		
 		private TypePARTYID findExternalPartyId(OT2PARTY xmlParty) {
 			return findPartyIdForTypeImpl(xmlParty, "customer_specific") ;
 		}

 		private TypePARTYID findPartyIdForTypeImpl(OT2PARTY xmlParty, String theType) {	
 			if(xmlParty == null) return null ;
 			
			TypePARTYID partyId = null ;
			
			for (TypePARTYID partyIdEntry : xmlParty.getPARTYID()) {
				if(theType.equals(partyIdEntry.getType())) return partyIdEntry ;
			}
			
			return partyId ;
		}
 		
 		private KundeDto findKundeDtoFromPartyId(TypePARTYID partyId) {
			if(partyId == null) return null ;
			
			try {
				int kundeIId = Integer.parseInt(partyId.getValue()) ;
				return getKundeFac().kundeFindByPrimaryKeyOhneExc(kundeIId, getAuthController().getWebClientDto()) ;
			} catch(NumberFormatException e) {				
			}			
			
			return null ;
		}
		
		private KundeDto findKundeDtoFromAddress(WebAddress webAddress, String externalPartyId) {
			PartnerDto[] partners = getPartnerFac().partnerFindByName1Lower(webAddress.getHvName()) ;
			for (PartnerDto partnerDto : partners) {
				KundeDto kundeDto = acceptableKundeDtoForExternalSystem(partnerDto.getIId(), externalPartyId) ;
				if(kundeDto != null) return kundeDto ;
			}

			return null ;
		}
		
		/**
		 * Ein existierender Helium Kunde wird nur als solcher zurueckgeliefert, wenn:</br>
		 * die HeliumIId ueberhaupt gefunden wird und
		 * die Externe Id nicht uebermittelt oder
		 * diese Externe Id mit der identisch ist, die im Helium hinterlegt ist.
		 * 
		 * <p><b>Background:</b>
		 * Der gleiche Kunde/Adresse koennte von mehreren Externern-System-Adressen benutzt
		 * werden. Wir wollen aber nicht haben, dass wenn Kunde A im externen System seine Adresse 
		 * aendert, dies auch Auswirkungen auf den Kunden B im externen System hat.
		 * </p>
		 * @param kundeId
		 * @param externalId
		 * @return
		 */
		private KundeDto acceptableKundeDtoForExternalSystem(Integer kundeId, String externalId) {
			try {
				KundeDto kundeDto = getKundeFac()
						.kundeFindByiIdPartnercNrMandantOhneExc(kundeId,
								getAuthController().getWebClientDto().getMandant(),
								getAuthController().getWebClientDto());
				if(kundeDto == null) return null ;
				if(externalId == null) return kundeDto ;

				return externalId.equals(kundeDto.getCIdExtern()) ? kundeDto : null ;
			} catch (RemoteException e) {
				myLogger.warn("RemoteException" + e.getMessage());
				return null;
			}			
		}
		
		private AnsprechpartnerDto acceptableAnsprechpartnerDtoForExternalSystem(KundeDto kundeDto, WebAddress webAddress) {
			if(webAddress == null || webAddress.getWebPerson() == null) return null ;
			String externalId = webAddress.getWebPerson().getContactId() ;
			if(externalId == null || externalId.trim().length() == 0) return null ;
			
			try {
				AnsprechpartnerDto[] aDtos = getAnsprechpartnerFac()
						.ansprechpartnerFindByPartnerIId(
								kundeDto.getPartnerIId(), getAuthController().getWebClientDto()) ;				
				for (AnsprechpartnerDto ansprechpartnerDto : aDtos) {
					if(externalId.equals(ansprechpartnerDto.getCFremdsystemnr())) return ansprechpartnerDto ;
				}				
			} catch(RemoteException e) {				
				myLogger.warn("RemoteException" + e.getMessage());
			}
			
			return null ;
		}
		
		private OT2ADDRESS findFirstNonEmptyAddress(List<OT2ADDRESS> addresses) {
			for(OT2ADDRESS ot2address : addresses) {
				if(ot2address.getNAME().size() != 0) return ot2address ;
				
				List<OT2CONTACTDETAILS> contactDetails = ot2address.getCONTACTDETAILS() ;
				if(contactDetails == null || contactDetails.size() == 0) continue ;
				
				List<BCCONTACTNAME> contactNames = contactDetails.get(0).getCONTACTNAME() ;
				if(contactNames == null || contactNames.size() == 0) continue ;

				return ot2address ;
			}			
			
			return null ;		
		}
		
				
		private LandplzortDto createLandplzortIfNeeded(OT2ADDRESS ot2address) throws RemoteException {
			String lkz = emptyString(ot2address.getCOUNTRYCODED());
			if(isEmptyString(lkz)) {
				MandantDto mandantDto = null ;

				try {
					mandantDto = getMandantFac().mandantFindByPrimaryKey(
								getAuthController().getWebClientDto().getMandant(), getAuthController().getWebClientDto());
				} catch(RemoteException e) {
					myLogger.error("Kann Mandant " + getAuthController().getWebClientDto().getMandant() + " nicht ermitteln:" + e.getMessage()) ;
					return null ;
				}
		
				lkz = mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz() ;
			}
						
			String city = null ;
			if(ot2address.getCITY() != null && ot2address.getCITY().size() > 0) {
				city = ot2address.getCITY().get(0).getValue().trim() ;
			}			
			if(city == null) return null ;
			
			String plz = "" ;
			if(ot2address.getZIP() != null && ot2address.getZIP().size() > 0) {
				plz = ot2address.getZIP().get(0).getValue().trim() ;
			}
			
			LandDto landDto = getSystemFac().landFindByLkz(lkz) ;
			Integer landIId = null ;
			
			if(landDto == null) {
				landDto = new LandDto() ;
				landDto.setCLkz(lkz) ;
				landDto.setCName(lkz) ;
				landDto.setILaengeuidnummer(0) ;
				landDto.setBSepa(new Short((short)0)) ;
				landIId = getSystemFac().createLand(landDto, getAuthController().getWebClientDto()) ;
				landDto = getSystemFac().landFindByPrimaryKey(landIId) ;
				
				addChangeEntry(new ChangeEntry<String>("", lkz, "Das Land {1} wurde mit Standardwerten neu angelegt!")) ;
			} else {
				landIId = landDto.getIID() ;
			}
			
			OrtDto ortDto = getSystemFac().ortFindByNameOhneExc(city) ;
			Integer ortIId ;
			if(ortDto == null) {
				ortDto = new OrtDto() ;
				ortDto.setCName(city) ;
				ortIId = getSystemFac().createOrt(ortDto, getAuthController().getWebClientDto()) ;
				ortDto = getSystemFac().ortFindByPrimaryKey(ortIId) ;
			} else {
				ortIId = ortDto.getIId() ;
			}
			
			LandplzortDto lpoDto = getSystemFac().landplzortFindByLandOrtPlzOhneExc(
					landDto.getIID(), ortDto.getIId(), plz) ; 
			
			if(lpoDto != null) return lpoDto ;
			
			lpoDto = new LandplzortDto() ;
			lpoDto.setIlandID(landIId) ;
			lpoDto.setLandDto(landDto) ;
			lpoDto.setCPlz(plz) ;
			lpoDto.setOrtDto(ortDto) ;
			lpoDto.setOrtIId(ortIId) ;
			Integer lpoIId = getSystemFac().createLandplzort(lpoDto, getAuthController().getWebClientDto()) ;
			return getSystemFac().landplzortFindByPrimaryKey(lpoIId) ;
		}
		
		private WebshopPartner createPartnerDtoFromAddress(OT2PARTY party) throws RemoteException {
			OT2ADDRESS ot2address = findFirstNonEmptyAddress(party.getADDRESS()) ;
			if(ot2address == null) return null ;
			
			WebAddress webAddress = new WebAddress(ot2address) ;
			PartnerDto partnerDto = new PartnerDto() ;
			if(webAddress.isPerson()) {
				partnerDto.setCName2vornamefirmazeile2(webAddress.getWebPerson().getFirstName()) ;
				partnerDto.setCName1nachnamefirmazeile1(webAddress.getWebPerson().getLastName()) ;
				partnerDto.setAnredeCNr(webAddress.getWebPerson().getAnrede()) ;				
			} else {
				partnerDto.setCName1nachnamefirmazeile1(webAddress.getName()) ;
				partnerDto.setCName2vornamefirmazeile2(webAddress.getName2()) ;
				partnerDto.setCName3vorname2abteilung(webAddress.getDepartment()) ;
				partnerDto.setAnredeCNr(webAddress.getAnrede()) ;				
			}
			
			partnerDto.setCKbez(partnerDto.getCName1nachnamefirmazeile1()) ;

			if(!isEmptyString(ot2address.getVATID())) {
				partnerDto.setCUid(ot2address.getVATID()) ;
			}
			
			if(ot2address.getSTREET().size() > 0) {
				partnerDto.setCStrasse(ot2address.getSTREET().get(0).getValue()) ;						
			}
			if(ot2address.getPHONE().size() > 0) {
				partnerDto.setCTelefon(ot2address.getPHONE().get(0).getValue()) ;						
			}
			if(ot2address.getFAX().size() > 0) {
				partnerDto.setCFax(ot2address.getFAX().get(0).getValue()) ;						
			}

			List<Object> emails = ot2address.getEMAILAndPUBLICKEY() ;
			if(emails != null && emails.size() > 0) {
				partnerDto.setCEmail((String)emails.get(0)) ;
			}
			
			partnerDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE) ;
			partnerDto.setBVersteckt(false) ;
			partnerDto.setLocaleCNrKommunikation(getAuthController().getWebClientDto().getLocMandantAsString()) ;
			partnerDto.setLandplzortDto(createLandplzortIfNeeded(ot2address)) ;
			partnerDto.setLandplzortIId(partnerDto.getLandplzortDto() != null ? partnerDto.getLandplzortDto().getIId() : null) ;
			
			Integer partnerIId = getPartnerFac().createPartner(partnerDto, getAuthController().getWebClientDto()) ;
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, getAuthController().getWebClientDto()) ;

			WebshopPartner webPartner = new WebshopPartner(partnerDto) ;
			if(!webAddress.isPerson() && webAddress.getWebPerson() != null) {
				WebPerson webPerson = webAddress.getWebPerson() ;
				
				PartnerDto pDto = new PartnerDto() ;
				pDto.setCName1nachnamefirmazeile1(webPerson.getLastName()) ;
				pDto.setCName2vornamefirmazeile2(webPerson.getFirstName());
				pDto.setAnredeCNr(webPerson.getAnrede()) ;
				pDto.setCTitel(webPerson.getAcademicTitle()) ;
				pDto.setCKbez(partnerDto.getCName1nachnamefirmazeile1()) ;
				pDto.setCStrasse(partnerDto.getCStrasse()) ;
				pDto.setCTelefon(webPerson.getPhone()) ;
				pDto.setCFax(webPerson.getFax()) ;
				pDto.setCEmail(webPerson.getEmail()) ;
				pDto.setPartnerartCNr(PartnerFac.PARTNERART_ANSPRECHPARTNER) ;
				pDto.setBVersteckt(false) ;
				pDto.setLocaleCNrKommunikation(getAuthController().getWebClientDto().getLocMandantAsString()) ;
				pDto.setLandplzortDto(partnerDto.getLandplzortDto()) ;
				pDto.setLandplzortIId(partnerDto.getLandplzortDto() != null ? partnerDto.getLandplzortDto().getIId() : null) ;
				Integer ansprechpartnerPartnerId = getPartnerFac().createPartner(pDto, getAuthController().getWebClientDto()) ;
				
				Map<String,String> allFunctions = (Map<String,String>)getAnsprechpartnerFac().getAllAnsprechpartnerfunktion(getAuthController().getWebClientDto().getLocUiAsString(),
						getAuthController().getWebClientDto()) ; 
				AnsprechpartnerDto ansprechpartnerDto = new AnsprechpartnerDto() ;
				ansprechpartnerDto.setCEmail(webPerson.getEmail()) ;
				ansprechpartnerDto.setCTelefon(webPerson.getPhone()) ;
				ansprechpartnerDto.setCFax(webPerson.getFax()) ;
				ansprechpartnerDto.setPartnerDto(partnerDto) ;
				ansprechpartnerDto.setPartnerIId(partnerDto.getIId()) ;
				ansprechpartnerDto.setPartnerIIdAnsprechpartner(ansprechpartnerPartnerId) ;
				ansprechpartnerDto.setXBemerkung(webPerson.getDescription()) ;
				
				AnsprechpartnerfunktionDto aDto = getAnsprechpartnerFac()
						.ansprechpartnerfunktionFindByCnr(allFunctions.values().iterator().next(), getAuthController().getWebClientDto()) ;				
				ansprechpartnerDto.setAnsprechpartnerfunktionIId(aDto.getIId()) ;
				ansprechpartnerDto.setCFremdsystemnr(webPerson.getContactId()) ;
				ansprechpartnerDto.setDGueltigab(new java.sql.Date(GregorianCalendar.getInstance().getTime().getTime())) ;
				
				Integer id = getAnsprechpartnerFac().createAnsprechpartner(ansprechpartnerDto, getAuthController().getWebClientDto()) ;
				AnsprechpartnerDto apDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(id, getAuthController().getWebClientDto()) ;
				webPartner.setAnsprechpartnerDto(apDto) ;
			}
			
			return webPartner ;
		}
		
		
		private KundeDto createKundeDtoFromPartnerDto(OT2PARTY party, PartnerDto partnerDto) {
			MandantDto mandantDto = null ;
			try {
				mandantDto = getMandantFac().mandantFindByPrimaryKey(
							getAuthController().getWebClientDto().getMandant(), getAuthController().getWebClientDto());
			} catch(RemoteException e) {
				myLogger.error("Kann Mandant " + getAuthController().getWebClientDto().getMandant() + " nicht ermitteln:" + e.getMessage()) ;
				return null ;
			}
			
			KundeDto kundeDto = new KundeDto() ;
			kundeDto.setPartnerDto(partnerDto) ;
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
			kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
			kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));

			// Default 'Akzeptiert Teillieferung'
			kundeDto.setBAkzeptiertteillieferung(getParameterKundeAkzeptiertTeillieferung());

			Integer personalId = getAuthController().getWebClientDto().getIDPersonal() ;
			kundeDto.setPersonaliIdProvisionsempfaenger(personalId) ;
			kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());

			kundeDto.setPersonalAnlegenIID(personalId) ;
			kundeDto.setPersonalAendernIID(personalId) ;
			Timestamp t = getTimestamp() ;
			kundeDto.setTAnlegen(t) ;
			kundeDto.setTAendern(t) ;

			TypePARTYID externalPartyId = findExternalPartyId(party) ;
			if(externalPartyId != null) {
				kundeDto.setCIdExtern(externalPartyId.getValue()) ;
			}
			
			try {
				Integer kundeId = getKundeFac().createKunde(kundeDto, getAuthController().getWebClientDto()) ;
				kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, getAuthController().getWebClientDto()) ;
				
				createPartnerSelektionen(kundeDto) ;
			} catch(RemoteException e) {
				myLogger.error("Kann kunden nicht anlegen: " + e.getMessage()) ;
				return null ;
			}
			
			return kundeDto ;
		}
		
		private void createPartnerSelektionen(KundeDto kundeDto) throws EJBExceptionLP, RemoteException {
			List<Selektion> selektions = SelektionQuery
					.listByMandantCNrForWebshop(em, getAuthController().getWebClientDto().getMandant()) ;
			for (Selektion selektion : selektions) {
				PASelektionDto paSelektionDto = new PASelektionDto() ;
				paSelektionDto.setSelektionIId(selektion.getIId()) ;
				paSelektionDto.setPartnerIId(kundeDto.getPartnerIId()) ;
				getPartnerFac().createPASelektion(paSelektionDto, getAuthController().getWebClientDto()) ;				
			}			
		}
		
		private WebshopPartner findKundeImpl(OT2PARTY theParty) {
			if(theParty == null) return null ;
			
			OT2ADDRESS ot2address = findFirstNonEmptyAddress(theParty.getADDRESS()) ;
			if(ot2address == null) return null ;

			WebAddress webAddress = new WebAddress(ot2address) ;
			
			TypePARTYID buyerPartyId = findPartyId(theParty) ;
			KundeDto kundeDto = findKundeDtoFromPartyId(buyerPartyId)  ;
			if(kundeDto != null) {
				AnsprechpartnerDto aDto = acceptableAnsprechpartnerDtoForExternalSystem(kundeDto, webAddress) ;
				return new WebshopPartner(kundeDto, aDto) ; 
			}

			TypePARTYID externalPartyId = findExternalPartyId(theParty) ;
			kundeDto = findKundeDtoFromAddress(webAddress, externalPartyId != null ? externalPartyId.getValue() : null) ;
			if(kundeDto != null) {
				AnsprechpartnerDto aDto = acceptableAnsprechpartnerDtoForExternalSystem(kundeDto, webAddress) ;
				return new WebshopPartner(kundeDto, aDto) ; 
			}
			
			WebshopPartner webpartnerDto = null ;
			try {
				webpartnerDto = createPartnerDtoFromAddress(theParty) ;
			} catch(RemoteException e) {
				myLogger.error("Kann Partner nicht anlegen: " + e.getMessage()) ;
			}

			if(webpartnerDto == null) return null ;
			
			kundeDto = createKundeDtoFromPartnerDto(theParty, webpartnerDto.getPartnerDto()) ; 
			webpartnerDto.setKundeDto(kundeDto) ;
			return webpartnerDto ;
		}
	}
	
	
	
	class ArtikelFinderWebshop implements ArtikelFinder {
		private OT2ORDERITEM xmlOrderItem ;
		
		public ArtikelFinderWebshop(OT2ORDERITEM convertedOrderItem) {
			xmlOrderItem = convertedOrderItem ;
		}
  
		@Override
		public String getUsedItemCnr() {
			return null;
		}
		
		@Override
		public ArtikelDto findArtikel(KundeDto kundeDto, AuftragDto auftragDto) {
			ArtikelDto artikelDto = null ;
			Integer itemId = findItemId(xmlOrderItem.getPRODUCTID()) ;
			if(itemId != null) {
				artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmallOhneExc(itemId, getAuthController().getWebClientDto()) ;
				if(artikelDto == null) {
					addChangeEntry(new ChangeEntry<String>(
							"", itemId.toString(), "Die Helium V Artikel-Id vom Webshop ist unbekannt")) ;				
				}
			} else {
				addChangeEntry(new ChangeEntry<String>(
						"", "", "Es wurde keine Helium V Artikel-Id vom Webshop uebermittelt")) ;
			}
			if(artikelDto != null) return artikelDto ;
			
			String itemCnr = findItemCnr(xmlOrderItem.getPRODUCTID()) ;
			if(itemCnr != null) {
				try {
					artikelDto = getArtikelFac()
						.artikelFindByCNrOhneExc(itemCnr, getAuthController().getWebClientDto()) ;
					if(artikelDto == null) {
						addChangeEntry(new ChangeEntry<String>(
								"", itemCnr, "Die Helium V Artikel-Nr '{1}' vom Webshop ist unbekannt")) ;						
					}

				} catch(RemoteException e) {}
			}
			return artikelDto ;
		}
		
		public Integer findItemId(OT2PRODUCTID productId) {
			if(productId.getSUPPLIERPID() == null) return null ;
			
			BCSUPPLIERPID supplierPid = productId.getSUPPLIERPID() ;
			if("supplier_specific".equals(supplierPid.getType())) {
				try {
					return Integer.parseInt(supplierPid.getValue()) ;
				} catch(NumberFormatException e) {
				}
			}
			
			return null ;
		}
		
		public String findItemCnr(OT2PRODUCTID productId) {
			return null ;
		}
		
		private Integer findItemHeliumId(OT2PRODUCTID productId) {
			if(productId.getINTERNATIONALPID() == null) return null ;
			
			for (BCINTERNATIONALPID internationlPid : productId.getINTERNATIONALPID()) {
				if("heliumv".equals(internationlPid.getType())) {
					try {
						return Integer.parseInt(internationlPid.getValue()) ;
					} catch(NumberFormatException e) {
					}
				}
			}
			
			return null ;			
		}
	}
	
	class AuftragFinderWebshop implements AuftragFinder {
		private OT2ORDER xmlOrder ;
		private KundeFinder kundeFinder ;
		private Timestamp earliestDeliveryTime ;
		private Timestamp latestDeliveryTime ;
		private int nachkommastellenPreiseVK = -1 ;
		
		public AuftragFinderWebshop(OT2ORDER convertedOrder, KundeFinder theKundeFinder) {
			xmlOrder = convertedOrder ;
			kundeFinder = theKundeFinder ;
		}
		
		private OT2ORDER getXmlOrder() {
			return xmlOrder ;
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
			if(earliestDeliveryTime != null) return earliestDeliveryTime ;			
			return getNextDayAsTimestamp();
		}

		private Timestamp getLatestDeliveryTime() {
			if(latestDeliveryTime != null) return latestDeliveryTime ;
			return getNextDayAsTimestamp();
		}
		
		private Timestamp getNextDayAsTimestamp() {
			Calendar cal = Calendar.getInstance(getAuthController().getWebClientDto().getLocMandant()) ;
			cal.add(Calendar.DAY_OF_MONTH, 1) ;
			return new Timestamp(cal.getTimeInMillis()) ;
		}

		@Override
		public AuftragDto[] findAuftrag(KundeDto kundeDto) {
			if(null == kundeDto) return null ;
			
			if(!getOrderAdapter().hasBestellnummer()) {
				myLogger.error("Die XML-Struktur enth\u00E4lt keine auswertbare OrderInfo/OrderId/Bestellnummmer") ;
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

		@Override
		public AuftragDto createAuftrag(KundeDto kundeDto) {
			AuftragDto[] dtos = findAuftrag(kundeDto) ;
			if(dtos != null && dtos.length > 0) {
				myLogger.warn("Der Auftrag '" + dtos[0].getCNr()
						+ "' existiert mit der Bestellnummer '" 
						+ dtos[0].getCBestellnummer() 
						+ "'. Kein weiterer Auftrag angelegt!") ;
				
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(dtos[0].getCNr())) ;
			}

			String orderCurrency = getOrderAdapter().getWaehrung() ;
			if(isEmptyString(orderCurrency)) {
				orderCurrency = kundeDto.getCWaehrung() ;
			}
			
			if(!isValidCurrency(kundeDto, orderCurrency)) {
				addChangeEntry(new ChangeEntry<String>(kundeDto.getCWaehrung(), orderCurrency, 
					"Die Auftragsw\u00E4hrung {1} entspricht nicht der Kundenw\u00E4hrung {0}. Es wird die Kundenw\u00E4hrung {0} verwendet")) ;
			}
						
			AuftragDto returnDto = null ;

			try {
				AuftragDto auftragDto = buildAuftragDto(kundeDto, getOrderAdapter()) ;
//				KundeDto lieferKundeDto = kundeFinder.findKundeDelivery() ;
				WebshopPartner deliveryPartner = kundeFinder.findKundeDelivery() ;
				KundeDto lieferKundeDto = deliveryPartner != null ? deliveryPartner.getKundeDto() : null ;
				auftragDto.setKundeIIdLieferadresse(
						lieferKundeDto != null ? lieferKundeDto.getIId() : auftragDto.getKundeIIdAuftragsadresse()) ;
				if(lieferKundeDto != null && deliveryPartner.getAnsprechpartnerDto() != null) {
					auftragDto.setAnsprechpartnerIIdLieferadresse(deliveryPartner.getAnsprechpartnerDto().getIId()) ;
				}
				
//				KundeDto rechnungKundeDto = kundeFinder.findKundeInvoice() ;
				WebshopPartner invoicePartner = kundeFinder.findKundeInvoice() ;
				KundeDto rechnungKundeDto = invoicePartner != null ? invoicePartner.getKundeDto() : null ;
				auftragDto.setKundeIIdRechnungsadresse(
						rechnungKundeDto != null ? rechnungKundeDto.getIId() : auftragDto.getKundeIIdAuftragsadresse()) ;
				if(rechnungKundeDto != null && invoicePartner.getAnsprechpartnerDto() != null) {
					auftragDto.setAnsprechpartnerIIdRechnungsadresse(invoicePartner.getAnsprechpartnerDto().getIId()) ;
				}
				
				KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(kundeDto.getIId(), getAuthController().getWebClientDto()) ;
				kundeFinder.validateKunde(kDto) ;
				KundeDto lDto = getKundeFac().kundeFindByPrimaryKey(lieferKundeDto.getIId(), getAuthController().getWebClientDto()) ;
				kundeFinder.validateKundeDelivery(lDto) ;
				KundeDto rDto = getKundeFac().kundeFindByPrimaryKey(rechnungKundeDto.getIId(), getAuthController().getWebClientDto()) ;
				kundeFinder.validateKundeInvoice(rDto) ;
				
				Integer iid = getAuftragFac().createAuftrag(auftragDto, getAuthController().getWebClientDto()) ;
				
				returnDto = getAuftragFac().auftragFindByPrimaryKey(iid) ;
			} catch(RemoteException e) {				
			}

			return returnDto ;
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
		}
		
		private BigDecimal getPrice(KundeDto kundeDto, AuftragDto auftragDto, ArtikelDto itemDto, BigDecimal quantity) {
			java.sql.Date d = new java.sql.Date(auftragDto.getDBestelldatum().getTime()) ;
			
			VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
					itemDto.getIId(), kundeDto.getIId(), quantity, d,
					kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), 
					itemDto.getMwstsatzbezIId(), auftragDto.getCAuftragswaehrung(), getAuthController().getWebClientDto()) ;

			BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto) ;
			return p ;
		}
		
		private BigDecimal getXmlPrice(BigDecimal quantity, OT2ORDERITEM xmlPosition) {
			BigDecimal lineItemAmount = xmlPosition.getPRICELINEAMOUNT() ;
			OT2PRODUCTPRICEFIX priceFix = xmlPosition.getPRODUCTPRICEFIX() ;
			if(lineItemAmount == null && priceFix == null) return null ;
			
			BigDecimal priceForOne = null ;
			if(priceFix != null) {
				// TODO: Abkuerzung: priceFix.getPRICEQUANTITY() noch nicht beruecksichtigt
				priceForOne = priceFix.getPRICEAMOUNT() ;				
				return priceForOne ;
			}
			
			if(lineItemAmount != null) {
				priceForOne = lineItemAmount.divide(quantity) ;
			}
			
			return priceForOne ;
		}
		
		private BigDecimal getXmlTax(OT2ORDERITEM xmlPosition) {
			OT2PRODUCTPRICEFIX priceFix = xmlPosition.getPRODUCTPRICEFIX() ;
			if(priceFix == null) return null ;
			
			List<OT2TAXDETAILSFIX> taxDetails = priceFix.getTAXDETAILSFIX() ;
			if(taxDetails.size() > 0) {
				return taxDetails.get(0).getTAX() ;
			} 
			
			return null ;
		}
		
		private boolean hasDeliveryStartDate(OT2ORDERITEM xmlPosition) {
			return 
					xmlPosition.getDELIVERYDATE() != null &&
					xmlPosition.getDELIVERYDATE().getDELIVERYSTARTDATE() != null &&
					xmlPosition.getDELIVERYDATE().getDELIVERYSTARTDATE().trim().length() != 0 ;
		}

		private boolean hasDeliveryEndDate(OT2ORDERITEM xmlPosition) {
			return 
					xmlPosition.getDELIVERYDATE() != null &&
					xmlPosition.getDELIVERYDATE().getDELIVERYENDDATE() != null &&
					xmlPosition.getDELIVERYDATE().getDELIVERYENDDATE().trim().length() != 0 ;
		}
		
		private AuftragpositionDto createPositionIdent(KundeDto kundeDto, AuftragDto auftragDto, ArtikelDto itemDto, OT2ORDERITEM xmlPosition) throws RemoteException {
			AuftragpositionDto positionDto = new AuftragpositionDto() ;
			positionDto.setBelegIId((auftragDto.getIId())) ;
			positionDto.setArtikelIId(itemDto.getIId()) ;
			positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT) ;
			// positionDto.setCBez(xmlPosition.getARTICLEID().getDESCRIPTIONSHORT()) ;
			
			BigDecimal xmlQuantity = xmlPosition.getQUANTITY() ;
			if(xmlQuantity == null || xmlQuantity.signum() <= 0) {
				xmlQuantity = BigDecimal.ONE ;
			}
			positionDto.setNMenge(xmlQuantity) ;
			positionDto.setNOffeneMenge(xmlQuantity) ;
			positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
			
			String einheit = xmlPosition.getORDERUNIT() ;
			if(isEmptyString(einheit)) {
				einheit = itemDto.getEinheitCNr() ;
			}
			if(!isValidEinheitCnr(einheit)) {
				String validEinheit = itemDto.getEinheitCNr() ;
				addChangeEntry(new ChangeEntry<String>(validEinheit, einheit, 
						"Die Einheit {1} wurde nicht gefunden, stattdessen wird {0} verwendet.")) ;
				einheit = validEinheit ;
			}
			
			positionDto.setEinheitCNr(einheit) ;

// Der Steuersatz des Artikels wird ignoriert, es wird immer der beim Kunden hinterlegte verwendet			
//			if(null == itemDto.getMwstsatzbezIId()) {
//				itemDto.setMwstsatzbezIId(kundeDto.getMwstsatzbezIId()) ;
//			}
			
			// Es wird immer der Mehrwertsteuersatz vom Kunden verwendet
			// Hintergrund: Im Shop bestellen neben Endkunden auch Haendler
			// auch Endkunden und Mini-Haendler in Deutschland die eben Mwst zahlen muessen
			itemDto.setMwstsatzbezIId(kundeDto.getMwstsatzbezIId()) ;
			
			BigDecimal vkPrice = getPrice(kundeDto, auftragDto, itemDto, positionDto.getNMenge()) ;
			BigDecimal xmlPrice = getXmlPrice(positionDto.getNMenge(), xmlPosition) ;
			
			if(xmlPrice != null && vkPrice.compareTo(xmlPrice) != 0) {
				addChangeEntry(new ChangeEntry<BigDecimal>(vkPrice, xmlPrice, 
						"Der angegebene Preis {1} entspricht nicht dem hinterlegtem Preis {0}. Es wird der Shop-Preis {1} verwendet.")) ;				
				vkPrice = xmlPrice ;
			}

// Der Steuersatz vom Shop wird ignoriert			
//			BigDecimal xmlTax = getXmlTax(xmlPosition) ;
//			if(xmlTax != null) {
//				Integer xmlMwstSatz = getMwstsatzIIdForTax(xmlTax.movePointRight(2)) ;
//				if(xmlMwstSatz == null) {
//					addChangeEntry(new ChangeEntry<BigDecimal>(BigDecimal.ZERO, xmlTax, "Der MwSt.Satz {1} des Webshops ist in Helium V unbekannt.")) ;
//				} else {
//					if(itemDto.getMwstsatzbezIId() != null && !xmlMwstSatz.equals(itemDto.getMwstsatzbezIId())) {
//						ChangeEntry<?> changeEntry = new ChangeEntry<Integer>(itemDto.getMwstsatzbezIId(), xmlMwstSatz, "Der MwSt.Satz {1} ist nicht mit dem Artikel MwstSatz {0} ident, es wird {0} verwendet.") ;
//						addChangeEntry(changeEntry) ;
//					}	
//				}
//			}
				
			positionDto.setNEinzelpreis(vkPrice) ;
			positionDto.setNNettoeinzelpreis(vkPrice) ;
			positionDto.setNBruttoeinzelpreis(vkPrice) ;
			positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false)) ;
//			positionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setBDrucken(Helper.boolean2Short(true)) ;
			positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
			positionDto.setFRabattsatz(new Double(0.0)) ;
			positionDto.setFZusatzrabattsatz(new Double(0.0)) ;
			positionDto.setNRabattbetrag(BigDecimal.ZERO) ;
			positionDto.setNMaterialzuschlag(BigDecimal.ZERO) ;
			positionDto.setMwstsatzIId(itemDto.getMwstsatzbezIId()) ;
			updateMwstBetraege(positionDto);
			
			Timestamp earliest = null ;
			Timestamp latest = null ;
			
			if(hasDeliveryStartDate(xmlPosition)) {
				earliest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYSTARTDATE()).getTime()) ;
				updateEarliestDeliveryTime(earliest) ;
			}

			if(hasDeliveryEndDate(xmlPosition)) {
				latest = new Timestamp(
					getDateFromDateStringWithDefault(xmlPosition.getDELIVERYDATE().getDELIVERYENDDATE()).getTime()) ;
				updateLatestDeliveryTime(latest) ;
			}

			if(earliest == null) {
				earliest = new Timestamp(Calendar.getInstance().getTime().getTime()) ;
			}
			
			positionDto.setTUebersteuerbarerLiefertermin(earliest) ;
			
			Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, getAuthController().getWebClientDto()) ; 
			return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
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
	
		@Override
		public void createPositions(KundeDto kundeDto, AuftragDto auftragDto) {
			try {
				createPositionsForChangeEntries(auftragDto) ;
	
				resetDeliveryTimestamps() ;
				
				for (OT2ORDERITEM orderItem : getXmlOrder().getORDERITEMLIST().getORDERITEM()) {
					String lineItemId = orderItem.getLINEITEMID() ;
					OT2PRODUCTID productId = orderItem.getPRODUCTID() ;
					if(productId == null) {
						ChangeEntry<?> changeEntry = new ChangeEntry<String>(lineItemId, "", 
								"Fuer die Auftragszeile {0} gibt es keine Produktdaten!") ;
						addChangeEntry(changeEntry) ;
						continue ;
					}
					
					setupArtikelFinder(orderItem) ;
					ArtikelDto itemDto = getArtikelFinder().findArtikel(kundeDto, auftragDto) ; 
					if(itemDto != null) {
						 createPositionIdent(kundeDto, auftragDto, itemDto, orderItem) ;
					}
					
					createPositionsForChangeEntries(auftragDto) ;					
				}
						
				auftragDto.setDLiefertermin(getEarliestDeliveryTime()) ;
				auftragDto.setDFinaltermin(getLatestDeliveryTime()) ;
				
				getAuftragFac().updateAuftragOhneWeitereAktion(auftragDto, getAuthController().getWebClientDto()) ;				
   			} catch(RemoteException e) {
			}
		}			
	}
}
