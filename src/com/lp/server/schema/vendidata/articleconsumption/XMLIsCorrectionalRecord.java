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
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.lp.server.schema.vendidata.commonobjects.XMLArticle;


/**
 * <p>Java-Klasse fuer IsCorrectionalRecord complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="IsCorrectionalRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CorrectsRecordWithRowid">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OriginalArticle" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Article"/>
 *         &lt;element name="OriginalNumberOfArticles">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="-9999999"/>
 *               &lt;maxInclusive value="9999999"/>
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OriginalBookingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IsCorrectionalRecord", propOrder = {
    "correctsRecordWithRowid",
    "originalArticle",
    "originalNumberOfArticles",
    "originalBookingDate"
})
public class XMLIsCorrectionalRecord {

    @XmlElement(name = "CorrectsRecordWithRowid")
    protected int correctsRecordWithRowid;
    @XmlElement(name = "OriginalArticle", required = true)
    protected XMLArticle originalArticle;
    @XmlElement(name = "OriginalNumberOfArticles", required = true)
    protected BigDecimal originalNumberOfArticles;
    @XmlElement(name = "OriginalBookingDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar originalBookingDate;

    /**
     * Ruft den Wert der correctsRecordWithRowid-Eigenschaft ab.
     * 
     */
    public int getCorrectsRecordWithRowid() {
        return correctsRecordWithRowid;
    }

    /**
     * Legt den Wert der correctsRecordWithRowid-Eigenschaft fest.
     * 
     */
    public void setCorrectsRecordWithRowid(int value) {
        this.correctsRecordWithRowid = value;
    }

    /**
     * Ruft den Wert der originalArticle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLArticle }
     *     
     */
    public XMLArticle getOriginalArticle() {
        return originalArticle;
    }

    /**
     * Legt den Wert der originalArticle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLArticle }
     *     
     */
    public void setOriginalArticle(XMLArticle value) {
        this.originalArticle = value;
    }

    /**
     * Ruft den Wert der originalNumberOfArticles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOriginalNumberOfArticles() {
        return originalNumberOfArticles;
    }

    /**
     * Legt den Wert der originalNumberOfArticles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOriginalNumberOfArticles(BigDecimal value) {
        this.originalNumberOfArticles = value;
    }

    /**
     * Ruft den Wert der originalBookingDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOriginalBookingDate() {
        return originalBookingDate;
    }

    /**
     * Legt den Wert der originalBookingDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOriginalBookingDate(XMLGregorianCalendar value) {
        this.originalBookingDate = value;
    }

}
