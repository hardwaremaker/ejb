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
package com.lp.server.eingangsrechnung.bl;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Kuemmert sich um das Marshalling der Sepa-XML-Objekte in eine
 * textuelle XML-Darstellung
 * 
 * @author andi
 *
 */
public abstract class SepaXmlMarshaller {

	public SepaXmlMarshaller() {
	}

	/**
	 * Wandelt die befuellten Sepa-XML-Objekte eines Zahlungsauftrag (pain)
	 * in eine XML-Darstellung
	 * Wird vom jeweiligen Marshaller der pain-Versionen implementiert. Je
	 * Version gibt es ein eigenes Package mit der Klassenstruktur.
	 * 
	 * @param sepaDoc Wurzelobjekt des Sepa-XML Zahlungsauftrags
	 * @return die erzeugte XML-Datei als String
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public abstract String marshal (Object sepaDoc) throws JAXBException, SAXException;
	
	public static String marshal(String xsdSchema, Object jaxbElement)
			throws JAXBException, SAXException {
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		Schema schema = (xsdSchema == null || xsdSchema.trim().length() == 0)
				? null : schemaFactory.newSchema(new StreamSource(new StringReader(xsdSchema)));
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbElement.getClass().getPackage().getName());
		return marshal(jaxbContext, schema, jaxbElement);
	}

	public static String marshal(JAXBContext jaxbContext, Schema schema, Object jaxbElement)
			throws JAXBException {
		
		StringWriter writer = new StringWriter();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(jaxbElement, writer);
		
		return writer.toString();
	}
}
