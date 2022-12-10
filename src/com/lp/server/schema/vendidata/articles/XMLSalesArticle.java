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
 * <p>Java-Klasse fuer SalesArticle complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SalesArticle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItemNumber" type="{http://www.vendidata.com/XML/Schema/Articles}ItemNumber"/>
 *         &lt;element name="Price" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Group">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VATRate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isSalesArticle" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="status" type="{http://www.vendidata.com/XML/Schema/Articles}ArticleStatus" default="ACTIVE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesArticle", propOrder = {
    "itemNumber",
    "price",
    "name",
    "group",
    "vatRate"
})
public class XMLSalesArticle {

    @XmlElement(name = "ItemNumber", required = true)
    protected XMLItemNumber itemNumber;
    @XmlElement(name = "Price")
    protected double price;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Group", required = true)
    protected String group;
    @XmlElement(name = "VATRate")
    protected double vatRate;
    @XmlAttribute(name = "isSalesArticle", required = true)
    protected boolean isSalesArticle;
    @XmlAttribute(name = "status")
    protected XMLArticleStatus status;

    /**
     * Ruft den Wert der itemNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLItemNumber }
     *     
     */
    public XMLItemNumber getItemNumber() {
        return itemNumber;
    }

    /**
     * Legt den Wert der itemNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLItemNumber }
     *     
     */
    public void setItemNumber(XMLItemNumber value) {
        this.itemNumber = value;
    }

    /**
     * Ruft den Wert der price-Eigenschaft ab.
     * 
     */
    public double getPrice() {
        return price;
    }

    /**
     * Legt den Wert der price-Eigenschaft fest.
     * 
     */
    public void setPrice(double value) {
        this.price = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der group-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Legt den Wert der group-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * Ruft den Wert der vatRate-Eigenschaft ab.
     * 
     */
    public double getVATRate() {
        return vatRate;
    }

    /**
     * Legt den Wert der vatRate-Eigenschaft fest.
     * 
     */
    public void setVATRate(double value) {
        this.vatRate = value;
    }

    /**
     * Ruft den Wert der isSalesArticle-Eigenschaft ab.
     * 
     */
    public boolean isIsSalesArticle() {
        return isSalesArticle;
    }

    /**
     * Legt den Wert der isSalesArticle-Eigenschaft fest.
     * 
     */
    public void setIsSalesArticle(boolean value) {
        this.isSalesArticle = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLArticleStatus }
     *     
     */
    public XMLArticleStatus getStatus() {
        if (status == null) {
            return XMLArticleStatus.ACTIVE;
        } else {
            return status;
        }
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLArticleStatus }
     *     
     */
    public void setStatus(XMLArticleStatus value) {
        this.status = value;
    }

}
