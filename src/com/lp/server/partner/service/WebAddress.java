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
package com.lp.server.partner.service;

import java.util.List;

import com.lp.server.schema.bmecat_2005.BCNAME;
import com.lp.server.schema.opentrans_2_1.OT2ADDRESS;
import com.lp.server.schema.opentrans_2_1.OT2CONTACTDETAILS;

public class WebAddress {
	private boolean person ;
	private String anrede ;
	private String name ;
	private String name2 ;
	private String name3 ;
	private String department ;
	private WebPerson webPerson ;
	
	public WebAddress(OT2ADDRESS ot2Address) {
		List<BCNAME> names = ot2Address.getNAME() ;
		if(names == null || names.size() == 0 || names.get(0).getValue().trim().length() == 0) {
			processContactDetails(ot2Address) ;
			person = getWebPerson() != null ;
			if(person) {
				anrede = getWebPerson().getAnrede() ;
			}
		} else {
			processBusinessAddress(ot2Address) ;
			processContactDetails(ot2Address) ;
		}
	}

	private void processBusinessAddress(OT2ADDRESS ot2Address) {
		name = ot2Address.getNAME().get(0).getValue() ;
		if(ot2Address.getNAME2() != null && ot2Address.getNAME2().size() > 0) {
			name2 = ot2Address.getNAME2().get(0).getValue() ;
		}
		if(ot2Address.getNAME3() != null && ot2Address.getNAME3().size() > 0) {
			name3 = ot2Address.getNAME3().get(0).getValue() ;
		}
		if(ot2Address.getDEPARTMENT() != null && ot2Address.getDEPARTMENT().size() > 0) {
			department = ot2Address.getDEPARTMENT().get(0).getValue() ;
		}
		
		anrede = PartnerFac.PARTNER_ANREDE_FIRMA ;
		person = false ;
	}
	
	private void processContactDetails(OT2ADDRESS ot2Address) {
		List<OT2CONTACTDETAILS> contactDetails = ot2Address.getCONTACTDETAILS() ;
		if(contactDetails != null && contactDetails.size() > 0) {
			setWebPerson(new WebPerson(contactDetails.get(0))) ;
		}
	}

	public boolean isPerson() {
		return person;
	}

	public String getAnrede() {
		return anrede ;
	}
	
	protected void setPerson(boolean person) {
		this.person = person;
	}


	public String getName() {
		return name;
	}

	protected void setName(String name) {
		person = false ;
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	protected void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	protected void setName3(String name3) {
		this.name3 = name3;
	}

	public String getDepartment() {
		return department;
	}

	protected void setDepartment(String department) {
		this.department = department;
	}

	
	public String getHvName() {
		return person ? getWebPerson().getLastName() : getName() ;
	}
	
	public WebPerson getWebPerson() {
		return webPerson;
	}

	public void setWebPerson(WebPerson webPerson) {
		this.webPerson = webPerson;
	}
}

