package com.lp.service.edifact.schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.lp.server.util.collection.CollectionTools;
import com.lp.util.Helper;

public class NadInfo {
	private String partyFunctionCode;
	private String partyIdentifier;
	private String identification;
	private String responsibleAgency;
	private String names[];
	private String partyNames[];
	private String partyNameFormatCode;
	private String streets[];
	private String cityName;
	private String countrySubEntityNameCode;
	private String countryIndentificationCode;
	private String countryResponsibleAgencyCode;
	private String countrySubEntityName;
	private String postalIdentifcationCode;
	private String countryNameCode;

	
	public NadInfo() {
		names = new String[5];
		partyNames = new String[5];
		streets = new String[4];
	}
	
	public String getPartyFunctionCode() {
		return partyFunctionCode;
	}
	public void setPartyFunctionCode(String partyFunctionCode) {
		this.partyFunctionCode = partyFunctionCode;
	}
	public String getPartyIdentifier() {
		return partyIdentifier;
	}
	public void setPartyIdentifier(String partyIdentifier) {
		this.partyIdentifier = partyIdentifier;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getResponsibleAgency() {
		return responsibleAgency;
	}
	public void setResponsibleAgency(String responsibleAgency) {
		this.responsibleAgency = responsibleAgency;
	}
	public String getName(int index) {
		if(index < 0 || index >= names.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + names.length);
		}
		return names[index];
	}

	public Collection<String> getNames() {
		return CollectionTools.select(names, name -> {
			return !Helper.isStringEmpty(name);
		});
	}
	
	public void setName(int index, String name) {
		if(index < 0 || index >= names.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + names.length);
		}
		this.names[index] = name;
	}	
	public String getPartyName(int index) {
		if(index < 0 || index >= partyNames.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + partyNames.length);
		}
		return partyNames[index];
	}
	public Collection<String> getPartyNames() {
		return CollectionTools.select(partyNames, name -> {
			return !Helper.isStringEmpty(name);
		});
	}
	public void setPartyName(int index, String name) {
		if(index < 0 || index >= partyNames.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + partyNames.length);
		}
		this.partyNames[index] = name;
	}

	public String getPartyNameFormatCode() {
		return partyNameFormatCode;
	}

	public void setPartyNameFormatCode(String partyNameFormatCode) {
		this.partyNameFormatCode = partyNameFormatCode;
	}	
	public String getStreet(int index) {
		if(index < 0 || index >= streets.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + streets.length);
		}
		return streets[index];
	}

	public Collection<String> getStreets() {
		return CollectionTools.select(streets, street -> {
			return !Helper.isStringEmpty(street);
		});
	}
	
	public void setStreet(int index, String name) {
		if(index < 0 || index >= streets.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is > " + streets.length);
		}
		this.streets[index] = name;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryNameCode() {
		return countryNameCode;
	}

	public void setCountryNameCode(String countryNameCode) {
		this.countryNameCode = countryNameCode;
	}

	public String getCountryIndentificationCode() {
		return countryIndentificationCode;
	}

	public void setCountryIndentificationCode(String countryIndentificationCode) {
		this.countryIndentificationCode = countryIndentificationCode;
	}

	public String getCountryResponsibleAgencyCode() {
		return countryResponsibleAgencyCode;
	}

	public void setCountryResponsibleAgencyCode(
			String countryResponsibleAgencyCode) {
		this.countryResponsibleAgencyCode = countryResponsibleAgencyCode;
	}

	public String getCountrySubEntityName() {
		return countrySubEntityName;
	}

	public void setCountrySubEntityName(String countrySubEntityName) {
		this.countrySubEntityName = countrySubEntityName;
	}

	public String getCountrySubEntityNameCode() {
		return countrySubEntityNameCode;
	}

	public void setCountrySubEntityNameCode(String countrySubEntityNameCode) {
		this.countrySubEntityNameCode = countrySubEntityNameCode;
	}

	public String getPostalIdentifcationCode() {
		return postalIdentifcationCode;
	}

	public void setPostalIdentifcationCode(String postalIdentifcationCode) {
		this.postalIdentifcationCode = postalIdentifcationCode;
	}	
}
