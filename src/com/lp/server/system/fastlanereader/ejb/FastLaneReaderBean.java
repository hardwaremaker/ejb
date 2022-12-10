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
package com.lp.server.system.fastlanereader.ejb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.anfrage.fastlanereader.AnfrageHandler;
import com.lp.server.anfrage.fastlanereader.AnfrageartHandler;
import com.lp.server.anfrage.fastlanereader.AnfrageerledigungsgrundHandler;
import com.lp.server.anfrage.fastlanereader.AnfragepositionHandler;
import com.lp.server.anfrage.fastlanereader.AnfragepositionartHandler;
import com.lp.server.anfrage.fastlanereader.AnfragepositionlieferdatenHandler;
import com.lp.server.anfrage.fastlanereader.AnfragetextHandler;
import com.lp.server.anfrage.fastlanereader.ZertifikatartHandler;
import com.lp.server.angebot.bl.AngebotUebersichtHandler;
import com.lp.server.angebot.bl.AngebotzeitenHandler;
import com.lp.server.angebot.fastlanereader.AkquisestatusHandler;
import com.lp.server.angebot.fastlanereader.AngebotAuftragHandler;
import com.lp.server.angebot.fastlanereader.AngebotHandler;
import com.lp.server.angebot.fastlanereader.AngebotartHandler;
import com.lp.server.angebot.fastlanereader.AngeboterledigungsgrundHandler;
import com.lp.server.angebot.fastlanereader.AngebotpositionHandler;
import com.lp.server.angebot.fastlanereader.AngebotpositionartHandler;
import com.lp.server.angebot.fastlanereader.AngebottextHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklarbeitsplanHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklmaterialHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklmengenstaffelHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklmengenstaffelSchnellerfassungHandler;
import com.lp.server.angebotstkl.fastlanereader.AgstklpositionHandler;
import com.lp.server.angebotstkl.fastlanereader.AufschlagHandler;
import com.lp.server.angebotstkl.fastlanereader.EinkaufsangebotHandler;
import com.lp.server.angebotstkl.fastlanereader.EinkaufsangebotpositionHandler;
import com.lp.server.angebotstkl.fastlanereader.EkaglieferantHandler;
import com.lp.server.angebotstkl.fastlanereader.EkgruppeHandler;
import com.lp.server.angebotstkl.fastlanereader.EkgruppelieferantHandler;
import com.lp.server.angebotstkl.fastlanereader.EkweblieferantHandler;
import com.lp.server.angebotstkl.fastlanereader.PositionlieferantHandler;
import com.lp.server.angebotstkl.fastlanereader.PositionlieferantOptimierenHandler;
import com.lp.server.angebotstkl.fastlanereader.WebFindChipsHandler;
import com.lp.server.angebotstkl.fastlanereader.WeblieferantHandler;
import com.lp.server.artikel.fastlanereader.AlergenHandler;
import com.lp.server.artikel.fastlanereader.AlleSnrChnrHandler;
import com.lp.server.artikel.fastlanereader.ArtikelHandler;
import com.lp.server.artikel.fastlanereader.ArtikelSokoHandler;
import com.lp.server.artikel.fastlanereader.ArtikelSokomengenstaffelHandler;
import com.lp.server.artikel.fastlanereader.ArtikelalergenHandler;
import com.lp.server.artikel.fastlanereader.ArtikelbestelltHandler;
import com.lp.server.artikel.fastlanereader.ArtikelgruEingeschraenktHandler;
import com.lp.server.artikel.fastlanereader.ArtikelgruHandler;
import com.lp.server.artikel.fastlanereader.ArtikelherstellerHandler;
import com.lp.server.artikel.fastlanereader.ArtikelklaHandler;
import com.lp.server.artikel.fastlanereader.ArtikelkommentarHandler;
import com.lp.server.artikel.fastlanereader.ArtikelkommentarSucheHandler;
import com.lp.server.artikel.fastlanereader.ArtikelkommentarartHandler;
import com.lp.server.artikel.fastlanereader.ArtikellagerHandler;
import com.lp.server.artikel.fastlanereader.ArtikellagerplaetzeHandler;
import com.lp.server.artikel.fastlanereader.ArtikellieferantHandler;
import com.lp.server.artikel.fastlanereader.ArtikellieferantstaffelHandler;
import com.lp.server.artikel.fastlanereader.ArtikellisteHandler;
import com.lp.server.artikel.fastlanereader.ArtikelreservierungHandler;
import com.lp.server.artikel.fastlanereader.ArtikelshopgruppeHandler;
import com.lp.server.artikel.fastlanereader.ArtikelsperrenHandler;
import com.lp.server.artikel.fastlanereader.AutomotiveHandler;
import com.lp.server.artikel.fastlanereader.ChargenAufLagerHandler;
import com.lp.server.artikel.fastlanereader.DateiverweisHandler;
import com.lp.server.artikel.fastlanereader.EinkaufseanHandler;
import com.lp.server.artikel.fastlanereader.ErsatztypenHandler;
import com.lp.server.artikel.fastlanereader.FarbcodeHandler;
import com.lp.server.artikel.fastlanereader.FasessionHandler;
import com.lp.server.artikel.fastlanereader.FehlmengeHandler;
import com.lp.server.artikel.fastlanereader.FehlmengeReservierungAufloesenHandler;
import com.lp.server.artikel.fastlanereader.GebindeHandler;
import com.lp.server.artikel.fastlanereader.HandlagerbewegungHandler;
import com.lp.server.artikel.fastlanereader.InventurHandler;
import com.lp.server.artikel.fastlanereader.InventurlisteHandler;
import com.lp.server.artikel.fastlanereader.InventurprotokollHandler;
import com.lp.server.artikel.fastlanereader.InventurstandHandler;
import com.lp.server.artikel.fastlanereader.KatalogHandler;
import com.lp.server.artikel.fastlanereader.KundenidentnummerHandler;
import com.lp.server.artikel.fastlanereader.LagerGrunddatenHandler;
import com.lp.server.artikel.fastlanereader.LagerHandler;
import com.lp.server.artikel.fastlanereader.LagerMitMandantHandler;
import com.lp.server.artikel.fastlanereader.LagercockpitArtikelHandler;
import com.lp.server.artikel.fastlanereader.LagercockpitFehlmengeHandler;
import com.lp.server.artikel.fastlanereader.LagercockpitLossollmaterialHandler;
import com.lp.server.artikel.fastlanereader.LagercockpitNichtLagerbewirtschafteteArtikelHandler;
import com.lp.server.artikel.fastlanereader.LagerplatzHandler;
import com.lp.server.artikel.fastlanereader.LaseroberflaecheHandler;
import com.lp.server.artikel.fastlanereader.MaterialHandler;
import com.lp.server.artikel.fastlanereader.MaterialpreisHandler;
import com.lp.server.artikel.fastlanereader.MaterialzuschlagHandler;
import com.lp.server.artikel.fastlanereader.MedicalHandler;
import com.lp.server.artikel.fastlanereader.PaternosterHandler;
import com.lp.server.artikel.fastlanereader.PreislistennameHandler;
import com.lp.server.artikel.fastlanereader.ReachHandler;
import com.lp.server.artikel.fastlanereader.RohsHandler;
import com.lp.server.artikel.fastlanereader.SeriennummernchargennummernAufLagerHandler;
import com.lp.server.artikel.fastlanereader.ShopgruppeHandler;
import com.lp.server.artikel.fastlanereader.ShopgruppewebshopHandler;
import com.lp.server.artikel.fastlanereader.SnrChnrFuerReklamationHandler;
import com.lp.server.artikel.fastlanereader.SperrenHandler;
import com.lp.server.artikel.fastlanereader.VerfuegbarkeitHandler;
import com.lp.server.artikel.fastlanereader.VerleihHandler;
import com.lp.server.artikel.fastlanereader.VerpackungsmittelHandler;
import com.lp.server.artikel.fastlanereader.VerschleissteilHandler;
import com.lp.server.artikel.fastlanereader.VerschleissteilwerkzeugHandler;
import com.lp.server.artikel.fastlanereader.VkpfEinzelpreisHandler;
import com.lp.server.artikel.fastlanereader.VkpfStaffelmengeHandler;
import com.lp.server.artikel.fastlanereader.VkpfartikelverkaufspreisbasisHandler;
import com.lp.server.artikel.fastlanereader.VorschlagstextHandler;
import com.lp.server.artikel.fastlanereader.VorzugHandler;
import com.lp.server.artikel.fastlanereader.WaffenausfuehrungHandler;
import com.lp.server.artikel.fastlanereader.WaffenkaliberHandler;
import com.lp.server.artikel.fastlanereader.WaffenkategorieHandler;
import com.lp.server.artikel.fastlanereader.WaffentypFeinHandler;
import com.lp.server.artikel.fastlanereader.WaffentypHandler;
import com.lp.server.artikel.fastlanereader.WaffenzusatzHandler;
import com.lp.server.artikel.fastlanereader.WarenbewegungenHandler;
import com.lp.server.artikel.fastlanereader.WebshopHandler;
import com.lp.server.artikel.fastlanereader.WerkzeugHandler;
import com.lp.server.artikel.fastlanereader.ZugehoerigeHandler;
import com.lp.server.auftrag.bl.AuftragUebersichtHandler;
import com.lp.server.auftrag.bl.AuftragzeitenHandler;
import com.lp.server.auftrag.bl.SichtLieferstatusHandler;
import com.lp.server.auftrag.fastlanereader.AuftragEingangsrechnungenHandler;
import com.lp.server.auftrag.fastlanereader.AuftragHandler;
import com.lp.server.auftrag.fastlanereader.AuftragSichtLSREHandler;
import com.lp.server.auftrag.fastlanereader.AuftragartHandler;
import com.lp.server.auftrag.fastlanereader.AuftragbegruendungHandler;
import com.lp.server.auftrag.fastlanereader.AuftragdokumentHandler;
import com.lp.server.auftrag.fastlanereader.AuftragkostenstelleHandler;
import com.lp.server.auftrag.fastlanereader.AuftragpositionHandler;
import com.lp.server.auftrag.fastlanereader.AuftragpositionSichtRahmenHandler;
import com.lp.server.auftrag.fastlanereader.AuftragpositionZeiterfassungHandler;
import com.lp.server.auftrag.fastlanereader.AuftragpositionartHandler;
import com.lp.server.auftrag.fastlanereader.AuftragseriennrnHandler;
import com.lp.server.auftrag.fastlanereader.AuftragteilnehmerHandler;
import com.lp.server.auftrag.fastlanereader.AuftragtextHandler;
import com.lp.server.auftrag.fastlanereader.MeilensteinHandler;
import com.lp.server.auftrag.fastlanereader.PositionenSichtAuftragHandler;
import com.lp.server.auftrag.fastlanereader.VerrechenbarHandler;
import com.lp.server.auftrag.fastlanereader.ZahlungsplanHandler;
import com.lp.server.auftrag.fastlanereader.ZahlungsplanmeilensteinHandler;
import com.lp.server.auftrag.fastlanereader.ZeitplanHandler;
import com.lp.server.auftrag.fastlanereader.ZeitplantypHandler;
import com.lp.server.auftrag.fastlanereader.ZeitplantypdetailHandler;
import com.lp.server.benutzer.fastlanereader.ArtgrurolleHandler;
import com.lp.server.benutzer.fastlanereader.BenutzerHandler;
import com.lp.server.benutzer.fastlanereader.BenutzermandantsystemrolleHandler;
import com.lp.server.benutzer.fastlanereader.FertigungsgrupperolleHandler;
import com.lp.server.benutzer.fastlanereader.LagerrolleHandler;
import com.lp.server.benutzer.fastlanereader.NachrichtarchivHandler;
import com.lp.server.benutzer.fastlanereader.NachrichtartHandler;
import com.lp.server.benutzer.fastlanereader.RechtHandler;
import com.lp.server.benutzer.fastlanereader.RollerechtHandler;
import com.lp.server.benutzer.fastlanereader.SystemrolleHandler;
import com.lp.server.benutzer.fastlanereader.ThemaHandler;
import com.lp.server.benutzer.fastlanereader.ThemarolleHandler;
import com.lp.server.bestellung.fastlanereader.AnfragevorschlagHandler;
import com.lp.server.bestellung.fastlanereader.BSMahnlaufHandler;
import com.lp.server.bestellung.fastlanereader.BSMahnstufeHandler;
import com.lp.server.bestellung.fastlanereader.BSMahntextHandler;
import com.lp.server.bestellung.fastlanereader.BSMahnungHandler;
import com.lp.server.bestellung.fastlanereader.BSZahlungsplanHandler;
import com.lp.server.bestellung.fastlanereader.BestellpositionSichtRahmenHandler;
import com.lp.server.bestellung.fastlanereader.BestellpositionartHandler;
import com.lp.server.bestellung.fastlanereader.Bestellpositionhandler;
import com.lp.server.bestellung.fastlanereader.BestellungHandler;
import com.lp.server.bestellung.fastlanereader.BestellungSichtLieferTermineHandler;
import com.lp.server.bestellung.fastlanereader.BestellungWEPEingangHandler;
import com.lp.server.bestellung.fastlanereader.BestellungartHandler;
import com.lp.server.bestellung.fastlanereader.BestellungstatusHandler;
import com.lp.server.bestellung.fastlanereader.BestellungtextHandler;
import com.lp.server.bestellung.fastlanereader.BestellungwareneingangHandler;
import com.lp.server.bestellung.fastlanereader.BestellvorschlagAlleTermineHandler;
import com.lp.server.bestellung.fastlanereader.BestellvorschlagHandler;
import com.lp.server.bestellung.fastlanereader.LieferantenoptimierenHandler;
import com.lp.server.bestellung.fastlanereader.MahngruppeHandler;
import com.lp.server.bestellung.fastlanereader.OffeneWEPosHandler;
import com.lp.server.bestellung.fastlanereader.WarezwischenmandantenHandler;
import com.lp.server.eingangsrechnung.bl.EingangsrechnungUebersichtHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungAuftragszuordnungHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungKontierungHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungWEHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungartHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungsstatusHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungtextHandler;
import com.lp.server.eingangsrechnung.fastlanereader.EingangsrechnungzahlungHandler;
import com.lp.server.eingangsrechnung.fastlanereader.WEPsEinerRechnungsadresseHandler;
import com.lp.server.eingangsrechnung.fastlanereader.WEsEinerRechnungsadresseHandler;
import com.lp.server.eingangsrechnung.fastlanereader.ZahlungsvorschlagHandler;
import com.lp.server.eingangsrechnung.fastlanereader.ZahlungsvorschlaglaufHandler;
import com.lp.server.eingangsrechnung.fastlanereader.ZusatzkostenHandler;
import com.lp.server.fertigung.bl.BewegungsvorschauHandler;
import com.lp.server.fertigung.bl.LosNachkalkulationHandler;
import com.lp.server.fertigung.bl.LoszeitenHandler;
import com.lp.server.fertigung.fastlanereader.BedarfsuebernahmeHandler;
import com.lp.server.fertigung.fastlanereader.InternebestellungHandler;
import com.lp.server.fertigung.fastlanereader.LosHandler;
import com.lp.server.fertigung.fastlanereader.LosablieferungHandler;
import com.lp.server.fertigung.fastlanereader.LosbereichHandler;
import com.lp.server.fertigung.fastlanereader.LoseErledigenHandler;
import com.lp.server.fertigung.fastlanereader.LosgutschlechtHandler;
import com.lp.server.fertigung.fastlanereader.LosistmaterialHandler;
import com.lp.server.fertigung.fastlanereader.LosklasseHandler;
import com.lp.server.fertigung.fastlanereader.LoslagerentnahmeHandler;
import com.lp.server.fertigung.fastlanereader.LoslosklasseHandler;
import com.lp.server.fertigung.fastlanereader.LospruefplanHandler;
import com.lp.server.fertigung.fastlanereader.LossollarbeitsplanHandler;
import com.lp.server.fertigung.fastlanereader.LossollmaterialHandler;
import com.lp.server.fertigung.fastlanereader.LossollmaterialTrumpfHandler;
import com.lp.server.fertigung.fastlanereader.LosstatusHandler;
import com.lp.server.fertigung.fastlanereader.LostechnikerHandler;
import com.lp.server.fertigung.fastlanereader.LoszusatzstatusHandler;
import com.lp.server.fertigung.fastlanereader.OffeneAgsHandler;
import com.lp.server.fertigung.fastlanereader.ProduzierenHandler;
import com.lp.server.fertigung.fastlanereader.TrumpfmaterialHandler;
import com.lp.server.fertigung.fastlanereader.WiederholendeloseHandler;
import com.lp.server.fertigung.fastlanereader.ZusatzstatusHandler;
import com.lp.server.finanz.fastlanereader.BankkontoHandler;
import com.lp.server.finanz.fastlanereader.BuchungDetailBuchungsjournalHandler;
import com.lp.server.finanz.fastlanereader.BuchungDetailHandler;
import com.lp.server.finanz.fastlanereader.BuchungDetailKassenbuchHandler;
import com.lp.server.finanz.fastlanereader.BuchungHandler;
import com.lp.server.finanz.fastlanereader.ErgebnisgruppeHandler;
import com.lp.server.finanz.fastlanereader.ExportdatenHandler;
import com.lp.server.finanz.fastlanereader.ExportlaufHandler;
import com.lp.server.finanz.fastlanereader.FinanzBuchungenDetailliertHandler;
import com.lp.server.finanz.fastlanereader.FinanzamtHandler;
import com.lp.server.finanz.fastlanereader.KassenbuchHandler;
import com.lp.server.finanz.fastlanereader.KontoHandlerAuswahl;
import com.lp.server.finanz.fastlanereader.KontoHandlerDebitorenkonten;
import com.lp.server.finanz.fastlanereader.KontoHandlerKreditorenkonten;
import com.lp.server.finanz.fastlanereader.KontoHandlerSachkonten;
import com.lp.server.finanz.fastlanereader.KontoLandHandler;
import com.lp.server.finanz.fastlanereader.KontoartHandler;
import com.lp.server.finanz.fastlanereader.KontolaenderartHandler;
import com.lp.server.finanz.fastlanereader.LaenderartHandler;
import com.lp.server.finanz.fastlanereader.MahnlaufHandler;
import com.lp.server.finanz.fastlanereader.MahnsperreHandler;
import com.lp.server.finanz.fastlanereader.MahnspesenHandler;
import com.lp.server.finanz.fastlanereader.MahnstufeHandler;
import com.lp.server.finanz.fastlanereader.MahntextHandler;
import com.lp.server.finanz.fastlanereader.MahnungHandler;
import com.lp.server.finanz.fastlanereader.ReversechargeartHandler;
import com.lp.server.finanz.fastlanereader.SepakontoauszugHandler;
import com.lp.server.finanz.fastlanereader.SteuerkategorieHandler;
import com.lp.server.finanz.fastlanereader.UvaartHandler;
import com.lp.server.finanz.fastlanereader.WarenverkehrsnummerHandler;
import com.lp.server.forecast.fastlanereader.FclieferadresseHandler;
import com.lp.server.forecast.fastlanereader.ForecastHandler;
import com.lp.server.forecast.fastlanereader.ForecastartHandler;
import com.lp.server.forecast.fastlanereader.ForecastauftragHandler;
import com.lp.server.forecast.fastlanereader.ForecastpositionHandler;
import com.lp.server.forecast.fastlanereader.ImportdefHandler;
import com.lp.server.forecast.fastlanereader.KommdruckerHandler;
import com.lp.server.forecast.fastlanereader.LinienabrufHandler;
import com.lp.server.inserat.fastlanereader.EingangsrechnungenEinesInseratesHandler;
import com.lp.server.inserat.fastlanereader.InseratHandler;
import com.lp.server.inserat.fastlanereader.InseratartikelHandler;
import com.lp.server.inserat.fastlanereader.InserateOhneERHandler;
import com.lp.server.inserat.fastlanereader.InseraterHandler;
import com.lp.server.inserat.fastlanereader.InseratrechnungHandler;
import com.lp.server.instandhaltung.fastlanereader.AnlageHandler;
import com.lp.server.instandhaltung.fastlanereader.GeraetHandler;
import com.lp.server.instandhaltung.fastlanereader.GeraetehistorieHandler;
import com.lp.server.instandhaltung.fastlanereader.GeraetehistorieMitGeraetHandler;
import com.lp.server.instandhaltung.fastlanereader.GeraetetypHandler;
import com.lp.server.instandhaltung.fastlanereader.GewerkHandler;
import com.lp.server.instandhaltung.fastlanereader.HalleHandler;
import com.lp.server.instandhaltung.fastlanereader.InstandhaltungHandler;
import com.lp.server.instandhaltung.fastlanereader.IskategorieHandler;
import com.lp.server.instandhaltung.fastlanereader.IsmaschineHandler;
import com.lp.server.instandhaltung.fastlanereader.StandortHandler;
import com.lp.server.instandhaltung.fastlanereader.StandorttechnikerHandler;
import com.lp.server.instandhaltung.fastlanereader.WartungslisteHandler;
import com.lp.server.instandhaltung.fastlanereader.WartungsschritteHandler;
import com.lp.server.kueche.fastlanereader.BedienerlagerHandler;
import com.lp.server.kueche.fastlanereader.KassaartikelHandler;
import com.lp.server.kueche.fastlanereader.Kdc100logHandler;
import com.lp.server.kueche.fastlanereader.KuecheumrechnungHandler;
import com.lp.server.kueche.fastlanereader.SpeiseplanHandler;
import com.lp.server.kueche.fastlanereader.TageslosHandler;
import com.lp.server.lieferschein.bl.LieferscheinUebersichtHandler;
import com.lp.server.lieferschein.bl.LieferscheinUmsatzHandler;
import com.lp.server.lieferschein.fastlanereader.AuftraegeEinesLieferscheinsHandler;
import com.lp.server.lieferschein.fastlanereader.AuftragpositionInLieferscheinHandler;
import com.lp.server.lieferschein.fastlanereader.AusliefervorschlagHandler;
import com.lp.server.lieferschein.fastlanereader.BegruendungHandler;
import com.lp.server.lieferschein.fastlanereader.LieferscheinHandler;
import com.lp.server.lieferschein.fastlanereader.LieferscheinartHandler;
import com.lp.server.lieferschein.fastlanereader.LieferscheinpositionHandler;
import com.lp.server.lieferschein.fastlanereader.LieferscheinpositionartHandler;
import com.lp.server.lieferschein.fastlanereader.LieferscheintextHandler;
import com.lp.server.lieferschein.fastlanereader.VerkettetHandler;
import com.lp.server.media.fastlanereader.EmailMediaBelegHandler;
import com.lp.server.media.fastlanereader.EmailMediaInboxHandler;
import com.lp.server.partner.bl.KundeUmsatzHandler;
import com.lp.server.partner.bl.LieferantMonatsstatistikHandler;
import com.lp.server.partner.fastlanereader.AnredeHandler;
import com.lp.server.partner.fastlanereader.AnsprechpartnerHandler;
import com.lp.server.partner.fastlanereader.AnsprechpartnerfunktionHandler;
import com.lp.server.partner.fastlanereader.BankHandler;
import com.lp.server.partner.fastlanereader.BeauskunftungHandler;
import com.lp.server.partner.fastlanereader.BrancheHandler;
import com.lp.server.partner.fastlanereader.CockpitHandler;
import com.lp.server.partner.fastlanereader.DsgvokategorieHandler;
import com.lp.server.partner.fastlanereader.DsgvotextHandler;
import com.lp.server.partner.fastlanereader.IdentifikationHandler;
import com.lp.server.partner.fastlanereader.KommunikationsartHandler;
import com.lp.server.partner.fastlanereader.KontaktHandler;
import com.lp.server.partner.fastlanereader.KontaktartHandler;
import com.lp.server.partner.fastlanereader.KundeHandler;
import com.lp.server.partner.fastlanereader.KundeStrukturHandler;
import com.lp.server.partner.fastlanereader.KundekennungHandler;
import com.lp.server.partner.fastlanereader.KundesokoHandler;
import com.lp.server.partner.fastlanereader.KundesokomengenstaffelHandler;
import com.lp.server.partner.fastlanereader.LFLiefergruppenHandler;
import com.lp.server.partner.fastlanereader.LFLiefergruppenOneLFHandler;
import com.lp.server.partner.fastlanereader.LieferantHandler;
import com.lp.server.partner.fastlanereader.LieferantbeurteilungHandler;
import com.lp.server.partner.fastlanereader.LiefergruppenHandler;
import com.lp.server.partner.fastlanereader.NewslettergrundHandler;
import com.lp.server.partner.fastlanereader.PAKurzbriefHandler;
import com.lp.server.partner.fastlanereader.PASelektionHandler;
import com.lp.server.partner.fastlanereader.PartnerBankHandler;
import com.lp.server.partner.fastlanereader.PartnerHandler;
import com.lp.server.partner.fastlanereader.PartnerReferenzHandler;
import com.lp.server.partner.fastlanereader.PartnerTelefonzeitenHandler;
import com.lp.server.partner.fastlanereader.PartnerartHandler;
import com.lp.server.partner.fastlanereader.PartnerklasseHandler;
import com.lp.server.partner.fastlanereader.PartnerkommentarHandler;
import com.lp.server.partner.fastlanereader.PartnerkommentarartHandler;
import com.lp.server.partner.fastlanereader.PartnerkommunikationHandler;
import com.lp.server.partner.fastlanereader.ProvisionsempfaengerHandler;
import com.lp.server.partner.fastlanereader.SachbearbeiterHandler;
import com.lp.server.partner.fastlanereader.SelektionHandler;
import com.lp.server.partner.fastlanereader.SerienbriefHandler;
import com.lp.server.partner.fastlanereader.SerienbriefselektionnegativHandler;
import com.lp.server.partner.fastlanereader.WiedervorlageHandler;
import com.lp.server.partner.fastlanereader.generated.AnsprechpartnerPartnerHandler;
import com.lp.server.partner.fastlanereader.generated.SerienbriefselektionHandler;
import com.lp.server.personal.fastlanereader.*;
import com.lp.server.projekt.bl.ProjektzeitenHandler;
import com.lp.server.projekt.fastlanereader.BereichHandler;
import com.lp.server.projekt.fastlanereader.HistoryHandler;
import com.lp.server.projekt.fastlanereader.HistoryartHandler;
import com.lp.server.projekt.fastlanereader.KategorieHandler;
import com.lp.server.projekt.fastlanereader.ProjektHandler;
import com.lp.server.projekt.fastlanereader.ProjektHandlerSchnell;
import com.lp.server.projekt.fastlanereader.ProjektQueueHandler;
import com.lp.server.projekt.fastlanereader.ProjektStatusHandler;
import com.lp.server.projekt.fastlanereader.ProjektTypHandler;
import com.lp.server.projekt.fastlanereader.ProjekterledigungsgrundHandler;
import com.lp.server.projekt.fastlanereader.ProjektgruppeHandler;
import com.lp.server.projekt.fastlanereader.ProjekttaetigkeitHandler;
import com.lp.server.projekt.fastlanereader.ProjekttechnikerHandler;
import com.lp.server.projekt.fastlanereader.ProjektverlaufHandler;
import com.lp.server.projekt.fastlanereader.VkfortschrittHandler;
import com.lp.server.rechnung.bl.GutschriftUmsatzHandler;
import com.lp.server.rechnung.bl.RechnungUmsatzHandler;
import com.lp.server.rechnung.fastlanereader.AbrechnungsvorschlagHandler;
import com.lp.server.rechnung.fastlanereader.AuftraegeEinerRechnungHandler;
import com.lp.server.rechnung.fastlanereader.GutschriftHandler;
import com.lp.server.rechnung.fastlanereader.GutschriftPositionHandler;
import com.lp.server.rechnung.fastlanereader.GutschriftgrundHandler;
import com.lp.server.rechnung.fastlanereader.GutschriftpositionsartHandler;
import com.lp.server.rechnung.fastlanereader.GutschrifttextHandler;
import com.lp.server.rechnung.fastlanereader.LastschriftvorschlagHandler;
import com.lp.server.rechnung.fastlanereader.MmzHandler;
import com.lp.server.rechnung.fastlanereader.ProformarechnungHandler;
import com.lp.server.rechnung.fastlanereader.ProformarechnungPositionHandler;
import com.lp.server.rechnung.fastlanereader.ProformarechnungpositionsartHandler;
import com.lp.server.rechnung.fastlanereader.RechnungHandler;
import com.lp.server.rechnung.fastlanereader.RechnungPositionHandler;
import com.lp.server.rechnung.fastlanereader.RechnungkontierungHandler;
import com.lp.server.rechnung.fastlanereader.RechnungpositionsartHandler;
import com.lp.server.rechnung.fastlanereader.RechnungsartHandler;
import com.lp.server.rechnung.fastlanereader.RechnungstatusHandler;
import com.lp.server.rechnung.fastlanereader.RechnungtextHandler;
import com.lp.server.rechnung.fastlanereader.RechnungtypHandler;
import com.lp.server.rechnung.fastlanereader.VerrechnungsmodellHandler;
import com.lp.server.rechnung.fastlanereader.VerrechnungsmodelltagHandler;
import com.lp.server.rechnung.fastlanereader.VorkasseSichtAuftragHandler;
import com.lp.server.rechnung.fastlanereader.ZahlungHandler;
import com.lp.server.rechnung.fastlanereader.ZahlungsartHandler;
import com.lp.server.rechnung.fastlanereader.ZeitinfoHandler;
import com.lp.server.reklamation.fastlanereader.AufnahmeartHandler;
import com.lp.server.reklamation.fastlanereader.BehandlungHandler;
import com.lp.server.reklamation.fastlanereader.FehlerHandler;
import com.lp.server.reklamation.fastlanereader.FehlerangabeHandler;
import com.lp.server.reklamation.fastlanereader.MassnahmeHandler;
import com.lp.server.reklamation.fastlanereader.ReklamationHandler;
import com.lp.server.reklamation.fastlanereader.ReklamationbildHandler;
import com.lp.server.reklamation.fastlanereader.SchwereHandler;
import com.lp.server.reklamation.fastlanereader.TermintreueHandler;
import com.lp.server.reklamation.fastlanereader.WirksamkeitHandler;
import com.lp.server.stueckliste.fastlanereader.AlternativmaschineHandler;
import com.lp.server.stueckliste.fastlanereader.ApkommentarHandler;
import com.lp.server.stueckliste.fastlanereader.FertigungsgruppeEingeschraenktHandler;
import com.lp.server.stueckliste.fastlanereader.FertigungsgruppeHandler;
import com.lp.server.stueckliste.fastlanereader.KommentarimportHandler;
import com.lp.server.stueckliste.fastlanereader.MontageartHandler;
import com.lp.server.stueckliste.fastlanereader.PosersatzHandler;
import com.lp.server.stueckliste.fastlanereader.PruefartHandler;
import com.lp.server.stueckliste.fastlanereader.PruefkombinationHandler;
import com.lp.server.stueckliste.fastlanereader.StklagerentnahmeHandler;
import com.lp.server.stueckliste.fastlanereader.StklparameterHandler;
import com.lp.server.stueckliste.fastlanereader.StklpruefplanHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklisteHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklisteHandlerMitMandant;
import com.lp.server.stueckliste.fastlanereader.StuecklisteScriptartHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklistearbeitsplanHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklisteeigenschaftHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklisteeigenschaftartHandler;
import com.lp.server.stueckliste.fastlanereader.StuecklistepositionHandler;
import com.lp.server.system.fastlanereader.ArbeitsplatzHandler;
import com.lp.server.system.fastlanereader.ArbeitsplatzparameterHandler;
import com.lp.server.system.fastlanereader.AutomatikjobHandler;
import com.lp.server.system.fastlanereader.BelegartHandler;
import com.lp.server.system.fastlanereader.BelegartdokumentHandler;
import com.lp.server.system.fastlanereader.DokumenteBelegartHandler;
import com.lp.server.system.fastlanereader.DokumenteGruppeHandler;
import com.lp.server.system.fastlanereader.DokumentenlinkHandler;
import com.lp.server.system.fastlanereader.EinheitHandler;
import com.lp.server.system.fastlanereader.EinheitKonvertierungHandler;
import com.lp.server.system.fastlanereader.ExtralisteHandler;
import com.lp.server.system.fastlanereader.FunktionHandler;
import com.lp.server.system.fastlanereader.HVMAParameterHandler;
import com.lp.server.system.fastlanereader.KennungHandler;
import com.lp.server.system.fastlanereader.KostenstelleHandler;
import com.lp.server.system.fastlanereader.KostentraegerHandler;
import com.lp.server.system.fastlanereader.LandHandler;
import com.lp.server.system.fastlanereader.LandKfzKennzeichenHandler;
import com.lp.server.system.fastlanereader.LandPLZOrtHandler;
import com.lp.server.system.fastlanereader.LieferartHandler;
import com.lp.server.system.fastlanereader.MailPropertyHandler;
import com.lp.server.system.fastlanereader.MandantHandler;
import com.lp.server.system.fastlanereader.MediaartHandler;
import com.lp.server.system.fastlanereader.MediastandardHandler;
import com.lp.server.system.fastlanereader.MwstsatzHandler;
import com.lp.server.system.fastlanereader.MwstsatzbezHandler;
import com.lp.server.system.fastlanereader.OrtHandler;
import com.lp.server.system.fastlanereader.PanelHandler;
import com.lp.server.system.fastlanereader.PanelbeschreibungHandler;
import com.lp.server.system.fastlanereader.PanelsperrenHandler;
import com.lp.server.system.fastlanereader.ParameterHandler;
import com.lp.server.system.fastlanereader.ParameteranwenderHandler;
import com.lp.server.system.fastlanereader.ParametermandantHandler;
import com.lp.server.system.fastlanereader.ParametermandantgueltigabHandler;
import com.lp.server.system.fastlanereader.PositionsartHandler;
import com.lp.server.system.fastlanereader.ReportvarianteHandler;
import com.lp.server.system.fastlanereader.SpediteurHandler;
import com.lp.server.system.fastlanereader.StatusHandler;
import com.lp.server.system.fastlanereader.TheClientHandler;
import com.lp.server.system.fastlanereader.TheConcurrentUserHandler;
import com.lp.server.system.fastlanereader.TheJudgeHandler;
import com.lp.server.system.fastlanereader.VersandauftragHandler;
import com.lp.server.system.fastlanereader.WaehrungHandler;
import com.lp.server.system.fastlanereader.WechselkursHandler;
import com.lp.server.system.fastlanereader.ZahlungszielHandler;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Pair;

@Singleton
@Lock(LockType.READ)
public class FastLaneReaderBean extends Facade implements FastLaneReader {
	// protected final ILPLogger myLogger =
	// LPLogService.getInstance().getLogger(
	// this.getClass());

	/**
	 * holds instances of UseCaseHandlers for all use cases.
	 */
	private HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> useCaseHandlers;
	private int iCleanupZaehler = 0;

	@PersistenceContext(unitName = "ejb")
	private EntityManager em;

	/**
	 * Holt den UseCaseHandler zu einer bestimmten UseCase ID und ruft .setQuery
	 * auf.
	 * 
	 * @param useCaseId die ID des UseCase
	 * @param query     QueryParameters alle Filterkriterien, null ist erlaubt
	 * @return QueryResult das Ergebnis der Abfrage, enthaelt PAGE_SIZE Elemente
	 * @throws EJBExceptionLP
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public QueryResult setQuery(String uuid, Integer useCaseId, QueryParameters query, TheClientDto theClientDto)
			throws EJBExceptionLP {
		QueryResult qr = null;

		try {
			// UW 31.03.06 im vorherigen Aufruf wurde sCurrentUser gesetzt
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);
			// System.out.println("setQuery: " + useCaseId + " "
			// + ucHandler.toString() + " FLR:" + this.toString());

			qr = ucHandler.setQuery(query);
		} catch (Throwable t) {
			if (t instanceof EJBExceptionLP) {
				throw (EJBExceptionLP) t;
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
			}
		}
		return qr;
	}

	/**
	 * Holt den UseCaseHandler zu einer bestimmten UseCase ID und ruft .sort auf.
	 * 
	 * @param useCaseId        die ID des UseCase
	 * @param sortierKriterien alle Sortierkriterien, null ist erlaubt
	 * @param selectedId       der Key jenes Datensatzes, der selektiert sein soll
	 * @return QueryResult das Ergebnis der Abfrage, enthaelt PAGE_SIZE Elemente
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public QueryResult sort(String uuid, Integer useCaseId, SortierKriterium[] sortierKriterien, Object selectedId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		QueryResult qr = null;

		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);

			qr = ucHandler.sort(sortierKriterien, selectedId);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return qr;
	}

	/**
	 * Holt den UseCaseHandler zu einer bestimmten UseCase ID und ruft .getPageAt
	 * auf.
	 * 
	 * @param useCaseId die ID des UseCase
	 * @param row       die gewuenschte Zeile
	 * @return QueryResult das Ergebnis der Abfrage, enthaelt PAGE_SIZE Elemente
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public QueryResult getPageAt(String uuid, Integer useCaseId, Integer row, TheClientDto theClientDto)
			throws EJBExceptionLP {
		QueryResult qr = null;

		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);

			qr = ucHandler.getPageAt(row);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return qr;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public QueryResult getResults(String uuid, Integer useCaseId, long iPageSize, TheClientDto theClientDto)
			throws EJBExceptionLP {
		QueryResult qr = null;

		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);
			UseCaseHandler.PAGE_SIZE = (int) iPageSize;
			qr = ucHandler.getPageAt(0);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return qr;
	}

	/**
	 * Liefert die TableInfo fuer den angegeben UseCase
	 * 
	 * @param uuid      die unique Id mit der der UsecaseHandler erzeugt wurde
	 * @param useCaseId die ID des UseCase
	 * @return TableInfo die Information fuer die Tabelle am Client
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public TableInfo getTableInfo(String uuid, Integer useCaseId, FilterKriterium[] defaultFilter,
			TheClientDto theClientDto) throws EJBExceptionLP {
		TableInfo ti = null;

		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);
			ucHandler.setDefaultFiltersForTableInfo(defaultFilter);
			ti = ucHandler.getTableInfo();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return ti;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public TableColumnInformation getTableColumnInfo(String uuid, Integer useCaseId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		TableColumnInformation info = null;

		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);
			info = ucHandler.getTableColumnInformation();
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}

		return info;
	}

	/**
	 * called when a new instance of the FastLaneReaderBean is created. For each use
	 * case an instance of {@link com.lp.server.util.fastlanereader.UseCaseHandler
	 * UseCaseHandler} has to be registered here.
	 */

	public FastLaneReaderBean() {
		if (this.useCaseHandlers == null) {
			this.useCaseHandlers = new HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>>();
		}
	}

	public synchronized UseCaseHandler getUseCaseHandler(String uuid, Integer useCaseId, TheClientDto theClientDto) {
		//
		// // SP 2015
		// StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		// if (stack != null && stack.length > 2)
		// myLogger.warn(stack[2].getMethodName());
		// myLogger.warn(String.format(
		// "uuid = %s; useCaseId = %d; IDUser = %s; Login Time = %s",
		// uuid, useCaseId, theClientDto.getIDUser(), theClientDto
		// .getDLoggedin().toString()));

		HashMap<Integer, HashMap<String, UseCaseHandler>> hashMapDesClients = useCaseHandlers
				.get(theClientDto.getIDUser());

		if (hashMapDesClients == null) {
			hashMapDesClients = new HashMap<Integer, HashMap<String, UseCaseHandler>>();
		}

		HashMap<String, UseCaseHandler> hashUsecases = hashMapDesClients.get(useCaseId);

		if (hashUsecases == null) {
			hashUsecases = new HashMap<String, UseCaseHandler>();
		}

		UseCaseHandler oUCHandler = hashUsecases.get(uuid);
		if (oUCHandler == null) {
			switch (useCaseId.intValue()) {
			case QueryParameters.UC_ID_AUFTRAG: {
				oUCHandler = new AuftragHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGPOSITION: {
				oUCHandler = new AuftragpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGPOSITION_ZEITERFASSUNG: {
				oUCHandler = new AuftragpositionZeiterfassungHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKEL: { // nicht mehr in verwendung
				oUCHandler = new ArtikelHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNG: {
				oUCHandler = new RechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_PROVISIONSEMPFAENGER: {
				oUCHandler = new ProvisionsempfaengerHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGSICHTLSRE: {
				oUCHandler = new AuftragSichtLSREHandler();
			}
				break;
			case QueryParameters.UC_ID_REFERENZ_ZU: {
				oUCHandler = new PartnerReferenzHandler();
			}
				break;
			case QueryParameters.UC_ID_COCKPIT: {
				oUCHandler = new CockpitHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNG: {
				oUCHandler = new ZahlungHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFT: {
				oUCHandler = new GutschriftHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTVERLAUF: {
				oUCHandler = new ProjektverlaufHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITERFASSUNG_FAVORITEN: {
				oUCHandler = new ZeiterfassungFavoritenHandler();
			}
				break;
			case QueryParameters.UC_ID_FINANZKONTEN: {
				oUCHandler = new KontoHandlerAuswahl();
			}
				break;
			case QueryParameters.UC_ID_ERGEBNISGRUPPE: {
				oUCHandler = new ErgebnisgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_FINANZKONTEN_DEBITOREN: {
				oUCHandler = new KontoHandlerDebitorenkonten();
			}
				break;
			case QueryParameters.UC_ID_FINANZKONTEN_KREDITOREN: {
				oUCHandler = new KontoHandlerKreditorenkonten();
			}
				break;
			case QueryParameters.UC_ID_FINANZKONTEN_SACHKONTEN: {
				oUCHandler = new KontoHandlerSachkonten();
			}
				break;
			case QueryParameters.UC_ID_KONTOLAENDERART: {
				oUCHandler = new KontolaenderartHandler();
			}
				break;
			case QueryParameters.UC_ID_KONTOLAND: {
				oUCHandler = new KontoLandHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNLAUF: {
				oUCHandler = new MahnlaufHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNUNG: {
				oUCHandler = new MahnungHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNSPERRE: {
				oUCHandler = new MahnsperreHandler();
			}
				break;
			case QueryParameters.UC_ID_BANK: {
				oUCHandler = new BankHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNG_UMSATZ: {
				oUCHandler = new RechnungUmsatzHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFT_UMSATZ: {
				oUCHandler = new GutschriftUmsatzHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGPOSITION: {
				oUCHandler = new RechnungPositionHandler();
			}
				break;
			case QueryParameters.UC_ID_PROFORMARECHNUNG: {
				oUCHandler = new ProformarechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_PROFORMARECHNUNGPOSITION: { // wird nicht
				// verwendet
				oUCHandler = new ProformarechnungPositionHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFTPOSITION: {
				oUCHandler = new GutschriftPositionHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELGRUPPE: {
				oUCHandler = new ArtikelgruHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELGRUPPE_EINGESCHRAENKT: {
				oUCHandler = new ArtikelgruEingeschraenktHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELKLASSE: {
				oUCHandler = new ArtikelklaHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELHERSTELLER: {
				oUCHandler = new ArtikelherstellerHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELLIEFERANT: {
				oUCHandler = new ArtikellieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_KASSENBUCH: {
				oUCHandler = new KassenbuchHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG: {
				oUCHandler = new EingangsrechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNGEN_EINES_INSERATES: {
				oUCHandler = new EingangsrechnungenEinesInseratesHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUSATZKOSTEN: {
				oUCHandler = new ZusatzkostenHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_UMSATZ: {
				oUCHandler = new EingangsrechnungUebersichtHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_AUFTRAGSZUORDNUNG: {
				oUCHandler = new EingangsrechnungAuftragszuordnungHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGEINGANGSRECHNUNGEN: {
				oUCHandler = new AuftragEingangsrechnungenHandler();
			}
				break;
			case QueryParameters.UC_ID_FINANZAMT: {
				oUCHandler = new FinanzamtHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_KONTIERUNG: {
				oUCHandler = new EingangsrechnungKontierungHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_ZAHLUNG: {
				oUCHandler = new EingangsrechnungzahlungHandler();
			}
				break;
			case QueryParameters.UC_ID_BUCHUNG: {
				oUCHandler = new BuchungHandler();
			}
				break;
			case QueryParameters.UC_ID_BUCHUNGDETAILKASSENBUCH: {
				oUCHandler = new BuchungDetailKassenbuchHandler();
			}
				break;
			case QueryParameters.UC_ID_BUCHUNGDETAIL: {
				oUCHandler = new BuchungDetailHandler();
			}
				break;
			case QueryParameters.UC_ID_BUCHUNGDETAILBUCHUNGSJOURNAL: {
				oUCHandler = new BuchungDetailBuchungsjournalHandler();
			}
				break;
			case QueryParameters.UC_ID_BUCHUNGDETAILLIERT: {
				oUCHandler = new FinanzBuchungenDetailliertHandler();
			}
				break;
			case QueryParameters.UC_ID_KATALOG: {
				oUCHandler = new KatalogHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGER: {
				oUCHandler = new LagerHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGER_ALLE: {
				oUCHandler = new LagerGrunddatenHandler();
			}
				break;
			case QueryParameters.UC_ID_MATERIAL: {
				oUCHandler = new MaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDEMATERIAL: {
				oUCHandler = new com.lp.server.partner.fastlanereader.KundematerialHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDESPEDITEUR: {
				oUCHandler = new com.lp.server.partner.fastlanereader.KundespediteurHandler();
			}
				break;
			case QueryParameters.UC_ID_MATERIALZUSCHLAG: {
				oUCHandler = new MaterialzuschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_MATERIALPREIS: {
				oUCHandler = new MaterialpreisHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELLAGER: {
				oUCHandler = new ArtikellagerHandler();
			}
				break;
			case QueryParameters.UC_ID_THECLIENT: { 
				oUCHandler = new TheClientHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGTEILNEHMER: {
				oUCHandler = new AuftragteilnehmerHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGKOSTENSTELLE: {
				oUCHandler = new AuftragkostenstelleHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELRESERVIERUNG: { // wird nirgends
				// verwendet
				oUCHandler = new ArtikelreservierungHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNER: {
				oUCHandler = new PartnerHandler();
			}
				break;
			case QueryParameters.UC_ID_SHOPGRUPPE: {
				oUCHandler = new ShopgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_ERSATZTYPEN: {
				oUCHandler = new ErsatztypenHandler();
			}
				break;
			case QueryParameters.UC_ID_SHOPGRUPPEWEBSHOP: {
				oUCHandler = new ShopgruppewebshopHandler();
			}
				break;
			case QueryParameters.UC_ID_ANSPRECHPARTNER: {
				oUCHandler = new AnsprechpartnerHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEIN: {
				oUCHandler = new LieferscheinHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINPOSITION: {
				oUCHandler = new LieferscheinpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINPOSITIONSICHTAUFTRAG: {
				oUCHandler = new PositionenSichtAuftragHandler();
			}
				break;
			case QueryParameters.UC_ID_VORKASSEPOSITIONSICHTAUFTRAG: {
				oUCHandler = new VorkasseSichtAuftragHandler();
			}
				break;
			case QueryParameters.UC_ID_THEJUDGE: {
				oUCHandler = new TheJudgeHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONAL: {
				oUCHandler = new PersonalHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONAL_ZEITERFASSUNG: {
				oUCHandler = new PersonalZeiterfassungHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDE2: {
				oUCHandler = new KundeHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDE_STRUKTUR: {
				oUCHandler = new KundeStrukturHandler();
			}
				break;
			case QueryParameters.UC_ID_LANDPLZORT: {
				oUCHandler = new LandPLZOrtHandler();
			}
				break;
			case QueryParameters.UC_ID_HANDLAGERBEWEGUNG: {
				oUCHandler = new HandlagerbewegungHandler();
			}
				break;
			case QueryParameters.UC_ID_KONTOART: {
				oUCHandler = new KontoartHandler();
			}
				break;
			case QueryParameters.UC_ID_UVAART: {
				oUCHandler = new UvaartHandler();
			}
				break;
			case QueryParameters.UC_ID_KOSTENSTELLE: {
				oUCHandler = new KostenstelleHandler();
			}
				break;
			case QueryParameters.UC_ID_SACHBERABEITER: {
				oUCHandler = new SachbearbeiterHandler();
			}
				break;
			case QueryParameters.UC_ID_PREISLISTENNAME: {
				oUCHandler = new PreislistennameHandler();
			}
				break;
			case QueryParameters.UC_ID_SICHTLIEFERSTATUS: {
				oUCHandler = new SichtLieferstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGUEBERSICHT: {
				oUCHandler = new AuftragUebersichtHandler();
			}
				break;
			case QueryParameters.UC_ID_BRANCHE: {
				oUCHandler = new BrancheHandler();
			}
				break;
			case QueryParameters.UC_ID_IDENTIFIKATION: {
				oUCHandler = new IdentifikationHandler();
			}
				break;
			case QueryParameters.UC_ID_DSGVOKATEGORIE: {
				oUCHandler = new DsgvokategorieHandler();
			}
				break;
			case QueryParameters.UC_ID_ABWESENHEITSART: {
				oUCHandler = new AbwesenheitsartHandler();
			}
				break;
			case QueryParameters.UC_ID_DSGVOTEXT: {
				oUCHandler = new DsgvotextHandler();
			}
				break;
			case QueryParameters.UC_ID_STANDORT: {
				oUCHandler = new StandortHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERKLASSE: {
				oUCHandler = new PartnerklasseHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNG: {
				oUCHandler = new BestellungHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLPOSITION: {
				oUCHandler = new Bestellpositionhandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERKOMMUNIKATION: {
				oUCHandler = new PartnerkommunikationHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERANTEN: {
				oUCHandler = new LieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_ANSPRECHPARTNERFUNKTION: {
				oUCHandler = new AnsprechpartnerfunktionHandler();
			}
				break;
			case QueryParameters.UC_ID_KOLLEKTIV: {
				oUCHandler = new KollektivHandler();
			}
				break;
			case QueryParameters.UC_ID_KOLLEKTIVUESTD: {
				oUCHandler = new KollektivuestdHandler();
			}
				break;
			case QueryParameters.UC_ID_PASSIVEREISE: {
				oUCHandler = new PassivereiseHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALTERMINAL: {
				oUCHandler = new PersonalterminalHandler();
			}
				break;
			case QueryParameters.UC_ID_KOLLEKTIVUESTD50: {
				oUCHandler = new Kollektivuestd50Handler();
			}
				break;
			case QueryParameters.UC_ID_KOLLEKTIVUESTDBVA: {
				oUCHandler = new KollektivuestdBVAHandler();
			}
				break;
			case QueryParameters.UC_ID_BERUF: {
				oUCHandler = new BerufHandler();
			}
				break;
			case QueryParameters.UC_ID_ANWESENHEITSBESTAETIGUNG: {
				oUCHandler = new AnwesenheitsbestaetigungHandler();
			}
				break;
			case QueryParameters.UC_ID_PENDLERPAUSCHALE: {
				oUCHandler = new PendlerpauschaleHandler();
			}
				break;
			case QueryParameters.UC_ID_LOHNGRUPPE: {
				oUCHandler = new LohngruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTERLEDIGUNGSGRUND: {
				oUCHandler = new ProjekterledigungsgrundHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEERLEDIGUNGSGRUND: {
				oUCHandler = new AnfrageerledigungsgrundHandler();
			}
				break;
			case QueryParameters.UC_ID_RELIGION: {
				oUCHandler = new ReligionHandler();
			}
				break;
			case QueryParameters.UC_ID_EINTRITTAUSTRITT: {
				oUCHandler = new EintrittaustrittHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALZEITEN: {
				oUCHandler = new PersonalzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALANGEHOERIGE: {
				oUCHandler = new PersonalangehoerigeHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELLISTE: {
				oUCHandler = new ArtikellisteHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERCOCKPIT_ARTIKEL: {
				oUCHandler = new LagercockpitArtikelHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERCOCKPIT_NICHTLAGERBEWIRTSCHAFTETE_ARTIKEL: {
				oUCHandler = new LagercockpitNichtLagerbewirtschafteteArtikelHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERCOCKPIT_LOSSOLLMATERIAL: {
				oUCHandler = new LagercockpitLossollmaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_FUNKTION: {
				oUCHandler = new FunktionHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSZIEL: {
				oUCHandler = new ZahlungszielHandler();
			}
				break;
			case QueryParameters.UC_ID_LAND: {
				oUCHandler = new LandHandler();
			}
				break;
			case QueryParameters.UC_ID_VKFORTSCHRITT: {
				oUCHandler = new VkfortschrittHandler();
			}
				break;
			case QueryParameters.UC_ID_VERPACKUNGSMITTEL: {
				oUCHandler = new VerpackungsmittelHandler();
			}
				break;
			case QueryParameters.UC_ID_WARTUNGSSCHRITTE: {
				oUCHandler = new WartungsschritteHandler();
			}
				break;
			case QueryParameters.UC_ID_SERIENNUMMERNCHARGENNUMMERNAUFLAGER: {
				oUCHandler = new SeriennummernchargennummernAufLagerHandler();
			}
				break;
			case QueryParameters.UC_ID_SNRCHNRFUERREKLAMATION: {
				oUCHandler = new SnrChnrFuerReklamationHandler();
			}
				break;
			case QueryParameters.UC_ID_ALLESNRCHNR: {
				oUCHandler = new AlleSnrChnrHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELLIEFERANTSTAFFEL: {
				oUCHandler = new ArtikellieferantstaffelHandler();
			}
				break;
			case QueryParameters.UC_ID_ORT: {
				oUCHandler = new OrtHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITMODELL: {
				oUCHandler = new ZeitmodellHandler();
			}
				break;
			case QueryParameters.UC_ID_VERRECHNUNGSMODELL: {
				oUCHandler = new VerrechnungsmodellHandler();
			}
				break;
			case QueryParameters.UC_ID_VERRECHNUNGSMODELLTAG: {
				oUCHandler = new VerrechnungsmodelltagHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINEZM: {
				oUCHandler = new MaschinenzmHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINELEISTUNGSFAKTOR: {
				oUCHandler = new MaschineleistungsfaktorHandler();
			}
				break;
			case QueryParameters.UC_ID_ALTERNATIVMASCHINE: {
				oUCHandler = new AlternativmaschineHandler();
			}
				break;
			case QueryParameters.UC_ID_WEBLIEFERANT: {
				oUCHandler = new WeblieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_EKWEBLIEFERANT: {
				oUCHandler = new EkweblieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERPLATZ: { // gibt es nicht
				oUCHandler = new LagerplatzHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELLAGERPLAETZE: {
				oUCHandler = new ArtikellagerplaetzeHandler();
			}
				break;
			case QueryParameters.UC_ID_MANDANT: {
				oUCHandler = new MandantHandler();
			}
				break;
			case QueryParameters.UC_ID_CHARGENAUFLAGER: {
				oUCHandler = new ChargenAufLagerHandler();
			}
				break;
			case QueryParameters.UC_ID_PRUEFKOMBINATION: {
				oUCHandler = new PruefkombinationHandler();
			}
				break;
			case QueryParameters.UC_ID_STKLPRUEFPLAN: {
				oUCHandler = new StklpruefplanHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSPRUEFPLAN: {
				oUCHandler = new LospruefplanHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITMODELLTAG: {
				oUCHandler = new ZeitmodelltagHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINENZMTAGESART: {
				oUCHandler = new MaschinenzmtagesartHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALZEITMODELL: {
				oUCHandler = new PersonalzeitmodellHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALFAHRZEUG: {
				oUCHandler = new PersonalfahrzeugHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINEMASCHINENZM: {
				oUCHandler = new MaschinemaschinenzmHandler();
			}
				break;
			case QueryParameters.UC_ID_SCHICHTZEITMODELL: {
				oUCHandler = new SchichtzeitmodellHandler();
			}
				break;
			case QueryParameters.UC_ID_GEBINDE: {
				oUCHandler = new GebindeHandler();
			}
				break;
			case QueryParameters.UC_ID_BETRIEBSKALENDER: {
				oUCHandler = new BetriebskalenderHandler();
			}
				break;
			case QueryParameters.UC_ID_FEIERTAG: {
				oUCHandler = new FeiertagHandler();
			}
				break;
			case QueryParameters.UC_ID_BELEGART: {
				oUCHandler = new BelegartHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINUEBERSICHT: {
				oUCHandler = new LieferscheinUebersichtHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINUMSATZ: {
				oUCHandler = new LieferscheinUmsatzHandler();
			}
				break;
			case QueryParameters.UC_ID_SONDERTAETIGKEIT: {
				oUCHandler = new SondertaetigkeitHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITDATEN: {
				oUCHandler = new ZeitdatenHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITINFO: {
				oUCHandler = new ZeitinfoHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITBUCHUNGEN: {
				oUCHandler = new ZeitbuchungenHandler();
			}
				break;
			case QueryParameters.UC_ID_DATEIVERWEIS: {
				oUCHandler = new DateiverweisHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITDATEN_VON_BIS: {
				oUCHandler = new ZeitdatenVonBisHandler();
			}
				break;
			case QueryParameters.UC_ID_GEWERK: {
				oUCHandler = new GewerkHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITDATENGUTSCHLECHT: {
				oUCHandler = new ZeitdatenGutSchlechtHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINENZEITDATENGUTSCHLECHT: {
				oUCHandler = new MaschinenzeitdatenGutSchlechtHandler();
			}
				break;
			case QueryParameters.UC_ID_SPEDITEUR: {
				oUCHandler = new SpediteurHandler();
			}
				break;
			case QueryParameters.UC_ID_URLAUBSANSPRUCH: {
				oUCHandler = new UrlaubsanspruchHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERART: {
				oUCHandler = new LieferartHandler();
			}
				break;
			case QueryParameters.UC_ID_KENNUNG: {
				oUCHandler = new KennungHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDEKENNUNG: {
				oUCHandler = new KundekennungHandler();
			}
				break;
			case QueryParameters.UC_ID_VERSANDAUFTRAG: {
				oUCHandler = new VersandauftragHandler();
			}
				break;
			case QueryParameters.UC_ID_SONDERZEITEN: {
				oUCHandler = new SonderzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITGUTSCHRIFT: {
				oUCHandler = new ZeitgutschriftHandler();
			}
				break;
			case QueryParameters.UC_ID_STUNDENABRECHNUNG: {
				oUCHandler = new StundenabrechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_ABRECHNUNGSVORSCHLAG: {
				oUCHandler = new AbrechnungsvorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_VERSCHLEISSTEIL: {
				oUCHandler = new VerschleissteilHandler();
			}
				break;
			case QueryParameters.UC_ID_GLEITZEITSALDO: {
				oUCHandler = new GleitzeitsaldoHandler();
			}
				break;
			case QueryParameters.UC_ID_UEBERTRAGBVA: {
				oUCHandler = new UebertragBVAHandler();
			}
				break;
			case QueryParameters.UC_ID_AUSZAHLUNGBVA: {
				oUCHandler = new AuszahlungBVAHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALGEHALT: {
				oUCHandler = new PersonalgehaltHandler();
			}
				break;
			case QueryParameters.UC_ID_TAGESART: {
				oUCHandler = new TagesartHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITPLAN: {
				oUCHandler = new ZeitplanHandler();
			}
				break;
			case QueryParameters.UC_ID_BSZAHLUNGSPLAN: {
				oUCHandler = new BSZahlungsplanHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITPLANTYP: {
				oUCHandler = new ZeitplantypHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITPLANTYPDETAIL: {
				oUCHandler = new ZeitplantypdetailHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSPLAN: {
				oUCHandler = new ZahlungsplanHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSPLANMEILENSTEIN: {
				oUCHandler = new ZahlungsplanmeilensteinHandler();
			}
				break;
			case QueryParameters.UC_ID_APKOMMENTAR: {
				oUCHandler = new ApkommentarHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITMODELLTAGPAUSE: {
				oUCHandler = new ZeitmodelltagpauseHandler();
			}
				break;
			case QueryParameters.UC_ID_BENUZTER: {
				oUCHandler = new BenutzerHandler();
			}
				break;
			case QueryParameters.UC_ID_VERLEIH: {
				oUCHandler = new VerleihHandler();
			}
				break;
			case QueryParameters.UC_ID_SYSTEMROLLE: {
				oUCHandler = new SystemrolleHandler();
			}
				break;
			case QueryParameters.UC_ID_BENUTZERMANDANTSYSTEMROLLE: {
				oUCHandler = new BenutzermandantsystemrolleHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERBANK: {
				oUCHandler = new PartnerBankHandler();
			}
				break;
			case QueryParameters.UC_ID_KOSTENTRAEGER: {
				oUCHandler = new KostentraegerHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNGWARENEINGANG: {
				oUCHandler = new BestellungwareneingangHandler();
			}
				break;
			case QueryParameters.UC_ID_EINHEIT: {
				oUCHandler = new EinheitHandler();
			}
				break;
			case QueryParameters.UC_ID_STATUS: {
				oUCHandler = new StatusHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN: {
				oUCHandler = new BestellungWEPEingangHandler();
			}
				break;
			case QueryParameters.UC_ID_WAREZWISCHENMANDANTEN: {
				oUCHandler = new WarezwischenmandantenHandler();
			}
				break;
			case QueryParameters.UC_ID_WE_EINER_RECHNUNGSADRESSE: {
				oUCHandler = new WEsEinerRechnungsadresseHandler();
			}
				break;
			case QueryParameters.UC_ID_WEP_EINER_RECHNUNGSADRESSE: {
				oUCHandler = new WEPsEinerRechnungsadresseHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINTEXT: {
				oUCHandler = new LieferscheintextHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGPOSITIONINLIEFERSCHEIN: {
				oUCHandler = new AuftragpositionInLieferscheinHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGZEITEN: {
				oUCHandler = new AuftragzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTZEITEN: {
				oUCHandler = new AngebotzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTAUFTRAG: {
				oUCHandler = new AngebotAuftragHandler();
			}
				break;
			case QueryParameters.UC_ID_KOMMENTARIMPORT: {
				oUCHandler = new KommentarimportHandler();
			}
				break;
			case QueryParameters.UC_ID_MEDIASTANDARD: {
				oUCHandler = new MediastandardHandler();
			}
				break;
			case QueryParameters.UC_ID_POSITIONSART: {
				oUCHandler = new PositionsartHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNGART: {
				oUCHandler = new EingangsrechnungartHandler();
			}
				break;
			case QueryParameters.UC_ID_HALLE: {
				oUCHandler = new HalleHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFSCHLAG: {
				oUCHandler = new AufschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_INSERAT: {
				oUCHandler = new InseratHandler();
			}
				break;
			case QueryParameters.UC_ID_INSERATE_OHNE_ER: {
				oUCHandler = new InserateOhneERHandler();
			}
				break;
			case QueryParameters.UC_ID_INSERATRECHNUNG: {
				oUCHandler = new InseratrechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_INSERATER: {
				oUCHandler = new InseraterHandler();
			}
				break;
			case QueryParameters.UC_ID_INSERATARTIKEL: {
				oUCHandler = new InseratartikelHandler();
			}
				break;
			case QueryParameters.UC_ID_GERAETETYP: {
				oUCHandler = new GeraetetypHandler();
			}
				break;
			case QueryParameters.UC_ID_GERAET: {
				oUCHandler = new GeraetHandler();
			}
				break;
			case QueryParameters.UC_ID_ISMASCHINE: {
				oUCHandler = new IsmaschineHandler();
			}
				break;
			case QueryParameters.UC_ID_ANLAGE: {
				oUCHandler = new AnlageHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNGSSTATUS: {
				oUCHandler = new EingangsrechnungsstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDE_UMSATZSTATISTIK: {
				oUCHandler = new KundeUmsatzHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGSTATUS: {
				oUCHandler = new RechnungstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGART: {
				oUCHandler = new RechnungsartHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFTGRUND: {
				oUCHandler = new GutschriftgrundHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGPOSITIONSART: {
				oUCHandler = new RechnungpositionsartHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFTPOSITIONSART: {
				oUCHandler = new GutschriftpositionsartHandler();
			}
				break;
			case QueryParameters.UC_ID_PROFORMARECHNUNGPOSITIONSART: {
				oUCHandler = new ProformarechnungpositionsartHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGTYP: {
				oUCHandler = new RechnungtypHandler();
			}
				break;
			case QueryParameters.UC_ID_DOKUMENTENLINK: {
				oUCHandler = new DokumentenlinkHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSART: {
				oUCHandler = new ZahlungsartHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELBESTELLT: { // noch aktuell? wird
				// nicht benutzt
				oUCHandler = new ArtikelbestelltHandler();
			}
				break;
			case QueryParameters.UC_ID_MEDIAART: {
				oUCHandler = new MediaartHandler();
			}
				break;
			case QueryParameters.UC_ID_FASESSION: {
				oUCHandler = new FasessionHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGTEXT: {
				oUCHandler = new AuftragtextHandler();
			}
				break;
			case QueryParameters.UC_ID_MWSTSATZ: {
				oUCHandler = new MwstsatzHandler();
			}
				break;
			case QueryParameters.UC_ID_MWSTSATZBEZ: {
				oUCHandler = new MwstsatzbezHandler();
			}
				break;
			case QueryParameters.UC_ID_VORSCHLAGSTEXT: {
				oUCHandler = new VorschlagstextHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGTEXT: {
				oUCHandler = new RechnungtextHandler();
			}
				break;
			case QueryParameters.UC_ID_WARENBEWEGUNGEN: {
				oUCHandler = new WarenbewegungenHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNGTEXT: {
				oUCHandler = new EingangsrechnungtextHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNGRUPPE: {
				oUCHandler = new MahngruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_GUTSCHRIFTTEXT: {
				oUCHandler = new GutschrifttextHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNTEXT: {
				oUCHandler = new MahntextHandler();
			}
				break;
			case QueryParameters.UC_ID_WAEHRUNG: {
				oUCHandler = new WaehrungHandler();
			}
				break;
			case QueryParameters.UC_ID_WECHSELKURS: {
				oUCHandler = new WechselkursHandler();
			}
				break;
			case QueryParameters.UC_ID_OFFENEWEPOS: {
				oUCHandler = new OffeneWEPosHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNSTUFE: {
				oUCHandler = new MahnstufeHandler();
			}
				break;
			case QueryParameters.UC_ID_MAHNSPESEN: {
				oUCHandler = new MahnspesenHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTARCHIV: {
				oUCHandler = new NachrichtarchivHandler();
			}
				break;
			case QueryParameters.UC_ID_STKLPARAMETER: {
				oUCHandler = new StklparameterHandler();
			}
				break;
			case QueryParameters.UC_ID_ANREDE: {
				oUCHandler = new AnredeHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERART: {
				oUCHandler = new PartnerartHandler();
			}
				break;
			case QueryParameters.UC_ID_IMPORTDEF: {
				oUCHandler = new ImportdefHandler();
			}
				break;
			case QueryParameters.UC_ID_KOMMUNIKATIONSART: {
				oUCHandler = new KommunikationsartHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNGTEXT: {
				oUCHandler = new BestellungtextHandler();
			}
				break;
			case QueryParameters.UC_ID_PARAMETERANWENDER: {
				oUCHandler = new ParameteranwenderHandler();
			}
				break;
			case QueryParameters.UC_ID_PARAMETERMANDANT: {
				oUCHandler = new ParametermandantHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMAPARAMETER: {
				oUCHandler = new HVMAParameterHandler();
			}
				break;
			case QueryParameters.UC_ID_PARAMETERMANDANTGUELTIGAB: {
				oUCHandler = new ParametermandantgueltigabHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGE: {
				oUCHandler = new AnfrageHandler();
			}
				break;
			case QueryParameters.UC_ID_BEREICH: {
				oUCHandler = new BereichHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGETEXT: {
				oUCHandler = new AnfragetextHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEPOSITION: {
				oUCHandler = new AnfragepositionHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNGSTATUS: {
				oUCHandler = new BestellungstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEPOSITIONLIEFERDATEN: {
				oUCHandler = new AnfragepositionlieferdatenHandler();
			}
				break;
			case QueryParameters.UC_ID_ROLLERECHT: {
				oUCHandler = new RollerechtHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHT: {
				oUCHandler = new RechtHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLVORSCHLAG: {
				oUCHandler = new BestellvorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEVORSCHLAG: {
				oUCHandler = new AnfragevorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_AUSLIEFERVORSCHLAG: {
				oUCHandler = new AusliefervorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLVORSCHLAGALLETERMINE: {
				oUCHandler = new BestellvorschlagAlleTermineHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOT: {
				oUCHandler = new AngebotHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELZUSCHLAG: {
				oUCHandler = new ArtikelzuschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_GERAETEHISTORIE: {
				oUCHandler = new GeraetehistorieHandler();
			}
				break;
			case QueryParameters.UC_ID_GERAETEHISTORIE_MIT_GERAET: {
				oUCHandler = new GeraetehistorieMitGeraetHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTTEXT: {
				oUCHandler = new AngebottextHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTART: {
				oUCHandler = new AngebotartHandler();
			}
				break;
			case QueryParameters.UC_ID_PRUEFART: {
				oUCHandler = new PruefartHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTERLEDIGUNGSGRUND: {
				oUCHandler = new AngeboterledigungsgrundHandler();
			}
				break;
			case QueryParameters.UC_ID_INVENTUR: {
				oUCHandler = new InventurHandler();
			}
				break;
			case QueryParameters.UC_ID_INVENTURLISTE: {
				oUCHandler = new InventurlisteHandler();
			}
				break;
			case QueryParameters.UC_ID_PATERNOSTER: {
				oUCHandler = new PaternosterHandler();
			}
				break;
			case QueryParameters.UC_ID_WEBSHOP: {
				oUCHandler = new WebshopHandler();
			}
				break;
			case QueryParameters.UC_ID_INVENTURPROTOKOLL: {
				oUCHandler = new InventurprotokollHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTPOSITION: {
				oUCHandler = new AngebotpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_LOS: {
				oUCHandler = new LosHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSE_ERLEDIGEN: {
				oUCHandler = new LoseErledigenHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSKLASSE: {
				oUCHandler = new LosklasseHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSBEREICH: {
				oUCHandler = new LosbereichHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSSTATUS: {
				oUCHandler = new LosstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_THEMA: {
				oUCHandler = new ThemaHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMALIZENZ: {
				oUCHandler = new HvmalizenzHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMABENUTZER: {
				oUCHandler = new HvmabenutzerHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMABENUTZERPARAMETER: {
				oUCHandler = new HvambenutzerparameterHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMAROLLE: {
				oUCHandler = new HvmarolleHandler();
			}
				break;
			case QueryParameters.UC_ID_HVMARECHT: {
				oUCHandler = new HvmarechtHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIEKLSHOPGRUPPE: {
				oUCHandler = new ArtikelshopgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_VKPREISBASIS: {
				oUCHandler = new VkpfartikelverkaufspreisbasisHandler();
			}
				break;
			case QueryParameters.UC_ID_VKEINZELPREIS: {
				oUCHandler = new VkpfEinzelpreisHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLUNGART: {
				oUCHandler = new BestellungartHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGART: {
				oUCHandler = new AuftragartHandler();
			}
				break;
			case QueryParameters.UC_ID_FORECASTART: {
				oUCHandler = new ForecastartHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTENART: {
				oUCHandler = new NachrichtenartHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTENGRUPPE: {
				oUCHandler = new NachrichtengruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTENVERLAUF: {
				oUCHandler = new NachrichtenverlaufHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTENGRUPPETEILNEHMER: {
				oUCHandler = new NachrichtengruppeteilnehmerHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTENABO: {
				oUCHandler = new NachrichtenaboHandler();
			}
				break;
			case QueryParameters.UC_ID_FCLIEFERADRESSE: {
				oUCHandler = new FclieferadresseHandler();
			}
				break;
			case QueryParameters.UC_ID_LINIENABRUF: {
				oUCHandler = new LinienabrufHandler();
			}
				break;
			case QueryParameters.UC_ID_MEILENSTEIN: {
				oUCHandler = new MeilensteinHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINART: {
				oUCHandler = new LieferscheinartHandler();
			}
				break;
			case QueryParameters.UC_ID_BANKKONTO: {
				oUCHandler = new BankkontoHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTEPOSITION: {
				oUCHandler = new StuecklistepositionHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFTART: {
				oUCHandler = new StuecklisteeigenschaftartHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFT: {
				oUCHandler = new StuecklisteeigenschaftHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTEARBEITSPLAN: {
				oUCHandler = new StuecklistearbeitsplanHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKLARBEITSPLAN: {
				oUCHandler = new AgstklarbeitsplanHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTE: {
				oUCHandler = new StuecklisteHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTE_MIT_MANDANT: {
				oUCHandler = new StuecklisteHandlerMitMandant();
			}
				break;
			case QueryParameters.UC_ID_ISKATEGORIE: {
				oUCHandler = new IskategorieHandler();
			}
				break;
			case QueryParameters.UC_ID_WARTUNGSLISTE: {
				oUCHandler = new WartungslisteHandler();
			}
				break;
			case QueryParameters.UC_ID_STUECKLISTEMONTAGEART: {
				oUCHandler = new MontageartHandler();
			}
				break;
			case QueryParameters.UC_ID_POSERSATZ: {
				oUCHandler = new PosersatzHandler();
			}
				break;
			case QueryParameters.UC_ID_BEREITSCHAFTART: {
				oUCHandler = new BereitschaftartHandler();
			}
				break;
			case QueryParameters.UC_ID_SCHICHTZUSCHLAG: {
				oUCHandler = new SchichtzuschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_BEAUSKUNFTUNG: {
				oUCHandler = new BeauskunftungHandler();
			}
				break;
			case QueryParameters.UC_ID_SCHICHTZEITEN: {
				oUCHandler = new SchichtzeitHandler();
			}
				break;
			case QueryParameters.UC_ID_SCHICHT: {
				oUCHandler = new SchichtHandler();
			}
				break;
			case QueryParameters.UC_ID_BEREITSCHAFT: {
				oUCHandler = new BereitschaftHandler();
			}
				break;
			case QueryParameters.UC_ID_BEREITSCHAFTTAG: {
				oUCHandler = new BereitschafttagHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLPOSITIONSICHTRAHMEN: {
				oUCHandler = new BestellpositionSichtRahmenHandler();
			}
				break;
			case QueryParameters.UC_ID_MONATSSTATISTIK_LIEFERANT: {
				oUCHandler = new LieferantMonatsstatistikHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTPOSITIONART: {
				oUCHandler = new AngebotpositionartHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERSCHEINPOSITIONART: {
				oUCHandler = new LieferscheinpositionartHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEPOSITIONART: {
				oUCHandler = new AnfragepositionartHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGPOSITIONART: {
				oUCHandler = new AuftragpositionartHandler();
			}
				break;
			case QueryParameters.UC_ID_SICHTLIEFERTERMINE: {
				oUCHandler = new BestellungSichtLieferTermineHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERGRUPPEN: {
				oUCHandler = new LiefergruppenHandler();
			}
				break;
			case QueryParameters.UC_ID_LAENDERART: {
				oUCHandler = new LaenderartHandler();
			}
				break;
			case QueryParameters.UC_ID_LFLIEFERGRUPPEN: {
				oUCHandler = new LFLiefergruppenHandler();
			}
				break;
			case QueryParameters.UC_ID_ANFRAGEART: {
				oUCHandler = new AnfrageartHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSSOLLMATERIAL: {
				oUCHandler = new LossollmaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_TRUMPFMATERIAL: {
				oUCHandler = new TrumpfmaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSSOLLMATERIAL_TRUMPF: {
				oUCHandler = new LossollmaterialTrumpfHandler();
			}
				break;
			case QueryParameters.UC_ID_BEDARFSUEBERNAHME: {
				oUCHandler = new BedarfsuebernahmeHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSSOLLARBEITSPLAN: {
				oUCHandler = new LossollarbeitsplanHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSLAGERENTNAHME: {
				oUCHandler = new LoslagerentnahmeHandler();
			}
				break;
			case QueryParameters.UC_ID_STKLAGERENTNAHME: {
				oUCHandler = new StklagerentnahmeHandler();
			}
				break;
			case QueryParameters.UC_ID_ZULAGE: {
				oUCHandler = new ZulageHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELZULAGE: {
				oUCHandler = new ArtikelzulageHandler();
			}
				break;
			case QueryParameters.UC_ID_BESTELLPOSITIONART: {
				oUCHandler = new BestellpositionartHandler();
			}
				break;
			case QueryParameters.UC_ID_FEHLMENGE: {
				oUCHandler = new FehlmengeHandler();
			}
				break;
			case QueryParameters.UC_ID_VERFUEGBARKEIT: {
				oUCHandler = new VerfuegbarkeitHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERCOCKPIT_FEHLMENGE: {
				oUCHandler = new LagercockpitFehlmengeHandler();
			}
				break;
			case QueryParameters.UC_ID_BEGRUENDUNG: {
				oUCHandler = new BegruendungHandler();
			}
				break;
			case QueryParameters.UC_ID_VERKETTET: {
				oUCHandler = new VerkettetHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSTECHNIKER: {
				oUCHandler = new LostechnikerHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTTECHNIKER: {
				oUCHandler = new ProjekttechnikerHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTGRUPPE: {
				oUCHandler = new ProjektgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTTAETIGKEIT: {
				oUCHandler = new ProjekttaetigkeitHandler();
			}
				break;
			case QueryParameters.UC_ID_FEHLMENGEAUFLOESEN: {
				oUCHandler = new FehlmengeReservierungAufloesenHandler();
			}
				break;
			case QueryParameters.UC_ID_LFLIEFERGRUPPENONELF: {
				oUCHandler = new LFLiefergruppenOneLFHandler();
			}
				break;
			case QueryParameters.UC_ID_EINHEITENKONVERTIERUNG: {
				oUCHandler = new EinheitKonvertierungHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSLOSKLASSE: {
				oUCHandler = new LoslosklasseHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSABLIEFERUNG: {
				oUCHandler = new LosablieferungHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERANTENOPTIMIEREN: {
				oUCHandler = new LieferantenoptimierenHandler();
			}
				break;
			case QueryParameters.UC_ID_BESMAHNTEXT: {
				oUCHandler = new BSMahntextHandler();
			}
				break;
			case QueryParameters.UC_ID_BESMAHNSTUFE: {
				oUCHandler = new BSMahnstufeHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGBEGRUENDUNG: {
				oUCHandler = new AuftragbegruendungHandler();
			}
				break;
			case QueryParameters.UC_ID_BESMAHNLAUF: {
				oUCHandler = new BSMahnlaufHandler();
			}
				break;
			case QueryParameters.UC_ID_LOHNART: {
				oUCHandler = new LohnartHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLTAG: {
				oUCHandler = new ZahltagHandler();
			}
				break;
			case QueryParameters.UC_ID_LOHNSTUNDENART: {
				oUCHandler = new LohnstundenartHandler();
			}
				break;
			case QueryParameters.UC_ID_BESMAHNUNG: {
				oUCHandler = new BSMahnungHandler();
			}
				break;
			case QueryParameters.UC_ID_HISTORYART: {
				oUCHandler = new HistoryartHandler();
			}
				break;
			case QueryParameters.UC_ID_ANGEBOTUEBERSICHT: {
				oUCHandler = new AngebotUebersichtHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSNACHKALKULATION: {
				oUCHandler = new LosNachkalkulationHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELKOMMENTAR: {
				oUCHandler = new ArtikelkommentarHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERKOMMENTAR: {
				oUCHandler = new PartnerkommentarHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELKOMMENTARSUCHE: {
				oUCHandler = new ArtikelkommentarSucheHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALGRUPPE: {
				oUCHandler = new PersonalgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALGRUPPEKOSTEN: {
				oUCHandler = new PersonalgruppekostenHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELKOMMENTARART: {
				oUCHandler = new ArtikelkommentarartHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERKOMMENTARART: {
				oUCHandler = new PartnerkommentarartHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSZEITEN: {
				oUCHandler = new LoszeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_SELEKTION: {
				oUCHandler = new SelektionHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERSELEKTION: {
				oUCHandler = new PASelektionHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERKURZBRIEF: {
				oUCHandler = new PAKurzbriefHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGPOSITIONSICHTRAHMEN: {
				oUCHandler = new AuftragpositionSichtRahmenHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNERSERIENBRIEF: {
				oUCHandler = new SerienbriefHandler();
			}
				break;
			case QueryParameters.UC_ID_NEWSLETTERGRUND: {
				oUCHandler = new NewslettergrundHandler();
			}
				break;
			case QueryParameters.UC_ID_INTERNEBESTELLUNG: {
				oUCHandler = new InternebestellungHandler();
			}
				break;
			case QueryParameters.UC_ID_SERIENBRIEFSELEKTION: {
				oUCHandler = new SerienbriefselektionHandler();
			}
				break;
			case QueryParameters.UC_ID_SERIENBRIEFSELEKTIONNEGATIV: {
				oUCHandler = new SerienbriefselektionnegativHandler();
			}
				break;
			case QueryParameters.UC_ID_BEWEGUNGSVORSCHAU2: {
				oUCHandler = new BewegungsvorschauHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKL: {
				oUCHandler = new AgstklHandler();
			}
				break;
			case QueryParameters.UC_ID_EKGRUPPE: {
				oUCHandler = new EkgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_EKGRUPPELIEFERANT: {
				oUCHandler = new EkgruppelieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_EKAGLIEFERANT: {
				oUCHandler = new EkaglieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_POSITIONLIEFERANT: {
				oUCHandler = new PositionlieferantHandler();
			}
				break;
			case QueryParameters.UC_ID_POSITIONLIEFERANT_OPTIMIEREN: {
				oUCHandler = new PositionlieferantOptimierenHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKLPOSITION: {
				oUCHandler = new AgstklpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKLMATERIAL: {
				oUCHandler = new AgstklmaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_INVENTURSTAND: {
				oUCHandler = new InventurstandHandler();
			}
				break;
			case QueryParameters.UC_ID_EXPORTDATEN: {
				oUCHandler = new ExportdatenHandler();
			}
				break;
			case QueryParameters.UC_ID_EXPORTLAUF: {
				oUCHandler = new ExportlaufHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGKONTIERUNG: {
				oUCHandler = new RechnungkontierungHandler();
			}
				break;
			case QueryParameters.UC_ID_PANEL: {
				oUCHandler = new PanelHandler();
			}
				break;
			case QueryParameters.UC_ID_PANELBESCHREIBUNG: {
				oUCHandler = new PanelbeschreibungHandler();
			}
				break;
			case QueryParameters.UC_ID_PANELSPERREN: {
				oUCHandler = new PanelsperrenHandler();
			}
				break;
			case QueryParameters.UC_ID_VKPFSTAFFELMENGE: {
				oUCHandler = new VkpfStaffelmengeHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDESOKO: {
				oUCHandler = new KundesokoHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELSOKO: {
				oUCHandler = new ArtikelSokoHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELSOKOMENGENSTAFFEL: {
				oUCHandler = new ArtikelSokomengenstaffelHandler();
			}
				break;
			case QueryParameters.UC_ID_NACHRICHTART: {
				oUCHandler = new NachrichtartHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDESOKOMENGENSTAFFEL: {
				oUCHandler = new KundesokomengenstaffelHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINE: {
				oUCHandler = new MaschineHandler();

			}
				break;
			case QueryParameters.UC_ID_ZEITSTIFT: {
				oUCHandler = new ZeitstiftHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINENKOSTEN: {
				oUCHandler = new MaschinenkostenHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKT: {
				oUCHandler = new ProjektHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKT_SCHNELL: {
				oUCHandler = new ProjektHandlerSchnell();
			}
				break;
			case QueryParameters.UC_ID_PROJEKT_QUEUE: {
				oUCHandler = new ProjektQueueHandler();
			}
				break;
			case QueryParameters.UC_ID_KATEGORIE: {
				oUCHandler = new KategorieHandler();
			}
				break;
			case QueryParameters.UC_ID_TYP: {
				oUCHandler = new ProjektTypHandler();
			}
				break;
			case QueryParameters.UC_ID_HISTORY: {
				oUCHandler = new HistoryHandler();
			}
				break;
			case QueryParameters.UC_ID_KONTAKTART: {
				oUCHandler = new KontaktartHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSCONTROLLER: {
				oUCHandler = new ZutrittscontrollerHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSKLASSE: {
				oUCHandler = new ZutrittsklasseHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSMODELL: {
				oUCHandler = new ZutrittsmodellHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSMODELLTAG: {
				oUCHandler = new ZutrittsmodelltagHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSMODELLTAGDETAIL: {
				oUCHandler = new ZutrittsmodelltagdetailHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSOBJEKT: {
				oUCHandler = new ZutrittsobjektHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTONLINECHECK: {
				oUCHandler = new ZutrittonlinecheckHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSOBJEKTVERWENDUNG: {
				oUCHandler = new ZutrittsobjektverwendungHandler();
			}
				break;
			case QueryParameters.UC_ID_THEMAROLLE: {
				oUCHandler = new ThemarolleHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSKLASSEOBJEKT: {
				oUCHandler = new ZutrittsklasseobjektHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALZUTRITTSKLASSE: {
				oUCHandler = new PersonalzutrittsklasseHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTSTATUS: {
				oUCHandler = new ProjektStatusHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTSLOG: {
				oUCHandler = new ZutrittslogHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUTRITTDAUEROFFEN: {
				oUCHandler = new ZutrittdaueroffenHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINENGRUPPE: {
				oUCHandler = new MaschinengruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALVERFUEGBARKEIT: {
				oUCHandler = new PersonalverfuegbarkeitHandler();
			}
				break;
			case QueryParameters.UC_ID_FARBCODE: {
				oUCHandler = new FarbcodeHandler();
			}
				break;
			case QueryParameters.UC_ID_BELEGARTDOKUMENT: {
				oUCHandler = new BelegartdokumentHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSVORSCHLAGLAUF: {
				oUCHandler = new ZahlungsvorschlaglaufHandler();
			}
				break;
			case QueryParameters.UC_ID_ZAHLUNGSVORSCHLAG: {
				oUCHandler = new ZahlungsvorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAEGE_EINES_LIEFERSCHEINS: {
				oUCHandler = new AuftraegeEinesLieferscheinsHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAEGE_EINER_RECHNUNG: {
				oUCHandler = new AuftraegeEinerRechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_REISE: {
				oUCHandler = new ReiseHandler();
			}
				break;
			case QueryParameters.UC_ID_REISESPESEN: {
				oUCHandler = new ReisespesenHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_REISEZEITEN: {
				oUCHandler = new EingangsrechnungReisezeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_STANDORTTECHNIKER: {
				oUCHandler = new StandorttechnikerHandler();
			}
				break;
			case QueryParameters.UC_ID_RECHNUNGPOSITIONSICHTAUFTRAG: {
				oUCHandler = new PositionenSichtAuftragHandler();
			}
				break;
			case QueryParameters.UC_ID_FAHRZEUG: {
				oUCHandler = new FahrzeugHandler();
			}
				break;
			case QueryParameters.UC_ID_FAHRZEUGKOSTEN: {
				oUCHandler = new FahrzeugkostenHandler();
			}
				break;
			case QueryParameters.UC_ID_KUNDENIDENTNUMMER: {
				oUCHandler = new KundenidentnummerHandler();
			}
				break;
			case QueryParameters.UC_ID_PERSONALFINGER: {
				oUCHandler = new PersonalfingerHandler();
			}
				break;
			case QueryParameters.UC_ID_FINGERART: {
				oUCHandler = new FingerartHandler();
			}
				break;
			case QueryParameters.UC_ID_LANDKFZKENNZEICHEN: {
				oUCHandler = new LandKfzKennzeichenHandler();
			}
				break;
			case QueryParameters.UC_ID_EXTRALISTE: {
				oUCHandler = new ExtralisteHandler();
			}
				break;
			case QueryParameters.UC_ID_REPORTVARIANTE: {
				oUCHandler = new ReportvarianteHandler();
			}
				break;
			case QueryParameters.UC_ID_PARAMETER: {
				oUCHandler = new ParameterHandler();
			}
				break;
			case QueryParameters.UC_ID_ARBEITSPLATZ: {
				oUCHandler = new ArbeitsplatzHandler();
			}
				break;
			case QueryParameters.UC_ID_ARBEITSPLATZPARAMETER: {
				oUCHandler = new ArbeitsplatzparameterHandler();
			}
				break;
			case QueryParameters.UC_ID_WARENVERKEHRSNUMMER: {
				oUCHandler = new WarenverkehrsnummerHandler();
			}
				break;
			case QueryParameters.UC_ID_EINGANGSRECHNUNG_WARENEINGANG: {
				oUCHandler = new EingangsrechnungWEHandler();
			}
				break;
			case QueryParameters.UC_ID_FERTIGUNGSGRUPPE: {
				oUCHandler = new FertigungsgruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_FERTIGUNGSGRUPPE_EINGESCHRAENKT: {
				oUCHandler = new FertigungsgruppeEingeschraenktHandler();
			}
			break;
			
			case QueryParameters.UC_ID_FERTIGUNGSGRUPPE_MANDANT: {
				oUCHandler = new FertigungsgruppeHandler(true);
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGDOKUMENT: {
				oUCHandler = new AuftragdokumentHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTZEITEN: {
				oUCHandler = new ProjektzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_FORECAST: {
				oUCHandler = new ForecastHandler();
			}
				break;
			case QueryParameters.UC_ID_FORECASTPOSITION: {
				oUCHandler = new ForecastpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_FORECASTAUFTRAG: {
				oUCHandler = new ForecastauftragHandler();
			}
				break;
			case QueryParameters.UC_ID_TELEFONZEITEN: {
				oUCHandler = new TelefonzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_PARTNER_TELEFONZEITEN: {
				oUCHandler = new PartnerTelefonzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_TELEFONZEITENTODO: {
				oUCHandler = new TelefonzeitenTodoHandler();
			}
				break;
			case QueryParameters.UC_ID_COCKPITTELEFONZEITEN: {
				oUCHandler = new CockpitTelefonzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_PROJEKTTELEFONZEITEN: {
				oUCHandler = new ProjektTelefonzeitenHandler();
			}
				break;
			case QueryParameters.UC_ID_DIAETEN: {
				oUCHandler = new DiaetenHandler();
			}
				break;
			case QueryParameters.UC_ID_DIAETENTAGESSATZ: {
				oUCHandler = new DiaetentagessatzHandler();
			}
				break;
			case QueryParameters.UC_ID_SPERREN: {
				oUCHandler = new SperrenHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELSPERREN: {
				oUCHandler = new ArtikelsperrenHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFTRAGSERIENNRN: {
				oUCHandler = new AuftragseriennrnHandler();
			}
				break;
			case QueryParameters.UC_ID_WIEDERHOLENDELOSE: {
				oUCHandler = new WiederholendeloseHandler();
			}
				break;
			case QueryParameters.UC_ID_OFFENE_AGS: {
				oUCHandler = new OffeneAgsHandler();
			}
				break;
			case QueryParameters.UC_ID_PRODUZIEREN: {
				oUCHandler = new ProduzierenHandler();
			}
				break;
			case QueryParameters.UC_ID_REKLAMATION: {
				oUCHandler = new ReklamationHandler();
			}
				break;
			case QueryParameters.UC_ID_FEHLER: {
				oUCHandler = new FehlerHandler();
			}
				break;
			case QueryParameters.UC_ID_FEHLERANGABE: {
				oUCHandler = new FehlerangabeHandler();
			}
				break;
			case QueryParameters.UC_ID_MASSNAHME: {
				oUCHandler = new MassnahmeHandler();
			}
				break;
			case QueryParameters.UC_ID_AUFNAHMEART: {
				oUCHandler = new AufnahmeartHandler();
			}
				break;
			case QueryParameters.UC_ID_AUTOMATIK: {
				oUCHandler = new AutomatikjobHandler();
			}
				break;
			case QueryParameters.UC_ID_EINKAUFSANGEBOT: {
				oUCHandler = new EinkaufsangebotHandler();
			}
				break;
			case QueryParameters.UC_ID_EINKAUFSANGEBOTPOSITIONEN: {
				oUCHandler = new EinkaufsangebotpositionHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSZUSATZSTATUS: {
				oUCHandler = new LoszusatzstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_AKQUISESTATUS: {
				oUCHandler = new AkquisestatusHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUSATZSTATUS: {
				oUCHandler = new ZusatzstatusHandler();
			}
				break;
			case QueryParameters.UC_ID_SCHWERE: {
				oUCHandler = new SchwereHandler();
			}
				break;
			case QueryParameters.UC_ID_ZUGEHOERIGE: {
				oUCHandler = new ZugehoerigeHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSISTMATERIAL: {
				oUCHandler = new LosistmaterialHandler();
			}
				break;
			case QueryParameters.UC_ID_DOKUMENTBELEGART: {
				oUCHandler = new DokumenteBelegartHandler();
			}
				break;
			case QueryParameters.UC_ID_DOKUMENTGRUPPE: {
				oUCHandler = new DokumenteGruppeHandler();
			}
				break;
			case QueryParameters.UC_ID_BEHANDLUNG: {
				oUCHandler = new BehandlungHandler();
			}
				break;
			case QueryParameters.UC_ID_LIEFERANTBEURTEILUNG: {
				oUCHandler = new LieferantbeurteilungHandler();
			}
				break;
			case QueryParameters.UC_ID_TERMINTREUE: {
				oUCHandler = new TermintreueHandler();
			}
				break;
			case QueryParameters.UC_ID_WIRKSAMKEIT: {
				oUCHandler = new WirksamkeitHandler();
			}
				break;
			case QueryParameters.UC_ID_REKLAMATIONBILD: {
				oUCHandler = new ReklamationbildHandler();
			}
				break;
			case QueryParameters.UC_ID_SPEISEPLAN: {
				oUCHandler = new SpeiseplanHandler();
			}
				break;
			case QueryParameters.UC_ID_INSTANDHALTUNG: {
				oUCHandler = new InstandhaltungHandler();
			}
				break;
			case QueryParameters.UC_ID_TAGESLOS: {
				oUCHandler = new TageslosHandler();
			}
				break;
			case QueryParameters.UC_ID_KASSAARTIKEL: {
				oUCHandler = new KassaartikelHandler();
			}
				break;
			case QueryParameters.UC_ID_EINKAUFSEAN: {
				oUCHandler = new EinkaufseanHandler();
			}
				break;
			case QueryParameters.UC_ID_KDC100LOG: {
				oUCHandler = new Kdc100logHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGER_MIT_MANDANT: {
				oUCHandler = new LagerMitMandantHandler();
			}
				break;
			case QueryParameters.UC_ID_WIEDERVORLAGE: {
				oUCHandler = new WiedervorlageHandler();
			}
				break;
			case QueryParameters.UC_ID_ANSPRECHPARTNERPARTNER: {
				oUCHandler = new AnsprechpartnerPartnerHandler();
			}
				break;
			case QueryParameters.UC_ID_KONTAKT: {
				oUCHandler = new KontaktHandler();
			}
				break;
			case QueryParameters.UC_ID_KUECHEUMRECHNUNG: {
				oUCHandler = new KuecheumrechnungHandler();
			}
				break;
			case QueryParameters.UC_ID_BEDIENERLAGER: {
				oUCHandler = new BedienerlagerHandler();
			}
				break;
			case QueryParameters.UC_ID_LOSGUTSCHLECHT: {
				oUCHandler = new LosgutschlechtHandler();
			}
				break;
			case QueryParameters.UC_ID_LAGERROLLE: {
				oUCHandler = new LagerrolleHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKLMENGENSTAFFEL: {
				oUCHandler = new AgstklmengenstaffelHandler();
			}
				break;
			case QueryParameters.UC_ID_AGSTKLMENGENSTAFFEL_SCHNELLERFASSUNG: {
				oUCHandler = new AgstklmengenstaffelSchnellerfassungHandler();
			}
				break;
			case QueryParameters.UC_ID_FERTIGUNGSGRUPPEROLLE: {
				oUCHandler = new FertigungsgrupperolleHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTGRUROLLE: {
				oUCHandler = new ArtgrurolleHandler();
			}
				break;
			case QueryParameters.UC_ID_MASCHINENZEITDATEN: {
				oUCHandler = new MaschinenzeitdatenHandler();
			}
				break;
			case QueryParameters.UC_ID_LOHNARTSTUNDENFAKTOR: {
				oUCHandler = new LohnartstundenfaktorHandler();
			}
				break;
			case QueryParameters.UC_ID_ZERTIFIKATART: {
				oUCHandler = new ZertifikatartHandler();
			}
				break;
			case QueryParameters.UC_ID_STEUERKATEGORIE: {
				oUCHandler = new SteuerkategorieHandler();
			}
				break;

			case QueryParameters.UC_ID_REVERSECHARGEART: {
				oUCHandler = new ReversechargeartHandler();
			}
				break;

			case QueryParameters.UC_ID_REACH: {
				oUCHandler = new ReachHandler();
			}
				break;
			case QueryParameters.UC_ID_LASEROBERFLAECHE: {
				oUCHandler = new LaseroberflaecheHandler();
			}
				break;
			case QueryParameters.UC_ID_VORZUG: {
				oUCHandler = new VorzugHandler();
			}
				break;
			case QueryParameters.UC_ID_KOMMDRUCKER: {
				oUCHandler = new KommdruckerHandler();
			}
				break;
			case QueryParameters.UC_ID_ALERGEN: {
				oUCHandler = new AlergenHandler();
			}
				break;
			case QueryParameters.UC_ID_ARTIKELALERGEN: {
				oUCHandler = new ArtikelalergenHandler();
			}
				break;
			case QueryParameters.UC_ID_ROHS: {
				oUCHandler = new RohsHandler();
			}
				break;
			case QueryParameters.UC_ID_VERRECHENBAR: {
				oUCHandler = new VerrechenbarHandler();
			}
				break;
			case QueryParameters.UC_ID_WERKZEUG: {
				oUCHandler = new WerkzeugHandler();
			}
				break;
			case QueryParameters.UC_ID_VERSCHLEISSTEILWERKZEUG: {
				oUCHandler = new VerschleissteilwerkzeugHandler();
			}
				break;
			case QueryParameters.UC_ID_AUTOMOTIVE: {
				oUCHandler = new AutomotiveHandler();
			}
				break;
			case QueryParameters.UC_ID_MEDICAL: {
				oUCHandler = new MedicalHandler();
			}
				break;
			case QueryParameters.UC_ID_WAFFENKALIBER: {
				oUCHandler = new WaffenkaliberHandler();
			}
				break;
				
			case QueryParameters.UC_ID_WAFFENTYP: {
				oUCHandler = new WaffentypHandler();
			}
				break;
			case QueryParameters.UC_ID_WAFFENTYPFEIN: {
				oUCHandler = new WaffentypFeinHandler();
			}
				break;
			case QueryParameters.UC_ID_WAFFENKATEGORIE: {
				oUCHandler = new WaffenkategorieHandler();
			}
				break;
			case QueryParameters.UC_ID_WAFFENZUSATZ: {
				oUCHandler = new WaffenzusatzHandler();
			}
				break;
			case QueryParameters.UC_ID_WAFFENAUSFUEHRUNG: {
				oUCHandler = new WaffenausfuehrungHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITABSCHLUSS: {
				oUCHandler = new ZeitabschlussHandler();
			}
				break;

			case QueryParameters.UC_ID_MEDIA_INBOX: {
				oUCHandler = new EmailMediaInboxHandler();
			}
				break;

			case QueryParameters.UC_ID_MEDIA_BELEG: {
				oUCHandler = new EmailMediaBelegHandler();
			}
				break;

			case QueryParameters.UC_ID_STUECKLISTESCRIPTART: {
				oUCHandler = new StuecklisteScriptartHandler();
			}
				break;

			case QueryParameters.UC_ID_WEBFINDCHIPS: {
				oUCHandler = new WebFindChipsHandler();
			}
				break;

			case QueryParameters.UC_ID_MMZ: {
				oUCHandler = new MmzHandler();
			}
				break;

			case QueryParameters.UC_ID_SEPAKONTOAUSZUG: {
				oUCHandler = new SepakontoauszugHandler();
			}
				break;

			case QueryParameters.UC_ID_LASTSCHRIFTVORSCHLAG: {
				oUCHandler = new LastschriftvorschlagHandler();
			}
				break;
			case QueryParameters.UC_ID_ZEITDATENPRUEFEN:
				oUCHandler = new ZeitdatenpruefenHandler();
				break;

			case QueryParameters.UC_ID_MAILPROPERTY:
				oUCHandler = new MailPropertyHandler();
				break;
				
			case QueryParameters.UC_ID_CONCURRENTUSER:
				oUCHandler = new TheConcurrentUserHandler();
			break;
				
			default: {
				oUCHandler = new UnknownUseCaseHandler(useCaseId);
			}
			}

			oUCHandler.setCurrentUser(theClientDto);

			oUCHandler.setUuid(uuid);
			
			oUCHandler.setUsecaseId(useCaseId);

			hashUsecases.put(uuid, oUCHandler);

			hashMapDesClients.put(useCaseId, hashUsecases);

			this.useCaseHandlers.put(theClientDto.getIDUser(), hashMapDesClients);

			// // SP 2015
			// myLogger.warn(String
			// .format("new UseCaseHandler: uuid = %s; useCaseId = %d; IDUser = %s; Login
			// Time = %s",
			// uuid, useCaseId, theClientDto.getIDUser(),
			// theClientDto.getDLoggedin().toString()));

		}
		oUCHandler.setCurrentUser(theClientDto);
		return oUCHandler;
	}

	@Remove
	public synchronized void cleanup(String uuid, Integer useCaseId, TheClientDto theClientDto) {

		iCleanupZaehler++;

		HashMap<Integer, HashMap<String, UseCaseHandler>> hashMapDesClients = useCaseHandlers
				.get(theClientDto.getIDUser());

		// Bei jedem 1000ten Aufruf die Liste nach verwaisten eintraegen
		// durchsuchen und diese loeschen
		if (iCleanupZaehler > 1000 && useCaseHandlers != null) {
			iCleanupZaehler = 0;
			Iterator<String> it = useCaseHandlers.keySet().iterator();

			while (it.hasNext()) {

				String idUser = it.next();

				HashMap<Integer, HashMap<String, UseCaseHandler>> temp = useCaseHandlers.get(idUser);

				Iterator<Integer> itUsecases = temp.keySet().iterator();

				Vector<String> vToRemove = new Vector<String>();

				while (itUsecases.hasNext()) {
					Integer useCaseKey = itUsecases.next();
					HashMap<String, UseCaseHandler> usecasesEinesclients = temp.get(useCaseKey);

					Iterator<String> itListen = usecasesEinesclients.keySet().iterator();
					while (itListen.hasNext()) {
						String key = itListen.next();
						UseCaseHandler uc = usecasesEinesclients.get(key);

						long lErstellung = uc.getTErstellung().getTime();

						// Nach 2 Tagen wird der Eintrag aus der Liste entfernt

						if ((lErstellung + (24 * 3600000 * 2)) < System.currentTimeMillis()) {
							vToRemove.add(key);

						}

					}

					// Nun alle entfernen
					for (int i = 0; i < vToRemove.size(); i++) {
						usecasesEinesclients.remove(vToRemove.get(i));
					}

					temp.put(useCaseKey, usecasesEinesclients);
				}

				useCaseHandlers.put(idUser, temp);

			}

		}

		// Alle Infos des Clients entfernen
		if (useCaseId == null && uuid == null && useCaseHandlers != null && hashMapDesClients != null) {
			if (useCaseHandlers.containsKey(theClientDto.getIDUser())) {
				useCaseHandlers.remove(theClientDto.getIDUser());
			}
			return;
		}

		if (uuid != null && useCaseId != null && hashMapDesClients != null) {
			
			HashMap<String, UseCaseHandler> hashUsecases = hashMapDesClients.get(useCaseId);
			if(hashUsecases == null) {
				return;
			}
		
			if (hashUsecases.containsKey(uuid)) {
				hashUsecases.remove(uuid);
			}

			hashMapDesClients.put(useCaseId, hashUsecases);
			useCaseHandlers.put(theClientDto.getIDUser(), hashMapDesClients);
		}
	}

	@Override
	public List<Pair<?, ?>> getInfoForSelectedIIds(String uuid, Integer useCaseId, TheClientDto theClientDto,
			List<Object> selectedIIds) throws RemoteException {
		try {
			UseCaseHandler ucHandler = getUseCaseHandler(uuid, useCaseId, theClientDto);

			return ucHandler.getInfoForSelectedIIds(theClientDto, selectedIIds);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(t));
		}
	}
}
