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
package com.lp.server.angebot.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
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
import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotpositionReport;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotDtoAssembler;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionDtoAssembler;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.rechnung.ejb.Mmz;
import com.lp.server.rechnung.service.MmzDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejbfac.BelegAktivierungFac;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.ejbfac.Versionizer;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.MwstsatzEvaluateCache;
import com.lp.server.util.Validator;
import com.lp.server.util.ZwsPositionMapper;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AngebotFacBean extends Facade implements AngebotFac, IAktivierbar {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private BelegAktivierungFac belegAktivierungFac;

	/**
	 * Ein neues Angebot anlegen.
	 * 
	 * @param angebotDtoI  das neue Angebot
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer PK des neuen Angebots
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer createAngebot(AngebotDto angebotDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Integer angebotIId = null;
		String angebotCNr = null;

		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(angebotDtoI.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(angebotDtoI.getMandantCNr(),
					angebotDtoI.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr, PKConst.PK_ANGEBOT,
					angebotDtoI.getMandantCNr(), theClientDto);

			angebotIId = bnr.getPrimaryKey();
			angebotCNr = f.formatMitStellenZufall(bnr);

			if (angebotDtoI.getBMitzusammenfassung() == null) {
				angebotDtoI.setBMitzusammenfassung(Helper.boolean2Short(false));
			}
			if (angebotDtoI.getBMindermengenzuschlag() == null) {
				angebotDtoI.setBMindermengenzuschlag(Helper.boolean2Short(false));
			}

			angebotDtoI.setIId(angebotIId);
			angebotDtoI.setCNr(angebotCNr);
			angebotDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			angebotDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());

			if (angebotDtoI.getKundeIIdRechnungsadresse() == null) {
				angebotDtoI.setKundeIIdRechnungsadresse(angebotDtoI.getKundeIIdAngebotsadresse());
			}
			if (angebotDtoI.getKundeIIdLieferadresse() == null) {
				angebotDtoI.setKundeIIdLieferadresse(angebotDtoI.getKundeIIdAngebotsadresse());
			}

			Angebot angebot = new Angebot(angebotDtoI.getIId(), angebotDtoI.getCNr(), angebotDtoI.getMandantCNr(),
					angebotDtoI.getArtCNr(), angebotDtoI.getStatusCNr(), angebotDtoI.getBelegartCNr(),
					angebotDtoI.getTBelegdatum(), angebotDtoI.getTAnfragedatum(),
					angebotDtoI.getTAngebotsgueltigkeitbis(), angebotDtoI.getKundeIIdAngebotsadresse(),
					angebotDtoI.getKundeIIdRechnungsadresse(), angebotDtoI.getKundeIIdLieferadresse(),
					angebotDtoI.getPersonalIIdVertreter(), angebotDtoI.getWaehrungCNr(),
					angebotDtoI.getFWechselkursmandantwaehrungzubelegwaehrung(), angebotDtoI.getAngeboteinheitCNr(),
					angebotDtoI.getKostenstelleIId(), angebotDtoI.getTNachfasstermin(),
					angebotDtoI.getFAuftragswahrscheinlichkeit(), angebotDtoI.getFVersteckterAufschlag(),
					angebotDtoI.getFAllgemeinerRabattsatz(), angebotDtoI.getFProjektierungsrabattsatz(),
					angebotDtoI.getLieferartIId(), angebotDtoI.getZahlungszielIId(), angebotDtoI.getSpediteurIId(),
					angebotDtoI.getIGarantie(), angebotDtoI.getPersonalIIdAnlegen(),
					angebotDtoI.getPersonalIIdAendern(), angebotDtoI.getBMitzusammenfassung(),
					angebotDtoI.getBMindermengenzuschlag());
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
	 * @param angebotDtoI  das zu stornierende Angebot
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void storniereAngebot(AngebotDto angebotDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Angebot kann nicht storniert werden da kein Angebot mit iid " + angebotDtoI.getIId()
							+ " CNr " + angebotDtoI.getCNr() + " vorhanden ist"));
		}
		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)
				|| angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT);
			angebot.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			angebot.setTStorniert(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Angebot kann nicht storniert werden, Status : " + angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde storniert.");
	}

	/**
	 * Storno eines Angebots aufheben.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void stornoAufheben(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Storno kann nicht aufgehoben werden da Angebot mit der iid " + iIdAngebotI
							+ " nicht vorhanden ist"));
		}

		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setTStorniert(null);
			angebot.setPersonalIIdStorniert(null);
			angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebot.setTAendern(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Storno des Angebots kann nicht aufgehoben werden, Status : " + angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Stornierung wurde aufgehoben");
	}

	/**
	 * Wenn ein Angebot erledigt wird, muss ein Erledigungsgrund angegeben werden.
	 * 
	 * @param iIdAngebotI                 PK des Angebots
	 * @param cNrAngeboterledigungsgrundI der Erledigungsgrund
	 * @param theClientDto                der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void angebotErledigen(Integer iIdAngebotI, String cNrAngeboterledigungsgrundI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (cNrAngeboterledigungsgrundI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("angeboterledigungsgrundCNrI == null"));
		}
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
					"Angebot kann nicht erledigt werden. Kein Angebot mit iid " + iIdAngebotI + " vorhanden"));
		}

		if (!angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
			angebot.setAngeboterledigungsgrundCNr(cNrAngeboterledigungsgrundI);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Angebot kann nicht erledigt werden, Status : " + angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde erledigt, Erledigungsgrund: " + cNrAngeboterledigungsgrundI);
	}

	/**
	 * Den Status eines Angebots von erledigt auf offen zuruecksetzen.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void erledigungAufheben(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
					"Fehler in der Funktion erledigung aufheben. Kein Angebot mit iid " + iIdAngebotI + " vorhanden"));
		}

		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setAngeboterledigungsgrundCNr(null);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Die Erledigung des Angebots kann nicht aufgehoben werden, Status: "
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
	 * @param iIdAngebotI                 PK des Angebots
	 * @param cNrAngeboterledigungsgrundI der Erledigungsgrund darf nicht null sein
	 * @param theClientDto                der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void angebotManuellErledigen(Integer iIdAngebotI, String cNrAngeboterledigungsgrundI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		if (cNrAngeboterledigungsgrundI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrAngeboterledigungsgrundI == null"));
		}
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Es konnte kein angebot mit iid " + iIdAngebotI + " gefunden werden"));
		}

		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
			angebot.setAngeboterledigungsgrundCNr(cNrAngeboterledigungsgrundI);
			angebot.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			angebot.setTManuellerledigt(getTimestamp());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
					"Angebot kann nicht manuell erledigt werden, Status : " + angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Angebot wurde manuell erledigt.");
	}

	/**
	 * manuellerledigen: 1 Ein Angebot manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param iIdAuftragI  der Auftrag darf nicht null sein
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public String angebotManuellErledigendurchAuftrag(Integer iIdAngebotI, ArrayList<Integer> iIdAuftragIs,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);

		String auftraegeMitFalschenKonditionen = null;

		try {

			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
						"Fehler bei Angebot erledigen durch Auftrag. Es gibt kein Angebot mit iid " + iIdAngebotI));
			}

			for (Integer iIdAuftragI : iIdAuftragIs) {

				boolean bKonditionenStimmenNichtZusammen = false;

				if (iIdAuftragI == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
							new Exception("iIdAuftragI == null"));
				}

				AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(iIdAuftragI);

				// PJ 15499
				if (!angebot.getLieferartIId().equals(auftragDto.getLieferartIId())
						|| !angebot.getZahlungszielIId().equals(auftragDto.getZahlungszielIId())
						|| !angebot.getSpediteurIId().equals(auftragDto.getSpediteurIId())) {
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

				if (angebot.getFProjektierungsrabattsatz().doubleValue() != auftragDto.getFProjektierungsrabattsatz()
						.doubleValue()) {
					bKonditionenStimmenNichtZusammen = true;
				}
				if (angebot.getFVersteckteraufschlag().doubleValue() != auftragDto.getFVersteckterAufschlag()
						.doubleValue()) {
					bKonditionenStimmenNichtZusammen = true;
				}
				if (angebot.getFAllgemeinerrabattsatz().doubleValue() != auftragDto.getFAllgemeinerRabattsatz()
						.doubleValue()) {
					bKonditionenStimmenNichtZusammen = true;
				}

				auftragDto.setAngebotIId(iIdAngebotI);
				// SP4383
				auftragDto.setProjektIId(angebot.getProjektIId());
				getAuftragFac().updateAuftragOhneWeitereAktion(auftragDto, theClientDto);

				if (bKonditionenStimmenNichtZusammen == true) {
					if (auftraegeMitFalschenKonditionen == null) {
						auftraegeMitFalschenKonditionen = auftragDto.getCNr();
					} else {
						auftraegeMitFalschenKonditionen += ", " + auftragDto.getCNr();
					}
				}

			}
			if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT);
				angebot.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
				angebot.setTManuellerledigt(getTimestamp());
				// PJ 14748
				if (angebot.getAngeboterledigungsgrundCNr() == null) {
					angebot.setAngeboterledigungsgrundCNr(AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN);
				}

			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(
						"Angebot kann nicht manuell erledigt werden, Status : " + angebot.getAngebotstatusCNr()));
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Angebot wurde manuell erledigt.");
		return auftraegeMitFalschenKonditionen;
	}

	/**
	 * manuellerledigen: 2 Den Status eines Angebots von 'Erledigt' auf 'Offen'
	 * setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void manuelleErledigungAufheben(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Manuelle erledigung konnte nicht aufgehoben werden da Angebot mit iid " + iIdAngebotI
							+ " nicht vorhandne ist"));
		}

		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
			if (angebot.getPersonalIIdManuellerledigt() != null && angebot.getTManuellerledigt() != null) {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
				angebot.setAngeboterledigungsgrundCNr(null);
				angebot.setPersonalIIdManuellerledigt(null);
				angebot.setTManuellerledigt(null);
			} else {
				angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
				myLogger.logKritisch(
						"Status Erledigt wurde aufgehoben, obwohl das Angebot nicht manuell erledigt wurde, AngebotIId: "
								+ iIdAngebotI);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception("Die Erledigung des Angebots kann nicht aufgehoben werden, Status: "
							+ angebot.getAngebotstatusCNr()));
		}
		myLogger.exit("Manuelle Erledigung wurde aufgehoben.");
	}

	/**
	 * Ein bestehendes Angebot aktualisieren.
	 * 
	 * @param angebotDtoI     das bestehende Angebot
	 * @param waehrungOriCNrI die urspruengliche Belegwaehrung
	 * @param theClientDto    der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public boolean updateAngebot(AngebotDto angebotDtoI, String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = false;
		checkAngebotDto(angebotDtoI);
		try {
			if (waehrungOriCNrI != null && !waehrungOriCNrI.equals(angebotDtoI.getWaehrungCNr())) {
				AngebotpositionDto[] aAngebotpositionDto = getAngebotpositionFac()
						.angebotpositionFindByAngebotIId(angebotDtoI.getIId(), theClientDto);

				// die Positionswerte neu berechnen und abspeichern
				BigDecimal ffWechselkurs = getLocaleFac().getWechselkurs2(waehrungOriCNrI, angebotDtoI.getWaehrungCNr(),
						theClientDto);

				for (int i = 0; i < aAngebotpositionDto.length; i++) {
					if (aAngebotpositionDto[i].getNMenge() != null
							&& aAngebotpositionDto[i].getNEinzelpreis() != null) {
						BigDecimal nNettoeinzelpreisInNeuerWaehrung = aAngebotpositionDto[i].getNEinzelpreis()
								.multiply(ffWechselkurs);

						VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac().berechnePreisfelder(
								nNettoeinzelpreisInNeuerWaehrung, aAngebotpositionDto[i].getFRabattsatz(),
								aAngebotpositionDto[i].getFZusatzrabattsatz(), aAngebotpositionDto[i].getMwstsatzIId(),
								4, // @todo Konstante PJ 3748
								theClientDto);

						aAngebotpositionDto[i].setNEinzelpreis(verkaufspreisDto.einzelpreis);
						aAngebotpositionDto[i].setNRabattbetrag(verkaufspreisDto.rabattsumme);
						aAngebotpositionDto[i].setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
						aAngebotpositionDto[i].setNMwstbetrag(verkaufspreisDto.mwstsumme);
						aAngebotpositionDto[i].setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

						// alle Preisfelder incl. der zusaetzlichen Preisfelder
						// befuellen
						getAngebotpositionFac().updateAngebotposition(aAngebotpositionDto[i], theClientDto);
					}
				}
			}

			angebotDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebotDtoI.setTAendern(getTimestamp());

			Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
			if (angebot == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						new Exception("Fehler bei Update Angebot es gibt kein angebot mit iid " + angebotDtoI.getIId()
								+ " und Cnr " + angebotDtoI.getCNr()));
			}

			// SP8308
			if (!angebot.getTBelegdatum().equals(angebotDtoI.getTBelegdatum())) {
				checkMwstsaetzeImZeitraum(angebot.getTBelegdatum(), angebotDtoI.getTBelegdatum(),
						angebotDtoI.getMandantCNr());
			}

			// SP8598 Wenn Projekt entfernt wird, dann auch in zugehoerigen Auftragegen
			// entfernen
			if ((angebotDtoI.getProjektIId() == null && angebot.getProjektIId() != null)
					|| angebotDtoI.getProjektIId() != null && angebot.getProjektIId() == null
					|| (angebotDtoI.getProjektIId() != null && angebot.getProjektIId() != null
							&& !angebotDtoI.getProjektIId().equals(angebot.getProjektIId()))) {
				Query query = em.createNamedQuery("AuftragfindByAngebotIId");
				query.setParameter(1, angebotDtoI.getIId());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Auftrag ab = (Auftrag) it.next();
					ab.setProjektIId(angebotDtoI.getProjektIId());
					em.merge(ab);
					em.flush();

				}

			}

			// Wird der kunde geaendert muss man die Konditionen neu holen
			if (!angebotDtoI.getKundeIIdAngebotsadresse().equals(angebot.getKundeIIdAngebotsadresse())) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDtoI.getKundeIIdAngebotsadresse(),
						theClientDto);
				KundeDto kundeDtoVorher = getKundeFac().kundeFindByPrimaryKey(angebot.getKundeIIdAngebotsadresse(),
						theClientDto);
				AngebotpositionDto[] aAngebotpositionDto = getAngebotpositionFac()
						.angebotpositionFindByAngebotIId(angebotDtoI.getIId(), theClientDto);

				ParametermandantDto parameterPositionskontierung = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);

				boolean bDefaultMwstsatzAusArtikel = (Boolean) parameterPositionskontierung.getCWertAsObject();

				for (int i = 0; i < aAngebotpositionDto.length; i++) {
					if (aAngebotpositionDto[i].getPositionsartCNr().equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)
							|| aAngebotpositionDto[i].getPositionsartCNr()
									.equals(AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE)) {
						/*
						 * MwstsatzDto mwstsatzDto = getMandantFac()
						 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
						 * theClientDto);
						 */
						MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(kundeDto.getMwstsatzbezIId(),
								angebotDtoI.getTBelegdatum(), theClientDto);
						if (bDefaultMwstsatzAusArtikel && aAngebotpositionDto[i].getPositionsartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(aAngebotpositionDto[i].getArtikelIId(), theClientDto);
							if (artikelDto.getMwstsatzbezIId() != null) {
								/*
								 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
								 * artikelDto.getMwstsatzbezIId(), theClientDto);
								 */
								mwstsatzDto = getMandantFac().mwstsatzZuDatumValidate(artikelDto.getMwstsatzbezIId(),
										angebotDtoI.getTBelegdatum(), theClientDto);
							}
						}

						// SP503
						if (bDefaultMwstsatzAusArtikel && aAngebotpositionDto[i].getPositionsartCNr()
								.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

							// Wenn alter und neuer Kunde den gleichen MWST-Satz
							// haben, dann nichts tun
							/*
							 * MwstsatzDto mwstsatzDtoKundeNeu =
							 * getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
							 * kundeDto.getMwstsatzbezIId(), theClientDto);
							 * 
							 * MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac()
							 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDtoVorher.getMwstsatzbezIId(),
							 * theClientDto);
							 */
							MwstsatzDto mwstsatzDtoKundeNeu = getMandantFac().mwstsatzZuDatumValidate(
									kundeDto.getMwstsatzbezIId(), angebotDtoI.getTBelegdatum(), theClientDto);
							MwstsatzDto mwstsatzDtoKundeVorher = getMandantFac().mwstsatzZuDatumValidate(
									kundeDtoVorher.getMwstsatzbezIId(), angebotDtoI.getTBelegdatum(), theClientDto);
							if (mwstsatzDtoKundeVorher.getFMwstsatz() == 0 && mwstsatzDtoKundeNeu.getFMwstsatz() > 0) {
								bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = true;
							}

							if (mwstsatzDtoKundeNeu.getIId().equals(mwstsatzDtoKundeVorher.getIId())) {
								continue;
							}
						}

						if (!aAngebotpositionDto[i].getMwstsatzIId().equals(mwstsatzDto.getIId())) {
							aAngebotpositionDto[i].setMwstsatzIId(mwstsatzDto.getIId());

							BigDecimal mwstBetrag = aAngebotpositionDto[i]
									.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().multiply(
											new BigDecimal(mwstsatzDto.getFMwstsatz().doubleValue()).movePointLeft(2));
							aAngebotpositionDto[i].setNMwstbetrag(mwstBetrag);
							aAngebotpositionDto[i].setNBruttoeinzelpreis(mwstBetrag.add(
									aAngebotpositionDto[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
							getAngebotpositionFac().updateAngebotposition(aAngebotpositionDto[i], theClientDto);
						}
					}
				}
				// CK: 2013-06-04 Gilt nicht mehr, da die Konditionen nun
				// vorher am Client bestaetigt werden muessen
				/*
				 * Double dAllgemeinerrabattsatz = new Double(0); if (kundeDto.getFRabattsatz()
				 * != null) { dAllgemeinerrabattsatz = kundeDto.getFRabattsatz(); }
				 * angebotDtoI.setFAllgemeinerRabattsatz (dAllgemeinerrabattsatz); if
				 * (kundeDto.getLieferartIId() != null) {
				 * angebotDtoI.setLieferartIId(kundeDto.getLieferartIId()); } if
				 * (kundeDto.getZahlungszielIId() != null) {
				 * angebotDtoI.setZahlungszielIId(kundeDto .getZahlungszielIId()); } if
				 * (kundeDto.getSpediteurIId() != null) {
				 * angebotDtoI.setSpediteurIId(kundeDto.getSpediteurIId()); }
				 */
				Integer iGarantie = new Integer(0);
				if (kundeDto.getIGarantieinmonaten() != null) {
					iGarantie = kundeDto.getIGarantieinmonaten();
				}
				angebotDtoI.setIGarantie(iGarantie);
			}

			Integer iGJAlt = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(), angebot.getTBelegdatum());
			Integer iGJNeu = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(),
					angebotDtoI.getTBelegdatum());
			if (!iGJNeu.equals(iGJAlt)) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
						new Exception("Es wurde versucht, das LS " + angebotDtoI.getCNr() + " auf "
								+ angebotDtoI.getTBelegdatum() + " (GJ " + iGJNeu + ") umzudatieren"));
			}

			setAngebotFromAngebotDto(angebot, angebotDtoI);
			pruefeUndSetzeAngebotstatusBeiAenderung(angebotDtoI.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben;
	}

	/**
	 * Die Daten eines Angebots aktualisieren ohne weitere Aenderungen vorzunehmen.
	 * Pflege der Akquisedaten.
	 * 
	 * @param angebotDtoI  AngebotDto
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateAngebotOhneWeitereAktion(AngebotDto angebotDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotDto(angebotDtoI);
		Angebot angebot = em.find(Angebot.class, angebotDtoI.getIId());
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Fehler bei update Angebot. Es konnte kein Angebot mit iid"));
		}
		setAngebotFromAngebotDto(angebot, angebotDtoI);
		angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
		angebot.setTAendern(getTimestamp());
		myLogger.exit("Das Angebot wurde aktualisiert, Status: " + angebot.getAngebotstatusCNr());
	}

	/**
	 * Die Kopfdaten eines Angebots duerfen nur in bestimmten Stati geaendert
	 * werden. <br>
	 * Nachdem ein Angebot geaendert wurde, befindet es sich im Status ANGELEGT.
	 * 
	 * @param iIdAngebotI  PK des angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void pruefeUndSetzeAngebotstatusBeiAenderung(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Angebot angebot = null;
		angebot = em.find(Angebot.class, iIdAngebotI);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
					"Fehler bei PruefeAngebotUndSetzeAngebotstatusbeiAenderung. Es gibt kein Angebot mit der iid: "
							+ iIdAngebotI));
		}
		String sStatus = angebot.getAngebotstatusCNr();
		if (!sStatus.equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)
				&& !sStatus.equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS, new Exception(sStatus));
		}

		angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
		angebot.setNGesamtangebotswertinangebotswaehrung(null);
		myLogger.exit("Das Angebot wurde aktualisiert, Status: " + angebot.getAngebotstatusCNr());

		mindermengenzuschlagEntfernen(assembleAngebotDto(angebot), theClientDto);

	}

	/**
	 * Die Kennung eines Angebots bestimmten.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @return String die Kennung
	 * @throws EJBExceptionLP Ausnahme
	 */
	public String getAngebotkennung(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		String cKennung = null;
		try {
			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
						"Fehler bei getAngebotkennung Angebot mit iid " + iIdAngebotI + " existiert nicht"));
			}
			BelegartDto belegartDto = getLocaleFac().belegartFindByPrimaryKey(angebot.getBelegartCNr(), theClientDto);
			StringBuffer sbKennung = new StringBuffer(belegartDto.getCKurzbezeichnung());
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
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto String der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
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
	public void pruefeAktivierbar(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Fehler bei aktiviereAngebot. Angobt mit iid " + iid + " ist nicht vorhanden"));
		}
		// Ohne Positionen darf der Beleg nicht aktiviert werden.
		if (getAngebotpositionFac().getAnzahlMengenbehafteteAngebotpositionen(iid, theClientDto) == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN,
					new Exception("Fehler bei aktiviereAngebot. Es gibt keine Mengenbehafteten Positionen"));
		}

		pruefeObZusammenfassungErfuellt(Helper.isTrue(angebot.getBMitzusammenfassung()), angebot.getIId(),
				theClientDto);

		pruefeZwischensumme(iid, theClientDto);
	}

	private void pruefeZwischensumme(Integer angebotId, TheClientDto theClientDto) throws RemoteException {
		AngebotpositionDto[] allPos = getAngebotpositionFac().angebotpositionFindByAngebotIId(angebotId);
		getBelegVerkaufFac().validiereZwsAufGleichenMwstSatzThrow(allPos);
	}

	private void pruefeObZusammenfassungErfuellt(boolean mitZusammenfassung, Integer angebotId,
			TheClientDto theClientDto) throws RemoteException {
		if (!mitZusammenfassung)
			return;

		AngebotpositionDto[] allPos = getAngebotpositionFac().angebotpositionFindByAngebotIId(angebotId, theClientDto);
		Set<Integer> posIdsNotInZws = getBelegVerkaufFac().calculatePositionsNotInZws(allPos);
		if (posIdsNotInZws.size() != 0) {

			List<Integer> posNrs = new ArrayList<Integer>();
			for (Integer posId : posIdsNotInZws) {
				Integer posNr = getAngebotpositionFac().getPositionNummer(posId);
				posNrs.add(posNr);
			}
			Collections.sort(posNrs, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			throw EJBExcFactory.angebotPositionenOhneZws(posNrs);
		}
	}

	/*
	 * public Set<Integer> prepareIntZwsPositions( BelegpositionVerkaufDto[]
	 * positionDtos, BelegVerkaufDto belegVerkaufDto) { Set<Integer>
	 * modifiedPositions = new HashSet<Integer>(); for (BelegpositionVerkaufDto
	 * positionDto : positionDtos) { if (!positionDto.isIntelligenteZwischensumme())
	 * continue;
	 * 
	 * if (positionDto.getZwsVonPosition() == null ||
	 * positionDto.getZwsBisPosition() == null) continue;
	 * 
	 * int vonIndex = getIndexOfBelegPosition( positionDto.getZwsVonPosition(),
	 * positionDtos); int bisIndex = getIndexOfBelegPositionEnd(
	 * positionDto.getZwsBisPosition(), positionDtos);
	 * 
	 * if (vonIndex == -1 || bisIndex == -1) continue;
	 * 
	 * for (int i = vonIndex; i <= bisIndex; i++) { // positionDtos[i] //
	 * .setNNettoeinzelpreisplusversteckteraufschlag(positionDtos[i] //
	 * .getNNettoeinzelpreis()); // positionDtos[i] //
	 * .setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(positionDtos[i] //
	 * .getNNettoeinzelpreis()); berechneBelegpositionVerkauf(positionDtos[i],
	 * belegVerkaufDto); modifiedPositions.add(i); } } return modifiedPositions; }
	 */

	@Override
	public BelegPruefungDto aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
			// PJ 15233
			angebot.setAngebotstatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
			angebot.setTGedruckt(getTimestamp());

			// SP9000
			Kunde kunde = em.find(Kunde.class, angebot.getKundeIIdAngebotsadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kunde.getPartnerIId(),
					angebot.getAnsprechpartnerIIdKunde(), theClientDto);
			Kunde kundeLieferadresse = em.find(Kunde.class, angebot.getKundeIIdLieferadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kundeLieferadresse.getPartnerIId(),
					angebot.getAnsprechpartnerIIdLieferadresse(), theClientDto);
			Kunde kundeRechnungsadresse = em.find(Kunde.class, angebot.getKundeIIdRechnungsadresse());
			getAnsprechpartnerFac().pruefeAnsprechpartnerZugehoerigkeit(kundeRechnungsadresse.getPartnerIId(),
					angebot.getAnsprechpartnerIIdRechnungsadresse(), theClientDto);

		}
		return null;
	}

	@Override
	public Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		Angebot angebot = em.find(Angebot.class, iid);
		if (angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {

			// PJ22552
			ParametermandantDto parameterInitialkostenArtikel = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
					ParameterFac.PARAMETER_INITIALKOSTEN_ARTIKEL);

			String initialkostenArtikel = parameterInitialkostenArtikel.getCWert();
			if (initialkostenArtikel != null && initialkostenArtikel.trim().length() > 0) {

				ArtikelDto aDtoInitialkosten = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(initialkostenArtikel,
						theClientDto.getMandant());
				if (aDtoInitialkosten != null) {

					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebot.getKundeIIdRechnungsadresse(),
							theClientDto);
					ParametermandantDto parameterPosKont = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
					boolean isPositionskontierung = ((Boolean) parameterPosKont.getCWertAsObject()).booleanValue();

					Integer mwstsatzBezId = aDtoInitialkosten.getMwstsatzbezIId();
					if (!isPositionskontierung) {

						mwstsatzBezId = kundeDto.getMwstsatzbezIId();
					}
					MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(mwstsatzBezId,
							angebot.getTBelegdatum(), theClientDto);

					// Zuerst alle INITIALKOSTEN entfernen
					AngebotpositionDto[] alle = getAngebotpositionFac()
							.angebotpositionFindByAngebotIIdOhneAlternative(angebot.getIId(), theClientDto);
					for (int i = 0; i < alle.length; i++) {
						if (alle[i].getArtikelIId() != null
								&& alle[i].getArtikelIId().equals(aDtoInitialkosten.getIId())) {
							getAngebotpositionFac().removeAngebotposition(alle[i], theClientDto);

						}
					}

					alle = getAngebotpositionFac().angebotpositionFindByAngebotIIdOhneAlternative(angebot.getIId(),
							theClientDto);

					for (int i = 0; i < alle.length; i++) {

						if (alle[i].getAgstklIId() != null) {
							AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(alle[i].getAgstklIId());
							if (agstklDto.getNInitialkosten() != null
									&& agstklDto.getNInitialkosten().doubleValue() != 0) {
								Integer iSortNaechstePosition = null;
								if (alle.length - 1 > i) {
									AngebotpositionDto agPosDto = getAngebotpositionFac()
											.angebotpositionFindByPrimaryKey(alle[i + 1].getIId(), theClientDto);
									iSortNaechstePosition = agPosDto.getISort();
									getAngebotpositionFac().sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
											angebot.getIId(), iSortNaechstePosition, theClientDto);

								}

								AngebotpositionDto posIntitialkostenDto = new AngebotpositionDto();
								posIntitialkostenDto.setArtikelIId(aDtoInitialkosten.getIId());
								posIntitialkostenDto.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
								posIntitialkostenDto.setBelegIId(angebot.getIId());
								posIntitialkostenDto.setNMenge(new BigDecimal(1));
								posIntitialkostenDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
								posIntitialkostenDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
								posIntitialkostenDto.setBAlternative(Helper.boolean2Short(false));

								BigDecimal bdNettoeinzel = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
										agstklDto.getNInitialkosten(), agstklDto.getWaehrungCNr(),
										angebot.getWaehrungCNrAngebotswaehrung(),
										new java.sql.Date(angebot.getTBelegdatum().getTime()), theClientDto);
								// Belegwaehrung
								// umrechnen
								posIntitialkostenDto
										.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(bdNettoeinzel);
								posIntitialkostenDto.setNNettoeinzelpreis(bdNettoeinzel);
								posIntitialkostenDto.setNEinzelpreis(bdNettoeinzel);
								posIntitialkostenDto.setFRabattsatz(0D);
								posIntitialkostenDto.setNRabattbetrag(BigDecimal.ZERO);
								posIntitialkostenDto.setFZusatzrabattsatz(0D);
								posIntitialkostenDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

								BigDecimal mwstBetrag = posIntitialkostenDto
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
										.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));

								posIntitialkostenDto.setNMwstbetrag(mwstBetrag);

								posIntitialkostenDto.setNBruttoeinzelpreis(posIntitialkostenDto
										.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().add(mwstBetrag));

								posIntitialkostenDto.setISort(iSortNaechstePosition);

								getAngebotpositionFac().createAngebotposition(posIntitialkostenDto, theClientDto);
							}

						}

					}

				} else {
					// Initialkostenartikel nicht vorhanen
					ArrayList al = new ArrayList();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INITIALKOSTENARTIKEL_NICHT_VORHANDEN, al,
							new Exception("INITIALKOSTENARTIKEL_NICHT_VORHANDEN: " + initialkostenArtikel));
				}
			}

			// Mindermengenzuschlag PJ21361

			if (Helper.short2boolean(angebot.getBMindermengenzuschlag())) {

				mindermengenzuschlagEntfernen(assembleAngebotDto(angebot), theClientDto);

				BigDecimal nNettowertGesamt = berechneNettowertGesamt(angebot.getIId(), theClientDto);

				BigDecimal bdWertInMandantenwaehrung = nNettowertGesamt.divide(
						new BigDecimal(angebot.getFWechselkursmandantwaehrungzuangebotswaehrung()), 4,
						BigDecimal.ROUND_HALF_EVEN);

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebot.getKundeIIdRechnungsadresse(),
						theClientDto);
				Integer landIId = null;
				if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
					landIId = kundeDto.getPartnerDto().getLandplzortDto().getIlandID();
				}

				MmzDto mmzDto = getRechnungServiceFac().getMindermengenzuschlag(bdWertInMandantenwaehrung, landIId,
						theClientDto);
				if (mmzDto != null) {

					ArtikelDto aDtoMMz = getArtikelFac().artikelFindByPrimaryKey(mmzDto.getArtikelIId(), theClientDto);

					if (Helper.short2boolean(aDtoMMz.getBLagerbewirtschaftet())) {

						ArrayList al = new ArrayList();
						al.add(aDtoMMz);
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN,
								al, new Exception(
										"FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN: " + aDtoMMz.getCNr()));
					}

					ParametermandantDto parameterPosKont = getParameterFac().getMandantparameter(
							theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
					boolean isPositionskontierung = ((Boolean) parameterPosKont.getCWertAsObject()).booleanValue();

					Integer mwstsatzBezId = aDtoMMz.getMwstsatzbezIId();
					if (!isPositionskontierung) {

						mwstsatzBezId = kundeDto.getMwstsatzbezIId();
					}
					MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(mwstsatzBezId,
							angebot.getTBelegdatum(), theClientDto);
					/*
					 * if (!isPositionskontierung) { // Aktuellen MWST-Satz uebersetzen. KundeDto
					 * kundeDto =
					 * getKundeFac().kundeFindByPrimaryKey(angebot.getKundeIIdRechnungsadresse(),
					 * theClientDto);
					 * 
					 * mwstsatzDtoAktuell = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId(),
					 * theClientDto);
					 * 
					 * } else { mwstsatzDtoAktuell = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(aDtoMMz.getMwstsatzbezIId(),
					 * theClientDto); }
					 */
					AngebotpositionDto posMmzDto = new AngebotpositionDto();
					posMmzDto.setArtikelIId(aDtoMMz.getIId());
					posMmzDto.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
					posMmzDto.setBelegIId(angebot.getIId());
					posMmzDto.setNMenge(new BigDecimal(1));
					posMmzDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					posMmzDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
					posMmzDto.setBAlternative(Helper.boolean2Short(false));

					BigDecimal bdNettoeinzel = mmzDto.getNZuschlag()
							.multiply(new BigDecimal(angebot.getFWechselkursmandantwaehrungzuangebotswaehrung()));// noch
																													// in
					// Belegwaehrung
					// umrechnen
					posMmzDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(bdNettoeinzel);
					posMmzDto.setNNettoeinzelpreis(bdNettoeinzel);
					posMmzDto.setNEinzelpreis(bdNettoeinzel);
					posMmzDto.setFRabattsatz(0D);
					posMmzDto.setNRabattbetrag(BigDecimal.ZERO);
					posMmzDto.setFZusatzrabattsatz(0D);
					posMmzDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

					BigDecimal mwstBetrag = posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
							.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));

					posMmzDto.setNMwstbetrag(mwstBetrag);

					posMmzDto.setNBruttoeinzelpreis(
							posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().add(mwstBetrag));

					getAngebotpositionFac().createAngebotposition(posMmzDto, theClientDto);

				}

			}

			mindestbestellwertHinzufuegen(angebot.getIId(), theClientDto);

			// PJ21885

			AngebotpositionDto[] alle = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(angebot.getIId(), theClientDto);

			Integer artikelIIdVerpackungskosten = getVerpackunskostenArtikel(theClientDto);
			if (artikelIIdVerpackungskosten != null) {
				for (int i = 0; i < alle.length; i++) {
					if (alle[i].getArtikelIId() != null
							&& alle[i].getArtikelIId().equals(artikelIIdVerpackungskosten)) {
						getAngebotpositionFac().removeAngebotposition(alle[i], theClientDto);

					}
				}

				alle = getAngebotpositionFac().angebotpositionFindByAngebotIIdOhneAlternative(angebot.getIId(),
						theClientDto);
			}

			AngebotpositionDto agPosDto_Verpackungskosten = new AngebotpositionDto();
			agPosDto_Verpackungskosten.setBAlternative(Helper.boolean2Short(false));
			agPosDto_Verpackungskosten = (AngebotpositionDto) getBelegVerkaufFac()
					.erstelleVerpackungskostenpositionAnhandNettowert(angebot.getKundeIIdRechnungsadresse(), alle,
							assembleAngebotDto(angebot), agPosDto_Verpackungskosten, theClientDto);

			if (agPosDto_Verpackungskosten != null) {
				getAngebotpositionFac().createAngebotposition(agPosDto_Verpackungskosten, false, theClientDto);
			}

			BigDecimal nNettowertGesamt = berechneNettowertGesamt(angebot.getIId(), theClientDto);
			if (angebot.getNKorrekturbetrag() != null) {
				nNettowertGesamt = nNettowertGesamt.add(angebot.getNKorrekturbetrag());
			}

			angebot.setNGesamtangebotswertinangebotswaehrung(nNettowertGesamt);
			em.merge(angebot);
			em.flush();

		}
		return getTimestamp();
	}

	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNr(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AngebotDto[] aAngebotDtos = null;
		Query query = em.createNamedQuery("AngebotfindByKundeIIdAngebotsadresseMandantCNr");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAngebotDtos = assembleAngebotDtos(cl);
		return aAngebotDtos;
	}

	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) {
		AngebotDto[] aAngebotDtos = null;
		try {
			Query query = em.createNamedQuery("AngebotfindByKundeIIdAngebotsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAngebotDtos = assembleAngebotDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}
		return aAngebotDtos;
	}

	public AngebotDto[] angebotfindByKundeIIdLieferadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) {
		AngebotDto[] aDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em.createNamedQuery("AngebotfindByKundeIIdLieferadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aDtos = assembleAngebotDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}

		return aDtos;
	}

	public AngebotDto[] angebotfindByKundeIIdRechnungsadresseMandantCNrOhneExc(Integer iIdKundeI, String cNrMandantI,
			TheClientDto theClientDto) {
		AngebotDto[] aDtos = null;
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdKundeI == null"));
		}
		try {
			Query query = em.createNamedQuery("AngebotfindByKundeIIdRechnungsadresseMandantCNr");
			query.setParameter(1, iIdKundeI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aDtos = assembleAngebotDtos(cl);
		} catch (Throwable t) {
			myLogger.warn("iIdKundeI=" + iIdKundeI + " cNrMandantI" + cNrMandantI, t);
		}

		return aDtos;
	}

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNr(Integer iIdAnsprechpartnerI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AngebotDto[] aAngebotDtos = null;
		Query query = em.createNamedQuery("AngebotfindByAnsprechpartnerIIdKundeMandantCNr");
		query.setParameter(1, iIdAnsprechpartnerI);
		query.setParameter(2, cNrMandantI);
		Collection<?> cl = query.getResultList();
		aAngebotDtos = assembleAngebotDtos(cl);
		return aAngebotDtos;
	}

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNrOhneExc(Integer iIdAnsprechpartnerI,
			String cNrMandantI, TheClientDto theClientDto) {
		AngebotDto[] aAngebotDtos = null;
		try {
			Query query = em.createNamedQuery("AngebotfindByAnsprechpartnerIIdKundeMandantCNr");
			query.setParameter(1, iIdAnsprechpartnerI);
			query.setParameter(2, cNrMandantI);
			Collection<?> cl = query.getResultList();
			aAngebotDtos = assembleAngebotDtos(cl);
		} catch (Throwable th) {
			return null;
		}
		return aAngebotDtos;
	}

	public AngebotDto angebotFindByPrimaryKey(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		AngebotDto angebotDto = angebotFindByPrimaryKeyOhneExec(iIdAngebotI);
		if (angebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Fehler bei angebotfindbyPrimarykey. Es gibt kein Angebot mit iid " + iIdAngebotI));
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

	public AngebotDto angebotFindByCNrMandantCNrOhneEx(String cNr, String mandantCNr) {
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
		angebot.setTAngebotsgueltigkeitbis(angebotDto.getTAngebotsgueltigkeitbis());
		angebot.setKundeIIdAngebotsadresse(angebotDto.getKundeIIdAngebotsadresse());
		angebot.setAnsprechpartnerIIdKunde(angebotDto.getAnsprechpartnerIIdKunde());
		angebot.setPersonalIIdVertreter(angebotDto.getPersonalIIdVertreter());
		angebot.setCBez(angebotDto.getCBez());
		angebot.setWaehrungCNrAngebotswaehrung(angebotDto.getWaehrungCNr());
		angebot.setFWechselkursmandantwaehrungzuangebotswaehrung(
				angebotDto.getFWechselkursmandantwaehrungzubelegwaehrung());
		angebot.setILieferzeitinstunden(angebotDto.getILieferzeitinstunden());
		angebot.setAngeboteinheitCNr(angebotDto.getAngeboteinheitCNr());
		angebot.setKostenstelleIId(angebotDto.getKostenstelleIId());
		angebot.setAngeboterledigungsgrundCNr(angebotDto.getAngeboterledigungsgrundCNr());
		angebot.setTNachfasstermin(angebotDto.getTNachfasstermin());
		angebot.setTRealisierungstermin(angebotDto.getTRealisierungstermin());
		angebot.setFAuftragswahrscheinlichkeit(angebotDto.getFAuftragswahrscheinlichkeit());
		angebot.setXAblageort(angebotDto.getXAblageort());

		angebot.setBMindermengenzuschlag(angebotDto.getBMindermengenzuschlag());

		angebot.setKundeIIdLieferadresse(angebotDto.getKundeIIdLieferadresse());
		angebot.setKundeIIdRechnungsadresse(angebotDto.getKundeIIdRechnungsadresse());
		angebot.setAnsprechpartnerIIdLieferadresse(angebotDto.getAnsprechpartnerIIdLieferadresse());
		angebot.setAnsprechpartnerIIdRechnungsadresse(angebotDto.getAnsprechpartnerIIdRechnungsadresse());

		if (angebotDto.getFVersteckterAufschlag() != null) {
			angebot.setFVersteckteraufschlag(angebotDto.getFVersteckterAufschlag());
		} else {
			angebot.setFVersteckteraufschlag(0D);
		}
		if (angebotDto.getFAllgemeinerRabattsatz() != null) {
			angebot.setFAllgemeinerrabattsatz(angebotDto.getFAllgemeinerRabattsatz());
		} else {
			angebot.setFAllgemeinerrabattsatz(0D);
		}
		if (angebotDto.getFProjektierungsrabattsatz() != null) {
			angebot.setFProjektierungsrabattsatz(angebotDto.getFProjektierungsrabattsatz());
		} else {
			angebot.setFProjektierungsrabattsatz(0D);
		}
		angebot.setLieferartIId(angebotDto.getLieferartIId());
		angebot.setZahlungszielIId(angebotDto.getZahlungszielIId());
		angebot.setSpediteurIId(angebotDto.getSpediteurIId());
		angebot.setIGarantie(angebotDto.getIGarantie());
		angebot.setNGesamtangebotswertinangebotswaehrung(angebotDto.getNGesamtwertinbelegwaehrung());
		angebot.setAngebottextIIdKopftext(angebotDto.getBelegtextIIdKopftext());
		angebot.setXKopftextuebersteuert(angebotDto.getXKopftextuebersteuert());
		angebot.setAngebottextIIdFusstext(angebotDto.getBelegtextIIdFusstext());
		angebot.setXFusstextuebersteuert(angebotDto.getXFusstextuebersteuert());
		angebot.setXExternerkommentar(angebotDto.getXExternerkommentar());
		angebot.setXInternerkommentar(angebotDto.getXInternerkommentar());
		angebot.setTGedruckt(angebotDto.getTGedruckt());
		angebot.setPersonalIIdStorniert(angebotDto.getPersonalIIdStorniert());
		angebot.setTStorniert(angebotDto.getTStorniert());
		angebot.setPersonalIIdManuellerledigt(angebotDto.getPersonalIIdManuellerledigt());
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
		angebot.setTAenderungsangebot(angebotDto.getTAenderungsangebot());
		angebot.setIVersion(angebotDto.getIVersion());

		angebot.setPersonalIIdVertreter2(angebotDto.getPersonalIIdVertreter2());

		angebot.setAkquisestatusIId(angebotDto.getAkquisestatusIId());
		angebot.setCKommission(angebotDto.getCKommission());

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("angebotDtoI == null"));
		}

		myLogger.info("AngebotDto: " + angebotDtoI.toString());
	}

	private void checkAngebotIId(Integer iIdAngebotI) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAngebotI == null"));
		}

		myLogger.info("AngebotIId: " + iIdAngebotI.toString());
	}

	public BigDecimal berechneGestehungswertSoll(Integer iIdAngebotI, String sArtikelartI,
			boolean bMitEigengefertigtenStuecklisten, TheClientDto theClientDto) {
		final String METHOD_NAME = "berechneGestehungswertSoll";
		myLogger.entry();
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAngebotI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdGestehungswertSollO = Helper.getBigDecimalNull();

		try {

			AngebotDto angebotDto = angebotFindByPrimaryKey(iIdAngebotI, theClientDto);
			AngebotpositionDto[] aAngebotpositionDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(iIdAngebotI, theClientDto);

			for (int i = 0; i < aAngebotpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAngebotpositionDtos[i].getNMenge() != null && aAngebotpositionDtos[i].getArtikelIId() != null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aAngebotpositionDtos[i].getArtikelIId(), theClientDto);

					if (bMitEigengefertigtenStuecklisten == false) {
						StuecklisteDto stklDto = getStuecklisteFac()
								.stuecklisteFindByMandantCNrArtikelIIdOhneExc(oArtikelDto.getIId(), theClientDto);
						if (stklDto != null && Helper.short2boolean(stklDto.getBFremdfertigung()) == false) {
							continue;
						}
					}

					// das Hauptlager des Mandanten
					LagerDto oHauptlagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

					// Grundlage ist der Gestehungspreis des Artikels am
					// Hauptlager des Mandanten
					BigDecimal bdGestehungspreisSoll = new BigDecimal(0);
					if (aAngebotpositionDtos[i].getPositioniIdArtikelset() == null) {

						Query query = em.createNamedQuery("AngebotpositionpositionfindByPositionIIdArtikelset");
						query.setParameter(1, aAngebotpositionDtos[i].getIId());
						Collection<?> angebotpositionDtos = query.getResultList();
						AngebotpositionDto[] zugehoerigeABPosDtos = AngebotpositionDtoAssembler
								.createDtos(angebotpositionDtos);

						if (zugehoerigeABPosDtos.length == 0) {
							bdGestehungspreisSoll = getLagerFac().getGestehungspreisZumZeitpunkt(oArtikelDto.getIId(),
									oHauptlagerDto.getIId(), angebotDto.getTBelegdatum(), theClientDto);
						} else {

							for (int k = 0; k < zugehoerigeABPosDtos.length; k++) {
								bdGestehungspreisSoll = bdGestehungspreisSoll.add(getLagerFac()
										.getGestehungspreisZumZeitpunkt(zugehoerigeABPosDtos[k].getArtikelIId(),
												oHauptlagerDto.getIId(), angebotDto.getTBelegdatum(), theClientDto));
							}
						}

					}

					// je nach Artikelart beruecksichtigen

					if (bdGestehungspreisSoll != null) {

						BigDecimal bdBeitragDieserPosition = aAngebotpositionDtos[i].getNMenge()
								.multiply(bdGestehungspreisSoll);

						if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO.add(bdBeitragDieserPosition);
							}
						} else {
							if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bdGestehungswertSollO = bdGestehungswertSollO.add(bdBeitragDieserPosition);
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Gestehungswert " + sArtikelartI + " Soll : " + bdGestehungswertSollO.toString());

		return bdGestehungswertSollO;
	}

	public BigDecimal berechneVerkaufswertSoll(Integer iIAngebotI, String sArtikelartI, TheClientDto theClientDto) {
		if (iIAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIAngebotI == null"));
		}

		if (sArtikelartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("sArtikelartI == null"));
		}

		BigDecimal bdVerkaufswertSollO = Helper.getBigDecimalNull();

		try {
			AngebotpositionDto[] aAngebotpositionDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(iIAngebotI, theClientDto);

			for (int i = 0; i < aAngebotpositionDtos.length; i++) {

				// alle mengenbehafteten Positionen beruecksichtigen
				if (aAngebotpositionDtos[i].getNMenge() != null && aAngebotpositionDtos[i].getArtikelIId() != null
						&& aAngebotpositionDtos[i].getPositioniIdArtikelset() == null) {
					ArtikelDto oArtikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aAngebotpositionDtos[i].getArtikelIId(), theClientDto);

					// je nach Artikelart beruecksichtigen
					BigDecimal bdBeitragDieserPosition = aAngebotpositionDtos[i].getNMenge().multiply(
							aAngebotpositionDtos[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					if (sArtikelartI.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						if (oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO.add(bdBeitragDieserPosition);
						}
					} else {
						if (!oArtikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
							bdVerkaufswertSollO = bdVerkaufswertSollO.add(bdBeitragDieserPosition);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			// exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}

		myLogger.exit("Verkaufswert " + sArtikelartI + " Soll : " + bdVerkaufswertSollO.toString());

		return bdVerkaufswertSollO;
	}

	public void korrekturbetragZuruecknehmen(Integer iIdAngebotI) {
		Angebot angebot = em.find(Angebot.class, iIdAngebotI);
		angebot.setNKorrekturbetrag(null);
		em.merge(angebot);
		em.flush();
	}

	public void uebersteuereIntelligenteZwischensumme(Integer angebotpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert, TheClientDto theClientDto) {

		try {

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_RUNDUNGSAUSGLEICH_ARTIKEL);
			String artikelCnr = parameterDto.getCWert();
			ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelCnr, theClientDto);
			if (null == artikelDto) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
						"Rundungsartikel nicht definiert");
			}

			AngebotpositionDto apDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey(angebotpositionIId,
					theClientDto);

			AngebotDto agDto = angebotFindByPrimaryKey(apDto.getBelegIId(), theClientDto);

			if (apDto.getZwsNettoSumme() == null) {
				berechneBeleg(agDto.getIId(), theClientDto);
				apDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey(angebotpositionIId, theClientDto);
			}

			AngebotpositionDto[] apDtos = getAngebotpositionFac().angebotpositionFindByAngebotIId(agDto.getIId());

			int iNachkommastellenPreis = getUINachkommastellenPreisVK(theClientDto.getMandant());

			if (apDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& apDto.getZwsNettoSumme() != null && apDto.getZwsNettoSumme().doubleValue() != 0) {

				BigDecimal bdSumme = apDto.getZwsNettoSumme();

				BigDecimal faktor = bdBetragInBelegwaehrungUebersteuert.divide(bdSumme, 10, BigDecimal.ROUND_HALF_EVEN);

				ArrayList<BelegpositionVerkaufDto> alPos = getBelegVerkaufFac().getIntZwsPositions(apDto, apDtos);

				Integer mwstsatzIIdErstePosition = alPos.get(0).getMwstsatzIId();

				MwstsatzDto mwstsatzDtoErstePosition = getMandantFac()
						.mwstsatzFindByPrimaryKey(mwstsatzIIdErstePosition, theClientDto);

				BigDecimal bdNettosummeNeu = BigDecimal.ZERO;

				for (int i = 0; i < alPos.size(); i++) {

					AngebotpositionDto bvDto = (AngebotpositionDto) alPos.get(i);

					if (bvDto.getMwstsatzIId() != null && !artikelDto.getIId().equals(bvDto.getArtikelIId())) {

						MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
								.mwstsatzFindByPrimaryKey(bvDto.getMwstsatzIId(), theClientDto);

						bvDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						bvDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));

						BigDecimal bdPreisNeu = Helper.rundeKaufmaennisch(bvDto.getNNettoeinzelpreis().multiply(faktor),
								iNachkommastellenPreis);

						bvDto.setNNettoeinzelpreis(bdPreisNeu);

						bvDto.setFZusatzrabattsatz(0D);

						BigDecimal bdRabattsumme = bvDto.getNEinzelpreis().subtract(bdPreisNeu);

						Double rabbattsatz = new Double(
								Helper.getProzentsatzBD(bvDto.getNEinzelpreis(), bdRabattsumme, 4).doubleValue());
						bvDto.setFRabattsatz(rabbattsatz);
						bvDto.setNRabattbetrag(bdRabattsumme);

						BigDecimal mwstBetrag = bdPreisNeu
								.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));
						bvDto.setNMwstbetrag(mwstBetrag);
						bvDto.setNBruttoeinzelpreis(bdPreisNeu.add(mwstBetrag));

						getAngebotpositionFac().updateAngebotposition(bvDto, theClientDto);

						bdNettosummeNeu = bdNettosummeNeu.add(bdPreisNeu.multiply(bvDto.getNMenge()));
					}

				}

				// Wenn rundungsdifferenz auftritt, Rundungsartikel einfuegen

				BigDecimal diff = bdBetragInBelegwaehrungUebersteuert.subtract(bdNettosummeNeu);

				AngebotpositionDto angebotpositionDtoRundungsartikel = null;
				for (int i = 0; i < alPos.size(); i++) {
					AngebotpositionDto bvDto = (AngebotpositionDto) alPos.get(i);
					if (artikelDto.getIId().equals(bvDto.getArtikelIId())) {
						angebotpositionDtoRundungsartikel = bvDto;
					}
				}

				if (diff.doubleValue() != 0) {

					if (angebotpositionDtoRundungsartikel == null) {
						angebotpositionDtoRundungsartikel = new AngebotpositionDto();
						angebotpositionDtoRundungsartikel.setBAlternative(Helper.boolean2Short(false));
						angebotpositionDtoRundungsartikel.setBelegIId(agDto.getIId());
						angebotpositionDtoRundungsartikel
								.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);
						angebotpositionDtoRundungsartikel.setArtikelIId(artikelDto.getIId());
						angebotpositionDtoRundungsartikel.setNMenge(BigDecimal.ONE);
						angebotpositionDtoRundungsartikel.setEinheitCNr(artikelDto.getEinheitCNr());

						angebotpositionDtoRundungsartikel.setArtikelIId(artikelDto.getIId());
						angebotpositionDtoRundungsartikel.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						angebotpositionDtoRundungsartikel.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
						angebotpositionDtoRundungsartikel.setFRabattsatz(0D);
						angebotpositionDtoRundungsartikel.setFZusatzrabattsatz(0D);
						angebotpositionDtoRundungsartikel.setNRabattbetrag(BigDecimal.ZERO);
					}

					angebotpositionDtoRundungsartikel.setNEinzelpreis(diff);

					angebotpositionDtoRundungsartikel.setNNettoeinzelpreis(diff);

					MwstsatzDto mwstsatzDto = getMandantFac()
							.mwstsatzFindZuDatum(mwstsatzDtoErstePosition.getIIMwstsatzbezId(), agDto.getTBelegdatum());
					angebotpositionDtoRundungsartikel.setMwstsatzIId(mwstsatzDto.getIId());
					BigDecimal mwstBetrag = diff.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()).movePointLeft(2));
					angebotpositionDtoRundungsartikel.setNMwstbetrag(mwstBetrag);
					angebotpositionDtoRundungsartikel.setNBruttoeinzelpreis(diff.add(mwstBetrag));

					if (angebotpositionDtoRundungsartikel.getIId() == null) {

						Integer iSort = apDto.getISort();

						getAngebotpositionFac().sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(agDto.getIId(),
								iSort, theClientDto);

						angebotpositionDtoRundungsartikel.setISort(iSort);

						apDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey(angebotpositionIId,
								theClientDto);

						Integer agposIIdNeu = getAngebotpositionFac()
								.createAngebotposition(angebotpositionDtoRundungsartikel, theClientDto);
						apDto.setZwsBisPosition(agposIIdNeu);
					} else {
						getAngebotpositionFac().updateAngebotposition(angebotpositionDtoRundungsartikel, theClientDto);
					}

				} else {
					if (angebotpositionDtoRundungsartikel != null) {

						Integer angebotspositionIId_LetzterNomalerArtikel = null;

						for (int i = alPos.size(); i > 0; i--) {
							AngebotpositionDto bvDto = (AngebotpositionDto) alPos.get(i - 1);
							if (!artikelDto.getIId().equals(bvDto.getArtikelIId())) {
								angebotspositionIId_LetzterNomalerArtikel = bvDto.getIId();
								break;
							}
						}

						if (angebotspositionIId_LetzterNomalerArtikel != null) {
							getAngebotpositionFac().removeAngebotposition(angebotpositionDtoRundungsartikel,
									theClientDto);
							apDto.setZwsBisPosition(angebotspositionIId_LetzterNomalerArtikel);
						} else {
							angebotpositionDtoRundungsartikel.setNEinzelpreis(BigDecimal.ZERO);
							angebotpositionDtoRundungsartikel.setNNettoeinzelpreis(BigDecimal.ZERO);
							angebotpositionDtoRundungsartikel.setNMwstbetrag(BigDecimal.ZERO);
							angebotpositionDtoRundungsartikel.setNBruttoeinzelpreis(BigDecimal.ZERO);

							getAngebotpositionFac().updateAngebotposition(angebotpositionDtoRundungsartikel,
									theClientDto);
						}

					}
				}

				apDto.setZwsNettoSumme(null);
				getAngebotpositionFac().updateAngebotposition(apDto, theClientDto);
			} else if (apDto.isIntelligenteZwischensumme() && bdBetragInBelegwaehrungUebersteuert != null
					&& apDto.getZwsNettoSumme() != null && apDto.getZwsNettoSumme().doubleValue() == 0) {
				throwExceptionZwischensummeNull();
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	private void mindestbestellwertHinzufuegen(Integer angebotIId, TheClientDto theClientDto) {

		try {
			AngebotDto aDto = getAngebotFac().angebotFindByPrimaryKey(angebotIId, theClientDto);
			int iNachkommastellenPreis = getUINachkommastellenPreisVK(aDto.getMandantCNr());

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(aDto.getKundeIIdAngebotsadresse(), theClientDto);

			BigDecimal bdMindestbestellwertInAngebotwaehrung = null;

			if (kdDto.getNMindestbestellwert() != null) {
				bdMindestbestellwertInAngebotwaehrung = getLocaleFac().rechneUmInAndereWaehrungGerundetZuDatum(
						kdDto.getNMindestbestellwert(), kdDto.getCWaehrung(), aDto.getWaehrungCNr(),
						new java.sql.Date(aDto.getTBelegdatum().getTime()), theClientDto);
			}

			if (bdMindestbestellwertInAngebotwaehrung != null
					&& bdMindestbestellwertInAngebotwaehrung.doubleValue() > 0) {

				ArtikelDto aDtoMindestbestellwert = getParameterFac().getMindestbestellwertArtikel(theClientDto);

				if (aDtoMindestbestellwert != null) {

					// Zuerst alle Mindesbestellwertsartikel entfernen
					AngebotpositionDto[] posDtos = getAngebotpositionFac().angebotpositionFindByAngebotIId(angebotIId);
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].getArtikelIId() != null
								&& posDtos[i].getArtikelIId().equals(aDtoMindestbestellwert.getIId())) {
							getAngebotpositionFac().removeAngebotposition(posDtos[i], theClientDto);
						}
					}

					posDtos = getAngebotpositionFac().angebotpositionFindByAngebotIId(angebotIId);

					BigDecimal bdGesamtPreisVorRabatten = BigDecimal.ZERO;
					for (int i = 0; i < posDtos.length; i++) {
						if (posDtos[i].getArtikelIId() != null) {
							BelegpositionVerkaufDto bvDto = getBelegVerkaufFac()
									.getBelegpositionVerkaufReport(posDtos[i], aDto, iNachkommastellenPreis);

							if (bvDto != null && bvDto.getNReportGesamtpreis() != null) {

								bdGesamtPreisVorRabatten = bdGesamtPreisVorRabatten.add(bvDto.getNReportGesamtpreis());
							}
						}

					}

					// Mindestbestellwert beim Kunden ist immer in
					// Kundenwaehrung

					// Parameter ist in Mandantenwaehrung

					// TODO: Wieso wird das neu geladen, es gibt doch aDto? (ghp, 12.06.2020)
					Angebot oAngebot = em.find(Angebot.class, angebotIId);

					// Wenn der Nettowert < Mindestbestellwert
					if (bdGesamtPreisVorRabatten.compareTo(bdMindestbestellwertInAngebotwaehrung) == -1) {

						BigDecimal diff = bdMindestbestellwertInAngebotwaehrung.subtract(bdGesamtPreisVorRabatten);

						BigDecimal bdDiffOhneVerstecktemAufschlag = diff;

						if (oAngebot.getFVersteckteraufschlag() != null
								&& oAngebot.getFVersteckteraufschlag().doubleValue() != 0) {
							bdDiffOhneVerstecktemAufschlag = diff.subtract(Helper.getProzentWert(diff,
									new BigDecimal(oAngebot.getFVersteckteraufschlag()), 4));
						}
						// + Allgemeiner Rabatt

						// Nun neue Position hinzufuegen

						// Dann Angebotsposition anlegen
						AngebotpositionDto angebotpositionDto = new AngebotpositionDto();
						angebotpositionDto.setBelegIId(angebotIId);
						angebotpositionDto.setArtikelIId(aDtoMindestbestellwert.getIId());
						angebotpositionDto.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);

						angebotpositionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						angebotpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						angebotpositionDto.setNMenge(new BigDecimal(1));

						angebotpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
						angebotpositionDto.setFRabattsatz(new Double(0));

						angebotpositionDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));

						/*
						 * MwstsatzDto mwstsatzDtoAktuell = getMandantFac()
						 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kdDto.getMwstsatzbezIId(),
						 * theClientDto);
						 */
						MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(
								kdDto.getMwstsatzbezIId(), oAngebot.getTBelegdatum(), theClientDto);

						angebotpositionDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
						angebotpositionDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
						angebotpositionDto.setNEinzelpreis(diff);
						angebotpositionDto.setNRabattbetrag(new BigDecimal(0));
						angebotpositionDto.setNNettoeinzelpreis(diff);
						angebotpositionDto.setNBruttoeinzelpreis(diff);
						angebotpositionDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(diff);
						angebotpositionDto.setNMwstbetrag(new BigDecimal(0));

						angebotpositionDto.setNEinzelpreisplusversteckteraufschlag(bdDiffOhneVerstecktemAufschlag);
						angebotpositionDto.setNNettoeinzelpreisplusversteckteraufschlag(bdDiffOhneVerstecktemAufschlag);

						angebotpositionDto.setFZusatzrabattsatz(new Double(0));
						angebotpositionDto.setBAlternative(Helper.boolean2Short(false));

						Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(angebotpositionDto,
								theClientDto);

						Angebotposition position = em.find(Angebotposition.class, angebotpositionIId);

						position.setNRabattbetrag(new BigDecimal(0));

						position.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(diff);

						position.setNNettogesamtpreis(bdDiffOhneVerstecktemAufschlag);
						position.setNNettoeinzelpreis(bdDiffOhneVerstecktemAufschlag);
						position.setNNettogesamtpreisplusversteckteraufschlag(diff);
						position.setNNettoeinzelpreisplusversteckteraufschlag(diff);

						// MWST
						MwstsatzDto mwstsatzDto = getBelegpositionkonvertierungFac()
								.ermittleMwstSatz(angebotpositionDto, aDto.getTBelegdatum(), theClientDto);
						if (mwstsatzDto != null && mwstsatzDto.getFMwstsatz() != 0.0) {
							BigDecimal bdMwstbetrag = Helper.getProzentWert(position.getNNettoeinzelpreis(),
									new BigDecimal(mwstsatzDto.getFMwstsatz()), iNachkommastellenPreis);
							position.setNMwstbetrag(bdMwstbetrag);
							position.setNMwstbetrag(new BigDecimal(0));
							position.setNBruttogesamtpreis(position.getNNettoeinzelpreis().add(bdMwstbetrag));

						} else {
							position.setNMwstbetrag(new BigDecimal(0));
							position.setNBruttogesamtpreis(bdDiffOhneVerstecktemAufschlag);
						}

						em.merge(position);
						em.flush();

					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	/**
	 * Berechne den Gesamtwert eines bestimmten Angebots in der Angebotswaehrung.
	 * <br>
	 * Beruecksichtigt werden alle positiv mengenbehafteten Positionen.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return BigDecimal der Gesamtwert des Angebots
	 */

	public BigDecimal berechneNettowertGesamt(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.warn("berechneNettowertGesamt Angebot '" + iIdAngebotI + "'.");
		checkAngebotIId(iIdAngebotI);
		BigDecimal nNettowertGesamt = BigDecimal.ZERO;
		try {
			AngebotDto angebotDto = angebotFindByPrimaryKey(iIdAngebotI, theClientDto);
			if (angebotDto.getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				AngebotpositionDto[] alle = getAngebotpositionFac()
						// .angebotpositionFindByAngebotIIdOhneAlternative(
						.angebotpositionFindByAngebotIId(iIdAngebotI, theClientDto);

				Set<Integer> modifiedPositions = getBelegVerkaufFac().prepareIntZwsPositions(alle, angebotDto);

				nNettowertGesamt = getBelegVerkaufFac().getGesamtwertinBelegwaehrung(alle, angebotDto);

				getBelegVerkaufFac().adaptIntZwsPositions(alle);

				getBelegVerkaufFac().saveIntZwsPositions(alle, modifiedPositions, Angebotposition.class);

				for (AngebotpositionDto angebotPositionDto : alle) {
					if (AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME
							.equals(angebotPositionDto.getPositionsartCNr())) {
						getAngebotpositionFac().updateAngebotposition(angebotPositionDto, theClientDto);
					}
				}
			} else
			// Step 2: Wenn der status STORNIERT ist, 0 zurueckgeben obwohl der
			// NettowertGesamt noch in der Tabelle steht
			if (angebotDto.getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_STORNIERT)) {
				nNettowertGesamt = new BigDecimal(0);
			} else {
				// Step 3: den NettowertGesamt aus der Tabelle lesen
				if (angebotDto.getNGesamtwertinbelegwaehrung() != null) {
					nNettowertGesamt = angebotDto.getNGesamtwertinbelegwaehrung();
				}
			}
			// Step 4: Der Wert muss in unsere Datenbank passen, fuer decimal(9)
			// gilt 15,4
		} catch (RemoteException e) {
		}
		return nNettowertGesamt;
	}

	/**
	 * Wenn die Zu- und Abschlaege in den Positionen geaendert wurden, dann werden
	 * im Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAngebotI  PK des Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAngebotKonditionen(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			FLRAngebotpositionReport[] aFLRAngebotposition = holeAngebotpositionen(session, iIdAngebotI, true, // nur
																												// mengenbehaftete
					false, // auch negative und 0 Mengen
					false); // alternative Positionen werden beruecksichtigt

			for (int i = 0; i < aFLRAngebotposition.length; i++) {
				getAngebotpositionFac().befuelleZusaetzlichePreisfelder(aFLRAngebotposition[i].getI_id(), theClientDto);
			}

			Angebot angebot = em.find(Angebot.class, iIdAngebotI);
			if (angebot == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
						"Fehler bei updateAngebotKonidition. Es gibt keine Angebot mit iid " + iIdAngebotI));
			}
			angebot.setPersonalIIdAendern(theClientDto.getIDPersonal());
			angebot.setTAendern(getTimestamp());
			if (!angebot.getAngebotstatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				angebot.setNGesamtangebotswertinangebotswaehrung(berechneNettowertGesamt(iIdAngebotI, theClientDto));
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
	 * @param iIdAngebotI  PK des bestehenden Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer PK des neuen Angebots
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer erzeugeAngebotAusAngebot(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotBasisDto = null;
		try {
			angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(iIdAngebotI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdAngebotKopie = null;

		try {
			AngebotDto angebotDto = (AngebotDto) angebotBasisDto.clone();

			angebotDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac()
					.getWechselkurs2(theClientDto.getSMandantenwaehrung(), angebotDto.getWaehrungCNr(), theClientDto)
					.doubleValue()));

			// IMS 2379
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

			int iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

			// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
			Timestamp tAngebotGueltigkeitsdauer = new Timestamp(gc.getTimeInMillis());

			Timestamp timestamp = getTimestamp();
			angebotDto.setTBelegdatum(timestamp);
			angebotDto.setTAnfragedatum(timestamp);
			angebotDto.setTAngebotsgueltigkeitbis(tAngebotGueltigkeitsdauer);

			parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN);
			int defaultNachfasstermin = (Integer) parametermandantDto.getCWertAsObject();

			angebotDto.setTNachfasstermin(
					Helper.cutTimestamp(Helper.addiereTageZuTimestamp(timestamp, defaultNachfasstermin)));

			// SP6638 Ist Vertreter noch im Unternehmen
			angebotDto.setPersonalIIdVertreter(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(angebotDto.getPersonalIIdVertreter(),
							angebotDto.getKundeIIdAngebotsadresse(), angebotDto.getTBelegdatum(), theClientDto));

			iIdAngebotKopie = createAngebot(angebotDto, theClientDto);

			// UW 22.03.06 alle Positionen kopieren, das bleibt auf Bean Basis
			AngebotpositionDto[] aAngebotpositionBasis = getAngebotpositionFac()
					.angebotpositionFindByAngebotIId(iIdAngebotI, theClientDto);
			Integer positionIIdSet = null;
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			MwstsatzEvaluateCache mwstsatzCache = new MwstsatzEvaluateCache(angebotDto, theClientDto);
			for (int i = 0; i < aAngebotpositionBasis.length; i++) {
				AngebotpositionDto angebotpositionDto = (AngebotpositionDto) aAngebotpositionBasis[i].clone();

				// Wenn sich sie MWST seither geaendert hat
				if (angebotpositionDto.getMwstsatzIId() != null) {
					/*
					 * MwstsatzDto mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByPrimaryKey(angebotpositionDto.getMwstsatzIId(), theClientDto);
					 * mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzDto.getIIMwstsatzbezId(),
					 * theClientDto);
					 */
					MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(angebotpositionDto.getMwstsatzIId());
					BigDecimal mwstBetrag = Helper.getProzentWert(angebotpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					angebotpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					angebotpositionDto.setNMwstbetrag(mwstBetrag);
					angebotpositionDto.setNBruttoeinzelpreis(angebotpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAngebotpositionBasis[i].isIntelligenteZwischensumme()) {
					ZwsPositionMapper mapper = new ZwsPositionMapper(getAngebotpositionFac(), getAngebotpositionFac());
					mapper.map(aAngebotpositionBasis[i], angebotpositionDto, iIdAngebotKopie);
				}

				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() != null) {
					angebotpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}

				if (aAngebotpositionBasis[i].getPositionIIdZugehoerig() != null) {
					angebotpositionDto
							.setPositionIIdZugehoerig(hm.get(aAngebotpositionBasis[i].getPositionIIdZugehoerig()));
				}
				angebotpositionDto.setBelegIId(iIdAngebotKopie);

				// SP9090 Gestehungspreis muss neu berechnet werden
				if (angebotpositionDto.getArtikelIId() != null) {
					angebotpositionDto.setNGestehungspreis(getLagerFac().getGestehungspreisZumZeitpunkt(
							angebotpositionDto.getArtikelIId(), null, timestamp, theClientDto));
				}

				Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(angebotpositionDto, false,
						theClientDto);

				getBelegartmediaFac().kopiereBelegartmedia(QueryParameters.UC_ID_ANGEBOTPOSITION,
						aAngebotpositionBasis[i].getIId(), QueryParameters.UC_ID_ANGEBOTPOSITION, angebotpositionIId,
						theClientDto);

				if (angebotpositionDto.getPositioniIdArtikelset() == null) {
					positionIIdSet = angebotpositionIId;
				}
				hm.put(aAngebotpositionBasis[i].getIId(), angebotpositionIId);

			}

			// kopieren der Auftrageigenschaften
			PaneldatenDto[] aPaneldatenDtoBasis = getPanelFac()
					.paneldatenFindByPanelCNrCKey(PanelFac.PANEL_ANGEBOTSEIGENSCHAFTEN, iIdAngebotI.toString());

			PaneldatenDto[] datenKorrigiert = new PaneldatenDto[aPaneldatenDtoBasis.length];

			for (int y = 0; y < aPaneldatenDtoBasis.length; y++) {
				PaneldatenDto paneldatenDto = (PaneldatenDto) aPaneldatenDtoBasis[y].clone();
				paneldatenDto.setCKey(iIdAngebotKopie.toString());
				datenKorrigiert[y] = paneldatenDto;
			}

			if (datenKorrigiert != null) {
				getPanelFac().createPaneldaten(datenKorrigiert, theClientDto);
			}

			myLogger.exit("Das Angebot wurde mit " + aAngebotpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAngebotKopie;
	}

	private void mindermengenzuschlagEntfernen(AngebotDto angebotDto, TheClientDto theClientDto) {
		if (Helper.short2boolean(angebotDto.getBMindermengenzuschlag())) {
			// Vorhandene MMZ-Artikel entfernen
			try {
				AngebotpositionDto[] rePosDtos = getAngebotpositionFac()
						.angebotpositionFindByAngebotIId(angebotDto.getIId(), theClientDto);
				Query query = em.createNamedQuery("MmzByMandantCNr");
				query.setParameter(1, theClientDto.getMandant());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Mmz mmz = (Mmz) it.next();

					for (int i = 0; i < rePosDtos.length; i++) {
						if (rePosDtos[i].getArtikelIId() != null
								&& rePosDtos[i].getArtikelIId().equals(mmz.getArtikelIId())) {
							getAngebotpositionFac().removeAngebotposition(rePosDtos[i], theClientDto);
						}
					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
	}

	@Override
	public Integer erzeugeAngebotAusAuftrag(Integer auftragId, HvOptional<Timestamp> belegDatum,
			TheClientDto theClientDto) {
		Validator.notNull(auftragId, "auftragId");
		Validator.dtoNotNull(belegDatum, "belegDatum");

		AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(auftragId);

		Integer iIdAngebotKopie = null;

		try {
			AngebotDto angebotDto = (AngebotDto) auftragBasisDto.cloneAsAngebotDto();
			if (belegDatum.isPresent()) {
				angebotDto.setTBelegdatum(belegDatum.get());
			}

			angebotDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac()
					.getWechselkurs2(theClientDto.getSMandantenwaehrung(), angebotDto.getWaehrungCNr(), theClientDto)
					.doubleValue()));

			// IMS 2379
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

			int iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

			// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(angebotDto.getTBelegdatum().getTime());
			gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
			Timestamp tAngebotGueltigkeitsdauer = new Timestamp(gc.getTimeInMillis());

			angebotDto.setTAnfragedatum(angebotDto.getTBelegdatum());
			angebotDto.setTAngebotsgueltigkeitbis(tAngebotGueltigkeitsdauer);

			parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN);

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			if (kdDto.getIGarantieinmonaten() != null) {
				angebotDto.setIGarantie(kdDto.getIGarantieinmonaten());
			}

			int defaultNachfasstermin = (Integer) parametermandantDto.getCWertAsObject();

			angebotDto.setTNachfasstermin(Helper
					.cutTimestamp(Helper.addiereTageZuTimestamp(angebotDto.getTBelegdatum(), defaultNachfasstermin)));

			// SP6638 Ist Vertreter noch im Unternehmen
			angebotDto.setPersonalIIdVertreter(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(angebotDto.getPersonalIIdVertreter(),
							angebotDto.getKundeIIdAngebotsadresse(), angebotDto.getTBelegdatum(), theClientDto));

			iIdAngebotKopie = createAngebot(angebotDto, theClientDto);

			// UW 22.03.06 alle Positionen kopieren, das bleibt auf Bean Basis
			AuftragpositionDto[] aAuftragpositionBasis = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(auftragId);
			Integer positionIIdSet = null;
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			MwstsatzEvaluateCache mwstsatzCache = new MwstsatzEvaluateCache(angebotDto, theClientDto);
			AngebotpositionDto[] angebotpositionDtos = getBelegpositionkonvertierungFac()
					.konvertiereNachAngebotpositionDto(aAuftragpositionBasis, angebotDto.getKundeIIdAngebotsadresse(),
							angebotDto.getTBelegdatum(), theClientDto);

			for (int i = 0; i < angebotpositionDtos.length; i++) {

				AngebotpositionDto angebotpositionDto = angebotpositionDtos[i];

				// Wenn sich sie MWST seither geaendert hat
				if (angebotpositionDto.getMwstsatzIId() != null) {
					/*
					 * MwstsatzDto mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByPrimaryKey(angebotpositionDto.getMwstsatzIId(), theClientDto);
					 */
					MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(angebotpositionDto.getMwstsatzIId());
					BigDecimal mwstBetrag = Helper.getProzentWert(angebotpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					angebotpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					angebotpositionDto.setNMwstbetrag(mwstBetrag);
					angebotpositionDto.setNBruttoeinzelpreis(angebotpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAuftragpositionBasis[i].isIntelligenteZwischensumme()) {
					ZwsPositionMapper mapper = new ZwsPositionMapper(getAngebotpositionFac(), getAngebotpositionFac());
					mapper.map(aAuftragpositionBasis[i], angebotpositionDto, iIdAngebotKopie);
				}

				if (aAuftragpositionBasis[i].getPositioniIdArtikelset() != null) {
					angebotpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}
				if (aAuftragpositionBasis[i].getPositionIIdZugehoerig() != null) {
					angebotpositionDto
							.setPositionIIdZugehoerig(hm.get(aAuftragpositionBasis[i].getPositionIIdZugehoerig()));
				}

				angebotpositionDto.setBelegIId(iIdAngebotKopie);

				Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(angebotpositionDto, false,
						theClientDto);

				hm.put(aAuftragpositionBasis[i].getIId(), angebotpositionIId);

				if (angebotpositionDto.getPositioniIdArtikelset() == null) {
					positionIIdSet = angebotpositionIId;
				}
			}

			myLogger.exit("Das Angebot wurde mit " + aAuftragpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iIdAngebotKopie;
	}

	public Integer erzeugeAngebotAusAuftrag(Integer auftragIId, TheClientDto theClientDto) {
		return erzeugeAngebotAusAuftrag(auftragIId, HvOptional.empty(), theClientDto);
	}

	public Integer erzeugeAngebotAusRechnung(Integer rechnungIId, final TheClientDto theClientDto) {
		RechnungDto rechnungBasisDto = null;
		try {
			rechnungBasisDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		Integer iIdAngebotKopie = null;

		try {
			final AngebotDto angebotDto = (AngebotDto) rechnungBasisDto.cloneAsAngebotDto();

			angebotDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac()
					.getWechselkurs2(theClientDto.getSMandantenwaehrung(), angebotDto.getWaehrungCNr(), theClientDto)
					.doubleValue()));

			// IMS 2379
			ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

			int iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

			// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
			Timestamp tAngebotGueltigkeitsdauer = new Timestamp(gc.getTimeInMillis());

			Timestamp timestamp = getTimestamp();
			angebotDto.setTBelegdatum(timestamp);
			angebotDto.setTAnfragedatum(timestamp);
			angebotDto.setTAngebotsgueltigkeitbis(tAngebotGueltigkeitsdauer);

			parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN);

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			if (kdDto.getIGarantieinmonaten() != null) {
				angebotDto.setIGarantie(kdDto.getIGarantieinmonaten());
			}

			int defaultNachfasstermin = (Integer) parametermandantDto.getCWertAsObject();

			angebotDto.setTNachfasstermin(
					Helper.cutTimestamp(Helper.addiereTageZuTimestamp(timestamp, defaultNachfasstermin)));

			// SP6638 Ist Vertreter noch im Unternehmen
			angebotDto.setPersonalIIdVertreter(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(angebotDto.getPersonalIIdVertreter(),
							angebotDto.getKundeIIdAngebotsadresse(), angebotDto.getTBelegdatum(), theClientDto));

			iIdAngebotKopie = createAngebot(angebotDto, theClientDto);

			// UW 22.03.06 alle Positionen kopieren, das bleibt auf Bean Basis
			RechnungPositionDto[] aRechnungpositionBasis = getRechnungFac()
					.rechnungPositionFindByRechnungIId(rechnungIId);
			Integer positionIIdSet = null;

			MwstsatzEvaluateCache mwstsatzCache = new MwstsatzEvaluateCache(angebotDto, theClientDto);
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();

			for (int i = 0; i < aRechnungpositionBasis.length; i++) {

				RechnungPositionDto rechnungPositionDto = aRechnungpositionBasis[i];

				if (rechnungPositionDto.getPositionsartCNr().equals(RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN)) {
					LieferscheinpositionDto[] lsposDtoS = getLieferscheinpositionFac()
							.lieferscheinpositionFindByLieferscheinIId(rechnungPositionDto.getLieferscheinIId());

					AngebotpositionDto[] posDtos = getBelegpositionkonvertierungFac().konvertiereNachAngebotpositionDto(
							lsposDtoS, angebotDto.getKundeIIdAngebotsadresse(), angebotDto.getTBelegdatum(),
							theClientDto);

					for (int j = 0; j < posDtos.length; j++) {
						AngebotpositionDto angebotpositionDto = posDtos[j];

						// Wenn sich sie MWST seither geaendert hat
						if (angebotpositionDto.getMwstsatzIId() != null) {
							/*
							 * MwstsatzDto mwstsatzDto = getMandantFac()
							 * .mwstsatzFindByPrimaryKey(angebotpositionDto.getMwstsatzIId(), theClientDto);
							 * 
							 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
							 * mwstsatzDto.getIIMwstsatzbezId(), theClientDto);
							 */
							MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(angebotpositionDto.getMwstsatzIId());
							BigDecimal mwstBetrag = Helper.getProzentWert(angebotpositionDto.getNNettoeinzelpreis(),
									new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
							angebotpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
							angebotpositionDto.setNMwstbetrag(mwstBetrag);
							angebotpositionDto
									.setNBruttoeinzelpreis(angebotpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
						}

						if (aRechnungpositionBasis[i].isIntelligenteZwischensumme()) {
							ZwsPositionMapper mapper = new ZwsPositionMapper(getAngebotpositionFac(),
									getAngebotpositionFac());
							mapper.map(aRechnungpositionBasis[i], angebotpositionDto, iIdAngebotKopie);

						}

						if (aRechnungpositionBasis[i].getPositioniIdArtikelset() != null) {
							angebotpositionDto.setPositioniIdArtikelset(positionIIdSet);
						}

						if (aRechnungpositionBasis[i].getPositionIIdZugehoerig() != null) {
							angebotpositionDto.setPositionIIdZugehoerig(
									hm.get(aRechnungpositionBasis[i].getPositionIIdZugehoerig()));
						}
						angebotpositionDto.setBelegIId(iIdAngebotKopie);
						Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(angebotpositionDto,
								false, theClientDto);

						hm.put(aRechnungpositionBasis[i].getIId(), angebotpositionIId);

						if (angebotpositionDto.getPositioniIdArtikelset() == null) {
							positionIIdSet = angebotpositionIId;
						}

					}

				} else {

					AngebotpositionDto[] posDtos = getBelegpositionkonvertierungFac().konvertiereNachAngebotpositionDto(
							new RechnungPositionDto[] { rechnungPositionDto }, angebotDto.getKundeIIdAngebotsadresse(),
							angebotDto.getTBelegdatum(), theClientDto);

					AngebotpositionDto angebotpositionDto = posDtos[0];

					// Wenn sich sie MWST seither geaendert hat
					if (angebotpositionDto.getMwstsatzIId() != null) {
						/*
						 * MwstsatzDto mwstsatzDto = getMandantFac()
						 * .mwstsatzFindByPrimaryKey(angebotpositionDto.getMwstsatzIId(), theClientDto);
						 * 
						 * mwstsatzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
						 * mwstsatzDto.getIIMwstsatzbezId(), theClientDto);
						 */
						MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(angebotpositionDto.getMwstsatzIId());
						BigDecimal mwstBetrag = Helper.getProzentWert(angebotpositionDto.getNNettoeinzelpreis(),
								new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
						angebotpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
						angebotpositionDto.setNMwstbetrag(mwstBetrag);
						angebotpositionDto
								.setNBruttoeinzelpreis(angebotpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
					}

					if (aRechnungpositionBasis[i].isIntelligenteZwischensumme()) {
						ZwsPositionMapper mapper = new ZwsPositionMapper(getAngebotpositionFac(),
								getAngebotpositionFac());
						mapper.map(aRechnungpositionBasis[i], angebotpositionDto, iIdAngebotKopie);

					}

					if (aRechnungpositionBasis[i].getPositioniIdArtikelset() != null) {
						angebotpositionDto.setPositioniIdArtikelset(positionIIdSet);
					}

					if (aRechnungpositionBasis[i].getPositionIIdZugehoerig() != null) {
						angebotpositionDto
								.setPositionIIdZugehoerig(hm.get(aRechnungpositionBasis[i].getPositionIIdZugehoerig()));
					}

					angebotpositionDto.setBelegIId(iIdAngebotKopie);

					Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(angebotpositionDto,
							false, theClientDto);

					hm.put(aRechnungpositionBasis[i].getIId(), angebotpositionIId);

					if (angebotpositionDto.getPositioniIdArtikelset() == null) {
						positionIIdSet = angebotpositionIId;
					}

				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAngebotKopie;
	}

	private Timestamp lieferterminAnhandBetriebskalenderBerechnen(int iLieferzeitInStunden, String einheit,
			TheClientDto theClientDto) {

		// PJ20826

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		Integer tagesartIId_BU = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, theClientDto).getIId();

		// Berechnung beginnt Morgen
		Calendar cLiefertermin = Calendar.getInstance();
		cLiefertermin.setTime(Helper.cutDate(new java.util.Date()));
		cLiefertermin.add(Calendar.DAY_OF_MONTH, 1);

		if (einheit.equals(AngebotServiceFac.ANGEBOTEINHEIT_WOCHE)) {

			int iLieferzeitInWochen = iLieferzeitInStunden / 24 / 7;

			cLiefertermin.add(Calendar.WEEK_OF_YEAR, iLieferzeitInWochen);

			int iAzahlTageSchonVersetzt = 0;

			while (iAzahlTageSchonVersetzt < 999) {

				boolean bArbeitstag = true;

				if (cLiefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
						|| cLiefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					bArbeitstag = false;
				} else {

					String sQuery = "SELECT bk.flrtagesart.i_id FROM FLRBetriebskalender bk WHERE bk.mandant_c_nr='"
							+ theClientDto.getMandant() + "' AND bk.t_datum='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(cLiefertermin.getTimeInMillis())) + "'";

					Session session = FLRSessionFactory.getFactory().openSession();

					org.hibernate.Query bk = session.createQuery(sQuery);

					bk.setMaxResults(1);

					List<?> resultList = bk.list();

					Iterator<?> resultListIterator = resultList.iterator();

					if (resultListIterator.hasNext()) {
						Integer tagesartIId = (Integer) resultListIterator.next();
						if (tagesartIId.equals(tagesartIId_Feiertag) || tagesartIId.equals(tagesartIId_Halbtag)
								|| tagesartIId.equals(tagesartIId_BU)) {
							bArbeitstag = false;
						}
					}
					session.close();

				}

				if (bArbeitstag == false) {
					cLiefertermin.add(Calendar.DAY_OF_MONTH, 1);
					iAzahlTageSchonVersetzt++;
				} else {
					break;
				}

			}
			return new Timestamp(cLiefertermin.getTime().getTime());
		} else {

			int iLieferzeitInTagen = iLieferzeitInStunden / 24;

			int iLieferzeitSchonVersetzt = 0;

			while (iLieferzeitSchonVersetzt < 999) {

				cLiefertermin.add(Calendar.DAY_OF_MONTH, 1);

				boolean bArbeitstag = true;

				if (cLiefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
						|| cLiefertermin.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					bArbeitstag = false;
				} else {

					String sQuery = "SELECT bk.flrtagesart.i_id FROM FLRBetriebskalender bk WHERE bk.mandant_c_nr='"
							+ theClientDto.getMandant() + "' AND bk.t_datum='"
							+ Helper.formatDateWithSlashes(new java.sql.Date(cLiefertermin.getTimeInMillis())) + "'";

					Session session = FLRSessionFactory.getFactory().openSession();

					org.hibernate.Query bk = session.createQuery(sQuery);

					bk.setMaxResults(1);

					List<?> resultList = bk.list();

					Iterator<?> resultListIterator = resultList.iterator();

					if (resultListIterator.hasNext()) {
						Integer tagesartIId = (Integer) resultListIterator.next();
						if (tagesartIId.equals(tagesartIId_Feiertag) || tagesartIId.equals(tagesartIId_Halbtag)
								|| tagesartIId.equals(tagesartIId_BU)) {
							bArbeitstag = false;
						}
					}
					session.close();

				}

				if (bArbeitstag == true) {
					iLieferzeitSchonVersetzt++;

				}

				if (iLieferzeitSchonVersetzt >= iLieferzeitInTagen) {
					break;
				}

			}
			return new Timestamp(cLiefertermin.getTime().getTime());
		}

	}

	/*
	 * private class MwstsatzCache extends HvCreatingCachingProvider<Integer,
	 * MwstsatzDto> { private final BelegVerkaufDto belegDto; private final
	 * TheClientDto theClientDto;
	 * 
	 * public MwstsatzCache(final BelegVerkaufDto belegDto, final TheClientDto
	 * theClientDto) { this.belegDto = belegDto; this.theClientDto = theClientDto; }
	 * 
	 * @Override protected MwstsatzDto provideValue(Integer key, Integer
	 * transformedKey) { return getMandantFac().mwstsatzZuDatumEvaluate( new
	 * MwstsatzId(key), belegDto.getTBelegdatum(), theClientDto); } }
	 */

	public Integer erzeugeAuftragAusAngebot(Integer iIdAngebotI, boolean bMitZeitDaten, boolean bRahmenauftrag,
			boolean bAlternativPositionenUebernehmen, final TheClientDto theClientDto) throws EJBExceptionLP {
		checkAngebotIId(iIdAngebotI);
		AngebotDto angebotBasisDto = null;

		try {
			angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(iIdAngebotI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		Integer iIdAuftrag = null;

		try {

			ParametermandantDto parametermandantLieferzeitDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
			int defaultLieferzeitAuftrag = ((Integer) parametermandantLieferzeitDto.getCWertAsObject()).intValue();

			ParametermandantDto parametermandantKopftextuebernehmenDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_KOPFTEXT_UEBERNEHMEN);
			boolean bKopftextUebernehmen = (Boolean) parametermandantKopftextuebernehmenDto.getCWertAsObject();

			AuftragDto auftragDto = (AuftragDto) angebotBasisDto.cloneAsAuftragDto(defaultLieferzeitAuftrag * 24);

			// PJ18581
			if (bRahmenauftrag) {
				auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_RAHMEN);
			}

			// PJ21028
			if (bKopftextUebernehmen && angebotBasisDto.getXKopftextuebersteuert() != null) {
				auftragDto.setCKopftextUebersteuert(angebotBasisDto.getXKopftextuebersteuert());
			}

			// PJ20826

			ParametermandantDto parametermandantLieferzeitAusAngebot = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_LIEFERZEIT_AUS_ANGEBOT);
			boolean bLieferzeitAusAngebot = ((Boolean) parametermandantLieferzeitAusAngebot.getCWertAsObject());

			if (bLieferzeitAusAngebot) {

				int iLieferzeitInStunden = angebotBasisDto.getILieferzeitinstunden();

				auftragDto.setDLiefertermin(lieferterminAnhandBetriebskalenderBerechnen(iLieferzeitInStunden,
						angebotBasisDto.getAngeboteinheitCNr(), theClientDto));

			}

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotBasisDto.getKundeIIdAngebotsadresse(),
					theClientDto);

			// PJ20266
			ParametermandantDto parametermandantVertreterDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGSVERTRETER_AUS_ANGEBOT);
			boolean auftragsvertreterAusAngebot = ((Boolean) parametermandantVertreterDto.getCWertAsObject());

			if (auftragsvertreterAusAngebot == false) {

				ParametermandantDto parametermandantVorschlagDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
				boolean vertreterVorschlagAusKunde = ((Boolean) parametermandantVorschlagDto.getCWertAsObject());

				if (vertreterVorschlagAusKunde == true && kundeDto.getPersonaliIdProvisionsempfaenger() != null) {
					auftragDto.setPersonalIIdVertreter(kundeDto.getPersonaliIdProvisionsempfaenger());
				} else {
					auftragDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

				}
			} else {
				// SP6638 Ist Vertreter noch im Unternehmen
				auftragDto.setPersonalIIdVertreter(
						pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(auftragDto.getPersonalIIdVertreter(),
								auftragDto.getKundeIIdAuftragsadresse(), auftragDto.getTBelegdatum(), theClientDto));

			}

			// PJ21385
			auftragDto.setPersonalIIdVertreter2(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(auftragDto.getPersonalIIdVertreter2(),
							auftragDto.getKundeIIdAuftragsadresse(), auftragDto.getTBelegdatum(), theClientDto));

			// SP458
			if (kundeDto.getPartnerIIdRechnungsadresse() != null) {
				KundeDto kundeRechnungsadresseDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
						kundeDto.getPartnerIIdRechnungsadresse(), kundeDto.getMandantCNr(), theClientDto);
				if (kundeRechnungsadresseDto != null) {
					// SP9000
					if (!kundeRechnungsadresseDto.getIId().equals(auftragDto.getKundeIIdRechnungsadresse())) {
						auftragDto.setAnsprechpartnerIIdRechnungsadresse(null);
					}
					auftragDto.setKundeIIdRechnungsadresse(kundeRechnungsadresseDto.getIId());
				}
			}

			auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

			auftragDto.setVerrechnungsmodellIId(kundeDto.getVerrechnungsmodellIId());

			auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(getLocaleFac().getWechselkurs2(
					getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto).getWaehrungCNr(),
					auftragDto.getCAuftragswaehrung(), theClientDto).doubleValue()));

			iIdAuftrag = getAuftragFac().createAuftrag(auftragDto, theClientDto);

			auftragDto = getAuftragFac().auftragFindByPrimaryKey(iIdAuftrag);

			// UW 22.03.06 alle Positionen aus dem Angebot uebernehmen, das
			// bleibt auf Bean Basis
			AngebotpositionDto[] aAngebotpositionBasis = getAngebotpositionFac()
					.angebotpositionFindByAngebotIId(iIdAngebotI, theClientDto);

			Integer positionIIdSet = null;

			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();

			MwstsatzEvaluateCache mwstsatzCache = new MwstsatzEvaluateCache(auftragDto, theClientDto);

			for (int i = 0; i < aAngebotpositionBasis.length; i++) {
				AuftragpositionDto auftragpositionDto = aAngebotpositionBasis[i].cloneAsAuftragposition();

				if (bAlternativPositionenUebernehmen == false && aAngebotpositionBasis[i].getBAlternative() != null
						&& Helper.short2boolean(aAngebotpositionBasis[i].getBAlternative()) == true) {

					continue;
				}

				// Wenn sich sie MWST seither geaendert hat
				if (auftragpositionDto.getMwstsatzIId() != null) {
					/*
					 * MwstsatzDto mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByPrimaryKey(auftragpositionDto.getMwstsatzIId(), theClientDto);
					 * 
					 * mwstsatzDto = getMandantFac()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzDto.getIIMwstsatzbezId(),
					 * theClientDto);
					 */
					MwstsatzDto mwstsatzDto = mwstsatzCache.getValueOfKey(auftragpositionDto.getMwstsatzIId());

					BigDecimal mwstBetrag = Helper.getProzentWert(auftragpositionDto.getNNettoeinzelpreis(),
							new BigDecimal(mwstsatzDto.getFMwstsatz()), 4);
					auftragpositionDto.setMwstsatzIId(mwstsatzDto.getIId());
					auftragpositionDto.setNMwstbetrag(mwstBetrag);
					auftragpositionDto.setNBruttoeinzelpreis(auftragpositionDto.getNNettoeinzelpreis().add(mwstBetrag));
				}

				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() != null) {
					auftragpositionDto.setPositioniIdArtikelset(positionIIdSet);
				}

				if (aAngebotpositionBasis[i].getPositionIIdZugehoerig() != null) {
					auftragpositionDto
							.setPositionIIdZugehoerig(hm.get(aAngebotpositionBasis[i].getPositionIIdZugehoerig()));
				}

				auftragpositionDto.setBelegIId(iIdAuftrag);
				auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());

				if (aAngebotpositionBasis[i].getPositionsartCNr()
						.equals(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE)) {
					// WH: 16.12.05 Aus der AGStueckliste wird bei der
					// Ueberleitung in den Auftrag
					// eine Handeingabeposition
					auftragpositionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE);
					// PJ21341 ausser die Agstkl wurde bereits in einen STKL uebergeleitet
					if (aAngebotpositionBasis[i].getAgstklIId() != null) {
						AgstklDto agstklDto = getAngebotstklFac()
								.agstklFindByPrimaryKey(aAngebotpositionBasis[i].getAgstklIId());
						if (agstklDto.getStuecklisteIId() != null) {
							StuecklisteDto stklDto = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(agstklDto.getStuecklisteIId(), theClientDto);
							auftragpositionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
							auftragpositionDto.setArtikelIId(stklDto.getArtikelIId());
						} else {
							if (auftragpositionDto.getCBez() == null || auftragpositionDto.getCBez().length() == 0) {
								auftragpositionDto.setCBez("");
							}

							auftragpositionDto.setCZusatzbez(aAngebotpositionBasis[i].getPositionsartCNr());
							if (auftragpositionDto.getEinheitCNr() == null) {
								auftragpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
							}
						}
					}

					auftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				}

				if (aAngebotpositionBasis[i].isIntelligenteZwischensumme()) {
					ZwsPositionMapper mapper = new ZwsPositionMapper(getAngebotpositionFac(), getAuftragpositionFac());
					mapper.map(aAngebotpositionBasis[i], auftragpositionDto, iIdAuftrag);
					//
					// auftragpositionDto.setBZwsPositionspreisDrucken(
					// aAngebotpositionBasis[i].getBZwsPositionspreisZeigen());
					// Integer von = getAngebotpositionFac().getPositionNummer(
					// aAngebotpositionBasis[i].getZwsVonPosition());
					// auftragpositionDto
					// .setZwsVonPosition(getAuftragpositionFac()
					// .getPositionIIdFromPositionNummer(
					// iIdAuftrag, von));
					// Integer bis = getAngebotpositionFac().getPositionNummer(
					// aAngebotpositionBasis[i].getZwsBisPosition());
					// auftragpositionDto
					// .setZwsBisPosition(getAuftragpositionFac()
					// .getPositionIIdFromPositionNummer(
					// iIdAuftrag, bis));
				}

				// PJ22111

				// damit werden auch die Reservierungen angelegt
				Integer auftragpositionIId = getAuftragpositionFac().createAuftragposition(auftragpositionDto, false,
						theClientDto);

				getBelegartmediaFac().kopiereBelegartmedia(QueryParameters.UC_ID_ANGEBOTPOSITION,
						aAngebotpositionBasis[i].getIId(), QueryParameters.UC_ID_AUFTRAGPOSITION, auftragpositionIId,
						theClientDto);

				hm.put(aAngebotpositionBasis[i].getIId(), auftragpositionIId);

				if (aAngebotpositionBasis[i].getPositioniIdArtikelset() == null) {
					positionIIdSet = auftragpositionIId;
				}

			}

			// PJ21361
			getAuftragFac().mindermengenzuschlagEntfernen(auftragDto, theClientDto);

			// SP7835

			// wenn das Angebot noch nicht erledigt war, muss der Status
			// geaendert werden
			if (!angebotBasisDto.getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
				angebotErledigen(iIdAngebotI, AngebotServiceFac.ANGEBOTERLEDIGUNGSGRUND_AUFTRAGERHALTEN, theClientDto);
			}
			if (bMitZeitDaten) {
				getZeiterfassungFac().konvertiereAngebotszeitenNachAuftragzeiten(iIdAngebotI, iIdAuftrag, theClientDto);
			}

			myLogger.exit("Der Auftrag wurde mit " + aAngebotpositionBasis.length + " Positionen erzeugt.");
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return iIdAuftrag;
	}

	/**
	 * Methode zum Erzeugen eines Lieferscheins aus einem bestehenden Angebot.
	 * 
	 * @param iIdAngebotI  PK des bestehenden Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer PK des neuen Lieferscheins
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAngebot(Integer iIdAngebotI, TheClientDto theClientDto) throws EJBExceptionLP {

		checkAngebotIId(iIdAngebotI);

		Integer lieferscheinIId = null;

		try {
			AngebotDto angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(iIdAngebotI, theClientDto);

			// Schritt 1: Den Auftrag zum Angebot erzeugen
			Integer auftragIId = erzeugeAuftragAusAngebot(iIdAngebotI, false, false, true, theClientDto);

			// Schritt 2: Den Auftrag automatisch verarbeiten -> Status Offen
			AuftragDto auftragBasisDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

			KundeDto kundeBasisDto = getKundeFac().kundeFindByPrimaryKey(auftragBasisDto.getKundeIIdAuftragsadresse(),
					theClientDto);

			auftragBasisDto.setAuftragtextIIdKopftext(getAuftragServiceFac().auftragtextFindByMandantLocaleCNr(
					theClientDto.getMandant(), kundeBasisDto.getPartnerDto().getLocaleCNrKommunikation(),
					MediaFac.MEDIAART_KOPFTEXT, theClientDto).getIId());

			auftragBasisDto.setAuftragtextIIdFusstext(getAuftragServiceFac().auftragtextFindByMandantLocaleCNr(
					theClientDto.getMandant(), kundeBasisDto.getPartnerDto().getLocaleCNrKommunikation(),
					MediaFac.MEDIAART_FUSSTEXT, theClientDto).getIId());

			getAuftragFac().updateAuftrag(auftragBasisDto, null, theClientDto);

			// pruefen, ob das Hauptlager des Mandanten existiert wegen der
			// nachfolgenden Wertebrechnung
			LagerDto hauptlagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			// den Auftrag aktivieren
			getAuftragFac().aktiviereAuftrag(auftragBasisDto.getIId(), theClientDto);

			// den Auftrag drucken -> Status Offen
			getAuftragReportFac().printAuftragbestaetigung(auftragIId, null, Boolean.TRUE, null, theClientDto);

			// Schritt 3: Einen Lieferschein zum Auftrag erzeugen
			lieferscheinIId = getAuftragFac().erzeugeLieferscheinAusAuftrag(auftragIId, null, null, theClientDto);
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
	 * @param iIdAngebotI  PK des bestehenden Angebots
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer PK der neuen Rechnung
	 * @throws EJBExceptionLP Ausnahme
	 */
	// public Integer erzeugeRechnungAusAngebot(Integer iIdAngebotI,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// checkAngebotIId(iIdAngebotI);
	// AngebotDto angebotBasisDto = null;
	// try {
	// angebotBasisDto = getAngebotFac().angebotFindByPrimaryKey(
	// iIdAngebotI, theClientDto);
	// } catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	// Integer iIdRechnung = null;
	// return iIdRechnung;
	// }

	/**
	 * Ueber Hibernate alle berechnungsrelevanten Positionen eines Angebots holen.
	 * <br>
	 * Diese Methode mu&szlig; innerhalb einer offenen Hibernate Session aufgerufen
	 * werden. <br>
	 * 
	 * @todo diese Methode muesste eigentlich in der AngebotpositionFac sitzen... PJ
	 *       3790
	 * 
	 * @param sessionI             die Hibernate Session
	 * @param iIdAngebotI          PK des Angebots
	 * @param bNurMengenbehafteteI nur mengenbehaftete Positionen beruecksichtigen
	 * @param bNurPositiveMengenI  nur positive Mengen beruecksichtigen; wird bei
	 *                             !bNurMengenbehafteteI nicht ausgewertet
	 * @param bOhneAlternativenI   alternative Positionen werden nicht
	 *                             beruecksichtigt
	 * @return FLRAngebotpositionReport[] die berechnungsrelevanten Positionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	private FLRAngebotpositionReport[] holeAngebotpositionen(Session sessionI, Integer iIdAngebotI,
			boolean bNurMengenbehafteteI, boolean bNurPositiveMengenI, boolean bOhneAlternativenI)
			throws EJBExceptionLP {
		FLRAngebotpositionReport[] aFLRAngebotpositionReport = null;

		try {
			Criteria crit = sessionI.createCriteria(FLRAngebotpositionReport.class);

			crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_ANGEBOT_I_ID, iIdAngebotI));

			if (bNurMengenbehafteteI) {
				crit.add(Restrictions.isNotNull(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE));

				if (bNurPositiveMengenI) {
					crit.add(Restrictions.gt(AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE, new BigDecimal(0)));
				}
			}

			// nur Positionen beruecksichtigen, die keine Alternative sind
			if (bOhneAlternativenI) {
				crit.add(Restrictions.eq(AngebotpositionFac.FLR_ANGEBOTPOSITION_B_ALTERNATIVE, new Short((short) 0)));
			}

			crit.addOrder(Order.asc("i_sort"));

			// Liste aller Positionen, die behandelt werden sollen
			List<?> list = crit.list();
			aFLRAngebotpositionReport = new FLRAngebotpositionReport[list.size()];
			aFLRAngebotpositionReport = (FLRAngebotpositionReport[]) list.toArray(aFLRAngebotpositionReport);
		} catch (HibernateException he) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, new Exception(he));
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return aFLRAngebotpositionReport;
	}

	@Override
	public void setzeVersandzeitpunktAufJetzt(Integer angebotIId, String druckart) {
		if (angebotIId != null) {
			Angebot angebot = em.find(Angebot.class, angebotIId);
			angebot.setTVersandzeitpunkt(new Timestamp(System.currentTimeMillis()));
			angebot.setCVersandtype(druckart);
			em.merge(angebot);
			em.flush();
		}

	}

	@Override
	public BelegPruefungDto aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.aktiviereBelegControlled(this, iid, t, theClientDto);
		// new BelegAktivierungController(this).aktiviereBelegControlled(iid, t,
		// theClientDto);
	}

	@Override
	public BelegPruefungDto berechneBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneBelegControlled(this, iid, theClientDto);
		// return new
		// BelegAktivierungController(this).berechneBelegControlled(iid,
		// theClientDto);
	}

	@Override
	public List<Timestamp> getAenderungsZeitpunkte(Integer iid) throws EJBExceptionLP, RemoteException {
		Angebot angebot = em.find(Angebot.class, iid);
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		timestamps.add(angebot.getTAendern());
		timestamps.add(angebot.getTManuellerledigt());
		timestamps.add(angebot.getTStorniert());
		return timestamps;
	}

	@Override
	public BelegPruefungDto berechneAktiviereBelegControlled(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return belegAktivierungFac.berechneAktiviereControlled(this, iid, theClientDto);
		// new BelegAktivierungController(this).berechneAktiviereControlled(iid,
		// theClientDto);
	}

	@Override
	public BelegDto getBelegDto(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BelegDto belegDto = angebotFindByPrimaryKey(iid, theClientDto);
		belegDto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);
		return belegDto;
	}

	@Override
	public BelegpositionDto[] getBelegPositionDtos(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getAngebotpositionFac().angebotpositionFindByAngebotIIdOhneAlternative(iid, theClientDto);
	}

	@Override
	public Integer getKundeIdDesBelegs(BelegDto belegDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return ((AngebotDto) belegDto).getKundeIIdAngebotsadresse();
	}

	@Override
	public void pruefeAktivierbarRecht(Integer belegId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		boolean rechtAktivieren = getTheJudgeFac().hatRecht(RechteFac.RECHT_ANGB_AKTIVIEREN, theClientDto);
		boolean rechtCUD = getTheJudgeFac().hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_CUD, theClientDto);
		if (!(rechtAktivieren || rechtCUD)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		}
		// if (!getTheJudgeFac().hatRecht(RechteFac.RECHT_ANGB_AKTIVIEREN,
		// theClientDto)) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
		// "FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN");
		// }
	}

	// SP5524
	@Override
	public void repairAngebotZws5524(Integer angebotId, TheClientDto theClientDto) throws RemoteException {
		AngebotDto angebotDto = angebotFindByPrimaryKey(angebotId, theClientDto);
		if (AngebotServiceFac.ANGEBOTSTATUS_STORNIERT.equals(angebotDto.getStatusCNr()))
			return;

		AngebotpositionDto[] anPosDto = getAngebotpositionFac().angebotpositionFindByAngebotIId(angebotDto.getIId());
		getBelegVerkaufFac().prepareIntZwsPositions(anPosDto, angebotDto);
		Set<Integer> modifiedPositions = getBelegVerkaufFac().adaptIntZwsPositions(anPosDto);
		getBelegVerkaufFac().saveIntZwsPositions(anPosDto, modifiedPositions, Angebotposition.class);
	}

	@Override
	public List<Integer> repairAngebotZws5524GetList(TheClientDto theClientDto) {

		try {
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT DISTINCT angebot_i_id "
					+ " FROM FLRAngebotposition AS anpos WHERE anpos.positionart_c_nr = 'IZwischensumme'";
			org.hibernate.Query angebotIdsQuery = session.createQuery(sQuery);
			List<Integer> resultList = new ArrayList<Integer>();
			List<Integer> queryList = (List<Integer>) angebotIdsQuery.list();
			for (Integer angebot_i_id : queryList) {
				Angebot angebot = em.find(Angebot.class, angebot_i_id);
				if (theClientDto.getMandant().equals(angebot.getMandantCNr())) {
					resultList.add(angebot_i_id);
				}
			}

			session.close();
			return resultList;
		} catch (Exception e) {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public AngebotDto erzeugeAenderungsangebot(Integer angebotIId, TheClientDto theClientDto) {
		Validator.notNull(angebotIId, "angebotIId");

		synchronized (angebotIId) {
			Angebot angebot = em.find(Angebot.class, angebotIId);
			Versionizer versionizer = new Versionizer(em);
			versionizer.incrementVersion(angebot);

			AngebotDto dto = angebotFindByPrimaryKey(angebotIId, theClientDto);
			dto.setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			updateAngebot(dto, null, theClientDto);

			return dto;
		}
	}
}
