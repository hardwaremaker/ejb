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
package com.lp.server.lieferschein.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.ejb.AuftragpositionQuery;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.ejb.LieferscheinpositionQuery;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.LieferscheinPositionNumberAdapter;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.PositionNumberHandlerLieferschein;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LieferscheinpositionFacBean extends Facade implements
		LieferscheinpositionFac, IPrimitiveSwapper {
	@PersistenceContext
	private EntityManager em;

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session
					.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq(
					AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID,
					iIdPositionI));
			crit.addOrder(Order.asc("i_sort"));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter
						.next();
				if (pos.getPositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)
						|| pos.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE)) {
					wert = wert
							.add(pos.getN_menge()
									.multiply(
											pos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()));
				} else if (pos.getPositionsart_c_nr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_POSITION)) {
					if (pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_BEGINN))
						if (pos.getPosition_i_id() != null) {
							BigDecimal posWert = new BigDecimal(0);
							session = factory.openSession();
							Criteria critPosition = session
									.createCriteria(FLRLieferscheinposition.class);
							critPosition
									.add(Restrictions
											.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID,
													pos.getI_id()));
							List<?> posList = critPosition.list();
							for (Iterator<?> ipos = posList.iterator(); ipos
									.hasNext();) {
								FLRLieferscheinposition item = (FLRLieferscheinposition) ipos
										.next();
								if (!pos.getPositionsart_c_nr().equals(
										LocaleFac.POSITIONSART_IDENT)) {
									posWert = posWert
											.add(item
													.getN_menge()
													.multiply(
															item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()));
								}
							}
							wert = wert.add(posWert);
						}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return wert;
	}

	private BigDecimal getGestpreisFuerRuecklieferung(
			LieferscheinpositionDto oDtoI, Integer lagerIId,
			String sSerienchargennummer, TheClientDto theClientDto) {
		BigDecimal gestpreis = new BigDecimal(0);
		try {
			gestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
					oDtoI.getArtikelIId(), lagerIId, theClientDto);

			if (oDtoI.getAuftragpositionIId() != null) {

				Integer auftragIId = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								oDtoI.getAuftragpositionIId()).getBelegIId();
				// Nachsehen
				Query query = em
						.createNamedQuery("AuftragpositionfindByAuftragIIdArtikelIId");
				query.setParameter(1, auftragIId);
				query.setParameter(2, oDtoI.getArtikelIId());
				Collection c = query.getResultList();

				if (c.size() > 0) {
					Auftragposition p = (Auftragposition) c.iterator().next();

					query = em
							.createNamedQuery("LieferscheinpositionfindPositiveByAuftragpositionIIdArtikelIId");
					query.setParameter(1, p.getIId());
					query.setParameter(2, oDtoI.getArtikelIId());
					c = query.getResultList();
					if (c.size() > 0) {
						Lieferscheinposition l = (Lieferscheinposition) c
								.iterator().next();
						gestpreis = getLagerFac()
								.getGestehungspreisEinerAbgangsposition(
										LocaleFac.BELEGART_LIEFERSCHEIN,
										l.getIId(), sSerienchargennummer);
					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return gestpreis;

	}

	public Integer createLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return createLieferscheinposition(lieferscheinpositionDtoI,
				bArtikelSetAufloesen,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
	}

	/**
	 * Eine neue Lieferscheinposition anlegen. <br>
	 * Preisinformationen werden in Lieferscheinwaehrung abgelegt.
	 * <ul>
	 * <li>PK generieren.
	 * <li>Bei Positionsart Handeingabe einen Handartikel anlegen, wenn es noch
	 * keinen gibt.
	 * <li>Lieferscheinstatus anpassen.
	 * <li>Lieferscheinposition anlegen und die zusaetzlichen Preisfelder
	 * befuellen.
	 * <li>Bei Bezug auf Auftrag die Auftragreservierung anpassen.
	 * <li>Bei Positionsart Ident eine Lagerbuchung ausloesen.
	 * </ul>
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Integer PK der neuen Lieferscheinposition
	 * @throws RemoteException 
	 */
	public Integer createLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		pruefePflichtfelderBelegposition(lieferscheinpositionDtoI, theClientDto);

		if (lieferscheinpositionDtoI.getPositionsartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)
				&& lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
						lieferscheinpositionDtoI.getBelegIId(),
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME) != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
					new Exception("Eine Position Endsumme existiert bereits."));
		}

		// PK generieren
		Integer iIdLieferscheinposition = getPKGeneratorObj()
				.getNextPrimaryKey(PKConst.PK_LIEFERSCHEINPOSITION);
		lieferscheinpositionDtoI.setIId(iIdLieferscheinposition);

		// Sortierung: falls nicht anders definiert, hinten dran haengen.
		if (lieferscheinpositionDtoI.getISort() == null) {
			int iSortNeu = getMaxISort(lieferscheinpositionDtoI.getBelegIId()) + 1;
			lieferscheinpositionDtoI.setISort(iSortNeu);
		}

		if (lieferscheinpositionDtoI.getBKeinlieferrest() == null) {
			lieferscheinpositionDtoI.setBKeinlieferrest(new Short((short) 0));
		}

		// wenn es sich um eine Handeingabeposition handelt und kein bestehender
		// Artikel mitgelifert wird, muss ein Handartikel angelegt werden
		if (lieferscheinpositionDtoI.getLieferscheinpositionartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
				&& lieferscheinpositionDtoI.getArtikelIId() == null) {
			Integer iIdArtikel = null;

			try {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(lieferscheinpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(lieferscheinpositionDtoI
						.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(lieferscheinpositionDtoI
						.getEinheitCNr());

				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								lieferscheinpositionDtoI.getMwstsatzIId(),
								theClientDto);
				oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Handartikel anlegen
				iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
						theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			lieferscheinpositionDtoI.setArtikelIId(iIdArtikel);
		}

		LieferscheinpositionDto vorherigelieferscheinpositionDtoI = null;
		try {
			int iSort = getMaxISort(lieferscheinpositionDtoI.getBelegIId());
			if (lieferscheinpositionDtoI.getISort() != null) {
				iSort = lieferscheinpositionDtoI.getISort() - 1;
			}
			Query query = em
					.createNamedQuery("LieferscheinpositionfindByLieferscheinISort");
			query.setParameter(1, lieferscheinpositionDtoI.getLieferscheinIId());
			query.setParameter(2, iSort);
			vorherigelieferscheinpositionDtoI = assembleLieferscheinpositionDto((Lieferscheinposition) query
					.getSingleResult());

		} catch (NoResultException ex1) {
		}
		lieferscheinpositionDtoI = (LieferscheinpositionDto) befuellepositionBelegpositionDtoVerkauf(
				vorherigelieferscheinpositionDtoI, lieferscheinpositionDtoI,
				theClientDto);

		if (lieferscheinpositionDtoI.getPositioniId() == null
				&& lieferscheinpositionDtoI.getTypCNr() != null) {
			if (!lieferscheinpositionDtoI.getLieferscheinpositionartCNr()
					.equals(LocaleFac.POSITIONSART_POSITION))
				lieferscheinpositionDtoI = befuelleZusaetzlichePositionfelder(
						lieferscheinpositionDtoI, theClientDto);
		}

		if (lieferscheinpositionDtoI.getTypCNr() != null
				&& (lieferscheinpositionDtoI.getTypCNr().equals(
						LocaleFac.POSITIONTYP_EBENE1) || lieferscheinpositionDtoI
						.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2))
				&& lieferscheinpositionDtoI.getPositioniId() != null) {
			LieferscheinpositionDto[] dtos = lieferscheinpositionFindByPositionIId(lieferscheinpositionDtoI
					.getPositioniId());
			for (int i = 0; i < dtos.length; i++) {
				if (lieferscheinpositionDtoI.getMwstsatzIId() != null) {
					if (dtos[i].getMwstsatzIId() != null) {
						if (!lieferscheinpositionDtoI.getMwstsatzIId().equals(
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

		// die Lieferscheinposition anlegen
		Lieferscheinposition lieferscheinposition = null;

		try {

			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {

				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								lieferscheinpositionDtoI
										.getAuftragpositionIId());
				if (lieferscheinpositionDtoI.getXTextinhalt() == null) {
					lieferscheinpositionDtoI.setXTextinhalt(auftragpositionDto
							.getXTextinhalt());
				}
			}

			getLieferscheinFac().pruefeUndSetzeLieferscheinstatusBeiAenderung(
					lieferscheinpositionDtoI.getLieferscheinIId());

			lieferscheinposition = new Lieferscheinposition(
					lieferscheinpositionDtoI.getIId(),
					lieferscheinpositionDtoI.getLieferscheinIId(),
					lieferscheinpositionDtoI.getISort(),
					lieferscheinpositionDtoI.getLieferscheinpositionartCNr(),
					lieferscheinpositionDtoI.getBNettopreisuebersteuert(),
					lieferscheinpositionDtoI.getBKeinlieferrest());
			em.persist(lieferscheinposition);
			em.flush();

			setLieferscheinpositionFromLieferscheinpositionDto(
					lieferscheinposition, lieferscheinpositionDtoI);

			befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(),
					theClientDto);
			lieferscheinpositionDtoI
					.setNEinzelpreisplusversteckteraufschlag(lieferscheinposition
							.getNNettoeinzelpreisplusversteckteraufschlag());
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlag(lieferscheinposition
							.getNNettogesamtpreisplusversteckteraufschlag());
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(lieferscheinposition
							.getNNettogesamtpreisplusversteckteraufschlagminusrabatt());

			// IMS 2129
			if (lieferscheinpositionDtoI.getNMenge() != null
					&& lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
				// Wenn es einen Bezug zum Auftrag gibt, muss die
				// Auftragposition angepasst werden
				if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
					getAuftragpositionFac().updateOffeneMengeAuftragposition(
							lieferscheinpositionDtoI.getAuftragpositionIId(),
							theClientDto);
				}
				// Wenn es sich um eine Artikelposition handelt, muss
				// eine neue Lagerbuchung gemacht werden. Es gilt: Handartikel
				// sind nicht lagerbewirtschaftet.
				if (lieferscheinpositionDtoI
						.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bucheAbLager(lieferscheinpositionDtoI, theClientDto);
				}
			} else if (lieferscheinpositionDtoI.getNMenge() != null
					&& lieferscheinpositionDtoI.getNMenge().doubleValue() < 0) {
				// Wenn es einen Bezug zum Auftrag gibt, muss die
				// Auftragposition angepasst werden
				if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
					getAuftragpositionFac().updateOffeneMengeAuftragposition(
							lieferscheinpositionDtoI.getAuftragpositionIId(),
							theClientDto);
				}
				if (lieferscheinpositionDtoI
						.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bucheZuLager(lieferscheinpositionDtoI, theClientDto);
				}
			} /*
			 * lt. WH 16.1.2013 else { if
			 * (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
			 * AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
			 * .auftragpositionFindByPrimaryKey( lieferscheinpositionDtoI
			 * .getAuftragpositionIId()); auftragpositionDto
			 * .setAuftragpositionstatusCNr
			 * (AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
			 * getAuftragpositionFac() .updateAuftragpositionOhneWeitereAktion(
			 * auftragpositionDto, theClientDto); } }
			 */

			sortierungAnpassenInBezugAufEndsumme(
					lieferscheinpositionDtoI.getBelegIId(), theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikel ebenfalls
			// buchen:
			if (bArtikelSetAufloesen == true
					&& lieferscheinpositionDtoI.getArtikelIId() != null) {

				resolveArtikelset(lieferscheinpositionDtoI, identities,
						theClientDto);
			}
			
			getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return lieferscheinpositionDtoI.getIId();
	}

	private void resolveArtikelset(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						lieferscheinpositionDtoI.getArtikelIId(), theClientDto);
		if (stklDto == null)
			return;
		if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto
				.getStuecklisteartCNr()))
			return;

		Integer artikelsetHeadIIdLieferscheinPositionIId = lieferscheinpositionDtoI
				.getIId();

		Integer auftragpositionIId = lieferscheinpositionDtoI
				.getAuftragpositionIId();
		if (auftragpositionIId != null) {
			resolveArtikelsetFromAuftragArtikelset(lieferscheinpositionDtoI,
					identities, auftragpositionIId, theClientDto);
		} else {
			resolveArtikelsetFromStueckliste(lieferscheinpositionDtoI,
					identities, stklDto.getIId(), theClientDto);
		}

		preiseEinesArtikelsetsUpdaten(artikelsetHeadIIdLieferscheinPositionIId,
				theClientDto);
	}

	private void resolveArtikelsetFromStueckliste(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			Integer stuecklisteIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lieferscheinPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);

		List<?> m = null;
		try {
			m = getStuecklisteFac()
					.getStrukturDatenEinerStueckliste(
							stuecklisteIId,
							theClientDto,
							StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
							0, null, false, false,
							lieferscheinpositionDtoI.getNMenge(), null, true);
		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		Iterator<?> it = m.listIterator();
		List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
				identities);

		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
					.next();
			StuecklistepositionDto position = struktur
					.getStuecklistepositionDto();

			lieferscheinpositionDtoI.setNEinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNMwstbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNRabattbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNBruttoeinzelpreis(BigDecimal.ZERO);

			lieferscheinpositionDtoI
					.setNMenge(Helper.rundeKaufmaennisch(
							position.getNZielmenge().multiply(
									lieferscheinPositionDtoKopfartikel
											.getNMenge()), 4));

			lieferscheinpositionDtoI.setArtikelIId(position.getArtikelIId());
			lieferscheinpositionDtoI.setEinheitCNr(position.getEinheitCNr());
			lieferscheinpositionDtoI
					.setPositioniIdArtikelset(lieferscheinPositionDtoKopfartikel
							.getIId());
			lieferscheinpositionDtoI.setIId(null);

			int iSort = lieferscheinpositionDtoI.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
					lieferscheinpositionDtoI.getLieferscheinIId(), iSort);

			lieferscheinpositionDtoI.setISort(iSort);
			lieferscheinpositionDtoI.setSeriennrChargennrMitMenge(null);
			lieferscheinpositionDtoI.setIId(null);

			getBelegVerkaufFac().setupPositionWithIdentities(
					lieferscheinpositionDtoI, notyetUsedIdentities,
					theClientDto);

			createLieferscheinposition(lieferscheinpositionDtoI, false,
					theClientDto);
		}
	}

	private void resolveArtikelsetFromAuftragArtikelset(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			Integer auftragpositionIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lieferscheinPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);

		Auftragposition headAuftragposition = em.find(Auftragposition.class,
				auftragpositionIId);
		BigDecimal auftragposHeadMenge = headAuftragposition.getNMenge();
		BigDecimal lsposHeadMenge = lieferscheinPositionDtoKopfartikel
				.getNMenge();

		List<Auftragposition> auftragpositionen = AuftragpositionQuery
				.listByPositionIIdArtikelset(em, auftragpositionIId);

		for (Auftragposition auftragposition : auftragpositionen) {
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
					identities);

			lieferscheinpositionDtoI.setNEinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNMwstbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNRabattbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			lieferscheinpositionDtoI
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNBruttoeinzelpreis(BigDecimal.ZERO);

			BigDecimal m = auftragposition.getNMenge().multiply(lsposHeadMenge)
					.divide(auftragposHeadMenge, 4, BigDecimal.ROUND_HALF_EVEN);
			lieferscheinpositionDtoI.setNMenge(m);
			lieferscheinpositionDtoI.setArtikelIId(auftragposition
					.getArtikelIId());
			lieferscheinpositionDtoI.setEinheitCNr(auftragposition
					.getEinheitCNr());
			lieferscheinpositionDtoI.setAuftragpositionIId(auftragposition
					.getIId());
			lieferscheinpositionDtoI
					.setPositioniIdArtikelset(lieferscheinPositionDtoKopfartikel
							.getIId());
			lieferscheinpositionDtoI.setIId(null);

			int iSort = lieferscheinpositionDtoI.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
					lieferscheinpositionDtoI.getLieferscheinIId(), iSort);

			lieferscheinpositionDtoI.setISort(iSort);
			lieferscheinpositionDtoI.setSeriennrChargennrMitMenge(null);
			lieferscheinpositionDtoI.setIId(null);
			getBelegVerkaufFac().setupPositionWithIdentities(
					lieferscheinpositionDtoI, notyetUsedIdentities,
					theClientDto);

			createLieferscheinposition(lieferscheinpositionDtoI, false,
					theClientDto);
		}
	}

	private boolean hasSeriennrchargennr(List<SeriennrChargennrMitMengeDto> snrs) {
		if (null == snrs)
			return false;
		if (snrs.size() == 0)
			return false;

		if (snrs.size() > 1)
			return true;

		List<GeraetesnrDto> geraete = snrs.get(0).getAlGeraetesnr();
		if (geraete != null && geraete.size() > 0)
			return true;

		return snrs.get(0).getCSeriennrChargennr() != null;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(
			Integer lieferscheinposIId) throws EJBExceptionLP {
		if (lieferscheinposIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferscheinposIId == null"));
		}

		List<SeriennrChargennrMitMengeDto> allSnrs = new ArrayList<SeriennrChargennrMitMengeDto>();
		List<Lieferscheinposition> positions = LieferscheinpositionQuery
				.listByPositionIIdArtikelset(em, lieferscheinposIId);
		for (Lieferscheinposition lieferscheinposition : positions) {
			List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartposition(
							LocaleFac.BELEGART_LIEFERSCHEIN,
							lieferscheinposition.getIId());
			if (hasSeriennrchargennr(snrs)) {
				allSnrs.addAll(snrs);
			}
		}

		return allSnrs;
	}

	public LieferscheinpositionDto befuelleZusaetzlichePositionfelder(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
			Session session = null;
			// try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			FLRAuftragposition identAuftragposition = (FLRAuftragposition) session
					.load(FLRAuftragposition.class,
							lieferscheinpositionDtoI.getAuftragpositionIId());
			FLRAuftragposition position = (FLRAuftragposition) session.load(
					FLRAuftragposition.class,
					identAuftragposition.getPosition_i_id());
			int ipsort = 0;
			ipsort = identAuftragposition.getI_sort() - position.getI_sort();
			Criteria crit = session
					.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq("auftragposition_i_id", position.getI_id()));
			crit.add(Restrictions.eq("flrlieferschein.i_id",
					lieferscheinpositionDtoI.getLieferscheinIId()));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			if (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter
						.next();
				lieferscheinpositionDtoI.setPositioniId(pos.getI_id());
				lieferscheinpositionDtoI.setISort(pos.getI_sort() + ipsort);
				Query query = em
						.createNamedQuery("LieferscheinpositionfindByLieferschein");
				query.setParameter(1,
						lieferscheinpositionDtoI.getLieferscheinIId());
				Collection<?> cl = query.getResultList();
				// if (cl.isEmpty()) {
				// return lieferscheinpositionDtoI;
				// }
				Iterator<?> it = cl.iterator();
				while (it.hasNext()) {
					Lieferscheinposition oPosition = (Lieferscheinposition) it
							.next();
					if (oPosition.getISort().intValue() > lieferscheinpositionDtoI
							.getISort()) {
						oPosition.setISort(oPosition.getISort() + 1);
					}
				}

				sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
						lieferscheinpositionDtoI.getLieferscheinIId(),
						lieferscheinpositionDtoI.getISort());
				getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
				return lieferscheinpositionDtoI;
			}
			// }
			// catch (FinderException ex) {
			// return lieferscheinpositionDtoI;
			// }

			if (session != null) {
				session.close();
			}
		}
		return lieferscheinpositionDtoI;
	}

	/**
	 * Fuer eine bestehende Lieferscheinposition vom Typ Ident oder Handeingabe
	 * werden die zusaetzlichen Preisfelder befuellt.
	 * 
	 * @param iIdPositionI
	 *            PK der Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws EJBExceptionLP
	 */
	public LieferscheinpositionDto befuelleZusaetzlichePreisfelder(
			Integer iIdPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			Lieferscheinposition oPosition = em.find(
					Lieferscheinposition.class, iIdPositionI);
			if (oPosition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByPrimaryKey(
					iIdPositionI, theClientDto);
			if (oPosition
					.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
					|| oPosition
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

				LieferscheinDto lieferscheinDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(
								oPosition.getLieferscheinIId(), theClientDto);
				lieferscheinpositionDto = (LieferscheinpositionDto) getBelegVerkaufFac()
						.berechneBelegpositionVerkauf(lieferscheinpositionDto,
								lieferscheinDto);

				oPosition
						.setNNettoeinzelpreisplusversteckteraufschlag(lieferscheinpositionDto
								.getNEinzelpreisplusversteckteraufschlag());
				oPosition
						.setNNettogesamtpreisplusversteckteraufschlag(lieferscheinpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlag());
				oPosition
						.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(lieferscheinpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

				/*
				 * // den versteckten Aufschlag aus den Konditionen
				 * beruecksichtigen BigDecimal bdVersteckterAufschlag = new
				 * BigDecimal(ls. getFVersteckteraufschlag().
				 * doubleValue()).movePointLeft(2); bdVersteckterAufschlag =
				 * Helper.rundeKaufmaennisch(bdVersteckterAufschlag, 4);
				 * 
				 * BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme =
				 * oPosition
				 * .getNNettoeinzelpreis().multiply(bdVersteckterAufschlag);
				 * bdNettoeinzelpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettoeinzelpreisVersteckterAufschlagSumme, 4);
				 * 
				 * 
				 * 
				 * oPosition.setNNettoeinzelpreisplusversteckteraufschlag(oPosition
				 * . getNNettoeinzelpreis(). add(
				 * bdNettoeinzelpreisVersteckterAufschlagSumme));
				 * 
				 * BigDecimal bdNettogesamtpreisVersteckterAufschlagSumme =
				 * oPosition
				 * .getNNettogesamtpreis().multiply(bdVersteckterAufschlag);
				 * bdNettogesamtpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisVersteckterAufschlagSumme, 4);
				 * oPosition.setNNettogesamtpreisplusversteckteraufschlag
				 * (oPosition. getNNettogesamtpreis(). add(
				 * bdNettogesamtpreisVersteckterAufschlagSumme));
				 * 
				 * // die Abschlaege werden auf Basis des Versteckten Aufschlags
				 * beruecksichtigt
				 * 
				 * // - Allgemeiner Rabatt BigDecimal bdAllgemeinerRabatt = new
				 * BigDecimal(ls. getFAllgemeinerrabatt().
				 * doubleValue()).movePointLeft(2); bdAllgemeinerRabatt =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);
				 * 
				 * BigDecimal bdNettogesamtpreisAllgemeinerRabattSumme =
				 * oPosition
				 * .getNNettogesamtpreisplusversteckteraufschlag().multiply(
				 * bdAllgemeinerRabatt);
				 * bdNettogesamtpreisAllgemeinerRabattSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisAllgemeinerRabattSumme, 4);
				 * 
				 * 
				 * 
				 * 
				 * oPosition.setNNettogesamtpreisplusversteckteraufschlagminusrabatt
				 * (oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlag().subtract(
				 * bdNettogesamtpreisAllgemeinerRabattSumme));
				 */
			}

			return lieferscheinpositionDto;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	// IMS 2129
	public void bucheZuLager(LieferscheinpositionDto oDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (oDtoI == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(
								"oDtoI == null"));
			}

			LieferscheinDto oAktuellerLieferschein = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oDtoI.getLieferscheinIId(),
							theClientDto);
			Integer iMandantenLager = null;
			Integer iKundeLager = null;
			// Kunden Lager
			if (oAktuellerLieferschein.getZiellagerIId() != null) {

				iKundeLager = oAktuellerLieferschein.getZiellagerIId();
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(
						theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1)
							.divide(new BigDecimal(
									oAktuellerLieferschein
											.getFWechselkursmandantwaehrungzubelegwaehrung()
											.doubleValue()), 4,
									BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(),
						oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge().negate(),
						bdNettogesamtpreis, iKundeLager,
						oDtoI.getSeriennrChargennrMitMenge(),
						oAktuellerLieferschein.getTBelegdatum(), theClientDto);

				BigDecimal gestpreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								oDtoI.getArtikelIId(), iMandantenLager,
								theClientDto);

				getLagerFac().bucheZu(LocaleFac.BELEGART_LSZIELLAGER,
						oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge().negate(),
						gestpreis, iMandantenLager,
						oDtoI.getSeriennrChargennrMitMenge(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto, oAktuellerLieferschein.getBelegartCNr(),
						oDtoI.getIId());

			}
			// Mandanten Lager
			else {
				iMandantenLager = oAktuellerLieferschein.getLagerIId();
				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}
				String s = null;

				if (oDtoI.getSeriennrChargennrMitMenge() != null
						&& oDtoI.getSeriennrChargennrMitMenge().size() > 0) {
					s = oDtoI.getSeriennrChargennrMitMenge().get(0)
							.getCSeriennrChargennr();
				}

				getLagerFac().bucheZu(
						oAktuellerLieferschein.getBelegartCNr(),
						oAktuellerLieferschein.getIId(),
						oDtoI.getIId(),
						oDtoI.getArtikelIId(),
						oDtoI.getNMenge().negate(),
						getGestpreisFuerRuecklieferung(oDtoI, iMandantenLager,
								s, theClientDto), iMandantenLager,
						oDtoI.getSeriennrChargennrMitMenge(),
						oAktuellerLieferschein.getTBelegdatum(), theClientDto);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Lagerbuchung fuer eine bestimmte Lieferscheinposition machen. <br>
	 * Lagerbuchungen sind absolut, es zaehlt die Menge, die im uebergebenen Dto
	 * enthalten ist.
	 * 
	 * @param oDtoI
	 *            die Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void bucheAbLager(LieferscheinpositionDto oDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (oDtoI == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(
								"oDtoI == null"));
			}

			// die zu buchende SNR- oder Chargennummer pruefen
			List<SeriennrChargennrMitMengeDto> sSerienchargennummer = oDtoI
					.getSeriennrChargennrMitMenge();

			LieferscheinDto oAktuellerLieferschein = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oDtoI.getLieferscheinIId(),
							theClientDto);

			KostenstelleDto kostenstelleDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(
							oAktuellerLieferschein.getKostenstelleIId());

			if (kostenstelleDto.getLagerIIdOhneabbuchung() != null) {
				if (oAktuellerLieferschein.getLagerIId().equals(
						kostenstelleDto.getLagerIIdOhneabbuchung())) {
					// PJ14813
					return;
				}
			}

			Integer iMandantenLager = null;
			Integer iKundeLager = null;
			// Kunden Lager
			if (oAktuellerLieferschein.getZiellagerIId() != null) {
				iKundeLager = oAktuellerLieferschein.getZiellagerIId();
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(
						theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1)
							.divide(new BigDecimal(
									oAktuellerLieferschein
											.getFWechselkursmandantwaehrungzubelegwaehrung()
											.doubleValue()), 4,
									BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(),
						oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge(),
						bdNettogesamtpreis, iMandantenLager,
						sSerienchargennummer,
						oAktuellerLieferschein.getTBelegdatum(), theClientDto);
				BigDecimal gestpreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								oDtoI.getArtikelIId(), iMandantenLager,
								theClientDto);
				getLagerFac().bucheZu(LocaleFac.BELEGART_LSZIELLAGER,
						oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge(), gestpreis,
						iKundeLager, sSerienchargennummer,
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto, oAktuellerLieferschein.getBelegartCNr(),
						oDtoI.getIId());

			}
			// Mandanten Lager
			else {
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(
						theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1)
							.divide(new BigDecimal(
									oAktuellerLieferschein
											.getFWechselkursmandantwaehrungzubelegwaehrung()
											.doubleValue()), 4,
									BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				// absolute Lagerbuchung, funktioniert fuer nicht
				// lagerbewirtschaftete Artikel;
				// es gilt: Lagerbewertete Artikel buchen implizit auf KEIN
				// LAGER
				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(),
						oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge(),
						bdNettogesamtpreis, iMandantenLager,
						sSerienchargennummer,
						oAktuellerLieferschein.getTBelegdatum(), theClientDto);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeLieferscheinpositionen(Object[] idsI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinpositionDto lspos = null;
		Integer pk = null;
		for (int i = 0; i < idsI.length; i++) {
			pk = new Integer(idsI[i].toString());
			lspos = lieferscheinpositionFindByPrimaryKey(pk, theClientDto);
			removeLieferscheinposition(lspos, theClientDto);
		}
	}

	/**
	 * Eine Lieferscheinposition loeschen.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die zu loeschene Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		try {
			getLieferscheinFac().pruefeUndSetzeLieferscheinstatusBeiAenderung(
					lieferscheinpositionDtoI.getLieferscheinIId());

			LieferscheinDto ls = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							lieferscheinpositionDtoI.getLieferscheinIId(),
							theClientDto);

			if (lieferscheinpositionDtoI.getPositioniIdArtikelset() == null) {

				Query query = em
						.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
				query.setParameter(1, lieferscheinpositionDtoI.getIId());
				Collection<?> lieferscheinpositionDtos = query.getResultList();
				LieferscheinpositionDto[] zugehoerigeLSPosDtos = assembleLieferscheinpositionDtos(lieferscheinpositionDtos);

				for (int i = 0; i < zugehoerigeLSPosDtos.length; i++) {
					removeLieferscheinposition(zugehoerigeLSPosDtos[i],
							theClientDto);
				}
			}

			if (ls.getZiellagerIId() != null) {
				storniereLieferscheinpositionZielLager(ls,
						lieferscheinpositionDtoI.getIId(), theClientDto);
			} else {
				// Rollback aller Buchungen, die das Anlegen bzw. Aendern der
				// Lieferscheinposition ausgeloest hat
				storniereLieferscheinposition(
						lieferscheinpositionDtoI.getIId(), theClientDto);
			}
			// jetzt die lieferscheinposition loeschen
			Lieferscheinposition toRemove = em.find(Lieferscheinposition.class,
					lieferscheinpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();

				if (lieferscheinpositionDtoI.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(
							lieferscheinpositionDtoI.getPositioniIdArtikelset(),
							theClientDto);
				}

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			getAuftragpositionFac().updateOffeneMengeAuftragposition(
					lieferscheinpositionDtoI.getAuftragpositionIId(),
					theClientDto);
			sortierungAnpassenBeiLoeschenEinerPosition(
					lieferscheinpositionDtoI.getLieferscheinIId(),
					lieferscheinpositionDtoI.getISort().intValue());
			
			//wegen Aenderungszeitpunkt
			getLieferscheinFac().updateLieferscheinOhneWeitereAktion(ls, theClientDto);
			
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			EJBExceptionLP ex = null;
			if (t instanceof EJBExceptionLP) {
				ex = (EJBExceptionLP) t;
			}
			if (ex != null) {
				EJBExceptionLP toThrow = new EJBExceptionLP(ex);
				throw toThrow;
			}
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}
	}

	/**
	 * Eine Lieferscheinposition stornieren. <br>
	 * Der Storno einer Lieferscheinposition loest ein Rollback der Aktionen
	 * aus, die beim Anlegen bzw. Aendern der Position durchgefuehrt wurden. <br>
	 * Die Lieferscheinposition selbst bleibt mit ihren Informationen bestehen. <br>
	 * Der Storno einer Lieferscheinposition kann nicht aufgehoben werden. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void storniereLieferscheinposition(Integer iIdLieferscheinpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionIId(iIdLieferscheinpositionI);

		LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByPrimaryKey(
				iIdLieferscheinpositionI, theClientDto);

		// IDENT + Bezug zu Auftrag
		// Artikelreservierung anpassen
		// Auftragposition korrigieren
		// Lagerbuchung korrigieren
		// IDENT ohne Bezug Auftrag
		// Lagerbuchung korrigieren
		// HANDEINGABE + Bezug zu Auftrag
		// Auftragposition korrigieren
		// HANDEINGABE ohne Bezug zu Auftrag

		if (lieferscheinpositionDto.getAuftragpositionIId() != null
				&& lieferscheinpositionDto.getNMenge() == null) {
			try {
				AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								lieferscheinpositionDto.getAuftragpositionIId());
				oAuftragpositionDto
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
						oAuftragpositionDto, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		// Schritt 2: Wenn die Lieferscheinposition einen Ident
		// Artikel beinhaltet, muessen weitere Schritte zum Rollback unternommen
		// werden
		if (lieferscheinpositionDto.getArtikelIId() != null) {
			if (lieferscheinpositionDto.getLieferscheinpositionartCNr().equals(
					LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
				// Schritt 2a: die zugehoerige Auftragreseriverung korrigieren,
				// muss VOR
				// Schritt 2b ausgefuehrt werden, weil dort die Menge in
				// lieferscheinpositionDto
				// veraendert wird
				// if (lieferscheinpositionDto.getAuftragpositionIId() != null
				// &&
				// lieferscheinpositionDto.getNMenge().doubleValue() > 0) {
				// rollbackReservierungZuAuftragposition(lieferscheinpositionDto,
				// cNrUserI);
				// }
				if (lieferscheinpositionDto.getNMenge().doubleValue() > 0) {
					// Schritt 2b: die Lagerbuchung rueckgaengig machen;
					// besondere
					// Beruecksichtigung von SNR und Chargennummer
					lieferscheinpositionDto.setNMenge(Helper
							.getBigDecimalNull()); // die Lagerbuchung bezieht
					// sich immer auf eine
					// bestimmte Position!
					bucheAbLager(lieferscheinpositionDto, theClientDto);
				} else if (lieferscheinpositionDto.getNMenge().doubleValue() < 0) {
					lieferscheinpositionDto.setNMenge(Helper
							.getBigDecimalNull()); // die Lagerbuchung bezieht
					// sich immer auf eine
					// bestimmte Position!
					bucheZuLager(lieferscheinpositionDto, theClientDto);
				}

				try {
					if (lieferscheinpositionDto.getAuftragpositionIId() != null) {
						getAuftragpositionFac()
								.updateOffeneMengeAuftragposition(
										lieferscheinpositionDto
												.getAuftragpositionIId(),
										theClientDto);
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

			}
		}
	}

	/**
	 * Eine Lieferscheinposition stornieren. <br>
	 * Der Storno einer Lieferscheinposition loest ein Rollback der Aktionen
	 * aus, die beim Anlegen bzw. Aendern der Position durchgefuehrt wurden. <br>
	 * Die Lieferscheinposition selbst bleibt mit ihren Informationen bestehen. <br>
	 * Der Storno einer Lieferscheinposition kann nicht aufgehoben werden. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param oAktuellerLieferschein
	 *            LieferscheinDto
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void storniereLieferscheinpositionZielLager(
			LieferscheinDto oAktuellerLieferschein,
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinpositionIId(iIdLieferscheinpositionI);
		LieferscheinpositionDto oDtoI = lieferscheinpositionFindByPrimaryKey(
				iIdLieferscheinpositionI, theClientDto);

		try {

			if (oDtoI.getArtikelIId() != null) {
				if (oDtoI.getLieferscheinpositionartCNr().equals(
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					if (oDtoI.getAuftragpositionIId() != null) {
						rollbackReservierungZuAuftragposition(oDtoI,
								theClientDto);
					}
					if (oDtoI.getNMenge().doubleValue() > 0) {
						// lieferscheinpositionDto.setNMenge(Helper.
						// getBigDecimalNull()); // die Lagerbuchung bezieht
						// sich immer auf eine bestimmte Position!

						if (oDtoI == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
									new Exception("oDtoI == null"));
						}

						ArtikelDto oArtikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(oDtoI.getArtikelIId(),
										theClientDto);

						Integer iQuelleLager = null;
						Integer iZielLager = null;

						iZielLager = oAktuellerLieferschein.getLagerIId();
						iQuelleLager = oAktuellerLieferschein.getZiellagerIId();
						// den Nettogesamtpreis in Mandantenwaehrung umrechnen
						BigDecimal bdNettogesamtpreis = oDtoI
								.getNNettoeinzelpreis();

						if (!oAktuellerLieferschein.getWaehrungCNr().equals(
								theClientDto.getSMandantenwaehrung())) {
							BigDecimal ddFaktor = new BigDecimal(1)
									.divide(new BigDecimal(
											oAktuellerLieferschein
													.getFWechselkursmandantwaehrungzubelegwaehrung()
													.doubleValue()), 4,
											BigDecimal.ROUND_HALF_EVEN);

							bdNettogesamtpreis = bdNettogesamtpreis
									.multiply(ddFaktor);
						}

						// absolute Lagerbuchung, funktioniert fuer nicht
						// lagerbewirtschaftete Artikel;
						// es gilt: Lagerbewertete Artikel buchen implizit
						// auf KEIN LAGER
						getLagerFac().bucheZu(
								LocaleFac.BELEGART_LSZIELLAGER,
								oAktuellerLieferschein.getIId(),
								oDtoI.getIId(),
								oDtoI.getArtikelIId(),
								new BigDecimal(0),
								bdNettogesamtpreis,
								iQuelleLager,
								null,
								new java.sql.Timestamp(System
										.currentTimeMillis()), theClientDto);
						getLagerFac().bucheAb(
								oAktuellerLieferschein.getBelegartCNr(),
								oAktuellerLieferschein.getIId(),
								oDtoI.getIId(), oDtoI.getArtikelIId(),
								new BigDecimal(0), bdNettogesamtpreis,
								iZielLager, (List) null,
								oAktuellerLieferschein.getTBelegdatum(),
								theClientDto);

					} else if (oDtoI.getNMenge().doubleValue() < 0) {

						if (oDtoI == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
									new Exception("oDtoI == null"));
						}

						ArtikelDto oArtikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(oDtoI.getArtikelIId(),
										theClientDto);

						// die zu buchende SNR- oder Chargennummer pruefen
						List<SeriennrChargennrMitMengeDto> sSerienchargennummer = oDtoI
								.getSeriennrChargennrMitMenge();

						if (Helper.short2boolean(oArtikelDto
								.getBChargennrtragend())) {
							if (sSerienchargennummer == null) {
								throw new EJBExceptionLP(
										EJBExceptionLP.ARTIKEL_ISTCHARGENNUMMERNBEHAFET,
										new Exception(
												"sSerienchargennummer == null"));
							}
						}

						if (Helper.short2boolean(oArtikelDto
								.getBSeriennrtragend())) {
							if (sSerienchargennummer == null) {
								throw new EJBExceptionLP(
										EJBExceptionLP.ARTIKEL_ISTSERIENNUMMERNBEHAFTET,
										new Exception(
												"sSerienchargennummer == null"));
							}

							if (sSerienchargennummer.size() != oDtoI
									.getNMenge().intValue()
									&& oDtoI.getNMenge().intValue() != 0) {
								throw new EJBExceptionLP(
										EJBExceptionLP.ARTIKEL_ANZAHLSERIENNUMMERNNICHTKORREKT,
										new Exception("Gewuenschte Menge: "
												+ oDtoI.getNMenge().intValue()));
							}
						}

						Integer iQuelleLager = null;
						Integer iZielLager = null;

						iZielLager = oAktuellerLieferschein.getZiellagerIId();
						iQuelleLager = oAktuellerLieferschein.getLagerIId();
						// den Nettogesamtpreis in Mandantenwaehrung umrechnen
						BigDecimal bdNettogesamtpreis = oDtoI
								.getNNettoeinzelpreis();

						if (!oAktuellerLieferschein.getWaehrungCNr().equals(
								theClientDto.getSMandantenwaehrung())) {
							BigDecimal ddFaktor = new BigDecimal(1)
									.divide(new BigDecimal(
											oAktuellerLieferschein
													.getFWechselkursmandantwaehrungzubelegwaehrung()
													.doubleValue()));

							bdNettogesamtpreis = bdNettogesamtpreis
									.multiply(ddFaktor);
						}

						// bei chargennummerbehafteten Artikeln kann es sein,
						// dass nicht die gesamte Charge verbraucht wird
						if (Helper.short2boolean(oArtikelDto
								.getBChargennrtragend())) {

							BigDecimal dAbzubuchendeMengeGesamt = oDtoI
									.getNMenge();

							for (int i = 0; i < sSerienchargennummer.size(); i++) {
								String sChargennummer = sSerienchargennummer
										.get(i).getCSeriennrChargennr();

								BigDecimal dMengeDerCharge = getLagerFac()
										.getMengeAufLager(
												oArtikelDto.getIId(),
												oAktuellerLieferschein
														.getLagerIId(),
												sChargennummer, theClientDto);

								BigDecimal dAbzubuchendeMengeEinzel = new BigDecimal(
										-1);

								if (dAbzubuchendeMengeGesamt.doubleValue() < dMengeDerCharge
										.doubleValue()) {
									dAbzubuchendeMengeEinzel = dAbzubuchendeMengeGesamt;
								} else {
									dAbzubuchendeMengeEinzel = dMengeDerCharge;
								}
								getLagerFac()
										.bucheUm(
												oDtoI.getArtikelIId(),
												iQuelleLager,
												oDtoI.getArtikelIId(),
												iZielLager,
												dAbzubuchendeMengeEinzel
														.negate(),
												SeriennrChargennrMitMengeDto
														.erstelleDtoAusEinerChargennummer(
																sChargennummer,
																dAbzubuchendeMengeEinzel
																		.negate()),
												"Umbuchung", null, theClientDto);
								dAbzubuchendeMengeGesamt = dAbzubuchendeMengeGesamt
										.subtract(dAbzubuchendeMengeEinzel);
							}
						} else if (Helper.short2boolean(oArtikelDto
								.getBSeriennrtragend())) {

							BigDecimal nMengeAbsolut = new BigDecimal(1);

							// eine Lagerbuchung fuer SNR Artikel stornieren
							if (oDtoI.getNMenge().intValue() == 0) {
								nMengeAbsolut = Helper.getBigDecimalNull();
							}

							for (int i = 0; i < sSerienchargennummer.size(); i++) {
								getLagerFac()
										.bucheUm(
												oDtoI.getArtikelIId(),
												iQuelleLager,
												oDtoI.getArtikelIId(),
												iZielLager,
												nMengeAbsolut.negate(),
												SeriennrChargennrMitMengeDto
														.erstelleDtoAusEinerChargennummer(
																sSerienchargennummer
																		.get(i)
																		.getCSeriennrChargennr(),
																nMengeAbsolut
																		.negate()),
												"Umbuchung", null, theClientDto);
							}
						} else {
							// absolute Lagerbuchung, funktioniert fuer nicht
							// lagerbewirtschaftete Artikel;
							// es gilt: Lagerbewertete Artikel buchen implizit
							// auf KEIN LAGER
							getLagerFac().bucheUm(oDtoI.getArtikelIId(),
									iQuelleLager, oDtoI.getArtikelIId(),
									iZielLager, oDtoI.getNMenge().negate(),
									null, "Umbuchung", null, theClientDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_STORNIEREN,
					new Exception(t));
		}
	}

	/**
	 * Die Reservierung fuer die (Teil) Lieferung einer Auftragposition
	 * zuruecknehmen. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void rollbackReservierungZuAuftragposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		try {
			// die Lieferscheinposition bezieht sich auf die folgende
			// Auftragposition
			AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(
							lieferscheinpositionDtoI.getAuftragpositionIId());

			// fuer die Auftragposition kann eine Artikelreservierung existieren
			ArtikelreservierungDto oReservierungDto = getReservierungFac()
					.getArtikelreservierung(LocaleFac.BELEGART_AUFTRAG,
							lieferscheinpositionDtoI.getAuftragpositionIId());

			// Artikelreservierungen werden absolut gebucht. Es gilt: Reserviert
			// wird
			// maximal die Menge, die in der Auftragposition vorgesehen ist.
			BigDecimal nAbsoluteMengeFuerArtikelreservierung = null;

			if (oReservierungDto != null) {
				// die nicht gelieferte Menge muss zur reservierten Menge
				// addiert werden
				nAbsoluteMengeFuerArtikelreservierung = oReservierungDto
						.getNMenge().add(lieferscheinpositionDtoI.getNMenge());

				// eine eventuelle Ueberlieferung beruecksichtigen
				if (nAbsoluteMengeFuerArtikelreservierung.doubleValue() > oAuftragpositionDto
						.getNMenge().doubleValue()) {
					nAbsoluteMengeFuerArtikelreservierung = oAuftragpositionDto
							.getNMenge();
				}

				oReservierungDto
						.setNMenge(nAbsoluteMengeFuerArtikelreservierung); // absolut
				// buchen

				getReservierungFac()
						.updateArtikelreservierung(oReservierungDto);
			} else {
				// eine neue Reservierung machen
				oReservierungDto = new ArtikelreservierungDto();
				oReservierungDto.setArtikelIId(lieferscheinpositionDtoI
						.getArtikelIId());
				oReservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				oReservierungDto
						.setIBelegartpositionid(lieferscheinpositionDtoI
								.getAuftragpositionIId());
				oReservierungDto.setTLiefertermin(oAuftragpositionDto
						.getTUebersteuerbarerLiefertermin());

				// die nicht gelieferte Menge ist die zu reservierende Menge
				nAbsoluteMengeFuerArtikelreservierung = lieferscheinpositionDtoI
						.getNMenge();

				// eine eventuelle Ueberlieferung beruecksichtigen
				if (nAbsoluteMengeFuerArtikelreservierung.doubleValue() > oAuftragpositionDto
						.getNMenge().doubleValue()) {
					nAbsoluteMengeFuerArtikelreservierung = oAuftragpositionDto
							.getNMenge();
				}

				oReservierungDto
						.setNMenge(nAbsoluteMengeFuerArtikelreservierung);

				getReservierungFac()
						.createArtikelreservierung(oReservierungDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void checkLieferscheinpositionDto(
			LieferscheinpositionDto lieferscheinpositionDto)
			throws EJBExceptionLP {
		if (lieferscheinpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferscheinpositionDto == null"));
		}

		myLogger.info("LieferscheinpositionDto: "
				+ lieferscheinpositionDto.toString());
	}

	private void checkLieferscheinpositionIId(Integer iIdlieferscheinpositionI)
			throws EJBExceptionLP {
		if (iIdlieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdlieferscheinpositionI == null"));
		}

		myLogger.info("LieferscheinpositionIId: "
				+ iIdlieferscheinpositionI.toString());
	}

	/**
	 * Anzahl aller Positionen eines Lieferscheins bestimmen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            String
	 * @return int Anzahl aller Positionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public int berechneAnzahlMengenbehaftetePositionen(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		int iAnzahlO = 0;
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();
			if (oPosition.getNMenge() != null) {
				iAnzahlO++;
			}
		}
		return iAnzahlO;
	}

	public void berechnePauschalposition(BigDecimal neuWert,
			Integer positionIId, Integer belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BigDecimal altWert = getGesamtpreisPosition(positionIId, theClientDto);
		LieferscheinDto lieferscheinDto = getLieferscheinFac()
				.lieferscheinFindByPrimaryKey(belegIId, theClientDto);
		LieferscheinpositionDto[] lieferscheinpositionDtos = lieferscheinpositionFindByPositionIId(positionIId);
		for (int i = 0; i < lieferscheinpositionDtos.length; i++) {
			LieferscheinpositionDto lieferscheinpositionDto = (LieferscheinpositionDto) getBelegVerkaufFac()
					.berechnePauschalposition(lieferscheinpositionDtos[i],
							lieferscheinDto, neuWert, altWert);

			Lieferscheinposition position = em.find(Lieferscheinposition.class,
					lieferscheinpositionDto.getIId());
			position.setNNettogesamtpreis(lieferscheinpositionDto
					.getNNettoeinzelpreis());
			position.setNNettogesamtpreisplusversteckteraufschlag(lieferscheinpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			position.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(lieferscheinpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			position.setBNettopreisuebersteuert(Helper.boolean2Short(true));
			try {

				Query queryLB = em
						.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
				queryLB.setParameter(1, LocaleFac.BELEGART_LIEFERSCHEIN);
				queryLB.setParameter(2, position.getIId());
				queryLB.setParameter(3, null);
				Lagerbewegung lagerbewegung = (Lagerbewegung) queryLB
						.getSingleResult();
				lagerbewegung.setNVerkaufspreis(lieferscheinpositionDto
						.getNNettoeinzelpreis());
			} catch (NoResultException ex) {

			}
		}
	}

	/**
	 * Die Anzahl der mengenbehafteten Positionen eines Lieferscheins bestimmen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @return int die Anzahl der mengenbehafteten Positionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public int berechneAnzahlArtikelpositionen(Integer iIdLieferscheinI)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneAnzahlArtikelpositionen";
		myLogger.entry();

		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		int iAnzahlO = 0;
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getLieferscheinpositionartCNr().equals(
					LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
					|| oPosition
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
				iAnzahlO++;
			}
		}
		return iAnzahlO;
	}

	private void setLieferscheinpositionFromLieferscheinpositionDto(
			Lieferscheinposition lieferscheinposition,
			LieferscheinpositionDto lieferscheinpositionDto) {
		if (lieferscheinpositionDto.getLieferscheinIId() != null) {
			lieferscheinposition.setLieferscheinIId(lieferscheinpositionDto
					.getLieferscheinIId());
		}
		if (lieferscheinpositionDto.getISort() != null) {
			lieferscheinposition.setISort(lieferscheinpositionDto.getISort());
		}
		if (lieferscheinpositionDto.getLieferscheinpositionartCNr() != null) {
			lieferscheinposition
					.setLieferscheinpositionartCNr(lieferscheinpositionDto
							.getLieferscheinpositionartCNr());
		}
		if (lieferscheinpositionDto.getArtikelIId() != null) {
			lieferscheinposition.setArtikelIId(lieferscheinpositionDto
					.getArtikelIId());
		}

		// die Bezeichnung kann null sein
		lieferscheinposition.setCBez(lieferscheinpositionDto.getCBez());
		lieferscheinposition.setCZbez(lieferscheinpositionDto.getCZusatzbez());

		if (lieferscheinpositionDto.getBArtikelbezeichnunguebersteuert() != null) {
			lieferscheinposition
					.setBArtikelbezeichnunguebersteuert(lieferscheinpositionDto
							.getBArtikelbezeichnunguebersteuert());
		}

		lieferscheinposition.setXTextinhalt(lieferscheinpositionDto
				.getXTextinhalt());
		lieferscheinposition.setMediastandardIId(lieferscheinpositionDto
				.getMediastandardIId());

		if (lieferscheinpositionDto.getAuftragpositionIId() != null) {
			lieferscheinposition.setAuftragpositionIId(lieferscheinpositionDto
					.getAuftragpositionIId());
		}
		if (lieferscheinpositionDto.getNMenge() != null) {
			lieferscheinposition.setNMenge(lieferscheinpositionDto.getNMenge());
		}
		if (lieferscheinpositionDto.getEinheitCNr() != null) {
			lieferscheinposition.setEinheitCNr(lieferscheinpositionDto
					.getEinheitCNr());
		}
		lieferscheinposition.setFRabattsatz(lieferscheinpositionDto
				.getFRabattsatz());
		if (lieferscheinpositionDto.getBRabattsatzuebersteuert() != null) {
			lieferscheinposition
					.setBRabattsatzuebersteuert(lieferscheinpositionDto
							.getBRabattsatzuebersteuert());
		}
		lieferscheinposition.setFZusatzrabattsatz(lieferscheinpositionDto
				.getFZusatzrabattsatz());
		lieferscheinposition.setFKupferzuschlag(lieferscheinpositionDto
				.getFKupferzuschlag());
		if (lieferscheinpositionDto.getMwstsatzIId() != null) {
			lieferscheinposition.setMwstsatzIId(lieferscheinpositionDto
					.getMwstsatzIId());
		}
		if (lieferscheinpositionDto.getBMwstsatzuebersteuert() != null) {
			lieferscheinposition
					.setBMwstsatzuebersteuert(lieferscheinpositionDto
							.getBMwstsatzuebersteuert());
		}
		lieferscheinposition.setBNettopreisuebersteuert(lieferscheinpositionDto
				.getBNettopreisuebersteuert());
		lieferscheinposition.setNNettoeinzelpreis(lieferscheinpositionDto
				.getNEinzelpreis());
		lieferscheinposition
				.setNNettoeinzelpreisplusversteckteraufschlag(lieferscheinpositionDto
						.getNEinzelpreisplusversteckteraufschlag());
		lieferscheinposition.setNRabattbetrag(lieferscheinpositionDto
				.getNRabattbetrag());
		lieferscheinposition.setNNettogesamtpreis(lieferscheinpositionDto
				.getNNettoeinzelpreis());
		lieferscheinposition
				.setNNettogesamtpreisplusversteckteraufschlag(lieferscheinpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlag());
		lieferscheinposition
				.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(lieferscheinpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		lieferscheinposition.setNMwstbetrag(lieferscheinpositionDto
				.getNMwstbetrag());
		lieferscheinposition.setNBruttogesamtpreis(lieferscheinpositionDto
				.getNBruttoeinzelpreis());
		lieferscheinposition.setPositionIId(lieferscheinpositionDto
				.getPositioniId());
		lieferscheinposition.setPositionIIdArtikelset(lieferscheinpositionDto
				.getPositioniIdArtikelset());
		lieferscheinposition.setTypCNr(lieferscheinpositionDto.getTypCNr());
		lieferscheinposition.setVerleihIId(lieferscheinpositionDto
				.getVerleihIId());
		lieferscheinposition.setKostentraegerIId(lieferscheinpositionDto
				.getKostentraegerIId());
		lieferscheinposition.setBKeinlieferrest(lieferscheinpositionDto
				.getBKeinlieferrest());
		lieferscheinposition.setZwsVonPosition(lieferscheinpositionDto
				.getZwsVonPosition());
		lieferscheinposition.setZwsBisPosition(lieferscheinpositionDto
				.getZwsBisPosition());
		lieferscheinposition.setZwsNettoSumme(lieferscheinpositionDto
				.getZwsNettoSumme());
		lieferscheinposition.setCLvposition(lieferscheinpositionDto
				.getCLvposition());
		lieferscheinposition.setNMaterialzuschlag(lieferscheinpositionDto
				.getNMaterialzuschlag());
		lieferscheinposition.setLagerIId(lieferscheinpositionDto.getLagerIId());

		em.merge(lieferscheinposition);
		em.flush();
	}

	private LieferscheinpositionDto assembleLieferscheinpositionDto(
			Lieferscheinposition lieferscheinposition) {

		LieferscheinpositionDto lieferscheinpositionDto = LieferscheinpositionDtoAssembler
				.createDto(lieferscheinposition);
		lieferscheinpositionDto.setSeriennrChargennrMitMenge(getLagerFac()
				.getAllSeriennrchargennrEinerBelegartposition(
						LocaleFac.BELEGART_LIEFERSCHEIN,
						lieferscheinpositionDto.getIId()));

		return lieferscheinpositionDto;
	}

	private LieferscheinpositionDto[] assembleLieferscheinpositionDtos(
			Collection<?> lieferscheinpositions) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator
						.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition));
			}
		}
		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[list
				.size()];
		return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}

	private Collection<LieferscheinpositionDto> assembleLieferscheinpositionDtos(
			Collection<?> lieferscheinpositions, TheClientDto theClientDto) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator
						.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition));
			}
		}
		return list;
	}

	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinIId(
			Integer iIdLieferscheinI) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos);
	}

	public LieferscheinpositionDto[] lieferscheinpositionFindByPositionIId(
			Integer iIdPositionI) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByPositionIId");
		query.setParameter(1, iIdPositionI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos);
	}

	public HashMap lieferscheinpositionFindByLieferscheinIIdAuftragIId(
			Integer lieferscheinIId, Integer iIdAuftragI,
			TheClientDto theClientDto) {
		Session session = null;
		HashMap alIds = new HashMap();

		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Suchen aller LS-Positionen, die sich auf diesen Auftrag beziehen.
			Criteria c = session.createCriteria(FLRLieferscheinposition.class);
			c.createCriteria(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN)
					.add(Restrictions.eq("i_id", lieferscheinIId));
			c.createCriteria(
					LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
					.add(Restrictions
							.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID,
									iIdAuftragI));
			// Query ausfuehren
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLieferscheinposition item = (FLRLieferscheinposition) iter
						.next();

				alIds.put(item.getI_id(), item.getI_id());

			}

			return alIds;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Collection<LieferscheinpositionDto> lieferscheinpositionFindByLieferscheinIId(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos,
				theClientDto);
	}

	/**
	 * Alle mengenbehafteten Positionen zu einem bestimmten Lieferschein holen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @return LieferscheinpositionDto[] alle mengenbehafteten Positionen zu
	 *         einem bestimmten Lieferschein
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinMenge(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "lieferscheinpositionFindByLieferscheinMenge";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		LieferscheinpositionDto[] aLieferscheinpositionDtos = null;
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferscheinMenge");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		aLieferscheinpositionDtos = assembleLieferscheinpositionDtos(cl);
		return aLieferscheinpositionDtos;
	}

	public LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(
			Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<LieferscheinpositionDto> dtos = new ArrayList<LieferscheinpositionDto>();
		LieferscheinpositionDto[] aLieferscheinpositionDto = null;
		Session sesion = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			sesion = factory.openSession();
			Criteria crit = sesion
					.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq("flrlieferschein.i_id", iIdLieferscheinI));
			crit.add(Restrictions.isNull("position_i_id"));

			crit.addOrder(Order.asc("i_sort"));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter
						.next();
				LieferscheinpositionDto apositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(pos.getI_id(),
								theClientDto);
				if (pos.getPositionsart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					if (!pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
						dtos.add(apositionDto);
					}
				} else {
					dtos.add(apositionDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (sesion != null) {
				sesion.close();
			}
		}
		aLieferscheinpositionDto = new LieferscheinpositionDto[dtos.size()];
		for (int i = 0; i < dtos.size(); i++) {
			aLieferscheinpositionDto[i] = dtos.get(i);
		}
		return aLieferscheinpositionDto;
	}

	/**
	 * Der Kupferzuschlag einer Position kann von aussen gesetzt werden (z.B.
	 * durch die Rechnung). <br>
	 * Das Setzen des Kupferzuschlags zieht die folgenden Schritte nach sich: <br>
	 * - Die Werte der Lieferscheinposition werden neu berechnet und in der DB
	 * gespeichert <br>
	 * - Der Gesamtwert des Lieferscheins wird neu berechnet und in der DB
	 * gespeichert
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param ddKupferzuschlagI
	 *            der Kupferzuschlag in Prozent, z.B. 30
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void setzeKupferzuschlag(Integer iIdLieferscheinpositionI,
			Double ddKupferzuschlagI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "setzeKupferzuschlag";
		myLogger.entry();

		if (iIdLieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinpositionI == null"));
		}

		// der Kupferzuschlag kann auf 0 gesetzt werden, ein Wechsel auf NULL
		// ist nicht moeglich
		if (ddKupferzuschlagI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("ddKupferzuschlagI == null"));
		}
		Lieferscheinposition oPosition = null;
		oPosition = em.find(Lieferscheinposition.class,
				iIdLieferscheinpositionI);
		if (oPosition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		try {
			// Schritt 1: den Kupfzuschlag setzen
			oPosition.setFKupferzuschlag(ddKupferzuschlagI);

			// Schritt 2: die Werte der Lieferscheinposition neu berechnen

			// Nettoeinzelpreis der Position bleibt unveraendert
			// NettoeinzelpreisPlusVersteckterAufschlag der Position bleibt
			// unveraendert und dient als Berechnungsgrundlage
			// Nettogesamtpreis, Rabattbetrag, Mwstbetrag und Bruttogesamtpreis
			// werden im PanelPositionen angezeigt und bleiben UNVERAENDERT
			// NettogesamtpreisPlusVersteckterAufschlag wird NEU berechnet
			// NettogesamtpreisPlusVersteckterAufschlagMinusRabatt wird NEU
			// berechnet

			BigDecimal bdBetragTemp = oPosition
					.getNNettoeinzelpreisplusversteckteraufschlag();

			// + Kupferzuschlag
			BigDecimal bdKupferzuschlagBetrag = oPosition
					.getNNettoeinzelpreisplusversteckteraufschlag().multiply(
							new BigDecimal(oPosition.getFKupferzuschlag()
									.doubleValue()).movePointLeft(2));
			bdKupferzuschlagBetrag = Helper.rundeKaufmaennisch(
					bdKupferzuschlagBetrag, 4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.add(bdKupferzuschlagBetrag);

			// - Rabattsatz
			BigDecimal bdRabattsatzBetrag = bdBetragTemp
					.multiply(new BigDecimal(oPosition.getFRabattsatz()
							.doubleValue()).movePointLeft(2));
			bdRabattsatzBetrag = Helper.rundeKaufmaennisch(bdRabattsatzBetrag,
					4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.subtract(bdRabattsatzBetrag);

			oPosition
					.setNNettogesamtpreisplusversteckteraufschlag(bdBetragTemp); // NEU

			LieferscheinDto oLieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							oPosition.getLieferscheinIId(), theClientDto);

			// - Allgemeiner Rabatt aus den Konditionen
			BigDecimal bdAllgemeinerRabattBetrag = bdBetragTemp
					.multiply(new BigDecimal(oLieferscheinDto
							.getFAllgemeinerRabattsatz().doubleValue())
							.movePointLeft(2));
			bdAllgemeinerRabattBetrag = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattBetrag, 4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.subtract(bdAllgemeinerRabattBetrag);

			oPosition
					.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(bdBetragTemp); // NEU

			// Schritt 3: Den Wert des Lieferscheins neu berechnen und setzen
			BigDecimal bdGesamtwertInLieferscheinwaehrung = getLieferscheinFac()
					.berechneGesamtwertLieferscheinAusPositionen(
							oPosition.getLieferscheinIId(), theClientDto);

			bdGesamtwertInLieferscheinwaehrung = Helper.rundeKaufmaennisch(
					bdGesamtwertInLieferscheinwaehrung, 4);
			checkNumberFormat(bdGesamtwertInLieferscheinwaehrung);

			oLieferscheinDto
					.setNGesamtwertInLieferscheinwaehrung(bdGesamtwertInLieferscheinwaehrung);

			getLieferscheinFac().updateLieferschein(oLieferscheinDto,
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	// Methoden zur Bestimmung der Sortierung der Lieferscheinpositionen
	// ---------

	/**
	 * Das maximale iSort bei den Lieferscheinpositionen fuer einen bestimmten
	 * Mandanten bestimmen.
	 * 
	 * @param iIdLieferscheinI
	 *            der aktuelle Lieferschein
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private Integer getMaxISort(Integer iIdLieferscheinI) throws EJBExceptionLP {
		Integer iiMaxISortO = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheinpositionejbSelectMaxISort");
			query.setParameter(1, iIdLieferscheinI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(t));
		}
		return iiMaxISortO;
	}

	public boolean gehoertZuArtikelset(Integer lieferscheinpositionIId) {

		Lieferscheinposition oPosition1 = em.find(Lieferscheinposition.class,
				lieferscheinpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return true;
		}

		Query query = em
				.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
		query.setParameter(1, lieferscheinpositionIId);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		LieferscheinpositionDto[] zugehoerigeLSPosDtos = assembleLieferscheinpositionDtos(lieferscheinpositionDtos);

		if (zugehoerigeLSPosDtos != null && zugehoerigeLSPosDtos.length > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Zwei bestehende Lieferscheinpositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I
	 *            PK der ersten Position
	 * @param iIdPosition2I
	 *            PK der zweiten Position
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauschePositionen(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP {
		Lieferscheinposition oPosition1 = em.find(Lieferscheinposition.class,
				iIdPosition1I);

		Lieferscheinposition oPosition2 = em.find(Lieferscheinposition.class,
				iIdPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() == null) {

			// das zweite iSort auf ungueltig setzen, damit UK
			// constraint nicht verletzt wird
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
							.createNamedQuery("LieferscheinpositionfindByLieferscheinISort");
					query.setParameter(1, oPosition2.getLieferscheinIId());
					query.setParameter(2, oPosition2.getISort() - 1);
					Lieferscheinposition oPos = (Lieferscheinposition) query
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

	}

	public void vertauscheLieferscheinpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Lieferscheinposition> comp = new CompositeISort<Lieferscheinposition>(
				new LieferscheinpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto);
		getLieferscheinFac().updateTAendern(
				lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto).getLieferscheinIId(),
				theClientDto);
	}

	public void vertauscheLieferscheinpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Lieferscheinposition> comp = new CompositeISort<Lieferscheinposition>(
				new LieferscheinpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto);
		getLieferscheinFac().updateTAendern(
				lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto).getLieferscheinIId(),
				theClientDto);
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdLieferscheinI
	 *            der aktuelle Lieferschein
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdLieferscheinI, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
	}

	private LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(
			Integer iIdLieferscheinI, String positionsartCNrI)
			throws NoResultException, NonUniqueResultException {
		LieferscheinpositionDto angebotpositionDto = null;
		Query query = em
				.createNamedQuery("LieferscheinpositionpositionfindByLieferscheinIIdLieferscheinpositionartCNr");
		query.setParameter(1, iIdLieferscheinI);
		query.setParameter(2, positionsartCNrI);
		Lieferscheinposition lieferscheinposition = (Lieferscheinposition) query
				.getSingleResult();
		angebotpositionDto = assembleLieferscheinpositionDto(lieferscheinposition);
		return angebotpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI) {
		LieferscheinpositionDto lieferscheinpositionDto = null;
		try {
			lieferscheinpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(
					iIdAngebotI, positionsartCNrI);
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex) {
		}
		return lieferscheinpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNr(
			Integer iIdLieferscheinI, String positionsartCNrI)
			throws EJBExceptionLP {
		LieferscheinpositionDto angebotpositionDto = null;
		try {
			angebotpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(
					iIdLieferscheinI, positionsartCNrI);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler. Es gibt mehere Angebotspositionen mit positionsart "
							+ positionsartCNrI + " fuer angebotIId "
							+ iIdLieferscheinI);
		}
		return angebotpositionDto;
	}

	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdLieferscheinI);
		LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
				iIdLieferscheinI,
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME);

		if (lieferscheinpositionDto != null) {
			LieferscheinpositionDto[] aLieferscheinDto = lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinDto.length; i++) {
				if (aLieferscheinDto[i]
						.getPositionsartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)) {
					int iIndexLetztePreisbehaftetePositionNachEndsumme = -1;

					for (int j = i + 1; j < aLieferscheinDto.length; j++) {
						if (aLieferscheinDto[j].getNEinzelpreis() != null) {
							// die Position der letzten preisbehafteten Position
							// nach der Endsumme bestimmen
							iIndexLetztePreisbehaftetePositionNachEndsumme = j;
						}
					}

					if (iIndexLetztePreisbehaftetePositionNachEndsumme != -1) {
						// die Endsumme muss nach die letzte preisbehaftete
						// Position verschoben werden
						for (int k = i; k < iIndexLetztePreisbehaftetePositionNachEndsumme; k++) {
							vertauschePositionen(aLieferscheinDto[i].getIId(),
									aLieferscheinDto[k + 1].getIId());
						}
					}
				}
			}
		}
	}

	/**
	 * Wenn fuer einen Lieferschein eine Position geloescht wurde, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken
	 * entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param iIdLieferscheinI
	 *            PK des Lieferscheins
	 * @param iSortierungGeloeschtePositionI
	 *            die Position der geloschten Position
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(
			Integer iIdLieferscheinI, int iSortierungGeloeschtePositionI)
			throws Throwable {
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getPositionIId() != null) {
				LieferscheinpositionDto posBegin = lieferscheinpositionFindByLieferscheinIIdPositionIId(
						oPosition.getLieferscheinIId(),
						oPosition.getPositionIId());
				if (posBegin == null) {
					oPosition.setPositionIId(null);
					oPosition.setTypCNr(null);
				}
			}
			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	/**
	 * Eine bestimmte Lieferscheinposition holen.
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return LieferscheinpositionDto die Lieferscheinposition
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LieferscheinpositionDto lieferscheinpositionDto = null;
		Lieferscheinposition lieferscheinposition = em.find(
				Lieferscheinposition.class, iIdLieferscheinpositionI);
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinpositionDto = assembleLieferscheinpositionDto(lieferscheinposition);

		return lieferscheinpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByLieferscheinIIdPositionIId(
			Integer lieferscheinIId, Integer lieferscheinpositionIId)
			throws EJBExceptionLP {
		Lieferscheinposition lieferscheinposition = null;
		if (lieferscheinIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("lieferscheinIId == null"));
		}
		if (lieferscheinpositionIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdPosition == null"));
		}
		try {
			Query query = em
					.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdPositionIId");
			query.setParameter(1, lieferscheinIId);
			query.setParameter(2, lieferscheinpositionIId);
			lieferscheinposition = (Lieferscheinposition) query
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		return assembleLieferscheinpositionDto(lieferscheinposition);
	}

	/**
	 * Eine Lieferscheinposition ueber ihren PK holen.
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return LieferscheinpositionDto die Position
	 */
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKeyOhneExc(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto) {

		Lieferscheinposition lieferscheinposition = em.find(
				Lieferscheinposition.class, iIdLieferscheinpositionI);

		return lieferscheinposition == null ? null
				: assembleLieferscheinpositionDto(lieferscheinposition);
	}

	public LieferscheinpositionDto lieferscheinpositionFindPositionIIdISort(
			Integer positionIId, Integer iSort) throws EJBExceptionLP {
		Lieferscheinposition lieferscheinposition = null;
		try {
			Query query = em
					.createNamedQuery("LieferscheinpositionfindByPositionIIdISort");
			query.setParameter(1, positionIId);
			query.setParameter(2, iSort);
			lieferscheinposition = (Lieferscheinposition) query
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		return assembleLieferscheinpositionDto(lieferscheinposition);
	}

	/**
	 * Alle Lieferscheinposition holen, die Bezug zu einer bestimmten
	 * Auftragposition haben.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK der Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return LieferscheinpositionDto[] die Lieferscheinpositionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(
			Integer iIdAuftragpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAndLogData(theClientDto, iIdAuftragpositionI);
		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}
		LieferscheinpositionDto[] aPositionDtos = null;
		Query query = em
				.createNamedQuery("LieferscheinpositionfindByAuftragposition");
		query.setParameter(1, iIdAuftragpositionI);
		Collection<?> cl = query.getResultList();
		aPositionDtos = assembleLieferscheinpositionDtos(cl);
		return aPositionDtos;
	}

	private void preiseEinesArtikelsetsUpdaten(
			Integer rechnungpositionIIdKopfartikel, TheClientDto theClientDto) {

		LieferscheinpositionDto rechnungPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				rechnungpositionIIdKopfartikel, theClientDto);

		Query query = em
				.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
		query.setParameter(1, rechnungPositionDtoKopfartikel.getIId());
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		try {
			LieferscheinDto rechnungDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							rechnungPositionDtoKopfartikel.getLieferscheinIId(),
							theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIIdRechnungsadresse(), theClientDto);

			Integer mwstsatzbezIId = getMandantFac().mwstsatzFindByPrimaryKey(
					rechnungPositionDtoKopfartikel.getMwstsatzIId(),
					theClientDto).getIIMwstsatzbezId();

			// Zuerst Gesamtwert berechnen
			BigDecimal bdMenge = rechnungPositionDtoKopfartikel.getNMenge();

			BigDecimal bdNettoeinzelpreis = rechnungPositionDtoKopfartikel
					.getNNettoeinzelpreis();

			BigDecimal bdGesamtwertposition = bdMenge
					.multiply(bdNettoeinzelpreis);

			BigDecimal bdGesamtVKwert = new BigDecimal(0);

			Iterator<?> it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Lieferscheinposition struktur = (Lieferscheinposition) it
						.next();

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								rechnungDto.getKundeIIdRechnungsadresse(),

								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(),
								rechnungDto.getWaehrungCNr(), theClientDto);

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
				Lieferscheinposition struktur = (Lieferscheinposition) it
						.next();

				struktur.setNNettoeinzelpreis(new BigDecimal(0));

				struktur.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
						0));
				struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(new BigDecimal(
						0));
				struktur.setNBruttogesamtpreis(new BigDecimal(0));

				// Mehrwertsteuersatz: Kommt immer aus dem Kopfartikel,
				// da dieser die Hauptleistung darstellt

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								rechnungDto.getKundeIIdRechnungsadresse(),
								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(),
								rechnungDto.getWaehrungCNr(), theClientDto);

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

					BigDecimal preis = bdGesamtwertposition.multiply(
							bdAnteilVKWert).divide(
							struktur.getNMenge().multiply(bdMenge), 4,
							BigDecimal.ROUND_HALF_EVEN);

					struktur.setNNettoeinzelpreis(preis);
					struktur.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);

					/*
					 * struktur.setNBruttogesamtpreis(bdGesamtwertposition
					 * .multiply(bdAnteilVKWert).divide(
					 * struktur.getNMenge().multiply(bdMenge), 4,
					 * BigDecimal.ROUND_HALF_UP));
					 */
					struktur.setNNettogesamtpreis(preis);
					struktur.setNBruttogesamtpreis(preis);

					struktur.setNNettoeinzelpreisplusversteckteraufschlag(preis);
					struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatt(preis);

				}

			}
			getLieferscheinFac().updateTAendern(rechnungDto.getIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}
	}

	public void preiseAusAuftragspositionenUebernehmen(Integer auftragIId,
			TheClientDto theClientDto) {
		try {
			AuftragpositionDto[] posDtos = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragIId);

			for (int i = 0; i < posDtos.length; i++) {

				LieferscheinpositionDto[] lsPosDtos = lieferscheinpositionFindByAuftragpositionIId(
						posDtos[i].getIId(), theClientDto);

				for (int j = 0; j < lsPosDtos.length; j++) {
					LieferscheinpositionDto lsPosDto = lsPosDtos[j];

					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									lsPosDto.getLieferscheinIId(), theClientDto);

					if (!lsDto.getStatusCNr().equals(
							LieferscheinFac.LSSTATUS_VERRECHNET)
							&& !lsDto.getStatusCNr().equals(
									LieferscheinFac.LSSTATUS_ERLEDIGT)) {
						if (lsPosDto.getNMenge() != null) {
							lsPosDto.setNBruttoeinzelpreis(posDtos[i]
									.getNBruttoeinzelpreis());
							lsPosDto.setNEinzelpreis(posDtos[i]
									.getNEinzelpreis());
							lsPosDto.setNEinzelpreisplusversteckteraufschlag(posDtos[i]
									.getNEinzelpreisplusversteckteraufschlag());
							lsPosDto.setNMaterialzuschlag(posDtos[i]
									.getNMaterialzuschlag());
							lsPosDto.setNMwstbetrag(posDtos[i].getNMwstbetrag());
							lsPosDto.setNNettoeinzelpreis(posDtos[i]
									.getNNettoeinzelpreis());
							lsPosDto.setNNettoeinzelpreisplusversteckteraufschlag(posDtos[i]
									.getNNettoeinzelpreisplusversteckteraufschlag());
							lsPosDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(posDtos[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
							lsPosDto.setNRabattbetrag(posDtos[i]
									.getNRabattbetrag());
							lsPosDto.setMwstsatzIId(posDtos[i].getMwstsatzIId());
							lsPosDto.setFRabattsatz(posDtos[i].getFRabattsatz());
							lsPosDto.setFZusatzrabattsatz(posDtos[i]
									.getFZusatzrabattsatz());

							updateLieferscheinpositionOhneWeitereAktion(
									lsPosDto, theClientDto);
						}
					}

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Lieferscheinposition aktualisieren. <br>
	 * Chargenbehaftete Lieferscheinpositionen koennen nicht aktualisiert
	 * werden.
	 * 
	 * @param oLieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws RemoteException 
	 */
	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdLieferscheinposition = null;
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		pruefePflichtfelderBelegposition(oLieferscheinpositionDtoI,
				theClientDto);

		if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
			updateLieferscheinpositionHandeingabe(oLieferscheinpositionDtoI,
					theClientDto);
		} else if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
			// auf KEIN LAGER)
			iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
					oLieferscheinpositionDtoI, theClientDto);

		} else {
			// die Lieferscheinposition und den Lieferscheinstatus
			// korrigieren
			updateLieferscheinstatusLieferscheinposition(
					oLieferscheinpositionDtoI, theClientDto);
		}
		getLieferscheinFac().updateTAendern(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		return iIdLieferscheinposition;
	}

	public Integer updateLieferscheinposition(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Integer iIdLieferscheinposition = null;
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		pruefePflichtfelderBelegposition(oLieferscheinpositionDtoI,
				theClientDto);

		if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr().equals(
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
			updateLieferscheinpositionHandeingabe(oLieferscheinpositionDtoI,
					theClientDto);
		} else if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
			// auf KEIN LAGER)
			iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
					oLieferscheinpositionDtoI, identities, theClientDto);

		} else {
			// die Lieferscheinposition und den Lieferscheinstatus
			// korrigieren
			updateLieferscheinstatusLieferscheinposition(
					oLieferscheinpositionDtoI, theClientDto);
		}
		getLieferscheinFac().updateTAendern(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		return iIdLieferscheinposition;
	}

	public void lieferscheinpositionKeinLieferrestEintragen(
			Integer lieferscheinpositionIId, boolean bKeinLieferrest, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Lieferscheinposition lieferscheinposition = em.find(
				Lieferscheinposition.class, lieferscheinpositionIId);
		lieferscheinposition.setBKeinlieferrest(Helper
				.boolean2Short(bKeinLieferrest));
		getLieferscheinFac().updateTAendern(lieferscheinposition.getLieferscheinIId(), theClientDto);
	}

	/**
	 * Eine Lieferscheinposition vom Typ Handeingabe aktualisieren. <br>
	 * Eine Handeingabeposition bucht nicht auf Lager. <br>
	 * Vorsicht: Es kann mehrere Lieferscheine zu einem Auftrag geben, ausserdem
	 * kann eine Auftragposition teil- oder ueberliefert sein.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void updateLieferscheinpositionHandeingabe(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		try {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					lieferscheinpositionDtoI.getArtikelIId(), theClientDto);

			// den Handartikel korrigieren
			ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();
			oArtikelsprDto.setCBez(lieferscheinpositionDtoI.getCBez());
			oArtikelsprDto.setCZbez(lieferscheinpositionDtoI.getCZusatzbez());

			artikelDto.setArtikelsprDto(oArtikelsprDto);
			artikelDto.setEinheitCNr(lieferscheinpositionDtoI.getEinheitCNr());

			// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					lieferscheinpositionDtoI.getMwstsatzIId(), theClientDto);
			artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
			// Artikel speichern
			getArtikelFac().updateArtikel(artikelDto, theClientDto);

			LieferscheinpositionDto lieferscheinpositionBisherDto = lieferscheinpositionFindByPrimaryKey(
					lieferscheinpositionDtoI.getIId(), theClientDto);

			// die Lieferscheinposition und den Lieferscheinstatus korrigieren
			updateLieferscheinstatusLieferscheinposition(
					lieferscheinpositionDtoI, theClientDto);

			// die offene Menge im Auftrag korrigieren
			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
				// Mengenaenderung bestimmen

				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						lieferscheinpositionDtoI.getAuftragpositionIId(),
						theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Wiederkehrende Aktionen beim aktualisieren einer Lieferscheinposition. <br>
	 * - Status des Lieferscheins aktualisieren <br>
	 * - die Lieferscheinposition aktualisieren <br>
	 * - die zusaetzlichen Preisfelder der Lieferscheinposition neu berechnen
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void updateLieferscheinstatusLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			getLieferscheinFac().pruefeUndSetzeLieferscheinstatusBeiAenderung(
					lieferscheinpositionDtoI.getLieferscheinIId());

			Lieferscheinposition lieferscheinposition = em.find(
					Lieferscheinposition.class,
					lieferscheinpositionDtoI.getIId());
			if (lieferscheinposition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheinpositionFromLieferscheinpositionDto(
					lieferscheinposition, lieferscheinpositionDtoI);
			befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem lagerbewirtschafteten
	 * oder nicht lagerbewirtschafteten Artikel aktualisieren. <br>
	 * Eine nicht lagerbewirtschaftete Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws RemoteException 
	 */
	private Integer updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
				lieferscheinpositionDtoI,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
		// Integer iIdLieferscheinposition = null;
		//
		// // die Lagerbuchung und die Lieferscheinposition korrigieren
		// LieferscheinpositionDto lieferscheinpositionBisherDto =
		// lieferscheinpositionFindByPrimaryKey(
		// lieferscheinpositionDtoI.getIId(), theClientDto);
		// removeLieferscheinposition(lieferscheinpositionBisherDto,
		// theClientDto);
		// sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
		// lieferscheinpositionDtoI.getLieferscheinIId(),
		// lieferscheinpositionDtoI.getISort());
		// iIdLieferscheinposition = createLieferscheinposition(
		// lieferscheinpositionDtoI, true, theClientDto);
		//
		// return iIdLieferscheinposition;
	}

	private Integer updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Integer iIdLieferscheinposition = null;

		// die Lagerbuchung und die Lieferscheinposition korrigieren
		LieferscheinpositionDto lieferscheinpositionBisherDto = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);
		removeLieferscheinposition(lieferscheinpositionBisherDto, theClientDto);
		sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
				lieferscheinpositionDtoI.getLieferscheinIId(),
				lieferscheinpositionDtoI.getISort());
		iIdLieferscheinposition = createLieferscheinposition(
				lieferscheinpositionDtoI, true, identities, theClientDto);

		return iIdLieferscheinposition;
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem nicht
	 * lagerbewirtschafteten Artikel aktualisieren. <br>
	 * Eine solche Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void updateLieferscheinpositionIdentChargennr(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// es duerfen keine mengen- und somit lagerrelevanten Teile veraendert
		// werden
		updateLieferscheinstatusLieferscheinposition(lieferscheinpositionDtoI,
				theClientDto);
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem nicht
	 * lagerbewirtschafteten Artikel aktualisieren. <br>
	 * Eine solche Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws RemoteException 
	 */
	private Integer updateLieferscheinpositionIdentSeriennr(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdLieferscheinposition = null;
		iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
				lieferscheinpositionDtoI, theClientDto);
		return iIdLieferscheinposition;
	}

	/**
	 * Feststellen, ob eine Lieferscheinposition einen lagerbewirtschafteten
	 * Artikel enthaelt.
	 * 
	 * @param iIdLieferscheinpositionI
	 *            PK der Lieferscheinposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return boolean true, wenn die Lieferscheinposition einen
	 *         lagerbewirtschafteten Artikel enthaelt
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdLieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinpositionI == null"));
		}

		boolean bLagerbewirtschaftet = false;

		Lieferscheinposition lieferscheinposition = null;

		lieferscheinposition = em.find(Lieferscheinposition.class,
				iIdLieferscheinpositionI);
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (lieferscheinposition.getArtikelIId() != null) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					lieferscheinposition.getArtikelIId(), theClientDto);

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				bLagerbewirtschaftet = true;
			}
		}

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return bLagerbewirtschaftet;
	}

	/**
	 * Spezielle Methode zum Erfassen einer mengenbehafteten auftragbezogenen
	 * Lieferscheinposition aus der Sicht Auftrag heraus. Diese Methode wird nur
	 * dann aufgerufen, wenn zu einer Auftragposition bereits eine
	 * Lieferscheinposition erfasst wurde. Das bedeutet, dass die eingegebene
	 * Menge zur bestehenden Menge addiert werden muss. <br>
	 * Es gilt: Die eingegebene Menge muss > 0 sein.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            dieser Teil der Auftragposition wird als Lieferscheinposition
	 *            erfasst
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinpositionSichtAuftrag(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		try {
			// IMS 2129
			if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
				getLieferscheinFac()
						.pruefeUndSetzeLieferscheinstatusBeiAenderung(
								lieferscheinpositionDtoI.getLieferscheinIId());

				BigDecimal nZusaetzlicheMenge = lieferscheinpositionDtoI
						.getNMenge();

				if (nZusaetzlicheMenge.doubleValue() > 0) {

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Lieferscheinposition.

					Lieferscheinposition lieferscheinpositionBisher = em.find(
							Lieferscheinposition.class,
							lieferscheinpositionDtoI.getIId());
					if (lieferscheinpositionBisher == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
					}

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					lieferscheinpositionDtoI
							.setNMenge(lieferscheinpositionBisher.getNMenge()
									.add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (lieferscheinpositionDtoI
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// die Lagerbuchung ist absolut

						bucheAbLager(lieferscheinpositionDtoI, theClientDto);
						// die Lieferscheinposition mit den neuen Werten
						// aktualisieren
						lieferscheinpositionBisher
								.setNMenge(lieferscheinpositionDtoI.getNMenge());
						em.merge(lieferscheinpositionBisher);
						em.flush();

					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN,
								new Exception("nMenge <= 0"));
					}
				} else if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					getLieferscheinFac()
							.pruefeUndSetzeLieferscheinstatusBeiAenderung(
									lieferscheinpositionDtoI
											.getLieferscheinIId());

					nZusaetzlicheMenge = lieferscheinpositionDtoI.getNMenge();

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Lieferscheinposition.

					Lieferscheinposition lieferscheinpositionBisher = em.find(
							Lieferscheinposition.class,
							lieferscheinpositionDtoI.getIId());

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					lieferscheinpositionDtoI
							.setNMenge(lieferscheinpositionBisher.getNMenge()
									.add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (lieferscheinpositionDtoI
							.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// die Lagerbuchung ist absolut
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(
										lieferscheinpositionDtoI
												.getArtikelIId(),
										theClientDto);

						bucheZuLager(lieferscheinpositionDtoI, theClientDto);
					}
					// die Lieferscheinposition mit den neuen Werten
					// aktualisieren
					lieferscheinpositionBisher
							.setNMenge(lieferscheinpositionDtoI.getNMenge());

				}
			}
			getAuftragpositionFac().updateOffeneMengeAuftragposition(
					lieferscheinpositionDtoI.getAuftragpositionIId(),
					theClientDto);
			getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}
	}

	/**
	 * Aktualisieren einer Lieferscheinposition ohne weitere Aktion.
	 * 
	 * @param lieferscheinpositionDtoI
	 *            die Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @throws RemoteException 
	 */
	public void updateLieferscheinpositionOhneWeitereAktion(
			LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		Lieferscheinposition lieferscheinposition = em.find(
				Lieferscheinposition.class, lieferscheinpositionDtoI.getIId());
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheinpositionFromLieferscheinpositionDto(
				lieferscheinposition, lieferscheinpositionDtoI);
		befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(),
				theClientDto);
		getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
	}

	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		updateLieferscheinpositionAusRechnung(oLieferscheinpositionDtoI,
				rechnungpositionIId,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
	}

	/**
	 * Eine Lieferscheinposition aus der Rechnung nachtraeglich bearbeiten. <br>
	 * Chargenbehaftete Lieferscheinpositionen koennen nicht aktualisiert
	 * werden.
	 * 
	 * @param oLieferscheinpositionDtoI
	 *            die aktuelle Lieferscheinposition
	 * @param rechnungpositionIId
	 *            Integer
	 * @param snrs die Seriennummern/Chargeninfos
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateLieferscheinpositionAusRechnung(
			LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId,
			List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		try {
			LieferscheinDto lsDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							oLieferscheinpositionDtoI.getLieferscheinIId(),
							theClientDto);
			// Id der Rechnung merken
			Integer rechnungIId = lsDto.getRechnungIId();
			// Status wieder auf geliefert setzen
			getLieferscheinFac().setzeStatusLieferschein(
					oLieferscheinpositionDtoI.getLieferscheinIId(),
					LieferscheinFac.LSSTATUS_GELIEFERT, null, theClientDto);
			// jetzt kann die Position upgedatet werden
			if (oLieferscheinpositionDtoI
					.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
				updateLieferscheinpositionHandeingabe(
						oLieferscheinpositionDtoI, theClientDto);
			} else if (oLieferscheinpositionDtoI
					.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								oLieferscheinpositionDtoI.getArtikelIId(),
								theClientDto);

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
					updateLieferscheinpositionIdentChargennr(
							oLieferscheinpositionDtoI, theClientDto);
				} else if (Helper.short2boolean(artikelDto
						.getBSeriennrtragend())) {
					updateLieferscheinpositionIdentSeriennr(
							oLieferscheinpositionDtoI, theClientDto);
				} else {
					// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
					// auf KEIN LAGER)
					updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
							oLieferscheinpositionDtoI, snrs, theClientDto);
				}
			} else {
				// die Lieferscheinposition und den Lieferscheinstatus
				// korrigieren
				updateLieferscheinstatusLieferscheinposition(
						oLieferscheinpositionDtoI, theClientDto);
			}
			// jetzt den LS wieder aktivieren
			getLieferscheinFac().aktiviereLieferschein(
					oLieferscheinpositionDtoI.getLieferscheinIId(),
					theClientDto);
			// Lieferschein wieder auf verrechnet setzen
			getLieferscheinFac().setzeStatusLieferschein(
					oLieferscheinpositionDtoI.getLieferscheinIId(),
					LieferscheinFac.LSSTATUS_VERRECHNET, rechnungIId,
					theClientDto);
			// jetzt kommt noch ein update auf die Rechnungsposition, damit der
			// Preis wieder stimmt
			RechnungPositionDto rePosDto = getRechnungFac()
					.rechnungPositionFindByPrimaryKey(rechnungpositionIId);
			getRechnungFac().updateRechnungPosition(rePosDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void pruefePflichtfelderBelegposition(
			LieferscheinpositionDto lsPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDtoVerkauf(lsPosDto, theClientDto);

		/**
		 * @todo hier spezielle positionsarten pruefen
		 */
	}

	/*
	 * Pr&uuml;ft, bzw. ersetzt Preise im Lager, die nicht in Mandantenwaehrung
	 * sind
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pruefeVKPreisAufLagerbewegung(TheClientDto theClientDto)
			throws EJBExceptionLP {

		// Lieferschein

		Query query = em
				.createNamedQuery("LieferscheinpositionfindByPositionartCNr");
		query.setParameter(1,
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		Collection<?> cl = query.getResultList();
		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Lieferscheinposition position = (Lieferscheinposition) iterator
					.next();
			Query queryLB = em
					.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
			queryLB.setParameter(1, LocaleFac.BELEGART_LIEFERSCHEIN);
			queryLB.setParameter(2, position.getIId());
			queryLB.setParameter(3, null);

			LieferscheinDto lieferscheinDto = null;

			try {
				lieferscheinDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(
								position.getLieferscheinIId(), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			Collection<?> clLB = queryLB.getResultList();
			Iterator<?> iteratorLB = clLB.iterator();
			while (iteratorLB.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iteratorLB.next();

				// Lieferschein holen

				BigDecimal preisInMandantenwaehrung = position
						.getNNettogesamtpreisplusversteckteraufschlagminusrabatt();

				if (!lieferscheinDto.getWaehrungCNr().equals(
						theClientDto.getSMandantenwaehrung())
						&& position.getNMenge().doubleValue() != 0) {
					preisInMandantenwaehrung = position
							.getNNettogesamtpreisplusversteckteraufschlagminusrabatt()
							.divide(new BigDecimal(
									lieferscheinDto
											.getFWechselkursmandantwaehrungzubelegwaehrung()),
									4, BigDecimal.ROUND_HALF_EVEN);
				}

				try {
					com.lp.server.artikel.service.LagerbewegungDto lagerbewegungDto = getLagerFac()
							.lagerbewegungFindByPrimaryKey(
									lagerbewegung.getIId());
					lagerbewegungDto
							.setNVerkaufspreis(preisInMandantenwaehrung);
					System.out.println("XXXXXXXXXXXXXXXXXXX "
							+ position.getIId() + "  "
							+ preisInMandantenwaehrung);
					getLagerFac().updateLagerbewegung(lagerbewegungDto,
							theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}

		// Rechnung

		query = em.createNamedQuery("RechnungpositionfindByPositionartCNr");
		query.setParameter(1,
				LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		Collection<?> clRechpos = query.getResultList();
		Iterator<?> iteratorRechpos = clRechpos.iterator();
		while (iteratorRechpos.hasNext()) {
			Rechnungposition position = (Rechnungposition) iteratorRechpos
					.next();
			Query queryLB = em
					.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
			queryLB.setParameter(1, LocaleFac.BELEGART_RECHNUNG);
			queryLB.setParameter(2, position.getIId());

			RechnungDto rechnungDto = null;

			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(
						position.getRechnungIId());

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			Collection<?> clLB = queryLB.getResultList();
			Iterator<?> iteratorLB = clLB.iterator();
			while (iteratorLB.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iteratorLB.next();

				BigDecimal preisInMandantenwaehrung = position
						.getNNettoeinzelpreisplusaufschlagminusrabatt();

				if (!rechnungDto.getWaehrungCNr().equals(
						theClientDto.getSMandantenwaehrung())
						&& position.getNMenge().doubleValue() != 0) {
					preisInMandantenwaehrung = position
							.getNNettoeinzelpreisplusaufschlagminusrabatt()
							.divide(new BigDecimal(
									rechnungDto
											.getFWechselkursmandantwaehrungzubelegwaehrung()),
									4, BigDecimal.ROUND_HALF_EVEN);
				}

				try {
					com.lp.server.artikel.service.LagerbewegungDto lagerbewegungDto = getLagerFac()
							.lagerbewegungFindByPrimaryKey(
									lagerbewegung.getIId());
					lagerbewegungDto
							.setNVerkaufspreis(preisInMandantenwaehrung);
					System.out.println("XXXXXXXXXXXXXXXXXXX "
							+ position.getIId() + "  "
							+ preisInMandantenwaehrung);
					getLagerFac().updateLagerbewegung(lagerbewegungDto,
							theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}

	}

	private LieferscheinDto findLieferscheinByLieferscheinpositionIId(
			Integer lieferscheinpositionIId) {
		if (null == lieferscheinpositionIId)
			return null;

		Lieferscheinposition lieferscheinposition = em.find(
				Lieferscheinposition.class, lieferscheinpositionIId);
		return getLieferscheinFac().lieferscheinFindByPrimaryKey(
				lieferscheinposition.getLieferscheinIId());

	}

	public Integer getLSPositionIIdFromPositionNummer(Integer lieferscheinIId,
			Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionIIdFromPositionNummer(lieferscheinIId,
				position, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getPositionIIdFromPositionNummer(Integer lieferscheinIId,
			Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(
				getAuftragpositionFac(), getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(lieferscheinIId));
		return numberHandler.getPositionIIdFromPositionNummer(lieferscheinIId,
				position, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionNummer(lsposIId,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(
				getAuftragpositionFac(),
				findLieferscheinByLieferscheinpositionIId(lsposIId));
		return numberHandler.getPositionNummer(lsposIId,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSLastPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(lsposIId,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLastPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(
				getAuftragpositionFac(),
				findLieferscheinByLieferscheinpositionIId(lsposIId));
		return numberHandler.getLastPositionNummer(lsposIId,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSHighestPositionNumber(Integer lieferscheinIId) {
		LieferscheinpositionDto lieferscheinposDtos[] = lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);
		if (lieferscheinposDtos.length == 0)
			return 0;

		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(
				lieferscheinposDtos[lieferscheinposDtos.length - 1].getIId(),
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getHighestPositionNumber(Integer lieferscheinIId)
			throws EJBExceptionLP {
		LieferscheinpositionDto lieferscheinposDtos[] = lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);
		if (lieferscheinposDtos.length == 0)
			return 0;

		return getLastPositionNummer(lieferscheinposDtos[lieferscheinposDtos.length - 1]
				.getIId());
	}

	public boolean pruefeAufGleichenMwstSatz(Integer auftragIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		LieferscheinpositionDto dtos[] = lieferscheinpositionFindByLieferscheinIId(auftragIId);

		return getBelegVerkaufFac().pruefeAufGleichenMwstSatz(dtos,
				vonPositionNumber, bisPositionNumber);
	}

}
