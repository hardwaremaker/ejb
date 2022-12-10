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
package com.lp.server.stueckliste.ejbfac;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.lp.server.system.service.TheClientDto;
import com.lp.service.StklImportSpezifikation;

public class StklImportSearchHelperEinkauf extends StklImportSearchHelper {
	
	private Integer lieferantIId;

	public StklImportSearchHelperEinkauf(StklImportSpezifikation spez,
			TheClientDto theClientDto, Integer lieferantIId) {
		super(spez, theClientDto);
		this.lieferantIId = lieferantIId;
	}

	@Override
	protected void initColumnPriorityOrder() {
		columnPriorityOrder = Arrays.asList(
				StklImportSpezifikation.LIEFERANTENARTIKELNUMMER,
				StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
				StklImportSpezifikation.ARTIKELNUMMER,
				StklImportSpezifikation.HERSTELLERBEZ,
				StklImportSpezifikation.SI_WERT,
				StklImportSpezifikation.BEZEICHNUNG,
				StklImportSpezifikation.BEZEICHNUNG1,
				StklImportSpezifikation.BEZEICHNUNG2,
				StklImportSpezifikation.BEZEICHNUNG3,
				StklImportSpezifikation.BAUFORM,
				StklImportSpezifikation.KOMMENTAR);
	}

	@Override
	protected void initImportColumnsMap() {
		importColumns = new HashMap<String, IImportColumnQueryBuilder>();
		
		importColumns.put(StklImportSpezifikation.LIEFERANTENARTIKELNUMMER,
				new ImportColumnQueryBuilderLiefArtikelNr());
		importColumns.put(StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
				new ImportColumnQueryBuilderHerstellernummer());
		importColumns.put(StklImportSpezifikation.ARTIKELNUMMER,
				new ImportColumnQueryBuilderArtikelnr());
		importColumns.put(StklImportSpezifikation.HERSTELLERBEZ,
				new ImportColumnQueryBuilderHerstellerBez());
		importColumns.put(StklImportSpezifikation.SI_WERT,
				new ImportColumnQueryBuilderSIWert());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG1,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG2,
				new ImportColumnQueryBuilderBezeichnung());
		importColumns.put(StklImportSpezifikation.BEZEICHNUNG3,
				new ImportColumnQueryBuilderBezeichnung());	
		importColumns.put(StklImportSpezifikation.BAUFORM, 
				new ImportColumnQueryBuilderBauform());
	}

	@Override
	public List<String> getWhereQuery(String type) {
		List<String> wheres = super.getWhereQuery(type);
		
		if(type.equals(StklImportSpezifikation.LIEFERANTENARTIKELNUMMER) && lieferantIId != null) {
			String whereLieferantNr = ((ImportColumnQueryBuilderLiefArtikelNr)getImportColumns()
					.get(type)).buildWhereLieferantenIIdQuery(lieferantIId);
			wheres.add(whereLieferantNr);
		}

		return wheres;
	}

}
