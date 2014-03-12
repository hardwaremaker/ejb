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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;
import com.lp.server.util.IShopgruppeData;

@NamedQueries({
		@NamedQuery(name = Shopgruppe.QueryFindByMandantCNr, query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.mandantCNr = ?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "ShopgruppefindAllRoot", query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.shopgruppeIId IS NULL"),
		@NamedQuery(name = Shopgruppe.QueryFindByParentIId, query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.shopgruppeIId= :id"),
		@NamedQuery(name = Shopgruppe.QueryFindByCNrMandantCNr, query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.cNr = ?1 AND o.mandantCNr = ?2"),
		@NamedQuery(name = Shopgruppe.QueryFindByParentIIdDate, query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.shopgruppeIId= :id AND o.tAendern >= :tChanged"),
//		@NamedQuery(name = Shopgruppe.QueryFindLastByMandantCNr, query = 
//		"SELECT OBJECT(o) FROM Shopgruppe o WHERE o.mandantCNr = :mandant AND o.iSort = (SELECT MAX(o.iSort) from Shopgruppe s where s.mandantCNr = :mandant)"),
		@NamedQuery(name = Shopgruppe.QueryFindByMandantCNrISort, query = 
		"SELECT OBJECT(o) FROM Shopgruppe o WHERE o.mandantCNr = :mandant AND o.iSort = :isort"),
		@NamedQuery(name = Shopgruppe.QueryMaxISortByMandantCNr, query = "SELECT MAX(o.iSort) from Shopgruppe o where o.mandantCNr = :mandant"),
		@NamedQuery(name = Shopgruppe.QueryFindPreviousISortByMandantCNr, query = "SELECT OBJECT(o) FROM Shopgruppe o WHERE o.mandantCNr = :mandant AND o.iSort < :isort ORDER BY o.iSort DESC")
		})
@Entity
@Table(name = "WW_SHOPGRUPPE")
public class Shopgruppe implements Serializable, IShopgruppeData, ICNr {
	private static final long serialVersionUID = -2775957193879143519L;

	public final static String QueryFindByMandantCNr = "ShopgruppefindByMandantCNr" ;
	public final static String QueryFindByParentIId = "ShopgruppefindByParentIId" ;
	public final static String QueryFindByCNrMandantCNr = "ShopgruppefindByCNrMandantCNr" ;
	public final static String QueryFindByParentIIdDate = "ShopgruppefindByParentIIdDate" ;
	public final static String QueryFindByMandantCNrISort = "ShopgruppefindLastByMandantCNr" ;
	public final static String QueryFindPreviousISortByMandantCNr = "ShopgruppefindPreviousISortByMandantCNr" ;
	public final static String QueryMaxISortByMandantCNr = "ShopgruppeMaxISortByMandantCNr" ;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "SHOPGRUPPE_I_ID")
	private Integer shopgruppeIId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	@Column(name = "PERSONAL_I_ID_AENDERN") 
	private Integer personalIIdAendern;
	
	@Column(name = "T_AENDERN") 
	private Timestamp tAendern;
	
	@Column(name = "T_ANLEGEN") 
	private Timestamp tAnlegen;

	@Column(name = "I_SORT")
	private Integer iSort;

	
	public Shopgruppe() {
		super();
	}

	public Shopgruppe(Integer id, String nr, String mandantCNr) {
		setCNr(nr);
		setIId(id);
		setMandantCNr(mandantCNr);
	}

	
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getShopgruppeIId() {
		return this.shopgruppeIId;
	}

	public void setShopgruppeIId(Integer shopgruppeIId) {
		this.shopgruppeIId = shopgruppeIId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

}
