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
package com.lp.server.angebotstkl.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.server.system.service.SystemFac;
import com.lp.service.POSDocument2POSDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:56:42 $
 */
public class AngStklPosDocument2AngStklPosDto extends POSDocument2POSDto {

	private ArrayList<AgstklpositionDto> alOfAngStklPos = null;
	private AgstklpositionDto angStklPosDto = null;

	public AngStklPosDocument2AngStklPosDto(File fDocumentI) {

		super(fDocumentI);

		alOfAngStklPos = new ArrayList<AgstklpositionDto>();
		angStklPosDto = new AgstklpositionDto();
	}

	public ArrayList<AgstklpositionDto> getPOS() throws SAXException,
			ParserConfigurationException, IOException {

		return alOfAngStklPos;
	}

	public void endDocument() throws SAXException {
		if (angStklPosDto.getIId() != null) {
			alOfAngStklPos.add(angStklPosDto);
			getAlPOS().add(getsbPOS().toString());
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		super.characters(ch, start, length);

		if (getsQNameLast().equals(SystemFac.SCHEMA_OF_POSITION)) {
			// --Gruppenwechsel: "Position"
			if (angStklPosDto.getIId() != null) {
				alOfAngStklPos.add(angStklPosDto);
				getAlPOS().add(getsbPOS().toString());
			}
			angStklPosDto = new AgstklpositionDto();

			setsbPOS(new StringBuffer());
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		// ***ab hier alle belegpos-daten
		if (getsQNameLast().equals(SystemFac.SCHEMA_OF_QUANTITY_CUSTOMER)) {
			// QuantityCustomer
			angStklPosDto.fillWithXML(getsQNameLast(), getsCharacters());
		} else if (getsQNameLast().equals(
				SystemFac.SCHEMA_OF_QUANTITY_UNIT_CUSTOMER)) {
			// UnitCustomer
			angStklPosDto.fillWithXML(getsQNameLast(), getsCharacters());
		} else if (isFeature()) {
			// Feature ...
			if (getsQNameLast().equals(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION)) {
				// ... > Description
				setsFeatureDescriptionLast(getsCharacters());
			} else if (getsQNameLast()
					.equals(SystemFac.SCHEMA_OF_FEATURE_VALUE)) {
				angStklPosDto.fillWithXML(getsFeatureDescriptionLast(),
						getsCharacters());
				setsFeatureDescriptionLast("");
			}
		}
	}
}
