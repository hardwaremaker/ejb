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
 *******************************************************************************/
package com.lp.server.personal.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.ejb.Taetigkeit;
import com.lp.server.personal.service.PersonalApiFac;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacLocal;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Stateless
public class PersonalApiFacBean extends PersonalFacBean implements PersonalApiFac {
	@PersistenceContext
	private EntityManager em;
	@EJB
	private ZeiterfassungFacLocal zfa;

	private Personal getPersonal(String cAusweis) {
		Query query = em.createNamedQuery("PersonalfindByCAusweis");
		query.setParameter(1, cAusweis);
		Personal personal = (Personal) query.getSingleResult();
		return personal;
	}

	private Los getLos(String losCNr, String mandantCNr) {
		Query query = em.createNamedQuery("LosfindByCNrMandantCNr");
		query.setParameter(1, losCNr);
		query.setParameter(2, mandantCNr);
		Los los = null;
		try {
			los = (Los) query.getSingleResult();
		} catch (NoResultException e1) {
			//
		}
		return los;
	}
	
	private Taetigkeit getTaetigkeit(String cNr) {
		Query query1 = em.createNamedQuery("TaetigkeitfindByCNr");
		query1.setParameter(1, cNr);
		return (Taetigkeit) query1.getSingleResult();
	}
	
	private Integer getIntegerParameter(String mandantCnr, String kategorie,
			String parameter) {
		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(mandantCnr, kategorie,
					parameter);
		} catch (Exception e) {
			return new Integer(0);
		}
		return new Integer(p.getCWert());
	}
	
	private String getStringParameter(String mandantCnr, String kategorie,
			String parameter) {
		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(mandantCnr, kategorie,
					parameter);
		} catch (Exception e) {
			return null;
		}
		return p.getCWert();
	}
	
	private boolean istLosBuchenErlaubt(String statusCNr) {
		return istLosBuchenErlaubt(statusCNr, false);
	}

	private boolean istLosBuchenErlaubt(String losStatusCNr,
			boolean buchenStatusAngelegt) {
		if (buchenStatusAngelegt
				&& losStatusCNr
						.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ANGELEGT))
			return true;
		else {
			if (losStatusCNr
					.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ANGELEGT)
					|| losStatusCNr
							.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_AUSGEGEBEN)
					|| losStatusCNr
							.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_GESTOPPT)
					|| losStatusCNr
							.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ERLEDIGT)
					|| losStatusCNr
							.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_STORNIERT)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	private LosablieferungDto abliefernLos(TheClientDto theClientDto, Los los,
			BigDecimal bdGesamt, LosablieferungDto laDto) {
		boolean fertig = false;
		
		// PJ 18681 Fertig wenn alles abgeliefert
		if (getIntegerParameter(theClientDto.getMandant(), 
				ParameterFac.KATEGORIE_FERTIGUNG, 
				ParameterFac.PARAMETER_AUTOFERTIG_ABLIEFERUNG_TERMINAL).intValue() == 1) {
			if (los.getNLosgroesse().compareTo(bdGesamt) != 1) {
				// wenn gesamte Abliefermenge >= Losmenge dann Position ist Fertig
				fertig = true;
			}
		}
					
		// SP 2013/01671 neue Methode ohne Preisaufrollung
		return getFertigungFac().createLosablieferungFuerTerminalOhnePreisberechnung(laDto,
						theClientDto, fertig);
	}
	
	private Integer getArtikelIId(String mandantCNr, String artikelCNr) {
		Query query = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
		query.setParameter(1, artikelCNr);
		query.setParameter(2, mandantCNr);
		Artikel artikel = (Artikel) query.getSingleResult();
		if (artikel == null)
			return null;
		else
			return artikel.getIId();
	}
	
	private Integer getStuecklisteIId(String artikelCNr, String mandantCNr) {
		Integer artikelIId = getArtikelIId(mandantCNr, artikelCNr);
		if (artikelIId != null) {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, mandantCNr);
			Stueckliste stkl = (Stueckliste) query.getSingleResult();
			if (stkl != null)
				return stkl.getIId();
		}
		return null;
	}
	
	@Override
	public int bucheLosGroessenAenderung(String cSeriennummerLeser, String idUser, 
			String station, String losCNr, Integer menge, String cAusweis) {
		TheClientDto theClientDto = check(idUser);
		Personal personal = null;
		if (station.equals("MEMOR")) {
			if (theClientDto.getIDPersonal() == null) {
				return -4;
			}
			personal = em.find(Personal.class, theClientDto.getIDPersonal());
		} else { 
			personal = getPersonal(cAusweis);
			// hier wird das Personal mit dem buchenden uebersteuert, damit die
			// folgenden Buchungen
			// die PersonalID des Buchenden verwendet wird!!
			theClientDto.setIDPersonal(personal.getIId());
		}
		if (personal == null) {
			return -4;
		}

		Los los = getLos(losCNr, theClientDto.getMandant());
		if (los == null) {
			// Los nicht vorhanden
			return -1;
		}

		try {
			getFertigungFac().aendereLosgroesse(los.getIId(), menge, true,
					theClientDto);

			String zusatzStatus = getStringParameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_FERTIGUNG_GROESSENAENDERUNG_ZUSATZSTATUS);
			if (zusatzStatus != null && zusatzStatus.trim().length() > 0) {
				ZusatzstatusDto zsDto = getFertigungFac()
						.zusatzstatusFindByMandantCNrCBez(
								theClientDto.getMandant(), zusatzStatus);
				if (zsDto != null) {
					LoszusatzstatusDto loszusatzstatusDto = getFertigungFac()
							.loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(
									los.getIId(), zsDto.getIId());
					if (loszusatzstatusDto == null) {
						loszusatzstatusDto = new LoszusatzstatusDto();
						loszusatzstatusDto.setLosIId(los.getIId());
						loszusatzstatusDto.setZusatzstatusIId(zsDto.getIId());
						getFertigungFac().createLoszusatzstatus(
								loszusatzstatusDto, theClientDto);
					}
				}
			}

		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		} catch (EJBExceptionLP e) {
			if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN)
				// Ablieferungen > neuer Menge
				return -5;
			else if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT)
				// Status erlaubt keine Buchung
				return -2;
			else if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT)
				// Status erlaubt keine Buchung
				return -2;
			else if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN)
				// Status erlaubt keine Buchung
				return -2;
			else
				throw new EJBExceptionLP(e);
		}
		return 1;
	}
	
	public int bucheLosAblieferung(String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer menge, String cAusweis) {
		TheClientDto theClientDto = check(idUser);
		Personal personal = null;
		if (station.equals("MEMOR")) {
			if (theClientDto.getIDPersonal() == null) {
				return -4;
			}
			personal = em.find(Personal.class, theClientDto.getIDPersonal());
		} else { 
			personal = getPersonal(cAusweis);
			// hier wird das Personal mit dem buchenden uebersteuert, damit die
			// folgenden Buchungen
			// die PersonalID des Buchenden verwendet wird!!
			theClientDto.setIDPersonal(personal.getIId());
		}
		if (personal == null) {
			return -4;
		}

		Los los = getLos(losCNr, theClientDto.getMandant());
		if (los == null) {
			// Los nicht vorhanden
			return -1;
		}

		if (!istLosBuchenErlaubt(los.getStatusCNr())) {
			// Status erlaubt keine Buchung
			return -2;
		}
//TODO: Rolle setzen
		if (!getLagerFac().hatRolleBerechtigungAufLager(los.getLagerIIdZiel(),
				theClientDto)) {
			// Person hat kein Recht umn auf Ziellager zu buchen
			return -4;
		}

		// TODO: Parameter fuer nicht "ueberbuchen"
		try {
			BigDecimal bdMenge = new BigDecimal(menge);

			BigDecimal bdGesamt = getFertigungFac().getErledigteMenge(
					los.getIId(), theClientDto).add(bdMenge);
			if (los.getNLosgroesse().compareTo(bdGesamt) == -1) {
				// wenn gesamte Abliefermenge > Losmenge dann Losgroesse
				// anpassen
				getFertigungFac().aendereLosgroesse(los.getIId(),
						bdGesamt.intValue(), false, theClientDto);
			}

			// fehlendes Material buchen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getIId());
			getFertigungFac().bucheMaterialAufLos(losDto, bdMenge, false,
					false, true, theClientDto, null, false);

			// Pruefung ist abhaengig von Parameter
			if (getIntegerParameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_SOLLSATZGROESSE_PRUEFEN).intValue() == 1) {
				try {
					getFertigungFac()
							.pruefePositionenMitSollsatzgroesseUnterschreitung(
									los.getIId(), bdMenge, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {
						return -3;
					}
				}
			}

			// SP 2013/01036
			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			Integer zeitdatenIId = null;
			if (getIntegerParameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE).intValue() == 1) {
				zeitdatenDto.setPersonalIId(personal.getIId());
				zeitdatenDto.setCWowurdegebucht(station);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				zeitdatenDto.setTZeit(new java.sql.Timestamp(cal
						.getTimeInMillis()));
				zeitdatenDto.setTAendern(zeitdatenDto.getTZeit());
				zeitdatenDto.setTaetigkeitIId(getTaetigkeit(
						ZeiterfassungFac.TAETIGKEIT_ENDE).getIId());
				zeitdatenDto.setCBelegartnr(null);
				zeitdatenDto.setArtikelIId(null);
				zeitdatenDto.setIBelegartid(null);
				zeitdatenDto.setIBelegartpositionid(null);
				zeitdatenIId = zfa.createZeitdaten(zeitdatenDto, false, false,
						false, false, theClientDto);

				// PJ17797
				if (bdMenge.signum() == 1) {
					if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG,
							theClientDto)) {
						Integer lossollarbeitsplanIId = null;
						LossollarbeitsplanDto[] sollDtos = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(losDto.getIId());
						if (sollDtos.length > 0) {
							lossollarbeitsplanIId = sollDtos[sollDtos.length - 1]
									.getIId();
						} else {
							lossollarbeitsplanIId = getFertigungFac()
									.defaultArbeitszeitartikelErstellen(losDto,
											theClientDto);
						}

						LosgutschlechtDto losgutschlechtDto = new LosgutschlechtDto();
						losgutschlechtDto.setZeitdatenIId(zeitdatenIId);
						losgutschlechtDto
								.setLossollarbeitsplanIId(lossollarbeitsplanIId);
						losgutschlechtDto.setNGut(bdMenge);
						losgutschlechtDto.setNSchlecht(new BigDecimal(0));
						losgutschlechtDto.setNInarbeit(new BigDecimal(0));
						getFertigungFac().createLosgutschlecht(
								losgutschlechtDto, theClientDto);
					}
				}
			}

			LosablieferungDto laDto = new LosablieferungDto();
			laDto.setLosIId(los.getIId());
			laDto.setNMenge(bdMenge);
			abliefernLos(theClientDto, los, bdGesamt, laDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		return 1;
	}	

	public int bucheLosAblieferungSeriennummer(String idUser,
			String station, String losCNr, String artikelCNr, String cSeriennummer, String cVersion) {
		TheClientDto theClientDto = check(idUser);

		Los los = getLos(losCNr, theClientDto.getMandant());
		if (los == null) {
			// Los nicht vorhanden
			return -1;
		}

		if (!istLosBuchenErlaubt(los.getStatusCNr())) {
			// Status erlaubt keine Buchung
			return -2;
		}

		if (!getLagerFac().hatRolleBerechtigungAufLager(los.getLagerIIdZiel(),
				theClientDto)) {
			// Person hat kein Recht umn auf Ziellager zu buchen
			return -4;
		}

		Integer stuecklisteIId = null;
		try {
			stuecklisteIId = getStuecklisteIId(artikelCNr,
					theClientDto.getMandant());
		} catch (Exception e1) {
			//
		}
		if (stuecklisteIId == null)
			// Stueckliste nicht vorhanden
			return -5;

		if (!los.getStuecklisteIId().equals(stuecklisteIId))
			// Stueckliste ist nicht im Los
			return -6;

		try {
			// Menge kommt aus Anzahl der Seriennummern
			String[] seriennrs = cSeriennummer.split(",");
			BigDecimal bdMenge = new BigDecimal(seriennrs.length);

			BigDecimal bdGesamt = getFertigungFac().getErledigteMenge(
					los.getIId(), theClientDto).add(bdMenge);

			// fehlendes Material buchen
			LosDto losDto = getFertigungFac().losFindByPrimaryKey(los.getIId());
			getFertigungFac().bucheMaterialAufLos(losDto, bdMenge, false,
					false, true, theClientDto, null, false);

			try {
				getFertigungFac()
						.pruefePositionenMitSollsatzgroesseUnterschreitung(
								los.getIId(), bdMenge, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {
					return -3;
				}
			}
			LosablieferungDto laDto = new LosablieferungDto();
			laDto.setLosIId(los.getIId());
			laDto.setNMenge(bdMenge);
			ArrayList<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
			for (int i = 0; i < seriennrs.length; i++) {
				alSeriennrChargennrMitMenge
						.add(new SeriennrChargennrMitMengeDto(seriennrs[i]
								.trim(), new BigDecimal(1)));
			}
			laDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);

			LosablieferungDto losablieferungDto = abliefernLos(theClientDto, los, bdGesamt, laDto);
			if (losablieferungDto != null) {
				// version jetzt nachtragen
				getLagerFac().versionInLagerbewegungUpdaten(
						LocaleFac.BELEGART_LOSABLIEFERUNG,
						losablieferungDto.getIId(), cVersion);
			}
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		return 1;
	}
}
