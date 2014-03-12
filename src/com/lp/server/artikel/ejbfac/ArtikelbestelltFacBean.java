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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.ejb.Artikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.service.ArtikelbestelltDto;
import com.lp.server.artikel.service.ArtikelbestelltDtoAssembler;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionReport;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;

@Stateless
public class ArtikelbestelltFacBean extends Facade implements
		ArtikelbestelltFac {
	@PersistenceContext
	private EntityManager em;

	public void createArtikelbestellt(ArtikelbestelltDto artikelbestelltDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelbestelltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelbestelltDto == null"));
		}
		if (artikelbestelltDto.getNMenge() == null
				|| artikelbestelltDto.getCBelegartnr() == null
				|| artikelbestelltDto.getIBelegartpositionid() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"artikelbestelltDto.getNMenge() == null || artikelbestelltDto.getBelegartCNr() == null || artikelbestelltDto.getBelegartpositionIId() == null"));
		}
		if (artikelbestelltDto.getNMenge().doubleValue() <= 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN,
					new Exception(
							"artikelbestelltDto.getNMenge().doubleValue() <= 0"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelreservierungfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, artikelbestelltDto.getCBelegartnr());
			query.setParameter(2, artikelbestelltDto.getIBelegartpositionid());
			Artikelbestellt doppelt = (Artikelbestellt) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("WW_ARTIKELBESTELLT.UK"));
		} catch (NoResultException ex) {

		}
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELBESTELLT);
			artikelbestelltDto.setIId(pk);

			Artikelbestellt artikelreservierung = new Artikelbestellt(
					artikelbestelltDto.getIId(),
					artikelbestelltDto.getCBelegartnr(),
					artikelbestelltDto.getIBelegartpositionid(),
					artikelbestelltDto.getArtikelIId(),
					artikelbestelltDto.getTLiefertermin(),
					artikelbestelltDto.getNMenge());
			em.persist(artikelreservierung);
			em.flush();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeArtikelbestellt(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP {
		myLogger.entry();
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("ArtikelbestelltfindByBelegartCNrIBelegartpositionid");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		Artikelbestellt artikelbestellt = (Artikelbestellt) query
				.getSingleResult();
		if (artikelbestellt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei removeArtikelbestellt. Es gibt keinen Eintrag f\u00FCr Belegart "
							+ belegartCNr + " und Positioniid "
							+ belegartpositionIId);
		}
		try {
			em.remove(artikelbestellt);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }

	}

	public void updateArtikelbestellt(ArtikelbestelltDto artikelbestelltDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelbestelltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelbestelltDto == null"));

		}
		if (artikelbestelltDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelbestelltDto.getIId() == null"));

		}
		if (artikelbestelltDto.getIBelegartpositionid() == null
				|| artikelbestelltDto.getCBelegartnr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelbestelltDto.getBelegartpositionIId() == null || artikelbestelltDto.getBelegartCNr() == null"));

		}
		// JO 23.3.06 wenn negativ, dann Ueberlieferung um die Menge
		// if (artikelbestelltDto.getNMenge() == null) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_FELD_IN_DTO_IS_NULL,
		// new Exception(
		// "artikelbestelltDto.getFMenge() == null"));
		// }
		// if (artikelbestelltDto.getNMenge().doubleValue() <= 0) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_MUSS_GROESSER_0_SEIN,
		// new Exception(
		// "artikelbestelltDto.getFMenge().doubleValue() <= 0"));
		// }

		// try {
		Artikelbestellt artikelbestellt = em.find(Artikelbestellt.class,
				artikelbestelltDto.getIId());
		if (artikelbestellt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei updateArtikelbestellt. Es gibt keine iid "
							+ artikelbestelltDto.getIId()
							+ "\nartikelbestelltDto.toString: "
							+ artikelbestelltDto.toString());
		}

		// JO: 23.3.06
		artikelbestellt
				.setNMenge(artikelbestelltDto.getNMenge().doubleValue() < 0 ? new BigDecimal(
						0) : artikelbestelltDto.getNMenge());

		artikelbestellt.setArtikelIId(artikelbestelltDto.getArtikelIId());
		artikelbestellt.setTLiefertermin(artikelbestelltDto.getTLiefertermin());
		artikelbestellt.setCBelegartnr(artikelbestelltDto.getCBelegartnr());
		artikelbestellt.setIBelegartpositionid(artikelbestelltDto
				.getIBelegartpositionid());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
		em.merge(artikelbestellt);
		em.flush();

	}

	public void updateArtikelbestelltRelativ(
			ArtikelbestelltDto artikelbestelltDto) throws EJBExceptionLP {
		myLogger.entry();
		if (artikelbestelltDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelbestelltDto == null"));

		}
		if (artikelbestelltDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelbestelltDto.getIId() == null"));

		}
		if (artikelbestelltDto.getIBelegartpositionid() == null
				|| artikelbestelltDto.getCBelegartnr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelbestelltDto.getBelegartpositionIId() == null || artikelbestelltDto.getBelegartCNr() == null"));

		}
		if (artikelbestelltDto.getTLiefertermin() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"artikelbestelltDto.getDLiefertermin() == null"));

		}
		if (artikelbestelltDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"auftragreservierungDto.getArtikelIId() == null"));

		}
		if (artikelbestelltDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception("artikelbestelltDto.getNMenge() == null"));
		}

		// try {
		Artikelbestellt artikelbestellt = em.find(Artikelbestellt.class,
				artikelbestelltDto.getIId());
		if (artikelbestellt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateArtikelbestelltRelativ. Es gibt keine iid "
							+ artikelbestelltDto.getIId()
							+ "\nartikelbestelltDto.toString: "
							+ artikelbestelltDto.toString());
		}
		/** Alter Wert + neuer Relativer Wert = neuer Wert */
		BigDecimal valueNeu = artikelbestellt.getNMenge().add(artikelbestelltDto.getNMenge());

		artikelbestellt.setNMenge(valueNeu);
		artikelbestellt.setArtikelIId(artikelbestelltDto.getArtikelIId());
		artikelbestellt.setTLiefertermin(artikelbestelltDto.getTLiefertermin());
		artikelbestellt.setCBelegartnr(artikelbestelltDto.getCBelegartnr());
		artikelbestellt.setIBelegartpositionid(artikelbestelltDto
				.getIBelegartpositionid());
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public ArtikelbestelltDto artikelbestelltFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Artikelbestellt artikelbestellt = em.find(Artikelbestellt.class, iId);
		if (artikelbestellt == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei ArtikelbestelltFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleArtikelbestelltDto(artikelbestellt);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public ArtikelbestelltDto artikelbestelltFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelbestelltfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelbestellt artikelbestellt = (Artikelbestellt) query
					.getSingleResult();
			return assembleArtikelbestelltDto(artikelbestellt);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei artikelbestelltFindByBelegartCNrBelegartPositioniid. Es gab mehr als ein Ergebnis f\u00FCr Belegart "
							+ belegartCNr
							+ " und BelegpositionIId "
							+ belegartpositionIId);
		}
	}

	public ArtikelbestelltDto artikelbestelltFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		ArtikelbestelltDto artikelbestelltDto = null;
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelbestelltfindByBelegartCNrIBelegartpositionid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelbestellt artikelbestellt = (Artikelbestellt) query
					.getSingleResult();
			if (artikelbestellt == null) {
				return null;
			}
			// @todo getSingleResult oder getResultList ?
			artikelbestelltDto = assembleArtikelbestelltDto(artikelbestellt);
			// }
			// catch (javax.ejb.ObjectNotFoundException e) {
			// // nothing here
		} catch (NoResultException e) {

		}
		return artikelbestelltDto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getAnzahlBestellt(Integer artikelIId)
			throws EJBExceptionLP {
		myLogger.entry();
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("ArtikelbestelltfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> allAuftragreservierungen = query.getResultList();
		// if (allAuftragreservierungen.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, null);
		// }
		Iterator<?> iter = allAuftragreservierungen.iterator();
		BigDecimal f = new BigDecimal(0);
		while (iter.hasNext()) {
			Artikelbestellt artikelbestelltTemp = (Artikelbestellt) iter.next();

			if (artikelbestelltTemp.getNMenge() != null) {
				f = f.add(artikelbestelltTemp.getNMenge());
			}
		}

		return f;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, ex);
		// }

	}

	private void setArtikelbestelltFromArtikelbestelltDto(
			Artikelbestellt artikelbestellt,
			ArtikelbestelltDto artikelbestelltDto) {
		artikelbestellt.setCBelegartnr(artikelbestelltDto.getCBelegartnr());
		artikelbestellt.setIBelegartpositionid(artikelbestelltDto
				.getIBelegartpositionid());
		artikelbestellt.setArtikelIId(artikelbestelltDto.getArtikelIId());
		artikelbestellt.setTLiefertermin(artikelbestelltDto.getTLiefertermin());
		artikelbestellt.setNMenge(artikelbestelltDto.getNMenge());
		em.merge(artikelbestellt);
		em.flush();
	}

	private ArtikelbestelltDto assembleArtikelbestelltDto(
			Artikelbestellt artikelbestellt) {
		return ArtikelbestelltDtoAssembler.createDto(artikelbestellt);
	}

	private ArtikelbestelltDto[] assembleArtikelbestelltDtos(
			Collection<?> artikelbestellts) {
		List<ArtikelbestelltDto> list = new ArrayList<ArtikelbestelltDto>();
		if (artikelbestellts != null) {
			Iterator<?> iterator = artikelbestellts.iterator();
			while (iterator.hasNext()) {
				Artikelbestellt artikelbestellt = (Artikelbestellt) iterator
						.next();
				list.add(assembleArtikelbestelltDto(artikelbestellt));
			}
		}
		ArtikelbestelltDto[] returnArray = new ArtikelbestelltDto[list.size()];
		return (ArtikelbestelltDto[]) list.toArray(returnArray);
	}

	public void aktualisiereBestelltListe(Integer bestellpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// Den eventuell schon vorhandenen Eintrag in der Bestelltliste
			// suchen.
			ArtikelbestelltDto aBestelltDto = artikelbestelltFindByBelegartCNrBelegartPositionIIdOhneExc(
					LocaleFac.BELEGART_BESTELLUNG, bestellpositionIId);
			// die Bestellposition holen
			BestellpositionDto bestposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKeyOhneExc(bestellpositionIId);
			if (bestposDto == null
					|| !bestposDto.getPositionsartCNr().equals(
							BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
				// es gibt keine Bestellposition mit dieser ID oder die
				// Bestellposition ist kein Artikel
				if (aBestelltDto != null) {
					// den Bestellteintrag loeschen.
					removeArtikelbestellt(LocaleFac.BELEGART_BESTELLUNG,
							bestellpositionIId);
				}
			} else {
				// Auch der Status und die Art der Bestellung sind wichtig.
				BestellungDto bsDto = getBestellungFac()
						.bestellungFindByPrimaryKey(
								bestposDto.getBestellungIId());
				boolean bRahmenbestellung = bsDto.getBestellungartCNr().equals(
						BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR);
				boolean bBestellungStorniert = bsDto.getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_STORNIERT);
				boolean bBestellungErledigt = bsDto.getStatusCNr()
						.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT);
				boolean bBestellpositionErledigt = bestposDto
						.getBestellpositionstatusCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT);
				BigDecimal bdOffeneMenge = getBestellpositionFac()
						.berechneOffeneMenge(bestposDto);
				boolean bEsIstNichtsMehrOffen = bdOffeneMenge
						.compareTo(new BigDecimal(0)) <= 0;
				// Kein Eintrag fuer Rahmenbest, stornierte oder erledigte,
				// sowie bereits vollstaendig gelieferte Positionen.
				if (bRahmenbestellung || bBestellungStorniert
						|| bBestellungErledigt || bBestellpositionErledigt
						|| bEsIstNichtsMehrOffen) {
					if (aBestelltDto != null) {
						// den Bestellteintrag loeschen.
						removeArtikelbestellt(LocaleFac.BELEGART_BESTELLUNG,
								bestellpositionIId);
					}
				} else {
					// es wird ein Eintrag benoetigt.
					if (aBestelltDto == null) {
						aBestelltDto = new ArtikelbestelltDto();
					}
					aBestelltDto.setArtikelIId(bestposDto.getArtikelIId());
					aBestelltDto.setCBelegartnr(LocaleFac.BELEGART_BESTELLUNG);
					aBestelltDto.setIBelegartpositionid(bestposDto.getIId());
					// offene Menge neu berechnen
					aBestelltDto.setNMenge(bdOffeneMenge);
					// wenn liefertermin in Bestellvorschlag uebersteuert dann
					// von dort sonst
					// setzen des liefertermins der bestellung
					if (bestposDto.getTUebersteuerterLiefertermin() != null) {
						aBestelltDto.setTLiefertermin(bestposDto
								.getTUebersteuerterLiefertermin());
					} else {
						aBestelltDto.setTLiefertermin(new Timestamp(bsDto
								.getDLiefertermin().getTime()));
					}
					if (aBestelltDto.getIId() == null) {
						// ein neuer -> anlegen
						createArtikelbestellt(aBestelltDto);
					} else {
						// update
						updateArtikelbestellt(aBestelltDto);
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Bestelltliste pruefen. 1. Bestehende Eintraege. 2. Bestellungen pruefen,
	 * ob die Bestelltwerte richtig eingetragen sind.
	 * 
	 * @param theClientDto der aktuelle Benutzer 
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pruefeBestelltliste(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		try {

			session = FLRSessionFactory.getFactory().openSession();

			String hqlDelete = "delete FROM FLRArtikelbestellt";
			session.createQuery(hqlDelete).executeUpdate();

			session.close();

			// ------------------------------------------------------------------
			// ----
			// Alle Eintraege in den Bestellungen pruefen
			// ------------------------------------------------------------------
			// ----

			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria besPos = session
					.createCriteria(FLRBestellposition.class);
			org.hibernate.Criteria best = besPos
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			// Filter auf den Mandanten
			best.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// keine Rahmenbestellungen.
			best.add(Restrictions.not(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)));
			// keine stornierten und erledigten.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(BestellungFac.BESTELLSTATUS_STORNIERT);
			cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
			best.add(Restrictions.not(Restrictions.in(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati)));
			// Query ausfuehren
			List<?> besposList = besPos.list();
			Iterator<?> besposListIterator = besposList.iterator();
			while (besposListIterator.hasNext()) {
				FLRBestellposition bespos = (FLRBestellposition) besposListIterator
						.next();
				if (bespos.getBestellpositionart_c_nr().equals(
						BestellpositionFac.BESTELLPOSITIONART_IDENT)
						|| bespos
								.getBestellpositionart_c_nr()
								.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
					if (bespos.getBestellpositionstatus_c_nr() != null
							&& (bespos
									.getBestellpositionstatus_c_nr()
									.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN)
									|| bespos
											.getBestellpositionstatus_c_nr()
											.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_BESTAETIGT) || bespos
									.getBestellpositionstatus_c_nr()
									.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_TEILGELIEFERT))) {

						// nur fuer Artikel
						if (bespos.getFlrartikel() != null) {
							BigDecimal bdMenge = new BigDecimal(0);
							if (bespos.getN_offenemenge() != null) {
								bdMenge = bespos.getN_offenemenge();
							} else {
								bdMenge = bespos.getN_menge();
							}
							ArtikelbestelltDto artikelBestelltDto = new ArtikelbestelltDto();
							artikelBestelltDto.setArtikelIId(bespos
									.getFlrartikel().getI_id());
							artikelBestelltDto
									.setCBelegartnr(LocaleFac.BELEGART_BESTELLUNG);
							artikelBestelltDto.setIBelegartpositionid(bespos
									.getI_id());
							artikelBestelltDto.setNMenge(bdMenge);

							if (bespos.getT_uebersteuerterliefertermin() != null) {
								artikelBestelltDto
										.setTLiefertermin(new java.sql.Timestamp(
												bespos.getT_uebersteuerterliefertermin()
														.getTime()));
							} else {
								artikelBestelltDto
										.setTLiefertermin(new java.sql.Timestamp(
												bespos.getFlrbestellung()
														.getT_liefertermin()
														.getTime()));
							}

							// anlegen, negative Mengen werden ignoriert
							if (artikelBestelltDto.getNMenge().compareTo(
									new BigDecimal(0)) > 0) {
								getArtikelbestelltFac().createArtikelbestellt(
										artikelBestelltDto);
								myLogger.warn(theClientDto.getIDUser(),
										"Bestelltliste nachgetragen: "
												+ artikelBestelltDto);
							}
						}

					}
				}
				// Fuer allen anderen Stati darf es keine Reservierungen geben.
				else {
					// Schaun, ob es eine Reservierung gibt
					ArtikelbestelltDto artikelBestelltDto = artikelbestelltFindByBelegartCNrBelegartPositionIIdOhneExc(
							LocaleFac.BELEGART_BESTELLUNG, bespos.getI_id());
					// wenn ja, dann loeschen
					if (artikelBestelltDto != null) {
						Artikelbestellt toRemove = em.find(
								Artikelbestellt.class,
								artikelBestelltDto.getIId());
						if (toRemove == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
									"Fehler beo prufeBestelltliste. Artikelbestellt das Aufgrund von reservierung gel\u00F6scht werden soll konnte nicht gefunden werden. iid "
											+ artikelBestelltDto.getIId());
						}
						try {
							em.remove(toRemove);
							em.flush();
						} catch (EntityExistsException er) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
						}
						myLogger.warn(theClientDto.getIDUser(),
								"Bestellteintrag gel\u00F6scht: "
										+ artikelBestelltDto);
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public Hashtable getAnzahlRahmenbestellt(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal bdReserviert = new BigDecimal(0);
		Hashtable<String, Object> retHashtable = new Hashtable<String, Object>();
		String sBestellungCNr = null;
		Collection<String> aBestellungsCNr = new ArrayList<String>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session
					.createCriteria(FLRBestellpositionReport.class);
			Criteria critBestellung = crit
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);

			Criteria critArtikel = crit
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
			// nur diesen Artikel
			critArtikel.add(Restrictions.eq("i_id", artikelIId));
			// Filter nach Mandant
			critBestellung.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// keine stornierten oder erledigten Bestellungen.
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(BestellungFac.BESTELLSTATUS_STORNIERT);
			cStati.add(BestellungFac.BESTELLSTATUS_ERLEDIGT);
			critBestellung
					.add(Restrictions.not(Restrictions.in(
							BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
							cStati)));
			// Nur Rahmenbestellungen
			critBestellung.add(Restrictions.eq(
					BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
					BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
			// Query ausfuehren
			List<?> list = crit.list();
			FLRBestellpositionReport[] aResult = new FLRBestellpositionReport[list
					.size()];
			aResult = (FLRBestellpositionReport[]) list.toArray(aResult);
			for (int i = 0; i < aResult.length; i++) {
				// Rahmenbestellnr, Achtung nur einmal pro Positionen noetig.
				if (!aBestellungsCNr.contains(aResult[i].getFlrbestellung()
						.getC_nr())) {
					aBestellungsCNr
							.add(aResult[i].getFlrbestellung().getC_nr());
				}
				// negative Rahmenreservierungen bleiben unberuecksichtigt.
				if (aResult[i].getN_offenemenge() != null
						&& aResult[i].getN_offenemenge().doubleValue() > 0) {
					bdReserviert = bdReserviert.add(aResult[i]
							.getN_offenemenge());
				}
			}

			if (bdReserviert != null) {
				retHashtable.put(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL,
						bdReserviert);
			}
			if (aBestellungsCNr.size() > 0) {
				retHashtable.put(
						ArtikelbestelltFac.KEY_RAHMENBESTELLT_BELEGCNR,
						aBestellungsCNr);
			}
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return retHashtable;
	}

	/**
	 * getArtikelbestellt gibt eine List vom FLRArtikelbestellt zurueck. Achtung
	 * casten notwendig.
	 * 
	 * @param artikelIId
	 *            Integer
	 * @param dVon
	 *            Date
	 * @param dBis
	 *            Date
	 * @return List
	 * @throws EJBExceptionLP
	 */
	public Collection<FLRArtikelbestellt> getArtikelbestellt(
			Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis)
			throws EJBExceptionLP {

		List<FLRArtikelbestellt> aFLRArtikelbestellt = new ArrayList<FLRArtikelbestellt>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria bestelltliste = session
				.createCriteria(FLRArtikelbestellt.class);
		bestelltliste.createAlias(
				ArtikelbestelltFac.FLR_ARTIKELBESTELLT_FLRARTIKEL, "a").add(
				Restrictions.eq("a.i_id", artikelIId));
		if (dVon != null) {
			bestelltliste.add(Restrictions
					.ge(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN,
							dVon));
		}
		if (dBis != null) {
			bestelltliste.add(Restrictions
					.lt(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN,
							dBis));
		}

		bestelltliste.addOrder(Order
				.asc(ArtikelbestelltFac.FLR_ARTIKELBESTELLT_D_LIEFERTERMIN));

		List<?> resultList = bestelltliste.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLRArtikelbestellt artikelbestellt = (FLRArtikelbestellt) resultListIterator
					.next();
			aFLRArtikelbestellt.add(artikelbestellt);
		}

		return aFLRArtikelbestellt;
	}

}
