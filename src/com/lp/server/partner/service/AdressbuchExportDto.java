package com.lp.server.partner.service;

import java.io.Serializable;

import com.lp.server.personal.service.ZeitdatenDto;

public class AdressbuchExportDto implements Serializable {
	private String exportId;
	private String city;
	private String displayName;
	private String firstName;
	private String lastName;

	private String notes;
	private String postalCode;
	private String stressAdress;
	private String phone;
	private String email;
	private String country;
	private String company;
	private String mobilePhone;
	private String title;
	private boolean hasFoto = false;
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private Integer ANSPRECHPARTNER_I_ID;
	
	public Integer getANSPRECHPARTNER_I_ID() {
		return ANSPRECHPARTNER_I_ID;
	}

	public void setANSPRECHPARTNER_I_ID(Integer aNSPRECHPARTNER_I_ID) {
		ANSPRECHPARTNER_I_ID = aNSPRECHPARTNER_I_ID;
	}

	private Integer PARTNER_I_ID;

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStressAdress() {
		return stressAdress;
	}

	public void setStressAdress(String stressAdress) {
		this.stressAdress = stressAdress;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getPARTNER_I_ID() {
		return PARTNER_I_ID;
	}

	public void setPARTNER_I_ID(Integer pARTNER_I_ID) {
		PARTNER_I_ID = pARTNER_I_ID;
	}

	public boolean getHasFoto() {
		return this.hasFoto;
	}
	
	public void setHasFoto(boolean hasFoto) {
		this.hasFoto = hasFoto;
	}
	
	public String toString() {
		String returnString = "PARTNER_I_ID:";
		returnString += PARTNER_I_ID;
		returnString += ",ANSPRECHPARTNER_I_ID: " +ANSPRECHPARTNER_I_ID;
		returnString += ",company: " + company;
		returnString += ",title: " + title;
		returnString += ",displayName: " + displayName;
		returnString += ",firstName: " + firstName;
		returnString += ",lastName: " + lastName;
		returnString += ",stressAdress: " + stressAdress;
		returnString += ",country: " + country;
		returnString += ",postalCode: " + postalCode;
		returnString += ",city: " + city;
		returnString += ",phone: " + phone;
		returnString += ",mobilePhone: " + mobilePhone;
		returnString += ",email: " + email;
		returnString += ",hasFoto: " + hasFoto;

		return returnString;
	}

	public String toCsvString() {
		//mehrfach EMail ; -> |
		String mail = email;
		if (mail != null) {
			mail = mail.replace(";", "|");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(PARTNER_I_ID);
		sb.append(";");
		sb.append(ANSPRECHPARTNER_I_ID);
		sb.append(";");
		sb.append(exportId);
		sb.append(";");
		sb.append(company);
		sb.append(";");
		sb.append(title);
		sb.append(";");
		sb.append(displayName);
		sb.append(";");
		sb.append(firstName);
		sb.append(";");
		sb.append(lastName);
		sb.append(";");
		sb.append(replaceIllegal(stressAdress));
		sb.append(";");
		sb.append(replaceIllegal(country));
		sb.append(";");
		sb.append(replaceIllegal(postalCode));
		sb.append(";");
		sb.append(replaceIllegal(city));
		sb.append(";");
		sb.append(phone);
		sb.append(";");
		sb.append(mobilePhone);
		sb.append(";");
		sb.append(mail);
		sb.append(";");
		sb.append(replaceIllegal(notes));
		sb.append(";");
		sb.append(hasFoto ? "1" : "0");
		
		return sb.toString();
	}
	
	private String replaceIllegal(String in) {
		if (in==null)
			return in;
		else
			return in.replace(";", "|").replace("\n", "\\n");
	}
}
