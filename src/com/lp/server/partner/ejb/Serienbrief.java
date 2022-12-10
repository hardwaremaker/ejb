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
package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PART_SERIENBRIEF")
public class Serienbrief implements Serializable {
	public Serienbrief() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_GEHTANKUNDEN")
	private Short bGehtankunden;

	@Column(name = "B_GEHTANINTERESSENTEN")
	private Short bGehtaninteressenten;

	@Column(name = "B_VERSTECKTEDABEI")
	private Short bVerstecktedabei;

	@Column(name = "B_ANSPRECHPARTNERFUNKTIONAUCHOHNE")
	private Short bAnsprechpartnerfunktionauchohne;

	@Column(name = "C_PLZ")
	private String cPlz;

	@Column(name = "C_BETREFF")
	private String cBetreff;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "B_GEHTANLIEFERANTEN")
	private Short bGehtanlieferanten;

	@Column(name = "B_GEHTANMOEGLICHELIEFERANTEN")
	private Short bGehtanmoeglichelieferanten;

	@Column(name = "B_GEHTANPARTNER")
	private Short bGehtanpartner;

	@Column(name = "B_MITZUGEORDNETENFIRMEN")
	private Short bMitzugeordnetenfirmen;

	@Column(name = "X_MAILTEXT")
	private String xMailtext;

	@Column(name = "B_HTML")
	private Short bHtml ;

	@Column(name = "LOCALE_C_NR")
	private String localeCNr;
	
	
	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public String getXMailtext() {
		return this.xMailtext;
	}

	public void setXMailtext(String xMailtext) {
		this.xMailtext = xMailtext;
	}

	@Column(name = "B_WENNKEINANSPMITFKT_DANNERSTERANSP")
	private Short bWennkeinanspmitfktDannersteransp;

	@Column(name = "B_SELEKTIONEN_LOGISCHES_ODER")
	private Short bSelektionenLogischesOder;

	public Short getBWennkeinanspmitfktDannersteransp() {
		return bWennkeinanspmitfktDannersteransp;
	}

	public void setBWennkeinanspmitfktDannersteransp(
			Short bWennkeinanspmitfktDannersteransp) {
		this.bWennkeinanspmitfktDannersteransp = bWennkeinanspmitfktDannersteransp;
	}

	public Short getBSelektionenLogischesOder() {
		return bSelektionenLogischesOder;
	}

	public void setBSelektionenLogischesOder(Short bSelektionenLogischesOder) {
		this.bSelektionenLogischesOder = bSelektionenLogischesOder;
	}

	public Short getBMitzugeordnetenfirmen() {
		return bMitzugeordnetenfirmen;
	}

	public void setBMitzugeordnetenfirmen(Short bMitzugeordnetenfirmen) {
		this.bMitzugeordnetenfirmen = bMitzugeordnetenfirmen;
	}

	@Column(name = "LAND_I_ID")
	private Integer landIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "ANSPRECHPARTNERFUNKTION_I_ID")
	private Integer ansprechpartnerfunktionIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PARTNERKLASSE_I_ID")
	private Integer partnerklasseIId;

	@Column(name = "BRANCHE_I_ID")
	private Integer brancheIId;

	@Column(name = "B_NEWSLETTER")
	private Short bNewsletter;

	public Short getbNewsletter() {
		return bNewsletter;
	}

	public void setbNewsletter(Short bNewsletter) {
		this.bNewsletter = bNewsletter;
	}

	public Integer getPartnerklasseIId() {
		return partnerklasseIId;
	}

	public void setPartnerklasseIId(Integer partnerklasseIId) {
		this.partnerklasseIId = partnerklasseIId;
	}

	public Integer getBrancheIId() {
		return brancheIId;
	}

	public void setBrancheIId(Integer brancheIId) {
		this.brancheIId = brancheIId;
	}

	@Column(name = "N_ABUMSATZ")
	private BigDecimal nAbumsatz;

	public BigDecimal getNAbumsatz() {
		return nAbumsatz;
	}

	public void setNAbumsatz(BigDecimal abumsatz) {
		nAbumsatz = abumsatz;
	}

	public BigDecimal getNBisumsatz() {
		return nBisumsatz;
	}

	public void setNBisumsatz(BigDecimal bisumsatz) {
		nBisumsatz = bisumsatz;
	}

	public Timestamp getTUmsatzab() {
		return tUmsatzab;
	}

	public void setTUmsatzab(Timestamp umsatzab) {
		tUmsatzab = umsatzab;
	}

	public Timestamp getTUmsatzbis() {
		return tUmsatzbis;
	}

	public void setTUmsatzbis(Timestamp umsatzbis) {
		tUmsatzbis = umsatzbis;
	}

	@Column(name = "N_BISUMSATZ")
	private BigDecimal nBisumsatz;

	@Column(name = "T_UMSATZAB")
	private Timestamp tUmsatzab;
	@Column(name = "T_UMSATZBIS")
	private Timestamp tUmsatzbis;

	private static final long serialVersionUID = 1L;

	public Serienbrief(Integer iId, String cBez, String mandantCNr,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Short bGehtAnKunden, Short bGehtAnInteressenten,
			Short bVersteckteDabei, Short bAnsprechpartnerfunktionAuchOhne,
			Short bGehtanlieferanten, Short bGehtanmoeglichelieferanten,
			Short bGehtanpartner, Short bMitzugeordnetenfirmen,
			Short bNewsletterShort, Short bSelektionenLogischesOder,
			Short bWennkeinanspmitfktDannersteransp, Short bHtml, String localeCNr) {

		setIId(iId);
		setCBez(cBez);
		setMandantCNr(mandantCNr);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAendern(personalIIdAendern);
		setBGehtankunden(bGehtAnKunden);
		setBGehtaninteressenten(bGehtAnInteressenten);
		setBVerstecktedabei(bVersteckteDabei);
		setBAnsprechpartnerfunktionauchohne(bAnsprechpartnerfunktionAuchOhne);
		setBGehtanlieferanten(bGehtanlieferanten);
		setBGehtanmoeglichelieferanten(bGehtanmoeglichelieferanten);
		setBGehtanpartner(bGehtanpartner);
		setBMitzugeordnetenfirmen(bMitzugeordnetenfirmen);
		setbNewsletter(bNewsletter);
		setBWennkeinanspmitfktDannersteransp(bWennkeinanspmitfktDannersteransp);
		setBSelektionenLogischesOder(bSelektionenLogischesOder);
		setBHtml(bHtml) ;
		setLocaleCNr(localeCNr);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Short getBGehtankunden() {
		return this.bGehtankunden;
	}

	public void setBGehtankunden(Short bGehtankunden) {
		this.bGehtankunden = bGehtankunden;
	}

	public Short getBGehtaninteressenten() {
		return this.bGehtaninteressenten;
	}

	public void setBGehtaninteressenten(Short bGehtaninteressenten) {
		this.bGehtaninteressenten = bGehtaninteressenten;
	}

	public Short getBVerstecktedabei() {
		return this.bVerstecktedabei;
	}

	public void setBVerstecktedabei(Short bVerstecktedabei) {
		this.bVerstecktedabei = bVerstecktedabei;
	}

	public Short getBAnsprechpartnerfunktionauchohne() {
		return this.bAnsprechpartnerfunktionauchohne;
	}

	public void setBAnsprechpartnerfunktionauchohne(
			Short bAnsprechpartnerfunktionauchohne) {
		this.bAnsprechpartnerfunktionauchohne = bAnsprechpartnerfunktionauchohne;
	}

	public String getCPlz() {
		return this.cPlz;
	}

	public void setCPlz(String cPlz) {
		this.cPlz = cPlz;
	}

	public String getCBetreff() {
		return this.cBetreff;
	}

	public void setCBetreff(String cBetreff) {
		this.cBetreff = cBetreff;
	}

	public String getXText() {
		return this.xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Short getBGehtanlieferanten() {
		return this.bGehtanlieferanten;
	}

	public void setBGehtanlieferanten(Short bGehtanlieferanten) {
		this.bGehtanlieferanten = bGehtanlieferanten;
	}

	public Short getBGehtanmoeglichelieferanten() {
		return this.bGehtanmoeglichelieferanten;
	}

	public void setBGehtanmoeglichelieferanten(Short bGehtanmoeglichelieferanten) {
		this.bGehtanmoeglichelieferanten = bGehtanmoeglichelieferanten;
	}

	public Short getBGehtanpartner() {
		return this.bGehtanpartner;
	}

	public void setBGehtanpartner(Short bGehtanpartner) {
		this.bGehtanpartner = bGehtanpartner;
	}

	public Integer getLandIId() {
		return this.landIId;
	}

	public void setLandIId(Integer landIId) {
		this.landIId = landIId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getAnsprechpartnerfunktionIId() {
		return this.ansprechpartnerfunktionIId;
	}

	public void setAnsprechpartnerfunktionIId(Integer ansprechpartnerfunktionIId) {
		this.ansprechpartnerfunktionIId = ansprechpartnerfunktionIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Short getBHtml() {
		return bHtml;
	}

	public void setBHtml(Short bHtml) {
		this.bHtml = bHtml;
	}
}
