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

import java.math.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.artikel.ejb.*;
import com.lp.server.artikel.fastlanereader.generated.*;
import com.lp.server.artikel.service.*;
import com.lp.server.system.pkgenerator.*;
import com.lp.server.system.pkgenerator.bl.*;
import com.lp.server.system.service.*;
import com.lp.server.util.*;
import com.lp.server.util.fastlanereader.*;
import com.lp.util.*;

import net.sf.jasperreports.engine.*;
import com.lp.server.util.report.JasperPrintLP;
/**
 *
 * <p> Diese Klasse kuemmert sich um ...</p>
 *
 * <p>Copyright Logistik Pur GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2013/01/17 16:15:13 $
 *
 * @todo diese Klasse darf nicht von LPReport erben.
 */
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class InventurFacBean extends LPReport implements InventurFac,
		JRDataSource {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;
	private String sAktuellerReport = null;

	private static int REPORT_INVENTURLISTE_ARTIKELNUMMER = 0;
	private static int REPORT_INVENTURLISTE_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_INVENTURLISTE_SERIENNUMMERCHARGENNUMMER = 2;
	private static int REPORT_INVENTURLISTE_INVENTURMENGE = 3;
	private static int REPORT_INVENTURLISTE_INVENTURPREIS = 4;
	private static int REPORT_INVENTURLISTE_LAGER = 5;
	private static int REPORT_INVENTURLISTE_INVENTURWERT = 6;
	private static int REPORT_INVENTURLISTE_LIEF1PREIS_ZUM_INVENTURDATUM = 7;
	private static int REPORT_INVENTURLISTE_LIEF1PREIS_AKTUELL = 8;

	private static int REPORT_INVENTURSTAND_ARTIKELNUMMER = 0;
	private static int REPORT_INVENTURSTAND_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG = 2;
	private static int REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG2 = 3;
	private static int REPORT_INVENTURSTAND_ARTIKELKURZBEZEICHNUNG = 4;
	private static int REPORT_INVENTURSTAND_SERIENNUMMERCHARGENNUMMER = 5;
	private static int REPORT_INVENTURSTAND_INVENTURMENGE = 6;
	private static int REPORT_INVENTURSTAND_INVENTURPREIS = 7;
	private static int REPORT_INVENTURSTAND_LAGER = 8;
	private static int REPORT_INVENTURSTAND_INVENTURWERT = 9;
	private static int REPORT_INVENTURSTAND_LAGERSTAND = 10;
	private static int REPORT_INVENTURSTAND_ARTIKELART = 11;
	private static int REPORT_INVENTURSTAND_ARTIKELGRUPPE = 12;
	private static int REPORT_INVENTURSTAND_ARTIKELKLASSE = 13;
	private static int REPORT_INVENTURSTAND_STUECKLISTE = 14;

	private static int REPORT_INVENTURPROTOKOLL_ARTIKELNUMMER = 0;
	private static int REPORT_INVENTURPROTOKOLL_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_INVENTURPROTOKOLL_KORREKTURZEITPUNKT = 2;
	private static int REPORT_INVENTURPROTOKOLL_KORREKTURMENGE = 3;
	private static int REPORT_INVENTURPROTOKOLL_INVENTURPREIS = 4;
	private static int REPORT_INVENTURPROTOKOLL_INVENTURMENGE = 5;
	private static int REPORT_INVENTURPROTOKOLL_ARTIKELART = 6;
	private static int REPORT_INVENTURPROTOKOLL_LAGERSTAND = 7;

	private static int REPORT_NICHTERFASSTEARTIKEL_ARTIKELNUMMER = 0;
	private static int REPORT_NICHTERFASSTEARTIKEL_ARTIKELBEZEICHNUNG = 1;
	private static int REPORT_NICHTERFASSTEARTIKEL_SERIENNUMMERCHARGENNUMMER = 2;
	private static int REPORT_NICHTERFASSTEARTIKEL_LAGER = 3;
	private static int REPORT_NICHTERFASSTEARTIKEL_LAGERSTAND = 4;

	public Integer createInventur(InventurDto inventurDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (inventurDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurDto == null"));
		}
		if (inventurDto.getTInventurdatum() == null
				|| inventurDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurDto.getTInventurdatum() == null || inventurDto.getCBez() == null"));
		}
		inventurDto.setMandantCNr(theClientDto.getMandant());
		inventurDto.setBInventurdurchgefuehrt(Helper.boolean2Short(false));
		inventurDto.setBAbwertungdurchgefuehrt(Helper.boolean2Short(false));
		try {

			// PJ 17901
			Query query = em
					.createNamedQuery("InventurfindByTInventurdatumMandantCNr");
			query.setParameter(1, inventurDto.getTInventurdatum());
			query.setParameter(2, inventurDto.getMandantCNr());

			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Inventur inventur = (Inventur) it.next();

				if (inventur.getLagerIId() == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_INVENTUR.UC"));
				} else {
					if (inventurDto.getLagerIId() != null) {
						if (inventurDto.getLagerIId().equals(
								inventur.getLagerIId())) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
									new Exception("WW_INVENTUR.UC"));
						}
					}
				}

			}

		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INVENTUR);
			inventurDto.setIId(pk);

			inventurDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			inventurDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			Inventur inventur = new Inventur(inventurDto.getIId(),
					inventurDto.getTInventurdatum(),
					inventurDto.getBInventurdurchgefuehrt(),
					inventurDto.getBAbwertungdurchgefuehrt(),
					inventurDto.getCBez(), inventurDto.getPersonalIIdAendern(),
					inventurDto.getMandantCNr());
			em.persist(inventur);
			em.flush();
			setInventurFromInventurDto(inventur, inventurDto);
			return inventurDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeInventur(InventurDto inventurDto) throws EJBExceptionLP {
		myLogger.entry();
		if (inventurDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurDto == null"));
		}
		if (inventurDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("inventurDto.getIId() == null"));
		}
		// try {
		Inventur toRemove = em.find(Inventur.class, inventurDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeInventur. Es gibt keine iid "
							+ inventurDto.getIId() + "\ndto.toString: "
							+ inventurDto.toString());
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

	public void updateInventur(InventurDto inventurDto,
			TheClientDto theClientDto) {
		myLogger.entry();
		if (inventurDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurDto == null"));
		}
		if (inventurDto.getIId() == null
				|| inventurDto.getTInventurdatum() == null
				|| inventurDto.getCBez() == null
				|| inventurDto.getMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurDto.getIId() == null || inventurDto.getTInventurdatum() == null || inventurDto.getCBez() == null || inventurDto.getMandantCNr() == null"));
		}
		Integer iId = inventurDto.getIId();
		Inventur inventur = null;
		// try {
		inventur = em.find(Inventur.class, iId);
		if (inventur == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateInventur. Es gibt keine iid "
							+ inventurDto.getIId() + "\ndto.toString: "
							+ inventurDto.toString());
		}

		try {

			Integer iIdVorhanden = inventur.getIId();

			Query query = em
					.createNamedQuery("InventurfindByTInventurdatumMandantCNr");
			query.setParameter(1, inventurDto.getTInventurdatum());
			query.setParameter(2, inventurDto.getMandantCNr());

			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {

				Inventur inventurVorhanden = (Inventur) it.next();

				if (!inventurVorhanden.getIId().equals(inventurDto.getIId())) {

					if (inventurVorhanden.getLagerIId() == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
								new Exception("WW_INVENTUR.UC"));
					} else {
						if (inventurDto.getLagerIId() != null) {
							if (inventurDto.getLagerIId().equals(
									inventurVorhanden.getLagerIId())) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
										new Exception("WW_INVENTUR.UC"));
							}
						}
					}
				}
			}

		} catch (NoResultException ex) {
			// nix da
		}

		inventurDto.setLagerIId(inventur.getLagerIId());
		// Wenn sich das Inventurdatum aendert, dann Belegdatum im Lager updaten
		try {
			if (!inventur.getTInventurdatum().equals(
					inventurDto.getTInventurdatum())) {
				getLagerFac().updateTBelegdatumEinesBelegesImLager(
						LocaleFac.BELEGART_INVENTUR, inventurDto.getIId(),
						inventurDto.getTInventurdatum(), theClientDto);
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		inventurDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		inventurDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		setInventurFromInventurDto(inventur, inventurDto);

	}

	public InventurDto inventurFindByPrimaryKey(Integer iId,
			TheClientDto theClientDtoI) throws EJBExceptionLP {
		return inventurFindByPrimaryKey(iId);
	}

	public InventurDto inventurFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");

		InventurDto inventurDto = findByPrimaryKeyOhneExc(iId);
		if (inventurDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei inventurFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return inventurDto;
	}

	public InventurDto findByPrimaryKeyOhneExc(Integer iId) {
		if (iId == null)
			return null;

		Inventur inventur = em.find(Inventur.class, iId);
		if (inventur == null)
			return null;

		return assembleInventurDto(inventur);
	}

	public InventurDto findByTInventurdatumMandantCNr(Timestamp tInventurdatum,
			String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("InventurfindByTInventurdatumMandantCNr");
		query.setParameter(1, tInventurdatum);
		query.setParameter(2, mandantCNr);
		Inventur inventur = (Inventur) query.getSingleResult();
		if (inventur == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei findByTInventurdatumMandantCNr. Es gibt keinen Eintrag mit inventurdatum "
							+ tInventurdatum
							+ " f\u00FCr dem mandant "
							+ mandantCNr);
		}
		return assembleInventurDto(inventur);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public InventurDto[] inventurFindDurchgefuehrteInventurenEinesZeitraums(
			Timestamp tVon, Timestamp tBis, String mandantCNr)
			throws EJBExceptionLP {
		if (tVon == null || tBis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tVon == null || tBis == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("InventurfindDurchgefuehrteInventurenEinesZeitraums");
		query.setParameter(1, Helper.cutTimestamp(tVon));
		query.setParameter(2, Helper.cutTimestamp(tBis));
		query.setParameter(3, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }

		return assembleInventurDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public InventurDto[] inventurFindInventurenNachDatum(
			Timestamp tInventurdatum, String mandantCNr) throws EJBExceptionLP {
		if (tInventurdatum == null || mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("tVon == null || tBis == null"));
		}

		// try {
		Query query = em.createNamedQuery("InventurfindInventurenNachDatum");
		query.setParameter(1, Helper.cutTimestamp(tInventurdatum));
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }

		return assembleInventurDtos(cl);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public InventurDto[] inventurFindOffene(String mandantCNr)
			throws EJBExceptionLP {
		Validator.notEmpty(mandantCNr, "mandantCNr");
		return assembleInventurDtos(InventurQuery.listByMandantOffene(em,
				mandantCNr));
	}

	private void setInventurFromInventurDto(Inventur inventur,
			InventurDto inventurDto) {
		inventur.setTInventurdatum(inventurDto.getTInventurdatum());
		inventur.setCBez(inventurDto.getCBez());
		inventur.setTAendern(inventurDto.getTAendern());
		inventur.setPersonalIIdAendern(inventurDto.getPersonalIIdAendern());
		inventur.setPersonalIIdAbwertungdurchgefuehrt(inventurDto
				.getPersonalIIdAendern());
		inventur.setPersonalIIdInventurdurchgefuehrt(inventurDto
				.getPersonalIIdAendern());
		inventur.setBAbwertungdurchgefuehrt(inventurDto
				.getBAbwertungdurchgefuehrt());
		inventur.setBInventurdurchgefuehrt(inventurDto
				.getBInventurdurchgefuehrt());
		inventur.setTAbwertungdurchgefuehrt(inventurDto
				.getTAbwertungdurchgefuehrt());
		inventur.setTInventurdurchgefuehrt(inventurDto
				.getTInventurdurchgefuehrt());
		inventur.setMandantCNr(inventurDto.getMandantCNr());
		inventur.setLagerIId(inventurDto.getLagerIId());
		em.merge(inventur);
		em.flush();
	}

	private InventurDto assembleInventurDto(Inventur inventur) {
		return InventurDtoAssembler.createDto(inventur);
	}

	private InventurDto[] assembleInventurDtos(Collection<?> inventurs) {
		List<InventurDto> list = new ArrayList<InventurDto>();
		if (inventurs != null) {
			Iterator<?> iterator = inventurs.iterator();
			while (iterator.hasNext()) {
				Inventur inventur = (Inventur) iterator.next();
				list.add(assembleInventurDto(inventur));
			}
		}
		InventurDto[] returnArray = new InventurDto[list.size()];
		return (InventurDto[]) list.toArray(returnArray);
	}

	public ArrayList<String> sindSeriennumernBereitsInventiert(
			InventurlisteDto inventurlisteDto, String[] snrs,
			TheClientDto theClientDto) {
		ArrayList<String> al = new ArrayList<String>();

		if (snrs != null) {
			for (int i = 0; i < snrs.length; i++) {
				try {
					Query query = em
							.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
					query.setParameter(1, inventurlisteDto.getInventurIId());
					query.setParameter(2, inventurlisteDto.getLagerIId());
					query.setParameter(3, inventurlisteDto.getArtikelIId());
					query.setParameter(4, snrs[i]);
					Inventurliste inventurliste = (Inventurliste) query
							.getSingleResult();

					// bereits inventiert
					al.add(snrs[i]);

				} catch (NoResultException e) {
					// nicht inventiert
				}
			}
		}

		return al;
	}

	public void mehrereSeriennumernInventieren(
			InventurlisteDto inventurlisteDto, String[] snrs,
			TheClientDto theClientDto) {

		if (snrs != null) {
			for (int i = 0; i < snrs.length; i++) {

				try {
					Query query = em
							.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
					query.setParameter(1, inventurlisteDto.getInventurIId());
					query.setParameter(2, inventurlisteDto.getLagerIId());
					query.setParameter(3, inventurlisteDto.getArtikelIId());
					query.setParameter(4, snrs[i]);
					Inventurliste inventurliste = (Inventurliste) query
							.getSingleResult();
					// Gibts schon, auslassen

				} catch (NoResultException e) {

					inventurlisteDto.setCSeriennrchargennr(snrs[i]);

					createInventurliste(inventurlisteDto, false, theClientDto);
				}

			}
		}

	}

	public void inventurlisteErfassenMitScanner(
			InventurlisteDto inventurlisteDto, boolean bKorrekturbuchung,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (inventurlisteDto.getCSeriennrchargennr() != null) {

			Inventurliste inventurliste;
			try {
				Query query = em
						.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
				query.setParameter(1, inventurlisteDto.getInventurIId());
				query.setParameter(2, inventurlisteDto.getLagerIId());
				query.setParameter(3, inventurlisteDto.getArtikelIId());
				query.setParameter(4, inventurlisteDto.getCSeriennrchargennr());
				inventurliste = (Inventurliste) query.getSingleResult();
				inventurlisteDto.setIId(inventurliste.getIId());

				if (bKorrekturbuchung == false) {
					inventurlisteDto.setNInventurmenge(inventurliste
							.getNInventurmenge().add(
									inventurlisteDto.getNInventurmenge()));
				}
			} catch (NoResultException e) {
				createInventurliste(inventurlisteDto, bPruefeAufZuGrosseMenge,
						theClientDto);
			}

		} else {
			Query query = em
					.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
			query.setParameter(1, inventurlisteDto.getInventurIId());
			query.setParameter(2, inventurlisteDto.getLagerIId());
			query.setParameter(3, inventurlisteDto.getArtikelIId());
			InventurlisteDto[] dtos = assembleInventurlisteDtos(query
					.getResultList());

			if (dtos != null && dtos.length > 0) {
				inventurlisteDto.setIId(dtos[0].getIId());
				if (bKorrekturbuchung == false) {
					inventurlisteDto.setNInventurmenge(dtos[0]
							.getNInventurmenge().add(
									inventurlisteDto.getNInventurmenge()));
				}

			} else {
				createInventurliste(inventurlisteDto, bPruefeAufZuGrosseMenge,
						theClientDto);
				return;
			}
		}

		try {
			LockMeDto[] dtos = getTheJudgeFac().findByWerWas("lockme_inventur",
					inventurlisteDto.getInventurIId() + "");

			if (dtos != null && dtos.length > 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, new Exception(
								"FEHLER_IS_ALREADY_LOCKED"));

			} else {
				updateInventurliste(inventurlisteDto, bPruefeAufZuGrosseMenge,
						theClientDto);
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

	}

	public void inventurlisteFuerSerienChargennummerAbschliessen(
			Integer inventurIId, Integer artikelIId, Integer lagerIId,
			TheClientDto theClientDto) {

		SeriennrChargennrAufLagerDto[] dtos = null;
		try {
			dtos = getLagerFac().getAllSerienChargennrAufLagerInfoDtos(
					artikelIId, lagerIId, false, null, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		for (int i = 0; i < dtos.length; i++) {
			try {
				Query query = em
						.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
				query.setParameter(1, inventurIId);
				query.setParameter(2, lagerIId);
				query.setParameter(3, artikelIId);
				query.setParameter(4, dtos[i].getCSeriennrChargennr());
				Inventurliste inventurliste = (Inventurliste) query
						.getSingleResult();
				if (inventurliste == null) {
					InventurlisteDto inventurlisteDto = new InventurlisteDto();
					inventurlisteDto.setArtikelIId(artikelIId);
					inventurlisteDto.setLagerIId(lagerIId);
					inventurlisteDto.setInventurIId(inventurIId);
					inventurlisteDto.setCSeriennrchargennr(dtos[i]
							.getCSeriennrChargennr());
					inventurlisteDto.setNInventurmenge(new BigDecimal(0));
					createInventurliste(inventurlisteDto, false, theClientDto);
				}
			} catch (NoResultException ex1) {
				InventurlisteDto inventurlisteDto = new InventurlisteDto();
				inventurlisteDto.setArtikelIId(artikelIId);
				inventurlisteDto.setLagerIId(lagerIId);
				inventurlisteDto.setInventurIId(inventurIId);
				inventurlisteDto.setCSeriennrchargennr(dtos[i]
						.getCSeriennrChargennr());
				inventurlisteDto.setNInventurmenge(new BigDecimal(0));
				createInventurliste(inventurlisteDto, false, theClientDto);
			}
		}
	}

	public Integer createInventurliste(InventurlisteDto inventurlisteDto,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (inventurlisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurlisteDto == null"));
		}
		if (inventurlisteDto.getInventurIId() == null
				|| inventurlisteDto.getLagerIId() == null
				|| inventurlisteDto.getArtikelIId() == null
				|| inventurlisteDto.getNInventurmenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurlisteDto.getInventurIId() == null || inventurlisteDto.getLagerIId() == null || inventurlisteDto.getArtikelIId() == null || inventurlisteDto.getNInventurmenge() == null"));
		}

		try {
			if (inventurlisteDto.getCSeriennrchargennr() != null) {
				Query query = em
						.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
				query.setParameter(1, inventurlisteDto.getInventurIId());
				query.setParameter(2, inventurlisteDto.getLagerIId());
				query.setParameter(3, inventurlisteDto.getArtikelIId());
				query.setParameter(4, inventurlisteDto.getCSeriennrchargennr());
				Inventurliste doppelt = (Inventurliste) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_INVENTURLISTE.UC"));
				}
			} else {
				Query query1 = em
						.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
				query1.setParameter(1, inventurlisteDto.getInventurIId());
				query1.setParameter(2, inventurlisteDto.getLagerIId());
				query1.setParameter(3, inventurlisteDto.getArtikelIId());
				Collection<?> c = query1.getResultList();
				if (c.size() > 0) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("WW_INVENTURLISTE.UC"));
				}
			}
		} catch (NoResultException ex) {
			// NIX
		}

		// Wenn ein Lager in der Inventur angegeben ist, dann darf nur dieses
		// inventiert werden.
		InventurDto inventurDto = inventurFindByPrimaryKey(inventurlisteDto
				.getInventurIId());
		if (inventurDto.getLagerIId() != null
				&& !inventurDto.getLagerIId().equals(
						inventurlisteDto.getLagerIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN,
					new Exception(
							"FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN"));

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INVENTURLISTE);
			inventurlisteDto.setIId(pk);

			inventurlisteDto
					.setPersonalIIdAendern(theClientDto.getIDPersonal());
			inventurlisteDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Inventurliste inventurliste = new Inventurliste(
					inventurlisteDto.getIId(),
					inventurlisteDto.getInventurIId(),
					inventurlisteDto.getLagerIId(),
					inventurlisteDto.getArtikelIId(),
					inventurlisteDto.getNInventurmenge(),
					inventurlisteDto.getPersonalIIdAendern());
			em.persist(inventurliste);
			em.flush();
			setInventurlisteFromInventurlisteDto(inventurliste,
					inventurlisteDto);

			// Korrekturbuchung durchfuehren + ins Protokoll schreiben

			inventurlisteDto.setArtikelDto(getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							inventurlisteDto.getArtikelIId(), theClientDto));

			if (Helper.short2boolean(inventurlisteDto.getArtikelDto()
					.getBSeriennrtragend())) {
				if (inventurlisteDto.getNInventurmenge().doubleValue() == 0
						|| inventurlisteDto.getNInventurmenge().doubleValue() == 1) {
				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
							new Exception());
				}
			}

			java.math.BigDecimal lagerstand = new java.math.BigDecimal(0);

			if (Helper.short2boolean(inventurlisteDto.getArtikelDto()
					.getBChargennrtragend())
					|| Helper.short2boolean(inventurlisteDto.getArtikelDto()
							.getBSeriennrtragend())) {
				lagerstand = getLagerFac().getMengeAufLager(
						inventurlisteDto.getArtikelIId(),
						inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(), theClientDto);
			} else {
				lagerstand = getLagerFac().getLagerstandOhneExc(
						inventurlisteDto.getArtikelIId(),
						inventurlisteDto.getLagerIId(), theClientDto);
			}

			java.math.BigDecimal diff = inventurlisteDto.getNInventurmenge()
					.subtract(lagerstand);

			InventurprotokollDto inventurprotokollDto = new InventurprotokollDto();
			inventurprotokollDto.setInventurIId(inventurlisteDto
					.getInventurIId());
			inventurprotokollDto.setInventurlisteIId(inventurlisteDto.getIId());
			inventurprotokollDto.setNKorrekturmenge(diff);

			if (bPruefeAufZuGrosseMenge == true) {
				ParametermandantDto parametermandantautoDebitDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_ERLAUBTE_ABWEICHUNG_INVENTURLISTE);
				int i = (Integer) parametermandantautoDebitDto
						.getCWertAsObject();

				if (diff.abs().doubleValue() > i) {

					ArrayList alInfo = new ArrayList();
					alInfo.add(i);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_INVENTUR_MENGE_ZU_GROSS,
							alInfo, new Exception(
									"FEHLER_INVENTUR_MENGE_ZU_GROSS"));
				}

			}

			java.math.BigDecimal gestpreis = getLagerFac()
					.getGemittelterGestehungspreisEinesLagers(
							inventurlisteDto.getArtikelIId(),
							inventurlisteDto.getLagerIId(), theClientDto);

			inventurprotokollDto.setNInventurpreis(gestpreis);
			inventurprotokollDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			inventurprotokollDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			inventurprotokollDto.setTZeitpunkt(new java.sql.Timestamp(System
					.currentTimeMillis()));

			if (diff.doubleValue() < 0) {
				getLagerFac().bucheAb(LocaleFac.BELEGART_INVENTUR,
						inventurlisteDto.getInventurIId(),
						inventurlisteDto.getIId(),
						inventurlisteDto.getArtikelIId(), diff.abs(),
						gestpreis, inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto);
				createInventurprotokoll(inventurprotokollDto, theClientDto);

			} else if (diff.doubleValue() > 0) {
				getLagerFac().bucheZu(LocaleFac.BELEGART_INVENTUR,
						inventurlisteDto.getInventurIId(),
						inventurlisteDto.getIId(),
						inventurlisteDto.getArtikelIId(), diff, gestpreis,
						inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto, null, true);
				createInventurprotokoll(inventurprotokollDto, theClientDto);

			}

			return inventurlisteDto.getIId();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public void removeInventurliste(InventurlisteDto inventurlisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		if (inventurlisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurlisteDto == null"));
		}
		if (inventurlisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("inventurlisteDto.getIId() == null"));
		}

		// try {
		Inventurliste toRemove = em.find(Inventurliste.class,
				inventurlisteDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeInventurliste. Es gibt kein iid "
							+ inventurlisteDto.getIId() + "\ndto.toString(): "
							+ inventurlisteDto.toString());
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

	public void removeInventurprotokoll(Integer inventurprotokollIId) {

		// try {
		Inventurprotokoll toRemove = em.find(Inventurprotokoll.class,
				inventurprotokollIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Integer updateInventurliste(InventurlisteDto inventurlisteDto,
			boolean bPruefeAufZuGrosseMenge, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();
		if (inventurlisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurlisteDto == null"));
		}
		if (inventurlisteDto.getIId() == null
				|| inventurlisteDto.getInventurIId() == null
				|| inventurlisteDto.getLagerIId() == null
				|| inventurlisteDto.getArtikelIId() == null
				|| inventurlisteDto.getNInventurmenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurlisteDto.getIId() == null || inventurlisteDto.getInventurIId() == null || inventurlisteDto.getLagerIId() == null || inventurlisteDto.getArtikelIId() == null || inventurlisteDto.getNInventurmenge() == null || inventurlisteDto.getNInventurpreis() == null"));
		}
		Integer iId = inventurlisteDto.getIId();
		Inventurliste inventurliste = null;
		BigDecimal differenzInventurmenge = new BigDecimal(0);
		// try {
		inventurliste = em.find(Inventurliste.class, iId);
		if (inventurliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateInventurliste. Es gint keine iid " + iId
							+ "\ndto.toString(): "
							+ inventurlisteDto.toString());
		}

		java.math.BigDecimal lagerstand = new java.math.BigDecimal(0);
		inventurlisteDto.setArtikelDto(getArtikelFac()
				.artikelFindByPrimaryKeySmall(inventurlisteDto.getArtikelIId(),
						theClientDto));
		try {
			if (Helper.short2boolean(inventurlisteDto.getArtikelDto()
					.getBChargennrtragend())
					|| Helper.short2boolean(inventurlisteDto.getArtikelDto()
							.getBSeriennrtragend())) {
				lagerstand = getLagerFac().getMengeAufLager(
						inventurlisteDto.getArtikelIId(),
						inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(), theClientDto);
			} else {
				lagerstand = getLagerFac().getLagerstandOhneExc(
						inventurlisteDto.getArtikelIId(),
						inventurlisteDto.getLagerIId(), theClientDto);
			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		differenzInventurmenge = inventurlisteDto.getNInventurmenge().subtract(
				lagerstand);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		// Wenn ein Lager in der Inventur angegeben ist, dann darf nur dieses
		// inventiert werden.
		InventurDto inventurDto = inventurFindByPrimaryKey(
				inventurlisteDto.getInventurIId(), theClientDto);
		if (inventurDto.getLagerIId() != null
				&& !inventurDto.getLagerIId().equals(
						inventurlisteDto.getLagerIId())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN,
					new Exception(
							"FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN"));

		}

		Integer iId_Alt = inventurlisteDto.getIId();
		// Neuen eintrag anlegen
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INVENTURLISTE);
		inventurlisteDto.setIId(pk);

		inventurlisteDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		inventurlisteDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		try {
			Inventurliste inventurlisteNeueintrag = new Inventurliste(
					inventurlisteDto.getIId(),
					inventurlisteDto.getInventurIId(),
					inventurlisteDto.getLagerIId(),
					inventurlisteDto.getArtikelIId(),
					inventurlisteDto.getNInventurmenge(),
					inventurlisteDto.getPersonalIIdAendern());
			em.persist(inventurliste);
			em.flush();
			setInventurlisteFromInventurlisteDto(inventurlisteNeueintrag,
					inventurlisteDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		// Protokoll an den neuen Eintrag anhaengen
		// try {
		Query query = em
				.createNamedQuery("InventurprotokollfindByInventurlisteIId");
		query.setParameter(1, iId_Alt);
		Collection<?> inventurprotokolls = query.getResultList();
		// if (inventurprotokolls.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		//
		// }

		Iterator<?> iterator = inventurprotokolls.iterator();
		while (iterator.hasNext()) {
			Inventurprotokoll inventurprotokoll = (Inventurprotokoll) iterator
					.next();
			inventurprotokoll.setInventurlisteIId(inventurlisteDto.getIId());
		}
		// }
		// catch (FinderException ex2) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex2);

		// }

		// Alten Eintrag loechen
		try {
			em.remove(inventurliste);
			em.flush();
		} catch (EntityExistsException ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex3);
		}

		try {
			// Korrekturbuchung durchfuehren + ins Protokoll schreiben

			inventurlisteDto.setArtikelDto(getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							inventurlisteDto.getArtikelIId(), theClientDto));

			if (Helper.short2boolean(inventurlisteDto.getArtikelDto()
					.getBSeriennrtragend())) {
				if (inventurlisteDto.getNInventurmenge().doubleValue() == 0
						|| inventurlisteDto.getNInventurmenge().doubleValue() == 1) {
				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
							new Exception());
				}
			}

			InventurprotokollDto inventurprotokollDto = new InventurprotokollDto();
			inventurprotokollDto.setInventurIId(inventurlisteDto
					.getInventurIId());
			inventurprotokollDto.setInventurlisteIId(inventurlisteDto.getIId());
			inventurprotokollDto.setNKorrekturmenge(differenzInventurmenge);

			java.math.BigDecimal gestpreis = getLagerFac()
					.getGemittelterGestehungspreisEinesLagers(
							inventurlisteDto.getArtikelIId(),
							inventurlisteDto.getLagerIId(), theClientDto);

			inventurprotokollDto.setNInventurpreis(gestpreis);

			inventurprotokollDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			inventurprotokollDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));
			inventurprotokollDto.setTZeitpunkt(new java.sql.Timestamp(System
					.currentTimeMillis()));

			if (bPruefeAufZuGrosseMenge == true) {
				ParametermandantDto parametermandantautoDebitDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_ERLAUBTE_ABWEICHUNG_INVENTURLISTE);
				int i = (Integer) parametermandantautoDebitDto
						.getCWertAsObject();

				if (differenzInventurmenge.abs().doubleValue() > i) {

					ArrayList alInfo = new ArrayList();
					alInfo.add(i);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_INVENTUR_MENGE_ZU_GROSS,
							alInfo, new Exception(
									"FEHLER_INVENTUR_MENGE_ZU_GROSS"));
				}

			}

			if (differenzInventurmenge.doubleValue() < 0) {
				getLagerFac().bucheAb(LocaleFac.BELEGART_INVENTUR,
						inventurlisteDto.getInventurIId(),
						inventurlisteDto.getIId(),
						inventurlisteDto.getArtikelIId(),
						differenzInventurmenge.abs(), gestpreis,
						inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto);
				createInventurprotokoll(inventurprotokollDto, theClientDto);

			} else if (differenzInventurmenge.doubleValue() > 0) {
				getLagerFac().bucheZu(LocaleFac.BELEGART_INVENTUR,
						inventurlisteDto.getInventurIId(),
						inventurlisteDto.getIId(),
						inventurlisteDto.getArtikelIId(),
						differenzInventurmenge, gestpreis,
						inventurlisteDto.getLagerIId(),
						inventurlisteDto.getCSeriennrchargennr(),
						new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto, null, true);
				createInventurprotokoll(inventurprotokollDto, theClientDto);

			}

		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}
		return inventurlisteDto.getIId();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printInventurprotokoll(Integer inventurIId,
			Integer lagerIId, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = InventurFac.REPORT_INVENTURPROTOKOLL;

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String subQueryLager = "(SELECT sum(al.n_lagerstand) FROM FLRArtikellager al WHERE compId.artikel_i_id=inventurprotokoll.flrinventurliste.flrartikel.i_id ";

		if (lagerIId != null) {
			subQueryLager += " AND compId.lager_i_id=" + lagerIId;
		}

		subQueryLager += " ) as lagerstand ";

		String sQuery = "SELECT inventurprotokoll,(SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=inventurprotokoll.flrinventurliste.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' ),"
				+ subQueryLager
				+ " from FLRInventurprotokoll inventurprotokoll WHERE inventurprotokoll.flrinventurliste.flrlager.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inventurprotokoll.flrinventurliste.flrinventur.i_id="
				+ inventurIId;

		if (lagerIId != null) {
			sQuery = sQuery
					+ " AND inventurprotokoll.flrinventurliste.flrlager.i_id="
					+ lagerIId;
		}
		sQuery += " ORDER BY inventurprotokoll.flrinventurliste.flrartikel.c_nr ASC";
		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		data = new Object[resultList.size()][8];
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRInventurprotokoll flrinventurprotokoll = (FLRInventurprotokoll) o[0];

			data[row][REPORT_INVENTURPROTOKOLL_ARTIKELNUMMER] = flrinventurprotokoll
					.getFlrinventurliste().getFlrartikel().getC_nr();
			data[row][REPORT_INVENTURPROTOKOLL_ARTIKELBEZEICHNUNG] = o[1];
			data[row][REPORT_INVENTURPROTOKOLL_LAGERSTAND] = o[2];
			data[row][REPORT_INVENTURPROTOKOLL_KORREKTURMENGE] = flrinventurprotokoll
					.getN_korrekturmenge();
			data[row][REPORT_INVENTURPROTOKOLL_INVENTURPREIS] = flrinventurprotokoll
					.getN_inventurpreis();
			data[row][REPORT_INVENTURPROTOKOLL_INVENTURMENGE] = flrinventurprotokoll
					.getFlrinventurliste().getN_inventurmenge();
			data[row][REPORT_INVENTURPROTOKOLL_KORREKTURZEITPUNKT] = flrinventurprotokoll
					.getT_zeitpunkt();
			data[row][REPORT_INVENTURPROTOKOLL_ARTIKELART] = flrinventurprotokoll
					.getFlrinventurliste().getFlrartikel().getArtikelart_c_nr();

			row++;
		}

		session.close();
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = InventurFac.REPORT_INVENTURPROTOKOLL;
		try {
			if (lagerIId != null) {
				LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", dto.getCNr());
			} else {
				parameter.put("P_LAGER", "ALLE");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		try {
			parameter.put(
					"P_WAEHRUNG",
					getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto)
							.getWaehrungCNr());
		} catch (RemoteException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		parameter.put("P_INVENTUR", inventurDto.getCBez());
		parameter.put("P_INVENTURDATUM", inventurDto.getTInventurdatum());
		initJRDS(parameter, InventurFac.REPORT_MODUL,
				InventurFac.REPORT_INVENTURPROTOKOLL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	/**
	 * Erstellt eine Inventurstand einer best. Inventur
	 * 
	 * @param inventurIId
	 *            Inventur-ID
	 * @param lagerIId
	 *            Lager-ID
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printInventurstand(Integer inventurIId,
			Integer lagerIId, int iSortierung, TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = InventurFac.REPORT_INVENTURSTAND;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		session.enableFilter("filterMandant").setParameter("paramMandant",
				theClientDto.getMandant());
		session.enableFilter("filterLocale").setParameter("paramLocale",
				Helper.locale2String(theClientDto.getLocUi()));

		String subQueryLager = "(SELECT SUM(al.n_lagerstand) FROM FLRArtikellager al WHERE compId.artikel_i_id=inventurstand.flrartikel.i_id ";

		if (lagerIId != null) {
			subQueryLager += " AND compId.lager_i_id=" + lagerIId;
		}

		subQueryLager += " ) as lagerstand ";

		String sQuery = "SELECT inventurstand, aspr.c_bez , "
				+ subQueryLager
				+ ",aspr.c_kbez,aspr.c_zbez,aspr.c_zbez2,(SELECT stkl FROM FLRStueckliste stkl WHERE stkl.artikel_i_id=inventurstand.flrartikel.i_id)  from FLRInventurstand as inventurstand LEFT OUTER JOIN inventurstand.flrartikel.artikelsprset AS aspr LEFT OUTER JOIN inventurstand.flrartikel.flrartikelgruppe as ag LEFT OUTER JOIN inventurstand.flrartikel.flrartikelklasse as ak WHERE inventurstand.flrlager.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inventurstand.flrinventur.i_id=" + inventurIId;

		if (lagerIId != null) {
			sQuery = sQuery + " AND inventurstand.flrlager.i_id=" + lagerIId;
		}

		if (iSortierung == InventurFac.REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELNR) {
			sQuery = sQuery
					+ "ORDER BY inventurstand.flrlager.i_id , inventurstand.flrartikel.c_nr";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("artikel.artikelnummerlang",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else if (iSortierung == InventurFac.REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELGRUPPE) {
			sQuery = sQuery
					+ "ORDER BY inventurstand.flrlager.i_id , ag.c_nr , inventurstand.flrartikel.c_nr";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.artikelgruppe",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else if (iSortierung == InventurFac.REPORT_INVENTURSTAND_SORTIERUNG_ARTIKELKLASSE) {
			sQuery = sQuery
					+ "ORDER BY inventurstand.flrlager.i_id , ak.c_nr , inventurstand.flrartikel.c_nr";
			parameter
					.put("P_SORTIERUNG",
							getTextRespectUISpr("lp.artikelklasse",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));

		}

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		String letzterArtikel = null;
		Object[][] dataHelp = new Object[resultList.size()][15];

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			FLRInventurstand flrinventurstand = (FLRInventurstand) o[0];

			if ((!flrinventurstand.getFlrartikel().getC_nr()
					.equals(letzterArtikel))) {

				if (Helper.short2boolean(flrinventurstand.getFlrartikel()
						.getB_chargennrtragend()) == true
						|| Helper.short2boolean(flrinventurstand
								.getFlrartikel().getB_seriennrtragend()) == true) {

					Query query = em
							.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
					query.setParameter(1, inventurIId);
					query.setParameter(2, flrinventurstand.getFlrlager()
							.getI_id());
					query.setParameter(3, flrinventurstand.getFlrartikel()
							.getI_id());
					Collection<?> cl = query.getResultList();

					InventurlisteDto[] inventurlisteDtos = assembleInventurlisteDtos(cl);

					BigDecimal menge = new BigDecimal(0);
					String snrs = "";
					for (int i = 0; i < inventurlisteDtos.length; i++) {
						menge = menge.add(inventurlisteDtos[i]
								.getNInventurmenge());

						if (Helper.short2boolean(flrinventurstand
								.getFlrartikel().getB_chargennrtragend()) == true) {

							snrs = snrs
									+ inventurlisteDtos[i].getNInventurmenge()
									+ " "
									+ flrinventurstand.getFlrartikel()
											.getEinheit_c_nr()
									+ " "
									+ inventurlisteDtos[i]
											.getCSeriennrchargennr() + ", ";
						} else if (Helper.short2boolean(flrinventurstand
								.getFlrartikel().getB_seriennrtragend()) == true) {
							snrs = snrs
									+ inventurlisteDtos[i]
											.getCSeriennrchargennr() + ", ";
						}

						// }

						// }
						// catch (FinderException ex1) {
						// dataHelp[row][REPORT_INVENTURSTAND_INVENTURMENGE] =
						// new BigDecimal(0);
						// }
					}

					dataHelp[row][REPORT_INVENTURSTAND_SERIENNUMMERCHARGENNUMMER] = snrs;

				}

				dataHelp[row][REPORT_INVENTURSTAND_INVENTURMENGE] = flrinventurstand
						.getN_inventurmenge();

				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELNUMMER] = flrinventurstand
						.getFlrartikel().getC_nr();
				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELBEZEICHNUNG] = o[1];
				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELKURZBEZEICHNUNG] = o[3];
				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG] = o[4];
				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG2] = o[5];

				if (flrinventurstand.getFlrartikel().getFlrartikelklasse() != null) {
					dataHelp[row][REPORT_INVENTURSTAND_ARTIKELKLASSE] = flrinventurstand
							.getFlrartikel().getFlrartikelklasse().getC_nr();

				}
				if (flrinventurstand.getFlrartikel().getFlrartikelgruppe() != null) {
					dataHelp[row][REPORT_INVENTURSTAND_ARTIKELGRUPPE] = flrinventurstand
							.getFlrartikel().getFlrartikelgruppe().getC_nr();

				}

				dataHelp[row][REPORT_INVENTURSTAND_LAGER] = flrinventurstand
						.getFlrlager().getC_nr();

				dataHelp[row][REPORT_INVENTURSTAND_LAGERSTAND] = o[2];

				if (o[6] == null) {
					dataHelp[row][REPORT_INVENTURSTAND_STUECKLISTE] = new Boolean(
							false);
				} else {
					dataHelp[row][REPORT_INVENTURSTAND_STUECKLISTE] = new Boolean(
							true);
				}

				dataHelp[row][REPORT_INVENTURSTAND_ARTIKELART] = flrinventurstand
						.getFlrartikel().getArtikelart_c_nr();

				if (flrinventurstand.getN_inventurpreis() != null) {
					dataHelp[row][REPORT_INVENTURSTAND_INVENTURPREIS] = flrinventurstand
							.getN_inventurpreis();
					dataHelp[row][REPORT_INVENTURSTAND_INVENTURWERT] = ((BigDecimal) dataHelp[row][REPORT_INVENTURSTAND_INVENTURMENGE])
							.multiply(flrinventurstand.getN_inventurpreis());

				} else {
					dataHelp[row][REPORT_INVENTURSTAND_INVENTURWERT] = new java.math.BigDecimal(
							0);
					dataHelp[row][REPORT_INVENTURSTAND_INVENTURPREIS] = new java.math.BigDecimal(
							0);

				}
				row++;
			}
			letzterArtikel = flrinventurstand.getFlrartikel().getC_nr();

		}

		session.close();
		data = new Object[row][7];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}
		index = -1;
		sAktuellerReport = InventurFac.REPORT_INVENTURSTAND;
		parameter.put("P_INVENTUR",
				inventurFindByPrimaryKey(inventurIId, theClientDto).getCBez());
		try {
			if (lagerIId != null) {
				LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", dto.getCNr());
			} else {
				parameter.put("P_LAGER", "ALLE");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		try {
			parameter.put(
					"P_WAEHRUNG",
					getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto)
							.getWaehrungCNr());
		} catch (RemoteException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}

		initJRDS(parameter, InventurFac.REPORT_MODUL,
				InventurFac.REPORT_INVENTURSTAND, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		// lpReport.saveHTML("c:/anwesenheitsliste.html");
		return getReportPrint();
	}

	public InventurstandDto inventurstandfindByInventurIIdArtikelIIdLagerIId(
			Integer inventurIId, Integer lagerIId, Integer artikelIId,
			TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("InventurstandfindByInventurIIdArtikelIIdLagerIId");
		query.setParameter(1, inventurIId);
		query.setParameter(2, artikelIId);
		query.setParameter(3, lagerIId);
		Inventurstand inventurstand = (Inventurstand) query.getSingleResult();
		return assembleInventurstandDto(inventurstand);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String importiereInventurliste(Integer inventurIId,
			ArrayList<InvenurlisteImportDto> alImportdaten,
			TheClientDto theClientDto) {
		String log = "";
		byte[] CRLFAscii = { 13, 10 };
		InventurDto inventurDto = null;
		try {
			inventurDto = getInventurFac().inventurFindByPrimaryKey(
					inventurIId, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (inventurDto.getLagerIId() != null) {

			for (int i = 0; i < alImportdaten.size(); i++) {
				InvenurlisteImportDto impDto = alImportdaten.get(i);
				ArtikelDto aDto = getArtikelFac()
						.artikelFindByCNrMandantCNrOhneExc(
								impDto.getCArtikelnummer(),
								theClientDto.getMandant());

				if (aDto != null) {
					BigDecimal bdMenge = null;
					try {
						bdMenge = new BigDecimal(impDto.getCInventurmenge());
					} catch (NumberFormatException e) {
						log += "Zeile -" + (i + 1) + "- Die Menge  "
								+ impDto.getCInventurmenge()
								+ " ist nicht g\u00FCltig"
								+ new String(CRLFAscii);
						continue;
					}

					InventurlisteDto ilDto = new InventurlisteDto();
					ilDto.setArtikelIId(aDto.getIId());
					ilDto.setInventurIId(inventurIId);
					ilDto.setLagerIId(inventurDto.getLagerIId());
					ilDto.setNInventurmenge(bdMenge);

					// Wenn bereits in Inventurliste, dann auslassen und loggen

					InventurlisteDto[] ilVorhandenDtos = null;
					try {
						ilVorhandenDtos = getInventurFac()
								.inventurlisteFindByInventurIIdLagerIIdArtikelIId(
										inventurIId, inventurDto.getLagerIId(),
										aDto.getIId(), theClientDto);

						if (ilVorhandenDtos.length == 0) {

							getInventurFac().createInventurliste(ilDto, false,
									theClientDto);
						} else {
							log += "Zeile -"
									+ (i + 1)
									+ "- Es gibt bereits einen Inventurlisteneintrag f\u00FCr den Artikel "
									+ impDto.getCArtikelnummer()
									+ new String(CRLFAscii);
							continue;
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else {
					log += "Zeile -" + (i + 1)
							+ "- Es konnte keine Artikelnummer  "
							+ impDto.getCArtikelnummer() + " gefunden werden."
							+ new String(CRLFAscii);
					continue;
				}

			}

		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_INVENTUR_IMPORT_NUR_MIT_LAGER_MOEGLICH,
					new Exception(
							"EJBExceptionLP.FEHLER_INVENTUR_IMPORT_NUR_MIT_LAGER_MOEGLICH"));
		}

		return log;
	}

	public InventurstandDto inventurstandfindByInventurIIdArtikelIIdLagerIIdOhneExc(
			Integer inventurIId, Integer lagerIId, Integer artikelIId,
			TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("InventurstandfindByInventurIIdArtikelIIdLagerIId");
		query.setParameter(1, inventurIId);
		query.setParameter(2, artikelIId);
		query.setParameter(3, lagerIId);
		Inventurstand inventurstand;
		try {
			inventurstand = (Inventurstand) query.getSingleResult();
			return assembleInventurstandDto(inventurstand);
		} catch (NoResultException e) {
			return null;
		}

	}

	/**
	 * Erstellt eine Inventurliste einer best. Inventur
	 * 
	 * @param inventurIId
	 *            Inventur-ID
	 * @param lagerIId
	 *            Lager-ID
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 */

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printInventurliste(Integer inventurIId,
			Integer lagerIId, boolean bInventurpreis, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT inventurliste, (SELECT spr.c_bez FROM FLRArtikellistespr as spr WHERE spr.Id.artikelliste=inventurliste.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "' )  from FLRInventurliste as inventurliste WHERE inventurliste.flrinventur.i_id="
				+ inventurIId;

		sQuery = sQuery + " ORDER BY inventurliste.flrartikel.c_nr";

		org.hibernate.Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		String letzterArtikel = null;
		Object[][] dataHelp = new Object[resultList.size()][9];

		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLRInventurliste flrinventurliste = (FLRInventurliste) o[0];

			if ((!flrinventurliste.getFlrartikel().getC_nr()
					.equals(letzterArtikel))) {

				if (Helper.short2boolean(flrinventurliste.getFlrartikel()
						.getB_chargennrtragend()) == true
						|| Helper.short2boolean(flrinventurliste
								.getFlrartikel().getB_seriennrtragend()) == true) {

					// try {
					Query query = em
							.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
					query.setParameter(1, inventurIId);
					query.setParameter(2, flrinventurliste.getFlrlager()
							.getI_id());
					query.setParameter(3, flrinventurliste.getFlrartikel()
							.getI_id());
					Collection<?> cl = query.getResultList();
					// if (cl.isEmpty()) {
					// dataHelp[row][REPORT_INVENTURLISTE_INVENTURMENGE] = new
					// BigDecimal(0);
					// }
					// else {
					InventurlisteDto[] inventurlisteDtos = assembleInventurlisteDtos(cl);

					BigDecimal menge = new BigDecimal(0);
					String snrs = "";
					for (int i = 0; i < inventurlisteDtos.length; i++) {
						menge = menge.add(inventurlisteDtos[i]
								.getNInventurmenge());

						if (Helper.short2boolean(flrinventurliste
								.getFlrartikel().getB_chargennrtragend()) == true) {

							snrs = snrs
									+ inventurlisteDtos[i].getNInventurmenge()
									+ " "
									+ flrinventurliste.getFlrartikel()
											.getEinheit_c_nr()
									+ " "
									+ inventurlisteDtos[i]
											.getCSeriennrchargennr() + ", ";
						} else if (Helper.short2boolean(flrinventurliste
								.getFlrartikel().getB_seriennrtragend()) == true) {
							snrs = snrs
									+ inventurlisteDtos[i]
											.getCSeriennrchargennr() + ", ";
						}

						// }

						dataHelp[row][REPORT_INVENTURLISTE_INVENTURMENGE] = menge;
						dataHelp[row][REPORT_INVENTURLISTE_SERIENNUMMERCHARGENNUMMER] = snrs;

						// }
						// catch (FinderException ex1) {
						// dataHelp[row][REPORT_INVENTURLISTE_INVENTURMENGE] =
						// new BigDecimal(0);
						// }
					}
				} else {
					dataHelp[row][REPORT_INVENTURLISTE_INVENTURMENGE] = flrinventurliste
							.getN_inventurmenge();

				}

				dataHelp[row][REPORT_INVENTURLISTE_ARTIKELNUMMER] = flrinventurliste
						.getFlrartikel().getC_nr();
				if (o[1] != null) {
					dataHelp[row][REPORT_INVENTURLISTE_ARTIKELBEZEICHNUNG] = o[1];
				}
				dataHelp[row][REPORT_INVENTURLISTE_LAGER] = flrinventurliste
						.getFlrlager().getC_nr();

				ArtikellieferantDto alDto;
				try {
					alDto = getArtikelFac().getArtikelEinkaufspreis(
							flrinventurliste.getFlrartikel().getI_id(),
							new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (alDto != null && alDto.getNNettopreis() != null
							&& alDto.getNNettopreis().doubleValue() != 0) {
						dataHelp[row][REPORT_INVENTURLISTE_LIEF1PREIS_AKTUELL] = alDto
								.getNNettopreis();
					}

					alDto = getArtikelFac().getArtikelEinkaufspreis(
							flrinventurliste.getFlrartikel().getI_id(),
							null,
							new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(),
							new java.sql.Date(inventurDto.getTInventurdatum()
									.getTime()), theClientDto);
					if (alDto != null && alDto.getNNettopreis() != null
							&& alDto.getNNettopreis().doubleValue() != 0) {
						dataHelp[row][REPORT_INVENTURLISTE_LIEF1PREIS_ZUM_INVENTURDATUM] = alDto
								.getNNettopreis();
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				BigDecimal inventurmenge = (BigDecimal) dataHelp[row][REPORT_INVENTURLISTE_INVENTURMENGE];

				if (bInventurpreis == true) {

					try {
						Query query = em
								.createNamedQuery("InventurstandfindByInventurIIdArtikelIIdLagerIId");
						query.setParameter(1, flrinventurliste.getFlrinventur()
								.getI_id());
						query.setParameter(2, flrinventurliste.getFlrartikel()
								.getI_id());
						query.setParameter(3, flrinventurliste.getFlrlager()
								.getI_id());
						Inventurstand inventurstand = (Inventurstand) query
								.getSingleResult();
						dataHelp[row][REPORT_INVENTURLISTE_INVENTURPREIS] = inventurstand
								.getNInventurpreis();
						dataHelp[row][REPORT_INVENTURLISTE_INVENTURWERT] = inventurmenge
								.multiply(inventurstand.getNInventurpreis());

					} catch (NoResultException ex2) {
						dataHelp[row][REPORT_INVENTURLISTE_INVENTURWERT] = new java.math.BigDecimal(
								0);
						dataHelp[row][REPORT_INVENTURLISTE_INVENTURPREIS] = new java.math.BigDecimal(
								0);
					}

				} else {
					try {
						BigDecimal preis = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										flrinventurliste.getFlrartikel()
												.getI_id(),
										flrinventurliste.getFlrlager()
												.getI_id(), theClientDto);

						dataHelp[row][REPORT_INVENTURLISTE_INVENTURPREIS] = getLagerFac()
								.getGemittelterGestehungspreisEinesLagers(
										flrinventurliste.getFlrartikel()
												.getI_id(),
										flrinventurliste.getFlrlager()
												.getI_id(), theClientDto);

						dataHelp[row][REPORT_INVENTURLISTE_INVENTURWERT] = inventurmenge
								.multiply(preis);

					} catch (RemoteException ex3) {
						throwEJBExceptionLPRespectOld(ex3);
					}
				}
				row++;
			}
			letzterArtikel = flrinventurliste.getFlrartikel().getC_nr();

		}

		session.close();
		data = new Object[row][7];
		for (int i = 0; i < row; i++) {
			data[i] = dataHelp[i];
		}
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = InventurFac.REPORT_INVENTURLISTE;
		parameter.put("P_INVENTUR", inventurDto.getCBez());
		parameter.put("P_INVENTURDATUM", inventurDto.getTInventurdatum());
		try {
			if (lagerIId != null) {
				LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", dto.getCNr());
			} else {
				parameter.put("P_LAGER", "ALLE");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (bInventurpreis == true) {
			parameter
					.put("P_PREIS",
							getTextRespectUISpr("lp.inventurpreis",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			parameter
					.put("P_WERT",
							getTextRespectUISpr("lp.inventurwert",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		} else {
			parameter
					.put("P_PREIS",
							getTextRespectUISpr("lp.gestehungspreis",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
			parameter
					.put("P_WERT",
							getTextRespectUISpr("lp.gestehungswert",
									theClientDto.getMandant(),
									theClientDto.getLocUi()));
		}

		try {
			parameter.put(
					"P_WAEHRUNG",
					getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto)
							.getWaehrungCNr());
		} catch (RemoteException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}

		initJRDS(parameter, InventurFac.REPORT_MODUL,
				InventurFac.REPORT_INVENTURLISTE, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		// lpReport.saveHTML("c:/anwesenheitsliste.html");
		return getReportPrint();
	}

	/**
	 * Erstellt eine Deltaliste einer best. Inventur
	 * 
	 * @param inventurIId
	 *            Inventur-ID
	 * @param lagerIId
	 *            Lager-ID
	 * @param bNurArtikelMitLagerstand
	 *            Boolean
	 * @param theClientDto
	 *            String
	 * @return JasperPrint
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printNichterfassteartikel(Integer inventurIId,
			Integer lagerIId, boolean bNurArtikelMitLagerstand,
			TheClientDto theClientDto) {

		if (inventurIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("inventurIId == null"));
		}
		index = -1;
		sAktuellerReport = InventurFac.REPORT_NICHTERFASSTEARTIKEL;

		Session session = FLRSessionFactory.getFactory().openSession();
		session.enableFilter("filterLocale").setParameter("paramLocale",
				Helper.locale2String(theClientDto.getLocUi()));
		String queryString = null;
		try {
			queryString = "SELECT a.i_id as artikelid, l.i_id as lagerid,a.c_nr, l.c_nr, "
					+ "(SELECT al.n_lagerstand FROM FLRArtikellager al WHERE compId.artikel_i_id=a.i_id AND compId.lager_i_id=l.i_id ) as lagerstand, "
					+ "(SELECT sum( il.n_inventurmenge) FROM FLRInventurliste il WHERE il.flrartikel=a.i_id AND il.flrlager.i_id=l.i_id AND il.flrinventur="
					+ inventurIId
					+ ") as inventurmenge, "
					+ "aspr.c_bez, aspr.c_zbez, a.b_lagerbewirtschaftet, a.b_seriennrtragend, a.b_chargennrtragend "
					+ "FROM FLRArtikelliste a, FLRLager as l LEFT OUTER JOIN a.artikelsprset AS aspr "
					+ "WHERE a.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND l.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND a.artikelart_c_nr NOT IN ('"
					+ ArtikelFac.ARTIKELART_HANDARTIKEL
					+ "') "
					+ " AND l.i_id NOT IN ("
					+ getLagerFac()
							.lagerFindByCNrByMandantCNr(
									LagerFac.LAGER_KEINLAGER,
									theClientDto.getMandant()).getIId()
					+ ","
					+ getLagerFac().lagerFindByCNrByMandantCNr(
							LagerFac.LAGER_WERTGUTSCHRIFT,
							theClientDto.getMandant()).getIId() + ") ";
			if (lagerIId != null) {
				queryString += " AND l.i_id=" + lagerIId + " ";
			}

			queryString += "ORDER BY a.c_nr ASC,l.c_nr ASC";
		} catch (RemoteException ex3) {
			throwEJBExceptionLPRespectOld(ex3);
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		int row = 0;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			Short b_seriennrtragend = (Short) o[9];
			Short b_chargennrtragend = (Short) o[10];
			java.math.BigDecimal lagerstand = (BigDecimal) o[4];
			if (lagerstand == null) {
				lagerstand = new BigDecimal(0);
			}
			if (Helper.short2Boolean(b_seriennrtragend) == false
					&& Helper.short2Boolean(b_chargennrtragend) == false) {

				java.math.BigDecimal summeInventur = (BigDecimal) o[5];

				if (summeInventur != null) {
					continue;
				}
				Object[] dataHelp = new Object[7];
				dataHelp[REPORT_NICHTERFASSTEARTIKEL_ARTIKELNUMMER] = o[2];
				dataHelp[REPORT_NICHTERFASSTEARTIKEL_ARTIKELBEZEICHNUNG] = o[6];

				dataHelp[REPORT_NICHTERFASSTEARTIKEL_LAGER] = o[3];
				dataHelp[REPORT_NICHTERFASSTEARTIKEL_LAGERSTAND] = lagerstand;

				alDaten.add(dataHelp);

			} else {

				Integer artikelIId = (Integer) o[0];
				Integer lagerIId_Zeile = (Integer) o[1];
				try {
					SeriennrChargennrAufLagerDto[] snrChnrDtos = getLagerFac()
							.getAllSerienChargennrAufLagerInfoDtos(artikelIId,
									lagerIId_Zeile, true, null, theClientDto);

					HashMap<String, BigDecimal> hmSnrChnr = new HashMap<String, BigDecimal>();
					for (int i = 0; i < snrChnrDtos.length; i++) {
						if (!hmSnrChnr.containsKey(snrChnrDtos[i]
								.getCSeriennrChargennr())) {
							hmSnrChnr.put(
									snrChnrDtos[i].getCSeriennrChargennr(),
									snrChnrDtos[i].getNMenge());
						} else {
							hmSnrChnr.put(
									snrChnrDtos[i].getCSeriennrChargennr(),
									hmSnrChnr.get(
											snrChnrDtos[i]
													.getCSeriennrChargennr())
											.add(snrChnrDtos[i].getNMenge()));
						}
					}

					InventurlisteDto[] invDtos = null;
					if (lagerIId != null) {
						invDtos = inventurlisteFindByInventurIIdLagerIIdArtikelIId(
								inventurIId, lagerIId, artikelIId, theClientDto);
					} else {
						invDtos = inventurlisteFindByInventurIIdArtikelIId(
								inventurIId, artikelIId, theClientDto);
					}

					for (int i = 0; i < invDtos.length; i++) {
						if (hmSnrChnr.containsKey(invDtos[i]
								.getCSeriennrchargennr())) {
							hmSnrChnr
									.remove(invDtos[i].getCSeriennrchargennr());
						}
					}

					Iterator<String> it = hmSnrChnr.keySet().iterator();

					while (it.hasNext()) {

						String chnr = it.next();
						Object[] dataHelp = new Object[7];
						dataHelp[REPORT_NICHTERFASSTEARTIKEL_ARTIKELNUMMER] = o[2];
						dataHelp[REPORT_NICHTERFASSTEARTIKEL_ARTIKELBEZEICHNUNG] = o[6];
						dataHelp[REPORT_NICHTERFASSTEARTIKEL_SERIENNUMMERCHARGENNUMMER] = chnr;
						dataHelp[REPORT_NICHTERFASSTEARTIKEL_LAGER] = o[3];
						dataHelp[REPORT_NICHTERFASSTEARTIKEL_LAGERSTAND] = hmSnrChnr
								.get(chnr);

						alDaten.add(dataHelp);
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		Object[][] returnArray = new Object[row][alDaten.size()];
		data = (Object[][]) alDaten.toArray(returnArray);

		session.close();
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;
		sAktuellerReport = InventurFac.REPORT_NICHTERFASSTEARTIKEL;
		try {
			if (lagerIId != null) {
				LagerDto dto = getLagerFac().lagerFindByPrimaryKey(lagerIId);
				parameter.put("P_LAGER", dto.getCNr());
			} else {
				parameter.put("P_LAGER", "ALLE");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		parameter.put("P_INVENTUR", inventurDto.getCBez());
		parameter.put("P_INVENTURDATUM", inventurDto.getTInventurdatum());
		parameter.put("P_NURMITLAGERSTAND", new Boolean(
				bNurArtikelMitLagerstand));

		initJRDS(parameter, InventurFac.REPORT_MODUL,
				InventurFac.REPORT_NICHTERFASSTEARTIKEL,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	public InventurlisteDto[] inventurlisteFindByInventurIIdArtikelIId(
			Integer inventurIId, Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (inventurIId == null || artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurIId == null || artikelIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("InventurlistefindByInventurIIdArtikelIId");
		query.setParameter(1, inventurIId);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleInventurlisteDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public InventurlisteDto[] inventurlisteFindByInventurIIdLagerIIdArtikelIId(
			Integer inventurIId, Integer lagerIId, Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check(cNrUserI);
		if (inventurIId == null || artikelIId == null || lagerIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"inventurIId == null || artikelIId == null || lagerIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIId");
		query.setParameter(1, inventurIId);
		query.setParameter(2, lagerIId);
		query.setParameter(3, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleInventurlisteDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public InventurlisteDto inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr(
			Integer inventurIId, Integer artikelIId, Integer lagerIId,
			String cSeriennrchargennr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (inventurIId == null || artikelIId == null || lagerIId == null
				|| cSeriennrchargennr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"inventurIId == null || artikelIId == null || lagerIId == null || cSeriennrchargennr == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("InventurlistefindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr");
		query.setParameter(1, inventurIId);
		query.setParameter(2, lagerIId);
		query.setParameter(3, artikelIId);
		query.setParameter(4, cSeriennrchargennr);
		Inventurliste inventurliste = (Inventurliste) query.getSingleResult();
		if (inventurliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennr. Es gibt "
							+ "keine inventurliste mit iid "
							+ inventurIId
							+ " und lageriid "
							+ lagerIId
							+ " und artikelIid "
							+ artikelIId
							+ " und der Serienchargennummer "
							+ cSeriennrchargennr);
		}
		return assembleInventurlisteDto(inventurliste);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public InventurlisteDto inventurlisteFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Inventurliste inventurliste = em.find(Inventurliste.class, iId);
		if (inventurliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei inventurlisteFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		InventurlisteDto dto = assembleInventurlisteDto(inventurliste);

		try {
			dto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
					dto.getArtikelIId(), theClientDto));
			dto.setLagerDto(getLagerFac().lagerFindByPrimaryKey(
					dto.getLagerIId()));
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		return dto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setInventurlisteFromInventurlisteDto(
			Inventurliste inventurliste, InventurlisteDto inventurlisteDto) {
		inventurliste.setInventurIId(inventurlisteDto.getInventurIId());
		inventurliste.setLagerIId(inventurlisteDto.getLagerIId());
		inventurliste.setCSeriennrchargennr(inventurlisteDto
				.getCSeriennrchargennr());
		inventurliste.setArtikelIId(inventurlisteDto.getArtikelIId());
		inventurliste.setNInventurmenge(inventurlisteDto.getNInventurmenge());
		inventurliste.setTAendern(inventurlisteDto.getTAendern());
		inventurliste.setPersonalIIdAendern(inventurlisteDto
				.getPersonalIIdAendern());
		em.merge(inventurliste);
		em.flush();
	}

	private InventurlisteDto assembleInventurlisteDto(
			Inventurliste inventurliste) {
		return InventurlisteDtoAssembler.createDto(inventurliste);
	}

	private InventurlisteDto[] assembleInventurlisteDtos(
			Collection<?> inventurlistes) {
		List<InventurlisteDto> list = new ArrayList<InventurlisteDto>();
		if (inventurlistes != null) {
			Iterator<?> iterator = inventurlistes.iterator();
			while (iterator.hasNext()) {
				Inventurliste inventurliste = (Inventurliste) iterator.next();
				list.add(assembleInventurlisteDto(inventurliste));
			}
		}
		InventurlisteDto[] returnArray = new InventurlisteDto[list.size()];
		return (InventurlisteDto[]) list.toArray(returnArray);
	}

	public Integer createInventurprotokoll(
			InventurprotokollDto inventurprotokollDto, TheClientDto theClient)
			throws EJBExceptionLP {
		// check(cNrUserI);
		if (inventurprotokollDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurprotokollDto == null"));
		}
		if (inventurprotokollDto.getInventurlisteIId() == null
				|| inventurprotokollDto.getNKorrekturmenge() == null
				|| inventurprotokollDto.getNInventurpreis() == null
				|| inventurprotokollDto.getTZeitpunkt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurprotokollDto.getInventurlisteIId() == null || inventurprotokollDto.getNKorrekturmenge() == null ||  inventurprotokollDto.getNInventurpreis() == null || inventurprotokollDto.getTZeitpunkt() == null"));
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INVENTURPROTOKOLL);
			inventurprotokollDto.setIId(pk);

			inventurprotokollDto.setPersonalIIdAendern(theClient
					.getIDPersonal());
			inventurprotokollDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			Inventurprotokoll inventurprotokoll = new Inventurprotokoll(
					inventurprotokollDto.getIId(),
					inventurprotokollDto.getInventurIId(),
					inventurprotokollDto.getInventurlisteIId(),
					inventurprotokollDto.getTZeitpunkt(),
					inventurprotokollDto.getNKorrekturmenge(),
					inventurprotokollDto.getNInventurpreis(),
					inventurprotokollDto.getPersonalIIdAendern());
			em.persist(inventurprotokoll);
			em.flush();
			setInventurprotokollFromInventurprotokollDto(inventurprotokoll,
					inventurprotokollDto);
			return inventurprotokollDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public InventurprotokollDto inventurprotokollFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Inventurprotokoll inventurprotokoll = em.find(Inventurprotokoll.class,
				iId);
		if (inventurprotokoll == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei inventurprotokollFindByPrimaryKey. Es gibt keine iid "
							+ iId);
		}
		return assembleInventurprotokollDto(inventurprotokoll);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public InventurprotokollDto inventurprotokollFindyByInventurlisteIIdTZeitpunkt(
			Integer inventurlisteIId, Timestamp tZeitpunkt)
			throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("InventurprotokollfindyByInventurlisteIIdTZeitpunkt");
			query.setParameter(1, inventurlisteIId);
			query.setParameter(2, tZeitpunkt);
			Inventurprotokoll inventurprotokoll = (Inventurprotokoll) query
					.getSingleResult();
			return assembleInventurprotokollDto(inventurprotokoll);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}
	}

	private void setInventurprotokollFromInventurprotokollDto(
			Inventurprotokoll inventurprotokoll,
			InventurprotokollDto inventurprotokollDto) {
		inventurprotokoll.setInventurlisteIId(inventurprotokollDto
				.getInventurlisteIId());
		inventurprotokoll.setInventurIId(inventurprotokollDto.getInventurIId());
		inventurprotokoll.setTZeitpunkt(inventurprotokollDto.getTZeitpunkt());
		inventurprotokoll.setNKorrekturmenge(inventurprotokollDto
				.getNKorrekturmenge());
		inventurprotokoll.setTAendern(inventurprotokollDto.getTAendern());
		inventurprotokoll.setPersonalIIdAendern(inventurprotokollDto
				.getPersonalIIdAendern());
		em.merge(inventurprotokoll);
		em.flush();
	}

	private InventurprotokollDto assembleInventurprotokollDto(
			Inventurprotokoll inventurprotokoll) {
		return InventurprotokollDtoAssembler.createDto(inventurprotokoll);
	}

	private InventurprotokollDto[] assembleInventurprotokollDtos(
			Collection<?> inventurprotokolls) {
		List<InventurprotokollDto> list = new ArrayList<InventurprotokollDto>();
		if (inventurprotokolls != null) {
			Iterator<?> iterator = inventurprotokolls.iterator();
			while (iterator.hasNext()) {
				Inventurprotokoll inventurprotokoll = (Inventurprotokoll) iterator
						.next();
				list.add(assembleInventurprotokollDto(inventurprotokoll));
			}
		}
		InventurprotokollDto[] returnArray = new InventurprotokollDto[list
				.size()];
		return (InventurprotokollDto[]) list.toArray(returnArray);
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

	// @TransactionTimeout(200000)
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void inventurpreiseAktualisieren(Integer inventurIId,
			boolean bAufGestpreisZumInventurdatumAktualisieren,
			TheClientDto theClientDto) {

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRInventurstand.class);
		crit.add(Restrictions.eq("inventur_i_id", inventurIId));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRInventurstand flrInventurstand = (FLRInventurstand) resultListIterator
					.next();

			row++;
			System.out.println(row);

			java.math.BigDecimal gestpreis = new BigDecimal(0);

			if (bAufGestpreisZumInventurdatumAktualisieren == false) {

				gestpreis = getLagerFac()
						.getGemittelterGestehungspreisEinesLagers(
								flrInventurstand.getFlrartikel().getI_id(),
								flrInventurstand.getFlrartikel()
										.getB_lagerbewirtschaftet(),
								flrInventurstand.getFlrartikel()
										.getArtikelart_c_nr(),
								flrInventurstand.getFlrlager().getI_id(),
								theClientDto);

				// Einkaufspreis holen

				ArtikellieferantDto[] dtos = getArtikelFac()
						.artikellieferantFindByArtikelIId(
								flrInventurstand.getFlrartikel().getI_id(),
								theClientDto);

				if (dtos.length > 0 && dtos[0].getNNettopreis() != null) {

					if (dtos[0].getNNettopreis().doubleValue() > gestpreis
							.doubleValue()) {
						gestpreis = dtos[0].getNNettopreis();
					}

				} else {
					// Losablieferung holen
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();
					String query = "SELECT la.n_gestehungspreis FROM FLRLosablieferung la WHERE la.flrlos.flrstueckliste.flrartikel.i_id="
							+ flrInventurstand.getFlrartikel().getI_id()
							+ " AND la.n_gestehungspreis>0 AND la.t_aendern<='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(
									inventurDto.getTInventurdatum().getTime()))
							+ "' ORDER BY la.t_aendern DESC";

					org.hibernate.Query ablieferungen = session2
							.createQuery(query);
					ablieferungen.setMaxResults(1);

					List<?> resultList = ablieferungen.list();

					Iterator<?> resultListIteratorAbl = resultList.iterator();

					data = new Object[resultList.size()][8];
					if (resultListIteratorAbl.hasNext()) {
						BigDecimal gestpreisAblieferung = (BigDecimal) resultListIteratorAbl

						.next();
						gestpreis = gestpreisAblieferung;
					}

					session2.close();
				}

			} else {
				try {
					BigDecimal gestpreisZumZeitpunkt = getLagerFac()
							.getGestehungspreisZumZeitpunkt(
									flrInventurstand.getFlrartikel().getI_id(),
									flrInventurstand.getFlrlager().getI_id(),
									inventurDto.getTInventurdatum(),
									theClientDto);
					if (gestpreisZumZeitpunkt != null) {
						gestpreis = gestpreisZumZeitpunkt;
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			// Updaten

			InventurstandDto inventurstandDto = inventurstandFindByPrimaryKey(flrInventurstand
					.getI_id());
			inventurstandDto.setNInventurpreis(gestpreis);
			try {
				getInventurFac().updateInventurstand(inventurstandDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		InventurDto dto = inventurFindByPrimaryKey(inventurIId, theClientDto);

		dto.setBAbwertungdurchgefuehrt(Helper.boolean2Short(false));
		dto.setTAbwertungdurchgefuehrt(null);

		getInventurFac().updateInventur(dto, theClientDto);

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void invturprotokollZumStichtagZuruecknehmen(Integer inventurIId,
			java.sql.Date tAbStichtag, TheClientDto theClientDto) {

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		if (Helper.short2boolean(inventurDto.getBInventurdurchgefuehrt()) == false) {

			Session session = FLRSessionFactory.getFactory().openSession();

			org.hibernate.Criteria crit = session
					.createCriteria(FLRInventurprotokoll.class);
			crit.add(Restrictions.gt("t_zeitpunkt", tAbStichtag));
			crit.createAlias("flrinventurliste", "il").createAlias(
					"flrinventur", "i");
			crit.add(Restrictions.eq("i.i_id", inventurIId));

			List<?> results = crit.list();
			Iterator<?> resultListIteratorInventur = results.iterator();
			int row = 0;
			while (resultListIteratorInventur.hasNext()) {
				FLRInventurprotokoll flrInventurprotokoll = (FLRInventurprotokoll) resultListIteratorInventur
						.next();

				if (flrInventurprotokoll.getN_korrekturmenge().doubleValue() < 0) {
					try {
						getLagerFac().bucheAb(
								LocaleFac.BELEGART_INVENTUR,
								inventurIId,
								flrInventurprotokoll.getFlrinventurliste()
										.getI_id(),
								flrInventurprotokoll.getFlrinventurliste()
										.getFlrartikel().getI_id(),
								new BigDecimal(0),
								flrInventurprotokoll.getN_inventurpreis(),
								flrInventurprotokoll.getFlrinventurliste()
										.getFlrlager().getI_id(),
								flrInventurprotokoll.getFlrinventurliste()
										.getC_seriennrchargennr(),
								inventurDto.getTInventurdatum(), theClientDto);
					} catch (Throwable e) {
						// Auslassen
						continue;
					}

				} else if (flrInventurprotokoll.getN_korrekturmenge()
						.doubleValue() > 0) {
					try {
						getLagerFac().bucheZu(
								LocaleFac.BELEGART_INVENTUR,
								inventurIId,
								flrInventurprotokoll.getFlrinventurliste()
										.getI_id(),
								flrInventurprotokoll.getFlrinventurliste()
										.getFlrartikel().getI_id(),
								new BigDecimal(0),
								flrInventurprotokoll.getN_inventurpreis(),
								flrInventurprotokoll.getFlrinventurliste()
										.getFlrlager().getI_id(),
								flrInventurprotokoll.getFlrinventurliste()
										.getC_seriennrchargennr(),
								inventurDto.getTInventurdatum(), theClientDto,
								null, true);
					} catch (RemoteException e) {
						// Auslassen
						continue;
					}

				}

				Integer inventurlisteIId = flrInventurprotokoll
						.getFlrinventurliste().getI_id();
				getInventurFac().removeInventurprotokoll(
						flrInventurprotokoll.getI_id());

				InventurlisteDto ilDto = new InventurlisteDto();
				ilDto.setIId(inventurlisteIId);

				try {
					getInventurFac().removeInventurliste(ilDto, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

	}

	public void inventurDurchfuehrungZuruecknehmen(Integer inventurIId,
			TheClientDto theClientDto) {

		Inventur inventur = em.find(Inventur.class, inventurIId);
		inventur.setBInventurdurchgefuehrt(Helper.boolean2Short(false));
		inventur.setTInventurdurchgefuehrt(null);
		inventur.setPersonalIIdInventurdurchgefuehrt(null);
		em.merge(inventur);
		em.flush();

		Session session = FLRSessionFactory.getFactory().openSession();
		String hqlDelete = "delete FLRInventurstand WHERE inventur_i_id="
				+ inventurIId;
		session.createQuery(hqlDelete).executeUpdate();
		session.close();

		myLogger.logKritisch("Die Inventur mit dem Datum "
				+ inventur.getTInventurdatum()
				+ " wurde von der Person mit der ID="
				+ theClientDto.getIDPersonal() + " zurueckgenommen");

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void inventurpreiseAufEkPreisSetzen(Integer inventurIId,
			TheClientDto theClientDto) {

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRInventurstand.class);
		crit.add(Restrictions.eq("inventur_i_id", inventurIId));

		List<?> results = crit.list();
		Iterator<?> resultListIteratorInventur = results.iterator();
		int row = 0;
		while (resultListIteratorInventur.hasNext()) {
			FLRInventurstand flrInventurstand = (FLRInventurstand) resultListIteratorInventur
					.next();

			row++;
			System.out.println(row + " von " + results.size());

			java.math.BigDecimal inventurpeis = new BigDecimal(0);

			Session sessionZugang = FLRSessionFactory.getFactory()
					.openSession();
			org.hibernate.Criteria critZugang = sessionZugang
					.createCriteria(FLRLagerbewegung.class);
			critZugang.add(Restrictions.eq(
					LagerFac.FLR_LAGERBEWEGUNG_ARTIKEL_I_ID, flrInventurstand
							.getFlrartikel().getI_id()));
			critZugang.add(Restrictions.eq(LagerFac.FLR_LAGERBEWEGUNG_B_ABGANG,
					Helper.boolean2Short(false)));
			critZugang.add(Restrictions.eq(
					LagerFac.FLR_LAGERBEWEGUNG_B_HISTORIE,
					Helper.boolean2Short(false)));
			critZugang.add(Restrictions.gt(LagerFac.FLR_LAGERBEWEGUNG_N_MENGE,
					new BigDecimal(0)));
			critZugang.add(Restrictions.gt(
					LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT,
					inventurDto.getTInventurdatum()));

			String[] arten = new String[2];
			arten[0] = LocaleFac.BELEGART_LOSABLIEFERUNG;
			arten[1] = LocaleFac.BELEGART_BESTELLUNG;
			critZugang.add(Restrictions.in(
					LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, arten));
			critZugang.addOrder(Order
					.asc(LagerFac.FLR_LAGERBEWEGUNG_T_BUCHUNGSZEIT));

			critZugang.setMaxResults(1);
			List<?> resultList = critZugang.list();

			if (resultList.size() > 0) {
				Iterator<?> resultListIterator = resultList.iterator();
				FLRLagerbewegung lagerbewegung = (FLRLagerbewegung) resultListIterator
						.next();
				inventurpeis = lagerbewegung.getN_einstandspreis();
			} else {
				// EK-Preis
				ArtikellieferantDto alDto;
				try {
					alDto = getArtikelFac().getArtikelEinkaufspreis(
							flrInventurstand.getFlrartikel().getI_id(),
							new BigDecimal(1),
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (alDto != null && alDto.getNNettopreis() != null
							&& alDto.getNNettopreis().doubleValue() != 0) {
						inventurpeis = alDto.getNNettopreis();
					} else {
						continue;
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
			sessionZugang.close();
			// Updaten

			InventurstandDto inventurstandDto = inventurstandFindByPrimaryKey(flrInventurstand
					.getI_id());
			inventurstandDto.setNInventurpreis(inventurpeis);
			try {
				getInventurFac().updateInventurstand(inventurstandDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		InventurDto dto = inventurFindByPrimaryKey(inventurIId, theClientDto);

		dto.setBAbwertungdurchgefuehrt(Helper.boolean2Short(false));
		dto.setTAbwertungdurchgefuehrt(null);

		getInventurFac().updateInventur(dto, theClientDto);

	}

	// PJ 17152
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void inventurDurchfuehren(Integer inventurIId,
			boolean bNichtInventierteArtikelAufNullSetzen,
			TheClientDto theClientDto) throws EJBExceptionLP {

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		myLogger.logKritisch("Inventur Start:"
				+ new Timestamp(System.currentTimeMillis()));

		// Inventurdatum auf morgen 00:00 setzen
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(inventurDto.getTInventurdatum().getTime());
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);

		Timestamp ts = new Timestamp(c.getTimeInMillis());
		inventurDto.setTInventurdatum(Helper.cutTimestamp(ts));

		if (Helper.short2boolean(inventurDto.getBInventurdurchgefuehrt()) == true) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_INVENTUR_BEREITS_DURCHGEFUEHRT,
					new Exception(
							"Helper.short2boolean(inventurDto.getBInventurdurchgefuehrt())==true"));
		}

		try {

			// Wenn nur ein Lager angegeben, dann Inventur auch nur fuer ein
			// Lager durchfuehren
			LagerDto[] lagerDtos = null;
			if (inventurDto.getLagerIId() == null) {
				lagerDtos = getLagerFac().lagerFindByMandantCNr(
						theClientDto.getMandant());
			} else {
				lagerDtos = new LagerDto[1];
				lagerDtos[0] = getLagerFac().lagerFindByPrimaryKey(
						inventurDto.getLagerIId());
			}
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();

			Criteria artikelQuery = session.createCriteria(FLRArtikel.class);
			artikelQuery
					.add(Restrictions.eq("mandant_c_nr",
							theClientDto.getMandant()))
					.add(Restrictions.not(Restrictions.in(
							ArtikelFac.FLR_ARTIKELLISTE_ARTIKELART_C_NR,
							new String[] { ArtikelFac.ARTIKELART_HANDARTIKEL })));
			artikelQuery.addOrder(Order.asc("i_id"));

			List<?> resultList = artikelQuery.list();

			Iterator<?> resultListIterator = resultList.iterator();
			int ii = 0;
			while (resultListIterator.hasNext()) {
				FLRArtikel artikel = (FLRArtikel) resultListIterator.next();
				ii += 1;

				// myLogger.logKritisch(artikel.getC_nr()+": "+new
				// Integer(ii).toString());
				// long t=System.currentTimeMillis();
				for (int i = 0; i < lagerDtos.length; i++) {
					long l = System.currentTimeMillis();

					// wenn bNichtInventierteArtikelAufNullSetzen==true, dann
					// artikel mit jetzt auf lagerstand null setzen
					if (bNichtInventierteArtikelAufNullSetzen == true
							&& Helper.short2boolean(artikel
									.getB_lagerbewirtschaftet()) == true) {
						if (Helper.short2boolean(artikel
								.getB_chargennrtragend()) == false
								&& Helper.short2boolean(artikel
										.getB_seriennrtragend()) == false) {

							InventurlisteDto[] ilDtos = inventurlisteFindByInventurIIdLagerIIdArtikelIId(
									inventurIId, lagerDtos[i].getIId(),
									artikel.getI_id(), theClientDto);
							if (ilDtos.length < 1) {
								InventurlisteDto inventurlisteDto = new InventurlisteDto();
								inventurlisteDto.setArtikelIId(artikel
										.getI_id());
								inventurlisteDto.setLagerIId(lagerDtos[i]
										.getIId());
								inventurlisteDto.setInventurIId(inventurIId);
								inventurlisteDto
										.setNInventurmenge(new BigDecimal(0));
								getInventurFac().createInventurliste(
										inventurlisteDto, false, theClientDto);
							}
						}
					}

					InventurstandDto inventurstandDtoVorhanden = inventurstandfindByInventurIIdArtikelIIdLagerIIdOhneExc(
							inventurIId, lagerDtos[i].getIId(),
							artikel.getI_id(), theClientDto);
					if (inventurstandDtoVorhanden == null) {
						java.math.BigDecimal gestpreis = getLagerFac()
								.getGestehungspreisZumZeitpunkt(
										artikel.getI_id(),
										lagerDtos[i].getIId(),
										inventurDto.getTInventurdatum(),
										theClientDto);

						if (gestpreis == null) {
							gestpreis = new BigDecimal(0);
						}
						InventurstandDto inventurstandDto = new InventurstandDto();
						inventurstandDto.setArtikelIId(artikel.getI_id());
						inventurstandDto.setInventurIId(inventurIId);
						inventurstandDto.setLagerIId(lagerDtos[i].getIId());
						inventurstandDto.setNInventurmenge(getInventurstand(
								artikel.getI_id(), lagerDtos[i].getIId(),
								inventurDto.getIId(),
								inventurDto.getTInventurdatum(), theClientDto));
						inventurstandDto.setNInventurpreis(gestpreis);
						getInventurFac().createInventurstand(inventurstandDto,
								theClientDto);
					}
				}
				// myLogger.logKritisch("*"+ new
				// Long(System.currentTimeMillis()-t).toString());
				// t=System.currentTimeMillis();
			}
			session.close();

		}

		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		InventurDto inventur = inventurFindByPrimaryKey(inventurIId,
				theClientDto);

		inventur.setBInventurdurchgefuehrt(Helper.boolean2Short(true));
		inventur.setTInventurdurchgefuehrt(new Timestamp(System
				.currentTimeMillis()));
		inventur.setPersonalIIdInventurdurchgefuehrt(theClientDto
				.getIDPersonal());

		getInventurFac().updateInventur(inventur, theClientDto);

		myLogger.logKritisch("Inventur Ende:"
				+ new Timestamp(System.currentTimeMillis()));

	}

	public BigDecimal getInventurstand(Integer artikelIId, Integer lagerIId,
			Integer inventurIId, java.sql.Timestamp tInventurdatum,
			TheClientDto theClientDto) {
		InventurlisteDto[] dtos = inventurlisteFindByInventurIIdLagerIIdArtikelIId(
				inventurIId, lagerIId, artikelIId, theClientDto);
		BigDecimal inventurStand = null;

		try {
			if (dtos != null && dtos.length > 0) {
				BigDecimal mengeAusInventurliste = dtos[0].getNInventurmenge();

				java.math.BigDecimal lagerstandVeraenderung = getLagerFac()
						.getLagerstandsVeraenderungOhneInventurbuchungen(
								artikelIId, lagerIId, tInventurdatum,
								dtos[0].getTAendern(), theClientDto);
				inventurStand = mengeAusInventurliste
						.subtract(lagerstandVeraenderung);
			} else {
				java.math.BigDecimal lagerstandzumInventurdatum = getLagerFac()
						.getLagerstandZumZeitpunkt(artikelIId, lagerIId,
								tInventurdatum, theClientDto);

				inventurStand = lagerstandzumInventurdatum;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return inventurStand;
	}

	@TransactionTimeout(6000)
	public void preiseAbwerten(Integer inventurIId, boolean bMitStuecklisten,
			TheClientDto theClientDto) throws EJBExceptionLP {
		int iMonateGestpreisAbwerten = 6;
		double dProzentGestpreisAbwerten = 10;
		try {
			ParametermandantDto mandantparameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_AB_MONATE);

			iMonateGestpreisAbwerten = (Integer) mandantparameter
					.getCWertAsObject();

			mandantparameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_GESTEHUNGSPREISABWERTEN_PROZENT_PRO_MONAT);

			dProzentGestpreisAbwerten = (Double) mandantparameter
					.getCWertAsObject();

		}

		catch (RemoteException ex8) {
			throwEJBExceptionLPRespectOld(ex8);
		}

		InventurDto inventurDto = inventurFindByPrimaryKey(inventurIId);

		if (!Helper.short2boolean(inventurDto.getBAbwertungdurchgefuehrt())) {

			Session session = FLRSessionFactory.getFactory().openSession();

			String sQuery = "SELECT i, (SELECT SUM(r.n_menge) FROM FLRArtikelreservierung r WHERE r.flrartikel.i_id=i.flrartikel.i_id),(SELECT SUM(r.n_gesamtmenge) FROM FLRRahmenbedarfe r WHERE r.flrartikel.i_id=i.flrartikel.i_id),(SELECT SUM(r.n_menge) FROM FLRFehlmenge r WHERE r.flrartikel.i_id=i.flrartikel.i_id),(SELECT s.i_id FROM FLRStueckliste s WHERE s.flrartikel.i_id=i.flrartikel.i_id) FROM FLRInventurstand i WHERE i.inventur_i_id="
					+ inventurIId + " ";

			org.hibernate.Query query = session.createQuery(sQuery);

			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			int iRow = 0;

			while (resultListIterator.hasNext()) {

				System.out.println(iRow);

				Object[] o = (Object[]) resultListIterator.next();

				FLRInventurstand flrInventurstand = (FLRInventurstand) o[0];

				BigDecimal reservierungen = (BigDecimal) o[1];
				BigDecimal fehlmengen = (BigDecimal) o[3];
				BigDecimal rahmenbedarfe = (BigDecimal) o[2];

				Integer stuecklisteIId = (Integer) o[4];

				if (bMitStuecklisten == false && stuecklisteIId != null) {
					continue;
				}

				if (reservierungen != null && reservierungen.doubleValue() > 0) {
					continue;
				}
				if (fehlmengen != null && fehlmengen.doubleValue() > 0) {
					continue;
				}
				if (rahmenbedarfe != null && rahmenbedarfe.doubleValue() > 0) {
					continue;
				}
				if (flrInventurstand.getN_inventurpreis().doubleValue() != 0) {

					try {

						BigDecimal rahmenreservierungen = getReservierungFac()
								.getAnzahlRahmenreservierungen(
										flrInventurstand.getFlrartikel()
												.getI_id(), theClientDto);

						if (rahmenreservierungen != null
								&& rahmenreservierungen.doubleValue() > 0) {
							continue;
						}

						Inventurstand inventurstand = em
								.find(Inventurstand.class,
										flrInventurstand.getI_id());
						BigDecimal abPreis = getLagerFac()
								.getAbgewertetenGestehungspreis(
										flrInventurstand.getN_inventurpreis(),
										flrInventurstand.getFlrartikel()
												.getI_id(),
										flrInventurstand.getFlrlager()
												.getI_id(),
										inventurDto.getTInventurdatum(),
										iMonateGestpreisAbwerten,
										dProzentGestpreisAbwerten);
						if (abPreis != null) {
							inventurstand.setNInventurpreis(abPreis);
							em.merge(inventurstand);
							em.flush();
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
				iRow++;
			}

			session.close();

			Inventur inventur = em.find(Inventur.class, inventurIId);

			inventur.setBAbwertungdurchgefuehrt(Helper.boolean2Short(true));
			inventur.setPersonalIIdAbwertungdurchgefuehrt(theClientDto
					.getIDPersonal());
			inventur.setTAbwertungdurchgefuehrt(new Timestamp(System
					.currentTimeMillis()));
			em.merge(inventur);
			em.flush();
		}

	}

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;

		String fieldName = field.getName();

		if (sAktuellerReport.equals(InventurFac.REPORT_INVENTURSTAND)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELBEZEICHNUNG];
			} else if ("Kurzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELKURZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG];
			} else if ("Zusatzbezeichnung2".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELZUSATZBEZEICHNUNG2];
			} else if ("Seriennummerchargennummer".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_SERIENNUMMERCHARGENNUMMER];
			} else if ("Inventurmenge".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_INVENTURMENGE];
			} else if ("Inventurpreis".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_INVENTURPREIS];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_LAGER];
			} else if ("Inventurwert".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_INVENTURWERT];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_LAGERSTAND];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELART];
			} else if ("Artikelgruppe".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELGRUPPE];
			} else if ("Artikelklasse".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_ARTIKELKLASSE];
			} else if ("Stueckliste".equals(fieldName)) {
				value = data[index][REPORT_INVENTURSTAND_STUECKLISTE];
			}
		}
		if (sAktuellerReport.equals(InventurFac.REPORT_INVENTURLISTE)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_ARTIKELBEZEICHNUNG];
			} else if ("Seriennummerchargennummer".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_SERIENNUMMERCHARGENNUMMER];
			} else if ("Inventurmenge".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_INVENTURMENGE];
			} else if ("Inventurpreis".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_INVENTURPREIS];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_LAGER];
			} else if ("Inventurwert".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_INVENTURWERT];
			} else if ("Lief1PreisInventurdatum".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_LIEF1PREIS_ZUM_INVENTURDATUM];
			} else if ("Lief1PreisAktuell".equals(fieldName)) {
				value = data[index][REPORT_INVENTURLISTE_LIEF1PREIS_AKTUELL];
			}
		} else if (sAktuellerReport
				.equals(InventurFac.REPORT_INVENTURPROTOKOLL)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_ARTIKELBEZEICHNUNG];
			} else if ("Korrekturmenge".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_KORREKTURMENGE];
			} else if ("Korrekturzeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_KORREKTURZEITPUNKT];
			} else if ("Inventurpreis".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_INVENTURPREIS];
			} else if ("Inventurmenge".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_INVENTURMENGE];
			} else if ("Artikelart".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_ARTIKELART];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_INVENTURPROTOKOLL_LAGERSTAND];
			}
		} else if (sAktuellerReport
				.equals(InventurFac.REPORT_NICHTERFASSTEARTIKEL)) {
			if ("Artikelnummer".equals(fieldName)) {
				value = data[index][REPORT_NICHTERFASSTEARTIKEL_ARTIKELNUMMER];
			} else if ("Bezeichnung".equals(fieldName)) {
				value = data[index][REPORT_NICHTERFASSTEARTIKEL_ARTIKELBEZEICHNUNG];
			} else if ("Seriennummerchargennummer".equals(fieldName)) {
				value = data[index][REPORT_NICHTERFASSTEARTIKEL_SERIENNUMMERCHARGENNUMMER];
			} else if ("Lager".equals(fieldName)) {
				value = data[index][REPORT_NICHTERFASSTEARTIKEL_LAGER];
			} else if ("Lagerstand".equals(fieldName)) {
				value = data[index][REPORT_NICHTERFASSTEARTIKEL_LAGERSTAND];
				System.out
						.println(data[index][REPORT_NICHTERFASSTEARTIKEL_LAGERSTAND]);
			}
		}
		return value;

	}

	public Integer createInventurstand(InventurstandDto inventurstandDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (inventurstandDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("inventurstandDto == null"));
		}
		if (inventurstandDto.getInventurIId() == null
				|| inventurstandDto.getLagerIId() == null
				|| inventurstandDto.getArtikelIId() == null
				|| inventurstandDto.getNInventurmenge() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"inventurstandDto.getInventurIId() == null || inventurstandDto.getLagerIId() == null || inventurstandDto.getArtikelIId() == null || inventurstandDto.getNInventurmenge() == null"));
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INVENTURSTAND);
			inventurstandDto.setIId(pk);

			Inventurstand inventurstand = new Inventurstand(
					inventurstandDto.getIId(),
					inventurstandDto.getInventurIId(),
					inventurstandDto.getArtikelIId(),
					inventurstandDto.getLagerIId(),
					inventurstandDto.getNInventurmenge(),
					inventurstandDto.getNInventurpreis());
			em.persist(inventurstand);
			em.flush();
			setInventurstandFromInventurstandDto(inventurstand,
					inventurstandDto);
			return inventurstandDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeInventurstand(Integer iId) throws EJBExceptionLP {
		// try {
		Inventurstand toRemove = em.find(Inventurstand.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeInventurstand. Es gibt keine iid " + iId);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void removeInventurstand(InventurstandDto inventurstandDto)
			throws EJBExceptionLP {
		if (inventurstandDto != null) {
			Integer iId = inventurstandDto.getIId();
			removeInventurstand(iId);
		}
	}

	public void updateInventurstand(InventurstandDto inventurstandDto)
			throws EJBExceptionLP {
		if (inventurstandDto != null) {
			Integer iId = inventurstandDto.getIId();
			// try {
			Inventurstand inventurstand = em.find(Inventurstand.class, iId);
			if (inventurstand == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"FEhler bei updateInventurstand. Es gibt keine iid "
								+ iId + "\ndto.toString: "
								+ inventurstandDto.toString());
			}
			setInventurstandFromInventurstandDto(inventurstand,
					inventurstandDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public InventurstandDto inventurstandFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Inventurstand inventurstand = em.find(Inventurstand.class, iId);
		if (inventurstand == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei inventurstandFindByPrimaryKey. Es gitb keine iid "
							+ iId);
		}
		return assembleInventurstandDto(inventurstand);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setInventurstandFromInventurstandDto(
			Inventurstand inventurstand, InventurstandDto inventurstandDto) {
		inventurstand.setInventurIId(inventurstandDto.getInventurIId());
		inventurstand.setArtikelIId(inventurstandDto.getArtikelIId());
		inventurstand.setLagerIId(inventurstandDto.getLagerIId());
		inventurstand.setNInventurmenge(inventurstandDto.getNInventurmenge());
		inventurstand.setNInventurpreis(inventurstandDto.getNInventurpreis());
		inventurstand.setNAbgewerteterpreis(inventurstandDto
				.getNAbgewerteterpreis());
		em.merge(inventurstand);
		em.flush();
	}

	private InventurstandDto assembleInventurstandDto(
			Inventurstand inventurstand) {
		return InventurstandDtoAssembler.createDto(inventurstand);
	}

	private InventurstandDto[] assembleInventurstandDtos(
			Collection<?> inventurstands) {
		List<InventurstandDto> list = new ArrayList<InventurstandDto>();
		if (inventurstands != null) {
			Iterator<?> iterator = inventurstands.iterator();
			while (iterator.hasNext()) {
				Inventurstand inventurstand = (Inventurstand) iterator.next();
				list.add(assembleInventurstandDto(inventurstand));
			}
		}
		InventurstandDto[] returnArray = new InventurstandDto[list.size()];
		return (InventurstandDto[]) list.toArray(returnArray);
	}
}
