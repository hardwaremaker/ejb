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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

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
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelbestellt;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelreservierung;
import com.lp.server.artikel.fastlanereader.generated.FLRFehlmenge;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelfehlmengeDto;
import com.lp.server.artikel.service.ArtikelfehlmengeDtoAssembler;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.AufgeloesteFehlmengenDto;
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

	public TreeMap<String, BigDecimal> fuelleFehlmengenDesAnderenMandantenNach(
			String mandantCNr_Zielmandant, java.sql.Timestamp tStichtag,
			TheClientDto theClientDto) {

		TreeMap<String, BigDecimal> snrChnrArtikelAusgelassen = new TreeMap<String, BigDecimal>();

		tStichtag = Helper.cutTimestamp(new java.sql.Timestamp(tStichtag
				.getTime() + 24 * 3600000));

		try {

			HashMap<Integer, BigDecimal> hmArtikelMitFehlmengenUndReservierungen = new HashMap<Integer, BigDecimal>();

			LagerDto lagerDto_Hautptlager_Quellmandant = getLagerFac()
					.getHauptlagerEinesMandanten(theClientDto.getMandant());
			LagerDto lagerDto_Hautptlager_Zielmandant = getLagerFac()
					.getHauptlagerEinesMandanten(mandantCNr_Zielmandant);

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			String sQuery = " SELECT sum(fm.n_menge),  artikel_i_id FROM FLRFehlmenge fm WHERE fm.flrlossollmaterial.flrlos.mandant_c_nr='"
					+ mandantCNr_Zielmandant
					+ "' AND fm.n_menge >0 AND fm.t_liefertermin<'"
					+ Helper.formatTimestampWithSlashes(tStichtag)
					+ "' GROUP BY fm.artikel_i_id";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> list = query.list();
			Iterator it = list.iterator();

			Integer lieferscheinIId_Ziellager = null;

			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();

				BigDecimal summeFehlmenge = (BigDecimal) o[0];
				Integer artikelIId = (Integer) o[1];

				hmArtikelMitFehlmengenUndReservierungen.put(artikelIId,
						summeFehlmenge);

			}
			session.close();
			session = factory.openSession();
			// Es muessen auch Reservierungen ohne Fehlmengen beruecksichtigt
			// werden

			sQuery = " SELECT distinct ar.flrartikel.i_id FROM FLRArtikelreservierung ar WHERE ar.n_menge >0 AND ar.t_liefertermin<'"
					+ Helper.formatTimestampWithSlashes(tStichtag) + "'";

			query = session.createQuery(sQuery);

			list = query.list();
			it = list.iterator();

			while (it.hasNext()) {
				Integer artikelIId = (Integer) it.next();

				// SP2968
				// Reservierungen hinzufuegen
				BigDecimal bdReserviert = getReservierungFac()
						.getAnzahlReservierungen(artikelIId, tStichtag,
								mandantCNr_Zielmandant);

				if (bdReserviert.doubleValue() > 0) {
					// Wenn noch nicht in Liste
					if (!hmArtikelMitFehlmengenUndReservierungen
							.containsKey(artikelIId)) {
						hmArtikelMitFehlmengenUndReservierungen.put(artikelIId,
								bdReserviert);
					} else {
						BigDecimal bdVorhanden = hmArtikelMitFehlmengenUndReservierungen
								.get(artikelIId);
						hmArtikelMitFehlmengenUndReservierungen.put(artikelIId,
								bdVorhanden.add(bdReserviert));
					}
				}
			}

			session.close();

			Iterator itHm = hmArtikelMitFehlmengenUndReservierungen.keySet()
					.iterator();
			while (itHm.hasNext()) {

				Integer artikelIId = (Integer) itHm.next();

				// SP2991
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						artikelIId, theClientDto);
				if (Helper.short2Boolean(aDto.getBLagerbewirtschaftet()) == true) {

					BigDecimal summeFehlmengeUndReservierungen = (BigDecimal) hmArtikelMitFehlmengenUndReservierungen
							.get(artikelIId);

					BigDecimal bdLagerstandQuellmandant = getLagerFac()
							.getLagerstand(artikelIId,
									lagerDto_Hautptlager_Quellmandant.getIId(),
									theClientDto);
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(artikelIId,
									theClientDto);

					BigDecimal bdLagerstandBeiZielMandant = getLagerFac()
							.getLagerstand(artikelIId,
									lagerDto_Hautptlager_Zielmandant.getIId(),
									theClientDto);

					BigDecimal bdBestellt = getSummeBestellungenZielmandant(
							mandantCNr_Zielmandant, tStichtag, artikelIId);

					// Bestelltliste abziehen
					summeFehlmengeUndReservierungen = summeFehlmengeUndReservierungen
							.subtract(bdBestellt);

					// Nur wenn bei Zielmandant nicht genug auf Lager ist
					if (bdLagerstandBeiZielMandant.doubleValue() < summeFehlmengeUndReservierungen
							.doubleValue()
							&& bdLagerstandQuellmandant.doubleValue() > 0
							&& Helper.short2boolean(artikelDto
									.getBLagerbewirtschaftet())) {

						if (Helper.short2boolean(artikelDto
								.getBSeriennrtragend()) == true
								|| Helper.short2boolean(artikelDto
										.getBChargennrtragend()) == true) {

							snrChnrArtikelAusgelassen.put(
									artikelDto.formatArtikelbezeichnung(),
									summeFehlmengeUndReservierungen);

						} else {

							BigDecimal bdAbzubuchendeMenge = summeFehlmengeUndReservierungen
									.subtract(bdLagerstandBeiZielMandant);

							if (bdLagerstandQuellmandant.doubleValue() > 0) {

								BigDecimal bdUmbuchen = BigDecimal.ZERO;

								if (bdLagerstandQuellmandant.doubleValue() >= bdAbzubuchendeMenge
										.doubleValue()) {

									bdUmbuchen = bdAbzubuchendeMenge;
									bdAbzubuchendeMenge = BigDecimal.ZERO;
								} else {
									bdUmbuchen = bdLagerstandQuellmandant;
									bdAbzubuchendeMenge = bdAbzubuchendeMenge
											.subtract(bdLagerstandQuellmandant);
								}

								if (bdUmbuchen.doubleValue() > 0) {

									// Nun ZiellagerLieferschein erstellen Kunde
									// =
									// Mandant

									MandantDto mDto = getMandantFac()
											.mandantFindByPrimaryKey(
													mandantCNr_Zielmandant,
													theClientDto);

									KundeDto kDto_Quellmandant = getKundeFac()
											.kundeFindByiIdPartnercNrMandantOhneExc(
													mDto.getPartnerIId(),
													theClientDto.getMandant(),
													theClientDto);

									if (kDto_Quellmandant != null) {

										if (lieferscheinIId_Ziellager == null) {

											LieferscheinDto lieferscheinDto = new LieferscheinDto();
											lieferscheinDto
													.setKundeIIdLieferadresse(kDto_Quellmandant
															.getIId());
											lieferscheinDto
													.setKundeIIdRechnungsadresse(kDto_Quellmandant
															.getIId());
											lieferscheinDto
													.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
											lieferscheinDto
													.setPersonalIIdVertreter(theClientDto
															.getIDPersonal());

											lieferscheinDto
													.setLagerIId(lagerDto_Hautptlager_Quellmandant
															.getIId());

											// Ziellager
											lieferscheinDto
													.setZiellagerIId(lagerDto_Hautptlager_Zielmandant
															.getIId());

											lieferscheinDto
													.setWaehrungCNr(theClientDto
															.getSMandantenwaehrung());
											lieferscheinDto
													.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
															1));

											lieferscheinDto
													.setTBelegdatum(Helper
															.cutTimestamp(new java.sql.Timestamp(
																	System.currentTimeMillis())));
											lieferscheinDto
													.setMandantCNr(theClientDto
															.getMandant());

											if (kDto_Quellmandant
													.getKostenstelleIId() != null) {
												lieferscheinDto
														.setKostenstelleIId(kDto_Quellmandant
																.getKostenstelleIId());
											} else {
												lieferscheinDto
														.setKostenstelleIId(mDto
																.getIIdKostenstelle());
											}

											lieferscheinDto
													.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

											lieferscheinDto
													.setLieferartIId(kDto_Quellmandant
															.getLieferartIId());
											lieferscheinDto
													.setSpediteurIId(kDto_Quellmandant
															.getSpediteurIId());
											lieferscheinDto
													.setZahlungszielIId(kDto_Quellmandant
															.getZahlungszielIId());

											lieferscheinIId_Ziellager = getLieferscheinFac()
													.createLieferschein(
															lieferscheinDto,
															theClientDto);

										}

										MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
												.mwstsatzFindByMwstsatzbezIIdAktuellster(
														kDto_Quellmandant
																.getMwstsatzbezIId(),
														theClientDto);
										// Position anlegen
										LieferscheinpositionDto lieferscheinposDto = new LieferscheinpositionDto();
										lieferscheinposDto
												.setLieferscheinIId(lieferscheinIId_Ziellager);
										lieferscheinposDto
												.setPositionsartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
										lieferscheinposDto
												.setNMenge(bdUmbuchen);

										VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
												.verkaufspreisfindung(
														artikelDto.getIId(),
														kDto_Quellmandant
																.getIId(),
														bdUmbuchen,
														new Date(
																System.currentTimeMillis()),
														kDto_Quellmandant
																.getVkpfArtikelpreislisteIIdStdpreisliste(),
														mwstsatzDtoAktuell
																.getIId(),
														theClientDto
																.getSMandantenwaehrung(),
														theClientDto);

										VerkaufspreisDto kundenVKPreisDto = Helper
												.getVkpreisBerechnet(vkpreisfindungDto);
										BigDecimal preis = BigDecimal.ZERO;
										BigDecimal materialzuschlag = BigDecimal.ZERO;

										if (kundenVKPreisDto != null
												&& kundenVKPreisDto.nettopreis != null) {
											preis = kundenVKPreisDto.nettopreis;
										}

										if (kundenVKPreisDto != null
												&& kundenVKPreisDto.bdMaterialzuschlag != null) {
											materialzuschlag = kundenVKPreisDto.bdMaterialzuschlag;
											lieferscheinposDto
													.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);
										}

										lieferscheinposDto
												.setNEinzelpreis(preis);

										lieferscheinposDto
												.setNNettoeinzelpreis(preis
														.add(materialzuschlag));


										lieferscheinposDto.setFRabattsatz(0.0);
										lieferscheinposDto
												.setFZusatzrabattsatz(0.0);
										lieferscheinposDto
												.setBNettopreisuebersteuert(Helper
														.boolean2Short(false));
										lieferscheinposDto
												.setArtikelIId(artikelDto
														.getIId());
										lieferscheinposDto
												.setEinheitCNr(artikelDto
														.getEinheitCNr());

										// Ausser der Kunde hat MWST-Satz mit
										// 0%,
										// dann muss
										// dieser
										// verwendet werden

										BigDecimal mwstBetrag = new BigDecimal(
												0);

										lieferscheinposDto
												.setMwstsatzIId(mwstsatzDtoAktuell
														.getIId());

										mwstBetrag = preis
												.add(materialzuschlag)
												.multiply(
														new BigDecimal(
																mwstsatzDtoAktuell
																		.getFMwstsatz())
																.movePointLeft(2));

										lieferscheinposDto
												.setNBruttoeinzelpreis(preis
														.add(materialzuschlag)
														.add(mwstBetrag));
										lieferscheinposDto
												.setNMwstbetrag(mwstBetrag);

										getLieferscheinpositionFac()
												.createLieferscheinposition(
														lieferscheinposDto,
														true, theClientDto);

									} else {
										// FEHLERMELDUNG
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE,
												"FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE");
									}

								}
							}

						}
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return snrChnrArtikelAusgelassen;

	}

	public BigDecimal getSummeBestellungenZielmandant(
			String mandantCNr_Zielmandant, java.sql.Timestamp tStichtag,
			Integer artikelIId) throws RemoteException {
		BigDecimal bdBestellt = BigDecimal.ZERO;

		Collection<FLRArtikelbestellt> c = (Collection<FLRArtikelbestellt>) getArtikelbestelltFac()
				.getArtikelbestellt(artikelIId, null,
						new Date(tStichtag.getTime()));

		Iterator itBest = c.iterator();
		while (itBest.hasNext()) {
			FLRArtikelbestellt flr = (FLRArtikelbestellt) itBest.next();

			if (flr.getC_belegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {

				BestellpositionDto bestposDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKeyOhneExc(
								flr.getI_belegartpositionid());
				if (bestposDto != null) {

					BestellungDto besDto = getBestellungFac()
							.bestellungFindByPrimaryKey(
									bestposDto.getBelegIId());

					if (besDto.getMandantCNr().equals(mandantCNr_Zielmandant)) {
						bdBestellt = bdBestellt.add(flr.getN_menge());
					}

				}

			}

		}
		return bdBestellt;
	}

	public TreeMap<?, ?> alleFehlmengenDesMandantenAufloesen(
			TheClientDto theClientDto) {

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRFehlmenge.class);

		TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>> tmAufgeloesteFehlmengen = new TreeMap<String, ArrayList<AufgeloesteFehlmengenDto>>();
		ArrayList<AufgeloesteFehlmengenDto> alDaten = new ArrayList<AufgeloesteFehlmengenDto>();

		try {

			// Filter nach Mandant
			c.createCriteria(ArtikelFac.FLR_FEHLMENGE_FLRLOSSOLLMATERIAL)
					.createCriteria(FertigungFac.FLR_LOSSOLLMATERIAL_FLRLOS)
					.add(Restrictions.eq(FertigungFac.FLR_LOS_MANDANT_C_NR,
							theClientDto.getMandant()))
					.addOrder(
							Order.asc(FertigungFac.FLR_LOS_T_PRODUKTIONSBEGINN));
			c.add(Restrictions.gt(ArtikelFac.FLR_FEHLMENGE_N_MENGE,
					BigDecimal.ZERO));

			List<?> list = c.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FLRFehlmenge fm = (FLRFehlmenge) it.next();

				if (Helper.short2boolean(fm.getFlrartikel()
						.getB_seriennrtragend()) == false) {
					if (Helper.short2boolean(fm.getFlrartikel()
							.getB_chargennrtragend()) == false) {
						BigDecimal bdAbzubuchendeMenge = fm.getN_menge();

						LossollmaterialDto sollDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKey(
										fm.getFlrlossollmaterial().getI_id());
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										fm.getArtikel_i_id(), theClientDto);
						LosDto losDto = getFertigungFac().losFindByPrimaryKey(
								fm.getFlrlossollmaterial().getFlrlos()
										.getI_id());

						LoslagerentnahmeDto[] loslagerentnahmeDtos = getFertigungFac()
								.loslagerentnahmeFindByLosIId(losDto.getIId());

						for (int i = 0; i < loslagerentnahmeDtos.length; i++) {
							BigDecimal bdLagerstand = getLagerFac()
									.getLagerstand(
											fm.getArtikel_i_id(),
											loslagerentnahmeDtos[i]
													.getLagerIId(),
											theClientDto);

							if (bdLagerstand.doubleValue() > 0) {

								LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
								losistmaterialDto.setLossollmaterialIId(sollDto
										.getIId());
								losistmaterialDto
										.setLagerIId(loslagerentnahmeDtos[i]
												.getLagerIId());

								if (bdLagerstand.doubleValue() >= bdAbzubuchendeMenge
										.doubleValue()) {
									losistmaterialDto
											.setNMenge(bdAbzubuchendeMenge);
									bdAbzubuchendeMenge = BigDecimal.ZERO;
								} else {
									losistmaterialDto.setNMenge(bdLagerstand);
									bdAbzubuchendeMenge = bdAbzubuchendeMenge
											.subtract(bdLagerstand);
								}
								losistmaterialDto.setBAbgang(Helper
										.boolean2Short(true));

								getFertigungFac().gebeMaterialNachtraeglichAus(
										sollDto, losistmaterialDto, null, true,
										theClientDto);

								LagerDto lagerDto = getLagerFac()
										.lagerFindByPrimaryKey(
												loslagerentnahmeDtos[i]
														.getLagerIId());

								AufgeloesteFehlmengenDto aufgeloesteFehlmengenDto = new AufgeloesteFehlmengenDto();
								aufgeloesteFehlmengenDto
										.setArtikelDto(artikelDto);
								aufgeloesteFehlmengenDto
										.setArtikelCNr(artikelDto.getCNr());
								aufgeloesteFehlmengenDto.setLagerDto(lagerDto);
								aufgeloesteFehlmengenDto.setLagerCNr(lagerDto
										.getCNr());
								aufgeloesteFehlmengenDto.setLosDto(losDto);
								aufgeloesteFehlmengenDto.setLosCNr(losDto
										.getCNr());
								aufgeloesteFehlmengenDto
										.setAufgeloesteMenge(losistmaterialDto
												.getNMenge());
								aufgeloesteFehlmengenDto.setSSeriennrChnr(null);
								aufgeloesteFehlmengenDto.setLosDto(losDto);

								if (tmAufgeloesteFehlmengen.containsKey(losDto
										.getCNr())) {
									ArrayList<AufgeloesteFehlmengenDto> al = (ArrayList<AufgeloesteFehlmengenDto>) tmAufgeloesteFehlmengen
											.get(losDto.getCNr());
									al.add(aufgeloesteFehlmengenDto);
									tmAufgeloesteFehlmengen.put(
											losDto.getCNr(), al);
								} else {
									ArrayList<AufgeloesteFehlmengenDto> al = new ArrayList<AufgeloesteFehlmengenDto>();
									al.add(aufgeloesteFehlmengenDto);
									tmAufgeloesteFehlmengen.put(
											losDto.getCNr(), al);
								}

								if (bdAbzubuchendeMenge.doubleValue() <= 0) {
									break;
								}
							}
						}

					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return tmAufgeloesteFehlmengen;
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
	 * @param theClientDto
	 *            der aktuelle Benutzer
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
