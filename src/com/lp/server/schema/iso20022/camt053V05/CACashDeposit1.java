//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CashDeposit1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CashDeposit1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoteDnmtn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveCurrencyAndAmount"/>
 *         &lt;element name="NbOfNotes" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max15NumericText"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveCurrencyAndAmount"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CashDeposit1", propOrder = {
    "noteDnmtn",
    "nbOfNotes",
    "amt"
})
public class CACashDeposit1 {

    @XmlElement(name = "NoteDnmtn", required = true)
    protected CAActiveCurrencyAndAmount noteDnmtn;
    @XmlElement(name = "NbOfNotes", required = true)
    protected String nbOfNotes;
    @XmlElement(name = "Amt", required = true)
    protected CAActiveCurrencyAndAmount amt;

    /**
     * Ruft den Wert der noteDnmtn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveCurrencyAndAmount }
     *     
     */
    public CAActiveCurrencyAndAmount getNoteDnmtn() {
        return noteDnmtn;
    }

    /**
     * Legt den Wert der noteDnmtn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveCurrencyAndAmount }
     *     
     */
    public void setNoteDnmtn(CAActiveCurrencyAndAmount value) {
        this.noteDnmtn = value;
    }

    /**
     * Ruft den Wert der nbOfNotes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbOfNotes() {
        return nbOfNotes;
    }

    /**
     * Legt den Wert der nbOfNotes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbOfNotes(String value) {
        this.nbOfNotes = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveCurrencyAndAmount }
     *     
     */
    public CAActiveCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveCurrencyAndAmount }
     *     
     */
    public void setAmt(CAActiveCurrencyAndAmount value) {
        this.amt = value;
    }

}
