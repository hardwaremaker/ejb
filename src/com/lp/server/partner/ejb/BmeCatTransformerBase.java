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

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BmeCatTransformerBase {
	public BmeCatTransformerBase() {
		
	}
	
	protected Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance() ;
		DocumentBuilder docBuilder = builderFactory.newDocumentBuilder() ;
		
		return docBuilder.newDocument() ;
	}
	
	
	protected String getEmptyStringForNull(String value) {
		return value == null ? "" : value ;
	}
	
	/**
	 * Die in der Element-Liste befindlichen Elemente zum mainNode hinzufuegen
	 * 
	 * @param mainNode an diesen Node werden die Elemente hinzugefuegt
	 * @param elements sind die hinzuzufuegenden Nodes
	 */
	protected void appendChildNodes(Element mainNode, List<Element> elements) {
		for (Element d : elements) {
			mainNode.appendChild(d) ;
		}
	}	
	
	protected void appendTextContentIfNotEmpty(Document d, Element appendTo, String tagName, String value) {
		if(value == null || (value.trim().length() == 0)) return ;
		
		Element e = d.createElement(tagName) ;
		e.setTextContent(value.trim()) ;
		appendTo.appendChild(e) ;
	}	

	
	protected void appendTextContentIfNotEmptyNodes(Document d, Element appendTo, TagValuePair[] values) {
		for (TagValuePair tagValuePair : values) {
			if(tagValuePair.getValue() == null || tagValuePair.getValue().trim().length() == 0) continue ;
			
			Element e = d.createElement(tagValuePair.getTagName()) ;
			e.setTextContent(tagValuePair.getValue().trim()) ;
			appendTo.appendChild(e) ;			
		}
	}
	
	/**
	 * Wandelt ein Document in einen String um
	 * 
	 * @param d ist das umzuwandelnde Dokument
	 * @return das Dokument als String
	 */
	public String asString(Document d) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance() ;
			Transformer t = transformerFactory.newTransformer() ;
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes") ;
			t.setOutputProperty(OutputKeys.INDENT, "yes") ;
			
			StringWriter sw = new StringWriter() ;
			StreamResult result = new StreamResult(sw) ;
			DOMSource s = new DOMSource(d) ;
			t.transform(s, result) ;

			return sw.toString() ;
		} catch(TransformerConfigurationException e) {			
		} catch(TransformerException e) {
		}
		
		return "" ;
	}	
	
	
	class TagValuePair {
		private String tag ;
		private String value ;
		
		public TagValuePair(String tagName, String value) {
			tag = tagName ;
			this.value = value ;
		}

		public String getTagName() {
			return tag ;
		}
		
		public String getValue() {
			return value ;
		}
	}
}
