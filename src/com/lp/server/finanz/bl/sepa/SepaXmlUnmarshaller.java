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
package com.lp.server.finanz.bl.sepa;

import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Unmarshaller, wandelt den XML-Content in eine XML-Objektstruktur um 
 * 
 * @author andi
 *
 */
public abstract class SepaXmlUnmarshaller {

	public SepaXmlUnmarshaller() {
	}

	public abstract Object unmarshal(String xmlContent) throws JAXBException, SAXException;
	
	public static <T> T unmarshal(String xsdSchema, String xmlContent, Class<T> clss) 
			throws JAXBException, SAXException {
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = (xsdSchema == null || xsdSchema.trim().length() == 0)
				? null : schemaFactory.newSchema(new StreamSource(new StringReader(xsdSchema)));
		
//		Schema schema = null;
		JAXBContext jaxbContext = JAXBContext.newInstance(clss.getPackage().getName());
		return unmarshal(jaxbContext, schema, xmlContent, clss);
	}
	
	public static <T> T unmarshal(JAXBContext jaxbContext, Schema schema,
			String xmlContent, Class<T> clss) throws JAXBException {
		
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		
		Object o = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlContent)));
		if(o instanceof JAXBElement<?>) {
			return clss.cast(((JAXBElement) o).getValue());
		}
		return clss.cast(o);
	}
	
}
