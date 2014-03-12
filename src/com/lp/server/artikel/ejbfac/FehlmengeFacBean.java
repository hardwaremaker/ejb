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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.ejb.Artikelfehlmenge;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDtoAssembler;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FehlmengeFacBean extends Facade implements FehlmengeFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public void createArtikelfehlmenge(ArtikelfehlmengeDto artikelfehlmengeDto)
			throws EJBExceptionLP {
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_FEHLMENGE);
		artikelfehlmengeDto.setIId(iId);
		try {
			Artikelfehlmenge artikelfehlmenge = new Artikelfehlmenge(
					artikelfehlmengeDto.getIId(),
					artikelfehlmengeDto.getCBelegartnr(),
					artikelfehlmengeDto.getIBelegartpositionid(),
					artikelfehlmengeDto.getArtikelIId(),
					artikelfehlmengeDto.getNMenge(),
					artikelfehlmengeDto.getTLiefertermin());
			em.persist(artikelfehlmenge);
			em.flush();
			setArtikelfehlmengeFromArtikelfehlmengeDto(artikelfehlmenge,
					artikelfehlmengeDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private void removeArtikelfehlmenge(ArtikelfehlmengeDto artikelfehlmengeDto)
			throws EJBExceptionLP {
		// try {
		if (artikelfehlmengeDto != null) {
			Integer iId = artikelfehlmengeDto.getIId();
			Artikelfehlmenge toRemove = em.find(Artikelfehlmenge.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeArtikelfehlmenge. Es gibt keine iid "
								+ iId + "\ndto.toString: "
								+ artikelfehlmengeDto.toString());
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	private void updateArtikelfehlmenge(ArtikelfehlmengeDto artikelfehlmengeDto)
			throws EJBExceptionLP {
		if (artikelfehlmengeDto != null) {
			Integer iId = artikelfehlmengeDto.getIId();
			// try {
			Artikelfehlmenge artikelfehlmenge = em.find(Artikelfehlmenge.class,
					iId);
			if (artikelfehlmenge == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
						"Fehler bei updateArtikelfehlmenge. Es gibt keine iid "
								+ iId + "\ndto.toString: "
								+ artikelfehlmengeDto.toString());
			}
			setArtikelfehlmengeFromArtikelfehlmengeDto(artikelfehlmenge,
					artikelfehlmengeDto);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
			// }
		}
	}

	public void aktualisiereFehlmenge(String belegartCNr,
			Integer belegpositionIId, boolean throwExceptionWhenCreate,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// try {
		if (belegartCNr.equals(LocaleFac.BELEGART_LOS)) {
			// die position holen
			LossollmaterialDto losmat = getFertigungFac()
					.lossollmaterialFindByPrimaryKeyOhneExc(belegpositionIId);
			if (losmat == null) {
				// entweder falsche id uebergeben oder die position wurde gerade
				// geloescht
				// also auch den eintrag in der fehlmengenliste loeschen
				try {
					removeArtikelfehlmenge(LocaleFac.BELEGART_LOS,
							belegpositionIId);

				} catch (Throwable ex) {
					// nix tun
					// das kann passieren wenn eine position geloescht wird, die
					// keinen artikel hat
				}
				return;
			}

			// nur fuer Artikel
			if (losmat.getArtikelIId() != null) {
				LosDto losDto = getFertigungFac().losFindByPrimaryKey(
						losmat.getLosIId());
				boolean bFehlmengeZulaessig = true;
				if (losDto.getStatusCNr().equals(FertigungFac.STATUS_STORNIERT)
						|| losDto.getStatusCNr().equals(
								FertigungFac.STATUS_ANGELEGT)
						|| losDto.getStatusCNr().equals(
								FertigungFac.STATUS_ERLEDIGT)) {
					bFehlmengeZulaessig = false;
				}
				// Eintrag suchen
				ArtikelfehlmengeDto aFehlmengeDto = null;
				try {
					Query query = em
							.createNamedQuery("ArtikelfehlmengefindByBelegartCNrBelegartPositionIId");
					query.setParameter(1, LocaleFac.BELEGART_LOS);
					query.setParameter(2, losmat.getIId());
					Artikelfehlmenge aFehlmenge = (Artikelfehlmenge) query
							.getSingleResult();
					aFehlmengeDto = assembleArtikelfehlmengeDto(aFehlmenge);
					aFehlmengeDto.setArtikelIId(losmat.getArtikelIId());
					// fuer Stati, in denen es keine Fehlmengen gibt, wird der
					// fehlmengeneintrag geloescht
					if (!bFehlmengeZulaessig) {
						removeArtikelfehlmenge(aFehlmengeDto);
					}
				} catch (NoResultException ex) {
					// gibts noch nicht, also muss ein neuer her
					aFehlmengeDto = new ArtikelfehlmengeDto();
					aFehlmengeDto.setArtikelIId(losmat.getArtikelIId());
					aFehlmengeDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
					aFehlmengeDto.setIBelegartpositionid(losmat.getIId());
				}
				// Weitere Aktionen nur dann, wenn das anlegen einer Fehlmenge
				// auch zulaessig ist
				if (bFehlmengeZulaessig) {
					java.sql.Date dTermin;

					BigDecimal bdAusgegeben = getFertigungFac()
							.getAusgegebeneMenge(losmat.getIId(), null,
									theClientDto);

					if (losmat.getNMenge().compareTo(new BigDecimal(0)) > 0) {
						// Positive Fehlmenge: produktionsstart
						dTermin = losDto.getTProduktionsbeginn();

						if (losmat.getNMenge().subtract(bdAusgegeben)
								.doubleValue() > 0) {

							aFehlmengeDto.setNMenge(losmat.getNMenge()
									.subtract(bdAusgegeben));
						} else {
							aFehlmengeDto.setNMenge(new BigDecimal(0));
							if (aFehlmengeDto.getIId() != null) {
								removeArtikelfehlmenge(aFehlmengeDto);
								return;
							}
						}

					} else {
						// Negative Fehlmenge: produktionsende
						dTermin = losDto.getTProduktionsende();
						BigDecimal fehlmenge = losmat.getNMenge().abs()
								.subtract(bdAusgegeben.abs());

						if (fehlmenge.doubleValue() > 0) {
							aFehlmengeDto.setNMenge(losmat.getNMenge()
									.subtract(bdAusgegeben));
						} else {
							aFehlmengeDto.setNMenge(new BigDecimal(0));
						}

					}

					// PJ17994
					dTermin = Helper.addiereTageZuDatum(dTermin,
							losmat.getIBeginnterminoffset());
					aFehlmengeDto.setTLiefertermin(dTermin);

					// eintraege mit menge 0 loeschen bzw. gar nicht speichern
					if (aFehlmengeDto.getNMenge().compareTo(new BigDecimal(0)) == 0) {
						if (aFehlmengeDto.getIId() != null) {
							removeArtikelfehlmenge(
									aFehlmengeDto.getCBelegartnr(),
									aFehlmengeDto.getIBelegartpositionid());
						} else {
							// steht eh nicht in der db
						}
					} else {
						if (aFehlmengeDto.getIId() == null) {
							// ein neuer -> anlegen
							if (throwExceptionWhenCreate) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN,
										"FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN");
							}

							createArtikelfehlmenge(aFehlmengeDto);
						} else {
							// steht eh nicht in der db
							updateArtikelfehlmenge(aFehlmengeDto);
						}
					}
				} else {
					// wenn nicht zulaessig, dann loeschen.

					try {
						Query query = em
								.createNamedQuery("ArtikelfehlmengefindByBelegartCNrBelegartPositionIId");
						query.setParameter(1, aFehlmengeDto.getCBelegartnr());
						query.setParameter(2,
								aFehlmengeDto.getIBelegartpositionid());
						Artikelfehlmenge fehlmenge = (Artikelfehlmenge) query
								.getSingleResult();
						if (fehlmenge != null) {
							try {
								em.remove(fehlmenge);
								em.flush();
							} catch (EntityExistsException ex2) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
										ex2);
							}
						}
					} catch (NoResultException ex1) {
						// Wenn nicht gefunden, dann brauchts auch nicht
						// geloescht zu werden
					}
					// }
					// }
					// }
					// }
					// catch (RemoteException ex) {
					// throwEJBExceptionLPRespectOld(ex);
				}
			}
		}
	}

	public ArtikelfehlmengeDto artikelfehlmengeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Artikelfehlmenge artikelfehlmenge = em.find(Artikelfehlmenge.class,
					iId);
			if (artikelfehlmenge == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei artikelfehlmengeFindByPrimaryKey. Es gibt keine iid "
								+ iId);
			}
			return assembleArtikelfehlmengeDto(artikelfehlmenge);
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	public ArtikelfehlmengeDto[] artikelfehlmengeFindByArtikelIId(
			Integer artikelIId) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ArtikelfehlmengefindByArtikelIId");
		query.setParameter(1, artikelIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleArtikelfehlmengeDtos(cl);
		// }
		// catch (Exception ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setArtikelfehlmengeFromArtikelfehlmengeDto(
			Artikelfehlmenge artikelfehlmenge,
			ArtikelfehlmengeDto artikelfehlmengeDto) {
		artikelfehlmenge.setCBelegartnr(artikelfehlmengeDto.getCBelegartnr());
		artikelfehlmenge.setIBelegartpositionid(artikelfehlmengeDto
				.getIBelegartpositionid());
		artikelfehlmenge.setArtikelIId(artikelfehlmengeDto.getArtikelIId());
		artikelfehlmenge.setNMenge(artikelfehlmengeDto.getNMenge());
		artikelfehlmenge.setTLiefertermin(artikelfehlmengeDto
				.getTLiefertermin());
		em.merge(artikelfehlmenge);
		em.flush();
	}

	private ArtikelfehlmengeDto assembleArtikelfehlmengeDto(
			Artikelfehlmenge artikelfehlmenge) {
		return ArtikelfehlmengeDtoAssembler.createDto(artikelfehlmenge);
	}

	private ArtikelfehlmengeDto[] assembleArtikelfehlmengeDtos(
			Collection<?> artikelfehlmenges) {
		List<ArtikelfehlmengeDto> list = new ArrayList<ArtikelfehlmengeDto>();
		if (artikelfehlmenges != null) {
			Iterator<?> iterator = artikelfehlmenges.iterator();
			while (iterator.hasNext()) {
				Artikelfehlmenge artikelfehlmenge = (Artikelfehlmenge) iterator
						.next();
				list.add(assembleArtikelfehlmengeDto(artikelfehlmenge));
			}
		}
		ArtikelfehlmengeDto[] returnArray = new ArtikelfehlmengeDto[list.size()];
		return (ArtikelfehlmengeDto[]) list.toArray(returnArray);
	}

	public BigDecimal getAnzahlFehlmengeEinesArtikels(Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal bdFehlmenge = new BigDecimal(0);

		ArrayList<?> al = getFehlmengen(artikelIId, theClientDto.getMandant(),
				theClientDto);

		for (int i = 0; i < al.size(); i++) {
			FLRFehlmenge flr = (FLRFehlmenge) al.get(i);
			bdFehlmenge = bdFehlmenge.add(flr.getN_menge());
		}
		return bdFehlmenge;
	}

	public BigDecimal getAnzahlderPositivenFehlmengenEinesArtikels(
			Integer artikelIId, TheClientDto theClientDto) {
		BigDecimal bdFehlmenge = new BigDecimal(0);

		ArrayList<?> al = getFehlmengen(artikelIId, theClientDto.getMandant(),
				theClientDto);

		for (int i = 0; i < al.size(); i++) {
			FLRFehlmenge flr = (FLRFehlmenge) al.get(i);
			if (flr.getN_menge().doubleValue() > 0) {
				bdFehlmenge = bdFehlmenge.add(flr.getN_menge());
			}
		}
		return bdFehlmenge;
	}

	public ArrayList getFehlmengen(Integer artikelIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFehlmenge.class);
			// Filter nach Artikel
			c.add(Restrictions.eq(ArtikelFac.FLR_FEHLMENGE_ARTIKEL_I_ID,
					artikelIId));
			// Filter nach Mandant
			c.createCriteria(ArtikelFac.FLR_FEHLMENGE_FLRLOSSOLLMATERIAL)
					.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS)
					.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
							mandantCNr));
			// Sortierung nach Liefertermin
			c.addOrder(Order.asc(ArtikelFac.FLR_FEHLMENGE_T_LIEFERTERMIN));
			List<?> list = c.list();
			return new ArrayList(list);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIId(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		// myLogger.entry();
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ArtikelfehlmengefindByBelegartCNrBelegartPositionIId");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, belegartpositionIId);
		// @todo getSingleResult oder getResultList ?
		Artikelfehlmenge artikelfehlmenge = (Artikelfehlmenge) query
				.getSingleResult();
		if (artikelfehlmenge == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei artikelfehlmengeFindByBelegartCNrBelegartPositionIId. Es gibt keine Artikelfehlmenge"
							+ "mit belegartpositionIId "
							+ belegartpositionIId
							+ " und belegart " + belegartCNr);
		}
		return assembleArtikelfehlmengeDto(artikelfehlmenge);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public ArtikelfehlmengeDto artikelfehlmengeFindByBelegartCNrBelegartPositionIIdOhneExc(
			String belegartCNr, Integer belegartpositionIId)
			throws EJBExceptionLP {
		if (belegartpositionIId == null || belegartCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"belegartpositionIId == null || belegartCNr == null"));
		}
		try {
			Query query = em
					.createNamedQuery("ArtikelfehlmengefindByBelegartCNrBelegartPositionIId");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, belegartpositionIId);
			Artikelfehlmenge artikelfehlmenge = (Artikelfehlmenge) query
					.getSingleResult();
			return assembleArtikelfehlmengeDto(artikelfehlmenge);
		} catch (NoResultException ex) {
			return null;
		}
	}

	private void removeArtikelfehlmenge(String belegartCNr,
			Integer belegartpositionIId) throws EJBExceptionLP {
		myLogger.entry();
		if (belegartCNr == null || belegartpositionIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"belegartCNr == null || belegartpositionIId == null"));
		}
		ArtikelfehlmengeDto a = artikelfehlmengeFindByBelegartCNrBelegartPositionIId(
				belegartCNr, belegartpositionIId);
		removeArtikelfehlmenge(a);
	}

	/**
	 * Fehlmengen komplett neu erzeugen
	 * 
	 * @param theClientDto der aktuelle Benutzer 
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pruefeFehlmengen(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();

			String hqlDelete = "delete FROM FLRFehlmenge";
			session.createQuery(hqlDelete).executeUpdate();

			session.close();
			session = FLRSessionFactory.getFactory().openSession();

			Criteria cLosmat = session.createCriteria(FLRLossollmaterial.class);
			Criteria cLos = cLosmat
					.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS);
			// Filter nach Status
			Collection<String> cStati = new LinkedList<String>();
			cStati.add(FertigungFac.STATUS_AUSGEGEBEN);
			cStati.add(FertigungFac.STATUS_GESTOPPT);
			cStati.add(FertigungFac.STATUS_IN_PRODUKTION);
			cStati.add(FertigungFac.STATUS_TEILERLEDIGT);
			cLos.add(Restrictions.in(FertigungFac.FLR_LOS_STATUS_C_NR, cStati));

			// Query ausfuehren
			List<?> listLosmat = cLosmat.list();
			for (Iterator<?> iter = listLosmat.iterator(); iter.hasNext();) {
				FLRLossollmaterial item = (FLRLossollmaterial) iter.next();
				BigDecimal bdAusgegeben = getFertigungFac()
						.getAusgegebeneMenge(item.getI_id(), null, theClientDto);
				BigDecimal bdFehlmenge = item.getN_menge().subtract(
						bdAusgegeben.abs());

				// gibt es ein Fehlmenge?
				if (bdFehlmenge.compareTo(new BigDecimal(0)) > 0) {
					// Eintrag suchen
					ArtikelfehlmengeDto fmDto = new ArtikelfehlmengeDto();
					fmDto.setArtikelIId(item.getFlrartikel().getI_id());
					fmDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
					fmDto.setIBelegartpositionid(item.getI_id());
					fmDto.setNMenge(bdFehlmenge);
					fmDto.setTLiefertermin(new java.sql.Date(item.getFlrlos()
							.getT_produktionsbeginn().getTime()));

					// PJ17994
					fmDto.setTLiefertermin(Helper.addiereTageZuDatum(
							new java.sql.Date(item.getFlrlos()
									.getT_produktionsbeginn().getTime()),
							item.getI_beginnterminoffset()));

					context.getBusinessObject(FehlmengeFac.class)
							.createArtikelfehlmenge(fmDto);
					myLogger.warn(theClientDto.getIDUser(),
							"Fehlmenge nachgetragen: " + fmDto);

				} else {

					BigDecimal fehlmenge = item.getN_menge().abs()
							.subtract(bdAusgegeben.abs());

					if (fehlmenge.doubleValue() > 0) {
						ArtikelfehlmengeDto fmDto = new ArtikelfehlmengeDto();
						fmDto.setArtikelIId(item.getFlrartikel().getI_id());
						fmDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
						fmDto.setIBelegartpositionid(item.getI_id());

						fmDto.setNMenge(item.getN_menge()
								.subtract(bdAusgegeben));
						// PJ17994
						fmDto.setTLiefertermin(Helper.addiereTageZuDatum(
								new java.sql.Date(item.getFlrlos()
										.getT_produktionsende().getTime()),
								item.getI_beginnterminoffset()));

						context.getBusinessObject(FehlmengeFac.class)
								.createArtikelfehlmenge(fmDto);
						myLogger.warn(theClientDto.getIDUser(),
								"Fehlmenge nachgetragen: " + fmDto);
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
}
