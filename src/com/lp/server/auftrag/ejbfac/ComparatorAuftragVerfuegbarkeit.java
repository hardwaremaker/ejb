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
package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.util.Comparator;

import com.lp.server.auftrag.service.ReportAuftragVerfuegbarkeitDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Sortierung der Verfuegbarkeitspruefung.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 24.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:57:17 $
 */
public class ComparatorAuftragVerfuegbarkeit implements Comparator<Object> {
	/**
	 * Tatsaechliche Wiederbeschaffungszeit ermitteln</br>
	 * <p>wbz = dto.Wbzeit + dto.Durchlaufzeit
	 * @param dto
	 * @return die tats&auml;chliche Wiederbeschaffungszeit bzw. null wenn sowohl die
	 *   Wiederbeschaffungszeit als auch die Durchlaufzeit null sind.
	 */
	private BigDecimal getWbz(ReportAuftragVerfuegbarkeitDto dto) {
		if(dto.getArtikellieferantDto() == null) return null ;
		
		if(dto.getArtikellieferantDto().getIWiederbeschaffungszeit() == null) {
			return null ;
		}
		
		return new BigDecimal(dto.getArtikellieferantDto().getIWiederbeschaffungszeit()) ;
	}
	
	
	private int compareDz(ReportAuftragVerfuegbarkeitDto a, ReportAuftragVerfuegbarkeitDto b) {
		BigDecimal dzA = a.getBdDefaultdurchlaufzeit() ;
		BigDecimal dzB = b.getBdDefaultdurchlaufzeit() ;
		if(dzA == null && dzB == null) {
			return 0 ;
		}
		if(dzA == null) return -1 ;
		if(dzB == null) return 1 ;
		return dzB.compareTo(dzA) ;
	}
	
	/**
	 * Wiederbeschaffungszeit sortieren
	 * 
	 * @param a ist die Wbz A
	 * @param b ist die Wbz B
	 * @return 0 wenn sowohl a als auch b null sind (oder a.compareTo(b)), 
	 *   ansonsten wird eine WBZ == null als "sehr gro&szlig;" betrachtet.  
	 */
	private int compareWbz(ReportAuftragVerfuegbarkeitDto a, ReportAuftragVerfuegbarkeitDto b) {
		BigDecimal wbzA = getWbz(a) ;
		BigDecimal wbzB = getWbz(b) ;
		if(wbzA == null && wbzB == null) {
			return compareDz(a, b) ;
		}
		if(wbzA == null) return -1 ;
		if(wbzB == null) return 1 ;
		int result = wbzB.compareTo(wbzA);
		if(result == 0) {
			result = compareDz(a, b) ;
		}
		return result ;
	}
	
	public int compare(Object a, Object b) {
		ReportAuftragVerfuegbarkeitDto avDto1 = (ReportAuftragVerfuegbarkeitDto) a;
		ReportAuftragVerfuegbarkeitDto avDto2 = (ReportAuftragVerfuegbarkeitDto) b;		
		
		if(avDto1.isBLagernd() && avDto2.isBLagernd()) {
			return compareWbz(avDto1, avDto2) ;
		}

		if(!avDto1.isBLagernd() && !avDto2.isBLagernd()) {
			return compareWbz(avDto1, avDto2) ;			
		}

		if(avDto1.isBLagernd()) return 1 ;
		return -1 ;
 	}
	
	public int compareOld(Object a, Object b) {
		ReportAuftragVerfuegbarkeitDto avDto1 = (ReportAuftragVerfuegbarkeitDto) a;
		ReportAuftragVerfuegbarkeitDto avDto2 = (ReportAuftragVerfuegbarkeitDto) b;
		BigDecimal iWiederbeschaffungszeitTats1 = null;
		// Tatsaechliche WB-Zeit = WB-Zeit + Durchlaufzeit
		if (avDto1.getArtikellieferantDto() != null
				&& avDto1.getArtikellieferantDto().getIWiederbeschaffungszeit() != null) {
			iWiederbeschaffungszeitTats1 = new BigDecimal(avDto1
					.getArtikellieferantDto().getIWiederbeschaffungszeit());
		}
		if (avDto1.getBdDefaultdurchlaufzeit() != null) {
			if (iWiederbeschaffungszeitTats1 == null) {
				iWiederbeschaffungszeitTats1 = avDto1
						.getBdDefaultdurchlaufzeit();
			} else {
				iWiederbeschaffungszeitTats1 = iWiederbeschaffungszeitTats1
						.add(avDto1.getBdDefaultdurchlaufzeit());
			}
		}
		BigDecimal iWiederbeschaffungszeitTats2 = null;
		// Tatsaechliche WB-Zeit = WB-Zeit + Durchlaufzeit
		if (avDto2.getArtikellieferantDto() != null
				&& avDto2.getArtikellieferantDto().getIWiederbeschaffungszeit() != null) {
			iWiederbeschaffungszeitTats2 = new BigDecimal(avDto2
					.getArtikellieferantDto().getIWiederbeschaffungszeit());
		}
		if (avDto2.getBdDefaultdurchlaufzeit() != null) {
			if (iWiederbeschaffungszeitTats2 == null) {
				iWiederbeschaffungszeitTats2 = avDto2
						.getBdDefaultdurchlaufzeit();
			} else {
				iWiederbeschaffungszeitTats2 = iWiederbeschaffungszeitTats2
						.add(avDto2.getBdDefaultdurchlaufzeit());
			}
		}
		// Positionen mit hoeherer Wiederbeschaffungszeit stehen weiter oben.
		// Ist diese nicht definiert, kommt die Zeile ganz nach oben.
		int iCompWiederbeschaffungszeit;
		if (avDto1.isBLagernd()) {
			// der 1. ist eh lagernd -> nach unten.
			iCompWiederbeschaffungszeit = 1;
		} else if (avDto2.isBLagernd()) {
			// der 2. ist eh lagernd -> nach unten.
			iCompWiederbeschaffungszeit = -1;
		} else if (iWiederbeschaffungszeitTats1 == null) {
			// erster nicht definiert -> dieser kommt nach oben.
			iCompWiederbeschaffungszeit = -1;
		} else if (iWiederbeschaffungszeitTats2 == null) {
			// zweiter nicht definiert -> dieser kommt nach oben.
			iCompWiederbeschaffungszeit = 1;
		} else {
			// beide sind definiert -> direkter vergleich.
			// die mit einer kuerzeren Wiederbeschaffungszeit stehen weiter
			// unten.
			iCompWiederbeschaffungszeit = iWiederbeschaffungszeitTats2
					.compareTo(iWiederbeschaffungszeitTats1);
		}
		return iCompWiederbeschaffungszeit;
	}
}
