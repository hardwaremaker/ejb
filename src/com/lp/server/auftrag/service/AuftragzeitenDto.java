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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.util.EJBExceptionLP;

public class AuftragzeitenDto extends TabelleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String maschinengruppe;

	public String getMaschinengruppe() {
		return maschinengruppe;
	}

	private boolean bTelefonzeit = false;

	public boolean isBTelefonzeit() {
		return bTelefonzeit;
	}

	public void setBTelefonzeit(boolean bTelefonzeit) {
		this.bTelefonzeit = bTelefonzeit;
	}

	private Integer iArbeitsgang = null;
	private Integer iUnterarbeitsgang = null;

	public Integer getiArbeitsgang() {
		return iArbeitsgang;
	}

	public void setiArbeitsgang(Integer iArbeitsgang) {
		this.iArbeitsgang = iArbeitsgang;
	}

	public Integer getiUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}

	public void setiUnterarbeitsgang(Integer iUnterarbeitsgang) {
		this.iUnterarbeitsgang = iUnterarbeitsgang;
	}

	public void setMaschinengruppe(String maschinengruppe) {
		this.maschinengruppe = maschinengruppe;
	}

	private String sPersonalKurzzeichen;

	public String getSPersonalKurzzeichen() {
		return sPersonalKurzzeichen;
	}

	public void setSPersonalKurzzeichen(String sPersonalKurzzeichen) {
		this.sPersonalKurzzeichen = sPersonalKurzzeichen;
	}

	private String sPersonalnummer;
	private String sPersonalMaschinenname;
	private String sPersonNachnameVorname = "";

	public String getsPersonNachnameVorname() {
		return sPersonNachnameVorname;
	}

	public void setsPersonNachnameVorname(String sPersonNachnameVorname) {

		this.sPersonNachnameVorname = sPersonNachnameVorname;
	}

	private String sArtikelcnr;
	private String sArtikelbezeichnung;
	private String sArtikelzusatzbezeichnung;

	public String getSArtikelzusatzbezeichnung() {
		return sArtikelzusatzbezeichnung;
	}

	public void setSArtikelzusatzbezeichnung(String sArtikelzusatzbezeichnung) {
		this.sArtikelzusatzbezeichnung = sArtikelzusatzbezeichnung;
	}

	private String sKommentarIntern;

	public String getSKommentarIntern() {
		return sKommentarIntern;
	}

	public void setSKommentarIntern(String sKommentarIntern) {
		this.sKommentarIntern = sKommentarIntern;
	}

	private String sZeitbuchungtext;
	private String sKommentar;
	private Integer zeitdatenIIdBelegbuchung;

	public Integer getZeitdatenIIdBelegbuchung() {
		return zeitdatenIIdBelegbuchung;
	}

	public void setZeitdatenIIdBelegbuchung(Integer zeitdatenIIdBelegbuchung) {
		this.zeitdatenIIdBelegbuchung = zeitdatenIIdBelegbuchung;
	}

	public String getSKommentar() {
		return sKommentar;
	}

	public void setSKommentar(String kommentar) {
		sKommentar = kommentar;
	}

	private Timestamp tsErledigt;
	private Double fVerrechenbarInProzent;
	private String sPersonalKurzzeichenErledigt;

	public Timestamp getTsErledigt() {
		return tsErledigt;
	}

	public void setTsErledigt(Timestamp tsErledigt) {
		this.tsErledigt = tsErledigt;
	}

	public Double getFVerrechenbarInProzent() {
		return fVerrechenbarInProzent;
	}

	public void setFVerrechenbarInProzent(Double fVerrechenbarInProzent) {
		this.fVerrechenbarInProzent = fVerrechenbarInProzent;
	}

	public String getSPersonalKurzzeichenErledigt() {
		return sPersonalKurzzeichenErledigt;
	}

	public void setSPersonalKurzzeichenErledigt(String sPersonalKurzzeichenErledigt) {
		this.sPersonalKurzzeichenErledigt = sPersonalKurzzeichenErledigt;
	}

	private Integer artikelIId;
	private Timestamp tsBeginn;
	private Timestamp tsEnde;
	private String sBewegungsart;
	private Double ddDauer;
	private Time tDauer;
	private BigDecimal bdKosten;
	private Integer artikelgruppeIId;
	private Integer artikelklasseIId;
	private Integer iPersonalMaschinenId;
	private Integer belegpositionIId;
	private String sArtikelgruppe;
	private String sQuelle;

	public String getSQuelle() {
		return sQuelle;
	}

	public void setSQuelle(String sQuelle) {
		this.sQuelle = sQuelle;
	}

	public String getSArtikelgruppe() {
		return sArtikelgruppe;
	}

	public void setSArtikelgruppe(String sArtikelgruppe) {
		this.sArtikelgruppe = sArtikelgruppe;
	}

	public Integer getBelegpositionIId() {
		return belegpositionIId;
	}

	public void setBelegpositionIId(Integer belegpositionIId) {
		this.belegpositionIId = belegpositionIId;
	}

	public String getSPersonalnummer() {
		return sPersonalnummer;
	}

	public void setSPersonalnummer(String sPersonalnummer) {
		this.sPersonalnummer = sPersonalnummer;
	}

	public String getSPersonalMaschinenname() {
		return sPersonalMaschinenname;
	}

	public void setSPersonalMaschinenname(String sPersonalMaschinenname) {
		this.sPersonalMaschinenname = sPersonalMaschinenname;
	}

	public String getSArtikelcnr() {
		return sArtikelcnr;
	}

	public void setSArtikelcnr(String sArtikelcnr) {
		this.sArtikelcnr = sArtikelcnr;
	}

	public String getSArtikelbezeichnung() {
		return sArtikelbezeichnung;
	}

	public void setSArtikelbezeichnung(String sArtikelbezeichnung) {
		this.sArtikelbezeichnung = sArtikelbezeichnung;
	}

	public String getSZeitbuchungtext() {
		return sZeitbuchungtext;
	}

	public void setSZeitbuchungtext(String sZeitbuchungtext) {
		this.sZeitbuchungtext = sZeitbuchungtext;
	}

	public Timestamp getTsBeginn() {
		return tsBeginn;
	}

	public void setTsBeginn(Timestamp tsBeginn) {
		this.tsBeginn = tsBeginn;
	}

	public Double getDdDauer() {
		return ddDauer;
	}

	public void setDdDauer(Double ddDauer) {
		this.ddDauer = ddDauer;
	}

	public BigDecimal getBdKosten() {
		return bdKosten;
	}

	public Timestamp getTsEnde() {
		return tsEnde;
	}

	public String getSBewegungsart() {
		return sBewegungsart;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public Time getTDauer() {
		return tDauer;
	}

	public Integer getArtikelgruppeIId() {
		return artikelgruppeIId;
	}

	public Integer getArtikelklasseIId() {
		return artikelklasseIId;
	}

	public Integer getIPersonalMaschinenId() {
		return iPersonalMaschinenId;
	}

	public void setSBewegungsart(String sBewegungsart) {
		this.sBewegungsart = sBewegungsart;
	}

	public void setBdKosten(BigDecimal bdKosten) {
		this.bdKosten = bdKosten;
	}

	public void setTsEnde(Timestamp tsEnde) {
		this.tsEnde = tsEnde;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public void setTDauer(Time tDauer) {
		this.tDauer = tDauer;
	}

	public void setArtikelgruppeIId(Integer artikelgruppeIId) {
		this.artikelgruppeIId = artikelgruppeIId;
	}

	public void setArtikelklasseIId(Integer artikelklasseIId) {
		this.artikelklasseIId = artikelklasseIId;
	}

	public void setIPersonalMaschinenId(Integer iPersonalMaschinenId) {
		this.iPersonalMaschinenId = iPersonalMaschinenId;
	}

	public static AuftragzeitenDto[] add2BelegzeitenDtos(ArrayList<AuftragzeitenDto> alDaten,
			AuftragzeitenDto[] vorhandenDtos) {
		if (alDaten != null && alDaten.size() > 0) {
			ArrayList alnew = new ArrayList<AuftragzeitenDto>();
			for (int i = 0; i < vorhandenDtos.length; i++) {
				alnew.add(vorhandenDtos[i]);
			}

			for (int i = 0; i < alDaten.size(); i++) {
				alnew.add(alDaten.get(i));
			}

			AuftragzeitenDto[] azDtos = new AuftragzeitenDto[alnew.size()];
			return (AuftragzeitenDto[]) alnew.toArray(azDtos);
		} else {
			return vorhandenDtos;
		}

	}

	public static AuftragzeitenDto[] sortiereBelegzeitDtos(AuftragzeitenDto[] azDtos, int iSortierung) {

		try {
			if (iSortierung == ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL) {

				for (int i = azDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						if (azDtos[j].getSArtikelcnr().compareTo(azDtos[j + 1].getSArtikelcnr()) > 0) {
							AuftragzeitenDto tauschDto = azDtos[j];
							azDtos[j] = azDtos[j + 1];
							azDtos[j + 1] = tauschDto;
						}
					}
				}
			} else if (iSortierung == ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL) {
				for (int i = azDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						if (azDtos[j].getsPersonNachnameVorname()
								.compareTo(azDtos[j + 1].getsPersonNachnameVorname()) > 0) {
							AuftragzeitenDto tauschDto = azDtos[j];
							azDtos[j] = azDtos[j + 1];
							azDtos[j + 1] = tauschDto;
						} else if (azDtos[j].getsPersonNachnameVorname()
								.compareTo(azDtos[j + 1].getsPersonNachnameVorname()) == 0) {
							if (azDtos[j].getTsBeginn().before(azDtos[j + 1].getTsBeginn())) {
								AuftragzeitenDto tauschDto = azDtos[j];
								azDtos[j] = azDtos[j + 1];
								azDtos[j + 1] = tauschDto;
							}
						}
					}
				}
			} else if (iSortierung == ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ZEITPUNKT_PERSONAL) {
				for (int i = azDtos.length - 1; i > 0; --i) {
					for (int j = 0; j < i; ++j) {
						if (azDtos[j].getTsBeginn().before(azDtos[j + 1].getTsBeginn())) {
							AuftragzeitenDto tauschDto = azDtos[j];
							azDtos[j] = azDtos[j + 1];
							azDtos[j + 1] = tauschDto;
						} else if (azDtos[j].getTsBeginn().equals(azDtos[j + 1].getTsBeginn())) {
							if (azDtos[j].getsPersonNachnameVorname()
									.compareTo(azDtos[j + 1].getsPersonNachnameVorname()) > 0) {
								AuftragzeitenDto tauschDto = azDtos[j];
								azDtos[j] = azDtos[j + 1];
								azDtos[j + 1] = tauschDto;
							}
						}
					}
				}
			}

		} catch (NullPointerException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IN_ZEITDATEN, ex3);
		}

		return azDtos;
	}

	public static AuftragzeitenDto clone(AuftragzeitenDto orig) {
		AuftragzeitenDto klon = new AuftragzeitenDto();
		klon.setArtikelgruppeIId(orig.getArtikelgruppeIId());
		klon.setArtikelIId(orig.getArtikelIId());
		klon.setArtikelklasseIId(orig.getArtikelklasseIId());
		klon.setBdKosten(orig.getBdKosten());
		klon.setBelegpositionIId(orig.getBelegpositionIId());
		klon.setBTelefonzeit(orig.isBTelefonzeit());
		klon.setDdDauer(orig.getDdDauer());
		klon.setFVerrechenbarInProzent(orig.getFVerrechenbarInProzent());
		klon.setiArbeitsgang(orig.getiArbeitsgang());
		klon.setIPersonalMaschinenId(orig.getIPersonalMaschinenId());
		klon.setiUnterarbeitsgang(orig.getiUnterarbeitsgang());
		klon.setMaschinengruppe(orig.getMaschinengruppe());
		klon.setSArtikelbezeichnung(orig.getSArtikelbezeichnung());
		klon.setSArtikelcnr(orig.getSArtikelcnr());
		klon.setSArtikelgruppe(orig.getSArtikelgruppe());
		klon.setSArtikelzusatzbezeichnung(orig.getSArtikelzusatzbezeichnung());
		klon.setSBewegungsart(orig.getSBewegungsart());
		klon.setSKommentar(orig.getSKommentar());
		klon.setSKommentarIntern(orig.getSKommentarIntern());
		klon.setSPersonalKurzzeichen(orig.getSPersonalKurzzeichen());

		klon.setSPersonalKurzzeichenErledigt(orig.getSPersonalKurzzeichenErledigt());
		klon.setSPersonalMaschinenname(orig.getSPersonalMaschinenname());
		klon.setSPersonalnummer(orig.getSPersonalnummer());
		klon.setsPersonNachnameVorname(orig.getsPersonNachnameVorname());
		klon.setSPersonalKurzzeichen(orig.getSPersonalKurzzeichen());

		klon.setSQuelle(orig.getSQuelle());

		klon.setSZeilenheader(orig.getSZeilenHeader());
		klon.setSZeitbuchungtext(orig.getSZeitbuchungtext());
		klon.setTDauer(orig.getTDauer());
		klon.setTsBeginn(orig.getTsBeginn());
		klon.setTsEnde(orig.getTsEnde());
		klon.setTsErledigt(orig.getTsErledigt());
		klon.setZeitdatenIIdBelegbuchung(orig.getZeitdatenIIdBelegbuchung());

		return klon;
	}
}
