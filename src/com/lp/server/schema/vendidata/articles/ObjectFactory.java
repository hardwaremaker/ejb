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
//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.11 um 08:57:54 AM CET 
//


package com.lp.server.schema.vendidata.articles;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lp.server.schema.vendidata.articles package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Articles_QNAME = new QName("http://www.vendidata.com/XML/Schema/Articles", "Articles");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lp.server.schema.vendidata.articles
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLArticles }
     * 
     */
    public XMLArticles createXMLArticles() {
        return new XMLArticles();
    }

    /**
     * Create an instance of {@link XMLItemNumber }
     * 
     */
    public XMLItemNumber createXMLItemNumber() {
        return new XMLItemNumber();
    }

    /**
     * Create an instance of {@link XMLArticle }
     * 
     */
    public XMLArticle createXMLArticle() {
        return new XMLArticle();
    }

    /**
     * Create an instance of {@link XMLBaseArticle }
     * 
     */
    public XMLBaseArticle createXMLBaseArticle() {
        return new XMLBaseArticle();
    }

    /**
     * Create an instance of {@link XMLSalesArticle }
     * 
     */
    public XMLSalesArticle createXMLSalesArticle() {
        return new XMLSalesArticle();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLArticles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.vendidata.com/XML/Schema/Articles", name = "Articles")
    public JAXBElement<XMLArticles> createArticles(XMLArticles value) {
        return new JAXBElement<XMLArticles>(_Articles_QNAME, XMLArticles.class, null, value);
    }

}
