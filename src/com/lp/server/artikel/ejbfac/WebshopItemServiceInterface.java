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
package com.lp.server.artikel.ejbfac;

import com.lp.server.system.service.WebshopAuthHeader;


public interface WebshopItemServiceInterface {

	/**
	 * Alle zur Verf&uuml;gung stehenden Shopgruppen liefern
	 * 
	 * @param header die Authentifizierungsinfo
	 * 
	 * @return die f&uuml;r den aktuellen Webshop zur Verfuegung stehenden
	 * Shopgruppen zurueckliefern
	 */
	public ShopgroupsResult getShopGroupsFindAll(WebshopAuthHeader header) ;

	/**
	 * Alle ge&auml;nderten Shopgruppen der erstenen Ebene liefern, die sich seit 
	 * dem angegebenen Datum ge&auml;ndert haben.</br>
	 * Es wird dabei ber&uuml;cksichtigt, dass sich eine (*Untershopgruppe) ge&auml;ndert
	 * haben k&ouml;nnte.
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param changedDate im Format "YYYY-MM-DD". Beginnzeit ist immer 00:00h
	 * 
	 * @return die fuer den aktuellen Webshop zur Verfuegung stehenden
	 * Shopgruppen die sich seit dem angegebenen Zeitpunkt geaendert haben. 
	 */
	public ShopgroupsResult getShopGroupsFindAllChanged(
			WebshopAuthHeader header, String changedDate) ;
	
	

	/**
	 * Alle zur Verfuegung stehenden Shopgruppen in einer flachen Struktur liefern.
	 * Es werden <b>alle</b> Shopgruppen geliefert, die fuer den angeforderten Shop 
	 * zur Verfuegung stehen.
	 * 
	 * @param header die Authentifizierungsinfo
	 *
	 * @return die fuer den aktuellen Webshop zur Verfuegung stehenden Shopgruppen
 	 */
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(WebshopAuthHeader header) ;

	/**
	 * Alle zur Verfuegung stehenden Shopgruppen in einer flachen Struktur liefern.
	 * Es werden <b>alle</b> Shopgruppen geliefert, die fuer den angeforderten Shop 
	 * zur Verfuegung stehen und seit dem angegebenen Datum/Zeit geaendert wurden.
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param changedDate
	 * @return die fuer den aktuellen Webshop zur Verfuegung stehenden Shopgruppen
	 */
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			WebshopAuthHeader header, String changedDate) ;
	
	
	/**
	 * Alle geaenderten Untershopgruppen der aktuellen Ebene (bzw. der angegebenen
	 * Shopgruppe) liefern.
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param rootShopgruppe
	 * @param changedDate
	 * @return ge&auml;nderte Shopruppen
	 */
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			WebshopAuthHeader header, String rootShopgruppe, String changedDate) ;
	
	/**
	 * Die per Name angeforderte Shopgruppe liefern
	 * @param header die Authentifizierungsinfo
	 * @param name der gesuchten Shopgruppe
	 * @return die Shopgruppe sofern sie f&uuml;r den aktuellen Webshop zur
	 *   Verfuegung steht
	 */
	public ShopgroupResult getShopGroupFindByCnr(WebshopAuthHeader header, String name) ;
	
	/**
	 * Die per Id angeforderte Shopgruppe liefern
	 * @param id der gesuchten Shopgruppe
	 * @return die Shopgruppe sofern sie f&uuml;r den aktuellen Webshop zur
	 *   Verfuegung steht
	 */
	public ShopgroupResult getShopGroupFindById(WebshopAuthHeader header, Integer id) ;

	
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			WebshopAuthHeader header, Integer rootShopgruppeIId, String changedDate) ;
	
	/**
	 * Einen spezifischen Artikel mit seinem Namen/Kurzbezeichnung holen
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param cnr
	 * @return der Artikel
	 */
	public WebshopItemResult getItemFindByCnr(WebshopAuthHeader header, String cnr) ;

	/**
	 * Einen spezifischen Artikel mit seiner Id holen
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param id des Artikels
	 * @return der Artikel
	 */
	public WebshopItemResult getItemFindById(WebshopAuthHeader header, Integer id) ;
	
	/**
	 * Das Artikelbild holen
	 * 
	 * @param header
	 * @param itemImageName der Name des Images welches mit {@link #getItemFindByCnr(WebshopAuthHeader, String)}
	 *  ermittelt wurde
	 * @return das Artikelbild
	 */
	public WebshopItemImageResult getItemImage(WebshopAuthHeader header, String itemImageName) ;
	
	
	/**
	 * Alle Artikel dieses Webshops holen
	 * 
	 * @param header die Authentifizierungsinfo
	 * @return die Artikel
	 */
	public WebshopItemsResult getItems(WebshopAuthHeader header) ;	
	
	
	/**
	 * Alle Artikel dieses Webshops holen, die sich seit dem Datum geaendert haben.
	 * 
	 * @param header die Authentifizierungsinfo
	 * @param changedDateTime im Format "YYYY-MM-DD HH:MM".
	 * @return die ge&auml;nderten Artikel
	 */
	public WebshopItemsResult getItemsChanged(WebshopAuthHeader header, String changedDateTime) ;
}
