package com.lp.server.finanz.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuExportFormatterDatevExt extends FibuExportFormatter {
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
	
	private static final String STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER = "19";
	private static final String STEUCOD_EINKAUF_REVERSE_CHARGE = "94";
	private static final String STEUCOD_REVERSE_CHARGE = "94";
	
	protected FibuExportFormatterDatevExt(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super(exportKriterienDto, theClientDto);
	}

	protected String exportiereDaten(FibuexportDto[] fibuExportDtos)
			throws EJBExceptionLP {
		try {
			if (fibuExportDtos == null) {
				return "";
			}

			DatevExtDatenProducer datevProducer = new DatevExtDatenProducer();
			return datevProducer.produce(fibuExportDtos);
//				if (fibuExportDtos[0].getBelegart().equals(
//						FibuExportManager.BELEGART_ER)) {
//					sExportDaten = exportiereDaten(fibuExportDtos,
//							partnerDtoFinanzamt, bInnerGemeinschaftlich,
//							sPartnerUstLkz);
//				} else {
//					sExportDaten = exportiereDaten(fibuExportDtos,
//							partnerDtoFinanzamt, bInnerGemeinschaftlich,
//							sPartnerUstLkz);
//				}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return "";
		}
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
				//--------------------------------------------------------------
				// Betrag
				if (fibuExportDtos[0].getBelegart().equals("ER")) {
					if (fibuExportDtos[i].getHabenbetragBD() != null) {
						swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(fibuExportDtos[i].getHabenbetragBD(), 12, 2));
					} else {
						if (bInnerGemeinschaftlich)
							mt.addParameter(P_UMSATZ, formatBetrag(fibuExportDtos[i].getSollbetragBD(), 12, 2));
						else
							mt.addParameter(P_UMSATZ, formatBetrag(fibuExportDtos[i].getSollbetragBD()
								.add(fibuExportDtos[i].getSteuerBD()), 12, 2));
					}
				} else {
					if (fibuExportDtos[i].getSollbetragBD() != null) {
						if (fibuExportDtos[i].getSollbetragBD().signum() != -1)
							swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(fibuExportDtos[i].getSollbetragBD().abs(), 12, 2));
					} else {
						if (fibuExportDtos[i].getHabenbetragBD().signum() == -1)
							swapKonten = true;
						mt.addParameter(P_UMSATZ, formatBetrag(fibuExportDtos[i].getHabenbetragBD()
								.add(fibuExportDtos[i].getSteuerBD()).abs(), 12, 2));
					}
				}
				//--------------------------------------------------------------
				// BU-Schluessel
				
				String sBUcode = "";
				// Eingangsrechnung
				if (fibuExportDtos[i].getBelegart().equals(FibuExportManager.BELEGART_ER)) {
					if (bInnerGemeinschaftlich) {
						if (fibuExportDtos[i].isBReverseCharge())
							sBUcode = STEUCOD_EINKAUF_REVERSE_CHARGE;
						else
							sBUcode = STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER;
					}
				} else {
					// Rechnung oder Gutschrift
					if (fibuExportDtos[i].getBelegart().equals(
							FibuExportManager.BELEGART_AR) || fibuExportDtos[i].getBelegart().equals(
									FibuExportManager.BELEGART_GS)) {
						if (bInnerGemeinschaftlich) {
							if (fibuExportDtos[i].isBReverseCharge())
								sBUcode = STEUCOD_REVERSE_CHARGE;
						}
					}
				}

				mt.addParameter(P_BU_SCHLUESSEL, sBUcode);
				
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
				mt.addParameter(P_WAEHRUNGSKENNUNG, fibuExportDtos[i].getWaehrung());
				//--------------------------------------------------------------
				// Basiswaehrungsbetrag
				mt.addParameter(P_BASISWAEHRUNGSBETRAG, "");
				//--------------------------------------------------------------
				// Basiswaehrungskennung
				mt.addParameter(P_BASISWAEHRUNGSKENNUNG, "");
				//--------------------------------------------------------------
				// Kurs
				mt.addParameter(P_KURS, "");
				//--------------------------------------------------------------
				// Lieferantenrechnungsnummer
				mt.addParameter(P_EXT_RECHNUNGNR, fibuExportDtos[i].getSExterneBelegnummer());
	
				//--------------------------------------------------------------
				// Faelligkeitsdatum
				mt.addParameter(P_FAELLIGKEIT, formatDatumTagMonatJahr(fibuExportDtos[i].getFaelligkeitsdatum()));

				mt.addParameter(P_PARTNER_KBZ, fibuExportDtos[i].getPartnerDto().getCKbez());
				
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
	
	class DatevExtDatenProducer {
		
		private HvOptional<String> mitlaufendesKonto = HvOptional.empty();
		private boolean isInnergemeinschaftlich = false;
		private boolean swapKonten = false;
		private BigDecimal umsatz;
		private String buSchluessel;
		private String kontoNr;
		private String gegenkontoNr;
		
		public DatevExtDatenProducer() {
			setMitlaufendesKonto();
		}
		
		public String produce(FibuexportDto[] fibuExportDtos) throws RemoteException, EJBExceptionLP {
			// Trennung zwischen ER und AR bzw. GS
			// derzeit keine Trennung
			setInnergemeinschaftlich(fibuExportDtos);
			
			StringBuilder builder = new StringBuilder();
			List<FibuexportDto> fibuExportList = validExportDtos(fibuExportDtos);
			Integer count = 0;
			for (FibuexportDto exportDto : fibuExportList) {
				setUmsatz(exportDto);
				setBuSchluessel(exportDto);
				setKonten(exportDto, count++);
				
				builder.append(export(exportDto));
			}
			
			return builder.toString();
		}
		
		private void setInnergemeinschaftlich(FibuexportDto[] fibuExportDtos) throws RemoteException, EJBExceptionLP {
			Integer finanzamtPartnerIId = pruefeFinanzamt(fibuExportDtos);
			// UST-LKZ des Partners bestimmen
			final String sPartnerUstLkz = getPartnerUstLkz(fibuExportDtos);
			// Zuerst: Richtigen UST-Prozentsatz und Finanzamt bestimmen.
			PartnerDto partnerDtoFinanzamt = null;
			// FA holen, wenn eines zugeordnet werden konnte.
			if (finanzamtPartnerIId != null) {
				partnerDtoFinanzamt = getFinanzamtPartner(finanzamtPartnerIId);
			}
			boolean bFinanzamtImPartnerLand = isFinanzamtImPartnerland(
					sPartnerUstLkz, partnerDtoFinanzamt);
			// feststellen, ob es sich um IG Erwerb / Erloes handelt
			isInnergemeinschaftlich = isInnergemeinschaftlich(
					fibuExportDtos, bFinanzamtImPartnerLand);
		}
		
		private void setMitlaufendesKonto() {
			mitlaufendesKonto = HvOptional.empty();
			String paramMitlaufendesKonto = getParameterFac().getExportDatevMitlaufendesKonto(theClientDto.getMandant());
			if (!Helper.isStringEmpty(paramMitlaufendesKonto)) {
				mitlaufendesKonto = HvOptional.of(paramMitlaufendesKonto);
			}
		}

		private void setBuSchluessel(FibuexportDto exportDto) {
			buSchluessel = "";
			if (isER(exportDto)) {
				setBuSchluesselER(exportDto);
				
			} else if (isARorGS(exportDto)) {
				setBuSchlusselARGS(exportDto);
			}
		}

		private void setBuSchlusselARGS(FibuexportDto exportDto) {
			if (isInnergemeinschaftlich
					&& exportDto.isBReverseCharge()) {
				buSchluessel = STEUCOD_REVERSE_CHARGE;
			}
		}

		private void setBuSchluesselER(FibuexportDto exportDto) {
			if (!isInnergemeinschaftlich) 
				return;
			
			buSchluessel = exportDto.isBReverseCharge()
					? STEUCOD_EINKAUF_REVERSE_CHARGE
					: STEUCOD_EINKAUF_IG_ERWERB_VORSTEUER;
		}

		private void setKonten(FibuexportDto exportDto, Integer count) {
			clearKontonummern();

			HvOptional<KontoDto> konto = HvOptional.ofNullable(exportDto.getKontoDto());
			HvOptional<KontoDto> gegenkonto = HvOptional.ofNullable(exportDto.getGegenkontoDto());
			
			if (konto.isPresent())
				kontoNr = konto.get().getCNr();
			
			if (gegenkonto.isPresent()) 
				gegenkontoNr = gegenkonto.get().getCNr();

			if (mitlaufendesKonto.isPresent()) {
				setMitlaufendesKonto(exportDto, count);
			}
			
			if (swapKonten) {
				String temp = kontoNr;
				kontoNr = gegenkontoNr;
				gegenkontoNr = temp;
			}
		}

		private void setMitlaufendesKonto(FibuexportDto exportDto, Integer count) {
			if (mitlaufendesKonto.isPresent()) {
				gegenkontoNr = mitlaufendesKonto.get();
			}
		}

		private void clearKontonummern() {
			kontoNr = "";
			gegenkontoNr = "";
		}

		private void setUmsatz(FibuexportDto exportDto) {
			swapKonten = false;
			if (isER(exportDto)) {
				if (exportDto.getHabenbetragBD() != null) {
					swapKonten = true;
					umsatz = exportDto.getHabenbetragBD();
				} else {
					umsatz = isInnergemeinschaftlich
							? exportDto.getSollbetragBD()
							: exportDto.getSollbetragBD().add(exportDto.getSteuerBD());
				}
			} else {
				if (exportDto.getSollbetragBD() != null) {
					if (exportDto.getSollbetragBD().signum() != -1)
						swapKonten = true;
					umsatz = exportDto.getSollbetragBD().abs();
				} else {
					if (exportDto.getHabenbetragBD().signum() == -1)
						swapKonten = true;
					umsatz = exportDto.getHabenbetragBD().add(exportDto.getSteuerBD()).abs();
				}
			}
		}

		private List<FibuexportDto> validExportDtos(FibuexportDto[] fibuExportDtos) {
			if (mitlaufendesKonto.isPresent()) {
				return Arrays.asList(fibuExportDtos);
			}
			
			List<FibuexportDto> result = new ArrayList<FibuexportDto>();
			for (int i = 0; i < fibuExportDtos.length; i++) {
				if (isER(fibuExportDtos[0]) && fibuExportDtos[i].getHabenbetragBD() != null
							|| isARorGS(fibuExportDtos[0]) && fibuExportDtos[i].getSollbetragBD() != null) {
					continue;
				}
				result.add(fibuExportDtos[i]);
			}
			return result;
		}
	
		private String export(FibuexportDto exportDto) {
			LpMailText mt = new LpMailText();
			mt.addParameter(P_UMSATZ, formatBetrag(umsatz, 12, 2));
	
			//--------------------------------------------------------------
			// BU-Schluessel
			mt.addParameter(P_BU_SCHLUESSEL, buSchluessel);
			
			//--------------------------------------------------------------
			// Gegenkonto
			mt.addParameter(P_GEGENKONTO, formatKontonummer(gegenkontoNr));

			//--------------------------------------------------------------
			// Belegnummer
			mt.addParameter(P_BELEGFELD1, exportDto.getBelegnummer());
			//--------------------------------------------------------------
			// Belegfeld2
			mt.addParameter(P_BELEGFELD2, exportDto.getBelegart());
			//--------------------------------------------------------------
			// Belegdatum
			mt.addParameter(P_BELEGDAT, formatDatum(exportDto.getBelegdatum()));
			//--------------------------------------------------------------
			// Konto
			mt.addParameter(P_KONTO, formatKontonummer(kontoNr));
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
			mt.addParameter(P_TEXT, exportDto.getText());
			//--------------------------------------------------------------
			// UID
			mt.addParameter(P_USTID, exportDto.getUidNummer());
			//--------------------------------------------------------------
			// EU-Steuersatz
			mt.addParameter(P_EU_STEUERSATZ, "");
			//--------------------------------------------------------------
			// Waehrungskennung
			mt.addParameter(P_WAEHRUNGSKENNUNG, exportDto.getWaehrung());
			//--------------------------------------------------------------
			// Basiswaehrungsbetrag
			mt.addParameter(P_BASISWAEHRUNGSBETRAG, "");
			//--------------------------------------------------------------
			// Basiswaehrungskennung
			mt.addParameter(P_BASISWAEHRUNGSKENNUNG, "");
			//--------------------------------------------------------------
			// Kurs
			mt.addParameter(P_KURS, "");
			//--------------------------------------------------------------
			// Lieferantenrechnungsnummer
			mt.addParameter(P_EXT_RECHNUNGNR, exportDto.getSExterneBelegnummer());
	
			//--------------------------------------------------------------
			// Faelligkeitsdatum
			mt.addParameter(P_FAELLIGKEIT, formatDatumTagMonatJahr(exportDto.getFaelligkeitsdatum()));
	
			mt.addParameter(P_PARTNER_KBZ, exportDto.getPartnerDto().getCKbez());
			
			//--------------------------------------------------------------
			// exportieren
			//--------------------------------------------------------------
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
							.getLocMandant(), theClientDto);
			return sZeile + Helper.CR_LF;
		}
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
			return df.format(nBetrag.movePointRight(2).doubleValue());
		} else {
			return "";
		}
	}

	protected String uebersetzeUSTLand(String sLKZ) {
		return "";
	}
}
