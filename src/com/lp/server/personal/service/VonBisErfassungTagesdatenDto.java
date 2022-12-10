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
package com.lp.server.personal.service;

import java.io.Serializable;

public class VonBisErfassungTagesdatenDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double dUrlaub = 0;
	private double dArzt = 0;
	private double dUnter = 0;
	private double dBehoerde = 0;
	private double dZeitausgleich = 0;
	private double dKrank = 0;
	private double dKindkrank = 0;
	private double dSontigeBezahlt = 0;
	private double dSonstigeNichtBezahlt = 0;

	private String sZusatzbezeichnung=""; 
	
	public String getSZusatzbezeichnung() {
		return sZusatzbezeichnung;
	}
	public void setSZusatzbezeichnung(String sZusatzbezeichnung) {
		this.sZusatzbezeichnung = sZusatzbezeichnung;
	}
	public double getdUrlaub() {
		return dUrlaub;
	}
	public void setdUrlaub(double dUrlaub) {
		this.dUrlaub = dUrlaub;
	}
	public double getdArzt() {
		return dArzt;
	}
	public void setdArzt(double dArzt) {
		this.dArzt = dArzt;
	}
	public double getdUnter() {
		return dUnter;
	}
	public void setdUnter(double dUnter) {
		this.dUnter = dUnter;
	}
	public double getdBehoerde() {
		return dBehoerde;
	}
	public void setdBehoerde(double dBehoerde) {
		this.dBehoerde = dBehoerde;
	}
	public double getdZeitausgleich() {
		return dZeitausgleich;
	}
	public void setdZeitausgleich(double dZeitausgleich) {
		this.dZeitausgleich = dZeitausgleich;
	}
	public double getdKrank() {
		return dKrank;
	}
	public void setdKrank(double dKrank) {
		this.dKrank = dKrank;
	}
	public double getdKindkrank() {
		return dKindkrank;
	}
	public void setdKindkrank(double dKindkrank) {
		this.dKindkrank = dKindkrank;
	}
	public double getdSontigeBezahlt() {
		return dSontigeBezahlt;
	}
	public void setdSontigeBezahlt(double dSontigeBezahlt) {
		this.dSontigeBezahlt = dSontigeBezahlt;
	}
	public double getdSonstigeNichtBezahlt() {
		return dSonstigeNichtBezahlt;
	}
	public void setdSonstigeNichtBezahlt(double dSonstigeNichtBezahlt) {
		this.dSonstigeNichtBezahlt = dSonstigeNichtBezahlt;
	}
	public double getdIst() {
		return dIst;
	}
	public void setdIst(double dIst) {
		this.dIst = dIst;
	}
	private double dIst = 0;

}
