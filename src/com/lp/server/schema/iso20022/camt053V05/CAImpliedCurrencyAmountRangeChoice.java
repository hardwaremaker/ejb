//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ImpliedCurrencyAmountRangeChoice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ImpliedCurrencyAmountRangeChoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="FrAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AmountRangeBoundary1"/>
 *         &lt;element name="ToAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AmountRangeBoundary1"/>
 *         &lt;element name="FrToAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}FromToAmountRange"/>
 *         &lt;element name="EQAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ImpliedCurrencyAndAmount"/>
 *         &lt;element name="NEQAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ImpliedCurrencyAndAmount"/>
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
public class CAImpliedCurrencyAmountRangeChoice {

    @XmlElement(name = "FrAmt")
    protected CAAmountRangeBoundary1 frAmt;
    @XmlElement(name = "ToAmt")
    protected CAAmountRangeBoundary1 toAmt;
    @XmlElement(name = "FrToAmt")
    protected CAFromToAmountRange frToAmt;
    @XmlElement(name = "EQAmt")
    protected BigDecimal eqAmt;
    @XmlElement(name = "NEQAmt")
    protected BigDecimal neqAmt;

    /**
     * Ruft den Wert der frAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public CAAmountRangeBoundary1 getFrAmt() {
        return frAmt;
    }

    /**
     * Legt den Wert der frAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public void setFrAmt(CAAmountRangeBoundary1 value) {
        this.frAmt = value;
    }

    /**
     * Ruft den Wert der toAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public CAAmountRangeBoundary1 getToAmt() {
        return toAmt;
    }

    /**
     * Legt den Wert der toAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public void setToAmt(CAAmountRangeBoundary1 value) {
        this.toAmt = value;
    }

    /**
     * Ruft den Wert der frToAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAFromToAmountRange }
     *     
     */
    public CAFromToAmountRange getFrToAmt() {
        return frToAmt;
    }

    /**
     * Legt den Wert der frToAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAFromToAmountRange }
     *     
     */
    public void setFrToAmt(CAFromToAmountRange value) {
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
