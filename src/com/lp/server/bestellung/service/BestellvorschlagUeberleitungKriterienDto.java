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
package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.sql.Date;

public class BestellvorschlagUeberleitungKriterienDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean bBelegprolieferantprotermin;
	private boolean bBelegprolieferant;
	private boolean bBelegeinlieferanteintermin;
	private Integer belegeinlieferanteinterminLieferantIId;
	private Date tBelegeinlieferanteinterminTermin;
	private boolean bBelegeinlieferant;
	private Integer belegeinlieferantLieferantIId;
	private Integer kostenstelleIId;
	private boolean bAbrufeZuRahmen;
	private boolean bRichtpreisUebernehmen;
	private boolean bBeruecksichtigeProjektklammer;

	
	
	public boolean isBBeruecksichtigeProjektklammer() {
		return bBeruecksichtigeProjektklammer;
	}

	public void setBBeruecksichtigeProjektklammer(
			boolean bBeruecksichtigeProjektklammer) {
		this.bBeruecksichtigeProjektklammer = bBeruecksichtigeProjektklammer;
	}

	public boolean getBRichtpreisUebernehmen() {
		return this.bRichtpreisUebernehmen;
	}

	public void setBRichtpreisUebernehmen(boolean bRichtpreisUebernehmen) {
		this.bRichtpreisUebernehmen = bRichtpreisUebernehmen;
	}

	public boolean getBAbrufeZuRahmen() {
		return this.bAbrufeZuRahmen;
	}

	public void setBAbrufeZuRahmen(boolean bAbrufeZuRahmen) {
		this.bAbrufeZuRahmen = bAbrufeZuRahmen;
	}

	public boolean getBBelegprolieferantprotermin() {
		return this.bBelegprolieferantprotermin;
	}

	public void setBBelegprolieferantprotermin(
			boolean bBelegprolieferantprotermin) {
		this.bBelegprolieferantprotermin = bBelegprolieferantprotermin;
	}

	public boolean getBBelegprolieferant() {
		return this.bBelegprolieferant;
	}

	public void setBBelegprolieferant(boolean bBelegprolieferant) {
		this.bBelegprolieferant = bBelegprolieferant;
	}

	public boolean getBBelegeinlieferanteintermin() {
		return this.bBelegeinlieferanteintermin;
	}

	public void setBBelegeinlieferanteintermin(
			boolean bBelegeinlieferanteintermin) {
		this.bBelegeinlieferanteintermin = bBelegeinlieferanteintermin;
	}

	public Integer getBelegeinlieferanteinterminLieferantIId() {
		return this.belegeinlieferanteinterminLieferantIId;
	}

	public void setBelegeinlieferanteinterminLieferantIId(
			Integer belegeinlieferanteinterminLieferantIId) {
		this.belegeinlieferanteinterminLieferantIId = belegeinlieferanteinterminLieferantIId;
	}

	public Date getTBelegeinlieferanteinterminTermin() {
		return this.tBelegeinlieferanteinterminTermin;
	}

	public void setTBelegeinlieferanteinterminTermin(
			Date tBelegeinlieferanteinterminTermin) {
		this.tBelegeinlieferanteinterminTermin = tBelegeinlieferanteinterminTermin;
	}

	public boolean getBBelegeinlieferant() {
		return this.bBelegeinlieferant;
	}

	public void setBBelegeinlieferant(boolean bBelegeinlieferant) {
		this.bBelegeinlieferant = bBelegeinlieferant;
	}

	public Integer getBelegeinlieferantLieferantIId() {
		return this.belegeinlieferantLieferantIId;
	}

	public void setBelegeinlieferantLieferantIId(
			Integer belegeinlieferantLieferantIId) {
		this.belegeinlieferantLieferantIId = belegeinlieferantLieferantIId;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
	}
}
