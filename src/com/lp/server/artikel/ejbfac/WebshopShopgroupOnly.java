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
package com.lp.server.artikel.ejbfac;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Eine Webshop-Gruppe enthaelt folgende Informationen
 * - die id, cnr, bez der Shopgruppe
 * - die (eventuell vorhandene) Kind-Shopgruppe
 * - die (eventuell) vorhandenen Artikel-Schluesselfelder
 * - den (eventuell) vorhandenen Referenzartikel der Shopgruppe
 * <br>
 * <p>Im aktuellen Stand ist es nicht "verboten", dass eine
 * Shopgruppe eine Kind-Shopgruppe hat und trotzdem Artikel auflistet.
 * Es ist allerdings so, dass HeliumV derzeit nur entweder eine Kindershopgruppe,
 * oder eine Artikelliste liefern wird.
 * </p>
 * @author Gerold
 */

@XmlRootElement(name = "WebshopShopgroupOnly") 
public class WebshopShopgroupOnly implements Serializable {
	private static final long serialVersionUID = 557817063327792249L;

	private Integer id ;
	private String cnr ;
	private String bez ;
	private Integer sortValue ;

	public WebshopShopgroupOnly() {
	}
	
	
	/**
	 * Die Id der aktuellen Shopgruppe.
	 * @return die (eindeutige) Id der Shopgruppe. Mit dieser Id kann spaeter referenziert werden.
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Die Kennung der Shopgruppe (Kurzbezeichnung, Suchschluessel)
	 * 
	 * @return die Kennung dieser Shopgruppe
	 */
	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	/**
	 * Die Bezeichnung der Shopgruppe
	 * 
	 * @return die (leere) Bezeichnung der Shopgruppe.
	 */
	public String getName() {
		return bez;
	}

	public void setName(String name) {
		this.bez = name;
	}


	public Integer getSortValue() {
		return sortValue;
	}


	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
}
