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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.apache.xerces.dom.DocumentImpl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
// import org.hibernate.util.StringHelper;
import org.modelmapper.ModelMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.anfrage.fastlanereader.generated.FLRAnfragepositionlieferdaten;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.ejb.Einkaufsangebotposition;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.ejb.Alergen;
import com.lp.server.artikel.ejb.Artgru;
import com.lp.server.artikel.ejb.Artgrumandant;
import com.lp.server.artikel.ejb.Artgruspr;
import com.lp.server.artikel.ejb.ArtgrusprPK;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.ArtikelQuery;
import com.lp.server.artikel.ejb.ArtikelTruTops;
import com.lp.server.artikel.ejb.ArtikelTruTopsMetadaten;
import com.lp.server.artikel.ejb.ArtikelTruTopsMetadatenQuery;
import com.lp.server.artikel.ejb.ArtikelTruTopsQuery;
import com.lp.server.artikel.ejb.Artikelalergen;
import com.lp.server.artikel.ejb.Artikelart;
import com.lp.server.artikel.ejb.Artikelartspr;
import com.lp.server.artikel.ejb.ArtikelartsprPK;
import com.lp.server.artikel.ejb.Artikellager;
import com.lp.server.artikel.ejb.ArtikellagerPK;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.ArtikellieferantQuery;
import com.lp.server.artikel.ejb.Artikellieferantstaffel;
import com.lp.server.artikel.ejb.ArtikellieferantstaffelQuery;
import com.lp.server.artikel.ejb.Artikellog;
import com.lp.server.artikel.ejb.Artikelshopgruppe;
import com.lp.server.artikel.ejb.Artikelsperren;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprPK;
import com.lp.server.artikel.ejb.ArtikelsprQuery;
import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.artikel.ejb.Artklaspr;
import com.lp.server.artikel.ejb.ArtklasprPK;
import com.lp.server.artikel.ejb.Automotive;
import com.lp.server.artikel.ejb.Einkaufsean;
import com.lp.server.artikel.ejb.EinkaufseanQuery;
import com.lp.server.artikel.ejb.Ersatztypen;
import com.lp.server.artikel.ejb.Farbcode;
import com.lp.server.artikel.ejb.Gebinde;
import com.lp.server.artikel.ejb.Geometrie;
import com.lp.server.artikel.ejb.Handlagerbewegung;
import com.lp.server.artikel.ejb.Hersteller;
import com.lp.server.artikel.ejb.HerstellerQuery;
import com.lp.server.artikel.ejb.Katalog;
import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.ejb.Laseroberflaeche;
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
import com.lp.server.artikel.ejb.Verpackungsmittel;
import com.lp.server.artikel.ejb.Verpackungsmittelspr;
import com.lp.server.artikel.ejb.VerpackungsmittelsprPK;
import com.lp.server.artikel.ejb.Verschleissteil;
import com.lp.server.artikel.ejb.Verschleissteilwerkzeug;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.ejb.VkPreisfindungPreisliste;
import com.lp.server.artikel.ejb.Vorschlagstext;
import com.lp.server.artikel.ejb.Vorzug;
import com.lp.server.artikel.ejb.Webshop;
import com.lp.server.artikel.ejb.WebshopArtikel;
import com.lp.server.artikel.ejb.WebshopArtikelPreisliste;
import com.lp.server.artikel.ejb.WebshopArtikelQuery;
import com.lp.server.artikel.ejb.WebshopKunde;
import com.lp.server.artikel.ejb.WebshopKundeQuery;
import com.lp.server.artikel.ejb.WebshopMwstsatzbez;
import com.lp.server.artikel.ejb.WebshopMwstsatzbezQuery;
import com.lp.server.artikel.ejb.WebshopPreislisteQuery;
import com.lp.server.artikel.ejb.WebshopShopgruppe;
import com.lp.server.artikel.ejb.WebshopShopgruppeQuery;
import com.lp.server.artikel.ejb.Webshopart;
import com.lp.server.artikel.ejb.WebshopartQuery;
import com.lp.server.artikel.ejb.Webshopartspr;
import com.lp.server.artikel.ejb.WebshopartsprPK;
import com.lp.server.artikel.ejb.Werkzeug;
import com.lp.server.artikel.ejb.Zugehoerige;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelklasse;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferantstaffel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellistespr;
import com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe;
import com.lp.server.artikel.fastlanereader.generated.FLRTrumphtopslog;
import com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreis;
import com.lp.server.artikel.fastlanereader.generated.FLRVorschlagstext;
import com.lp.server.artikel.service.*;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.ejb.Artgrurolle;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.bl.VendidataArticleExportTransformer;
import com.lp.server.fertigung.bl.VendidataArticlesMarshaller;
import com.lp.server.fertigung.bl.VendidataXmlArticleTransformResult;
import com.lp.server.fertigung.ejbfac.VendidataArticleExportBeanHolder;
import com.lp.server.fertigung.ejbfac.VendidataArticleExportBeanServices;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.service.IVendidataArticleExportBeanServices;
import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.fastlanereader.generated.FLRKundesoko;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.shop.service.WebshopConnectionDto;
import com.lp.server.stueckliste.ejb.Stuecklisteposition;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;
import com.lp.server.util.ArtikelTruTopsMetadatenId;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KundeId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.PreislisteId;
import com.lp.server.util.ShopgruppeId;
import com.lp.server.util.Validator;
import com.lp.server.util.WebshopId;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.KeyValue;
import com.lp.util.SiWertParser;
import com.lp.util.gs1.GTIN13;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CellReferenceHelper;
import jxl.format.Colour;
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

	private String PREISPFLEGE_XLS_SPALTE_VKBASIS = "VK-Basis";
	private String PREISPFLEGE_XLS_SPALTE_GUELTIGAB = "G\u00FCltig seit";

	public void preiseDesBasisartikelAndhandDesZuschnittartikelsNeuBerechnen(Integer artikelIIdZuschnitt,
			Integer artikellieferantIId, TheClientDto theClientDto) {
		ArtikelDto artikelDtoZuschnitt = artikelFindByPrimaryKey(artikelIIdZuschnitt, theClientDto);
		ArtikellieferantDto alDtoZuschnitt = artikellieferantFindByPrimaryKey(artikellieferantIId, theClientDto);

		Integer artikelIIdBasis = getBasisArtikel(artikelIIdZuschnitt, theClientDto);

		if (artikelIIdBasis != null) {
			ArtikelDto artikelDtoBasis = artikelFindByPrimaryKey(artikelIIdBasis, theClientDto);
			try {
				EinheitDto eDtoBasis = getSystemFac().einheitFindByPrimaryKey(artikelDtoBasis.getEinheitCNr(),
						theClientDto);

				if (eDtoBasis.getIDimension() > 0
						&& artikelDtoZuschnitt.getEinheitCNr().equals(SystemFac.EINHEIT_STUECK)) {
					// Von Zuschnitt nach Basis rechnen

					if (artikelDtoZuschnitt.getGeometrieDto() != null
							&& artikelDtoZuschnitt.getGeometrieDto().getFBreite() != null) {
						BigDecimal bdZielmenge = new BigDecimal(artikelDtoZuschnitt.getGeometrieDto().getFBreite());

						if (eDtoBasis.getIDimension() > 1
								&& artikelDtoZuschnitt.getGeometrieDto().getFHoehe() != null) {
							bdZielmenge = bdZielmenge
									.multiply(new BigDecimal(artikelDtoZuschnitt.getGeometrieDto().getFHoehe()));

							if (eDtoBasis.getIDimension() > 2
									&& artikelDtoZuschnitt.getGeometrieDto().getFTiefe() != null) {
								bdZielmenge = bdZielmenge
										.multiply(new BigDecimal(artikelDtoZuschnitt.getGeometrieDto().getFTiefe()));
							}
						}

						if (eDtoBasis.getIDimension() == 1) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_MILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						} else if (eDtoBasis.getIDimension() == 2) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_QUADRATMILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						} else if (eDtoBasis.getIDimension() == 3) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_KUBIKMILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						}

						// Preise neu berechnen

						ArtikellieferantDto alDto = getArtikelFac()
								.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(artikelIIdBasis,
										alDtoZuschnitt.getLieferantIId(), alDtoZuschnitt.getTPreisgueltigab(),
										theClientDto);

						if (bdZielmenge.doubleValue() > 0) {

							BigDecimal bdPreisAlt = alDtoZuschnitt.getNNettopreis();

							if (alDto != null && alDto.getNInitialkosten() != null) {
								bdPreisAlt = bdPreisAlt.subtract(alDto.getNInitialkosten());
								if (bdPreisAlt.doubleValue() < 0) {
									bdPreisAlt = BigDecimal.ZERO;
								}
							}

							BigDecimal bdNettoPreisNeu = Helper
									.rundeKaufmaennisch(
											bdPreisAlt.divide(bdZielmenge,
													getMandantFac().getNachkommastellenPreisEK(
															theClientDto.getMandant()),
													BigDecimal.ROUND_HALF_EVEN),
											getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));
							Integer artikellieferantIIdBasis = null;
							if (alDto != null && bdZielmenge.doubleValue() > 0) {

								alDto.setFRabatt(0D);

								alDto.setNEinzelpreis(bdNettoPreisNeu);

								alDto.setNNettopreis(bdNettoPreisNeu);

								getArtikelFac().updateArtikellieferant(alDto, theClientDto);
								artikellieferantIIdBasis = alDto.getIId();
							} else {
								// Neu anlegen
								alDto = new ArtikellieferantDto();
								alDto.setArtikelIId(artikelIIdBasis);
								alDto.setLieferantIId(alDtoZuschnitt.getLieferantIId());
								alDto.setTPreisgueltigab(alDtoZuschnitt.getTPreisgueltigab());
								alDto.setNEinzelpreis(bdNettoPreisNeu);
								alDto.setFRabatt(0D);
								alDto.setNNettopreis(bdNettoPreisNeu);

								alDto.setBRabattbehalten(Helper.boolean2Short(false));

								artikellieferantIIdBasis = getArtikelFac().createArtikellieferant(alDto, theClientDto);
							}

							preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(artikelDtoBasis.getIId(),
									artikellieferantIIdBasis, theClientDto);

						}

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	public void preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(Integer artikelIId,
			Integer artikellieferantIId, TheClientDto theClientDto) {

		//

		// Dimensionen in Artikel updaten
		ArtikelDto artikelDtoBasis = artikelFindByPrimaryKey(artikelIId, theClientDto);

		ArtikellieferantDto alDtoBasis = artikellieferantFindByPrimaryKey(artikellieferantIId, theClientDto);

		ArtikellieferantDto[] dtos = artikellieferantFindByArtikelIId(artikelIId, theClientDto);
		boolean bBasisIstLief1 = false;
		if (dtos.length > 0 && dtos[0].getLieferantIId() == alDtoBasis.getLieferantIId()) {
			bBasisIstLief1 = true;
		}

		try {
			EinheitDto eDtoBasis = getSystemFac().einheitFindByPrimaryKey(artikelDtoBasis.getEinheitCNr(),
					theClientDto);

			String sQuery = "SELECT a FROM FLRArtikelliste a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND a.c_nr LIKE '" + artikelDtoBasis.getCNr() + "\\_%' AND a.i_id<>" + artikelDtoBasis.getIId()
					+ " ORDER BY a.c_nr ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				// Artikel ist bereits vorhande, diesen verwenden
				FLRArtikelliste flrArtikelliste = (FLRArtikelliste) resultListIterator.next();

				if (eDtoBasis.getIDimension() > 0) {
					if (flrArtikelliste.getFlrgeometrie() != null
							&& flrArtikelliste.getFlrgeometrie().getF_breite() != null) {
						BigDecimal bdZielmenge = new BigDecimal(flrArtikelliste.getFlrgeometrie().getF_breite());

						if (eDtoBasis.getIDimension() > 1 && flrArtikelliste.getFlrgeometrie().getF_hoehe() != null) {
							bdZielmenge = bdZielmenge
									.multiply(new BigDecimal(flrArtikelliste.getFlrgeometrie().getF_hoehe()));

							if (eDtoBasis.getIDimension() > 2
									&& flrArtikelliste.getFlrgeometrie().getF_tiefe() != null) {
								bdZielmenge = bdZielmenge
										.multiply(new BigDecimal(flrArtikelliste.getFlrgeometrie().getF_tiefe()));
							}
						}

						if (eDtoBasis.getIDimension() == 1) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_MILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						} else if (eDtoBasis.getIDimension() == 2) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_QUADRATMILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						} else if (eDtoBasis.getIDimension() == 3) {

							bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
									SystemFac.EINHEIT_KUBIKMILLIMETER, eDtoBasis.getCNr(), null, theClientDto);

						}

						// Preise neu berechnen

						BigDecimal bdNettoPreisNeu = Helper.rundeKaufmaennisch(
								alDtoBasis.getNNettopreis().multiply(bdZielmenge),
								getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));

						if (alDtoBasis.getNInitialkosten() != null) {
							bdNettoPreisNeu = bdNettoPreisNeu.add(alDtoBasis.getNInitialkosten());

						}

						ArtikellieferantDto alDto = getArtikelFac()
								.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
										flrArtikelliste.getI_id(), alDtoBasis.getLieferantIId(),
										alDtoBasis.getTPreisgueltigab(), theClientDto);

						if (alDto != null) {

							alDto.setNInitialkosten(null);

							alDto.setFRabatt(0D);

							alDto.setNEinzelpreis(bdNettoPreisNeu);

							alDto.setNNettopreis(bdNettoPreisNeu);

							getArtikelFac().updateArtikellieferant(alDto, theClientDto);

						} else {
							// Neu anlegen
							alDto = new ArtikellieferantDto();
							alDto.setArtikelIId(flrArtikelliste.getI_id());
							alDto.setLieferantIId(alDtoBasis.getLieferantIId());
							alDto.setTPreisgueltigab(alDtoBasis.getTPreisgueltigab());
							alDto.setNEinzelpreis(bdNettoPreisNeu);
							alDto.setFRabatt(0D);
							alDto.setNNettopreis(bdNettoPreisNeu);

							alDto.setBRabattbehalten(Helper.boolean2Short(false));

							Integer artikellieferantIIdNeu = getArtikelFac().createArtikellieferant(alDto,
									theClientDto);

							// Wenn BasisArtikel = Lief1, dann

							if (bBasisIstLief1) {
								artikellieferantAlsErstesReihen(flrArtikelliste.getI_id(), artikellieferantIIdNeu);
							}

						}

					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public Integer kopiereArtikelFuerDimensionenBestellen(Integer artikelIId, BigDecimal bdPositionsmenge,
			Integer dimension1, Integer dimension2, Integer dimension3, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIId, theClientDto);

		EinheitDto eDto = getSystemFac().einheitFindByPrimaryKey(artikelDto.getEinheitCNr(), theClientDto);

		// Zuerst nachsehen ob es einen Artikel mit diesen Dimensionen schon
		// gibt
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT a FROM FLRArtikelliste a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND a.c_nr LIKE '" + artikelDto.getCNr() + "%'";

		if (eDto.getIDimension() == 1) {
			sQuery += " AND cast( a.flrgeometrie.f_breite as int)=" + dimension1
					+ " AND a.flrgeometrie.f_hoehe IS null AND a.flrgeometrie.f_tiefe IS null";
		} else if (eDto.getIDimension() == 2) {
			sQuery += " AND cast( a.flrgeometrie.f_breite as int)=" + dimension1
					+ " AND  cast( a.flrgeometrie.f_hoehe as int)=" + dimension2
					+ " AND a.flrgeometrie.f_tiefe IS null";
		} else if (eDto.getIDimension() == 3) {
			sQuery += " AND cast( a.flrgeometrie.f_breite as int)=" + dimension1
					+ " AND  cast( a.flrgeometrie.f_hoehe as int)=" + dimension2
					+ " AND  cast( a.flrgeometrie.f_tiefe as int)=" + dimension3;
		}

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {
			// Artikel ist bereits vorhande, diesen verwenden
			FLRArtikelliste flrArtikelliste = (FLRArtikelliste) resultListIterator.next();
			return flrArtikelliste.getI_id();

		} else {

			// Artikelnummer generieren
			String artikelnummer = null;
			if (eDto.getIDimension() == 1) {
				artikelnummer = artikelDto.getCNr() + "_" + Helper.fitString2LengthAlignRight(dimension1 + "", 4, '0');
			} else {
				artikelnummer = generiereNeueArtikelnummer(artikelDto.getCNr() + "_0001", theClientDto);
			}

			ArtikelDto artikelDtoVorhanden = artikelFindByCNrMandantCNrOhneExc(artikelnummer,
					theClientDto.getMandant());
			if (artikelDtoVorhanden != null) {
				// Wenn es die Artikelnummer schon gibt, dann Fehler -die
				// vorgeschlagene Artikelnummer ist mit abweichenden Dimensionen
				// bereits vorhanden-
				ArrayList al = new ArrayList();
				al.add(artikelDtoVorhanden.getCNr());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DIMENSIONEN_BESTELLEN_ARTIKELNUMMERBEREITSVORHANDEN, al,
						new Exception("FEHLER_DIMENSIONEN_BESTELLEN_ARTIKELNUMMERBEREITSVORHANDEN"));

			} else {

				HashMap hmZuKopieren = new java.util.HashMap();
				hmZuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE, "");

				Integer artikelIIdNeu = (Integer) kopiereArtikel(artikelIId, artikelnummer, hmZuKopieren, null, null,
						theClientDto)[0];

				// Dimensionen in Artikel updaten
				ArtikelDto artikelDtoNeu = artikelFindByPrimaryKey(artikelIIdNeu, theClientDto);

				artikelDtoNeu.setEinheitCNr(SystemFac.EINHEIT_STUECK);

				if (artikelDtoNeu.getGeometrieDto() == null) {
					artikelDtoNeu.setGeometrieDto(new GeometrieDto());
				}

				BigDecimal bdZielmenge = new BigDecimal(dimension1);

				artikelDtoNeu.getGeometrieDto().setFBreite((double) dimension1);
				if (eDto.getIDimension() > 1) {
					artikelDtoNeu.getGeometrieDto().setFHoehe((double) dimension2);
					bdZielmenge = bdZielmenge.multiply(new BigDecimal(dimension2));
				}
				if (eDto.getIDimension() > 2) {
					artikelDtoNeu.getGeometrieDto().setFTiefe((double) dimension3);
					bdZielmenge = bdZielmenge.multiply(new BigDecimal(dimension3));

				}

				// EK-Preise umrechnen
				ArtikellieferantDto[] artikellieferantDtos = artikellieferantFindByArtikelIId(artikelIId, theClientDto);

				BigDecimal bdZielmengeVorher = bdZielmenge;

				if (eDto.getIDimension() == 1) {
					bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge, SystemFac.EINHEIT_MILLIMETER,
							artikelDto.getEinheitCNr(), null, theClientDto);
				} else if (eDto.getIDimension() == 2) {
					// SP6191
					bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge,
							SystemFac.EINHEIT_QUADRATMILLIMETER, artikelDto.getEinheitCNr(), null, theClientDto);
				} else if (eDto.getIDimension() == 3) {
					// SP6191
					bdZielmenge = getSystemFac().rechneUmInAndereEinheit(bdZielmenge, SystemFac.EINHEIT_KUBIKMILLIMETER,
							artikelDto.getEinheitCNr(), null, theClientDto);
				}

				if (artikelDto.getEinheitCNrBestellung() != null && artikelDto.getNUmrechnungsfaktor() != null) {

					// Faktor berechnen
					BigDecimal faktor = BigDecimal.ONE;

					if (bdZielmenge.doubleValue() != 0) {
						faktor = bdZielmengeVorher.divide(bdZielmenge, 8, BigDecimal.ROUND_HALF_EVEN);
					}

					artikelDtoNeu.setEinheitCNrBestellung(artikelDto.getEinheitCNrBestellung());
					if (faktor.doubleValue() != 0) {
						artikelDtoNeu.setNUmrechnungsfaktor(Helper.rundeKaufmaennisch(artikelDto.getNUmrechnungsfaktor()
								.divide(faktor, 6, BigDecimal.ROUND_HALF_EVEN).multiply(bdZielmengeVorher), 6));
					}

					artikelDtoNeu.setbBestellmengeneinheitInvers(artikelDto.getbBestellmengeneinheitInvers());
				}

				updateArtikel(artikelDtoNeu, theClientDto);

				for (int i = 0; i < artikellieferantDtos.length; i++) {

					LieferantDto lfDto = getLieferantFac()
							.lieferantFindByPrimaryKey(artikellieferantDtos[i].getLieferantIId(), theClientDto);

					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikelIId,
							artikellieferantDtos[i].getLieferantIId(), bdZielmenge.multiply(bdPositionsmenge),
							lfDto.getWaehrungCNr(), new java.sql.Date(System.currentTimeMillis()), theClientDto);

					ArtikellieferantDto artikellieferantDto = artikellieferantDtos[i];
					Integer artikellieferantIId = artikellieferantDto.getIId();
					artikellieferantDto.setArtikelIId(artikelIIdNeu);
					artikellieferantDto.setIId(null);
					artikellieferantDto.setFMindestbestelmenge(null);

					if (alDto != null && alDto.getNNettopreis() != null) {

						artikellieferantDto.setNNettopreis(alDto.getNNettopreis());

						BigDecimal bdNettoPreisNeu = Helper.rundeKaufmaennisch(
								artikellieferantDto.getNNettopreis().multiply(bdZielmenge),
								getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));

						if (artikellieferantDto.getNInitialkosten() != null) {
							bdNettoPreisNeu = bdNettoPreisNeu.add(artikellieferantDto.getNInitialkosten());
							artikellieferantDto.setNInitialkosten(null);
						}

						artikellieferantDto.setNEinzelpreis(bdNettoPreisNeu);
						artikellieferantDto.setFRabatt(0D);
						artikellieferantDto.setNNettopreis(bdNettoPreisNeu);

						Integer artikellieferantIId_Neu = createArtikellieferant(artikellieferantDto, theClientDto);
					}

				}

				return artikelIIdNeu;
			}

		}

	}

	public Object[] kopiereArtikel(Integer artikelIId, String artikelnummerNeu, java.util.HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIId, theClientDto);

		artikelDto.setCNr(artikelnummerNeu);
		artikelDto.setIId(null);

		if (herstellerIIdNeu != null) {
			artikelDto.setHerstellerIId(herstellerIIdNeu);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG,
					theClientDto)) {
				// SP5385
				artikelDto.setCArtikelnrhersteller(null);
				artikelDto.setCArtikelbezhersteller(null);

			}
		} else {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG,
					theClientDto)) {
				// SP5385
				artikelDto.setCArtikelnrhersteller(null);
				artikelDto.setCArtikelbezhersteller(null);

			} else {
				if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HERSTELLER)) {
					artikelDto.setHerstellerIId(null);
				}
			}

		}

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELGRUPPE)) {
			artikelDto.setArtgruIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ARTIKELKLASSE)) {
			artikelDto.setArtklaIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_SHOPGRUPPE)) {
			artikelDto.setShopgruppeIId(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_REFERENZNUMMER)) {
			artikelDto.setCReferenznr(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_LAGERMINDESTSTAND)) {
			artikelDto.setFLagermindest(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_LAGERSOLLSTAND)) {
			artikelDto.setFLagersoll(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERPACKUNSMENGE)) {
			artikelDto.setFVerpackungsmenge(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTFAKTOR)) {
			artikelDto.setFVerschnittfaktor(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERSCHNITTBASIS)) {
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
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MATERIALGEWICHT)) {
			artikelDto.setFMaterialgewicht(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ZUGEHOERIGERARTIKEL)) {
			artikelDto.setArtikelIIdZugehoerig(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERTRETERPROVISION)) {
			artikelDto.setFVertreterprovisionmax(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR1)) {
			artikelDto.setFMinutenfaktor1(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINUTENFAKTOR2)) {
			artikelDto.setFMinutenfaktor2(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG)) {
			artikelDto.setFMindestdeckungsbeitrag(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_VERKAUFSEAN)) {
			artikelDto.setCVerkaufseannr(null);
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_WARENVERKEHRSNUMMER)) {
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

		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_BESTELLMENGENEINHEIT)) {
			artikelDto.setEinheitCNrBestellung(null);
			artikelDto.setNUmrechnungsfaktor(null);
			artikelDto.setbBestellmengeneinheitInvers(Helper.boolean2Short(false));
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
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_FERTIGUNGSSATZGROESSE)) {
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
				artikelDto.getMontageDto().setBHochstellen(new Short((short) 0));
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_HOCHSETZEN)) {
			if (artikelDto.getMontageDto() != null) {
				artikelDto.getMontageDto().setBHochsetzen(new Short((short) 0));
			}
		}
		if (!zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_ANTISTATIC)) {
			artikelDto.setBAntistatic(Helper.boolean2Short(false));
		}

		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_SNRBEHAFTET)) {
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(true));
			artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
		}

		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_CHNRBEHAFTET)) {
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
			artikelDto.setBChargennrtragend(Helper.boolean2Short(true));
			artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
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

			if (!artikelsprTemp.getPk().getLocaleCNr().equals(theClientDto.getLocUiAsString())) {
				bAndereSprachenKopiert = true;
				Artikelspr artikelsprNeu = new Artikelspr(artikelIId_Neu, artikelsprTemp.getPk().getLocaleCNr(),
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
						.paneldatenFindByPanelCNrCKey(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, artikelIId + "");
				for (int i = 0; i < paneldatenDtos.length; i++) {
					paneldatenDtos[i].setCKey(artikelIId_Neu + "");
				}
				getPanelFac().createPaneldaten(paneldatenDtos, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Technik-Eigenschaften
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_TECHNIK_EIGENSCHAFTEN)) {

			try {
				PaneldatenDto[] paneldatenDtos = getPanelFac()
						.paneldatenFindByPanelCNrCKey(PanelFac.PANEL_ARTIKELTECHNIK, artikelIId + "");
				for (int i = 0; i < paneldatenDtos.length; i++) {
					paneldatenDtos[i].setCKey(artikelIId_Neu + "");
				}
				getPanelFac().createPaneldaten(paneldatenDtos, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Kommentar
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE)) {

			try {
				boolean bAnderssprachigenKommentarKopiert = getArtikelkommentarFac().kopiereArtikelkommentar(artikelIId,
						artikelIId_Neu, false, theClientDto);

				if (bAnderssprachigenKommentarKopiert == true) {
					bAndereSprachenKopiert = true;
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Artikellieferant
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_EKPREISE)) {
			ArtikellieferantDto[] artikellieferantDtos = artikellieferantFindByArtikelIId(artikelIId, theClientDto);

			for (int i = 0; i < artikellieferantDtos.length; i++) {
				ArtikellieferantDto artikellieferantDto = artikellieferantDtos[i];
				Integer artikellieferantIId = artikellieferantDto.getIId();
				artikellieferantDto.setArtikelIId(artikelIId_Neu);
				artikellieferantDto.setIId(null);
				artikellieferantDto.setAnfragepositionlieferdatenIId(null);
				artikellieferantDto.setCAngebotnummer(null);
				Integer artikellieferantIId_Neu = createArtikellieferant(artikellieferantDto, theClientDto);

				ArtikellieferantstaffelDto[] artikellieferantstaffelDtos = artikellieferantstaffelFindByArtikellieferantIId(
						artikellieferantIId);
				for (int j = 0; j < artikellieferantstaffelDtos.length; j++) {
					ArtikellieferantstaffelDto artikellieferantstaffelDto = artikellieferantstaffelDtos[j];
					artikellieferantstaffelDto.setIId(null);
					artikellieferantstaffelDto.setAnfragepositionlieferdatenIId(null);
					artikellieferantstaffelDto.setArtikellieferantIId(artikellieferantIId_Neu);
					artikellieferantstaffelDto.setCAngebotnummer(null);
					createArtikellieferantstaffel(artikellieferantstaffelDto, theClientDto);
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
						.vkpfartikelverkaufspreisbasisFindByArtikelIId(artikelIId, theClientDto);

				for (int i = 0; i < dtos.length; i++) {
					VkPreisfindungEinzelverkaufspreisDto dto = dtos[i];
					if (dto.getTVerkaufspreisbasisgueltigab().after(new Date(System.currentTimeMillis()))) {
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
				query.setParameter(3, Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis())));
				Collection<?> cl = query.getResultList();

				if (cl.size() > 0) {

					VkPreisfindungEinzelverkaufspreis basis = (VkPreisfindungEinzelverkaufspreis) cl.iterator().next();

					VkPreisfindungEinzelverkaufspreisDto basisDto = VkPreisfindungEinzelverkaufspreisDtoAssembler
							.createDto(basis);
					basisDto.setIId(null);
					basisDto.setArtikelIId(artikelIId_Neu);
					basisDto.setTVerkaufspreisbasisgueltigab(Helper.cutDate(new Date(System.currentTimeMillis())));
					getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(basisDto, theClientDto);

				}

				// Vorher Artikelpreislisten loeschen, da bei createArtikel
				// schon
				// welche angelegt werden.
				VkPreisfindungPreislisteDto[] vkPreisfindungPreislisteDtos = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(artikelIId_Neu);
				for (int i = 0; i < vkPreisfindungPreislisteDtos.length; i++) {
					getVkPreisfindungFac().removeVkPreisfindungPreisliste(vkPreisfindungPreislisteDtos[i]);
				}

				// Nun fuer jede aktive Preisliste den letzen Preis holen und
				// mit gueltigektie heute anlegen

				VkpfartikelpreislisteDto[] vkpreislistenDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true), theClientDto);

				for (int i = 0; i < vkpreislistenDtos.length; i++) {

					Query ejbquery = em.createNamedQuery(
							"VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab");
					ejbquery.setParameter(1, artikelIId);
					ejbquery.setParameter(2, vkpreislistenDtos[i].getIId());
					ejbquery.setParameter(3, Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

					Collection c = ejbquery.getResultList();

					if (c.size() > 0) {

						VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) c.iterator()
								.next();

						VkPreisfindungPreislisteDto artikelpreislisteDto = VkPreisfindungPreislisteDtoAssembler
								.createDto(vkPreisfindungPreisliste);

						artikelpreislisteDto.setArtikelIId(artikelIId_Neu);
						artikelpreislisteDto.setTPreisgueltigab(Helper.cutDate(new Date(System.currentTimeMillis())));

						getVkPreisfindungFac().createVkPreisfindungPreisliste(artikelpreislisteDto, theClientDto);

					}

				}

				vkPreisfindungPreislisteDtos = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(artikelIId);

				for (int i = 0; i < vkPreisfindungPreislisteDtos.length; i++) {
					VkPreisfindungPreislisteDto dto = vkPreisfindungPreislisteDtos[i];

					if (dto.getTPreisgueltigab().after(new Date(System.currentTimeMillis()))) {
						bEsSindZukuenftigePreisevorhanden = true;
					}

				}

				// Mengenstaffeln

				VkpfMengenstaffelDto[] vkpfMengenstaffelDtos = getVkPreisfindungFac()
						.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(artikelIId,
								new java.sql.Date(System.currentTimeMillis()), null, theClientDto);

				for (int i = 0; i < vkpfMengenstaffelDtos.length; i++) {
					VkpfMengenstaffelDto dto = vkpfMengenstaffelDtos[i];
					dto.setIId(null);
					dto.setArtikelIId(artikelIId_Neu);
					dto.setTPreisgueltigab(new Date(System.currentTimeMillis()));
					getVkPreisfindungFac().createVkpfMengenstaffel(dto, theClientDto);
				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// PJ20230
		if (zuKopieren.containsKey(ArtikelFac.ARTIKEL_KOPIEREN_URSPRUNGSTEIL_VERLINKEN)) {
			ArtikelDto artikelDtoAlt = artikelFindByPrimaryKey(artikelIId, theClientDto);
			artikelDtoAlt.setArtikelIIdErsatz(artikelIId_Neu);
			updateArtikel(artikelDtoAlt, theClientDto);
		}

		if (stuecklistepositionIId != null) {
			StuecklistepositionDto stklPosDto = getStuecklisteFac()
					.stuecklistepositionFindByPrimaryKey(stuecklistepositionIId, theClientDto);
			stklPosDto.setArtikelIId(artikelIId_Neu);
			getStuecklisteFac().updateStuecklisteposition(stklPosDto, theClientDto);
		}

		return new Object[] { artikelIId_Neu, bAndereSprachenKopiert, bEsSindZukuenftigePreisevorhanden };
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

	private SiWertParser createSiWertParser(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ParametermandantDto p = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SI_EINHEITEN,
				ParameterFac.KATEGORIE_ARTIKEL, theClientDto.getMandant());
		String einheiten = p.getCWert();
		p = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SI_OHNE_EINHEIT,
				ParameterFac.KATEGORIE_ARTIKEL, theClientDto.getMandant());
		boolean ohneEinheit = (Boolean) p.getCWertAsObject();

		return new SiWertParser(ohneEinheit, einheiten);
	}

	private BigDecimal berechneSIWert(SiWertParser parser, ArtikelsprDto artikelsprDto) {
		return parser.berechneSiWertAusBezeichnung(artikelsprDto.getCBez(), artikelsprDto.getCZbez(),
				artikelsprDto.getCZbez2());
	}

	private BigDecimal berechneSIWert(SiWertParser parser, Artikelspr artikelspr) {
		return parser.berechneSiWertAusBezeichnung(artikelspr.getCBez(), artikelspr.getCZbez(), artikelspr.getCZbez2());
	}

	/**
	 * Legt einen neuen Artikel- Datensatz an, samt "Satellitentabellen". Ob Artikel
	 * Lagerbewirtschaftet ist, wird in der mandantenabh&auml;ngigen Tabelle.
	 * WW_ARTIKELLAGERBEWIRTSCHAFTET festgelegt
	 * 
	 * @param artikelDto   ArtikelDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return Integer
	 * @throws RemoteException
	 */
	public Integer createArtikel(ArtikelDto artikelDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return createArtikelMitParameterEinmalartikel(artikelDto, false, theClientDto);
	}

	public Integer createArtikelMitParameterEinmalartikel(ArtikelDto artikelDto, boolean bEinmalartikel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (artikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelDto == null"));
		}
		if (artikelDto.getArtikelartCNr() == null || artikelDto.getEinheitCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelDto.getArtikelartCNr() == null || artikelDto.getEinheitCNr() == null"));
		}

		if (bEinmalartikel) {
			artikelDto.setBVersteckt(Helper.boolean2Short(true));

		}

		if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)
				&& artikelDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelDto.getBVersteckt() == null"));
		}
		// Wenn Artikel Handartikel ist, dann darf cNr null sein
		if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

			if (bEinmalartikel == false) {
				if (artikelDto.getCNr() == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
							new Exception("artikelDto.getCNr() == null"));
				}
				pruefeArtikelnummer(artikelDto.getCNr(), theClientDto);

				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SI_WERT,
						theClientDto) && artikelDto.getArtikelsprDto() != null) {

					SiWertParser parser = createSiWertParser(theClientDto);
					artikelDto.getArtikelsprDto().setNSiwert(berechneSIWert(parser, artikelDto.getArtikelsprDto()));
				}
			}
		}

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
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
		if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL) || bEinmalartikel == true) {
			artikelDto.setCNr("~" + artikelDto.getIId());
			// Wenn Handartikel, dann NIE Lagerbewertet bzw. Lagerbewirtschaftet

			if (bEinmalartikel) {
				artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
				artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
				artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			} else {
				artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(false));
				artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
				artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			}

			artikelDto.setBLagerbewertet(Helper.boolean2Short(false));
			artikelDto.setBDokumentenpflicht(Helper.boolean2Short(false));
			artikelDto.setBVerleih(Helper.boolean2Short(false));
			artikelDto.setbNurzurinfo(Helper.boolean2Short(false));
			artikelDto.setBKalkulatorisch(Helper.boolean2Short(false));
			artikelDto.setbReinemannzeit(Helper.boolean2Short(false));
			artikelDto.setBRahmenartikel(Helper.boolean2Short(false));
			artikelDto.setBAzinabnachkalk(Helper.boolean2Short(false));
			artikelDto.setBKommissionieren(Helper.boolean2Short(false));
			artikelDto.setBKeineLagerzubuchung(Helper.boolean2Short(false));
			artikelDto.setIExternerArbeitsgang(0);
			artikelDto.setBWepinfoAnAnforderer(Helper.boolean2Short(false));
			artikelDto.setBVkpreispflichtig(Helper.boolean2Short(true));
			artikelDto.setIPassiveReisezeit(ArtikelFac.REISEZEIT_KEINE);
			artikelDto.setBSummeInBestellung(Helper.boolean2Short(false));
			artikelDto.setBBevorzugt(Helper.boolean2Short(false));
			artikelDto.setBMultiplikatorAufrunden(Helper.boolean2Short(false));
			artikelDto.setBMultiplikatorInvers(Helper.boolean2Short(false));
			artikelDto.setBMeldepflichtig(Helper.boolean2Short(false));
			artikelDto.setBBewilligungspflichtig(Helper.boolean2Short(false));
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

		if (artikelDto.getBVkpreispflichtig() == null) {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_VKPREISPFLICHTIG);
			artikelDto.setBVkpreispflichtig(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));
		}

		if (artikelDto.getArtklaIId() == null) {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELKLASSE_IST_PFLICHTFELD);
			if (((Boolean) parameter.getCWertAsObject()) == true) {

				ArtklaDto[] artklaDtos = artklaFindByMandantCNr(theClientDto);
				if (artklaDtos != null && artklaDtos.length > 0) {
					artikelDto.setArtklaIId(artklaDtos[0].getIId());
				}
			}

		}

		try {
			try {
				// Default Mindestdeckungsbeitrag
				if (artikelDto.getFMindestdeckungsbeitrag() == null) {
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_DECKUNGSBEITRAG);

					artikelDto.setFMindestdeckungsbeitrag(new Double(parameter.getCWert()));
				}
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			Artikel artikel = new Artikel(artikelDto.getIId(), artikelDto.getCNr(), artikelDto.getArtikelartCNr(),
					artikelDto.getEinheitCNr(), artikelDto.getFMindestdeckungsbeitrag(), theClientDto.getIDPersonal(),
					theClientDto.getIDPersonal(), artikelDto.getBVersteckt(), artikelDto.getMandantCNr());
			em.persist(artikel);
			em.flush();

			System.out.println("C_NR:" + artikelDto.getCNr() + " ID:" + artikelDto.getIId());

			if (artikelDto.getBAntistatic() == null) {
				artikelDto.setBAntistatic(artikel.getBAntistatic());
			}

			if (artikelDto.getBLagerbewertet() == null) {
				artikelDto.setBLagerbewertet(artikel.getBLagerbewertet());
			}

			try {

				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_NEUER_ARTIKEL_IST_LAGERBEWIRTSCHAFTET);

				Boolean b = (Boolean) parameter.getCWertAsObject();

				if (artikelDto.getBLagerbewirtschaftet() == null) {
					artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(b));
				}

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}

			if (artikelDto.getBDokumentenpflicht() == null) {
				artikelDto.setBDokumentenpflicht(artikel.getBDokumentenpflicht());
			}
			if (artikelDto.getbNurzurinfo() == null) {
				artikelDto.setbNurzurinfo(artikel.getbNurzurinfo());
			}
			if (artikelDto.getbReinemannzeit() == null) {
				artikelDto.setbReinemannzeit(artikel.getbReinemannzeit());
			}
			if (artikelDto.getbBestellmengeneinheitInvers() == null) {
				artikelDto.setbBestellmengeneinheitInvers(artikel.getbBestellmengeneinheitInvers());
			}
			if (artikelDto.getBWerbeabgabepflichtig() == null) {
				artikelDto.setBWerbeabgabepflichtig(artikel.getBWerbeabgabepflichtig());
			}
			if (artikelDto.getBVerleih() == null) {
				artikelDto.setBVerleih(artikel.getBVerleih());
			}
			if (artikelDto.getBKalkulatorisch() == null) {
				artikelDto.setBKalkulatorisch(artikel.getBKalkulatorisch());
			}

			if (artikelDto.getBRahmenartikel() == null) {
				artikelDto.setBRahmenartikel(artikel.getBRahmenartikel());
			}

			if (artikelDto.getBAzinabnachkalk() == null) {
				artikelDto.setBAzinabnachkalk(artikel.getBAzinabnachkalk());
			}

			if (artikelDto.getBWepinfoAnAnforderer() == null) {
				artikelDto.setBWepinfoAnAnforderer(artikel.getBWepinfoAnAnforderer());
			}

			if (artikelDto.getBKommissionieren() == null) {
				artikelDto.setBKommissionieren(artikel.getBKommissionieren());
			}
			if (artikelDto.getBKeineLagerzubuchung() == null) {
				artikelDto.setBKeineLagerzubuchung(artikel.getBKeineLagerzubuchung());
			}
			if (artikelDto.getIExternerArbeitsgang() == null) {
				artikelDto.setIExternerArbeitsgang(artikel.getIExternerArbeitsgang());
			}

			if (artikelDto.getBVkpreispflichtig() == null) {
				artikelDto.setBVkpreispflichtig(artikel.getBVkpreispflichtig());
			}

			if (artikelDto.getIPassiveReisezeit() == null) {
				artikelDto.setIPassiveReisezeit(artikel.getIPassiveReisezeit());
			}

			if (artikelDto.getBSummeInBestellung() == null) {
				artikelDto.setBSummeInBestellung(artikel.getBSummeInBestellung());
			}
			if (artikelDto.getBBevorzugt() == null) {
				artikelDto.setBBevorzugt(artikel.getBBevorzugt());
			}
			if (artikelDto.getBMultiplikatorAufrunden() == null) {
				artikelDto.setBMultiplikatorAufrunden(artikel.getBMultiplikatorAufrunden());
			}
			if (artikelDto.getBMultiplikatorInvers() == null) {
				artikelDto.setBMultiplikatorInvers(artikel.getBMultiplikatorInvers());
			}

			if (artikelDto.getBMeldepflichtig() == null) {
				artikelDto.setBMeldepflichtig(artikel.getBMeldepflichtig());
			}

			if (artikelDto.getBBewilligungspflichtig() == null) {
				artikelDto.setBBewilligungspflichtig(artikel.getBBewilligungspflichtig());
			}

			artikelDto.setBRabattierbar(artikel.getBRabattierbar());
			if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				// Default-Wert fuer Rabattierbar steht in Mandantparameter
				try {
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_RABATTIERBAR);
					boolean bRabattierbar = ((Boolean) parameter.getCWertAsObject()).booleanValue();
					artikelDto.setBRabattierbar(Helper.boolean2Short(bRabattierbar));

				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}

				Query query = em.createNamedQuery("SperrenfindBDefaultBeiArtikelneuanlage");
				query.setParameter(1, theClientDto.getMandant());

				Collection c = query.getResultList();
				Iterator it = c.iterator();
				while (it.hasNext()) {

					Sperren sperren = (Sperren) it.next();

					ArtikelsperrenDto artikelsperrenDto = new ArtikelsperrenDto();
					artikelsperrenDto.setArtikelIId(artikelDto.getIId());
					artikelsperrenDto.setSperrenIId(sperren.getIId());
					artikelsperrenDto.setCGrund("Neuanlage");

					createArtikelsperren(artikelsperrenDto, theClientDto);

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
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_AUFSCHLAG);
					artikelDto.getSollverkaufDto().setFAufschlag(new Double(parameter.getCWert()));
				} catch (RemoteException ex) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
				}

			}
			try {
				// Default Sollverkauf
				if (artikelDto.getSollverkaufDto().getFSollverkauf() != null) {

					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_ARTIKEL_SOLLVERKAUF);
					artikelDto.getSollverkaufDto().setFSollverkauf(new Double(parameter.getCWert()));
				}

				if (artikelDto.getBSeriennrtragend() == null) {

					// Default Seriennummernbehaftet
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_DEFAULT_SERIENNUMMERNBEHAFTET);

					Boolean b = (Boolean) parameter.getCWertAsObject();

					artikelDto.setBSeriennrtragend(Helper.boolean2Short(b));
					if (b == true) {
						artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
					} else {

						// SP7113

						if (artikelDto.getArtgruIId() != null) {
							Artgru artgru = em.find(Artgru.class, artikelDto.getArtgruIId());

							if (Helper.short2boolean(artgru.getBSeriennrtragend())) {
								artikelDto.setBSeriennrtragend(Helper.getShortTrue());
							}
						}

					}
				}

				if (artikelDto.getBChargennrtragend() == null) {

					// Default Chrgennummernbehaftet
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKEL_DEFAULT_CHARGENNUMMERNBEHAFTET);

					Boolean b = (Boolean) parameter.getCWertAsObject();

					artikelDto.setBChargennrtragend(Helper.boolean2Short(b));

					if (b == true) {
						artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
					} else {
						// SP7113

						if (artikelDto.getArtgruIId() != null) {
							Artgru artgru = em.find(Artgru.class, artikelDto.getArtgruIId());

							if (Helper.short2boolean(artgru.getBChargennrtragend())) {
								artikelDto.setBChargennrtragend(Helper.getShortTrue());
							}
						}
					}

				}

				// PJ 15001
				VkpfartikelpreislisteDto[] preislisteDtos = getVkPreisfindungFac()
						.getAlleAktivenPreislisten(Helper.boolean2Short(true), theClientDto);

				for (int i = 0; i < preislisteDtos.length; i++) {

					if (preislisteDtos[i].getNStandardrabattsatz() != null) {

						VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();

						vkPreisfindungPreislisteDto.setArtikelIId(artikelDto.getIId());
						vkPreisfindungPreislisteDto
								.setNArtikelstandardrabattsatz(preislisteDtos[i].getNStandardrabattsatz());
						vkPreisfindungPreislisteDto
								.setTPreisgueltigab(Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));
						vkPreisfindungPreislisteDto.setVkpfartikelpreislisteIId(preislisteDtos[i].getIId());

						getVkPreisfindungFac().createVkPreisfindungPreisliste(vkPreisfindungPreislisteDto,
								theClientDto);
					}
				}

			} catch (RemoteException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
			if (artikelDto.getBSeriennrtragend().intValue() == 1 || artikelDto.getBChargennrtragend().intValue() == 1) {
				// Wenn Artikel Seriennummern/Chargennummerntragend ist, dann
				// muss er Lagerbewirtschaftet sein
				artikelDto.setBLagerbewirtschaftet(new Short((short) 1));
			}

			if (artikelDto.getBKalkulatorisch() == null) {
				artikelDto.setBKalkulatorisch(Helper.boolean2Short(false));
			}

			setArtikelFromArtikelDto(artikel, artikelDto);
			Integer iId = artikel.getIId();

			if (artikelDto.getArtikelsprDto() != null) {
				Artikelspr artikelspr = new Artikelspr(iId, theClientDto.getLocUiAsString(),
						theClientDto.getIDPersonal());
				em.persist(artikelspr);
				em.flush();
				artikelDto.getArtikelsprDto().setPersonalIIdAendern(theClientDto.getIDPersonal());
				artikelDto.getArtikelsprDto().setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
				setArtikelsprFromArtikelsprDto(artikelspr, artikelDto.getArtikelsprDto(),
						getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant()));
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
				setVerpackungFromVerpackungDto(verpackung, artikelDto.getVerpackungDto());
			}
			if (artikelDto.getSollverkaufDto() != null) {
				Sollverkauf sollverkauf = new Sollverkauf(iId);
				em.persist(sollverkauf);
				em.flush();
				setSollverkaufFromSollverkaufDto(sollverkauf, artikelDto.getSollverkaufDto());
			}
			if (artikelDto.getGeometrieDto() != null) {
				Geometrie geometrie = new Geometrie(iId);
				em.persist(geometrie);
				em.flush();
				setGeometrieFromGeometrieDto(geometrie, artikelDto.getGeometrieDto());
			}

			artikeleigenschaftenDefaultwerteAnlegen(artikelDto.getIId(), artikelDto.getArtgruIId(), theClientDto);

			return artikelDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void createArtikelspr(ArtikelsprDto sprDto, TheClientDto theClientDto) {
		Artikelspr artikelspr = new Artikelspr(sprDto.getArtikelIId(), sprDto.getLocaleCNr(),
				theClientDto.getIDPersonal());
		em.persist(artikelspr);
		em.flush();
		sprDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		sprDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setArtikelsprFromArtikelsprDto(artikelspr, sprDto,
				getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant()));
	}

	/**
	 * Entfernt vorhandenen Artikel- Datensatz samt Satellitentabellen, wenn dieser
	 * nicht mehr in Verwendung ist. Wenn dieser noch in Verwendung ist, wird eine
	 * EJBExceptionLP ausgel&ouml;st.
	 * 
	 * @param iId          Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void removeArtikel(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
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
				ArtikelkommentarDto[] kommentare = getArtikelkommentarFac().artikelkommentarFindByArtikelIId(iId,
						theClientDto);
				for (ArtikelkommentarDto dto : kommentare) {
					getArtikelkommentarFac().removeArtikelkommentar(dto);
				}

				// Preisfindung loeschen
				VkPreisfindungPreislisteDto[] vkPfPl = getVkPreisfindungFac()
						.vkPreisfindungPreislisteFindByArtikelIId(iId);
				for (VkPreisfindungPreislisteDto dto : vkPfPl) {
					getVkPreisfindungFac().removeVkPreisfindungPreisliste(dto.getIId());
				}
				// Einzelverkaufspreise loeschen UW->CK
				VkPreisfindungEinzelverkaufspreisDto[] dtos = getVkPreisfindungFac()
						.vkpfartikelverkaufspreisbasisFindByArtikelIId(iId, theClientDto);

				for (int i = 0; i < dtos.length; i++) {
					getVkPreisfindungFac().removeVkPreisfindungEinzelverkaufspreis(dtos[i], theClientDto);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei Remove Artikel. der Artikel mit der iid " + iId + " existiert nicht");
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
			ArtikelsperrenDto[] artSp = getArtikelFac().artikelsperrenFindByArtikelIId(artikelIId);
			if (artSp.length > 0) {
				sperren = "";
				for (int i = 0; i < artSp.length; i++) {

					String sSperrenbez = getArtikelFac().sperrenFindByPrimaryKey(artSp[i].getSperrenIId()).getCBez();
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
	 * @param cNrSpracheI String
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
			allArtikelgruppeEntries.add(new FLRArtikelgruppe((Integer) object[0], (Integer) object[1]));
		}

		return allArtikelgruppeEntries;
	}

	public boolean sindArtikelgruppenEingeschraenkt(TheClientDto theClientDto) {

		Query query = em.createNamedQuery("ArtgrurollefindBySystemrolleIId");
		query.setParameter(1, theClientDto.getSystemrolleIId());
		Collection<?> clArten = query.getResultList();

		if (clArten.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public Map getAllSprArtgru(TheClientDto theClientDto) {

		int iNurVatergruppenAnzeigen = 0;
		boolean bSorierungNachCNr = false;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELGRUPPE_NUR_VATERGRUPPEN_ANZEIGEN);
			iNurVatergruppenAnzeigen = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKELGRUPPE_NACH_CBEZ_ODER_CNR_ANZEIGEN);
			bSorierungNachCNr = (Boolean) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// PJ19982
		Query queryArtgrurolle = em.createNamedQuery("ArtgrurollefindBySystemrolleIId");
		queryArtgrurolle.setParameter(1, theClientDto.getSystemrolleIId());
		Collection<?> clArten = queryArtgrurolle.getResultList();

		HashSet hsZugelasseneArtgru = null;
		if (clArten.size() > 0) {
			hsZugelasseneArtgru = new HashSet();

			Iterator<?> itArten = clArten.iterator();

			while (itArten.hasNext()) {
				Artgrurolle artgrurolle = (Artgrurolle) itArten.next();
				hsZugelasseneArtgru.add(artgrurolle.getArtgruIId());
			}

		}

		if (iNurVatergruppenAnzeigen == 2) {

			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = getNewSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez" + " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr" + " WHERE ag.artgru_i_id IS NULL ";

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
				sQuery += " AND ag.mandant_c_nr='" + theClientDto.getMandant() + "'";
			}

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];

				if (hsZugelasseneArtgru == null || hsZugelasseneArtgru.contains(id)) {

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

					ArrayList<ArtikelFilterComboBoxEntry> alUG = holeUntergruppenEingerueckt(id, hsZugelasseneArtgru);

					String filterExpression = "(" + id;

					Iterator it = alUG.iterator();

					while (it.hasNext()) {
						ArtikelFilterComboBoxEntry temp = (ArtikelFilterComboBoxEntry) it.next();

						filterExpression += "," + temp.getFilterExpressionOhneKlammern();
					}

					filterExpression += ")";

					entry.setUntergruppen(alUG);
					entry.setFilterExpression(filterExpression);
					al.add(entry);
				}

			}

			return ArtikelFilterComboBoxEntry.getSortierteListe(al, bSorierungNachCNr);
		} else if (iNurVatergruppenAnzeigen == 1) {

			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = getNewSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez" + " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr" + " WHERE ag.artgru_i_id IS NULL ";

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
				sQuery += " AND ag.mandant_c_nr='" + theClientDto.getMandant() + "'";
			}

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];

				if (hsZugelasseneArtgru == null || hsZugelasseneArtgru.contains(id)) {

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

			}

			return ArtikelFilterComboBoxEntry.getSortierteListe(al, bSorierungNachCNr);
		} else {
			ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList();

			Session session = getNewSession();

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String sQuery = "SELECT ag.i_id, ag.c_nr, aspr.c_bez" + " FROM FLRArtikelgruppe as ag"
					+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr ";

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
				sQuery += " WHERE ag.mandant_c_nr='" + theClientDto.getMandant() + "'";
			}

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				Integer id = (Integer) o[0];

				if (hsZugelasseneArtgru == null || hsZugelasseneArtgru.contains(id)) {

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

			}

			return ArtikelFilterComboBoxEntry.getSortierteListe(al, bSorierungNachCNr);
		}
	}

	public KopfGruppeMitUntergruppen holeAlleArtikelgruppen(Integer artikelgruppeIId) {
		return new KopfGruppeMitUntergruppen(artikelgruppeIId,
				holeUntergruppen(artikelgruppeIId, new HashSet<Integer>()));
	}

	public KopfGruppeMitUntergruppen holeAlleShopgruppen(Integer shopgruppeIId) {
		return new KopfGruppeMitUntergruppen(shopgruppeIId,
				holeShopUntergruppen(shopgruppeIId, new HashSet<Integer>()));
	}

	public KopfGruppeMitUntergruppen holeAlleArtikelklassen(Integer artikelklasseIId) {
		return new KopfGruppeMitUntergruppen(artikelklasseIId,
				holeUnterklassen(artikelklasseIId, new HashSet<Integer>()));
	}

	private HashSet<Integer> holeUntergruppen(Integer id, HashSet<Integer> hs) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory().openSession();
		String sQueryUntegruppen = "SELECT ag FROM FLRArtikelgruppe as ag WHERE ag.artgru_i_id=" + id;
		org.hibernate.Query queryUntergruppen = sessionUntegruppen.createQuery(sQueryUntegruppen);

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {
			FLRArtikelgruppe flrArtikelgruppe = (FLRArtikelgruppe) resultListIteratorUntegruppen.next();
			hs.add(flrArtikelgruppe.getI_id());

			hs = holeUntergruppen(flrArtikelgruppe.getI_id(), hs);

		}

		return hs;
	}

	private HashSet<Integer> holeShopUntergruppen(Integer id, HashSet<Integer> hs) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory().openSession();
		String sQueryUntegruppen = "SELECT sg FROM FLRShopgruppe as sg WHERE sg.flrshopgruppe.i_id=" + id;
		org.hibernate.Query queryUntergruppen = sessionUntegruppen.createQuery(sQueryUntegruppen);

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {
			FLRShopgruppe flrShopgruppe = (FLRShopgruppe) resultListIteratorUntegruppen.next();
			hs.add(flrShopgruppe.getI_id());

			hs = holeShopUntergruppen(flrShopgruppe.getI_id(), hs);

		}

		return hs;
	}

	private HashSet<Integer> holeUnterklassen(Integer id, HashSet<Integer> hs) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory().openSession();
		String sQueryUntegruppen = "SELECT ak FROM FLRArtikelklasse as ak WHERE ak.flrartikelklasse.i_id=" + id;
		org.hibernate.Query queryUntergruppen = sessionUntegruppen.createQuery(sQueryUntegruppen);

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {
			FLRArtikelklasse flrArtikelklasse = (FLRArtikelklasse) resultListIteratorUntegruppen.next();
			hs.add(flrArtikelklasse.getI_id());

			hs = holeUnterklassen(flrArtikelklasse.getI_id(), hs);

		}

		return hs;
	}

	private ArrayList<ArtikelFilterComboBoxEntry> holeUntergruppenEingerueckt(Integer vatergeruppe_id,
			HashSet hsZugelasseneArtgru) {
		// Nun alle Untergruppen holen
		Session sessionUntegruppen = FLRSessionFactory.getFactory().openSession();

		String sQueryUntegruppen = "SELECT ag.i_id, ag.c_nr, aspr.c_bez" + " FROM FLRArtikelgruppe as ag"
				+ " LEFT OUTER JOIN ag.artikelgruppesprset AS aspr WHERE ag.artgru_i_id=" + vatergeruppe_id;

		org.hibernate.Query queryUntergruppen = sessionUntegruppen.createQuery(sQueryUntegruppen);

		ArrayList<ArtikelFilterComboBoxEntry> al = new ArrayList<ArtikelFilterComboBoxEntry>();

		List<?> resultListUntegruppen = queryUntergruppen.list();

		Iterator<?> resultListIteratorUntegruppen = resultListUntegruppen.iterator();
		while (resultListIteratorUntegruppen.hasNext()) {

			Object o[] = (Object[]) resultListIteratorUntegruppen.next();
			Integer id = (Integer) o[0];

			if (hsZugelasseneArtgru == null || hsZugelasseneArtgru.contains(id)) {

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

				ArrayList<ArtikelFilterComboBoxEntry> alUG = holeUntergruppenEingerueckt(id, hsZugelasseneArtgru);

				String filterExpression = id + "";

				Iterator it = alUG.iterator();

				while (it.hasNext()) {
					ArtikelFilterComboBoxEntry temp = (ArtikelFilterComboBoxEntry) it.next();

					filterExpression += "," + temp.getFilterExpressionOhneKlammern();
				}

				entrySub.setFilterExpression("(" + filterExpression + ")");
				entrySub.setFilterExpressionOhneKlammern(filterExpression + "");

				entrySub.setUntergruppen(alUG);

				al.add(entrySub);
			}
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

	public void artikelAenderungLoggen(Integer artikelIId, String key, String von, String nach,
			TheClientDto theClientDto) {
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELLOG);

		if (von != null && von.length() > 80) {
			von = von.substring(0, 79);
		}
		if (nach != null && nach.length() > 80) {
			nach = nach.substring(0, 79);
		}

		Artikellog log = new Artikellog(pk, artikelIId, key, von, nach, theClientDto.getLocUiAsString(),
				theClientDto.getIDPersonal(), new java.sql.Timestamp(System.currentTimeMillis()));
		em.merge(log);
		em.flush();
	}

	private List<Artikellog> artikellogFindByArtikelIId(Integer iid) {
		HvTypedQuery<Artikellog> logs = new HvTypedQuery<Artikellog>(em.createNamedQuery(Artikellog.FindByArtikelIId));
		logs.setParameter("iid", iid);
		return logs.getResultList();
	}

	private void vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(ArtikelDto artikelDto_Aktuell,
			TheClientDto theClientDto) {

		ArtikelDto artikelDto_Vorher = artikelFindByPrimaryKey(artikelDto_Aktuell.getIId(), theClientDto);

		HvDtoLogger<ArtikelDto> artikelLogger = new HvDtoLogger<ArtikelDto>(em, theClientDto);
		artikelLogger.log(artikelDto_Vorher, artikelDto_Aktuell);

		// Artikelnummer
		if (!artikelDto_Aktuell.getCNr().equals(artikelDto_Vorher.getCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_NUMMER,
					artikelDto_Vorher.getCNr(), artikelDto_Aktuell.getCNr(), theClientDto);
		}

		if (!artikelDto_Aktuell.getEinheitCNr().equals(artikelDto_Vorher.getEinheitCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_EINHEIT,
					artikelDto_Vorher.getEinheitCNr(), artikelDto_Aktuell.getEinheitCNr(), theClientDto);
		}
		if (!artikelDto_Aktuell.getBVersteckt().equals(artikelDto_Vorher.getBVersteckt())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_VERSTECKT,
					artikelDto_Vorher.getBVersteckt() + "", artikelDto_Aktuell.getBVersteckt() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getBSeriennrtragend().equals(artikelDto_Vorher.getBSeriennrtragend())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_SNRTRAGEND,
					artikelDto_Vorher.getBSeriennrtragend() + "", artikelDto_Aktuell.getBSeriennrtragend() + "",
					theClientDto);
		}

		if (!artikelDto_Aktuell.getBBewilligungspflichtig().equals(artikelDto_Vorher.getBBewilligungspflichtig())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_BEWILLIGUNGSPFLICHTIG,
					artikelDto_Vorher.getBBewilligungspflichtig() + "",
					artikelDto_Aktuell.getBBewilligungspflichtig() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getBMeldepflichtig().equals(artikelDto_Vorher.getBMeldepflichtig())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_MELDEPFLICHTIG,
					artikelDto_Vorher.getBMeldepflichtig() + "", artikelDto_Aktuell.getBMeldepflichtig() + "",
					theClientDto);
		}

		if (!artikelDto_Aktuell.getBChargennrtragend().equals(artikelDto_Vorher.getBChargennrtragend())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_CHNRTRAGEND,
					artikelDto_Vorher.getBChargennrtragend() + "", artikelDto_Aktuell.getBChargennrtragend() + "",
					theClientDto);
		}
		if (!artikelDto_Aktuell.getBLagerbewirtschaftet().equals(artikelDto_Vorher.getBLagerbewirtschaftet())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_LAGERBEWIRTSCHAFTET,
					artikelDto_Vorher.getBLagerbewirtschaftet() + "", artikelDto_Aktuell.getBLagerbewirtschaftet() + "",
					theClientDto);
		}
		if (!artikelDto_Aktuell.getbNurzurinfo().equals(artikelDto_Vorher.getbNurzurinfo())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_NUR_ZUR_INFO,
					artikelDto_Vorher.getbNurzurinfo() + "", artikelDto_Aktuell.getbNurzurinfo() + "", theClientDto);
		}
		if (!artikelDto_Aktuell.getbReinemannzeit().equals(artikelDto_Vorher.getbReinemannzeit())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_REINE_MANNZEIT,
					artikelDto_Vorher.getbReinemannzeit() + "", artikelDto_Aktuell.getbReinemannzeit() + "",
					theClientDto);
		}
		if (!artikelDto_Aktuell.getArtikelartCNr().equals(artikelDto_Vorher.getArtikelartCNr())) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELART,
					artikelDto_Vorher.getArtikelartCNr() + "", artikelDto_Aktuell.getArtikelartCNr() + "",
					theClientDto);
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
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_BEZEICHNUNG, bezVorher,
					bezNachher, theClientDto);
		}

		if (!(kbezVorher + "").equals(kbezNachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_KURZBEZEICHNUNG, kbezVorher,
					kbezNachher, theClientDto);
		}

		if (!(zbezVorher + "").equals(zbezNachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_ZUSATZBEZ, zbezVorher,
					zbezNachher, theClientDto);
		}

		if (!(zbez2Vorher + "").equals(zbez2Nachher + "")) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_ZUSATZBEZ2, zbez2Vorher,
					zbez2Nachher, theClientDto);
		}

		String freigabeZurueckgenommenVorher = artikelDto_Vorher.getCFreigabeZuerueckgenommen() + "";
		String freigabeZurueckgenommenAktuell = artikelDto_Aktuell.getCFreigabeZuerueckgenommen() + "";

		if (!freigabeZurueckgenommenVorher.equals(freigabeZurueckgenommenAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_FREIGABE_ZURUECKGENOMMEN,
					freigabeZurueckgenommenVorher, freigabeZurueckgenommenAktuell, theClientDto);
		}

		String referenzVorher = artikelDto_Vorher.getCReferenznr() + "";
		String referenzAktuell = artikelDto_Aktuell.getCReferenznr() + "";

		if (!referenzVorher.equals(referenzAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_REFERENZNUMMER, referenzVorher,
					referenzAktuell, theClientDto);
		}

		String indexVorher = artikelDto_Vorher.getCIndex() + "";
		String indexAktuell = artikelDto_Aktuell.getCIndex() + "";

		if (!indexVorher.equals(indexAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_INDEX, indexVorher, indexAktuell,
					theClientDto);
		}

		String revisionVorher = artikelDto_Vorher.getCRevision() + "";
		String revisionAktuell = artikelDto_Aktuell.getCRevision() + "";

		if (!revisionVorher.equals(revisionAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_REVISION, revisionVorher,
					revisionAktuell, theClientDto);
		}

		String bestelleinheitVorher = artikelDto_Vorher.getEinheitCNrBestellung() + "";
		String bestelleinheitAktuell = artikelDto_Aktuell.getEinheitCNrBestellung() + "";

		if (!bestelleinheitVorher.equals(bestelleinheitAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_BESTELLEINHEIT,
					bestelleinheitVorher, bestelleinheitAktuell, theClientDto);
		}
		String umrechnungsfaktorVorher = artikelDto_Vorher.getNUmrechnungsfaktor() + "";
		String umrechnungsfaktorAktuell = artikelDto_Aktuell.getNUmrechnungsfaktor() + "";

		if (!umrechnungsfaktorVorher.equals(umrechnungsfaktorAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_UMRECHNUNGSFAKTOR,
					umrechnungsfaktorVorher, umrechnungsfaktorAktuell, theClientDto);
		}

		String gewichtVorher = artikelDto_Vorher.getFGewichtkg() + "";
		String gewichtAktuell = artikelDto_Aktuell.getFGewichtkg() + "";

		if (!gewichtVorher.equals(gewichtAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_GEWICHTKG, gewichtVorher,
					gewichtAktuell, theClientDto);
		}

		String fertigungssatzgroesseVorher = artikelDto_Vorher.getFFertigungssatzgroesse() + "";
		String fertigungssatzgroesseAktuell = artikelDto_Aktuell.getFFertigungssatzgroesse() + "";

		if (!fertigungssatzgroesseVorher.equals(fertigungssatzgroesseAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_FERTIGUNGSSATZGROESSE,
					fertigungssatzgroesseVorher, fertigungssatzgroesseAktuell, theClientDto);
		}

		String maxfertigungssatzgroesseVorher = artikelDto_Vorher.getFMaxfertigungssatzgroesse() + "";
		String maxfertigungssatzgroesseAktuell = artikelDto_Aktuell.getFMaxfertigungssatzgroesse() + "";

		if (!maxfertigungssatzgroesseVorher.equals(maxfertigungssatzgroesseAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_MAXFERTIGUNGSSATZGROESSE,
					maxfertigungssatzgroesseVorher, maxfertigungssatzgroesseAktuell, theClientDto);
		}

		String lagersollVorher = artikelDto_Vorher.getFLagersoll() + "";
		String lagersollAktuell = artikelDto_Aktuell.getFLagersoll() + "";

		if (!lagersollVorher.equals(lagersollAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_LAGERSOLL, lagersollVorher,
					lagersollAktuell, theClientDto);
		}

		String lagermindestVorher = artikelDto_Vorher.getFLagermindest() + "";
		String lagermindestAktuell = artikelDto_Aktuell.getFLagermindest() + "";

		if (!lagermindestVorher.equals(lagermindestAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_LAGERMINDEST, lagermindestVorher,
					lagermindestAktuell, theClientDto);
		}

		try {
			String mwstsatzVorher = "";
			if (artikelDto_Vorher.getMwstsatzbezIId() != null) {
				MwstsatzbezDto mwstsatzbezDto = getMandantFac()
						.mwstsatzbezFindByPrimaryKey(artikelDto_Vorher.getMwstsatzbezIId(), theClientDto);
				mwstsatzVorher = mwstsatzbezDto.getCBezeichnung();
			}

			String mwstsatzAktuell = "";
			if (artikelDto_Aktuell.getMwstsatzbezIId() != null) {
				MwstsatzbezDto mwstsatzbezDto = getMandantFac()
						.mwstsatzbezFindByPrimaryKey(artikelDto_Aktuell.getMwstsatzbezIId(), theClientDto);
				mwstsatzAktuell = mwstsatzbezDto.getCBezeichnung();
			}
			if (!mwstsatzVorher.equals(mwstsatzAktuell)) {
				artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_MWSTSATZ, mwstsatzVorher,
						mwstsatzAktuell, theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String gruVorher = "";
		if (artikelDto_Vorher.getArtgruIId() != null) {
			ArtgruDto dto = artgruFindByPrimaryKey(artikelDto_Vorher.getArtgruIId(), theClientDto);
			gruVorher = dto.getCNr();
		}

		String gruAktuell = "";
		if (artikelDto_Aktuell.getArtgruIId() != null) {
			ArtgruDto dto = artgruFindByPrimaryKey(artikelDto_Aktuell.getArtgruIId(), theClientDto);
			gruAktuell = dto.getCNr();
		}
		if (!gruVorher.equals(gruAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELGRUPPE, gruVorher,
					gruAktuell, theClientDto);
		}

		String shopgruppeVorher = "";
		if (artikelDto_Vorher.getShopgruppeIId() != null) {
			ShopgruppeDto dto = shopgruppeFindByPrimaryKey(artikelDto_Vorher.getShopgruppeIId(), theClientDto);
			shopgruppeVorher = dto.getCNr();
		}

		String shopgruppeAktuell = "";
		if (artikelDto_Aktuell.getShopgruppeIId() != null) {
			ShopgruppeDto dto = shopgruppeFindByPrimaryKey(artikelDto_Aktuell.getShopgruppeIId(), theClientDto);
			shopgruppeAktuell = dto.getCNr();
		}
		if (!shopgruppeVorher.equals(shopgruppeAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_SHOPGRUPPE, shopgruppeVorher,
					shopgruppeAktuell, theClientDto);
		}

		String liefergruppeVorher = "";
		if (artikelDto_Vorher.getLfliefergruppeIId() != null) {
			LfliefergruppeDto dto = getLieferantServicesFac()
					.lfliefergruppeFindByPrimaryKey(artikelDto_Vorher.getLfliefergruppeIId(), theClientDto);
			liefergruppeVorher = dto.getCNr();
		}

		String liefergruppeAktuell = "";
		if (artikelDto_Aktuell.getLfliefergruppeIId() != null) {
			LfliefergruppeDto dto = getLieferantServicesFac()
					.lfliefergruppeFindByPrimaryKey(artikelDto_Aktuell.getLfliefergruppeIId(), theClientDto);
			liefergruppeAktuell = dto.getCNr();
		}
		if (!liefergruppeVorher.equals(liefergruppeAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_LIEFERGRUPPE, liefergruppeVorher,
					liefergruppeAktuell, theClientDto);
		}

		String artklaVorher = "";
		if (artikelDto_Vorher.getArtklaIId() != null) {
			ArtklaDto dto = artklaFindByPrimaryKey(artikelDto_Vorher.getArtklaIId(), theClientDto);
			artklaVorher = dto.getCNr();
		}

		String artklaAktuell = "";
		if (artikelDto_Aktuell.getArtklaIId() != null) {
			ArtklaDto dto = artklaFindByPrimaryKey(artikelDto_Aktuell.getArtklaIId(), theClientDto);
			artklaAktuell = dto.getCNr();
		}
		if (!artklaVorher.equals(artklaAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELKLASSE, artklaVorher,
					artklaAktuell, theClientDto);
		}

		String herstellerVorher = "";
		if (artikelDto_Vorher.getHerstellerIId() != null) {
			HerstellerDto dto = herstellerFindByPrimaryKey(artikelDto_Vorher.getHerstellerIId(), theClientDto);
			herstellerVorher = dto.getCNr();
		}

		String herstellerAktuell = "";
		if (artikelDto_Aktuell.getHerstellerIId() != null) {
			HerstellerDto dto = herstellerFindByPrimaryKey(artikelDto_Aktuell.getHerstellerIId(), theClientDto);
			herstellerAktuell = dto.getCNr();
		}
		if (!herstellerVorher.equals(herstellerAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_HERSTELLER, herstellerVorher,
					herstellerAktuell, theClientDto);
		}

		String freigabePersonVorher = "";
		if (artikelDto_Vorher.getPersonalIIdFreigabe() != null) {
			freigabePersonVorher = getPersonalFac()
					.personalFindByPrimaryKey(artikelDto_Vorher.getPersonalIIdFreigabe(), theClientDto)
					.getCKurzzeichen();

		}
		String freigabePersonAktuell = "";
		if (artikelDto_Aktuell.getPersonalIIdFreigabe() != null) {
			freigabePersonAktuell = getPersonalFac()
					.personalFindByPrimaryKey(artikelDto_Aktuell.getPersonalIIdFreigabe(), theClientDto)
					.getCKurzzeichen();
		}
		if (!freigabePersonVorher.equals(freigabePersonAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_FREIGABE_PERSON,
					freigabePersonVorher, freigabePersonAktuell, theClientDto);
		}

		String freigabeZeitpunktVorher = artikelDto_Vorher.getTFreigabe() + "";
		String fereigabeZeitpunktAktuell = artikelDto_Aktuell.getTFreigabe() + "";

		if (!freigabeZeitpunktVorher.equals(fereigabeZeitpunktAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_FREIGABE_ZEITPUNKT,
					freigabeZeitpunktVorher, fereigabeZeitpunktAktuell, theClientDto);
		}

		try {

			String materialVorher = "";
			if (artikelDto_Vorher.getMaterialIId() != null) {
				MaterialDto dto = getMaterialFac().materialFindByPrimaryKey(artikelDto_Vorher.getMaterialIId(),
						theClientDto);
				materialVorher = dto.getCNr();
			}

			String materialAktuell = "";
			if (artikelDto_Aktuell.getMaterialIId() != null) {
				MaterialDto dto = getMaterialFac().materialFindByPrimaryKey(artikelDto_Aktuell.getMaterialIId(),
						theClientDto);
				materialAktuell = dto.getCNr();
			}
			if (!materialVorher.equals(materialAktuell)) {
				artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_MATERIAL, materialVorher,
						materialAktuell, theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String materialgewichtVorher = artikelDto_Vorher.getFMaterialgewicht() + "";
		String materialgewichtAktuell = artikelDto_Aktuell.getFMaterialgewicht() + "";

		if (!materialgewichtVorher.equals(materialgewichtAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_MATERIALGEWICHT,
					materialgewichtVorher, materialgewichtAktuell, theClientDto);
		}

		String letzteWartungVorher = artikelDto_Vorher.getTLetztewartung() + "";
		String letzteWartungAktuell = artikelDto_Aktuell.getTLetztewartung() + "";

		if (!letzteWartungVorher.equals(letzteWartungAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_LETZTE_WARTUNG,
					letzteWartungVorher, letzteWartungAktuell, theClientDto);
		}
		// Waffenkaliber
		String waffenkaliberVorher = "";
		if (artikelDto_Vorher.getWaffenkaliberIId() != null) {
			WaffenkaliberDto dto = getArtikelServiceFac()
					.waffenkaliberFindByPrimaryKey(artikelDto_Vorher.getWaffenkaliberIId());
			waffenkaliberVorher = dto.getCNr();
		}

		String waffenkaliberAktuell = "";
		if (artikelDto_Aktuell.getWaffenkaliberIId() != null) {
			WaffenkaliberDto dto = getArtikelServiceFac()
					.waffenkaliberFindByPrimaryKey(artikelDto_Aktuell.getWaffenkaliberIId());
			waffenkaliberAktuell = dto.getCNr();
		}
		if (!waffenkaliberVorher.equals(waffenkaliberAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENKALIBER,
					waffenkaliberVorher, waffenkaliberAktuell, theClientDto);
		}

		// Waffenausfuehrung
		String waffenausfuehrungVorher = "";
		if (artikelDto_Vorher.getWaffenausfuehrungIId() != null) {
			WaffenausfuehrungDto dto = getArtikelServiceFac()
					.waffenausfuehrungFindByPrimaryKey(artikelDto_Vorher.getWaffenausfuehrungIId());
			waffenausfuehrungVorher = dto.getCNr();
		}

		String waffenausfuehrungAktuell = "";
		if (artikelDto_Aktuell.getWaffenausfuehrungIId() != null) {
			WaffenausfuehrungDto dto = getArtikelServiceFac()
					.waffenausfuehrungFindByPrimaryKey(artikelDto_Aktuell.getWaffenausfuehrungIId());
			waffenausfuehrungAktuell = dto.getCNr();
		}
		if (!waffenausfuehrungVorher.equals(waffenausfuehrungAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENAUSFUEHRUNG,
					waffenausfuehrungVorher, waffenausfuehrungAktuell, theClientDto);
		}

		// Waffentyp
		String waffentypVorher = "";
		if (artikelDto_Vorher.getWaffentypIId() != null) {
			WaffentypDto dto = getArtikelServiceFac().waffentypFindByPrimaryKey(artikelDto_Vorher.getWaffentypIId());
			waffentypVorher = dto.getCNr();
		}

		String waffentypAktuell = "";
		if (artikelDto_Aktuell.getWaffentypIId() != null) {
			WaffentypDto dto = getArtikelServiceFac().waffentypFindByPrimaryKey(artikelDto_Aktuell.getWaffentypIId());
			waffentypAktuell = dto.getCNr();
		}
		if (!waffentypVorher.equals(waffentypAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENTYP, waffentypVorher,
					waffentypAktuell, theClientDto);
		}

		// WaffentypFein
		String waffentypFeinVorher = "";
		if (artikelDto_Vorher.getWaffentypFeinIId() != null) {
			WaffentypFeinDto dto = getArtikelServiceFac()
					.waffentypFeinFindByPrimaryKey(artikelDto_Vorher.getWaffentypFeinIId());
			waffentypFeinVorher = dto.getCNr();
		}

		String waffentypFeinAktuell = "";
		if (artikelDto_Aktuell.getWaffentypFeinIId() != null) {
			WaffentypFeinDto dto = getArtikelServiceFac()
					.waffentypFeinFindByPrimaryKey(artikelDto_Aktuell.getWaffentypFeinIId());
			waffentypFeinAktuell = dto.getCNr();
		}
		if (!waffentypFeinVorher.equals(waffentypFeinAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENTYP, waffentypFeinVorher,
					waffentypFeinAktuell, theClientDto);
		}
		// Waffenzusatz
		String waffenzusatzVorher = "";
		if (artikelDto_Vorher.getWaffenzusatzIId() != null) {
			WaffenzusatzDto dto = getArtikelServiceFac()
					.waffenzusatzFindByPrimaryKey(artikelDto_Vorher.getWaffenzusatzIId());
			waffenzusatzVorher = dto.getCNr();
		}

		String waffenzusatzAktuell = "";
		if (artikelDto_Aktuell.getWaffenzusatzIId() != null) {
			WaffenzusatzDto dto = getArtikelServiceFac()
					.waffenzusatzFindByPrimaryKey(artikelDto_Aktuell.getWaffenzusatzIId());
			waffenzusatzAktuell = dto.getCNr();
		}
		if (!waffenzusatzVorher.equals(waffenzusatzAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENZUSATZ, waffenzusatzVorher,
					waffenzusatzAktuell, theClientDto);
		}

		// Waffenkategorie
		String waffenkategorieVorher = "";
		if (artikelDto_Vorher.getWaffenkategorieIId() != null) {
			WaffenkategorieDto dto = getArtikelServiceFac()
					.waffenkategorieFindByPrimaryKey(artikelDto_Vorher.getWaffenkategorieIId());
			waffenkategorieVorher = dto.getCNr();
		}

		String waffenkategorieAktuell = "";
		if (artikelDto_Aktuell.getWaffenkategorieIId() != null) {
			WaffenkategorieDto dto = getArtikelServiceFac()
					.waffenkategorieFindByPrimaryKey(artikelDto_Aktuell.getWaffenkategorieIId());
			waffenkategorieAktuell = dto.getCNr();
		}
		if (!waffenkategorieVorher.equals(waffenkategorieAktuell)) {
			artikelAenderungLoggen(artikelDto_Aktuell.getIId(), ArtikelFac.ARTIKEL_LOG_WAFFENKATEGORIE,
					waffenkategorieVorher, waffenkategorieAktuell, theClientDto);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void alleSIwerteNachtragen(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT spr FROM FLRArtikellistespr spr ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		int zaehler = 1;
		while (resultListIterator.hasNext()) {
			FLRArtikellistespr spr = (FLRArtikellistespr) resultListIterator.next();

			if (spr.getC_bez() == null && spr.getC_zbez() == null && spr.getC_zbez2() == null) {

			} else {
				getArtikelFac().siWertNachtragen(spr.getId().getArtikelliste().getI_id(), spr.getLocale_c_nr(),
						theClientDto);
			}
			zaehler++;

			System.out.println("Zeile " + zaehler + " von " + resultList.size());

		}
	}

	public void siWertNachtragen(Integer artikelIId, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(artikelIId, localeCNr));
		SiWertParser siParser = createSiWertParser(theClientDto);

		BigDecimal si = berechneSIWert(siParser, artikelspr);
		artikelspr.setCSiwert(HelperServer.getDBValueFromBigDecimal(si, 60));
		em.merge(artikelspr);
		em.flush();
	}

	public void toggleFreigabe(Integer artikelIId, String cFreigabeZuerueckgenommen, TheClientDto theClientDto) {

		// WG logging
		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIId, theClientDto);

		if (artikelDto.getTFreigabe() == null) {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE,
					theClientDto)) {
				// PJ21640
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
						theClientDto.getMandant());
				if (stklDto != null && stklDto.getTFreigabe() == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_FREIGABE_NUR_WENN_STUECKLISTE_FREIGEGEBEN,
							new Exception("FEHLER_ARTIKEL_FREIGABE_NUR_WENN_STUECKLISTE_FREIGEGEBEN"));
				}

			}

			artikelDto.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			artikelDto.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
			artikelDto.setCFreigabeZuerueckgenommen(null);
		} else {
			artikelDto.setTFreigabe(null);
			artikelDto.setPersonalIIdFreigabe(null);
			artikelDto.setCFreigabeZuerueckgenommen(cFreigabeZuerueckgenommen);
		}

		vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(artikelDto, theClientDto);

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel.getTFreigabe() == null) {
			artikel.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			artikel.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
			artikel.setCFreigabeZuerueckgenommen(null);
			artikelDto.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			artikelDto.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
			artikelDto.setCFreigabeZuerueckgenommen(null);
		} else {
			artikel.setTFreigabe(null);
			artikel.setPersonalIIdFreigabe(null);
			artikel.setCFreigabeZuerueckgenommen(cFreigabeZuerueckgenommen);
			artikelDto.setTFreigabe(null);
			artikelDto.setPersonalIIdFreigabe(null);
			artikelDto.setCFreigabeZuerueckgenommen(cFreigabeZuerueckgenommen);
		}

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
	 * @param artikelDto   ArtikelDto
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void updateArtikel(ArtikelDto artikelDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (artikelDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelDto == null"));
		}
		if (artikelDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelDto.getIId()==null"));
		}
		if (artikelDto.getBVersteckt() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelDto.getBVersteckt() == null"));
		}

		if (artikelDto.getCNr() == null || artikelDto.getBAntistatic() == null
				|| artikelDto.getBChargennrtragend() == null || artikelDto.getBRabattierbar() == null
				|| artikelDto.getBSeriennrtragend() == null || artikelDto.getBLagerbewirtschaftet() == null
				|| artikelDto.getBVerleih() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artikelDto.getCNr() == null || artikelDto.getBAntistatic() == null || artikelDto.getBChargennrtragend() == null || artikelDto.getBRabattierbar() == null || artikelDto.getBSeriennrtragend() == null || artikelDto.getBLagerbewirtschaftet() == null || artikelDto.getBVerleih() == null"));
		}

		vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(artikelDto, theClientDto);

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			artikelDto.setMandantCNr(getSystemFac().getHauptmandant());
		} else {
			artikelDto.setMandantCNr(theClientDto.getMandant());
		}

		// Wenn Artikel Handartikel ist, dann darf cNr null sein
		if (!artikelDto.getCNr().startsWith("~")) {
			pruefeArtikelnummer(artikelDto.getCNr(), theClientDto);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SI_WERT, theClientDto)
					&& artikelDto.getArtikelsprDto() != null) {
				SiWertParser siParser = createSiWertParser(theClientDto);
				artikelDto.getArtikelsprDto().setNSiwert(berechneSIWert(siParser, artikelDto.getArtikelsprDto()));
			}
		}

		if (artikelDto.getEinheitCNrBestellung() == null) {
			artikelDto.setNUmrechnungsfaktor(null);
		} else {
			// PJ18425 Wenn Bestellmengeneinheit vorhanden, dann darf im
			// Artikellieferant keine VPE (Bestelleinheit) definiert sein
			Query query = em.createNamedQuery("ArtikellieferantfindByArtikelIId");
			query.setParameter(1, artikelDto.getIId());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Artikellieferant al = (Artikellieferant) it.next();
				if (al.getEinheitCNrVpe() != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN,
							new Exception("FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN"));

				}

			}

		}

		if (artikelDto.getNUmrechnungsfaktor() == null) {
			artikelDto.setEinheitCNrBestellung(null);
		}

		if (artikelDto.getArtikelIIdErsatz() != null) {
			pruefeObEndlosschleifeErsatzartikel(artikelDto.getIId(), artikelDto.getArtikelIIdErsatz());
		}

		if (Helper.short2boolean(artikelDto.getBVerleih()) == true) {
			artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
		}

		if (Helper.short2boolean(artikelDto.getBKalkulatorisch()) == true) {
			artikelDto.setArtikelIIdZugehoerig(null);
		}

		Integer iId = artikelDto.getIId();

		try {
			Artikel artikel = em.find(Artikel.class, iId);

			try {
				Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
				query.setParameter(1, artikelDto.getCNr());
				query.setParameter(2, artikelDto.getMandantCNr());
				Integer iIdVorhanden = ((Artikel) query.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKEL.CNR"));
				}

			} catch (NoResultException ex) {

			}

			if (artikelDto.getTLetztewartung() != null) {

				if (artikel.getTLetztewartung() == null) {
					artikelDto.setPersonalIIdLetztewartung(theClientDto.getIDPersonal());
				} else {
					if (!artikelDto.getTLetztewartung().equals(artikel.getTLetztewartung())) {
						artikelDto.setPersonalIIdLetztewartung(theClientDto.getIDPersonal());
					}
				}

			} else {
				artikelDto.setPersonalIIdLetztewartung(null);
			}

			// PJ 16901
			boolean bArtGruGeaendert = false;
			if (artikelDto.getArtgruIId() == null && artikel.getArtgruIId() != null) {
				bArtGruGeaendert = true;
			}
			if (artikelDto.getArtgruIId() != null && artikel.getArtgruIId() == null) {
				bArtGruGeaendert = true;
			}

			if (artikelDto.getArtgruIId() != null && artikel.getArtgruIId() != null
					&& !artikelDto.getArtgruIId().equals(artikel.getArtgruIId())) {
				bArtGruGeaendert = true;
			}

			if (bArtGruGeaendert == true) {
				PaneldatenDto[] eigs = getPanelFac().paneldatenFindByPanelCNrCKey(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
						artikelDto.getIId() + "");
				for (int i = 0; i < eigs.length; i++) {
					getPanelFac().removePaneldaten(eigs[i].getIId());
				}
			}

			if (artikelDto.getBBevorzugt() != artikel.getBBevorzugt()
					&& Helper.short2boolean(artikelDto.getBBevorzugt())) {
				// SP21339

				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
				int iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

				String artikelnummerOhneHst = artikelDto.getCNr().substring(0, iLaengeArtikelnummer);

				Session session = FLRSessionFactory.getFactory().openSession();

				String queryStringIN = "(SELECT a.i_id FROM FLRArtikelliste AS a WHERE a.mandant_c_nr='"
						+ theClientDto.getMandant() + "' AND a.c_nr LIKE '" + artikelnummerOhneHst + "%')";

				String sQuery = "SELECT s FROM FLRKundesoko AS s WHERE s.b_wirkt_nicht_fuer_preisfindung=1 AND s.t_preisgueltigab <='"
						+ Helper.formatDateWithSlashes(getDate())
						+ "' AND (s.t_preisgueltigbis IS NULL OR s.t_preisgueltigbis > '"
						+ Helper.formatDateWithSlashes(getDate()) + "') AND s.artikel_i_id IN " + queryStringIN;

				org.hibernate.Query query = session.createQuery(sQuery);
				List<?> resultList = query.list();
				Iterator it = resultList.iterator();

				HashMap<Integer, ArrayList<Integer>> hmKunde = new HashMap<Integer, ArrayList<Integer>>();

				while (it.hasNext()) {
					FLRKundesoko s = (FLRKundesoko) it.next();

					ArrayList<Integer> artikelIIds = null;
					if (hmKunde.containsKey(s.getKunde_i_id())) {
						artikelIIds = hmKunde.get(s.getKunde_i_id());
					} else {
						artikelIIds = new ArrayList<Integer>();
					}

					artikelIIds.add(s.getArtikel_i_id());

					if (artikelIIds.size() > 1) {
						// EXCEPTION
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEVORZUGTER_ARTIKEL_NICHT_MOEGLICH_MEHRERE_SOKOS,
								new Exception("FEHLER_BEVORZUGTER_ARTIKEL_NICHT_MOEGLICH_MEHRERE_SOKOS"));
					}

					hmKunde.put(s.getKunde_i_id(), artikelIIds);

					Kundesoko kundesoko = em.find(Kundesoko.class, s.getI_id());
					kundesoko.setArtikelIId(artikelDto.getIId());
					em.merge(kundesoko);
					em.flush();

				}

			}

			// PJ 16811
			if (artikelDto.getCVerpackungseannr() != null && artikelDto.getCVerkaufseannr() != null
					&& artikelDto.getCVerpackungseannr().equals(artikelDto.getCVerkaufseannr())) {
				ArrayList al = new ArrayList();

				al.add(artikelDto.formatArtikelbezeichnung());
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN, al,
						new Exception("FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
			}

			if (artikelDto.getCVerpackungseannr() != null) {

				try {
					Query query = em.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerpackungseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0)).getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(iIdVorhanden,
										theClientDto);
								al.add(artikelDtoVorhanden.formatArtikelbezeichnung());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN, al,
										new Exception("FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
				try {
					Query query = em.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerpackungseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0)).getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(iIdVorhanden,
										theClientDto);
								al.add(artikelDtoVorhanden.formatArtikelbezeichnung());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN, al,
										new Exception("FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
			}
			if (artikelDto.getCVerkaufseannr() != null) {

				try {
					Query query = em.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerkaufseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0)).getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(iIdVorhanden,
										theClientDto);
								al.add(artikelDtoVorhanden.formatArtikelbezeichnung());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN, al,
										new Exception("FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN"));
							}
						}
					}

				} catch (NoResultException ex) {

				}
				try {
					Query query = em.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr");
					query.setParameter(1, artikelDto.getCVerkaufseannr());
					query.setParameter(2, artikelDto.getMandantCNr());
					List l = query.getResultList();
					if (l.size() > 0) {

						for (int i = 0; i < l.size(); i++) {
							Integer iIdVorhanden = ((Artikel) l.get(0)).getIId();
							if (iId.equals(iIdVorhanden) == false) {

								ArrayList al = new ArrayList();
								ArtikelDto artikelDtoVorhanden = artikelFindByPrimaryKeySmall(iIdVorhanden,
										theClientDto);
								al.add(artikelDtoVorhanden.formatArtikelbezeichnung());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN, al,
										new Exception("FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN"));
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
							|| Helper.short2boolean(artikel.getBSeriennrtragend())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.ARTIKEL_LAGERBEWIRTSCHAFTET_KANN_NUR_ABGESCHALTET_WERDEN_WENN_NICHT_CHNR_SNR_BEHAFTET,
								new Exception(
										"ARTIKEL_LAGERBEWIRTSCHAFTET_KANN_NUR_ABGESCHALTET_WERDEN_WENN_NICHT_CHNR_SNR_BEHAFTET"));
					} else {

						myLogger.warn("Eigenschaft 'Lagerbewirtschaftet' des Artikels " + artikelDto.getCNr()
								+ " abgeschaltet durch Personal-ID " + theClientDto.getIDPersonal());

						artikelDto.setBLagerbewirtschaftet(Helper.boolean2Short(false));

						Integer lagerIIdKeinLager = getLagerFac()
								.lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER, theClientDto.getMandant())
								.getIId();

						Artikellager artikellager = em.find(Artikellager.class,
								new ArtikellagerPK(artikel.getIId(), lagerIIdKeinLager));
						if (artikellager == null) {
							artikellager = new Artikellager(artikel.getIId(), lagerIIdKeinLager,
									artikelDto.getMandantCNr());
							em.merge(artikellager);
							em.flush();
						}

						// Zuerst fuer den Artikel alle LAGER_I_ID in
						// WW_LAGERBEWEGUNG auf KEIN_LAGER aendern
						Session session = FLRSessionFactory.getFactory().openSession();
						String hqlUpdate = "update FLRLagerbewegung l SET l.lager_i_id=" + lagerIIdKeinLager
								+ " WHERE l.artikel_i_id=" + artikel.getIId();
						session.createQuery(hqlUpdate).executeUpdate();
						session.close();

						// In WW_ARTIKELLAGER alle Laeger ausser KEIN_LAGER
						// entfernen und Gestpreis neu kalkulieren
						BigDecimal bdPreisNeu = getLagerReportFac().recalcGestehungspreisKomplett(artikel.getIId(),
								false);

						Query query = em.createNamedQuery("ArtikellagerfindByArtikelIId");
						query.setParameter(1, artikel.getIId());
						Collection<?> cl = query.getResultList();
						Iterator it = cl.iterator();
						while (it.hasNext()) {
							Artikellager al = (Artikellager) it.next();
							if (!al.getPk().getLagerIId().equals(lagerIIdKeinLager)) {
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
					LagerbewegungDto[] dtos = getLagerFac().lagerbewegungFindByArtikelIId(artikelDto.getIId());
					if (dtos != null & dtos.length > 0) {
						throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH,
								new Exception("ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH"));

					}
				}

			}
			// Von nicht Chargennummernbehaftet auf Chargennummernbehaftet kann
			// man nur wechseln, wenn Lagerstaende 0 sind
			if (Helper.short2boolean(artikel.getBChargennrtragend()) != Helper
					.short2boolean(artikelDto.getBChargennrtragend())) {
				if (Helper.short2boolean(artikelDto.getBChargennrtragend()) == true) {
					getLagerFac().aendereEigenschaftChargengefuehrt(artikelDto.getIId(), true, theClientDto);
				} else {
					getLagerFac().aendereEigenschaftChargengefuehrt(artikelDto.getIId(), false, theClientDto);
				}
			}
			// Von nicht Seriennummernbehaftet auf Seriennummernbehaftet kann
			// man nur wechseln, wenn Lagerstaende 0 sind
			if (Helper.short2boolean(artikel.getBSeriennrtragend()) != Helper
					.short2boolean(artikelDto.getBSeriennrtragend())) {
				// CK: PJ 13906 Wenn Buchungen auf dem Artikel sind, dann aknn
				// man nicht mehr wechseln
				LagerbewegungDto[] dtos = getLagerFac().lagerbewegungFindByArtikelIId(artikelDto.getIId());
				if (dtos != null & dtos.length > 0) {
					{

						// PJ21629
						for (int i = 0; i < dtos.length; i++) {

							LagerbewegungDto lbewDto = dtos[i];

							if (lbewDto.getNMenge().doubleValue() > 0
									&& Helper.short2boolean(lbewDto.getBHistorie()) == false) {

								BelegInfos bi = getLagerFac().getBelegInfos(lbewDto.getCBelegartnr(),
										lbewDto.getIBelegartid(), lbewDto.getIBelegartpositionid(), theClientDto);
								ArrayList al = new ArrayList();
								al.add(bi);
								// Dann ist noch eine gueltige Lagerbewegung vorhanden und es kann nicht
								// veraendert werden
								throw new EJBExceptionLP(
										EJBExceptionLP.ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH, al,
										new Exception("ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH"));
							} else {
								Lagerbewegung toRemove = em.find(Lagerbewegung.class, lbewDto.getIId());
								em.remove(toRemove);
							}
						}
					}
				}
			}

			if (artikelDto.getBSeriennrtragend().intValue() == 1 || artikelDto.getBChargennrtragend().intValue() == 1) {
				// Wenn Artikel Seriennummern/Chargennummerntragend ist, dann
				// muss er Lagerbewirtschaftet sein
				artikelDto.setBLagerbewirtschaftet(new Short((short) 1));
			}

			if (artikelDto.getArtikelIIdZugehoerig() != null
					&& pruefeObArtikelInArtikelSchonVorhanden(artikelDto.getIId(), artikelDto.getArtikelIIdZugehoerig(),
							theClientDto) == true) {

				throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_DEADLOCK, new Exception("ARTIKEL_DEADLOCK"));

			}

			setArtikelFromArtikelDto(artikel, artikelDto);
			artikel.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			artikel.setPersonalIIdAendern(theClientDto.getIDPersonal());

			if (artikelDto.getArtikelsprDto() != null) {

				// PJ19566
				String locale = artikelDto.getArtikelsprDto().getLocaleCNr();
				if (locale == null) {
					locale = theClientDto.getLocUiAsString();
				}

				// try {
				Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId, locale));

				if (artikelspr != null) {
					artikelDto.getArtikelsprDto().setPersonalIIdAendern(theClientDto.getIDPersonal());
					artikelDto.getArtikelsprDto().setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
					setArtikelsprFromArtikelsprDto(artikelspr, artikelDto.getArtikelsprDto(),
							getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant()));
				} else {
					artikelspr = new Artikelspr(iId, locale, theClientDto.getIDPersonal());
					em.persist(artikelspr);
					em.flush();
					artikelDto.getArtikelsprDto().setPersonalIIdAendern(theClientDto.getIDPersonal());
					artikelDto.getArtikelsprDto().setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

					setArtikelsprFromArtikelsprDto(artikelspr, artikelDto.getArtikelsprDto(),
							getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant()));
				}

			}
			if (artikelDto.getGeometrieDto() != null) {
				// try {
				Geometrie geometrie = em.find(Geometrie.class, iId);
				if (geometrie == null) {
					geometrie = new Geometrie(iId);
					em.persist(geometrie);
					em.flush();
					setGeometrieFromGeometrieDto(geometrie, artikelDto.getGeometrieDto());
				}
				setGeometrieFromGeometrieDto(geometrie, artikelDto.getGeometrieDto());
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
					setSollverkaufFromSollverkaufDto(sollverkauf, artikelDto.getSollverkaufDto());
				}
				setSollverkaufFromSollverkaufDto(sollverkauf, artikelDto.getSollverkaufDto());
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
					setMontageFromMontageDto(montage, artikelDto.getMontageDto());
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
					setVerpackungFromVerpackungDto(verpackung, artikelDto.getVerpackungDto());
				}
				setVerpackungFromVerpackungDto(verpackung, artikelDto.getVerpackungDto());
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

	public void updateArtikelspr(ArtikelsprDto sprDto, TheClientDto theClientDto) {
		Artikelspr artikelspr = em.find(Artikelspr.class,
				new ArtikelsprPK(sprDto.getArtikelIId(), sprDto.getLocaleCNr()));
		sprDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		sprDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setArtikelsprFromArtikelsprDto(artikelspr, sprDto,
				getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant()));
	}

	/**
	 * Gibt den Artikel samt vorhandenen Satellitentabellen in der User- Sprache
	 * zur&uuml;ck, der dem Prim&auml;rschl&uuml;ssel (iId) entspricht.
	 * 
	 * @param iId          Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return ArtikelDto
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArtikelDto artikelFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		ArtikelDto artikelDto = artikelFindByPrimaryKeyOhneExc(iId, theClientDto);
		Validator.entityFound(artikelDto, iId);

		return artikelDto;

		/*
		 * if(artikelDto == null) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		 * "Fehler bei ArtikelFindByPrimaryKey. Es gibt keine iid " + iId);
		 * 
		 * } if (iId == null) { throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new
		 * Exception("iId == null")); }
		 * 
		 * // try { Artikel artikel = em.find(Artikel.class, iId); if (artikel == null)
		 * { throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		 * "Fehler bei ArtikelFindByPrimaryKey. Es gibt keine iid " + iId); } ArtikelDto
		 * artikelDto = assembleArtikelDto(artikel); ArtikelsprDto artikelsprDto = null;
		 * // try { Artikelspr artikelspr = em.find(Artikelspr.class, new
		 * ArtikelsprPK(iId, theClientDto.getLocUiAsString())); if (artikelspr != null)
		 * { artikelsprDto = assembleArtikelsprDto(artikelspr); } // } // catch
		 * (FinderException ex) { // } if (artikelsprDto == null) { // try { artikelspr
		 * = em.find(Artikelspr.class, new ArtikelsprPK(iId,
		 * theClientDto.getLocKonzernAsString())); if (artikelspr != null) {
		 * artikelsprDto = assembleArtikelsprDto(artikelspr); } // } // catch
		 * (FinderException ex) { // Nothing here // } }
		 * 
		 * artikelDto.setArtikelsprDto(artikelsprDto);
		 * 
		 * // try { Geometrie geometrie = em.find(Geometrie.class, iId); if (geometrie
		 * != null) { GeometrieDto geometrieDto = assembleGeometrieDto(geometrie);
		 * artikelDto.setGeometrieDto(geometrieDto); } // catch (FinderException ex) {
		 * // Nothing here // } // try { Sollverkauf sollverkauf =
		 * em.find(Sollverkauf.class, iId); if (sollverkauf != null) { SollverkaufDto
		 * sollverkaufDto = assembleSollverkaufDto(sollverkauf);
		 * artikelDto.setSollverkaufDto(sollverkaufDto); } // } // catch
		 * (FinderException ex) { // Nothing here // } // try { Verpackung verpackung =
		 * em.find(Verpackung.class, iId); if (verpackung != null) { VerpackungDto
		 * verpackungDto = assembleVerpackungDto(verpackung);
		 * artikelDto.setVerpackungDto(verpackungDto); } // catch (FinderException ex) {
		 * // Nothing here // } // try { Montage montage = em.find(Montage.class, iId);
		 * if (montage != null) { MontageDto montageDto = assembleMontageDto(montage);
		 * artikelDto.setMontageDto(montageDto); } // catch (FinderException ex) { //
		 * Nothing here // }
		 * 
		 * return artikelDto; // } // catch (FinderException e) { // throw new
		 * EJBExceptionLP(EJBExceptionLP. // FEHLER_BEI_FINDBYPRIMARYKEY, // e); // }
		 * 
		 */
	}

	/**
	 * Gibt den Artikel samt vorhandenen Satellitentabellen in der User- Sprache
	 * zur&uuml;ck, der dem Prim&auml;rschl&uuml;ssel (iId) entspricht.
	 * 
	 * @param iId          Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return ArtikelDto
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArtikelDto artikelFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(iId, "iId");

		Artikel artikel = em.find(Artikel.class, iId);
		if (artikel == null)
			return null;

		ArtikelDto artikelDto = assembleArtikelDto(artikel);
		ArtikelsprDto artikelsprDto = null;

		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId, theClientDto.getLocUiAsString()));
		if (artikelspr != null) {
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		}

		if (artikelsprDto == null) {
			artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId, theClientDto.getLocKonzernAsString()));
			if (artikelspr != null) {
				artikelsprDto = assembleArtikelsprDto(artikelspr);
			}
		}

		artikelDto.setArtikelsprDto(artikelsprDto);

		Geometrie geometrie = em.find(Geometrie.class, iId);
		if (geometrie != null) {
			GeometrieDto geometrieDto = assembleGeometrieDto(geometrie);
			artikelDto.setGeometrieDto(geometrieDto);
		}

		Sollverkauf sollverkauf = em.find(Sollverkauf.class, iId);
		if (sollverkauf != null) {
			SollverkaufDto sollverkaufDto = assembleSollverkaufDto(sollverkauf);
			artikelDto.setSollverkaufDto(sollverkaufDto);
		}

		Verpackung verpackung = em.find(Verpackung.class, iId);
		if (verpackung != null) {
			VerpackungDto verpackungDto = assembleVerpackungDto(verpackung);
			artikelDto.setVerpackungDto(verpackungDto);
		}

		Montage montage = em.find(Montage.class, iId);
		if (montage != null) {
			MontageDto montageDto = assembleMontageDto(montage);
			artikelDto.setMontageDto(montageDto);
		}

		return artikelDto;
	}

	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer iId, TheClientDto theClientDto) {

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

	public ArrayList<Integer> getAlleVorgaengerArtikel(Integer artikelIId) {
		ArrayList<Integer> artikel = new ArrayList<Integer>();

		Query query = em.createNamedQuery("ArtikelfindByArtikelIIdErsatz");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();

		while (it.hasNext()) {

			Artikel a = (Artikel) it.next();
			artikel.add(a.getIId());

			artikel.addAll(getAlleVorgaengerArtikel(a.getIId()));

		}

		return artikel;
	}

	public ArtikelDto artikelFindByPrimaryKeySmall(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Artikel artikel = em.find(Artikel.class, iId);
		if (artikel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelFindByPrimaryKEySmall. Es gibt keinen Artikel mit der iid " + iId);
		}
		ArtikelDto artikelDto = assembleArtikelDto(artikel);
		ArtikelsprDto artikelsprDto = null;
		// try {
		Artikelspr artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId, theClientDto.getLocUiAsString()));
		if (artikelspr != null) {
			artikelsprDto = assembleArtikelsprDto(artikelspr);
		}
		// }
		// catch (FinderException ex) {
		// }
		if (artikelsprDto == null) {
			// try {
			artikelspr = em.find(Artikelspr.class, new ArtikelsprPK(iId, theClientDto.getLocKonzernAsString()));
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

	private boolean pruefeObArtikelInArtikelSchonVorhanden(Integer artikelIId_Wurzel, Integer artikelIId_ZuSuchende,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// try {
		Artikel artikel = em.find(Artikel.class, artikelIId_ZuSuchende);
		if (artikel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei priefeObArtikelINArtikelSChonVorhanden. Es gibt keine Artikel mit iid "
							+ artikelIId_ZuSuchende);

		}
		if (artikel.getArtikelIIdZugehoerig() == null) {
			return false;
		}

		if (artikelIId_Wurzel.equals(artikel.getArtikelIIdZugehoerig())) {
			return true;
		} else {
			return pruefeObArtikelInArtikelSchonVorhanden(artikelIId_Wurzel, artikel.getArtikelIIdZugehoerig(),
					theClientDto);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);

		// }
	}

	public ArtikelsprDto getDefaultArtikelbezeichnungen(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
		}
		ArtikelsprDto artikelsprDto = null;
		// try {
		ArtikelsprPK pk = new ArtikelsprPK(artikelIId, theClientDto.getLocKonzernAsString());
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
	 * @param cNr          String
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @return ArtikelDto
	 */
	public ArtikelDto artikelFindByCNr(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			ArtikelDto artikelDto = artikelFindByCNrOhneExc(cNr, theClientDto);
			if (null == artikelDto)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, new NoResultException());
			return artikelDto;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public ArtikelDto artikelFindByCNrMandantCNrOhneExc(String cNr, String mandantCnr) {

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

	public ArtikelDto[] artikelfindByCReferenznrMandantCNrOhneExc(String cNr, String mandantCnr) {

		Query query = em.createNamedQuery("ArtikelfindByCReferenznrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCnr);
		try {
			Collection c = query.getResultList();
			return assembleArtikelDtos(c);
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
	public ArtikelDto artikelFindByCNrOhneExc(String cNr, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
		query.setParameter(1, cNr);

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
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

	public ArtikelDto[] artikelFindByCNrOhneExcAlleMandanten(String cNr) {
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
		 * + " WHERE A.ARTIKELART_C_NR='Artikel' AND levenshtein(S.C_BEZ, '" + bauteil +
		 * "')<10 LIMIT 20;";
		 */

		String sQuery = "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
				+ " WHERE ARTIKELART_C_NR='Artikel' " + " AND A.B_VERSTECKT = 0 " + " AND ("
				+ " LOWER(TRIM(S.C_BEZ)) LIKE '%" + bauteil.toLowerCase() + "%'"
				+ " OR dmetaphone(trim(S.C_BEZ)) = dmetaphone('" + bauteil + "')"
				+ " OR dmetaphone_alt(trim(S.C_BEZ)) = dmetaphone_alt('" + bauteil + "')"
				+ " OR soundex(trim(S.C_BEZ)) = soundex('" + bauteil + "')" + " OR lower(trim(A.C_NR)) like '%"
				+ bauteil.toLowerCase() + "%'" + " ) " + " ORDER BY levenshtein(trim(S.C_BEZ), '" + bauteil
				+ "'), levenshtein(trim(A.C_NR), '" + bauteil + "')" + " LIMIT 20;";

		/*
		 * String sQuery =
		 * "SELECT I_ID FROM WW_ARTIKEL A INNER JOIN WW_ARTIKELSPR S ON S.ARTIKEL_I_ID=A.I_ID "
		 * + " WHERE A.ARTIKELART_C_NR='Artikel' AND soundex(S.C_BEZ) LIKE soundex('" +
		 * bauteil + "') LIMIT 20;";
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
					+ " WHERE ARTIKELART_C_NR='Artikel' AND dmetaphone(trim(A.C_NR)) = dmetaphone('" + bauteil
					+ "') AND soundex(trim(A.C_NR)) = soundex('" + bauteil + "') "
					+ " ORDER BY levenshtein(trim(A.C_NR), '" + bauteil + "') LIMIT 20;";
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
	public String pruefeCSVImport(ArtikelImportDto[] daten, boolean bestehendeArtikelUeberschreiben,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		boolean bArtikelfreigabe = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE, theClientDto);

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
					fehler += "Feld Artikelnummer enth\u00E4lt ung\u00FCltige Zeichen, Zeile:" + i + "; ";
				}
			}

			if (bestehendeArtikelUeberschreiben && bArtikelfreigabe) {

				ArtikelDto artikelDto = artikelFindByCNrMandantCNrOhneExc(zeile.getArtikelnummer(),
						theClientDto.getMandant());
				// PJ21596
				if (artikelDto != null && artikelDto.getTFreigabe() != null) {
					fehler += "Der Artikel '" + artikelDto.getCNr()
							+ "' ist freigegeben und darf nicht mehr veraendert werden. Zeile " + i + "; ";

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
				if (zeile.getArtikelart().equals(ArtikelFac.ARTIKELART_ARTIKEL.trim())) {

				} else if (zeile.getArtikelart().equals(ArtikelFac.ARTIKELART_HANDARTIKEL.trim())) {

				} else if (zeile.getArtikelart().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT.trim())) {

				} else {
					fehler += " Artikelart '" + zeile.getArtikelart() + "' nicht vorhanden, Zeile:" + i + "; ";
				}

			}

			if (zeile.getEinheit().length() > 0) {
				try {
					getSystemFac().einheitFindByPrimaryKey(zeile.getEinheit(), theClientDto);
				} catch (Throwable e) {
					fehler += " Einheit '" + zeile.getEinheit() + "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getArtikelgruppe().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelgruppe bereits
					// vorhanden.
					Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelgruppe());
					query.setParameter(2, theClientDto.getMandant());
					Artgru doppelt = (Artgru) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Artikelgruppe  '" + zeile.getArtikelgruppe() + "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getArtikelklasse().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
					query.setParameter(1, zeile.getArtikelklasse());
					query.setParameter(2, theClientDto.getMandant());
					Artkla doppelt = (Artkla) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Artikelklasse  '" + zeile.getArtikelklasse() + "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getMwstsatz().length() > 0) {
				try {
					// duplicateunique: Pruefung: Artikelklasse bereits
					// vorhanden.
					Query query = em.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
					query.setParameter(1, theClientDto.getMandant());
					query.setParameter(2, zeile.getMwstsatz());
					Mwstsatzbez doppelt = (Mwstsatzbez) query.getSingleResult();
				} catch (NoResultException ex) {
					fehler += " Mwstsatz  '" + zeile.getMwstsatz() + "' nicht vorhanden, Zeile:" + i + "; ";
				}
			}

			if (zeile.getVkpreisbasis().length() > 0) {

				try {
					BigDecimal bdvkpreisbasis = new BigDecimal(zeile.getVkpreisbasis());
				} catch (NumberFormatException e) {
					fehler += " VK-Preisbasis ist kein g\u00FCltiger Wert  '" + zeile.getVkpreisbasis() + "'  Zeile:"
							+ i + "; ";
				}

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

				try {
					java.sql.Date date = new java.sql.Date(format.parse(zeile.getVkpreisbasisgueltigab()).getTime());
				} catch (ParseException e) {
					fehler += " VK-PreisbasisG\u00FCltigab ist kein g\u00FCltiges Datum  '"
							+ zeile.getVkpreisbasisgueltigab() + "'  Zeile:" + i + "; ";
				}

				if (zeile.getFixpreispreisliste1().length() > 0 || zeile.getRabattsatzpreisliste1().length() > 0) {

					if (zeile.getFixpreispreisliste1().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste1());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste1 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste1() + "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste1());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste1 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste1() + "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(zeile.getGueltigabpreisliste1()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste1 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste1() + "'  Zeile:" + i + "; ";
					}

				}

				// Preisliste2

				if (zeile.getFixpreispreisliste2().length() > 0 || zeile.getRabattsatzpreisliste2().length() > 0) {

					if (zeile.getFixpreispreisliste2().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste2());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste2 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste2() + "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste2());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste2 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste2() + "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(zeile.getGueltigabpreisliste2()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste2 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste2() + "'  Zeile:" + i + "; ";
					}

				}

				// Preisliste3

				if (zeile.getFixpreispreisliste3().length() > 0 || zeile.getRabattsatzpreisliste3().length() > 0) {

					if (zeile.getFixpreispreisliste3().length() > 0) {
						try {
							BigDecimal bdFixpreis = new BigDecimal(zeile.getFixpreispreisliste3());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste3 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste3() + "'  Zeile:" + i + "; ";
						}
					} else {
						try {
							BigDecimal bdRabattsatz = new BigDecimal(zeile.getRabattsatzpreisliste3());
						} catch (NumberFormatException e) {
							fehler += " Fixpreispreisliste3 ist kein g\u00FCltiger Wert  '"
									+ zeile.getFixpreispreisliste3() + "'  Zeile:" + i + "; ";
						}
					}

					try {
						java.sql.Date date = new java.sql.Date(format.parse(zeile.getGueltigabpreisliste3()).getTime());
					} catch (ParseException e) {
						fehler += " VK-Gueltigabpreisliste3 ist kein g\u00FCltiges Datum  '"
								+ zeile.getGueltigabpreisliste3() + "'  Zeile:" + i + "; ";
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
					fehler += " Seriennumernbehaftet hat keinen g\u00FCltigen Wert  '" + zeile.getSnrbehaftet()
							+ "'  Zeile:" + i + "; ";
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
								+ zeile.getSnrbehaftet() + "'  Zeile:" + i + "; ";
					}
				} catch (NumberFormatException e) {
					fehler += " Seriennumernbehaftet hat keinen g\u00FCltigen Wert  '" + zeile.getChargenbehaftet()
							+ "'  Zeile:" + i + "; ";
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
		artikel.setFVertreterprovisionmax(artikelDto.getFVertreterprovisionmax());
		artikel.setFMinutenfaktor1(artikelDto.getFMinutenfaktor1());
		artikel.setFMinutenfaktor2(artikelDto.getFMinutenfaktor2());
		artikel.setFMindestdeckungsbeitrag(artikelDto.getFMindestdeckungsbeitrag());
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
		artikel.setFFertigungssatzgroesse(artikelDto.getFFertigungssatzgroesse());
		artikel.setIWartungsintervall(artikelDto.getIWartungsintervall());
		artikel.setISofortverbrauch(artikelDto.getISofortverbrauch());
		artikel.setCIndex(artikelDto.getCIndex());
		artikel.setCRevision(artikelDto.getCRevision());
		artikel.setFStromverbrauchmax(artikelDto.getFStromverbrauchmax());
		artikel.setFStromverbrauchtyp(artikelDto.getFStromverbrauchtyp());
		artikel.setFDetailprozentmindeststand(artikelDto.getFDetailprozentmindeststand());
		artikel.setLfliefergruppeIId(artikelDto.getLfliefergruppeIId());
		artikel.setbNurzurinfo(artikelDto.getbNurzurinfo());
		artikel.setbReinemannzeit(artikelDto.getbReinemannzeit());
		artikel.setShopgruppeIId(artikelDto.getShopgruppeIId());
		artikel.setbBestellmengeneinheitInvers(artikelDto.getbBestellmengeneinheitInvers());
		artikel.setBWerbeabgabepflichtig(artikelDto.getBWerbeabgabepflichtig());
		artikel.setTLetztewartung(artikelDto.getTLetztewartung());
		artikel.setPersonalIIdLetztewartung(artikelDto.getPersonalIIdLetztewartung());
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
		artikel.setBRahmenartikel(artikelDto.getBRahmenartikel());
		artikel.setNVerschnittmenge(artikelDto.getNVerschnittmenge());
		artikel.setVerpackungsmittelIId(artikelDto.getVerpackungsmittelIId());
		artikel.setNVerpackungsmittelmenge(artikelDto.getNVerpackungsmittelmenge());
		artikel.setNMindestverkaufsmenge(artikelDto.getNMindestverkaufsmenge());

		artikel.setFMultiplikatorZugehoerigerartikel(artikelDto.getFMultiplikatorZugehoerigerartikel());
		artikel.setBAzinabnachkalk(artikelDto.getBAzinabnachkalk());
		artikel.setBKommissionieren(artikelDto.getBKommissionieren());
		artikel.setBKeineLagerzubuchung(artikelDto.getBKeineLagerzubuchung());
		artikel.setIExternerArbeitsgang(artikelDto.getIExternerArbeitsgang());
		artikel.setILaengemaxSnrchnr(artikelDto.getILaengemaxSnrchnr());
		artikel.setILaengeminSnrchnr(artikelDto.getILaengeminSnrchnr());

		artikel.setBWepinfoAnAnforderer(artikelDto.getBWepinfoAnAnforderer());
		artikel.setBVkpreispflichtig(artikelDto.getBVkpreispflichtig());
		artikel.setIPassiveReisezeit(artikelDto.getIPassiveReisezeit());
		artikel.setBSummeInBestellung(artikelDto.getBSummeInBestellung());
		artikel.setBBevorzugt(artikelDto.getBBevorzugt());
		artikel.setBMultiplikatorInvers(artikelDto.getBMultiplikatorInvers());
		artikel.setBMultiplikatorAufrunden(artikelDto.getBMultiplikatorAufrunden());
		artikel.setBBewilligungspflichtig(artikelDto.getBBewilligungspflichtig());
		artikel.setBMeldepflichtig(artikelDto.getBMeldepflichtig());

		artikel.setWaffenausfuehrungIId(artikelDto.getWaffenausfuehrungIId());
		artikel.setWaffentypIId(artikelDto.getWaffentypIId());
		artikel.setWaffentypFeinIId(artikelDto.getWaffentypFeinIId());
		artikel.setWaffenkategorieIId(artikelDto.getWaffenkategorieIId());
		artikel.setWaffenzusatzIId(artikelDto.getWaffenzusatzIId());
		artikel.setWaffenkaliberIId(artikelDto.getWaffenkaliberIId());
		artikel.setNPreisZugehoerigerartikel(artikelDto.getNPreisZugehoerigerartikel());
		artikel.setFMaxfertigungssatzgroesse(artikelDto.getFMaxfertigungssatzgroesse());

		artikel.setLaseroberflaecheIId(artikelDto.getLaseroberflaecheIId());

		em.merge(artikel);
		em.flush();
	}

	private ArtikelDto assembleArtikelDto(Artikel artikel) {
		return ArtikelDtoAssembler.createDto(artikel);
	}

	private List<ArtikelDto> assembleArtikelDtosAsList(Collection<?> artikels) {
		List<ArtikelDto> list = new ArrayList<ArtikelDto>();
		if (artikels != null) {
			Iterator<?> iterator = artikels.iterator();
			while (iterator.hasNext()) {
				Artikel artikel = (Artikel) iterator.next();
				list.add(assembleArtikelDto(artikel));
			}
		}
		return list;
	}

	private ArtikelDto[] assembleArtikelDtos(Collection<?> artikels) {
		List<ArtikelDto> list = assembleArtikelDtosAsList(artikels);
		return (ArtikelDto[]) list.toArray(new ArtikelDto[0]);
	}

	public Integer createArtkla(ArtklaDto artklaDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artklaDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artklaDto == null"));
		}
		if (artklaDto.getCNr() == null || artklaDto.getBTops() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artklaDto.getCNr() == null || artklaDto.getBTops() == null"));
		}

		String sMandant = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			sMandant = getSystemFac().getHauptmandant();
		}

		try {
			Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
			query.setParameter(1, artklaDto.getCNr());
			query.setParameter(2, sMandant);
			Artkla doppelt = (Artkla) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTGRU.CNR"));

		} catch (NoResultException ex) {

		}
		artklaDto.setMandantCNr(sMandant);

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELKLASSE);
			artklaDto.setIId(pk);

			Artkla artkla = new Artkla(artklaDto.getIId(), artklaDto.getCNr(), artklaDto.getBTops(),
					artklaDto.getMandantCNr());

			em.persist(artkla);
			em.flush();
			setArtklaFromArtklaDto(artkla, artklaDto);
			if (artklaDto.getArtklasprDto() != null) {
				Artklaspr artklaspr = new Artklaspr(theClientDto.getLocUiAsString(), artklaDto.getIId());
				em.persist(artklaspr);
				em.flush();
				setArtklasprFromArtklasprDto(artklaspr, artklaDto.getArtklasprDto());
			}
			return artklaDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtkla(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
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

	public void updateArtkla(ArtklaDto artklaDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artklaDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artklaDto == null"));
		}
		if (artklaDto.getIId() == null || artklaDto.getCNr() == null || artklaDto.getBTops() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"artklaDto.getIId() == null || artklaDto.getCNr() == null || artklaDto.getBTops() == null"));
		}
		if (artklaDto.getIId().equals(artklaDto.getArtklaIId())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception("artklaDto.getIId() == artklaDto.getArtklaIId()"));

		}
		Integer iId = artklaDto.getIId();

		Artkla artkla = null;
		// try {
		artkla = em.find(Artkla.class, iId);
		if (artkla == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei updateArtikelkla. Es gibt keine iid " + iId + "\nartklaDto.toString: "
							+ artklaDto.toString());
		}

		try {
			Query query = em.createNamedQuery("ArtklafindByCNrMandantCNr");
			query.setParameter(1, artklaDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Artkla) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTKLA.CNR"));
			}
		} catch (NoResultException ex) {

		}
		try {
			setArtklaFromArtklaDto(artkla, artklaDto);

			if (artklaDto.getArtklasprDto() != null) {
				// try {
				Artklaspr artklaspr = em.find(Artklaspr.class, new ArtklasprPK(theClientDto.getLocUiAsString(), iId));

				if (artklaspr == null) {
					artklaspr = new Artklaspr(theClientDto.getLocUiAsString(), iId);
					em.persist(artklaspr);
					em.flush();
					setArtklasprFromArtklasprDto(artklaspr, artklaDto.getArtklasprDto());
				}
				setArtklasprFromArtklasprDto(artklaspr, artklaDto.getArtklasprDto());
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

		ShopgruppeISort iSorter = new ShopgruppeISort(em, theClientDto.getMandant());
		dto.setISort(iSorter.getNextISort());
		return createShopgruppeImpl(dto, theClientDto);
	}

	public Integer createShopgruppeVor(ShopgruppeDto dto, Integer vorIId, TheClientDto theClientDto) {
		if (vorIId == null)
			return createShopgruppe(dto, theClientDto);

		existsShopgruppe(dto, theClientDto);
		ShopgruppeISort iSorter = new ShopgruppeISort(em, theClientDto.getMandant());
		dto.setISort(iSorter.getPreviousISort(vorIId));
		return createShopgruppeImpl(dto, theClientDto);
	}

	private void existsShopgruppe(ShopgruppeDto dto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	protected Integer createShopgruppeImpl(ShopgruppeDto dto, TheClientDto theClientDto) throws EJBExceptionLP {
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

			Shopgruppe shopgruppe = new Shopgruppe(dto.getIId(), dto.getCNr(), dto.getMandantCNr());

			setShopgruppeFromShopgruppeDto(shopgruppe, dto);
			if (dto.getShopgruppesprDto() != null) {
				Shopgruppespr spr = new Shopgruppespr(theClientDto.getLocUiAsString(), dto.getIId());
				em.persist(spr);
				em.flush();
				setShopgruppesprFromShoprguppesprDto(spr, dto.getShopgruppesprDto());
			}
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeShopgruppe(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("ShopgruppesprfindByShopgruppeIId");
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

	public void updateShopgruppe(ShopgruppeDto shopgruppeDto, TheClientDto theClientDto) {

		if (shopgruppeDto.getIId().equals(shopgruppeDto.getShopgruppeIId())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception("shopgruppeDto.getIId().equals(shopgruppeDto.getShopgruppeIId())"));

		}
		Integer iId = shopgruppeDto.getIId();

		Shopgruppe artkla = null;

		artkla = em.find(Shopgruppe.class, iId);

		try {
			Query query = em.createNamedQuery("ShopgruppefindByCNrMandantCNr");
			query.setParameter(1, shopgruppeDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Shopgruppe) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SHOPGRUPPE.CNR"));
			}
		} catch (NoResultException ex) {

		}
		try {
			shopgruppeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			shopgruppeDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

			setShopgruppeFromShopgruppeDto(artkla, shopgruppeDto);

			if (shopgruppeDto.getShopgruppesprDto() != null) {

				Shopgruppespr artklaspr = em.find(Shopgruppespr.class,
						new ShopgruppesprPK(theClientDto.getLocUiAsString(), iId));

				if (artklaspr == null) {
					artklaspr = new Shopgruppespr(theClientDto.getLocUiAsString(), iId);
					em.persist(artklaspr);
					em.flush();

				}
				setShopgruppesprFromShoprguppesprDto(artklaspr, shopgruppeDto.getShopgruppesprDto());

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public ArtikelDto getErsatzartikel(Integer artikelIId, TheClientDto theClientDto) {
		// try {

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei getERsatzartikel. Es gibt keinen Artikel mit der iid " + artikelIId);
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

	public ArtklaDto[] artklaFindAll(TheClientDto theClientDto) throws EJBExceptionLP {
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

		String mandantCnr = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCnr = getSystemFac().getHauptmandant();
		}

		query.setParameter(1, mandantCnr);
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

	public ArtklaDto artklaFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Artkla artkla = em.find(Artkla.class, iId);
		if (artkla == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelklaFindByPrimaryKey. Keine Artikelkla mit iid " + iId);
		}
		ArtklaDto artklaDto = assembleArtklaDto(artkla);
		ArtklasprDto artklasprDto = null;
		Artklaspr artklaspr = em.find(Artklaspr.class, new ArtklasprPK(theClientDto.getLocUiAsString(), iId));
		if (artklaspr != null) {
			artklasprDto = assembleArtklasprDto(artklaspr);
		}
		if (artklasprDto == null) {
			String konzernsprache = theClientDto.getLocKonzernAsString();
			artklaspr = em.find(Artklaspr.class, new ArtklasprPK(konzernsprache, iId));
			if (artklaspr != null) {
				artklasprDto = assembleArtklasprDto(artklaspr);
			}
		}

		artklaDto.setArtklasprDto(artklasprDto);
		return artklaDto;
	}

	public Integer createVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto) {

		try {

			try {
				Query query = em.createNamedQuery("VerpackungsmittelfindByCNrMandantCNr");

				query.setParameter(1, dto.getCNr());
				query.setParameter(2, theClientDto.getMandant());
				Verpackungsmittel doppelt = (Verpackungsmittel) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_VERPACKUNGSMITTEL.UK"));
				}
			} catch (NoResultException ex1) {
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERPACKUNGSMITTEL);
			dto.setIId(pk);

			dto.setMandantCNr(theClientDto.getMandant());

			Verpackungsmittel verpackungsmittel = new Verpackungsmittel(dto.getIId(), dto.getCNr(), dto.getMandantCNr(),
					dto.getNGewichtInKG());
			em.persist(verpackungsmittel);
			em.flush();
			setVerpackungsmittelFromVerpackungsmittelDto(verpackungsmittel, dto);

			if (dto.getVerpackungsmittelsprDto() != null) {
				dto.getVerpackungsmittelsprDto().setVerpackungsmittelIId(dto.getIId());
				dto.getVerpackungsmittelsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
				createVerpackungsmittelspr(dto.getVerpackungsmittelsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return dto.getIId();

	}

	private void setVerpackungsmittelFromVerpackungsmittelDto(Verpackungsmittel bean, VerpackungsmittelDto dto) {
		bean.setCNr(dto.getCNr());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setNGewichtInKG(dto.getNGewichtInKG());

		em.merge(bean);
		em.flush();
	}

	public void createVerpackungsmittelspr(VerpackungsmittelsprDto verpackungsmittelsprDto) throws EJBExceptionLP {

		try {
			Verpackungsmittelspr spr = new Verpackungsmittelspr(verpackungsmittelsprDto.getLocaleCNr(),

					verpackungsmittelsprDto.getCBez(), verpackungsmittelsprDto.getVerpackungsmittelIId());
			em.persist(spr);
			em.flush();

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto) {

		try {

			Query query = em.createNamedQuery("VerpackungsmittelsprfindByVerpackungsmittelIId");
			query.setParameter(1, dto.getIId());
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Verpackungsmittelspr item = (Verpackungsmittelspr) iter.next();
				em.remove(item);
			}
			Verpackungsmittel verpackungsmittel = em.find(Verpackungsmittel.class, dto.getIId());

			em.remove(verpackungsmittel);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateVerpackungsmittel(VerpackungsmittelDto dto, TheClientDto theClientDto) {
		if (dto != null) {
			try {

				Verpackungsmittel verpackungsmittel = em.find(Verpackungsmittel.class, dto.getIId());

				try {
					Query query = em.createNamedQuery("VerpackungsmittelfindByCNrMandantCNr");

					query.setParameter(1, dto.getCNr());
					query.setParameter(2, theClientDto.getMandant());
					Integer iIdVorhanden = ((Verpackungsmittel) query.getSingleResult()).getIId();
					if (verpackungsmittel.getIId().equals(iIdVorhanden) == false) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
								new Exception("STKL_MONTAGEART.C_BEZ"));
					}

				} catch (NoResultException ex) {
					// nix
				}

				setVerpackungsmittelFromVerpackungsmittelDto(verpackungsmittel, dto);
				// sprache
				if (dto.getVerpackungsmittelsprDto() != null) {
					dto.getVerpackungsmittelsprDto().setVerpackungsmittelIId(dto.getIId());
					dto.getVerpackungsmittelsprDto().setLocaleCNr(theClientDto.getLocUiAsString());

					updateVerpackungsmittelspr(dto.getVerpackungsmittelsprDto());
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateVerpackungsmittelspr(VerpackungsmittelsprDto sprDto) throws EJBExceptionLP {

		if (sprDto != null) {

			try {
				VerpackungsmittelsprPK pk = new VerpackungsmittelsprPK(sprDto.getLocaleCNr(),
						sprDto.getVerpackungsmittelIId());
				Verpackungsmittelspr spr = em.find(Verpackungsmittelspr.class, pk);
				if (spr == null) {
					// diese Uebersetzung gibt es nocht nicht
					createVerpackungsmittelspr(sprDto);
				} else {
					spr.setCBez(sprDto.getCBez());
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public VerpackungsmittelDto verpackungsmittelFindByPrimaryKeyUndLocale(Integer iId, String localeCNr,
			TheClientDto theClientDto) {

		Verpackungsmittel bean = em.find(Verpackungsmittel.class, iId);

		VerpackungsmittelDto verpackungsmittelDto = VerpackungsmittelDtoAssembler.createDto(bean);

		// jetzt die Spr
		VerpackungsmittelsprDto sprDto = null;

		try {
			VerpackungsmittelsprPK sprPKErsterVersuch = new VerpackungsmittelsprPK(localeCNr,
					verpackungsmittelDto.getIId());
			Verpackungsmittelspr verpackungsmittelsprErsterVersuch = em.find(Verpackungsmittelspr.class,
					sprPKErsterVersuch);
			if (verpackungsmittelsprErsterVersuch != null) {
				sprDto = VerpackungsmittelsprDtoAssembler.createDto(verpackungsmittelsprErsterVersuch);
			} else {
				VerpackungsmittelsprPK sprPK = new VerpackungsmittelsprPK(theClientDto.getLocUiAsString(),
						verpackungsmittelDto.getIId());
				Verpackungsmittelspr verpackungsmittelspr = em.find(Verpackungsmittelspr.class, sprPK);
				if (verpackungsmittelspr != null) {
					sprDto = VerpackungsmittelsprDtoAssembler.createDto(verpackungsmittelspr);
				}
			}
		} catch (Throwable t) {
			// ignore
		}

		verpackungsmittelDto.setVkfortschrittsprDto(sprDto);
		return verpackungsmittelDto;
	}

	public VerpackungsmittelDto verpackungsmittelFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		return verpackungsmittelFindByPrimaryKeyUndLocale(iId, theClientDto.getLocUiAsString(), theClientDto);
	}

	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Shopgruppe shopgruppe = em.find(Shopgruppe.class, iId);
		Validator.entityFound(shopgruppe, iId);

		ShopgruppeDto shopgruppeDto = ShopgruppeDtoAssembler.createDto(shopgruppe);
		ShopgruppesprDto shopgruppesprDto = null;
		Shopgruppespr shopgruppespr = em.find(Shopgruppespr.class,
				new ShopgruppesprPK(theClientDto.getLocUiAsString(), iId));
		if (shopgruppespr != null) {
			shopgruppesprDto = ShopgruppesprDtoAssembler.createDto(shopgruppespr);
		}
		if (shopgruppesprDto == null) {
			String konzernsprache = theClientDto.getLocKonzernAsString();
			shopgruppespr = em.find(Shopgruppespr.class, new ShopgruppesprPK(konzernsprache, iId));
			if (shopgruppespr != null) {
				shopgruppesprDto = ShopgruppesprDtoAssembler.createDto(shopgruppespr);
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

	private void setShopgruppeFromShopgruppeDto(Shopgruppe shopgruppe, ShopgruppeDto shopgruppeDto) {
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
			Query query = em.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			Webshop doppelt = (Webshop) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOP.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOP);
			dto.setIId(pk);

			Webshop bean = new Webshop(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
			bean.setWebshopartCnr(dto.getWebshopartCnr());
			em.persist(bean);
			em.flush();
			setWebshopFromIsWebshopDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createArtikelshopgruppe(ArtikelshopgruppeDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("ArtikelshopgruppefindByArtikelIIdShopgruppeIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getShopgruppeIId());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELSHOPGRUPPE.UK"));
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

			Artikelshopgruppe bean = new Artikelshopgruppe(dto.getIId(), dto.getArtikelIId(), dto.getShopgruppeIId());
			setArtikelshopgruppeFromArtikelshopgruppeDto(bean, dto);
			// em.persist(bean);
			// em.flush();
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createArtikelallergen(ArtikelalergenDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("ArtikelalergenfindByArtikelIIdAlergenIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getAlergenIId());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELALERGEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELALERGEN);
			dto.setIId(pk);

			Artikelalergen bean = new Artikelalergen(dto.getIId(), dto.getArtikelIId(), dto.getAlergenIId());
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

	public ErsatztypenDto ersatztypenFindByPrimaryKey(Integer iId) {
		Ersatztypen ialle = em.find(Ersatztypen.class, iId);
		return ErsatztypenDtoAssembler.createDto(ialle);
	}

	public ErsatztypenDto[] ersatztypenFindByArtikelIId(Integer artikelId) {
		Query query = em.createNamedQuery("ErsatztypenfindByArtikelIId");
		query.setParameter(1, artikelId);

		return ErsatztypenDtoAssembler.createDtos(query.getResultList());
	}

	public ErsatztypenDto[] ersatztypenfindByArtikelIIdErsatz(Integer artikelIIdErsatz) {
		Query query = em.createNamedQuery("ErsatztypenfindByArtikelIIdErsatz");
		query.setParameter(1, artikelIIdErsatz);

		return ErsatztypenDtoAssembler.createDtos(query.getResultList());
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
			Query query = em.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Webshop) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOP.UK"));
			}
		} catch (NoResultException ex) {

		}

		setWebshopFromIsWebshopDto(ialle, dto);

	}

	public void updateArtikelshopgruppe(ArtikelshopgruppeDto dto, TheClientDto theClientDto) {
		Artikelshopgruppe ialle = em.find(Artikelshopgruppe.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("ArtikelshopgruppefindByArtikelIIdShopgruppeIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getShopgruppeIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artikelshopgruppe) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_ARTIKELSHOPGRUPPE.UK"));
			}
		} catch (NoResultException ex) {

		}

		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setArtikelshopgruppeFromArtikelshopgruppeDto(ialle, dto);
	}

	public void updateErsatztypen(ErsatztypenDto dto, TheClientDto theClientDto) {
		Ersatztypen ialle = em.find(Ersatztypen.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("ErsatztypenfindByArtikelIIdArtikelIIdErsatz");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getArtikelIIdErsatz());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Ersatztypen) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ERSATZTYPEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setErsatztypenFromErsatztypenDto(ialle, dto);
	}

	public void erhoeheAlleStaffelnEinesArtikellieferant(Integer artikellieferantIId, Date tGueltigab,
			BigDecimal nProzent, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
		query.setParameter(1, artikellieferantIId);
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();

		Artikellieferant al = em.find(Artikellieferant.class, artikellieferantIId);

		int nachkommastellen = 2;
		try {
			nachkommastellen = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HashMap<BigDecimal, TreeMap<Timestamp, Artikellieferantstaffel>> hmMengen = new HashMap<BigDecimal, TreeMap<Timestamp, Artikellieferantstaffel>>();

		while (it.hasNext()) {
			Artikellieferantstaffel staffel = (Artikellieferantstaffel) it.next();

			TreeMap tm = null;

			if (hmMengen.containsKey(staffel.getNMenge())) {

				tm = hmMengen.get(staffel.getNMenge());

			} else {

				tm = new TreeMap();

			}

			if (Helper.short2boolean(staffel.getBRabattbehalten()) == false) {
				if (staffel.getTPreisgueltigab().getTime() <= tGueltigab.getTime()
						&& (staffel.getTPreisgueltigbis() == null || staffel.getTPreisgueltigbis().after(tGueltigab))) {

					if (al.getNEinzelpreis() != null && al.getNEinzelpreis().doubleValue() != 0
							&& staffel.getNNettopreis() != null) {

						tm.put(staffel.getTPreisgueltigab(), staffel);

						hmMengen.put(staffel.getNMenge(), tm);
					}

				}
			}
		}

		Iterator itMengen = hmMengen.keySet().iterator();

		while (itMengen.hasNext()) {

			BigDecimal bdMengen = (BigDecimal) itMengen.next();

			TreeMap tmStaffeln = hmMengen.get(bdMengen);

			if (tmStaffeln.size() > 0) {

				Object lastKey = tmStaffeln.lastKey();

				Artikellieferantstaffel staffel = (Artikellieferantstaffel) tmStaffeln.get(lastKey);

				// Nur wenn Nettopreis fixiert und nach dem genwuenschten Datum

				BigDecimal preisNeu = Helper.getProzentWert(staffel.getNNettopreis(), nProzent, nachkommastellen)
						.add(staffel.getNNettopreis());
				// Rabattsatz

				BigDecimal rabattsatz = new BigDecimal(1)
						.subtract(preisNeu.divide(al.getNEinzelpreis(), 4, BigDecimal.ROUND_HALF_EVEN));

				Query queryUK = em
						.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
				queryUK.setParameter(1, artikellieferantIId);
				queryUK.setParameter(2, staffel.getNMenge());
				queryUK.setParameter(3, Helper.cutTimestamp(new Timestamp(tGueltigab.getTime())));
				Collection<?> clUK = queryUK.getResultList();

				// Wenns genau zu dem Datum schon einen gibt, dann
				// diesen Updaten
				if (clUK.size() > 0) {
					Artikellieferantstaffel vorhanden = (Artikellieferantstaffel) clUK.iterator().next();

					vorhanden.setNNettopreis(preisNeu);

					vorhanden.setFRabatt(rabattsatz.multiply(new BigDecimal(100)).doubleValue());

					em.merge(vorhanden);
					em.flush();

				} else {

					ArtikellieferantstaffelDto astaffelDtoKopie = artikellieferantstaffelFindByPrimaryKey(
							staffel.getIId());

					// Sonst neue anlegen

					astaffelDtoKopie.setTPreisgueltigab(Helper.cutTimestamp(new Timestamp(tGueltigab.getTime())));
					astaffelDtoKopie.setNNettopreis(preisNeu);
					astaffelDtoKopie.setFRabatt(rabattsatz.multiply(new BigDecimal(100)).doubleValue());
					astaffelDtoKopie.setIId(null);

					createArtikellieferantstaffel(astaffelDtoKopie, theClientDto);

				}
			}
		}

	}

	public void updateArtikelallergen(ArtikelalergenDto dto, TheClientDto theClientDto) {
		Artikelalergen ialle = em.find(Artikelalergen.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("ArtikelalergenfindByArtikelIIdAlergenIId");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getAlergenIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artikelalergen) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELALERGEN.UK"));
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
		Artikelshopgruppe toRemove = em.find(Artikelshopgruppe.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeErsatztypen(ErsatztypenDto dto) {
		Ersatztypen toRemove = em.find(Ersatztypen.class, dto.getIId());
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
		bean.setWebshopartCnr(dto.getWebshopartCnr());
		bean.setCUrl(dto.getCUrl());
		bean.setCUser(dto.getCUser());
		bean.setCPassword(dto.getCPassword());
		em.merge(bean);
		em.flush();
	}

	private void setArtikelshopgruppeFromArtikelshopgruppeDto(Artikelshopgruppe bean, ArtikelshopgruppeDto dto) {
		bean.setShopgruppeIId(dto.getShopgruppeIId());
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());
		bean.setTAendern(dto.getTAendern());
		bean.setTAnlegen(dto.getTAnlegen());

		em.merge(bean);
		em.flush();
	}

	private void setArtikelalergenFromArtikelalergenDto(Artikelalergen bean, ArtikelalergenDto dto) {
		bean.setAlergenIId(dto.getAlergenIId());
		bean.setArtikelIId(dto.getArtikelIId());

		em.merge(bean);
		em.flush();
	}

	public Integer createArtgru(ArtgruDto artgruDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artgruDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artgruDto == null"));
		}
		if (artgruDto.getCNr() == null || artgruDto.getBRueckgabe() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artgruDto.getCNr() == null || artgruDto.getBRueckgabe() == null"));
		}

		String sMandant = theClientDto.getMandant();

		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			sMandant = getSystemFac().getHauptmandant();
		}

		artgruDto.setMandantCNr(sMandant);

		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.

			Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
			query.setParameter(1, artgruDto.getCNr());
			query.setParameter(2, sMandant);
			Artgru doppelt = (Artgru) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTGRU.CNR"));
		} catch (NoResultException ex) {

		}

		if (artgruDto.getBSeriennrtragend() == null) {
			artgruDto.setBSeriennrtragend(Helper.getShortFalse());
		}

		if (artgruDto.getBChargennrtragend() == null) {
			artgruDto.setBChargennrtragend(Helper.getShortFalse());
		}

		if (artgruDto.getBKeinBelegdruckMitRabatt() == null) {
			artgruDto.setBKeinBelegdruckMitRabatt(Helper.getShortFalse());
		}

		if (artgruDto.getBAufschlagEinzelpreis() == null) {
			artgruDto.setBAufschlagEinzelpreis(Helper.getShortFalse());
		}

		if (artgruDto.getBFremdfertigung() == null) {
			artgruDto.setBFremdfertigung(Helper.getShortFalse());
		}

		if (artgruDto.getBBeiErsterZeitbuchungAbbuchen() == null) {
			artgruDto.setBBeiErsterZeitbuchungAbbuchen(Helper.getShortTrue());
		}

		artgruDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		artgruDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artgruDto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		artgruDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELGRUPPE);
			artgruDto.setIId(pk);
			Artgru artgru = new Artgru(artgruDto.getIId(), artgruDto.getCNr(), artgruDto.getBRueckgabe(),
					artgruDto.getMandantCNr(), artgruDto.getBZertifizierung(), artgruDto.getPersonalIIdAnlegen(),
					artgruDto.getPersonalIIdAendern(), artgruDto.getTAnlegen(), artgruDto.getTAendern(),
					artgruDto.getBKeinevkwarnmeldungimls(), artgruDto.getBSeriennrtragend(),
					artgruDto.getBChargennrtragend(), artgruDto.getBKeinBelegdruckMitRabatt(),
					artgruDto.getBAufschlagEinzelpreis(), artgruDto.getBFremdfertigung(),
					artgruDto.getBKeinBelegdruckMitRabatt());
			em.persist(artgru);
			em.flush();
			setArtgruFromArtgruDto(artgru, artgruDto);
			if (artgruDto.getArtgrusprDto() != null) {
				Artgruspr artgruspr = new Artgruspr(theClientDto.getLocUiAsString(), artgruDto.getIId());
				em.persist(artgruspr);
				em.flush();
				setArtgrusprFromArtgrusprDto(artgruspr, artgruDto.getArtgrusprDto());
			}
			updateArtgrumandant(artgruDto.getIId(), artgruDto.getKontoIId(), theClientDto);

			return artgruDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}

	}

	public void updateArtgrumandant(Integer artgruIId, Integer kontoIId, TheClientDto theClientDto) {

		if (kontoIId == null) {
			try {
				Query querykto = em.createNamedQuery("ArtgrumandantfindByArtgruIIdMandantCNr");
				querykto.setParameter(1, artgruIId);
				querykto.setParameter(2, theClientDto.getMandant());
				Artgrumandant am = (Artgrumandant) querykto.getSingleResult();
				em.remove(am);
			} catch (NoResultException ex) {

			}
		} else {
			try {
				Query querykto = em.createNamedQuery("ArtgrumandantfindByArtgruIIdMandantCNr");
				querykto.setParameter(1, artgruIId);
				querykto.setParameter(2, theClientDto.getMandant());
				Artgrumandant am = (Artgrumandant) querykto.getSingleResult();
				am.setKontoIId(kontoIId);
				em.merge(am);
				em.flush();
			} catch (NoResultException ex) {
				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTGRUMANDANT);

				Artgrumandant am = new Artgrumandant(pk, artgruIId, theClientDto.getMandant());
				am.setKontoIId(kontoIId);
				em.merge(am);
				em.flush();
			}
		}
	}

	public void removeArtgru(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
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

			try {
				Query querykto = em.createNamedQuery("ArtgrumandantfindByArtgruIIdMandantCNr");
				querykto.setParameter(1, iId);
				querykto.setParameter(2, theClientDto.getMandant());
				Artgrumandant am = (Artgrumandant) querykto.getSingleResult();
				em.remove(am);
			} catch (NoResultException ex) {

			}

			Artgru artgru = em.find(Artgru.class, iId);
			if (artgru == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtgru. Es gibt keine Artgru mit der iid " + iId);
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
			Trumphtopslog trumphtopslog = new Trumphtopslog(ttlogDto.getIId(), ttlogDto.getCImportfilename(),
					ttlogDto.getCError(), ttlogDto.getArtikelIId(), ttlogDto.getArtikelIIdMaterial(),
					ttlogDto.getNGewicht(), ttlogDto.getNGestpreisneu(), ttlogDto.getIBearbeitungszeit());
			em.persist(trumphtopslog);
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex2);
		}

	}

	public void updateArtgru(ArtgruDto artgruDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artgruDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artgruDto == null"));
		}
		if (artgruDto.getIId() == null || artgruDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artgruDto.getIId() == null || artgruDto.getCNr() == null"));
		}
		if (artgruDto.getIId().equals(artgruDto.getArtgruIId())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					new Exception("artgruDto.getIId() == artgruDto.getArtgruIId()"));

		}
		Integer iId = artgruDto.getIId();
		Artgru artgru = null;
		// try {
		artgru = em.find(Artgru.class, iId);
		if (artgru == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtgru. Es gibt keine Artgru mit iid " + iId);

		}

		// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
		try {
			Query query = em.createNamedQuery("ArtgrufindByCNrMandantCNr");
			query.setParameter(1, artgruDto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Artgru) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTGRU.CNR"));

			}
		} catch (NoResultException ex) {

		}

		artgruDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artgruDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		setArtgruFromArtgruDto(artgru, artgruDto);
		try {
			if (artgruDto.getArtgrusprDto() != null) {
				try {
					Artgruspr artgruspr = em.find(Artgruspr.class,
							new ArtgrusprPK(theClientDto.getLocUiAsString(), iId));
					if (artgruspr == null) {
						artgruspr = new Artgruspr(theClientDto.getLocUiAsString(), iId);
						em.persist(artgruspr);
						em.flush();
					}
					setArtgrusprFromArtgrusprDto(artgruspr, artgruDto.getArtgrusprDto());

				} catch (NoResultException ex) {
					Artgruspr artgruspr = new Artgruspr(theClientDto.getLocUiAsString(), iId);
					em.persist(artgruspr);
					setArtgrusprFromArtgrusprDto(artgruspr, artgruDto.getArtgrusprDto());
				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		updateArtgrumandant(artgruDto.getIId(), artgruDto.getKontoIId(), theClientDto);

	}

	public ArtgruDto getLetzteVatergruppe(Integer artgruIId) {
		// try {
		Artgru artgru = em.find(Artgru.class, artgruIId);
		if (artgru == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei getLetzteVatergruppe. Es gibt keine Artgru mit iid " + artgruIId);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei getLetzteVaterklasse. Es gibt keine artklasse mit iid " + artklaIId);
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

	public ArtgruDto artgruFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Artgru artgru = em.find(Artgru.class, iId);
		if (artgru == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artgru FindByPrimaryKey. Es gibt keine Artikelgruppe mit iid " + iId);
		}
		ArtgruDto artgruDto = assembleArtgruDto(artgru);
		ArtgrusprDto artgrusprDto = null;
		// try {
		Artgruspr artgruspr = em.find(Artgruspr.class, new ArtgrusprPK(theClientDto.getLocUiAsString(), iId));
		if (artgruspr != null) {
			artgrusprDto = assembleArtgrusprDto(artgruspr);
		}
		// }
		// catch (FinderException ex) {
		// nothing here
		// }
		if (artgrusprDto == null) {
			// try {
			artgruspr = em.find(Artgruspr.class, new ArtgrusprPK(theClientDto.getLocKonzernAsString(), iId));
			if (artgruspr != null) {
				artgrusprDto = assembleArtgrusprDto(artgruspr);
			}
			// }
			// catch (FinderException ex) {
			// nothing here
			// }
		}
		artgruDto.setArtgrusprDto(artgrusprDto);
		try {
			Query query = em.createNamedQuery("ArtgrumandantfindByArtgruIIdMandantCNr");
			query.setParameter(1, iId);
			query.setParameter(2, theClientDto.getMandant());
			Artgrumandant am = (Artgrumandant) query.getSingleResult();
			artgruDto.setKontoIId(am.getKontoIId());
		} catch (NoResultException ex) {

		}

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

		String mandantCnr = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandantCnr = getSystemFac().getHauptmandant();
		}

		query.setParameter(1, mandantCnr);
		Collection<?> cl = query.getResultList();
		ArtgruDto[] artgruDto = assembleArtgruDtos(cl);
		return artgruDto;
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
		artgru.setNEkpreisaufschlag(artgruDto.getNEkpreisaufschlag());
		artgru.setBRueckgabe(artgruDto.getBRueckgabe());
		artgru.setBZertifizierung(artgruDto.getBZertifizierung());
		artgru.setPersonalIIdAendern(artgruDto.getPersonalIIdAendern());
		artgru.setPersonalIIdAnlegen(artgruDto.getPersonalIIdAnlegen());
		artgru.setTAendern(artgruDto.getTAendern());
		artgru.setTAnlegen(artgruDto.getTAnlegen());
		artgru.setBKeinevkwarnmeldungimls(artgruDto.getBKeinevkwarnmeldungimls());
		artgru.setKostenstelleIId(artgruDto.getKostenstelleIId());
		artgru.setBSeriennrtragend(artgruDto.getBSeriennrtragend());
		artgru.setBChargennrtragend(artgruDto.getBChargennrtragend());
		artgru.setBKeinBelegdruckMitRabatt(artgruDto.getBKeinBelegdruckMitRabatt());
		artgru.setBAufschlagEinzelpreis(artgruDto.getBAufschlagEinzelpreis());
		artgru.setArtikelIIdKommentar(artgruDto.getArtikelIIdKommentar());
		artgru.setBFremdfertigung(artgruDto.getBFremdfertigung());
		artgru.setBBeiErsterZeitbuchungAbbuchen(artgruDto.getBBeiErsterZeitbuchungAbbuchen());

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("katalogDto == null"));
		}
		if (katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null"));
		}
		try {
			Query query = em.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, katalogDto.getArtikelIId());
			query.setParameter(2, katalogDto.getCKatalog());
			// @todo getSingleResult oder getResultList ?
			Katalog doppelt = (Katalog) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_KATALOG.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KATALOG);
			katalogDto.setIId(pk);
			Katalog katalog = new Katalog(katalogDto.getIId(), katalogDto.getArtikelIId(), katalogDto.getCKatalog());
			em.persist(katalog);
			em.flush();
			setKatalogFromKatalogDto(katalog, katalogDto);
			return katalogDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeKatalog(KatalogDto dto) throws EJBExceptionLP {
		myLogger.entry();
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}
		// try {
		Katalog toRemove = em.find(Katalog.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeKatalog. Es gibt keinen Katalog mit iid " + dto.getIId()
							+ "\nKatalogDto.toString(): " + dto.toString());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("katalogDto == null"));
		}
		if (katalogDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("katalogDto.getIId() == null"));
		}
		if (katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"katalogDto.getArtikelIId() == null || katalogDto.getCSeite() == null || katalogDto.getCKatalog() == null"));
		}
		// try {
		Integer iId = katalogDto.getIId();
		Katalog katalog = em.find(Katalog.class, iId);
		if (katalog == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateKatalog. Es gibt keinen Katalog mit iid " + iId + "\n KAtalogDto.toString(): "
							+ katalogDto.toString());
		}

		try {
			Query query = em.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, katalogDto.getArtikelIId());
			query.setParameter(2, katalogDto.getCKatalog());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Katalog) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_KATALOG.UK"));
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

	public KatalogDto katalogFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Katalog katalog = em.find(Katalog.class, iId);
		if (katalog == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei KatalogFindByPrimaryKey. Es gibt keinen Katalog mit iid " + iId);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("katalogDto == null"));
		}
		if (einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null
				|| einkaufseanDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null || einkaufseanDto.getNMenge() == null"));
		}

		try {
			Query query = em.createNamedQuery("EinkaufseanfindByCEan");
			query.setParameter(1, einkaufseanDto.getCEan());

			Integer iIdVorhanden = ((Einkaufsean) query.getSingleResult()).getIId();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_EINKAUFSEAN.UK"));

		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EINKAUFSEAN);
			einkaufseanDto.setIId(pk);
			Einkaufsean einkaufsean = new Einkaufsean(einkaufseanDto.getIId(), einkaufseanDto.getArtikelIId(),
					einkaufseanDto.getCEan(), einkaufseanDto.getNMenge());
			em.persist(einkaufsean);
			em.flush();
			setEinkaufseanFromEinkaufseanDto(einkaufsean, einkaufseanDto);
			return einkaufseanDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeEinkaufsean(EinkaufseanDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		Einkaufsean toRemove = em.find(Einkaufsean.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei remove Einkaufsean. Es gibt keine iid " + dto.getIId()
							+ "\neimnkaufseanDto.toString(): " + dto.toString());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("einkaufseanDto == null"));
		}
		if (einkaufseanDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufseanDto.getIId() == null"));
		}
		if (einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null
				|| einkaufseanDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufseanDto.getArtikelIId() == null || einkaufseanDto.getCEan() == null || einkaufseanDto.getNMenge() == null"));
		}

		Integer iId = einkaufseanDto.getIId();
		Einkaufsean einkaufsean = em.find(Einkaufsean.class, iId);
		if (einkaufsean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsean. Es gibt keine iid " + iId + "\nEinkaufseanDto.toString(): "
							+ einkaufseanDto.toString());
		}

		try {
			Query query = em.createNamedQuery("EinkaufseanfindByCEan");
			query.setParameter(1, einkaufseanDto.getCEan());

			Integer iIdVorhanden = ((Einkaufsean) query.getSingleResult()).getIId();
			if (einkaufsean.getIId().equals(iIdVorhanden) == false) {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_EINKAUFSEAN.UK"));
			}

		} catch (NoResultException ex) {

		}
		setEinkaufseanFromEinkaufseanDto(einkaufsean, einkaufseanDto);

	}

	public EinkaufseanDto einkaufseanFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Einkaufsean einkaufsean = em.find(Einkaufsean.class, iId);
		if (einkaufsean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei einkaufseanFindByPrimaryKey. Es gibt keine iid " + iId);
		}
		return assembleEinkaufseanDto(einkaufsean);

	}

	public EinkaufseanDto einkaufseanFindByCEan(String cEan) {
		if (cEan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cEan == null"));
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

	private void setEinkaufseanFromEinkaufseanDto(Einkaufsean einkaufsean, EinkaufseanDto einkaufseanDto) {
		einkaufsean.setCEan(einkaufseanDto.getCEan());
		einkaufsean.setArtikelIId(einkaufseanDto.getArtikelIId());
		einkaufsean.setNMenge(einkaufseanDto.getNMenge());
		em.merge(einkaufsean);
		em.flush();
	}

	private EinkaufseanDto assembleEinkaufseanDto(Einkaufsean einkaufsean) {
		return EinkaufseanDtoAssembler.createDto(einkaufsean);
	}

	public KatalogDto katalogFindByArtikelIIdCKatalog(Integer iId, String cKatalog) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		if (cKatalog == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("cKatalog == null"));
		}

		try {
			Query query = em.createNamedQuery("KatalogfindByArtikelIIdCKatalog");
			query.setParameter(1, iId);
			query.setParameter(2, cKatalog);
			Katalog katalog = (Katalog) query.getSingleResult();
			return assembleKatalogDto(katalog);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei katalogFindByArtikelIIdCKatalog es gibt mehr als ein ERgebnis f\u00FCr ArtikelIId "
							+ iId + " KatalogCnr " + cKatalog);
		}
	}

	public Integer gibtEsBereitsEinenBevorzugtenArtikel(String artikelnummer, TheClientDto theClientDto) {

		try {
			if (artikelnummer != null) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
				int iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

				String artikelnummerOhneHst = artikelnummer.substring(0, iLaengeArtikelnummer);

				Session session = FLRSessionFactory.getFactory().openSession();

				String queryString = "SELECT a FROM FLRArtikelliste AS a WHERE a.mandant_c_nr='"
						+ theClientDto.getMandant() + "' AND a.c_nr LIKE '" + artikelnummerOhneHst
						+ "%' AND a.b_bevorzugt=1 AND a.c_nr<>'" + artikelnummer + "'";

				org.hibernate.Query query = session.createQuery(queryString);
				List<?> resultList = query.list();
				if (resultList.size() > 0) {
					FLRArtikelliste artikel = (FLRArtikelliste) resultList.iterator().next();
					return artikel.getI_id();
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	// PJ20713
	public ArtikelDto pruefeObHerstellernummerandererArtikelVerwendet(Integer artikelIId, TheClientDto theClientDto) {

		Artikel artikel = em.find(Artikel.class, artikelIId);

		if (artikel.getCArtikelnrhersteller() == null) {
			return null;
		} else {
			List<ArtikelDto> verwendet = artikelFindByArtikelnrHersteller(artikel.getCArtikelnrhersteller(),
					theClientDto);

			Iterator it = verwendet.iterator();
			while (it.hasNext()) {
				ArtikelDto aDto = (ArtikelDto) it.next();

				if (!aDto.getIId().equals(artikelIId)) {
					return aDto;
				}

			}

			return null;
		}

	}

	/**
	 * Prueft eine Artikelnummer auf erlaubte Zeichen. Erlaubt sind :
	 * A-Z,0-9,OE,UE,AE,&szlig;,_,- Blank ist nur als Fueller zwischen Artikelnummer
	 * und Herstellerkennung erlaubt
	 * 
	 * @param cNr          String
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void pruefeArtikelnummer(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception("cNr == null"));
		}

		Integer iMaxLaenge = null;
		Integer iMinLaenge = null;
		String gueltigeZeichen = "";
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_ARTIKELNUMMER);
			iMinLaenge = (Integer) parameter.getCWertAsObject();

			if (cNr.length() < iMinLaenge.intValue() && !cNr.startsWith("~")) {
				ArrayList<Object> al = new ArrayList<Object>();
				if (iMinLaenge != null) {
					al.add(iMinLaenge);
					al.add(cNr);
				}
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ, al,
						new Exception("FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ"));
			}

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);

			iMaxLaenge = (Integer) parameter.getCWertAsObject();

			// SP9555
			if (iMaxLaenge > 25) {
				ArrayList al = new ArrayList();
				al.add(iMaxLaenge);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKELNUMMER_ZU_LANG, al,
						new Exception("FEHLER_ARTIKELNUMMER_ZU_LANG"));
			}

			boolean bHerstellerkopplung = getMandantFac().hatZusatzfunktionberechtigung(
					com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG, theClientDto);

			int iLaengeHersteller = 0;
			int iLetztesZeichenVorDerHerstellerNummer = 0;
			if (bHerstellerkopplung) {

				parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG);

				iMaxLaenge = iMaxLaenge + (Integer) parameter.getCWertAsObject();
				iLaengeHersteller = (Integer) parameter.getCWertAsObject();

				for (int k = 0; k < cNr.length(); k++) {
					char ck = cNr.charAt(k);

					if (ck == ' ') {
						iLetztesZeichenVorDerHerstellerNummer = k - 1;
						break;
					}

				}

			}

			// SP9360
			if (iMaxLaenge > 25) {
				ArrayList al = new ArrayList();
				al.add(iMaxLaenge);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKELNUMMER_INCL_HERSTELLERNUMMER_ZU_LANG, al,
						new Exception("FEHLER_ARTIKELNUMMER_INCL_HERSTELLERNUMMER_ZU_LANG"));
			}

			if (cNr.length() > iMaxLaenge.intValue()) {
				ArrayList al = new ArrayList();
				al.add(cNr);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG, al,
						new Exception("FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG"));
			}

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
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

						if (i > iLetztesZeichenVorDerHerstellerNummer && i < (iMaxLaenge - iLaengeHersteller)) {
							continue;
						}

					}

					ArrayList<Object> l = new ArrayList<Object>();
					l.add(new Character(c));
					l.add(cNr);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT, l,
							new Exception("FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT"));
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

	public String getHerstellercode(Integer partnerIId, TheClientDto theClientDto) {

		try {

			Query ejbquery = em.createNamedQuery("HerstellerfindByPartnerIId");
			ejbquery.setParameter(1, partnerIId);

			HerstellerDto[] dtos = assembleHerstellerDtos(ejbquery.getResultList());

			if (dtos.length == 1) {
				return dtos[0].getCNr();
			} else if (dtos.length > 1) {
				// Eventuell mehrere Resultate, dann schauen ob eines dem Pattern passt
				for (HerstellerDto dto : dtos) {
					if (patternHerstellerkuerzel.matcher(dto.getCNr()).matches()) {
						return dto.getCNr();
					}
				}
			}

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_LAENGE_HERSTELLERBEZEICHNUNG);
			Integer iLaenge = (Integer) parameter.getCWertAsObject();

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

			String buchstabe = partnerDto.getCName1nachnamefirmazeile1().substring(0, 1).toUpperCase();

			Session session = FLRSessionFactory.getFactory().openSession();

			String query = "FROM FLRHersteller h WHERE h.c_nr LIKE '" + buchstabe + "%' ORDER BY C_NR DESC";
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HERSTELLERKUERZEL_OVERFLOW,
							new Exception("FEHLER_HERSTELLERKUERZEL_OVERFLOW"));
				}

				return buchstabe + Helper.fitString2LengthAlignRight(neueNummer + "", iLaenge - 1, '0');

			} else {
				return buchstabe + Helper.fitString2LengthAlignRight("0", iLaenge - 1, '0');
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

	private void setArtgrusprFromArtgrusprDto(Artgruspr artgruspr, ArtgrusprDto artgrusprDto) {
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

	private void setVerpackungFromVerpackungDto(Verpackung verpackung, VerpackungDto verpackungDto) {
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

	private void pruefeObEndlosschleifeErsatzartikel(Integer artikelIId, Integer artikelIId_Ersatz) {
		// try {
		Artikel artikel = em.find(Artikel.class, artikelIId_Ersatz);
		if (artikel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei pruefeObEndlosschleifeErsatzartikel Es gibt keinen Ersatzartikel. iId: "
							+ artikelIId_Ersatz);
		}
		Integer artikelIId_ErsatzNaechsteEbene = artikel.getArtikelIIdErsatz();
		if (artikelIId_ErsatzNaechsteEbene != null) {
			if (artikelIId_ErsatzNaechsteEbene.equals(artikelIId)) {
				// DEADLOCK
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK,
						new Exception("FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK"));

			} else {
				pruefeObEndlosschleifeErsatzartikel(artikelIId, artikelIId_ErsatzNaechsteEbene);
			}

		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void artikeleigenschaftenDefaultwerteAnlegen(Integer artikelIId, Integer artgruIId,
			TheClientDto theClientDto) {

		ArrayList<PaneldatenDto> alDaten = new ArrayList<PaneldatenDto>();
		PanelbeschreibungDto[] pbesDtos = getPanelFac().panelbeschreibungFindByPanelCNrMandantCNr(
				PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, theClientDto.getMandant(), artgruIId);
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

		getPanelFac().createPaneldaten(alDaten.toArray(new PaneldatenDto[alDaten.size()]), theClientDto);

	}

	public void preiseAusAnfrageRueckpflegen(Integer anfrageIId, Integer anfragepositionlieferdatenIId,
			boolean bStaffelnLoeschen, boolean bLieferantVorreihen, boolean bAlsStaffelpreisRueckpflegen,
			TheClientDto theClientDto) {

		try {
			AnfragepositionlieferdatenDto anfposliefDto = getAnfragepositionFac()
					.anfragepositionlieferdatenFindByPrimaryKey(anfragepositionlieferdatenIId, theClientDto);

			AnfragepositionDto anfposDto = getAnfragepositionFac()
					.anfragepositionFindByPrimaryKey(anfposliefDto.getAnfragepositionIId(), theClientDto);

			com.lp.server.anfrage.service.AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(anfrageIId,
					theClientDto);

			com.lp.server.partner.service.LieferantDto lf = getLieferantFac()
					.lieferantFindByPrimaryKey(anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto);

			String sQuery = "SELECT apl FROM FLRAnfragepositionlieferdaten apl WHERE apl.flranfrageposition.flrartikel.i_id="
					+ anfposDto.getArtikelIId() + " AND apl.flranfrageposition.flranfrage.i_id=" + anfrageIId
					+ " ORDER BY apl.n_anliefermenge ASC";

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query query = session.createQuery(sQuery);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			Timestamp tPreisgueltigab = anfposliefDto.getTPreisgueltigab();

			if (tPreisgueltigab == null) {
				tPreisgueltigab = anfrageDto.getTAngebotdatum();
			}

			if (tPreisgueltigab == null) {
				tPreisgueltigab = anfrageDto.getTBelegdatum();
			}

			ArtikellieferantDto alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					anfposDto.getArtikelIId(), anfrageDto.getLieferantIIdAnfrageadresse(),
					new java.sql.Date(tPreisgueltigab.getTime()), null, theClientDto);

			BigDecimal anlieferPreisInLieferantenwaehrung = null;

			if (bStaffelnLoeschen && alDto != null) {
				ArtikellieferantstaffelDto[] staffelnDto = artikellieferantstaffelFindByArtikellieferantIId(
						alDto.getIId());
				for (int i = 0; i < staffelnDto.length; i++) {
					removeArtikellieferantstaffel(staffelnDto[i]);
				}

			}

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANFRAGE, ParameterFac.PARAMETER_MENGEN_IN_STAFFELN_UEBERNEHMEN);

			boolean bMengenInStaffelUebernehmen = (Boolean) parameter.getCWertAsObject();

			int i = 0;

			while (resultListIterator.hasNext()) {
				FLRAnfragepositionlieferdaten flrAnfragepositionlieferdaten = (FLRAnfragepositionlieferdaten) resultListIterator
						.next();

				if (flrAnfragepositionlieferdaten.getN_nettogesamtpreis() != null
						&& flrAnfragepositionlieferdaten.getN_anliefermenge() != null
						&& flrAnfragepositionlieferdaten.getN_anliefermenge().doubleValue() > 0
						&& flrAnfragepositionlieferdaten.getN_nettogesamtpreis() != null
						&& flrAnfragepositionlieferdaten.getN_nettogesamtpreis().doubleValue() > 0) {
					anfposliefDto = getAnfragepositionFac().anfragepositionlieferdatenFindByPrimaryKey(
							flrAnfragepositionlieferdaten.getI_id(), theClientDto);
					if (i == 0) {

						anlieferPreisInLieferantenwaehrung = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								anfposliefDto.getNNettogesamtpreis(), anfrageDto.getWaehrungCNr(), lf.getWaehrungCNr(),
								new Date(anfrageDto.getTBelegdatum().getTime()), theClientDto);

						// SP9451
						if (anfposliefDto.getTPreisgueltigab() != null) {
							tPreisgueltigab = anfposliefDto.getTPreisgueltigab();
						}

						if (alDto != null && !alDto.getTPreisgueltigab().equals(tPreisgueltigab)) {
							// Damit ein neuer Eintrag angelegt wird
							alDto = null;
						}

						// Zuerst Artikellieferant anlegen
						if (alDto != null) {

							if (bAlsStaffelpreisRueckpflegen == false) {

								alDto.setBRabattbehalten(Helper.boolean2Short(true));
								if (flrAnfragepositionlieferdaten.getN_anliefermenge().doubleValue() <= 1
										|| (bMengenInStaffelUebernehmen == false
												&& bAlsStaffelpreisRueckpflegen == false)) {
									alDto.setFRabatt((double) 0);
									alDto.setNEinzelpreis(anlieferPreisInLieferantenwaehrung);
									alDto.setNNettopreis(anlieferPreisInLieferantenwaehrung);
								}
								alDto.setCArtikelnrlieferant(anfposliefDto.getCArtikelnrlieferant());
								alDto.setCBezbeilieferant(anfposliefDto.getCBezbeilieferant());
								alDto.setNVerpackungseinheit(anfposliefDto.getNVerpackungseinheit());
								alDto.setCAngebotnummer(anfrageDto.getCAngebotnummer());
								alDto.setZertifikatartIId(anfposliefDto.getZertifikatartIId());
								alDto.setIWiederbeschaffungszeit(anfposliefDto.getIAnlieferzeit());

								alDto.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

								if (anfposliefDto.getNMindestbestellmenge() != null) {
									alDto.setFMindestbestelmenge(anfposliefDto.getNMindestbestellmenge().doubleValue());
								} else {
									alDto.setFMindestbestelmenge(null);
								}

								if (anfposliefDto.getNStandardmenge() != null) {
									alDto.setFStandardmenge(anfposliefDto.getNStandardmenge().doubleValue());
								} else {
									alDto.setFStandardmenge(null);
								}

								alDto.setTPreisgueltigab(tPreisgueltigab);
								alDto.setTPreisgueltigbis(anfrageDto.getTAngebotgueltigbis());

								updateArtikellieferant(alDto, theClientDto);
							}

						} else {
							alDto = new ArtikellieferantDto();
							alDto.setArtikelIId(anfposDto.getArtikelIId());
							alDto.setLieferantIId(anfrageDto.getLieferantIIdAnfrageadresse());
							alDto.setBRabattbehalten(Helper.boolean2Short(true));

							// SP5327
							if (flrAnfragepositionlieferdaten.getN_anliefermenge().doubleValue() <= 1
									|| (bMengenInStaffelUebernehmen == false
											&& bAlsStaffelpreisRueckpflegen == false)) {
								alDto.setFRabatt((double) 0);
								alDto.setNEinzelpreis(anlieferPreisInLieferantenwaehrung);
								alDto.setNNettopreis(anlieferPreisInLieferantenwaehrung);
							}

							alDto.setCArtikelnrlieferant(anfposliefDto.getCArtikelnrlieferant());
							alDto.setCBezbeilieferant(anfposliefDto.getCBezbeilieferant());
							alDto.setNVerpackungseinheit(anfposliefDto.getNVerpackungseinheit());
							alDto.setCAngebotnummer(anfrageDto.getCAngebotnummer());

							if (anfposliefDto.getNMindestbestellmenge() != null) {
								alDto.setFMindestbestelmenge(anfposliefDto.getNMindestbestellmenge().doubleValue());
							} else {
								alDto.setFMindestbestelmenge(null);
							}

							if (anfposliefDto.getNStandardmenge() != null) {
								alDto.setFStandardmenge(anfposliefDto.getNStandardmenge().doubleValue());
							} else {
								alDto.setFStandardmenge(null);
							}

							alDto.setTPreisgueltigab(tPreisgueltigab);

							alDto.setIWiederbeschaffungszeit(anfposliefDto.getIAnlieferzeit());

							alDto.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

							alDto.setTPreisgueltigbis(anfrageDto.getTAngebotgueltigbis());
							Integer aLiefIId = createArtikellieferant(alDto, theClientDto);
							alDto.setIId(aLiefIId);
						}

						if (bAlsStaffelpreisRueckpflegen == true) {

							if (alDto.getNEinzelpreis() != null) {

								BigDecimal bdRabattsumme = alDto.getNEinzelpreis()
										.subtract(anlieferPreisInLieferantenwaehrung);

								BigDecimal bdRabattsatz = new BigDecimal(0);
								if (alDto.getNEinzelpreis().doubleValue() != 0) {
									bdRabattsatz = bdRabattsumme
											.divide(alDto.getNEinzelpreis(), 4, BigDecimal.ROUND_HALF_EVEN)
											.movePointRight(2);

								} else {
									bdRabattsatz = new BigDecimal(100);
								}

								Query q = em.createNamedQuery(
										"ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
								q.setParameter(1, alDto.getIId());
								q.setParameter(2, flrAnfragepositionlieferdaten.getN_anliefermenge());
								q.setParameter(3, tPreisgueltigab);
								Artikellieferantstaffel artikellieferantstaffel = null;
								try {
									artikellieferantstaffel = (Artikellieferantstaffel) q.getSingleResult();

									artikellieferantstaffel
											.setNMenge(flrAnfragepositionlieferdaten.getN_anliefermenge());
									artikellieferantstaffel.setFRabatt(bdRabattsatz.doubleValue());
									artikellieferantstaffel.setNNettopreis(anlieferPreisInLieferantenwaehrung);
									artikellieferantstaffel.setTPreisgueltigbis(alDto.getTPreisgueltigbis());

									artikellieferantstaffel.setCAngebotnummer(anfrageDto.getCAngebotnummer());
									artikellieferantstaffel
											.setIWiederbeschaffungszeit(anfposliefDto.getIAnlieferzeit());

									artikellieferantstaffel
											.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

									em.merge(artikellieferantstaffel);
									em.flush();

								} catch (NoResultException e) {
									ArtikellieferantstaffelDto staffelDto = new ArtikellieferantstaffelDto();
									staffelDto.setArtikellieferantIId(alDto.getIId());
									staffelDto.setBRabattbehalten((short) 0);
									staffelDto.setNMenge(flrAnfragepositionlieferdaten.getN_anliefermenge());
									staffelDto.setFRabatt(bdRabattsatz.doubleValue());
									staffelDto.setNNettopreis(anlieferPreisInLieferantenwaehrung);

									staffelDto.setTPreisgueltigab(tPreisgueltigab);

									staffelDto.setTPreisgueltigbis(alDto.getTPreisgueltigbis());

									staffelDto.setCAngebotnummer(anfrageDto.getCAngebotnummer());
									staffelDto.setIWiederbeschaffungszeit(anfposliefDto.getIAnlieferzeit());
									staffelDto.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

									createArtikellieferantstaffel(staffelDto, theClientDto);
								}
							}
						}

					}

					if (i >= 0) {
						// Die anderen werden als Staffeln angelegt

						if (bMengenInStaffelUebernehmen == false && bAlsStaffelpreisRueckpflegen == false) {
							// SP7015 Dann Auslassen
						} else if ((bMengenInStaffelUebernehmen == false) || (bMengenInStaffelUebernehmen == true
								&& anfposliefDto.getNAnliefermenge().doubleValue() != 1)) {

							BigDecimal staffelPreisInLieferantenwaehrung = getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(anfposliefDto.getNNettogesamtpreis(),
											anfrageDto.getWaehrungCNr(), lf.getWaehrungCNr(),
											new Date(anfrageDto.getTBelegdatum().getTime()), theClientDto);

							// Rabatt berechnen
							BigDecimal rabattsatz = null;
							if (anlieferPreisInLieferantenwaehrung.doubleValue() != 0) {

								rabattsatz = new BigDecimal(1).subtract(staffelPreisInLieferantenwaehrung
										.divide(anlieferPreisInLieferantenwaehrung, 4, BigDecimal.ROUND_HALF_EVEN));
								rabattsatz = rabattsatz.multiply(new BigDecimal(100));

							} else {
								rabattsatz = new BigDecimal(100);
							}

							Query q = em.createNamedQuery(
									"ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
							q.setParameter(1, alDto.getIId());
							q.setParameter(2, flrAnfragepositionlieferdaten.getN_anliefermenge());
							q.setParameter(3, tPreisgueltigab);
							Artikellieferantstaffel artikellieferantstaffel = null;
							try {
								artikellieferantstaffel = (Artikellieferantstaffel) q.getSingleResult();

								artikellieferantstaffel.setNMenge(flrAnfragepositionlieferdaten.getN_anliefermenge());

								if (alDto.getNEinzelpreis() == null) {
									artikellieferantstaffel.setFRabatt(null);
									artikellieferantstaffel.setBRabattbehalten(Helper.boolean2Short(false));
								} else {
									artikellieferantstaffel.setFRabatt(rabattsatz.doubleValue());
								}

								artikellieferantstaffel.setNNettopreis(staffelPreisInLieferantenwaehrung);
								artikellieferantstaffel.setTPreisgueltigbis(alDto.getTPreisgueltigbis());
								artikellieferantstaffel.setCAngebotnummer(anfrageDto.getCAngebotnummer());
								artikellieferantstaffel.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

								em.merge(artikellieferantstaffel);
								em.flush();

							} catch (NoResultException e) {
								ArtikellieferantstaffelDto staffelDto = new ArtikellieferantstaffelDto();
								staffelDto.setArtikellieferantIId(alDto.getIId());
								staffelDto.setBRabattbehalten((short) 0);
								staffelDto.setNMenge(flrAnfragepositionlieferdaten.getN_anliefermenge());
								if (alDto.getNEinzelpreis() != null) {
									staffelDto.setFRabatt(rabattsatz.doubleValue());
								}
								staffelDto.setNNettopreis(staffelPreisInLieferantenwaehrung);
								staffelDto.setTPreisgueltigab(tPreisgueltigab);
								staffelDto.setTPreisgueltigbis(alDto.getTPreisgueltigbis());
								staffelDto.setCAngebotnummer(anfrageDto.getCAngebotnummer());
								staffelDto.setAnfragepositionlieferdatenIId(anfragepositionlieferdatenIId);

								staffelDto
										.setIWiederbeschaffungszeit(flrAnfragepositionlieferdaten.getI_anlieferzeit());

								createArtikellieferantstaffel(staffelDto, theClientDto);
							}
						}
					}

					i++;
				}
			}

			if (alDto != null && bLieferantVorreihen == true) {
				artikellieferantAlsErstesReihen(anfposDto.getArtikelIId(), alDto.getIId());
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void artikellieferantAlsErstesReihen(Integer artikelIId, Integer artikellieferantIId) {

		Artikellieferant artikellieferantZuVerschieben = em.find(Artikellieferant.class, artikellieferantIId);

		Query query = em.createNamedQuery("ArtikellieferantfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		int i = 0;

		Artikellieferant erster = null;

		while (iterator.hasNext()) {
			Artikellieferant artikellieferant = (Artikellieferant) iterator.next();

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

	public Integer createArtikellieferant(ArtikellieferantDto artikellieferantDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		int iNachkommastellenPreis = 4;
		try {
			iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (artikellieferantDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikellieferantDto == null"));
		}
		if (artikellieferantDto.getArtikelIId() == null || artikellieferantDto.getLieferantIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL, new Exception(
					"artikellieferantDto.getArtikelIId() == null || artikellieferantDto.getPartnerIIdLieferant() == null"));
		}
		if (artikellieferantDto.getTPreisgueltigab() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("artikellieferantDto.getDPreisgueltigab() == null"));
		}

		if (artikellieferantDto.getGebindeIId() != null && artikellieferantDto.getNGebindemenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GEBINDEMENGE_ERFORDERLICH,
					new Exception("FEHLER_GEBINDEMENGE_ERFORDERLICH"));
		}

		if (artikellieferantDto.getGebindeIId() == null) {
			artikellieferantDto.setNGebindemenge(null);
		}

		// SP5732
		if (artikellieferantDto.getNEinzelpreis() == null) {
			artikellieferantDto.setFRabatt(null);
			artikellieferantDto.setNNettopreis(null);
		}

		artikellieferantDto.setTPreisgueltigab(Helper.cutTimestamp(artikellieferantDto.getTPreisgueltigab()));
		artikellieferantDto.setTPreisgueltigbis(Helper.cutTimestamp(artikellieferantDto.getTPreisgueltigbis()));
		artikellieferantDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikellieferantDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		try {
			// duplicateunique: Pruefung: Artikellieferant bereits vorhanden.

			if (artikellieferantDto.getGebindeIId() != null) {
				Query query = em
						.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabGebindeIId");
				query.setParameter(1, artikellieferantDto.getArtikelIId());
				query.setParameter(2, artikellieferantDto.getLieferantIId());
				query.setParameter(3, artikellieferantDto.getTPreisgueltigab());
				query.setParameter(4, artikellieferantDto.getGebindeIId());
				Artikellieferant doppelt = (Artikellieferant) query.getSingleResult();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_ARTIKELLIEFERANT.UK"));
			} else {
				Query query = em.createNamedQuery(
						"ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabGebindeIIdIsNull");
				query.setParameter(1, artikellieferantDto.getArtikelIId());
				query.setParameter(2, artikellieferantDto.getLieferantIId());
				query.setParameter(3, artikellieferantDto.getTPreisgueltigab());

				Artikellieferant doppelt = (Artikellieferant) query.getSingleResult();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_ARTIKELLIEFERANT.UK"));
			}

		} catch (NoResultException ex) {

		}

		// PJ 16931 Daten des vorherigen uebernehmen
		if (artikellieferantDto.getCArtikelnrlieferant() == null || artikellieferantDto.getCBezbeilieferant() == null) {
			ArtikellieferantDto alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					artikellieferantDto.getArtikelIId(), artikellieferantDto.getLieferantIId(),
					new java.sql.Date(artikellieferantDto.getTPreisgueltigab().getTime()),
					artikellieferantDto.getGebindeIId(), theClientDto);

			if (alDto != null) {
				if (artikellieferantDto.getCArtikelnrlieferant() == null) {
					artikellieferantDto.setCArtikelnrlieferant(alDto.getCArtikelnrlieferant());
				}
				if (artikellieferantDto.getCBezbeilieferant() == null) {
					artikellieferantDto.setCBezbeilieferant(alDto.getCBezbeilieferant());
				}
				if (artikellieferantDto.getNVerpackungseinheit() == null) {
					artikellieferantDto.setNVerpackungseinheit(alDto.getNVerpackungseinheit());
				}
				if (artikellieferantDto.getIWiederbeschaffungszeit() == null) {
					artikellieferantDto.setIWiederbeschaffungszeit(alDto.getIWiederbeschaffungszeit());
				}
				if (artikellieferantDto.getFMindestbestelmenge() == null) {
					artikellieferantDto.setFMindestbestelmenge(alDto.getFMindestbestelmenge());
				}
				if (artikellieferantDto.getFStandardmenge() == null) {
					artikellieferantDto.setFStandardmenge(alDto.getFStandardmenge());
				}

			}

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELLIEFERANT);
			artikellieferantDto.setIId(pk);
			Query queryNext = em.createNamedQuery("ArtikellieferantejbSelectNextReihung");
			queryNext.setParameter(1, artikellieferantDto.getArtikelIId());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			artikellieferantDto.setISort(i);
			Artikellieferant artikellieferant = new Artikellieferant(artikellieferantDto.getArtikelIId(),
					artikellieferantDto.getLieferantIId(), artikellieferantDto.getTPreisgueltigab(),
					artikellieferantDto.getISort(), artikellieferantDto.getIId(),
					artikellieferantDto.getPersonalIIdAendern(), artikellieferantDto.getTAendern());
			em.persist(artikellieferant);
			em.flush();

			if (artikellieferantDto.getBHerstellerbez() == null) {
				artikellieferantDto.setBHerstellerbez(artikellieferant.getBHerstellerbez());
			}
			if (artikellieferantDto.getBWebshop() == null) {
				artikellieferantDto.setBWebshop(artikellieferant.getBWebshop());
			}
			if (artikellieferantDto.getBNichtLieferbar() == null) {
				artikellieferantDto.setBNichtLieferbar(artikellieferant.getBNichtLieferbar());
			}

			setArtikellieferantFromArtikellieferantDto(artikellieferant, artikellieferantDto, iNachkommastellenPreis);

			// PJ20587 VK-Basis Updaten
			if (artikellieferantDto.getNNettopreis() != null) {
				automatischesVKPreisbasisUpdate(artikellieferantDto, theClientDto);
			}

			// PJ21824 bei unmittelbar vorherigen das gueltig bis setzen

			Timestamp tUnmittelbarDavor = new Timestamp(
					Helper.cutTimestamp(artikellieferantDto.getTPreisgueltigab()).getTime() - 100);

			ArtikellieferantDto alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					artikellieferantDto.getArtikelIId(), artikellieferantDto.getLieferantIId(),
					new java.sql.Date(tUnmittelbarDavor.getTime()), artikellieferantDto.getGebindeIId(), theClientDto);

			if (alDto != null && alDto.getTPreisgueltigbis() == null
					&& alDto.getTPreisgueltigab().before(tUnmittelbarDavor)) {
				alDto.setTPreisgueltigbis(tUnmittelbarDavor);
				updateArtikellieferant(alDto, theClientDto);
			}

			return artikellieferantDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

	}

	public void removeArtikellieferant(ArtikellieferantDto dto) throws EJBExceptionLP {
		myLogger.entry();
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}
		// try {
		Artikellieferant toRemove = em.find(Artikellieferant.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikellieferant Es gibt keine iid " + dto.getIId()
							+ "\nArtikellieferantDto.toString(): " + dto.toString());
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
	 * @param iiDLieferant1 PK des ersten Lieferanten
	 * @param iIdLieferant2 PK des zweiten Lieferanten
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void vertauscheArtikellieferanten(Integer iiDLieferant1, Integer iIdLieferant2, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Artikellieferant oLieferant1 = em.find(Artikellieferant.class, iiDLieferant1);
		if (oLieferant1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheArtikellieferanten. Es gibt keinen Lieferanten mit iid1 " + iiDLieferant1);
		}

		Artikellieferant oLieferant2 = em.find(Artikellieferant.class, iIdLieferant2);
		if (oLieferant2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheArtikellieferanten. Es gibt keinen Lieferanten mit iid2 " + iIdLieferant2);
		}

		Integer iSort1 = oLieferant1.getISort();
		Integer iSort2 = oLieferant2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oLieferant2.setISort(new Integer(-1));
		oLieferant1.setISort(iSort2);
		oLieferant2.setISort(iSort1);

		em.merge(oLieferant1);

		em.merge(oLieferant2);
		em.flush();
		try {
			// PJ20587
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERKAUFSPREIS_AUTOMATIK_LIEF1PREIS);

			BigDecimal bdAufschlagInProzentAusParameter = (BigDecimal) parameter.getCWertAsObject();

			Artikel artikel = em.find(Artikel.class, oLieferant1.getArtikelIId());
			Artgru artrgu = null;

			BigDecimal bdAufschlagInProzentAusArtikelgruppe = null;
			if (artikel.getArtgruIId() != null) {
				artrgu = em.find(Artgru.class, artikel.getArtgruIId());
				bdAufschlagInProzentAusArtikelgruppe = artrgu.getNEkpreisaufschlag();
			}

			if (bdAufschlagInProzentAusParameter.doubleValue() > -1
					|| (bdAufschlagInProzentAusParameter.doubleValue() == -1
							&& bdAufschlagInProzentAusArtikelgruppe != null
							&& bdAufschlagInProzentAusArtikelgruppe.doubleValue() > -1)) {

				ArtikellieferantDto[] dtos = artikellieferantFindByArtikelIId(oLieferant2.getArtikelIId(),
						theClientDto);
				if (dtos.length > 0 && (dtos[0].getISort() == oLieferant1.getISort()
						|| dtos[0].getISort() == oLieferant2.getISort())) {
					// Wenn einer von beiden Lief 1 ist
					Integer artikelieferantIId = null;
					if (dtos[0].getISort() == oLieferant1.getISort()) {
						artikelieferantIId = oLieferant1.getIId();
					} else {
						artikelieferantIId = oLieferant2.getIId();
					}

					updateArtikellieferant(artikellieferantFindByPrimaryKey(artikelieferantIId, theClientDto),
							theClientDto);
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

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

	public void vertauscheErsatztypen(Integer iId1, Integer iId2) {

		Ersatztypen o1 = em.find(Ersatztypen.class, iId1);
		Ersatztypen o2 = em.find(Ersatztypen.class, iId2);
		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));
		o1.setISort(iSort2);
		o2.setISort(iSort1);

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

	public Integer getBasisArtikel(Integer artikelIIdZuschnittsartikel, TheClientDto theClientDto) {

		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIIdZuschnittsartikel, theClientDto);

		if (artikelDto.getCNr().contains("_") && artikelDto.getGeometrieDto() != null
				&& artikelDto.getGeometrieDto().getFBreite() != null) {

			String artikelnummerBasis = artikelDto.getCNr().substring(0, artikelDto.getCNr().lastIndexOf("_"));

			ArtikelDto artikelDtoBasis = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelnummerBasis,
					theClientDto.getMandant());

			if (artikelDtoBasis != null) {
				return artikelDtoBasis.getIId();
			}

		}
		return null;

	}

	public int ichBinZuschittOderBasisArtikel(Integer artikelIId, TheClientDto theClientDto) {
		ArtikelDto artikelDto = artikelFindByPrimaryKey(artikelIId, theClientDto);
		Integer artikelIIdBasis = getBasisArtikel(artikelIId, theClientDto);
		if (artikelIIdBasis != null) {
			return ZUSCHNITTSARTIKEL_ZUSCHNITTARTIKEL;
		} else {
			String sQuery = "SELECT count(a.i_id) FROM FLRArtikelliste a WHERE a.mandant_c_nr='"
					+ theClientDto.getMandant() + "' AND a.c_nr LIKE '" + artikelDto.getCNr() + "\\_%' AND a.i_id<>"
					+ artikelDto.getIId();

			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session.createQuery(sQuery);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			if (resultListIterator.hasNext()) {
				Long l = (Long) resultListIterator.next();
				if (l > 0) {
					return ZUSCHNITTSARTIKEL_BASISARTIKEL;
				}
			}
		}
		return ZUSCHNITTSARTIKEL_KEINER;
	}

	public void updateArtikellieferant(ArtikellieferantDto artLiefDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		updateArtikellieferantImpl(artLiefDtoI, false, theClientDto);
	}

	/**
	 * Wenn der Einzelpreis geaendert wird, muessen auch die zugehoerigen
	 * Staffelpreise upgedatet werden
	 * 
	 * @param artLiefDtoI ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public void updateArtikellieferantImpl(ArtikellieferantDto artLiefDtoI, boolean zuschnittsartikelNeuBerechnen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artLiefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artLiefDtoI == null"));
		}

		if (artLiefDtoI.getIId() == null || artLiefDtoI.getArtikelIId() == null
				|| artLiefDtoI.getLieferantIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception(
					"artLiefDtoI.getIId() == null || artLiefDtoI.getArtikelIId() == null || artLiefDtoI.getPartnerIIdLieferant() == null"));
		}

		artLiefDtoI.setTPreisgueltigab(Helper.cutTimestamp(artLiefDtoI.getTPreisgueltigab()));
		artLiefDtoI.setTPreisgueltigbis(Helper.cutTimestamp(artLiefDtoI.getTPreisgueltigbis()));

		// try {
		Artikellieferant artikellieferant = em.find(Artikellieferant.class, artLiefDtoI.getIId());
		if (artikellieferant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikellieferant. Es gibt keinen Artikellieferanten mit iid "
							+ artLiefDtoI.getIId() + "\nartliefDto.toString(): " + artLiefDtoI.toString());
		} else {

			ArtikellieferantDto dtoAlt = assembleArtikellieferantDto(artikellieferant);
			if (!dtoAlt.equals(artLiefDtoI)) {
				artLiefDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
				artLiefDtoI.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			}

		}

		// SP2009
		if (artikellieferant.getNEinzelpreis() != null && artLiefDtoI.getNEinzelpreis() == null) {
			// Einzelpreis kann nur geloescht werden, wenn keine Staffekn
			// vorhanden

			Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
			query.setParameter(1, artikellieferant.getIId());
			Collection<?> cl = query.getResultList();
			if (cl.size() > 0) {

				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Artikellieferantstaffel staffel = (Artikellieferantstaffel) it.next();

					if (Helper.short2boolean(staffel.getBRabattbehalten()) == true) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN,
								"FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN");
					}

					staffel.setFRabatt(null);

				}

			}

			// SP5732
			artLiefDtoI.setFRabatt(null);
			artLiefDtoI.setNNettopreis(null);

		}
		int iNachkommastellenPreis = 4;
		try {
			iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Wenn sich der Einzelpreis geaendert hat

		boolean bEinzelpreisGeaendert = false;
		if (artikellieferant.getNEinzelpreis() != null && artLiefDtoI.getNEinzelpreis() != null) {
			if (artikellieferant.getNEinzelpreis().doubleValue() != artLiefDtoI.getNEinzelpreis().doubleValue()) {
				bEinzelpreisGeaendert = true;
			}
		}

		if (artikellieferant.getNEinzelpreis() == null && artLiefDtoI.getNEinzelpreis() != null) {
			bEinzelpreisGeaendert = true;
		}

		if (bEinzelpreisGeaendert == true) {

			Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
			query.setParameter(1, artikellieferant.getIId());
			Collection<?> cl = query.getResultList();

			ArtikellieferantstaffelDto[] aArtLiefStafDto = assembleArtikellieferantstaffelDtos(cl);
			for (int xArtLiefStafDto = 0; xArtLiefStafDto < aArtLiefStafDto.length; xArtLiefStafDto++) {
				ArtikellieferantstaffelDto artLiefStafDto = aArtLiefStafDto[xArtLiefStafDto];
				if (artLiefDtoI.getNEinzelpreis() != null) {
					if (artLiefDtoI.getNEinzelpreis().doubleValue() != 0) {
						BigDecimal bd100 = new BigDecimal(100);

						if (Helper.short2boolean(artLiefStafDto.getBRabattbehalten()) == false) {
							BigDecimal fRabatt = (artLiefStafDto.getNNettopreis().multiply(bd100))
									.subtract(bd100.multiply(artLiefDtoI.getNEinzelpreis()))
									.divide(artLiefDtoI.getNEinzelpreis(), iNachkommastellenPreis,
											BigDecimal.ROUND_HALF_EVEN);
							fRabatt = fRabatt.negate();

							Artikellieferantstaffel artikellieferantstaffel = em.find(Artikellieferantstaffel.class,
									artLiefStafDto.getIId());
							// TODO: CG Stellen bei Rabatt
							if (artikellieferantstaffel == null) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
										"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
												+ artLiefStafDto.getIId() + "\nartlifstaffelDto.toString(): "
												+ artLiefStafDto.toString());
							}
							artikellieferantstaffel.setFRabatt(fRabatt.doubleValue());
							em.merge(artikellieferantstaffel);
							em.flush();

						} else {

							Artikellieferantstaffel artikellieferantstaffel = em.find(Artikellieferantstaffel.class,
									artLiefStafDto.getIId());
							if (artikellieferantstaffel == null) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
										"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
												+ artLiefStafDto.getIId() + "\nartlifstaffelDto.toString(): "
												+ artLiefStafDto.toString());
							}
							artikellieferantstaffel.setNNettopreis(artLiefDtoI.getNEinzelpreis()
									.subtract(Helper.getProzentWert(artLiefDtoI.getNEinzelpreis(),
											new BigDecimal(artLiefStafDto.getFRabatt()), iNachkommastellenPreis)));
							em.merge(artikellieferantstaffel);
							em.flush();

						}

					} else {
						if (Helper.short2boolean(artLiefStafDto.getBRabattbehalten()) == true) {
							Artikellieferantstaffel artikellieferantstaffel = em.find(Artikellieferantstaffel.class,
									artLiefStafDto.getIId());
							if (artikellieferantstaffel == null) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
										"Fehler bei updateArtikellieferant. Es gibt keine artiklellieferantenstaffel mit iid "
												+ artLiefStafDto.getIId() + "\nartlifstaffelDto.toString(): "
												+ artLiefStafDto.toString());
							}
							artikellieferantstaffel.setNNettopreis(new BigDecimal(0));
							em.merge(artikellieferantstaffel);
							em.flush();
						}
					}
				}
			}

		}

		setArtikellieferantFromArtikellieferantDto(artikellieferant, artLiefDtoI, iNachkommastellenPreis);

		// PJ22482
		if (zuschnittsartikelNeuBerechnen) {

			int iArtikelart = ichBinZuschittOderBasisArtikel(artLiefDtoI.getArtikelIId(), theClientDto);

			if (iArtikelart == ArtikelFac.ZUSCHNITTSARTIKEL_BASISARTIKEL) {
				preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(artLiefDtoI.getArtikelIId(),
						artLiefDtoI.getIId(), theClientDto);
			} else if (iArtikelart == ArtikelFac.ZUSCHNITTSARTIKEL_ZUSCHNITTARTIKEL) {

				preiseDesBasisartikelAndhandDesZuschnittartikelsNeuBerechnen(artLiefDtoI.getArtikelIId(),
						artLiefDtoI.getIId(), theClientDto);
			}

		}

		// PJ20587 VK-Basis Updaten
		if (artLiefDtoI.getNNettopreis() != null) {
			automatischesVKPreisbasisUpdate(artLiefDtoI, theClientDto);
		}

	}

	private void automatischesVKPreisbasisUpdate(ArtikellieferantDto artLiefDtoI, TheClientDto theClientDto) {
		// Ja ich bin erster

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_VERKAUFSPREIS_AUTOMATIK_LIEF1PREIS);

			BigDecimal bdAufschlagInProzentAusParameter = (BigDecimal) parameter.getCWertAsObject();

			Artikel artikel = em.find(Artikel.class, artLiefDtoI.getArtikelIId());
			Artgru artrgu = null;

			BigDecimal bdAufschlagInProzentAusArtikelgruppe = null;
			if (artikel.getArtgruIId() != null) {
				artrgu = em.find(Artgru.class, artikel.getArtgruIId());
				bdAufschlagInProzentAusArtikelgruppe = artrgu.getNEkpreisaufschlag();
			}

			if (bdAufschlagInProzentAusParameter.doubleValue() > -1
					|| (bdAufschlagInProzentAusParameter.doubleValue() == -1
							&& bdAufschlagInProzentAusArtikelgruppe != null
							&& bdAufschlagInProzentAusArtikelgruppe.doubleValue() > -1)) {
				// Wenn ich Lief1 bin

				ArtikellieferantDto[] dtos = artikellieferantFindByArtikelIId(artLiefDtoI.getArtikelIId(),
						theClientDto);
				if (dtos.length > 0 && dtos[0].getISort() == artLiefDtoI.getISort()) {
					BigDecimal bdAufschlagEffektiv = null;

					if (artrgu != null) {

						if (artrgu.getNEkpreisaufschlag() == null) {
							bdAufschlagEffektiv = bdAufschlagInProzentAusParameter;
						} else {

							if (artrgu.getNEkpreisaufschlag().doubleValue() == -1) {
								bdAufschlagEffektiv = null;
							} else {
								bdAufschlagEffektiv = artrgu.getNEkpreisaufschlag();
							}

						}

					} else {
						bdAufschlagEffektiv = bdAufschlagInProzentAusParameter;
					}

					if (bdAufschlagEffektiv != null && bdAufschlagEffektiv.doubleValue() > -1) {

						LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(artLiefDtoI.getLieferantIId(),
								theClientDto);

						// PJ21436
						BigDecimal basis = artLiefDtoI.getNNettopreis();
						if (artrgu != null && Helper.short2boolean(artrgu.getBAufschlagEinzelpreis())
								&& artLiefDtoI.getNEinzelpreis() != null) {
							basis = artLiefDtoI.getNEinzelpreis();
						}

						BigDecimal bdVKBasisNeu = getLocaleFac().rechneUmInAndereWaehrungZuDatum(basis,
								lfDto.getWaehrungCNr(), theClientDto.getSMandantenwaehrung(),
								Helper.cutDate(new java.sql.Date(artLiefDtoI.getTPreisgueltigab().getTime())),
								theClientDto);

						bdVKBasisNeu = bdVKBasisNeu.add(Helper.getProzentWert(bdVKBasisNeu, bdAufschlagEffektiv,
								getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant())));
						String bemerkung = "Preisupdate aus Lief1Preis Lieferant="
								+ lfDto.getPartnerDto().formatFixName1Name2();

						VkPreisfindungEinzelverkaufspreisDto dto = null;
						try {
							Query query = em.createNamedQuery(
									"VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
							query.setParameter(1, theClientDto.getMandant());
							query.setParameter(2, artLiefDtoI.getArtikelIId());
							query.setParameter(3, Helper.cutDate(new Date(artLiefDtoI.getTPreisgueltigab().getTime())));
							VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) query
									.getSingleResult();
							// Updaten

							vkPreisfindungEinzelverkaufspreis.setCBemerkung(bemerkung);
							vkPreisfindungEinzelverkaufspreis.setNVerkaufspreisbasis(bdVKBasisNeu);

						} catch (NoResultException e) {
							// Neu erstellen
							VkPreisfindungEinzelverkaufspreisDto vkPReisbasis = new VkPreisfindungEinzelverkaufspreisDto();

							vkPReisbasis.setArtikelIId(artLiefDtoI.getArtikelIId());
							vkPReisbasis.setTVerkaufspreisbasisgueltigab(
									Helper.cutDate(new Date(artLiefDtoI.getTPreisgueltigab().getTime())));
							vkPReisbasis.setCBemerkung(bemerkung);
							vkPReisbasis.setNVerkaufspreisbasis(bdVKBasisNeu);
							vkPReisbasis.setMandantCNr(theClientDto.getMandant());

							getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(vkPReisbasis, theClientDto);

						}

					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	// exccatch: hier immer EJBExceptionLP deklarieren
	public ArtikellieferantDto artikellieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(
					new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null")));
		}
		Artikellieferant artikellieferant = em.find(Artikellieferant.class, iId);
		if (artikellieferant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei artikellieferantFindByPrimaryKey. Es gibt keine iid " + iId);
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

		artikellieferantDto.setLieferantDto(
				getLieferantFac().lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId(), theClientDto));

		return artikellieferantDto;
	}

	public ArtikellieferantDto[] artikellieferantFindByArtikelIId(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
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

	public ArtikellieferantDto[] artikellieferantfindByArtikelIIdTPreisgueltigab(Integer artikelIId,
			java.sql.Date tPreisGuelitgab, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
		}
		ArtikellieferantDto[] returnArray = new ArtikellieferantDto[0];
		// PJ22121
		Collection<?> cl = null;
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GETRENNTE_EINKAUFSPREISE);
			boolean bGetrennteEinkaufspreise = (Boolean) parameter.getCWertAsObject();

			String sQuery = "SELECT al.i_id  FROM FLRArtikellieferant al WHERE al.artikel_i_id=" + artikelIId
					+ " AND al.t_preisgueltigab <='" + Helper.formatDateWithSlashes(tPreisGuelitgab)
					+ "' AND ( al.t_preisgueltigbis IS NULL OR al.t_preisgueltigbis >= '"
					+ Helper.formatDateWithSlashes(tPreisGuelitgab) + "'  )";

			if (bGetrennteEinkaufspreise) {

				sQuery += " AND al.flrlieferant.mandant_c_nr='" + theClientDto.getMandant() + "'";

			} else {

			}

			sQuery += " ORDER BY al.i_sort";

			org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query queryFLR = session.createQuery(sQuery);
			List<?> resultList = queryFLR.list();
			Iterator<?> resultListIterator = resultList.iterator();

			returnArray = new ArtikellieferantDto[resultList.size()];

			int i = 0;
			while (resultListIterator.hasNext()) {
				Integer i_id = (Integer) resultListIterator.next();

				Artikellieferant artikellieferant = em.find(Artikellieferant.class, i_id);

				returnArray[i] = assembleArtikellieferantDto(artikellieferant);
				i++;

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return returnArray;

	}

	public ArrayList<String> getEKStaffeln(Integer artikellieferantIId, TheClientDto theClientDto) {

		ArtikellieferantDto alDto = artikellieferantFindByPrimaryKey(artikellieferantIId, theClientDto);

		ArtikelDto artikelDto = artikelFindByPrimaryKey(alDto.getArtikelIId(), theClientDto);

		ArrayList<String> staffeln = new ArrayList<String>();

		List<Artikellieferantstaffel> gueltigeStaffeln = ArtikellieferantstaffelQuery
				.listByArtikellieferantIIdGueltigZuDatum(em, artikellieferantIId, getDate());

		int iNachkommastellenPreis = 2;
		try {
			iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		String gb = getTextRespectUISpr("artikel.ekpreis.gueltigbis", theClientDto.getMandant(),
				theClientDto.getLocUi());

		for (Artikellieferantstaffel entity : gueltigeStaffeln) {

			// Aufgrund von SP8731 wird der zu HEUTE gueltige Preis angezeigt
			ArtikellieferantDto alDtoStaffel = getArtikelEinkaufspreis(alDto.getArtikelIId(), alDto.getLieferantIId(),
					entity.getNMenge(), theClientDto.getSMandantenwaehrung(), getDate(), theClientDto);

			String menge = Helper.formatZahl(entity.getNMenge(), 2, theClientDto.getLocUi());

			menge = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(menge, 8, true);

			String preis = "";
			if (alDtoStaffel != null && alDtoStaffel.getNNettopreis() != null) {
				preis = Helper.formatZahl(alDtoStaffel.getNNettopreis(), iNachkommastellenPreis,
						theClientDto.getLocUi());

			}

			preis = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(preis, 15, true);

			// SP8501 Ist der Preis heute noch der gleiche
			ArtikellieferantDto alDtoStaffelPruefung = getArtikelEinkaufspreis(alDto.getArtikelIId(),
					alDto.getLieferantIId(), entity.getNMenge(), theClientDto.getSMandantenwaehrung(), getDate(),
					theClientDto);

			// Wenn zwischen Gueltigab des Artikellieferant und Heute noch ein anderer Preis
			// definiert ist, dann ist dieser nicht gueltig
			String sgueltig = "";
			if (alDtoStaffel != null && alDtoStaffelPruefung != null
					&& !alDtoStaffel.getIId().equals(alDtoStaffelPruefung.getIId())) {

				sgueltig = "<font color=\"#FF0000\"> " + gb + " "
						+ Helper.formatDatum(alDtoStaffelPruefung.getTPreisgueltigab(), theClientDto.getLocUi())
						+ "</font>";
			}

			String s = "<html><body><font color=\"#000000\">" + menge + " "
					+ Helper.fitString2Length(artikelDto.getEinheitCNr().trim(), 5, ' ') + preis + " "
					+ theClientDto.getSMandantenwaehrung() + sgueltig + "</font></body></html>";

			if (!staffeln.contains(s)) {
				staffeln.add(s);
			}

		}

		return staffeln;
	}

	public ArrayList<String> getTruTopsMetadaten(ArtikelId artikelId, TheClientDto theClientDto) {

		List<ArtikelTruTopsMetadatenDto> lMetadaten = artikelTruTopsMetadatenFindByArtikelId(artikelId);

		ArrayList<String> zeilen = new ArrayList<String>();
		String ueberschrift = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(getTextRespectUISpr(
				"artikel.trumpf.metadaten.anlagezeitpunkt", theClientDto.getMandant(), theClientDto.getLocUi()), 20);

		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
				getTextRespectUISpr("artikel.trumpf.metadaten.aenderungszeitpunkt", theClientDto.getMandant(),
						theClientDto.getLocUi()),
				20);
		ueberschrift += Helper
				.fitString2LengthHTMLBefuelltMitLeerzeichen(getTextRespectUISpr("artikel.trumpf.metadaten.groesse",
						theClientDto.getMandant(), theClientDto.getLocUi()) + " ", 8, true);
		ueberschrift += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(getTextRespectUISpr(
				"artikel.trumpf.metadaten.name", theClientDto.getMandant(), theClientDto.getLocUi()), 20);

		ueberschrift = "<html><body><font color=\"#000000\">" + ueberschrift + "</font></body></html>";

		zeilen.add(ueberschrift);
		zeilen.add("-------------------------------------------------------------------------------------------");

		for (ArtikelTruTopsMetadatenDto dto : lMetadaten) {

			String anlage = "";
			if (dto.getTCreation() != null) {
				anlage = Helper.formatTimestamp(dto.getTCreation(), theClientDto.getLocUi());
			}
			String zeile = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(anlage, 20);

			String aendern = "";
			if (dto.getTModification() != null) {
				aendern = Helper.formatTimestamp(dto.getTModification(), theClientDto.getLocUi());
			}

			zeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(aendern, 20);

			String groesse = "";
			if (dto.getISize() != null) {
				groesse = dto.getISize() + "";
			}

			zeile += Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(groesse + " ", 8, true);

			zeile += dto.getCFilename();

			String s = "<html><body><font color=\"#000000\">" + zeile + "</font></body></html>";

			zeilen.add(s);

		}

		return zeilen;
	}

	public ArrayList<KeyValue> getListeDerArtikellieferanten(Integer bestellvorschlagIId, BigDecimal nMenge,
			TheClientDto theClientDto) {

		ArrayList<KeyValue> m = new ArrayList<KeyValue>();

		BestellvorschlagDto bvDto = null;

		int iGesamtstellenPreis = 10;
		int iNachkommastellenPreis = 2;
		try {
			bvDto = getBestellvorschlagFac().bestellvorschlagFindByPrimaryKey(bestellvorschlagIId);
			iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

			iGesamtstellenPreis = 6 + iNachkommastellenPreis;

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		boolean bZentralerArtikelstamm = getMandantFac().hatZusatzfunktionZentralerArtikelstamm(theClientDto);

		ArtikellieferantDto[] artikellieferantDtos = getArtikelFac().artikellieferantfindByArtikelIIdTPreisgueltigab(
				bvDto.getIArtikelId(), new java.sql.Date(System.currentTimeMillis()), theClientDto);

		ArrayList liste = new ArrayList();

		HashSet hsBereitsBearbeiteteLieferanten = new HashSet();

		for (int i = 0; i < artikellieferantDtos.length; i++) {
			ArtikellieferantDto artikellieferantDto = artikellieferantDtos[i];
			LieferantDto lfDto = getLieferantFac()
					.lieferantFindByPrimaryKeySmall(artikellieferantDto.getLieferantIId());
			if (!lfDto.getMandantCNr().equals(theClientDto.getMandant())) {
				if (!bZentralerArtikelstamm) {
					continue;
				}
			}

			// SP7447
			if (Helper.short2boolean(artikellieferantDto.getBNichtLieferbar())) {
				continue;
			}

			if (!hsBereitsBearbeiteteLieferanten
					.contains(artikellieferantDto.getLieferantIId() + " " + artikellieferantDto.getGebindeIId())) {

				hsBereitsBearbeiteteLieferanten
						.add(artikellieferantDto.getLieferantIId() + " " + artikellieferantDto.getGebindeIId());

				// Fuer jeden Artikellieferant den Einzelpreis holen:

				ArtikellieferantDto alDto = getArtikelEinkaufspreisMitOptionGebinde(bvDto.getIArtikelId(),
						artikellieferantDto.getLieferantIId(), BigDecimal.ONE, theClientDto.getSMandantenwaehrung(),
						new Date(System.currentTimeMillis()), artikellieferantDto.getGebindeIId(), theClientDto);
				// Wenn enizelpreis vorhanden

				if (alDto == null) {
					alDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(bvDto.getIArtikelId(),
							artikellieferantDto.getLieferantIId(), new Date(System.currentTimeMillis()),
							artikellieferantDto.getGebindeIId(), theClientDto);
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
						ArtikellieferantDto alDtoLinksTemp = getArtikelEinkaufspreisMitOptionGebinde(
								bvDto.getIArtikelId(), artikellieferantDto.getLieferantIId(), nMenge,
								theClientDto.getSMandantenwaehrung(), new Date(System.currentTimeMillis()),
								artikellieferantDto.getGebindeIId(), theClientDto);

						if (alDtoLinksTemp != null && alDtoLinksTemp.getNStaffelmenge() != null) {
							alDtoLinks = alDtoLinksTemp;
						}
					}

					// NaechsthoehereStaffel
					Query q = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeGroesser");
					q.setParameter(1, alDto.getIId());
					q.setParameter(2, nMenge);
					q.setParameter(3, Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

					Collection c = q.getResultList();

					if (c.size() > 0) {
						Artikellieferantstaffel als = (Artikellieferantstaffel) c.iterator().next();

						alDtoRechts = getArtikelEinkaufspreisMitOptionGebinde(bvDto.getIArtikelId(),
								artikellieferantDto.getLieferantIId(), als.getNMenge(),
								theClientDto.getSMandantenwaehrung(), new Date(System.currentTimeMillis()),
								artikellieferantDto.getGebindeIId(), theClientDto);

						if (alDtoRechts != null && alDtoRechts.getNStaffelmenge() != null) {

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
		}

		int iZeileGuenstigsterPreis = -1;
		int iZeileSchnellsteWBZ = -1;
		Integer schnellstewbz = null;
		BigDecimal guenstigsterPreis = null;
		for (int i = 0; i < liste.size(); i++) {

			Object[] oZeile = (Object[]) liste.get(i);
			ArtikellieferantDto alDtoLinks = (ArtikellieferantDto) oZeile[0];

			// WBZ
			if (schnellstewbz == null && alDtoLinks.getIWiederbeschaffungszeit() != null) {
				iZeileSchnellsteWBZ = i;
				schnellstewbz = alDtoLinks.getIWiederbeschaffungszeit();
			}

			if (schnellstewbz != null && alDtoLinks.getIWiederbeschaffungszeit() != null
					&& alDtoLinks.getIWiederbeschaffungszeit() < schnellstewbz) {
				iZeileSchnellsteWBZ = i;
				schnellstewbz = alDtoLinks.getIWiederbeschaffungszeit();
			}
			// Guenstigster Preis

			if (guenstigsterPreis == null) {

				if (alDtoLinks.getNNettopreis() != null) {

					guenstigsterPreis = alDtoLinks.getLief1Preis();
					iZeileGuenstigsterPreis = i;
				}
			} else {
				if (alDtoLinks.getNNettopreis() != null) {

					if (alDtoLinks.getLief1Preis().doubleValue() < guenstigsterPreis.doubleValue()) {
						guenstigsterPreis = alDtoLinks.getLief1Preis();
						iZeileGuenstigsterPreis = i;
					}
				}
			}

		}

		int iZeileGuenstigsterMoeglicherWert = -1;
		if (guenstigsterPreis != null) {

			BigDecimal guenstigsterRealerWert = bvDto.getNZubestellendeMenge().multiply(guenstigsterPreis);

			BigDecimal guenstigsterMoegicherWert = null;

			for (int i = 0; i < liste.size(); i++) {

				Object[] oZeile = (Object[]) liste.get(i);

				ArtikellieferantDto alDtoRechts = (ArtikellieferantDto) oZeile[1];

				if (alDtoRechts != null && alDtoRechts.getNStaffelmenge() != null
						&& alDtoRechts.getLief1Preis() != null) {

					BigDecimal temp = alDtoRechts.getNStaffelmenge().multiply(alDtoRechts.getLief1Preis());

					if (guenstigsterMoegicherWert == null
							&& temp.doubleValue() < guenstigsterRealerWert.doubleValue()) {
						guenstigsterMoegicherWert = temp;
						iZeileGuenstigsterMoeglicherWert = i;
					} else {
						if (temp.doubleValue() < guenstigsterRealerWert.doubleValue()
								&& temp.doubleValue() < guenstigsterMoegicherWert.doubleValue()) {
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

			LieferantDto lDto = getLieferantFac().lieferantFindByPrimaryKey(alDtoLinks.getLieferantIId(), theClientDto);

			String lieferant = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(lDto.getPartnerDto().getCKbez(), 20);

			String mandant = "";
			if (bZentralerArtikelstamm) {
				mandant = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(lDto.getMandantCNr(), 4);
			}

			String sperre = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(" ", 1);
			if (lDto.getTBestellsperream() != null) {
				sperre = "<span style=\"background-color: #FF0000\">"
						+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("X", 1) + "</span>";
			}

			if (bvDto.getILieferantId() != null && bvDto.getILieferantId().equals(lDto.getIId())) {

				lieferant = "<span style=\"background-color: #C0C0C0\">" + lieferant + "</span>";

			}

			String beurteilung = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(" ", 2);
			try {
				LieferantbeurteilungDto[] bDtos = getLieferantFac()
						.lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(lDto.getIId(),
								new java.sql.Timestamp(System.currentTimeMillis()));

				if (bDtos != null && bDtos.length > 0) {

					beurteilung = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(bDtos[0].getCKlasse() + " ", 2);

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			String gebinde = "";
			if (alDtoLinks.getGebindeIId() != null) {
				gebinde = gebindeFindByPrimaryKey(alDtoLinks.getGebindeIId()).getCBez();
			}

			gebinde = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(gebinde, 10, false);

			String vpe = "";
			if (alDtoLinks.getNVerpackungseinheit() != null) {
				vpe = Helper.formatZahl(alDtoLinks.getNVerpackungseinheit(), 0, theClientDto.getLocUi());

			}

			vpe = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(vpe, 5, true);

			String mindbestmenge = "";
			if (alDtoLinks.getFMindestbestelmenge() != null) {
				mindbestmenge = Helper.formatZahl(alDtoLinks.getFMindestbestelmenge(), 0, theClientDto.getLocUi());

			}

			mindbestmenge = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(mindbestmenge, 6, true);

			// Menge links
			String mengeLinks = null;

			if (alDtoLinks.getNStaffelmenge() != null) {
				mengeLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(alDtoLinks.getNStaffelmenge(), 0, theClientDto.getLocUi()), 6, true);
			} else {
				mengeLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(BigDecimal.ONE, 0, theClientDto.getLocUi()), 6, true);
			}

			// Preis

			String preisLinks = "";

			if (alDtoLinks.getLief1Preis() == null) {
				preisLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("", iGesamtstellenPreis, true);
			} else {
				preisLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(alDtoLinks.getLief1Preis(), iNachkommastellenPreis, theClientDto.getLocUi()),
						iGesamtstellenPreis, true);
			}

			if (i == iZeileGuenstigsterPreis) {
				preisLinks = "<span style=\"background-color: #00FF00\">" + preisLinks + "</span>";
			}

			// Wiedebeschaffungszeit
			String wbzLinks = "";

			if (alDtoLinks.getIWiederbeschaffungszeit() != null) {
				wbzLinks = alDtoLinks.getIWiederbeschaffungszeit() + "";
			}
			wbzLinks = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(wbzLinks, 2, true);

			if (i == iZeileSchnellsteWBZ) {
				wbzLinks = "<span style=\"background-color: #00FF00\">" + wbzLinks + "</span>";
			}

			// Preisgueltigkeit
			String preisgueltikeitLinks = Helper.formatDatum(alDtoLinks.getTPreisgueltigab(), theClientDto.getLocUi());

			String sZeile = mandant + sperre + beurteilung + lieferant + " VPE:" + vpe + " Min:" + mindbestmenge
					+ " Mng:" + mengeLinks + " Prs:" + preisLinks + " WBZ:" + wbzLinks + "Ab:" + preisgueltikeitLinks
					+ " Gebinde:" + gebinde;

			ArtikellieferantDto alDtoRechts = (ArtikellieferantDto) oZeile[1];

			if (alDtoRechts != null) {

				// Menge links
				String mengeRechts = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(alDtoRechts.getNStaffelmenge(), 0, theClientDto.getLocUi()), 6, true);

				// Preis

				String preisRechts = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
						Helper.formatZahl(alDtoRechts.getLief1Preis(), iNachkommastellenPreis, theClientDto.getLocUi()),
						iGesamtstellenPreis, true);

				if (i == iZeileGuenstigsterMoeglicherWert) {
					preisRechts = "<span style=\"background-color: #FFFF00\">" + preisRechts + "</span>";
				}

				// Wiedebeschaffungszeit
				String wbzRechts = "";

				if (alDtoRechts.getIWiederbeschaffungszeit() != null) {
					wbzRechts = alDtoRechts.getIWiederbeschaffungszeit() + "";
				}
				wbzRechts = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(wbzRechts, 2, true);

				// Preisgueltigkeit
				String preisgueltikeitRechts = Helper.formatDatum(alDtoRechts.getTPreisgueltigab(),
						theClientDto.getLocUi());

				sZeile += " Mng:" + mengeRechts + " Prs:" + preisRechts + " WBZ:" + wbzRechts + "Ab:"
						+ preisgueltikeitRechts;

			}

			String s = "<html><body><font color=\"#000000\">" + sZeile + "</font></body></html>";

			KeyValue mZeile = new KeyValue(alDtoLinks, s);
			m.add(mZeile);

		}
		return m;
	}

	public ArrayList<String> getVerfuegbarkeitErsatztypen(Integer artikelIId, TheClientDto theClientDto) {

		ArrayList<String> m = new ArrayList<String>();

		BestellvorschlagDto bvDto = null;

		int iNachkommastellenMenge = 3;
		int iLaengeArtikelnummer = 12;
		try {

			iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
			iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

			// Alle Artikel suchen

			ArtikelDto aDto = artikelFindByPrimaryKey(artikelIId, theClientDto);
			String praefix = aDto.getCNr();

			if (aDto.getCNr().length() >= iLaengeArtikelnummer) {
				praefix = aDto.getCNr().substring(0, iLaengeArtikelnummer);
			}
			Session sessionPreisliste = FLRSessionFactory.getFactory().openSession();

			String sQueryPreisliste = "SELECT artikel FROM FLRArtikel artikel WHERE artikel.c_nr<>'" + aDto.getCNr()
					+ "' AND artikel.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND artikel.b_lagerbewirtschaftet=1 AND artikel.c_nr LIKE '" + praefix
					+ "%' ORDER BY artikel.c_nr ASC";

			org.hibernate.Query queryPreisliste = FLRSessionFactory.getFactory().openSession()
					.createQuery(sQueryPreisliste);

			List<?> resultListPreisliste = queryPreisliste.list();

			Iterator<?> resultListIteratorPreisliste = resultListPreisliste.iterator();

			String sUEBERSCHRIFT = "<html><body><font color=\"#000000\">"
					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen("", 5, true)
					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							getTextRespectUISpr("lp.lagerstand", theClientDto.getMandant(), theClientDto.getLocUi()),
							15, true)
					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							getTextRespectUISpr("lp.verfuegbar", theClientDto.getMandant(), theClientDto.getLocUi()),
							15, true)

					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							getTextRespectUISpr("lp.infertigung", theClientDto.getMandant(), theClientDto.getLocUi()),
							15, true)
					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							getTextRespectUISpr("lp.bestellt", theClientDto.getMandant(), theClientDto.getLocUi()), 15,
							true)

					+ "</font></body></html>";
			m.add(sUEBERSCHRIFT);

			while (resultListIteratorPreisliste.hasNext()) {

				FLRArtikel flrArtikel = (FLRArtikel) resultListIteratorPreisliste.next();

				if (flrArtikel.getC_nr().length() > iLaengeArtikelnummer) {

					String hersteller = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							flrArtikel.getC_nr().substring(iLaengeArtikelnummer), 5, false);

					BigDecimal lagerstand = getLagerFac().getLagerstandAllerLagerEinesMandanten(flrArtikel.getI_id(),
							false, theClientDto);

					BigDecimal reservierungen = getReservierungFac().getAnzahlReservierungen(flrArtikel.getI_id(),
							theClientDto);
					BigDecimal fehlmengen = getFehlmengeFac().getAnzahlFehlmengeEinesArtikels(flrArtikel.getI_id(),
							theClientDto);

					BigDecimal bestellt = getArtikelbestelltFac().getAnzahlBestellt(flrArtikel.getI_id());
					BigDecimal infertigung = getFertigungFac().getAnzahlInFertigung(flrArtikel.getI_id(), theClientDto);

					BigDecimal verfuegbar = lagerstand.subtract(reservierungen).subtract(fehlmengen);

					String sLagerstand = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							Helper.formatZahl(lagerstand, iNachkommastellenMenge, theClientDto.getLocUi()), 15, true);

					// Alle Artikel suchen
					String sVerfuegbar = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							Helper.formatZahl(verfuegbar, iNachkommastellenMenge, theClientDto.getLocUi()), 15, true);

					String sBestellt = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							Helper.formatZahl(bestellt, iNachkommastellenMenge, theClientDto.getLocUi()), 15, true);
					String sInFertigung = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							Helper.formatZahl(infertigung, iNachkommastellenMenge, theClientDto.getLocUi()), 15, true);

					String sZeile = hersteller + sLagerstand + sVerfuegbar + sInFertigung + sBestellt;

					String s = "<html><body><font color=\"#000000\">" + sZeile + "</font></body></html>";

					m.add(s);
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return m;
	}

	public ArtikellieferantDto[] artikellieferantFindByLieferantIId(Integer lieferantIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("lieferantIId == null"));
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

	public ArtikellieferantDto[] artikellieferantFindByLieferantIIdOhneExc(Integer lieferantIId,
			TheClientDto theClientDto) {
		if (lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("lieferantIId == null"));
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<String> wandleHandeingabeInBestehendenArtikelUm(Integer positionIId, int iArt, Integer artikelIId,
			TheClientDto theClientDto) {

		ArrayList<String> returnWerte = new ArrayList<String>();

		try {

			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant());

			if (iArt == HANDARTIKEL_UMWANDELN_AUFTRAG) {
				AuftragpositionDto posDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(positionIId);

				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(posDto.getBelegIId());

				KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse(),
						theClientDto);

				ParametermandantDto parameterPositionskontierung = null;
				try {
					parameterPositionskontierung = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				MwstsatzDto mwstsatzDto = null;

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();
				// Mwstsatz aus Artikel
				if (bDefaultMwstsatzAusArtikel) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
					/*
					 * mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(artikelDto.getMwstsatzbezIId(),
					 * theClientDto);
					 */
					mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(artikelDto.getMwstsatzbezIId(),
							auftragDto.getTBelegdatum());
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
						mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(
								mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz(), auftragDto.getTBelegdatum());
					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}

					if (kdDto.getMwstsatzbezIId() != null) {
						/*
						 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.
						 * getMwstsatzbezIId(), theClientDto);
						 */
						mwstsatzDto = getMandantFac().mwstsatzFindZuDatum(kdDto.getMwstsatzbezIId(),
								auftragDto.getTBelegdatum());
					}

				}

				if (mwstsatzDto != null) {

					if (!mwstsatzDto.getIId().equals(posDto.getMwstsatzIId())) {
						// Meldung

						MwstsatzDto mwstsatzDtoVorhanden = getMandantFac()
								.mwstsatzFindByPrimaryKey(posDto.getMwstsatzIId(), theClientDto);

						String meldung1 = getTextRespectUISpr(
								"auftrag.bearbeiten.handartikelinbestehendenumwandeln.error.mwstsatz",
								theClientDto.getMandant(), theClientDto.getLocUi(),
								Helper.formatZahl(mwstsatzDtoVorhanden.getFMwstsatz(), theClientDto.getLocUi()) + "%",
								Helper.formatZahl(mwstsatzDto.getFMwstsatz(), theClientDto.getLocUi()) + "%");

						returnWerte.add(meldung1);

					}

					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(artikelIId,
							auftragDto.getKundeIIdRechnungsadresse(), posDto.getNMenge(),
							new Date(auftragDto.getTBelegdatum().getTime()),
							kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDto.getIId(),
							auftragDto.getCAuftragswaehrung(), theClientDto);

					if (vkpreisfindungDto != null) {
						VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisfindungDto);
						if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
							BigDecimal nettopreis = kundenVKPreisDto.nettopreis;

							if (nettopreis.doubleValue() != 0) {

								if (!nettopreis.equals(posDto.getNNettoeinzelpreis())) {
									// Meldung

									String meldung2 = getTextRespectUISpr(
											"auftrag.bearbeiten.handartikelinbestehendenumwandeln.error.preis",
											theClientDto.getMandant(), theClientDto.getLocUi(),
											Helper.formatZahl(posDto.getNNettoeinzelpreis(), iNachkommastellenPreis,
													theClientDto.getLocUi()) + " " + auftragDto.getCAuftragswaehrung(),
											Helper.formatZahl(nettopreis, iNachkommastellenPreis,
													theClientDto.getLocUi()) + " " + auftragDto.getCAuftragswaehrung());
									returnWerte.add(meldung2);

								}
							}

						}
					}

				}
				getArtikelFac().wandleHandeingabeInBestehendenArtikelUmTeil2(positionIId, artikelIId);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return returnWerte;
	}

	public void wandleHandeingabeInBestehendenArtikelUmTeil2(Integer positionIId, Integer artikelIId)
			throws RemoteException {
		// ArtikelId austauschen
		Auftragposition ap = em.find(Auftragposition.class, positionIId);
		ap.setArtikelIId(artikelIId);

		// SP7891
		ap.setCBezeichnung(null);
		ap.setCZusatzbezeichnung(null);

		ap.setAuftragpositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
		em.flush();

		ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
		reservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
		reservierungDto.setIBelegartpositionid(ap.getIId());
		reservierungDto.setArtikelIId(ap.getArtikelIId());
		reservierungDto.setTLiefertermin(ap.getTUebersteuerterliefertermin());
		reservierungDto.setNMenge(ap.getNOffeneMenge());
		getReservierungFac().createArtikelreservierung(reservierungDto);
	}

	public void wandleHandeingabeInArtikelUm(Integer positionIId, int iArt, String neueArtikelnummer,
			TheClientDto theClientDto) {

		try {
			if (iArt == HANDARTIKEL_UMWANDELN_ANGEBOT) {
				AngebotpositionDto posDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey(positionIId,
						theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Angebotposition ap = em.find(Angebotposition.class, positionIId);
				ap.setArtikelIId(artikelIIdNeu);
				ap.setAngebotpositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_ANFRAGE) {
				AnfragepositionDto posDto = getAnfragepositionFac().anfragepositionFindByPrimaryKey(positionIId,
						theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Anfrageposition ap = em.find(Anfrageposition.class, positionIId);
				ap.setArtikelIId(artikelIIdNeu);
				ap.setAnfragepositionartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_BESTELLUNG) {
				BestellpositionDto posDto = getBestellpositionFac().bestellpositionFindByPrimaryKey(positionIId);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Bestellposition bp = em.find(Bestellposition.class, positionIId);
				bp.setArtikelIId(artikelIIdNeu);
				bp.setBestellpositionartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_STUECKLISTEPOSITION) {
				StuecklistepositionDto posDto = getStuecklisteFac().stuecklistepositionFindByPrimaryKey(positionIId,
						theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Stuecklisteposition bp = em.find(Stuecklisteposition.class, positionIId);
				bp.setArtikelIId(artikelIIdNeu);

				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_AGSTKLPOSITION) {
				AgstklpositionDto posDto = getAngebotstklpositionFac().agstklpositionFindByPrimaryKey(positionIId,
						theClientDto);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Agstklposition bp = em.find(Agstklposition.class, positionIId);
				bp.setAgstklpositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				bp.setArtikelIId(artikelIIdNeu);

				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_EINKAUFSANGEBOTPOSITION) {
				EinkaufsangebotpositionDto posDto = getAngebotstklFac()
						.einkaufsangebotpositionFindByPrimaryKey(positionIId);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				// ArtikelId austauschen
				Einkaufsangebotposition bp = em.find(Einkaufsangebotposition.class, positionIId);
				bp.setAgstklpositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
				bp.setArtikelIId(artikelIIdNeu);

				em.flush();
			} else if (iArt == HANDARTIKEL_UMWANDELN_AUFTRAG) {
				AuftragpositionDto posDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey(positionIId);
				Integer artikelIIdNeu = artikelUmwandeln(theClientDto, neueArtikelnummer, posDto.getArtikelIId());
				wandleHandeingabeInBestehendenArtikelUmTeil2(positionIId, artikelIIdNeu);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private Integer artikelUmwandeln(TheClientDto theClientDto, String artikelnummer, Integer artikelIIdHandeingabe) {
		ArtikelDto artikelDtoHandeingabe = artikelFindByPrimaryKey(artikelIIdHandeingabe, theClientDto);

		ArtikelDto artikelDto = new ArtikelDto();
		artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		artikelDto.setCNr(artikelnummer);

		ArtikelsprDto asprDto = new ArtikelsprDto();
		if (artikelDtoHandeingabe.getArtikelsprDto() != null) {
			asprDto.setCBez(artikelDtoHandeingabe.getArtikelsprDto().getCBez());
			asprDto.setCZbez(artikelDtoHandeingabe.getArtikelsprDto().getCZbez());
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

	public ArtikellieferantDto artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(Integer artikelIId,
			Integer lieferantIId, java.sql.Timestamp tPreisgueltigab, TheClientDto theClientDto) {
		try {
			Query query = em
					.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabGebindeIIdIsNull");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lieferantIId);
			query.setParameter(3, tPreisgueltigab);
			Artikellieferant al = (Artikellieferant) query.getSingleResult();

			return assembleArtikellieferantDto(al);

		} catch (NoResultException ex) {
			return null;
		}
	}

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(Integer artikelIId,
			Integer lieferantIId, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null || lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || lieferantIId == null"));
		}
		try {
			Query query = null;

			if (gebindeIId == null) {
				query = em.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabKleiner");
				query.setParameter(1, artikelIId);
				query.setParameter(2, lieferantIId);
				query.setParameter(3, tDatumPreisgueltigkeit);
			} else {
				query = em.createNamedQuery(
						"ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabGebindeIIdKleiner");
				query.setParameter(1, artikelIId);
				query.setParameter(2, lieferantIId);
				query.setParameter(3, tDatumPreisgueltigkeit);
				query.setParameter(4, gebindeIId);
			}

			// @todo getSingleResult oder getResultList ?
			Collection colArtikellieferant = query.getResultList();
			if (colArtikellieferant.size() > 0) {
				ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(
						(Artikellieferant) colArtikellieferant.iterator().next());

				artikellieferantDto.setLieferantDto(getLieferantFac()
						.lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId(), theClientDto));
				return artikellieferantDto;
			} else
				return null;

		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(e));
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei artikellieferantFindByArtikellIIdLieferantIId. Es gibt mehr als ein Ergebnis f\u00FCr artikelIId "
							+ artikelIId + " und lieferantiid " + lieferantIId);
		}
	}

	/**
	 * Liefert Einzelpreis,Nettopreis und Fixkosten in Wunschwaehrung zurueck
	 * 
	 * @param artikelIId      Integer
	 * @param lieferantIId    Integer
	 * @param cWunschwaehrung String
	 * @param theClientDto    String
	 * @return ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(Integer artikelIId,
			Integer lieferantIId, String cWunschwaehrung, TheClientDto theClientDto) throws EJBExceptionLP {

		if (artikelIId == null || lieferantIId == null || cWunschwaehrung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || lieferantIId == null || cWunschwaehrung == null"));
		}

		Query query = em.createNamedQuery("ArtikellieferantfindByArtikellIIdLieferantIId");
		query.setParameter(1, artikelIId);
		query.setParameter(2, lieferantIId);

		List l = query.getResultList();
		if (l.size() > 0) {
			Artikellieferant artikellieferant = (Artikellieferant) l.iterator().next();
			ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(artikellieferant);

			artikellieferantDto.setLieferantDto(
					getLieferantFac().lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId(), theClientDto));

			try {
				Date datum = new Date(System.currentTimeMillis());
				if (artikellieferantDto.getNEinzelpreis() != null) {
					artikellieferantDto.setNEinzelpreis(
							getLocaleFac().rechneUmInAndereWaehrungZuDatum(artikellieferantDto.getNEinzelpreis(),
									artikellieferantDto.getLieferantDto().getWaehrungCNr(), cWunschwaehrung, datum,
									theClientDto));
				}
				if (artikellieferantDto.getNNettopreis() != null) {
					artikellieferantDto.setNNettopreis(
							getLocaleFac().rechneUmInAndereWaehrungZuDatum(artikellieferantDto.getNNettopreis(),
									artikellieferantDto.getLieferantDto().getWaehrungCNr(), cWunschwaehrung, datum,
									theClientDto));
				}
				if (artikellieferantDto.getNFixkosten() != null) {
					artikellieferantDto.setNFixkosten(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							artikellieferantDto.getNFixkosten(), artikellieferantDto.getLieferantDto().getWaehrungCNr(),
							cWunschwaehrung, datum, theClientDto));
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			return artikellieferantDto;
		} else {
			return null;
		}

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	public void preiseXLSForPreispflege(byte[] xlsFile, String cBegruendung, TheClientDto theClientDto) {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsFile);
		try {

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			HashMap<Integer, Integer> hmPreislisten = new HashMap<Integer, Integer>();

			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
			try {
				vkpfartikelpreislisteDtos = getVkPreisfindungFac().getAlleAktivenPreislisten(Helper.boolean2Short(true),
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (sheet.getRows() > 2 && sheet.getColumns() > 9) {
				// Zuerst alle PreislistenIds holen
				Cell[] zeileUeberschrift = sheet.getRow(0);
				int iSpalteStart = 11;

				// Aufgrund von PJ21519 kann die Startspalte auch erst spaeter sein
				while (iSpalteStart < zeileUeberschrift.length) {
					String inhalt = zeileUeberschrift[iSpalteStart].getContents();
					if (inhalt != null && inhalt.length() > 0) {
						break;
					} else {
						iSpalteStart++;
					}
				}

				//

				int iSpalteVKBasis = 9;
				Cell[] zeileUeberschrift2 = sheet.getRow(1);
				for (int i = 0; i < zeileUeberschrift2.length; i++) {
					String inhalt = zeileUeberschrift2[i].getContents();
					if (inhalt != null && inhalt.length() > 0 && inhalt.equals(PREISPFLEGE_XLS_SPALTE_VKBASIS)) {
						iSpalteVKBasis = i;
						break;
					}
				}

				int iSpalteGueltigAb = 10;
				for (int i = 0; i < zeileUeberschrift2.length; i++) {
					// Es gibt 2 Gueltigab -> Wir brauchen das letzte
					String inhalt = zeileUeberschrift2[i].getContents();
					if (inhalt != null && inhalt.length() > 0 && inhalt.equals(PREISPFLEGE_XLS_SPALTE_GUELTIGAB)) {
						iSpalteGueltigAb = i;
					}
				}

				while (iSpalteStart < zeileUeberschrift.length) {
					String preislistenname = zeileUeberschrift[iSpalteStart].getContents();

					for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {
						if (vkpfartikelpreislisteDtos[i].getCNr().equals(preislistenname)) {
							hmPreislisten.put(vkpfartikelpreislisteDtos[i].getIId(), new Integer(iSpalteStart));
						}
					}

					iSpalteStart = iSpalteStart + 5;
				}

				for (int i = 2; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					if (sZeile.length > iSpalteGueltigAb) {
						// VKpreisbasis + Gueltigab muss befuellt sein
						if ((sZeile[iSpalteVKBasis].getType() == CellType.NUMBER
								|| sZeile[iSpalteVKBasis].getType() == CellType.NUMBER_FORMULA)
								&& (sZeile[iSpalteGueltigAb].getType() == CellType.DATE
										|| sZeile[iSpalteGueltigAb].getType() == CellType.DATE_FORMULA)) {

							ArtikelDto aDto = getArtikelFac().artikelFindByCNrOhneExc(sZeile[0].getContents(),
									theClientDto);

							if (aDto != null) {

								BigDecimal vkPreisbasis = new BigDecimal(
										((jxl.NumberCell) sZeile[iSpalteVKBasis]).getValue());
								java.sql.Timestamp basisGueltigab = Helper.cutTimestamp(new java.sql.Timestamp(
										((jxl.DateCell) sZeile[iSpalteGueltigAb]).getDate().getTime()));

								// Nun nachsehen, ob die VK-Basis schon
								// vorhanden ist

								try {
									Query query = em.createNamedQuery(
											"VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
									query.setParameter(1, theClientDto.getMandant());
									query.setParameter(2, aDto.getIId());
									query.setParameter(3, basisGueltigab);

									VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) query
											.getSingleResult();

									// Wenn vorhanden, und ungleich dann updaten
									if (vkPreisbasis.doubleValue() != vkPreisfindungEinzelverkaufspreis
											.getNVerkaufspreisbasis().doubleValue()) {

										vkPreisfindungEinzelverkaufspreis.setCBemerkung(cBegruendung);

										vkPreisfindungEinzelverkaufspreis.setNVerkaufspreisbasis(vkPreisbasis);
									}
								} catch (NoResultException ex) {

									VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = new VkPreisfindungEinzelverkaufspreisDto();
									vkPreisfindungEinzelverkaufspreisDto
											.setTVerkaufspreisbasisgueltigab(new Date(basisGueltigab.getTime()));
									vkPreisfindungEinzelverkaufspreisDto.setNVerkaufspreisbasis(vkPreisbasis);
									vkPreisfindungEinzelverkaufspreisDto.setCBemerkung(cBegruendung);
									vkPreisfindungEinzelverkaufspreisDto.setArtikelIId(aDto.getIId());
									vkPreisfindungEinzelverkaufspreisDto.setMandantCNr(theClientDto.getMandant());
									getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(
											vkPreisfindungEinzelverkaufspreisDto, theClientDto);
								}

								// Nun fuer jede Preisliste Fixpreis oder Rabatt
								// nachtragen

								Iterator<Integer> it = hmPreislisten.keySet().iterator();
								while (it.hasNext()) {
									Integer preislisteIId = it.next();
									int iStartSpalte = hmPreislisten.get(preislisteIId);

									if (sZeile.length > iStartSpalte + 3) {

										BigDecimal nFixpreis = null;
										BigDecimal nRabattsatz = null;
										java.sql.Timestamp tGueltigab = null;

										if (sZeile[iStartSpalte + 3].getType() == CellType.EMPTY
												&& sZeile[iStartSpalte].getType() == CellType.EMPTY
												&& sZeile[iStartSpalte + 1].getType() == CellType.EMPTY) {
											// SP9832
											// Wenn keine der 3 Spalten befuellt ist, dann ignorieren

										} else {
											// Gueltigab
											if (sZeile[iStartSpalte + 3].getType() == CellType.DATE
													|| sZeile[iStartSpalte + 3].getType() == CellType.DATE_FORMULA) {
												tGueltigab = new java.sql.Timestamp(
														((jxl.DateCell) sZeile[iStartSpalte + 3]).getDate().getTime());

												if (sZeile[iStartSpalte].getType() == CellType.NUMBER
														|| sZeile[iStartSpalte].getType() == CellType.NUMBER_FORMULA) {
													nFixpreis = new BigDecimal(
															((jxl.NumberCell) sZeile[iStartSpalte]).getValue());
												}

												if (sZeile[iStartSpalte].getType() != CellType.EMPTY
														&& sZeile[iStartSpalte].getType() != CellType.NUMBER
														&& sZeile[iStartSpalte].getType() != CellType.NUMBER_FORMULA) {
													ArrayList al = new ArrayList();

													String preisliste = "";

													for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
														if (vkpfartikelpreislisteDtos[k].getIId()
																.equals(preislisteIId)) {
															preisliste = vkpfartikelpreislisteDtos[k].getCNr();
														}
													}

													al.add("Zeile " + (i + 1) + "/ Spalte 'Fixpreis' der Preisliste '"
															+ preisliste + "' muss ein Zahlenformat enthalten");

													throw new EJBExceptionLP(
															EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT, al,
															new Exception("FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

												}

												if (sZeile[iStartSpalte + 1].getType() == CellType.NUMBER
														|| sZeile[iStartSpalte + 1]
																.getType() == CellType.NUMBER_FORMULA) {
													nRabattsatz = new BigDecimal(
															((jxl.NumberCell) sZeile[iStartSpalte + 1]).getValue());
												}

												if (sZeile[iStartSpalte + 1].getType() != CellType.EMPTY
														&& sZeile[iStartSpalte + 1].getType() != CellType.NUMBER
														&& sZeile[iStartSpalte + 1]
																.getType() != CellType.NUMBER_FORMULA) {
													ArrayList al = new ArrayList();

													String preisliste = "";

													for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
														if (vkpfartikelpreislisteDtos[k].getIId()
																.equals(preislisteIId)) {
															preisliste = vkpfartikelpreislisteDtos[k].getCNr();
														}
													}

													al.add("Zeile " + (i + 1) + "/ Spalte 'Rabattsatz' der Preisliste '"
															+ preisliste + "' muss ein Zahlenformat enthalten");

													throw new EJBExceptionLP(
															EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT, al,
															new Exception("FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

												}

												if ((nFixpreis != null && nRabattsatz != null)
														|| (nFixpreis == null && nRabattsatz != null)
														|| (nFixpreis != null && nRabattsatz == null)) {

													// Pruefen obs schon einen
													// Eintrag gibt
													VkPreisfindungPreisliste preis = null;
													try {
														Query query = em.createNamedQuery(
																"VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
														query.setParameter(1, preislisteIId);
														query.setParameter(2, aDto.getIId());
														query.setParameter(3, tGueltigab);
														preis = (VkPreisfindungPreisliste) query.getSingleResult();

														if (nFixpreis != null) {
															preis.setNArtikelfixpreis(nFixpreis);
															preis.setNArtikelstandardrabattsatz(new BigDecimal(0));
														} else {
															preis.setNArtikelstandardrabattsatz(nRabattsatz);
															preis.setNArtikelfixpreis(null);
														}

														preis.setCBemerkung(cBegruendung);

													} catch (NoResultException ex) {
														// Wenns keinen gibt, neu
														// anlegen

														VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
														vkPreisfindungPreislisteDto.setArtikelIId(aDto.getIId());
														vkPreisfindungPreislisteDto.setTPreisgueltigab(
																new java.sql.Date(tGueltigab.getTime()));

														if (nFixpreis != null) {
															vkPreisfindungPreislisteDto.setNArtikelfixpreis(nFixpreis);
															vkPreisfindungPreislisteDto
																	.setNArtikelstandardrabattsatz(new BigDecimal(0));
														} else {
															vkPreisfindungPreislisteDto
																	.setNArtikelstandardrabattsatz(nRabattsatz);
															vkPreisfindungPreislisteDto.setNArtikelfixpreis(null);
														}

														vkPreisfindungPreislisteDto.setCBemerkung(cBegruendung);

														vkPreisfindungPreislisteDto
																.setVkpfartikelpreislisteIId(preislisteIId);
														getVkPreisfindungFac().createVkPreisfindungPreisliste(
																vkPreisfindungPreislisteDto, theClientDto);

													}

												}

											} else {
												ArrayList al = new ArrayList();

												String preisliste = "";

												for (int k = 0; k < vkpfartikelpreislisteDtos.length; k++) {
													if (vkpfartikelpreislisteDtos[k].getIId().equals(preislisteIId)) {
														preisliste = vkpfartikelpreislisteDtos[k].getCNr();
													}
												}

												al.add("Zeile " + (i + 1) + "/ Spalte '"
														+ PREISPFLEGE_XLS_SPALTE_GUELTIGAB + "' der Preisliste '"
														+ preisliste + "' muss ein gueltiges Datum enthalten");

												throw new EJBExceptionLP(
														EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT, al,
														new Exception("FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));
											}
										}

									}

								}

							}

						} else {

							ArrayList al = new ArrayList();
							if (!(sZeile[iSpalteVKBasis].getType() == CellType.NUMBER
									|| sZeile[iSpalteVKBasis].getType() == CellType.NUMBER_FORMULA)) {
								al.add("Zeile " + (i + 1) + "/ Spalte '" + PREISPFLEGE_XLS_SPALTE_VKBASIS
										+ "' muss Numerische werte enthalten");
							}

							if (!(sZeile[iSpalteGueltigAb].getType() == CellType.DATE
									|| sZeile[iSpalteGueltigAb].getType() == CellType.DATE_FORMULA)) {
								al.add("Zeile " + (i + 1) + "/ Spalte '" + PREISPFLEGE_XLS_SPALTE_GUELTIGAB
										+ "' muss ein gueltiges Datum enthalten");
							}

							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT, al,
									new Exception("FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT"));

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
	public byte[] getXLSForPreispflege(Integer artikelgruppeIId, boolean mitUntergruppen, Integer artikelklasseIId,
			boolean mitUnterklassen, Integer shopgruppeIId, boolean mitShopuntergruppen, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten, TheClientDto theClientDto) {
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

			String mandant = theClientDto.getMandant();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				session.enableFilter("filterMandant").setParameter("paramMandant", getSystemFac().getHauptmandant());
				mandant = getSystemFac().getHauptmandant();
			} else {
				session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
			}

			session.enableFilter("filterLocale").setParameter("paramLocale",
					Helper.locale2String(theClientDto.getLocUi()));

			String queryString = "SELECT artikelliste FROM FLRArtikelliste AS artikelliste LEFT OUTER JOIN artikelliste.artikelshopgruppeset AS webshopgruppen WHERE artikelliste.mandant_c_nr='"
					+ mandant + "' AND artikelliste.artikelart_c_nr<>'" + ArtikelFac.ARTIKELART_HANDARTIKEL + "'";

			if (bMitVersteckten == false) {
				queryString += " AND artikelliste.b_versteckt=0 ";
			}

			if (artikelNrVon != null) {
				queryString += " AND artikelliste.c_nr >='" + artikelNrVon + "'";
			}
			if (artikelNrBis != null) {

				String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelNrBis, 25, '_');
				queryString += " AND artikelliste.c_nr <='" + artikelNrBis_Gefuellt + "'";
			}
			if (artikelklasseIId != null) {
				if (mitUnterklassen) {
					queryString += " AND artikelliste.flrartikelklasse.i_id IN"
							+ getArtikelFac().holeAlleArtikelklassen(artikelklasseIId).erzeuge_IN_Fuer_Query();
				} else {
					queryString += " AND artikelliste.flrartikelklasse.i_id=" + artikelklasseIId.intValue();
				}

			}
			if (artikelgruppeIId != null) {

				if (mitUntergruppen) {
					queryString += " AND artikelliste.flrartikelgruppe.i_id IN"
							+ getArtikelFac().holeAlleArtikelgruppen(artikelgruppeIId).erzeuge_IN_Fuer_Query();
				} else {
					queryString += " AND artikelliste.flrartikelgruppe.i_id=" + artikelgruppeIId.intValue();
				}

			}

			if (shopgruppeIId != null) {
				if (mitShopuntergruppen) {

					String in = getArtikelFac().holeAlleShopgruppen(shopgruppeIId).erzeuge_IN_Fuer_Query();

					queryString += " AND( artikelliste.shopgruppe_i_id IN " + in
							+ " OR webshopgruppen.shopgruppe_i_id IN " + in + "  ) ";

				} else {
					queryString += " AND (artikelliste.shopgruppe_i_id=" + shopgruppeIId.intValue()
							+ " OR webshopgruppen.shopgruppe_i_id=" + shopgruppeIId.intValue() + ")";
				}

			}

			queryString += " ORDER BY artikelliste.c_nr ASC";

			org.hibernate.Query query = session.createQuery(queryString);

			List<?> resultList = query.list();

			Iterator<?> resultListIterator = resultList.iterator();

			// 1. Zeile = Ueberschrift
			VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
			try {
				vkpfartikelpreislisteDtos = getVkPreisfindungFac().getAlleAktivenPreislisten(Helper.boolean2Short(true),
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			// Datumsformat
			DateFormat customDateFormat = new DateFormat("dd.MM.yyyy");
			WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

			DateFormat customDateFormatGelb = new DateFormat("dd.MM.yyyy");
			WritableCellFormat dateFormatGelb = new WritableCellFormat(customDateFormatGelb);
			dateFormatGelb.setBackground(Colour.YELLOW);

			// Zahlenformat

			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant());

			NumberFormat customNumberFormatRabatt = new NumberFormat("####0.00");
			WritableCellFormat numberFormatRabatt = new WritableCellFormat(customNumberFormatRabatt);

			WritableCellFormat numberFormatRabattGelb = new WritableCellFormat(customNumberFormatRabatt);
			numberFormatRabattGelb.setBackground(Colour.YELLOW);

			NumberFormat customNumberFormatMenge = new NumberFormat("####0.000");
			WritableCellFormat numberFormatMenge = new WritableCellFormat(customNumberFormatMenge);

			String sNachkomma = "";
			for (int i = 0; i < iNachkommastellenPreis; i++) {
				sNachkomma += "0";
			}

			WritableCellFormat gelb = new WritableCellFormat();
			gelb.setBackground(Colour.YELLOW);

			NumberFormat customNumberFormatPreis = new NumberFormat("#######0." + sNachkomma);
			WritableCellFormat numberFormatPreis = new WritableCellFormat(customNumberFormatPreis);

			WritableCellFormat numberFormatPreisGelb = new WritableCellFormat(customNumberFormatPreis);
			numberFormatPreisGelb.setBackground(Colour.YELLOW);

			WritableCellFormat numberAsTextFormat = new WritableCellFormat(NumberFormats.TEXT);

			String[] ALPHABET = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
					"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", };

			int iSpalteArtikelnummer = 0;
			int iSpalteBezeichnung = 1;
			int iSpalteKurzbezeichnung = 2;
			int iSpalteZusatzbezeichnung = 3;
			int iSpalteReferenznummer = 4;
			int iSpalteRevision = 5;
			int iSpalteIndex = 6;
			int iSpalteEinheit = 7;
			int iSpalteLieferant = 8;
			int iSpalteEinzelpreis = 9;
			int iSpalteRabatt = 10;
			int iSpalteNettopreis = 11;
			int iSpalteGueltigabEinzelpreis = 12;
			int iSpalteVKBasis = 13;
			int iSpalteGueltigabVKBasis = 14;

			sheet.addCell(new Label(iSpalteArtikelnummer, 1, "Artikelnummer"));
			sheet.addCell(new Label(iSpalteBezeichnung, 1, "Bezeichnung"));
			sheet.addCell(new Label(iSpalteKurzbezeichnung, 1, "Kurzbezeichnung"));
			sheet.addCell(new Label(iSpalteZusatzbezeichnung, 1, "Zusatzbezeichnung"));
			sheet.addCell(new Label(iSpalteReferenznummer, 1, "Referenznummer"));
			sheet.addCell(new Label(iSpalteRevision, 1, "Revision"));
			sheet.addCell(new Label(iSpalteIndex, 1, "Index"));
			sheet.addCell(new Label(iSpalteEinheit, 1, "Einheit"));
			sheet.addCell(new Label(iSpalteLieferant, 1, "Lieferant"));

			sheet.addCell(new Label(1, 0,
					"ACHTUNG: EK-Preise sind nur zur Information und könnnen nicht veraendert werden!!!", gelb));

			sheet.mergeCells(1, 0, 10, 0);

			sheet.addCell(new Label(iSpalteEinzelpreis, 1, "Einzelpreis"));
			sheet.addCell(new Label(iSpalteRabatt, 1, "Rabatt"));
			sheet.addCell(new Label(iSpalteNettopreis, 1, "Nettopreis"));
			sheet.addCell(new Label(iSpalteGueltigabEinzelpreis, 1, PREISPFLEGE_XLS_SPALTE_GUELTIGAB));
			// Ab hier VK-Basis
			sheet.addCell(new Label(iSpalteVKBasis, 1, PREISPFLEGE_XLS_SPALTE_VKBASIS));
			sheet.addCell(new Label(iSpalteGueltigabVKBasis, 1, PREISPFLEGE_XLS_SPALTE_GUELTIGAB));
			// Nun alle aktiven Preislisten

			int iSpalte = iSpalteGueltigabVKBasis + 1;

			for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {

				sheet.addCell(new Label(iSpalte, 0, vkpfartikelpreislisteDtos[i].getCNr()));
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

			int iSpalteAbLetzterAbgang = iSpalte;
			sheet.addCell(new Label(iSpalte, 1, "LetzterAbgang"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "LetzterZugang"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Reservierungen Auftrag"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Reservierungen Forecast"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Reservierungen Forecast (Alle)"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Artikelgruppe"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Artikelklasse"));
			iSpalte++;
			sheet.addCell(new Label(iSpalte, 1, "Shopgruppe"));
			iSpalte++;

			int iRow = 2;
			while (resultListIterator.hasNext()) {
				FLRArtikelliste artikelliste = (FLRArtikelliste) resultListIterator.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelliste.getI_id(), theClientDto);

				sheet.addCell(new Label(iSpalteArtikelnummer, iRow, aDto.getCNr(), numberAsTextFormat));

				if (aDto.getArtikelsprDto() != null) {

					sheet.addCell(new Label(iSpalteBezeichnung, iRow, aDto.getArtikelsprDto().getCBez()));
					sheet.addCell(new Label(iSpalteKurzbezeichnung, iRow, aDto.getArtikelsprDto().getCKbez()));
					sheet.addCell(new Label(iSpalteZusatzbezeichnung, iRow, aDto.getArtikelsprDto().getCZbez()));
				}

				sheet.addCell(new Label(iSpalteReferenznummer, iRow, aDto.getCReferenznr()));
				sheet.addCell(new Label(iSpalteRevision, iRow, aDto.getCRevision()));
				sheet.addCell(new Label(iSpalteIndex, iRow, aDto.getCIndex()));

				sheet.addCell(new Label(iSpalteEinheit, iRow, aDto.getEinheitCNr().trim()));

				ArtikellieferantDto alDto = getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelliste.getI_id(),
						new BigDecimal(1), theClientDto.getSMandantenwaehrung(), theClientDto);

				if (alDto != null) {
					if (alDto.getLieferantDto() != null) {
						sheet.addCell(new Label(iSpalteLieferant, iRow,
								alDto.getLieferantDto().getPartnerDto().formatFixName1Name2()));
					}

					if (alDto.getNEinzelpreis() != null) {
						sheet.addCell(new jxl.write.Number(iSpalteEinzelpreis, iRow,
								alDto.getNEinzelpreis().doubleValue(), numberFormatPreisGelb));
					}
					if (alDto.getFRabatt() != null) {
						sheet.addCell(new jxl.write.Number(iSpalteRabatt, iRow, alDto.getFRabatt().doubleValue(),
								numberFormatRabattGelb));
					}
					if (alDto.getNNettopreis() != null) {
						sheet.addCell(new jxl.write.Number(iSpalteNettopreis, iRow,
								alDto.getNNettopreis().doubleValue(), numberFormatPreisGelb));
					}

					sheet.addCell(new jxl.write.DateTime(iSpalteGueltigabEinzelpreis, iRow, alDto.getTPreisgueltigab(),
							dateFormatGelb));
				}

				try {
					VkPreisfindungEinzelverkaufspreisDto preisbasisDto = getVkPreisfindungFac()
							.getArtikeleinzelverkaufspreis(artikelliste.getI_id(), new Date(System.currentTimeMillis()),
									theClientDto.getSMandantenwaehrung(), theClientDto);

					// Ab hier VK-Basis
					if (preisbasisDto != null) {
						sheet.addCell(new jxl.write.Number(iSpalteVKBasis, iRow,
								preisbasisDto.getNVerkaufspreisbasis().doubleValue()));
						sheet.addCell(new jxl.write.DateTime(iSpalteGueltigabVKBasis, iRow,
								preisbasisDto.getTVerkaufspreisbasisgueltigab(), dateFormat));

					}

					iSpalte = iSpalteGueltigabVKBasis + 1;
					for (int i = 0; i < vkpfartikelpreislisteDtos.length; i++) {

						// Nun fuer jede preisliste den preis holen

						Session sessionPreisliste = FLRSessionFactory.getFactory().openSession();

						String sQueryPreisliste = "SELECT artikelpreis FROM FLRVkpfartikelpreis artikelpreis WHERE artikelpreis.vkpfartikelpreisliste_i_id="
								+ vkpfartikelpreislisteDtos[i].getIId() + " AND artikelpreis.artikel_i_id="
								+ artikelliste.getI_id() + " AND artikelpreis.t_preisgueltigab<='"
								+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis()))
								+ "' ORDER BY artikelpreis.t_preisgueltigab DESC";

						org.hibernate.Query queryPreisliste = session.createQuery(sQueryPreisliste);
						queryPreisliste.setMaxResults(1);

						List<?> resultListPreisliste = queryPreisliste.list();

						Iterator<?> resultListIteratorPreisliste = resultListPreisliste.iterator();

						if (resultListIteratorPreisliste.hasNext()) {

							FLRVkpfartikelpreis artikelpreis = (FLRVkpfartikelpreis) resultListIteratorPreisliste
									.next();

							if (artikelpreis.getN_artikelfixpreis() != null) {
								sheet.addCell(new jxl.write.Number(iSpalte, iRow,
										artikelpreis.getN_artikelfixpreis().doubleValue(), numberFormatPreis));
							}
							iSpalte++;
							if (artikelpreis.getN_artikelstandardrabattsatz() != null) {
								sheet.addCell(new jxl.write.Number(iSpalte, iRow,
										artikelpreis.getN_artikelstandardrabattsatz().doubleValue(),
										numberFormatRabatt));
							}
							iSpalte++;

							String referenzVKBasis = CellReferenceHelper.getCellReference(iSpalteVKBasis, iRow);

							String referenzRabatt = CellReferenceHelper.getCellReference(iSpalte - 1, iRow);

							sheet.addCell(new Formula(iSpalte, iRow,
									"WENN(" + CellReferenceHelper.getCellReference(iSpalte - 2, iRow) + "=0,$"
											+ referenzVKBasis + "*(1-" + referenzRabatt + "/100),WENN(" + referenzRabatt
											+ "=0," + CellReferenceHelper.getCellReference(iSpalte - 2, iRow)
											+ ",\"????\"))"));
							iSpalte++;
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(artikelpreis.getT_preisgueltigab().getTime());

							DateTime dateCell = new DateTime(iSpalte, iRow, c.getTime(), dateFormat);
							sheet.addCell(dateCell);

							// sheet.addCell(new jxl.write.DateTime(iSpalte,
							// iRow,
							// new Date(
							// artikelpreis.getT_preisgueltigab().getTime())));

							iSpalte++;
							sheet.addCell(new Label(iSpalte, iRow, vkpfartikelpreislisteDtos[i].getWaehrungCNr()));
							iSpalte++;

						} else {
							iSpalte = iSpalte + 5;
						}
						sessionPreisliste.close();

					}

					iSpalte = iSpalteAbLetzterAbgang;
					java.sql.Timestamp tsLetzterAbgang = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(artikelliste.getI_id(), true);
					if (tsLetzterAbgang != null) {
						sheet.addCell(new jxl.write.DateTime(iSpalte, iRow,
								getLagerFac().getDatumLetzterZugangsOderAbgangsbuchung(artikelliste.getI_id(), true),
								dateFormat));
					}
					iSpalte++;
					java.sql.Timestamp tsLetzterZugang = getLagerFac()
							.getDatumLetzterZugangsOderAbgangsbuchung(artikelliste.getI_id(), false);
					if (tsLetzterZugang != null) {

						sheet.addCell(new jxl.write.DateTime(iSpalte, iRow, tsLetzterZugang, dateFormat));
					}
					iSpalte++;

					BigDecimal bdReservierungenAuftrag = getReservierungFac().getAnzahlReservierungen(
							artikelliste.getI_id(), null, theClientDto.getMandant(), null, false,
							LocaleFac.BELEGART_AUFTRAG);
					if (bdReservierungenAuftrag != null && bdReservierungenAuftrag.doubleValue() != 0) {
						sheet.addCell(new jxl.write.Number(iSpalte, iRow, bdReservierungenAuftrag.doubleValue(),
								numberFormatMenge));
					}
					iSpalte++;
					BigDecimal bdReservierungenForecast = getReservierungFac().getAnzahlReservierungen(
							artikelliste.getI_id(), null, theClientDto.getMandant(), null, false,
							LocaleFac.BELEGART_FORECAST);
					if (bdReservierungenForecast != null && bdReservierungenForecast.doubleValue() != 0) {
						sheet.addCell(new jxl.write.Number(iSpalte, iRow, bdReservierungenForecast.doubleValue(),
								numberFormatMenge));
					}
					iSpalte++;

					BigDecimal bdReservierungenForecastAlle = getReservierungFac().getAnzahlReservierungen(
							artikelliste.getI_id(), null, theClientDto.getMandant(), null, false,
							LocaleFac.BELEGART_FORECAST, true);
					if (bdReservierungenForecastAlle != null && bdReservierungenForecastAlle.doubleValue() != 0) {
						sheet.addCell(new jxl.write.Number(iSpalte, iRow, bdReservierungenForecastAlle.doubleValue(),
								numberFormatMenge));
					}
					iSpalte++;

					if (artikelliste.getFlrartikelgruppe() != null) {
						sheet.addCell(new Label(iSpalte, iRow, artikelliste.getFlrartikelgruppe().getC_nr().trim()));
					}
					iSpalte++;

					if (artikelliste.getFlrartikelklasse() != null) {
						sheet.addCell(new Label(iSpalte, iRow, artikelliste.getFlrartikelklasse().getC_nr().trim()));
					}
					iSpalte++;

					if (artikelliste.getFlrshopgruppe() != null) {
						sheet.addCell(new Label(iSpalte, iRow, artikelliste.getFlrshopgruppe().getC_nr().trim()));
					}
					iSpalte++;

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

		return (Integer[]) hmBereitsVerwendet.keySet().toArray(new Integer[hmBereitsVerwendet.keySet().size()]);
	}

	public ArtikellieferantDto artikellieferantFindByIIdInWunschwaehrung(Integer artikellieferantIId,
			String cWunschwaehrung, TheClientDto theClientDto) {

		try {

			Artikellieferant artikellieferant = em.find(Artikellieferant.class, artikellieferantIId);

			ArtikellieferantDto artikellieferantDto = assembleArtikellieferantDto(artikellieferant);
			artikellieferantDto.setLieferantDto(
					getLieferantFac().lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId(), theClientDto));

			try {
				Date datum = new Date(System.currentTimeMillis());
				if (artikellieferantDto.getNEinzelpreis() != null) {
					artikellieferantDto.setNEinzelpreis(
							getLocaleFac().rechneUmInAndereWaehrungZuDatum(artikellieferantDto.getNEinzelpreis(),
									artikellieferantDto.getLieferantDto().getWaehrungCNr(), cWunschwaehrung, datum,
									theClientDto));
				}
				if (artikellieferantDto.getNNettopreis() != null) {
					artikellieferantDto.setNNettopreis(
							getLocaleFac().rechneUmInAndereWaehrungZuDatum(artikellieferantDto.getNNettopreis(),
									artikellieferantDto.getLieferantDto().getWaehrungCNr(), cWunschwaehrung, datum,
									theClientDto));
				}
				if (artikellieferantDto.getNFixkosten() != null) {
					artikellieferantDto.setNFixkosten(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							artikellieferantDto.getNFixkosten(), artikellieferantDto.getLieferantDto().getWaehrungCNr(),
							cWunschwaehrung, datum, theClientDto));
				}
			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			return artikellieferantDto;
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setArtikellieferantFromArtikellieferantDto(Artikellieferant artikellieferant,
			ArtikellieferantDto artikellieferantDto, int iNachkommastellenPreis) {
		artikellieferant.setCBezbeilieferant(artikellieferantDto.getCBezbeilieferant());
		artikellieferant.setCArtikelnrlieferant(artikellieferantDto.getCArtikelnrlieferant());
		artikellieferant.setBHerstellerbez(artikellieferantDto.getBHerstellerbez());
		artikellieferant.setBWebshop(artikellieferantDto.getBWebshop());
		artikellieferant.setNEinzelpreis(
				Helper.rundeKaufmaennisch(artikellieferantDto.getNEinzelpreis(), iNachkommastellenPreis));
		artikellieferant.setFRabatt(artikellieferantDto.getFRabatt());
		artikellieferant.setNNettopreis(
				Helper.rundeKaufmaennisch(artikellieferantDto.getNNettopreis(), iNachkommastellenPreis));
		artikellieferant.setFStandardmenge(artikellieferantDto.getFStandardmenge());
		artikellieferant.setFMindestbestellmenge(artikellieferantDto.getFMindestbestelmenge());
		artikellieferant.setNVerpackungseinheit(artikellieferantDto.getNVerpackungseinheit());
		artikellieferant.setNFixkosten(artikellieferantDto.getNFixkosten());
		artikellieferant.setCRabattgruppe(artikellieferantDto.getCRabattgruppe());
		artikellieferant.setCAngebotnummer(artikellieferantDto.getCAngebotnummer());
		artikellieferant.setTPreisgueltigab(artikellieferantDto.getTPreisgueltigab());
		artikellieferant.setTPreisgueltigbis(artikellieferantDto.getTPreisgueltigbis());
		artikellieferant.setISort(artikellieferantDto.getISort());
		artikellieferant.setIWiederbeschaffungszeit(artikellieferantDto.getIWiederbeschaffungszeit());
		artikellieferant.setLieferantIId(artikellieferantDto.getLieferantIId());
		artikellieferant.setBRabattbehalten(artikellieferantDto.getBRabattbehalten());
		artikellieferant.setTAendern(artikellieferantDto.getTAendern());
		artikellieferant.setPersonalIIdAendern(artikellieferantDto.getPersonalIIdAendern());
		artikellieferant.setZertifikatartIId(artikellieferantDto.getZertifikatartIId());
		artikellieferant.setCWeblink(artikellieferantDto.getCWeblink());
		artikellieferant.setEinheitCNrVpe(artikellieferantDto.getEinheitCNrVpe());
		artikellieferant.setGebindeIId(artikellieferantDto.getGebindeIId());
		artikellieferant.setNGebindemenge(artikellieferantDto.getNGebindemenge());
		artikellieferant.setXKommentarFixkosten(artikellieferantDto.getXKommentarFixkosten());
		artikellieferant.setNWebabfrageBestand(artikellieferantDto.getNWebabfrageBestand());
		artikellieferant.setTLetzteWebabfrage(artikellieferantDto.getTLetzteWebabfrage());
		artikellieferant.setBNichtLieferbar(artikellieferantDto.getBNichtLieferbar());
		artikellieferant.setXKommentarNichtLieferbar(artikellieferantDto.getXKommentarNichtLieferbar());
		artikellieferant.setAnfragepositionlieferdatenIId(artikellieferantDto.getAnfragepositionlieferdatenIId());
		artikellieferant.setNInitialkosten(artikellieferantDto.getNInitialkosten());

		em.merge(artikellieferant);
		em.flush();
	}

	private ArtikellieferantDto assembleArtikellieferantDto(Artikellieferant artikellieferant) {
		return ArtikellieferantDtoAssembler.createDto(artikellieferant);
	}

	private ArtikellieferantDto[] assembleArtikellieferantDtos(Collection<?> artikellieferants) {
		List<ArtikellieferantDto> list = new ArrayList<ArtikellieferantDto>();
		if (artikellieferants != null) {
			Iterator<?> iterator = artikellieferants.iterator();
			while (iterator.hasNext()) {
				Artikellieferant artikellieferant = (Artikellieferant) iterator.next();
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

	private void setArtklasprFromArtklasprDto(Artklaspr artklaspr, ArtklasprDto artklasprDto) {
		artklaspr.setCBez(artklasprDto.getCBez());
		em.merge(artklaspr);
		em.flush();
	}

	private void setShopgruppesprFromShoprguppesprDto(Shopgruppespr shopgruppespr, ShopgruppesprDto shopgruppesprDto) {
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

	private void setSollverkaufFromSollverkaufDto(Sollverkauf sollverkauf, SollverkaufDto sollverkaufDto) {
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

	private void setGeometrieFromGeometrieDto(Geometrie geometrie, GeometrieDto geometrieDto) {
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

	private void setArtikelsprFromArtikelsprDto(Artikelspr artikelspr, ArtikelsprDto artikelsprDto,
			int laengeArtikelbezeichnungen) {
		artikelspr.setCKbez(Helper.cutString(artikelsprDto.getCKbez(), MAX_ARTIKEL_KURZBEZEICHNUNG));
		artikelspr.setCBez(Helper.cutString(artikelsprDto.getCBez(), laengeArtikelbezeichnungen));
		artikelspr.setCZbez(Helper.cutString(artikelsprDto.getCZbez(), laengeArtikelbezeichnungen));
		artikelspr.setCZbez2(Helper.cutString(artikelsprDto.getCZbez2(), laengeArtikelbezeichnungen));
		artikelspr.setPersonalIIdAendern(artikelsprDto.getPersonalIIdAendern());
		artikelspr.setCSiwert(HelperServer.getDBValueFromBigDecimal(artikelsprDto.getNSiwert(), 60));
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

	public Integer createHersteller(HerstellerDto herstellerDto) throws EJBExceptionLP {
		myLogger.entry();
		if (herstellerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("herstellerDto == null"));
		}
		if (herstellerDto.getCNr() == null || herstellerDto.getPartnerIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("herstellerDto.getCNr() == null || herstellerDto.getPartnerIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("HerstellerfindByCNr");
			query.setParameter(1, herstellerDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Hersteller doppelt = (Hersteller) query.getSingleResult();

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_HERSTELLER.CNR"));

		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HERSTELLER);
			herstellerDto.setIId(pk);
			Hersteller hersteller = new Hersteller(herstellerDto.getIId(), herstellerDto.getCNr(),
					herstellerDto.getPartnerIId(), herstellerDto.getCLeadIn());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Hersteller hersteller = em.find(Hersteller.class, iId);
		if (hersteller == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
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

	public void updateHersteller(HerstellerDto herstellerDto) throws EJBExceptionLP {
		if (herstellerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("herstellerDto == null"));
		}
		Integer iId = herstellerDto.getIId();
		// try {
		Hersteller hersteller = em.find(Hersteller.class, iId);
		if (hersteller == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
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
			Integer iIdVorhanden = ((Hersteller) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_HERSTELLER.CNR"));
			}
		} catch (NoResultException ex) {
			// nothing here
		}
		setHerstellerFromHerstellerDto(hersteller, herstellerDto);

	}

	public HerstellerDto herstellerFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}
		try {
			HerstellerDto herstellerDto = new HerstellerDto();
			Hersteller hersteller = em.find(Hersteller.class, iId);
			if (hersteller == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei herstellerFindByPrimaryKey. Es gibt keine iid " + iId);
			}
			herstellerDto = assembleHerstellerDto(hersteller);
			herstellerDto.setPartnerDto(
					getPartnerFac().partnerFindByPrimaryKey(herstellerDto.getPartnerIId(), theClientDto));

			return herstellerDto;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei herstellerFindByPrimaryKey. Es gibt mehrere iids " + iId);
		}
	}

	public HerstellerDto[] herstellerFindByPartnerIId(Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
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

	public HerstellerDto[] herstellerFindByPartnerIIdOhneExc(Integer iPartnerId, TheClientDto theClientDto)
			throws RemoteException {
		// try {
		Collection<Hersteller> cl = HerstellerQuery.listByPartnerIId(iPartnerId, em);
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

	private void setHerstellerFromHerstellerDto(Hersteller hersteller, HerstellerDto herstellerDto) {
		hersteller.setPartnerIId(herstellerDto.getPartnerIId());
		hersteller.setCNr(herstellerDto.getCNr());
		hersteller.setCLeadIn(herstellerDto.getCLeadIn());
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

	private void setArtikelartFromArtikelartDto(Artikelart artikelart, ArtikelartDto artikelartDto) {
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

	private void setArtikelartsprFromArtikelartsprDto(Artikelartspr artikelartspr, ArtikelartsprDto artikelartsprDto) {
		artikelartspr.setCBez(artikelartsprDto.getCBez());
		em.merge(artikelartspr);
		em.flush();
	}

	private ArtikelartsprDto assembleArtikelartsprDto(Artikelartspr artikelartspr) {
		return ArtikelartsprDtoAssembler.createDto(artikelartspr);
	}

	private ArtikelartsprDto[] assembleArtikelartsprDtos(Collection<?> artikelartsprs) {
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

	private HandlagerbewegungDto assembleHandlagerbewegungDto(Handlagerbewegung handlagerbewegung) {
		return HandlagerbewegungDtoAssembler.createDto(handlagerbewegung);
	}

	private HandlagerbewegungDto[] assembleHandlagerbewegungDtos(Collection<?> handlagerbewegungs) {
		List<HandlagerbewegungDto> list = new ArrayList<HandlagerbewegungDto>();
		if (handlagerbewegungs != null) {
			Iterator<?> iterator = handlagerbewegungs.iterator();
			while (iterator.hasNext()) {
				Handlagerbewegung handlagerbewegung = (Handlagerbewegung) iterator.next();
				list.add(assembleHandlagerbewegungDto(handlagerbewegung));
			}
		}
		HandlagerbewegungDto[] returnArray = new HandlagerbewegungDto[list.size()];
		return (HandlagerbewegungDto[]) list.toArray(returnArray);
	}

	public void createArtikelart(ArtikelartDto artikelartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("partnerartDto == null"));
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
				Artikelartspr artikelartspr = new Artikelartspr(theClientDto.getLocUiAsString(),
						artikelartDto.getCNr());
				em.persist(artikelartspr);
				em.flush();
				setArtikelartsprFromArtikelartsprDto(artikelartspr, artikelartDto.getArtikelartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelart(ArtikelartDto artikelartDto) throws EJBExceptionLP {

		Artikelart toRemove = em.find(Artikelart.class, artikelartDto.getCNr());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelart. Es gibt keine Artikelart " + artikelartDto.getCNr());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateArtikelart(ArtikelartDto artikelartDto) throws EJBExceptionLP {
		if (artikelartDto != null) {
			String cNr = artikelartDto.getCNr();
			// try {
			Artikelart artikelart = em.find(Artikelart.class, cNr);
			if (artikelart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateArtikelart. Es gibt keine cnr " + cNr + "\nartikelartDto.toString(): "
								+ artikelartDto.toString());
			}
			setArtikelartFromArtikelartDto(artikelart, artikelartDto);
			// try {
			if (artikelartDto.getArtikelartsprDto() != null) {

				// try {
				Artikelartspr artikelartspr = em.find(Artikelartspr.class,
						new ArtikelartsprPK(artikelartDto.getArtikelartsprDto().getLocaleCNr(), cNr));
				if (artikelartspr == null) {
					artikelartspr = new Artikelartspr(artikelartDto.getArtikelartsprDto().getLocaleCNr(), cNr);
					em.persist(artikelartspr);
					em.flush();
				}
				setArtikelartsprFromArtikelartsprDto(artikelartspr, artikelartDto.getArtikelartsprDto());
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

	public ArtikelartDto artikelartFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		Artikelart artikelart = em.find(Artikelart.class, cNr);
		if (artikelart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelartFindByPrimaryKey. Es gibt keine Cnr " + cNr);
		}
		return assembleArtikelartDto(artikelart);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void createArtikelartspr(ArtikelartsprDto artikelartsprDto) throws EJBExceptionLP {
		if (artikelartsprDto == null) {

		}
		try {
			Artikelartspr artikelartspr = new Artikelartspr(artikelartsprDto.getLocaleCNr(),
					artikelartsprDto.getArtikelartCNr());
			em.persist(artikelartspr);
			em.flush();
			setArtikelartsprFromArtikelartsprDto(artikelartspr, artikelartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private void setArtikellieferantstaffelFromArtikellieferantstaffelDto(
			Artikellieferantstaffel artikellieferantstaffel, ArtikellieferantstaffelDto artikellieferantstaffelDto,
			int iNachkommastellenEK) {
		artikellieferantstaffel.setArtikellieferantIId(artikellieferantstaffelDto.getArtikellieferantIId());
		artikellieferantstaffel.setNMenge(artikellieferantstaffelDto.getNMenge());
		artikellieferantstaffel.setFRabatt(artikellieferantstaffelDto.getFRabatt());
		artikellieferantstaffel.setNNettopreis(
				Helper.rundeKaufmaennisch(artikellieferantstaffelDto.getNNettopreis(), iNachkommastellenEK));
		artikellieferantstaffel.setTPreisgueltigab(artikellieferantstaffelDto.getTPreisgueltigab());
		artikellieferantstaffel.setTPreisgueltigbis(artikellieferantstaffelDto.getTPreisgueltigbis());
		artikellieferantstaffel.setBRabattbehalten(artikellieferantstaffelDto.getBRabattbehalten());
		artikellieferantstaffel.setTAendern(artikellieferantstaffelDto.getTAendern());
		artikellieferantstaffel.setPersonalIIdAendern(artikellieferantstaffelDto.getPersonalIIdAendern());
		artikellieferantstaffel.setIWiederbeschaffungszeit(artikellieferantstaffelDto.getIWiederbeschaffungszeit());
		artikellieferantstaffel.setCAngebotnummer(artikellieferantstaffelDto.getCAngebotnummer());
		artikellieferantstaffel.setCArtikelnrlieferant(artikellieferantstaffelDto.getCArtikelnrlieferant());
		artikellieferantstaffel.setCBezbeilieferant(artikellieferantstaffelDto.getCBezbeilieferant());
		artikellieferantstaffel.setEinheitCNrVpe(artikellieferantstaffelDto.getEinheitCNrVpe());
		artikellieferantstaffel
				.setAnfragepositionlieferdatenIId(artikellieferantstaffelDto.getAnfragepositionlieferdatenIId());
		em.merge(artikellieferantstaffel);
		em.flush();
	}

	private ArtikellieferantstaffelDto assembleArtikellieferantstaffelDto(
			Artikellieferantstaffel artikellieferantstaffel) {
		return ArtikellieferantstaffelDtoAssembler.createDto(artikellieferantstaffel);
	}

	private ArtikellieferantstaffelDto[] assembleArtikellieferantstaffelDtos(Collection<?> artikellieferantstaffels) {
		List<ArtikellieferantstaffelDto> list = new ArrayList<ArtikellieferantstaffelDto>();
		if (artikellieferantstaffels != null) {
			Iterator<?> iterator = artikellieferantstaffels.iterator();
			while (iterator.hasNext()) {
				Artikellieferantstaffel artikellieferantstaffel = (Artikellieferantstaffel) iterator.next();
				list.add(assembleArtikellieferantstaffelDto(artikellieferantstaffel));
			}
		}
		ArtikellieferantstaffelDto[] returnArray = new ArtikellieferantstaffelDto[list.size()];
		return (ArtikellieferantstaffelDto[]) list.toArray(returnArray);
	}

	public Integer createArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto,
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL, new Exception(
					"artikellieferantstaffelDto.getArtikellieferantIId() == null || artikellieferantstaffelDto.getNNettopreis() == null || artikellieferantstaffelDto.getDPreisgueltigab() == null || artikellieferantstaffelDto.getFMenge() == null"));
		}
		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
			query.setParameter(1, artikellieferantstaffelDto.getArtikellieferantIId());
			query.setParameter(2, artikellieferantstaffelDto.getNMenge());
			query.setParameter(3, artikellieferantstaffelDto.getTPreisgueltigab());
			// @todo getSingleResult oder getResultList ?
			Artikellieferantstaffel doppelt = (Artikellieferantstaffel) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELLIEFERANTSTAFFEL.UK"));
		} catch (NoResultException ex) {

		}
		try {
			artikellieferantstaffelDto
					.setNNettopreis(Helper.rundeKaufmaennisch(artikellieferantstaffelDto.getNNettopreis(),
							getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant())));
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		artikellieferantstaffelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikellieferantstaffelDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELLIEFERANTSTAFFEL);
		artikellieferantstaffelDto.setIId(pk);

		try {
			Artikellieferantstaffel artikellieferantstaffel = new Artikellieferantstaffel(
					artikellieferantstaffelDto.getIId(), artikellieferantstaffelDto.getArtikellieferantIId(),
					artikellieferantstaffelDto.getNMenge(), artikellieferantstaffelDto.getFRabatt(),
					artikellieferantstaffelDto.getNNettopreis(), artikellieferantstaffelDto.getTPreisgueltigab(),
					artikellieferantstaffelDto.getBRabattbehalten(), artikellieferantstaffelDto.getPersonalIIdAendern(),
					artikellieferantstaffelDto.getTAendern());
			em.persist(artikellieferantstaffel);
			em.flush();
			try {
				setArtikellieferantstaffelFromArtikellieferantstaffelDto(artikellieferantstaffel,
						artikellieferantstaffelDto,
						getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return artikellieferantstaffelDto.getIId();
	}

	public void removeArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws EJBExceptionLP {
		Artikellieferantstaffel toRemove = em.find(Artikellieferantstaffel.class, artikellieferantstaffelDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikellieferantstaffel. Es gibt keine iid "
							+ artikellieferantstaffelDto.getIId());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellieferantstaffelDto != null) {
			Integer iId = artikellieferantstaffelDto.getIId();
			// try {
			Artikellieferantstaffel artikellieferantstaffel = em.find(Artikellieferantstaffel.class, iId);
			if (artikellieferantstaffel == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateArtikellieferantstaffel es gibt keine iid " + iId + "\ndto.tostring: "
								+ artikellieferantstaffelDto.toString());
			}
			artikellieferantstaffelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			artikellieferantstaffelDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
			try {
				setArtikellieferantstaffelFromArtikellieferantstaffelDto(artikellieferantstaffel,
						artikellieferantstaffelDto,
						getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateArtikellieferantstaffels(ArtikellieferantstaffelDto[] artikellieferantstaffelDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellieferantstaffelDtos != null) {
			for (int i = 0; i < artikellieferantstaffelDtos.length; i++) {
				updateArtikellieferantstaffel(artikellieferantstaffelDtos[i], theClientDto);
			}
		}
	}

	/**
	 * Gibt den Einkaufspreis in der gew&uuml;nschten W&auml;hunrg des
	 * Erst-gereihten Lieferanten eines Artikels pro Einheit zurueck
	 * 
	 * @param artikelIId   Integer
	 * @param fMenge       BigDecimal
	 * @param waehrungCNr  gewuenschte Waehrung
	 * @param theClientDto String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto getArtikelEinkaufspreisDesBevorzugtenLieferantenZuDatum(Integer artikelIId,
			BigDecimal fMenge, String waehrungCNr, Date tDatumPreisgueltigkeit, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getArtikelEinkaufspreis(artikelIId, null, fMenge, waehrungCNr, tDatumPreisgueltigkeit, theClientDto);
	}

	/**
	 * Gibt den Einkaufspreis in der gew&uuml;nschten W&auml;hunrg des
	 * Erst-gereihten Lieferanten eines Artikels pro Einheit zurueck
	 * 
	 * @param artikelIId   Integer
	 * @param fMenge       BigDecimal
	 * @param waehrungCNr  gewuenschte Waehrung
	 * @param theClientDto String
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto getArtikelEinkaufspreisDesBevorzugtenLieferanten(Integer artikelIId, BigDecimal fMenge,
			String waehrungCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		return getArtikelEinkaufspreis(artikelIId, null, fMenge, waehrungCNr, null, theClientDto);
	}

	/**
	 * Gibt den Einkaufspreis eines Artikels des Lieferanten der betreffenden
	 * Bestellung und W&auml;hrung zur&uuml;ck
	 * 
	 * @param artikelIId
	 * @param bestellungIId
	 * @param fMenge        bestellte Menge
	 * @param theClientDto  der aktuelle Benutzer
	 */
	public ArtikellieferantDto getArtikelEinkaufspreisEinesLieferantenEinerBestellung(Integer artikelIId,
			Integer bestellungIId, BigDecimal fMenge, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (artikelIId == null || bestellungIId == null || fMenge == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || bestellungIId == null || fMenge == null"));
		}

		BestellungDto bestDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);
		LieferantDto liefDto = getLieferantFac().lieferantFindByPrimaryKey(bestDto.getLieferantIIdBestelladresse(),
				theClientDto);

		ArtikellieferantDto artliefDto = getArtikelEinkaufspreis(artikelIId, liefDto.getIId(), fMenge,
				liefDto.getWaehrungCNr(), bestDto.getDBelegdatum(), theClientDto);

		return artliefDto;
	}

	public ArtikellieferantDto getGuenstigstenEKPreis(Integer artikelIId, BigDecimal bdMenge, java.sql.Date zeitpunkt,
			String waehrungCNr, Integer lieferantIIdVergleich, TheClientDto theClientDto) {

		ArtikellieferantDto alZurueckDto = null;

		String sQueryLieferanten = "SELECT distinct al.lieferant_i_id "
				+ " FROM FLRArtikellieferant AS al WHERE al.flrlieferant.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND al.artikel_i_id=" + artikelIId + " AND al.t_preisgueltigab <='"
				+ Helper.formatDateWithSlashes(zeitpunkt)
				+ "' AND (al.t_preisgueltigbis IS NULL OR al.t_preisgueltigbis >='"
				+ Helper.formatDateWithSlashes(zeitpunkt) + "')";

		if (lieferantIIdVergleich != null) {
			sQueryLieferanten += " AND al.lieferant_i_id<>" + lieferantIIdVergleich;
		}

		Session sessionLieferanten = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query lieferanten = sessionLieferanten.createQuery(sQueryLieferanten);

		List<?> resultListLieferanten = lieferanten.list();
		Iterator<?> resultListIteratorLieferanten = resultListLieferanten.iterator();

		while (resultListIteratorLieferanten.hasNext()) {
			Integer lieferant_i_id = (Integer) resultListIteratorLieferanten.next();

			String sQuery = "SELECT al "
					+ " FROM FLRArtikellieferant AS al WHERE al.b_nicht_lieferbar=0 AND al.artikel_i_id=" + artikelIId
					+ " AND al.lieferant_i_id=" + lieferant_i_id
					+ "  AND al.flrlieferant.t_bestellsperream IS NULL AND al.t_preisgueltigab <='"
					+ Helper.formatDateWithSlashes(zeitpunkt)
					+ "' AND (al.t_preisgueltigbis IS NULL OR al.t_preisgueltigbis >='"
					+ Helper.formatDateWithSlashes(zeitpunkt) + "')   ORDER BY al.t_preisgueltigab DESC";
			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Query inventurliste = session.createQuery(sQuery);
			inventurliste.setMaxResults(1);
			List<?> resultList = inventurliste.list();

			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				FLRArtikellieferant al = (FLRArtikellieferant) resultListIterator.next();
				Set s = al.getStaffelset();

				if (al.getN_nettopreis() == null) {
					continue;
				}

				com.lp.server.partner.service.LieferantDto lieferantDto;
				try {
					lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(al.getLieferant_i_id(), theClientDto);

					BigDecimal nettopreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(al.getN_nettopreis(),
							lieferantDto.getWaehrungCNr(), waehrungCNr, new Date(System.currentTimeMillis()),
							theClientDto);

					if (alZurueckDto == null) {
						alZurueckDto = new ArtikellieferantDto();
						alZurueckDto.setLieferantIId(al.getLieferant_i_id());
						alZurueckDto.setNNettopreis(nettopreis);
					} else {
						if (al.getN_nettopreis().doubleValue() < alZurueckDto.getNNettopreis().doubleValue()) {
							alZurueckDto.setLieferantIId(al.getLieferant_i_id());
							alZurueckDto.setNNettopreis(nettopreis);
						}
					}

					alZurueckDto.setNMaterialzuschlag(getMaterialFac().getMaterialzuschlagEKInZielwaehrung(artikelIId,
							al.getLieferant_i_id(), zeitpunkt, waehrungCNr, theClientDto));

					Iterator it = s.iterator();

					while (it.hasNext()) {

						FLRArtikellieferantstaffel staffel = (FLRArtikellieferantstaffel) it.next();

						if (staffel.getT_preisgueltigab().getTime() <= zeitpunkt.getTime()) {

							if (staffel.getN_menge().doubleValue() <= bdMenge.doubleValue()) {

								BigDecimal nettopreisStaffel = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
										staffel.getN_nettopreis(), lieferantDto.getWaehrungCNr(), waehrungCNr,
										new Date(System.currentTimeMillis()), theClientDto);

								if (nettopreisStaffel.doubleValue() < alZurueckDto.getNNettopreis().doubleValue()) {
									alZurueckDto.setLieferantIId(al.getLieferant_i_id());
									alZurueckDto.setNNettopreis(nettopreisStaffel);
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

	public boolean gibtEsEKStaffelnZuEinemArtikel(Integer artikelIId) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String query = "SELECT st FROM FLRArtikellieferantstaffel st WHERE st.flrartikellieferant.artikel_i_id="
				+ artikelIId + " AND st.t_preisgueltigab<= '"
				+ Helper.formatDateWithSlashes(new java.sql.Date(System.currentTimeMillis())) + "'";
		org.hibernate.Query herst = session.createQuery(query);

		List results = herst.list();

		Iterator it = results.iterator();
		if (it.hasNext()) {
			return true;
		} else {
			return false;
		}
	}

	public Map getAlleGueltigenStaffelneinesLieferanten(Integer artikellieferantIId, java.sql.Date dDatum,
			String waehrungCNrGewuenschteWaehrung, TheClientDto theClientDto) {

		Map m = new LinkedHashMap();

		ArtikellieferantDto artikellieferantDto = artikellieferantFindByPrimaryKey(artikellieferantIId, theClientDto);
		ArtikelDto artikelDto = artikelFindByPrimaryKeySmall(artikellieferantDto.getArtikelIId(), theClientDto);
		Session session = FLRSessionFactory.getFactory().openSession();

		String query = "SELECT st.n_menge FROM FLRArtikellieferantstaffel st WHERE st.artikellieferant_i_id="
				+ artikellieferantIId + " AND st.t_preisgueltigab<= '" + Helper.formatDateWithSlashes(dDatum)
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
				Artikellieferantstaffel als = (Artikellieferantstaffel) c.iterator().next();

				BigDecimal nettopreis = als.getNNettopreis();

				try {
					nettopreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(als.getNNettopreis(),
							artikellieferantDto.getLieferantDto().getWaehrungCNr(), waehrungCNrGewuenschteWaehrung,
							dDatum, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				m.put(als.getIId(),
						Helper.formatZahl(als.getNMenge(), 3, theClientDto.getLocUi()) + " "
								+ artikelDto.getEinheitCNr().trim() + " "
								+ Helper.formatBetrag(nettopreis, theClientDto.getLocUi()) + " "
								+ waehrungCNrGewuenschteWaehrung);

			}

		}

		return m;
	}

	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId, Integer lieferantIId, BigDecimal fMenge,
			String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto) {
		return getArtikelEinkaufspreisMitOptionGebinde(artikelIId, lieferantIId, fMenge, waehrungCNr,
				tDatumPreisgueltigkeit, null, theClientDto);
	}

	/**
	 * Gibt den Einkaufspreis in der gew&uuml;nschten W&auml;hrung eines Lieferanten
	 * eines Artikels pro Einheit zurueck
	 * 
	 * @param artikelIId   Integer
	 * @param lieferantIId Integer
	 * @param fMenge       BigDecimal
	 * @param waehrungCNr  gewuenschte Waehrung
	 * @param theClientDto String
	 * @return ArtikellieferantDto
	 * @throws EJBExceptionLP
	 */
	public ArtikellieferantDto getArtikelEinkaufspreisMitOptionGebinde(Integer artikelIId, Integer lieferantIId,
			BigDecimal fMenge, String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId,
			TheClientDto theClientDto) {
		// check2(idUser);
		if (artikelIId == null || fMenge == null || waehrungCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || fMenge == null || waehrungCNr == null"));
		}

		if (tDatumPreisgueltigkeit == null) {
			tDatumPreisgueltigkeit = Helper.cutDate(new java.sql.Date(System.currentTimeMillis()));
		}

		fMenge = Helper.rundeKaufmaennisch(fMenge, 4);

		ArtikellieferantDto dto = null;
		if (lieferantIId == null) {
			// Wenn lieferantIId nicht vorhanden, dann wird der 1. Gereihte
			// verwendet
			ArtikellieferantDto[] dtos = artikellieferantfindByArtikelIIdTPreisgueltigab(artikelIId,
					tDatumPreisgueltigkeit, theClientDto);
			if (dtos.length > 0) {
				dto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(artikelIId,
						dtos[0].getLieferantIId(), tDatumPreisgueltigkeit, gebindeIId, theClientDto);

				// SP5461 Wenn es fuer 'Kein Gebinde' keinen Preis gibt, dann
				// fuer das naechstgroessere Gebinde den Preis suchen
				if (gebindeIId == null && dto == null) {
					TreeMap tmGebinde = new TreeMap();
					for (int i = 0; i < dtos.length; i++) {
						// Sortieren nach Gebindemenge
						BigDecimal bdGebindemenge = dtos[i].getNGebindemenge();
						if (dtos[i].getNGebindemenge() == null) {
							bdGebindemenge = BigDecimal.ONE;
						}

						if (dtos[i].getLieferantIId().equals(dtos[0].getLieferantIId())) {
							tmGebinde.put(bdGebindemenge, dtos[i]);
						}

					}

					if (tmGebinde.containsKey(fMenge)) {
						dto = (ArtikellieferantDto) tmGebinde.get(fMenge);
					} else if (tmGebinde.higherKey(fMenge) != null) {
						dto = (ArtikellieferantDto) tmGebinde.get(tmGebinde.higherKey(fMenge));
					} else if (tmGebinde.lowerKey(fMenge) != null) {
						dto = (ArtikellieferantDto) tmGebinde.get(tmGebinde.lowerKey(fMenge));
					}

				}

			}
		} else {
			try {

				dto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(artikelIId, lieferantIId,
						tDatumPreisgueltigkeit, gebindeIId, theClientDto);
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

			ArtikellieferantstaffelDto[] staffeln = artikellieferantstaffelFindByArtikellieferantIIdFMenge(dto.getIId(),
					fMenge, tDatumPreisgueltigkeit);

			// SP5327
			if (dto.getNEinzelpreis() == null && staffeln.length == 0) {
				return null;
			}

			if (staffeln.length > 0) {
				ArtikellieferantstaffelDto staffel = staffeln[0];
				if (staffel.getFRabatt() != null) {
					dto.setFRabatt(staffel.getFRabatt().doubleValue());
				}
				dto.setBRabattbehalten(staffel.getBRabattbehalten());
				dto.setNNettopreis(staffel.getNNettopreis());

				// SP6114
				if (dto.getNEinzelpreis() == null) {
					dto.setNEinzelpreis(staffel.getNNettopreis());
					dto.setFRabatt(0D);
				}

				dto.setArtikellieferantstaffelIId(staffel.getIId());
				dto.setNStaffelmenge(staffel.getNMenge());
				dto.setCAngebotnummer(staffel.getCAngebotnummer());
				dto.setTPreisgueltigab(staffel.getTPreisgueltigab());

				if (staffel.getCArtikelnrlieferant() != null) {
					dto.setCArtikelnrlieferant(staffel.getCArtikelnrlieferant());
				}

				if (staffel.getCBezbeilieferant() != null) {
					dto.setCBezbeilieferant(staffel.getCBezbeilieferant());
				}

				if (staffel.getEinheitCNrVpe() != null) {
					dto.setEinheitCNrVpe(staffel.getEinheitCNrVpe());
				}

				if (staffel.getIWiederbeschaffungszeit() != null) {
					dto.setIWiederbeschaffungszeit(staffel.getIWiederbeschaffungszeit());
				}

			}

			// Von Lieferantenwaehrung in gewuenschte Waehrung umrechnen
			try {
				com.lp.server.partner.service.LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(dto.getLieferantIId(), theClientDto);

				dto.setLieferantDto(lieferantDto);

				Date datum = new Date(System.currentTimeMillis());
				if (dto.getNNettopreis() != null) {

					BigDecimal zuschlag = getMaterialFac().getMaterialzuschlagEKInZielwaehrung(artikelIId,
							dto.getLieferantIId(), tDatumPreisgueltigkeit, waehrungCNr, theClientDto);
					if (zuschlag != null) {
						dto.setNMaterialzuschlag(Helper.rundeKaufmaennisch(zuschlag, 4));
					} else {
						dto.setNMaterialzuschlag(new BigDecimal(0));
					}
					dto.setNNettopreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNNettopreis(),
							lieferantDto.getWaehrungCNr(), waehrungCNr, datum, theClientDto));
				}
				if (dto.getNEinzelpreis() != null) {
					dto.setNEinzelpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNEinzelpreis(),
							lieferantDto.getWaehrungCNr(), waehrungCNr, datum, theClientDto));
				}
				if (dto.getNFixkosten() != null) {
					dto.setNFixkosten(getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNFixkosten(),
							lieferantDto.getWaehrungCNr(), waehrungCNr, datum, theClientDto));
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			return dto;

		}
	}

	public ArtikellieferantstaffelDto artikellieferantstaffelFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Artikellieferantstaffel artikellieferantstaffel = em.find(Artikellieferantstaffel.class, iId);
		if (artikellieferantstaffel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikellieferantstaffelFindByPrimaryKey. Es gibt keine iid " + iId);
		}

		return assembleArtikellieferantstaffelDto(artikellieferantstaffel);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIIdFMenge(
			Integer artikellieferantIId, BigDecimal fMenge, java.sql.Date dDatum) throws EJBExceptionLP {
		if (artikellieferantIId == null || fMenge == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikellieferantIId == null || fMenge == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIIdFMenge");
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

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIId(Integer artikellieferantIId)
			throws EJBExceptionLP {
		if (artikellieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikellieferantIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikellieferantstaffelfindByArtikellieferantIId");
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
	 * @param iIdArtikelI     PK des Artikels
	 * @param locBezeichnungI das gewuenschte Locale
	 * @return String
	 */
	public String formatArtikelbezeichnungEinzeiligOhneExc(Integer iIdArtikelI, Locale locBezeichnungI) {
		return formatArtikelbezeichnungEinzeiligOhneExcUebersteuert(iIdArtikelI, locBezeichnungI, null, null);
	}

	public String formatArtikelbezeichnungEinzeiligOhneExcUebersteuert(Integer iIdArtikelI, Locale locBezeichnungI,
			String c_bez_uebersteuert, String c_zbez_uebersteuert) {
		StringBuffer sbBez = new StringBuffer();
		if (iIdArtikelI != null) {
			Artikel oArtikel = em.find(Artikel.class, iIdArtikelI);

			if (c_bez_uebersteuert != null && c_zbez_uebersteuert != null) {
				sbBez.append(c_bez_uebersteuert);
				sbBez.append(" \n");
				sbBez.append(c_zbez_uebersteuert);
				return sbBez.toString();
			}

			if (locBezeichnungI != null) {
				try {
					Query query = em.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
					query.setParameter(1, iIdArtikelI);
					query.setParameter(2, Helper.locale2String(locBezeichnungI));
					Artikelspr oArtikelspr = (Artikelspr) query.getSingleResult();

					// CK->UW
					// umgebaut lt. tel mit WH vom 28.12.05:
					// Zusatzubezeichnung muss immer mitangedruckt werden

					String cBez = c_bez_uebersteuert;
					String cZBez = c_zbez_uebersteuert;

					if (cBez == null) {
						cBez = oArtikelspr.getCBez();
					}

					if (cZBez == null) {
						cZBez = oArtikelspr.getCZbez();
					}

					if (cBez != null && cBez.length() > 0) {
						sbBez.append(cBez);
						sbBez.append(" ");
						if (cZBez != null && cZBez.length() > 0) {
							sbBez.append("\n");
							sbBez.append(cZBez);
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

		}

		return sbBez.toString();
	}

	/**
	 * Die vollstaendige Artikelbezeichnung zum Andrucken zusammenbauen. Wird ein
	 * locale angegeben,betrifft das nicht die uebersteuerten Bez und ZBez
	 * 
	 * @param iIdArtikelI        PK des Artikels
	 * @param cNrPositionsartI   muss LocaleFac.POSITIONSART_IDENT oder
	 *                           LocaleFac.POSITIONSART_HANDEINGABE sein
	 * @param cBezUebersteuertI  die Artikelbezeichnung kann eventuell uebersteuert
	 *                           sein
	 * @param cZBezUebersteuertI String
	 * @param bIncludeCNrI       inklusive cnr
	 * @param localeI            localeI
	 * @param theClientDto       der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 * @throws EJBExceptionLP Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String baueArtikelBezeichnungMehrzeilig(Integer iIdArtikelI, String cNrPositionsartI,
			String cBezUebersteuertI, String cZBezUebersteuertI, boolean bIncludeCNrI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdArtikelI == null"));
		}

		if (cNrPositionsartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrPositionsartI == null"));
		}

		if (!cNrPositionsartI.equals(LocaleFac.POSITIONSART_IDENT)
				&& !cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL, new Exception(
					"!cNrPositionsartI.equals(LocaleFac.POSITIONSART_IDENT) && !cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)"));
		}

		ArtikelDto artikelDto = null;

		artikelDto = artikelFindByPrimaryKeySmall(iIdArtikelI, theClientDto);

		// Uebersetzung in Kundenlocale: Uebersteuerte Texte sind davon nicht
		// betroffen

		Artikelspr oArtikelsprInKundeLocale = null;
		if (localeI != null) {
			try {
				Query query = em.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
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
				if (oArtikelsprInKundeLocale != null && oArtikelsprInKundeLocale.getCBez() != null) {
					if (bIncludeCNrI) {
						sbBezeichnung.append("\n");
					}

					sbBezeichnung.append(oArtikelsprInKundeLocale.getCBez());

				} else if (artikelDto.getArtikelsprDto() != null) {

					if (artikelDto.getArtikelsprDto().getCBez() != null) {
						if (bIncludeCNrI) {
							sbBezeichnung.append("\n");
						}

						sbBezeichnung.append(artikelDto.getArtikelsprDto().getCBez());
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
						sbBezeichnung.append(oArtikelsprInKundeLocale.getCZbez());
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
						sbBezeichnung.append(artikelDto.getArtikelsprDto().getCZbez());
					}
				}

			}

		} else if (cNrPositionsartI.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			sbBezeichnung.append(artikelDto.getArtikelsprDto().getCBez());

			if (artikelDto.getArtikelsprDto().getCZbez() != null) {
				sbBezeichnung.append("\n").append(artikelDto.getArtikelsprDto().getCZbez());
			}

		}

		// System.out.println(">> exit ");
		return sbBezeichnung.toString();
	}

	/**
	 * Die vollstaendige Artikelbezeichnung zum Andrucken zusammenbauen.
	 * 
	 * @param iIdArtikelI        PK des Artikels
	 * @param cNrPositionsartI   muss LocaleFac.POSITIONSART_IDENT oder
	 *                           LocaleFac.POSITIONSART_HANDEINGABE sein
	 * @param cBezUebersteuertI  die Artikelbezeichnung kann eventuell uebersteuert
	 *                           sein
	 * @param cZBezUebersteuertI String
	 * @param bIncludeCNrI       inklusive cnr
	 * @param locale             locale
	 * @param theClientDto       der aktuelle Benutzer
	 * @return String die vallstaendig Artikelbezeichnung
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String baueArtikelBezeichnungMehrzeiligOhneExc(Integer iIdArtikelI, String cNrPositionsartI,
			String cBezUebersteuertI, String cZBezUebersteuertI, boolean bIncludeCNrI, Locale locale,
			TheClientDto theClientDto) {
		myLogger.entry();

		String cBez = null;

		try {
			cBez = baueArtikelBezeichnungMehrzeilig(iIdArtikelI, cNrPositionsartI, cBezUebersteuertI,
					cZBezUebersteuertI, bIncludeCNrI, locale, theClientDto);
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
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(iIdArtikelI, theClientDto);

		// ItemType: Description.
		Helper.addOFElement(nodeItemRet, docI, artikelDto.formatArtikelbezeichnung(),
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
		Helper.addOFElement(nodeItemRet, docI, artikelDto.getCWarenverkehrsnummer(),
				ArtikelFac.SCHEMA_OF_ITEM_CUSTOMS_TARIFF);

		// ItemType: EAN.
		Helper.addOFElement(nodeItemRet, docI, artikelDto.getCVerkaufseannr(), ArtikelFac.SCHEMA_OF_ITEM_EAN);

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
	 * @param sArtikelI String
	 * @param idUser    String
	 * @return String
	 */

	@WebMethod
	@WebResult(name = "StringDocument")
	public String getItemAsStringDocumentWS(@WebParam(name = "sArtikelnr") String sArtikelI,
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
			artikelDto = getArtikelFac().artikelFindByCNr(sArtikelI, theClientDto);
			bdLagerstand = getLagerFac().getLagerstand(artikelDto.getIId(), lagerDto.getIId(), theClientDto);
			ArtikellagerplaetzeDto artLagerPlaetzeDto = getLagerFac()
					.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelDto.getIId(), lagerDto.getIId());
			if (artLagerPlaetzeDto != null) {
				if (artLagerPlaetzeDto.getLagerplatzDto() != null) {
					sLagerplatz = artLagerPlaetzeDto.getLagerplatzDto().getCLagerplatz();
				}
			}

			Node nodeFeatures = doc.createElement(SystemFac.SCHEMA_OF_FEATURES);
			doc.appendChild(nodeFeatures);

			Node nodeFeature = doc.createElement(SystemFac.SCHEMA_OF_FEATURE);
			Node node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
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
			node.appendChild(doc.createTextNode(artikelDto.formatArtikelbezeichnung()));
			nodeFeature.appendChild(node);
			nodeFeatures.appendChild(nodeFeature);

			if (sLagerplatz != null) {
				nodeFeature = doc.createElement(SystemFac.SCHEMA_OF_FEATURE);
				node = doc.createElement(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
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

	public Integer createFarbcode(FarbcodeDto farbcodeDto) throws EJBExceptionLP {
		if (farbcodeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("farbcodeDto == null"));
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_FARBCODE.C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FARBCODE);
			farbcodeDto.setIId(pk);

			Farbcode farbcode = new Farbcode(farbcodeDto.getIId(), farbcodeDto.getCNr());
			em.persist(farbcode);
			em.flush();
			setFarbcodeFromFarbcodeDto(farbcode, farbcodeDto);
			return farbcodeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVorschlagstext(VorschlagstextDto vorschlagstextDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("VorschlagstextFindByLocaleCNrCBez");
			query.setParameter(1, theClientDto.getLocUiAsString());
			query.setParameter(2, vorschlagstextDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Vorschlagstext doppelt = (Vorschlagstext) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_VORSCHLAGSTEXT.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}
		vorschlagstextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VORSCHLAGSTEXT);
			vorschlagstextDto.setIId(pk);

			Vorschlagstext vorschlagstext = new Vorschlagstext(vorschlagstextDto.getIId(),
					vorschlagstextDto.getLocaleCNr(), vorschlagstextDto.getCBez());
			em.persist(vorschlagstext);
			em.flush();
			setVorschlagstextFromVorschlagstextDto(vorschlagstext, vorschlagstextDto);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_REACH.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REACH);
			dto.setIId(pk);

			Reach bean = new Reach(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setReachFromReachDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createLaseroberflaeche(LaseroberflaecheDto dto) {

		try {
			Query query = em.createNamedQuery("LaseroberflaechefindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Laseroberflaeche doppelt = (Laseroberflaeche) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_LASEROBERFLAECHE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LASEROBERFLAECHE);
			dto.setIId(pk);

			Laseroberflaeche bean = new Laseroberflaeche(dto.getIId(), dto.getMandantCNr(), dto.getCNr());
			em.persist(bean);
			em.flush();
			setLaseroberflaecheFromLaseroberflaecheDto(bean, dto);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_VORZUG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VORZUG);
			dto.setIId(pk);

			Vorzug bean = new Vorzug(dto.getIId(), dto.getCNr(), dto.getMandantCNr(), dto.getCBez());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ALERGEN.UK"));
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

			Alergen bean = new Alergen(dto.getIId(), dto.getISort(), dto.getMandantCNr(), dto.getCBez());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ROHS.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ROHS);
			dto.setIId(pk);

			Rohs bean = new Rohs(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setRohsFromRohsDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createWerkzeug(WerkzeugDto dto) {

		try {
			Query query = em.createNamedQuery("WerkzeugfindByCNr");
			query.setParameter(1, dto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Werkzeug doppelt = (Werkzeug) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WERKZEUG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WERKZEUG);
			dto.setIId(pk);

			Werkzeug bean = new Werkzeug(dto.getIId(), dto.getCNr(), dto.getMandantCNrStandort());
			em.persist(bean);
			em.flush();
			setWerkzeugFromWerkzeugDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) {

		try {
			Query query = em.createNamedQuery("VerschleissteilwerkzeugfindByVerschleissteilIIdWerkzeugIId");
			query.setParameter(1, dto.getVerschleissteilIId());
			query.setParameter(2, dto.getWerkzeugIId());
			// @todo getSingleResult oder getResultList ?
			Verschleissteilwerkzeug doppelt = (Verschleissteilwerkzeug) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_VERSCHLEISSTEILWERKZEUG.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERSCHLEISSTEILWERKZEUG);
			dto.setIId(pk);

			Verschleissteilwerkzeug bean = new Verschleissteilwerkzeug(dto.getIId(), dto.getVerschleissteilIId(),
					dto.getWerkzeugIId());
			em.persist(bean);
			em.flush();

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createVerschleissteil(VerschleissteilDto dto) {

		try {
			Query query = em.createNamedQuery("VerschleissteilfindByCNr");
			query.setParameter(1, dto.getCNr());

			// @todo getSingleResult oder getResultList ?
			Verschleissteil doppelt = (Verschleissteil) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_VERSCHLEISSTEIL.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERSCHLEISSTEIL);
			dto.setIId(pk);

			Verschleissteil bean = new Verschleissteil(dto.getIId(), dto.getCNr());
			em.persist(bean);
			em.flush();
			setVerschleissteilFromVerschleissteilDto(bean, dto);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_AUTOMOTIVE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUTOMOTIVE);
			dto.setIId(pk);

			Automotive bean = new Automotive(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MEDICAL.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MEDICAL);
			dto.setIId(pk);

			Medical bean = new Medical(dto.getIId(), dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setMedicalFromMedicalDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createGebinde(GebindeDto dto) {

		try {
			Query query = em.createNamedQuery(Gebinde.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Gebinde doppelt = (Gebinde) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_GEBINDE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_GEBINDE);
			dto.setIId(pk);

			Gebinde bean = new Gebinde(dto.getIId(), dto.getMandantCNr(), dto.getCBez(), dto.getNMenge());
			em.persist(bean);
			em.flush();
			setGebindeFromGebindeDto(bean, dto);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_VORZUG.UK"));
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ALERGEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setAlergenFromAlergenDto(bean, dto);
	}

	public void updateLaseroberflaeche(LaseroberflaecheDto dto) {
		Laseroberflaeche bean = em.find(Laseroberflaeche.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("LaseroberflaechefindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Laseroberflaeche) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_LASEROBERFLAECHE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setLaseroberflaecheFromLaseroberflaecheDto(bean, dto);
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_REACH.UK"));
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ROHS.UK"));
			}
		} catch (NoResultException ex) {

		}

		setRohsFromRohsDto(bean, dto);
	}

	public void updateWerkzeug(WerkzeugDto dto) {
		Werkzeug bean = em.find(Werkzeug.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("WerkzeugfindByCNr");
			query.setParameter(1, dto.getCNr());

			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Werkzeug) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WERKZEUG.UK"));
			}
		} catch (NoResultException ex) {

		}

		setWerkzeugFromWerkzeugDto(bean, dto);
	}

	public void updateVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) {
		Verschleissteilwerkzeug bean = em.find(Verschleissteilwerkzeug.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("VerschleissteilwerkzeugfindByVerschleissteilIIdWerkzeugIId");
			query.setParameter(1, dto.getVerschleissteilIId());
			query.setParameter(2, dto.getWerkzeugIId());

			Integer iIdVorhanden = ((Verschleissteilwerkzeug) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_VERSCHLEISSTEILWERKZEUG.UK"));
			}
		} catch (NoResultException ex) {

		}
		bean.setVerschleissteilIId(dto.getVerschleissteilIId());
		bean.setWerkzeugIId(dto.getWerkzeugIId());
	}

	public void updateVerschleissteil(VerschleissteilDto dto) {
		Verschleissteil bean = em.find(Verschleissteil.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("VerschleissteilfindByCNr");

			query.setParameter(1, dto.getCNr());

			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Verschleissteil) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_VERSCHLEISSTEIL.UK"));
			}
		} catch (NoResultException ex) {

		}

		setVerschleissteilFromVerschleissteilDto(bean, dto);
	}

	public void updateAutomotive(AutomotiveDto dto) {
		Automotive bean = em.find(Automotive.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("AutomotivefindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Automotive) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_AUTOMOTIVE.UK"));
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MEDICAL.UK"));
			}
		} catch (NoResultException ex) {

		}

		setMedicalFromMedicalDto(bean, dto);
	}

	public void updateGebinde(GebindeDto dto) {
		Gebinde bean = em.find(Gebinde.class, dto.getIId());

		try {
			Query query = em.createNamedQuery(Gebinde.QueryFindByMandantCNrCBez);
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Gebinde) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_GEBINDE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setGebindeFromGebindeDto(bean, dto);
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

	public void removeLaseroberflaeche(LaseroberflaecheDto dto) {
		Laseroberflaeche toRemove = em.find(Laseroberflaeche.class, dto.getIId());
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

	public void removeWerkzeug(WerkzeugDto dto) {
		Werkzeug toRemove = em.find(Werkzeug.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeArtikelspr(Integer artikelIId, String locale, TheClientDto theClientDto) {
		try {
			Query query = em.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, locale);
			Artikelspr artikelspr = (Artikelspr) query.getSingleResult();

			if (artikelspr != null) {

				ArtikelsprDto asprDto = ArtikelsprDtoAssembler.createDto(artikelspr);

				HvDtoLogger<ArtikelsprDto> zsLogger = new HvDtoLogger<ArtikelsprDto>(em,
						artikelspr.getPk().getArtikelIId(), theClientDto);
				zsLogger.logDelete(asprDto);

				theClientDto.setUiLoc(Helper.string2Locale(locale));

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);

				artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId, locale, theClientDto);

				aDto.setArtikelsprDto(null);
				vergleicheArtikelDtoVorherNachherUndLoggeAenderungen(aDto, theClientDto);

				em.remove(artikelspr);
				em.flush();
			}

		} catch (Throwable t) {
			// es gibt die gewuenschte Uebersetzung nicht
		}

	}

	public void removeVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) {
		Verschleissteilwerkzeug toRemove = em.find(Verschleissteilwerkzeug.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeVerschleissteil(VerschleissteilDto dto) {
		Verschleissteil toRemove = em.find(Verschleissteil.class, dto.getIId());
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

	public void removeGebinde(GebindeDto dto) {
		Gebinde toRemove = em.find(Gebinde.class, dto.getIId());
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

	public LaseroberflaecheDto laseroberflaecheFindByPrimaryKey(Integer iId) {
		Laseroberflaeche bean = em.find(Laseroberflaeche.class, iId);
		return LaseroberflaecheDtoAssembler.createDto(bean);
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

	public WerkzeugDto werkzeugFindByPrimaryKey(Integer iId) {
		Werkzeug bean = em.find(Werkzeug.class, iId);
		return WerkzeugDtoAssembler.createDto(bean);
	}

	public VerschleissteilwerkzeugDto verschleissteilwerkzeugFindByPrimaryKey(Integer iId) {
		Verschleissteilwerkzeug bean = em.find(Verschleissteilwerkzeug.class, iId);
		return VerschleissteilwerkzeugDtoAssembler.createDto(bean);
	}

	public VerschleissteilwerkzeugDto[] verschleissteilwerkzeugFindByVerschleissteilIId(Integer verschleissteilIId) {
		Query query = em.createNamedQuery("VerschleissteilwerkzeugfindByVerschleissteilIId");
		query.setParameter(1, verschleissteilIId);

		return VerschleissteilwerkzeugDtoAssembler.createDtos(query.getResultList());
	}

	public VerschleissteilDto verschleissteilFindByPrimaryKey(Integer iId) {
		Verschleissteil bean = em.find(Verschleissteil.class, iId);
		return VerschleissteilDtoAssembler.createDto(bean);
	}

	public Map getAllVerschleissteile(Integer werkzeugIId) {

		Query query = em.createNamedQuery("VerschleissteilfindByWerkzeugIId");
		query.setParameter(1, werkzeugIId);

		Collection c = query.getResultList();

		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();

		Iterator<?> itArten = c.iterator();
		while (itArten.hasNext()) {
			Verschleissteil vt = (Verschleissteil) itArten.next();
			tmArten.put(vt.getIId(), VerschleissteilDtoAssembler.createDto(vt).getBezeichnung());
		}

		return tmArten;
	}

	public AutomotiveDto automotiveFindByPrimaryKey(Integer iId) {
		Automotive bean = em.find(Automotive.class, iId);
		return AutomotiveDtoAssembler.createDto(bean);
	}

	public MedicalDto medicalFindByPrimaryKey(Integer iId) {
		Medical bean = em.find(Medical.class, iId);
		return MedicalDtoAssembler.createDto(bean);
	}

	public GebindeDto gebindeFindByPrimaryKey(Integer iId) {
		Gebinde bean = em.find(Gebinde.class, iId);
		return GebindeDtoAssembler.createDto(bean);
	}

	public Integer createVerleih(VerleihDto verleihDto) {
		if (verleihDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("verleihDto == null"));
		}
		if (verleihDto.getITage() == null || verleihDto.getFFaktor() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("verleihDto.getITage() == null || verleihDto.getFFaktor() == null"));
		}
		try {
			Query query = em.createNamedQuery("VerleihfindByITage");
			query.setParameter(1, verleihDto.getITage());
			// @todo getSingleResult oder getResultList ?
			Verleih doppelt = (Verleih) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_VERLEIH.I_TAGE"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_VERLEIH);
			verleihDto.setIId(pk);

			Verleih farbcode = new Verleih(verleihDto.getIId(), verleihDto.getITage(), verleihDto.getFFaktor());
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}
		// try {
		Farbcode toRemove = em.find(Farbcode.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeFarbcode. Es gibt keine iid " + dto.getIId() + "\ndto.toString: "
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		Verleih toRemove = em.find(Verleih.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeVerleih. Es gibt keine iid " + dto.getIId() + "\ndto.toString: "
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("farbcodeDto == null"));
		}
		if (farbcodeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("farbcodeDto.getIId() == null"));
		}
		if (farbcodeDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("farbcodeDto.getCNr() == null"));
		}

		Integer iId = farbcodeDto.getIId();
		try {
			Farbcode farbcode = em.find(Farbcode.class, iId);

			try {
				Query query = em.createNamedQuery("FarbcodefindByCNr");
				query.setParameter(1, farbcodeDto.getCNr());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Farbcode) query.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_FARBCODE.C_NR"));
				}
			} catch (NoResultException ex) {

			}

			setFarbcodeFromFarbcodeDto(farbcode, farbcodeDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public void updateVorschlagstext(VorschlagstextDto vorschlagstextDto, TheClientDto theClientDto) {

		Integer iId = vorschlagstextDto.getIId();
		try {
			Vorschlagstext vorschlagstext = em.find(Vorschlagstext.class, iId);

			try {
				Query query = em.createNamedQuery("VorschlagstextFindByLocaleCNrCBez");
				query.setParameter(1, theClientDto.getLocUiAsString());
				query.setParameter(2, vorschlagstextDto.getCBez());

				Integer iIdVorhanden = ((Vorschlagstext) query.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_VORSCHLAGSTEXT.C_NR"));
				}
			} catch (NoResultException ex) {

			}

			setVorschlagstextFromVorschlagstextDto(vorschlagstext, vorschlagstextDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public void updateVerleih(VerleihDto verleihDto) {
		if (verleihDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("verleihDto == null"));
		}
		if (verleihDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("verleihDto.getIId() == null"));
		}
		if (verleihDto.getFFaktor() == null || verleihDto.getITage() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("verleihDto.getFFaktor() == null || verleihDto.getITage() == null"));
		}

		Integer iId = verleihDto.getIId();
		try {
			Verleih verleih = em.find(Verleih.class, iId);

			try {
				Query query = em.createNamedQuery("VerleihfindByITage");
				query.setParameter(1, verleihDto.getITage());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Verleih) query.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_VERLEIH.I_TAGE"));
				}

			} catch (NoResultException ex) {

			}

			setVerleihFromVerleihDto(verleih, verleihDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public String generiereNeueArtikelnummer(String beginnArtikelnummer, TheClientDto theClientDto) {

		boolean bLetzterZiffernblock = false;

		if (beginnArtikelnummer == null) {

			beginnArtikelnummer = "";

			try {
				// PJ18234
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_STARTWERT_ARTIKELNUMMER);
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
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_GENERIERE_ARTIKELNUMMER_ZIFFERNBLOCK);

			bLetzterZiffernblock = (Boolean) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
			iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		boolean bHerstellerkopplung = getMandantFac().hatZusatzfunktionberechtigung(
				com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG, theClientDto);

		beginnArtikelnummer = beginnArtikelnummer.trim();
		Session session = FLRSessionFactory.getFactory().openSession();

		String mandant = theClientDto.getMandant();
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			mandant = getSystemFac().getHauptmandant();
		}

		String sQuery = "SELECT substring(a.c_nr,0," + (iLaengeArtikelnummer + 1)
				+ ") FROM FLRArtikel a WHERE a.artikelart_c_nr<>'" + ArtikelFac.ARTIKELART_HANDARTIKEL
				+ "' AND a.mandant_c_nr='" + mandant + "' AND substring(a.c_nr,0," + (iLaengeArtikelnummer + 1)
				+ ") LIKE '" + beginnArtikelnummer + "%' ORDER BY substring(a.c_nr,0," + (iLaengeArtikelnummer + 1)
				+ ") DESC";

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
				String zahlenteil = letzteArtikelnummer.substring(iStartZahl, iEndeZahl + 1);

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
					zahlenteilNeu = Helper.fitString2LengthAlignRight(zahl + "", iNeueLaenge, '0');
					// Neue Artikelnummer zusammenbauen

					String neueArtNr = letzteArtikelnummer.substring(0, iStartZahl) + zahlenteilNeu
							+ letzteArtikelnummer.substring(iEndeZahl + 1);

					//
					Session session2 = FLRSessionFactory.getFactory().openSession();

					String sQuery2 = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='" + mandant
							+ "' AND substring(a.c_nr,0," + (iLaengeArtikelnummer + 1) + ") = '"
							+ Helper.fitString2Length(neueArtNr, iLaengeArtikelnummer, ' ') + "'";

					org.hibernate.Query query2 = session2.createQuery(sQuery2);
					query2.setMaxResults(1);
					List<?> results2 = query2.list();
					if (results2.size() > 0) {

						continue;

					}
					session2.close();

					Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
					query.setParameter(1, neueArtNr);
					query.setParameter(2, mandant);
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

	public FarbcodeDto farbcodeFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Farbcode farbcode = em.find(Farbcode.class, iId);
		if (farbcode == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei farbcodeFindByPrimaryKey. Es gibt keine iid " + iId);
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
		Query query = em.createNamedQuery("SperrenfindByOBildMandantCNrNotNull");

		// SP8258
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			query.setParameter(1, getSystemFac().getHauptmandant());
		} else {
			query.setParameter(1, theClientDto.getMandant());
		}

		Collection<?> cl = query.getResultList();

		SperrenDto[] sperrenDtos = assembleSperrenDtos(cl);

		for (int i = 0; i < sperrenDtos.length; i++) {
			hmStatiMitBild.put(sperrenDtos[i].getCBez(), sperrenDtos[i].getOBild());
		}

		return hmStatiMitBild;
	}

	public VerleihDto verleihFindByPrimaryKey(Integer iId) {

		Verleih verleih = em.find(Verleih.class, iId);
		if (verleih == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei verleihFindByPrimaryKey. Es gibt keine iid " + iId);
		}
		return assembleVerleihDto(verleih);

	}

	private void setFarbcodeFromFarbcodeDto(Farbcode farbcode, FarbcodeDto farbcodeDto) {
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

	private void setLaseroberflaecheFromLaseroberflaecheDto(Laseroberflaeche bean, LaseroberflaecheDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCNr(dto.getCNr());
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

	private void setWerkzeugFromWerkzeugDto(Werkzeug bean, WerkzeugDto dto) {

		bean.setMandantCNrStandort(dto.getMandantCNrStandort());
		bean.setCBez(dto.getCBez());
		bean.setLagerplatzIId(dto.getLagerplatzIId());
		bean.setCNr(dto.getCNr());
		bean.setLieferantIId(dto.getLieferantIId());
		bean.setNKaufpreis(dto.getNKaufpreis());
		bean.setTKaufdatum(dto.getTKaufdatum());
		bean.setXKommentar(dto.getXKommentar());

		em.merge(bean);
		em.flush();
	}

	private void setVerschleissteilFromVerschleissteilDto(Verschleissteil bean, VerschleissteilDto dto) {

		bean.setCBez(dto.getCBez());
		bean.setCBez2(dto.getCBez2());

		bean.setCNr(dto.getCNr());

		em.merge(bean);
		em.flush();
	}

	private void setAutomotiveFromAutomotiveDto(Automotive bean, AutomotiveDto dto) {
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

	private void setGebindeFromGebindeDto(Gebinde bean, GebindeDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCBez(dto.getCBez());
		bean.setNMenge(dto.getNMenge());
		em.merge(bean);
		em.flush();
	}

	private void setVorschlagstextFromVorschlagstextDto(Vorschlagstext vorschlagstext,
			VorschlagstextDto vorschlagstextDto) {
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

	private VorschlagstextDto assembleVorschlagstextDto(Vorschlagstext vorschlagstext) {
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
	 * @param artikelIId   Integer
	 * @param localeCNr    String
	 * @param theClientDto String
	 * @return ArtikelsprDto
	 * @throws EJBExceptionLP
	 */
	public ArtikelsprDto artikelsprFindByArtikelIIdLocaleCNrOhneExc(Integer artikelIId, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null || localeCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || localeCNr == null"));
		}
		ArtikelsprDto artikelsprDto = null;
		try {
			Query query = em.createNamedQuery("ArtikelsprfindByArtikelIIdLocaleCNr");
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

	public VerpackungsmittelDto verpackungsmittelfindByCNrMandantCNrOhneExc(String cNr, TheClientDto theClientDto) {

		VerpackungsmittelDto verpackungmsittelDto = null;
		try {
			Query query = em.createNamedQuery("VerpackungsmittelfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, theClientDto.getMandant());
			Verpackungsmittel verpackungmsittel = (Verpackungsmittel) query.getSingleResult();
			verpackungmsittelDto = VerpackungsmittelDtoAssembler.createDto(verpackungmsittel);
		} catch (NoResultException ex) {
			// nicht gefunden
		}

		return verpackungmsittelDto;
	}

	public Integer createSperren(SperrenDto sperrenDto, TheClientDto theClientDto) throws EJBExceptionLP {

		sperrenDto.setMandantCNr(theClientDto.getMandant());
		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em.createNamedQuery("SperrenfindByCBezMandantCNr");
			query.setParameter(1, sperrenDto.getCBez());
			query.setParameter(2, sperrenDto.getMandantCNr());
			Sperren doppelt = (Sperren) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SPERREN.UK"));
		} catch (NoResultException ex) {

		}

		if (Helper.short2boolean(sperrenDto.getBDurchfertigung()) == true) {
			try {

				Query query = em.createNamedQuery("SperrenfindBDurchfertigung");
				query.setParameter(1, sperrenDto.getMandantCNr());
				Sperren doppelt = (Sperren) query.getSingleResult();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SPERREN.UK"));
			} catch (NoResultException ex) {

			}
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SPERREN);
			sperrenDto.setIId(pk);

			Sperren sperren = new Sperren(sperrenDto.getIId(), sperrenDto.getCBez(), sperrenDto.getMandantCNr(),
					sperrenDto.getBGesperrt(), sperrenDto.getBGesperrteinkauf(), sperrenDto.getBGesperrtverkauf(),
					sperrenDto.getBGesperrtlos(), sperrenDto.getBGesperrtstueckliste(), sperrenDto.getBDurchfertigung(),
					sperrenDto.getBDefaultBeiArtikelneuanlage());
			em.persist(sperren);
			em.flush();
			setSperrenFromSperrenDto(sperren, sperrenDto);
			return sperrenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createShopgruppewebshop(ShopgruppewebshopDto dto, TheClientDto theClientDto) {

		try {

			Query query = em.createNamedQuery("ShopgruppefindByShopgruppeIIdWebshopIId");
			query.setParameter("shopgruppeIId", dto.getShopgruppeIId());
			query.setParameter("webshopIId", dto.getWebshopIId());
			Shopgruppewebshop doppelt = (Shopgruppewebshop) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SHOPGRUPPWEBSHOP.UK"));
		} catch (NoResultException ex) {

		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SHOPGRUPPEWEBSHOP);
			dto.setIId(pk);

			Shopgruppewebshop sperren = new Shopgruppewebshop(dto.getIId(), dto.getShopgruppeIId(),
					dto.getWebshopIId());
			em.persist(sperren);
			em.flush();
			setShopgruppewebshopFromShopgruppewebshopDto(sperren, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createErsatztypen(ErsatztypenDto dto, TheClientDto theClientDto) {

		try {

			Query query = em.createNamedQuery("ErsatztypenfindByArtikelIIdArtikelIIdErsatz");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getArtikelIIdErsatz());
			Ersatztypen doppelt = (Ersatztypen) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ERSATZTYPEN.UK"));
		} catch (NoResultException ex) {

		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ERSATZTYPEN);
			dto.setIId(pk);

			if (dto.getISort() == null) {
				Integer i = null;
				try {
					Query querynext = em.createNamedQuery("ErsatztypenejbSelectNextReihung");
					querynext.setParameter(1, dto.getArtikelIId());
					i = (Integer) querynext.getSingleResult();
				} catch (NoResultException ex) {
				}
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				dto.setISort(i);

			}

			Ersatztypen ersatztypen = new Ersatztypen(dto.getIId(), dto.getArtikelIId(), dto.getArtikelIIdErsatz(),
					dto.getISort());
			em.persist(ersatztypen);
			em.flush();
			setErsatztypenFromErsatztypenDto(ersatztypen, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeSperren(SperrenDto dto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}
		// try {
		Sperren toRemove = em.find(Sperren.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeSperren. Es gibt keine Sperre mit iid " + dto.getIId() + "\ndto.toString(): "
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

		Shopgruppewebshop toRemove = em.find(Shopgruppewebshop.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateSperren(SperrenDto sperrenDto) throws EJBExceptionLP {
		if (sperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("sperrenDto == null"));
		}

		Integer iId = sperrenDto.getIId();
		// try {
		Sperren sperren = em.find(Sperren.class, iId);
		if (sperren == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSperren. Es gibt keine iid " + iId + "\ndto.toString(): "
							+ sperrenDto.toString());
		}

		try {
			Query query = em.createNamedQuery("SperrenfindByCBezMandantCNr");
			query.setParameter(1, sperrenDto.getCBez());
			query.setParameter(2, sperrenDto.getMandantCNr());
			Sperren sperrenVorhanden = (Sperren) query.getSingleResult();
			if (sperrenVorhanden != null && iId.equals(sperrenVorhanden.getIId()) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SPERREN.UK"));
			}

		} catch (NoResultException ex) {
		}

		if (Helper.short2boolean(sperrenDto.getBDurchfertigung()) == true) {
			try {
				Query query = em.createNamedQuery("SperrenfindBDurchfertigung");
				query.setParameter(1, sperrenDto.getMandantCNr());
				Sperren sperrenVorhanden = (Sperren) query.getSingleResult();
				if (sperrenVorhanden != null && iId.equals(sperrenVorhanden.getIId()) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_SPERREN.UK"));
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
			Query query = em.createNamedQuery("ShopgruppefindByShopgruppeIIdWebshopIId");
			query.setParameter("shopgruppeIId", dto.getShopgruppeIId());
			query.setParameter("webshopIId", dto.getWebshopIId());
			Shopgruppewebshop sperrenVorhanden = (Shopgruppewebshop) query.getSingleResult();
			if (sperrenVorhanden != null && iId.equals(sperrenVorhanden.getIId()) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_SHOPGRUPPEWEBSHOP.UK"));
			}

		} catch (NoResultException ex) {
		}
		setShopgruppewebshopFromShopgruppewebshopDto(sperren, dto);
	}

	public SperrenDto sperrenFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Sperren sperren = em.find(Sperren.class, iId);
		if (sperren == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei sperrenFindByPrimaryKey. Es gibt keine iid " + iId);
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
		Query query = em.createNamedQuery(Shopgruppewebshop.QueryFindByWebshopId);
		query.setParameter(1, webshopIId);
		Collection<Shopgruppewebshop> cl = query.getResultList();
		return ShopgruppewebshopDtoAssembler.createDtos(cl);
	}

	public ShopgruppeDto shopgruppeFindByCNrMandantOhneExc(String cnr, TheClientDto theClientDto) {
		try {
			Query query = em.createNamedQuery(Shopgruppe.QueryFindByCNrMandantCNr);
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

	public boolean isShopgruppeInWebshop(ShopgruppeDto shopgruppeDto, Integer webshopIId) {
		if (null == shopgruppeDto || webshopIId == null)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));

		if (null == shopgruppeDto.getShopgruppeIId()) {
			Query query = em.createNamedQuery(Shopgruppewebshop.QueryFindByShopgruppeIIdWebshopIId);
			query.setParameter(1, shopgruppeDto.getIId());
			query.setParameter(2, webshopIId);
			Shopgruppewebshop sgwebshop = (Shopgruppewebshop) query.getSingleResult();
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
		sperren.setBDefaultBeiArtikelneuanlage(sperrenDto.getBDefaultBeiArtikelneuanlage());
		em.merge(sperren);
		em.flush();
	}

	private void setShopgruppewebshopFromShopgruppewebshopDto(Shopgruppewebshop shopgruppewebshop,
			ShopgruppewebshopDto shopgruppewebshopDto) {
		shopgruppewebshop.setShopgruppeIId(shopgruppewebshopDto.getShopgruppeIId());
		shopgruppewebshop.setWebshopIId(shopgruppewebshopDto.getWebshopIId());
		em.merge(shopgruppewebshop);
		em.flush();
	}

	private void setErsatztypenFromErsatztypenDto(Ersatztypen ersatztypen, ErsatztypenDto ersatztypenDto) {
		ersatztypen.setArtikelIId(ersatztypenDto.getArtikelIId());
		ersatztypen.setArtikelIIdErsatz(ersatztypenDto.getArtikelIIdErsatz());
		ersatztypen.setISort(ersatztypenDto.getISort());
		em.merge(ersatztypen);
		em.flush();
	}

	private SperrenDto assembleSperrenDto(Sperren sperren) {
		return SperrenDtoAssembler.createDto(sperren);
	}

	private ShopgruppewebshopDto assembleShopgruppewebshopDto(Shopgruppewebshop shopgruppe) {
		return ShopgruppewebshopDtoAssembler.createDto(shopgruppe);
	}

	private ShopgruppewebshopDto[] assembleShopgruppewebshopDto(Collection<Shopgruppewebshop> shopgruppen) {
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

	public Integer createArtikelsperren(ArtikelsperrenDto artikelsperrenDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelsperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelsperrenDto == null"));
		}
		if (artikelsperrenDto.getArtikelIId() == null || artikelsperrenDto.getSperrenIId() == null
				|| artikelsperrenDto.getCGrund() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"artikelsperrenDto.getArtikelIId() == null || artikelsperrenDto.getSperrenIId() == null || artikelsperrenDto.getCGrund() == null"));
		}

		artikelsperrenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikelsperrenDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		try {
			// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
			Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
			query.setParameter(1, artikelsperrenDto.getArtikelIId());
			query.setParameter(2, artikelsperrenDto.getSperrenIId());
			// @todo getSingleResult oder getResultList ?
			Artikelsperren doppelt = (Artikelsperren) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELSPERREN.UK"));
		} catch (NoResultException ex) {

		}

		Query queryNext = em.createNamedQuery("ArtikelsperrenejbSelectNextReihung");
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

			Artikelsperren artikelsperren = new Artikelsperren(artikelsperrenDto.getIId(),
					artikelsperrenDto.getArtikelIId(), artikelsperrenDto.getSperrenIId(),
					artikelsperrenDto.getPersonalIIdAendern(), artikelsperrenDto.getTAendern(),
					artikelsperrenDto.getCGrund(), artikelsperrenDto.getiSort());
			em.persist(artikelsperren);
			em.flush();
			setArtikelsperrenFromArtikelsperrenDto(artikelsperren, artikelsperrenDto);

			Sperren spAktuell = em.find(Sperren.class, artikelsperrenDto.getSperrenIId());

			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE,
					null, spAktuell.getCBez(), theClientDto);
			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND, null,
					artikelsperrenDto.getCGrund(), theClientDto);

			return artikelsperrenDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public ArrayList<GebindeDto> getGebindeEinesArtikelsUndEinesLieferanten(Integer artikelIId, Integer lieferantIId,
			java.sql.Date tDatumPreisgueltigkeit, TheClientDto theClientDto) {

		ArrayList<GebindeDto> al = new ArrayList<GebindeDto>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQueryIstmaterial = "SELECT distinct al.flrgebinde.i_id, al.flrgebinde.c_bez FROM FLRArtikellieferant al WHERE al.artikel_i_id="
				+ artikelIId + " AND al.lieferant_i_id=" + lieferantIId
				+ " AND al.flrgebinde.i_id IS NOT NULL AND al.t_preisgueltigab <='"
				+ Helper.formatDateWithSlashes(tDatumPreisgueltigkeit) + "' AND al.flrgebinde.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY al.flrgebinde.c_bez";

		org.hibernate.Query istmaterial = session.createQuery(sQueryIstmaterial);

		List results = istmaterial.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			al.add(gebindeFindByPrimaryKey((Integer) o[0]));

		}
		return al;
	}

	public void removeArtikelsperren(ArtikelsperrenDto dto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		Sperren spAktuell = em.find(Sperren.class, dto.getSperrenIId());

		artikelAenderungLoggen(dto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE, spAktuell.getCBez(),
				null, theClientDto);
		artikelAenderungLoggen(dto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND, dto.getCGrund(), null,
				theClientDto);

		Artikelsperren toRemove = em.find(Artikelsperren.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikelsperren. Es gitb keine iid " + dto.getIId() + "\ndto.toString():"
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

		Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIIdOrderByISort");
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

	public void updateArtikelsperren(ArtikelsperrenDto artikelsperrenDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelsperrenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("artikelsperrenDto == null"));
		}
		if (artikelsperrenDto.getIId() == null || artikelsperrenDto.getArtikelIId() == null
				|| artikelsperrenDto.getSperrenIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"artikelsperrenDto.getIId() == null || artikelsperrenDto.getArtikelIId() == null || artikelsperrenDto.getSperrenIId() == null"));
		}

		Integer iId = artikelsperrenDto.getIId();
		// try {
		Artikelsperren artikelsperren = em.find(Artikelsperren.class, iId);
		if (artikelsperren == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelsperren. Es gibt keine iid " + iId + "\ndto.toString: "
							+ artikelsperrenDto.toString());
		}

		if (!artikelsperren.getSperrenIId().equals(artikelsperrenDto.getSperrenIId())) {

			Sperren spVorher = em.find(Sperren.class, artikelsperren.getSperrenIId());
			Sperren spAktuell = em.find(Sperren.class, artikelsperrenDto.getSperrenIId());

			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_SPERRE,
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
			artikelAenderungLoggen(artikelsperrenDto.getArtikelIId(), ArtikelFac.ARTIKEL_LOG_ARTIKELSPERREN_GRUND,
					grundVorher, grundAktuell, theClientDto);
		}

		artikelsperrenDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		artikelsperrenDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		try {
			Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
			query.setParameter(1, artikelsperrenDto.getArtikelIId());
			query.setParameter(2, artikelsperrenDto.getSperrenIId());
			Integer iIdVorhanden = ((Artikelsperren) query.getSingleResult()).getIId();

			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ARTIKELSPERREN.UK"));
			}

		} catch (NoResultException ex) {
			// nix da
		}

		setArtikelsperrenFromArtikelsperrenDto(artikelsperren, artikelsperrenDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ArtikelsperrenDto artikelsperrenFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Artikelsperren artikelsperren = em.find(Artikelsperren.class, iId);
		if (artikelsperren == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelsperrenFindByPrimaryKey. Es gibt keine iid" + iId);
		}

		return assembleArtikelsperrenDto(artikelsperren);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }

	}

	public ArtikelsperrenDto[] artikelsperrenFindByArtikelIId(Integer artikelId) throws EJBExceptionLP {

		if (artikelId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
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

	public ArtikelalergenDto[] artikelallergenFindByArtikelIId(Integer artikelIId) {
		Query query = em.createNamedQuery("ArtikelalergenfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return ArtikelalergenDtoAssembler.createDtos(cl);
	}

	public ArtikelsperrenDto artikelsperrenFindByArtikelIIdSperrenIIdOhneExc(Integer artikelId, Integer sperrenlId) {

		// try {
		Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
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

	private void setArtikelsperrenFromArtikelsperrenDto(Artikelsperren artikelsperren,
			ArtikelsperrenDto artikelsperrenDto) {
		artikelsperren.setArtikelIId(artikelsperrenDto.getArtikelIId());
		artikelsperren.setSperrenIId(artikelsperrenDto.getSperrenIId());

		artikelsperren.setCGrund(artikelsperrenDto.getCGrund());
		artikelsperren.setPersonalIIdAendern(artikelsperrenDto.getPersonalIIdAendern());
		artikelsperren.setTAendern(artikelsperrenDto.getTAendern());
		artikelsperren.setiSort(artikelsperrenDto.getiSort());

		em.merge(artikelsperren);
		em.flush();
	}

	private ArtikelsperrenDto assembleArtikelsperrenDto(Artikelsperren artikelsperren) {
		return ArtikelsperrenDtoAssembler.createDto(artikelsperren);
	}

	private ArtikelsperrenDto[] assembleArtikelsperrenDtos(Collection<?> artikelsperrens) {
		List<ArtikelsperrenDto> list = new ArrayList<ArtikelsperrenDto>();
		if (artikelsperrens != null) {
			Iterator<?> iterator = artikelsperrens.iterator();
			while (iterator.hasNext()) {
				Artikelsperren artikelsperren = (Artikelsperren) iterator.next();
				list.add(assembleArtikelsperrenDto(artikelsperren));
			}
		}
		ArtikelsperrenDto[] returnArray = new ArtikelsperrenDto[list.size()];
		return (ArtikelsperrenDto[]) list.toArray(returnArray);
	}

	public void updateTrumphtopslog(String artikelnummer, String kurzbezeichnungMaterial, String importfileName,
			BigDecimal gewicht, long iBearbeitsungszeit, BigDecimal laserkostenProStunde, Integer lagerIId,
			String mandantCNr, boolean kalkulationsart1, int mehrverbrauchfuerlaserinmm, double breiteArtikel,
			double laengeArtikel, Double hoeheArtikel, TheClientDto theClientDto) throws EJBExceptionLP {

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

			String query = "FROM FLRArtikellistespr aspr WHERE aspr.c_kbez ='" + kurzbezeichnungMaterial + "'";

			org.hibernate.Query kurzbez = session.createQuery(query);

			List subResults = kurzbez.list();

			Integer artikelIIdMaterial = null;
			if (subResults.size() == 0) {
				// PJ22747 Neuer Versuch wg. Resttafeln

				if (kurzbezeichnungMaterial.contains("-")) {

					String teil = kurzbezeichnungMaterial.substring(0, kurzbezeichnungMaterial.indexOf("-"));

					query = "FROM FLRArtikellistespr aspr WHERE  aspr.c_kbez LIKE '" + teil
							+ "%' ORDER BY (aspr.Id.artikelliste.flrgeometrie.f_breite * aspr.Id.artikelliste.flrgeometrie.f_tiefe)  DESC  ";

					kurzbez = session.createQuery(query);
					kurzbez.setMaxResults(1);

					subResults = kurzbez.list();

					if (subResults.size() > 0) {
						FLRArtikellistespr flrSpr = (FLRArtikellistespr) subResults.iterator().next();
						artikelIIdMaterial = flrSpr.getId().getArtikelliste().getI_id();

						ttlogDto.setCError("Der Artikel wurde aufgrund der Resttafelerkennung verwendet.");

					}
				}

			} else {
				FLRArtikellistespr flrSpr = (FLRArtikellistespr) subResults.iterator().next();
				artikelIIdMaterial = flrSpr.getId().getArtikelliste().getI_id();
			}

			if (artikelIIdMaterial != null) {

				ttlogDto.setArtikelIIdMaterial(artikelIIdMaterial);
				if (subResults.size() > 1) {
					ttlogDto.setCError("Es wurden mehrer Artikel mit der Kurzbezeichnung '" + kurzbezeichnungMaterial
							+ "' gefunden.");
				}
				// Laserkosten=
				BigDecimal laserkosten = new BigDecimal(iBearbeitsungszeit / (double) 3600000)
						.multiply(laserkostenProStunde);

				// PreisMNeu berechnen
				BigDecimal preisNeu = new BigDecimal(0);
				try {
					ArtikellagerDto artikellagerDto = getLagerFac().artikellagerFindByPrimaryKey(artikelIIdMaterial,
							lagerIId);

					Artikel artikelMaterial = em.find(Artikel.class, artikelIIdMaterial);

					// Laserzeit in Minutenfaktor eintragen
					artikel.setFMinutenfaktor1((double) iBearbeitsungszeit / 60000);

					// Anzahl der moeglichen Teile berechnen
					int iAnzahlDerMoeglichenTeile = 0;

					int iAnzahl_1 = 0;
					int iAnzahl_2 = 0;
					double materialLaenge = 0;
					double materialBreite = 0;

					Geometrie geometrieMaterial = em.find(Geometrie.class, artikelIIdMaterial);

					if (geometrieMaterial != null) {
						if (geometrieMaterial.getFBreite() != null) {
							materialBreite = geometrieMaterial.getFBreite();

						}
						if (geometrieMaterial.getFTiefe() != null) {
							materialLaenge = geometrieMaterial.getFTiefe();
						}
					}

					// Geometrie des Artikels zurueckschreiben
					Geometrie geometrieArtikel = em.find(Geometrie.class, artikel.getIId());
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
					laengeArtikel = laengeArtikel + 2 * mehrverbrauchfuerlaserinmm;
					breiteArtikel = breiteArtikel + 2 * mehrverbrauchfuerlaserinmm;

					iAnzahl_1 = (int) (materialLaenge / laengeArtikel) * (int) (materialBreite / breiteArtikel);
					iAnzahl_2 = (int) (materialLaenge / breiteArtikel) * (int) (materialBreite / laengeArtikel);

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

					if (artikelMaterial.getFGewichtkg() != null && iAnzahlDerMoeglichenTeile != 0) {
						artikel.setFGewichtkg(artikelMaterial.getFGewichtkg() / iAnzahlDerMoeglichenTeile);
					}

					if (kalkulationsart1 == true) {

						BigDecimal preisProKilo = artikellagerDto.getNGestehungspreis();
						if (artikelMaterial.getNUmrechnungsfaktor() != null
								&& artikelMaterial.getNUmrechnungsfaktor().doubleValue() != 0) {
							preisProKilo = artikellagerDto.getNGestehungspreis()
									.divide(artikelMaterial.getNUmrechnungsfaktor(), 4, BigDecimal.ROUND_HALF_EVEN);
						}

						// Materialkosten
						BigDecimal materialkosten = preisProKilo.multiply(gewicht);

						preisNeu = Helper.rundeKaufmaennisch(materialkosten.add(laserkosten), 4);
					} else {
						if (iAnzahlDerMoeglichenTeile == 0) {
							return;
						}

						preisNeu = Helper.rundeKaufmaennisch(artikellagerDto.getNGestehungspreis()
								.divide(new BigDecimal(iAnzahlDerMoeglichenTeile), 4, BigDecimal.ROUND_HALF_EVEN)
								.add(laserkosten), 4);
					}

					ttlogDto.setNGestpreisneu(preisNeu);

					// Gestehungspreis in Artikellager updaten
					ArtikellagerDto alDto = new ArtikellagerDto();
					alDto.setArtikelIId(artikel.getIId());
					alDto.setLagerIId(lagerIId);
					alDto.setMandantCNr(mandantCNr);
					alDto.setNGestehungspreis(preisNeu);
					getLagerFac().updateGestpreisArtikellager(alDto, theClientDto);

				} catch (RemoteException ex1) {
					// Wenn finderEx, dann kibts keinen preis
					ttlogDto.setCError("Kein Material-Gestehungspreis gefunden.");
				}

				session = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Criteria crit = session.createCriteria(FLRTrumphtopslog.class);
				crit.add(Restrictions.eq(ArtikelFac.FLR_TRUMPHTOPSLOS_ARTIKEL_I_ID, artikel.getIId()));
				crit.addOrder(Order.desc(ArtikelFac.FLR_TRUMPHTOPSLOS_T_ANLEGEN));

				crit.setMaxResults(1);
				List results = crit.list();

				if (results.size() > 0) {

					FLRTrumphtopslog log = (FLRTrumphtopslog) results.iterator().next();
					Trumphtopslog ttlogVorhanden = em.find(Trumphtopslog.class, log.getI_id());

					if (ttlogVorhanden.getNGestpreisneu().doubleValue() == ttlogDto.getNGestpreisneu().doubleValue()) {
						// Wenn der Preis gleich ist, dann auslassen
					} else {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
						ttlogDto.setIId(pk);

						erzeugeTrumphTopsLogeintrag(ttlogDto);

					}

					// Wenn der Preis geaendert ist, dann neuer Eintrag

				} else {
					// Neuer Eintrag
					PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
					Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TRUMPHTOPSLOG);
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
						+ LocaleFac.STATUS_IN_PRODUKTION + "' AND istmaterial.flrlossollmaterial.flrartikel.i_id="
						+ artikel.getIId() + " AND istmaterial.b_abgang=1";

				org.hibernate.Query istmaterial = session.createQuery(sQueryIstmaterial);

				results = istmaterial.list();
				Iterator<?> resultListIterator = results.iterator();
				while (resultListIterator.hasNext()) {

					Object[] o = (Object[]) resultListIterator.next();

					BigDecimal summeAblieferungen = (BigDecimal) o[1];

					if (summeAblieferungen == null || summeAblieferungen.doubleValue() == 0) {

						FLRLosistmaterial flrLosistmaterial = (FLRLosistmaterial) o[0];

						try {
							BigDecimal bdPreisAlt = getLagerFac().getGestehungspreisEinerAbgangsposition(
									LocaleFac.BELEGART_LOS, flrLosistmaterial.getI_id(), null);

							if (preisNeu.doubleValue() != bdPreisAlt.doubleValue()) {
								// Preis in Lager updaten

								LagerbewegungDto lagerbewegungDto = getLagerFac()
										.getLetzteintrag(LocaleFac.BELEGART_LOS, flrLosistmaterial.getI_id(), null);

								if (lagerbewegungDto != null) {

									// Hole Urspruenge dazu
									LagerabgangursprungDto[] urspruenge = getLagerFac()
											.lagerabgangursprungFindByLagerbewegungIIdBuchung(
													lagerbewegungDto.getIIdBuchung());

									for (int i = 0; i < urspruenge.length; i++) {

										LagerbewegungDto[] lagerbewegungDtos = getLagerFac()
												.lagerbewegungFindByIIdBuchung(
														urspruenge[i].getILagerbewegungidursprung());

										// Preis in Lager updaten
										getLagerFac().bucheZu(lagerbewegungDtos[0].getCBelegartnr(),
												lagerbewegungDtos[0].getIBelegartid(),
												lagerbewegungDtos[0].getIBelegartpositionid(),
												lagerbewegungDtos[0].getArtikelIId(), lagerbewegungDtos[0].getNMenge(),
												preisNeu, lagerbewegungDtos[0].getLagerIId(), null,
												lagerbewegungDtos[0].getTBelegdatum(), theClientDto, null, null, true);

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
				ttlogDto.setCError(
						"Kein Materialartikel mit der Kurzbezeichnung '" + kurzbezeichnungMaterial + "' gefunden");
				ttlogDto.setNGestpreisneu(new BigDecimal(0));
				erzeugeTrumphTopsLogeintrag(ttlogDto);

			}
		} catch (NoResultException ex) {
			// Fehler
			ttlogDto.setCError("Kein Artikel mit der Nummer '" + artikelnummer + "' gefunden");
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

	private void setTrumphtopslogFromTrumphtopslogDto(Trumphtopslog trumphtopslog, TrumphtopslogDto trumphtopslogDto) {
		trumphtopslog.setCImportfilename(trumphtopslogDto.getCImportfilename());
		trumphtopslog.setCError(trumphtopslogDto.getCError());
		trumphtopslog.setArtikelIId(trumphtopslogDto.getArtikelIId());
		trumphtopslog.setArtikelIIdMaterial(trumphtopslogDto.getArtikelIIdMaterial());
		trumphtopslog.setNGewicht(trumphtopslogDto.getNGewicht());
		trumphtopslog.setNGestpreisneu(trumphtopslogDto.getNGestpreisneu());
		trumphtopslog.setIBearbeitungszeit(trumphtopslogDto.getIBearbeitungszeit());
		trumphtopslog.setTAnlegen(trumphtopslogDto.getTAnlegen());
		em.merge(trumphtopslog);
		em.flush();
	}

	private TrumphtopslogDto assembleTrumphtopslogDto(Trumphtopslog trumphtopslog) {
		return TrumphtopslogDtoAssembler.createDto(trumphtopslog);
	}

	private TrumphtopslogDto[] assembleTrumphtopslogDtos(Collection<?> trumphtopslogs) {
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

	public Integer createZugehoerige(ZugehoerigeDto zugehoerigeDto) throws EJBExceptionLP {
		if (zugehoerigeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("zugehoerigeDto == null"));
		}
		if (zugehoerigeDto.getArtikelIId() == null || zugehoerigeDto.getArtikelIIdZugehoerig() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL, new Exception(
					"zugehoerigeDto.getArtikelIId() == null || zugehoerigeDto.getArtikelIIdZugehoerig() == null"));
		}
		try {
			Query query = em.createNamedQuery("ZugehoerigefindByArtikelIIdArtikelIIdZugehoerig");
			query.setParameter(1, zugehoerigeDto.getArtikelIId());
			query.setParameter(2, zugehoerigeDto.getArtikelIIdZugehoerig());
			// @todo getSingleResult oder getResultList ?
			Zugehoerige doppelt = (Zugehoerige) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ZUGEHOERIGE.UK"));
		} catch (NoResultException ex) {

		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ZUGEHOERIGE);
			zugehoerigeDto.setIId(pk);

			Zugehoerige zugehoerige = new Zugehoerige(zugehoerigeDto.getIId(), zugehoerigeDto.getArtikelIId(),
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		// try {
		Zugehoerige toRemove = em.find(Zugehoerige.class, dto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeZugehoerige. Es gibt keine iid " + dto.getIId() + "\ndto.toString: "
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

	public void updateZugehoerige(ZugehoerigeDto zugehoerigeDto) throws EJBExceptionLP {
		if (zugehoerigeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("zugehoerigeDto == null"));
		}
		if (zugehoerigeDto.getIId() == null || zugehoerigeDto.getArtikelIId() == null
				|| zugehoerigeDto.getArtikelIIdZugehoerig() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL, new Exception(
					"zugehoerigeDto.getIId() == null || zugehoerigeDto.getArtikelIId() == null || zugehoerigeDto.getArtikelIIdZugehoerig() == null"));
		}

		Integer iId = zugehoerigeDto.getIId();

		// try {
		Query query = em.createNamedQuery("ZugehoerigefindByArtikelIIdArtikelIIdZugehoerig");
		query.setParameter(1, zugehoerigeDto.getArtikelIId());
		query.setParameter(2, zugehoerigeDto.getArtikelIIdZugehoerig());
		// @todo getSingleResult oder getResultList ?
		Integer iIdVorhanden = ((Zugehoerige) query.getSingleResult()).getIId();
		if (iId.equals(iIdVorhanden) == false) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_ZUGEHOERIGE.UK"));
		}

		// }
		// catch (FinderException ex) {
		//
		// }

		// try {
		Zugehoerige zugehoerige = em.find(Zugehoerige.class, iId);
		if (zugehoerige == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateZugehoerige. Es gibt keine iid " + iId + "\ndto.toString(): "
							+ zugehoerigeDto.toString());
		}
		setZugehoerigeFromZugehoerigeDto(zugehoerige, zugehoerigeDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ZugehoerigeDto zugehoerigeFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Zugehoerige zugehoerige = em.find(Zugehoerige.class, iId);
		if (zugehoerige == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei zugehoerigeFindByPrimaryKey. Es gibt keine iid " + iId);
		}

		return assembleZugehoerigeDto(zugehoerige);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);
		// }
	}

	public Integer[] getZugehoerigeArtikel(Integer artikelIId) throws EJBExceptionLP {
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

	private void setZugehoerigeFromZugehoerigeDto(Zugehoerige zugehoerige, ZugehoerigeDto zugehoerigeDto) {
		zugehoerige.setArtikelIId(zugehoerigeDto.getArtikelIId());
		zugehoerige.setArtikelIIdZugehoerig(zugehoerigeDto.getArtikelIIdZugehoerig());
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
	public void printArtikeletikettOnServer(@WebParam(name = "sArtikelnr") String sArtikelnr,
			@WebParam(name = "sPrinter") String sPrinter, @WebParam(name = "idUser") String idUser) {
		try {
			TheClientDto theClientDto = getTheClient(idUser);
			ArtikelDto artikelDto = artikelFindByCNr(sArtikelnr, theClientDto);
			JasperPrintLP print = getArtikelReportFac().printArtikeletikett(artikelDto.getIId(), "", new BigDecimal(1),
					1, null, null, null, null, theClientDto);

			HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(sPrinter);
			if (getServerDruckerFacLocal().exists(hvPrinter))
				getServerDruckerFacLocal().print(print, hvPrinter);
		} catch (Throwable e) {
			myLogger.error("Error waehrend des Artikeletikettdrucks ueber Server", e);
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
	public void setArtikelLagerort(@WebParam(name = "sArtikelnr") String sArtikelnr,
			@WebParam(name = "sLagerort") String sLagerort, @WebParam(name = "idUser") String idUser) {
		TheClientDto theClientDto = getTheClient(idUser);
		ArtikelDto artikelDto = artikelFindByCNr(sArtikelnr, theClientDto);
	}

	public void vertauscheShopgruppen(Integer pos1, Integer pos2) throws EJBExceptionLP {
		ShopgruppeISort helper = new ShopgruppeISort(em);
		helper.tausche(pos1, pos2);
	}

	@Override
	public void updateArtikelAusImportResult(IStklImportResult result, TheClientDto theClientDto)
			throws RemoteException {
		if (result == null || result.getSelectedArtikelDto() == null)
			return;

		ArtikelDto artikelDtoDB = artikelFindByPrimaryKeySmall(result.getSelectedArtikelDto().getIId(), theClientDto);

		boolean artikelNeedsUpdate = false;
		String herstellerArtNr = result.getValues().get(StklImportSpezifikation.HERSTELLERARTIKELNUMMER);
		if (updateNeeded(herstellerArtNr, artikelDtoDB.getCArtikelnrhersteller())) {
			artikelNeedsUpdate = true;
			artikelDtoDB.setCArtikelnrhersteller(herstellerArtNr);
		}

		String herstellerCBez = result.getValues().get(StklImportSpezifikation.HERSTELLERBEZ);
		if (updateNeeded(herstellerCBez, artikelDtoDB.getCArtikelbezhersteller())) {
			artikelNeedsUpdate = true;
			artikelDtoDB.setCArtikelbezhersteller(
					Helper.cutString(herstellerCBez, ArtikelFac.MAX_ARTIKEL_HERSTELLERBEZEICHNUNG));
		}

		String herstellerCNr = Helper.cutString(result.getValues().get(StklImportSpezifikation.HERSTELLER),
				ArtikelFac.MAX_HERSTELLER_NAME);
		if (!Helper.isStringEmpty(herstellerCNr)) {
			Hersteller hersteller = HerstellerQuery.resultByCNr(herstellerCNr, em);
			if (hersteller != null && !hersteller.getIId().equals(artikelDtoDB.getHerstellerIId())) {
				artikelNeedsUpdate = true;
				artikelDtoDB.setHerstellerIId(hersteller.getIId());
			}
		}

		if (artikelNeedsUpdate) {
			updateArtikel(artikelDtoDB, theClientDto);
		}
	}

	/**
	 * Vergleicht zwei Strings miteinander, um zu &uuml;berpr&uuml;fen, ob ein
	 * Update des DB-Eintrags n&ouml;tig ist.
	 * 
	 * @param newValue neuer zu vergleichender Wert
	 * @param dbValue  Basiswert aus DB
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

	@Override
	public void updateArtikellieferantOrCreateIfNotExist(Integer artikelIId, Integer lieferantIId,
			String lieferantenArtikelCNr, BigDecimal nettopreis, TheClientDto theClientDto) {
		if (artikelIId == null || lieferantIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || lieferantIId == null"));
		}

		ArtikellieferantDto artikellieferantDto = artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
				artikelIId, lieferantIId, getDate(), null, theClientDto);

		if (artikellieferantDto == null) {
			artikellieferantDto = new ArtikellieferantDto();
			artikellieferantDto.setArtikelIId(artikelIId);
			artikellieferantDto.setTPreisgueltigab(getTimestamp());
			artikellieferantDto.setCArtikelnrlieferant(lieferantenArtikelCNr);
			artikellieferantDto.setLieferantIId(lieferantIId);
			artikellieferantDto.setBRabattbehalten(Helper.getShortFalse());
			artikellieferantDto.setNEinzelpreis(nettopreis);
			artikellieferantDto.setNNettopreis(nettopreis);
			artikellieferantDto.setFRabatt(new Double(0));
			artikellieferantDto.setIWiederbeschaffungszeit(0);
			createArtikellieferant(artikellieferantDto, theClientDto);

		} else if (artikellieferantDto.getCArtikelnrlieferant() == null
				|| artikellieferantDto.getCArtikelnrlieferant().isEmpty()
				|| !lieferantenArtikelCNr.equals(artikellieferantDto.getCArtikelnrlieferant())) {
			artikellieferantDto.setCArtikelnrlieferant(lieferantenArtikelCNr);
			updateArtikellieferant(artikellieferantDto, theClientDto);
		}
	}

	@Override
	public ArtikelDto artikelFindBy4VendingIdOhneExc(String fourVendingId, TheClientDto theClientDto) {
		try {
			Artikel artikel = ArtikelQuery.resultByMandantCNr4VendingId(em, theClientDto.getMandant(), fourVendingId);
			return artikel != null ? assembleArtikelDto(artikel) : null;
		} catch (NoResultException ex) {
			return null;
		} catch (NonUniqueResultException ex) {
			return null;
		}
	}

	@Override
	public List<ArtikelDto> artikelFindByMandantCNr4VendingIdNotNull(String mandantCNr, TheClientDto theClientDto) {
		List<Artikel> artikelList = ArtikelQuery.listByMandantCNr4VendingIdNotNull(em, mandantCNr);
		List<ArtikelDto> artikelListFull = new ArrayList<ArtikelDto>();
		for (Artikel artikel : artikelList) {
			artikelListFull.add(artikelFindByPrimaryKey(artikel.getIId(), theClientDto));
		}

		return artikelListFull;
	}

	@Override
	public List<EinkaufseanDto> einkaufseanFindByArtikelIId(Integer artikelIId) throws RemoteException {
		List<Einkaufsean> einkaufseanList = EinkaufseanQuery.listByArtikelIId(em, artikelIId);
		List<EinkaufseanDto> einkaufseanDtoList = new ArrayList<EinkaufseanDto>();
		for (Einkaufsean einkaufsean : einkaufseanList) {
			einkaufseanDtoList.add(assembleEinkaufseanDto(einkaufsean));
		}

		return einkaufseanDtoList;
	}

	@Override
	public VendidataArticleExportResult exportiere4VendingArtikel(boolean checkOnly, TheClientDto theClientDto)
			throws RemoteException {
		VendidataArticleExportBeanHolder artikelBeanHolder = new VendidataArticleExportBeanHolder(getArtikelFac(),
				getVkPreisfindungFac(), getMandantFac());
		IVendidataArticleExportBeanServices artikelBeanServices = new VendidataArticleExportBeanServices(theClientDto,
				artikelBeanHolder);
		VendidataArticleExportTransformer exporter = new VendidataArticleExportTransformer(artikelBeanServices);
		try {
			VendidataXmlArticleTransformResult exportResult = exporter.export4VendingArticles();
			if (checkOnly) {
				return new VendidataArticleExportResult("", exportResult.getExportErrors(), exportResult.getStats());
			}
			VendidataArticlesMarshaller marshaller = new VendidataArticlesMarshaller();
			String xmlString = marshaller.marshal(exportResult.getXmlArticles());
			return new VendidataArticleExportResult(xmlString, exportResult.getExportErrors(), exportResult.getStats());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		} catch (JAXBException e) {
			throw new EJBExceptionLP(e);
		} catch (SAXException e) {
			throw new EJBExceptionLP(e);
		}

		return null;
	}

	@Override
	public Integer generiere4VendingId(Integer artikelIId, TheClientDto theClientDto) throws RemoteException {
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer highest4Vending = pkGen.getLastPrimaryKey(PKConst.PK_4VENDINGID);
		if (highest4Vending == 0) {
			highest4Vending = ArtikelQuery.resultMaxCUl(em);
			pkGen.createSequenceIfNotExists(PKConst.PK_4VENDINGID, highest4Vending);
		}
		Integer nextPk = pkGen.getNextPrimaryKey(PKConst.PK_4VENDINGID);
		Artikel artikelNextPk = ArtikelQuery.resultByMandantCNr4VendingId(em, theClientDto.getMandant(),
				nextPk.toString());
		if (artikelNextPk != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "Generierte 4Vending-Id bereits vorhanden!");
		}

		Artikel artikel = em.find(Artikel.class, artikelIId);
		artikel.setCUL(nextPk.toString());
		em.merge(artikel);
		em.flush();

		return nextPk;
	}

	@Override
	public void delete4VendingId(Integer artikelIId, TheClientDto theClientDto) throws RemoteException {
		Artikel artikel = em.find(Artikel.class, artikelIId);
		artikel.setCUL(null);
		em.merge(artikel);
		em.flush();
	}

	public ArtikelDto artikelFindByEanFuerSchnellerfassung(String ean, TheClientDto theClientDto) {
		EinkaufseanDto eanDto = einkaufseanFindByCEan(ean);
		if (eanDto != null) {
			return artikelFindByPrimaryKey(eanDto.getArtikelIId(), theClientDto);
		} else {

			Query query = em.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr");
			query.setParameter(1, ean);
			query.setParameter(2, theClientDto.getMandant());
			List l = query.getResultList();
			if (l.size() > 0) {
				Artikel a = (Artikel) l.iterator().next();
				return artikelFindByPrimaryKey(a.getIId(), theClientDto);
			} else {
				query = em.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr");
				query.setParameter(1, ean);
				query.setParameter(2, theClientDto.getMandant());
				l = query.getResultList();
				if (l.size() > 0) {
					Artikel a = (Artikel) l.iterator().next();
					return artikelFindByPrimaryKey(a.getIId(), theClientDto);
				}
			}

			return null;
		}

	}

	public ArtikelMitVerpackungsgroessenDto artikelFindByEanMandantCnr(String ean, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notEmpty(ean, "ean");

		EinkaufseanDto eanDto = einkaufseanFindByCEan(ean);
		if (eanDto != null) {
			return artikelMitVerpackungsEan(eanDto, theClientDto);
		}

		return findArtikelByEan(ean, theClientDto);
	}

	private ArtikelMitVerpackungsgroessenDto artikelMitVerpackungsEan(EinkaufseanDto eanDto, TheClientDto theClientDto)
			throws RemoteException {
		Artikel artikel = em.find(Artikel.class, eanDto.getArtikelIId());
		if (artikel != null && artikel.getMandantCNr().equals(theClientDto.getMandant())) {
			List<EinkaufseanDto> eanDtos = einkaufseanFindByArtikelIId(eanDto.getArtikelIId());
			return new ArtikelMitVerpackungsgroessenDto(artikelFindByPrimaryKey(eanDto.getArtikelIId(), theClientDto),
					eanDtos);
		}

		return null;
	}

	private ArtikelMitVerpackungsgroessenDto findArtikelByEan(String ean, TheClientDto theClientDto) {
		Integer artikelId = findByVerkaufsEan(ean, theClientDto.getMandant()).getIId();
		if (artikelId == null) {
			artikelId = findByVerpackungsEan(ean, theClientDto.getMandant()).getIId();
		}

		return artikelId == null ? null
				: new ArtikelMitVerpackungsgroessenDto(artikelFindByPrimaryKey(artikelId, theClientDto));
	}

	private Artikel findByVerkaufsEan(String ean, String mandantCnr) {
		List<Artikel> artikels = ArtikelQuery.listByVerkaufsEanMandantCnr(em, ean, mandantCnr);
		return artikels.size() == 0 ? new Artikel() : artikels.get(0);
	}

	private Artikel findByVerpackungsEan(String ean, String mandantCnr) {
		List<Artikel> artikels = ArtikelQuery.listByVerpackungsEanMandantCnr(em, ean, mandantCnr);
		return artikels.size() == 0 ? new Artikel() : artikels.get(0);
	}

	public List<ArtikelDto> artikelFindByCKBezOhneExc(String cKbez, TheClientDto theClientDto) {
		Validator.notEmpty(cKbez, "cKbez");
		Validator.notNull(theClientDto, "theClientDto");

		List<Artikelspr> artikelsprList = ArtikelsprQuery.listByCKBez(em, cKbez, theClientDto.getLocMandantAsString());
		List<ArtikelDto> dtos = new ArrayList<ArtikelDto>();
		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		for (Artikelspr spr : artikelsprList) {
			ArtikelDto artikelDto = artikelFindByPrimaryKeySmallOhneExc(spr.getPk().getArtikelIId(), theClientDto);
			if (artikelDto != null
					&& (bZentralerArtikelstamm || theClientDto.getMandant().equals(artikelDto.getMandantCNr()))) {
				dtos.add(artikelDto);
			}
		}
		return dtos;
	}

	public ArtikelDto artikelFindByArtikelnrlieferant(String artikelnrlieferant, Integer lieferantId,
			TheClientDto theClientDto) {
		List<Artikellieferant> items = ArtikellieferantQuery.listByArtikelnrlieferantLieferantIId(em,
				artikelnrlieferant, lieferantId);
		if (items.size() > 0) {
			return artikelFindByPrimaryKey(items.get(0).getArtikelIId(), theClientDto);
		}

		return null;
	}

	// PJ21008
	public List<ArtikelDto> artikelFindByHerstellernummerausBarcode(String herstellernummer,
			TheClientDto theClientDto) {

		List<ArtikelDto> dtos = new ArrayList<ArtikelDto>();

		String mandantCnr = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto) ? getSystemFac().getHauptmandant()
						: theClientDto.getMandant();

		String sQuery = "SELECT h.c_leadin FROM FLRHersteller h WHERE h.c_leadin IS NOT NULL ORDER BY length(h.c_leadin) DESC, h.c_leadin )";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createQuery(sQuery);
		List results = query.list();
		Iterator it = results.iterator();

		LinkedHashSet lhm = new LinkedHashSet();

		while (it.hasNext()) {
			String sLeadIn = (String) it.next();

			if (herstellernummer.startsWith(sLeadIn)) {
				herstellernummer = herstellernummer.substring(sLeadIn.length());
				break;
			}

		}

		sQuery = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='" + mandantCnr
				+ "' AND (a.c_artikelnrhersteller LIKE '" + herstellernummer + "|%' OR a.c_artikelnrhersteller LIKE '%|"
				+ herstellernummer + "|%' OR a.c_artikelnrhersteller LIKE '%|" + herstellernummer
				+ "' OR a.c_artikelnrhersteller ='" + herstellernummer + "')";

		Session session2 = FLRSessionFactory.getFactory().openSession();

		query = session2.createQuery(sQuery);
		results = query.list();
		Iterator it2 = results.iterator();

		while (it2.hasNext()) {
			FLRArtikel a = (FLRArtikel) it2.next();

			dtos.add(artikelFindByPrimaryKeySmall(a.getI_id(), theClientDto));

		}

		session.close();
		return dtos;

	}

	public List<ArtikelDto> artikelFindByArtikelnrHersteller(String artikelnrhersteller, TheClientDto theClientDto) {
		String mandantCnr = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto) ? getSystemFac().getHauptmandant()
						: theClientDto.getMandant();
		List<Artikel> items = ArtikelQuery.listByArtikelnrherstellerMandantCnr(em, artikelnrhersteller, mandantCnr);
		List<ArtikelDto> dtos = new ArrayList<ArtikelDto>();
		for (Artikel item : items) {
			dtos.add(assembleArtikelDto(item));
		}

		return dtos;
	}

	@Override
	public String generiereGTIN13VerkaufseanNummer(Integer artikelIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		Validator.pkFieldNotNull(artikelIId, "artikelIId");
		String companyPrefix = getParameterFac().getGS1BasisnummerGTIN(theClientDto.getMandant());
		if (Helper.isStringEmpty(companyPrefix)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_PARAMETER_NICHT_DEFINIERT,
					"Parameter der Basisnummer fuer GTIN-Generierung ist nicht definiert");
		}

		if (companyPrefix.length() != 7 && companyPrefix.length() != 9) {
			throw EJBExcFactory.gtinBasisnummerLaengeUngueltig(ParameterFac.PARAMETER_GS1_BASISNUMMER_GTIN,
					companyPrefix);
		}

		GTINVerkaufseanHelper gtinHelper = new GTINVerkaufseanHelper();
		return gtinHelper.generiereGTIN13Verkaufseannummer(companyPrefix, theClientDto);
	}

	private class GTINVerkaufseanHelper {
		private int companyPrefixLength;
		private int itemReferenceLength;

		public String generiereGTIN13Verkaufseannummer(String companyPrefix, TheClientDto theClientDto)
				throws RemoteException, EJBExceptionLP {
			initLengths(companyPrefix);
			GTIN13 gtin13;
			Integer currentValueItemReference = getGTINGeneratorFac().getCurrentValueItemReference(companyPrefix,
					theClientDto.getMandant());
			if (currentValueItemReference != null && getHighestPossibleItemReference() <= currentValueItemReference) {
				// hoechste laufende Artikelnummer erreicht, suche nach Luecken
				int nextNumber = getNextPossibleGTIN13VerkaufseanNummer(companyPrefix, 0, theClientDto);
				getGTINGeneratorFac().setDefaultValueItemReference(companyPrefix, nextNumber - 1, theClientDto);
				gtin13 = getGTINGeneratorFac().generiereGTIN13(companyPrefix, theClientDto);

			} else {
				gtin13 = getGTINGeneratorFac().generiereGTIN13(companyPrefix, theClientDto);
			}
			List<Artikel> list = ArtikelQuery.listByVerkaufsEanMandantCnr(em, gtin13.asString(),
					theClientDto.getMandant());

			if (!list.isEmpty()) {
				int nextNumber = getNextPossibleGTIN13VerkaufseanNummer(companyPrefix, currentValueItemReference,
						theClientDto);
				getGTINGeneratorFac().setDefaultValueItemReference(companyPrefix, nextNumber - 1, theClientDto);
				gtin13 = getGTINGeneratorFac().generiereGTIN13(companyPrefix, theClientDto);
			}

			return gtin13.asString();
		}

		private void initLengths(String companyPrefix) {
			companyPrefixLength = companyPrefix.length();
			itemReferenceLength = 13 - companyPrefixLength - 1;
		}

		private Integer getNextPossibleGTIN13VerkaufseanNummer(String companyPrefix, int startNumber,
				TheClientDto theClientDto) {
			Session session = FLRSessionFactory.getFactory().openSession();

			String mandant = theClientDto.getMandant();
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				mandant = getSystemFac().getHauptmandant();
			}

			int checkDigitIndex = companyPrefixLength + itemReferenceLength;
			String sQuery = "SELECT substring(a.c_verkaufseannr," + (companyPrefix.length() + 1) + ","
					+ (itemReferenceLength) + ") FROM FLRArtikel a WHERE a.mandant_c_nr='" + mandant
					+ "' AND a.c_verkaufseannr LIKE '" + companyPrefix + "%' ORDER BY substring(a.c_verkaufseannr,"
					+ (companyPrefix.length() + 1) + "," + (itemReferenceLength) + ") ASC";

			org.hibernate.Query queryS = session.createQuery(sQuery);
			List<String> results = queryS.list();

			for (int i = 0; i < results.size(); i++) {
				int current = Integer.parseInt(results.get(i));
				if (current < startNumber)
					continue;

				if (i == 0) {
					// 1 nicht besetzt
					if (current > 1)
						return 1;
				}
				if (i + 1 >= results.size())
					break;

				int next = Integer.parseInt(results.get(i + 1));

				if (current + 1 < next) {
					session.close();
					return current + 1;
				}

				if (next >= getHighestPossibleItemReference()) {
					throw EJBExcFactory.gtinGenerierungAlleArtikelnummernVergeben(companyPrefix,
							getHighestPossibleItemReference());
				}
			}

			session.close();
			String highest = results.get(results.size() - 1);
			return Integer.parseInt(highest) + 1;
		}

		private Integer getHighestPossibleItemReference() {
			if (itemReferenceLength == 5) {
				return 99999;
			}
			return 999;
		}
	}

	@Override
	public WebshopConnectionDto webshopConnectionFindByPrimaryKey(WebshopId shopId) {
		Validator.notNull(shopId, "shopId");
		WebshopConnectionDto dto = new WebshopConnectionDto();
		dto.setShopId(shopId);
		dto.setUrl("http://ubu16mag:80/mag2/");
		dto.setUser("user");
		dto.setPassword("39tdu0la1y08bw5ukp8cf6lgwyorx2nq");
		return dto;
	}

	private ModelMapper mapper = null;

	protected ModelMapper getMapper() {
		if (mapper == null) {
			mapper = new ModelMapper();
		}
		return mapper;
	}

	@Override
	public Map<String, String> getAllSprWebshoparten(String cNrSpracheI) throws EJBExceptionLP {
		TreeMap<String, String> tmArten = new TreeMap<String, String>();

		List<Webshopart> entities = WebshopartQuery.listAll(em);
		for (Webshopart webshopart : entities) {
			String key = webshopart.getCnr();
			String value = null;

			Webshopartspr webshopartspr = em.find(Webshopartspr.class,
					new WebshopartsprPK(cNrSpracheI, webshopart.getCnr()));
			value = webshopartspr == null ? webshopart.getCnr() : webshopartspr.getCBez();
			tmArten.put(key, value);
		}
		return tmArten;
	}

	public WebshopShopgruppeDto webshopShopgruppeFindByShopShopgruppe(WebshopId shopId, ShopgruppeId shopgruppeId) {
		WebshopShopgruppeDto dto = webshopShopgruppeFindByShopShopgruppeNoExc(shopId, shopgruppeId);
		Validator.entityFound(dto, shopgruppeId.id());
		return dto;
	}

	public WebshopShopgruppeDto webshopShopgruppeFindByShopShopgruppeNoExc(WebshopId shopId,
			ShopgruppeId shopgruppeId) {
		Validator.notNull(shopId, "shopId");
		Validator.notNull(shopgruppeId, "shopgruppeId");

		WebshopShopgruppe entity = WebshopShopgruppeQuery.findByShopIdShopgruppeId(em, shopId, shopgruppeId);
		return entity == null ? null : getMapper().map(entity, WebshopShopgruppeDto.class);
	}

	@Override
	public WebshopShopgruppeDto webshopShopgruppeFindByShopExternalIdNull(WebshopId shopId, String externalId) {
		Validator.notNull(shopId, "shopId");
		Validator.notEmpty(externalId, "externalId");

		WebshopShopgruppe entity = WebshopShopgruppeQuery.findByShopIdExternalId(em, shopId, externalId);
		return entity == null ? null : getMapper().map(entity, WebshopShopgruppeDto.class);
	}

	public Integer createWebshopShopgruppe(WebshopShopgruppeDto wssgDto) {
		Validator.dtoNotNull(wssgDto, "wssgDto");

		WebshopShopgruppe entity = WebshopShopgruppeQuery.findByShopIdShopgruppeId(em,
				new WebshopId(wssgDto.getWebshopId()), new ShopgruppeId(wssgDto.getShopgruppeId()));
		if (entity != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOPSHOPGRUPPE.UK"));
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOPSHOPGRUPPE);

			WebshopShopgruppe bean = new WebshopShopgruppe(pk, wssgDto.getWebshopId(), wssgDto.getShopgruppeId(),
					wssgDto.getExternalId(), wssgDto.getPfad());
			Timestamp t = getTimestamp();
			bean.setTAnlegen(t);
			bean.setTAendern(t);
			em.persist(bean);
			em.flush();

			myLogger.warn("webshopshopgruppe id(" + pk + "), shop " + bean.getWebshopId() + ", gruppe "
					+ bean.getShopgruppeIId() + ", externalId " + bean.getExternalId() + " created.");
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public void removeWebshopShopgruppe(Integer webshopShopgruppeId) {
		Validator.notNull(webshopShopgruppeId, "webshopShopgruppeId");

		WebshopShopgruppe entity = em.find(WebshopShopgruppe.class, webshopShopgruppeId);
		if (entity != null) {
			em.remove(entity);
			em.flush();
		} else {
			myLogger.warn("removeWebshopShopgruppe Id:" + webshopShopgruppeId + ", doesn't exist!");
		}
	}

	@Override
	public WebshopArtikelDto webshopArtikelFindByShopArtikel(WebshopId shopId, ArtikelId artikelId) {
		WebshopArtikelDto dto = webshopArtikelFindByShopArtikelNoExc(shopId, artikelId);
		Validator.entityFound(dto, artikelId.id());
		return dto;
	}

	@Override
	public WebshopArtikelDto webshopArtikelFindByShopArtikelNoExc(WebshopId shopId, ArtikelId artikelId) {
		Validator.notNull(shopId, "shopId");
		Validator.notNull(artikelId, "artikelId");

		WebshopArtikel entity = WebshopArtikelQuery.findByShopIdArtikelId(em, shopId, artikelId);
		return entity == null ? null : getMapper().map(entity, WebshopArtikelDto.class);
	}

	@Override
	public WebshopArtikelDto webshopArtikelFindByShopExternalIdNull(WebshopId shopId, String externalId) {
		Validator.notNull(shopId, "shopId");
		Validator.notEmpty(externalId, "externalId");

		WebshopArtikel entity = WebshopArtikelQuery.findByShopIdExternalId(em, shopId, externalId);
		return entity == null ? null : getMapper().map(entity, WebshopArtikelDto.class);
	}

	@Override
	public Integer createWebshopArtikel(WebshopArtikelDto wsaDto) {
		Validator.dtoNotNull(wsaDto, "wsaDto");

		WebshopArtikel entity = WebshopArtikelQuery.findByShopIdArtikelId(em, wsaDto.getWebshopId(),
				wsaDto.getArtikelId());
		if (entity != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOPARTIKEL.UK"));
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOPARTIKEL);

			WebshopArtikel bean = new WebshopArtikel(pk, wsaDto.getWebshopId().id(), wsaDto.getArtikelId().id(),
					wsaDto.getExternalId());
			Timestamp t = getTimestamp();
			bean.setTAnlegen(t);
			bean.setTAendern(t);
			em.persist(bean);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public WebshopArtikelPreislisteDto webshopPreislisteFindByShopPreisliste(WebshopId shopId,
			PreislisteId preislisteId) {
		WebshopArtikelPreislisteDto dto = webshopPreislisteFindByShopPreislisteNoExc(shopId, preislisteId);
		Validator.entityFound(dto, preislisteId.id());
		return dto;
	}

	@Override
	public WebshopArtikelPreislisteDto webshopPreislisteFindByShopPreislisteNoExc(WebshopId shopId,
			PreislisteId preislisteId) {
		Validator.notNull(shopId, "shopId");
		Validator.notNull(preislisteId, "preislisteId");

		WebshopArtikelPreisliste entity = WebshopPreislisteQuery.findByShopIdPreislisteId(em, shopId, preislisteId);
		return entity == null ? null : getMapper().map(entity, WebshopArtikelPreislisteDto.class);
	}

	@Override
	public Integer createWebshopPreisliste(WebshopArtikelPreislisteDto wspDto) {
		Validator.dtoNotNull(wspDto, "wspDto");

		WebshopArtikelPreisliste entity = WebshopPreislisteQuery.findByShopIdPreislisteId(em, wspDto.getWebshopId(),
				wspDto.getPreislisteId());
		if (entity != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOPPREISLISTE.UK"));
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOPPREISLISTE);

			WebshopArtikelPreisliste bean = new WebshopArtikelPreisliste(pk, wspDto.getWebshopId().id(),
					wspDto.getPreislisteId().id(), wspDto.getExternalId());
			Timestamp t = getTimestamp();
			bean.setTAnlegen(t);
			bean.setTAendern(t);
			em.persist(bean);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public WebshopMwstsatzbezDto webshopMwstsatzbezFindByShopMwstsatzbez(WebshopId shopId, MwstsatzbezId mwstsatzId) {
		WebshopMwstsatzbezDto dto = webshopMwstsatzbezFindByShopMwstsatzbezNoExc(shopId, mwstsatzId);
		Validator.entityFound(dto, mwstsatzId.id());
		return dto;
	}

	@Override
	public WebshopMwstsatzbezDto webshopMwstsatzbezFindByShopMwstsatzbezNoExc(WebshopId shopId,
			MwstsatzbezId mwstsatzbezId) {
		Validator.notNull(shopId, "shopId");
		Validator.notNull(mwstsatzbezId, "mwstsatzbezId");

		WebshopMwstsatzbez entity = WebshopMwstsatzbezQuery.findByShopIdMwstsatzbezId(em, shopId, mwstsatzbezId);
		return entity == null ? null : getMapper().map(entity, WebshopMwstsatzbezDto.class);
	}

	@Override
	public Integer createWebshopMwstsatzbez(WebshopMwstsatzbezDto wsmDto) {
		Validator.dtoNotNull(wsmDto, "wsmDto");

		WebshopMwstsatzbez entity = WebshopMwstsatzbezQuery.findByShopIdMwstsatzbezId(em, wsmDto.getWebshopId(),
				wsmDto.getMwstsatzbezId());
		if (entity != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOPMWSTSATZBEZ.UK"));
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOPMWSTSATZBEZ);

			WebshopMwstsatzbez bean = new WebshopMwstsatzbez(pk, wsmDto.getWebshopId().id(),
					wsmDto.getMwstsatzbezId().id(), wsmDto.getExternalId());
			Timestamp t = getTimestamp();
			bean.setTAnlegen(t);
			bean.setTAendern(t);
			em.persist(bean);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public WebshopKundeDto webshopKundeFindByShopKunde(WebshopId shopId, KundeId kundeId) {
		WebshopKundeDto dto = webshopKundeFindByShopKundeNoExc(shopId, kundeId);
		Validator.entityFound(dto, kundeId.id());
		return dto;
	}

	@Override
	public WebshopKundeDto webshopKundeFindByShopKundeNoExc(WebshopId shopId, KundeId kundeId) {
		Validator.notNull(shopId, "shopId");
		Validator.notNull(kundeId, "kundeId");

		WebshopKunde entity = WebshopKundeQuery.findByShopIdKundeId(em, shopId, kundeId);
		if (entity == null)
			return null;
		WebshopKundeDto wskDto = getMapper().map(entity, WebshopKundeDto.class);
		wskDto.setWebshopId(new WebshopId(entity.getWebshopId()));
		return wskDto;
		// return entity == null ? null
		// : getMapper().map(entity, WebshopKundeDto.class);
	}

	@Override
	public Integer createWebshopKunde(WebshopKundeDto wskDto) {
		Validator.dtoNotNull(wskDto, "wskDto");

		WebshopKunde entity = WebshopKundeQuery.findByShopIdKundeId(em, wskDto.getWebshopId(), wskDto.getKundeId());
		if (entity != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_WEBSHOPKUNDE.UK"));
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WEBSHOPKUNDE);

			WebshopKunde bean = new WebshopKunde(pk, wskDto.getWebshopId().id(), wskDto.getKundeId().id(),
					wskDto.getExternalId());
			Timestamp t = getTimestamp();
			bean.setTAnlegen(t);
			bean.setTAendern(t);
			em.persist(bean);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public void updateWebshopKunde(WebshopKundeDto wskDto) {
		Validator.notNull(wskDto, "wskDto");
		WebshopKunde entity = em.find(WebshopKunde.class, wskDto.getIId());
		Validator.entityFound(entity, wskDto.getIId());
		setWebshopKundeFromWebshopKundeDto(entity, wskDto);
	}

	private void setWebshopKundeFromWebshopKundeDto(WebshopKunde entity, WebshopKundeDto dto) {
		entity.setKundeIId(dto.getKundeIId());
		entity.setExternalId(dto.getExternalId());
		entity.setWebshopId(dto.getWebshopId().id());
		entity.setTAendern(getTimestamp());

		em.merge(entity);
		em.flush();
	}

	@Override
	public WebshopKundeDto webshopKundeFindByShopExternalIdNull(WebshopId shopId, String externalId) {
		Validator.notNull(shopId, "shopId");
		Validator.notEmpty(externalId, "externalId");

		WebshopKunde entity = WebshopKundeQuery.findByShopIdExternalId(em, shopId, externalId);
		if (entity == null)
			return null;

		WebshopKundeDto wskDto = getMapper().map(entity, WebshopKundeDto.class);
		wskDto.setWebshopId(new WebshopId(entity.getWebshopId()));
		return wskDto;
		// return entity == null ? null
		// : getMapper().map(entity, WebshopKundeDto.class);
	}

	@Override
	public List<ArtikelsperrenSperrenDto> artikelsperrenFindByArtikelIIdMitSperren(Integer artikelId)
			throws EJBExceptionLP {
		Validator.pkFieldNotNull(artikelId, "artikelId");

		Query query = em.createNamedQuery("ArtikelsperrenfindByArtikelIId");
		query.setParameter(1, artikelId);
		Collection<Artikelsperren> cl = query.getResultList();
		List<ArtikelsperrenSperrenDto> dtos = assembleArtikelsperrenSperrenDtos(cl);
		for (ArtikelsperrenSperrenDto assDto : dtos) {
			SperrenDto sperrenDto = sperrenFindByPrimaryKey(assDto.getSperrenIId());
			assDto.setSperrenDto(sperrenDto);
		}
		return dtos;
	}

	private List<ArtikelsperrenSperrenDto> assembleArtikelsperrenSperrenDtos(
			Collection<Artikelsperren> artikelsperrens) {
		List<ArtikelsperrenSperrenDto> list = new ArrayList<ArtikelsperrenSperrenDto>();
		if (artikelsperrens != null) {
			for (Artikelsperren artikelsperren : artikelsperrens) {
				list.add(assembleArtikelsperrenSperrenDto(artikelsperren));
			}
		}
		return list;
	}

	private ArtikelsperrenSperrenDto assembleArtikelsperrenSperrenDto(Artikelsperren artikelsperren) {
		return ArtikelsperrenSperrenDtoAssembler.createDto(artikelsperren);
	}

	@EJB
	private WebPartSearchServiceFac webPartSearchService;

	@Override
	public WebabfrageArtikellieferantResult aktualisiereArtikellieferantByWebabfrage(
			WebabfrageArtikellieferantProperties properties, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(properties, "WebabfrageArtikellieferantProperties");
		Validator.notNull(properties.getArtikelId(), "ArtikelId");

		WebPartSearchResult searchResult = webPartSearchService.find(properties.getArtikelId(),
				properties.getLieferantId(), theClientDto);
		WebabfrageArtikellieferantResult webabfrageResult = new WebabfrageArtikellieferantResult(null, searchResult);
		if (properties.isUpdate() && searchResult.getNumberOfResults() == 1) {
			WebabfrageArtikellieferantUpdateProperties updateProperties = new WebabfrageArtikellieferantUpdateProperties(
					properties.getArtikelId(), properties.getLieferantId(),
					webabfrageResult.getWebPartSearchResult().getParts().get(0));
			ArtikellieferantDto updatedArtliefDto = webPartSearchService
					.updateOrCreateArtikellieferantByWebPart(updateProperties, theClientDto);
			webabfrageResult.setArtikellieferantDto(updatedArtliefDto);
			webabfrageResult.setArtikellieferantUpdated(true);
		}

		return webabfrageResult;
	}

	@Override
	public List<Integer> getArtikelIdsArtikellieferantByLieferantIId(Integer lieferantIId) {
		List<Integer> artikelIIds = ArtikellieferantQuery.listDistinctArtikelIIdsByLieferantIId(em, lieferantIId);
		return artikelIIds;
	}

	@Override
	public List<Integer> getEingeschraenkteArtikelgruppen(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("ArtgrurollefindBySystemrolleIId");
		query.setParameter(1, theClientDto.getSystemrolleIId());
		Collection<Artgrurolle> clArten = query.getResultList();

		List<Integer> artgruIds = new ArrayList<Integer>();
		for (Artgrurolle artgrurolle : clArten) {
			Artgru artgru = em.find(Artgru.class, artgrurolle.getArtgruIId());
			if (artgru != null && artgru.getMandantCNr().equals(theClientDto.getMandant())) {
				artgruIds.add(artgrurolle.getArtgruIId());
			}
		}

		return artgruIds;
	}

	@Override
	public List<ArtgruDto> artgruEingeschraenktFindByMandantCNrSpr(TheClientDto theClientDto) {
		Collection<Artgru> artikelgruppen = null;
		List<Integer> artikelgruppenIds = getEingeschraenkteArtikelgruppen(theClientDto);
		if (artikelgruppenIds.isEmpty()) {
			Query query = em.createNamedQuery("ArtgrufindByMandantCNr");
			query.setParameter(1, theClientDto.getMandant());
			artikelgruppen = query.getResultList();
		} else {
			artikelgruppen = new ArrayList<Artgru>();
			for (Integer artgruId : artikelgruppenIds) {
				artikelgruppen.add(em.find(Artgru.class, artgruId));
			}
		}
		List<ArtgruDto> dtos = new ArrayList<ArtgruDto>();
		for (Artgru artgru : artikelgruppen) {
			dtos.add(artgruFindByPrimaryKey(artgru.getIId(), theClientDto));
		}

		return dtos;
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

	private String getArtikelMandantCnr(TheClientDto theClientDto) {
		if (getMandantFac().hatZusatzfunktionZentralerArtikelstamm(theClientDto)) {
			return getSystemFac().getHauptmandant();
		}
		return theClientDto.getMandant();
	}

	private String removeLeadingZero(String value) {
		int len = value.length();
		int index = 0;
		while (index < len && value.charAt(index) == '0') {
			++index;
		}
		return index > 0 ? value.substring(index) : value;
	}

	@Override
	public List<ArtikelDto> artikelFindByReferenzCNrMandantCNr(String value, String requiredPrefix,
			TheClientDto theClientDto) {
		Validator.notEmpty(value, "value");

		value = "%" + removeLeadingZero(value);
		String baseQuery = "SELECT OBJECT(c) FROM Artikel c WHERE " + "c.mandantCNr = :mandantCnr "
				+ "AND REPLACE(c.cReferenznr, '-', '') LIKE :value ";
		if (!Helper.isStringEmpty(requiredPrefix)) {
			requiredPrefix = requiredPrefix + "%";
			baseQuery += "AND c.cNr LIKE :prefix";
		}

		Query query = em.createQuery(baseQuery).setParameter("mandantCnr", getArtikelMandantCnr(theClientDto))
				.setParameter("value", value).setParameter("prefix", requiredPrefix);

		try {
			Collection<?> cl = query.getResultList();
			return assembleArtikelDtosAsList(cl);
		} catch (NoResultException e) {
		} catch (NonUniqueResultException e) {
		}

		return new ArrayList<ArtikelDto>();
	}

	public HvOptional<ArtikelTruTopsDto> artikelTruTopsFindByPrimaryKeyOhneExc(ArtikelTruTopsId artikelTruTopsId) {
		if (artikelTruTopsId == null || artikelTruTopsId.id() == null)
			return HvOptional.empty();

		ArtikelTruTops entity = em.find(ArtikelTruTops.class, artikelTruTopsId.id());
		if (entity == null)
			return HvOptional.empty();

		return HvOptional.of(getMapper().map(entity, ArtikelTruTopsDto.class));
	}

	public ArtikelTruTopsDto artikelTruTopsFindByPrimaryKey(ArtikelTruTopsId artikelTruTopsId) {
		Validator.pkFieldNotNull(artikelTruTopsId, "artikelTruTopsId");

		HvOptional<ArtikelTruTopsDto> dto = artikelTruTopsFindByPrimaryKeyOhneExc(artikelTruTopsId);
		if (dto.isEmpty())
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"artikelTruTopsId=" + artikelTruTopsId.id().toString());

		return dto.get();
	}

	public Integer createArtikelTruTops(ArtikelTruTopsDto dto, TheClientDto theClientDto) {
		Validator.dtoNotNull(dto, "artikelTruTopsDto");
		Validator.notNull(dto.getArtikelIId(), "ArtikelTruTopsDto.getArtikelIId()");

		HvOptional<ArtikelTruTops> duplicate = ArtikelTruTopsQuery.findByArtikelIId(em,
				new ArtikelId(dto.getArtikelIId()));
		if (duplicate.isPresent())
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("UK_WW_ARTIKELTRUTOPS"));

		try {
			Integer pk = new PKGeneratorObj().getNextPrimaryKey(PKConst.PK_ARTIKELTRUTOPS);
			dto.setIId(pk);

			ArtikelTruTops entity = getMapper().map(dto, ArtikelTruTops.class);
			em.persist(entity);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeArtikelTruTops(ArtikelTruTopsId artikelTruTopsId) {
		Validator.pkFieldNotNull(artikelTruTopsId, "artikelTruTopsId");

		ArtikelTruTops entity = em.find(ArtikelTruTops.class, artikelTruTopsId.id());
		Validator.entityFound(entity, artikelTruTopsId.id());

		em.remove(entity);
		em.flush();
	}

	public void updateArtikelTruTops(ArtikelTruTopsDto dto) {
		Validator.dtoNotNull(dto, "artikelTruTopsDto");
		Validator.notNull(dto.getArtikelIId(), "ArtikelTruTopsDto.getArtikelIId()");

		ArtikelTruTops entity = em.find(ArtikelTruTops.class, dto.getIId());
		Validator.entityFound(entity, dto.getIId());

		HvOptional<ArtikelTruTops> duplicate = ArtikelTruTopsQuery.findByArtikelIId(em,
				new ArtikelId(dto.getArtikelIId()));
		if (duplicate.isPresent() && !duplicate.get().getIId().equals(dto.getIId()))
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("UK_WW_ARTIKELTRUTOPS"));

		entity = getMapper().map(dto, ArtikelTruTops.class);
		em.merge(entity);
		em.flush();
	}

	public HvOptional<ArtikelTruTopsMetadatenDto> artikelTruTopsMetadatenFindByPrimaryKeyNoExc(
			ArtikelTruTopsMetadatenId id) {
		if (id == null || id.id() == null)
			return HvOptional.empty();

		ArtikelTruTopsMetadaten entity = em.find(ArtikelTruTopsMetadaten.class, id.id());
		if (entity == null)
			return HvOptional.empty();

		return HvOptional.of(getMapper().map(entity, ArtikelTruTopsMetadatenDto.class));
	}

	public ArtikelTruTopsMetadatenDto artikelTruTopsMetadatenFindByPrimaryKey(ArtikelTruTopsMetadatenId id) {
		Validator.pkFieldNotNull(id, "artikelTruTopsId");

		HvOptional<ArtikelTruTopsMetadatenDto> dto = artikelTruTopsMetadatenFindByPrimaryKeyNoExc(id);
		if (dto.isEmpty())
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"artikelTruTopsId=" + id.id().toString());

		return dto.get();
	}

	public Integer createArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenDto dto, TheClientDto theClientDto) {
		Validator.dtoNotNull(dto, "artikelTruTopsMetadatenDto");
		Validator.notNull(dto.getArtikelTruTopsIId(), "ArtikelTruTopsMetadatenDto.getArtikelTruTopsIId()");

		try {
			Integer pk = new PKGeneratorObj().getNextPrimaryKey(PKConst.PK_ARTIKELTRUTOPSMETADATEN);
			dto.setIId(pk);

			ArtikelTruTopsMetadaten entity = getMapper().map(dto, ArtikelTruTopsMetadaten.class);
			em.persist(entity);
			em.flush();
			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenId id) {
		Validator.pkFieldNotNull(id, "artikelTruTopsMetadatenId");

		ArtikelTruTopsMetadaten entity = em.find(ArtikelTruTopsMetadaten.class, id.id());
		Validator.entityFound(entity, id.id());

		em.remove(entity);
		em.flush();
	}

	public void updateArtikelTruTopsMetadaten(ArtikelTruTopsMetadatenDto dto) {
		Validator.dtoNotNull(dto, "artikelTruTopsMetadatenDto");
		Validator.notNull(dto.getArtikelTruTopsIId(), "ArtikelTruTopsMetadatenDto.getArtikelTruTopsIId()");

		ArtikelTruTopsMetadaten entity = em.find(ArtikelTruTopsMetadaten.class, dto.getIId());
		Validator.entityFound(entity, dto.getIId());

		entity = getMapper().map(dto, ArtikelTruTopsMetadaten.class);
		em.merge(entity);
		em.flush();
	}

	public HvOptional<ArtikelTruTopsDto> artikelTruTopsFindByArtikelId(ArtikelId artikelId) {
		Validator.notNull(artikelId, "artikelId");

		HvOptional<ArtikelTruTops> entity = ArtikelTruTopsQuery.findByArtikelIId(em, artikelId);
		return entity.isPresent() ? HvOptional.of(getMapper().map(entity.get(), ArtikelTruTopsDto.class))
				: HvOptional.empty();
	}

	public List<ArtikelTruTopsMetadatenDto> artikelTruTopsMetadatenFindByArtikelId(ArtikelId artikelId) {
		Validator.notNull(artikelId, "artikelId");

		List<ArtikelTruTopsMetadaten> entities = ArtikelTruTopsMetadatenQuery.findByArtikelIId(em, artikelId);
		List<ArtikelTruTopsMetadatenDto> dtos = new ArrayList<ArtikelTruTopsMetadatenDto>();
		entities.forEach(entity -> dtos.add(getMapper().map(entity, ArtikelTruTopsMetadatenDto.class)));
		return dtos;
	}

	public void resetArtikelTruTops(ArtikelTruTopsId artikelTruTopsId) {
		Validator.notNull(artikelTruTopsId, "artikelTruTopsId");

		ArtikelTruTops entity = em.find(ArtikelTruTops.class, artikelTruTopsId.id());
		Validator.entityFound(entity, artikelTruTopsId.id());

		removeArtikelTruTopsMetadatenByArtikelId(new ArtikelId(entity.getArtikelIId()));

		entity.setTExportBeginn(null);
		entity.setTExportEnde(null);
		entity.setCExportPfad(null);
		entity.setCFehlercode(null);
		entity.setCFehlertext(null);

		em.merge(entity);
		em.flush();
	}

	public void removeArtikelTruTopsMetadatenByArtikelId(ArtikelId artikelId) {
		Validator.pkFieldNotNull(artikelId, "artikelId");

		List<ArtikelTruTopsMetadaten> metadaten = ArtikelTruTopsMetadatenQuery.findByArtikelIId(em, artikelId);
		metadaten.forEach(entity -> em.remove(entity));

		em.flush();
	}

	public void resetArtikelTruTopsByArtikelId(ArtikelId artikelId) {
		Validator.notNull(artikelId, "artikelId");

		HvOptional<ArtikelTruTops> opt = ArtikelTruTopsQuery.findByArtikelIId(em, artikelId);
		if (opt.isPresent()) {
			resetArtikelTruTops(new ArtikelTruTopsId(opt.get().getIId()));
		}
	}
}
