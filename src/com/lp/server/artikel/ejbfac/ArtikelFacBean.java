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
package com.lp.server.artikel.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CellReferenceHelper;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.xerces.dom.DocumentImpl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.anfrage.fastlanereader.generated.FLRAnfragepositionlieferdaten;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.ejb.Alergen;
import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artgruspr;
import com.lp.server.artikel.ejb.ArtgrusprPK;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelalergen;
import com.lp.server.artikel.ejb.Artikelart;
import com.lp.server.artikel.ejb.Artikelartspr;
import com.lp.server.artikel.ejb.ArtikelartsprPK;
import com.lp.server.artikel.ejb.Artikellager;
import com.lp.server.artikel.ejb.ArtikellagerPK;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.Artikellieferantstaffel;
import com.lp.server.artikel.ejb.Artikellog;
import com.lp.server.artikel.ejb.Artikelshopgruppe;
import com.lp.server.artikel.ejb.Artikelsperren;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprPK;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Artklaspr;
import com.lp.server.artikel.ejb.ArtklasprPK;
import com.lp.server.artikel.ejb.Automotive;
import com.lp.server.artikel.ejb.Einkaufsean;
import com.lp.server.artikel.ejb.Farbcode;
import com.lp.server.artikel.ejb.Geometrie;
import com.lp.server.artikel.ejb.Handlagerbewegung;
import com.lp.server.artikel.ejb.Hersteller;
import com.lp.server.artikel.ejb.Katalog;
import com.lp.server.artikel.ejb.Medical;
import com.lp.server.artikel.ejb.Montage;
import com.lp.server.artikel.ejb.Reach;
import com.lp.server.artikel.ejb.Rohs;
import com.lp.server.artikel.ejb.Shopgruppe;
import com.lp.server.artikel.ejb.ShopgruppeISort;
import com.lp.server.artikel.ejb.Shopgruppespr;
import com.lp.server.artikel.ejb.ShopgruppesprPK;
import com.lp.server.artikel.ejb.Shopgruppewebshop;
import com.lp.server.artikel.ejb.Sollverkauf;
import com.lp.server.artikel.ejb.Sperren;
import com.lp.server.artikel.ejb.Trumphtopslog;
import com.lp.server.artikel.ejb.Verleih;
import com.lp.server.artikel.ejb.Verpackung;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.ejb.VkPreisfindungPreisliste;
import com.lp.server.artikel.ejb.Vorschlagstext;
import com.lp.server.artikel.ejb.Vorzug;
import com.lp.server.artikel.ejb.Webshop;
import com.lp.server.artikel.ejb.Zugehoerige;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferantstaffel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRTrumphtopslog;
import com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreis;
import com.lp.server.artikel.fastlanereader.generated.FLRVorschlagstext;
import com.lp.server.artikel.service.AlergenDto;
import com.lp.server.artikel.service.AlergenDtoAssembler;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtgruDtoAssembler;
import com.lp.server.artikel.service.ArtgrusprDto;
import com.lp.server.artikel.service.ArtgrusprDtoAssembler;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelDtoAssembler;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelFilterComboBoxEntry;
import com.lp.server.artikel.service.ArtikelImportDto;
import com.lp.server.artikel.service.ArtikelalergenDto;
import com.lp.server.artikel.service.ArtikelalergenDtoAssembler;
import com.lp.server.artikel.service.ArtikelartDto;
import com.lp.server.artikel.service.ArtikelartDtoAssembler;
import com.lp.server.artikel.service.ArtikelartsprDto;
import com.lp.server.artikel.service.ArtikelartsprDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellagerDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantDtoAssembler;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDtoAssembler;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelshopgruppeDto;
import com.lp.server.artikel.service.ArtikelshopgruppeDtoAssembler;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.ArtikelsperrenDtoAssembler;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtikelsprDtoAssembler;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.ArtklaDtoAssembler;
import com.lp.server.artikel.service.ArtklasprDto;
import com.lp.server.artikel.service.ArtklasprDtoAssembler;
import com.lp.server.artikel.service.AutomotiveDto;
import com.lp.server.artikel.service.AutomotiveDtoAssembler;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.EinkaufseanDtoAssembler;
import com.lp.server.artikel.service.FarbcodeDto;
import com.lp.server.artikel.service.FarbcodeDtoAssembler;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.GeometrieDtoAssembler;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.HandlagerbewegungDtoAssembler;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.HerstellerDtoAssembler;
import com.lp.server.artikel.service.KatalogDto;
import com.lp.server.artikel.service.KatalogDtoAssembler;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.MedicalDto;
import com.lp.server.artikel.service.MedicalDtoAssembler;
import com.lp.server.artikel.service.MontageDto;
import com.lp.server.artikel.service.MontageDtoAssembler;
import com.lp.server.artikel.service.ReachDto;
import com.lp.server.artikel.service.ReachDtoAssembler;
import com.lp.server.artikel.service.RohsDto;
import com.lp.server.artikel.service.RohsDtoAssembler;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.ShopgruppeDtoAssembler;
import com.lp.server.artikel.service.ShopgruppesprDto;
import com.lp.server.artikel.service.ShopgruppesprDtoAssembler;
import com.lp.server.artikel.service.ShopgruppewebshopDto;
import com.lp.server.artikel.service.ShopgruppewebshopDtoAssembler;
import com.lp.server.artikel.service.SollverkaufDto;
import com.lp.server.artikel.service.SollverkaufDtoAssembler;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.artikel.service.SperrenDtoAssembler;
import com.lp.server.artikel.service.TrumphtopslogDto;
import com.lp.server.artikel.service.TrumphtopslogDtoAssembler;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VerleihDtoAssembler;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.artikel.service.VerpackungDtoAssembler;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDtoAssembler;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDtoAssembler;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.artikel.service.VorschlagstextDtoAssembler;
import com.lp.server.artikel.service.VorzugDto;
import com.lp.server.artikel.service.VorzugDtoAssembler;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.artikel.service.WebshopDtoAssembler;
import com.lp.server.artikel.service.ZugehoerigeDto;
import com.lp.server.artikel.service.ZugehoerigeDtoAssembler;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejbfac.ServerDruckerFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.SiWertParser;

/**
 * <p>
 * SessionFacade fuer Modul Artikel.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-07-12
 * </p>
 * <p>
 * </p>
 * 
 * @author Christian Kollmann
 * @version 1.0
 * 
 * @todo diese Klasse darf nicht von LPReport erben.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class ArtikelFacBean extends Facade implements ArtikelFac {
	@PersistenceContext
	private EntityManager em;

	public Object[] kopiereArtikel(Integer artikelIId, String artikelnummerNeu,
			java.util.HashMap zuKopieren, Integer herstellerIIdNeu,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIId,
				theClientDto);

		artikelDto.setCNr(artikelnummerNeu);
		artikelDto.setIId(null);

		if (herstellerIIdNeu != null) {
			artikelDto.setHerstellerIId(herstellerIIdNeu);
		} else {
			if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HERSTELLER)) {
				artikelDto.setHerstellerIId(null);
			}
		}

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELGRUPPE)) {
			artikelDto.setArtgruIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELKLASSE)) {
			artikelDto.setArtklaIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_REFERENZNUMMER)) {
			artikelDto.setCReferenznr(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_LAGERMINDESTSTAND)) {
			artikelDto.setFLagermindest(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_LAGERSOLLSTAND)) {
			artikelDto.setFLagersoll(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERPACKUNSMENGE)) {
			artikelDto.setFVerpackungsmenge(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTFAKTOR)) {
			artikelDto.setFVerschnittfaktor(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTBASIS)) {
			artikelDto.setFVerschnittbasis(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_JAHRESMENGE)) {
			artikelDto.setFJahresmenge(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MWSTSATZ)) {
			artikelDto.setMwstsatzbezIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MATERIAL)) {
			artikelDto.setMaterialIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_GEWICHT)) {
			artikelDto.setFGewichtkg(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MATERIALGEWICHT)) {
			artikelDto.setFMaterialgewicht(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ZUGEHOERIGERARTIKEL)) {
			artikelDto.setArtikelIIdZugehoerig(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERTRETERPROVISION)) {
			artikelDto.setFVertreterprovisionmax(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR1)) {
			artikelDto.setFMinutenfaktor1(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR2)) {
			artikelDto.setFMinutenfaktor2(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG)) {
			artikelDto.setFMindestdeckungsbeitrag(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERKAUFSEAN)) {
			artikelDto.setCVerkaufseannr(null);
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_WARENVERKEHRSNUMMER)) {
			artikelDto.setCWarenverkehrsnummer(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_RABATTIERBAR)) {
			artikelDto.setBRabattierbar(new Short((short) 0));
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_GARANTIEZEIT)) {
			artikelDto.setIGarantiezeit(null);
		}

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_INDEX)) {
			artikelDto.setCIndex(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_REVISION)) {
			artikelDto.setCRevision(null);
		}

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_FARBCODE)) {
			artikelDto.setFarbcodeIId(null);
		}

		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_BESTELLMENGENEINHEIT)) {
			artikelDto.setEinheitCNrBestellung(null);
			artikelDto.setNUmrechnungsfaktor(null);
			artikelDto.setbBestellmengeneinheitInvers(Helper
					.boolean2Short(false));
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ERSATZARTIKEL)) {
			artikelDto.setArtikelIIdErsatz(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_URSPRUNGSLAND)) {
			artikelDto.setLandIIdUrsprungsland(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_BREITE)) {
			if (artikelDto.getGeometrieDto() != null) {
				artikelDto.getGeometrieDto().setCBreitetext(null);
				artikelDto.getGeometrieDto().setFBreite(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HOEHE)) {
			if (artikelDto.getGeometrieDto() != null) {
				artikelDto.getGeometrieDto().setFHoehe(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_TIEFE)) {
			if (artikelDto.getGeometrieDto() != null) {
				artikelDto.getGeometrieDto().setFTiefe(null);
			}
		}
		if (!zuKopieren
				.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE)) {
			artikelDto.setFFertigungssatzgroesse(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_BAUFORM)) {
			if (artikelDto.getVerpackungDto() != null) {
				artikelDto.getVerpackungDto().setCBauform(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERPACKUNGSART)) {
			if (artikelDto.getVerpackungDto() != null) {
				artikelDto.getVerpackungDto().setCVerpackungsart(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_AUFSCHLAG)) {
			if (artikelDto.getSollverkaufDto() != null) {
				artikelDto.getSollverkaufDto().setFAufschlag(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_SOLLVERKAUF)) {
			if (artikelDto.getSollverkaufDto() != null) {
				artikelDto.getSollverkaufDto().setFSollverkauf(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_RASTERLIEGEND)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto().setFRasterliegend(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_RASTERSTEHEND)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto().setFRasterstehend(null);
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HOCHSTELLEN)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto()
						.setBHochstellen(new Short((short) 0));
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HOCHSETZEN)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto().setBHochsetzen(new Short((short) 0));
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_POLARISIERT)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto()
						.setBPolarisiert(new Short((short) 0));
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_SNRBEHAFTET)) {
			artikelDto.setBSeriennrtragend(null);
		} else {
			if (artikelDto.getBSeriennrtragend() != null
					&& Helper.short2Boolean(artikelDto.getBSeriennrtragend()) == true) {
				artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
			}
		}

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_CHNRBEHAFTET)) {
			artikelDto.setBChargennrtragend(null);
		} else {
			if (artikelDto.getBChargennrtragend() != null
					&& Helper.short2Boolean(artikelDto.getBChargennrtragend()) == true) {
				artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
			}
		}

		Integer artikelIId_Neu = createArtikel(artikelDto, theClientDto);

		boolean bAndereSprachenKopiert = false;
		// PJ 10040:
		Query queryspr = em.createNamedQuery("ArtikelsprfindByArtikelIId");
		queryspr.setParameter(1, artikelIId);
		Collection<?> allArtikelspr = queryspr.getResultList();
		Iterator<?> iter = allArtikelspr.iterator();
		while (iter.hasNext()) {
			Artikelspr artikelsprTemp = (Artikelspr) iter.next();

			if (!artikelsprTemp.getPk().getLocaleCNr()
					.equals(theClientDto.getLocUiAsString())) {
				bAndereSprachenKopiert = true;
				Artikelspr artikelsprNeu = new Artikelspr(artikelIId_Neu,
						artikelsprTemp.getPk().getLocaleCNr(),
						theClientDto.getIDPersonal());
				artikelsprNeu.setCBez(artikelsprTemp.getCBez());
				artikelsprNeu.setCKbez(artikelsprTemp.getCKbez());
				artikelsprNeu.setCZbez(artikelsprTemp.getCZbez());
				artikelsprNeu.setCZbez2(artikelsprTemp.getCZbez2());

				em.persist(artikelsprNeu);
				em.flush();
			}

		}

		// Eigenschaften
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_EIGENSCHAFTEN)) {

			try {
				PaneldatenDto[] paneldatenDtos = getPanelFac()
						.paneldatenFindByPanelCNrCKey(
								PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
								artikelIId + "");
				for (int i = 0; i < paneldatenDtos.length; i++) {
					paneldatenDtos[i].setCKey(artikelIId_Neu + "");
				}
				getPanelFac().createPaneldaten(paneldatenDtos);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Kommentar
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE)) {

			try {
				boolean bAnderssprachigenKommentarKopiert = getArtikelkommentarFac()
						.kopiereArtikelkommentar(artikelIId, artikelIId_Neu,
								theClientDto);

				if (bAnderssprachigenKommentarKopiert == true) {
					bAndereSprachenKopiert = true;
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Artikellieferant
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_EKPREISE)) {
			ArtikellieferantDto[] artikellieferantDtos = artikellieferantFindByArtikelIId(
					artikelIId, theClientDto);

			for (int i = 0; i < artikellieferantDtos.length; i++) {
				ArtikellieferantDto artikellieferantDto = artikellieferantDtos[i];
				Integer artikellieferantIId = artikellieferantDto.getIId();
				artikellieferantDto.setArtikelIId(artikelIId_Neu);
				artikellieferantDto.setIId(null);
				Integer artikellieferantIId_Neu = createArtikellieferant(
						artikellieferantDto, theClientDto);

				ArtikellieferantstaffelDto[] artikellieferantstaffelDtos = artikellieferantstaffelFindByArtikellieferantIId(artikellieferantIId);
				for (int j = 0; j < artikellieferantstaffelDtos.length; j++) {
					ArtikellieferantstaffelDto artikellieferantstaffelDto = artikellieferantstaffelDtos[j];
					artikellieferantstaffelDto.setIId(null);
					artikellieferantstaffelDto
							.setArtikellieferantIId(artikellieferantIId_Neu);
					createArtikellieferantstaffel(artikellieferantstaffelDto,
							theClientDto);
				}
			}
		}
		// Katalog

		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_KATALOG)) {

			Query query = em.createNamedQuery("KatalogfindByArtikelIId");
			query.setParameter(1, artikelIId);
			Collection<?> cl = query.getResultList();

			KatalogDto[] kdtos = assembleKatalogDtos(cl);
			for (int i = 0; i < kdtos.length; i++) {
				KatalogDto dto = kdtos[i];
				dto.setIId(null);
				dto.setArtikelIId(artikelIId_Neu);
				createKatalog(dto);
			}
		}

		boolean bEsSindZukuenftigePreisevorhanden = false;

		// VK-Preise
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VKPREISE)) {
			try {

				VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
						.vkpfartikelverkaufspreisbasisFindByArtikelIId(
								artikelIId, theClientDto);

				for (int i = 0; i < dtos.length; i++) {
					VkPreisfindungEinzelverkaufspreisDto dto = dtos[i];
					if (dto.getTVerkaufspreisbasisgueltigab().after(
							new Date(System.currentTimeMillis()))) {
						bEsSindZukuenftigePreisevorhanden = true;
					}
				}
				
				// SP3021 Es werden nur Preise des angemeldeten Mandanten
				// kopiert
				// PJ18634 Nur die letztgueltige VK-Basis mit Gueltigkeit =
				// Heute anlegen

				Query query = em
						.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdBisGueltigab");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, artikelIId);
				query.setParameter(3, Helper
						.cutTimestamp(new java.sql.Timestamp(System
								.currentTimeMillis())));
				Collection<?> cl = query.getResultList();

				if (cl.size() > 0) {

					VkPreisfindungEinzelverkaufspreis basis = (VkPreisfindungEinzelverkaufspreis) cl
							.iterator().next();

					VkPreisfindungEinzelverkaufspreisDto basisDto = VkPreisfindungEinzelverkaufspreisDtoAssembler
							.createDto(basis);
					basisDto.setIId(null);
					basisDto.setArtikelIId(artikelIId_Neu);
					basisDto.setTVerkaufspreisbasisgueltigab(Helper
							.cutDate(new Date(System.currentTimeMillis())));
					getVkPreisfindungFac()
							.createVkPreisfindungEinzelverkaufspreis(basisDto,
									theClientDto);

				}

			

				// Vorher Artikelpreislisten loeschen, da bei createArtikel
				// schon
				// welche angelegt werden.
				VkPreisfindungPreislisteDto[] vkPreisfindungPreislisteDtos = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(
								artikelIId_Neu);
				for (int i = 0; i < vkPreisfindungPreislisteDtos.length; i++) {
					getVkPreisfindungFac().removeVkPreisfindungPreisliste(
							vkPreisfindungPreislisteDtos[i]);
				}

				// Nun fuer jede aktive Preisliste den letzen Preis holen und
				// mit gueltigektie heute anlegen

				VkpfartikelpreislisteDto[] vkpreislistenDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true),
								theClientDto);

				for (int i = 0; i < vkpreislistenDtos.length; i++) {

					Query ejbquery = em
							.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab");
					ejbquery.setParameter(1, artikelIId);
					ejbquery.setParameter(2, vkpreislistenDtos[i].getIId());
					ejbquery.setParameter(3, Helper.cutTimestamp(new Timestamp(
							System.currentTimeMillis())));

					Collection c = ejbquery.getResultList();

					if (c.size() > 0) {

						VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) c
								.iterator().next();

						VkPreisfindungPreislisteDto artikelpreislisteDto = VkPreisfindungPreislisteDtoAssembler
								.createDto(vkPreisfindungPreisliste);

						artikelpreislisteDto.setArtikelIId(artikelIId_Neu);
						artikelpreislisteDto.setTPreisgueltigab(Helper
								.cutDate(new Date(System.currentTimeMillis())));

						getVkPreisfindungFac().createVkPreisfindungPreisliste(
								artikelpreislisteDto, theClientDto);

					}

				}

				vkPreisfindungPreislisteDtos = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(artikelIId);

				for (int i = 0; i < vkPreisfindungPreislisteDtos.length; i++) {
					VkPreisfindungPreislisteDto dto = vkPreisfindungPreislisteDtos[i];

					if (dto.getTPreisgueltigab().after(
							new Date(System.currentTimeMillis()))) {
						bEsSindZukuenftigePreisevorhanden = true;
					}

				}

				// Mengenstaffeln

				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
								artikelIId,
								new java.sql.Date(System.currentTimeMillis()),
								null, theClientDto);

				for (int i = 0; i < vkpfMengenstaffelDtos.length; i++) {
					VkpfMengenstaffelDto dto = vkpfMengenstaffelDtos[i];
					dto.setIId(null);
					dto.setArtikelIId(artikelIId_Neu);
					dto.setTPreisgueltigab(new Date(System.currentTimeMillis()));
					getVkPreisfindungFac().createVkpfMengenstaffel(dto,
							theClientDto);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		return new Object[] { artikelIId_Neu, bAndereSprachenKopiert,
				bEsSindZukuenftigePreisevorhanden };
	}

	// private BigDecimal berechneSIWert(ArtikelsprDto artikelsprDto,
	// TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException {
	//
	// ParametermandantDto p =
	// getParameterFac().parametermandantFindByPrimaryKey(
	// ParameterFac.PARAMETER_SI_EINHEITEN,
	// ParameterFac.KATEGORIE_ARTIKEL, theClientDto.getMandant());
	// String einheiten = p.getCWert();
	// p = getParameterFac().parametermandantFindByPrimaryKey(
	// ParameterFac.PARAMETER_SI_OHNE_EINHEIT,
	// ParameterFac.KATEGORIE_ARTIKEL, theClientDto.getMandant());
	// boolean ohneEinheit = (Boolean)p.getCWertAsObject();
	// return Helper.berechneSiWertAusBezeichnung(ohneEinheit, einheiten,
	// artikelsprDto.getCBez(),
	// artikelsprDto.getCZbez(),
	// artikelsprDto.getCZbez2());
	// }

	private SiWertParser createSiWertParser(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ParametermandantDto p = getParameterFac()
				.parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_SI_EINHEITEN,
						ParameterFac.KATEGORIE_ARTIKEL,
						theClientDto.getMandant());
		String einheiten = p.getCWert();
		p = getParameterFac().parametermandantFindByPrimaryKey(
				ParameterFac.PARAMETER_SI_OHNE_EINHEIT,
				ParameterFac.KATEGORIE_ARTIKEL, theClientDto.getMandant());
		boolean ohneEinheit = (Boolean) p.getCWertAsObject();

		return new SiWertParser(ohneEinheit, einheiten);
	}

	private BigDecimal berechneSIWert(SiWertParser parser,
			ArtikelsprDto artikelsprDto) {
		return parser.berechneSiWertAusBezeichnung(artikelsprDto.getCBez(),
				artikelsprDto.getCZbez(), artikelsprDto.getCZbez2());
	}

	private BigDecimal berechneSIWert(SiWertParser parser, Artikelspr artikelspr) {
		return parser.berechneSiWertAusBezeichnung(artikelspr.getCBez(),
				artikelspr.getCZbez(), artikelspr.getCZbez2());
	}

	/**
	 * Legt einen neuen Artikel- Datensatz an, samt "Satellitentabellen". Ob
	 * Artikel Lagerbewirtschaftet ist, wird in der mandantenabh&auml;ngigen
	 * Tabelle. WW_ARTIKELLAGERBEWIRTSCHAFTET festgelegt
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return Integer
	 * @throws RemoteException
	 */
	public Integer createArtikel(ArtikelDto artikelDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (artikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelDto == null"));
		}
		if (artikelDto.getArtikelartCNr() == null
				|| artikelDto.getEinheitCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelDto.getArtikelartCNr() == null || artikelDto.getEinheitCNr() == null"));
		}
		if (!artikelDto.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)
				&& artikelDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelDto.getBVersteckt() == null"));
		}
		// Wenn Artikel Handartikel ist, dann darf cNr null sein
		if (!artikelDto.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			if (artikelDto.getCNr() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("artikelDto.getCNr() == null"));
			}
			pruefeArtikelnummer(artikelDto.getCNr(), theClientDto);

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_SI_WERT, theClientDto)
					&& artikelDto.getArtikelsprDto() != null) {

				SiWertParser parser = createSiWertParser(theClientDto);
				artikelDto.getArtikelsprDto().setNSiwert(
						berechneSIWert(parser, artikelDto.getArtikelsprDto()));
			}
		}

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			artikelDto.setMandantCNr(getSystemFac().getHauptmandant());
		} else {
			artikelDto.setMandantCNr(theClientDto.getMandant());
		}

		if (artikelDto.getIId() == null) {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKEL);
			artikelDto.setIId(pk);
		}
		if (artikelDto.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			artikelDto.setCNr("~" + artikelDto.getIId());
			// Wenn Handartikel, dann NIE Lagerbewertet bzw. Lagerbewirtschaftet
			artikelDto.setBLagerbewertet(Helper.boolean2Short(false));
			artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(false));
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
			artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			artikelDto.setBDokumentenpflicht(Helper.boolean2Short(false));
			artikelDto.setBVerleih(Helper.boolean2Short(false));
			artikelDto.setbNurzurinfo(Helper.boolean2Short(false));
			artikelDto.setBKalkulatorisch(Helper.boolean2Short(false));
			artikelDto.setbReinemannzeit(Helper.boolean2Short(false));
			// Handartikel versteckt setzen
			artikelDto.setBVersteckt(Helper.boolean2Short(true));
		}
		try {
			Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
			query.setParameter(1, artikelDto.getCNr());
			query.setParameter(2, artikelDto.getMandantCNr());
			Artikel doppelt = (Artikel) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKEL.UK" + "->" + artikelDto.getCNr()));
		} catch (NoResultException ex1) {
			// nothing here
		}
		if (artikelDto.getEinheitCNrBestellung() == null) {
			artikelDto.setNUmrechnungsfaktor(null);
		}
		if (artikelDto.getNUmrechnungsfaktor() == null) {
			artikelDto.setEinheitCNrBestellung(null);
		}

		try {
			try {
				// Default Mindestdeckungsbeitrag
				if (artikelDto.getFMindestdeckungsbeitrag() == null) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_DEFAULT_ARTIKEL_DECKUNGSBEITRAG);

					artikelDto.setFMindestdeckungsbeitrag(new Double(parameter
							.getCWert()));
				}
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			Artikel artikel = new Artikel(artikelDto.getIId(),
					artikelDto.getCNr(), artikelDto.getArtikelartCNr(),
					artikelDto.getEinheitCNr(),
					artikelDto.getFMindestdeckungsbeitrag(),
					theClientDto.getIDPersonal(), theClientDto.getIDPersonal(),
					artikelDto.getBVersteckt(), artikelDto.getMandantCNr());
			em.persist(artikel);
			em.flush();

			System.out.println("C_NR:" + artikelDto.getCNr() + " ID:"
					+ artikelDto.getIId());

			artikelDto.setBAntistatic(artikel.getBAntistatic());

			if (artikelDto.getBLagerbewertet() == null) {
				artikelDto.setBLagerbewertet(artikel.getBLagerbewertet());
			}

			try {

				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_NEUER_ARTIKEL_IST_LAGERBEWIRTSCHAFTET);

				Boolean b = (Boolean) parameter.getCWertAsObject();

				if (artikelDto.getBLagerbewirtschaftet() == null) {
					artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(b));
				}

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (artikelDto.getBDokumentenpflicht() == null) {
				artikelDto.setBDokumentenpflicht(artikel
						.getBDokumentenpflicht());
			}
			if (artikelDto.getbNurzurinfo() == null) {
				artikelDto.setbNurzurinfo(artikel.getbNurzurinfo());
			}
			if (artikelDto.getbReinemannzeit() == null) {
				artikelDto.setbReinemannzeit(artikel.getbReinemannzeit());
			}
			if (artikelDto.getbBestellmengeneinheitInvers() == null) {
				artikelDto.setbBestellmengeneinheitInvers(artikel
						.getbBestellmengeneinheitInvers());
			}
			if (artikelDto.getBWerbeabgabepflichtig() == null) {
				artikelDto.setBWerbeabgabepflichtig(artikel
						.getBWerbeabgabepflichtig());
			}
			if (artikelDto.getBVerleih() == null) {
				artikelDto.setBVerleih(artikel.getBVerleih());
			}
			if (artikelDto.getBKalkulatorisch() == null) {
				artikelDto.setBVerleih(artikel.getBKalkulatorisch());
			}

			artikelDto.setBRabattierbar(artikel.getBRabattierbar());
			if (!artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// Default-Wert fuer Rabattierbar steht in Mandantparameter
				try {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_DEFAULT_ARTIKEL_RABATTIERBAR);
					boolean bRabattierbar = ((Boolean) parameter
							.getCWertAsObject()).booleanValue();
					artikelDto.setBRabattierbar(Helper
							.boolean2Short(bRabattierbar));

				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}
			}

			if (artikelDto.getSollverkaufDto() == null) {
				SollverkaufDto dto = new SollverkaufDto();
				dto.setArtikelIId(artikelDto.getIId());
				artikelDto.setSollverkaufDto(dto);
			}

			// Default Aufschlag
			if (artikelDto.getSollverkaufDto().getFAufschlag() != null) {
				try {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_DEFAULT_ARTIKEL_AUFSCHLAG);
					artikelDto.getSollverkaufDto().setFAufschlag(
							new Double(parameter.getCWert()));
				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}

			}
			try {
				// Default Sollverkauf
				if (artikelDto.getSollverkaufDto().getFSollverkauf() != null) {

					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_DEFAULT_ARTIKEL_SOLLVERKAUF);
					artikelDto.getSollverkaufDto().setFSollverkauf(
							new Double(parameter.getCWert()));
				}

				if (artikelDto.getBSeriennrtragend() == null) {

					// Default Seriennummernbehaftet
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_ARTIKEL_DEFAULT_SERIENNUMMERNBEHAFTET);

					Boolean b = (Boolean) parameter.getCWertAsObject();

					artikelDto.setBSeriennrtragend(Helper.boolean2Short(b));
					if (b == true) {
						artikelDto.setBChargennrtragend(Helper
								.boolean2Short(false));
					}
				}

				if (artikelDto.getBChargennrtragend() == null) {

					// Default Chrgennummernbehaftet
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_ARTIKEL_DEFAULT_CHARGENNUMMERNBEHAFTET);

					Boolean b = (Boolean) parameter.getCWertAsObject();

					artikelDto.setBChargennrtragend(Helper.boolean2Short(b));
				}

				// PJ 15001
				VkpfartikelpreislisteDto[] preislisteDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true),
								theClientDto);

				for (int i = 0; i < preislisteDtos.length; i++) {

					if (preislisteDtos[i].getNStandardrabattsatz() != null) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();

						vkPreisfindungPreislisteDto.setArtikelIId(artikelDto
								.getIId());
						vkPreisfindungPreislisteDto
								.setNArtikelstandardrabattsatz(preislisteDtos[i]
										.getNStandardrabattsatz());
						vkPreisfindungPreislisteDto.setTPreisgueltigab(Helper
								.cutDate(new java.sql.Date(System
										.currentTimeMillis())));
						vkPreisfindungPreislisteDto
								.setVkpfartikelpreislisteIId(preislisteDtos[i]
										.getIId());

						getVkPreisfindungFac().createVkPreisfindungPreisliste(
								vkPreisfindungPreislisteDto, theClientDto);
					}
				}

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
			if (artikelDto.getBSeriennrtragend().intValue() == 1
					|| artikelDto.getBChargennrtragend().intValue() == 1) {
				// Wenn Artikel Seriennummern/Chargennummerntragend ist, dann
				// muss er Lagerbewirtschaftet sein
				artikelDto.setBLagerbewirtschaftet(new Short((short) 1));
			}
			artikelDto.setBKalkulatorisch(Helper.boolean2Short(false));

			setArtikelFromArtikelDto(artikel, artikelDto);
			Integer iId = artikel.getIId();

			if (artikelDto.getArtikelsprDto() != null) {
				Artikelspr artikelspr = new Artikelspr(iId,
						theClientDto.getLocUiAsString(),
						theClientDto.getIDPersonal());
				em.persist(artikelspr);
				em.flush();
				artikelDto.getArtikelsprDto().setPersonalIIdAendern(
						theClientDto.getIDPersonal());
				artikelDto.getArtikelsprDto().setTAendern(
						new java.sql.Timestamp(System.currentTimeMillis()));
				setArtikelsprFromArtikelsprDto(artikelspr,
						artikelDto.getArtikelsprDto());
			}
			if (artikelDto.getMontageDto() != null) {
				Montage montage = new Montage(iId);
				em.persist(montage);
				em.flush();
				setMontageFromMontageDto(montage, artikelDto.getMontageDto());
			}
			if (artikelDto.getVerpackungDto() != null) {
				Verpackung verpackung = new Verpackung(iId);
				em.persist(verpackung);
				em.flush();
				setVerpackungFromVerpackungDto(verpackung,
						artikelDto.getVerpackungDto());
			}
			if (artikelDto.getSollverkaufDto() != null) {
				Sollverkauf sollverkauf = new Sollverkauf(iId);
				em.persist(sollverkauf);
				em.flush();
				setSollverkaufFromSollverkaufDto(sollverkauf,
						artikelDto.getSollverkaufDto());
			}
			if (artikelDto.getGeometrieDto() != null) {
				Geometrie geometrie = new Geometrie(iId);
				em.persist(geometrie);
				em.flush();
				setGeometrieFromGeometrieDto(geometrie,
						artikelDto.getGeometrieDto());
			}

			artikeleigenschaftenDefaultwerteAnlegen(artikelDto.getIId(),
					artikelDto.getArtgruIId(), theClientDto);

			return artikelDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	/**
	 * Entfernt vorhandenen Artikel- Datensatz samt Satellitentabellen, wenn
	 * dieser nicht mehr in Verwendung ist. Wenn dieser noch in Verwendung ist,
	 * wird eine EJBExceptionLP ausgel&ouml;st.
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeArtikel(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			Verpackung verpackung = em.find(Verpackung.class, iId);
			if (verpackung != null) {
				em.remove(verpackung);
			}
			Sollverkauf sollverkauf = em.find(Sollverkauf.class, iId);
			if (sollverkauf != null) {
				em.remove(sollverkauf);
			}
			Geometrie geometrie = em.find(Geometrie.class, iId);
			if (geometrie != null) {
				em.remove(geometrie);
			}
			Montage montage = em.find(Montage.class, iId);
			if (montage != null) {
				em.remove(montage);
			}
			for (Artikellog log : artikellogFindByArtikelIId(iId)) {
				em.remove(log);
			}

			try {
				ArtikelkommentarDto[] kommentare = getArtikelkommentarFac()
						.artikelkommentarFindByArtikelIId(iId, theClientDto);
				for (ArtikelkommentarDto dto : kommentare) {
					getArtikelkommentarFac().removeArtikelkommentar(dto);
				}

				// Preisfindung loeschen
				VkPreisfindungPreislisteDto[] vkPfPl = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(iId);
				for (VkPreisfindungPreislisteDto dto : vkPfPl) {
					getVkPreisfindungFac().removeVkPreisfindungPreisliste(
							dto.getIId());
				}
				// Einzelverkaufspreise loeschen UW->CK
				VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
						.vkpfartikelverkaufspreisbasisFindByArtikelIId(iId,
								theClientDto);

				for (int i = 0; i < dtos.length; i++) {
					getVkPreisfindungFac()
							.removeVkPreisfindungEinzelverkaufspreis(dtos[i],
									theClientDto);
				}
			} catch (RemoteException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
			}
			Query query = em.createNamedQuery("ArtikelsprfindByArtikelIId");
			query.setParameter(1, iId);
			Collection<?> allArtikelspr = query.getResultList();
			Iterator<?> iter = allArtikelspr.iterator();
			while (iter.hasNext()) {
				Artikelspr artikelsprTemp = (Artikelspr) iter.next();
				em.remove(artikelsprTemp);
			}
			Artikel toRemove = em.find(Artikel.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei Remove Artikel. der Artikel mit der iid "
								+ iId + " existiert nicht");
			}
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public String getArtikelsperrenText(Integer artikelIId) {
		String sperren = null;

		try {
			ArtikelsperrenDto[] artSp = getArtikelFac()
					.artikelsperrenFindByArtikelIId(artikelIId);
			if (artSp.length > 0) {
				sperren = "";
				for (int i = 0; i < artSp.length; i++) {

					String sSperrenbez = getArtikelFac()
							.sperrenFindByPrimaryKey(artSp[i].getSperrenIId())
							.getCBez();
					if (sSperrenbez.length() >= 4) {
						sperren += sSperrenbez.substring(0, 4) + ". ";
					} else {
						sperren += sSperrenbez + ". ";
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return sperren;
	}

	/**
	 * uselocalspr: 0 Hole alle Artikelarten nach Spr.
	 * 
	 * @param cNrSpracheI
	 *            String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllSprArtikelarten(String cNrSpracheI) throws EJBExceptionLP {

		myLogger.entry();
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("ArtikelartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Artikelart artikelartTemp = (Artikelart) itArten.next();
			Object key = artikelartTemp.getCNr();
			Object value = null;
			// try {
			Artikelartspr artikelartspr = em.find(Artikelartspr.class,
					new ArtikelartsprPK(cNrSpracheI, artikelartTemp.getCNr()));
			if (artikelartspr == null) {
				// fuer locale und C_NR keine Bezeichnu g vorhanden ...
				value = artikelartTemp.getCNr();
			} else {
				value = artikelartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// fuer locale und C_NR keine Bezeichnu g vorhanden ...
			// value = artikelartTemp.getCNr();
			// }
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	private List<FLRArtikelgruppe> holeAlleArtikelgruppen(Session session) {

		String hbnString = "SELECT ag.i_id, ag.artgru_i_id FROM FLRArtikelgruppe AS ag WHERE ag.artgru_i_id IS NOT NULL";

		@SuppressWarnings("unchecked")
		List<Object[]> hbnQueryList = session.createQuery(hbnString).list();

		List<FLRArtikelgruppe> allArtikelgruppeEntries = new ArrayList<FLRArtikelgruppe>();

		for (Object[] object : hbnQueryList) {
			// i_id, artgru_i_id
			allArtikelgruppeEntries.add(new FLRArtikelgruppe(
					(Integer) object[0], (Integer) object[1]));
		}

		return allArtikelgruppeEntries;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Map getAllSprArtgru(TheClientDto theClientDto) {

		int iNurVatergruppenAnzeigen = 0;
		boolean bSorierungNachCNr = false;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELGRUPPE_NUR_VATERGRUPPEN_ANZEIGEN);
			iNurVatergruppenAnzeigen = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELGRUPPE_NACH_CBEZ_ODER_CNR_ANZEIGEN);
			bSorierungNachCNr = (Boolean) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (iNurVatergruppenAnzeigen == 2) {

			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = FLRSessionFactory.getFactory().openSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez"
					+ " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr"
					+ " WHERE ag.artgru_i_id IS NULL ";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];
				String key = (String) o[1];
				String c_bez = (String) o[2];
				String value = null;
				if (c_bez != null) {
					value = c_bez;
				} else {
					value = key;
				}

				ArtikelFilterComboBoxEntry entry = new ArtikelFilterComboBoxEntry();

				entry.setCnr(key);
				entry.setCbez(value);

				entry.setUntergruppen(holeUntergruppenEingerueckt(id));
				entry.setFilterExpression("(" + id + ")");
				al.add(entry);

			}
			session.close();

			return ArtikelFilterComboBoxEntry.getSortierteListe(al,
					bSorierungNachCNr);
		} else if (iNurVatergruppenAnzeigen == 1) {

			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = FLRSessionFactory.getFactory().openSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez"
					+ " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr"
					+ " WHERE ag.artgru_i_id IS NULL ";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];
				String key = (String) o[1];
				String c_bez = (String) o[2];
				String value = null;
				if (c_bez != null) {
					value = c_bez;
				} else {
					value = key;
				}

				ArtikelFilterComboBoxEntry entry = new ArtikelFilterComboBoxEntry();

				entry.setCnr(key);
				entry.setCbez(value);

				// PJ18638
				HashSet<Integer> hs = new HashSet<Integer>();

				hs.add(id);

				hs = holeUntergruppen(id, hs);

				String inClause = "(";

				Iterator it = hs.iterator();
				while (it.hasNext()) {
					inClause += it.next() + "";
					if (it.hasNext()) {
						inClause += ",";
					}
				}

				inClause += ")";

				entry.setFilterExpression(inClause);
				al.add(entry);

			}
			session.close();

			return ArtikelFilterComboBoxEntry.getSortierteListe(al,
					bSorierungNachCNr);
		} else {
			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = FLRSessionFactory.getFactory().openSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez"
					+ " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr ";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];
				String key = (String) o[1];
				String c_bez = (String) o[2];
				String value = null;
				if (c_bez != null) {
					value = c_bez;
				} else {
					value = key;
				}

				ArtikelFilterComboBoxEntry entry = new ArtikelFilterComboBoxEntry();

				entry.setCnr(key);
				entry.setCbez(value);
				entry.setFilterExpression("(" + id + ")");

				al.add(entry);

			}
			session.close();

			return ArtikelFilterComboBoxEntry.getSortierteListe(al,
					bSorierungNachCNr);
		}
	}

	private HashSet<Integer> holeUntergruppen(Integer id, HashSet<Integer> hs) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory()
				.openSession();
		String sQueryUntegruppen = "SELECT ag FROM FLRArtikelgruppe as ag WHERE ag.artgru_i_id="
				+ id;
		org.hibernate.Query queryUntergruppen = sessionUntegruppen
				.createQuery(sQueryUntegruppen);

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen
				.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {
			FLRArtikelgruppe flrArtikelgruppe = (FLRArtikelgruppe) resultListIteratorUntegruppen
					.next();
			hs.add(flrArtikelgruppe.getI_id());

			hs = holeUntergruppen(flrArtikelgruppe.getI_id(), hs);

		}

		return hs;
	}

	private ArrayList<ArtikelFilterComboBoxEntry> holeUntergruppenEingerueckt(
			Integer vatergeruppe_id) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory()
				.openSession();

		String sQueryUntegruppen = "SELECT ag.i_id, ag.c_nr, aspr.c_bez"
				+ " FROM FLRArtikelgruppe as ag"
				+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr WHERE ag.artgru_i_id="
				+ vatergeruppe_id;

		org.hibernate.Query queryUntergruppen = sessionUntegruppen
				.createQuery(sQueryUntegruppen);

		ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList<ArtikelFilterComboBoxEntry>();

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen
				.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {

			Object o[] = (Object[]) resultListIteratorUntegruppen.next();
			Integer id = (Integer) o[0];
			String key = (String) o[1];
			String c_bez = (String) o[2];
			String value = null;
			if (c_bez != null) {
				value = c_bez;
			} else {
				value = key;
			}

			System.out.println(id + " " + value);

			ArtikelFilterComboBoxEntry entrySub = new ArtikelFilterComboBoxEntry();
			entrySub.setCnr(key);
			entrySub.setCbez(value);

			entrySub.setFilterExpression("(" + id + ")");

			entrySub.setUntergruppen(holeUntergruppenEingerueckt(id));

			al.add(entrySub);
		}

		return al;
	}

	public Map getAllVerleih() {

		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("VerleihfindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Verleih verleih = (Verleih) itArten.next();
			tmArten.put(verleih.getIId(), verleih.getITage());
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	public Map getAllVorzug(TheClientDto theClientDto) {

		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("VorzugfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Vorzug verleih = (Vorzug) itArten.next();
			tmArten.put(verleih.getIId(), verleih.getCBez());
		}

		return tmArten;
	}

	private void artikelAenderungLoggen(Integer artikelIId, String key,
			String von, String nach, TheClientDto theClientDto) {
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELLOG);

		if (von != null && von.length() > 80) {
			von = von.substring(0, 79);
		}
		if (nach != null && nach.length() > 80) {
			nach = nach.substring(0, 79);
		}

		Artikellog log = new Artikellog(pk, artikelIId, key, von, nach,
				theClientDto.getLocUiAsString(), theClientDto.getIDPersonal(),
				new java.sql.Timestamp(System.currentTimeMillis()));
		em.merge(log);
		em.flush();
	}

	private List<Artikellog> artikellogFindByArtikelIId(Integer iid) {
		HvTypedQuery<Artikellog> logs = new HvTypedQuery<Artikellog>(
				em.createNamedQuery(Artikellog.FindByArtikelIId));
		logs.setParameter("iid", iid);
		return logs.getResultList();
	}

	private void vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(
			ArtikelDto artikelDto_Aktuell, TheClientDto theClientDto) {

		ArtikelDto artikelDto_Vorher = artikelFindByPrimaryKey(
				artikelDto_Aktuell.getIId(), theClientDto);

		HvDtoLogger<ArtikelDto> artikelLogger = new HvDtoLogger<ArtikelDto>(em,
				theClientDto);
		artikelLogger.log(artikelDto_Vorher, artikelDto_Aktuell);

		// Artikelnummer
		if (!artikelDto_Aktuell.getCNr().equals(artikelDto_Vorher.getCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_NUMMER, artikelDto_Vorher.getCNr(),
					artikelDto_Aktuell.getCNr(), theClientDto);
		}

		if (!artikelDto_Aktuell.getEinheitCNr().equals(
				artikelDto_Vorher.getEinheitCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_EINHEIT,
					artikelDto_Vorher.getEinheitCNr(),
					artikelDto_Aktuell.getEinheitCNr(), theClientDto);
		}
		if (!artikelDto_Aktuell.getBVersteckt().equals(
				artikelDto_Vorher.getBVersteckt())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_VERSTECKT,
					artikelDto_Vorher.getBVersteckt() + "",
					artikelDto_Aktuell.getBVersteckt() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getbNurzurinfo().equals(
				artikelDto_Vorher.getbNurzurinfo())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_NUR_ZUR_INFO,
					artikelDto_Vorher.getbNurzurinfo() + "",
					artikelDto_Aktuell.getbNurzurinfo() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getbReinemannzeit().equals(
				artikelDto_Vorher.getbReinemannzeit())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_REINE_MANNZEIT,
					artikelDto_Vorher.getbReinemannzeit() + "",
					artikelDto_Aktuell.getbReinemannzeit() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getArtikelartCNr().equals(
				artikelDto_Vorher.getArtikelartCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELART,
					artikelDto_Vorher.getArtikelartCNr() + "",
					artikelDto_Aktuell.getArtikelartCNr() + "", theClientDto);
		}

		// SPR
		String bezVorher = "";
		String kbezVorher = "";
		String zbezVorher = "";
		String zbez2Vorher = "";

		if (artikelDto_Vorher.getArtikelsprDto() != null) {
			bezVorher = artikelDto_Vorher.getArtikelsprDto().getCBez();
			kbezVorher = artikelDto_Vorher.getArtikelsprDto().getCKbez();
			zbezVorher = artikelDto_Vorher.getArtikelsprDto().getCZbez();
			zbez2Vorher = artikelDto_Vorher.getArtikelsprDto().getCZbez2();
		}

		String bezNachher = "";
		String kbezNachher = "";
		String zbezNachher = "";
		String zbez2Nachher = "";

		if (artikelDto_Aktuell.getArtikelsprDto() != null) {
			bezNachher = artikelDto_Aktuell.getArtikelsprDto().getCBez();
			kbezNachher = artikelDto_Aktuell.getArtikelsprDto().getCKbez();
			zbezNachher = artikelDto_Aktuell.getArtikelsprDto().getCZbez();
			zbez2Nachher = artikelDto_Aktuell.getArtikelsprDto().getCZbez2();
		}

		if (!(bezVorher + "").equals(bezNachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_BEZEICHNUNG, bezVorher, bezNachher,
					theClientDto);
		}

		if (!(kbezVorher + "").equals(kbezNachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_KURZBEZEICHNUNG, kbezVorher,
					kbezNachher, theClientDto);
		}

		if (!(zbezVorher + "").equals(zbezNachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_ZUSATZBEZ, zbezVorher, zbezNachher,
					theClientDto);
		}

		if (!(zbez2Vorher + "").equals(zbez2Nachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_ZUSATZBEZ2, zbez2Vorher,
					zbez2Nachher, theClientDto);
		}

		String referenzVorher = artikelDto_Vorher.getCReferenznr() + "";
		String referenzAktuell = artikelDto_Aktuell.getCReferenznr() + "";

		if (!referenzVorher.equals(referenzAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_REFERENZNUMMER, referenzVorher,
					referenzAktuell, theClientDto);
		}

		String indexVorher = artikelDto_Vorher.getCIndex() + "";
		String indexAktuell = artikelDto_Aktuell.getCIndex() + "";

		if (!indexVorher.equals(indexAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_INDEX, indexVorher, indexAktuell,
					theClientDto);
		}

		String revisionVorher = artikelDto_Vorher.getCRevision() + "";
		String revisionAktuell = artikelDto_Aktuell.getCRevision() + "";

		if (!revisionVorher.equals(revisionAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_REVISION, revisionVorher,
					revisionAktuell, theClientDto);
		}

		String bestelleinheitVorher = artikelDto_Vorher
				.getEinheitCNrBestellung() + "";
		String bestelleinheitAktuell = artikelDto_Aktuell
				.getEinheitCNrBestellung() + "";

		if (!bestelleinheitVorher.equals(bestelleinheitAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_BESTELLEINHEIT,
					bestelleinheitVorher, bestelleinheitAktuell, theClientDto);
		}
		String umrechnungsfaktorVorher = artikelDto_Vorher
				.getNUmrechnungsfaktor() + "";
		String umrechnungsfaktorAktuell = artikelDto_Aktuell
				.getNUmrechnungsfaktor() + "";

		if (!umrechnungsfaktorVorher.equals(umrechnungsfaktorAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_UMRECHNUNGSFAKTOR,
					umrechnungsfaktorVorher, umrechnungsfaktorAktuell,
					theClientDto);
		}

		try {
			String mwstsatzVorher = "";
			if (artikelDto_Vorher.getMwstsatzbezIId() != null) {
				MwstsatzbezDto mwstsatzbezDto = getMandantFac()
						.mwstsatzbezFindByPrimaryKey(
								artikelDto_Vorher.getMwstsatzbezIId(),
								theClientDto);
				mwstsatzVorher = mwstsatzbezDto.getCBezeichnung();
			}

			String mwstsatzAktuell = "";
			if (artikelDto_Aktuell.getMwstsatzbezIId() != null) {
				MwstsatzbezDto mwstsatzbezDto = getMandantFac()
						.mwstsatzbezFindByPrimaryKey(
								artikelDto_Aktuell.getMwstsatzbezIId(),
								theClientDto);
				mwstsatzAktuell = mwstsatzbezDto.getCBezeichnung();
			}
			if (!mwstsatzVorher.equals(mwstsatzAktuell)) {
				artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
						ArtikelFac.ARTIKEL_LOG_MWSTSATZ, mwstsatzVorher,
						mwstsatzAktuell, theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String gruVorher = "";
		if (artikelDto_Vorher.getArtgruIId() != null) {
			ArtgruDto dto = artgruFindByPrimaryKey(
					artikelDto_Vorher.getArtgruIId(), theClientDto);
			gruVorher = dto.getCNr();
		}

		String gruAktuell = "";
		if (artikelDto_Aktuell.getArtgruIId() != null) {
			ArtgruDto dto = artgruFindByPrimaryKey(
					artikelDto_Aktuell.getArtgruIId(), theClientDto);
			gruAktuell = dto.getCNr();
		}
		if (!gruVorher.equals(gruAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELGRUPPE, gruVorher,
					gruAktuell, theClientDto);
		}

		String shopgruppeVorher = "";
		if (artikelDto_Vorher.getShopgruppeIId() != null) {
			ShopgruppeDto dto = shopgruppeFindByPrimaryKey(
					artikelDto_Vorher.getShopgruppeIId(), theClientDto);
			shopgruppeVorher = dto.getCNr();
		}

		String shopgruppeAktuell = "";
		if (artikelDto_Aktuell.getShopgruppeIId() != null) {
			ShopgruppeDto dto = shopgruppeFindByPrimaryKey(
					artikelDto_Aktuell.getShopgruppeIId(), theClientDto);
			shopgruppeAktuell = dto.getCNr();
		}
		if (!shopgruppeVorher.equals(shopgruppeAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_SHOPGRUPPE, shopgruppeVorher,
					shopgruppeAktuell, theClientDto);
		}

		String liefergruppeVorher = "";
		if (artikelDto_Vorher.getLfliefergruppeIId() != null) {
			LfliefergruppeDto dto = getLieferantServicesFac()
					.lfliefergruppeFindByPrimaryKey(
							artikelDto_Vorher.getLfliefergruppeIId(),
							theClientDto);
			liefergruppeVorher = dto.getCNr();
		}

		String liefergruppeAktuell = "";
		if (artikelDto_Aktuell.getLfliefergruppeIId() != null) {
			LfliefergruppeDto dto = getLieferantServicesFac()
					.lfliefergruppeFindByPrimaryKey(
							artikelDto_Aktuell.getLfliefergruppeIId(),
							theClientDto);
			liefergruppeAktuell = dto.getCNr();
		}
		if (!liefergruppeVorher.equals(liefergruppeAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_LIEFERGRUPPE, liefergruppeVorher,
					liefergruppeAktuell, theClientDto);
		}

		String artklaVorher = "";
		if (artikelDto_Vorher.getArtklaIId() != null) {
			ArtklaDto dto = artklaFindByPrimaryKey(
					artikelDto_Vorher.getArtklaIId(), theClientDto);
			artklaVorher = dto.getCNr();
		}

		String artklaAktuell = "";
		if (artikelDto_Aktuell.getArtklaIId() != null) {
			ArtklaDto dto = artklaFindByPrimaryKey(
					artikelDto_Aktuell.getArtklaIId(), theClientDto);
			artklaAktuell = dto.getCNr();
		}
		if (!artklaVorher.equals(artklaAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELKLASSE, artklaVorher,
					artklaAktuell, theClientDto);
		}

		String herstellerVorher = "";
		if (artikelDto_Vorher.getHerstellerIId() != null) {
			HerstellerDto dto = herstellerFindByPrimaryKey(
					artikelDto_Vorher.getHerstellerIId(), theClientDto);
			herstellerVorher = dto.getCNr();
		}

		String herstellerAktuell = "";
		if (artikelDto_Aktuell.getHerstellerIId() != null) {
			HerstellerDto dto = herstellerFindByPrimaryKey(
					artikelDto_Aktuell.getHerstellerIId(), theClientDto);
			herstellerAktuell = dto.getCNr();
		}
		if (!herstellerVorher.equals(herstellerAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_HERSTELLER, herstellerVorher,
					herstellerAktuell, theClientDto);
		}

		String letzteWartungVorher = artikelDto_Vorher.getTLetztewartung() + "";
		String letzteWartungAktuell = artikelDto_Aktuell.getTLetztewartung()
				+ "";

		if (!letzteWartungVorher.equals(letzteWartungAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(),
					ArtikelFac.ARTIKEL_LOG_LETZTE_WARTUNG, letzteWartungVorher,
					letzteWartungAktuell, theClientDto);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void alleSIwerteNachtragen(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT spr FROM FLRArtikellistespr spr ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		int zaehler = 1;
		while (resultListIterator.hasNext()) {
			FLRArtikellistespr spr = (FLRArtikellistespr) resultListIterator
					.next();

			if (spr.getC_bez() == null && spr.getC_zbez() == null
					&& spr.getC_zbez2() == null) {

			} else {
				getArtikelFac().siWertNachtragen(
						spr.getId().getArtikelliste().getI_id(),
						spr.getLocale_c_nr(), theClientDto);
			}
			zaehler++;

			System.out
					.println("Zeile " + zaehler + " von " + resultList.size());

		}
	}

	public void siWertNachtragen(Integer artikelIId, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(
				artikelIId, localeCNr));
		SiWertParser siParser = createSiWertParser(theClientDto);

		BigDecimal si = berechneSIWert(siParser, artikelspr);
		artikelspr.setCSiwert(HelperServer.getDBValueFromBigDecimal(si, 60));
		em.merge(artikelspr);
		em.flush();
	}

	//
	public void setzeArtikelSNRtragendOhneWeitereAktion(Integer artikelIId) {
		Artikel artikel = em.find(Artikel.class, artikelIId);
		artikel.setBSeriennrtragend(Helper.boolean2Short(true));
	}

	/**
	 * Schreibt &Auml;nderungen eines Aritkels samt Satellitentabellen in die
	 * Datenbank zur&uuml;ck.
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void updateArtikel(ArtikelDto artikelDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (artikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelDto == null"));
		}
		if (artikelDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelDto.getIId()==null"));
		}
		if (artikelDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelDto.getBVersteckt() == null"));
		}

		if (artikelDto.getCNr() == null || artikelDto.getBAntistatic() == null
				|| artikelDto.getBChargennrtragend() == null
				|| artikelDto.getBRabattierbar() == null
				|| artikelDto.getBSeriennrtragend() == null
				|| artikelDto.getBLagerbewirtschaftet() == null
				|| artikelDto.getBVerleih() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelDto.getCNr() == null || artikelDto.getBAntistatic() == null || artikelDto.getBChargennrtragend() == null || artikelDto.getBRabattierbar() == null || artikelDto.getBSeriennrtragend() == null || artikelDto.getBLagerbewirtschaftet() == null || artikelDto.getBVerleih() == null"));
		}

		vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(artikelDto,
				theClientDto);

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			artikelDto.setMandantCNr(getSystemFac().getHauptmandant());
		} else {
			artikelDto.setMandantCNr(theClientDto.getMandant());
		}

		// Wenn Artikel Handartikel ist, dann darf cNr null sein
		if (!artikelDto.getArtikelartCNr().equals(
				ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			pruefeArtikelnummer(artikelDto.getCNr(), theClientDto);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_SI_WERT, theClientDto)
					&& artikelDto.getArtikelsprDto() != null) {
				SiWertParser siParser = createSiWertParser(theClientDto);
				artikelDto.getArtikelsprDto()
						.setNSiwert(
								berechneSIWert(siParser,
										artikelDto.getArtikelsprDto()));
			}
		}

		if (artikelDto.getEinheitCNrBestellung() == null) {
			artikelDto.setNUmrechnungsfaktor(null);
		} else {
			// PJ18425 Wenn Bestellmengeneinheit vorhanden, dann darf im
			// Artikellieferant keine VPE (Bestelleinheit) definiert sein
			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikelIId");
			query.setParameter(1, artikelDto.getIId());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Artikellieferant al = (Artikellieferant) it.next();
				if (al.getEinheitCNrVpe() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN,
							new Exception(
									"FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN"));

				}

			}

		}

		if (artikelDto.getNUmrechnungsfaktor() == null) {
			artikelDto.setEinheitCNrBestellung(null);
		}

		if (artikelDto.getArtikelIIdErsatz() != null) {
			pruefeObEndlosschleifeErsatzartikel(artikelDto.getIId(),
					artikelDto.getArtikelIIdErsatz());
		}

		if (Helper.short2boolean(artikelDto.getBVerleih()) == true) {
			artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
		}

		Integer iId = artikelDto.getIId();

		try {
			Artikel artikel = em.find(Artikel.class, iId);

			try {
				Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
				query.setParameter(1, artikelDto.getCNr());
				query.setParameter(2, artikelDto.getMandantCNr());
				Integer iIdVorhanden = ((Artikel) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_ARTIKEL.CNR"));
				}

			} catch (NoResultException ex) {

			}

			if (artikelDto.getTLetztewartung() != null) {

				if (artikel.getTLetztewartung() == null) {
					artikelDto.setPersonalIIdLetztewartung(theClientDto
							.getIDPersonal());
				} else {
					if (!artikelDto.getTLetztewartung().equals(
							artikel.getTLetztewartung())) {
						artikelDto.setPersonalIIdLetztewartung(theClientDto
								.getIDPersonal());
					}
				}

			} else {
				artikelDto.setPersonalIIdLetztewartung(null);
			}

			// PJ 16901
			boolean bArtGruGeaendert = false;
			if (artikelDto.getArtgruIId() == null
					&& artikel.getArtgruIId() != null) {
				bArtGruGeaendert = true;
			}
			if (artikelDto.getArtgruIId() != null
					&& artikel.getArtgruIId() == null) {
				bArtGruGeaendert = true;
			}

			if (artikelDto.getArtgruIId() != null
					&& artikel.getArtgruIId() != null
					&& !artikelDto.getArtgruIId()
							.equals(artikel.getArtgruIId())) {
				bArtGruGeaendert = true;
			}

			if (bArtGruGeaendert == true) {
				PaneldatenDto[] eigs = getPanelFac()
						.paneldatenFindByPanelCNrCKey(
								PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
								artikelDto.getIId() + "");
				for (int i = 0; i < eigs.length; i++) {
					getPanelFac().removePaneldaten(eigs[i].getIId());
				}
			}

			// PJ 16811
			if (artikelDto.getCVerpackungseannr() != null
					&& artikelDto.getCVerkaufseannr() != null
					&& artikelDto.getCVerpackungseannr().equals(
							artikelDto.getCVerkaufseannr())) {
				ArrayList al = new ArrayList();

				al.add(artikelDto.formatArtikelbezeichnung());
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN,
						al, new Exception(
								"FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
			}

			if (artikelDto.getCVerpackungseannr() != null) {

				try {
					Query query = em
							.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerpackungseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0))
									.getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(
										iIdVorhanden, theClientDto);
								al.add(artikelDtoVorhanden
										.formatArtikelbezeichnung());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN,
										al,
										new Exception(
												"FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
				try {
					Query query = em
							.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerpackungseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0))
									.getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(
										iIdVorhanden, theClientDto);
								al.add(artikelDtoVorhanden
										.formatArtikelbezeichnung());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN,
										al,
										new Exception(
												"FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
			}
			if (artikelDto.getCVerkaufseannr() != null) {

				try {
					Query query = em
							.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerkaufseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0))
									.getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(
										iIdVorhanden, theClientDto);
								al.add(artikelDtoVorhanden
										.formatArtikelbezeichnung());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN,
										al,
										new Exception(
												"FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
				try {
					Query query = em
							.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerkaufseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0))
									.getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(
										iIdVorhanden, theClientDto);
								al.add(artikelDtoVorhanden
										.formatArtikelbezeichnung());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN,
										al,
										new Exception(
												"FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
			}
			// Von nicht Lagerbewirtschaftet auf Lagerbewirtschaftet kann man
			// nur wechseln, wenn Lagerstaende 0 sind
			if (Helper.short2boolean(artikel.getBLagerbewirtschaftet()) != Helper
					.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				// CK: PJ 13906 Wenn Buchungen auf dem Artikel sind, dann aknn
				// man nicht mehr wechseln

				// Wechsel wenn lagerbewegungen vorhanden nur von
				// Lagerbewirtschaftet auf NICHT
				// LAGERBEWIRTSCHAFTET moeglich
				if (Helper.short2boolean(artikel.getBLagerbewirtschaftet())) {

					if (Helper.short2boolean(artikel.getBChargennrtragend())
							|| Helper.short2boolean(artikel
									.getBSeriennrtragend())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH,
								new Exception(
										"ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH"));
					} else {

						myLogger.warn("Eigenschaft 'Lagerbewirtschaftet' des Artikels "
								+ artikelDto.getCNr()
								+ " abgeschaltet durch Personal-ID "
								+ theClientDto.getIDPersonal());

						artikelDto.setBLagerbewirtschaftet(Helper
								.boolean2Short(false));

						Integer lagerIIdKeinLager = getLagerFac()
								.lagerFindByCNrByMandantCNr(
										LagerFac.LAGER_KEINLAGER,
										theClientDto.getMandant()).getIId();

						Artikellager artikellager = em.find(Artikellager.class,
								new ArtikellagerPK(artikel.getIId(),
										lagerIIdKeinLager));
						if (artikellager == null) {
							artikellager = new Artikellager(artikel.getIId(),
									lagerIIdKeinLager,
									artikelDto.getMandantCNr());
							em.merge(artikellager);
							em.flush();
						}

						// Zuerst fuer den Artikel alle LAGER_I_ID in
						// WW_LAGERBEWEGUNG auf KEIN_LAGER aendern
						Session session = FLRSessionFactory.getFactory()
								.openSession();
						String hqlUpdate = "update FLRLagerbewegung l SET l.lager_i_id="
								+ lagerIIdKeinLager
								+ " WHERE l.artikel_i_id="
								+ artikel.getIId();
						session.createQuery(hqlUpdate).executeUpdate();
						session.close();

						// In WW_ARTIKELLAGER alle Laeger ausser KEIN_LAGER
						// entfernen und Gestpreis neu kalkulieren
						BigDecimal bdPreisNeu = getLagerReportFac()
								.recalcGestehungspreisKomplett(
										artikel.getIId(), false);

						Query query = em
								.createNamedQuery("ArtikellagerfindByArtikelIId");
						query.setParameter(1, artikel.getIId());
						Collection<?> cl = query.getResultList();
						Iterator it = cl.iterator();
						while (it.hasNext()) {
							Artikellager al = (Artikellager) it.next();
							if (!al.getPk().getLagerIId()
									.equals(lagerIIdKeinLager)) {
								em.remove(al);
								em.flush();
							} else {
								if (bdPreisNeu != null) {
									al.setNGestehungspreis(bdPreisNeu);
									em.merge(al);
									em.flush();
								}
							}
						}

					}

				} else {
					LagerbewegungDto[] dtos = getLagerFac()
							.lagerbewegungFindByArtikelIId(artikelDto.getIId());
					if (dtos != null & dtos.length > 0) {
						throw new EJBExceptionLP(
								EJBExceptionLP.ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH,
								new Exception(
										"ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH"));

					}
				}

			}
			// Von nicht Chargennummernbehaftet auf Chargennummernbehaftet kann
			// man nur wechseln, wenn Lagerstaende 0 sind
			if (Helper.short2boolean(artikel.getBChargennrtragend()) != Helper
					.short2boolean(artikelDto.getBChargennrtragend())) {
				if (Helper.short2boolean(artikelDto.getBChargennrtragend()) == true) {
					getLagerFac().aendereEigenschaftChargengefuehrt(
							artikelDto.getIId(), true, theClientDto);
				} else {
					getLagerFac().aendereEigenschaftChargengefuehrt(
							artikelDto.getIId(), false, theClientDto);
				}
			}
			// Von nicht Seriennummernbehaftet auf Seriennummernbehaftet kann
			// man nur wechseln, wenn Lagerstaende 0 sind
			if (Helper.short2boolean(artikel.getBSeriennrtragend()) != Helper
					.short2boolean(artikelDto.getBSeriennrtragend())) {
				// CK: PJ 13906 Wenn Buchungen auf dem Artikel sind, dann aknn
				// man nicht mehr wechseln
				LagerbewegungDto[] dtos = getLagerFac()
						.lagerbewegungFindByArtikelIId(artikelDto.getIId());
				if (dtos != null & dtos.length > 0) {
					{
						throw new EJBExceptionLP(
								EJBExceptionLP.ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH,
								new Exception(
										"ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH"));
					}
				}
			}

			if (artikelDto.getBSeriennrtragend().intValue() == 1
					|| artikelDto.getBChargennrtragend().intValue() == 1) {
				// Wenn Artikel Seriennummern/Chargennummerntragend ist, dann
				// muss er Lagerbewirtschaftet sein
				artikelDto.setBLagerbewirtschaftet(new Short((short) 1));
			}

			if (artikelDto.getArtikelIIdZugehoerig() != null
					&& pruefeObArtikelInArtikelSchonVorhanden(
							artikelDto.getIId(),
							artikelDto.getArtikelIIdZugehoerig(), theClientDto) == true) {

				throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_DEADLOCK,
						new Exception("ARTIKEL_DEADLOCK"));

			}

			setArtikelFromArtikelDto(artikel, artikelDto);
			artikel.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			artikel.setPersonalIIdAendern(theClientDto.getIDPersonal());

			if (artikelDto.getArtikelsprDto() != null) {

				// try {
				Artikelspr artikelspr = em.find(Artikelspr.class,
						new ArtikelsprPK(iId, theClientDto.getLocUiAsString()));

				if (artikelspr != null) {
					artikelDto.getArtikelsprDto().setPersonalIIdAendern(
							theClientDto.getIDPersonal());
					artikelDto.getArtikelsprDto().setTAendern(
							new java.sql.Timestamp(System.currentTimeMillis()));
					setArtikelsprFromArtikelsprDto(artikelspr,
							artikelDto.getArtikelsprDto());
				} else {
					artikelspr = new Artikelspr(iId,
							theClientDto.getLocUiAsString(),
							theClientDto.getIDPersonal());
					em.persist(artikelspr);
					em.flush();
					artikelDto.getArtikelsprDto().setPersonalIIdAendern(
							theClientDto.getIDPersonal());
					artikelDto.getArtikelsprDto().setTAendern(
							new java.sql.Timestamp(System.currentTimeMillis()));

					setArtikelsprFromArtikelsprDto(artikelspr,
							artikelDto.getArtikelsprDto());
				}
				// }
				// catch (FinderException ex) {
				// Artikelspr artikelspr = new
				// Artikelspr(iId,getTheClient(idUser
				// ).getLocUiAsString(),getTheClient(idUser).getIDPersonal());
				// em.persist(artikelspr);
				// artikelDto.getArtikelsprDto().setPersonalIIdAendern(theClient.
				// getIDPersonal());
				// artikelDto.getArtikelsprDto().setTAendern(new
				// java.sql.Timestamp(System.
				// currentTimeMillis()));

				// setArtikelsprFromArtikelsprDto(artikelspr,
				// artikelDto.getArtikelsprDto());
				// }

			}
			if (artikelDto.getGeometrieDto() != null) {
				// try {
				Geometrie geometrie = em.find(Geometrie.class, iId);
				if (geometrie == null) {
					geometrie = new Geometrie(iId);
					em.persist(geometrie);
					em.flush();
					setGeometrieFromGeometrieDto(geometrie,
							artikelDto.getGeometrieDto());
				}
				setGeometrieFromGeometrieDto(geometrie,
						artikelDto.getGeometrieDto());
				// }
				// catch (FinderException ex) {
				// Geometrie geometrie = new Geometrie(iId);
				// em.persist(geometrie);
				// setGeometrieFromGeometrieDto(geometrie,
				// artikelDto.getGeometrieDto());
				// }
			}
			if (artikelDto.getSollverkaufDto() != null) {
				// try {
				Sollverkauf sollverkauf = em.find(Sollverkauf.class, iId);
				if (sollverkauf == null) {
					sollverkauf = new Sollverkauf(iId);
					em.persist(sollverkauf);
					em.flush();
					setSollverkaufFromSollverkaufDto(sollverkauf,
							artikelDto.getSollverkaufDto());
				}
				setSollverkaufFromSollverkaufDto(sollverkauf,
						artikelDto.getSollverkaufDto());
				// }
				// catch (FinderException ex) {
				// Sollverkauf sollverkauf = new Sollverkauf(iId);
				// em.persist(sollverkauf);
				// setSollverkaufFromSollverkaufDto(sollverkauf,
				// artikelDto.getSollverkaufDto());
				// }
			}
			if (artikelDto.getMontageDto() != null) {
				// try {
				Montage montage = em.find(Montage.class, iId);
				if (montage == null) {
					montage = new Montage(iId);
					em.persist(montage);
					em.flush();
					setMontageFromMontageDto(montage,
							artikelDto.getMontageDto());
				}
				setMontageFromMontageDto(montage, artikelDto.getMontageDto());
				// }
				// catch (FinderException ex) {
				// Montage montage = new Montage(iId);
				// em.persist(montage);
				// setMontageFromMontageDto(montage,
				// artikelDto.getMontageDto());
				// }
			}
			if (artikelDto.getVerpackungDto() != null) {
				// try {
				Verpackung verpackung = em.find(Verpackung.class, iId);
				if (verpackung == null) {
					verpackung = new Verpackung(iId);
					em.persist(verpackung);
					em.flush();
					setVerpackungFromVerpackungDto(verpackung,
							artikelDto.getVerpackungDto());
				}
				setVerpackungFromVerpackungDto(verpackung,
						artikelDto.getVerpackungDto());
				// }
				// catch (FinderException ex) {
				// Verpackung verpackung = new Verpackung(iId);
				// em.persist(verpackung);
				// setVerpackungFromVerpackungDto(verpackung,
				// artikelDto.getVerpackungDto());
				// }
			}

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	/**
	 * Gibt den Artikel samt vorhandenen Satellitentabellen in der User- Sprache
	 * zur&uuml;ck, der dem Prim&auml;rschl&uuml;ssel (iId) entspricht.
	 * 
	 * @param iId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ArtikelDto
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArtikelDto artikelFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Artikel artikel = em.find(Artikel.class, iId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		ArtikelDto artikelDto = assembleArtikelDto(artikel);
		ArtikelsprDto artikelsprDto = null;
		// try {
		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId,
				theClientDto.getLocUiAsString()));
		if (artikelspr != null) {
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		}
		// }
		// catch (FinderException ex) {
		// }
		if (artikelsprDto == null) {
			// try {
			artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId,
					theClientDto.getLocKonzernAsString()));
			if (artikelspr != null) {
				artikelsprDto = assembleArtikelsprDto(artikelspr);
			}
			// }
			// catch (FinderException ex) {
			// Nothing here
			// }
		}

		artikelDto.setArtikelsprDto(artikelsprDto);

		// try {
		Geometrie geometrie = em.find(Geometrie.class, iId);
		if (geometrie != null) {
			GeometrieDto geometrieDto = assembleGeometrieDto(geometrie);
			artikelDto.setGeometrieDto(geometrieDto);
		}
		// catch (FinderException ex) {
		// Nothing here
		// }
		// try {
		Sollverkauf sollverkauf = em.find(Sollverkauf.class, iId);
		if (sollverkauf != null) {
			SollverkaufDto sollverkaufDto = assembleSollverkaufDto(sollverkauf);
			artikelDto.setSollverkaufDto(sollverkaufDto);
		}
		// }
		// catch (FinderException ex) {
		// Nothing here
		// }
		// try {
		Verpackung verpackung = em.find(Verpackung.class, iId);
		if (verpackung != null) {
			VerpackungDto verpackungDto = assembleVerpackungDto(verpackung);
			artikelDto.setVerpackungDto(verpackungDto);
		}
		// catch (FinderException ex) {
		// Nothing here
		// }
		// try {
		Montage montage = em.find(Montage.class, iId);
		if (montage != null) {
			MontageDto montageDto = assembleMontageDto(montage);
			artikelDto.setMontageDto(montageDto);
		}
		// catch (FinderException ex) {
		// Nothing here
		// }

		return artikelDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer iId,
			TheClientDto theClientDto) {

		Artikel artikel = em.find(Artikel.class, iId);
		if (artikel == null) {
			return null;
		}
		return assembleArtikelDto(artikel);
	}

	public ArrayList<String> getVorgaengerArtikel(Integer artikelIId) {
		ArrayList<String> artikel = new ArrayList<String>();

		Query query = em.createNamedQuery("ArtikelfindByArtikelIIdErsatz");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();

		while (it.hasNext()) {

			Artikel a = (Artikel) it.next();
			artikel.add(a.getCNr());
		}

		return artikel;
	}

	public ArtikelDto artikelFindByPrimaryKeySmall(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Artikel artikel = em.find(Artikel.class, iId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelFindByPrimaryKEySmall. Es gibt keinen Artikel mit der iid "
							+ iId);
		}
		ArtikelDto artikelDto = assembleArtikelDto(artikel);
		ArtikelsprDto artikelsprDto = null;
		// try {
		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId,
				theClientDto.getLocUiAsString()));
		if (artikelspr != null) {
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		}
		// }
		// catch (FinderException ex) {
		// }
		if (artikelsprDto == null) {
			// try {
			artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId,
					theClientDto.getLocKonzernAsString()));
			if (artikelspr != null) {
				artikelsprDto = assembleArtikelsprDto(artikelspr);
			}
			// }
			// catch (FinderException ex) {
			// Nothing here
			// }
		}

		artikelDto.setArtikelsprDto(artikelsprDto);

		return artikelDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private boolean pruefeObArtikelInArtikelSchonVorhanden(
			Integer artikelIId_Wurzel, Integer artikelIId_ZuSuchende,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		Artikel artikel = em.find(Artikel.class, artikelIId_ZuSuchende);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei priefeObArtikelINArtikelSChonVorhanden. Es gibt keine Artikel mit iid "
							+ artikelIId_ZuSuchende);

		}
		if (artikel.getArtikelIIdZugehoerig() == null) {
			return false;
		}

		if (artikelIId_Wurzel.equals(artikel.getArtikelIIdZugehoerig())) {
			return true;
		} else {
			return pruefeObArtikelInArtikelSchonVorhanden(artikelIId_Wurzel,
					artikel.getArtikelIIdZugehoerig(), theClientDto);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);

		// }
	}

	public ArtikelsprDto getDefaultArtikelbezeichnungen(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		ArtikelsprDto artikelsprDto = null;
		// try {
		ArtikelsprPK pk = new ArtikelsprPK(artikelIId,
				theClientDto.getLocKonzernAsString());
		Artikelspr artikelspr = em.find(Artikelspr.class, pk);
		if (artikelspr != null) {
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		}
		// }
		// catch (FinderException ex) {
		// Nothing here
		// }

		return artikelsprDto;

	}

	/**
	 * Gibt den Artikel samt vorhandenen Satellitentabellen in der User- Sprache
	 * zur&uuml;ck, der der Artikelnummer entspricht.
	 * 
	 * @param cNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return ArtikelDto
	 */
	public ArtikelDto artikelFindByCNr(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ArtikelDto artikelDto = artikelFindByCNrOhneExc(cNr, theClientDto);
			if (null == artikelDto)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						new NoResultException());
			return artikelDto;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public ArtikelDto artikelFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCnr) {

		Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCnr);
		try {
			Artikel artikel = (Artikel) query.getSingleResult();
			return assembleArtikelDto(artikel);
		} catch (NoResultException ex) {
			return null;
		}
	}

	/**
	 * Gibt den Artikel samt vorhandenen Satellitentabellen in der User- Sprache
	 * zur&uuml;ck, der der Artikelnummer entspricht. Es wird keine EJBException
	 * geworfen
	 * 
	 * @param cNr
	 * @param theClientDto
	 * @return null, oder der Artikel
	 */
	public ArtikelDto artikelFindByCNrOhneExc(String cNr,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
		query.setParameter(1, cNr);

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			query.setParameter(2, getSystemFac().getHauptmandant());
		} else {
			query.setParameter(2, theClientDto.getMandant());
		}

		try {
			Artikel artikel = (Artikel) query.getSingleResult();
			return artikelFindByPrimaryKey(artikel.getIId(), theClientDto);
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
		}

		return null;
	}

	/**
	 * Gibt alle Artikel mit einer bestimmte CNr mandantenunabhaengig zurueck
	 * 
	 * @param cNr
	 *            String
	 * @return ArtikelDto[]
	 * @throws EJBExceptionLP
	 */
	public ArtikelDto[] artikelFindByCNr(String cNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ArtikelfindByCNr");
		query.setParameter(1, cNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleArtikelDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ArtikelDto[] artikelFindByCNrOhneExc(String cNr) {
		Query query = em.createNamedQuery("ArtikelfindByCNr");
		query.setParameter(1, cNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleArtikelDtos(cl);
	}

	public ArtikelDto[] artikelFindSpecial(String bauteil, String bauform) {
		Session session = FLRSessionFactory.getFactory().openSession();

		/*
		 * String sQuery =
		 * "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID WHERE lower(translate(S.C_BEZ,' ABCDEFGHIJKLMNOPQRSTUVWXYZ-+.;,abcdefghijklmnopqrstuvwxyz\\%/_&Auml;&Ouml;&UUml;&auml;&ouml;&uuml;&szlig;','')) LIKE '"
		 * + bauteil.replaceAll("\\D", "").toLowerCase() + "%'";
		 */
		/*
		 * String sQuery =
		 * "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
		 * + " WHERE A.ARTIKELART_C_NR='Artikel' AND levenshtein(S.C_BEZ, '" +
		 * bauteil + "')<10 LIMIT 20;";
		 */

		String sQuery = "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
				+ " WHERE ARTIKELART_C_NR='Artikel' "
				+ " AND A.B_VERSTECKT = 0 "
				+ " AND ("
				+ " LOWER(TRIM(S.C_BEZ)) LIKE '%"
				+ bauteil.toLowerCase()
				+ "%'"
				+ " OR dmetaphone(trim(S.C_BEZ)) = dmetaphone('"
				+ bauteil
				+ "')"
				+ " OR dmetaphone_alt(trim(S.C_BEZ)) = dmetaphone_alt('"
				+ bauteil
				+ "')"
				+ " OR soundex(trim(S.C_BEZ)) = soundex('"
				+ bauteil
				+ "')"
				+ " OR lower(trim(A.C_NR)) like '%"
				+ bauteil.toLowerCase()
				+ "%'"
				+ " ) "
				+ " ORDER BY levenshtein(trim(S.C_BEZ), '"
				+ bauteil
				+ "'), levenshtein(trim(A.C_NR), '"
				+ bauteil
				+ "')"
				+ " LIMIT 20;";

		/*
		 * String sQuery =
		 * "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
		 * +
		 * " WHERE A.ARTIKELART_C_NR='Artikel' AND soundex(S.C_BEZ) LIKE soundex('"
		 * + bauteil + "') LIMIT 20;";
		 */
		/*
		 * String sQuery =
		 * "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
		 * + " WHERE metaphone(S.C_BEZ,4) LIKE metaphone('" + bauteil +
		 * "',4) LIMIT 100;";
		 */
		org.hibernate.Query query = session.createSQLQuery(sQuery);
		// query.setMaxResults(100);
		List<?> list = null;
		try {
			list = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list.size() == 0) {
			sQuery = "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
					+ " WHERE ARTIKELART_C_NR='Artikel' AND dmetaphone(trim(A.C_NR)) = dmetaphone('"
					+ bauteil
					+ "') AND soundex(trim(A.C_NR)) = soundex('"
					+ bauteil
					+ "') "
					+ " ORDER BY levenshtein(trim(A.C_NR), '"
					+ bauteil + "') LIMIT 20;";
			query = session.createSQLQuery(sQuery);
			try {
				list = query.list();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Iterator it = list.iterator();
		ArtikelDto[] artikelDtos = new ArtikelDto[list.size()];
		int i = 0;
		while (it.hasNext()) {
			Integer iid = (Integer) it.next();
			Artikel artikel = em.find(Artikel.class, iid);
			if (artikel != null) {
				artikelDtos[i++] = assembleArtikelDto(artikel);
			}
		}
		return artikelDtos;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeCSVImport(ArtikelImportDto[] daten,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		for (int i = 2; i < daten.length + 2; i++) {

			String fehler = "";
			ArtikelImportDto zeile = daten[i - 2];

			try {
				pruefeArtikelnummer(zeile.getArtikelnummer(), theClientDto);
			} catch (EJBExceptionLP e1) {

				if (e1.getCode() == EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG) {
					fehler += "Feld Artikelnummer zu Lang, Zeile:" + i + "; ";
				} else if (e1.getCode() == EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ) {
					fehler += "Feld Artikelnummer zu Kurz, Zeile:" + i + "; ";
				} else if (e1.getCode() == EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT) {
					fehler += "Feld Artikelnummer enth\u00E4lt ung\u00FCltige Zeichen, Zeile:"
							+ i + "; ";
				}
			}

			if (zeile.getBezeichnung().length() > 40) {
				fehler += "Feld Bezeichnung zu lang, Zeile:" + i + "; ";
			}
			if (zeile.getKurzbezeichnung().length() > 25) {
				fehler += "Feld Kurzbezeichnung zu lang, Zeile:" + i + "; ";
			}
			if (zeile.getZusatzbezeichnung().length() > 40) {
				fehler += "Feld Zusatzbezeichnung zu lang, Zeile:" + i + "; ";
			}
			if (zeile.getZusatzbezeichnung2().length() > 40) {
				fehler += "Feld Zusatzbezeichnung2 zu lang, Zeile:" + i + "; ";
			}
			if (zeile.getReferenznummer().length() > 25) {
				fehler += "Feld Referenznummer zu lang, Zeile:" + i + "; ";
			}

			if (zeile.getArtikelart().length() > 0) {
				if (zeile.getArtikelart().equals(
						ArtikelFac.ARTIKELART_ARTIKEL.trim())) {

				} else if (zeile.getArtikelart().equals(
						ArtikelFac.ARTIKELART_HANDARTIKEL.trim())) {

				} else if (zeile.getArtikelart().equals(
						ArtikelFac.ARTIKELART_ARBEITSZEIT.trim())) {

				} else {
					fehler += " Artikelart '" + zeile.getArtikelart()
							+ "' nicht vorhanden, Zeile:" + i + "; ";
				}

			}

			if (zeile.getEinheit().length() > 0) {
				try {
					getSystemFac().einheitFindByPrimaryKey(zeile.getEinheit(),
							theClientDto);
				} catch (Throwable e) {
					fehler += " Einheit '" + zeile.getEinheit()
							+ "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getArtikelgruppe().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelgruppe bereits
					// vorhanden.
					Query query = em
							.createNamedQuery("ArtgrufindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelgruppe());
					query.setParameter(2, theClientDto.getMandant());
					Artgru doppelt = (Artgru) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Artikelgruppe  '" + zeile.getArtikelgruppe()
							+ "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getArtikelklasse().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em
							.createNamedQuery("ArtklafindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelklasse());
					query.setParameter(2, theClientDto.getMandant());
					Artkla doppelt = (Artkla) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Artikelklasse  '" + zeile.getArtikelklasse()
							+ "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getMwstsatz().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em
							.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, zeile.getMwstsatz());
					Mwstsatzbez doppelt = (Mwstsatzbez) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Mwstsatz  '" + zeile.getMwstsatz()
							+ "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getVkpreisbasis().length() > 0) {

				try {
					BigDecimal bdvkpreisbasis = new BigDecimal(
							zeile.getVkpreisbasis());
				} catch (NumberFormatException e) {
					fehler += " VK-Preisbasis ist kein g\u00FCltiger Wert  '"
							+ zeile.getVkpreisbasis() + "'  Zeile:" + i + "; ";
				}

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

				try {
					java.sql.Date date = new java.sql.Date(format.parse(
							zeile.getVkpreisbasisgueltigab()).getTime());
				} catch (ParseException e) {
					fehler += " VK-PreisbasisG\u00FCltigab ist kein g\u00FCltiges Datum  '"
							+ zeile.getVkpreisbasisgueltigab()
							+ "'  Zeile:"
							+ i + "; ";
				}

				if (zeile.getFixpreispreisliste1().length() > 0
						|| zeile.getRabattsatzpreisliste1().length() > 0) {

					if (zeile.getFixpreispreisliste1().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(
									zeile.getFixpreispreisliste1());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste1 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste1()
									+ "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(
									zeile.getRabattsatzpreisliste1());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste1 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste1()
									+ "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(
								zeile.getGueltigabpreisliste1()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste1 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste1()
								+ "'  Zeile:"
								+ i + "; ";
					}

				}

				// Preisliste2

				if (zeile.getFixpreispreisliste2().length() > 0
						|| zeile.getRabattsatzpreisliste2().length() > 0) {

					if (zeile.getFixpreispreisliste2().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(
									zeile.getFixpreispreisliste2());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste2 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste2()
									+ "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(
									zeile.getRabattsatzpreisliste2());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste2 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste2()
									+ "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(
								zeile.getGueltigabpreisliste2()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste2 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste2()
								+ "'  Zeile:"
								+ i + "; ";
					}

				}

				// Preisliste3

				if (zeile.getFixpreispreisliste3().length() > 0
						|| zeile.getRabattsatzpreisliste3().length() > 0) {

					if (zeile.getFixpreispreisliste3().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(
									zeile.getFixpreispreisliste3());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste3 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste3()
									+ "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(
									zeile.getRabattsatzpreisliste3());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste3 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste3()
									+ "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(
								zeile.getGueltigabpreisliste3()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste3 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste3()
								+ "'  Zeile:"
								+ i + "; ";
					}

				}

			}

			if (zeile.getRevision().length() > 15) {
				fehler += "Feld Revision zu lang, Zeile:" + i + "; ";
			}

			if (zeile.getIndex().length() > 15) {
				fehler += "Feld Index zu lang, Zeile:" + i + "; ";
			}

			boolean bSnrBehaftet = false;
			if (zeile.getSnrbehaftet().length() > 0) {
				try {
					Short snrbehaftet = new Short(zeile.getSnrbehaftet());

					if (snrbehaftet > 1) {
						throw new NumberFormatException();
					}

					bSnrBehaftet = Helper.short2boolean(snrbehaftet);
				} catch (NumberFormatException e) {
					fehler += " Seriennumernbehaftet hat keinen g\u00FCltigen Wert  '"
							+ zeile.getSnrbehaftet() + "'  Zeile:" + i + "; ";
				}
			}

			if (zeile.getChargenbehaftet().length() > 0) {
				try {
					Short chnrbehaftet = new Short(zeile.getChargenbehaftet());
					boolean bChnrBehaftet = Helper.short2boolean(chnrbehaftet);
					if (chnrbehaftet > 1) {
						throw new NumberFormatException();
					}
					if (bSnrBehaftet == true && bChnrBehaftet == true) {
						fehler += " Seriennumernbehaftet und Chragennummernbehaftet d\u00FCrfen nicht gleichzeitig = 1 sein  '"
								+ zeile.getSnrbehaftet()
								+ "'  Zeile:"
								+ i
								+ "; ";
					}
				} catch (NumberFormatException e) {
					fehler += " Seriennumernbehaftet hat keinen g\u00FCltigen Wert  '"
							+ zeile.getChargenbehaftet()
							+ "'  Zeile:"
							+ i
							+ "; ";
				}
			}

			if (fehler.length() > 0) {
				rueckgabe += fehler + new String(CRLFAscii);
			}

		}

		return rueckgabe;
	}

	private void setArtikelFromArtikelDto(Artikel artikel, ArtikelDto artikelDto) {
		artikel.setHerstellerIId(artikelDto.getHerstellerIId());
		artikel.setCArtikelbezhersteller(artikelDto.getCArtikelbezhersteller());
		artikel.setCArtikelnrhersteller(artikelDto.getCArtikelnrhersteller());
		artikel.setArtgruIId(artikelDto.getArtgruIId());
		artikel.setArtklaIId(artikelDto.getArtklaIId());
		artikel.setCNr(artikelDto.getCNr());
		artikel.setArtikelartCNr(artikelDto.getArtikelartCNr());
		artikel.setEinheitCNr(artikelDto.getEinheitCNr());
		artikel.setBSeriennrtragend(artikelDto.getBSeriennrtragend());
		artikel.setBChargennrtragend(artikelDto.getBChargennrtragend());
		artikel.setBLagerbewertet(artikelDto.getBLagerbewertet());
		artikel.setBLagerbewirtschaftet(artikelDto.getBLagerbewirtschaftet());
		artikel.setBDokumentenpflicht(artikelDto.getBDokumentenpflicht());
		artikel.setCReferenznr(artikelDto.getCReferenznr());
		artikel.setFLagermindest(artikelDto.getFLagermindest());
		artikel.setFLagersoll(artikelDto.getFLagersoll());
		artikel.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
		artikel.setFVerschnittfaktor(artikelDto.getFVerschnittfaktor());
		artikel.setFVerschnittbasis(artikelDto.getFVerschnittbasis());
		artikel.setFJahresmenge(artikelDto.getFJahresmenge());
		artikel.setMwstsatzIId(artikelDto.getMwstsatzbezIId());
		artikel.setMaterialIId(artikelDto.getMaterialIId());
		artikel.setFGewichtkg(artikelDto.getFGewichtkg());
		artikel.setFMaterialgewicht(artikelDto.getFMaterialgewicht());
		artikel.setBAntistatic(artikelDto.getBAntistatic());
		artikel.setArtikelIIdZugehoerig(artikelDto.getArtikelIIdZugehoerig());
		artikel.setArtikelIIdErsatz(artikelDto.getArtikelIIdErsatz());
		artikel.setFVertreterprovisionmax(artikelDto
				.getFVertreterprovisionmax());
		artikel.setFMinutenfaktor1(artikelDto.getFMinutenfaktor1());
		artikel.setFMinutenfaktor2(artikelDto.getFMinutenfaktor2());
		artikel.setFMindestdeckungsbeitrag(artikelDto
				.getFMindestdeckungsbeitrag());
		artikel.setCVerkaufseannr(artikelDto.getCVerkaufseannr());
		artikel.setCVerpackungseannr(artikelDto.getCVerpackungseannr());
		artikel.setCWarenverkehrsnummer(artikelDto.getCWarenverkehrsnummer());
		artikel.setBRabattierbar(artikelDto.getBRabattierbar());
		artikel.setBVerleih(artikelDto.getBVerleih());
		artikel.setIGarantiezeit(artikelDto.getIGarantiezeit());
		artikel.setMandantCNr(artikelDto.getMandantCNr());
		artikel.setFarbcodeIId(artikelDto.getFarbcodeIId());
		artikel.setEinheitCNrBestellung(artikelDto.getEinheitCNrBestellung());
		artikel.setNUmrechnungsfaktor(artikelDto.getNUmrechnungsfaktor());
		artikel.setBVersteckt(artikelDto.getBVersteckt());
		artikel.setLandIIdUrsprungsland(artikelDto.getLandIIdUrsprungsland());
		artikel.setFFertigungssatzgroesse(artikelDto
				.getFFertigungssatzgroesse());
		artikel.setIWartungsintervall(artikelDto.getIWartungsintervall());
		artikel.setISofortverbrauch(artikelDto.getISofortverbrauch());
		artikel.setCIndex(artikelDto.getCIndex());
		artikel.setCRevision(artikelDto.getCRevision());
		artikel.setFStromverbrauchmax(artikelDto.getFStromverbrauchmax());
		artikel.setFStromverbrauchtyp(artikelDto.getFStromverbrauchtyp());
		artikel.setFDetailprozentmindeststand(artikelDto
				.getFDetailprozentmindeststand());
		artikel.setLfliefergruppeIId(artikelDto.getLfliefergruppeIId());
		artikel.setbNurzurinfo(artikelDto.getbNurzurinfo());
		artikel.setbReinemannzeit(artikelDto.getbReinemannzeit());
		artikel.setShopgruppeIId(artikelDto.getShopgruppeIId());
		artikel.setbBestellmengeneinheitInvers(artikelDto
				.getbBestellmengeneinheitInvers());
		artikel.setBWerbeabgabepflichtig(artikelDto.getBWerbeabgabepflichtig());
		artikel.setTLetztewartung(artikelDto.getTLetztewartung());
		artikel.setPersonalIIdLetztewartung(artikelDto
				.getPersonalIIdLetztewartung());
		artikel.setBKalkulatorisch(artikelDto.getBKalkulatorisch());
		artikel.setNAufschlagBetrag(artikelDto.getNAufschlagBetrag());
		artikel.setFAufschlagProzent(artikelDto.getFAufschlagProzent());

		artikel.setCUL(artikelDto.getCUL());
		artikel.setReachIId(artikelDto.getReachIId());
		artikel.setRohsIId(artikelDto.getRohsIId());
		artikel.setAutomotiveIId(artikelDto.getAutomotiveIId());
		artikel.setMedicalIId(artikelDto.getMedicalIId());
		artikel.setFUeberproduktion(artikelDto.getFUeberproduktion());
		artikel.setVorzugIId(artikelDto.getVorzugIId());
		artikel.setCEccn(artikelDto.getCEccn());
		artikel.setFFertigungsVpe(artikelDto.getFFertigungsVpe());

		em.merge(artikel);
		em.flush();
	}

	private ArtikelDto assembleArtikelDto(Artikel artikel) {
		return ArtikelDtoAssembler.createDto(artikel);
	}

	private ArtikelDto[] assembleArtikelDtos(Collection<?> artikels) {
		List<ArtikelDto> list = new ArrayList<ArtikelDto>();
		if (artikels != null) {
			Iterator<?> iterator = artikels.iterator();
			while (iterator.hasNext()) {
				Artikel artikel = (Artikel) iterator.next();
				list.add(assembleArtikelDto(artikel));
			}
		}
		ArtikelDto[] returnArray = new ArtikelDto[list.size()];
		return (ArtikelDto[]) list.toArray(returnArray);
	}

	public Integer createArtkla(ArtklaDto artklaDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artklaDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artklaDto == null"));
		}
		if (artklaDto.getCNr() == null || artklaDto.getBTops() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artklaDto.getCNr() == null || artklaDto.getBTops() == null"));
		}
		try {
			Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
			query.setParameter(1, artklaDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Artkla doppelt = (Artkla) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTGRU.CNR"));

		} catch (NoResultException ex) {

		}
		artklaDto.setMandantCNr(theClientDto.getMandant());

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKLASSE);
			artklaDto.setIId(pk);

			Artkla artkla = new Artkla(artklaDto.getIId(), artklaDto.getCNr(),
					artklaDto.getBTops(), artklaDto.getMandantCNr());

			em.persist(artkla);
			em.flush();
			setArtklaFromArtklaDto(artkla, artklaDto);
			if (artklaDto.getArtklasprDto() != null) {
				Artklaspr artklaspr = new Artklaspr(
						theClientDto.getLocUiAsString(), artklaDto.getIId());
				em.persist(artklaspr);
				em.flush();
				setArtklasprFromArtklasprDto(artklaspr,
						artklaDto.getArtklasprDto());
			}
			return artklaDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtkla(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("ArtklasprfindByArtklaIId");
			query.setParameter(1, iId);
			Collection<?> allArtklaspr = query.getResultList();
			Iterator<?> iter = allArtklaspr.iterator();
			while (iter.hasNext()) {
				Artklaspr artklasprTemp = (Artklaspr) iter.next();
				em.remove(artklasprTemp);
			}
			Artkla artkla = em.find(Artkla.class, iId);
			if (artkla != null) {
				em.remove(artkla);
				em.flush();
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public void updateArtkla(ArtklaDto artklaDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artklaDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artklaDto == null"));
		}
		if (artklaDto.getIId() == null || artklaDto.getCNr() == null
				|| artklaDto.getBTops() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artklaDto.getIId() == null || artklaDto.getCNr() == null || artklaDto.getBTops() == null"));
		}
		if (artklaDto.getIId().equals(artklaDto.getArtklaIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception(
							"artklaDto.getIId() == artklaDto.getArtklaIId()"));

		}
		Integer iId = artklaDto.getIId();

		Artkla artkla = null;
		// try {
		artkla = em.find(Artkla.class, iId);
		if (artkla == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei updateArtikelkla. Es gibt keine iid " + iId
							+ "\nartklaDto.toString: " + artklaDto.toString());
		}

		try {
			Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
			query.setParameter(1, artklaDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Artkla) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTKLA.CNR"));
			}
		} catch (NoResultException ex) {

		}
		try {
			setArtklaFromArtklaDto(artkla, artklaDto);

			if (artklaDto.getArtklasprDto() != null) {
				// try {
				Artklaspr artklaspr = em.find(Artklaspr.class, new ArtklasprPK(
						theClientDto.getLocUiAsString(), iId));

				if (artklaspr == null) {
					artklaspr = new Artklaspr(theClientDto.getLocUiAsString(),
							iId);
					em.persist(artklaspr);
					em.flush();
					setArtklasprFromArtklasprDto(artklaspr,
							artklaDto.getArtklasprDto());
				}
				setArtklasprFromArtklasprDto(artklaspr,
						artklaDto.getArtklasprDto());
				// }
				// catch (FinderException ex) {
				// Artklaspr artklaspr = new
				// Artklaspr(getTheClient(idUser).getLocUiAsString(), iId);
				// em.persist(artklaspr);
				// setArtklasprFromArtklasprDto(artklaspr,
				// artklaDto.getArtklasprDto());
				// }
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	// public Integer createShopgruppeOld(ShopgruppeDto dto, TheClientDto
	// theClientDto) {
	//
	// try {
	// Query query = em.createNamedQuery("ShopgruppefindByCNrMandantCNr");
	// query.setParameter(1, dto.getCNr());
	// query.setParameter(2, theClientDto.getMandant());
	// Shopgruppe doppelt = (Shopgruppe) query.getSingleResult();
	//
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
	// new Exception("WW_SHOPGRUPPE.CNR"));
	//
	// } catch (NoResultException ex) {
	//
	// }
	//
	// dto.setMandantCNr(theClientDto.getMandant());
	// dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
	// dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
	// dto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
	// dto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
	//
	// ShopgruppeISort iSorter = new ShopgruppeISort(em,
	// theClientDto.getMandant()) ;
	// dto.setISort(iSorter.getNextISort()) ;
	//
	// try {
	// // generieren von primary key
	// PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
	// Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SHOPGRUPPE);
	// dto.setIId(pk);
	//
	// Shopgruppe shopgruppe = new Shopgruppe(dto.getIId(), dto.getCNr(),
	// dto.getMandantCNr());
	//
	// setShopgruppeFromShopgruppeDto(shopgruppe, dto);
	// if (dto.getShopgruppesprDto() != null) {
	// Shopgruppespr spr = new Shopgruppespr(
	// theClientDto.getLocUiAsString(), dto.getIId());
	// em.persist(spr);
	// em.flush();
	// setShopgruppesprFromShoprguppesprDto(spr,
	// dto.getShopgruppesprDto());
	// }
	// return dto.getIId();
	// } catch (EntityExistsException e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
	// }
	// }
	//

	public Integer createShopgruppe(ShopgruppeDto dto, TheClientDto theClientDto) {
		existsShopgruppe(dto, theClientDto);

		ShopgruppeISort iSorter = new ShopgruppeISort(em,
				theClientDto.getMandant());
		dto.setISort(iSorter.getNextISort());
		return createShopgruppeImpl(dto, theClientDto);
	}

	public Integer createShopgruppeVor(ShopgruppeDto dto, Integer vorIId,
			TheClientDto theClientDto) {
		if (vorIId == null)
			return createShopgruppe(dto, theClientDto);

		existsShopgruppe(dto, theClientDto);
		ShopgruppeISort iSorter = new ShopgruppeISort(em,
				theClientDto.getMandant());
		dto.setISort(iSorter.getPreviousISort(vorIId));
		return createShopgruppeImpl(dto, theClientDto);
	}

	private void existsShopgruppe(ShopgruppeDto dto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("ShopgruppefindByCNrMandantCNr");
			query.setParameter(1, dto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Shopgruppe doppelt = (Shopgruppe) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_SHOPGRUPPE.CNR (" + dto.getCNr() + ")"));
		} catch (NoResultException ex) {
		}
	}

	protected Integer createShopgruppeImpl(ShopgruppeDto dto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		dto.setMandantCNr(theClientDto.getMandant());
		dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		dto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SHOPGRUPPE);
			dto.setIId(pk);

			Shopgruppe shopgruppe = new Shopgruppe(dto.getIId(), dto.getCNr(),
					dto.getMandantCNr());

			setShopgruppeFromShopgruppeDto(shopgruppe, dto);
			if (dto.getShopgruppesprDto() != null) {
				Shopgruppespr spr = new Shopgruppespr(
						theClientDto.getLocUiAsString(), dto.getIId());
				em.persist(spr);
				em.flush();
				setShopgruppesprFromShoprguppesprDto(spr,
						dto.getShopgruppesprDto());
			}
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeShopgruppe(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em
					.createNamedQuery("ShopgruppesprfindByShopgruppeIId");
			query.setParameter(1, iId);
			Collection<?> allArtklaspr = query.getResultList();
			Iterator<?> iter = allArtklaspr.iterator();
			while (iter.hasNext()) {
				Shopgruppespr artklasprTemp = (Shopgruppespr) iter.next();
				em.remove(artklasprTemp);
			}
			Shopgruppe artkla = em.find(Shopgruppe.class, iId);
			if (artkla != null) {
				em.remove(artkla);
				em.flush();
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

	}

	public void updateShopgruppe(ShopgruppeDto shopgruppeDto,
			TheClientDto theClientDto) {

		if (shopgruppeDto.getIId().equals(shopgruppeDto.getShopgruppeIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception(
							"shopgruppeDto.getIId().equals(shopgruppeDto.getShopgruppeIId())"));

		}
		Integer iId = shopgruppeDto.getIId();

		Shopgruppe artkla = null;

		artkla = em.find(Shopgruppe.class, iId);

		try {
			Query query = em.createNamedQuery("ShopgruppefindByCNrMandantCNr");
			query.setParameter(1, shopgruppeDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Shopgruppe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_SHOPGRUPPE.CNR"));
			}
		} catch (NoResultException ex) {

		}
		try {
			shopgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			shopgruppeDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			setShopgruppeFromShopgruppeDto(artkla, shopgruppeDto);

			if (shopgruppeDto.getShopgruppesprDto() != null) {

				Shopgruppespr artklaspr = em.find(Shopgruppespr.class,
						new ShopgruppesprPK(theClientDto.getLocUiAsString(),
								iId));

				if (artklaspr == null) {
					artklaspr = new Shopgruppespr(
							theClientDto.getLocUiAsString(), iId);
					em.persist(artklaspr);
					em.flush();

				}
				setShopgruppesprFromShoprguppesprDto(artklaspr,
						shopgruppeDto.getShopgruppesprDto());

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ArtikelDto getErsatzartikel(Integer artikelIId,
			TheClientDto theClientDto) {
		// try {

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei getERsatzartikel. Es gibt keinen Artikel mit der iid "
							+ artikelIId);
		}
		if (artikel.getArtikelIIdErsatz() != null) {
			return getErsatzartikel(artikel.getArtikelIIdErsatz(), theClientDto);
		} else {
			return artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtklaDto[] artklaFindAll(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("ArtklafindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		// try {
		ArtklaDto[] artklaDto = assembleArtklaDtos(cl);
		// }
		// catch (FinderException e) {
		// }
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

		return artklaDto;
	}

	public ArtklaDto[] artklaFindByMandantCNr(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtklafindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		// try {
		ArtklaDto[] artklaDto = assembleArtklaDtos(cl);
		// }
		// catch (FinderException e) {
		// }
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

		return artklaDto;
	}

	public ArtklaDto artklaFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Artkla artkla = em.find(Artkla.class, iId);
		if (artkla == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelklaFindByPrimaryKey. Keine Artikelkla mit iid "
							+ iId);
		}
		ArtklaDto artklaDto = assembleArtklaDto(artkla);
		ArtklasprDto artklasprDto = null;
		Artklaspr artklaspr = em.find(Artklaspr.class, new ArtklasprPK(
				theClientDto.getLocUiAsString(), iId));
		if (artklaspr != null) {
			artklasprDto = assembleArtklasprDto(artklaspr);
		}
		if (artklasprDto == null) {
			String konzernsprache = theClientDto.getLocKonzernAsString();
			artklaspr = em.find(Artklaspr.class, new ArtklasprPK(
					konzernsprache, iId));
			if (artklaspr != null) {
				artklasprDto = assembleArtklasprDto(artklaspr);
			}
		}

		artklaDto.setArtklasprDto(artklasprDto);
		return artklaDto;
	}

	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {

		Shopgruppe shopgruppe = em.find(Shopgruppe.class, iId);

		ShopgruppeDto shopgruppeDto = ShopgruppeDtoAssembler
				.createDto(shopgruppe);
		ShopgruppesprDto shopgruppesprDto = null;
		Shopgruppespr shopgruppespr = em.find(Shopgruppespr.class,
				new ShopgruppesprPK(theClientDto.getLocUiAsString(), iId));
		if (shopgruppespr != null) {
			shopgruppesprDto = ShopgruppesprDtoAssembler
					.createDto(shopgruppespr);
		}
		if (shopgruppesprDto == null) {
			String konzernsprache = theClientDto.getLocKonzernAsString();
			shopgruppespr = em.find(Shopgruppespr.class, new ShopgruppesprPK(
					konzernsprache, iId));
			if (shopgruppespr != null) {
				shopgruppesprDto = ShopgruppesprDtoAssembler
						.createDto(shopgruppespr);
			}
		}

		shopgruppeDto.setShopgruppesprDto(shopgruppesprDto);
		return shopgruppeDto;
	}

	private void setArtklaFromArtklaDto(Artkla artkla, ArtklaDto artklaDto) {
		artkla.setCNr(artklaDto.getCNr());
		artkla.setArtklaIId(artklaDto.getArtklaIId());
		artkla.setBTops(artklaDto.getBTops());
		em.merge(artkla);
		em.flush();
	}

	private void setShopgruppeFromShopgruppeDto(Shopgruppe shopgruppe,
			ShopgruppeDto shopgruppeDto) {
		shopgruppe.setCNr(shopgruppeDto.getCNr());
		shopgruppe.setShopgruppeIId(shopgruppeDto.getShopgruppeIId());
		shopgruppe.setArtikelIId(shopgruppeDto.getArtikelIId());
		shopgruppe.setTAnlegen(shopgruppeDto.getTAnlegen());
		shopgruppe.setTAendern(shopgruppeDto.getTAendern());
		shopgruppe.setPersonalIIdAendern(shopgruppeDto.getPersonalIIdAendern());
		shopgruppe.setPersonalIIdAnlegen(shopgruppeDto.getPersonalIIdAnlegen());
		shopgruppe.setISort(shopgruppeDto.getISort());

		em.merge(shopgruppe);
		em.flush();
	}

	private ArtklaDto assembleArtklaDto(Artkla artkla) {
		return ArtklaDtoAssembler.createDto(artkla);
	}

	private ArtklaDto[] assembleArtklaDtos(Collection<?> artklas) {
		List<ArtklaDto> list = new ArrayList<ArtklaDto>();
		if (artklas != null) {
			Iterator<?> iterator = artklas.iterator();
			while (iterator.hasNext()) {
				Artkla artkla = (Artkla) iterator.next();
				list.add(assembleArtklaDto(artkla));
			}
		}
		ArtklaDto[] returnArray = new ArtklaDto[list.size()];
		return (ArtklaDto[]) list.toArray(returnArray);
	}

	public Integer createWebshop(WebshopDto dto) {

		try {
			Query query = em
					.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Webshop doppelt = (Webshop) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_WEBSHOP.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOP);
			dto.setIId(pk);

			Webshop bean = new Webshop(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setWebshopFromIsWebshopDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createArtikelshopgruppe(ArtikelshopgruppeDto dto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("ArtikelshopgruppefindByArtikelIIdShopgruppeIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getShopgruppeIId());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELSHOPGRUPPE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		dto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELSHOPGRUPPE);
			dto.setIId(pk);

			Artikelshopgruppe bean = new Artikelshopgruppe(dto.getIId(),
					dto.getArtikelIId(), dto.getShopgruppeIId());
			setArtikelshopgruppeFromArtikelshopgruppeDto(bean, dto);
			// em.persist(bean);
			// em.flush();
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createArtikelallergen(ArtikelalergenDto dto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("ArtikelalergenfindByArtikelIIdAlergenIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getAlergenIId());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELALERGEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELALERGEN);
			dto.setIId(pk);

			Artikelalergen bean = new Artikelalergen(dto.getIId(),
					dto.getArtikelIId(), dto.getAlergenIId());
			setArtikelalergenFromArtikelalergenDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ArtikelshopgruppeDto artikelshopgruppeFindByPrimaryKey(Integer iId) {
		Artikelshopgruppe ialle = em.find(Artikelshopgruppe.class, iId);
		return ArtikelshopgruppeDtoAssembler.createDto(ialle);
	}

	public ArtikelalergenDto artikelallergenFindByPrimaryKey(Integer iId) {
		Artikelalergen ialle = em.find(Artikelalergen.class, iId);
		return ArtikelalergenDtoAssembler.createDto(ialle);
	}

	public WebshopDto webshopFindByPrimaryKey(Integer iId) {
		Webshop ialle = em.find(Webshop.class, iId);
		return WebshopDtoAssembler.createDto(ialle);
	}

	public void updateWebshop(WebshopDto dto) {
		Webshop ialle = em.find(Webshop.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Webshop) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_WEBSHOP.UK"));
			}
		} catch (NoResultException ex) {

		}

		setWebshopFromIsWebshopDto(ialle, dto);
	}

	public void updateArtikelshopgruppe(ArtikelshopgruppeDto dto,
			TheClientDto theClientDto) {
		Artikelshopgruppe ialle = em
				.find(Artikelshopgruppe.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("ArtikelshopgruppefindByArtikelIIdShopgruppeIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getShopgruppeIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artikelshopgruppe) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELSHOPGRUPPE.UK"));
			}
		} catch (NoResultException ex) {

		}

		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setArtikelshopgruppeFromArtikelshopgruppeDto(ialle, dto);
	}

	public void updateArtikelallergen(ArtikelalergenDto dto,
			TheClientDto theClientDto) {
		Artikelalergen ialle = em.find(Artikelalergen.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("ArtikelalergenfindByArtikelIIdAlergenIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getAlergenIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artikelalergen) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELALERGEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setArtikelalergenFromArtikelalergenDto(ialle, dto);
	}

	public void removeWebshop(WebshopDto dto) {
		Webshop toRemove = em.find(Webshop.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeArtikelshopgruppe(ArtikelshopgruppeDto dto) {
		Artikelshopgruppe toRemove = em.find(Artikelshopgruppe.class,
				dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeArtikelallergen(ArtikelalergenDto dto) {
		Artikelalergen toRemove = em.find(Artikelalergen.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setWebshopFromIsWebshopDto(Webshop bean, WebshopDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setArtikelshopgruppeFromArtikelshopgruppeDto(
			Artikelshopgruppe bean, ArtikelshopgruppeDto dto) {
		bean.setShopgruppeIId(dto.getShopgruppeIId());
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());
		bean.setTAendern(dto.getTAendern());
		bean.setTAnlegen(dto.getTAnlegen());

		em.merge(bean);
		em.flush();
	}

	private void setArtikelalergenFromArtikelalergenDto(Artikelalergen bean,
			ArtikelalergenDto dto) {
		bean.setAlergenIId(dto.getAlergenIId());
		bean.setArtikelIId(dto.getArtikelIId());

		em.merge(bean);
		em.flush();
	}

	public Integer createArtgru(ArtgruDto artgruDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artgruDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artgruDto == null"));
		}
		if (artgruDto.getCNr() == null || artgruDto.getBRueckgabe() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artgruDto.getCNr() == null || artgruDto.getBRueckgabe() == null"));
		}

		artgruDto.setMandantCNr(theClientDto.getMandant());

		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.

			Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
			query.setParameter(1, artgruDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Artgru doppelt = (Artgru) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTGRU.CNR"));
		} catch (NoResultException ex) {

		}

		artgruDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		artgruDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artgruDto
				.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		artgruDto
				.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELGRUPPE);
			artgruDto.setIId(pk);
			Artgru artgru = new Artgru(artgruDto.getIId(), artgruDto.getCNr(),
					artgruDto.getBRueckgabe(), artgruDto.getMandantCNr(),
					artgruDto.getBZertifizierung(),
					artgruDto.getPersonalIIdAnlegen(),
					artgruDto.getPersonalIIdAendern(), artgruDto.getTAnlegen(),
					artgruDto.getTAendern(),
					artgruDto.getBKeinevkwarnmeldungimls());
			em.persist(artgru);
			em.flush();
			setArtgruFromArtgruDto(artgru, artgruDto);
			if (artgruDto.getArtgrusprDto() != null) {
				Artgruspr artgruspr = new Artgruspr(
						theClientDto.getLocUiAsString(), artgruDto.getIId());
				em.persist(artgruspr);
				em.flush();
				setArtgrusprFromArtgrusprDto(artgruspr,
						artgruDto.getArtgrusprDto());
			}
			return artgruDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeArtgru(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("ArtgrusprfindByArtgruIId");
			query.setParameter(1, iId);
			Collection<?> allArtgruspr = query.getResultList();
			Iterator<?> iter = allArtgruspr.iterator();
			while (iter.hasNext()) {
				Artgruspr artgrusprTemp = (Artgruspr) iter.next();
				em.remove(artgrusprTemp);
			}
			Artgru artgru = em.find(Artgru.class, iId);
			if (artgru == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtgru. Es gibt keine Artgru mit der iid "
								+ iId);
			}
			em.remove(artgru);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void erzeugeTrumphTopsLogeintrag(TrumphtopslogDto ttlogDto) {
		// Fehler
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
		ttlogDto.setIId(pk);

		try {
			Trumphtopslog trumphtopslog = new Trumphtopslog(ttlogDto.getIId(),
					ttlogDto.getCImportfilename(), ttlogDto.getCError(),
					ttlogDto.getArtikelIId(), ttlogDto.getArtikelIIdMaterial(),
					ttlogDto.getNGewicht(), ttlogDto.getNGestpreisneu(),
					ttlogDto.getIBearbeitungszeit());
			em.persist(trumphtopslog);
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex2);
		}

	}

	public void updateArtgru(ArtgruDto artgruDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artgruDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artgruDto == null"));
		}
		if (artgruDto.getIId() == null || artgruDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artgruDto.getIId() == null || artgruDto.getCNr() == null"));
		}
		if (artgruDto.getIId().equals(artgruDto.getArtgruIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception(
							"artgruDto.getIId() == artgruDto.getArtgruIId()"));

		}
		Integer iId = artgruDto.getIId();
		Artgru artgru = null;
		// try {
		artgru = em.find(Artgru.class, iId);
		if (artgru == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtgru. Es gibt keine Artgru mit iid "
							+ iId);

		}

		// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
		try {
			Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
			query.setParameter(1, artgruDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artgru) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTGRU.CNR"));

			}
		} catch (NoResultException ex) {

		}

		artgruDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artgruDto
				.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		setArtgruFromArtgruDto(artgru, artgruDto);
		try {
			if (artgruDto.getArtgrusprDto() != null) {
				try {
					Artgruspr artgruspr = em.find(Artgruspr.class,
							new ArtgrusprPK(theClientDto.getLocUiAsString(),
									iId));
					if (artgruspr == null) {
						artgruspr = new Artgruspr(
								theClientDto.getLocUiAsString(), iId);
						em.persist(artgruspr);
						em.flush();
					}
					setArtgrusprFromArtgrusprDto(artgruspr,
							artgruDto.getArtgrusprDto());

				} catch (NoResultException ex) {
					Artgruspr artgruspr = new Artgruspr(
							theClientDto.getLocUiAsString(), iId);
					em.persist(artgruspr);
					setArtgrusprFromArtgrusprDto(artgruspr,
							artgruDto.getArtgrusprDto());
				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ArtgruDto getLetzteVatergruppe(Integer artgruIId) {
		// try {
		Artgru artgru = em.find(Artgru.class, artgruIId);
		if (artgru == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei getLetzteVatergruppe. Es gibt keine Artgru mit iid "
							+ artgruIId);
		}
		if (artgru.getArtgruIId() != null) {
			return getLetzteVatergruppe(artgru.getArtgruIId());
		} else {
			return assembleArtgruDto(artgru);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public ArtklaDto getLetzteVaterklasse(Integer artklaIId) {
		// try {
		Artkla artkla = em.find(Artkla.class, artklaIId);
		if (artkla == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei getLetzteVaterklasse. Es gibt keine artklasse mit iid "
							+ artklaIId);
		}
		if (artkla.getArtklaIId() != null) {
			return getLetzteVaterklasse(artkla.getArtklaIId());
		} else {
			return assembleArtklaDto(artkla);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public ArtgruDto artgruFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artgru artgru = em.find(Artgru.class, iId);
		if (artgru == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artgru FindByPrimaryKey. Es gibt keine Artikelgruppe mit iid "
							+ iId);
		}
		ArtgruDto artgruDto = assembleArtgruDto(artgru);
		ArtgrusprDto artgrusprDto = null;
		// try {
		Artgruspr artgruspr = em.find(Artgruspr.class, new ArtgrusprPK(
				theClientDto.getLocUiAsString(), iId));
		if (artgruspr != null) {
			artgrusprDto = assembleArtgrusprDto(artgruspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artgrusprDto == null) {
			// try {
			artgruspr = em.find(Artgruspr.class,
					new ArtgrusprPK(theClientDto.getLocKonzernAsString(), iId));
			if (artgruspr != null) {
				artgrusprDto = assembleArtgrusprDto(artgruspr);
			}
			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artgruDto.setArtgrusprDto(artgrusprDto);
		return artgruDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtgruDto[] artgruFindByMandantCNr(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtgrufindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		ArtgruDto[] artgruDto = assembleArtgruDtos(cl);
		return artgruDto;
	}

	public List<ArtgruDto> artgruFindByMandantCNrSpr(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtgrufindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<Artgru> cl = query.getResultList();
		List<ArtgruDto> dtos = new ArrayList<ArtgruDto>();

		for (Artgru artgru : cl) {
			dtos.add(artgruFindByPrimaryKey(artgru.getIId(), theClientDto));
		}
		return dtos;
	}

	public ArtgruDto[] artgruFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ArtgrufindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		ArtgruDto[] artgruDto = assembleArtgruDtos(cl);
		return artgruDto;

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setArtgruFromArtgruDto(Artgru artgru, ArtgruDto artgruDto) {
		artgru.setCNr(artgruDto.getCNr());
		artgru.setArtgruIId(artgruDto.getArtgruIId());
		artgru.setKontoIId(artgruDto.getKontoIId());
		artgru.setBRueckgabe(artgruDto.getBRueckgabe());
		artgru.setBZertifizierung(artgruDto.getBZertifizierung());
		artgru.setPersonalIIdAendern(artgruDto.getPersonalIIdAendern());
		artgru.setPersonalIIdAnlegen(artgruDto.getPersonalIIdAnlegen());
		artgru.setTAendern(artgruDto.getTAendern());
		artgru.setTAnlegen(artgruDto.getTAnlegen());
		artgru.setBKeinevkwarnmeldungimls(artgruDto
				.getBKeinevkwarnmeldungimls());
		em.merge(artgru);
		em.flush();
	}

	private ArtgruDto assembleArtgruDto(Artgru artgru) {
		return ArtgruDtoAssembler.createDto(artgru);
	}

	private ArtgruDto[] assembleArtgruDtos(Collection<?> artgrus) {
		List<ArtgruDto> list = new ArrayList<ArtgruDto>();
		if (artgrus != null) {
			Iterator<?> iterator = artgrus.iterator();
			while (iterator.hasNext()) {
				Artgru artgru = (Artgru) iterator.next();
				list.add(assembleArtgruDto(artgru));
			}
		}
		ArtgruDto[] returnArray = new ArtgruDto[list.size()];
		return (ArtgruDto[]) list.toArray(returnArray);
	}

	public Integer createKatalog(KatalogDto katalogDto) throws EJBExceptionLP {
		myLogger.entry();
		if (katalogDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("katalogDto == null"));
		}
		if (katalogDto.getArtikelIId() == null
				|| katalogDto.getCSeite() == null
				|| katalogDto.getCKatalog() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, katalogDto.getArtikelIId());
			query.setParameter(2, katalogDto.getCKatalog());
			// @todo getSingleResult oder getResultList ?
			Katalog doppelt = (Katalog) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_KATALOG.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KATALOG);
			katalogDto.setIId(pk);
			Katalog katalog = new Katalog(katalogDto.getIId(),
					katalogDto.getArtikelIId(), katalogDto.getCKatalog());
			em.persist(katalog);
			em.flush();
			setKatalogFromKatalogDto(katalog, katalogDto);
			return katalogDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeKatalog(KatalogDto dto) throws EJBExceptionLP {
		myLogger.entry();
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}
		// try {
		Katalog toRemove = em.find(Katalog.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeKatalog. Es gibt keinen Katalog mit iid "
							+ dto.getIId() + "\nKatalogDto.toString(): "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void updateKatalog(KatalogDto katalogDto) throws EJBExceptionLP {
		myLogger.entry();
		if (katalogDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("katalogDto == null"));
		}
		if (katalogDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("katalogDto.getIId() == null"));
		}
		if (katalogDto.getArtikelIId() == null
				|| katalogDto.getCSeite() == null
				|| katalogDto.getCKatalog() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null"));
		}
		// try {
		Integer iId = katalogDto.getIId();
		Katalog katalog = em.find(Katalog.class, iId);
		if (katalog == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateKatalog. Es gibt keinen Katalog mit iid "
							+ iId + "\n KAtalogDto.toString(): "
							+ katalogDto.toString());
		}

		try {
			Query query = em
					.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, katalogDto.getArtikelIId());
			query.setParameter(2, katalogDto.getCKatalog());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Katalog) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_KATALOG.UK"));
			}

		} catch (NoResultException ex) {

		}
		setKatalogFromKatalogDto(katalog, katalogDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public KatalogDto katalogFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Katalog katalog = em.find(Katalog.class, iId);
		if (katalog == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei KatalogFindByPrimaryKey. Es gibt keinen Katalog mit iid "
							+ iId);
		}
		return assembleKatalogDto(katalog);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public Integer createEinkaufsean(EinkaufseanDto einkaufseanDto) {
		if (einkaufseanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("katalogDto == null"));
		}
		if (einkaufseanDto.getArtikelIId() == null
				|| einkaufseanDto.getCEan() == null
				|| einkaufseanDto.getNMenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null || einkaufseanDto.getNMenge() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("EinkaufseanfindByArtikelIIdNMenge");
			query.setParameter(1, einkaufseanDto.getArtikelIId());
			query.setParameter(2, einkaufseanDto.getNMenge());
			// @todo getSingleResult oder getResultList ?
			Einkaufsean doppelt = (Einkaufsean) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_EINKAUFSEAN.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EINKAUFSEAN);
			einkaufseanDto.setIId(pk);
			Einkaufsean einkaufsean = new Einkaufsean(einkaufseanDto.getIId(),
					einkaufseanDto.getArtikelIId(), einkaufseanDto.getCEan(),
					einkaufseanDto.getNMenge());
			em.persist(einkaufsean);
			em.flush();
			setEinkaufseanFromEinkaufseanDto(einkaufsean, einkaufseanDto);
			return einkaufseanDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeEinkaufsean(EinkaufseanDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Einkaufsean toRemove = em.find(Einkaufsean.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei remove Einkaufsean. Es gibt keine iid "
							+ dto.getIId() + "\neimnkaufseanDto.toString(): "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateEinkaufsean(EinkaufseanDto einkaufseanDto) {

		if (einkaufseanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufseanDto == null"));
		}
		if (einkaufseanDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufseanDto.getIId() == null"));
		}
		if (einkaufseanDto.getArtikelIId() == null
				|| einkaufseanDto.getCEan() == null
				|| einkaufseanDto.getNMenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null || einkaufseanDto.getNMenge() == null"));
		}

		Integer iId = einkaufseanDto.getIId();
		Einkaufsean einkaufsean = em.find(Einkaufsean.class, iId);
		if (einkaufsean == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsean. Es gibt keine iid " + iId
							+ "\nEinkaufseanDto.toString(): "
							+ einkaufseanDto.toString());
		}

		try {
			Query query = em
					.createNamedQuery("EinkaufseanfindByArtikelIIdNMenge");
			query.setParameter(1, einkaufseanDto.getArtikelIId());
			query.setParameter(2, einkaufseanDto.getNMenge());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Einkaufsean) query.getSingleResult())
					.getIId();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_EINKAUFSEAN.UK"));

		} catch (NoResultException ex) {

		}
		setEinkaufseanFromEinkaufseanDto(einkaufsean, einkaufseanDto);

	}

	public EinkaufseanDto einkaufseanFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Einkaufsean einkaufsean = em.find(Einkaufsean.class, iId);
		if (einkaufsean == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei einkaufseanFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleEinkaufseanDto(einkaufsean);

	}

	public EinkaufseanDto einkaufseanFindByCEan(String cEan) {
		if (cEan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cEan == null"));
		}

		Query query = em.createNamedQuery("EinkaufseanfindByCEan");
		query.setParameter(1, cEan);

		Einkaufsean einkaufsean;
		try {
			einkaufsean = (Einkaufsean) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

		return assembleEinkaufseanDto(einkaufsean);

	}

	private void setEinkaufseanFromEinkaufseanDto(Einkaufsean einkaufsean,
			EinkaufseanDto einkaufseanDto) {
		einkaufsean.setCEan(einkaufseanDto.getCEan());
		einkaufsean.setArtikelIId(einkaufseanDto.getArtikelIId());
		einkaufsean.setNMenge(einkaufseanDto.getNMenge());
		em.merge(einkaufsean);
		em.flush();
	}

	private EinkaufseanDto assembleEinkaufseanDto(Einkaufsean einkaufsean) {
		return EinkaufseanDtoAssembler.createDto(einkaufsean);
	}

	public KatalogDto katalogFindByArtikelIIdCKatalog(Integer iId,
			String cKatalog) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		if (cKatalog == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cKatalog == null"));
		}

		try {
			Query query = em
					.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, iId);
			query.setParameter(2, cKatalog);
			Katalog katalog = (Katalog) query.getSingleResult();
			return assembleKatalogDto(katalog);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei katalogFindByArtikelIIdCKatalog es gibt mehr als ein ERgebnis f\u00FCr ArtikelIId "
							+ iId + " KatalogCnr " + cKatalog);
		}
	}

	/**
	 * Prueft eine Artikelnummer auf erlaubte Zeichen. Erlaubt sind :
	 * A-Z,0-9,OE,UE,AE,&szlig;,_,- Blank ist nur als Fueller zwischen
	 * Artikelnummer und Herstellerkennung erlaubt
	 * 
	 * @param cNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	private void pruefeArtikelnummer(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cNr == null"));
		}

		Integer iMaxLaenge = null;
		Integer iMinLaenge = null;
		String gueltigeZeichen = "";
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_ARTIKELNUMMER);
			iMinLaenge = (Integer) parameter.getCWertAsObject();

			if (cNr.length() < iMinLaenge.intValue()) {
				ArrayList<Object> al = new ArrayList<Object>();
				if (iMinLaenge != null) {
					al.add(iMinLaenge);
					al.add(cNr);
				}
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ,
						al, new Exception(
								"FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ"));
			}

			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);

			iMaxLaenge = (Integer) parameter.getCWertAsObject();

			boolean bHerstellerkopplung = getMandantFac()
					.hatZusatzfunktionberechtigung(
							com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG,
							theClientDto);

			int iLaengeHersteller = 0;
			int iLetztesZeichenVorDerHerstellerNummer = 0;
			if (bHerstellerkopplung) {

				parameter = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG);

				iMaxLaenge = iMaxLaenge
						+ (Integer) parameter.getCWertAsObject();
				iLaengeHersteller = (Integer) parameter.getCWertAsObject();

				for (int k = 0; k < cNr.length(); k++) {
					char ck = cNr.charAt(k);

					if (ck == ' ') {
						iLetztesZeichenVorDerHerstellerNummer = k - 1;
						break;
					}

				}

			}

			if (cNr.length() > iMaxLaenge.intValue()) {
				ArrayList al = new ArrayList();
				al.add(cNr);
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG,
						al, new Exception(
								"FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG"));
			}

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELNUMMER_ZEICHENSATZ);

			gueltigeZeichen = parameter.getCWert();

			for (int i = 0; i < cNr.length(); i++) {
				boolean bErlaubt = false;
				char c = cNr.charAt(i);

				for (int j = 0; j < gueltigeZeichen.length(); j++) {
					if (c == gueltigeZeichen.charAt(j)) {
						bErlaubt = true;
						break;
					}
				}

				if (bErlaubt == false) {

					// Wenns ein Leerzeichen zwischen Artikelnummer und
					// Herstellernumer ist, dann ists erlaubt
					if (bHerstellerkopplung && c == ' ') {

						if (i > iLetztesZeichenVorDerHerstellerNummer
								&& i < (iMaxLaenge - iLaengeHersteller)) {
							continue;
						}

					}

					ArrayList<Object> l = new ArrayList<Object>();
					l.add(new Character(c));
					l.add(cNr);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT,
							l,
							new Exception(
									"FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT"));
				}

			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private void setKatalogFromKatalogDto(Katalog katalog, KatalogDto katalogDto) {
		katalog.setCSeite(katalogDto.getCSeite());
		katalog.setArtikelIId(katalogDto.getArtikelIId());
		katalog.setCKatalog(katalogDto.getCKatalog());
		em.merge(katalog);
		em.flush();
	}

	private KatalogDto assembleKatalogDto(Katalog katalog) {
		return KatalogDtoAssembler.createDto(katalog);
	}

	public String getHerstellercode(Integer partnerIId,
			TheClientDto theClientDto) {

		try {

			Query ejbquery = em.createNamedQuery("HerstellerfindByPartnerIId");
			ejbquery.setParameter(1, partnerIId);

			HerstellerDto[] dtos = assembleHerstellerDtos(ejbquery
					.getResultList());

			if (dtos.length == 1) {
				return dtos[0].getCNr();
			}

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG);
			Integer iLaenge = (Integer) parameter.getCWertAsObject();

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					partnerIId, theClientDto);

			String buchstabe = partnerDto.getCName1nachnamefirmazeile1()
					.substring(0, 1).toUpperCase();

			Session session = FLRSessionFactory.getFactory().openSession();

			String query = "FROM FLRHersteller h WHERE h.c_nr LIKE '"
					+ buchstabe + "%' ORDER BY C_NR DESC";
			org.hibernate.Query herst = session.createQuery(query);

			herst.setMaxResults(1);

			List subResults = herst.list();

			if (subResults.iterator().hasNext()) {
				com.lp.server.artikel.fastlanereader.generated.FLRHersteller h = (com.lp.server.artikel.fastlanereader.generated.FLRHersteller) subResults
						.iterator().next();
				String alteNummer = h.getC_nr();

				Integer neueNummer;
				try {
					neueNummer = new Integer(alteNummer.substring(1)) + 1;
				} catch (NumberFormatException e) {
					return null;
				}

				if ((neueNummer + "").length() > iLaenge - 1) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_HERSTELLERKUERZEL_OVERFLOW,
							new Exception("FEHLER_HERSTELLERKUERZEL_OVERFLOW"));
				}

				return buchstabe
						+ Helper.fitString2LengthAlignRight(neueNummer + "",
								iLaenge - 1, '0');

			} else {
				return buchstabe
						+ Helper.fitString2LengthAlignRight("0", iLaenge - 1,
								'0');
			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		return null;

	}

	private KatalogDto[] assembleKatalogDtos(Collection<?> katalogs) {
		List<KatalogDto> list = new ArrayList<KatalogDto>();
		if (katalogs != null) {
			Iterator<?> iterator = katalogs.iterator();
			while (iterator.hasNext()) {
				Katalog katalog = (Katalog) iterator.next();
				list.add(assembleKatalogDto(katalog));
			}
		}
		KatalogDto[] returnArray = new KatalogDto[list.size()];
		return (KatalogDto[]) list.toArray(returnArray);
	}

	private void setArtgrusprFromArtgrusprDto(Artgruspr artgruspr,
			ArtgrusprDto artgrusprDto) {
		artgruspr.setCBez(artgrusprDto.getCBez());
		em.merge(artgruspr);
		em.flush();
	}

	private ArtgrusprDto assembleArtgrusprDto(Artgruspr artgruspr) {
		return ArtgrusprDtoAssembler.createDto(artgruspr);
	}

	private ArtgrusprDto[] assembleArtgrusprDtos(Collection<?> artgrusprs) {
		List<ArtgrusprDto> list = new ArrayList<ArtgrusprDto>();
		if (artgrusprs != null) {
			Iterator<?> iterator = artgrusprs.iterator();
			while (iterator.hasNext()) {
				Artgruspr artgruspr = (Artgruspr) iterator.next();
				list.add(assembleArtgrusprDto(artgruspr));
			}
		}
		ArtgrusprDto[] returnArray = new ArtgrusprDto[list.size()];
		return (ArtgrusprDto[]) list.toArray(returnArray);
	}

	private void setVerpackungFromVerpackungDto(Verpackung verpackung,
			VerpackungDto verpackungDto) {
		verpackung.setCBauform(verpackungDto.getCBauform());
		verpackung.setCVerpackungsart(verpackungDto.getCVerpackungsart());
		em.merge(verpackung);
		em.flush();
	}

	private VerpackungDto assembleVerpackungDto(Verpackung verpackung) {
		return VerpackungDtoAssembler.createDto(verpackung);
	}

	private VerpackungDto[] assembleVerpackungDtos(Collection<?> verpackungs) {
		List<VerpackungDto> list = new ArrayList<VerpackungDto>();
		if (verpackungs != null) {
			Iterator<?> iterator = verpackungs.iterator();
			while (iterator.hasNext()) {
				Verpackung verpackung = (Verpackung) iterator.next();
				list.add(assembleVerpackungDto(verpackung));
			}
		}
		VerpackungDto[] returnArray = new VerpackungDto[list.size()];
		return (VerpackungDto[]) list.toArray(returnArray);
	}

	private void pruefeObEndlosschleifeErsatzartikel(Integer artikelIId,
			Integer artikelIId_Ersatz) {
		// try {
		Artikel artikel = em.find(Artikel.class, artikelIId_Ersatz);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei pruefeObEndlosschleifeErsatzartikel Es gibt keinen Ersatzartikel. iId: "
							+ artikelIId_Ersatz);
		}
		Integer artikelIId_ErsatzNaechsteEbene = artikel.getArtikelIIdErsatz();
		if (artikelIId_ErsatzNaechsteEbene != null) {
			if (artikelIId_ErsatzNaechsteEbene.equals(artikelIId)) {
				// DEADLOCK
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK,
						new Exception("FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK"));

			} else {
				pruefeObEndlosschleifeErsatzartikel(artikelIId,
						artikelIId_ErsatzNaechsteEbene);
			}

		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void artikeleigenschaftenDefaultwerteAnlegen(Integer artikelIId,
			Integer artgruIId, TheClientDto theClientDto) {

		ArrayList<PaneldatenDto> alDaten = new ArrayList<PaneldatenDto>();
		PanelbeschreibungDto[] pbesDtos = getPanelFac()
				.panelbeschreibungFindByPanelCNrMandantCNr(
						PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
						theClientDto.getMandant(), artgruIId);
		for (int i = 0; i < pbesDtos.length; i++) {

			if (pbesDtos[i].getCDefault() != null) {
				PaneldatenDto pdDto = new PaneldatenDto();
				pdDto.setPanelCNr(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN);
				pdDto.setCKey(artikelIId + "");
				pdDto.setCDatentypkey("java.lang.String");
				if (pbesDtos[i].getCTyp().equals(PanelFac.TYP_WRAPPERCHECKBOX)) {
					pdDto.setCDatentypkey("java.lang.Short");
				}
				pdDto.setPanelbeschreibungIId(pbesDtos[i].getIId());
				pdDto.setXInhalt(pbesDtos[i].getCDefault());
				alDaten.add(pdDto);
			}

		}

		getPanelFac().createPaneldaten(
				alDaten.toArray(new PaneldatenDto[alDaten.size()]));

	}

	public void preiseAusAnfrageRueckpflegen(Integer anfrageIId,
			Integer anfragepositionlieferdatenIId, boolean bStaffelnLoeschen,
			boolean bLieferantVorreihen, TheClientDto theClientDto) {

		try {
			AnfragepositionlieferdatenDto anfposliefDto = getAnfragepositionFac()
					.anfragepositionlieferdatenFindByPrimaryKey(
							anfragepositionlieferdatenIId, theClientDto);

			AnfragepositionDto anfposDto = getAnfragepositionFac()
					.anfragepositionFindByPrimaryKey(
							anfposliefDto.getAnfragepositionIId(), theClientDto);

			com.lp.server.anfrage.service.AnfrageDto anfrageDto = getAnfrageFac()
					.anfrageFindByPrimaryKey(anfrageIId, theClientDto);

			com.lp.server.partner.service.LieferantDto lf = getLieferantFac()
					.lieferantFindByPrimaryKey(
							anfrageDto.getLieferantIIdAnfrageadresse(),
							theClientDto);

			String sQuery = "SELECT apl FROM FLRAnfragepositionlieferdaten apl WHERE apl.flranfrageposition.flrartikel.i_id="
					+ anfposDto.getArtikelIId()
					+ " AND apl.flranfrageposition.flranfrage.i_id="
					+ anfrageIId + " ORDER BY apl.n_anliefermenge ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(sQuery);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			ArtikellieferantDto alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					anfposDto.getArtikelIId(),
					anfrageDto.getLieferantIIdAnfrageadresse(),
					new java.sql.Date(anfrageDto.getTBelegdatum().getTime()),
					theClientDto);

			BigDecimal anlieferPreisInLieferantenwaehrung = null;

			if (bStaffelnLoeschen && alDto != null) {
				ArtikellieferantstaffelDto[] staffelnDto = artikellieferantstaffelFindByArtikellieferantIId(alDto
						.getIId());
				for (int i = 0; i < staffelnDto.length; i++) {
					removeArtikellieferantstaffel(staffelnDto[i].getIId());
				}

			}

			// Default Chrgennummernbehaftet
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANFRAGE,
							ParameterFac.PARAMETER_MENGEN_IN_STAFFELN_UEBERNEHMEN);

			boolean bMengenInStaffelUebernehmen = (Boolean) parameter
					.getCWertAsObject();

			int i = 0;

			while (resultListIterator.hasNext()) {
				FLRAnfragepositionlieferdaten flrAnfragepositionlieferdaten = (FLRAnfragepositionlieferdaten) resultListIterator
						.next();

				if (flrAnfragepositionlieferdaten.getN_nettogesamtpreis() != null
						&& flrAnfragepositionlieferdaten.getN_anliefermenge() != null
						&& flrAnfragepositionlieferdaten.getN_anliefermenge()
								.doubleValue() > 0
						&& flrAnfragepositionlieferdaten
								.getN_nettogesamtpreis() != null
						&& flrAnfragepositionlieferdaten
								.getN_nettogesamtpreis().doubleValue() > 0) {
					anfposliefDto = getAnfragepositionFac()
							.anfragepositionlieferdatenFindByPrimaryKey(
									flrAnfragepositionlieferdaten.getI_id(),
									theClientDto);
					if (i == 0) {

						anlieferPreisInLieferantenwaehrung = getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										anfposliefDto.getNNettogesamtpreis(),
										anfrageDto.getWaehrungCNr(),
										lf.getWaehrungCNr(),
										new Date(anfrageDto.getTBelegdatum()
												.getTime()), theClientDto);

						// Der mit der niedrigsten Menge kommt in den
						// Artikellieferant
						if (alDto != null) {

							alDto.setBRabattbehalten(Helper.boolean2Short(true));
							alDto.setFRabatt((double) 0);
							alDto.setNEinzelpreis(anlieferPreisInLieferantenwaehrung);
							alDto.setNNettopreis(anlieferPreisInLieferantenwaehrung);
							alDto.setCArtikelnrlieferant(anfposliefDto
									.getCArtikelnrlieferant());
							alDto.setCBezbeilieferant(anfposliefDto
									.getCBezbeilieferant());
							alDto.setNVerpackungseinheit(anfposliefDto
									.getNVerpackungseinheit());
							alDto.setCAngebotnummer(anfrageDto
									.getCAngebotnummer());
							alDto.setZertifikatartIId(anfposliefDto
									.getZertifikatartIId());
							alDto.setIWiederbeschaffungszeit(anfposliefDto
									.getIAnlieferzeit());

							if (anfposliefDto.getNMindestbestellmenge() != null) {
								alDto.setFMindestbestelmenge(anfposliefDto
										.getNMindestbestellmenge()
										.doubleValue());
							} else {
								alDto.setFMindestbestelmenge(null);
							}

							if (anfposliefDto.getNStandardmenge() != null) {
								alDto.setFStandardmenge(anfposliefDto
										.getNStandardmenge().doubleValue());
							} else {
								alDto.setFStandardmenge(null);
							}

							if (anfrageDto.getTAngebotdatum() != null) {
								alDto.setTPreisgueltigab(anfrageDto
										.getTAngebotdatum());
							} else {
								alDto.setTPreisgueltigab(anfrageDto
										.getTBelegdatum());
							}

							alDto.setTPreisgueltigbis(anfrageDto
									.getTAngebotgueltigbis());

							updateArtikellieferant(alDto, theClientDto);

						} else {
							alDto = new ArtikellieferantDto();
							alDto.setArtikelIId(anfposDto.getArtikelIId());
							alDto.setLieferantIId(anfrageDto
									.getLieferantIIdAnfrageadresse());
							alDto.setBRabattbehalten(Helper.boolean2Short(true));
							alDto.setFRabatt((double) 0);
							alDto.setNEinzelpreis(anlieferPreisInLieferantenwaehrung);
							alDto.setNNettopreis(anlieferPreisInLieferantenwaehrung);
							alDto.setCArtikelnrlieferant(anfposliefDto
									.getCArtikelnrlieferant());
							alDto.setCBezbeilieferant(anfposliefDto
									.getCBezbeilieferant());
							alDto.setNVerpackungseinheit(anfposliefDto
									.getNVerpackungseinheit());
							alDto.setCAngebotnummer(anfrageDto
									.getCAngebotnummer());
							if (anfrageDto.getTAngebotdatum() != null) {
								alDto.setTPreisgueltigab(anfrageDto
										.getTAngebotdatum());
							} else {
								alDto.setTPreisgueltigab(anfrageDto
										.getTBelegdatum());
							}

							if (anfrageDto.getTAngebotdatum() != null) {
								alDto.setTPreisgueltigab(anfrageDto
										.getTAngebotdatum());
							} else {
								alDto.setTPreisgueltigab(anfrageDto
										.getTBelegdatum());
							}

							alDto.setTPreisgueltigbis(anfrageDto
									.getTAngebotgueltigbis());
							Integer aLiefIId = createArtikellieferant(alDto,
									theClientDto);
							alDto.setIId(aLiefIId);
						}

					}

					if (i >= 0) {
						// Die anderen werden als Staffeln angelegt

						if ((bMengenInStaffelUebernehmen == false && i > 0)
								|| (bMengenInStaffelUebernehmen == true && anfposliefDto
										.getNAnliefermenge().doubleValue() != 1)) {

							BigDecimal staffelPreisInLieferantenwaehrung = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											anfposliefDto
													.getNNettogesamtpreis(),
											anfrageDto.getWaehrungCNr(),
											lf.getWaehrungCNr(),
											new Date(anfrageDto
													.getTBelegdatum().getTime()),
											theClientDto);

							// Rabatt berechnen
							BigDecimal rabattsatz = null;
							if (anlieferPreisInLieferantenwaehrung
									.doubleValue() != 0) {

								rabattsatz = new BigDecimal(1)
										.subtract(staffelPreisInLieferantenwaehrung
												.divide(anlieferPreisInLieferantenwaehrung,
														4,
														BigDecimal.ROUND_HALF_EVEN));
								rabattsatz = rabattsatz
										.multiply(new BigDecimal(100));

							} else {
								rabattsatz = new BigDecimal(100);
							}

							Query q = em
									.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
							q.setParameter(1, alDto.getIId());
							q.setParameter(2, flrAnfragepositionlieferdaten
									.getN_anliefermenge());
							q.setParameter(3, alDto.getTPreisgueltigab());
							Artikellieferantstaffel artikellieferantstaffel = null;
							try {
								artikellieferantstaffel = (Artikellieferantstaffel) q
										.getSingleResult();

								artikellieferantstaffel
										.setNMenge(flrAnfragepositionlieferdaten
												.getN_anliefermenge());
								artikellieferantstaffel.setFRabatt(rabattsatz
										.doubleValue());
								artikellieferantstaffel
										.setNNettopreis(staffelPreisInLieferantenwaehrung);
								artikellieferantstaffel
										.setTPreisgueltigbis(alDto
												.getTPreisgueltigbis());

								em.merge(artikellieferantstaffel);
								em.flush();

							} catch (NoResultException e) {
								ArtikellieferantstaffelDto staffelDto = new ArtikellieferantstaffelDto();
								staffelDto.setArtikellieferantIId(alDto
										.getIId());
								staffelDto.setBRabattbehalten((short) 0);
								staffelDto
										.setNMenge(flrAnfragepositionlieferdaten
												.getN_anliefermenge());
								staffelDto.setFRabatt(rabattsatz.doubleValue());
								staffelDto
										.setNNettopreis(staffelPreisInLieferantenwaehrung);
								staffelDto.setTPreisgueltigab(alDto
										.getTPreisgueltigab());
								staffelDto.setTPreisgueltigbis(alDto
										.getTPreisgueltigbis());
								createArtikellieferantstaffel(staffelDto,
										theClientDto);
							}
						}
					}

					i++;
				}
			}

			if (alDto != null && bLieferantVorreihen == true) {
				artikellieferantAlsErstesReihen(anfposDto.getArtikelIId(),
						alDto.getIId());
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void artikellieferantAlsErstesReihen(Integer artikelIId,
			Integer artikellieferantIId) {

		Artikellieferant artikellieferantZuVerschieben = em.find(
				Artikellieferant.class, artikellieferantIId);

		Query query = em.createNamedQuery("ArtikellieferantfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		int i = 0;

		Artikellieferant erster = null;

		while (iterator.hasNext()) {
			Artikellieferant artikellieferant = (Artikellieferant) iterator
					.next();

			if (i == 0 && artikellieferant.getIId().equals(artikellieferantIId)) {
				return;
			}

			if (i == 0) {
				erster = artikellieferant;
			}
			if (artikellieferant.getIId().equals(artikellieferantIId)) {

				int temp = artikellieferant.getISort();
				artikellieferant.setISort(erster.getISort());
				erster.setISort(temp);
				em.merge(erster);
				em.flush();

				em.merge(erster);
				em.flush();

			}
			i++;
		}

	}

	public Integer createArtikellieferant(
			ArtikellieferantDto artikellieferantDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikellieferantDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellieferantDto == null"));
		}
		if (artikellieferantDto.getArtikelIId() == null
				|| artikellieferantDto.getLieferantIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"artikellieferantDto.getArtikelIId() == null || artikellieferantDto.getPartnerIIdLieferant() == null"));
		}
		if (artikellieferantDto.getTPreisgueltigab() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"artikellieferantDto.getDPreisgueltigab() == null"));
		}
		artikellieferantDto.setTPreisgueltigab(Helper
				.cutTimestamp(artikellieferantDto.getTPreisgueltigab()));
		artikellieferantDto.setTPreisgueltigbis(Helper
				.cutTimestamp(artikellieferantDto.getTPreisgueltigbis()));
		artikellieferantDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikellieferantDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		try {
			// duplicateunique: Pruefung: Artikellieferant bereits vorhanden.
			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigab");
			query.setParameter(1, artikellieferantDto.getArtikelIId());
			query.setParameter(2, artikellieferantDto.getLieferantIId());
			query.setParameter(3, artikellieferantDto.getTPreisgueltigab());
			Artikellieferant doppelt = (Artikellieferant) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELLIEFERANT.UK"));
		} catch (NoResultException ex) {

		}

		// PJ 16931 Daten des vorherigen uebernehmen
		if (artikellieferantDto.getCArtikelnrlieferant() == null
				|| artikellieferantDto.getCBezbeilieferant() == null) {
			ArtikellieferantDto alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					artikellieferantDto.getArtikelIId(),
					artikellieferantDto.getLieferantIId(),
					new java.sql.Date(artikellieferantDto.getTPreisgueltigab()
							.getTime()), theClientDto);

			if (alDto != null) {
				if (artikellieferantDto.getCArtikelnrlieferant() == null) {
					artikellieferantDto.setCArtikelnrlieferant(alDto
							.getCArtikelnrlieferant());
				}
				if (artikellieferantDto.getCBezbeilieferant() == null) {
					artikellieferantDto.setCBezbeilieferant(alDto
							.getCBezbeilieferant());
				}
				if (artikellieferantDto.getNVerpackungseinheit() == null) {
					artikellieferantDto.setNVerpackungseinheit(alDto
							.getNVerpackungseinheit());
				}
				if (artikellieferantDto.getIWiederbeschaffungszeit() == null) {
					artikellieferantDto.setIWiederbeschaffungszeit(alDto
							.getIWiederbeschaffungszeit());
				}
				if (artikellieferantDto.getFMindestbestelmenge() == null) {
					artikellieferantDto.setFMindestbestelmenge(alDto
							.getFMindestbestelmenge());
				}
				if (artikellieferantDto.getFStandardmenge() == null) {
					artikellieferantDto.setFStandardmenge(alDto
							.getFStandardmenge());
				}

			}

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELLIEFERANT);
			artikellieferantDto.setIId(pk);
			Query queryNext = em
					.createNamedQuery("ArtikellieferantejbSelectNextReihung");
			queryNext.setParameter(1, artikellieferantDto.getArtikelIId());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			artikellieferantDto.setISort(i);
			Artikellieferant artikellieferant = new Artikellieferant(
					artikellieferantDto.getArtikelIId(),
					artikellieferantDto.getLieferantIId(),
					artikellieferantDto.getTPreisgueltigab(),
					artikellieferantDto.getISort(),
					artikellieferantDto.getIId(),
					artikellieferantDto.getPersonalIIdAendern(),
					artikellieferantDto.getTAendern());
			em.persist(artikellieferant);
			em.flush();

			if (artikellieferantDto.getBHerstellerbez() == null) {
				artikellieferantDto.setBHerstellerbez(artikellieferant
						.getBHerstellerbez());
			}
			if (artikellieferantDto.getBWebshop() == null) {
				artikellieferantDto.setBWebshop(artikellieferant.getBWebshop());
			}

			setArtikellieferantFromArtikellieferantDto(artikellieferant,
					artikellieferantDto);
			return artikellieferantDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

	}

	public void removeArtikellieferant(ArtikellieferantDto dto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}
		// try {
		Artikellieferant toRemove = em.find(Artikellieferant.class,
				dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikellieferant Es gibt keine iid "
							+ dto.getIId()
							+ "\nArtikellieferantDto.toString(): "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public boolean sindVorschlagstexteVorhanden() {
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRVorschlagstext.class);
		c.setMaxResults(1);
		List<?> l = c.list();

		if (l.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Zwei bestehende Artikellieferanten in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iiDLieferant1
	 *            PK des ersten Lieferanten
	 * @param iIdLieferant2
	 *            PK des zweiten Lieferanten
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheArtikellieferanten(Integer iiDLieferant1,
			Integer iIdLieferant2) throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Artikellieferant oLieferant1 = em.find(Artikellieferant.class,
				iiDLieferant1);
		if (oLieferant1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheArtikellieferanten. Es gibt keinen Lieferanten mit iid1 "
							+ iiDLieferant1);
		}

		Artikellieferant oLieferant2 = em.find(Artikellieferant.class,
				iIdLieferant2);
		if (oLieferant2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheArtikellieferanten. Es gibt keinen Lieferanten mit iid2 "
							+ iIdLieferant2);
		}

		Integer iSort1 = oLieferant1.getISort();
		Integer iSort2 = oLieferant2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oLieferant2.setISort(new Integer(-1));
		oLieferant1.setISort(iSort2);
		oLieferant2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void vertauscheArtikelsperren(Integer iId1, Integer iId2) {

		Artikelsperren o1 = em.find(Artikelsperren.class, iId1);
		Artikelsperren o2 = em.find(Artikelsperren.class, iId2);
		Integer iSort1 = o1.getiSort();
		Integer iSort2 = o2.getiSort();

		o2.setiSort(new Integer(-1));
		o1.setiSort(iSort2);
		o2.setiSort(iSort1);

	}

	public void vertauscheAlergen(Integer iId1, Integer iId2) {

		Alergen o1 = em.find(Alergen.class, iId1);
		Alergen o2 = em.find(Alergen.class, iId2);
		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));
		o1.setISort(iSort2);
		o2.setISort(iSort1);

	}

	/**
	 * Wenn der Einzelpreis geaendert wird, muessen auch die zugehoerigen
	 * Staffelpreise upgedatet werden
	 * 
	 * @param artLiefDtoI
	 *            ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public void updateArtikellieferant(ArtikellieferantDto artLiefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artLiefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artLiefDtoI == null"));
		}

		if (artLiefDtoI.getIId() == null || artLiefDtoI.getArtikelIId() == null
				|| artLiefDtoI.getLieferantIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"artLiefDtoI.getIId() == null || artLiefDtoI.getArtikelIId() == null || artLiefDtoI.getPartnerIIdLieferant() == null"));
		}

		artLiefDtoI.setTPreisgueltigab(Helper.cutTimestamp(artLiefDtoI
				.getTPreisgueltigab()));
		artLiefDtoI.setTPreisgueltigbis(Helper.cutTimestamp(artLiefDtoI
				.getTPreisgueltigbis()));

		// try {
		Artikellieferant artikellieferant = em.find(Artikellieferant.class,
				artLiefDtoI.getIId());
		if (artikellieferant == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikellieferant. Es gibt keinen Artikellieferanten mit iid "
							+ artLiefDtoI.getIId()
							+ "\nartliefDto.toString(): "
							+ artLiefDtoI.toString());
		} else {

			ArtikellieferantDto dtoAlt = assembleArtikellieferantDto(artikellieferant);
			if (!dtoAlt.equals(artLiefDtoI)) {
				artLiefDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
				artLiefDtoI.setTAendern(new java.sql.Timestamp(System
						.currentTimeMillis()));
			}

		}

		// SP2009
		if (artikellieferant.getNEinzelpreis() != null
				&& artLiefDtoI.getNEinzelpreis() == null) {
			// Einzelpreis kann nur geloescht werden, wenn keine Staffekn
			// vorhanden

			Query query = em
					.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
			query.setParameter(1, artikellieferant.getIId());
			Collection<?> cl = query.getResultList();
			if (cl.size() > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN,
						"FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN");
			}

		}

		// Wenn sich der Einzelpreis geaendert hat
		if (artikellieferant.getNEinzelpreis() != null
				&& artLiefDtoI.getNEinzelpreis() != null) {
			if (artikellieferant.getNEinzelpreis().doubleValue() != artLiefDtoI
					.getNEinzelpreis().doubleValue()) {
				Query query = em
						.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
				query.setParameter(1, artikellieferant.getIId());
				Collection<?> cl = query.getResultList();

				ArtikellieferantstaffelDto[] aArtLiefStafDto = assembleArtikellieferantstaffelDtos(cl);
				for (int xArtLiefStafDto = 0; xArtLiefStafDto < aArtLiefStafDto.length; xArtLiefStafDto++) {
					ArtikellieferantstaffelDto artLiefStafDto = aArtLiefStafDto[xArtLiefStafDto];
					if (artLiefDtoI.getNEinzelpreis() != null) {
						if (artLiefDtoI.getNEinzelpreis().doubleValue() != 0) {
							BigDecimal bd100 = new BigDecimal(100);

							if (Helper.short2boolean(artLiefStafDto
									.getBRabattbehalten()) == false) {
								BigDecimal fRabatt = (artLiefStafDto
										.getNNettopreis().multiply(bd100))
										.subtract(
												bd100.multiply(artLiefDtoI
														.getNEinzelpreis()))
										.divide(artLiefDtoI.getNEinzelpreis(),
												4, BigDecimal.ROUND_HALF_EVEN);
								fRabatt = fRabatt.negate();

								Artikellieferantstaffel artikellieferantstaffel = em
										.find(Artikellieferantstaffel.class,
												artLiefStafDto.getIId());
								// TODO: CG Stellen bei Rabatt
								if (artikellieferantstaffel == null) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
											"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
													+ artLiefStafDto.getIId()
													+ "\nartlifstaffelDto.toString(): "
													+ artLiefStafDto.toString());
								}
								artikellieferantstaffel.setFRabatt(fRabatt
										.doubleValue());
								em.merge(artikellieferantstaffel);
								em.flush();

							} else {

								Artikellieferantstaffel artikellieferantstaffel = em
										.find(Artikellieferantstaffel.class,
												artLiefStafDto.getIId());
								if (artikellieferantstaffel == null) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
											"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
													+ artLiefStafDto.getIId()
													+ "\nartlifstaffelDto.toString(): "
													+ artLiefStafDto.toString());
								}
								artikellieferantstaffel
										.setNNettopreis(artLiefDtoI
												.getNEinzelpreis()
												.subtract(
														Helper.getProzentWert(
																artLiefDtoI
																		.getNEinzelpreis(),
																new BigDecimal(
																		artLiefStafDto
																				.getFRabatt()),
																4)));
								em.merge(artikellieferantstaffel);
								em.flush();

							}

						} else {
							if (Helper.short2boolean(artLiefStafDto
									.getBRabattbehalten()) == true) {
								Artikellieferantstaffel artikellieferantstaffel = em
										.find(Artikellieferantstaffel.class,
												artLiefStafDto.getIId());
								if (artikellieferantstaffel == null) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
											"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
													+ artLiefStafDto.getIId()
													+ "\nartlifstaffelDto.toString(): "
													+ artLiefStafDto.toString());
								}
								artikellieferantstaffel
										.setNNettopreis(new BigDecimal(0));
								em.merge(artikellieferantstaffel);
								em.flush();
							}
						}
					}
				}
			}
		}

		setArtikellieferantFromArtikellieferantDto(artikellieferant,
				artLiefDtoI);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	// exccatch: hier immer EJBExceptionLP deklarieren
	public ArtikellieferantDto artikellieferantFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
							"iId == null")));
		}
		Artikellieferant artikellieferant = em
				.find(Artikellieferant.class, iId);
		if (artikellieferant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei artikellieferantFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}

		ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(artikellieferant);

		// exccatch: test begin; JO: immer kommentiert, testing only!
		// nachfolgendes create muss "rollbacked werden"
		// KontoartDto kontoartDto = new KontoartDto();
		// kontoartDto.setCNr("test");
		// kontoartDto.setISort(new Integer(1));
		// getFinanzServiceFac().createKontoart(kontoartDto, idUser);
		// getBestellungFac().createBestellung(null, null);
		// exccatch: test end

		artikellieferantDto.setLieferantDto(getLieferantFac()
				.lieferantFindByPrimaryKey(
						artikellieferantDto.getLieferantIId(), theClientDto));

		return artikellieferantDto;
	}

	public ArtikellieferantDto[] artikellieferantFindByArtikelIId(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// try {

		Query query = em.createNamedQuery("ArtikellieferantfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		ArtikellieferantDto[] artikellieferantDtos = assembleArtikellieferantDtos(cl);
		return artikellieferantDtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtikellieferantDto[] artikellieferantfindByArtikelIIdTPreisgueltigab(
			Integer artikelIId, java.sql.Date tPreisGuelitgab,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// try {

		Query query = em
				.createNamedQuery("ArtikellieferantfindByArtikelIIdTPreisgueltigab");
		query.setParameter(1, artikelIId);
		query.setParameter(2, tPreisGuelitgab);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		ArtikellieferantDto[] artikellieferantDtos = assembleArtikellieferantDtos(cl);
		return artikellieferantDtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArrayList<Map<Integer, String>> getListeDerArtikellieferanten(
			Integer bestellvorschlagIId, BigDecimal nMenge,
			TheClientDto theClientDto) {

		ArrayList<Map<Integer, String>> m = new ArrayList<Map<Integer, String>>();

		BestellvorschlagDto bvDto = null;

		int iGesamtstellenPreis = 10;
		int iNachkommastellenPreis = 2;
		try {
			bvDto = getBestellvorschlagFac().bestellvorschlagFindByPrimaryKey(
					bestellvorschlagIId);
			iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisEK(theClientDto.getMandant());

			iGesamtstellenPreis = 6 + iNachkommastellenPreis;

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArtikellieferantDto[] artikellieferantDtos = artikellieferantFindByArtikelIId(
				bvDto.getIArtikelId(), theClientDto);

		ArrayList liste = new ArrayList();

		for (int i = 0; i < artikellieferantDtos.length; i++) {
			ArtikellieferantDto artikellieferantDto = artikellieferantDtos[i];
			LieferantDto lfDto = getLieferantFac()
					.lieferantFindByPrimaryKeySmall(
							artikellieferantDto.getLieferantIId());
			if (!lfDto.getMandantCNr().equals(theClientDto.getMandant())) {
				continue;
			}

			// Fuer jeden Artikellieferant den Einzelpreis holen:

			ArtikellieferantDto alDto = getArtikelEinkaufspreis(
					bvDto.getIArtikelId(),
					artikellieferantDto.getLieferantIId(), BigDecimal.ONE,
					theClientDto.getSMandantenwaehrung(),
					new Date(System.currentTimeMillis()), theClientDto);
			// Wenn enizelpreis vorhanden

			if (alDto == null) {
				alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
						bvDto.getIArtikelId(),
						artikellieferantDto.getLieferantIId(),
						new Date(System.currentTimeMillis()), theClientDto);
			}

			if (alDto != null) {
				// Fuer jeden Artikellieferant den Preis vor der aktuellen
				// Staffelmenge und nach der aktuellen Staffelmenge holen
				ArtikellieferantDto alDtoLinks = null;
				ArtikellieferantDto alDtoRechts = null;
				alDtoLinks = alDto;
				// Naechste Staffelmenge holen

				if (nMenge.doubleValue() > 1) {
					// Links ist Einzelpreis
					// Suche nach einer Staffelmenge <= Menge
					ArtikellieferantDto alDtoLinksTemp = getArtikelEinkaufspreis(
							bvDto.getIArtikelId(),
							artikellieferantDto.getLieferantIId(), nMenge,
							theClientDto.getSMandantenwaehrung(), new Date(
									System.currentTimeMillis()), theClientDto);

					if (alDtoLinksTemp != null
							&& alDtoLinksTemp.getNStaffelmenge() != null) {
						alDtoLinks = alDtoLinksTemp;
					}
				}

				// NaechsthoehereStaffel
				Query q = em
						.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeGroesser");
				q.setParameter(1, alDto.getIId());
				q.setParameter(2, nMenge);
				q.setParameter(3, Helper.cutTimestamp(new Timestamp(System
						.currentTimeMillis())));

				Collection c = q.getResultList();

				if (c.size() > 0) {
					Artikellieferantstaffel als = (Artikellieferantstaffel) c
							.iterator().next();

					alDtoRechts = getArtikelEinkaufspreis(
							bvDto.getIArtikelId(),
							artikellieferantDto.getLieferantIId(),
							als.getNMenge(),
							theClientDto.getSMandantenwaehrung(), new Date(
									System.currentTimeMillis()), theClientDto);

					if (alDtoRechts != null
							&& alDtoRechts.getNStaffelmenge() != null) {

					} else {
						alDtoRechts = null;
					}

				}

				Object[] o = new Object[2];
				o[0] = alDtoLinks;
				o[1] = alDtoRechts;

				liste.add(o);

			}

		}

		int iZeileGuenstigsterPreis = -1;
		int iZeileSchnellsteWBZ = -1;
		Integer schnellstewbz = null;
		BigDecimal guenstigsterPreis = null;
		for (int i = 0; i < liste.size(); i++) {

			Object[] oZeile = (Object[]) liste.get(i);
			ArtikellieferantDto alDtoLinks = (ArtikellieferantDto) oZeile[0];

			// WBZ
			if (schnellstewbz == null
					&& alDtoLinks.getIWiederbeschaffungszeit() != null) {
				iZeileSchnellsteWBZ = i;
				schnellstewbz = alDtoLinks.getIWiederbeschaffungszeit();
			}

			if (schnellstewbz != null
					&& alDtoLinks.getIWiederbeschaffungszeit() != null
					&& alDtoLinks.getIWiederbeschaffungszeit() < schnellstewbz) {
				iZeileSchnellsteWBZ = i;
				schnellstewbz = alDtoLinks.getIWiederbeschaffungszeit();
			}
			// Guenstigster Preis

			if (guenstigsterPreis == null) {

				if (alDtoLinks.getNNettopreis() != null) {

					guenstigsterPreis = alDtoLinks.getNNettopreis();
					iZeileGuenstigsterPreis = i;
				}
			} else {
				if (alDtoLinks.getNNettopreis() != null) {

					if (alDtoLinks.getNNettopreis().doubleValue() < guenstigsterPreis
							.doubleValue()) {
						guenstigsterPreis = alDtoLinks.getNNettopreis();
						iZeileGuenstigsterPreis = i;
					}
				}
			}

		}

		int iZeileGuenstigsterMoeglicherWert = -1;
		if (guenstigsterPreis != null) {

			BigDecimal guenstigsterRealerWert = bvDto.getNZubestellendeMenge()
					.multiply(guenstigsterPreis);

			BigDecimal guenstigsterMoegicherWert = null;

			for (int i = 0; i < liste.size(); i++) {

				Object[] oZeile = (Object[]) liste.get(i);

				ArtikellieferantDto alDtoRechts = (ArtikellieferantDto) oZeile[1];

				if (alDtoRechts != null
						&& alDtoRechts.getNStaffelmenge() != null
						&& alDtoRechts.getNNettopreis() != null) {

					BigDecimal temp = alDtoRechts.getNStaffelmenge().multiply(
							alDtoRechts.getNNettopreis());

					if (guenstigsterMoegicherWert == null
							&& temp.doubleValue() < guenstigsterRealerWert
									.doubleValue()) {
						guenstigsterMoegicherWert = temp;
						iZeileGuenstigsterMoeglicherWert = i;
					} else {
						if (temp.doubleValue() < guenstigsterRealerWert
								.doubleValue()
								&& temp.doubleValue() < guenstigsterMoegicherWert
										.doubleValue()) {
							guenstigsterMoegicherWert = temp;
							iZeileGuenstigsterMoeglicherWert = i;
						}
					}

				}

			}

		}

		// Nun liste zusammenbauen

		for (int i = 0; i < liste.size(); i++) {

			Object[] oZeile = (Object[]) liste.get(i);
			ArtikellieferantDto alDtoLinks = (ArtikellieferantDto) oZeile[0];

			LieferantDto lDto = getLieferantFac().lieferantFindByPrimaryKey(
					alDtoLinks.getLieferantIId(), theClientDto);

			String lieferant = Helper
					.fitString2LengthHTMLBefuelltMitLeerzeichen(lDto
							.getPartnerDto().getCKbez(), 20);
			if (bvDto.getILieferantId() != null
					&& bvDto.getILieferantId().equals(lDto.getIId())) {

				lieferant = "<span style=\"background-color: #C0C0C0\">"
						+ lieferant + "</span>";

			}

			String vpe = "";
			if (alDtoLinks.getNVerpackungseinheit() != null) {
				vpe = Helper.formatZahl(alDtoLinks.getNVerpackungseinheit(), 0,
						theClientDto.getLocUi());

			}

			vpe = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(vpe, 5,
					true);

			String mindbestmenge = "";
			if (alDtoLinks.getFMindestbestelmenge() != null) {
				mindbestmenge = Helper.formatZahl(
						alDtoLinks.getFMindestbestelmenge(), 0,
						theClientDto.getLocUi());

			}

			mindbestmenge = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					mindbestmenge, 6, true);

			// Menge links
			String mengeLinks = null;

			if (alDtoLinks.getNStaffelmenge() != null) {
				mengeLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(alDtoLinks.getNStaffelmenge(), 0,
								theClientDto.getLocUi()), 6, true);
			} else {
				mengeLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(BigDecimal.ONE, 0,
								theClientDto.getLocUi()), 6, true);
			}

			// Preis

			String preisLinks = "";

			if (alDtoLinks.getNNettopreis() == null) {
				preisLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						"", iGesamtstellenPreis, true);
			} else {
				preisLinks = Helper
						.fitString2LengthHTMLBefuelltMitLeerzeichen(Helper
								.formatZahl(alDtoLinks.getNNettopreis(),
										iNachkommastellenPreis,
										theClientDto.getLocUi()),
								iGesamtstellenPreis, true);
			}

			if (i == iZeileGuenstigsterPreis) {
				preisLinks = "<span style=\"background-color: #00FF00\">"
						+ preisLinks + "</span>";
			}

			// Wiedebeschaffungszeit
			String wbzLinks = "";

			if (alDtoLinks.getIWiederbeschaffungszeit() != null) {
				wbzLinks = alDtoLinks.getIWiederbeschaffungszeit() + "";
			}
			wbzLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					wbzLinks, 2, true);

			if (i == iZeileSchnellsteWBZ) {
				wbzLinks = "<span style=\"background-color: #00FF00\">"
						+ wbzLinks + "</span>";
			}

			String sZeile = lieferant + " VPE:" + vpe + " Min:" + mindbestmenge
					+ " Mng:" + mengeLinks + " Prs:" + preisLinks + " WBZ:"
					+ wbzLinks;

			ArtikellieferantDto alDtoRechts = (ArtikellieferantDto) oZeile[1];

			if (alDtoRechts != null) {

				// Menge links
				String mengeRechts = Helper
						.fitString2LengthHTMLBefuelltMitLeerzeichen(Helper
								.formatZahl(alDtoRechts.getNStaffelmenge(), 0,
										theClientDto.getLocUi()), 6, true);

				// Preis

				String preisRechts = Helper
						.fitString2LengthHTMLBefuelltMitLeerzeichen(Helper
								.formatZahl(alDtoRechts.getNNettopreis(),
										iNachkommastellenPreis,
										theClientDto.getLocUi()),
								iGesamtstellenPreis, true);

				if (i == iZeileGuenstigsterMoeglicherWert) {
					preisRechts = "<span style=\"background-color: #FFFF00\">"
							+ preisRechts + "</span>";
				}

				// Wiedebeschaffungszeit
				String wbzRechts = "";

				if (alDtoRechts.getIWiederbeschaffungszeit() != null) {
					wbzRechts = alDtoRechts.getIWiederbeschaffungszeit() + "";
				}
				wbzRechts = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						wbzRechts, 2, true);

				sZeile += " Mng:" + mengeRechts + " Prs:" + preisRechts
						+ " WBZ:" + wbzRechts;

			}

			String s = "<html><body><font color=\"#000000\">" + sZeile
					+ "</font></body></html>";

			Map<Integer, String> mZeile = new LinkedHashMap<Integer, String>();
			mZeile.put(alDtoLinks.getIId(), s);

			m.add(mZeile);

		}
		return m;
	}

	public ArtikellieferantDto[] artikellieferantFindByLieferantIId(
			Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikellieferantfindByLieferantIId");
		query.setParameter(1, lieferantIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		ArtikellieferantDto[] artikellieferantDtos = assembleArtikellieferantDtos(cl);
		return artikellieferantDtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public ArtikellieferantDto[] artikellieferantFindByLieferantIIdOhneExc(
			Integer lieferantIId, TheClientDto theClientDto) {
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferantIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikellieferantfindByLieferantIId");
		query.setParameter(1, lieferantIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		ArtikellieferantDto[] artikellieferantDtos = assembleArtikellieferantDtos(cl);
		return artikellieferantDtos;
		// }
		// catch (Throwable th) {
		// return null;
		// }
	}

	public void wandleHandeingabeInArtikelUm(Integer positionIId, int iArt,
			String neueArtikelnummer, TheClientDto theClientDto) {

		try {
			if (iArt == HANDARTIKEL_UMWANDELN_ANGEBOT) {
				AngebotpositionDto posDto = getAngebotpositionFac()
						.angebotpositionFindByPrimaryKey(positionIId,
								theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto,
						neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Angebotposition ap = em
						.find(Angebotposition.class, positionIId);
				ap.setArtikelIId(artikelIIdNeu);
				ap.setAngebotpositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_ANFRAGE) {
				AnfragepositionDto posDto = getAnfragepositionFac()
						.anfragepositionFindByPrimaryKey(positionIId,
								theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto,
						neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Anfrageposition ap = em
						.find(Anfrageposition.class, positionIId);
				ap.setArtikelIId(artikelIIdNeu);
				ap.setAnfragepositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_AUFTRAG) {
				AuftragpositionDto posDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(positionIId);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto,
						neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Auftragposition ap = em
						.find(Auftragposition.class, positionIId);
				ap.setArtikelIId(artikelIIdNeu);
				ap.setAuftragpositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				em.flush();

				ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
				reservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				reservierungDto.setIBelegartpositionid(ap.getIId());
				reservierungDto.setArtikelIId(ap.getArtikelIId());
				reservierungDto.setTLiefertermin(ap
						.getTUebersteuerterliefertermin());
				reservierungDto.setNMenge(ap.getNOffeneMenge());
				getReservierungFac().createArtikelreservierung(reservierungDto);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private Integer artikelUmwandeln(TheClientDto theClientDto,
			String artikelnummer, Integer artikelIIdHandeingabe) {
		ArtikelDto artikelDtoHandeingabe = artikelFindByPrimaryKey(
				artikelIIdHandeingabe, theClientDto);

		ArtikelDto artikelDto = new ArtikelDto();
		artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		artikelDto.setCNr(artikelnummer);

		ArtikelsprDto asprDto = new ArtikelsprDto();
		if (artikelDtoHandeingabe.getArtikelsprDto() != null) {
			asprDto.setCBez(artikelDtoHandeingabe.getArtikelsprDto().getCBez());
			asprDto.setCZbez(artikelDtoHandeingabe.getArtikelsprDto()
					.getCZbez());
		}
		artikelDto.setArtikelsprDto(asprDto);
		artikelDto.setEinheitCNr(artikelDtoHandeingabe.getEinheitCNr());

		artikelDto.setBVersteckt(Helper.boolean2Short(false));

		try {
			return createArtikel(artikelDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public ArtikellieferantDto artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
			Integer artikelIId, Integer lieferantIId,
			java.sql.Timestamp tPreisgueltigab, TheClientDto theClientDto) {
		try {
			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigab");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lieferantIId);
			query.setParameter(3, tPreisgueltigab);
			Artikellieferant al = (Artikellieferant) query.getSingleResult();

			return assembleArtikellieferantDto(al);

		} catch (NoResultException ex) {
			return null;
		}
	}

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
			Integer artikelIId, Integer lieferantIId,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null || lieferantIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || lieferantIId == null"));
		}
		try {

			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabKleiner");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lieferantIId);
			query.setParameter(3, tDatumPreisgueltigkeit);
			// @todo getSingleResult oder getResultList ?
			Collection colArtikellieferant = query.getResultList();
			if (colArtikellieferant.size() > 0) {
				ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto((Artikellieferant) colArtikellieferant
						.iterator().next());

				artikellieferantDto.setLieferantDto(getLieferantFac()
						.lieferantFindByPrimaryKey(
								artikellieferantDto.getLieferantIId(),
								theClientDto));
				return artikellieferantDto;
			} else
				return null;

		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(e));
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei artikellieferantFindByArtikellIIdLieferantIId. Es gibt mehr als ein Ergebnis f\u00FCr artikelIId "
							+ artikelIId + " und lieferantiid " + lieferantIId);
		}
	}

	/**
	 * Liefert Einzelpreis,Nettopreis und Fixkosten in Wunschwaehrung zurueck
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lieferantIId
	 *            Integer
	 * @param cWunschwaehrung
	 *            String
	 * @param theClientDto
	 *            String
	 * @return ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(
			Integer artikelIId, Integer lieferantIId, String cWunschwaehrung,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (artikelIId == null || lieferantIId == null
				|| cWunschwaehrung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || lieferantIId == null || cWunschwaehrung == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIId");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lieferantIId);
			Artikellieferant artikellieferant = (Artikellieferant) query
					.getSingleResult();
			ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(artikellieferant);

			artikellieferantDto
					.setLieferantDto(getLieferantFac()
							.lieferantFindByPrimaryKey(
									artikellieferantDto.getLieferantIId(),
									theClientDto));

			try {
				Date datum = new Date(System.currentTimeMillis());
				if (artikellieferantDto.getNEinzelpreis() != null) {
					artikellieferantDto.setNEinzelpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNEinzelpreis(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
				if (artikellieferantDto.getNNettopreis() != null) {
					artikellieferantDto.setNNettopreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNNettopreis(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
				if (artikellieferantDto.getNFixkosten() != null) {
					artikellieferantDto.setNFixkosten(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNFixkosten(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			return artikellieferantDto;
		} catch (NoResultException e) {
			return null;
		}
	}

	@TransactionTimeout(2000)
	public void preiseXLSForPreispflege(byte[] xlsFile,
			TheClientDto theClientDto) {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsFile);
		try {

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<Integer, Integer> hmPreislisten = new HashMap<Integer, Integer>();

			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
			try {
				vkpfartikelpreislisteDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true),
								theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sheet.getRows() > 2 && sheet.getColumns() > 9) {
				// Zuerst alle PreislistenIds holen
				Cell[] zeileUeberschrift = sheet.getRow(0);
				int iSpalteStart = 11;
				while (iSpalteStart < zeileUeberschrift.length) {
					String preislistenname = zeileUeberschrift[iSpalteStart]
							.getContents();

					for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {
						if (vkpfartikelpreislisteDtos[i].getCNr().equals(
								preislistenname)) {
							hmPreislisten.put(
									vkpfartikelpreislisteDtos[i].getIId(),
									new Integer(iSpalteStart));
						}
					}

					iSpalteStart = iSpalteStart + 5;
				}

				for (int i = 2; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					if (sZeile.length > 10) {
						// VKpreisbasis + Gueltigab muss befuellt sein
						if ((sZeile[9].getType() == CellType.NUMBER || sZeile[9]
								.getType() == CellType.NUMBER_FORMULA)
								&& (sZeile[10].getType() == CellType.DATE || sZeile[10]
										.getType() == CellType.DATE_FORMULA)) {

							ArtikelDto aDto = getArtikelFac()
									.artikelFindByCNrOhneExc(
											sZeile[0].getContents(),
											theClientDto);

							if (aDto != null) {

								BigDecimal vkPreisbasis = new BigDecimal(
										((jxl.NumberCell) sZeile[9]).getValue());
								java.sql.Timestamp basisGueltigab = Helper
										.cutTimestamp(new java.sql.Timestamp(
												((jxl.DateCell) sZeile[10])
														.getDate().getTime()));

								// Nun nachsehen, ob die VK-Basis schon
								// vorhanden ist

								try {
									Query query = em
											.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
									query.setParameter(1,
											theClientDto.getMandant());
									query.setParameter(2, aDto.getIId());
									query.setParameter(3, basisGueltigab);

									VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) query
											.getSingleResult();

									// Wenn vorhanden, und ungleich dann updaten
									if (vkPreisbasis.doubleValue() != vkPreisfindungEinzelverkaufspreis
											.getNVerkaufspreisbasis()
											.doubleValue()) {
										vkPreisfindungEinzelverkaufspreis
												.setNVerkaufspreisbasis(vkPreisbasis);
									}
								} catch (NoResultException ex) {

									VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = new VkPreisfindungEinzelverkaufspreisDto();
									vkPreisfindungEinzelverkaufspreisDto
											.setTVerkaufspreisbasisgueltigab(new Date(
													basisGueltigab.getTime()));
									vkPreisfindungEinzelverkaufspreisDto
											.setNVerkaufspreisbasis(vkPreisbasis);
									vkPreisfindungEinzelverkaufspreisDto
											.setArtikelIId(aDto.getIId());
									vkPreisfindungEinzelverkaufspreisDto
											.setMandantCNr(theClientDto
													.getMandant());
									getVkPreisfindungFac()
											.createVkPreisfindungEinzelverkaufspreis(
													vkPreisfindungEinzelverkaufspreisDto,
													theClientDto);
								}

								// Nun fuer jede Preisliste Fixpreis oder Rabatt
								// nachtragen

								Iterator<Integer> it = hmPreislisten.keySet()
										.iterator();
								while (it.hasNext()) {
									Integer preislisteIId = it.next();
									int iStartSpalte = hmPreislisten
											.get(preislisteIId);

									if (sZeile.length > iStartSpalte + 3) {

										BigDecimal nFixpreis = null;
										BigDecimal nRabattsatz = null;
										java.sql.Timestamp tGueltigab = null;

										// Gueltigab
										if (sZeile[iStartSpalte + 3].getType() == CellType.DATE
												|| sZeile[iStartSpalte + 3]
														.getType() == CellType.DATE_FORMULA) {
											tGueltigab = new java.sql.Timestamp(
													((jxl.DateCell) sZeile[iStartSpalte + 3])
															.getDate()
															.getTime());

											if (sZeile[iStartSpalte].getType() == CellType.NUMBER
													|| sZeile[iStartSpalte]
															.getType() == CellType.NUMBER_FORMULA) {
												nFixpreis = new BigDecimal(
														((jxl.NumberCell) sZeile[iStartSpalte])
																.getValue());
											}

											if (sZeile[iStartSpalte].getType() != CellType.EMPTY
													&& sZeile[iStartSpalte]
															.getType() != CellType.NUMBER
													&& sZeile[iStartSpalte]
															.getType() != CellType.NUMBER_FORMULA) {
												ArrayList al = new ArrayList();

												String preisliste = "";

												for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
													if (vkpfartikelpreislisteDtos[k]
															.getIId()
															.equals(preislisteIId)) {
														preisliste = vkpfartikelpreislisteDtos[k]
																.getCNr();
													}
												}

												al.add("Zeile "
														+ (i + 1)
														+ "/ Spalte 'Fixpreis' der Preisliste '"
														+ preisliste
														+ "' muss ein Zahlenformat enthalten");

												throw new EJBExceptionLP(
														EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT,
														al,
														new Exception(
																"FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

											}

											if (sZeile[iStartSpalte + 1]
													.getType() == CellType.NUMBER
													|| sZeile[iStartSpalte + 1]
															.getType() == CellType.NUMBER_FORMULA) {
												nRabattsatz = new BigDecimal(
														((jxl.NumberCell) sZeile[iStartSpalte + 1])
																.getValue());
											}

											if (sZeile[iStartSpalte + 1]
													.getType() != CellType.EMPTY
													&& sZeile[iStartSpalte + 1]
															.getType() != CellType.NUMBER
													&& sZeile[iStartSpalte + 1]
															.getType() != CellType.NUMBER_FORMULA) {
												ArrayList al = new ArrayList();

												String preisliste = "";

												for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
													if (vkpfartikelpreislisteDtos[k]
															.getIId()
															.equals(preislisteIId)) {
														preisliste = vkpfartikelpreislisteDtos[k]
																.getCNr();
													}
												}

												al.add("Zeile "
														+ (i + 1)
														+ "/ Spalte 'Rabattsatz' der Preisliste '"
														+ preisliste
														+ "' muss ein Zahlenformat enthalten");

												throw new EJBExceptionLP(
														EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT,
														al,
														new Exception(
																"FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

											}

											if ((nFixpreis != null && nRabattsatz != null)
													|| (nFixpreis == null && nRabattsatz != null)
													|| (nFixpreis != null && nRabattsatz == null)) {

												// Pruefen obs schon einen
												// Eintrag gibt
												VkPreisfindungPreisliste preis = null;
												try {
													Query query = em
															.createNamedQuery("VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
													query.setParameter(1,
															preislisteIId);
													query.setParameter(2,
															aDto.getIId());
													query.setParameter(3,
															tGueltigab);
													preis = (VkPreisfindungPreisliste) query
															.getSingleResult();

													if (nFixpreis != null) {
														preis.setNArtikelfixpreis(nFixpreis);
														preis.setNArtikelstandardrabattsatz(new BigDecimal(
																0));
													} else {
														preis.setNArtikelstandardrabattsatz(nRabattsatz);
														preis.setNArtikelfixpreis(null);
													}

												} catch (NoResultException ex) {
													// Wenns keinen gibt, neu
													// anlegen

													VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
													vkPreisfindungPreislisteDto
															.setArtikelIId(aDto
																	.getIId());
													vkPreisfindungPreislisteDto
															.setTPreisgueltigab(new java.sql.Date(
																	tGueltigab
																			.getTime()));

													if (nFixpreis != null) {
														vkPreisfindungPreislisteDto
																.setNArtikelfixpreis(nFixpreis);
														vkPreisfindungPreislisteDto
																.setNArtikelstandardrabattsatz(new BigDecimal(
																		0));
													} else {
														vkPreisfindungPreislisteDto
																.setNArtikelstandardrabattsatz(nRabattsatz);
														vkPreisfindungPreislisteDto
																.setNArtikelfixpreis(null);
													}

													vkPreisfindungPreislisteDto
															.setVkpfartikelpreislisteIId(preislisteIId);
													getVkPreisfindungFac()
															.createVkPreisfindungPreisliste(
																	vkPreisfindungPreislisteDto,
																	theClientDto);

												}

											}

										} else {
											ArrayList al = new ArrayList();

											String preisliste = "";

											for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
												if (vkpfartikelpreislisteDtos[k]
														.getIId().equals(
																preislisteIId)) {
													preisliste = vkpfartikelpreislisteDtos[k]
															.getCNr();
												}
											}

											al.add("Zeile "
													+ (i + 1)
													+ "/ Spalte 'G\u00FCltig ab' der Preisliste '"
													+ preisliste
													+ "' muss ein gueltiges Datum enthalten");

											throw new EJBExceptionLP(
													EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT,
													al,
													new Exception(
															"FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));
										}

									}

								}

							}

						} else {

							ArrayList al = new ArrayList();
							if (!(sZeile[9].getType() == CellType.NUMBER || sZeile[9]
									.getType() == CellType.NUMBER_FORMULA)) {
								al.add("Zeile "
										+ (i + 1)
										+ "/ Spalte J muss Numerische werte enthalten");
							}

							if (!(sZeile[10].getType() == CellType.DATE || sZeile[10]
									.getType() == CellType.DATE_FORMULA)) {
								al.add("Zeile "
										+ (i + 1)
										+ "/ Spalte K muss ein gueltiges Datum enthalten");
							}

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT,
									al,
									new Exception(
											"FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public byte[] getXLSForPreispflege(Integer artikelgruppeIId,
			Integer artikelklasseIId, String artikelNrVon, String artikelNrBis,
			boolean bMitVersteckten, TheClientDto theClientDto) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		WritableWorkbook workbook = null;
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			workbook = Workbook.createWorkbook(os, ws);

		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		try {
			WritableSheet sheet = workbook.createSheet("Artikel", 0);

			Session session = FLRSessionFactory.getFactory().openSession();

			session.enableFilter("filterMandant").setParameter("paramMandant",
					theClientDto.getMandant());
			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String queryString = "SELECT artikelliste FROM FLRArtikelliste AS artikelliste WHERE artikelliste.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND artikelliste.artikelart_c_nr<>'"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL + "'";

			if (bMitVersteckten == false) {
				queryString += " AND artikelliste.b_versteckt=0 ";
			}

			if (artikelNrVon != null) {
				queryString += " AND artikelliste.c_nr >='" + artikelNrVon
						+ "'";
			}
			if (artikelNrBis != null) {

				String artikelNrBis_Gefuellt = Helper.fitString2Length(
						artikelNrBis, 25, '_');
				queryString += " AND artikelliste.c_nr <='"
						+ artikelNrBis_Gefuellt + "'";
			}
			if (artikelklasseIId != null) {
				queryString += " AND klasse.i_id="
						+ artikelklasseIId.intValue();
			}
			if (artikelgruppeIId != null) {
				queryString += " AND gruppe.i_id="
						+ artikelgruppeIId.intValue();
			}

			queryString += " ORDER BY artikelliste.c_nr ASC";

			org.hibernate.Query query = session.createQuery(queryString);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();

			// 1. Zeile = Ueberschrift
			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
			try {
				vkpfartikelpreislisteDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true),
								theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Datumsformat
			DateFormat customDateFormat = new DateFormat("dd.MM.yyyy");
			WritableCellFormat dateFormat = new WritableCellFormat(
					customDateFormat);

			// Zahlenformat

			int iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisVK(theClientDto.getMandant());

			NumberFormat customNumberFormatRabatt = new NumberFormat("####0.00");
			WritableCellFormat numberFormatRabatt = new WritableCellFormat(
					customNumberFormatRabatt);

			String sNachkomma = "";
			for (int i = 0; i < iNachkommastellenPreis; i++) {
				sNachkomma += "0";
			}

			NumberFormat customNumberFormatPreis = new NumberFormat("#######0."
					+ sNachkomma);
			WritableCellFormat numberFormatPreis = new WritableCellFormat(
					customNumberFormatPreis);

			WritableCellFormat numberAsTextFormat = new WritableCellFormat(
					NumberFormats.TEXT);

			sheet.addCell(new Label(0, 1, "Artikelnummer"));
			sheet.addCell(new Label(1, 1, "Bezeichnung"));
			sheet.addCell(new Label(2, 1, "Zusatzbezeichnung"));
			sheet.addCell(new Label(3, 1, "Einheit"));
			sheet.addCell(new Label(4, 1, "Lieferant"));
			sheet.addCell(new Label(5, 1, "Einzelpreis"));
			sheet.addCell(new Label(6, 1, "Rabatt"));
			sheet.addCell(new Label(7, 1, "Nettopreis"));
			sheet.addCell(new Label(8, 1, "G\u00FCltig seit"));
			// Ab hier VK-Basis
			sheet.addCell(new Label(9, 1, "VK-Basis"));
			sheet.addCell(new Label(10, 1, "G\u00FCltig seit"));
			// Nun alle aktiven Preislisten

			int iSpalte = 11;

			for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {

				sheet.addCell(new Label(iSpalte, 0,
						vkpfartikelpreislisteDtos[i].getCNr()));
				sheet.addCell(new Label(iSpalte, 1, "Fixpreis"));
				iSpalte++;
				sheet.addCell(new Label(iSpalte, 1, "Rabatt"));
				iSpalte++;
				sheet.addCell(new Label(iSpalte, 1, "Errechneter Preis"));
				iSpalte++;
				sheet.addCell(new Label(iSpalte, 1, "G\u00FCltig ab"));
				iSpalte++;
				sheet.addCell(new Label(iSpalte, 1, "W\u00E4hrung"));
				iSpalte++;
			}

			int iRow = 2;
			while (resultListIterator.hasNext()) {
				FLRArtikelliste artikelliste = (FLRArtikelliste) resultListIterator
						.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						artikelliste.getI_id(), theClientDto);

				sheet.addCell(new Label(0, iRow, aDto.getCNr(),
						numberAsTextFormat));

				if (aDto.getArtikelsprDto() != null) {

					sheet.addCell(new Label(1, iRow, aDto.getArtikelsprDto()
							.getCBez()));
					sheet.addCell(new Label(2, iRow, aDto.getArtikelsprDto()
							.getCZbez()));
				}
				sheet.addCell(new Label(3, iRow, aDto.getEinheitCNr().trim()));

				ArtikellieferantDto alDto = getArtikelEinkaufspreis(
						artikelliste.getI_id(), new BigDecimal(1),
						theClientDto.getSMandantenwaehrung(), theClientDto);

				if (alDto != null) {
					if (alDto.getLieferantDto() != null) {
						sheet.addCell(new Label(4, iRow, alDto
								.getLieferantDto().getPartnerDto()
								.formatFixName1Name2()));
					}

					if (alDto.getNEinzelpreis() != null) {
						sheet.addCell(new jxl.write.Number(5, iRow, alDto
								.getNEinzelpreis().doubleValue(),
								numberFormatPreis));
					}
					if (alDto.getFRabatt() != null) {
						sheet.addCell(new jxl.write.Number(6, iRow, alDto
								.getFRabatt().doubleValue(), numberFormatRabatt));
					}
					if (alDto.getNNettopreis() != null) {
						sheet.addCell(new jxl.write.Number(7, iRow, alDto
								.getNNettopreis().doubleValue(),
								numberFormatPreis));
					}

					sheet.addCell(new jxl.write.DateTime(8, iRow, alDto
							.getTPreisgueltigab(), dateFormat));
				}

				try {
					VkPreisfindungEinzelverkaufspreisDto preisbasisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(
									artikelliste.getI_id(),
									new Date(System.currentTimeMillis()),
									theClientDto.getSMandantenwaehrung(),
									theClientDto);

					// Ab hier VK-Basis
					if (preisbasisDto != null) {
						sheet.addCell(new jxl.write.Number(9, iRow,
								preisbasisDto.getNVerkaufspreisbasis()
										.doubleValue()));
						sheet.addCell(new jxl.write.DateTime(
								10,
								iRow,
								preisbasisDto.getTVerkaufspreisbasisgueltigab(),
								dateFormat));

					}

					iSpalte = 11;
					for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {

						// Nun fuer jede preisliste den preis holen

						Session sessionPreisliste = FLRSessionFactory
								.getFactory().openSession();

						String sQueryPreisliste = "SELECT artikelpreis FROM FLRVkpfartikelpreis artikelpreis WHERE artikelpreis.vkpfartikelpreisliste_i_id="
								+ vkpfartikelpreislisteDtos[i].getIId()
								+ " AND artikelpreis.artikel_i_id="
								+ artikelliste.getI_id()
								+ " AND artikelpreis.t_preisgueltigab<='"
								+ Helper.formatDateWithSlashes(new java.sql.Date(
										System.currentTimeMillis()))
								+ "' ORDER BY artikelpreis.t_preisgueltigab DESC";

						org.hibernate.Query queryPreisliste = session
								.createQuery(sQueryPreisliste);
						queryPreisliste.setMaxResults(1);

						List<?> resultListPreisliste = queryPreisliste.list();

						Iterator<?> resultListIteratorPreisliste = resultListPreisliste
								.iterator();

						if (resultListIteratorPreisliste.hasNext()) {

							FLRVkpfartikelpreis artikelpreis = (FLRVkpfartikelpreis) resultListIteratorPreisliste
									.next();

							if (artikelpreis.getN_artikelfixpreis() != null) {
								sheet.addCell(new jxl.write.Number(iSpalte,
										iRow, artikelpreis
												.getN_artikelfixpreis()
												.doubleValue(),
										numberFormatPreis));
							}
							iSpalte++;
							if (artikelpreis.getN_artikelstandardrabattsatz() != null) {
								sheet.addCell(new jxl.write.Number(
										iSpalte,
										iRow,
										artikelpreis
												.getN_artikelstandardrabattsatz()
												.doubleValue(),
										numberFormatRabatt));
							}
							iSpalte++;

							String referenzRabatt = CellReferenceHelper
									.getCellReference(iSpalte - 1, iRow);

							sheet.addCell(new Formula(iSpalte, iRow, "WENN("
									+ CellReferenceHelper.getCellReference(
											iSpalte - 2, iRow)
									+ "=0,$J"
									+ (iRow + 1)
									+ "*(1-"
									+ referenzRabatt
									+ "/100),WENN("
									+ referenzRabatt
									+ "=0,"
									+ CellReferenceHelper.getCellReference(
											iSpalte - 2, iRow) + ",\"????\"))"));
							iSpalte++;
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(artikelpreis
									.getT_preisgueltigab().getTime());

							DateTime dateCell = new DateTime(iSpalte, iRow,
									c.getTime(), dateFormat);
							sheet.addCell(dateCell);

							// sheet.addCell(new jxl.write.DateTime(iSpalte,
							// iRow,
							// new Date(
							// artikelpreis.getT_preisgueltigab().getTime())));

							iSpalte++;
							sheet.addCell(new Label(iSpalte, iRow,
									vkpfartikelpreislisteDtos[i]
											.getWaehrungCNr()));
							iSpalte++;

						} else {
							iSpalte = iSpalte + 5;
						}
						sessionPreisliste.close();

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				iRow++;
			}

			session.close();
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);

		} catch (RowsExceededException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (WriteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return os.toByteArray();
	}

	public Integer[] getBereitsVerwendeteShopgruppen(Integer artikelIId) {

		Artikel artikel = em.find(Artikel.class, artikelIId);

		HashMap<Integer, String> hmBereitsVerwendet = new HashMap<Integer, String>();

		if (artikel.getShopgruppeIId() != null) {
			hmBereitsVerwendet.put(artikel.getShopgruppeIId(), "");
		}

		Query query = em.createNamedQuery("ArtikelshopgruppefindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Artikelshopgruppe sg = (Artikelshopgruppe) iterator.next();
			hmBereitsVerwendet.put(sg.getShopgruppeIId(), "");

		}

		return (Integer[]) hmBereitsVerwendet.keySet().toArray(
				new Integer[hmBereitsVerwendet.keySet().size()]);
	}

	public ArtikellieferantDto artikellieferantFindByIIdInWunschwaehrung(
			Integer artikellieferantIId, String cWunschwaehrung,
			TheClientDto theClientDto) {

		try {

			Artikellieferant artikellieferant = em.find(Artikellieferant.class,
					artikellieferantIId);

			ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(artikellieferant);
			artikellieferantDto
					.setLieferantDto(getLieferantFac()
							.lieferantFindByPrimaryKey(
									artikellieferantDto.getLieferantIId(),
									theClientDto));

			try {
				Date datum = new Date(System.currentTimeMillis());
				if (artikellieferantDto.getNEinzelpreis() != null) {
					artikellieferantDto.setNEinzelpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNEinzelpreis(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
				if (artikellieferantDto.getNNettopreis() != null) {
					artikellieferantDto.setNNettopreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNNettopreis(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
				if (artikellieferantDto.getNFixkosten() != null) {
					artikellieferantDto.setNFixkosten(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									artikellieferantDto.getNFixkosten(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(), cWunschwaehrung,
									datum, theClientDto));
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			return artikellieferantDto;
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setArtikellieferantFromArtikellieferantDto(
			Artikellieferant artikellieferant,
			ArtikellieferantDto artikellieferantDto) {
		artikellieferant.setCBezbeilieferant(artikellieferantDto
				.getCBezbeilieferant());
		artikellieferant.setCArtikelnrlieferant(artikellieferantDto
				.getCArtikelnrlieferant());
		artikellieferant.setBHerstellerbez(artikellieferantDto
				.getBHerstellerbez());
		artikellieferant.setBWebshop(artikellieferantDto.getBWebshop());
		artikellieferant.setNEinzelpreis(Helper.rundeKaufmaennisch(
				artikellieferantDto.getNEinzelpreis(), 4));
		artikellieferant.setFRabatt(artikellieferantDto.getFRabatt());
		artikellieferant.setNNettopreis(Helper.rundeKaufmaennisch(
				artikellieferantDto.getNNettopreis(), 4));
		artikellieferant.setFStandardmenge(artikellieferantDto
				.getFStandardmenge());
		artikellieferant.setFMindestbestellmenge(artikellieferantDto
				.getFMindestbestelmenge());
		artikellieferant.setNVerpackungseinheit(artikellieferantDto
				.getNVerpackungseinheit());
		artikellieferant.setNFixkosten(artikellieferantDto.getNFixkosten());
		artikellieferant.setCRabattgruppe(artikellieferantDto
				.getCRabattgruppe());
		artikellieferant.setCAngebotnummer(artikellieferantDto
				.getCAngebotnummer());
		artikellieferant.setTPreisgueltigab(artikellieferantDto
				.getTPreisgueltigab());
		artikellieferant.setTPreisgueltigbis(artikellieferantDto
				.getTPreisgueltigbis());
		artikellieferant.setISort(artikellieferantDto.getISort());
		artikellieferant.setIWiederbeschaffungszeit(artikellieferantDto
				.getIWiederbeschaffungszeit());
		artikellieferant.setLieferantIId(artikellieferantDto.getLieferantIId());
		artikellieferant.setBRabattbehalten(artikellieferantDto
				.getBRabattbehalten());
		artikellieferant.setTAendern(artikellieferantDto.getTAendern());
		artikellieferant.setPersonalIIdAendern(artikellieferantDto
				.getPersonalIIdAendern());
		artikellieferant.setZertifikatartIId(artikellieferantDto
				.getZertifikatartIId());
		artikellieferant.setCWeblink(artikellieferantDto.getCWeblink());
		artikellieferant.setEinheitCNrVpe(artikellieferantDto
				.getEinheitCNrVpe());
		em.merge(artikellieferant);
		em.flush();
	}

	private ArtikellieferantDto assembleArtikellieferantDto(
			Artikellieferant artikellieferant) {
		return ArtikellieferantDtoAssembler.createDto(artikellieferant);
	}

	private ArtikellieferantDto[] assembleArtikellieferantDtos(
			Collection<?> artikellieferants) {
		List<ArtikellieferantDto> list = new ArrayList<ArtikellieferantDto>();
		if (artikellieferants != null) {
			Iterator<?> iterator = artikellieferants.iterator();
			while (iterator.hasNext()) {
				Artikellieferant artikellieferant = (Artikellieferant) iterator
						.next();
				list.add(assembleArtikellieferantDto(artikellieferant));
			}
		}
		ArtikellieferantDto[] returnArray = new ArtikellieferantDto[list.size()];
		return (ArtikellieferantDto[]) list.toArray(returnArray);
	}

	private void setMontageFromMontageDto(Montage montage, MontageDto montageDto) {
		montage.setFRasterliegend(montageDto.getFRasterliegend());
		montage.setFRasterstehend(montageDto.getFRasterstehend());
		montage.setBHochstellen(montageDto.getBHochstellen());
		montage.setBHochsetzen(montageDto.getBHochsetzen());
		montage.setBPolarisiert(montageDto.getBPolarisiert());
		em.merge(montage);
		em.flush();
	}

	private MontageDto assembleMontageDto(Montage montage) {
		return MontageDtoAssembler.createDto(montage);
	}

	private MontageDto[] assembleMontageDtos(Collection<?> montages) {
		List<MontageDto> list = new ArrayList<MontageDto>();
		if (montages != null) {
			Iterator<?> iterator = montages.iterator();
			while (iterator.hasNext()) {
				Montage montage = (Montage) iterator.next();
				list.add(assembleMontageDto(montage));
			}
		}
		MontageDto[] returnArray = new MontageDto[list.size()];
		return (MontageDto[]) list.toArray(returnArray);
	}

	private void setArtklasprFromArtklasprDto(Artklaspr artklaspr,
			ArtklasprDto artklasprDto) {
		artklaspr.setCBez(artklasprDto.getCBez());
		em.merge(artklaspr);
		em.flush();
	}

	private void setShopgruppesprFromShoprguppesprDto(
			Shopgruppespr shopgruppespr, ShopgruppesprDto shopgruppesprDto) {
		shopgruppespr.setCBez(shopgruppesprDto.getCBez());
		em.merge(shopgruppespr);
		em.flush();
	}

	private ArtklasprDto assembleArtklasprDto(Artklaspr artklaspr) {
		return ArtklasprDtoAssembler.createDto(artklaspr);
	}

	private ArtklasprDto[] assembleArtklasprDtos(Collection<?> artklasprs) {
		List<ArtklasprDto> list = new ArrayList<ArtklasprDto>();
		if (artklasprs != null) {
			Iterator<?> iterator = artklasprs.iterator();
			while (iterator.hasNext()) {
				Artklaspr artklaspr = (Artklaspr) iterator.next();
				list.add(assembleArtklasprDto(artklaspr));
			}
		}
		ArtklasprDto[] returnArray = new ArtklasprDto[list.size()];
		return (ArtklasprDto[]) list.toArray(returnArray);
	}

	private void setSollverkaufFromSollverkaufDto(Sollverkauf sollverkauf,
			SollverkaufDto sollverkaufDto) {
		sollverkauf.setFAufschlag(sollverkaufDto.getFAufschlag());
		sollverkauf.setFSollverkauf(sollverkaufDto.getFSollverkauf());
		em.merge(sollverkauf);
		em.flush();
	}

	private SollverkaufDto assembleSollverkaufDto(Sollverkauf sollverkauf) {
		return SollverkaufDtoAssembler.createDto(sollverkauf);
	}

	private SollverkaufDto[] assembleSollverkaufDtos(Collection<?> sollverkaufs) {
		List<SollverkaufDto> list = new ArrayList<SollverkaufDto>();
		if (sollverkaufs != null) {
			Iterator<?> iterator = sollverkaufs.iterator();
			while (iterator.hasNext()) {
				Sollverkauf sollverkauf = (Sollverkauf) iterator.next();
				list.add(assembleSollverkaufDto(sollverkauf));
			}
		}
		SollverkaufDto[] returnArray = new SollverkaufDto[list.size()];
		return (SollverkaufDto[]) list.toArray(returnArray);
	}

	private void setGeometrieFromGeometrieDto(Geometrie geometrie,
			GeometrieDto geometrieDto) {
		geometrie.setFBreite(geometrieDto.getFBreite());
		geometrie.setCBreitetext(geometrieDto.getCBreitetext());
		geometrie.setFHoehe(geometrieDto.getFHoehe());
		geometrie.setFTiefe(geometrieDto.getFTiefe());
		em.merge(geometrie);
		em.flush();
	}

	private GeometrieDto assembleGeometrieDto(Geometrie geometrie) {
		return GeometrieDtoAssembler.createDto(geometrie);
	}

	private GeometrieDto[] assembleGeometrieDtos(Collection<?> geometries) {
		List<GeometrieDto> list = new ArrayList<GeometrieDto>();
		if (geometries != null) {
			Iterator<?> iterator = geometries.iterator();
			while (iterator.hasNext()) {
				Geometrie geometrie = (Geometrie) iterator.next();
				list.add(assembleGeometrieDto(geometrie));
			}
		}
		GeometrieDto[] returnArray = new GeometrieDto[list.size()];
		return (GeometrieDto[]) list.toArray(returnArray);
	}

	private void setArtikelsprFromArtikelsprDto(Artikelspr artikelspr,
			ArtikelsprDto artikelsprDto) {
		artikelspr.setCKbez(Helper.cutString(artikelsprDto.getCKbez(),
				ArtikelFac.MAX_ARTIKEL_KURZBEZEICHNUNG));
		artikelspr.setCBez(Helper.cutString(artikelsprDto.getCBez(),
				ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG));
		artikelspr.setCZbez(Helper.cutString(artikelsprDto.getCZbez(),
				ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG));
		artikelspr.setCZbez2(Helper.cutString(artikelsprDto.getCZbez2(),
				ArtikelFac.MAX_ARTIKEL_ZUSATZBEZEICHNUNG2));
		artikelspr.setPersonalIIdAendern(artikelsprDto.getPersonalIIdAendern());
		artikelspr.setCSiwert(HelperServer.getDBValueFromBigDecimal(
				artikelsprDto.getNSiwert(), 60));
		em.merge(artikelspr);
		em.flush();
	}

	private ArtikelsprDto assembleArtikelsprDto(Artikelspr artikelspr) {
		return ArtikelsprDtoAssembler.createDto(artikelspr);
	}

	private ArtikelsprDto[] assembleArtikelsprDtos(Collection<?> artikelsprs) {
		List<ArtikelsprDto> list = new ArrayList<ArtikelsprDto>();
		if (artikelsprs != null) {
			Iterator<?> iterator = artikelsprs.iterator();
			while (iterator.hasNext()) {
				Artikelspr artikelspr = (Artikelspr) iterator.next();
				list.add(assembleArtikelsprDto(artikelspr));
			}
		}
		ArtikelsprDto[] returnArray = new ArtikelsprDto[list.size()];
		return (ArtikelsprDto[]) list.toArray(returnArray);
	}

	public Integer createHersteller(HerstellerDto herstellerDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (herstellerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("herstellerDto == null"));
		}
		if (herstellerDto.getCNr() == null
				|| herstellerDto.getPartnerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"herstellerDto.getCNr() == null || herstellerDto.getPartnerIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("HerstellerfindByCNr");
			query.setParameter(1, herstellerDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Hersteller doppelt = (Hersteller) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_HERSTELLER.CNR"));

		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HERSTELLER);
			herstellerDto.setIId(pk);
			Hersteller hersteller = new Hersteller(herstellerDto.getIId(),
					herstellerDto.getCNr(), herstellerDto.getPartnerIId());
			em.persist(hersteller);
			em.flush();
			setHerstellerFromHerstellerDto(hersteller, herstellerDto);
			return herstellerDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeHersteller(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Hersteller hersteller = em.find(Hersteller.class, iId);
		if (hersteller == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeHersteller. Es gibt keine iid " + iId);
		}
		try {
			em.remove(hersteller);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public void updateHersteller(HerstellerDto herstellerDto)
			throws EJBExceptionLP {
		if (herstellerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("herstellerDto == null"));
		}
		Integer iId = herstellerDto.getIId();
		// try {
		Hersteller hersteller = em.find(Hersteller.class, iId);
		if (hersteller == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateHersteller. Es gibt keine iid " + iId);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		try {
			Query query = em.createNamedQuery("HerstellerfindByCNr");
			query.setParameter(1, herstellerDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Hersteller) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_HERSTELLER.CNR"));
			}
		} catch (NoResultException ex) {
			// nothing here
		}
		setHerstellerFromHerstellerDto(hersteller, herstellerDto);

	}

	public HerstellerDto herstellerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		try {
			HerstellerDto herstellerDto = new HerstellerDto();
			Hersteller hersteller = em.find(Hersteller.class, iId);
			if (hersteller == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei herstellerFindByPrimaryKey. Es gibt keine iid "
								+ iId);
			}
			herstellerDto = assembleHerstellerDto(hersteller);
			herstellerDto.setPartnerDto(getPartnerFac()
					.partnerFindByPrimaryKey(herstellerDto.getPartnerIId(),
							theClientDto));

			return herstellerDto;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei herstellerFindByPrimaryKey. Es gibt mehrere iids "
							+ iId);
		}
	}

	public HerstellerDto[] herstellerFindByPartnerIId(Integer iPartnerId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("HerstellerfindByPartnerIId");
			query.setParameter(1, iPartnerId);
			Collection<?> cl = query.getResultList();
			// if (cl.isEmpty()) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }

			return assembleHerstellerDtos(cl);
			// }
			// catch (FinderException e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public HerstellerDto[] herstellerFindByPartnerIIdOhneExc(
			Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException {
		// try {
		Query query = em.createNamedQuery("HerstellerfindByPartnerIId");
		query.setParameter(1, iPartnerId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("herstellerFindByPartnerIId");
		// return null;
		// }
		return assembleHerstellerDtos(cl);
		// }
		// catch (Throwable t) {
		// // kein eintrag gefunden
		// myLogger.warn("herstellerFindByPartnerIId", t);
		// return null;
		// }
	}

	private void setHerstellerFromHerstellerDto(Hersteller hersteller,
			HerstellerDto herstellerDto) {
		hersteller.setPartnerIId(herstellerDto.getPartnerIId());
		hersteller.setCNr(herstellerDto.getCNr());
		em.merge(hersteller);
		em.flush();
	}

	private HerstellerDto assembleHerstellerDto(Hersteller hersteller) {
		return HerstellerDtoAssembler.createDto(hersteller);
	}

	private HerstellerDto[] assembleHerstellerDtos(Collection<?> herstellers) {
		List<HerstellerDto> list = new ArrayList<HerstellerDto>();
		if (herstellers != null) {
			Iterator<?> iterator = herstellers.iterator();
			while (iterator.hasNext()) {
				Hersteller hersteller = (Hersteller) iterator.next();
				list.add(assembleHerstellerDto(hersteller));
			}
		}
		HerstellerDto[] returnArray = new HerstellerDto[list.size()];
		return (HerstellerDto[]) list.toArray(returnArray);
	}

	public ArtikelartDto[] artikelartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ArtikelartfindAll");
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }

		return assembleArtikelartDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
	}

	private void setArtikelartFromArtikelartDto(Artikelart artikelart,
			ArtikelartDto artikelartDto) {
		em.merge(artikelart);
		em.flush();
	}

	private ArtikelartDto assembleArtikelartDto(Artikelart artikelart) {
		return ArtikelartDtoAssembler.createDto(artikelart);
	}

	private ArtikelartDto[] assembleArtikelartDtos(Collection<?> artikelarts) {
		List<ArtikelartDto> list = new ArrayList<ArtikelartDto>();
		if (artikelarts != null) {
			Iterator<?> iterator = artikelarts.iterator();
			while (iterator.hasNext()) {
				Artikelart artikelart = (Artikelart) iterator.next();
				list.add(assembleArtikelartDto(artikelart));
			}
		}
		ArtikelartDto[] returnArray = new ArtikelartDto[list.size()];
		return (ArtikelartDto[]) list.toArray(returnArray);
	}

	private void setArtikelartsprFromArtikelartsprDto(
			Artikelartspr artikelartspr, ArtikelartsprDto artikelartsprDto) {
		artikelartspr.setCBez(artikelartsprDto.getCBez());
		em.merge(artikelartspr);
		em.flush();
	}

	private ArtikelartsprDto assembleArtikelartsprDto(
			Artikelartspr artikelartspr) {
		return ArtikelartsprDtoAssembler.createDto(artikelartspr);
	}

	private ArtikelartsprDto[] assembleArtikelartsprDtos(
			Collection<?> artikelartsprs) {
		List<ArtikelartsprDto> list = new ArrayList<ArtikelartsprDto>();
		if (artikelartsprs != null) {
			Iterator<?> iterator = artikelartsprs.iterator();
			while (iterator.hasNext()) {
				Artikelartspr artikelartspr = (Artikelartspr) iterator.next();
				list.add(assembleArtikelartsprDto(artikelartspr));
			}
		}
		ArtikelartsprDto[] returnArray = new ArtikelartsprDto[list.size()];
		return (ArtikelartsprDto[]) list.toArray(returnArray);
	}

	private HandlagerbewegungDto assembleHandlagerbewegungDto(
			Handlagerbewegung handlagerbewegung) {
		return HandlagerbewegungDtoAssembler.createDto(handlagerbewegung);
	}

	private HandlagerbewegungDto[] assembleHandlagerbewegungDtos(
			Collection<?> handlagerbewegungs) {
		List<HandlagerbewegungDto> list = new ArrayList<HandlagerbewegungDto>();
		if (handlagerbewegungs != null) {
			Iterator<?> iterator = handlagerbewegungs.iterator();
			while (iterator.hasNext()) {
				Handlagerbewegung handlagerbewegung = (Handlagerbewegung) iterator
						.next();
				list.add(assembleHandlagerbewegungDto(handlagerbewegung));
			}
		}
		HandlagerbewegungDto[] returnArray = new HandlagerbewegungDto[list
				.size()];
		return (HandlagerbewegungDto[]) list.toArray(returnArray);
	}

	public void createArtikelart(ArtikelartDto artikelartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("partnerartDto == null"));
		}
		if (artikelartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("partnerartDto.getCNr() == null"));
		}
		try {
			Artikelart artikelart = new Artikelart(artikelartDto.getCNr());
			em.persist(artikelart);
			em.flush();
			setArtikelartFromArtikelartDto(artikelart, artikelartDto);
			if (artikelartDto.getArtikelartsprDto() != null) {
				Artikelartspr artikelartspr = new Artikelartspr(
						theClientDto.getLocUiAsString(), artikelartDto.getCNr());
				em.persist(artikelartspr);
				em.flush();
				setArtikelartsprFromArtikelartsprDto(artikelartspr,
						artikelartDto.getArtikelartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelart(String cNr) throws EJBExceptionLP {
		// try {
		Artikelart toRemove = em.find(Artikelart.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelart. Es gibt keine Artikelart "
							+ cNr);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeArtikelart(ArtikelartDto artikelartDto)
			throws EJBExceptionLP {
		if (artikelartDto != null) {
			String cNr = artikelartDto.getCNr();
			removeArtikelart(cNr);
		}
	}

	public void updateArtikelart(ArtikelartDto artikelartDto)
			throws EJBExceptionLP {
		if (artikelartDto != null) {
			String cNr = artikelartDto.getCNr();
			// try {
			Artikelart artikelart = em.find(Artikelart.class, cNr);
			if (artikelart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateArtikelart. Es gibt keine cnr " + cNr
								+ "\nartikelartDto.toString(): "
								+ artikelartDto.toString());
			}
			setArtikelartFromArtikelartDto(artikelart, artikelartDto);
			// try {
			if (artikelartDto.getArtikelartsprDto() != null) {

				// try {
				Artikelartspr artikelartspr = em.find(Artikelartspr.class,
						new ArtikelartsprPK(artikelartDto.getArtikelartsprDto()
								.getLocaleCNr(), cNr));
				if (artikelartspr == null) {
					artikelartspr = new Artikelartspr(artikelartDto
							.getArtikelartsprDto().getLocaleCNr(), cNr);
					em.persist(artikelartspr);
					em.flush();
				}
				setArtikelartsprFromArtikelartsprDto(artikelartspr,
						artikelartDto.getArtikelartsprDto());
			}
		}
		// catch (FinderException ex) {
		// Artikelartspr artikelartspr = new
		// Artikelartspr(artikelartDto.getArtikelartsprDto
		// ().getLocaleCNr(),cNr);
		// em.persist(artikelartspr);

		// setArtikelartsprFromArtikelartsprDto(artikelartspr,
		// artikelartDto.getArtikelartsprDto());
		// }
		// }
		// }
		// catch (CreateException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
		// e);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// }
	}

	public ArtikelartDto artikelartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		// try {
		Artikelart artikelart = em.find(Artikelart.class, cNr);
		if (artikelart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelartFindByPrimaryKey. Es gibt keine Cnr "
							+ cNr);
		}
		return assembleArtikelartDto(artikelart);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void createArtikelartspr(ArtikelartsprDto artikelartsprDto)
			throws EJBExceptionLP {
		if (artikelartsprDto == null) {

		}
		try {
			Artikelartspr artikelartspr = new Artikelartspr(
					artikelartsprDto.getLocaleCNr(),
					artikelartsprDto.getArtikelartCNr());
			em.persist(artikelartspr);
			em.flush();
			setArtikelartsprFromArtikelartsprDto(artikelartspr,
					artikelartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private void setArtikellieferantstaffelFromArtikellieferantstaffelDto(
			Artikellieferantstaffel artikellieferantstaffel,
			ArtikellieferantstaffelDto artikellieferantstaffelDto) {
		artikellieferantstaffel
				.setArtikellieferantIId(artikellieferantstaffelDto
						.getArtikellieferantIId());
		artikellieferantstaffel.setNMenge(artikellieferantstaffelDto
				.getNMenge());
		artikellieferantstaffel.setFRabatt(artikellieferantstaffelDto
				.getFRabatt());
		artikellieferantstaffel.setNNettopreis(Helper.rundeKaufmaennisch(
				artikellieferantstaffelDto.getNNettopreis(), 4));
		artikellieferantstaffel.setTPreisgueltigab(artikellieferantstaffelDto
				.getTPreisgueltigab());
		artikellieferantstaffel.setTPreisgueltigbis(artikellieferantstaffelDto
				.getTPreisgueltigbis());
		artikellieferantstaffel.setBRabattbehalten(artikellieferantstaffelDto
				.getBRabattbehalten());
		artikellieferantstaffel.setTAendern(artikellieferantstaffelDto
				.getTAendern());
		artikellieferantstaffel
				.setPersonalIIdAendern(artikellieferantstaffelDto
						.getPersonalIIdAendern());
		artikellieferantstaffel
				.setIWiederbeschaffungszeit(artikellieferantstaffelDto
						.getIWiederbeschaffungszeit());
		em.merge(artikellieferantstaffel);
		em.flush();
	}

	private ArtikellieferantstaffelDto assembleArtikellieferantstaffelDto(
			Artikellieferantstaffel artikellieferantstaffel) {
		return ArtikellieferantstaffelDtoAssembler
				.createDto(artikellieferantstaffel);
	}

	private ArtikellieferantstaffelDto[] assembleArtikellieferantstaffelDtos(
			Collection<?> artikellieferantstaffels) {
		List<ArtikellieferantstaffelDto> list = new ArrayList<ArtikellieferantstaffelDto>();
		if (artikellieferantstaffels != null) {
			Iterator<?> iterator = artikellieferantstaffels.iterator();
			while (iterator.hasNext()) {
				Artikellieferantstaffel artikellieferantstaffel = (Artikellieferantstaffel) iterator
						.next();
				list.add(assembleArtikellieferantstaffelDto(artikellieferantstaffel));
			}
		}
		ArtikellieferantstaffelDto[] returnArray = new ArtikellieferantstaffelDto[list
				.size()];
		return (ArtikellieferantstaffelDto[]) list.toArray(returnArray);
	}

	public Integer createArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikellieferantstaffelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellieferantstaffelDto == null"));
		}
		if (artikellieferantstaffelDto.getArtikellieferantIId() == null
				|| artikellieferantstaffelDto.getNNettopreis() == null
				|| artikellieferantstaffelDto.getTPreisgueltigab() == null
				|| artikellieferantstaffelDto.getNMenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"artikellieferantstaffelDto.getArtikellieferantIId() == null || artikellieferantstaffelDto.getNNettopreis() == null || artikellieferantstaffelDto.getDPreisgueltigab() == null || artikellieferantstaffelDto.getFMenge() == null"));
		}
		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em
					.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
			query.setParameter(1,
					artikellieferantstaffelDto.getArtikellieferantIId());
			query.setParameter(2, artikellieferantstaffelDto.getNMenge());
			query.setParameter(3,
					artikellieferantstaffelDto.getTPreisgueltigab());
			// @todo getSingleResult oder getResultList ?
			Artikellieferantstaffel doppelt = (Artikellieferantstaffel) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELLIEFERANTSTAFFEL.UK"));
		} catch (NoResultException ex) {

		}
		artikellieferantstaffelDto.setNNettopreis(Helper.rundeKaufmaennisch(
				artikellieferantstaffelDto.getNNettopreis(), 4));
		artikellieferantstaffelDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		artikellieferantstaffelDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_ARTIKELLIEFERANTSTAFFEL);
		artikellieferantstaffelDto.setIId(pk);

		try {
			Artikellieferantstaffel artikellieferantstaffel = new Artikellieferantstaffel(
					artikellieferantstaffelDto.getIId(),
					artikellieferantstaffelDto.getArtikellieferantIId(),
					artikellieferantstaffelDto.getNMenge(),
					artikellieferantstaffelDto.getFRabatt(),
					artikellieferantstaffelDto.getNNettopreis(),
					artikellieferantstaffelDto.getTPreisgueltigab(),
					artikellieferantstaffelDto.getBRabattbehalten(),
					artikellieferantstaffelDto.getPersonalIIdAendern(),
					artikellieferantstaffelDto.getTAendern());
			em.persist(artikellieferantstaffel);
			em.flush();
			setArtikellieferantstaffelFromArtikellieferantstaffelDto(
					artikellieferantstaffel, artikellieferantstaffelDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return artikellieferantstaffelDto.getIId();
	}

	public void removeArtikellieferantstaffel(Integer iId)
			throws EJBExceptionLP {
		// try {
		Artikellieferantstaffel toRemove = em.find(
				Artikellieferantstaffel.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikellieferantstaffel. Es gibt keine iid "
							+ iId);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws EJBExceptionLP {
		if (artikellieferantstaffelDto != null) {
			Integer iId = artikellieferantstaffelDto.getIId();
			removeArtikellieferantstaffel(iId);
		}
	}

	public void updateArtikellieferantstaffel(
			ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellieferantstaffelDto != null) {
			Integer iId = artikellieferantstaffelDto.getIId();
			// try {
			Artikellieferantstaffel artikellieferantstaffel = em.find(
					Artikellieferantstaffel.class, iId);
			if (artikellieferantstaffel == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateArtikellieferantstaffel es gibt keine iid "
								+ iId + "\ndto.tostring: "
								+ artikellieferantstaffelDto.toString());
			}
			artikellieferantstaffelDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			artikellieferantstaffelDto.setTAendern(new java.sql.Timestamp(
					System.currentTimeMillis()));
			setArtikellieferantstaffelFromArtikellieferantstaffelDto(
					artikellieferantstaffel, artikellieferantstaffelDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateArtikellieferantstaffels(
			ArtikellieferantstaffelDto[] artikellieferantstaffelDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellieferantstaffelDtos != null) {
			for (int i = 0; i < artikellieferantstaffelDtos.length; i++) {
				updateArtikellieferantstaffel(artikellieferantstaffelDtos[i],
						theClientDto);
			}
		}
	}

	/**
	 * Gibt den Einkaufspreis in der gew&uuml;nschten W&auml;hunrg des
	 * Erst-gereihten Lieferanten eines Artikels pro Einheit zurueck
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param fMenge
	 *            BigDecimal
	 * @param waehrungCNr
	 *            gewuenschte Waehrung
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId,
			BigDecimal fMenge, String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getArtikelEinkaufspreis(artikelIId, null, fMenge, waehrungCNr,
				null, theClientDto);
	}

	public ArtikellieferantDto getGuenstigstenEKPreis(Integer artikelIId,
			BigDecimal bdMenge, java.sql.Date zeitpunkt, String waehrungCNr,
			Integer lieferantIIdVergleich, TheClientDto theClientDto) {
		ArtikellieferantDto alZurueckDto = null;
		String sQueryLieferanten = "SELECT distinct al.lieferant_i_id "
				+ " FROM FLRArtikellieferant AS al WHERE al.artikel_i_id="
				+ artikelIId + " AND al.t_preisgueltigab <='"
				+ Helper.formatDateWithSlashes(zeitpunkt) + "'";

		if (lieferantIIdVergleich != null) {
			sQueryLieferanten += " AND al.lieferant_i_id<>"
					+ lieferantIIdVergleich;
		}

		Session sessionLieferanten = FLRSessionFactory.getFactory()
				.openSession();
		org.hibernate.Query lieferanten = sessionLieferanten
				.createQuery(sQueryLieferanten);

		List<?> resultListLieferanten = lieferanten.list();
		Iterator<?> resultListIteratorLieferanten = resultListLieferanten
				.iterator();

		while (resultListIteratorLieferanten.hasNext()) {
			Integer lieferant_i_id = (Integer) resultListIteratorLieferanten
					.next();

			String sQuery = "SELECT al "
					+ " FROM FLRArtikellieferant AS al WHERE al.artikel_i_id="
					+ artikelIId
					+ " AND al.lieferant_i_id="
					+ lieferant_i_id
					+ "  AND al.flrlieferant.t_bestellsperream IS NULL AND al.t_preisgueltigab <='"
					+ Helper.formatDateWithSlashes(zeitpunkt)
					+ "' ORDER BY al.t_preisgueltigab DESC";
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);
			inventurliste.setMaxResults(1);
			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				FLRArtikellieferant al = (FLRArtikellieferant) resultListIterator
						.next();
				Set s = al.getStaffelset();

				if (al.getN_nettopreis() == null) {
					continue;
				}

				com.lp.server.partner.service.LieferantDto lieferantDto;
				try {
					lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
							al.getLieferant_i_id(), theClientDto);

					BigDecimal nettopreis = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									al.getN_nettopreis(),
									lieferantDto.getWaehrungCNr(), waehrungCNr,
									new Date(System.currentTimeMillis()),
									theClientDto);

					if (alZurueckDto == null) {
						alZurueckDto = new ArtikellieferantDto();
						alZurueckDto.setLieferantIId(al.getLieferant_i_id());
						alZurueckDto.setNNettopreis(nettopreis);
					} else {
						if (al.getN_nettopreis().doubleValue() < alZurueckDto
								.getNNettopreis().doubleValue()) {
							alZurueckDto
									.setLieferantIId(al.getLieferant_i_id());
							alZurueckDto.setNNettopreis(nettopreis);
						}
					}

					Iterator it = s.iterator();

					while (it.hasNext()) {

						FLRArtikellieferantstaffel staffel = (FLRArtikellieferantstaffel) it
								.next();

						if (staffel.getT_preisgueltigab().getTime() <= zeitpunkt
								.getTime()) {

							if (staffel.getN_menge().doubleValue() <= bdMenge
									.doubleValue()) {

								BigDecimal nettopreisStaffel = getLocaleFac()
										.rechneUmInAndereWaehrungZuDatum(
												staffel.getN_nettopreis(),
												lieferantDto.getWaehrungCNr(),
												waehrungCNr,
												new Date(System
														.currentTimeMillis()),
												theClientDto);

								if (nettopreisStaffel.doubleValue() < alZurueckDto
										.getNNettopreis().doubleValue()) {
									alZurueckDto.setLieferantIId(al
											.getLieferant_i_id());
									alZurueckDto
											.setNNettopreis(nettopreisStaffel);
								}
							}

						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		return alZurueckDto;
	}

	public Map getAlleGueltigenStaffelneinesLieferanten(
			Integer artikellieferantIId, java.sql.Date dDatum,
			String waehrungCNrGewuenschteWaehrung, TheClientDto theClientDto) {

		Map m = new LinkedHashMap();

		ArtikellieferantDto artikellieferantDto = artikellieferantFindByPrimaryKey(
				artikellieferantIId, theClientDto);
		ArtikelDto artikelDto = artikelFindByPrimaryKeySmall(
				artikellieferantDto.getArtikelIId(), theClientDto);
		Session session = FLRSessionFactory.getFactory().openSession();

		String query = "SELECT st.n_menge FROM FLRArtikellieferantstaffel st WHERE st.artikellieferant_i_id="
				+ artikellieferantIId
				+ " AND st.t_preisgueltigab<= '"
				+ Helper.formatDateWithSlashes(dDatum)
				+ "' GROUP BY st.n_menge ORDER BY st.n_menge ASC";
		org.hibernate.Query herst = session.createQuery(query);

		List results = herst.list();

		Iterator it = results.iterator();
		while (it.hasNext()) {
			BigDecimal nMenge = (BigDecimal) it.next();
			Query q = em
					.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigabKleiner");
			q.setParameter(1, artikellieferantIId);
			q.setParameter(2, nMenge);
			q.setParameter(3, dDatum);

			Collection c = q.getResultList();

			if (c.size() > 0) {
				Artikellieferantstaffel als = (Artikellieferantstaffel) c
						.iterator().next();

				BigDecimal nettopreis = als.getNNettopreis();

				try {
					nettopreis = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									als.getNNettopreis(),
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr(),
									waehrungCNrGewuenschteWaehrung, dDatum,
									theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				m.put(als.getIId(),
						Helper.formatZahl(als.getNMenge(), 3,
								theClientDto.getLocUi())
								+ " "
								+ artikelDto.getEinheitCNr().trim()
								+ " "
								+ Helper.formatBetrag(nettopreis,
										theClientDto.getLocUi())
								+ " "
								+ waehrungCNrGewuenschteWaehrung);

			}

		}

		return m;
	}

	/**
	 * Gibt den Einkaufspreis in der gew&uuml;nschten W&auml;hrung eines
	 * Lieferanten eines Artikels pro Einheit zurueck
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lieferantIId
	 *            Integer
	 * @param fMenge
	 *            BigDecimal
	 * @param waehrungCNr
	 *            gewuenschte Waehrung
	 * @param theClientDto
	 *            String
	 * @return ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId,
			Integer lieferantIId, BigDecimal fMenge, String waehrungCNr,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto) {
		// check2(idUser);
		if (artikelIId == null || fMenge == null || waehrungCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || fMenge == null || waehrungCNr == null"));
		}

		if (tDatumPreisgueltigkeit == null) {
			tDatumPreisgueltigkeit = Helper.cutDate(new java.sql.Date(System
					.currentTimeMillis()));
		}

		fMenge = Helper.rundeKaufmaennisch(fMenge, 4);

		ArtikellieferantDto dto = null;
		if (lieferantIId == null) {
			// Wenn lieferantIId nicht vorhanden, dann wird der 1. Gereihte
			// verwendet
			ArtikellieferantDto[] dtos = artikellieferantfindByArtikelIIdTPreisgueltigab(
					artikelIId, tDatumPreisgueltigkeit, theClientDto);
			if (dtos.length > 0) {
				dto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
						artikelIId, dtos[0].getLieferantIId(),
						tDatumPreisgueltigkeit, theClientDto);

			}
		} else {
			try {

				dto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
						artikelIId, lieferantIId, tDatumPreisgueltigkeit,
						theClientDto);
			} catch (EJBExceptionLP ex) {
				if (ex.getCode() != EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
					throw ex;
				}

			}
		}
		// Wenn es keinen Artikellieferant gibt, wird automatisch null
		// zurueckgegeben
		if (dto == null) {
			return null;
		} else {
			// Wenn kein Einzelpreis definiert ist, dann wird auch null
			// zurueckgegeben
			if (dto.getNEinzelpreis() != null) {
				ArtikellieferantstaffelDto[] staffeln = artikellieferantstaffelFindByArtikellieferantIIdFMenge(
						dto.getIId(), fMenge, tDatumPreisgueltigkeit);

				if (staffeln.length > 0) {
					ArtikellieferantstaffelDto staffel = staffeln[0];
					if (staffel.getFRabatt() != null) {
						dto.setFRabatt(staffel.getFRabatt().doubleValue());
					}
					dto.setBRabattbehalten(staffel.getBRabattbehalten());
					dto.setNNettopreis(staffel.getNNettopreis());
					dto.setArtikellieferantstaffelIId(staffel.getIId());
					dto.setNStaffelmenge(staffel.getNMenge());

					if (staffel.getIWiederbeschaffungszeit() != null) {
						dto.setIWiederbeschaffungszeit(staffel
								.getIWiederbeschaffungszeit());
					}
				}

				// Von Lieferantenwaehrung in gewuenschte Waehrung umrechnen
				try {
					com.lp.server.partner.service.LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(dto.getLieferantIId(),
									theClientDto);

					dto.setLieferantDto(lieferantDto);

					Date datum = new Date(System.currentTimeMillis());
					if (dto.getNNettopreis() != null) {

						BigDecimal zuschlag = getMaterialFac()
								.getMaterialzuschlagEKInZielwaehrung(
										artikelIId, dto.getLieferantIId(),
										tDatumPreisgueltigkeit, waehrungCNr,
										theClientDto);
						if (zuschlag != null) {
							dto.setNMaterialzuschlag(Helper.rundeKaufmaennisch(
									zuschlag, 4));
						} else {
							dto.setNMaterialzuschlag(new BigDecimal(0));
						}
						dto.setNNettopreis(getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										dto.getNNettopreis(),
										lieferantDto.getWaehrungCNr(),
										waehrungCNr, datum, theClientDto));
					}
					if (dto.getNEinzelpreis() != null) {
						dto.setNEinzelpreis(getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										dto.getNEinzelpreis(),
										lieferantDto.getWaehrungCNr(),
										waehrungCNr, datum, theClientDto));
					}
					if (dto.getNFixkosten() != null) {
						dto.setNFixkosten(getLocaleFac()
								.rechneUmInAndereWaehrungZuDatum(
										dto.getNFixkosten(),
										lieferantDto.getWaehrungCNr(),
										waehrungCNr, datum, theClientDto));
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				return dto;
			} else {
				return null;
			}
		}
	}

	public ArtikellieferantstaffelDto artikellieferantstaffelFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikellieferantstaffel artikellieferantstaffel = em.find(
				Artikellieferantstaffel.class, iId);
		if (artikellieferantstaffel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikellieferantstaffelFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}

		return assembleArtikellieferantstaffelDto(artikellieferantstaffel);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIIdFMenge(
			Integer artikellieferantIId, BigDecimal fMenge, java.sql.Date dDatum)
			throws EJBExceptionLP {
		if (artikellieferantIId == null || fMenge == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikellieferantIId == null || fMenge == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMenge");
		query.setParameter(1, artikellieferantIId);
		query.setParameter(2, fMenge);
		query.setParameter(3, dDatum);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		return assembleArtikellieferantstaffelDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIId(
			Integer artikellieferantIId) throws EJBExceptionLP {
		if (artikellieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikellieferantIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
		query.setParameter(1, artikellieferantIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		return assembleArtikellieferantstaffelDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

	}

	/**
	 * Einzeilige Artikelbezeichnung fuer die UI Darstellung zusammenbauen. <br>
	 * Wird beispielsweise in FLR Listen verwendet.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param locBezeichnungI
	 *            das gewuenschte Locale
	 * @return String
	 */
	public String formatArtikelbezeichnungEinzeiligOhneExc(Integer iIdArtikelI,
			Locale locBezeichnungI) {
		myLogger.entry();
		StringBuffer sbBez = new StringBuffer();
		if (iIdArtikelI != null) {
			// try {
			Artikel oArtikel = em.find(Artikel.class, iIdArtikelI);
			if (oArtikel == null) {
				// ignore, leere Artikelbezeichnung
				myLogger.warn("formatArtikelbezeichnungEinzeiligOhneExc");
			}
			// nach 5.0.5 Mandantenparameter: cnr oder die sprachabhaengige bez
			// angezeigt werden?

			if (locBezeichnungI != null) {
				try {
					Query query = em
							.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
					query.setParameter(1, iIdArtikelI);
					query.setParameter(2, Helper.locale2String(locBezeichnungI));
					Artikelspr oArtikelspr = (Artikelspr) query
							.getSingleResult();

					// CK->UW
					// umgebaut lt. tel mit WH vom 28.12.05:
					// Zusatzubezeichnung muss immer mitangedruckt werden

					if (oArtikelspr.getCBez() != null
							&& oArtikelspr.getCBez().length() > 0) {
						sbBez.append(oArtikelspr.getCBez());
						sbBez.append(" ");
						if (oArtikelspr.getCZbez() != null
								&& oArtikelspr.getCZbez().length() > 0) {
							sbBez.append("\n");
							sbBez.append(oArtikelspr.getCZbez());
						}
					} else {
						sbBez.append(oArtikel.getCNr()); // greift im Notfall
					}
				} catch (Throwable t) {
					// es gibt die gewuenschte Uebersetzung nicht
					sbBez.append(oArtikel.getCNr());
				}

			} else {
				sbBez.append(oArtikel.getCNr());
			}
			// }
			// catch (Throwable t) {
			// // ignore, leere Artikelbezeichnung
			// myLogger.warn("formatArtikelbezeichnungEinzeiligOhneExc", t);
			// }
		}

		return sbBez.toString();
	}

	/**
	 * Die vollstaendige Artikelbezeichnung zum Andrucken zusammenbauen. Wird
	 * ein locale angegeben,betrifft das nicht die uebersteuerten Bez und ZBez
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param cNrPositionsartI
	 *            muss LocaleFac.POSITIONSART_IDENT oder
	 *            LocaleFac.POSITIONSART_HANDEINGABE sein
	 * @param cBezUebersteuertI
	 *            die Artikelbezeichnung kann eventuell uebersteuert sein
	 * @param cZBezUebersteuertI
	 *            String
	 * @param bIncludeCNrI
	 *            inklusive cnr
	 * @param localeI
	 *            localeI
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String baueArtikelBezeichnungMehrzeilig(Integer iIdArtikelI,
			String cNrPositionsartI, String cBezUebersteuertI,
			String cZBezUebersteuertI, boolean bIncludeCNrI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (cNrPositionsartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrPositionsartI == null"));
		}

		if (!cNrPositionsartI.equals(LocaleFac.POSITIONSART_IDENT)
				&& !cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
					new Exception(
							"!cNrPositionsartI.equals(LocaleFac.POSITIONSART_IDENT) && !cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)"));
		}

		ArtikelDto artikelDto = null;

		artikelDto = artikelFindByPrimaryKey(iIdArtikelI, theClientDto);

		// Uebersetzung in Kundenlocale: Uebersteuerte Texte sind davon nicht
		// betroffen

		Artikelspr oArtikelsprInKundeLocale = null;
		if (localeI != null) {
			try {
				Query query = em
						.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
				query.setParameter(1, iIdArtikelI);
				query.setParameter(2, Helper.locale2String(localeI));
				oArtikelsprInKundeLocale = (Artikelspr) query.getSingleResult();
			} catch (Throwable t) {
				// es gibt die gewuenschte Uebersetzung nicht
			}
		}

		StringBuffer sbBezeichnung = new StringBuffer();

		if (cNrPositionsartI.equals(LocaleFac.POSITIONSART_IDENT)) {
			if (bIncludeCNrI) {
				sbBezeichnung.append(artikelDto.getCNr());
			}

			if (cBezUebersteuertI != null) {
				if (bIncludeCNrI) {
					sbBezeichnung.append("\n");
				}

				sbBezeichnung.append(cBezUebersteuertI);
			} else {
				// Artikel in Kundensprache, wenn Bez null ist , soll default
				// Sprache verwendet werden
				if (oArtikelsprInKundeLocale != null
						&& oArtikelsprInKundeLocale.getCBez() != null) {
					if (bIncludeCNrI) {
						sbBezeichnung.append("\n");
					}

					sbBezeichnung.append(oArtikelsprInKundeLocale.getCBez());

				} else if (artikelDto.getArtikelsprDto() != null) {

					if (artikelDto.getArtikelsprDto().getCBez() != null) {
						if (bIncludeCNrI) {
							sbBezeichnung.append("\n");
						}

						sbBezeichnung.append(artikelDto.getArtikelsprDto()
								.getCBez());
					}
				}
			}

			if (cZBezUebersteuertI != null) {
				if (bIncludeCNrI) {
					sbBezeichnung.append("\n");
				} else {
					// Stueckliste Bez und Zusatzbez werden sonst direkt
					// zusammengeschrieben.
					sbBezeichnung.append(" ");
				}

				sbBezeichnung.append(cZBezUebersteuertI);
			} else {
				// Artikel in Kundensprache
				if (oArtikelsprInKundeLocale != null) {
					if (oArtikelsprInKundeLocale.getCZbez() != null) {
						if (bIncludeCNrI) {
							sbBezeichnung.append("\n");
						} else {
							// Stueckliste Bez und Zusatzbez werden sonst direkt
							// zusammengeschrieben.
							sbBezeichnung.append(" ");
						}
						// sbBezeichnung.append("\n");
						sbBezeichnung.append(oArtikelsprInKundeLocale
								.getCZbez());
					}

				} else if (artikelDto.getArtikelsprDto() != null) {
					if (artikelDto.getArtikelsprDto().getCZbez() != null) {
						if (bIncludeCNrI) {
							sbBezeichnung.append("\n");
						} else {
							// Stueckliste Bez und Zusatzbez werden sonst direkt
							// zusammengeschrieben.
							sbBezeichnung.append(" ");
						}
						// sbBezeichnung.append("\n");
						sbBezeichnung.append(artikelDto.getArtikelsprDto()
								.getCZbez());
					}
				}

			}

		} else if (cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			sbBezeichnung.append(artikelDto.getArtikelsprDto().getCBez());

			if (artikelDto.getArtikelsprDto().getCZbez() != null) {
				sbBezeichnung.append("\n").append(
						artikelDto.getArtikelsprDto().getCZbez());
			}

		}

		// System.out.println(">> exit ");
		return sbBezeichnung.toString();
	}

	/**
	 * Die vollstaendige Artikelbezeichnung zum Andrucken zusammenbauen.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param cNrPositionsartI
	 *            muss LocaleFac.POSITIONSART_IDENT oder
	 *            LocaleFac.POSITIONSART_HANDEINGABE sein
	 * @param cBezUebersteuertI
	 *            die Artikelbezeichnung kann eventuell uebersteuert sein
	 * @param cZBezUebersteuertI
	 *            String
	 * @param bIncludeCNrI
	 *            inklusive cnr
	 * @param locale
	 *            locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String baueArtikelBezeichnungMehrzeiligOhneExc(Integer iIdArtikelI,
			String cNrPositionsartI, String cBezUebersteuertI,
			String cZBezUebersteuertI, boolean bIncludeCNrI, Locale locale,
			TheClientDto theClientDto) {
		myLogger.entry();

		String cBez = null;

		try {
			cBez = baueArtikelBezeichnungMehrzeilig(iIdArtikelI,
					cNrPositionsartI, cBezUebersteuertI, cZBezUebersteuertI,
					bIncludeCNrI, locale, theClientDto);
		} catch (Throwable t) {
			// do nothing
		}

		return cBez;
	} // Als Kunde oder Lieferant? PartnerIId fehlt

	public Node getItemAsNode(Document docI, Integer iIdArtikelI, String idUser) {

		if (docI == null) {
			throw new IllegalArgumentException("docI==null");
		}
		if (iIdArtikelI == null) {
			throw new IllegalArgumentException("iIdArtikelI==null");
		}
		if (idUser == null) {
			throw new IllegalArgumentException("theClientDto==null");
		}

		TheClientDto theClientDto = check(idUser);

		Element nodeItemRet = docI.createElement(SystemFac.SCHEMA_OF_ITEM);
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				iIdArtikelI, theClientDto);

		// ItemType: Description.
		Helper.addOFElement(nodeItemRet, docI,
				artikelDto.formatArtikelbezeichnung(),
				ArtikelFac.SCHEMA_OF_ITEM_DESCRIPTION);

		// ItemType: CountryOfOrigin.
		// elemOF =
		// docI.createElement(ArtikelFac.SCHEMA_OF_ITEM_COUNTRY_OF_ORIGIN);
		// elemOF.appendChild(docI.createTextNode(
		// "JO->WH SCHEMA_OF_ITEM_COUNTRY_OF_ORIGIN"));
		// nodeWhere2AddI.appendChild(elemOF);

		// ItemType: CountryOfProduction.
		// elemOF =
		// docI.createElement(ArtikelFac.SCHEMA_OF_ITEM_COUNTRY_OF_PRODUCTION);
		// elemOF.appendChild(docI.createTextNode(
		// "JO->WH SCHEMA_OF_ITEM_COUNTRY_OF_PRODUCTION"));
		// nodeWhere2AddI.appendChild(elemOF);

		// ItemType: CustomsTariff. WARENVERKEHRSNUMMER
		Helper.addOFElement(nodeItemRet, docI,
				artikelDto.getCWarenverkehrsnummer(),
				ArtikelFac.SCHEMA_OF_ITEM_CUSTOMS_TARIFF);

		// ItemType: EAN.
		Helper.addOFElement(nodeItemRet, docI, artikelDto.getCVerkaufseannr(),
				ArtikelFac.SCHEMA_OF_ITEM_EAN);

		// ItemType: EngineeringID. ->Zeichnungsnummer
		// elemOF =
		// docI.createElement(ArtikelFac.SCHEMA_OF_ITEM_ENGINEERING_ID);
		// elemOF.appendChild(docI.createTextNode(
		// "JO->WH SCHEMA_OF_ITEM_ENGINEERING_ID"));
		// nodeWhere2AddI.appendChild(elemOF);

		// ItemType: ExportProcedureIndicator. ->Was ist das? Klaeren
		// elemOF =
		// docI.createElement(ArtikelFac.SCHEMA_OF_ITEM_EXPORT_PROCEDURE_INDICATOR
		// );
		// elemOF.appendChild(docI.createTextNode(
		// "JO->WH SCHEMA_OF_ITEM_EXPORT_PROCEDURE_INDICATOR"));
		// nodeWhere2AddI.appendChild(elemOF);

		// ItemType: Hazard. -> Gefahrengutnummer
		// elemOF = docI.createElement(ArtikelFac.SCHEMA_OF_ITEM_HAZARD);
		// elemOF.appendChild(docI.createTextNode("JO->WH SCHEMA_OF_ITEM_HAZARD")
		// );
		// nodeWhere2AddI.appendChild(elemOF);

		return nodeItemRet;
	}

	/**
	 * fuer adi zur system q&d; JO->CK klaeren
	 * 
	 * @param sArtikelI
	 *            String
	 * @param idUser
	 *            String
	 * @return String
	 */

	@WebMethod
	@WebResult(name = "StringDocument")
	public String getItemAsStringDocumentWS(
			@WebParam(name = "sArtikelnr") String sArtikelI,
			@WebParam(name = "sUserID") String idUser) {
		LagerDto lagerDto = null;
		ArtikelDto artikelDto = null;
		BigDecimal bdLagerstand = new BigDecimal(-1);

		String asXML = null;

		Document doc = new DocumentImpl();
		String sLagerplatz = "";

		TheClientDto theClientDto = check(idUser);

		try {
			lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);
			artikelDto = getArtikelFac().artikelFindByCNr(sArtikelI,
					theClientDto);
			bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(),
					lagerDto.getIId(), theClientDto);
			ArtikellagerplaetzeDto artLagerPlaetzeDto = getLagerFac()
					.artikellagerplaetzeFindByArtikelIIdLagerIId(
							artikelDto.getIId(), lagerDto.getIId());
			if (artLagerPlaetzeDto != null) {
				if (artLagerPlaetzeDto.getLagerplatzDto() != null) {
					sLagerplatz = artLagerPlaetzeDto.getLagerplatzDto()
							.getCLagerplatz();
				}
			}

			Node nodeFeatures = doc.createElement(SystemFac.SCHEMA_OF_FEATURES);
			doc.appendChild(nodeFeatures);

			Node nodeFeature = doc.createElement(SystemFac.SCHEMA_OF_FEATURE);
			Node node = doc
					.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
			node.appendChild(doc.createTextNode("Lagerstand"));
			nodeFeature.appendChild(node);
			node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_VALUE);
			node.appendChild(doc.createTextNode(bdLagerstand.toPlainString()));
			nodeFeature.appendChild(node);
			nodeFeatures.appendChild(nodeFeature);

			nodeFeature = doc.createElement(SystemFac.SCHEMA_OF_FEATURE);
			node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
			node.appendChild(doc.createTextNode("formatArtikelbezeichnung"));
			nodeFeature.appendChild(node);
			node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_VALUE);
			node.appendChild(doc.createTextNode(artikelDto
					.formatArtikelbezeichnung()));
			nodeFeature.appendChild(node);
			nodeFeatures.appendChild(nodeFeature);

			if (sLagerplatz != null) {
				nodeFeature = doc.createElement(SystemFac.SCHEMA_OF_FEATURE);
				node = doc
						.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
				node.appendChild(doc.createTextNode("Lagerort"));
				nodeFeature.appendChild(node);
				node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_VALUE);
				node.appendChild(doc.createTextNode(sLagerplatz));
				nodeFeature.appendChild(node);
				nodeFeatures.appendChild(nodeFeature);
			}

			asXML = Helper.xML2String(doc);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (IOException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return asXML;
	}

	public Integer createFarbcode(FarbcodeDto farbcodeDto)
			throws EJBExceptionLP {
		if (farbcodeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("farbcodeDto == null"));
		}
		if (farbcodeDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("farbcodeDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("FarbcodefindByCNr");
			query.setParameter(1, farbcodeDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Farbcode doppelt = (Farbcode) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_FARBCODE.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FARBCODE);
			farbcodeDto.setIId(pk);

			Farbcode farbcode = new Farbcode(farbcodeDto.getIId(),
					farbcodeDto.getCNr());
			em.persist(farbcode);
			em.flush();
			setFarbcodeFromFarbcodeDto(farbcode, farbcodeDto);
			return farbcodeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVorschlagstext(VorschlagstextDto vorschlagstextDto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("VorschlagstextFindByLocaleCNrCBez");
			query.setParameter(1, theClientDto.getLocUiAsString());
			query.setParameter(2, vorschlagstextDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Vorschlagstext doppelt = (Vorschlagstext) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_VORSCHLAGSTEXT.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		vorschlagstextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VORSCHLAGSTEXT);
			vorschlagstextDto.setIId(pk);

			Vorschlagstext vorschlagstext = new Vorschlagstext(
					vorschlagstextDto.getIId(),
					vorschlagstextDto.getLocaleCNr(),
					vorschlagstextDto.getCBez());
			em.persist(vorschlagstext);
			em.flush();
			setVorschlagstextFromVorschlagstextDto(vorschlagstext,
					vorschlagstextDto);
			return vorschlagstextDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	// REACH
	public Integer createReach(ReachDto dto) {

		try {
			Query query = em.createNamedQuery("ReachfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Reach doppelt = (Reach) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_REACH.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REACH);
			dto.setIId(pk);

			Reach bean = new Reach(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setReachFromReachDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVorzug(VorzugDto dto) {

		try {
			Query query = em.createNamedQuery("VorzugfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Vorzug doppelt = (Vorzug) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_VORZUG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VORZUG);
			dto.setIId(pk);

			Vorzug bean = new Vorzug(dto.getIId(), dto.getCNr(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setVorzugFromVorzugDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createAllergen(AlergenDto dto) {

		try {
			Query query = em.createNamedQuery("AlergenfindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Alergen doppelt = (Alergen) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ALERGEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		Query queryNext = em.createNamedQuery("AlergenejbSelectNextReihung");
		queryNext.setParameter(1, dto.getMandantCNr());

		Integer i = (Integer) queryNext.getSingleResult();

		if (i == null) {
			i = new Integer(0);
		}
		i = new Integer(i.intValue() + 1);
		dto.setISort(i);

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ALERGEN);
			dto.setIId(pk);

			Alergen bean = new Alergen(dto.getIId(), dto.getISort(),
					dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setAlergenFromAlergenDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	// ROHS
	public Integer createRohs(RohsDto dto) {

		try {
			Query query = em.createNamedQuery("RohsfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Rohs doppelt = (Rohs) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ROHS.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ROHS);
			dto.setIId(pk);

			Rohs bean = new Rohs(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setRohsFromRohsDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	// AUTOMOTIVE
	public Integer createAutomotive(AutomotiveDto dto) {

		try {
			Query query = em.createNamedQuery("AutomotivefindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Automotive doppelt = (Automotive) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_AUTOMOTIVE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUTOMOTIVE);
			dto.setIId(pk);

			Automotive bean = new Automotive(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setAutomotiveFromAutomotiveDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	// MEDICAL
	public Integer createMedicale(MedicalDto dto) {

		try {
			Query query = em.createNamedQuery("MedicalfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Medical doppelt = (Medical) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_MEDICAL.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MEDICAL);
			dto.setIId(pk);

			Medical bean = new Medical(dto.getIId(), dto.getMandantCNr(),
					dto.getCBez());
			em.persist(bean);
			em.flush();
			setMedicalFromMedicalDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateVorzug(VorzugDto dto) {
		Vorzug bean = em.find(Vorzug.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("VorzugfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Vorzug) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_VORZUG.UK"));
			}
		} catch (NoResultException ex) {

		}

		setVorzugFromVorzugDto(bean, dto);
	}

	public void updateAllergen(AlergenDto dto) {
		Alergen bean = em.find(Alergen.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("AlergenfindByMandantCNrCBez");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Alergen) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ALERGEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setAlergenFromAlergenDto(bean, dto);
	}

	public void updateReach(ReachDto dto) {
		Reach bean = em.find(Reach.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("ReachfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Reach) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_REACH.UK"));
			}
		} catch (NoResultException ex) {

		}

		setReachFromReachDto(bean, dto);
	}

	public void updateRohs(RohsDto dto) {
		Rohs bean = em.find(Rohs.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("RohsfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Rohs) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ROHS.UK"));
			}
		} catch (NoResultException ex) {

		}

		setRohsFromRohsDto(bean, dto);
	}

	public void updateAutomotive(AutomotiveDto dto) {
		Automotive bean = em.find(Automotive.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("AutomotivefindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Automotive) query.getSingleResult())
					.getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_AUTOMOTIVE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setAutomotiveFromAutomotiveDto(bean, dto);
	}

	public void updateMedical(MedicalDto dto) {
		Medical bean = em.find(Medical.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("MedicalfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Medical) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_MEDICAL.UK"));
			}
		} catch (NoResultException ex) {

		}

		setMedicalFromMedicalDto(bean, dto);
	}

	public void removeReach(ReachDto dto) {
		Reach toRemove = em.find(Reach.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeVorzug(VorzugDto dto) {
		Vorzug toRemove = em.find(Vorzug.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAllergen(AlergenDto dto) {
		Alergen toRemove = em.find(Alergen.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeRohs(RohsDto dto) {
		Rohs toRemove = em.find(Rohs.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAutomotive(AutomotiveDto dto) {
		Automotive toRemove = em.find(Automotive.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeMedical(MedicalDto dto) {
		Medical toRemove = em.find(Medical.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public ReachDto reachFindByPrimaryKey(Integer iId) {
		Reach bean = em.find(Reach.class, iId);
		return ReachDtoAssembler.createDto(bean);
	}

	public VorzugDto vorzugFindByPrimaryKey(Integer iId) {
		Vorzug bean = em.find(Vorzug.class, iId);
		return VorzugDtoAssembler.createDto(bean);
	}

	public AlergenDto allergenFindByPrimaryKey(Integer iId) {
		Alergen bean = em.find(Alergen.class, iId);
		return AlergenDtoAssembler.createDto(bean);
	}

	public RohsDto rohsFindByPrimaryKey(Integer iId) {
		Rohs bean = em.find(Rohs.class, iId);
		return RohsDtoAssembler.createDto(bean);
	}

	public AutomotiveDto automotiveFindByPrimaryKey(Integer iId) {
		Automotive bean = em.find(Automotive.class, iId);
		return AutomotiveDtoAssembler.createDto(bean);
	}

	public MedicalDto medicalFindByPrimaryKey(Integer iId) {
		Medical bean = em.find(Medical.class, iId);
		return MedicalDtoAssembler.createDto(bean);
	}

	public Integer createVerleih(VerleihDto verleihDto) {
		if (verleihDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("verleihDto == null"));
		}
		if (verleihDto.getITage() == null || verleihDto.getFFaktor() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"verleihDto.getITage() == null || verleihDto.getFFaktor() == null"));
		}
		try {
			Query query = em.createNamedQuery("VerleihfindByITage");
			query.setParameter(1, verleihDto.getITage());
			// @todo getSingleResult oder getResultList ?
			Verleih doppelt = (Verleih) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_VERLEIH.I_TAGE"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERLEIH);
			verleihDto.setIId(pk);

			Verleih farbcode = new Verleih(verleihDto.getIId(),
					verleihDto.getITage(), verleihDto.getFFaktor());
			em.persist(farbcode);
			em.flush();
			setVerleihFromVerleihDto(farbcode, verleihDto);
			return verleihDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeFarbcode(FarbcodeDto dto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}
		// try {
		Farbcode toRemove = em.find(Farbcode.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeFarbcode. Es gibt keine iid "
							+ dto.getIId() + "\ndto.toString: "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void removeVorschlagstext(VorschlagstextDto dto) {

		Vorschlagstext toRemove = em.find(Vorschlagstext.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeVerleih(VerleihDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Verleih toRemove = em.find(Verleih.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeVerleih. Es gibt keine iid "
							+ dto.getIId() + "\ndto.toString: "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateFarbcode(FarbcodeDto farbcodeDto) throws EJBExceptionLP {
		if (farbcodeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("farbcodeDto == null"));
		}
		if (farbcodeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("farbcodeDto.getIId() == null"));
		}
		if (farbcodeDto.getCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("farbcodeDto.getCNr() == null"));
		}

		Integer iId = farbcodeDto.getIId();
		try {
			Farbcode farbcode = em.find(Farbcode.class, iId);

			try {
				Query query = em.createNamedQuery("FarbcodefindByCNr");
				query.setParameter(1, farbcodeDto.getCNr());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Farbcode) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_FARBCODE.C_NR"));
				}
			} catch (NoResultException ex) {

			}

			setFarbcodeFromFarbcodeDto(farbcode, farbcodeDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public void updateVorschlagstext(VorschlagstextDto vorschlagstextDto,
			TheClientDto theClientDto) {

		Integer iId = vorschlagstextDto.getIId();
		try {
			Vorschlagstext vorschlagstext = em.find(Vorschlagstext.class, iId);

			try {
				Query query = em
						.createNamedQuery("VorschlagstextFindByLocaleCNrCBez");
				query.setParameter(1, theClientDto.getLocUiAsString());
				query.setParameter(2, vorschlagstextDto.getCBez());

				Integer iIdVorhanden = ((Vorschlagstext) query
						.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_VORSCHLAGSTEXT.C_NR"));
				}
			} catch (NoResultException ex) {

			}

			setVorschlagstextFromVorschlagstextDto(vorschlagstext,
					vorschlagstextDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public void updateVerleih(VerleihDto verleihDto) {
		if (verleihDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("verleihDto == null"));
		}
		if (verleihDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("verleihDto.getIId() == null"));
		}
		if (verleihDto.getFFaktor() == null || verleihDto.getITage() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"verleihDto.getFFaktor() == null || verleihDto.getITage() == null"));
		}

		Integer iId = verleihDto.getIId();
		try {
			Verleih verleih = em.find(Verleih.class, iId);

			try {
				Query query = em.createNamedQuery("VerleihfindByITage");
				query.setParameter(1, verleihDto.getITage());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Verleih) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_VERLEIH.I_TAGE"));
				}

			} catch (NoResultException ex) {

			}

			setVerleihFromVerleihDto(verleih, verleihDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public String generiereNeueArtikelnummer(String beginnArtikelnummer,
			TheClientDto theClientDto) {

		boolean bLetzterZiffernblock = false;

		if (beginnArtikelnummer == null) {

			beginnArtikelnummer = "";

			try {
				// PJ18234
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_STARTWERT_ARTIKELNUMMER);
				String startwert = parameter.getCWert();
				if (startwert != null && startwert.trim().length() > 0) {
					beginnArtikelnummer = startwert.trim();
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		int iLaengeArtikelnummer = 0;

		try {
			// SP2376
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_GENERIERE_ARTIKELNUMMER_ZIFFERNBLOCK);

			bLetzterZiffernblock = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
			iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		boolean bHerstellerkopplung = getMandantFac()
				.hatZusatzfunktionberechtigung(
						com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG,
						theClientDto);

		beginnArtikelnummer = beginnArtikelnummer.trim();
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT substring(a.c_nr,0,"
				+ (iLaengeArtikelnummer + 1)
				+ ") FROM FLRArtikel a WHERE a.artikelart_c_nr<>'"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "' AND a.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND substring(a.c_nr,0,"
				+ (iLaengeArtikelnummer + 1) + ") LIKE '" + beginnArtikelnummer
				+ "%' ORDER BY substring(a.c_nr,0,"
				+ (iLaengeArtikelnummer + 1) + ") DESC";

		org.hibernate.Query queryS = session.createQuery(sQuery);

		queryS.setMaxResults(1);
		List<?> results = queryS.list();
		Iterator<?> resultListIterator = results.iterator();

		if (results.size() > 0) {

			String letzteArtikelnummer = (String) resultListIterator.next();

			if (bHerstellerkopplung) {
				// SP2660 wg. Hersteller
				letzteArtikelnummer = letzteArtikelnummer.trim();
			}

			int iStartZahl = -1;
			int iEndeZahl = -1;
			boolean bEndeFound = false;

			if (bLetzterZiffernblock == false) {
				int i = 0;
				while (i < letzteArtikelnummer.length()) {

					char c = letzteArtikelnummer.charAt(i);
					// wenn 0-9
					if (c > 47 && c < 58) {
						iStartZahl = i;
						iEndeZahl = iStartZahl;
						for (int j = i; j < letzteArtikelnummer.length(); j++) {
							char d = letzteArtikelnummer.charAt(j);
							if (d > 47 && d < 58) {
								iEndeZahl = j;
								if (j == letzteArtikelnummer.length() - 1) {
									bEndeFound = true;
								}
							} else {
								bEndeFound = true;
								break;
							}
						}
					}
					i++;
					if (bEndeFound) {
						break;
					}
				}
			} else {

				int i = letzteArtikelnummer.length() - 1;
				while (i >= 0) {

					char c = letzteArtikelnummer.charAt(i);
					// wenn 0-9
					if (c > 47 && c < 58) {
						iEndeZahl = i;
						iStartZahl = iEndeZahl;

						for (int j = i; j >= 0; j--) {
							char d = letzteArtikelnummer.charAt(j);
							if (d > 47 && d < 58) {
								iStartZahl = j;
								if (j == 0) {
									bEndeFound = true;
								}
							} else {
								bEndeFound = true;
								break;
							}
						}
					}
					i--;
					if (bEndeFound) {
						break;
					}
				}
			}

			if (iStartZahl >= 0 && iEndeZahl >= 0) {
				String zahlenteil = letzteArtikelnummer.substring(iStartZahl,
						iEndeZahl + 1);

				long zahl = new Long(zahlenteil);

				while (1 == 1) {

					zahl++;

					String zahlenteilNeu = new String(zahl + "");

					if (zahlenteilNeu.length() > zahlenteil.length()) {
						// PJ 14917
						String s = "";
						for (int k = 0; k < zahlenteilNeu.length(); k++) {
							s += "?";
						}

						return letzteArtikelnummer.substring(0, iStartZahl) + s
								+ letzteArtikelnummer.substring(iEndeZahl + 1);
					}

					int iNeueLaenge = zahlenteilNeu.length();
					if (iNeueLaenge < zahlenteil.length()) {
						iNeueLaenge = zahlenteil.length();
					}
					zahlenteilNeu = Helper.fitString2LengthAlignRight(
							zahl + "", iNeueLaenge, '0');
					// Neue Artikelnummer zusammenbauen

					String neueArtNr = letzteArtikelnummer.substring(0,
							iStartZahl)
							+ zahlenteilNeu
							+ letzteArtikelnummer.substring(iEndeZahl + 1);

					//
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();

					String sQuery2 = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "' AND substring(a.c_nr,0,"
							+ (iLaengeArtikelnummer + 1)
							+ ") = '"
							+ Helper.fitString2Length(neueArtNr,
									iLaengeArtikelnummer, ' ') + "'";

					org.hibernate.Query query2 = session2.createQuery(sQuery2);
					query2.setMaxResults(1);
					List<?> results2 = query2.list();
					if (results2.size() > 0) {

						continue;

					}
					session2.close();

					Query query = em
							.createNamedQuery("ArtikelfindByCNrMandantCNr");
					query.setParameter(1, neueArtNr);
					query.setParameter(2, theClientDto.getMandant());
					Collection<?> cl = query.getResultList();
					if (cl.size() > 0) {

						continue;

					}

					return neueArtNr;
				}

			}
		}
		session.close();

		return beginnArtikelnummer;
	}

	public FarbcodeDto farbcodeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Farbcode farbcode = em.find(Farbcode.class, iId);
		if (farbcode == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei farbcodeFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleFarbcodeDto(farbcode);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public VorschlagstextDto vorschlagstextFindByPrimaryKey(Integer iId) {
		Vorschlagstext vorschlagstext = em.find(Vorschlagstext.class, iId);

		return assembleVorschlagstextDto(vorschlagstext);

	}

	public HashMap getAllSperrenIcon(TheClientDto theClientDto) {
		HashMap<String, Object> hmStatiMitBild = new HashMap<String, Object>();
		// try {
		Query query = em
				.createNamedQuery("SperrenfindByOBildMandantCNrNotNull");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();

		SperrenDto[] sperrenDtos = assembleSperrenDtos(cl);

		for (int i = 0; i < sperrenDtos.length; i++) {
			hmStatiMitBild.put(sperrenDtos[i].getCBez(),
					sperrenDtos[i].getOBild());
		}

		return hmStatiMitBild;
	}

	public VerleihDto verleihFindByPrimaryKey(Integer iId) {

		Verleih verleih = em.find(Verleih.class, iId);
		if (verleih == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei verleihFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleVerleihDto(verleih);

	}

	private void setFarbcodeFromFarbcodeDto(Farbcode farbcode,
			FarbcodeDto farbcodeDto) {
		farbcode.setCNr(farbcodeDto.getCNr());
		farbcode.setCBez(farbcodeDto.getCBez());
		em.merge(farbcode);
		em.flush();
	}

	private void setReachFromReachDto(Reach bean, ReachDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setVorzugFromVorzugDto(Vorzug bean, VorzugDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setAlergenFromAlergenDto(Alergen bean, AlergenDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setISort(dto.getISort());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setRohsFromRohsDto(Rohs bean, RohsDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setAutomotiveFromAutomotiveDto(Automotive bean,
			AutomotiveDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setMedicalFromMedicalDto(Medical bean, MedicalDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setVorschlagstextFromVorschlagstextDto(
			Vorschlagstext vorschlagstext, VorschlagstextDto vorschlagstextDto) {
		vorschlagstext.setCBez(vorschlagstextDto.getCBez());
		em.merge(vorschlagstext);
		em.flush();
	}

	private void setVerleihFromVerleihDto(Verleih verleih, VerleihDto verleihDto) {
		verleih.setFFaktor(verleihDto.getFFaktor());
		verleih.setITage(verleihDto.getITage());
		em.merge(verleih);
		em.flush();
	}

	private FarbcodeDto assembleFarbcodeDto(Farbcode farbcode) {
		return FarbcodeDtoAssembler.createDto(farbcode);
	}

	private VorschlagstextDto assembleVorschlagstextDto(
			Vorschlagstext vorschlagstext) {
		return VorschlagstextDtoAssembler.createDto(vorschlagstext);
	}

	private VerleihDto assembleVerleihDto(Verleih verleih) {
		return VerleihDtoAssembler.createDto(verleih);
	}

	private FarbcodeDto[] assembleFarbcodeDtos(Collection<?> farbcodes) {
		List<FarbcodeDto> list = new ArrayList<FarbcodeDto>();
		if (farbcodes != null) {
			Iterator<?> iterator = farbcodes.iterator();
			while (iterator.hasNext()) {
				Farbcode farbcode = (Farbcode) iterator.next();
				list.add(assembleFarbcodeDto(farbcode));
			}
		}
		FarbcodeDto[] returnArray = new FarbcodeDto[list.size()];
		return (FarbcodeDto[]) list.toArray(returnArray);
	}

	/**
	 * Bezeichnung eines Artikels in ieiner bestimmten sprache finden.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param localeCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return ArtikelsprDto
	 * @throws EJBExceptionLP
	 */
	public ArtikelsprDto artikelsprFindByArtikelIIdLocaleCNrOhneExc(
			Integer artikelIId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || localeCNr == null"));
		}
		ArtikelsprDto artikelsprDto = null;
		try {
			Query query = em
					.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, localeCNr);
			Artikelspr artikelspr = (Artikelspr) query.getSingleResult();
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		} catch (NoResultException ex) {
			// nicht gefunden
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return artikelsprDto;
	}

	public Integer createSperren(SperrenDto sperrenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		sperrenDto.setMandantCNr(theClientDto.getMandant());
		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em.createNamedQuery("SperrenfindByCBezMandantCNr");
			query.setParameter(1, sperrenDto.getCBez());
			query.setParameter(2, sperrenDto.getMandantCNr());
			Sperren doppelt = (Sperren) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_SPERREN.UK"));
		} catch (NoResultException ex) {

		}

		if (Helper.short2boolean(sperrenDto.getBDurchfertigung()) == true) {
			try {

				Query query = em.createNamedQuery("SperrenfindBDurchfertigung");
				query.setParameter(1, sperrenDto.getMandantCNr());
				Sperren doppelt = (Sperren) query.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_SPERREN.UK"));
			} catch (NoResultException ex) {

			}
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SPERREN);
			sperrenDto.setIId(pk);

			Sperren sperren = new Sperren(sperrenDto.getIId(),
					sperrenDto.getCBez(), sperrenDto.getMandantCNr(),
					sperrenDto.getBGesperrt(),
					sperrenDto.getBGesperrteinkauf(),
					sperrenDto.getBGesperrtverkauf(),
					sperrenDto.getBGesperrtlos(),
					sperrenDto.getBGesperrtstueckliste(),
					sperrenDto.getBDurchfertigung());
			em.persist(sperren);
			em.flush();
			setSperrenFromSperrenDto(sperren, sperrenDto);
			return sperrenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public Integer createShopgruppewebshop(ShopgruppewebshopDto dto,
			TheClientDto theClientDto) {

		try {

			Query query = em
					.createNamedQuery("ShopgruppefindByShopgruppeIIdWebshopIId");
			query.setParameter("shopgruppeIId", dto.getShopgruppeIId());
			query.setParameter("webshopIId", dto.getWebshopIId());
			Shopgruppewebshop doppelt = (Shopgruppewebshop) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_SHOPGRUPPWEBSHOP.UK"));
		} catch (NoResultException ex) {

		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SHOPGRUPPEWEBSHOP);
			dto.setIId(pk);

			Shopgruppewebshop sperren = new Shopgruppewebshop(dto.getIId(),
					dto.getShopgruppeIId(), dto.getWebshopIId());
			em.persist(sperren);
			em.flush();
			setShopgruppewebshopFromShopgruppewebshopDto(sperren, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeSperren(SperrenDto dto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}
		// try {
		Sperren toRemove = em.find(Sperren.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeSperren. Es gibt keine Sperre mit iid "
							+ dto.getIId() + "\ndto.toString(): "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void removeShopgruppewebshop(ShopgruppewebshopDto dto) {

		Shopgruppewebshop toRemove = em.find(Shopgruppewebshop.class,
				dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateSperren(SperrenDto sperrenDto) throws EJBExceptionLP {
		if (sperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("sperrenDto == null"));
		}

		Integer iId = sperrenDto.getIId();
		// try {
		Sperren sperren = em.find(Sperren.class, iId);
		if (sperren == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSperren. Es gibt keine iid " + iId
							+ "\ndto.toString(): " + sperrenDto.toString());
		}

		try {
			Query query = em.createNamedQuery("SperrenfindByCBezMandantCNr");
			query.setParameter(1, sperrenDto.getCBez());
			query.setParameter(2, sperrenDto.getMandantCNr());
			Sperren sperrenVorhanden = (Sperren) query.getSingleResult();
			if (sperrenVorhanden != null
					&& iId.equals(sperrenVorhanden.getIId()) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_SPERREN.UK"));
			}

		} catch (NoResultException ex) {
		}

		if (Helper.short2boolean(sperrenDto.getBDurchfertigung()) == true) {
			try {
				Query query = em.createNamedQuery("SperrenfindBDurchfertigung");
				query.setParameter(1, sperrenDto.getMandantCNr());
				Sperren sperrenVorhanden = (Sperren) query.getSingleResult();
				if (sperrenVorhanden != null
						&& iId.equals(sperrenVorhanden.getIId()) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_SPERREN.UK"));
				}

			} catch (NoResultException ex) {
			}
		}

		setSperrenFromSperrenDto(sperren, sperrenDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public void updateShopgruppewebshop(ShopgruppewebshopDto dto) {
		Integer iId = dto.getIId();
		Shopgruppewebshop sperren = em.find(Shopgruppewebshop.class, iId);
		try {
			Query query = em
					.createNamedQuery("ShopgruppefindByShopgruppeIIdWebshopIId");
			query.setParameter("shopgruppeIId", dto.getShopgruppeIId());
			query.setParameter("webshopIId", dto.getWebshopIId());
			Shopgruppewebshop sperrenVorhanden = (Shopgruppewebshop) query
					.getSingleResult();
			if (sperrenVorhanden != null
					&& iId.equals(sperrenVorhanden.getIId()) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_SHOPGRUPPEWEBSHOP.UK"));
			}

		} catch (NoResultException ex) {
		}
		setShopgruppewebshopFromShopgruppewebshopDto(sperren, dto);
	}

	public SperrenDto sperrenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Sperren sperren = em.find(Sperren.class, iId);
		if (sperren == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei sperrenFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}

		return assembleSperrenDto(sperren);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public ShopgruppewebshopDto shopgruppewebshopFindByPrimaryKey(Integer iId) {
		Shopgruppewebshop sperren = em.find(Shopgruppewebshop.class, iId);
		return assembleShopgruppewebshopDto(sperren);
	}

	public ShopgruppewebshopDto[] shopgruppeFindByWebshopId(Integer webshopIId) {
		Query query = em
				.createNamedQuery(Shopgruppewebshop.QueryFindByWebshopId);
		query.setParameter(1, webshopIId);
		Collection<Shopgruppewebshop> cl = query.getResultList();
		return ShopgruppewebshopDtoAssembler.createDtos(cl);
	}

	public ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr,
			TheClientDto theClientDto) {
		try {
			Query query = em
					.createNamedQuery(Shopgruppe.QueryFindByCNrMandantCNr);
			query.setParameter(1, cnr);
			query.setParameter(2, theClientDto.getMandant());
			Shopgruppe sg = (Shopgruppe) query.getSingleResult();

			return shopgruppeFindByPrimaryKey(sg.getIId(), theClientDto);
			// return ShopgruppeDtoAssembler.createDto(sg) ;
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
		}

		return null;
	}

	public boolean isShopgruppeInWebshop(ShopgruppeDto shopgruppeDto,
			Integer webshopIId) {
		if (null == shopgruppeDto || webshopIId == null)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));

		if (null == shopgruppeDto.getShopgruppeIId()) {
			Query query = em
					.createNamedQuery(Shopgruppewebshop.QueryFindByShopgruppeIIdWebshopIId);
			query.setParameter(1, shopgruppeDto.getIId());
			query.setParameter(2, webshopIId);
			Shopgruppewebshop sgwebshop = (Shopgruppewebshop) query
					.getSingleResult();
		} else {

		}
		return false;
	}

	public SperrenDto sperrenFindBDurchfertigung(TheClientDto theClientDto) {
		SperrenDto sperrenDto = null;

		try {
			Query query = em.createNamedQuery("SperrenfindBDurchfertigung");
			query.setParameter(1, theClientDto.getMandant());
			Sperren sperren = (Sperren) query.getSingleResult();
			sperrenDto = assembleSperrenDto(sperren);
		} catch (NoResultException ex) {
			//
		}

		return sperrenDto;

	}

	private void setSperrenFromSperrenDto(Sperren sperren, SperrenDto sperrenDto) {
		sperren.setCBez(sperrenDto.getCBez());
		sperren.setMandantCNr(sperrenDto.getMandantCNr());
		sperren.setBGesperrt(sperrenDto.getBGesperrt());
		sperren.setBGesperrteinkauf(sperrenDto.getBGesperrteinkauf());
		sperren.setBGesperrtverkauf(sperrenDto.getBGesperrtverkauf());
		sperren.setBGesperrtlos(sperrenDto.getBGesperrtlos());
		sperren.setBGesperrtstueckliste(sperrenDto.getBGesperrtstueckliste());
		sperren.setBDurchfertigung(sperrenDto.getBDurchfertigung());
		sperren.setOBild(sperrenDto.getOBild());
		em.merge(sperren);
		em.flush();
	}

	private void setShopgruppewebshopFromShopgruppewebshopDto(
			Shopgruppewebshop shopgruppewebshop,
			ShopgruppewebshopDto shopgruppewebshopDto) {
		shopgruppewebshop.setShopgruppeIId(shopgruppewebshopDto
				.getShopgruppeIId());
		shopgruppewebshop.setWebshopIId(shopgruppewebshopDto.getWebshopIId());
		em.merge(shopgruppewebshop);
		em.flush();
	}

	private SperrenDto assembleSperrenDto(Sperren sperren) {
		return SperrenDtoAssembler.createDto(sperren);
	}

	private ShopgruppewebshopDto assembleShopgruppewebshopDto(
			Shopgruppewebshop shopgruppe) {
		return ShopgruppewebshopDtoAssembler.createDto(shopgruppe);
	}

	private ShopgruppewebshopDto[] assembleShopgruppewebshopDto(
			Collection<Shopgruppewebshop> shopgruppen) {
		return ShopgruppewebshopDtoAssembler.createDtos(shopgruppen);
	}

	private SperrenDto[] assembleSperrenDtos(Collection<?> sperrens) {
		List<SperrenDto> list = new ArrayList<SperrenDto>();
		if (sperrens != null) {
			Iterator<?> iterator = sperrens.iterator();
			while (iterator.hasNext()) {
				Sperren sperren = (Sperren) iterator.next();
				list.add(assembleSperrenDto(sperren));
			}
		}
		SperrenDto[] returnArray = new SperrenDto[list.size()];
		return (SperrenDto[]) list.toArray(returnArray);
	}

	public Integer createArtikelsperren(ArtikelsperrenDto artikelsperrenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelsperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelsperrenDto == null"));
		}
		if (artikelsperrenDto.getArtikelIId() == null
				|| artikelsperrenDto.getSperrenIId() == null
				|| artikelsperrenDto.getCGrund() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelsperrenDto.getArtikelIId() == null || artikelsperrenDto.getSperrenIId() == null || artikelsperrenDto.getCGrund() == null"));
		}

		artikelsperrenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikelsperrenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em
					.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
			query.setParameter(1, artikelsperrenDto.getArtikelIId());
			query.setParameter(2, artikelsperrenDto.getSperrenIId());
			// @todo getSingleResult oder getResultList ?
			Artikelsperren doppelt = (Artikelsperren) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELSPERREN.UK"));
		} catch (NoResultException ex) {

		}

		Query queryNext = em
				.createNamedQuery("ArtikelsperrenejbSelectNextReihung");
		queryNext.setParameter(1, artikelsperrenDto.getArtikelIId());

		Integer i = (Integer) queryNext.getSingleResult();

		if (i == null) {
			i = new Integer(0);
		}
		i = new Integer(i.intValue() + 1);
		artikelsperrenDto.setiSort(i);

		try {

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELSPERREN);
			artikelsperrenDto.setIId(pk);

			Artikelsperren artikelsperren = new Artikelsperren(
					artikelsperrenDto.getIId(),
					artikelsperrenDto.getArtikelIId(),
					artikelsperrenDto.getSperrenIId(),
					artikelsperrenDto.getPersonalIIdAendern(),
					artikelsperrenDto.getTAendern(),
					artikelsperrenDto.getCGrund(), artikelsperrenDto.getiSort());
			em.persist(artikelsperren);
			em.flush();
			setArtikelsperrenFromArtikelsperrenDto(artikelsperren,
					artikelsperrenDto);

			Sperren spAktuell = em.find(Sperren.class,
					artikelsperrenDto.getSperrenIId());

			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE, null,
					spAktuell.getCBez(), theClientDto);
			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND, null,
					artikelsperrenDto.getCGrund(), theClientDto);

			return artikelsperrenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeArtikelsperren(ArtikelsperrenDto dto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Sperren spAktuell = em.find(Sperren.class, dto.getSperrenIId());

		artikelAenderungLoggen(dto.getArtikelIId(),
				ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE,
				spAktuell.getCBez(), null, theClientDto);
		artikelAenderungLoggen(dto.getArtikelIId(),
				ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND, dto.getCGrund(),
				null, theClientDto);

		Artikelsperren toRemove = em.find(Artikelsperren.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelsperren. Es gitb keine iid "
							+ dto.getIId() + "\ndto.toString():"
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		// Es muss immer einen Kunden mit der I_SORT=1 geben, daher nach dem
		// loeschen unbedingt neu re.indizieren

		Query query = em
				.createNamedQuery("ArtikelsperrenfindByArtikelIIdOrderByISort");
		query.setParameter(1, dto.getArtikelIId());

		List l = query.getResultList();
		Iterator it = l.iterator();
		int iSort = 1;
		while (it.hasNext()) {
			Artikelsperren al = (Artikelsperren) it.next();
			al.setiSort(iSort);
			iSort++;

		}

	}

	public void updateArtikelsperren(ArtikelsperrenDto artikelsperrenDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelsperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelsperrenDto == null"));
		}
		if (artikelsperrenDto.getIId() == null
				|| artikelsperrenDto.getArtikelIId() == null
				|| artikelsperrenDto.getSperrenIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelsperrenDto.getIId() == null || artikelsperrenDto.getArtikelIId() == null || artikelsperrenDto.getSperrenIId() == null"));
		}

		Integer iId = artikelsperrenDto.getIId();
		// try {
		Artikelsperren artikelsperren = em.find(Artikelsperren.class, iId);
		if (artikelsperren == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelsperren. Es gibt keine iid " + iId
							+ "\ndto.toString: " + artikelsperrenDto.toString());
		}

		if (!artikelsperren.getSperrenIId().equals(
				artikelsperrenDto.getSperrenIId())) {

			Sperren spVorher = em.find(Sperren.class,
					artikelsperren.getSperrenIId());
			Sperren spAktuell = em.find(Sperren.class,
					artikelsperrenDto.getSperrenIId());

			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE,
					spVorher.getCBez(), spAktuell.getCBez(), theClientDto);
		}

		String grundVorher = "";
		if (artikelsperren.getCGrund() != null) {
			grundVorher = artikelsperren.getCGrund();
		}

		String grundAktuell = "";
		if (artikelsperrenDto.getCGrund() != null) {
			grundAktuell = artikelsperrenDto.getCGrund();
		}

		if (!grundVorher.equals(grundAktuell)) {
			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(),
					ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND, grundVorher,
					grundAktuell, theClientDto);
		}

		artikelsperrenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikelsperrenDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		try {
			Query query = em
					.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
			query.setParameter(1, artikelsperrenDto.getArtikelIId());
			query.setParameter(2, artikelsperrenDto.getSperrenIId());
			Integer iIdVorhanden = ((Artikelsperren) query.getSingleResult())
					.getIId();

			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_ARTIKELSPERREN.UK"));
			}

		} catch (NoResultException ex) {
			// nix da
		}

		setArtikelsperrenFromArtikelsperrenDto(artikelsperren,
				artikelsperrenDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ArtikelsperrenDto artikelsperrenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Artikelsperren artikelsperren = em.find(Artikelsperren.class, iId);
		if (artikelsperren == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelsperrenFindByPrimaryKey. Es gibt keine iid"
							+ iId);
		}

		return assembleArtikelsperrenDto(artikelsperren);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public ArtikelsperrenDto[] artikelsperrenFindByArtikelIId(Integer artikelId)
			throws EJBExceptionLP {

		if (artikelId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIId");
		query.setParameter(1, artikelId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		//
		// }
		return assembleArtikelsperrenDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public AlergenDto[] allergenFindByMandantCNr(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("AlergenfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		return AlergenDtoAssembler.createDtos(cl);
	}

	public ArtikelalergenDto[] artikelallergenFindByArtikelIId(
			Integer artikelIId) {
		Query query = em.createNamedQuery("ArtikelalergenfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return ArtikelalergenDtoAssembler.createDtos(cl);
	}

	public ArtikelsperrenDto artikelsperrenFindByArtikelIIdSperrenIIdOhneExc(
			Integer artikelId, Integer sperrenlId) {

		// try {
		Query query = em
				.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
		query.setParameter(1, artikelId);
		query.setParameter(2, sperrenlId);
		Artikelsperren s;
		try {
			s = (Artikelsperren) query.getSingleResult();
			return assembleArtikelsperrenDto(s);

		} catch (NoResultException e) {
			return null;
		}

	}

	private void setArtikelsperrenFromArtikelsperrenDto(
			Artikelsperren artikelsperren, ArtikelsperrenDto artikelsperrenDto) {
		artikelsperren.setArtikelIId(artikelsperrenDto.getArtikelIId());
		artikelsperren.setSperrenIId(artikelsperrenDto.getSperrenIId());

		artikelsperren.setCGrund(artikelsperrenDto.getCGrund());
		artikelsperren.setPersonalIIdAendern(artikelsperrenDto
				.getPersonalIIdAendern());
		artikelsperren.setTAendern(artikelsperrenDto.getTAendern());
		artikelsperren.setiSort(artikelsperrenDto.getiSort());

		em.merge(artikelsperren);
		em.flush();
	}

	private ArtikelsperrenDto assembleArtikelsperrenDto(
			Artikelsperren artikelsperren) {
		return ArtikelsperrenDtoAssembler.createDto(artikelsperren);
	}

	private ArtikelsperrenDto[] assembleArtikelsperrenDtos(
			Collection<?> artikelsperrens) {
		List<ArtikelsperrenDto> list = new ArrayList<ArtikelsperrenDto>();
		if (artikelsperrens != null) {
			Iterator<?> iterator = artikelsperrens.iterator();
			while (iterator.hasNext()) {
				Artikelsperren artikelsperren = (Artikelsperren) iterator
						.next();
				list.add(assembleArtikelsperrenDto(artikelsperren));
			}
		}
		ArtikelsperrenDto[] returnArray = new ArtikelsperrenDto[list.size()];
		return (ArtikelsperrenDto[]) list.toArray(returnArray);
	}

	public void updateTrumphtopslog(String artikelnummer,
			String kurzbezeichnungMaterial, String importfileName,
			BigDecimal gewicht, long iBearbeitsungszeit,
			BigDecimal laserkostenProStunde, Integer lagerIId,
			String mandantCNr, boolean kalkulationsart1,
			int mehrverbrauchfuerlaserinmm, double breiteArtikel,
			double laengeArtikel, Double hoeheArtikel, TheClientDto theClientDto)
			throws EJBExceptionLP {

		TrumphtopslogDto ttlogDto = new TrumphtopslogDto();
		try {

			ttlogDto.setNGewicht(Helper.rundeKaufmaennisch(gewicht, 4));
			ttlogDto.setNGestpreisneu(new BigDecimal(0));
			ttlogDto.setCImportfilename(importfileName);
			ttlogDto.setIBearbeitungszeit((int) iBearbeitsungszeit);

			Query queryArt = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
			queryArt.setParameter(1, artikelnummer);
			queryArt.setParameter(2, mandantCNr);
			Artikel artikel = (Artikel) queryArt.getSingleResult();
			ttlogDto.setArtikelIId(artikel.getIId());

			Session session = FLRSessionFactory.getFactory().openSession();

			String query = "FROM FLRArtikellistespr aspr WHERE aspr.c_kbez ='"
					+ kurzbezeichnungMaterial + "'";
			;
			org.hibernate.Query kurzbez = session.createQuery(query);

			List subResults = kurzbez.list();

			if (subResults.size() > 0) {
				session.close();

				FLRArtikellistespr flrSpr = (FLRArtikellistespr) subResults
						.iterator().next();

				Integer artikelIIdMaterial = flrSpr.getId().getArtikelliste()
						.getI_id();
				ttlogDto.setArtikelIIdMaterial(artikelIIdMaterial);
				if (subResults.size() > 1) {
					ttlogDto.setCError("Es wurden mehrer Artikel mit der Kurzbezeichnung '"
							+ kurzbezeichnungMaterial + "' gefunden.");
				}
				// Laserkosten=
				BigDecimal laserkosten = new BigDecimal(iBearbeitsungszeit
						/ (double) 3600000).multiply(laserkostenProStunde);

				// PreisMNeu berechnen
				BigDecimal preisNeu = new BigDecimal(0);
				try {
					ArtikellagerDto artikellagerDto = getLagerFac()
							.artikellagerFindByPrimaryKey(artikelIIdMaterial,
									lagerIId);

					Artikel artikelMaterial = em.find(Artikel.class,
							artikelIIdMaterial);

					// Laserzeit in Minutenfaktor eintragen
					artikel.setFMinutenfaktor1((double) iBearbeitsungszeit / 60000);

					// Anzahl der moeglichen Teile berechnen
					int iAnzahlDerMoeglichenTeile = 0;

					int iAnzahl_1 = 0;
					int iAnzahl_2 = 0;
					double materialLaenge = 0;
					double materialBreite = 0;

					Geometrie geometrieMaterial = em.find(Geometrie.class,
							artikelIIdMaterial);

					if (geometrieMaterial != null) {
						if (geometrieMaterial.getFBreite() != null) {
							materialBreite = geometrieMaterial.getFBreite();

						}
						if (geometrieMaterial.getFTiefe() != null) {
							materialLaenge = geometrieMaterial.getFTiefe();
						}
					}

					// Geometrie des Artikels zurueckschreiben
					Geometrie geometrieArtikel = em.find(Geometrie.class,
							artikel.getIId());
					if (geometrieArtikel == null) {
						geometrieArtikel = new Geometrie(artikel.getIId());
					}

					geometrieArtikel.setFBreite(laengeArtikel);
					geometrieArtikel.setFTiefe(breiteArtikel);
					if (hoeheArtikel != null) {
						geometrieArtikel.setFHoehe(hoeheArtikel);
					}

					em.merge(geometrieArtikel);
					em.flush();

					// Mehrverbrauch fuer Laser
					laengeArtikel = laengeArtikel + 2
							* mehrverbrauchfuerlaserinmm;
					breiteArtikel = breiteArtikel + 2
							* mehrverbrauchfuerlaserinmm;

					iAnzahl_1 = (int) (materialLaenge / laengeArtikel)
							* (int) (materialBreite / breiteArtikel);
					iAnzahl_2 = (int) (materialLaenge / breiteArtikel)
							* (int) (materialBreite / laengeArtikel);

					if (iAnzahl_1 > iAnzahl_2) {
						iAnzahlDerMoeglichenTeile = iAnzahl_1;
					} else {
						iAnzahlDerMoeglichenTeile = iAnzahl_2;
					}

					// PJ 15468
					// Wenn das Teil zu grosz fuer die Blechtafel ist, dann wird
					// die gesamte Blechtafel berechnet
					// d.h. Es wird gerechnet, wie wenn 1 Stueck gefertigt wird
					if (iAnzahlDerMoeglichenTeile == 0) {
						iAnzahlDerMoeglichenTeile = 1;
					}

					// Gewicht eintragen (Gewicht des Ausgangsmateiral / Anzahl
					// der Teile

					if (artikelMaterial.getFGewichtkg() != null
							&& iAnzahlDerMoeglichenTeile != 0) {
						artikel.setFGewichtkg(artikelMaterial.getFGewichtkg()
								/ iAnzahlDerMoeglichenTeile);
					}

					if (kalkulationsart1 == true) {

						BigDecimal preisProKilo = artikellagerDto
								.getNGestehungspreis();
						if (artikelMaterial.getNUmrechnungsfaktor() != null
								&& artikelMaterial.getNUmrechnungsfaktor()
										.doubleValue() != 0) {
							preisProKilo = artikellagerDto
									.getNGestehungspreis()
									.divide(artikelMaterial
											.getNUmrechnungsfaktor(),
											4, BigDecimal.ROUND_HALF_EVEN);
						}

						// Materialkosten
						BigDecimal materialkosten = preisProKilo
								.multiply(gewicht);

						preisNeu = Helper.rundeKaufmaennisch(
								materialkosten.add(laserkosten), 4);
					} else {
						if (iAnzahlDerMoeglichenTeile == 0) {
							return;
						}

						preisNeu = Helper.rundeKaufmaennisch(
								artikellagerDto
										.getNGestehungspreis()
										.divide(new BigDecimal(
												iAnzahlDerMoeglichenTeile), 4,
												BigDecimal.ROUND_HALF_EVEN)
										.add(laserkosten), 4);
					}

					ttlogDto.setNGestpreisneu(preisNeu);

					// Gestehungspreis in Artikellager updaten
					ArtikellagerDto alDto = new ArtikellagerDto();
					alDto.setArtikelIId(artikel.getIId());
					alDto.setLagerIId(lagerIId);
					alDto.setMandantCNr(mandantCNr);
					alDto.setNGestehungspreis(preisNeu);
					getLagerFac().updateGestpreisArtikellager(alDto,
							theClientDto);

				} catch (RemoteException ex1) {
					// Wenn finderEx, dann kibts keinen preis
					ttlogDto.setCError("Kein Material-Gestehungspreis gefunden.");
				}

				session = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Criteria crit = session
						.createCriteria(FLRTrumphtopslog.class);
				crit.add(Restrictions.eq(
						ArtikelFac.FLR_TRUMPHTOPSLOS_ARTIKEL_I_ID,
						artikel.getIId()));
				crit.addOrder(Order
						.desc(ArtikelFac.FLR_TRUMPHTOPSLOS_T_ANLEGEN));

				crit.setMaxResults(1);
				List results = crit.list();

				if (results.size() > 0) {

					FLRTrumphtopslog log = (FLRTrumphtopslog) results
							.iterator().next();
					Trumphtopslog ttlogVorhanden = em.find(Trumphtopslog.class,
							log.getI_id());

					if (ttlogVorhanden.getNGestpreisneu().doubleValue() == ttlogDto
							.getNGestpreisneu().doubleValue()) {
						// Wenn der Preis gleich ist, dann auslassen
					} else {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen
								.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
						ttlogDto.setIId(pk);

						erzeugeTrumphTopsLogeintrag(ttlogDto);

					}

					// Wenn der Preis geaendert ist, dann neuer Eintrag

				} else {
					// Neuer Eintrag
					PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
					Integer pk = pkGen
							.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
					ttlogDto.setIId(pk);

					erzeugeTrumphTopsLogeintrag(ttlogDto);
				}

				session.close();

				// Gestehungspreis in der Losbuchung updaten

				// Alle Lose die IN_PRODUKTION sind (d.h. noch keine
				// Ablieferungen haben) und den Artikel beinhalten

				session = FLRSessionFactory.getFactory().openSession();

				String sQueryIstmaterial = "SELECT istmaterial, (SELECT sum(abl.n_menge) FROM FLRLosablieferung AS abl WHERE abl.flrlos.i_id=istmaterial.flrlossollmaterial.flrlos.i_id) "
						+ " FROM FLRLosistmaterial AS istmaterial WHERE istmaterial.flrlossollmaterial.flrlos.status_c_nr='"
						+ LocaleFac.STATUS_IN_PRODUKTION
						+ "' AND istmaterial.flrlossollmaterial.flrartikel.i_id="
						+ artikel.getIId() + " AND istmaterial.b_abgang=1";

				org.hibernate.Query istmaterial = session
						.createQuery(sQueryIstmaterial);

				results = istmaterial.list();
				Iterator<?> resultListIterator = results.iterator();
				while (resultListIterator.hasNext()) {

					Object[] o = (Object[]) resultListIterator.next();

					BigDecimal summeAblieferungen = (BigDecimal) o[1];

					if (summeAblieferungen == null
							|| summeAblieferungen.doubleValue() == 0) {

						FLRLosistmaterial flrLosistmaterial = (FLRLosistmaterial) o[0];

						try {
							BigDecimal bdPreisAlt = getLagerFac()
									.getGestehungspreisEinerAbgangsposition(
											LocaleFac.BELEGART_LOS,
											flrLosistmaterial.getI_id(), null);

							if (preisNeu.doubleValue() != bdPreisAlt
									.doubleValue()) {
								// Preis in Lager updaten

								LagerbewegungDto lagerbewegungDto = getLagerFac()
										.getLetzteintrag(
												LocaleFac.BELEGART_LOS,
												flrLosistmaterial.getI_id(),
												null);

								if (lagerbewegungDto != null) {

									// Hole Urspruenge dazu
									LagerabgangursprungDto[] urspruenge = getLagerFac()
											.lagerabgangursprungFindByLagerbewegungIIdBuchung(
													lagerbewegungDto
															.getIIdBuchung());

									for (int i = 0; i < urspruenge.length; i++) {

										LagerbewegungDto[] lagerbewegungDtos = getLagerFac()
												.lagerbewegungFindByIIdBuchung(
														urspruenge[i]
																.getILagerbewegungidursprung());

										// Preis in Lager updaten
										getLagerFac()
												.bucheZu(
														lagerbewegungDtos[0]
																.getCBelegartnr(),
														lagerbewegungDtos[0]
																.getIBelegartid(),
														lagerbewegungDtos[0]
																.getIBelegartpositionid(),
														lagerbewegungDtos[0]
																.getArtikelIId(),
														lagerbewegungDtos[0]
																.getNMenge(),
														preisNeu,
														lagerbewegungDtos[0]
																.getLagerIId(),
														null,
														lagerbewegungDtos[0]
																.getTBelegdatum(),
														theClientDto, null,
														null, true);

									}

								}
							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}

				}

			} else {
				// Kein Material gefunden
				session.close();
				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
				ttlogDto.setIId(pk);
				ttlogDto.setCError("Kein Materialartikel mit der Kurzbezeichnung '"
						+ kurzbezeichnungMaterial + "' gefunden");
				ttlogDto.setNGestpreisneu(new BigDecimal(0));
				erzeugeTrumphTopsLogeintrag(ttlogDto);

			}
		} catch (NoResultException ex) {
			// Fehler
			ttlogDto.setCError("Kein Artikel mit der Nummer '" + artikelnummer
					+ "' gefunden");
			ttlogDto.setNGestpreisneu(new BigDecimal(0));

			try {
				erzeugeTrumphTopsLogeintrag(ttlogDto);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int u = 0;

		}

	}

	private void setTrumphtopslogFromTrumphtopslogDto(
			Trumphtopslog trumphtopslog, TrumphtopslogDto trumphtopslogDto) {
		trumphtopslog.setCImportfilename(trumphtopslogDto.getCImportfilename());
		trumphtopslog.setCError(trumphtopslogDto.getCError());
		trumphtopslog.setArtikelIId(trumphtopslogDto.getArtikelIId());
		trumphtopslog.setArtikelIIdMaterial(trumphtopslogDto
				.getArtikelIIdMaterial());
		trumphtopslog.setNGewicht(trumphtopslogDto.getNGewicht());
		trumphtopslog.setNGestpreisneu(trumphtopslogDto.getNGestpreisneu());
		trumphtopslog.setIBearbeitungszeit(trumphtopslogDto
				.getIBearbeitungszeit());
		trumphtopslog.setTAnlegen(trumphtopslogDto.getTAnlegen());
		em.merge(trumphtopslog);
		em.flush();
	}

	private TrumphtopslogDto assembleTrumphtopslogDto(
			Trumphtopslog trumphtopslog) {
		return TrumphtopslogDtoAssembler.createDto(trumphtopslog);
	}

	private TrumphtopslogDto[] assembleTrumphtopslogDtos(
			Collection<?> trumphtopslogs) {
		List<TrumphtopslogDto> list = new ArrayList<TrumphtopslogDto>();
		if (trumphtopslogs != null) {
			Iterator<?> iterator = trumphtopslogs.iterator();
			while (iterator.hasNext()) {
				Trumphtopslog trumphtopslog = (Trumphtopslog) iterator.next();
				list.add(assembleTrumphtopslogDto(trumphtopslog));
			}
		}
		TrumphtopslogDto[] returnArray = new TrumphtopslogDto[list.size()];
		return (TrumphtopslogDto[]) list.toArray(returnArray);
	}

	public Integer createZugehoerige(ZugehoerigeDto zugehoerigeDto)
			throws EJBExceptionLP {
		if (zugehoerigeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zugehoerigeDto == null"));
		}
		if (zugehoerigeDto.getArtikelIId() == null
				|| zugehoerigeDto.getArtikelIIdZugehoerig() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zugehoerigeDto.getArtikelIId() == null || zugehoerigeDto.getArtikelIIdZugehoerig() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ZugehoerigefindByArtikelIIdArtikelIIdZugehoerig");
			query.setParameter(1, zugehoerigeDto.getArtikelIId());
			query.setParameter(2, zugehoerigeDto.getArtikelIIdZugehoerig());
			// @todo getSingleResult oder getResultList ?
			Zugehoerige doppelt = (Zugehoerige) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ZUGEHOERIGE.UK"));
		} catch (NoResultException ex) {

		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUGEHOERIGE);
			zugehoerigeDto.setIId(pk);

			Zugehoerige zugehoerige = new Zugehoerige(zugehoerigeDto.getIId(),
					zugehoerigeDto.getArtikelIId(),
					zugehoerigeDto.getArtikelIIdZugehoerig());
			em.persist(zugehoerige);
			em.flush();
			setZugehoerigeFromZugehoerigeDto(zugehoerige, zugehoerigeDto);
			return zugehoerigeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeZugehoerige(ZugehoerigeDto dto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		// try {
		Zugehoerige toRemove = em.find(Zugehoerige.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeZugehoerige. Es gibt keine iid "
							+ dto.getIId() + "\ndto.toString: "
							+ dto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateZugehoerige(ZugehoerigeDto zugehoerigeDto)
			throws EJBExceptionLP {
		if (zugehoerigeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("zugehoerigeDto == null"));
		}
		if (zugehoerigeDto.getIId() == null
				|| zugehoerigeDto.getArtikelIId() == null
				|| zugehoerigeDto.getArtikelIIdZugehoerig() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"zugehoerigeDto.getIId() == null || zugehoerigeDto.getArtikelIId() == null || zugehoerigeDto.getArtikelIIdZugehoerig() == null"));
		}

		Integer iId = zugehoerigeDto.getIId();

		// try {
		Query query = em
				.createNamedQuery("ZugehoerigefindByArtikelIIdArtikelIIdZugehoerig");
		query.setParameter(1, zugehoerigeDto.getArtikelIId());
		query.setParameter(2, zugehoerigeDto.getArtikelIIdZugehoerig());
		// @todo getSingleResult oder getResultList ?
		Integer iIdVorhanden = ((Zugehoerige) query.getSingleResult()).getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ZUGEHOERIGE.UK"));
		}

		// }
		// catch (FinderException ex) {
		//
		// }

		// try {
		Zugehoerige zugehoerige = em.find(Zugehoerige.class, iId);
		if (zugehoerige == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateZugehoerige. Es gibt keine iid " + iId
							+ "\ndto.toString(): " + zugehoerigeDto.toString());
		}
		setZugehoerigeFromZugehoerigeDto(zugehoerige, zugehoerigeDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ZugehoerigeDto zugehoerigeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Zugehoerige zugehoerige = em.find(Zugehoerige.class, iId);
		if (zugehoerige == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei zugehoerigeFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}

		return assembleZugehoerigeDto(zugehoerige);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
	}

	public Integer[] getZugehoerigeArtikel(Integer artikelIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ZugehoerigefindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		ZugehoerigeDto[] dtos = assembleZugehoerigeDtos(cl);
		Integer[] i = new Integer[dtos.length];
		for (int j = 0; j < dtos.length; j++) {
			i[j] = dtos[j].getArtikelIIdZugehoerig();
		}
		return i;
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
	}

	private void setZugehoerigeFromZugehoerigeDto(Zugehoerige zugehoerige,
			ZugehoerigeDto zugehoerigeDto) {
		zugehoerige.setArtikelIId(zugehoerigeDto.getArtikelIId());
		zugehoerige.setArtikelIIdZugehoerig(zugehoerigeDto
				.getArtikelIIdZugehoerig());
		em.merge(zugehoerige);
		em.flush();
	}

	private ZugehoerigeDto assembleZugehoerigeDto(Zugehoerige zugehoerige) {
		return ZugehoerigeDtoAssembler.createDto(zugehoerige);
	}

	private ZugehoerigeDto[] assembleZugehoerigeDtos(Collection<?> zugehoeriges) {
		List<ZugehoerigeDto> list = new ArrayList<ZugehoerigeDto>();
		if (zugehoeriges != null) {
			Iterator<?> iterator = zugehoeriges.iterator();
			while (iterator.hasNext()) {
				Zugehoerige zugehoerige = (Zugehoerige) iterator.next();
				list.add(assembleZugehoerigeDto(zugehoerige));
			}
		}
		ZugehoerigeDto[] returnArray = new ZugehoerigeDto[list.size()];
		return (ZugehoerigeDto[]) list.toArray(returnArray);
	}

	@WebMethod
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void printArtikeletikettOnServer(
			@WebParam(name = "sArtikelnr") String sArtikelnr,
			@WebParam(name = "sPrinter") String sPrinter,
			@WebParam(name = "idUser") String idUser) {
		try {
			TheClientDto theClientDto = getTheClient(idUser);
			ArtikelDto artikelDto = artikelFindByCNr(sArtikelnr, theClientDto);
			JasperPrintLP print = getArtikelReportFac().printArtikeletikett(
					artikelDto.getIId(), "", new BigDecimal(1), 1, null,
					theClientDto);
			PrintService[] printService = PrintServiceLookup
					.lookupPrintServices(null, null);
			int i = 0;
			String usedPrinter = sPrinter;
			if (usedPrinter.equals("")) {
				// do nothing there is no Printer
			} else {
				while (i < printService.length) {
					if (!printService[i].getName().equals(usedPrinter)) {
						i++;
					} else {
						ServerDruckerFacBean.print(print, printService[i]);
						break;
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@WebMethod
	@WebResult(name = "sLagerorte")
	public String[] getAlleLagerorte(@WebParam(name = "idUser") String idUser) {
		TheClientDto theClientDto = getTheClient(idUser);
		String[] toReturn = new String[] {};
		try {
			LagerplatzDto[] lagerplatzDto = getLagerFac().getAlleLagerplaetze();
			toReturn = new String[lagerplatzDto.length];
			for (int i = 0; i < lagerplatzDto.length; i++) {
				toReturn[i] = lagerplatzDto[i].getCLagerplatz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;

	}

	@WebMethod
	public void setArtikelLagerort(
			@WebParam(name = "sArtikelnr") String sArtikelnr,
			@WebParam(name = "sLagerort") String sLagerort,
			@WebParam(name = "idUser") String idUser) {
		TheClientDto theClientDto = getTheClient(idUser);
		ArtikelDto artikelDto = artikelFindByCNr(sArtikelnr, theClientDto);
	}

	public void vertauscheShopgruppen(Integer pos1, Integer pos2)
			throws EJBExceptionLP {
		ShopgruppeISort helper = new ShopgruppeISort(em);
		helper.tausche(pos1, pos2);
	}

	@Override
	public void updateArtikelAusImportResult(IStklImportResult result,
			TheClientDto theClientDto) throws RemoteException {
		if (result == null || result.getSelectedArtikelDto() == null)
			return;

		ArtikelDto artikelDtoDB = artikelFindByPrimaryKeySmall(result
				.getSelectedArtikelDto().getIId(), theClientDto);

		boolean artikelNeedsUpdate = false;
		String herstellerArtNr = result.getValues().get(
				StklImportSpezifikation.HERSTELLERARTIKELNUMMER);
		if (updateNeeded(herstellerArtNr,
				artikelDtoDB.getCArtikelnrhersteller())) {
			artikelNeedsUpdate = true;
			artikelDtoDB.setCArtikelnrhersteller(herstellerArtNr);
		}

		String herstellerCBez = result.getValues().get(
				StklImportSpezifikation.HERSTELLERBEZ);
		if (updateNeeded(herstellerCBez,
				artikelDtoDB.getCArtikelbezhersteller())) {
			artikelNeedsUpdate = true;
			artikelDtoDB.setCArtikelbezhersteller(Helper.cutString(
					herstellerCBez,
					ArtikelFac.MAX_ARTIKEL_HERSTELLERBEZEICHNUNG));
		}

		if (artikelNeedsUpdate) {
			updateArtikel(artikelDtoDB, theClientDto);
		}
	}

	/**
	 * Vergleicht zwei Strings miteinander, um zu &uuml;berpr&uuml;fen, ob ein
	 * Update des DB-Eintrags n&ouml;tig ist.
	 * 
	 * @param newValue
	 *            neuer zu vergleichender Wert
	 * @param dbValue
	 *            Basiswert aus DB
	 * @return true, wenn ein Update durchgef&uuml;hrt werden soll
	 */
	private boolean updateNeeded(String newValue, String dbValue) {
		if (newValue == null)
			return false; // kein neuer wert -> false
		newValue = newValue.trim();
		if (newValue.isEmpty())
			return false;// kein neuer wert -> false
		if (dbValue == null)
			return true; // kein alter wert -> true
		dbValue = dbValue.trim();
		if (dbValue.isEmpty())
			return true; // kein alter wert -> true
		if (newValue.equals(dbValue))
			return false;// alter wert == neuer wert -> false
		return true; // alter wert != neuer wert -> true
	}

}
