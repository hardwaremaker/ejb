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
package com.lp.server.finanz.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuExportFormatterDatev extends FibuExportFormatter {
	/*
	 * ACHTUNG: lt. WH 14.03.06 Zahlungszieltage <3 Tage = Mahnsperre mit Stufe
	 * 1-3 99 Tage = Sofort faellig Skonto: Wenn kein Skonto (=0%) dann 99%
	 * eintragen
	 */

	private final static String P_UMSATZ = "umsatz";						// Soll/Haben Betrag
	private final static String P_BU_SCHLUESSEL = "bu-schluessel";			// ER, AR GS...
	private final static String P_GEGENKONTO = "gegenkonto";				// Gegenkonto
	private final static String P_BELEGFELD1 = "belegfeld1";				// Belegnummer
	private final static String P_BELEGFELD2 = "belegfeld2";				// <leer>
	private final static String P_BELEGDAT = "datum";						// Belegdatum
	private final static String P_KONTO = "konto";							// Konto
	private final static String P_KOSTFELD1 = "kostfeld1";					// <leer>
	private final static String P_KOSTFELD2 = "kostfeld2";					// <leer>
	private final static String P_KOSTMENGE = "kostmenge";					// <leer>
	private final static String P_SKONTO = "skonto";						// <leer>
	private final static String P_TEXT = "buchungstext";					// Text
	private final static String P_USTID = "ustid";							// UID Nummer
	private final static String P_EU_STEUERSATZ = "eu-steuersatz";			// <leer>
	private final static String P_WAEHRUNGSKENNUNG = "waehrungskennung";	// Mandantenwaehrung KZ
	private final static String P_BASISWAEHRUNGSBETRAG = "basiswaehrungsbetrag";	// <leer>
	private final static String P_BASISWAEHRUNGSKENNUNG = "basiswaehrungskennung";	// <leer>
	private final static String P_KURS = "kurs";							// <leer>
	private final static String P_EXT_RECHNUNGNR = "extrechnung";			// bei ER die Lieferanten Rechnungsnummer
	private final static String P_FAELLIGKEIT = "faelligkeit";				// Datum der Faelligkeit
	private final static String P_PARTNER_KBZ = "partnerkbz";				// Partner Kurzbezeichnung
	private final static String P_BESTELLNUMMER = "bestellnummer";			// (optionale) Bestellnummer
	private final static String P_AUFTRAGSNUMMER = "auftragsnummer";		// (optionale) AB-Nummer
	private final static String P_BUCHUNGSTYP = "buchungstyp";				// Buchungstyp
	
	public class Buchungstyp {
		public static final String AR_ANZAHLUNG = "AA";
		public static final String AR_SCHLUSSRECHNUNG = "SR";
	}
	
	private static final String STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER = "19";
	private static final String STEUCOD_EINKAUF_REVERSE_CHARGE = "94";
	private static final String STEUCOD_REVERSE_CHARGE = "94";
	
	private List<String> kontoklassenOhneSteuercode = new ArrayList<String>();
	
	protected FibuExportFormatterDatev(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super(exportKriterienDto, theClientDto);
		
		kontoklassenOhneSteuercode = getParameterFac().getExportDatevKontoklassenOhneBuSchluessel(theClientDto.getMandant());
	}

	protected String exportiereDaten(FibuexportDto[] fibuExportDtos)
			throws EJBExceptionLP {
		String sExportDaten = "";
		try {
			if (fibuExportDtos != null) {
				Integer finanzamtPartnerIId = pruefeFinanzamt(fibuExportDtos);
				// UST-LKZ des Partners bestimmen
				final String sPartnerUstLkz = getPartnerUstLkz(fibuExportDtos);
				// Zuerst: Richtigen UST-Prozentsatz und Finanzamt bestimmen.
				PartnerDto partnerDtoFinanzamt = null;
				// FA holen, wenn eines zugeordnet werden konnte.
				if (finanzamtPartnerIId != null) {
					partnerDtoFinanzamt = super
							.getFinanzamtPartner(finanzamtPartnerIId);
				}
				boolean bFinanzamtImPartnerLand = isFinanzamtImPartnerland(
						sPartnerUstLkz, partnerDtoFinanzamt);
				// feststellen, ob es sich um IG Erwerb / Erloes handelt
				boolean bInnerGemeinschaftlich = isInnergemeinschaftlich(
						fibuExportDtos, bFinanzamtImPartnerLand);
				// Trennung zwischen ER und AR bzw. GS
				// derzeit keine Trennung
				if (fibuExportDtos[0].getBelegart().equals(
						FibuExportManager.BELEGART_ER)) {
					sExportDaten = exportiereDaten(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				} else {
					sExportDaten = exportiereDaten(fibuExportDtos,
							partnerDtoFinanzamt, bInnerGemeinschaftlich,
							sPartnerUstLkz);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExportDaten;
	}

	/**
	 * exportiereDatenAR
	 * 
	 * @param fibuExportDtos
	 *            FibuexportDto[]
	 * @param partnerDtoFinanzamt
	 *            PartnerDto
	 * @param bInnerGemeinschaftlich
	 *            boolean
	 * @param sPartnerUstLkz
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	private String exportiereDaten(final FibuexportDto[] fibuExportDtos,
			final PartnerDto partnerDtoFinanzamt,
			final boolean bInnerGemeinschaftlich, final String sPartnerUstLkz)
			throws EJBExceptionLP, RemoteException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fibuExportDtos.length; i++) {
			if ((fibuExportDtos[0].getBelegart().equals("ER") && fibuExportDtos[i].getHabenbetragBD() != null) ||
					(!fibuExportDtos[0].getBelegart().equals("ER") && fibuExportDtos[i].getSollbetragBD() != null)) {
				// skip it
			} else {
				LpMailText mt = new LpMailText();
				boolean swapKonten = false;
				ExportdatenAdapter exportdatenAdapter = new ExportdatenAdapter(fibuExportDtos[i]);
				if (isFremdwaehrung(fibuExportDtos[i])) {
					exportdatenAdapter.asFremdwaehrung();
				}
				//--------------------------------------------------------------
				// Betrag
				if (fibuExportDtos[0].getBelegart().equals("ER")) {
					if (exportdatenAdapter.getHabenbetrag() != null) {
						swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(exportdatenAdapter.getHabenbetrag(), 12, 2));
					} else {
						if (bInnerGemeinschaftlich)
							mt.addParameter(P_UMSATZ, formatBetrag(exportdatenAdapter.getSollbetrag(), 12, 2));
						else
							mt.addParameter(P_UMSATZ, formatBetrag(exportdatenAdapter.getSollbetrag()
								.add(exportdatenAdapter.getSteuerbetrag()), 12, 2));
					}
				} else {
					if (exportdatenAdapter.getSollbetrag() != null) {
						if (exportdatenAdapter.getSollbetrag().signum() != -1)
							swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(exportdatenAdapter.getSollbetrag().abs(), 12, 2));
					} else {
						if (exportdatenAdapter.getHabenbetrag().signum() == -1)
							swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(exportdatenAdapter.getHabenbetrag()
								.add(exportdatenAdapter.getSteuerbetrag()).abs(), 12, 2));
					}
				}
				//--------------------------------------------------------------
				// BU-Schluessel
				
//				String sBUcode = "";
//				// Eingangsrechnung
//				if (fibuExportDtos[i].getBelegart().equals(FibuExportManager.BELEGART_ER)) {
//					if (bInnerGemeinschaftlich) {
//						if (fibuExportDtos[i].isBReverseCharge())
//							sBUcode = STEUCOD_EINKAUF_REVERSE_CHARGE;
//						else
//							sBUcode = STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER;
//					}
//				} else {
//					// Rechnung oder Gutschrift
//					if (fibuExportDtos[i].getBelegart().equals(
//							FibuExportManager.BELEGART_AR) || fibuExportDtos[i].getBelegart().equals(
//									FibuExportManager.BELEGART_GS)) {
//						if (bInnerGemeinschaftlich) {
//							if (fibuExportDtos[i].isBReverseCharge())
//								sBUcode = STEUCOD_REVERSE_CHARGE;
//						}
//					}
//				}

				mt.addParameter(P_BU_SCHLUESSEL, getSteuercode(fibuExportDtos[i]));
				
				//--------------------------------------------------------------
				// Gegenkonto
				if (swapKonten) {
					if (fibuExportDtos[i].getKontoDto() != null) {
						mt.addParameter(P_GEGENKONTO, formatKontonummer(fibuExportDtos[i].getKontoDto().getCNr()));
					} else {
						mt.addParameter(P_GEGENKONTO, "");
					}
				} else {
					if (fibuExportDtos[i].getGegenkontoDto() != null) {
						mt.addParameter(P_GEGENKONTO, formatKontonummer(fibuExportDtos[i].getGegenkontoDto().getCNr()));
					} else {
						mt.addParameter(P_GEGENKONTO, "");
					}
				}
				//--------------------------------------------------------------
				// Belegnummer
				mt.addParameter(P_BELEGFELD1, fibuExportDtos[i].getBelegnummer());
				//--------------------------------------------------------------
				// Belegfeld2
				mt.addParameter(P_BELEGFELD2, fibuExportDtos[i].getBelegart());
				//--------------------------------------------------------------
				// Belegdatum
				mt.addParameter(P_BELEGDAT, formatDatum(fibuExportDtos[i].getBelegdatum()));
				//--------------------------------------------------------------
				// Konto
				if (swapKonten) {
					if (fibuExportDtos[i].getGegenkontoDto() != null) {
						mt.addParameter(P_KONTO, formatKontonummer(fibuExportDtos[i].getGegenkontoDto().getCNr()));
					} else {
						mt.addParameter(P_KONTO, "");
					}
				} else {
					mt.addParameter(P_KONTO, formatKontonummer(fibuExportDtos[i].getKontoDto().getCNr()));
				}
				//--------------------------------------------------------------
				// Kostfeld1
				mt.addParameter(P_KOSTFELD1, "");
				//--------------------------------------------------------------
				// Kostfeld2
				mt.addParameter(P_KOSTFELD2, "");
				//--------------------------------------------------------------
				// Kostmenge
				mt.addParameter(P_KOSTMENGE, "");
				//--------------------------------------------------------------
				// Skonto
				mt.addParameter(P_SKONTO, "");
				//--------------------------------------------------------------
				// Buchungstext
				mt.addParameter(P_TEXT, fibuExportDtos[i].getText());
				//--------------------------------------------------------------
				// UID
				mt.addParameter(P_USTID, fibuExportDtos[i].getUidNummer());
				//--------------------------------------------------------------
				// EU-Steuersatz
				mt.addParameter(P_EU_STEUERSATZ, "");
				//--------------------------------------------------------------
				// Waehrungskennung
				mt.addParameter(P_WAEHRUNGSKENNUNG, exportdatenAdapter.getWaehrungskennung());
				//--------------------------------------------------------------
				// Basiswaehrungsbetrag
				mt.addParameter(P_BASISWAEHRUNGSBETRAG, "");
				//--------------------------------------------------------------
				// Basiswaehrungskennung
				mt.addParameter(P_BASISWAEHRUNGSKENNUNG, "");
				//--------------------------------------------------------------
				// Kurs
				mt.addParameter(P_KURS, 
						exportdatenAdapter.isFremdwaehrung() ? formatBetrag(exportdatenAdapter.getKurs(), 4, 6) : "");
				//--------------------------------------------------------------
				// Lieferantenrechnungsnummer
				mt.addParameter(P_EXT_RECHNUNGNR, fibuExportDtos[i].getSExterneBelegnummer());
	
				//--------------------------------------------------------------
				// Faelligkeitsdatum
				mt.addParameter(P_FAELLIGKEIT, formatDatumTagMonatJahr(fibuExportDtos[i].getFaelligkeitsdatum()));

				mt.addParameter(P_PARTNER_KBZ, fibuExportDtos[i].getPartnerDto().getCKbez());
				mt.addParameter(P_AUFTRAGSNUMMER, exportdatenAdapter.getAuftragsnummer());
				mt.addParameter(P_BUCHUNGSTYP, exportdatenAdapter.getBuchungstyp());
				mt.addParameter(P_BESTELLNUMMER, fibuExportDtos[i].getBestellnummer());
				
				//--------------------------------------------------------------
				// exportieren
				//--------------------------------------------------------------
				String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
						theClientDto.getMandant(), getXSLFile(), theClientDto
								.getLocMandant(), theClientDto);
				sb.append(sZeile).append(Helper.CR_LF);
			}
		}
		return sb.toString();
	}

	protected String getXSLFile() {
		return XSL_FILE_DATEV;
	}

	protected String exportiereUeberschrift() throws EJBExceptionLP {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_UMSATZ, P_UMSATZ);
		mt.addParameter(P_BU_SCHLUESSEL, P_BU_SCHLUESSEL);
		mt.addParameter(P_GEGENKONTO, P_GEGENKONTO);
		mt.addParameter(P_BELEGFELD1, P_BELEGFELD1);
		mt.addParameter(P_BELEGFELD2, P_BELEGFELD2);
		mt.addParameter(P_BELEGDAT, P_BELEGDAT);
		mt.addParameter(P_KONTO, P_KONTO);
		mt.addParameter(P_KOSTFELD1, P_KOSTFELD1);
		mt.addParameter(P_KOSTFELD2, P_KOSTFELD2);
		mt.addParameter(P_KOSTMENGE, P_KOSTMENGE);
		mt.addParameter(P_SKONTO, P_SKONTO);
		mt.addParameter(P_TEXT, P_TEXT);
		mt.addParameter(P_USTID, P_USTID);
		mt.addParameter(P_EU_STEUERSATZ, P_EU_STEUERSATZ);
		mt.addParameter(P_WAEHRUNGSKENNUNG, P_WAEHRUNGSKENNUNG);
		mt.addParameter(P_BASISWAEHRUNGSBETRAG, P_BASISWAEHRUNGSBETRAG);
		mt.addParameter(P_BASISWAEHRUNGSKENNUNG, P_BASISWAEHRUNGSKENNUNG);
		mt.addParameter(P_KURS, P_KURS);
		mt.addParameter(P_EXT_RECHNUNGNR, P_EXT_RECHNUNGNR);
		mt.addParameter(P_FAELLIGKEIT, P_FAELLIGKEIT);
		mt.addParameter(P_PARTNER_KBZ, P_PARTNER_KBZ);
		mt.addParameter(P_AUFTRAGSNUMMER, P_AUFTRAGSNUMMER);
		mt.addParameter(P_BUCHUNGSTYP, P_BUCHUNGSTYP);
		mt.addParameter(P_BESTELLNUMMER, P_BESTELLNUMMER);

		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}

	/**
	 * Eine Kontonummer fuer BMD formatieren. Die Kontonummer kann neunstellig
	 * erfasst werden. Fehlende Stellen k&ouml;nnen mit fuehrenden Nullen (zB
	 * 000202001) ergaenzt werden.
	 * 
	 * @param sKontonummer
	 *            Kontonummer
	 * @return String
	 */
	private static String formatKontonummer(String sKontonummer) {
		Integer iKontonummer = new Integer(sKontonummer);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMaximumIntegerDigits(9);
		//nf.setMinimumIntegerDigits(9);
		nf.setGroupingUsed(false);
		return nf.format(iKontonummer.intValue());
	}

	/**
	 * Eine Belegnummer fuer BMD formatieren. Belegnummer (zB 000000135 =
	 * Belegnummer 135).
	 * 
	 * @param sBelegnummer
	 *            Belegnummer
	 * @return String
	 */
	private static String formatBelegnummer(String sBelegnummer) {
		Integer iBelegnummer = new Integer(sBelegnummer);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMaximumIntegerDigits(9);
		nf.setMinimumIntegerDigits(9);
		return nf.format(iBelegnummer.intValue());
	}

	/**
	 * Eine Kostenstellennummer fuer BMD formatieren. Belegnummer (zB 000000135
	 * = Belegnummer 135).
	 * 
	 * @param sKSTnummer
	 *            Nummer der Kostenstelle
	 * @return String
	 */
	private static String formatKostenstelle(String sKSTnummer) {
		Integer iKSTnummer = new Integer(sKSTnummer);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMaximumIntegerDigits(9);
		nf.setMinimumIntegerDigits(9);
		nf.setGroupingUsed(false);
		return nf.format(iKSTnummer.intValue());
	}

	/**
	 * Datum fuer BMD formatieren. Hier kann das Buchungsdatum im Format
	 * JJJJMMTT (zB 19980501) angegeben werden.
	 * 
	 * @param d
	 *            Date
	 * @return String
	 */
	private static String formatDatum(java.util.Date d) {
		if (d != null) {
			DateFormat df = new SimpleDateFormat("ddMM");
			return df.format(d);
		} else {
			return "";
		}
	}
	
	private static String formatDatumTagMonatJahr(Date datum) {
		if (datum == null) return "";
		
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		return dateFormat.format(datum);
	}

	/**
	 * Betrag fuer Datev formatieren.
	 * 
	 * @param nBetrag
	 *            Betrag
	 * @param iVorkommastellen
	 *            int
	 * @param iNachkommastellen
	 *            int
	 * @return String
	 */
	private static String formatBetrag(BigDecimal nBetrag, int iVorkommastellen,
			int iNachkommastellen) {
		if (nBetrag != null) {
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setGroupingUsed(false);
			df.setDecimalSeparatorAlwaysShown(false);
			df.setNegativePrefix("");
			df.setNegativeSuffix("-");
			return df.format(nBetrag.movePointRight(iNachkommastellen).doubleValue());
		} else {
			return "";
		}
	}

	protected String uebersetzeUSTLand(String sLKZ) {
		return "";
	}
	
	private boolean isFremdwaehrung(FibuexportDto exportDto) {
		return !exportDto.getWaehrung().equals(exportDto.getFremdwaehrung());
	}

	private String getSteuercode(FibuexportDto exportDto) {
		return isKontoOhneSteuercode(exportDto.getKontoDto(), kontoklassenOhneSteuercode)
				? ""
				: exportDto.getSteucod();
	}
	
	private boolean isKontoOhneSteuercode(KontoDto kontoDto, List<String> kontoklassenOhneSteuercode) {
		if(kontoDto == null 
				|| !FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontoDto.getKontotypCNr())) {
			return true;
		}

		for(String klassen : kontoklassenOhneSteuercode) {
			if(!klassen.isEmpty() && kontoDto.getCNr().startsWith(klassen)) {
				return true;
			}
		}

		return false;
	}
	
	class ExportdatenAdapter {
		private FibuexportDto exportDto;
		private boolean isFremdwaehrung;
		
		public ExportdatenAdapter(FibuexportDto exportDto) {
			this.exportDto = exportDto;
			isFremdwaehrung = false;
		}

		public void asFremdwaehrung() {
			isFremdwaehrung = true;
		}
		
		public boolean isFremdwaehrung() {
			return isFremdwaehrung;
		}
		
		public BigDecimal getHabenbetrag() {
			return isFremdwaehrung ? exportDto.getHabenbetragFWBD() : exportDto.getHabenbetragBD();
		}
		
		public BigDecimal getSollbetrag() {
			return isFremdwaehrung ? exportDto.getSollbetragFWBD() : exportDto.getSollbetragBD();
		}
		
		public BigDecimal getSteuerbetrag() {
			return isFremdwaehrung ? exportDto.getSteuerFWBD() : exportDto.getSteuerBD();
		}
		
		public String getWaehrungskennung() {
			return isFremdwaehrung ? exportDto.getFremdwaehrung() : exportDto.getWaehrung();
		}
		
		public BigDecimal getKurs() {
			return exportDto.getKurs();
		}
		
		private boolean isARAnzahlungOrSchlussrechnung() {
			return isAR(exportDto)
					&& (exportDto.isAnzahlungsbeleg() || exportDto.isSchlussrechnungsbeleg());
		}
		
		public String getBuchungstyp() {
			if (!isARAnzahlungOrSchlussrechnung())
				return "";
			
			return exportDto.isAnzahlungsbeleg()
					? Buchungstyp.AR_ANZAHLUNG
					: exportDto.isSchlussrechnungsbeleg()
						? Buchungstyp.AR_SCHLUSSRECHNUNG
						: "";
		}
		
		public String getAuftragsnummer() {
			return isARAnzahlungOrSchlussrechnung()
					? exportDto.getAuftnr()
					: "";
		}
	}
}
