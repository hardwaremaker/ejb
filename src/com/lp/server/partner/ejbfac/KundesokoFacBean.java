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
package com.lp.server.partner.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.Kundesokomengenstaffel;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokoDtoAssembler;
import com.lp.server.partner.service.KundesokoFac;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.partner.service.KundesokomengenstaffelDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class KundesokoFacBean extends Facade implements KundesokoFac {
	@PersistenceContext
	private EntityManager em;

	// Kundensoko
	// ----------------------------------------------------------------

	/**
	 * Einen neue Kundensonderkondition anlegen.
	 * 
	 * @param kundesokoDtoI            die neue SOKO
	 * @param defaultMengenstaffelDtoI die Default Mengenstaffel fuer diese
	 *                                 Kundesoko
	 * @param theClientDto             der aktuelle Benutzer
	 * @return Integer PK der neuen SOKO
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer createKundesoko(KundesokoDto kundesokoDtoI, KundesokomengenstaffelDto defaultMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkKundesokoDto(kundesokoDtoI);
		checkKundesokoMengenstaffelDto(defaultMengenstaffelDtoI);
		Kundesoko kundesoko = null;
		try {
			// UK pruefen
			if (kundesokoDtoI.getBWirktNichtFuerPreisfindung() == null) {
				kundesokoDtoI.setBWirktNichtFuerPreisfindung(Helper.boolean2Short(false));
			}

			if (kundesokoDtoI.getArtikelIId() != null) {
				Query query = em.createNamedQuery("KundesokofindByKundeIIdArtikelIIdTPreisgueltigab");
				query.setParameter(1, kundesokoDtoI.getKundeIId());
				query.setParameter(2, kundesokoDtoI.getArtikelIId());
				query.setParameter(3, kundesokoDtoI.getTPreisgueltigab());
				// @todo getSingleResult oder getResultList ?
				kundesoko = (Kundesoko) query.getSingleResult();
			} else {
				Query query = em.createNamedQuery("KundesokofindByKundeIIdArtikelIIdTPreisgueltigab");
				query.setParameter(1, kundesokoDtoI.getKundeIId());
				query.setParameter(2, kundesokoDtoI.getArtgruIId());
				query.setParameter(3, kundesokoDtoI.getTPreisgueltigab());
				// @todo getSingleResult oder getResultList ?
				kundesoko = (Kundesoko) query.getSingleResult();
			}

			// wenn eine passenden Kundesoko gibt
			KundesokomengenstaffelDto[] aMengenstaffelDtos = kundesokomengenstaffelFindByKundesokoIIdNMenge(
					kundesoko.getIId(), defaultMengenstaffelDtoI.getNMenge());

			if (aMengenstaffelDtos != null && aMengenstaffelDtos.length > 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception());
			}
		} catch (NoResultException e) {
			// keine Kundesoko fuer Artikel(gruppe) mit dieser Gueltigkeit oder
			// keine zugehoerige Mengenstaffel
		}

		Integer kundesokoIId = new Integer(-1);

		try {
			// Daten normieren
			kundesokoDtoI.setTPreisgueltigab(Helper.cutDate(kundesokoDtoI.getTPreisgueltigab()));
			kundesokoDtoI.setTPreisgueltigbis(Helper.cutDate(kundesokoDtoI.getTPreisgueltigbis()));

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			kundesokoIId = pkGen.getNextPrimaryKey(PKConst.PK_KUNDESOKO);

			kundesokoDtoI.setIId(kundesokoIId);
			kundesokoDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			kundesokoDtoI.setTAendern(getTimestamp());

			if (kundesokoDtoI.getBKeineMengenstaffel() == null) {
				kundesokoDtoI.setBKeineMengenstaffel(Helper.boolean2Short(false));
			}

			// Datumsbereich pruefen
			checkKundesokoDto(kundesokoDtoI, defaultMengenstaffelDtoI.getNMenge(), theClientDto);

			kundesoko = new Kundesoko(kundesokoDtoI.getIId(), kundesokoDtoI.getKundeIId(),
					kundesokoDtoI.getTPreisgueltigab(), kundesokoDtoI.getPersonalIIdAendern());
			em.persist(kundesoko);
			em.flush();

			setKundesokoFromKundesokoDto(kundesoko, kundesokoDtoI);

			// die Default Mengenstaffel dazu anlegen
			// generieren von primary key
			pkGen = new PKGeneratorObj();
			Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_KUNDESOKOMENGENSTAFFEL);

			defaultMengenstaffelDtoI.setIId(iId);
			defaultMengenstaffelDtoI.setKundesokoIId(kundesokoIId);

			createKundesokomengenstaffel(defaultMengenstaffelDtoI, theClientDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return kundesokoIId;
	}

	/**
	 * Kundensonderkonditionen duerfen datumsmaessig nicht ueberlappen. Wenn eine
	 * Staffel an eine bestehende Staffel anschliesst, die kein bis-Datum hat, wird
	 * dieses automatisch ergaenzt. Wenn eine Staffel vor eine bestehende Staffel
	 * kommt und kein bis-Datum hat, dann wird dieses automatisch ergaenzt. Jede
	 * Staffel wird in dieser Art eingereiht, sodass lediglich die letzte erfasste
	 * Staffel ein offenes Ende haben kann.
	 * 
	 * @param kundesokoDtoI die zu pruefende Kundesoko
	 * @param nMengeI       die Menge
	 * @param theClientDto  der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void checkKundesokoDto(KundesokoDto kundesokoDtoI, BigDecimal nMengeI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// Schritt 1: Alle bestehenden Mengenstaffeln zu einem Artikel bzw.
		// einer Artikelgruppe holen
		KundesokoDto[] bestehendeDtos = null;

		if (kundesokoDtoI.getArtikelIId() != null) {
			bestehendeDtos = kundesokoFindByKundeIIdArtikelIId(kundesokoDtoI.getKundeIId(),
					kundesokoDtoI.getArtikelIId());
		} else {
			bestehendeDtos = kundesokoFindByKundeIIdArtgruIId(kundesokoDtoI.getKundeIId(),
					kundesokoDtoI.getArtgruIId());
		}

		// Schritt 2: durch den vorhergegangen UK ist sichergestellt, dass
		// Artikel, Menge,
		// gueltig ab eindeutig ist. Ausserdem muss sichgestellt sein, dass
		// weder gueltig ab
		// noch gueltig bis durch einen Datumsbereich abgedeckt wurde
		boolean bEnthalten = false;
		int iIndex = 0;

		while (!bEnthalten && iIndex < bestehendeDtos.length) {
			if (kundesokoDtoI.getIId().intValue() != bestehendeDtos[iIndex].getIId().intValue()) {
				bEnthalten = checkDatumInDatumsbereichEnthalten(bestehendeDtos[iIndex].getTPreisgueltigab(),
						bestehendeDtos[iIndex].getTPreisgueltigbis(), kundesokoDtoI.getTPreisgueltigab());

				if (kundesokoDtoI.getTPreisgueltigbis() != null && !bEnthalten) {
					bEnthalten = checkDatumInDatumsbereichEnthalten(bestehendeDtos[iIndex].getTPreisgueltigab(),
							bestehendeDtos[iIndex].getTPreisgueltigbis(), kundesokoDtoI.getTPreisgueltigbis());
				}
			}

			iIndex++;
		}

		if (bEnthalten) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT,
					new Exception(
							"ArtikelIId=" + kundesokoDtoI.getArtikelIId() + ", Menge=" + nMengeI + ", gueltig bis="
									+ Helper.formatDatum(kundesokoDtoI.getTPreisgueltigab(), theClientDto.getLocUi())));
		}

		// Schritt 3: Den Vorgaenger und den Nachfolger der neuen Staffel
		// aufgrund ihres Anfangsdatums bestimmen.
		iIndex = 0;
		KundesokoDto vorgaengerDto = null;
		KundesokoDto nachfolgerDto = null;

		if (bestehendeDtos != null && bestehendeDtos.length > 0) {
			while (nachfolgerDto == null && iIndex < bestehendeDtos.length) {
				if (kundesokoDtoI.getIId().intValue() != bestehendeDtos[iIndex].getIId().intValue()) {
					if (kundesokoDtoI.getTPreisgueltigab().getTime() > bestehendeDtos[iIndex].getTPreisgueltigab()
							.getTime()) {
						iIndex++;
					} else if (kundesokoDtoI.getTPreisgueltigab().getTime() == bestehendeDtos[iIndex]
							.getTPreisgueltigab().getTime()) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT,
								new Exception("ArtikelIId=" + kundesokoDtoI.getArtikelIId() + ", Menge=" + nMengeI
										+ ", gueltig ab=" + Helper.formatDatum(kundesokoDtoI.getTPreisgueltigab(),
												theClientDto.getLocUi())));
					} else if (kundesokoDtoI.getTPreisgueltigab().getTime() < bestehendeDtos[iIndex]
							.getTPreisgueltigab().getTime()) {
						nachfolgerDto = bestehendeDtos[iIndex];

						if (iIndex > 0) {
							vorgaengerDto = bestehendeDtos[iIndex - 1];
						}
					}
				} else {
					iIndex++;
				}
			}

			// wenn die neue Mengenstaffel die letzte ist
			if (iIndex > 0 && kundesokoDtoI.getIId().intValue() != bestehendeDtos[iIndex - 1].getIId().intValue()
					&& nachfolgerDto == null && vorgaengerDto == null) {
				vorgaengerDto = bestehendeDtos[iIndex - 1];
			}
		}

		// Schritt 4: Wenn es einen Nachfolger gibt, muss der neue Bereich ev.
		// ergaenzt werden
		if (nachfolgerDto != null && kundesokoDtoI.getTPreisgueltigbis() == null) {
			// den neuen Zeitraum ergaenzen, wenn es kein bis-Datum, aber einen
			// Nachfolger gibt
			if (kundesokoDtoI.getTPreisgueltigbis() == null) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(nachfolgerDto.getTPreisgueltigab());
				cal.add(Calendar.DATE, -1); // den Tag davor festsetzen

				kundesokoDtoI.setTPreisgueltigbis(new java.sql.Date(cal.getTimeInMillis()));
			}
		}

		// Schritt 5: Wenn es einen Vorgaenger gibt, muss dieser ev. ergaenzt
		// werden
		if (vorgaengerDto != null && vorgaengerDto.getTPreisgueltigbis() == null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(kundesokoDtoI.getTPreisgueltigab());
			cal.add(Calendar.DATE, -1); // den Tag davor festsetzen

			vorgaengerDto.setTPreisgueltigbis(new java.sql.Date(cal.getTimeInMillis()));

			updateKundesoko(vorgaengerDto, null, theClientDto);
		}
	}

	/**
	 * Pruefen, ob ein Datum in einem Datumsbereich enthalten ist.
	 * 
	 * @param tDatumVonI       Date
	 * @param tDatumBisI       Date
	 * @param tDatumEnthaltenI Date
	 * @return boolean true, wenn das Datum enthalten ist
	 * @throws EJBExceptionLP
	 */
	private boolean checkDatumInDatumsbereichEnthalten(Date tDatumVonI, Date tDatumBisI, Date tDatumEnthaltenI)
			throws EJBExceptionLP {
		Calendar calIt = new GregorianCalendar();
		calIt.setTime(tDatumVonI);

		boolean bEnthalten = false;

		while (!bEnthalten && tDatumBisI != null && calIt.getTimeInMillis() <= tDatumBisI.getTime()) {
			if (calIt.getTimeInMillis() == tDatumEnthaltenI.getTime()) {
				bEnthalten = true;
			} else {
				calIt.add(Calendar.DATE, 1); // den naechsten Tag pruefen
			}
		}

		return bEnthalten;
	}

	/**
	 * Einen Kundensonderkondition loeschen. <br>
	 * Damit werden auch alle zugehoerigen Mengenstaffeln geloescht.
	 * 
	 * @param kundesokoDtoI die Kundensoko
	 * @param theClientDto  der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeKundesoko(KundesokoDto kundesokoDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkKundesokoDto(kundesokoDtoI);

		try {
			KundesokomengenstaffelDto[] aMengenstaffelDtos = kundesokomengenstaffelFindByKundesokoIId(
					kundesokoDtoI.getIId(), theClientDto);

			for (int i = 0; i < aMengenstaffelDtos.length; i++) {
				removeKundesokomengenstaffel(aMengenstaffelDtos[i], theClientDto);
			}

			Kundesoko toRemove = em.find(Kundesoko.class, kundesokoDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception(t));
		}
	}

	/**
	 * Eine bestehende Kundensonderkondition aktualisieren.
	 * 
	 * @param kundesokoDtoI            die zu aktualisierende Kundensoko
	 * @param defaultMengenstaffelDtoI die Default Mengenstaffel
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateKundesoko(KundesokoDto kundesokoDtoI, KundesokomengenstaffelDto defaultMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkKundesokoDto(kundesokoDtoI);

		// try {
		Kundesoko kundesoko = em.find(Kundesoko.class, kundesokoDtoI.getIId());
		if (kundesoko == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (defaultMengenstaffelDtoI != null) {
			checkKundesokoDto(kundesokoDtoI, defaultMengenstaffelDtoI.getNMenge(), theClientDto);
		}

		// Daten normieren
		kundesokoDtoI.setTPreisgueltigab(Helper.cutDate(kundesokoDtoI.getTPreisgueltigab()));
		kundesokoDtoI.setTPreisgueltigbis(Helper.cutDate(kundesokoDtoI.getTPreisgueltigbis()));

		kundesokoDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		kundesokoDtoI.setTAendern(getTimestamp());

		setKundesokoFromKundesokoDto(kundesoko, kundesokoDtoI);

		if (defaultMengenstaffelDtoI != null) {
			updateKundesokomengenstaffel(defaultMengenstaffelDtoI, theClientDto);
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public KundesokoDto kundesokoFindByPrimaryKey(Integer iIdI) throws EJBExceptionLP {
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdI == null"));
		}
		Kundesoko kundesoko = em.find(Kundesoko.class, iIdI);
		if (kundesoko == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// try {
		return assembleKundesokoDto(kundesoko);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public KundesokoDto[] kundesokoFindByKundeIIdArtikelIId(Integer kundeIIdI, Integer artikelIIdI)
			throws EJBExceptionLP {
		if (kundeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIIdI == null"));
		}

		if (artikelIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("artikelIIdI == null"));
		}

		// try {
		Query query = em.createNamedQuery("KundesokofindByKundeIIdArtikelIId");
		query.setParameter(1, kundeIIdI);
		query.setParameter(2, artikelIIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKundesokoDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesokoDto[] kundesokoFindByKundeIIdArtgruIId(Integer kundeIIdI, Integer artgruIIdI)
			throws EJBExceptionLP {
		if (kundeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIId == null"));
		}

		if (artgruIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("artgruIId == null"));
		}

		// try {
		Query query = em.createNamedQuery("KundesokofindByKundeIIdArtgruIId");
		query.setParameter(1, kundeIIdI);
		query.setParameter(2, artgruIIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKundesokoDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesokoDto[] kundesokoFindByKundeIId(Integer kundeIIdI) throws EJBExceptionLP {
		if (kundeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("KundesokofindByKundeIId");
		query.setParameter(1, kundeIIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKundesokoDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesokoDto[] kundesokoFindByKundeIIdOhneExc(Integer kundeIIdI) throws EJBExceptionLP {
		if (kundeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("KundesokofindByKundeIId");
		query.setParameter(1, kundeIIdI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleKundesokoDtos(cl);
	}

	public KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatum(Integer kundeIIdI, Integer artikelIIdI,
			Date tGueltigkeitsdatumI) throws EJBExceptionLP {
		Validator.notNull(tGueltigkeitsdatumI, "tGueltigkeitsdatumI");
		Validator.notNull(kundeIIdI, "kundeIIdI");
		Validator.notNull(artikelIIdI, "artikelIIdI");

		KundesokoDto dto = kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumImpl(kundeIIdI, artikelIIdI,
				tGueltigkeitsdatumI);
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, new NoResultException());
		}

		return dto;
	}

	public KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(Integer kundeIIdI,
			Integer artikelIIdI, Date tGueltigkeitsdatumI) {
		KundesokoDto kundesokoDto = null;

		try {
			kundesokoDto = kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumImpl(kundeIIdI, artikelIIdI,
					tGueltigkeitsdatumI);
		} catch (Throwable t) {
		}

		return kundesokoDto;
	}

	private KundesokoDto kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumImpl(Integer kundeIIdI, Integer artikelIIdI,
			Date tGueltigkeitsdatumI) {
		try {
			Query query = em.createNamedQuery("KundesokofindByKundeIIdArtikelIIdGueltigkeitsdatum");
			query.setParameter(1, kundeIIdI);
			query.setParameter(2, artikelIIdI);
			query.setParameter(3, tGueltigkeitsdatumI);
			Kundesoko kundesoko = (Kundesoko) query.getSingleResult();
			return assembleKundesokoDto(kundesoko);
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex) {
		}

		return null;
	}

	public KundesokoDto kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatum(Integer kundeIIdI, Integer artgruIIdI,
			Date tGueltigkeitsdatumI) throws EJBExceptionLP {
		if (tGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tPreisgueltigabI == null"));
		}

		if (kundeIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundeIId == null"));
		}

		if (artgruIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("artgruIId == null"));
		}

		try {
			Query query = em.createNamedQuery("KundesokofindByKundeIIdArtgruIIdGueltigkeitsdatum");
			query.setParameter(1, kundeIIdI);
			query.setParameter(2, artgruIIdI);
			query.setParameter(3, tGueltigkeitsdatumI);
			Kundesoko kundesoko = (Kundesoko) query.getSingleResult();
			return assembleKundesokoDto(kundesoko);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	public KundesokoDto kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(Integer kundeIIdI, Integer artgruIIdI,
			Date tGueltigkeitsdatumI) {
		KundesokoDto kundesokoDto = null;

		try {
			kundesokoDto = kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatum(kundeIIdI, artgruIIdI,
					tGueltigkeitsdatumI);
		} catch (Throwable t) {
			// ignore
		}

		return kundesokoDto;
	}

	private void setKundesokoFromKundesokoDto(Kundesoko kundesoko, KundesokoDto kundesokoDto) {
		kundesoko.setKundeIId(kundesokoDto.getKundeIId());
		kundesoko.setArtikelIId(kundesokoDto.getArtikelIId());
		kundesoko.setArtgruIId(kundesokoDto.getArtgruIId());
		kundesoko.setTPreisgueltigab(kundesokoDto.getTPreisgueltigab());
		kundesoko.setTPreisgueltigbis(kundesokoDto.getTPreisgueltigbis());
		kundesoko.setCBemerkung(kundesokoDto.getCBemerkung());
		kundesoko.setCKundeartikelnummer(kundesokoDto.getCKundeartikelnummer());
		kundesoko.setCKundeartikelbez(kundesokoDto.getCKundeartikelbez());
		kundesoko.setCKundeartikelzbez(kundesokoDto.getCKundeartikelzbez());
		kundesoko.setBBemerkungdrucken(kundesokoDto.getBBemerkungdrucken());
		kundesoko.setBRabattsichtbar(kundesokoDto.getBRabattsichtbar());
		kundesoko.setBDrucken(kundesokoDto.getBDrucken());
		kundesoko.setPersonalIIdAendern(kundesokoDto.getPersonalIIdAendern());
		kundesoko.setTAendern(kundesokoDto.getTAendern());
		kundesoko.setBWirktNichtFuerPreisfindung(kundesokoDto.getBWirktNichtFuerPreisfindung());
		kundesoko.setBKeineMengenstaffel(kundesokoDto.getBKeineMengenstaffel());
		kundesoko.setNStartwertLiefermenge(kundesokoDto.getNStartwertLiefermenge());
		em.merge(kundesoko);
		em.flush();
	}

	private KundesokoDto assembleKundesokoDto(Kundesoko kundesoko) {
		return KundesokoDtoAssembler.createDto(kundesoko);
	}

	private KundesokoDto[] assembleKundesokoDtos(Collection<?> kundesokos) {
		List<KundesokoDto> list = new ArrayList<KundesokoDto>();
		if (kundesokos != null) {
			Iterator<?> iterator = kundesokos.iterator();
			while (iterator.hasNext()) {
				Kundesoko kundesoko = (Kundesoko) iterator.next();
				list.add(assembleKundesokoDto(kundesoko));
			}
		}
		KundesokoDto[] returnArray = new KundesokoDto[list.size()];
		return (KundesokoDto[]) list.toArray(returnArray);
	}

	private void checkKundesokoDto(KundesokoDto kundesokoDtoI) throws EJBExceptionLP {
		if (kundesokoDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception());
		}
	}

	// Kundesokomengenstaffel
	// ----------------------------------------------------

	/**
	 * Einen neue Mengenstaffel zu einer Kundensonderkondition anlegen.
	 * 
	 * @param kundesokomengenstaffelDtoI die neue Mengenstaffel
	 * @param theClientDto               der aktuelle Benutzer
	 * @return Integer PK der neuen Mengenstaffel
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer createKundesokomengenstaffel(KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkKundesokoMengenstaffelDto(kundesokomengenstaffelDtoI);
		checkUniqueKey(kundesokomengenstaffelDtoI);

		Integer iId = new Integer(-1);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj();
		iId = pkGen.getNextPrimaryKey(PKConst.PK_KUNDESOKOMENGENSTAFFEL);

		kundesokomengenstaffelDtoI.setIId(iId);

		Kundesokomengenstaffel kundesokomengenstaffel = new Kundesokomengenstaffel(kundesokomengenstaffelDtoI.getIId(),
				kundesokomengenstaffelDtoI.getKundesokoIId(), kundesokomengenstaffelDtoI.getNMenge());
		em.persist(kundesokomengenstaffel);
		em.flush();

		setKundesokomengenstaffelFromKundesokomengenstaffelDto(kundesokomengenstaffel, kundesokomengenstaffelDtoI);

		return iId;
	}

	private void checkUniqueKey(KundesokomengenstaffelDto kundesokomengenstaffelDtoI) throws EJBExceptionLP {
		KundesokomengenstaffelDto mengenstaffelDtoI = null;

		try {
			Query query = em.createNamedQuery("KundesokomengenstaffelfindByUniqueKey");
			query.setParameter(1, kundesokomengenstaffelDtoI.getKundesokoIId());
			query.setParameter(2, kundesokomengenstaffelDtoI.getNMenge());
			// @todo getSingleResult oder getResultList ?
			mengenstaffelDtoI = assembleKundesokomengenstaffelDto((Kundesokomengenstaffel) query.getSingleResult());
		} catch (Throwable t) {
			// continue
		}

		if (mengenstaffelDtoI != null) {
			// constraint verletzt
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception());
		}
	}

	/**
	 * Eine Mengenstaffel zu einer Kundensonderkondition loeschen.
	 * 
	 * @param kundesokomengenstaffelDtoI die zu loeschende Mengenstaffel
	 * @param theClientDto               der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeKundesokomengenstaffel(KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkKundesokoMengenstaffelDto(kundesokomengenstaffelDtoI);

		try {
			Kundesokomengenstaffel toRemove = em.find(Kundesokomengenstaffel.class,
					kundesokomengenstaffelDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception(t));
		}
	}

	/**
	 * Eine bestehende SOKO Mengenstaffel aktualisieren.
	 * 
	 * @param kundesokomengenstaffelDtoI die Mengenstaffel
	 * @param theClientDto               der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateKundesokomengenstaffel(KundesokomengenstaffelDto kundesokomengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkKundesokoMengenstaffelDto(kundesokomengenstaffelDtoI);

		// try {
		Kundesokomengenstaffel kundesokomengenstaffel = em.find(Kundesokomengenstaffel.class,
				kundesokomengenstaffelDtoI.getIId());
		if (kundesokomengenstaffel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (kundesokomengenstaffel.getNMenge().doubleValue() != kundesokomengenstaffelDtoI.getNMenge().doubleValue()) {
			checkUniqueKey(kundesokomengenstaffelDtoI);
		}

		setKundesokomengenstaffelFromKundesokomengenstaffelDto(kundesokomengenstaffel, kundesokomengenstaffelDtoI);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public KundesokomengenstaffelDto kundesokomengenstaffelFindByPrimaryKey(Integer iIdI) throws EJBExceptionLP {
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdI == null"));
		}

		// try {
		Kundesokomengenstaffel kundesokomengenstaffel = em.find(Kundesokomengenstaffel.class, iIdI);
		if (kundesokomengenstaffel == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKundesokomengenstaffelDto(kundesokomengenstaffel);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setKundesokomengenstaffelFromKundesokomengenstaffelDto(Kundesokomengenstaffel kundesokomengenstaffel,
			KundesokomengenstaffelDto kundesokomengenstaffelDto) {
		kundesokomengenstaffel.setKundesokoIId(kundesokomengenstaffelDto.getKundesokoIId());
		kundesokomengenstaffel.setNMenge(kundesokomengenstaffelDto.getNMenge());
		kundesokomengenstaffel.setFArtikelstandardrabattsatz(kundesokomengenstaffelDto.getFArtikelstandardrabattsatz());
		kundesokomengenstaffel.setNArtikelfixpreis(kundesokomengenstaffelDto.getNArtikelfixpreis());

		em.merge(kundesokomengenstaffel);
		em.flush();
	}

	private KundesokomengenstaffelDto assembleKundesokomengenstaffelDto(Kundesokomengenstaffel kundesokomengenstaffel) {
		return KundesokomengenstaffelDtoAssembler.createDto(kundesokomengenstaffel);
	}

	private KundesokomengenstaffelDto[] assembleKundesokomengenstaffelDtos(Collection<?> kundesokomengenstaffels) {
		List<KundesokomengenstaffelDto> list = new ArrayList<KundesokomengenstaffelDto>();
		if (kundesokomengenstaffels != null) {
			Iterator<?> iterator = kundesokomengenstaffels.iterator();
			while (iterator.hasNext()) {
				Kundesokomengenstaffel kundesokomengenstaffel = (Kundesokomengenstaffel) iterator.next();
				list.add(assembleKundesokomengenstaffelDto(kundesokomengenstaffel));
			}
		}
		KundesokomengenstaffelDto[] returnArray = new KundesokomengenstaffelDto[list.size()];
		return (KundesokomengenstaffelDto[]) list.toArray(returnArray);
	}

	private void checkKundesokoMengenstaffelDto(KundesokomengenstaffelDto kundesokoMengenstaffelDtoI)
			throws EJBExceptionLP {
		if (kundesokoMengenstaffelDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception());
		}
	}

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(Integer iIdKundesokoIId,
			Date tGueltigkeitsdatumI, String waehrungCNrZielwaehrung, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("KundesokomengenstaffelfindByKundesokoIId");
		query.setParameter(1, iIdKundesokoIId);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		List<KundesokomengenstaffelDto> list = new ArrayList<KundesokomengenstaffelDto>();
		while (it.hasNext()) {

			Kundesokomengenstaffel mengenstaffel = (Kundesokomengenstaffel) it.next();
			Kundesoko kundesoko = em.find(Kundesoko.class, mengenstaffel.getKundesokoIId());
			Kunde kunde = em.find(Kunde.class, kundesoko.getKundeIId());

			KundesokomengenstaffelDto mengenstaffelDto = assembleKundesokomengenstaffelDto(mengenstaffel);

			if (mengenstaffelDto.getNArtikelfixpreis() != null
					&& mengenstaffelDto.getNArtikelfixpreis().doubleValue() != 0) {
				try {
					mengenstaffelDto.setNArtikelfixpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							mengenstaffelDto.getNArtikelfixpreis(), kunde.getWaehrungCNr(), waehrungCNrZielwaehrung,
							tGueltigkeitsdatumI, theClientDto));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			list.add(mengenstaffelDto);
		}

		KundesokomengenstaffelDto[] returnArray = new KundesokomengenstaffelDto[list.size()];
		return (KundesokomengenstaffelDto[]) list.toArray(returnArray);
	}

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIId(Integer iIdKundesokoIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdKundesokoIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundesokoIId == null"));
		}

		// try {
		Query query = em.createNamedQuery("KundesokomengenstaffelfindByKundesokoIId");
		query.setParameter(1, iIdKundesokoIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleKundesokomengenstaffelDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdOhneExc(Integer iIdKundesokoIId,
			TheClientDto theClientDto) {
		if (iIdKundesokoIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundesokoIId == null"));
		}
		Query query = em.createNamedQuery("KundesokomengenstaffelfindByKundesokoIId");
		query.setParameter(1, iIdKundesokoIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleKundesokomengenstaffelDtos(cl);
	}

	/**
	 * Liefert alle Mengenstaffeln <= nMenge, die zu einer Kundensoko erfasst
	 * wurden. ORDER BY nMenge, d.h. die letzte Menge der Ergebnisliste ist die
	 * passende Mengenstaffel.
	 * 
	 * @param kundesokoIIdI PK der Kundensoko
	 * @param nMengeI       die fragliche Menge
	 * @return KundesokomengenstaffelDto[] alle Mengenstaffeln, die <= nMenge sind
	 * @throws EJBExceptionLP Ausnahme
	 */
	public KundesokomengenstaffelDto[] kundesokomengenstaffelFindByKundesokoIIdNMenge(Integer kundesokoIIdI,
			BigDecimal nMengeI) throws EJBExceptionLP {
		if (kundesokoIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("kundesokoIIdI == null"));
		}

		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("nMengeI == null"));
		}
		Query query = em.createNamedQuery("KundesokomengenstaffelfindByKundesokoIIdNMenge");
		query.setParameter(1, kundesokoIIdI);
		query.setParameter(2, nMengeI);
		Collection<?> cl = query.getResultList();
		// try {
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return this.assembleKundesokomengenstaffelDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	@Override
	public void updateKundesokoOrCreateIfNotExist(Integer kundeIId, Integer artikelIId, String kundeArtikelCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		KundesokoDto soko = kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(kundeIId, artikelIId,
				new Date(System.currentTimeMillis()));

		if (soko == null) {
			ParametermandantDto paramWirktNichtInPreisfindung = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KUNDESOKO_WIRKT_NICHT_IN_PREISFINDUNG);
			ParametermandantDto paramRabatt = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_INTEL_STKL_IMPORT_RABATT);
			BigDecimal rabatt = Helper.toBigDecimal(paramRabatt.getCWert());

			soko = new KundesokoDto();
			soko.setKundeIId(kundeIId);
			soko.setArtikelIId(artikelIId);
			soko.setBDrucken(Helper.boolean2Short(false));
			soko.setBBemerkungdrucken(Helper.boolean2Short(false));
			soko.setBRabattsichtbar(Helper.boolean2Short(false));
			soko.setCKundeartikelnummer(kundeArtikelCNr);
			soko.setTPreisgueltigab(new Date(System.currentTimeMillis()));
			soko.setBWirktNichtFuerPreisfindung(
					Helper.boolean2Short((Boolean) paramWirktNichtInPreisfindung.getCWertAsObject()));

			KundesokomengenstaffelDto sokoStaffel = new KundesokomengenstaffelDto();
			sokoStaffel.setFArtikelstandardrabattsatz(rabatt.doubleValue());
			sokoStaffel.setNMenge(BigDecimal.ONE);
			getKundesokoFac().createKundesoko(soko, sokoStaffel, theClientDto);
		} else if (soko.getCKundeartikelnummer() == null || soko.getCKundeartikelnummer().isEmpty()) {
			// TODO derzeit nur updaten, wenn noch keine kundenartikelnummer
			// vorhanden.
			// in Zukunft immer updaten?
			soko.setCKundeartikelnummer(kundeArtikelCNr);
			updateKundesoko(soko, null, theClientDto);
		}
	}

}
