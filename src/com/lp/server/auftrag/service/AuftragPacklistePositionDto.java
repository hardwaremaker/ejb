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
package com.lp.server.auftrag.service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.LPDatenSubreport;

public class AuftragPacklistePositionDto implements Serializable {
	private static final long serialVersionUID = -503814038537104642L;

	
	private boolean bGesehen=false;
	
	public boolean isbGesehen() {
		return bGesehen;
	}

	public void setBGesehen(boolean bGesehen) {
		this.bGesehen = bGesehen;
	}

	private String ident;
	private String bezeichnung;
	private String kurzbezeichnung;
	private String referenznummer;
	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}

	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public String getReferenznummer() {
		return referenznummer;
	}

	public void setReferenznummer(String referenznummer) {
		this.referenznummer = referenznummer;
	}

	private BigDecimal gesamtMenge;
	private BigDecimal offeneMenge;
	private BigDecimal lagerstandAbbuchungslager;
	private BigDecimal lagerstandAllerLaeger;
	public BigDecimal getLagerstandAllerLaeger() {
		return lagerstandAllerLaeger;
	}

	public void setLagerstandAllerLaeger(BigDecimal lagerstandAllerLaeger) {
		this.lagerstandAllerLaeger = lagerstandAllerLaeger;
	}

	private BigDecimal fiktiverLagerstand;
	private String lagerplatz;
	private Image kommentarImage;
	private String artikelkommentar;
	private String freierText;
	private String vollstaendigkeitKomponentenIdent;
	private String vollstaendigkeitKomponentenBez;
	private String serienChargenNr;
	private LPDatenSubreport arbeitsgaenge;
	private Integer vorzeichen;
	private String positionsTerminString;
	private Timestamp positionsTerminTimestamp;
	private BigDecimal gewicht;
	private Double rasterLiegend;
	private Double rasterStehend;
	private Double materialgewicht;
	private Double hoehe;
	private Double breite;
	private Double tiefe;
	private String bauform;
	private String verpackungsart;
	private String farbcode;
	private String material;
	private String artikelklasse;
	private String artikelgruppe;
	private String verkaufsEAN;
	private String verpackungsEAN;
	private Double verpackungsmenge;
	private String positionsStatus;
	
	
	private String bezeichnungEinzeln;
	private String zusatzbezeichnungEinzeln;
	
	private Integer positionsnummer;
	private Integer auftragspositionIId;
	

	public Integer getPositionsnummer() {
		return positionsnummer;
	}

	public void setPositionsnummer(Integer positionsnummer) {
		this.positionsnummer = positionsnummer;
	}

	public Integer getAuftragspositionIId() {
		return auftragspositionIId;
	}

	public void setAuftragspositionIId(Integer auftragspositionIId) {
		this.auftragspositionIId = auftragspositionIId;
	}

	private String setArtikelTyp;
	
	public String getSetArtikelTyp() {
		return setArtikelTyp;
	}

	public void setSetArtikelTyp(String setArtikelTyp) {
		this.setArtikelTyp = setArtikelTyp;
	}

	public String getBezeichnungEinzeln() {
		return bezeichnungEinzeln;
	}

	public void setBezeichnungEinzeln(String bezeichnungEinzeln) {
		this.bezeichnungEinzeln = bezeichnungEinzeln;
	}

	public String getZusatzbezeichnungEinzeln() {
		return zusatzbezeichnungEinzeln;
	}

	public void setZusatzbezeichnungEinzeln(String zusatzbezeichnungEinzeln) {
		this.zusatzbezeichnungEinzeln = zusatzbezeichnungEinzeln;
	}

	public String getPositionsStatus() {
		return positionsStatus;
	}

	public void setPositionsStatus(String positionsStatus) {
		this.positionsStatus = positionsStatus;
	}

	private Integer mengenTeiler;

	public Object[] toDataRow() {
		Object[] row = new Object[AuftragReportFac.REPORT_PACKLISTE_ANZAHL_SPALTEN];
		row[AuftragReportFac.REPORT_PACKLISTE_IDENT] = ident;
		row[AuftragReportFac.REPORT_PACKLISTE_BEZEICHNUNG] = bezeichnung;
		row[AuftragReportFac.REPORT_PACKLISTE_GESAMTMENGE] = gesamtMenge;
		row[AuftragReportFac.REPORT_PACKLISTE_OFFENEMENGE] = offeneMenge;
		row[AuftragReportFac.REPORT_PACKLISTE_LAGERSTAND] = lagerstandAbbuchungslager;
		row[AuftragReportFac.REPORT_PACKLISTE_LAGERSTAND_ALLER_LAEGER] = lagerstandAllerLaeger;
		row[AuftragReportFac.REPORT_PACKLISTE_LAGERORT] = lagerplatz;
		row[AuftragReportFac.REPORT_PACKLISTE_GEWICHT] = gewicht;
		row[AuftragReportFac.REPORT_PACKLISTE_RASTER_LIEGEND] = rasterLiegend;
		row[AuftragReportFac.REPORT_PACKLISTE_RASTER_STEHEND] = rasterStehend;
		row[AuftragReportFac.REPORT_PACKLISTE_MATERIALGEWICHT] = materialgewicht;
		row[AuftragReportFac.REPORT_PACKLISTE_SERIENCHARGENR] = serienChargenNr;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITION_FREIERTEXT] = freierText;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITION_KOMMENTAR_IMAGE] = kommentarImage;
		row[AuftragReportFac.REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_IDENT] = vollstaendigkeitKomponentenIdent;
		row[AuftragReportFac.REPORT_PACKLISTE_VOLLSTAENDIGKEIT_KOMPONENTEN_BEZ] = vollstaendigkeitKomponentenBez;
		row[AuftragReportFac.REPORT_PACKLISTE_VORZEICHEN] = vorzeichen;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITIONSTERMIN] = positionsTerminString;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITIONSTERMIN_TIMESTAMP] = positionsTerminTimestamp;
		row[AuftragReportFac.REPORT_PACKLISTE_FIKTIVERLAGERSTAND] = fiktiverLagerstand;
		row[AuftragReportFac.REPORT_PACKLISTE_ARTIKELKLASSE] = artikelklasse;
		row[AuftragReportFac.REPORT_PACKLISTE_ARTIKELGRUPPE] = artikelgruppe;
		row[AuftragReportFac.REPORT_PACKLISTE_FARBCODE] = farbcode;
		row[AuftragReportFac.REPORT_PACKLISTE_MATERIAL] = material;
		row[AuftragReportFac.REPORT_PACKLISTE_HOEHE] = hoehe;
		row[AuftragReportFac.REPORT_PACKLISTE_BREITE] = breite;
		row[AuftragReportFac.REPORT_PACKLISTE_TIEFE] = tiefe;
		row[AuftragReportFac.REPORT_PACKLISTE_BAUFORM] = bauform;
		row[AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSART] = verpackungsart;
		row[AuftragReportFac.REPORT_PACKLISTE_ARTIKELKOMMENTAR] = artikelkommentar;
		row[AuftragReportFac.REPORT_PACKLISTE_VERKAUFSEAN] = verkaufsEAN;
		row[AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSMENGE] = verpackungsmenge;
		row[AuftragReportFac.REPORT_PACKLISTE_VERPACKUNGSEAN] = verpackungsEAN;
		row[AuftragReportFac.REPORT_PACKLISTE_ARBEITSGAENGE] = arbeitsgaenge;
		row[AuftragReportFac.REPORT_PACKLISTE_MENGENTEILER] = mengenTeiler;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITIONSSTATUS] = positionsStatus;
		row[AuftragReportFac.REPORT_PACKLISTE_BEZEICHNUNG_EINZELN] = bezeichnungEinzeln;
		row[AuftragReportFac.REPORT_PACKLISTE_ZUSATZBEZEICHNUNG_EINZELN] = zusatzbezeichnungEinzeln;
		row[AuftragReportFac.REPORT_PACKLISTE_SETARTIKEL_TYP] = setArtikelTyp;
		row[AuftragReportFac.REPORT_PACKLISTE_POSITIONSNUMMER] = positionsnummer;
		row[AuftragReportFac.REPORT_PACKLISTE_AUFTRAGPOSITION_I_ID] = auftragspositionIId;
		row[AuftragReportFac.REPORT_PACKLISTE_GESEHEN] = bGesehen;
		row[AuftragReportFac.REPORT_PACKLISTE_KURZBEZEICHNUNG] = kurzbezeichnung;
		row[AuftragReportFac.REPORT_PACKLISTE_REFERENZNUMMER] = referenznummer;
		return row;

	}

	@Override
	public Object clone() {
		AuftragPacklistePositionDto pos = new AuftragPacklistePositionDto();
		pos.setIdent(ident);
		pos.setBezeichnung(bezeichnung);
		pos.setKurzbezeichnung(kurzbezeichnung);
		pos.setReferenznummer(referenznummer);
		pos.setBezeichnung(bezeichnung);
		pos.setBezeichnungEinzeln(bezeichnungEinzeln);
		pos.setZusatzbezeichnungEinzeln(zusatzbezeichnungEinzeln);
		pos.setBezeichnung(bezeichnung);
		pos.setGesamtMenge(gesamtMenge);
		pos.setOffeneMenge(offeneMenge);
		pos.setLagerstand(lagerstandAbbuchungslager);
		pos.setFiktiverLagerstand(fiktiverLagerstand);
		pos.setLagerplatz(lagerplatz);
		if (kommentarImage != null) {
			BufferedImage copyOfImage = new BufferedImage(
					kommentarImage.getWidth(null),
					kommentarImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = copyOfImage.createGraphics();
			g.drawImage(kommentarImage, 0, 0, null);
			pos.setKommentarImage(copyOfImage);
		}
		pos.setArtikelkommentar(artikelkommentar);
		pos.setFreierText(freierText);
		pos.setVollstaendigkeitKomponentenIdent(vollstaendigkeitKomponentenIdent);
		pos.setVollstaendigkeitKomponentenBez(vollstaendigkeitKomponentenBez);
		pos.setSerienChargenNr(serienChargenNr);
		if (arbeitsgaenge != null)
			pos.setArbeitsgaenge((LPDatenSubreport) arbeitsgaenge.clone());
		pos.setVorzeichen(vorzeichen);
		pos.setPositionsTerminString(positionsTerminString);
		if (positionsTerminTimestamp != null)
			pos.setPositionsTerminTimestamp(new Timestamp(
					positionsTerminTimestamp.getTime()));
		pos.setGewicht(gewicht);
		pos.setPositionsStatus(positionsStatus);
		pos.setRasterLiegend(rasterLiegend);
		pos.setRasterStehend(rasterStehend);
		pos.setMaterialgewicht(materialgewicht);
		pos.setHoehe(hoehe);
		pos.setBreite(breite);
		pos.setTiefe(tiefe);
		pos.setBauform(bauform);
		pos.setVerpackungsart(verpackungsart);
		pos.setFarbcode(farbcode);
		pos.setMaterial(material);
		pos.setArtikelklasse(artikelklasse);
		pos.setArtikelgruppe(artikelgruppe);
		pos.setVerkaufsEAN(verkaufsEAN);
		pos.setVerpackungsEAN(verpackungsEAN);
		pos.setVerpackungsmenge(verpackungsmenge);
		pos.setMengenTeiler(mengenTeiler);
		return pos;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public BigDecimal getGesamtMenge() {
		return gesamtMenge;
	}

	public void setGesamtMenge(BigDecimal gesamtMenge) {
		this.gesamtMenge = gesamtMenge;
	}

	public BigDecimal getOffeneMenge() {
		return offeneMenge;
	}

	public void setOffeneMenge(BigDecimal offeneMenge) {
		this.offeneMenge = offeneMenge;
	}

	public BigDecimal getLagerstand() {
		return lagerstandAbbuchungslager;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstandAbbuchungslager = lagerstand;
	}

	public BigDecimal getFiktiverLagerstand() {
		return fiktiverLagerstand;
	}

	public void setFiktiverLagerstand(BigDecimal fiktiverLagerstand) {
		this.fiktiverLagerstand = fiktiverLagerstand;
	}

	public String getLagerplatz() {
		return lagerplatz;
	}

	public void setLagerplatz(String lagerplatz) {
		this.lagerplatz = lagerplatz;
	}

	public Image getKommentarImage() {
		return kommentarImage;
	}

	public void setKommentarImage(Image kommentarImage) {
		this.kommentarImage = kommentarImage;
	}

	public String getArtikelkommentar() {
		return artikelkommentar;
	}

	public void setArtikelkommentar(String artikelkommentar) {
		this.artikelkommentar = artikelkommentar;
	}

	public String getFreierText() {
		return freierText;
	}

	public void setFreierText(String freierText) {
		this.freierText = freierText;
	}

	public String getVollstaendigkeitKomponentenIdent() {
		return vollstaendigkeitKomponentenIdent;
	}

	public void setVollstaendigkeitKomponentenIdent(
			String vollstaendigkeitKomponentenIdent) {
		this.vollstaendigkeitKomponentenIdent = vollstaendigkeitKomponentenIdent;
	}

	public String getVollstaendigkeitKomponentenBez() {
		return vollstaendigkeitKomponentenBez;
	}

	public void setVollstaendigkeitKomponentenBez(
			String vollstaendigkeitKomponentenBez) {
		this.vollstaendigkeitKomponentenBez = vollstaendigkeitKomponentenBez;
	}

	public String getSerienChargenNr() {
		return serienChargenNr;
	}

	public void setSerienChargenNr(String serienChargenNr) {
		this.serienChargenNr = serienChargenNr;
	}

	public LPDatenSubreport getArbeitsgaenge() {
		return arbeitsgaenge;
	}

	public void setArbeitsgaenge(LPDatenSubreport arbeitsgaenge) {
		this.arbeitsgaenge = arbeitsgaenge;
	}

	public Integer getVorzeichen() {
		return vorzeichen;
	}

	public void setVorzeichen(Integer vorzeichen) {
		this.vorzeichen = vorzeichen;
	}

	public String getPositionsTerminString() {
		return positionsTerminString;
	}

	public void setPositionsTerminString(String positionsTerminString) {
		this.positionsTerminString = positionsTerminString;
	}

	public Timestamp getPositionsTerminTimestamp() {
		return positionsTerminTimestamp;
	}

	public void setPositionsTerminTimestamp(Timestamp positionsTerminTimestamp) {
		this.positionsTerminTimestamp = positionsTerminTimestamp;
	}

	public BigDecimal getGewicht() {
		return gewicht;
	}

	public void setGewicht(BigDecimal gewicht) {
		this.gewicht = gewicht;
	}

	public Double getRasterLiegend() {
		return rasterLiegend;
	}

	public void setRasterLiegend(Double rasterLiegend) {
		this.rasterLiegend = rasterLiegend;
	}

	public Double getRasterStehend() {
		return rasterStehend;
	}

	public void setRasterStehend(Double rasterStehend) {
		this.rasterStehend = rasterStehend;
	}

	public Double getMaterialgewicht() {
		return materialgewicht;
	}

	public void setMaterialgewicht(Double materialgewicht) {
		this.materialgewicht = materialgewicht;
	}

	public Double getHoehe() {
		return hoehe;
	}

	public void setHoehe(Double hoehe) {
		this.hoehe = hoehe;
	}

	public Double getBreite() {
		return breite;
	}

	public void setBreite(Double breite) {
		this.breite = breite;
	}

	public Double getTiefe() {
		return tiefe;
	}

	public void setTiefe(Double tiefe) {
		this.tiefe = tiefe;
	}

	public String getBauform() {
		return bauform;
	}

	public void setBauform(String bauform) {
		this.bauform = bauform;
	}

	public String getVerpackungsart() {
		return verpackungsart;
	}

	public void setVerpackungsart(String verpackungsart) {
		this.verpackungsart = verpackungsart;
	}

	public String getFarbcode() {
		return farbcode;
	}

	public void setFarbcode(String farbcode) {
		this.farbcode = farbcode;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getArtikelklasse() {
		return artikelklasse;
	}

	public void setArtikelklasse(String artikelklasse) {
		this.artikelklasse = artikelklasse;
	}

	public String getArtikelgruppe() {
		return artikelgruppe;
	}

	public void setArtikelgruppe(String artikelgruppe) {
		this.artikelgruppe = artikelgruppe;
	}

	public String getVerkaufsEAN() {
		return verkaufsEAN;
	}

	public void setVerkaufsEAN(String verkaufsEAN) {
		this.verkaufsEAN = verkaufsEAN;
	}

	public String getVerpackungsEAN() {
		return verpackungsEAN;
	}

	public void setVerpackungsEAN(String verpackungsEAN) {
		this.verpackungsEAN = verpackungsEAN;
	}

	public Double getVerpackungsmenge() {
		return verpackungsmenge;
	}

	public void setVerpackungsmenge(Double verpackungsmenge) {
		this.verpackungsmenge = verpackungsmenge;
	}

	public Integer getMengenTeiler() {
		return mengenTeiler;
	}

	public void setMengenTeiler(Integer mengenTeiler) {
		this.mengenTeiler = mengenTeiler;
	}

}
