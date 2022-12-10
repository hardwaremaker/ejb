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
package com.lp.server.finanz.service;






public class CsvKonto extends KontoDto {
	private static final long serialVersionUID = 1L;

	private String ergebnisgruppeCnr ;

	private String kontoCnrWeiterfuehrendbilanz;

	private String kontoCnrWeiterfuehrendskonto;

	private String kontoCnrWeiterfuehrendust;

	private String uvaartCnr;
	
	private String finanzamtCnr;

	private String kostenstelleCnr;

	private String steuerkategorieCnr ;
	
	private String steuerkategorieCnrReverse;
	
	private String steuerart;

	public String getErgebnisgruppeCnr() {
		return ergebnisgruppeCnr;
	}

	public void setErgebnisgruppeCnr(String ergebnisgruppeCnr) {
		this.ergebnisgruppeCnr = ergebnisgruppeCnr;
	}

	public String getKontoCnrWeiterfuehrendbilanz() {
		return kontoCnrWeiterfuehrendbilanz;
	}

	public void setKontoCnrWeiterfuehrendbilanz(
			String kontoCnrWeiterfuehrendbilanz) {
		this.kontoCnrWeiterfuehrendbilanz = kontoCnrWeiterfuehrendbilanz;
	}

	public String getKontoCnrWeiterfuehrendskonto() {
		return kontoCnrWeiterfuehrendskonto;
	}

	public void setKontoCnrWeiterfuehrendskonto(
			String kontoCnrWeiterfuehrendskonto) {
		this.kontoCnrWeiterfuehrendskonto = kontoCnrWeiterfuehrendskonto;
	}

	public String getKontoCnrWeiterfuehrendust() {
		return kontoCnrWeiterfuehrendust;
	}

	public void setKontoCnrWeiterfuehrendust(String kontoCnrWeiterfuehrendust) {
		this.kontoCnrWeiterfuehrendust = kontoCnrWeiterfuehrendust;
	}

	public String getUvaartCnr() {
		return uvaartCnr;
	}

	public void setUvaartCnr(String uvaartCnr) {
		this.uvaartCnr = uvaartCnr;
	}

	public String getFinanzamtCnr() {
		return finanzamtCnr;
	}

	public void setFinanzamtCnr(String finanzamtCnr) {
		this.finanzamtCnr = finanzamtCnr;
	}

	public String getKostenstelleCnr() {
		return kostenstelleCnr;
	}

	public void setKostenstelleCnr(String kostenstelleCnr) {
		this.kostenstelleCnr = kostenstelleCnr;
	}

	public String getSteuerkategorieCnr() {
		return steuerkategorieCnr;
	}

	public void setSteuerkategorieCnr(String steuerkategorieCnr) {
		this.steuerkategorieCnr = steuerkategorieCnr;
	}

	public String getSteuerkategorieCnrReverse() {
		return steuerkategorieCnrReverse;
	}

	public void setSteuerkategorieCnrReverse(String steuerkategorieCnrReverse) {
		this.steuerkategorieCnrReverse = steuerkategorieCnrReverse;
	}

	public String getSteuerart() {
		return steuerart == null ? steuerart : steuerart.trim();
	}

	public void setSteuerart(String steuerart) {
		this.steuerart = steuerart;
	}
}
