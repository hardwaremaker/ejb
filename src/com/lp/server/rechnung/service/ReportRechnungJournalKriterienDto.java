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

import java.io.Serializable;
import java.sql.Date;

import com.lp.server.system.service.ReportJournalKriterienDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Kriterien fuer ein Rechnungsjournal
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 28.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: victor $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/05/25 13:56:25 $
 */
public class ReportRechnungJournalKriterienDto extends
		ReportJournalKriterienDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * statt dem Kunden wird die Statistikadresse verwenden?
	 */
	private boolean bVerwendeStatistikAdresse = false;
	/**
	 * Nur offene Rechnungen anfuehren?
	 */
	private boolean bNurOffene = false;
	/**
	 * Stichtag der Betrachtung.
	 */
	private Date tStichtag = null;
	/**
	 * Stichtag der Betrachtung.
	 */
	private boolean bGutschriftenBeruecksichtigen = false;
	
	private boolean bMitTexteingaben = false;

	private boolean bMitNichtZugeordnetendBelegen = false;
	
	public boolean getBMitNichtZugeordnetendBelegen() {
		return bMitNichtZugeordnetendBelegen;
	}
	
	public void setbMitNichtZugeordnetendBelegen(
			boolean bMitNichtZugeordnetendBelegen) {
		this.bMitNichtZugeordnetendBelegen = bMitNichtZugeordnetendBelegen;
	}
	
	public boolean getBMitTexteingaben() {
		return bMitTexteingaben;
	}

	public void setBMitTexteingaben(boolean mitTexteingaben) {
		bMitTexteingaben = mitTexteingaben;
	}

	public boolean getBVerwendeStatistikAdresse() {
		return this.bVerwendeStatistikAdresse;
	}

	public void setBVerwendeStatistikAdresse(boolean bVerwendeStatistikAdresse) {
		this.bVerwendeStatistikAdresse = bVerwendeStatistikAdresse;
	}

	public boolean getBNurOffene() {
		return this.bNurOffene;
	}

	public void setBNurOffene(boolean bNurOffene) {
		this.bNurOffene = bNurOffene;
	}

	public Date getTStichtag() {
		return this.tStichtag;
	}

	public void setTStichtag(Date tStichtag) {
		this.tStichtag = tStichtag;
	}

	public boolean getBGutschriftenBeruecksichtigen() {
		return this.bGutschriftenBeruecksichtigen;
	}

	public void setBGutschriftenBeruecksichtigen(
			boolean bGutschriftenBeruecksichtigen) {
		this.bGutschriftenBeruecksichtigen = bGutschriftenBeruecksichtigen;
	}
}
