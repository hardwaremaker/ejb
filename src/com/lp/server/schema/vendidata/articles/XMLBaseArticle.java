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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.lp.server.schema.vendidata.commonobjects.XMLBarcode;


/**
 * <p>Java-Klasse fuer BaseArticle complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BaseArticle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItemNumber" type="{http://www.vendidata.com/XML/Schema/Articles}ItemNumber"/>
 *         &lt;element name="ErpId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="Barcode" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Barcode" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="isBaseArticle" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="status" type="{http://www.vendidata.com/XML/Schema/Articles}ArticleStatus" default="ACTIVE" />
 *       &lt;attribute name="dontShowInLoadingLists" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="strictBarcodeSynchronisation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseArticle", propOrder = {
    "itemNumber",
    "erpId",
    "price",
    "name",
    "group",
    "vatRate",
    "barcode"
})
public class XMLBaseArticle {

    @XmlElement(name = "ItemNumber", required = true)
    protected XMLItemNumber itemNumber;
    @XmlElement(name = "ErpId")
    protected String erpId;
    @XmlElement(name = "Price")
    protected double price;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Group", required = true)
    protected String group;
    @XmlElement(name = "VATRate")
    protected double vatRate;
    @XmlElement(name = "Barcode")
    protected List<XMLBarcode> barcode;
    @XmlAttribute(name = "isBaseArticle", required = true)
    protected boolean isBaseArticle;
    @XmlAttribute(name = "status")
    protected XMLArticleStatus status;
    @XmlAttribute(name = "dontShowInLoadingLists")
    protected Boolean dontShowInLoadingLists;
    @XmlAttribute(name = "strictBarcodeSynchronisation")
    protected Boolean strictBarcodeSynchronisation;

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
     * Ruft den Wert der erpId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErpId() {
        return erpId;
    }

    /**
     * Legt den Wert der erpId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErpId(String value) {
        this.erpId = value;
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
     * Gets the value of the barcode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the barcode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBarcode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLBarcode }
     * 
     * 
     */
    public List<XMLBarcode> getBarcode() {
        if (barcode == null) {
            barcode = new ArrayList<XMLBarcode>();
        }
        return this.barcode;
    }

    /**
     * Ruft den Wert der isBaseArticle-Eigenschaft ab.
     * 
     */
    public boolean isIsBaseArticle() {
        return isBaseArticle;
    }

    /**
     * Legt den Wert der isBaseArticle-Eigenschaft fest.
     * 
     */
    public void setIsBaseArticle(boolean value) {
        this.isBaseArticle = value;
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

    /**
     * Ruft den Wert der dontShowInLoadingLists-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDontShowInLoadingLists() {
        if (dontShowInLoadingLists == null) {
            return false;
        } else {
            return dontShowInLoadingLists;
        }
    }

    /**
     * Legt den Wert der dontShowInLoadingLists-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDontShowInLoadingLists(Boolean value) {
        this.dontShowInLoadingLists = value;
    }

    /**
     * Ruft den Wert der strictBarcodeSynchronisation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStrictBarcodeSynchronisation() {
        if (strictBarcodeSynchronisation == null) {
            return false;
        } else {
            return strictBarcodeSynchronisation;
        }
    }

    /**
     * Legt den Wert der strictBarcodeSynchronisation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStrictBarcodeSynchronisation(Boolean value) {
        this.strictBarcodeSynchronisation = value;
    }

}
