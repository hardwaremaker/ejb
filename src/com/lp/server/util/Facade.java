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
package com.lp.server.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.DataException;

import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.ejbfac.WebshopItemServiceFacLocal;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelimportFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.artikel.service.RahmenbedarfeFac;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejbfac.WebshopOrderServiceFacLocal;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.media.ejbfac.EmailMediaLocalFac;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.partner.ejbfac.WebshopCustomerServiceFacLocal;
import com.lp.server.partner.service.AnredesprDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesachbearbeiterFac;
import com.lp.server.partner.service.KundesokoFac;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.personal.service.MaschineFac;
import com.lp.server.personal.service.PersonalApiFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.ejbfac.BatcherFac;
import com.lp.server.system.ejbfac.BatcherSingleTransactionFac;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRMediaFac;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AutoBestellvorschlagFac;
import com.lp.server.system.service.AutoFehlmengendruckFac;
import com.lp.server.system.service.AutoMahnenFac;
import com.lp.server.system.service.AutoMahnungsversandFac;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckFac;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.BelegpositionkonvertierungFac;
import com.lp.server.system.service.DokumenteFac;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.PflegeFac;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

/**
 * 
 * <p>
 * <I>Gemeinsame Superklasse fuer alle Session Facades in LP5</I>
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>27. 10. 2004</I>
 * </p>
 * 
 * verantwortlich: Martin Bluehweis
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class Facade {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			this.getClass());

	private FacadeBeauftragter oFacadeBeauftragter = null;

	public final static int MAX_UNBESCHRAENKT = 300;

	public final static String NICHT_SORTIERBAR = " X";

	public Facade() {
		oFacadeBeauftragter = new FacadeBeauftragter();
	}

	public Context getInitialContext() {
		return oFacadeBeauftragter.getInitialContext();
	}

	/**
	 * Aktuelles Datum holen.
	 * 
	 * @return Date
	 */
	protected final java.sql.Date getDate() {
		return Helper.cutDate(new java.sql.Date(System.currentTimeMillis()));
	}

	/**
	 * Aktuellen Timestamp holen.
	 * 
	 * @return Date
	 */
	protected final java.sql.Timestamp getTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * Ueberpruefe ob der User cNrUserI auf das System zugreifen darf.
	 * 
	 * @param cNrUserI
	 *            String
	 * @throws EJBExceptionLP
	 * @return TheClientDto
	 */
	protected final TheClientDto check(String cNrUserI) throws EJBExceptionLP {
		myLogger.entry(cNrUserI);
		if (cNrUserI == null) {
			// exccatch:
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_C_NR_USER_IS_NULL,
					"Fehler bei check. cNrUserI == null");
		}
		try {
			return getTheClientFac().theClientFindByUserLoggedIn(cNrUserI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public String getLPStackTrace(StackTraceElement[] trace) {
		String str = "";
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < trace.length; i++) {
			StackTraceElement el = trace[i];
			if (el.getClassName().startsWith("com.lp.")) {
				al.add(el.toString());
			}
		}
		for (int i = 0; i < al.size(); i++) {
			str += al.get(i) + "\r\n";
		}
		return str;
	}

	protected void erstelleProtokollEintrag(ProtokollDto protokollDto,
			TheClientDto theClientDto) {
		getSystemFac().erstelleProtokolleintrag(protokollDto, theClientDto);
	}

	/**
	 * Ueberpruefe ob der User cNrUserI auf das System zugreifen darf.
	 * 
	 * @param theClientDto
	 *            String
	 * @param oData
	 *            Object
	 * @throws EJBExceptionLP
	 * @return TheClientDto
	 */
	protected final TheClientDto checkAndLogData(TheClientDto theClientDto,
			Object oData) throws EJBExceptionLP {
		myLogger.logData(oData, theClientDto.getIDUser());
		if (theClientDto.getIDUser() == null) {
			// exccatch:
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_C_NR_USER_IS_NULL,
					"Fehler bei CheckAndLogData. theClientDto.getIDUser() == null");
		}
		try {
			return getTheClientFac().theClientFindByUserLoggedIn(
					theClientDto.getIDUser());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected final void checkLocale(Locale localeI) throws EJBExceptionLP {
		myLogger.logData(localeI);

		if (localeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("localeI == null"));
		}
	}

	/**
	 * BigDecimals in der DB muessen das Format 15, 4 haben. Es ist moeglich,
	 * dass das Ergebnis einer Berechnung am Client mehr als 15 Vorkommastellen
	 * hat. In diesem Fall wird dem Benutzer eine entsprechende Meldung
	 * angezeigt. checknumberformat: 0
	 * 
	 * @param bdNumberI
	 *            die Zahl
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	protected final static void checkNumberFormat(BigDecimal bdNumberI)
			throws EJBExceptionLP {
		if (bdNumberI.doubleValue() > SystemFac.MAX_N_NUMBER
				|| bdNumberI.doubleValue() < SystemFac.MIN_N_NUMBER) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORMAT_NUMBER,
					new Exception("Ungueltiges NumberFormat, Format 15,4"));
		}
	}

	/**
	 * Hole die Clientinformationen des Users.
	 * 
	 * deprecated MB: TheClientDto wird von check2(idUser) zurueckgegeben -> du
	 * ersparst dir einen db-zugriff
	 * 
	 * @param idUser
	 *            String
	 * @return TheClientDto
	 */
	protected final TheClientDto getTheClient(String idUser)
			throws EJBExceptionLP {
		try {
			return oFacadeBeauftragter.getTheClient(idUser);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	/**
	 * SessionFacade fuer Ansprechpartner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AnsprechpartnerFac
	 */
	protected final AnsprechpartnerFac getAnsprechpartnerFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnsprechpartnerFac();
	}

	/**
	 * SessionFacade fuer Auftrag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragFac
	 */
	protected final AuftragFac getAuftragFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragFac();
	}

	protected final KuecheFac getKuecheFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getKuecheFac();
	}

	/**
	 * SessionFacade fuer Auftragteilnehmer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragFac
	 */
	protected final AuftragteilnehmerFac getAuftragteilnehmerFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragteilnehmerFac();
	}

	protected final InstandhaltungFac getInstandhaltungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getInstandhaltungFac();
	}

	/**
	 * SessionFacade fuer Anfrage holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AnfrageFac
	 */
	protected final AnfrageFac getAnfrageFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnfrageFac();
	}

	protected final AnfragepositionFac getAnfragepositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnfragepositionFac();
	}

	protected final AnfrageServiceFac getAnfrageServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnfrageServiceFac();
	}

	/**
	 * SessionFacade fuer Kundesachbearbeiter holen.
	 * 
	 * @return KundesachbearbeiterFac
	 * @throws EJBExceptionLP
	 */
	protected final KundesachbearbeiterFac getKundesachbearbeiterFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getKundesachbearbeiterFac();
	}

	/**
	 * SessionFacade fuer Artikel holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	protected final ArtikelFac getArtikelFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelFac();
	}

	protected final ArtikelimportFac getArtikelimportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelimportFac();
	}

	/**
	 * SessionFacade fuer ArtikelReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	protected final ArtikelReportFac getArtikelReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelReportFac();
	}

	protected final ArtikelkommentarFac getArtikelkommentarFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelkommentarFac();
	}

	/**
	 * SessionFacade fuer Auftragposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragpositionFac
	 */
	protected final AuftragpositionFac getAuftragpositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragpositionFac();
	}

	protected final AuftragReportFac getAuftragReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragReportFac();
	}

	/**
	 * SessionFacade fuer AuftragServices holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragServiceFac
	 */
	protected final AuftragServiceFac getAuftragServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragServiceFac();
	}

	/**
	 * SessionFacade fuer Bestellung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellungFac
	 */
	protected final BestellungFac getBestellungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellungFac();
	}

	protected final BestellpositionFac getBestellpositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellpositionFac();
	}

	protected final BestellungServiceFac getBestellungServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellungServiceFac();
	}

	/**
	 * SessionFacade fuer Kunde holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	protected final KundeFac getKundeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getKundeFac();
	}

	/**
	 * SessionFacade fuer KundeReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	protected final KundeReportFac getKundeReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getKundeReportFac();
	}

	/**
	 * SessionFacade fuer Lager holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LagerFac
	 */
	protected final LagerFac getLagerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLagerFac();
	}

	protected final LagerReportFac getLagerReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLagerReportFac();
	}

	/**
	 * SessionFacade fuer Wareneingang holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return WareneingangFac
	 */
	protected final WareneingangFac getWareneingangFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getWareneingangFac();
	}

	/**
	 * SessionFacade fuer Wareneingang holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return WareneingangFac
	 */
	protected final BestellvorschlagFac getBestellvorschlagFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellvorschlagFac();
	}

	/**
	 * SessionFacade fuer Lieferschein holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinFac
	 */
	protected final LieferscheinFac getLieferscheinFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinFac();
	}

	/**
	 * SessionFacade fuer Reklamation holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReklamationFac
	 */
	protected final ReklamationFac getReklamationFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getReklamationFac();
	}

	/**
	 * SessionFacade fuer Lieferscheinposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinFac
	 */
	protected final LieferscheinpositionFac getLieferscheinpositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinpositionFac();
	}

	protected final LieferscheinServiceFac getLieferscheinServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinServiceFac();
	}

	protected final LieferscheinReportFac getLieferscheinReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinReportFac();
	}

	/**
	 * SessionFacade fuer Lieferant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinFac
	 */
	protected final LieferantFac getLieferantFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferantFac();
	}

	protected final LieferantServicesFac getLieferantServicesFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferantServicesFac();
	}

	/**
	 * SessionFacade fuer System Locale holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LocaleFac
	 */
	protected final LocaleFac getLocaleFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLocaleFac();
	}

	/**
	 * SessionFacade fuer System Mandant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return MandantFac
	 */
	protected final MandantFac getMandantFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMandantFac();
	}

	/**
	 * SessionFacade fuer Partner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return PartnerFac
	 */
	protected final PartnerFac getPartnerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerFac();
	}

	protected final PanelFac getPanelFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPanelFac();
	}

	protected final PflegeFac getPflegeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPflegeFac();
	}

	protected final PartnerReportFac getPartnerReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerReportFac();
	}

	protected final PartnerServicesFac getPartnerServicesFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerServicesFac();
	}

	protected final PersonalFac getPersonalFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPersonalFac();
	}

	protected final ProjektFac getProjektFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getProjektFac();
	}

	protected final ProjektServiceFac getProjektServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getProjektServiceFac();
	}

	/**
	 * SessionFacade fuer Stueckliste holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return StuecklisteFac
	 */
	protected final StuecklisteFac getStuecklisteFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getStuecklisteFac();
	}
	
	protected final IntelligenterStklImportFac getIntelligenterStklImportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getIntelligenterStklImportFac();
	}

	/**
	 * SessionFacade fuer ArtikelReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	protected final StuecklisteReportFac getStuecklisteReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getStuecklisteReportFac();
	}

	/**
	 * SessionFacade fuer Reservierung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final ReservierungFac getReservierungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getReservierungFac();
	}

	/**
	 * SessionFacade fuer Bestelltliste holen
	 * 
	 * @return ArtikelbestelltFac
	 * @throws EJBExceptionLP
	 */
	protected final ArtikelbestelltFac getArtikelbestelltFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelbestelltFac();
	}

	/**
	 * SessionFacade fuer Rechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final RechnungFac getRechnungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getRechnungFac();
	}

	/**
	 * SessionFacade fuer RechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungReportFac
	 */
	protected final RechnungReportFac getRechnungReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getRechnungReportFac();
	}

	/**
	 * SessionFacade fuer RechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungServiceFac
	 */
	protected final RechnungServiceFac getRechnungServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getRechnungServiceFac();
	}

	/**
	 * SessionFacade fuer Parameter holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ParameterFac
	 */
	protected final ParameterFac getParameterFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getParameterFac();
	}

	/**
	 * SessionFacade fuer Buchen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final BuchenFac getBuchenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBuchenFac();
	}

	/**
	 * SessionFacade fuer Finanz holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final FinanzFac getFinanzFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFinanzFac();
	}

	/**
	 * SessionFacade fuer Inventur holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final InventurFac getInventurFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getInventurFac();
	}

	/**
	 * SessionFacade fuer FinanzService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzServiceFac
	 */
	protected final FinanzServiceFac getFinanzServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getFinanzServiceFac();
	}

	/**
	 * SessionFacade fuer FinanzReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzReportFac
	 */
	protected final FinanzReportFac getFinanzReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFinanzReportFac();
	}

	/**
	 * SessionFacade fuer Benutzer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final BenutzerFac getBenutzerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBenutzerFac();
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final MahnwesenFac getMahnwesenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMahnwesenFac();
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final BSMahnwesenFac getBSMahnwesenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBSMahnwesenFac();
	}

	protected final AutoFehlmengendruckFac getAutoFehlmengendruckFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoFehlmengendruckFac();
	}

	protected final AutoMahnungsversandFac getAutoMahnungsversandFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoMahnungsversandFac();
	}

	protected final AutoMahnenFac getAutoMahnenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoMahnenFac();
	}

	protected final AutoBestellvorschlagFac getAutoBestellvorschlagFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoBestellvorschlagFac();
	}

	protected final AutoPaternosterFac getAutoPaternosterFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoPaternosterFac();
	}

	protected final BestellungReportFac getBestellungReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellungReportFac();
	}

	/**
	 * SessionFacade fuer TheJudge holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final TheJudgeFac getTheJudgeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getTheJudgeFac();
	}

	/**
	 * SessionFacade fuer TheClient holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final TheClientFac getTheClientFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getTheClientFac();
	}

	/**
	 * SessionFacade fuer SystemMultilanguage holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final SystemMultilanguageFac getSystemMultilanguageFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemMultilanguageFac();
	}

	/**
	 * Primary Key Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final PKGeneratorObj getPKGeneratorObj() {
		return oFacadeBeauftragter.getPKGeneratorObj();
	}

	/**
	 * Belegnummern Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final BelegnummerGeneratorObj getBelegnummerGeneratorObj() {
		return oFacadeBeauftragter.getBelegnummerGeneratorObj();
	}

	/**
	 * SessionFacade fuer System holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final SystemFac getSystemFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemFac();
	}

	protected final SystemReportFac getSystemReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemReportFac();
	}

	protected final SystemServicesFac getSystemServicesFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemServicesFac();
	}

	/**
	 * SessionFacade fuer VkPreisfindung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final VkPreisfindungFac getVkPreisfindungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getVkPreisfindungFac();
	}

	/**
	 * SessionFacade fuer Eingangsrechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungFac
	 */
	protected final EingangsrechnungFac getEingangsrechnungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getEingangsrechnungFac();
	}

	/**
	 * SessionFacade fuer EingangsrechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungReportFac
	 */
	protected final EingangsrechnungReportFac getEingangsrechnungReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getEingangsrechnungReportFac();
	}

	/**
	 * SessionFacade fuer EingangsrechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungServiceFac
	 */
	protected final EingangsrechnungServiceFac getEingangsrechnungServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getEingangsrechnungServiceFac();
	}

	/**
	 * SessionFacade fuer Rechte holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechteFac
	 */
	protected final RechteFac getRechteFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getRechteFac();
	}

	/**
	 * SessionFacade fuer Zeiterfassung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ZeiterfassungFac
	 */
	protected final ZeiterfassungFac getZeiterfassungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getZeiterfassungFac();
	}

	protected final MaschineFac getMaschineFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMaschineFac();
	}

	protected final ZeiterfassungReportFac getZeiterfassungReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getZeiterfassungReportFac();
	}

	/**
	 * SessionFacade fuer Zeiterfassung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ZeiterfassungFac
	 */
	protected final ZutrittscontrollerFac getZutrittscontrollerFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getZutrittscontrollerFac();
	}

	/**
	 * SessionFacade fuer Versand holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	protected final VersandFac getVersandFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getVersandFac();
	}

	/**
	 * SessionFacade fuer Bank holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	protected final BankFac getBankFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBankFac();
	}

	/**
	 * SessionFacade fuer Material holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	protected final MaterialFac getMaterialFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMaterialFac();
	}

	protected final MediaFac getMediaFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMediaFac();
	}

	protected final AngebotFac getAngebotFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotFac();
	}

	protected final AngebotpositionFac getAngebotpositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotpositionFac();
	}

	protected final AngebotReportFac getAngebotReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotReportFac();
	}

	protected final AngebotServiceFac getAngebotServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotServiceFac();
	}

	protected final BenutzerServicesFacLocal getBenutzerServicesFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBenutzerServicesFac();
	}

	protected final FastLaneReader getFastLaneReader() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFastLaneReader();
	}

	protected final FertigungFac getFertigungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungFac();
	}

	protected final FertigungReportFac getFertigungReportFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungReportFac();
	}

	protected final FertigungServiceFac getFertigungServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungServiceFac();
	}

	protected final FehlmengeFac getFehlmengeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFehlmengeFac();
	}

	protected final InternebestellungFac getInternebestellungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getInternebestellungFac();
	}

	protected final AngebotstklFac getAngebotstklFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotstklFac();
	}

	protected final AngebotstklpositionFac getAngebotstklpositionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotstklpositionFac();
	}

	protected final InseratFac getInseratFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getInseratFac();
	}

	protected final FibuExportFac getFibuExportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFibuExportFac();
	}

	protected final DruckerFac getDruckerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getDruckerFac();
	}

	protected final DokumenteFac getDokumenteFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getDokumenteFac();
	}

	protected final KundesokoFac getKundesokoFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getKundesokoFac();
	}

	protected final BelegbuchungFac getBelegbuchungFac(String mandantCNr)
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBelegbuchungFac(mandantCNr);
	}

	protected final ZahlungsvorschlagFac getZahlungsvorschlagFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getZahlungsvorschlagFac();
	}

	protected final BelegpositionkonvertierungFac getBelegpositionkonvertierungFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBelegpositionkonvertierungFac();
	}

	protected final RahmenbedarfeFac getRahmenbedarfeFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getRahmenbedarfeFac();
	}

	protected final AutoRahmendetailbedarfdruckFac getAutoRahmendetailbedarfdruckFac() {
		return oFacadeBeauftragter.getAutoRahmendetailbedarfdruckFac();
	}

	protected final AutomatikjobFac getAutomatikjobFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutomatikjobFac();
	}

	protected final AutomatiktimerFac getAutomatiktimerFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutomatiktimerFac();
	}

	protected final AutomatikjobtypeFac getAutomatikjobtypeFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutomatikjobtypeFac();
	}

	protected final LogonFac getLogonFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLogonFac();
	}

	protected final JCRDocFac getJCRDocFac() {
		return oFacadeBeauftragter.getJCRDocFac();
	}

	protected final BelegVerkaufFac getBelegVerkaufFac() {
		return oFacadeBeauftragter.getBelegVerkaufFac();
	}

	protected final AuftragRahmenAbrufFac getAuftragRahmenAbrufFac() {
		return oFacadeBeauftragter.getAuftragRahmenAbrufFac();
	}

	/**
	 * exccatch: Pruefe ob in RemoteException eine EJBExceptionLP enthaelt, wenn
	 * ja dann throw diese, sonst die RemoteException.
	 * 
	 * @param reI
	 *            int
	 * @throws EJBExceptionLP
	 */
	final protected void throwEJBExceptionLPRespectOld(RemoteException reI)
			throws EJBExceptionLP {
		Throwable t = reI.detail;
		if (t != null && t instanceof EJBExceptionLP) {
			throw new EJBExceptionLP((EJBExceptionLP) t);
		} else {
			throw new EJBExceptionLP(reI);
		}
	}

	final protected void throwEJBExceptionLPforPersistence(
			PersistenceException peI) throws EJBExceptionLP {
		EJBExceptionLP en = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SQL_EXCEPTION_MIT_INFO,
				"Eine Persistence Exception ist aufgetreten. Infos erhalten Sie in der SQLException");
		if ((Throwable) peI.getCause() instanceof DataException) {
			DataException ed = (DataException) peI.getCause();
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add(ed.getSQLException().getMessage().toString());
			alInfo.add(ed.getSQLException().getNextException().getMessage()
					.toString());
			en.setAlInfoForTheClient(alInfo);
		} else {
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add(peI.getCause().getMessage());
			alInfo.add(peI.getCause().getStackTrace().toString());
		}
		throw en;
	}

	/**
	 * 
	 * Einen Betrag mit einem Helium V Wechselkurs multiplizieren. <br>
	 * Es gilt: Die Rundung fuer das Produkt aus Betrag und Wechselkurs in
	 * Helium V muss einheitlich sein.
	 * 
	 * @param nBetragI
	 *            der Betrag
	 * @param bdWechselkursI
	 *            der Wechselkurs
	 * @return BigDecimal der berechnete Betrag
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	protected BigDecimal getBetragMalWechselkurs(BigDecimal nBetragI,
			BigDecimal bdWechselkursI) throws EJBExceptionLP {
		if (nBetragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nBetragI == null"));
		}

		if (bdWechselkursI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdWechselkursI == null"));
		}

		// UW 08.03.06 Der Wechselkurs wird immer 6-stellig betrachtet
		BigDecimal nWechselkurs = Helper.rundeKaufmaennisch(bdWechselkursI,
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);

		BigDecimal nBetrag = nBetragI;
		nBetrag = nBetrag.multiply(nWechselkurs);
		nBetrag = Helper.rundeKaufmaennisch(nBetrag, 4);
		checkNumberFormat(nBetrag);

		return nBetrag;
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr,
			Locale loI) throws EJBExceptionLP {
		return getBenutzerServicesFac().getTextRespectUISpr(sTokenI,
				mandantCNr, loI);
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr,
			Locale loI, Object... replacements) throws EJBExceptionLP {
		String msg = getBenutzerServicesFac().getTextRespectUISpr(sTokenI,
				mandantCNr, loI);
		return MessageFormat.format(msg, replacements);
	}

	public String getBriefanredeNeutralOderPrivatperson(Integer partnerIId,
			Locale locDruck, TheClientDto theClientDto) {

		PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId,
				theClientDto);

		if (pDto.getAnredeCNr() != null
				&& (pDto.getAnredeCNr().equals(PartnerFac.PARTNER_ANREDE_HERR)
						|| pDto.getAnredeCNr().equals(
								PartnerFac.PARTNER_ANREDE_FRAU) || pDto
						.getAnredeCNr().equals(
								PartnerFac.PARTNER_ANREDE_FAMILIE))) {
			try {
				return getPartnerFac().formatBriefAnrede(pDto, locDruck,
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;
			}
		} else {
			return getTextRespectUISpr("lp.anrede.sehrgeehrtedamenundherren",
					theClientDto.getMandant(), locDruck);
		}

	}

	/**
	 * Die Kundendaten fuer Reports wie AB, RE, BS, etc formatieren nach der
	 * oesterreicheischen postvorschrift: Zeile 1: Name 1 Zeile 2: Name 2 Zeile
	 * 3: Abteilung Zeile 4: Ansprechpartner Zeile 5: Strasse / Postfach Zeile
	 * 6: PLZ-Ort Zeile 7: Land (in Blockbuchstaben) es gibt keine Leerzeilen.
	 * Der Adressblock ist am Report 7-zeilig darzustellen, damit sich nach
	 * unten nichts verschiebt
	 * 
	 * @return String
	 * @param partnerDto
	 *            PartnerDto
	 * @param anspDto
	 *            AnsprechpartnerDto
	 * @param mandantDto
	 *            MandantDto
	 * @param locale
	 *            Locale
	 * @throws EJBExceptionLP
	 */
	public String formatAdresseFuerAusdruckOld(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale)
			throws EJBExceptionLP {
		StringBuffer sbAdressblock = new StringBuffer("");
		try {
			String[] sZeilen = new String[7];
			if (partnerDto != null) {
				String sAnredeCNr = partnerDto.getAnredeCNr();
				String titel = "";
				if (partnerDto.getCTitel() != null) {
					titel = partnerDto.getCTitel() + " ";
				}

				String ntitel = "";
				if (partnerDto.getCNtitel() != null) {
					ntitel = " " + partnerDto.getCNtitel();
				}
				if (sAnredeCNr != null
						&& !sAnredeCNr.equals("")
						&& (sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FRAU) || sAnredeCNr
								.equals(PartnerFac.PARTNER_ANREDE_HERR))) {

					if (titel != null && titel.length() > 0) {
						if (partnerDto.getCName2vornamefirmazeile2() != null) {
							sZeilen[0] = titel
									+ partnerDto.getCName2vornamefirmazeile2();
						} else {
							sZeilen[0] = partnerDto
									.getCName2vornamefirmazeile2();
						}

					} else {
						sZeilen[0] = partnerDto.getCName2vornamefirmazeile2();
					}

					if (partnerDto.getCName3vorname2abteilung() != null) {

						if (sZeilen[0] != null) {

							sZeilen[0] += " "
									+ partnerDto.getCName3vorname2abteilung();
						} else {
							sZeilen[0] = partnerDto
									.getCName3vorname2abteilung();
						}
					}
					if (sZeilen[0] != null) {
						sZeilen[0] += " "
								+ partnerDto.getCName1nachnamefirmazeile1()
								+ ntitel;
					} else {
						sZeilen[0] = partnerDto.getCName1nachnamefirmazeile1()
								+ ntitel;
					}
				} else {
					// Firma
					sZeilen[0] = titel
							+ partnerDto.getCName1nachnamefirmazeile1()
							+ ntitel;
					sZeilen[1] = partnerDto.getCName2vornamefirmazeile2();
					sZeilen[2] = partnerDto.getCName3vorname2abteilung();
				}

				if (partnerDto.getCPostfach() != null
						&& partnerDto.getCPostfach().trim().length() > 0) {
					String s = getTextRespectUISpr("lp.postfach",
							mandantDto.getCNr(), locale)
							+ " " + partnerDto.getCPostfach();
					sZeilen[4] = s;
				} else {
					// sonst Strasse
					if (partnerDto.getCStrasse() != null) {
						// Strasse
						sZeilen[4] = partnerDto.getCStrasse();
					}
				}
				// Postfach - LandPLZOrt (wenn vorhanden)
				if (partnerDto.getLandplzortDto_Postfach() != null) {
					sZeilen[5] = partnerDto.getLandplzortDto_Postfach()
							.formatPlzOrt();
					if (partnerDto.getLandplzortDto_Postfach().getLandDto() != null) {
						// das Land nur drucken, wenn es nicht dem Land des
						// Mandanten entspricht
						if (mandantDto == null
								|| mandantDto.getPartnerDto()
										.getLandplzortDto() == null
								|| mandantDto.getPartnerDto()
										.getLandplzortDto().getLandDto() == null
								|| !mandantDto
										.getPartnerDto()
										.getLandplzortDto()
										.getLandDto()
										.getIID()
										.equals(partnerDto
												.getLandplzortDto_Postfach()
												.getLandDto().getIID())) {
							sZeilen[6] = partnerDto.getLandplzortDto_Postfach()
									.getLandDto().getCName().toUpperCase();
						}
					}
				}
				// sonst den "normalen" Ort
				else if (partnerDto.getLandplzortDto() != null) {
					sZeilen[5] = partnerDto.getLandplzortDto().formatPlzOrt();
					if (partnerDto.getLandplzortDto().getLandDto() != null) {

						LandDto landDto = null;
						ParametermandantDto parametermandantDto = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_POSTVERSENDUNGSLAND);
						if (parametermandantDto.getCWert() != null)
							;
						if (!parametermandantDto.getCWert().equals("")) {
							landDto = getSystemFac().landFindByLkz(
									parametermandantDto.getCWert());
						}
						if (landDto != null) {
							if (!landDto.getIID().equals(
									partnerDto.getLandplzortDto().getLandDto()
											.getIID()))
								sZeilen[6] = partnerDto.getLandplzortDto()
										.getLandDto().getCName().toUpperCase();
						} else {
							// das Land nur drucken, wenn es nicht dem Land des
							// Mandanten entspricht
							if (mandantDto == null
									|| mandantDto.getPartnerDto()
											.getLandplzortDto() == null
									|| mandantDto.getPartnerDto()
											.getLandplzortDto().getLandDto() == null
									|| !mandantDto
											.getPartnerDto()
											.getLandplzortDto()
											.getLandDto()
											.getIID()
											.equals(partnerDto
													.getLandplzortDto()
													.getLandDto().getIID())) {
								sZeilen[6] = partnerDto.getLandplzortDto()
										.getLandDto().getCName().toUpperCase();
							}
						}
					}
				}
			}
			if (anspDto != null) {
				sZeilen[3] = getPartnerFac()
						.formatFixAnredeTitelName2Name1FuerAdresskopf(
								anspDto.getPartnerDto(), locale, null);
			}
			for (int i = 0; i < sZeilen.length; i++) {
				if (sZeilen[i] != null && !sZeilen[i].equals("")) {
					sbAdressblock.append(sZeilen[i] + "\n");
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sbAdressblock.toString();
	}

	private boolean isAnredePerson(String sAnredeCNr) {

		// 2012-10-22 WH: Wenn Anrede vorhandenm welche nicht 'Firma' ist, dann
		// ist es eine Person

		if (sAnredeCNr != null
				&& !PartnerFac.PARTNER_ANREDE_FIRMA.equals(sAnredeCNr)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean sindAnsprechpartnerGleich(Integer ansprechpartnerIId1,
			Integer ansprechpartnerIId2) {
		boolean nAnsprechpartnerLieferadresseSindGleich = false;
		if (ansprechpartnerIId1 == null && ansprechpartnerIId2 == null) {
			nAnsprechpartnerLieferadresseSindGleich = true;
		} else if (ansprechpartnerIId1 == null && ansprechpartnerIId2 != null) {
			nAnsprechpartnerLieferadresseSindGleich = false;
		} else if (ansprechpartnerIId1 != null && ansprechpartnerIId2 == null) {
			nAnsprechpartnerLieferadresseSindGleich = false;
		} else if (ansprechpartnerIId1 != null && ansprechpartnerIId2 != null
				&& ansprechpartnerIId1.equals(ansprechpartnerIId2)) {
			nAnsprechpartnerLieferadresseSindGleich = true;
		}
		return nAnsprechpartnerLieferadresseSindGleich;
	}

	private String getTrimmed(String value) {
		if (null == value)
			return "";
		return value.trim();
	}

	private String getBlankSuffixed(String value) {
		if (null == value)
			return "";
		if (value.trim().length() == 0)
			return "";
		return value.trim() + " ";
	}

	private String getBlankPrefixed(String value) {
		if (null == value)
			return "";
		if (value.trim().length() == 0)
			return "";
		return " " + value.trim();
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto,
				locale, false, null,false);
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale,
			String belegartCNr,boolean postfachIgnorieren) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto,
				locale, false, belegartCNr,postfachIgnorieren);
	}
	
	public String formatAdresseFuerAusdruck(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale,
			String belegartCNr) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto,
				locale, false, belegartCNr,false);
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto,
			AnsprechpartnerDto anspDto, MandantDto mandantDto, Locale locale,
			boolean bLandImmerAnhaengen, String belegartCNr, boolean postfachIgnorieren) {
		StringBuffer sbAdressblock = new StringBuffer("");
		try {
			String[] sZeilen = new String[7];
			if (partnerDto != null) {
				String sAnredeCNr = partnerDto.getAnredeCNr();
				String titel = getTrimmed(partnerDto.getCTitel());
				String ntitel = getTrimmed(partnerDto.getCNtitel());

				if (isAnredePerson(sAnredeCNr)) {
					// SP629
					ParametermandantDto parametermandantDto = getParameterFac()
							.getMandantparameter(
									mandantDto.getCNr(),
									ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.PARAMETER_ADRESSE_FUER_AUSDRUCK_MIT_ANREDE);

					if (((Boolean) parametermandantDto.getCWertAsObject()) == true
							&& sAnredeCNr != null) {

						String ret = partnerDto.getAnredeCNr().trim();

						if (locale != null) {
							AnredesprDto anredesprDto = getPartnerFac()
									.anredesprFindByAnredeCNrLocaleCNrOhneExc(
											sAnredeCNr,
											Helper.locale2String(locale));
							if (anredesprDto != null) {
								ret = anredesprDto.getCBez().trim();
							}
						}

						sZeilen[0] = ret;
						sZeilen[1] = getBlankSuffixed(titel)
								+ getBlankSuffixed(partnerDto
										.getCName2vornamefirmazeile2())
								+ getBlankSuffixed(partnerDto
										.getCName1nachnamefirmazeile1())
								+ ntitel;

						sZeilen[2] = getTrimmed(partnerDto
								.getCName3vorname2abteilung());
					} else {
						sZeilen[0] = getBlankSuffixed(titel)
								+ getBlankSuffixed(partnerDto
										.getCName2vornamefirmazeile2())
								+ getBlankSuffixed(partnerDto
										.getCName1nachnamefirmazeile1())
								+ ntitel;

						sZeilen[2] = getTrimmed(partnerDto
								.getCName3vorname2abteilung());
					}

				} else {
					// Firma
					sZeilen[0] = getBlankSuffixed(titel)
							+ getBlankSuffixed(partnerDto
									.getCName1nachnamefirmazeile1()) + ntitel;
					sZeilen[1] = partnerDto.getCName2vornamefirmazeile2();

					if (anspDto != null && anspDto.getCAbteilung() != null) {
						sZeilen[2] = anspDto.getCAbteilung();
					} else {
						sZeilen[2] = partnerDto.getCName3vorname2abteilung();
					}

				}

				String postfach = getTrimmed(partnerDto.getCPostfach());
				//PJ18752
				if (postfach.length() > 0 && postfachIgnorieren == false) {
					sZeilen[4] = getBlankSuffixed(getTextRespectUISpr(
							"lp.postfach", mandantDto.getCNr(), locale))
							+ partnerDto.getCPostfach();
				} else {
					// sonst Strasse
					sZeilen[4] = getTrimmed(partnerDto.getCStrasse());
				}

				// Postfach - LandPLZOrt (wenn vorhanden)
				if (partnerDto.getLandplzortDto_Postfach() != null) {
					sZeilen[5] = partnerDto.getLandplzortDto_Postfach()
							.formatPlzOrt();
					if (partnerDto.getLandplzortDto_Postfach().getLandDto() != null) {
						// das Land nur drucken, wenn es nicht dem Land des
						// Mandanten entspricht
						if (mandantDto == null
								|| mandantDto.getPartnerDto()
										.getLandplzortDto() == null
								|| mandantDto.getPartnerDto()
										.getLandplzortDto().getLandDto() == null
								|| !mandantDto
										.getPartnerDto()
										.getLandplzortDto()
										.getLandDto()
										.getIID()
										.equals(partnerDto
												.getLandplzortDto_Postfach()
												.getLandDto().getIID())
								|| bLandImmerAnhaengen) {
							sZeilen[6] = partnerDto.getLandplzortDto_Postfach()
									.getLandDto().getCName().toUpperCase();

							// Land entfernen, wenn gemeinsames Postland
							if (bLandImmerAnhaengen == false
									&& mandantDto != null
									&& mandantDto.getPartnerDto()
											.getLandplzortDto() != null
									&& mandantDto.getPartnerDto()
											.getLandplzortDto().getLandDto() != null

									&& mandantDto.getPartnerDto()
											.getLandplzortDto().getLandDto()
											.getLandIIdGemeinsamespostland() != null

									&& mandantDto
											.getPartnerDto()
											.getLandplzortDto()
											.getLandDto()
											.getLandIIdGemeinsamespostland()
											.equals(partnerDto
													.getLandplzortDto_Postfach()
													.getLandDto().getIID())) {
								sZeilen[6] = null;
							}

						}
					}
				}
				// sonst den "normalen" Ort
				else if (partnerDto.getLandplzortDto() != null) {
					sZeilen[5] = partnerDto.getLandplzortDto().formatPlzOrt();
					if (partnerDto.getLandplzortDto().getLandDto() != null) {

						LandDto landDto = null;
						ParametermandantDto parametermandantDto = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_POSTVERSENDUNGSLAND);
						if (parametermandantDto.getCWert() != null)
							;
						if (!parametermandantDto.getCWert().equals("")) {
							landDto = getSystemFac().landFindByLkz(
									parametermandantDto.getCWert());
						}
						if (landDto != null) {
							if (!landDto.getIID().equals(
									partnerDto.getLandplzortDto().getLandDto()
											.getIID()))
								sZeilen[6] = partnerDto.getLandplzortDto()
										.getLandDto().getCName().toUpperCase();
						} else {
							// das Land nur drucken, wenn es nicht dem Land des
							// Mandanten entspricht
							if (mandantDto == null
									|| mandantDto.getPartnerDto()
											.getLandplzortDto() == null
									|| mandantDto.getPartnerDto()
											.getLandplzortDto().getLandDto() == null
									|| !mandantDto
											.getPartnerDto()
											.getLandplzortDto()
											.getLandDto()
											.getIID()
											.equals(partnerDto
													.getLandplzortDto()
													.getLandDto().getIID())
									|| bLandImmerAnhaengen) {

								// SP581 das Land nicht andrucken, wenn es das
								// gemeinsame Postland des Mandanten-Landes

								sZeilen[6] = partnerDto.getLandplzortDto()
										.getLandDto().getCName().toUpperCase();

								// Land entfernen, wenn gemeinsames Postland
								if (mandantDto != null
										&& mandantDto.getPartnerDto() != null
										&& mandantDto.getPartnerDto()
												.getLandplzortDto() != null
										&& mandantDto
												.getPartnerDto()
												.getLandplzortDto()
												.getLandDto()
												.getLandIIdGemeinsamespostland() != null) {

									if (bLandImmerAnhaengen == false
											&& mandantDto != null
											&& mandantDto.getPartnerDto()
													.getLandplzortDto() != null
											&& mandantDto.getPartnerDto()
													.getLandplzortDto()
													.getLandDto() != null
											&& mandantDto
													.getPartnerDto()
													.getLandplzortDto()
													.getLandDto()
													.getLandIIdGemeinsamespostland()
													.equals(partnerDto
															.getLandplzortDto()
															.getLandDto()
															.getIID())) {
										sZeilen[6] = null;
									}

									if (bLandImmerAnhaengen == false
											&& partnerDto
													.getLandplzortDto()
													.getLandDto()
													.getLandIIdGemeinsamespostland() != null
											&& mandantDto != null
											&& mandantDto.getPartnerDto()
													.getLandplzortDto() != null
											&& mandantDto.getPartnerDto()
													.getLandplzortDto()
													.getLandDto() != null
											&& mandantDto
													.getPartnerDto()
													.getLandplzortDto()
													.getLandDto()
													.getIID()
													.equals(partnerDto
															.getLandplzortDto()
															.getLandDto()
															.getIID())) {
										sZeilen[6] = null;
									}

								}

							}
						}
					}
				}
			}

			if (anspDto != null) {

				boolean bAnsprechpartnerAndrucken = true;
				if (belegartCNr != null) {
					if (belegartCNr.equals(LocaleFac.BELEGART_ANFRAGE)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_ANFRAGE,
										ParameterFac.PARAMETER_ANFRAGE_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_AUFTRAG,
										ParameterFac.PARAMETER_AUFTRAG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					} else if (belegartCNr
							.equals(LocaleFac.BELEGART_BESTELLUNG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_BESTELLUNG,
										ParameterFac.PARAMETER_BESTELLUNG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_ANGEBOT,
										ParameterFac.PARAMETER_ANGEBOT_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					} else if (belegartCNr
							.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_LIEFERSCHEIN,
										ParameterFac.PARAMETER_LIEFERSCHEIN_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac()
								.getMandantparameter(
										mandantDto.getCNr(),
										ParameterFac.KATEGORIE_RECHNUNG,
										ParameterFac.PARAMETER_RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken
								.getCWertAsObject();
					}

				}

				if (bAnsprechpartnerAndrucken == true) {
					sZeilen[3] = getPartnerFac()
							.formatFixAnredeTitelName2Name1FuerAdresskopf(
									anspDto.getPartnerDto(), locale, null);
				}

			}
			for (int i = 0; i < sZeilen.length; i++) {
				if (sZeilen[i] != null && sZeilen[i].length() > 0) {
					sbAdressblock.append(sZeilen[i] + "\n");
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sbAdressblock.toString();
	}

	/**
	 * Hibernate-Session sauber beenden.
	 * 
	 * @todo bei Gelegenheit quersanieren.
	 * 
	 * @param session
	 *            Session
	 * @throws EJBExceptionLP
	 */
	public final static void closeSession(Session session)
			throws EJBExceptionLP {
		if (session != null) {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
	}

	protected final void pruefePflichtfelderBelegpositionDto(
			BelegpositionDto belegPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (belegPosDto.getPositionsartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegPosDto.getPositionsartCNr() == null"));
		}
		if (belegPosDto.getBelegIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegPosDto.getBelegIId() == null"));
		}

		if (belegPosDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)) {
			if (belegPosDto.getArtikelIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getArtikelIId() == null"));
			}
		}
		if (belegPosDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)
				|| belegPosDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (belegPosDto.getNMenge() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNMenge() == null"));
			}

			if (belegPosDto.getEinheitCNr() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getEinheitCNr() == null"));
			}
		} else if (belegPosDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_TEXTEINGABE)) {

			if (belegPosDto.getXTextinhalt() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getXTextinhalt() == null"));
			}
		} else if (belegPosDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {

			if (belegPosDto.getMediastandardIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"belegPosDto.getMediastandardIId() == null"));
			}
		}

	}

	// PJ18659
	protected LPDatenSubreport getSubreportLiefergruppenTexte(
			BelegpositionDto[] belegpositionDtos, TheClientDto theClientDto) {

		String[] fieldnames = new String[] { "F_LIEFERGRUPPE", "F_TEXT" };
		HashSet hsBereitsGefunden = new HashSet();

		ArrayList alDaten = new ArrayList();

		for (int i = 0; i < belegpositionDtos.length; i++) {

			if (belegpositionDtos[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						belegpositionDtos[i].getArtikelIId(), theClientDto);
				if (aDto.getLfliefergruppeIId() != null) {

					if (!hsBereitsGefunden
							.contains(aDto.getLfliefergruppeIId())) {

						LfliefergruppeDto lflieferguppeDto = getLieferantServicesFac()
								.lfliefergruppeFindByPrimaryKey(
										aDto.getLfliefergruppeIId(),
										theClientDto);
						if (lflieferguppeDto.getLfliefergruppesprDto() != null
								&& lflieferguppeDto.getLfliefergruppesprDto()
										.getXText() != null && lflieferguppeDto.getLfliefergruppesprDto()
												.getXText().length()>0) {

							Object[] oZeile = new Object[2];

							if (lflieferguppeDto.getLfliefergruppesprDto()
									.getCBez() != null) {
								oZeile[0] = lflieferguppeDto
										.getLfliefergruppesprDto().getCBez();
							} else {
								oZeile[0] = lflieferguppeDto.getCNr();

							}

							oZeile[1] = lflieferguppeDto
									.getLfliefergruppesprDto().getXText();
							alDaten.add(oZeile);
							hsBereitsGefunden.add(aDto.getLfliefergruppeIId());
						}

					}

				}
			}

		}

		Object[][] dataSub = new Object[alDaten.size()][fieldnames.length];
		dataSub = (Object[][]) alDaten.toArray(dataSub);

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	protected BelegpositionVerkaufDto befuellePreisfelderAnhandVKPreisfindung(
			BelegpositionVerkaufDto bvDto, java.sql.Timestamp tBelegdatum,
			Integer kundeIId, String waehrungCNr, TheClientDto theClientDto) {

		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
				theClientDto);

		try {

			if (bvDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
					|| bvDto.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_HANDEINGABE)) {

				bvDto.setNNettoeinzelpreis(new BigDecimal(0));
				bvDto.setNBruttoeinzelpreis(new BigDecimal(0));
				bvDto.setNEinzelpreis(new BigDecimal(0));
				bvDto.setFRabattsatz(0D);
				bvDto.setNMaterialzuschlag(new BigDecimal(0));
				bvDto.setFZusatzrabattsatz(0D);
				bvDto.setNRabattbetrag(new BigDecimal(0));
				bvDto.setNMwstbetrag(new BigDecimal(0));

				if (bvDto.getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

					Integer mwstsatzbezIId = null;
					ParametermandantDto parameterPositionskontierung = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_KUNDEN,
									ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
					boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
							.getCWertAsObject();

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									bvDto.getArtikelIId(), theClientDto);

					if (bDefaultMwstsatzAusArtikel) {

						mwstsatzbezIId = aDto.getMwstsatzbezIId();
					} else {
						mwstsatzbezIId = kdDto.getMwstsatzbezIId();
					}

					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									mwstsatzbezIId, theClientDto);
					bvDto.setMwstsatzIId(mwstsatzDto.getIId());
					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
							.verkaufspreisfindung(
									bvDto.getArtikelIId(),
									kundeIId,
									bvDto.getNMenge(),
									new java.sql.Date(tBelegdatum.getTime()),
									kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
									mwstsatzDto.getIId(), waehrungCNr, true,
									theClientDto);

					VerkaufspreisDto verkaufspreisDtoInZielwaehrung = Helper
							.getVkpreisBerechnet(vkpreisfindungDto);

					if (verkaufspreisDtoInZielwaehrung != null) {
						bvDto.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
						bvDto.setNBruttoeinzelpreis(verkaufspreisDtoInZielwaehrung.bruttopreis);
						bvDto.setNEinzelpreis(verkaufspreisDtoInZielwaehrung.einzelpreis);
						bvDto.setFRabattsatz(verkaufspreisDtoInZielwaehrung.rabattsatz);
						bvDto.setNMaterialzuschlag(verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag);
						bvDto.setFZusatzrabattsatz(verkaufspreisDtoInZielwaehrung
								.getDdZusatzrabattsatz());

						BigDecimal mwstBetrag = Helper.getProzentWert(
								verkaufspreisDtoInZielwaehrung.nettopreis,
								new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
						bvDto.setNMwstbetrag(mwstBetrag);

						BigDecimal bdRabattbetrag = bvDto
								.getNEinzelpreis()
								.multiply(
										new BigDecimal(bvDto.getFRabattsatz()
												.doubleValue()))
								.movePointLeft(2);
						bvDto.setNRabattbetrag(bdRabattbetrag);

					}

				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);

		}
		return bvDto;
	}

	protected final void pruefePflichtfelderBelegpositionDtoVerkauf(
			BelegpositionVerkaufDto belegPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		pruefePflichtfelderBelegpositionDto(belegPosDto, theClientDto);

		if (belegPosDto.getBRabattsatzuebersteuert() == null) {
			belegPosDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		}
		if (belegPosDto.getBMwstsatzuebersteuert() == null) {
			belegPosDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		}

		if (belegPosDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)
				|| belegPosDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (belegPosDto.getMwstsatzIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getMwstsatzIId() == null"));
			}

			// Verkaufspreise pruefen
			if (belegPosDto.getNNettoeinzelpreis() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"belegPosDto.getNNettoeinzelpreis() == null"));
			}

			/*
			 * if (belegPosDto.getNNettoeinzelpreisplusversteckteraufschlag() ==
			 * null) { throw new
			 * EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
			 * new Exception(
			 * "belegPosDto.getNNettoeinzelpreisplusversteckteraufschlag() == null"
			 * )); }
			 */
			/*
			 * if (belegPosDto.getNEinzelpreisplusversteckteraufschlag() ==
			 * null) { throw new
			 * EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
			 * new Exception(
			 * "belegPosDto.getNEinzelpreisplusversteckteraufschlag() == null"
			 * )); }
			 */

			if (belegPosDto.getNBruttoeinzelpreis() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"belegPosDto.getNBruttoeinzelpreis() == null"));
			}
			if (belegPosDto.getNEinzelpreis() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNEinzelpreis() == null"));
			}

			if (belegPosDto.getFRabattsatz() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getFRabattsatz() == null"));
			}
			if (belegPosDto.getFZusatzrabattsatz() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"belegPosDto.getFZusatzrabattsatz() == null"));
			}
		}
	}

	protected String extractLaufendeNummerAusBelegnummer(String sBelegnummer,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			ParametermandantDto pTrennzeichen = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
			StringTokenizer st = new StringTokenizer(sBelegnummer,
					pTrennzeichen.getCWert());
			if (st.hasMoreTokens()) {
				st.nextToken();
			}
			String sBelegnummer2;
			if (st.hasMoreTokens()) {
				sBelegnummer2 = st.nextToken();
			} else {
				sBelegnummer2 = "";
			}
			return sBelegnummer2;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected BelegpositionVerkaufDto befuellepositionBelegpositionDtoVerkauf(
			BelegpositionVerkaufDto vorherige,
			BelegpositionVerkaufDto aktuelle, TheClientDto theClientDto) {

		if (vorherige != null) {
			if (vorherige.getPositioniId() != null) {
				if (vorherige.getTypCNr() != null) {
					if (vorherige.getPositioniId() != null) {
						if (aktuelle.getPositionsartCNr().equalsIgnoreCase(
								LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							if (aktuelle.getCZusatzbez().equals(
									LocaleFac.POSITIONBEZ_BEGINN)) {

							} else if (aktuelle.getCZusatzbez().equals(
									LocaleFac.POSITIONBEZ_ENDE)) {
								BelegpositionVerkaufDto posDto = getPositionZuBelegpositionDtoVerkauf(
										vorherige, theClientDto);
								if (vorherige
										.getPositionsartCNr()
										.equalsIgnoreCase(
												LocaleFac.POSITIONSART_POSITION)) {
									if (vorherige.getTypCNr().equals(
											LocaleFac.POSITIONTYP_EBENE2)) {
										aktuelle.setPositioniId(null);
										aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
									}
								} else if (vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									aktuelle.setPositioniId(posDto
											.getPositioniId());
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
								} else if (vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE2)) {
									aktuelle.setPositioniId(posDto
											.getPositioniId());
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
								}
							} else {
								if (vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
									aktuelle.setPositioniId(null);
								} else if (vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE2)) {
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
								}
							}
						} else if (vorherige.getTypCNr().equals(
								LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						} else if (vorherige.getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getCZusatzbez().equals(
										LocaleFac.POSITIONBEZ_BEGINN)) {
							aktuelle.setPositioniId(vorherige.getIId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (vorherige.getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getCZusatzbez().equals(
										LocaleFac.POSITIONBEZ_ENDE)) {
							BelegpositionVerkaufDto posDto = getPositionZuBelegpositionDtoVerkauf(
									vorherige, theClientDto);
							aktuelle.setPositioniId(posDto.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						} else if (vorherige.getTypCNr().equals(
								LocaleFac.POSITIONTYP_EBENE2)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (aktuelle.getPositionsartCNr()
								.equalsIgnoreCase(
										LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE2)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (aktuelle.getPositionsartCNr()
								.equalsIgnoreCase(
										LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getTypCNr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					} else {
						if (aktuelle.getPositionsartCNr().equalsIgnoreCase(
								LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_ALLES);
						} else if (vorherige.getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getIId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					}
				} else {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(
							LocaleFac.POSITIONSART_POSITION)) {
						aktuelle.setPositioniId(vorherige.getIId());
						aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					} else if (vorherige.getTypCNr() != null) {
						if (vorherige.getTypCNr().equalsIgnoreCase(
								LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					}
				}
			} else {
				if (aktuelle.getPositionsartCNr().equalsIgnoreCase(
						LocaleFac.POSITIONSART_POSITION)) {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(
							LocaleFac.POSITIONSART_POSITION)) {
						if (vorherige.getCZusatzbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {

						} else {
							aktuelle.setPositioniId(vorherige.getIId());
						}
					} else {

					}
				} else {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(
							LocaleFac.POSITIONSART_POSITION)
							&& vorherige.getCZusatzbez().equals(
									LocaleFac.POSITIONBEZ_ENDE)) {
					} else if (vorherige.getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_POSITION)
							&& vorherige.getCZusatzbez().equals(
									LocaleFac.POSITIONBEZ_BEGINN)) {
						aktuelle.setPositioniId(vorherige.getIId());
						aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					}
				}
			}
		} else {
			if (aktuelle.getPositionsartCNr().equalsIgnoreCase(
					LocaleFac.POSITIONSART_POSITION)) {
				// aktuelle.setCZusatzbez(getPositionBez());
				// if(aktuelle.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN
				// ))
				// aktuelle.setTypCNr(getPositiontyp());
			}
		}
		return aktuelle;
	}

	protected BelegpositionVerkaufDto getPositionZuBelegpositionDtoVerkauf(
			BelegpositionVerkaufDto aktuelle, TheClientDto theClientDto) {
		BelegpositionVerkaufDto positionDto = null;
		try {
			if (aktuelle instanceof AuftragpositionDto) {
				positionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								aktuelle.getPositioniId());
			} else if (aktuelle instanceof AngebotpositionDto) {
				positionDto = getAngebotpositionFac()
						.angebotpositionFindByPrimaryKey(
								aktuelle.getPositioniId(), theClientDto);
			} else if (aktuelle instanceof LieferscheinpositionDto) {
				positionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(
								aktuelle.getPositioniId(), theClientDto);
			} else if (aktuelle instanceof RechnungPositionDto) {
				positionDto = getRechnungFac()
						.rechnungPositionFindByPrimaryKey(
								aktuelle.getPositioniId());
			}
		} catch (RemoteException ex) {
		} catch (EJBExceptionLP ex) {
		}

		return positionDto;
	}

	protected Integer getUINachkommastellenPreisAllgemein(String mandantCNr) {
		Integer iPreisRabatte = new Integer(4);
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN);
			iPreisRabatte = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return iPreisRabatte;
	}

	/**
	 * SessionFacade fuer Webshop-Artikel holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return WebshopItemServiceFac
	 */
	protected final WebshopItemServiceFacLocal getWebshopItemServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopItemServiceFac();
	}

	protected final WebshopOrderServiceFacLocal getWebshopOrderServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopOrderServiceFac();
	}

	protected final WebshopOrderServiceFacLocal getWebshopCustomerOrderServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopCustomerOrderServiceFac();
	}

	protected final BatcherFac getBatcherFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBatcherFac();
	}

	protected Integer getUINachkommastellenPreisVK(String mandantCNr) {
		Integer iPreisRabatte = new Integer(4);
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
			iPreisRabatte = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return iPreisRabatte;
	}

	protected Integer getUINachkommastellenPreisEK(String mandantCNr) {
		Integer iPreisRabatte = new Integer(4);
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);
			iPreisRabatte = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return iPreisRabatte;
	}

	protected Query createNamedQuery(EntityManager em, String namedQuery,
			QueryParam[] params) {
		Query query = em.createNamedQuery(namedQuery);
		for (QueryParam entry : params) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}

	protected final WebshopCustomerServiceFacLocal getWebshopCustomerServiceFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopCustomerServiceFac();
	}

	protected final JCRMediaFac getJCRMediaFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJCRMediaFac();
	}

	protected final EmailMediaFac getEmailMediaFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getEmailMediaFac();
	}

	protected final EmailMediaLocalFac getEMailMediaLocalFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getEmailMediaLocalFac();
	}

	protected final BatcherSingleTransactionFac getBatcherSingleTransactionFac()
			throws EJBExceptionLP {
		return oFacadeBeauftragter.getBatcherSingleTransactionFac();
	}
	
	protected final PersonalApiFac getPersonalApiFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPersonalApiFac() ;
	}
}
