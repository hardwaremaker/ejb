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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ArtikelFilterComboBoxEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String filterExpression = null;
	
	private String filterExpressionOhneKlammern = null;

	public String getFilterExpressionOhneKlammern() {
		return filterExpressionOhneKlammern;
	}

	public void setFilterExpressionOhneKlammern(String filterExpressionOhneKlammern) {
		this.filterExpressionOhneKlammern = filterExpressionOhneKlammern;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	public String getCbez() {
		return cbez;
	}

	public void setCbez(String cbez) {
		this.cbez = cbez;
	}

	public ArrayList<ArtikelFilterComboBoxEntry> getUntergruppen() {
		return untergruppen;
	}

	public void setUntergruppen(
			ArrayList<ArtikelFilterComboBoxEntry> untergruppen) {
		this.untergruppen = untergruppen;
	}

	private String cnr = null;
	private String cbez = null;

	private ArrayList<ArtikelFilterComboBoxEntry> untergruppen = null;

	public static LinkedHashMap getSortierteListe(
			ArrayList<ArtikelFilterComboBoxEntry> al, boolean bSortiertNachCNr) {
		return getSortierteListe(al, bSortiertNachCNr, 0);
	}

	private static LinkedHashMap getSortierteListe(
			ArrayList<ArtikelFilterComboBoxEntry> al, boolean bSortiertNachCNr,
			int iEbene) {

		String einrueckung = "";
		for (int i = 0; i < iEbene; i++) {
			einrueckung += "   ";
		}

		// Jede Ebene sortieren
		for (int i = al.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				ArtikelFilterComboBoxEntry o = al.get(j);
				ArtikelFilterComboBoxEntry o1 = al.get(j + 1);

				if (bSortiertNachCNr) {
					if (o.getCnr().compareTo(o1.getCnr()) > 0) {
						al.set(j, o1);
						al.set(j + 1, o);

					}
				} else {
					if (o.getCbez().compareTo(o1.getCbez()) > 0) {
						al.set(j, o1);
						al.set(j + 1, o);

					}
				}

			}
		}

		LinkedHashMap lhmGesamt = new LinkedHashMap();

		for (int i = 0; i < al.size(); i++) {

			ArtikelFilterComboBoxEntry zeile = al.get(i);

			if (bSortiertNachCNr) {
				lhmGesamt.put(zeile.getFilterExpression(),
						einrueckung + zeile.getCnr());
			} else {
				lhmGesamt.put(zeile.getFilterExpression(),
						einrueckung + zeile.getCbez());
			}

			if (zeile.getUntergruppen() != null
					&& zeile.getUntergruppen().size() > 0) {
				LinkedHashMap lhmSub = getSortierteListe(
						zeile.getUntergruppen(), bSortiertNachCNr, iEbene + 1);
				Iterator it = lhmSub.keySet().iterator();
				while (it.hasNext()) {

					String key = (String) it.next();

					lhmGesamt.put(key, lhmSub.get(key));

				}

			}

		}

		return lhmGesamt;
	}

}
