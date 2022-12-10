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

import java.io.IOException;
import java.util.ArrayList;

import com.lp.service.IReader;
import com.lp.service.JsonWriter;
import com.lp.service.StklImportSpezifikation;

public class FertigungsStklImportSpezifikation extends StklImportSpezifikation {

	private static final long serialVersionUID = -2456950132674843386L;
	private int montageartIId;

	public FertigungsStklImportSpezifikation() {
		super();
	}

	public void setMontageartIId(int montageartIId) {
		this.montageartIId = montageartIId;
	}

	public int getMontageartIId() {
		return montageartIId;
	}

	@Override
	protected void readIndividualValues(IReader reader) throws IOException {
		setMontageartIId(reader.getInteger());
	}

	@Override
	protected void readIndividualValuesJson(IReader reader) throws IOException {
		setMontageartIId(reader.getInteger(JsonProperties.MONTAGEART_IID));
	}
	
	@Override
	protected void writeIndividualValues(JsonWriter writer) {
		writer.putInteger(JsonProperties.MONTAGEART_IID, montageartIId);
	}

	@Override
	public String getKeyValueCGruppe() {
		return StklImportSpezifikation.getKeyValueCGruppeByStklTyp(SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ);
	}

	@Override
	protected void setAvailableColumnTypes() {
		availableColumnTypes = new ArrayList<String>();
		
		availableColumnTypes.add(KUNDENARTIKELNUMMER);
		availableColumnTypes.add(HERSTELLERARTIKELNUMMER);
		availableColumnTypes.add(ARTIKELNUMMER);
		availableColumnTypes.add(HERSTELLERBEZ);
		availableColumnTypes.add(SI_WERT);
		availableColumnTypes.add(BEZEICHNUNG);
		availableColumnTypes.add(BEZEICHNUNG1);
		availableColumnTypes.add(BEZEICHNUNG2);
		availableColumnTypes.add(BEZEICHNUNG3);
		availableColumnTypes.add(BAUFORM);
		availableColumnTypes.add(MENGE);
		availableColumnTypes.add(POSITION);
		availableColumnTypes.add(KOMMENTAR);
		availableColumnTypes.add(DIM_BREITE);
		availableColumnTypes.add(DIM_HOEHE);
		availableColumnTypes.add(DIM_TIEFE);
		availableColumnTypes.add(LAUFENDE_NUMMER);
	}

	@Override
	public void removeMappingColumnType() {
		availableColumnTypes.remove(KUNDENARTIKELNUMMER);
		int index = columnTypes.indexOf(KUNDENARTIKELNUMMER);
		if(index >= 0) {
			columnTypes.set(index, UNDEFINED);
		}
	}

	@Override
	public boolean isStuecklisteMitBezugVerkauf() {
		return true;
	}

}
