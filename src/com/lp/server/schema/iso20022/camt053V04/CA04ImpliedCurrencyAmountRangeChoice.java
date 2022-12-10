//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ImpliedCurrencyAmountRangeChoice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ImpliedCurrencyAmountRangeChoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="FrAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}AmountRangeBoundary1"/>
 *         &lt;element name="ToAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}AmountRangeBoundary1"/>
 *         &lt;element name="FrToAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}FromToAmountRange"/>
 *         &lt;element name="EQAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ImpliedCurrencyAndAmount"/>
 *         &lt;element name="NEQAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ImpliedCurrencyAndAmount"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImpliedCurrencyAmountRangeChoice", propOrder = {
    "frAmt",
    "toAmt",
    "frToAmt",
    "eqAmt",
    "neqAmt"
})
public class CA04ImpliedCurrencyAmountRangeChoice {

    @XmlElement(name = "FrAmt")
    protected CA04AmountRangeBoundary1 frAmt;
    @XmlElement(name = "ToAmt")
    protected CA04AmountRangeBoundary1 toAmt;
    @XmlElement(name = "FrToAmt")
    protected CA04FromToAmountRange frToAmt;
    @XmlElement(name = "EQAmt")
    protected BigDecimal eqAmt;
    @XmlElement(name = "NEQAmt")
    protected BigDecimal neqAmt;

    /**
     * Ruft den Wert der frAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04AmountRangeBoundary1 }
     *     
     */
    public CA04AmountRangeBoundary1 getFrAmt() {
        return frAmt;
    }

    /**
     * Legt den Wert der frAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04AmountRangeBoundary1 }
     *     
     */
    public void setFrAmt(CA04AmountRangeBoundary1 value) {
        this.frAmt = value;
    }

    /**
     * Ruft den Wert der toAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04AmountRangeBoundary1 }
     *     
     */
    public CA04AmountRangeBoundary1 getToAmt() {
        return toAmt;
    }

    /**
     * Legt den Wert der toAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04AmountRangeBoundary1 }
     *     
     */
    public void setToAmt(CA04AmountRangeBoundary1 value) {
        this.toAmt = value;
    }

    /**
     * Ruft den Wert der frToAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04FromToAmountRange }
     *     
     */
    public CA04FromToAmountRange getFrToAmt() {
        return frToAmt;
    }

    /**
     * Legt den Wert der frToAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04FromToAmountRange }
     *     
     */
    public void setFrToAmt(CA04FromToAmountRange value) {
        this.frToAmt = value;
    }

    /**
     * Ruft den Wert der eqAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEQAmt() {
        return eqAmt;
    }

    /**
     * Legt den Wert der eqAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEQAmt(BigDecimal value) {
        this.eqAmt = value;
    }

    /**
     * Ruft den Wert der neqAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNEQAmt() {
        return neqAmt;
    }

    /**
     * Legt den Wert der neqAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNEQAmt(BigDecimal value) {
        this.neqAmt = value;
    }

}
