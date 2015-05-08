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
package com.lp.server.artikel.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Gestehungspreis Calculator
 * 
 * wird mit den Daten aus einem Hibernate-SQL initialisiert Achtung: die
 * Feldreihenfolge muss stimmen!
 * 
 * recalc() ... die Gestehungspreise neu berechnet Iterator ... liefert einen
 * Iterator auf die Update-SQL Commands
 * 
 * Beispiel SQL f&uuml;r Hibernate:
 * "select  I_ID, B_ABGANG, SUM(N_MENGE) as N_MENGE, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT "
 * + "from WW_LAGERBEWEGUNG " +
 * "where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=0 and B_HISTORIE=0 " +
 * "group by LAGER_I_ID, T_BUCHUNGSZEIT, I_ID, B_ABGANG, N_EINSTANDSPREIS, N_GESTEHUNGSPREIS "
 * + "union " +
 * "select I_ID, B_ABGANG, SUM(U.N_VERBRAUCHTEMENGE) as MENGE, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS, LAGER_I_ID, T_BUCHUNGSZEIT "
 * +
 * "from WW_LAGERBEWEGUNG B inner join WW_LAGERABGANGURSPRUNG U on U.I_LAGERBEWEGUNGID = B.I_ID_BUCHUNG "
 * + " where LAGER_I_ID=? and ARTIKEL_I_ID=? and B_ABGANG=1 and B_HISTORIE=0 " +
 * "group by LAGER_I_ID, B.T_BUCHUNGSZEIT, B.I_ID, B_ABGANG, N_EINSTANDSPREIS, U.N_GESTEHUNGSPREIS "
 * + "order by LAGER_I_ID, T_BUCHUNGSZEIT, B_ABGANG";
 * 
 * &Auml;nderung zur Vermeidung von falscher Berechnung bei negativem Lagerstand:
 * Aufgrund der Tatsache das Abbuchungen keine Uhrzeit im Belegdatum haben kann
 * es zu einem Lagerwert trotz Lagermenge 0 kommen. Daher wird bei Lagermenge 0
 * auch der Lagerwert auf 0 gesetzt. Zus&auml;tzlich wird falls der neu berechnete
 * Gestehungspreis negativ werden sollte der vorherige Wert verwendet.
 * 
 * @author Adi
 * 
 */
public class ArtikelGestehungspreisCalc {

	private int iScale = 4;
	private int iMAX_PRECISION = 15;
	private ArrayList<ArrayList<ArtikelStatistik>> aaslager = new ArrayList<ArrayList<ArtikelStatistik>>();
	private ArrayList<BigDecimal> anGestehungspreisNeu = new ArrayList<BigDecimal>();
	private boolean isCalculated = false;

	private final static String sCSV_HEADER = "id,Abgang,Menge,Einstand,Gestpreis,Lager,Neu,Diff,Menge_kumm,Wert_kumm,forceUpd,needPreis\r\n";

	/**
	 * Konstruktor mit Statistikdaten
	 * 
	 * @param data
	 *            Hibernate Resultlist
	 */
	public ArtikelGestehungspreisCalc(List<?> data) {
		ArrayList<ArtikelStatistik> aas = null;
		Iterator<?> it = data.iterator();
		Integer lagerid = null;
		while (it.hasNext()) {
			Object[] as = (Object[]) it.next();
			if (lagerid == null) {
				lagerid = (Integer) as[5];
				aas = new ArrayList<ArtikelStatistik>();
				aaslager.add(aas);
			} else if (lagerid.compareTo((Integer) as[5]) != 0) {
				lagerid = (Integer) as[5];
				aas = new ArrayList<ArtikelStatistik>();
				aaslager.add(aas);
			}
			aas.add(new ArtikelStatistik(as));
		}
		isCalculated = false;
	}

	private ArrayList<Integer> getAlleLagerId(List<?> data) {
		Iterator<?> it = data.iterator();
		Integer lagerid = null;
		ArrayList<Integer> alager = new ArrayList<Integer>();
		while (it.hasNext()) {
			Object[] as = (Object[]) it.next();
			if (lagerid == null) {
				lagerid = (Integer) as[5];
				alager.add(lagerid);
			} else if (lagerid.compareTo((Integer) as[5]) != 0) {
				lagerid = (Integer) as[5];
				alager.add(lagerid);
			}
		}
		return alager;
	}

	/**
	 * Berechnung aller Gestehungspreise eines Artikels f&uuml;r alle Lager
	 * 
	 * @return true bei Erfolg
	 */
	public boolean doRecalc() {
		return doRecalc(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
	}

	/**
	 * Berechnung aller Gestehungspreise eines Artikels mit Anfangswerten
	 * 
	 * @param nMengeAnfang
	 * @param nWertAnfang
	 * @param nGestPreisAnfang
	 * @return true bei Erfolg
	 */
	public boolean doRecalc(BigDecimal nMengeAnfang, BigDecimal nWertAnfang,
			BigDecimal nGestPreisAnfang) {
		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			doRecalcEinLager(nMengeAnfang, nWertAnfang, nGestPreisAnfang, aas);
		}
		isCalculated = true;
		return true;
	}

	/**
	 * Berechnung aller Gestehungspreise eines Artikels f&uuml;r ein Lager mit
	 * Anfangswerten
	 * 
	 * @param lagerIId
	 * @param nMengeAnfang
	 * @param nWertAnfang
	 * @param nGestPreisAnfang
	 * @return true bei Erfolg false wenn die Daten mehrere Lager beinhalten
	 *         oder falsches Lager gew&auml;hlt wurde
	 */
	public boolean doRecalc(Integer lagerIId, BigDecimal nMengeAnfang,
			BigDecimal nWertAnfang, BigDecimal nGestPreisAnfang) {
		if (getLageranzahl() > 1) // nur erlaubt bei Daten fuer ein Lager
			return false;

		if (aaslager.size() > 0) {
			ArrayList<ArtikelStatistik> aas = aaslager.get(0);
			if (aas.get(0).iLagerId.compareTo(lagerIId) != 0) // nur das
																// gewaehlte
																// Lager ist
																// erlaubt
				return false;

			doRecalcEinLager(nMengeAnfang, nWertAnfang, nGestPreisAnfang, aas);
		}
		isCalculated = true;
		return true;
	}

	private void doRecalcEinLager(BigDecimal nMengeAnfang,
			BigDecimal nWertAnfang, BigDecimal nGestPreisAnfang,
			ArrayList<ArtikelStatistik> aas) {
		Iterator<ArtikelStatistik> it = aas.iterator();
		// BigDecimal nMengeKummuliert = (nWertAnfang.doubleValue()==0 ? new
		// BigDecimal(0) : nMengeAnfang);
		// Test bs_bestellung geht nicht mehr da lagerstand mit wert 0 vorhanden
		BigDecimal nMengeKummuliert = nMengeAnfang;
		BigDecimal nWertKummuliert = nWertAnfang;
		BigDecimal nGestehungspreis = nGestPreisAnfang;
		BigDecimal nGestehungspreisNeu = nGestPreisAnfang;

		// wenn kein Gestehungspreis vorhanden und die Buchungen beginnen mit
		// einem Abgang
		// dann wird der Gestehungspreis mit dem Abgang initialisiert
		if (nGestehungspreis.doubleValue() == 0) {
			if (hasDataError()) {
				while (it.hasNext()) {
					ArtikelStatistik as = it.next();
					if (!as.bAbgang)
						break;
					if (as.nGestehungspreis != null
							&& as.nGestehungspreis.doubleValue() > 0) {
						nGestehungspreis = as.nGestehungspreis;
						break;
					}
				}
			}
		}

		it = aas.iterator();
		while (it.hasNext()) {
			ArtikelStatistik as = it.next();
			if (as.bAbgang) {
				// Abbuchung: Lagerwert und Lagermenge vermindern
				nMengeKummuliert = nMengeKummuliert.subtract(as.nMenge);
				nWertKummuliert = nWertKummuliert.subtract(as.nMenge
						.multiply(nGestehungspreis));
				as.nGestehungspreisBerechnet = nGestehungspreis;
			} else {
				// Zubuchung: Lagerwert und Lagermenge erhoehen
				BigDecimal nMengeKummuliertAlt = nMengeKummuliert;
				nMengeKummuliert = nMengeKummuliert.add(as.nMenge);
				nWertKummuliert = nWertKummuliert.add(as.nMenge
						.multiply(as.nEinstandspreis));
				// es gibt noch eine Lagermenge -> neuen Gestehungspreis
				// berechnen
				if (nMengeKummuliert.doubleValue() != 0) {
					if (nMengeKummuliert.doubleValue() > 0
							&& nMengeKummuliertAlt.doubleValue() < 0) {
						// bei Nulldurchgang der Menge von - auf + wird der
						// Gestehungspreis auf
						// Einstandpreis gesetzt und der Lagerwert entsprechend
						// berechnet
						nGestehungspreisNeu = as.nEinstandspreis;
						nWertKummuliert = nMengeKummuliert
								.multiply(nGestehungspreisNeu);
					} else {
						nGestehungspreisNeu = nWertKummuliert.divide(
								nMengeKummuliert, iScale,
								BigDecimal.ROUND_HALF_EVEN);
					}
				}

				// wenn neu berechneter Wert positiv dann wird dies der neue
				// Gestehungspreis
				// 2009-11-24 auch bei 0 updaten
				if (nGestehungspreisNeu.doubleValue() >= 0) {
					nGestehungspreis = nGestehungspreisNeu;
				}
				as.nGestehungspreisBerechnet = nGestehungspreis.setScale(iScale);
			}
			// wenn Lagermenge 0 ist auch den Lagerwert auf 0 setzen
			// (wegen Rundungsfehlern und Rueckdatierung)
			if (nMengeKummuliert.doubleValue() == 0) {
				nWertKummuliert = new BigDecimal(0);
				// wenn zusaetzlich auch der Gestehungspreis noch 0 dann auf
				// Einstand setzen
				// (z.B. Lieferschein vordatiert vor Losablieferung)
				if (nGestehungspreis.signum() == 0) {
					if ((as.nEinstandspreis != null)
							&& (as.nEinstandspreis.doubleValue() != 0)) {
						nGestehungspreis = as.nEinstandspreis;
						as.nGestehungspreisBerechnet = nGestehungspreis.setScale(iScale);
						if (hasDataError())
							as.bForceUpdate = true;
					}
				}
			}
			/*
			 * geht nicht so wegen Zubuchung mit 0-Preis!!! // wenn Lagerwert 0
			 * ist auch die Lagermenge auf 0 setzen if
			 * (nWertKummuliert.doubleValue() == 0) { nMengeKummuliert = new
			 * BigDecimal(0); // wenn es eine vordatierte Abbuchung ist so muss
			 * der Gestehungspreis aus der Zubuchung geholt werden if
			 * (as.bAbgang && hasDataError()) if
			 * (as.nGestehungspreisBerechnet.compareTo(nGestehungspreis) != 0)
			 * as.bNeedPreis = true; }
			 */
			as.nMengeKummuliert = nMengeKummuliert;
			as.nWertKummuliert = nWertKummuliert;
		}
		// Gestehungspreis je Lager merken
		anGestehungspreisNeu.add(nGestehungspreis);
	}

	/**
	 * Anzahl der Lager in den Daten
	 * 
	 * @return die Anzahl der Lager
	 */
	public int getLageranzahl() {
		return aaslager.size();
	}

	/**
	 * Datenfehlerpr&uuml;fung zur Zeit wird nur gepr&uuml;ft ob die Daten mit einem
	 * Zugang beginnen
	 * 
	 * @return true bei Fehler
	 */
	public boolean hasDataError() {
		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			if (aas.get(0).bAbgang)
				return true;
		}
		return false;
	}

	/**
	 * Datenfehlerpr&uuml;fung f&uuml;r ein Lager zur Zeit wird nur gepr&uuml;ft ob die Daten
	 * mit einem Zugang beginnen
	 * 
	 * @param nummer
	 *            Nummer des Lagers in den Daten (Achtung: nicht die IId)
	 * @return true bei Fehler
	 */
	public boolean hasDataError(int nummer) {
		ArrayList<ArtikelStatistik> aas = aaslager.get(nummer);
		if (aas.get(0).bAbgang) {
			return true;
		}
		return false;
	}

	/**
	 * &UUml;berpr&uuml;fung ob einer der Gestehungspreise falsch ist
	 * 
	 * @return true bei Unterschied
	 */
	public boolean hasGestPreisDiff() {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		boolean bDiff = false;
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			Iterator<ArtikelStatistik> it = aas.iterator();
			while (it.hasNext()) {
				if (it.next().isChanged()) {
					bDiff = true;
					break;
				}
			}
		}
		return bDiff;
	}

	/**
	 * &UUml;berpr&uuml;fung ob einer der Gestehungspreise f&uuml;r ein Lager falsch ist
	 * 
	 * @param nummer
	 *            Nummer des Lagers in den Daten (Achtung: nicht die IId)
	 * @return true bei Fehler
	 */
	public boolean hasGestPreisDiff(int nummer) {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		boolean bDiff = false;
		ArrayList<ArtikelStatistik> aas = aaslager.get(nummer);
		Iterator<ArtikelStatistik> it = aas.iterator();
		while (it.hasNext()) {
			if (it.next().isChanged()) {
				bDiff = true;
				break;
			}
		}
		return bDiff;
	}

	/**
	 * Maximale Differenz des Gestehungspreis abrufen
	 * 
	 * @return maximale Differenz des Gestehungspreises
	 */
	public BigDecimal getMaxDiff() {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		BigDecimal maxdiff = new BigDecimal(0);
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			Iterator<ArtikelStatistik> it = aas.iterator();
			while (it.hasNext()) {
				ArtikelStatistik as = it.next();
				if (as.isChanged()) {
					if (as.getDiff().abs().compareTo(maxdiff.abs()) == 1)
						maxdiff = as.getDiff();
				}
			}
		}
		return maxdiff;
	}

	/**
	 * Maximale Differenz des Gestehungspreis f&uuml;r ein Lager abrufen
	 * 
	 * @param nummer
	 *            Nummer des Lagers in den Daten (Achtung: nicht die IId)
	 * @return maximale Differenz des Gestehungspreises f&uuml;r das angegebene Lager
	 */
	public BigDecimal getMaxDiff(int nummer) {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		BigDecimal maxdiff = new BigDecimal(0);
		ArrayList<ArtikelStatistik> aas = aaslager.get(nummer);
		Iterator<ArtikelStatistik> it = aas.iterator();
		while (it.hasNext()) {
			ArtikelStatistik as = it.next();
			if (as.isChanged()) {
				if (as.getDiff().abs().compareTo(maxdiff.abs()) == 1)
					maxdiff = as.getDiff();
			}
		}
		return maxdiff;
	}

	public BigDecimal getGestehungspreisNeu() {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");
		if (anGestehungspreisNeu != null && anGestehungspreisNeu.size() > 0) {
			return anGestehungspreisNeu.get(0);
		} else {
			return null;
		}
	}

	public BigDecimal getGestehungspreisNeu(int nummer) {
		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		return anGestehungspreisNeu.get(nummer);
	}

	/**
	 * LagerIId f&uuml;r Lagernummer
	 * 
	 * @param nummer
	 * @return die IId der angefragten Lagernummer
	 */
	public Integer getLagerIId(int nummer) {
		if (nummer > aaslager.size())
			return null;
		return aaslager.get(nummer).get(0).iLagerId;
	}

	public String toString(int lager) {
		StringBuffer sb = new StringBuffer();
		ArrayList<ArtikelStatistik> aas = aaslager.get(lager);
		Iterator<ArtikelStatistik> it = aas.iterator();
		while (it.hasNext()) {
			sb.append(it.next().toString() + "\r\n");
		}
		return sb.toString();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		int anzahl = this.getLageranzahl();
		for (int i = 0; i < anzahl; i++) {
			sb.append(this.toString(i));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	private class ArtikelStatistik {
		public Integer iId;
		public boolean bAbgang;
		public BigDecimal nMenge;
		public BigDecimal nEinstandspreis;
		public BigDecimal nGestehungspreis;
		public Integer iLagerId;
		public BigDecimal nGestehungspreisBerechnet;
		public BigDecimal nMengeKummuliert;
		public BigDecimal nWertKummuliert;
		public boolean bForceUpdate = false; // Update der Abgaenge eines Zugangs
												// erzwingen
		public boolean bNeedPreis = false; // Gestehungspreis eines Abgangs aus
											// dem Zugang holen

		ArtikelStatistik(Object[] data) {
			iId = (Integer) data[0];
			bAbgang = ((Short) data[1] == 1);
			nMenge = (BigDecimal) data[2];
			nEinstandspreis = (BigDecimal) data[3];
			nGestehungspreis = (BigDecimal) data[4];
			iLagerId = (Integer) data[5];
		}

		public boolean isChanged() {
			if (bForceUpdate)
				return true;
			if (bNeedPreis)
				return true;
			if (nGestehungspreis == null)
				return true;
			if (nGestehungspreisBerechnet == null)
				return false;
			return (nGestehungspreis.compareTo(nGestehungspreisBerechnet) != 0);
		}

		public BigDecimal getDiff() {
			if ((nGestehungspreis == null) && !bAbgang)
				return new BigDecimal(0);
			else if (nGestehungspreis == null)
				return nGestehungspreisBerechnet;
			else
				return nGestehungspreis.subtract(nGestehungspreisBerechnet);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();

			sb.append(iId.toString() + "\t");
			sb.append(new Boolean(bAbgang).toString() + "\t");
			sb.append(nMenge == null ? "\t" : nMenge.toString() + "\t");
			sb.append(nEinstandspreis == null ? "\t" : nEinstandspreis
					.toString() + "\t");
			sb.append(nGestehungspreis == null ? "\t" : nGestehungspreis
					.toString() + "\t");
			sb.append(iLagerId == null ? "\t" : iLagerId.toString() + "\t");
			sb.append(nGestehungspreisBerechnet == null ? "\t"
					: nGestehungspreisBerechnet.toString() + "\t");
			sb.append(new Boolean(isChanged()).toString() + "\t");
			sb.append(nMengeKummuliert == null ? "\t" : nMengeKummuliert + "\t");
			sb.append(nWertKummuliert == null ? "\t" : nWertKummuliert + "\t");
			sb.append(new Boolean(bForceUpdate).toString() + "\t");
			sb.append(new Boolean(bNeedPreis).toString());
			return sb.toString();
		}
	}

	/**
	 * Iterator f&uuml;r Update-SQL Statements
	 * 
	 * @return den Iterator
	 */
	public Iterator<String> iteratorSql() {
		return new Iterator<String>() {
			int pointer = 0;
			ArrayList<String> asSql = getSQL();

			public boolean hasNext() {
				if (asSql == null)
					return false;
				else
					return (pointer < asSql.size());
			}

			public String next() {
				if (asSql == null) {
					throw new NoSuchElementException();
				}
				return asSql.get(pointer++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private ArrayList<String> getSQL() {
		if (aaslager == null || aaslager.size() == 0) {
			return null;
		}

		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		ArrayList<String> tmp = new ArrayList<String>();
		StringBuffer sSql = new StringBuffer();
		StringBuffer sSqlForce = new StringBuffer();
		StringBuffer sSqlAbgang = new StringBuffer();
		BigDecimal nGestPreis = null;
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			Iterator<ArtikelStatistik> it = aas.iterator();

			while (it.hasNext()) {
				ArtikelStatistik as = it.next();
				if (as.isChanged()) {
					if (nGestPreis == null) {
						nGestPreis = as.nGestehungspreisBerechnet;
						if (as.bNeedPreis) {
							// Abgang braucht den Gestehungspreis aus der
							// Zubuchung
							sSqlAbgang = sSqlAbgang.append(as.iId);
						} else {
							sSql = sSql.append(as.iId);
							if ((as.bAbgang == false) && as.bForceUpdate)
								// die Zubuchung muss auch vordatierte
								// Ablieferungen updaten
								sSqlForce = sSqlForce.append(as.iId);
						}
					} else if (nGestPreis
							.compareTo(as.nGestehungspreisBerechnet) == 0) {
						if (as.bNeedPreis) {
							// Abgang braucht den Gestehungspreis aus der
							// Zubuchung
							if (sSqlAbgang.length() == 0)
								sSqlAbgang = sSqlAbgang.append(as.iId);
							else
								sSqlAbgang = sSqlAbgang.append("," + as.iId);
						} else {
							if (sSql.length() == 0)
								sSql = sSql.append(as.iId);
							else
								sSql = sSql.append("," + as.iId);
							if ((as.bAbgang == false) && as.bForceUpdate) {
								// die Zubuchung muss auch vordatierte
								// Ablieferungen updaten
								if (sSqlForce.length() == 0)
									sSqlForce = sSqlForce.append(as.iId);
								else
									sSqlForce = sSqlForce.append("," + as.iId);
							}
						}
					} else {
						add2Update(tmp, sSql, nGestPreis);
						add2UpdateAblieferung(tmp, sSqlForce, nGestPreis);
						add2UpdateAbgang(tmp, sSqlAbgang);
						nGestPreis = as.nGestehungspreisBerechnet;
						sSql = new StringBuffer();
						sSqlForce = new StringBuffer();
						sSqlAbgang = new StringBuffer();
						if (as.bNeedPreis) {
							// Abgang braucht den Gestehungspreis aus der
							// Zubuchung
							sSqlAbgang = sSqlAbgang.append(as.iId);
						} else {
							sSql = sSql.append(as.iId.toString());
							if ((as.bAbgang == false) && as.bForceUpdate)
								// die Zubuchung muss auch vordatierte
								// Ablieferungen updaten
								sSqlForce = sSqlForce.append(as.iId);
						}
					}

				}
			}
		}
		if (sSql.length() > 0) {
			add2Update(tmp, sSql, nGestPreis);
		}
		if (sSqlForce.length() > 0) {
			add2UpdateAblieferung(tmp, sSqlForce, nGestPreis);
		}
		if (sSqlAbgang.length() > 0) {
			add2UpdateAbgang(tmp, sSqlAbgang);
		}
		return tmp;
	}

	private void add2Update(ArrayList<String> tmp, StringBuffer sSql,
			BigDecimal nGestPreis) {
		if (sSql.length() != 0) {
			StringBuffer sTemp = new StringBuffer(sSql.toString());
			sSql = sSql.insert(0,
					"UPDATE WW_LAGERBEWEGUNG SET N_GESTEHUNGSPREIS="
							+ nGestPreis.toString() + " WHERE I_ID IN (");
			sSql = sSql.append(");");
			tmp.add(sSql.toString());

			sTemp = sTemp
					.insert(0,
							"UPDATE WW_LAGERABGANGURSPRUNG SET N_GESTEHUNGSPREIS="
									+ nGestPreis.toString()
									+ " WHERE I_LAGERBEWEGUNGID IN (SELECT I_ID_BUCHUNG FROM WW_LAGERBEWEGUNG WHERE I_ID IN(");
			sTemp = sTemp.append("));");
			tmp.add(sTemp.toString());
		}
	}

	private void add2UpdateAblieferung(ArrayList<String> tmp,
			StringBuffer sSql, BigDecimal nGestPreis) {
		if (sSql.length() != 0) {
			sSql = sSql
					.insert(0,
							"UPDATE WW_LAGERABGANGURSPRUNG SET N_GESTEHUNGSPREIS="
									+ nGestPreis.toString()
									+ " WHERE I_LAGERBEWEGUNGIDURSPRUNG IN (SELECT I_ID_BUCHUNG FROM WW_LAGERBEWEGUNG WHERE I_ID IN(");
			sSql = sSql.append("));");
			tmp.add(sSql.toString());
		}
	}

	private void add2UpdateAbgang(ArrayList<String> tmp, StringBuffer sSql) {
		if (sSql.length() != 0) {
			String[] s = sSql.toString().split(",");
			for (int i = 0; i < s.length; i++) {
				String sUpd = "UPDATE WW_LAGERABGANGURSPRUNG SET N_GESTEHUNGSPREIS="
						+ "(SELECT N_GESTEHUNGSPREIS FROM WW_LAGERBEWEGUNG WHERE B_HISTORIE=0 AND I_ID_BUCHUNG = WW_LAGERABGANGURSPRUNG.I_LAGERBEWEGUNGIDURSPRUNG) "
						+ "WHERE I_LAGERBEWEGUNGID = (SELECT I_ID_BUCHUNG FROM WW_LAGERBEWEGUNG WHERE I_ID="
						+ s[i] + ");";
				tmp.add(sUpd + "\n");
			}
		}
	}

	/**
	 * Update SQL in Datei schreiben
	 * 
	 * @param filename
	 * @return true wenn das Update-Script in die Datei geschrieben werden konnte
	 */
	public boolean saveUpdateSQL(String filename) {
		if (filename == null || filename.length() == 0)
			return false;

		Iterator<String> it = this.iteratorSql();
		String sql = null;
		File f = new File(filename);
		try {
			FileOutputStream fo = new FileOutputStream(f);
			try {
				while (it.hasNext()) {
					sql = it.next() + "\r\n";
					fo.write(sql.getBytes());
				}
				fo.flush();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Daten f&uuml;r ein Lager in CSV-File schreiben
	 * 
	 * @param lagernummer
	 * @param filename
	 * @return wenn das angegebene Lager in die Datei geschrieben werden konnte
	 */
	public boolean save2Csv(int lagernummer, String filename) {
		if (filename == null || filename.length() == 0)
			return false;
		if (lagernummer > aaslager.size())
			return false;

		File f = new File(filename);
		try {
			FileOutputStream fo = new FileOutputStream(f);
			try {
				String s = this.toString(lagernummer);
				s = s.replace("\t", ",");
				fo.write(sCSV_HEADER.getBytes("UTF-8"));
				fo.write(s.getBytes("UTF-8"));
				fo.flush();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Daten f&uuml;r alle Lager in CSV-File schreiben _lagernummer wird an den
	 * Filename angeh&auml;ngt
	 * 
	 * @param filename
	 * @return true wenn die Daten erfolgreich geschrieben werden konnten
	 */
	public boolean save2Csv(String filename) {
		if (filename == null || filename.length() == 0)
			return false;

		int anzahl = this.getLageranzahl();
		for (int i = 0; i < anzahl; i++) {
			int j = filename.indexOf(".", 1);
			String filetmp = filename.substring(0, j) + "_" + i
					+ filename.substring(j);
			File f = new File(filetmp);
			try {
				FileOutputStream fo = new FileOutputStream(f);
				try {
					String s = this.toString(i);
					s = s.replace("\t", ",");
					fo.write(sCSV_HEADER.getBytes("UTF-8"));
					fo.write(s.getBytes("UTF-8"));
					fo.flush();
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * Info &uuml;ber Fehler in File schreiben
	 * 
	 * @param filename
	 * @return true wenn die Datei gespeichert werden konnte
	 */
	public boolean saveInfo(String filename) {
		if (filename == null || filename.length() == 0)
			return false;

		File f = new File(filename);
		try {
			FileOutputStream fo = new FileOutputStream(f);
			try {
				fo.write(getInfo().getBytes("UTF-8"));
				fo.flush();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Info &uuml;ber Fehler als String
	 * 
	 * @return Info &uuml;ber Fehler
	 */
	public String getInfo() {
		StringBuffer sb = new StringBuffer();
		int anzahl = this.getLageranzahl();
		for (int i = 0; i < anzahl; i++) {
			sb.append("Lager" + i + " (IId:" + getLagerIId(i) + ")\r\n");
			sb.append("\tDatenfehler:\t"
					+ (this.hasDataError(i) ? "Ja" : "Nein") + "\r\n");
			sb.append("\tGestehungspreisfehler:\t"
					+ (this.hasGestPreisDiff(i) ? "Ja" : "Nein") + "\r\n");
			sb.append("\tMax. Differnz:\t" + this.getMaxDiff(i).toString()
					+ "\r\n");
		}
		return sb.toString();
	}

	public boolean hasOverflow() {

		if (!isCalculated)
			throw new IllegalStateException("Recalc ist notwendig");

		Iterator<ArrayList<ArtikelStatistik>> itlager = aaslager.iterator();
		boolean bOverflow = false;
		while (itlager.hasNext()) {
			ArrayList<ArtikelStatistik> aas = itlager.next();
			Iterator<ArtikelStatistik> it = aas.iterator();
			while (it.hasNext()) {
				BigDecimal x = it.next().nGestehungspreisBerechnet;
				if (x.precision() > iMAX_PRECISION) {
					bOverflow = true;
					break;
				}
			}
		}
		return bOverflow;
	}
}
