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
package com.lp.server.eingangsrechnung.ejbfac;


import com.lp.server.eingangsrechnung.ejb.*;
import com.lp.server.eingangsrechnung.service.*;
import com.lp.server.system.service.*;
import com.lp.server.util.*;
import com.lp.util.*;
import java.util.*;

import org.hibernate.*;
import com.lp.server.util.fastlanereader.*;
import com.lp.server.eingangsrechnung.fastlanereader.generated.*;
import org.hibernate.criterion.*;
import com.lp.server.system.pkgenerator.PKConst;
import java.rmi.*;
import java.sql.Date;
import java.math.*;
import com.lp.server.finanz.service.*;
import com.lp.server.eingangsrechnung.bl.*;


import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ZahlungsvorschlagFacBean
extends Facade implements ZahlungsvorschlagFac {
	@PersistenceContext
	private EntityManager em;



	public Integer createZahlungsvorschlag(ZahlungsvorschlagkriterienDto krit,
			TheClientDto theClientDto)
	throws EJBExceptionLP {
		Integer iZViid = null;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLREingangsrechnungReport.class);
			// Es werden alle offenen ERs dieses Mandanten ausgewertet.
			// 1. Filter nach Mandant
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
					theClientDto.getMandant()));
			// 2. Filter nach Status
			Collection<String>  collStati = new LinkedList<String>();
			collStati.add(EingangsrechnungFac.STATUS_ANGELEGT);
			collStati.add(EingangsrechnungFac.STATUS_TEILBEZAHLT);
			c.add(Restrictions.in(EingangsrechnungFac.FLR_ER_STATUS_C_NR, collStati));
			// Abfragen
			List<?> list = c.list();
			if (list.size() > 0) {
				// wenn da welche daherkommen, mach ich einen neuen lauf
				ZahlungsvorschlaglaufDto zvlaufDto = new ZahlungsvorschlaglaufDto();
				zvlaufDto.setBankverbindungIId(krit.getBankvertbindungIId());
				zvlaufDto.setBMitskonto(Helper.boolean2Short(krit.isBMitSkonto()));
				zvlaufDto.setISkontoueberziehungsfristintagen(krit.
						getISkontoUeberziehungsfristInTagen());
				zvlaufDto.setMandantCNr(theClientDto.getMandant());
				zvlaufDto.setTNaechsterzahlungslauf(krit.getDNaechsterZahlungslauf());
				zvlaufDto.setTZahlungsstichtag(krit.getDZahlungsstichtag());
				iZViid = createZahlungsvorschlaglauf(zvlaufDto, theClientDto);
				// Zahlungsziele cachen
				final Map<Integer, ZahlungszielDto> mZahlungsziele = (Map<Integer, ZahlungszielDto>) getMandantFac().zahlungszielFindAllByMandantAsDto(
						theClientDto.getMandant(), theClientDto);
				for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
					FLREingangsrechnungReport e = (FLREingangsrechnungReport) iter.next();
					// Offenen Werte ermitteln
					BigDecimal bdBruttowertFW = e.getN_betragfw();
					BigDecimal bdBruttooffenFW = bdBruttowertFW.
					subtract(getEingangsrechnungFac().getBezahltBetragFw(e.getI_id(), null));
					// Wenn noch was offen ist
					if (bdBruttooffenFW.compareTo(new BigDecimal(0)) > 0) {
						// Faelligkeitsdatum berechnen
						// das ist grundsaetzlich Freigabedatum + Zahlungsziel (sofern definiert)
						java.util.Date dFaellig = e.getT_freigabedatum();
						// Netto faellig - ebenfalls plus Zahlungsziel (sofern definiert)
						java.util.Date dFaelligNetto = e.getT_freigabedatum();
						int iZielTage = 0;
						BigDecimal bdAngewandterSkontoSatz = new BigDecimal(0);
						if (e.getZahlungsziel_i_id() != null) {
							ZahlungszielDto zzDto = mZahlungsziele.get(e.getZahlungsziel_i_id());
							// mit Skonto zahlen
							if (krit.isBMitSkonto()) {
								// Skontoueberziehungstage einrechnen
								dFaellig = Helper.addiereTageZuDatum(dFaellig,
										krit.getISkontoUeberziehungsfristInTagen());
								boolean bSkontoGehtSichAus = false;

								// schaun, ob sich der erste Skonto noch ausgeht
								if (zzDto.getSkontoAnzahlTage1() != null &&
										zzDto.getSkontoProzentsatz1() != null) {
									// Bei diesem Datum waere mit Skonto1 zu zahlen
									java.util.Date dFaelligBeiSkonto1 = Helper.addiereTageZuDatum(dFaellig,
											zzDto.getSkontoAnzahlTage1());
									// wenn das spaetestens heute ist -> anwenden
									if (dFaelligBeiSkonto1.compareTo(Helper.cutDate(getDate())) >= 0) {
										bSkontoGehtSichAus = true;
										bdAngewandterSkontoSatz = zzDto.getSkontoProzentsatz1();
										iZielTage = zzDto.getSkontoAnzahlTage1();
									}
								}
								// wenn nicht, dann schaun, ob sich der zweite ausgeht
								if (!bSkontoGehtSichAus &&
										zzDto.getSkontoAnzahlTage2() != null &&
										zzDto.getSkontoProzentsatz2() != null) {
									// Bei diesem Datum waere mit Skonto2 zu zahlen
									java.util.Date dFaelligBeiSkonto2 = Helper.addiereTageZuDatum(dFaellig,
											zzDto.getSkontoAnzahlTage2());
									// wenn das spaetestens heute ist -> anwenden
									if (dFaelligBeiSkonto2.compareTo(Helper.cutDate(getDate())) >= 0) {
										bSkontoGehtSichAus = true;
										bdAngewandterSkontoSatz = zzDto.getSkontoProzentsatz2();
										iZielTage = zzDto.getSkontoAnzahlTage2();
									}
								}
								// wenn auch das nicht gegangen ist, muss sowieso netto gezahlt werden
								if (!bSkontoGehtSichAus &&
										zzDto.getAnzahlZieltageFuerNetto() != null) {
									iZielTage = zzDto.getAnzahlZieltageFuerNetto();
								}
							}
							// nicht mit Skonto zahlen -> nettotage
							else {
								if (zzDto.getAnzahlZieltageFuerNetto() != null) {
									iZielTage = zzDto.getAnzahlZieltageFuerNetto();
								}
							}
							// Nettofaelligkeitsdatum
							if (zzDto.getAnzahlZieltageFuerNetto() != null) {
								dFaelligNetto = Helper.addiereTageZuDatum(dFaelligNetto, zzDto.getAnzahlZieltageFuerNetto());
							}
						}
						// Zieltage addieren
						dFaellig = Helper.addiereTageZuDatum(dFaellig, iZielTage);
						// wenn vor dem naechsten Zahlungslauf faellig, dann speichern
						if (dFaellig.before(krit.getDNaechsterZahlungslauf())) {
							// vorher noch den Skontosatz anwenden
							BigDecimal bdMultiplikator = new BigDecimal(1).subtract(
									bdAngewandterSkontoSatz.movePointLeft(2));
							bdBruttooffenFW = bdBruttooffenFW.multiply(bdMultiplikator).setScale(
									FinanzFac.NACHKOMMASTELLEN, RoundingMode.HALF_UP);
							// Dto zusammenbauen
							ZahlungsvorschlagDto zvDto = new ZahlungsvorschlagDto();
							zvDto.setBBezahlen(Helper.boolean2Short(true));
							zvDto.setEingangsrechnungIId(e.getI_id());
							zvDto.setNAngewandterskontosatz(bdAngewandterSkontoSatz);
							zvDto.setNOffen(bdBruttooffenFW);
							zvDto.setTFaellig(new java.sql.Date(dFaellig.getTime())); // in sql-date umwandeln
							zvDto.setZahlungsvorschlaglaufIId(iZViid);
							// Eintrag speichern
							createZahlungsvorschlag(zvDto);
						}
					}
				}
			}
		}
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
		return iZViid;
	}


	/**
	 * Einen ganzen Zahlungsvorschlaglauf loeschen.
	 * @param zahlungsvorschlaglaufIId Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void removeZahlungsvorschlaglauf(Integer zahlungsvorschlaglaufIId, TheClientDto theClientDto)
	throws EJBExceptionLP {
		myLogger.warn(theClientDto.getIDUser(), "removeZahlungsvorschlaglauf:" + zahlungsvorschlaglaufIId);
		Session session = null;
		try {
			// Zuerst alle zugehoerigen Eintraege suchen.
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRZahlungsvorschlag.class);
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID,
					zahlungsvorschlaglaufIId));
			List<?> list = c.list();
			// und alle loeschen.
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
				FLRZahlungsvorschlag zv=(FLRZahlungsvorschlag)iter.next();
				removeZahlungsvorschlag(zv.getI_id());
			}
			//  jetzt den Kopfdatensatz loeschen.
			removeZahlungsvorschlaglauf(zahlungsvorschlaglaufIId);
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}


	private Integer createZahlungsvorschlaglauf(ZahlungsvorschlaglaufDto
			zahlungsvorschlaglaufDto, TheClientDto theClientDto)
	throws EJBExceptionLP {
		// PK generieren
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ZAHLUNGSVORSCHLAGLAUF);
		zahlungsvorschlaglaufDto.setIId(iId);
		// Anleger
		zahlungsvorschlaglaufDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		try {
			Zahlungsvorschlaglauf zahlungsvorschlaglauf = new Zahlungsvorschlaglauf(zahlungsvorschlaglaufDto.getIId(), zahlungsvorschlaglaufDto.getMandantCNr(),zahlungsvorschlaglaufDto.getTZahlungsstichtag(),zahlungsvorschlaglaufDto.getTNaechsterzahlungslauf(),zahlungsvorschlaglaufDto.getBMitskonto(),zahlungsvorschlaglaufDto.getISkontoueberziehungsfristintagen(),zahlungsvorschlaglaufDto.getBankverbindungIId(),zahlungsvorschlaglaufDto.getPersonalIIdAnlegen());
			em.persist(zahlungsvorschlaglauf);
  em.flush();
			zahlungsvorschlaglaufDto.setTAnlegen(zahlungsvorschlaglauf.getTAnlegen());
			setZahlungsvorschlaglaufFromZahlungsvorschlaglaufDto(zahlungsvorschlaglauf,
					zahlungsvorschlaglaufDto);
		}
		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return iId;
	}


	private void removeZahlungsvorschlaglauf(Integer iId)
	throws EJBExceptionLP {
		//   try {
Zahlungsvorschlaglauf toRemove = em.find(Zahlungsvorschlaglauf.class, iId);
if (toRemove == null) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
}
try {
  em.remove(toRemove);
  em.flush();
} catch (EntityExistsException er) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
}
		//   }
		//   catch (RemoveException e) {
		//    throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		//  }
	}


	public ZahlungsvorschlaglaufDto zahlungsvorschlaglaufFindByPrimaryKey(Integer iId)
	throws EJBExceptionLP {
		//    try {
		Zahlungsvorschlaglauf zahlungsvorschlaglauf = em.find(Zahlungsvorschlaglauf.class, iId);
		if(zahlungsvorschlaglauf==null){ // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZahlungsvorschlaglaufDto(zahlungsvorschlaglauf);
		//{ 
		//     throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
		//}
		// }
		//  catch (javax.ejb.ObjectNotFoundException e) {
		//     throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		//     }
		//     catch (FinderException e) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		//     }
	}


	private void setZahlungsvorschlaglaufFromZahlungsvorschlaglaufDto(Zahlungsvorschlaglauf zahlungsvorschlaglauf, ZahlungsvorschlaglaufDto zahlungsvorschlaglaufDto) {
		zahlungsvorschlaglauf.setMandantCNr(zahlungsvorschlaglaufDto.getMandantCNr());
		zahlungsvorschlaglauf.setTAnlegen(zahlungsvorschlaglaufDto.getTAnlegen());
		zahlungsvorschlaglauf.setTZahlungsstichtag(zahlungsvorschlaglaufDto.
				getTZahlungsstichtag());
		zahlungsvorschlaglauf.setTNaechsterzahlungslauf(zahlungsvorschlaglaufDto.
				getTNaechsterzahlungslauf());
		zahlungsvorschlaglauf.setBMitskonto(zahlungsvorschlaglaufDto.getBMitskonto());
		zahlungsvorschlaglauf.setISkontoueberziehungsfristintagen(zahlungsvorschlaglaufDto.
				getISkontoueberziehungsfristintagen());
		zahlungsvorschlaglauf.setBankverbindungIId(zahlungsvorschlaglaufDto.
				getBankverbindungIId());
		zahlungsvorschlaglauf.setPersonalIIdAnlegen(zahlungsvorschlaglaufDto.
				getPersonalIIdAnlegen());
		em.merge(zahlungsvorschlaglauf);
  em.flush();
	}


	private ZahlungsvorschlaglaufDto assembleZahlungsvorschlaglaufDto(Zahlungsvorschlaglauf
			zahlungsvorschlaglauf) {
		return ZahlungsvorschlaglaufDtoAssembler.createDto(zahlungsvorschlaglauf);
	}


	private void createZahlungsvorschlag(ZahlungsvorschlagDto zahlungsvorschlagDto)
	throws EJBExceptionLP {
		// PK generieren
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ZAHLUNGSVORSCHLAG);
		zahlungsvorschlagDto.setIId(iId);
		try {
			Zahlungsvorschlag zahlungsvorschlag = new Zahlungsvorschlag(zahlungsvorschlagDto.getIId(), zahlungsvorschlagDto.getZahlungsvorschlaglaufIId(),zahlungsvorschlagDto.getEingangsrechnungIId(),zahlungsvorschlagDto.getBBezahlen(), zahlungsvorschlagDto.getTFaellig(),zahlungsvorschlagDto.getNAngewandterskontosatz(),zahlungsvorschlagDto.getNOffen());
			em.persist(zahlungsvorschlag);
  em.flush();
			setZahlungsvorschlagFromZahlungsvorschlagDto(zahlungsvorschlag,
					zahlungsvorschlagDto);
		}
		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}


	private void removeZahlungsvorschlag(Integer iId)
	throws EJBExceptionLP {
		// try {
Zahlungsvorschlag toRemove = em.find(Zahlungsvorschlag.class, iId);
if (toRemove == null) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
}
try {
  em.remove(toRemove);
  em.flush();
} catch (EntityExistsException er) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
}
		//   }
		//   catch (RemoveException e) {
		//    throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		//  }
	}


	public ZahlungsvorschlagDto zahlungsvorschlagFindByPrimaryKey(Integer iId)
	throws EJBExceptionLP{
		//    try {
		Zahlungsvorschlag zahlungsvorschlag = em.find(Zahlungsvorschlag.class, iId);
		if(zahlungsvorschlag == null){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleZahlungsvorschlagDto(zahlungsvorschlag);
		//{ 
		//   throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		//}
		//  }
		//  catch (javax.ejb.ObjectNotFoundException e) {
		//   throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		//     }
		//     catch (FinderException e) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		//     }
	}


	public ZahlungsvorschlagDto[] zahlungsvorschlagFindByZahlungsvorschlaglaufIId(Integer
			zahlungsvorschlaglaufIId)
	throws EJBExceptionLP {
		//    try {
		Query query = em.createNamedQuery("ZahlungsvorschlagfindByZahlungsvorschlaglaufIId");
		query.setParameter(1, zahlungsvorschlaglaufIId);
		Collection<?>  zahlungsvorschlag =  query.getResultList();
		//if(zahlungsvorschlag.isEmpty()){
		//	throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
		//}
		return assembleZahlungsvorschlagDtos(zahlungsvorschlag);
		//{ 
		//    throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		//}
		//  }
		//  catch (javax.ejb.ObjectNotFoundException e) {
		//    throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		//     }
		//     catch (FinderException e) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		//     }
	}


	private void setZahlungsvorschlagFromZahlungsvorschlagDto(Zahlungsvorschlag zahlungsvorschlag, ZahlungsvorschlagDto zahlungsvorschlagDto) {
		zahlungsvorschlag.setZahlungsvorschlaglaufIId(zahlungsvorschlagDto.
				getZahlungsvorschlaglaufIId());
		zahlungsvorschlag.setEingangsrechnungIId(zahlungsvorschlagDto.getEingangsrechnungIId());
		zahlungsvorschlag.setBBezahlen(zahlungsvorschlagDto.getBBezahlen());
		zahlungsvorschlag.setTFaellig(zahlungsvorschlagDto.getTFaellig());
		zahlungsvorschlag.setNAngewandterskontosatz(zahlungsvorschlagDto.
				getNAngewandterskontosatz());
		zahlungsvorschlag.setNOffen(zahlungsvorschlagDto.getNOffen());
		em.merge(zahlungsvorschlag);
  em.flush();
	}


	private ZahlungsvorschlagDto assembleZahlungsvorschlagDto(Zahlungsvorschlag
			zahlungsvorschlag) {
		return ZahlungsvorschlagDtoAssembler.createDto(zahlungsvorschlag);
	}


	private ZahlungsvorschlagDto[] assembleZahlungsvorschlagDtos(Collection<?> 
			zahlungsvorschlags) {
		List<ZahlungsvorschlagDto> list = new ArrayList<ZahlungsvorschlagDto>();
		if (zahlungsvorschlags != null) {
			Iterator<?> iterator = zahlungsvorschlags.iterator();
			while (iterator.hasNext()) {
				Zahlungsvorschlag zahlungsvorschlag = (Zahlungsvorschlag) iterator.next();
				list.add(assembleZahlungsvorschlagDto(zahlungsvorschlag));
			}
		}
		ZahlungsvorschlagDto[] returnArray = new ZahlungsvorschlagDto[list.size()];
		return (ZahlungsvorschlagDto[]) list.toArray(returnArray);
	}


	/**
	 * Flag "Bezahlen" auf diesem Datensatz invertieren.
	 *
	 * @param zahlungsvorschlagIId Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void toggleZahlungsvorschlagBBezahlen(Integer zahlungsvorschlagIId,
			TheClientDto theClientDto)
	throws EJBExceptionLP {
		//    try {
		Zahlungsvorschlag zv = em.find(Zahlungsvorschlag.class, zahlungsvorschlagIId);
		if (zv == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		zv.setBBezahlen(Helper.boolean2Short(!Helper.short2Boolean(zv.getBBezahlen())));
		// }
		//  catch (javax.ejb.ObjectNotFoundException e) {
		//    throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		//     }
		//     catch (FinderException e) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		//     }
	}


	public BigDecimal getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(Integer
			zahlungsvorschlagleufIId, TheClientDto theClientDto)
	throws EJBExceptionLP {
		BigDecimal bdWert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRZahlungsvorschlag.class);
			// Filter nach ZV Lauf
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ZV_ZAHLUNGSVORSCHLAGLAUF_I_ID,
					zahlungsvorschlagleufIId));
			// Filter B_BEZAHLEN = true
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ZV_B_BEZAHLEN,
					Helper.boolean2Short(true)));
			// Abfragen
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); ) {
				FLRZahlungsvorschlag zv = (FLRZahlungsvorschlag) iter.next();
				BigDecimal bdWertZV = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						zv.getN_offen(),
						zv.getFlreingangsrechnungreport().getWaehrung_c_nr(),
						theClientDto.getSMandantenwaehrung(), 
						new Date(System.currentTimeMillis()),
						theClientDto);
				bdWert = bdWert.add(bdWertZV);
			}
		}
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
		return bdWert;
	}


	public String exportiereZahlungsvorschlaglauf(Integer zahlungsvorschlagleufIId,
			TheClientDto theClientDto)
	throws EJBExceptionLP {
		ZahlungsvorschlagExportFormatter em = ZahlungsvorschlagExportFormatterFactory.
		getZahlungsvorschlagExportFormatter(getExportFormat(theClientDto), theClientDto);
		return exportiereZV(em, zahlungsvorschlagleufIId, theClientDto);
	}


	private String exportiereZV(ZahlungsvorschlagExportFormatter em, Integer zahlungsvorschlagleufIId,
			TheClientDto theClientDto)
	throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		ZahlungsvorschlaglaufDto zvLauf = zahlungsvorschlaglaufFindByPrimaryKey(zahlungsvorschlagleufIId);
		ZahlungsvorschlagDto[] zv = zahlungsvorschlagFindByZahlungsvorschlaglaufIId(zahlungsvorschlagleufIId);
		//    sExport.append(em.exportiereUeberschriftPersonenkonten(theClientDto));
		if (zv == null || zv.length == 0) {
			return null;
		}
		sExport.append(em.exportiereDaten(zvLauf, zv, theClientDto));
		return sExport.toString();
	}


	private int getExportFormat(TheClientDto theClientDto)
	throws EJBExceptionLP {
		return ZahlungsvorschlagFac.FORMAT_CSV;
	}


	public ZahlungsvorschlagDto updateZahlungsvorschlag(ZahlungsvorschlagDto
			zahlungsvorschlagDto, TheClientDto theClientDto)
	throws EJBExceptionLP {
		if (zahlungsvorschlagDto != null) {
			Integer iId = zahlungsvorschlagDto.getIId();
			//      try {
			Zahlungsvorschlag zahlungsvorschlag = em.find(Zahlungsvorschlag.class, iId);
			if (zahlungsvorschlag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
			}
			setZahlungsvorschlagFromZahlungsvorschlagDto(zahlungsvorschlag,
					zahlungsvorschlagDto);
		}
		//   catch (javax.ejb.ObjectNotFoundException e) {
		//     throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		//       }
		//       catch (FinderException e) {
		//         throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		//       }
		// }
		return zahlungsvorschlagDto;
	}
}

