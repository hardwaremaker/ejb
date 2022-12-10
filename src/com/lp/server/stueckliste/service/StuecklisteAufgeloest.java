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
package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.util.Helper;

public class StuecklisteAufgeloest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StuecklistepositionDto stuecklistepositionDto;

	private double dWbzInTagenAusBevoruzgtemArtikellieferant = 0;

	private ArtikellieferantDto artikellieferantDto = null;

	public ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}

	public void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}

	public double getWbzInTagenAusBevoruzgtemArtikellieferant() {
		return dWbzInTagenAusBevoruzgtemArtikellieferant;
	}

	public void setWbzInTagenAusBevoruzgtemArtikellieferant(double dWbzInTagenAusBevoruzgtemArtikellieferant) {
		this.dWbzInTagenAusBevoruzgtemArtikellieferant = dWbzInTagenAusBevoruzgtemArtikellieferant;
	}

	public boolean isBStueckliste() {
		if (getStuecklisteDto() != null && getPositionen() != null && getPositionen().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isBFremdfertigung() {
		if (getStuecklisteDto() != null && Helper.short2boolean(getStuecklisteDto().getBFremdfertigung()) == true) {
			return true;
		} else {
			return false;
		}
	}

	private BigDecimal lagerstand = new BigDecimal(0);

	private BigDecimal lagerstandSperrlaeger = new BigDecimal(0);

	private BigDecimal inFertigung = new BigDecimal(0);

	private BigDecimal bdLagerstandsFehlmenge = new BigDecimal(0);

	public BigDecimal getBdLagerstandsFehlmenge() {
		return bdLagerstandsFehlmenge;
	}

	public void setBdLagerstandsFehlmenge(BigDecimal bdLagerstandsFehlmenge) {
		this.bdLagerstandsFehlmenge = bdLagerstandsFehlmenge;
	}

	public BigDecimal getLagerstandSperrlaeger() {
		return lagerstandSperrlaeger;
	}

	public void setLagerstandSperrlaeger(BigDecimal lagerstandSperrlaeger) {
		this.lagerstandSperrlaeger = lagerstandSperrlaeger;
	}

	public BigDecimal getInFertigung() {
		return inFertigung;
	}

	public void setInFertigung(BigDecimal inFertigung) {
		this.inFertigung = inFertigung;
	}

	public BigDecimal getLagerstand() {
		return lagerstand;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}

	HashMap<String, BigDecimal> lagerstaendeZumAufloesungszeitpunkt = null;

	private StuecklisteDto stuecklisteDto = null;

	public double getVorlaufendeProduktionsdauer() {
		double d = 0;
		if (getStuecklisteDto() == null) {
			d += getWbzInTagenAusBevoruzgtemArtikellieferant();
		}
		return d + getVorlaufendeProduktionsdauer(getPositionen());
	}

	public double getDauerBiszurFertigstellungDieserEbene() {

		if (getStuecklisteDto() != null) {
			return getMaximaleBeschaffungsdauerDerAktuellenEbene()
					+ getStuecklisteDto().getNDefaultdurchlaufzeit().doubleValue();
		} else {

			if (vorgaenger != null && vorgaenger.getStuecklisteDto() != null) {

				return getMaximaleBeschaffungsdauerDerAktuellenEbene()
						+ vorgaenger.getStuecklisteDto().getNDefaultdurchlaufzeit().doubleValue();

			} else {
				return 0;
			}
		}

	}

	public double getMaximaleBeschaffungsdauerDerAktuellenEbene() {

		if (getVorgaenger() != null) {
			return getMaximaleBeschaffungsdauerDerAktuellenEbene(getVorgaenger().getPositionen());
		} else {
			return getMaximaleBeschaffungsdauerDerAktuellenEbene(getPositionen());
		}

	}

	private double getMaximaleBeschaffungsdauerDerAktuellenEbene(ArrayList<StuecklisteAufgeloest> positionen) {

		double dMaxWBZ = 0;

		for (int i = 0; i < positionen.size(); i++) {
			if (positionen.get(i).getStuecklisteDto() == null) {
				double d = positionen.get(i).getWbzInTagenAusBevoruzgtemArtikellieferant();

				if (positionen.get(i).getStuecklistepositionDto().getNMenge().doubleValue() <= positionen.get(i)
						.getLagerstand().doubleValue()) {
					d = 0;
				}

				if (d > dMaxWBZ) {
					dMaxWBZ = d;
				}

			} else {
				double d = 0;
				// Wenn die Stueckliste lagernd, dann brauch ich nicht mehr
				// weiter runter
				if (positionen.get(i).getStuecklistepositionDto().getNMenge().doubleValue() <= positionen.get(i)
						.getLagerstand().doubleValue()) {
					d = 0;
				} else {
					d = getMaximaleBeschaffungsdauerDerAktuellenEbene(positionen.get(i).getPositionen())
							+ positionen.get(i).getStuecklisteDto().getNDefaultdurchlaufzeit().doubleValue();
				}

				if (d > dMaxWBZ) {
					dMaxWBZ = d;
				}

			}
		}

		return dMaxWBZ;
	}

	private double getVorlaufendeProduktionsdauer(ArrayList<StuecklisteAufgeloest> positionen) {
		double d = 0;

		if (positionen != null) {

			for (int i = 0; i < positionen.size(); i++) {
				if (positionen.get(i).getStuecklisteDto() == null) {
					d += positionen.get(i).getWbzInTagenAusBevoruzgtemArtikellieferant();
				}
				d += getVorlaufendeProduktionsdauer(positionen.get(i).getPositionen());
			}
		}

		return d;
	}

	public double getNachfolgendeProduktionsdauer() {
		double d = 0;

		if (getStuecklisteDto() != null && getStuecklisteDto().getNDefaultdurchlaufzeit() != null) {
			d += getStuecklisteDto().getNDefaultdurchlaufzeit().doubleValue();
		}

		return d + getNachfolgendeProduktionsdauer(vorgaenger);
	}

	private double getNachfolgendeProduktionsdauer(StuecklisteAufgeloest vorgaenger) {
		double d = 0;

		if (vorgaenger != null && vorgaenger.getStuecklisteDto().getNDefaultdurchlaufzeit() != null) {
			d += vorgaenger.getStuecklisteDto().getNDefaultdurchlaufzeit().doubleValue();
			d += getNachfolgendeProduktionsdauer(vorgaenger.getVorgaenger());
		}

		return d;
	}

	public StuecklisteAufgeloest getVorgaenger() {
		return vorgaenger;
	}

	public int getAnzahlPositionen() {
		return getAnzahlPositionen(getPositionen());

	}

	public ArrayList<StuecklisteAufgeloest> getAlStuecklisteAufgeloest_OhneLagerndeArtikelUndOhneStuecklisten(
			BigDecimal bdKopfmenge, boolean bBeruecksichtigeInFertigung) {

		HashMap<Integer, BigDecimal> hmLagerstaende = new HashMap<Integer, BigDecimal>();

		ArrayList<StuecklisteAufgeloest> al = new ArrayList<StuecklisteAufgeloest>();

		al.addAll(getAlStuecklisteAufgeloest_OhneLagerndeArtikelUndOhneStuecklisten(this.getPositionen(),
				hmLagerstaende, bBeruecksichtigeInFertigung, bdKopfmenge));

		return al;

	}

	private ArrayList<StuecklisteAufgeloest> getAlStuecklisteAufgeloest_OhneLagerndeArtikelUndOhneStuecklisten(
			ArrayList<StuecklisteAufgeloest> positionen, HashMap<Integer, BigDecimal> hmLagerstaende,
			boolean bBeruecksichtigeInFertigung, BigDecimal bdKopfmenge) {

		ArrayList<StuecklisteAufgeloest> temp = new ArrayList<StuecklisteAufgeloest>();

		if (positionen != null) {
			for (int i = 0; i < positionen.size(); i++) {

				if (!hmLagerstaende.containsKey(positionen.get(i).getStuecklistepositionDto().getArtikelIId())) {

					BigDecimal bdLagerstandOhneSperrlaeger = positionen.get(i).getLagerstand()
							.subtract(positionen.get(i).getLagerstandSperrlaeger());

					if (bBeruecksichtigeInFertigung == true) {
						bdLagerstandOhneSperrlaeger = bdLagerstandOhneSperrlaeger
								.add(positionen.get(i).getInFertigung());
					}

					hmLagerstaende.put(positionen.get(i).getStuecklistepositionDto().getArtikelIId(),
							bdLagerstandOhneSperrlaeger);
				}

				BigDecimal lagerstand = hmLagerstaende
						.get(positionen.get(i).getStuecklistepositionDto().getArtikelIId());

				BigDecimal bdPositionsmenge = positionen.get(i).getStuecklistepositionDto().getNZielmenge(bdKopfmenge);
				BigDecimal bdMengeAbzueglichLagerstand = bdPositionsmenge.subtract(lagerstand);

				if (bdMengeAbzueglichLagerstand.doubleValue() <= 0) {
					// Position entfaellt, da genug auf Lager

					lagerstand = lagerstand.subtract(bdPositionsmenge);

					hmLagerstaende.put(positionen.get(i).getStuecklistepositionDto().getArtikelIId(), lagerstand);

				} else {

					lagerstand = BigDecimal.ZERO;
					hmLagerstaende.put(positionen.get(i).getStuecklistepositionDto().getArtikelIId(), lagerstand);

					positionen.get(i).getStuecklistepositionDto().setNZielmenge(bdMengeAbzueglichLagerstand);

					temp.add(positionen.get(i));

					temp.addAll(getAlStuecklisteAufgeloest_OhneLagerndeArtikelUndOhneStuecklisten(
							positionen.get(i).getPositionen(), hmLagerstaende, bBeruecksichtigeInFertigung,
							bdMengeAbzueglichLagerstand));
				}

			}
		}

		return temp;

	}

	public HashMap<String, BigDecimal> getLagerstaendeZumAufloesungszeitpunkt() {
		return lagerstaendeZumAufloesungszeitpunkt;
	}

	public void setLagerstaendeZumAufloesungszeitpunkt(
			HashMap<String, BigDecimal> lagerstaendeZumAufloesungszeitpunkt) {
		this.lagerstaendeZumAufloesungszeitpunkt = lagerstaendeZumAufloesungszeitpunkt;
	}

	public ArrayList<StuecklisteAufgeloest> getAlStuecklisteAufgeloest_OhneLagerndeArtikelMitStuecklisten(
			ArrayList<StuecklisteAufgeloest> positionen,
			HashMap<Integer, HashMap<String, BigDecimal>> hmLagerstendeAnhandLagerart, BigDecimal bdKopfmenge) {

		ArrayList<StuecklisteAufgeloest> temp = new ArrayList<StuecklisteAufgeloest>();

		if (positionen != null) {
			for (int i = 0; i < positionen.size(); i++) {

				Integer artikelIId = positionen.get(i).getStuecklistepositionDto().getArtikelIId();

				
				if (positionen.get(i).getStuecklistepositionDto().getArtikelDto().getCNr().equals("EP-E19092-A")) {
					int z = 0;
				}
				
				hmLagerstendeAnhandLagerart.get(positionen.get(i).getStuecklistepositionDto().getArtikelIId());

				HashMap<String, BigDecimal> lagerstaende = hmLagerstendeAnhandLagerart.get(artikelIId);

				positionen.get(i)
						.setLagerstaendeZumAufloesungszeitpunkt((HashMap<String, BigDecimal>) lagerstaende.clone());

				BigDecimal bdLagerstand = lagerstaende.get(LagerFac.LAGERART_NORMAL);
				bdLagerstand=bdLagerstand.add(lagerstaende.get(LagerFac.LAGERART_LIEFERANT));
				bdLagerstand=bdLagerstand.add(lagerstaende.get(LagerFac.LAGERART_HALBFERTIG));

				BigDecimal bdPositionsmenge = positionen.get(i).getStuecklistepositionDto().getNZielmenge(bdKopfmenge);
				BigDecimal bdMengeAbzueglichLagerstand = bdPositionsmenge.subtract(bdLagerstand);

				if (bdMengeAbzueglichLagerstand.doubleValue() <= 0) {
					// Position entfaellt, da genug auf Lager

					if (positionen.get(i).getStuecklistepositionDto().getArtikelDto().getCNr().equals("ST-10335-1")) {
						int z = 0;
					}

					bdLagerstand = bdLagerstand.subtract(bdPositionsmenge);

					positionen.get(i).setBdLagerstandsFehlmenge(BigDecimal.ZERO);

					lagerstaendeAbziehen(hmLagerstendeAnhandLagerart, bdPositionsmenge, artikelIId);

					if (bdKopfmenge.doubleValue() == 0) {
						positionen.get(i).getStuecklistepositionDto().setNZielmenge(BigDecimal.ZERO);
						positionen.get(i).getStuecklistepositionDto().setNMenge(BigDecimal.ZERO);
					}

					temp.add(positionen.get(i));

					temp.addAll(getAlStuecklisteAufgeloest_OhneLagerndeArtikelMitStuecklisten(
							positionen.get(i).getPositionen(), hmLagerstendeAnhandLagerart, BigDecimal.ZERO));

				} else {

					if (positionen.get(i).getStuecklistepositionDto().getArtikelDto().getCNr().equals("ST-10335-1")) {
						int z = 0;
					}

					bdLagerstand = BigDecimal.ZERO;

					positionen.get(i).setBdLagerstandsFehlmenge(bdMengeAbzueglichLagerstand);

					lagerstaendeAbziehen(hmLagerstendeAnhandLagerart, bdPositionsmenge, artikelIId);

					positionen.get(i).getStuecklistepositionDto().setNZielmenge(bdMengeAbzueglichLagerstand);

					temp.add(positionen.get(i));

					temp.addAll(getAlStuecklisteAufgeloest_OhneLagerndeArtikelMitStuecklisten(
							positionen.get(i).getPositionen(), hmLagerstendeAnhandLagerart,
							bdMengeAbzueglichLagerstand));
				}

			}
		}

		return temp;

	}

	private String TOKEN_GESAMTLAGERSTAND = "GESAMTLAGERSTAND";

	private void lagerstaendeAbziehen(HashMap<Integer, HashMap<String, BigDecimal>> hmLagerstendeAllerArtikel,
			BigDecimal offeneMenge, Integer artikelIId) {

		// Zuerst Hauptlager (=Hauptlager+Normallager) dann
		// Lieferantenlager dann Halbfertiglager

		// Wenn nichts mehr auf Lager, dann Fehlmenge anzeigen

		HashMap<String, BigDecimal> hmLagerstaendeAllerLagerarten = hmLagerstendeAllerArtikel.get(artikelIId);

		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandNormal = hmLagerstaendeAllerLagerarten.get(LagerFac.LAGERART_NORMAL);

			if (offeneMenge.doubleValue() > lagerstandNormal.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_NORMAL, new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(lagerstandNormal));
				offeneMenge = offeneMenge.subtract(lagerstandNormal);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_NORMAL, lagerstandNormal.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}
		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandLieferantenlager = hmLagerstaendeAllerLagerarten.get(LagerFac.LAGERART_LIEFERANT);

			if (offeneMenge.doubleValue() > lagerstandLieferantenlager.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_LIEFERANT, new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(lagerstandLieferantenlager));
				offeneMenge = offeneMenge.subtract(lagerstandLieferantenlager);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_LIEFERANT,
						lagerstandLieferantenlager.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}

		if (offeneMenge.doubleValue() > 0) {
			// Zuerst von Normallager holen
			BigDecimal lagerstandHalbfertig = hmLagerstaendeAllerLagerarten.get(LagerFac.LAGERART_HALBFERTIG);

			if (offeneMenge.doubleValue() > lagerstandHalbfertig.doubleValue()) {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_HALBFERTIG, new BigDecimal(0));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(lagerstandHalbfertig));
				offeneMenge = offeneMenge.subtract(lagerstandHalbfertig);
			} else {
				hmLagerstaendeAllerLagerarten.put(LagerFac.LAGERART_HALBFERTIG,
						lagerstandHalbfertig.subtract(offeneMenge));
				hmLagerstaendeAllerLagerarten.put(TOKEN_GESAMTLAGERSTAND,
						hmLagerstaendeAllerLagerarten.get(TOKEN_GESAMTLAGERSTAND).subtract(offeneMenge));
				offeneMenge = new BigDecimal(0);
			}

		}

		hmLagerstendeAllerArtikel.put(artikelIId, hmLagerstaendeAllerLagerarten);
	}

	public ArrayList<StuecklisteAufgeloest> getAlStuecklisteAufgeloest() {

		ArrayList<StuecklisteAufgeloest> al = new ArrayList<StuecklisteAufgeloest>();

		al.addAll(getAlStuecklisteAufgeloest(this.getPositionen()));

		return al;

	}

	private ArrayList<StuecklisteAufgeloest> getAlStuecklisteAufgeloest(ArrayList<StuecklisteAufgeloest> positionen) {

		ArrayList<StuecklisteAufgeloest> temp = new ArrayList<StuecklisteAufgeloest>();

		if (positionen != null) {

			for (int i = 0; i < positionen.size(); i++) {

				temp.add(positionen.get(i));

				temp.addAll(getAlStuecklisteAufgeloest(positionen.get(i).getPositionen()));

			}
		}

		return temp;

	}

	private int getAnzahlPositionen(ArrayList<StuecklisteAufgeloest> positionen) {
		int x = 0;
		if (positionen != null) {
			x += positionen.size();
			for (int i = 0; i < positionen.size(); i++) {
				x += getAnzahlPositionen(positionen.get(i).getPositionen());
			}
		}
		return x;

	}

	public void setVorgaenger(StuecklisteAufgeloest vorgaenger) {
		this.vorgaenger = vorgaenger;
	}

	private ArrayList<StuecklisteAufgeloest> positionen = new ArrayList<StuecklisteAufgeloest>();

	private ArrayList<StuecklistearbeitsplanDto> arbeitsplanPositionen = new ArrayList<StuecklistearbeitsplanDto>();

	public ArrayList<StuecklistearbeitsplanDto> getArbeitsplanPositionen() {
		return arbeitsplanPositionen;
	}

	public ArrayList<StuecklistearbeitsplanDto> getArbeitsplanPositionenGesplittedInMannUndMaschine() {

		ArrayList<StuecklistearbeitsplanDto> gesplitted = new ArrayList<StuecklistearbeitsplanDto>();
		for (int i = 0; i < arbeitsplanPositionen.size(); i++) {

			StuecklistearbeitsplanDto posDto = arbeitsplanPositionen.get(0);

			if (posDto.getMaschineIId() == null || Helper.short2boolean(posDto.getBNurmaschinenzeit()) == false) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanDto.setIId(posDto.getIId());
				stuecklistearbeitsplanDto.setArtikelIId(posDto.getArtikelIId());
				stuecklistearbeitsplanDto.setCKommentar(posDto.getCKommentar());
				stuecklistearbeitsplanDto.setLRuestzeit(posDto.getLRuestzeit());
				stuecklistearbeitsplanDto.setBNurmaschinenzeit(posDto.getBNurmaschinenzeit());
				stuecklistearbeitsplanDto.setIAufspannung(posDto.getIAufspannung());
				stuecklistearbeitsplanDto.setXFormel(posDto.getXFormel());
				stuecklistearbeitsplanDto.setLStueckzeit(posDto.getLStueckzeit());
				stuecklistearbeitsplanDto.setIArbeitsgang(posDto.getIArbeitsgang());
				stuecklistearbeitsplanDto.setIUnterarbeitsgang(posDto.getIUnterarbeitsgang());

				gesplitted.add(stuecklistearbeitsplanDto);

			}
			// Wenn Maschine, dann zusaetzlichen Eintrag erstellen:
			if (posDto.getMaschineIId() != null) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanDto.setIId(posDto.getIId());
				stuecklistearbeitsplanDto.setArtikelIId(posDto.getArtikelIId());
				stuecklistearbeitsplanDto.setCKommentar(posDto.getCKommentar());
				stuecklistearbeitsplanDto.setLRuestzeit(posDto.getLRuestzeit());
				stuecklistearbeitsplanDto.setBNurmaschinenzeit(posDto.getBNurmaschinenzeit());
				stuecklistearbeitsplanDto.setIAufspannung(posDto.getIAufspannung());
				stuecklistearbeitsplanDto.setXFormel(posDto.getXFormel());
				stuecklistearbeitsplanDto.setLStueckzeit(posDto.getLStueckzeit());
				stuecklistearbeitsplanDto.setIArbeitsgang(posDto.getIArbeitsgang());
				stuecklistearbeitsplanDto.setIUnterarbeitsgang(posDto.getIUnterarbeitsgang());

				stuecklistearbeitsplanDto.setMaschineIId(posDto.getMaschineIId());

				gesplitted.add(stuecklistearbeitsplanDto);

			}

		}

		return gesplitted;
	}

	public void setArbeitsplanPositionen(ArrayList<StuecklistearbeitsplanDto> arbeitsplanPositionen) {
		this.arbeitsplanPositionen = arbeitsplanPositionen;
	}

	private StuecklisteAufgeloest vorgaenger = null;

	public StuecklisteAufgeloest clone() {
		StuecklisteAufgeloest klon = new StuecklisteAufgeloest(this.getStuecklistepositionDto());

		klon.arbeitsplanPositionen = this.arbeitsplanPositionen;
		klon.setStuecklisteDto(this.getStuecklisteDto());

		klon.setStuecklistepositionDto(this.getStuecklistepositionDto());

		return klon;
	}

	public StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	public StuecklisteAufgeloest(StuecklistepositionDto stuecklistepositionDto) {

		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	public StuecklistepositionDto getStuecklistepositionDto() {
		return stuecklistepositionDto;
	}

	public int getIEbene() {
		int iEbene = -1;
		StuecklisteAufgeloest vg = getVorgaenger();
		while (vg != null) {
			vg = vg.getVorgaenger();
			iEbene++;
		}

		return iEbene;
	}

	public void setStuecklistepositionDto(StuecklistepositionDto stuecklistepositionDto) {
		this.stuecklistepositionDto = stuecklistepositionDto;
	}

	public ArrayList<StuecklisteAufgeloest> getPositionen() {
		return positionen;
	}

	public void setPositionen(ArrayList<StuecklisteAufgeloest> positionen) {
		this.positionen = positionen;
	}
}
