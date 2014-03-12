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
package com.lp.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lp.server.system.service.SystemFac;

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
 * @version not attributable Date $Date: 2008/08/07 12:59:41 $
 */
public abstract class POSDocument2POSDto extends DefaultHandler {

	private String sQNameLast = "";
	private String sFeatureDescriptionLast = "";
	private File fDocument = null;

	private int xaLast = -1;
	protected static int MAX_LAST = 3;
	private String[] aLast = null;
	private String sCharacters = null;

	private ArrayList<String> alPOS = null;
	private StringBuffer sbPOS = null;

	public POSDocument2POSDto(File fDocumentI) {
		sCharacters = new String("");
		fDocument = fDocumentI;
		aLast = new String[MAX_LAST];
		alPOS = new ArrayList<String>();
		sbPOS = new StringBuffer();
		xaLast = 0;
	}

	public void startElement(String uriI, String nameI, String qNameI,
			Attributes attsI) {

		sCharacters = "";

		if (qNameI.equals(SystemFac.SCHEMA_OF_PRICE)
				|| qNameI.equals(SystemFac.SCHEMA_OF_FEATURE)) {
			// damit in aLast immer das gleiche muster steht.
			xaLast = 0;
		}

		xaLast = ((xaLast) % MAX_LAST);
		aLast[xaLast] = qNameI;
		xaLast++;

		sQNameLast = qNameI;

		// JO 08.10.06 for testing
		// System.out.println("startElement: >"
		// + uriI
		// + "<  >"
		// + nameI
		// + "<  >"
		// + qNameI
		// + "<  >"
		// + sCharacters
		// + "<"
		// );
	}

	abstract public void endElement(String uriI, String localNameI,
			String qNameI) throws SAXException;

	public void parse() throws SAXException, ParserConfigurationException,
			IOException {

		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Parse the input
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(fDocument, this);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		sCharacters += new String(ch, start, length);
		sbPOS.append(sCharacters);
	}

	public String[] getAlLast() {
		return aLast;
	}

	public ArrayList<String> getAlPOS() {
		return alPOS;
	}

	public String getsQNameLast() {
		return sQNameLast;
	}

	public String getsCharacters() {
		return sCharacters.trim();
	}

	public String getsCharactersRaw() {
		return sCharacters;
	}

	public void setsFeatureDescriptionLast(String sFeatureDescriptionLastI) {
		sFeatureDescriptionLast = sFeatureDescriptionLastI;
	}

	public String getsFeatureDescriptionLast() {
		return sFeatureDescriptionLast;
	}

	public StringBuffer getsbPOS() {
		return sbPOS;
	}

	public void setsbPOS(StringBuffer sbPOSI) {
		sbPOS = sbPOSI;
	}

	protected boolean isBaseprice() {
		return getAlLast()[0].equals(SystemFac.SCHEMA_OF_PRICE)
				&& getAlLast()[1].equals(SystemFac.SCHEMA_OF_PRICE_BASEPRICE)
				&& getsQNameLast().equals(SystemFac.SCHEMA_OF_PRICE_BASEPRICE);
	}

	protected boolean isFeature() {
		return getAlLast()[0].equals(SystemFac.SCHEMA_OF_FEATURE)
				|| getAlLast()[1]
						.equals(SystemFac.SCHEMA_OF_FEATURE_DESCRIPTION);
	}
}
