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
package com.lp.server.system.pkgenerator.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.ejb.SequenceBelegnr;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.system.pkgenerator.format.LpMandantBelegnummerFormat;
import com.lp.server.system.pkgenerator.service.SequenceBelegnrPK;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>25. 10. 2004</I>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
@Stateless
public class BNGeneratorFacBean extends Facade implements BNGeneratorFacLocal {
	@PersistenceContext
	private EntityManager em;
	private int _retryCount;
	private SequenceBelegnr _sequenceBelegnr;

	/**
	 * Kreiert eine neue Belegnummer inkl. Primaerschluessel
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return LpBelegnummer
	 * @throws EJBExceptionLP
	 */
	public LpBelegnummer getNextBelegNr(Integer geschaeftsjahr, String name,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		return getNextBelegNr(geschaeftsjahr, name, name, mandantCNr,
				theClientDto);
	}

	/**
	 * Liefert die naechste Belegnummer aus dem aktuellen Geschaeftsjahr fuer
	 * ein Modul.
	 * 
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public LpBelegnummer getNextBelegNr(String name, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			return getNextBelegNr(
					getParameterFac().getGeschaeftsjahr(mandantCNr), name,
					mandantCNr, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Kreiert eine neue Belegnummer inkl. Primaerschluessel fuer das aktuelle
	 * Geschaeftsjahr, wobei auch mehrere verschiedene Belegnummernarten
	 * innerhalb einer Tabelle moeglich sind z.B. Rechnungen und Gutschriften
	 * werden in einer Tabelle verwaltet, aber haben einen eigenen Nummernkreis
	 * -- ----------------------------------------------------------------------
	 * ---
	 * 
	 * @param nameTabelle
	 *            String
	 * @param nameBeleg
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return LpBelegnummer
	 */
	public LpBelegnummer getNextBelegNr(String nameTabelle, String nameBeleg,
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			return getNextBelegNr(
					getParameterFac().getGeschaeftsjahr(mandantCNr),
					nameTabelle, nameBeleg, mandantCNr, theClientDto);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, ex);
		}
	}

	/**
	 * Kreiert eine neue Belegnummer inkl. Primaerschluessel, wobei auch mehrere
	 * verschiedene Belegnummernarten innerhalb einer Tabelle moeglich sind z.B.
	 * Rechnungen und Gutschriften werden in einer Tabelle verwaltet, aber haben
	 * einen eigenen Nummernkreis
	 * ------------------------------------------------
	 * ---------------------------
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param nameTabelle
	 *            String
	 * @param nameBeleg
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            String
	 * @return LpBelegnummer
	 * @throws EJBExceptionLP
	 */
	public LpBelegnummer getNextBelegNr(Integer geschaeftsjahr,
			String nameTabelle, String nameBeleg, String mandantCNr,
			TheClientDto theClientDto) {
		return getNextBelegNr(geschaeftsjahr, nameTabelle, nameBeleg,
				mandantCNr, true, theClientDto);
	}

	private LpBelegnummer getNextBelegNr(Integer geschaeftsjahr,
			String nameTabelle, String nameBeleg, String mandantCNr,
			boolean rueckdatierungPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(geschaeftsjahr + ", " + nameTabelle + ", " + nameBeleg
				+ ", " + mandantCNr + ")");
		try {
			// Pruefen, ob fuer das gewuenschte Geschaeftsjahr auch eine
			// Belegnummer generiert werden darf
			int iAktGJ = getParameterFac().getGeschaeftsjahr(mandantCNr)
					.intValue();
			// Vordatieren ins uebernaechste GJ ist nicht gestattet
			if ((iAktGJ + 1) < geschaeftsjahr.intValue()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN,
						new Exception(
								"FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN"));
			}

			Integer jahreRueckdatierbar = getMandantFac()
					.mandantFindByPrimaryKey(mandantCNr, theClientDto)
					.getJahreRueckdatierbar();
			// Rueckdatieren ins vorletzte GJ ist nicht gestattet
			if (rueckdatierungPruefen
					&& (iAktGJ - jahreRueckdatierbar) > geschaeftsjahr
							.intValue()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN,
						new Exception(
								"FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN"));
			}
			// Belegnummernobjekt mit neuem Primary Key erstellen
			LpBelegnummer bnr = new LpBelegnummer(getPKGeneratorObj()
					.getNextPrimaryKey(nameTabelle), geschaeftsjahr,
					getMandantKuerzel(mandantCNr), null);

			SequenceBelegnrPK sqbpk = new SequenceBelegnrPK(geschaeftsjahr,
					nameTabelle, mandantCNr, nameBeleg);
			_sequenceBelegnr = em.find(SequenceBelegnr.class, sqbpk);
			if (_sequenceBelegnr == null) {
				myLogger.logData("Beleg Nr Sequence not found: " + nameBeleg);
				// primaerschluesseleintrag erzeugen, falls dieser noch nicht
				// existiert
				getPKGeneratorObj().createSequenceIfNotExists(nameTabelle);
				// ist das geschaeftsjahr schon angelegt
				if (getSystemFac().geschaeftsjahrFindByPrimaryKeyOhneExc(
						geschaeftsjahr, mandantCNr) == null) {
					createGeschaeftsjahr(geschaeftsjahr, theClientDto);

					// GeschaeftsjahrMandantDto gDto = new
					// GeschaeftsjahrMandantDto();
					// // Beginndatum berechnen
					// GregorianCalendar gcBeginn = new GregorianCalendar();
					// gcBeginn.set(GregorianCalendar.DATE, 1);
					// ParametermandantDto pmBeginnMonat = null;
					// pmBeginnMonat = getParameterFac().getMandantparameter(
					// mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					// ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);
					// int beginnMonat = Integer
					// .parseInt(pmBeginnMonat.getCWert());
					// beginnMonat = beginnMonat - 1; // wegen Jaenner = 0, Feb.
					// =
					// // 1 etc.
					// gcBeginn.set(GregorianCalendar.YEAR, geschaeftsjahr);
					// gcBeginn.set(GregorianCalendar.MONTH, beginnMonat);
					// gDto.setDBeginndatum(new Timestamp(gcBeginn
					// .getTimeInMillis()));
					//
					//
					// gDto.setIGeschaeftsjahr(geschaeftsjahr);
					// gDto.setMandantCnr(mandantCNr) ;
					// gDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
					// gDto.setTAnlegen(new
					// Timestamp(System.currentTimeMillis()));
					// getSystemFac().createGeschaeftsjahr(gDto);
				}
				Integer iStartwert = new Integer(0);
				// Fuer Rechnungen und Lose kann der Startwert gesetzt werden
				// Falls das fuer weitere Belege uebersteuerbar sein soll, waers
				// besser, das in eine eigene Tabelle zu geben.
				if (nameTabelle.equals(PKConst.PK_RECHNUNG_TABELLE)
						&& nameBeleg.equals(PKConst.PK_RECHNUNG)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
									ParameterFac.PARAMETER_RECHNUNG_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_RECHNUNG_TABELLE)
						&& nameBeleg.equals("gutschrift")) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_GUTSCHRIFT,
									ParameterFac.PARAMETER_GUTSCHRIFT_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_RECHNUNG_TABELLE)
						&& nameBeleg.equals("proformare")) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_AUSGANGSRECHNUNG,
									ParameterFac.PARAMETER_PROFORMARECHNUNG_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_LIEFERSCHEIN)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_LIEFERSCHEIN,
									ParameterFac.PARAMETER_LIEFERSCHEIN_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_ANGEBOT)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_ANGEBOT,
									ParameterFac.PARAMETER_ANGEBOT_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_AUFTRAG)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_AUFTRAG,
									ParameterFac.PARAMETER_AUFTRAG_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_BESTELLUNG)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_BESTELLUNG,
									ParameterFac.PARAMETER_BESTELLUNG_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_EINGANGSRECHNUNG)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
									ParameterFac.PARAMETER_EINGANGSRECHNUNG_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;
				} else if (nameTabelle.equals(PKConst.PK_LOS)) {
					// PJ17823
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_FERTIGUNG,
									ParameterFac.PARAMETER_LOS_BELEGNUMMERSTARTWERT_FREIE_LOSE);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;

				} else if (nameTabelle.equals(PKConst.PK_PROJEKT)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_PROJEKT,
									ParameterFac.PARAMETER_PROJEKT_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;

				} else if (nameTabelle.equals(PKConst.PK_REKLAMATION)) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									mandantCNr,
									ParameterFac.KATEGORIE_REKLAMATION,
									ParameterFac.PARAMETER_REKLAMATION_BELEGNUMMERSTARTWERT);
					iStartwert = ((Integer) parameter.getCWertAsObject()) - 1;

				}
				// PJ17870
				if (iStartwert == -2) {
					// Wert des letzten Jahres holen

					int iGeschaeftjahr = geschaeftsjahr;
					int iLoop = 0;
					while (iLoop < 100) {

						iGeschaeftjahr = iGeschaeftjahr - 1;
						SequenceBelegnrPK toFind = new SequenceBelegnrPK(
								iGeschaeftjahr, nameTabelle, mandantCNr,
								nameBeleg);
						_sequenceBelegnr = em.find(SequenceBelegnr.class,
								toFind);

						if (_sequenceBelegnr == null) {
							// Noch ein Jahr zurueck
						} else {
							iStartwert = _sequenceBelegnr.getIIndex();
							break;
						}
						iLoop++;
					}
					if (iStartwert == -2) {
						iStartwert = 0;
					}

				}

				// belegnummerneintrag erzeugen
				_sequenceBelegnr = new SequenceBelegnr(geschaeftsjahr,
						iStartwert, nameTabelle, mandantCNr, nameBeleg);
				em.persist(_sequenceBelegnr);
				em.flush();
				myLogger.logData("Beleg Nr Sequence created: " + nameBeleg);
			}
			for (int retry = 0; true; retry++) {
				try {
					// und jetzt die belegnummer
					bnr.setBelegNummer(getValueAfterIncrementing(_sequenceBelegnr));
					em.merge(_sequenceBelegnr);
					em.flush();
					break;
				} catch (Exception e) {
					if (retry < _retryCount) {
						// we hit a concurrency exception, so try again...
						myLogger.logData("RETRYING");
						continue;
					} else {
						// we tried too many times, so fail...
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_PK_GENERATOR, e);
					}
				}
			}
			myLogger.logData("Next BelegNr for: " + geschaeftsjahr.intValue()
					+ " " + nameBeleg + ": " + bnr.getBelegNummer());
			return bnr;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	public Integer getValueAfterIncrementing(SequenceBelegnr _sequenceBelegnr) {
		_sequenceBelegnr.setIIndex(new Integer(_sequenceBelegnr.getIIndex()
				.intValue() + 1));
		return _sequenceBelegnr.getIIndex();
	}

	public Integer getBelegAnzahl(Integer geschaeftsjahr, String name,
			String mandantCNr) throws EJBExceptionLP {
		SequenceBelegnr sb = null;
		sb = em.find(SequenceBelegnr.class, new SequenceBelegnrPK(
				geschaeftsjahr, name, mandantCNr, name));
		if (sb == null)
			return new Integer(0);
		else
			return sb.getIIndex();
	}

	/**
	 * Liefert die Anzahl aller Belege einer "Belegart" fuer einen Mandanten
	 * 
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getBelegAnzahl(String name, String mandantCNr)
			throws EJBExceptionLP {
		SequenceBelegnr sb = null;
		try {
			Query query = em
					.createNamedQuery("SequenceBelegnrfindByNameMandant");
			query.setParameter(1, name);
			query.setParameter(2, mandantCNr);
			Collection<?> cl = query.getResultList();
			Iterator<?> it = cl.iterator();
			int summe = 0;
			while (it.hasNext()) {
				sb = (SequenceBelegnr) it.next();
				summe = summe + sb.getIIndex().intValue();
			}
			return new Integer(summe);
		} catch (Exception ex) {
			return new Integer(0);
		}
	}

	/**
	 * Liefert die Anzahl aller Belege in einer Tabelle
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 * @throws EJBExceptionLP
	 */
	public Integer getBelegAnzahl(String name) throws EJBExceptionLP {
		SequenceBelegnr sb = null;
		try {
			Query query = em.createNamedQuery("SequenceBelegnrfindByName");
			query.setParameter(1, name);
			Collection<?> cl = query.getResultList();
			Iterator<?> it = cl.iterator();
			int summe = 0;
			while (it.hasNext()) {
				sb = (SequenceBelegnr) it.next();
				summe = summe + sb.getIIndex().intValue();
			}
			return new Integer(summe);
		} catch (Exception ex) {
			return new Integer(0);
		}
	}

	/**
	 * Holt das Kuerzel eines Mandanten, das in die Belegnummer eingebaut werden
	 * soll
	 * 
	 * @param mandantCNr
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	private String getMandantKuerzel(String mandantCNr) throws EJBExceptionLP {
		String mk = null;
		try {
			ParametermandantDto pm = getParameterFac().getMandantparameter(
					mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG);
			mk = pm.getCWert().trim();
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception("Anwenderparameter "
							+ ParameterFac.KATEGORIE_ALLGEMEIN + "."
							+ ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG
							+ " kann nicht gefunden werden !"));
		}
		return mk;
		/**
		 * @todo bei Bedarf implementieren PJ 3849 dafuer muss wahrscheinlich
		 *       das Datenmodell d. Mandanten angepasst werden!?
		 */
	}

	public LpBelegnummerFormat getBelegnummernFormat(String mandantCNr)
			throws EJBExceptionLP {
		try {
			
			System.out.println("XX");
			ParametermandantDto pStellenGJ = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);
			ParametermandantDto pTrennzeichen = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
			ParametermandantDto pStellenBelegnummer = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
			ParametermandantDto pStellenZufall = getParameterFac()
					.getMandantparameter(
							mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_ZUFALL);
			ParameteranwenderDto parameteranwenderDto = getParameterFac()
					.getAnwenderparameter(
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.ANWENDERPARAMETER_BELEGNUMMER_MIT_MANDANTKENNUNG);
			if (Integer.parseInt(parameteranwenderDto.getCWert().trim()) == 0) {
				return new LpDefaultBelegnummerFormat(
						Integer.parseInt(pStellenGJ.getCWert()), pTrennzeichen
								.getCWert().toCharArray()[0],
						Integer.parseInt(pStellenBelegnummer.getCWert()),
						Integer.parseInt(pStellenZufall.getCWert()));
			} else {
				parameteranwenderDto = getParameterFac()
						.getAnwenderparameter(
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.ANWENDERPARAMETER_BELEGNUMMER_STELLEN_MANDANTKENNUNG);
				return new LpMandantBelegnummerFormat(
						Integer.parseInt(pStellenGJ.getCWert()), pTrennzeichen
								.getCWert().toCharArray()[0],
						Integer.parseInt(parameteranwenderDto.getCWert()),
						Integer.parseInt(pStellenBelegnummer.getCWert()),
						Integer.parseInt(pStellenZufall.getCWert()));
			}
		} catch (NumberFormatException ex) {
			// Standard-Belenummernformat: 2007/1234567
			return new LpDefaultBelegnummerFormat(4, '/', 7, 0);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, ex);
		}
	}

	/**
	 * Kreiert eine neue Belegnummer inkl. Prim&auml;schl&uuml;ssel, wobei auch
	 * mehrere verschiedene Belegnummernarten innerhalb einer Tabelle
	 * m&ouml;glich sind z.B. Rechnungen und Gutschriften werden in einer
	 * Tabelle verwaltet, aber haben einen eigenen Nummernkreis
	 * ------------------------------------------------
	 * ---------------------------
	 * 
	 * @param geschaeftsjahr
	 *            Integer
	 * @param name
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return LpBelegnummer
	 * @throws EJBExceptionLP
	 */
	@Override
	public LpBelegnummer getNextBelegNrFinanz(Integer geschaeftsjahr,
			String name, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getNextBelegNr(geschaeftsjahr, name, name, mandantCNr, false,
				theClientDto);
	}

	private void createGeschaeftsjahr(Integer geschaeftsjahr,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		int beginnMonat = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT).asInteger() - 1;
		boolean plusEins = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_GESCHAEFTSJAHRPLUSEINS).asBoolean();

		int kalenderjahr = geschaeftsjahr;
		if (plusEins) {
			--kalenderjahr;
		}

		GregorianCalendar gcBeginn = new GregorianCalendar();
		gcBeginn.set(GregorianCalendar.DATE, 1);
		gcBeginn.set(GregorianCalendar.YEAR, kalenderjahr);
		gcBeginn.set(GregorianCalendar.MONTH, beginnMonat);

		GeschaeftsjahrMandantDto gDto = new GeschaeftsjahrMandantDto();
		gDto.setDBeginndatum(new Timestamp(gcBeginn.getTimeInMillis()));

		gDto.setIGeschaeftsjahr(geschaeftsjahr);
		gDto.setMandantCnr(theClientDto.getMandant());
		gDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		gDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		getSystemFac().createGeschaeftsjahr(gDto);
	}
}
