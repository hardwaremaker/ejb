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

import java.io.Serializable;
import java.math.BigDecimal;

public class SerienbriefEmpfaengerDto implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PartnerDto partnerDto;
	private String cFirma1_Partner;
	private String cStrasse_Partner;
	private String cFirma2_Partner;
	private String cTelefon_Partner;
	private String cFax_Partner;
	private String cBriefanrede_Partner;
	private String cEmail_Partner;
	private String cFax_Ansprechpartner;
	private String cAnrede_Ansprechpartner;
	public String getCAnrede_Ansprechpartner() {
		return cAnrede_Ansprechpartner;
	}

	public void setCAnrede_Ansprechpartner(String cAnrede_Ansprechpartner) {
		this.cAnrede_Ansprechpartner = cAnrede_Ansprechpartner;
	}

	private String cAbteilung_Ansprechpartner;
	
	public String getCAbteilung_Ansprechpartner() {
		return cAbteilung_Ansprechpartner;
	}

	public void setCAbteilung_Ansprechpartner(String cAbteilung_Ansprechpartner) {
		this.cAbteilung_Ansprechpartner = cAbteilung_Ansprechpartner;
	}

	private String cNTitel_Ansprechpartner;
	public String getCNTitel_Ansprechpartner() {
		return cNTitel_Ansprechpartner;
	}

	public void setCNTitel_Ansprechpartner(String cNTitel_Ansprechpartner) {
		this.cNTitel_Ansprechpartner = cNTitel_Ansprechpartner;
	}

	private String cTitel_Ansprechpartner;
	private String cVorname_Ansprechpartner;
	private String cNachname_Ansprechpartner;
	private AnsprechpartnerDto ansprechpartnerDto;
	private String cLand_Partner;
	private String cPlz_Partner;
	private String cOrt_Partner;
	private String cTelefonDW_Ansprechpartner;
	private String cHandy_Ansprechpartner;
	private String cFaxDW_Ansprechpartner;
	private String cEmail_Ansprechpartner;
	private boolean bBekommtFax;
	private boolean bBekommtEmail;
	private boolean bBekommtBrief;
	private String cVersandEmailadresse;
	private String cVersandFaxnummer;
	private String cVersandBriefanrede;
	
	private Integer kundeIId;
	private Integer lieferantIId;
	private boolean istInteressent;
	private BigDecimal umsatz;
	private Integer partnerIId;
	private String cBranche;
	private String cPartnerklasse;

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public String getCFirma1_Partner() {
		return cFirma1_Partner;
	}

	public SerienbriefEmpfaengerDto clone() {
		SerienbriefEmpfaengerDto serienbriefEmpfaengerDto = new SerienbriefEmpfaengerDto();

		serienbriefEmpfaengerDto.partnerDto = this.partnerDto;
		serienbriefEmpfaengerDto.cFirma1_Partner = this.cFirma1_Partner;
		serienbriefEmpfaengerDto.cStrasse_Partner = this.cStrasse_Partner;
		serienbriefEmpfaengerDto.cFirma2_Partner = this.cFirma2_Partner;
		serienbriefEmpfaengerDto.cTelefon_Partner = this.cTelefon_Partner;
		serienbriefEmpfaengerDto.cFax_Partner = this.cFax_Partner;
		serienbriefEmpfaengerDto.cBriefanrede_Partner = this.cBriefanrede_Partner;
		serienbriefEmpfaengerDto.cEmail_Partner = this.cEmail_Partner;
		serienbriefEmpfaengerDto.cFax_Ansprechpartner = this.cFax_Ansprechpartner;
		serienbriefEmpfaengerDto.cTitel_Ansprechpartner = this.cTitel_Ansprechpartner;
		serienbriefEmpfaengerDto.cVorname_Ansprechpartner = this.cVorname_Ansprechpartner;
		serienbriefEmpfaengerDto.cNachname_Ansprechpartner = this.cNachname_Ansprechpartner;
		serienbriefEmpfaengerDto.ansprechpartnerDto = this.ansprechpartnerDto;
		serienbriefEmpfaengerDto.cLand_Partner = this.cLand_Partner;
		serienbriefEmpfaengerDto.cPlz_Partner = this.cPlz_Partner;
		serienbriefEmpfaengerDto.cOrt_Partner = this.cOrt_Partner;
		serienbriefEmpfaengerDto.cTelefonDW_Ansprechpartner = this.cTelefonDW_Ansprechpartner;
		serienbriefEmpfaengerDto.cHandy_Ansprechpartner = this.cHandy_Ansprechpartner;
		serienbriefEmpfaengerDto.cFaxDW_Ansprechpartner = this.cFaxDW_Ansprechpartner;
		serienbriefEmpfaengerDto.cEmail_Ansprechpartner = this.cEmail_Ansprechpartner;
		serienbriefEmpfaengerDto.bBekommtFax = this.bBekommtFax;
		serienbriefEmpfaengerDto.bBekommtEmail = this.bBekommtEmail;
		serienbriefEmpfaengerDto.bBekommtBrief = this.bBekommtBrief;
		serienbriefEmpfaengerDto.cVersandEmailadresse = this.cVersandEmailadresse;
		serienbriefEmpfaengerDto.cVersandFaxnummer = this.cVersandFaxnummer;
		serienbriefEmpfaengerDto.cVersandBriefanrede = this.cVersandBriefanrede;
		serienbriefEmpfaengerDto.istInteressent = this.istInteressent;
		serienbriefEmpfaengerDto.kundeIId = this.kundeIId;
		serienbriefEmpfaengerDto.lieferantIId = this.lieferantIId;
		serienbriefEmpfaengerDto.umsatz = this.umsatz;
		serienbriefEmpfaengerDto.partnerIId = this.partnerIId;
		serienbriefEmpfaengerDto.cBranche = this.cBranche;
		serienbriefEmpfaengerDto.cPartnerklasse = this.cPartnerklasse;

		return serienbriefEmpfaengerDto;
	}

	public String getCStrasse_Partner() {
		return cStrasse_Partner;
	}

	public String getCFirma2_Partner() {
		return cFirma2_Partner;
	}

	public String getCTelefon_Partner() {
		return cTelefon_Partner;
	}

	public String getCFax_Partner() {
		return cFax_Partner;
	}

	public String getCBriefanrede_Partner() {
		return cBriefanrede_Partner;
	}

	public String getCEmail_Partner() {
		return cEmail_Partner;
	}

	public String getCFax_Ansprechpartner() {
		return cFax_Ansprechpartner;
	}

	public String getCTitel_Ansprechpartner() {
		return cTitel_Ansprechpartner;
	}

	public String getCVorname_Ansprechpartner() {
		return cVorname_Ansprechpartner;
	}

	public String getCNachname_Ansprechpartner() {
		return cNachname_Ansprechpartner;
	}

	public AnsprechpartnerDto getAnsprechpartnerDto() {
		return ansprechpartnerDto;
	}


	public String getCTelefonDW_Ansprechpartner() {
		return cTelefonDW_Ansprechpartner;
	}

	public String getCHandy_Ansprechpartner() {
		return cHandy_Ansprechpartner;
	}

	public String getCFaxDW_Ansprechpartner() {
		return cFaxDW_Ansprechpartner;
	}

	public String getCEmail_Ansprechpartner() {
		return cEmail_Ansprechpartner;
	}

	public boolean isBBekommtFax() {
		return bBekommtFax;
	}

	public boolean isBBekommtEmail() {
		return bBekommtEmail;
	}

	public boolean isBBekommtBrief() {
		return bBekommtBrief;
	}

	public String getCVersandEmailadresse() {
		return cVersandEmailadresse;
	}

	public String getCVersandFaxnummer() {
		return cVersandFaxnummer;
	}

	public String getCVersandBriefanrede() {
		return cVersandBriefanrede;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public void setCFirma1_Partner(String cFirma1_Partner) {
		this.cFirma1_Partner = cFirma1_Partner;
	}

	public void setCStrasse_Partner(String cStrasse_Partner) {
		this.cStrasse_Partner = cStrasse_Partner;
	}

	public void setCFirma2_Partner(String cFirma2_Partner) {
		this.cFirma2_Partner = cFirma2_Partner;
	}

	public void setCTelefon_Partner(String cTelefon_Partner) {
		this.cTelefon_Partner = cTelefon_Partner;
	}

	public void setCFax_Partner(String cFax_Partner) {
		this.cFax_Partner = cFax_Partner;
	}

	public void setCBriefanrede_Partner(String cBriefanrede_Partner) {
		this.cBriefanrede_Partner = cBriefanrede_Partner;
	}

	public void setCEmail_Partner(String cEmail_Partner) {
		this.cEmail_Partner = cEmail_Partner;
	}

	public void setCFax_Ansprechpartner(String cFax_Ansprechpartner) {
		this.cFax_Ansprechpartner = cFax_Ansprechpartner;
	}

	public void setCEmail_Ansprechpartner(String cEmail_Ansprechpartner) {
		this.cEmail_Ansprechpartner = cEmail_Ansprechpartner;
	}

	public void setCTitel_Ansprechpartner(String cTitel_Ansprechpartner) {
		this.cTitel_Ansprechpartner = cTitel_Ansprechpartner;
	}

	public void setCVorname_Ansprechpartner(String cVorname_Ansprechpartner) {
		this.cVorname_Ansprechpartner = cVorname_Ansprechpartner;
	}

	public void setCNachname_Ansprechpartner(String cNachname_Ansprechpartner) {
		this.cNachname_Ansprechpartner = cNachname_Ansprechpartner;
	}

	public void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDto) {
		this.ansprechpartnerDto = ansprechpartnerDto;
	}

	public String getCLand_Partner() {
		return cLand_Partner;
	}

	public void setCLand_Partner(String land_Partner) {
		cLand_Partner = land_Partner;
	}

	public String getCPlz_Partner() {
		return cPlz_Partner;
	}

	public void setCPlz_Partner(String plz_Partner) {
		cPlz_Partner = plz_Partner;
	}

	public String getCOrt_Partner() {
		return cOrt_Partner;
	}

	public void setCOrt_Partner(String ort_Partner) {
		cOrt_Partner = ort_Partner;
	}

	public void setCTelefonDW_Ansprechpartner(String cTelefonDW_Ansprechpartner) {
		this.cTelefonDW_Ansprechpartner = cTelefonDW_Ansprechpartner;
	}

	public void setCHandy_Ansprechpartner(String cHandy_Ansprechpartner) {
		this.cHandy_Ansprechpartner = cHandy_Ansprechpartner;
	}

	public void setCFaxDW_Ansprechpartner(String cFaxDW_Ansprechpartner) {
		this.cFaxDW_Ansprechpartner = cFaxDW_Ansprechpartner;
	}

	public void setBBekommtFax(boolean bBekommtFax) {
		this.bBekommtFax = bBekommtFax;
	}

	public void setBBekommtEmail(boolean bBekommtEmail) {
		this.bBekommtEmail = bBekommtEmail;
	}

	public void setBBekommtBrief(boolean bBekommtBrief) {
		this.bBekommtBrief = bBekommtBrief;
	}

	public void setCVersandEmailadresse(String cVersandEmailadresse) {
		this.cVersandEmailadresse = cVersandEmailadresse;
	}

	public void setCVersandFaxnummer(String cVersandFaxnummer) {
		this.cVersandFaxnummer = cVersandFaxnummer;
	}

	public void setCVersandBriefanrede(String cVersandBriefanrede) {
		this.cVersandBriefanrede = cVersandBriefanrede;
	}

	public void setIstInteressent(boolean istInteressent) {
		this.istInteressent = istInteressent;
	}
	
	public boolean isInteressent() {
		return istInteressent;
	}
	
	public Integer getKundeIId() {
		return kundeIId;
	}
	
	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}
	
	public Integer getLieferantIId() {
		return lieferantIId;
	}
	
	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}
	
	public BigDecimal getUmsatz() {
		return umsatz;
	}
	
	public void setUmsatz(BigDecimal umsatz) {
		this.umsatz = umsatz;
	}
	
	public boolean isKunde() {
		return kundeIId != null;
	}
	
	public boolean isLieferant() {
		return lieferantIId != null;
	}
	
	public boolean isAnsprechpartner() {
		return getAnsprechpartnerDto() != null;
	}
	
	public String getCBranche() {
		return cBranche;
	}
	public void setCBranche(String cBranche) {
		this.cBranche = cBranche;
	}
	
	public String getCPartnerklasse() {
		return cPartnerklasse;
	}
	
	public void setCPartnerklasse(String cPartnerklasse) {
		this.cPartnerklasse = cPartnerklasse;
	}
}
