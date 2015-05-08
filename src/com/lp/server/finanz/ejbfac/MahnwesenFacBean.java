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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.hibernate.criterion.Restrictions;

import com.lp.server.finanz.ejb.Mahnlauf;
import com.lp.server.finanz.ejb.Mahnstufe;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejb.Mahnung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzMahnung;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnlaufDtoAssembler;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahnstufeDtoAssembler;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.finanz.service.MahnungDtoAssembler;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class MahnwesenFacBean extends Facade implements MahnwesenFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public MahnlaufDto createMahnlauf(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			MahnlaufDto mahnlaufDto = new MahnlaufDto();
			mahnlaufDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_MAHNLAUF));
			mahnlaufDto.setMandantCNr(theClientDto.getMandant());
			mahnlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mahnlaufDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			Mahnlauf mahnlauf = new Mahnlauf(mahnlaufDto.getIId(),
					mahnlaufDto.getMandantCNr(),
					mahnlaufDto.getPersonalIIdAnlegen(),
					mahnlaufDto.getPersonalIIdAendern());
			em.persist(mahnlauf);
			em.flush();
			mahnlaufDto.setTAendern(mahnlauf.getTAendern());
			mahnlaufDto.setTAnlegen(mahnlauf.getTAnlegen());
			setMahnlaufFromMahnlaufDto(mahnlauf, mahnlaufDto);
			return mahnlaufDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MahnlaufDto createMahnlaufMitMahnvorschlag(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			MahnlaufDto mahnlaufDto = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungReport.class);
			// Filter nach Mandant.
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Filter nach Stati
			String[] sStati = new String[] { RechnungFac.STATUS_OFFEN,
					RechnungFac.STATUS_TEILBEZAHLT, RechnungFac.STATUS_VERBUCHT };
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, sStati));

			// PJ 17236
			ParametermandantDto p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_RECHNUNG,
					ParameterFac.PARAMETER_MAHNUNGEN_AB_GF_JAHR);
			Integer iGFJahrAB = (Integer) p.getCWertAsObject();
			c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_I_GESCHAEFTSJAHR,
					iGFJahrAB));

			c.createAlias(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra").add(
					Restrictions.not(Restrictions.eq("ra.rechnungtyp_c_nr",
							RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)));

			// Query ausfuehren.
			List<?> listRechnungen = c.list();
			// nur, wenn Rechnungen eingetragen sind.
			if (listRechnungen.size() > 0) {
				mahnlaufDto = context.getBusinessObject(MahnwesenFac.class)
						.createMahnlauf(theClientDto);
				for (Iterator<?> iter = listRechnungen.iterator(); iter
						.hasNext();) {

					FLRRechnungReport flrRechnung = (FLRRechnungReport) iter
							.next();

					RechnungDto rechnungDto = getRechnungFac()
							.rechnungFindByPrimaryKey(flrRechnung.getI_id());
					boolean mahnbar = false;
					// kann die Rechnung gemahnt werden?
					mahnbar = istRechnungMahnbar(flrRechnung.getI_id(),
							theClientDto);

					myLogger.logData(rechnungDto.getRechnungartCNr() + ": "
							+ flrRechnung.getC_nr() + " mahnbar: " + mahnbar);
					// mahnung anlegen
					if (mahnbar) {
						Integer mahnstufe = berechneMahnstufeFuerRechnung(
								rechnungDto, theClientDto);
						if (mahnstufe != null) {
							// pruefen, ob diese Mahnstufe schon gemahnt wurde
							try {
								Query query = em
										.createNamedQuery("MahnungfindByRechnungMahnstufe");
								query.setParameter(1, flrRechnung.getI_id());
								query.setParameter(2, mahnstufe);
								// @todo getSingleResult oder getResultList ?
								Mahnung mahnung = (Mahnung) query
										.getSingleResult();
								myLogger.logData(rechnungDto
										.getRechnungartCNr()
										+ ": "
										+ flrRechnung.getC_nr()
										+ ", Stufe "
										+ mahnstufe
										+ " ist bereits in einem Mahnvorschlag");
							} catch (NoResultException ex1) {
								
								Integer letzteMahnstufe=getAktuelleMahnstufeEinerRechnung(flrRechnung.getI_id(), theClientDto);
								
								if (letzteMahnstufe == null
										|| !letzteMahnstufe
												.equals(mahnstufe)
										|| (letzteMahnstufe != null && letzteMahnstufe == FinanzServiceFac.MAHNSTUFE_99)) {
									myLogger.logData(rechnungDto
											.getRechnungartCNr()
											+ ": "
											+ flrRechnung.getC_nr()
											+ " wird gemahnt");
									MahnungDto mahnungDto = new MahnungDto();
									mahnungDto.setTMahndatum(super.getDate());
									mahnungDto.setMahnlaufIId(mahnlaufDto
											.getIId());
									mahnungDto.setMahnstufeIId(mahnstufe);
									mahnungDto.setRechnungIId(flrRechnung
											.getI_id());

									mahnungDto.setTGedruckt(null);
									// Mahnsperre
									if (rechnungDto.getTMahnsperrebis() != null) {
										if (super.getDate()
												.before(rechnungDto
														.getTMahnsperrebis())) {
											continue;
										}
									}

									Integer mahnstufeIId=getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);
									
									if (mahnstufeIId != null
											&& mahnstufeIId
													.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
										mahnungDto
												.setTGedruckt(new java.sql.Timestamp(
														System.currentTimeMillis()));
										mahnungDto
												.setPersonalIIdGedruckt(theClientDto
														.getIDPersonal());
									}

									
									
									
									
										mahnungDto
												.setTLetztesmahndatum(getAktuellesMahndatumEinerRechnung(flrRechnung.getI_id(), theClientDto));
										mahnungDto
										.setMahnstufeIIdLetztemahnstufe(mahnstufeIId);
									
									context.getBusinessObject(
											MahnwesenFac.class).createMahnung(
											mahnungDto, theClientDto);
								} else {
									myLogger.logData(rechnungDto
											.getRechnungartCNr()
											+ ": "
											+ flrRechnung.getC_nr()
											+ " hat bereits Mahnstufe "
											+ mahnstufe);
								}
							}
						} else {
							myLogger.logData(rechnungDto.getRechnungartCNr()
									+ ": " + flrRechnung.getC_nr()
									+ " muss nicht gemahnt werden");
						}
					}
				}
			}
			return mahnlaufDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public MahnlaufDto createMahnlaufAusRechnung(Integer rechnungIId,
			Integer mahnstufeIId, java.sql.Date tMahndatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		MahnlaufDto mahnlauf = null;
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("rechnungIId == null"));
		}
		if (mahnstufeIId != null && tMahndatum != null) {
			// wurde die Rechnung bereits mit dieser Mahnstufe gemahnt?
			MahnungDto mahnungDto = mahnungFindByRechnungMahnstufe(rechnungIId,
					mahnstufeIId);
			if (mahnungDto != null) {
				if (mahnungDto.getTGedruckt() == null) {
					// wenn noch nicht gedruckt -> dann jetzt
					mahneMahnung(mahnungDto.getIId(), theClientDto);
				}
				mahnlauf = mahnlaufFindByPrimaryKey(mahnungDto.getMahnlaufIId());
			} else {
				// wurde noch nicht gemahnt
				MahnlaufDto mahnlaufDto = createMahnlauf(theClientDto);
				MahnungDto mahnungDtoNew = new MahnungDto();
				mahnungDtoNew.setMahnlaufIId(mahnlaufDto.getIId());
				mahnungDtoNew.setMahnstufeIId(mahnstufeIId);
				mahnungDtoNew.setMahnstufeIIdLetztemahnstufe(null); // spaeter
				mahnungDtoNew.setRechnungIId(rechnungIId);
				mahnungDtoNew.setTMahndatum(tMahndatum);
				// speichern
				mahnungDtoNew = createMahnung(mahnungDtoNew, theClientDto);
				// und mahnen
				mahneMahnlauf(mahnlaufDto.getIId(), theClientDto);
				mahnlauf = mahnlaufDto;
			}
		} else if (mahnstufeIId == null && tMahndatum == null) {
			MahnungDto[] mahnungDtos = mahnungFindByRechnungIId(rechnungIId);
			for (int i = mahnungDtos.length - 1; i >= 0; i--) {
				mahneMahnungRueckgaengig(mahnungDtos[i].getIId(), theClientDto);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mahnstufeIId==null || tMahndatum==null"));
		}
		return mahnlauf;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removeMahnlauf(MahnlaufDto mahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (mahnlaufDto != null) {
			Integer iId = mahnlaufDto.getIId();
			// zuerst die angehaengten Mahnungen loeschen
			MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(iId);
			for (int i = 0; i < mahnungen.length; i++) {
				// es darf noch keine der Mahnungen gedruckt sein.
				if (mahnungen[i].getTGedruckt() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
							"");
				}
			}
			// Einzelmahnungen loeschen
			for (int i = 0; i < mahnungen.length; i++) {
				getMahnwesenFac().removeMahnung(mahnungen[i], theClientDto);
			}

			getMahnwesenFac().removeMahnlauf(iId);

			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public void removeMahnlauf(Integer iMahnlaufIId) {
		// Mahnlauf loeschen
		// try {
		Mahnlauf toRemove = em.find(Mahnlauf.class, iMahnlaufIId);
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

	public void updateMahnlauf(MahnlaufDto mahnlaufDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (mahnlaufDto != null) {
			Integer iId = mahnlaufDto.getIId();
			mahnlaufDto.setTAendern(super.getTimestamp());
			mahnlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Mahnlauf mahnlauf = em.find(Mahnlauf.class, iId);
			if (mahnlauf == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setMahnlaufFromMahnlaufDto(mahnlauf, mahnlaufDto);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public MahnlaufDto mahnlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Mahnlauf mahnlauf = em.find(Mahnlauf.class, iId);
		if (mahnlauf == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMahnlaufDto(mahnlauf);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public MahnlaufDto[] mahnlaufFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("MahnlauffindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		return assembleMahnlaufDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	private void setMahnlaufFromMahnlaufDto(Mahnlauf mahnlauf,
			MahnlaufDto mahnlaufDto) {
		mahnlauf.setMandantCNr(mahnlaufDto.getMandantCNr());
		mahnlauf.setPersonalIIdAnlegen(mahnlaufDto.getPersonalIIdAnlegen());
		mahnlauf.setTAnlegen(mahnlaufDto.getTAnlegen());
		mahnlauf.setPersonalIIdAendern(mahnlaufDto.getPersonalIIdAendern());
		mahnlauf.setTAendern(mahnlaufDto.getTAendern());
		em.merge(mahnlauf);
		em.flush();
	}

	private MahnlaufDto assembleMahnlaufDto(Mahnlauf mahnlauf) {
		return MahnlaufDtoAssembler.createDto(mahnlauf);
	}

	private MahnlaufDto[] assembleMahnlaufDtos(Collection<?> mahnlaufs) {
		List<MahnlaufDto> list = new ArrayList<MahnlaufDto>();
		if (mahnlaufs != null) {
			Iterator<?> iterator = mahnlaufs.iterator();
			while (iterator.hasNext()) {
				Mahnlauf mahnlauf = (Mahnlauf) iterator.next();
				list.add(assembleMahnlaufDto(mahnlauf));
			}
		}
		MahnlaufDto[] returnArray = new MahnlaufDto[list.size()];
		return (MahnlaufDto[]) list.toArray(returnArray);
	}

	public Integer createMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnstufeDto);
		mahnstufeDto.setMandantCNr(theClientDto.getMandant());
		try {
			Mahnstufe mahnstufe = new Mahnstufe(mahnstufeDto.getIId(),
					mahnstufeDto.getMandantCNr(), mahnstufeDto.getITage());
			em.persist(mahnstufe);
			em.flush();
			setMahnstufeFromMahnstufeDto(mahnstufe, mahnstufeDto);
			return mahnstufeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (mahnstufeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mahnstufeDto == null"));
		}
		if (mahnstufeDto != null) {
			// try {
			Integer iId = mahnstufeDto.getIId();
			if (iId.intValue() == FinanzServiceFac.MAHNSTUFE_1
					|| iId.intValue() == FinanzServiceFac.MAHNSTUFE_2
					|| iId.intValue() == FinanzServiceFac.MAHNSTUFE_3
					|| iId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
						new Exception("Mahnstufe " + iId.intValue()
								+ " darf nicht geloescht werden"));
			}
			Mahnstufe toRemove = em.find(Mahnstufe.class, new MahnstufePK(iId,
					mahnstufeDto.getMandantCNr()));
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public void updateMahnstufe(MahnstufeDto mahnstufeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (mahnstufeDto != null) {
			// try {
			Mahnstufe mahnstufe = em.find(Mahnstufe.class, new MahnstufePK(
					mahnstufeDto.getIId(), mahnstufeDto.getMandantCNr()));
			if (mahnstufe == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setMahnstufeFromMahnstufeDto(mahnstufe, mahnstufeDto);
			// }
			// catch (FinderException e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			// }
		}
	}

	public MahnstufeDto mahnstufeFindByPrimaryKey(MahnstufePK mahnstufePK)
			throws EJBExceptionLP {
		// try {
		Mahnstufe mahnstufe = em.find(Mahnstufe.class, mahnstufePK);
		if (mahnstufe == null) {
			return null;
		}
		return assembleMahnstufeDto(mahnstufe);

		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	public MahnstufeDto[] mahnstufeFindByMandantCNr(String mandantCNr)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("MahnstufefindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		Collection<Mahnstufe> c2 = new LinkedList<Mahnstufe>();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Mahnstufe item = (Mahnstufe) iter.next();
			// if(item.getIId().intValue()!=FinanzServiceFac.MAHNSTUFE_99) {
			c2.add(item);
			// }
		}
		return assembleMahnstufeDtos(c2);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	public LinkedHashMap<Integer, Integer> getAllMahnstufe(String mandantCNr)
			throws EJBExceptionLP {
		LinkedHashMap<Integer, Integer> uMap = new LinkedHashMap<Integer, Integer>();
		MahnstufeDto[] mahnstufen = mahnstufeFindByMandantCNr(mandantCNr);
		for (int i = 0; i < mahnstufen.length; i++) {
			uMap.put(mahnstufen[i].getIId(), mahnstufen[i].getIId());
		}
		return uMap;
	}

	private void setMahnstufeFromMahnstufeDto(Mahnstufe mahnstufe,
			MahnstufeDto mahnstufeDto) {
		mahnstufe.setITage(mahnstufeDto.getITage());
		mahnstufe.setNMahnspesen(mahnstufeDto.getNMahnspesen());
		mahnstufe.setFZinssatz(mahnstufeDto.getFZinssatz());
		em.merge(mahnstufe);
		em.flush();
	}

	private MahnstufeDto assembleMahnstufeDto(Mahnstufe mahnstufe) {
		return MahnstufeDtoAssembler.createDto(mahnstufe);
	}

	private MahnstufeDto[] assembleMahnstufeDtos(Collection<?> mahnstufes) {
		List<MahnstufeDto> list = new ArrayList<MahnstufeDto>();
		if (mahnstufes != null) {
			Iterator<?> iterator = mahnstufes.iterator();
			while (iterator.hasNext()) {
				Mahnstufe mahnstufe = (Mahnstufe) iterator.next();
				list.add(assembleMahnstufeDto(mahnstufe));
			}
		}
		MahnstufeDto[] returnArray = new MahnstufeDto[list.size()];
		return (MahnstufeDto[]) list.toArray(returnArray);
	}

	public MahnungDto createMahnung(MahnungDto mahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		try {
			mahnungDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_MAHNUNG));
			mahnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mahnungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			mahnungDto.setMandantCNr(theClientDto.getMandant());
			Mahnung mahnung = new Mahnung(mahnungDto.getIId(),
					mahnungDto.getMandantCNr(), mahnungDto.getMahnlaufIId(),
					mahnungDto.getMahnstufeIId(), mahnungDto.getRechnungIId(),
					mahnungDto.getTMahndatum(),
					mahnungDto.getPersonalIIdAnlegen(),
					mahnungDto.getPersonalIIdAendern());
			em.persist(mahnung);
			em.flush();
			mahnung.setTLetztesmahndatum(mahnungDto.getTLetztesmahndatum());
			mahnungDto.setTAendern(mahnung.getTAendern());
			mahnungDto.setTAnlegen(mahnung.getTAnlegen());
			setMahnungFromMahnungDto(mahnung, mahnungDto);
			return mahnungDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public Integer getAktuelleMahnstufeEinerRechnung(Integer rechnungIId,
			TheClientDto theClientDto) {
		Integer mahnstufeIId = null;
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT  MAX(m.mahnstufe_i_id) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.i_id=  "
				+ rechnungIId + " AND m.t_gedruckt IS NOT NULL";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {
			mahnstufeIId = (Integer) resultListIterator.next();
		}
		return mahnstufeIId;
	}

	public java.sql.Date getAktuellesMahndatumEinerRechnung(
			Integer rechnungIId, TheClientDto theClientDto) {
		java.sql.Date date = null;
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT  MAX(m.flrmahnlauf.t_anlegen) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.i_id=  "
				+ rechnungIId + " AND m.t_gedruckt IS NOT NULL";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {

			
			java.util.Date utilDate =(java.util.Date) resultListIterator.next();
			if(utilDate!=null){
				date = new java.sql.Date(utilDate.getTime());
			}
		}
		return date;
	}

	public void removeMahnung(MahnungDto mahnungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		if (mahnungDto != null) {
			if (mahnungDto.getTGedruckt() != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
						"");
			}
			Integer iId = mahnungDto.getIId();
			// try {
			Mahnung toRemove = em.find(Mahnung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public MahnungDto updateMahnung(MahnungDto mahnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		// try {
		Integer iId = mahnungDto.getIId();
		mahnungDto.setTAendern(super.getTimestamp());
		mahnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		Mahnung mahnung = em.find(Mahnung.class, iId);
		if (mahnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setMahnungFromMahnungDto(mahnung, mahnungDto);
		return mahnungDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
	}

	public MahnungDto mahnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Mahnung mahnung = em.find(Mahnung.class, iId);
		if (mahnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMahnungDto(mahnung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public MahnungDto[] mahnungFindByMahnlaufIId(Integer mahnlaufIId)
			throws EJBExceptionLP {
		MahnungDto[] mahnungen = null;
		// try {
		Query query = em.createNamedQuery("MahnungfindByMahnlaufIId");
		query.setParameter(1, mahnlaufIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		mahnungen = assembleMahnungDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// nothing here
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return mahnungen;
	}

	public MahnungDto[] mahnungFindByRechnungIId(Integer rechnungIId)
			throws EJBExceptionLP {
		MahnungDto[] mahnungen = null;
		// try {
		Query query = em.createNamedQuery("MahnungfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		mahnungen = assembleMahnungDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// nothing here
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return mahnungen;
	}

	public MahnungDto mahnungFindByRechnungMahnstufe(Integer rechnungIId,
			Integer mahnstufeIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("MahnungfindByRechnungMahnstufe");
			query.setParameter(1, rechnungIId);
			query.setParameter(2, mahnstufeIId);
			Mahnung mahnung = (Mahnung) query.getSingleResult();
			return assembleMahnungDto(mahnung);

			// }
			// catch (javax.ejb.ObjectNotFoundException ex) {
			// return null;
		} catch (NoResultException ex) {
			return null;
		}
	}

	private void setMahnungFromMahnungDto(Mahnung mahnung, MahnungDto mahnungDto) {
		mahnung.setMahnlaufIId(mahnungDto.getMahnlaufIId());
		mahnung.setMahnstufeIId(mahnungDto.getMahnstufeIId());
		mahnung.setRechnungIId(mahnungDto.getRechnungIId());
		mahnung.setTMahndatum(mahnungDto.getTMahndatum());
		mahnung.setTGedruckt(mahnungDto.getTGedruckt());
		mahnung.setPersonalIIdGedruckt(mahnungDto.getPersonalIIdGedruckt());
		mahnung.setTAnlegen(mahnungDto.getTAnlegen());
		mahnung.setPersonalIIdAnlegen(mahnungDto.getPersonalIIdAnlegen());
		mahnung.setTAendern(mahnungDto.getTAendern());
		mahnung.setPersonalIIdAendern(mahnungDto.getPersonalIIdAendern());
		mahnung.setMahnstufeIIdLetztemahnstufe(mahnungDto
				.getMahnstufeIIdLetztemahnstufe());
		mahnung.setTLetztesmahndatum(mahnungDto.getTLetztesmahndatum());
		mahnung.setMandantCNr(mahnungDto.getMandantCNr());
		em.merge(mahnung);
		em.flush();
	}

	private MahnungDto assembleMahnungDto(Mahnung mahnung) {
		return MahnungDtoAssembler.createDto(mahnung);
	}

	private MahnungDto[] assembleMahnungDtos(Collection<?> mahnungs) {
		List<MahnungDto> list = new ArrayList<MahnungDto>();
		if (mahnungs != null) {
			Iterator<?> iterator = mahnungs.iterator();
			while (iterator.hasNext()) {
				Mahnung mahnung = (Mahnung) iterator.next();
				list.add(assembleMahnungDto(mahnung));
			}
		}
		MahnungDto[] returnArray = new MahnungDto[list.size()];
		return (MahnungDto[]) list.toArray(returnArray);
	}

	public int getMahntageVonMahnstufe(Integer mahnstufeIId,
			TheClientDto theClientDto) {
		int tage = 0;
		MahnstufeDto[] alle = mahnstufeFindByMandantCNr(theClientDto
				.getMandant());
		for (int i = 0; i < alle.length; i++) {
			MahnstufeDto mahnstufe = alle[i];
			if (mahnstufe.getIId().intValue() <= mahnstufeIId.intValue()) {
				tage = tage + mahnstufe.getITage().intValue();
			}
		}
		return tage;
	}

	/**
	 * Berechnen, in welche Mahnstufe eine Rechnung faellt.
	 * 
	 * @param rechnungDto
	 *            RechnungDto
	 * @param theClientDto
	 *            String
	 * @return Integer mahnstufe bzw. null, wenn noch keine mahnstufe erreicht
	 */
	public Integer berechneMahnstufeFuerRechnung(RechnungDto rechnungDto,
			TheClientDto theClientDto) {
		try {
			Integer zahlungszielIId = rechnungDto.getZahlungszielIId();
			if (zahlungszielIId == null) {
				// Rechnungen ohne Zahlungsziel werden nicht gemahnt
				return null;
			}
			
			Integer mahnstufeIId=getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);
			java.sql.Date mahndatum=getAktuellesMahndatumEinerRechnung(rechnungDto.getIId(), theClientDto);
			
			if (mahnstufeIId != null) {
				// Rechnungen der letzten Mahnstufe werden nicht mehr weiter
				// gemahnt
				if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
					return FinanzServiceFac.MAHNSTUFE_99;
				}
			}
			MahnstufeDto[] mahnstufen = mahnstufeFindByMandantCNr(theClientDto
					.getMandant());
			if (mahnstufen == null
					|| mahnstufen.length < FinanzServiceFac.MAHNSTUFE_3) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN,
						new Exception("Fuer Mandant "
								+ theClientDto.getMandant()
								+ " sind keine Mahnstufen eingetragen"));
			}
			java.sql.Date dAusgangsdatum;

			// wurde die Rechnung schon gemahnt?
			if (mahnstufeIId != null
					&& mahndatum != null) {
				// vom letzten mahndatum ausgehen
				dAusgangsdatum =mahndatum;
			} else {
				ZahlungszielDto zahlungszielDto = getMandantFac()
						.zahlungszielFindByPrimaryKey(
								rechnungDto.getZahlungszielIId(), theClientDto);
				int iNettotage;
				if (zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
					iNettotage = zahlungszielDto.getAnzahlZieltageFuerNetto()
							.intValue();
				} else {
					iNettotage = 0;
				}
				// Belegdatum + Zahlungsziel
				dAusgangsdatum = Helper.addiereTageZuDatum(
						rechnungDto.getTBelegdatum(), iNettotage);
			}
			int tageSeitAusgangsdatum = Helper.getDifferenzInTagen(
					dAusgangsdatum, super.getDate());
			
			if(rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)){
				tageSeitAusgangsdatum = 9999;
			}
			
			if (mahnstufeIId == null) {
				// eventuell erste Mahnung
				int iTageMS1 = 0;
				for (int i = 0; i < mahnstufen.length; i++) {
					if (mahnstufen[i].getIId().intValue() == FinanzServiceFac.MAHNSTUFE_1) {
						if (mahnstufen[i].getITage() != null) {
							iTageMS1 = mahnstufen[i].getITage().intValue();
						}
						if (tageSeitAusgangsdatum >= iTageMS1) {
							return mahnstufen[i].getIId();
						}
					}
				}
			} else {
				// n'te Mahnung
				int iTage = 0;
				for (int i = 0; i < mahnstufen.length; i++) {
					// ich suche die naechstgroessere Mahnstufe
					if (mahnstufen[i].getIId().intValue() > mahnstufeIId.intValue()) {
						if (mahnstufen[i].getITage() != null) {
							iTage = mahnstufen[i].getITage().intValue();
						}
						if (tageSeitAusgangsdatum >= iTage) {
							return mahnstufen[i].getIId();
						} else {
							return null;
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return null;
	}

	/**
	 * Eine Mahnung aus einem Mahnlauf durchfuehren.
	 * 
	 * @param mahnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void mahneMahnung(Integer mahnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Mahnung mahnung = em.find(Mahnung.class, mahnungIId);
			if (mahnung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			if (mahnung.getTGedruckt() == null) {
				RechnungDto rechnungDto = getRechnungFac()
						.rechnungFindByPrimaryKey(mahnung.getRechnungIId());
				// Stornierte nicht mahnen.
				if (!rechnungDto.getStatusCNr().equals(
						RechnungFac.STATUS_STORNIERT)) {
					// nur dann mahnen, wenn die Rechnung keine mahnsperre har
					// oder diese schon vorbei ist.
					if (rechnungDto.getTMahnsperrebis() == null
							|| rechnungDto.getTMahnsperrebis()
									.before(getDate())) {
						// die neue Mahnstufe muss groesser sein als die alte
						// gleich kann sie auch sein, wegen dem drucken
						
						Integer mahnstufeIId=getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);
						java.sql.Date mahndatum=getAktuellesMahndatumEinerRechnung(rechnungDto.getIId(), theClientDto);
						if (mahnstufeIId == null
								|| mahnstufeIId.intValue() <= mahnung
										.getMahnstufeIId().intValue()) {
							// Alten Mahnstatus der Rechnung sichern
							mahnung.setMahnstufeIIdLetztemahnstufe(mahnstufeIId);
							mahnung.setTLetztesmahndatum(mahndatum);
							
							// jetzt kann die Mahnung als gemahnt markiert
							// werden
							mahnung.setTGedruckt(getTimestamp());

							ParametermandantDto p = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_RECHNUNG,
											ParameterFac.PARAMETER_LIEFERSPERRE_AB_MAHNSTUFE);
							Integer iSperreAbMahnstufe = (Integer) p
									.getCWertAsObject();

							if (mahnung.getMahnstufeIId() >= iSperreAbMahnstufe && 	!rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
								Kunde k = em.find(Kunde.class,
										rechnungDto.getKundeIId());
								if (k.getTLiefersperream() == null) {
									MahnlaufDto mlDto = mahnlaufFindByPrimaryKey(mahnung
											.getMahnlaufIId());
									k.setTLiefersperream(new Date(mlDto
											.getTAnlegen().getTime()));
									em.merge(k);
									em.flush();
								}
							}

							mahnung.setPersonalIIdGedruckt(theClientDto
									.getIDPersonal());
						} else {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE,
									new Exception(
											"Die Rechnung "
													+ rechnungDto.getCNr()
													+ " darf nicht gemahnt werden: alte Mahnstufe="
													+ mahnstufeIId
													+ ", neue Mahnstufe="
													+ mahnung.getMahnstufeIId()));
						}
					} else {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RECHNUNG_MAHNSPERRE,
								new Exception(
										"Die Rechnung "
												+ rechnungDto.getCNr()
												+ " darf nicht gemahnt werden: Mahnsperre bis "
												+ rechnungDto
														.getTMahnsperrebis()));
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Eine Mahnung aus einem Mahnlauf durchfuehren.
	 * 
	 * @param mahnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void mahneMahnungRueckgaengig(Integer mahnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Mahnung mahnung = em.find(Mahnung.class, mahnungIId);
		if (mahnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		Rechnung rechnung = em.find(Rechnung.class, mahnung.getRechnungIId());

		// nur wenn ich auch die richtige mahnung dazu hab.
		
		Integer mahnstufeIId=getAktuelleMahnstufeEinerRechnung(rechnung.getIId(), theClientDto);
		
		if (mahnstufeIId == null
				|| rechnung.getTMahnsperrebis() != null || // dieser fall sollte
				// nicht eintreten.
				(mahnstufeIId != null
						&& mahnung.getMahnstufeIId() != null
						&& mahnung.getTGedruckt() != null && mahnstufeIId.equals(mahnung.getMahnstufeIId()))) {
			// Alten Mahnstatus loeschen
			mahnung.setMahnstufeIIdLetztemahnstufe(null);
			mahnung.setTLetztesmahndatum(null);
			// jetzt kann die Mahnung als gemahnt markiert werden
			mahnung.setTGedruckt(null);
			mahnung.setPersonalIIdGedruckt(null);
		}

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes mahnen. mahnt schrittweise nur die
	 * Mahnungen eines Kunden und gibt desse Id zurueck gibt null zurueck, wenn
	 * alle Mahnungen des Kunden gemahnt sind
	 * 
	 * @param mahnlaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void mahneMahnlauf(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufIId);
		for (int i = 0; i < mahnungen.length; i++) {
			try {
				getMahnwesenFac().mahneMahnung(mahnungen[i].getIId(),
						theClientDto);
			} catch (EJBExceptionLP ex1) {
				if (ex1.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE) {
					// nothing here
				} else {
					throw ex1;
				}
			}
		}
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes mahnen.
	 * 
	 * @param mahnlaufIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void mahneMahnlaufRueckgaengig(Integer mahnlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufIId);
		for (int i = 0; i < mahnungen.length; i++) {
			getMahnwesenFac().mahneMahnungRueckgaengig(mahnungen[i].getIId(),
					theClientDto);
		}
	}

	public java.sql.Date berechneFaelligkeitsdatumFuerMahnstufe(
			java.util.Date dBelegdatum, Integer zahlungszielIId,
			Integer mahnstufeIId, TheClientDto theClientDto) {
		try {
			ZahlungszielDto zzDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return null;
	}

	public Boolean bGibtEsEinenOffenenMahnlauf(String mandantCNr,
			TheClientDto theClientDto) {
		boolean bEsGibtEinenOffenen = false;
		MahnlaufDto[] mahnlaufDtos = mahnlaufFindByMandantCNr(mandantCNr);
		for (int i = 0; i < mahnlaufDtos.length; i++) {
			MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufDtos[i]
					.getIId());
			for (int j = 0; j < mahnungen.length; j++) {
				if (mahnungen[j].getTGedruckt() == null) {
					bEsGibtEinenOffenen = true;
					break;
				}
			}
		}
		return new Boolean(bEsGibtEinenOffenen);
	}

	public boolean istRechnungMahnbar(Integer rechnungIId,
			TheClientDto theClientDto) {
		try {
			RechnungDto rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(rechnungIId);
			String statusCNr = rechnungDto.getStatusCNr();
			if (statusCNr.equals(RechnungFac.STATUS_TEILBEZAHLT)
					|| statusCNr.equals(RechnungFac.STATUS_OFFEN)
					|| statusCNr.equals(RechnungFac.STATUS_VERBUCHT)) {

				ParametermandantDto p = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_MAHNUNG_AB_RECHNUNGSBETRAG);
				BigDecimal bdWert = new BigDecimal(p.getCWert());

				BigDecimal bdBruttoFw = rechnungDto.getNWertfw().add(
						rechnungDto.getNWertustfw());

				BigDecimal bdBezahltUstFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungUstFw(
								rechnungDto.getIId(), null);
				BigDecimal bdBezahltNettoFw = getRechnungFac()
						.getBereitsBezahltWertVonRechnungFw(
								rechnungDto.getIId(), null);

				BigDecimal offenFw = bdBruttoFw.subtract(bdBezahltNettoFw
						.add(bdBezahltUstFw));

				BigDecimal offenMandWhg = getLocaleFac()
						.rechneUmInAndereWaehrungZuDatum(offenFw,
								rechnungDto.getWaehrungCNr(),
								theClientDto.getSMandantenwaehrung(),
								new Date(System.currentTimeMillis()),
								theClientDto);

				if (offenMandWhg.abs().doubleValue() <= bdWert.doubleValue()) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return false;
		}
	}

	public BigDecimal getSummeEinesKundenImMahnlauf(Integer mahnlaufIId,
			Integer kundeIId, TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzMahnung.class);
			// Filter nach Mahnlauf
			c.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID,
					mahnlaufIId));
			// Filter nach Lunde
			c.createCriteria(FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT).add(
					Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID,
							kundeIId));
			// Sortierung aufsteigend nach Kontonummer
			List<?> list = c.list();
			BigDecimal bdSumme = new BigDecimal(0);
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung mahnung = (FLRFinanzMahnung) iter.next();
				if (mahnung.getFlrrechnungreport().getN_wert() != null) {
					BigDecimal bdWert = mahnung.getFlrrechnungreport()
							.getN_wert()
							.add(mahnung.getFlrrechnungreport().getN_wertust());
					if (bdWert != null) {
						BigDecimal bdOffen = bdWert
								.subtract(
										getRechnungFac()
												.getBereitsBezahltWertVonRechnung(
														mahnung.getFlrrechnungreport()
																.getI_id(),
														null))
								.subtract(
										getRechnungFac()
												.getBereitsBezahltWertVonRechnungUst(
														mahnung.getFlrrechnungreport()
																.getI_id(),
														null));
						if (bdOffen.compareTo(new BigDecimal(0)) > 0) {
							bdSumme = bdSumme.add(bdOffen);
						}
					}
				}
			}
			return bdSumme;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
