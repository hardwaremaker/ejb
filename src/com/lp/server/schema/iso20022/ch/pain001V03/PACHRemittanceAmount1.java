//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer RemittanceAmount1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RemittanceAmount1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DuePyblAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="DscntApldAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="CdtNoteAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="TaxAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="AdjstmntAmtAndRsn" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}DocumentAdjustment1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RmtdAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemittanceAmount1", propOrder = {
    "duePyblAmt",
    "dscntApldAmt",
    "cdtNoteAmt",
    "taxAmt",
    "adjstmntAmtAndRsn",
    "rmtdAmt"
})
public class PACHRemittanceAmount1 {

    @XmlElement(name = "DuePyblAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount duePyblAmt;
    @XmlElement(name = "DscntApldAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount dscntApldAmt;
    @XmlElement(name = "CdtNoteAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount cdtNoteAmt;
    @XmlElement(name = "TaxAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount taxAmt;
    @XmlElement(name = "AdjstmntAmtAndRsn")
    protected List<PACHDocumentAdjustment1> adjstmntAmtAndRsn;
    @XmlElement(name = "RmtdAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount rmtdAmt;

    /**
     * Ruft den Wert der duePyblAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getDuePyblAmt() {
        return duePyblAmt;
    }

    /**
     * Legt den Wert der duePyblAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setDuePyblAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.duePyblAmt = value;
    }

    /**
     * Ruft den Wert der dscntApldAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getDscntApldAmt() {
        return dscntApldAmt;
    }

    /**
     * Legt den Wert der dscntApldAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setDscntApldAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.dscntApldAmt = value;
    }

    /**
     * Ruft den Wert der cdtNoteAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getCdtNoteAmt() {
        return cdtNoteAmt;
    }

    /**
     * Legt den Wert der cdtNoteAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setCdtNoteAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.cdtNoteAmt = value;
    }

    /**
     * Ruft den Wert der taxAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getTaxAmt() {
        return taxAmt;
    }

    /**
     * Legt den Wert der taxAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setTaxAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.taxAmt = value;
    }

    /**
     * Gets the value of the adjstmntAmtAndRsn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adjstmntAmtAndRsn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdjstmntAmtAndRsn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PACHDocumentAdjustment1 }
     * 
     * 
     */
    public List<PACHDocumentAdjustment1> getAdjstmntAmtAndRsn() {
        if (adjstmntAmtAndRsn == null) {
            adjstmntAmtAndRsn = new ArrayList<PACHDocumentAdjustment1>();
        }
        return this.adjstmntAmtAndRsn;
    }

    /**
     * Ruft den Wert der rmtdAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getRmtdAmt() {
        return rmtdAmt;
    }

    /**
     * Legt den Wert der rmtdAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setRmtdAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.rmtdAmt = value;
    }

}
