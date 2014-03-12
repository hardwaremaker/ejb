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
package com.lp.server.anfrage.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.ejb.Anfrage;
import com.lp.server.anfrage.fastlanereader.generated.FLRAnfrage;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageDtoAssembler;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.ejbfac.BelegAktivierungController;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AnfrageFacBean extends Facade implements AnfrageFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	// Anfrage
	// -------------------------------------------------------------------

	/**
	 * Anlegen einer neuen Anfrage.
	 * 
	 * @param anfrageDtoI
	 *            die neue Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Anfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAnfrage(AnfrageDto anfrageDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageDto(anfrageDtoI);

		Integer anfrageIId = null;
		String anfrageCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(anfrageDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					anfrageDtoI.getMandantCNr(), anfrageDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_ANFRAGE,
					anfrageDtoI.getMandantCNr(), theClientDto);

			anfrageIId = bnr.getPrimaryKey();
			anfrageCNr = f.format(bnr);

			anfrageDtoI.setIId(anfrageIId);
			anfrageDtoI.setCNr(anfrageCNr);
			anfrageDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			anfrageDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			anfrageDtoI.setNGesamtwertinbelegwaehrung(new BigDecimal(0));

			Anfrage anfrage = new Anfrage(
					anfrageDtoI.getIId(),
					anfrageDtoI.getCNr(),
					anfrageDtoI.getMandantCNr(),
					anfrageDtoI.getStatusCNr(),
					anfrageDtoI.getArtCNr(),
					anfrageDtoI.getBelegartCNr(),
					anfrageDtoI.getTBelegdatum(),
					anfrageDtoI.getWaehrungCNr(),
					anfrageDtoI.getFWechselkursmandantwaehrungzubelegwaehrung(),
					anfrageDtoI.getKostenstelleIId(), anfrageDtoI
							.getFAllgemeinerRabattsatz(), anfrageDtoI
							.getNTransportkosteninanfragewaehrung(),
					anfrageDtoI.getBelegtextIIdKopftext(), anfrageDtoI
							.getBelegtextIIdFusstext(), anfrageDtoI
							.getPersonalIIdAnlegen(), anfrageDtoI
							.getPersonalIIdAendern());

			em.persist(anfrage);
			// em.flush();

			anfrageDtoI.setTAnlegen(anfrage.getTAnlegen());
			anfrageDtoI.setTAendern(anfrage.getTAendern());

			setAnfrageFromAnfrageDto(anfrage, anfrageDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return anfrageIId;
	}

	/**
	 * Eine Anfrage stornieren.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void storniereAnfrage(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehlerb beim stornieren der Anfrage. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}
		anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT);
		anfrage.setPersonalIIdStorniert(theClientDto.getIDPersonal());
		anfrage.setTStorniert(getTimestamp());

		// saemtliche Daten bleiben erhalten
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Eine bestehende Anfrage aktualisieren.
	 * 
	 * @param anfrageDtoI
	 *            die Anfrage
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung aenderewaehrung: 0
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrage(AnfrageDto anfrageDtoI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageDto(anfrageDtoI);

		try {
			// wenn die Waehrung geaendert wurde, muessen die Belegwerte neu
			// berechnet werden
			if (waehrungOriCNrI != null
					&& !waehrungOriCNrI.equals(anfrageDtoI.getWaehrungCNr())) {
				AnfragepositionDto[] aAnfragepositionDto = getAnfragepositionFac()
						.anfragepositionFindByAnfrage(anfrageDtoI.getIId(),
								theClientDto);

				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(
						waehrungOriCNrI, anfrageDtoI.getWaehrungCNr(),
						theClientDto);

				for (int i = 0; i < aAnfragepositionDto.length; i++) {
					if (aAnfragepositionDto[i].getNMenge() != null
							&& aAnfragepositionDto[i].getNRichtpreis() != null) {
						// die Positionswerte neu berechnen und abspeichern
						BigDecimal nRichtpreisInNeuerWaehrung = aAnfragepositionDto[i]
								.getNRichtpreis().multiply(ffWechselkurs);

						aAnfragepositionDto[i]
								.setNRichtpreis(nRichtpreisInNeuerWaehrung);

						getAnfragepositionFac().updateAnfrageposition(
								aAnfragepositionDto[i], theClientDto);

						// die zugehoerigen Lieferdaten korrigieren
						AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = getAnfragepositionFac()
								.anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
										aAnfragepositionDto[i].getIId());

						if (anfragepositionlieferdatenDto != null) {
							BigDecimal nNettogesamtpreis = anfragepositionlieferdatenDto
									.getNNettogesamtpreis().multiply(
											ffWechselkurs);

							anfragepositionlieferdatenDto
									.setNNettogesamtpreis(nNettogesamtpreis);

							getAnfragepositionFac()
									.updateAnfragepositionlieferdaten(
											anfragepositionlieferdatenDto,
											theClientDto);

							getAnfragepositionFac()
									.befuelleZusaetzlichesPreisfeld(
											anfragepositionlieferdatenDto
													.getIId(),
											theClientDto);
						}
					}
				}
			}

			anfrageDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			anfrageDtoI.setTAendern(getTimestamp());

			Anfrage anfrage = em.find(Anfrage.class, anfrageDtoI.getIId());
			if (anfrage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei Update Anfrage. Es gibt keine anfrage mit iid "
								+ anfrageDtoI.getIId()
								+ " anfrageDto.toString:"
								+ anfrageDtoI.toString());
			}
			setAnfrageFromAnfrageDto(anfrage, anfrageDtoI);
			pruefeUndSetzeAnfragestatusBeiAenderung(anfrageDtoI.getIId(),
					theClientDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Die Kopfdaten einer Anfrage duerfen nur in bestimmten Stati geaendert
	 * werden. <br>
	 * Nachdem eine Anfrage geaendert wurde, befindet er sich im Status
	 * ANGELEGT.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @throws EJBExceptionLP
	 */
	/*
	 * private void pruefeUndSetzeAnfragestatusBeiAenderung(Integer iIdAnfrageI,
	 * String cNrUserI) throws EJBExceptionLP { check2(cNrUserI);
	 * checkAnfrageHome(); checkAnfrageIId(iIdAnfrageI);
	 * 
	 * Anfrage anfrage = null;
	 * 
	 * try { anfrage = em.find(Anfrage.class, iIdAnfrageI);
	 * 
	 * String sStatus = anfrage.getAnfragestatusCNr();
	 * 
	 * if (!sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT) &&
	 * !sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN) &&
	 * !sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(sStatus)); }
	 * 
	 * anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
	 * anfrage.setNGesamtanfragewertinanfragewaehrung(null); } catch
	 * (FinderException ex) { throw new
	 * EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex); } }
	 */

	/**
	 * Eine Anfrage manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void manuellErledigen(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler beim manuell erledigen der Anfrage. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}

		if (anfrage.getAnfragestatusCNr().equals(
				AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
				|| anfrage.getAnfragestatusCNr().equals(
						AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
			anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT);
			anfrage.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			anfrage.setTManuellerledigt(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Anfrage kann nicht manuell erledigt werden, Status : "
									+ anfrage.getAnfragestatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Den Status einer Anfrage von 'Erledigt' auf 'Erfasst' setzen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void erledigungAufheben(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehlerb beim Aufheben der erledigung. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}

		if (anfrage.getAnfragestatusCNr().equals(
				AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
			if (anfrage.getPersonalIIdManuellerledigt() != null
					&& anfrage.getTManuellerledigt() != null) {

				anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_OFFEN);
				anfrage.setPersonalIIdManuellerledigt(null);
				anfrage.setTManuellerledigt(null);
			} else {
				// throw new EJBExceptionLP(
				// EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT,
				// new Exception("Diese Anfrage wurde nicht manuell erledigt"));
				anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_OFFEN);
				myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl die Anfrage nicht manuell erledigt wurde, AnfrageIId: "
						+ iIdAnfrageI);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung der Anfrage kann nicht aufgehoben werden, Status: "
									+ anfrage.getAnfragestatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public AnfrageDto anfrageFindByPrimaryKey(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check2(cNrUserI);
		// checkAnfrageIId(iIdAnfrageI);

		AnfrageDto anfrageDto = anfrageFindByPrimaryKeyOhneExc(iIdAnfrageI);
		if (anfrageDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfrageFindByPrimaryKey. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}

		return anfrageDto;
	}

	public AnfrageDto anfrageFindByPrimaryKeyOhneExc(Integer iIdAnfrageI) {

		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			return null;
		}

		return assembleAnfrageDto(anfrage);
	}

	public AnfrageDto anfrageFindByCNrMandantCNrOhneExc(String cNr,
			String cNrMandantI) {
		Query query = em.createNamedQuery(Anfrage.QueryFindByCnrMandantCnr);
		query.setParameter("cnr", cNr);
		query.setParameter("mandant", cNrMandantI);
		try {
			Anfrage a = (Anfrage) query.getSingleResult();
			return assembleAnfrageDto(a);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public AnfrageDto[] anfrageFindByMandantCNr(String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// check2(cNrUserI);

		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrMandantI == null"));
		}

		myLogger.info("cNrMandantI: " + cNrMandantI);

		AnfrageDto[] aAnfrageDto = null;

		// try {
		Query query = em.createNamedQuery("AnfragefindByMandant");
		query.setParameter(1, cNrMandantI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aAnfrageDto = assembleAnfrageDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAnfrageDto;
	}

	public AnfrageDto[] anfrageFindByAnfrageIIdLiefergruppenanfrage(
			Integer iIdAnfrageLiefergruppenanfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// check2(cNrUserI);

		if (iIdAnfrageLiefergruppenanfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnfrageLiefergruppenanfrageI == null"));
		}

		myLogger.info("iIdAnfrageLiefergruppenanfrageI: "
				+ iIdAnfrageLiefergruppenanfrageI);

		AnfrageDto[] aAnfrageDto = null;

		// try {
		Query query = em
				.createNamedQuery("AnfragefindByAnfrageIIdLiefergruppenanfrage");
		query.setParameter(1, iIdAnfrageLiefergruppenanfrageI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aAnfrageDto = assembleAnfrageDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAnfrageDto;
	}

	public AnfrageDto[] anfrageFindByAnsprechpartnerlieferantIIdMandantCNr(
			Integer iAnsprechpartnerLieferantIId, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iAnsprechpartnerLieferantIId == null || cNrMandantI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"iAnsprechpartnerLieferantIId == null || cNrMandantI == null"));
		}

		myLogger.info("iAnsprechpartnerLieferantIId: "
				+ iAnsprechpartnerLieferantIId);

		AnfrageDto[] aAnfrageDto = null;

		// try {
		Query query = em
				.createNamedQuery("AnfragefindByAnsprechpartnerlieferantIIdMandantCNr");
		query.setParameter(1, iAnsprechpartnerLieferantIId);
		query.setParameter(2, cNrMandantI);
		aAnfrageDto = assembleAnfrageDtos(query.getResultList());
		if (aAnfrageDto.length == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception(
							"Es wurden keine Anfragen mit der iAnsprechpartnerLieferantIId "
									+ iAnsprechpartnerLieferantIId + "gefunden"));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAnfrageDto;
	}

	public AnfrageDto[] anfrageFindByAnsprechpartnerlieferantIIdMandantCNrOhneExc(
			Integer iAnsprechpartnerLieferantIId, String cNrMandantI,
			TheClientDto theClientDto) {

		if (iAnsprechpartnerLieferantIId == null || cNrMandantI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"iAnsprechpartnerLieferantIId == null || cNrMandantI == null"));
		}

		myLogger.info("iAnsprechpartnerLieferantIId: "
				+ iAnsprechpartnerLieferantIId);

		AnfrageDto[] aAnfrageDto = null;

		try {
			Query query = em
					.createNamedQuery("AnfragefindByAnsprechpartnerlieferantIIdMandantCNr");
			query.setParameter(1, iAnsprechpartnerLieferantIId);
			query.setParameter(2, cNrMandantI);
			aAnfrageDto = assembleAnfrageDtos(query.getResultList());
		} catch (Throwable ex) {
			return null;
		}

		return aAnfrageDto;
	}

	public AnfrageDto[] anfrageFindByLieferantIIdAnfrageadresseMandantCNr(
			Integer iLieferantIIdAnfrageadresseI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iLieferantIIdAnfrageadresseI == null || cNrMandantI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"iLieferantIIdAnfrageadresseI == null || cNrMandantI == null"));
		}

		myLogger.info("iLieferantIIdAnfrageadresseI: "
				+ iLieferantIIdAnfrageadresseI);

		AnfrageDto[] aAnfrageDto = null;

		// try {
		Query query = em
				.createNamedQuery("AnfragefindByLieferantIIdAnfrageadresseMandantCNr");
		query.setParameter(1, iLieferantIIdAnfrageadresseI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAnfrageDto = assembleAnfrageDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAnfrageDto;
	}

	public AnfrageDto[] anfrageFindByLieferantIIdAnfrageadresseMandantCNrOhneExc(
			Integer iLieferantIIdAnfrageadresseI, String cNrMandantI,
			TheClientDto theClientDto) {

		if (iLieferantIIdAnfrageadresseI == null || cNrMandantI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"iLieferantIIdAnfrageadresseI == null || cNrMandantI == null"));
		}

		myLogger.info("iLieferantIIdAnfrageadresseI: "
				+ iLieferantIIdAnfrageadresseI);

		AnfrageDto[] aAnfrageDto = null;

		try {
			Query query = em
					.createNamedQuery("AnfragefindByLieferantIIdAnfrageadresseMandantCNr");
			query.setParameter(1, iLieferantIIdAnfrageadresseI);
			query.setParameter(2, cNrMandantI);
			aAnfrageDto = assembleAnfrageDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}

		return aAnfrageDto;
	}

	private void setAnfrageFromAnfrageDto(Anfrage anfrage, AnfrageDto anfrageDto) {
		anfrage.setCNr(anfrageDto.getCNr());
		anfrage.setMandantCNr(anfrageDto.getMandantCNr());
		anfrage.setAnfrageartCNr(anfrageDto.getArtCNr());
		anfrage.setAnfragestatusCNr(anfrageDto.getStatusCNr());
		anfrage.setBelegartCNr(anfrageDto.getBelegartCNr());
		anfrage.setTBelegdatum(anfrageDto.getTBelegdatum());
		anfrage.setLieferantIIdAnfrageadresse(anfrageDto
				.getLieferantIIdAnfrageadresse());
		anfrage.setAnsprechpartnerIIdLieferant(anfrageDto
				.getAnsprechpartnerIIdLieferant());
		anfrage.setLieferguppeIId(anfrageDto.getLiefergruppeIId());
		anfrage.setCBez(anfrageDto.getCBez());
		anfrage.setCAngebotnummer(anfrageDto.getCAngebotnummer());
		anfrage.setWaehrungCNrAnfragewaehrung(anfrageDto.getWaehrungCNr());
		anfrage.setFWechselkursmandantwaehrungzuanfragewaehrung(anfrageDto
				.getFWechselkursmandantwaehrungzubelegwaehrung());
		anfrage.setTAnliefertermin(anfrageDto.getTAnliefertermin());
		anfrage.setTAngebotdatum(anfrageDto.getTAngebotdatum());
		anfrage.setTAngebotgueltigbis(anfrageDto.getTAngebotgueltigbis());
		anfrage.setKostenstelleIId(anfrageDto.getKostenstelleIId());
		anfrage.setFAllgemeinerrabattsatz(anfrageDto
				.getFAllgemeinerRabattsatz());
		anfrage.setLieferartIId(anfrageDto.getLieferartIId());
		anfrage.setZahlungszielIId(anfrageDto.getZahlungszielIId());
		anfrage.setSpediteurIId(anfrageDto.getSpediteurIId());
		anfrage.setNGesamtanfragewertinanfragewaehrung(anfrageDto
				.getNGesamtwertinbelegwaehrung());
		anfrage.setNTransportkosteninanfragewaehrung(anfrageDto
				.getNTransportkosteninanfragewaehrung());
		anfrage.setAnfragetextIIdKopftext(anfrageDto.getBelegtextIIdKopftext());
		anfrage.setXKopftextuebersteuert(anfrageDto.getXKopftextuebersteuert());
		anfrage.setAnfragetextIIdFusstext(anfrageDto.getBelegtextIIdFusstext());
		anfrage.setXFusstextuebersteuert(anfrageDto.getXFusstextuebersteuert());
		anfrage.setTGedruckt(anfrageDto.getTGedruckt());
		anfrage.setPersonalIIdStorniert(anfrageDto.getPersonalIIdStorniert());
		anfrage.setTStorniert(anfrageDto.getTStorniert());
		anfrage.setPersonalIIdAnlegen(anfrageDto.getPersonalIIdAnlegen());
		anfrage.setTAnlegen(anfrageDto.getTAnlegen());
		anfrage.setPersonalIIdAendern(anfrageDto.getPersonalIIdAendern());
		anfrage.setTAendern(anfrageDto.getTAendern());
		anfrage.setPersonalIIdManuellerledigt(anfrageDto
				.getPersonalIIdManuellerledigt());
		anfrage.setTManuellerledigt(anfrageDto.getTManuellerledigt());
		anfrage.setAnfrageIIdLiefergruppenanfrage(anfrageDto
				.getAnfrageIIdLiefergruppenanfrage());
		anfrage.setCLieferartort(anfrageDto.getCLieferartort());
		anfrage.setProjektIId(anfrageDto.getProjektIId());
		em.merge(anfrage);
		em.flush();
	}

	private AnfrageDto assembleAnfrageDto(Anfrage anfrage) {
		return AnfrageDtoAssembler.createDto(anfrage);
	}

	private AnfrageDto[] assembleAnfrageDtos(Collection<?> anfrages) {
		List<AnfrageDto> list = new ArrayList<AnfrageDto>();
		if (anfrages != null) {
			Iterator<?> iterator = anfrages.iterator();
			while (iterator.hasNext()) {
				Anfrage anfrage = (Anfrage) iterator.next();
				list.add(assembleAnfrageDto(anfrage));
			}
		}
		AnfrageDto[] returnArray = new AnfrageDto[list.size()];
		return (AnfrageDto[]) list.toArray(returnArray);
	}

	private void checkAnfrageDto(AnfrageDto anfrageDtoI) throws EJBExceptionLP {
		if (anfrageDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("anfrageDtoI == null"));
		}

		myLogger.info("AnfrageDto: " + anfrageDtoI.toString());
	}

	private void checkAnfrageIId(Integer iIdAnfrageI) throws EJBExceptionLP {
		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdAnfrageI == null"));
		}

		myLogger.info("AnfrageIId: " + iIdAnfrageI.toString());
	}

	/**
	 * Anfragen duerfen nur in bestimmten Stati geaendert werden. <br>
	 * Nachdem eine Anfrage geaendert wurde, befindet sie sich im Status
	 * 'Angelegt'.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void pruefeUndSetzeAnfragestatusBeiAenderung(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		Anfrage anfrage = null;

		// try {
		anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei pruefe und setze Anfragestatus bei Aenderung. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		anfrage.setPersonalIIdAendern(theClientDto.getIDPersonal());
		anfrage.setTAendern(getTimestamp());
		String sStatus = anfrage.getAnfragestatusCNr();

		if (!sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)
				&& !sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
				&& !sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(sStatus));
		}

		if (sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
				|| sStatus.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)) {
			anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
			anfrage.setNGesamtanfragewertinanfragewaehrung(null);
		}
	}

	/**
	 * Berechne den Gesamtwert einer bestimmten Anfrage in der Anfragewaehrung. <br>
	 * Der Gesamtwert berechnet sich aus
	 * <p>
	 * Summe der Nettogesamtpreiseminusrabatt der Positionlieferdaten. <br>
	 * Beruecksichtigt werden alle mengenbehafteten Positionen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert der Anfrage
	 */
	public BigDecimal berechneNettowertGesamt(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		BigDecimal bdAnfragewert = new BigDecimal(0);

		try {
			// die aktuelle Anfrage
			Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
			if (anfrage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei berechneNettoGesamtwert. Es gibt keine ANfrage mit iid "
								+ iIdAnfrageI);
			}

			// Step 1: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
			// Anfragewert noch in der Tabelle steht
			if (anfrage.getAnfragestatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
				bdAnfragewert = new BigDecimal(0);
			} else {
				// Step 2: Den Wert aus den Positionen berechnen
				AnfragepositionDto[] aAnfragepositionDto = getAnfragepositionFac()
						.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);

				for (int i = 0; i < aAnfragepositionDto.length; i++) {
					// Lieferdaten gibt es nur zu Ident und
					// Handeingabepositionen
					if (aAnfragepositionDto[i].getPositionsartCNr().equals(
							AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
							|| aAnfragepositionDto[i]
									.getPositionsartCNr()
									.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
						AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = getAnfragepositionFac()
								.anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
										aAnfragepositionDto[i].getIId());

						// alle positiven mengenbehafteten Positionen
						// beruecksichtigen
						if (anfragepositionlieferdatenDto != null
								&& anfragepositionlieferdatenDto
										.getNAnliefermenge() != null
								&& anfragepositionlieferdatenDto
										.getNAnliefermenge().doubleValue() > 0) {

							BigDecimal bdAnliefermenge = anfragepositionlieferdatenDto
									.getNAnliefermenge();

							// Grundlage ist der Nettogesamtwertminusrabatt der
							// Position in Anfragewaehrung
							BigDecimal wertDerPosition = bdAnliefermenge
									.multiply(anfragepositionlieferdatenDto
											.getNNettogesamtpreisminusrabatt());

							bdAnfragewert = bdAnfragewert.add(wertDerPosition);
						}
					}
				}
			}

			// Step 3: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
			// gilt 15,4
			bdAnfragewert = Helper.rundeKaufmaennisch(bdAnfragewert, 4);
			checkNumberFormat(bdAnfragewert);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		myLogger.exit("Nettoanfragewert: " + bdAnfragewert.toString());

		return bdAnfragewert;
	}

	/**
	 * Wenn der Abschlag in den Konditionen geaendert wurde, dann werden im
	 * Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAnfrageKonditionen(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		try {
			// alle Positionen dieser Anfrage
			AnfragepositionDto[] aAnfragepositionDto = getAnfragepositionFac()
					.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);

			// fuer jede Position den Abschlag neu beruecksichtigen, wenn es
			// eine zugehoerige anfragepositionlieferdaten gibt
			for (int i = 0; i < aAnfragepositionDto.length; i++) {
				AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = getAnfragepositionFac()
						.anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
								aAnfragepositionDto[i].getIId());

				if (anfragepositionlieferdatenDto != null) {
					getAnfragepositionFac().befuelleZusaetzlichesPreisfeld(
							anfragepositionlieferdatenDto.getIId(),
							theClientDto);
				}
			}

			Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
			if (anfrage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAnfrageKonditionen. Es gibt keine Anfrage mit iid "
								+ iIdAnfrageI);
			}
			
			anfrage.setPersonalIIdAendern(theClientDto.getIDPersonal());
			anfrage.setTAendern(getTimestamp());

			if (!anfrage.getAnfragestatusCNr().equals(
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
				anfrage.setNGesamtanfragewertinanfragewaehrung(berechneNettowertGesamt(
						iIdAnfrageI, theClientDto));
				em.merge(anfrage);
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Den Druckzeitpunkt einer Anfrage vermerken.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param timestampI
	 *            der Zeitpunkt des Drucks
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void setzeDruckzeitpunkt(Integer iIdAnfrageI, Timestamp timestampI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei setzeDruckzeitpunkt. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}

		anfrage.setTGedruckt(timestampI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Die Kennung einer Anfrage bestimmten.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param localeI
	 *            das gewuenschte Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Kennung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String getAnfragekennung(Integer iIdAnfrageI, Locale localeI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		StringBuffer buff = new StringBuffer();

		try {
			Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
			if (anfrage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei getAnfragenkennung. Es gibt keine Anfrage mit iid "
								+ iIdAnfrageI);
			}

			if (anfrage.getAnfrageartCNr().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERANT)) {
				buff.append(getTextRespectUISpr("anf.anfragekennung",
						theClientDto.getMandant(), localeI));
			} else if (anfrage.getAnfrageartCNr().equals(
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE)) {
				buff.append(getTextRespectUISpr(
						"anf.liefergruppenanfragekennung",
						theClientDto.getMandant(), localeI));
			}

			buff.append(" ");

			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(
					anfrage.getBelegartCNr(), theClientDto);

			buff.append(belegartDto.getCKurzbezeichnung());
			buff.append(" ").append(anfrage.getCNr());
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return buff.toString();
	}

	/**
	 * Eine Anfrage mit Status 'Storniert' auf 'Offen' setzen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void stornoAufheben(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei stornoAufheben. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}

		if (anfrage.getAnfragestatusCNr().equals(
				AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)) {
			anfrage.setAnfragestatusCNr(AnfrageServiceFac.ANFRAGESTATUS_OFFEN);
			anfrage.setTStorniert(null);
			anfrage.setPersonalIIdStorniert(null);
			anfrage.setTAendern(getTimestamp());
			anfrage.setPersonalIIdAendern(theClientDto.getIDPersonal());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Storno der Bestellung kann nicht aufgehoben werden, Status : "
									+ anfrage.getAnfragestatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}
	
	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		//nichts zu pruefen
	}
	
	@Override
	public void aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Anfrage anfrage = em.find(Anfrage.class, iid);
		if(anfrage.getAnfragestatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT))
			setzeAnfragestatus(AnfrageServiceFac.ANFRAGESTATUS_OFFEN, iid, theClientDto);
	}
	
	@Override
	public void berechneBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		//hier gibt es nichts zu berechnen
	}

	/**
	 * Den Status einer Anfrage aendern.
	 * 
	 * @param cNrStatusI
	 *            der neue Status der Anfrage
	 * @param iIdAnfrageI
	 *            PK der Anfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void setzeAnfragestatus(String cNrStatusI, Integer iIdAnfrageI,
			TheClientDto theClientDto) {
		checkAnfrageIId(iIdAnfrageI);

		if (cNrStatusI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrStatusI == null"));
		}
		try {
			// Das Setzen auf einen Status "Offen" oder hoeher ist ohne
			// Positionen nicht zulaessig
			if (cNrStatusI.equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
					|| cNrStatusI
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
					|| cNrStatusI
							.equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				if (getAnfragepositionFac()
						.getAnzahlMengenbehafteteAnfragepositionen(iIdAnfrageI,
								theClientDto) == 0) {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(iIdAnfrageI);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN,
							al,
							new Exception(
									"Fehler bei setzeAnfrageStatus. Es gibt keine Mengenbehafteten Positionen f\u00FCr die Anfrageiid "
											+ iIdAnfrageI));
				}
			}
			myLogger.info("Neuer Status: " + cNrStatusI);
			Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
			if (anfrage == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei setzeAnfrageStatus. Es gibt keine Anfrage mit iid "
								+ iIdAnfrageI);
			}
			anfrage.setAnfragestatusCNr(cNrStatusI);
			em.merge(anfrage);
			em.flush();
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void setzeNettogesamtwert(Integer iIdAnfrageI,
			BigDecimal nNettogesamtwertI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfrageIId(iIdAnfrageI);

		// try {
		Anfrage anfrage = em.find(Anfrage.class, iIdAnfrageI);
		if (anfrage == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei setzeNEttoGesamtwert. Es gibt keine Anfrage mit iid "
							+ iIdAnfrageI);
		}
		anfrage.setNGesamtanfragewertinanfragewaehrung(nNettogesamtwertI);
		em.merge(anfrage);

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public BigDecimal berechneEinkaufswertIst(Integer iIdAnfrageI,
			String sArtikelartI, TheClientDto theClientDto) {

		BigDecimal bdEKwertIstO = Helper.getBigDecimalNull();
		AnfragepositionDto[] aAnfragepositionDtos = null;
		try {
			aAnfragepositionDtos = getAnfragepositionFac()
					.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		for (int i = 0; i < aAnfragepositionDtos.length; i++) {

			// alle mengenbehafteten Positionen beruecksichtigen
			if (aAnfragepositionDtos[i].getNMenge() != null
					&& aAnfragepositionDtos[i].getArtikelIId() != null) {
				
				if (aAnfragepositionDtos[i].getPositioniIdArtikelset() == null) {

					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAnfragepositionDtos[i].getArtikelIId(),
									theClientDto);

					BigDecimal preis = new BigDecimal(0);

					AnfragepositionlieferdatenDto lfDaten = null;
					try {
						lfDaten = getAnfragepositionFac()
								.anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
										aAnfragepositionDtos[i].getIId());
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					if (lfDaten != null
							&& lfDaten.getNNettogesamtpreisminusrabatt() != null) {
						preis = lfDaten.getNNettogesamtpreisminusrabatt();
					}

					BigDecimal bdBeitragDieserPosition = aAnfragepositionDtos[i]
							.getNMenge().multiply(preis);

					// je nach Artikelart beruecksichtigen
					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdEKwertIstO = bdEKwertIstO
									.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdEKwertIstO = bdEKwertIstO
									.add(bdBeitragDieserPosition);
						}
					}
				}
			}
		}

		return bdEKwertIstO;
	}

	/**
	 * Alle Anfragen zu einer Liefergruppenanfrage erzeugen.
	 * 
	 * @param iIdAnfrageI
	 *            PK der Liefergruppenanfrage
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der zuletzt erzeugten Anfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ArrayList<Integer> erzeugeAnfragenAusLiefergruppenanfrage(
			Integer iIdAnfrageI, ArrayList<Integer> bereitserzeugteAnfragen,
			TheClientDto theClientDto) {
		checkAnfrageIId(iIdAnfrageI);

		if (bereitserzeugteAnfragen == null) {
			bereitserzeugteAnfragen = new ArrayList<Integer>();
		}

		Integer iIdLetzteErzeugteAnfrage = null;

		AnfrageDto liefergruppenanfrageDto = anfrageFindByPrimaryKey(
				iIdAnfrageI, theClientDto);

		AnfragepositionDto[] aAnfragepositionDto = null;
		LieferantDto[] aLieferantDto = null;
		try {
			aAnfragepositionDto = getAnfragepositionFac()
					.anfragepositionFindByAnfrage(iIdAnfrageI, theClientDto);

			aLieferantDto = getLieferantFac().lieferantFindByLiefergruppeIId(
					liefergruppenanfrageDto.getLiefergruppeIId(), theClientDto);
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		if (aLieferantDto != null && aLieferantDto.length > 0) {
			// fuer jeden Lieferanten eine Anfrage zur Liefergruppenanfrage
			// erzeugen
			for (int i = 0; i < aLieferantDto.length; i++) {
				AnfrageDto anfrageDto = (AnfrageDto) liefergruppenanfrageDto
						.clone();

				// PJ 16726 Wenn der Lieferant einen andere Waehrung hat,
				// dann diese verwenden
				if (!anfrageDto.getWaehrungCNr().equals(
						aLieferantDto[i].getWaehrungCNr())) {
					anfrageDto
							.setWaehrungCNr(aLieferantDto[i].getWaehrungCNr());

				}
				try {
					anfrageDto
							.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
									getLocaleFac()
											.getWechselkurs2(
													getMandantFac()
															.mandantFindByPrimaryKey(
																	theClientDto
																			.getMandant(),
																	theClientDto)
															.getWaehrungCNr(),
													anfrageDto.getWaehrungCNr(),
													theClientDto).doubleValue()));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				anfrageDto.setArtCNr(AnfrageServiceFac.ANFRAGEART_LIEFERANT);
				anfrageDto.setLieferantIIdAnfrageadresse(aLieferantDto[i]
						.getIId());
				anfrageDto.setLiefergruppeIId(null);
				anfrageDto
						.setAnfrageIIdLiefergruppenanfrage(liefergruppenanfrageDto
								.getIId());

				if (aLieferantDto[i].getLieferartIId() != null) {
					anfrageDto.setLieferartIId(aLieferantDto[i]
							.getLieferartIId());
				}
				if (aLieferantDto[i].getZahlungszielIId() != null) {
					anfrageDto.setZahlungszielIId(aLieferantDto[i]
							.getZahlungszielIId());
				}
				if (aLieferantDto[i].getIdSpediteur() != null) {
					anfrageDto.setSpediteurIId(aLieferantDto[i]
							.getIdSpediteur());
				}

				iIdLetzteErzeugteAnfrage = getAnfrageFac().createAnfrage(
						anfrageDto, theClientDto);

				bereitserzeugteAnfragen.add(iIdLetzteErzeugteAnfrage);

				for (int j = 0; j < aAnfragepositionDto.length; j++) {
					AnfragepositionDto anfragepositionDto = (AnfragepositionDto) aAnfragepositionDto[j]
							.clone();
					anfragepositionDto.setBelegIId(iIdLetzteErzeugteAnfrage);

					if (anfragepositionDto.getNRichtpreis() != null
							&& !liefergruppenanfrageDto.getWaehrungCNr()
									.equals(aLieferantDto[i].getWaehrungCNr())) {
						anfragepositionDto
								.setNRichtpreis(anfragepositionDto
										.getNRichtpreis()
										.multiply(
												new BigDecimal(
														anfrageDto
																.getFWechselkursmandantwaehrungzubelegwaehrung())));
					}

					getAnfragepositionFac().createAnfrageposition(
							anfragepositionDto, theClientDto);
				}
			}

			// den Status der Liefergruppenanfrage auf Erledigt setzen
			liefergruppenanfrageDto
					.setStatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT);
			getAnfrageFac().setzeAnfragestatus(
					AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT,
					liefergruppenanfrageDto.getIId(), theClientDto);
		}

		return bereitserzeugteAnfragen;
	}

	public ArrayList<Integer> getAngelegteAnfragenNachUmwandlungDerLiefergruppenanfragen(
			Integer liefergruppeIId, TheClientDto theClientDto) {
		Session session = null;
		ArrayList<Integer> alErzeugteAnfragen = new ArrayList<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRAnfrage.class);
			// Filter auf Mandant
			c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_MANDANT_C_NR,
					theClientDto.getMandant()));
			c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_ANFRAGEART_C_NR,
					AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE));

			if (liefergruppeIId != null) {
				c.add(Restrictions.eq(
						AnfrageFac.FLR_ANFRAGE_LFLIEFERGRUPPE_I_ID,
						liefergruppeIId));
			}

			// Filter nach Status: nur angelegte
			c.add(Restrictions.or(Restrictions.eq(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT), Restrictions.eq(
					AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_OFFEN)));
			// nach RE-Nummer sortieren
			c.addOrder(Order.asc("c_nr"));
			// Query ausfuehren
			List<?> list = c.list();

			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRAnfrage item = (FLRAnfrage) iter.next();
				alErzeugteAnfragen = getAnfrageFac()
						.erzeugeAnfragenAusLiefergruppenanfrage(item.getI_id(),
								alErzeugteAnfragen, theClientDto);
			}
		} finally {
			if (session != null) {
				session.close();
			}

		}
		if (liefergruppeIId != null) {
			return alErzeugteAnfragen;
		}

		ArrayList<Integer> a = new ArrayList<Integer>();
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRAnfrage.class);
			// Filter auf Mandant
			c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_MANDANT_C_NR,
					theClientDto.getMandant()));
			c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_ANFRAGEART_C_NR,
					AnfrageServiceFac.ANFRAGEART_LIEFERANT));

			// Filter nach Status: nur angelegte
			c.add(Restrictions.eq(AnfrageFac.FLR_ANFRAGE_ANFRAGESTATUS_C_NR,
					AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT));
			// nach RE-Nummer sortieren
			c.addOrder(Order.asc("c_nr"));
			// Query ausfuehren
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRAnfrage item = (FLRAnfrage) iter.next();
				a.add(item.getI_id());
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return a;
	}

	/**
	 * Nach Kriterien, die der Benutzer bestimmt hat, Anfragen aus einem
	 * existierenden Bestellvorschlag erzeugen.
	 * 
	 * @param bestellvorschlagUeberleitungKriterienDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void erzeugeAnfragenAusBestellvorschlag(
			BestellvorschlagUeberleitungKriterienDto bestellvorschlagUeberleitungKriterienDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (bestellvorschlagUeberleitungKriterienDtoI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"bestellvorschlagUeberleitungKriterienDtoI == null"));
		}

		try {
			// alle Bestellvorschlaege entsprechend den Kriterien des Benutzers
			// holen,
			// sortiert wird nach Lieferanten und innerhalb der Lieferanten nach
			// Artikel.c_nr
			BestellvorschlagDto[] aBestellvorschlagDto = getListeBestellvorschlaege(
					bestellvorschlagUeberleitungKriterienDtoI, theClientDto);

			LieferantDto lieferantDto = null;
			Timestamp tLiefertermin = null;
			int iIndexAbarbeitung = -1; // Index der Position aus der Liste der
			// Bestellvorschlaege, die bearbeitet
			// werden
			int iSortPosition = 1; // iSort der Positionen, die zu einer Anfrage
			// angelegt werden

			// pro Lieferant wird eine Anfrage erstellt
			if (aBestellvorschlagDto != null && aBestellvorschlagDto.length > 0) {
				iIndexAbarbeitung++;

				while (iIndexAbarbeitung < aBestellvorschlagDto.length) {
					lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									aBestellvorschlagDto[iIndexAbarbeitung]
											.getILieferantId(),
									theClientDto);

					tLiefertermin = aBestellvorschlagDto[iIndexAbarbeitung]
							.getTLiefertermin();

					// Anfragekopf erstellen
					AnfrageDto anfrageDto = new AnfrageDto();
					anfrageDto.setMandantCNr(theClientDto.getMandant());
					anfrageDto
							.setArtCNr(AnfrageServiceFac.ANFRAGEART_LIEFERANT);
					anfrageDto
							.setStatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
					anfrageDto.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
					anfrageDto.setTBelegdatum(new Timestamp(System
							.currentTimeMillis()));
					anfrageDto.setLieferantIIdAnfrageadresse(lieferantDto
							.getIId());
					// ansprechpartner, liefergruppe, cbez, angebotnummer null
					anfrageDto.setTAnliefertermin(tLiefertermin);
					anfrageDto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
					anfrageDto
							.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
									getLocaleFac().getWechselkurs2(
											theClientDto
													.getSMandantenwaehrung(),
											lieferantDto.getWaehrungCNr(),
											theClientDto).doubleValue()));
					anfrageDto
							.setKostenstelleIId(bestellvorschlagUeberleitungKriterienDtoI
									.getKostenstelleIId());
					anfrageDto.setFAllgemeinerRabattsatz(lieferantDto
							.getNRabatt() == null ? new Double(0)
							: lieferantDto.getNRabatt());
					anfrageDto.setLieferartIId(lieferantDto.getLieferartIId());
					anfrageDto.setZahlungszielIId(lieferantDto
							.getZahlungszielIId());
					anfrageDto.setSpediteurIId(lieferantDto.getIdSpediteur());
					// anfragewert wird berechnet
					anfrageDto
							.setNTransportkosteninanfragewaehrung(new BigDecimal(
									0));
					// speziell in der Anfrage werden Kopf- und Fusstext besetzt
					anfrageDto.setBelegtextIIdKopftext(getAnfrageServiceFac()
							.anfragetextFindByMandantLocaleCNr(
									lieferantDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT, theClientDto)
							.getIId());
					anfrageDto.setBelegtextIIdFusstext(getAnfrageServiceFac()
							.anfragetextFindByMandantLocaleCNr(
									lieferantDto.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT, theClientDto)
							.getIId());
					// die restlichen Felder sind alle null

					anfrageDto.setIId(getAnfrageFac().createAnfrage(anfrageDto,
							theClientDto));

					// Flag bestimmen, das festlegt, ob die aktuelle Position
					// noch zu dieser Anfrage gehoert
					boolean bPositionGehoertZuDieserAnfrage = true;

					while (bPositionGehoertZuDieserAnfrage) {
						if (bestellvorschlagUeberleitungKriterienDtoI
								.getBBelegprolieferantprotermin()) {
							// in diesem Fall wird fuer jeden Lieferanten eine
							// Anfrage pro Liefertermin erzeugt
							if (iIndexAbarbeitung < aBestellvorschlagDto.length
									&& lieferantDto
											.getIId()
											.equals(aBestellvorschlagDto[iIndexAbarbeitung]
													.getILieferantId())
									&& tLiefertermin
											.equals(aBestellvorschlagDto[iIndexAbarbeitung]
													.getTLiefertermin())) {
								bPositionGehoertZuDieserAnfrage = true;
							} else {
								bPositionGehoertZuDieserAnfrage = false;
							}
						} else {
							// in den restlichen Faellen wird genau eine Anfrage
							// fuer jeden Lieferanten erzeugt
							if (iIndexAbarbeitung < aBestellvorschlagDto.length
									&& lieferantDto
											.getIId()
											.equals(aBestellvorschlagDto[iIndexAbarbeitung]
													.getILieferantId())) {
								bPositionGehoertZuDieserAnfrage = true;
							} else {
								bPositionGehoertZuDieserAnfrage = false;
							}
						}

						// alle Positionen zur Anfrage an einen Lieferanten
						// werden hier erfasst
						if (bPositionGehoertZuDieserAnfrage) {
							// jetzt die Positionen der Anfrage erstellen
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											aBestellvorschlagDto[iIndexAbarbeitung]
													.getIArtikelId(),
											theClientDto);

							AnfragepositionDto anfragepositionDto = new AnfragepositionDto();
							anfragepositionDto.setBelegIId(anfrageDto.getIId());
							anfragepositionDto.setISort(new Integer(
									iSortPosition));
							anfragepositionDto
									.setPositionsartCNr(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT);
							anfragepositionDto.setArtikelIId(artikelDto
									.getIId());
							anfragepositionDto
									.setBArtikelbezeichnunguebersteuert(new Short(
											(short) 0));
							anfragepositionDto
									.setNMenge(aBestellvorschlagDto[iIndexAbarbeitung]
											.getNZubestellendeMenge());
							anfragepositionDto.setEinheitCNr(artikelDto
									.getEinheitCNr());
							if (bestellvorschlagUeberleitungKriterienDtoI
									.getBRichtpreisUebernehmen()) {
								anfragepositionDto
										.setNRichtpreis(aBestellvorschlagDto[iIndexAbarbeitung]
												.getNNettogesamtpreis());
							} else {
								anfragepositionDto
										.setNRichtpreis(new BigDecimal(0));
							}
							getAnfragepositionFac().createAnfrageposition(
									anfragepositionDto, theClientDto);

							iIndexAbarbeitung++;
							iSortPosition++;
						}
					}

					iSortPosition = 1; // zuruecksetzen fuer die naechste
					// Anfrage
				}
			}
			for (int i = 0; i < aBestellvorschlagDto.length; i++) {
				getBestellvorschlagFac().removeBestellvorschlag(
						aBestellvorschlagDto[i]);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	/**
	 * Die Liste der Bestellvorschlaege holen, die den Kriterien des Benutzers
	 * entsprechen.
	 * 
	 * @param kritDtoI
	 *            die Kriterien des Benutzers
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BestellvorschlagDto[] die Bestellvorschlaege
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private BestellvorschlagDto[] getListeBestellvorschlaege(
			BestellvorschlagUeberleitungKriterienDto kritDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		BestellvorschlagDto[] aBestellvorschlagDto = null;

		Session session = null;

		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();

			// Criteria duerfen keine Texts oder Blobs enthalten!

			// Criteria anlegen fuer flrbestellvorschlag
			Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
			Criteria critArtikel = crit.createCriteria("flrartikel"); // UW->JE
			// Konstante

			// Einschraenken nach Mandant
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));

			// der Lieferant muss gesetzt sein
			crit.add(Restrictions.isNotNull("lieferant_i_id"));

			if (kritDtoI.getBBelegeinlieferant()) {
				// alle Bestellvorschlaege zu einem bestimmten Lieferanten
				crit.add(Restrictions.eq("lieferant_i_id",
						kritDtoI.getBelegeinlieferantLieferantIId())); // UW->JE
				// Kosntante

				critArtikel.addOrder(Property.forName("c_nr").asc());
			} else if (kritDtoI.getBBelegeinlieferanteintermin()) {
				// alle Bestellvorschlaege zu einem bestimmten Lieferanten und
				// einem bestimmten Termin
				crit.add(Restrictions.eq("lieferant_i_id",
						kritDtoI.getBelegeinlieferanteinterminLieferantIId())); // UW->JE
				// Konstante
				crit.add(Restrictions
						.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN,
								kritDtoI.getTBelegeinlieferanteinterminTermin())); // UW
				// -
				// >
				// JE
				// Konstante

				critArtikel.addOrder(Property.forName("c_nr").asc());
			} else if (kritDtoI.getBBelegprolieferant()) {
				// fuer jeden Lieferanten alle seine Bestellvorschlaege
				crit.addOrder(Property.forName("lieferant_i_id").asc()); // UW->JE
				// Konstante
				critArtikel.addOrder(Property.forName("c_nr").asc());
			} else if (kritDtoI.getBBelegprolieferantprotermin()) {
				// fuer jeden Lieferanten alle seine Bestellvorschlaege zu einem
				// bestimmten Termin
				crit.addOrder(Property.forName("lieferant_i_id").asc()); // UW->JE
				// Konstante
				crit.addOrder(Property
						.forName(
								BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN)
						.asc()); // UW->JE Konstante
				critArtikel.addOrder(Property.forName("c_nr").asc());
			}

			List<?> list = crit.list();

			aBestellvorschlagDto = new BestellvorschlagDto[list.size()];
			int iIndex = 0;
			Iterator<?> it = list.iterator();
			BestellvorschlagDto bestellvorschlagDto = null;

			while (it.hasNext()) {
				FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) it
						.next();

				bestellvorschlagDto = new BestellvorschlagDto();

				bestellvorschlagDto.setIId(flrbestellvorschlag.getI_id());
				bestellvorschlagDto.setCMandantCNr(flrbestellvorschlag
						.getMandant_c_nr());
				bestellvorschlagDto.setIArtikelId(flrbestellvorschlag
						.getArtikel_i_id());
				bestellvorschlagDto.setNZubestellendeMenge(flrbestellvorschlag
						.getN_zubestellendemenge());
				bestellvorschlagDto
						.setTLiefertermin((Timestamp) flrbestellvorschlag
								.getT_liefertermin());
				bestellvorschlagDto.setCBelegartCNr(flrbestellvorschlag
						.getBelegart_c_nr());
				bestellvorschlagDto.setIBelegartId(flrbestellvorschlag
						.getI_belegartid());
				bestellvorschlagDto.setILieferantId(flrbestellvorschlag
						.getLieferant_i_id());
				bestellvorschlagDto.setNNettoeinzelpreis(flrbestellvorschlag
						.getN_nettoeinzelpreis());
				// bestellvorschlagDto.setDRabattsatz(flrbestellvorschlag.
				// getFRabattsatz()); UW->JE
				bestellvorschlagDto.setNRabattbetrag(flrbestellvorschlag
						.getN_rabattbetrag());

				bestellvorschlagDto.setNNettogesamtpreis(flrbestellvorschlag
						.getN_nettogesamtpreis());

				aBestellvorschlagDto[iIndex] = bestellvorschlagDto;
				iIndex++;
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
					new Exception(t));
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}

		return aBestellvorschlagDto;
	}

	public void setzeVersandzeitpunktAufJetzt(Integer anfrageIId,
			String druckart) {
		if (anfrageIId != null) {
			Anfrage anfrage = em.find(Anfrage.class, anfrageIId);
			anfrage.setTVersandzeitpunkt(new Timestamp(System
					.currentTimeMillis()));
			anfrage.setCVersandtype(druckart);
			em.merge(anfrage);
			em.flush();
		}

	}

	@Override
	public void aktiviereBelegControlled(Integer iid, Timestamp t,
			TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		new BelegAktivierungController(this).aktiviereBelegControlled(iid, t, theClientDto);
	}
	
	@Override
	public Timestamp berechneBelegControlled(Integer iid,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return new BelegAktivierungController(this).berechneBelegControlled(iid, theClientDto);
	}

	@Override
	public boolean hatAenderungenNach(Integer iid, Timestamp t)
			throws EJBExceptionLP, RemoteException {
		Anfrage anfrage = em.find(Anfrage.class, iid);
		if(anfrage.getTAendern() != null && anfrage.getTAendern().after(t))
			return true;
		if(anfrage.getTManuellerledigt() != null && anfrage.getTManuellerledigt().after(t))
			return true;
		if(anfrage.getTStorniert() != null && anfrage.getTStorniert().after(t))
			return true;
		return false;
	}
	
	public void updateTAendern(Integer iid, TheClientDto theClientDto) {
		Anfrage anfrage = em.find(Anfrage.class, iid);
		anfrage.setPersonalIIdAendern(theClientDto.getIDPersonal());
		anfrage.setTAendern(getTimestamp());
		em.merge(anfrage);
		em.flush();
	}
}
