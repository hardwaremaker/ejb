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
package com.lp.server.fertigung.service;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.lp.service.DiagrammDto;

/**
 * <p>
 * Diese Klasse haelt die Daten einer Kapazitaetsvorschau
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 24.11.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:57:12 $
 */
public class KapazitaetsvorschauDto extends DiagrammDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<KapazitaetsvorschauDetailDto>[][] details = null;
	private BigDecimal[][] bdVerfuegbareStunden = null;
	private String[] sSpaltenueberschrift = null;
	private String[] sZeilen = null;

	public KapazitaetsvorschauDto(int iGruppen, int iSpalten) {
		details = new LinkedList[iGruppen][iSpalten];
		bdVerfuegbareStunden = new BigDecimal[iGruppen][iSpalten];
		sSpaltenueberschrift = new String[iSpalten];
		sZeilen = new String[iGruppen];
		for (int i = 0; i < details.length; i++) {
			for (int j = 0; j < details[i].length; j++) {
				details[i][j] = new LinkedList<KapazitaetsvorschauDetailDto>();
				bdVerfuegbareStunden[i][j] = new BigDecimal(0);
			}
		}
	}

	public LinkedList<KapazitaetsvorschauDetailDto>[][] getDetails() {
		return details;
	}

	public void addDetail(int iGruppe, int iSpalte,
			KapazitaetsvorschauDetailDto detail) {
		this.details[iGruppe][iSpalte].add(detail);
	}

	public BigDecimal[][] getBdVerfuegbareStunden() {
		return bdVerfuegbareStunden;
	}

	public void addBdVerfuegbareStunden(int iGruppe, int iSpalte,
			BigDecimal bdVerfuegbareStunden) {
		this.bdVerfuegbareStunden[iGruppe][iSpalte] = this.bdVerfuegbareStunden[iGruppe][iSpalte]
				.add(bdVerfuegbareStunden);
	}

	public void setISpaltenueberschrift(int index, String title) {
		sSpaltenueberschrift[index] = title;
	}

	public String getISpaltenueberschrift(int index) {
		return sSpaltenueberschrift[index];
	}

	public void setIZeilenueberschrift(int index, String title) {
		sZeilen[index] = title;
	}

	public String getIZeilenueberschrift(int index) {
		return sZeilen[index];
	}

}
