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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.BitSet;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.partner.ejb.Branche;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.system.ejb.Kostenstelle;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.util.IIId;

@HvDtoLogClass(name = HvDtoLogClass.KUNDE)
public class KundeDto implements Serializable, IIId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int I_UPD_DEBITORENKONTO_KEIN_UPDATE = 0;
	public final static int I_UPD_DEBITORENKONTO_UPDATE = 1;
	public final static int I_UPD_DEBITORENKONTO_PRUEFE_AUF_DOPPELTE = 2;

	int iF = 0;
	public int iFPartnerIIdRechnungsadresse = iF++;
	private Integer partnerIIdRechnungsadresse;

	public int iFPersonaliIdProvisionsempfaenger = iF++;
	private Integer personaliIdProvisionsempfaenger;

	public int iFIId = iF++;
	private Integer iId;

	public int iFPartnerIId = iF++;
	private Integer partnerIId;

	public int iFmandantCNr = iF++;
	private String mandantCNr;

	public int iFcWaehrung = iF++;
	private String cWaehrung;

	public int iFlieferartIId = iF++;
	private Integer lieferartIId;

	public int iFspediteurIId = iF++;
	private Integer spediteurIId;

	public int iFzahlungszielIId = iF++;
	private Integer zahlungszielIId;

	public int iFkostenstelleIId = iF++;
	private Integer kostenstelleIId;

	public int iFmwstsatzbezIId = iF++;
	private Integer mwstsatzbezIId;

	public int iFvkpfArtikelpreislisteIIdStdpreisliste = iF++;
	private Integer vkpfArtikelpreislisteIIdStdpreisliste;

	public int iFfRabattsatz = iF++;
	private Double fRabattsatz;

	public int iFiGarantieinmonaten = iF++;
	private Integer iGarantieinmonaten;

	public int iFxKommentar = iF++;
	private String xKommentar;

	public int iFtAnlegen = iF++;
	private Timestamp tAnlegen;

	public int iFpersonalAnlegenIID = iF++;
	private Integer personalAnlegenIID;

	public int iFtAendern = iF++;
	private Timestamp tAendern;

	public int iFpersonalAendernIID = iF++;
	private Integer personalAendernIID;


	public int iFcKurznr = iF++;
	private String cKurznr;

	public int iFnKreditlimit = iF++;
	private BigDecimal nKreditlimit;

	public int iFtBonitaet = iF++;
	private Date tBonitaet;

	public int iFtLiefersperream = iF++;
	private Date tLiefersperream;

	public int iFbMindermengenzuschlag = iF++;
	private Short bMindermengenzuschlag;

	public int iFbMonatsrechnung = iF++;
	private Short bMonatsrechnung;

	public int iFbSammelrechnung = iF++;
	private Short bSammelrechnung;

	public int iFbIstreempfaenger = iF++;
	private Short bIstreempfaenger;

	public int iFbPreiseanlsandrucken = iF++;
	private Short bPreiseanlsandrucken;

	public int iFiDefaultrekopiendrucken = iF++;
	private Integer iDefaultrekopiendrucken;

	public int iFiDefaultlskopiendrucken = iF++;
	private Integer iDefaultlskopiendrucken;

	public int iFbRechnungsdruckmitrabatt = iF++;
	private Short bRechnungsdruckmitrabatt;

	public int iFiMitarbeiteranzahl = iF++;
	private Integer iMitarbeiteranzahl;

	public int iFcTour = iF++;
	private String cTour;

	public int iFcLieferantennr = iF++;
	private String cLieferantennr;

	public int iFbDistributor = iF++;
	private Short bDistributor;

	public int iFbIstkunde = iF++;
	private Short bIstkunde;

	public int iFbReversecharge = iF++;
	private Short bReversecharge;

	public int iFbVersteckterlieferant = iF++;
	private Short bVersteckterlieferant;

	public int iFcAbc = iF++;
	private String cAbc;

	public int iFtAgbuebermittelung = iF++;
	private Date tAgbuebermittelung;

	public int iFbAkzeptiertteillieferung = iF++;
	private Short bAkzeptiertteillieferung;

	public int iFbLsgewichtangeben = iF++;
	private Short bLsgewichtangeben;

	public int iFparternbankIId = iF++;
	private Integer parternbankIId;

	public int iFbIstinteressent = iF++;
	private Short bIstinteressent;

	public int iFcFremdsystemnr = iF++;
	private String cFremdsystemnr;

	public int iFiidErloeseKonto = iF++;
	private Integer iidErloeseKonto;

	public int iFiidDebitorenkonto = iF++;
	private Integer iidDebitorenkonto;

	public int iFiDebitorenkontoAsIntegerNotiId = iF++;
	private Integer iDebitorenkontoAsIntegerNotiId = null;

	public int iFpartnerDto = iF++;
	private PartnerDto partnerDto = null;

	public int iFpartnerRechnungsadresseDto = iF++;
	private PartnerDto partnerRechnungsadresseDto = new PartnerDto();

	public int iFansprechpartnerDto = iF++;
	private AnsprechpartnerDto[] ansprechpartnerDto = null;

	public int iFsHinweisintern = iF++;
	private String sHinweisintern = null;

	public int iFsHinweisextern = iF++;
	private String sHinweisextern = null;

	public int iFfZessionsfaktor = iF++;
	private Double fZessionsfaktor = null;

	private Integer iLieferdauer;

	private String cIdExtern;

	private Integer verrechnungsmodellIId;

	public Integer getVerrechnungsmodellIId() {
		return verrechnungsmodellIId;
	}

	public void setVerrechnungsmodellIId(Integer verrechnungsmodellIId) {
		this.verrechnungsmodellIId = verrechnungsmodellIId;
	}

	public Integer getILieferdauer() {
		return iLieferdauer;
	}

	public void setILieferdauer(Integer lieferdauer) {
		iLieferdauer = lieferdauer;
	}

	// indi: 0 jedes feld hat einen inidkator - bit
	public BitSet bsIndikator = new BitSet(iF);

	private int iUpdateDebitorenkonto = I_UPD_DEBITORENKONTO_PRUEFE_AUF_DOPPELTE;
	private Integer iKundennummer;

	private Timestamp tErwerbsberechtigung;
	private String cErwerbsberechtigungsbegruendung;

	private BigDecimal nKupferzahl;

	public BigDecimal getNKupferzahl() {
		return nKupferzahl;
	}

	public void setNKupferzahl(BigDecimal nKupferzahl) {
		this.nKupferzahl = nKupferzahl;
	}

	private Integer iMaxRepos;

	public Integer getIMaxRepos() {
		return iMaxRepos;
	}

	public void setIMaxRepos(Integer iMaxRepos) {
		this.iMaxRepos = iMaxRepos;
	}

	private Integer reversechargeartId;
	private String laenderartCnr;

	public KundeDto() {
		// indi: 1 alle nicht gesetzt
		setPartnerDto(new PartnerDto());
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		// indi: 1 gesetzt
		bsIndikator.set(iFIId, true);
		this.iId = iId;
	}

	private Integer lagerIIdAbbuchungslager;

	@HvDtoLogIdCnr(entityClass = Lager.class)
	public Integer getLagerIIdAbbuchungslager() {
		return this.lagerIIdAbbuchungslager;
	}

	public void setLagerIIdAbbuchungslager(Integer lagerIIdAbbuchungslager) {
		this.lagerIIdAbbuchungslager = lagerIIdAbbuchungslager;
	}

	private Short bZollpapier;

	public Short getBZollpapier() {
		return bZollpapier;
	}

	public void setBZollpapier(Short bZollpapier) {
		this.bZollpapier = bZollpapier;
	}

	private Short bVkpreisAnhandLSDatum;
	
	
	public Short getBVkpreisAnhandLSDatum() {
		return bVkpreisAnhandLSDatum;
	}

	public void setBVkpreisAnhandLSDatum(Short bVkpreisAnhandLSDatum) {
		this.bVkpreisAnhandLSDatum = bVkpreisAnhandLSDatum;
	}

	
	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}


	public String getCWaehrung() {
		return cWaehrung;
	}

	public void setCWaehrung(String cWaehrung) {
		bsIndikator.set(iFcWaehrung, true);
		this.cWaehrung = cWaehrung;
	}

	@HvDtoLogIdCnr(entityClass = Lieferart.class)
	public Integer getLieferartIId() {
		return lieferartIId;
	}

	public void setLieferartIId(Integer lieferartIId) {
		bsIndikator.set(iFlieferartIId, true);
		this.lieferartIId = lieferartIId;
	}

	public Integer getSpediteurIId() {
		return spediteurIId;
	}

	public void setSpediteurIId(Integer speditionIId) {
		this.spediteurIId = speditionIId;
	}

	@HvDtoLogIdCBez(entityClass = Zahlungsziel.class)
	public Integer getZahlungszielIId() {
		return zahlungszielIId;
	}

	public void setZahlungszielIId(Integer zahlungszielIId) {
		this.zahlungszielIId = zahlungszielIId;
	}

	@HvDtoLogIdCnr(entityClass = Kostenstelle.class)
	public Integer getKostenstelleIId() {
		return kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}

	public Integer getMwstsatzbezIId() {
		return mwstsatzbezIId;
	}

	public void setMwstsatzbezIId(Integer mwstsatzbezIId) {
		this.mwstsatzbezIId = mwstsatzbezIId;
	}

	public Integer getVkpfArtikelpreislisteIIdStdpreisliste() {
		return vkpfArtikelpreislisteIIdStdpreisliste;
	}

	public void setVkpfArtikelpreislisteIIdStdpreisliste(Integer vkpfArtikelpreislisteIIdStdpreisliste) {
		this.vkpfArtikelpreislisteIIdStdpreisliste = vkpfArtikelpreislisteIIdStdpreisliste;
	}

	public Double getFRabattsatz() {
		return fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		bsIndikator.set(iFfRabattsatz, true);
		this.fRabattsatz = fRabattsatz;
	}

	public Integer getIGarantieinmonaten() {
		return iGarantieinmonaten;
	}

	public void setIGarantieinmonaten(Integer iGarantieinmonaten) {
		bsIndikator.set(iFiGarantieinmonaten, true);
		this.iGarantieinmonaten = iGarantieinmonaten;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		bsIndikator.set(iFxKommentar, true);
		this.xKommentar = xKommentar;
	}

	@HvDtoLogIgnore
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		bsIndikator.set(iFtAnlegen, true);
		this.tAnlegen = tAnlegen;
	}

	@HvDtoLogIgnore
	public Integer getPersonalAnlegenIId() {
		return personalAnlegenIID;
	}

	public void setPersonalAnlegenIID(Integer personalAnlegenIID) {
		bsIndikator.set(iFpersonalAnlegenIID, true);
		this.personalAnlegenIID = personalAnlegenIID;
	}

	@HvDtoLogIgnore
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		bsIndikator.set(iFtAendern, true);
		this.tAendern = tAendern;
	}

	@HvDtoLogIgnore
	public Integer getPersonalAendernIId() {
		return personalAendernIID;
	}

	public void setPersonalAendernIID(Integer personalAendernIID) {
		bsIndikator.set(iFpersonalAendernIID, true);
		this.personalAendernIID = personalAendernIID;
	}

	public String getCKurznr() {
		return cKurznr;
	}

	public void setCKurznr(String cKurznr) {
		bsIndikator.set(iFcKurznr, true);
		this.cKurznr = cKurznr;
	}

	public BigDecimal getNKreditlimit() {
		return nKreditlimit;
	}

	public void setNKreditlimit(BigDecimal nKreditlimit) {
		bsIndikator.set(iFnKreditlimit, true);
		this.nKreditlimit = nKreditlimit;
	}

	public Date getTBonitaet() {
		return tBonitaet;
	}

	public void setTBonitaet(Date tBonitaet) {
		bsIndikator.set(iFtBonitaet, true);
		this.tBonitaet = tBonitaet;
	}

	public Date getTLiefersperream() {
		return tLiefersperream;
	}

	public void setTLiefersperream(Date tLiefersperream) {
		bsIndikator.set(iFtLiefersperream, true);
		this.tLiefersperream = tLiefersperream;
	}

	public Short getBMindermengenzuschlag() {
		return bMindermengenzuschlag;
	}

	public void setBMindermengenzuschlag(Short bMindermengenzuschlag) {
		bsIndikator.set(iFbMindermengenzuschlag, true);
		this.bMindermengenzuschlag = bMindermengenzuschlag;
	}

	public Short getBMonatsrechnung() {
		return bMonatsrechnung;
	}

	public void setBMonatsrechnung(Short bMonatsrechnung) {
		bsIndikator.set(iFbMonatsrechnung, true);
		this.bMonatsrechnung = bMonatsrechnung;
	}

	public Short getBReversecharge() {
		return bReversecharge;
	}

	public void setBReversecharge(Short bReversecharge) {
		bsIndikator.set(iFbReversecharge, true);
		this.bReversecharge = bReversecharge;
	}

	private Short bZuschlagInklusive;

	public Short getBZuschlagInklusive() {
		return bZuschlagInklusive;
	}

	public void setBZuschlagInklusive(Short bZuschlagInklusive) {
		this.bZuschlagInklusive = bZuschlagInklusive;
	}

	public Short getBVersteckterlieferant() {
		return bVersteckterlieferant;
	}

	public void setBVersteckterlieferant(Short bVersteckterlieferant) {
		bsIndikator.set(iFbVersteckterlieferant, true);
		this.bVersteckterlieferant = bVersteckterlieferant;
	}

	public Short getBSammelrechnung() {
		return bSammelrechnung;
	}

	public void setBSammelrechnung(Short bSammelrechnung) {
		bsIndikator.set(iFbSammelrechnung, true);
		this.bSammelrechnung = bSammelrechnung;
	}

	public Short getBIstreempfaenger() {
		return bIstreempfaenger;
	}

	public void setBIstreempfaenger(Short bIstreempfaenger) {
		bsIndikator.set(iFbIstreempfaenger, true);
		this.bIstreempfaenger = bIstreempfaenger;
	}

	public Short getBPreiseanlsandrucken() {
		return bPreiseanlsandrucken;
	}

	public void setBPreiseanlsandrucken(Short bPreiseanlsandrucken) {
		bsIndikator.set(iFbPreiseanlsandrucken, true);
		this.bPreiseanlsandrucken = bPreiseanlsandrucken;
	}

	public Integer getIDefaultrekopiendrucken() {
		return iDefaultrekopiendrucken;
	}

	public void setIDefaultrekopiendrucken(Integer iDefaultrekopiendrucken) {
		bsIndikator.set(iFiDefaultrekopiendrucken, true);
		this.iDefaultrekopiendrucken = iDefaultrekopiendrucken;
	}

	public Integer getIDefaultlskopiendrucken() {
		return iDefaultlskopiendrucken;
	}

	public void setIDefaultlskopiendrucken(Integer iDefaultlskopiendrucken) {
		bsIndikator.set(iFiDefaultlskopiendrucken, true);
		this.iDefaultlskopiendrucken = iDefaultlskopiendrucken;
	}

	public Short getBRechnungsdruckmitrabatt() {
		return bRechnungsdruckmitrabatt;
	}

	public void setBRechnungsdruckmitrabatt(Short bRechnungsdruckmitrabatt) {
		bsIndikator.set(iFbRechnungsdruckmitrabatt, true);
		this.bRechnungsdruckmitrabatt = bRechnungsdruckmitrabatt;
	}

	public Integer getIMitarbeiteranzahl() {
		return iMitarbeiteranzahl;
	}

	public void setIMitarbeiteranzahl(Integer iMitarbeiteranzahl) {
		bsIndikator.set(iFiMitarbeiteranzahl, true);
		this.iMitarbeiteranzahl = iMitarbeiteranzahl;
	}

	public String getCTour() {
		return cTour;
	}

	public void setCTour(String cTour) {
		bsIndikator.set(iFcTour, true);
		this.cTour = cTour;
	}

	public String getCLieferantennr() {
		return cLieferantennr;
	}

	public void setCLieferantennr(String cLieferantennr) {
		bsIndikator.set(iFcLieferantennr, true);
		this.cLieferantennr = cLieferantennr;
	}

	public Short getBDistributor() {
		return bDistributor;
	}

	public void setBDistributor(Short bDistributor) {
		bsIndikator.set(iFbDistributor, true);
		this.bDistributor = bDistributor;
	}

	public Short getBIstkunde() {
		return bIstkunde;
	}

	public void setBIstkunde(Short bIstkunde) {
		bsIndikator.set(iFbIstkunde, true);
		this.bIstkunde = bIstkunde;
	}

	public String getCAbc() {
		return cAbc;
	}

	public void setCAbc(String cAbc) {
		bsIndikator.set(iFcAbc, true);
		this.cAbc = cAbc;
	}

	public Date getTAgbuebermittelung() {
		return tAgbuebermittelung;
	}

	public void setTAgbuebermittelung(Date tAgbuebermittelung) {
		bsIndikator.set(iFtAgbuebermittelung, true);
		this.tAgbuebermittelung = tAgbuebermittelung;
	}

	public Short getBAkzeptiertteillieferung() {
		return bAkzeptiertteillieferung;
	}

	public void setBAkzeptiertteillieferung(Short bAkzeptiertteillieferung) {
		bsIndikator.set(iFbAkzeptiertteillieferung, true);
		this.bAkzeptiertteillieferung = bAkzeptiertteillieferung;
	}

	public Short getBLsgewichtangeben() {
		return bLsgewichtangeben;
	}

	public void setBLsgewichtangeben(Short bLsgewichtangeben) {
		bsIndikator.set(iFbLsgewichtangeben, true);
		this.bLsgewichtangeben = bLsgewichtangeben;
	}

	public Integer getParternbankIId() {
		return parternbankIId;
	}

	public void setParternbankIId(Integer parternbankIId) {
		bsIndikator.set(iFparternbankIId, true);
		this.parternbankIId = parternbankIId;
	}

	public Short getbIstinteressent() {
		return bIstinteressent;
	}

	public void setbIstinteressent(Short bIstinteressent) {
		bsIndikator.set(iFbIstinteressent, true);
		this.bIstinteressent = bIstinteressent;
	}

	public String getCFremdsystemnr() {
		return cFremdsystemnr;
	}

	public void setCFremdsystemnr(String cFremdsystemnr) {
		bsIndikator.set(iFcFremdsystemnr, true);
		this.cFremdsystemnr = cFremdsystemnr;
	}

	@HvDtoLogIdCnr(entityClass = Konto.class)
	public Integer getIidErloeseKonto() {
		return iidErloeseKonto;
	}

	@HvDtoLogIgnore
	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setIidErloeseKonto(Integer iidErloeseKonto) {
		bsIndikator.set(iFiidErloeseKonto, true);
		this.iidErloeseKonto = iidErloeseKonto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		bsIndikator.set(iFpartnerDto, true);
		this.partnerDto = partnerDto;
	}

	public void setCErwerbsberechtigungsbegruendung(String cErwerbsberechtigungsbegruendung) {
		this.cErwerbsberechtigungsbegruendung = cErwerbsberechtigungsbegruendung;
	}

	public String getCErwerbsberechtigungsbegruendung() {
		return this.cErwerbsberechtigungsbegruendung;
	}

	public Timestamp getTErwerbsberechtigung() {
		return this.tErwerbsberechtigung;
	}

	public void setTErwerbsberechtigung(Timestamp tErwerbsberechtigung) {
		this.tErwerbsberechtigung = tErwerbsberechtigung;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KundeDto)) {
			return false;
		}
		KundeDto that = (KundeDto) obj;
		if (!(that.iId == null ? this.iId == null : that.iId.equals(this.iId))) {
			return false;
		}
		if (!(that.partnerIId == null ? this.partnerIId == null : that.partnerIId.equals(this.partnerIId))) {
			return false;
		}
		if (!(that.mandantCNr == null ? this.mandantCNr == null : that.mandantCNr.equals(this.mandantCNr))) {
			return false;
		}
		if (!(that.cWaehrung == null ? this.cWaehrung == null : that.cWaehrung.equals(this.cWaehrung))) {
			return false;
		}
		if (!(that.lieferartIId == null ? this.lieferartIId == null : that.lieferartIId.equals(this.lieferartIId))) {
			return false;
		}
		if (!(that.spediteurIId == null ? this.spediteurIId == null : that.spediteurIId.equals(this.spediteurIId))) {
			return false;
		}
		if (!(that.zahlungszielIId == null ? this.zahlungszielIId == null
				: that.zahlungszielIId.equals(this.zahlungszielIId))) {
			return false;
		}
		if (!(that.kostenstelleIId == null ? this.kostenstelleIId == null
				: that.kostenstelleIId.equals(this.kostenstelleIId))) {
			return false;
		}
		if (!(that.mwstsatzbezIId == null ? this.mwstsatzbezIId == null
				: that.mwstsatzbezIId.equals(this.mwstsatzbezIId))) {
			return false;
		}
		if (!(that.vkpfArtikelpreislisteIIdStdpreisliste == null ? this.vkpfArtikelpreislisteIIdStdpreisliste == null
				: that.vkpfArtikelpreislisteIIdStdpreisliste.equals(this.vkpfArtikelpreislisteIIdStdpreisliste))) {
			return false;
		}
		if (!(that.fRabattsatz == null ? this.fRabattsatz == null : that.fRabattsatz.equals(this.fRabattsatz))) {
			return false;
		}
		if (!(that.iGarantieinmonaten == null ? this.iGarantieinmonaten == null
				: that.iGarantieinmonaten.equals(this.iGarantieinmonaten))) {
			return false;
		}
		if (!(that.xKommentar == null ? this.xKommentar == null : that.xKommentar.equals(this.xKommentar))) {
			return false;
		}
		if (!(that.tAnlegen == null ? this.tAnlegen == null : that.tAnlegen.equals(this.tAnlegen))) {
			return false;
		}
		if (!(that.personalAnlegenIID == null ? this.personalAnlegenIID == null
				: that.personalAnlegenIID.equals(this.personalAnlegenIID))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalAendernIID == null ? this.personalAendernIID == null
				: that.personalAendernIID.equals(this.personalAendernIID))) {
			return false;
		}
		if (!(that.cKurznr == null ? this.cKurznr == null : that.cKurznr.equals(this.cKurznr))) {
			return false;
		}
		if (!(that.nKreditlimit == null ? this.nKreditlimit == null : that.nKreditlimit.equals(this.nKreditlimit))) {
			return false;
		}
		if (!(that.tBonitaet == null ? this.tBonitaet == null : that.tBonitaet.equals(this.tBonitaet))) {
			return false;
		}
		if (!(that.tLiefersperream == null ? this.tLiefersperream == null
				: that.tLiefersperream.equals(this.tLiefersperream))) {
			return false;
		}
		if (!(that.bMindermengenzuschlag == null ? this.bMindermengenzuschlag == null
				: that.bMindermengenzuschlag.equals(this.bMindermengenzuschlag))) {
			return false;
		}
		if (!(that.bMonatsrechnung == null ? this.bMonatsrechnung == null
				: that.bMonatsrechnung.equals(this.bMonatsrechnung))) {
			return false;
		}
		if (!(that.bSammelrechnung == null ? this.bSammelrechnung == null
				: that.bSammelrechnung.equals(this.bSammelrechnung))) {
			return false;
		}
		if (!(that.bIstreempfaenger == null ? this.bIstreempfaenger == null
				: that.bIstreempfaenger.equals(this.bIstreempfaenger))) {
			return false;
		}
		if (!(that.bVkpreisAnhandLSDatum == null ? this.bVkpreisAnhandLSDatum == null
				: that.bVkpreisAnhandLSDatum.equals(this.bVkpreisAnhandLSDatum))) {
			return false;
		}
		if (!(that.bPreiseanlsandrucken == null ? this.bPreiseanlsandrucken == null
				: that.bPreiseanlsandrucken.equals(this.bPreiseanlsandrucken))) {
			return false;
		}
		if (!(that.iDefaultrekopiendrucken == null ? this.iDefaultrekopiendrucken == null
				: that.iDefaultrekopiendrucken.equals(this.iDefaultrekopiendrucken))) {
			return false;
		}
		if (!(that.iDefaultlskopiendrucken == null ? this.iDefaultlskopiendrucken == null
				: that.iDefaultlskopiendrucken.equals(this.iDefaultlskopiendrucken))) {
			return false;
		}
		if (!(that.bRechnungsdruckmitrabatt == null ? this.bRechnungsdruckmitrabatt == null
				: that.bRechnungsdruckmitrabatt.equals(this.bRechnungsdruckmitrabatt))) {
			return false;
		}
		if (!(that.iMitarbeiteranzahl == null ? this.iMitarbeiteranzahl == null
				: that.iMitarbeiteranzahl.equals(this.iMitarbeiteranzahl))) {
			return false;
		}
		if (!(that.cTour == null ? this.cTour == null : that.cTour.equals(this.cTour))) {
			return false;
		}
		if (!(that.cLieferantennr == null ? this.cLieferantennr == null
				: that.cLieferantennr.equals(this.cLieferantennr))) {
			return false;
		}
		if (!(that.bDistributor == null ? this.bDistributor == null : that.bDistributor.equals(this.bDistributor))) {
			return false;
		}
		if (!(that.bIstkunde == null ? this.bIstkunde == null : that.bIstkunde.equals(this.bIstkunde))) {
			return false;
		}
		if (!(that.bReversecharge == null ? this.bReversecharge == null
				: that.bReversecharge.equals(this.bReversecharge))) {
			return false;
		}
		if (!(that.bVersteckterlieferant == null ? this.bVersteckterlieferant == null
				: that.bVersteckterlieferant.equals(this.bVersteckterlieferant))) {
			return false;
		}
		if (!(that.cAbc == null ? this.cAbc == null : that.cAbc.equals(this.cAbc))) {
			return false;
		}
		if (!(that.tAgbuebermittelung == null ? this.tAgbuebermittelung == null
				: that.tAgbuebermittelung.equals(this.tAgbuebermittelung))) {
			return false;
		}
		if (!(that.bAkzeptiertteillieferung == null ? this.bAkzeptiertteillieferung == null
				: that.bAkzeptiertteillieferung.equals(this.bAkzeptiertteillieferung))) {
			return false;
		}
		if (!(that.bLsgewichtangeben == null ? this.bLsgewichtangeben == null
				: that.bLsgewichtangeben.equals(this.bLsgewichtangeben))) {
			return false;
		}
		if (!(that.parternbankIId == null ? this.parternbankIId == null
				: that.parternbankIId.equals(this.parternbankIId))) {
			return false;
		}
		if (!(that.bIstinteressent == null ? this.bIstinteressent == null
				: that.bIstinteressent.equals(this.bIstinteressent))) {
			return false;
		}
		if (!(that.cFremdsystemnr == null ? this.cFremdsystemnr == null
				: that.cFremdsystemnr.equals(this.cFremdsystemnr))) {
			return false;
		}
		if (!(that.iidDebitorenkonto == null ? this.iidDebitorenkonto == null
				: that.iidDebitorenkonto.equals(this.iidDebitorenkonto))) {
			return false;
		}
		if (!(that.iidErloeseKonto == null ? this.iidErloeseKonto == null
				: that.iidErloeseKonto.equals(this.iidErloeseKonto))) {
			return false;
		}
		if (!(that.personaliIdProvisionsempfaenger == null ? this.personaliIdProvisionsempfaenger == null
				: that.personaliIdProvisionsempfaenger.equals(this.personaliIdProvisionsempfaenger))) {
			return false;
		}
		if (!(that.tErwerbsberechtigung == null ? this.tErwerbsberechtigung == null
				: that.tErwerbsberechtigung.equals(this.tErwerbsberechtigung))) {
			return false;
		}
		if (!(that.cErwerbsberechtigungsbegruendung == null ? this.cErwerbsberechtigungsbegruendung == null
				: that.cErwerbsberechtigungsbegruendung.equals(this.cErwerbsberechtigungsbegruendung))) {
			return false;
		}
		if (!(that.iKundennummer == null ? this.iKundennummer == null
				: that.iKundennummer.equals(this.iKundennummer))) {
			return false;
		}
		if (!(that.laenderartCnr == null ? this.laenderartCnr == null
				: that.laenderartCnr.equals(this.laenderartCnr))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.iId.hashCode();
		result = 37 * result + this.partnerIId.hashCode();
		result = 37 * result + this.mandantCNr.hashCode();
		result = 37 * result + this.cWaehrung.hashCode();
		result = 37 * result + this.lieferartIId.hashCode();
		result = 37 * result + this.spediteurIId.hashCode();
		result = 37 * result + this.zahlungszielIId.hashCode();
		result = 37 * result + this.kostenstelleIId.hashCode();
		result = 37 * result + this.mwstsatzbezIId.hashCode();
		result = 37 * result + this.vkpfArtikelpreislisteIIdStdpreisliste.hashCode();
		result = 37 * result + this.fRabattsatz.hashCode();
		result = 37 * result + this.iGarantieinmonaten.hashCode();
		result = 37 * result + this.xKommentar.hashCode();
		result = 37 * result + this.tAnlegen.hashCode();
		result = 37 * result + this.personalAnlegenIID.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalAendernIID.hashCode();
		result = 37 * result + this.cKurznr.hashCode();
		result = 37 * result + this.nKreditlimit.hashCode();
		result = 37 * result + this.tBonitaet.hashCode();
		result = 37 * result + this.tLiefersperream.hashCode();
		result = 37 * result + this.bMindermengenzuschlag.hashCode();
		result = 37 * result + this.bMonatsrechnung.hashCode();
		result = 37 * result + this.bSammelrechnung.hashCode();
		result = 37 * result + this.bIstreempfaenger.hashCode();
		result = 37 * result + this.bPreiseanlsandrucken.hashCode();
		result = 37 * result + this.iDefaultrekopiendrucken.hashCode();
		result = 37 * result + this.iDefaultlskopiendrucken.hashCode();
		result = 37 * result + this.bRechnungsdruckmitrabatt.hashCode();
		result = 37 * result + this.iMitarbeiteranzahl.hashCode();
		result = 37 * result + this.cTour.hashCode();
		result = 37 * result + this.cLieferantennr.hashCode();
		result = 37 * result + this.bDistributor.hashCode();
		result = 37 * result + this.bIstkunde.hashCode();
		result = 37 * result + this.bReversecharge.hashCode();
		result = 37 * result + this.bVersteckterlieferant.hashCode();
		result = 37 * result + this.cAbc.hashCode();
		result = 37 * result + this.tAgbuebermittelung.hashCode();
		result = 37 * result + this.bAkzeptiertteillieferung.hashCode();
		result = 37 * result + this.bLsgewichtangeben.hashCode();
		result = 37 * result + this.parternbankIId.hashCode();
		result = 37 * result + this.bIstinteressent.hashCode();
		result = 37 * result + this.cFremdsystemnr.hashCode();
		result = 37 * result + this.iidDebitorenkonto.hashCode();
		result = 37 * result + this.iidErloeseKonto.hashCode();
		result = 37 * result + this.personaliIdProvisionsempfaenger.hashCode();
		result = 37 * result + this.tErwerbsberechtigung.hashCode();
		result = 37 * result + this.cErwerbsberechtigungsbegruendung.hashCode();
		result = 37 * result + this.iKundennummer.hashCode();
		result = 37 * result + this.laenderartCnr.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += iId;
		returnString += ", tAnlegen=" + tAnlegen;
		returnString += ", personalAnlegenIID=" + personalAnlegenIID;
		returnString += ", tAendern=" + tAendern;
		returnString += ", personalAendernIID=" + personalAendernIID;
		returnString += ", cKurznr=" + cKurznr;
		returnString += ", partnerIId=" + partnerIId;
		returnString += ", nKreditlimit=" + nKreditlimit;
		returnString += ", tBonitaet=" + tBonitaet;
		returnString += ", tLiefersperream=" + tLiefersperream;
		returnString += ", bMindermengenzuschlag=" + bMindermengenzuschlag;
		returnString += ", bMonatsrechnung=" + bMonatsrechnung;
		returnString += ", bSammelrechnung=" + bSammelrechnung;
		returnString += ", " + mandantCNr;
		returnString += ", " + cWaehrung;
		returnString += ", " + lieferartIId;
		returnString += ", " + spediteurIId;
		returnString += ", " + zahlungszielIId;
		returnString += ", " + kostenstelleIId;
		returnString += ", " + mwstsatzbezIId;
		returnString += ", " + vkpfArtikelpreislisteIIdStdpreisliste;
		returnString += ", " + fRabattsatz;
		returnString += ", " + iGarantieinmonaten;
		returnString += ", " + xKommentar;
		returnString += ", " + bIstreempfaenger;
		returnString += ", " + bPreiseanlsandrucken;
		returnString += ", " + iDefaultrekopiendrucken;
		returnString += ", " + iDefaultlskopiendrucken;
		returnString += ", " + bRechnungsdruckmitrabatt;
		returnString += ", " + iMitarbeiteranzahl;
		returnString += ", " + cTour;
		returnString += ", " + cLieferantennr;
		returnString += ", " + bDistributor;
		returnString += ", " + bIstkunde;
		returnString += ", " + bReversecharge;
		returnString += ", " + bVersteckterlieferant;
		returnString += ", " + cAbc;
		returnString += ", " + tAgbuebermittelung;
		returnString += ", " + bAkzeptiertteillieferung;
		returnString += ", " + bLsgewichtangeben;
		returnString += ", " + parternbankIId;
		returnString += ", " + bIstinteressent;
		returnString += ", " + cFremdsystemnr;
		returnString += ", " + iidDebitorenkonto;
		returnString += ", " + iidErloeseKonto;
		returnString += ", " + personaliIdProvisionsempfaenger;
		returnString += ", " + iKundennummer;
		returnString += ", " + tErwerbsberechtigung;
		returnString += ", " + cErwerbsberechtigungsbegruendung;
		return returnString;
	}

	public Integer getPersonaliIdProvisionsempfaenger() {
		return personaliIdProvisionsempfaenger;
	}

	@HvDtoLogIgnore
	public PartnerDto getPartnerRechnungsadresseDto() {
		return partnerRechnungsadresseDto;
	}

	public Integer getPartnerIIdRechnungsadresse() {
		return partnerIIdRechnungsadresse;
	}

	public void setPersonaliIdProvisionsempfaenger(Integer personaliIdProvisionsempfaenger) {
		bsIndikator.set(iFPersonaliIdProvisionsempfaenger, true);
		this.personaliIdProvisionsempfaenger = personaliIdProvisionsempfaenger;
	}

	public void setPartnerRechnungsadresseDto(PartnerDto partnerRechnungsadresseDto) {
		bsIndikator.set(iFpartnerRechnungsadresseDto, true);
		this.partnerRechnungsadresseDto = partnerRechnungsadresseDto;
	}

	public void setPartnerIIdRechnungsadresse(Integer partnerIIdRechnungsadresse) {
		bsIndikator.set(iFPartnerIIdRechnungsadresse, true);
		this.partnerIIdRechnungsadresse = partnerIIdRechnungsadresse;
	}

	@HvDtoLogIdCnr(entityClass = Konto.class)
	public Integer getIidDebitorenkonto() {
		return iidDebitorenkonto;
	}

	@HvDtoLogIgnore
	public Integer getIDebitorenkontoAsIntegerNotiId() {
		return iDebitorenkontoAsIntegerNotiId;
	}

	@HvDtoLogIgnore
	public int getUpdateModeDebitorenkonto() {
		return iUpdateDebitorenkonto;
	}

	@HvDtoLogIgnore
	public BitSet getBsIndikator() {
		return bsIndikator;
	}

	@HvDtoLogIgnore
	public AnsprechpartnerDto[] getAnsprechpartnerDto() {
		return ansprechpartnerDto;
	}

	public String getSHinweisintern() {
		return sHinweisintern;
	}

	public String getSHinweisextern() {
		return sHinweisextern;
	}

	public Double getFZessionsfaktor() {
		return fZessionsfaktor;
	}

	public Integer getIKundennummer() {
		return iKundennummer;
	}

	public void setIidDebitorenkonto(Integer iidDebitorenkonto) {
		bsIndikator.set(iFiidDebitorenkonto, true);
		this.iidDebitorenkonto = iidDebitorenkonto;
	}

	public void setIDebitorenkontoAsIntegerNotiId(Integer iDebitorenkontoAsIntegerNotiIdI) {
		bsIndikator.set(iFiDebitorenkontoAsIntegerNotiId, true);
		this.iDebitorenkontoAsIntegerNotiId = iDebitorenkontoAsIntegerNotiIdI;
	}

	public void setUpdateModeDebitorenkonto(int iUpdateDebitorenkonto) {
		this.iUpdateDebitorenkonto = iUpdateDebitorenkonto;
	}

	public void setBsIndikator(BitSet bsIndikator) {
		this.bsIndikator = bsIndikator;
	}

	public void setAnsprechpartnerDto(AnsprechpartnerDto[] ansprechpartnerDto) {
		bsIndikator.set(iFansprechpartnerDto, true);
		this.ansprechpartnerDto = ansprechpartnerDto;
	}

	public void setSHinweisintern(String sHinweisintern) {
		bsIndikator.set(iFsHinweisintern, true);
		this.sHinweisintern = sHinweisintern;
	}

	public void setSHinweisextern(String sHinweisextern) {
		bsIndikator.set(iFsHinweisextern, true);
		this.sHinweisextern = sHinweisextern;
	}

	public void setFZessionsfaktor(Double fZessionsfaktor) {
		bsIndikator.set(iFfZessionsfaktor, true);
		this.fZessionsfaktor = fZessionsfaktor;
	}

	public void setIKundennummer(Integer iKundennummer) {
		this.iKundennummer = iKundennummer;
	}

	private BigDecimal nMindestbestellwert;

	public BigDecimal getNMindestbestellwert() {
		return nMindestbestellwert;
	}

	public void setNMindestbestellwert(BigDecimal nMindestbestellwert) {
		this.nMindestbestellwert = nMindestbestellwert;
	}

	/**
	 * Die externe ID (Magento-Id)
	 * 
	 * @return die Id des externen Systems
	 */
	public String getCIdExtern() {
		return cIdExtern;
	}

	public void setCIdExtern(String cIdExtern) {
		this.cIdExtern = cIdExtern;
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}

	private String cEmailRechnungsempfang;

	public String getCEmailRechnungsempfang() {
		return cEmailRechnungsempfang;
	}

	public void setCEmailRechnungsempfang(String cEmailRechnungsempfang) {
		this.cEmailRechnungsempfang = cEmailRechnungsempfang;
	}

	public String getLaenderartCnr() {
		return laenderartCnr;
	}

	public void setLaenderartCnr(String laenderartCnr) {
		this.laenderartCnr = laenderartCnr;
	}

	private Short bRechnungJeLieferadresse;

	public Short getBRechnungJeLieferadresse() {
		return bRechnungJeLieferadresse;
	}

	public void setBRechnungJeLieferadresse(Short bRechnungJeLieferadresse) {
		this.bRechnungJeLieferadresse = bRechnungJeLieferadresse;
	}

	private Double fVerpackungskostenInProzent;

	public Double getfVerpackungskostenInProzent() {
		return fVerpackungskostenInProzent;
	}

	public void setfVerpackungskostenInProzent(Double fVerpackungskostenInProzent) {
		this.fVerpackungskostenInProzent = fVerpackungskostenInProzent;
	}

}
