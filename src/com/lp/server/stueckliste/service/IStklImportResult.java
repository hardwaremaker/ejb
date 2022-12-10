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
 package com.lp.server.stueckliste.service;

import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.ArtikelDto;

public interface IStklImportResult {

		/**
		 * Gibt die gefundenen ArtikelDtos zur&uuml;ck.
		 * @return eine Liste mit ArtikelDtos, ist eventuell unmodifizierbar.
		 */
		public List<ArtikelDto> getFoundItems() ;

		public void setFoundItems(List<ArtikelDto> foundItems);

		/**
		 * 
		 * @return true, wenn es sich beim gefundenen <code>ArtikelDto</code>
		 *  um 100%ige &Uuml;bereinstimmung handelt
		 */
		public boolean isTotalMatch();

		/**
		 * setter zu {@link #isTotalMatch()}
		 * @param totalMatch
		 * @see #isTotalMatch() isTotalMatch
		 */
		public void setTotalMatch(boolean totalMatch);

		/**
		 * Liefert eine Map mit den Spaltentypen als Keys und den Spalteninhalten als Value.
		 * @return eine Map mit dem Zeileninhalt, ist eventuell unmodifizierbar.
		 */
		public Map<String, String> getValues();

		/**
		 * Setzt die Inhalte der Zeile
		 * @param values eine Map mit den Spaltentypen 
		 * als Keys und den Spalteninhalten als Value
		 */
		public void setValues(Map<String, String> values);
		
		/**
		 * Gibt den Index des ausgew&auml;hlten {@link ArtikelDto} in
		 * der Liste <code>getFoundItems()</code>. Dieser Artikel wird
		 * dann importiert.
		 * @return den Index des ArtikelDto in der Liste; null oder -1 wenn nichts ausgew&auml;hlt ist.
		 */
		public Integer getSelectedIndex();
		
		/**
		 * Setter zu {@link #getSelectedIndex()}
		 * @param selectedIndex
		 * @see #setSelectedIndex(Integer)
		 */
		public void setSelectedIndex(Integer selectedIndex);
		
		/**
		 * Liefert, im Fall dass ein Artikel selektiert ist, das gleiche Ergebnis wie:<br>
		 * <code>
		 * IStklImportResult res = ...<br>
		 * return res.getFoundItems().get(res.getSelectedIndex);</code>
		 * @return das ausgew&auml;hlte ArtikelDto oder null wenn keines ausgew&auml;hlt ist.
		 */
		public ArtikelDto getSelectedArtikelDto();
		
		/**
		 * Setzt, ob das ArtikelnummerMapping des Results aktualisiert werden soll.
		 * @param updateArtikelnummerMapping
		 */
		public void setUpdateArtikelnummerMapping(boolean updateArtikelnummerMapping);
		
		/**
		 * 
		 * @return true, wenn ein Update des ArtikelnummerMapping f&uuml;r dieses 
		 * Result durchgef&uuml;hrt werden soll
		 */
		public boolean isUpdateArtikelnummerMapping();
		
		/**
		 *	
		 * @param foundTooManyArticles
		 */
		public void setFoundTooManyArticles(boolean foundTooManyArticles);
		
		/**
		 * 
		 * @return true, wenn zuviele Artikel f&uuml;r dieses Result gefunden wurden.
		 */
		public boolean foundTooManyArticles();

		/**
		 * 
		 * @return true, wenn ein bereits existierender Preis des Lieferanten
		 * aus dem Artikellieferanten anstatt des Preises aus der Bestellung 
		 * &uuml;bernommen werden soll 
		 */
		public boolean uebernehmeLiefPreisInBestellung();
		
		/**
		 * Setzt den Wert, wenn ein bereits existierender Preis des Lieferanten
		 * aus dem Artikellieferanten anstatt des Preises aus der Bestellung 
		 * &uuml;bernommen werden soll
		 * 
		 * @param uebernehmen true wenn der Lieferantenpreis &uuml;bernommen
		 * werden soll
		 */
		public void setUebernehmeLiefPreisInBestellung(boolean uebernehmen);
}
