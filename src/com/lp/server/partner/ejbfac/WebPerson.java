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
package com.lp.server.partner.ejbfac;

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.schema.opentrans_2_1.OT2CONTACTDETAILS;

public class WebPerson {
	private boolean person ;
	private String contactId ;
	private String anrede ;
	private String firstName ;
	private String lastName ;
	private String academicTitle ;
	private String fax ;
	private String phone ;
	private String email ;
	private String description ;

	public WebPerson(OT2CONTACTDETAILS contactDetail) {
		processContactDetails(contactDetail);
	}

	private void processContactDetails(OT2CONTACTDETAILS contactDetail) {
		if(contactDetail.getCONTACTNAME() != null && contactDetail.getCONTACTNAME().size() > 0) {
			lastName = contactDetail.getCONTACTNAME().get(0).getValue() ;
		}
		if(contactDetail.getFIRSTNAME() != null && contactDetail.getFIRSTNAME().size() > 0) {
			firstName = contactDetail.getFIRSTNAME().get(0).getValue() ;
		}
		
		if(contactDetail.getACADEMICTITLE() != null && contactDetail.getACADEMICTITLE().size() > 0) {
			academicTitle = contactDetail.getACADEMICTITLE().get(0).getValue() ;
		}

//		anrede = PartnerFac.PARTNER_ANREDE_HERR ;
		anrede = null ;
		if(contactDetail.getTITLE() != null && contactDetail.getTITLE().size() > 0) {
			String title = contactDetail.getTITLE().get(0).getValue() ;
			if(PartnerFac.PARTNER_ANREDE_HERR.trim().equals(title) || "male".equals(title)) {
				anrede = PartnerFac.PARTNER_ANREDE_HERR ;
			}
			if(PartnerFac.PARTNER_ANREDE_FRAU.trim().equals(title) || "female".equals(title)) {
				anrede = PartnerFac.PARTNER_ANREDE_FRAU ;							
			}
		}
		
		if(contactDetail.getCONTACTDESCR() != null && contactDetail.getCONTACTDESCR().size() > 0) {
			description = contactDetail.getCONTACTDESCR().get(0).getValue() ;
		}
		
		if(contactDetail.getPHONE() != null && contactDetail.getPHONE().size() > 0) {
			phone = contactDetail.getPHONE().get(0).getValue() ;
		}
		
		if(contactDetail.getFAX() != null && contactDetail.getFAX().size() > 0) {
			fax = contactDetail.getFAX().get(0).getValue() ;
		}

		if(contactDetail.getEMAILS() != null && contactDetail.getEMAILS().getEMAILAndPUBLICKEY().size() > 0) {
			email = (String) contactDetail.getEMAILS().getEMAILAndPUBLICKEY().get(0) ;
		}
		
		contactId = contactDetail.getCONTACTID() ;
		person = true ;
	}

	
	public boolean isPerson() {
		return person;
	}

	public void setPerson(boolean person) {
		this.person = person;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getAnrede() {
		return anrede;
	}

	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAcademicTitle() {
		return academicTitle;
	}

	public void setAcademicTitle(String academicTitle) {
		this.academicTitle = academicTitle;
	}
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
