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

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.RepositoryException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionFactoryImplementor;

import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikellager;
import com.lp.server.artikel.ejb.ArtikellagerPK;
import com.lp.server.artikel.ejb.Artikellagerplaetze;
import com.lp.server.artikel.ejb.Geraetesnr;
import com.lp.server.artikel.ejb.Handlagerbewegung;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.artikel.ejb.Lagerabgangursprung;
import com.lp.server.artikel.ejb.LagerabgangursprungPK;
import com.lp.server.artikel.ejb.Lagerart;
import com.lp.server.artikel.ejb.Lagerartspr;
import com.lp.server.artikel.ejb.LagerartsprPK;
import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.ejb.Lagerplatz;
import com.lp.server.artikel.ejb.Lagerumbuchung;
import com.lp.server.artikel.ejb.LagerumbuchungPK;
import com.lp.server.artikel.ejb.Lagerzugangursprung;
import com.lp.server.artikel.ejb.LagerzugangursprungPK;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRHandlagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.fastlanereader.generated.FLRWareneingangsreferez;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelGestehungspreisCalc;
import com.lp.server.artikel.service.ArtikellagerDto;
import com.lp.server.artikel.service.ArtikellagerDtoAssembler;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.GeraetesnrDtoAssembler;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.HandlagerbewegungDtoAssembler;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerDtoAssembler;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerabgangursprungDtoAssembler;
import com.lp.server.artikel.service.LagerartDto;
import com.lp.server.artikel.service.LagerartDtoAssembler;
import com.lp.server.artikel.service.LagerartsprDto;
import com.lp.server.artikel.service.LagerartsprDtoAssembler;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerbewegungDtoAssembler;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerumbuchungDto;
import com.lp.server.artikel.service.LagerumbuchungDtoAssembler;
import com.lp.server.artikel.service.LagerzugangursprungDto;
import com.lp.server.artikel.service.LagerzugangursprungDtoAssembler;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.WarenabgangsreferenzDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.ejb.Lagerrolle;
import com.lp.server.bestellung.ejb.Wareneingangsposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BewegungsvorschauDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.Losablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosablieferung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDtoSmall;
import com.lp.server.personal.service.ArtikellagerplaetzeDtoAssembler;
import com.lp.server.personal.service.LagerplatzDtoAssembler;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.reklamation.fastlanereader.generated.FLRReklamation;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
public class LagerFacBean extends LPReport implements LagerFac, JRDataSource {
	@PersistenceContext
	private EntityManager em;
	private Lagerart lagerart;
	private Lagerartspr lagerartspr;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_SERIENNUMMERN_ARTIKELNUMMER = 0;
	private static int REPORT_SERIENNUMMERN_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_SERIENNUMMERN_SERIENNUMER = 2;
	private static int REPORT_SERIENNUMMERN_MENGE = 3;
	private static int REPORT_SERIENNUMMERN_ZUGANG = 4;
	private static int REPORT_SERIENNUMMERN_ABGANG = 5;
	private static int REPORT_SERIENNUMMERN_ZEITPUNKT = 6;
	private static int REPORT_SERIENNUMMERN_SUBREPORT_GERAETESNR = 7;
	private static int REPORT_SERIENNUMMERN_VERSION = 8;

	private Boolean bLagerLogging = null;;

	public String CHARGENNUMMER_KEINE_CHARGE = "KEINE_CHARGE";

	public BelegInfos getBelegInfos(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, TheClientDto theClientDto) {
		BelegInfos belegInfos = new BelegInfos();
		try {
			// AUSGANGSBELEGE
			if (belegartCNr.equals(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				AgstklDto dto = getAngebotstklFac().agstklFindByPrimaryKey(
						belegartIId);
				belegInfos.setBelegdatum(dto.getTBelegdatum());
				belegInfos.setBelegnummer(dto.getCNr());

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						dto.getKundeIId(), theClientDto);
				belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
						.formatFixName1Name2());
				belegInfos.setPartnerIId(kundeDto.getPartnerIId());
				belegInfos.setBelegbezeichnung(dto.getCBez());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)
					|| belegartCNr.equals(LocaleFac.BELEGART_LSZIELLAGER)) {
				LieferscheinDto dto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(belegartIId, theClientDto);

				if (belegartpositionIId != null) {

					Lieferscheinposition lieferscheinposition = em.find(
							Lieferscheinposition.class, belegartpositionIId);

					if (lieferscheinposition != null
							&& lieferscheinposition.getVerleihIId() != null) {
						VerleihDto verleihDto = getArtikelFac()
								.verleihFindByPrimaryKey(
										lieferscheinposition.getVerleihIId());
						belegInfos.setVerleihfaktor(verleihDto.getFFaktor());
						belegInfos.setVerleihtage(verleihDto.getITage());
					}

					if (lieferscheinposition != null) {
						belegInfos.setBdMaterialzuschlag(lieferscheinposition
								.getNMaterialzuschlag());
					}
				}

				// CK: Belegdatum -> Nicht Liefertermin, da dies in der
				// Artikelstatistik sonst falsch ist.
				belegInfos.setBelegdatum(dto.getTBelegdatum());
				belegInfos.setBelegnummer(dto.getCNr());

				Kunde kunde = em.find(Kunde.class,
						dto.getKundeIIdLieferadresse());

				PartnerDtoSmall pDto = getPartnerFac()
						.partnerFindByPrimaryKeySmall(kunde.getPartnerIId(),
								theClientDto);

				belegInfos.setKundeLieferant(pDto.formatFixName1Name2());
				belegInfos.setPartnerIId(kunde.getPartnerIId());
				belegInfos.setBelegbezeichnung(dto.getCBezProjektbezeichnung());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragDto dto = getAuftragFac().auftragFindByPrimaryKey(
						belegartIId);
				belegInfos.setBelegdatum(dto.getTBelegdatum());
				belegInfos.setBelegnummer(dto.getCNr());

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						dto.getKundeIIdLieferadresse(), theClientDto);
				belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
						.formatFixName1Name2());
				belegInfos.setPartnerIId(kundeDto.getPartnerIId());
				belegInfos.setBelegbezeichnung(dto.getCBezProjektbezeichnung());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {

				com.lp.server.rechnung.service.RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(belegartIId);

				if (belegartpositionIId != null) {
					RechnungPositionDto rechnungPositionDto = getRechnungFac()
							.rechnungPositionFindByPrimaryKeyOhneExc(
									belegartpositionIId);
					if (rechnungPositionDto != null
							&& rechnungPositionDto.getVerleihIId() != null) {
						VerleihDto verleihDto = getArtikelFac()
								.verleihFindByPrimaryKey(
										rechnungPositionDto.getVerleihIId());
						belegInfos.setVerleihfaktor(verleihDto.getFFaktor());
						belegInfos.setVerleihtage(verleihDto.getITage());
					}
					if (rechnungPositionDto != null) {
						belegInfos.setBdMaterialzuschlag(rechnungPositionDto
								.getNMaterialzuschlag());
					}
				}

				belegInfos.setBelegdatum(new Timestamp(rechnungDto
						.getTBelegdatum().getTime()));
				belegInfos.setBelegnummer(rechnungDto.getCNr());

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnungDto.getKundeIId(), theClientDto);
				belegInfos.setPartnerIId(kundeDto.getPartnerIId());
				belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
						.formatFixName1Name2());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {

				com.lp.server.angebot.service.AngebotDto angebotDto = getAngebotFac()
						.angebotFindByPrimaryKey(belegartIId, theClientDto);
				belegInfos.setBelegdatum(new Timestamp(angebotDto
						.getTBelegdatum().getTime()));
				belegInfos.setBelegnummer(angebotDto.getCNr());

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
				belegInfos.setPartnerIId(kundeDto.getPartnerIId());
				belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
						.formatFixName1Name2());
			}
			// EINGANGSBELEGE
			else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
				BestellungDto dto = getBestellungFac()
						.bestellungFindByPrimaryKey(belegartIId);
				belegInfos.setBelegnummer(dto.getCNr());
				if (belegartpositionIId != null) {
					WareneingangspositionDto wareneingangspositionDto = getWareneingangFac()
							.wareneingangspositionFindByPrimaryKey(
									belegartpositionIId);
					WareneingangDto wareneingangDto = getWareneingangFac()
							.wareneingangFindByPrimaryKey(
									wareneingangspositionDto
											.getWareneingangIId());
					belegInfos.setBelegdatum(wareneingangDto
							.getTWareneingangsdatum());

					BestellpositionDto bestPos = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									wareneingangspositionDto
											.getBestellpositionIId());
					belegInfos.setBdMaterialzuschlag(bestPos
							.getNMaterialzuschlag());
				} else {
					belegInfos.setBelegdatum(new Timestamp(dto.getDBelegdatum()
							.getTime()));
				}

				if (dto.getLieferartIId() != null) {
					LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									dto.getLieferantIIdBestelladresse(),
									theClientDto);
					belegInfos.setKundeLieferant(lieferantDto.getPartnerDto()
							.formatFixName1Name2());
					belegInfos.setPartnerIId(lieferantDto.getPartnerIId());
				}
				belegInfos.setBelegbezeichnung(dto.getCBez());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(
						belegartIId);

				if (belegartpositionIId != null) {
					Losablieferung losablieferung = em.find(
							Losablieferung.class, belegartpositionIId);
					if (losablieferung != null) {
						belegInfos.setBelegdatum(losablieferung.getTAendern());
					}
				} else {
					belegInfos.setBelegdatum(losDto.getTAusgabe());
				}
				belegInfos.setBelegnummer(losDto.getCNr());
				belegInfos.setBelegbezeichnung(losDto.getCProjekt());

				// Kunde nur, wenn auftragbezogen
				if (losDto.getAuftragIId() != null) {
					// losDto.getAuftragIId()
					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByPrimaryKeyOhneExc(
									losDto.getAuftragIId());
					if (auftragDto != null) {
						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										auftragDto.getKundeIIdAuftragsadresse(),
										theClientDto);
						belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
								.formatFixName1Name2());
						belegInfos.setPartnerIId(kundeDto.getPartnerIId());
					}
				}

			} else if (belegartCNr.equals(LocaleFac.BELEGART_INVENTUR)) {

				InventurDto inventurDto = getInventurFac()
						.inventurFindByPrimaryKey(belegartIId, theClientDto);
				belegInfos.setBelegnummer(inventurDto.getCBez()
						+ "("
						+ Helper.formatDatum(inventurDto.getTInventurdatum(),
								theClientDto.getLocUi()) + ")");
			} else if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
				com.lp.server.rechnung.service.RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(belegartIId);
				belegInfos.setBelegdatum(new Timestamp(rechnungDto
						.getTBelegdatum().getTime()));
				belegInfos.setBelegnummer(rechnungDto.getCNr());
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						rechnungDto.getKundeIId(), theClientDto);
				belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
						.formatFixName1Name2());
				belegInfos.setPartnerIId(kundeDto.getPartnerIId());

				if (belegartpositionIId != null) {
					RechnungPositionDto rechnungPositionDto = getRechnungFac()
							.rechnungPositionFindByPrimaryKeyOhneExc(
									belegartpositionIId);
					if (rechnungPositionDto != null) {
						belegInfos.setBdMaterialzuschlag(rechnungPositionDto
								.getNMaterialzuschlag());
					}
				}

			} else if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {
				HandlagerbewegungDto dto = null;
				dto = getLagerFac().handlagerbewegungFindByPrimaryKey(
						belegartIId, theClientDto);
				belegInfos.setBelegdatum(dto.getTBuchungszeit());
				belegInfos.setBelegnummer(dto.getIId() + "");
				belegInfos.setKundeLieferant(dto.getCKommentar());
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(
						belegartIId);
				belegInfos.setBelegnummer(losDto.getCNr());
				belegInfos.setBelegbezeichnung(losDto.getCProjekt());

				if (losDto.getKundeIId() != null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							losDto.getKundeIId(), theClientDto);
					belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
							.formatFixName1Name2());
					belegInfos.setPartnerIId(kundeDto.getPartnerIId());
				} else {
					if (losDto.getAuftragIId() != null) {

						KundeDto kundeDto = getKundeFac()
								.kundeFindByPrimaryKey(
										getAuftragFac()
												.auftragFindByPrimaryKey(
														losDto.getAuftragIId())
												.getKundeIIdAuftragsadresse(),
										theClientDto);
						belegInfos.setKundeLieferant(kundeDto.getPartnerDto()
								.formatFixName1Name2());
						belegInfos.setPartnerIId(kundeDto.getPartnerIId());
					}
				}

			}
			// sonstige
			else if (belegartCNr.equals(LocaleFac.BELEGART_PROJEKT)) {
				ProjektDto losDto = null;
				losDto = getProjektFac().projektFindByPrimaryKey(belegartIId);
				belegInfos.setBelegnummer(losDto.getCNr());
				belegInfos.setBelegbezeichnung(losDto.getCTitel());
				belegInfos.setPartnerIId(losDto.getPartnerIId());
				if (losDto.getPartnerIId() != null) {
					belegInfos.setKundeLieferant(getPartnerFac()
							.partnerFindByPrimaryKey(losDto.getPartnerIId(),
									theClientDto).formatFixName1Name2());
				}
			} else {
				belegInfos.setBelegnummer(belegartCNr);
				belegInfos.setKundeLieferant("not implemented");
			}

		} catch (EJBExceptionLP ex) {
			belegInfos.setKundeLieferant("not found");
			return belegInfos;

		} catch (EJBException ex) {
			belegInfos.setBelegnummer(belegartCNr);
			belegInfos.setKundeLieferant("not found");
			return belegInfos;
		} catch (RemoteException ex) {
			belegInfos.setBelegnummer(belegartCNr);
			belegInfos.setKundeLieferant("not found");
			return belegInfos;
		}

		return belegInfos;
	}

	/**
	 * @deprecated
	 */
	public LPDatenSubreport getWareneingangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId, String cSeriennrChargennr,
			boolean bMitJcrDocs, TheClientDto theClientDto) {

		ArrayList<WarenzugangsreferenzDto> al = getWareneingangsreferenz(
				sBelegart, belegartpositionIId, cSeriennrChargennr,
				bMitJcrDocs, theClientDto);

		String[] fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER",
				"F_ZUSATZ", "F_BELEGDATUM", "F_POSITION1", "F_POSITION2",
				"F_URSPRUNGSLAND", "F_HERSTELLER", "F_MENGE" };

		Object[][] dataSub = new Object[al.size()][fieldnames.length];

		for (int i = 0; i < al.size(); i++) {
			WarenzugangsreferenzDto dto = (WarenzugangsreferenzDto) al.get(i);
			dataSub[i][0] = dto.getBelegart();
			dataSub[i][1] = dto.getBelegnummer();
			dataSub[i][2] = dto.getZusatz();
			dataSub[i][3] = dto.getTBelegdatum();
			dataSub[i][4] = dto.getPosition1();
			dataSub[i][5] = dto.getPosition2();
			dataSub[i][6] = dto.getLand();
			dataSub[i][7] = dto.getHersteller();
			dataSub[i][8] = dto.getMenge();
		}

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	public LPDatenSubreport getWareneingangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, boolean bMitJcrDocs,
			TheClientDto theClientDto) {

		ArrayList<WarenzugangsreferenzDto> al = getWareneingangsreferenz(
				sBelegart, belegartpositionIId, snrs, bMitJcrDocs, theClientDto);

		String[] fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER",
				"F_ZUSATZ", "F_BELEGDATUM", "F_POSITION1", "F_POSITION2",
				"F_URSPRUNGSLAND", "F_HERSTELLER", "F_MENGE", "F_LOS_AZANTEIL",
				"F_LOS_MATERIALANTEIL", "F_I_ID_BUCHUNG", "F_PERSON_BUCHENDER",
				"F_KURZZEICHEN_BUCHENDER" };

		Object[][] dataSub = new Object[al.size()][fieldnames.length];

		for (int i = 0; i < al.size(); i++) {
			WarenzugangsreferenzDto dto = (WarenzugangsreferenzDto) al.get(i);
			dataSub[i][0] = dto.getBelegart();
			dataSub[i][1] = dto.getBelegnummer();
			dataSub[i][2] = dto.getZusatz();
			dataSub[i][3] = dto.getTBelegdatum();
			dataSub[i][4] = dto.getPosition1();
			dataSub[i][5] = dto.getPosition2();
			dataSub[i][6] = dto.getLand();
			dataSub[i][7] = dto.getHersteller();
			dataSub[i][8] = dto.getMenge();
			dataSub[i][9] = dto.getLosAZAnteil();
			dataSub[i][10] = dto.getLosMaterialanteil();
			dataSub[i][11] = dto.getI_id_buchung();
			dataSub[i][12] = dto.getPerson_buchender();
			dataSub[i][13] = dto.getKurzzeichen_buchender();

		}

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	public LPDatenSubreport getWarenausgangsreferenzSubreport(String sBelegart,
			Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto) {

		ArrayList<WarenabgangsreferenzDto> al = getWarenausgangsreferenz(
				sBelegart, belegartpositionIId, snrs, theClientDto);

		String[] fieldnames = new String[] { "F_BELEGART", "F_BELEGNUMMER",
				"F_ZUSATZ", "F_BELEGDATUM", "F_MENGE" };

		Object[][] dataSub = new Object[al.size()][fieldnames.length];

		for (int i = 0; i < al.size(); i++) {
			WarenabgangsreferenzDto dto = (WarenabgangsreferenzDto) al.get(i);
			dataSub[i][0] = dto.getBelegart();
			dataSub[i][1] = dto.getBelegnummer();
			dataSub[i][2] = dto.getZusatz();
			dataSub[i][3] = dto.getTBelegdatum();
			dataSub[i][4] = dto.getMenge();

		}

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	public LPDatenSubreport getSubreportEnthaltenesLosIstMaterial(
			String artikelnummer, String chargennummer,
			TheClientDto theClientDto) {
		String[] fieldnames = new String[] { "Losnummer", "Artikelnummer",
				"Bezeichnung", "Menge", "Snrchnr" };

		ArrayList alDaten = new ArrayList();

		ArtikelDto aDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(
				artikelnummer, theClientDto.getMandant());

		if (aDto != null) {

			Query query = em
					.createNamedQuery("LagerbewegungfindAllSnrChnrEinesArtikels");
			query.setParameter(1, aDto.getIId());
			query.setParameter(2, chargennummer);
			Collection<?> cl = query.getResultList();

			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) it.next();

				if (Helper.short2boolean(lagerbewegung.getBAbgang())) {
					LagerabgangursprungDto[] dtos = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagerbewegung
							.getIIdBuchung());

					for (int i = 0; i < dtos.length; i++) {

						if (dtos[i].getNVerbrauchtemenge().doubleValue() > 0) {

							LagerbewegungDto[] urspDtos = lagerbewegungFindByIIdBuchung(dtos[i]
									.getILagerbewegungidursprung());

							if (urspDtos.length > 0) {

								if (urspDtos[0].getCBelegartnr().equals(
										LocaleFac.BELEGART_LOSABLIEFERUNG)) {

									LosablieferungDto laDto = getFertigungFac()
											.losablieferungFindByPrimaryKeyOhneExc(
													urspDtos[0]
															.getIBelegartpositionid());

									if (laDto != null) {

										try {

											LosDto loDto = getFertigungFac()
													.losFindByPrimaryKey(
															laDto.getLosIId());

											LossollmaterialDto[] sollmatDtos = getFertigungFac()
													.lossollmaterialFindByLosIIdOrderByISort(
															laDto.getLosIId());

											for (int u = 0; u < sollmatDtos.length; u++) {
												LossollmaterialDto sollmatDto = sollmatDtos[u];

												ArtikelDto aDto_Sollmat = getArtikelFac()
														.artikelFindByPrimaryKey(
																sollmatDto
																		.getArtikelIId(),
																theClientDto);

												LosistmaterialDto[] istmatDtos = getFertigungFac()
														.losistmaterialFindByLossollmaterialIId(
																sollmatDto
																		.getIId());
												for (int x = 0; x < istmatDtos.length; x++) {
													LosistmaterialDto istmatDto = istmatDtos[x];

													List<SeriennrChargennrMitMengeDto> snrDtos = getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
															LocaleFac.BELEGART_LOS,
															istmatDto.getIId());

													Object[] oZeile = new Object[5];

													oZeile[0] = loDto.getCNr();
													oZeile[1] = aDto_Sollmat
															.getCNr();

													if (aDto_Sollmat
															.getArtikelsprDto() != null) {
														oZeile[2] = aDto_Sollmat
																.getArtikelsprDto()
																.getCBez();
													}

													oZeile[3] = istmatDto
															.getNMenge();

													if (snrDtos.size() == 0) {
														alDaten.add(oZeile);
													} else {

														for (int z = 0; z < snrDtos
																.size(); z++) {
															Object[] oZeileKopie = oZeile
																	.clone();
															oZeileKopie[4] = snrDtos
																	.get(z)
																	.getCSeriennrChargennr();
															oZeileKopie[3] = snrDtos
																	.get(z)
																	.getNMenge();

															alDaten.add(oZeileKopie);
														}

													}

												}

											}

										} catch (RemoteException e) {
											throwEJBExceptionLPRespectOld(e);
										}

									}

								}

							}

						}

					}
				}
			}
		}

		Object[][] dataSubKD = new Object[alDaten.size()][5];
		dataSubKD = (Object[][]) alDaten.toArray(dataSubKD);
		return new LPDatenSubreport(dataSubKD, fieldnames);
	}

	/**
	 * @deprecated
	 */

	public ArrayList<WarenzugangsreferenzDto> getWareneingangsreferenz(
			String sBelegart, Integer belegartpositionIId,
			String cSeriennrChargennr, boolean bMitJcrDocs,
			TheClientDto theClientDto) {
		// PJ14939

		ArrayList<WarenzugangsreferenzDto> al = new ArrayList<WarenzugangsreferenzDto>();

		String[] s = new String[] { null };
		if (cSeriennrChargennr != null) {
			s = Helper.erzeugeStringArrayAusString(cSeriennrChargennr);
		}

		for (int a = 0; a < s.length; a++) {
			LagerbewegungDto lagebewDtos = getLagerFac().getLetzteintrag(
					sBelegart, belegartpositionIId, s[a]);

			if (lagebewDtos != null) {
				LagerabgangursprungDto[] dtos = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagebewDtos
						.getIIdBuchung());

				if (Helper.short2boolean(lagebewDtos.getBAbgang()) == false) {
					dtos = new LagerabgangursprungDto[1];
				}

				for (int i = 0; i < dtos.length; i++) {

					LagerbewegungDto referenzDto = null;
					if (Helper.short2boolean(lagebewDtos.getBAbgang()) == true) {
						LagerbewegungDto[] urspDtos = lagerbewegungFindByIIdBuchung(dtos[i]
								.getILagerbewegungidursprung());

						if (urspDtos != null && urspDtos.length > 0) {
							referenzDto = urspDtos[0];
						}

					} else {
						referenzDto = lagebewDtos;
					}
					if (referenzDto != null) {

						try {
							WarenzugangsreferenzDto wzu = new WarenzugangsreferenzDto();
							if (Helper.short2boolean(lagebewDtos.getBAbgang()) == true) {
								wzu.setMenge(dtos[i].getNVerbrauchtemenge());
							} else {
								wzu.setMenge(referenzDto.getNMenge());
							}

							wzu.setnEinstandspreis(referenzDto
									.getNEinstandspreis());
							wzu.setBelegartIId(referenzDto.getIBelegartid());
							wzu.setBelegartpositionIId(referenzDto
									.getIBelegartpositionid());
							// Hersteller
							if (referenzDto.getHerstellerIId() != null) {
								HerstellerDto hst = getArtikelFac()
										.herstellerFindByPrimaryKey(
												referenzDto.getHerstellerIId(),
												theClientDto);
								wzu.setHersteller(hst.getCNr());
							}

							// Land
							if (referenzDto.getLandIId() != null) {
								LandDto landDto = getSystemFac()
										.landFindByPrimaryKey(
												referenzDto.getLandIId());
								wzu.setLand(landDto.getCLkz());
							}

							if (referenzDto.getCBelegartnr().equals(
									LocaleFac.BELEGART_BESTELLUNG)) {

								Wareneingangsposition wareneingangsposition = em
										.find(Wareneingangsposition.class,
												referenzDto
														.getIBelegartpositionid());

								if (wareneingangsposition != null) {

									WareneingangDto weDto = getWareneingangFac()
											.wareneingangFindByPrimaryKey(
													wareneingangsposition
															.getWareneingangIId());

									BestellungDto best = getBestellungFac()
											.bestellungFindByPrimaryKey(
													weDto.getBestellungIId());

									BestellpositionDto bestellpositionDto = getBestellpositionFac()
											.bestellpositionFindByPrimaryKey(
													wareneingangsposition
															.getBestellpositionIId());

									wzu.setPosition1(getBestellpositionFac()
											.getPositionNummer(
													bestellpositionDto.getIId(),
													theClientDto));

									wzu.setBelegart(LocaleFac.BELEGART_BESTELLUNG
											.trim());
									wzu.setKostenstelleIId(best
											.getKostenstelleIId());
									wzu.setBelegnummer(best.getCNr());
									wzu.setTBelegdatum(weDto
											.getTWareneingangsdatum());
									wzu.setZusatz(weDto.getCLieferscheinnr());

									wzu.setPosition2(weDto.getISort());

									ArtikelDto artikelDtoSmall = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													referenzDto.getArtikelIId(),
													theClientDto);

									// JCR-Referenz mitgeben
									if (Helper.short2boolean(artikelDtoSmall
											.getBDokumentenpflicht())) {

										PrintInfoDto piDto = getJCRDocFac()
												.getPathAndPartnerAndTable(
														wareneingangsposition
																.getIId(),
														QueryParameters.UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN,
														theClientDto);

										if (piDto != null
												&& piDto.getDocPath() != null) {
											wzu.setDocPath(piDto.getDocPath());

											if (bMitJcrDocs)

												try {
													ArrayList objects = getJCRDocFac()
															.getJCRDocDtoFromNodeChildren(
																	piDto.getDocPath());

													ArrayList<JCRDocDto> alTemp = new ArrayList<JCRDocDto>();
													if (objects != null) {
														for (int u = 0; u < objects
																.size(); u++) {
															if (objects.get(u) instanceof JCRDocDto) {

																JCRDocDto dto = (JCRDocDto) objects
																		.get(u);
																dto = getJCRDocFac()
																		.getData(
																				dto);
																alTemp.add(dto);
															}
														}
													}
													wzu.setJcrdocs(alTemp);

												} catch (RepositoryException e) {
													e.printStackTrace();
												} catch (IOException e) {
													e.printStackTrace();
												}
										}

									}

								}

							}

							if (referenzDto.getCBelegartnr().equals(
									LocaleFac.BELEGART_LIEFERSCHEIN)) {
								Lieferschein ls = em.find(Lieferschein.class,
										referenzDto.getIBelegartid());
								if (ls != null) {
									wzu.setKostenstelleIId(ls
											.getKostenstelleIId());
								}
							}
							if (referenzDto.getCBelegartnr().equals(
									LocaleFac.BELEGART_RECHNUNG)) {
								Rechnung re = em.find(Rechnung.class,
										referenzDto.getIBelegartid());
								if (re != null) {
									wzu.setKostenstelleIId(re
											.getKostenstelleIId());
								}
							}

							if (referenzDto.getCBelegartnr().equals(
									LocaleFac.BELEGART_LOSABLIEFERUNG)) {

								wzu.setBelegart(LocaleFac.BELEGART_LOSABLIEFERUNG);
								wzu.setTBelegdatum(referenzDto
										.getTBuchungszeit());
								Los los = em.find(Los.class,
										referenzDto.getIBelegartid());
								if (los != null) {
									wzu.setBelegnummer(los.getCNr().trim());
									wzu.setKostenstelleIId(los
											.getKostenstelleIId());
								}
							}

							else {
								wzu.setBelegart(referenzDto.getCBelegartnr()
										.trim());
								wzu.setBelegnummer(referenzDto.getCBelegartnr()
										.trim());

							}

							al.add(wzu);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
					}
				}
			}
		}
		return al;
	}

	public ArrayList<WarenabgangsreferenzDto> getWarenausgangsreferenz(
			String sBelegart, Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto) {
		// PJ14939

		ArrayList<WarenabgangsreferenzDto> al = new ArrayList<WarenabgangsreferenzDto>();

		String[] s = new String[] { null };
		if (snrs != null && snrs.size() > 0) {
			s = SeriennrChargennrMitMengeDto
					.erstelleStringArrayAusMehrerenSeriennummern(snrs);
		}

		for (int a = 0; a < s.length; a++) {
			LagerbewegungDto lagebewDtos = getLagerFac().getLetzteintrag(
					sBelegart, belegartpositionIId, s[a]);

			if (lagebewDtos != null) {
				LagerabgangursprungDto[] dtos = lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(lagebewDtos
						.getIIdBuchung());

				for (int i = 0; i < dtos.length; i++) {

					LagerbewegungDto referenzDto = null;

					LagerbewegungDto[] urspDtos = lagerbewegungFindByIIdBuchung(dtos[i]
							.getILagerbewegungid());

					if (urspDtos != null && urspDtos.length > 0) {
						referenzDto = urspDtos[0];
					}

					if (referenzDto != null) {

						WarenabgangsreferenzDto wzu = new WarenabgangsreferenzDto();
						wzu.setBelegart(referenzDto.getCBelegartnr().trim());
						wzu.setBelegnummer(referenzDto.getCBelegartnr().trim());
						wzu.setTBelegdatum(referenzDto.getTBelegdatum());
						wzu.setMenge(dtos[i].getNVerbrauchtemenge());
						wzu.setnVKpreis(referenzDto.getNVerkaufspreis());

						if (referenzDto.getCBelegartnr().equals(
								LocaleFac.BELEGART_LOS)) {

							Los los = em.find(Los.class,
									referenzDto.getIBelegartid());
							if (los != null) {
								wzu.setBelegnummer(los.getCNr().trim());

							}

						}

						else if (referenzDto.getCBelegartnr().equals(
								LocaleFac.BELEGART_LIEFERSCHEIN)) {
							Lieferschein ls = em.find(Lieferschein.class,
									referenzDto.getIBelegartid());
							if (ls != null) {
								wzu.setBelegnummer(ls.getCNr().trim());

							}

						} else if (referenzDto.getCBelegartnr().equals(
								LocaleFac.BELEGART_RECHNUNG)) {
							Rechnung re = em.find(Rechnung.class,
									referenzDto.getIBelegartid());
							if (re != null) {
								wzu.setBelegnummer(re.getCNr().trim());

							}
						}

						al.add(wzu);

					}
				}
			}
		}
		return al;
	}

	public ArrayList<WarenzugangsreferenzDto> getWareneingangsreferenz(
			String sBelegart, Integer belegartpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, boolean bMitJcrDocs,
			TheClientDto theClientDto) {
		// PJ14939
		ArrayList<WarenzugangsreferenzDto> al = new ArrayList<WarenzugangsreferenzDto>();
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT wr FROM FLRWareneingangsreferez wr WHERE wr.abbuchung.c_belegartnr='"
				+ sBelegart
				+ "' AND wr.abbuchung.i_belegartpositionid="
				+ belegartpositionIId;

		if (snrs == null || snrs.size() == 0 || snrs.size() == 1
				&& snrs.get(0).getCSeriennrChargennr() == null) {
			sQuery += " AND wr.abbuchung.c_seriennrchargennr IS NULL";
		} else {
			sQuery += " AND wr.abbuchung.c_seriennrchargennr IN (";

			String[] s = SeriennrChargennrMitMengeDto
					.erstelleStringArrayAusMehrerenSeriennummern(snrs);

			for (int i = 0; i < s.length; i++) {

				sQuery += "'" + s[i] + "'";

				if (i != s.length - 1) {
					sQuery += ",";
				}
			}

			sQuery += ")";

		}

		sQuery += " ORDER BY wr.abbuchung.c_seriennrchargennr ASC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRWareneingangsreferez wr = (FLRWareneingangsreferez) resultListIterator
					.next();

			FLRLagerbewegung zugang = wr.getZubuchung();
			FLRLagerbewegung abgang = wr.getAbbuchung();

			// SP3369
			if (zugang.getC_belegartnr().equals(LocaleFac.BELEGART_HAND)) {
				HandlagerbewegungDto umbuchung = getZugehoerigeUmbuchung(
						zugang.getI_belegartid(), theClientDto);

				if (umbuchung != null
						&& Helper.short2boolean(umbuchung.getBAbgang())) {
					ArrayList<WarenzugangsreferenzDto> alOri = getWareneingangsreferenz(
							LocaleFac.BELEGART_HAND, umbuchung.getIId(), snrs,
							bMitJcrDocs, theClientDto);

					for (int i = 0; i < alOri.size(); i++) {

						if (alOri.get(i).getMenge().doubleValue() > 0) {
							al.add(alOri.get(i));
						}
					}

					continue;

				}
			}

			try {
				WarenzugangsreferenzDto wzu = new WarenzugangsreferenzDto();

				// PJ18617

				wzu.setPerson_buchender(HelperServer
						.formatPersonAusFLRPartner(zugang.getFlrpersonal()
								.getFlrpartner()));
				wzu.setKurzzeichen_buchender(zugang.getFlrpersonal()
						.getC_kurzzeichen());
				wzu.setBelegartIId(zugang.getI_belegartid());
				wzu.setBelegartpositionIId(zugang.getI_belegartpositionid());
				wzu.setMenge(wr.getN_verbrauchtemenge());

				wzu.setI_id_buchung(zugang.getI_id_buchung());

				wzu.setnEinstandspreis(zugang.getN_einstandspreis());
				// Hersteller
				if (zugang.getFlrhersteller() != null) {

					wzu.setHersteller(zugang.getFlrhersteller().getC_nr());
				}

				// Land
				if (zugang.getFlrland() != null) {

					wzu.setLand(zugang.getFlrland().getC_lkz());
				}

				if (zugang.getC_belegartnr().equals(
						LocaleFac.BELEGART_BESTELLUNG)) {

					Wareneingangsposition wareneingangsposition = em.find(
							Wareneingangsposition.class,
							zugang.getI_belegartpositionid());

					if (wareneingangsposition != null) {

						WareneingangDto weDto = getWareneingangFac()
								.wareneingangFindByPrimaryKey(
										wareneingangsposition
												.getWareneingangIId());

						BestellungDto best = getBestellungFac()
								.bestellungFindByPrimaryKey(
										weDto.getBestellungIId());

						BestellpositionDto bestellpositionDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(
										wareneingangsposition
												.getBestellpositionIId());

						wzu.setPosition1(getBestellpositionFac()
								.getPositionNummer(bestellpositionDto.getIId(),
										theClientDto));

						wzu.setBelegart(LocaleFac.BELEGART_BESTELLUNG.trim());
						wzu.setKostenstelleIId(best.getKostenstelleIId());
						wzu.setBelegnummer(best.getCNr());
						wzu.setTBelegdatum(weDto.getTWareneingangsdatum());
						wzu.setZusatz(weDto.getCLieferscheinnr());

						wzu.setPosition2(weDto.getISort());

						// JCR-Referenz mitgeben
						if (Helper.short2boolean(zugang.getFlrartikel()
								.getB_dokumentenpflicht())) {

							PrintInfoDto piDto = getJCRDocFac()
									.getPathAndPartnerAndTable(
											wareneingangsposition.getIId(),
											QueryParameters.UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN,
											theClientDto);

							if (piDto != null && piDto.getDocPath() != null) {
								wzu.setDocPath(piDto.getDocPath());

								if (bMitJcrDocs)

									try {
										ArrayList objects = getJCRDocFac()
												.getJCRDocDtoFromNodeChildren(
														piDto.getDocPath());

										ArrayList<JCRDocDto> alTemp = new ArrayList<JCRDocDto>();
										if (objects != null) {
											for (int u = 0; u < objects.size(); u++) {
												if (objects.get(u) instanceof JCRDocDto) {

													JCRDocDto dto = (JCRDocDto) objects
															.get(u);
													dto = getJCRDocFac()
															.getData(dto);
													alTemp.add(dto);
												}
											}
										}
										wzu.setJcrdocs(alTemp);

									} catch (RepositoryException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
							}

						}

					}

				}

				else if (zugang.getC_belegartnr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)) {
					Lieferschein ls = em.find(Lieferschein.class,
							zugang.getI_belegartid());
					if (ls != null) {
						wzu.setKostenstelleIId(ls.getKostenstelleIId());
						wzu.setBelegart(zugang.getC_belegartnr().trim());
						wzu.setBelegnummer(ls.getCNr());
						wzu.setTBelegdatum(ls.getTBelegdatum());
					}
				} else if (zugang.getC_belegartnr().equals(
						LocaleFac.BELEGART_LSZIELLAGER)) {
					Lieferschein ls = em.find(Lieferschein.class,
							zugang.getI_belegartid());
					if (ls != null) {
						wzu.setKostenstelleIId(ls.getKostenstelleIId());

						wzu.setBelegart(zugang.getC_belegartnr().trim());
						wzu.setBelegnummer(ls.getCNr());
						wzu.setTBelegdatum(ls.getTBelegdatum());

					}

				} else if (zugang.getC_belegartnr().equals(
						LocaleFac.BELEGART_RECHNUNG)) {
					Rechnung re = em.find(Rechnung.class,
							zugang.getI_belegartid());
					if (re != null) {
						wzu.setKostenstelleIId(re.getKostenstelleIId());
					}
				}

				else if (zugang.getC_belegartnr().equals(
						LocaleFac.BELEGART_LOSABLIEFERUNG)) {

					wzu.setBelegart(LocaleFac.BELEGART_LOSABLIEFERUNG);
					wzu.setTBelegdatum(new Timestamp(zugang.getT_buchungszeit()
							.getTime()));
					Los los = em.find(Los.class, zugang.getI_belegartid());
					if (los != null) {
						wzu.setBelegnummer(los.getCNr().trim());
						wzu.setKostenstelleIId(los.getKostenstelleIId());

						ArtikelDto artikelDtoSmall = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										zugang.getFlrartikel().getI_id(),
										theClientDto);

						// JCR-Referenz mitgeben
						if (Helper.short2boolean(artikelDtoSmall
								.getBDokumentenpflicht())) {

							PrintInfoDto piDto = getJCRDocFac()
									.getPathAndPartnerAndTable(
											zugang.getI_belegartpositionid(),
											QueryParameters.UC_ID_LOSABLIEFERUNG,
											theClientDto);

							if (piDto != null && piDto.getDocPath() != null) {
								wzu.setDocPath(piDto.getDocPath());

								if (bMitJcrDocs)

									try {
										ArrayList objects = getJCRDocFac()
												.getJCRDocDtoFromNodeChildren(
														piDto.getDocPath());

										ArrayList<JCRDocDto> alTemp = new ArrayList<JCRDocDto>();
										if (objects != null) {
											for (int u = 0; u < objects.size(); u++) {
												if (objects.get(u) instanceof JCRDocDto) {

													JCRDocDto dto = (JCRDocDto) objects
															.get(u);
													dto = getJCRDocFac()
															.getData(dto);
													alTemp.add(dto);
												}
											}
										}
										wzu.setJcrdocs(alTemp);

									} catch (RepositoryException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
							}

						}

					}

					Losablieferung la = em.find(Losablieferung.class,
							zugang.getI_belegartpositionid());
					if (la != null) {
						wzu.setLosAZAnteil(la.getNArbeitszeitwert());
						wzu.setLosMaterialanteil(la.getNMaterialwert());
					}

				}

				else {
					wzu.setBelegart(zugang.getC_belegartnr().trim());
					wzu.setBelegnummer(zugang.getC_belegartnr().trim());
					wzu.setTBelegdatum(new Timestamp(zugang.getT_belegdatum()
							.getTime()));

				}

				al.add(wzu);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return al;
	}

	/**
	 * Legt ein neues Lager bei einem Mandanten mit einer bestimmten Lagerart
	 * an.
	 * 
	 * @param lagerDto
	 *            lagerDto
	 * @throws EJBExceptionLP
	 *             lagerDto == null oder lagerDto.getCNr() == null ||
	 *             lagerDto.getLagerartCNr() == null || lagerDto.getMandantCNr()
	 *             == null oder
	 * @return id des angelegten Lagers
	 */
	public Integer createLager(LagerDto lagerDto) throws EJBExceptionLP {
		myLogger.entry();
		if (lagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerDto == null"));
		}
		if (lagerDto.getCNr() == null || lagerDto.getLagerartCNr() == null
				|| lagerDto.getMandantCNr() == null
				|| lagerDto.getBBestellvorschlag() == null
				|| lagerDto.getBInternebestellung() == null
				|| lagerDto.getBVersteckt() == null
				|| lagerDto.getBKonsignationslager() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerDto.getCNr() == null || lagerDto.getLagerartCNr() == null || lagerDto.getMandantCNr() == null || lagerDto.getBBestellvorschlag() == null || lagerDto.getBInternebestellung() == null || lagerDto.getBVersteckt() == null || lagerDto.getBKonsignationslager() == null"));
		}
		Lager doppelt = null;
		try {
			Query query = em.createNamedQuery("LagerfindByCNrByMandantCNr");
			query.setParameter(1, lagerDto.getCNr());
			query.setParameter(2, lagerDto.getMandantCNr());
			doppelt = (Lager) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_LAGER.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		}
		try {
			if (lagerDto.getLagerartCNr().equals(LagerFac.LAGERART_HAUPTLAGER)) {
				Query query1 = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query1.setParameter(1, lagerDto.getMandantCNr());
				query1.setParameter(2, LagerFac.LAGERART_HAUPTLAGER);
				doppelt = (Lager) query1.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
						new Exception(
								"FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));

			}
		} catch (NoResultException ex) {
			// nothing here
		}
		try {
			if (lagerDto.getLagerartCNr().equals(
					LagerFac.LAGERART_WERTGUTSCHRIFT)) {
				Query query1 = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query1.setParameter(1, lagerDto.getMandantCNr());
				query1.setParameter(2, LagerFac.LAGERART_WERTGUTSCHRIFT);
				doppelt = (Lager) query1.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
						new Exception(
								"FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));

			}
		} catch (NoResultException ex) {
			// nothing here
		}
		try {
			if (lagerDto.getLagerartCNr()
					.equals(LagerFac.LAGERART_WARENEINGANG)) {
				Query query1 = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query1.setParameter(1, lagerDto.getMandantCNr());
				query1.setParameter(2, LagerFac.LAGERART_WARENEINGANG);
				doppelt = (Lager) query1.getSingleResult();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
						new Exception(
								"FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));

			}
		} catch (NoResultException ex) {
			// nothing here
		}

		if (lagerDto.getIId() == null) {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LAGER);
			lagerDto.setIId(pk);
		}

		try {
			Lager lager = new Lager(lagerDto.getIId(), lagerDto.getCNr(),
					lagerDto.getLagerartCNr(), lagerDto.getMandantCNr(),
					lagerDto.getBBestellvorschlag(),
					lagerDto.getBInternebestellung(), lagerDto.getBVersteckt(),
					lagerDto.getBKonsignationslager());
			em.persist(lager);
			em.flush();

			setLagerFromLagerDto(lager, lagerDto);
			return lager.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public Integer createLagerplatz(LagerplatzDto lagerplatzDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerplatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerplatzDto == null"));
		}
		if (lagerplatzDto.getCLagerplatz() == null
				|| lagerplatzDto.getLagerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerplatzDto.getCLagerplatz() == null || lagerplatzDto.getLagerIId() == null"));
		}
		lagerplatzDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lagerplatzDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		Lagerplatz doppelt = null;
		try {
			Query query = em
					.createNamedQuery("LagerplatzfindByLagerIIdCLagerplatz");
			query.setParameter(1, lagerplatzDto.getLagerIId());
			query.setParameter(2, lagerplatzDto.getCLagerplatz());
			doppelt = (Lagerplatz) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_LAGERPLATZ.UK"));

			}
		} catch (NoResultException ex) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LAGERPLATZ);
			lagerplatzDto.setIId(pk);
			Lagerplatz lagerplatz = new Lagerplatz(lagerplatzDto.getIId(),
					lagerplatzDto.getCLagerplatz(), new Integer(0),
					lagerplatzDto.getLagerIId(),
					lagerplatzDto.getPersonalIIdAendern(),
					lagerplatzDto.getTAendern());
			em.persist(lagerplatz);
			em.flush();

			if (lagerplatzDto.getIMaxmenge() == null) {
				lagerplatzDto.setIMaxmenge(lagerplatz.getIMaxmenge());
			}

			setLagerplatzFromLagerplatzDto(lagerplatz, lagerplatzDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return lagerplatzDto.getIId();
	}

	public void removeLagerplatz(Integer iId) throws EJBExceptionLP {
		Lagerplatz toRemove = em.find(Lagerplatz.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeLagerplatz. Es gibt keine iid " + iId);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeLagerplatz(LagerplatzDto lagerplatzDto)
			throws EJBExceptionLP {
		if (lagerplatzDto != null) {
			Integer iId = lagerplatzDto.getIId();
			removeLagerplatz(iId);
		}
	}

	public void updateLagerplatz(LagerplatzDto lagerplatzDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerplatzDto != null) {
			Integer iId = lagerplatzDto.getIId();
			Lagerplatz lagerplatz = em.find(Lagerplatz.class, iId);
			if (lagerplatz == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateLagerplatz. Es gibt keine iid " + iId
								+ "\ndto.toString(): "
								+ lagerplatzDto.toString());
			}

			try {
				Query query = em
						.createNamedQuery("LagerplatzfindByLagerIIdCLagerplatz");
				query.setParameter(1, lagerplatzDto.getLagerIId());
				query.setParameter(2, lagerplatzDto.getCLagerplatz());
				// @todo getSingleResult oder getResultList ?
				Integer iIdVorhanden = ((Lagerplatz) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_LAGERPLATZ.UK"));

				}
			} catch (NoResultException ex) {

			}
			lagerplatzDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			lagerplatzDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			setLagerplatzFromLagerplatzDto(lagerplatz, lagerplatzDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public LagerplatzDto lagerplatzFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		Lagerplatz lagerplatz = em.find(Lagerplatz.class, iId);
		if (lagerplatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei lagerplatzFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleLagerplatzDto(lagerplatz);
	}

	private void setLagerplatzFromLagerplatzDto(Lagerplatz lagerplatz,
			LagerplatzDto lagerplatzDto) {
		lagerplatz.setCLagerplatz(lagerplatzDto.getCLagerplatz());
		lagerplatz.setIMaxmenge(lagerplatzDto.getIMaxmenge());
		lagerplatz.setLagerIId(lagerplatzDto.getLagerIId());
		lagerplatz.setTAendern(lagerplatzDto.getTAendern());
		lagerplatz.setPersonalIIdAendern(lagerplatzDto.getPersonalIIdAendern());
		lagerplatz.setPaternosterIId(lagerplatzDto.getPaternosterIId());
		em.merge(lagerplatz);
		em.flush();
	}

	private LagerplatzDto assembleLagerplatzDto(Lagerplatz lagerplatz) {
		return LagerplatzDtoAssembler.createDto(lagerplatz);
	}

	private LagerplatzDto[] assembleLagerplatzDtos(Collection<?> lagerplatzs) {
		List<LagerplatzDto> list = new ArrayList<LagerplatzDto>();
		if (lagerplatzs != null) {
			Iterator<?> iterator = lagerplatzs.iterator();
			while (iterator.hasNext()) {
				Lagerplatz lagerplatz = (Lagerplatz) iterator.next();
				list.add(assembleLagerplatzDto(lagerplatz));
			}
		}
		LagerplatzDto[] returnArray = new LagerplatzDto[list.size()];
		return (LagerplatzDto[]) list.toArray(returnArray);
	}

	public LagerplatzDto[] getAlleLagerplaetze() {
		Query query = em.createNamedQuery("LagerplatzfindAll");
		Collection<?> c = query.getResultList();
		LagerplatzDto[] lagerplatzDto = assembleLagerplatzDtos(c);
		return lagerplatzDto;
	}

	public Integer createArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikellagerplaetzeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellagerplaetzeDto == null"));
		}
		if (artikellagerplaetzeDto.getArtikelIId() == null
				&& artikellagerplaetzeDto.getLagerplatzIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikellagerplaetzeDto.getArtikelIId() == null && artikellagerplaetzeDto.getLagerplatzIId() == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdLagerplatzIId");
			query.setParameter(1, artikellagerplaetzeDto.getArtikelIId());
			query.setParameter(2, artikellagerplaetzeDto.getLagerplatzIId());
			Artikellagerplaetze doppelt = (Artikellagerplaetze) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELLAGERPLAETZE.UK"));
		} catch (NoResultException ex) {
			//
		}

		Integer paternosterIId = null;

		// PJ 14831

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_MEHRFACH_LAGERPLATZ_JE_LAGER,
				theClientDto)) {

			Lagerplatz lagerplatzHinzuzufuegen = em.find(Lagerplatz.class,
					artikellagerplaetzeDto.getLagerplatzIId());
			if (lagerplatzHinzuzufuegen.getPaternosterIId() != null) {
				paternosterIId = lagerplatzHinzuzufuegen.getPaternosterIId();
			}

			Query query = em
					.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
			query.setParameter(1, artikellagerplaetzeDto.getArtikelIId());
			Collection vorhandeneLagerplaetze = query.getResultList();

			Iterator<?> iterator = vorhandeneLagerplaetze.iterator();
			while (iterator.hasNext()) {
				Artikellagerplaetze temp = (Artikellagerplaetze) iterator
						.next();

				Lagerplatz lagerplatz = em.find(Lagerplatz.class,
						temp.getLagerplatzIId());

				if (lagerplatz.getLagerIId().equals(
						lagerplatzHinzuzufuegen.getLagerIId())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH,
							"FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH");
				}

			}
		}

		Query queryNext = em
				.createNamedQuery("ArtikellagerplaetzeejbSelectNextReihung");
		queryNext.setParameter(1, artikellagerplaetzeDto.getArtikelIId());

		Integer i = (Integer) queryNext.getSingleResult();

		if (i == null) {
			i = new Integer(0);
		}
		i = new Integer(i.intValue() + 1);
		artikellagerplaetzeDto.setiSort(i);

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_ARTIKELLAGERPLAETZE);
			artikellagerplaetzeDto.setIId(pk);

			Artikellagerplaetze artikellagerplaetze = new Artikellagerplaetze(
					artikellagerplaetzeDto.getIId(),
					artikellagerplaetzeDto.getArtikelIId(),
					artikellagerplaetzeDto.getLagerplatzIId(),
					artikellagerplaetzeDto.getiSort());
			em.persist(artikellagerplaetze);
			em.flush();
			setArtikellagerplaetzeFromArtikellagerplaetzeDto(
					artikellagerplaetze, artikellagerplaetzeDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		if (paternosterIId != null) {
			try {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								artikellagerplaetzeDto.getArtikelIId(),
								theClientDto);

				getAutoPaternosterFac().paternosterAddArtikel(paternosterIId,
						artikelDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return artikellagerplaetzeDto.getIId();
	}

	public LagerDto getLagerDesErstenArtikellagerplatzes(Integer artikelIId,
			TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdOrderByISort");
		query.setParameter(1, artikelIId);

		List l = query.getResultList();
		Iterator it = l.iterator();

		if (it.hasNext()) {
			Artikellagerplaetze alp = (Artikellagerplaetze) it.next();
			Lagerplatz al = em.find(Lagerplatz.class, alp.getLagerplatzIId());
			return lagerFindByPrimaryKey(al.getLagerIId());
		}

		return null;
	}

	public void removeArtikellagerplaetze(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Artikellagerplaetze toRemove = em.find(Artikellagerplaetze.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtikellagerplaetze. Es gibt keine iid "
							+ iId);
		}
		Lagerplatz lagerplatz = em.find(Lagerplatz.class,
				toRemove.getLagerplatzIId());

		Integer artikelIId = toRemove.getArtikelIId();

		if (lagerplatz.getPaternosterIId() != null) {
			try {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(toRemove.getArtikelIId(),
								theClientDto);

				getAutoPaternosterFac().paternosterDelArtikel(
						lagerplatz.getPaternosterIId(), artikelDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
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
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdOrderByISort");
		query.setParameter(1, artikelIId);

		List l = query.getResultList();
		Iterator it = l.iterator();
		int iSort = 1;
		while (it.hasNext()) {
			Artikellagerplaetze al = (Artikellagerplaetze) it.next();
			al.setiSort(iSort);
			iSort++;

		}

	}

	public void removeArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellagerplaetzeDto != null) {
			Integer iId = artikellagerplaetzeDto.getIId();
			removeArtikellagerplaetze(iId, theClientDto);
		}
	}

	public void updateArtikellagerplaetze(
			ArtikellagerplaetzeDto artikellagerplaetzeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellagerplaetzeDto != null) {
			Integer iId = artikellagerplaetzeDto.getIId();
			Artikellagerplaetze artikellagerplaetze = em.find(
					Artikellagerplaetze.class, iId);
			if (artikellagerplaetze == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateArtikellagerplaetze. Es gibt keine iid "
								+ iId + "\ndto.toString() "
								+ artikellagerplaetzeDto.toString());
			}
			try {
				Query query = em
						.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdLagerplatzIId");
				query.setParameter(1, artikellagerplaetzeDto.getArtikelIId());
				query.setParameter(2, artikellagerplaetzeDto.getLagerplatzIId());
				Integer iIdVorhanden = ((Artikellagerplaetze) query
						.getSingleResult()).getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_ARTIKELLAGERPLAETZE.UK"));
				}

			} catch (NoResultException ex) {

			}

			// PJ 14831

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_MEHRFACH_LAGERPLATZ_JE_LAGER,
					theClientDto)) {

				Lagerplatz lagerplatzHinzuzufuegen = em.find(Lagerplatz.class,
						artikellagerplaetzeDto.getLagerplatzIId());

				Query query = em
						.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
				query.setParameter(1, artikellagerplaetzeDto.getArtikelIId());
				Collection vorhandeneLagerplaetze = query.getResultList();

				Iterator<?> iterator = vorhandeneLagerplaetze.iterator();
				while (iterator.hasNext()) {
					Artikellagerplaetze temp = (Artikellagerplaetze) iterator
							.next();

					Lagerplatz lagerplatz = em.find(Lagerplatz.class,
							temp.getLagerplatzIId());

					if (!temp.getIId().equals(artikellagerplaetzeDto.getIId())) {

						if (lagerplatz.getLagerIId().equals(
								lagerplatzHinzuzufuegen.getLagerIId())) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH,
									"FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH");
						}
					}

				}
			}

			setArtikellagerplaetzeFromArtikellagerplaetzeDto(
					artikellagerplaetze, artikellagerplaetzeDto);
		}
	}

	public Integer getAnzahlVerwendungenEinesLagerplatzes(Integer lagerplatzIId) {

		Query query = em
				.createNamedQuery("ArtikellagerplaetzeAnzahlVerwendungEinesLagerplatz");
		query.setParameter(1, lagerplatzIId);
		long i = (Long) query.getSingleResult();
		return (int) i;
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		Artikellagerplaetze artikellagerplaetze = em.find(
				Artikellagerplaetze.class, iId);
		if (artikellagerplaetze == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikellagerplaetzeFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		ArtikellagerplaetzeDto dto = assembleArtikellagerplaetzeDto(artikellagerplaetze);

		dto.setLagerplatzDto(lagerplatzFindByPrimaryKey(dto.getLagerplatzIId()));
		return dto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getLagerplaezteEinesArtikels(Integer artikelIId,
			Integer lagerIId) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}

		String s = "";
		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
		query.setParameter(1, artikelIId);

		Collection<?> cl = query.getResultList();
		ArtikellagerplaetzeDto[] artikellagerplaetzeDto = assembleArtikellagerplaetzeDtos(cl);

		for (int i = 0; i < artikellagerplaetzeDto.length; i++) {

			Lagerplatz lagerplatz = em.find(Lagerplatz.class,
					artikellagerplaetzeDto[i].getLagerplatzIId());

			LagerplatzDto lagerplatzDto = assembleLagerplatzDto(lagerplatz);

			if (lagerIId != null
					&& !lagerIId.equals(lagerplatzDto.getLagerIId())) {
				continue;
			}
			s += lagerplatzDto.getCLagerplatz() + ", ";
		}
		return s;
	}

	public String istLagerplatzBereitsDurchAnderenArtikelBelegt(
			Integer artikelIId, Integer lagerplatzIId, TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByLagerplatzIId");
		query.setParameter(1, lagerplatzIId);

		String artikel = null;
		Collection<?> cl = query.getResultList();
		ArtikellagerplaetzeDto[] artikellagerplaetzeDto = assembleArtikellagerplaetzeDtos(cl);

		for (int i = 0; i < artikellagerplaetzeDto.length; i++) {

			if (!artikellagerplaetzeDto[i].getArtikelIId().equals(artikelIId)) {

				artikel = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								artikellagerplaetzeDto[i].getArtikelIId(),
								theClientDto).formatArtikelbezeichnung();

				break;
			}

		}
		return artikel;
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerIId(
			Integer artikelIId, Integer lagerIId) {
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}

		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
		query.setParameter(1, artikelIId);

		Collection<?> cl = query.getResultList();
		ArtikellagerplaetzeDto[] artikellagerplaetzeDto = assembleArtikellagerplaetzeDtos(cl);

		for (int i = 0; i < artikellagerplaetzeDto.length; i++) {
			Lagerplatz lagerplatz = em.find(Lagerplatz.class,
					artikellagerplaetzeDto[i].getLagerplatzIId());
			LagerplatzDto lagerplatzDto = assembleLagerplatzDto(lagerplatz);
			artikellagerplaetzeDto[i].setLagerplatzDto(lagerplatzDto);

			if (lagerIId.equals(lagerplatzDto.getLagerIId())) {
				return artikellagerplaetzeDto[i];
			}
		}

		return null;
	}

	public ArtikellagerplaetzeDto getErstenArtikellagerplatz(
			Integer artikelIId, TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdOrderByISort");
		query.setParameter(1, artikelIId);

		List l = query.getResultList();
		Iterator it = l.iterator();

		if (it.hasNext()) {
			Artikellagerplaetze alp = (Artikellagerplaetze) it.next();

			ArtikellagerplaetzeDto alpDto = assembleArtikellagerplaetzeDto(alp);

			Lagerplatz al = em.find(Lagerplatz.class, alp.getLagerplatzIId());

			LagerplatzDto lagerplatzDto = assembleLagerplatzDto(al);

			alpDto.setLagerplatzDto(lagerplatzDto);
			if (l.size() > 1) {
				alpDto.setbEsGibtMehrereLagerplaetze(true);
			}
			return alpDto;
		}

		return null;
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIId(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}

		try {
			Query query = em
					.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdLagerplatzIId");
			query.setParameter(1, artikelIId);
			query.setParameter(2, lagerIId);
			Artikellagerplaetze artikellagerplaetze = (Artikellagerplaetze) query
					.getSingleResult();
			ArtikellagerplaetzeDto dto = assembleArtikellagerplaetzeDto(artikellagerplaetze);
			dto.setLagerplatzDto(lagerplatzFindByPrimaryKey(dto
					.getLagerplatzIId()));
			return dto;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto) {
		ArtikellagerplaetzeDto oArtikellagerplaetzeDtoO = null;

		try {
			oArtikellagerplaetzeDtoO = artikellagerplaetzeFindByArtikelIIdLagerplatzIId(
					artikelIId, lagerIId, theClientDto);
		} catch (Throwable t) {
			myLogger.error("artikellagerplaetzeFindByArtikelIIdLagerIIdOhneExc");
		}

		return oArtikellagerplaetzeDtoO;
	}

	private void setArtikellagerplaetzeFromArtikellagerplaetzeDto(
			Artikellagerplaetze artikellagerplaetze,
			ArtikellagerplaetzeDto artikellagerplaetzeDto) {
		artikellagerplaetze.setArtikelIId(artikellagerplaetzeDto
				.getArtikelIId());
		artikellagerplaetze.setLagerplatzIId(artikellagerplaetzeDto
				.getLagerplatzIId());

		em.merge(artikellagerplaetze);
		em.flush();
	}

	private ArtikellagerplaetzeDto assembleArtikellagerplaetzeDto(
			Artikellagerplaetze artikellagerplaetze) {
		return ArtikellagerplaetzeDtoAssembler.createDto(artikellagerplaetze);
	}

	private ArtikellagerplaetzeDto[] assembleArtikellagerplaetzeDtos(
			Collection<?> artikellagerplaetzes) {
		List<ArtikellagerplaetzeDto> list = new ArrayList<ArtikellagerplaetzeDto>();
		if (artikellagerplaetzes != null) {
			Iterator<?> iterator = artikellagerplaetzes.iterator();
			while (iterator.hasNext()) {
				Artikellagerplaetze artikellagerplaetze = (Artikellagerplaetze) iterator
						.next();
				list.add(assembleArtikellagerplaetzeDto(artikellagerplaetze));
			}
		}
		ArtikellagerplaetzeDto[] returnArray = new ArtikellagerplaetzeDto[list
				.size()];
		return (ArtikellagerplaetzeDto[]) list.toArray(returnArray);
	}

	/**
	 * Loescht ein in der Datenbank vorhandenes Lager, wenn darauf nicht mehr
	 * referenziert wird.
	 * 
	 * @param lagerDto
	 *            Lager
	 * @throws EJBExceptionLP
	 */
	public void removeLager(LagerDto lagerDto) throws EJBExceptionLP {
		myLogger.entry();
		if (lagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lagerDto == null"));
		}
		if (lagerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lagerDto.getIId()"));
		}
		Integer iId = lagerDto.getIId();

		Lager lager = em.find(Lager.class, iId);
		if (lager == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeLager. Es gibt keine iid "
							+ lagerDto.getIId() + "\ndto.toString(): "
							+ lagerDto.toString());
		}
		if (lager.getCNr().equals(LagerFac.LAGER_KEINLAGER)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception("DARF_NICHT_GELOESCHT_WERDEN"));
		}

		Lager toRemove = em.find(Lager.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeLager. Es gibt keine iid "
							+ lagerDto.getIId() + "\ndto.toString(): "
							+ lagerDto.toString());
		}

		// Lagerberechtigungen loeschen
		Query query = em.createNamedQuery("LagerrollefindByLagerIId");
		query.setParameter(1, iId);
		Collection<?> c = query.getResultList();

		Iterator<?> iterator = c.iterator();
		while (iterator.hasNext()) {
			Lagerrolle lagerrolle = (Lagerrolle) iterator.next();
			em.remove(lagerrolle);
			em.flush();
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	/**
	 * Schreibt die &Auml;nderungen eines Lager- Datensatzes in der Datenbank
	 * fest.
	 * 
	 * @param lagerDto
	 *            LagerDto
	 * @throws EJBExceptionLP
	 *             lagerDto == null
	 */
	public void updateLager(LagerDto lagerDto) throws EJBExceptionLP {
		myLogger.entry();
		if (lagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerDto == null"));
		}
		Integer iId = lagerDto.getIId();

		Lager lager = null;
		lager = em.find(Lager.class, iId);
		if (lager == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateLager. Es gibt kein Lager mit iid " + iId
							+ "\ndto.tostring: " + lagerDto.toString());
		}
		try {
			if (lagerDto.getLagerartCNr().equals(LagerFac.LAGERART_HAUPTLAGER)) {
				Query query = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query.setParameter(1, lagerDto.getMandantCNr());
				query.setParameter(2, LagerFac.LAGERART_HAUPTLAGER);
				Integer iIdVorhanden = ((Lager) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
							new Exception(
									"FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));
				}
			}
		} catch (NoResultException ex) {

		}

		try {
			if (lagerDto.getLagerartCNr().equals(
					LagerFac.LAGERART_WERTGUTSCHRIFT)) {
				Query query = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query.setParameter(1, lagerDto.getMandantCNr());
				query.setParameter(2, LagerFac.LAGERART_WERTGUTSCHRIFT);
				Integer iIdVorhanden = ((Lager) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
							new Exception(
									"FEHLER_LAGER_WERTGUTSCHRIFT_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));
				}
			}
		} catch (NoResultException ex) {

		}
		try {
			if (lagerDto.getLagerartCNr()
					.equals(LagerFac.LAGERART_WARENEINGANG)) {
				Query query = em
						.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
				query.setParameter(1, lagerDto.getMandantCNr());
				query.setParameter(2, LagerFac.LAGERART_WARENEINGANG);
				Integer iIdVorhanden = ((Lager) query.getSingleResult())
						.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
							new Exception(
									"FEHLER_LAGER_WARENEINGANG_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN"));
				}
			}
		} catch (NoResultException ex) {

		}
		try {
			Query query = em.createNamedQuery("LagerfindByCNrByMandantCNr");
			query.setParameter(1, lagerDto.getCNr());
			query.setParameter(2, lagerDto.getMandantCNr());
			Integer iIdVorhanden = ((Lager) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"WW_LAGER.UK"));
			}

		} catch (NoResultException ex) {

		}
		setLagerFromLagerDto(lager, lagerDto);
	}

	/**
	 * Holt einen Lager-Datensatz anhand des Primaerschluessels.
	 * 
	 * @param iId
	 *            7
	 * @throws EJBExceptionLP
	 *             iId == null
	 * @return LagerDto
	 */
	public LagerDto lagerFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");
		LagerDto lagerDto = lagerFindByPrimaryKeyOhneExc(iId);
		if (lagerDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei lagerFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return lagerDto;
	}

	/**
	 * Holt einen Lager-Datensatz anhand der Lagerkennung und des Mandanten.
	 * 
	 * @param cNr
	 *            Keller
	 * @param mandantCNr
	 *            001
	 * @throws EJBExceptionLP
	 *             iId == null
	 * @return LagerDto
	 */
	public LagerDto lagerFindByCNrByMandantCNr(String cNr, String mandantCNr)
			throws EJBExceptionLP {
		LagerDto lagerDto = lagerFindByCNrByMandantCNrImpl(cNr, mandantCNr);
		if (lagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei lagerFindByCNrByMandantCNr. Es gibt kein Lager mit cnr "
							+ cNr + " f\u00FCr den Mandant " + mandantCNr);
		}
		return lagerDto;
	}

	public LagerDto lagerFindByCNrByMandantCNrOhneExc(String cnr,
			String mandantCnr) {
		return lagerFindByCNrByMandantCNrImpl(cnr, mandantCnr);
	}

	private LagerDto lagerFindByCNrByMandantCNrImpl(String cnr,
			String mandantCnr) {
		Validator.notEmpty(cnr, "cnr");
		Validator.notEmpty(mandantCnr, "mandantCnr");

		Query query = em.createNamedQuery("LagerfindByCNrByMandantCNr");
		query.setParameter(1, cnr);
		query.setParameter(2, mandantCnr);
		Lager lager = (Lager) query.getSingleResult();
		if (lager == null)
			return null;

		return assembleLagerDto(lager);
	}

	private void setLagerFromLagerDto(Lager lager, LagerDto lagerDto) {
		lager.setCNr(lagerDto.getCNr());
		lager.setLagerartCNr(lagerDto.getLagerartCNr());
		lager.setMandantCNr(lagerDto.getMandantCNr());
		lager.setBBestellvorschlag(lagerDto.getBBestellvorschlag());
		lager.setBInternebestellung(lagerDto.getBInternebestellung());
		lager.setBVersteckt(lagerDto.getBVersteckt());
		lager.setBKonsignationslager(lagerDto.getBKonsignationslager());
		lager.setILoslagersort(lagerDto.getILoslagersort());
		em.merge(lager);
		em.flush();
	}

	private LagerDto assembleLagerDto(Lager lager) {
		return LagerDtoAssembler.createDto(lager);
	}

	private LagerDto[] assembleLagerDtos(Collection<?> lagers) {
		List<LagerDto> list = new ArrayList<LagerDto>();
		if (lagers != null) {
			Iterator<?> iterator = lagers.iterator();
			while (iterator.hasNext()) {
				Lager lager = (Lager) iterator.next();
				if (!lager.getCNr().equals(LagerFac.LAGER_KEINLAGER)) {
					if (!lager.getLagerartCNr().equals(
							LagerFac.LAGERART_WERTGUTSCHRIFT)) {
						list.add(assembleLagerDto(lager));
					}
				}
			}
		}
		LagerDto[] returnArray = new LagerDto[list.size()];
		return (LagerDto[]) list.toArray(returnArray);
	}

	/**
	 * Liefert aktuellen Gestehungspreis eines Artikels im Hauptlager des
	 * Mandanten zurueck.
	 * 
	 * @param iIdArtikelI
	 *            4711
	 * @param theClientDto
	 *            String
	 * @return BigDecimal Gestehungspreis
	 * @throws EJBExceptionLP
	 *             artikelIId == null || lagerIId == null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getGemittelterGestehungspreisDesHauptlagers(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal nGestehungspreis = null;
		// das Hauptlager des Mandanten bestimmen
		LagerDto hauptlager = getHauptlagerDesMandanten(theClientDto);
		nGestehungspreis = getGemittelterGestehungspreisEinesLagers(
				iIdArtikelI, hauptlager.getIId(), theClientDto);
		return nGestehungspreis == null ? new BigDecimal(0) : nGestehungspreis;
	}

	/**
	 * Ordnet einem Lager einen bestimmen Artikel zu. Lagerstand und
	 * Gestehungspreis sind beim Anlegen automatisch 0.
	 * 
	 * @param artikellagerDto
	 *            ArtikellagerDto
	 * @throws EJBExceptionLP
	 *             artikellagerDto == null oder artikellagerDto.getArtikelIId()
	 *             == null || artikellagerDto.getLagerIId()==null
	 */
	public void createArtikellager(ArtikellagerDto artikellagerDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikellagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellagerDto == null"));
		}
		if (artikellagerDto.getArtikelIId() == null
				|| artikellagerDto.getLagerIId() == null
				|| artikellagerDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikellagerDto.getArtikelIId() == null || artikellagerDto.getLagerIId()==null || artikellagerDto.getMandantCNr() == null"));
		}
		try {
			Artikellager artikellager = null;
			try {
				artikellager = new Artikellager(
						artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(),
						artikellagerDto.getMandantCNr());
				em.persist(artikellager);
				em.flush();

			} catch (EntityExistsException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY,
						new Exception(ex1));
			}
			setArtikellagerFromArtikellagerDto(artikellager, artikellagerDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public GeraetesnrDto[] getGeraeteseriennummerEinerLagerbewegung(
			String belegartCnr, Integer belegartpositionIId, String cSnr) {
		GeraetesnrDto[] snrs = new GeraetesnrDto[0];
		LagerbewegungDto lBewDto = getLetzteintrag(belegartCnr,
				belegartpositionIId, cSnr);

		if (lBewDto != null) {
			Query query = em.createNamedQuery("GeraetesnrfindByIIdBuchung");
			query.setParameter(1, lBewDto.getIIdBuchung());
			Collection c = query.getResultList();
			snrs = new GeraetesnrDto[c.size()];
			Iterator<?> iterator = c.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				snrs[i] = assembleGeraetesnrDto((Geraetesnr) iterator.next());
				i++;
			}

		}

		return snrs;

	}

	public void removeArtikellager(ArtikellagerDto artikellagerDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikellagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikellagerDto == null"));
		}
		if (artikellagerDto.getArtikelIId() == null
				|| artikellagerDto.getLagerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikellagerDto.getArtikelIId() == null || artikellagerDto.getLagerIId() == null"));
		}

		Artikellager artikellager = null;
		try {
			artikellager = em.find(Artikellager.class,
					new ArtikellagerPK(artikellagerDto.getArtikelIId(),
							artikellagerDto.getLagerIId()));
			if (artikellager == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtikellager. Es gibt kein artikellager.\ndto.toString: "
								+ artikellagerDto.toString());
			}
			em.remove(artikellager);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	/**
	 * Schreibt Aenderungen wie den Lagerstand und den Gestehungspreis eines
	 * Artikels in einem bestimmen Lager fest.
	 * 
	 * @param artikellagerDto
	 *            ArtikellagerDto
	 * @throws EJBExceptionLP
	 *             artikellagerDto == null oder artikellagerDto.getArtikelIId()
	 *             == null || artikellagerDto.getLagerIId()==null ||
	 *             artikellagerDto.getNGestehungspreis() == null ||
	 *             artikellagerDto.getFLagerstand() == null
	 */
	public void updateArtikellager(ArtikellagerDto artikellagerDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikellagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellagerDto == null"));
		}
		if (artikellagerDto.getArtikelIId() == null
				|| artikellagerDto.getLagerIId() == null
				|| artikellagerDto.getNGestehungspreis() == null
				|| artikellagerDto.getNLagerstand() == null
				|| artikellagerDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikellagerDto.getArtikelIId() == null || artikellagerDto.getLagerIId()==null || artikellagerDto.getNGestehungspreis() == null || artikellagerDto.getFLagerstand() == null || artikellagerDto.getMandantCNr() == null"));
		}
		ArtikellagerPK artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikellagerDto.getArtikelIId());
		artikellagerPK.setLagerIId(artikellagerDto.getLagerIId());
		Artikellager artikellager = null;
		artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikellager. Es gibt kein Artikellager mit pk "
							+ artikellagerPK.toString());
		}
		setArtikellagerFromArtikellagerDto(artikellager, artikellagerDto);
	}

	public void artikellagerplatzCRUD(Integer artikelIId, Integer lagerIId,
			Integer lagerplatzIId, TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
		query.setParameter(1, artikelIId);

		Collection<?> cl = query.getResultList();
		ArtikellagerplaetzeDto[] artikellagerplaetzeDto = assembleArtikellagerplaetzeDtos(cl);

		if (artikellagerplaetzeDto.length > 0) {

			for (int i = 0; i < artikellagerplaetzeDto.length; i++) {
				LagerplatzDto lagerplatzDto = lagerplatzFindByPrimaryKey(artikellagerplaetzeDto[i]
						.getLagerplatzIId());

				if (lagerplatzDto.getLagerIId().equals(lagerIId)) {

					if (lagerplatzIId == null) {
						// Eintrag loeschen
						removeArtikellagerplaetze(
								artikellagerplaetzeDto[i].getIId(),
								theClientDto);
					} else {
						if (lagerplatzIId.equals(artikellagerplaetzeDto[i]
								.getLagerplatzIId())) {
							// nix machen
						} else {
							// Eintrag aendern
							artikellagerplaetzeDto[i]
									.setLagerplatzIId(lagerplatzIId);

							// Nachsehen obs den nicht schon gibt
							try {
								Query queryVorhanden = em
										.createNamedQuery("ArtikellagerplaetzefindByArtikelIIdLagerplatzIId");
								queryVorhanden.setParameter(1, artikelIId);
								queryVorhanden.setParameter(2, lagerplatzIId);
								queryVorhanden.getSingleResult();
							} catch (NoResultException ex) {
								// Wenn nicht, dann anlegen
								updateArtikellagerplaetze(
										artikellagerplaetzeDto[i], theClientDto);
							}
						}
					}
				}
			}

		} else {

			if (lagerplatzIId != null) {
				// neuen eintrag anlegen

				ArtikellagerplaetzeDto artikellagerplaetzeDtoNeu = new ArtikellagerplaetzeDto();
				artikellagerplaetzeDtoNeu.setArtikelIId(artikelIId);
				artikellagerplaetzeDtoNeu.setLagerplatzIId(lagerplatzIId);
				createArtikellagerplaetze(artikellagerplaetzeDtoNeu,
						theClientDto);
			}
		}

	}

	public void updateGestpreisArtikellager(ArtikellagerDto artikellagerDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikellagerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikellagerDto == null"));
		}
		if (artikellagerDto.getArtikelIId() == null
				|| artikellagerDto.getLagerIId() == null
				|| artikellagerDto.getNGestehungspreis() == null
				|| artikellagerDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikellagerDto.getArtikelIId() == null || artikellagerDto.getLagerIId()==null || artikellagerDto.getNGestehungspreis() == null || artikellagerDto.getMandantCNr() == null"));
		}
		ArtikellagerPK artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikellagerDto.getArtikelIId());
		artikellagerPK.setLagerIId(artikellagerDto.getLagerIId());
		Artikellager artikellager = null;

		myLogger.logKritisch("Gestehungspreis geaendert:"
				+ artikellagerDto.toString());
		String logEintrag = "artikelIId=" + artikellagerDto.getArtikelIId()
				+ ",lagerIId=" + artikellagerDto.getLagerIId();

		artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager == null) {
			try {
				artikellager = new Artikellager(
						artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(),
						artikellagerDto.getMandantCNr());
				em.persist(artikellager);
				em.flush();
				artikellager.setNGestehungspreis(artikellagerDto
						.getNGestehungspreis());
			} catch (EntityExistsException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
						"Fehler bei updateGestpreisArtikellager. Es gibt bereits einen Eintrag. \ndto.toString: "
								+ artikellagerDto.toString());
			}

		}

		BigDecimal preisVorher = artikellager.getNGestehungspreis();

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				artikellagerDto.getArtikelIId(), theClientDto);

		if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == true
				&& artikellager.getNLagerstand().doubleValue() > 0) {

			if (Helper.short2boolean(artikelDto.getBChargennrtragend())
					|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
				SeriennrChargennrAufLagerDto[] snrs = getAllSerienChargennrAufLagerInfoDtos(
						artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(), false, null,
						theClientDto);

				ArrayList alSnrs = new ArrayList();

				BigDecimal menge = new BigDecimal(0);

				for (int i = 0; i < snrs.length; i++) {
					SeriennrChargennrMitMengeDto.add2SnrChnrDtos(alSnrs,
							snrs[i]);
					menge = menge.add(snrs[i].getNMenge());
				}

				bucheUm(artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(),
						artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(), menge, alSnrs,
						"Gestpreis\u00E4nderung: Preis vorher: " + preisVorher,
						artikellagerDto.getNGestehungspreis(), theClientDto);

			} else {
				BigDecimal lagerstand = getLagerstand(
						artikellagerDto.getArtikelIId(),
						artikellagerDto.getLagerIId(), theClientDto);
				if (lagerstand.doubleValue() > 0) {
					bucheUm(artikellagerDto.getArtikelIId(),
							artikellagerDto.getLagerIId(),
							artikellagerDto.getArtikelIId(),
							artikellagerDto.getLagerIId(), lagerstand, null,
							"Gestpreis\u00E4nderung: Preis vorher: "
									+ preisVorher,
							artikellagerDto.getNGestehungspreis(), theClientDto);
				}
			}

		} else {
			artikellager.setNGestehungspreis(artikellagerDto
					.getNGestehungspreis());
			em.merge(artikellager);
			em.flush();
		}

		// Lagerstand + Gestpreis hier abrufen
		if (bLagerLogging == null) {
			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_LAGER_LOGGING);
				bLagerLogging = Helper.short2boolean(new Short(parameter
						.getCWert()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		if (bLagerLogging) {
			logEintrag += ";GestpreisVorher="
					+ artikellager.getNGestehungspreis() + ","
					+ theClientDto.toString();

			ProtokollDto protokollDto = new ProtokollDto();

			protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_UPDATE_GESTPREIS);
			protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);

			protokollDto.setCText(logEintrag);
			protokollDto.setCLangtext("");

			erstelleProtokollEintrag(protokollDto, theClientDto);
		}
	}

	public void updateLagerabgangursprung(
			LagerabgangursprungDto lagerabgangursprungDto)
			throws EJBExceptionLP {
		if (lagerabgangursprungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerabgangursprungDto == null"));
		}
		if (lagerabgangursprungDto.getILagerbewegungid() == null
				|| lagerabgangursprungDto.getILagerbewegungidursprung() == null
				|| lagerabgangursprungDto.getNVerbrauchtemenge() == null
				|| lagerabgangursprungDto.getNGestehungspreis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lagerabgangursprungDto.getLagerbewegungIIdBuchung() == null || lagerabgangursprungDto.getLagerbewegungIIdBuchungsursprung() == null ||  lagerabgangursprungDto.getFVerbrauchtemenge() == null || lagerabgangursprungDto.getNGestehungspreis() == null"));
		}
		LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
		lagerabgangursprungPK.setILagerbewegungid(lagerabgangursprungDto
				.getILagerbewegungid());
		lagerabgangursprungPK
				.setILagerbewegungidursprung(lagerabgangursprungDto
						.getILagerbewegungidursprung());
		Lagerabgangursprung lagerabgangursprung = null;
		// myLogger.info("ims487" + lagerabgangursprungDto.toString());
		lagerabgangursprung = em.find(Lagerabgangursprung.class,
				lagerabgangursprungPK);
		if (lagerabgangursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateLagerabgangursprung. Es gibt keinen pk "
							+ lagerabgangursprungPK.toString());
		}

		try {
			// Gestehungspreis in in Los updaten
			LagerbewegungDto[] dtos = lagerbewegungFindByIIdBuchung(lagerabgangursprungDto
					.getILagerbewegungid());

			if (dtos[0].getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
				getFertigungFac().updateLosistmaterialGestehungspreis(
						dtos[0].getIBelegartpositionid(),
						lagerabgangursprungDto.getNGestehungspreis(), null);
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		setLagerabgangursprungFromLagerabgangursprungDto(lagerabgangursprung,
				lagerabgangursprungDto);
	}

	/**
	 * Findet, ob ein bestimmter Artikel einem bestimmten Lager zugeordnet ist.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @throws EJBExceptionLP
	 * @return ArtikellagerDto
	 */
	public ArtikellagerDto artikellagerFindByPrimaryKey(Integer artikelIId,
			Integer lagerIId) throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}
		ArtikellagerPK artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikelIId);
		artikellagerPK.setLagerIId(lagerIId);
		Artikellager artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleArtikellagerDto(artikellager);
	}

	private void setArtikellagerFromArtikellagerDto(Artikellager artikellager,
			ArtikellagerDto artikellagerDto) {
		artikellager.setNGestehungspreis(artikellagerDto.getNGestehungspreis());
		artikellager.setNLagerstand(artikellagerDto.getNLagerstand());
		artikellager.setMandantCNr(artikellagerDto.getMandantCNr());
		em.merge(artikellager);
		em.flush();
	}

	public ArtikellagerDto[] getAllArtikellager(Integer artikelIId)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("ArtikellagerfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleArtikellagerDtos(cl);
	}

	private ArtikellagerDto assembleArtikellagerDto(Artikellager artikellager) {
		return ArtikellagerDtoAssembler.createDto(artikellager);
	}

	private ArtikellagerDto[] assembleArtikellagerDtos(
			Collection<?> artikellagers) {
		List<ArtikellagerDto> list = new ArrayList<ArtikellagerDto>();
		if (artikellagers != null) {
			Iterator<?> iterator = artikellagers.iterator();
			while (iterator.hasNext()) {
				Artikellager artikellager = (Artikellager) iterator.next();
				list.add(assembleArtikellagerDto(artikellager));
			}
		}
		ArtikellagerDto[] returnArray = new ArtikellagerDto[list.size()];
		return (ArtikellagerDto[]) list.toArray(returnArray);
	}

	public String getAllSerienChargennrAufLagerInfo(Integer artikelIId,
			Integer lagerIId, TheClientDto theClientDto) throws EJBExceptionLP {

		SeriennrChargennrAufLagerDto[] dtos = getAllSerienChargennrAufLagerInfoDtos(
				artikelIId, lagerIId, true, null, theClientDto);

		String s = "";

		for (int i = 0; i < dtos.length; i++) {
			s += dtos[i].getCSeriennrChargennr();
			s += "\t";
			s += dtos[i].getNMenge().toString();
			s += "\n\r";
		}

		return s;

	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtosMitBereitsVerbrauchten(
			Integer artikelIId, Integer lagerIId,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String s = "SELECT DISTINCT (C_SERIENNRCHARGENNR) FROM WW_LAGERBEWEGUNG WHERE B_HISTORIE=0 AND ARTIKEL_I_ID="
				+ artikelIId;

		if (lagerIId != null) {
			s += " AND LAGER_I_ID=" + lagerIId;
		}
		if (tStichtag != null) {
			s += " AND T_BELEGDATUM <'"
					+ Helper.formatTimestampWithSlashes(tStichtag) + "' ";
		}

		s += " ORDER BY C_SERIENNRCHARGENNR ";

		org.hibernate.Query query = session.createSQLQuery(s);

		Collection<?> cl = query.list();
		Iterator it = cl.iterator();

		ArrayList alDtos = new ArrayList();
		while (it.hasNext()) {
			String o = (String) it.next();

			SeriennrChargennrAufLagerDto dto = new SeriennrChargennrAufLagerDto();
			dto.setCSeriennrChargennr(o);
			dto.setNMenge(new BigDecimal(0));
			alDtos.add(dto);
		}

		Collections.sort(alDtos, new ComparatorSnrChnrNumerisch(
				bSortiertNachSerienChargennummer));

		SeriennrChargennrAufLagerDto[] temp = new SeriennrChargennrAufLagerDto[cl
				.size()];

		return (SeriennrChargennrAufLagerDto[]) alDtos.toArray(temp);

	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {
		return getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
				null, bSortiertNachSerienChargennummer, tStichtag, theClientDto);
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(
			Integer artikelIId, Integer lagerIId, String cSeriennrChargennr,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String s = "SELECT COALESCE(C_SERIENNRCHARGENNR,'') AS C_SERIENNUMMERCHARGENNR, "
				+ " SUM(CASE WHEN B_ABGANG=1 THEN -N_MENGE ELSE N_MENGE END) AS N_MENGE, "
				+ " MIN(T_BELEGDATUM) AS T_BELEGDATUMMIN, (SELECT B_SERIENNRTRAGEND FROM WW_ARTIKEL WHERE I_ID="
				+ artikelIId
				+ ") AS B_SERIENNRTRAGEND,  COALESCE(C_VERSION,'') AS C_VERSION "
				+ " FROM WW_LAGERBEWEGUNG WHERE B_HISTORIE=0 AND ARTIKEL_I_ID="
				+ artikelIId;

		if (lagerIId != null) {
			s += " AND LAGER_I_ID=" + lagerIId;
		}
		if (cSeriennrChargennr != null) {
			s += " AND C_SERIENNRCHARGENNR='" + cSeriennrChargennr + "'";
		}
		if (tStichtag != null) {
			s += " AND T_BELEGDATUM <'"
					+ Helper.formatTimestampWithSlashes(tStichtag) + "' ";
		}

		s += " GROUP BY C_SERIENNRCHARGENNR, C_VERSION "
				+ " HAVING SUM(CASE WHEN B_ABGANG=1 THEN -N_MENGE ELSE N_MENGE END) <> 0 ";

		org.hibernate.Query query = session.createSQLQuery(s)
				.addScalar("C_SERIENNUMMERCHARGENNR", Hibernate.STRING)
				.addScalar("N_MENGE", Hibernate.BIG_DECIMAL)
				.addScalar("T_BELEGDATUMMIN", Hibernate.TIMESTAMP)
				.addScalar("B_SERIENNRTRAGEND", Hibernate.SHORT)
				.addScalar("C_VERSION", Hibernate.STRING);

		Collection<?> cl = query.list();
		Iterator it = cl.iterator();

		ArrayList alDtos = new ArrayList();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();

			SeriennrChargennrAufLagerDto dto = new SeriennrChargennrAufLagerDto();
			dto.setCSeriennrChargennr((String) o[0]);
			dto.setNMenge((BigDecimal) o[1]);
			dto.setTBuchungszeit((Timestamp) o[2]);
			dto.setBSeriennr((Short) o[3]);
			dto.setCVersion((String) o[4]);

			alDtos.add(dto);

		}

		Collections.sort(alDtos, new ComparatorSnrChnrNumerisch(
				bSortiertNachSerienChargennummer));

		SeriennrChargennrAufLagerDto[] temp = new SeriennrChargennrAufLagerDto[cl
				.size()];

		return (SeriennrChargennrAufLagerDto[]) alDtos.toArray(temp);

	}

	/**
	 * @deprecated use getAllSerienChargennrAufLagerInfoDtos() instead
	 */

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto,
			Boolean bNurChargennummern,
			boolean bSortiertNachSerienChargennummer,
			java.sql.Timestamp tStichtag) throws EJBExceptionLP {
		if (artikelIId == null || bNurChargennummern == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelIId == null || bNurChargennummern == null"));
		}
		java.util.TreeMap list = new java.util.TreeMap();
		FLRLagerbewegung lagerbewegungen = new FLRLagerbewegung();
		lagerbewegungen
				.setB_vollstaendigverbraucht(Helper.boolean2Short(false));
		lagerbewegungen.setB_abgang(Helper.boolean2Short(false));

		FLRArtikel artikel = new FLRArtikel();
		artikel.setI_id(artikelIId);
		if (bNurChargennummern == true) {
			artikel.setB_chargennrtragend(Helper.boolean2Short(true));
		}
		lagerbewegungen.setFlrartikel(artikel);

		Session session = null;
		session = FLRSessionFactory.getFactory().openSession();
		Criteria snrs = session
				.createCriteria(FLRLagerbewegung.class)
				.add(Expression.eq("this."
						+ LagerFac.FLR_LAGERBEWEGUNG_B_VOLLSTAENDIGVERBRAUCHT,
						Helper.boolean2Short(false)))
				.add(Expression.eq("this."
						+ LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
						Helper.boolean2Short(false)))
				.add(Expression.isNotNull("this."
						+ LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR));

		snrs.add(Example.create(lagerbewegungen));
		snrs.createAlias("flrlager", "l");

		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			snrs.add(Restrictions.eq("l.mandant_c_nr",
					theClientDto.getMandant()));
		} else {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)) {
				snrs.add(Restrictions.eq("l.mandant_c_nr",
						theClientDto.getMandant()));
			}
		}

		if (lagerIId != null) {
			snrs.add(Restrictions.eq("l.i_id", lagerIId));
		}

		snrs.createCriteria(LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL).add(
				Expression.eq("i_id", artikelIId));

		if (tStichtag != null) {
			snrs.add(Expression.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT,
					tStichtag));
		}

		snrs.addOrder(Order.asc(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG));
		snrs.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

		List<?> listSnr = snrs.list();

		Iterator<?> listSnrIterator = listSnr.iterator();

		Integer lagerIIdKeinlager = lagerFindByCNrByMandantCNr(
				LagerFac.LAGER_KEINLAGER, theClientDto.getMandant()).getIId();
		int lastColumn = -1;
		String letzteChargennummer = null;
		while (listSnrIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) listSnrIterator
					.next();
			if (lastColumn == lagerbewegung.getI_id_buchung().intValue()
					|| lagerbewegung.getC_seriennrchargennr().equals(
							letzteChargennummer)) {
			} else {
				if (!lagerbewegung.getFlrlager().getI_id()
						.equals(lagerIIdKeinlager)) {
					SeriennrChargennrAufLagerDto dto = new SeriennrChargennrAufLagerDto();

					BigDecimal menge = getMengeAufLager(lagerbewegung
							.getFlrartikel().getI_id(), lagerbewegung
							.getFlrlager().getI_id(),
							lagerbewegung.getC_seriennrchargennr(),
							theClientDto);

					if (menge.doubleValue() != 0) {

						if (Helper.short2boolean(lagerbewegung.getFlrartikel()
								.getB_chargennrtragend()) == true) {
							dto.setBSeriennr(Helper.boolean2Short(false));
							dto.setNMenge(Helper.rundeKaufmaennisch(menge, 2));
							dto.setCSeriennrChargennr(lagerbewegung
									.getC_seriennrchargennr());
						} else if (Helper.short2boolean(lagerbewegung
								.getFlrartikel().getB_seriennrtragend()) == true) {

							dto.setBSeriennr(Helper.boolean2Short(true));
							dto.setNMenge(new BigDecimal(1));
							dto.setCSeriennrChargennr(lagerbewegung
									.getC_seriennrchargennr());
						}
						dto.setTBuchungszeit(new java.sql.Timestamp(
								lagerbewegung.getT_buchungszeit().getTime()));
						if (bSortiertNachSerienChargennummer) {
							list.put(lagerbewegung.getC_seriennrchargennr(),
									dto);
						} else {
							list.put(dto.getTBuchungszeit(), dto);
						}
					}
				}
				lastColumn = lagerbewegung.getI_id_buchung().intValue();
				letzteChargennummer = lagerbewegung.getC_seriennrchargennr();
			}
		}
		session.close();
		SeriennrChargennrAufLagerDto[] returnArray = new SeriennrChargennrAufLagerDto[list
				.size()];

		Iterator<?> it = list.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {

			if (bSortiertNachSerienChargennummer) {
				String s = (String) it.next();
				SeriennrChargennrAufLagerDto dto = (SeriennrChargennrAufLagerDto) list
						.get(s);
				returnArray[i] = dto;

			} else {
				Timestamp ts = (Timestamp) it.next();
				SeriennrChargennrAufLagerDto dto = (SeriennrChargennrAufLagerDto) list
						.get(ts);
				returnArray[i] = dto;

			}

			i++;
		}
		return returnArray;

	}

	public void updateTBelegdatumEinesBelegesImLager(String belegartCNr,
			Integer belegartIId, java.sql.Timestamp tBelegdatumNeu,
			TheClientDto theClientDto) {

		if (belegartCNr == null || belegartIId == null
				|| tBelegdatumNeu == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartIId == null || tBelegdatumNeu == null"));
		}

		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrIBelegartid");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartIId);
		Collection<?> cl = query.getResultList();
		// if (!cl.isEmpty()) {
		LagerbewegungDto[] lagerbewegungDtos = assembleLagerbewegungDtos(cl);
		for (int i = 0; i < lagerbewegungDtos.length; i++) {
			lagerbewegungDtos[i].setTBelegdatum(tBelegdatumNeu);
		}
		updateLagerbewegungs(lagerbewegungDtos, theClientDto);
	}

	public BigDecimal getGemittelterEinstandspreisAllerLagerndenArtikel(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l.i_id_buchung,l.n_einstandspreis, l.n_menge,(SELECT SUM(u.n_verbrauchtemenge) FROM FLRLagerabgangursprung as u WHERE u.compId.i_lagerbewegungidursprung=l.i_id_buchung) as verbrauchtemenge  FROM FLRLagerbewegung as l"
				+ " WHERE l.artikel_i_id =" + artikelIId + " AND l.b_abgang=0 ";
		if (lagerIId != null) {
			queryString += " AND l.flrlager.i_id =" + lagerIId + " ";
		}
		queryString += " ORDER BY l.i_id_buchung ASC,l.t_buchungszeit DESC ";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal bdWert = new BigDecimal(0);
		BigDecimal bdMenge = new BigDecimal(0);

		int lastColumn = -1;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			if (lastColumn == ((Integer) o[0]).intValue()) {
			} else {
				bdMenge = bdMenge.add((BigDecimal) o[2]);
				bdWert = bdWert.add(((BigDecimal) o[1])
						.multiply((BigDecimal) o[2]));
			}
			lastColumn = ((Integer) o[0]).intValue();
		}

		if (bdMenge.doubleValue() != 0) {
			return bdWert.divide(bdMenge, 4, BigDecimal.ROUND_HALF_EVEN);
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * @deprecated use getAllSerienChargennrAufLagerInfoDtos() instead
	 */

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto,
			Boolean bNurChargennummern, boolean bSortiertNachSerienChargennummer)
			throws EJBExceptionLP {
		return getAllSerienChargennrAufLager(artikelIId, lagerIId,
				theClientDto, bNurChargennummern,
				bSortiertNachSerienChargennummer, null);
	}

	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
			String belegartCNr, Integer belegartpositionIId) {

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class);
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
				belegartCNr));
		crit.add(Restrictions.eq(
				LagerFac.FLR_LAGERBEWEGUNG_I_BELEGARTPOSITIONID,
				belegartpositionIId));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));
		crit.addOrder(Order.asc(LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR));

		List<?> resultList = crit.list();

		ArrayList<SeriennrChargennrMitMengeDto> list = new ArrayList<SeriennrChargennrMitMengeDto>();

		Iterator<?> iteratorLB = resultList.iterator();
		if (resultList.size() > 0) {
			while (iteratorLB.hasNext()) {
				FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) iteratorLB
						.next();

				SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto();
				dto.setCSeriennrChargennr(lagerbewegung
						.getC_seriennrchargennr());
				dto.setNMenge(lagerbewegung.getN_menge());
				dto.setCVersion(lagerbewegung.getC_version());

				// 18452
				if (Helper.short2boolean(lagerbewegung.getFlrartikel()
						.getB_chargennrtragend())
						&& Helper.short2boolean(lagerbewegung.getB_abgang()) == false) {

					try {
						dto.setPaneldatenDtos(getPanelFac()
								.paneldatenFindByPanelCNrCKey(
										PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
										lagerbewegung.getI_id_buchung() + ""));
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

				list.add(dto);
			}
		}

		return list;
	}

	/**
	 * Lesen aller in der DB vorhandenen ArtikelArten.
	 * 
	 * @param spracheCNr
	 *            Sprache
	 * @return Map Inhalt
	 * @throws EJBExceptionLP
	 */
	public Map getAllSprLagerArten(String spracheCNr) throws EJBExceptionLP {
		myLogger.entry();
		TreeMap<Object, Object> treemap = new TreeMap<Object, Object>();

		Query query = em.createNamedQuery("LagerartfindAll");
		Collection<?> allArten = query.getResultList();
		Iterator<?> iter = allArten.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Lagerart lagerartTemp = (Lagerart) iter.next();

			Object key = lagerartTemp.getCNr();

			if (!lagerartTemp.getCNr().equals(LagerFac.LAGERART_WERTGUTSCHRIFT)) {

				Object value = null;
				Lagerartspr lagerartspr = (em.find(Lagerartspr.class,
						new LagerartsprPK((String) key, spracheCNr)));

				if (lagerartspr != null) {
					value = lagerartspr.getCBez();
				} else {
					value = lagerartTemp.getCNr();
				}
				treemap.put(key, value);
			}
		}

		return treemap;
	}

	/**
	 * Lesen aller Lager eines Mandanten.
	 * 
	 * @param theClientDto
	 *            User-ID
	 * @return Map Inhalt
	 * @throws EJBExceptionLP
	 */
	public Map getAllLager(TheClientDto theClientDto) throws EJBExceptionLP {
		String mandantCNr = theClientDto.getMandant();

		TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();

		Query query = em.createNamedQuery("LagerfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> allLager = query.getResultList();

		Iterator<?> iter = allLager.iterator();
		while (iter.hasNext()) {
			Lager lagerTemp = (Lager) iter.next();
			if (!lagerTemp.getCNr().equals(LagerFac.LAGER_KEINLAGER)) {

				if (!lagerTemp.getLagerartCNr().equals(
						LagerFac.LAGER_WERTGUTSCHRIFT)) {

					treemap.put(lagerTemp.getIId(), lagerTemp.getCNr());
				}
			}
		}
		return treemap;
	}

	/**
	 * Liefert aktuellen Gestehungspreis eines Artikels in einem Lager zurueck.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param theClientDto
	 *            String
	 * @return BigDecimal Gestehungspreis
	 * @throws EJBExceptionLP
	 *             artikelIId == null || lagerIId == null
	 */
	public BigDecimal getGemittelterGestehungspreisEinesLagers(
			Integer artikelIId, Integer lagerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				artikelIId, theClientDto);

		return getGemittelterGestehungspreisEinesLagers(artikelIId,
				artikelDto.getBLagerbewirtschaftet(),
				artikelDto.getArtikelartCNr(), lagerIId, theClientDto);
	}

	public BigDecimal getGemittelterGestehungspreisEinesLagers(
			Integer artikelIId, Short bLagerbewirtschaftet,
			String artikelartCNr, Integer lagerIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// Wenn Arbeitszeitartikel, dann wird der Einkaufspreis des
		// 1.Lieferanten verwendet
		try {
			// Wenn nicht lagerbewirtschaftet, dann auf KEIN_LAGER umstellen.
			if (!Helper.short2boolean(bLagerbewirtschaftet)) {
				lagerIId = lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER,
						theClientDto.getMandant()).getIId();
			}

			if (artikelartCNr.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				ArtikellieferantDto dto = getArtikelFac()
						.getArtikelEinkaufspreis(artikelIId, new BigDecimal(1),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);
				if (dto != null) {
					if (dto.getNNettopreis() != null) {
						return dto.getNNettopreis();
					} else {
						return new BigDecimal(0);
					}
				} else {
					return new BigDecimal(0);
				}
			}
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		ArtikellagerPK artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikelIId);
		artikellagerPK.setLagerIId(lagerIId);
		Artikellager artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager == null) {
			return new BigDecimal(0);
		}
		return artikellager.getNGestehungspreis();
	}

	/**
	 * Liefert aktuellen Gestehungspreis eines Artikels (gemittelt ueber alle
	 * Laeger).
	 * 
	 * @param artikelIId
	 *            4711
	 * @param theClientDto
	 *            User-ID
	 * @return BigDecimal Gestehungspreis
	 * @throws EJBExceptionLP
	 *             artikelIId == null
	 */
	public BigDecimal getGemittelterGestehungspreisAllerLaegerEinesMandanten(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// Wenn Arbeitszeitartikel, dann wird der Einkaufspreis des
		// 1.Lieferanten verwendet
		try {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(artikelIId, theClientDto);
			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				ArtikellieferantDto dto = getArtikelFac()
						.getArtikelEinkaufspreis(artikelIId, new BigDecimal(1),
								theClientDto.getSMandantenwaehrung(),
								theClientDto);
				if (dto != null) {
					if (dto.getLief1Preis() != null) {
						return dto.getLief1Preis();
					} else {
						return new BigDecimal(0);
					}
				} else {
					return new BigDecimal(0);
				}
			}
		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		BigDecimal bdErgebnis = new BigDecimal(0);
		BigDecimal bdGesamtLagerstand = new BigDecimal(0);
		BigDecimal bdGesamtGestehungspreis = new BigDecimal(0);
		Query query = em.createNamedQuery("ArtikellagerfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		ArtikellagerDto[] dtos = assembleArtikellagerDtos(cl);
		for (int i = 0; i < dtos.length; i++) {

			ArtikellagerDto dto = dtos[i];
			Lager lager = em.find(Lager.class, dto.getLagerIId());
			if (lager == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (!lager.getLagerartCNr()
					.equals(LagerFac.LAGERART_WERTGUTSCHRIFT)) {
				if (lager.getMandantCNr().equals(theClientDto.getMandant())) {
					// Gestehungspreise berechnen
					bdGesamtLagerstand = bdGesamtLagerstand.add(dto
							.getNLagerstand());
					bdGesamtGestehungspreis = bdGesamtGestehungspreis.add(dto
							.getNLagerstand().multiply(
									dto.getNGestehungspreis()));
				}
			}

		}
		if (bdGesamtLagerstand.doubleValue() != 0) {
			bdErgebnis = bdGesamtGestehungspreis.divide(bdGesamtLagerstand,
					BigDecimal.ROUND_HALF_EVEN);
		}
		return bdErgebnis;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getLagerstandAllerLagerAllerMandanten(Integer artikelIId,
			boolean bMitKonsignationslager, TheClientDto theClientDto) {

		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}

		LagerDto[] lagerDtos = lagerFindAll();
		BigDecimal lagerstand = new BigDecimal(0);

		for (int i = 0; i < lagerDtos.length; i++) {
			LagerDto lagerDto = lagerDtos[i];

			if (bMitKonsignationslager == false
					&& Helper.short2boolean(lagerDto.getBKonsignationslager()) == true) {
			} else {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerDto.getIId()));
				if (artikellager != null) {
					lagerstand = lagerstand.add(artikellager.getNLagerstand());
				}
			}

		}
		return lagerstand;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId,
			TheClientDto theClientDto) {
		return getLagerstandAllerLagerEinesMandanten(artikelIId, true,
				theClientDto);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HashMap<String, BigDecimal> getLagerstaendeAllerLagerartenOhneKeinLager(
			Integer artikelIId, TheClientDto theClientDto) {
		HashMap<String, BigDecimal> hmLagerstaende = new HashMap<String, BigDecimal>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT al.flrlager.lagerart_c_nr,sum(al.n_lagerstand) FROM FLRArtikellager as al WHERE al.flrlager.c_nr <>'"
				+ LagerFac.LAGER_KEINLAGER
				+ "'  AND al.flrartikel.i_id ="
				+ artikelIId + "";

		queryString += " GROUP BY al.flrlager.lagerart_c_nr ORDER BY al.flrlager.lagerart_c_nr ASC ";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			BigDecimal lagerstand = (BigDecimal) o[1];
			if (lagerstand == null) {
				lagerstand = new BigDecimal(0);
			}
			hmLagerstaende.put((String) o[0], lagerstand);
		}
		session.close();

		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_KUNDENLAGER)) {
			hmLagerstaende
					.put(LagerFac.LAGERART_KUNDENLAGER, new BigDecimal(0));
		}

		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_HAUPTLAGER)) {
			hmLagerstaende.put(LagerFac.LAGERART_HAUPTLAGER, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_NORMAL)) {
			hmLagerstaende.put(LagerFac.LAGERART_NORMAL, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_PERSOENLICH)) {
			hmLagerstaende
					.put(LagerFac.LAGERART_PERSOENLICH, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_SCHROTT)) {
			hmLagerstaende.put(LagerFac.LAGERART_SCHROTT, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_SPERRLAGER)) {
			hmLagerstaende.put(LagerFac.LAGERART_SPERRLAGER, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_WERTGUTSCHRIFT)) {
			hmLagerstaende.put(LagerFac.LAGERART_WERTGUTSCHRIFT,
					new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_ZOLLLAGER)) {
			hmLagerstaende.put(LagerFac.LAGERART_ZOLLLAGER, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_LIEFERANT)) {
			hmLagerstaende.put(LagerFac.LAGERART_LIEFERANT, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_HALBFERTIG)) {
			hmLagerstaende.put(LagerFac.LAGERART_HALBFERTIG, new BigDecimal(0));
		}
		if (!hmLagerstaende.containsKey(LagerFac.LAGERART_WARENEINGANG)) {
			hmLagerstaende.put(LagerFac.LAGERART_WARENEINGANG,
					new BigDecimal(0));
		}

		return hmLagerstaende;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId,
			boolean bMitKonsignationslager, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}

		LagerDto[] lagerDtos = lagerFindByMandantCNr(theClientDto.getMandant());
		BigDecimal lagerstand = new BigDecimal(0);

		for (int i = 0; i < lagerDtos.length; i++) {
			LagerDto lagerDto = lagerDtos[i];

			if (bMitKonsignationslager == false
					&& Helper.short2boolean(lagerDto.getBKonsignationslager()) == true) {
			} else {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerDto.getIId()));
				if (artikellager != null) {
					lagerstand = lagerstand.add(artikellager.getNLagerstand());
				}
			}

		}
		return lagerstand;

	}

	public BigDecimal getLagerstandAllerSperrlaegerEinesMandanten(
			Integer artikelIId, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}

		LagerDto[] lagerDtos = lagerFindByMandantCNr(theClientDto.getMandant());
		BigDecimal lagerstand = new BigDecimal(0);

		for (int i = 0; i < lagerDtos.length; i++) {
			LagerDto lagerDto = lagerDtos[i];

			if (lagerDto.getLagerartCNr().equals(LagerFac.LAGERART_SPERRLAGER)) {

				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerDto.getIId()));
				if (artikellager != null) {
					lagerstand = lagerstand.add(artikellager.getNLagerstand());
				}
			}
		}
		return lagerstand;

	}

	public BigDecimal getGestehungspreisZumZeitpunkt(Integer artikelIId,
			Integer lagerIId, java.sql.Timestamp tsZeitpunkt,
			TheClientDto theClientDto) {
		if (artikelIId == null || tsZeitpunkt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || tsZeitpunkt == null"));
		}

		ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				artikelIId, theClientDto);

		// Wenn nicht lagerbewirtschaftet, dann auf KEIN_LAGER umstellen.
		if (!Helper.short2boolean(aDto.getBLagerbewirtschaftet())) {
			lagerIId = lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER,
					theClientDto.getMandant()).getIId();
		}

		if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			ArtikellieferantDto dto = getArtikelFac().getArtikelEinkaufspreis(
					artikelIId, null, new BigDecimal(1),
					theClientDto.getSMandantenwaehrung(),
					new java.sql.Date(tsZeitpunkt.getTime()), theClientDto);
			if (dto != null) {
				if (dto.getNNettopreis() != null) {
					return dto.getNNettopreis();
				} else {
					return new BigDecimal(0);
				}
			} else {
				return new BigDecimal(0);
			}
		}

		BigDecimal gesamtwert = null;
		BigDecimal gesamtlagerstand = new BigDecimal(0.0000);

		LagerDto[] lagerDtos = new LagerDto[1];
		if (lagerIId != null) {
			LagerDto lagerDto = new LagerDto();
			lagerDto.setIId(lagerIId);
			lagerDtos[0] = lagerDto;
		} else {
			lagerDtos = lagerFindByMandantCNr(theClientDto.getMandant());
		}

		for (int i = 0; i < lagerDtos.length; i++) {

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			session = factory.openSession();

			org.hibernate.Criteria crit = session
					.createCriteria(FLRLagerbewegung.class);
			crit.setMaxResults(1);

			crit.add(
					Expression.le(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
							tsZeitpunkt))
					.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRARTIKEL, "a")
					.add(Expression.eq("a.i_id", artikelIId))
					.add(Expression.eq("b_historie",
							Helper.boolean2Short(false)));
			crit.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRLAGER, "l");

			crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
					Helper.boolean2Short(false)));

			crit.add(Expression.eq("l.i_id", lagerDtos[i].getIId()));

			crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM))
					.addOrder(
							Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();

			if (resultListIterator.hasNext()) {
				if (gesamtwert == null) {
					gesamtwert = new BigDecimal(0.0000);
				}
				FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
						.next();

				// Wenns der Gestehungspreis eines bestimmten Lagers ist, dann
				// kann diesel sofort zurueckgegeben werden
				if (lagerIId != null) {
					return lagerbewegung.getN_gestehungspreis();
				}

				BigDecimal lpZp = getLagerstandZumZeitpunkt(artikelIId,
						lagerDtos[i].getIId(), tsZeitpunkt, theClientDto);

				gesamtlagerstand = gesamtlagerstand.add(lpZp);
				gesamtwert = gesamtwert.add(lpZp.multiply(lagerbewegung
						.getN_gestehungspreis()));
			}

			session.close();
		}
		if (gesamtlagerstand.doubleValue() != 0) {
			return gesamtwert.divide(gesamtlagerstand, 4,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			if (gesamtwert == null) {
				return null;
			} else {
				return new BigDecimal(0.0000);
			}
		}
	}

	/**
	 * Liefert den Lagerstand zu einem best. Datum
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lagerIId
	 *            Integer
	 * @param tsZeitpunkt
	 *            Timestamp
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 */
	public BigDecimal getLagerstandZumZeitpunkt(Integer artikelIId,
			Integer lagerIId, java.sql.Timestamp tsZeitpunkt,
			TheClientDto theClientDto) {

		if (artikelIId == null || tsZeitpunkt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null ||tsZeitpunkt == null"));
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String sQuery = "SELECT sum(l.n_menge) FROM FLRLagerbewegung l WHERE l.b_historie=0 AND l.flrlager.c_nr NOT IN('"
				+ LagerFac.LAGER_KEINLAGER
				+ "') AND l.flrlager.lagerart_c_nr NOT IN ('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')  AND l.artikel_i_id="
				+ artikelIId
				+ " AND l.t_belegdatum <'"
				+ Helper.formatTimestampWithSlashes(tsZeitpunkt) + "'";

		if (lagerIId != null) {
			sQuery += " AND l.lager_i_id=" + lagerIId;
		}

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery
				+ "AND l.b_abgang=0");
		List<?> result = querylagerbewegungen.list();
		BigDecimal zugaenge = new BigDecimal(0);

		if (result.iterator().hasNext()) {
			BigDecimal bd = (BigDecimal) result.iterator().next();
			if (bd != null) {
				zugaenge = bd;
			}
		}
		session.close();
		session = factory.openSession();
		querylagerbewegungen = session.createQuery(sQuery + "AND l.b_abgang=1");
		result = querylagerbewegungen.list();
		BigDecimal abgaenge = new BigDecimal(0);

		if (result.iterator().hasNext()) {
			BigDecimal bd = (BigDecimal) result.iterator().next();
			if (bd != null) {
				abgaenge = bd;
			}
		}

		session.close();

		return zugaenge.subtract(abgaenge);
	}

	public HashMap<Integer, BigDecimal> getLagerstandAllerArtikelZumZeitpunkt(
			Integer lagerIId, java.sql.Timestamp tsZeitpunkt,
			TheClientDto theClientDto) {

		HashMap<Integer, BigDecimal> hmLagerstaendeZumZeitpunkt = new HashMap<Integer, BigDecimal>();

		if (tsZeitpunkt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tsZeitpunkt == null"));
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String sQuery = "SELECT sum(l.n_menge),l.artikel_i_id FROM FLRLagerbewegung l WHERE l.b_historie=0 AND l.flrlager.c_nr NOT IN('"
				+ LagerFac.LAGER_KEINLAGER
				+ "') AND l.flrlager.lagerart_c_nr NOT IN ('"
				+ LagerFac.LAGERART_WERTGUTSCHRIFT
				+ "')  AND l.t_belegdatum <'"
				+ Helper.formatTimestampWithSlashes(tsZeitpunkt)
				+ "' AND l.flrlager.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ";

		if (lagerIId != null) {
			sQuery += " AND l.lager_i_id=" + lagerIId;
		}

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery
				+ "AND l.b_abgang=0 GROUP BY l.artikel_i_id");
		List<?> result = querylagerbewegungen.list();

		Object[] o = result.toArray();
		for (int i = 0; i < o.length; i++) {
			Object[] oZeile = (Object[]) o[i];
			BigDecimal bd = (BigDecimal) oZeile[0];
			Integer artikelIId = (Integer) oZeile[1];

			hmLagerstaendeZumZeitpunkt.put(artikelIId, bd);

		}

		session.close();
		session = factory.openSession();
		querylagerbewegungen = session.createQuery(sQuery
				+ "AND l.b_abgang=1 GROUP BY l.artikel_i_id");
		result = querylagerbewegungen.list();
		o = result.toArray();

		o = result.toArray();
		for (int i = 0; i < o.length; i++) {
			Object[] oZeile = (Object[]) o[i];
			BigDecimal bdAbgaenge = (BigDecimal) oZeile[0];
			Integer artikelIId = (Integer) oZeile[1];

			BigDecimal lagerstand = new BigDecimal(0);

			if (hmLagerstaendeZumZeitpunkt.containsKey(artikelIId)) {
				BigDecimal zugaenge = hmLagerstaendeZumZeitpunkt
						.get(artikelIId);
				lagerstand = zugaenge.subtract(bdAbgaenge);
			}
			hmLagerstaendeZumZeitpunkt.put(artikelIId, lagerstand);

		}

		session.close();

		return hmLagerstaendeZumZeitpunkt;
	}

	/**
	 * Nur fuer interne Zwecke verwenden
	 * 
	 * @param artikelIId
	 * @param lagerIId
	 * @param tsZeitpunkt
	 * @param theClientDto
	 * @return der Lagerstand
	 */

	private BigDecimal getLagerstandVorZeitpunktBelegdatum(Integer artikelIId,
			Integer lagerIId, java.sql.Timestamp tsZeitpunkt,
			TheClientDto theClientDto) {

		if (artikelIId == null || tsZeitpunkt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null ||tsZeitpunkt == null"));
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		String sql = "select sum(N_MENGE) from ( "
				+ "select SUM(N_MENGE) as N_MENGE from WW_LAGERBEWEGUNG "
				+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 and T_BELEGDATUM < ? "
				+ "union "
				+ "select -SUM(U.N_VERBRAUCHTEMENGE) as N_MENGE from WW_LAGERBEWEGUNG B "
				+ "inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
				+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 and B.T_BELEGDATUM < ?) as FOO;";
		org.hibernate.Query query = session.createSQLQuery(sql);
		query.setParameter(0, lagerIId);
		query.setParameter(1, artikelIId);
		query.setParameter(2, tsZeitpunkt);
		query.setParameter(3, lagerIId);
		query.setParameter(4, artikelIId);
		query.setParameter(5, tsZeitpunkt);
		List<?> list = query.list();
		BigDecimal lagerstand = new BigDecimal(0);
		if (list.size() > 0 && list.get(0) != null) {
			lagerstand = (BigDecimal) list.get(0);
		}
		return lagerstand;
	}

	private BigDecimal getGestehungspreisVorZeitpunktBelegdatum(
			Integer artikelIId, Integer lagerIId,
			java.sql.Timestamp tsZeitpunkt, TheClientDto theClientDto) {
		if (artikelIId == null || tsZeitpunkt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || tsZeitpunkt == null"));
		}

		String sql = "select N_GESTEHUNGSPREIS "
				+ "from WW_LAGERBEWEGUNG "
				+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 and T_BELEGDATUM < ? "
				+ "order by T_BELEGDATUM desc, T_BUCHUNGSZEIT desc";

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		org.hibernate.Query query = session.createSQLQuery(sql);
		query.setParameter(0, lagerIId);
		query.setParameter(1, artikelIId);
		query.setParameter(2, tsZeitpunkt);
		query.setMaxResults(1);
		List<?> list = query.list();

		BigDecimal gestpreis = new BigDecimal(0);
		if (list.size() > 0 && list.get(0) != null) {
			gestpreis = (BigDecimal) list.get(0);
		}
		session.close();
		return gestpreis;
	}

	public LagerDto getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(
			Integer artikelIId) {

		// fuer PJ 17921
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(al.flrlager.i_id) FROM FLRArtikellager as al WHERE al.flrlager.c_nr <>'"
				+ LagerFac.LAGER_KEINLAGER
				+ "' AND  al.flrlager.lagerart_c_nr IN ('"
				+ LagerFac.LAGERART_HAUPTLAGER
				+ "','"
				+ LagerFac.LAGERART_NORMAL
				+ "','"
				+ LagerFac.LAGERART_HALBFERTIG
				+ "') AND al.n_lagerstand > 0 AND al.flrartikel.i_id="
				+ artikelIId;

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		if (results.size() == 1) {
			Integer lagerIId = (Integer) resultListIterator.next();
			return lagerFindByPrimaryKey(lagerIId);
		} else {
			return null;
		}

	}

	/**
	 * Liefert die Mengenmaessige Lagerstandsveraenderung in einem Zeitraum
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lagerIId
	 *            Integer
	 * @param tVon
	 *            Timestamp
	 * @param tBis
	 *            Timestamp
	 * @param theClientDto
	 *            String
	 * @return BigDecimal
	 */
	public BigDecimal getLagerstandsVeraenderungOhneInventurbuchungen(
			Integer artikelIId, Integer lagerIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, String cSnrChnr, TheClientDto theClientDto) {

		if (artikelIId == null || lagerIId == null || tVon == null
				|| tBis == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || lagerIId  ==  null || tsZeitpunkt == null || tBis == null"));
		}

		boolean bVonNachBis = false;
		if (tVon.after(tBis)) {
			bVonNachBis = true;

			java.sql.Timestamp tHelp = tVon;
			tVon = tBis;
			tBis = tHelp;

		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();

		String[] s = new String[1];
		s[0] = LocaleFac.BELEGART_INVENTUR;

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class)
				.add(Expression.gt(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
						tVon))
				.add(Expression.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
						tBis))
				.add(Restrictions.not(Expression.in(
						LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, s)))
				.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRARTIKEL, "a")
				.add(Expression.eq("a.i_id", artikelIId))
				.createAlias(LagerFac.FLR_LAGERPLAETZE_FLRLAGER, "l")
				.add(Expression.eq("l.i_id", lagerIId))
				.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
						Helper.boolean2Short(false)))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG))
				.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

		if (cSnrChnr != null) {
			crit.add(Expression.eq(
					LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR, cSnrChnr));
		}

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		BigDecimal lagerstandsVeraenderung = new BigDecimal(0);

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();

			if (lagerbewegung.getN_menge().doubleValue() > 0) {

				BigDecimal menge = lagerbewegung.getN_menge();
				if (bVonNachBis == true) {
					menge = menge.multiply(new BigDecimal(-1));
				}
				// Wenn Lagerabgang, dann abziehen Menge
				if (Helper.short2boolean(lagerbewegung.getB_abgang())) {
					lagerstandsVeraenderung = lagerstandsVeraenderung
							.subtract(menge);
				} else {
					lagerstandsVeraenderung = lagerstandsVeraenderung
							.add(menge);
				}
			}

		}

		session.close();

		return lagerstandsVeraenderung;
	}

	/**
	 * Gibt den Lagerstand eines Artikels in einem bestimmten Lager zurueck
	 * (Quick-Lagerstand). Hier kann keine Seriennummer/Chargennummer
	 * mitangegeben werden -> Ist immer ein Gesamtlagerstand eines Artikels und
	 * Lagers
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param theClientDto
	 *            User-ID
	 * @return BigDecimal Lagerstand
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check(idUser);
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}
		Artikel artikel = null;

		artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (Helper.short2boolean(artikel.getBLagerbewirtschaftet()) == false) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET,
					new Exception(
							"FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET"));
		}
		try {
			return artikellagerFindByPrimaryKey(artikelIId, lagerIId)
					.getNLagerstand();
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String fehlendeAbbuchungenNachtragen(Timestamp tAb,
			TheClientDto theClientDto) {

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		int iFehler = 0;
		// 1.) Handlagerbewegungen nachbuchen

		String queryString = "SELECT hand FROM FLRHandlagerbewegung hand WHERE hand.b_abgang=1 AND hand.t_buchungszeit>='"
				+ Helper.formatTimestampWithSlashes(tAb) + "'";
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> it = resultList.iterator();
		while (it.hasNext()) {
			FLRHandlagerbewegung hand = (FLRHandlagerbewegung) it.next();

			if (Helper.short2boolean(hand.getB_abgang())) {

				getLagerFac()
						.pruefeQuickLagerstandGegenEchtenLagerstandUndFuehreAus(
								hand.getFlrartikel().getI_id(), theClientDto);

				try {
					getLagerFac().bucheAb(LocaleFac.BELEGART_HAND,
							hand.getI_id(), hand.getI_id(),
							hand.getFlrartikel().getI_id(), hand.getN_menge(),
							hand.getN_verkaufspreis(),
							hand.getFlrlager().getI_id(), (String) null,
							new Timestamp(hand.getT_buchungszeit().getTime()),
							theClientDto);
				} catch (EJBExceptionLP e) {
					iFehler++;
					String zeile = "FEHLER:" + iFehler + ",HANDBUCHUNGS_ID:"
							+ hand.getI_id() + ",ARTIKEL:"
							+ hand.getFlrartikel().getC_nr();
					rueckgabe += zeile + new String(CRLFAscii);
					System.out.println(zeile);
				}
			}

		}

		session.close();

		// Rechnungen

		queryString = "SELECT rp FROM FLRRechnungPosition rp WHERE rp.flrrechnung.t_aendern>='"
				+ Helper.formatTimestampWithSlashes(tAb) + "'";
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(queryString);
		resultList = query.list();
		it = resultList.iterator();
		while (it.hasNext()) {
			FLRRechnungPosition rp = (FLRRechnungPosition) it.next();
			try {
				RechnungDto rDto = getRechnungFac().rechnungFindByPrimaryKey(
						rp.getFlrrechnung().getI_id());
				RechnungPositionDto rePosDto = getRechnungFac()
						.rechnungPositionFindByPrimaryKey(rp.getI_id());

				if (rePosDto.getRechnungpositionartCNr().equals(
						LocaleFac.POSITIONSART_IDENT)) {
					getLagerFac()
							.pruefeQuickLagerstandGegenEchtenLagerstandUndFuehreAus(
									rePosDto.getArtikelIId(), theClientDto);
					getRechnungFac().bucheRechnungPositionAmLager(rePosDto,
							rDto.getLagerIId(), false, theClientDto);

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

		// Lieferschein

		queryString = "SELECT ls FROM FLRLieferschein ls WHERE ls.t_aendern>='"
				+ Helper.formatTimestampWithSlashes(tAb) + "'";
		session = FLRSessionFactory.getFactory().openSession();
		query = session.createQuery(queryString);
		resultList = query.list();
		it = resultList.iterator();
		while (it.hasNext()) {
			FLRLieferschein ls = (FLRLieferschein) it.next();
			try {
				LieferscheinpositionDto[] dtos = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(ls.getI_id());
				for (int i = 0; i < dtos.length; i++) {

					if (dtos[i]
							.getPositionsartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						getLagerFac()
								.pruefeQuickLagerstandGegenEchtenLagerstandUndFuehreAus(
										dtos[i].getArtikelIId(), theClientDto);
						if (dtos[i].getNMenge().doubleValue() != 0) {
							if (dtos[i].getNMenge().doubleValue() > 0) {

								try {
									getLieferscheinpositionFac().bucheAbLager(
											dtos[i], theClientDto);
								} catch (EJBExceptionLP e) {
									iFehler++;

									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													dtos[i].getArtikelIId(),
													theClientDto);

									String zeile = "FEHLER:" + iFehler + ",LS:"
											+ ls.getC_nr() + ",ARTIKEL:"
											+ artikelDto.getCNr() + ",I_SORT:"
											+ dtos[i].getISort();
									rueckgabe += zeile + new String(CRLFAscii);
									System.out.println(zeile);
								}
							} else {
								getLieferscheinpositionFac().bucheZuLager(
										dtos[i], theClientDto);
							}
						}

					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		session.close();

		return rueckgabe;
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.NEVER) public void
	 * versucheFehlerLagerbewegungOhneUrsprungseintragZuKorrigieren(
	 * TheClientDto theClientDto) { Session session =
	 * FLRSessionFactory.getFactory().openSession(); String queryString =
	 * "SELECT distinct bew.artikel_i_id FROM FLRLagerbewegung AS bew WHERE bew.b_historie=0 AND bew.flrartikel.b_lagerbewirtschaftet=1 AND bew.flrartikel.b_seriennrtragend=0 AND bew.flrartikel.b_chargennrtragend=0 "
	 * +
	 * "  GROUP BY bew.c_belegartnr, bew.i_belegartpositionid, bew.lager_i_id, bew.artikel_i_id HAVING count(*) > 1 ORDER BY bew.artikel_i_id "
	 * ;
	 * 
	 * org.hibernate.Query query = session.createQuery(queryString); List<?>
	 * resultList = query.list(); Iterator<?> resultListIteratorFTGruppe =
	 * resultList.iterator(); int i=0; while
	 * (resultListIteratorFTGruppe.hasNext()) { i++; Integer artikelIId =
	 * (Integer) resultListIteratorFTGruppe.next();
	 * 
	 * getLagerFac().ueberzaehligeLagerbewegungenZusammenfuehren( artikelIId);
	 * System.out.println(i+" von "+resultList.size());
	 * 
	 * } session.close();
	 * 
	 * }
	 */

	public int aendereEigenschaftChargengefuehrt(Integer artikelIId,
			boolean bSnrChargennrtragend, TheClientDto theClientDto) {
		// Alle Lagerbewegungen eines Artikels mit der SNR-Chargennummer holen
		int iAnzahl = 0;

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				artikelIId, theClientDto);
		myLogger.entry("Eigenschaft 'Chargennummernbehaftet' des Artikels "
				+ artikelDto.getCNr() + " ge\u00E4ndert durch Personal-ID"
				+ theClientDto.getIDPersonal());

		Session session = FLRSessionFactory.getFactory().openSession();

		String chNR = "NULL";
		if (bSnrChargennrtragend) {
			chNR = "'" + CHARGENNUMMER_KEINE_CHARGE + "'";
		}

		String hqlUpdate = "update FLRLagerbewegung l set l.c_seriennrchargennr="
				+ chNR + " WHERE l.artikel_i_id= " + artikelIId;
		session.createQuery(hqlUpdate).executeUpdate();

		session.close();
		if (bSnrChargennrtragend == false) {
			// SP617 Fuer den Fall, dass es mehrere Chargennumern auf einer
			// Belegposition gegeben hat, muessen diese Zusammengefuert werden

			// SELECT C_BELEGARTNR, I_BELEGARTPOSITIONID FROM WW_LAGERBEWEGUNG
			// WHERE
			// ARTIKEL_I_ID=9821 AND B_HISTORIE=0 AND B_ABGANG=0 GROUP BY
			// C_BELEGARTNR, I_BELEGARTPOSITIONID HAVING COUNT(
			// I_BELEGARTPOSITIONID) >1

			// ZUGAENGE

			ueberzaehligeLagerbewegungenZusammenfuehren(artikelIId);

		}

		return iAnzahl;
	}

	private void ueberzaehligeLagerbewegungenZusammenfuehren(Integer artikelIId) {
		Session sessionLagerbewegung = FLRSessionFactory.getFactory()
				.openSession();

		String queryString = "SELECT bew.c_belegartnr, bew.i_belegartpositionid, bew.lager_i_id FROM FLRLagerbewegung AS bew"
				+ " WHERE bew.artikel_i_id= "
				+ artikelIId
				+ " AND bew.b_abgang=0 AND bew.b_historie=0 AND bew.n_menge>0 GROUP BY bew.c_belegartnr, bew.i_belegartpositionid, bew.lager_i_id HAVING count(bew.i_belegartpositionid)>1";

		org.hibernate.Query query = sessionLagerbewegung
				.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIteratorFTGruppe = resultList.iterator();
		while (resultListIteratorFTGruppe.hasNext()) {
			Object[] zeile = (Object[]) resultListIteratorFTGruppe.next();

			String cBelegartNr = (String) zeile[0];
			Integer iBelegartpositionId = (Integer) zeile[1];

			Query queryLager = em
					.createNamedQuery("LagerbewegungfindLetzteintrag");
			queryLager.setParameter(1, cBelegartNr);
			queryLager.setParameter(2, iBelegartpositionId);
			queryLager.setParameter(3, null);

			Collection cl = queryLager.getResultList();

			Iterator it = cl.iterator();

			Lagerbewegung lagerbewegung = (Lagerbewegung) it.next();

			// immer auf den ersten zusammenfuehren

			BigDecimal bdGesamtmenge = lagerbewegung.getNMenge();
			while (it.hasNext()) {

				Lagerbewegung lagerbewegungZusammenzufueren = (Lagerbewegung) it
						.next();

				bdGesamtmenge = bdGesamtmenge.add(lagerbewegungZusammenzufueren
						.getNMenge());

				Query queryUrsprung = em
						.createNamedQuery("LagerabgangursprungfindByILagerbewegungIIdursprung");
				queryUrsprung.setParameter(1,
						lagerbewegungZusammenzufueren.getIIdBuchung());
				Collection<?> clUrsprung = queryUrsprung.getResultList();

				Iterator itUrsprung = clUrsprung.iterator();
				while (itUrsprung.hasNext()) {
					Lagerabgangursprung lagerabgangursprung = (Lagerabgangursprung) itUrsprung
							.next();

					// Zuerst nachsehen, obs nicht schon einen
					// Ursprungseintrag
					// gibt
					LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
					lagerabgangursprungPK
							.setILagerbewegungid(lagerabgangursprung.getPk()
									.getILagerbewegungid());
					lagerabgangursprungPK
							.setILagerbewegungidursprung(lagerbewegung
									.getIIdBuchung());
					Lagerabgangursprung lagerabgangursprungVorhanden = em.find(
							Lagerabgangursprung.class, lagerabgangursprungPK);

					if (lagerabgangursprungVorhanden == null) {

						BigDecimal nMenge = lagerabgangursprung
								.getNVerbrauchtemenge();
						BigDecimal nPreis = lagerabgangursprung
								.getNGestehungspreis();

						em.remove(lagerabgangursprung);
						em.flush();
						Lagerabgangursprung lagerabgangursprungNeuAnlegen = new Lagerabgangursprung(
								lagerabgangursprungPK.getILagerbewegungid(),
								lagerabgangursprungPK
										.getILagerbewegungidursprung(), nMenge,
								nPreis);
						em.merge(lagerabgangursprungNeuAnlegen);
						em.flush();
					} else {
						// Wenn vorhanden, dann Menge aktualisieren
						lagerabgangursprungVorhanden
								.setNVerbrauchtemenge(lagerabgangursprungVorhanden
										.getNVerbrauchtemenge()
										.add(lagerabgangursprung
												.getNVerbrauchtemenge()));
						em.remove(lagerabgangursprung);
						em.flush();
					}
				}
				em.remove(lagerbewegungZusammenzufueren);
				em.flush();
			}

			Double verbrauchteMenge = getVerbrauchteMenge(lagerbewegung
					.getIId());

			if (verbrauchteMenge.doubleValue() < bdGesamtmenge.doubleValue()) {
				lagerbewegung.setBVollstaendigverbraucht(Helper
						.boolean2Short(false));
			} else {
				lagerbewegung.setBVollstaendigverbraucht(Helper
						.boolean2Short(true));
			}

			lagerbewegung.setNMenge(bdGesamtmenge);
			em.merge(lagerbewegung);
			em.flush();

		}

		sessionLagerbewegung.close();

		// ABGANGE

		sessionLagerbewegung = FLRSessionFactory.getFactory().openSession();

		queryString = "SELECT bew.c_belegartnr, bew.i_belegartpositionid FROM FLRLagerbewegung AS bew"
				+ " WHERE bew.artikel_i_id= "
				+ artikelIId
				+ " AND bew.b_abgang=1 AND bew.b_historie=0 AND bew.n_menge>0 GROUP BY bew.c_belegartnr, bew.i_belegartpositionid HAVING count(bew.i_belegartpositionid)>1";

		query = sessionLagerbewegung.createQuery(queryString);
		resultList = query.list();
		resultListIteratorFTGruppe = resultList.iterator();
		while (resultListIteratorFTGruppe.hasNext()) {
			Object[] zeile = (Object[]) resultListIteratorFTGruppe.next();

			String cBelegartNr = (String) zeile[0];
			Integer iBelegartpositionId = (Integer) zeile[1];

			Query queryLager = em
					.createNamedQuery("LagerbewegungfindLetzteintrag");
			queryLager.setParameter(1, cBelegartNr);
			queryLager.setParameter(2, iBelegartpositionId);
			queryLager.setParameter(3, null);

			Collection cl = queryLager.getResultList();

			Iterator it = cl.iterator();

			Lagerbewegung lagerbewegung = (Lagerbewegung) it.next();

			// immer auf den ersten zusammenfuehren

			BigDecimal bdGesamtmenge = lagerbewegung.getNMenge();
			while (it.hasNext()) {

				Lagerbewegung lagerbewegungZusammenzufueren = (Lagerbewegung) it
						.next();

				bdGesamtmenge = bdGesamtmenge.add(lagerbewegungZusammenzufueren
						.getNMenge());

				Query queryUrsprung = em
						.createNamedQuery("LagerabgangursprungfindByILagerbewegungid");
				queryUrsprung.setParameter(1,
						lagerbewegungZusammenzufueren.getIIdBuchung());
				Collection<?> clUrsprung = queryUrsprung.getResultList();

				Iterator itUrsprung = clUrsprung.iterator();
				while (itUrsprung.hasNext()) {
					Lagerabgangursprung lagerabgangursprung = (Lagerabgangursprung) itUrsprung
							.next();

					// Zuerst nachsehen, obs nicht schon einen
					// Ursprungseintrag
					// gibt
					LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
					lagerabgangursprungPK.setILagerbewegungid(lagerbewegung
							.getIIdBuchung());
					lagerabgangursprungPK
							.setILagerbewegungidursprung(lagerabgangursprung
									.getPk().getILagerbewegungidursprung());
					Lagerabgangursprung lagerabgangursprungVorhanden = em.find(
							Lagerabgangursprung.class, lagerabgangursprungPK);

					if (lagerabgangursprungVorhanden == null) {

						BigDecimal nMenge = lagerabgangursprung
								.getNVerbrauchtemenge();
						BigDecimal nPreis = lagerabgangursprung
								.getNGestehungspreis();

						em.remove(lagerabgangursprung);
						em.flush();
						Lagerabgangursprung lagerabgangursprungNeuAnlegen = new Lagerabgangursprung(
								lagerabgangursprungPK.getILagerbewegungid(),
								lagerabgangursprungPK
										.getILagerbewegungidursprung(), nMenge,
								nPreis);
						em.merge(lagerabgangursprungNeuAnlegen);
						em.flush();
					} else {
						// Wenn vorhanden, dann Menge aktualisieren
						lagerabgangursprungVorhanden
								.setNVerbrauchtemenge(lagerabgangursprungVorhanden
										.getNVerbrauchtemenge()
										.add(lagerabgangursprung
												.getNVerbrauchtemenge()));
						em.remove(lagerabgangursprung);
						em.flush();
					}
				}
				em.remove(lagerbewegungZusammenzufueren);
				em.flush();
			}

			lagerbewegung.setNMenge(bdGesamtmenge);
			em.merge(lagerbewegung);
			em.flush();

		}
	}

	public int aendereEinzelneSerienChargennummerEinesArtikel(
			Integer artikelIId, String snrChnr_Alt, String snrChnr_Neu,
			String version_Alt, String version_Neu, TheClientDto theClientDto) {
		// Alle Lagerbewegungen eines Artikels mit der SNR-Chargennummer holen
		int iAnzahl = 0;

		if (artikelIId != null && snrChnr_Alt != null && snrChnr_Neu != null) {

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					artikelIId, theClientDto);

			if (Helper.short2boolean(artikelDto.getBChargennrtragend()) == true
					|| Helper.short2boolean(artikelDto.getBSeriennrtragend()) == true) {

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {

					try {
						ParametermandantDto parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE);
						Integer ichnrlaenge = (Integer) parameter
								.getCWertAsObject();

						ArrayList al = new ArrayList();
						al.add(ichnrlaenge);

						if (snrChnr_Neu.length() < ichnrlaenge) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_CHARGENNUMMER_ZU_KURZ,
									al, new Exception(
											"FEHLER_CHARGENNUMMER_ZU_KURZ"));

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else if (Helper.short2boolean(artikelDto
						.getBSeriennrtragend())) {
					try {
						ParametermandantDto parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);
						Integer isnrlaenge = (Integer) parameter
								.getCWertAsObject();

						ArrayList al = new ArrayList();
						al.add(isnrlaenge);

						if (snrChnr_Neu.length() < isnrlaenge) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_KURZ,
									al, new Exception(
											"FEHLER_SERIENNUMMER_ZU_KURZ"));

						}
						parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER);
						isnrlaenge = (Integer) parameter.getCWertAsObject();

						al = new ArrayList();
						al.add(isnrlaenge);

						if (snrChnr_Neu.length() > isnrlaenge) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_LANG,
									al, new Exception(
											"FEHLER_SERIENNUMMER_ZU_LANG"));

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// Zuerst Lagerbewegungen updaten

				Query query = em
						.createNamedQuery("LagerbewegungfindAllSnrChnrEinesArtikels");
				query.setParameter(1, artikelIId);
				query.setParameter(2, snrChnr_Alt);
				Collection<?> cl = query.getResultList();

				Iterator<?> iterator = cl.iterator();
				while (iterator.hasNext()) {
					Lagerbewegung lagerbewegung = (Lagerbewegung) iterator
							.next();

					if (Helper.short2boolean(artikelDto.getBSeriennrtragend()) == true) {
						if (version_Alt != null) {
							if (!version_Alt
									.equals(lagerbewegung.getCVersion())) {
								// Wenn Version nicht gleich, dann auslassen
								continue;
							}

						}
						lagerbewegung.setCVersion(version_Neu);
					}

					lagerbewegung.setCSeriennrchargennr(snrChnr_Neu);

					em.merge(lagerbewegung);
					em.flush();
					iAnzahl++;

				}

			}

		}
		return iAnzahl;
	}

	/**
	 * Gibt den Lagerstand eines Artikels in einem bestimmten Lager zurueck
	 * (Quick-Lagerstand). Hier kann keine Seriennummer/Chargennummer
	 * mitangegeben werden -> Ist immer ein Gesamtlagerstand eines Artikels und
	 * Lagers. <br>
	 * Diese Methode wirft keine Exceptions.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param theClientDto
	 *            User-ID
	 * @return BigDecimal Lagerstand, wenn nichts auf Lager 0
	 */
	public BigDecimal getLagerstandOhneExc(Integer artikelIId,
			Integer lagerIId, TheClientDto theClientDto) {
		// check(idUser);

		BigDecimal bdLagerstandO = new BigDecimal(0);

		try {
			bdLagerstandO = artikellagerFindByPrimaryKey(artikelIId, lagerIId)
					.getNLagerstand();
		} catch (Throwable t) {
			//
		}
		return bdLagerstandO;
	}

	public BigDecimal getPaternosterLagerstand(Integer artikelIId) {

		BigDecimal bdLagerstandO = new BigDecimal(0);

		Query query = em
				.createNamedQuery("ArtikellagerplaetzefindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection cl = query.getResultList();
		ArtikellagerplaetzeDto[] dtos = assembleArtikellagerplaetzeDtos(cl);
		for (int i = 0; i < dtos.length; i++) {
			if (dtos[i].getNLagerstandPaternoster() != null) {
				bdLagerstandO = bdLagerstandO.add(dtos[i]
						.getNLagerstandPaternoster());
			}
		}

		return bdLagerstandO;
	}

	/**
	 * Liefert Mindest-Verkaufspreis zurueck:= WW_ARTIKELLAGER.N_GESTEHUNGSPREIS
	 * * WW_ARTIKEL.F_MINDESTDECKUNGSBEITRAG.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lagerIId
	 *            Integer
	 * @param theClientDto
	 *            User-ID
	 * @return BigDecimal Mindestverkaufspreis
	 */
	public BigDecimal getMindestverkaufspreis(Integer artikelIId,
			Integer lagerIId, BigDecimal nMenge, TheClientDto theClientDto) {
		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null || lagerIId == null"));
		}
		Artikel artikel = null;

		// Mindestdeckungsbeitrag holen
		artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		Double mindestdeckungsbeitrag = artikel.getFMindestdeckungsbeitrag();
		BigDecimal minVerkaufspreis = null;

		ParametermandantDto p = null;
		try {
			p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		} catch (RemoteException e) {
			//
		} catch (EJBExceptionLP e) {
			//
		}
		// PJ 15314 minVK Preis aus Lieferant1 ermitteln wenn dies die Basis
		if (p.getCWert().equals("1")) {
			try {
				minVerkaufspreis = getVkPreisfindungFac().ermittlePreisbasis(
						artikelIId,
						new java.sql.Date(System.currentTimeMillis()), nMenge,
						null, theClientDto.getSMandantenwaehrung(),
						theClientDto);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			}
			if (minVerkaufspreis != null) {
				minVerkaufspreis = minVerkaufspreis.multiply(new BigDecimal(1)
						.add(new BigDecimal(mindestdeckungsbeitrag
								.doubleValue()).divide(new BigDecimal(100), 4,
								BigDecimal.ROUND_HALF_EVEN)));
				return Helper.rundeKaufmaennisch(minVerkaufspreis, 4);
			}
		}

		// 1. Gibt es den Artikel oder es gab ihn schon auf Lager X ? -> return
		// minVK
		ArtikellagerPK artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikelIId);
		artikellagerPK.setLagerIId(lagerIId);

		Artikellager artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager != null) {
			minVerkaufspreis = artikellager.getNGestehungspreis().multiply(
					new BigDecimal(1).add(new BigDecimal(mindestdeckungsbeitrag
							.doubleValue()).divide(new BigDecimal(100), 4,
							BigDecimal.ROUND_HALF_EVEN)));
			return Helper.rundeKaufmaennisch(minVerkaufspreis, 4);
		}

		// 2. Gibt es den Artikel am Hauptlager? -> return minVK
		Query query = em.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, LagerFac.LAGERART_HAUPTLAGER);
		Lager lager = (Lager) query.getSingleResult();

		artikellagerPK = new ArtikellagerPK();
		artikellagerPK.setArtikelIId(artikelIId);
		artikellagerPK.setLagerIId(lager.getIId());

		artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager != null) {
			minVerkaufspreis = artikellager.getNGestehungspreis()
					.multiply(
							(new BigDecimal(1).add(new BigDecimal(
									mindestdeckungsbeitrag)).divide(
									new BigDecimal(100), 4,
									BigDecimal.ROUND_HALF_EVEN)));
			return Helper.rundeKaufmaennisch(minVerkaufspreis, 4);
		}

		// 3. Den Einkaufspreis (Nettopreis) des bevorzugten Lieferanten (= der
		// erste Lieferant)
		try {
			ArtikellieferantDto dtos = getArtikelFac().getArtikelEinkaufspreis(
					artikelIId, nMenge, theClientDto.getSMandantenwaehrung(),
					theClientDto);
			if (dtos != null) {
				if (dtos.getLief1Preis() != null) {
					minVerkaufspreis = dtos.getLief1Preis().multiply(
							(new BigDecimal(1).add((new BigDecimal(
									mindestdeckungsbeitrag).divide(
									new BigDecimal(100), 4,
									BigDecimal.ROUND_HALF_EVEN)))));

				} else {
					// lt. Doku von UW
					minVerkaufspreis = new BigDecimal(0);
				}
			} else {
				// 4. Sonst 0
				minVerkaufspreis = new BigDecimal(0);
			}
		} catch (RemoteException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex3);
		}
		return Helper.rundeKaufmaennisch(minVerkaufspreis, 4);
	}

	public LagerartDto lagerartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}
		Lagerart lagerart = em.find(Lagerart.class, cNr);
		if (lagerart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerartDto(lagerart);
	}

	public LagerartDto[] lagerartFindAll() throws EJBExceptionLP {
		myLogger.entry();
		Query query = em.createNamedQuery("LagerartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleLagerartDtos(cl);
	}

	private void setLagerartFromLagerartDto(Lagerart lagerart,
			LagerartDto lagerartDto) {
		em.merge(lagerart);
		em.flush();
	}

	private LagerartDto assembleLagerartDto(Lagerart lagerart) {
		return LagerartDtoAssembler.createDto(lagerart);
	}

	private LagerartDto[] assembleLagerartDtos(Collection<?> lagerarts) {
		List<LagerartDto> list = new ArrayList<LagerartDto>();
		if (lagerarts != null) {
			Iterator<?> iterator = lagerarts.iterator();
			while (iterator.hasNext()) {
				Lagerart lagerart = (Lagerart) iterator.next();
				list.add(assembleLagerartDto(lagerart));
			}
		}
		LagerartDto[] returnArray = new LagerartDto[list.size()];
		return (LagerartDto[]) list.toArray(returnArray);
	}

	public LagerartsprDto lagerartsprFindByPrimaryKey(String lagerartCNr,
			String spracheCNr) throws EJBExceptionLP {
		LagerartsprPK lagerartsprPK = new LagerartsprPK();
		lagerartsprPK.setLagerartCNr(lagerartCNr);
		lagerartsprPK.setLocaleCNr(spracheCNr);
		Lagerartspr lagerartspr = em.find(Lagerartspr.class, lagerartsprPK);
		if (lagerartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerartsprDto(lagerartspr);
	}

	private void setLagerartsprFromLagerartsprDto(Lagerartspr lagerartspr,
			LagerartsprDto lagerartsprDto) {
		lagerartspr.setCBez(lagerartsprDto.getCBez());
		em.merge(lagerartspr);
		em.flush();
	}

	private LagerartsprDto assembleLagerartsprDto(Lagerartspr lagerartspr) {
		return LagerartsprDtoAssembler.createDto(lagerartspr);
	}

	private LagerartsprDto[] assembleLagerartsprDtos(Collection<?> lagerartsprs) {
		List<LagerartsprDto> list = new ArrayList<LagerartsprDto>();
		if (lagerartsprs != null) {
			Iterator<?> iterator = lagerartsprs.iterator();
			while (iterator.hasNext()) {
				Lagerartspr lagerartspr = (Lagerartspr) iterator.next();
				list.add(assembleLagerartsprDto(lagerartspr));
			}
		}
		LagerartsprDto[] returnArray = new LagerartsprDto[list.size()];
		return (LagerartsprDto[]) list.toArray(returnArray);
	}

	/**
	 * Legt einen neuen Bewegungsdatensatz an.
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 */
	private void createLagerbewegung(LagerbewegungDto lagerbewegungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerbewegungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerbewegungDto == null"));

		}
		if (lagerbewegungDto.getCBelegartnr() == null
				|| lagerbewegungDto.getIBelegartpositionid() == null
				|| lagerbewegungDto.getTBelegdatum() == null
				|| lagerbewegungDto.getIIdBuchung() == null
				|| lagerbewegungDto.getLagerIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerbewegungDto.getBelegartCNr() == null || lagerbewegungDto.getTBelegdatum() == null || lagerbewegungDto.getBelegartpositionIId() == null || lagerbewegungDto.getIIdBuchung() == null || lagerbewegungDto.getLagerIId() == null"));

		}
		if (lagerbewegungDto.getArtikelIId() == null
				|| lagerbewegungDto.getBAbgang() == null
				|| lagerbewegungDto.getBVollstaendigverbraucht() == null
				|| lagerbewegungDto.getIBelegartid() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerbewegungDto.getArtikelIId() == null || lagerbewegungDto.getBAbgang() == null || lagerbewegungDto.getBVollstaendigverbraucht() == null || lagerbewegungDto.getBelegartIId() == null"));

		}
		if (Helper.short2boolean(lagerbewegungDto.getBAbgang()) == true
				&& lagerbewegungDto.getNGestehungspreis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"Helper.short2boolean(lagerbewegungDto.getBAbgang()) == true && lagerbewegungDto.getNGestehungspreis() == null"));

		}
		ArtikellagerPK artikellagerPK = new ArtikellagerPK(
				lagerbewegungDto.getArtikelIId(),
				lagerbewegungDto.getLagerIId());
		Artikellager artikellager = em.find(Artikellager.class, artikellagerPK);
		if (artikellager == null) {
			try {
				artikellager = new Artikellager(artikellagerPK,
						theClientDto.getMandant());
				em.merge(artikellager);
				em.flush();
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
			}
		}
		try {

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LAGERBEWEGUNG);
			lagerbewegungDto.setIId(pk);

			lagerbewegungDto.setPersonalIIdMengegeaendert(theClientDto
					.getIDPersonal());

			Lagerbewegung lagerbewegung = new Lagerbewegung(
					lagerbewegungDto.getIId(),
					lagerbewegungDto.getCBelegartnr(),
					lagerbewegungDto.getIBelegartid(),
					lagerbewegungDto.getIBelegartpositionid(),
					lagerbewegungDto.getIIdBuchung(),
					lagerbewegungDto.getLagerIId(),
					lagerbewegungDto.getArtikelIId(),
					lagerbewegungDto.getNMenge(),
					lagerbewegungDto.getPersonalIIdMengegeaendert(),
					lagerbewegungDto.getBAbgang(),
					lagerbewegungDto.getBVollstaendigverbraucht(),
					lagerbewegungDto.getTBelegdatum());
			if (lagerbewegungDto.getTBuchungszeit() != null)
				// Buchungszeit muss bei Korrrekturbuchungen uebernommen werden
				lagerbewegung.setTBuchungszeit(lagerbewegungDto
						.getTBuchungszeit());
			em.persist(lagerbewegung);
			em.flush();
			setLagerbewegungFromLagerbewegungDto(lagerbewegung,
					lagerbewegungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Schreibt Preisaenderungen im Lagerbewegungsdatensatz fest.
	 * Mengenaenderungen sind nicht moeglich, dazu muss ein Korrekturdatensatz
	 * angelegt werden.
	 * 
	 * Zus&auml;tzlich wird jetzt auch das Historie Flag upgedatet
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 *             lagerbewegungDto == null oder "lagerbewegungDto.getIId() ==
	 *             null oder lagerbewegungDto.getBelegartCNr() == null ||
	 *             lagerbewegungDto.getBelegartpositionIId() == null ||
	 *             lagerbewegungDto.getIIdBuchung() == null ||
	 *             lagerbewegungDto.getLagerIId() == null
	 */
	public void updateLagerbewegung(LagerbewegungDto lagerbewegungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerbewegungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerbewegungDto == null"));

		}
		if (lagerbewegungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerbewegungDto.getIId() == null"));

		}
		if (lagerbewegungDto.getCBelegartnr() == null
				|| lagerbewegungDto.getIBelegartpositionid() == null
				|| lagerbewegungDto.getTBelegdatum() == null
				|| lagerbewegungDto.getIIdBuchung() == null
				|| lagerbewegungDto.getLagerIId() == null
				|| lagerbewegungDto.getBHistorie() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerbewegungDto.getBelegartCNr() == null || lagerbewegungDto.getTBelegdatum() == null || lagerbewegungDto.getBelegartpositionIId() == null || lagerbewegungDto.getIIdBuchung() == null || lagerbewegungDto.getLagerIId() == null"));

		}
		if (lagerbewegungDto.getArtikelIId() == null
				|| lagerbewegungDto.getPersonalIIdMengegeaendert() == null
				|| lagerbewegungDto.getBAbgang() == null
				|| lagerbewegungDto.getBVollstaendigverbraucht() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerbewegungDto.getArtikelIId() == null || lagerbewegungDto.getIPersonMengegeaendert() == null || lagerbewegungDto.getBAbgang() == null || lagerbewegungDto.getBVollstaendigverbraucht() == null"));

		}
		Integer iId = lagerbewegungDto.getIId();
		Lagerbewegung lagerbewegung = null;
		lagerbewegung = em.find(Lagerbewegung.class, iId);
		if (lagerbewegung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		// Nur Preisaenderungen festhalten und ob Artikel schon vollstaendig
		// verbraucht worden ist
		if (lagerbewegungDto.getBAbgang().intValue() == 0) {
			lagerbewegung.setPersonalIIdEinstandspreisgeaendert(theClientDto
					.getIDPersonal());
			lagerbewegung.setNEinstandspreis(lagerbewegungDto
					.getNEinstandspreis());
			lagerbewegung.setNGestehungspreis(lagerbewegungDto
					.getNGestehungspreis());
		} else {
			lagerbewegung.setPersonalIIdVerkaufspreisgeaendert(theClientDto
					.getIDPersonal());
			lagerbewegung.setNVerkaufspreis(lagerbewegungDto
					.getNVerkaufspreis());
			lagerbewegung.setNGestehungspreis(lagerbewegungDto
					.getNGestehungspreis());
		}
		lagerbewegung.setBVollstaendigverbraucht(lagerbewegungDto
				.getBVollstaendigverbraucht());
		lagerbewegung.setTBelegdatum(lagerbewegungDto.getTBelegdatum());
		if (lagerbewegungDto.getTBuchungszeit() != null) {
			lagerbewegung.setTBuchungszeit(lagerbewegungDto.getTBuchungszeit());
		}
		lagerbewegung.setBHistorie(lagerbewegungDto.getBHistorie());

		em.merge(lagerbewegung);
		em.flush();
	}

	public void updateLagerbewegungs(LagerbewegungDto[] lagerbewegungDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerbewegungDtos != null) {
			for (int i = 0; i < lagerbewegungDtos.length; i++) {
				updateLagerbewegung(lagerbewegungDtos[i], theClientDto);
			}
		}
	}

	/**
	 * Holt einen Lagerbewegungsdatensatz anhand des Primaerschluessels
	 * 
	 * @param iId
	 *            8
	 * @throws EJBExceptionLP
	 *             iId == null
	 * @return LagerbewegungDto
	 */
	public LagerbewegungDto lagerbewegungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Lagerbewegung lagerbewegung = em.find(Lagerbewegung.class, iId);
		if (lagerbewegung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerbewegungDto(lagerbewegung);
	}

	/**
	 * Gibt Alle Lagerbewegunsdatensaetzte einer Buchungsnummer zurueck.
	 * 
	 * @param iIdBuchung
	 *            78
	 * @throws EJBExceptionLP
	 *             iIdBuchung == null
	 * @return LagerbewegungDto[]
	 */
	public LagerbewegungDto[] lagerbewegungFindByIIdBuchung(Integer iIdBuchung)
			throws EJBExceptionLP {
		if (iIdBuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchung == null"));
		}
		Query query = em.createNamedQuery("LagerbewegungfindByIIdBuchung");
		query.setParameter(1, iIdBuchung);
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);
	}

	/**
	 * Holt Alle Lagerbuchungen eines Artikels eines Lagers einer
	 * Buchungsrichtung (ZU-/ABGANG) ausser diejenigen, die schon vollstaendig
	 * verbraucht sind, wenn nicht seriennummerntragen Wenn eine Seriennummer
	 * angegeben wird, dann wird diese mitberuecksichtigt.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param bAbgang
	 *            0
	 * @param cSeriennrchargennr
	 *            f546
	 * @throws EJBExceptionLP
	 *             artikelIId == null || lagerIId == null || bAbgang == null
	 * @return LagerbewegungDto[]
	 */
	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdLagerIIdBAbgangCSeriennrchargennr(
			Integer artikelIId, Integer lagerIId, Short bAbgang,
			String cSeriennrchargennr) throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null || bAbgang == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || lagerIId == null || bAbgang == null"));
		}
		Query query = em
				.createNamedQuery("LagerbewegungfindByArtikelIIdLagerIIdBAbgangCSeriennrchargennrBVollstaendigverbraucht");
		query.setParameter(1, artikelIId);
		query.setParameter(2, lagerIId);
		query.setParameter(3, bAbgang);
		query.setParameter(4, cSeriennrchargennr);
		query.setParameter(5, Helper.boolean2Short(false));
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);
	}

	/**
	 * Holt Alle Lagerbuchungen eines Artikels einer Buchungsrichtung
	 * (ZU-/ABGANG) ausser diejenigen, die schon vollstaendig verbraucht sind
	 * 
	 * @param artikelIId
	 *            4711
	 * @param bAbgang
	 *            0
	 * @throws EJBExceptionLP
	 *             artikelIId == null || bAbgang == null
	 * @return LagerbewegungDto[]
	 */
	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdBAbgang(
			Integer artikelIId, Short bAbgang) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelIId == null || bAbgang == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || bAbgang == null"));
		}
		Query query = em
				.createNamedQuery("LagerbewegungfindByArtikelIIdBAbgang");
		query.setParameter(1, artikelIId);
		query.setParameter(2, bAbgang);
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);
	}

	public LagerbewegungDto[] lagerbewegungFindByArtikelIId(Integer artikelIId)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		Query query = em.createNamedQuery("LagerbewegungfindArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);
	}

	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdCSeriennrChargennr(
			Integer artikelIId, String cSnrChnr) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		Query query = em
				.createNamedQuery("LagerbewegungfindAllSnrChnrEinesArtikels");
		query.setParameter(1, artikelIId);
		query.setParameter(2, cSnrChnr);
		Collection<?> cl = query.getResultList();

		return assembleLagerbewegungDtos(cl);

	}

	/**
	 * Holt alle Lagerbewegungsdatensaetzte einer Belegart und einer
	 * Belegartposition, ausser vollstaendig verbrauchten Lagerbewegungen. Wenn
	 * eine Seriennummer angegeben wird, dann wird diese mitberuecksichtigt.
	 * 
	 * @param belegartCNr
	 *            Rechnung
	 * @param belegartIId
	 *            6785
	 * @param artikelIId
	 *            Id des Artikels
	 * @param cSnrChnr
	 *            f54653
	 * @throws EJBExceptionLP
	 *             belegartCNr == null || belegartpositionIId == null
	 * @return LagerbewegungDto[]
	 */

	public boolean pruefeObSnrChnrAufBelegGebuchtWurde(String belegartCNr,
			Integer belegartIId, Integer artikelIId, String cSnrChnr) {

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(
				FLRLagerbewegung.class).createAlias(
				LagerFac.FLR_LAGERBEWEGUNG_FLRARTIKEL, "a");
		crit.add(Restrictions.eq("a.i_id", artikelIId));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
				Helper.boolean2Short(false)));
		crit.add(Restrictions.eq(
				LagerFac.FLR_LAGERBEWEGUNG_C_SERIENNRCHARGENNR, cSnrChnr));
		crit.add(Restrictions.gt(LagerFac.FLR_LAGERBEWEGUNG_N_MENGE,
				new BigDecimal(0)));
		List<?> resultList = crit.list();

		if (resultList.size() > 0) {

			session.close();
			return true;
		} else {

			session.close();
			return false;
		}

	}

	public HandlagerbewegungDto getZugehoerigeUmbuchung(
			Integer handlagerbewegungIId, TheClientDto theClientDto) {
		HandlagerbewegungDto handlagerbewegungDto = handlagerbewegungFindByPrimaryKey(
				handlagerbewegungIId, theClientDto);

		String cSnrChnr = null;

		if (handlagerbewegungDto.getSeriennrChargennrMitMenge() != null
				&& handlagerbewegungDto.getSeriennrChargennrMitMenge().size() > 0) {
			cSnrChnr = handlagerbewegungDto.getSeriennrChargennrMitMenge()
					.get(0).getCSeriennrChargennr();
		}

		LagerbewegungDto lbewDto = getLetzteintrag(LocaleFac.BELEGART_HAND,
				handlagerbewegungIId, cSnrChnr);
		if (Helper.short2boolean(handlagerbewegungDto.getBAbgang())) {
			LagerumbuchungDto[] luDtos = lagerumbuchungFindByIdAbbuchung(lbewDto
					.getIIdBuchung());
			if (luDtos.length > 0) {
				LagerbewegungDto tempDto = getJuengsteBuchungEinerBuchungsNummer(luDtos[0]
						.getILagerbewegungidzubuchung());
				Handlagerbewegung handlagerbewegung = em.find(
						Handlagerbewegung.class,
						tempDto.getIBelegartpositionid());
				if (handlagerbewegung == null) {
					return null;
				} else {
					return handlagerbewegungFindByPrimaryKey(
							tempDto.getIBelegartpositionid(), theClientDto);
				}

			} else {
				return null;
			}
		} else {
			LagerumbuchungDto[] luDtos = lagerumbuchungFindByIdZubuchung(lbewDto
					.getIIdBuchung());
			if (luDtos.length > 0) {
				LagerbewegungDto tempDto = getJuengsteBuchungEinerBuchungsNummer(luDtos[0]
						.getILagerbewegungidabbuchung());
				Handlagerbewegung handlagerbewegung = em.find(
						Handlagerbewegung.class,
						tempDto.getIBelegartpositionid());
				if (handlagerbewegung == null) {
					return null;
				} else {
					return handlagerbewegungFindByPrimaryKey(
							tempDto.getIBelegartpositionid(), theClientDto);
				}
			} else {
				return null;
			}
		}

	}

	public LagerbewegungDto getLetzteintrag(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrchargennr) {

		Query query = em.createNamedQuery("LagerbewegungfindLetzteintrag");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		query.setParameter(3, cSeriennrchargennr);

		try {
			Collection cl = query.getResultList();

			if (cl.size() == 1) {
				return assembleLagerbewegungDto((Lagerbewegung) cl.iterator()
						.next());
			} else if (cl.size() == 0) {
				System.out.println("Keine Daten gefunden: " + belegartCNr
						+ " Pos:" + belegartpositionIId + " SNR:"
						+ cSeriennrchargennr);
				return null;
			} else {
				Iterator it = cl.iterator();
				LagerbewegungDto lBew = assembleLagerbewegungDto((Lagerbewegung) it
						.next());
				while (it.hasNext()) {
					lBew.setNMenge(lBew.getNMenge().add(
							((Lagerbewegung) it.next()).getNMenge()));
				}
				System.out.println("Zuviele Buchungen: " + belegartCNr
						+ " Pos:" + belegartpositionIId + " SNR:"
						+ cSeriennrchargennr);
				return lBew;
			}

		} catch (NoResultException ex) {
			return null;
		}

	}

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrchargennr) throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}
		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		query.setParameter(3, cSeriennrchargennr);
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);
	}

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId) {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}
		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		Collection<?> cl = query.getResultList();
		return assembleLagerbewegungDtos(cl);

	}

	/**
	 * Gibt die Buchungsnummer einer Lagerbewegung anhand der eindeutigen ID
	 * zurueck.
	 * 
	 * @param iId
	 *            6
	 * @throws EJBExceptionLP
	 *             iId == null
	 * @return Integer
	 */
	public Integer lagerbewegungFindIIdBuchungByIId(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iId == null"));
		}
		Query query = em.createNamedQuery("LagerbewegungfindIIdBuchungByIId");
		query.setParameter(1, iId);
		Lagerbewegung lagerbewegung = (Lagerbewegung) query.getSingleResult();
		if (lagerbewegung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return lagerbewegung.getIIdBuchung();
	}

	private void setLagerbewegungFromLagerbewegungDto(
			Lagerbewegung lagerbewegung, LagerbewegungDto lagerbewegungDto) {
		lagerbewegung.setCBelegartnr(lagerbewegungDto.getCBelegartnr());
		lagerbewegung.setIBelegartpositionid(lagerbewegungDto
				.getIBelegartpositionid());
		lagerbewegung.setIBelegartid(lagerbewegungDto.getIBelegartid());
		lagerbewegung.setIIdBuchung(lagerbewegungDto.getIIdBuchung());
		lagerbewegung.setLagerIId(lagerbewegungDto.getLagerIId());
		lagerbewegung.setArtikelIId(lagerbewegungDto.getArtikelIId());
		lagerbewegung.setNMenge(lagerbewegungDto.getNMenge());
		lagerbewegung.setPersonalIIdMengegeaendert(lagerbewegungDto
				.getPersonalIIdMengegeaendert());
		lagerbewegung.setCSeriennrchargennr(lagerbewegungDto
				.getCSeriennrchargennr());
		lagerbewegung.setNVerkaufspreis(lagerbewegungDto.getNVerkaufspreis());
		lagerbewegung.setPersonalIIdVerkaufspreisgeaendert(lagerbewegungDto
				.getPersonalIIdVerkaufspreisgeaendert());
		lagerbewegung.setNEinstandspreis(lagerbewegungDto.getNEinstandspreis());
		lagerbewegung.setPersonalIIdEinstandspreisgeaendert(lagerbewegungDto
				.getPersonalIIdEinstandspreisgeaendert());
		lagerbewegung.setNGestehungspreis(lagerbewegungDto
				.getNGestehungspreis());
		lagerbewegung.setTBelegdatum(lagerbewegungDto.getTBelegdatum());
		lagerbewegung.setBAbgang(lagerbewegungDto.getBAbgang());
		lagerbewegung.setBVollstaendigverbraucht(lagerbewegungDto
				.getBVollstaendigverbraucht());
		lagerbewegung.setLandIId(lagerbewegungDto.getLandIId());
		lagerbewegung.setHerstellerIId(lagerbewegungDto.getHerstellerIId());
		lagerbewegung.setCVersion(lagerbewegungDto.getCVersion());
		em.merge(lagerbewegung);
		em.flush();
	}

	private LagerbewegungDto assembleLagerbewegungDto(
			Lagerbewegung lagerbewegung) {
		return LagerbewegungDtoAssembler.createDto(lagerbewegung);
	}

	private GeraetesnrDto assembleGeraetesnrDto(Geraetesnr geraetesnr) {
		return GeraetesnrDtoAssembler.createDto(geraetesnr);
	}

	private LagerbewegungDto[] assembleLagerbewegungDtos(
			Collection<?> lagerbewegungs) {
		List<LagerbewegungDto> list = new ArrayList<LagerbewegungDto>();
		if (lagerbewegungs != null) {
			Iterator<?> iterator = lagerbewegungs.iterator();
			while (iterator.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iterator.next();
				list.add(assembleLagerbewegungDto(lagerbewegung));
			}
		}
		LagerbewegungDto[] returnArray = new LagerbewegungDto[list.size()];
		return (LagerbewegungDto[]) list.toArray(returnArray);
	}

	/**
	 * Ordnet einem Lagerabgang einen Lagerzugang zu, um zu einem beliebigen
	 * Zeitpunkt festellen zu koennen, welcher Lagerabgang aus welchem
	 * Lagerzugang zustande kam.
	 * 
	 * @param lagerabgangursprungDto
	 *            LagerabgangursprungDto
	 * @throws EJBExceptionLP
	 */
	public void createLagerabgangursprung(
			LagerabgangursprungDto lagerabgangursprungDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (lagerabgangursprungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerabgangursprungDto == null"));
		}
		if (lagerabgangursprungDto.getILagerbewegungid() == null
				|| lagerabgangursprungDto.getILagerbewegungidursprung() == null
				|| lagerabgangursprungDto.getNVerbrauchtemenge() == null
				|| lagerabgangursprungDto.getNGestehungspreis() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lagerabgangursprungDto.getLagerbewegungIIdBuchung() == null || lagerabgangursprungDto.getLagerbewegungIIdBuchungsursprung() == null || lagerabgangursprungDto.getFVerbrauchtemenge() == null || lagerabgangursprungDto.getNGestehungspreis() == null"));
		}
		if (lagerabgangursprungDto.getILagerbewegungid().intValue() == lagerabgangursprungDto
				.getILagerbewegungidursprung().intValue()) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lagerabgangursprungDto.getLagerbewegungIIdBuchung() == null || lagerabgangursprungDto.getLagerbewegungIIdBuchungsursprung() == null"));
		}

		try {
			Lagerabgangursprung lagerabgangursprung = null;
			try {
				// myLogger.info("ims487" + lagerabgangursprungDto.toString());
				lagerabgangursprung = new Lagerabgangursprung(
						lagerabgangursprungDto.getILagerbewegungid(),
						lagerabgangursprungDto.getILagerbewegungidursprung(),
						lagerabgangursprungDto.getNVerbrauchtemenge(),
						lagerabgangursprungDto.getNGestehungspreis());
				em.persist(lagerabgangursprung);
				em.flush();

			} catch (EntityExistsException ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, ex);
			}

			setLagerabgangursprungFromLagerabgangursprungDto(
					lagerabgangursprung, lagerabgangursprungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Hebt die Zuordnung eines Lagerabgang mit seinem Ursprung auf
	 * 
	 * @param lagerabgangursprungDto
	 *            lagerabgangursprungDto
	 * @throws EJBExceptionLP
	 */
	public void removeLagerabgangursprung(
			LagerabgangursprungDto lagerabgangursprungDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (lagerabgangursprungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerabgangursprungDto == null"));
		}
		if (lagerabgangursprungDto.getILagerbewegungid() == null
				|| lagerabgangursprungDto.getILagerbewegungidursprung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerabgangursprungDto.getLagerbewegungIIdBuchung() == null || lagerabgangursprungDto.getLagerbewegungIIdBuchungsursprung() == null"));
		}
		LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
		lagerabgangursprungPK.setILagerbewegungid(lagerabgangursprungDto
				.getILagerbewegungid());
		lagerabgangursprungPK
				.setILagerbewegungidursprung(lagerabgangursprungDto
						.getILagerbewegungidursprung());
		Lagerabgangursprung lagerabgangursprung = null;
		try {
			lagerabgangursprung = em.find(Lagerabgangursprung.class,
					lagerabgangursprungPK);
			if (lagerabgangursprung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(lagerabgangursprung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	/**
	 * Findet eine Zuordnung eines Lagerabganges zu seinem Ursprung.
	 * 
	 * @param iIdBuchung
	 *            7
	 * @param iIdBuchungsursprung
	 *            2
	 * @throws EJBExceptionLP
	 * @return LagerabgangursprungDto
	 */
	public LagerabgangursprungDto lagerabgangursprungFindByPrimaryKey(
			Integer iIdBuchung, Integer iIdBuchungsursprung)
			throws EJBExceptionLP {
		if (iIdBuchung == null || iIdBuchungsursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"iIdBuchung == null || iIdBuchungsursprung == null"));
		}
		LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
		lagerabgangursprungPK.setILagerbewegungid(iIdBuchung);
		lagerabgangursprungPK.setILagerbewegungidursprung(iIdBuchungsursprung);
		Lagerabgangursprung lagerabgangursprung = em.find(
				Lagerabgangursprung.class, lagerabgangursprungPK);
		if (lagerabgangursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerabgangursprungDto(lagerabgangursprung);
	}

	/**
	 * Findet alle Lagerabgaenge die einem bestimmten Lagerzugang benoetigt
	 * haben
	 * 
	 * @param iIdBuchungsursprung
	 *            7
	 * @throws EJBExceptionLP
	 * @return LagerabgangursprungDto[]
	 */
	public LagerabgangursprungDto[] lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(
			Integer iIdBuchungsursprung) throws EJBExceptionLP {
		if (iIdBuchungsursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchungsursprung == null"));
		}

		Query query = em
				.createNamedQuery("LagerabgangursprungfindByILagerbewegungIIdursprung");
		query.setParameter(1, iIdBuchungsursprung);
		Collection<?> cl = query.getResultList();
		return assembleLagerabgangursprungDtos(cl);
	}

	/**
	 * Findet alle Lagerzugaenge die ein bestimmter Lagerabgang benoetigt hat
	 * 
	 * @param iIdBuchung
	 *            7
	 * @throws EJBExceptionLP
	 * @return LagerabgangursprungDto[]
	 */
	public LagerabgangursprungDto[] lagerabgangursprungFindByLagerbewegungIIdBuchung(
			Integer iIdBuchung) throws EJBExceptionLP {
		if (iIdBuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchung == null"));
		}
		Query query = em
				.createNamedQuery("LagerabgangursprungfindByILagerbewegungid");
		query.setParameter(1, iIdBuchung);
		Collection<?> cl = query.getResultList();
		return assembleLagerabgangursprungDtos(cl);
	}

	/**
	 * Findet den Urpsrung eines bestimmten Lagerzuganges
	 * 
	 * @param iIdBuchung
	 *            36
	 * @throws EJBExceptionLP
	 * @return LagerzugangursprungDto
	 */
	public LagerzugangursprungDto lagerzugangursprungFindIIdBuchungsursprungByLagerbewegungIIdBuchung(
			Integer iIdBuchung) throws EJBExceptionLP {
		if (iIdBuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchung == null"));
		}
		try {
			Query query = em
					.createNamedQuery("LagerzugangursprungfindIIdBuchungsursprungByILagerbewegungid");
			query.setParameter(1, iIdBuchung);
			Lagerzugangursprung lagerzugangursprung = (Lagerzugangursprung) query
					.getSingleResult();
			return assembleLagerzugangursprungDto(lagerzugangursprung);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setLagerabgangursprungFromLagerabgangursprungDto(
			Lagerabgangursprung lagerabgangursprung,
			LagerabgangursprungDto lagerabgangursprungDto) {
		lagerabgangursprung.setNVerbrauchtemenge(lagerabgangursprungDto
				.getNVerbrauchtemenge());
		lagerabgangursprung.setNGestehungspreis(lagerabgangursprungDto
				.getNGestehungspreis());

		em.merge(lagerabgangursprung);
		em.flush();
	}

	private LagerabgangursprungDto assembleLagerabgangursprungDto(
			Lagerabgangursprung lagerabgangursprung) {
		return LagerabgangursprungDtoAssembler.createDto(lagerabgangursprung);
	}

	private LagerabgangursprungDto[] assembleLagerabgangursprungDtos(
			Collection<?> lagerabgangursprungs) {
		List<LagerabgangursprungDto> list = new ArrayList<LagerabgangursprungDto>();
		if (lagerabgangursprungs != null) {
			Iterator<?> iterator = lagerabgangursprungs.iterator();
			while (iterator.hasNext()) {
				Lagerabgangursprung lagerabgangursprung = (Lagerabgangursprung) iterator
						.next();
				list.add(assembleLagerabgangursprungDto(lagerabgangursprung));
			}
		}
		LagerabgangursprungDto[] returnArray = new LagerabgangursprungDto[list
				.size()];
		return (LagerabgangursprungDto[]) list.toArray(returnArray);
	}

	/**
	 * Ordnet einem Lagerzugang einen Lagerabgang zu (Rueckschein 56 kommt
	 * urspruenglich auf Lieferschein 38).
	 * 
	 * @param lagerzugangursprungDto
	 *            LagerzugangursprungDto
	 * @throws EJBExceptionLP
	 */
	public void createLagerzugangursprung(
			LagerzugangursprungDto lagerzugangursprungDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (lagerzugangursprungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchung == null"));
		}
		if (lagerzugangursprungDto.getILagerbewegungid() == null
				|| lagerzugangursprungDto.getILagerbewegungidursprung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"lagerzugangursprungDto.getLagerbewegungIIdBuchung() == null || lagerzugangursprungDto.getLagerbewegungIIdBuchungsursprung() == null"));
		}
		try {
			Lagerzugangursprung lagerzugangursprung = null;
			try {
				lagerzugangursprung = new Lagerzugangursprung(
						lagerzugangursprungDto.getILagerbewegungid(),
						lagerzugangursprungDto.getILagerbewegungidursprung());
				em.persist(lagerzugangursprung);
				em.flush();

			} catch (EntityExistsException ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, ex);
			}

			setLagerzugangursprungFromLagerzugangursprungDto(
					lagerzugangursprung, lagerzugangursprungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Findet eine Zuordnung eines Lagerzuganges zu seinem Ursprung.
	 * 
	 * @param iIdBuchung
	 *            Integer
	 * @param iIdBuchungsursprung
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return LagerzugangursprungDto
	 */
	public LagerzugangursprungDto lagerzugangursprungFindByPrimaryKey(
			Integer iIdBuchung, Integer iIdBuchungsursprung)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iIdBuchung == null || iIdBuchungsursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"iIdBuchung == null || iIdBuchungsursprung == null"));
		}
		LagerzugangursprungPK lagerzugangursprungPK = new LagerzugangursprungPK();
		lagerzugangursprungPK.setILagerbewegungid(iIdBuchung);
		lagerzugangursprungPK.setILagerbewegungidursprung(iIdBuchungsursprung);
		Lagerzugangursprung lagerzugangursprung = em.find(
				Lagerzugangursprung.class, lagerzugangursprungPK);
		if (lagerzugangursprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerzugangursprungDto(lagerzugangursprung);
	}

	private void setLagerzugangursprungFromLagerzugangursprungDto(
			Lagerzugangursprung lagerzugangursprung,
			LagerzugangursprungDto lagerzugangursprungDto) {
		em.merge(lagerzugangursprung);
		em.flush();
	}

	private LagerzugangursprungDto assembleLagerzugangursprungDto(
			Lagerzugangursprung lagerzugangursprung) {
		return LagerzugangursprungDtoAssembler.createDto(lagerzugangursprung);
	}

	private LagerzugangursprungDto[] assembleLagerzugangursprungDtos(
			Collection<?> lagerzugangursprungs) {
		List<LagerzugangursprungDto> list = new ArrayList<LagerzugangursprungDto>();
		if (lagerzugangursprungs != null) {
			Iterator<?> iterator = lagerzugangursprungs.iterator();
			while (iterator.hasNext()) {
				Lagerzugangursprung lagerzugangursprung = (Lagerzugangursprung) iterator
						.next();
				list.add(assembleLagerzugangursprungDto(lagerzugangursprung));
			}
		}
		LagerzugangursprungDto[] returnArray = new LagerzugangursprungDto[list
				.size()];
		return (LagerzugangursprungDto[]) list.toArray(returnArray);
	}

	/**
	 * Liefert die verbrauchte Menge eines Lagerzuganges zurueck
	 * 
	 * @param lagerbewegungIId
	 *            Integer
	 * @return Double verbrauchte Menge
	 */
	public Double getVerbrauchteMenge(Integer lagerbewegungIId) {
		Integer iIdBuchung = getBuchungsNrUeberId(lagerbewegungIId);
		// Hole alle Buchungen, die diesen Lagerzugang benoetigen
		LagerabgangursprungDto[] dto = lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(iIdBuchung);

		double fMengeVerbraucht = 0;

		for (int i = 0; i < dto.length; i++) {
			fMengeVerbraucht = fMengeVerbraucht
					+ Helper.rundeKaufmaennisch(dto[i].getNVerbrauchtemenge(),
							4).doubleValue();
		}

		return new Double(fMengeVerbraucht);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeQuickLagerstandGegenEchtenLagerstand(
			Integer artikelIIdInput, TheClientDto theClientDto) {

		LagerDto[] lagerDtos = lagerFindByMandantCNr(theClientDto.getMandant());

		String[] handartikel = new String[1];
		handartikel[0] = ArtikelFac.ARTIKELART_HANDARTIKEL;

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRArtikel.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		// crit.add(Restrictions.eq("i_id", 6901));
		crit.add(Restrictions.not(Restrictions.in(
				ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, handartikel)));
		crit.add(Expression.eq(ArtikelFac.FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET,
				new Short((short) 1)));

		if (artikelIIdInput != null) {
			crit.add(Restrictions.eq("i_id", artikelIIdInput));
		}

		crit.addOrder(Order.asc("c_nr"));
		List<?> resultList = crit.list();

		int zaehler = 1;
		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "ZAEHLER;ARTIKEL_I_ID;ARTIKEL_C_NR;LAGER;QUICK;LAGERSTAND"
				+ new String(CRLFAscii);
		String sql = "";
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRArtikel flrArtikel = (FLRArtikel) resultListIterator.next();
			for (int i = 0; i < lagerDtos.length; i++) {
				LagerDto lagerDto = lagerDtos[i];

				String ausgabe = flrArtikel.getI_id() + ";"
						+ flrArtikel.getC_nr() + ";";

				ausgabe += lagerDto.getCNr() + ";";
				BigDecimal quicklagerstand = new BigDecimal(0);
				Artikellager artikellager = em.find(
						Artikellager.class,
						new ArtikellagerPK(flrArtikel.getI_id(), lagerDto
								.getIId()));
				if (artikellager == null) {
					ausgabe += "0;";
				} else {
					quicklagerstand = artikellager.getNLagerstand();
				}
				ausgabe += quicklagerstand.toString() + ";";
				BigDecimal echterlagerstand = getLagerstandZumZeitpunkt(
						flrArtikel.getI_id(), lagerDto.getIId(),
						new java.sql.Timestamp(
								System.currentTimeMillis() + 3600000 * 24),
						theClientDto);
				ausgabe += echterlagerstand.toString() + ";";

				if (quicklagerstand.doubleValue() != echterlagerstand
						.doubleValue()) {
					rueckgabe += zaehler + ";" + ausgabe
							+ new String(CRLFAscii);

					System.out.println(zaehler + ";" + ausgabe);

					String update = "UPDATE WW_ARTIKELLAGER SET N_LAGERSTAND="
							+ echterlagerstand.doubleValue()
							+ " WHERE ARTIKEL_I_ID=" + flrArtikel.getI_id()
							+ " AND LAGER_I_ID=" + lagerDto.getIId();
					System.out.println(update + ";" + new String(CRLFAscii));

					sql += update + ";" + new String(CRLFAscii);

					zaehler++;
				}
			}
		}
		session.close();
		return rueckgabe + sql;
	}

	public void pruefeQuickLagerstandGegenEchtenLagerstandUndFuehreAus(
			Integer artikelIIdInput, TheClientDto theClientDto) {

		LagerDto[] lagerDtos = lagerFindByMandantCNr(theClientDto.getMandant());

		String[] handartikel = new String[1];
		handartikel[0] = ArtikelFac.ARTIKELART_HANDARTIKEL;

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRArtikel.class);
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		// crit.add(Restrictions.eq("i_id", 6901));
		crit.add(Restrictions.not(Restrictions.in(
				ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, handartikel)));
		crit.add(Expression.eq(ArtikelFac.FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET,
				new Short((short) 1)));

		if (artikelIIdInput != null) {
			crit.add(Restrictions.eq("i_id", artikelIIdInput));
		}

		crit.addOrder(Order.asc("c_nr"));
		List<?> resultList = crit.list();

		int zaehler = 1;
		byte[] CRLFAscii = { 13, 10 };

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRArtikel flrArtikel = (FLRArtikel) resultListIterator.next();
			for (int i = 0; i < lagerDtos.length; i++) {
				LagerDto lagerDto = lagerDtos[i];

				String ausgabe = flrArtikel.getI_id() + ";"
						+ flrArtikel.getC_nr() + ";";

				ausgabe += lagerDto.getCNr() + ";";
				BigDecimal quicklagerstand = new BigDecimal(0);
				Artikellager artikellager = em.find(
						Artikellager.class,
						new ArtikellagerPK(flrArtikel.getI_id(), lagerDto
								.getIId()));
				if (artikellager == null) {
					ausgabe += "0;";
				} else {
					quicklagerstand = artikellager.getNLagerstand();
				}
				ausgabe += quicklagerstand.toString() + ";";
				BigDecimal echterlagerstand = getLagerstandZumZeitpunkt(
						flrArtikel.getI_id(), lagerDto.getIId(),
						new java.sql.Timestamp(
								System.currentTimeMillis() + 3600000 * 24),
						theClientDto);
				ausgabe += echterlagerstand.toString() + ";";

				if (quicklagerstand.doubleValue() != echterlagerstand
						.doubleValue()) {

					// System.out.println(zaehler + ";" + ausgabe);

					String update = "UPDATE WW_ARTIKELLAGER SET N_LAGERSTAND="
							+ echterlagerstand.doubleValue()
							+ " WHERE ARTIKEL_I_ID=" + flrArtikel.getI_id()
							+ " AND LAGER_I_ID=" + lagerDto.getIId();
					System.out.println(update + ";--BEREITS_AUSGEFUEHRT"
							+ new String(CRLFAscii));

					artikellager.setNLagerstand(echterlagerstand);
					em.flush();

					zaehler++;
				}
			}
		}
		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeBelegeMitLagerbewegungen(TheClientDto theClientDto) {
		// Belege: "Inventur       "Losablieferung "
		// "Gutschrift     "

		byte[] CRLFAscii = { 13, 10 };
		int zaehler = 1;
		String rueckgabe = "ZAEHLER;BELEGART;POSITION" + new String(CRLFAscii);
		Session session = FLRSessionFactory.getFactory().openSession();
		// ----------------------HAND-------------------
		String queryString = "SELECT position.i_id"
				+ " FROM FLRHandlagerbewegung AS position"
				+ " ORDER BY position.i_id ASC";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Integer positionIId = (Integer) resultListIterator.next();
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			if (sessionSub
					.createQuery(
							"SELECT lagerbewegung.i_id FROM FLRLagerbewegung as lagerbewegung WHERE lagerbewegung.c_belegartnr='"
									+ LocaleFac.BELEGART_HAND
									+ "'"
									+ " AND lagerbewegung.i_belegartpositionid="
									+ positionIId).list().size() == 0) {
				rueckgabe += zaehler + ";" + LocaleFac.BELEGART_HAND + ";"
						+ positionIId + new String(CRLFAscii);
				zaehler++;
			}

			System.out.println(LocaleFac.BELEGART_HAND + " " + positionIId);
			sessionSub.close();
		}

		// ------------------LOS---------------
		queryString = "SELECT position.i_id"
				+ " FROM FLRLosistmaterial AS position"
				+ " ORDER BY position.i_id ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Integer positionIId = (Integer) resultListIterator.next();
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			if (sessionSub
					.createQuery(
							"SELECT lagerbewegung.i_id FROM FLRLagerbewegung as lagerbewegung WHERE lagerbewegung.c_belegartnr='"
									+ LocaleFac.BELEGART_LOS
									+ "'"
									+ " AND lagerbewegung.i_belegartpositionid="
									+ positionIId).list().size() == 0) {
				rueckgabe += zaehler + ";" + LocaleFac.BELEGART_LOS + ";"
						+ positionIId + new String(CRLFAscii);
				zaehler++;
			}

			System.out.println(LocaleFac.BELEGART_LOS + " " + positionIId);
			sessionSub.close();
		}
		// ------------------LIEFERSCHEIN---------------
		queryString = "SELECT position.i_id"
				+ " FROM FLRLieferscheinposition AS position WHERE position.positionsart_c_nr='"
				+ com.lp.server.lieferschein.service.LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
				+ "'" + " ORDER BY position.i_id ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Integer positionIId = (Integer) resultListIterator.next();
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			if (sessionSub
					.createQuery(
							"SELECT lagerbewegung.i_id FROM FLRLagerbewegung as lagerbewegung WHERE lagerbewegung.c_belegartnr='"
									+ LocaleFac.BELEGART_LIEFERSCHEIN
									+ "'"
									+ " AND lagerbewegung.i_belegartpositionid="
									+ positionIId).list().size() == 0) {
				rueckgabe += zaehler + ";" + LocaleFac.BELEGART_LIEFERSCHEIN
						+ ";" + positionIId + new String(CRLFAscii);
				zaehler++;
			}

			System.out.println(LocaleFac.BELEGART_LIEFERSCHEIN + " "
					+ positionIId);
			sessionSub.close();
		}
		// ------------------RECHNUNG---------------
		queryString = "SELECT position.i_id"
				+ " FROM FLRRechnungPosition AS position WHERE position.positionsart_c_nr='"
				+ com.lp.server.rechnung.service.RechnungFac.POSITIONSART_RECHNUNG_IDENT
				+ "'" + " ORDER BY position.i_id ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Integer positionIId = (Integer) resultListIterator.next();
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			if (sessionSub
					.createQuery(
							"SELECT lagerbewegung.i_id FROM FLRLagerbewegung as lagerbewegung WHERE lagerbewegung.c_belegartnr='"
									+ LocaleFac.BELEGART_RECHNUNG
									+ "'"
									+ " AND lagerbewegung.i_belegartpositionid="
									+ positionIId).list().size() == 0) {
				rueckgabe += zaehler + ";" + LocaleFac.BELEGART_RECHNUNG + ";"
						+ positionIId + new String(CRLFAscii);
				zaehler++;
			}

			System.out.println(LocaleFac.BELEGART_RECHNUNG + " " + positionIId);
			sessionSub.close();
		}
		// ------------------BESTELLUNG---------------
		queryString = "SELECT position.i_id"
				+ " FROM FLRBestellposition AS position WHERE position.bestellpositionart_c_nr='"
				+ com.lp.server.bestellung.service.BestellpositionFac.BESTELLPOSITIONART_IDENT
				+ "'" + " ORDER BY position.i_id ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Integer positionIId = (Integer) resultListIterator.next();
			Session sessionSub = FLRSessionFactory.getFactory().openSession();
			if (sessionSub
					.createQuery(
							"SELECT lagerbewegung.i_id FROM FLRLagerbewegung as lagerbewegung WHERE lagerbewegung.c_belegartnr='"
									+ LocaleFac.BELEGART_BESTELLUNG
									+ "'"
									+ " AND lagerbewegung.i_belegartpositionid="
									+ positionIId).list().size() == 0) {
				rueckgabe += zaehler + ";" + LocaleFac.BELEGART_BESTELLUNG
						+ ";" + positionIId + new String(CRLFAscii);
				zaehler++;
			}

			System.out.println(LocaleFac.BELEGART_BESTELLUNG + " "
					+ positionIId);
			sessionSub.close();
		}
		session.close();
		return rueckgabe;
	}

	/**
	 * Prueft, ob die Lagerbewegungen mit den Belegartpositionen konsistent sind
	 * 
	 * @param theClientDto
	 *            String
	 * @return String
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeLagerbewegungenMitBelege(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class);
		crit.addOrder(Order.asc(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG));
		crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

		List<?> resultList = crit.list();
		int lastColumn = -1;
		int zaehler = 1;
		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "ZAEHLER;BELEGART;BELEGARTPOSITIOND;"
				+ new String(CRLFAscii);
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();

			if (lastColumn == flrLagerbewegung.getI_id_buchung().intValue()) {
			} else {
				double nMenge = flrLagerbewegung.getN_menge().doubleValue();
				if (nMenge != 0) {
					// ------------------HAND---------------
					if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_HAND)) {
						Handlagerbewegung hb = em.find(Handlagerbewegung.class,
								flrLagerbewegung.getI_belegartpositionid());
						if (hb != null) {
							if (hb.getNMenge().doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_HAND
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} else {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_HAND
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------LIEFERSCHEIN---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LIEFERSCHEIN)) {
						try {
							BigDecimal nPosMenge = getLieferscheinpositionFac()
									.lieferscheinpositionFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid(),
											theClientDto).getNMenge();

							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_LIEFERSCHEIN
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_LIEFERSCHEIN
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------RECHNUNG---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_RECHNUNG)) {
						try {
							BigDecimal nPosMenge = getRechnungFac()
									.rechnungPositionFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid())
									.getNMenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_RECHNUNG
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}

						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_RECHNUNG
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------BESTELLUNG---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_BESTELLUNG)) {
						try {
							BigDecimal nPosMenge = getBestellpositionFac()
									.bestellpositionFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid())
									.getNMenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_BESTELLUNG
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_BESTELLUNG
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------INVENTUR---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_INVENTUR)) {
						try {
							BigDecimal nPosMenge = getInventurFac()
									.inventurlisteFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid(),
											theClientDto).getNInventurmenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_INVENTUR
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_INVENTUR
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------GUTSCHRIFT---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_GUTSCHRIFT)) {
						try {
							BigDecimal nPosMenge = getRechnungFac()
									.rechnungPositionFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid())
									.getNMenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_GUTSCHRIFT
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_GUTSCHRIFT
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------LOSABLIEFERUNG---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LOSABLIEFERUNG)) {
						try {
							BigDecimal nPosMenge = getFertigungFac()
									.losablieferungFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid(),
											false, theClientDto).getNMenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_LOSABLIEFERUNG
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_LOSABLIEFERUNG
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
					// ------------------LOS---------------
					else if (flrLagerbewegung.getC_belegartnr().equals(
							LocaleFac.BELEGART_LOS)) {
						try {
							BigDecimal nPosMenge = getFertigungFac()
									.lossollmaterialFindByPrimaryKey(
											flrLagerbewegung
													.getI_belegartpositionid())
									.getNMenge();
							if (nPosMenge != null
									&& nPosMenge.doubleValue() != nMenge) {
								rueckgabe += zaehler
										+ ";"
										+ LocaleFac.BELEGART_LOS
										+ ";"
										+ flrLagerbewegung
												.getI_belegartpositionid()
										+ new String(CRLFAscii);
								zaehler++;
							}
						} catch (Throwable ex1) {
							rueckgabe += zaehler
									+ ";"
									+ LocaleFac.BELEGART_LOS
									+ ";"
									+ flrLagerbewegung
											.getI_belegartpositionid()
									+ new String(CRLFAscii);
							zaehler++;
						}
					}
				}

				lastColumn = flrLagerbewegung.getI_id_buchung().intValue();
			}
		}
		session.close();
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeIIdBuchungen(Integer artikelIId,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(lagerbewegung.i_id_buchung)"
				+ " FROM FLRLagerbewegung AS lagerbewegung ";

		if (artikelIId != null) {
			queryString += " WHERE lagerbewegung.artikel_i_id= " + artikelIId;
		}

		queryString += " ORDER BY lagerbewegung.i_id_buchung ASC ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();

		byte[] CRLFAscii = { 13, 10 };

		String rueckgabe = "BUCHUNGS_I_ID;" + new String(CRLFAscii);
		Iterator<?> resultListIterator = resultList.iterator();

		int zaehler = 1;
		while (resultListIterator.hasNext()) {
			Integer i_idBuchung = (Integer) resultListIterator.next();

			Query query = em.createNamedQuery("LagerbewegungfindByIIdBuchung");
			query.setParameter(1, i_idBuchung);
			Collection<?> cl = query.getResultList();
			if (cl.size() > 1) {
				Iterator it = cl.iterator();

				HashMap hmZeile = new HashMap();
				int i = 0;

				if (it.hasNext()) {
					Lagerbewegung l = (Lagerbewegung) it.next();
					String s = l.getCBelegartnr() + ";"
							+ l.getIBelegartpositionid();
					hmZeile.put(s, s);
				}

				while (it.hasNext()) {
					Lagerbewegung l = (Lagerbewegung) it.next();
					String s = l.getCBelegartnr() + ";"
							+ l.getIBelegartpositionid();

					if (!hmZeile.containsKey(s)) {
						rueckgabe += i_idBuchung + new String(CRLFAscii);
						System.out.println("I_ID_BUCHUNG: " + i_idBuchung);
						zaehler++;
						break;
					}

					i++;

				}
			}

		}
		session.close();
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeLagerabgangurpsrung(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(lagerbewegung.i_id_buchung)"
				+ " FROM FLRLagerbewegung AS lagerbewegung "
				+ " ORDER BY lagerbewegung.i_id_buchung ASC ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();

		byte[] CRLFAscii = { 13, 10 };

		String rueckgabe = "ZAEHLER;BUCHUNGS_I_ID;MENGEAUSLAGERBEWEGUNG;MENGEAUSABGANGSURSPRUNG"
				+ new String(CRLFAscii);
		Iterator<?> resultListIterator = resultList.iterator();

		int zaehler = 1;
		while (resultListIterator.hasNext()) {
			Integer i_idBuchung = (Integer) resultListIterator.next();

			LagerbewegungDto lagerbewegungDto = getJuengsteBuchungEinerBuchungsNummer(i_idBuchung);
			BigDecimal mengeAusLagerbewegung = lagerbewegungDto.getNMenge();
			if (Helper.short2boolean(lagerbewegungDto.getBAbgang()) == true) {

				BigDecimal abgangsmengeAusLagerabgangUrsprung = new BigDecimal(
						0);
				String ausgabe = "";
				Query query = em
						.createNamedQuery("LagerabgangursprungfindByILagerbewegungid");
				query.setParameter(1, i_idBuchung);
				Collection<?> cl = query.getResultList();
				// if (! cl.isEmpty()) {
				LagerabgangursprungDto[] dtos = assembleLagerabgangursprungDtos(cl);
				for (int i = 0; i < dtos.length; i++) {
					LagerabgangursprungDto dto = dtos[i];
					abgangsmengeAusLagerabgangUrsprung = abgangsmengeAusLagerabgangUrsprung
							.add(dto.getNVerbrauchtemenge());

					Query query2 = em
							.createNamedQuery("LagerbewegungfindByIIdBuchung");
					query2.setParameter(1, dto.getILagerbewegungidursprung());
					Collection<?> c = query2.getResultList();
					if (c.size() == 0) {
						ausgabe += "URSPRUNGNICHTGEFUNDEN: " + zaehler + ";"
								+ dto.getILagerbewegungidursprung();
						rueckgabe += ausgabe + new String(CRLFAscii);
						System.out.println(ausgabe);
						zaehler++;

					}
				}
				if (abgangsmengeAusLagerabgangUrsprung.doubleValue() > mengeAusLagerbewegung
						.doubleValue()) {
					ausgabe += "ABGANGURSPRUNG: " + zaehler + ";" + i_idBuchung
							+ ";" + mengeAusLagerbewegung + ";"
							+ abgangsmengeAusLagerabgangUrsprung;
					rueckgabe += ausgabe + new String(CRLFAscii);
					System.out.println(ausgabe);
					zaehler++;
				} else {
					System.out.println(i_idBuchung);
				}
			} else {

				BigDecimal verbrauchteAusLagerabgangUrsprung = new BigDecimal(0);
				String ausgabe = "";
				Query query2 = em
						.createNamedQuery("LagerabgangursprungfindByILagerbewegungIIdursprung");
				query2.setParameter(1, i_idBuchung);
				Collection<?> cl = query2.getResultList();
				// if (! cl.isEmpty()) {
				LagerabgangursprungDto[] dtos = assembleLagerabgangursprungDtos(cl);
				for (int i = 0; i < dtos.length; i++) {
					LagerabgangursprungDto dto = dtos[i];
					verbrauchteAusLagerabgangUrsprung = verbrauchteAusLagerabgangUrsprung
							.add(dto.getNVerbrauchtemenge());
				}
				if (verbrauchteAusLagerabgangUrsprung.doubleValue() > mengeAusLagerbewegung
						.doubleValue()) {
					ausgabe += "VERBRAUCHTEMENGEDESZUGANGS: " + zaehler + ";"
							+ i_idBuchung + ";" + mengeAusLagerbewegung + ";"
							+ verbrauchteAusLagerabgangUrsprung;
					rueckgabe += ausgabe + new String(CRLFAscii);
					System.out.println(ausgabe);
					zaehler++;
				} else {
					System.out.println(i_idBuchung);
				}
			}
		}
		session.close();
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeVerbrauchteMenge(TheClientDto theClientDto) {

		System.out
				.println("ZAEHLER;I_ID_BUCHUNG;ARTIKEL_I_ID;ARTIKEL_C_NR;POSITIONSMENGE;VERBRAUCHTEMENGE");
		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "ZAEHLER;I_ID_BUCHUNG;ARTIKEL_I_ID;ARTIKEL_C_NR;POSITIONSMENGE;VERBRAUCHTEMENGE"
				+ new String(CRLFAscii);
		String[] handartikel = new String[1];
		handartikel[0] = ArtikelFac.ARTIKELART_HANDARTIKEL;

		Session sessionArtikel = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria critArtikel = sessionArtikel
				.createCriteria(FLRArtikel.class);
		critArtikel.add(Restrictions.eq("mandant_c_nr",
				theClientDto.getMandant()));
		// crit.add(Restrictions.eq("i_id", 13995));
		critArtikel.add(Restrictions.not(Restrictions.in(
				ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR, handartikel)));
		critArtikel.add(Expression.eq(
				ArtikelFac.FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET, new Short(
						(short) 1)));

		critArtikel.addOrder(Order.asc("c_nr"));
		List<?> resultListArtikel = critArtikel.list();
		Iterator<?> resultListIteratorArtikel = resultListArtikel.iterator();
		int row = 0;

		while (resultListIteratorArtikel.hasNext()) {
			FLRArtikel flrArtikel = (FLRArtikel) resultListIteratorArtikel
					.next();

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			session = factory.openSession();

			org.hibernate.Criteria crit = session.createCriteria(
					FLRLagerbewegung.class).createAlias(
					LagerFac.FLR_LAGERPLAETZE_FLRLAGER, "l");
			crit.add(Expression.not(Expression.in("l.c_nr",
					new String[] { LagerFac.LAGER_KEINLAGER })));
			crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
					Helper.boolean2Short(true)));
			crit.add(Expression.eq(LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID,
					flrArtikel.getI_id()));

			crit.addOrder(Order.asc(LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG))
					.addOrder(
							Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();

			int lastColumn = -1;

			while (resultListIterator.hasNext()) {
				FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
						.next();

				if (lastColumn == lagerbewegung.getI_id_buchung().intValue()) {
				} else {

					BigDecimal verbrauchteMenge = new BigDecimal(0);

					Query query = em
							.createNamedQuery("LagerabgangursprungfindByILagerbewegungid");
					query.setParameter(1, lagerbewegung.getI_id_buchung());
					Collection<?> cl = query.getResultList();
					// if (cl.isEmpty()) {
					// verbrauchteMenge = new BigDecimal(0);
					// }
					// else {
					LagerabgangursprungDto[] dtos = assembleLagerabgangursprungDtos(cl);

					for (int i = 0; i < dtos.length; i++) {
						verbrauchteMenge = verbrauchteMenge.add(dtos[i]
								.getNVerbrauchtemenge());
					}

					if (lagerbewegung.getN_menge().compareTo(verbrauchteMenge) != 0) {
						row++;

						String zeile = row + ";"
								+ lagerbewegung.getI_id_buchung() + ";"
								+ lagerbewegung.getArtikel_i_id() + ";"
								+ lagerbewegung.getFlrartikel().getC_nr() + ";"
								+ lagerbewegung.getN_menge() + ";"
								+ verbrauchteMenge;
						System.out.println(zeile);

						rueckgabe += zeile + new String(CRLFAscii);

					}
					lastColumn = lagerbewegung.getI_id_buchung().intValue();
				}
			}

			session.close();
		}
		sessionArtikel.close();
		return rueckgabe;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void konstruiereLagergewegungenLSREAusBelegen(
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		// ----------------------RECHNUNG-------------------
		String queryString = " FROM FLRRechnungPosition AS position WHERE position.positionsart_c_nr='"
				+ LocaleFac.POSITIONSART_IDENT
				+ "' AND position.flrartikel.artikelart_c_nr='"
				+ ArtikelFac.ARTIKELART_ARTIKEL
				+ "'"
				+ " ORDER BY position.flrrechnung.d_belegdatum ASC";
		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();
		System.out.println("RECHNUNG: Gesamt-Positionen " + resultList.size());

		int ilfdNrSnrChnr = 99;

		while (resultListIterator.hasNext()) {
			com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition position = (com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition) resultListIterator
					.next();

			RechnungDto rechnungDto = null;
			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
						position.getFlrrechnung().getI_id());

				RechnungPositionDto rpDto = getRechnungFac()
						.rechnungPositionFindByPrimaryKey(position.getI_id());

				boolean bSnrChnrTragend = false;
				if (Helper.short2boolean(position.getFlrartikel()
						.getB_seriennrtragend()) == true
						|| Helper.short2boolean(position.getFlrartikel()
								.getB_chargennrtragend()) == true) {
					bSnrChnrTragend = true;

				}
				HashMap hmSNRMitVersion = new HashMap();
				List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
				if (bSnrChnrTragend == true) {
					String snr = position.getC_snrchnr_mig();
					if (snr != null && snr.length() > 0) {
						String[] sTeile = snr.split(",");
						alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
						for (int i = 0; i < sTeile.length; i++) {
							String[] sSnrVersion = sTeile[i].split(":");
							SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
									sSnrVersion[0], new BigDecimal(1));
							alSeriennrChargennrMitMenge.add(sDto);
							if (sSnrVersion.length > 1) {
								hmSNRMitVersion.put(sSnrVersion[0],
										sSnrVersion[1]);
							}

						}

					} else {
						// Nun SNR generieren
						alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
						for (int i = 0; i < position.getN_menge().intValue(); i++) {

							SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
									"RE" + position.getFlrrechnung().getC_nr()
											+ "#" + ilfdNrSnrChnr,
									new BigDecimal(1));
							alSeriennrChargennrMitMenge.add(sDto);

							ilfdNrSnrChnr++;
						}

					}
				}

				rpDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);

				if (position.getN_menge().doubleValue() > 0
						&& !position.getFlrrechnung().getFlrrechnungart()
								.getRechnungtyp_c_nr()
								.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					// Material zuerst per Hand zubuchen

					HandlagerbewegungDto handlagerbewegung2Dto = new HandlagerbewegungDto();
					handlagerbewegung2Dto.setArtikelIId(position
							.getArtikel_i_id());
					handlagerbewegung2Dto

					.setLagerIId(rechnungDto.getLagerIId());
					handlagerbewegung2Dto.setBAbgang(new Short((short) 0));
					handlagerbewegung2Dto.setCKommentar("Zubuchung");
					handlagerbewegung2Dto
							.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
					handlagerbewegung2Dto.setNMenge(position.getN_menge());
					handlagerbewegung2Dto
							.setNEinstandspreis(position
									.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt());
					getLagerFac().createHandlagerbewegung(
							handlagerbewegung2Dto, theClientDto);
				}

				getRechnungFac().bucheRechnungPositionAmLager(rpDto,
						rechnungDto.getLagerIId(), false, theClientDto);

				if (bSnrChnrTragend == true) {
					Iterator it = hmSNRMitVersion.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						String version = (String) hmSNRMitVersion.get(key);
						getLagerFac().versionInLagerbewegungUpdaten(
								LocaleFac.BELEGART_RECHNUNG,
								position.getI_id(), key, version);
					}
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			System.out.println("RECHNUNG-ID: " + position.getI_id());
		}

		session.close();
		session = FLRSessionFactory.getFactory().openSession();
		// ----------------------LIEFERSCHEIN-------------------

		queryString = " FROM FLRLieferscheinposition AS position WHERE position.positionsart_c_nr='"
				+ LocaleFac.POSITIONSART_IDENT
				+ "' AND position.flrartikel.artikelart_c_nr='"
				+ ArtikelFac.ARTIKELART_ARTIKEL
				+ "'"
				+ " ORDER BY position.flrlieferschein.t_liefertermin ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();
		System.out.println("LIEFERSCHEIN: Gesamt-Positionen "
				+ resultList.size());

		while (resultListIterator.hasNext()) {
			com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition position = (com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition) resultListIterator
					.next();

			String snr = position.getC_snrchnr_mig();

			boolean bSnrChnrTragend = false;
			if (Helper.short2boolean(position.getFlrartikel()
					.getB_seriennrtragend()) == true
					|| Helper.short2boolean(position.getFlrartikel()
							.getB_chargennrtragend()) == true) {
				bSnrChnrTragend = true;

			}
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
			HashMap hmSNRMitVersion = new HashMap();
			if (bSnrChnrTragend) {

				if (snr != null && snr.length() > 0) {

					String[] sTeile = snr.split(",");
					alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
					for (int i = 0; i < sTeile.length; i++) {

						String[] sSnrVersion = sTeile[i].split(":");

						SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
								sSnrVersion[0], new BigDecimal(1));
						alSeriennrChargennrMitMenge.add(sDto);
						if (sSnrVersion.length > 1) {
							hmSNRMitVersion.put(sSnrVersion[0], sSnrVersion[1]);
						}
					}

				} else {
					// Generieren
					// Nun SNR generieren
					alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
					for (int i = 0; i < position.getN_menge().intValue(); i++) {

						SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
								"LS" + position.getFlrlieferschein().getC_nr()
										+ "#" + ilfdNrSnrChnr,
								new BigDecimal(1));
						alSeriennrChargennrMitMenge.add(sDto);

						ilfdNrSnrChnr++;
					}
				}
			}

			LieferscheinpositionDto lsposDto = null;
			try {
				lsposDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(
								position.getI_id(), theClientDto);
				lsposDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);

				if (alSeriennrChargennrMitMenge != null) {
					if ((double) alSeriennrChargennrMitMenge.size() != position
							.getN_menge().doubleValue()) {

						// mit Wh besprochen: Pos-Menge aendern:
						lsposDto.setNMenge(new BigDecimal(
								alSeriennrChargennrMitMenge.size()));

						String s = "Positionsmenge stimmt mit SNR-Menge nicht zusammen: LSPOS_I_ID:"
								+ position.getI_id()
								+ " POS-MENGE:"
								+ position.getN_menge().doubleValue()
								+ " SNR-ANZAHL:"
								+ alSeriennrChargennrMitMenge.size();
						System.out.println(s);
						myLogger.logKritisch(s);
					}
				}

			} catch (RemoteException ex2) {
				throwEJBExceptionLPRespectOld(ex2);
			}
			try {

				if (lsposDto.getNMenge().doubleValue() > 0) {
					// Material zuerst per Hand zubuchen

					HandlagerbewegungDto handlagerbewegung2Dto = new HandlagerbewegungDto();
					handlagerbewegung2Dto.setArtikelIId(position
							.getFlrartikel().getI_id());
					handlagerbewegung2Dto.setLagerIId(position
							.getFlrlieferschein().getLager_i_id());
					handlagerbewegung2Dto.setBAbgang(new Short((short) 0));
					handlagerbewegung2Dto.setCKommentar("Zubuchung");
					handlagerbewegung2Dto
							.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
					handlagerbewegung2Dto.setNMenge(lsposDto.getNMenge());
					handlagerbewegung2Dto
							.setNEinstandspreis(position
									.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt());

					getLagerFac().createHandlagerbewegung(
							handlagerbewegung2Dto, theClientDto);

				}

				if (lsposDto.getNMenge().doubleValue() != 0) {
					if (lsposDto.getNMenge().doubleValue() > 0) {

						getLieferscheinpositionFac().bucheAbLager(lsposDto,
								theClientDto);

					} else {
						getLieferscheinpositionFac().bucheZuLager(lsposDto,
								theClientDto);
					}
				}

				if (bSnrChnrTragend == true) {
					Iterator it = hmSNRMitVersion.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						String version = (String) hmSNRMitVersion.get(key);
						getLagerFac().versionInLagerbewegungUpdaten(
								LocaleFac.BELEGART_LIEFERSCHEIN,
								position.getI_id(), key, version);
					}
				}

			} catch (RemoteException ex3) {
				ex3.printStackTrace();
			}

			System.out.println("LSPOS-ID: " + position.getI_id());
		}

		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void konstruiereLagergewegungenBESTAusBelegen(
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		// ----------------------BESTELLUNG-------------------
		String queryString = " FROM FLRWareneingangspositionen AS position WHERE position.flrbestellposition.flrartikel.i_id IS NOT NULL ORDER BY position.i_id ASC";
		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();
		System.out.println("WARENEINGANG: Gesamt-Positionen "
				+ resultList.size());
		while (resultListIterator.hasNext()) {
			FLRWareneingangspositionen position = (FLRWareneingangspositionen) resultListIterator
					.next();
			try {
				getWareneingangFac().bucheWareneingangspositionAmLager(
						getWareneingangFac()
								.wareneingangspositionFindByPrimaryKey(
										position.getI_id()), false,
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			System.out.println("WARENEINGANG-ID: " + position.getI_id());
		}

		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void konstruiereLagergewegungenHAND(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		// ----------------------BESTELLUNG-------------------
		String queryString = " FROM FLRHandlagerbewegung AS position ORDER BY position.i_id ASC";
		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();
		System.out.println("HAND: Gesamt-Positionen " + resultList.size());
		int ilfdNrSnrChnr = 99;
		while (resultListIterator.hasNext()) {
			FLRHandlagerbewegung position = (FLRHandlagerbewegung) resultListIterator
					.next();
			try {
				HandlagerbewegungDto handlagerbewegung = getLagerFac()
						.handlagerbewegungFindByPrimaryKey(position.getI_id(),
								theClientDto);
				// Lagerbewegung durchfuehren

				LagerbewegungDto lDto = getLagerFac().getLetzteintrag(
						LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
						null);

				if (handlagerbewegung.getNMenge().doubleValue() > 0
						&& lDto == null) {

					boolean bSnrChnrTragend = false;
					if (Helper.short2boolean(position.getFlrartikel()
							.getB_seriennrtragend()) == true
							|| Helper.short2boolean(position.getFlrartikel()
									.getB_chargennrtragend()) == true) {
						bSnrChnrTragend = true;

					}

					HashMap hmSNRMitVersion = new HashMap();
					List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;
					String snr = position.getC_snrchnr_mig();

					if (bSnrChnrTragend == true) {

						if (snr != null && snr.length() > 0) {
							String[] sTeile = snr.split(",");
							alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							for (int i = 0; i < sTeile.length; i++) {
								String[] sSnrVersion = sTeile[i].split(":");
								SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
										sSnrVersion[0], new BigDecimal(1));
								alSeriennrChargennrMitMenge.add(sDto);
								if (sSnrVersion.length > 1) {
									hmSNRMitVersion.put(sSnrVersion[0],
											sSnrVersion[1]);
								}

							}

						} else {
							// Nun SNR generieren
							alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							for (int i = 0; i < position.getN_menge()
									.intValue(); i++) {

								SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
										"HAND" + position.getI_id() + "#"
												+ ilfdNrSnrChnr,
										new BigDecimal(1));
								alSeriennrChargennrMitMenge.add(sDto);

								ilfdNrSnrChnr++;
							}

						}
						handlagerbewegung
								.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
					}

					if (Helper.short2boolean(handlagerbewegung.getBAbgang())) {

						getLagerFac().bucheAb(
								LocaleFac.BELEGART_HAND,
								handlagerbewegung.getIId(),
								handlagerbewegung.getIId(),
								handlagerbewegung.getArtikelIId(),
								handlagerbewegung.getNMenge(),
								handlagerbewegung.getNVerkaufspreis(),
								handlagerbewegung.getLagerIId(),
								handlagerbewegung
										.getSeriennrChargennrMitMenge(),
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto);
					} else {

						getLagerFac().bucheZu(
								LocaleFac.BELEGART_HAND,
								handlagerbewegung.getIId(),
								handlagerbewegung.getIId(),
								handlagerbewegung.getArtikelIId(),
								handlagerbewegung.getNMenge(),
								handlagerbewegung.getNEinstandspreis(),
								handlagerbewegung.getLagerIId(),
								handlagerbewegung
										.getSeriennrChargennrMitMenge(),
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto);

					}
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			System.out.println("HAND-ID: " + position.getI_id());
		}

		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void konstruiereLagergewegungenLOSAusBelegen(
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		// ----------------------LOSISTMATERIAL-------------------
		String queryString = " FROM FLRLosistmaterial AS li ORDER BY li.i_id";
		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();

		System.out.println("LOSISTMATERIAL: Gesamt-Positionen "
				+ resultList.size());
		while (resultListIterator.hasNext()) {
			FLRLosistmaterial li = (FLRLosistmaterial) resultListIterator
					.next();

			if (Helper.short2boolean(li.getB_abgang()) == true) {
				if (li.getN_menge().doubleValue() > 0) { // Material zuerst per
															// Hand zubuchen

					HandlagerbewegungDto handlagerbewegung2Dto = new HandlagerbewegungDto();
					handlagerbewegung2Dto.setArtikelIId(li
							.getFlrlossollmaterial().getFlrartikel().getI_id());
					handlagerbewegung2Dto

					.setLagerIId(li.getLager_i_id());
					handlagerbewegung2Dto.setBAbgang(new Short((short) 0));
					handlagerbewegung2Dto
							.setCKommentar("Los-MaterialZubuchung");
					handlagerbewegung2Dto.setSeriennrChargennrMitMenge(null);
					handlagerbewegung2Dto.setNMenge(li.getN_menge());
					handlagerbewegung2Dto.setNEinstandspreis(li
							.getFlrlossollmaterial().getN_sollpreis());
					try {
						getLagerFac().createHandlagerbewegung(
								handlagerbewegung2Dto, theClientDto);

						getLagerFac().bucheAb(
								LocaleFac.BELEGART_LOS,
								li.getFlrlossollmaterial().getLos_i_id(),
								li.getI_id(),
								li.getFlrlossollmaterial().getFlrartikel()
										.getI_id(),
								li.getN_menge(),
								li.getFlrlossollmaterial().getN_sollpreis(),
								li.getLager_i_id(),
								(String) null,
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto);

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

			} else {
				try {
					getLagerFac().bucheZu(
							LocaleFac.BELEGART_LOS,
							li.getFlrlossollmaterial().getLos_i_id(),
							li.getI_id(),
							li.getFlrlossollmaterial().getFlrartikel()
									.getI_id(), new BigDecimal(1),
							li.getFlrlossollmaterial().getN_sollpreis(),
							li.getLager_i_id(), (String) null,
							new java.sql.Timestamp(System.currentTimeMillis()),
							theClientDto, null, null, true);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			System.out.println("LOSISTMATERIAL-ID: " + li.getI_id());
		}

		// ----------------------losablieferung-------------------

		queryString = " FROM FLRLosablieferung AS la ORDER BY la.i_id  ASC";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();
		System.out.println("LOSABLIEFERUNG: Gesamt-Positionen "
				+ resultList.size());

		int ilfdNrSnrChnr = 99;

		while (resultListIterator.hasNext()) {
			FLRLosablieferung la = (FLRLosablieferung) resultListIterator
					.next();
			try {

				if (la.getFlrlos().getFlrstueckliste() != null) {
					String snr = la.getC_snrchnr_mig();

					boolean bSnrChnrTragend = false;
					if (Helper.short2boolean(la.getFlrlos().getFlrstueckliste()
							.getFlrartikel().getB_seriennrtragend()) == true
							|| Helper.short2boolean(la.getFlrlos()
									.getFlrstueckliste().getFlrartikel()
									.getB_chargennrtragend()) == true) {
						bSnrChnrTragend = true;

					}

					List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = null;

					HashMap hmSNRMitVersion = new HashMap();
					if (bSnrChnrTragend == true) {
						if (snr != null && snr.length() > 0) {

							String[] sTeile = snr.split(",");
							alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							for (int i = 0; i < sTeile.length; i++) {

								String[] sSnrVersion = sTeile[i].split(":");

								SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
										sSnrVersion[0], new BigDecimal(1));
								alSeriennrChargennrMitMenge.add(sDto);

								if (sSnrVersion.length > 1) {
									hmSNRMitVersion.put(sSnrVersion[0],
											sSnrVersion[1]);
								}

							}

						} else {
							// Nun SNR generieren
							alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
							for (int i = 0; i < la.getN_menge().intValue(); i++) {

								SeriennrChargennrMitMengeDto sDto = new SeriennrChargennrMitMengeDto(
										"LO" + la.getFlrlos().getC_nr() + "#"
												+ ilfdNrSnrChnr,
										new BigDecimal(1));
								alSeriennrChargennrMitMenge.add(sDto);

								ilfdNrSnrChnr++;
							}

						}
					}

					LosablieferungDto laDto = getFertigungFac()
							.losablieferungFindByPrimaryKey(la.getI_id(),
									false, theClientDto);
					laDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);

					if (bSnrChnrTragend == true) {
						laDto.setNMenge(new BigDecimal(
								alSeriennrChargennrMitMenge.size()));
					}

					getFertigungFac().bucheLosAblieferungAufLager(
							laDto,
							getFertigungFac().losFindByPrimaryKey(
									la.getLos_i_id()), theClientDto);

					if (bSnrChnrTragend == true) {
						Iterator it = hmSNRMitVersion.keySet().iterator();
						while (it.hasNext()) {
							String key = (String) it.next();
							String version = (String) hmSNRMitVersion.get(key);
							getLagerFac().versionInLagerbewegungUpdaten(
									LocaleFac.BELEGART_LOSABLIEFERUNG,
									la.getI_id(), key, version);
						}
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			System.out.println("LOSABLIEFERUNG-ID: " + la.getI_id());
		}

		session.close();

	}

	public void versionPerEntityManagerUpdaten(Integer lagerbewegungIId,
			String cSnr, String cVersion) {

		Lagerbewegung zugang = em.find(Lagerbewegung.class, lagerbewegungIId);

		zugang.setCSeriennrchargennr(cSnr);
		zugang.setCVersion(cVersion);
		em.merge(zugang);
		em.flush();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pruefeSeriennummernMitVersion(TheClientDto theClientDto) {

		// 1.) Serienneummern der Abbuchungen an die Zubuchugnenj angleichen

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLagerbewegung l WHERE  l.c_seriennrchargennr IS NOT NULL AND l.c_version IS NOT NULL AND l.b_abgang=0";

		org.hibernate.Query query = session.createQuery(queryString);

		int u = 0;
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRLagerbewegung o = (FLRLagerbewegung) resultListIterator.next();
			// Zugang suchen und angleichen

			try {
				LagerabgangursprungDto[] abgaenge = getLagerFac()
						.lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(
								o.getI_id_buchung());

				for (int i = 0; i < abgaenge.length; i++) {

					LagerbewegungDto[] bewAbgaenge = getLagerFac()
							.lagerbewegungFindByIIdBuchung(
									abgaenge[i].getILagerbewegungid());

					for (int j = 0; j < bewAbgaenge.length; j++) {
						getLagerFac().versionPerEntityManagerUpdaten(
								bewAbgaenge[j].getIId(),
								o.getC_seriennrchargennr(), o.getC_version());

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			u++;
			System.out.println(u);

		}

		session.close();

	}

	/*
	 * 
	 * @TransactionAttribute(TransactionAttributeType.NEVER) public void
	 * konstruiereLagergewegungenAusBelegen(TheClientDto theClientDto) {
	 * 
	 * Session session = FLRSessionFactory.getFactory().openSession();
	 * 
	 * //----------------- //--ZUAGEANGE------ //-----------------
	 * 
	 * // ----------------------BESTELLUNG-------------------
	 * org.hibernate.Criteria crit =
	 * session.createCriteria(FLRWareneingangspositionen.class); List resultList
	 * = crit.list(); Iterator<?> resultListIterator = resultList.iterator();
	 * while (resultListIterator.hasNext()) { FLRWareneingangspositionen
	 * position = (FLRWareneingangspositionen) resultListIterator.next(); try {
	 * 
	 * String[] snrs =
	 * Helper.erzeugeSeriennummernArray(position.getC_seriennrchargennr(),
	 * position.getN_geliefertemenge(), false);
	 * 
	 * if (snrs != null && snrs.length > 0) { for (int i = 0; i < snrs.length;
	 * i++) {
	 * 
	 * if
	 * (Helper.short2boolean(position.getFlrbestellposition().getFlrartikel().
	 * getB_chargennrtragend())) {
	 * 
	 * bucheZu(LocaleFac.BELEGART_BESTELLUNG,
	 * position.getFlrwareneingang().getFlrbestellung().getI_id(),
	 * position.getI_idbestellposition(),
	 * position.getFlrbestellposition().getFlrartikel().getI_id(),
	 * position.getN_geliefertemenge(),
	 * position.getFlrbestellposition().getN_nettogesamtpreis(),
	 * position.getFlrwareneingang().getLager_i_id(), snrs[i], idUser); } else {
	 * bucheZu(LocaleFac.BELEGART_BESTELLUNG,
	 * position.getFlrwareneingang().getFlrbestellung().getI_id(),
	 * position.getI_idbestellposition(),
	 * position.getFlrbestellposition().getFlrartikel().getI_id(), new
	 * BigDecimal(1), position.getFlrbestellposition().getN_nettogesamtpreis(),
	 * position.getFlrwareneingang().getLager_i_id(), snrs[i], idUser);
	 * 
	 * } } } else {
	 * 
	 * bucheZu(LocaleFac.BELEGART_BESTELLUNG,
	 * position.getFlrwareneingang().getFlrbestellung().getI_id(),
	 * position.getI_idbestellposition(),
	 * position.getFlrbestellposition().getFlrartikel().getI_id(),
	 * position.getN_geliefertemenge(),
	 * position.getFlrbestellposition().getN_nettogesamtpreis(),
	 * position.getFlrwareneingang().getLager_i_id(),
	 * position.getC_seriennrchargennr(), idUser); } } catch (EJBExceptionLP
	 * ex3) { ex3.printStackTrace(); } } //session.close();
	 * 
	 * // ----------------------HAND------------------- String queryString =
	 * " FROM FLRHandlagerbewegung AS position WHERE position.n_menge >0 AND position.b_abgang=0 AND position.flrlager.i_id <>11 AND position.flrartikel.artikelart_c_nr='"
	 * + ArtikelFac.ARTIKELART_ARTIKEL + "'" +
	 * " ORDER BY position.t_buchungszeit ASC"; Query query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("HAND: Gesamt-Positionen " + resultList.size()); while
	 * (resultListIterator.hasNext()) { FLRHandlagerbewegung position =
	 * (FLRHandlagerbewegung) resultListIterator.next(); try {
	 * bucheZu(LocaleFac.BELEGART_HAND, position.getI_id() , position.getI_id(),
	 * position.getFlrartikel().getI_id(), position.getN_menge(),
	 * position.getN_einstandspreis(), position.getFlrlager().getI_id(),
	 * position.getC_seriennrchargennr(), idUser); } catch (EJBExceptionLP ex3)
	 * { ex3.printStackTrace(); }
	 * 
	 * System.out.println("HAND-ID: " + position.getI_id()); }
	 * 
	 * // ----------------------INVENTUR------------------- queryString =
	 * " FROM FLRInventurprotokoll AS position WHERE position.n_korrekturmenge >0 "
	 * + " ORDER BY position.t_zeitpunkt ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("INVENUTR: Gesamt-Positionen " + resultList.size());
	 * while (resultListIterator.hasNext()) { FLRInventurprotokoll position =
	 * (FLRInventurprotokoll) resultListIterator.next();
	 * bucheZu(LocaleFac.BELEGART_INVENTUR,
	 * position.getFlrinventurliste().getFlrinventur().getI_id() ,
	 * position.getI_id(),
	 * position.getFlrinventurliste().getFlrartikel().getI_id(),
	 * position.getN_korrekturmenge(), position.getN_inventurpreis(),
	 * position.getFlrinventurliste().getFlrlager().getI_id(),
	 * position.getFlrinventurliste().getC_seriennrchargennr(), idUser);
	 * System.out.println("INVENTUR-ID: " + position.getI_id()); }
	 * 
	 * // ----------------------LOSABLIEFERUNG------------------- queryString =
	 * " FROM FLRLosablieferung AS position WHERE position.flrlos.stueckliste_i_id is not null AND position.n_menge>0 ORDER BY position.flrlos.t_erledigt ASC"
	 * ; query = session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("LOSABLIEFERUNG: Gesamt-Positionen " +
	 * resultList.size()); while (resultListIterator.hasNext()) {
	 * FLRLosablieferung position = (FLRLosablieferung)
	 * resultListIterator.next(); if (position.getFlrlos().getFlrstueckliste()
	 * != null) { LosDto losDto = null; LosablieferungDto losablieferungDto =
	 * null; try { losDto =
	 * getFertigungFac().losFindByPrimaryKey(position.getFlrlos().getI_id());
	 * losablieferungDto =
	 * getFertigungFac().losablieferungFindByPrimaryKey(position. getI_id()); }
	 * catch (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); }
	 * bucheZu(LocaleFac.BELEGART_LOSABLIEFERUNG, position.getLos_i_id() ,
	 * position.getI_id(),
	 * position.getFlrlos().getFlrstueckliste().getFlrartikel().getI_id(),
	 * position.getN_menge(), position.getN_gestehungspreis(),
	 * losDto.getLagerIIdZiel(), losablieferungDto.getCSerienchargennummer(),
	 * idUser); } System.out.println("LOSABLIEFERUNG-ID: " +
	 * position.getI_id()); }
	 * 
	 * // ----------------------RECHNUNG------------------- queryString =
	 * " FROM FLRRechnungPosition AS position WHERE position.n_menge <0 AND position.positionsart_c_nr='"
	 * + LocaleFac.POSITIONSART_IDENT +
	 * "' AND position.flrartikel.artikelart_c_nr='" +
	 * ArtikelFac.ARTIKELART_ARTIKEL + "'" +
	 * " ORDER BY position.flrrechnung.d_belegdatum ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("RECHNUNG: Gesamt-Positionen " + resultList.size());
	 * while (resultListIterator.hasNext()) {
	 * com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition
	 * position =
	 * (com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition)
	 * resultListIterator.next();
	 * 
	 * RechnungDto rechnungDto = null; try { rechnungDto =
	 * getRechnungFac().rechnungFindByPrimaryKey(position.
	 * getFlrrechnung().getI_id()); } catch (RemoteException ex1) {
	 * throwEJBExceptionLPRespectOld(ex1); }
	 * bucheZu(LocaleFac.BELEGART_RECHNUNG, position.getFlrrechnung().getI_id()
	 * , position.getI_id(), position.getFlrartikel().getI_id(),
	 * position.getN_menge().abs(),
	 * position.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt(),
	 * rechnungDto.getLagerIId(), position.getC_serienchargennummer(), idUser);
	 * 
	 * System.out.println("RECHNUNG-ID: " + position.getI_id()); }
	 * 
	 * //----------------- //--ABAGEANGE------ //-----------------
	 * 
	 * 
	 * // ----------------------RECHNUNG------------------- queryString =
	 * " FROM FLRRechnungPosition AS position WHERE position.n_menge >0 AND position.positionsart_c_nr='"
	 * + LocaleFac.POSITIONSART_IDENT +
	 * "' AND position.flrartikel.artikelart_c_nr='" +
	 * ArtikelFac.ARTIKELART_ARTIKEL + "'" +
	 * " ORDER BY position.flrrechnung.d_belegdatum ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("RECHNUNG: Gesamt-Positionen " + resultList.size());
	 * while (resultListIterator.hasNext()) {
	 * com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition
	 * position =
	 * (com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition)
	 * resultListIterator.next(); RechnungDto rechnungDto = null; try {
	 * rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(position.
	 * getFlrrechnung().getI_id()); } catch (RemoteException ex1) {
	 * throwEJBExceptionLPRespectOld(ex1); } try {
	 * bucheAb(LocaleFac.BELEGART_RECHNUNG, position.getFlrrechnung().getI_id()
	 * , position.getI_id(), position.getFlrartikel().getI_id(),
	 * position.getN_menge(), position.getN_einzelpreis(),
	 * rechnungDto.getLagerIId(), position.getC_serienchargennummer(), idUser);
	 * } catch (EJBExceptionLP ex3) { ex3.printStackTrace(); }
	 * 
	 * System.out.println("RECHNUNG-ID: " + position.getI_id()); }
	 * //ZUSAETZLICHE HANDLAGERBUCHUNGEN:
	 * 
	 * 
	 * try { HandlagerbewegungDto handlagerbewegung2Dto = new
	 * HandlagerbewegungDto(); handlagerbewegung2Dto.setArtikelIId(new
	 * Integer(11)); handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr(null);
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(9));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(252));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr(null);
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(2));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(12));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr("104");
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(450));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(12));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr("A");
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(10));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(620));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr(null);
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(3));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(842));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(31));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr(null);
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(3));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser);
	 * 
	 * handlagerbewegung2Dto = new HandlagerbewegungDto();
	 * handlagerbewegung2Dto.setArtikelIId(new Integer(392));
	 * handlagerbewegung2Dto.setLagerIId(new Integer(12));
	 * handlagerbewegung2Dto.setBAbgang(new Short( (short) 0));
	 * handlagerbewegung2Dto.setCKommentar("Korr");
	 * handlagerbewegung2Dto.setCSeriennrchargennr(null);
	 * handlagerbewegung2Dto.setNMenge(new BigDecimal(6));
	 * handlagerbewegung2Dto.setNEinstandspreis(new BigDecimal("5.0"));
	 * createHandlagerbewegung(handlagerbewegung2Dto, idUser); } catch
	 * (EJBExceptionLP ex3) { ex3.printStackTrace(); }
	 * 
	 * // ----------------------LIEFERSCHEIN------------------- queryString =
	 * " FROM FLRLieferscheinposition AS position WHERE position.n_menge >0 AND position.positionart_c_nr='"
	 * + LocaleFac.POSITIONSART_IDENT +
	 * "' AND position.flrartikel.artikelart_c_nr='" +
	 * ArtikelFac.ARTIKELART_ARTIKEL + "'" +
	 * " ORDER BY position.flrlieferschein.t_liefertermin ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("LIEFERSCHEIN: Gesamt-Positionen " +
	 * resultList.size()); while (resultListIterator.hasNext()) {
	 * com.lp.server.lieferschein
	 * .fastlanereader.generated.FLRLieferscheinposition position =
	 * (com.lp.server.lieferschein.fastlanereader.generated.
	 * FLRLieferscheinposition) resultListIterator.next();
	 * 
	 * LieferscheinpositionDto lsposDto = null; try { lsposDto =
	 * getLieferscheinpositionFac().lieferscheinpositionFindByPrimaryKey(
	 * position.getI_id(), idUser); } catch (RemoteException ex2) {
	 * throwEJBExceptionLPRespectOld(ex2); } try { if
	 * (position.getFlrlieferschein().getLager_i_id().equals(new Integer(11))) {
	 * bucheAb(LocaleFac.BELEGART_LIEFERSCHEIN,
	 * position.getFlrlieferschein().getI_id() , position.getI_id(),
	 * position.getFlrartikel().getI_id(), position.getN_menge(),
	 * position.getN_nettogesamtpreis(), new Integer(12),
	 * lsposDto.getCSerienchargennummer(), idUser); } else {
	 * bucheAb(LocaleFac.BELEGART_LIEFERSCHEIN,
	 * position.getFlrlieferschein().getI_id() , position.getI_id(),
	 * position.getFlrartikel().getI_id(), position.getN_menge(),
	 * position.getN_nettogesamtpreis(),
	 * position.getFlrlieferschein().getLager_i_id(),
	 * lsposDto.getCSerienchargennummer(), idUser);
	 * 
	 * }
	 * 
	 * } catch (EJBExceptionLP ex3) { ex3.printStackTrace(); }
	 * 
	 * System.out.println("LOSABLIEFERUNG-ID: " + position.getI_id()); }
	 * 
	 * // ----------------------HAND------------------- queryString =
	 * " FROM FLRHandlagerbewegung AS position WHERE position.n_menge >0 AND position.b_abgang=1 AND position.flrartikel.artikelart_c_nr='"
	 * + ArtikelFac.ARTIKELART_ARTIKEL + "'" +
	 * " ORDER BY position.t_buchungszeit ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("HAND: Gesamt-Positionen " + resultList.size()); while
	 * (resultListIterator.hasNext()) { FLRHandlagerbewegung position =
	 * (FLRHandlagerbewegung) resultListIterator.next();
	 * bucheAb(LocaleFac.BELEGART_HAND, position.getI_id() , position.getI_id(),
	 * position.getFlrartikel().getI_id(), position.getN_menge(),
	 * position.getN_verkaufspreis(), position.getFlrlager().getI_id(),
	 * position.getC_seriennrchargennr(), idUser);
	 * System.out.println("HAND-ID: " + position.getI_id()); }
	 * 
	 * // ----------------------INVENTUR------------------- queryString =
	 * " FROM FLRInventurprotokoll AS position WHERE position.n_korrekturmenge <0 "
	 * + " ORDER BY position.t_zeitpunkt ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("INVENTUR: Gesamt-Positionen " + resultList.size());
	 * while (resultListIterator.hasNext()) { FLRInventurprotokoll position =
	 * (FLRInventurprotokoll) resultListIterator.next();
	 * 
	 * BigDecimal lagerstand =
	 * getLagerstand(position.getFlrinventurliste().getFlrartikel(). getI_id(),
	 * new Integer(12), idUser);
	 * 
	 * if (position.getN_korrekturmenge().abs().doubleValue() >
	 * lagerstand.doubleValue()) { BigDecimal diff = new
	 * BigDecimal(position.getN_korrekturmenge().abs().doubleValue() -
	 * lagerstand.doubleValue()); HandlagerbewegungDto dto = new
	 * HandlagerbewegungDto();
	 * dto.setArtikelIId(position.getFlrinventurliste().getFlrartikel
	 * ().getI_id()); dto.setBAbgang(Helper.boolean2Short(false));
	 * dto.setLagerIId(position.getFlrinventurliste().getFlrlager().getI_id());
	 * dto.setCKommentar("Korrekturbuchung");
	 * dto.setCSeriennrchargennr(position.
	 * getFlrinventurliste().getC_seriennrchargennr()); dto.setNMenge(diff);
	 * dto.setNEinstandspreis(position.getN_inventurpreis());
	 * createHandlagerbewegung(dto, idUser); }
	 * 
	 * bucheAb(LocaleFac.BELEGART_INVENTUR,
	 * position.getFlrinventurliste().getFlrinventur().getI_id() ,
	 * position.getI_id(),
	 * position.getFlrinventurliste().getFlrartikel().getI_id(),
	 * position.getN_korrekturmenge().abs(), position.getN_inventurpreis(), new
	 * Integer(12), null, idUser); System.out.println("INVENTUR-ID: " +
	 * position.getI_id()); }
	 * 
	 * // ----------------------LOS------------------- queryString =
	 * " FROM FLRLosistmaterial AS position WHERE position.n_menge >0 " +
	 * " ORDER BY position.flrlossollmaterial.flrlos.t_erledigt ASC"; query =
	 * session.createQuery(queryString); resultList = query.list();
	 * resultListIterator = resultList.iterator();
	 * System.out.println("LOS: Gesamt-Positionen " + resultList.size()); while
	 * (resultListIterator.hasNext()) {
	 * com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial
	 * position =
	 * (com.lp.server.fertigung.fastlanereader.generated.FLRLosistmaterial)
	 * resultListIterator.next(); bucheAb(LocaleFac.BELEGART_LOS,
	 * position.getFlrlossollmaterial().getFlrlos().getI_id() ,
	 * position.getI_id(),
	 * position.getFlrlossollmaterial().getFlrartikel().getI_id(),
	 * position.getN_menge(), position.getFlrlossollmaterial().getN_sollpreis(),
	 * position.getFlrlager().getI_id(), null, idUser);
	 * System.out.println("LOSPOS-ID: " + position.getI_id()); }
	 * 
	 * session.close();
	 * 
	 * }
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeVollstaendigVerbraucht(Integer artikelIIdInput,
			boolean bKorrigieren, TheClientDto theClientDto) {

		String sUpdateStatement = "";

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "ZAEHLER;ARTIKEL_I_ID;BUCHUNGS_I_ID;MENGEAUSLAGERBEWEGUNG;MENGEAUSABGANGSURSPRUNG"
				+ new String(CRLFAscii);

		HashMap hmIIdBuchungZumKorrigieren = new HashMap();

		int iZaehlerArtikel = 0;

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT lagerbewegung.artikel_i_id,lagerbewegung.n_menge,lagerbewegung.i_id_buchung,lagerbewegung.b_vollstaendigverbraucht,(SELECT SUM(lagerabgangursprung.n_verbrauchtemenge) FROM FLRLagerabgangursprung as lagerabgangursprung WHERE lagerabgangursprung.compId.i_lagerbewegungidursprung=lagerbewegung.i_id_buchung)"
				+ " FROM FLRLagerbewegung AS lagerbewegung WHERE lagerbewegung.b_abgang=0 and lagerbewegung.b_historie=0 ORDER BY lagerbewegung.artikel_i_id ASC";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		int zaehler = 1;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			Integer i_id_buchung = (Integer) o[2];
			Integer artikel_i_id = (Integer) o[0];

			BigDecimal mengeAusLagerbewegung = (BigDecimal) o[1];
			Short vollstverbraucht = (Short) o[3];
			iZaehlerArtikel++;

			if (iZaehlerArtikel % 100 == 0
					|| iZaehlerArtikel == resultList.size()) {
				System.out.println("Lagerbewegung " + iZaehlerArtikel + " von "
						+ resultList.size());
			}

			BigDecimal verbrauchteAusLagerabgangUrsprung = new BigDecimal(0);
			if (o[4] != null) {
				verbrauchteAusLagerabgangUrsprung = (BigDecimal) o[4];
			}
			String ausgabe = "";

			if (Helper.short2boolean(vollstverbraucht) == true) {

				if (verbrauchteAusLagerabgangUrsprung.doubleValue() > mengeAusLagerbewegung
						.doubleValue()) {
					ausgabe += zaehler + ";" + artikel_i_id + ";"
							+ i_id_buchung + ";" + mengeAusLagerbewegung + ";"
							+ verbrauchteAusLagerabgangUrsprung + "--FATAL";
					rueckgabe += ausgabe + new String(CRLFAscii);
					System.out.println(ausgabe);
					zaehler++;
					if (bKorrigieren == true) {
						getLagerFac().setzteZugangsBuchungAlsVerbraucht(
								i_id_buchung, true, theClientDto);
					}
					sUpdateStatement += "UPDATE WW_LAGERBEWEGUNG SET B_VOLLSTAENDIGVERBRAUCHT=1 WHERE I_ID_BUCHUNG="
							+ i_id_buchung
							+ "; --FATAL_ZUVIEL_VERBRAUCHT"
							+ new String(CRLFAscii);

					if (!hmIIdBuchungZumKorrigieren.containsKey(i_id_buchung)) {
						BigDecimal differenz = verbrauchteAusLagerabgangUrsprung
								.subtract(mengeAusLagerbewegung);

						hmIIdBuchungZumKorrigieren.put(i_id_buchung, differenz);
					}

				} else if (verbrauchteAusLagerabgangUrsprung.doubleValue() < mengeAusLagerbewegung
						.doubleValue()) {
					ausgabe += zaehler + ";" + artikel_i_id + ";"
							+ i_id_buchung + ";" + mengeAusLagerbewegung + ";"
							+ verbrauchteAusLagerabgangUrsprung;
					rueckgabe += ausgabe + new String(CRLFAscii);
					System.out.println(ausgabe);
					zaehler++;
					if (bKorrigieren == true) {
						getLagerFac().setzteZugangsBuchungAlsVerbraucht(
								i_id_buchung, false, theClientDto);
					}

				}

			} else {
				if (verbrauchteAusLagerabgangUrsprung.doubleValue() >= mengeAusLagerbewegung
						.doubleValue()) {
					ausgabe += zaehler + ";" + artikel_i_id + ";"
							+ i_id_buchung + ";" + mengeAusLagerbewegung + ";"
							+ verbrauchteAusLagerabgangUrsprung;

					rueckgabe += ausgabe + new String(CRLFAscii);
					System.out.println(ausgabe);
					if (bKorrigieren == true) {
						getLagerFac().setzteZugangsBuchungAlsVerbraucht(
								i_id_buchung, true, theClientDto);
					}
					zaehler++;
					sUpdateStatement += "UPDATE WW_LAGERBEWEGUNG SET B_VOLLSTAENDIGVERBRAUCHT=1 WHERE I_ID_BUCHUNG="
							+ i_id_buchung + ";" + new String(CRLFAscii);

				}
			}

		}
		session.close();

		System.out.println();
		System.out.println(sUpdateStatement);

		System.out.println("ARTIKEL_I_IDS:");
		if (bKorrigieren == true) {
			Iterator iTeratoriIdBuchung = hmIIdBuchungZumKorrigieren.keySet()
					.iterator();
			int izeile = 0;
			while (iTeratoriIdBuchung.hasNext()) {
				izeile++;
				System.out.println("Zeile " + izeile + " von "
						+ hmIIdBuchungZumKorrigieren.size());
				Integer iIdBuchungTemp = (Integer) iTeratoriIdBuchung.next();
				LagerbewegungDto[] lBew = lagerbewegungFindByIIdBuchung(iIdBuchungTemp);

				if (lBew.length > 0) {
					getLagerFac().korrigiereVollstaendigVerbraucht(
							theClientDto,
							lBew[0].getIIdBuchung(),
							lBew[0].getArtikelIId(),
							lBew[0].getLagerIId(),
							lBew[0].getCSeriennrchargennr(),
							(BigDecimal) hmIIdBuchungZumKorrigieren
									.get(iIdBuchungTemp));
				}

			}

		}

		rueckgabe += sUpdateStatement;
		return rueckgabe;
	}

	public void korrigiereVollstaendigVerbraucht(TheClientDto theClientDto,
			Integer i_id_buchung, Integer artikel_i_id, Integer lager_i_id,
			String snrchnr, BigDecimal differenz) {
		// Korrektur:

		Query queryursprung = em
				.createNamedQuery("LagerabgangursprungfindByILagerbewegungIIdursprung");
		queryursprung.setParameter(1, i_id_buchung);
		Collection<?> cl = queryursprung.getResultList();

		Iterator itUrsprung = cl.iterator();
		while (itUrsprung.hasNext()) {
			Lagerabgangursprung ursp = (Lagerabgangursprung) itUrsprung.next();
			if (ursp.getNVerbrauchtemenge().doubleValue() > 0) {
				if (differenz.doubleValue() <= ursp.getNVerbrauchtemenge()
						.doubleValue()) {
					ursp.setNVerbrauchtemenge(ursp.getNVerbrauchtemenge()
							.subtract(differenz));
					LagerabgangursprungDto[] urspruenge = getVonLagerNachRegel(
							artikel_i_id, lager_i_id, snrchnr, differenz,
							theClientDto);

					for (int j = 0; j < urspruenge.length; j++) {
						urspruenge[j].setILagerbewegungid(ursp.getPk()
								.getILagerbewegungid());

						// Zuerst nachsehen, ob es diesen
						// Urspung scon gibt,
						// dann Updaten
						LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
						lagerabgangursprungPK.setILagerbewegungid(ursp.getPk()
								.getILagerbewegungid());
						lagerabgangursprungPK
								.setILagerbewegungidursprung(urspruenge[j]
										.getILagerbewegungidursprung());
						Lagerabgangursprung lagerabgangursprung = em.find(
								Lagerabgangursprung.class,
								lagerabgangursprungPK);
						if (lagerabgangursprung == null) {
							createLagerabgangursprung(urspruenge[j]);
						} else {
							lagerabgangursprung
									.setNVerbrauchtemenge(lagerabgangursprung
											.getNVerbrauchtemenge()
											.add(urspruenge[j]
													.getNVerbrauchtemenge()));
							em.merge(lagerabgangursprung);
							em.flush();
						}
					}

					break;
				} else {
					BigDecimal bdVerbr = ursp.getNVerbrauchtemenge();
					ursp.setNVerbrauchtemenge(new BigDecimal(0));
					LagerabgangursprungDto[] urspruenge = getVonLagerNachRegel(
							artikel_i_id, lager_i_id, snrchnr, bdVerbr,
							theClientDto);

					for (int j = 0; j < urspruenge.length; j++) {
						urspruenge[j].setILagerbewegungid(ursp.getPk()
								.getILagerbewegungid());

						// Zuerst nachsehen, ob es diesen
						// Urspung scon gibt,
						// dann Updaten
						LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
						lagerabgangursprungPK.setILagerbewegungid(ursp.getPk()
								.getILagerbewegungid());
						lagerabgangursprungPK
								.setILagerbewegungidursprung(urspruenge[j]
										.getILagerbewegungidursprung());
						Lagerabgangursprung lagerabgangursprung = em.find(
								Lagerabgangursprung.class,
								lagerabgangursprungPK);
						if (lagerabgangursprung == null) {
							createLagerabgangursprung(urspruenge[j]);
						} else {
							lagerabgangursprung
									.setNVerbrauchtemenge(lagerabgangursprung
											.getNVerbrauchtemenge()
											.add(urspruenge[j]
													.getNVerbrauchtemenge()));
							em.merge(lagerabgangursprung);
							em.flush();
						}
					}

					differenz = differenz.subtract(bdVerbr);
				}
			}
		}
	}

	/**
	 * Sortiert Lagerbewegungen nach Buchungsdatum, wobei die juengste
	 * Lagerbewegung an 1. Stelle steht.
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto[]
	 * @return LagerbewegungDto[] sortierte Lagerbewegungen
	 */
	private LagerbewegungDto[] sortiereNachDatumJuensterZuerst(
			LagerbewegungDto[] lagerbewegungDto) {
		if (lagerbewegungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerbewegungDto == null"));
		}

		for (int i = lagerbewegungDto.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				// Wenn z.B: 15.8.2004 < als 19.8.2004, dann wird getauscht
				if (lagerbewegungDto[j].getTBuchungszeit().getTime() < lagerbewegungDto[j + 1]
						.getTBuchungszeit().getTime()) {
					LagerbewegungDto lagerbewegungDtoTemp = lagerbewegungDto[j];
					lagerbewegungDto[j] = lagerbewegungDto[j + 1];
					lagerbewegungDto[j + 1] = lagerbewegungDtoTemp;
				}
			}
		}
		return lagerbewegungDto;
	}

	/**
	 * Sortiert Lagerbewegungen nach Buchungsdatum, wobei die aelteste
	 * Lagerbewegung an 1. Stelle steht.
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto[]
	 * @return LagerbewegungDto[] sortierte Lagerbewegungen
	 */
	private LagerbewegungDto[] sortiereNachDatumAeltesterZuerst(
			LagerbewegungDto[] lagerbewegungDto) {
		if (lagerbewegungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerbewegungDto == null"));
		}

		for (int i = lagerbewegungDto.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				// Wenn z.B: 15.8.2004 < als 19.8.2004, dann wird getauscht
				if (lagerbewegungDto[j].getTBuchungszeit().getTime() > lagerbewegungDto[j + 1]
						.getTBuchungszeit().getTime()) {
					LagerbewegungDto lagerbewegungDtoTemp = lagerbewegungDto[j];
					lagerbewegungDto[j] = lagerbewegungDto[j + 1];
					lagerbewegungDto[j + 1] = lagerbewegungDtoTemp;
				}
			}
		}
		return lagerbewegungDto;
	}

	/**
	 * Sortiert Lagerbewegungen nach Buchungsnummer
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto[]
	 * @throws EJBExceptionLP
	 *             lagerbewegungDto == null
	 * @return LagerbewegungDto[]
	 */
	private LagerbewegungDto[] sortiereNachBuchungsnummerKleinsteZuerst(
			LagerbewegungDto[] lagerbewegungDto) throws EJBExceptionLP {
		if (lagerbewegungDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerbewegungDto == null"));
		}

		for (int i = lagerbewegungDto.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (lagerbewegungDto[j].getIIdBuchung().intValue() < lagerbewegungDto[j + 1]
						.getIIdBuchung().intValue()) {
					LagerbewegungDto lagerbewegungDtoTemp = lagerbewegungDto[j];
					lagerbewegungDto[j] = lagerbewegungDto[j + 1];
					lagerbewegungDto[j + 1] = lagerbewegungDtoTemp;
				}
			}
		}
		return lagerbewegungDto;
	}

	/**
	 * Gibt die juengste Lagerbewegung einer Buchungsnummer zurueck
	 * 
	 * @param iIdBuchung
	 *            Integer 32
	 * @throws EJBExceptionLP
	 *             Wenn iIdBuchung==null
	 * @return LagerbewegungDto Juengste Lagerbewegung einer Buchungsnummer
	 */
	public LagerbewegungDto getJuengsteBuchungEinerBuchungsNummer(
			Integer iIdBuchung) throws EJBExceptionLP {
		if (iIdBuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iIdBuchung == null"));
		}

		LagerbewegungDto[] lagerbewegungDto = lagerbewegungFindByIIdBuchung(iIdBuchung);
		lagerbewegungDto = sortiereNachDatumJuensterZuerst(lagerbewegungDto);

		if (lagerbewegungDto == null || lagerbewegungDto.length == 0) {
			myLogger.logKritisch("KEINE_LAGERBEWEGUNG_FUER_I_ID_BUCHUNG="
					+ iIdBuchung);
			return null;
		}
		return lagerbewegungDto[0];
	}

	/**
	 * Gibt alle "juengsten" Buchungen eines Artikels und eines Lagers zurueck,
	 * die von der Art Lager ZU- oder AB-Buchung sind. Wenn Serien-/
	 * Charegnnummer mit angegeben wird, dann wird diese mitberuecksichtigt. Bei
	 * jeder Abgangsbuchung wird das Datum der Aeltesten Buchung verwendet,
	 * falls fuer diese Buchung Korrekturen existieren.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param sAbgang
	 *            0
	 * @param cSeriennrchargennr
	 *            gjh67i6
	 * @throws EJBExceptionLP
	 *             artikelIId == null || lagerIId == null || sAbgang == null
	 *             oder sAbgang.intValue() < 0 && sAbgang.intValue() > 1
	 * @return LagerbewegungDto[] alle "juengsten" Buchungen eines Artikels und
	 *         eines Lagers
	 */
	private LagerbewegungDto[] getAlleBuchungenEinesArtikelUndEinesLagers(
			Integer artikelIId, Integer lagerIId, Short sAbgang,
			String cSeriennrchargennr) throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null || sAbgang == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || lagerIId == null || sAbgang == null"));
		}
		if (sAbgang.intValue() < 0 && sAbgang.intValue() > 1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"sAbgang.intValue() < 0 && sAbgang.intValue() > 1"));
		}

		LagerbewegungDto[] dtos = lagerbewegungFindByArtikelIIdLagerIIdBAbgangCSeriennrchargennr(
				artikelIId, lagerIId, sAbgang, cSeriennrchargennr);
		return sortiereNachBuchungsnummerKleinsteZuerst(dtos);
	}

	/**
	 * Gibt das Datum der Letzten Zugangsbuchung zurueck
	 * 
	 * @param artikelIId
	 *            4711
	 * @param bAbgang
	 *            Zu/Abgang
	 * @throws EJBExceptionLP
	 *             artikelIId == null
	 * @return java.sql.Timestamp Timestamp der Letzten Buchung
	 */
	public java.sql.Timestamp getDatumLetzterZugangsOderAbgangsbuchung(
			Integer artikelIId, boolean bAbgang) throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		java.sql.Timestamp tsZeit = null;
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class);
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID,
				artikelIId));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
				Helper.boolean2Short(bAbgang)));
		crit.add(Restrictions.gt(LagerFac.FLR_LAGERBEWEGUNG_N_MENGE,
				new BigDecimal(0)));
		crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
		crit.setMaxResults(1);
		List<?> resultList = crit.list();

		if (resultList.size() > 0) {
			Iterator<?> resultListIterator = resultList.iterator();
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();
			tsZeit = new Timestamp(lagerbewegung.getT_buchungszeit().getTime());
		}
		session.close();
		return tsZeit;

	}

	public Integer getLetzteWEP_IID(Integer artikelIId) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}

		Integer wepIId = null;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class);
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID,
				artikelIId));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
				LocaleFac.BELEGART_BESTELLUNG));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
				Helper.boolean2Short(false)));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));
		crit.add(Restrictions.gt(LagerFac.FLR_LAGERBEWEGUNG_N_MENGE,
				new BigDecimal(0)));
		crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));
		crit.setMaxResults(1);
		List<?> resultList = crit.list();

		if (resultList.size() > 0) {
			Iterator<?> resultListIterator = resultList.iterator();
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();
			wepIId = lagerbewegung.getI_belegartpositionid();
		}
		session.close();
		return wepIId;

	}

	/**
	 * Gibt die Menge eines bestimmten Artikels (Charegen oder
	 * Seriennummernbehaftet) auf einem bestimmten Lager zu zurueckgeliefert.
	 * Wenn ein Artikel nicht Lagerbewirtschaftet ist, dann wird 2086712345
	 * zurueckgegeben
	 * 
	 * @param artikelIId
	 *            Integer 78
	 * @param lagerIId
	 *            Integer 63
	 * @param cSeriennrchargennr
	 *            String 435KN68
	 * @param theClientDto
	 *            User-ID
	 * @return Double Menge
	 * @throws EJBExceptionLP
	 *             artikelIId==null oder lagerIId==null oder (serienNr != null
	 *             && chargenNr != null)
	 */
	public BigDecimal getMengeAufLager(Integer artikelIId, Integer lagerIId,
			String cSeriennrchargennr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null || lagerIId == null"));
		}
		boolean lagerbewirtschaftet = true;
		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (Helper.short2boolean(artikel.getBLagerbewirtschaftet()) == false) {
			lagerbewirtschaftet = false;
		}

		// Wenn Artikel Snr/Chargennummerbehaftet ist, dann muss Seriennummer
		// angegeben werden
		if (artikel.getBChargennrtragend().intValue() == 1
				|| artikel.getBSeriennrtragend().intValue() == 1) {
			if (cSeriennrchargennr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("cSeriennrchargennr == null"));
			}
		}

		// Wenn Artikel nicht Snr/Chargennummerbehaftet ist, dann darf keine
		// Seriennummer angegeben werden
		if (artikel.getBChargennrtragend().intValue() != 1
				&& artikel.getBSeriennrtragend().intValue() != 1) {
			if (cSeriennrchargennr != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER,
						new Exception(
								"SERIENNUMMER_CHARGENNUMMER_DARF_NICHT_ANGEGEBEN_WERDEN"));
			}
		}

		if (lagerbewirtschaftet == true) {
			BigDecimal mengeAufLager = null;
			if (cSeriennrchargennr == null) {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager == null) {
					mengeAufLager = new BigDecimal(0);
				} else {
					mengeAufLager = artikellager.getNLagerstand();
				}
			} else {
				mengeAufLager = new BigDecimal(0);
				SeriennrChargennrAufLagerDto[] snrDtos = getAllSerienChargennrAufLagerInfoDtos(
						artikelIId, lagerIId, cSeriennrchargennr, true, null,
						theClientDto);
				for (int i = 0; i < snrDtos.length; i++) {
					if (cSeriennrchargennr.equals(snrDtos[i]
							.getCSeriennrChargennr())) {
						mengeAufLager = snrDtos[i].getNMenge();
						break;
					}
				}

			}
			return mengeAufLager;
		} else {
			return new BigDecimal(2086712345);
		}

	}

	/**
	 * Liefert die Buchungsnummer einer Lagerbewegung zurueck
	 * 
	 * @param lagerbewegungIId
	 *            Integer 83
	 * @throws EJBExceptionLP
	 *             lagerbewegungIId==null
	 * @return Integer Buchungsnummer
	 */
	private Integer getBuchungsNrUeberId(Integer lagerbewegungIId)
			throws EJBExceptionLP {
		if (lagerbewegungIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("lagerbewegungIId == null"));
		}
		return lagerbewegungFindIIdBuchungByIId(lagerbewegungIId);
	}

	/**
	 * Gibt den Deckungsbeitrag einer Belegartposition zurueck (Pro
	 * Mengeneinheit) Errechnet sich aus VK-Preis - EK- Preis.
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @param cSeriennrChargennr
	 *            SNR/CHARGENNUMMER
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getDeckungsbeitrag(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId ==null"));
		}
		/** @todo Testen und reparieren PJ 4369 */

		LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
				belegartpositionIId, cSeriennrChargennr);
		// Hole alle Urspruenge
		LagerabgangursprungDto[] alleUrspruenge = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagerbewegungDto
				.getIIdBuchung());
		BigDecimal deckungsbeitrag = new BigDecimal(0);
		for (int i = 0; i < alleUrspruenge.length; i++) {
			LagerbewegungDto ursprung = getJuengsteBuchungEinerBuchungsNummer(alleUrspruenge[i]
					.getILagerbewegungidursprung());

			BigDecimal vkpreis = null;
			vkpreis = lagerbewegungDto.getNVerkaufspreis().multiply(
					alleUrspruenge[i].getNVerbrauchtemenge());
			BigDecimal ekpreis = null;
			ekpreis = ursprung.getNEinstandspreis().multiply(
					alleUrspruenge[i].getNVerbrauchtemenge());
			deckungsbeitrag = deckungsbeitrag.add(vkpreis.subtract(ekpreis));

		}
		if (lagerbewegungDto.getNMenge().doubleValue() != 0) {
			deckungsbeitrag = deckungsbeitrag
					.divide(lagerbewegungDto.getNMenge(), 4,
							BigDecimal.ROUND_HALF_EVEN);
		} else {
			deckungsbeitrag = new BigDecimal(0);
		}
		return deckungsbeitrag;
	}

	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
			String belegartCNr, Integer belegartpositionIId) {

		List<SeriennrChargennrMitMengeDto> list = new ArrayList<SeriennrChargennrMitMengeDto>();
		Query query = em
				.createNamedQuery("LagerbewegungejbSelectAllSeriennrchargennr");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);

		Collection<?> clLB = query.getResultList();
		Iterator<?> iteratorLB = clLB.iterator();
		if (clLB.size() > 0) {
			while (iteratorLB.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iteratorLB.next();

				SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto();
				dto.setCSeriennrChargennr(lagerbewegung.getCSeriennrchargennr());
				dto.setNMenge(lagerbewegung.getNMenge());
				if (lagerbewegung.getCVersion() != null) {
					dto.setCVersion(lagerbewegung.getCVersion());
				} else {
					dto.setCVersion("");
				}

				list.add(dto);
			}
		}

		return list;
	}

	/**
	 * Bringt den Gemittelten Gestehungspreis einer Abgangsposition zurueck,
	 * wenn diese mehrere Seriennummernpositionen enthaelt
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getGemittelterGestehungspreisEinerAbgangsposition(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {

		BigDecimal gemittelterGestPreis = new BigDecimal(0);

		List<SeriennrChargennrMitMengeDto> snrs = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
				belegartCNr, belegartpositionIId);
		if (snrs == null) {
			snrs = new ArrayList<SeriennrChargennrMitMengeDto>();
			SeriennrChargennrMitMengeDto s = new SeriennrChargennrMitMengeDto();
			snrs.add(s);
		}

		for (int i = 0; i < snrs.size(); i++) {

			gemittelterGestPreis = gemittelterGestPreis
					.add(getGestehungspreisEinerAbgangsposition(belegartCNr,
							belegartpositionIId, snrs.get(i)
									.getCSeriennrChargennr()));
		}
		if (snrs.size() != 0) {
			gemittelterGestPreis = gemittelterGestPreis.divide(new BigDecimal(
					(double) snrs.size()), BigDecimal.ROUND_HALF_EVEN);
		}

		return Helper.rundeKaufmaennisch(gemittelterGestPreis, 4);
	}

	/**
	 * Bringt den Gemittelten Gestehungspreis einer Abgangsposition zurueck,
	 * wenn diese mehrere Seriennummernpositionen enthaelt
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getGemittelterEinstandspreisEinerZugangsposition(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {

		BigDecimal gemittelterGestPreis = new BigDecimal(0);

		List<SeriennrChargennrMitMengeDto> snrs = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
				belegartCNr, belegartpositionIId);
		if (snrs == null) {
			snrs = new ArrayList<SeriennrChargennrMitMengeDto>();
			SeriennrChargennrMitMengeDto s = new SeriennrChargennrMitMengeDto();
			snrs.add(s);
		}

		for (int i = 0; i < snrs.size(); i++) {

			gemittelterGestPreis = gemittelterGestPreis.add(getEinstandspreis(
					belegartCNr, belegartpositionIId, snrs.get(i)
							.getCSeriennrChargennr()));
		}
		if (snrs.size() != 0) {
			gemittelterGestPreis = gemittelterGestPreis.divide(new BigDecimal(
					(double) snrs.size()), BigDecimal.ROUND_HALF_EVEN);
		}

		return Helper.rundeKaufmaennisch(gemittelterGestPreis, 4);
	}

	/**
	 * Gibt den Gestehungspreis einer Belegartposition zurueck (Pro
	 * Mengeneinheit) Gestehungspreisabfrage kann nur fuer Ausgangbelege
	 * abgefragt werden.
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @param cSeriennrChargennr
	 *            SNR/CHARGENNUMMER
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getGestehungspreisEinerAbgangsposition(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrChargennr) throws EJBExceptionLP {
		return getGestehungspreisEinerAbgangsposition(belegartCNr,
				belegartpositionIId, cSeriennrChargennr, 0);
	}

	public BigDecimal getGestehungspreisEinerAbgangspositionMitTransaktion(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrChargennr) throws EJBExceptionLP {
		return getGestehungspreisEinerAbgangsposition(belegartCNr,
				belegartpositionIId, cSeriennrChargennr, 0);
	}

	private BigDecimal getGestehungspreisEinerAbgangsposition(
			String belegartCNr, Integer belegartpositionIId,
			String cSeriennrChargennr, int durchlauf) throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}
		LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
				belegartpositionIId, cSeriennrChargennr);

		if (lagerbewegungDto == null) {

			System.out.println("Belegart=" + belegartCNr + " Position="
					+ belegartpositionIId + " /SnrChnr=" + cSeriennrChargennr);
			return new BigDecimal(0);
		}

		if (Helper.short2boolean(lagerbewegungDto.getBAbgang()) == false) {
			throw new EJBExceptionLP(
					EJBExceptionLP.ARTIKEL_GESTEHUNGSPREISABFRAGE_NUR_FUER_WARENABGAENGE_MOEGLICH,
					new Exception(
							"Helper.short2boolean(lagerbewegungDto.getBAbgang())==false && belegartCNr="
									+ belegartCNr + ", belegartpositionIId="
									+ belegartpositionIId));
		}

		// Hole alle Urspruenge
		LagerabgangursprungDto[] alleUrspruenge = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagerbewegungDto
				.getIIdBuchung());
		BigDecimal gestehungspreis = new BigDecimal(0);

		// Wenn kein Ursprung vorhanden, dann kommt der Gestehungspreis aus der
		// Buchung selbst
		if (alleUrspruenge.length == 0) {

			gestehungspreis = lagerbewegungDto.getNGestehungspreis().multiply(
					lagerbewegungDto.getNMenge());

		} else {
			// Wenn Urspruenge vorhanden, dann kommt der Gestehungspreis aus
			// diesen
			for (int i = 0; i < alleUrspruenge.length; i++) {

				LagerbewegungDto ursprung = getJuengsteBuchungEinerBuchungsNummer(alleUrspruenge[i]
						.getILagerbewegungidursprung());

				try {
					Query query = em
							.createNamedQuery("LagerzugangursprungfindIIdBuchungsursprungByILagerbewegungid");
					query.setParameter(1,
							alleUrspruenge[i].getILagerbewegungidursprung());
					Lagerzugangursprung lagerzugangursprung = (Lagerzugangursprung) query
							.getSingleResult();
					ursprung = getJuengsteBuchungEinerBuchungsNummer(lagerzugangursprung
							.getPk().getILagerbewegungidursprung());
					if (durchlauf < 10) {
						durchlauf++;
						gestehungspreis = gestehungspreis
								.add(getGestehungspreisEinerAbgangsposition(
										ursprung.getCBelegartnr(),
										ursprung.getIBelegartpositionid(),
										cSeriennrChargennr, durchlauf)
										.multiply(
												alleUrspruenge[i]
														.getNVerbrauchtemenge()));

					} else {
						gestehungspreis = gestehungspreis.add(alleUrspruenge[i]
								.getNGestehungspreis().multiply(
										alleUrspruenge[i]
												.getNVerbrauchtemenge()));
					}

				} catch (NoResultException ex) {
					gestehungspreis = gestehungspreis.add(alleUrspruenge[i]
							.getNGestehungspreis().multiply(
									alleUrspruenge[i].getNVerbrauchtemenge()));

				}

			}
		}

		if (lagerbewegungDto.getNMenge().doubleValue() != 0) {
			gestehungspreis = gestehungspreis
					.divide(lagerbewegungDto.getNMenge(), 4,
							BigDecimal.ROUND_HALF_EVEN);
		} else {
			gestehungspreis = new BigDecimal(0);
		}

		return gestehungspreis;
	}

	/**
	 * Gibt die Menge einer Belegartposition zurueck die f&uuml;r diese
	 * Belegartposition gebucht worden ist Wenn Seriennummern/Chargennummern
	 * behaftet, dann die Summe aller Snrs/Chargennummern
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getMengeEinerBelegposition(String belegartCNr,
			Integer belegartpositionIId, java.sql.Timestamp tStichtag)
			throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}

		BigDecimal mengeGesamt = new BigDecimal(0);
		String queryString = "SELECT sum(l.n_menge) FROM FLRLagerbewegung l WHERE l.b_historie=0 AND l.c_belegartnr='"
				+ belegartCNr
				+ "' AND l.i_belegartpositionid="
				+ belegartpositionIId;

		if (tStichtag != null) {
			queryString += " AND l.t_buchungszeit<='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(Helper
							.cutTimestamp(tStichtag).getTime())) + "'";
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(queryString);
		query.setMaxResults(1);

		List<?> resultList = query.list();

		if (resultList.size() > 0) {
			BigDecimal bdMenge = (BigDecimal) resultList.iterator().next();
			if (bdMenge != null) {
				mengeGesamt = bdMenge;
			}
		}
		return mengeGesamt;

		/*
		 * List<SeriennrChargennrMitMengeDto> allSnrs =
		 * getAllSeriennrchargennrEinerBelegartposition( belegartCNr,
		 * belegartpositionIId); if (allSnrs == null) { allSnrs = new
		 * ArrayList<SeriennrChargennrMitMengeDto>();
		 * SeriennrChargennrMitMengeDto s = new SeriennrChargennrMitMengeDto();
		 * allSnrs.add(s); } BigDecimal mengeGesamt = new BigDecimal(0); for
		 * (int i = 0; i < allSnrs.size(); i++) { LagerbewegungDto
		 * lagerbewegungDto = getLetzteintrag(belegartCNr, belegartpositionIId,
		 * allSnrs.get(i).getCSeriennrChargennr());
		 * 
		 * if (tStichtag == null ||
		 * tStichtag.after(lagerbewegungDto.getTBuchungszeit())) { mengeGesamt =
		 * mengeGesamt.add(lagerbewegungDto.getNMenge()); } } return
		 * mengeGesamt;
		 */
	}

	public BigDecimal getEinstandsWertEinesBeleges(String belegartCNr,
			Integer belegartIId, String sArtikelartI, TheClientDto theClientDto) {

		BigDecimal bdEinstandswert = BigDecimal.ZERO;
		if (belegartCNr == null || belegartIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("belegartCNr == null || belegartIId ==null"));
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		// ----------------------RECHNUNG-------------------
		String queryString = "SELECT distinct bew.i_belegartpositionid,bew.c_seriennrchargennr,bew.flrartikel.artikelart_c_nr,bew.i_id_buchung FROM FLRLagerbewegung AS bew WHERE bew.c_belegartnr='"
				+ belegartCNr + "' AND bew.i_belegartid='" + belegartIId + "'";
		org.hibernate.Query query = session.createQuery(queryString);
		List resultList = query.list();
		Iterator resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			Integer belegpositionIId = (Integer) o[0];
			String snrChnr = (String) o[1];
			String artikelart = (String) o[2];
			Integer i_id_buchung = (Integer) o[3];

			// je nach Artikelart beruecksichtigen

			if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				if (artikelart.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {

					// Einstandspreis ist Lief1Preis
					LagerbewegungDto bew = getJuengsteBuchungEinerBuchungsNummer(i_id_buchung);
					if (bew != null) {
						ArtikellieferantDto alDto = getArtikelFac()
								.getArtikelEinkaufspreis(
										bew.getArtikelIId(),
										null,
										bew.getNMenge(),
										theClientDto.getSMandantenwaehrung(),
										new java.sql.Date(bew.getTBelegdatum()
												.getTime()), theClientDto);
						if (alDto != null && alDto.getNNettopreis() != null) {
							bdEinstandswert = bdEinstandswert
									.add(alDto.getNNettopreis().multiply(
											bew.getNMenge()));
						}

					}
				}
			} else {
				if (!artikelart.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {

					BigDecimal einstandswertPosition = getEinstandspreis(
							belegartCNr, belegpositionIId, snrChnr);
					if (einstandswertPosition != null) {
						bdEinstandswert = bdEinstandswert
								.add(einstandswertPosition);

					}
				}
			}

		}

		return bdEinstandswert;

	}

	/**
	 * Gibt den Einstandspreis einer Belegartposition zurueck (Pro
	 * Mengeneinheit)
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartpositionIId
	 *            Integer
	 * @param cSeriennrChargennr
	 *            SNR/CHARGENNUMMER
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getEinstandspreis(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrChargennr)
			throws EJBExceptionLP {
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId ==null"));
		}
		// Hole Menge
		LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
				belegartpositionIId, cSeriennrChargennr);
		// Hole alle Urspruenge
		LagerabgangursprungDto[] alleUrspruenge = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagerbewegungDto
				.getIIdBuchung());
		BigDecimal einstandspreis = new BigDecimal(0);

		if (alleUrspruenge.length == 0) {
			einstandspreis = lagerbewegungDto.getNEinstandspreis();
		} else {

			for (int i = 0; i < alleUrspruenge.length; i++) {

				LagerbewegungDto ursprung = getJuengsteBuchungEinerBuchungsNummer(alleUrspruenge[i]
						.getILagerbewegungidursprung());

				einstandspreis = einstandspreis.add(ursprung
						.getNEinstandspreis().multiply(
								alleUrspruenge[i].getNVerbrauchtemenge()));
			}
		}

		return einstandspreis;
	}

	/**
	 * Liefert die Lagerzugangs- Buchungsnummer(n) zurueck, von denen Material
	 * fuer den gewuenschten Lagerabgang verwendet word. Derzeit wird nach dem
	 * FIFO- Prinzip Ware ausgeliefert.
	 * 
	 * @param artikelIId
	 *            4711
	 * @param lagerIId
	 *            7
	 * @param cSeriennrchargennr
	 *            g64b7435v
	 * @param fMenge
	 *            18
	 * @param theClientDto
	 *            User
	 * @throws EJBExceptionLP
	 *             artikelIId == null || lagerIId == null || fMenge == null ||
	 *             idUser == null
	 * @return int[]
	 */
	private LagerabgangursprungDto[] getVonLagerNachRegel(Integer artikelIId,
			Integer lagerIId, String cSeriennrchargennr, BigDecimal fMenge,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (artikelIId == null || lagerIId == null || fMenge == null
				|| theClientDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelIId == null || lagerIId == null || fMenge == null || theClientDto == null"));
		}

		LagerbewegungDto[] alleZugaenge = getAlleBuchungenEinesArtikelUndEinesLagers(
				artikelIId, lagerIId, new Short((short) 0), cSeriennrchargennr);

		LagerbewegungDto[] alleZugaengeMitUrsprungsdatum = ursprungsDatumEintragen(alleZugaenge);

		alleZugaengeMitUrsprungsdatum = sortiereNachDatumJuensterZuerst(alleZugaengeMitUrsprungsdatum);
		LagerbewegungDto[] alleAbgaenge = alleZugaengeMitUrsprungsdatum;

		LagerbewegungDto[] alleAbgaengeMitVerbrauchtenMengen = new LagerbewegungDto[alleAbgaenge.length];
		for (int i = 0; i < alleAbgaenge.length; i++) {
			double fMengeVorhanden = alleZugaengeMitUrsprungsdatum[i]
					.getNMenge().doubleValue();
			fMengeVorhanden = fMengeVorhanden
					- getVerbrauchteMenge(alleAbgaenge[i].getIId())
							.doubleValue();
			alleAbgaengeMitVerbrauchtenMengen[i] = alleAbgaenge[i];
			if (fMengeVorhanden < 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"FATAL_ZUVIEL_VERBRAUCHT, ARTIKEL_I_ID=" + artikelIId));
			}
			alleAbgaengeMitVerbrauchtenMengen[i].setNMenge(new BigDecimal(
					fMengeVorhanden));
			alleAbgaengeMitVerbrauchtenMengen[i].setCVersion(alleAbgaenge[i]
					.getCVersion());
		}
		LagerabgangursprungDto[] urspruenge = new LagerabgangursprungDto[alleAbgaengeMitVerbrauchtenMengen.length];
		int zaehler = -1;

		double fMengeabzubuchen = fMenge.doubleValue();

		alleAbgaengeMitVerbrauchtenMengen = sortiereNachDatumAeltesterZuerst(alleAbgaengeMitVerbrauchtenMengen);

		for (int i = 0; i < alleAbgaengeMitVerbrauchtenMengen.length; i++) {
			if (fMengeabzubuchen > 0) {
				if (alleAbgaengeMitVerbrauchtenMengen[i].getNMenge()
						.doubleValue() > 0) {
					zaehler++;
					urspruenge[zaehler] = new LagerabgangursprungDto();
					urspruenge[zaehler]
							.setILagerbewegungidursprung(alleAbgaengeMitVerbrauchtenMengen[i]
									.getIIdBuchung());
					urspruenge[zaehler]
							.setVersionAusUrsprung(alleAbgaengeMitVerbrauchtenMengen[i]
									.getCVersion());
					fMengeabzubuchen = fMengeabzubuchen
							- alleAbgaengeMitVerbrauchtenMengen[i].getNMenge()
									.doubleValue();
					if (fMengeabzubuchen >= 0) {
						urspruenge[zaehler]
								.setNVerbrauchtemenge(alleAbgaengeMitVerbrauchtenMengen[i]
										.getNMenge());
						LagerbewegungDto dtoTemp = lagerbewegungFindByPrimaryKey(alleAbgaengeMitVerbrauchtenMengen[i]
								.getIId());
						// Lagerzugang auf "Verbraucht" setzen
						setzteZugangsBuchungAlsVerbraucht(
								dtoTemp.getCBelegartnr(),
								dtoTemp.getIBelegartpositionid(),
								dtoTemp.getCSeriennrchargennr(), theClientDto,
								true);

					} else {
						urspruenge[zaehler]
								.setNVerbrauchtemenge(alleAbgaengeMitVerbrauchtenMengen[i]
										.getNMenge()
										.add(new BigDecimal(fMengeabzubuchen)));
					}
				}
			}
		}
		zaehler++;
		LagerabgangursprungDto[] urspuengeReturn = new LagerabgangursprungDto[zaehler];

		for (int i = 0; i < zaehler; i++) {
			urspuengeReturn[i] = new LagerabgangursprungDto();
			urspuengeReturn[i].setNVerbrauchtemenge(urspruenge[i]
					.getNVerbrauchtemenge());
			urspuengeReturn[i].setILagerbewegungidursprung(urspruenge[i]
					.getILagerbewegungidursprung());
			urspuengeReturn[i]
					.setNGestehungspreis(artikellagerFindByPrimaryKey(
							artikelIId, lagerIId).getNGestehungspreis());
			urspuengeReturn[i].setVersionAusUrsprung(urspruenge[i]
					.getVersionAusUrsprung());
		}
		return urspuengeReturn;
	}

	/**
	 * Wenn ein Lagerzugang urspruenglich aus einem Lagerabgang kam (z.B.:
	 * Rueckschein kan aus Lieferschein), dann muss dieses urspruengliche
	 * Lagerzugangsdatum verwendet werden, da es bei der zurueckgegebenen Ware
	 * noch immer um diese Urspruechliche Ware handelt.
	 * 
	 * @param lagerbewegungDto
	 *            LagerbewegungDto[]
	 * @throws EJBExceptionLP
	 * @return LagerbewegungDto[]
	 */
	private LagerbewegungDto[] ursprungsDatumEintragen(
			LagerbewegungDto[] lagerbewegungDto) throws EJBExceptionLP {

		for (int i = 0; i < lagerbewegungDto.length; i++) {
			Integer idBuchung = lagerbewegungDto[i].getIIdBuchung();
			Integer idBuchungUrsprung = null;

			try {
				idBuchungUrsprung = lagerzugangursprungFindIIdBuchungsursprungByLagerbewegungIIdBuchung(
						idBuchung).getILagerbewegungidursprung();
			} catch (Exception e) {
				// wir sind an der Wurzel
			}
			if (idBuchungUrsprung != null) {
				LagerabgangursprungDto dto[] = lagerabgangursprungFindByLagerbewegungIIdBuchung(idBuchungUrsprung);
				if (dto != null) {
					for (int j = 0; j < dto.length; j++) {
						LagerbewegungDto lagerbewegungDtoUrsprung = getJuengsteBuchungEinerBuchungsNummer(dto[j]
								.getILagerbewegungid());
						lagerbewegungDtoUrsprung.getTBuchungszeit();
						lagerbewegungDto[i]
								.setTBuchungszeit(lagerbewegungDtoUrsprung
										.getTBuchungszeit());
					}
				}
			}
		}
		return lagerbewegungDto;
	}

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId, String cSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto,
			ArrayList<GeraetesnrDto> alGeraetesnr,
			PaneldatenDto[] paneldatenDtos,
			boolean gestehungspreisNeuKalkulieren) {
		bucheZu(belegartCNr, belegartIId, belegartpositionIId, artikelIId,
				fMengeAbsolut, nEinstansdpreis, lagerIId, cSeriennrchargennr,
				null, tBelegdatum, theClientDto, null, null, null,
				alGeraetesnr, paneldatenDtos, gestehungspreisNeuKalkulieren);
	}

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto) {
		bucheZu(belegartCNr, belegartIId, belegartpositionIId, artikelIId,
				fMengeAbsolut, nEinstansdpreis, lagerIId, alSeriennrchargennr,
				tBelegdatum, theClientDto, null, null);
	}

	private void pruefeLagerbewegungsMengeMitSnrChnrMenge(
			BigDecimal fMengeAbsolut,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr) {

		BigDecimal bdMengeSnrChnr = new BigDecimal(0);
		if (alSeriennrchargennr != null && alSeriennrchargennr.size() > 0) {
			if (alSeriennrchargennr.size() == 1
					&& alSeriennrchargennr.get(0).getCSeriennrChargennr() == null) {
				return;
			}
			for (int i = 0; i < alSeriennrchargennr.size(); i++) {
				bdMengeSnrChnr = bdMengeSnrChnr.add(alSeriennrchargennr.get(i)
						.getNMenge());
			}
			if (fMengeAbsolut.doubleValue() != bdMengeSnrChnr.doubleValue()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH,
						new Exception(
								"fMengeAbsolut.doubleValue()!=bdMengeSnrChnr.doubleValue()"));

			}
		}
	}

	private List<SeriennrChargennrMitMengeDto> verdichteSerienChargennummern(
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr) {

		if (alSeriennrchargennr != null && alSeriennrchargennr.size() > 0) {

			HashMap<String, SeriennrChargennrMitMengeDto> hmArtikel = new HashMap<String, SeriennrChargennrMitMengeDto>();

			for (int i = 0; i < alSeriennrchargennr.size(); i++) {
				if (hmArtikel.containsKey(alSeriennrchargennr.get(i)
						.getCSeriennrChargennr())) {
					SeriennrChargennrMitMengeDto dtoTemp = hmArtikel
							.get(alSeriennrchargennr.get(i)
									.getCSeriennrChargennr());
					dtoTemp.setNMenge(alSeriennrchargennr.get(i).getNMenge()
							.add(dtoTemp.getNMenge()));
					hmArtikel.put(alSeriennrchargennr.get(i)
							.getCSeriennrChargennr(), dtoTemp);

				} else {
					hmArtikel.put(alSeriennrchargennr.get(i)
							.getCSeriennrChargennr(), alSeriennrchargennr
							.get(i));
				}
			}

			return new ArrayList<SeriennrChargennrMitMengeDto>(
					hmArtikel.values());

		} else {
			return alSeriennrchargennr;
		}

	}

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstansdpreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto,
			String belegartCNrUrsprung, Integer belegartpositionIIdUrsprung) {

		Artikel artikel = em.find(Artikel.class, artikelIId);
		// Wenn Menge >0
		if (fMengeAbsolut.doubleValue() > 0) {
			if (alSeriennrchargennr == null
					|| alSeriennrchargennr.size() == 0
					|| (alSeriennrchargennr != null
							&& alSeriennrchargennr.size() == 1 && alSeriennrchargennr
							.get(0).getCSeriennrChargennr() == null)) {
				// Kein SeriennummernChargenbehafteter Artikel
				bucheZu(belegartCNr, belegartIId, belegartpositionIId,
						artikelIId, fMengeAbsolut, nEinstansdpreis, lagerIId,
						null, null, tBelegdatum, theClientDto, null, null,
						null, null, null, true);
			} else {

				// SNR/CHNR verdichten, da sonst bei 2 gleichen Charge ein
				// Update durchgefuehrt wird

				pruefeLagerbewegungsMengeMitSnrChnrMenge(fMengeAbsolut,
						alSeriennrchargennr);

				alSeriennrchargennr = verdichteSerienChargennummern(alSeriennrchargennr);

				List<SeriennrChargennrMitMengeDto> snrVorher = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
						belegartCNr, belegartpositionIId);
				if (snrVorher != null && snrVorher.size() > 0) {

					HashMap<String, String> hmZuLoeschendeSNR = new HashMap<String, String>();
					for (int i = 0; i < snrVorher.size(); i++) {
						hmZuLoeschendeSNR.put(snrVorher.get(i)
								.getCSeriennrChargennr(), snrVorher.get(i)
								.getCSeriennrChargennr());
					}

					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						hmZuLoeschendeSNR.remove(alSeriennrchargennr.get(i)
								.getCSeriennrChargennr());
					}

					// Alle nicht mehr eingegebenen loeschen
					for (Iterator<?> iter = hmZuLoeschendeSNR.keySet()
							.iterator(); iter.hasNext();) {
						String snr2Delete = (String) iter.next();
						bucheZu(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId,
								new BigDecimal(0), // Menge
								nEinstansdpreis, lagerIId, snr2Delete,
								tBelegdatum, theClientDto, null, null, false);
					}
					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						SeriennrChargennrMitMengeDto alZeile = alSeriennrchargennr
								.get(i);

						boolean bGestpreisUpdaten = false;
						if (alZeile.getCSeriennrChargennr() == null) {
							bGestpreisUpdaten = true;
						}

						bucheZu(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId, alZeile.getNMenge(),
								nEinstansdpreis, lagerIId,
								alZeile.getCSeriennrChargennr(),
								alZeile.getCVersion(), tBelegdatum,
								theClientDto, null, null, null,
								alZeile.getAlGeraetesnr(),
								alZeile.getPaneldatenDtos(), bGestpreisUpdaten);
					}

				} else {
					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						SeriennrChargennrMitMengeDto alZeile = alSeriennrchargennr
								.get(i);
						bucheZu(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId, alZeile.getNMenge(),
								nEinstansdpreis, lagerIId,
								alZeile.getCSeriennrChargennr(),
								alZeile.getCVersion(), tBelegdatum,
								theClientDto, null, null, null,
								alZeile.getAlGeraetesnr(),
								alZeile.getPaneldatenDtos(), false);

					}
				}

			}
		} else {
			// Alle vorhandenen Seriennummmern loeschen
			if (Helper.short2boolean(artikel.getBChargennrtragend())
					|| Helper.short2boolean(artikel.getBSeriennrtragend())) {
				List<SeriennrChargennrMitMengeDto> alSeriennrchargennrVorhanden = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
						belegartCNr, belegartpositionIId);
				for (int i = 0; i < alSeriennrchargennrVorhanden.size(); i++) {
					bucheZu(belegartCNr, belegartIId, belegartpositionIId,
							artikelIId, fMengeAbsolut, nEinstansdpreis,
							lagerIId, alSeriennrchargennrVorhanden.get(i)
									.getCSeriennrChargennr(), null,
							tBelegdatum, theClientDto, null, null, null, null,
							null, false);
				}
			} else {
				bucheZu(belegartCNr, belegartIId, belegartpositionIId,
						artikelIId, fMengeAbsolut, nEinstansdpreis, lagerIId,
						null, null, tBelegdatum, theClientDto, null, null,
						null, null, null, false);
			}

		}

		if (alSeriennrchargennr != null && alSeriennrchargennr.size() > 0) {
			LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
					belegartpositionIId, alSeriennrchargennr.get(0)
							.getCSeriennrChargennr());

			if (lagerbewegungDto != null) {
				BigDecimal preis = recalcGestehungspreisAbTermin(
						lagerbewegungDto.getIId(), artikelIId,
						lagerbewegungDto.getLagerIId(), tBelegdatum,
						theClientDto);
				if (preis != null) {
					Artikellager artikellager = em.find(Artikellager.class,
							new ArtikellagerPK(artikelIId, lagerIId));
					if (artikellager != null) {
						artikellager.setNGestehungspreis(preis);
					}
				}
			}
		}

	}

	/**
	 * Bucht eine gewisse Menge eines Artikel von einem Quell-Lager in ein
	 * Ziel-Lager. Wird wir Handlagerbewegung behandelt und ist dort auch
	 * sichtbar Es ist auch m&ouml;glich, einen Quell-Artikel auf einen anderen
	 * Ziel-Artikel zu buchen. unter WW_LAGERUMBUCHUNG ist sichtbar, welche
	 * Handlagerabbuchung mit welcher Handlagerzubuchung zusammengehoert
	 * 
	 * @param artikelIId_Quelle
	 *            Integer
	 * @param lagerIId_Quelle
	 *            Integer
	 * @param artikelIId_Ziel
	 *            Integer
	 * @param lagerIId_Ziel
	 *            Integer
	 * @param fMengeUmzubuchen
	 * @param alSeriennrChargennrMitMenge
	 *            Liste der Seriennr/Chargeninfo
	 * @param sKommentar
	 *            Kommentar
	 * @param vkpreis
	 * @param theClientDto
	 *            User-ID
	 * @throws EJBExceptionLP
	 */
	public void bucheUm(Integer artikelIId_Quelle, Integer lagerIId_Quelle,
			Integer artikelIId_Ziel, Integer lagerIId_Ziel,
			BigDecimal fMengeUmzubuchen,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			String sKommentar, BigDecimal vkpreis, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// Zuerst von Quell-Lager abbuchen
		HandlagerbewegungDto handlagerbewegungDto_Quelle = new HandlagerbewegungDto();
		handlagerbewegungDto_Quelle.setArtikelIId(artikelIId_Quelle);
		handlagerbewegungDto_Quelle.setLagerIId(lagerIId_Quelle);
		handlagerbewegungDto_Quelle.setBAbgang(new Short((short) 1));
		handlagerbewegungDto_Quelle.setCKommentar(sKommentar);

		// VKPreis kommt aus Artikellager, wenn keiner angegeben
		if (vkpreis == null) {
			Artikellager artikellager = em.find(Artikellager.class,
					new ArtikellagerPK(artikelIId_Quelle, lagerIId_Quelle));

			if (artikellager == null) {
				vkpreis = new BigDecimal(0);
			} else {
				vkpreis = artikellager.getNGestehungspreis();
			}
		}

		if (alSeriennrChargennrMitMenge == null) {
			alSeriennrChargennrMitMenge = SeriennrChargennrMitMengeDto
					.erstelleDtoAusEinerChargennummer(null, fMengeUmzubuchen);
		}

		handlagerbewegungDto_Quelle.setNMenge(fMengeUmzubuchen);
		handlagerbewegungDto_Quelle.setNVerkaufspreis(vkpreis);
		handlagerbewegungDto_Quelle
				.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
		Integer belegartpositionIId_Quelle = createHandlagerbewegung(
				handlagerbewegungDto_Quelle, theClientDto);

		LagerbewegungDto[] dtosQuelle = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				LocaleFac.BELEGART_HAND, belegartpositionIId_Quelle,
				alSeriennrChargennrMitMenge.get(0).getCSeriennrChargennr());

		// Handlagerbuchung einfuegen
		HandlagerbewegungDto handlagerbewegungDto_Ziel = new HandlagerbewegungDto();
		handlagerbewegungDto_Ziel.setArtikelIId(artikelIId_Ziel);
		handlagerbewegungDto_Ziel.setLagerIId(lagerIId_Ziel);
		handlagerbewegungDto_Ziel.setBAbgang(new Short((short) 0));
		handlagerbewegungDto_Ziel.setCKommentar(sKommentar);
		handlagerbewegungDto_Ziel
				.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
		/*
		 * handlagerbewegungDto_Ziel
		 * .setCSeriennummerchargennummerUrsprung(alSeriennrChargennrMitMenge
		 * .get(i).getCSeriennrChargennr());
		 */
		handlagerbewegungDto_Ziel.setNMenge(fMengeUmzubuchen);
		handlagerbewegungDto_Ziel.setNEinstandspreis(vkpreis);
		// Ursprung eintragen:
		handlagerbewegungDto_Ziel
				.setBelegartCNrUrsprung(LocaleFac.BELEGART_HAND);
		handlagerbewegungDto_Ziel
				.setBelegartpositionIIdUrsprung(belegartpositionIId_Quelle);
		Integer belegartpositionIId_Ziel = createHandlagerbewegung(
				handlagerbewegungDto_Ziel, theClientDto);
		LagerbewegungDto[] dtosZiel = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				LocaleFac.BELEGART_HAND, belegartpositionIId_Ziel,
				alSeriennrChargennrMitMenge.get(0).getCSeriennrChargennr());

		// Buchungen zusammenhaengen, damit Zusammengehoerigkeit
		// wiederherstellbar ist
		LagerumbuchungDto umBuchungDto = new LagerumbuchungDto();
		umBuchungDto
				.setILagerbewegungidabbuchung(dtosQuelle[0].getIIdBuchung());
		umBuchungDto.setILagerbewegungidzubuchung(dtosZiel[0].getIIdBuchung());
		createLagerumbuchung(umBuchungDto);

	}

	public void bucheUmMitAngabeDerQuelle(Integer artikelIId,
			String belegartCNr_Quelle, Integer belegartIId_Quelle,
			Integer belegartpositionIId_Quelle, Integer lagerIId_Quelle,
			Integer lagerIId_Ziel, BigDecimal fMengeUmzubuchen,
			String cSeriennrchargennr, String sKommentar,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// VKPreis kommt aus Artikellager
		BigDecimal vkpreis = null;
		Artikellager artikellager = em.find(Artikellager.class,
				new ArtikellagerPK(artikelIId, lagerIId_Quelle));
		if (artikellager == null) {
			vkpreis = new BigDecimal(0);
		} else {
			vkpreis = artikellager.getNGestehungspreis();
		}

		// Zuerst von der Quelle abbuchen
		bucheAb(belegartCNr_Quelle, belegartIId_Quelle,
				belegartpositionIId_Quelle, artikelIId, fMengeUmzubuchen,
				vkpreis, lagerIId_Quelle, cSeriennrchargennr, tBelegdatum,
				theClientDto);

		LagerbewegungDto[] dtosQuelle = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				LocaleFac.BELEGART_HAND, belegartpositionIId_Quelle,
				cSeriennrchargennr);

		// Handlagerbuchung einfuegen
		HandlagerbewegungDto handlagerbewegungDto_Ziel = new HandlagerbewegungDto();
		handlagerbewegungDto_Ziel.setArtikelIId(artikelIId);
		handlagerbewegungDto_Ziel.setLagerIId(lagerIId_Ziel);
		handlagerbewegungDto_Ziel.setBAbgang(new Short((short) 0));
		handlagerbewegungDto_Ziel.setCKommentar(sKommentar);
		handlagerbewegungDto_Ziel
				.setCSeriennummerchargennummerUrsprung(cSeriennrchargennr);
		handlagerbewegungDto_Ziel.setNMenge(fMengeUmzubuchen);
		handlagerbewegungDto_Ziel.setNEinstandspreis(vkpreis);
		// Ursprung eintragen:
		handlagerbewegungDto_Ziel
				.setBelegartCNrUrsprung(LocaleFac.BELEGART_HAND);
		handlagerbewegungDto_Ziel
				.setBelegartpositionIIdUrsprung(belegartpositionIId_Quelle);
		Integer belegartpositionIId_Ziel = createHandlagerbewegung(
				handlagerbewegungDto_Ziel, theClientDto);
		LagerbewegungDto[] dtosZiel = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				LocaleFac.BELEGART_HAND, belegartpositionIId_Ziel,
				cSeriennrchargennr);
		// Buchungen zusammenhaengen, damit Zusammengehoerigkeit
		// wiederherstellbar ist
		LagerumbuchungDto umBuchungDto = new LagerumbuchungDto();
		umBuchungDto
				.setILagerbewegungidabbuchung(dtosQuelle[0].getIIdBuchung());
		umBuchungDto.setILagerbewegungidzubuchung(dtosZiel[0].getIIdBuchung());
		createLagerumbuchung(umBuchungDto);
	}

	public Integer getArtikelIIdUeberSeriennummer(String snr,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LagerbewegungfindSnr");
		query.setParameter(1, snr);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			return ((Lagerbewegung) cl.iterator().next()).getArtikelIId();
		} else {
			return null;
		}

	}

	public Integer getArtikelIIdUeberSeriennummerAbgang(String snr,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery(Lagerbewegung.QueryFindSnrAbgang);
		query.setParameter(1, snr);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			return ((Lagerbewegung) cl.iterator().next()).getArtikelIId();
		} else {
			return null;
		}
	}

	private void pruefeObSeriennummerNumerisch(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cNr == null"));
		}

		String gueltigeZeichen = "";

		gueltigeZeichen = "0123456789";

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
				ArrayList<Object> l = new ArrayList<Object>();
				l.add(new Character(c));
				l.add(cNr);
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN,
						l,
						new Exception(
								"FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN"));
			}

		}

	}

	/**
	 * Bucht eine gewisse Anzahl von Artikel, wenn angegeben auch mit Serien/
	 * Chargennummer, in ein Lager ein. Wenn Ursprung mitangegeben ist, wird
	 * eine Verbindung mit diesem hergestellt.
	 * 
	 * @param belegartCNr
	 *            Bestellung
	 * @param belegartIId
	 *            ID der Bestellung
	 * @param belegartpositionIId
	 *            563
	 * @param artikelIId
	 *            4711
	 * @param fMengeAbsolut
	 *            16
	 * @param nEinstandspreis
	 *            56,8
	 * @param lagerIId
	 *            7
	 * @param cSeriennrchargennr
	 *            DF4365FGH
	 * @param theClientDto
	 *            User-ID
	 * @param belegartCNrUrsprung
	 *            Hand
	 * @param belegartpositionIIdUrsprung
	 *            546
	 * @param cSeriennrchargennrUrsprung
	 *            DCFZ654
	 * @throws EJBExceptionLP
	 *             belegartCNr == null || belegartpositionIId == null ||
	 *             artikelIId == null oder fMengeAbsolut == null ||
	 *             nEinstansdpreis == null || lagerIId == null
	 */

	public void bucheZu(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nEinstandspreis,
			Integer lagerIId, String cSeriennrchargennr, String cVersion,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto,
			String belegartCNrUrsprung, Integer belegartpositionIIdUrsprung,
			String cSeriennrchargennrUrsprung,
			ArrayList<GeraetesnrDto> alGeraetesnr,
			PaneldatenDto[] paneldatenDtos,
			boolean gestehungspreisNeuKalkulieren) {

		if (belegartCNr == null || belegartIId == null
				|| belegartpositionIId == null || artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartIId == null || belegartpositionIId == null || artikelIId == null"));
		}
		if (tBelegdatum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tBelegdatum == null"));
		}
		if (fMengeAbsolut == null || nEinstandspreis == null
				|| lagerIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"fMengeAbsolut == null || nEinstansdpreis == null || lagerIId == null"));
		}
		if (fMengeAbsolut.doubleValue() < 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
					new Exception("fMengeAbsolut.doubleValue() < 0"));
		}
		if (belegartCNrUrsprung != null && belegartpositionIIdUrsprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNrUrsprung != null && belegartpositionIIdUrsprung==null"));
		}
		if (belegartpositionIIdUrsprung != null && belegartCNrUrsprung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIIdUrsprung != null && belegartCNrUrsprung==null"));
		}

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (Helper.short2boolean(artikel.getBChargennrtragend())
				|| Helper.short2boolean(artikel.getBSeriennrtragend())) {
			if (cSeriennrchargennr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("cSeriennrchargennr == null ArtikelNr:"
								+ artikel.getCNr()));
			} else {

				if (Helper.short2boolean(artikel.getBChargennrtragend())) {

					try {
						ParametermandantDto parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE);
						Integer ichnrlaenge = (Integer) parameter
								.getCWertAsObject();

						if (cSeriennrchargennr.length() < ichnrlaenge) {
							ArrayList al = new ArrayList();
							al.add(ichnrlaenge);

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_CHARGENNUMMER_ZU_KURZ,
									al, new Exception(
											"FEHLER_CHARGENNUMMER_ZU_KURZ"));

						}

						int iLaengeEinEindeutig = 0;
						try {
							parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ARTIKEL,
											ParameterFac.PARAMETER_LAENGE_CHARGENNUMMER_EINEINDEUTIG);
							iLaengeEinEindeutig = (Integer) parameter
									.getCWertAsObject();

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
						// PJ18589
						if (iLaengeEinEindeutig > 0
								&& !cSeriennrchargennr
										.equals(CHARGENNUMMER_KEINE_CHARGE)) {
							// Pruefen, ob Chargnenummer numerisch ist

							// SP2790 nur wenn Stkl und eigengefertigt
							StuecklisteDto stklDto = getStuecklisteFac()
									.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
											artikel.getIId(), theClientDto);

							if (stklDto != null
									&& Helper.short2boolean(stklDto
											.getBFremdfertigung()) == false) {
								try {
									Long snr = new Long(cSeriennrchargennr);
								} catch (NumberFormatException e) {
									ArrayList al = new ArrayList();
									al.add(cSeriennrchargennr);
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_CHARGENNUMMER_NICHT_NUMERISCH,
											al, new Exception(
													cSeriennrchargennr));

								}

							}

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else if (Helper.short2boolean(artikel.getBSeriennrtragend())) {
					try {
						ParametermandantDto parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);
						Integer isnrlaenge = (Integer) parameter
								.getCWertAsObject();

						ArrayList al = new ArrayList();
						al.add(isnrlaenge);

						if (cSeriennrchargennr.length() < isnrlaenge) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_KURZ,
									al, new Exception(
											"FEHLER_SERIENNUMMER_ZU_KURZ"));

						}
						parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER);
						isnrlaenge = (Integer) parameter.getCWertAsObject();

						al = new ArrayList();
						al.add(isnrlaenge);

						if (cSeriennrchargennr.length() > isnrlaenge) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_LANG,
									al, new Exception(
											"FEHLER_SERIENNUMMER_ZU_LANG"));

						}

						// PJ 17392
						parameter = getParameterFac()
								.getMandantparameter(
										theClientDto.getMandant(),
										ParameterFac.KATEGORIE_ARTIKEL,
										ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN);
						boolean bFuehrendeEntfernen = (Boolean) parameter
								.getCWertAsObject();
						if (bFuehrendeEntfernen) {
							cSeriennrchargennr = cSeriennrchargennr
									.replaceFirst("0*", "");
						}

						// PJ18555

						parameter = getParameterFac().getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH);
						boolean bSnrNumerisch = (Boolean) parameter
								.getCWertAsObject();
						if (bSnrNumerisch) {
							pruefeObSeriennummerNumerisch(cSeriennrchargennr,
									theClientDto);
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}

		}
		if (artikel.getBChargennrtragend().intValue() != 1
				&& artikel.getBSeriennrtragend().intValue() != 1) {
			if (cSeriennrchargennr != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER,
						new Exception(
								"SERIENNUMMER_CHARGENNUMMER_DARF_NICHT_ANGEGEBEN_WERDEN"));
			}
		}
		if (artikel.getBSeriennrtragend().intValue() == 1
				&& fMengeAbsolut.doubleValue() != 1
				&& fMengeAbsolut.doubleValue() != 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
					new Exception(
							"artikel.getBSeriennrtragend().intValue() == 1 && fMengeAbsolut.doubleValue()!=1"));
		}

		boolean bLagerbewirtschaftet = false;
		// Nachsehen, ob Artikel lagerbewirtschaftet ist
		if (artikel.getBLagerbewirtschaftet().shortValue() == 1) {
			bLagerbewirtschaftet = true;
		}
		// Wenn Artikel nicht Lagerbewirtschaftet ist, dann wirds automatisch
		// auf "KEIN LAGER" gebucht sonst
		// DARF NICHT auf KEIN_LAGER gebucht werden
		if (bLagerbewirtschaftet == false) {
			LagerDto tempLager = lagerFindByCNrByMandantCNr(
					LagerFac.LAGER_KEINLAGER, theClientDto.getMandant());
			if (tempLager != null) {
				lagerIId = tempLager.getIId();
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_KEIN_LAGER_NICHT_ANGELEGT,
						new Exception("LAGER_KEIN_LAGER_NICHT_ANGELEGT"));
			}
		} else {
			if (lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER,
					theClientDto.getMandant()).getIId().equals(lagerIId)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN,
						new Exception("FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN"));

			}
			if (!hatRolleBerechtigungAufLager(lagerIId, theClientDto)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER,
						new Exception(
								"FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER"));
			}
		}
		// Pruefen, ob Lager bei dem Mandanten erlaubt ist
		LagerDto lagerDto_Erlaubt = lagerFindByPrimaryKey(lagerIId);
		if (!lagerDto_Erlaubt.getMandantCNr().equals(theClientDto.getMandant())) {

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)
					|| (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
							theClientDto) && getMandantFac()
							.darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
									theClientDto))) {
				// PJ18610
				if (!belegartCNr.equals(LocaleFac.BELEGART_LSZIELLAGER)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FALSCHER_MANDANT,
							new Exception("FEHLER_FALSCHER_MANDANT"));
				}

			}

		}

		if (bLagerLogging == null) {
			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_LAGER_LOGGING);
				bLagerLogging = Helper.short2boolean(new Short(parameter
						.getCWert()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		if (bLagerLogging) {
			String logEintrag = "VOR ZUBUCHUNG:belegartCNr=" + belegartCNr
					+ ",belegartIId=" + belegartIId + ",belegartpositionIId="
					+ belegartpositionIId + ",artikelIId=" + artikelIId
					+ ",fMengeAbsolut=" + fMengeAbsolut + ",nEinstandspreis="
					+ nEinstandspreis + ",lagerIId=" + lagerIId
					+ ",cSeriennrchargennr=" + cSeriennrchargennr
					+ ",tBelegdatum=" + tBelegdatum + ",theClientDto="
					+ theClientDto.toString();

			// Lagerstand + Gestpreis hier abrufen

			double lagerstandVorher = 0;
			double gestpreis = 0;

			try {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager != null) {
					lagerstandVorher = artikellager.getNLagerstand()
							.doubleValue();
					gestpreis = artikellager.getNGestehungspreis()
							.doubleValue();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logEintrag += ";Lagerstand=" + lagerstandVorher + ",Gestpreis="
					+ gestpreis;

			erstelleProtokollEintragZubuchung(theClientDto, logEintrag, true);
		}

		// Ist Buchung eine Korrekturbuchung oder nicht
		LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
				belegartpositionIId, cSeriennrchargennr);

		if (lagerbewegungDto != null) {
			// es ist eine Korrektur
			lagerbewegungDto.setTBelegdatum(tBelegdatum);
			if (!artikelIId.equals(lagerbewegungDto.getArtikelIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT,
						new Exception("LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT"));

			}

			if (cSeriennrchargennr != null) {
				if (!cSeriennrchargennr.equals(lagerbewegungDto
						.getCSeriennrchargennr())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT,
							new Exception(
									"LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT"));
				}
			}
			if (!lagerIId.equals(lagerbewegungDto.getLagerIId())
					&& Helper.short2boolean(artikel.getBLagerbewirtschaftet()) == true) {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_UPDATE_AUF_LAGER_NICHT_ERLAUBT,
						new Exception("LAGER_UPDATE_AUF_LAGER_NICHT_ERLAUBT"));

			}
			if (lagerbewegungDto.getNMenge().compareTo(fMengeAbsolut) == 0) {
				// Es ist nur eine Preisaenderung
				// BigDecimal diffEinstandspreis =
				// nEinstandspreis.subtract(lagerbewegungDto
				// .getNEinstandspreis());

				// Gemittelter Einstandspreispreis in WW_ARTIKELLAGER updaten
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET,
							new Exception("ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET"));
				}
				// double lagerstand =
				// artikellager.getNLagerstand().doubleValue();
				lagerbewegungDto.setNEinstandspreis(nEinstandspreis);
				updateLagerbewegung(lagerbewegungDto, theClientDto);

				if (gestehungspreisNeuKalkulieren == true) {

					BigDecimal preis = recalcGestehungspreisAbTermin(
							lagerbewegungDto.getIId(), artikelIId, lagerIId,
							lagerbewegungDto.getTBelegdatum(), theClientDto);

					if (preis != null) {

						artikellager.setNGestehungspreis(preis);
						lagerbewegungDto.setNGestehungspreis(preis);
					}
				}
				// PJ18452
				if (Helper.short2boolean(artikel.getBChargennrtragend())
						&& paneldatenDtos != null) {
					chargenEingenschaftenEinerBuchungUpdaten(paneldatenDtos,
							lagerbewegungDto.getIIdBuchung());
				}

			} else {
				// es ist Mengen und ev. Preisaenderung
				Double m = getVerbrauchteMenge(lagerbewegungDto.getIId());
				if (fMengeAbsolut.doubleValue() < m.doubleValue()
						&& bLagerbewirtschaftet == true) {
					EJBExceptionLP ex = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH,
							new Exception(
									"FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH"));
					ArrayList<Object> alInfo = new ArrayList<Object>();
					alInfo.add(artikel.getCNr());
					alInfo.add(m.doubleValue());
					ex.setAlInfoForTheClient(alInfo);
					throw ex;
				} else {
					// Alter Eintrag wird Historie und bekommt die
					// Aenderungszeit
					Timestamp tBuchungszeitOriginal = lagerbewegungDto
							.getTBuchungszeit();
					lagerbewegungDto.setTBuchungszeit(new Timestamp(System
							.currentTimeMillis()));
					lagerbewegungDto.setBHistorie(Helper.boolean2Short(true));
					updateLagerbewegung(lagerbewegungDto, theClientDto);

					// Korrektureintrag anlegen, bekommt die original
					// Buchungszeit
					LagerbewegungDto lagerbewegungDtoKorrektur = new LagerbewegungDto();
					lagerbewegungDtoKorrektur
							.setTBuchungszeit(tBuchungszeitOriginal);
					lagerbewegungDtoKorrektur
							.setBHistorie(new Short((short) 0));
					lagerbewegungDtoKorrektur.setArtikelIId(artikelIId);
					lagerbewegungDtoKorrektur.setTBelegdatum(tBelegdatum);
					lagerbewegungDtoKorrektur.setBAbgang(new Short((short) 0));
					lagerbewegungDtoKorrektur.setCBelegartnr(belegartCNr);
					lagerbewegungDtoKorrektur.setIBelegartid(belegartIId);
					lagerbewegungDtoKorrektur
							.setIBelegartpositionid(belegartpositionIId);
					lagerbewegungDtoKorrektur
							.setCSeriennrchargennr(cSeriennrchargennr);
					lagerbewegungDtoKorrektur.setCVersion(cVersion);
					lagerbewegungDtoKorrektur.setNMenge(fMengeAbsolut);

					// Geraetesnrs loeschen
					if (lagerbewegungDtoKorrektur.getNMenge().doubleValue() == 0) {
						Query query = em
								.createNamedQuery("GeraetesnrfindByIIdBuchung");
						query.setParameter(1, lagerbewegungDto.getIIdBuchung());
						Collection c = query.getResultList();
						Iterator<?> iterator = c.iterator();
						int i = 0;
						while (iterator.hasNext()) {
							Geraetesnr gsnr = (Geraetesnr) iterator.next();
							em.remove(gsnr);
							em.flush();
						}
					}

					if (fMengeAbsolut.doubleValue() == m.doubleValue()) {
						lagerbewegungDtoKorrektur
								.setBVollstaendigverbraucht(new Short((short) 1));
					} else {
						if (bLagerbewirtschaftet == true) {
							lagerbewegungDtoKorrektur
									.setBVollstaendigverbraucht(new Short(
											(short) 0));
						} else {
							lagerbewegungDtoKorrektur
									.setBVollstaendigverbraucht(new Short(
											(short) 1));
						}
					}
					lagerbewegungDtoKorrektur.setLagerIId(lagerIId);
					lagerbewegungDtoKorrektur.setIIdBuchung(lagerbewegungDto
							.getIIdBuchung());
					lagerbewegungDtoKorrektur
							.setNGestehungspreis(lagerbewegungDto
									.getNGestehungspreis());
					// BigDecimal diffEinstandspreis =
					// nEinstandspreis.subtract(lagerbewegungDto
					// .getNEinstandspreis());
					lagerbewegungDtoKorrektur
							.setNEinstandspreis(nEinstandspreis);
					createLagerbewegung(lagerbewegungDtoKorrektur, theClientDto);

					// 2481
					if (cSeriennrchargennr != null) {
						versionInLagerbewegungUpdaten(belegartCNr,
								belegartpositionIId, cSeriennrchargennr,
								cVersion);
					}

					// PJ18452
					if (Helper.short2boolean(artikel.getBChargennrtragend())
							&& paneldatenDtos != null) {
						chargenEingenschaftenEinerBuchungUpdaten(
								paneldatenDtos,
								lagerbewegungDtoKorrektur.getIIdBuchung());
					}

					if (fMengeAbsolut.doubleValue() == m.doubleValue()) {
						setzteZugangsBuchungAlsVerbraucht(belegartCNr,
								belegartpositionIId, cSeriennrchargennr,
								theClientDto, true);
					} else {
						setzteZugangsBuchungAlsVerbraucht(belegartCNr,
								belegartpositionIId, cSeriennrchargennr,
								theClientDto, false);
					}
					// Lagerstand in WW_ARTIKELLAGER updaten
					Artikellager artikellager = em.find(Artikellager.class,
							new ArtikellagerPK(artikelIId, lagerIId));
					if (artikellager == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET,
								new Exception(
										"ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET"));
					}
					BigDecimal lagerstandBisher = artikellager.getNLagerstand();
					BigDecimal lagerstandNeu = lagerstandBisher
							.add(fMengeAbsolut.subtract(lagerbewegungDto
									.getNMenge()));

					if (bLagerbewirtschaftet == true) {
						artikellager.setNLagerstand(lagerstandNeu);
					} else {
						artikellager.setNLagerstand(new BigDecimal(0));
					}
					if (gestehungspreisNeuKalkulieren == true) {
						BigDecimal preis = recalcGestehungspreisAbTermin(
								lagerbewegungDtoKorrektur.getIId(), artikelIId,
								lagerIId,
								lagerbewegungDtoKorrektur.getTBelegdatum(),
								theClientDto);
						if (preis != null && lagerstandNeu.doubleValue() != 0) {
							artikellager.setNGestehungspreis(preis);
						}
					}
					em.merge(artikellager);
					em.flush();
				}
			}
		} else {
			// es ist keine Aenderung
			if (fMengeAbsolut.doubleValue() == 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
						new Exception("fMengeAbsolut.doubleValue() == 0"));
			}

			// PJ 16623
			if (cSeriennrchargennr != null) {
				try {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG);
					boolean bSnrEindeutig = Helper.short2boolean(new Short(
							parameter.getCWert()));

					if (bSnrEindeutig == true) {

						Query query = em
								.createNamedQuery("LagerbewegungfindSnr");
						query.setParameter(1, cSeriennrchargennr);
						Collection<?> cl = query.getResultList();
						if (cl.size() > 0) {
							ArrayList al = new ArrayList();
							al.add(cSeriennrchargennr);

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN,
									al, new Exception(
											"FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN: "
													+ cSeriennrchargennr));
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			// Wenn Artikel SNR-Tragend und die Seriennummer schon einmal
			// gebucht worden ist, dann Fehler
			if (cSeriennrchargennr != null
					&& Helper.short2boolean(artikel.getBSeriennrtragend()) == true) {
				BigDecimal d = getMengeAufLager(artikelIId, lagerIId,
						cSeriennrchargennr, theClientDto);
				if (d.doubleValue() > 0) {
					ArrayList al = new ArrayList();
					al.add(cSeriennrchargennr);
					throw new EJBExceptionLP(
							EJBExceptionLP.LAGER_SERIENNUMMER_SCHON_VORHANDEN,
							al, new Exception(
									"LAGER_SERIENNUMMER_SCHON_VORHANDEN"));
				}

			}

			LagerbewegungDto lagerbewegungDtoNeueintrag = new LagerbewegungDto();
			lagerbewegungDtoNeueintrag.setArtikelIId(artikelIId);
			lagerbewegungDtoNeueintrag.setBAbgang(new Short((short) 0));
			lagerbewegungDtoNeueintrag
					.setBHistorie(Helper.boolean2Short(false));
			lagerbewegungDtoNeueintrag.setTBelegdatum(tBelegdatum);
			if (bLagerbewirtschaftet == true) {
				lagerbewegungDtoNeueintrag
						.setBVollstaendigverbraucht(new Short((short) 0));
			} else {
				lagerbewegungDtoNeueintrag
						.setBVollstaendigverbraucht(new Short((short) 1));

			}
			lagerbewegungDtoNeueintrag.setCBelegartnr(belegartCNr);
			lagerbewegungDtoNeueintrag.setIBelegartid(belegartIId);
			lagerbewegungDtoNeueintrag
					.setIBelegartpositionid(belegartpositionIId);
			lagerbewegungDtoNeueintrag
					.setCSeriennrchargennr(cSeriennrchargennr);
			lagerbewegungDtoNeueintrag.setCVersion(cVersion);
			lagerbewegungDtoNeueintrag.setNMenge(fMengeAbsolut);
			lagerbewegungDtoNeueintrag.setLagerIId(lagerIId);
			lagerbewegungDtoNeueintrag.setNEinstandspreis(nEinstandspreis);
			lagerbewegungDtoNeueintrag.setNGestehungspreis(new BigDecimal(0));

			// Neue Nuchungsnummer erzeugen
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_LAGERBEWEGUNG_I_ID_BUCHUNG);
			lagerbewegungDtoNeueintrag.setIIdBuchung(pk);
			createLagerbewegung(lagerbewegungDtoNeueintrag, theClientDto);

			// Geraeteseriennummern
			if (alGeraetesnr != null && alGeraetesnr.size() > 0) {
				for (int i = 0; i < alGeraetesnr.size(); i++) {

					// durch SP 664 auskommentiert:
					// wg. folgenden Fall:
					// Der Artikel XY mit SNR 12345 wird in einem Los verbaut
					// und abgeliefert.
					// Wenn das Bauteil jedoch wieder zurueckkommt (z.b. mit
					// einer Handbuchung), dann kann es in einem anderen Los
					// (bzw. auch im selben) nochmals verwendet werden, dahetr
					// darf die Meldung nicht mehr angezeigt werden
					// Ausserdem kann ein Artikel mit einer best. Seriennummer
					// auf ein Los gebucht werden. Danach kann der Artikel mit
					// der gleichen Seriennummer wieder (z.b. per Hand) aufs
					// Lager gebucht werden und anschliessend wieder auf das
					// selbe Los zugebucht werden.
					/*
					 * // Zuerst nachsehen, ob die Geraetesnr nicht schon mal //
					 * zugebucht worden ist. Query query = em
					 * .createNamedQuery("GeraetesnrfindByArtikelIIdCSnr");
					 * query.setParameter(1,
					 * alGeraetesnr.get(i).getArtikelIId());
					 * query.setParameter(2, alGeraetesnr.get(i).getCSnr());
					 * Collection c = query.getResultList(); if (c.size() > 0) {
					 * throw new EJBExceptionLP(
					 * EJBExceptionLP.FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT, new
					 * Exception( "FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT")); }
					 */

					Integer pkGsnr = pkGen
							.getNextPrimaryKey(PKConst.PK_LAGERBEWEGUNG_I_ID_BUCHUNG);
					Geraetesnr gsnr = new Geraetesnr(pkGsnr, alGeraetesnr
							.get(i).getCSnr(), pk, alGeraetesnr.get(i)
							.getArtikelIId());
					em.merge(gsnr);
					em.flush();
				}

			}

			// PJ18452
			if (Helper.short2boolean(artikel.getBChargennrtragend())
					&& paneldatenDtos != null) {
				chargenEingenschaftenEinerBuchungUpdaten(paneldatenDtos,
						lagerbewegungDtoNeueintrag.getIIdBuchung());
			}

			// Lagerstand in WW_ARTIKELLAGER updaten
			Artikellager artikellager = em.find(Artikellager.class,
					new ArtikellagerPK(artikelIId, lagerIId));
			if (artikellager == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET,
						new Exception("ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET"));
			}
			BigDecimal lagerstandNeu = artikellager.getNLagerstand();
			BigDecimal lagerstandBisher = artikellager.getNLagerstand();
			lagerstandNeu = lagerstandBisher.add(fMengeAbsolut);

			if (bLagerbewirtschaftet == true) {
				artikellager.setNLagerstand(Helper.rundeKaufmaennisch(
						lagerstandNeu, 4));
			} else {
				artikellager.setNLagerstand(new BigDecimal(0));
			}

			BigDecimal gestpreisBisher = artikellager.getNGestehungspreis();
			if (lagerstandNeu.doubleValue() != 0) {
				BigDecimal gestpreisNeu = (gestpreisBisher
						.multiply(lagerstandBisher).add(fMengeAbsolut
						.multiply(nEinstandspreis))).divide(lagerstandNeu, 4,
						BigDecimal.ROUND_HALF_EVEN);

				artikellager.setNGestehungspreis(gestpreisNeu);
				lagerbewegungDtoNeueintrag.setNGestehungspreis(gestpreisNeu);
			} else {
				artikellager.setNGestehungspreis(new BigDecimal((double) 0));
				lagerbewegungDtoNeueintrag.setNGestehungspreis(new BigDecimal(
						(double) 0));
			}

			updateLagerbewegung(lagerbewegungDtoNeueintrag, theClientDto);

			if (belegartCNrUrsprung != null
					&& belegartpositionIIdUrsprung != null) {
				// Wenn Ursprung angegeben wurde, dann Buchungsnummer des
				// Ursprungen herausfunden
				LagerbewegungDto[] dtos = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
						belegartCNrUrsprung, belegartpositionIIdUrsprung,
						cSeriennrchargennrUrsprung);
				if (dtos.length < 1) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LAGERBEWEGUNGSURSPRUNG_NICHT_AUFFINDBAR,
							new Exception("dtos.length<1"));
				}
				// Mit Ursprung verketten
				LagerzugangursprungDto zugangursprungDto = new LagerzugangursprungDto();
				zugangursprungDto.setILagerbewegungid(pk);
				zugangursprungDto
						.setILagerbewegungidursprung(sortiereNachDatumJuensterZuerst(dtos)[0]
								.getIIdBuchung());
				createLagerzugangursprung(zugangursprungDto);
			}

			// Wenn nach dem Belegdatum noch Buchungen vorhanden sind, dann neu
			// aufrollen

			String queryString = "SELECT l FROM FLRLagerbewegung l WHERE l.artikel_i_id="
					+ artikelIId
					+ " AND l.lager_i_id="
					+ lagerIId
					+ " AND l.t_belegdatum>'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(
							tBelegdatum.getTime())) + "'";
			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session.createQuery(queryString);
			query.setMaxResults(1);

			List<?> resultList = query.list();

			if (resultList.size() > 0 && gestehungspreisNeuKalkulieren == true) {
				// Gestehungspreis neu aufrollen
				BigDecimal preis = recalcGestehungspreisAbTermin(
						lagerbewegungDtoNeueintrag.getIId(), artikelIId,
						lagerIId, Helper.cutTimestamp(tBelegdatum),
						theClientDto);
				if (preis != null && lagerstandNeu.doubleValue() != 0) {
					artikellager.setNGestehungspreis(preis);
				}
			}

			session.close();

		}
		if (bLagerLogging) {
			double lagerstandNachher = 0;
			double gestpreisNachher = 0;

			try {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager != null) {
					lagerstandNachher = artikellager.getNLagerstand()
							.doubleValue();
					gestpreisNachher = artikellager.getNGestehungspreis()
							.doubleValue();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String logEintrag = "LagerstandNachBuchung=" + lagerstandNachher
					+ ",GestpreisNachBuchung=" + gestpreisNachher;
			erstelleProtokollEintragZubuchung(theClientDto, logEintrag, false);
		}

	}

	private void chargenEingenschaftenEinerBuchungUpdaten(PaneldatenDto[] dtos,
			Integer iId_Buchung) {

		if (dtos != null) {

			for (int i = 0; i < dtos.length; i++) {
				dtos[i].setCKey(iId_Buchung + "");
				dtos[i].setCDatentypkey("java.lang.String");
			}

			getPanelFac().createPaneldaten(dtos);

		}

	}

	public boolean hatRolleBerechtigungAufLager(Integer lagerIId,
			TheClientDto theClientDto) {
		// PJ 15712
		try {
			Integer systemrolleIId = theClientDto.getSystemrolleIId();
			if (systemrolleIId == null) {
				systemrolleIId = getTheJudgeFac().getSystemrolleIId(
						theClientDto);
			}
			Query query = em
					.createNamedQuery("LagerrollefindBySystemrolleIIdLagerIId");
			query.setParameter(1, systemrolleIId);
			query.setParameter(2, lagerIId);
			Lagerrolle temp = (Lagerrolle) query.getSingleResult();
			return true;
		} catch (NoResultException ex1) {
			// keine Lagerberechtigung
			return false;
		} catch (EJBExceptionLP ex2) {
			// Systemrolle nicht vorhanden
			return false;
		}
	}

	private void erstelleProtokollEintragZubuchung(TheClientDto theClientDto,
			String logEintrag, boolean bStacktrace) {
		ProtokollDto protokollDto = new ProtokollDto();

		protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_LAGER_ZU);
		protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);

		protokollDto.setCText(logEintrag);
		if (bStacktrace)
			protokollDto.setCLangtext(getLPStackTrace(new Throwable()
					.getStackTrace()));
		erstelleProtokollEintrag(protokollDto, theClientDto);
	}

	private BigDecimal recalcGestehungspreisAbTermin(Integer bewegungIId,
			Integer artikelIId, Integer lagerIId, Timestamp startTermin,
			TheClientDto theClientDto) {

		// Ohne Uhrzeit, da Lagerabgaenge keine Uhrzeit im Belegdatum haben.
		startTermin = Helper.cutTimestamp(startTermin);

		// wegen vordatierter Abbuchungen pruefen
		Timestamp tVordatiert = getAbbuchungenVordatiert(bewegungIId,
				startTermin);
		if (tVordatiert.getTime() < startTermin.getTime()) {
			startTermin = tVordatiert;
			startTermin = Helper.cutTimestamp(startTermin);
		}
		Session session = FLRSessionFactory.getFactory().openSession();
		Dialect dialect = ((SessionFactoryImplementor) session
				.getSessionFactory()).getDialect();
		String sQuery = null;
		if (dialect instanceof org.hibernate.dialect.SQLServerDialect) {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, CONVERT(CHAR(10), T_BELEGDATUM, 102) AS T_BELEGDATUM, T_BUCHUNGSZEIT "
					+ "from WW_LAGERBEWEGUNG "
					+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 and T_BELEGDATUM >= ? "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), T_BELEGDATUM, 102), T_BUCHUNGSZEIT, B_ABGANG, I_ID, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, CONVERT(CHAR(10), B.T_BELEGDATUM, 102) AS T_BELEGDATUM, T_BUCHUNGSZEIT "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where LAGER_I_ID=?  and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 and T_BELEGDATUM >= ? "
					+ "group by LAGER_I_ID, CONVERT(CHAR(10), T_BELEGDATUM, 102), B.T_BUCHUNGSZEIT, B_ABGANG, B.I_ID, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		} else {
			sQuery = "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, CAST(T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM, T_BUCHUNGSZEIT "
					+ "from WW_LAGERBEWEGUNG "
					+ "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 and T_BELEGDATUM >= ? "
					+ "group by LAGER_I_ID, CAST(T_BELEGDATUM AS CHAR(10)), T_BUCHUNGSZEIT, B_ABGANG, I_ID, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
					+ "union "
					+ "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, CAST(T_BELEGDATUM AS CHAR(10)) AS T_BELEGDATUM, T_BUCHUNGSZEIT "
					+ "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
					+ " where LAGER_I_ID=?  and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 and T_BELEGDATUM >= ? "
					+ "group by LAGER_I_ID, CAST(B.T_BELEGDATUM AS CHAR(10)), B.T_BUCHUNGSZEIT, B_ABGANG, B.I_ID, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
					+ "order by LAGER_I_ID, T_BELEGDATUM, T_BUCHUNGSZEIT, B_ABGANG";
		}

		org.hibernate.Query query = session.createSQLQuery(sQuery);
		query.setParameter(0, lagerIId);
		query.setParameter(1, artikelIId);
		query.setParameter(2, startTermin);
		query.setParameter(3, lagerIId);
		query.setParameter(4, artikelIId);
		query.setParameter(5, startTermin);
		List<?> list = query.list();

		ArtikelGestehungspreisCalc agc = new ArtikelGestehungspreisCalc(list);
		session.close();

		BigDecimal bdLsZeitpunkt = getLagerstandVorZeitpunktBelegdatum(
				artikelIId, lagerIId, startTermin, theClientDto);
		BigDecimal bdGestpreisZeitpunkt = getGestehungspreisVorZeitpunktBelegdatum(
				artikelIId, lagerIId, startTermin, theClientDto);

		// agc.save2Csv("c:/temp/" + artikelIId.toString() + "_v_" + new
		// Long(System.currentTimeMillis()).toString() + ".csv");
		agc.doRecalc(lagerIId, bdLsZeitpunkt,
				bdGestpreisZeitpunkt.multiply(bdLsZeitpunkt),
				bdGestpreisZeitpunkt);

		// agc.save2Csv("c:/temp/" + artikelIId.toString() + ".csv");
		// agc.saveUpdateSQL("c:/temp/" + artikelIId.toString() + ".sql");
		// agc.save2Csv("c:/temp/" + artikelIId.toString() + "_n_" + new
		// Long(System.currentTimeMillis()).toString() + ".csv");
		/*
		 * if (agc.hasDataError()) { //
		 * ERSTE_BUCHUNG_ZUM_ZEITPUNKT_MUSS_ZUGANGSBUCHUNG_SEIN // TODO:
		 * Aufrufer sollte Fehler fangen throw new
		 * EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
		 * "Erste Buchung muss eine Zugangsbuchung sein"); }
		 */
		if (agc.hasGestPreisDiff() && !agc.hasOverflow()) {
			// ES GIBT EINE DIFFERENZ UPDATE AUSFUEHREN
			Iterator<String> it = agc.iteratorSql();
			session = FLRSessionFactory.getFactory().openSession();
			while (it.hasNext()) {
				String sql = it.next();
				session.createSQLQuery(sql).executeUpdate();
			}
			session.close();
		}

		if (agc.hasOverflow()) {
			return null;
		}

		return agc.getGestehungspreisNeu();
	}

	private Timestamp getAbbuchungenVordatiert(Timestamp startTermin) {
		String sQuery = "select MIN(T_BELEGDATUM) from WW_LAGERBEWEGUNG "
				+ "WHERE B_HISTORIE = 0 AND B_ABGANG = 1 AND N_MENGE <> 0 AND T_BELEGDATUM < ? AND I_ID_BUCHUNG IN "
				+ "(select I_LAGERBEWEGUNGID from WW_LAGERABGANGURSPRUNG where I_LAGERBEWEGUNGIDURSPRUNG  IN "
				+ "(select I_ID_BUCHUNG from WW_LAGERBEWEGUNG WHERE B_ABGANG = 0 AND B_HISTORIE=0 AND T_BELEGDATUM = ?))";
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createSQLQuery(sQuery);
		query.setParameter(0, startTermin);
		query.setParameter(1, startTermin);
		List<?> list = query.list();
		Timestamp ts = null;
		if (list.size() == 0) {
			ts = startTermin;
		} else {
			ts = (Timestamp) list.get(0);
		}

		return ts;
	}

	private Timestamp getAbbuchungenVordatiert(Integer bewegungIId,
			Timestamp startTermin) {
		String sQuery = "select MIN(T_BELEGDATUM) from WW_LAGERBEWEGUNG "
				+ "WHERE B_HISTORIE = 0 AND B_ABGANG = 1 AND N_MENGE <> 0 AND T_BELEGDATUM < ? AND I_ID_BUCHUNG IN "
				+ "(select I_LAGERBEWEGUNGID from WW_LAGERABGANGURSPRUNG where I_LAGERBEWEGUNGIDURSPRUNG  = "
				+ "(select I_ID_BUCHUNG from WW_LAGERBEWEGUNG WHERE I_ID=? ))";
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query query = session.createSQLQuery(sQuery);
		query.setParameter(0, startTermin);
		query.setParameter(1, bewegungIId);
		List<?> list = query.list();
		Timestamp ts = null;
		if (list.size() == 0) {
			ts = startTermin;
		} else {
			ts = (Timestamp) list.get(0);
			if (ts == null)
				ts = startTermin;
		}

		return ts;
	}

	public void setzteZugangsBuchungAlsVerbraucht(String belegartCNr,
			Integer belegartpositionIId, String cSeriennrchargennr,
			TheClientDto theClientDto, boolean bVerbraucht)
			throws EJBExceptionLP {
		LagerbewegungDto[] lagerbewegungDto = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				belegartCNr, belegartpositionIId, cSeriennrchargennr);
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}
		for (int i = 0; i < lagerbewegungDto.length; i++) {
			if (lagerbewegungDto[i].getBAbgang().intValue() > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"BUCHUNG_IST_KEINE_ZUGANGSBUCHUNG BELEGART_C_NR="
										+ belegartCNr + " BELEGARTPOSITIONID="
										+ belegartpositionIId));

			}
			if (bVerbraucht == true) {
				lagerbewegungDto[i].setBVollstaendigverbraucht(new Short(
						(short) 1));
			} else {
				lagerbewegungDto[i].setBVollstaendigverbraucht(new Short(
						(short) 0));
			}
			updateLagerbewegung(lagerbewegungDto[i], theClientDto);
		}

	}

	public void setzteZugangsBuchungAlsVerbraucht(Integer iIdBuchung,
			boolean bVerbraucht, TheClientDto theClientDto) {
		LagerbewegungDto[] lagerbewegungDto = lagerbewegungFindByIIdBuchung(iIdBuchung);

		for (int i = 0; i < lagerbewegungDto.length; i++) {
			if (lagerbewegungDto[i].getBAbgang().intValue() > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception(
								"BUCHUNG_IST_KEINE_ZUGANGSBUCHUNG I_ID_BUCHUNG="
										+ iIdBuchung));

			}
			if (bVerbraucht == true) {
				lagerbewegungDto[i].setBVollstaendigverbraucht(new Short(
						(short) 1));
			} else {
				lagerbewegungDto[i].setBVollstaendigverbraucht(new Short(
						(short) 0));
			}
			updateLagerbewegung(lagerbewegungDto[i], theClientDto);
		}

	}

	/**
	 * Bucht mehrere Seriennummern ab. Derzeit nur Komma als Trenner
	 * m&ouml;glich
	 * 
	 * @param belegartCNr
	 *            String
	 * @param belegartIId
	 *            Integer
	 * @param belegartpositionIId
	 *            Integer
	 * @param artikelIId
	 *            Integer
	 * @param iMenge
	 *            Integer
	 * @param nVerkaufspreis
	 *            BigDecimal
	 * @param lagerIId
	 *            Integer
	 * @param sSeriennummern
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void bucheSeriennummernAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId, Integer iMenge,
			BigDecimal nVerkaufspreis, Integer lagerIId, String sSeriennummern,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP {

		String[] sSeriennummernZerlegt = Helper
				.erzeugeStringArrayAusString(sSeriennummern);

		for (int i = 0; i < iMenge.intValue(); i++) {
			bucheAb(belegartCNr, belegartIId, belegartpositionIId, artikelIId,
					new BigDecimal(1), nVerkaufspreis, lagerIId,
					sSeriennummernZerlegt[i], tBelegdatum, theClientDto);
		}

	}

	public String wirdLagermindeststandUnterschritten(
			java.sql.Date tPositionsdatum, BigDecimal bdPositionsmenge,
			Integer artikelIId, TheClientDto theClientDto) {
		try {

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					artikelIId, theClientDto);
			double dLagermindeststand = 0;
			if (artikelDto.getFLagermindest() != null) {
				dLagermindeststand = artikelDto.getFLagermindest();
			}
			ArrayList<BewegungsvorschauDto> al = getInternebestellungFac()
					.getBewegungsvorschauSortiert(artikelIId, false,
							theClientDto);

			BigDecimal anfangslagerstand = new BigDecimal(0);
			LagerDto[] allelaegerDtos = getLagerFac().lagerFindByMandantCNr(
					theClientDto.getMandant());

			for (int i = 0; i < allelaegerDtos.length; i++) {
				if (Helper.short2boolean(allelaegerDtos[i]
						.getBInternebestellung())) {
					anfangslagerstand = anfangslagerstand.add(getLagerFac()
							.getLagerstand(artikelIId,
									allelaegerDtos[i].getIId(), theClientDto));
				}
			}

			BewegungsvorschauDto[] returnArray = new BewegungsvorschauDto[al
					.size()];
			BewegungsvorschauDto[] dtos = (com.lp.server.bestellung.service.BewegungsvorschauDto[]) al
					.toArray(returnArray);

			for (int i = 0; i < dtos.length; i++) {
				BewegungsvorschauDto dto = dtos[i];

				if (tPositionsdatum.getTime() <= dto.getTLiefertermin()
						.getTime()) {
					anfangslagerstand = anfangslagerstand.add(dto.getNMenge());

					if (anfangslagerstand.doubleValue() < dLagermindeststand) {

						BigDecimal diff = new BigDecimal(dLagermindeststand)
								.subtract(anfangslagerstand);
						MessageFormat mf = new MessageFormat(
								getTextRespectUISpr(
										"lp.error.position.lagermindeststandunterschritten",
										theClientDto.getMandant(),
										theClientDto.getLocUi()));
						mf.setLocale(theClientDto.getLocUi());
						Object pattern[] = {
								Helper.formatDatum(dto.getTLiefertermin(),
										theClientDto.getLocUi()),
								Helper.formatZahl(diff, 2,
										theClientDto.getLocUi())
										+ " "
										+ artikelDto.getEinheitCNr().trim() };
						return mf.format(pattern);
					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;

	}

	/**
	 * Gibt die Anzahl der gewuenschten Seriennummern/Chargennummern auf Lager
	 * an.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param lagerIId
	 *            Integer
	 * @param cMehrereSeriennrchargennr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return Double
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getMengeMehrererSeriennummernChargennummernAufLager(
			Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> cMehrereSeriennrchargennr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (cMehrereSeriennrchargennr != null) {

			BigDecimal menge = new BigDecimal(0);

			for (int i = 0; i < cMehrereSeriennrchargennr.size(); i++) {
				menge = menge.add(getMengeAufLager(artikelIId, lagerIId,
						cMehrereSeriennrchargennr.get(i)
								.getCSeriennrChargennr(), theClientDto));
			}
			return menge;
		} else {
			return getMengeAufLager(artikelIId, lagerIId, null, theClientDto);
		}
	}

	public void versionInLagerbewegungUpdaten(String belegartCNr,
			Integer belegartpositionIId, String cVersion) {
		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();

		while (it.hasNext()) {
			Lagerbewegung l = (Lagerbewegung) it.next();

			Query query2 = em.createNamedQuery("LagerbewegungfindByIIdBuchung");
			query2.setParameter(1, l.getIIdBuchung());
			Collection cl2 = query2.getResultList();

			Iterator it2 = cl2.iterator();

			while (it2.hasNext()) {
				Lagerbewegung l2 = (Lagerbewegung) it2.next();
				l2.setCVersion(cVersion);
				em.merge(l2);
				em.flush();
			}

		}

	}

	public void versionInLagerbewegungUpdaten(String belegartCNr,
			Integer belegartpositionIId, String snrChnr, String cVersion) {
		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		query.setParameter(3, snrChnr);
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();

		while (it.hasNext()) {
			Lagerbewegung l = (Lagerbewegung) it.next();

			Query query2 = em.createNamedQuery("LagerbewegungfindByIIdBuchung");
			query2.setParameter(1, l.getIIdBuchung());
			Collection cl2 = query2.getResultList();

			Iterator it2 = cl2.iterator();

			while (it2.hasNext()) {
				Lagerbewegung l2 = (Lagerbewegung) it2.next();
				l2.setCVersion(cVersion);
				em.merge(l2);
				em.flush();
			}

		}

	}

	// 14317
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal sofortverbrauchAllerArtikelAbbuchen(
			TheClientDto theClientDto) {

		int iStandarddurchlaufzeit = 0;
		MontageartDto montageartDto = null;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.INTERNEBESTELLUNG_DEFAULTDURCHLAUFZEIT);
			iStandarddurchlaufzeit = ((Integer) parameter.getCWertAsObject())
					.intValue();

			montageartDto = getStuecklisteFac().montageartFindByMandantCNr(
					theClientDto)[0];

		} catch (RemoteException ex2) {
			throwEJBExceptionLPRespectOld(ex2);
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria snrs = session
				.createCriteria(FLRArtikel.class)
				.add(Expression.isNotNull("i_sofortverbrauch"))
				.add(Expression.eq("b_lagerbewirtschaftet",
						Helper.boolean2Short(true)));

		snrs.addOrder(Order.asc("c_nr"));

		List<?> listSnr = snrs.list();

		Iterator<?> listSnrIterator = listSnr.iterator();

		while (listSnrIterator.hasNext()) {
			FLRArtikel artikel = (FLRArtikel) listSnrIterator.next();

			if (artikel.getI_id() == 2509) {
				int u = 0;
			}

			if (artikel.getI_sofortverbrauch() > 0) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)
						- artikel.getI_sofortverbrauch());

				Session session2 = FLRSessionFactory.getFactory().openSession();

				String sQuery = "SELECT l FROM FLRLagerbewegung AS l WHERE l.artikel_i_id="
						+ artikel.getI_id()
						+ " AND l.b_vollstaendigverbraucht=0 AND l.b_abgang=0 AND l.b_historie=0 AND l.n_menge>0 AND l.t_belegdatum<'"
						+ Helper.formatDateWithSlashes(new java.sql.Date(c
								.getTimeInMillis())) + "'";

				org.hibernate.Query hquery = session.createQuery(sQuery);
				List<?> resultList = hquery.list();
				Iterator<?> resultListIterator = resultList.iterator();

				while (resultListIterator.hasNext()) {
					FLRLagerbewegung lbew = (FLRLagerbewegung) resultListIterator
							.next();

					try {

						// Buche verbleibende Menge aufs Tageslos

						Integer losIId = getFertigungFac().holeTageslos(
								lbew.getLager_i_id(),
								artikel.getMandant_c_nr(),
								iStandarddurchlaufzeit, true, theClientDto);

						if (losIId != null) {

							LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
							lossollmaterialDto.setLosIId(losIId);
							lossollmaterialDto.setNSollpreis(lbew
									.getN_einstandspreis());
							lossollmaterialDto.setNMenge(new BigDecimal(0));
							lossollmaterialDto.setMontageartIId(montageartDto
									.getIId());
							lossollmaterialDto.setEinheitCNr(artikel
									.getEinheit_c_nr());
							lossollmaterialDto.setArtikelIId(artikel.getI_id());

							LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
							losistmaterialDto.setLagerIId(lbew.getLager_i_id());
							losistmaterialDto.setBAbgang(Helper
									.boolean2Short(true));

							Double bdVerbraucht = getLagerFac()
									.getVerbrauchteMenge(lbew.getI_id());

							losistmaterialDto
									.setNMenge(Helper
											.rundeKaufmaennisch(
													lbew.getN_menge()
															.subtract(
																	new BigDecimal(
																			bdVerbraucht
																					.doubleValue())),
													4));

							if (losistmaterialDto.getNMenge().doubleValue() > 0) {

								getFertigungFac()
										.gebeMaterialNachtraeglichAus(
												lossollmaterialDto,
												losistmaterialDto,
												SeriennrChargennrMitMengeDto
														.erstelleDtoAusEinerChargennummer(
																lbew.getC_seriennrchargennr(),
																losistmaterialDto
																		.getNMenge()),
												true, theClientDto);
							}
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

				session2.close();

			}

		}

		session.close();

		return null;

	}

	/**
	 * Wie bucheAb(...) nur dass auf die ArtikelIID ein Update erlaubt ist, wenn
	 * belegartpositionIId mitangegeben wird
	 * 
	 * @param belegartpositionIId_Alt
	 *            Integer
	 * @param belegartCNr
	 *            String
	 * @param belegartIId
	 *            Integer
	 * @param belegartpositionIId
	 *            Integer
	 * @param artikelIId
	 *            Integer
	 * @param fMengeAbsolut
	 *            BigDecimal
	 * @param nVerkaufspreis
	 *            BigDecimal
	 * @param lagerIId
	 *            Integer
	 * @param cSeriennrchargennr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void bucheAbArtikelUpdate(Integer belegartpositionIId_Alt,
			String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId, String cSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (belegartpositionIId_Alt != null) {
			Query query = em
					.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId_Alt);
			query.setParameter(3, null);
			Collection<?> cl = query.getResultList();
			// if (! cl.isEmpty()) {
			LagerbewegungDto[] lagerbewegungDtos = assembleLagerbewegungDtos(cl);
			for (int i = 0; i < lagerbewegungDtos.length; i++) {
				LagerbewegungDto dto = lagerbewegungDtos[i];
				bucheAb(belegartCNr, dto.getIBelegartid(),
						dto.getIBelegartpositionid(), dto.getArtikelIId(),
						new BigDecimal(0), dto.getNVerkaufspreis(),
						dto.getLagerIId(), cSeriennrchargennr, tBelegdatum,
						theClientDto);
			}
		}
		bucheAb(belegartCNr, belegartIId, belegartpositionIId, artikelIId,
				fMengeAbsolut, nVerkaufspreis, lagerIId, cSeriennrchargennr,
				tBelegdatum, theClientDto);

	}

	/**
	 * Bucht eine gewisse Anzahl von Artikel, wenn angegeben auch mit Serien/
	 * Chargennummer, aus einem Lager ab.
	 * 
	 * @param landIId
	 *            IId des Ursprungslands
	 * @param herstellerIId
	 *            IId des Herstellers
	 * @param belegartCNr
	 *            Lieferschein
	 * @param belegartpositionIId
	 *            5463
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */

	public void setzeHerstellerUrsprungsland(Integer landIId,
			Integer herstellerIId, String belegartCNr,
			Integer belegartpositionIId, String cSeriennrchargennr,
			TheClientDto theClientDto) {
		LagerbewegungDto[] lagerbewegungDto = lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(
				belegartCNr, belegartpositionIId, cSeriennrchargennr);

		for (int i = 0; i < lagerbewegungDto.length; i++) {
			Lagerbewegung lagerbewegung = em.find(Lagerbewegung.class,
					lagerbewegungDto[i].getIId());
			lagerbewegung.setLandIId(landIId);
			lagerbewegung.setHerstellerIId(herstellerIId);
			em.merge(lagerbewegung);
			em.flush();
		}

	}

	public boolean sindBereitsLagerbewegungenVorhanden(Integer artikelIId) {
		LagerbewegungDto[] dtos;
		try {
			dtos = getLagerFac().lagerbewegungFindByArtikelIId(artikelIId);
			if (dtos != null & dtos.length > 0) {
				return true;
			} else {
				return false;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void loescheKompletteLagerbewegungEinerBelgposition(
			String belegartCNr, Integer belegartpositionIId,
			TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		Collection cl = query.getResultList();

		Iterator it = cl.iterator();

		while (it.hasNext()) {
			Lagerbewegung l = (Lagerbewegung) it.next();

			if (Helper.short2boolean(l.getBAbgang())) {

				bucheAb(belegartCNr, l.getIBelegartid(), belegartpositionIId,
						l.getArtikelIId(), new BigDecimal(0),
						l.getNVerkaufspreis(), l.getLagerIId(),
						l.getCSeriennrchargennr(), l.getTBelegdatum(),
						theClientDto);
			} else {
				bucheZu(belegartCNr, l.getIBelegartid(), belegartpositionIId,
						l.getArtikelIId(), new BigDecimal(0),
						l.getNEinstandspreis(), l.getLagerIId(),
						l.getCSeriennrchargennr(), null, l.getTBelegdatum(),
						theClientDto, null, null, null, null, null, false);

			}
		}
		it = cl.iterator();
		if (it.hasNext()) {
			Lagerbewegung l = (Lagerbewegung) it.next();
			if (!Helper.short2boolean(l.getBAbgang())) {

				BigDecimal preis = recalcGestehungspreisAbTermin(l.getIId(),
						l.getArtikelIId(), l.getLagerIId(), l.getTBelegdatum(),
						theClientDto);

				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(l.getArtikelIId(), l.getLagerIId()));
				if (artikellager != null) {
					// und in Artikellager updaten
					artikellager.setNGestehungspreis(preis);
				}
			}
		}

	}

	public void bucheAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto) {
		Artikel artikel = em.find(Artikel.class, artikelIId);
		// Wenn Menge >0
		if (fMengeAbsolut.doubleValue() > 0) {
			if (alSeriennrchargennr == null || alSeriennrchargennr.size() == 0) {
				// Kein SeriennummernChargenbehafteter Artikel

				bucheAb(belegartCNr, belegartIId, belegartpositionIId,
						artikelIId, fMengeAbsolut, nVerkaufspreis, lagerIId,
						(String) null, tBelegdatum, theClientDto);
			} else {
				alSeriennrchargennr = verdichteSerienChargennummern(alSeriennrchargennr);
				pruefeLagerbewegungsMengeMitSnrChnrMenge(fMengeAbsolut,
						alSeriennrchargennr);
				List<SeriennrChargennrMitMengeDto> snrVorher = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
						belegartCNr, belegartpositionIId);
				if (snrVorher != null && snrVorher.size() > 0) {

					HashMap<String, String> hmZuLoeschendeSNR = new HashMap<String, String>();
					for (int i = 0; i < snrVorher.size(); i++) {
						hmZuLoeschendeSNR.put(snrVorher.get(i)
								.getCSeriennrChargennr(), snrVorher.get(i)
								.getCSeriennrChargennr());
					}

					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						hmZuLoeschendeSNR.remove(alSeriennrchargennr.get(i)
								.getCSeriennrChargennr());
					}

					// Alle nicht mehr eingegebenen loeschen
					for (Iterator<?> iter = hmZuLoeschendeSNR.keySet()
							.iterator(); iter.hasNext();) {
						String snr2Delete = (String) iter.next();
						bucheAb(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId,
								new BigDecimal(0), // Menge
								nVerkaufspreis, lagerIId, snr2Delete,
								tBelegdatum, theClientDto);
					}
					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						SeriennrChargennrMitMengeDto alZeile = alSeriennrchargennr
								.get(i);
						bucheAb(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId, alZeile.getNMenge(),
								nVerkaufspreis, lagerIId,
								alZeile.getCSeriennrChargennr(), tBelegdatum,
								theClientDto);
					}

				} else {

					for (int i = 0; i < alSeriennrchargennr.size(); i++) {
						SeriennrChargennrMitMengeDto alZeile = alSeriennrchargennr
								.get(i);
						bucheAb(belegartCNr, belegartIId, belegartpositionIId,
								artikelIId, alZeile.getNMenge(),
								nVerkaufspreis, lagerIId,
								alZeile.getCSeriennrChargennr(), tBelegdatum,
								theClientDto);

					}
				}
			}
		} else {
			// Alle vorhandenen Seriennummmern loeschen
			if (Helper.short2boolean(artikel.getBChargennrtragend())
					|| Helper.short2boolean(artikel.getBSeriennrtragend())) {
				List<SeriennrChargennrMitMengeDto> alSeriennrchargennrVorhanden = getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
						belegartCNr, belegartpositionIId);
				for (int i = 0; i < alSeriennrchargennrVorhanden.size(); i++) {
					bucheAb(belegartCNr, belegartIId, belegartpositionIId,
							artikelIId, new BigDecimal(0), nVerkaufspreis,
							lagerIId, alSeriennrchargennrVorhanden.get(i)
									.getCSeriennrChargennr(), tBelegdatum,
							theClientDto);
				}
			} else {
				bucheAb(belegartCNr, belegartIId, belegartpositionIId,
						artikelIId, new BigDecimal(0), nVerkaufspreis,
						lagerIId, (String) null, tBelegdatum, theClientDto);
			}

		}

	}

	public void bucheAb(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, Integer artikelIId,
			BigDecimal fMengeAbsolut, BigDecimal nVerkaufspreis,
			Integer lagerIId, String cSeriennrchargennr,
			java.sql.Timestamp tBelegdatum, TheClientDto theClientDto) {
		// check(idUser);

		if (belegartCNr == null || belegartpositionIId == null
				|| artikelIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null || artikelIId == null"));
		}
		if (fMengeAbsolut == null || nVerkaufspreis == null || lagerIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"fMengeAbsolut == null || nVerkaufspreis == null || lagerIId == null"));
		}
		if (tBelegdatum == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("tBelegdatum == null"));
		}
		if (fMengeAbsolut.doubleValue() < 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
					new Exception("fMengeAbsolut.doubleValue() < 0"));
		}
		// PJ 14476 Zumindest mal auf 5 Stellen runden
		fMengeAbsolut = Helper.rundeKaufmaennisch(fMengeAbsolut, 5);

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (artikel.getBChargennrtragend().intValue() == 1
				|| artikel.getBSeriennrtragend().intValue() == 1) {
			if (cSeriennrchargennr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("cSeriennrchargennr == null ArtikelNr:"
								+ artikel.getCNr()));
			}
		}
		if (artikel.getBChargennrtragend().intValue() != 1
				&& artikel.getBSeriennrtragend().intValue() != 1) {
			if (cSeriennrchargennr != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER,
						new Exception(
								"SERIENNUMMER_CHARGENNUMMER_DARF_NICHT_ANGEGEBEN_WERDEN"));
			}
		}

		if (artikel.getBSeriennrtragend().intValue() == 1
				&& fMengeAbsolut.doubleValue() != 1
				&& fMengeAbsolut.doubleValue() != 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
					new Exception(
							"artikel.getBSeriennrtragend().intValue() == 1 && fMengeAbsolut.doubleValue()!=1"));
		}

		// es wird davon ausgegangen, dass ein Artikel nicht lagerbewirtschaftet
		// ist
		boolean bLagerbewirtschaftet = false;

		// Nachsehen, ob Artikel lagerbewirtschaftet ist
		if (Helper.short2boolean(artikel.getBLagerbewirtschaftet()) == true) {
			bLagerbewirtschaftet = true;
		}

		// Wenns ein Verleihartikel ist und in die Rechnung eingefuegt wird,
		// dann
		// ist er wie ein 'nicht lagerbewirtschafteter'
		if (Helper.short2boolean(artikel.getBVerleih()) == true
				&& belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
			bLagerbewirtschaftet = false;
		}

		// Wenn Artikel nicht Lagerbewirtschaftet ist, dann wirds automatisch
		// auf "KEIN LAGER" gebucht sonst
		// DARF NICHT auf KEIN_LAGER gebucht werden
		if (bLagerbewirtschaftet == false) {
			LagerDto tempLager = lagerFindByCNrByMandantCNr(
					LagerFac.LAGER_KEINLAGER, theClientDto.getMandant());
			if (tempLager != null) {
				lagerIId = tempLager.getIId();
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_KEIN_LAGER_NICHT_ANGELEGT,
						new Exception("LAGER_KEIN_LAGER_NICHT_ANGELEGT"));
			}
		} else {
			if (lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER,
					theClientDto.getMandant()).getIId().equals(lagerIId)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN,
						new Exception("FEHLER_DARF_NICHT_AUF_KEIN_LAGER_BUCHEN"));

			}

			if (!hatRolleBerechtigungAufLager(lagerIId, theClientDto)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER,
						new Exception(
								"FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER"));
			}
		}

		// Pruefen, ob Lager bei dem Mandanten erlaubt ist
		LagerDto lagerDto_Erlaubt = lagerFindByPrimaryKey(lagerIId);
		if (!lagerDto_Erlaubt.getMandantCNr().equals(theClientDto.getMandant())) {

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)
					|| (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
							theClientDto) && getMandantFac()
							.darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
									theClientDto))) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FALSCHER_MANDANT, new Exception(
								"FEHLER_FALSCHER_MANDANT"));
			}

		}

		if (bLagerLogging == null) {
			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_LAGER_LOGGING);
				bLagerLogging = Helper.short2boolean(new Short(parameter
						.getCWert()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		if (bLagerLogging) {

			String logEintrag = "VOR ABBUCHUNG:belegartCNr=" + belegartCNr
					+ ",belegartIId=" + belegartIId + ",belegartpositionIId="
					+ belegartpositionIId + ",artikelIId=" + artikelIId
					+ ",fMengeAbsolut=" + fMengeAbsolut + ",nEinstandspreis="
					+ nVerkaufspreis + ",lagerIId=" + lagerIId
					+ ",cSeriennrchargennr=" + cSeriennrchargennr
					+ ",tBelegdatum=" + tBelegdatum + ",theClientDto="
					+ theClientDto.toString();

			// Lagerstand + Gestpreis hier abrufen

			double lagerstandVorher = 0;
			double gestpreis = 0;

			try {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager != null) {
					lagerstandVorher = artikellager.getNLagerstand()
							.doubleValue();
					gestpreis = artikellager.getNGestehungspreis()
							.doubleValue();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logEintrag += ";Lagerstand=" + lagerstandVorher + ",Gestpreis="
					+ gestpreis;

			erstelleProtokollEintragAbbuchung(theClientDto, logEintrag, true);
		}

		LagerbewegungDto lagerbewegungDto = getLetzteintrag(belegartCNr,
				belegartpositionIId, cSeriennrchargennr);
		if (lagerbewegungDto != null) {
			// es ist eine Aenderungsbuchung
			lagerbewegungDto.setTBelegdatum(tBelegdatum);
			if (!artikelIId.equals(lagerbewegungDto.getArtikelIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT,
						new Exception(
								"LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT artikelIId_Neu="
										+ artikelIId));

			}

			if (cSeriennrchargennr != null) {
				if (!cSeriennrchargennr.equals(lagerbewegungDto
						.getCSeriennrchargennr())) {
					throw new EJBExceptionLP(
							EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT,
							new Exception(
									"LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT"));
				}
			}
			if (!lagerIId.equals(lagerbewegungDto.getLagerIId())
					&& Helper.short2boolean(artikel.getBLagerbewirtschaftet()) == true) {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_UPDATE_AUF_LAGER_NICHT_ERLAUBT,
						new Exception("LAGER_UPDATE_AUF_LAGER_NICHT_ERLAUBT"));

			}

			if (lagerbewegungDto.getNMenge().doubleValue() == fMengeAbsolut
					.doubleValue()) {
				// Es ist nur eine Preisaenderung
				lagerbewegungDto.setNVerkaufspreis(nVerkaufspreis);
				updateLagerbewegung(lagerbewegungDto, theClientDto);
				myLogger.logKritisch("Preisaenderung von User mit ID "
						+ theClientDto.getIDPersonal()
						+ "Eingetragener Preis: "
						+ lagerbewegungDto.getNVerkaufspreis());
			} else {
				// Mengenaenderung und ev. Preisaenderung

				// Wenn Menge erhoeht wird, dann pruefen, ob dies moeglich ist
				if (fMengeAbsolut.doubleValue() > lagerbewegungDto.getNMenge()
						.doubleValue()) {
					BigDecimal m = getMengeAufLager(artikelIId, lagerIId,
							cSeriennrchargennr, theClientDto);

					m = Helper.rundeKaufmaennisch(m, 4);

					if (fMengeAbsolut.doubleValue() > m.doubleValue()
							+ lagerbewegungDto.getNMenge().doubleValue()
							&& bLagerbewirtschaftet == true) {

						double zuwenig = fMengeAbsolut.doubleValue()
								- lagerbewegungDto.getNMenge().doubleValue()
								- m.doubleValue();

						// PJ18290
						try {
							ParametermandantDto parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ARTIKEL,
											ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
							boolean bLagerImmerAusreichendVerfuegbar = Helper
									.short2boolean(new Short(parameter
											.getCWert()));

							if (bLagerImmerAusreichendVerfuegbar) {

								HandlagerbewegungDto handlagerbewegungDto = new HandlagerbewegungDto();
								handlagerbewegungDto.setArtikelIId(artikelIId);
								handlagerbewegungDto

								.setLagerIId(lagerIId);
								handlagerbewegungDto.setBAbgang(new Short(
										(short) 0));
								handlagerbewegungDto
										.setCKommentar("Zubuchungsautomatik");

								handlagerbewegungDto
										.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
												.erstelleDtoAusEinerChargennummer(
														cSeriennrchargennr,
														Helper.rundeKaufmaennisch(
																new BigDecimal(
																		zuwenig),
																4)));
								handlagerbewegungDto.setNMenge(Helper
										.rundeKaufmaennisch(new BigDecimal(
												zuwenig), 4));
								// SP3254
								// Zuerst nachsehen, obs einen Gestpreis gibt
								BigDecimal gestpreis = getGestehungspreisZumZeitpunkt(
										artikelIId, lagerIId, tBelegdatum,
										theClientDto);
								// SP3410 Wenn keinen zum Zeitpunkt gibt, dann
								// den aktuellen aus Artikellager verwenden
								if (gestpreis == null
										|| gestpreis.doubleValue() == 0) {

									ArtikellagerPK artikellagerPK = new ArtikellagerPK();
									artikellagerPK.setArtikelIId(artikelIId);
									artikellagerPK.setLagerIId(lagerIId);
									Artikellager artikellager = em.find(
											Artikellager.class, artikellagerPK);
									if (artikellager != null) {
										gestpreis = artikellager
												.getNGestehungspreis();
									}

								}

								if (gestpreis == null
										|| gestpreis.doubleValue() == 0) {
									// Wenn Gestpreis null oder 0, dann
									// Lief1Preis verwenden:
									ArtikellieferantDto alDto = getArtikelFac()
											.getArtikelEinkaufspreis(
													artikelIId,
													fMengeAbsolut,
													theClientDto
															.getSMandantenwaehrung(),
													theClientDto);

									if (alDto == null
											|| alDto.getNNettopreis() == null) {
										// wenn auch kein Lief1Preis, dann
										// VKPreis verwenden
										handlagerbewegungDto
												.setNEinstandspreis(nVerkaufspreis);
									} else {
										handlagerbewegungDto
												.setNEinstandspreis(alDto
														.getNNettopreis());
									}

								} else {
									handlagerbewegungDto
											.setNEinstandspreis(gestpreis);
								}

								createHandlagerbewegung(handlagerbewegungDto,
										theClientDto);

							} else {

								ArrayList al = new ArrayList();
								al.add(artikel.getCNr());

								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER,
										al, new Exception(
												"FEHLER_ZUWENIG_AUF_LAGER snrchnr="
														+ cSeriennrchargennr
														+ " menge_abzubuchen="
														+ fMengeAbsolut
																.doubleValue()
														+ ", artikelIId="
														+ artikelIId));
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					}
				}

				// original Buchungszeit merken
				Timestamp tBuchungszeitOrig = lagerbewegungDto
						.getTBuchungszeit();
				// alter Eintrag wird Historie mit Aenderungszeit
				lagerbewegungDto.setTBuchungszeit(new Timestamp(System
						.currentTimeMillis()));
				lagerbewegungDto.setBHistorie(new Short((short) 1));
				updateLagerbewegung(lagerbewegungDto, theClientDto);

				LagerbewegungDto lagerbewegungDtoKorrektur = new LagerbewegungDto();
				lagerbewegungDtoKorrektur.setBHistorie(new Short((short) 0));
				lagerbewegungDtoKorrektur.setTBuchungszeit(tBuchungszeitOrig);
				lagerbewegungDtoKorrektur.setArtikelIId(artikelIId);
				lagerbewegungDtoKorrektur.setTBelegdatum(tBelegdatum);
				lagerbewegungDtoKorrektur.setBAbgang(new Short((short) 1));
				if (bLagerbewirtschaftet == true) {
					lagerbewegungDtoKorrektur
							.setBVollstaendigverbraucht(new Short((short) 0));
				} else {
					lagerbewegungDtoKorrektur
							.setBVollstaendigverbraucht(new Short((short) 1));
				}
				lagerbewegungDtoKorrektur.setCBelegartnr(belegartCNr);
				lagerbewegungDtoKorrektur.setIBelegartid(belegartIId);
				lagerbewegungDtoKorrektur
						.setIBelegartpositionid(belegartpositionIId);
				lagerbewegungDtoKorrektur
						.setCSeriennrchargennr(cSeriennrchargennr);
				lagerbewegungDtoKorrektur.setNMenge(fMengeAbsolut);
				lagerbewegungDtoKorrektur.setLagerIId(lagerIId);
				lagerbewegungDtoKorrektur.setNGestehungspreis(lagerbewegungDto
						.getNGestehungspreis());
				lagerbewegungDtoKorrektur.setIIdBuchung(lagerbewegungDto
						.getIIdBuchung());
				lagerbewegungDtoKorrektur.setNVerkaufspreis(Helper
						.rundeKaufmaennisch(nVerkaufspreis, 4));
				createLagerbewegung(lagerbewegungDtoKorrektur, theClientDto);

				BigDecimal fMengeWeniger = lagerbewegungDto.getNMenge()
						.subtract(fMengeAbsolut);
				// Lagerstand in WW_ARTIKELLAGER updaten
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET,
							"");
				}
				BigDecimal lagerstand = artikellager.getNLagerstand();
				lagerstand = lagerstand.add(fMengeWeniger);

				if (bLagerbewirtschaftet == true) {
					artikellager.setNLagerstand(Helper.rundeKaufmaennisch(
							lagerstand, 4));
				} else {
					artikellager.setNLagerstand(new BigDecimal(0));
				}

				// Wenn die Menge erhoeht wird, dann muss zusaetzlich vom
				// Lager abgebucht werden
				if (fMengeWeniger.doubleValue() < 0) {
					BigDecimal bdZusaetzlichZuVerbrauchen = fMengeWeniger.abs();
					LagerabgangursprungDto[] urspruenge = getVonLagerNachRegel(
							artikelIId, lagerIId, cSeriennrchargennr,
							bdZusaetzlichZuVerbrauchen, theClientDto);
					if (bLagerbewirtschaftet == true) {
						BigDecimal fUrspruenge = new BigDecimal(0);

						for (int j = 0; j < urspruenge.length; j++) {
							fUrspruenge = fUrspruenge.add(urspruenge[j]
									.getNVerbrauchtemenge());
						}

						if (cSeriennrchargennr != null && urspruenge.length > 0) {
							versionInLagerbewegungUpdaten(belegartCNr,
									belegartpositionIId, cSeriennrchargennr,
									urspruenge[0].getVersionAusUrsprung());
						}

						if (bdZusaetzlichZuVerbrauchen.compareTo(fUrspruenge) == 1
								|| bdZusaetzlichZuVerbrauchen
										.compareTo(fUrspruenge) == -1) {
							myLogger.logKritisch("FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH, ARTIKEL_I_ID="
									+ artikelIId
									+ " ABZUBUCHENDEMENGE="
									+ fMengeAbsolut.doubleValue()
									+ " SNR_CHNR: "
									+ cSeriennrchargennr
									+ " MENGE_AUS_URSPRUENGEN=" + fUrspruenge);
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH,
									new Exception(
											"FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH"));
						}

					}
					for (int j = 0; j < urspruenge.length; j++) {
						urspruenge[j]
								.setILagerbewegungid(lagerbewegungDtoKorrektur
										.getIIdBuchung());

						// Zuerst nachsehen, ob es diesen Urspung scon gibt,
						// dann Updaten
						LagerabgangursprungPK lagerabgangursprungPK = new LagerabgangursprungPK();
						lagerabgangursprungPK
								.setILagerbewegungid(lagerbewegungDtoKorrektur
										.getIIdBuchung());
						lagerabgangursprungPK
								.setILagerbewegungidursprung(urspruenge[j]
										.getILagerbewegungidursprung());
						Lagerabgangursprung lagerabgangursprung = em.find(
								Lagerabgangursprung.class,
								lagerabgangursprungPK);
						if (lagerabgangursprung == null) {
							createLagerabgangursprung(urspruenge[j]);
						} else {
							lagerabgangursprung
									.setNVerbrauchtemenge(lagerabgangursprung
											.getNVerbrauchtemenge()
											.add(urspruenge[j]
													.getNVerbrauchtemenge()));
							em.merge(lagerabgangursprung);
							em.flush();
						}
					}

				} else {
					// Verbrauchte Mengen in WW_LAGERABGANGURSPRUNG updaten
					LagerabgangursprungDto[] lagerabgangurspruenge = lagerabgangursprungFindByLagerbewegungIIdBuchung(lagerbewegungDto
							.getIIdBuchung());
					BigDecimal fMengezuVerringern = fMengeWeniger;

					for (int i = 0; i < lagerabgangurspruenge.length; i++) {
						BigDecimal verbrauchteMenge = lagerabgangurspruenge[i]
								.getNVerbrauchtemenge();
						if (verbrauchteMenge.doubleValue() > 0) {
							// Wenn die Menge verringert wurde,
							// bVollstaendigVerbraucht des Ursprungs
							// zuruecksetzen
							if (fMengeWeniger.doubleValue() > 0) {
								LagerbewegungDto[] buchungen = lagerbewegungFindByIIdBuchung(lagerabgangurspruenge[i]
										.getILagerbewegungidursprung());

								for (int u = 0; u < buchungen.length; u++) {
									buchungen[u]
											.setBVollstaendigverbraucht(new Short(
													(short) 0));
									updateLagerbewegung(buchungen[u],
											theClientDto);

								}
								fMengeWeniger = fMengeWeniger
										.subtract(lagerabgangurspruenge[i]
												.getNVerbrauchtemenge());
							}

							// Wenn verringert wird, dann muessen die
							// Ursprungen
							// ebenfalls verringert werden
							if (fMengezuVerringern.subtract(verbrauchteMenge)
									.doubleValue() > 0) {
								fMengezuVerringern = fMengezuVerringern
										.subtract(lagerabgangurspruenge[i]
												.getNVerbrauchtemenge());
								lagerabgangurspruenge[i]
										.setNVerbrauchtemenge(new BigDecimal(0));
								updateLagerabgangursprung(lagerabgangurspruenge[i]);
							} else {
								lagerabgangurspruenge[i]
										.setNVerbrauchtemenge(verbrauchteMenge
												.subtract(fMengezuVerringern));
								updateLagerabgangursprung(lagerabgangurspruenge[i]);
							}
						}
					}
				}

			}
		} else {
			if (fMengeAbsolut.doubleValue() == 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
						new Exception("fMengeAbsolut.doubleValue() == 0"));
			}

			BigDecimal m = getMengeAufLager(artikelIId, lagerIId,
					cSeriennrchargennr, theClientDto);

			m = Helper.rundeKaufmaennisch(m, 4);

			if (fMengeAbsolut.doubleValue() > m.doubleValue()
					&& bLagerbewirtschaftet == true) {

				double zuwenig = fMengeAbsolut.doubleValue() - m.doubleValue();

				// PJ18290
				try {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR);
					boolean bLagerImmerAusreichendVerfuegbar = Helper
							.short2boolean(new Short(parameter.getCWert()));

					if (bLagerImmerAusreichendVerfuegbar) {

						HandlagerbewegungDto handlagerbewegungDto = new HandlagerbewegungDto();
						handlagerbewegungDto.setArtikelIId(artikelIId);
						handlagerbewegungDto

						.setLagerIId(lagerIId);
						handlagerbewegungDto.setBAbgang(new Short((short) 0));
						handlagerbewegungDto
								.setCKommentar("Zubuchungsautomatik");

						handlagerbewegungDto
								.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
										cSeriennrchargennr,
										Helper.rundeKaufmaennisch(
												new BigDecimal(zuwenig), 4)));
						handlagerbewegungDto
								.setNMenge(Helper.rundeKaufmaennisch(
										new BigDecimal(zuwenig), 4));

						// 3254
						// Zuerst nachsehen, obs einen Gestpreis gibt
						BigDecimal gestpreis = getGestehungspreisZumZeitpunkt(
								artikelIId, lagerIId, tBelegdatum, theClientDto);

						// SP3410 Wenn keinen zum Zeitpunkt gibt, dann
						// den aktuellen aus Artikellager verwenden
						if (gestpreis == null || gestpreis.doubleValue() == 0) {

							ArtikellagerPK artikellagerPK = new ArtikellagerPK();
							artikellagerPK.setArtikelIId(artikelIId);
							artikellagerPK.setLagerIId(lagerIId);
							Artikellager artikellager = em.find(
									Artikellager.class, artikellagerPK);
							if (artikellager != null) {
								gestpreis = artikellager.getNGestehungspreis();
							}

						}

						if (gestpreis == null || gestpreis.doubleValue() == 0) {
							// Wenn Gestpreis null oder 0, dann
							// Lief1Preis verwenden:
							ArtikellieferantDto alDto = getArtikelFac()
									.getArtikelEinkaufspreis(
											artikelIId,
											fMengeAbsolut,
											theClientDto
													.getSMandantenwaehrung(),
											theClientDto);

							if (alDto == null || alDto.getNNettopreis() == null) {
								// wenn auch kein Lief1Preis, dann
								// VKPreis verwenden
								handlagerbewegungDto
										.setNEinstandspreis(nVerkaufspreis);
							} else {
								handlagerbewegungDto.setNEinstandspreis(alDto
										.getNNettopreis());
							}

						} else {
							handlagerbewegungDto.setNEinstandspreis(gestpreis);
						}

						createHandlagerbewegung(handlagerbewegungDto,
								theClientDto);
					} else {

						ArrayList al = new ArrayList();
						al.add(artikel.getCNr());

						if (cSeriennrchargennr != null) {
							al.add(cSeriennrchargennr);
						}

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER, al,
								new Exception(
										"FEHLER_ZUWENIG_AUF_LAGER snrchnr="
												+ cSeriennrchargennr
												+ " menge_abzubuchen="
												+ fMengeAbsolut.doubleValue()
												+ ", artikelIId=" + artikelIId));
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			LagerbewegungDto lagerbewegungDtoNeueintrag = new LagerbewegungDto();
			lagerbewegungDtoNeueintrag.setArtikelIId(artikelIId);
			lagerbewegungDtoNeueintrag.setBAbgang(new Short((short) 1));
			lagerbewegungDtoNeueintrag.setTBelegdatum(tBelegdatum);
			if (bLagerbewirtschaftet == true) {
				lagerbewegungDtoNeueintrag
						.setBVollstaendigverbraucht(new Short((short) 0));
			} else {
				lagerbewegungDtoNeueintrag
						.setBVollstaendigverbraucht(new Short((short) 1));
			}

			lagerbewegungDtoNeueintrag.setCBelegartnr(belegartCNr);
			lagerbewegungDtoNeueintrag.setIBelegartid(belegartIId);
			lagerbewegungDtoNeueintrag
					.setIBelegartpositionid(belegartpositionIId);
			lagerbewegungDtoNeueintrag
					.setCSeriennrchargennr(cSeriennrchargennr);
			lagerbewegungDtoNeueintrag.setNMenge(fMengeAbsolut);
			lagerbewegungDtoNeueintrag.setLagerIId(lagerIId);
			lagerbewegungDtoNeueintrag.setNVerkaufspreis(Helper
					.rundeKaufmaennisch(nVerkaufspreis, 4));
			lagerbewegungDtoNeueintrag.setTBuchungszeit(new Timestamp(System
					.currentTimeMillis()));
			lagerbewegungDtoNeueintrag.setBHistorie(new Short((short) 0));
			// Neue Nuchungsnummer erzeugen
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen
					.getNextPrimaryKey(PKConst.PK_LAGERBEWEGUNG_I_ID_BUCHUNG);
			lagerbewegungDtoNeueintrag.setIIdBuchung(pk);

			// wenn nicht lagerbewirtschaftet artikel uaf Lager KEIN_LAGER
			// in artikelager anlegen
			if (bLagerbewirtschaftet == false) {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager == null) {
					try {
						artikellager = new Artikellager(artikelIId, lagerIId,
								theClientDto.getMandant());
						em.persist(artikellager);
						em.flush();
					} catch (EntityExistsException ex) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
					}
				}
			}

			// Lagerstand in WW_ARTIKELLAGER updaten und Gestehungspreis zum
			// Abbuchungszeitpunkt niederschreiben niederschreiben
			Artikellager artikellager = em.find(Artikellager.class,
					new ArtikellagerPK(artikelIId, lagerIId));
			if (artikellager == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET,
						new Exception("ARTIKEL_IST_KEINEM_LAGER_ZUGEORDNET"));
			}
			BigDecimal lagerstand = artikellager.getNLagerstand();
			lagerstand = lagerstand.subtract(fMengeAbsolut);

			if (bLagerbewirtschaftet == true) {
				artikellager.setNLagerstand(Helper.rundeKaufmaennisch(
						lagerstand, 4));
			} else {
				artikellager.setNLagerstand(new BigDecimal(0));
			}
			// TODO: sollte hier nicht der Gestehungspreis zum Zeitpunkt
			// verwendet werden?
			lagerbewegungDtoNeueintrag.setNGestehungspreis(artikellager
					.getNGestehungspreis());
			createLagerbewegung(lagerbewegungDtoNeueintrag, theClientDto);

			// Urspruenge eintragen
			LagerabgangursprungDto[] urspruenge = getVonLagerNachRegel(
					artikelIId, lagerIId, cSeriennrchargennr, fMengeAbsolut,
					theClientDto);

			if (bLagerbewirtschaftet == true) {
				BigDecimal fUrspruenge = new BigDecimal(0);

				if (cSeriennrchargennr != null && urspruenge.length > 0) {
					versionInLagerbewegungUpdaten(belegartCNr,
							belegartpositionIId, cSeriennrchargennr,
							urspruenge[0].getVersionAusUrsprung());
				}

				for (int i = 0; i < urspruenge.length; i++) {
					fUrspruenge = fUrspruenge.add(urspruenge[i]
							.getNVerbrauchtemenge());

				}

				if (fMengeAbsolut.compareTo(fUrspruenge) == 1
						|| fMengeAbsolut.compareTo(fUrspruenge) == -1) {
					myLogger.logKritisch("FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH, ARTIKEL_I_ID="
							+ artikelIId
							+ " SNR_CHNR: "
							+ cSeriennrchargennr
							+ " ABZUBUCHENDEMENGE="
							+ fMengeAbsolut.doubleValue()
							+ " MENGE_AUS_URSPRUENGEN=" + fUrspruenge);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH,
							new Exception(
									"FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH"
											+ "FEHLER_ABBUCHUNG_OHNE_URSPRUNGSEINTRAG_NICHT_MOEGLICH, ARTIKEL_I_ID="
											+ artikelIId + " SNR_CHNR: "
											+ cSeriennrchargennr
											+ " ABZUBUCHENDEMENGE="
											+ fMengeAbsolut.doubleValue()
											+ " MENGE_AUS_URSPRUENGEN="
											+ fUrspruenge));
				}

			}
			for (int i = 0; i < urspruenge.length; i++) {
				urspruenge[i].setILagerbewegungid(pk);
				createLagerabgangursprung(urspruenge[i]);
			}

		}
		if (bLagerLogging) {
			double lagerstandNachher = 0;
			double gestpreisNachher = 0;

			try {
				Artikellager artikellager = em.find(Artikellager.class,
						new ArtikellagerPK(artikelIId, lagerIId));
				if (artikellager != null) {
					lagerstandNachher = artikellager.getNLagerstand()
							.doubleValue();
					gestpreisNachher = artikellager.getNGestehungspreis()
							.doubleValue();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String logEintrag = "LagerstandNachBuchung=" + lagerstandNachher
					+ ",GestpreisNachBuchung=" + gestpreisNachher;
			erstelleProtokollEintragAbbuchung(theClientDto, logEintrag, false);
		}
	}

	private void erstelleProtokollEintragAbbuchung(TheClientDto theClientDto,
			String logEintrag, boolean bStacktrace) {
		ProtokollDto protokollDto = new ProtokollDto();

		protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_LAGER_AB);
		protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);

		protokollDto.setCText(logEintrag);
		if (bStacktrace) {
			protokollDto.setCLangtext(getLPStackTrace(new Throwable()
					.getStackTrace()));
		}

		erstelleProtokollEintrag(protokollDto, theClientDto);
	}

	/**
	 * Ein Lager ueber seine Lagerart bestimmen.
	 * 
	 * @param sMandantI
	 *            Mandtant
	 * @param sLagerartI
	 *            Lagerart
	 * @throws EJBExceptionLP
	 * @return LagerDto
	 */
	public LagerDto lagerFindByMandantCNrLagerartCNr(String sMandantI,
			String sLagerartI) throws EJBExceptionLP {
		myLogger.entry();

		if (sLagerartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception());
		}
		try {
			Query query = em
					.createNamedQuery("LagerfindByMandantCNrLagerartCNr");
			query.setParameter(1, sMandantI);
			query.setParameter(2, sLagerartI);
			Lager lager = (Lager) query.getSingleResult();
			return assembleLagerDto(lager);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	/**
	 * Alle Lager eines Mandanten holen (ohne Kein_Lager)
	 * 
	 * @param sMandantI
	 *            Mandtant
	 * @throws EJBExceptionLP
	 * @return LagerDto
	 */
	public LagerDto[] lagerFindByMandantCNr(String sMandantI)
			throws EJBExceptionLP {
		if (sMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("sMandantI == null"));
		}
		Query query = em.createNamedQuery("LagerfindByMandantCNr");
		query.setParameter(1, sMandantI);
		Collection<?> cl = query.getResultList();
		return assembleLagerDtos(cl);
	}

	public LagerDto[] lagerFindAll() {
		Query query = em.createNamedQuery("LagerfindAll");
		Collection<?> cl = query.getResultList();
		return assembleLagerDtos(cl);
	}

	public String getNaechsteSeriennummer(Integer artikelIId,
			String uebersteuerteSeriennummer, TheClientDto theClientDto) {

		String neueSnrChnr = null;
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLagerbewegung l WHERE l.flrartikel.b_seriennrtragend = 1 AND l.n_menge > 0 AND l.b_historie=0 AND l.b_abgang=0 AND l.c_seriennrchargennr IS NOT NULL";
		int iMaxLaengeSnr = 15;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG);
			boolean bSnrEindeutig = Helper.short2boolean(new Short(parameter
					.getCWert()));

			if (bSnrEindeutig == false) {
				queryString += " AND l.artikel_i_id=" + artikelIId + " ";

			}

			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER);
			iMaxLaengeSnr = (Integer) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		queryString += " ORDER BY l.c_seriennrchargennr DESC";

		org.hibernate.Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		String artikelnummerLetzteVersion = null;

		if (resultListIterator.hasNext() || uebersteuerteSeriennummer != null) {

			String letzteSeriennummer = null;
			if (uebersteuerteSeriennummer != null) {
				letzteSeriennummer = uebersteuerteSeriennummer;
			} else {

				FLRLagerbewegung flrLagerbewegung = (FLRLagerbewegung) resultListIterator
						.next();

				letzteSeriennummer = flrLagerbewegung.getC_seriennrchargennr();
			}

			try {
				Long snr = new Long(letzteSeriennummer);
				snr = snr + 1;
				return Helper.fitString2LengthAlignRight(snr + "",
						iMaxLaengeSnr, '0');

			} catch (NumberFormatException e) {
				ArrayList al = new ArrayList();
				al.add(letzteSeriennummer);
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN,
						al, new Exception(letzteSeriennummer));

			}

		} else {
			return Helper.fitString2LengthAlignRight("1", iMaxLaengeSnr, '0');
		}

	}

	public String getNaechsteChargennummer(Integer artikelIId,
			TheClientDto theClientDto) {

		String neueSnrChnr = null;
		Session session = FLRSessionFactory.getFactory().openSession();

		int iLaengeEinEindeutig = 0;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_LAENGE_CHARGENNUMMER_EINEINDEUTIG);
			iLaengeEinEindeutig = (Integer) parameter.getCWertAsObject();

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (iLaengeEinEindeutig <= 0) {
			return null;
		}

		String queryString = "SELECT L.C_SERIENNRCHARGENNR FROM STK_STUECKLISTE STKL "
				+ "LEFT OUTER JOIN WW_LAGERBEWEGUNG L ON STKL.ARTIKEL_I_ID=L.ARTIKEL_I_ID "
				+ "LEFT OUTER JOIN WW_ARTIKEL A ON STKL.ARTIKEL_I_ID= A.I_ID "
				+ "WHERE STKL.B_FREMDFERTIGUNG=0 AND A.B_CHARGENNRTRAGEND=1 AND L.N_MENGE>0 AND L.B_HISTORIE=0 AND L.B_ABGANG=0 AND L.C_SERIENNRCHARGENNR IS NOT NULL AND L.C_SERIENNRCHARGENNR NOT IN ('KEINE_CHARGE') ORDER BY L.C_SERIENNRCHARGENNR DESC";

		org.hibernate.Query query = session.createSQLQuery(queryString);
		query.setMaxResults(1);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		String artikelnummerLetzteVersion = null;

		if (resultListIterator.hasNext()) {

			String letzteSeriennummer = (String) resultListIterator.next();

			try {
				Long snr = new Long(letzteSeriennummer);
				snr = snr + 1;
				return Helper.fitString2LengthAlignRight(snr + "",
						iLaengeEinEindeutig, '0');

			} catch (NumberFormatException e) {
				ArrayList al = new ArrayList();
				al.add(letzteSeriennummer);
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_CHARGENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN,
						al, new Exception(letzteSeriennummer));

			}

		} else {
			return Helper.fitString2LengthAlignRight("1", iLaengeEinEindeutig,
					'0');
		}

	}

	public LagerDto[] lagerFindByMandantCNrOrderByILoslagersort(String sMandantI)
			throws EJBExceptionLP {
		if (sMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("sMandantI == null"));
		}
		Query query = em
				.createNamedQuery("LagerfindByMandantCNrOrderByILoslagersort");
		query.setParameter(1, sMandantI);
		Collection<?> cl = query.getResultList();
		return assembleLagerDtos(cl);
	}

	public LagerDto lagerFindByMandantCNrLagerartCNrOhneExc(String sMandantI,
			String sLagerartI) {
		myLogger.entry();

		LagerDto oLagerDtoO = null;

		try {
			oLagerDtoO = lagerFindByMandantCNrLagerartCNr(sMandantI, sLagerartI);
		} catch (Throwable t) {
			myLogger.error("lagerFindByMandantCNrLagerartCNrOhneExc", t);
		}

		return oLagerDtoO;
	}

	public LagerDto lagerFindByPrimaryKeyOhneExc(Integer lagerIId) {
		LagerDto lagerDto = null;

		Lager lager = em.find(Lager.class, lagerIId);
		if (lager != null) {
			lagerDto = assembleLagerDto(lager);
		}

		return lagerDto;
	}

	public void createLagerart(LagerartDto lagerartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lagerartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerartDto == null"));
		}
		if (lagerartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lagerartDto.getCNr() == null"));
		}
		try {
			Lagerart partnerart = new Lagerart(lagerartDto.getCNr());
			em.persist(lagerart);
			em.flush();
			setLagerartFromLagerartDto(partnerart, lagerartDto);
			if (lagerartDto.getLagerartsprDto() != null) {
				Lagerartspr artikelartspr = new Lagerartspr(
						lagerartDto.getCNr(),
						theClientDto.getLocMandantAsString());
				em.persist(lagerartspr);
				em.flush();
				setLagerartsprFromLagerartsprDto(artikelartspr,
						lagerartDto.getLagerartsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeLagerart(String cNr) throws EJBExceptionLP {
		Lagerart toRemove = em.find(Lagerart.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeLagerart(LagerartDto lagerartDto) throws EJBExceptionLP {
		if (lagerartDto != null) {
			String cNr = lagerartDto.getCNr();
			removeLagerart(cNr);
		}
	}

	public void updateLagerart(LagerartDto lagerartDto) throws EJBExceptionLP {
		if (lagerartDto != null) {
			String cNr = lagerartDto.getCNr();
			Lagerart lagerart = em.find(Lagerart.class, cNr);
			if (lagerart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLagerartFromLagerartDto(lagerart, lagerartDto);
		}
	}

	public Integer createHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (handlagerbewegungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("handlagerbewegungDto == null"));
		}
		if (handlagerbewegungDto.getArtikelIId() == null
				|| handlagerbewegungDto.getNMenge() == null
				|| handlagerbewegungDto.getLagerIId() == null
				|| handlagerbewegungDto.getBAbgang() == null
				|| handlagerbewegungDto.getCKommentar() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"handlagerbewegungDto.getArtikelIId() == null || handlagerbewegungDto.getFMenge() == null || handlagerbewegungDto.getLagerIId() == null || handlagerbewegungDto.getCKommentar() == null"));
		}
		if (handlagerbewegungDto.getNMenge().doubleValue() <= 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN,
					new Exception(
							"handlagerbewegungDto.getFMenge().doubleValue() <= 0"));
		}
		// Wenn Ein Lagerplatz mitangegeben wird und es sich um eine Zubuchung
		// handelt, dann wird entweder
		// Ein neuer Lagerplatz angelegt oder ein vorhandener upgedated oder
		// eine Zuordnung geloescht
		if (handlagerbewegungDto.isBAendereLagerplatz()) {
			artikellagerplatzCRUD(handlagerbewegungDto.getArtikelIId(),
					handlagerbewegungDto.getLagerIId(),
					handlagerbewegungDto.getLagerplatzIId(), theClientDto);
		}

		// es wird davon ausgegangen, dass ein Artikel nicht lagerbewirtschaftet
		// ist
		boolean bLagerbewirtschaftet = false;
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
				handlagerbewegungDto.getArtikelIId(), theClientDto);

		// Nachsehen, ob Artikel lagerbewirtschaftet ist
		if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == true) {
			bLagerbewirtschaftet = true;
		}
		// Wenn Artikel nicht Lagerbewirtschaftet ist, dann wirs automatisch auf
		// "KEIN LAGER" gebucht
		if (bLagerbewirtschaftet == false) {
			LagerDto tempLager = lagerFindByCNrByMandantCNr(
					LagerFac.LAGER_KEINLAGER, theClientDto.getMandant());
			if (tempLager != null) {
				handlagerbewegungDto.setLagerIId(tempLager.getIId());
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.LAGER_KEIN_LAGER_NICHT_ANGELEGT,
						new Exception("LAGER_KEIN_LAGER_NICHT_ANGELEGT"));
			}
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HANDLAGERBEWEGUNG);
			handlagerbewegungDto.setIId(pk);
			Handlagerbewegung handlagerbewegung = new Handlagerbewegung(
					handlagerbewegungDto.getIId(),
					handlagerbewegungDto.getArtikelIId(),
					handlagerbewegungDto.getLagerIId(),
					handlagerbewegungDto.getNMenge(),
					handlagerbewegungDto.getCKommentar(),
					theClientDto.getIDPersonal(),
					handlagerbewegungDto.getBAbgang());
			em.persist(handlagerbewegung);
			em.flush();
			setHandlagerbewegungFromHandlagerbewegungDto(handlagerbewegung,
					handlagerbewegungDto);

			// Lagerbewegung durchfuehren
			if (Helper.short2boolean(handlagerbewegungDto.getBAbgang())) {

				bucheAb(LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
						handlagerbewegung.getIId(),
						handlagerbewegung.getArtikelIId(),
						handlagerbewegungDto.getNMenge(),
						handlagerbewegungDto.getNVerkaufspreis(),
						handlagerbewegung.getLagerIId(),
						handlagerbewegungDto.getSeriennrChargennrMitMenge(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto);
			} else {

				if (handlagerbewegungDto.getBelegartCNrUrsprung() == null
						&& handlagerbewegungDto.getBelegartCNrUrsprung() == null) {
					bucheZu(LocaleFac.BELEGART_HAND,
							handlagerbewegung.getIId(),
							handlagerbewegung.getIId(),
							handlagerbewegung.getArtikelIId(),
							handlagerbewegung.getNMenge(),
							handlagerbewegungDto.getNEinstandspreis(),
							handlagerbewegung.getLagerIId(),
							handlagerbewegungDto.getSeriennrChargennrMitMenge(),
							new java.sql.Timestamp(System.currentTimeMillis()),
							theClientDto);
				} else {
					bucheZu(LocaleFac.BELEGART_HAND,
							handlagerbewegung.getIId(), handlagerbewegung
									.getIId(), handlagerbewegung
									.getArtikelIId(), handlagerbewegung
									.getNMenge(), handlagerbewegungDto
									.getNEinstandspreis(), handlagerbewegung
									.getLagerIId(), handlagerbewegungDto
									.getSeriennrChargennrMitMenge(),
							new java.sql.Timestamp(System.currentTimeMillis()),
							theClientDto, handlagerbewegungDto
									.getBelegartCNrUrsprung(),
							handlagerbewegungDto
									.getBelegartpositionIIdUrsprung());
				}

			}

			if (handlagerbewegungDto.getSeriennrChargennrMitMenge() != null
					&& handlagerbewegungDto.getSeriennrChargennrMitMenge()
							.size() > 0) {
				setzeHerstellerUrsprungsland(handlagerbewegungDto.getLandIId(),
						handlagerbewegungDto.getHerstellerIId(),
						LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
						handlagerbewegungDto.getSeriennrChargennrMitMenge()
								.get(0).getCSeriennrChargennr(), theClientDto);
			} else {
				setzeHerstellerUrsprungsland(handlagerbewegungDto.getLandIId(),
						handlagerbewegungDto.getHerstellerIId(),
						LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
						null, theClientDto);

			}

			return handlagerbewegung.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto) {

		if (handlagerbewegungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("handlagerbewegungDto == null"));
		}
		if (handlagerbewegungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("handlagerbewegungDto.getIId() == null"));
		}
		Handlagerbewegung handlagerbewegung = em.find(Handlagerbewegung.class,
				handlagerbewegungDto.getIId());
		if (handlagerbewegung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		// Zuerst muss die Handlagerbewegun mit 0 ausbuchbar sein, dann erst
		// darf sie geloescht werden:
		// Lagerbewegung durchfuehren
		if (Helper.short2boolean(handlagerbewegungDto.getBAbgang())) {

			bucheAb(LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
					handlagerbewegung.getIId(),
					handlagerbewegung.getArtikelIId(), new BigDecimal(0),
					handlagerbewegungDto.getNVerkaufspreis(),
					handlagerbewegung.getLagerIId(), (List) null,
					new java.sql.Timestamp(System.currentTimeMillis()),
					theClientDto);
		} else {

			bucheZu(LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
					handlagerbewegung.getIId(),
					handlagerbewegung.getArtikelIId(), new BigDecimal(0),
					handlagerbewegungDto.getNEinstandspreis(),
					handlagerbewegung.getLagerIId(), null,
					new java.sql.Timestamp(System.currentTimeMillis()),
					theClientDto);

		}

		try {
			em.remove(handlagerbewegung);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateHandlagerbewegung(
			HandlagerbewegungDto handlagerbewegungDto, TheClientDto theClientDto) {

		if (handlagerbewegungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("handlagerbewegungDto == null"));
		}
		if (handlagerbewegungDto.getIId() == null
				|| handlagerbewegungDto.getArtikelIId() == null
				|| handlagerbewegungDto.getNMenge() == null
				|| handlagerbewegungDto.getLagerIId() == null
				|| handlagerbewegungDto.getBAbgang() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"(handlagerbewegungDto.getIId() == null || handlagerbewegungDto.getArtikelIId() == null || handlagerbewegungDto.getFMenge() == null || handlagerbewegungDto.getLagerIId() == null || handlagerbewegungDto.getBAbgang() == null"));
		}
		Integer iId = handlagerbewegungDto.getIId();
		handlagerbewegungDto
				.setPersonalIIdAendern(theClientDto.getIDPersonal());
		Handlagerbewegung handlagerbewegung = em.find(Handlagerbewegung.class,
				iId);
		if (handlagerbewegung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (Helper.short2boolean(handlagerbewegungDto.getBAbgang())) {

			bucheAb(LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
					handlagerbewegung.getIId(),
					handlagerbewegung.getArtikelIId(),
					handlagerbewegungDto.getNMenge(),
					handlagerbewegungDto.getNVerkaufspreis(),
					handlagerbewegung.getLagerIId(),
					handlagerbewegungDto.getSeriennrChargennrMitMenge(),
					new java.sql.Timestamp(System.currentTimeMillis()),
					theClientDto);
		} else {
			// PJ Gestehungspreis Belegdatum bleibt
			bucheZu(LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
					handlagerbewegung.getIId(),
					handlagerbewegung.getArtikelIId(),
					handlagerbewegungDto.getNMenge(),
					handlagerbewegungDto.getNEinstandspreis(),
					handlagerbewegung.getLagerIId(),
					handlagerbewegungDto.getSeriennrChargennrMitMenge(),
					handlagerbewegung.getTBuchungszeit(), theClientDto);

		}

		if (handlagerbewegungDto.getSeriennrChargennrMitMenge() != null
				&& handlagerbewegungDto.getSeriennrChargennrMitMenge().size() > 0) {
			setzeHerstellerUrsprungsland(handlagerbewegungDto.getLandIId(),
					handlagerbewegungDto.getHerstellerIId(),
					LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(),
					handlagerbewegungDto.getSeriennrChargennrMitMenge().get(0)
							.getCSeriennrChargennr(), theClientDto);
		} else {
			setzeHerstellerUrsprungsland(handlagerbewegungDto.getLandIId(),
					handlagerbewegungDto.getHerstellerIId(),
					LocaleFac.BELEGART_HAND, handlagerbewegung.getIId(), null,
					theClientDto);

		}

		setHandlagerbewegungFromHandlagerbewegungDto(handlagerbewegung,
				handlagerbewegungDto);
	}

	public HandlagerbewegungDto handlagerbewegungFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		Handlagerbewegung handlagerbewegung = em.find(Handlagerbewegung.class,
				iId);
		if (handlagerbewegung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		HandlagerbewegungDto handlagerDto = assembleHandlagerbewegungDto(handlagerbewegung);
		handlagerDto
				.setSeriennrChargennrMitMenge(getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
						LocaleFac.BELEGART_HAND, handlagerDto.getIId()));

		if (handlagerDto.getSeriennrChargennrMitMenge() != null
				&& handlagerDto.getSeriennrChargennrMitMenge().size() > 0) {
			LagerbewegungDto lagerbewegungDto = getLetzteintrag(
					LocaleFac.BELEGART_HAND, handlagerDto.getIId(),
					handlagerDto.getSeriennrChargennrMitMenge().get(0)
							.getCSeriennrChargennr());

			handlagerDto.setHerstellerIId(lagerbewegungDto.getHerstellerIId());
			handlagerDto.setLandIId(lagerbewegungDto.getLandIId());
		} else {
			LagerbewegungDto lagerbewegungDto = getLetzteintrag(
					LocaleFac.BELEGART_HAND, handlagerDto.getIId(), null);

			if (lagerbewegungDto != null) {
				handlagerDto.setHerstellerIId(lagerbewegungDto
						.getHerstellerIId());
				handlagerDto.setLandIId(lagerbewegungDto.getLandIId());
			}
		}

		handlagerDto.setLagerDto(lagerFindByPrimaryKey(handlagerDto
				.getLagerIId()));

		handlagerDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
				handlagerDto.getArtikelIId(), theClientDto));

		return handlagerDto;
	}

	private void setHandlagerbewegungFromHandlagerbewegungDto(
			Handlagerbewegung handlagerbewegung,
			HandlagerbewegungDto handlagerbewegungDto) {
		handlagerbewegung.setArtikelIId(handlagerbewegungDto.getArtikelIId());
		handlagerbewegung.setLagerIId(handlagerbewegungDto.getLagerIId());
		handlagerbewegung.setNMenge(handlagerbewegungDto.getNMenge());
		handlagerbewegung.setCKommentar(handlagerbewegungDto.getCKommentar());
		handlagerbewegung.setNEinstandspreis(handlagerbewegungDto
				.getNEinstandspreis());
		handlagerbewegung.setNGestehungspreis(handlagerbewegungDto
				.getNGestehungspreis());
		handlagerbewegung.setNVerkaufspreis(handlagerbewegungDto
				.getNVerkaufspreis());
		em.merge(handlagerbewegung);
		em.flush();
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

	public void createLagerumbuchung(LagerumbuchungDto lagerumbuchungDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (lagerumbuchungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lagerumbuchungDto == null"));
		}
		if (lagerumbuchungDto.getILagerbewegungidabbuchung() == null
				|| lagerumbuchungDto.getILagerbewegungidzubuchung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"lagerumbuchungDto.getLagerbewegungIIdBuchungAbbuchung() == null || lagerumbuchungDto.getLagerbewegungIIdBuchungZubuchung() == null"));
		}
		try {
			Lagerumbuchung lagerumbuchung = new Lagerumbuchung(
					lagerumbuchungDto.getILagerbewegungidzubuchung(),
					lagerumbuchungDto.getILagerbewegungidabbuchung());
			em.persist(lagerumbuchung);
			em.flush();
			setLagerumbuchungFromLagerumbuchungDto(lagerumbuchung,
					lagerumbuchungDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeLagerumbuchung(Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungAbbuchung) throws EJBExceptionLP {
		LagerumbuchungPK lagerumbuchungPK = new LagerumbuchungPK(
				lagerbewegungIIdBuchungZubuchung,
				lagerbewegungIIdBuchungAbbuchung);
		Lagerumbuchung toRemove = em.find(Lagerumbuchung.class,
				lagerumbuchungPK);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeLagerumbuchung(LagerumbuchungDto lagerumbuchungDto)
			throws EJBExceptionLP {
		if (lagerumbuchungDto != null) {
			Integer lagerbewegungIIdBuchungZubuchung = lagerumbuchungDto
					.getILagerbewegungidzubuchung();
			Integer lagerbewegungIIdBuchungAbbuchung = lagerumbuchungDto
					.getILagerbewegungidabbuchung();
			removeLagerumbuchung(lagerbewegungIIdBuchungZubuchung,
					lagerbewegungIIdBuchungAbbuchung);
		}
	}

	public void updateLagerumbuchung(LagerumbuchungDto lagerumbuchungDto)
			throws EJBExceptionLP {
		if (lagerumbuchungDto != null) {
			LagerumbuchungPK lagerumbuchungPK = new LagerumbuchungPK(
					lagerumbuchungDto.getILagerbewegungidzubuchung(),
					lagerumbuchungDto.getILagerbewegungidabbuchung());
			Lagerumbuchung lagerumbuchung = em.find(Lagerumbuchung.class,
					lagerumbuchungPK);
			if (lagerumbuchung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLagerumbuchungFromLagerumbuchungDto(lagerumbuchung,
					lagerumbuchungDto);
		}
	}

	public LagerumbuchungDto lagerumbuchungFindByPrimaryKey(
			Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungAbbuchung) throws EJBExceptionLP {
		LagerumbuchungPK lagerumbuchungPK = new LagerumbuchungPK(
				lagerbewegungIIdBuchungZubuchung,
				lagerbewegungIIdBuchungAbbuchung);
		Lagerumbuchung lagerumbuchung = em.find(Lagerumbuchung.class,
				lagerumbuchungPK);
		if (lagerumbuchung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleLagerumbuchungDto(lagerumbuchung);
	}

	public LagerumbuchungDto[] lagerumbuchungFindByIdAbbuchung(
			Integer lagerbewegungIIdBuchungAbbuchung) throws EJBExceptionLP {
		Query query = em.createNamedQuery("LagerumbuchungfindByIdAbbuchung");
		query.setParameter(1, lagerbewegungIIdBuchungAbbuchung);
		Collection<?> cl = query.getResultList();
		return assembleLagerumbuchungDtos(cl);
	}

	public LagerumbuchungDto[] lagerumbuchungFindByIdZubuchung(
			Integer lagerbewegungIIdBuchungZubuchung) throws EJBExceptionLP {
		Query query = em.createNamedQuery("LagerumbuchungfindByIdZubuchung");
		query.setParameter(1, lagerbewegungIIdBuchungZubuchung);
		Collection<?> cl = query.getResultList();
		return assembleLagerumbuchungDtos(cl);
	}

	private void setLagerumbuchungFromLagerumbuchungDto(
			Lagerumbuchung lagerumbuchung, LagerumbuchungDto lagerumbuchungDto) {
		em.merge(lagerumbuchung);
		em.flush();
	}

	private LagerumbuchungDto assembleLagerumbuchungDto(
			Lagerumbuchung lagerumbuchung) {
		return LagerumbuchungDtoAssembler.createDto(lagerumbuchung);
	}

	private LagerumbuchungDto[] assembleLagerumbuchungDtos(
			Collection<?> lagerumbuchungs) {
		List<LagerumbuchungDto> list = new ArrayList<LagerumbuchungDto>();
		if (lagerumbuchungs != null) {
			Iterator<?> iterator = lagerumbuchungs.iterator();
			while (iterator.hasNext()) {
				Lagerumbuchung lagerumbuchung = (Lagerumbuchung) iterator
						.next();
				list.add(assembleLagerumbuchungDto(lagerumbuchung));
			}
		}
		LagerumbuchungDto[] returnArray = new LagerumbuchungDto[list.size()];
		return (LagerumbuchungDto[]) list.toArray(returnArray);
	}

	/**
	 * Das Hauptlager des Mandanten des aktuellen Benutzers bestimmen.
	 * 
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return LagerDto das Hauptlager
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LagerDto getHauptlagerDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP {

		LagerDto oLagerDtoO = null;

		try {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {

				if (getMandantFac()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
								theClientDto)) {
					oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
							theClientDto.getMandant(),
							LagerFac.LAGERART_HAUPTLAGER);
				} else {
					oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
							getSystemFac().getHauptmandant(),
							LagerFac.LAGERART_HAUPTLAGER);
				}

			} else {
				oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
						theClientDto.getMandant(), LagerFac.LAGERART_HAUPTLAGER);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT,
					new Exception("Hauptlager des Mandanten == null"));
		}

		return oLagerDtoO;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public LagerDto getHauptlagerEinesMandanten(String mandantCNr) {
		LagerDto oLagerDtoO = null;
		try {
			oLagerDtoO = lagerFindByMandantCNrLagerartCNr(mandantCNr,
					LagerFac.LAGERART_HAUPTLAGER);
		} catch (Throwable t) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT,
					new Exception("Hauptlager des Mandanten == null"));
		}

		return oLagerDtoO;
	}

	public LagerDto getLagerWertgutschriftDesMandanten(TheClientDto theClientDto) {

		LagerDto oLagerDtoO = null;

		try {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {

				if (getMandantFac()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
								theClientDto)) {
					oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
							theClientDto.getMandant(),
							LagerFac.LAGERART_WERTGUTSCHRIFT);
				} else {
					oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
							getSystemFac().getHauptmandant(),
							LagerFac.LAGERART_WERTGUTSCHRIFT);
				}

			} else {
				oLagerDtoO = lagerFindByMandantCNrLagerartCNr(
						theClientDto.getMandant(),
						LagerFac.LAGERART_WERTGUTSCHRIFT);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_LAGER_WERTGUTSCHRIFTDESMANDANTEN_NICHT_ANGELEGT,
					new Exception("Hauptlager des Mandanten == null"));
		}

		return oLagerDtoO;
	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printSeriennummern(Integer lagerIId,
			Integer artikelIId, String[] snrs, String snrWildcard,
			Boolean bSortNachIdent, boolean bMitGeraeteseriennummern,
			String versionWildcard, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l.flrartikel, l.n_menge, l.t_buchungszeit, l.c_seriennrchargennr, l.c_belegartnr, l.i_belegartid, l.i_belegartpositionid, l.b_abgang, l.c_version, (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste.i_id=l.artikel_i_id AND spr.Id.locale.c_nr='"
				+ theClientDto.getLocUiAsString()
				+ "') as bez, (SELECT spr.c_zbez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste.i_id=l.artikel_i_id AND spr.Id.locale.c_nr='"
				+ theClientDto.getLocUiAsString()
				+ "') as zbez FROM FLRLagerbewegung as l "
				+ " WHERE l.n_menge > 0 AND l.b_historie=0 AND l.n_menge>0 AND l.c_seriennrchargennr IS NOT NULL ";

		// SP18698
		Boolean bZentralerArtikelstamm;
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
			//Wenn zentraler Artikelstamm, dann alle Bewegungen anzeigen
			bZentralerArtikelstamm=Boolean.TRUE;

		} else {
			bZentralerArtikelstamm=Boolean.FALSE;
			
			queryString += " AND l.flrlager.mandant_c_nr='"
					+ theClientDto.getMandant() + "' ";
		}

		if (lagerIId != null) {

			queryString += " AND l.flrlager.i_id=" + lagerIId;

		}

		if (artikelIId != null) {
			queryString += " AND l.flrartikel.i_id=" + artikelIId;

		}

		if (snrs != null) {
			String snrsIn = "";

			for (int i = 0; i < snrs.length; i++) {
				snrsIn += "'" + snrs[i] + "'";
				if (i != snrs.length - 1) {
					snrsIn += ",";
				}
			}
			queryString += " AND l.c_seriennrchargennr IN(" + snrsIn + ")";

		} else if (snrWildcard != null) {

			queryString += " AND l.c_seriennrchargennr LIKE '%" + snrWildcard
					+ "%'";

		}

		if (versionWildcard != null) {

			queryString += " AND l.c_version LIKE '%" + versionWildcard + "%'";

		}

		if (bSortNachIdent.booleanValue() == true) {

			queryString += " ORDER BY l.flrartikel.c_nr, l.c_seriennrchargennr";

		} else {

			queryString += " ORDER BY l.c_seriennrchargennr ASC, l.flrartikel.c_nr ASC";

		}

		boolean bGeraeteseriennummern = false;
		if (getMandantFac()
				.hatZusatzfunktionberechtigung(
						com.lp.server.system.service.MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
						theClientDto)) {
			bGeraeteseriennummern = true;
		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		index = -1;
		sAktuellerReport = LagerFac.REPORT_SERIENNUMMERN;

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();

			FLRArtikel flrartikel = (FLRArtikel) o[0];
			BigDecimal menge = (BigDecimal) o[1];
			java.util.Date buchungszeit = (java.util.Date) o[2];
			String cSnrChnr = (String) o[3];
			String belegartCNr = (String) o[4];
			Integer belegartIId = (Integer) o[5];
			Integer belegartpositionIId = (Integer) o[6];
			Short bAbgang = (Short) o[7];
			String cVersion = (String) o[8];

			String cBez = (String) o[9];
			String cZbez = (String) o[10];

			Object[] dataHelp = new Object[9];
			dataHelp[REPORT_SERIENNUMMERN_ZEITPUNKT] = buchungszeit;
			dataHelp[REPORT_SERIENNUMMERN_MENGE] = menge;
			dataHelp[REPORT_SERIENNUMMERN_ARTIKELNUMMER] = flrartikel.getC_nr();
			dataHelp[REPORT_SERIENNUMMERN_VERSION] = cVersion;

			if (bMitGeraeteseriennummern == true
					&& bGeraeteseriennummern == true
					&& Helper.short2boolean(flrartikel.getB_seriennrtragend()) == true) {

				Query queryGsnr = em
						.createNamedQuery("GeraetesnrfindByArtikelIIdCSnr");
				queryGsnr.setParameter(1, flrartikel.getI_id());
				queryGsnr.setParameter(2, cSnrChnr);
				Collection c = queryGsnr.getResultList();
				Iterator<?> iterator = c.iterator();
				Object[][] dataSub = new Object[c.size()][3];
				String[] fieldnames = new String[] { "F_ARTIKELNUMMER",
						"F_BEZEICHNUNG", "F_SNR" };
				int i = 0;
				while (iterator.hasNext()) {
					Geraetesnr g = (Geraetesnr) iterator.next();

					Session session2 = FLRSessionFactory.getFactory()
							.openSession();

					org.hibernate.Criteria crit2 = session
							.createCriteria(FLRLagerbewegung.class);
					crit2.add(Restrictions.eq(
							LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
							Helper.boolean2Short(false)));
					crit2.add(Restrictions.eq(
							LagerFac.FLR_LAGERBEWEGUNG_I_ID_BUCHUNG,
							g.getIIdBuchung()));

					List l = crit2.list();
					if (l.size() > 0) {
						FLRLagerbewegung flrLagerbew = (FLRLagerbewegung) l
								.iterator().next();
						dataSub[i][0] = flrLagerbew.getFlrartikel().getC_nr();

						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										flrLagerbew.getFlrartikel().getI_id(),
										theClientDto);
						dataSub[i][1] = aDto.formatBezeichnung();

						dataSub[i][2] = flrLagerbew.getC_seriennrchargennr();
					}

					session2.close();
					i++;
				}
				if (dataSub.length > 0) {
					dataHelp[REPORT_SERIENNUMMERN_SUBREPORT_GERAETESNR] = new LPDatenSubreport(
							dataSub, fieldnames);
				}
			}

			String artikelbez = "";

			if (cBez != null) {
				artikelbez += cBez + " ";
			}
			if (cZbez != null) {
				artikelbez += cZbez;
			}

			dataHelp[REPORT_SERIENNUMMERN_ARTIKELBEZEICHNUNG] = artikelbez;

			dataHelp[REPORT_SERIENNUMMERN_SERIENNUMER] = cSnrChnr;

			String cAbgang = "";
			String cZugang = "";

			if (Helper.short2boolean(bAbgang)) {

				if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {

					Handlagerbewegung handlagerbewegung = em.find(
							Handlagerbewegung.class, belegartpositionIId);
					if (handlagerbewegung != null) {
						cAbgang = LocaleFac.BELEGART_HAND.trim() + " "
								+ belegartpositionIId + " "
								+ handlagerbewegung.getCKommentar();
					} else {
						cAbgang = LocaleFac.BELEGART_HAND.trim() + " "
								+ belegartpositionIId
								+ " Error - Handbuchung not found";
					}

				} else {

					cAbgang = getBelegUndPartner(belegartCNr, belegartIId,
							belegartpositionIId, theClientDto);
				}
			} else {

				if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {

					Handlagerbewegung handlagerbewegung = em.find(
							Handlagerbewegung.class, belegartpositionIId);
					if (handlagerbewegung != null) {
						cZugang = LocaleFac.BELEGART_HAND.trim() + " "
								+ belegartpositionIId + " "
								+ handlagerbewegung.getCKommentar();
					} else {
						cZugang = LocaleFac.BELEGART_HAND.trim() + " "
								+ belegartpositionIId
								+ " Error - Handbuchung not found";
					}

				} else {

					cZugang = getBelegUndPartner(belegartCNr, belegartIId,
							belegartpositionIId, theClientDto);
				}
			}
			dataHelp[REPORT_SERIENNUMMERN_ABGANG] = cAbgang;
			dataHelp[REPORT_SERIENNUMMERN_ZUGANG] = cZugang;
			alDaten.add(dataHelp);
		}
		session.close();

		reklamationHinzufuegen(theClientDto, alDaten, artikelIId,
				bSortNachIdent, snrs, snrWildcard);

		for (int m = alDaten.size() - 1; m > 0; --m) {
			for (int n = 0; n < m; ++n) {
				Object[] o1 = (Object[]) alDaten.get(n);
				Object[] o2 = (Object[]) alDaten.get(n + 1);
				if (bSortNachIdent.booleanValue() == true) {
					if (((String) o1[REPORT_SERIENNUMMERN_ARTIKELNUMMER])
							.compareTo((String) o2[REPORT_SERIENNUMMERN_ARTIKELNUMMER]) > 0) {
						alDaten.set(n, o2);
						alDaten.set(n + 1, o1);
					}
				} else {
					if (((String) o1[REPORT_SERIENNUMMERN_SERIENNUMER])
							.compareTo((String) o2[REPORT_SERIENNUMMERN_SERIENNUMER]) > 0) {
						alDaten.set(n, o2);
						alDaten.set(n + 1, o1);
					}
				}
			}
		}

		data = new Object[alDaten.size()][8];
		for (int i = 0; i < alDaten.size(); i++) {
			data[i] = (Object[]) alDaten.get(i);
		}

		HashMap<String, Object> parameter = new HashMap<String, Object>();
		if (lagerIId != null) {
			parameter.put("P_LAGER", lagerFindByPrimaryKey(lagerIId).getCNr());
		} else {
			parameter.put("P_LAGER", "ALLE");

		}
		parameter.put("P_SERIENNUMMER",
				Helper.erzeugeStringAusStringArray(snrs));
		
		parameter.put("P_ZENTRALER_ARTIKELSTAMM",bZentralerArtikelstamm);
		

		parameter.put("P_MITGERAETESERIENNUMMERN", new Boolean(
				bMitGeraeteseriennummern));

		parameter.put("P_VERSION", versionWildcard);

		initJRDS(parameter, LagerFac.REPORT_MODUL,
				LagerFac.REPORT_SERIENNUMMERN, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void gestehungspreiseImportieren(ArrayList<Object[]> alDaten,
			Integer lagerIId, TheClientDto theClientDto) {

		try {
			Integer lagerIId_KeinLager = getLagerFac()
					.lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER,
							theClientDto.getMandant()).getIId();

			for (int i = 0; i < alDaten.size(); i++) {
				Object[] oZeile = alDaten.get(i);

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByCNrMandantCNrOhneExc((String) oZeile[0],
								theClientDto.getMandant());
				if (artikelDto != null) {

					ArtikellagerDto artikellagerDto = new ArtikellagerDto();

					artikellagerDto.setNGestehungspreis((BigDecimal) oZeile[1]);
					artikellagerDto.setArtikelIId(artikelDto.getIId());

					if (Helper.short2boolean(artikelDto
							.getBLagerbewirtschaftet())) {
						artikellagerDto.setLagerIId(lagerIId);
					} else {
						artikellagerDto.setLagerIId(lagerIId_KeinLager);
					}

					artikellagerDto.setMandantCNr(theClientDto.getMandant());

					getLagerFac().updateGestpreisArtikellager(artikellagerDto,
							theClientDto);
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	private void reklamationHinzufuegen(TheClientDto theClientDto,
			ArrayList<Object[]> alDaten, Integer artikelIId,
			Boolean bSortNachIdent, String[] snrs, String snrWildcard) {

		// PJ 16364 Reklamationen hinzufuegen

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT r FROM FLRReklamation as r "
				+ " WHERE r.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND r.c_seriennrchargennr IS NOT NULL ";

		if (artikelIId != null) {
			queryString += " AND r.flrartikel.i_id=" + artikelIId;

		}

		if (snrs != null) {
			String snrsIn = "";

			for (int i = 0; i < snrs.length; i++) {
				snrsIn += "'" + snrs[i] + "'";
				if (i != snrs.length - 1) {
					snrsIn += ",";
				}
			}
			queryString += " AND r.c_seriennrchargennr IN(" + snrsIn + ")";

		} else if (snrWildcard != null) {

			queryString += " AND r.c_seriennrchargennr LIKE '%" + snrWildcard
					+ "%'";

		}

		if (bSortNachIdent.booleanValue() == true) {

			queryString += " ORDER BY r.flrartikel.c_nr, r.c_seriennrchargennr";

		} else {

			queryString += " ORDER BY r.c_seriennrchargennr ASC, r.flrartikel.c_nr ASC";

		}

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRReklamation r = (FLRReklamation) resultListIterator.next();

			if (r.getFlrartikel() != null) {
				Object[] dataHelp = new Object[9];
				dataHelp[REPORT_SERIENNUMMERN_ZEITPUNKT] = r.getT_belegdatum();
				dataHelp[REPORT_SERIENNUMMERN_MENGE] = r.getN_menge();

				ArtikelDto artikelDto;
				try {

					String kurzzeichenReklamation = getLocaleFac()
							.belegartFindByCNr(LocaleFac.BELEGART_REKLAMATION)
							.getCKurzbezeichnung();
					artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
							r.getFlrartikel().getI_id(), theClientDto);
					dataHelp[REPORT_SERIENNUMMERN_ARTIKELNUMMER] = artikelDto
							.getCNr();
					dataHelp[REPORT_SERIENNUMMERN_ARTIKELBEZEICHNUNG] = artikelDto
							.formatBezeichnung();

					dataHelp[REPORT_SERIENNUMMERN_SERIENNUMER] = r
							.getC_seriennrchargennr();

					dataHelp[REPORT_SERIENNUMMERN_ZUGANG] = "";
					dataHelp[REPORT_SERIENNUMMERN_ABGANG] = "";

					if (r.getReklamationart_c_nr().equals(
							ReklamationFac.REKLAMATIONART_LIEFERANT)) {
						dataHelp[REPORT_SERIENNUMMERN_ZUGANG] = kurzzeichenReklamation
								+ " " + r.getC_nr();

						if (r.getFlrlieferant() != null) {

							LieferantDto lfDto = getLieferantFac()
									.lieferantFindByPrimaryKey(
											r.getFlrlieferant().getI_id(),
											theClientDto);

							dataHelp[REPORT_SERIENNUMMERN_ZUGANG] = dataHelp[REPORT_SERIENNUMMERN_ZUGANG]
									+ lfDto.getPartnerDto()
											.formatFixTitelName1Name2();
						}

					} else {
						dataHelp[REPORT_SERIENNUMMERN_ABGANG] = kurzzeichenReklamation
								+ " " + r.getC_nr();
						if (r.getFlrkunde() != null) {

							KundeDto lfDto = getKundeFac()
									.kundeFindByPrimaryKey(
											r.getFlrkunde().getI_id(),
											theClientDto);

							dataHelp[REPORT_SERIENNUMMERN_ZUGANG] = dataHelp[REPORT_SERIENNUMMERN_ZUGANG]
									+ lfDto.getPartnerDto()
											.formatFixTitelName1Name2();
						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
				alDaten.add(dataHelp);
			}
		}
	}

	public BigDecimal getAbgewertetenGestehungspreis(BigDecimal gestpreis,
			Integer artikelIId, Integer lagerIId, Timestamp tStichtag,
			int iMonate, double dProzent) {
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class);
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID,
				artikelIId));

		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));

		if (lagerIId != null) {
			crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_LAGER_I_ID,
					lagerIId));
		}

		if (tStichtag != null) {
			crit.add(Restrictions.le(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
					tStichtag));
		} else {
			crit.add(Restrictions.lt(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM,
					Helper.cutTimestamp(new Timestamp(System
							.currentTimeMillis() + 24 * 3600000))));
		}

		// CK: Kundenwunsch am 2009-03-04: Handbuchungen und
		// Inventurbuchungen
		// sollen nicht beruecksichtigt werden
		String[] belegarten = new String[2];
		belegarten[0] = LocaleFac.BELEGART_INVENTUR;
		belegarten[1] = LocaleFac.BELEGART_HAND;
		crit.add(Restrictions.not(Restrictions.in(
				LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, belegarten)));

		// CK: Kundenwunsch am 2009-03-04: Es muessen sowohl
		// Zubuchungen, als auch Abbuchungen
		// beruecksichtigt werden
		// crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG, Helper
		// .boolean2Short(false)));
		crit.addOrder(Order.desc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

		List results = crit.list();
		Iterator resultListIterator = results.iterator();

		ArrayList<FLRLagerbewegung> alZugaenge = new ArrayList<FLRLagerbewegung>();

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();

			if (lagerbewegung.getN_menge().doubleValue() > 0) {
				// Datum des letzten Zugangs
				alZugaenge.add(lagerbewegung);
			}
		}

		if (alZugaenge.size() > 0) {
			// Lagerbewegungen nach Belegdatum sortieren
			for (int m = alZugaenge.size() - 1; m > 0; --m) {
				for (int n = 0; n < m; ++n) {
					FLRLagerbewegung o1 = (FLRLagerbewegung) alZugaenge.get(n);
					FLRLagerbewegung o2 = (FLRLagerbewegung) alZugaenge
							.get(n + 1);

					if (o1.getT_belegdatum().before(o2.getT_belegdatum())) {
						alZugaenge.set(n, o2);
						alZugaenge.set(n + 1, o1);
					}
				}
			}

			// nun zaehlt das juengste Belegdatum
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) alZugaenge
					.get(0);

			java.util.Calendar cAbDemWirdAbgewertet = java.util.Calendar
					.getInstance();
			if (tStichtag != null) {
				cAbDemWirdAbgewertet.setTimeInMillis(tStichtag.getTime());
			}

			cAbDemWirdAbgewertet.set(java.util.Calendar.MONTH,
					cAbDemWirdAbgewertet.get(java.util.Calendar.MONTH)
							- iMonate);

			double dAbwertung = 0;
			while (dAbwertung < 100
					&& lagerbewegung.getT_buchungszeit().getTime() < cAbDemWirdAbgewertet
							.getTimeInMillis()) {
				dAbwertung += dProzent;
				cAbDemWirdAbgewertet.set(java.util.Calendar.MONTH,
						cAbDemWirdAbgewertet.get(java.util.Calendar.MONTH) - 1);
			}

			return Helper.rundeKaufmaennisch(
					gestpreis.multiply(new BigDecimal(1 - (dAbwertung / 100))),
					4);

		}

		session.close();

		return new BigDecimal(0);
	}

	private String getBelegUndPartner(String belegartCNr, Integer belegartIId,
			Integer belegartpositionIId, TheClientDto theClientDto) {
		String belegnummer = "";
		String partner = "";
		String kurzzeichen = null;
		try {
			kurzzeichen = getLocaleFac().belegartFindByCNr(belegartCNr)
					.getCKurzbezeichnung();
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		if (belegartCNr.equals(LocaleFac.BELEGART_HAND)) {
			belegnummer = LocaleFac.BELEGART_HAND.trim() + " "
					+ belegartpositionIId;
			try {
				kurzzeichen = "";
				HandlagerbewegungDto handlagerbwewgungDto = getLagerFac()
						.handlagerbewegungFindByPrimaryKey(belegartpositionIId,
								theClientDto);

				partner = handlagerbwewgungDto.getCKommentar();

			} catch (Exception ex) {
				partner = "Error - Handbuchung not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
			try {

				com.lp.server.rechnung.service.RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(belegartIId);

				belegnummer = rechnungDto.getCNr();
				partner = getKundeFac()
						.kundeFindByPrimaryKey(rechnungDto.getKundeIId(),
								theClientDto).getPartnerDto().formatAnrede();
			} catch (Exception ex) {
				partner = "Error - RE not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
			try {
				com.lp.server.rechnung.service.RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(belegartIId);

				belegnummer = rechnungDto.getCNr();
				partner = getKundeFac()
						.kundeFindByPrimaryKey(rechnungDto.getKundeIId(),
								theClientDto).getPartnerDto().formatAnrede();
			} catch (Exception ex) {
				belegnummer = "Error - GS not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
			try {

				com.lp.server.bestellung.service.BestellungDto bestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(belegartIId);

				belegnummer = bestellungDto.getCNr();
				partner = getLieferantFac()
						.lieferantFindByPrimaryKey(
								bestellungDto.getLieferantIIdBestelladresse(),
								theClientDto).getPartnerDto().formatAnrede();
			} catch (Exception ex) {
				belegnummer = "Error - BES not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)
				|| belegartCNr.equals(LocaleFac.BELEGART_LSZIELLAGER)) {
			try {
				LieferscheinDto lieferscheinDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(belegartIId, theClientDto);
				belegnummer = lieferscheinDto.getCNr();

				partner = getKundeFac()
						.kundeFindByPrimaryKey(
								lieferscheinDto.getKundeIIdLieferadresse(),
								theClientDto).getPartnerDto().formatAnrede();
			} catch (Exception ex) {
				belegnummer = "Error - LS not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {
			try {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(
						belegartIId);
				belegnummer = losDto.getCNr();
			} catch (Exception ex) {
				belegnummer = "Error - LOS not found";
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
			try {
				LosistmaterialDto dto = getFertigungFac()
						.losistmaterialFindByPrimaryKeyOhneExc(
								belegartpositionIId);
				if (dto != null) {
					LossollmaterialDto lossollmaterialDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKey(
									dto.getLossollmaterialIId());
					LosDto losDto = getFertigungFac().losFindByPrimaryKey(
							lossollmaterialDto.getLosIId());
					belegnummer = losDto.getCNr();
				} else {
					belegnummer = "Error - LOS not found";
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		} else if (belegartCNr.equals(LocaleFac.BELEGART_INVENTUR)) {
			try {
				// Hier koennen wir nicht auf die Positions-ID der Inventurliste
				// zurueckgreifen
				// da diese bei jeder Aenderung neu angelegt und geloescht wird
				InventurDto inventurDto = getInventurFac()
						.inventurFindByPrimaryKey(belegartIId, theClientDto);
				partner = inventurDto.getCBez();
				belegnummer = "";
			} catch (RemoteException ex) {
				belegnummer = "Inventur not found";
			}

		} else {
			belegnummer = "BELEGART NOT IMPLEMENTED YET";
		}

		return kurzzeichen + belegnummer + " " + partner;

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal getVerbrauchteMengeEinesArtikels(Integer artikelIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		// PJ 14006

		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class)
				.createAlias("flrartikel", "a")
				.add(Restrictions.eq("a.i_id", artikelIId))
				.createAlias("flrlager", "l")
				.add(Restrictions.eq("l.mandant_c_nr",
						theClientDto.getMandant()));
		crit.add(Restrictions.ge(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tVon));
		crit.add(Restrictions.le(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tBis));

		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));
		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
				Helper.boolean2Short(true)));
		crit.add(Restrictions.not(Restrictions.in(
				LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, new String[] {
						LocaleFac.BELEGART_HAND,
						LocaleFac.BELEGART_LOSABLIEFERUNG })));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal bdMenge = new BigDecimal(0);

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();
			if (lagerbewegung.getN_menge().doubleValue() > 0) {
				bdMenge = bdMenge.add(lagerbewegung.getN_menge());
			}
		}

		return bdMenge;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public BigDecimal getLosablieferungenEinesArtikels(Integer artikelIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		// PJ 14006
		org.hibernate.Criteria crit = session
				.createCriteria(FLRLagerbewegung.class)
				.createAlias("flrartikel", "a")
				.add(Restrictions.eq("a.i_id", artikelIId))
				.createAlias("flrlager", "l")
				.add(Restrictions.eq("l.mandant_c_nr",
						theClientDto.getMandant()));
		crit.add(Restrictions.ge(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tVon));
		crit.add(Restrictions.le(LagerFac.FLR_LAGERBEWEGUNG_T_BELEGDATUM, tBis));

		crit.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
				Helper.boolean2Short(false)));

		crit.add(Restrictions.in(LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR,
				new String[] { LocaleFac.BELEGART_LOSABLIEFERUNG }));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal bdMenge = new BigDecimal(0);

		while (resultListIterator.hasNext()) {
			FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
					.next();

			bdMenge = bdMenge.add(lagerbewegung.getN_menge());

		}

		return bdMenge;
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;

		String fieldName = jRField.getName();

		if (sAktuellerReport.equals(LagerFac.REPORT_SERIENNUMMERN)) {

			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_ARTIKELBEZEICHNUNG];
			} else if ("Menge".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_MENGE];
			} else if ("Abgang".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_ABGANG];
			} else if ("Zugang".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_ZUGANG];
			} else if ("Seriennummer".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_SERIENNUMER];
			} else if ("Version".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_VERSION];
			} else if ("Zeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_ZEITPUNKT];
			} else if ("DatenSubreport".equals(fieldName)) {
				value = data[index][REPORT_SERIENNUMMERN_SUBREPORT_GERAETESNR];
			}

		}

		return value;

	}

	/**
	 * Ermittlung des Solllagerstandes eines Artikels.
	 * 
	 * Soll-Lagerstand ist im Artikel definierbar. Falls dieser nicht definiert
	 * ist, greift der Mindest-Lagerstand. Ist auch der Mindest-Lagerstand nicht
	 * definiert -> 0
	 * 
	 * @param artikelDto
	 *            ArtikelDto
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getArtikelSollBestand(ArtikelDto artikelDto)
			throws EJBExceptionLP {
		BigDecimal bdSollLagerstand;
		// Solllagerstand definiert?
		if (artikelDto.getFLagersoll() != null) {
			// Mindestbestand > Sollbestand? -> dann zieht der Mindestbestand.
			// sollte aber nicht vorkommen.
			if (artikelDto.getFLagermindest() != null
					&& artikelDto.getFLagermindest() > artikelDto
							.getFLagersoll()) {
				bdSollLagerstand = new BigDecimal(artikelDto.getFLagermindest());
			}
			// Normalfall: der definierte Sollbestand wirkt.
			else {
				bdSollLagerstand = new BigDecimal(artikelDto.getFLagersoll());
			}
		}
		// kein Sollbestand definiert
		else {
			// -> es zieht der Mindestbestand, falls dieser definiert ist.
			if (artikelDto.getFLagermindest() != null) {
				bdSollLagerstand = new BigDecimal(artikelDto.getFLagermindest());
			}
			// sonst 0.
			else {
				bdSollLagerstand = new BigDecimal(0);
			}
		}
		return bdSollLagerstand;
	}

	public String getAsStringDocumentWS(Integer iIdBestellpositionI,
			TheClientDto theClientDto) {
		return "juhu";
	}

	public void vertauscheArtikellagerplaetze(Integer iId1, Integer iId2) {

		Artikellagerplaetze o1 = em.find(Artikellagerplaetze.class, iId1);
		Artikellagerplaetze o2 = em.find(Artikellagerplaetze.class, iId2);
		Integer iSort1 = o1.getiSort();
		Integer iSort2 = o2.getiSort();

		o2.setiSort(new Integer(-1));
		o1.setiSort(iSort2);
		o2.setiSort(iSort1);

	}

}
