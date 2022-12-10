
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
package com.lp.server.auftrag.service;

import java.io.Serializable;

public class ImportWooCommerceCsvDto implements Serializable {
	public String bestellnummer = null;
	public String email = null;
	public String artikelnummer = null;
	public String bruttopreis = null;
	public String versandkosten = null;
	
	public String menge = null;
	public String belegdatum = null;

	public String anrede = null;
	
	public String lieferadresseAnrede = null;
	public String lieferadresseName1 = null;
	public String lieferadresseName2 = null;
	public String lieferadresseLand = null;
	public String lieferadressePLZ = null;
	public String lieferadresseOrt = null;
	public String lieferadresseStrasse = null;

	public String rechnungsadresseFirma = null;
	public String rechnungsadresseName1 = null;
	public String rechnungsadresseName2 = null;
	public String rechnungsadresseLand = null;
	public String rechnungsadressePLZ = null;
	public String rechnungsadresseOrt = null;
	public String rechnungsadresseStrasse = null;
	public String rechnungsadresseTelefon = null;

	public String artikelpreis = null;

	public String gesamterbelegbetrag = null;
	public String lieferart = null;
	public String zahlungsziel = null;
	
	public String kommentar = null;
	

	public int zeile;

}
