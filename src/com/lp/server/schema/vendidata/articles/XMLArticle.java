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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A valid article either contain a SalesArticle elemnent or a BaseArcticle element or both.
 * 
 * <p>Java-Klasse fuer Article complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Article">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BaseArticle" type="{http://www.vendidata.com/XML/Schema/Articles}BaseArticle" minOccurs="0"/>
 *         &lt;element name="SalesArticle" type="{http://www.vendidata.com/XML/Schema/Articles}SalesArticle" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="referenceCriterion" use="required" type="{http://www.vendidata.com/XML/Schema/Articles}ReferenceCriterion" />
 *       &lt;attribute name="rowid">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="-2147483640"/>
 *             &lt;maxInclusive value="2147483640"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Article", propOrder = {
    "baseArticle",
    "salesArticle"
})
public class XMLArticle {

    @XmlElement(name = "BaseArticle")
    protected XMLBaseArticle baseArticle;
    @XmlElement(name = "SalesArticle")
    protected XMLSalesArticle salesArticle;
    @XmlAttribute(name = "referenceCriterion", required = true)
    protected XMLReferenceCriterion referenceCriterion;
    @XmlAttribute(name = "rowid")
    protected Integer rowid;

    /**
     * Ruft den Wert der baseArticle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLBaseArticle }
     *     
     */
    public XMLBaseArticle getBaseArticle() {
        return baseArticle;
    }

    /**
     * Legt den Wert der baseArticle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLBaseArticle }
     *     
     */
    public void setBaseArticle(XMLBaseArticle value) {
        this.baseArticle = value;
    }

    /**
     * Ruft den Wert der salesArticle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLSalesArticle }
     *     
     */
    public XMLSalesArticle getSalesArticle() {
        return salesArticle;
    }

    /**
     * Legt den Wert der salesArticle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSalesArticle }
     *     
     */
    public void setSalesArticle(XMLSalesArticle value) {
        this.salesArticle = value;
    }

    /**
     * Ruft den Wert der referenceCriterion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLReferenceCriterion }
     *     
     */
    public XMLReferenceCriterion getReferenceCriterion() {
        return referenceCriterion;
    }

    /**
     * Legt den Wert der referenceCriterion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLReferenceCriterion }
     *     
     */
    public void setReferenceCriterion(XMLReferenceCriterion value) {
        this.referenceCriterion = value;
    }

    /**
     * Ruft den Wert der rowid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRowid() {
        return rowid;
    }

    /**
     * Legt den Wert der rowid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRowid(Integer value) {
        this.rowid = value;
    }

}
