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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.anfrage.ejbfac.AnfrageFacBean;
import com.lp.server.anfrage.ejbfac.AnfragepositionFacBean;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.angebot.ejbfac.AngebotFacBean;
import com.lp.server.angebot.ejbfac.AngebotReportFacBean;
import com.lp.server.angebot.ejbfac.AngebotServiceFacBean;
import com.lp.server.angebot.ejbfac.AngebotpositionFacBean;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebotstkl.ejbfac.AngebotstklFacBean;
import com.lp.server.angebotstkl.ejbfac.AngebotstklpositionFacBean;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.ejbfac.ArtikelbestelltFacBean;
import com.lp.server.artikel.ejbfac.FehlmengeFacBean;
import com.lp.server.artikel.ejbfac.GTINGeneratorFac;
import com.lp.server.artikel.ejbfac.GTINGeneratorFacBean;
import com.lp.server.artikel.ejbfac.JobDetailsWebabfrageArtikellieferantFacBean;
import com.lp.server.artikel.ejbfac.LagerFacBean;
import com.lp.server.artikel.ejbfac.LagerReportFacBean;
import com.lp.server.artikel.ejbfac.MaterialFacBean;
import com.lp.server.artikel.ejbfac.RahmenbedarfeFacBean;
import com.lp.server.artikel.ejbfac.ReservierungFacBean;
import com.lp.server.artikel.ejbfac.VkPreisfindungFacBean;
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
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.artikel.service.WebshopItemServiceFacLocal;
import com.lp.server.auftrag.ejbfac.AuftragFacBean;
import com.lp.server.auftrag.ejbfac.AuftragRahmenAbrufFacBean;
import com.lp.server.auftrag.ejbfac.AuftragReportFacBean;
import com.lp.server.auftrag.ejbfac.AuftragServiceFacBean;
import com.lp.server.auftrag.ejbfac.AuftragpositionFacBean;
import com.lp.server.auftrag.ejbfac.AuftragteilnehmerFacBean;
import com.lp.server.auftrag.ejbfac.EdifactOrdersImportFacBean;
import com.lp.server.auftrag.ejbfac.WebshopOrderServiceEjb;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;
import com.lp.server.auftrag.service.EdifactOrdersImportFac;
import com.lp.server.auftrag.service.WebshopOrderServiceFacLocal;
import com.lp.server.benutzer.ejbfac.BenutzerFacBean;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacBean;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.benutzer.ejbfac.LogonFacBean;
import com.lp.server.benutzer.ejbfac.RechteFacBean;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.ejbfac.BSMahnwesenFacBean;
import com.lp.server.bestellung.ejbfac.BestellpositionFacBean;
import com.lp.server.bestellung.ejbfac.BestellungReportFacBean;
import com.lp.server.bestellung.ejbfac.BestellvorschlagFacBean;
import com.lp.server.bestellung.ejbfac.JobWEJournalFacBean;
import com.lp.server.bestellung.ejbfac.WareneingangFacBean;
import com.lp.server.bestellung.service.BSMahnwesenFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.JobWEJournalFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.eingangsrechnung.ejbfac.EingangsrechnungFacBean;
import com.lp.server.eingangsrechnung.ejbfac.EingangsrechnungReportFacBean;
import com.lp.server.eingangsrechnung.ejbfac.EingangsrechnungServiceFacBean;
import com.lp.server.eingangsrechnung.ejbfac.JobDetailsErImportFacBean;
import com.lp.server.eingangsrechnung.ejbfac.ZahlungsvorschlagFacBean;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.JobDetailsErImportFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.fertigung.ejbfac.FertigungFacBean;
import com.lp.server.fertigung.ejbfac.FertigungFacLocal;
import com.lp.server.fertigung.ejbfac.FertigungImportFacBean;
import com.lp.server.fertigung.ejbfac.FertigungReportFacBean;
import com.lp.server.fertigung.ejbfac.FertigungServiceFacBean;
import com.lp.server.fertigung.ejbfac.InternebestellungFacBean;
import com.lp.server.fertigung.ejbfac.JobArbeitszeitstatusFacBean;
import com.lp.server.fertigung.ejbfac.JobBedarfsuebernahmeOffeneFacBean;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungImportFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.JobArbeitszeitstatusFac;
import com.lp.server.fertigung.service.JobBedarfsuebernahmeOffeneFac;
import com.lp.server.finanz.ejbfac.BelegbuchungFacBean;
import com.lp.server.finanz.ejbfac.BelegbuchungIstversteurerFacBean;
import com.lp.server.finanz.ejbfac.BelegbuchungMischversteurerFacBean;
import com.lp.server.finanz.ejbfac.BuchenFacBean;
import com.lp.server.finanz.ejbfac.FibuExportFacBean;
import com.lp.server.finanz.ejbfac.FinanzFacBean;
import com.lp.server.finanz.ejbfac.FinanzReportFacBean;
import com.lp.server.finanz.ejbfac.FinanzServiceFacBean;
import com.lp.server.finanz.ejbfac.MahnwesenFacBean;
import com.lp.server.finanz.ejbfac.SepaImportFacBean;
import com.lp.server.finanz.service.BelegbuchungFac;
import com.lp.server.finanz.service.BelegbuchungIstversteurerFac;
import com.lp.server.finanz.service.BelegbuchungMischversteuererFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.forecast.ejbfac.ForecastFacBean;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.inserat.ejbfac.InseratFacBean;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.instandhaltung.ejbfac.InstandhaltungFacBean;
import com.lp.server.instandhaltung.service.InstandhaltungFac;
import com.lp.server.kpi.ejbfac.KpiReportFacBean;
import com.lp.server.kpi.service.KpiReportFac;
import com.lp.server.kueche.ejbfac.KuecheFacBean;
import com.lp.server.kueche.ejbfac.KuecheReportFacBean;
import com.lp.server.kueche.service.KuecheFac;
import com.lp.server.kueche.service.KuecheReportFac;
import com.lp.server.lieferschein.ejbfac.AusliefervorschlagFacBean;
import com.lp.server.lieferschein.ejbfac.LieferscheinFacBean;
import com.lp.server.lieferschein.ejbfac.LieferscheinFacLocal;
import com.lp.server.lieferschein.ejbfac.LieferscheinReportFacBean;
import com.lp.server.lieferschein.ejbfac.LieferscheinServiceFacBean;
import com.lp.server.lieferschein.ejbfac.LieferscheinpositionFacBean;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.media.ejbfac.EmailMediaFacBean;
import com.lp.server.media.ejbfac.EmailMediaLocalFac;
import com.lp.server.media.ejbfac.EmailMediaLocalFacBean;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.partner.ejbfac.AnsprechpartnerFacBean;
import com.lp.server.partner.ejbfac.BankFacBean;
import com.lp.server.partner.ejbfac.KundeFacBean;
import com.lp.server.partner.ejbfac.KundeReportFacBean;
import com.lp.server.partner.ejbfac.KundesachbearbeiterFacBean;
import com.lp.server.partner.ejbfac.KundesokoFacBean;
import com.lp.server.partner.ejbfac.LieferantFacBean;
import com.lp.server.partner.ejbfac.LieferantServicesFacBean;
import com.lp.server.partner.ejbfac.PartnerFacBean;
import com.lp.server.partner.ejbfac.PartnerReportFacBean;
import com.lp.server.partner.ejbfac.PartnerServicesFacBean;
import com.lp.server.partner.ejbfac.PartnerServicesFacLocal;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundesachbearbeiterFac;
import com.lp.server.partner.service.KundesokoFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantServicesFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.WebshopCustomerServiceFacLocal;
import com.lp.server.personal.ejbfac.HvmaFacBean;
import com.lp.server.personal.ejbfac.MaschineFacBean;
import com.lp.server.personal.ejbfac.NachrichtenFacBean;
import com.lp.server.personal.ejbfac.PersonalApiFacBean;
import com.lp.server.personal.ejbfac.PersonalFacBean;
import com.lp.server.personal.ejbfac.ReisekostenFacBean;
import com.lp.server.personal.ejbfac.SchichtFacBean;
import com.lp.server.personal.ejbfac.ZeiterfassungFacBean;
import com.lp.server.personal.ejbfac.ZeiterfassungReportFacBean;
import com.lp.server.personal.ejbfac.ZutrittscontrollerFacBean;
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
import com.lp.server.projekt.ejbfac.ProjektFacBean;
import com.lp.server.projekt.ejbfac.ProjektServiceFacBean;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.ejbfac.AbrechnungsvorschlagFacBean;
import com.lp.server.rechnung.ejbfac.RechnungFacBean;
import com.lp.server.rechnung.ejbfac.RechnungReportFacBean;
import com.lp.server.rechnung.ejbfac.RechnungServiceFacBean;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.reklamation.ejbfac.ReklamationFacBean;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.shop.ejbfac.JobDetailsSyncItemFac;
import com.lp.server.shop.ejbfac.JobDetailsSyncItemFacBean;
import com.lp.server.shop.ejbfac.JobDetailsSyncOrderFac;
import com.lp.server.shop.ejbfac.JobDetailsSyncOrderFacBean;
import com.lp.server.shop.ejbfac.ShopTimerCronJobLocal;
import com.lp.server.shop.ejbfac.ShopTimerCronJobs;
import com.lp.server.shop.ejbfac.ShopTimerFacBean;
import com.lp.server.shop.ejbfac.SyncNichtZutreffendFacBean;
import com.lp.server.shop.magento2.Magento2CategoryFacBean;
import com.lp.server.shop.magento2.Magento2CategoryFacLocal;
import com.lp.server.shop.magento2.Magento2CustomerFacBean;
import com.lp.server.shop.magento2.Magento2CustomerFacLocal;
import com.lp.server.shop.magento2.Magento2OrderFacBean;
import com.lp.server.shop.magento2.Magento2OrderFacLocal;
import com.lp.server.shop.magento2.Magento2ProductFacBean;
import com.lp.server.shop.magento2.Magento2ProductFacLocal;
import com.lp.server.shop.magento2.SyncMagento2FacBean;
import com.lp.server.shop.service.ShopTimerFac;
import com.lp.server.shop.service.SyncShopFac;
import com.lp.server.stueckliste.ejbfac.StuecklisteFacBean;
import com.lp.server.stueckliste.ejbfac.StuecklisteReportFacBean;
import com.lp.server.stueckliste.ejbfac.StuecklisteimportFacBean;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.system.ejbfac.AutoBestellvorschlagFacBean;
import com.lp.server.system.ejbfac.AutoFehlmengendruckFacBean;
import com.lp.server.system.ejbfac.AutoLoseerledigenFacBean;
import com.lp.server.system.ejbfac.AutoLumiquoteFacBean;
import com.lp.server.system.ejbfac.AutoMahnenFacBean;
import com.lp.server.system.ejbfac.AutoMahnungsversandFacBean;
import com.lp.server.system.ejbfac.AutoMonatsabrechnungversandFacBean;
import com.lp.server.system.ejbfac.AutoPaternosterFacBean;
import com.lp.server.system.ejbfac.AutoRahmendetailbedarfdruckFacBean;
import com.lp.server.system.ejbfac.AutomatikjobFacBean;
import com.lp.server.system.ejbfac.AutomatikjobtypeFacBean;
import com.lp.server.system.ejbfac.AutomatiktimerFacBean;
import com.lp.server.system.ejbfac.BatcherFac;
import com.lp.server.system.ejbfac.BatcherFacBean;
import com.lp.server.system.ejbfac.BatcherSingleTransactionFac;
import com.lp.server.system.ejbfac.BatcherSingleTransactionFacBean;
import com.lp.server.system.ejbfac.BelegartmediaFacBean;
import com.lp.server.system.ejbfac.BelegpositionkonvertierungFacBean;
import com.lp.server.system.ejbfac.DokumenteFacBean;
import com.lp.server.system.ejbfac.DruckerFacBean;
import com.lp.server.system.ejbfac.EventLoggerCentralFacBean;
import com.lp.server.system.ejbfac.EventLoggerCentralFacLocal;
import com.lp.server.system.ejbfac.IntelligenterStklImportFacBean;
import com.lp.server.system.ejbfac.Job4VendingExportFacBean;
import com.lp.server.system.ejbfac.JobAuslieferlisteFacBean;
import com.lp.server.system.ejbfac.JobKpiReportFacBean;
import com.lp.server.system.ejbfac.LocaleFacBean;
import com.lp.server.system.ejbfac.MandantFacBean;
import com.lp.server.system.ejbfac.MediaFacBean;
import com.lp.server.system.ejbfac.PanelFacBean;
import com.lp.server.system.ejbfac.ParameterFacBean;
import com.lp.server.system.ejbfac.PflegeFacBean;
import com.lp.server.system.ejbfac.ReportConnectionFacBean;
import com.lp.server.system.ejbfac.ReportConnectionFacLocal;
import com.lp.server.system.ejbfac.ServerDruckerFacBean;
import com.lp.server.system.ejbfac.ServerDruckerFacLocal;
import com.lp.server.system.ejbfac.SystemFacBean;
import com.lp.server.system.ejbfac.SystemMultilanguageFacBean;
import com.lp.server.system.ejbfac.SystemReportFacBean;
import com.lp.server.system.ejbfac.SystemReportFacLocal;
import com.lp.server.system.ejbfac.SystemServicesFacBean;
import com.lp.server.system.ejbfac.TheClientFacBean;
import com.lp.server.system.ejbfac.TheClientFacLocal;
import com.lp.server.system.ejbfac.TheJudgeFacBean;
import com.lp.server.system.ejbfac.VersandFacBean;
import com.lp.server.system.fastlanereader.ejb.FastLaneReaderBean;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.jcr.ejbfac.JCRDocFacBean;
import com.lp.server.system.jcr.ejbfac.JCRMediaFacBean;
import com.lp.server.system.jcr.ejbfac.JcrDumpFacBean;
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
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.PflegeFac;
import com.lp.server.system.service.ServerDruckerFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemMultilanguageFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheJudgeFac;
import com.lp.server.system.service.VersandFac;
import com.lp.util.EJBExceptionLP;

/**
 * 
 * <p>
 * <I>In dieser Klasse stehen alle unsere Facades zur Verfuegung.</I>
 * </p>
 * <br>
 * Jeder, der auf die Facades zugreifen moechte, muss diese Klasse
 * instantiieren. <br>
 * beauftragt: 0
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>17. 01. 2005</I>
 * </p>
 * 
 * verantwortlich: Martin Bluehweis
 * 
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class FacadeBeauftragter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PKGeneratorObj pkGeneratorObj = null;
	private BelegnummerGeneratorObj bnGeneratorObj = null;
	private TheJudgeFac theJudgeFac = null;
	private TheClientFac theClientFac = null;
	private AnsprechpartnerFac ansprechpartnerFac = null;
	private AuftragFac auftragFac = null;
	private AuftragpositionFac auftragpositionFac = null;
	private AuftragReportFac auftragReportFac = null;
	private AuftragServiceFac auftragServiceFac = null;
	private AnfrageFac anfrageFac = null;
	private AnfragepositionFac anfragepositionFac = null;
	private AnfrageServiceFac anfrageServiceFac = null;
	private BestellungFac bestellungFac = null;
	private BestellungServiceFac bestellungServiceFac = null;
	private BestellpositionFac bestellpositionFac = null;
	private KundeFac kundeFac = null;
	private KundeReportFac kundeReportFac = null;
	private KundesachbearbeiterFac kundesachbearbeiterFac = null;
	private LagerFac lagerFac = null;
	private LagerReportFac lagerReportFac = null;
	private WareneingangFac wareneingangFac = null;
	private BestellvorschlagFac bestellvorschlagFac = null;
	private LieferscheinFac lieferscheinFac = null;
	private LieferscheinpositionFac lieferscheinpositionFac = null;
	private LieferscheinReportFac lieferscheinReportFac = null;
	private LieferscheinServiceFac lieferscheinServiceFac = null;
	private LocaleFac localeFac = null;
	private MandantFac mandantFac = null;
	private NachrichtenFac nachrichtenFac = null;
	private PartnerFac partnerFac = null;
	private PanelFac panelFac = null;
	private PflegeFac pflegeFac = null;
	private PartnerServicesFac partnerServicesFac = null;
	private PersonalFac personalFac = null;
	private ReklamationFac reklamationFac = null;
	private ReservierungFac reservierungFac = null;
	private RechnungFac rechnungFac = null;
	private AbrechnungsvorschlagFac abrechnungsvorschlagFac = null;
	private BuchenFac buchenFac = null;
	private FinanzFac finanzFac = null;
	private FinanzServiceFac finanzServiceFac = null;
	private FinanzReportFac finanzReportFac = null;
	private MahnwesenFac mahnwesenFac = null;
	private SystemMultilanguageFac systemMultilanguageFac = null;
	private SystemFac systemFac = null;
	private SystemServicesFac systemServicesFac = null;
	private VkPreisfindungFac vkPreisfindungFac = null;
	private ArtikelFac artikelFac = null;
	private ArtikelServiceFac artikelServiceFac = null;
	private ArtikelReportFac artikelReportFac = null;
	private ParameterFac parameterFac = null;
	private LieferantFac lieferantFac = null;
	private LieferantServicesFac lieferantServicesFac = null;
	private RechnungReportFac rechnungReportFac = null;
	private RechnungServiceFac rechnungServiceFac = null;
	private EingangsrechnungFac eingangsrechnungFac = null;
	private EingangsrechnungReportFac eingangsrechnungReportFac = null;
	private EingangsrechnungServiceFac eingangsrechnungServiceFac = null;
	private ZeiterfassungFac zeiterfassungFac = null;
	private ReisekostenFac reisekostenFac = null;
	private ZeiterfassungReportFac zeiterfassungReportFac = null;
	private VersandFac versandFac = null;
	private BankFac bankFac = null;
	private BenutzerFac benutzerFac = null;
	private MaterialFac materialFac = null;
	private MediaFac mediaFac = null;
	private ArtikelbestelltFac artikelbestelltFac = null;
	private RechteFac rechteFac = null;
	private AngebotFac angebotFac = null;
	private AusliefervorschlagFac ausliefervorschlagFac = null;
	private SchichtFac schichtFac = null;
	private AngebotpositionFac angebotpositionFac = null;
	private AngebotReportFac angebotReportFac = null;
	private AngebotServiceFac angebotServiceFac = null;
	private FertigungFac fertigungFac = null;
	private FertigungReportFac fertigungReportFac = null;
	private FertigungServiceFac fertigungServiceFac = null;
	private StuecklisteFac stuecklisteFac = null;
	private StuecklisteimportFac stuecklisteimportFac = null;
	private InventurFac inventurFac = null;
	private FehlmengeFac fehlmengeFac = null;
	private BSMahnwesenFac bsmahnwesenFac = null;
	private ArtikelkommentarFac artikelkommentarFac = null;
	private PartnerReportFac partnerReportFac = null;
	private InternebestellungFac internebestellungFac = null;
	private AngebotstklFac angebotstklFac = null;
	private ForecastFac forecastFac = null;
	private AngebotstklpositionFac angebotstklpositionFac = null;
	private FibuExportFac fibuExportFac = null;
	private DruckerFac druckerFac = null;
	private BelegartmediaFac belegartmediaFac = null;
	private KundesokoFac kundesokoFac = null;
	private BelegbuchungFac belegbuchungFac = null;
	private BelegbuchungFac belegbuchungIstversteurerFac = null;
	private BelegbuchungFac belegbuchungMischversteurerFac = null;
	private DokumenteFac dokumenteFac = null;
	private ZutrittscontrollerFac zutrittscontrollerFac = null;
	private ProjektFac projektFac = null;
	private ProjektServiceFac projektServiceFac = null;
	private ZahlungsvorschlagFac zahlungsvorschlagFac = null;
	private BelegpositionkonvertierungFac belegpositionkonvertierungFac = null;
	private RahmenbedarfeFac rahmenbedarfeFac = null;
	private SystemReportFac systemReportFac = null;
	private SystemReportFacLocal systemReportFacLocal = null;
	private AuftragteilnehmerFac auftragteilnehmerFac = null;
	private StuecklisteReportFac stuecklisteReportFac = null;
	private AutoFehlmengendruckFac autoFehlmengendruckFac = null;
	private AutoMahnungsversandFac autoMahnungsversandFac = null;
	private AutoMonatsabrechnungversandFac autoMonatsabrechnungversandFac = null;
	private AutoLoseerledigenFac autoLoseerledigenFac = null;
	private AutoMahnenFac autoMahnenFac = null;
	private BestellungReportFac bestellungReportFac = null;
	private AutoBestellvorschlagFac autoBestellvorschlagFac = null;
	private AutoPaternosterFac autoPaternosterFac = null;
	private InstandhaltungFac instandhaltungFac = null;
	private AutomatikjobFac automatikjobFac = null;
	private AutomatikjobtypeFac automatikjobtypeFac = null;
	private AutomatiktimerFac automatiktimerFac = null;
	private LogonFac logonFac = null;
	private JCRDocFac jcrDocFac = null;
	private AutoRahmendetailbedarfdruckFac autoRahmendetailbedarfdruckFac = null;
	private KuecheFac kuecheFac = null;
	private BelegVerkaufFac belegVerkaufFac = null;
	private KuecheReportFac kuecheReportFac = null;
	private AuftragRahmenAbrufFac auftragRahmenAbrufFac = null;
	private BenutzerServicesFacLocal benutzerServicesFac = null;
	private ShopTimerCronJobLocal shopTimerCronJob = null;
	private ShopTimerFac shopTimerFac = null;
	
	private FastLaneReader fastLaneReader = null;
	private WebshopItemServiceFacLocal webshopItemServicesFac = null;
	private WebshopOrderServiceFacLocal webshopOrderServicesFac = null;
	private WebshopOrderServiceFacLocal webshopCustomerOrderServicesFac = null;
	private InseratFac inseratFac = null;
	private WebshopCustomerServiceFacLocal webshopCustomerServicesFac = null;
	private JCRMediaFac jcrMediaFac = null;
	private EmailMediaFac emailMediaFac = null;
	private EmailMediaLocalFac emailMediaLocalFac = null;
	private BatcherFac batcherFac = null;
	private BatcherSingleTransactionFac batcherSingleTransactionFac = null;
	private MaschineFac maschineFac = null;
	private ArtikelimportFac artikelimportFac = null;
	private PersonalApiFac personalApiFac = null;
	private IntelligenterStklImportFac intellStklimportFac = null;
	private JcrDumpFac jcrDumpFac = null;
	private SepaImportFac sepaImportFac = null;
	private JobAuslieferlisteFac jobAuslieferlisteFac = null;
	private AutoLumiquoteFac autoLumiquoteFac = null;
	private EventLoggerCentralFacLocal eventLoggerCentralFac = null;
	private FertigungImportFac fertigungImportFac = null;
	private TheClientFacLocal theClientFacLocal = null;
	private Job4VendingExportFac job4VendingExportFac = null;
	private GTINGeneratorFac gtinGeneratorFac = null;
	private LieferscheinFacLocal lieferscheinFacLocal = null;
	private ServerDruckerFac serverDruckerFac = null;
	private ServerDruckerFacLocal serverDruckerFacLocal = null;
	private Magento2CategoryFacLocal magento2CategoryFacBean = null;
	private Magento2ProductFacLocal magento2ProductFacBean = null;
	private Magento2OrderFacLocal magento2OrderFacBean = null;
	private Magento2CustomerFacLocal magento2CustomerFacBean = null;
	private JobDetailsSyncOrderFac jobDetailsSyncOrderFacBean = null;
	private JobDetailsSyncItemFac jobDetailsSyncItemFacBean = null;
	private Map<WebshopId, SyncShopFac> syncShopFacBeans = new HashMap<WebshopId, SyncShopFac>();
	private JobDetailsErImportFac jobDetailsErImportFac = null;
	private JobArbeitszeitstatusFac jobArbeitszeitstatusFac = null;
	private JobWEJournalFac jobWEJournalFac = null;
	private ReportConnectionFacLocal reportConnectionFacLocal = null;
	private JobDetailsWebabfrageArtikellieferantFac jobDetailsWebabfrageArtliefFac;
	private FertigungFacLocal fertigungFacLocal = null;
	private PartnerServicesFacLocal partnerServicesFacLocal = null;

	private HvmaFac hvmaFac;
	private KpiReportFac kpiReportFac = null;
	private JobKpiReportFac jobKpiReportFac = null;
	private JobBedarfsuebernahmeOffeneFac jobBedarfsuebernahmeOffenFac = null;
	private EdifactOrdersImportFac edifactOrdersImportFac = null;
	
	@PersistenceContext
	private EntityManager em;

	public Context context;

	public FacadeBeauftragter() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Context getInitialContext() {
		return context;
	}

	/**
	 * Hole die Clientinformationen des Users.
	 * 
	 * @param idUser String
	 * @return TheClientDto
	 * @throws EJBExceptionLP
	 */
	public final TheClientDto getTheClient(String idUser) throws EJBExceptionLP {
		try {
			return getTheClientFac().theClientFindByPrimaryKey(idUser);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	private <T> T lookupFac(Class<? extends Facade> bean, Class<T> callInterface) {
		return FacLookup.lookup(context, bean, callInterface);
	}

	private <T> T lookupLocalFac(Class<? extends Facade> bean, Class<T> callInterface) {
		return FacLookup.lookupLocal(context, bean, callInterface);
	}


	/**
	 * SessionFacade fuer TheClient holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public TheClientFac getTheClientFac() throws EJBExceptionLP {
		if (theClientFac == null) {
			theClientFac = lookupFac(TheClientFacBean.class, TheClientFac.class);
		}
		return theClientFac;

		// try {
		// if (theClientFac == null) {
		//
		// theClientFac = (TheClientFac) context
		// .lookup("lpserver/TheClientFacBean/remote");
		// }
		// } catch (Throwable t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		// }
		// return theClientFac;
	}

	/**
	 * SessionFacade fuer Ansprechpartner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AnsprechpartnerFac
	 */
	public AnsprechpartnerFac getAnsprechpartnerFac() throws EJBExceptionLP {
		try {
			if (ansprechpartnerFac == null) {
				ansprechpartnerFac = lookupFac(AnsprechpartnerFacBean.class, AnsprechpartnerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return ansprechpartnerFac;
	}

	/**
	 * SessionFacade fuer Auftrag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragFac
	 */
	public AuftragFac getAuftragFac() throws EJBExceptionLP {
		try {
			if (auftragFac == null) {
				auftragFac = lookupFac(AuftragFacBean.class, AuftragFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragFac;
	}

	/**
	 * SessionFacade fuer Auftragteilnehmer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragteilnehmerFac
	 */
	public AuftragteilnehmerFac getAuftragteilnehmerFac() throws EJBExceptionLP {
		try {
			if (auftragteilnehmerFac == null) {
				auftragteilnehmerFac = lookupFac(AuftragteilnehmerFacBean.class, AuftragteilnehmerFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragteilnehmerFac;
	}

	public AnfrageFac getAnfrageFac() throws EJBExceptionLP {
		try {
			if (anfrageFac == null) {
				anfrageFac = lookupFac(AnfrageFacBean.class, AnfrageFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfrageFac;
	}

	public AnfragepositionFac getAnfragepositionFac() throws EJBExceptionLP {
		try {
			if (anfragepositionFac == null) {
				anfragepositionFac = lookupFac(AnfragepositionFacBean.class, AnfragepositionFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfragepositionFac;
	}

	public AnfrageServiceFac getAnfrageServiceFac() throws EJBExceptionLP {
		try {
			if (anfrageServiceFac == null) {
				anfrageServiceFac = lookupFac(com.lp.server.anfrage.ejbfac.AnfrageServiceFacBean.class,
						AnfrageServiceFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return anfrageServiceFac;
	}

	/**
	 * SessionFacade fuer Bestellung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellungFac
	 */
	public BestellungFac getBestellungFac() throws EJBExceptionLP {
		try {
			if (bestellungFac == null) {
				bestellungFac = lookupFac(com.lp.server.bestellung.ejbfac.BestellungFacBean.class, BestellungFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellungFac;
	}

	public BestellungServiceFac getBestellungServiceFac() throws EJBExceptionLP {
		try {
			if (bestellungServiceFac == null) {
				bestellungServiceFac = lookupFac(com.lp.server.bestellung.ejbfac.BestellungServiceFacBean.class,
						BestellungServiceFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return bestellungServiceFac;
	}

	/**
	 * SessionFacade fuer ArtikelFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	public ArtikelFac getArtikelFac() throws EJBExceptionLP {
		try {
			if (artikelFac == null) {
				artikelFac = lookupFac(com.lp.server.artikel.ejbfac.ArtikelFacBean.class, ArtikelFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelFac;
	}
	
	public ArtikelServiceFac getArtikelServiceFac() throws EJBExceptionLP {
		try {
			if (artikelServiceFac == null) {
				artikelServiceFac = lookupFac(com.lp.server.artikel.ejbfac.ArtikelServiceFacBean.class, ArtikelServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelServiceFac;
	}
	
	public ArtikelimportFac getArtikelimportFac() throws EJBExceptionLP {
		try {
			if (artikelimportFac == null) {
				artikelimportFac = lookupFac(com.lp.server.artikel.ejbfac.ArtikelimportFacBean.class,
						ArtikelimportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelimportFac;
	}

	/**
	 * SessionFacade fuer ArtikelReportFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelReportFac
	 */
	public ArtikelReportFac getArtikelReportFac() throws EJBExceptionLP {
		try {
			if (artikelReportFac == null) {
				artikelReportFac = lookupFac(com.lp.server.artikel.ejbfac.ArtikelReportFacBean.class,
						ArtikelReportFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelReportFac;
	}

	/**
	 * SessionFacade fuer ArtikelkommentarFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelkommentarFac
	 */
	public ArtikelkommentarFac getArtikelkommentarFac() throws EJBExceptionLP {
		try {
			if (artikelkommentarFac == null) {
				artikelkommentarFac = lookupFac(com.lp.server.artikel.ejbfac.ArtikelkommentarFacBean.class,
						ArtikelkommentarFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelkommentarFac;
	}

	/**
	 * SessionFacade fuer InventurFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ArtikelFac
	 */
	public InventurFac getInventurFac() throws EJBExceptionLP {
		try {
			if (inventurFac == null) {
				inventurFac = lookupFac(com.lp.server.artikel.ejbfac.InventurFacBean.class, InventurFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return inventurFac;
	}

	/**
	 * SessionFacade fuer Benutzer holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BenutzerFac
	 */
	public BenutzerFac getBenutzerFac() throws EJBExceptionLP {

		if (benutzerFac == null) {
			benutzerFac = lookupFac(BenutzerFacBean.class, BenutzerFac.class);
		}

		return benutzerFac;
	}

	public NachrichtenFac getNachrichtenFac() throws EJBExceptionLP {
		try {
			if (nachrichtenFac == null) {

				nachrichtenFac = lookupFac(NachrichtenFacBean.class, NachrichtenFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return nachrichtenFac;
	}

	/**
	 * SessionFacade fuer Auftragposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragpositionFac
	 */
	public AuftragpositionFac getAuftragpositionFac() throws EJBExceptionLP {
		try {
			if (auftragpositionFac == null) {
				auftragpositionFac = lookupFac(AuftragpositionFacBean.class, AuftragpositionFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return auftragpositionFac;
	}

	public AuftragReportFac getAuftragReportFac() throws EJBExceptionLP {
		try {
			if (auftragReportFac == null) {
				auftragReportFac = lookupFac(AuftragReportFacBean.class, AuftragReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return auftragReportFac;
	}

	/**
	 * SessionFacade fuer AuftragServices holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return AuftragServiceFac
	 */
	public AuftragServiceFac getAuftragServiceFac() throws EJBExceptionLP {
		try {
			if (auftragServiceFac == null) {
				auftragServiceFac = lookupFac(AuftragServiceFacBean.class, AuftragServiceFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return auftragServiceFac;
	}

	/**
	 * SessionFacade fuer Bestellposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellpositionFac
	 */
	public BestellpositionFac getBestellpositionFac() throws EJBExceptionLP {
		try {
			if (bestellpositionFac == null) {
				bestellpositionFac = lookupFac(BestellpositionFacBean.class, BestellpositionFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellpositionFac;
	}

	/**
	 * SessionFacade fuer Kunde holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	public KundeFac getKundeFac() throws EJBExceptionLP {
		try {
			if (kundeFac == null) {
				kundeFac = lookupFac(KundeFacBean.class, KundeFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundeFac;
	}

	/**
	 * SessionFacade fuer Kundesachbearbeiter holen.
	 * 
	 * @return KundesachbearbeiterFac
	 * @throws EJBExceptionLP
	 */
	public KundesachbearbeiterFac getKundesachbearbeiterFac() throws EJBExceptionLP {
		try {
			if (kundesachbearbeiterFac == null) {
				kundesachbearbeiterFac = lookupFac(KundesachbearbeiterFacBean.class, KundesachbearbeiterFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundesachbearbeiterFac;
	}

	/**
	 * SessionFacade fuer KundeReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return KundeFac
	 */
	public KundeReportFac getKundeReportFac() throws EJBExceptionLP {
		try {
			if (kundeReportFac == null) {
				kundeReportFac = lookupFac(KundeReportFacBean.class, KundeReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundeReportFac;
	}

	/**
	 * SessionFacade fuer Lager holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LagerFac
	 */
	public LagerFac getLagerFac() throws EJBExceptionLP {
		try {
			if (lagerFac == null) {
				lagerFac = lookupFac(LagerFacBean.class, LagerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lagerFac;
	}

	public InseratFac getInseratFac() throws EJBExceptionLP {
		try {
			if (inseratFac == null) {
				inseratFac = lookupFac(InseratFacBean.class, InseratFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return inseratFac;
	}

	public LagerReportFac getLagerReportFac() throws EJBExceptionLP {
		try {
			if (lagerReportFac == null) {
				lagerReportFac = lookupFac(LagerReportFacBean.class, LagerReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lagerReportFac;
	}

	/**
	 * SessionFacade fuer Wareneingang holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return WareneingangFac
	 */
	public WareneingangFac getWareneingangFac() throws EJBExceptionLP {
		try {
			if (wareneingangFac == null) {
				wareneingangFac = lookupFac(WareneingangFacBean.class, WareneingangFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return wareneingangFac;
	}

	/**
	 * SessionFacade fuer Bestellvorschlag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return BestellvorschlagFac
	 */
	public BestellvorschlagFac getBestellvorschlagFac() throws EJBExceptionLP {
		try {
			if (bestellvorschlagFac == null) {
				bestellvorschlagFac = lookupFac(BestellvorschlagFacBean.class, BestellvorschlagFac.class);

			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellvorschlagFac;
	}

	/**
	 * SessionFacade fuer Lieferschein holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinFac
	 */
	public LieferscheinFac getLieferscheinFac() throws EJBExceptionLP {
		try {
			if (lieferscheinFac == null) {
				lieferscheinFac = lookupFac(LieferscheinFacBean.class, LieferscheinFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferscheinFac;
	}

	/**
	 * SessionFacade fuer Reklamation holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReklamationFac
	 */
	public ReklamationFac getReklamationFac() throws EJBExceptionLP {
		try {
			if (reklamationFac == null) {
				reklamationFac = lookupFac(ReklamationFacBean.class, ReklamationFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reklamationFac;
	}

	/**
	 * SessionFacade fuer Lieferscheinposition holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferscheinpositionFac
	 */
	public LieferscheinpositionFac getLieferscheinpositionFac() throws EJBExceptionLP {
		try {
			if (lieferscheinpositionFac == null) {
				lieferscheinpositionFac = lookupFac(LieferscheinpositionFacBean.class, LieferscheinpositionFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinpositionFac;
	}

	public LieferscheinReportFac getLieferscheinReportFac() throws EJBExceptionLP {
		try {
			if (lieferscheinReportFac == null) {
				lieferscheinReportFac = lookupFac(LieferscheinReportFacBean.class, LieferscheinReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinReportFac;
	}

	public LieferscheinServiceFac getLieferscheinServiceFac() throws EJBExceptionLP {
		try {
			if (lieferscheinServiceFac == null) {
				lieferscheinServiceFac = lookupFac(LieferscheinServiceFacBean.class, LieferscheinServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinServiceFac;
	}

	/**
	 * SessionFacade fuer Lieferant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LieferantFac
	 */
	public LieferantFac getLieferantFac() throws EJBExceptionLP {
		try {
			if (lieferantFac == null) {
				lieferantFac = lookupFac(LieferantFacBean.class, LieferantFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferantFac;
	}

	public LieferantServicesFac getLieferantServicesFac() throws EJBExceptionLP {
		try {
			if (lieferantServicesFac == null) {
				lieferantServicesFac = lookupFac(LieferantServicesFacBean.class, LieferantServicesFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferantServicesFac;
	}

	/**
	 * SessionFacade fuer System Locale holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return LocaleFac
	 */
	public LocaleFac getLocaleFac() throws EJBExceptionLP {

		if (localeFac == null) {
			localeFac = lookupFac(LocaleFacBean.class, LocaleFac.class);
		}

		return localeFac;
	}

	/**
	 * SessionFacade fuer System Mandant holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return MandantFac
	 */
	public MandantFac getMandantFac() throws EJBExceptionLP {

		if (mandantFac == null) {
			mandantFac = lookupFac(MandantFacBean.class, MandantFac.class);
		}

		return mandantFac;
	}

	/**
	 * SessionFacade fuer Partner holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return PartnerFac
	 */
	public PartnerFac getPartnerFac() throws EJBExceptionLP {

		if (partnerFac == null) {
			partnerFac = lookupFac(PartnerFacBean.class, PartnerFac.class);
		}
		return partnerFac;
	}

	public PanelFac getPanelFac() throws EJBExceptionLP {
		try {
			if (panelFac == null) {
				panelFac = lookupFac(PanelFacBean.class, PanelFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return panelFac;
	}

	public PflegeFac getPflegeFac() throws EJBExceptionLP {
		try {
			if (pflegeFac == null) {
				pflegeFac = lookupFac(PflegeFacBean.class, PflegeFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return pflegeFac;
	}

	public PartnerServicesFac getPartnerServicesFac() throws EJBExceptionLP {
		try {
			if (partnerServicesFac == null) {
				partnerServicesFac = lookupFac(PartnerServicesFacBean.class, PartnerServicesFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerServicesFac;
	}

	public PartnerReportFac getPartnerReportFac() throws EJBExceptionLP {
		try {
			if (partnerReportFac == null) {
				partnerReportFac = lookupFac(PartnerReportFacBean.class, PartnerReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerReportFac;
	}

	/**
	 * SessionFacade fuer Personal holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return PersonalFac
	 */
	public PersonalFac getPersonalFac() throws EJBExceptionLP {

		if (personalFac == null) {
			personalFac = lookupFac(PersonalFacBean.class, PersonalFac.class);
		}

		return personalFac;
	}

	/**
	 * SessionFacade fuer Reservierung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public ReservierungFac getReservierungFac() throws EJBExceptionLP {
		try {
			if (reservierungFac == null) {
				reservierungFac = lookupFac(ReservierungFacBean.class, ReservierungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reservierungFac;
	}

	public ArtikelbestelltFac getArtikelbestelltFac() throws EJBExceptionLP {
		try {
			if (artikelbestelltFac == null) {
				artikelbestelltFac = lookupFac(ArtikelbestelltFacBean.class, ArtikelbestelltFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return artikelbestelltFac;
	}

	public RechteFac getRechteFac() throws EJBExceptionLP {
		try {
			if (rechteFac == null) {
				rechteFac = lookupFac(RechteFacBean.class, RechteFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechteFac;
	}

	/**
	 * SessionFacade fuer Rechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungFac getRechnungFac() throws EJBExceptionLP {
		try {
			if (rechnungFac == null) {
				rechnungFac = lookupFac(RechnungFacBean.class, RechnungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungFac;
	}
	
	public AbrechnungsvorschlagFac getAbrechnungsvorschlagFac() throws EJBExceptionLP {
		try {
			if (abrechnungsvorschlagFac == null) {
				abrechnungsvorschlagFac = lookupFac(AbrechnungsvorschlagFacBean.class, AbrechnungsvorschlagFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return abrechnungsvorschlagFac;
	}

	/**
	 * SessionFacade fuer RechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungReportFac getRechnungReportFac() throws EJBExceptionLP {
		try {
			if (rechnungReportFac == null) {
				rechnungReportFac = lookupFac(RechnungReportFacBean.class, RechnungReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungReportFac;
	}

	/**
	 * SessionFacade fuer RechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return RechnungFac
	 */
	public RechnungServiceFac getRechnungServiceFac() throws EJBExceptionLP {
		try {
			if (rechnungServiceFac == null) {
				rechnungServiceFac = lookupFac(RechnungServiceFacBean.class, RechnungServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rechnungServiceFac;
	}

	/**
	 * SessionFacade fuer Buchen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public BuchenFac getBuchenFac() throws EJBExceptionLP {
		try {
			if (buchenFac == null) {
				buchenFac = lookupFac(BuchenFacBean.class, BuchenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return buchenFac;
	}

	public InstandhaltungFac getInstandhaltungFac() throws EJBExceptionLP {
		try {
			if (instandhaltungFac == null) {
				instandhaltungFac = lookupFac(InstandhaltungFacBean.class, InstandhaltungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return instandhaltungFac;
	}

	/**
	 * SessionFacade fuer Finanz holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public FinanzFac getFinanzFac() throws EJBExceptionLP {
		try {
			if (finanzFac == null) {
				finanzFac = lookupFac(FinanzFacBean.class, FinanzFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzFac;
	}

	/**
	 * SessionFacade fuer Stueckliste holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public StuecklisteFac getStuecklisteFac() throws EJBExceptionLP {
		try {
			if (stuecklisteFac == null) {
				stuecklisteFac = lookupFac(StuecklisteFacBean.class, StuecklisteFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return stuecklisteFac;
	}

	public StuecklisteimportFac getStuecklisteimportFac() throws EJBExceptionLP {
		try {
			if (stuecklisteimportFac == null) {
				stuecklisteimportFac = lookupFac(StuecklisteimportFacBean.class, StuecklisteimportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return stuecklisteimportFac;
	}

	public IntelligenterStklImportFac getIntelligenterStklImportFac() throws EJBExceptionLP {
		try {
			if (intellStklimportFac == null) {
				intellStklimportFac = lookupFac(IntelligenterStklImportFacBean.class, IntelligenterStklImportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return intellStklimportFac;
	}

	/**
	 * SessionFacade fuer FinanzReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzReportFac
	 */
	public FinanzReportFac getFinanzReportFac() throws EJBExceptionLP {
		try {
			if (finanzReportFac == null) {
				finanzReportFac = lookupFac(FinanzReportFacBean.class, FinanzReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzReportFac;
	}

	/**
	 * SessionFacade fuer FinanzService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return FinanzServiceFac
	 */
	public FinanzServiceFac getFinanzServiceFac() throws EJBExceptionLP {
		try {
			if (finanzServiceFac == null) {
				finanzServiceFac = lookupFac(FinanzServiceFacBean.class, FinanzServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return finanzServiceFac;
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public MahnwesenFac getMahnwesenFac() throws EJBExceptionLP {
		try {
			if (mahnwesenFac == null) {
				mahnwesenFac = lookupFac(MahnwesenFacBean.class, MahnwesenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return mahnwesenFac;
	}

	/**
	 * SessionFacade fuer Mahnwesen holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public BSMahnwesenFac getBSMahnwesenFac() throws EJBExceptionLP {
		try {
			if (bsmahnwesenFac == null) {
				bsmahnwesenFac = lookupFac(BSMahnwesenFacBean.class, BSMahnwesenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bsmahnwesenFac;
	}

	public StuecklisteReportFac getStuecklisteReportFac() throws EJBExceptionLP {
		try {
			if (stuecklisteReportFac == null) {
				stuecklisteReportFac = lookupFac(StuecklisteReportFacBean.class, StuecklisteReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return stuecklisteReportFac;
	}

	/**
	 * SessionFacade fuer SystemMultilanguage holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public SystemMultilanguageFac getSystemMultilanguageFac() throws EJBExceptionLP {
		try {
			if (systemMultilanguageFac == null) {
				systemMultilanguageFac = lookupFac(SystemMultilanguageFacBean.class, SystemMultilanguageFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return systemMultilanguageFac;
	}

	/**
	 * SessionFacade fuer System holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public SystemFac getSystemFac() throws EJBExceptionLP {

		if (systemFac == null) {
			systemFac = lookupFac(SystemFacBean.class, SystemFac.class);
		}

		return systemFac;
	}

	public SystemReportFacLocal getSystemReportFacLocal() throws EJBExceptionLP {

		if (systemReportFacLocal == null) {
			try {
				systemReportFacLocal = lookupLocalFac(SystemReportFacBean.class, SystemReportFacLocal.class);
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
		}
		return systemReportFacLocal;
	}

	
	public SystemReportFac getSystemReportFac() throws EJBExceptionLP {

		if (systemReportFac == null) {
			try {
				systemReportFac = lookupFac(SystemReportFacBean.class, SystemReportFac.class);
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
		}
		return systemReportFac;
	}

	public SystemServicesFac getSystemServicesFac() throws EJBExceptionLP {
		if (systemServicesFac == null) {
			try {
				systemServicesFac = lookupFac(SystemServicesFacBean.class, SystemServicesFac.class);
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}

		}
		return systemServicesFac;
	}

	/**
	 * SessionFacade fuer VkPreisfindungFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public VkPreisfindungFac getVkPreisfindungFac() throws EJBExceptionLP {
		try {
			if (vkPreisfindungFac == null) {
				vkPreisfindungFac = lookupFac(VkPreisfindungFacBean.class, VkPreisfindungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return vkPreisfindungFac;
	}

	/**
	 * SessionFacade fuer VkPreisfindungFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public ZeiterfassungFac getZeiterfassungFac() throws EJBExceptionLP {
		try {
			if (zeiterfassungFac == null) {
				zeiterfassungFac = lookupFac(ZeiterfassungFacBean.class, ZeiterfassungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zeiterfassungFac;
	}
	
	public ReisekostenFac getReisekostenFac() throws EJBExceptionLP {
		try {
			if (reisekostenFac == null) {
				reisekostenFac = lookupFac(ReisekostenFacBean.class, ReisekostenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reisekostenFac;
	}

	public MaschineFac getMaschineFac() throws EJBExceptionLP {
		try {
			if (maschineFac == null) {
				maschineFac = lookupFac(MaschineFacBean.class, MaschineFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return maschineFac;
	}

	public ZeiterfassungReportFac getZeiterfassungReportFac() throws EJBExceptionLP {
		try {
			if (zeiterfassungReportFac == null) {
				zeiterfassungReportFac = lookupFac(ZeiterfassungReportFacBean.class, ZeiterfassungReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zeiterfassungReportFac;
	}

	public ZutrittscontrollerFac getZutrittscontrollerFac() throws EJBExceptionLP {
		try {
			if (zutrittscontrollerFac == null) {
				zutrittscontrollerFac = lookupFac(ZutrittscontrollerFacBean.class, ZutrittscontrollerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zutrittscontrollerFac;
	}

	/**
	 * SessionFacade fuer ParameterFac holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungFac
	 */
	public ParameterFac getParameterFac() throws EJBExceptionLP {
		if (parameterFac == null) {
			parameterFac = lookupFac(ParameterFacBean.class, ParameterFac.class);
		}
		return parameterFac;

	}

	/**
	 * SessionFacade fuer Eingangsrechnung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungFac
	 */
	public EingangsrechnungFac getEingangsrechnungFac() throws EJBExceptionLP {
		try {
			if (eingangsrechnungFac == null) {
				eingangsrechnungFac = lookupFac(EingangsrechnungFacBean.class, EingangsrechnungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungFac;
	}

	/**
	 * SessionFacade fuer EingangsrechnungReport holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungReportFac
	 */
	public EingangsrechnungReportFac getEingangsrechnungReportFac() throws EJBExceptionLP {
		try {
			if (eingangsrechnungReportFac == null) {
				eingangsrechnungReportFac = lookupFac(EingangsrechnungReportFacBean.class,
						EingangsrechnungReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungReportFac;
	}

	/**
	 * SessionFacade fuer EingangsrechnungService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return EingangsrechnungServiceFac
	 */
	public EingangsrechnungServiceFac getEingangsrechnungServiceFac() throws EJBExceptionLP {
		try {
			if (eingangsrechnungServiceFac == null) {
				eingangsrechnungServiceFac = lookupFac(EingangsrechnungServiceFacBean.class,
						EingangsrechnungServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eingangsrechnungServiceFac;
	}

	/**
	 * SessionFacade fuer Versand holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public VersandFac getVersandFac() throws EJBExceptionLP {
		try {
			if (versandFac == null) {
				versandFac = lookupFac(VersandFacBean.class, VersandFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return versandFac;
	}

	/**
	 * SessionFacade fuer Bank holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public BankFac getBankFac() throws EJBExceptionLP {
		try {
			if (bankFac == null) {
				bankFac = lookupFac(BankFacBean.class, BankFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bankFac;
	}

	/**
	 * SessionFacade fuer Material holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return VersandFac
	 */
	public MaterialFac getMaterialFac() throws EJBExceptionLP {
		try {
			if (materialFac == null) {
				materialFac = lookupFac(MaterialFacBean.class, MaterialFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return materialFac;
	}

	public MediaFac getMediaFac() throws EJBExceptionLP {
		try {
			if (mediaFac == null) {
				mediaFac = lookupFac(MediaFacBean.class, MediaFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return mediaFac;
	}

	public AngebotFac getAngebotFac() throws EJBExceptionLP {
		try {
			if (angebotFac == null) {
				angebotFac = lookupFac(AngebotFacBean.class, AngebotFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotFac;
	}

	public AusliefervorschlagFac getAusliefervorschlagFac() throws EJBExceptionLP {
		try {
			if (ausliefervorschlagFac == null) {
				ausliefervorschlagFac = lookupFac(AusliefervorschlagFacBean.class, AusliefervorschlagFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return ausliefervorschlagFac;
	}

	public SchichtFac getSchichtFac() throws EJBExceptionLP {
		try {
			if (schichtFac == null) {
				schichtFac = lookupFac(SchichtFacBean.class, SchichtFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return schichtFac;
	}

	public AngebotpositionFac getAngebotpositionFac() throws EJBExceptionLP {
		try {
			if (angebotpositionFac == null) {
				angebotpositionFac = lookupFac(AngebotpositionFacBean.class, AngebotpositionFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotpositionFac;
	}

	public AngebotReportFac getAngebotReportFac() throws EJBExceptionLP {
		try {
			if (angebotReportFac == null) {
				angebotReportFac = lookupFac(AngebotReportFacBean.class, AngebotReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotReportFac;
	}

	public AngebotServiceFac getAngebotServiceFac() throws EJBExceptionLP {
		try {
			if (angebotServiceFac == null) {
				angebotServiceFac = lookupFac(AngebotServiceFacBean.class, AngebotServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotServiceFac;
	}

	public FertigungFac getFertigungFac() throws EJBExceptionLP {
		try {
			if (fertigungFac == null) {
				fertigungFac = lookupFac(FertigungFacBean.class, FertigungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungFac;
	}

	public FertigungReportFac getFertigungReportFac() throws EJBExceptionLP {
		try {
			if (fertigungReportFac == null) {
				fertigungReportFac = lookupFac(FertigungReportFacBean.class, FertigungReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungReportFac;
	}

	public FertigungServiceFac getFertigungServiceFac() throws EJBExceptionLP {
		try {
			if (fertigungServiceFac == null) {
				fertigungServiceFac = lookupFac(FertigungServiceFacBean.class, FertigungServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungServiceFac;
	}

	public FehlmengeFac getFehlmengeFac() throws EJBExceptionLP {
		try {
			if (fehlmengeFac == null) {
				fehlmengeFac = lookupFac(FehlmengeFacBean.class, FehlmengeFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fehlmengeFac;
	}

	public InternebestellungFac getInternebestellungFac() throws EJBExceptionLP {
		try {
			if (internebestellungFac == null) {
				internebestellungFac = lookupFac(InternebestellungFacBean.class, InternebestellungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return internebestellungFac;
	}

	public BenutzerServicesFacLocal getBenutzerServicesFac() throws EJBExceptionLP {
		if (benutzerServicesFac == null) {
			benutzerServicesFac = lookupLocalFac(BenutzerServicesFacBean.class, BenutzerServicesFacLocal.class);
		}
		return benutzerServicesFac;

	}

	public FastLaneReader getFastLaneReader() throws EJBExceptionLP {
		try {
			if (fastLaneReader == null) {
				fastLaneReader=lookupFac(FastLaneReaderBean.class, FastLaneReader.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fastLaneReader;
	}

	public AngebotstklFac getAngebotstklFac() throws EJBExceptionLP {
		try {
			if (angebotstklFac == null) {
				angebotstklFac= lookupFac(AngebotstklFacBean.class, AngebotstklFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotstklFac;
	}

	public ForecastFac getForecastFac() throws EJBExceptionLP {
		try {
			if (forecastFac == null) {
				forecastFac = lookupFac(ForecastFacBean.class, ForecastFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return forecastFac;
	}

	public AngebotstklpositionFac getAngebotstklpositionFac() throws EJBExceptionLP {
		try {
			if (angebotstklpositionFac == null) {
				angebotstklpositionFac =lookupFac(AngebotstklpositionFacBean.class, AngebotstklpositionFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return angebotstklpositionFac;
	}

	public FibuExportFac getFibuExportFac() throws EJBExceptionLP {
		try {
			if (fibuExportFac == null) {
				fibuExportFac =lookupFac(FibuExportFacBean.class, FibuExportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fibuExportFac;
	}

	public DruckerFac getDruckerFac() throws EJBExceptionLP {
		try {
			if (druckerFac == null) {
				druckerFac =lookupFac(DruckerFacBean.class, DruckerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return druckerFac;
	}
	
	public BelegartmediaFac getBelegartmediaFac() throws EJBExceptionLP {
		try {
			if (belegartmediaFac == null) {
				belegartmediaFac =lookupFac(BelegartmediaFacBean.class, BelegartmediaFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return belegartmediaFac;
	}

	public DokumenteFac getDokumenteFac() throws EJBExceptionLP {
		try {
			if (dokumenteFac == null) {
				dokumenteFac =lookupFac(DokumenteFacBean.class, DokumenteFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return dokumenteFac;
	}

	public KundesokoFac getKundesokoFac() throws EJBExceptionLP {
		try {
			if (kundesokoFac == null) {
				kundesokoFac = lookupFac(KundesokoFacBean.class, KundesokoFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kundesokoFac;
	}

	public BelegbuchungFac getBelegbuchungFac(String mandantCNr) throws EJBExceptionLP {
		boolean istVersteurer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ISTVERSTEURER, mandantCNr);
		boolean mischVersteurer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MISCHVERSTEURER, mandantCNr);
		if (!istVersteurer && !mischVersteurer) {
			try {
				if (belegbuchungFac == null) {
					belegbuchungFac =  lookupFac(BelegbuchungFacBean.class, BelegbuchungFac.class);
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungFac;
		} else if (mischVersteurer) {
			try {
				if (belegbuchungMischversteurerFac == null) {
					belegbuchungMischversteurerFac = (BelegbuchungFac)  lookupFac(BelegbuchungMischversteurerFacBean.class, BelegbuchungMischversteuererFac.class);
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungMischversteurerFac;
		} else {
			try {
				if (belegbuchungIstversteurerFac == null) {
					belegbuchungIstversteurerFac = (BelegbuchungFac) lookupFac(BelegbuchungIstversteurerFacBean.class, BelegbuchungIstversteurerFac.class);
				}
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
			return belegbuchungIstversteurerFac;
		}
	}

	/**
	 * SessionFacade fuer TheJudge holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	protected final TheJudgeFac getTheJudgeFac() throws EJBExceptionLP {

		if (theJudgeFac == null) {
			theJudgeFac = lookupFac(TheJudgeFacBean.class, TheJudgeFac.class);
		}

		return theJudgeFac;
	}

	/**
	 * SessionFacade fuer Projekt holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ProjektFac
	 */

	public ProjektFac getProjektFac() throws EJBExceptionLP {
		try {
			if (projektFac == null) {
				projektFac = lookupFac(ProjektFacBean.class, ProjektFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return projektFac;
	}

	/**
	 * SessionFacade fuer ProjektService holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ProjektFac
	 */

	public ProjektServiceFac getProjektServiceFac() throws EJBExceptionLP {
		try {
			if (projektServiceFac == null) {
				projektServiceFac = lookupFac(ProjektServiceFacBean.class, ProjektServiceFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return projektServiceFac;
	}

	/**
	 * SessionFacade fuer Zahlungsvorschlag holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ZahlungsvorschlagFac
	 */

	public ZahlungsvorschlagFac getZahlungsvorschlagFac() throws EJBExceptionLP {
		try {
			if (zahlungsvorschlagFac == null) {
				zahlungsvorschlagFac = lookupFac(ZahlungsvorschlagFacBean.class, ZahlungsvorschlagFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return zahlungsvorschlagFac;
	}

	public AutoFehlmengendruckFac getAutoFehlmengendruckFac() throws EJBExceptionLP {
		try {
			if (autoFehlmengendruckFac == null) {
				autoFehlmengendruckFac = lookupFac(AutoFehlmengendruckFacBean.class, AutoFehlmengendruckFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoFehlmengendruckFac;
	}

	public AutoMahnungsversandFac getAutoMahnungsversandFac() throws EJBExceptionLP {
		try {
			if (autoMahnungsversandFac == null) {
				autoMahnungsversandFac = lookupFac(AutoMahnungsversandFacBean.class, AutoMahnungsversandFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoMahnungsversandFac;
	}
	public AutoMonatsabrechnungversandFac getAutoMonatsabrechnungversandFac() throws EJBExceptionLP {
		try {
			if (autoMonatsabrechnungversandFac == null) {
				autoMonatsabrechnungversandFac = lookupFac(AutoMonatsabrechnungversandFacBean.class, AutoMonatsabrechnungversandFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoMonatsabrechnungversandFac;
	}
	public AutoLoseerledigenFac getAutoLoseerledigenFac() throws EJBExceptionLP {
		try {
			if (autoLoseerledigenFac == null) {
				autoLoseerledigenFac = lookupFac(AutoLoseerledigenFacBean.class, AutoLoseerledigenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoLoseerledigenFac;
	}
	public AutoMahnenFac getAutoMahnenFac() {
		try {
			if (autoMahnenFac == null) {
				autoMahnenFac = lookupFac(AutoMahnenFacBean.class, AutoMahnenFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoMahnenFac;
	}

	public KuecheFac getKuecheFac() {
		try {
			if (kuecheFac == null) {
				kuecheFac = lookupFac(KuecheFacBean.class, KuecheFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kuecheFac;
	}

	public KuecheReportFac getKuecheReportFac() {
		try {
			if (kuecheReportFac == null) {
				kuecheReportFac = lookupFac(KuecheReportFacBean.class, KuecheReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kuecheReportFac;
	}

	public BestellungReportFac getBestellungReportFac() throws EJBExceptionLP {
		try {
			if (bestellungReportFac == null) {
				bestellungReportFac = lookupFac(BestellungReportFacBean.class, BestellungReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return bestellungReportFac;
	}

	public ReportConnectionFacLocal getReportConnectionFacLocal() throws EJBExceptionLP {
		try {
			if (reportConnectionFacLocal == null) {
				reportConnectionFacLocal = lookupLocalFac(ReportConnectionFacBean.class, ReportConnectionFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return reportConnectionFacLocal;
	}

	public AutoBestellvorschlagFac getAutoBestellvorschlagFac() throws EJBExceptionLP {
		try {
			if (autoBestellvorschlagFac == null) {
				autoBestellvorschlagFac = lookupFac(AutoBestellvorschlagFacBean.class, AutoBestellvorschlagFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoBestellvorschlagFac;
	}

	public AutoPaternosterFac getAutoPaternosterFac() throws EJBExceptionLP {
		try {
			if (autoPaternosterFac == null) {
				autoPaternosterFac = lookupFac(AutoPaternosterFacBean.class, AutoPaternosterFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));

		}
		return autoPaternosterFac;
	}

	public BelegpositionkonvertierungFac getBelegpositionkonvertierungFac() throws EJBExceptionLP {
		try {
			if (belegpositionkonvertierungFac == null) {
				belegpositionkonvertierungFac = lookupFac(BelegpositionkonvertierungFacBean.class, BelegpositionkonvertierungFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return belegpositionkonvertierungFac;
	}

	public RahmenbedarfeFac getRahmenbedarfeFac() throws EJBExceptionLP {
		try {
			if (rahmenbedarfeFac == null) {
				rahmenbedarfeFac = lookupFac(RahmenbedarfeFacBean.class, RahmenbedarfeFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return rahmenbedarfeFac;
	}

	public AutomatikjobFac getAutomatikjobFac() throws EJBExceptionLP {
		try {
			if (automatikjobFac == null) {
				automatikjobFac = lookupFac(AutomatikjobFacBean.class, AutomatikjobFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatikjobFac;
	}

	public AutomatikjobtypeFac getAutomatikjobtypeFac() throws EJBExceptionLP {
		try {
			if (automatikjobtypeFac == null) {
				automatikjobtypeFac = lookupFac(AutomatikjobtypeFacBean.class, AutomatikjobtypeFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatikjobtypeFac;
	}

	public AutomatiktimerFac getAutomatiktimerFac() throws EJBExceptionLP {
		try {
			if (automatiktimerFac == null) {
				automatiktimerFac = lookupFac(AutomatiktimerFacBean.class, AutomatiktimerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return automatiktimerFac;
	}

	public AutoRahmendetailbedarfdruckFac getAutoRahmendetailbedarfdruckFac() {
		try {
			if (autoRahmendetailbedarfdruckFac == null) {
				autoRahmendetailbedarfdruckFac = lookupFac(AutoRahmendetailbedarfdruckFacBean.class, AutoRahmendetailbedarfdruckFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoRahmendetailbedarfdruckFac;
	}

	public LogonFac getLogonFac() throws EJBExceptionLP {
		try {
			if (logonFac == null) {
				logonFac = lookupFac(LogonFacBean.class, LogonFac.class);
			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return logonFac;
	}

	public JCRDocFac getJCRDocFac() {
		try {
			if (jcrDocFac == null) {
				jcrDocFac = lookupFac(JCRDocFacBean.class, JCRDocFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jcrDocFac;
	}

	public AuftragRahmenAbrufFac getAuftragRahmenAbrufFac() {
		try {
			if (auftragRahmenAbrufFac == null) {
				auftragRahmenAbrufFac = lookupFac(AuftragRahmenAbrufFacBean.class, AuftragRahmenAbrufFac.class);
			}
		} catch (Throwable t) {

		}
		return auftragRahmenAbrufFac;
	}

	/**
	 * Primary Key Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public final PKGeneratorObj getPKGeneratorObj() {
		if (pkGeneratorObj == null) {
			pkGeneratorObj = new PKGeneratorObj();
		}
		return pkGeneratorObj;
	}

	/**
	 * Belegnummern Generator holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return ReservierungFac
	 */
	public final BelegnummerGeneratorObj getBelegnummerGeneratorObj() {
		if (bnGeneratorObj == null) {
			bnGeneratorObj = new BelegnummerGeneratorObj();
		}
		return bnGeneratorObj;
	}

	public BelegVerkaufFac getBelegVerkaufFac() throws EJBExceptionLP {
		try {
			if (belegVerkaufFac == null) {
				belegVerkaufFac = lookupLocalFac(BelegVerkaufFacBean.class, BelegVerkaufFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return belegVerkaufFac;
	}

	public WebshopItemServiceFacLocal getWebshopItemServiceFac() throws EJBExceptionLP {
		try {
			if (null == webshopItemServicesFac) {
				webshopItemServicesFac = (WebshopItemServiceFacLocal) context
						.lookup("lpserver/WebshopItemServiceEjb/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return webshopItemServicesFac;
	}

	public WebshopOrderServiceFacLocal getWebshopOrderServiceFac() throws EJBExceptionLP {
		try {
			if (null == webshopOrderServicesFac) {
				webshopOrderServicesFac = lookupLocalFac(
						WebshopOrderServiceEjb.class, WebshopOrderServiceFacLocal.class);
/*				
				webshopOrderServicesFac = (WebshopOrderServiceFacLocal) context
						.lookup("lpserver/WebshopOrderServiceEjb/local");
*/						
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return webshopOrderServicesFac;
	}

	public ShopTimerCronJobLocal getShopTimerCronJob() throws EJBExceptionLP {
		try {
			if (null == shopTimerCronJob) {
				shopTimerCronJob = lookupLocalFac(ShopTimerCronJobs.class, ShopTimerCronJobLocal.class);
//				shopTimerCronJob = (ShopTimerCronJobLocal) context.lookup("lpserver/ShopTimerCronJobs/local");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return shopTimerCronJob;
	}

	public ShopTimerFac getShopTimerFac() throws EJBExceptionLP {
		try {
			if (null == shopTimerFac) {
				shopTimerFac = lookupFac(ShopTimerFacBean.class, ShopTimerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return shopTimerFac;
	}

	public WebshopOrderServiceFacLocal getWebshopCustomerOrderServiceFac() throws EJBExceptionLP {
		try {
			if (null == webshopCustomerOrderServicesFac) {
				webshopCustomerOrderServicesFac = (WebshopOrderServiceFacLocal) context
						.lookup("lpserver/WebshopCustomerOrderServiceEjb/remote");
			}
		} catch (Throwable t) {
			try {
				if (null == webshopCustomerOrderServicesFac) {
					webshopCustomerOrderServicesFac = (WebshopOrderServiceFacLocal) context.lookup(
							"java:global/lpserver/ejb/WebshopCustomerOrderServiceEjb!com.lp.server.auftrag.service.WebshopOrderServiceFacLocal");
				}
			} catch (Throwable t2) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t2));
			}
		}

		return webshopCustomerOrderServicesFac;
	}

	public BatcherFac getBatcherFac() throws EJBExceptionLP {
		try {
			if (null == batcherFac) {
				batcherFac = lookupLocalFac(BatcherFacBean.class, BatcherFac.class);
				
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return batcherFac;
	}

	public WebshopCustomerServiceFacLocal getWebshopCustomerServiceFac() throws EJBExceptionLP {
		try {
			if (null == webshopCustomerServicesFac) {
				webshopCustomerServicesFac = (WebshopCustomerServiceFacLocal) context
						.lookup("lpserver/WebshopCustomerServiceEjb/remote");
			}
		} catch (Throwable t) {
			try {
				if (null == webshopCustomerServicesFac) {
					webshopCustomerServicesFac = (WebshopCustomerServiceFacLocal) context.lookup(
							"java:global/lpserver/ejbWebshopCustomerServiceEjb!com.lp.server.auftrag.service.WebshopCustomerServiceFacLocal");
				}
			} catch (Throwable t2) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t2));
			}
		}

		return webshopCustomerServicesFac;
	}

	public JCRMediaFac getJCRMediaFac() throws EJBExceptionLP {
		try {
			if (jcrMediaFac == null) {
				jcrMediaFac = lookupFac(JCRMediaFacBean.class, JCRMediaFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jcrMediaFac;
	}

	public EmailMediaFac getEmailMediaFac() throws EJBExceptionLP {
		try {
			if (emailMediaFac == null) {
				emailMediaFac = lookupFac(EmailMediaFacBean.class, EmailMediaFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return emailMediaFac;
	}

	public EmailMediaLocalFac getEmailMediaLocalFac() throws EJBExceptionLP {
		try {
			if (emailMediaLocalFac == null) {
				emailMediaLocalFac = lookupLocalFac(EmailMediaLocalFacBean.class, EmailMediaLocalFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return emailMediaLocalFac;
	}

	public BatcherSingleTransactionFac getBatcherSingleTransactionFac() throws EJBExceptionLP {
		try {
			if (null == batcherSingleTransactionFac) {
				batcherSingleTransactionFac = lookupLocalFac(BatcherSingleTransactionFacBean.class, BatcherSingleTransactionFac.class);
				
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return batcherSingleTransactionFac;
	}

	public PersonalApiFac getPersonalApiFac() throws EJBExceptionLP {
		try {
			if (personalApiFac == null) {
				personalApiFac = lookupFac(PersonalApiFacBean.class, PersonalApiFac.class);
			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return personalApiFac;
	}

	public JcrDumpFac getJcrDumpFac() throws EJBExceptionLP {
		try {
			if (jcrDumpFac == null) {
				jcrDumpFac = lookupFac(JcrDumpFacBean.class, JcrDumpFac.class);
			}

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return jcrDumpFac;
	}

	public SepaImportFac getSepaImportFac() throws EJBExceptionLP {
		try {
			if (sepaImportFac == null) {
				sepaImportFac = lookupFac(SepaImportFacBean.class, SepaImportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return sepaImportFac;
	}

	public JobAuslieferlisteFac getJobAuslieferlisteFac() throws EJBExceptionLP {
		try {
			if (jobAuslieferlisteFac == null) {
				jobAuslieferlisteFac = lookupFac(JobAuslieferlisteFacBean.class, JobAuslieferlisteFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobAuslieferlisteFac;
	}
	
	public AutoLumiquoteFac getAutoLumiqoteFac() throws EJBExceptionLP {
		try {
			if (autoLumiquoteFac == null) {
				autoLumiquoteFac = lookupFac(AutoLumiquoteFacBean.class, AutoLumiquoteFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return autoLumiquoteFac;
	}

	public EventLoggerCentralFacLocal getEventLoggerFac() {
		try {
			if (eventLoggerCentralFac == null) {
				eventLoggerCentralFac = lookupLocalFac(EventLoggerCentralFacBean.class, EventLoggerCentralFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return eventLoggerCentralFac;
	}

	public FertigungImportFac getFertigungImportFac() throws EJBExceptionLP {
		try {
			if (fertigungImportFac == null) {
				fertigungImportFac = lookupFac(FertigungImportFacBean.class, FertigungImportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungImportFac;
	}

	public TheClientFacLocal getTheClientFacLocal() throws EJBExceptionLP {
		try {
			if (theClientFacLocal == null) {
				theClientFacLocal = lookupLocalFac(TheClientFacBean.class, TheClientFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return theClientFacLocal;
	}

	public Job4VendingExportFac getJob4VendingExportFac() throws EJBExceptionLP {
		try {
			if (job4VendingExportFac == null) {
				job4VendingExportFac = lookupFac(Job4VendingExportFacBean.class, Job4VendingExportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return job4VendingExportFac;
	}

	public GTINGeneratorFac getGTINGeneratorFac() throws EJBExceptionLP {
		try {
			if (gtinGeneratorFac == null) {
				gtinGeneratorFac = lookupLocalFac(GTINGeneratorFacBean.class, GTINGeneratorFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return gtinGeneratorFac;
	}

	public LieferscheinFacLocal getLieferscheinFacLocal() {
		try {
			if (lieferscheinFacLocal == null) {
				lieferscheinFacLocal = lookupLocalFac(LieferscheinFacBean.class, LieferscheinFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return lieferscheinFacLocal;
	}
	public PartnerServicesFacLocal getPartnerServicesFacLocal() {
		try {
			if (partnerServicesFacLocal == null) {
				partnerServicesFacLocal = lookupLocalFac(PartnerServicesFacBean.class, PartnerServicesFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return partnerServicesFacLocal;
	}
	public ServerDruckerFac getServerDruckerFac() {
		try {
			if (serverDruckerFac == null) {
				serverDruckerFac = lookupFac(ServerDruckerFacBean.class, ServerDruckerFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return serverDruckerFac;
	}

	public ServerDruckerFacLocal getServerDruckerFacLocal() {
		try {
			if (serverDruckerFacLocal == null) {
				serverDruckerFacLocal = lookupLocalFac(ServerDruckerFacBean.class, ServerDruckerFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return serverDruckerFacLocal;
	}

	public Magento2CategoryFacLocal getMagento2CategoryFacLocal() {
		try {
			if (magento2CategoryFacBean == null) {
				magento2CategoryFacBean = lookupLocalFac(Magento2CategoryFacBean.class, Magento2CategoryFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return magento2CategoryFacBean;
	}

	public Magento2ProductFacLocal getMagento2ProductFacLocal() {
		try {
			if (magento2ProductFacBean == null) {
				magento2ProductFacBean = lookupLocalFac(Magento2ProductFacBean.class, Magento2ProductFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return magento2ProductFacBean;
	}

	public Magento2OrderFacLocal getMagento2OrderFacLocal() {
		try {
			if (magento2OrderFacBean == null) {
				magento2OrderFacBean = lookupLocalFac(Magento2OrderFacBean.class, Magento2OrderFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return magento2OrderFacBean;
	}

	public Magento2CustomerFacLocal getMagento2CustomerFacLocal() {
		try {
			if (magento2CustomerFacBean == null) {
				magento2CustomerFacBean = lookupLocalFac(Magento2CustomerFacBean.class, Magento2CustomerFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return magento2CustomerFacBean;
	}

	public JobDetailsSyncOrderFac getJobDetailsSyncOrderFac() throws EJBExceptionLP {
		try {
			if (jobDetailsSyncOrderFacBean == null) {
				jobDetailsSyncOrderFacBean = lookupFac(JobDetailsSyncOrderFacBean.class, JobDetailsSyncOrderFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return jobDetailsSyncOrderFacBean;
	}

	public JobDetailsSyncItemFac getJobDetailsSyncItemFac() throws EJBExceptionLP {
		try {
			if (jobDetailsSyncItemFacBean == null) {
				jobDetailsSyncItemFacBean = lookupFac(JobDetailsSyncItemFacBean.class, JobDetailsSyncItemFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return jobDetailsSyncItemFacBean;
	}

	public SyncShopFac getSyncShopFac(WebshopId shopId) {
		SyncShopFac bean = syncShopFacBeans.get(shopId);
		if (bean == null) {
			bean = createBean(shopId);
			syncShopFacBeans.put(shopId, bean);
		}

		return bean;
	}

	private SyncShopFac createBean(WebshopId shopId) {
		WebshopDto shopDto = getArtikelFac().webshopFindByPrimaryKey(shopId.id());
		String artCnr = shopDto.getWebshopartCnr().trim();
		try {
			if (ArtikelFac.WEBSHOPART_MAGENTO2REST.equals(artCnr)) {
				return (SyncShopFac) lookupLocalFac(SyncMagento2FacBean.class, SyncShopFac.class);
			}
			if (ArtikelFac.WEBSHOPART_NICHT_ZUTREFFEND.equals(artCnr)) {
				return   (SyncShopFac) lookupLocalFac(SyncNichtZutreffendFacBean.class, SyncShopFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return null;
	}

	public JobDetailsErImportFac getJobDetailsErImportFac() {
		try {
			if (jobDetailsErImportFac == null) {
				jobDetailsErImportFac = lookupFac(JobDetailsErImportFacBean.class, JobDetailsErImportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return jobDetailsErImportFac;
	}

	public JobArbeitszeitstatusFac getJobArbeitszeitstatusFac() {
		try {
			if (jobArbeitszeitstatusFac == null) {
				jobArbeitszeitstatusFac = lookupFac(JobArbeitszeitstatusFacBean.class, JobArbeitszeitstatusFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobArbeitszeitstatusFac;
	}

	public JobWEJournalFac getJobWEJournalFac() {
		try {
			if (jobWEJournalFac == null) {
				jobWEJournalFac = lookupFac(JobWEJournalFacBean.class, JobWEJournalFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobWEJournalFac;
	}
	
	public JobDetailsWebabfrageArtikellieferantFac getJobWebabfrageArtikellieferantFac() {
		try {
			if (jobDetailsWebabfrageArtliefFac == null) {
				jobDetailsWebabfrageArtliefFac = lookupFac(
						JobDetailsWebabfrageArtikellieferantFacBean.class, 
						JobDetailsWebabfrageArtikellieferantFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobDetailsWebabfrageArtliefFac;
	}
	
	public HvmaFac getHvmaFac() {
		try {
			if(hvmaFac == null) {
				hvmaFac = lookupFac(HvmaFacBean.class, HvmaFac.class);
			}
		} catch(Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return hvmaFac;
	}

	public FertigungFacLocal getFertigungFacLocal() {
		try {
			if (fertigungFacLocal == null) {
				fertigungFacLocal = lookupLocalFac(FertigungFacBean.class, FertigungFacLocal.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return fertigungFacLocal;
	}
	
	public KpiReportFac getKpiReportFac() throws EJBExceptionLP {
		try {
			if (kpiReportFac == null) {
				kpiReportFac = lookupFac(KpiReportFacBean.class, KpiReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return kpiReportFac;
	}
	
	public JobKpiReportFac getJobKpiReportFac() throws EJBExceptionLP {
		try {
			if (jobKpiReportFac == null) {
				jobKpiReportFac = lookupFac(JobKpiReportFacBean.class, JobKpiReportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobKpiReportFac;
	}
	
	public JobBedarfsuebernahmeOffeneFac getJobBedarfsuebernahmeOffeneFac() throws EJBExceptionLP {
		try {
			if (jobBedarfsuebernahmeOffenFac == null) {
				jobBedarfsuebernahmeOffenFac = lookupFac(JobBedarfsuebernahmeOffeneFacBean.class, JobBedarfsuebernahmeOffeneFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		return jobBedarfsuebernahmeOffenFac;
	}

	public EdifactOrdersImportFac getEdifactOrdersImportFac() {
		try {
			if (edifactOrdersImportFac == null) {
				edifactOrdersImportFac = lookupFac(EdifactOrdersImportFacBean.class, EdifactOrdersImportFac.class);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));			
		}
		return edifactOrdersImportFac;
	}
}
