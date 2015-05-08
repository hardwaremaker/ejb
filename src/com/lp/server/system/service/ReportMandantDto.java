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
package com.lp.server.system.service;

import java.io.Serializable;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;

public class ReportMandantDto implements Serializable{

	private static final long serialVersionUID = 4222380484150741478L;
	
	public String getAktuellenBenutzer(){
		String sAktuellerBenutzer="";
		if(personalDto_AktuellerBenutzer!=null && personalDto_AktuellerBenutzer.getPartnerDto()!=null){
			sAktuellerBenutzer = personalDto_AktuellerBenutzer.formatAnrede();
		}
		return sAktuellerBenutzer;
	}
	
	private TheClientDto clientDto;
	private MandantDto mandantDto;
	private PersonalDto personalDto_AktuellerBenutzer;

	public final static String EMPTY_FIELD_VALUE = "";

	protected ReportMandantDto() {
		// TODO Auto-generated constructor stub
	}
	
	public ReportMandantDto(TheClientDto theClientDto, MandantDto mandantDto, PersonalDto personalDto_AktuellerBenutzer) {
		if (null == theClientDto)
			throw new IllegalArgumentException("theClientDto == null");
		if (null == mandantDto)
			throw new IllegalArgumentException("mandantDto == null");

		this.clientDto = theClientDto;
		this.mandantDto = mandantDto;
		this.personalDto_AktuellerBenutzer=personalDto_AktuellerBenutzer;
	}

	public TheClientDto getTheClientDto() {
		return clientDto;
	}

	public MandantDto getMandantDto() {
		return mandantDto;
	}

	public PartnerDto getPartnerDto() {
		return mandantDto.getPartnerDto();
	}

	public String getMandantCNr() {
		return getReportString(clientDto.getMandant());
	}
	
	public String getKurzbezeichnung() {
		return getReportString(getMandantDto().getCKbez());
	}

	private String getReportString(String value) {
		return value == null ? EMPTY_FIELD_VALUE : value;
	}

	public String getStrasse() {
		PartnerDto partnerDto = getPartnerDto();
		if (partnerDto == null)
			return EMPTY_FIELD_VALUE;

		return getReportString(partnerDto.getCStrasse());
	}

	public String getOrt() {
		PartnerDto partnerDto = getPartnerDto();
		if (partnerDto == null)
			return "";

		LandplzortDto lpzoDto = partnerDto.getLandplzortDto();
		if (lpzoDto == null)
			return "";

		OrtDto ortDto = lpzoDto.getOrtDto();
		if (ortDto == null)
			return "";

		return getReportString(ortDto.getCName());
	}

	public String getLkz() {
		return isLandDefined() ? getReportString(getPartnerDto()
				.getLandplzortDto().getLandDto().getCLkz()) : EMPTY_FIELD_VALUE;
	}

	private boolean isPartnerDefined() {
		return getPartnerDto() != null;
	}

	private boolean isLandplzortDefined() {
		return isPartnerDefined() && getPartnerDto().getLandplzortDto() != null;
	}

	private boolean isLandDefined() {
		return isLandplzortDefined()
				&& getPartnerDto().getLandplzortDto().getLandDto() != null;
	}

	public String getName1() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCName1nachnamefirmazeile1()) : EMPTY_FIELD_VALUE;
	}

	public String getName2() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCName2vornamefirmazeile2()) : EMPTY_FIELD_VALUE;
	}

	public String getName3() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCName3vorname2abteilung()) : EMPTY_FIELD_VALUE;
	}

	public String getPLZ() {
		return isLandplzortDefined() ? getReportString(getPartnerDto()
				.getLandplzortDto().getCPlz()) : EMPTY_FIELD_VALUE;
	}

	public String getTelefon() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCTelefon()) : EMPTY_FIELD_VALUE;
	}

	public String getFax() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCFax()) : EMPTY_FIELD_VALUE;
	}

	public String getEmail() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCEmail()) : EMPTY_FIELD_VALUE;
	}

	public String getHomepage() {
		return isPartnerDefined() ? getReportString(getPartnerDto()
				.getCHomepage()) : EMPTY_FIELD_VALUE;
	}
}
