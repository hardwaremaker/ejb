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
package com.lp.server.rechnung.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import com.lp.server.rechnung.ejbfac.RechnungFacBean;
import com.lp.util.Helper;

public class HelperWiederholendeRechnung {

	RechnungDto[] rechnungDtos;
	Date startDatum;
	String intervallArt;
	Locale localeKunde;
	String sMandant;
	
	ArrayList<RechnungInfo> al = new ArrayList<RechnungInfo>();
	RechnungFacBean callback;

	public HelperWiederholendeRechnung(RechnungFacBean callback, RechnungDto[] rechnungDtos, 
			Date startDatum, String intervallArt, Locale localeKunde, String sMandant) {
		super();
		this.callback = callback;
		this.rechnungDtos = rechnungDtos;
		this.startDatum = startDatum;
		this.intervallArt = intervallArt;
		this.localeKunde = localeKunde;
		this .sMandant = sMandant;
	}
	
	public int init(java.sql.Date dateVorfaelligkeit) {
		Date datum = startDatum;
		Date aktDatum = Helper.cutDate(dateVorfaelligkeit);
//		SimpleDateFormat dateformat = new SimpleDateFormat(
//				"MMMM yyyy", new Locale("de","AT"));
		while (datum.compareTo(aktDatum) <= 0) {
			RechnungInfo ri = new RechnungInfo(datum);
//			String sollText = "Rechnung fuer: " + dateformat.format(datum) + "\n";
			String sollText = callback.getKopftextForIntervall(this.intervallArt, datum, localeKunde, sMandant).toString();
			sollText = sollText + "\n";
			ri.solltext = sollText.replace("\n", "");
			RechnungDto rDto = findText(rechnungDtos, sollText);
			if (rDto != null) {
				ri.istDatum = new Date(rDto.getTBelegdatum().getTime());
				ri.verrechnet = true;
				ri.rechnungtext = rDto.getCKopftextuebersteuert().replace("\n", "");
				Integer[] anzahl = anzahlText(rechnungDtos, sollText);
				ri.anzahl = anzahl[0];
				ri.anzahlStorniert = anzahl[1];
			}
			al.add(ri);
			datum = callback.berechneinterval(intervallArt, datum);
				
		}
		return al.size();
	}
	
	public int anzahlNichtVerrechnet() {
		int anzahl = 0;
		for (int i=0; i<al.size(); i++) {
			if (!al.get(i).verrechnet)
				anzahl++;
		}
		return anzahl;
	}
	
	public Iterator<RechnungInfo> iteratorZuVerrechnen() {
		return new Iterator<RechnungInfo>() {
			int pointer = 0;
			ArrayList<RechnungInfo> asRi = getZuVerrechnen();

			public boolean hasNext() {
				if (asRi == null)
					return false;
				else
					return (pointer < asRi.size());
			}

			public RechnungInfo next() {
				if (asRi == null) {
					throw new NoSuchElementException();
				}
				return asRi.get(pointer++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	protected ArrayList<RechnungInfo> getZuVerrechnen() {
		ArrayList<RechnungInfo> as = new ArrayList<RechnungInfo>();
		Iterator<RechnungInfo> it = this.al.iterator();
		while (it.hasNext()) {
			RechnungInfo ri = it.next();
			if (!ri.verrechnet)
				as.add(ri);
		}
		return as;
	}
	
	private RechnungDto findText(RechnungDto[] rechnungDtos, String text) {
		RechnungDto r = null;
		for (int i=0; i<rechnungDtos.length; i++) {
			r = rechnungDtos[i];
			if (r.getCKopftextuebersteuert().startsWith(text)) {
				return r;
			}
		}
		return null;
	}
	
	private Integer[] anzahlText(RechnungDto[] rechnungDtos, String text) {
		RechnungDto r = null;
		Integer[] anzahl = new Integer[2];
		anzahl[0] = new Integer(0);
		anzahl[1] = new Integer(0);
		for (int i=0; i<rechnungDtos.length; i++) {
			r = rechnungDtos[i];
			if (r.getCKopftextuebersteuert().equals(text)) {
				anzahl[0]++;
				if (r.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT))
					anzahl[1]++;
			}
		}
		return anzahl;
	}
	
	public Date neuesDatumfuerIntervall() {
		Date lastDate = null;
		for (int i=al.size()-1; i>=0; i--) {
			if (al.get(i).verrechnet && al.get(i).anzahl > al.get(i).anzahlStorniert) {
				lastDate = al.get(i).sollDatum;
				break;
			}
		}
		if (lastDate == null)
			return Helper.cutDate(new java.sql.Date(System.currentTimeMillis()));
		else
			return callback.berechneinterval(this.intervallArt, lastDate);
	}
	
	public class RechnungInfo {
		public Date sollDatum;
		public Date istDatum;
		public boolean verrechnet;
		public String rechnungtext;
		public String solltext;
		public int anzahl;
		public int anzahlStorniert;
		
		public RechnungInfo(Date sollDatum) {
			this.sollDatum = sollDatum;
			this.verrechnet = false;
			this.anzahl = 0;
			this.anzahlStorniert = 0;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Solldatum: " + (sollDatum == null ? "null" : sollDatum.toString()) + ";");
			sb.append("Istdatum: " + (istDatum == null ? "null" : istDatum.toString()) + ";");
			sb.append("Verrechnet: " + new Boolean(verrechnet).toString() + ";");
			sb.append("Text: " + (rechnungtext == null ? "null" : rechnungtext) + ";");
			sb.append("Solltext: " + (solltext == null ? "null" : solltext) + ";");
			sb.append("Anzahl: " + anzahl + ";");
			sb.append("Storno: " + anzahlStorniert + ";");
			return sb.toString();
		}
		
	}
}
