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

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikelshopgruppeQuery {
	/**
	 * Alle Artikelshopgruppen der angegebenen ShopgruppeIId
	 */
	public final static String ByShopgruppeIId = "ArtikelshopgruppefindByShopgruppeIId" ;

	/**
	 * Alle Artikelshopgruppen der angegebenen ShopgruppeIId nach dem angegebenen Datum
	 */
	public final static String ByShopgruppeIIdDate = "ArtikelshopgruppefindByShopgruppeIIdDate" ;
	
	public static HvTypedQuery<Artikelshopgruppe> byShopgruppeIId(EntityManager em, Integer shopgruppeIId) {
		HvTypedQuery<Artikelshopgruppe> q = new HvTypedQuery<Artikelshopgruppe>(
				em.createNamedQuery(ByShopgruppeIId)) ;
		q.setParameter("shopgruppeId", shopgruppeIId) ;
		return q ;
	}	

	/**
	 * Die Liste aller Artikel/Shopgruppeneintr&auml;ge mit der angegebenen Shopgruppe
	 * @param em
	 * @param shopgruppeIId
	 * @return die Liste der Artikel/Shoppengruppen
	 */
	public static List<Artikelshopgruppe> listByShopgruppeIId(EntityManager em, Integer shopgruppeIId) {
		return byShopgruppeIId(em, shopgruppeIId).getResultList() ;
	}

	public static HvTypedQuery<Artikelshopgruppe> byShopgruppeIIdDate(
			EntityManager em, Integer shopgruppeIId, Timestamp changedDate) {
		HvTypedQuery<Artikelshopgruppe> q = new HvTypedQuery<Artikelshopgruppe>(
				em.createNamedQuery(ByShopgruppeIIdDate)) ;
		q.setParameter("shopgruppeId", shopgruppeIId) ;
		q.setParameter("tChanged", changedDate) ;
		return q ;
	}	

	/**
	 * Die Liste aller Artikel/Shopgruppeneintr&auml;ge mit der angegebenen Shopgruppe,
	 *  die sich seit dem timestamp geaendert haben
	 * @param em
	 * @param shopgruppeIId
	 * @param changedDate
	 * @return die Liste der Artikelshopgruppen
	 */
	public static List<Artikelshopgruppe> listByShopgruppeIIdDate(
			EntityManager em, Integer shopgruppeIId, Timestamp changedDate) {
		return byShopgruppeIIdDate(em, shopgruppeIId, changedDate).getResultList() ;
	}
}
