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
package com.lp.util;

import java.io.DataOutputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMSerializer {

	private int indent;
	private Node rootNode;

	/**
	 * Konstuktor
	 * 
	 * @param doc
	 *            Document
	 */
	public DOMSerializer(Document doc) {
		indent = 0;
		rootNode = (Node) doc;
	}

	/**
	 * Schreibt eine Zeile in OutputStream. Davor werden 3*indent leere Zeichen
	 * geschrieben.
	 * 
	 * @param line
	 *            String
	 * @param to
	 *            PrintStream
	 * @throws IOException
	 */
	private void writeIndentLine(String line, DataOutputStream to)
			throws IOException {
		for (int i = 0; i < indent; i++) {
			to.writeBytes(" ");
		}
		to.writeBytes(line);
	}

	/**
	 * Erzeuge aus einem DOM-Baum eine (XML-)Textausgabe.
	 * 
	 * @param node
	 *            Node
	 * @param to
	 *            PrintStream
	 * @throws IOException
	 */
	public void printNode(Node node, DataOutputStream to) throws IOException {
		switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE:
			Document doc = (Document) node;
			printNode(doc.getDocumentElement(), to);
			break;
		case Node.ELEMENT_NODE:
			String name = node.getNodeName();
			String elementStr = "<" + name;
			NamedNodeMap attrs = node.getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				Node current = attrs.item(i);
				elementStr = elementStr + " " + current.getNodeName() + "=\""
						+ current.getNodeValue() + "\"";
			}
			elementStr = elementStr + ">";
			writeIndentLine(elementStr, to);
			NodeList children = node.getChildNodes();
			if (children != null) {
				indent++;
				for (int i = 0; i < children.getLength(); i++) {
					printNode(children.item(i), to);
				}
				indent--;
			}
			writeIndentLine("</" + name + ">", to);
			break;
		case Node.TEXT_NODE:
			writeIndentLine(node.getNodeValue(), to);
			break;
		case Node.PROCESSING_INSTRUCTION_NODE:
			break;
		case Node.ENTITY_REFERENCE_NODE:
			break;
		case Node.DOCUMENT_TYPE_NODE:
			break;
		}
	}

	public void serialize(DataOutputStream to) throws IOException {
		printNode(rootNode, to);
	}
}
