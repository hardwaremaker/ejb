/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den FibuExport
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 26.01.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/30 13:28:06 $
 */
public abstract class FibuExportManager extends Facade {

	private FibuExportFormatter exportFormatter = null;
	private FibuKontoExportFormatter kontoExportFormatter = null;

	public final static String UST_CODE_VORSTEUER_ODER_KEINE_STEUER = "00";
	public final static String UST_CODE_IST_UMSATZSTEUER = "01";
	public final static String UST_CODE_SOLL_UMSATZSTEUER = "03";
	public final static String UST_CODE_EK_KONTEN_IG_ERWERB_KEIN_VST_ABZUG = "08";
	public final static String UST_CODE_EK_KONTEN_IG_ERWERB_VST_ABZUG = "09";

	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_KEINE = "";
	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_TEILRECHNUNG = "01";
	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_STORNO_DER_TEILRECHNUNG = "02";
	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_ANZAHLUNGSRECHNUNG = "03";
	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_STORNO_DER_ANZAHLUNGSRECHNUNG = "04";
	public final static String ANZAHLUNGSRECHNUNGSKENNZEICHEN_SCHLUSSRECHNUNG = "05";

	public final static String IG_ART_IG_LIEFERUNG_ODER_VERBRINGUNG = "0";
	public final static String IG_ART_WERKLEISTUNG = "1";
	public final static String IG_ART_WARENBEWEGUNG = "2";
	public final static String IG_ART_DREIECKSGESCHAEFTE = "3";
	public final static String IG_ART_SPERRE_FUER_ZM = "9";

	public final static String SATZART = "0";

	public final static String VERBUCHUNGSKENNZEICHEN = "A";

	public final static String BUCHUNGSSCODE_SOLL = "1";
	public final static String BUCHUNGSSCODE_HABEN = "2";

	public final static String BELEGART_ER = "ER";
	public final static String BELEGART_AR = "AR";
	public final static String BELEGART_GS = "GS";

	protected final TheClientDto theClientDto;
	protected final FibuExportKriterienDto exportKriterienDto;
	protected final MandantDto mandantDto;
	protected final Map<?, ?> mMwst;
	protected final Map<?, ?> mZahlungsziele;
	protected final Map<Integer, FinanzamtDto> mFinanzaemter;

	FibuExportManager(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		this.theClientDto = theClientDto;
		this.exportKriterienDto = exportKriterienDto;
		this.mandantDto = holeMandant();
		this.mMwst = holeMwstSaetze();
		this.mZahlungsziele = holeZahlungsziele();
		this.mFinanzaemter = holeFinanzaemter();
	}

	private MandantDto holeMandant() {
		try {
			return getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private Map<?, ?> holeMwstSaetze() {
		try {
			return getMandantFac().mwstsatzFindAllByMandantAsDto(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private Map<?, ?> holeZahlungsziele() {
		try {
			return getMandantFac().zahlungszielFindAllByMandantAsDto(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private Map<Integer, FinanzamtDto> holeFinanzaemter() {
		FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAllByMandantCNr(theClientDto);
		Map<Integer, FinanzamtDto> map = new HashMap<Integer, FinanzamtDto>();
		for (int i = 0; i < finanzaemter.length; i++) {
			// Daten der Finanzaemter werden gleich auf Vollstaendigkeit
			// ueberprueft (nur die des mandanten)
			FinanzamtDto fa = finanzaemter[i];
			if (fa.getMandantCNr().equals(theClientDto.getMandant())
					&& fa.getPartnerDto().getLandplzortDto() == null) {
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(fa.getPartnerDto().getCName1nachnamefirmazeile1());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT,
						a,
						new Exception(
								"FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT "
								+ fa.getPartnerDto()
								.getCName1nachnamefirmazeile1()));
			}
			// in die Map geben. Key ist die PartnerIId
			map.put(fa.getPartnerIId(), fa);
		}
		return map;
	}

	public abstract FibuexportDto[] getExportdatenEingangsrechnung(
			Integer eingangsrechnungIId, Date dStichtag) throws EJBExceptionLP;

	public abstract FibuexportDto[] getExportdatenRechnung(Integer rechnungIId,
			Date dStichtag) throws EJBExceptionLP;

	public abstract FibuexportDto[] getExportdatenGutschrift(
			Integer rechnungIId, Date dStichtag) throws EJBExceptionLP;

	public final String exportiereEingangsrechnung(Integer eingangsrechnungIId, Date dStichtag)
			throws EJBExceptionLP {
		FibuexportDto[] exportDaten = getExportdatenEingangsrechnung(eingangsrechnungIId ,dStichtag);
		return exportFormatter.exportiereDaten(exportDaten);
	}

	public final String exportiereRechnung(Integer rechnungIId, Date dStichtag)
			throws EJBExceptionLP {
		FibuexportDto[] exportDaten = getExportdatenRechnung(rechnungIId,
				dStichtag);
		return exportFormatter.exportiereDaten(exportDaten);
	}

	public final String exportiereGutschrift(Integer rechnungIId, Date dStichtag)
			throws EJBExceptionLP {
		FibuexportDto[] exportDaten = getExportdatenGutschrift(rechnungIId,
				dStichtag);
		return exportFormatter.exportiereDaten(exportDaten);
	}
	/**
	 * Anhand Eingangsrechnung den Lieferanten holen, und pruefen, ob ein
	 * Kreditorenkonto definiert ist.
	 * 
	 * @param erDto
	 *            EingangsrechnungDto
	 * @return LieferantDto
	 * @throws EJBExceptionLP
	 */
	protected LieferantDto pruefeEingangsrechnungLieferant(
			EingangsrechnungDto erDto) throws EJBExceptionLP {

		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(erDto.getLieferantIId(),
						theClientDto);
		// die Laenderart eines Partners muss feststellbar sein
		pruefePartnerLaenderart(lieferantDto.getPartnerIId(), erDto);
		// Pruefen, ob die ER noch im gueltigen Exportzeitraum liegt
		pruefeBelegAufGueltigenExportZeitraum(erDto,
				lieferantDto.getPartnerDto());
		// der Lieferant muss ein kreditorenkonto haben
		if (lieferantDto.getKontoIIdKreditorenkonto() == null) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_KREDITORENKONTO_NICHT_DEFINIERT,
					new Exception("Kreditorenkonto fuer Lieferant "
							+ lieferantDto.getIId()
							+ ","
							+ lieferantDto.getPartnerDto()
									.getCName1nachnamefirmazeile1()
							+ " ist nicht definiert"));
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getAllInfoForTheClient(erDto));
			a.addAll(getAllInfoForTheClient(lieferantDto.getPartnerDto()));
			ex.setAlInfoForTheClient(a);
			throw ex;
		}
		if (erDto.getCLieferantenrechnungsnummer() == null) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_LIEFERANTENRECHNUNGSNUMMER_FEHLT,
					new Exception("Lieferantenrechnungsnummer fehlt in ER "
							+ erDto.getCNr() + ","));
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getAllInfoForTheClient(erDto));
			a.addAll(getAllInfoForTheClient(lieferantDto.getPartnerDto()));
			ex.setAlInfoForTheClient(a);
			throw ex;
		}
		return lieferantDto;

	}

	/**
	 * Anhand Rechnung den Kunden holen, und pruefen, ob ein Debitorenkonto
	 * definiert ist.
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @return KundeDto
	 * @throws EJBExceptionLP
	 */
	protected KundeDto pruefeRechnungKunde(RechnungDto rechnungDto)
			throws EJBExceptionLP {
		try {
			// Ist die RE/GS noch nicht ausgedruckt?? das darf nicht sein
			if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
						new Exception("RE oder GS mit I_ID="
								+ rechnungDto.getIId() + " ist nicht aktiviert"));
				// Kunden holen
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnungDto.getKundeIId(), theClientDto);
				ArrayList<Object> a = new ArrayList<Object>();
				a.addAll(getAllInfoForTheClient(rechnungDto));
				a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
			// den Kunden holen
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			// die Laenderart eines Partners muss feststellbar sein
			pruefePartnerLaenderart(kundeDto.getPartnerIId(), rechnungDto);
			// Pruefen, ob die RE/GS noch im gueltigen Exportzeitraum liegt
			pruefeBelegAufGueltigenExportZeitraum(rechnungDto,
					kundeDto.getPartnerDto());
			// der Kunde muss ein debitorenkonto haben
			if (kundeDto.getIidDebitorenkonto() == null) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT,
						new Exception(kundeDto.getIId()	+ "," + kundeDto.getPartnerDto().getCName1nachnamefirmazeile1()));
				ArrayList<Object> a = new ArrayList<Object>();
				a.addAll(getAllInfoForTheClient(rechnungDto));
				a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
			// pruefen, ob die Rechnung vollstaendig kontiert ist
			BigDecimal bdKontiert = getRechnungFac().getProzentsatzKontiert(
					rechnungDto.getIId(), theClientDto);
			if (bdKontiert.compareTo(new BigDecimal(0)) != 0
					&& bdKontiert.compareTo(new BigDecimal(100)) != 0) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT,
						new Exception("Debitorenkonto fuer Kunde "
								+ kundeDto.getIId()
								+ ","
								+ kundeDto.getPartnerDto()
										.getCName1nachnamefirmazeile1()
								+ " ist nicht definiert"));
				ArrayList<Object> a = new ArrayList<Object>();
				a.addAll(getAllInfoForTheClient(rechnungDto));
				a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
			return kundeDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Holen der Mehrfachkontierungsdaten einer ER und Pruefung auf
	 * Vollstaendigkeit. Falls keine Mehrfachkontierung vorhanden, dann ist der
	 * Rueckgabewert null.
	 * 
	 * @param erDto
	 *            EingangsrechnungDto
	 * @return EingangsrechnungKontierungDto[]
	 * @throws EJBExceptionLP
	 */
	protected EingangsrechnungKontierungDto[] pruefeEingangsrechnungKontierung(
			EingangsrechnungDto erDto) throws EJBExceptionLP {
		EingangsrechnungKontierungDto[] erKontoDto = null;
		try {
			if (erDto.getKontoIId() != null) {
				// passt
			}
			// Mehrfachkontierung
			else {
				erKontoDto = getEingangsrechnungFac()
						.eingangsrechnungKontierungFindByEingangsrechnungIId(
								erDto.getIId());
				BigDecimal bdOffen = getEingangsrechnungFac()
						.getWertNochNichtKontiert(erDto.getIId());
				if (bdOffen.compareTo(new BigDecimal(0)) != 0) {
					EJBExceptionLP ex = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT,
							new Exception("ER " + erDto.getCNr()
									+ " ist nicht vollstaendig kontiert"));
					ArrayList<Object> a = new ArrayList<Object>();
					a.addAll(getAllInfoForTheClient(erDto));
					ex.setAlInfoForTheClient(a);
					throw ex;
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return erKontoDto;
	}

	/**
	 * Holen der Mehrfachkontierungsdaten einer RE oder GS und Pruefung auf
	 * Vollstaendigkeit. Falls keine Mehrfachkontierung vorhanden, dann ist der
	 * Rueckgabewert null.
	 * 
	 * @param erDto
	 *            RechnungDto
	 * @return RechnungKontierungDto[]
	 * @throws EJBExceptionLP
	 */
	protected RechnungkontierungDto[] pruefeRechnungKontierung(RechnungDto erDto)
			throws EJBExceptionLP {
		RechnungkontierungDto[] erKontoDto = null;
		try {
			erKontoDto = getRechnungFac().rechnungkontierungFindByRechnungIId(
					erDto.getIId());
			// wenn keine Eintraege, dann null setzen
			if (erKontoDto != null && erKontoDto.length == 0) {
				erKontoDto = null;
			} else {
				BigDecimal bdKontiert = getRechnungFac()
						.getProzentsatzKontiert(erDto.getIId(), theClientDto);
				if (bdKontiert.compareTo(new BigDecimal(100.0)) != 0) {
					EJBExceptionLP ex = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT,
							new Exception("RE/GS " + erDto.getCNr()
									+ " ist nicht vollstaendig kontiert"));
					ArrayList<Object> a = new ArrayList<Object>();
					a.addAll(getAllInfoForTheClient(erDto));
					ex.setAlInfoForTheClient(a);
					throw ex;
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return erKontoDto;
	}

	/**
	 * eine Kostenstelle anhand iId finden und gleichzeit pruefen, ob ein
	 * Sachkonto hinterlegt ist.
	 * 
	 * @param kostenstelleIId
	 *            Integer
	 * @param oBelegDto
	 *            Object
	 * @return KostenstelleDto
	 * @throws EJBExceptionLP
	 */
	protected KostenstelleDto pruefeKostenstelle(Integer kostenstelleIId,
			Object oBelegDto) throws EJBExceptionLP {

		KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(
				kostenstelleIId);
		// die Kostenstelle muss ein sachkonto haben
		if (kstDto.getKontoIId() == null) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_KOSTENSTELLE_HAT_KEIN_SACHKONTO,
					new Exception("Sachkonto fuer Kostenstelle i_id"
							+ kstDto.getIId() + "," + kstDto.getCNr() + " "
							+ kstDto.getCBez() + " ist nicht definiert"));
			ArrayList<Object> a = new ArrayList<Object>();
			if (oBelegDto instanceof RechnungDto) {
				a.addAll(getAllInfoForTheClient((RechnungDto) oBelegDto));
			} else if (oBelegDto instanceof EingangsrechnungDto) {
				a.addAll(getAllInfoForTheClient((EingangsrechnungDto) oBelegDto));
			}
			a.addAll(getAllInfoForTheClient(kstDto));
			ex.setAlInfoForTheClient(a);
			throw ex;
		}
		return kstDto;

	}

	/**
	 * Ein Konto in die Laenderart des uebergebenen Partners uebersetzen.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @param laenderartCNr
	 *            String
	 * @param oBelegDto
	 *            Object
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	protected KontoDto uebersetzeKontoNachLandBzwLaenderart(Integer kontoIId,
			String laenderartCNr, Integer finanzamtIId, String mandantCNr,
			Object oBelegDto, Integer LandIId) throws EJBExceptionLP {
		KontoDto kontoDto = null;
		MandantDto mandantDto;
		Integer meinFinanzamtIId = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKeyOhneExc(
					mandantCNr, theClientDto);
			meinFinanzamtIId = mandantDto.getPartnerIIdFinanzamt();
		} catch (RemoteException e) {
			//
		}

		boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);
		// keine Uebersetzung Inland:
		// keine Fibu dann bei Inland oder fuer eigenes Finanzamt
		// ansonst wenn Inland
		try {
			boolean keineUbersetzung = false;
			if (laenderartCNr.equals(FinanzFac.LAENDERART_INLAND)) {
				if (!hatFibu) {
					keineUbersetzung = true;
				} else {
					if (meinFinanzamtIId == null)
						keineUbersetzung = true;
					else if (meinFinanzamtIId.equals(finanzamtIId))
						keineUbersetzung = true;
				}
			}
			if (keineUbersetzung) {
				kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);
			} else {
				// Uebersetzung finden
				// zuerst nach Landzuordnung
				KontolandDto kontolandDto = getFinanzFac()
						.kontolandFindByPrimaryKeyOhneExc(kontoIId, LandIId);
				if (kontolandDto != null) {
					kontoDto = getFinanzFac().kontoFindByPrimaryKey(
							kontolandDto.getKontoIIdUebersetzt());
				} else {
					// dann nach laenderart
					KontolaenderartDto kontolaenderartDto = getFinanzFac()
							.kontolaenderartFindByPrimaryKeyOhneExc(kontoIId,
									laenderartCNr, finanzamtIId, mandantCNr);
					// wenn eine hinterlegt ist
					if (kontolaenderartDto != null) {
						kontoDto = getFinanzFac().kontoFindByPrimaryKey(
								kontolaenderartDto.getKontoIIdUebersetzt());
					}
					// ansonsten entsprechende Fehlermeldung
					else {
						KontoDto kontoDtoHelp = getFinanzFac()
								.kontoFindByPrimaryKey(kontoIId);
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT,
								new Exception("Kontolaenderart fuer konto i_id"
										+ kontoDtoHelp.getIId() + ","
										+ kontoDtoHelp.getCNr() + " "
										+ kontoDtoHelp.getCBez() + ", "
										+ laenderartCNr
										+ " ist nicht definiert"));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(kontoDtoHelp.getCNr() + " "
								+ kontoDtoHelp.getCBez() + ", " + laenderartCNr);
						a.add("Finanzamt Mandant I_ID='" + (meinFinanzamtIId == null ? "" : meinFinanzamtIId.toString()) + "'") ;
						a.add("Finanzamt I_ID='" + (finanzamtIId == null ? "" : finanzamtIId.toString()) + "'");
						if (oBelegDto instanceof RechnungDto) {
							a.addAll(getAllInfoForTheClient((RechnungDto) oBelegDto));
						} else if (oBelegDto instanceof EingangsrechnungDto) {
							a.addAll(getAllInfoForTheClient((EingangsrechnungDto) oBelegDto));
						}
						ex.setAlInfoForTheClient(a);
						throw ex;
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return kontoDto;
	}

	/**
	 * Pruefen, ob fuer das uebergebene Konto das weiterfuehrende UST-Konto
	 * definiert ist. Dieses wird, falls alle Daten vorhanden sind, in die
	 * Laenderart des Partners uebersetzt.
	 * 
	 * @param kontoDto
	 *            KontoDto
	 * @param partnerIId
	 *            Integer
	 * @param oBelegDto
	 *            String
	 * @return KontoDto
	 * @throws EJBExceptionLP
	 */
	/*
	 * protected KontoDto pruefeKontoNachUSTKonto(KontoDto kontoDto, Integer
	 * partnerIId, Object oBelegDto) throws EJBExceptionLP, RemoteException {
	 * KontoDto kontoDtoUebersetzt = null; if
	 * (kontoDto.getKontoIIdWeiterfuehrendUst() != null) { String laenderartCNr
	 * = pruefePartnerLaenderart(partnerIId, oBelegDto); PartnerDto partnerDto =
	 * getPartnerFac().partnerFindByPrimaryKey( partnerIId, null); Integer iLand
	 * = partnerDto.getLandplzortDto().getIlandID(); kontoDtoUebersetzt =
	 * uebersetzeKontoNachLandBzwLaenderart(kontoDto .getIId(), laenderartCNr,
	 * finanzamtIId, mandantCNr, oBelegDto, iLand); } else { EJBExceptionLP ex =
	 * new EJBExceptionLP(
	 * EJBExceptionLP.FEHLER_FINANZ_EXPORT_UST_KONTO_NICHT_DEFINIERT, new
	 * Exception("UST-Konto fuer konto i_id" + kontoDto.getIId() + "," +
	 * kontoDto.getCNr() + " " + kontoDto.getCBez() + " ist nicht definiert"));
	 * ArrayList<Object> a = new ArrayList<Object>(); if (oBelegDto instanceof
	 * RechnungDto) { a.addAll(getAllInfoForTheClient((RechnungDto) oBelegDto));
	 * } else if (oBelegDto instanceof EingangsrechnungDto) { a
	 * .addAll(getAllInfoForTheClient((EingangsrechnungDto) oBelegDto)); }
	 * a.addAll(getAllInfoForTheClient(kontoDto)); ex.setAlInfoForTheClient(a);
	 * throw ex; } return kontoDtoUebersetzt; }
	 */

	/**
	 * Pruefen, ob ein Partner einer Laenderarar ist, und die Laenderart
	 * zurueckgeben.
	 * 
	 * @param partnerIId
	 *            Integer
	 * @param oBelegDto
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	private String pruefePartnerLaenderart(Integer partnerIId, Object oBelegDto)
			throws EJBExceptionLP {
		String laenderartCNr = null;
		try {
			laenderartCNr = getFinanzServiceFac().getLaenderartZuPartner(
					partnerIId, theClientDto);
			if (laenderartCNr == null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(partnerIId, theClientDto);
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_LAENDERART_NICHT_FESTSTELLBAR_FUER_PARTNER,
						new Exception(
								"Laenderart nicht feststellbar partner i_id"
										+ partnerDto.getIId()
										+ ","
										+ partnerDto
												.getCName1nachnamefirmazeile1()));
				ArrayList<Object> a = new ArrayList<Object>();
				if (oBelegDto instanceof RechnungDto) {
					a.addAll(getAllInfoForTheClient((RechnungDto) oBelegDto));
				} else if (oBelegDto instanceof EingangsrechnungDto) {
					a.addAll(getAllInfoForTheClient((EingangsrechnungDto) oBelegDto));
				}
				a.addAll(getAllInfoForTheClient(partnerDto));
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return laenderartCNr;
	}

	private BuchungdetailDto[] getBuchungszeilenFromExportdaten(
			FibuexportDto[] exportDtos) throws EJBExceptionLP {
		BuchungdetailDto[] buchungen = new BuchungdetailDto[exportDtos.length];
		for (int i = 0; i < exportDtos.length; i++) {
			buchungen[i].setKontoIId(exportDtos[i].getKontoDto().getIId());
			buchungen[i].setKontoIIdGegenkonto(exportDtos[i].getGegenkontoDto()
					.getIId());
			if (exportDtos[i].getHabenbetragBD().doubleValue() != 0.0) {
				buchungen[i].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				buchungen[i].setNBetrag(exportDtos[i].getHabenbetragBD());
			} else {
				buchungen[i].setBuchungdetailartCNr(BuchenFac.SollBuchung);
				buchungen[i].setNBetrag(exportDtos[i].getSollbetragBD());
			}
			buchungen[i].setNUst(exportDtos[i].getSteuerBD());
		}
		return buchungen;
	}

	protected void verbucheExportdaten(BuchungDto buchungDto,
			FibuexportDto[] exportDtos, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			BuchungdetailDto[] buchungen = getBuchungszeilenFromExportdaten(exportDtos);
			getBuchenFac().buchen(buchungDto, buchungen, false, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	void setExportFormatter(FibuExportFormatter exportFormatter) {
		this.exportFormatter = exportFormatter;
	}

	public FibuExportFormatter getExportFormatter() {
		return this.exportFormatter;
	}

	void setKontoExportFormatter(FibuKontoExportFormatter kontoExportFormatter) {
		this.kontoExportFormatter = kontoExportFormatter;
	}

	public String exportiereUeberschriftBelege() {
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		if ((Boolean) parameter.getCWertAsObject()) {
			return exportFormatter.exportiereUeberschrift() + Helper.CR_LF;
		} else {
			return "";
		}
	}

	public String exportiereUeberschriftPersonenkonten(TheClientDto theClientDto) {
		ParametermandantDto parameter = null;
		try {
			parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		if ((Boolean) parameter.getCWertAsObject()) {
			return kontoExportFormatter.exportiereUebberschrift(theClientDto)
					+ Helper.CR_LF;
		} else {
			return "";
		}
	}

	public String exportierePersonenkonten(FibuKontoExportDto[] konten,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return kontoExportFormatter.exportiereDaten(konten, theClientDto); // +
		// Helper.
		// CR_LF
		// ;
	}

	/**
	 * Der Beleg muss im Monat des Stichtages liegen. lt. Definition von WH
	 * 28.03.06
	 * 
	 * @param oBelegDto
	 *            FibuExportKriterienDto
	 * @param partnerDto
	 *            PartnerDto
	 * @throws EJBExceptionLP
	 */
	protected void pruefeBelegAufGueltigenExportZeitraum(Object oBelegDto,
			PartnerDto partnerDto) throws EJBExceptionLP {
		if (!exportKriterienDto.isBAuchBelegeAusserhalbGueltigkeitszeitraum()) {
			if (liegtBelegdatumInnerhalbGueltigemExportZeitraum(oBelegDto)) {
				return;
			} else {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM,
						new Exception(
								"Der Beleg liegt ausserhalb des g\u00FCltigen Zeitraumes"
										+ oBelegDto.toString()));
				ArrayList<Object> a = new ArrayList<Object>();
				// Gueltigen Zeitraum angeben
				GregorianCalendar cBeginn = new GregorianCalendar();
				cBeginn.setTime(exportKriterienDto.getDStichtag());
				cBeginn.set(Calendar.DATE, 1);
				a.add(Helper.formatDatum(cBeginn.getTime(),
						theClientDto.getLocUi())
						+ " - "
						+ Helper.formatDatum(exportKriterienDto.getDStichtag(),
								theClientDto.getLocUi()));
				// Belegart + Belegnummer
				if (oBelegDto instanceof RechnungDto) {
					a.addAll(getAllInfoForTheClient((RechnungDto) oBelegDto));
				} else if (oBelegDto instanceof EingangsrechnungDto) {
					a.addAll(getAllInfoForTheClient((EingangsrechnungDto) oBelegDto));
				}
				// Belegdatum
				java.sql.Date tBelegdatum = null;
				if (oBelegDto instanceof RechnungDto) {
					tBelegdatum = new java.sql.Date(((RechnungDto) oBelegDto)
							.getTBelegdatum().getTime());
				} else if (oBelegDto instanceof EingangsrechnungDto) {
					tBelegdatum = new java.sql.Date(
							((EingangsrechnungDto) oBelegDto).getDBelegdatum()
									.getTime());
				}
				a.add(getTextRespectUISpr("lp.datum",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " "
						+ Helper.formatDatum(tBelegdatum,
								theClientDto.getLocUi()));
				a.addAll(getAllInfoForTheClient(partnerDto));
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
		}
	}

	public boolean liegtBelegdatumInnerhalbGueltigemExportZeitraum(
			Object oBelegDto) {
		java.util.Date tBelegdatum = null;
		if (oBelegDto instanceof RechnungDto) {
			tBelegdatum = ((RechnungDto) oBelegDto).getTBelegdatum();
		} else if (oBelegDto instanceof FLRRechnung) {
			tBelegdatum = ((FLRRechnung) oBelegDto).getD_belegdatum();
		} else if (oBelegDto instanceof EingangsrechnungDto) {
			tBelegdatum = ((EingangsrechnungDto) oBelegDto).getDBelegdatum();
		} else if (oBelegDto instanceof FLREingangsrechnung) {
			tBelegdatum = ((FLREingangsrechnung) oBelegDto).getT_belegdatum();
		}
		GregorianCalendar cStichtag = new GregorianCalendar();
		cStichtag.setTime(exportKriterienDto.getDStichtag());
		GregorianCalendar cBelegdatum = new GregorianCalendar();
		cBelegdatum.setTime(tBelegdatum);
		if (cStichtag.get(Calendar.YEAR) == cBelegdatum.get(Calendar.YEAR)
				&& cStichtag.get(Calendar.MONTH) == cBelegdatum
						.get(Calendar.MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	protected ArrayList<Object> getAllInfoForTheClient(PartnerDto partnerDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(partnerDto.formatFixTitelName1Name2());
		list.add(partnerDto.formatAdresse());
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(RechnungDto rechnungDto) {
		try {
			ArrayList<Object> list = new ArrayList<Object>();
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			if (rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				list.add(getTextRespectUISpr("lp.kuerzel.rechnung",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " " + rechnungDto.getCNr());
			} else if (rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				list.add(getTextRespectUISpr("lp.kuerzel.gutschrift",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " " + rechnungDto.getCNr());
			}
			return list;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected ArrayList<Object> getAllInfoForTheClient(EingangsrechnungDto erDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		String sBelegartBelegnummer = getTextRespectUISpr(
				"er.eingangsrechnungsnummer", theClientDto.getMandant(),
				theClientDto.getLocUi())
				+ " " + erDto.getCNr();
		list.add(sBelegartBelegnummer);
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(KontoDto kontoDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(kontoDto.getCNr() + " " + kontoDto.getCBez());
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(KostenstelleDto kstDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(kstDto.getCNr() + " " + kstDto.getCBez());
		return list;
	}

	protected String getLaenderartZuPartner(PartnerDto partnerDto)
			throws EJBExceptionLP {
		try {
			return getFinanzServiceFac().getLaenderartZuPartner(mandantDto,
					partnerDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected String getLaenderartZuKonto(PartnerDto partnerDto,
			Integer kontoIId) throws EJBExceptionLP {
		try {
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);
			PartnerDto partnerDtoBasis = getPartnerFac()
					.partnerFindByPrimaryKey(kontoDto.getFinanzamtIId(),
							theClientDto);
			return getFinanzServiceFac().getLaenderartZuPartner(
					partnerDtoBasis, partnerDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected String getLaenderartZuKonto(PartnerDto partnerDto,
			Integer kontoIId, boolean bReverseCharge) throws EJBExceptionLP {
		try {
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);
			PartnerDto partnerDtoBasis = getPartnerFac()
					.partnerFindByPrimaryKey(kontoDto.getFinanzamtIId(),
							theClientDto);
			String laenderart = getFinanzServiceFac().getLaenderartZuPartner(
					partnerDtoBasis, partnerDto, theClientDto);
			if (bReverseCharge) {
				if (laenderart.equals(FinanzFac.LAENDERART_INLAND))
					return FinanzFac.LAENDERART_REVCHARGE_INLAND;
				else if (laenderart.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID))
					return FinanzFac.LAENDERART_REVCHARGE_AUSLAND;
				else
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT,
							"Keine L\u00E4nderart\u00FCbersetzung bei Reversecharge m\u00F6glich: L\u00E4nderart=" + laenderart);
			} else {
				return laenderart;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected final ZahlungszielDto getZahlungsziel(Integer zahlungszielIId) {
		return (ZahlungszielDto) mZahlungsziele.get(zahlungszielIId);
	}

	protected final MwstsatzDto getMwstsatz(Integer mwstsatzIId) {
		return (MwstsatzDto) mMwst.get(mwstsatzIId);
	}

	protected final FinanzamtDto getFinanzamt(Integer partnerIIdFinanzamt) {
		return (FinanzamtDto) mFinanzaemter.get(partnerIIdFinanzamt);
	}
}
