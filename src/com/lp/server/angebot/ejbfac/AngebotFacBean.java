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
package com.lp.server.angebot.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotpositionReport;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotDtoAssembler;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionDtoAssembler;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejbfac.BelegAktivierungController;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AngebotFacBean extends Facade implements AngebotFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Ein neues Angebot anlegen.
	 * 
	 * @param angebotDtoI
	 *            das neue Angebot
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Angebots
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAngebot(AngebotDto angebotDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Integer angebotIId = null;
		String angebotCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(angebotDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					angebotDtoI.getMandantCNr(), angebotDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_ANGEBOT,
					angebotDtoI.getMandantCNr(), theClientDto);

			angebotIId = bnr.getPrimaryKey();
			angebotCNr = f.format(bnr);

			if (angebotDtoI.getBMitzusammenfassung() == null) {
				angebotDtoI.setBMitzusammenfassung(Helper.boolean2Short(false));
			}
			angebotDtoI.setIId(angebotIId);
			angebotDtoI.setCNr(angebotCNr);
			angebotDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			angebotDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Angebot angebot = new Angebot(
					angebotDtoI.getIId(),
					angebotDtoI.getCNr(),
					angebotDtoI.getMandantCNr(),
					angebotDtoI.getArtCNr(),
					angebotDtoI.getStatusCNr(),
					angebotDtoI.getBelegartCNr(),
					angebotDtoI.getTBelegdatum(),
					angebotDtoI.getTAnfragedatum(),
					angebotDtoI.getTAngebotsgueltigkeitbis(),
					angebotDtoI.getKundeIIdAngebotsadresse(),
					angebotDtoI.getPersonalIIdVertreter(),
					angebotDtoI.getWaehrungCNr(),
					angebotDtoI.getFWechselkursmandantwaehrungzubelegwaehrung(),
					angebotDtoI.getAngeboteinheitCNr(), angebotDtoI
							.getKostenstelleIId(), angebotDtoI
							.getTNachfasstermin(), angebotDtoI
							.getFAuftragswahrscheinlichkeit(), angebotDtoI
							.getFVersteckterAufschlag(), angebotDtoI
							.getFAllgemeinerRabattsatz(), angebotDtoI
							.getFProjektierungsrabattsatz(), angebotDtoI
							.getLieferartIId(), angebotDtoI
							.getZahlungszielIId(), angebotDtoI
							.getSpediteurIId(), angebotDtoI.getIGarantie(),
					angebotDtoI.getPersonalIIdAnlegen(), angebotDtoI
							.getPersonalIIdAendern(), angebotDtoI
							.getBMitzusammenfassung());
			em.persist(angebot);
			em.flush();

			angebotDtoI.setTAnlegen(angebot.getTAnlegen());
			angebotDtoI.setTAendern(angebot.getTAendern());

			setAngebotFromAngebotDto(angebot, angebotDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return angebotIId;
	}

	/**
	 * Ein bestehendes Angebot stornieren.
	 * 
	 * @param angebotDtoI
	 *            das zu stornierende Angebot
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void storniereAngebot(AngebotDto angebotDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Angebot kann nicht storniert werden da kein Angebot mit iid "
									+ angebotDtoI.getIId() + " CNr "
									+ angebotDtoI.getCNr() + " vorhanden ist"));
		}
		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT);
			angebot.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			angebot.setTStorniert(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Angebot kann nicht storniert werden, Status : "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde storniert.");
	}

	/**
	 * Storno eines Angebots aufheben.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void stornoAufheben(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Storno kann nicht aufgehoben werden da Angebot mit der iid "
									+ iIdAngebotI + " nicht vorhanden ist"));
		}

		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setTStorniert(null);
			angebot.setPersonalIIdStorniert(null);
			angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebot.setTAendern(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Storno des Angebots kann nicht aufgehoben werden, Status : "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Stornierung wurde aufgehoben");
	}

	/**
	 * Wenn ein Angebot erledigt wird, muss ein Erledigungsgrund angegeben
	 * werden.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param cNrAngeboterledigungsgrundI
	 *            der Erledigungsgrund
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void angebotErledigen(Integer iIdAngebotI,
			String cNrAngeboterledigungsgrundI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNrAngeboterledigungsgrundI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angeboterledigungsgrundCNrI == null"));
		}
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Angebot kann nicht erledigt werden. Kein Angebot mit iid "
									+ iIdAngebotI + " vorhanden"));
		}

		if (!angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
			angebot.setAngeboterledigungsgrundCNr(cNrAngeboterledigungsgrundI);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Angebot kann nicht erledigt werden, Status : "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde erledigt, Erledigungsgrund: "
				+ cNrAngeboterledigungsgrundI);
	}

	/**
	 * Den Status eines Angebots von erledigt auf offen zuruecksetzen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void erledigungAufheben(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler in der Funktion erledigung aufheben. Kein Angebot mit iid "
									+ iIdAngebotI + " vorhanden"));
		}

		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setAngeboterledigungsgrundCNr(null);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung des Angebots kann nicht aufgehoben werden, Status: "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Die Erledigung wurde aufgehoben.");
	}

	// manuellerledigen: 0 Jeder Beleg hat in der Tabelle die Felder
	// - PERSONAL_I_ID_MANUELLERLEDIGT null erlaubt
	// - T_MANUELLERLEDIGT null erlaubt

	/**
	 * manuellerledigen: 1 Ein Angebot manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param cNrAngeboterledigungsgrundI
	 *            der Erledigungsgrund darf nicht null sein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void angebotManuellErledigen(Integer iIdAngebotI,
			String cNrAngeboterledigungsgrundI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		if (cNrAngeboterledigungsgrundI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngeboterledigungsgrundI == null"));
		}
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Es konnte kein angebot mit iid " + iIdAngebotI
									+ " gefunden werden"));
		}

		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
			angebot.setAngeboterledigungsgrundCNr(cNrAngeboterledigungsgrundI);
			angebot.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			angebot.setTManuellerledigt(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Angebot kann nicht manuell erledigt werden, Status : "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde manuell erledigt.");
	}

	/**
	 * manuellerledigen: 1 Ein Angebot manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param iIdAuftragI
	 *            der Auftrag darf nicht null sein
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean angebotManuellErledigendurchAuftrag(Integer iIdAngebotI,
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);

		boolean bKonditionenStimmenNichtZusammen = false;

		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		try {
			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei Angebot erledigen durch Auftrag. Es gibt kein Angebot mit iid "
										+ iIdAngebotI));
			}
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					iIdAuftragI);

			// PJ 15499
			if (!angebot.getLieferartIId().equals(auftragDto.getLieferartIId())
					|| !angebot.getZahlungszielIId().equals(
							auftragDto.getZahlungszielIId())
					|| !angebot.getSpediteurIId().equals(
							auftragDto.getSpediteurIId())) {
				bKonditionenStimmenNichtZusammen = true;
			}

			String lieferortAG = "";
			if (angebot.getCLieferartort() != null) {
				lieferortAG = angebot.getCLieferartort();
			}
			String lieferortAB = "";
			if (auftragDto.getCLieferartort() != null) {
				lieferortAB = auftragDto.getCLieferartort();
			}

			if (!lieferortAG.equals(lieferortAB)) {
				bKonditionenStimmenNichtZusammen = true;
			}

			if (angebot.getFProjektierungsrabattsatz().doubleValue() != auftragDto
					.getFProjektierungsrabattsatz().doubleValue()) {
				bKonditionenStimmenNichtZusammen = true;
			}
			if (angebot.getFVersteckteraufschlag().doubleValue() != auftragDto
					.getFVersteckterAufschlag().doubleValue()) {
				bKonditionenStimmenNichtZusammen = true;
			}
			if (angebot.getFAllgemeinerrabattsatz().doubleValue() != auftragDto
					.getFAllgemeinerRabattsatz().doubleValue()) {
				bKonditionenStimmenNichtZusammen = true;
			}

			if (angebot.getAngebotstatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
				angebot.setPersonalIIdManuellerledigt(theClientDto
						.getIDPersonal());
				angebot.setTManuellerledigt(getTimestamp());
				// PJ 14748
				if (angebot.getAngeboterledigungsgrundCNr() == null) {
					angebot.setAngeboterledigungsgrundCNr(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN);
				}
				auftragDto.setAngebotIId(iIdAngebotI);
				getAuftragFac().updateAuftragOhneWeitereAktion(auftragDto,
						theClientDto);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
						new Exception(
								"Angebot kann nicht manuell erledigt werden, Status : "
										+ angebot.getAngebotstatusCNr()));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Angebot wurde manuell erledigt.");
		return bKonditionenStimmenNichtZusammen;
	}

	/**
	 * manuellerledigen: 2 Den Status eines Angebots von 'Erledigt' auf 'Offen'
	 * setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void manuelleErledigungAufheben(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Manuelle erledigung konnte nicht aufgehoben werden da Angebot mit iid "
									+ iIdAngebotI + " nicht vorhandne ist"));
		}

		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			if (angebot.getPersonalIIdManuellerledigt() != null
					&& angebot.getTManuellerledigt() != null) {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
				angebot.setAngeboterledigungsgrundCNr(null);
				angebot.setPersonalIIdManuellerledigt(null);
				angebot.setTManuellerledigt(null);
			} else {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
				myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl das Angebot nicht manuell erledigt wurde, AngebotIId: "
						+ iIdAngebotI);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung des Angebots kann nicht aufgehoben werden, Status: "
									+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Manuelle Erledigung wurde aufgehoben.");
	}

	/**
	 * Ein bestehendes Angebot aktualisieren.
	 * 
	 * @param angebotDtoI
	 *            das bestehende Angebot
	 * @param waehrungOriCNrI
	 *            die urspruengliche Belegwaehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean updateAngebot(AngebotDto angebotDtoI,
			String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;
		checkAngebotDto(angebotDtoI);
		try {
			if (waehrungOriCNrI != null
					&& !waehrungOriCNrI.equals(angebotDtoI.getWaehrungCNr())) {
				AngebotpositionDto[] aAngebotpositionDto = getAngebotpositionFac()
						.angebotpositionFindByAngebotIId(angebotDtoI.getIId(),
								theClientDto);

				// die Positionswerte neu berechnen und abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(
						waehrungOriCNrI, angebotDtoI.getWaehrungCNr(),
						theClientDto);

				for (int i = 0; i < aAngebotpositionDto.length; i++) {
					if (aAngebotpositionDto[i].getNMenge() != null
							&& aAngebotpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aAngebotpositionDto[i]
								.getNEinzelpreis().multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac()
								.berechnePreisfelder(
										nNettoeinzelpreisInNeuerWaehrung,
										aAngebotpositionDto[i].getFRabattsatz(),
										aAngebotpositionDto[i]
												.getFZusatzrabattsatz(),
										aAngebotpositionDto[i].getMwstsatzIId(),
										4, // @todo Konstante PJ 3748
										theClientDto);

						aAngebotpositionDto[i]
								.setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aAngebotpositionDto[i]
								.setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aAngebotpositionDto[i]
								.setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aAngebotpositionDto[i]
								.setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aAngebotpositionDto[i]
								.setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getAngebotpositionFac().updateAngebotposition(
								aAngebotpositionDto[i], theClientDto);
					}
				}
			}

			angebotDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebotDtoI.setTAendern(getTimestamp());

			Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
			if (angebot == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei Update Angebot es gibt kein angebot mit iid "
										+ angebotDtoI.getIId() + " und Cnr "
										+ angebotDtoI.getCNr()));
			}
			// Wird der kunde geaendert muss man die Konditionen neu holen
			if (!angebotDtoI.getKundeIIdAngebotsadresse().equals(
					angebot.getKundeIIdAngebotsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						angebotDtoI.getKundeIIdAngebotsadresse(), theClientDto);
				KundeDto kundeDtoVorher = getKundeFac().kundeFindByPrimaryKey(
						angebot.getKundeIIdAngebotsadresse(), theClientDto);
				AngebotpositionDto[] aAngebotpositionDto = getAngebotpositionFac()
						.angebotpositionFindByAngebotIId(angebotDtoI.getIId(),
								theClientDto);

				ParametermandantDto parameterPositionskontierung = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_KUNDEN,
								ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung
						.getCWertAsObject();

				for (int i = 0; i < aAngebotpositionDto.length; i++) {
					if (aAngebotpositionDto[i].getPositionsartCNr().equals(
							AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
							|| aAngebotpositionDto[i]
									.getPositionsartCNr()
									.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {

						MwstsatzDto mwstsatzDto = getMandantFac()
								.mwstsatzFindByMwstsatzbezIIdAktuellster(
										kundeDto.getMwstsatzbezIId(),
										theClientDto);
						if (bDefaultMwstsatzAusArtikel
								&& aAngebotpositionDto[i]
										.getPositionsartCNr()
										.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											aAngebotpositionDto[i]
													.getArtikelIId(),
											theClientDto);
							if (artikelDto.getMwstsatzbezIId() != null) {
								mwstsatzDto = getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												artikelDto.getMwstsatzbezIId(),
												theClientDto);
							}

						}

						// SP503
						if (bDefaultMwstsatzAusArtikel
								&& aAngebotpositionDto[i]
										.getPositionsartCNr()
										.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

							// Wenn alter und neuer Kunde den gleichen MWST-Satz
							// haben, dann nichts tun
							MwstsatzDto mwstsatzDtoKundeNeu = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDto.getMwstsatzbezIId(),
											theClientDto);

							MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac()
									.mwstsatzFindByMwstsatzbezIIdAktuellster(
											kundeDtoVorher.getMwstsatzbezIId(),
											theClientDto);

							if (mwstsatzDtoKundeVorher.getFMwstsatz() == 0
									&& mwstsatzDtoKundeNeu.getFMwstsatz() > 0) {

								bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = true;
							}

							if (mwstsatzDtoKundeNeu.getIId().equals(
									mwstsatzDtoKundeVorher.getIId())) {
								continue;
							}
						}

						if (!aAngebotpositionDto[i].getMwstsatzIId().equals(
								mwstsatzDto.getIId())) {
							aAngebotpositionDto[i].setMwstsatzIId(mwstsatzDto
									.getIId());

							BigDecimal mwstBetrag = aAngebotpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(
											new BigDecimal(mwstsatzDto
													.getFMwstsatz()
													.doubleValue())
													.movePointLeft(2));
							aAngebotpositionDto[i].setNMwstbetrag(mwstBetrag);
							aAngebotpositionDto[i]
									.setNBruttoeinzelpreis(mwstBetrag.add(aAngebotpositionDto[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getAngebotpositionFac().updateAngebotposition(
									aAngebotpositionDto[i], theClientDto);
						}
					}
				}
				// CK: 2013-06-04 Gilt nicht mehr, da die Konditionen nun
				// vorher am Client bestaetigt werden muessen
				/*
				 * Double dAllgemeinerrabattsatz = new Double(0); if
				 * (kundeDto.getFRabattsatz() != null) { dAllgemeinerrabattsatz
				 * = kundeDto.getFRabattsatz(); }
				 * angebotDtoI.setFAllgemeinerRabattsatz
				 * (dAllgemeinerrabattsatz); if (kundeDto.getLieferartIId() !=
				 * null) {
				 * angebotDtoI.setLieferartIId(kundeDto.getLieferartIId()); } if
				 * (kundeDto.getZahlungszielIId() != null) {
				 * angebotDtoI.setZahlungszielIId(kundeDto
				 * .getZahlungszielIId()); } if (kundeDto.getSpediteurIId() !=
				 * null) {
				 * angebotDtoI.setSpediteurIId(kundeDto.getSpediteurIId()); }
				 */
				Integer iGarantie = new Integer(0);
				if (kundeDto.getIGarantieinmonaten() != null) {
					iGarantie = kundeDto.getIGarantieinmonaten();
				}
				angebotDtoI.setIGarantie(iGarantie);
			}
			setAngebotFromAngebotDto(angebot, angebotDtoI);
			pruefeUndSetzeAngebotstatusBeiAenderung(angebotDtoI.getIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * Die Daten eines Angebots aktualisieren ohne weitere Aenderungen
	 * vorzunehmen. Pflege der Akquisedaten.
	 * 
	 * @param angebotDtoI
	 *            AngebotDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateAngebotOhneWeitereAktion(AngebotDto angebotDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei update Angebot. Es konnte kein Angebot mit iid"));
		}
		setAngebotFromAngebotDto(angebot, angebotDtoI);
		angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
		angebot.setTAendern(getTimestamp());
		myLogger.exit("Das Angebot wurde aktualisiert, Status: "
				+ angebot.getAngebotstatusCNr());
	}

	/**
	 * Die Kopfdaten eines Angebots duerfen nur in bestimmten Stati geaendert
	 * werden. <br>
	 * Nachdem ein Angebot geaendert wurde, befindet es sich im Status ANGELEGT.
	 * 
	 * @param iIdAngebotI
	 *            PK des angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void pruefeUndSetzeAngebotstatusBeiAenderung(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = null;
		angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception(
							"Fehler bei PruefeAngebotUndSetzeAngebotstatusbeiAenderung. Es gibt kein Angebot mit der iid: "
									+ iIdAngebotI));
		}
		String sStatus = angebot.getAngebotstatusCNr();
		if (!sStatus.equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)
				&& !sStatus.equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(sStatus));
		}

		angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
		angebot.setNGesamtangebotswertinangebotswaehrung(null);
		myLogger.exit("Das Angebot wurde aktualisiert, Status: "
				+ angebot.getAngebotstatusCNr());
	}

	/**
	 * Die Kennung eines Angebots bestimmten.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String die Kennung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String getAngebotkennung(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		String cKennung = null;
		try {
			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei getAngebotkennung Angebot mit iid "
										+ iIdAngebotI + " existiert nicht"));
			}
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(
					angebot.getBelegartCNr(), theClientDto);
			StringBuffer sbKennung = new StringBuffer(
					belegartDto.getCKurzbezeichnung());
			sbKennung.append(" ").append(angebot.getCNr());
			cKennung = sbKennung.toString();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		myLogger.exit("Angebotkennung: " + cKennung);
		return cKennung;
	}

	/**
	 * Den Status eines Angebots auf 'Offen' setzen und seinen Wert berechnen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            String der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void aktiviereAngebot(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(iIdAngebotI, "iIdAngebot");
		pruefeAktivierbar(iIdAngebotI, theClientDto);
		// Wert berechnen
		berechneBeleg(iIdAngebotI, theClientDto);
		// und Status aendern
		aktiviereBeleg(iIdAngebotI, theClientDto);
	}

	@Override
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei aktiviereAngebot. Angobt mit iid "
									+ iid + " ist nicht vorhanden"));
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getAngebotpositionFac().getAnzahlMengenbehafteteAngebotpositionen(
				iid, theClientDto) == 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN,
					new Exception(
							"Fehler bei aktiviereAngebot. Es gibt keine Mengenbehafteten Positionen"));
		}
		if (!getTheJudgeFac().hatRecht(RechteFac.RECHT_ANGB_AKTIVIEREN,
				theClientDto)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
	}
	
	@Override
	public void aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
			// PJ 15233
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setTGedruckt(getTimestamp());
		}
	}
	
	@Override
	public void berechneBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot.getAngebotstatusCNr().equals(
				AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
			BigDecimal nNettowertGesamt = berechneNettowertGesamt(
					angebot.getIId(), theClientDto);
			if (angebot.getNKorrekturbetrag() != null) {
				nNettowertGesamt = nNettowertGesamt.add(angebot
						.getNKorrekturbetrag());
			}

			angebot.setNGesamtangebotswertinangebotswaehrung(nNettowertGesamt);
		}
	}

	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AngebotDto[] aAngebotDtos = null;
		Query query = em
				.createNamedQuery("AngebotfindByKundeIIdAngebotsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAngebotDtos = assembleAngebotDtos(cl);
		return aAngebotDtos;
	}

	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto) {
		AngebotDto[] aAngebotDtos = null;
		try {
			Query query = em
					.createNamedQuery("AngebotfindByKundeIIdAngebotsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAngebotDtos = assembleAngebotDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI"
					+ cNrMandantI, t);
		}
		return aAngebotDtos;
	}

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AngebotDto[] aAngebotDtos = null;
		Query query = em
				.createNamedQuery("AngebotfindByAnsprechpartnerIIdKundeMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAngebotDtos = assembleAngebotDtos(cl);
		return aAngebotDtos;
	}

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) {
		AngebotDto[] aAngebotDtos = null;
		try {
			Query query = em
					.createNamedQuery("AngebotfindByAnsprechpartnerIIdKundeMandantCNr");
			query.setParameter(1, iIdAnsprechpartnerI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAngebotDtos = assembleAngebotDtos(cl);
		} catch (Throwable th) {
			return null;
		}
		return aAngebotDtos;
	}

	public AngebotDto angebotFindByPrimaryKey(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AngebotDto angebotDto = angebotFindByPrimaryKeyOhneExec(iIdAngebotI);
		if (angebotDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
							"Fehler bei angebotfindbyPrimarykey. Es gibt kein Angebot mit iid "
									+ iIdAngebotI));
		}
		return angebotDto;
	}

	@Override
	public AngebotDto angebotFindByPrimaryKeyOhneExec(Integer iIdAngebotI) {
		AngebotDto angebotDto = null;
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);

		if (angebot != null)
			angebotDto = assembleAngebotDto(angebot);
		return angebotDto;
	}

	public AngebotDto angebotFindByCNrMandantCNrOhneEx(String cNr,
			String mandantCNr) {
		Query query = em.createNamedQuery(Angebot.QueryFindByCnrMandantCnr);
		query.setParameter("cnr", cNr);
		query.setParameter("mandant", mandantCNr);
		try {
			Angebot a = (Angebot) query.getSingleResult();
			return assembleAngebotDto(a);
		} catch (NoResultException ex) {
			return null;
		}
	}

	private void setAngebotFromAngebotDto(Angebot angebot, AngebotDto angebotDto) {
		angebot.setCNr(angebotDto.getCNr());
		angebot.setMandantCNr(angebotDto.getMandantCNr());
		angebot.setAngebotartCNr(angebotDto.getArtCNr());
		angebot.setAngebotstatusCNr(angebotDto.getStatusCNr());
		angebot.setBelegartCNr(angebotDto.getBelegartCNr());
		angebot.setTBelegdatum(angebotDto.getTBelegdatum());
		angebot.setTAnfragedatum(angebotDto.getTAnfragedatum());
		angebot.setTAngebotsgueltigkeitbis(angebotDto
				.getTAngebotsgueltigkeitbis());
		angebot.setKundeIIdAngebotsadresse(angebotDto
				.getKundeIIdAngebotsadresse());
		angebot.setAnsprechpartnerIIdKunde(angebotDto
				.getAnsprechpartnerIIdKunde());
		angebot.setPersonalIIdVertreter(angebotDto.getPersonalIIdVertreter());
		angebot.setCBez(angebotDto.getCBez());
		angebot.setWaehrungCNrAngebotswaehrung(angebotDto.getWaehrungCNr());
		angebot.setFWechselkursmandantwaehrungzuangebotswaehrung(angebotDto
				.getFWechselkursmandantwaehrungzubelegwaehrung());
		angebot.setILieferzeitinstunden(angebotDto.getILieferzeitinstunden());
		angebot.setAngeboteinheitCNr(angebotDto.getAngeboteinheitCNr());
		angebot.setKostenstelleIId(angebotDto.getKostenstelleIId());
		angebot.setAngeboterledigungsgrundCNr(angebotDto
				.getAngeboterledigungsgrundCNr());
		angebot.setTNachfasstermin(angebotDto.getTNachfasstermin());
		angebot.setTRealisierungstermin(angebotDto.getTRealisierungstermin());
		angebot.setFAuftragswahrscheinlichkeit(angebotDto
				.getFAuftragswahrscheinlichkeit());
		angebot.setXAblageort(angebotDto.getXAblageort());

		if (angebotDto.getFVersteckterAufschlag() != null) {
			angebot.setFVersteckteraufschlag(angebotDto
					.getFVersteckterAufschlag());
		} else {
			angebot.setFVersteckteraufschlag(0D);
		}
		if (angebotDto.getFAllgemeinerRabattsatz() != null) {
			angebot.setFAllgemeinerrabattsatz(angebotDto
					.getFAllgemeinerRabattsatz());
		} else {
			angebot.setFAllgemeinerrabattsatz(0D);
		}
		if (angebotDto.getFProjektierungsrabattsatz() != null) {
			angebot.setFProjektierungsrabattsatz(angebotDto
					.getFProjektierungsrabattsatz());
		} else {
			angebot.setFProjektierungsrabattsatz(0D);
		}
		angebot.setLieferartIId(angebotDto.getLieferartIId());
		angebot.setZahlungszielIId(angebotDto.getZahlungszielIId());
		angebot.setSpediteurIId(angebotDto.getSpediteurIId());
		angebot.setIGarantie(angebotDto.getIGarantie());
		angebot.setNGesamtangebotswertinangebotswaehrung(angebotDto
				.getNGesamtwertinbelegwaehrung());
		angebot.setAngebottextIIdKopftext(angebotDto.getBelegtextIIdKopftext());
		angebot.setXKopftextuebersteuert(angebotDto.getXKopftextuebersteuert());
		angebot.setAngebottextIIdFusstext(angebotDto.getBelegtextIIdFusstext());
		angebot.setXFusstextuebersteuert(angebotDto.getXFusstextuebersteuert());
		angebot.setXExternerkommentar(angebotDto.getXExternerkommentar());
		angebot.setXInternerkommentar(angebotDto.getXInternerkommentar());
		angebot.setTGedruckt(angebotDto.getTGedruckt());
		angebot.setPersonalIIdStorniert(angebotDto.getPersonalIIdStorniert());
		angebot.setTStorniert(angebotDto.getTStorniert());
		angebot.setPersonalIIdManuellerledigt(angebotDto
				.getPersonalIIdManuellerledigt());
		angebot.setTManuellerledigt(angebotDto.getTManuellerledigt());
		angebot.setPersonalIIdAnlegen(angebotDto.getPersonalIIdAnlegen());
		angebot.setTAnlegen(angebotDto.getTAnlegen());
		angebot.setPersonalIIdAendern(angebotDto.getPersonalIIdAendern());
		angebot.setTAendern(angebotDto.getTAendern());
		angebot.setCKundenanfrage(angebotDto.getCKundenanfrage());
		angebot.setBMitzusammenfassung(angebotDto.getBMitzusammenfassung());
		angebot.setCLieferartort(angebotDto.getCLieferartort());
		angebot.setProjektIId(angebotDto.getProjektIId());
		angebot.setNKorrekturbetrag(angebotDto.getNKorrekturbetrag());
		em.merge(angebot);
		em.flush();
	}

	private AngebotDto assembleAngebotDto(Angebot angebot) {
		return AngebotDtoAssembler.createDto(angebot);
	}

	private AngebotDto[] assembleAngebotDtos(Collection<?> angebots) {
		List<AngebotDto> list = new ArrayList<AngebotDto>();
		if (angebots != null) {
			Iterator<?> iterator = angebots.iterator();
			while (iterator.hasNext()) {
				Angebot angebot = (Angebot) iterator.next();
				list.add(assembleAngebotDto(angebot));
			}
		}
		AngebotDto[] returnArray = new AngebotDto[list.size()];
		return (AngebotDto[]) list.toArray(returnArray);
	}

	private void checkAngebotDto(AngebotDto angebotDtoI) throws EJBExceptionLP {
		if (angebotDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angebotDtoI == null"));
		}

		myLogger.info("AngebotDto: " + angebotDtoI.toString());
	}

	private void checkAngebotIId(Integer iIdAngebotI) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}

		myLogger.info("AngebotIId: " + iIdAngebotI.toString());
	}

	
	
	public BigDecimal berechneGestehungswertSoll(Integer iIdAngebotI,
			String sArtikelartI, boolean bMitEigengefertigtenStuecklisten,
			TheClientDto theClientDto) {
		final String METHOD_NAME = "berechneGestehungswertSoll";
		myLogger.entry();
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertSollO = Helper.getBigDecimalNull();

		try {

			AngebotDto angebotDto = angebotFindByPrimaryKey(iIdAngebotI,theClientDto);
			AngebotpositionDto[] aAngebotpositionDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(iIdAngebotI,theClientDto);

			for (int i = 0; i < aAngebotpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAngebotpositionDtos[i].getNMenge() != null && aAngebotpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAngebotpositionDtos[i].getArtikelIId(),
									theClientDto);

					if (bMitEigengefertigtenStuecklisten == false) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
										oArtikelDto.getIId(), theClientDto);
						if (stklDto != null
								&& Helper.short2boolean(stklDto
										.getBFremdfertigung()) == false) {
							continue;
						}
					}

					// das Hauptlager des Mandanten
					LagerDto oHauptlagerDto = getLagerFac()
							.getHauptlagerDesMandanten(theClientDto);

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal bdGestehungspreisSoll = new BigDecimal(0);
					if (aAngebotpositionDtos[i].getPositioniIdArtikelset() == null) {

						Query query = em
								.createNamedQuery("AngebotpositionpositionfindByPositionIIdArtikelset");
						query.setParameter(1, aAngebotpositionDtos[i].getIId());
						Collection<?> angebotpositionDtos = query
								.getResultList();
						AngebotpositionDto[] zugehoerigeABPosDtos = AngebotpositionDtoAssembler
								.createDtos(angebotpositionDtos);

						if (zugehoerigeABPosDtos.length == 0) {
							bdGestehungspreisSoll = getLagerFac()
									.getGestehungspreisZumZeitpunkt(
											oArtikelDto.getIId(),
											oHauptlagerDto.getIId(),
											angebotDto.getTBelegdatum(),
											theClientDto);
						} else {

							for (int k = 0; k < zugehoerigeABPosDtos.length; k++) {
								bdGestehungspreisSoll = bdGestehungspreisSoll
										.add(getLagerFac()
												.getGestehungspreisZumZeitpunkt(
														zugehoerigeABPosDtos[k]
																.getArtikelIId(),
														oHauptlagerDto.getIId(),
														angebotDto
																.getTBelegdatum(),
														theClientDto));
							}
						}

					}

					// je nach Artikelart beruecksichtigen

					if (bdGestehungspreisSoll != null) {

						BigDecimal bdBeitragDieserPosition = aAngebotpositionDtos[i]
								.getNMenge().multiply(bdGestehungspreisSoll);

						if (sArtikelartI
								.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO
										.add(bdBeitragDieserPosition);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(
									ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO
										.add(bdBeitragDieserPosition);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Soll : "
				+ bdGestehungswertSollO.toString());

		return bdGestehungswertSollO;
	}

	
	public BigDecimal berechneVerkaufswertSoll(Integer iIAngebotI,
			String sArtikelartI, TheClientDto theClientDto) {
		if (iIAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIAngebotI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertSollO = Helper.getBigDecimalNull();

		try {
			AngebotpositionDto[] aAngebotpositionDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(iIAngebotI,theClientDto);

			for (int i = 0; i < aAngebotpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAngebotpositionDtos[i].getNMenge() != null && aAngebotpositionDtos[i].getArtikelIId() != null
						&& aAngebotpositionDtos[i].getPositioniIdArtikelset() == null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									aAngebotpositionDtos[i].getArtikelIId(),
									theClientDto);

					// je nach Artikelart beruecksichtigen
					BigDecimal bdBeitragDieserPosition = aAngebotpositionDtos[i]
							.getNMenge()
							.multiply(
									aAngebotpositionDtos[i]
											.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO
									.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO
									.add(bdBeitragDieserPosition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Soll : "
				+ bdVerkaufswertSollO.toString());

		return bdVerkaufswertSollO;
	}
	
	/**
	 * Berechne den Gesamtwert eines bestimmten Angebots in der
	 * Angebotswaehrung. <br>
	 * Beruecksichtigt werden alle positiv mengenbehafteten Positionen.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return BigDecimal der Gesamtwert des Angebots
	 */
	public BigDecimal berechneNettowertGesamt(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotDto = null;
		BigDecimal nNettowertGesamt = new BigDecimal(0);
		try {
			angebotDto = angebotFindByPrimaryKey(iIdAngebotI, theClientDto);
			// Step 1: Wenn der NettowertGesamt NULL und der Status ANGELEGT
			// ist, dann den Wert aus den Positionen berechnen
			if (angebotDto.getStatusCNr().equals(
							AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				AngebotpositionDto[] alle = null;
				alle = getAngebotpositionFac()
						.angebotpositionFindByAngebotIIdOhneAlternative(
								iIdAngebotI, theClientDto);
				nNettowertGesamt = getBelegVerkaufFac()
						.getGesamtwertinBelegwaehrung(alle, angebotDto);

				for (AngebotpositionDto angebotPositionDto : alle) {
					if (AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME
							.equals(angebotPositionDto.getPositionsartCNr())) {
						getAngebotpositionFac().updateAngebotposition(
								angebotPositionDto, theClientDto);
					}
				}
			} else
			// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
			// NettowertGesamt noch in der Tabelle steht
			if (angebotDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
				nNettowertGesamt = new BigDecimal(0);
			} else {
				// Step 3: den NettowertGesamt aus der Tabelle lesen
				if (angebotDto.getNGesamtwertinbelegwaehrung() != null) {
					nNettowertGesamt = angebotDto
							.getNGesamtwertinbelegwaehrung();
				}
			}
			// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
			// gilt 15,4
		} catch (RemoteException e) {
		}
		return nNettowertGesamt;
	}


	/**
	 * Wenn die Zu- und Abschlaege in den Positionen geaendert wurden, dann
	 * werden im Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAngebotKonditionen(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			FLRAngebotpositionReport[] aFLRAngebotposition = holeAngebotpositionen(
					session, iIdAngebotI, true, // nur mengenbehaftete
					false, // auch negative und 0 Mengen
					false); // alternative Positionen werden beruecksichtigt

			for (int i = 0; i < aFLRAngebotposition.length; i++) {
				getAngebotpositionFac().befuelleZusaetzlichePreisfelder(
						aFLRAngebotposition[i].getI_id(), theClientDto);
			}

			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception(
								"Fehler bei updateAngebotKonidition. Es gibt keine Angebot mit iid "
										+ iIdAngebotI));
			}
			angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebot.setTAendern(getTimestamp());
			if (!angebot.getAngebotstatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				angebot.setNGesamtangebotswertinangebotswaehrung(berechneNettowertGesamt(
						iIdAngebotI, theClientDto));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
			}
		}
	}

	/**
	 * Methode zum Erzeugen eines neues Angebots als Kopie eines bestehenden
	 * Angebots. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Angebots
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeAngebotAusAngebot(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotBasisDto = null;
		try {
			angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdAngebotKopie = null;

		try {
			AngebotDto angebotDto = (AngebotDto) angebotBasisDto.clone();

			angebotDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
							getLocaleFac().getWechselkurs2(
									theClientDto.getSMandantenwaehrung(),
									angebotDto.getWaehrungCNr(), theClientDto)
									.doubleValue()));

			// IMS 2379
			ParametermandantDto parametermandantDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOT,
							ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

			int iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto
					.getCWertAsObject()).intValue();

			// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
			Timestamp tAngebotGueltigkeitsdauer = new Timestamp(
					gc.getTimeInMillis());

			Timestamp timestamp = getTimestamp();
			angebotDto.setTBelegdatum(timestamp);
			angebotDto.setTAnfragedatum(timestamp);
			angebotDto.setTAngebotsgueltigkeitbis(tAngebotGueltigkeitsdauer);

			iIdAngebotKopie = createAngebot(angebotDto, theClientDto);

			// UW 22.03.06 alle Positionen kopieren, das bleibt auf Bean Basis
			AngebotpositionDto[] aAngebotpositionBasis = getAngebotpositionFac()
					.angebotpositionFindByAngebotIId(iIdAngebotI, theClientDto);
			Integer positionIIdSet = null;
			for (int i = 0; i < aAngebotpositionBasis.length; i++) {
				AngebotpositionDto angebotpositionDto = (AngebotpositionDto) aAngebotpositionBasis[i]
						.clone();

				// Wenn sich sie MWST seither geaendert hat
				if (angebotpositionDto.getMwstsatzIId() != null) {
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(
									angebotpositionDto.getMwstsatzIId(),
									theClientDto);

					mwstsatzDto = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									mwstsatzDto.getIIMwstsatzbezId(),
									theClientDto);
					BigDecimal mwstBetrag = Helper.getProzentWert(
							angebotpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					angebotpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					angebotpositionDto.setNMwstbetrag(mwstBetrag);
					angebotpositionDto.setNBruttoeinzelpreis(angebotpositionDto
							.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAngebotpositionBasis[i]
						.getPositionsartCNr()
						.equals(AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
					Integer von = getAngebotpositionFac().getPositionNummer(
							aAngebotpositionBasis[i].getZwsVonPosition());
					angebotpositionDto
							.setZwsVonPosition(getAngebotpositionFac()
									.getPositionIIdFromPositionNummer(
											iIdAngebotKopie, von));
					Integer bis = getAngebotpositionFac().getPositionNummer(
							aAngebotpositionBasis[i].getZwsBisPosition());
					angebotpositionDto
							.setZwsBisPosition(getAngebotpositionFac()
									.getPositionIIdFromPositionNummer(
											iIdAngebotKopie, bis));
				}

				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() != null) {
					angebotpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}
				angebotpositionDto.setBelegIId(iIdAngebotKopie);
				if (angebotpositionDto.getPositioniIdArtikelset() == null) {
					positionIIdSet = getAngebotpositionFac()
							.createAngebotposition(angebotpositionDto, false,
									theClientDto);
				} else {
					getAngebotpositionFac().createAngebotposition(
							angebotpositionDto, false, theClientDto);
				}

			}

			myLogger.exit("Das Angebot wurde mit "
					+ aAngebotpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAngebotKopie;
	}

	/**
	 * Methode zum Erzeugen eines Auftrags als Kopie eines bestehenden Angebots. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @param bMitZeitDaten
	 *            boolean
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Auftrags
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeAuftragAusAngebot(Integer iIdAngebotI,
			boolean bMitZeitDaten, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotBasisDto = null;

		try {
			angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdAuftrag = null;

		try {

			ParametermandantDto parametermandantLieferzeitDto = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
			int defaultLieferzeitAuftrag = ((Integer) parametermandantLieferzeitDto
					.getCWertAsObject()).intValue();

			AuftragDto auftragDto = (AuftragDto) angebotBasisDto
					.cloneAsAuftragDto(defaultLieferzeitAuftrag * 24);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotBasisDto.getKundeIIdAngebotsadresse(), theClientDto);

			// SP458
			if (kundeDto.getPartnerIIdRechnungsadresse() != null) {
				KundeDto kundeRechnungsadresseDto = getKundeFac()
						.kundeFindByiIdPartnercNrMandantOhneExc(
								kundeDto.getPartnerIIdRechnungsadresse(),
								kundeDto.getMandantCNr(), theClientDto);
				if (kundeRechnungsadresseDto != null) {
					auftragDto
							.setKundeIIdRechnungsadresse(kundeRechnungsadresseDto
									.getIId());
				}
			}

			auftragDto.setLagerIIdAbbuchungslager(kundeDto
					.getLagerIIdAbbuchungslager());

			auftragDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
							getLocaleFac().getWechselkurs2(
									getMandantFac().mandantFindByPrimaryKey(
											theClientDto.getMandant(),
											theClientDto).getWaehrungCNr(),
									auftragDto.getCAuftragswaehrung(),
									theClientDto).doubleValue()));

			iIdAuftrag = getAuftragFac()
					.createAuftrag(auftragDto, theClientDto);

			// UW 22.03.06 alle Positionen aus dem Angebot uebernehmen, das
			// bleibt auf Bean Basis
			AngebotpositionDto[] aAngebotpositionBasis = getAngebotpositionFac()
					.angebotpositionFindByAngebotIId(iIdAngebotI, theClientDto);

			Integer positionIIdSet = null;

			for (int i = 0; i < aAngebotpositionBasis.length; i++) {
				AuftragpositionDto auftragpositionDto = aAngebotpositionBasis[i]
						.cloneAsAuftragposition();

				// Wenn sich sie MWST seither geaendert hat
				if (auftragpositionDto.getMwstsatzIId() != null) {
					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindByPrimaryKey(
									auftragpositionDto.getMwstsatzIId(),
									theClientDto);

					mwstsatzDto = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									mwstsatzDto.getIIMwstsatzbezId(),
									theClientDto);
					BigDecimal mwstBetrag = Helper.getProzentWert(
							auftragpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					auftragpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					auftragpositionDto.setNMwstbetrag(mwstBetrag);
					auftragpositionDto.setNBruttoeinzelpreis(auftragpositionDto
							.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() != null) {
					auftragpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}

				auftragpositionDto.setBelegIId(iIdAuftrag);
				auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragDto
						.getDLiefertermin());

				if (aAngebotpositionBasis[i].getPositionsartCNr().equals(
						AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
					// WH: 16.12.05 Aus der AGStueckliste wird bei der
					// Ueberleitung in den Auftrag
					// eine Handeingabeposition
					auftragpositionDto
							.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);

					if (auftragpositionDto.getCBez() == null
							|| auftragpositionDto.getCBez().length() == 0) {
						auftragpositionDto.setCBez("");
					}

					auftragpositionDto.setCZusatzbez(aAngebotpositionBasis[i]
							.getPositionsartCNr());
					auftragpositionDto
							.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				}

				if (aAngebotpositionBasis[i]
						.getPositionsartCNr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)) {
					Integer von = getAngebotpositionFac().getPositionNummer(
							aAngebotpositionBasis[i].getZwsVonPosition());
					auftragpositionDto
							.setZwsVonPosition(getAuftragpositionFac()
									.getPositionIIdFromPositionNummer(
											iIdAuftrag, von));
					Integer bis = getAngebotpositionFac().getPositionNummer(
							aAngebotpositionBasis[i].getZwsBisPosition());
					auftragpositionDto
							.setZwsBisPosition(getAuftragpositionFac()
									.getPositionIIdFromPositionNummer(
											iIdAuftrag, bis));
				}

				// damit werden auch die Reservierungen angelegt
				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() == null) {
					positionIIdSet = getAuftragpositionFac()
							.createAuftragposition(auftragpositionDto, false,
									theClientDto);
				} else {
					getAuftragpositionFac().createAuftragposition(
							auftragpositionDto, false, theClientDto);
				}

			}

			// wenn das Angebot noch nicht erledigt war, muss der Status
			// geaendert werden
			if (!angebotBasisDto.getStatusCNr().equals(
					AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
				angebotErledigen(
						iIdAngebotI,
						AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN,
						theClientDto);
			}
			if (bMitZeitDaten) {
				getZeiterfassungFac()
						.konvertiereAngebotszeitenNachAuftragzeiten(
								iIdAngebotI, iIdAuftrag, theClientDto);
			}

			myLogger.exit("Der Auftrag wurde mit "
					+ aAngebotpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAuftrag;
	}

	/**
	 * Methode zum Erzeugen eines Lieferscheins aus einem bestehenden Angebot.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK des neuen Lieferscheins
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAngebot(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkAngebotIId(iIdAngebotI);

		Integer lieferscheinIId = null;

		try {
			AngebotDto angebotBasisDto = getAngebotFac()
					.angebotFindByPrimaryKey(iIdAngebotI, theClientDto);

			// Schritt 1: Den Auftrag zum Angebot erzeugen
			Integer auftragIId = erzeugeAuftragAusAngebot(iIdAngebotI, false,
					theClientDto);

			// Schritt 2: Den Auftrag automatisch verarbeiten -> Status Offen
			AuftragDto auftragBasisDto = getAuftragFac()
					.auftragFindByPrimaryKey(auftragIId);

			KundeDto kundeBasisDto = getKundeFac().kundeFindByPrimaryKey(
					auftragBasisDto.getKundeIIdAuftragsadresse(), theClientDto);

			auftragBasisDto.setAuftragtextIIdKopftext(getAuftragServiceFac()
					.auftragtextFindByMandantLocaleCNr(
							theClientDto.getMandant(),
							kundeBasisDto.getPartnerDto()
									.getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_KOPFTEXT, theClientDto).getIId());

			auftragBasisDto.setAuftragtextIIdFusstext(getAuftragServiceFac()
					.auftragtextFindByMandantLocaleCNr(
							theClientDto.getMandant(),
							kundeBasisDto.getPartnerDto()
									.getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_FUSSTEXT, theClientDto).getIId());

			getAuftragFac().updateAuftrag(auftragBasisDto, null, theClientDto);

			// pruefen, ob das Hauptlager des Mandanten existiert wegen der
			// nachfolgenden Wertebrechnung
			LagerDto hauptlagerDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);

			// den Auftrag aktivieren
			getAuftragFac().aktiviereAuftrag(auftragBasisDto.getIId(),
					theClientDto);

			// den Auftrag drucken -> Status Offen
			getAuftragReportFac().printAuftragbestaetigung(auftragIId, null,
					Boolean.TRUE, null, theClientDto);

			// Schritt 3: Einen Lieferschein zum Auftrag erzeugen
			lieferscheinIId = getAuftragFac().erzeugeLieferscheinAusAuftrag(
					auftragIId, null, null, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return lieferscheinIId;
	}

	/**
	 * Methode zum Erzeugen einer neuen Rechnung als Kopie eines bestehenden
	 * Angebots. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAngebotI
	 *            PK des bestehenden Angebots
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Rechnung
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer erzeugeRechnungAusAngebot(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotBasisDto = null;
		try {
			angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(
					iIdAngebotI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		Integer iIdRechnung = null;
		return iIdRechnung;
	}

	/**
	 * Ueber Hibernate alle berechnungsrelevanten Positionen eines Angebots
	 * holen. <br>
	 * Diese Methode mu&szlig; innerhalb einer offenen Hibernate Session aufgerufen
	 * werden. <br>
	 * 
	 * @todo diese Methode muesste eigentlich in der AngebotpositionFac
	 *       sitzen... PJ 3790
	 * 
	 * @param sessionI
	 *            die Hibernate Session
	 * @param iIdAngebotI
	 *            PK des Angebots
	 * @param bNurMengenbehafteteI
	 *            nur mengenbehaftete Positionen beruecksichtigen
	 * @param bNurPositiveMengenI
	 *            nur positive Mengen beruecksichtigen; wird bei
	 *            !bNurMengenbehafteteI nicht ausgewertet
	 * @param bOhneAlternativenI
	 *            alternative Positionen werden nicht beruecksichtigt
	 * @return FLRAngebotpositionReport[] die berechnungsrelevanten Positionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private FLRAngebotpositionReport[] holeAngebotpositionen(Session sessionI,
			Integer iIdAngebotI, boolean bNurMengenbehafteteI,
			boolean bNurPositiveMengenI, boolean bOhneAlternativenI)
			throws EJBExceptionLP {
		FLRAngebotpositionReport[] aFLRAngebotpositionReport = null;

		try {
			Criteria crit = sessionI
					.createCriteria(FLRAngebotpositionReport.class);

			crit.add(Restrictions.eq(
					AngebotpositionFac.FLR_ANGEBOTPOSITION_ANGEBOT_I_ID,
					iIdAngebotI));

			if (bNurMengenbehafteteI) {
				crit.add(Restrictions
						.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));

				if (bNurPositiveMengenI) {
					crit.add(Restrictions.gt(
							AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE,
							new BigDecimal(0)));
				}
			}

			// nur Positionen beruecksichtigen, die keine Alternative sind
			if (bOhneAlternativenI) {
				crit.add(Restrictions.eq(
						AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE,
						new Short((short) 0)));
			}

			crit.addOrder(Order.asc("i_sort"));

			// Liste aller Positionen, die behandelt werden sollen
			List<?> list = crit.list();
			aFLRAngebotpositionReport = new FLRAngebotpositionReport[list
					.size()];
			aFLRAngebotpositionReport = (FLRAngebotpositionReport[]) list
					.toArray(aFLRAngebotpositionReport);
		} catch (HibernateException he) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
					new Exception(he));
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return aFLRAngebotpositionReport;
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer angebotIId,
			String druckart) {
		if (angebotIId != null) {
			Angebot angebot = em.find(Angebot.class, angebotIId);
			angebot.setTVersandzeitpunkt(new Timestamp(System
					.currentTimeMillis()));
			angebot.setCVersandtype(druckart);
			em.merge(angebot);
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
		Angebot angebot = em.find(Angebot.class, iid);
		if(angebot.getTAendern() != null && angebot.getTAendern().after(t))
			return true;
		if(angebot.getTManuellerledigt() != null && angebot.getTManuellerledigt().after(t))
			return true;
		if(angebot.getTStorniert() != null && angebot.getTStorniert().after(t))
			return true;
		return false;
	}
}
