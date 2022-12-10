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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikellieferant;
import com.lp.server.artikel.ejb.Artikellieferantstaffel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.BestellpositionQuery;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellpositionServiceDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Beleg;
import com.lp.server.util.Validator;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IDetect;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BestellpositionFacBean extends Beleg implements BestellpositionFac, IPrimitiveSwapper, IImportPositionen {
	private static final long serialVersionUID = 280414487642286618L;

	@PersistenceContext
	private EntityManager em;

	private void setBestellpositionFromBestellpositionDto(Bestellposition bestellposition,
			BestellpositionDto bestellpositionDto) {
		bestellposition.setBestellungIId(bestellpositionDto.getBestellungIId());
		bestellposition.setISort(bestellpositionDto.getISort());
		bestellposition.setBestellpositionartCNr(bestellpositionDto.getPositionsartCNr());
		bestellposition.setBestellpositionstatusCNr(bestellpositionDto.getBestellpositionstatusCNr());
		bestellposition.setArtikelIId(bestellpositionDto.getArtikelIId());
		bestellposition.setCBez(bestellpositionDto.getCBez());
		bestellposition.setCZbez(bestellpositionDto.getCZusatzbez());
		bestellposition.setBestellpositionIIdRahmenposition(bestellpositionDto.getIBestellpositionIIdRahmenposition());
		bestellposition.setKundeIId(bestellpositionDto.getKundeIId());
		bestellposition.setNOffenemenge(bestellpositionDto.getNOffeneMenge());
		bestellposition.setBNettopreisuebersteuert(bestellpositionDto.getBNettopreisuebersteuert());

		if (bestellpositionDto.getBArtikelbezeichnunguebersteuert() != null) {
			bestellposition.setBArtikelbezuebersteuert(bestellpositionDto.getBArtikelbezeichnunguebersteuert());
		}
		bestellposition.setCTextinhalt(bestellpositionDto.getXTextinhalt());
		bestellposition.setMediastandardIId(bestellpositionDto.getMediastandardIId());
		bestellposition.setNMenge(bestellpositionDto.getNMenge());
		bestellposition.setNOffenemenge(bestellpositionDto.getNOffeneMenge());
		bestellposition.setEinheitCNr(bestellpositionDto.getEinheitCNr());
		bestellposition.setFRabattsatz(bestellpositionDto.getDRabattsatz());

		bestellposition.setMwstsatzIId(bestellpositionDto.getMwstsatzIId());
		if (bestellpositionDto.getBMwstsatzUebersteuert() != null) {
			bestellposition.setBMwstsatzuebersteuert(bestellpositionDto.getBMwstsatzUebersteuert());
		}
		bestellposition.setNNettoeinzelpreis(bestellpositionDto.getNNettoeinzelpreis());
		bestellposition.setNRabattbetrag(bestellpositionDto.getNRabattbetrag());
		bestellposition.setNNettogesamtpreis(bestellpositionDto.getNNettogesamtpreis());
		if (bestellpositionDto.getTUebersteuerterLiefertermin() != null) {
			bestellposition.setTUebersteuerterliefertermin(bestellpositionDto.getTUebersteuerterLiefertermin());
		}
		if (bestellpositionDto.getBDrucken() != null) {
			bestellposition.setBDrucken(bestellpositionDto.getBDrucken());
		}

		bestellposition.setTAuftragsbestaetigungstermin(bestellpositionDto.getTAuftragsbestaetigungstermin());
		bestellposition.setCAbkommentar(bestellpositionDto.getCABKommentar());
		bestellposition.setCAbnummer(bestellpositionDto.getCABNummer());

		bestellposition.setNFixkosten(bestellpositionDto.getNFixkosten());
		bestellposition.setNFixkostengeliefert(bestellpositionDto.getNFixkostengeliefert());
		bestellposition.setTManuellvollstaendiggeliefert(bestellpositionDto.getTManuellvollstaendiggeliefert());
		bestellposition.setPersonalIIdAbterminAendern(bestellpositionDto.getPersonalIIdAbterminAendern());
		bestellposition.setTAbterminAendern(bestellpositionDto.getTAbterminAendern());
		bestellposition.setTAbursprungstermin(bestellpositionDto.getTAbursprungstermin());
		bestellposition.setCAngebotnummer(bestellpositionDto.getCAngebotnummer());
		bestellposition.setNMaterialzuschlag(bestellpositionDto.getNMaterialzuschlag());
		bestellposition.setLossollmaterialIId(bestellpositionDto.getLossollmaterialIId());
		bestellposition.setPositionIIdArtikelset(bestellpositionDto.getPositioniIdArtikelset());

		bestellposition.setGebindeIId(bestellpositionDto.getGebindeIId());
		bestellposition.setNAnzahlgebinde(bestellpositionDto.getNAnzahlgebinde());
		bestellposition.setBWepinfoAnAnforderer(bestellpositionDto.getBWepinfoAnAnforderer());

		bestellposition.setTAbBelegdatum(bestellpositionDto.getTAbBelegdatum());
		
		em.merge(bestellposition);
		em.flush();
	}

	/**
	 * Fuer eine bestehende Bestellposition vom Typ Ident oder Handeingabe werden
	 * die zusaetzlichen Preisfelder befuellt.
	 * 
	 * @param iIdPositionI PK der Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI) throws EJBExceptionLP {
		try {
			Bestellposition oPosition = em.find(Bestellposition.class, iIdPositionI);
			if (oPosition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (oPosition.getBestellpositionartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
					|| oPosition.getBestellpositionartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

				BestellungDto oBestellung = getBestellungFac().bestellungFindByPrimaryKey(oPosition.getBestellungIId());

				// - Allgemeiner Rabatt
				BigDecimal bdAllgemeinerRabatt = new BigDecimal(oBestellung.getFAllgemeinerRabattsatz().doubleValue())
						.movePointLeft(2);
				bdAllgemeinerRabatt = Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);

				if (oPosition.getNNettogesamtpreis() != null) {
					BigDecimal bdNettogesamtpreisAllgemeinerRabattSumme = oPosition.getNNettogesamtpreis()
							.multiply(bdAllgemeinerRabatt);

					oPosition.setNNettogesamtpreisminusrabatte(Helper.rundeKaufmaennisch(
							oPosition.getNNettogesamtpreis().subtract(bdNettogesamtpreisAllgemeinerRabattSumme), 4));
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private BestellpositionDto assembleBestellpositionDto(Bestellposition bestellposition) {
		return BestellpositionDtoAssembler.createDto(bestellposition);
	}

	private BestellpositionDto[] assembleBestellpositionDtos(Collection<?> bestellpositions) {
		List<BestellpositionDto> list = new ArrayList<BestellpositionDto>();
		if (bestellpositions != null) {
			Iterator<?> iterator = bestellpositions.iterator();
			while (iterator.hasNext()) {
				Bestellposition bestellposition = (Bestellposition) iterator.next();
				list.add(assembleBestellpositionDto(bestellposition));
			}
		}
		BestellpositionDto[] returnArray = new BestellpositionDto[list.size()];
		return (BestellpositionDto[]) list.toArray(returnArray);
	}

	public Integer createBestellposition(BestellpositionDto oBestellpositionDtoI, TheClientDto theClientDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern) throws EJBExceptionLP {
		return createBestellposition(oBestellpositionDtoI, true, theClientDto, sPreispflegeI,
				artikellieferantstaffelIId_ZuAendern);
	}

	private void preisGemeinsamerArtikelAngleichen(BestellpositionDto oBestellpositionDtoI, TheClientDto theClientDto) {

		if (oBestellpositionDtoI.getArtikelIId() != null) {
			Artikel artikel = em.find(Artikel.class, oBestellpositionDtoI.getArtikelIId());
			if (Helper.short2boolean(artikel.getBSummeInBestellung()) == true) {
				Query query = em.createNamedQuery("BestellpositionfindByBestellungIIdArtikelIId");
				query.setParameter(1, oBestellpositionDtoI.getBestellungIId());
				query.setParameter(2, oBestellpositionDtoI.getArtikelIId());
				Collection<?> cl = query.getResultList();

				Iterator it = cl.iterator();

				while (it.hasNext()) {
					Bestellposition bspos = (Bestellposition) it.next();

					if (!oBestellpositionDtoI.getIId().equals(bspos.getIId())) {
						BestellpositionDto bsposDto = bestellpositionFindByPrimaryKey(bspos.getIId());

						bsposDto.setNNettoeinzelpreis(oBestellpositionDtoI.getNNettoeinzelpreis());
						bsposDto.setNNettogesamtpreis(oBestellpositionDtoI.getNNettogesamtpreis());
						bsposDto.setNNettogesamtPreisminusRabatte(
								oBestellpositionDtoI.getNNettogesamtPreisminusRabatte());
						bsposDto.setNRabattbetrag(oBestellpositionDtoI.getNRabattbetrag());
						bsposDto.setDRabattsatz(oBestellpositionDtoI.getDRabattsatz());

						updateBestellposition(bsposDto, theClientDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null, false, false);

					}

				}

			}

		}

	}

	public Integer createBestellposition(BestellpositionDto oBestellpositionDtoI, boolean bArtikelSetAufloesen,
			TheClientDto theClientDto, String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern)
			throws EJBExceptionLP {

		Bestellposition bestellposition = new Bestellposition();

		try {
			checkBestellpositionDto(oBestellpositionDtoI);
			pruefePflichtfelderBelegposition(oBestellpositionDtoI, theClientDto);

			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(oBestellpositionDtoI.getBestellungIId(),
					theClientDto);

			if (oBestellpositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				// einen Handartikel anlegen
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(oBestellpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(oBestellpositionDtoI.getCZusatzbez());
				;

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(oBestellpositionDtoI.getEinheitCNr());

				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				if (oBestellpositionDtoI.getMwstsatzIId() != null) {
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(oBestellpositionDtoI.getMwstsatzIId(), theClientDto);
					oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				}

				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

				oBestellpositionDtoI.setArtikelIId(iIdArtikel);
			}

			// Artikellieferant einpflegen
			if (oBestellpositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				preispflege(oBestellpositionDtoI, sPreispflegeI, artikellieferantstaffelIId_ZuAendern, false,
						theClientDto);
			}

			// PJ18750
			if (oBestellpositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				preispflege(oBestellpositionDtoI,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN,
						artikellieferantstaffelIId_ZuAendern, true, theClientDto);
			}

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_BESTELLPOSITION);
			oBestellpositionDtoI.setIId(iId);

			// Sortierung: falls nicht anders definiert, hinten dran haengen.
			if (oBestellpositionDtoI.getISort() == null) {
				int iSortNeu = getMaxISort(oBestellpositionDtoI.getBelegIId()) + 1;
				oBestellpositionDtoI.setISort(iSortNeu);
			}

			// Status bei neuen ist "Offen".
			if (oBestellpositionDtoI.getBestellpositionstatusCNr() == null) {
				oBestellpositionDtoI.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
			}

			// setzen des liefertermins
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(oBestellpositionDtoI.getBestellungIId());
			if (oBestellpositionDtoI.getTUebersteuerterLiefertermin() == null) {
				oBestellpositionDtoI.setTUebersteuerterLiefertermin(bestellungDto.getDLiefertermin());
			}

			if (oBestellpositionDtoI.getBWepinfoAnAnforderer() == null) {
				oBestellpositionDtoI.setBWepinfoAnAnforderer(Helper.boolean2Short(false));
			}

/*			
			if (bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
					|| bestellungDto.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

				if (oBestellpositionDtoI.getPositionsartCNr()
						.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					ArrayList<GebindeDto> alGebinde = getArtikelFac().getGebindeEinesArtikelsUndEinesLieferanten(
							oBestellpositionDtoI.getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
							bestellungDto.getDBelegdatum(), theClientDto);
					if (alGebinde.size() > 0) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GEBINDE_IN_RAHMEN_UND_ABRUF_NICHT_MOEGLICH,
								new Exception("FEHLER_GEBINDE_IN_RAHMEN_UND_ABRUF_NICHT_MOEGLICH"));
					}
				}
			}
*/

			// Rahmen zuweisen

			bestellposition = new Bestellposition(oBestellpositionDtoI.getIId(),
					oBestellpositionDtoI.getBestellungIId(), oBestellpositionDtoI.getISort(),
					oBestellpositionDtoI.getPositionsartCNr(), oBestellpositionDtoI.getBestellpositionstatusCNr());
			setBestellpositionFromBestellpositionDto(bestellposition, oBestellpositionDtoI);
			// em.persist(bestellposition);
			// em.flush();

			// SP1666
			oBestellpositionDtoI.setNOffeneMenge(berechneOffeneMenge(bestellposition.getIId()));

			setBestellpositionFromBestellpositionDto(bestellposition, oBestellpositionDtoI);

			
			
			getBelegartmediaFac().kopiereBelegartmedia(oBestellpositionDtoI.getUsecaseIIdQuelle(),
					oBestellpositionDtoI.getIKeyQuelle(), QueryParameters.UC_ID_BESTELLPOSITION, oBestellpositionDtoI.getIId(),
					theClientDto);
			
			// PJ19376
			if (oBestellpositionDtoI.getNNettogesamtPreisminusRabatte() != null) {
				pauschalkorrekturZuruecknehmen(bestellungDto.getIId(), theClientDto);
			}

			
			befuelleZusaetzlichePreisfelder(iId);

			// Bestelltliste aktualsisieren
			getArtikelbestelltFac().aktualisiereBestelltListe(bestellposition.getIId(), theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikle ebenfalls
			// buchen:
			if (bArtikelSetAufloesen == true && oBestellpositionDtoI.getArtikelIId() != null) {

				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						oBestellpositionDtoI.getArtikelIId(), theClientDto);
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
					BestellpositionDto bestellpositionDtoKopfartikel = bestellpositionFindByPrimaryKey(
							oBestellpositionDtoI.getIId());

					List<?> m = null;
					try {
						m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stklDto.getIId(), theClientDto,
								StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, false, false,
								oBestellpositionDtoI.getNMenge(), null, true);
					} catch (RemoteException ex4) {
						throwEJBExceptionLPRespectOld(ex4);
					}

					Iterator<?> it = m.listIterator();

					while (it.hasNext()) {
						StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
						StuecklistepositionDto position = struktur.getStuecklistepositionDto();

						oBestellpositionDtoI.setNNettoeinzelpreis(new BigDecimal(0));
						oBestellpositionDtoI.setNRabattbetrag(new BigDecimal(0));

						oBestellpositionDtoI.setDRabattsatz(0D);
						oBestellpositionDtoI.setNNettogesamtpreis(new BigDecimal(0));

						oBestellpositionDtoI.setNMenge(Helper.rundeKaufmaennisch(
								position.getNZielmenge(bestellpositionDtoKopfartikel.getNMenge()),
								getMandantFac().getNachkommastellenMenge(theClientDto.getMandant())));

						oBestellpositionDtoI.setArtikelIId(position.getArtikelIId());
						oBestellpositionDtoI.setEinheitCNr(position.getEinheitCNr());
						oBestellpositionDtoI.setPositioniIdArtikelset(bestellpositionDtoKopfartikel.getIId());
						oBestellpositionDtoI.setIId(null);

						int iSortNeu = oBestellpositionDtoI.getISort() + 1;

						sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(oBestellpositionDtoI.getBelegIId(),
								iSortNeu);

						oBestellpositionDtoI.setISort(iSortNeu);
						createBestellposition(oBestellpositionDtoI, false, theClientDto, sPreispflegeI,
								artikellieferantstaffelIId_ZuAendern);

					}
					preiseEinesArtikelsetsUpdaten(bestellpositionDtoKopfartikel.getIId(), theClientDto);
				}

			}

			HvDtoLogger<BestellpositionDto> hvLogger = new HvDtoLogger<BestellpositionDto>(em,
					oBestellpositionDtoI.getBelegIId(), theClientDto);
			hvLogger.logInsert(oBestellpositionDtoI);

			// PJ21210
			preisGemeinsamerArtikelAngleichen(oBestellpositionDtoI, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return bestellposition.getIId();
	}

	private void preiseEinesArtikelsetsUpdaten(Integer positionIIdKopfartikel, TheClientDto theClientDto) {

		BestellpositionDto positionDtoKopfartikel = bestellpositionFindByPrimaryKey(positionIIdKopfartikel);

		Query query = em.createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
		query.setParameter(1, positionDtoKopfartikel.getIId());
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		try {
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(positionDtoKopfartikel.getBestellungIId());

			// Zuerst Gesamtwert berechnen
			BigDecimal bdMenge = positionDtoKopfartikel.getNMenge();

			BigDecimal bdNettoeinzelpreis = positionDtoKopfartikel.getNNettoeinzelpreis();

			BigDecimal bdGesamtwertposition = bdMenge.multiply(bdNettoeinzelpreis);

			BigDecimal bdGesamtVKwert = new BigDecimal(0);

			Iterator<?> it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Bestellposition struktur = (Bestellposition) it.next();

				ArtikellieferantDto artLief = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(struktur.getArtikelIId(),
								bestellungDto.getLieferantIIdBestelladresse(), bestellungDto.getWaehrungCNr(),
								theClientDto);

				if (artLief != null && artLief.getNNettopreis() != null) {
					BigDecimal bdEinzel = artLief.getNNettopreis().multiply(struktur.getNMenge());
					bdGesamtVKwert = bdGesamtVKwert.add(bdEinzel);
				}
			}

			bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);
			if (bdGesamtVKwert.signum() == 0)
				return;

			BigDecimal ekpFaktor = bdGesamtwertposition.divide(bdGesamtVKwert, 4, BigDecimal.ROUND_HALF_EVEN);
			it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Bestellposition struktur = (Bestellposition) it.next();

				struktur.setNNettoeinzelpreis(BigDecimal.ZERO);

				ArtikellieferantDto artLief = getArtikelFac()
						.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(struktur.getArtikelIId(),
								bestellungDto.getLieferantIIdBestelladresse(), bestellungDto.getWaehrungCNr(),
								theClientDto);

				if (artLief != null && artLief.getNNettopreis() != null) {
					// BigDecimal d =
					// artLief.getNNettopreis().multiply(struktur.getNMenge())
					// .multiply(ekpFaktor) ;
					// BigDecimal dNettoGesamtEins =
					// d.divide(bdMenge, 4, BigDecimal.ROUND_HALF_EVEN) ;
					//
					// struktur.setNNettoeinzelpreis(dNettoGesamtEins) ;
					// // Hack, damit der Bestellpositionnhandler den richtigen
					// Betrag darstellen kann.
					// struktur.setNNettogesamtpreis(
					// d.divide(struktur.getNMenge(), 4,
					// BigDecimal.ROUND_HALF_EVEN)) ;

					struktur.setNNettoeinzelpreis(artLief.getNNettopreis());
					BigDecimal liefPreis = artLief.getNNettopreis();
					BigDecimal ekPreis = artLief.getNNettopreis().multiply(ekpFaktor);
					BigDecimal rabatt = ekPreis.subtract(liefPreis).negate();
					struktur.setNRabattbetrag(rabatt);
					struktur.setNNettogesamtpreis(ekPreis);

					// struktur.setNNettoeinzelpreis(
					// d.divide(struktur.getNMenge(), 4,
					// BigDecimal.ROUND_HALF_EVEN)) ;
					// struktur.setNNettogesamtpreis(
					// Helper.rundeKaufmaennisch(d, 4)) ;

					// Preis berechnen
					// BigDecimal bdAnteilVKWert = artLief.getNNettopreis()
					// .multiply(struktur.getNMenge())
					// .divide(bdGesamtVKwert, 4,
					// BigDecimal.ROUND_HALF_EVEN);
					//
					// struktur.setNNettoeinzelpreis(bdGesamtwertposition
					// .multiply(bdAnteilVKWert).divide(
					// struktur.getNMenge().multiply(bdMenge), 4,
					// BigDecimal.ROUND_HALF_EVEN));
					//
					// struktur.setNNettogesamtpreis(bdGesamtwertposition
					// .multiply(bdAnteilVKWert).divide(
					// struktur.getNMenge(), 4,
					// BigDecimal.ROUND_HALF_EVEN));
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}
	}

	private void checkBestellpositionDto(BestellpositionDto oBestellspositionI) throws EJBExceptionLP {
		if (oBestellspositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("bestellpositionDtoI == null"));
		}
	}

	public void removeBestellposition(BestellpositionDto bestellpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {

			checkBestellpositionDto(bestellpositionDto);

			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellpositionDto.getBestellungIId(),
					theClientDto);

			if (bestellpositionDto.getPositioniIdArtikelset() == null) {

				Query query = em.createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
				query.setParameter(1, bestellpositionDto.getIId());
				Collection<?> angebotpositionDtos = query.getResultList();
				BestellpositionDto[] zugehoerigeBSPosDtos = assembleBestellpositionDtos(angebotpositionDtos);

				for (int i = 0; i < zugehoerigeBSPosDtos.length; i++) {
					removeBestellposition(zugehoerigeBSPosDtos[i], theClientDto);
				}
			}

			Bestellposition toRemove = em.find(Bestellposition.class, bestellpositionDto.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			HvDtoLogger<BestellpositionDto> hvLogger = new HvDtoLogger<BestellpositionDto>(em,
					bestellpositionDto.getBelegIId(), theClientDto);
			hvLogger.logDelete(bestellpositionDto);

			try {
				em.remove(toRemove);
				em.flush();

				if (bestellpositionDto.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(bestellpositionDto.getPositioniIdArtikelset(), theClientDto);
				}

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

			// die Sortierung muss angepasst werden
			sortierungAnpassenBeiLoeschenEinerPosition(bestellpositionDto.getBestellungIId(),
					bestellpositionDto.getISort().intValue());
			// Bestelltliste aktualsisiert

			if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				getArtikelbestelltFac().aktualisiereBestelltListe(bestellpositionDto.getIId(), theClientDto);
			}

			// refresh
			checkStatusAbrufBestellungenUndRahmenbestellung(bestellpositionDto, theClientDto);

			pauschalkorrekturZuruecknehmen(bestellpositionDto.getBestellungIId(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void updateBestellpositionOhneWeitereAktion(BestellpositionDto bestellpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		checkBestellpositionDto(bestellpositionDto);
		pruefePflichtfelderBelegposition(bestellpositionDto, theClientDto);
		// try {
		Bestellposition bestellposition = em.find(Bestellposition.class, bestellpositionDto.getIId());
		if (bestellposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellpositionDto = ABTerminAendernpruefenUndSetzen(bestellpositionDto, bestellposition, theClientDto);
		setBestellpositionFromBestellpositionDto(bestellposition, bestellpositionDto);
		befuelleZusaetzlichePreisfelder(bestellpositionDto.getIId());
		BestellungDto b = getBestellungFac().bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());
		getBestellungFac().updateBestellung(b, theClientDto, false);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Eine Bestellposition manuell auf vollstaendig geliefert setzen.
	 * 
	 * @param iIdBestellpositionI Integer
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	public void manuellAufVollstaendigGeliefertSetzen(Integer iIdBestellpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdBestellpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}
		try {
			Bestellposition oPos = em.find(Bestellposition.class, iIdBestellpositionI);
			if (oPos == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// Die Bestellposition darf noch nicht erledigt sein.
			if (!oPos.getBestellpositionstatusCNr().equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
				oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT);
				oPos.setTManuellvollstaendiggeliefert(getTimestamp());
				oPos.setNOffenemenge(new BigDecimal(0));
				// Bestelltliste aktualisieren.
				getArtikelbestelltFac().aktualisiereBestelltListe(iIdBestellpositionI, theClientDto);
				// vielleicht kann die Bestellposition sogar schon ganz erledigt
				// werden.
				versucheBestellpositionAufErledigtZuSetzen(iIdBestellpositionI, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Bestellposition auf erledigt setzen. Dies ist nur dann erlaubt, wenn die
	 * Position den Status "Geliefert" hat, bzw. die offene Menge 0 betraegt.
	 * Ausserdem muessen alle Preise der Wareneingangspositionen erfasst sein.
	 * 
	 * @param iIdBestellpositionI Integer
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	public void versucheBestellpositionAufErledigtZuSetzen(Integer iIdBestellpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdBestellpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}
		try {
			boolean bDarfErledigen = true;
			boolean bDokumentVorhanden = true;
			boolean bNochWasOffen = false;
			String textFehlermeldungDokumente = "";
			Bestellposition oPos = em.find(Bestellposition.class, iIdBestellpositionI);
			if (oPos == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// offene Menge und Bestelltliste aktualisieren
			oPos.setNOffenemenge(berechneOffeneMenge(oPos.getIId()));
			getArtikelbestelltFac().aktualisiereBestelltListe(oPos.getIId(), theClientDto);

			// Wenn sie nicht eh schon erledigt ist.
			// Mengen/Preis behaftete Positionen muessen schon vollstaendig
			// geliefert sein.
			if (oPos.getBestellpositionartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
					|| oPos.getBestellpositionartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				// wenn es noch eine offene Menge gibt, dann darf ich nicht.
				if (oPos.getNOffenemenge().compareTo(new BigDecimal(0)) > 0) {
					bDarfErledigen = false;
					bNochWasOffen = true;
				}
				// bis jetzt ok.
				if (bDarfErledigen) {

					// Schaun, ob bereits alle Preise erfasst wurden.
					WareneingangspositionDto[] wePosDtos = getWareneingangFac()
							.wareneingangspositionFindByBestellpositionIId(iIdBestellpositionI);
					for (int i = 0; i < wePosDtos.length; i++) {

						// PJ14773 Schaun, ob bei dokumentenpflichtigen Artikeln
						// auch wirklich Dokumente hinterlegt sind

						ArtikelDto artikelDtoSmall = getArtikelFac().artikelFindByPrimaryKeySmall(oPos.getArtikelIId(),
								theClientDto);

						if (Helper.short2boolean(artikelDtoSmall.getBDokumentenpflicht())) {

							PrintInfoDto o = getJCRDocFac().getPathAndPartnerAndTable(wePosDtos[i].getIId(),
									QueryParameters.UC_ID_BESTELLUNGWAREINEINGANGSPOSITIONEN, theClientDto);

							if (o != null && o.getDocPath() != null) {
								try {
									ArrayList<JCRDocDto> al = getJCRDocFac()
											.getJCRDocDtoFromNodeChildren(o.getDocPath());
									if (al == null || al.size() == 0) {
										bDokumentVorhanden = false;
										WareneingangDto weDto = getWareneingangFac()
												.wareneingangFindByPrimaryKey(wePosDtos[i].getWareneingangIId());
										textFehlermeldungDokumente += Helper.formatDatum(weDto.getTWareneingangsdatum(),
												theClientDto.getLocUi()) + ", " + weDto.getCLieferscheinnr() + "\r\n";
									}

								} catch (Exception t) {
									t.printStackTrace();
									bDokumentVorhanden = false;
								}
							} else {
								bDokumentVorhanden = false;
							}

						}

						if (!wePosDtos[i].getBPreiseErfasst()) {
							bDarfErledigen = false;
						}
					}
				}
			}
			// Status auf Erledigt setzen.
			if (bDarfErledigen) {

				if (bDokumentVorhanden == false) {
					ArrayList al = new ArrayList();
					al.add(textFehlermeldungDokumente);

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT, al,
							new Exception("FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT"));
				}

				oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT);
			} else {
				if (bNochWasOffen) {
					if (oPos.getTAuftragsbestaetigungstermin() != null) {
						oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT);
					} else {
						oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					}
				} else {
					oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT);
				}
			}

			// Zugehoerige SET-Positionen auf denselben Status setzen
			List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em, iIdBestellpositionI);
			for (Bestellposition pos : positions) {
				// offene Menge und Bestelltliste aktualisieren
				pos.setNOffenemenge(berechneOffeneMenge(pos.getIId()));
				getArtikelbestelltFac().aktualisiereBestelltListe(pos.getIId(), theClientDto);
				pos.setBestellpositionstatusCNr(oPos.getBestellpositionstatusCNr());
			}

			// Query query = em
			// .createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
			// query.setParameter(1, iIdBestellpositionI);
			// Collection<Bestellposition> bestellPositionen = query
			// .getResultList();
			//
			// Iterator<Bestellposition> it = bestellPositionen.iterator();
			//
			// while (it.hasNext()) {
			// Bestellposition pos = it.next();
			// // offene Menge und Bestelltliste aktualisieren
			// pos.setNOffenemenge(berechneOffeneMenge(pos.getIId()));
			// getArtikelbestelltFac().aktualisiereBestelltListe(pos.getIId(),
			// theClientDto);
			// pos.setBestellpositionstatusCNr(oPos
			// .getBestellpositionstatusCNr());
			// }

			getBestellungFac().versucheBestellungAufErledigtZuSetzen(oPos.getBestellungIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Die (manuelle) Erledigung einer Bestellposition aufheben. Das ist nur
	 * zulaessig, wenn die Bestellung selbst noch nicht erledigt ist.
	 * 
	 * @param iIdBestellpositionI Integer
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	public void manuellErledigungAufheben(Integer iIdBestellpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdBestellpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}
		Bestellposition oPos = em.find(Bestellposition.class, iIdBestellpositionI);
		if (oPos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// wenn die Position manuell auf vollstaendig geliefert gesetzt wurde.
		if (oPos.getTManuellvollstaendiggeliefert() != null) {
			if (oPos.getTAuftragsbestaetigungstermin() != null) {
				oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT);
			} else {
				oPos.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
			}
			oPos.setNOffenemenge(oPos.getNMenge());
			oPos.setTManuellvollstaendiggeliefert(null);
			versucheBestellpositionAufErledigtZuSetzen(oPos.getIId(), theClientDto);
		}

		// SP6408
		try {
			getArtikelbestelltFac().aktualisiereBestelltListe(iIdBestellpositionI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private BestellpositionDto ABTerminAendernpruefenUndSetzen(BestellpositionDto besposDto, Bestellposition besPos,
			TheClientDto theClientDto) {
		if (besposDto.getTAuftragsbestaetigungstermin() != null) {
			if (!besposDto.getTAuftragsbestaetigungstermin().equals(besPos.getTAuftragsbestaetigungstermin())) {
				besposDto.setTAbterminAendern(getTimestamp());
				besposDto.setPersonalIIdAbterminAendern(theClientDto.getIDPersonal());
			}
		}
		return besposDto;
	}

	private void pauschalkorrekturZuruecknehmen(Integer bestellungIId, TheClientDto theClientDto) {

		Bestellung bes = em.find(Bestellung.class, bestellungIId);
		bes.setNKorrekturbetrag(null);
		em.merge(bes);
		em.flush();
		if (bes.getNBestellwert() != null) {
			try {
				bes.setNBestellwert(getBestellungFac().berechneNettowertGesamt(bes.getIId(), theClientDto));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			em.merge(bes);
			em.flush();
		}
	}

	private void updateBestellposition(BestellpositionDto bestellpositionDto, TheClientDto theClientDto,
			String preispflegeCnr, Integer artikellieferantstaffelIdZuAendern, boolean artikelsetAufloesen,
			boolean gleicheArtikelAnpassen) throws EJBExceptionLP {
		myLogger.warn("em hash=" + em.hashCode() + ", " + em.toString());
		pruefePflichtfelderBelegposition(bestellpositionDto, theClientDto);
		Integer iId = bestellpositionDto.getIId();
		try {
			Bestellposition bestellposition = em.find(Bestellposition.class, iId);
			if (bestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, iId.toString());
			}

			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellpositionDto.getBestellungIId(),
					theClientDto);

			// PJ19376
			if (bestellposition.getNNettogesamtpreis() != null && bestellpositionDto.getNNettogesamtpreis() != null
					&& bestellposition.getNNettogesamtpreis().doubleValue() != bestellpositionDto.getNNettogesamtpreis()
							.doubleValue()) {
				pauschalkorrekturZuruecknehmen(bestellpositionDto.getBestellungIId(), theClientDto);
			}

			// SK Die offenen Mengen muessen korregiert werden
			if (bestellpositionDto.getNMenge() != null && bestellpositionDto.getNOffeneMenge() != null) {
				BigDecimal bdGeliefert = bestellposition.getNMenge().subtract(bestellposition.getNOffenemenge());
				if (bestellpositionDto.getNMenge() != null) {
					bestellpositionDto.setNOffeneMenge(bestellpositionDto.getNMenge().subtract(bdGeliefert));
				}
			}

			if (bestellposition.getArtikelIId() != null && bestellpositionDto.getArtikelIId() != null
					&& !bestellposition.getArtikelIId().equals(bestellpositionDto.getArtikelIId())) {
				// Wenn WE-Pos vorhanden, dann darf Artikel nicht mehr geaendert
				// werden

				WareneingangspositionDto[] dtos = getWareneingangFac()
						.wareneingangspositionFindByBestellpositionIId(bestellpositionDto.getIId());

				if (dtos != null && dtos.length > 0) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
							new Exception("FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN"));
				}
			}

			bestellpositionDto = ABTerminAendernpruefenUndSetzen(bestellpositionDto, bestellposition, theClientDto);

			BestellpositionDto bestellpositionDto_vorher = bestellpositionFindByPrimaryKey(bestellpositionDto.getIId());
			HvDtoLogger<BestellpositionDto> logger = new HvDtoLogger<BestellpositionDto>(em,
					bestellpositionDto.getBelegIId(), theClientDto);
			logger.log(bestellpositionDto_vorher, bestellpositionDto);

			setBestellpositionFromBestellpositionDto(bestellposition, bestellpositionDto);

			befuelleZusaetzlichePreisfelder(iId);

			if (preispflegeCnr != null) {
				if (bestellpositionDto.getPositionsartCNr()
						.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
					preispflege(bestellpositionDto, preispflegeCnr, artikellieferantstaffelIdZuAendern, false,
							theClientDto);
				}
			}

			if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				Integer artikelIId = bestellpositionDto.getArtikelIId();

				if (artikelIId != null) {
					ArtikelFac artikelFac = getArtikelFac();
					ArtikelDto artikelDto = artikelFac.artikelFindByPrimaryKey(artikelIId, theClientDto);
					ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
					artikelsprDto.setCBez(bestellpositionDto.getCBez());
					artikelsprDto.setCZbez(bestellpositionDto.getCZusatzbez());

					artikelDto.setEinheitCNr(bestellpositionDto.getEinheitCNr());
					// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(bestellpositionDto.getMwstsatzIId(), theClientDto);
					artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
					artikelFac.updateArtikel(artikelDto, theClientDto);
				} else {
					// einen Handartikel anlegen
					ArtikelDto oArtikelDto = new ArtikelDto();
					oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

					ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
					oArtikelsprDto.setCBez(bestellpositionDto.getCBez());
					oArtikelsprDto.setCZbez(bestellpositionDto.getCZusatzbez());

					oArtikelDto.setArtikelsprDto(oArtikelsprDto);
					oArtikelDto.setEinheitCNr(bestellpositionDto.getEinheitCNr());

					// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
					if (bestellpositionDto.getMwstsatzIId() != null) {
						MwstsatzDto mwstsatzDto = getMandantFac()
								.mwstsatzFindByPrimaryKey(bestellpositionDto.getMwstsatzIId(), theClientDto);
						oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
					}

					Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

					bestellposition.setArtikelIId(iIdArtikel);
					bestellpositionDto.setArtikelIId(iIdArtikel);
				}
			}
			// Bestelltliste aktualisieren.
			getArtikelbestelltFac().aktualisiereBestelltListe(bestellposition.getIId(), theClientDto);

			if (artikelsetAufloesen) {
				if (bestellpositionDto.getArtikelIId() != null
						&& bestellpositionDto.getPositioniIdArtikelset() == null) {
					int nkStellen = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
					BigDecimal korrekturmenge = bestellpositionDto_vorher.getNMenge()
							.subtract(bestellpositionDto.getNMenge()).negate();
					BigDecimal headAmount = bestellpositionDto_vorher.getNMenge();
					List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em,
							bestellpositionDto.getIId());
					for (Bestellposition pos : positions) {
						BestellpositionDto posDto = assembleBestellpositionDto(pos);
						BigDecimal menge = posDto.getNMenge().add(korrekturmenge.multiply(posDto.getNMenge())
								.divide(headAmount, nkStellen, RoundingMode.HALF_EVEN));
						posDto.setNMenge(menge);
						BigDecimal offeneMenge = posDto.getNOffeneMenge()
								.add(korrekturmenge.multiply(posDto.getNOffeneMenge()).divide(headAmount, nkStellen,
										RoundingMode.HALF_EVEN));
						posDto.setNOffeneMenge(offeneMenge);
						updateBestellposition(posDto, theClientDto, preispflegeCnr, artikellieferantstaffelIdZuAendern,
								false, false);
					}
				}

				Integer positionIIdKopfartikel = gehoertZuArtikelset(bestellpositionDto.getIId());
				// Wenn Teil eines Artikelsets, dann muessen die Preise neu
				// berechnet werden

				if (positionIIdKopfartikel != null) {
					preiseEinesArtikelsetsUpdaten(positionIIdKopfartikel, theClientDto);
				}
			}

			// SP5182 Wenn Bezug zu Auftrag in anderem Mandanten, dann Termin

			// und Menge neu setzen und Auftragswert neu berechnen
			AuftragDto aDto = getAuftragFac()
					.istAuftragBeiAnderemMandantenHinterlegt(bestellpositionDto.getBestellungIId());
			if (aDto != null) {

				Query query = em.createNamedQuery("AuftragpositionfindByBestellpositionIId");
				query.setParameter(1, bestellpositionDto.getIId());
				Collection<?> cl = query.getResultList();

				if (cl.size() > 0) {

					Auftragposition ap = (Auftragposition) cl.iterator().next();

					AuftragpositionDto zugehoerigeAuftragspositionDto = getAuftragpositionFac()
							.auftragpositionFindByPrimaryKey(ap.getIId());

					if (zugehoerigeAuftragspositionDto.getArtikelIId() != null
							&& zugehoerigeAuftragspositionDto.getNMenge() != null) {

						if (bestellpositionDto.getNMenge() != null
								&& bestellpositionDto.getTUebersteuerterLiefertermin() != null) {
							zugehoerigeAuftragspositionDto.setNMenge(bestellpositionDto.getNMenge());

							zugehoerigeAuftragspositionDto.setTUebersteuerbarerLiefertermin(
									bestellpositionDto.getTUebersteuerterLiefertermin());
							TheClientDto theClientDto_Zielmandant = null;

							MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(aDto.getMandantCNr(),
									theClientDto);

							try {
								/*
								 * theClientDto_Zielmandant = getLogonFac().logon(
								 * Helper.getFullUsername(sUser), Helper.getMD5Hash((sUser + sPassword)
								 * .toCharArray()), Helper.string2Locale(mandantDto .getPartnerDto()
								 * .getLocaleCNrKommunikation()), aDto.getMandantCNr(), new Timestamp(System
								 * .currentTimeMillis()));
								 */
								theClientDto_Zielmandant = getLogonFac().logonIntern(
										Helper.string2Locale(mandantDto.getPartnerDto().getLocaleCNrKommunikation()),
										aDto.getMandantCNr());

							} catch (EJBExceptionLP e1) {
								// Meldung bringen
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT,
										new Exception("FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT"));

							}

							getAuftragpositionFac().updateAuftragposition(zugehoerigeAuftragspositionDto,
									theClientDto_Zielmandant);

							getAuftragFac().berechneBelegControlled(zugehoerigeAuftragspositionDto.getBelegIId(),
									theClientDto_Zielmandant);

							Bestellposition bestellpositionAB_Termin = em.find(Bestellposition.class,
									bestellpositionDto.getIId());

							bestellpositionAB_Termin.setTAuftragsbestaetigungstermin(
									new java.sql.Date(bestellpositionDto.getTUebersteuerterLiefertermin().getTime()));
						}

					}

				}
			}
			// PJ21210
			if (gleicheArtikelAnpassen) {
				preisGemeinsamerArtikelAngleichen(bestellpositionDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateBestellposition(BestellpositionDto bestellpositionDto, TheClientDto theClientDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern) throws EJBExceptionLP {
		checkBestellpositionDto(bestellpositionDto);
		updateBestellposition(bestellpositionDto, theClientDto, sPreispflegeI, artikellieferantstaffelIId_ZuAendern,
				true, true);
		// pruefePflichtfelderBelegposition(bestellpositionDto, theClientDto);
		// Integer iId = bestellpositionDto.getIId();
		// try {
		// getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(
		// bestellpositionDto.getBestellungIId(), theClientDto);
		//
		// Bestellposition bestellposition = em.find(Bestellposition.class,
		// iId);
		// if (bestellposition == null) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		// }
		//
		// // PJ19376
		// if (bestellposition.getNNettogesamtpreis() != null
		// && bestellpositionDto.getNNettogesamtpreis() != null
		// && bestellposition.getNNettogesamtpreis().doubleValue() !=
		// bestellpositionDto
		// .getNNettogesamtpreis().doubleValue()) {
		// pauschalkorrekturZuruecknehmen(
		// bestellpositionDto.getBestellungIId(), theClientDto);
		// }
		//
		// // SK Die offenen Mengen muessen korregiert werden
		// if (bestellpositionDto.getNMenge() != null
		// && bestellpositionDto.getNOffeneMenge() != null) {
		// BigDecimal bdGeliefert = bestellposition.getNMenge().subtract(
		// bestellposition.getNOffenemenge());
		// if (bestellpositionDto.getNMenge() != null) {
		// bestellpositionDto.setNOffeneMenge(bestellpositionDto
		// .getNMenge().subtract(bdGeliefert));
		// }
		// }
		//
		// if (bestellposition.getArtikelIId() != null
		// && bestellpositionDto.getArtikelIId() != null
		// && !bestellposition.getArtikelIId().equals(
		// bestellpositionDto.getArtikelIId())) {
		// // Wenn WE-Pos vorhanden, dann darf Artikel nicht mehr geaendert
		// // werden
		//
		// WareneingangspositionDto[] dtos = getWareneingangFac()
		// .wareneingangspositionFindByBestellpositionIId(
		// bestellpositionDto.getIId());
		//
		// if (dtos != null && dtos.length > 0) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
		// new Exception(
		// "FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN"));
		// }
		//
		// }
		// bestellpositionDto = ABTerminAendernpruefenUndSetzen(
		// bestellpositionDto, bestellposition, theClientDto);
		//
		// BestellpositionDto bestellpositionDto_vorher =
		// bestellpositionFindByPrimaryKey(bestellpositionDto
		// .getIId());
		// HvDtoLogger<BestellpositionDto> logger = new
		// HvDtoLogger<BestellpositionDto>(
		// em, bestellpositionDto.getBelegIId(), theClientDto);
		// logger.log(bestellpositionDto_vorher, bestellpositionDto);
		//
		// setBestellpositionFromBestellpositionDto(bestellposition,
		// bestellpositionDto);
		//
		// befuelleZusaetzlichePreisfelder(iId);
		//
		// if (sPreispflegeI != null) {
		// if (bestellpositionDto.getPositionsartCNr().equalsIgnoreCase(
		// BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
		// preispflege(bestellpositionDto, sPreispflegeI,
		// artikellieferantstaffelIId_ZuAendern, false,
		// theClientDto);
		// }
		// }
		//
		// if (bestellpositionDto.getPositionsartCNr().equals(
		// BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
		// Integer artikelIId = bestellpositionDto.getArtikelIId();
		//
		// if (artikelIId != null) {
		// ArtikelFac artikelFac = getArtikelFac();
		// ArtikelDto artikelDto = artikelFac.artikelFindByPrimaryKey(
		// artikelIId, theClientDto);
		// ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
		// artikelsprDto.setCBez(bestellpositionDto.getCBez());
		// artikelsprDto.setCZbez(bestellpositionDto.getCZusatzbez());
		//
		// artikelDto
		// .setEinheitCNr(bestellpositionDto.getEinheitCNr());
		// // Der Artikel erhaelt die Mwst-Satz-Bezeichnung
		// MwstsatzDto mwstsatzDto = getMandantFac()
		// .mwstsatzFindByPrimaryKey(
		// bestellpositionDto.getMwstsatzIId(),
		// theClientDto);
		// artikelDto.setMwstsatzbezIId(mwstsatzDto
		// .getIIMwstsatzbezId());
		// artikelFac.updateArtikel(artikelDto, theClientDto);
		// } else {
		// // einen Handartikel anlegen
		// ArtikelDto oArtikelDto = new ArtikelDto();
		// oArtikelDto
		// .setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
		//
		// ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
		// oArtikelsprDto.setCBez(bestellpositionDto.getCBez());
		// oArtikelsprDto.setCZbez(bestellpositionDto.getCZusatzbez());
		// ;
		//
		// oArtikelDto.setArtikelsprDto(oArtikelsprDto);
		// oArtikelDto.setEinheitCNr(bestellpositionDto
		// .getEinheitCNr());
		//
		// // Der Artikel erhaelt die Mwst-Satz-Bezeichnung
		// if (bestellpositionDto.getMwstsatzIId() != null) {
		// MwstsatzDto mwstsatzDto = getMandantFac()
		// .mwstsatzFindByPrimaryKey(
		// bestellpositionDto.getMwstsatzIId(),
		// theClientDto);
		// oArtikelDto.setMwstsatzbezIId(mwstsatzDto
		// .getIIMwstsatzbezId());
		// }
		//
		// Integer iIdArtikel = getArtikelFac().createArtikel(
		// oArtikelDto, theClientDto);
		//
		// bestellposition.setArtikelIId(iIdArtikel);
		// bestellpositionDto.setArtikelIId(iIdArtikel);
		// }
		// }
		// // Bestelltliste aktualisieren.
		// getArtikelbestelltFac().aktualisiereBestelltListe(
		// bestellposition.getIId(), theClientDto);
		//
		// if(bestellpositionDto.getArtikelIId() != null
		// && bestellpositionDto.getPositioniIdArtikelset() == null) {
		// BigDecimal korrekturmenge =
		// bestellpositionDto_vorher.getNMenge().subtract(bestellpositionDto.getNMenge()).negate();
		// BigDecimal headAmount = bestellpositionDto_vorher.getNMenge();
		// List<Bestellposition> positions = BestellpositionQuery
		// .listByPositionIIdArtikelset(em, bestellpositionDto.getIId());
		// for (Bestellposition pos : positions) {
		// BestellpositionDto posDto = assembleBestellpositionDto(pos);
		// BigDecimal menge = posDto.getNMenge()
		// .add(korrekturmenge.multiply(posDto.getNMenge()).divide(headAmount));
		// posDto.setNMenge(menge);
		// BigDecimal offeneMenge = posDto.getNOffeneMenge()
		// .add(korrekturmenge.multiply(posDto.getNOffeneMenge()).divide(headAmount));
		// posDto.setNOffeneMenge(offeneMenge);
		// updateBestellposition(posDto, theClientDto, sPreispflegeI,
		// artikellieferantstaffelIId_ZuAendern);
		// }
		// }
		//
		//
		//
		// Integer positionIIdKopfartikel =
		// gehoertZuArtikelset(bestellpositionDto
		// .getIId());
		// // Wenn Teil eines Artikelsets, dann muessen die Preise neu
		// // berechnet werden
		//
		// if (positionIIdKopfartikel != null) {
		// preiseEinesArtikelsetsUpdaten(positionIIdKopfartikel,
		// theClientDto);
		//
		// }
		//
		// } catch (RemoteException ex) {
		// throwEJBExceptionLPRespectOld(ex);
		// }
		//
	}

	public boolean bIstKopfartikelEinesArtikelSets(Integer bestellpositionIId) {
		List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em, bestellpositionIId);
		return positions.size() > 0;
		// Query query = em
		// .createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
		// query.setParameter(1, bestellpositionIId);
		// Collection<?> lieferscheinpositionDtos = query.getResultList();
		//
		// if (lieferscheinpositionDtos.size() > 0) {
		// return true;
		// } else {
		// return false;
		// }
	}

	public Integer gehoertZuArtikelset(Integer bestellpositionIId) {

		Bestellposition oPosition1 = em.find(Bestellposition.class, bestellpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return oPosition1.getPositionIIdArtikelset();
		}

		List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em, bestellpositionIId);
		return positions.size() > 0 ? bestellpositionIId : null;

		// Query query = em
		// .createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
		// query.setParameter(1, bestellpositionIId);
		// Collection<?> lieferscheinpositionDtos = query.getResultList();
		// BestellpositionDto[] zugehoerigeREPosDtos =
		// assembleBestellpositionDtos(lieferscheinpositionDtos);
		//
		// if (zugehoerigeREPosDtos != null && zugehoerigeREPosDtos.length > 0)
		// {
		// return bestellpositionIId;
		// } else {
		// return null;
		// }
		//
	}

	public void updateBestellpositions(BestellpositionDto[] bestellpositionDtos, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (bestellpositionDtos != null) {
			for (int i = 0; i < bestellpositionDtos.length; i++) {
				updateBestellposition(bestellpositionDtos[i], theClientDto,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
				;
			}
		}
	}

	private ArtikellieferantDto getArtikellieferant(Integer ArtikelIId, Integer LieferantIId, BigDecimal nMenge,
			String sWaehrungLF, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId, TheClientDto theClientDto) {
		ArtikellieferantDto artLiefDto = null;
		try {
			artLiefDto = getArtikelFac().getArtikelEinkaufspreisMitOptionGebinde(ArtikelIId, LieferantIId, nMenge,
					sWaehrungLF, tDatumPreisgueltigkeit, gebindeIId, theClientDto);
		}

		catch (Exception ex) {

		}
		return artLiefDto;

	}

	public void preispflege(BestellpositionDto besPosDto, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern, boolean bNullPreiseZurueckpflegen,
			TheClientDto theClientDto) {
		preispflege(besPosDto, sPreispflegeI, artikellieferantstaffelIId_ZuAendern, bNullPreiseZurueckpflegen, false, theClientDto);
	}
	
	public void preispflege(BestellpositionDto besPosDto, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern, boolean bNullPreiseZurueckpflegen,boolean zuschnittsartikelNeuBerechnen,
			TheClientDto theClientDto) {
		try {
			if (sPreispflegeI != null
					&& !sPreispflegeI.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT)) {

				// SP3305
				if (bNullPreiseZurueckpflegen == false) {

					if (besPosDto.getNNettogesamtpreis() == null
							|| besPosDto.getNNettogesamtpreis().doubleValue() == 0) {
						return;
					}

				}

				BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(besPosDto.getBestellungIId());

				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(besDto.getLieferantIIdBestelladresse(), theClientDto);

				// vonwaehrung BS
				String sWaehrungBS = besDto.getWaehrungCNr();
				// zielwaehrung LF
				String sWaehrungLF = lieferantDto.getWaehrungCNr();

				// Naqch LFWaehrung umrechnung
				besPosDto.setNNettogesamtpreis(
						getLocaleFac().rechneUmInAndereWaehrungZuDatum(besPosDto.getNNettogesamtpreis(), sWaehrungBS,
								sWaehrungLF, besDto.getDBelegdatum(), theClientDto));

				// einzelpreis: von BS nach LF-Waehrung umrechnen
				besPosDto.setNNettoeinzelpreis(
						getLocaleFac().rechneUmInAndereWaehrungZuDatum(besPosDto.getNNettoeinzelpreis(), sWaehrungBS,
								sWaehrungLF, besDto.getDBelegdatum(), theClientDto));
				// von BS nach LF-Waehrung umrechnen
				if (besPosDto.getNFixkosten() != null) {
					besPosDto.setNFixkosten(getLocaleFac().rechneUmInAndereWaehrungZuDatum(besPosDto.getNFixkosten(),
							sWaehrungBS, sWaehrungLF, besDto.getDBelegdatum(), theClientDto));
				}

				if (sPreispflegeI.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN)) {
					besPosDto.setfMindestbestellmenge_NOT_IN_DB(null);
					besPosDto.setNFixkosten(null);
				}

				ArtikellieferantDto artLiefDto = null;

				if (sPreispflegeI.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN)
						|| sPreispflegeI
								.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN)) {

					artLiefDto = getArtikellieferant(besPosDto.getArtikelIId(), besDto.getLieferantIIdBestelladresse(),
							besPosDto.getNMenge(), sWaehrungLF, besDto.getDBelegdatum(), besPosDto.getGebindeIId(),
							theClientDto);
					//
					if (artLiefDto == null) {
						artLiefDto = getArtikelFac()
								.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
										besPosDto.getArtikelIId(), besDto.getLieferantIIdBestelladresse(),
										besDto.getDBelegdatum(), besPosDto.getGebindeIId(), theClientDto);
					}
					// anlegen
					if (artLiefDto == null) {
						artLiefDto = new ArtikellieferantDto();
						artLiefDto.setArtikelIId(besPosDto.getArtikelIId());
						artLiefDto.setLieferantIId(besDto.getLieferantIIdBestelladresse());
						artLiefDto.setMandantCNr(besDto.getMandantCNr());
						artLiefDto.setTPreisgueltigab(new java.sql.Timestamp(besDto.getDBelegdatum().getTime()));
						artLiefDto.setBHerstellerbez(Helper.boolean2Short(false));

						// PJ20154
						if (sPreispflegeI
								.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN)) {

							artLiefDto.setNEinzelpreis(null);
							artLiefDto.setFRabatt(null);
							artLiefDto.setNNettopreis(null);

						} else {
							if (besPosDto.getNNettoeinzelpreis() != null) {
								artLiefDto.setNEinzelpreis(besPosDto.getNNettoeinzelpreis());
							} else {
								artLiefDto.setNEinzelpreis(new BigDecimal(0));
							}
							if (besPosDto.getDRabattsatz() != null) {
								artLiefDto.setFRabatt(besPosDto.getDRabattsatz());
							} else {
								artLiefDto.setFRabatt(new Double(0));
							}
							if (besPosDto.getNNettogesamtpreis() != null) {
								artLiefDto.setNNettopreis(besPosDto.getNNettogesamtpreis());
							} else {
								artLiefDto.setNNettopreis(new BigDecimal(0));
							}
						}

						if (besPosDto.getNFixkosten() != null) {
							artLiefDto.setNFixkosten(besPosDto.getNFixkosten());
						} else {
							artLiefDto.setNFixkosten(new BigDecimal(0));
						}

						if (besPosDto.getGebindeIId() != null) {
							artLiefDto.setGebindeIId(besPosDto.getGebindeIId());

							GebindeDto gebindeDto = getArtikelFac().gebindeFindByPrimaryKey(besPosDto.getGebindeIId());
							artLiefDto.setNGebindemenge(gebindeDto.getNMenge());
						}

						if (Helper.short2boolean(besPosDto.getBNettopreisuebersteuert())) {
							artLiefDto.setBRabattbehalten(Helper.boolean2Short(false));
						} else {
							artLiefDto.setBRabattbehalten(Helper.boolean2Short(true));
						}

						Integer iId = getArtikelFac().createArtikellieferant(artLiefDto, theClientDto);
						artLiefDto = getArtikelFac().artikellieferantFindByPrimaryKey(iId, theClientDto);
					}

					if (sPreispflegeI.equals(BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN)) {

						if (artLiefDto.getNEinzelpreis() != null) {

							updateEinzelpreis(artLiefDto, besPosDto, besDto, artLiefDto.getNNettopreis(),zuschnittsartikelNeuBerechnen, theClientDto);

						} else {

							if (artLiefDto.getIId() != null) {
								updateEinzelpreis(artLiefDto, besPosDto, besDto, artLiefDto.getNNettopreis(),zuschnittsartikelNeuBerechnen,
										theClientDto);
							} else {

								einzelpreisAnlegen(artLiefDto, besPosDto, besDto, zuschnittsartikelNeuBerechnen, theClientDto);
							}
						}
						
						
						
						

					} else {

						// PJ18036
						if (besPosDto.getnVerpackungseinheit_NOT_IN_DB() != null) {
							Artikellieferant artikellieferant = em.find(Artikellieferant.class, artLiefDto.getIId());
							artikellieferant.setNVerpackungseinheit(besPosDto.getnVerpackungseinheit_NOT_IN_DB());
						}

						if (artikellieferantstaffelIId_ZuAendern != null) {
							updateStaffel(artikellieferantstaffelIId_ZuAendern, artLiefDto, besPosDto, theClientDto);
						} else {
							// SP988
							try {

								Query query = em.createNamedQuery(
										"ArtikellieferantstaffelfindByArtikellieferantIIdFMengeTPreisgueltigab");
								query.setParameter(1, artLiefDto.getIId());
								query.setParameter(2, besPosDto.getNMenge());
								query.setParameter(3, artLiefDto.getTPreisgueltigab());

								Artikellieferantstaffel bereitsvorhanden = (Artikellieferantstaffel) query
										.getSingleResult();

								updateStaffel(bereitsvorhanden.getIId(), artLiefDto, besPosDto, theClientDto);

							} catch (NoResultException ex) {
								neueStaffel(artLiefDto, besPosDto, theClientDto);
							}

						}

					}

				} else {
					throw new EJBExceptionLP(new Exception("sPreispflegeI?"));
				}

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void updateStaffel(Integer artikellieferantstaffelIId_ZuAendern, ArtikellieferantDto artikellieferantDto,
			BestellpositionDto bestellpositionDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		try {

			if (artikellieferantstaffelIId_ZuAendern != null) {
				ArtikellieferantstaffelDto artikellieferantstaffelDto = getArtikelFac()
						.artikellieferantstaffelFindByPrimaryKey(artikellieferantstaffelIId_ZuAendern);

				artikellieferantstaffelDto
						.setNNettopreis(Helper.rundeKaufmaennisch(bestellpositionDtoI.getNNettogesamtpreis(), 4));

				// Rabattsatz muss neu berechnet werden
				// Rabattsatz muss immer neu gerechnet werden
				if (artikellieferantDto.getNEinzelpreis() != null) {

					BigDecimal bdRabattsumme = artikellieferantDto.getNEinzelpreis()
							.subtract(bestellpositionDtoI.getNNettogesamtpreis());

					BigDecimal bdRabattsatz = new BigDecimal(0);
					if (artikellieferantDto.getNEinzelpreis().doubleValue() != 0) {
						bdRabattsatz = bdRabattsumme
								.divide(artikellieferantDto.getNEinzelpreis(), 4, BigDecimal.ROUND_HALF_EVEN)
								.movePointRight(2);

					}

					artikellieferantstaffelDto.setFRabatt(bdRabattsatz.doubleValue());
				}

				getArtikelFac().updateArtikellieferantstaffel(artikellieferantstaffelDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private ArtikellieferantDto updateEinzelpreis(ArtikellieferantDto artikellieferantDtoI,
			BestellpositionDto bestellpositionDtoI, BestellungDto bestellungDto, BigDecimal bdNettopreisI,boolean zuschnittsartikelNeuBerechnen,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			
			
			int iNachkommastellenPreis = 4;
			try {
				iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			artikellieferantDtoI
					.setNNettopreis(Helper.rundeKaufmaennisch(bestellpositionDtoI.getNNettogesamtpreis(), iNachkommastellenPreis));
			artikellieferantDtoI.setFRabatt(bestellpositionDtoI.getDRabattsatz());
			artikellieferantDtoI
					.setNEinzelpreis(Helper.rundeKaufmaennisch(bestellpositionDtoI.getNNettoeinzelpreis(), iNachkommastellenPreis));

			artikellieferantDtoI.setTPreisgueltigab(
					Helper.cutTimestamp(new java.sql.Timestamp(bestellungDto.getDBelegdatum().getTime())));

			if (bestellpositionDtoI.getNFixkosten() != null) {
				artikellieferantDtoI.setNFixkosten(bestellpositionDtoI.getNFixkosten());
			}
			if (bestellpositionDtoI.getfMindestbestellmenge_NOT_IN_DB() != null) {
				artikellieferantDtoI.setFMindestbestelmenge(bestellpositionDtoI.getfMindestbestellmenge_NOT_IN_DB());
			}
			if (bestellpositionDtoI.getnVerpackungseinheit_NOT_IN_DB() != null) {
				artikellieferantDtoI.setNVerpackungseinheit(bestellpositionDtoI.getnVerpackungseinheit_NOT_IN_DB());
			}

			getArtikelFac().updateArtikellieferantImpl(artikellieferantDtoI,zuschnittsartikelNeuBerechnen, theClientDto);

			return getArtikelFac().artikellieferantFindByPrimaryKey(artikellieferantDtoI.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public Map getListeDerVerknuepftenBestellungen(Integer lossollmaterialIId, TheClientDto theClientDto) {
		Map m = new TreeMap();

		Query queryBestpos = em.createNamedQuery("BestellpositionfindByLossollmaterialIId");
		queryBestpos.setParameter(1, lossollmaterialIId);
		Collection<Bestellposition> cl = queryBestpos.getResultList();

		Iterator<Bestellposition> itBest = cl.iterator();
		while (itBest.hasNext()) {
			Bestellposition b = itBest.next();

			BestellungDto besDto;
			try {
				besDto = getBestellungFac().bestellungFindByPrimaryKey(b.getBestellungIId());

				String menge = " ";

				if (b.getNMenge() != null) {

					menge += Helper.fitString2LengthAlignRight(
							Helper.formatZahl(b.getNMenge(), 2, theClientDto.getLocUi()), 12, ' ');
				}

				LieferantDto lDto = getLieferantFac().lieferantFindByPrimaryKey(besDto.getLieferantIIdBestelladresse(),
						theClientDto);
				String kbezLief = "";
				if (lDto.getPartnerDto().getCKbez() != null) {
					kbezLief = lDto.getPartnerDto().getCKbez();
				}

				String artikelnummer = "";

				if (b.getArtikelIId() != null) {
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(b.getArtikelIId(), theClientDto);
					artikelnummer = aDto.getCNr();
				}

				m.put(b.getIId(),
						besDto.getCNr() + " " + Helper.fitString2Length(kbezLief, 15, ' ') + menge + " "
								+ Helper.fitString2Length(artikelnummer, 23, ' ') + " "
								+ Helper.formatDatum(b.getTUebersteuerterliefertermin(), theClientDto.getLocUi()) + " "
								+ Helper.formatDatum(b.getTAuftragsbestaetigungstermin(), theClientDto.getLocUi()));
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		return m;
	}

	/**
	 * Einzelpreis anlegen
	 * 
	 * @param artikellieferantDto ArtikellieferantDto
	 * @param bestellpositionDto  BestellpositionDto
	 * @param bestellungDto       BestellungDto
	 * @param sWaehrungBS         String
	 * @param sWaehrungLF         String
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	private ArtikellieferantDto einzelpreisAnlegen(ArtikellieferantDto artikellieferantDto,
			BestellpositionDto bestellpositionDto, BestellungDto bestellungDto,boolean zuschnittsartikelNeuBerechnen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// Artikellieferant anlegen
			artikellieferantDto = new ArtikellieferantDto();

			artikellieferantDto.setArtikelIId(bestellpositionDto.getArtikelIId());
			artikellieferantDto.setLieferantIId(bestellungDto.getLieferantIIdBestelladresse());

			artikellieferantDto.setFRabatt(bestellpositionDto.getDRabattsatz());

			if (Helper.short2boolean(bestellpositionDto.getBNettopreisuebersteuert())) {
				artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(false));
			} else {
				artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(true));
			}
			if (bestellpositionDto.getNFixkosten() != null) {
				artikellieferantDto.setNFixkosten(bestellpositionDto.getNFixkosten());
			}
			if (bestellpositionDto.getfMindestbestellmenge_NOT_IN_DB() != null) {
				artikellieferantDto.setFMindestbestelmenge(bestellpositionDto.getfMindestbestellmenge_NOT_IN_DB());
			}
			if (bestellpositionDto.getnVerpackungseinheit_NOT_IN_DB() != null) {
				artikellieferantDto.setNVerpackungseinheit(bestellpositionDto.getnVerpackungseinheit_NOT_IN_DB());
			}
			artikellieferantDto.setMandantCNr(bestellungDto.getMandantCNr());

			// hier wird artikelId geholt um 1 inkrementiert und wieder
			// abgespeichert als Integer
			int help = artikellieferantDto.getArtikelIId().intValue();
			Integer artikelId = new Integer(++help);
			artikellieferantDto.setISort(artikelId);
			artikellieferantDto.setTPreisgueltigab(new Timestamp(bestellungDto.getDBelegdatum().getTime()));
			Integer iId = getArtikelFac().createArtikellieferant(artikellieferantDto, theClientDto);
			artikellieferantDto = getArtikelFac().artikellieferantFindByPrimaryKey(iId, theClientDto);

			if(zuschnittsartikelNeuBerechnen) {
				getArtikelFac().updateArtikellieferantImpl(artikellieferantDto, true, theClientDto);
			}
			
			return artikellieferantDto;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * neue Staffel anlegen
	 * 
	 * @param artikellieferantDto ArtikellieferantDto
	 * @param bestellpositionDto  BestellpositionDto
	 * @throws EJBExceptionLP
	 */
	private void neueStaffel(ArtikellieferantDto artikellieferantDto, BestellpositionDto bestellpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// neue Staffel anlegen
		ArtikellieferantstaffelDto artikellieferantstaffelDto = new ArtikellieferantstaffelDto();
		artikellieferantstaffelDto.setTPreisgueltigab(artikellieferantDto.getTPreisgueltigab());
		artikellieferantstaffelDto.setArtikellieferantIId(artikellieferantDto.getIId());
		artikellieferantstaffelDto.setNMenge(bestellpositionDto.getNMenge());

		// Rabattsatz muss immer neu gerechnet werden
		if (artikellieferantDto.getNEinzelpreis() != null) {
			BigDecimal bdRabattsumme = artikellieferantDto.getNEinzelpreis()
					.subtract(bestellpositionDto.getNNettogesamtpreis());

			BigDecimal bdRabattsatz = new BigDecimal(0);
			if (artikellieferantDto.getNEinzelpreis().doubleValue() != 0) {
				bdRabattsatz = bdRabattsumme
						.divide(artikellieferantDto.getNEinzelpreis(), 4, BigDecimal.ROUND_HALF_EVEN).movePointRight(2);

			}

			artikellieferantstaffelDto.setFRabatt(bdRabattsatz.doubleValue());

			if (Helper.short2boolean(bestellpositionDto.getBNettopreisuebersteuert())) {
				artikellieferantstaffelDto.setBRabattbehalten(Helper.boolean2Short(false));
			} else {
				artikellieferantstaffelDto.setBRabattbehalten(Helper.boolean2Short(true));
			}

		} else {
			artikellieferantstaffelDto.setBRabattbehalten(Helper.boolean2Short(false));
		}

		artikellieferantstaffelDto.setNNettopreis(bestellpositionDto.getNNettogesamtpreis());

		try {
			getArtikelFac().createArtikellieferantstaffel(artikellieferantstaffelDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public BestellpositionDto bestellpositionFindByPrimaryKey(Integer iIdBestellpositionI) throws EJBExceptionLP {
		Bestellposition bestpos = em.find(Bestellposition.class, iIdBestellpositionI);
		if (bestpos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, iIdBestellpositionI.toString());
		}
		return assembleBestellpositionDto(bestpos);
	}

	public BestellpositionServiceDto bestellpositionFindByPrimaryKey(Integer positionId, boolean ladeArtikel,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BestellpositionServiceDto serviceDto = new BestellpositionServiceDto(
				bestellpositionFindByPrimaryKey(positionId));
		if (ladeArtikel && serviceDto.getArtikelIId() != null) {
			serviceDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(serviceDto.getArtikelIId(), theClientDto));
		}
		return serviceDto;
	}

	public BestellpositionDto bestellpositionFindByPrimaryKeyOhneExc(Integer iIdBestellpositionI) {
		Bestellposition bestpos = em.find(Bestellposition.class, iIdBestellpositionI);
		if (bestpos == null) {
			return null;
		}
		return assembleBestellpositionDto(bestpos);
	}

	// Methoden zur Bestimmung der Sortierung der Bestellpositionen
	// --------------

	/**
	 * Das maximale iSort bei den Bestellpositionen fuer einen bestimmten Mandanten
	 * bestimmen.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	private Integer getMaxISort(Integer iIdBestellungI) throws EJBExceptionLP {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("BestellpositionejbSelectMaxISort");
			query.setParameter(1, iIdBestellungI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, e);
		}
		return iiMaxISortO;
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdBestellungI           PK der Bestellung
	 * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdBestellungI,
			int iSortierungNeuePositionI) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Bestellposition oPosition = (Bestellposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Wenn fuer eine Bestellung eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken entstehen.
	 * <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server aufgerufen.
	 * 
	 * @param iIdBestellungI                 PK der Bestellung
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(Integer iIdBestellungI, int iSortierungGeloeschtePositionI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellungI);
		Collection<?> clPositionen = query.getResultList();
		// if (clPositionen.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Bestellposition oPosition = (Bestellposition) it.next();

			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	/**
	 * Alle Positionen zu einer Bestellung bestimmen.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @return BestellpositionDto[] alle Positionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BestellpositionDto[] bestellpositionFindByBestellung(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		BestellpositionDto[] aPositionDtos = null;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		aPositionDtos = assembleBestellpositionDtos(cl);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return aPositionDtos;
	}

	public BigDecimal getSummeMengeGleicherArtikelInBestellung(Integer iIdBestellungI, Integer bestellpositionIId,
			Integer artikelIId, TheClientDto theClientDto) {
		myLogger.entry();
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		BigDecimal bdMengeVorhanden = BigDecimal.ZERO;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellungIIdArtikelIId");
		query.setParameter(1, iIdBestellungI);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();

		while (it.hasNext()) {
			Bestellposition bspos = (Bestellposition) it.next();

			if (bestellpositionIId == null || !bestellpositionIId.equals(bspos.getIId())) {
				bdMengeVorhanden = bdMengeVorhanden.add(bspos.getNMenge());
			}

		}

		return bdMengeVorhanden;
	}

	public boolean enthaeltBestellungMaterialzuschlaege(Integer bestellungIId) {

		boolean b = false;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, bestellungIId);
		Collection<?> cl = query.getResultList();
		BestellpositionDto[] aPositionDtos = assembleBestellpositionDtos(cl);

		for (int i = 0; i < aPositionDtos.length; i++) {
			if (aPositionDtos[i].getNMaterialzuschlag() != null
					&& aPositionDtos[i].getNMaterialzuschlag().doubleValue() != 0) {
				return true;
			}

		}

		return b;

	}

	public Integer getAnzahlBestellpositionen(Integer iIdBestellungI) throws EJBExceptionLP {

		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdBestellungI == null"));
		}
		try {
			Query query = em.createNamedQuery("BestellpositionfindByBestellung");
			query.setParameter(1, iIdBestellungI);
			Collection<?> c = query.getResultList();
			return new Integer(c.size());
		} catch (NoResultException ex) {
			return new Integer(0);
		}
	}

	/**
	 * Die Anzahl der mengebehafteten Positionen zu einer Bestellung berechnen.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @return int die Anzahl
	 * @throws EJBExceptionLP Ausnahme
	 */
	public int berechneAnzahlMengenbehafteteBestellpositionen(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		int iAnzahl = 0;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellungMengeNotNull");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }

		iAnzahl = cl.size();
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }

		return iAnzahl;
	}

	public BestellpositionDto[] bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin(Integer iIdArtikelI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdArtikelI == null"));
		}

		BestellpositionDto[] aPositionDtos = null;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin");
		query.setParameter(1, iIdArtikelI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aPositionDtos = assembleBestellpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aPositionDtos;
	}
	
	public BestellpositionDto[] bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin2(Integer iIdArtikelI,
			TheClientDto theClientDto) {
		myLogger.entry();
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdArtikelI == null"));
		}

		BestellpositionDto[] aPositionDtos = null;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin2");
		query.setParameter(1, iIdArtikelI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aPositionDtos = assembleBestellpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aPositionDtos;
	}

	public void updateBestellpositionBeiLieferterminAenderung(Integer iIdbestellpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		BestellpositionDto bestellpositionDto = bestellpositionFindByPrimaryKey(iIdbestellpositionI);
		Integer iId = bestellpositionDto.getIId();
		try {

			// setzen des liefertermins
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());
			if (bestellpositionDto.getTUebersteuerterLiefertermin() == null) {
				bestellpositionDto.setTUebersteuerterLiefertermin(bestellungDto.getDLiefertermin());
			} else if ((!Helper.cutTimestamp(bestellpositionDto.getTUebersteuerterLiefertermin())
					.equals(Helper.cutTimestamp(bestellungDto.getDLiefertermin())))) {
				bestellpositionDto.setTUebersteuerterLiefertermin(bestellungDto.getDLiefertermin());
			}
			Bestellposition bestellposition = em.find(Bestellposition.class, iId);
			if (bestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			bestellpositionDto = ABTerminAendernpruefenUndSetzen(bestellpositionDto, bestellposition, theClientDto);

			setBestellpositionFromBestellpositionDto(bestellposition, bestellpositionDto);

			// Bestelltliste aktualsisiert
			getArtikelbestelltFac().aktualisiereBestelltListe(bestellposition.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	/**
	 * 
	 * @param iIdBesI      Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void refreshStatusWennAbgerufen(Integer iIdBesI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdBesI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBesI == null"));
		}

		BestellungDto rahmBesDto = null;
		BestellungDto rahmBesTest = null;

		try {
			rahmBesTest = getBestellungFac().bestellungFindByPrimaryKeyOhneExc(iIdBesI);
			// BigDecimal bdOffeneMenge = null;

			if (rahmBesTest.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)
					|| rahmBesTest.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {

				if (rahmBesTest.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
					rahmBesDto = rahmBesTest;
				} else if (rahmBesTest.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
					rahmBesDto = getBestellungFac()
							.bestellungFindByPrimaryKey(rahmBesTest.getIBestellungIIdRahmenbestellung());
				}

				if (rahmBesDto != null) {
					String sStauts = getBestellungFac().getRichtigenBestellStatus(rahmBesDto.getIId(), true,
							theClientDto);
					rahmBesDto.setStatusCNr(sStauts);
					getBestellungFac().updateBestellung(rahmBesDto, theClientDto);
				}

				// // alle Positionen der Rahmenbestellung holen
				// Collection<?> clRahmBesPos = null;
				//
				// // try {
				// Query query =
				// em.createNamedQuery("BestellpositionfindByBestellung");
				// query.setParameter(1, rahmBesDto.getIId());
				// // @todo getSingleResult oder getResultList ?
				// clRahmBesPos = query.getResultList();
				// // if (clRahmBesPos.isEmpty()) {
				// // //RahmBes hat keine positionn
				// // }
				// // }
				// // catch (FinderException ex2) {
				// //RahmBes hat keine positionn
				// // }
				//
				// Iterator<?> itRahmBesPos = clRahmBesPos.iterator();
				//
				// int nRahmBesPosAbgerufen = 0;
				// //wenn alle Rahmenpositionen gleich 0 sind dann wird der
				// // Status der Rahmenbestellung auf Abgerufen gesetzt
				// while (itRahmBesPos.hasNext()) {
				// Bestellposition rahmBesPos = (Bestellposition)
				// itRahmBesPos.next();
				//
				// bdOffeneMenge = rahmBesPos.getNOffenemenge();
				// if (bdOffeneMenge == null || bdOffeneMenge.compareTo(new
				// BigDecimal(0)) == 0) {
				// nRahmBesPosAbgerufen++;
				// rahmBesPos.setBestellpositionstatusCNr(BestellungFac.
				// BESTELLSTATUS_ABGERUFEN);
				// }
				// }
				// if (nRahmBesPosAbgerufen == clRahmBesPos.size()) {
				// rahmBesDto.setBestellungsstatusCNr(BestellungFac.
				// BESTELLSTATUS_ABGERUFEN);
				// getBestellungFac().updateBestellung(rahmBesDto,
				// theClientDto);
				// }
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Aufgrund des Divisors die Abrufpositionen fuer eine Abrufbestellung erzeugen.
	 * 
	 * @param iIdBestellungI PK der Abrufbestellung
	 * @param iDivisorI      der Divisor
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void erzeugeAbrufpositionen(Integer iIdBestellungI, int iDivisorI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		if (iDivisorI <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL, new Exception("iDivisorI <= 0"));
		}

		BestellungDto abrufbestellungDto = null;
		BestellungDto rahmenbestellungDto = null;

		try {
			abrufbestellungDto = getBestellungFac().bestellungFindByPrimaryKey(iIdBestellungI);

			rahmenbestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(abrufbestellungDto.getIBestellungIIdRahmenbestellung());

			// alle Positionen der Rahmenbestellung holen
			Query query = em.createNamedQuery("BestellpositionfindByBestellung");
			query.setParameter(1, rahmenbestellungDto.getIId());
			Collection<?> clRahmenbestellposition = query.getResultList();
			Iterator<?> itRahmenposition = clRahmenbestellposition.iterator();

			Integer headIdRahmen = null;
			Integer headIdPos = null;
			Integer posId = null;

			Map<Integer, Integer> rahmenArtikelsetIds = new HashMap<Integer, Integer>();

			// fuer jede Rahmenposition mit offener Menge > 0
			// eine Abrufposition in der Abrufbestellung erzeugen
			while (itRahmenposition.hasNext()) {
				Bestellposition rahmenbestellposition = (Bestellposition) itRahmenposition.next();
				headIdRahmen = rahmenbestellposition.getPositionIIdArtikelset();

				if (headIdRahmen != null) {
					if (headIdPos == null) {
						headIdPos = posId;

						rahmenArtikelsetIds.put(headIdRahmen, headIdPos);
					}
				} else {
					headIdPos = null;
				}

				if (rahmenbestellposition.getNOffenemenge() != null
						&& rahmenbestellposition.getNOffenemenge().signum() > 0) {
					BestellpositionDto abrufbestellpositionDto = (BestellpositionDto) assembleBestellpositionDto(
							rahmenbestellposition).clone();
					abrufbestellpositionDto.setBestellungIId(abrufbestellungDto.getIId());
					abrufbestellpositionDto.setIBestellpositionIIdRahmenposition(rahmenbestellposition.getIId());
					abrufbestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					abrufbestellpositionDto.setPositioniIdArtikelset(headIdPos);

					double dMengeAbrufbestellposition = 0;
					int iMengeAbrufbestellposition = 0;
					if (iDivisorI == 1) {
						abrufbestellpositionDto.setNMenge(rahmenbestellposition.getNOffenemenge());
					} else {
						if (new Double(iDivisorI).doubleValue() <= rahmenbestellposition.getNOffenemenge()
								.doubleValue()) {
							// jetzt ergibt die Division einen ganzzahligen Wert
							iMengeAbrufbestellposition = rahmenbestellposition.getNMenge().intValue() / iDivisorI;
							abrufbestellpositionDto.setNMenge(
									Helper.rundeKaufmaennisch(new BigDecimal(iMengeAbrufbestellposition), 4));
						} else {
							dMengeAbrufbestellposition = rahmenbestellposition.getNOffenemenge().doubleValue();
							abrufbestellpositionDto.setNMenge(
									Helper.rundeKaufmaennisch(new BigDecimal(dMengeAbrufbestellposition), 4));
						}
					}

					posId = createBestellposition(abrufbestellpositionDto, false, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

					// die offene Menge in der Rahmenposition anpassen
					rahmenbestellposition.setNOffenemenge(
							rahmenbestellposition.getNOffenemenge().subtract(abrufbestellpositionDto.getNMenge()));
				}
			}

			refreshStatusWennAbgerufen(iIdBestellungI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	/**
	 * Berechnet die gesamte offene Menge fuer eine Bestellung ueber alle
	 * Positionen. <br>
	 * Wird verwendet, um festzustellen, ob eine Rahmenbestellung erledigt ist.
	 * 
	 * @param iIdBestellungI PK der Bestellung
	 * @param theClientDto   der aktuelle Benutzer
	 * @return int offene Menge
	 * @throws EJBExceptionLP
	 */
	public double berechneOffeneMenge(Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP {
		double dOffeneMenge = 0.0;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellungI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Bestellposition bestellposition = (Bestellposition) it.next();

			if (bestellposition.getNOffenemenge() != null && bestellposition.getNOffenemenge().doubleValue() > 0.0) {
				dOffeneMenge += bestellposition.getNOffenemenge().doubleValue();
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return dOffeneMenge;
	}

	/**
	 * Innerhalb einer Bestellung einer Bestellposition suchen, die eine bestimmte
	 * Bestellposition referenziert.
	 * 
	 * @param iIdBestellungI                    PK der Bestellung
	 * @param iIdBestellpositionRahmenpositionI PK der referenzierten
	 *                                          Bestellposition
	 * @param theClientDto                      der aktuelle Benutzer
	 * @return BestellpositionDto die gesuchte Bestellposition oder null
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BestellpositionDto bestellpositionFindByBestellungIIdBestellpositionIIdRahmenposition(Integer iIdBestellungI,
			Integer iIdBestellpositionRahmenpositionI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdBestellungI == null"));
		}

		if (iIdBestellpositionRahmenpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}

		BestellpositionDto bestellpositionDto = null;
		try {
			Query query = em.createNamedQuery("BestellpositionfindByBestellungIIdBestellpositionIIdRahmenposition");
			query.setParameter(1, iIdBestellungI);
			query.setParameter(2, iIdBestellpositionRahmenpositionI);
			bestellpositionDto = assembleBestellpositionDto((Bestellposition) query.getSingleResult());
		} catch (NoResultException t) {
			// es gibt keine Bestellposition mit diesen Eigenschaften
		}
		return bestellpositionDto;
	}

	/**
	 * Eine Abrufbestellposition wird korrigiert. Damit mussen auch die Mengen in
	 * der Rahmenbestellung angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI die Abrufbestellposition mit den aktuellen
	 *                                 Werten
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAbrufbestellposition(BestellpositionDto abrufbestellpositionDtoI, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern, TheClientDto theClientDto) throws EJBExceptionLP {
		if (abrufbestellpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("abrufbestellpositionDtoI == null"));
		}

		myLogger.logData(abrufbestellpositionDtoI);

		BestellungDto bestellungDto = null;
		Bestellposition bestehendeAbrufbestellposition = null;
		Bestellposition bestehendeRahmenbestellposition = null;

		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(abrufbestellpositionDtoI.getBestellungIId());
			bestehendeAbrufbestellposition = em.find(Bestellposition.class, abrufbestellpositionDtoI.getIId());
			if (bestehendeAbrufbestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// wenn es sich um eine Position mit Bezug auf den Rahmen handelt
			if (bestehendeAbrufbestellposition.getBestellpositionIIdRahmenposition() != null) {
				bestehendeRahmenbestellposition = em.find(Bestellposition.class,
						abrufbestellpositionDtoI.getIBestellpositionIIdRahmenposition());
			}

			BigDecimal bdAktuelleMengeInAbrufbestellposition = null;
			BigDecimal bdBisherigeMengeInAbrufbestellposition = null;
			BigDecimal bdKorrekturmenge = null; // Menge fuer die
			// Korrekturbuchung

			bdAktuelleMengeInAbrufbestellposition = abrufbestellpositionDtoI.getNMenge();
			bdBisherigeMengeInAbrufbestellposition = bestehendeAbrufbestellposition.getNMenge();
			bdKorrekturmenge = Helper.getBigDecimalNull();

			// SP5785
			if (bestehendeAbrufbestellposition.getArtikelIId() != null
					&& abrufbestellpositionDtoI.getArtikelIId() != null && !bestehendeAbrufbestellposition
							.getArtikelIId().equals(abrufbestellpositionDtoI.getArtikelIId())) {
				// Wenn WE-Pos vorhanden, dann darf Artikel nicht mehr geaendert
				// werden

				WareneingangspositionDto[] dtos = getWareneingangFac()
						.wareneingangspositionFindByBestellpositionIId(bestehendeAbrufbestellposition.getIId());

				if (dtos != null && dtos.length > 0) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
							new Exception("FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN"));
				}
			}

			// eine bestehende Menge korrigieren
			if (bdBisherigeMengeInAbrufbestellposition != null
					&& bdBisherigeMengeInAbrufbestellposition.doubleValue() > 0) {
				bdKorrekturmenge = bdAktuelleMengeInAbrufbestellposition
						.subtract(bdBisherigeMengeInAbrufbestellposition);
			} // Menge war vorher null oder 0
			else {
				bdKorrekturmenge = bdAktuelleMengeInAbrufbestellposition;
			}

			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellungDto.getIId(), theClientDto);

			// spezielle Behandlung fuer eine Handeingabeposition
			if (abrufbestellpositionDtoI.getPositionsartCNr()
					.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(abrufbestellpositionDtoI.getArtikelIId(), theClientDto);
				ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
				artikelsprDto.setCBez(abrufbestellpositionDtoI.getCBez());
				artikelsprDto.setCZbez(abrufbestellpositionDtoI.getCZusatzbez());

				artikelDto.setArtikelsprDto(artikelsprDto);
				artikelDto.setEinheitCNr(abrufbestellpositionDtoI.getEinheitCNr());
				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(abrufbestellpositionDtoI.getMwstsatzIId(), theClientDto);
				artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());

				getArtikelFac().updateArtikel(artikelDto, theClientDto);
			}

			// Schritt 1: Den Bezug zum Rahmen korrigieren
			if (abrufbestellpositionDtoI.getIBestellpositionIIdRahmenposition() != null) {

				// bdKorrekturmenge < 0 -> die offene Menge in der
				// Rahmenposition erhoehen
				// bdKorrekturmenge > 0 -> die offene Menge in der
				// Rahmenposition vermindern
				// Wenn nicht Ident/Handeingabe
				if (abrufbestellpositionDtoI.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| abrufbestellpositionDtoI.getPositionsartCNr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

					bestehendeRahmenbestellposition.setNOffenemenge(
							bestehendeRahmenbestellposition.getNOffenemenge().add(bdKorrekturmenge.negate()));
					int nkStellen = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
					BigDecimal headAmount = bestehendeRahmenbestellposition.getNMenge();
					List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em,
							bestehendeRahmenbestellposition.getIId());
					for (Bestellposition rahmenpos : positions) {
						BigDecimal offeneMenge = rahmenpos.getNOffenemenge().add(bdKorrekturmenge.negate()
								.multiply(rahmenpos.getNMenge()).divide(headAmount, nkStellen, RoundingMode.HALF_EVEN));
						rahmenpos.setNOffenemenge(offeneMenge);
					}
				}
			}

			// Schritt 2: Die Abrufbestellposition korrigieren
			setBestellpositionFromBestellpositionDto(bestehendeAbrufbestellposition, abrufbestellpositionDtoI);
			getArtikelbestelltFac().aktualisiereBestelltListe(bestehendeAbrufbestellposition.getIId(), theClientDto);

			// Preispflege
			// Artikellieferant einpflegen
			if (abrufbestellpositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				preispflege(abrufbestellpositionDtoI, sPreispflegeI, artikellieferantstaffelIId_ZuAendern, false,
						theClientDto);
			}

			if (bestehendeAbrufbestellposition.getArtikelIId() != null
					&& bestehendeAbrufbestellposition.getPositionIIdArtikelset() == null) {
				int nkStellen = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
				BigDecimal headAmount = bdBisherigeMengeInAbrufbestellposition;
				List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em,
						bestehendeAbrufbestellposition.getIId());
				for (Bestellposition bestellposition : positions) {
					BestellpositionDto posDto = assembleBestellpositionDto(bestellposition);
					BigDecimal menge = posDto.getNMenge().add(bdKorrekturmenge.multiply(posDto.getNMenge())
							.divide(headAmount, nkStellen, RoundingMode.HALF_EVEN));
					posDto.setNMenge(menge);
					BigDecimal offeneMenge = posDto.getNOffeneMenge().add(bdKorrekturmenge
							.multiply(posDto.getNOffeneMenge()).divide(headAmount, nkStellen, RoundingMode.HALF_EVEN));
					posDto.setNOffeneMenge(offeneMenge);
					updateBestellposition(posDto, theClientDto, sPreispflegeI, artikellieferantstaffelIId_ZuAendern);
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Ein Teil einer Rahmenposition oder die gesamte wird in der Abrufbestellung
	 * als Abrufposition erfasst. <br>
	 * Die erfasste Menge muss dabei > 0 sein.
	 * 
	 * @param abrufbestellpositionDtoI die bestehenden Abrufposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAbrufbestellpositionSichtRahmen(BestellpositionDto abrufbestellpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (abrufbestellpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("abrufbestellpositionDtoI == null"));
		}

		myLogger.logData(abrufbestellpositionDtoI);

		BestellungDto bestellungDto = null;
		Bestellposition bestehendeAbrufbestellposition = null;
		Bestellposition bestehendeRahmenbestellposition = null;

		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(abrufbestellpositionDtoI.getBestellungIId());
			bestehendeAbrufbestellposition = em.find(Bestellposition.class, abrufbestellpositionDtoI.getIId());
			if (bestehendeAbrufbestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			bestehendeRahmenbestellposition = em.find(Bestellposition.class,
					abrufbestellpositionDtoI.getIBestellpositionIIdRahmenposition());
			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellungDto.getIId(), theClientDto);

			BigDecimal nZusaetzlicheMenge = abrufbestellpositionDtoI.getNMenge()
					.subtract(bestehendeAbrufbestellposition.getNMenge());

			if (nZusaetzlicheMenge.signum() > 0) {
				BestellpositionDto neuePosDto = bestellpositionFindByPrimaryKey(abrufbestellpositionDtoI.getIId());
				neuePosDto.setNMenge(neuePosDto.getNMenge().add(nZusaetzlicheMenge));

				updateBestellposition(neuePosDto, theClientDto, null, null);

				// // Die Abrufposition anpassen, die uebergebene Menge
				// // muss die Gesamtmenge fuer die bestehende Position sein
				// BigDecimal bdMengeGesamt = bestehendeAbrufbestellposition
				// .getNMenge().add(nZusaetzlicheMenge);
				//
				// bestehendeAbrufbestellposition.setNMenge(bdMengeGesamt);
				//
				// // in der zugehoerigen Rahmenbestellposition die offene Menge
				// // korrigieren
				bestehendeRahmenbestellposition.setNOffenemenge(
						bestehendeRahmenbestellposition.getNOffenemenge().subtract(nZusaetzlicheMenge));
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN, new Exception("nMenge <= 0"));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Wenn eine neue Abrufposition angelegt wird, muss die entsprechende
	 * Rahmenposition angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI die aktuelle Abrufbestellposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @return Integer PK der neuen Abrufposition
	 * @throws EJBExceptionLP
	 */
	public Integer createAbrufbestellposition(BestellpositionDto abrufbestellpositionDtoI, String sPreispflegeI,
			Integer artikellieferantstaffelIId_ZuAendern, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(abrufbestellpositionDtoI, "abrufbestellpositionDtoI");

		myLogger.logData(abrufbestellpositionDtoI);

		BestellungDto bestellungDto = null;

		Integer abrufpositionIId = null;
		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(abrufbestellpositionDtoI.getBestellungIId());

			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellungDto.getIId(), theClientDto);

			// PJ 15053
			abrufbestellpositionDtoI.setCABKommentar(null);
			abrufbestellpositionDtoI.setCABNummer(null);
			abrufbestellpositionDtoI.setTAuftragsbestaetigungstermin(null);
			abrufbestellpositionDtoI.setTAbursprungstermin(null);

			// die Abrufposition anlegen
			abrufpositionIId = createBestellposition(abrufbestellpositionDtoI, false, theClientDto, null, null);

			// die zugehoerige Rahmenposition aktualisieren
			BigDecimal nAbrufmenge = abrufbestellpositionDtoI.getNMenge();

			Bestellposition rahmenposition = em.find(Bestellposition.class,
					abrufbestellpositionDtoI.getIBestellpositionIIdRahmenposition());
			if (rahmenposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}

			if (abrufbestellpositionDtoI.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
					|| abrufbestellpositionDtoI.getPositionsartCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

				rahmenposition.setNOffenemenge(rahmenposition.getNOffenemenge().subtract(nAbrufmenge));

				int nkStellen = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
				BigDecimal headAmount = rahmenposition.getNMenge();
				List<Bestellposition> rahmenPositions = BestellpositionQuery.listByPositionIIdArtikelset(em,
						rahmenposition.getIId());
				for (Bestellposition rahmenpos : rahmenPositions) {
					if (rahmenpos.getNOffenemenge() != null && rahmenpos.getNOffenemenge().signum() > 0) {
						BestellpositionDto abrufbestellpositionDto = (BestellpositionDto) assembleBestellpositionDto(
								rahmenpos).clone();
						abrufbestellpositionDto.setBestellungIId(bestellungDto.getIId());
						abrufbestellpositionDto.setIBestellpositionIIdRahmenposition(rahmenpos.getIId());
						abrufbestellpositionDto
								.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
						abrufbestellpositionDto.setPositioniIdArtikelset(abrufpositionIId);

						BigDecimal menge = nAbrufmenge.multiply(rahmenpos.getNMenge()).divide(headAmount, nkStellen,
								RoundingMode.HALF_EVEN);
						abrufbestellpositionDto.setNMenge(menge);
						createBestellposition(abrufbestellpositionDto, false, theClientDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

						// die offene Menge in der Rahmenposition anpassen
						rahmenpos.setNOffenemenge(
								rahmenpos.getNOffenemenge().subtract(abrufbestellpositionDto.getNMenge()));
					}
				}
				if (rahmenPositions.size() > 0) {
					preiseEinesArtikelsetsUpdaten(abrufpositionIId, theClientDto);
				}
				// BigDecimal headAmount = rahmenposition.getNMenge();
				// List<Bestellposition> positions = BestellpositionQuery
				// .listByPositionIIdArtikelset(em, rahmenposition.getIId());
				// for (Bestellposition rahmenpos : positions) {
				// BigDecimal offeneMenge = rahmenpos.getNOffenemenge()
				// .subtract(nAbrufmenge.multiply(rahmenpos.getNMenge()).divide(headAmount));
				// rahmenpos.setNOffenemenge(offeneMenge);
				// }
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return abrufpositionIId;
	}

	/**
	 * Wenn eine Abrufposition geloescht wird, muss die entsprechende Rahmenposition
	 * angepasst werden.
	 * 
	 * @param abrufbestellpositionDtoI die aktuelle Abrufbestellposition
	 * @param theClientDto             String
	 * @throws EJBExceptionLP
	 */
	public void removeAbrufbestellposition(BestellpositionDto abrufbestellpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (abrufbestellpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("abrufbestellpositionDtoI == null"));
		}

		myLogger.logData(abrufbestellpositionDtoI);

		BestellungDto bestellungDto = null;

		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(abrufbestellpositionDtoI.getBestellungIId());
			getBestellungFac().pruefeUndSetzeBestellungstatusBeiAenderung(bestellungDto.getIId(), theClientDto);

			// die Abrufposition loeschen
			removeBestellposition(abrufbestellpositionDtoI, theClientDto);

			// die zugehoerige Rahmenposition aktualisieren
			BigDecimal nAbrufmenge = abrufbestellpositionDtoI.getNMenge();

			Bestellposition rahmenposition = em.find(Bestellposition.class,
					abrufbestellpositionDtoI.getIBestellpositionIIdRahmenposition());
			if (rahmenposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (abrufbestellpositionDtoI.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
					|| abrufbestellpositionDtoI.getPositionsartCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

				rahmenposition.setNOffenemenge(rahmenposition.getNOffenemenge().add(nAbrufmenge));

				int nkStellen = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
				BigDecimal headAmount = rahmenposition.getNMenge();
				List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em,
						rahmenposition.getIId());
				for (Bestellposition rahmenpos : positions) {
					BigDecimal offeneMenge = rahmenpos.getNOffenemenge().add(nAbrufmenge.multiply(rahmenpos.getNMenge())
							.divide(headAmount, nkStellen, RoundingMode.HALF_EVEN));
					rahmenpos.setNOffenemenge(offeneMenge);
				}
			}
			rahmenposition.setBestellpositionstatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Alle Abrufpositionen holen, die sich auf eine bestimmte Rahmenposition
	 * beziehen.
	 * 
	 * @param iIdRahmenpositionI PK der Rahmenposition
	 * @param theClientDto       der aktuelle Benutzer
	 * @return BestellpositionDto[] die Abrufpositionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BestellpositionDto[] bestellpositionFindByBestellpositionIIdRahmenposition(Integer iIdRahmenpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdRahmenpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRahmenpositionI == null"));
		}

		BestellpositionDto[] aBestellposition = null;

		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellpositionIIdRahmenposition");
		query.setParameter(1, iIdRahmenpositionI);
		Collection<?> cl = query.getResultList();

		// Stornierte entfernen
		ArrayList al = new ArrayList();
		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Bestellposition bspos = (Bestellposition) it.next();

			Bestellung bs = em.find(Bestellung.class, bspos.getBestellungIId());
			if (!bs.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_STORNIERT)) {

				al.add(bspos);

			}
		}

		aBestellposition = assembleBestellpositionDtos(al);

		return aBestellposition;
	}

	public Integer bestellpositionSplitten(Integer bestellpositionIId, BigDecimal bdMengeNeu,
			Timestamp dLieferterminNeu, TheClientDto theClientDto) {

		try {
			BestellpositionDto bsdPosDtoVorhanden = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(bestellpositionIId);

			BigDecimal bdMengeAltBerechnet = bsdPosDtoVorhanden.getNMenge().subtract(bdMengeNeu);

			BigDecimal bdSchonGeliefert = BigDecimal.ZERO;

			WareneingangspositionDto[] aWEPOSDto = getWareneingangFac()
					.wareneingangspositionFindByBestellpositionIId(bsdPosDtoVorhanden.getIId());

			for (int i = 0; i < aWEPOSDto.length; i++) {
				bdSchonGeliefert = bdSchonGeliefert.add(aWEPOSDto[i].getNGeliefertemenge());
			}

			if (bdSchonGeliefert.doubleValue() > bdMengeAltBerechnet.doubleValue()) {
				// FEHLER

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(bsdPosDtoVorhanden.getArtikelIId(),
						theClientDto);
				ArrayList al = new ArrayList();

				al.add(Helper.formatZahl(bdSchonGeliefert, 3, theClientDto.getLocUi()) + " "
						+ aDto.getEinheitCNr().trim());

				al.add(Helper.formatZahl(bsdPosDtoVorhanden.getNMenge().subtract(bdSchonGeliefert), 3,
						theClientDto.getLocUi()) + " " + aDto.getEinheitCNr().trim());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLUNG_SPLITT_WEP_MENGE_ZU_GROSS, al,
						new Exception("FEHLER_BESTELLUNG_SPLITT_WEP_MENGE_ZU_GROSS"));

			} else {
				bsdPosDtoVorhanden.setNMenge(bdMengeAltBerechnet);

				getBestellpositionFac().updateBestellposition(bsdPosDtoVorhanden, theClientDto,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
			}

			bsdPosDtoVorhanden.setIId(null);
			bsdPosDtoVorhanden.setTAuftragsbestaetigungstermin(null);
			bsdPosDtoVorhanden.setTManuellvollstaendiggeliefert(null);
			bsdPosDtoVorhanden.setTUebersteuerterLiefertermin(dLieferterminNeu);
			bsdPosDtoVorhanden.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);

			bsdPosDtoVorhanden.setISort(null);
			bsdPosDtoVorhanden.setNMenge(bdMengeNeu);

			Integer bsposIIdNeu = getBestellpositionFac().createBestellposition(bsdPosDtoVorhanden, theClientDto,
					BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
			return bsposIIdNeu;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

	}

	/**
	 * ueberprueft ob die Abrufbestellungen noch Positionen haben
	 * 
	 * @param bestellpositionDtoI BestellpositionDto
	 * @param theClientDto        String
	 * @throws EJBExceptionLP
	 */
	public void checkStatusAbrufBestellungenUndRahmenbestellung(BestellpositionDto bestellpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			BestellungDto abrufbestellungDto[] = null;
			if (bestellpositionDtoI.getIBestellpositionIIdRahmenposition() != null) {
				// holen von Bestellposition um an Rahmenbestellung Id zu kommen
				BestellpositionDto besposDto = bestellpositionFindByPrimaryKey(
						bestellpositionDtoI.getIBestellpositionIIdRahmenposition());
				// alle Abrufbestellungen zu einer Rahmenbestellung holen
				abrufbestellungDto = getBestellungFac()
						.abrufBestellungenfindByRahmenbestellung(besposDto.getBestellungIId(), theClientDto);
				this.setRahmenbestellstatus(abrufbestellungDto, besposDto.getBestellungIId(), theClientDto);
			} else {
				// wenn Bestellposition keinen Rahmenbezug so hat die Bestellung
				// einen Rahmenbezug
				BestellungDto bestellungDto = getBestellungFac()
						.bestellungFindByPrimaryKey(bestellpositionDtoI.getBestellungIId());
				if (bestellungDto.getIBestellungIIdRahmenbestellung() != null) {
					// alle Abrufbestellungen zu einer Rahmenbestellung holen
					abrufbestellungDto = getBestellungFac().abrufBestellungenfindByRahmenbestellung(
							bestellungDto.getIBestellungIIdRahmenbestellung(), theClientDto);
					this.setRahmenbestellstatus(abrufbestellungDto, bestellungDto.getIBestellungIIdRahmenbestellung(),
							theClientDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private int checkAlleAbrufeAufStatusAngelegtUndKeinePositionen(BestellungDto abrufbestellungDto[],
			TheClientDto theClientDto) {

		int countAbrufAufAngelegt = 0;
		// wenn keine Abrufpositionen da und Abrufbestellung auf angelegt
		for (int i = 0; i < abrufbestellungDto.length; i++) {
			if (bestellpositionFindByBestellung(abrufbestellungDto[i].getIId(), theClientDto).length == 0
					&& abrufbestellungDto[i].getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
				countAbrufAufAngelegt++;
			}
		}

		return countAbrufAufAngelegt;
	}

	private void setRahmenbestellstatus(BestellungDto abrufbestellungDto[], Integer iBestellungId,
			TheClientDto theClientDto) {
		try {
			if (checkAlleAbrufeAufStatusAngelegtUndKeinePositionen(abrufbestellungDto,
					theClientDto) != abrufbestellungDto.length) {
				BestellungDto rahmenbestellungDto = getBestellungFac().bestellungFindByPrimaryKey(iBestellungId);

				if (rahmenbestellungDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ABGERUFEN)) {
					rahmenbestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
					getBestellungFac().updateBestellung(rahmenbestellungDto, theClientDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void updateBestellpositionNurABTermin(Integer bestellpositionIId, java.sql.Date abTerminNeu,
			TheClientDto theClientDto) {

		Bestellposition bestellposition = em.find(Bestellposition.class, bestellpositionIId);
		if (bestellposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		bestellposition.setTAuftragsbestaetigungstermin(abTerminNeu);
		bestellposition.setTAbterminAendern(new Timestamp(System.currentTimeMillis()));
		bestellposition.setPersonalIIdAbterminAendern(theClientDto.getIDPersonal());
		em.merge(bestellposition);
		em.flush();
	}

	public void updateBestellpositionNurLieferterminBestaetigt(Integer bestellpositionIId, TheClientDto theClientDto) {

		Bestellposition bestellposition = em.find(Bestellposition.class, bestellpositionIId);
		if (bestellposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (bestellposition.getTLieferterminbestaetigt() == null) {
			bestellposition.setTLieferterminbestaetigt(new Timestamp(System.currentTimeMillis()));
			bestellposition.setPersonalIIdLieferterminbestaetigt(theClientDto.getIDPersonal());
		} else {
			bestellposition.setTLieferterminbestaetigt(null);
			bestellposition.setPersonalIIdLieferterminbestaetigt(null);
		}

		em.merge(bestellposition);
		em.flush();

	}

	public void updateBestellpositionMitABTermin(BestellpositionDto bestellpositionDto, TheClientDto theClientDto,
			String sPreispflegeI) throws EJBExceptionLP {
		myLogger.entry();
		checkBestellpositionDto(bestellpositionDto);
		pruefePflichtfelderBelegposition(bestellpositionDto, theClientDto);

		// Status setzen wenn ABTermin vorhanden "bestaetigt" wenn ABTermin null
		// dann "offen"
		if (bestellpositionDto.getTAuftragsbestaetigungstermin() != null) {
			bestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT);
		} else if (bestellpositionDto.getTAuftragsbestaetigungstermin() == null) {
			bestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
		}

		Integer iId = bestellpositionDto.getIId();

		try {

			Bestellposition bestellposition = em.find(Bestellposition.class, iId);
			if (bestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			bestellpositionDto = ABTerminAendernpruefenUndSetzen(bestellpositionDto, bestellposition, theClientDto);
			setBestellpositionFromBestellpositionDto(bestellposition, bestellpositionDto);

			befuelleZusaetzlichePreisfelder(iId);

			if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
				Integer artikelIId = bestellpositionDto.getArtikelIId();

				if (artikelIId != null) {
					ArtikelFac artikelFac = getArtikelFac();
					ArtikelDto artikelDto = artikelFac.artikelFindByPrimaryKey(artikelIId, theClientDto);

					if (artikelDto.getArtikelsprDto() != null) {
						ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
						artikelsprDto.setCBez(bestellpositionDto.getCBez());
						artikelsprDto.setCZbez(bestellpositionDto.getCZusatzbez());
					}
					artikelDto.setEinheitCNr(bestellpositionDto.getEinheitCNr());
					// Der Artikel erhaelt die Mwst-Satz-Bezeichnung

					// PJ 16649 - auskommentiert
					/*
					 * MwstsatzDto mwstsatzDto = getMandantFac() .mwstsatzFindByPrimaryKey(
					 * bestellpositionDto.getMwstsatzIId(), theClientDto);
					 * artikelDto.setMwstsatzbezIId(mwstsatzDto .getIIMwstsatzbezId());
					 */
					artikelFac.updateArtikel(artikelDto, theClientDto);
				}
			}
			// Bestelltliste aktualsisiert
			getArtikelbestelltFac().aktualisiereBestelltListe(bestellposition.getIId(), theClientDto);

			this.setBestellungstatusWennPositionenBestaetigt(bestellpositionDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * setzt Bestellstatus wenn jede Bestellposition den Status "Bestaetigt" hat auf
	 * "Bestaetigt"
	 * 
	 * @param bestellpositionDto BestellpositionDto
	 * @param theClientDto       String
	 * @throws EJBExceptionLP
	 */
	private void setBestellungstatusWennPositionenBestaetigt(BestellpositionDto bestellpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			BestellpositionDto bestellpos[] = bestellpositionFindByBestellung(bestellpositionDto.getBestellungIId(),
					theClientDto);

			ArrayList<BestellpositionDto> alBestpos = new ArrayList<BestellpositionDto>();
			// SP2979 Nur Ident/Handeingabe zaehlt
			for (int i = 0; i < bestellpos.length; i++) {
				if (bestellpos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| bestellpos[i].getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE))
					alBestpos.add(bestellpos[i]);
			}

			int countBestaetigt = 0;
			int countOffen = 0;
			for (int i = 0; i < alBestpos.size(); i++) {
				if (alBestpos.get(i).getBestellpositionstatusCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT)) {
					countBestaetigt++;
				}

				if (alBestpos.get(i).getBestellpositionstatusCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN)) {
					countOffen++;
				}

			}

			BestellungDto bestellungDto = null;
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());

			// Wenn kein Keine Position mit Bestaetigt vorhanden ist, dann ist
			// die Bestellung OFFEN

			if (countOffen == alBestpos.size()) {
				bestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_OFFEN);
				getBestellungFac().updateBestellung(bestellungDto, theClientDto);
			} else if (countOffen == 0) {
				// Wenn keine Offene merh vorhanden, dann ist die Bestellung
				// bestaetigt
				bestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_BESTAETIGT);
				getBestellungFac().updateBestellung(bestellungDto, theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * setzt den ABTermin und AB-Nummer auf null
	 * 
	 * @param bestellpositionDto BestellpositionDto
	 * @param theClientDto       String
	 * @throws EJBExceptionLP
	 */
	public void removeABTerminVonBestellposition(BestellpositionDto bestellpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkBestellpositionDto(bestellpositionDto);
		// ruecksetzten des ABTermins und der AB-Nummer
		if (bestellpositionDto.getTAuftragsbestaetigungstermin() != null) {
			bestellpositionDto.setTAuftragsbestaetigungstermin(null);
			bestellpositionDto.setTAbursprungstermin(null);
			bestellpositionDto.setCABNummer(null);
			bestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
		}

		Integer iId = bestellpositionDto.getIId();

		try {
			Bestellposition bestellposition = em.find(Bestellposition.class, iId);
			if (bestellposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			setBestellpositionFromBestellpositionDto(bestellposition, bestellpositionDto);

			if (bestellpositionDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				getArtikelbestelltFac().aktualisiereBestelltListe(bestellpositionDto.getIId(), theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		// status checken
		setBestellungstatusWennPositionenBestaetigt(bestellpositionDto, theClientDto);
	}

	/**
	 * setzt ABTermin und ABNummer fuer alle Positionen einer Bestellung
	 * 
	 * @param bestellungIId                  Integer
	 * @param markierteBestellpositionenIIds
	 * @param abDatum                        Date
	 * @param abNummer                       String
	 * @param iOption
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 */
	public void setForAllPositionenABTermin(Integer bestellungIId, Integer[] markierteBestellpositionenIIds,
			java.sql.Date abDatum, java.sql.Date abBelegDatum, String abNummer, int iOption, TheClientDto theClientDto) throws EJBExceptionLP {
		BestellpositionDto[] besposDto = null;
		besposDto = bestellpositionFindByBestellung(bestellungIId, theClientDto);

		// wenn true werden alle gesetzt
		if (iOption == SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_ALLE) {
			for (int i = 0; i < besposDto.length; i++) {
				besposDto[i].setTAuftragsbestaetigungstermin(abDatum);
				besposDto[i].setTAbBelegdatum(abBelegDatum);
				besposDto[i].setCABNummer(abNummer);
				updateBestellpositionMitABTermin(besposDto[i], theClientDto, "");
			}
		}
		// oder markierte Positionen setzen
		else if (iOption == SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_MARKIERTE) {

			for (int i = 0; i < besposDto.length; i++) {

				boolean bMarkiert = false;

				for (int z = 0; z < markierteBestellpositionenIIds.length; z++) {
					if (besposDto[i].getIId().equals(markierteBestellpositionenIIds[z])) {
						bMarkiert = true;
						break;
					}

				}

				if (bMarkiert == true) {

					besposDto[i].setTAuftragsbestaetigungstermin(abDatum);
					besposDto[i].setTAbBelegdatum(abBelegDatum);
					besposDto[i].setCABNummer(abNummer);
					updateBestellpositionMitABTermin(besposDto[i], theClientDto, "");
				}

			}
			// sonst nur leere Positionen setzen
		} else if (iOption == SICHT_LIEFERANTENTERMINE_ABTERMIN_SETZEN_OPTION_LEERE) {

			for (int i = 0; i < besposDto.length; i++) {
				if (besposDto[i].getTAuftragsbestaetigungstermin() == null) {
					besposDto[i].setTAuftragsbestaetigungstermin(abDatum);
					besposDto[i].setTAbBelegdatum(abBelegDatum);
					besposDto[i].setCABNummer(abNummer);
					updateBestellpositionMitABTermin(besposDto[i], theClientDto, "");
				}
			}
		}
	}

	public int getAnzahlMengenbehaftetBSPOS(Integer iIdBestellungI, TheClientDto theClientDto) {
		if (iIdBestellungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdBestellungI == null"));
		}
		int iAnzahl = 0;
		// try {
		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellungI);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Bestellposition item = (Bestellposition) iter.next();
			if (item.getNMenge() != null) {
				iAnzahl++;
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		return iAnzahl;
	}

	/**
	 * Berechnet die offene Menge einer BSPOS aufgrund der WEPOSen.
	 * 
	 * @param bestposDtoI Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal berechneOffeneMenge(BestellpositionDto bestposDtoI) throws EJBExceptionLP {
		BigDecimal bdOffeneMenge = new BigDecimal(0);
		try {
			// wenn die Bestellposition schon auf Geliefert oder Erledigt steht,
			// gibt es keine offene Menge mehr.
			if (bestposDtoI.getTManuellvollstaendiggeliefert() == null && !bestposDtoI.getBestellpositionstatusCNr()
					.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {
				BigDecimal bdMengeGeliefert = new BigDecimal(0);
				// zugehoerige WE-Positionen.
				WareneingangspositionDto[] wepos = getWareneingangFac()
						.wareneingangspositionFindByBestellpositionIId(bestposDtoI.getIId());
				if (wepos != null) {
					// Gelieferte Mengen addieren.
					for (int i = 0; i < wepos.length; i++) {
						bdMengeGeliefert = bdMengeGeliefert.add(wepos[i].getNGeliefertemenge());
					}
					if (bestposDtoI.getNMenge() != null)
						// Die offene Menge ist die Differenz zwischen
						// bestellter und gelieferter Menge.
						bdOffeneMenge = bestposDtoI.getNMenge().subtract(bdMengeGeliefert);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bdOffeneMenge;
	}

	/**
	 * berechnet die offene Menge einer Bestellposition aufgrund der WE-Positionen
	 * 
	 * @param iIdbsposI Integer
	 * @return BigDecimal
	 */
	public BigDecimal berechneOffeneMenge(Integer iIdbsposI) {
		BestellpositionDto bestposDto = bestellpositionFindByPrimaryKey(iIdbsposI);
		return berechneOffeneMenge(bestposDto);
	}

	/**
	 * hole zu belegPosDtoI den OF-price als node.
	 * 
	 * @param docI         Document
	 * @param belegPosDtoI BestellpositionDto
	 * @param theClientDto String
	 * @return Node
	 * @throws EJBExceptionLP
	 */
	public Node getPriceAsNode(Document docI, BelegpositionDto belegPosDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Node nodePriceRet = null;
		BestellpositionDto bSPosDto = (BestellpositionDto) belegPosDtoI;

		nodePriceRet = docI.createElement(SystemFac.SCHEMA_OF_PRICE);

		// Price: BasePrice
		addOFElement(nodePriceRet, docI, bSPosDto.getNNettoeinzelpreis(), SystemFac.SCHEMA_OF_PRICE_BASEPRICE);

		// Price: BaseQuantity
		addOFElement(nodePriceRet, docI, "1", /** @todo JO klaeren PJ 3908 */
				SystemFac.SCHEMA_OF_PRICE_BASEQUANTITY);

		// Price: Discounts /** @todo JO PJ 3909 */
		Node nodeOF = getDiscountsAsNode(docI, bSPosDto);
		nodePriceRet.appendChild(nodeOF);

		// Price: VAT /** @todo JO PJ 3910 */
		if (bSPosDto.getMwstsatzIId() != null) {
			Double dMwst = getMandantFac().mwstsatzFindByPrimaryKey(bSPosDto.getMwstsatzIId(), theClientDto)
					.getFMwstsatz();
			addOFElement(nodePriceRet, docI, dMwst, SystemFac.SCHEMA_OF_PRICE_VAT);
		}

		// Price: Contract /** @todo JO PJ 3918 */
		addOFElement(nodePriceRet, docI, "JO-WH Contract", SystemFac.SCHEMA_OF_PRICE_CONTRACT);
		return nodePriceRet;
	}

	/**
	 * hole zu bSPosDtoI den OF-discount als node.
	 * 
	 * @param docI         Document
	 * @param belegPosDtoI BestellpositionDto
	 * @return Node
	 */
	public Node getDiscountsAsNode(Document docI, BelegpositionDto belegPosDtoI) {

		BestellpositionDto bSPosDto = (BestellpositionDto) belegPosDtoI;

		Element nodeDiscount = docI.createElement(SystemFac.SCHEMA_OF_PRICE_DISCOUNTS);

		// Discounts: Daily /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, "JO-WH Daily", SystemFac.SCHEMA_OF_DISCOUNTTYPE_DAILY);

		// Discounts: Description /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, "JO-WH Description", SystemFac.SCHEMA_OF_DISCOUNTTYPE_DESCRIPTION);

		// Discounts: Percent /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, bSPosDto.getDRabattsatz(), SystemFac.SCHEMA_OF_DISCOUNTTYPE_PERCENT);

		// Discounts: Rest /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, "JO-WH Rest", SystemFac.SCHEMA_OF_DISCOUNTTYPE_REST);

		// Discounts: Sequence /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, "JO-WH Sequence", SystemFac.SCHEMA_OF_DISCOUNTTYPE_SEQUENCE);

		// Discounts: Value /** @todo JO PJ 3919 */
		addOFElement(nodeDiscount, docI, "JO-WH Value", SystemFac.SCHEMA_OF_DISCOUNTTYPE_VALUE);

		return nodeDiscount;
	}

	/**
	 * fuege zu belegpositionDtoI die speziellen HV-belegpos-feature-felder.
	 * 
	 * @param belegPosDtoI  BestellpositionDto
	 * @param nodeFeaturesI Node
	 * @param docI          Document
	 * @return Document
	 * @throws DOMException
	 */
	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI, Node nodeFeaturesI, Document docI)
			throws DOMException {

		BestellpositionDto bSPos = (BestellpositionDto) belegPosDtoI;

		// Feature: Drucken
		addHVFeature(nodeFeaturesI, docI, BestellpositionFac.SCHEMA_HV_FEATURE_DRUCKEN, bSPos.getBDrucken());

		// Feature: Bestellpositionstatus
		addHVFeature(nodeFeaturesI, docI, BestellpositionFac.SCHEMA_HV_FEATURE_BESTELLPOSITIONSTATUSCNR,
				bSPos.getBestellpositionstatusCNr());

		// Feature: MwstsatzUebersteuert
		addHVFeature(nodeFeaturesI, docI, BestellpositionFac.SCHEMA_HV_FEATURE_MWSTSATZUEBERSTEUERT,
				bSPos.getBMwstsatzUebersteuert());

		// Feature: MwstsatzIId
		addHVFeature(nodeFeaturesI, docI, BestellpositionFac.SCHEMA_HV_FEATURE_MWSTSATZIID, bSPos.getMwstsatzIId());

		// Feature: NNettogesamtpreis
		addHVFeature(nodeFeaturesI, docI, BestellpositionFac.SCHEMA_HV_FEATURE_NNETTOGESAMTPREIS,
				bSPos.getNNettogesamtpreis());

		return nodeFeaturesI;
	}

	public BelegpositionDto getNextBelegPosDto(int iIdBelegPosI, TheClientDto theClientDto) throws Exception {
		return bestellpositionFindByPrimaryKey(iIdBelegPosI);
	}

	private void pruefePflichtfelderBelegposition(BestellpositionDto bsPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDto(bsPosDto, theClientDto);
		if (LocaleFac.POSITIONSART_IDENT.equals(bsPosDto.getPositionsartCNr())) {
			if (bsPosDto.getDRabattsatz() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("DRabattsatz == null"));
			}
			if (bsPosDto.getNRabattbetrag() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("NRabattbetrag == null"));
			}
			if (bsPosDto.getNNettoeinzelpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("NNettoeinzelpreis == null"));
			}
			if (bsPosDto.getNNettogesamtpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("NNettogesamtpreis == null"));
			}
			if (bsPosDto.getNMaterialzuschlag() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("NMaterialzuschlag == null"));
			}
			// if(bsPosDto.getNNettogesamtPreisminusRabatte()==null){
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN
			// ,new Exception("NNettogesamtPreisminusRabatte == null"));
			// }
			// if(bsPosDto.getMwstsatzIId()==null){
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN
			// ,new Exception("MwstsatzIId == null"));
			// }
		}
	}

	public void erledigeAlleNichtMengenpositionen(BestellungDto bestellungDto, TheClientDto theClientDto) {
		BestellpositionDto[] besPos = bestellpositionFindByBestellung(bestellungDto.getIId(), theClientDto);
		if (besPos != null && besPos.length > 0) {
			for (int i = 0; i < besPos.length; i++) {
				if (!BestellpositionFac.BESTELLPOSITIONART_IDENT.equals(besPos[i].getPositionsartCNr())
						&& !BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE.equals(besPos[i].getPositionsartCNr())) {
					// Position ist eine Position ohne Menge
					besPos[i].setBestellpositionstatusCNr(BESTELLPOSITIONSTATUS_ERLEDIGT);
					Bestellposition bestellposition = em.find(Bestellposition.class, besPos[i].getIId());
					setBestellpositionFromBestellpositionDto(bestellposition, besPos[i]);
					// updateBestellposition(besPos[i],
					// theClientDto.getIDUser());
				}
			}
		}
	}

	public BestellpositionDto[] bestellpositionenFindAll() {
		Query query = em.createNamedQuery("BestellpositionfindAll");
		Collection<Bestellposition> cl = query.getResultList();
		BestellpositionDto[] besPosDto = assembleBestellpositionDtos(cl);
		return besPosDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String checkBestellpositionStati(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BestellpositionDto[] besPosDto = getBestellpositionFac().bestellpositionenFindAll();
		StringBuffer sErrors = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < besPosDto.length; i++) {
			String sRichtigerStatus = null;
			try {
				sRichtigerStatus = getRichtigenBestellpositionStatus(besPosDto[i].getIId(), theClientDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!besPosDto[i].getBestellpositionstatusCNr().equals(sRichtigerStatus)) {
				// merken und Ausgeben
				counter++;
				BestellungDto besDto = getBestellungFac().bestellungFindByPrimaryKey(besPosDto[i].getBestellungIId());
				String actError = getTimestamp().toString() + " " + counter + " Bestellung " + besDto.getCNr()
						+ " Position " + besPosDto[i].getISort() + " " + besPosDto[i].getCBez() + " in Status "
						+ besPosDto[i].getBestellpositionstatusCNr().trim() + " statt " + sRichtigerStatus.trim()
						+ "\n";
				sErrors.append(actError);
				System.out.println(actError);
				besPosDto[i].setBestellpositionstatusCNr(sRichtigerStatus);
				try {
					if (besPosDto[i].getEinheitCNr() == null) {
						if (besPosDto[i].getArtikelIId() != null) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(besPosDto[i].getArtikelIId(), theClientDto);
							besPosDto[i].setEinheitCNr(artikelDto.getEinheitCNr());
						}
					}
					getBestellpositionFac().updateBestellposition(besPosDto[i], theClientDto, null, null);
				} catch (Exception e) {
					String sError = "\n" + "Fehler beim Update der vorherigen position " + e.getLocalizedMessage()
							+ "\n";
					System.out.println(sError);
					sErrors.append(sError);
				}
			}
		}
		return sErrors.toString();
	}

	public String getRichtigenBestellpositionStatus(Integer bestellPosIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// Bestellposition holen
		BestellpositionDto besPosDto = bestellpositionFindByPrimaryKey(bestellPosIId);
		if (besPosDto.getTManuellvollstaendiggeliefert() != null) {
			return BESTELLPOSITIONSTATUS_ERLEDIGT;
		}
		// Positionen ohne Menge sind automatisch erledigt
		if (besPosDto.getNMenge() == null) {
			return BESTELLPOSITIONSTATUS_ERLEDIGT;
		} else {
			// Dann zugehoerige Bestellung holen
			BestellungDto bestDto = getBestellungFac().bestellungFindByPrimaryKey(besPosDto.getBestellungIId());
			// Wenn Bestellung storniert dann ist auch die Position storniert
			if (bestDto.getTStorniert() != null) {
				return BESTELLPOSITIONSTATUS_STORNIERT;
			} else {
				// Nachsehen ob WEs vorhanden sind
				WareneingangspositionDto[] wePosDto = getWareneingangFac()
						.wareneingangspositionFindByBestellpositionIId(bestellPosIId);
				if (wePosDto.length == 0) {
					// Es gibt keine Wareneingaenge => bestaetigt, offen,
					// abgerufen
					boolean bIsAbgerufen = true;
					BestellpositionDto[] abrufePosDto = getBestellpositionFac()
							.bestellpositionFindByBestellpositionIIdRahmenposition(bestellPosIId, theClientDto);
					if (abrufePosDto.length > 0) {
						BigDecimal bdPosMenge = besPosDto.getNMenge();
						for (int y = 0; y < abrufePosDto.length; y++) {
							String sAbrufStatus = getBestellungFac()
									.getRichtigenBestellStatus(abrufePosDto[y].getBestellungIId(), true, theClientDto);
							// Die Mengen von angelegten und stornierten abrufen
							// werden nicht beruecksichtigt
							if (!sAbrufStatus.equals(BestellungFac.BESTELLSTATUS_STORNIERT)
									&& !sAbrufStatus.equals(BestellungFac.BESTELLSTATUS_ANGELEGT)) {
								if (abrufePosDto[y].getNMenge() != null) {
									bdPosMenge = bdPosMenge.subtract(abrufePosDto[y].getNMenge());
								}
							}
						}
						// Wenn die Menge aller Abrufe kleiner als Rahmenmenge
						// ist dann ist nicht abgerufen
						if (new BigDecimal(0).compareTo(bdPosMenge) < 0) {
							bIsAbgerufen = false;
						}
					} else {
						// Keine abrufe zur Bestellung
						bIsAbgerufen = false;
					}
					if (bIsAbgerufen) {
						// Wenn im Status abgerufen gelten die Stati der
						// abrufpositionen
						boolean bAlleAbrufeGeliefert = true;
						for (int y = 0; y < abrufePosDto.length; y++) {
							String sStatus = getRichtigenBestellpositionStatus(abrufePosDto[y].getIId(), theClientDto);
							if (!sStatus.equals(BESTELLPOSITIONSTATUS_ERLEDIGT)
									&& !sStatus.equals(BESTELLPOSITIONSTATUS_GELIEFERT)) {
								bAlleAbrufeGeliefert = false;
							}
						}
						if (bAlleAbrufeGeliefert) {
							boolean bAlleAbrufeErledigt = true;
							for (int z = 0; z < abrufePosDto.length; z++) {
								String sStatus = getRichtigenBestellpositionStatus(abrufePosDto[z].getIId(),
										theClientDto);
								if (!sStatus.equals(BESTELLPOSITIONSTATUS_ERLEDIGT)
										&& !sStatus.equals(BESTELLPOSITIONSTATUS_STORNIERT)) {
									bAlleAbrufeErledigt = false;
								}
							}
							if (bAlleAbrufeErledigt) {
								return BESTELLPOSITIONSTATUS_ERLEDIGT;
							} else {
								return BESTELLPOSITIONSTATUS_GELIEFERT;
							}
						} else {
							return BESTELLPOSITIONSTATUS_ABGERUFEN;
						}
					} else {
						if (besPosDto.getTAuftragsbestaetigungstermin() != null) {
							return BESTELLPOSITIONSTATUS_BESTAETIGT;
						} else {
							return BESTELLPOSITIONSTATUS_OFFEN;
						}
					}
				} else {
					// Es gibt Wareneingaenge Position ist Teilgeliefert,
					// geliefert, oder Erledigt
					boolean bIsGeliefert = true;
					boolean bIsErledigt = true;
					// Pruefen ob geliefert oder teilgeliefert + erledigt
					BigDecimal bdPosMenge = besPosDto.getNMenge();
					for (int i = 0; i < wePosDto.length; i++) {
						// pruefen ob alle WePos erledigt
						if (!wePosDto[i].getBPreiseErfasst()) {
							bIsErledigt = false;
						}
						bdPosMenge = bdPosMenge.subtract(wePosDto[i].getNGeliefertemenge());
					}
					if (new BigDecimal(0).compareTo(bdPosMenge) < 0) {
						if (besPosDto.getTManuellvollstaendiggeliefert() == null) {
							bIsGeliefert = false;
						}
					}
					if (bIsGeliefert) {
						// Geliefert oder Erledigt
						if (bIsErledigt) {
							return BESTELLPOSITIONSTATUS_ERLEDIGT;
						} else {
							return BESTELLPOSITIONSTATUS_GELIEFERT;
						}
					} else {
						return BESTELLPOSITIONSTATUS_TEILGELIEFERT;
					}
				}
			}
		}
	}

	public void sortiereNachArtikelnummer(Integer iIdBestellung, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("BestellpositionfindByBestellung");
		query.setParameter(1, iIdBestellung);
		BestellpositionDto[] dtos = assembleBestellpositionDtos(query.getResultList());

		for (int i = dtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				BestellpositionDto o = dtos[j];
				BestellpositionDto o1 = dtos[j + 1];

				String sort = "";

				if (o.getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(o.getArtikelIId(),
							theClientDto);

					if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						sort = Helper.fitString2Length(artikelDto.getCNr(), 40, ' ');
					} else {
						sort = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZH";
					}

				} else {
					sort = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
				}

				String sort1 = "";

				if (o1.getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(o1.getArtikelIId(),
							theClientDto);

					if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						sort1 = Helper.fitString2Length(artikelDto.getCNr(), 40, ' ');
					} else {
						sort1 = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZH";
					}

				} else {
					sort1 = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
				}

				if (sort.compareTo(sort1) > 0) {
					dtos[j] = o1;
					dtos[j + 1] = o;
				}
			}
		}

		int iSort = 1;
		for (int i = 0; i < dtos.length; i++) {
			Bestellposition bestellposition = em.find(Bestellposition.class, dtos[i].getIId());
			bestellposition.setISort(iSort);
			em.merge(bestellposition);
			em.flush();
			iSort++;
		}
	}

	private Integer getPositionNummerImpl(Integer bestellpositionIId) {
		Bestellposition bestpos = em.find(Bestellposition.class, bestellpositionIId);

		Integer posnr = null;

		if (bestpos.getBestellpositionartCNr().equals(BESTELLPOSITIONART_HANDEINGABE)
				|| (bestpos.getBestellpositionartCNr().equals(BESTELLPOSITIONART_IDENT)
						&& bestpos.getPositionIIdArtikelset() == null)) {

			// TODO: Fehlt da nicht ein Order by I_SORT?
			String queryString = "SELECT count(i_id) "
					+ " FROM FLRBestellposition AS bespos  WHERE bespos.flrbestellung.i_id="
					+ bestpos.getBestellungIId() + " AND bespos.bestellpositionart_c_nr in('"
					+ BESTELLPOSITIONART_HANDEINGABE + "','" + BESTELLPOSITIONART_IDENT
					+ "') AND bespos.position_i_id_artikelset IS NULL AND bespos.i_sort <= " + bestpos.getISort();
			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();

			if (resultList.size() > 0) {
				Long l = (Long) resultList.iterator().next();

				if (l != null) {
					posnr = l.intValue();
				}
			}
		}

		return posnr;
	}

	// @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer getPositionNummer(Integer bestellpositionIId, TheClientDto theClientDto) {
		return getPositionNummerImpl(bestellpositionIId);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer getPositionNummerReadOnly(Integer bestellpositionIId) {
		return getPositionNummerImpl(bestellpositionIId);
	}

	@Override
	public void vertauscheBestellpositionenMinus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Bestellposition> comp = new CompositeISort<Bestellposition>(
				new BestellpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		BestellpositionDto pos = bestellpositionFindByPrimaryKey(iIdBasePosition);
		updateBestellpositionOhneWeitereAktion(pos, theClientDto);
	}

	@Override
	public void vertauscheBestellpositionenPlus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Bestellposition> comp = new CompositeISort<Bestellposition>(
				new BestellpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		BestellpositionDto pos = bestellpositionFindByPrimaryKey(iIdBasePosition);
		updateBestellpositionOhneWeitereAktion(pos, theClientDto);
	}

	@Override
	public void vertauschePositionen(Integer iId1, Integer iId2) throws EJBExceptionLP {
		myLogger.logData(iId1 + ", " + iId2);
		Bestellposition oPosition1 = em.find(Bestellposition.class, iId1);

		Bestellposition oPosition2 = em.find(Bestellposition.class, iId2);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// das zweite iSort auf ungueltig setzen, damit UK constraint
		// nicht verletzt wird
		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);

	}

	@Override
	public BelegpositionDto getNewPositionDto() {
		return new BestellpositionDto();
	}

	@Override
	public BelegpositionDto preparePositionDtoAusImportResult(BelegpositionDto posDto, StklImportSpezifikation spez,
			IStklImportResult result, TheClientDto theClientDto) {
		posDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		((BestellpositionDto) posDto).setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
		((BestellpositionDto) posDto).setBNettopreisuebersteuert(Helper.getShortFalse());
		((BestellpositionDto) posDto).setBMwstsatzUebersteuert(Helper.getShortTrue());
		((BestellpositionDto) posDto).setDRabattsatz(new Double(0));
		((BestellpositionDto) posDto).setNRabattbetrag(new BigDecimal(0));

		BestellungDto bestellungDto = null;
		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKeyOhneExc(posDto.getBelegIId());
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		((BestellpositionDto) posDto).setTUebersteuerterLiefertermin(
				bestellungDto == null || bestellungDto.getDLiefertermin() == null ? getTimestamp()
						: new Timestamp(bestellungDto.getDLiefertermin().getTime()));

		BigDecimal nNettoeinzelpreis = new BigDecimal(0);
		if (result.uebernehmeLiefPreisInBestellung()
				&& result.getValues().get(StklImportSpezifikation.LIEFPREIS) != null) {
			nNettoeinzelpreis = Helper.toBigDecimal(result.getValues().get(StklImportSpezifikation.LIEFPREIS));
		} else if (result.getValues().get(StklImportSpezifikation.BESTELLPREIS) != null) {
			nNettoeinzelpreis = Helper.toBigDecimal(result.getValues().get(StklImportSpezifikation.BESTELLPREIS));
		}
		((BestellpositionDto) posDto).setNNettoeinzelpreis(nNettoeinzelpreis);
		((BestellpositionDto) posDto).setNNettogesamtpreis(nNettoeinzelpreis);
		((BestellpositionDto) posDto).setNMaterialzuschlag(new BigDecimal(0));

		return posDto;
	}

	@Override
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		for (Iterator<BelegpositionDto> iter = posDtos.iterator(); iter.hasNext();) {
			createBestellposition((BestellpositionDto) iter.next(), theClientDto,
					BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
		}
	}

	@Override
	public IImportPositionen asPositionImporter() {
		return this;
	}

	public boolean istTeilEinesArtikelsets(BestellpositionDto positionDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(positionDto, "positionDto");

		Integer headId = positionDto.getPositioniIdArtikelset();
		if (headId != null)
			return true;

		StuecklisteDto stklDto = stuecklisteFindByBestellposition(positionDto, theClientDto);
		return stklDto != null;
	}

	private StuecklisteDto stuecklisteFindByBestellposition(BestellpositionDto positionDto, TheClientDto theClientDto) {
		Integer headItemId = null;
		Integer headId = positionDto.getPositioniIdArtikelset();
		if (headId != null) {
			Bestellposition posEntity = em.find(Bestellposition.class, headId);
			headItemId = posEntity.getArtikelIId();
			if (headItemId == null)
				return null;
		} else {
			headItemId = positionDto.getArtikelIId();
		}

		// Koennte der Kopfartikel sein
		StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(headItemId,
				theClientDto);
		if (null == stklDto)
			return null;
		if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr()))
			return null;

		return stklDto;
	}

	public List<Integer> getArtikelIdsEinesArtikelsetsMitSnrCharge(BestellpositionDto positionDto,
			TheClientDto theClientDto) {
		Validator.dtoNotNull(positionDto, "positionDto");

		StuecklisteDto stklDto = stuecklisteFindByBestellposition(positionDto, theClientDto);
		if (stklDto == null)
			return null;

		List<Integer> artikelIIds = getStuecklisteFac().getSeriennrChargennrArtikelIIdsFromStueckliste(stklDto.getIId(),
				BigDecimal.ONE, theClientDto);
		return artikelIIds;
	}

	public boolean istArtikelsetUnbearbeitet(BestellpositionDto positionDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(positionDto, "positionDto");

		Integer headId = positionDto.getPositioniIdArtikelset();
		if (headId == null) {
			// Koennte der Kopfartikel sein
			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(positionDto.getArtikelIId(), theClientDto);
			if (null == stklDto)
				return false;
			if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr()))
				return false;
			headId = positionDto.getIId();
		}

		// Query query = em
		// .createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
		// query.setParameter(1, headId);
		// Collection<Bestellposition> positions = query.getResultList();
		List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em, headId);
		for (Bestellposition bestellposition : positions) {
			if (bestellposition.getNOffenemenge() != null
					&& bestellposition.getNMenge().compareTo(bestellposition.getNOffenemenge()) != 0) {
				return false;
			}
		}

		return true;
	}

	public BigDecimal getErfuellbareSaetzeArtikelset(BigDecimal menge, BestellpositionDto positionDto,
			final Integer wareneingangId, TheClientDto theClientDto) throws RemoteException {
		Validator.dtoNotNull(positionDto, "positionDto");

		StuecklisteDto stklDto = stuecklisteFindByBestellposition(positionDto, theClientDto);
		if (stklDto == null)
			return null;

		// Query query = em
		// .createNamedQuery("BestellpositionfindByPositionIIdArtikelset");
		// query.setParameter(1,
		// positionDto.getPositioniIdArtikelset() != null ?
		// positionDto.getPositioniIdArtikelset() : positionDto.getIId());
		// Collection<Bestellposition> positions =
		// (Collection<Bestellposition>)query.getResultList();

		List<Bestellposition> positions = BestellpositionQuery.listByPositionIIdArtikelset(em,
				positionDto.getPositioniIdArtikelset() != null ? positionDto.getPositioniIdArtikelset()
						: positionDto.getIId());
		BigDecimal saetze = positionDto.getNMenge();
		BigDecimal erfuellbareMenge = menge;
		int scale = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
		for (Bestellposition position : positions) {
			WareneingangspositionDto[] weDtos = getWareneingangFac()
					.wareneingangspositionFindByBestellpositionIId(position.getIId());
			WareneingangspositionDto wePosDto = CollectionTools.detect(weDtos, new IDetect<WareneingangspositionDto>() {
				public boolean accept(WareneingangspositionDto posDto) {
					return posDto.getWareneingangIId().equals(wareneingangId);
				};
			});

			if (wePosDto == null && position.getNOffenemenge().signum() == 0) {
				// Im WE nicht erfasst, macht aber nichts, weil Bestellposition
				// bereits erfuellt
				continue;
			}

			// BigDecimal gelieferteMenge =
			// position.getNMenge().subtract(position.getNOffenemenge());
			BigDecimal gelieferteMenge = wePosDto == null ? BigDecimal.ZERO : wePosDto.getNGeliefertemenge();
			BigDecimal satzFactor = position.getNMenge().divide(saetze, scale, RoundingMode.HALF_EVEN);
			BigDecimal m = gelieferteMenge.divide(satzFactor, scale, RoundingMode.HALF_EVEN);
			if (m.compareTo(erfuellbareMenge) < 0) {
				erfuellbareMenge = m;
				if (erfuellbareMenge.signum() == 0) {
					break;
				}
			}
		}

		return erfuellbareMenge;
	}

	
}
