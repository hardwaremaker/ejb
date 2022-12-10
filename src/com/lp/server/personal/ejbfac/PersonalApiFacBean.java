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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.service.PersonalApiFac;
import com.lp.server.personal.service.ZeiterfassungFacLocal;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LosId;
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
	
	//PJ21261 mit GutSchlecht
	private LosablieferungDto abliefernLos(TheClientDto theClientDto, Los los, LosablieferungTerminalDto laDto) throws RemoteException, EJBExceptionLP {

		LosablieferungDto losablieferungDto = getFertigungFac().createLosablieferungFuerTerminal(laDto,
							theClientDto);
		if (losablieferungDto != null) {
			// PJ20428
			getFertigungReportFac().printAblieferEtikettOnServer(
					losablieferungDto.getIId(), null, theClientDto);
		}
		
		return losablieferungDto;
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

	public int bucheLosAblieferungStueckzaehler(String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer zaehler, String cAusweis, String artikelCNr, String cChargennummer) {
		TheClientDto theClientDto = check(idUser);
		BigDecimal losErledigt;
		
		Los los = getLos(losCNr, theClientDto.getMandant());
		if (los == null) {
			// Los nicht vorhanden
			return -1;
		}

		try {
			losErledigt = getFertigungFac().getErledigteMenge(los.getIId(),
					theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
		// die zu buchende Menge ist der Zaehlerstand minus der bisherigen Ablieferungen
		Integer menge = zaehler - losErledigt.intValue();
		if (Integer.signum(menge) != 1) {
			return -7;
		} else {
			if (cChargennummer == null)
				return bucheLosAblieferung(cSeriennummerLeser, idUser, station, losCNr, menge, cAusweis);
			else
				return bucheLosAblieferungChargennummer(idUser, station, losCNr, artikelCNr, cChargennummer, new BigDecimal(menge));
		}
		
	}
	
	private Personal validateAndGetPersonal(String station, String cAusweis, TheClientDto theClientDto) {
		Personal personal = null;
		if (station.equals("MEMOR")) {
			if (theClientDto.getIDPersonal() == null) {
				return null;
			}
			personal = em.find(Personal.class, theClientDto.getIDPersonal());
		} else { 
			personal = getPersonal(cAusweis);
			// hier wird das Personal mit dem buchenden uebersteuert, damit die
			// folgenden Buchungen
			// die PersonalID des Buchenden verwendet wird!!
			theClientDto.setIDPersonal(personal.getIId());
		}
		
		return personal;
	}

	/**
	 * @param theClientDto
	 * @param los
	 */
	private int validateLosBerechtigungen(TheClientDto theClientDto, Los los, Integer ziellagerIId) {
		if (los == null) {
			// Los nicht vorhanden
			return -1;
		}

		if (!istLosBuchenErlaubt(los.getStatusCNr())) {
			// Status erlaubt keine Buchung
			return -2;
		}
//TODO: Rolle setzen
		if (!getLagerFac().hatRolleBerechtigungAufLager(ziellagerIId,
				theClientDto)) {
			// Person hat kein Recht umn auf Ziellager zu buchen
			return -4;
		}
		
		return 1;
	}	

	public int bucheLosAblieferung(String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer menge, String cAusweis) {
//		return getPersonalApiFac().bucheLosAblieferungSubtransaction(
//				cSeriennummerLeser, idUser, station, losCNr, menge, cAusweis);
		return bucheLosAblieferungSubtransaction(
				cSeriennummerLeser, idUser, station, losCNr, menge, cAusweis, null);
	}
	
	public int bucheLosAblieferung(String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer menge, String cAusweis, Integer mengeSchrott) {
//		return getPersonalApiFac().bucheLosAblieferungSubtransaction(
//				cSeriennummerLeser, idUser, station, losCNr, menge, cAusweis);
		return bucheLosAblieferungSubtransaction(
				cSeriennummerLeser, idUser, station, losCNr, menge, cAusweis, mengeSchrott);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int bucheLosAblieferungSubtransaction(
			String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer menge, String cAusweis, Integer mengeSchrott) {
		TheClientDto theClientDto = check(idUser);
		
		Personal personal = validateAndGetPersonal(station, cAusweis, theClientDto);
		if (personal == null) {
			return -4;
		}

		Los los = getLos(losCNr, theClientDto.getMandant());
		int losValidationValue = validateLosBerechtigungen(theClientDto, los, los.getLagerIIdZiel());
		if (losValidationValue < 0) {
			return losValidationValue;
		}

		try {
			LosablieferungDto losablieferungDto = null;
			if (mengeSchrott == null) {
				losablieferungDto = getFertigungFacLocal().losAbliefernUeberSoap(
					new LosId(los.getIId()), new BigDecimal(menge), station, theClientDto);
				// PJ21402
				if (losablieferungDto != null && station.startsWith("ZT ") && !theClientDto.getBenutzername().contains("|")) {
					theClientDto.setBenutzername(theClientDto.getBenutzername().trim() + "|" + station);
					getFertigungReportFac().printAblieferEtikettOnServer(
							losablieferungDto.getIId(), null, theClientDto);
				}
			} else {
				LosablieferungTerminalDto laDto = new LosablieferungTerminalDto();
				laDto.setMengeSchrott(new BigDecimal(mengeSchrott));
				laDto.setLosIId(los.getIId());
				laDto.setNMenge(new BigDecimal(menge));
				//ArrayList<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
				losablieferungDto = abliefernLos(theClientDto, los, laDto);
			}
			int returnValue = losablieferungDto.getIId();
			return returnValue > 0 ? 1 : returnValue;
		} catch (EJBExceptionLP ex) {
			if (EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN == ex.getCode()) {
				return -3;
			}
			throw new EJBExceptionLP(ex);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		
	}	

	public int bucheLosAblieferungSeriennummer(String idUser,
			String station, String losCNr, String artikelCNr, String cSeriennummer, String cVersion) {
		
		// Menge kommt aus Anzahl der Seriennummern
		String[] seriennrs = cSeriennummer.split(",");
		BigDecimal bdMenge = new BigDecimal(seriennrs.length);

		return bucheLosAblieferungSerienChargennummer(idUser, station, losCNr, artikelCNr, seriennrs, bdMenge, cVersion, true, null);
	}

	public int bucheLosAblieferungChargennummer(String idUser,
			String station, String losCNr, String artikelCNr, String cChargennummer, BigDecimal menge) {
		return bucheLosAblieferungSerienChargennummer(idUser, station, losCNr, artikelCNr, new String[] {cChargennummer}, menge, null, false, null);
	}

	public int bucheLosAblieferungChargennummerSchrott(String idUser,
			String station, String losCNr, String artikelCNr, String cChargennummer, BigDecimal menge, BigDecimal mengeSchrott) {
		if (cChargennummer == null) {
			return bucheLosAblieferungSerienChargennummer(idUser, station, losCNr, artikelCNr, null, menge, null, false, mengeSchrott);
		} else {
			return bucheLosAblieferungSerienChargennummer(idUser, station, losCNr, artikelCNr, new String[] {cChargennummer}, menge, null, false, mengeSchrott);
		}
	}

	private int bucheLosAblieferungSerienChargennummer(String idUser,
			String station, String losCNr, String artikelCNr, String[] cSerienChargennummer, BigDecimal bdMenge, String cVersion, boolean isSeriennummern, BigDecimal mengeSchrott) {
		TheClientDto theClientDto = check(idUser);

		Los los = getLos(losCNr, theClientDto.getMandant());
		int losValidationValue = validateLosBerechtigungen(theClientDto, los, los.getLagerIIdZiel());
		if (losValidationValue < 0) {
			return losValidationValue;
		}

		Integer stuecklisteIId = null;
		if (artikelCNr == null || artikelCNr.length() == 0) {
			stuecklisteIId = los.getStuecklisteIId();
		} else {
			try {
				stuecklisteIId = getStuecklisteIId(artikelCNr,
						theClientDto.getMandant());
			} catch (Exception e1) {
				//
			}
		}
		if (stuecklisteIId == null)
			// Stueckliste nicht vorhanden
			return -5;

		if (!los.getStuecklisteIId().equals(stuecklisteIId))
			// Stueckliste ist nicht im Los
			return -6;

		try {
			LosablieferungDto losablieferungDto = null;
			LosablieferungTerminalDto laDto = new LosablieferungTerminalDto();
			laDto.setLosIId(los.getIId());
			laDto.setNMenge(bdMenge);
			laDto.setMengeSchrott(mengeSchrott);
			ArrayList<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge = new ArrayList<SeriennrChargennrMitMengeDto>();
			if (isSeriennummern) {
				for (int i = 0; i < cSerienChargennummer.length; i++) {
					alSeriennrChargennrMitMenge
							.add(new SeriennrChargennrMitMengeDto(cSerienChargennummer[i]
									.trim(), new BigDecimal(1)));
				}
			} else {
				alSeriennrChargennrMitMenge
				.add(new SeriennrChargennrMitMengeDto(cSerienChargennummer[0].trim(), bdMenge));
			}
			laDto.setSeriennrChargennrMitMenge(alSeriennrChargennrMitMenge);
			losablieferungDto = abliefernLos(theClientDto, los, laDto);

			// PJ21402
			if (losablieferungDto != null && station.startsWith("ZT ") && !theClientDto.getBenutzername().contains("|")) {
				theClientDto.setBenutzername(theClientDto.getBenutzername().trim() + "|" + station);
				getFertigungReportFac().printAblieferEtikettOnServer(
						losablieferungDto.getIId(), null, theClientDto);
			}

			if ((losablieferungDto != null) && (cVersion != null)) {
				// version jetzt nachtragen
				getLagerFac().versionInLagerbewegungUpdaten(
						LocaleFac.BELEGART_LOSABLIEFERUNG,
						losablieferungDto.getIId(), cVersion);
			}
		} catch (EJBExceptionLP e) {
			if (e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN) {
				return -3;
			}
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		return 1;
	}
}
