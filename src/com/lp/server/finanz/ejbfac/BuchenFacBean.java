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
package com.lp.server.finanz.ejbfac;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Kontolaenderart;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Steuerkategoriekonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungDtoAssembler;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.BuchungdetailDtoAssembler;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KassenbuchungsteuerartDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SaldovortragModelBase;
import com.lp.server.finanz.service.SaldovortragModelPersonenKonto;
import com.lp.server.finanz.service.SaldovortragModelSachkonto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.ejb.GeschaeftsjahrMandant;
import com.lp.server.system.ejb.GeschaeftsjahrMandantQuery;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.BelegnummerGeneratorObj;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpSimpleBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BuchenFacBean extends Facade implements BuchenFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Eintragen einer kompletten Buchung in die DB zuerst wird die Buchung auf
	 * ihre Korrektheit geprueft
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param buchungdetailDtos
	 *            BuchungdetailDto[]
	 * @param pruefeBuchungsregeln
	 *            boolean
	 * @param theClientDto aktueller Benutzer
	 * @throws EJBExceptionLP
	 * @return BuchungDto
	 */
	public BuchungDto buchen(BuchungDto buchungDto,
			BuchungdetailDto[] buchungdetailDtos, boolean pruefeBuchungsregeln,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(buchungDto, theClientDto.getIDUser());
		// Pruefen der Buchung
		pruefeBuchung(buchungDto, buchungdetailDtos, pruefeBuchungsregeln);
		// Buchungskopfdaten anlegen
		BuchungDto neueBuchung = createBuchung(buchungDto, theClientDto);
		// Geschaeftsjahrsperre pruefen (erst jetzt da in createBuchung ev. erst
		// das Geschaeftsjahr gesetzt wird
		getSystemFac().pruefeGeschaeftsjahrSperre(
				buchungDto.getIGeschaeftsjahr(), theClientDto.getMandant());
		getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
				buchungDto.getDBuchungsdatum(),
				buchungdetailDtos[0].getKontoIId(), theClientDto);
		// Buchungszeilen anlegen
		for (int i = 0; i < buchungdetailDtos.length; i++) {
			buchungdetailDtos[i].setBuchungIId(neueBuchung.getIId());
			createBuchungdetail(buchungdetailDtos[i], theClientDto);
		}
		buchenAbstimmkonto(buchungDto, buchungdetailDtos, theClientDto);
		return neueBuchung;
	}
	
	private String outPrintBuchungssatz(List<BuchungdetailDto> list) {
		return outPrintBuchungssatz(list, null);
	}
	private String outPrintBuchungssatz(List<BuchungdetailDto> list, PrintStream out) {
		StringBuffer bf = new StringBuffer("\n");
		//_________<-------30-------------------> <-----10-> <-----16------->|<------16------>
		bf.append("Konto                            Kontonr.            SOLL | HABEN\n"); 
		bf.append("__________________________________________________________|________________\n");
		Map<Integer, KontoDto> map = new HashMap<Integer, KontoDto>();
		for(BuchungdetailDto dto : list) {
			Integer id = dto.getKontoIId();
			if(!map.containsKey(id)) {
				try {
					map.put(id, getFinanzFac().kontoFindByPrimaryKey(id));
				} catch(Throwable t) {
				}
			}
			String bez;
			String nr;
			if(map.get(id) == null) {
				bez = id+"";
				nr = "";
			} else {
				bez = map.get(id).getCBez();
				nr = map.get(id).getCNr();
			}
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			df.setGroupingUsed(true);
			bf.append(String.format("%30.30s %10.10s %16s|%16s%n", bez, nr, BuchenFac.SollBuchung.equals(dto.getBuchungdetailartCNr()) ? df.format(dto.getNBetrag()) : "",
					BuchenFac.HabenBuchung.equals(dto.getBuchungdetailartCNr()) ? df.format(dto.getNBetrag()) : ""
					));
		}
		if(out != null)
			out.println(bf.toString());
		return bf.toString();
	}

	public void buchenAbstimmkonto(BuchungDto buchungDto,
			BuchungdetailDto[] buchungdetailDtos, TheClientDto theClientDto) {
		boolean istVersteurer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ISTVERSTEURER,
						theClientDto.getMandant());
		if (!istVersteurer) {
			for (int i = 0; i < buchungdetailDtos.length; i++) {
				Konto konto = em.find(Konto.class,
						buchungdetailDtos[i].getKontoIId());
				if (konto != null) {
					BuchungdetailDto buchungdetailDto = null;
					if (konto.getKontotypCNr().equals(
							FinanzServiceFac.KONTOTYP_DEBITOR)) {
						Steuerkategorie stk = em.find(Steuerkategorie.class,
								konto.getSteuerkategorieIId());
						if (stk == null)
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
									"Keine Steuerkategorie definiert f\u00FCr Konto: "
											+ konto.getCNr());
						if (stk.getKontoIIdForderungen() == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_ABSTIMMKONTO_DEFINIERT,
									"Kein Abstimmkonto definiert f\u00FCr Steuerkategorie: "
											+ stk.getCNr() + " FinanzamtIId: "
											+ stk.getFinanzamtIId());
						} else {
							buchungdetailDto = buchungdetailDtos[i];
							buchungdetailDto.setKontoIId(stk
									.getKontoIIdForderungen());
						}
					} else if (konto.getKontotypCNr().equals(
							FinanzServiceFac.KONTOTYP_KREDITOR)) {
						Steuerkategorie stk = em.find(Steuerkategorie.class,
								konto.getSteuerkategorieIId());
						if (stk == null)
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
									"Keine Steuerkategorie definiert f\u00FCr Konto: "
											+ konto.getCNr());
						if (stk.getKontoIIdVerbindlichkeiten() == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_KEIN_ABSTIMMKONTO_DEFINIERT,
									"Kein Abstimmkonto definiert f\u00FCr Steuerkategorie: "
											+ stk.getCNr() + " FinanzamtIId: "
											+ stk.getFinanzamtIId());
						} else {
							buchungdetailDto = buchungdetailDtos[i];
							buchungdetailDto.setKontoIId(stk
									.getKontoIIdVerbindlichkeiten());
						}
					}
					if (buchungdetailDto != null) {
						createBuchungdetail(buchungdetailDto, theClientDto);
					}
				}
			}
		}
	}

	/**
	 * Kopfdaten einer Buchung anlegen
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BuchungDto
	 */
	private BuchungDto createBuchung(BuchungDto buchungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// Primaerschluessel generieren
		PKGeneratorObj pkGen = getPKGeneratorObj();
		// die id ist ueber alle Mandanten durchgaengig
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BUCHUNG);
		buchungDto.setIId(pk);
		buchungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		buchungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			buchungDto.setIGeschaeftsjahr(getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant(), buchungDto.getDBuchungsdatum()));
			// buchung anlegen
			Buchung buchung = new Buchung(buchungDto.getIId(),
					buchungDto.getBuchungsartCNr(),
					buchungDto.getDBuchungsdatum(), buchungDto.getCText(),
					buchungDto.getKostenstelleIId(),
					buchungDto.getCBelegnummer(),
					buchungDto.getPersonalIIdAnlegen(),
					buchungDto.getPersonalIIdAendern(),
					buchungDto.getIGeschaeftsjahr());
			em.persist(buchung);
			em.flush();
			buchungDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			buchungDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			setBuchungFromBuchungDto(buchung, buchungDto);
			return buchungFindByPrimaryKey(pk);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Loeschen einer Buchung
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeBuchung(BuchungDto buchungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (buchungDto != null) {
			Integer iId = buchungDto.getIId();
			// zugehoerige Einzelbuchungen loeschen
			// try {
			Query query = em
					.createNamedQuery(Buchungdetail.QueryBuchungdetailfindByBuchungIID);
			query.setParameter(1, iId);
			Collection<?> cl = query.getResultList();

			BuchungdetailDto[] buchungen = assembleBuchungdetailDtos(cl);
			// if (buchungen.isEmpty()) {
			// wenns keine gibt, dann passt das auch
			// }
			for (int i = 0; i < buchungen.length; i++) {
				removeBuchungdetail(buchungen[i], theClientDto);
			}
			// }
			// catch (FinderException ex1) {
			// wenns keine gibt, dann passt das auch
			// }
			// try {
			Buchung toRemove = em.find(Buchung.class, iId);
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

	public void updateBuchung(BuchungDto buchungDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(buchungDto);
		if (buchungDto != null) {
			Integer iId = buchungDto.getIId();
			Buchung buchung = em.find(Buchung.class, iId);
			if (buchung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setBuchungFromBuchungDto(buchung, buchungDto);
		}
	}

	public BuchungDto buchungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// myLogger.logData(iId);
		Buchung buchung = em.find(Buchung.class, iId);
		if (buchung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleBuchungDto(buchung);
	}

	private void setBuchungFromBuchungDto(Buchung buchung, BuchungDto buchungDto) {
		buchung.setBuchungsartCNr(buchungDto.getBuchungsartCNr());
		buchung.setTBuchungsdatum(buchungDto.getDBuchungsdatum());
		buchung.setCText(buchungDto.getCText());
		buchung.setKostenstelleIId(buchungDto.getKostenstelleIId());
		buchung.setCBelegnummer(buchungDto.getCBelegnummer());
		buchung.setTAnlegen(buchungDto.getTAnlegen());
		buchung.setPersonalIIdAnlegen(buchungDto.getPersonalIIdAnlegen());
		buchung.setTAendern(buchungDto.getTAendern());
		buchung.setPersonalIIdAendern(buchungDto.getPersonalIIdAendern());
		buchung.setGeschaeftsjahr(buchungDto.getIGeschaeftsjahr());
		buchung.setTStorniert(buchungDto.getTStorniert());
		buchung.setPersonalIIdStorniert(buchungDto.getPersonalIIdStorniert());
		buchung.setBelegartCNr(buchungDto.getBelegartCNr());
		buchung.setbAutomatischeBuchung(buchungDto.getbAutomatischeBuchung());
		buchung.setbAutomatischeBuchungEB(buchungDto
				.getbAutomatischeBuchungEB());

		// TODO: Temporaerer Workaround, da bautombuchung bisher nicht gefuellt
		// wurde, nun aber not-null sein muss
		if (null == buchung.getbAutomatischeBuchung()) {
			buchungDto.setbAutomatischeBuchung((short) 0);
			buchung.setbAutomatischeBuchung(buchungDto
					.getbAutomatischeBuchung());
		}
		if (null == buchung.getbAutomatischeBuchungEB()) {
			buchungDto.setbAutomatischeBuchungEB((short) 0);
			buchung.setbAutomatischeBuchungEB(buchungDto
					.getbAutomatischeBuchungEB());
		}
		em.merge(buchung);
		em.flush();
	}

	private BuchungDto assembleBuchungDto(Buchung buchung) {
		return BuchungDtoAssembler.createDto(buchung);
	}

	private BuchungDto[] assembleBuchungDtos(Collection<?> buchungs) {
		List<BuchungDto> list = new ArrayList<BuchungDto>();
		if (buchungs != null) {
			Iterator<?> iterator = buchungs.iterator();
			while (iterator.hasNext()) {
				Buchung buchung = (Buchung) iterator.next();
				list.add(assembleBuchungDto(buchung));
			}
		}
		BuchungDto[] returnArray = new BuchungDto[list.size()];
		return (BuchungDto[]) list.toArray(returnArray);
	}

	/**
	 * Eine neue Buchungszeile erstellen
	 * 
	 * @param buchungdetailDto
	 *            BuchungdetailDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	private void createBuchungdetail(BuchungdetailDto buchungdetailDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// Primaerschluessel generieren
		Integer pk = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BUCHUNGDETAIL);
		buchungdetailDto.setIId(pk);
		// buchung anlegen
		buchungdetailDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		buchungdetailDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Buchungdetail buchungdetail = new Buchungdetail(
					buchungdetailDto.getIId(),
					buchungdetailDto.getBuchungIId(),
					buchungdetailDto.getKontoIId(),
					buchungdetailDto.getNBetrag(),
					buchungdetailDto.getBuchungdetailartCNr(),
					buchungdetailDto.getNUst(),
					buchungdetailDto.getPersonalIIdAnlegen(),
					buchungdetailDto.getPersonalIIdAendern());
			buchungdetail.setKommentar(buchungdetailDto.getKommentar());
			em.persist(buchungdetail);
			em.flush();
			buchungdetailDto.setKommentar(buchungdetail.getKommentar());
			buchungdetailDto.setTAendern(buchungdetail.getTAendern());
			buchungdetailDto.setTAnlegen(buchungdetail.getTAnlegen());
			setBuchungdetailFromBuchungdetailDto(buchungdetail,
					buchungdetailDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/**
	 * Eine Einzelbuchung loeschen
	 * 
	 * @param buchungdetailDto
	 *            BuchungdetailDto
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeBuchungdetail(BuchungdetailDto buchungdetailDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(buchungdetailDto);
		// try {
		if (buchungdetailDto != null) {
			Integer iId = buchungdetailDto.getIId();
			Buchungdetail toRemove = em.find(Buchungdetail.class, iId);
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
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void updateBuchungdetail(BuchungdetailDto buchungdetailDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(buchungdetailDto);
		if (buchungdetailDto != null) {
			Integer iId = buchungdetailDto.getIId();
			// try {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iId);
			if (buchungdetail == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setBuchungdetailFromBuchungdetailDto(buchungdetail,
					buchungdetailDto);
			// }
			// catch (FinderException e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			// }
		}
	}

	/**
	 * Detailbuchung anhand PK finden
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BuchungdetailDto
	 */
	public BuchungdetailDto buchungdetailFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Buchungdetail buchungdetail = em.find(Buchungdetail.class, iId);
		if (buchungdetail == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBuchungdetailDto(buchungdetail);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Alle Buchungen eines Kontos finden.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BuchungdetailDto
	 */
	public BuchungdetailDto[] buchungdetailFindByKontoIId(Integer kontoIId)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("BuchungdetailfindByKontoIId");
		query.setParameter(1, kontoIId);
		Collection<?> cl = query.getResultList();
		return assembleBuchungdetailDtos(cl);
	}

	/**
	 * Pruefen, ob belege eines Partners auf ein bestimmtes Konto gebucht
	 * wurden.
	 * 
	 * @param partnerIId
	 *            Integer
	 * @param kontoIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BuchungdetailDto
	 */
	public Boolean hatPartnerBuchungenAufKonto(Integer partnerIId,
			Integer kontoIId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			KundeDto kundeDto = getKundeFac()
					.kundeFindByiIdPartnercNrMandantOhneExc(partnerIId,
							theClientDto.getMandant(), theClientDto);
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByiIdPartnercNrMandantOhneExc(partnerIId,
							theClientDto.getMandant(), theClientDto);
			Boolean bResult = Boolean.FALSE;

			Query query = em.createNamedQuery("BuchungdetailfindByKontoIId");
			query.setParameter(1, kontoIId);
			Collection<?> cl = query.getResultList();
			BuchungdetailDto[] buchungen = assembleBuchungdetailDtos(cl);
			for (int i = 0; i < buchungen.length; i++) {
				BuchungDto buchungDto = buchungFindByPrimaryKey(buchungen[i]
						.getBuchungIId());
				if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_BUCHUNG)
						&& buchungDto.getBelegartCNr() != null
						&& buchungDto.getBelegartCNr().equals(
								LocaleFac.BELEGART_RECHNUNG)) {
					BelegbuchungDto brDto = getBelegbuchungFac(
							theClientDto.getMandant())
							.belegbuchungFindByBuchungIIdOhneExc(
									buchungDto.getIId());
					if (brDto != null
							&& brDto.getBelegartCNr().equals(
									LocaleFac.BELEGART_RECHNUNG)) {
						RechnungDto rechnungDto = getRechnungFac()
								.rechnungFindByPrimaryKeyOhneExc(
										brDto.getIBelegiid());
						if (kundeDto != null
								&& rechnungDto.getKundeIId().equals(
										kundeDto.getIId())) {
							bResult = Boolean.TRUE;
							break;
						}
					}
				} else if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_BANKBUCHUNG)) {

				} else if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_BUCHUNG)
						&& buchungDto.getBelegartCNr() != null
						&& buchungDto.getBelegartCNr().equals(
								LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					BelegbuchungDto brDto = getBelegbuchungFac(
							theClientDto.getMandant())
							.belegbuchungFindByBuchungIIdOhneExc(
									buchungDto.getIId());
					if (brDto != null
							&& brDto.getBelegartCNr().equals(
									LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
						EingangsrechnungDto erDto = getEingangsrechnungFac()
								.eingangsrechnungFindByPrimaryKeyOhneExc(
										brDto.getIBelegiid());
						if (lieferantDto != null
								&& erDto.getLieferantIId().equals(
										lieferantDto.getIId())) {
							bResult = Boolean.TRUE;
							break;
						}
					}
				} else if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_BUCHUNG)
						&& buchungDto.getBelegartCNr() != null
						&& buchungDto.getBelegartCNr().equals(
								LocaleFac.BELEGART_GUTSCHRIFT)) {
					BelegbuchungDto brDto = getBelegbuchungFac(
							theClientDto.getMandant())
							.belegbuchungFindByBuchungIIdOhneExc(
									buchungDto.getIId());
					if (brDto != null
							&& brDto.getBelegartCNr().equals(
									LocaleFac.BELEGART_GUTSCHRIFT)) {
						RechnungDto rechnungDto = getRechnungFac()
								.rechnungFindByPrimaryKeyOhneExc(
										brDto.getIBelegiid());
						if (kundeDto != null
								&& rechnungDto.getKundeIId().equals(
										kundeDto.getIId())) {
							bResult = Boolean.TRUE;
							break;
						}
					}
				} else if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_KASSENBUCHUNG)) {

				} else if (buchungDto.getBuchungsartCNr().equals(
						FinanzFac.BUCHUNGSART_UMBUCHUNG)) {

				}
			}
			return bResult;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void setBuchungdetailFromBuchungdetailDto(
			Buchungdetail buchungdetail, BuchungdetailDto buchungdetailDto) {
		buchungdetail.setBuchungIId(buchungdetailDto.getBuchungIId());
		buchungdetail.setKontoIId(buchungdetailDto.getKontoIId());
		buchungdetail.setKontoIIdGegenkonto(buchungdetailDto
				.getKontoIIdGegenkonto());
		buchungdetail.setNBetrag(buchungdetailDto.getNBetrag());
		buchungdetail.setBuchungdetailartCNr(buchungdetailDto
				.getBuchungdetailartCNr());
		buchungdetail.setNUst(buchungdetailDto.getNUst());
		buchungdetail.setTAnlegen(buchungdetailDto.getTAnlegen());
		buchungdetail.setPersonalIIdAnlegen(buchungdetailDto
				.getPersonalIIdAnlegen());
		buchungdetail.setTAendern(buchungdetailDto.getTAendern());
		buchungdetail.setPersonalIIdAendern(buchungdetailDto
				.getPersonalIIdAendern());
		buchungdetail.setIAuszug(buchungdetailDto.getIAuszug());
		buchungdetail.setIAusziffern(buchungdetailDto.getIAusziffern());
		buchungdetail.setKommentar(buchungdetail.getKommentar());
		em.merge(buchungdetail);
		em.flush();
	}

	private BuchungdetailDto assembleBuchungdetailDto(
			Buchungdetail buchungdetail) {
		return BuchungdetailDtoAssembler.createDto(buchungdetail);
	}

	private BuchungdetailDto[] assembleBuchungdetailDtos(
			Collection<?> buchungdetails) {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		if (buchungdetails != null) {
			Iterator<?> iterator = buchungdetails.iterator();
			while (iterator.hasNext()) {
				Buchungdetail buchungdetail = (Buchungdetail) iterator.next();
				list.add(assembleBuchungdetailDto(buchungdetail));
			}
		}

		BuchungdetailDto[] returnArray = new BuchungdetailDto[list.size()];
		return (BuchungdetailDto[]) list.toArray(returnArray);
	}

	// <<<<<<< BuchenFacBean.java
	// public BuchungsregelDto createBuchungsregel(
	// BuchungsregelDto buchungsregelDto, TheClientDto theClientDto)
	// throws EJBExceptionLP {
	// Integer iId = getPKGeneratorObj().getNextPrimaryKey(
	// PKConst.PK_BUCHUNGSREGEL);
	// buchungsregelDto.setIId(iId);
	// buchungsregelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
	// buchungsregelDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
	// try {
	// Buchungsregel buchungsregel = new Buchungsregel(
	// buchungsregelDto.getIId(),
	// buchungsregelDto.getMandantCNr(),
	// buchungsregelDto.getCBez(),
	// buchungsregelDto.getFinanzamtIId(),
	// buchungsregelDto.getBuchungsregelschieneCNr(),
	// buchungsregelDto.getLaenderartCNr(),
	// buchungsregelDto.getDGueltigVon(),
	// buchungsregelDto.getKontoIIdSkonto(),
	// buchungsregelDto.getKontoIIdSkontoAbzug(),
	// buchungsregelDto.getKontoIIdDefault(),
	// buchungsregelDto.getPersonalIIdAnlegen(),
	// buchungsregelDto.getPersonalIIdAendern());
	// em.persist(buchungsregel);
	// em.flush();
	// buchungsregelDto.setTAendern(buchungsregel.getTAendern());
	// buchungsregelDto.setTAnlegen(buchungsregel.getTAnlegen());
	// setBuchungsregelFromBuchungsregelDto(buchungsregel,
	// buchungsregelDto);
	// return buchungsregelDto;
	// } catch (EntityExistsException e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
	// }
	// }
	//
	// public void removeBuchungsregel(BuchungsregelDto buchungsregelDto,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// if (buchungsregelDto != null) {
	// Integer iId = buchungsregelDto.getIId();
	// // try {
	// Buchungsregel toRemove = em.find(Buchungsregel.class, iId);
	// if (toRemove == null) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	// }
	// try {
	// em.remove(toRemove);
	// em.flush();
	// } catch (EntityExistsException er) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
	// er);
	// }
	// // }
	// // catch (RemoveException ex) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
	// // ex);
	// // }
	// }
	// }
	//
	// public BuchungsregelDto updateBuchungsregel(
	// BuchungsregelDto buchungsregelDto, TheClientDto theClientDto)
	// throws EJBExceptionLP {
	// if (buchungsregelDto != null) {
	// buchungsregelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
	// buchungsregelDto.setTAendern(super.getTimestamp());
	//
	// Integer iId = buchungsregelDto.getIId();
	// Buchungsregel buchungsregel = em.find(Buchungsregel.class, iId);
	// if (buchungsregel == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
	// }
	// setBuchungsregelFromBuchungsregelDto(buchungsregel,
	// buchungsregelDto);
	// return buchungsregelDto;
	// } else {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, "");
	// }
	// }
	//
	// public BuchungsregelDto buchungsregelFindByPrimaryKey(Integer iId)
	// throws EJBExceptionLP {
	// Buchungsregel buchungsregel = em.find(Buchungsregel.class, iId);
	// if (buchungsregel == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
	// }
	// return assembleBuchungsregelDto(buchungsregel);
	// }
	//
	// private void setBuchungsregelFromBuchungsregelDto(
	// Buchungsregel buchungsregel, BuchungsregelDto buchungsregelDto) {
	// buchungsregel.setMandantCNr(buchungsregelDto.getMandantCNr());
	// buchungsregel.setCBez(buchungsregelDto.getCBez());
	// buchungsregel.setFinanzamtIId(buchungsregelDto.getFinanzamtIId());
	// buchungsregel.setBuchungsregelschieneCNr(buchungsregelDto
	// .getBuchungsregelschieneCNr());
	// buchungsregel.setLaenderartCNr(buchungsregelDto.getLaenderartCNr());
	// buchungsregel.setTGueltigvon(buchungsregelDto.getDGueltigVon());
	// buchungsregel.setTGueltigbis(buchungsregelDto.getDGueltigBis());
	// buchungsregel.setKontoIIdSkonto(buchungsregelDto.getKontoIIdSkonto());
	// buchungsregel.setKontoIIdSkontoabzug(buchungsregelDto
	// .getKontoIIdSkontoAbzug());
	// buchungsregel.setKontoIIdDefault(buchungsregelDto.getKontoIIdDefault());
	// buchungsregel.setTAnlegen(buchungsregelDto.getTAnlegen());
	// buchungsregel.setPersonalIIdAnlegen(buchungsregelDto
	// .getPersonalIIdAnlegen());
	// buchungsregel.setTAendern(buchungsregelDto.getTAendern());
	// buchungsregel.setPersonalIIdAendern(buchungsregelDto
	// .getPersonalIIdAendern());
	// em.merge(buchungsregel);
	// em.flush();
	// }
	//
	// private BuchungsregelDto assembleBuchungsregelDto(
	// Buchungsregel buchungsregel) {
	// return BuchungsregelDtoAssembler.createDto(buchungsregel);
	// }
	//
	// private BuchungsregelDto[] assembleBuchungsregelDtos(
	// Collection<?> buchungsregels) {
	// List<BuchungsregelDto> list = new ArrayList<BuchungsregelDto>();
	// if (buchungsregels != null) {
	// Iterator<?> iterator = buchungsregels.iterator();
	// while (iterator.hasNext()) {
	// Buchungsregel buchungsregel = (Buchungsregel) iterator.next();
	// list.add(assembleBuchungsregelDto(buchungsregel));
	// }
	// }
	// BuchungsregelDto[] returnArray = new BuchungsregelDto[list.size()];
	// return (BuchungsregelDto[]) list.toArray(returnArray);
	// }
	//
	// public BuchungsregelGegenkontoDto createBuchungsregelGegenkonto(
	// BuchungsregelGegenkontoDto buchungsregelGegenkontoDto,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// Integer iId = getPKGeneratorObj().getNextPrimaryKey(
	// PKConst.PK_BUCHUNGSREGELGEGENKONTO);
	// buchungsregelGegenkontoDto.setIId(iId);
	// buchungsregelGegenkontoDto.setPersonalIIdAendern(theClientDto
	// .getIDPersonal());
	// buchungsregelGegenkontoDto.setPersonalIIdAnlegen(theClientDto
	// .getIDPersonal());
	// try {
	// Buchungsregelgegenkonto buchungsregelGegenkonto = new
	// Buchungsregelgegenkonto(
	// buchungsregelGegenkontoDto.getIId(),
	// buchungsregelGegenkontoDto.getBuchungsregelIId(),
	// buchungsregelGegenkontoDto.getKontoIIdVon(),
	// buchungsregelGegenkontoDto.getKontoIIdBis(),
	// buchungsregelGegenkontoDto.getPersonalIIdAnlegen(),
	// buchungsregelGegenkontoDto.getPersonalIIdAendern());
	// em.persist(buchungsregelGegenkonto);
	// em.flush();
	// buchungsregelGegenkontoDto.setTAendern(buchungsregelGegenkonto
	// .getTAendern());
	// buchungsregelGegenkontoDto.setTAnlegen(buchungsregelGegenkonto
	// .getTAnlegen());
	// setBuchungsregelGegenkontoFromBuchungsregelGegenkontoDto(
	// buchungsregelGegenkonto, buchungsregelGegenkontoDto);
	// return buchungsregelGegenkontoDto;
	// } catch (EntityExistsException e) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
	// }
	// }
	//
	// public void removeBuchungsregelGegenkonto(
	// BuchungsregelGegenkontoDto buchungsregelGegenkontoDto,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// if (buchungsregelGegenkontoDto != null) {
	// Integer iId = buchungsregelGegenkontoDto.getIId();
	// // try {
	// Buchungsregelgegenkonto toRemove = em.find(
	// Buchungsregelgegenkonto.class, iId);
	// if (toRemove == null) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	// }
	// try {
	// em.remove(toRemove);
	// em.flush();
	// } catch (EntityExistsException er) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
	// er);
	// }
	// // }
	// // catch (RemoveException ex) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
	// // ex);
	// // }
	// }
	// }
	//
	// public BuchungsregelGegenkontoDto updateBuchungsregelGegenkonto(
	// BuchungsregelGegenkontoDto buchungsregelGegenkontoDto,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// if (buchungsregelGegenkontoDto != null) {
	// buchungsregelGegenkontoDto.setPersonalIIdAendern(theClientDto
	// .getIDPersonal());
	// buchungsregelGegenkontoDto.setTAendern(super.getTimestamp());
	//
	// Integer iId = buchungsregelGegenkontoDto.getIId();
	// Buchungsregelgegenkonto buchungsregelGegenkonto = em.find(
	// Buchungsregelgegenkonto.class, iId);
	// if (buchungsregelGegenkonto == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
	// }
	// setBuchungsregelGegenkontoFromBuchungsregelGegenkontoDto(
	// buchungsregelGegenkonto, buchungsregelGegenkontoDto);
	// return buchungsregelGegenkontoDto;
	// } else {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, "");
	// }
	// }
	//
	// public BuchungsregelGegenkontoDto
	// buchungsregelGegenkontoFindByPrimaryKey(
	// Integer iId) throws EJBExceptionLP {
	// Buchungsregelgegenkonto buchungsregelgegenkonto = em.find(
	// Buchungsregelgegenkonto.class, iId);
	// if (buchungsregelgegenkonto == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
	// }
	// return assembleBuchungsregelGegenkontoDto(buchungsregelgegenkonto);
	// }
	//
	// private void setBuchungsregelGegenkontoFromBuchungsregelGegenkontoDto(
	// Buchungsregelgegenkonto buchungsregelGegenkonto,
	// BuchungsregelGegenkontoDto buchungsregelGegenkontoDto) {
	// buchungsregelGegenkonto.setBuchungsregelIId(buchungsregelGegenkontoDto
	// .getBuchungsregelIId());
	// buchungsregelGegenkonto.setKontoIIdVon(buchungsregelGegenkontoDto
	// .getKontoIIdVon());
	// buchungsregelGegenkonto.setKontoIIdBis(buchungsregelGegenkontoDto
	// .getKontoIIdBis());
	// buchungsregelGegenkonto.setTAnlegen(buchungsregelGegenkontoDto
	// .getTAnlegen());
	// buchungsregelGegenkonto
	// .setPersonalIIdAnlegen(buchungsregelGegenkontoDto
	// .getPersonalIIdAnlegen());
	// buchungsregelGegenkonto.setTAendern(buchungsregelGegenkontoDto
	// .getTAendern());
	// buchungsregelGegenkonto
	// .setPersonalIIdAendern(buchungsregelGegenkontoDto
	// .getPersonalIIdAendern());
	// em.merge(buchungsregelGegenkonto);
	// em.flush();
	// }
	//
	// private BuchungsregelGegenkontoDto assembleBuchungsregelGegenkontoDto(
	// Buchungsregelgegenkonto buchungsregelGegenkonto) {
	// return BuchungsregelGegenkontoDtoAssembler
	// .createDto(buchungsregelGegenkonto);
	// }
	//
	// private BuchungsregelGegenkontoDto[] assembleBuchungsregelGegenkontoDtos(
	// Collection<?> buchungsregelGegenkontos) {
	// List<BuchungsregelGegenkontoDto> list = new
	// ArrayList<BuchungsregelGegenkontoDto>();
	// if (buchungsregelGegenkontos != null) {
	// Iterator<?> iterator = buchungsregelGegenkontos.iterator();
	// while (iterator.hasNext()) {
	// Buchungsregelgegenkonto buchungsregelGegenkonto =
	// (Buchungsregelgegenkonto) iterator
	// .next();
	// list.add(assembleBuchungsregelGegenkontoDto(buchungsregelGegenkonto));
	// }
	// }
	// BuchungsregelGegenkontoDto[] returnArray = new
	// BuchungsregelGegenkontoDto[list
	// .size()];
	// return (BuchungsregelGegenkontoDto[]) list.toArray(returnArray);
	// }
	//
	// =======
	// >>>>>>> 1.52

	public void storniereBuchung(Integer buchungIId, TheClientDto theClientDto) {
		BelegbuchungDto brDto = getBelegbuchungFac(theClientDto.getMandant())
				.belegbuchungFindByBuchungIIdOhneExc(buchungIId);
		if (brDto != null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_STORNIEREN_NICHT_MOEGLICH,
					new Exception(
							"EJBExceptionLP.FEHLER_FINANZ_STORNIEREN_NICHT_MOEGLICH"));

		}

		Buchung buchung = em.find(Buchung.class, buchungIId);
		BuchungdetailDto[] buchungdetails = buchungdetailsFindByBuchungIId(buchungIId);
		
		getSystemFac().pruefeGeschaeftsjahrSperre(buchung.getGeschaeftsjahr(),
				theClientDto.getMandant());
		getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(buchung.getTBuchungsdatum(), buchungdetails[0].getKontoIId(), theClientDto);
		
		if (buchung.getTStorniert() == null) {
			buchung.setTStorniert(new Timestamp(System.currentTimeMillis()));
			buchung.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			em.merge(buchung);
			removeAuszifferungen(buchung.getIId(), theClientDto);
			em.flush();
		}
		// eine Stornobuchung mit "- Betraegen" erzeugen gleich der vorigen
		BuchungDto buchungDto = assembleBuchungDto(buchung);
		buchungDto.setIId(null);
		buchungDto = createBuchung(buchungDto, theClientDto);
		for (int i = 0; i < buchungdetails.length; i++) {
			// neue Details mit negativem Vorzeichen
			buchungdetails[i].setIId(null);
			buchungdetails[i].setBuchungIId(buchungDto.getIId());
			buchungdetails[i].setNBetrag(buchungdetails[i].getNBetrag()
					.negate());
			buchungdetails[i].setNUst(buchungdetails[i].getNUst().negate());
			createBuchungdetail(buchungdetails[i], theClientDto);
		}
	}

	private void removeAuszifferungen(Integer buchungIId,
			TheClientDto theClientDto) {
		BuchungdetailDto[] buchungdetailDtos = buchungdetailsFindByBuchungIId(buchungIId);
		for (int i = 0; i < buchungdetailDtos.length; i++) {
			buchungdetailDtos[i].setIAusziffern(null);
			updateBuchungdetail(buchungdetailDtos[i], theClientDto);
		}
	}

	/**
	 * Eine Belegnummer fuer die Umbuchung generieren.
	 * 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	public String getBelegnummerUmbuchung(Integer geschaeftsjahr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BelegnummerGeneratorObj bnGen = new BelegnummerGeneratorObj();
		LpBelegnummer b = bnGen.getNextBelegNrFinanz(geschaeftsjahr,
				PKConst.PK_UMBUCHUNG, theClientDto.getMandant(), theClientDto);
		LpBelegnummerFormat f = new LpSimpleBelegnummerFormat(10);
		return f.format(b);
	}

	public String getBelegnummerKassenbuch(Integer geschaeftsjahr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BelegnummerGeneratorObj bnGen = new BelegnummerGeneratorObj();
		LpBelegnummer b = bnGen.getNextBelegNrFinanz(geschaeftsjahr,
				PKConst.PK_BUCHUNGENKASSABUCH, theClientDto.getMandant(),
				theClientDto);
		LpBelegnummerFormat f = new LpSimpleBelegnummerFormat(8);
		return "KB" + f.format(b);
	}

	/**
	 * Liste der Gegenkonten einer Buchung
	 * 
	 * @param buchungdetailIId IId des Buchungsdetails 
	 * @param theClientDto aktueller Benutzer
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */

	public Map getListeDerGegenkonten(Integer buchungdetailIId,
			TheClientDto theClientDto) {
		Map m = new TreeMap();

		BuchungdetailDto buchungdetailDto = buchungdetailFindByPrimaryKey(buchungdetailIId);
		try {
			BuchungdetailDto[] buchungen = buchungdetailsFindByBuchungIId(buchungdetailDto
					.getBuchungIId());
			for (int i = 0; i < buchungen.length; i++) {
				if (!buchungen[i].getIId().equals(buchungdetailIId)) {

					KontoDto ktoDto = getFinanzFac().kontoFindByPrimaryKey(
							buchungen[i].getKontoIId());

					String betrag = "";

					int lengthBetrag = 13;

					if (buchungen[i].getBuchungdetailartCNr().equals(
							BuchenFac.SollBuchung)) {

						betrag += Helper.fitString2LengthAlignRight(Helper
						// .formatBetrag(buchungen[i].getNBetrag(),
						// theClientDto.getLocUi()), lengthBetrag, ' ');
								.formatZahl(buchungen[i].getNBetrag(),
										FinanzFac.NACHKOMMASTELLEN,
										theClientDto.getLocUi()), lengthBetrag,
								' ');
					} else {
						betrag += Helper
								.fitString2Length("", lengthBetrag, ' ')
								+ Helper.fitString2LengthAlignRight(Helper
								// .formatBetrag(buchungen[i].getNBetrag(),
								// theClientDto.getLocUi()), lengthBetrag,' ');
										.formatZahl(buchungen[i].getNBetrag(),
												FinanzFac.NACHKOMMASTELLEN,
												theClientDto.getLocUi()),
										lengthBetrag, ' ');
					}

					m.put(buchungen[i].getIId(),
							Helper.fitString2Length(ktoDto.getCNr(), 15, ' ')
									+ Helper.fitString2Length(
											ktoDto.getKontotypCNr(), 1, ' ')
									+ " "
									+ Helper.fitString2Length(ktoDto.getCBez(),
											30, ' ')
									+ Helper.fitString2Length(
											ktoDto.getPartnerKurzbezeichnung(),
											5, ' ') + betrag);

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return m;
	}

	public BigDecimal getSaldoVonKontoMitEB(Integer kontoIId,
			int geschaftsjahr, int periode, TheClientDto theClientDto) {
		return getSaldoOhneEBVonKonto(kontoIId, geschaftsjahr, periode,
				theClientDto)
		// getSollVonKonto(kontoIId, geschaftsjahr, periode, true,
		// theClientDto).subtract(
		// getHabenVonKonto(kontoIId, geschaftsjahr, periode, true,
		// theClientDto))
				.add(getSummeEroeffnungKontoIId(kontoIId, geschaftsjahr,
						periode, true, theClientDto));
	}

	public BigDecimal getHabenVonKonto(Integer kontoIId, int geschaftsjahr,
			int periode, TheClientDto theClientDto) {
		return getHabenVonKonto(kontoIId, geschaftsjahr, periode, false,
				theClientDto);
	}

	public BigDecimal getHabenVonKonto(Integer kontoIId, int geschaftsjahr,
			int periode, boolean kummuliert, TheClientDto theClientDto) {
		return getSummeVonKonto(kontoIId, geschaftsjahr, periode,
				BuchenFac.HabenBuchung, null, kummuliert, false, theClientDto);
	}

	public BigDecimal getHabenVonKontoInWaehrung(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getSummeVonKontoInWaehrung(kontoIId, geschaftsjahr, periode,
				BuchenFac.HabenBuchung, null, kummuliert, false, waehrungCNr,
				theClientDto);
	}

	/**
	 * Den Soll-Wert eines Kontos berechnen.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getSollVonKonto(Integer kontoIId, int geschaftsjahr,
			int periode, TheClientDto theClientDto) {
		return getSollVonKonto(kontoIId, geschaftsjahr, periode, false,
				theClientDto);
	}

	public BigDecimal getSollVonKonto(Integer kontoIId, int geschaftsjahr,
			int periode, boolean kummuliert, TheClientDto theClientDto) {
		return getSummeVonKonto(kontoIId, geschaftsjahr, periode,
				BuchenFac.SollBuchung, null, kummuliert, false, theClientDto);
	}

	public BigDecimal getSollVonKontoInWaehrung(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getSummeVonKontoInWaehrung(kontoIId, geschaftsjahr, periode,
				BuchenFac.SollBuchung, null, kummuliert, false, waehrungCNr,
				theClientDto);
	}

	private BigDecimal getSummeVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, String buchungsartCNr, Integer iAuszug,
			boolean kummuliert, boolean inklEB, TheClientDto theClientDto) {
		return getSummeVonKonto(kontoIId, geschaeftsjahr, periode,
				buchungsartCNr, iAuszug, null, kummuliert, inklEB, theClientDto);
	}

	private BigDecimal getSummeVonKonto(Integer kontoIId, int geschaeftsjahr,
			String buchungsartCNr, Integer iAuszifferkennzeichen,
			TheClientDto theClientDto) {
		// periode auf -1 = ganzes Geschaeftsjahr
		// SP 2013/01007 bei Ausziffern inkl EB!
		return getSummeVonKonto(kontoIId, geschaeftsjahr, -1, buchungsartCNr,
				null, iAuszifferkennzeichen, false, true, theClientDto);
	}

	/**
	 * 
	 * @param kontoIId
	 *            I_ID des Kontos
	 * @param geschaeftsjahr
	 *            Gesch&auml;ftsjahr der Buchungen
	 * @param periode
	 *            Periode des Gesch&auml;ftsjahrs (Anmerkung: != Kalendermonat wenn
	 *            das GJ nicht im J&auml;nner beginnt)
	 * @param buchungsartCNr
	 *            SOLL / HABEN
	 * @param iAuszug
	 *            Filter f&uuml;r Auszug bis
	 * @param iAuszifferkennzeichen
	 *            Filter f&uuml;r Auszifferkennzeichen
	 * @param kummuliert
	 *            kummulierte Summe &uuml;ber alle Perioden bis inkl. Periode
	 * @param inklEB
	 *            inkl. der Er&ouml;ffungsbuchungen (eigentlich nur bei nach Auszug)
	 * @return Summe des Betrags der gew&auml;hlten Buchungen
	 * 
	 *         Anmerkung: Auszug und Auszifferkennzeichen werden nicht gemeinsam
	 *         benutzt bei Auszifferkennzeichen gibt es kein "kummuliert" und
	 *         kein "inklEB"
	 */
	private BigDecimal getSummeVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, String buchungsartCNr, Integer iAuszug,
			Integer iAuszifferkennzeichen, boolean kummuliert, boolean inklEB,
			TheClientDto theClientDto) {

		Timestamp[] tVonBis;
		Timestamp tBeginn = null;
		Timestamp tEnd = null;
		if (kummuliert) {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
			tBeginn = tVonBis[0];
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tEnd = tVonBis[1];
		} else {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tBeginn = tVonBis[0];
			tEnd = tVonBis[1];
		}

		BigDecimal summe = new BigDecimal(0);
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT sum(n_betrag) FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id="
					+ kontoIId
					+ " AND o.buchungdetailart_c_nr='"
					+ buchungsartCNr
					+ "'"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			if (!inklEB) {
				queryString += " AND NOT o.flrbuchung.buchungsart_c_nr='"
						+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' ";
				queryString += " AND NOT o.flrbuchung.b_autombuchungeb=1";
			}
			if (iAuszug != null)
				if (kummuliert)
					if (!inklEB)
						queryString += " AND NOT o.i_auszug IS NULL AND o.i_auszug <="
								+ iAuszug.intValue();
					else
						queryString += " AND (o.flrbuchung.buchungsart_c_nr='"
								+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' "
								+ " OR o.flrbuchung.b_autombuchungeb=1 "
								+ " OR ("
								+ " NOT o.i_auszug IS NULL AND o.i_auszug <="
								+ iAuszug.intValue() + "))";
				else if (!inklEB)
					queryString += " AND NOT o.i_auszug IS NULL AND o.i_auszug ="
							+ iAuszug.intValue();
				else
					queryString += " AND (o.flrbuchung.buchungsart_c_nr='"
							+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' "
							+ " OR o.flrbuchung.b_autombuchungeb=1 OR ("
							+ " NOT o.i_auszug IS NULL AND o.i_auszug ="
							+ iAuszug.intValue() + "))";

			if (iAuszifferkennzeichen != null) {
				queryString += " AND o.i_ausziffern ="
						+ iAuszifferkennzeichen.intValue()
						+ " OR (o.flrbuchung.b_autombuchungeb=1 AND o.i_ausziffern = "
						+ iAuszifferkennzeichen.intValue() + ")";
			}

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("pVon", tBeginn);
			query.setParameter("pEnd", tEnd);
			List<?> results = query.list();
			if (results.size() != 0)
				if (results.get(0) != null)
					summe = (BigDecimal) results.get(0);
		} finally {
			if (session != null)
				session.close();
		}
		return summe;
	}

	/**
	 * 
	 * @param kontoIId
	 *            I_ID des Kontos
	 * @param geschaeftsjahr
	 *            Gesch&auml;ftsjahr der Buchungen
	 * @param periode
	 *            Periode des Gesch&auml;ftsjahrs (Anmerkung: != Kalendermonat wenn
	 *            das GJ nicht im J&auml;nner beginnt)
	 * @param buchungsartCNr
	 *            SOLL / HABEN
	 * @param iAuszug
	 *            Filter f&uuml;r Auszug bis
	 * @param kummuliert
	 *            kummulierte Summe &uuml;ber alle Perioden bis inkl. Periode
	 * @param inklEB
	 *            inkl. der Er&ouml;ffungsbuchungen (eigentlich nur bei nach Auszug)
	 * @param waehrungCNr
	 *            W&auml;hrung in die umgerechnet wird (datumsrichtig)
	 * @return Summe des Betrags der gew&auml;hlten Buchungen
	 * 
	 *         Anmerkung: Berechnung kann etwas dauern, da f&uuml;r jede Buchung der
	 *         Kurs bestimmt wird
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private BigDecimal getSummeVonKontoInWaehrung(Integer kontoIId,
			int geschaeftsjahr, int periode, String buchungsartCNr,
			Integer iAuszug, boolean kummuliert, boolean inklEB,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis;
		Timestamp tBeginn = null;
		Timestamp tEnd = null;
		if (kummuliert) {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
			tBeginn = tVonBis[0];
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tEnd = tVonBis[1];
		} else {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tBeginn = tVonBis[0];
			tEnd = tVonBis[1];
		}

		BigDecimal summe = new BigDecimal(0);
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT o.i_id, o.n_betrag, o.flrbuchung.d_buchungsdatum FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id="
					+ kontoIId
					+ " AND o.buchungdetailart_c_nr='"
					+ buchungsartCNr
					+ "'"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			if (!inklEB) {
				queryString += " AND NOT o.flrbuchung.buchungsart_c_nr='"
						+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "'";
				queryString += " AND NOT o.flrbuchung.b_autombuchungeb=1";
			}

			if (iAuszug != null)
				if (kummuliert)
					if (!inklEB)
						queryString += " AND NOT o.i_auszug IS NULL AND o.i_auszug <="
								+ iAuszug.intValue();
					else
						queryString += " AND (o.flrbuchung.buchungsart_c_nr='"
								+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' "
								+ " OR o.flrbuchung.b_autombuchungeb=1 "
								+ " OR ("
								+ " NOT o.i_auszug IS NULL AND o.i_auszug <="
								+ iAuszug.intValue() + "))";
				else if (!inklEB)
					queryString += " AND NOT o.i_auszug IS NULL AND o.i_auszug ="
							+ iAuszug.intValue();
				else
					queryString += " AND (o.flrbuchung.buchungsart_c_nr='"
							+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' "
							+ " OR o.flrbuchung.b_autombuchungeb=1 " + " OR ("
							+ " NOT o.i_auszug IS NULL AND o.i_auszug ="
							+ iAuszug.intValue() + "))";

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("pVon", tBeginn);
			query.setParameter("pEnd", tEnd);
			List<?> results = query.list();
			BigDecimal betrag = null;
			Iterator<?> it = results.iterator();
			while (it.hasNext()) {
				Object[] daten = (Object[]) it.next();
				betrag = getBelegbuchungFac(theClientDto.getMandant())
						.getBuchungsbetragInWahrung((Integer) daten[0],
								waehrungCNr, theClientDto);
				summe = summe.add(betrag);
			}
		} finally {
			if (session != null)
				session.close();
		}
		return summe;
	}

	/**
	 * Den Saldo eines Kontos berechnen.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getSaldoOhneEBVonKonto(Integer kontoIId,
			int geschaeftsjahr, int periode, TheClientDto theClientDto) {
		BigDecimal soll = getSollVonKonto(kontoIId, geschaeftsjahr, periode,
				false, theClientDto);
		BigDecimal haben = getHabenVonKonto(kontoIId, geschaeftsjahr, periode,
				false, theClientDto);
		return soll.subtract(haben);
	}

	/**
	 * Den Saldo eines Kontos f&uuml;r die UVA berechnen. Finanzamtsbuchungen werden
	 * ignoriert!
	 * 
	 * @param kontoIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getSaldoUVAOhneEBVonKonto(Integer kontoIId,
			int geschaeftsjahr, int periode, TheClientDto theClientDto) {
		BigDecimal soll = getSollUVAVonKonto(kontoIId, geschaeftsjahr, periode,
				theClientDto);
		BigDecimal haben = getHabenUVAVonKonto(kontoIId, geschaeftsjahr,
				periode, theClientDto);
		return soll.subtract(haben);
	}

	private BigDecimal getHabenUVAVonKonto(Integer kontoIId,
			int geschaeftsjahr, int periode, TheClientDto theClientDto) {
		return getSummeUVAVonKonto(kontoIId, geschaeftsjahr, periode,
				BuchenFac.HabenBuchung, theClientDto);
	}

	private BigDecimal getSollUVAVonKonto(Integer kontoIId, int geschaftsjahr,
			int periode, TheClientDto theClientDto) {
		return getSummeUVAVonKonto(kontoIId, geschaftsjahr, periode,
				BuchenFac.SollBuchung, theClientDto);
	}

	private BigDecimal getSummeUVAVonKonto(Integer kontoIId,
			int geschaeftsjahr, int periode, String buchungsartCNr,
			TheClientDto theClientDto) {
		Timestamp[] tVonBis;
		Timestamp tBeginn = null;
		Timestamp tEnd = null;
		tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
				theClientDto);
		tBeginn = tVonBis[0];
		tEnd = tVonBis[1];

		BigDecimal summe = new BigDecimal(0);
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT sum(n_betrag) FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id="
					+ kontoIId
					+ " AND o.buchungdetailart_c_nr='"
					+ buchungsartCNr
					+ "'"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			queryString += " AND NOT o.flrbuchung.buchungsart_c_nr='"
					+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' ";
			queryString += " AND NOT o.flrbuchung.b_autombuchungeb=1";
			queryString += " AND NOT o.flrbuchung.b_autombuchung=1";

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("pVon", tBeginn);
			query.setParameter("pEnd", tEnd);
			List<?> results = query.list();
			if (results.size() != 0)
				if (results.get(0) != null)
					summe = (BigDecimal) results.get(0);
		} finally {
			if (session != null)
				session.close();
		}
		return summe;
	}

	public BigDecimal getSaldoOhneEBVonKonto(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			TheClientDto theClientDto) {
		BigDecimal soll = getSollVonKonto(kontoIId, geschaftsjahr, periode,
				kummuliert, theClientDto);
		BigDecimal haben = getHabenVonKonto(kontoIId, geschaftsjahr, periode,
				kummuliert, theClientDto);
		return soll.subtract(haben);
	}

	public BigDecimal getSaldoVonKontoInWaehrung(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		BigDecimal soll = getSollVonKontoInWaehrung(kontoIId, geschaftsjahr,
				periode, kummuliert, waehrungCNr, theClientDto);
		BigDecimal haben = getHabenVonKontoInWaehrung(kontoIId, geschaftsjahr,
				periode, kummuliert, waehrungCNr, theClientDto);
		return soll.subtract(haben);
	}

	/**
	 * Alle Eroeffnungs-Buchungen eines Kontos finden.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return Summe
	 */
	public BigDecimal getSummeEroeffnungKontoIId(Integer kontoIId,
			int geschaeftsjahr, int periode, boolean kummuliert,
			TheClientDto theClientDto) {
		// Timestamp[] tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1,
		// theClientDto);
		Timestamp[] tVonBis;
		Timestamp tBeginn = null;
		Timestamp tEnd = null;
		if (kummuliert) {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
			tBeginn = tVonBis[0];
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tEnd = tVonBis[1];
		} else {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tBeginn = tVonBis[0];
			tEnd = tVonBis[1];
		}

		BigDecimal summe = new BigDecimal(0);
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			// String queryString =
			// "SELECT sum(case when o.buchungdetailart_c_nr='SOLL' then n_betrag else -n_betrag end) FROM FLRFinanzBuchungDetail o"
			// + " WHERE o.konto_i_id="
			// + kontoIId
			// + " AND o.flrbuchung.buchungsart_c_nr in ("
			// + "'"+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "', "
			// + "'"+ FinanzFac.BUCHUNGSART_SALDOVORTRAG + "') "
			// + " AND o.flrbuchung.t_storniert IS null"
			// +
			// " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			String queryString = "SELECT sum(case when o.buchungdetailart_c_nr='SOLL' then n_betrag else -n_betrag end) FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id="
					+ kontoIId
					+ " AND ("
					+ "o.flrbuchung.buchungsart_c_nr = '"
					+ FinanzFac.BUCHUNGSART_EROEFFNUNG
					+ "'"
					+ " OR o.flrbuchung.b_autombuchungeb=1"
					+ ")"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("pVon", tBeginn);
			query.setParameter("pEnd", tEnd);
			List<?> results = query.list();
			if (results.size() != 0)
				if (results.get(0) != null)
					summe = (BigDecimal) results.get(0);
		} finally {
			if (session != null)
				session.close();
		}
		return summe;
	}

	public BigDecimal getSummeEroeffnungKontoIIdInWaehrung(Integer kontoIId,
			int geschaeftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis;
		Timestamp tBeginn = null;
		Timestamp tEnd = null;
		if (kummuliert) {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
			tBeginn = tVonBis[0];
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tEnd = tVonBis[1];
		} else {
			tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, periode,
					theClientDto);
			tBeginn = tVonBis[0];
			tEnd = tVonBis[1];
		}

		Session session = null;
		BigDecimal summe = new BigDecimal(0);
		try {
			session = FLRSessionFactory.getFactory().openSession();
			// String queryString =
			// "SELECT o.buchung_i_id, o.buchungdetailart_c_nr, n_betrag FROM FLRFinanzBuchungDetail o"
			// + " WHERE o.konto_i_id="
			// + kontoIId
			// + " AND o.flrbuchung.buchungsart_c_nr in ("
			// + "'"+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "', "
			// + "'"+ FinanzFac.BUCHUNGSART_SALDOVORTRAG + "') "
			// + " AND o.flrbuchung.t_storniert IS null"
			// +
			// " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			String queryString = "SELECT o.buchung_i_id, o.buchungdetailart_c_nr, n_betrag FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id="
					+ kontoIId
					+ " AND ("
					+ "o.flrbuchung.buchungsart_c_nr = '"
					+ FinanzFac.BUCHUNGSART_EROEFFNUNG
					+ "'"
					+ " OR o.flrbuchung.b_autombuchungeb=1"
					+ " )"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("pVon", tBeginn);
			query.setParameter("pEnd", tEnd);
			List<?> results = query.list();
			BigDecimal betrag = null;
			Iterator<?> it = results.iterator();
			while (it.hasNext()) {
				Object[] daten = (Object[]) it.next();
				Buchung buchung = em.find(Buchung.class, (Integer) daten[0]);
				if (((String) daten[1]).equals(BuchenFac.SollBuchung))
					betrag = (BigDecimal) daten[2];
				else
					betrag = ((BigDecimal) daten[2]).negate();
				betrag = getLocaleFac()
						.rechneUmInAndereWaehrungGerundetZuDatum(betrag,
								theClientDto.getSMandantenwaehrung(),
								waehrungCNr, buchung.getTBuchungsdatum(),
								theClientDto);
				summe = summe.add(betrag);
			}
		} finally {
			if (session != null)
				session.close();
		}
		return summe;
	}

	/**
	 * pruefen, ob eine buchung zulaessig ist Die Pruefung
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param buchungdetailDtos
	 *            BuchungdetailDto[]
	 * @param pruefeBuchungsregeln
	 *            boolean
	 * @throws EJBExceptionLP
	 */
	public void pruefeBuchung(BuchungDto buchungDto,
			BuchungdetailDto[] buchungdetailDtos, boolean pruefeBuchungsregeln)
			throws EJBExceptionLP {
		myLogger.logData(buchungDto);
		// Pruefen der Werte
		pruefeBuchungswerte(buchungdetailDtos);
		// Pruefen, ob die Buchung gemaess den Buchungsregeln erlaubt ist
		// In Ausnahmefaellen kann auch entgegen den Buchungsregeln gebucht
		// werden
		// also dann keine Pruefung
		if (pruefeBuchungsregeln) {
			pruefeBuchungsregeln(buchungDto, buchungdetailDtos);
		}
		// pruefen, dass nicht auf konten verschiedener mandanten gebucht wird
		String mandantCNr = null;
		for (int i = 0; i < buchungdetailDtos.length; i++) {
			if (buchungdetailDtos[i].getKontoIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
						"Beleg: " + buchungDto.getCBelegnummer() + ", Art: "
								+ buchungDto.getBelegartCNr()
								+ ", kein Konto definiert in Buchungszeile "
								+ (i + 1) + " ("
								+ buchungdetailDtos[i].toString() + ")");
			try {
				KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(
						buchungdetailDtos[i].getKontoIId());
				mandantCNr = konto.getMandantCNr();
				if (buchungdetailDtos[i].getKontoIIdGegenkonto() != null) {
					KontoDto gegenKonto = getFinanzFac().kontoFindByPrimaryKey(
							buchungdetailDtos[i].getKontoIIdGegenkonto());
					if (!mandantCNr.equals(gegenKonto.getMandantCNr())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_AUF_KONTEN_VERSCHIEDENER_MANDANTEN,
								"");
					}
				}
			} catch (RemoteException ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
		}
	}

	/**
	 * Pruefen der Betraege einer Buchung 1. Die Summen von Soll und Haben
	 * muessen gleich sein 2. Alle Werte werden auf die vorgegebene Zahl von
	 * Nachkommastellen getrimmt
	 * 
	 * @param buchungDetailDtos
	 *            BuchungdetailDto[]
	 * @throws EJBExceptionLP
	 */
	public void pruefeBuchungswerte(BuchungdetailDto[] buchungDetailDtos)
			throws EJBExceptionLP {
		java.math.BigDecimal summeSoll = new java.math.BigDecimal(0);
		// FindBugs: return value of java.math.BigDecimal.setScale(int, int)
		// ignored in
		// com.lp.server.finanz.ejbfac.BuchenFacBean.pruefeBuchungswerte(BuchungdetailDto[])
		summeSoll = summeSoll.setScale(FinanzFac.NACHKOMMASTELLEN,
				BigDecimal.ROUND_HALF_EVEN);
		java.math.BigDecimal summeHaben = new java.math.BigDecimal(0);
		// FindBugs: return value of java.math.BigDecimal.setScale(int, int)
		// ignored in
		// com.lp.server.finanz.ejbfac.BuchenFacBean.pruefeBuchungswerte(BuchungdetailDto[])
		summeHaben = summeHaben.setScale(FinanzFac.NACHKOMMASTELLEN,
				BigDecimal.ROUND_HALF_EVEN);
		for (int i = 0; i < buchungDetailDtos.length; i++) {
			BuchungdetailDto buchungdetailDto = buchungDetailDtos[i];
			// Werte auf die richtige Anzahl der Nachkommastellen druecken
			buchungdetailDto.setNBetrag(Helper.rundeKaufmaennisch(
					buchungdetailDto.getNBetrag(), FinanzFac.NACHKOMMASTELLEN));
			buchungdetailDto.setNUst(Helper.rundeKaufmaennisch(
					buchungdetailDto.getNUst(), FinanzFac.NACHKOMMASTELLEN));
			// addieren
			if (buchungdetailDto.getBuchungdetailartCNr().equals(
					BuchenFac.HabenBuchung))
				summeHaben = summeHaben.add(buchungdetailDto.getNBetrag());
			else if (buchungdetailDto.getBuchungdetailartCNr().equals(
					BuchenFac.SollBuchung))
				summeSoll = summeSoll.add(buchungdetailDto.getNBetrag());
			else
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"Unbekannte Buchungsart "
								+ buchungdetailDto.getBuchungdetailartCNr()));
		}
		if (!(summeHaben.compareTo(summeSoll) == 0)) {
			myLogger.logData("Haben: " + summeHaben);
			myLogger.logData("Soll:  " + summeSoll);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "!summeHaben.equals(summeSoll)", "\n"+outPrintBuchungssatz(Arrays.asList(buchungDetailDtos)));
		}
	}

	/**
	 * Pruefen, ob eine Buchung den Buchungsregeln entspricht
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param buchungDetailDtos
	 *            BuchungdetailDto[]
	 */
	public void pruefeBuchungsregeln(BuchungDto buchungDto,
			BuchungdetailDto[] buchungDetailDtos) {
		/**
		 * @todo pruefeBuchungsregeln implementieren PJ 3989
		 */
		pruefeAufMitlaufendeKonten(buchungDetailDtos);
	}
	
	protected void pruefeAufMitlaufendeKonten(BuchungdetailDto[] buchungDetailDtos) {
		for(BuchungdetailDto detail : buchungDetailDtos) {
			try {
				if(getFinanzFac().isMitlaufendesKonto(detail.getKontoIId())) {
					KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(detail.getKontoIId());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_AUF_MITLAUFENDES_KONTO_NICHT_ERLAUBT,
							outPrintBuchungssatz(Arrays.asList(buchungDetailDtos)),
							konto.getCNr()	+ ", " + konto.getCBez());
				}
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
		}
	}

	/**
	 * Verbuchen einer Umbuchung
	 * 
	 * @param buchungDto
	 *            BuchungDto
	 * @param buchungen
	 *            BuchungdetailDto[]
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return BuchungDto
	 */
	public BuchungDto verbucheUmbuchung(BuchungDto buchungDto,
			BuchungdetailDto[] buchungen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (buchungDto.getIId() != null) {
			// ist eine Aenderungsbuchung, alte stornieren
			storniereBuchung(buchungDto.getIId(), theClientDto);
			buchungDto.setIId(null);
			buchungDto.setTStorniert(null);
			buchungDto.setPersonalIIdStorniert(null);
			for (int i = 0; i < buchungen.length; i++)
				buchungen[i].setIId(null);
		}

		// buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_UMBUCHUNG);
		buchungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		buchungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		// wenn keine Belegnummer dann eine erzeugen und eintragen
		if (buchungDto.getCBelegnummer() == null) {
			buchungDto.setCBelegnummer(getBelegnummerUmbuchung(
					buchungDto.getIGeschaeftsjahr(), theClientDto));
		}

		// wenn ein Eroeffnungskonto beteiligt dann die Buchungsart anpassen
		for (int i = 0; i < buchungen.length; i++) {
			Konto konto = em.find(Konto.class, buchungen[i].getKontoIId());
			if (konto.getKontoartCNr() != null
					&& konto.getKontoartCNr().equals(
							FinanzServiceFac.KONTOART_EROEFFNUNG)) {
				buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_EROEFFNUNG);
				break;
			}
		}

		// Das Verbuchen einer Umbuchung muss immer den Buchungsregeln
		// entsprechen
		return buchen(buchungDto, buchungen, true, theClientDto);
	}

	public BigDecimal getSaldoVonKontoByAuszug(Integer kontoIId,
			int geschaftsjahr, Integer auszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto) {
		BigDecimal soll = getSollVonKontoByAuszug(kontoIId, geschaftsjahr,
				auszugBisInklusiv, kummuliert, inklEB, theClientDto);
		BigDecimal haben = getHabenVonKontoByAuszug(kontoIId, geschaftsjahr,
				auszugBisInklusiv, kummuliert, inklEB, theClientDto);
		return soll.subtract(haben);
	}

	public BigDecimal getHabenVonKontoByAuszug(Integer kontoIId,
			int geschaeftsjahr, Integer auszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto) {
		// periode auf -1 = ganzes geschaeftsjahr
		return getSummeVonKonto(kontoIId, geschaeftsjahr, -1,
				BuchenFac.HabenBuchung, auszugBisInklusiv, kummuliert, inklEB,
				theClientDto);
	}

	public BigDecimal getSollVonKontoByAuszug(Integer kontoIId,
			int geschaeftsjahr, Integer auszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto) {
		// periode auf -1 = ganzes geschaeftsjahr
		return getSummeVonKonto(kontoIId, geschaeftsjahr, -1,
				BuchenFac.SollBuchung, auszugBisInklusiv, kummuliert, inklEB,
				theClientDto);
	}

	public BigDecimal getSaldoVonKontoByAusziffern(Integer kontoIId,
			int geschaftsjahr, Integer auszifferkennzeichen,
			TheClientDto theClientDto) {
		if (auszifferkennzeichen != null) {
			BigDecimal soll = getSollVonKontoByAusziffern(kontoIId,
					geschaftsjahr, auszifferkennzeichen, theClientDto);
			BigDecimal haben = getHabenVonKontoByAusziffern(kontoIId,
					geschaftsjahr, auszifferkennzeichen, theClientDto);
			return soll.subtract(haben);
		} else
			return null;
	}

	private BigDecimal getHabenVonKontoByAusziffern(Integer kontoIId,
			int geschaeftsjahr, Integer auszifferkennzeichen,
			TheClientDto theClientDto) {
		return getSummeVonKonto(kontoIId, geschaeftsjahr,
				BuchenFac.HabenBuchung, auszifferkennzeichen, theClientDto);
	}

	private BigDecimal getSollVonKontoByAusziffern(Integer kontoIId,
			int geschaeftsjahr, Integer auszifferkennzeichen,
			TheClientDto theClientDto) {
		return getSummeVonKonto(kontoIId, geschaeftsjahr,
				BuchenFac.SollBuchung, auszifferkennzeichen, theClientDto);
	}

	protected GeschaeftsjahrMandant queryGeschaeftsjahrFuerDatum(Date date,
			String mandantCnr) throws Exception {
		HvTypedQuery<GeschaeftsjahrMandant> query = GeschaeftsjahrMandantQuery
				.byDatumMandant(em, date, mandantCnr);
		query.setMaxResults(1);
		return query.getSingleResult();
		// Query query = em.createNamedQuery("GeschaeftsjahrfindByDatum");
		// query.setParameter(1, date);
		// query.setParameter(2, mandantCnr) ;
		// query.setMaxResults(1);
		// return (Geschaeftsjahr) query.getSingleResult();
	}

	public Integer findGeschaeftsjahrFuerDatum(Date date, String mandantCnr) {
		Integer iGeschaeftsjahr = null;

		try {
			GeschaeftsjahrMandant gj = queryGeschaeftsjahrFuerDatum(date,
					mandantCnr);
			iGeschaeftsjahr = gj.getIGeschaeftsjahr();
		} catch (Exception e) {
		}

		return iGeschaeftsjahr;
	}

	/**
	 * Erstellt einen GregorianCalender f&uuml;r Millisekunden
	 * 
	 * @param millis
	 * @return Calendar vorbesetzt mit den angegebenen Millisekunden
	 */
	protected Calendar getCalendarForTimeInMillis(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}

	/**
	 * Liefert die Periode beginnend mit 1 fuer das angegebene Datum</br>
	 * 
	 * Es wird die Geschaeftsjahrdefinition beruecksichtigt.
	 * 
	 * @param date
	 * @return Die Periode des zum angegebenen Datum passenden Geschaeftsjahres,
	 *         beginnend mit 1, oder null wenn es ein Problem beim Ermitteln des
	 *         Geschaeftsjahres gab
	 */
	public Integer getPeriodeImGJFuerDatum(Date date, String mandantCnr) {
		try {
			GeschaeftsjahrMandant gj = queryGeschaeftsjahrFuerDatum(date,
					mandantCnr);

			Calendar cal = getCalendarForTimeInMillis(gj.getTBeginndatum()
					.getTime());
			int monthToSearch = getCalendarForTimeInMillis(date.getTime()).get(
					Calendar.MONTH);

			Integer periode = 1;
			while (cal.get(Calendar.MONTH) != monthToSearch) {
				++periode;
				cal.add(Calendar.MONTH, 1);
			}

			return periode;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Datumsbereich der Periode im Gesch&auml;ftsjahr periode = -1 ganzes
	 * Gesch&auml;ftsjahr
	 * 
	 * ACHTUNG: das Ende Datum ist der erste Tag im neuen Gesch&auml;ftsjahr! ->
	 * direkt als Filter mit datum < ende verwenden
	 */
	public Timestamp[] getDatumbereichPeriodeGJ(Integer geschaeftsjahr,
			int periode, TheClientDto theClientDto) {
		// Geschaeftsjahr gj = em.find(Geschaeftsjahr.class, geschaeftsjahr);
		GeschaeftsjahrMandant gj = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, geschaeftsjahr,
						theClientDto.getMandant());
		if (gj == null) {
			// Das Geschaeftsjahr existiert (noch) nicht -> ein "unmoegliches"
			// Datum setzen
			return new Timestamp[] { new Timestamp(0), new Timestamp(0) };
		}
		// Geschaeftsjahr gjNext = em.find(Geschaeftsjahr.class, new Integer(
		// geschaeftsjahr.intValue() + 1));
		GeschaeftsjahrMandant gjNext = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, geschaeftsjahr + 1,
						theClientDto.getMandant());
		if (gjNext != null && periode == -1) {
			return new Timestamp[] { gj.getTBeginndatum(),
					gjNext.getTBeginndatum() };
		}

		int beginnMonatGeschaeftsjahr = 1;
		ParametermandantDto para = null;
		try {
			para = getParameterFac()
					.parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							theClientDto.getMandant());
			beginnMonatGeschaeftsjahr = Integer.parseInt(para.getCWert());
		} catch (Exception e) {
			//
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(gj.getTBeginndatum().getTime());
		Timestamp tBeginndatum = new Timestamp(cal.getTimeInMillis());
		Timestamp tEnd;
		if (periode != -1) {
			cal.add(Calendar.MONTH, periode - 1);
			tBeginndatum = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.MONTH, 1);
			tEnd = new Timestamp(cal.getTimeInMillis());
		} else {
			cal.add(Calendar.MONTH, 12);
			tEnd = new Timestamp(cal.getTimeInMillis());
		}
		Timestamp tBeginndatumNext = null;
		if (gjNext != null) {
			// naechstes Geschaeftsjahr ist definiert, dann Ende ist maximal Start
			// naechstes GJ
			tBeginndatumNext = gjNext.getTBeginndatum();
			if (tEnd.after(tBeginndatumNext))
				tEnd = tBeginndatumNext;
		} else {
			cal.setTimeInMillis(gj.getTBeginndatum().getTime());
			if (cal.get(Calendar.MONTH) + 1 != beginnMonatGeschaeftsjahr) {
				// es war ein Rumpfjahr
				// richtiges Ende berechnen
				cal.add(Calendar.YEAR, 1);
				cal.roll(Calendar.MONTH, beginnMonatGeschaeftsjahr);
				if (tEnd.after(new Timestamp(cal.getTimeInMillis())))
					tEnd = new Timestamp(cal.getTimeInMillis());
			}
		}
		return new Timestamp[] { tBeginndatum, tEnd };
	}

	public Timestamp[] getDatumVonBisGeschaeftsjahr(Integer geschaeftsjahr,
			TheClientDto theClientDto) {
		Timestamp[] tVonBis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1,
				theClientDto);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tVonBis[1].getTime());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		tVonBis[1] = new Timestamp(cal.getTimeInMillis());
		return tVonBis;
	}

	/**
	 * verbucheKassenbuchung
	 * 
	 * Kasse bucht Einnahmen im Haben auf Kassenkonto Kasse bucht Ausgaben im
	 * Soll auf Kassenkonto Bei &Auml;nderungen wird die Buchung storniert und neu
	 * gebucht
	 * 
	 * @param buchungDto
	 *            Kassenbuchung
	 * @param buchungdetailDto
	 *            Gegenbuchung (falls eine Steuerbuchung erforderlich ist der
	 *            Ustbetrag gesetzt
	 * @param kassenKontoIId
	 *            KontoIId des Kassenbuchs in das gebucht werden soll
	 * 
	 */
	public BuchungDto verbucheKassenbuchung(BuchungDto buchungDto,
			BuchungdetailDto buchungdetailDto, Integer kassenKontoIId,
			KassenbuchungsteuerartDto kbstDto, TheClientDto theClientDto) {

		if (buchungDto.getIId() != null) {
			// stornieren
			storniereBuchung(buchungDto.getIId(), theClientDto);
			// buchungDto.setTStorniert(new
			// Timestamp(System.currentTimeMillis()));
			// buchungDto.setPersonalIIdStorniert(theClientDto.getIDPersonal());
			// updateBuchung(buchungDto, theClientDto);
			buchungDto.setIId(null);
			buchungDto.setTStorniert(null);
			buchungDto.setPersonalIIdStorniert(null);
			buchungdetailDto.setIId(null);
		}

		buchungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		buchungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());

		// wenn keine Belegnummer dann eine erzeugen und eintragen
		if (buchungDto.getCBelegnummer() == null) {
			buchungDto.setCBelegnummer(getBelegnummerKassenbuch(
					buchungDto.getIGeschaeftsjahr(), theClientDto));
		}

		BuchungdetailDto[] buchungen;
		Integer steuerKontoIId = null;
		KontoDto kontoDto = null;
		try {
			kontoDto = getFinanzFac().kontoFindByPrimaryKey(
					buchungdetailDto.getKontoIId());
		} catch (Exception ex1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
		}

		boolean mitUst = false;
		if (buchungdetailDto.getNUst().doubleValue() == 0.0) {
			mitUst = false;
		} else {
			if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				mitUst = true;
				try {
					steuerKontoIId = getSteuerkontoFuerKonto(kontoDto,
							buchungDto.getDBuchungsdatum(), buchungdetailDto,
							kbstDto, theClientDto);
				} catch (Exception e) {
					//
				}
				// es muessen alle Konten definiert sein
				if (steuerKontoIId == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_UST_KONTO_DEFINIERT,
							"Kein Steuerkonto definiert f\u00FCr Konto "
									+ kontoDto.getCNr());
				}
			} else {
				// keine Steuerbuchung bei Kreditoren / Debitoren
				mitUst = false;
			}
		}
		if (mitUst == false)
			buchungen = new BuchungdetailDto[2];
		else
			buchungen = new BuchungdetailDto[3];

		// Buchung am Kassenkonto
		buchungen[0] = new BuchungdetailDto();
		if (buchungdetailDto.getBuchungdetailartCNr().equals(
				BuchenFac.HabenBuchung))
			buchungen[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		else
			buchungen[0].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		buchungen[0].setKontoIId(kassenKontoIId);
		buchungen[0].setKontoIIdGegenkonto(buchungdetailDto.getKontoIId());
		buchungen[0].setNUst(buchungdetailDto.getNUst());
		if (kontoDto.getKontotypCNr().equals(
				FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			// Betrag ist netto, auf Kasse brutto buchen
			buchungen[0].setNBetrag(buchungdetailDto.getNBetrag().add(
					buchungdetailDto.getNUst()));
		} else {
			// Betrag ist brutto, so weiter buchen
			buchungen[0].setNBetrag(buchungdetailDto.getNBetrag());
		}
		buchungen[0].setIAuszug(buchungdetailDto.getIAuszug());

		// jetzt die Gegenbuchung
		buchungen[1] = buchungdetailDto;
		buchungen[1].setKontoIIdGegenkonto(kassenKontoIId);
		buchungen[1].setNUst(new BigDecimal(0));

		if (mitUst) {
			// Buchung am Ust Konto falls erforderlich
			buchungen[2] = new BuchungdetailDto();
			buchungen[2].setBuchungdetailartCNr(buchungen[1]
					.getBuchungdetailartCNr());
			buchungen[2].setKontoIId(steuerKontoIId);
			buchungen[2].setKontoIIdGegenkonto(kassenKontoIId);
			buchungen[2].setNBetrag(buchungen[0].getNUst());
			buchungen[2].setNUst(new BigDecimal(0));
		}

		// Das Verbuchen einer Umbuchung muss immer den Buchungsregeln
		// entsprechen
		return buchen(buchungDto, buchungen, true, theClientDto);
	}

	@SuppressWarnings("unchecked")
	private Integer getSteuerkontoFuerKonto(KontoDto kontoDto,
			Date buchungsdatum, BuchungdetailDto buchungdetailDto,
			KassenbuchungsteuerartDto kbstDto, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		Integer steuerKontoIId = null;
		Integer steuerKategorieIId = null;
		Integer mwstSatzbezIId = null;

		Konto konto = em.find(Konto.class, buchungdetailDto.getKontoIId());

		if (konto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			Query query = em
					.createNamedQuery("KontolaenderartfindBykontoIIdUebersetzt");
			query.setParameter(1, kontoDto.getIId());
			List<Kontolaenderart> ktoLaender = query.getResultList();
			Integer finanzamtIId = kontoDto.getFinanzamtIId();
			String laenderartCNr = null;
			if (ktoLaender.size() == 0) {
				// kein uebersetztes Konto -> Inland
				laenderartCNr = FinanzFac.LAENDERART_INLAND;
			} else if (ktoLaender.size() == 1) {
				Kontolaenderart ktoLand = ktoLaender.get(1);
				laenderartCNr = ktoLand.getLaenderartCNr();
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT,
						"Mehrere L\u00E4nderarten f\u00FCr Konto " + kontoDto.getCNr());
			}
			SteuerkategorieDto steuerkategorieDto = getFinanzFac()
					.getSteuerkategorieZuLaenderart(finanzamtIId,
							laenderartCNr, theClientDto);
			steuerKategorieIId = steuerkategorieDto.getIId();

			// BigDecimal steuersatz =
			// Helper.getProzentsatzBD(buchungdetailDto.getNBetrag(),
			// buchungdetailDto.getNUst(), FinanzFac.NACHKOMMASTELLEN);

			mwstSatzbezIId = kbstDto.getMwstsatzbezIId();
		}
		if (kbstDto.isUstBuchung())
			steuerKontoIId = getFinanzServiceFac()
					.getUstKontoFuerSteuerkategorie(steuerKategorieIId,
							mwstSatzbezIId);
		else
			steuerKontoIId = getFinanzServiceFac()
					.getVstKontoFuerSteuerkategorie(steuerKategorieIId,
							mwstSatzbezIId);
		return steuerKontoIId;
	}

	private List<Integer> findAllSteuersatzBezZuBetragFuerDatum(Date buchungsdatum,
			BigDecimal nettoBetrag, BigDecimal steuerBetrag,
			TheClientDto theClientDto) {
		MwstsatzDto[] allMwst = getMandantFac().mwstsatzfindAllByMandant(
				theClientDto.getMandant(),
				new Timestamp(buchungsdatum.getTime()), false);

		ArrayList<Object[]> diffs = new ArrayList<Object[]>();
		for (int i = 0; i < allMwst.length; i++) {
			BigDecimal mwst = Helper.getProzentWert(nettoBetrag,
					new BigDecimal(allMwst[i].getFMwstsatz()),
					FinanzFac.NACHKOMMASTELLEN);
			Object[] x = { allMwst[i].getIIMwstsatzbezId(),
					steuerBetrag.subtract(mwst).abs() };
			diffs.add(x);
		}
		
		Collections.sort(diffs, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return (((BigDecimal) o1[1]).compareTo((BigDecimal) o2[1]));
			}
		});
		BigDecimal minDiff = (BigDecimal) diffs.get(0)[1];
		List<Integer> mwstSatzbezIIds = new ArrayList<Integer>();
		for(Object[] diff : diffs) {
			if(diff[1].equals(minDiff)) {
				mwstSatzbezIIds.add((Integer)diff[0]);
			}
		}
		return mwstSatzbezIIds;
	}

	public BuchungdetailDto[] buchungdetailsFindByBuchungIId(Integer buchungIId) {
		Query query = em
				.createNamedQuery(Buchungdetail.QueryBuchungdetailfindByBuchungIID);
		query.setParameter(1, buchungIId);
		Collection<?> cl = query.getResultList();
		return assembleBuchungdetailDtos(cl);
	}

	public List<BuchungdetailDto> buchungdetailsFindByKontoIIdBuchungIId(
			Integer kontoIId, Integer buchungIId) {
		HvTypedQuery<Buchungdetail> query = new HvTypedQuery<Buchungdetail>(
				em.createNamedQuery(Buchungdetail.QueryBuchungdetailfindByKontoIIdBuchungIId));
		query.setParameter(1, kontoIId);
		query.setParameter(2, buchungIId);
		return Arrays.asList(assembleBuchungdetailDtos(query.getResultList()));
	}

	public KassenbuchungsteuerartDto getKassenbuchungSteuerart(
			Integer buchungIId, TheClientDto theClientDto) {
		KassenbuchungsteuerartDto kstDto = new KassenbuchungsteuerartDto();
		Buchung buchung = em.find(Buchung.class, buchungIId);
		MwstsatzDto[] allMwst = getMandantFac().mwstsatzfindAllByMandant(
				theClientDto.getMandant(),
				new Timestamp(buchung.getTBuchungsdatum().getTime()), false);
		BuchungdetailDto[] details = buchungdetailsFindByBuchungIId(buchungIId);
		Konto konto = em.find(Konto.class, details[0].getKontoIIdGegenkonto());

		outPrintBuchungssatz(Arrays.asList(details));
		int steuerIndex = 0;
		boolean keineSteuer = true;
		for (int i = 1; i < details.length; i++) {
			if (details[i].getNBetrag().compareTo(details[0].getNUst()) == 0) {
				keineSteuer = false;
				steuerIndex = i;
				break;
			}
		}
		if (keineSteuer) {
			// keine Steuer gebucht
			for (int i = 0; i < allMwst.length; i++) {
				if (allMwst[i].getFMwstsatz().doubleValue() == 0) {
					kstDto.setMwstsatzbezIId(allMwst[i]
							.getIIMwstsatzbezId());
					break;
				}
			}
		} else {
			List<Integer> mwstSatzbezIIds = findAllSteuersatzBezZuBetragFuerDatum(
					buchung.getTBuchungsdatum(), details[0].getNBetrag()
					.subtract(details[steuerIndex].getNBetrag()),
			details[0].getNUst(), theClientDto);
			List<Steuerkategoriekonto> stkk = new ArrayList<Steuerkategoriekonto>();
			
			// jetzt schauen ob in einer Steuerkategorie mit
			// einem der Steuersaetze mein Konto hinterlegt ist 
			for(Integer mwstSatzbezIId : mwstSatzbezIIds) {
				HvTypedQuery<Steuerkategoriekonto> query = new HvTypedQuery<Steuerkategoriekonto>(
						em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
				query.setParameter(1, details[steuerIndex].getKontoIId());
				query.setParameter(2, mwstSatzbezIId);
				stkk.addAll(query.getResultList());
			}
			if(stkk.size() == 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
						"Steuerkonto ID=" + details[steuerIndex].getKontoIId()
								+ " nicht zugeordnet");
			}
			kstDto.setMwstsatzbezIId(stkk.get(0).getMwstsatzbezIId());
		}
		if (konto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			// Steuerbuchung suchen
			if(!keineSteuer) {
				kstDto.setUstBuchung(isUstKontoFuerSteuersatz(
					details[steuerIndex].getKontoIId(),
					kstDto.getMwstsatzbezIId()));
			}
		} else {
			if (konto.getKontotypCNr()
					.equals(FinanzServiceFac.KONTOTYP_DEBITOR))
				kstDto.setUstBuchung(true);
			else
				kstDto.setUstBuchung(false);
//			BigDecimal nettoBetrag = details[0].getNBetrag().subtract(
//					details[0].getNUst());
//			// BigDecimal steuersatz = Helper.getProzentsatzBD(nettoBetrag,
//			// details[0].getNUst(), FinanzFac.NACHKOMMASTELLEN);
//			kstDto.setMwstsatzbezIId(findSteuersatzBezZuBetragFuerDatum(
//					buchung.getTBuchungsdatum(), nettoBetrag,
//					details[0].getNUst(), theClientDto));
		}
		
		// alle MwstSaetze finden bei denen der Betrag und das Datum passt
		if (kstDto.getMwstsatzbezIId() != null) {
			Mwstsatzbez mwstbez = em.find(Mwstsatzbez.class,
					kstDto.getMwstsatzbezIId());
			if (mwstbez != null)
				kstDto.setMwstsatzbezBezeichnung(mwstbez.getCBezeichnung());
		}

		return kstDto;
	}

	@SuppressWarnings("unchecked")
	private boolean isUstKontoFuerSteuersatz(Integer kontoIId,
			Integer mwstsatzbezIId) {
		Query query = em
				.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid");
		query.setParameter(1, kontoIId);
		query.setParameter(2, mwstsatzbezIId);
		query.setMaxResults(1);
		List<Steuerkategoriekonto> list = query.getResultList();
		if (list.size() == 0)
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
					"Steuerkonto ID=" + kontoIId.intValue()
							+ " nicht zugeordnet");
		else {
			Steuerkategoriekonto stkk = list.get(0);
			if (stkk.getKontoIIdVk() != null
					&& stkk.getKontoIIdVk().equals(kontoIId))
				return true;
			else
				return false;
		}
	}

	public void removeSaldovortragsBuchung(SaldovortragModelBase saldoModel,
			TheClientDto theClientDto) {
		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(
				saldoModel.getGeschaeftsJahr(), -1, theClientDto);

		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT o.flrbuchung.i_id FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id= :kontoiid";

			if (saldoModel.isDeleteManualEB()) {
				queryString += " AND ( o.flrbuchung.b_autombuchungeb = 1 OR o.flrbuchung.buchungsart_c_nr='"
						+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "') ";
			} else {
				queryString += " AND o.flrbuchung.b_autombuchungeb = 1";
			}

			queryString += " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("kontoiid", saldoModel.getKontoIId());
			query.setParameter("pVon", tVonbis[0]);
			query.setParameter("pEnd", tVonbis[1]);
			List<Integer> results = query.list();
			for (Integer buchungIId : results) {
				storniereBuchung(buchungIId, theClientDto);
			}
		} finally {
			if (session != null)
				session.close();
		}
	}

	public void removeSaldovortragsBuchung(Integer kontoIId,
			int geschaeftsjahr, boolean deleteEB, TheClientDto theClientDto) {
		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1,
				theClientDto);

		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT o.flrbuchung.i_id FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id= :kontoiid"
					+ " AND o.flrbuchung.b_autombuchungeb = 1";

			if (deleteEB) {
				queryString += " OR o.flrbuchung.buchungsart_c_nr='"
						+ FinanzFac.BUCHUNGSART_EROEFFNUNG + "' ";
			}

			queryString += " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("kontoiid", kontoIId);
			query.setParameter("pVon", tVonbis[0]);
			query.setParameter("pEnd", tVonbis[1]);
			List<Integer> results = query.list();
			for (Integer buchungIId : results) {
				storniereBuchung(buchungIId, theClientDto);
			}
		} finally {
			if (session != null)
				session.close();
		}
	}

	private void createSaldovortragsBuchungImpl(String kontoTyp,
			BuchungDto buchungDto, List<BuchungdetailDto> details,
			Integer kontoIIdGegenkonto, TheClientDto theClientDto) {

		BigDecimal buchungSaldo = BigDecimal.ZERO;

		for (BuchungdetailDto detail : details) {
			if (detail.isHabenBuchung()) {
				buchungSaldo = buchungSaldo.add(detail.getNBetrag());
			} else {
				buchungSaldo = buchungSaldo.subtract(detail.getNBetrag());
			}
		}

		BuchungdetailDto gegenBuchung = (BuchungdetailDto) details.get(0)
				.clone();

		if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontoTyp)) {
			if (HabenBuchung.equals(details.get(0).getBuchungdetailartCNr())) {
				gegenBuchung.setBuchungdetailartCNr(SollBuchung);
			} else {
				gegenBuchung.setBuchungdetailartCNr(HabenBuchung);
			}
		} else {
			if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontoTyp)) {
				if (details.size() == 1) {
					gegenBuchung
							.setBuchungdetailartCNr(HabenBuchung.equals(details
									.get(0).getBuchungdetailartCNr()) ? SollBuchung
									: HabenBuchung);
				} else {
					if (HabenBuchung.equals(details.get(0)
							.getBuchungdetailartCNr())) {
						gegenBuchung.setBuchungdetailartCNr(SollBuchung);
					} else {
						gegenBuchung.setBuchungdetailartCNr(HabenBuchung);
					}
					gegenBuchung.setNBetrag(buchungSaldo);
				}
				// gegenBuchung.setBuchungdetailartCNr(buchungSaldo.signum() > 0
				// ? SollBuchung : HabenBuchung) ;
			} else {
				if (details.size() == 1) {
					gegenBuchung
							.setBuchungdetailartCNr(HabenBuchung.equals(details
									.get(0).getBuchungdetailartCNr()) ? SollBuchung
									: HabenBuchung);
				} else {
					gegenBuchung
							.setBuchungdetailartCNr(buchungSaldo.signum() > 0 ? HabenBuchung
									: SollBuchung);
				}
			}
		}

		// gegenBuchung.setBuchungdetailartCNr(buchungSaldo.signum() > 0 ?
		// SollBuchung : HabenBuchung) ;
		gegenBuchung.setKontoIIdGegenkonto(gegenBuchung.getKontoIId());
		gegenBuchung.setKontoIId(kontoIIdGegenkonto);
		// gegenBuchung.setNUst(BigDecimal.ZERO) ;
		details.add(gegenBuchung);

		buchen(buchungDto,
				details.toArray(new BuchungdetailDto[details.size()]), true,
				theClientDto);
	}

	protected void createSaldovortragsTimestamp(Integer kontoIId,
			int geschaeftsjahr, TheClientDto theClientDto)
			throws RemoteException {
		KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);
		kontoDto.setiGeschaeftsjahrEB(geschaeftsjahr);
		kontoDto.settEBAnlegen(new Timestamp(System.currentTimeMillis()));
		getFinanzFac().updateKonto(kontoDto, theClientDto);
	}

	private void createSaldovortragsTimestamp(SaldovortragModelBase saldoModel,
			TheClientDto theClientDto) throws RemoteException {

		KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
				saldoModel.getKontoIId());
		kontoDto.setiGeschaeftsjahrEB(saldoModel.getGeschaeftsJahr());
		kontoDto.settEBAnlegen(new Timestamp(System.currentTimeMillis()));
		getFinanzFac().updateKonto(kontoDto, theClientDto);
	}

	private Integer getEroeffnungsKonto(String kontotyp,
			TheClientDto theClientDto) throws RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		Integer hauptFinanzamtIId = mandantDto.getPartnerIIdFinanzamt();
		FinanzamtDto finanzamtDto = getFinanzFac().finanzamtFindByPrimaryKey(
				hauptFinanzamtIId, theClientDto.getMandant(), theClientDto);
		if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotyp)) {
			return finanzamtDto.getKontoIIdEbsachkonten();
		}
		if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontotyp)) {
			return finanzamtDto.getKontoIIdEbdebitoren();
		}
		if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kontotyp)) {
			return finanzamtDto.getKontoIIdEbkreditoren();
		}

		return null;
	}

	private void prepareGegenkonto(SaldovortragModelBase kontoModel,
			TheClientDto theClientDto) throws RemoteException {
		if (null != kontoModel.getGegenkontoIId())
			return;

		Integer kontoIId = getEroeffnungsKonto(kontoModel.getKontoTyp(),
				theClientDto);
		kontoModel.setGegenkontoIId(kontoIId);

		if (null == kontoIId) {
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
					kontoModel.getKontoIId());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEIN_EROEFFNUNGSKONTO_DEFINIERT,
					"F\u00FCr Konto " + kontoDto.getCNr() + " ("
							+ kontoModel.getKontoTyp()
							+ ") ist kein Er\u00F6ffnungskonto definiert!");
		}
	}

	private Integer getDefaultKostenstelle(Integer sachkontoIId,
			TheClientDto theClientDto) throws RemoteException {
		KontoDto sachkontoDto = getFinanzFac().kontoFindByPrimaryKey(
				sachkontoIId);
		Integer kostenstelleIId = sachkontoDto.getKostenstelleIId();
		if (kostenstelleIId != null)
			return kostenstelleIId;

		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKeyOhneExc(
				theClientDto.getMandant(), theClientDto);
		return mandantDto == null ? null : mandantDto.getIIdKostenstelle();
	}

	/**
	 * Erfuellt das GJ die Anforderungen fuer einen Saldovortrag.
	 * 
	 * Es muss ein vorheriges GJ geben. Falls nicht -> false Das vorherige GJ
	 * muss(!) gesperrt sein. Falls nicht -> Exception
	 * 
	 * Existiert ein vorvorheriges GJ, dann muss dieses gesperrt sein.
	 * 
	 * @param geschaeftsjahr
	 *            das geprueft werden soll
	 */
	private void validateGjForSaldovortrag(int geschaeftsjahr, String mandant) {
		// Geschaeftsjahr vorvorigesGJ = em.find(Geschaeftsjahr.class,
		// geschaeftsjahr - 2);
		// Geschaeftsjahr vorigesGJ = em.find(Geschaeftsjahr.class,
		// geschaeftsjahr - 1);

		GeschaeftsjahrMandant vorvorigesGJ = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, geschaeftsjahr - 2, mandant);
		GeschaeftsjahrMandant vorigesGJ = GeschaeftsjahrMandantQuery
				.singleByYearMandant(em, geschaeftsjahr - 1, mandant);
		/*
		 * Wenn jemand noch kein GJ angelegt hat, dann kann er auch keine
		 * Perioden&uuml;bernahme f&uuml;r das "aktuelle" (== geschaeftsjahr) machen
		 */
		if (vorigesGJ == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGES_GESCHAEFTSJAHR,
					"Das Geschaeftsjahr " + geschaeftsjahr
							+ " existiert nicht!");
		}
		if (vorvorigesGJ == null)
			return;

		if (vorvorigesGJ.getTSperre() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_VORHERIGES_GESCHAEFTSJAHR_NICHT_GESPERRT,
					"Das Geschaeftsjahr " + vorvorigesGJ.getIGeschaeftsjahr()
							+ " ist nicht gesperrt.");
		}
	}

	public void createSaldovortragsBuchung(
			SaldovortragModelSachkonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException {
		validateGjForSaldovortrag(saldovortragModel.getGeschaeftsJahr(),
				theClientDto.getMandant());

		removeSaldovortragsBuchung(saldovortragModel, theClientDto);

		prepareGegenkonto(saldovortragModel, theClientDto);
		createSaldovortragsBuchungSachkonto(saldovortragModel, theClientDto);
		createSaldovortragsTimestamp(saldovortragModel, theClientDto);
	}

	protected boolean isKontoBankKonto(Integer kontoIId) throws RemoteException {
		return getFinanzFac().bankverbindungFindByKontoIIdOhneExc(kontoIId) != null;
	}

	protected boolean createSaldovortragsBuchungSachkonto(
			SaldovortragModelSachkonto saldoModel, TheClientDto theClientDto)
			throws RemoteException {
		BigDecimal soll = getSummeVonKonto(saldoModel.getKontoIId(),
				saldoModel.getGeschaeftsJahr() - 1, -1, BuchenFac.SollBuchung,
				null, null, true, true, theClientDto);
		BigDecimal haben = getSummeVonKonto(saldoModel.getKontoIId(),
				saldoModel.getGeschaeftsJahr() - 1, -1, BuchenFac.HabenBuchung,
				null, null, true, true, theClientDto);
		BigDecimal buchungsSaldo = soll.subtract(haben);

		Integer kostenstelleIId = getDefaultKostenstelle(
				saldoModel.getKontoIId(), theClientDto);
		if (null == kostenstelleIId) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_KEINE_KOSTENSTELLE_DEFINIERT,
					"Konto IID "
							+ saldoModel.getKontoIId()
							+ " hat (auch im Mandanten) keine Kostenstelle definiert");
		}

		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(
				saldoModel.getGeschaeftsJahr(), -1, theClientDto);
		Date gjBeginn = new Date(tVonbis[0].getTime());

		BuchungDto buchungDto = new BuchungDto();
		buchungDto.setIId(null);
		buchungDto.setAutomatischeBuchungEB(true);
		buchungDto.setAutomatischeBuchung(false);
		buchungDto.setIGeschaeftsjahr(saldoModel.getGeschaeftsJahr());
		buchungDto.setDBuchungsdatum(gjBeginn);
		buchungDto.setBelegartCNr(null);
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_EROEFFNUNG);
		buchungDto.setCBelegnummer(getTextRespectUISpr(
				"lp.finanz.saldovortrag.belegnummer",
				theClientDto.getMandant(), theClientDto.getLocMandant()));
		buchungDto.setCText(getTextRespectUISpr(
				"lp.finanz.saldovortrag.belegtext", theClientDto.getMandant(),
				theClientDto.getLocMandant()));
		buchungDto.setKostenstelleIId(kostenstelleIId);

		BuchungdetailDto detailDto = new BuchungdetailDto();
		detailDto
				.setBuchungdetailartCNr(buchungsSaldo.signum() > 0 ? SollBuchung
						: HabenBuchung);
		detailDto.setBuchungIId(null);
		detailDto.setIAusziffern(null);
		detailDto.setIAuszug(isKontoBankKonto(saldoModel.getKontoIId()) ? 0
				: null);
		detailDto.setKontoIId(saldoModel.getKontoIId());
		detailDto.setKontoIIdGegenkonto(saldoModel.getGegenkontoIId());
		detailDto.setNBetrag(buchungsSaldo.abs());
		detailDto.setNUst(BigDecimal.ZERO);

		List<BuchungdetailDto> buchungDetails = new ArrayList<BuchungdetailDto>();
		buchungDetails.add(detailDto);
		createSaldovortragsBuchungImpl(saldoModel.getKontoTyp(), buchungDto,
				buchungDetails, saldoModel.getGegenkontoIId(), theClientDto);

		return true;
	}

	public void createSaldovortragsBuchung(
			SaldovortragModelPersonenKonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException {
		validateGjForSaldovortrag(saldovortragModel.getGeschaeftsJahr(),
				theClientDto.getMandant());

		prepareGegenkonto(saldovortragModel, theClientDto);

		removeSaldovortragsBuchung(saldovortragModel, theClientDto);
		createSaldovortragsBuchungPersonenkonto(saldovortragModel, theClientDto);
		createSaldovortragsTimestamp(saldovortragModel, theClientDto);
	}

	public void createSaldovortragsBuchungErmittleOP(
			SaldovortragModelPersonenKonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException {
		validateGjForSaldovortrag(saldovortragModel.getGeschaeftsJahr(),
				theClientDto.getMandant());
		createSaldovortragsBuchungErmittleOPValidated(saldovortragModel,
				theClientDto);
	}

	protected void createSaldovortragsBuchungErmittleOPValidated(
			SaldovortragModelPersonenKonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException {

		saldovortragModel.setOPBuchungDetailsIId(getOffenePosten(
				saldovortragModel.getKontoIId(),
				saldovortragModel.getKontoTyp(),
				saldovortragModel.getGeschaeftsJahr() - 1, theClientDto));

		prepareGegenkonto(saldovortragModel, theClientDto);

		removeSaldovortragsBuchung(saldovortragModel, theClientDto);
		createSaldovortragsBuchungPersonenkonto(saldovortragModel, theClientDto);
		createSaldovortragsTimestamp(saldovortragModel, theClientDto);
	}

	public void createSaldovortragsBuchungErmittleOP(String kontotyp,
			int geschaeftsJahr, boolean deleteManualEB,
			TheClientDto theClientDto) throws RemoteException {
		validateGjForSaldovortrag(geschaeftsJahr, theClientDto.getMandant());

		KontoDto[] kontoDtos = getFinanzFac().kontoFindAllByKontotypMandant(
				kontotyp, theClientDto.getMandant());

		for (KontoDto kontoDto : kontoDtos) {
			if (kontoDto.getBAutomeroeffnungsbuchung() == 0)
				continue;

			SaldovortragModelPersonenKonto vortragModel = SaldovortragModelPersonenKonto
					.createFromKontotyp(kontotyp, geschaeftsJahr,
							kontoDto.getIId(), null);
			vortragModel.setDeleteManualEB(deleteManualEB);

			// Jede Periodenuebernahme in einer eigenen Transaktion. Das ist so
			// gewollt!
			getBuchenFac().eachCreateSaldovortragsBuchungErmittleOP(
					vortragModel, theClientDto);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void eachCreateSaldovortragsBuchungErmittleOP(
			SaldovortragModelPersonenKonto vortragModel,
			TheClientDto theClientDto) throws RemoteException {
		createSaldovortragsBuchungErmittleOPValidated(vortragModel,
				theClientDto);
	}

	protected boolean createSaldovortragsBuchungPersonenkonto(
			SaldovortragModelBase saldovortragModel, TheClientDto theClientDto)
			throws RemoteException {

		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(
				saldovortragModel.getGeschaeftsJahr(), -1, theClientDto);
		Date gjBeginn = new Date(tVonbis[0].getTime());

		BuchungDto buchungDto = null;
		List<BuchungdetailDto> buchungDetails = new ArrayList<BuchungdetailDto>();

		Integer lastBuchungId = null;
		BuchungdetailDto detail = null;
		for (Integer opDetail : saldovortragModel.getOPBuchungDetailsIId()) {
			detail = buchungdetailFindByPrimaryKey(opDetail);

			if (null == lastBuchungId
					|| (!lastBuchungId.equals(detail.getBuchungIId()))) {
				if (lastBuchungId != null) {
					createSaldovortragsBuchungImpl(
							saldovortragModel.getKontoTyp(), buchungDto,
							buchungDetails,
							saldovortragModel.getGegenkontoIId(), theClientDto);
				}

				buchungDto = buchungFindByPrimaryKey(detail.getBuchungIId());
				buchungDto.setIId(null);
				buchungDto.setAutomatischeBuchungEB(true);
				buchungDto.setIGeschaeftsjahr(saldovortragModel
						.getGeschaeftsJahr());
				buchungDto.setDBuchungsdatum(gjBeginn);
				buchungDetails = new ArrayList<BuchungdetailDto>();
				lastBuchungId = opDetail;
			}

			// Nur jene Details die mein Konto betreffen
			if (saldovortragModel.getKontoIId().equals(detail.getKontoIId())
					|| saldovortragModel.getKontoIId().equals(
							detail.getKontoIIdGegenkonto())) {
				detail.setKontoIIdGegenkonto(detail.getKontoIIdGegenkonto());
				detail.setIId(null);
				detail.setIAusziffern(null);
				detail.setIAuszug(null);
				buchungDetails.add(detail);
			}
		}

		if (buchungDetails.size() > 0) {
			createSaldovortragsBuchungImpl(saldovortragModel.getKontoTyp(),
					buchungDto, buchungDetails,
					saldovortragModel.getGegenkontoIId(), theClientDto);
		}

		return true;
	}

	// public void createSaldovortragsBuchung(
	// String kontotyp, Integer kontoIIdGegenkonto,
	// List<Integer> details, int geschaeftsjahr, TheClientDto theClientDto)
	// throws RemoteException {
	// List<Integer> ops = getOffenePosten(null, kontotyp, geschaeftsjahr,
	// theClientDto) ;
	//
	// KontoDto[] kontoDtos =
	// getFinanzFac().kontoFindAllByKontotypMandant(kontotyp,
	// theClientDto.getMandant()) ;
	// for (KontoDto kontoDto : kontoDtos) {
	// createSaldovortragsBuchung(kontoDto.getIId(), kontoIIdGegenkonto,
	// details, geschaeftsjahr, theClientDto) ;
	// }
	// }

	// TODO: OffenePosten sollte die Waehrungsausgleichskontenbetraege nicht
	// fuer den Saldo beruecksichtigen?
	/**
	 * Ermittelt die BuchungDetail-Ids jener BuchungDetail die als offene Posten
	 * betrachtet werden
	 * 
	 * @param kontoIId
	 *            die optional gesetzte (sonst null) KontoIId
	 * @param kontotypCNr
	 *            der betreffende Kontotyp (Debitor, Kreditor oder auch
	 *            Sachkonten (&auml;hm))
	 * @param geschaeftsjahr
	 * @param theClientDto
	 * @return eine (leere) Liste der noch offenen BuchungsDetails-IIds
	 */
	public List<Integer> getOffenePosten(Integer kontoIId, String kontotypCNr,
			int geschaeftsjahr, TheClientDto theClientDto) {
		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(geschaeftsjahr, -1,
				theClientDto);

		List<Integer> opDetails = new ArrayList<Integer>();
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria crit = session
					.createCriteria(FLRFinanzBuchungDetail.class);
			crit.createAlias("flrkonto", "k");
			crit.createAlias("flrbuchung", "b");
			crit.add(Restrictions.isNull("b.t_storniert"));
			crit.add(Restrictions.lt("b.d_buchungsdatum", tVonbis[1]));
			crit.add(Restrictions.eq("b.geschaeftsjahr_i_geschaeftsjahr",
					geschaeftsjahr));
			if (kontoIId != null)
				crit.add(Restrictions.eq("k.i_id", kontoIId));
			else
				crit.add(Restrictions.eq("k.kontotyp_c_nr", kontotypCNr));
			crit.addOrder(Order.asc("k.c_nr"));
			crit.addOrder(Order.asc("i_ausziffern"));
			crit.addOrder(Order.asc("b.c_belegnummer"));

			List<FLRFinanzBuchungDetail> results = crit.list();

			String belegnummer = "";
			String azk = "";

			List<Integer> groupedOpDetails = new ArrayList<Integer>();

			BigDecimal saldo = BigDecimal.ZERO;
			for (FLRFinanzBuchungDetail buchungDetail : results) {
				String t = "";
				if (buchungDetail.getI_ausziffern() != null) {
					// ausziffern hat Vorrang vor Belegnummer
					t = buchungDetail.getI_ausziffern().toString();
				} else {
					if (buchungDetail.getFlrbuchung().getFlrfbbelegart() != null)
						t += buchungDetail.getFlrbuchung().getFlrfbbelegart()
								.getC_kbez();
					t += buchungDetail.getFlrbuchung().getC_belegnummer();
				}

				if (buchungDetail.getI_ausziffern() == null ? !belegnummer
						.equals(t) : !azk.equals(t)) {
					if (groupedOpDetails.size() > 0 && saldo.signum() != 0) {
						opDetails.addAll(groupedOpDetails);
					}
					groupedOpDetails = new ArrayList<Integer>();
					saldo = BigDecimal.ZERO;
					if (buchungDetail.getI_ausziffern() == null)
						belegnummer = t;
					else
						azk = t;
				}

				if (BuchenFac.HabenBuchung.equals(buchungDetail
						.getBuchungdetailart_c_nr())) {
					saldo = saldo.add(buchungDetail.getN_betrag());
				} else {
					saldo = saldo.subtract(buchungDetail.getN_betrag());
				}

				groupedOpDetails.add(buchungDetail.getI_id());
			}

			if (groupedOpDetails.size() > 0 && saldo.signum() != 0) {
				opDetails.addAll(groupedOpDetails);
			}
		} finally {
			if (session != null)
				session.close();
		}

		return opDetails;
	}

	public boolean hatKontoBuchungen(Integer kontoIId) {
		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT COUNT(*) FROM FLRFinanzBuchungDetail o WHERE o.konto_i_id = "
					+ kontoIId + " AND o.flrbuchung.t_storniert IS NULL";
			org.hibernate.Query query = session.createQuery(queryString);
			List<?> results = query.list();
			if (results.size() != 0) {
				Long anzahl = (Long) results.get(0);
				return (anzahl.intValue() > 0);
			} else
				return false;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public boolean isUvaVerprobt(Integer buchungIId) {
		Buchung buchung = em.find(Buchung.class, buchungIId);
		return (buchung.getUvaverprobungIId() != null);
	}

	public BuchungdetailDto[] buchungdetailsFindByBuchungIIdOhneMitlaufende(
			Integer buchungIId, TheClientDto theClientDto) {
		BuchungdetailDto[] details = buchungdetailsFindByBuchungIId(buchungIId);
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		Konto konto = em.find(Konto.class, details[0].getKontoIId());
		SteuerkategorieDto[] stkDtos = getFinanzServiceFac()
				.steuerkategorieFindByFinanzamtIId(konto.getFinanzamtIId(),
						theClientDto);
		HashMap<Integer, SteuerkategorieDto> hm = new HashMap<Integer, SteuerkategorieDto>();
		for (int i = 0; i < stkDtos.length; i++) {
			hm.put(stkDtos[i].getKontoIIdForderungen(), stkDtos[i]);
			hm.put(stkDtos[i].getKontoIIdVerbindlichkeiten(), stkDtos[i]);
		}
		for (int i = 0; i < details.length; i++) {
			if (hm.get(details[i].getKontoIId()) == null)
				list.add(details[i]);
		}
		details = new BuchungdetailDto[list.size()];
		return list.toArray(details);
	}

	public void storniereFinanzamtsbuchungen(int geschaeftsjahr,
			Date buchungsDatum, int finanzamtIId, TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery(Buchung.QueryBuchungfindByGeschaeftsjahrDatumAutomatik);
		query.setParameter(Buchung.QueryParameterGeschaeftsjahr, geschaeftsjahr);
		query.setParameter(Buchung.QueryParameterBuchungsDatum, buchungsDatum);
		Collection<Buchung> cl = query.getResultList();

		Iterator<Buchung> it = cl.iterator();
		while (it.hasNext()) {
			Buchung buchung = it.next();
			BuchungdetailDto[] details = getBuchenFac()
					.buchungdetailsFindByBuchungIId(buchung.getIId());
			try {
				if (details != null && details.length > 0) {
					KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(
							details[0].getKontoIId());
					if (konto.getFinanzamtIId() == finanzamtIId
							&& konto.getMandantCNr().equals(
									theClientDto.getMandant()))
						storniereBuchung(buchung.getIId(), theClientDto);
				}
			} catch (EJBExceptionLP e) {
				myLogger.error(
						"Fehler bei Kontozuordnung auf Buchungsdetail mit id = "
								+ details[0].getIId(), e);
			} catch (RemoteException e) {
				myLogger.error(
						"Fehler bei Kontozuordnung auf Buchungsdetail mit id = "
								+ details[0].getIId(), e);
			}
		}
	}

	public boolean isKontoMitEBKonsistent(Integer kontoIId, int geschaeftsjahr,
			TheClientDto theClientDto) {
		Konto konto = em.find(Konto.class, kontoIId);
		if (konto == null)
			return false;

		if (konto.getiGeschaeftsjahrEB() == null)
			return true;
		if (konto.getiGeschaeftsjahrEB() != geschaeftsjahr)
			return true;

		Timestamp[] tVonbis = getDatumbereichPeriodeGJ(geschaeftsjahr - 1, -1,
				theClientDto);

		Session session = null;
		try {
			session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT COUNT(*) FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id= :kontoiid"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd"
					+ " AND o.flrbuchung.t_anlegen >= :pTEbAnlegen";

			org.hibernate.Query query = session.createQuery(queryString);
			query.setParameter("kontoiid", kontoIId);
			query.setParameter("pVon", tVonbis[0]);
			query.setParameter("pEnd", tVonbis[1]);
			query.setParameter("pTEbAnlegen", konto.getTEBAnlegen());

			List<?> results = query.list();
			if (results.size() != 0) {
				Long anzahl = (Long) results.get(0);
				return anzahl.longValue() == 0;
			}
		} finally {
			if (session != null)
				session.close();
		}

		return false;
	}

}
