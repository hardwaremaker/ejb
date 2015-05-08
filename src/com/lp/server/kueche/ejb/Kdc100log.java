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
package com.lp.server.kueche.ejb;



import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KUE_KDC100LOG")
public class Kdc100log implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_SERIENNUMMER")
	private String cSeriennummer;

	@Column(name = "C_ART")
	private String cArt;

	public String getCArt() {
		return cArt;
	}

	public void setCArt(String art) {
		cArt = art;
	}

	@Column(name = "C_BARCODE")
	private String cBarcode;

	@Column(name = "C_KOMMENTAR")
	private String cKommentar;

	@Column(name = "T_BUCHUNGSZEIT")
	private Timestamp tBuchungszeit;

	@Column(name = "T_STIFTZEIT")
	private Timestamp tStiftzeit;


	private static final long serialVersionUID = 1L;

	public Kdc100log() {
		super();
	}

	public Kdc100log(Integer id, String seriennr,String cArt, String barcode,String kommentar,
			Timestamp buchungszeit, Timestamp stiftzeit) {

		setIId(id);
		setCSeriennummer(seriennr);
		setCBarcode(barcode);
		setCKommentar(kommentar);
		setTBuchungszeit(buchungszeit);
		setTStiftzeit(stiftzeit);
		setCArt(cArt);
		

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCSeriennummer() {
		return cSeriennummer;
	}

	public void setCSeriennummer(String seriennummer) {
		this.cSeriennummer = seriennummer;
	}

	public String getCBarcode() {
		return cBarcode;
	}

	public void setCBarcode(String barcode) {
		this.cBarcode = barcode;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String kommentar) {
		this.cKommentar = kommentar;
	}

	public Timestamp getTBuchungszeit() {
		return tBuchungszeit;
	}

	public void setTBuchungszeit(Timestamp buchungszeit) {
		this.tBuchungszeit = buchungszeit;
	}

	public Timestamp getTStiftzeit() {
		return tStiftzeit;
	}

	public void setTStiftzeit(Timestamp stiftzeit) {
		this.tStiftzeit = stiftzeit;
	}



}
