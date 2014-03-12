/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.partner.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LandplzortDto;

public class BmeCatTransformerKunde extends BmeCatTransformerBase {
	private Map<Integer, String> pricelistMap ;
	
	public BmeCatTransformerKunde() {		
		pricelistMap = new HashMap<Integer, String> () ;
	}
	
	public BmeCatTransformerKunde(Map<Integer, String> pricelistCache) {
		pricelistMap = pricelistCache ;
	}

	private String getPricelistName(Integer pricelistId) {
		return pricelistMap.get(pricelistId) ;
	}

	
//	/**
//	 * KundenDto(s) in eine Parties-Liste wandeln
//	 * 
//	 * @param kundeDtos
//	 * @return
//	 */
//	public Document transform(List<KundeDto> kundeDtos) {
//		Document d = null ;
//		
//		try {
//			d = createDocument() ;
//
//			Element parties = d.createElement("PARTIES") ;
//			d.appendChild(parties) ;
//
//			for (KundeDto kundeDto : kundeDtos) {
//				List<Element> p = transform(d, kundeDto) ;
//				appendChildNodes(parties, p) ;			
//			}
//		} catch(Exception e) {
//			
//		}
//		
//		return d ;
//	}
//	
	
	public Document transform(KundeDto kundeDto, AnsprechpartnerDto[] ansprechpartnerDtos) {
		Document d = null ;
		try {
			d = createDocument() ;

			Element parties = d.createElement("PARTIES") ;
			d.appendChild(parties) ;
			List<Element> p = transform(d, kundeDto, ansprechpartnerDtos) ;
			appendChildNodes(parties, p) ;
		} catch(Exception e) {			
		}

		return d ;
	}
	
	protected List<Element> transform(Document d, KundeDto kundeDto, AnsprechpartnerDto[] ansprechpartnerDtos) {
		return createParty(d, kundeDto, ansprechpartnerDtos) ;
	}

	private List<Element> createParty(Document d, KundeDto kundeDto, AnsprechpartnerDto[] ansprechpartnerDtos) {		
		List<Element> elements = new ArrayList<Element>() ;
	
		elements.add(createOneParty(d, kundeDto, "buyer", kundeDto.getPartnerDto(), kundeDto.getIId(), ansprechpartnerDtos)) ;

// TODO: Voerst nicht uebermittelt, da sie die gleiche heliumv-Id haben		
//		if(kundeDto.getPartnerIIdRechnungsadresse() != null) {
//			elements.add(createOneParty(d, kundeDto, "invoice_recipient", kundeDto.getPartnerRechnungsadresseDto(), kundeDto.getIId(), new AnsprechpartnerDto[]{})) ;
//		}
	
		return elements ;
	}
	
	private Element createOneParty(Document d, KundeDto kundeDto, String partyType, PartnerDto partnerDto, Integer customerId, AnsprechpartnerDto[] ansprechpartnerDtos) {
		Element oneParty = d.createElement("PARTY") ;
		appendChildNodes(oneParty, createPartyId(d, kundeDto)) ;
		appendChildNodes(oneParty, createPartyRole(d, kundeDto, partyType)) ;
		oneParty.appendChild(createPartyAddress(d, partnerDto, customerId, ansprechpartnerDtos)) ;	
		
		return oneParty ;
	}
	
	
	private List<Element> createPartyId(Document d, KundeDto kundeDto) {
		List<Element> ids = new ArrayList<Element>() ;
		ids.add(createPartyIdImpl(d, "heliumv", kundeDto.getIId().toString())) ;

		if(kundeDto.getCIdExtern() != null) {
			ids.add(createPartyIdImpl(d, "customer_specific", kundeDto.getCIdExtern())) ;
		}
		
		if(kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste() != null) {
			Integer pricelistId = kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste() ;
			ids.add(createPartyIdImpl(
					d, "customer_group", pricelistId.toString())) ;
			ids.add(createPartyIdImpl(
					d, "customer_group_name", getPricelistName(pricelistId))) ;
		}
		return ids ;
	}
	
	private Element createPartyIdImpl(Document d, String theType, String value) {
		Element e = d.createElement("PARTY_ID") ;
		e.setAttribute("type", theType) ;
		e.setTextContent(value) ;
		return e ;
	}
	
	private List<Element> createPartyRole(Document d, KundeDto kundeDto, String role) {
		List<Element> roles = new ArrayList<Element>() ;
		Element buyerRole = d.createElement("PARTY_ROLE") ;
		buyerRole.setTextContent(role) ;
		roles.add(buyerRole) ;

//		if(Helper.short2boolean(kundeDto.getBVersteckterlieferant())) {
//			Element supplierRole = d.createElement("PARTY_ROLE") ;
//			supplierRole.setTextContent("supplier") ;
//			roles.add(supplierRole) ;
//		}

		return roles;
	}
	
	private Element createPartyAddress(Document d, PartnerDto partnerDto, Integer customerId, AnsprechpartnerDto[] ansprechpartnerDtos) {
		Element address = d.createElement("ADDRESS") ;

		if(isNaturalPerson(partnerDto)) {
			Element contactDetails = d.createElement("CONTACT_DETAILS") ;
			appendTextContentIfNotEmpty(d, contactDetails, "CONTACT_ID", customerId.toString()) ;
			appendTextContentIfNotEmpty(d, contactDetails, "CONTACT_NAME", partnerDto.getCName1nachnamefirmazeile1()) ;
			appendTextContentIfNotEmpty(d, contactDetails, "FIRST_NAME", partnerDto.getCName2vornamefirmazeile2()) ;
			appendTextContentIfNotEmpty(d, contactDetails, "ACADEMIC_TITLE", partnerDto.getCTitel()) ;
			appendTextContentIfNotEmpty(d, contactDetails, "TITLE", partnerDto.getAnredeCNr().trim()) ;
			address.appendChild(contactDetails) ;
 		} else {
			appendTextContentIfNotEmpty(d, address, "NAME", partnerDto.getCName1nachnamefirmazeile1()) ;
			appendTextContentIfNotEmpty(d, address, "NAME2", partnerDto.getCName2vornamefirmazeile2()) ;
			appendTextContentIfNotEmpty(d, address, "DEPARTMENT", partnerDto.getCName3vorname2abteilung()) ;
			
			appendChildNodes(address, createContactDetails(d, ansprechpartnerDtos)) ;
 		}
		
		appendTextContentIfNotEmpty(d, address, "STREET", partnerDto.getCStrasse()) ;
		if(partnerDto.getLandplzortDto() != null) {
			LandplzortDto lpzoDto = partnerDto.getLandplzortDto() ;
			appendTextContentIfNotEmpty(d, address, "ZIP", lpzoDto.getCPlz()) ;
			if(lpzoDto.getOrtDto() != null) {
				appendTextContentIfNotEmpty(d, address, "CITY", lpzoDto.getOrtDto().getCName()) ;
			}
			if(lpzoDto.getLandDto() != null) {
				appendTextContentIfNotEmpty(d, address, "COUNTRY", lpzoDto.getLandDto().getCName()) ;
				appendTextContentIfNotEmpty(d, address, "COUNTRY_CODED", lpzoDto.getLandDto().getCLkz()) ;
			}
		}
		
		appendTextContentIfNotEmpty(d, address, "VAT_ID", partnerDto.getCUid()) ;
		appendTextContentIfNotEmpty(d, address, "PHONE", partnerDto.getCTelefon()) ;
		appendTextContentIfNotEmpty(d, address, "FAX", partnerDto.getCFax()) ;
		appendTextContentIfNotEmpty(d, address, "EMAIL", partnerDto.getCEmail()) ;
		appendTextContentIfNotEmpty(d, address, "URL", partnerDto.getCHomepage()) ;
			
		return address ;
	}
	
	private List<Element> createContactDetails(Document d, AnsprechpartnerDto[] ansprechpartnerDtos) {
		List<Element> details = new ArrayList<Element>() ;
		for (AnsprechpartnerDto ansprechpartnerDto : ansprechpartnerDtos) {
			PartnerDto partnerDto = ansprechpartnerDto.getPartnerDto() ;
			Element detail = d.createElement("CONTACT_DETAILS") ;
			
			appendTextContentIfNotEmptyNodes(d, detail, new TagValuePair[] {
					new TagValuePair("CONTACT_ID", ansprechpartnerDto.getCFremdsystemnr()),
					new TagValuePair("CONTACT_NAME", partnerDto.getCName1nachnamefirmazeile1()), 
					new TagValuePair("FIRST_NAME", partnerDto.getCName2vornamefirmazeile2()),
					new TagValuePair("TITLE", partnerDto.getAnredeCNr().trim()),
					new TagValuePair("ACADEMIC_TITLE", partnerDto.getCTitel()),
					new TagValuePair("CONTACT_DESCR", ansprechpartnerDto.getXBemerkung()),
					new TagValuePair("PHONE", ansprechpartnerDto.getCTelefon()),
					new TagValuePair("FAX", ansprechpartnerDto.getCFax())
			}) ;

			if(ansprechpartnerDto.getCEmail() != null && ansprechpartnerDto.getCEmail().trim().length() > 0) {
				Element emails = d.createElement("EMAILS") ;
				Element email = d.createElement("EMAIL") ;
				email.setTextContent(ansprechpartnerDto.getCEmail().trim()) ;
				emails.appendChild(email) ;
				detail.appendChild(emails) ;
			}

			details.add(detail) ;
		}
	
		return details ;
	} 
	
	private boolean isNaturalPerson(PartnerDto partnerDto) {
		if(PartnerFac.PARTNER_ANREDE_FRAU.equals(partnerDto.getAnredeCNr())) return true ;
		if(PartnerFac.PARTNER_ANREDE_HERR.equals(partnerDto.getAnredeCNr())) return true ;
		
		return false ;
	}
}
