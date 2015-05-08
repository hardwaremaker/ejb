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
package com.lp.server.angebot.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionDtoAssembler;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.AngebotPositionNumberAdapter;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AngebotpositionFacBean extends Facade implements
		AngebotpositionFac, IPrimitiveSwapper {
	@PersistenceContext
	private EntityManager em;

	private void istSteuersatzInPositionsartPositionGleich(
			AngebotpositionDto agposDto, TheClientDto theClientDto) {
		if (agposDto.getTypCNr() != null
				&& (agposDto.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1) || agposDto
						.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2))
				&& agposDto.getPositioniId() != null) {
			AngebotpositionDto[] dtos = angebotpositionFindByPositionIId(
					agposDto.getPositioniId(), theClientDto);
			for (int i = 0; i < dtos.length; i++) {
				if (agposDto.getMwstsatzIId() != null) {
					if (dtos[i].getMwstsatzIId() != null) {
						if (!agposDto.getMwstsatzIId().equals(
								dtos[i].getMwstsatzIId())) {
							// MWST-Saetze innerhalb "Position" muessen
							// immer gleich sein
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH,
									"");
						}
					}
				}
			}
		}
	}

	public Integer createAngebotposition(
			AngebotpositionDto angebotpositionDtoI, TheClientDto theClientDto) {
		return createAngebotposition(angebotpositionDtoI, true, theClientDto);
	}

	private void updateTAendernAngebot(Integer angebotIId,
			TheClientDto theClientDto) {
		Angebot angebot = em.find(Angebot.class, angebotIId);
		angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
		angebot.setTAendern(getTimestamp());
		em.merge(angebot);
		em.flush();
	}

	public Integer createAngebotposition(
			AngebotpositionDto angebotpositionDtoI,
			boolean bArtikelSetAufloesen, TheClientDto theClientDto) {
		checkAngebotpositionDto(angebotpositionDtoI);
		Integer angebotpositionIId = null;
		pruefePflichtfelderBelegposition(angebotpositionDtoI, theClientDto);
		updateTAendernAngebot(angebotpositionDtoI.getBelegIId(), theClientDto);

		try {
			// endsumme: 5 Es kann nur eine Position Endsumme geben
			if (angebotpositionDtoI.getPositionsartCNr().equals(
					AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)
					&& angebotpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
							angebotpositionDtoI.getBelegIId(),
							AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME) != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
						new Exception(
								"Eine Position Endsumme existiert bereits."));
			}
			pruefeAngebotpositionAendernErlaubt(angebotpositionDtoI,
					theClientDto);

			// Handartikel anlegen
			if (angebotpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(angebotpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(angebotpositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(angebotpositionDtoI.getEinheitCNr());
				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								angebotpositionDtoI.getMwstsatzIId(),
								theClientDto);
				oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Handartikel anlegen
				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
						theClientDto);

				angebotpositionDtoI.setArtikelIId(iIdArtikel);

				myLogger.info("Handartikel wurde angelegt, iIdArtikel:"
						+ iIdArtikel);
			}

			if (angebotpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								angebotpositionDtoI.getArtikelIId(),
								theClientDto);
				if (artikelDto.getMaterialIId() != null) {
					AngebotDto agDto = getAngebotFac().angebotFindByPrimaryKey(
							angebotpositionDtoI.getBelegIId(), theClientDto);

					MaterialzuschlagDto mDto = getMaterialFac()
							.getKursMaterialzuschlagDtoInZielwaehrung(
									artikelDto.getMaterialIId(),
									new java.sql.Date(agDto.getTBelegdatum()
											.getTime()),
									agDto.getWaehrungCNr(), theClientDto);
					if (mDto != null) {
						angebotpositionDtoI.setNMaterialzuschlagKurs(mDto
								.getNZuschlag());
						angebotpositionDtoI.setTMaterialzuschlagDatum(mDto
								.getTGueltigab());
					}
				}
			}

			// generieren von primary key
			angebotpositionIId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_ANGEBOTPOSITION);
			angebotpositionDtoI.setIId(angebotpositionIId);

			// Sortierung: falls nicht anders definiert, hinten dran haengen.
			if (angebotpositionDtoI.getISort() == null) {
				int iSortNeu = getMaxISort(angebotpositionDtoI.getBelegIId(),
						theClientDto) + 1;
				angebotpositionDtoI.setISort(iSortNeu);
			}

			AngebotpositionDto vorherigeDtoI = null;
			int iSort = getMaxISort(angebotpositionDtoI.getBelegIId(),
					theClientDto);
			try {

				if (angebotpositionDtoI.getISort() != null) {
					iSort = angebotpositionDtoI.getISort() - 1;
				}
				Query query = em
						.createNamedQuery("AngebotpositionfindByAngebotIIdISort");
				query.setParameter(1, angebotpositionDtoI.getBelegIId());
				query.setParameter(2, iSort);
				vorherigeDtoI = assembleAngebotpositionDto((Angebotposition) query
						.getSingleResult());
			} catch (EJBExceptionLP ex1) {
			} catch (NoResultException ex1) {
			} catch (NonUniqueResultException e1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
						"Fehler bei AngebotpositionfindByIsort. Es gibt mehrere Angebotpositionen mit isort "
								+ iSort
								+ "fuer AngebotIId "
								+ angebotpositionDtoI.getBelegIId());
			}
			angebotpositionDtoI = (AngebotpositionDto) befuellepositionBelegpositionDtoVerkauf(
					vorherigeDtoI, angebotpositionDtoI, theClientDto);
			istSteuersatzInPositionsartPositionGleich(angebotpositionDtoI,
					theClientDto);

			Angebotposition angebotposition = new Angebotposition(
					angebotpositionDtoI.getIId(),
					angebotpositionDtoI.getBelegIId(),
					angebotpositionDtoI.getISort(),
					angebotpositionDtoI.getPositionsartCNr(),
					angebotpositionDtoI.getNGestehungspreis(),
					angebotpositionDtoI.getMwstsatzIId(),
					angebotpositionDtoI.getBNettopreisuebersteuert());
			angebotposition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
			em.persist(angebotposition);
			em.flush();

			setAngebotpositionFromAngebotpositionDto(angebotposition,
					angebotpositionDtoI);

			befuelleZusaetzlichePreisfelder(angebotpositionIId, theClientDto);

			// endsumme: 6 Nach dem Abspeichern wird die Reihenfolge der
			// Positionen
			// in Hinblick auf die Position Endsumme geprueft und ev. korrigiert
			sortierungAnpassenInBezugAufEndsumme(
					angebotpositionDtoI.getBelegIId(), theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikle ebenfalls
			// buchen:
			if (bArtikelSetAufloesen == true
					&& angebotpositionDtoI.getArtikelIId() != null) {

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								angebotpositionDtoI.getArtikelIId(),
								theClientDto);
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
					AngebotpositionDto angebotpositionDtoKopfartikel = angebotpositionFindByPrimaryKey(
							angebotpositionDtoI.getIId(), theClientDto);

					List<?> m = null;
					try {
						m = getStuecklisteFac()
								.getStrukturDatenEinerStueckliste(
										stklDto.getIId(),
										theClientDto,
										StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
										0, null, false, false,
										angebotpositionDtoI.getNMenge(), null,
										true);
					} catch (RemoteException ex4) {
						throwEJBExceptionLPRespectOld(ex4);
					}

					Iterator<?> it = m.listIterator();

					while (it.hasNext()) {
						StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
								.next();
						StuecklistepositionDto position = struktur
								.getStuecklistepositionDto();

						angebotpositionDtoI.setNEinzelpreis(new BigDecimal(0));
						angebotpositionDtoI
								.setNNettoeinzelpreis(new BigDecimal(0));
						angebotpositionDtoI.setNMwstbetrag(new BigDecimal(0));
						angebotpositionDtoI.setNRabattbetrag(new BigDecimal(0));
						angebotpositionDtoI.setFZusatzrabattsatz(0D);
						angebotpositionDtoI.setFRabattsatz(0D);
						angebotpositionDtoI
								.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
										0));
						angebotpositionDtoI
								.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
										0));
						angebotpositionDtoI
								.setNBruttoeinzelpreis(new BigDecimal(0));

						angebotpositionDtoI.setNMenge(Helper
								.rundeKaufmaennisch(
										position.getNZielmenge().multiply(
												angebotpositionDtoKopfartikel
														.getNMenge()), 4));

						angebotpositionDtoI.setArtikelIId(position
								.getArtikelIId());
						angebotpositionDtoI.setEinheitCNr(position
								.getEinheitCNr());
						angebotpositionDtoI
								.setPositioniIdArtikelset(angebotpositionDtoKopfartikel
										.getIId());
						angebotpositionDtoI.setIId(null);

						int iSortNeu = angebotpositionDtoI.getISort() + 1;

						sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
								angebotpositionDtoI.getBelegIId(), iSortNeu,
								theClientDto);

						angebotpositionDtoI.setISort(iSortNeu);
						createAngebotposition(angebotpositionDtoI, false,
								theClientDto);

					}
					preiseEinesArtikelsetsUpdaten(
							angebotpositionDtoKopfartikel.getIId(),
							theClientDto);
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		myLogger.exit("Angebotposition wurde angelegt.");

		return angebotpositionIId;
	}

	private void pruefeAngebotpositionAendernErlaubt(
			AngebotpositionDto angebotpositionDtoI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
				angebotpositionDtoI.getBelegIId(), theClientDto);
		if (!angebotDto.getStatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BELEG_IST_BEREITS_AKTIVIERT,
					new Exception("Status: " + angebotDto.getStatusCNr()));
		}
	}

	/**
	 * Fuer eine Angebotposition vom Typ Ident oder Handeingabe werden die
	 * zusaetzlichen Preisfelder befuellt.
	 * 
	 * @param iIdPositionI
	 *            PK der Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionIId(iIdPositionI);
		try {
			Angebotposition angebotposition = em.find(Angebotposition.class,
					iIdPositionI);
			if (angebotposition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei befuellezusaetzlichePreisfelder. Es gibt keine Angebotsposition mit der iid "
										+ iIdPositionI));
			}

			if (angebotposition.getAngebotpositionartCNr().equals(
					AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
					|| angebotposition.getAngebotpositionartCNr().equals(
							AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)
					|| angebotposition.getAngebotpositionartCNr().equals(
							AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {

				AngebotDto angebotDto = getAngebotFac()
						.angebotFindByPrimaryKey(
								angebotposition.getAngebotIId(), theClientDto);
				AngebotpositionDto angebotpositionDto = angebotpositionFindByPrimaryKey(
						iIdPositionI, theClientDto);
				angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac()
						.berechneBelegpositionVerkauf(angebotpositionDto,
								angebotDto);

				angebotposition
						.setNNettoeinzelpreisplusversteckteraufschlag(angebotpositionDto
								.getNEinzelpreisplusversteckteraufschlag());
				angebotposition
						.setNNettogesamtpreisplusversteckteraufschlag(angebotpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlag());
				angebotposition
						.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(angebotpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

				/*
				 * // den versteckten Aufschlag aus den Konditionen
				 * beruecksichtigen BigDecimal nVersteckterAufschlag = new
				 * BigDecimal(angebotDto. getFVersteckterAufschlag().
				 * doubleValue()).movePointLeft(2); nVersteckterAufschlag =
				 * Helper.rundeKaufmaennisch(nVersteckterAufschlag, 4);
				 * 
				 * BigDecimal nNettoeinzelpreisVersteckterAufschlagSumme =
				 * angebotposition
				 * .getNNettoeinzelpreis().multiply(nVersteckterAufschlag);
				 * nNettoeinzelpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * nNettoeinzelpreisVersteckterAufschlagSumme, 4);
				 * 
				 * angebotposition.setNNettoeinzelpreisplusversteckteraufschlag(
				 * angebotposition. getNNettoeinzelpreis().
				 * add(nNettoeinzelpreisVersteckterAufschlagSumme));
				 * 
				 * BigDecimal nNettogesamtpreisVersteckterAufschlagSumme =
				 * angebotposition
				 * .getNNettogesamtpreis().multiply(nVersteckterAufschlag);
				 * nNettogesamtpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * nNettogesamtpreisVersteckterAufschlagSumme, 4);
				 * angebotposition
				 * .setNNettogesamtpreisplusversteckteraufschlag(angebotposition
				 * . getNNettogesamtpreis().
				 * add(nNettogesamtpreisVersteckterAufschlagSumme));
				 * 
				 * // die Abschlaege werden auf Basis des Versteckten Aufschlags
				 * beruecksichtigt
				 * 
				 * // - Allgemeiner Rabatt BigDecimal nAllgemeinerRabatt = new
				 * BigDecimal(angebotDto. getFAllgemeinerRabattsatz().
				 * doubleValue()).movePointLeft(2); nAllgemeinerRabatt =
				 * Helper.rundeKaufmaennisch(nAllgemeinerRabatt, 4);
				 * 
				 * BigDecimal nNettogesamtpreisAllgemeinerRabattSumme =
				 * angebotposition
				 * .getNNettogesamtpreisplusversteckteraufschlag().multiply(
				 * nAllgemeinerRabatt);
				 * 
				 * nNettogesamtpreisAllgemeinerRabattSumme =
				 * Helper.rundeKaufmaennisch(
				 * nNettogesamtpreisAllgemeinerRabattSumme, 4);
				 * 
				 * angebotposition.
				 * setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
				 * angebotposition.
				 * getNNettogesamtpreisplusversteckteraufschlag().subtract(
				 * nNettogesamtpreisAllgemeinerRabattSumme));
				 * 
				 * // - Projektierungsrabatt BigDecimal nProjektierungsRabatt =
				 * new BigDecimal(angebotDto.
				 * getFProjektierungsrabattsatz().doubleValue
				 * ()).movePointLeft(2); nProjektierungsRabatt =
				 * Helper.rundeKaufmaennisch(nProjektierungsRabatt, 4);
				 * 
				 * BigDecimal nNettogesamtpreisProjektierungsrabattSumme =
				 * angebotposition
				 * .getNNettogesamtpreisplusversteckteraufschlagminusrabatte().
				 * multiply( nProjektierungsRabatt);
				 * 
				 * nNettogesamtpreisProjektierungsrabattSumme =
				 * Helper.rundeKaufmaennisch(
				 * nNettogesamtpreisProjektierungsrabattSumme, 4);
				 * 
				 * angebotposition.
				 * setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
				 * angebotposition
				 * .getNNettogesamtpreisplusversteckteraufschlagminusrabatte().
				 * subtract( nNettogesamtpreisProjektierungsrabattSumme));
				 */
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Die zusaetzlichen Preisfelder wurden befuellt.");
	}

	public void berechnePauschalposition(BigDecimal neuWert,
			Integer positionIId, Integer belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BigDecimal altWert = getGesamtpreisPosition(positionIId, theClientDto);
		AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
				belegIId, theClientDto);
		AngebotpositionDto[] angebotpositionDtos = angebotpositionFindByPositionIId(
				positionIId, theClientDto);
		for (int i = 0; i < angebotpositionDtos.length; i++) {
			AngebotpositionDto angebotpositionDto = (AngebotpositionDto) getBelegVerkaufFac()
					.berechnePauschalposition(angebotpositionDtos[i],
							angebotDto, neuWert, altWert);
			Angebotposition position = em.find(Angebotposition.class,
					angebotpositionDto.getIId());

			position.setNNettogesamtpreis(angebotpositionDto
					.getNNettoeinzelpreis());
			position.setNNettogesamtpreisplusversteckteraufschlag(angebotpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			position.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(angebotpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			position.setBNettopreisuebersteuert(Helper.boolean2Short(true));
		}
	}

	public Double berechneArbeitszeitSoll(Integer iIdAngebotI,
			TheClientDto theClientDto) {

		double dArbeitszeitSoll = 0;

		Query query = em
				.createNamedQuery("AngebotpositionfindByAngebotIIdOhneAlternative");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Angebotposition pos = (Angebotposition) it.next();

			if (pos.getAngebotpositionartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				if (pos.getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(pos.getArtikelIId(),
									theClientDto);

					if (artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						dArbeitszeitSoll += pos.getNMenge().doubleValue();
					}
				}
			}

		}

		return new Double(dArbeitszeitSoll);
	}

	/**
	 * Eine bestehende Angebotposition loeschen.
	 * 
	 * @param angebotpositionDtoI
	 *            die bestehende Angebotposition
	 * @param theClientDto
	 *            der aktuelle Bentuzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAngebotposition(AngebotpositionDto angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionDto(angebotpositionDtoI);
		try {
			pruefeAngebotpositionAendernErlaubt(angebotpositionDtoI,
					theClientDto);
			updateTAendernAngebot(angebotpositionDtoI.getBelegIId(),
					theClientDto);

			if (angebotpositionDtoI.getPositioniIdArtikelset() == null) {

				Query query = em
						.createNamedQuery("AngebotpositionpositionfindByPositionIIdArtikelset");
				query.setParameter(1, angebotpositionDtoI.getIId());
				Collection<?> angebotpositionDtos = query.getResultList();
				AngebotpositionDto[] zugehoerigeLSPosDtos = assembleAngebotpositionDtos(angebotpositionDtos);

				for (int i = 0; i < zugehoerigeLSPosDtos.length; i++) {
					removeAngebotposition(zugehoerigeLSPosDtos[i], theClientDto);
				}
			}

			Angebotposition toRemove = em.find(Angebotposition.class,
					angebotpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler beim entfernen der Angebotsposition. Es gibt keine Position mit iid "
										+ angebotpositionDtoI.getIId()));
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			// die Sortierung muss angepasst werden
			sortierungAnpassenBeiLoeschenEinerPosition(
					angebotpositionDtoI.getBelegIId(), angebotpositionDtoI
							.getISort().intValue());

			if (angebotpositionDtoI.getPositioniIdArtikelset() != null) {
				preiseEinesArtikelsetsUpdaten(
						angebotpositionDtoI.getPositioniIdArtikelset(),
						theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception(t));
		}
	}

	/**
	 * Wenn fuer ein Angebot eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken
	 * entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param iSortierungGeloeschtePositionI
	 *            die Position der geloschten Position
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(
			Integer iIdAngebotI, int iSortierungGeloeschtePositionI)
			throws Throwable {
		Query query = em.createNamedQuery("AngebotpositionfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Angebotposition angebotposition = (Angebotposition) it.next();

			if (angebotposition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				angebotposition.setISort(new Integer(
						iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}

		myLogger.exit("Die Sortierung wurde angepasst.");
	}

	public Integer gehoertZuArtikelset(Integer angebotpositionIId) {

		Angebotposition oPosition1 = em.find(Angebotposition.class,
				angebotpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return oPosition1.getPositionIIdArtikelset();
		}

		Query query = em
				.createNamedQuery("AngebotpositionpositionfindByPositionIIdArtikelset");
		query.setParameter(1, angebotpositionIId);
		Collection<?> lieferscheinpositionDtos = query.getResultList();

		if (lieferscheinpositionDtos != null
				&& lieferscheinpositionDtos.size() > 0) {
			return angebotpositionIId;
		} else {
			return null;
		}

	}

	/**
	 * Eine bestehende Angebotposition aktualisieren.
	 * 
	 * @param angebotpositionDtoI
	 *            die bestehende Angebotposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotposition(AngebotpositionDto angebotpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionDto(angebotpositionDtoI);
		pruefePflichtfelderBelegposition(angebotpositionDtoI, theClientDto);
		updateTAendernAngebot(angebotpositionDtoI.getBelegIId(), theClientDto);
		try {
			pruefeAngebotpositionAendernErlaubt(angebotpositionDtoI,
					theClientDto);

			Angebotposition angebotposition = em.find(Angebotposition.class,
					angebotpositionDtoI.getIId());
			if (angebotposition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei Update Angebotsposition. Es gibt keine Position mit iid "
										+ angebotpositionDtoI.getIId()));
			}

			setAngebotpositionFromAngebotpositionDto(angebotposition,
					angebotpositionDtoI);

			befuelleZusaetzlichePreisfelder(angebotposition.getIId(),
					theClientDto);

			Integer angebotspoitionIIdKopfartikel = gehoertZuArtikelset(angebotpositionDtoI
					.getIId());
			if (angebotspoitionIIdKopfartikel != null) {
				preiseEinesArtikelsetsUpdaten(angebotspoitionIIdKopfartikel,
						theClientDto);
			}

			// spezielle Behandlung fuer eine Handeingabeposition
			if (angebotpositionDtoI.getPositionsartCNr().equals(
					AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								angebotpositionDtoI.getArtikelIId(),
								theClientDto);

				ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();
				oArtikelsprDto.setCBez(angebotpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(angebotpositionDtoI.getCZusatzbez());

				artikelDto.setArtikelsprDto(oArtikelsprDto);
				artikelDto.setEinheitCNr(angebotpositionDtoI.getEinheitCNr());
				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								angebotpositionDtoI.getMwstsatzIId(),
								theClientDto);
				artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Artikel speichern
				getArtikelFac().updateArtikel(artikelDto, theClientDto);
			}
			// PJ 09/0014648 spezielle Behandlung fuer eine Position
			if (angebotpositionDtoI.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_POSITION)) {
				try {
					Query query = em
							.createNamedQuery("AngebotpositionfindByPositionIId");
					query.setParameter(1, angebotpositionDtoI.getIId());
					Collection<?> cl = query.getResultList();
					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Angebotposition position = (Angebotposition) iterator
								.next();
						position.setNMenge(angebotpositionDtoI.getNMenge());
					}

				} catch (NoResultException ex) {

				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}

		istSteuersatzInPositionsartPositionGleich(angebotpositionDtoI,
				theClientDto);

	}

	/**
	 * Das maximale iSort bei den Angebotpositionen fuer ein bestimmtes Angebot
	 * bestimmen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private Integer getMaxISort(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotpositionIId(iIdAngebotI);
		Integer maxISort = null;
		try {
			Query query = em
					.createNamedQuery("AngebotpositionejbSelectMaxISort");
			query.setParameter(1, iIdAngebotI);
			maxISort = (Integer) query.getSingleResult();
			if (maxISort == null) {
				maxISort = new Integer(0);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(t));
		}
		myLogger.exit("Max isort: " + maxISort);
		return maxISort;
	}

	/**
	 * Zwei bestehende Angbotpositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdAngebotposition1I
	 *            PK der ersten Position
	 * @param iIdAngebotposition2I
	 *            PK der zweiten Position
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauschePositionen(Integer iIdAngebotposition1I,
			Integer iIdAngebotposition2I) throws EJBExceptionLP {
		checkAngebotpositionIId(iIdAngebotposition1I);
		myLogger.info("Vertausche: " + iIdAngebotposition1I + ", "
				+ iIdAngebotposition2I);
		Angebotposition oPosition1 = em.find(Angebotposition.class,
				iIdAngebotposition1I);
		Angebotposition oPosition2 = em.find(Angebotposition.class,
				iIdAngebotposition2I);
		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() == null) {

			// das zweite iSort auf ungueltig setzen, damit UK constraint
			// nicht verletzt wird
			oPosition2.setISort(new Integer(-1));

			oPosition1.setISort(iSort2);
			oPosition2.setISort(iSort1);
		} else if (oPosition1.getTypCNr() == null
				&& oPosition2.getTypCNr() != null) {

			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					Query query = em
							.createNamedQuery("AngebotpositionfindByAngebotIIdISort");
					query.setParameter(1, oPosition2.getAngebotIId());
					query.setParameter(2, oPosition2.getISort() - 1);
					// @todo getSingleResult oder getResultList ?
					Angebotposition oPos = (Angebotposition) query
							.getSingleResult();
					oPosition1.setTypCNr(oPos.getTypCNr());
					oPosition1.setPositionIId(oPos.getPositionIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			} else if (oPosition2.getTypCNr().equals(
					LocaleFac.POSITIONTYP_ALLES)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_VERDICHTET)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_OHNEPREISE)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_MITPREISE)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_BEGINN)) {
					oPosition1.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					oPosition1.setPositionIId(oPosition2.getIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() == null) {

		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() != null) {
			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null
						&& oPosition2.getCZbez().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					oPosition1.setTypCNr(null);
					oPosition1.setPositionIId(null);
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				} else {
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));
					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		}
		myLogger.exit("Positionen vertauscht.");
	}

	public void vertauscheAngebotpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		CompositeISort<Angebotposition> comp = new CompositeISort<Angebotposition>(
				new AngebotpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		updateTAendernAngebot(
				angebotpositionFindByPrimaryKey(iIdBasePosition, theClientDto)
						.getBelegIId(), theClientDto);
	}

	public void vertauscheAngebotpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		CompositeISort<Angebotposition> comp = new CompositeISort<Angebotposition>(
				new AngebotpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		updateTAendernAngebot(
				angebotpositionFindByPrimaryKey(iIdBasePosition, theClientDto)
						.getBelegIId(), theClientDto);
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAngebotI, int iSortierungNeuePositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotpositionIId(iIdAngebotI);
		myLogger.info("Sortierung der neuen Position: "
				+ iSortierungNeuePositionI);
		Query query = em.createNamedQuery("AngebotpositionfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Angebotposition oPosition = (Angebotposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		myLogger.exit("Sortierung angepasst.");
	}

	/**
	 * endsumme: 6 Wenn eine Position Endsumme in den Angebotpositionen
	 * enthalten ist, dann muss sie am Ende der preisbehafteten Positionen
	 * stehen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdAngebotI);
		AngebotpositionDto angebotpositionDto = angebotpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
				iIdAngebotI, AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME);

		if (angebotpositionDto != null) {
			AngebotpositionDto[] aAngebotpositionDto = angebotpositionFindByAngebotIId(
					iIdAngebotI, theClientDto);

			updateTAendernAngebot(iIdAngebotI, theClientDto);

			for (int i = 0; i < aAngebotpositionDto.length; i++) {
				if (aAngebotpositionDto[i].getPositionsartCNr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME)) {
					int iIndexLetztePreisbehaftetePositionNachEndsumme = -1;

					for (int j = i + 1; j < aAngebotpositionDto.length; j++) {
						if (aAngebotpositionDto[j].getNEinzelpreis() != null) {
							// die Position der letzten preisbehafteten Position
							// nach der Endsumme bestimmen
							iIndexLetztePreisbehaftetePositionNachEndsumme = j;
						}
					}

					if (iIndexLetztePreisbehaftetePositionNachEndsumme != -1) {
						// die Endsumme muss nach die letzte preisbehaftete
						// Position verschoben werden
						for (int k = i; k < iIndexLetztePreisbehaftetePositionNachEndsumme; k++) {
							vertauschePositionen(
									aAngebotpositionDto[i].getIId(),
									aAngebotpositionDto[k + 1].getIId());
						}
					}
				}
			}
		}
	}

	private void preiseEinesArtikelsetsUpdaten(
			Integer angbotpositionIIdKopfartikel, TheClientDto theClientDto) {

		AngebotpositionDto angbeotpositionDtoKopfartikel = angebotpositionFindByPrimaryKey(
				angbotpositionIIdKopfartikel, theClientDto);

		Query query = em
				.createNamedQuery("AngebotpositionpositionfindByPositionIIdArtikelset");
		query.setParameter(1, angbeotpositionDtoKopfartikel.getIId());
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		try {
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					angbeotpositionDtoKopfartikel.getBelegIId(), theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);

			Integer mwstsatzbezIId = getMandantFac().mwstsatzFindByPrimaryKey(
					angbeotpositionDtoKopfartikel.getMwstsatzIId(),
					theClientDto).getIIMwstsatzbezId();

			// Zuerst Gesamtwert berechnen
			BigDecimal bdMenge = angbeotpositionDtoKopfartikel.getNMenge();

			BigDecimal bdNettoeinzelpreis = angbeotpositionDtoKopfartikel
					.getNNettoeinzelpreis();

			BigDecimal bdGesamtwertposition = bdMenge
					.multiply(bdNettoeinzelpreis);

			BigDecimal bdGesamtVKwert = new BigDecimal(0);

			Iterator<?> it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Angebotposition struktur = (Angebotposition) it.next();

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								angebotDto.getKundeIIdAngebotsadresse(),

								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(), angebotDto.getWaehrungCNr(),
								theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper
						.getVkpreisBerechnet(vkpreisDto);

				if (kundenVKPreisDto != null
						&& kundenVKPreisDto.nettopreis != null) {
					bdGesamtVKwert = bdGesamtVKwert
							.add(kundenVKPreisDto.nettopreis.multiply(struktur
									.getNMenge()));
				}

			}

			bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);

			it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Angebotposition struktur = (Angebotposition) it.next();

				struktur.setNNettoeinzelpreis(new BigDecimal(0));
				struktur.setNNettogesamtpreisplusversteckteraufschlag(new BigDecimal(
						0));
				struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
						0));
				struktur.setNBruttogesamtpreis(new BigDecimal(0));

				// Mehrwertsteuersatz: Kommt immer aus dem Kopfartikel,
				// da dieser die Hauptleistung darstellt

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								angebotDto.getKundeIIdAngebotsadresse(),
								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(), angebotDto.getWaehrungCNr(),
								theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper
						.getVkpreisBerechnet(vkpreisDto);

				if (kundenVKPreisDto != null
						&& kundenVKPreisDto.nettopreis != null
						&& bdGesamtVKwert.doubleValue() != 0) {
					// Preis berechnen
					BigDecimal bdAnteilVKWert = kundenVKPreisDto.nettopreis
							.multiply(struktur.getNMenge().multiply(bdMenge))
							.divide(bdGesamtVKwert, 4,
									BigDecimal.ROUND_HALF_EVEN);

					BigDecimal bdPreis = bdGesamtwertposition.multiply(
							bdAnteilVKWert).divide(
							struktur.getNMenge().multiply(bdMenge), 4,
							BigDecimal.ROUND_HALF_EVEN);

					struktur.setNNettoeinzelpreis(bdPreis);

					struktur.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);
					struktur.setNBruttogesamtpreis(bdPreis);
					struktur.setNMwstbetrag(new BigDecimal(0));
					struktur.setNRabattbetrag(new BigDecimal(0));
					struktur.setNNettogesamtpreisplusversteckteraufschlag(bdPreis);
					struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(bdPreis);
					struktur.setNNettogesamtpreis(bdPreis);

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}
	}

	/**
	 * Berechnet die Anzahl der Positionen zu einem bestimmten Angebot.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return int die Anzahl der Positonen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public int getAnzahlMengenbehafteteAngebotpositionen(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdAngebotI);
		int iAnzahl = 0;
		Query query = em.createNamedQuery("AngebotpositionfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Angebotposition pos = ((Angebotposition) iter.next());

			if (pos.getNMenge() != null) {

				if (pos.getArtikelIId() != null) {
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(pos.getArtikelIId(),
									theClientDto);
					if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
						continue;
					}

				}

				iAnzahl++;
			}
		}
		myLogger.exit("Anzahl: " + iAnzahl);
		return iAnzahl;
	}

	public AngebotpositionDto angebotpositionFindByPrimaryKey(
			Integer iIdAngebotpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AngebotpositionDto angebotpositionDto = angebotpositionFindByPrimaryKeyOhneExc(iIdAngebotpositionI);
		if (angebotpositionDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei Angebotsposition find by Primary Key. Es gibt keine angebotsposition mit iid "
									+ iIdAngebotpositionI));
		}
		myLogger.exit("AngebotpositionDto: " + angebotpositionDto.toString());
		return angebotpositionDto;
	}

	public AngebotpositionDto angebotpositionFindByPrimaryKeyOhneExc(
			Integer iIdAngebotpositionI) {
		Angebotposition angebotposition = em.find(Angebotposition.class,
				iIdAngebotpositionI);
		if (angebotposition == null) {
			return null;
		}
		return assembleAngebotpositionDto(angebotposition);
	}

	public AngebotpositionDto[] angebotpositionFindByAngebotIId(
			Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return angebotpositionFindByAngebotIId(iIdAngebotI);
	}

	public AngebotpositionDto[] angebotpositionFindByAngebotIId(
			Integer iIdAngebotI) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}

		myLogger.logData(iIdAngebotI.toString());
		AngebotpositionDto[] aAngebotpositionDto = null;
		Query query = em.createNamedQuery("AngebotpositionfindByAngebotIId");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();
		aAngebotpositionDto = assembleAngebotpositionDtos(cl);
		// myLogger.exit("Anzahl Positionen: " + aAngebotpositionDto.length);
		return aAngebotpositionDto;
	}

	public AngebotpositionDto[] angebotpositionFindByPositionIId(
			Integer iIPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AngebotpositionDto[] aAngebotpositionDto = null;
		Query query = em.createNamedQuery("AngebotpositionfindByPositionIId");
		query.setParameter(1, iIPositionI);
		Collection<?> cl = query.getResultList();
		aAngebotpositionDto = assembleAngebotpositionDtos(cl);
		return aAngebotpositionDto;
	}

	public AngebotpositionDto[] angebotpositionFindByAngebotIIdOhneAlternative(
			Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdAngebotI.toString());
		AngebotpositionDto[] aAngebotpositionDto = null;
		Query query = em
				.createNamedQuery("AngebotpositionfindByAngebotIIdOhneAlternative");
		query.setParameter(1, iIdAngebotI);
		Collection<?> cl = query.getResultList();

		List<Angebotposition> lOhneKalkulatorische = new ArrayList<Angebotposition>();
		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Angebotposition angebotposition = (Angebotposition) iterator.next();
			if (angebotposition.getArtikelIId() != null) {
				Artikel a = em.find(Artikel.class,
						angebotposition.getArtikelIId());
				if (Helper.short2boolean(a.getBKalkulatorisch())) {
					continue;
				}
			}

			lOhneKalkulatorische.add(angebotposition);
		}

		aAngebotpositionDto = assembleAngebotpositionDtos(lOhneKalkulatorische);
		myLogger.exit("Anzahl Positionen: " + aAngebotpositionDto.length);
		return aAngebotpositionDto;
	}

	private void setAngebotpositionFromAngebotpositionDto(
			Angebotposition angebotposition,
			AngebotpositionDto angebotpositionDto) {
		angebotposition.setAngebotIId(angebotpositionDto.getBelegIId());
		angebotposition.setISort(angebotpositionDto.getISort());
		angebotposition.setAngebotpositionartCNr(angebotpositionDto
				.getPositionsartCNr());
		angebotposition.setArtikelIId(angebotpositionDto.getArtikelIId());
		angebotposition.setCBez(angebotpositionDto.getCBez());
		angebotposition.setCZbez(angebotpositionDto.getCZusatzbez());
		angebotposition.setNGestehungspreis(angebotpositionDto
				.getNGestehungspreis());
		angebotposition.setXTextinhalt(angebotpositionDto.getXTextinhalt());
		angebotposition.setMediastandardIId(angebotpositionDto
				.getMediastandardIId());
		angebotposition.setNMenge(angebotpositionDto.getNMenge());
		angebotposition.setEinheitCNr(angebotpositionDto.getEinheitCNr());
		angebotposition.setFRabattsatz(angebotpositionDto.getFRabattsatz());
		angebotposition.setBRabattsatzuebersteuert(angebotpositionDto
				.getBRabattsatzuebersteuert());
		angebotposition.setBNettopreisuebersteuert(angebotpositionDto
				.getBNettopreisuebersteuert());
		angebotposition.setFZusatzrabattsatz(angebotpositionDto
				.getFZusatzrabattsatz());
		angebotposition.setMwstsatzIId(angebotpositionDto.getMwstsatzIId());
		angebotposition.setBMwstsatzuebersteuert(angebotpositionDto
				.getBMwstsatzuebersteuert());
		angebotposition.setNNettoeinzelpreis(angebotpositionDto
				.getNEinzelpreis());
		angebotposition
				.setNNettoeinzelpreisplusversteckteraufschlag(angebotpositionDto
						.getNEinzelpreisplusversteckteraufschlag());
		angebotposition.setNRabattbetrag(angebotpositionDto.getNRabattbetrag());
		angebotposition.setNNettogesamtpreis(angebotpositionDto
				.getNNettoeinzelpreis());
		angebotposition
				.setNNettogesamtpreisplusversteckteraufschlag(angebotpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlag());
		angebotposition
				.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(angebotpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		angebotposition.setNMwstbetrag(angebotpositionDto.getNMwstbetrag());
		angebotposition.setNBruttogesamtpreis(angebotpositionDto
				.getNBruttoeinzelpreis());
		angebotposition.setAgstklIId(angebotpositionDto.getAgstklIId());
		angebotposition
				.setNGesamtwertagstklinangebotswaehrung(angebotpositionDto
						.getNGesamtwertagstklinangebotswaehrung());
		angebotposition.setBAlternative(angebotpositionDto.getBAlternative());
		angebotposition.setPositionIId(angebotpositionDto.getPositioniId());
		angebotposition.setTypCNr(angebotpositionDto.getTypCNr());
		angebotposition.setPositionIIdArtikelset(angebotpositionDto
				.getPositioniIdArtikelset());
		angebotposition.setVerleihIId(angebotpositionDto.getVerleihIId());
		angebotposition.setKostentraegerIId(angebotpositionDto
				.getKostentraegerIId());
		angebotposition.setZwsVonPosition(angebotpositionDto
				.getZwsVonPosition());
		angebotposition.setZwsBisPosition(angebotpositionDto
				.getZwsBisPosition());
		angebotposition.setZwsNettoSumme(angebotpositionDto.getZwsNettoSumme());
		if (angebotpositionDto.getBZwsPositionspreisZeigen() != null) {
			angebotposition.setBZwsPositionspreisZeigen(angebotpositionDto
					.getBZwsPositionspreisZeigen());
		} else {
			angebotposition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
		}
		angebotposition.setCLvposition(angebotpositionDto.getCLvposition());
		angebotposition.setNMaterialzuschlag(angebotpositionDto
				.getNMaterialzuschlag());
		angebotposition.setLieferantIId(angebotpositionDto.getLieferantIId());
		angebotposition.setNEinkaufpreis(angebotpositionDto.getNEinkaufpreis());

		angebotposition.setNMaterialzuschlagKurs(angebotpositionDto
				.getNMaterialzuschlagKurs());
		angebotposition.setTMaterialzuschlagDatum(angebotpositionDto
				.getTMaterialzuschlagDatum());

		em.merge(angebotposition);
		em.flush();
	}

	private AngebotpositionDto assembleAngebotpositionDto(
			Angebotposition angebotposition) {
		return AngebotpositionDtoAssembler.createDto(angebotposition);
	}

	private AngebotpositionDto[] assembleAngebotpositionDtos(
			Collection<?> angebotpositions) {
		List<AngebotpositionDto> list = new ArrayList<AngebotpositionDto>();
		if (angebotpositions != null) {
			Iterator<?> iterator = angebotpositions.iterator();
			while (iterator.hasNext()) {
				Angebotposition angebotposition = (Angebotposition) iterator
						.next();
				list.add(assembleAngebotpositionDto(angebotposition));
			}
		}
		AngebotpositionDto[] returnArray = new AngebotpositionDto[list.size()];
		return list.toArray(returnArray);
	}

	private void checkAngebotpositionDto(AngebotpositionDto angebotpositionDtoI)
			throws EJBExceptionLP {
		if (angebotpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("angebotpositionI == null"));
		}

		myLogger.info("AngebotpositionDto: " + angebotpositionDtoI.toString());
	}

	private void checkAngebotpositionIId(Integer iIdAngebotpositionI)
			throws EJBExceptionLP {
		if (iIdAngebotpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdAngebotpositionI == null"));
		}

		myLogger.info("AngebotpositionIId: " + iIdAngebotpositionI.toString());
	}

	public AngebotpositionDto angebotpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI) {
		AngebotpositionDto angebotpositionDto = null;
		try {
			angebotpositionDto = angebotpositionFindByAngebotIIdAngebotpositionsartCNr(
					iIdAngebotI, positionsartCNrI);
		} catch (EJBExceptionLP ex) {
			// do nothing
		}
		return angebotpositionDto;
	}

	public AngebotpositionDto angebotpositionFindByAngebotIIdAngebotpositionsartCNr(
			Integer iIdAngebotI, String positionsartCNrI) throws EJBExceptionLP {
		AngebotpositionDto angebotpositionDto = null;
		try {
			Query query = em
					.createNamedQuery("AngebotpositionfindByAngebotIIdAngebotpositionsartCNr");
			query.setParameter(1, iIdAngebotI);
			query.setParameter(2, positionsartCNrI);
			Angebotposition angebotposition = (Angebotposition) query
					.getSingleResult();
			angebotpositionDto = assembleAngebotpositionDto(angebotposition);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler. Es gibt mehere Angebotspositionen mit positionsart "
							+ positionsartCNrI + " fuer angebotIId "
							+ iIdAngebotI);
		}
		return angebotpositionDto;
	}

	public AngebotpositionDto angebotpositionFindByAngebotIIdISort(
			Integer iIdAngebotI, Integer iSort) throws EJBExceptionLP {
		AngebotpositionDto angebotpositionDto = null;
		try {
			Query query = em
					.createNamedQuery("AngebotpositionfindByAngebotIIdISort");
			query.setParameter(1, iIdAngebotI);
			query.setParameter(2, iSort);
			Angebotposition angebotposition = (Angebotposition) query
					.getSingleResult();
			angebotpositionDto = assembleAngebotpositionDto(angebotposition);
		} catch (NoResultException ex) {
			return null;
		}
		return angebotpositionDto;
	}

	private void pruefePflichtfelderBelegposition(AngebotpositionDto agPosDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDtoVerkauf(agPosDto, theClientDto);

		if (agPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
				|| agPosDto.getPositionsartCNr().equals(
						LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (agPosDto.getBAlternative() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("belegPosDto.getBAlternative() == null"));
			}
		}
	}

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		wert = getBelegVerkaufFac().getWertPoisitionsartPosition(iIdPositionI,
				LocaleFac.BELEGART_ANGEBOT, theClientDto);
		/*
		 * Session session = null; try { SessionFactory factory =
		 * FLRSessionFactory.getFactory(); session = factory.openSession();
		 * Criteria crit = session.createCriteria(FLRAngebotposition.class);
		 * crit.add(Restrictions.eq("position_i_id",iIdPositionI)); List<?> l =
		 * crit.list(); Iterator<?> iter = l.iterator(); while(iter.hasNext()){
		 * FLRAngebotposition pos = (FLRAngebotposition) iter.next();
		 * if(pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT) ||
		 * pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
		 * ){ wert =wert.add(pos.getN_menge().multiply(pos.
		 * getN_nettogesamtpreisplusversteckteraufschlagminusrabatte())); } else
		 * if
		 * (pos.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)){
		 * if(pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_BEGINN))
		 * if(pos.getPosition_i_id() != null){ BigDecimal posWert = new
		 * BigDecimal(0); session = factory.openSession(); Criteria critPosition
		 * = session.createCriteria(FLRAngebotposition.class);
		 * critPosition.add(Restrictions.eq("position_i_id",pos.getI_id()));
		 * List<?> posList = critPosition.list(); for (Iterator<?> ipos =
		 * posList.iterator(); ipos.hasNext(); ) { FLRAngebotposition item =
		 * (FLRAngebotposition) ipos.next();
		 * if(!pos.getPositionart_c_nr().equals
		 * (LocaleFac.POSITIONSART_POSITION)){ posWert =
		 * posWert.add(item.getN_menge().multiply(item.
		 * getN_nettogesamtpreisplusversteckteraufschlagminusrabatte())); } }
		 * wert = wert.add(posWert); } } } } finally { if (session != null) {
		 * session.close(); } }
		 */
		return wert;
	}

	// public Integer getPositionNummer(Integer positionIId) {
	// Hashtable<Integer, Integer> nrn = new Hashtable<Integer, Integer>();
	// Integer nr = new Integer(1);
	// Angebotposition ap = em.find(Angebotposition.class, positionIId);
	// try {
	// Query query = em
	// .createNamedQuery("AngebotpositionfindByAngebotIId");
	// query.setParameter(1, ap.getAngebotIId());
	// Collection<Angebotposition> cl = query.getResultList();
	// Iterator<?> iterator = cl.iterator();
	// while (iterator.hasNext()) {
	// Angebotposition pos = (Angebotposition) iterator.next();
	// if (pos.getPositionIId() == null) {
	// if (pos.getAngebotpositionartCNr().equals(
	// LocaleFac.POSITIONSART_POSITION)
	// && pos.getCZbez().equals(
	// LocaleFac.POSITIONBEZ_BEGINN)) {
	// nrn.put(pos.getIId(), nr);
	// nr++;
	// } else if (pos.getAngebotpositionartCNr().equals(
	// LocaleFac.POSITIONSART_IDENT)
	// && pos.getTypCNr() == null
	// && pos.getPositionIIdArtikelset() == null) {
	// if (!Helper.short2boolean(pos.getBAlternative())) {
	// nrn.put(pos.getIId(), nr);
	// nr++;
	// }
	// } else if (pos.getAngebotpositionartCNr().equals(
	// LocaleFac.POSITIONSART_HANDEINGABE)
	// && pos.getTypCNr() == null) {
	// nrn.put(pos.getIId(), nr);
	// nr++;
	// } else if (pos.getAngebotpositionartCNr().equals(
	// LocaleFac.POSITIONSART_AGSTUECKLISTE)
	// && pos.getTypCNr() == null) {
	// nrn.put(pos.getIId(), nr);
	// nr++;
	// }
	//
	// }
	// }
	// } catch (NoResultException er) {
	// return null;
	// }
	// return nrn.get(positionIId);
	// }

	public Integer getPositionNummer(Integer auftragpositionIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionNummer(auftragpositionIId,
				new AngebotPositionNumberAdapter(em));
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param auftragIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer auftragIId,
			Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionIIdFromPositionNummer(auftragIId,
				position, new AngebotPositionNumberAdapter(em));
	}

	public Integer getLastPositionNummer(Integer auftragposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(auftragposIId,
				new AngebotPositionNumberAdapter(em));
	}

	public Integer getHighestPositionNumber(Integer auftragIId)
			throws EJBExceptionLP {
		AngebotpositionDto angebotposDtos[] = angebotpositionFindByAngebotIId(auftragIId);
		if (angebotposDtos.length == 0)
			return 0;

		return getLastPositionNummer(angebotposDtos[angebotposDtos.length - 1]
				.getIId());
	}

	public boolean pruefeAufGleichenMwstSatz(Integer auftragIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		AngebotpositionDto dtos[] = angebotpositionFindByAngebotIId(auftragIId);
		return getBelegVerkaufFac().pruefeAufGleichenMwstSatz(dtos,
				vonPositionNumber, bisPositionNumber);
	}
}
