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

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.*;

import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.artikel.ejb.*;
import com.lp.server.artikel.service.*;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Kundematerial;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundematerialDto;
import com.lp.server.partner.service.KundematerialDtoAssembler;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.pkgenerator.*;
import com.lp.server.system.pkgenerator.bl.*;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.*;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.*;

import java.math.BigDecimal;

/**
 * <p>SessionFacade fuer Material.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2007</p>
 * <p>Erstellungsdatum 2004-09-27</p>
 * <p> </p>
 * @author Christian Kollmann
 * @version 1.0
 */

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

@Stateless
public class MaterialFacBean extends Facade implements MaterialFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Liefert die IId des Materials welches &uuml;ber seine Kennung gesucht wird
	 * 
	 * @param materialCNr die Kennung des Materials
	 * @return Id des Materials
	 * @throws EJBExceptionLP
	 */
	public Integer materialFindByCNrOhneExc(String materialCNr) {
		try {
			Query query = em.createNamedQuery("MaterialfindByCNr");
			query.setParameter(1, materialCNr);
			Material doppelt = (Material) query.getSingleResult();
			return doppelt.getIId();
		} catch (NoResultException ex) {
			return null;
		}

	}

	public void pflegeMaterialzuschlagsKursUndDatumNachtragen(TheClientDto theClientDto) {
		// Angebot
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT a FROM FLRAngebotposition AS a WHERE a.flrartikel.flrmaterial.i_id IS NOT NULL";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAngebotposition a = (FLRAngebotposition) resultListIterator.next();

			// Nun Kurs zum Belegdatum holen

			MaterialzuschlagDto mDto = getKursMaterialzuschlagDtoVKInZielwaehrung(
					a.getFlrangebot().getKunde_i_id_angebotsadresse(), a.getFlrartikel().getI_id(),
					a.getFlrangebot().getT_belegdatum(), a.getFlrangebot().getWaehrung_c_nr_angebotswaehrung(),
					theClientDto);

			if (mDto != null) {
				Angebotposition ap = em.find(Angebotposition.class, a.getI_id());
				ap.setNMaterialzuschlagKurs(mDto.getNZuschlag());
				ap.setTMaterialzuschlagDatum(mDto.getTGueltigab());
				em.merge(ap);
				em.flush();
			}
		}

		session.close();

		// Auftrag

		session = FLRSessionFactory.getFactory().openSession();
		queryString = "SELECT a FROM FLRAuftragposition AS a WHERE a.flrartikel.flrmaterial.i_id IS NOT NULL";
		query = session.createQuery(queryString);
		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRAuftragposition a = (FLRAuftragposition) resultListIterator.next();

			// Nun Kurs zum Belegdatum holen

			MaterialzuschlagDto mDto = getKursMaterialzuschlagDtoVKInZielwaehrung(
					a.getFlrauftrag().getKunde_i_id_rechnungsadresse(), a.getFlrartikel().getI_id(),
					a.getFlrauftrag().getT_belegdatum(), a.getFlrauftrag().getWaehrung_c_nr_auftragswaehrung(),
					theClientDto);

			if (mDto != null) {
				Auftragposition ap = em.find(Auftragposition.class, a.getI_id());
				ap.setNMaterialzuschlagKurs(mDto.getNZuschlag());
				ap.setTMaterialzuschlagDatum(mDto.getTGueltigab());
				em.merge(ap);
				em.flush();
			}
		}

		// Lieferschein
		session = FLRSessionFactory.getFactory().openSession();
		queryString = "SELECT l FROM FLRLieferscheinposition AS l WHERE l.flrartikel.flrmaterial.i_id IS NOT NULL";
		query = session.createQuery(queryString);
		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRLieferscheinposition l = (FLRLieferscheinposition) resultListIterator.next();

			if (l.getAuftragposition_i_id() != null) {
				Auftragposition ap = em.find(Auftragposition.class, l.getAuftragposition_i_id());

				Lieferscheinposition lp = em.find(Lieferscheinposition.class, l.getI_id());
				lp.setNMaterialzuschlagKurs(ap.getNMaterialzuschlagKurs());
				lp.setTMaterialzuschlagDatum(ap.getTMaterialzuschlagDatum());
				em.merge(lp);
				em.flush();

			} else {
				// Nun Kurs zum Belegdatum holen

				MaterialzuschlagDto mDto = getKursMaterialzuschlagDtoVKInZielwaehrung(
						l.getFlrlieferschein().getKunde_i_id_rechnungsadresse(), l.getFlrartikel().getI_id(),
						l.getFlrlieferschein().getD_belegdatum(),
						l.getFlrlieferschein().getWaehrung_c_nr_lieferscheinwaehrung(), theClientDto);

				if (mDto != null) {
					Lieferscheinposition lp = em.find(Lieferscheinposition.class, l.getI_id());
					lp.setNMaterialzuschlagKurs(mDto.getNZuschlag());
					lp.setTMaterialzuschlagDatum(mDto.getTGueltigab());
					em.merge(lp);
					em.flush();
				}
			}

		}
		// Rechnung
		session = FLRSessionFactory.getFactory().openSession();
		queryString = "SELECT r FROM FLRRechnungPosition AS r WHERE r.flrartikel.flrmaterial.i_id IS NOT NULL";
		query = session.createQuery(queryString);
		resultList = query.list();

		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRRechnungPosition r = (FLRRechnungPosition) resultListIterator.next();

			if (r.getAuftragposition_i_id() != null) {
				Auftragposition ap = em.find(Auftragposition.class, r.getAuftragposition_i_id());

				Rechnungposition rp = em.find(Rechnungposition.class, r.getI_id());
				rp.setNMaterialzuschlagKurs(ap.getNMaterialzuschlagKurs());
				rp.setTMaterialzuschlagDatum(ap.getTMaterialzuschlagDatum());
				em.merge(rp);
				em.flush();

			} else {
				// Nun Kurs zum Belegdatum holen

				MaterialzuschlagDto mDto = getKursMaterialzuschlagDtoVKInZielwaehrung(
						r.getFlrrechnung().getFlrkunde().getI_id(), r.getFlrartikel().getFlrmaterial().getI_id(),
						r.getFlrrechnung().getD_belegdatum(), r.getFlrrechnung().getWaehrung_c_nr(), theClientDto);

				if (mDto != null) {
					Rechnungposition rp = em.find(Rechnungposition.class, r.getI_id());
					rp.setNMaterialzuschlagKurs(mDto.getNZuschlag());
					rp.setTMaterialzuschlagDatum(mDto.getTGueltigab());
					em.merge(rp);
					em.flush();
				}
			}

		}
	}

	public Integer createMaterial(MaterialDto materialDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (materialDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("materialDto == null"));
		}
		if (materialDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("materialDto.getCNr() == null"));
		}
		try {
			Query query = em.createNamedQuery("MaterialfindByCNr");
			query.setParameter(1, materialDto.getCNr());
			Material doppelt = (Material) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MATERIAL.CNR"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MATERIAL);
			materialDto.setIId(pk);

			Material material = new Material(materialDto.getIId(), materialDto.getCNr());
			em.persist(material);
			em.flush();
			setMaterialFromMaterialDto(material, materialDto);
			if (materialDto.getMaterialsprDto() != null) {
				Materialspr materialspr = new Materialspr(theClientDto.getLocUiAsString(), materialDto.getIId());
				em.persist(materialspr);
				em.flush();
				setMaterialsprFromMaterialsprDto(materialspr, materialDto.getMaterialsprDto());
			}
			return materialDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createKundematerial(KundematerialDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("KundematerialfindByKundeIIdMaterialIId");
			query.setParameter(1, dto.getKundeIId());
			query.setParameter(2, dto.getMaterialIId());
			Kundematerial doppelt = (Kundematerial) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_KUNDEMATERIAL.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KUNDEMATERIAL);
			dto.setIId(pk);

			Kundematerial material = new Kundematerial(dto.getIId(), dto.getKundeIId(), dto.getMaterialIId(),
					dto.getMaterialIIdNotierung(), dto.getNMaterialbasis(), dto.getBMaterialInklusive());
			em.persist(material);
			em.flush();
			setKundematerialFromKundematerialDto(material, dto);

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	/**
	 * Loescht ein vorhandenes Material
	 * 
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 */
	public void removeMaterial(Integer iId) throws EJBExceptionLP {
		final String METHOD_NAME = "removeMaterial";
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("MaterialsprfindByMaterialIId");
			query.setParameter(1, iId);
			Collection<?> allMaterialspr = query.getResultList();
			Iterator<?> iter = allMaterialspr.iterator();
			while (iter.hasNext()) {
				Materialspr materialsprTemp = (Materialspr) iter.next();
				em.remove(materialsprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
		Material material = em.find(Material.class, iId);
		if (material == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(material);
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

	public void updateMaterial(MaterialDto materialDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (materialDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("materialDto == null"));
		}
		Integer iId = materialDto.getIId();
		Material material = null;
		// try {
		material = em.find(Material.class, iId);
		if (material == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			Query query = em.createNamedQuery("MaterialfindByCNr");
			query.setParameter(1, materialDto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Material) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MATERIAL.C_NR"));
			}
		} catch (NoResultException ex) {
			//
		}

		setMaterialFromMaterialDto(material, materialDto);

		if (materialDto.getMaterialsprDto() != null) {

			// try {
			Materialspr materialspr = em.find(Materialspr.class,
					new MaterialsprPK(theClientDto.getLocUiAsString(), iId));
			if (materialspr == null) {
				try {
					materialspr = new Materialspr(theClientDto.getLocUiAsString(), iId);
					em.persist(materialspr);
					em.flush();
					setMaterialsprFromMaterialsprDto(materialspr, materialDto.getMaterialsprDto());
				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex7);

				}
			}
			setMaterialsprFromMaterialsprDto(materialspr, materialDto.getMaterialsprDto());
			// }
			// catch (FinderException ex) {
			// try {
			// Materialspr materialspr = new
			// Materialspr(getTheClient(idUser).getLocUiAsString(), iId);
			// em.persist(materialspr);
			// setMaterialsprFromMaterialsprDto(materialspr,
			// materialDto.getMaterialsprDto());
			// }
			// catch (CreateException ex7) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_BEIM_DRUCKEN,
			// ex7);

			// }
			// }
		}

	}

	/**
	 * @deprecated Use getMaterialzuschlagEKInZielwaehrung(...)
	 */

	public BigDecimal getKupferzuschlagInLieferantenwaehrung(Integer artikelIId, Integer lieferantIId,
			TheClientDto theClientDto) {

		BigDecimal zuschlagGesamt = null;

		Artikel artikel = em.find(Artikel.class, artikelIId);
		if (artikel.getMaterialIId() != null && artikel.getFMaterialgewicht() != null) {
			LieferantDto lieferantDto = null;
			if (lieferantIId == null) {
				// 1. Artikellieferant suchen
				ArtikellieferantDto[] dtos = getArtikelFac().artikellieferantFindByArtikelIId(artikelIId, theClientDto);
				if (dtos.length > 0) {

					lieferantDto = getLieferantFac().lieferantFindByPrimaryKeySmall(dtos[0].getLieferantIId());
				} else {
					return null;
				}
			} else {
				lieferantDto = getLieferantFac().lieferantFindByPrimaryKeySmall(lieferantIId);
			}

			return getMaterialzuschlagEKInZielwaehrung(artikelIId, lieferantDto.getIId(),
					new Date(System.currentTimeMillis()), lieferantDto.getWaehrungCNr(), theClientDto);

		}

		return zuschlagGesamt;
	}

	public BigDecimal getMaterialzuschlagEKInZielwaehrung(Integer artikelIId, Integer lieferantIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung, TheClientDto theClientDto)

	{

		BigDecimal zuschlagGesamt = null;

		try {
			Artikel artikel = em.find(Artikel.class, artikelIId);
			if (artikel.getMaterialIId() != null && artikel.getFMaterialgewicht() != null) {
				LieferantDto lieferantDto = null;
				if (lieferantIId == null) {
					// 1. Artikellieferant suchen
					ArtikellieferantDto[] dtos = getArtikelFac().artikellieferantFindByArtikelIId(artikelIId,
							theClientDto);
					if (dtos.length > 0) {

						lieferantDto = getLieferantFac().lieferantFindByPrimaryKeySmall(dtos[0].getLieferantIId());
					} else {
						return null;
					}
				} else {
					lieferantDto = getLieferantFac().lieferantFindByPrimaryKeySmall(lieferantIId);
				}

				// PJ19814
				if (Helper.short2boolean(lieferantDto.getBZuschlagInklusive()) == true) {
					return BigDecimal.ZERO;
				}

				if (lieferantDto.getNKupferzahl() != null) {
					BigDecimal zuschlag = materialzuschlagFindZuschlagZuDatum(artikel.getMaterialIId(),
							datGueltigkeitsdatumI, theClientDto);

					if (zuschlag != null) {
						if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
							zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(zuschlag,
									theClientDto.getSMandantenwaehrung(), waehrungCNrZielwaehrung,
									new Date(System.currentTimeMillis()), theClientDto);
						}
						zuschlagGesamt = Helper.rundeKaufmaennisch((zuschlag.subtract(lieferantDto.getNKupferzahl()))
								.multiply(new BigDecimal(artikel.getFMaterialgewicht().doubleValue()))
								.divide(new BigDecimal(1000), 6, BigDecimal.ROUND_HALF_EVEN), 6);

					}
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}

		return zuschlagGesamt;
	}

	public MaterialzuschlagDto getKursMaterialzuschlagDtoVKInZielwaehrung(Integer kundeIId, Integer artikelIId,
			java.util.Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung, TheClientDto theClientDto) {

		Integer materialIIdAusKundematerial = null;
		BigDecimal bdMaterialbasisAusKundematerial = null;

		Artikel artikel = em.find(Artikel.class, artikelIId);

		if (artikel.getMaterialIId() != null) {

			Integer materialIId = artikel.getMaterialIId();

			if (kundeIId != null) {
				// PJ19826
				try {
					Query query = em.createNamedQuery("KundematerialfindByKundeIIdMaterialIId");
					query.setParameter(1, kundeIId);
					query.setParameter(2, artikel.getMaterialIId());
					Kundematerial vorhanden = (Kundematerial) query.getSingleResult();

					if (Helper.short2boolean(vorhanden.getBMaterialInklusive()) == true) {
						return new MaterialzuschlagDto();
					}

					materialIId = vorhanden.getMaterialIIdNotierung();

					materialIIdAusKundematerial = vorhanden.getMaterialIIdNotierung();
					bdMaterialbasisAusKundematerial = vorhanden.getNMaterialbasis();

					// SP4762
					// Wenn Parameter VK_PREIS_BASIS_MATERIAL=1, dann wird der
					// Kurs aus dem Material des Artikels als Basis verwendet

					try {
						ParametermandantDto parameterX = getParameterFac().getMandantparameter(
								theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_VK_PREIS_BASIS_MATERIAL);
						boolean b = (Boolean) parameterX.getCWertAsObject();

						if (b == true) {

							Query query2 = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
							query2.setParameter(1, artikel.getMaterialIId());
							if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {
								query2.setParameter(2, getSystemFac().getHauptmandant());
							} else {
								query2.setParameter(2, theClientDto.getMandant());
							}
							query2.setParameter(3, new java.sql.Date(datGueltigkeitsdatumI.getTime()));
							query2.setMaxResults(1);
							Collection<?> cl = query2.getResultList();

							MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
							if (dtos.length > 0) {

								if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
									try {
										BigDecimal zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
												dtos[0].getNZuschlag(), theClientDto.getSMandantenwaehrung(),
												waehrungCNrZielwaehrung, new Date(System.currentTimeMillis()),
												theClientDto);

										bdMaterialbasisAusKundematerial = zuschlag;

									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);

									}
								} else {
									bdMaterialbasisAusKundematerial = dtos[0].getNZuschlag();
								}
							}

						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				} catch (NoResultException ex) {
					// PJ19814

					Kunde kunde = em.find(Kunde.class, kundeIId);

					if (Helper.short2boolean(kunde.getBZuschlagInklusive()) == true) {
						return new MaterialzuschlagDto();
					}

				}
			}

			Query query = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
			query.setParameter(1, materialIId);
			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)) {
				query.setParameter(2, getSystemFac().getHauptmandant());
			} else {
				query.setParameter(2, theClientDto.getMandant());
			}

			query.setParameter(3, new java.sql.Date(datGueltigkeitsdatumI.getTime()));
			query.setMaxResults(1);
			Collection<?> cl = query.getResultList();

			MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
			if (dtos.length > 0) {

				if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
					try {
						BigDecimal zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(dtos[0].getNZuschlag(),
								theClientDto.getSMandantenwaehrung(), waehrungCNrZielwaehrung,
								new Date(System.currentTimeMillis()), theClientDto);

						dtos[0].setNZuschlag(zuschlag);

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);

					}
				}

				dtos[0].materialIIdWennMaterialAusKundematerial = materialIIdAusKundematerial;
				dtos[0].nMaterialbasisWennMaterialAusKundematerial = bdMaterialbasisAusKundematerial;

				return dtos[0];
			} else {
				// SP4805 Basis zurueckgeben, auch wenn kein Kurs definiert
				MaterialzuschlagDto mzDto = new MaterialzuschlagDto();
				mzDto.materialIIdWennMaterialAusKundematerial = materialIIdAusKundematerial;
				mzDto.nMaterialbasisWennMaterialAusKundematerial = bdMaterialbasisAusKundematerial;
				return mzDto;
			}

		} else {
			return new MaterialzuschlagDto();
		}

	}

	public MaterialzuschlagDto getKursMaterialzuschlagDtoInZielwaehrung(Integer materialIId,
			java.util.Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
		query.setParameter(1, materialIId);
		query.setParameter(2, theClientDto.getMandant());
		query.setParameter(3, new java.sql.Date(datGueltigkeitsdatumI.getTime()));
		query.setMaxResults(1);
		Collection<?> cl = query.getResultList();

		MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
		if (dtos.length > 0) {

			if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
				try {
					BigDecimal zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(dtos[0].getNZuschlag(),
							theClientDto.getSMandantenwaehrung(), waehrungCNrZielwaehrung,
							new Date(System.currentTimeMillis()), theClientDto);

					dtos[0].setNZuschlag(zuschlag);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);

				}
			}
			return dtos[0];
		} else {
			return new MaterialzuschlagDto();
		}

	}

	public BigDecimal getMaterialpreisInZielwaehrung(Integer materialIId, java.util.Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("MaterialpreisfindAktuellenPreis");
		query.setParameter(1, materialIId);
		query.setParameter(2, new java.sql.Date(datGueltigkeitsdatumI.getTime()));
		query.setMaxResults(1);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			Materialpreis mp = (Materialpreis) cl.iterator().next();

			if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
				try {
					BigDecimal preis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(mp.getNPreisProKG(),
							theClientDto.getSMandantenwaehrung(), waehrungCNrZielwaehrung,
							new Date(System.currentTimeMillis()), theClientDto);

					return preis;
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);

				}
			}
			return mp.getNPreisProKG();
		} else {
			return null;
		}

	}

	public BigDecimal getMaterialzuschlagVKInZielwaehrung(Integer artikelIId, Integer kundeIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung, TheClientDto theClientDto) {

		BigDecimal zuschlagGesamt = new BigDecimal(0);

		try {
			Artikel artikel = em.find(Artikel.class, artikelIId);
			if (artikel.getMaterialIId() != null && artikel.getFMaterialgewicht() != null) {

				Integer materialIId = artikel.getMaterialIId();

				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN, ParameterFac.PARAMETER_KUPFERZAHL);

				Double kupferzahlTemp = (Double) parameter.getCWertAsObject();

				BigDecimal kupferzahl = new BigDecimal(kupferzahlTemp.doubleValue());

				// PJ19209 Wenn Kunde Kupferzahl hinterlegt hat, dann diese
				// verwenden

				KundematerialDto kundematerialDto = null;

				if (kundeIId != null) {

					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

					// PJ19826
					try {
						Query query = em.createNamedQuery("KundematerialfindByKundeIIdMaterialIId");
						query.setParameter(1, kundeIId);
						query.setParameter(2, artikel.getMaterialIId());
						Kundematerial vorhanden = (Kundematerial) query.getSingleResult();

						if (Helper.short2boolean(vorhanden.getBMaterialInklusive()) == true) {
							return BigDecimal.ZERO;
						}

						kundematerialDto = assembleKundematerialDto(vorhanden);

						materialIId = vorhanden.getMaterialIIdNotierung();
						kupferzahl = vorhanden.getNMaterialbasis();

						// SP4762
						// Wenn Parameter VK_PREIS_BASIS_MATERIAL=1, dann wird
						// der Kurs aus dem Material des Artikels als Basis
						// verwendet

						try {
							ParametermandantDto parameterX = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_VK_PREIS_BASIS_MATERIAL);
							boolean b = (Boolean) parameterX.getCWertAsObject();

							if (b == true) {

								Query query2 = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
								query2.setParameter(1, artikel.getMaterialIId());
								query2.setParameter(2, theClientDto.getMandant());
								query2.setParameter(3, new java.sql.Date(datGueltigkeitsdatumI.getTime()));
								query2.setMaxResults(1);
								Collection<?> cl = query2.getResultList();

								MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
								if (dtos.length > 0) {

									if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
										try {
											BigDecimal zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
													dtos[0].getNZuschlag(), theClientDto.getSMandantenwaehrung(),
													waehrungCNrZielwaehrung, new Date(System.currentTimeMillis()),
													theClientDto);

											kupferzahl = zuschlag;

										} catch (RemoteException e) {
											throwEJBExceptionLPRespectOld(e);

										}
									} else {
										kupferzahl = dtos[0].getNZuschlag();
									}
								}

							}

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

					} catch (NoResultException ex) {

						// SP4782
						if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_METALLNOTIERUNG_DETAILLIERT, theClientDto)) {
							// Wenn METALLNOTIERUNG_DETAILLIERT dann muss es
							// hier einen Eintrag geben

							ArrayList alDaten = new ArrayList();
							alDaten.add(kdDto.getPartnerDto().formatFixName1Name2());
							alDaten.add(
									materialFindByPrimaryKey(artikel.getMaterialIId(), theClientDto).getBezeichnung());
							alDaten.add(artikel.getCNr());

							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KUNDEMATERIAL_NICHT_DEFINIERT, alDaten,
									new Exception("FEHLER_KUNDEMATERIAL_NICHT_DEFINIERT"));

						}

						// PJ19814
						if (Helper.short2boolean(kdDto.getBZuschlagInklusive()) == true) {
							return BigDecimal.ZERO;
						}

						BigDecimal nKupferzahlKunde = kdDto.getNKupferzahl();

						if (nKupferzahlKunde != null) {
							kupferzahl = nKupferzahlKunde;
						}
					}

					// In Mandantenwaehrung umrechnen
					if (kupferzahl != null) {
						kupferzahl = getLocaleFac().rechneUmInAndereWaehrungZuDatum(kupferzahl, kdDto.getCWaehrung(),
								theClientDto.getSMandantenwaehrung(), new Date(System.currentTimeMillis()),
								theClientDto);
					}

				}

				BigDecimal zuschlag = materialzuschlagFindZuschlagZuDatum(materialIId, datGueltigkeitsdatumI,
						theClientDto);

				if (zuschlag != null) {
					if (!waehrungCNrZielwaehrung.equals(theClientDto.getSMandantenwaehrung())) {
						zuschlag = getLocaleFac().rechneUmInAndereWaehrungZuDatum(zuschlag,
								theClientDto.getSMandantenwaehrung(), waehrungCNrZielwaehrung,
								new Date(System.currentTimeMillis()), theClientDto);
					}

					zuschlagGesamt = zuschlag.subtract(kupferzahl);

					if (kundematerialDto != null) {
						if (kundematerialDto.getNAufschlagBetrag() != null) {
							zuschlagGesamt = zuschlagGesamt.add(kundematerialDto.getNAufschlagBetrag());
						}
					} else {
						// PJ18160
						if (artikel.getNAufschlagBetrag() != null) {
							zuschlagGesamt = zuschlagGesamt.add(artikel.getNAufschlagBetrag());
						}
					}

					if (kundematerialDto != null) {
						if (kundematerialDto.getFAufschlagProzent() != null
								&& kundematerialDto.getFAufschlagProzent().doubleValue() != 0) {

							BigDecimal prozentAufschlag = Helper.getProzentWert(zuschlagGesamt,
									new BigDecimal(kundematerialDto.getFAufschlagProzent()), 6);

							zuschlagGesamt = zuschlagGesamt.add(prozentAufschlag);
						}
					} else {
						if (artikel.getFAufschlagProzent() != null
								&& artikel.getFAufschlagProzent().doubleValue() != 0) {

							BigDecimal prozentAufschlag = Helper.getProzentWert(zuschlagGesamt,
									new BigDecimal(artikel.getFAufschlagProzent()), 6);

							zuschlagGesamt = zuschlagGesamt.add(prozentAufschlag);
						}
					}

					zuschlagGesamt = Helper.rundeKaufmaennisch(
							(zuschlagGesamt.multiply(new BigDecimal(artikel.getFMaterialgewicht().doubleValue()))
									.divide(new BigDecimal(1000), 6, BigDecimal.ROUND_HALF_EVEN)),
							6);

					// mit WH besprochen: Zuschlag wird immer in Anzahl der
					// VK-Nachkommastellen zurueckgegeben
					Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());
					zuschlagGesamt = Helper.rundeKaufmaennisch(zuschlagGesamt, iNachkommastellenPreis);

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}

		return zuschlagGesamt;
	}

	public MaterialDto materialFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Material material = em.find(Material.class, iId);
		if (material == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		MaterialDto materialDto = assembleMaterialDto(material);

		MaterialsprDto materialsprDto = null;
		// try {
		Materialspr materialspr = em.find(Materialspr.class, new MaterialsprPK(theClientDto.getLocUiAsString(), iId));
		materialsprDto = assembleMaterialsprDto(materialspr);
		if (materialsprDto == null) {
		}
		// }
		// catch (FinderException ex) {
		// }
		if (materialsprDto == null) {
			// try {
			Materialspr temp = em.find(Materialspr.class, new MaterialsprPK(theClientDto.getLocKonzernAsString(), iId));
			materialsprDto = assembleMaterialsprDto(temp);
			if (materialsprDto == null) {
			}
			// }
			// catch (FinderException ex) {
			// }
		}
		materialDto.setMaterialsprDto(materialsprDto);
		return materialDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public MaterialDto materialFindByPrimaryKey(Integer iId, Locale locDruck, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Material material = em.find(Material.class, iId);
		if (material == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		MaterialDto materialDto = assembleMaterialDto(material);

		MaterialsprDto materialsprDto = null;
		// try {
		Materialspr materialspr = em.find(Materialspr.class, new MaterialsprPK(Helper.locale2String(locDruck), iId));
		materialsprDto = assembleMaterialsprDto(materialspr);
		if (materialsprDto == null) {
		}
		// }
		// catch (FinderException ex) {
		// }
		if (materialsprDto == null) {
			// try {
			Materialspr temp = em.find(Materialspr.class, new MaterialsprPK(theClientDto.getLocUiAsString(), iId));
			materialsprDto = assembleMaterialsprDto(temp);
			if (materialsprDto == null) {
			}
			// }
			// catch (FinderException ex) {
			// }
		}
		materialDto.setMaterialsprDto(materialsprDto);
		return materialDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Gibt den zum Zeitpunkt JETZT gueltigen Materialzuschlag zurueck
	 * 
	 * @param iId          Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal materialzuschlagFindAktuellenzuschlag(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Query query = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
		query.setParameter(1, iId);
		// SP2910
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			query.setParameter(2, getSystemFac().getHauptmandant());
		} else {
			query.setParameter(2, theClientDto.getMandant());
		}
		query.setParameter(3, new java.sql.Date(System.currentTimeMillis()));
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
		if (dtos.length > 0) {
			return dtos[0].getNZuschlag();
		} else {
			return null;
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public BigDecimal materialzuschlagFindZuschlagZuDatum(Integer iId, java.sql.Date dDatum, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Query query = em.createNamedQuery("MaterialzuschlagfindAktuellenZuschlag");
		query.setParameter(1, iId);

		// SP2910
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			query.setParameter(2, getSystemFac().getHauptmandant());
		} else {
			query.setParameter(2, theClientDto.getMandant());
		}

		query.setParameter(3, dDatum);
		query.setMaxResults(1);
		Collection<?> cl = query.getResultList();

		MaterialzuschlagDto[] dtos = assembleMaterialzuschlagDtos(cl);
		if (dtos.length > 0) {
			return dtos[0].getNZuschlag();
		} else {
			return null;
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setMaterialFromMaterialDto(Material material, MaterialDto materialDto) {
		material.setCNr(materialDto.getCNr());
		material.setNGewichtInKG(materialDto.getNGewichtInKG());
		em.merge(material);
		em.flush();
	}

	private void setKundematerialFromKundematerialDto(Kundematerial bean, KundematerialDto dto) {
		bean.setKundeIId(dto.getKundeIId());
		bean.setMaterialIId(dto.getMaterialIId());
		bean.setMaterialIIdNotierung(dto.getMaterialIIdNotierung());
		bean.setBMaterialInklusive(dto.getBMaterialInklusive());
		bean.setFAufschlagProzent(dto.getFAufschlagProzent());
		bean.setNAufschlagBetrag(dto.getNAufschlagBetrag());
		bean.setNMaterialbasis(dto.getNMaterialbasis());
		em.merge(bean);
		em.flush();
	}

	private MaterialDto assembleMaterialDto(Material material) {
		return MaterialDtoAssembler.createDto(material);
	}

	private MaterialDto[] assembleMaterialDtos(Collection<?> materials) {
		List<MaterialDto> list = new ArrayList<MaterialDto>();
		if (materials != null) {
			Iterator<?> iterator = materials.iterator();
			while (iterator.hasNext()) {
				Material material = (Material) iterator.next();
				list.add(assembleMaterialDto(material));
			}
		}
		MaterialDto[] returnArray = new MaterialDto[list.size()];
		return (MaterialDto[]) list.toArray(returnArray);
	}

	public Integer createMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (materialzuschlagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("materialzuschlagDto == null"));
		}
		materialzuschlagDto.setMandantCNr(theClientDto.getMandant());

		if (materialzuschlagDto.getMandantCNr() == null || materialzuschlagDto.getMaterialIId() == null
				|| materialzuschlagDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"materialzuschlagDto.getMandantCNr() == null || materialzuschlagDto.getMaterialCNr() == null || materialzuschlagDto.getDGueltigab() == null"));
		}
		try {
			Query query = em.createNamedQuery("MaterialzuschlagfindByMaterialIIdMandantCNrTGueltigab");
			query.setParameter(1, materialzuschlagDto.getMaterialIId());
			query.setParameter(2, materialzuschlagDto.getMandantCNr());
			query.setParameter(3, materialzuschlagDto.getTGueltigab());
			// @todo getSingleResult oder getResultList ?
			Materialzuschlag doppelt = (Materialzuschlag) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MATERIALZUSCHLAG.UK"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MATERIALZUSCHLAG);
			materialzuschlagDto.setIId(pk);

			Materialzuschlag materialzuschlag = new Materialzuschlag(materialzuschlagDto.getMaterialIId(),
					materialzuschlagDto.getMandantCNr(), materialzuschlagDto.getTGueltigab(),
					materialzuschlagDto.getIId());
			em.persist(materialzuschlag);
			em.flush();

			HvDtoLogger<MaterialzuschlagDto> zsLogger = new HvDtoLogger<MaterialzuschlagDto>(em,
					materialzuschlagDto.getMaterialIId(), theClientDto);
			zsLogger.logInsert(materialzuschlagDto);
			setMaterialzuschlagFromMaterialzuschlagDto(materialzuschlag, materialzuschlagDto);
			return materialzuschlagDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public Integer createMaterialpreis(MaterialpreisDto materialpreisDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("MaterialpreisfindByMaterialIIdTGueltigab");
			query.setParameter(1, materialpreisDto.getMaterialIId());
			query.setParameter(2, materialpreisDto.getTGueltigab());
			// @todo getSingleResult oder getResultList ?
			Materialpreis doppelt = (Materialpreis) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MATERIALPREIS.UK"));
		} catch (NoResultException ex) {
			//
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MATERIALPREIS);
			materialpreisDto.setIId(pk);

			Materialpreis materialpreis = new Materialpreis(materialpreisDto.getIId(),
					materialpreisDto.getMaterialIId(), materialpreisDto.getTGueltigab(),
					materialpreisDto.getNPreisProKG());
			em.persist(materialpreis);
			em.flush();

			setMaterialpreisFromMaterialpreisDto(materialpreis, materialpreisDto);
			return materialpreisDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	public void removeMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (materialzuschlagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("materialzuschlagDto == null"));
		}
		if (materialzuschlagDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("materialzuschlagDto.getIId() == null"));
		}
		// try {

		Materialzuschlag materialzuschlag = em.find(Materialzuschlag.class, materialzuschlagDto.getIId());
		if (materialzuschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<MaterialzuschlagDto> zsLogger = new HvDtoLogger<MaterialzuschlagDto>(em,
				materialzuschlagDto.getMaterialIId(), theClientDto);
		zsLogger.logDelete(materialzuschlagDto);

		try {
			em.remove(materialzuschlag);
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

	public void removeMaterialpreis(MaterialpreisDto materialpreisDto, TheClientDto theClientDto) {

		Materialpreis materialpreis = em.find(Materialpreis.class, materialpreisDto.getIId());
		try {
			em.remove(materialpreis);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeKundematerial(KundematerialDto dto) {
		Kundematerial kundematerial = em.find(Kundematerial.class, dto.getIId());
		try {
			em.remove(kundematerial);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

	}

	public void updateMaterialzuschlag(MaterialzuschlagDto materialzuschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (materialzuschlagDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("materialzuschlagDto == null"));
		}

		materialzuschlagDto.setMandantCNr(theClientDto.getMandant());
		if (materialzuschlagDto.getIId() == null || materialzuschlagDto.getMandantCNr() == null
				|| materialzuschlagDto.getMaterialIId() == null || materialzuschlagDto.getTGueltigab() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"materialzuschlagDto.getIId() == null || materialzuschlagDto.getMaterialCNr() == null || materialzuschlagDto.getDGueltigab() == null"));
		}

		MaterialzuschlagDto materialzuschlagDto_Vorher = materialzuschlagFindByPrimaryKey(materialzuschlagDto.getIId());

		HvDtoLogger<MaterialzuschlagDto> logger = new HvDtoLogger<MaterialzuschlagDto>(em,
				materialzuschlagDto.getMaterialIId(), theClientDto);
		logger.log(materialzuschlagDto_Vorher, materialzuschlagDto);

		// try {
		Integer iId = materialzuschlagDto.getIId();
		Materialzuschlag materialzuschlag = em.find(Materialzuschlag.class, materialzuschlagDto.getIId());
		if (materialzuschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("MaterialzuschlagfindByMaterialIIdMandantCNrTGueltigab");
			query.setParameter(1, materialzuschlagDto.getIId());
			query.setParameter(2, materialzuschlagDto.getMandantCNr());
			query.setParameter(3, materialzuschlagDto.getTGueltigab());
			Integer iIdVorhanden = ((Materialzuschlag) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("WW_MATERIALZUSCHLAG.UK"));
			}
		} catch (NoResultException ex) {
			// NIX
		}
		setMaterialzuschlagFromMaterialzuschlagDto(materialzuschlag, materialzuschlagDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public void updateMaterialpreis(MaterialpreisDto materialpreisDto, TheClientDto theClientDto) {
		Integer iId = materialpreisDto.getIId();
		Materialpreis materialpreis = em.find(Materialpreis.class, materialpreisDto.getIId());
		if (materialpreis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("MaterialpreisfindByMaterialIIdTGueltigab");
			query.setParameter(1, materialpreisDto.getIId());
			query.setParameter(2, materialpreisDto.getTGueltigab());
			Integer iIdVorhanden = ((Materialpreis) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("WW_MATERIALPREIS.UK"));
			}
		} catch (NoResultException ex) {
			// NIX
		}
		setMaterialpreisFromMaterialpreisDto(materialpreis, materialpreisDto);
	}

	public void updateKundematerial(KundematerialDto dto, TheClientDto theClientDto) {
		Integer iId = dto.getIId();
		Kundematerial bean = em.find(Kundematerial.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("KundematerialfindByKundeIIdMaterialIId");
			query.setParameter(1, dto.getKundeIId());
			query.setParameter(2, dto.getMaterialIId());
			Integer iIdVorhanden = ((Kundematerial) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_KUNDEMATERIAL.UK"));
			}
		} catch (NoResultException ex) {
			// NIX
		}
		setKundematerialFromKundematerialDto(bean, dto);

	}

	public MaterialzuschlagDto materialzuschlagFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		myLogger.entry();
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Materialzuschlag materialzuschlag = em.find(Materialzuschlag.class, iId);
		if (materialzuschlag == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return assembleMaterialzuschlagDto(materialzuschlag);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public MaterialpreisDto materialpreisFindByPrimaryKey(Integer iId) {
		Materialpreis materialpreis = em.find(Materialpreis.class, iId);
		return MaterialpreisDtoAssembler.createDto(materialpreis);
	}

	public KundematerialDto kundematerialFindByPrimaryKey(Integer iId) {

		Kundematerial kundematerial = em.find(Kundematerial.class, iId);
		return assembleKundematerialDto(kundematerial);

	}

	private void setMaterialzuschlagFromMaterialzuschlagDto(Materialzuschlag materialzuschlag,
			MaterialzuschlagDto materialzuschlagDto) {
		materialzuschlag.setNZuschlag(materialzuschlagDto.getNZuschlag());
		em.merge(materialzuschlag);
		em.flush();
	}

	private void setMaterialpreisFromMaterialpreisDto(Materialpreis materialpreis, MaterialpreisDto materialpreisDto) {
		materialpreis.setNPreisProKG(materialpreisDto.getNPreisProKG());
		materialpreis.setTGueltigab(materialpreisDto.getTGueltigab());
		em.merge(materialpreis);
		em.flush();
	}

	private MaterialzuschlagDto assembleMaterialzuschlagDto(Materialzuschlag materialzuschlag) {
		return MaterialzuschlagDtoAssembler.createDto(materialzuschlag);
	}

	private KundematerialDto assembleKundematerialDto(Kundematerial bean) {
		return KundematerialDtoAssembler.createDto(bean);
	}

	private MaterialzuschlagDto[] assembleMaterialzuschlagDtos(Collection<?> materialzuschlags) {
		List<MaterialzuschlagDto> list = new ArrayList<MaterialzuschlagDto>();
		if (materialzuschlags != null) {
			Iterator<?> iterator = materialzuschlags.iterator();
			while (iterator.hasNext()) {
				Materialzuschlag materialzuschlag = (Materialzuschlag) iterator.next();
				list.add(assembleMaterialzuschlagDto(materialzuschlag));
			}
		}
		MaterialzuschlagDto[] returnArray = new MaterialzuschlagDto[list.size()];
		return (MaterialzuschlagDto[]) list.toArray(returnArray);
	}

	private void setMaterialsprFromMaterialsprDto(Materialspr materialspr, MaterialsprDto materialsprDto) {
		materialspr.setCBez(materialsprDto.getCBez());
		em.merge(materialspr);
		em.flush();
	}

	private MaterialsprDto assembleMaterialsprDto(Materialspr materialspr) {
		return MaterialsprDtoAssembler.createDto(materialspr);
	}

	private MaterialsprDto[] assembleMaterialsprDtos(Collection<?> materialsprs) {
		List<MaterialsprDto> list = new ArrayList<MaterialsprDto>();
		if (materialsprs != null) {
			Iterator<?> iterator = materialsprs.iterator();
			while (iterator.hasNext()) {
				Materialspr materialspr = (Materialspr) iterator.next();
				list.add(assembleMaterialsprDto(materialspr));
			}
		}
		MaterialsprDto[] returnArray = new MaterialsprDto[list.size()];
		return (MaterialsprDto[]) list.toArray(returnArray);
	}
}
