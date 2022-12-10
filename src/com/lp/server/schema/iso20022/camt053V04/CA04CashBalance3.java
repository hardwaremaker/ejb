//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CashBalance3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CashBalance3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BalanceType12"/>
 *         &lt;element name="CdtLine" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CreditLine2" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CreditDebitCode"/>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DateAndDateTimeChoice"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CashBalance3", propOrder = {
    "tp",
    "cdtLine",
    "amt",
    "cdtDbtInd",
    "dt",
    "avlbty"
})
public class CA04CashBalance3 {

    @XmlElement(name = "Tp", required = true)
    protected CA04BalanceType12 tp;
    @XmlElement(name = "CdtLine")
    protected CA04CreditLine2 cdtLine;
    @XmlElement(name = "Amt", required = true)
    protected CA04ActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CA04CreditDebitCode cdtDbtInd;
    @XmlElement(name = "Dt", required = true)
    protected CA04DateAndDateTimeChoice dt;
    @XmlElement(name = "Avlbty")
    protected List<CA04CashBalanceAvailability2> avlbty;

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BalanceType12 }
     *     
     */
    public CA04BalanceType12 getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BalanceType12 }
     *     
     */
    public void setTp(CA04BalanceType12 value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der cdtLine-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CreditLine2 }
     *     
     */
    public CA04CreditLine2 getCdtLine() {
        return cdtLine;
    }

    /**
     * Legt den Wert der cdtLine-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CreditLine2 }
     *     
     */
    public void setCdtLine(CA04CreditLine2 value) {
        this.cdtLine = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CA04ActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(CA04ActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der cdtDbtInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public CA04CreditDebitCode getCdtDbtInd() {
        return cdtDbtInd;
    }

    /**
     * Legt den Wert der cdtDbtInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public void setCdtDbtInd(CA04CreditDebitCode value) {
        this.cdtDbtInd = value;
    }

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04DateAndDateTimeChoice }
     *     
     */
    public CA04DateAndDateTimeChoice getDt() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04DateAndDateTimeChoice }
     *     
     */
    public void setDt(CA04DateAndDateTimeChoice value) {
        this.dt = value;
    }

    /**
     * Gets the value of the avlbty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the avlbty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAvlbty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04CashBalanceAvailability2 }
     * 
     * 
     */
    public List<CA04CashBalanceAvailability2> getAvlbty() {
        if (avlbty == null) {
            avlbty = new ArrayList<CA04CashBalanceAvailability2>();
        }
        return this.avlbty;
    }

}
