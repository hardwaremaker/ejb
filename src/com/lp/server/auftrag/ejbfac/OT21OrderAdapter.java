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
package com.lp.server.auftrag.ejbfac;

import java.util.List;

import com.lp.server.schema.opentrans_2_1.OT2ORDER;
import com.lp.server.schema.opentrans_2_1.OT2PARTY;

public class OT21OrderAdapter implements IXmlOrderAdapter {

	private OT2ORDER xmlOrder ;
	private String webshopName ;
	private Integer webshopId ;
	
	public OT21OrderAdapter(OT2ORDER convertedOrder, String webshopName, Integer webshopId) {
		xmlOrder = convertedOrder ;
		this.webshopName = webshopName ;
		this.webshopId = webshopId ;
	}
	
	@Override
	public String getWaehrung() {
		if(xmlOrder.getORDERHEADER().getORDERINFO().getCURRENCY() == null) return "" ; 
		return xmlOrder.getORDERHEADER().getORDERINFO().getCURRENCY().value() ;
	}

	@Override
	public String getDatum() {
		return xmlOrder.getORDERHEADER().getORDERINFO().getORDERDATE() ;
	}

	@Override
	public boolean hasBestellnummer() {
		return null != xmlOrder && 
			   null != xmlOrder.getORDERHEADER() && 
			   null != xmlOrder.getORDERHEADER().getORDERINFO() &&
			   null != xmlOrder.getORDERHEADER().getORDERINFO().getORDERID() ;
	}

	@Override
	public String getBestellnummer() {
		return webshopId + "/" + xmlOrder.getORDERHEADER().getORDERINFO().getORDERID() ;
	}

	@Override
	public String getLieferart() {
		if(null == xmlOrder.getORDERHEADER().getORDERINFO().getTRANSPORT()) return "invalid_incoterm" ;
		return xmlOrder.getORDERHEADER().getORDERINFO().getTRANSPORT().getINCOTERM() ;
	}

	private OT2PARTY findInvoiceRecipient(List<OT2PARTY> xmlParties) {
		if(null == xmlParties || xmlParties.size() == 0) return null ;

		for (OT2PARTY ot2party : xmlParties) {				
			for (String role : ot2party.getPARTYROLE()) {
				if("invoice_recipient".equals(role)) return ot2party ;
				if("buyer".equals(role)) return ot2party ;
			}
		}
		
		return null ;
	}
	
	
	@Override
	public String getUidNummerFromInvoicePartner() {
		List<OT2PARTY> parties = xmlOrder.getORDERHEADER().getORDERINFO().getPARTIES().getPARTY() ;
		OT2PARTY invoiceParty = findInvoiceRecipient(parties) ;
		if(null == invoiceParty) return null ;

		return invoiceParty.getADDRESS().get(0).getVATID() ;
	}
	
	@Override
	public boolean isTeillieferungErlaubt() {
		String value = xmlOrder.getORDERHEADER().getORDERINFO().getPARTIALSHIPMENTALLOWED() ;
		if(value == null || value.trim().length() == 0) return true ;
		return "true".equals(value) ;
	}
}
