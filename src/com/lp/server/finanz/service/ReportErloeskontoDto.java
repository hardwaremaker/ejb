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
package com.lp.server.finanz.service;

import java.io.Serializable;

/**
 * Erl&ouml;skonto mit Unterst&uuml;tzung f&uuml;r den Reportanwender abbilden</br>
 * <p>Der Standardanwendungsfall: Einfach ein "print" dieses Dto. Das liefert dann
 * die Kontonummer, oder eine Fehlernachricht.</p>
 * <p>Bei Bedarf kann auf die genauen Informationen zugegriffen werden</p>
 * 
 * @author Gerold
 */
public class ReportErloeskontoDto implements Serializable {
	private static final long serialVersionUID = -3402355948827521305L;

	private Integer kontoId ;
	private String kontoCnr ;
	private int errorNumber ;
	private String errorMessage ;
	
	public ReportErloeskontoDto() {
	}
	
	public ReportErloeskontoDto(Integer kontoId, String kontoCnr) {
		this.kontoId = kontoId ;
		this.kontoCnr = kontoCnr ;
	}
	
	public ReportErloeskontoDto(int errorNumber, String errorMessage) {
		this.errorNumber = errorNumber ;
		this.errorMessage = errorMessage ;
	}

	/**
	 * Die Kontonummer</br>
	 * <p>Gibt es eine KontoId dann gibt es auch eine Kontonummer -> diese anzeigen</p>
	 * <p>Gibt es keine KontoId, dann kann das deswegen sein, weil es f&uuml;r den 
	 * Artikel kein Erl&ouml;skonto gibt -> dann "" anzeigen,
	 * ansonsten kann es nur noch ein Fehler sein -> diesen anzeigen.</p>  
	 * 
	 */
	public String toString() {
		if(hasError()) {
			return "FEHLER: " + errorMessage ;
		}

		return kontoCnr ;
	}

	/**
	 * Wurde ein Fehler bei der Ermittlung des Erl&ouml;skontos festgestellt?
	 * @return true wenn ein Fehler bei der Ermittlung auftrat. Siehe dann 
	 * {@link #getErrorMessage()} bzw {@link #getErrorNumber()}
	 */
	public boolean hasError() {
		return errorMessage != null ;
	}
	
	public Integer getKontoId() {
		return kontoId;
	}

	public void setKontoId(Integer kontoId) {
		this.kontoId = kontoId;
	}

	public String getKontoCnr() {
		return kontoCnr;
	}

	public void setKontoCnr(String kontoCnr) {
		this.kontoCnr = kontoCnr;
	}

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + errorNumber;
		result = prime * result
				+ ((kontoCnr == null) ? 0 : kontoCnr.hashCode());
		result = prime * result + ((kontoId == null) ? 0 : kontoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportErloeskontoDto other = (ReportErloeskontoDto) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (errorNumber != other.errorNumber)
			return false;
		if (kontoCnr == null) {
			if (other.kontoCnr != null)
				return false;
		} else if (!kontoCnr.equals(other.kontoCnr))
			return false;
		if (kontoId == null) {
			if (other.kontoId != null)
				return false;
		} else if (!kontoId.equals(other.kontoId))
			return false;
		return true;
	}	
}
