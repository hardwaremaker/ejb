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
package com.lp.server.partner.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class KundesokoQuery {
	/**
	 * Suche Kunden &uuml;ber seine Lieferantennummer im angegebenen Mandanten
	 */
	public final static String ByKundeIIdArtikelnummer = "KundeIIdArtikelnummer" ;

	/**
	 * Suche nach der Artikelnummer und Gueltigkeit. Liefert alle Kunden
	 */
	public static final String ByArtikelIIdGueltigkeitsdatum = "ArtikelIIdGueltigkeitsdatum" ;

	/**
	 * Suche nach der Artikelgruppe und Gueltigkeit. Liefert alle Kunden
	 */
	public static final String ByArtgruIIdGueltigkeitsdatum = "ArtgruIIdGueltigkeitsdatum" ;
	
	public static Query byKundeIIdArtikelnummer(EntityManager em, Integer kundeIId, String artikelnummer, Date tGueltig) {
		Query q = em.createNamedQuery(ByKundeIIdArtikelnummer) ;
		q.setParameter("kundeIId", kundeIId) ;
		q.setParameter("artikelnummer", artikelnummer.trim()) ;
		q.setParameter("tGueltig", tGueltig) ;
		return q ;
	}	
	
	public static List<Kundesoko> listByKundeIIdArtikelnummer(EntityManager em, Integer kundeIId, String artikelnummer, Date tGueltig) {
		return byKundeIIdArtikelnummer(em, kundeIId, artikelnummer, tGueltig).getResultList() ;
	}

	public static Query byArtikelIIdGueltigkeitsdatum(EntityManager em, Integer artikelIId, Date tGueltig) {
		Query q = em.createNamedQuery(ByArtikelIIdGueltigkeitsdatum) ;
		q.setParameter("artikelIId", artikelIId) ;
		q.setParameter("tGueltig", tGueltig) ;
		return q ;
	}	
	
	public static List<Kundesoko> listByArtikelIIdGueltigkeitsdatum(EntityManager em, Integer artikelIId, Date tGueltig) {
		return byArtikelIIdGueltigkeitsdatum(em, artikelIId, tGueltig).getResultList() ;
	}

	public static Query byArtgruIIdGueltigkeitsdatum(EntityManager em, Integer artgruIId, Date tGueltig) {
		Query q = em.createNamedQuery(ByArtikelIIdGueltigkeitsdatum) ;
		q.setParameter("artgruIId", artgruIId) ;
		q.setParameter("tGueltig", tGueltig) ;
		return q ;
	}	
	
	public static List<Kundesoko> listByArtgruIIdGueltigkeitsdatum(EntityManager em, Integer artikelIId, Date tGueltig) {
		return byArtikelIIdGueltigkeitsdatum(em, artikelIId, tGueltig).getResultList() ;
	}
}
