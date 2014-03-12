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
package com.lp.server.partner.service;

import java.io.Serializable;
import java.sql.Date;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 08.08.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 14:03:09 $
 */
public class StatistikParamDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dDatumVon = null;
	private Date dDatumBis = null;
	private Integer id = null;
	private Integer iSortierungAscDesc = null;
	private Integer iSortierungNachWas = null;

	public StatistikParamDto() {
	}

	public void setDDatumVon(Date dDatumVon) {
		this.dDatumVon = dDatumVon;
	}

	public void setDDatumBis(Date dDatumBis) {
		this.dDatumBis = dDatumBis;
	}

	public void setId(Integer idI) {
		this.id = idI;
	}

	public void setISortierungNachWas(Integer ISortierungNachWas) {
		this.iSortierungNachWas = ISortierungNachWas;
	}

	public void setISortierung(Integer iSortierung) {
		this.iSortierungAscDesc = iSortierung;
	}

	public Date getDDatumVon() {
		return dDatumVon;
	}

	public Date getDDatumBis() {
		return dDatumBis;
	}

	public Integer getId() {
		return id;
	}

	public Integer getISortierungNachWas() {
		return iSortierungNachWas;
	}

	public Integer getISortierung() {
		return iSortierungAscDesc;
	}

}
