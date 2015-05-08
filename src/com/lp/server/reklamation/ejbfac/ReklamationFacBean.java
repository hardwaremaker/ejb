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
package com.lp.server.reklamation.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.reklamation.ejb.Aufnahmeart;
import com.lp.server.reklamation.ejb.Aufnahmeartspr;
import com.lp.server.reklamation.ejb.AufnahmeartsprPK;
import com.lp.server.reklamation.ejb.Behandlung;
import com.lp.server.reklamation.ejb.Fehler;
import com.lp.server.reklamation.ejb.Fehlerangabe;
import com.lp.server.reklamation.ejb.Fehlerangabespr;
import com.lp.server.reklamation.ejb.FehlerangabesprPK;
import com.lp.server.reklamation.ejb.Fehlerspr;
import com.lp.server.reklamation.ejb.FehlersprPK;
import com.lp.server.reklamation.ejb.Massnahme;
import com.lp.server.reklamation.ejb.Massnahmespr;
import com.lp.server.reklamation.ejb.MassnahmesprPK;
import com.lp.server.reklamation.ejb.Reklamation;
import com.lp.server.reklamation.ejb.Reklamationart;
import com.lp.server.reklamation.ejb.Reklamationbild;
import com.lp.server.reklamation.ejb.Schwere;
import com.lp.server.reklamation.ejb.Termintreue;
import com.lp.server.reklamation.ejb.Wirksamkeit;
import com.lp.server.reklamation.ejb.Wirksamkeitspr;
import com.lp.server.reklamation.ejb.WirksamkeitsprPK;
import com.lp.server.reklamation.service.AufnahmeartDto;
import com.lp.server.reklamation.service.AufnahmeartDtoAssembler;
import com.lp.server.reklamation.service.AufnahmeartsprDto;
import com.lp.server.reklamation.service.AufnahmeartsprDtoAssembler;
import com.lp.server.reklamation.service.BehandlungDto;
import com.lp.server.reklamation.service.BehandlungDtoAssembler;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.reklamation.service.FehlerDtoAssembler;
import com.lp.server.reklamation.service.FehlerangabeDto;
import com.lp.server.reklamation.service.FehlerangabeDtoAssembler;
import com.lp.server.reklamation.service.FehlerangabesprDto;
import com.lp.server.reklamation.service.FehlerangabesprDtoAssembler;
import com.lp.server.reklamation.service.FehlersprDto;
import com.lp.server.reklamation.service.FehlersprDtoAssembler;
import com.lp.server.reklamation.service.MassnahmeDto;
import com.lp.server.reklamation.service.MassnahmeDtoAssembler;
import com.lp.server.reklamation.service.MassnahmesprDto;
import com.lp.server.reklamation.service.MassnahmesprDtoAssembler;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationDtoAssembler;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.reklamation.service.ReklamationartDto;
import com.lp.server.reklamation.service.ReklamationartDtoAssembler;
import com.lp.server.reklamation.service.ReklamationbildDto;
import com.lp.server.reklamation.service.ReklamationbildDtoAssembler;
import com.lp.server.reklamation.service.SchwereDto;
import com.lp.server.reklamation.service.SchwereDtoAssembler;
import com.lp.server.reklamation.service.TermintreueDto;
import com.lp.server.reklamation.service.TermintreueDtoAssembler;
import com.lp.server.reklamation.service.WirksamkeitDto;
import com.lp.server.reklamation.service.WirksamkeitDtoAssembler;
import com.lp.server.reklamation.service.WirksamkeitsprDto;
import com.lp.server.reklamation.service.WirksamkeitsprDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ReklamationFacBean extends Facade implements ReklamationFac {
	@PersistenceContext
	private EntityManager em;
	private Massnahme massnahme;

	public void createReklamationart(ReklamationartDto reklamationartDto)
			throws EJBExceptionLP {
		if (reklamationartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationartDto == null"));
		}
		if (reklamationartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationartDto.getCNr() == null"));
		}
		try {
			Reklamationart reklamationart = new Reklamationart(
					reklamationartDto.getCNr());
			em.persist(reklamationart);
			em.flush();
			setReklamationartFromReklamationartDto(reklamationart,
					reklamationartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeReklamationart(ReklamationartDto reklamationartDto)
			throws EJBExceptionLP {
		if (reklamationartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationartDto == null"));
		}
		if (reklamationartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationartDto.getCNr() == null"));
		}
		// try {
		if (reklamationartDto != null) {
			Reklamationart toRemove = em.find(Reklamationart.class,
					reklamationartDto.getCNr());
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
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void updateReklamationart(ReklamationartDto reklamationartDto)
			throws EJBExceptionLP {
		if (reklamationartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationartDto == null"));
		}
		if (reklamationartDto.getCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationartDto.getCNr() == null"));
		}

		String cNr = reklamationartDto.getCNr();
		// try {
		Reklamationart reklamationart = em.find(Reklamationart.class, cNr);
		if (reklamationart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setReklamationartFromReklamationartDto(reklamationart,
				reklamationartDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ReklamationartDto reklamationartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("cNr == null"));
		}

		// try {
		Reklamationart reklamationsart = em.find(Reklamationart.class, cNr);
		if (reklamationsart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleReklamationartDto(reklamationsart);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setReklamationartFromReklamationartDto(
			Reklamationart reklamationart, ReklamationartDto reklamationartDto) {
		em.merge(reklamationart);
		em.flush();
	}

	private ReklamationartDto assembleReklamationartDto(
			Reklamationart reklamationart) {
		return ReklamationartDtoAssembler.createDto(reklamationart);
	}

	private ReklamationartDto[] assembleReklamationartDtos(
			Collection<?> reklamationarts) {
		List<ReklamationartDto> list = new ArrayList<ReklamationartDto>();
		if (reklamationarts != null) {
			Iterator<?> iterator = reklamationarts.iterator();
			while (iterator.hasNext()) {
				Reklamationart reklamationart = (Reklamationart) iterator
						.next();
				list.add(assembleReklamationartDto(reklamationart));
			}
		}
		ReklamationartDto[] returnArray = new ReklamationartDto[list.size()];
		return (ReklamationartDto[]) list.toArray(returnArray);
	}

	public Integer createReklamation(ReklamationDto reklamationDto,
			TheClientDto theClientDto) {
		if (reklamationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("massnahmeDto == null"));
		}
		if (reklamationDto.getAufnahmeartIId() == null
				|| reklamationDto.getBArtikel() == null
				|| reklamationDto.getStatusCNr() == null
				|| reklamationDto.getBBerechtigt() == null
				|| reklamationDto.getBFremdprodukt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getAufnahmeartIId() == null || reklamationDto.getBArtikel() == null || reklamationDto.getStatusCNr() == null || reklamationDto.getBBerechtigt() == null || reklamationDto.getBFremdprodukt() == null"));
		}
		if (reklamationDto.getFehlerangabeIId() == null
				|| reklamationDto.getKostenstelleIId() == null
				|| reklamationDto.getNMenge() == null
				|| reklamationDto.getReklamationartCNr() == null
				|| reklamationDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getFehlerangabeIId() == null || reklamationDto.getKostenstelleIId() == null || reklamationDto.getNMenge() == null || reklamationDto.getReklamationartCNr() == null || reklamationDto.getTBelegdatum() == null"));
		}
		if (reklamationDto.getPersonalIIdAufnehmer() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getPersonalIIdAufnehmer() == null"));
		}

		if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_FERTIGUNG)) {
			reklamationDto.setBestellungIId(null);
			reklamationDto.setLieferantIId(null);
			reklamationDto.setAnsprechpartnerIId(null);
			reklamationDto.setKundeIId(null);
			reklamationDto.setRechnungIId(null);
			reklamationDto.setLieferscheinIId(null);
			if (reklamationDto.getLosIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getLosIId()"));

			}

		} else if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_LIEFERANT)) {
			reklamationDto.setLosIId(null);
			reklamationDto.setKundeIId(null);
			reklamationDto.setRechnungIId(null);
			reklamationDto.setLieferscheinIId(null);
			if (reklamationDto.getLieferantIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getLieferantIId()"));

			}

		} else if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_KUNDE)) {
			if (reklamationDto.getKundeIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getKundeIId()"));

			}

		}

		if (Helper.short2boolean(reklamationDto.getBArtikel()) == true) {
			reklamationDto.setCHandartikel(null);
		} else {
			reklamationDto.setArtikelIId(null);
		}
		reklamationDto.setTBelegdatum(Helper.cutTimestamp(reklamationDto
				.getTBelegdatum()));

		reklamationDto.setNMenge(Helper.rundeKaufmaennisch(
				reklamationDto.getNMenge(), 4));
		if (reklamationDto.getNKostenmaterial() != null) {
			reklamationDto.setNKostenmaterial(Helper.rundeKaufmaennisch(
					reklamationDto.getNKostenmaterial(), 4));
		}
		if (reklamationDto.getNKostenarbeitszeit() != null) {
			reklamationDto.setNKostenarbeitszeit(Helper.rundeKaufmaennisch(
					reklamationDto.getNKostenarbeitszeit(), 4));
		}
		try {
			reklamationDto.setMandantCNr(theClientDto.getMandant());

			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(reklamationDto.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					reklamationDto.getMandantCNr(),
					reklamationDto.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_REKLAMATION,
					reklamationDto.getMandantCNr(), theClientDto);

			reklamationDto.setIId(bnr.getPrimaryKey());
			reklamationDto.setCNr(f.format(bnr));
		}

		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		try {
			Query query = em.createNamedQuery("ReklamationfindByMandantCNrCNr");
			query.setParameter(1, reklamationDto.getMandantCNr());
			query.setParameter(2, reklamationDto.getCNr());
			Reklamation doppelt = (Reklamation) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"RELKA_REKLAMATION.UK"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			reklamationDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			reklamationDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			reklamationDto.setTAnlegen(new java.sql.Timestamp(System
					.currentTimeMillis()));
			reklamationDto.setTAendern(new java.sql.Timestamp(System
					.currentTimeMillis()));

			Reklamation reklamation = new Reklamation(reklamationDto.getIId(),
					reklamationDto.getMandantCNr(), reklamationDto.getCNr(),
					reklamationDto.getReklamationartCNr(),
					reklamationDto.getTBelegdatum(),
					reklamationDto.getKostenstelleIId(),
					reklamationDto.getFehlerangabeIId(),
					reklamationDto.getAufnahmeartIId(),
					reklamationDto.getPersonalIIdAufnehmer(),
					reklamationDto.getBArtikel(), reklamationDto.getNMenge(),
					reklamationDto.getBFremdprodukt(),
					reklamationDto.getBBerechtigt(),
					reklamationDto.getBBetrifftgelieferte(),
					reklamationDto.getBBetrifftlagerstand(),
					reklamationDto.getPersonalIIdAnlegen(),
					reklamationDto.getPersonalIIdAendern(),
					reklamationDto.getStatusCNr());
			em.persist(reklamation);
			em.flush();
			setReklamationFromReklamationDto(reklamation, reklamationDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return reklamationDto.getIId();
	}

	public void removeReklamation(ReklamationDto reklamationDto)
			throws EJBExceptionLP {
		if (reklamationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationDto == null"));
		}
		if (reklamationDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationDto.getIId() == null"));
		}
		// try {
		Integer iId = reklamationDto.getIId();
		Reklamation toRemove = em.find(Reklamation.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateReklamation(ReklamationDto reklamationDto,
			TheClientDto theClientDto) {
		if (reklamationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("massnahmeDto == null"));
		}
		if (reklamationDto.getAufnahmeartIId() == null
				|| reklamationDto.getBArtikel() == null
				|| reklamationDto.getStatusCNr() == null
				|| reklamationDto.getBBerechtigt() == null
				|| reklamationDto.getBFremdprodukt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getAufnahmeartIId() == null || reklamationDto.getBArtikel() == null || reklamationDto.getStatusCNr() == null || reklamationDto.getBBerechtigt() == null || reklamationDto.getBFremdprodukt() == null"));
		}
		if (reklamationDto.getFehlerangabeIId() == null
				|| reklamationDto.getKostenstelleIId() == null
				|| reklamationDto.getNMenge() == null
				|| reklamationDto.getReklamationartCNr() == null
				|| reklamationDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getFehlerangabeIId() == null || reklamationDto.getKostenstelleIId() == null || reklamationDto.getNMenge() == null || reklamationDto.getReklamationartCNr() == null || reklamationDto.getTBelegdatum() == null"));
		}
		if (reklamationDto.getPersonalIIdAufnehmer() == null
				|| reklamationDto.getIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationDto.getPersonalIIdAufnehmer() == null || reklamationDto.getIId() == null"));
		}
		if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_FERTIGUNG)) {
			reklamationDto.setBestellungIId(null);
			reklamationDto.setLieferantIId(null);
			reklamationDto.setAnsprechpartnerIId(null);
			reklamationDto.setKundeIId(null);
			reklamationDto.setRechnungIId(null);
			reklamationDto.setLieferscheinIId(null);
			if (reklamationDto.getLosIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getLosIId()"));

			}

		} else if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_LIEFERANT)) {
			reklamationDto.setLosIId(null);
			reklamationDto.setKundeIId(null);
			reklamationDto.setRechnungIId(null);
			reklamationDto.setLieferscheinIId(null);
			if (reklamationDto.getLieferantIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getLieferantIId()"));

			}

		} else if (reklamationDto.getReklamationartCNr().equals(
				ReklamationFac.REKLAMATIONART_KUNDE)) {
			if (reklamationDto.getKundeIId() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("reklamationDto.getKundeIId()"));

			}

		}

		if (Helper.short2boolean(reklamationDto.getBArtikel()) == true) {
			reklamationDto.setCHandartikel(null);
		} else {
			reklamationDto.setArtikelIId(null);
		}
		reklamationDto.setTBelegdatum(Helper.cutTimestamp(reklamationDto
				.getTBelegdatum()));

		reklamationDto.setNMenge(Helper.rundeKaufmaennisch(
				reklamationDto.getNMenge(), 4));
		if (reklamationDto.getNKostenmaterial() != null) {
			reklamationDto.setNKostenmaterial(Helper.rundeKaufmaennisch(
					reklamationDto.getNKostenmaterial(), 4));
		}
		if (reklamationDto.getNKostenarbeitszeit() != null) {
			reklamationDto.setNKostenarbeitszeit(Helper.rundeKaufmaennisch(
					reklamationDto.getNKostenarbeitszeit(), 4));
		}

		Integer iId = reklamationDto.getIId();
		// try {
		reklamationDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		reklamationDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		Reklamation reklamation = em.find(Reklamation.class, iId);
		if (reklamation == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setReklamationFromReklamationDto(reklamation, reklamationDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public ReklamationDto reklamationFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		ReklamationDto reklamation = reklamationFindByPrimaryKeyOhneExc(iId);
		if (reklamation == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return reklamation;
	}
	

	public ReklamationDto reklamationFindByPrimaryKeyOhneExc(Integer iId){
		
		Reklamation reklamation = em.find(Reklamation.class, iId);
		return reklamation==null?null:assembleReklamationDto(reklamation);
	}

	public ReklamationDto[] reklamationFindByKundeIIdMandantCNr(
			Integer iKundeIId, String mandantCNrI) throws EJBExceptionLP {
		if (iKundeIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iKundeIId == null"));
		}

		if (mandantCNrI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("mandantCNrI == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByKundeIIdMandantCNr");
		query.setParameter(1, iKundeIId);
		query.setParameter(2, mandantCNrI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleReklamationDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ReklamationDto[] reklamationfindOffeneReklamationenEinesArtikels(
			Integer artikelIId) {

		Query query = em
				.createNamedQuery("ReklamationfindOffeneReklamationenEinesArtikels");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();
		return assembleReklamationDtos(cl);
	}

	public ReklamationDto[] reklamationFindByKundeIIdMandantCNrOhneExc(
			Integer iKundeIId, String mandantCNrI) {
		if (iKundeIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iKundeIId == null"));
		}

		if (mandantCNrI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("mandantCNrI == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByKundeIIdMandantCNr");
		query.setParameter(1, iKundeIId);
		query.setParameter(2, mandantCNrI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleReklamationDtos(cl);
		// }
		// catch (Throwable t) {
		// return null;
		// }
	}

	public ReklamationDto[] reklamationFindByLieferantIIdMandantCNr(
			Integer iLieferantIId, String mandantCNrI) throws EJBExceptionLP {
		if (iLieferantIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iLieferantIId == null"));
		}
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("mandantCNrI == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByLieferantIIdMandantCNr");
		query.setParameter(1, iLieferantIId);
		query.setParameter(2, mandantCNrI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }
		return assembleReklamationDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// e);
		// }
	}

	public ReklamationDto[] reklamationFindByLieferantIIdMandantCNrOhneExc(
			Integer iLieferantIId, String mandantCNrI) {
		if (iLieferantIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iLieferantIId == null"));
		}
		if (mandantCNrI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("mandantCNrI == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByLieferantIIdMandantCNr");
		query.setParameter(1, iLieferantIId);
		query.setParameter(2, mandantCNrI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		// @todo getSingleResult oder getResultList ?
		return assembleReklamationDtos(cl);
		// }
		// catch (Throwable th) {
		// return null;
		// }
	}

	private void setReklamationFromReklamationDto(Reklamation reklamation,
			ReklamationDto reklamationDto) {
		reklamation.setMandantCNr(reklamationDto.getMandantCNr());
		reklamation.setCNr(reklamationDto.getCNr());
		reklamation.setReklamationartCNr(reklamationDto.getReklamationartCNr());
		reklamation.setTBelegdatum(reklamationDto.getTBelegdatum());
		reklamation.setKostenstelleIId(reklamationDto.getKostenstelleIId());
		reklamation.setFehlerangabeIId(reklamationDto.getFehlerangabeIId());
		reklamation.setAufnahmeartIId(reklamationDto.getAufnahmeartIId());
		reklamation.setPersonalIIdAufnehmer(reklamationDto
				.getPersonalIIdAufnehmer());
		reklamation.setBArtikel(reklamationDto.getBArtikel());
		reklamation.setArtikelIId(reklamationDto.getArtikelIId());
		reklamation.setCHandartikel(reklamationDto.getCHandartikel());
		reklamation.setNMenge(reklamationDto.getNMenge());
		reklamation.setBFremdprodukt(reklamationDto.getBFremdprodukt());
		reklamation.setCGrund(reklamationDto.getCGrund());
		reklamation.setLosIId(reklamationDto.getLosIId());
		reklamation.setBestellungIId(reklamationDto.getBestellungIId());
		reklamation.setLieferscheinIId(reklamationDto.getLieferscheinIId());
		reklamation.setRechnungIId(reklamationDto.getRechnungIId());
		reklamation.setKundeIId(reklamationDto.getKundeIId());
		reklamation.setLieferantIId(reklamationDto.getLieferantIId());
		reklamation.setAnsprechpartnerIId(reklamationDto
				.getAnsprechpartnerIId());
		reklamation.setAnsprechpartnerIIdLieferant(reklamationDto
				.getAnsprechpartnerIIdLieferant());
		reklamation.setCProjekt(reklamationDto.getCProjekt());
		reklamation.setCTelansprechpartner(reklamationDto
				.getCTelansprechpartner());
		reklamation.setCTelansprechpartnerLieferant(reklamationDto
				.getCTelansprechpartnerLieferant());
		reklamation.setXAnalyse(reklamationDto.getXAnalyse());
		reklamation.setXKommentar(reklamationDto.getXKommentar());
		reklamation.setFehlerIId(reklamationDto.getFehlerIId());
		reklamation.setBBerechtigt(reklamationDto.getBBerechtigt());
		reklamation.setSchwereIId(reklamationDto.getSchwereIId());
		reklamation.setBehandlungIId(reklamationDto.getBehandlungIId());
		reklamation.setPersonalIIdRuecksprache(reklamationDto
				.getPersonalIIdRuecksprache());
		reklamation.setPersonalIIdErledigt(reklamationDto
				.getPersonalIIdErledigt());
		reklamation.setTRuecksprache(reklamationDto.getTRuecksprache());
		reklamation.setTErledigt(reklamationDto.getTErledigt());
		reklamation.setCRuecksprachemit(reklamationDto.getCRuecksprachemit());
		reklamation.setNKostenmaterial(reklamationDto.getNKostenmaterial());
		reklamation.setNKostenarbeitszeit(reklamationDto
				.getNKostenarbeitszeit());
		reklamation.setMassnahmeIIdKurz(reklamationDto.getMassnahmeIIdKurz());
		reklamation.setWareneingangIId(reklamationDto.getWareneingangIId());
		reklamation.setTMassnahmebiskurz(reklamationDto.getTMassnahmebiskurz());
		reklamation.setTEingefuehrtkurz(reklamationDto.getTEingefuehrtkurz());
		reklamation.setPersonalIIdEingefuehrtkurz(reklamationDto
				.getPersonalIIdEingefuehrtkurz());
		reklamation.setMassnahmeIIdMittel(reklamationDto
				.getMassnahmeIIdMittel());
		reklamation.setTMassnahmebismittel(reklamationDto
				.getTMassnahmebismittel());
		reklamation.setTEingefuehrtmittel(reklamationDto
				.getTEingefuehrtmittel());
		reklamation.setPersonalIIdEingefuehrtmittel(reklamationDto
				.getPersonalIIdEingefuehrtmittel());
		reklamation.setMassnahmeIIdLang(reklamationDto.getMassnahmeIIdLang());
		reklamation.setTMassnahmebislang(reklamationDto.getTMassnahmebislang());
		reklamation.setTEingefuehrtlang(reklamationDto.getTEingefuehrtlang());
		reklamation.setPersonalIIdEingefuehrtlang(reklamationDto
				.getPersonalIIdEingefuehrtlang());
		reklamation.setPersonalIIdAnlegen(reklamationDto
				.getPersonalIIdAnlegen());
		reklamation.setTAnlegen(reklamationDto.getTAnlegen());
		reklamation.setPersonalIIdAendern(reklamationDto
				.getPersonalIIdAendern());
		reklamation.setTAendern(reklamationDto.getTAendern());

		reklamation.setWirksamkeitIId(reklamationDto.getWirksamkeitIId());
		reklamation.setXWirksamkeit(reklamationDto.getXWirksamkeit());
		reklamation.setXMassnahmeKurz(reklamationDto.getXMassnahmeKurz());
		reklamation.setXMassnahmeMittel(reklamationDto.getXMassnahmeMittel());
		reklamation.setXMassnahmeLang(reklamationDto.getXMassnahmeLang());

		reklamation.setBBetrifftgelieferte(reklamationDto
				.getBBetrifftgelieferte());
		reklamation.setBBetrifftlagerstand(reklamationDto
				.getBBetrifftlagerstand());
		reklamation.setNStueckgelieferte(reklamationDto.getNStueckgelieferte());
		reklamation.setNStuecklagerstand(reklamationDto.getNStuecklagerstand());

		reklamation.setXGrundLang(reklamationDto.getXGrundLang());
		reklamation.setTWirksamkeitbis(reklamationDto.getTWirksamkeitbis());
		reklamation.setTWirksamkeiteingefuehrt(reklamationDto
				.getTWirksamkeiteingefuehrt());
		reklamation.setPersonalIIdWirksamkeit(reklamationDto
				.getPersonalIIdWirksamkeit());
		reklamation.setCKdlsnr(reklamationDto.getCKdlsnr());
		reklamation.setCKdreklanr(reklamationDto.getCKdreklanr());
		reklamation.setMaschineIId(reklamationDto.getMaschineIId());
		reklamation.setPersonalIIdVerursacher(reklamationDto
				.getPersonalIIdVerursacher());
		reklamation.setLossollarbeitsplanIId(reklamationDto
				.getLossollarbeitsplanIId());
		reklamation.setCSeriennrchargennr(reklamationDto
				.getCSeriennrchargennr());
		reklamation.setIKundeunterart(reklamationDto.getIKundeunterart());

		em.merge(reklamation);
		em.flush();
	}

	private ReklamationDto assembleReklamationDto(Reklamation reklamation) {
		return ReklamationDtoAssembler.createDto(reklamation);
	}

	private ReklamationDto[] assembleReklamationDtos(Collection<?> reklamations) {
		List<ReklamationDto> list = new ArrayList<ReklamationDto>();
		if (reklamations != null) {
			Iterator<?> iterator = reklamations.iterator();
			while (iterator.hasNext()) {
				Reklamation reklamation = (Reklamation) iterator.next();
				list.add(assembleReklamationDto(reklamation));
			}
		}
		ReklamationDto[] returnArray = new ReklamationDto[list.size()];
		return (ReklamationDto[]) list.toArray(returnArray);
	}

	public Integer createFehlerangabe(FehlerangabeDto fehlerangabeDto,
			TheClientDto theClientDto) {
		if (fehlerangabeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerangabeDto == null"));
		}
		if (fehlerangabeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("fehlerangabeDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("FehlerangabefindByCBez");
			query.setParameter(1, fehlerangabeDto.getCBez());
			Fehlerangabe doppelt = (Fehlerangabe) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_FEHLERANGABE.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FEHLERANGABE);
			fehlerangabeDto.setIId(pk);

			Fehlerangabe fehlerangabe = new Fehlerangabe(
					fehlerangabeDto.getIId(), fehlerangabeDto.getCBez());
			em.persist(fehlerangabe);
			em.flush();
			setFehlerangabeFromFehlerangabeDto(fehlerangabe, fehlerangabeDto);

			if (fehlerangabeDto.getFehlerangabesprDto() != null) {
				Fehlerangabespr spr = new Fehlerangabespr(
						theClientDto.getLocUiAsString(),
						fehlerangabeDto.getIId());
				spr.setCBez(fehlerangabeDto.getFehlerangabesprDto().getCBez());
				em.persist(spr);
				em.flush();

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return fehlerangabeDto.getIId();
	}

	public void removeFehlerangabe(FehlerangabeDto fehlerangabeDto)
			throws EJBExceptionLP {
		if (fehlerangabeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerangabeDto == null"));
		}
		if (fehlerangabeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("fehlerangabeDto.getIId() == null"));
		}

		Integer iId = fehlerangabeDto.getIId();

		try {
			Query query = em
					.createNamedQuery("FehlerangabesprfindByFehlerangabeIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Fehlerangabespr sprTemp = (Fehlerangabespr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Fehlerangabe toRemove = em.find(Fehlerangabe.class, iId);
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

	public void updateFehlerangabe(FehlerangabeDto fehlerangabeDto,
			TheClientDto theClientDto) {
		if (fehlerangabeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerangabeDto == null"));
		}
		if (fehlerangabeDto.getIId() == null
				|| fehlerangabeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"fehlerangabeDto.getIId() == null || fehlerangabeDto.getCBez() == null"));
		}

		Integer iId = fehlerangabeDto.getIId();

		Fehlerangabe fehlerangabe = em.find(Fehlerangabe.class, iId);
		if (fehlerangabe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("FehlerangabefindByCBez");
			query.setParameter(1, fehlerangabeDto.getCBez());
			Integer iIdVorhanden = ((Fehlerangabe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_FEHLERANGABE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setFehlerangabeFromFehlerangabeDto(fehlerangabe, fehlerangabeDto);
		if (fehlerangabeDto.getFehlerangabesprDto() != null) {

			Fehlerangabespr spr = em
					.find(Fehlerangabespr.class, new FehlerangabesprPK(
							theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				try {
					spr = new Fehlerangabespr(theClientDto.getLocUiAsString(),
							iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex7);

				}
			}
			spr.setCBez(fehlerangabeDto.getFehlerangabesprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	public Map getAllReklamationart() {

		myLogger.entry();
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("ReklamationartfindAll");
		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, null);
		// }

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Reklamationart reklamationartTemp = (Reklamationart) itArten.next();
			Object key = reklamationartTemp.getCNr();
			Object value = reklamationartTemp.getCNr();
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

		return tmArten;
	}

	public void reklamationErledigenOderAufheben(Integer reklamationIId,
			Integer behandlungIId, TheClientDto theClientDto) {
		// try {
		Reklamation reklamation = em.find(Reklamation.class, reklamationIId);
		if (reklamation == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (reklamation.getTErledigt() == null) {
			reklamation.setPersonalIIdErledigt(theClientDto.getIDPersonal());
			reklamation.setTErledigt(new Timestamp(System.currentTimeMillis()));
			reklamation.setBehandlungIId(behandlungIId);
			reklamation.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
		} else {
			reklamation.setTErledigt(null);
			reklamation.setPersonalIIdErledigt(null);
			reklamation.setBehandlungIId(null);
			reklamation.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, e);
		// }
	}

	public FehlerangabeDto fehlerangabeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Fehlerangabe fehlerangabe = em.find(Fehlerangabe.class, iId);
		if (fehlerangabe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		FehlerangabeDto dto = assembleFehlerangabeDto(fehlerangabe);

		Fehlerangabespr spr = em.find(Fehlerangabespr.class,
				new FehlerangabesprPK(theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Fehlerangabespr.class, new FehlerangabesprPK(
					theClientDto.getLocKonzernAsString(), iId));

		}
		FehlerangabesprDto sprDto = null;
		if (spr != null) {
			sprDto = FehlerangabesprDtoAssembler.createDto(spr);
		}
		dto.setFehlerangabesprDto(sprDto);
		return dto;

	}

	private void setFehlerangabeFromFehlerangabeDto(Fehlerangabe fehlerangabe,
			FehlerangabeDto fehlerangabeDto) {
		fehlerangabe.setCBez(fehlerangabeDto.getCBez());
		em.merge(fehlerangabe);
		em.flush();
	}

	private FehlerangabeDto assembleFehlerangabeDto(Fehlerangabe fehlerangabe) {
		return FehlerangabeDtoAssembler.createDto(fehlerangabe);
	}

	private FehlerangabeDto[] assembleFehlerangabeDtos(
			Collection<?> fehlerangabes) {
		List<FehlerangabeDto> list = new ArrayList<FehlerangabeDto>();
		if (fehlerangabes != null) {
			Iterator<?> iterator = fehlerangabes.iterator();
			while (iterator.hasNext()) {
				Fehlerangabe fehlerangabe = (Fehlerangabe) iterator.next();
				list.add(assembleFehlerangabeDto(fehlerangabe));
			}
		}
		FehlerangabeDto[] returnArray = new FehlerangabeDto[list.size()];
		return (FehlerangabeDto[]) list.toArray(returnArray);
	}

	public Integer createAufnahmeart(AufnahmeartDto aufnahmeartDto,
			TheClientDto theClientDto) {
		if (aufnahmeartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("aufnahmeartDto == null"));
		}
		if (aufnahmeartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("aufnahmeartDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("AufnahmeartfindByCBez");
			query.setParameter(1, aufnahmeartDto.getCBez());
			Aufnahmeart doppelt = (Aufnahmeart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_AUFNAHMEART.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUFNAHMEART);
			aufnahmeartDto.setIId(pk);

			Aufnahmeart aufnahmeart = new Aufnahmeart(aufnahmeartDto.getIId(),
					aufnahmeartDto.getCBez());
			em.persist(aufnahmeart);
			em.flush();
			setAufnahmeartFromAufnahmeartDto(aufnahmeart, aufnahmeartDto);

			if (aufnahmeartDto.getAufnahmeartsprDto() != null) {
				Aufnahmeartspr spr = new Aufnahmeartspr(
						theClientDto.getLocUiAsString(),
						aufnahmeartDto.getIId());
				spr.setCBez(aufnahmeartDto.getAufnahmeartsprDto().getCBez());
				em.persist(spr);
				em.flush();

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return aufnahmeartDto.getIId();
	}

	public void removeAufnahmeart(AufnahmeartDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Integer iId = dto.getIId();

		try {
			Query query = em
					.createNamedQuery("AufnahmeartsprfindByAufnahmeartIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Aufnahmeartspr sprTemp = (Aufnahmeartspr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Aufnahmeart toRemove = em.find(Aufnahmeart.class, iId);
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

	public void updateAufnahmeart(AufnahmeartDto aufnahmeartDto,
			TheClientDto theClientDto) {
		if (aufnahmeartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("aufnahmeartDto == null"));
		}
		if (aufnahmeartDto.getIId() == null || aufnahmeartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"aufnahmeartDto.getIId() == null || aufnahmeartDto.getCBez() == null"));
		}

		Integer iId = aufnahmeartDto.getIId();
		// try {
		Aufnahmeart aufnahmeart = em.find(Aufnahmeart.class, iId);
		if (aufnahmeart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("AufnahmeartfindByCBez");
			query.setParameter(1, aufnahmeartDto.getCBez());
			Integer iIdVorhanden = ((Aufnahmeart) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_AUFNAHMEART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setAufnahmeartFromAufnahmeartDto(aufnahmeart, aufnahmeartDto);
		if (aufnahmeartDto.getAufnahmeartsprDto() != null) {

			Aufnahmeartspr aufnahmeartspr = em.find(Aufnahmeartspr.class,
					new AufnahmeartsprPK(theClientDto.getLocUiAsString(), iId));
			if (aufnahmeartspr == null) {
				try {
					aufnahmeartspr = new Aufnahmeartspr(
							theClientDto.getLocUiAsString(), iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex7);

				}
			}
			aufnahmeartspr.setCBez(aufnahmeartDto.getAufnahmeartsprDto()
					.getCBez());
			em.persist(aufnahmeartspr);
			em.flush();
		}

	}

	public AufnahmeartDto aufnahmeartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Aufnahmeart aufnahmeart = em.find(Aufnahmeart.class, iId);
		if (aufnahmeart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		AufnahmeartDto dto = assembleAufnahmeartDto(aufnahmeart);

		Aufnahmeartspr spr = em.find(Aufnahmeartspr.class,
				new AufnahmeartsprPK(theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Aufnahmeartspr.class, new AufnahmeartsprPK(
					theClientDto.getLocKonzernAsString(), iId));

		}
		AufnahmeartsprDto sprDto = null;
		if (spr != null) {
			sprDto = AufnahmeartsprDtoAssembler.createDto(spr);
		}
		dto.setAufnahmeartsprDto(sprDto);
		return dto;

	}

	private void setAufnahmeartFromAufnahmeartDto(Aufnahmeart aufnahmeart,
			AufnahmeartDto aufnahmeartDto) {
		aufnahmeart.setCBez(aufnahmeartDto.getCBez());
		em.merge(aufnahmeart);
		em.flush();
	}

	private AufnahmeartDto assembleAufnahmeartDto(Aufnahmeart aufnahmeart) {
		return AufnahmeartDtoAssembler.createDto(aufnahmeart);
	}

	private AufnahmeartDto[] assembleAufnahmeartDtos(Collection<?> aufnahmearts) {
		List<AufnahmeartDto> list = new ArrayList<AufnahmeartDto>();
		if (aufnahmearts != null) {
			Iterator<?> iterator = aufnahmearts.iterator();
			while (iterator.hasNext()) {
				Aufnahmeart aufnahmeart = (Aufnahmeart) iterator.next();
				list.add(assembleAufnahmeartDto(aufnahmeart));
			}
		}
		AufnahmeartDto[] returnArray = new AufnahmeartDto[list.size()];
		return (AufnahmeartDto[]) list.toArray(returnArray);
	}

	public Integer createWirksamkeit(WirksamkeitDto wirksamkeitDto,
			TheClientDto theClientDto) {
		if (wirksamkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wirksamkeitDto == null"));
		}
		if (wirksamkeitDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("wirksamkeitDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("WirksamkeitfindByCBez");
			query.setParameter(1, wirksamkeitDto.getCBez());
			Wirksamkeit doppelt = (Wirksamkeit) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_WIRKSAMKEIT.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_WIRKSAMKEIT);
			wirksamkeitDto.setIId(pk);

			Wirksamkeit wirksamkeit = new Wirksamkeit(wirksamkeitDto.getIId(),
					wirksamkeitDto.getCBez());
			em.persist(wirksamkeit);
			em.flush();
			setWirksamkeitFromWirksamkeitDto(wirksamkeit, wirksamkeitDto);
			if (wirksamkeitDto.getWirksamkeitsprDto() != null) {
				Wirksamkeitspr spr = new Wirksamkeitspr(
						theClientDto.getLocUiAsString(),
						wirksamkeitDto.getIId());
				spr.setCBez(wirksamkeitDto.getWirksamkeitsprDto().getCBez());
				em.persist(spr);
				em.flush();

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return wirksamkeitDto.getIId();
	}

	public void removeWirksamkeit(WirksamkeitDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dto.getIId() == null"));
		}

		Integer iId = dto.getIId();

		try {
			Query query = em
					.createNamedQuery("WirksamkeitsprfindByWirksamkeitIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Wirksamkeitspr sprTemp = (Wirksamkeitspr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Wirksamkeit toRemove = em.find(Wirksamkeit.class, iId);
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

	public void updateWirksamkeit(WirksamkeitDto wirksamkeitDto,
			TheClientDto theClientDto) {
		if (wirksamkeitDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("wirksamkeitDto == null"));
		}
		if (wirksamkeitDto.getIId() == null || wirksamkeitDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"wirksamkeitDto.getIId() == null || wirksamkeitDto.getCBez() == null"));
		}

		Integer iId = wirksamkeitDto.getIId();

		Wirksamkeit wirksamkeit = em.find(Wirksamkeit.class, iId);
		if (wirksamkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("WirksamkeitfindByCBez");
			query.setParameter(1, wirksamkeitDto.getCBez());
			Integer iIdVorhanden = ((Wirksamkeit) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_AUFNAHMEART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setWirksamkeitFromWirksamkeitDto(wirksamkeit, wirksamkeitDto);

		if (wirksamkeitDto.getWirksamkeitsprDto() != null) {

			Wirksamkeitspr spr = em.find(Wirksamkeitspr.class,
					new WirksamkeitsprPK(theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				spr = new Wirksamkeitspr(theClientDto.getLocUiAsString(), iId);
			}
			spr.setCBez(wirksamkeitDto.getWirksamkeitsprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	public WirksamkeitDto wirksamkeitFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Wirksamkeit wirksamkeit = em.find(Wirksamkeit.class, iId);
		if (wirksamkeit == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		WirksamkeitDto dto = assembleWirksamkeitDto(wirksamkeit);

		Wirksamkeitspr spr = em.find(Wirksamkeitspr.class,
				new WirksamkeitsprPK(theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Wirksamkeitspr.class, new WirksamkeitsprPK(
					theClientDto.getLocKonzernAsString(), iId));

		}
		WirksamkeitsprDto sprDto = null;
		if (spr != null) {
			sprDto = WirksamkeitsprDtoAssembler.createDto(spr);
		}
		dto.setWirksamkeitsprDto(sprDto);
		return dto;

	}

	private void setWirksamkeitFromWirksamkeitDto(Wirksamkeit wirksamkeit,
			WirksamkeitDto wirksamkeitDto) {
		wirksamkeit.setCBez(wirksamkeitDto.getCBez());
		em.merge(wirksamkeit);
		em.flush();
	}

	private WirksamkeitDto assembleWirksamkeitDto(Wirksamkeit wirksamkeit) {
		return WirksamkeitDtoAssembler.createDto(wirksamkeit);
	}

	private WirksamkeitDto[] assembleWirksamkeitDtos(Collection<?> wirksamkeits) {
		List<WirksamkeitDto> list = new ArrayList<WirksamkeitDto>();
		if (wirksamkeits != null) {
			Iterator<?> iterator = wirksamkeits.iterator();
			while (iterator.hasNext()) {
				Wirksamkeit aufnahmeart = (Wirksamkeit) iterator.next();
				list.add(assembleWirksamkeitDto(aufnahmeart));
			}
		}
		WirksamkeitDto[] returnArray = new WirksamkeitDto[list.size()];
		return (WirksamkeitDto[]) list.toArray(returnArray);
	}

	public Integer createFehler(FehlerDto fehlerDto, TheClientDto theClientDto) {
		if (fehlerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerDto == null"));
		}
		if (fehlerDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("fehlerDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("FehlerfindByCBez");
			query.setParameter(1, fehlerDto.getCBez());
			Fehler doppelt = (Fehler) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_FEHLER.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FEHLER);
			fehlerDto.setIId(pk);

			Fehler fehler = new Fehler(fehlerDto.getIId(), fehlerDto.getCBez());
			em.persist(fehler);
			em.flush();
			setFehlerFromFehlerDto(fehler, fehlerDto);

			if (fehlerDto.getFehlersprDto() != null) {
				Fehlerspr spr = new Fehlerspr(theClientDto.getLocUiAsString(),
						fehlerDto.getIId());
				spr.setCBez(fehlerDto.getFehlersprDto().getCBez());
				em.persist(spr);
				em.flush();

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return fehlerDto.getIId();
	}

	public void removeFehler(FehlerDto fehlerDto) throws EJBExceptionLP {
		if (fehlerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerDto == null"));
		}
		if (fehlerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("fehlerDto.getIId() == null"));
		}
		// try {
		Integer iId = fehlerDto.getIId();

		try {
			Query query = em.createNamedQuery("FehlersprfindByFehlerIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Fehlerspr sprTemp = (Fehlerspr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Fehler toRemove = em.find(Fehler.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateFehler(FehlerDto fehlerDto, TheClientDto theClientDto) {
		if (fehlerDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerDto == null"));
		}
		if (fehlerDto.getIId() == null || fehlerDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"fehlerDto.getIId() == null || fehlerDto.getCBez() == null"));
		}

		Integer iId = fehlerDto.getIId();
		Fehler fehler = em.find(Fehler.class, iId);
		if (fehler == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("FehlerfindByCBez");
			query.setParameter(1, fehlerDto.getCBez());
			Integer iIdVorhanden = ((Fehler) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_FEHLER.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setFehlerFromFehlerDto(fehler, fehlerDto);
		if (fehlerDto.getFehlersprDto() != null) {

			Fehlerspr spr = em.find(Fehlerspr.class, new FehlersprPK(
					theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				try {
					spr = new Fehlerspr(theClientDto.getLocUiAsString(), iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex7);

				}
			}
			spr.setCBez(fehlerDto.getFehlersprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	public FehlerDto fehlerFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {

		Fehler fehler = em.find(Fehler.class, iId);
		if (fehler == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		FehlerDto dto = assembleFehlerDto(fehler);

		Fehlerspr spr = em.find(Fehlerspr.class,
				new FehlersprPK(theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Fehlerspr.class,
					new FehlersprPK(theClientDto.getLocKonzernAsString(), iId));

		}
		FehlersprDto sprDto = null;
		if (spr != null) {
			sprDto = FehlersprDtoAssembler.createDto(spr);
		}
		dto.setFehlersprDto(sprDto);
		return dto;

	}

	private void setFehlerFromFehlerDto(Fehler fehler, FehlerDto fehlerDto) {
		fehler.setCBez(fehlerDto.getCBez());
		em.merge(fehler);
		em.flush();
	}

	private FehlerDto assembleFehlerDto(Fehler fehler) {
		return FehlerDtoAssembler.createDto(fehler);
	}

	private FehlerDto[] assembleFehlerDtos(Collection<?> fehlers) {
		List<FehlerDto> list = new ArrayList<FehlerDto>();
		if (fehlers != null) {
			Iterator<?> iterator = fehlers.iterator();
			while (iterator.hasNext()) {
				Fehler fehler = (Fehler) iterator.next();
				list.add(assembleFehlerDto(fehler));
			}
		}
		FehlerDto[] returnArray = new FehlerDto[list.size()];
		return (FehlerDto[]) list.toArray(returnArray);
	}

	public Integer createReklamationbild(ReklamationbildDto reklamationbildDto) {
		if (reklamationbildDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fehlerDto == null"));
		}
		if (reklamationbildDto.getOBild() == null
				|| reklamationbildDto.getReklamationIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationbildDto.getOBild() == null || reklamationbildDto.getReklamationIId() == null"));
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_REKLAMATIONBILD);
			reklamationbildDto.setIId(pk);

			Reklamationbild reklamationbild = new Reklamationbild(
					reklamationbildDto.getIId(),
					reklamationbildDto.getReklamationIId(),
					reklamationbildDto.getISort(),
					reklamationbildDto.getOBild());
			em.persist(reklamationbild);
			em.flush();
			setReklamationbildFromReklamationbildDto(reklamationbild,
					reklamationbildDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return reklamationbildDto.getIId();
	}

	public void removeReklamationbild(ReklamationbildDto reklamationbildDto) {
		if (reklamationbildDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationbildDto == null"));
		}
		if (reklamationbildDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationbildDto.getIId() == null"));
		}
		// try {
		Integer iId = reklamationbildDto.getIId();
		Reklamationbild toRemove = em.find(Reklamationbild.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateReklamationbild(ReklamationbildDto reklamationbildDto) {
		if (reklamationbildDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationbildDto == null"));
		}
		if (reklamationbildDto.getIId() == null
				|| reklamationbildDto.getISort() == null
				|| reklamationbildDto.getOBild() == null
				|| reklamationbildDto.getReklamationIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"reklamationbildDto.getIId() == null || reklamationbildDto.getISort() == null || reklamationbildDto.getOBild() == null || reklamationbildDto.getReklamationIId() == null"));
		}

		Integer iId = reklamationbildDto.getIId();

		Reklamationbild reklamationbild = em.find(Reklamationbild.class, iId);
		if (reklamationbild == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setReklamationbildFromReklamationbildDto(reklamationbild,
				reklamationbildDto);

	}

	public ReklamationbildDto reklamationbildFindByPrimaryKey(Integer iId) {
		// try {
		Reklamationbild reklamationbild = em.find(Reklamationbild.class, iId);
		if (reklamationbild == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleReklamationbildDto(reklamationbild);

	}

	public ReklamationbildDto[] reklamationbildFindByReklamationIId(
			Integer reklamationIId) {
		// try {
		Query query = em
				.createNamedQuery("ReklamationbildfindByReklamationIId");
		query.setParameter(1, reklamationIId);
		Collection<?> cl = query.getResultList();
		return assembleReklamationbildDtos(cl);

	}

	private void setReklamationbildFromReklamationbildDto(
			Reklamationbild reklamationbild,
			ReklamationbildDto reklamationbildDto) {
		reklamationbild.setCBez(reklamationbildDto.getCBez());
		reklamationbild.setISort(reklamationbildDto.getISort());
		reklamationbild.setOBild(reklamationbildDto.getOBild());
		reklamationbild.setReklamationIId(reklamationbildDto
				.getReklamationIId());
		em.merge(reklamationbild);
		em.flush();
	}

	private ReklamationbildDto assembleReklamationbildDto(
			Reklamationbild reklamationbild) {
		return ReklamationbildDtoAssembler.createDto(reklamationbild);
	}

	private ReklamationbildDto[] assembleReklamationbildDtos(
			Collection<?> fehlers) {
		List<ReklamationbildDto> list = new ArrayList<ReklamationbildDto>();
		if (fehlers != null) {
			Iterator<?> iterator = fehlers.iterator();
			while (iterator.hasNext()) {
				Reklamationbild fehler = (Reklamationbild) iterator.next();
				list.add(assembleReklamationbildDto(fehler));
			}
		}
		ReklamationbildDto[] returnArray = new ReklamationbildDto[list.size()];
		return (ReklamationbildDto[]) list.toArray(returnArray);
	}

	public Integer createMassnahme(MassnahmeDto massnahmeDto,
			TheClientDto theClientDto) {
		if (massnahmeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("massnahmeDto == null"));
		}
		if (massnahmeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("massnahmeDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("MassnahmefindByCBez");
			query.setParameter(1, massnahmeDto.getCBez());
			Massnahme doppelt = (Massnahme) query.getSingleResult();
			if (massnahme != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_MASSNAHME.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MASSNAHME);
			massnahmeDto.setIId(pk);

			Massnahme massnahme = new Massnahme(massnahmeDto.getIId(),
					massnahmeDto.getCBez());
			em.persist(massnahme);
			em.flush();
			setMassnahmeFromMassnahmeDto(massnahme, massnahmeDto);

			if (massnahmeDto.getMassnahmesprDto() != null) {
				Massnahmespr spr = new Massnahmespr(
						theClientDto.getLocUiAsString(), massnahmeDto.getIId());
				spr.setCBez(massnahmeDto.getMassnahmesprDto().getCBez());
				em.persist(spr);
				em.flush();

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return massnahmeDto.getIId();
	}

	public void removeMassnahme(MassnahmeDto massnahmeDto)
			throws EJBExceptionLP {
		if (massnahmeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("massnahmeDto == null"));
		}
		if (massnahmeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("massnahmeDto.getIId() == null"));
		}
		Integer iId = massnahmeDto.getIId();

		try {
			Query query = em.createNamedQuery("MassnahmesprfindByMassnahmeIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Massnahmespr sprTemp = (Massnahmespr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Massnahme toRemove = em.find(Massnahme.class, iId);
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

	public void updateMassnahme(MassnahmeDto massnahmeDto,
			TheClientDto theClientDto) {
		if (massnahmeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("massnahmeDto == null"));
		}
		if (massnahmeDto.getIId() == null || massnahmeDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"massnahmeDto.getIId() == null || massnahmeDto.getCBez() == null"));
		}

		Integer iId = massnahmeDto.getIId();
		// try {
		Massnahme massnahme = em.find(Massnahme.class, iId);
		if (massnahme == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("MassnahmefindByCBez");
			query.setParameter(1, massnahmeDto.getCBez());
			Integer iIdVorhanden = ((Massnahme) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_MASSNAHME.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setMassnahmeFromMassnahmeDto(massnahme, massnahmeDto);
		if (massnahmeDto.getMassnahmesprDto() != null) {

			Massnahmespr spr = em.find(Massnahmespr.class, new MassnahmesprPK(
					theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				try {
					spr = new Massnahmespr(theClientDto.getLocUiAsString(), iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex7);

				}
			}
			spr.setCBez(massnahmeDto.getMassnahmesprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	/**
	 * Es wird nur X_KOMMENTAR wird aktualisiert 
	 * 
	 * @param reklamationDto 
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateReklamationKommentar(ReklamationDto reklamationDto,
			TheClientDto theClientDto) {
		if (reklamationDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("reklamationDto == null"));
		}
		if (reklamationDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("reklamationDto.getIId() == null"));
		}

		Reklamation reklamation = em.find(Reklamation.class,
				reklamationDto.getIId());
		if (reklamation == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		reklamation.setXKommentar(reklamationDto.getXKommentar());
	}

	public MassnahmeDto massnahmeFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Massnahme massnahme = em.find(Massnahme.class, iId);
		if (massnahme == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		MassnahmeDto dto = assembleMassnahmeDto(massnahme);

		Massnahmespr spr = em.find(Massnahmespr.class, new MassnahmesprPK(
				theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Massnahmespr.class,
					new MassnahmesprPK(theClientDto.getLocKonzernAsString(),
							iId));

		}
		MassnahmesprDto sprDto = null;
		if (spr != null) {
			sprDto = MassnahmesprDtoAssembler.createDto(spr);
		}
		dto.setMassnahmesprDto(sprDto);
		return dto;

	}

	private void setMassnahmeFromMassnahmeDto(Massnahme massnahme,
			MassnahmeDto massnahmeDto) {
		massnahme.setCBez(massnahmeDto.getCBez());
		em.merge(massnahme);
		em.flush();
	}

	private MassnahmeDto assembleMassnahmeDto(Massnahme massnahme) {
		return MassnahmeDtoAssembler.createDto(massnahme);
	}

	private MassnahmeDto[] assembleMassnahmeDtos(Collection<?> massnahmes) {
		List<MassnahmeDto> list = new ArrayList<MassnahmeDto>();
		if (massnahmes != null) {
			Iterator<?> iterator = massnahmes.iterator();
			while (iterator.hasNext()) {
				Massnahme massnahme = (Massnahme) iterator.next();
				list.add(assembleMassnahmeDto(massnahme));
			}
		}
		MassnahmeDto[] returnArray = new MassnahmeDto[list.size()];
		return (MassnahmeDto[]) list.toArray(returnArray);
	}

	public Integer createSchwere(SchwereDto schwereDto) throws EJBExceptionLP {
		if (schwereDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schwereDto == null"));
		}
		if (schwereDto.getCNr() == null || schwereDto.getCBez() == null
				|| schwereDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"schwereDto.getCNr() == null || schwereDto.getCBez() == null || schwereDto.getIPunkte() == null"));
		}
		try {
			Query query = em.createNamedQuery("SchwerefindByCNr");
			query.setParameter(1, schwereDto.getCNr());
			Schwere doppelt = (Schwere) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_SCHWERE.CNR"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_SCHWERE);
			schwereDto.setIId(pk);

			Schwere schwere = new Schwere(schwereDto.getIId(),
					schwereDto.getCNr(), schwereDto.getCBez(),
					schwereDto.getIPunkte());
			em.persist(schwere);
			em.flush();
			setSchwereFromSchwereDto(schwere, schwereDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return schwereDto.getIId();

	}

	public void removeSchwere(SchwereDto schwereDto) throws EJBExceptionLP {
		if (schwereDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schwereDto == null"));
		}
		if (schwereDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("schwereDto.getIId() == null"));
		}
		// try {
		Integer iId = schwereDto.getIId();
		Schwere toRemove = em.find(Schwere.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateSchwere(SchwereDto schwereDto) throws EJBExceptionLP {
		if (schwereDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schwereDto == null"));
		}
		if (schwereDto.getIId() == null || schwereDto.getCNr() == null
				|| schwereDto.getCBez() == null
				|| schwereDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"schwereDto.getIId() == null || schwereDto.getCNr() == null || schwereDto.getCBez() == null || schwereDto.getIPunkte() == null"));
		}

		Integer iId = schwereDto.getIId();
		// try {
		Schwere schwere = em.find(Schwere.class, iId);
		if (schwere == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("SchwerefindByCNr");
			query.setParameter(1, schwereDto.getCNr());
			Integer iIdVorhanden = ((Schwere) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_SCHWERE.CNR"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setSchwereFromSchwereDto(schwere, schwereDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public SchwereDto schwereFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Schwere schwere = em.find(Schwere.class, iId);
		if (schwere == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSchwereDto(schwere);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setSchwereFromSchwereDto(Schwere schwere, SchwereDto schwereDto) {
		schwere.setCNr(schwereDto.getCNr());
		schwere.setCBez(schwereDto.getCBez());
		schwere.setIPunkte(schwereDto.getIPunkte());
		em.merge(schwere);
		em.flush();
	}

	private SchwereDto assembleSchwereDto(Schwere schwere) {
		return SchwereDtoAssembler.createDto(schwere);
	}

	private SchwereDto[] assembleSchwereDtos(Collection<?> schweres) {
		List<SchwereDto> list = new ArrayList<SchwereDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				Schwere schwere = (Schwere) iterator.next();
				list.add(assembleSchwereDto(schwere));
			}
		}
		SchwereDto[] returnArray = new SchwereDto[list.size()];
		return (SchwereDto[]) list.toArray(returnArray);
	}

	public void removeReklamation(Integer iId) {
		// try {
		Reklamation toRemove = em.find(Reklamation.class, iId);
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
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}
	
	public ReklamationDto reklamationFindByCNrMandantCNrOhneExc(String cNr,
			String mandantCNr) {
		// try {
		Query query = em.createNamedQuery("ReklamationfindByMandantCNrCNr");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, cNr);
		try {
			Reklamation reklamation = (Reklamation) query.getSingleResult();
			return assembleReklamationDto(reklamation);
		} catch(NoResultException ex) {
			return null;
		}
	}

	public ReklamationDto reklamationFindByMandantCNrCNr(String string,
			String string1) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("ReklamationfindByMandantCNrCNr");
		query.setParameter(1, string);
		query.setParameter(2, string1);
		Reklamation reklamation = (Reklamation) query.getSingleResult();
		if (reklamation == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleReklamationDto(reklamation);
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReklamationDto[] reklamationFindByAnsprechpartnerIId(
			Integer AnsprechpartnerIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByAnsprechpartnerIId");
		query.setParameter(1, AnsprechpartnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,null);
		// }
		return assembleReklamationDtos(cl);
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ReklamationDto[] reklamationFindByWareneingangIIdMandantCNr(
			Integer wareneingangIId, TheClientDto theClientDto) {
		// try {
		Query query = em
				.createNamedQuery("ReklamationfindByWareneingangIIdMandantCNr");
		query.setParameter(1, wareneingangIId);
		query.setParameter(2, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		return assembleReklamationDtos(cl);

	}

	public Integer createBehandlung(BehandlungDto beurteilungDto) {
		if (beurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("beurteilungDto == null"));
		}
		if (beurteilungDto.getCNr() == null || beurteilungDto.getCBez() == null
				|| beurteilungDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"beurteilungDto.getCNr() == null || beurteilungDto.getCBez() == null || beurteilungDto.getIPunkte() == null"));
		}
		try {
			Query query = em.createNamedQuery("BehandlungfindByCNr");
			query.setParameter(1, beurteilungDto.getCNr());
			Behandlung doppelt = (Behandlung) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_BEURTEILUNG.CNR"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEURTEILUNG);
			beurteilungDto.setIId(pk);

			Behandlung beurteilung = new Behandlung(beurteilungDto.getIId(),
					beurteilungDto.getCNr(), beurteilungDto.getCBez(),
					beurteilungDto.getIPunkte());
			em.persist(beurteilung);
			em.flush();
			setBeurteilungFromBeurteilungDto(beurteilung, beurteilungDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return beurteilungDto.getIId();

	}

	public void removeBehandlung(BehandlungDto beurteilungDto) {
		if (beurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("schwereDto == null"));
		}
		if (beurteilungDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("schwereDto.getIId() == null"));
		}
		// try {
		Integer iId = beurteilungDto.getIId();
		Behandlung toRemove = em.find(Behandlung.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateBehandlung(BehandlungDto beurteilungDto) {
		if (beurteilungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("beurteilungDto == null"));
		}
		if (beurteilungDto.getIId() == null || beurteilungDto.getCNr() == null
				|| beurteilungDto.getCBez() == null
				|| beurteilungDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"beurteilungDto.getIId() == null || beurteilungDto.getCNr() == null || beurteilungDto.getCBez() == null || beurteilungDto.getIPunkte() == null"));
		}

		Integer iId = beurteilungDto.getIId();
		// try {
		Behandlung beurteilung = em.find(Behandlung.class, iId);
		if (beurteilung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("BehandlungfindByCNr");
			query.setParameter(1, beurteilungDto.getCNr());
			Integer iIdVorhanden = ((Behandlung) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_SCHWERE.CNR"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setBeurteilungFromBeurteilungDto(beurteilung, beurteilungDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public BehandlungDto behandlungFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Behandlung beurteilung = em.find(Behandlung.class, iId);
		if (beurteilung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBehandlungDto(beurteilung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setBeurteilungFromBeurteilungDto(Behandlung beurteilung,
			BehandlungDto beurteilungDto) {
		beurteilung.setCNr(beurteilungDto.getCNr());
		beurteilung.setCBez(beurteilungDto.getCBez());
		beurteilung.setIPunkte(beurteilungDto.getIPunkte());
		em.merge(beurteilung);
		em.flush();
	}

	private BehandlungDto assembleBehandlungDto(Behandlung beurteilung) {
		return BehandlungDtoAssembler.createDto(beurteilung);
	}

	private BehandlungDto[] assembleBehandlungDtos(Collection<?> beurteilungs) {
		List<BehandlungDto> list = new ArrayList<BehandlungDto>();
		if (beurteilungs != null) {
			Iterator<?> iterator = beurteilungs.iterator();
			while (iterator.hasNext()) {
				Behandlung beurteilung = (Behandlung) iterator.next();
				list.add(assembleBehandlungDto(beurteilung));
			}
		}
		BehandlungDto[] returnArray = new BehandlungDto[list.size()];
		return (BehandlungDto[]) list.toArray(returnArray);
	}

	public Integer createTermintreue(TermintreueDto termintreueDto) {
		if (termintreueDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("beurteilungDto == null"));
		}
		if (termintreueDto.getITage() == null
				|| termintreueDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"termintreueDto.getITage() || beurteilungDto.getIPunkte() == null"));
		}
		try {
			Query query = em.createNamedQuery("TermintreuefindByITage");
			query.setParameter(1, termintreueDto.getITage());
			Behandlung doppelt = (Behandlung) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_TERMINTREUE.CNR"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_TERMINTREUE);
			termintreueDto.setIId(pk);

			Termintreue termintreue = new Termintreue(termintreueDto.getIId(),
					termintreueDto.getITage(), termintreueDto.getIPunkte());
			em.persist(termintreue);
			em.flush();
			setTermintreueFromTermintreueDto(termintreue, termintreueDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return termintreueDto.getIId();

	}

	public void removeTermintreue(TermintreueDto termintreueDto) {
		if (termintreueDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("termintreueDto == null"));
		}
		if (termintreueDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("termintreueDto.getIId() == null"));
		}
		// try {
		Integer iId = termintreueDto.getIId();
		Termintreue toRemove = em.find(Termintreue.class, iId);
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
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void updateTermintreue(TermintreueDto termintreueDto) {
		if (termintreueDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("beurteilungDto == null"));
		}
		if (termintreueDto.getIId() == null
				|| termintreueDto.getITage() == null
				|| termintreueDto.getIPunkte() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"termintreueDto.getIId() == null || termintreueDto.getITage() == null || termintreueDto.getIPunkte() == null"));
		}

		Integer iId = termintreueDto.getIId();
		// try {
		Termintreue termintreue = em.find(Termintreue.class, iId);
		if (termintreue == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			Query query = em.createNamedQuery("TermintreuefindByITage");
			query.setParameter(1, termintreueDto.getITage());
			Integer iIdVorhanden = ((Termintreue) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"REKLA_SCHWERE.CNR"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		setTermintreueFromTermintreueDto(termintreue, termintreueDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public TermintreueDto termintreueFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Termintreue termintreue = em.find(Termintreue.class, iId);
		if (termintreue == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleTermintreueDto(termintreue);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public TermintreueDto[] termintreueFindAll() {

		Query query = em.createNamedQuery("TermintreuefindAll");
		Collection c = query.getResultList();

		return assembleTermintreueDtos(c);

	}

	private void setTermintreueFromTermintreueDto(Termintreue termintreue,
			TermintreueDto termintreueDto) {
		termintreue.setITage(termintreueDto.getITage());
		termintreue.setIPunkte(termintreueDto.getIPunkte());
		em.merge(termintreue);
		em.flush();
	}

	private TermintreueDto assembleTermintreueDto(Termintreue termintreue) {
		return TermintreueDtoAssembler.createDto(termintreue);
	}

	private TermintreueDto[] assembleTermintreueDtos(Collection<?> termintreues) {
		List<TermintreueDto> list = new ArrayList<TermintreueDto>();
		if (termintreues != null) {
			Iterator<?> iterator = termintreues.iterator();
			while (iterator.hasNext()) {
				Termintreue termintreue = (Termintreue) iterator.next();
				list.add(assembleTermintreueDto(termintreue));
			}
		}
		TermintreueDto[] returnArray = new TermintreueDto[list.size()];
		return (TermintreueDto[]) list.toArray(returnArray);
	}

	public Integer getNextReklamationbild(Integer reklamationIId) {

		try {
			Query querynext = em
					.createNamedQuery("ReklamationbildejbSelectNextReihung");
			querynext.setParameter(1, reklamationIId);

			Integer i = (Integer) querynext.getSingleResult();
			if (i != null) {
				i = 1 + 1;
			} else {
				i = 1;
			}
			return i;
		} catch (NoResultException ex) {
			return new Integer(1);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

}
