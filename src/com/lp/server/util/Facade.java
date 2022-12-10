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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.ejbfac.GTINGeneratorFac;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelServiceFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelimportFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.artikel.service.RahmenbedarfeFac;
import com.lp.server.artikel.service.ReservierungFac;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WebshopItemServiceFacLocal;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;
import com.lp.server.auftrag.service.EdifactOrdersImportFac;
import com.lp.server.auftrag.service.WebshopOrderServiceFacLocal;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.JobWEJournalFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.JobDetailsErImportFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.fertigung.ejbfac.FertigungFacLocal;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungImportFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.JobArbeitszeitstatusFac;
import com.lp.server.fertigung.service.JobBedarfsuebernahmeOffeneFac;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.kpi.service.KpiReportFac;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.lieferschein.ejbfac.LieferscheinFacLocal;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.media.ejbfac.EmailMediaLocalFac;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.ejbfac.PartnerServicesFacLocal;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
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
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.WebshopCustomerServiceFacLocal;
import com.lp.server.personal.service.HvmaFac;
import com.lp.server.personal.service.MaschineFac;
import com.lp.server.personal.service.NachrichtenFac;
import com.lp.server.personal.service.PersonalApiFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReisekostenFac;
import com.lp.server.personal.service.SchichtFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.shop.ejbfac.JobDetailsSyncItemFac;
import com.lp.server.shop.ejbfac.JobDetailsSyncOrderFac;
import com.lp.server.shop.ejbfac.ShopTimerCronJobLocal;
import com.lp.server.shop.magento2.Magento2CategoryFacLocal;
import com.lp.server.shop.magento2.Magento2CustomerFacLocal;
import com.lp.server.shop.magento2.Magento2OrderFacLocal;
import com.lp.server.shop.magento2.Magento2ProductFacLocal;
import com.lp.server.shop.service.ShopTimerFac;
import com.lp.server.shop.service.SyncShopFac;
import com.lp.server.stueckliste.ejbfac.ScriptItemId;
import com.lp.server.stueckliste.ejbfac.ScriptKundeId;
import com.lp.server.stueckliste.service.ItemId;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.system.ejb.Landplzort;
import com.lp.server.system.ejb.Ort;
import com.lp.server.system.ejbfac.BatcherFac;
import com.lp.server.system.ejbfac.BatcherSingleTransactionFac;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.EventLoggerCentralFacLocal;
import com.lp.server.system.ejbfac.ReportConnectionFacLocal;
import com.lp.server.system.ejbfac.ServerDruckerFacLocal;
import com.lp.server.system.ejbfac.SystemReportFacLocal;
import com.lp.server.system.ejbfac.TheClientFacLocal;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRMediaFac;
import com.lp.server.system.jcr.service.JcrDumpFac;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AutoBestellvorschlagFac;
import com.lp.server.system.service.AutoFehlmengendruckFac;
import com.lp.server.system.service.AutoLoseerledigenFac;
import com.lp.server.system.service.AutoLumiquoteFac;
import com.lp.server.system.service.AutoMahnenFac;
import com.lp.server.system.service.AutoMahnungsversandFac;
import com.lp.server.system.service.AutoMonatsabrechnungversandFac;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckFac;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.BelegartmediaFac;
import com.lp.server.system.service.BelegpositionkonvertierungFac;
import com.lp.server.system.service.DokumenteFac;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.Job4VendingExportFac;
import com.lp.server.system.service.JobAuslieferlisteFac;
import com.lp.server.system.service.JobKpiReportFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.PflegeFac;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.ServerDruckerFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.service.plscript.ScriptRuntime;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

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
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	@PersistenceContext
	protected EntityManager em;

	private FacadeBeauftragter oFacadeBeauftragter = null;

	public static final int MAX_UNBESCHRAENKT = 300;

	public static final String NICHT_SORTIERBAR = " X";

	protected static final String sUser = "lpwebappzemecs";
	protected static final String sPassword = "lpwebappzemecs";

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
	 * @param cNrUserI String
	 * @throws EJBExceptionLP
	 * @return TheClientDto
	 */
	protected final TheClientDto check(String cNrUserI) throws EJBExceptionLP {
		myLogger.entry(cNrUserI);
		if (cNrUserI == null) {
			// exccatch:
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_C_NR_USER_IS_NULL, "Fehler bei check. cNrUserI == null");
		}
		try {
			return getTheClientFac().theClientFindByUserLoggedIn(cNrUserI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected Session getNewSession() {
		return (Session) FLRSessionFactory.getFactory().openSession();
	}

	protected Session getCurrentSession() {
		return (Session) FLRSessionFactory.getFactory().getCurrentSession();
	}

	protected byte[] exportToPDF(JasperPrint print) {
		return exportToPDF(print, null);
	}

	protected boolean rabattDrucken(KundeDto kundeDto, Integer artikelIId, TheClientDto theClientDto) {
		if (Helper.short2boolean(kundeDto.getBRechnungsdruckmitrabatt())) {

			if (artikelIId != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
				if (aDto.getArtgruIId() != null) {
					ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(aDto.getArtgruIId(), theClientDto);

					if (Helper.short2boolean(artgruDto.getBKeinBelegdruckMitRabatt())) {
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}

			} else {
				return true;
			}

		} else {
			return false;
		}
	}

	protected byte[] exportToPDF(JasperPrint print, String kennwort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		if (kennwort != null) {
			exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD, kennwort);
			exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD, kennwort);
			exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		}

		try {
			exporter.exportReport();
			return baos.toByteArray();
		} catch (Throwable t) {
			myLogger.error("exportToPDF:", t);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENT_NICHT_IN_PDF_UMWANDELBAR, new Exception(t));
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

	protected ArrayList getKundeIIdsVerbundeUnternehmen(TheClientDto theClientDto) {

		ArrayList alKundeIId = new ArrayList();

		MandantDto[] mandantenDtos = getMandantFac().mandantFindAll(theClientDto);

		for (int i = 0; i < mandantenDtos.length; i++) {
			try {
				if (!mandantenDtos[i].getCNr().equals(theClientDto.getMandant())) {
					KundeDto kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							mandantenDtos[i].getPartnerIId(), theClientDto.getMandant(), theClientDto);
					if (kdDto != null) {
						alKundeIId.add(kdDto.getIId());

					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		return alKundeIId;
	}

	protected ArrayList getLieferantIIdsVerbundeUnternehmen(TheClientDto theClientDto) {

		ArrayList alLieferantIId = new ArrayList();

		MandantDto[] mandantenDtos = getMandantFac().mandantFindAll(theClientDto);

		for (int i = 0; i < mandantenDtos.length; i++) {
			try {
				if (!mandantenDtos[i].getCNr().equals(theClientDto.getMandant())) {
					LieferantDto lfDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
							mandantenDtos[i].getPartnerIId(), theClientDto.getMandant(), theClientDto);
					if (lfDto != null) {
						alLieferantIId.add(lfDto.getIId());

					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		return alLieferantIId;
	}

	protected ScriptRuntime prepareRuntime(ScriptRuntime runtime, Map<String, Object> konfigurationsWerte) {
		for (Map.Entry<String, Object> entry : konfigurationsWerte.entrySet()) {
			setRuntimeParam(runtime, entry);
		}

		return runtime;
	}

	private void setRuntimeParam(ScriptRuntime runtime, Map.Entry<String, Object> entry) {
		if (entry.getValue() instanceof ItemId) {
			ItemId itemId = (ItemId) entry.getValue();
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session.createCriteria(FLRArtikelliste.class);
			crit.add(Restrictions.eq("i_id", itemId.getId()));
			List<FLRArtikelliste> results = (List<FLRArtikelliste>) crit.list();
			if (results.isEmpty() || results.size() > 1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, itemId.getId().toString());
			}
			ScriptItemId scriptItemId = new ScriptItemId(itemId.getId(), results.get(0));
			runtime.setParam(entry.getKey(), scriptItemId);

			closeSession(session);
			return;
		}

		if (entry.getValue() instanceof KundeId) {
			KundeId kundeId = (KundeId) entry.getValue();
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session.createCriteria(FLRKunde.class);
			crit.add(Restrictions.eq("i_id", kundeId.id()));
			List<FLRKunde> results = (List<FLRKunde>) crit.list();
			if (results.isEmpty() || results.size() > 1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, kundeId.id().toString());
			}
			ScriptKundeId scriptKundeId = new ScriptKundeId(kundeId.id(), results.get(0));
			runtime.setParam(entry.getKey(), scriptKundeId);

			closeSession(session);
			return;
		}

		runtime.setParam(entry.getKey(), entry.getValue());
	}

	protected void erstelleProtokollEintrag(ProtokollDto protokollDto, TheClientDto theClientDto) {
		getSystemFac().erstelleProtokolleintrag(protokollDto, theClientDto);
	}

	protected HashMap uebersteuereAnsprechpartnerKommmunikationsdaten(TheClientDto theClientDto, PartnerDto partnerDto,
			HashMap parameter) throws RemoteException {
		// SP8814
		ArrayList<Integer> anspIIds = getAnsprechpartnerFac().getUebersteuerteAnsprechpartner(partnerDto,
				LieferscheinReportFac.REPORT_LIEFERSCHEIN, theClientDto);

		String sEmailUebst = "";
		String sFaxUebst = "";
		String sTelefonUebst = "";

		for (int i = 0; i < anspIIds.size(); i++) {
			Integer ansprechpartnerIIdUebersteuert = anspIIds.get(i);

			String sEmailUebstZeile = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(partnerDto.getIId(),
					ansprechpartnerIIdUebersteuert, PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(),
					theClientDto);

			if (sEmailUebstZeile != null) {
				if (sEmailUebst.length() > 0 && !sEmailUebst.endsWith(";")) {
					sEmailUebst += ";";
				}
				sEmailUebst += sEmailUebstZeile;
			}

			String sFaxUebstZeile = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(partnerDto.getIId(),
					ansprechpartnerIIdUebersteuert, PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(),
					theClientDto);

			if (sFaxUebstZeile != null) {
				if (sFaxUebst.length() > 0 && !sFaxUebst.endsWith(";")) {
					sFaxUebst += ";";
				}

				sFaxUebst += sFaxUebstZeile;
			}

			String sTelefonUebstZeile = getPartnerFac().partnerkommFindOhneAnpassungOhneExec(partnerDto.getIId(),
					ansprechpartnerIIdUebersteuert, PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(),
					theClientDto);

			if (sTelefonUebstZeile != null) {
				if (sTelefonUebst.length() > 0 && !sTelefonUebst.endsWith(";")) {
					sTelefonUebst += ";";
				}

				sTelefonUebst += sTelefonUebstZeile;
			}

		}

		if (sEmailUebst.length() > 0) {
			sEmailUebst = getTextRespectUISpr("lp.email", theClientDto.getMandant(),
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())) + ": " + sEmailUebst;
			parameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmailUebst);
		}
		if (sFaxUebst.length() > 0) {
			sFaxUebst = getTextRespectUISpr("lp.fax", theClientDto.getMandant(),
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())) + ": " + sFaxUebst;
			parameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFaxUebst);
		}
		if (sTelefonUebst.length() > 0) {
			sTelefonUebst = getTextRespectUISpr("lp.tel", theClientDto.getMandant(),
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())) + " " + sTelefonUebst;
			parameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefonUebst);
		}

		return parameter;
	}

	/**
	 * Ueberpruefe ob der User cNrUserI auf das System zugreifen darf.
	 * 
	 * @param theClientDto String
	 * @param oData        Object
	 * @throws EJBExceptionLP
	 * @return TheClientDto
	 */
	protected final TheClientDto checkAndLogData(TheClientDto theClientDto, Object oData) throws EJBExceptionLP {
		myLogger.logData(oData, theClientDto.getIDUser());
		if (theClientDto.getIDUser() == null) {
			// exccatch:
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_C_NR_USER_IS_NULL,
					"Fehler bei CheckAndLogData. theClientDto.getIDUser() == null");
		}
		try {
			return getTheClientFac().theClientFindByUserLoggedIn(theClientDto.getIDUser());
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected final void checkLocale(Locale localeI) throws EJBExceptionLP {
		myLogger.logData(localeI);

		if (localeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("localeI == null"));
		}
	}

	/**
	 * BigDecimals in der DB muessen das Format 15, 4 haben. Es ist moeglich, dass
	 * das Ergebnis einer Berechnung am Client mehr als 15 Vorkommastellen hat. In
	 * diesem Fall wird dem Benutzer eine entsprechende Meldung angezeigt.
	 * checknumberformat: 0
	 * 
	 * @param bdNumberI die Zahl
	 * @throws EJBExceptionLP Ausnahme
	 */
	protected final static void checkNumberFormat(BigDecimal bdNumberI) throws EJBExceptionLP {
		if (bdNumberI.doubleValue() > SystemFac.MAX_N_NUMBER || bdNumberI.doubleValue() < SystemFac.MIN_N_NUMBER) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORMAT_NUMBER,
					new Exception("Ungueltiges NumberFormat, Format 15,4"));
		}
	}

	protected void checkMwstsaetzeImZeitraum(Timestamp tBelegdatum1, Timestamp tBelegdatum2, String mandantCNr) {

		if (tBelegdatum1.after(tBelegdatum2)) {
			Timestamp tHelp = tBelegdatum1;
			tBelegdatum1 = tBelegdatum2;
			tBelegdatum2 = tHelp;
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT count(m.i_id)" + " FROM FLRMwstsatz AS m WHERE m.flrmwstsatzbez.mandant_c_nr='"
				+ mandantCNr + "' AND m.d_gueltigab>'"
				+ Helper.formatTimestampWithSlashes(Helper.cutTimestamp(tBelegdatum1)) + "' AND m.d_gueltigab <'"
				+ Helper.formatTimestampWithSlashes(Helper.cutTimestamp(Helper.addiereTageZuTimestamp(tBelegdatum2, 1)))
				+ "' ";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		if (resultList.size() > 0) {

			Long l = (Long) resultListIterator.next();
			if (l != null && l > 0) {

				ArrayList al = new ArrayList();

				if (tBelegdatum2.before(tBelegdatum1)) {
					al.add(tBelegdatum2);
					al.add(tBelegdatum1);
				} else {
					al.add(tBelegdatum1);
					al.add(tBelegdatum2);
				}

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MEHRERE_MWSTSAETZE_IM_DATUMSBEREICH, al,
						new Exception("FEHLER_MEHRERE_MWSTSAETZE_IM_DATUMSBEREICH"));
			}

		}

	}

	/**
	 * Hole die Clientinformationen des Users.
	 * 
	 * deprecated MB: TheClientDto wird von check2(idUser) zurueckgegeben -> du
	 * ersparst dir einen db-zugriff
	 * 
	 * @param idUser String
	 * @return TheClientDto
	 */
	protected final TheClientDto getTheClient(String idUser) throws EJBExceptionLP {
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
	protected final AnsprechpartnerFac getAnsprechpartnerFac() throws EJBExceptionLP {
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
	protected final AuftragteilnehmerFac getAuftragteilnehmerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragteilnehmerFac();
	}

	protected final InstandhaltungFac getInstandhaltungFac() throws EJBExceptionLP {
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

	protected final AnfragepositionFac getAnfragepositionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnfragepositionFac();
	}

	protected final AnfrageServiceFac getAnfrageServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAnfrageServiceFac();
	}

	/**
	 * SessionFacade fuer Kundesachbearbeiter holen.
	 * 
	 * @return KundesachbearbeiterFac
	 * @throws EJBExceptionLP
	 */
	protected final KundesachbearbeiterFac getKundesachbearbeiterFac() throws EJBExceptionLP {
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

	protected final ArtikelServiceFac getArtikelServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelServiceFac();
	}

	protected final ArtikelimportFac getArtikelimportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelimportFac();
	}

	/**
	 * SessionFacade fuer ArtikelReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	protected final ArtikelReportFac getArtikelReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelReportFac();
	}

	protected final ArtikelkommentarFac getArtikelkommentarFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getArtikelkommentarFac();
	}

	/**
	 * SessionFacade fuer Auftragposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragpositionFac
	 */
	protected final AuftragpositionFac getAuftragpositionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragpositionFac();
	}

	protected final AuftragReportFac getAuftragReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAuftragReportFac();
	}

	/**
	 * SessionFacade fuer AuftragServices holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragServiceFac
	 */
	protected final AuftragServiceFac getAuftragServiceFac() throws EJBExceptionLP {
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

	protected final BestellpositionFac getBestellpositionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBestellpositionFac();
	}

	protected final BestellungServiceFac getBestellungServiceFac() throws EJBExceptionLP {
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
	protected final BestellvorschlagFac getBestellvorschlagFac() throws EJBExceptionLP {
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
	protected final LieferscheinpositionFac getLieferscheinpositionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinpositionFac();
	}

	protected final LieferscheinServiceFac getLieferscheinServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getLieferscheinServiceFac();
	}

	protected final LieferscheinReportFac getLieferscheinReportFac() throws EJBExceptionLP {
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

	protected final LieferantServicesFac getLieferantServicesFac() throws EJBExceptionLP {
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

	protected final PartnerReportFac getPartnerReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerReportFac();
	}

	protected final PartnerServicesFac getPartnerServicesFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerServicesFac();
	}

	protected final PartnerServicesFacLocal getPartnerServicesFacLocal() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPartnerServicesFacLocal();
	}

	protected final PersonalFac getPersonalFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPersonalFac();
	}

	protected final ProjektFac getProjektFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getProjektFac();
	}

	protected final ProjektServiceFac getProjektServiceFac() throws EJBExceptionLP {
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

	protected final StuecklisteimportFac getStuecklisteimportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getStuecklisteimportFac();
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
	protected final StuecklisteReportFac getStuecklisteReportFac() throws EJBExceptionLP {
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
	protected final ArtikelbestelltFac getArtikelbestelltFac() throws EJBExceptionLP {
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

	protected final AbrechnungsvorschlagFac getAbrechnungsvorschlagFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAbrechnungsvorschlagFac();
	}

	/**
	 * SessionFacade fuer RechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungReportFac
	 */
	protected final RechnungReportFac getRechnungReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getRechnungReportFac();
	}

	/**
	 * SessionFacade fuer RechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungServiceFac
	 */
	protected final RechnungServiceFac getRechnungServiceFac() throws EJBExceptionLP {
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
	protected final FinanzServiceFac getFinanzServiceFac() throws EJBExceptionLP {
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

	protected final NachrichtenFac getNachrichtenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getNachrichtenFac();
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

	protected final AutoFehlmengendruckFac getAutoFehlmengendruckFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoFehlmengendruckFac();
	}

	protected final AutoMahnungsversandFac getAutoMahnungsversandFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoMahnungsversandFac();
	}

	protected final AutoMonatsabrechnungversandFac getAutoMonatsabrechnungversandFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoMonatsabrechnungversandFac();
	}

	protected final AutoLoseerledigenFac getAutoLoseerledigenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoLoseerledigenFac();
	}

	protected final AutoMahnenFac getAutoMahnenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoMahnenFac();
	}

	protected final AutoBestellvorschlagFac getAutoBestellvorschlagFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoBestellvorschlagFac();
	}

	protected final AutoPaternosterFac getAutoPaternosterFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoPaternosterFac();
	}

	protected final BestellungReportFac getBestellungReportFac() throws EJBExceptionLP {
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
	protected final SystemMultilanguageFac getSystemMultilanguageFac() throws EJBExceptionLP {
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

	protected final SystemReportFacLocal getSystemReportFacLocal() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemReportFacLocal();
	}

	protected final SystemServicesFac getSystemServicesFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSystemServicesFac();
	}

	/**
	 * SessionFacade fuer VkPreisfindung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final VkPreisfindungFac getVkPreisfindungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getVkPreisfindungFac();
	}

	/**
	 * SessionFacade fuer Eingangsrechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungFac
	 */
	protected final EingangsrechnungFac getEingangsrechnungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getEingangsrechnungFac();
	}

	/**
	 * SessionFacade fuer EingangsrechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungReportFac
	 */
	protected final EingangsrechnungReportFac getEingangsrechnungReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getEingangsrechnungReportFac();
	}

	/**
	 * SessionFacade fuer EingangsrechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungServiceFac
	 */
	protected final EingangsrechnungServiceFac getEingangsrechnungServiceFac() throws EJBExceptionLP {
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
	protected final ZeiterfassungFac getZeiterfassungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getZeiterfassungFac();
	}

	protected final ReisekostenFac getReisekostenFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getReisekostenFac();
	}

	protected final MaschineFac getMaschineFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getMaschineFac();
	}

	protected final ZeiterfassungReportFac getZeiterfassungReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getZeiterfassungReportFac();
	}

	/**
	 * SessionFacade fuer Zeiterfassung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ZeiterfassungFac
	 */
	protected final ZutrittscontrollerFac getZutrittscontrollerFac() throws EJBExceptionLP {
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

	protected final AusliefervorschlagFac getAusliefervorschlagFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAusliefervorschlagFac();
	}

	protected final SchichtFac getSchichtFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSchichtFac();
	}

	protected final AngebotpositionFac getAngebotpositionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotpositionFac();
	}

	protected final AngebotReportFac getAngebotReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotReportFac();
	}

	protected final AngebotServiceFac getAngebotServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotServiceFac();
	}

	protected final ReportConnectionFacLocal getReportConnectionFacLocal() throws EJBExceptionLP {
		return oFacadeBeauftragter.getReportConnectionFacLocal();
	}

	protected final BenutzerServicesFacLocal getBenutzerServicesFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBenutzerServicesFac();
	}

	protected final FastLaneReader getFastLaneReader() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFastLaneReader();
	}

	protected final FertigungFac getFertigungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungFac();
	}

	protected final ForecastFac getForecastFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getForecastFac();
	}

	protected final FertigungReportFac getFertigungReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungReportFac();
	}

	protected final FertigungServiceFac getFertigungServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFertigungServiceFac();
	}

	protected final FehlmengeFac getFehlmengeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getFehlmengeFac();
	}

	protected final InternebestellungFac getInternebestellungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getInternebestellungFac();
	}

	protected final AngebotstklFac getAngebotstklFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAngebotstklFac();
	}

	protected final AngebotstklpositionFac getAngebotstklpositionFac() throws EJBExceptionLP {
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

	protected final BelegbuchungFac getBelegbuchungFac(String mandantCNr) throws EJBExceptionLP {
		return oFacadeBeauftragter.getBelegbuchungFac(mandantCNr);
	}

	protected final ZahlungsvorschlagFac getZahlungsvorschlagFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getZahlungsvorschlagFac();
	}

	protected final BelegpositionkonvertierungFac getBelegpositionkonvertierungFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBelegpositionkonvertierungFac();
	}

	protected final RahmenbedarfeFac getRahmenbedarfeFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getRahmenbedarfeFac();
	}

	protected final AutoRahmendetailbedarfdruckFac getAutoRahmendetailbedarfdruckFac() {
		return oFacadeBeauftragter.getAutoRahmendetailbedarfdruckFac();
	}

	protected final AutomatikjobFac getAutomatikjobFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutomatikjobFac();
	}

	protected final ShopTimerCronJobLocal getShopTimerCronJob() throws EJBExceptionLP {
		return oFacadeBeauftragter.getShopTimerCronJob();
	}

	protected final AutomatiktimerFac getAutomatiktimerFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutomatiktimerFac();
	}

	protected final AutomatikjobtypeFac getAutomatikjobtypeFac() throws EJBExceptionLP {
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

	protected final BelegartmediaFac getBelegartmediaFac() {
		return oFacadeBeauftragter.getBelegartmediaFac();
	}

	protected final AuftragRahmenAbrufFac getAuftragRahmenAbrufFac() {
		return oFacadeBeauftragter.getAuftragRahmenAbrufFac();
	}

	protected final ShopTimerFac getShopTimerFac() {
		return oFacadeBeauftragter.getShopTimerFac();
	}

	/**
	 * exccatch: Pruefe ob in RemoteException eine EJBExceptionLP enthaelt, wenn ja
	 * dann throw diese, sonst die RemoteException.
	 * 
	 * @param reI int
	 * @throws EJBExceptionLP
	 */
	final protected void throwEJBExceptionLPRespectOld(RemoteException reI) throws EJBExceptionLP {
		Throwable t = reI.detail;
		if (t != null && t instanceof EJBExceptionLP) {
			throw new EJBExceptionLP((EJBExceptionLP) t);
		} else {
			throw new EJBExceptionLP(reI);
		}
	}

	protected EJBExceptionLP getThrowEJBExceptionLPRespectOld(RemoteException e) {
		Throwable t = e.detail;
		if (t instanceof EJBExceptionLP) {
			return new EJBExceptionLP((EJBExceptionLP) t);
		} else {
			return new EJBExceptionLP(e);
		}
	}

	final protected void throwEJBExceptionLPforPersistence(PersistenceException peI) throws EJBExceptionLP {
		EJBExceptionLP en = new EJBExceptionLP(EJBExceptionLP.FEHLER_SQL_EXCEPTION_MIT_INFO,
				"Eine Persistence Exception ist aufgetreten. Infos erhalten Sie in der SQLException");
		if ((Throwable) peI.getCause() instanceof DataException) {
			DataException ed = (DataException) peI.getCause();
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add(ed.getSQLException().getMessage().toString());
			if (ed.getSQLException().getNextException() != null) {
				alInfo.add(ed.getSQLException().getNextException().getMessage().toString());
			}
			en.setAlInfoForTheClient(alInfo);
		} else {
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add(peI.getCause().getMessage());
			alInfo.add(peI.getCause().getStackTrace().toString());
		}
		throw en;
	}

	final protected void throwExceptionZwischensummeNull() throws EJBExceptionLP {
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZWISCHENSUMME_0, new Exception("ZWISCHENSUMME_NULL"));
	}

	/**
	 * 
	 * Einen Betrag mit einem Helium V Wechselkurs multiplizieren. <br>
	 * Es gilt: Die Rundung fuer das Produkt aus Betrag und Wechselkurs in Helium V
	 * muss einheitlich sein.
	 * 
	 * @param nBetragI       der Betrag
	 * @param bdWechselkursI der Wechselkurs
	 * @return BigDecimal der berechnete Betrag
	 * @throws EJBExceptionLP Ausnahme
	 */
	protected BigDecimal getBetragMalWechselkurs(BigDecimal nBetragI, BigDecimal bdWechselkursI) throws EJBExceptionLP {
		if (nBetragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("nBetragI == null"));
		}

		if (bdWechselkursI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("bdWechselkursI == null"));
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

	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI) throws EJBExceptionLP {
		return getBenutzerServicesFac().getTextRespectUISpr(sTokenI, mandantCNr, loI);
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI, Object... replacements)
			throws EJBExceptionLP {
		return getBenutzerServicesFac().getTextRespectUISpr(sTokenI, mandantCNr, loI, replacements);
	}

	public String getBriefanredeNeutralOderPrivatperson(Integer partnerIId, Locale locDruck,
			TheClientDto theClientDto) {

		PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

		if (pDto.getAnredeCNr() != null && (pDto.getAnredeCNr().equals(PartnerFac.PARTNER_ANREDE_HERR)
				|| pDto.getAnredeCNr().equals(PartnerFac.PARTNER_ANREDE_FRAU)
				|| pDto.getAnredeCNr().equals(PartnerFac.PARTNER_ANREDE_FAMILIE))) {
			try {
				return getPartnerFac().formatBriefAnrede(pDto, locDruck, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;
			}
		} else {
			return getTextRespectUISpr("lp.anrede.sehrgeehrtedamenundherren", theClientDto.getMandant(), locDruck);
		}

	}

	/**
	 * Die Kundendaten fuer Reports wie AB, RE, BS, etc formatieren nach der
	 * oesterreicheischen postvorschrift: Zeile 1: Name 1 Zeile 2: Name 2 Zeile 3:
	 * Abteilung Zeile 4: Ansprechpartner Zeile 5: Strasse / Postfach Zeile 6:
	 * PLZ-Ort Zeile 7: Land (in Blockbuchstaben) es gibt keine Leerzeilen. Der
	 * Adressblock ist am Report 7-zeilig darzustellen, damit sich nach unten nichts
	 * verschiebt
	 * 
	 * @return String
	 * @param partnerDto PartnerDto
	 * @param anspDto    AnsprechpartnerDto
	 * @param mandantDto MandantDto
	 * @param locale     Locale
	 * @throws EJBExceptionLP
	 */
	public String formatAdresseFuerAusdruckOld(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale) throws EJBExceptionLP {
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
				if (sAnredeCNr != null && !sAnredeCNr.equals("") && (sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_FRAU)
						|| sAnredeCNr.equals(PartnerFac.PARTNER_ANREDE_HERR))) {

					if (titel != null && titel.length() > 0) {
						if (partnerDto.getCName2vornamefirmazeile2() != null) {
							sZeilen[0] = titel + partnerDto.getCName2vornamefirmazeile2();
						} else {
							sZeilen[0] = partnerDto.getCName2vornamefirmazeile2();
						}

					} else {
						sZeilen[0] = partnerDto.getCName2vornamefirmazeile2();
					}

					if (partnerDto.getCName3vorname2abteilung() != null) {

						if (sZeilen[0] != null) {

							sZeilen[0] += " " + partnerDto.getCName3vorname2abteilung();
						} else {
							sZeilen[0] = partnerDto.getCName3vorname2abteilung();
						}
					}
					if (sZeilen[0] != null) {
						sZeilen[0] += " " + partnerDto.getCName1nachnamefirmazeile1() + ntitel;
					} else {
						sZeilen[0] = partnerDto.getCName1nachnamefirmazeile1() + ntitel;
					}
				} else {
					// Firma
					sZeilen[0] = titel + partnerDto.getCName1nachnamefirmazeile1() + ntitel;
					sZeilen[1] = partnerDto.getCName2vornamefirmazeile2();
					sZeilen[2] = partnerDto.getCName3vorname2abteilung();
				}

				if (partnerDto.getCPostfach() != null && partnerDto.getCPostfach().trim().length() > 0) {
					String s = getTextRespectUISpr("lp.postfach", mandantDto.getCNr(), locale) + " "
							+ partnerDto.getCPostfach();
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
					sZeilen[5] = partnerDto.getLandplzortDto_Postfach().formatPlzOrt();
					if (partnerDto.getLandplzortDto_Postfach().getLandDto() != null) {
						// das Land nur drucken, wenn es nicht dem Land des
						// Mandanten entspricht
						if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
								|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
								|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
										.equals(partnerDto.getLandplzortDto_Postfach().getLandDto().getIID())) {
							sZeilen[6] = partnerDto.getLandplzortDto_Postfach().getLandDto().getCName().toUpperCase();
						}
					}
				}
				// sonst den "normalen" Ort
				else if (partnerDto.getLandplzortDto() != null) {
					sZeilen[5] = partnerDto.getLandplzortDto().formatPlzOrt();
					if (partnerDto.getLandplzortDto().getLandDto() != null) {

						LandDto landDto = null;
						ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_POSTVERSENDUNGSLAND);
						if (parametermandantDto.getCWert() != null)
							;
						if (!parametermandantDto.getCWert().equals("")) {
							landDto = getSystemFac().landFindByLkz(parametermandantDto.getCWert());
						}
						if (landDto != null) {
							if (!landDto.getIID().equals(partnerDto.getLandplzortDto().getLandDto().getIID()))
								sZeilen[6] = partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase();
						} else {
							// das Land nur drucken, wenn es nicht dem Land des
							// Mandanten entspricht
							if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
									|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
									|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
											.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
								sZeilen[6] = partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase();
							}
						}
					}
				}
			}
			if (anspDto != null) {
				sZeilen[3] = getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(anspDto.getPartnerDto(),
						locale, null);
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

	public boolean lagerMinJeLager(TheClientDto theClientDto) {
		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			return (Boolean) parametermandantDto.getCWertAsObject();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return false;
		}

	}

	public Integer getVerpackunskostenArtikel(TheClientDto theClientDto) {
		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERPACKUNGSKOSTEN_ARTIKEL);

			String vpKostenArtikel = parametermandantDto.getCWert();
			if (vpKostenArtikel != null && vpKostenArtikel.trim().length() > 0) {

				ArtikelDto aDtoVPKosten = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(vpKostenArtikel.trim(),
						theClientDto.getMandant());
				if (aDtoVPKosten != null) {
					return aDtoVPKosten.getIId();
				} else {
					ArrayList al = new ArrayList();
					al.add(vpKostenArtikel.trim());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN, al,
							new Exception("VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN: " + vpKostenArtikel));
				}
			} else {
				return null;
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public Integer getVersandkostenArtikel(TheClientDto theClientDto) {
		try {
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_VERSANDKOSTENARTIKEL);

			String vpKostenArtikel = parametermandantDto.getCWert();
			if (vpKostenArtikel != null && vpKostenArtikel.trim().length() > 0) {

				ArtikelDto aDtoVPKosten = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(vpKostenArtikel.trim(),
						theClientDto.getMandant());
				if (aDtoVPKosten != null) {
					return aDtoVPKosten.getIId();
				} else {
					ArrayList al = new ArrayList();
					al.add(vpKostenArtikel.trim());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERSANDKOSTENARTIKEL_NICHT_VORHANDEN, al,
							new Exception("FEHLER_VERSANDKOSTENARTIKEL_NICHT_VORHANDEN: " + vpKostenArtikel));
				}
			} else {
				return null;
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	private boolean isAnredePerson(String sAnredeCNr) {

		// 2012-10-22 WH: Wenn Anrede vorhandenm welche nicht 'Firma' ist, dann
		// ist es eine Person

		if (sAnredeCNr != null && !PartnerFac.PARTNER_ANREDE_FIRMA.equals(sAnredeCNr)) {
			return true;
		} else {
			return false;
		}

	}

	public Integer pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(Integer personalIIdVertreter, Integer kundeIId,
			Timestamp tsZeitpunkt, TheClientDto theClientDto) {
		// SP6638 Ist Vertreter noch im Unternehmen
		if (personalIIdVertreter != null) {
			try {
				if (getPersonalFac().istPersonalAusgetreten(personalIIdVertreter, tsZeitpunkt, theClientDto)) {
					if (kundeIId != null) {
						KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);
						if (kdDto.getPersonaliIdProvisionsempfaenger() != null) {
							return kdDto.getPersonaliIdProvisionsempfaenger();
						}
					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		return personalIIdVertreter;
	}

	public boolean sindAnsprechpartnerGleich(Integer ansprechpartnerIId1, Integer ansprechpartnerIId2) {
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

	/**
	 * Ermittle MWST Satz aus Mandantparameter PARAMETER_KUNDEN_POSITIONSKONTIERUNG
	 * oder aus oKundeOderLieferant. Ist belegpositionDto vom Typ
	 * BelegpositionVerkaufDto so muss mwstsatzIId gesetzt sein.
	 * 
	 * @param belegpositionDto BelegpositionDto Belegposition aus dem der MWST Satz
	 *                         geholt werden soll.
	 * @param theClientDto     TheClientDto
	 * @return MwstsatzDto
	 */
	public MwstsatzDto ermittleMwstSatz(BelegpositionDto belegpositionDto, Timestamp belegDatum,
			TheClientDto theClientDto) throws EJBExceptionLP {
		MwstsatzDto mwstsatzDto = null;

		// MWST Satz nur wenn Artikel oder Handeingabe
		if (belegpositionDto.getPositionsartCNr().equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)
				|| belegpositionDto.getPositionsartCNr().equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {

			// VerkaufDto muss bereits MWSTSatz IID haben
			if (belegpositionDto instanceof BelegpositionVerkaufDto) {
				BelegpositionVerkaufDto belegpositionVKDto = (BelegpositionVerkaufDto) belegpositionDto;
				Integer mwstSatzIId = belegpositionVKDto.getMwstsatzIId();
				if (mwstSatzIId == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
							new Exception("MwstsatzIId von BelegpositionVerkaufDto == null"));
				} else {
					mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(mwstSatzIId, theClientDto);
				}
				return mwstsatzDto;
			}

			// MWSTSatz fuer Einkaufsmodule setzen

			// Mandantenparameter fuer Positionskontierung bestimmen
			ParametermandantDto parameterPositionskontierung = null;
			try {
				parameterPositionskontierung = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();
			// Mwstsatz aus Artikel
			if (bDefaultMwstsatzAusArtikel) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(belegpositionDto.getArtikelIId(),
						theClientDto);
				/*
				 * mwstsatzDto = getMandantFac()
				 * .mwstsatzFindByMwstsatzbezIIdAktuellster(artikelDto.getMwstsatzbezIId(),
				 * theClientDto);
				 */
				mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(artikelDto.getMwstsatzbezIId(), belegDatum,
						theClientDto);
			} else {
				// MWST Satz setzen
				// Default MWST aus Mandant holen
				try {
					MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
							theClientDto);
					/*
					 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
					 * mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz(), theClientDto);
					 */
					mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(
							mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz(), belegDatum, theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				// Einkauf: MWST Satz aus Lieferant
				if (belegpositionDto instanceof AnfragepositionDto) {
					AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto;
					AnfrageDto anfrageDto = null;
					try {
						anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(quellDto.getBelegIId(), theClientDto);
						LieferantDto lieferantDto = getLieferantFac()
								.lieferantFindByPrimaryKey(anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto);

						if (lieferantDto.getMwstsatzbezIId() != null) {
							/*
							 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
							 * lieferantDto.getMwstsatzbezIId(), theClientDto);
							 */
							mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(lieferantDto.getMwstsatzbezIId(),
									belegDatum, theClientDto);
						}
					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}

				} else if (belegpositionDto instanceof BestellpositionDto) {
					BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto;
					BestellungDto bestellungDto = null;
					try {
						if (quellDto.getBelegIId() != null) {
							bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(quellDto.getBelegIId());
							LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
									bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
							if (lieferantDto.getMwstsatzbezIId() != null) {
								/*
								 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
								 * lieferantDto.getMwstsatzbezIId(), theClientDto);
								 */
								mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(lieferantDto.getMwstsatzbezIId(),
										belegDatum, theClientDto);

							}
						}
					} catch (RemoteException ex2) {
						throwEJBExceptionLPRespectOld(ex2);
					}
				}
			}

		}
		return mwstsatzDto;
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale, false, null, false);
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale, String belegartCNr, boolean postfachIgnorieren) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale, false, belegartCNr,
				postfachIgnorieren);
	}

	public BelegpositionVerkaufDto mwstsatzBestimmenUndNeuBerechnen(BelegpositionVerkaufDto posDto, Integer kundeIId,
			Timestamp tBelegdatum, TheClientDto theClientDto) {
		if (posDto.isIntelligenteZwischensumme() && posDto.getMwstsatzIId() != null && tBelegdatum != null) {
			// MwstsatzId neu bestimmen
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumEvaluate(new MwstsatzId(posDto.getMwstsatzIId()),
					tBelegdatum, theClientDto);
			posDto.setMwstsatzIId(mwstsatzDto.getIId());

			// Bruttopreis gibt es bei Zwischensumme nicht
		}

		// SP5827
		if (posDto.getArtikelIId() != null && kundeIId != null && tBelegdatum != null) {
			boolean bDefaultMwstsatzAusArtikel = false;
			try {
				ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
				bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(), theClientDto);
			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(kundeIId);
			MwstsatzDto mwstsatzDesKundenDtoZumBelegdatum = getMandantFac()
					.mwstsatzFindZuDatum(kdDto.getMwstsatzbezIId(), tBelegdatum);

			Double fMwstsatz = null;

			if (bDefaultMwstsatzAusArtikel == true) {
				if (mwstsatzDesKundenDtoZumBelegdatum.getFMwstsatz().doubleValue() == 0) {
					// Der Artikel bringt seine Mwst mit, ausser der Kunde hat
					// MWST-Satz mit 0%, dann muss dieser verwendet werden

					posDto.setMwstsatzIId(mwstsatzDesKundenDtoZumBelegdatum.getIId());

					fMwstsatz = mwstsatzDesKundenDtoZumBelegdatum.getFMwstsatz();
				} else {
					if (aDto.getMwstsatzbezIId() != null) {
						MwstsatzDto mwstsatzDesArtikelsZumBelegdatum = getMandantFac()
								.mwstsatzFindZuDatum(aDto.getMwstsatzbezIId(), tBelegdatum);
						posDto.setMwstsatzIId(mwstsatzDesArtikelsZumBelegdatum.getIId());
						fMwstsatz = mwstsatzDesArtikelsZumBelegdatum.getFMwstsatz();
					}
				}
			} else {
				posDto.setMwstsatzIId(mwstsatzDesKundenDtoZumBelegdatum.getIId());
				fMwstsatz = mwstsatzDesKundenDtoZumBelegdatum.getFMwstsatz();
			}

			if (posDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null) {

				BigDecimal mwstBetrag = posDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
						.multiply(new BigDecimal(fMwstsatz.doubleValue()).movePointLeft(2));
				posDto.setNMwstbetrag(mwstBetrag);
				posDto.setNBruttoeinzelpreis(
						mwstBetrag.add(posDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
			}
		}

		return posDto;
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale, String belegartCNr) {
		return formatAdresseFuerAusdruck(partnerDto, anspDto, mandantDto, locale, false, belegartCNr, false);
	}

	public String formatAdresseFuerAusdruck(PartnerDto partnerDto, AnsprechpartnerDto anspDto, MandantDto mandantDto,
			Locale locale, boolean bLandImmerAnhaengen, String belegartCNr, boolean postfachIgnorieren) {
		StringBuffer sbAdressblock = new StringBuffer("");
		try {
			String[] sZeilen = new String[8];
			if (partnerDto != null) {

				// PJ19839

				if (partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getIId() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(partnerDto.getLandplzortDto().getIlandID(),
							Helper.locale2String(locale));

					if (landDto.getLandsprDto() != null && landDto.getLandsprDto().getCBez() != null) {
						partnerDto.getLandplzortDto().getLandDto().setCName(landDto.getLandsprDto().getCBez());
					}

				}

				if (partnerDto.getLandplzortDto_Postfach() != null
						&& partnerDto.getLandplzortDto_Postfach().getIId() != null) {
					LandDto landDto = getSystemFac().landFindByPrimaryKey(
							partnerDto.getLandplzortDto_Postfach().getIlandID(), Helper.locale2String(locale));

					if (landDto.getLandsprDto() != null && landDto.getLandsprDto().getCBez() != null) {
						partnerDto.getLandplzortDto_Postfach().getLandDto().setCName(landDto.getLandsprDto().getCBez());
					}

				}

				String sAnredeCNr = partnerDto.getAnredeCNr();
				String titel = getTrimmed(partnerDto.getCTitel());
				String ntitel = getTrimmed(partnerDto.getCNtitel());

				if (isAnredePerson(sAnredeCNr)) {
					// SP629
					ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(mandantDto.getCNr(),
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ADRESSE_FUER_AUSDRUCK_MIT_ANREDE);

					if (((Boolean) parametermandantDto.getCWertAsObject()) == true && sAnredeCNr != null) {

						String ret = partnerDto.getAnredeCNr().trim();

						if (locale != null) {
							AnredesprDto anredesprDto = getPartnerFac()
									.anredesprFindByAnredeCNrLocaleCNrOhneExc(sAnredeCNr, Helper.locale2String(locale));
							if (anredesprDto != null) {
								ret = anredesprDto.getCBez().trim();
							}
						}

						sZeilen[0] = ret;
						sZeilen[1] = getBlankSuffixed(titel)
								+ getBlankSuffixed(partnerDto.getCName2vornamefirmazeile2())
								+ getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1()) + ntitel;

						sZeilen[2] = getTrimmed(partnerDto.getCName3vorname2abteilung());
					} else {
						sZeilen[0] = getBlankSuffixed(titel)
								+ getBlankSuffixed(partnerDto.getCName2vornamefirmazeile2())
								+ getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1()) + ntitel;

						sZeilen[2] = getTrimmed(partnerDto.getCName3vorname2abteilung());
					}

				} else {
					// Firma
					sZeilen[0] = getBlankSuffixed(titel) + getBlankSuffixed(partnerDto.getCName1nachnamefirmazeile1())
							+ ntitel;
					sZeilen[1] = partnerDto.getCName2vornamefirmazeile2();

					if (anspDto != null && anspDto.getCAbteilung() != null) {
						sZeilen[2] = anspDto.getCAbteilung();
					} else {
						sZeilen[2] = partnerDto.getCName3vorname2abteilung();
					}

				}

				String postfach = getTrimmed(partnerDto.getCPostfach());
				// PJ18752
				if (postfach.length() > 0 && postfachIgnorieren == false) {

					if (partnerDto.getLandplzortDto() != null && Helper
							.short2boolean(partnerDto.getLandplzortDto().getLandDto().getBPostfachmitstrasse())) {
						sZeilen[4] = getTrimmed(partnerDto.getCStrasse());
					}
					sZeilen[5] = getBlankSuffixed(getTextRespectUISpr("lp.postfach", mandantDto.getCNr(), locale))
							+ partnerDto.getCPostfach();
				} else {
					// sonst Strasse
					sZeilen[5] = getTrimmed(partnerDto.getCStrasse());
				}

				// Postfach - LandPLZOrt (wenn vorhanden)
				if (partnerDto.getLandplzortDto_Postfach() != null && postfachIgnorieren == false) {
					sZeilen[6] = partnerDto.getLandplzortDto_Postfach().formatPlzOrt();
					if (partnerDto.getLandplzortDto_Postfach().getLandDto() != null) {

						boolean bAusland = false;
						if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
								|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
								|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
										.equals(partnerDto.getLandplzortDto_Postfach().getLandDto().getIID())) {
							bAusland = true;
						}

						// das Land nur drucken, wenn es nicht dem Land des
						// Mandanten entspricht
						if (bAusland || bLandImmerAnhaengen) {
							sZeilen[6] = partnerDto.getLandplzortDto_Postfach().formatPlzOrt(bAusland);
							sZeilen[7] = partnerDto.getLandplzortDto_Postfach().getLandDto().getCName().toUpperCase();

							// Land entfernen, wenn gemeinsames Postland
							if (bLandImmerAnhaengen == false && mandantDto != null
									&& mandantDto.getPartnerDto().getLandplzortDto() != null
									&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null

									&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
											.getLandIIdGemeinsamespostland() != null

									&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
											.getLandIIdGemeinsamespostland()
											.equals(partnerDto.getLandplzortDto_Postfach().getLandDto().getIID())) {
								sZeilen[7] = null;
							}

						}
					}
				}
				// sonst den "normalen" Ort
				else if (partnerDto.getLandplzortDto() != null) {
					sZeilen[6] = partnerDto.getLandplzortDto().formatPlzOrt();
					if (partnerDto.getLandplzortDto().getLandDto() != null) {

						LandDto landDto = null;
						ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_POSTVERSENDUNGSLAND);
						if (parametermandantDto.getCWert() != null)
							;
						if (!parametermandantDto.getCWert().equals("")) {
							landDto = getSystemFac().landFindByLkz(parametermandantDto.getCWert());
						}
						if (landDto != null) {
							if (!landDto.getIID().equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
								sZeilen[7] = partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase();
								sZeilen[6] = partnerDto.getLandplzortDto().formatPlzOrt(true);
							}
						} else {
							// das Land nur drucken, wenn es nicht dem Land des
							// Mandanten entspricht

							boolean bAusland = false;
							if (mandantDto == null || mandantDto.getPartnerDto().getLandplzortDto() == null
									|| mandantDto.getPartnerDto().getLandplzortDto().getLandDto() == null
									|| !mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
											.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
								bAusland = true;
							}

							if (bAusland || bLandImmerAnhaengen) {

								// SP581 das Land nicht andrucken, wenn es das
								// gemeinsame Postland des Mandanten-Landes

								sZeilen[6] = partnerDto.getLandplzortDto().formatPlzOrt(bAusland);
								sZeilen[7] = partnerDto.getLandplzortDto().getLandDto().getCName().toUpperCase();

								// Land entfernen, wenn gemeinsames Postland
								if (mandantDto != null && mandantDto.getPartnerDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto() != null
										&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
												.getLandIIdGemeinsamespostland() != null) {

									if (bLandImmerAnhaengen == false && mandantDto != null
											&& mandantDto.getPartnerDto().getLandplzortDto() != null
											&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null
											&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto()
													.getLandIIdGemeinsamespostland()
													.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
										sZeilen[7] = null;
									}

									if (bLandImmerAnhaengen == false
											&& partnerDto.getLandplzortDto().getLandDto()
													.getLandIIdGemeinsamespostland() != null
											&& mandantDto != null
											&& mandantDto.getPartnerDto().getLandplzortDto() != null
											&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto() != null
											&& mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getIID()
													.equals(partnerDto.getLandplzortDto().getLandDto().getIID())) {
										sZeilen[7] = null;
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
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_ANFRAGE,
								ParameterFac.PARAMETER_ANFRAGE_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_AUFTRAG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_BESTELLUNG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_ANGEBOT,
								ParameterFac.PARAMETER_ANGEBOT_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_LIEFERSCHEIN,
								ParameterFac.PARAMETER_LIEFERSCHEIN_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
						ParametermandantDto parametermandantDtoAnspAndrucken = getParameterFac().getMandantparameter(
								mandantDto.getCNr(), ParameterFac.KATEGORIE_RECHNUNG,
								ParameterFac.PARAMETER_RECHNUNG_ANSPRECHPARTNER_ANDRUCKEN);
						bAnsprechpartnerAndrucken = (Boolean) parametermandantDtoAnspAndrucken.getCWertAsObject();
					}

				}

				if (bAnsprechpartnerAndrucken == true) {
					sZeilen[3] = getPartnerFac().formatFixAnredeTitelName2Name1FuerAdresskopf(anspDto.getPartnerDto(),
							locale, null);
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
	 * @param session Session
	 * @throws EJBExceptionLP
	 */
	public static void closeSession(Session session) throws EJBExceptionLP {
		if (session != null) {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
	}

	protected BigDecimal mengeZugehoerigerArtikelNeuBerechnen(BelegpositionVerkaufDto belegPosDtoHauptposition,
			BelegpositionVerkaufDto belegPosDto, TheClientDto theClientDto) throws RemoteException {

		if (belegPosDtoHauptposition.getNMenge() == null) {
			return belegPosDto.getNMenge();
		}

		if (belegPosDtoHauptposition.getArtikelIId() == null) {
			return belegPosDto.getNMenge();
		}

		if (belegPosDto.getNMenge() != null && belegPosDto.getArtikelIId() != null) {

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(belegPosDtoHauptposition.getArtikelIId(),
					theClientDto);
			if (aDto.getFMultiplikatorZugehoerigerartikel() != null) {

				if (Helper.short2boolean(aDto.getBMultiplikatorInvers())) {

					BigDecimal bdMenge = belegPosDtoHauptposition.getNMenge();

					if (aDto.getFMultiplikatorZugehoerigerartikel().doubleValue() != 0) {
						bdMenge = bdMenge.multiply(
								BigDecimal.ONE.divide(new BigDecimal(aDto.getFMultiplikatorZugehoerigerartikel()), 12,
										BigDecimal.ROUND_HALF_EVEN));
					}

					if (Helper.short2boolean(aDto.getBMultiplikatorAufrunden())) {
						int naechstesGanzes = (int) Math.ceil(bdMenge.doubleValue());
						bdMenge = new BigDecimal(naechstesGanzes);
					}
					return Helper.rundeKaufmaennisch(bdMenge,
							getMandantFac().getNachkommastellenMenge(theClientDto.getMandant()));
				} else {
					return Helper.rundeKaufmaennisch(
							belegPosDtoHauptposition.getNMenge()
									.multiply(new BigDecimal(aDto.getFMultiplikatorZugehoerigerartikel())),
							getMandantFac().getNachkommastellenMenge(theClientDto.getMandant()));
				}

			} else {
				return belegPosDtoHauptposition.getNMenge();
			}

		} else {
			return null;
		}

	}

	protected final void pruefePflichtfelderBelegpositionDto(BelegpositionDto belegPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (belegPosDto.getPositionsartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegPosDto.getPositionsartCNr() == null"));
		}
		if (belegPosDto.getBelegIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegPosDto.getBelegIId() == null"));
		}

		if (belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			if (belegPosDto.getArtikelIId() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getArtikelIId() == null"));
			}
		}
		if (belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
				|| belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (belegPosDto.getNMenge() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNMenge() == null"));
			}

			if (belegPosDto.getEinheitCNr() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getEinheitCNr() == null"));
			}
		} else if (belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {

			if (belegPosDto.getXTextinhalt() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getXTextinhalt() == null"));
			}
		} else if (belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {

			if (belegPosDto.getMediastandardIId() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getMediastandardIId() == null"));
			}
		}

	}

	// PJ18659
	protected LPDatenSubreport getSubreportLiefergruppenTexte(BelegpositionDto[] belegpositionDtos,
			TheClientDto theClientDto) {

		String[] fieldnames = new String[] { "F_LIEFERGRUPPE", "F_TEXT" };
		HashSet hsBereitsGefunden = new HashSet();

		ArrayList alDaten = new ArrayList();

		for (int i = 0; i < belegpositionDtos.length; i++) {

			if (belegpositionDtos[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(belegpositionDtos[i].getArtikelIId(),
						theClientDto);
				if (aDto.getLfliefergruppeIId() != null) {

					if (!hsBereitsGefunden.contains(aDto.getLfliefergruppeIId())) {

						LfliefergruppeDto lflieferguppeDto = getLieferantServicesFac()
								.lfliefergruppeFindByPrimaryKey(aDto.getLfliefergruppeIId(), theClientDto);
						if (lflieferguppeDto.getLfliefergruppesprDto() != null
								&& lflieferguppeDto.getLfliefergruppesprDto().getXText() != null
								&& lflieferguppeDto.getLfliefergruppesprDto().getXText().length() > 0) {

							Object[] oZeile = new Object[2];

							if (lflieferguppeDto.getLfliefergruppesprDto().getCBez() != null) {
								oZeile[0] = lflieferguppeDto.getLfliefergruppesprDto().getCBez();
							} else {
								oZeile[0] = lflieferguppeDto.getCNr();

							}

							oZeile[1] = lflieferguppeDto.getLfliefergruppesprDto().getXText();
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

	protected BelegpositionVerkaufDto befuellePreisfelderAnhandVKPreisfindung(BelegpositionVerkaufDto bvDto,
			java.sql.Timestamp tBelegdatum, Integer kundeIId, String waehrungCNr, TheClientDto theClientDto) {

		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

		try {

			if (bvDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
					|| bvDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

				bvDto.setNNettoeinzelpreis(new BigDecimal(0));
				bvDto.setNBruttoeinzelpreis(new BigDecimal(0));
				bvDto.setNEinzelpreis(new BigDecimal(0));
				bvDto.setFRabattsatz(0D);
				bvDto.setNMaterialzuschlag(new BigDecimal(0));
				bvDto.setFZusatzrabattsatz(0D);
				bvDto.setNRabattbetrag(new BigDecimal(0));
				bvDto.setNMwstbetrag(new BigDecimal(0));

				if (bvDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

					Integer mwstsatzbezIId = null;
					ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
					boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(bvDto.getArtikelIId(), theClientDto);

					if (bDefaultMwstsatzAusArtikel) {

						mwstsatzbezIId = aDto.getMwstsatzbezIId();
					} else {
						mwstsatzbezIId = kdDto.getMwstsatzbezIId();
					}

//					MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzbezIId,
//							theClientDto);
					MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(mwstsatzbezIId, tBelegdatum,
							theClientDto);
					bvDto.setMwstsatzIId(mwstsatzDto.getIId());
					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
							bvDto.getArtikelIId(), kundeIId, bvDto.getNMenge(),
							new java.sql.Date(tBelegdatum.getTime()), kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
							mwstsatzDto.getIId(), waehrungCNr, true, theClientDto);

					VerkaufspreisDto verkaufspreisDtoInZielwaehrung = Helper.getVkpreisBerechnet(vkpreisfindungDto);

					if (verkaufspreisDtoInZielwaehrung != null) {

						bvDto.setNEinzelpreis(verkaufspreisDtoInZielwaehrung.einzelpreis);
						bvDto.setFRabattsatz(verkaufspreisDtoInZielwaehrung.rabattsatz);

						if (verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag != null) {
							bvDto.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis
									.add(verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag));
						} else {
							bvDto.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
						}

						bvDto.setNMaterialzuschlag(verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag);
						bvDto.setFZusatzrabattsatz(verkaufspreisDtoInZielwaehrung.getDdZusatzrabattsatz());

						// SP8552
						if (verkaufspreisDtoInZielwaehrung.bKommtVonFixpreis) {
							bvDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
							bvDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
						} else {
							bvDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							bvDto.setBRabattsatzuebersteuert(Helper.boolean2Short(true));
						}

						BigDecimal mwstBetrag = Helper.getProzentWert(bvDto.getNNettoeinzelpreis(),
								new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
						bvDto.setNMwstbetrag(mwstBetrag);

						bvDto.setNBruttoeinzelpreis(bvDto.getNNettoeinzelpreis().add(mwstBetrag));

						BigDecimal bdRabattbetrag = bvDto.getNEinzelpreis()
								.multiply(new BigDecimal(bvDto.getFRabattsatz().doubleValue())).movePointLeft(2);
						bvDto.setNRabattbetrag(bdRabattbetrag);

					}

				} else if (bvDto.getPositionsartCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

//					MwstsatzDto mwstsatzDto = getMandantFac()
//							.mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(), theClientDto);
					MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(kdDto.getMwstsatzbezIId(),
							tBelegdatum, theClientDto);
					bvDto.setMwstsatzIId(mwstsatzDto.getIId());
				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);

		}
		return bvDto;
	}

	protected final void pruefePflichtfelderBelegpositionDtoVerkauf(BelegpositionVerkaufDto belegPosDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		pruefePflichtfelderBelegpositionDto(belegPosDto, theClientDto);

		if (belegPosDto.getBRabattsatzuebersteuert() == null) {
			belegPosDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		}
		if (belegPosDto.getBMwstsatzuebersteuert() == null) {
			belegPosDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		}

		if (belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
				|| belegPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (belegPosDto.getMwstsatzIId() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getMwstsatzIId() == null"));
			}

			// Verkaufspreise pruefen
			if (belegPosDto.getNNettoeinzelpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNNettoeinzelpreis() == null"));
			}

			/*
			 * if (belegPosDto.getNNettoeinzelpreisplusversteckteraufschlag() == null) {
			 * throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new
			 * Exception(
			 * "belegPosDto.getNNettoeinzelpreisplusversteckteraufschlag() == null" )); }
			 */
			/*
			 * if (belegPosDto.getNEinzelpreisplusversteckteraufschlag() == null) { throw
			 * new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new
			 * Exception( "belegPosDto.getNEinzelpreisplusversteckteraufschlag() == null"
			 * )); }
			 */

			if (belegPosDto.getNBruttoeinzelpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNBruttoeinzelpreis() == null"));
			}
			if (belegPosDto.getNEinzelpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getNEinzelpreis() == null"));
			}

			if (belegPosDto.getFRabattsatz() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getFRabattsatz() == null"));
			}
			if (belegPosDto.getFZusatzrabattsatz() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getFZusatzrabattsatz() == null"));
			}
		}
	}

	public HashMap<Integer, BestellvorschlagDto[]> trenneNachStandorten(BestellvorschlagDto[] aBestellvorschlagDto) {
		HashMap<Integer, ArrayList<BestellvorschlagDto>> hmGetrenntNachStandorten = new HashMap<Integer, ArrayList<BestellvorschlagDto>>();

		for (int i = 0; i < aBestellvorschlagDto.length; i++) {

			Integer partnerIIdStandort = aBestellvorschlagDto[i].getPartnerIIdStandort();

			ArrayList<BestellvorschlagDto> alBV = new ArrayList<BestellvorschlagDto>();

			if (hmGetrenntNachStandorten.containsKey(partnerIIdStandort)) {
				alBV = hmGetrenntNachStandorten.get(partnerIIdStandort);
			} else {
				alBV = new ArrayList<BestellvorschlagDto>();
			}

			alBV.add(aBestellvorschlagDto[i]);

			hmGetrenntNachStandorten.put(partnerIIdStandort, alBV);
		}

		HashMap<Integer, BestellvorschlagDto[]> hmStandorte = new HashMap<Integer, BestellvorschlagDto[]>();

		Iterator it = hmGetrenntNachStandorten.keySet().iterator();
		while (it.hasNext()) {
			Integer partnerIIdStandort = (Integer) it.next();
			ArrayList<BestellvorschlagDto> alBV = hmGetrenntNachStandorten.get(partnerIIdStandort);
			hmStandorte.put(partnerIIdStandort,
					(BestellvorschlagDto[]) alBV.toArray(new BestellvorschlagDto[alBV.size()]));

		}

		return hmStandorte;
	}

	protected String extractLaufendeNummerAusBelegnummer(String sBelegnummer, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto pTrennzeichen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
			StringTokenizer st = new StringTokenizer(sBelegnummer, pTrennzeichen.getCWert());
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

	protected BelegpositionVerkaufDto befuellepositionBelegpositionDtoVerkauf(BelegpositionVerkaufDto vorherige,
			BelegpositionVerkaufDto aktuelle, TheClientDto theClientDto) {

		if (vorherige != null) {
			if (vorherige.getPositioniId() != null) {
				if (vorherige.getTypCNr() != null) {
					if (vorherige.getPositioniId() != null) {
						if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							if (aktuelle.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN)) {

							} else if (aktuelle.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
								BelegpositionVerkaufDto posDto = getPositionZuBelegpositionDtoVerkauf(vorherige,
										theClientDto);
								if (vorherige.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
									if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
										aktuelle.setPositioniId(null);
										aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
									}
								} else if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
									aktuelle.setPositioniId(posDto.getPositioniId());
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
								} else if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
									aktuelle.setPositioniId(posDto.getPositioniId());
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
								}
							} else {
								if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
									aktuelle.setPositioniId(null);
								} else if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
									aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
								}
							}
						} else if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						} else if (vorherige.getPositionsartCNr().equals(LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN)) {
							aktuelle.setPositioniId(vorherige.getIId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (vorherige.getPositionsartCNr().equals(LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
							BelegpositionVerkaufDto posDto = getPositionZuBelegpositionDtoVerkauf(vorherige,
									theClientDto);
							aktuelle.setPositioniId(posDto.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						} else if (vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE2);
						} else if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)
								&& vorherige.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					} else {
						if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_ALLES);
						} else if (vorherige.getPositionsartCNr().equals(LocaleFac.POSITIONSART_POSITION)) {
							aktuelle.setPositioniId(vorherige.getIId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					}
				} else {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
						aktuelle.setPositioniId(vorherige.getIId());
						aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					} else if (vorherige.getTypCNr() != null) {
						if (vorherige.getTypCNr().equalsIgnoreCase(LocaleFac.POSITIONTYP_EBENE1)) {
							aktuelle.setPositioniId(vorherige.getPositioniId());
							aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
						}
					}
				}
			} else {
				if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
						if (vorherige.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {

						} else {
							aktuelle.setPositioniId(vorherige.getIId());
						}
					} else {

					}
				} else {
					if (vorherige.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)
							&& vorherige.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
					} else if (vorherige.getPositionsartCNr().equals(LocaleFac.POSITIONSART_POSITION)
							&& vorherige.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN)) {
						aktuelle.setPositioniId(vorherige.getIId());
						aktuelle.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					}
				}
			}
		} else {
			if (aktuelle.getPositionsartCNr().equalsIgnoreCase(LocaleFac.POSITIONSART_POSITION)) {
				// aktuelle.setCZusatzbez(getPositionBez());
				// if(aktuelle.getCZusatzbez().equals(LocaleFac.POSITIONBEZ_BEGINN
				// ))
				// aktuelle.setTypCNr(getPositiontyp());
			}
		}
		return aktuelle;
	}

	protected BelegpositionVerkaufDto getPositionZuBelegpositionDtoVerkauf(BelegpositionVerkaufDto aktuelle,
			TheClientDto theClientDto) {
		BelegpositionVerkaufDto positionDto = null;
		try {
			if (aktuelle instanceof AuftragpositionDto) {
				positionDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(aktuelle.getPositioniId());
			} else if (aktuelle instanceof AngebotpositionDto) {
				positionDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey(aktuelle.getPositioniId(),
						theClientDto);
			} else if (aktuelle instanceof LieferscheinpositionDto) {
				positionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(aktuelle.getPositioniId(), theClientDto);
			} else if (aktuelle instanceof RechnungPositionDto) {
				positionDto = getRechnungFac().rechnungPositionFindByPrimaryKey(aktuelle.getPositioniId());
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
			parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
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
	protected final WebshopItemServiceFacLocal getWebshopItemServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopItemServiceFac();
	}

	protected final WebshopOrderServiceFacLocal getWebshopOrderServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopOrderServiceFac();
	}

	protected final WebshopOrderServiceFacLocal getWebshopCustomerOrderServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopCustomerOrderServiceFac();
	}

	protected final BatcherFac getBatcherFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBatcherFac();
	}

	protected Integer getUINachkommastellenPreisVK(String mandantCNr) {
		Integer iPreisRabatte = new Integer(4);
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
			iPreisRabatte = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return iPreisRabatte;
	}

	protected boolean parameterDazugehoertMengeNeuberechnen(String mandantCNr) {
		boolean neuberechnen = false;
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_DAZUGEHOERT_BERUECKSICHTIGEN);
			int iTemp = (Integer) parameter.getCWertAsObject();

			if (iTemp == 2) {
				neuberechnen = true;
			}
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return neuberechnen;
	}

	protected Integer getUINachkommastellenPreisEK(String mandantCNr) {
		Integer iPreisRabatte = new Integer(4);
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);
			iPreisRabatte = (Integer) parameter.getCWertAsObject();
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		return iPreisRabatte;
	}

	protected Query createNamedQuery(EntityManager em, String namedQuery, QueryParam[] params) {
		Query query = em.createNamedQuery(namedQuery);
		for (QueryParam entry : params) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}

	protected final WebshopCustomerServiceFacLocal getWebshopCustomerServiceFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getWebshopCustomerServiceFac();
	}

	protected final JCRMediaFac getJCRMediaFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJCRMediaFac();
	}

	protected final EmailMediaFac getEmailMediaFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getEmailMediaFac();
	}

	protected final EmailMediaLocalFac getEMailMediaLocalFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getEmailMediaLocalFac();
	}

	protected final BatcherSingleTransactionFac getBatcherSingleTransactionFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getBatcherSingleTransactionFac();
	}

	protected final PersonalApiFac getPersonalApiFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getPersonalApiFac();
	}

	/**
	 * SessionFacade fuer JcrDumpFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return JcrDumpFac
	 */
	protected final JcrDumpFac getJcrDumpFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJcrDumpFac();
	}

	protected final SepaImportFac getSepaImportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getSepaImportFac();
	}

	protected final JobAuslieferlisteFac getJobAuslieferlisteFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJobAuslieferlisteFac();
	}
	
	protected final AutoLumiquoteFac getAutoLumiquoteFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getAutoLumiqoteFac();
	}

	protected final EventLoggerCentralFacLocal getEventLoggerFac() {
		return oFacadeBeauftragter.getEventLoggerFac();
	}

	protected final FertigungImportFac getFertigungImportFac() {
		return oFacadeBeauftragter.getFertigungImportFac();
	}

	protected final TheClientFacLocal getTheClientFacLocal() throws EJBExceptionLP {
		return oFacadeBeauftragter.getTheClientFacLocal();
	}

	protected final Job4VendingExportFac getJob4VendingExportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJob4VendingExportFac();
	}

	protected final GTINGeneratorFac getGTINGeneratorFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getGTINGeneratorFac();
	}

	protected final LieferscheinFacLocal getLieferscheinFacLocal() {
		return oFacadeBeauftragter.getLieferscheinFacLocal();
	}

	protected final ServerDruckerFac getServerDruckerFac() {
		return oFacadeBeauftragter.getServerDruckerFac();
	}

	protected final ServerDruckerFacLocal getServerDruckerFacLocal() {
		return oFacadeBeauftragter.getServerDruckerFacLocal();
	}

	protected final Magento2CategoryFacLocal getMagento2CategoryFacLocal() {
		return oFacadeBeauftragter.getMagento2CategoryFacLocal();
	}

	protected final Magento2ProductFacLocal getMagento2ProductFacLocal() {
		return oFacadeBeauftragter.getMagento2ProductFacLocal();
	}

	protected final Magento2OrderFacLocal getMagento2OrderFacLocal() {
		return oFacadeBeauftragter.getMagento2OrderFacLocal();
	}

	protected final Magento2CustomerFacLocal getMagento2CustomerFacLocal() {
		return oFacadeBeauftragter.getMagento2CustomerFacLocal();
	}

	protected final JobDetailsSyncOrderFac getJobdetailsSyncOrderFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJobDetailsSyncOrderFac();
	}

	protected final JobDetailsSyncItemFac getJobdetailsSyncItemFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJobDetailsSyncItemFac();
	}

	protected final SyncShopFac getSyncShopFac(WebshopId shopId) throws EJBExceptionLP {
		return oFacadeBeauftragter.getSyncShopFac(shopId);
	}

	protected final JobDetailsErImportFac getJobDetailsErImportFac() {
		return oFacadeBeauftragter.getJobDetailsErImportFac();
	}

	protected final JobArbeitszeitstatusFac getJobArbeitszeitstatusFac() {
		return oFacadeBeauftragter.getJobArbeitszeitstatusFac();
	}

	protected final JobWEJournalFac getJobWEJournalFac() {
		return oFacadeBeauftragter.getJobWEJournalFac();
	}

	protected final JobDetailsWebabfrageArtikellieferantFac getJobWebabfrageArtikellieferant() {
		return oFacadeBeauftragter.getJobWebabfrageArtikellieferantFac();
	}

	protected final HvmaFac getHvmaFac() {
		return oFacadeBeauftragter.getHvmaFac();
	}

	protected final FertigungFacLocal getFertigungFacLocal() {
		return oFacadeBeauftragter.getFertigungFacLocal();
	}

	/**
	 * SessionFacade fuer KpiReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KpiReportFac
	 */
	protected final KpiReportFac getKpiReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getKpiReportFac();
	}

	protected final JobKpiReportFac getJobKpiReportFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJobKpiReportFac();
	}

	protected final JobBedarfsuebernahmeOffeneFac getJobBedarfsuebernahmeOffeneFac() throws EJBExceptionLP {
		return oFacadeBeauftragter.getJobBedarfsuebernahmeOffeneFac();
	}

	protected <T> void flushRemove(EntityManager em, Class<T> clazz, Integer pk) {
		T entity = em.find(clazz, pk);
		Validator.entityFound(entity, pk);

		// TODO Warum ist der try/catch notwenig, weder flush/remove wirft
		// EntityExistsException (ghp, 24.06.2020)
		try {
			em.remove(entity);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	protected final EdifactOrdersImportFac getEdifactOrdersImportFac() {
		return oFacadeBeauftragter.getEdifactOrdersImportFac();
	}

	public Integer erstelleBzwHoleKundeLieferadresseadresseAnhandName(String anrede, String name1, String name2,
			String firma, String strasse, String land, String plz, String ort, boolean bPartnernameZusammenziehen,
			TheClientDto theClientDto) {

		Session session3 = FLRSessionFactory.getFactory().openSession();
		String sQuery3 = "FROM FLRPartner p WHERE 1=1";

		if (firma != null && firma.length() > 0) {

			sQuery3 += " AND p.c_name1nachnamefirmazeile1='" + firma + "'";

			if (name2 != null) {
				sQuery3 += " AND p.c_name2vornamefirmazeile2='" + name2 + "'";
			}

			if (name1 != null) {
				sQuery3 += " AND p.c_name3vorname2abteilung='" + name1 + "'";
			}

		} else {

			if (bPartnernameZusammenziehen) {
				String name = name1;
				if (name2 != null) {
					name = name2 + " " + name1;
				}

				if (name != null) {
					sQuery3 += " AND p.c_name1nachnamefirmazeile1='" + name + "'";
				}
			} else {
				if (name1 != null) {
					sQuery3 += " AND p.c_name1nachnamefirmazeile1='" + name1 + "'";
				}

				if (name2 != null) {
					sQuery3 += " AND p.c_name2vornamefirmazeile2='" + name2 + "'";
				}
			}

		}

		if (strasse != null) {
			sQuery3 += " AND p.c_strasse='" + strasse + "'";
		}

		if (land != null) {
			sQuery3 += " AND p.flrlandplzort.flrland.c_lkz='" + land + "'";
		}

		if (plz != null) {
			sQuery3 += " AND p.flrlandplzort.c_plz='" + plz.replace("'", "") + "'";
		}

		if (ort != null) {
			sQuery3 += " AND p.flrlandplzort.flrort.c_name='" + ort + "'";
		}

		org.hibernate.Query query3 = session3.createQuery(sQuery3);

		List<?> results3 = query3.list();
		Iterator<?> resultListIterator3 = results3.iterator();

		PartnerDto pDto = null;

		try {
			if (resultListIterator3.hasNext()) {

				FLRPartner flrPartner = (FLRPartner) resultListIterator3.next();

				pDto = getPartnerFac().partnerFindByPrimaryKey(flrPartner.getI_id(), theClientDto);
			} else {
				pDto = new PartnerDto();

				String kbez = name1;

				String sN1 = " ";
				if (firma != null && firma.length() > 0) {
					sN1 = (firma == null) ? " " : firma + " ";
				} else {
					if (name1 != null) {
						sN1 = (name1 == null) ? " " : name1 + " ";
					}
				}

				int iE = sN1.indexOf(" ");
				if (iE > PartnerFac.MAX_KBEZ / 2) {
					iE = PartnerFac.MAX_KBEZ / 2;
				}
				kbez = sN1.substring(0, iE);

				pDto.setAnredeCNr(anrede);

				pDto.setCKbez(kbez);

				if (firma != null && firma.length() > 0) {
					pDto.setCName1nachnamefirmazeile1(firma);
					pDto.setCName2vornamefirmazeile2(name2);
					pDto.setCName3vorname2abteilung(name1);
				} else {

					if (bPartnernameZusammenziehen) {
						String name = name1;
						if (name2 != null) {
							name = name2 + " " + name1;
						}
						pDto.setCName1nachnamefirmazeile1(name);
					} else {
						pDto.setCName1nachnamefirmazeile1(name1);
						pDto.setCName2vornamefirmazeile2(name2);
					}

				}

				pDto.setCStrasse(strasse);

				pDto.setLandplzortIId(createLandplzortIfNeeded(land, plz, ort, theClientDto).getIId());

				pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
				pDto.setCAdressart(PartnerFac.ADRESSART_LIEFERADRESSE);

				pDto.setBVersteckt(false);
				pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

				Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

				pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
			}

			// Kunde dazu anlegen

			KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
					theClientDto.getMandant(), theClientDto);

			if (kundeDto == null) {

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				kundeDto = new KundeDto();
				kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());
				kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId());
				kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
				kundeDto.setbIstinteressent(new Short((short) 0));

				kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());

				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				// Vorbelegungen werden vom Mandanten geholt

				kundeDto.setMandantCNr(mandantDto.getCNr());
				kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
				kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
				kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
				kundeDto.setCWaehrung(mandantDto.getWaehrungCNr());
				kundeDto.setbIstinteressent(Helper.boolean2Short(false));
				kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
				kundeDto.setBDistributor(Helper.boolean2Short(false));
				kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
				kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
				kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
				kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
				kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
				kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
				kundeDto.setBReversecharge(Helper.boolean2Short(false));

				// damit die Debitorenkto. nicht anschlaegt.
				kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

				kundeDto.setPartnerDto(pDto);

				return getKundeFac().createKunde(kundeDto, theClientDto);

			} else {
				return kundeDto.getIId();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;
	}

	private LandplzortDto createLandplzortIfNeeded(String land, String plz, String ort, TheClientDto theClientDto)
			throws RemoteException {

		if (land == null) {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			land = mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz();
		}

		if (plz != null) {
			plz = plz.replace("'", "");
		}

		Integer landIId = null;
		LandDto landDto = getSystemFac().landFindByLkz(land);
		if (landDto == null) {
			landDto = new LandDto();
			landDto.setCLkz(land);
			landDto.setCName(land);
			landDto.setILaengeuidnummer(0);
			landDto.setBSepa(Helper.getShortFalse());
			landIId = getSystemFac().createLand(landDto, theClientDto);
			landDto = getSystemFac().landFindByPrimaryKey(landIId);
		} else {
			landIId = landDto.getIID();
		}

		Query queryOrt = em.createNamedQuery("OrtfindByCName");
		queryOrt.setParameter(1, ort);

		Collection results = queryOrt.getResultList();

		Integer ortIId = null;

		OrtDto ortDto = null;

		if (results.size() == 0) {

			ortDto = new OrtDto();
			ortDto.setCName(ort);
			ortIId = getSystemFac().createOrt(ortDto, theClientDto);
			ortDto = getSystemFac().ortFindByPrimaryKey(ortIId);
		} else {
			Ort ortBean = (Ort) results.iterator().next();
			ortIId = ortBean.getIId();
			ortDto = getSystemFac().ortFindByPrimaryKey(ortIId);
		}

		Query query = em.createNamedQuery("LandplzortfindByLandIIdByCPlzByOrtIId");
		query.setParameter(1, landIId);
		query.setParameter(2, plz);
		query.setParameter(3, ortIId);
		Integer lpoIId = null;
		if (query.getResultList().size() == 0) {
			LandplzortDto lpoDto = new LandplzortDto();
			lpoDto.setIlandID(landIId);
			lpoDto.setLandDto(landDto);
			lpoDto.setCPlz(plz);
			lpoDto.setOrtDto(ortDto);
			lpoDto.setOrtIId(ortIId);
			lpoIId = getSystemFac().createLandplzort(lpoDto, theClientDto);
		} else {
			Landplzort lp = (Landplzort) query.getResultList().iterator().next();
			lpoIId = lp.getIId();
		}

		return getSystemFac().landplzortFindByPrimaryKey(lpoIId);
	}

	public Integer erstelleBzwHoleKundeRechnungsadresseAnhandEmail(String anrede, String cEmail, String name1,
			String name2, String firma, String strasse, String land, String plz, String ort, String telefonnummer,
			boolean bREEmail, boolean bPartnernameZusammenziehen, TheClientDto theClientDto) {

		// Als erstes Partner Suchen
		List<Partner> partners = PartnerQuery.listByEmail(em, cEmail);

		Query query = em.createNamedQuery("KundefindByCEmailRechnungsempfangMandantCNr");
		query.setParameter(1, cEmail);
		query.setParameter(2, theClientDto.getMandant());
		Collection<Kunde> cl = query.getResultList();

		KundeDto kundeDto = null;

		try {
			PartnerDto pDto = null;
			if ((bREEmail == false && partners.size() == 0) || (bREEmail == true && cl.size() == 0)) {
				pDto = new PartnerDto();

				String kbez = name1;

				String sN1 = " ";

				if (firma != null && firma.length() > 0) {
					sN1 = (firma == null) ? " " : firma + " ";
				} else {
					if (name1 != null) {
						sN1 = (name1 == null) ? " " : name1 + " ";
					}
				}

				int iE = sN1.indexOf(" ");
				if (iE > PartnerFac.MAX_KBEZ / 2) {
					iE = PartnerFac.MAX_KBEZ / 2;
				}
				kbez = sN1.substring(0, iE);

				pDto.setAnredeCNr(anrede);

				pDto.setCKbez(kbez);
				if (firma != null && firma.length() > 0) {
					pDto.setCName1nachnamefirmazeile1(firma);
					pDto.setCName2vornamefirmazeile2(name2);
					pDto.setCName3vorname2abteilung(name1);
				} else {

					if (bPartnernameZusammenziehen) {
						String name = name1;
						if (name2 != null) {
							name = name2 + " " + name1;
						}
						pDto.setCName1nachnamefirmazeile1(name);
					} else {
						pDto.setCName1nachnamefirmazeile1(name1);
						pDto.setCName2vornamefirmazeile2(name2);
					}

				}

				pDto.setCStrasse(strasse);

				pDto.setCEmail(cEmail);

				pDto.setLandplzortIId(createLandplzortIfNeeded(land, plz, ort, theClientDto).getIId());

				pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
				pDto.setBVersteckt(false);
				pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

				pDto.setCTelefon(telefonnummer);

				Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

				pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

			} else {
				if (bREEmail == false && partners.size() > 0) {

					Partner partner = partners.get(0);
					pDto = getPartnerFac().partnerFindByPrimaryKey(partner.getIId(), theClientDto);
					kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
							theClientDto.getMandant(), theClientDto);
				} else if (bREEmail == true && cl.size() > 0) {
					Kunde kunde = (Kunde) cl.iterator().next();
					kundeDto = getKundeFac().kundeFindByPrimaryKey(kunde.getIId(), theClientDto);
				}
			}

			// Kunde dazu anlegen

			if (kundeDto == null) {

				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				kundeDto = new KundeDto();

				Integer mwstsatzbezIId = mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz();
				if (pDto.getLandplzortIId() != null) {

					Integer mwstsatzbezIIdTemp = getPartnerFac().getDefaultMWSTSatzIIdAnhandLand(
							getSystemFac().landplzortFindByPrimaryKey(pDto.getLandplzortIId()).getLandDto(),
							theClientDto);
					if (mwstsatzbezIIdTemp != null) {
						mwstsatzbezIId = mwstsatzbezIIdTemp;
					}

				}

				kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));

				if (bREEmail == true) {
					kundeDto.setCEmailRechnungsempfang(cEmail);
					kundeDto.setBIstreempfaenger(Helper.boolean2Short(true));
				}

				kundeDto.setMwstsatzbezIId(mwstsatzbezIId);

				kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz());
				kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId());
				kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());
				kundeDto.setbIstinteressent(new Short((short) 0));

				kundeDto.setPersonaliIdProvisionsempfaenger(theClientDto.getIDPersonal());

				kundeDto.setBVersteckterlieferant(Helper.boolean2Short(false));
				// Vorbelegungen werden vom Mandanten geholt

				kundeDto.setMandantCNr(mandantDto.getCNr());
				kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde());
				kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde());
				kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde());
				kundeDto.setCWaehrung(mandantDto.getWaehrungCNr());
				kundeDto.setbIstinteressent(Helper.boolean2Short(false));
				kundeDto.setBAkzeptiertteillieferung(Helper.boolean2Short(false));
				kundeDto.setBDistributor(Helper.boolean2Short(false));

				kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
				kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
				kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
				kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
				kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
				kundeDto.setBReversecharge(Helper.boolean2Short(false));

				// damit die Debitorenkto. nicht anschlaegt.
				kundeDto.setUpdateModeDebitorenkonto(KundeDto.I_UPD_DEBITORENKONTO_KEIN_UPDATE);

				kundeDto.setPartnerDto(pDto);

				return getKundeFac().createKunde(kundeDto, theClientDto);

			} else {
				return kundeDto.getIId();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	/**
	 * Ruft die angegebene Methode mit den &uuml;bergebenen PrimaryKeys auf
	 * 
	 * @param methodName   die zu verwendende Methode, beispielsweise
	 *                     "artikelFindByPrimaryKey"
	 * @param keys         die Liste der Keys f&uuml;r die die Dtos ermittelt werden
	 *                     sollen Ist der gleiche key mehrfach angegeben, wird nur
	 *                     beim ersten die Methode (methodName) verwendet
	 * @param theClientDto
	 * @return die Map der Dtos mit dem PrimaryKey als Key
	 * @throws NoSuchMethodException
	 */
	public Map<Integer, Object> objFindByNameClientPrimaryKeys(String methodName, Collection<Integer> keys,
			TheClientDto theClientDto) throws NoSuchMethodException {
		Validator.notEmpty(methodName, "methodName");

		Method m = getClass().getMethod(methodName, Integer.class, TheClientDto.class);
		Map<Integer, Object> values = new HashMap<Integer, Object>();
		for (Integer key : keys) {
			try {
				if (!values.containsKey(key)) {
					Object o = m.invoke(this, key, theClientDto);
					values.put(key, o);
				}
			} catch (IllegalAccessException e) {

			} catch (IllegalArgumentException e) {

			} catch (InvocationTargetException e) {

			}
		}
		return values;

	}

	public Map<Integer, Object> objFindByNamePrimaryKeys(String methodName, Collection<Integer> keys)
			throws NoSuchMethodException {
		Validator.notEmpty(methodName, "methodName");

		Method m = getClass().getMethod(methodName, Integer.class);
		Map<Integer, Object> values = new HashMap<Integer, Object>();
		for (Integer key : keys) {
			try {
				if (!values.containsKey(key)) {
					Object o = m.invoke(this, key);
					values.put(key, o);
				}
			} catch (IllegalAccessException e) {

			} catch (IllegalArgumentException e) {

			} catch (InvocationTargetException e) {

			}
		}

		return values;
	}

	public <T extends BaseIntegerKey> Map<T, Object> objFindByNamePrimaryBaseIntegerKeys(String methodName,
			Collection<T> keys) throws NoSuchMethodException {
		Validator.notEmpty(methodName, "methodName");
		Map<T, Object> values = new HashMap<T, Object>();

		if (keys.isEmpty())
			return values;

		// Der erste Key definiert den Datentyp des Parameters
		Class keyClass = keys.iterator().next().getClass();
		Method m = getClass().getMethod(methodName, keyClass);
		for (T key : keys) {
			try {
				if (!key.getClass().equals(keyClass)) {
					throw EJBExcFactory.differentKeyTypes(keyClass, key.getClass());
				}

				if (!values.containsKey(key)) {
					Object o = m.invoke(this, key);
					values.put(key, o);
				}
			} catch (IllegalAccessException e) {
				myLogger.error("IllegalAccess", e);
			} catch (IllegalArgumentException e) {
				myLogger.error("IllegalArgument", e);
			} catch (InvocationTargetException e) {
				myLogger.error("Invocation", e);
			}
		}

		return values;
	}
}
