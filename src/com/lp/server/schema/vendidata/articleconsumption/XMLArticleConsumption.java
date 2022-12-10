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
// Generiert: 2016.01.11 um 09:04:33 AM CET 
//


package com.lp.server.schema.vendidata.articleconsumption;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.lp.server.schema.vendidata.commonobjects.XMLArticle;
import com.lp.server.schema.vendidata.commonobjects.XMLCustomer;
import com.lp.server.schema.vendidata.commonobjects.XMLEmployee;
import com.lp.server.schema.vendidata.commonobjects.XMLStand;
import com.lp.server.schema.vendidata.commonobjects.XMLVirtualVendingmachine;
import com.lp.server.schema.vendidata.expiredproducts.XMLExpiredProduct;


/**
 * <p>Java-Klasse fuer ArticleConsumption complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArticleConsumption">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Rowid">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BookingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Article" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Article"/>
 *         &lt;element name="NumberOfArticles">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="-9999999"/>
 *               &lt;maxInclusive value="9999999"/>
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PricePerItem">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="-9999999"/>
 *               &lt;maxInclusive value="9999999"/>
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VatRate">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="100"/>
 *               &lt;totalDigits value="10"/>
 *               &lt;fractionDigits value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Employee" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Employee"/>
 *         &lt;element name="InputType" type="{http://www.vendidata.com/XML/Schema/ArticleConsumption}InputType"/>
 *         &lt;element name="VirtualVendingmachine" type="{http://www.vendidata.com/XML/Schema/CommonObjects}VirtualVendingmachine"/>
 *         &lt;element name="Stand" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Stand"/>
 *         &lt;element name="Customer" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Customer"/>
 *         &lt;element name="IsCorrectionalRecord" type="{http://www.vendidata.com/XML/Schema/ArticleConsumption}IsCorrectionalRecord" minOccurs="0"/>
 *         &lt;element name="SourceStockID" type="{http://www.vendidata.com/XML/Schema/ArticleConsumption}SourceStockID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleConsumption", propOrder = {
    "rowid",
    "bookingDate",
    "article",
    "numberOfArticles",
    "pricePerItem",
    "vatRate",
    "employee",
    "inputType",
    "virtualVendingmachine",
    "stand",
    "customer",
    "isCorrectionalRecord",
    "sourceStockID"
})
@XmlSeeAlso({
    XMLExpiredProduct.class
})
public class XMLArticleConsumption {

    @XmlElement(name = "Rowid")
    protected int rowid;
    @XmlElement(name = "BookingDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar bookingDate;
    @XmlElement(name = "Article", required = true)
    protected XMLArticle article;
    @XmlElement(name = "NumberOfArticles", required = true)
    protected BigDecimal numberOfArticles;
    @XmlElement(name = "PricePerItem", required = true)
    protected BigDecimal pricePerItem;
    @XmlElement(name = "VatRate", required = true)
    protected BigDecimal vatRate;
    @XmlElement(name = "Employee", required = true)
    protected XMLEmployee employee;
    @XmlElement(name = "InputType", required = true)
    @XmlSchemaType(name = "string")
    protected XMLInputType inputType;
    @XmlElement(name = "VirtualVendingmachine", required = true)
    protected XMLVirtualVendingmachine virtualVendingmachine;
    @XmlElement(name = "Stand", required = true)
    protected XMLStand stand;
    @XmlElement(name = "Customer", required = true)
    protected XMLCustomer customer;
    @XmlElement(name = "IsCorrectionalRecord")
    protected XMLIsCorrectionalRecord isCorrectionalRecord;
    @XmlElement(name = "SourceStockID")
    protected Integer sourceStockID;

    /**
     * Ruft den Wert der rowid-Eigenschaft ab.
     * 
     */
    public int getRowid() {
        return rowid;
    }

    /**
     * Legt den Wert der rowid-Eigenschaft fest.
     * 
     */
    public void setRowid(int value) {
        this.rowid = value;
    }

    /**
     * Ruft den Wert der bookingDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBookingDate() {
        return bookingDate;
    }

    /**
     * Legt den Wert der bookingDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBookingDate(XMLGregorianCalendar value) {
        this.bookingDate = value;
    }

    /**
     * Ruft den Wert der article-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLArticle }
     *     
     */
    public XMLArticle getArticle() {
        return article;
    }

    /**
     * Legt den Wert der article-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLArticle }
     *     
     */
    public void setArticle(XMLArticle value) {
        this.article = value;
    }

    /**
     * Ruft den Wert der numberOfArticles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumberOfArticles() {
        return numberOfArticles;
    }

    /**
     * Legt den Wert der numberOfArticles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumberOfArticles(BigDecimal value) {
        this.numberOfArticles = value;
    }

    /**
     * Ruft den Wert der pricePerItem-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPricePerItem() {
        return pricePerItem;
    }

    /**
     * Legt den Wert der pricePerItem-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPricePerItem(BigDecimal value) {
        this.pricePerItem = value;
    }

    /**
     * Ruft den Wert der vatRate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVatRate() {
        return vatRate;
    }

    /**
     * Legt den Wert der vatRate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVatRate(BigDecimal value) {
        this.vatRate = value;
    }

    /**
     * Ruft den Wert der employee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLEmployee }
     *     
     */
    public XMLEmployee getEmployee() {
        return employee;
    }

    /**
     * Legt den Wert der employee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLEmployee }
     *     
     */
    public void setEmployee(XMLEmployee value) {
        this.employee = value;
    }

    /**
     * Ruft den Wert der inputType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLInputType }
     *     
     */
    public XMLInputType getInputType() {
        return inputType;
    }

    /**
     * Legt den Wert der inputType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLInputType }
     *     
     */
    public void setInputType(XMLInputType value) {
        this.inputType = value;
    }

    /**
     * Ruft den Wert der virtualVendingmachine-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLVirtualVendingmachine }
     *     
     */
    public XMLVirtualVendingmachine getVirtualVendingmachine() {
        return virtualVendingmachine;
    }

    /**
     * Legt den Wert der virtualVendingmachine-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLVirtualVendingmachine }
     *     
     */
    public void setVirtualVendingmachine(XMLVirtualVendingmachine value) {
        this.virtualVendingmachine = value;
    }

    /**
     * Ruft den Wert der stand-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLStand }
     *     
     */
    public XMLStand getStand() {
        return stand;
    }

    /**
     * Legt den Wert der stand-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLStand }
     *     
     */
    public void setStand(XMLStand value) {
        this.stand = value;
    }

    /**
     * Ruft den Wert der customer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLCustomer }
     *     
     */
    public XMLCustomer getCustomer() {
        return customer;
    }

    /**
     * Legt den Wert der customer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCustomer }
     *     
     */
    public void setCustomer(XMLCustomer value) {
        this.customer = value;
    }

    /**
     * Ruft den Wert der isCorrectionalRecord-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLIsCorrectionalRecord }
     *     
     */
    public XMLIsCorrectionalRecord getIsCorrectionalRecord() {
        return isCorrectionalRecord;
    }

    /**
     * Legt den Wert der isCorrectionalRecord-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLIsCorrectionalRecord }
     *     
     */
    public void setIsCorrectionalRecord(XMLIsCorrectionalRecord value) {
        this.isCorrectionalRecord = value;
    }

    /**
     * Ruft den Wert der sourceStockID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSourceStockID() {
        return sourceStockID;
    }

    /**
     * Legt den Wert der sourceStockID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSourceStockID(Integer value) {
        this.sourceStockID = value;
    }

}
