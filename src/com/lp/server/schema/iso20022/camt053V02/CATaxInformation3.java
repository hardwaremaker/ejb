//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer TaxInformation3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TaxInformation3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cdtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TaxParty1" minOccurs="0"/>
 *         &lt;element name="Dbtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TaxParty2" minOccurs="0"/>
 *         &lt;element name="AdmstnZn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Max35Text" minOccurs="0"/>
 *         &lt;element name="RefNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Max140Text" minOccurs="0"/>
 *         &lt;element name="Mtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Max35Text" minOccurs="0"/>
 *         &lt;element name="TtlTaxblBaseAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="TtlTaxAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ISODate" minOccurs="0"/>
 *         &lt;element name="SeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Number" minOccurs="0"/>
 *         &lt;element name="Rcrd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TaxRecord1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxInformation3", propOrder = {
    "cdtr",
    "dbtr",
    "admstnZn",
    "refNb",
    "mtd",
    "ttlTaxblBaseAmt",
    "ttlTaxAmt",
    "dt",
    "seqNb",
    "rcrd"
})
public class CATaxInformation3 {

    @XmlElement(name = "Cdtr")
    protected CATaxParty1 cdtr;
    @XmlElement(name = "Dbtr")
    protected CATaxParty2 dbtr;
    @XmlElement(name = "AdmstnZn")
    protected String admstnZn;
    @XmlElement(name = "RefNb")
    protected String refNb;
    @XmlElement(name = "Mtd")
    protected String mtd;
    @XmlElement(name = "TtlTaxblBaseAmt")
    protected CAActiveOrHistoricCurrencyAndAmount ttlTaxblBaseAmt;
    @XmlElement(name = "TtlTaxAmt")
    protected CAActiveOrHistoricCurrencyAndAmount ttlTaxAmt;
    @XmlElement(name = "Dt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dt;
    @XmlElement(name = "SeqNb")
    protected BigDecimal seqNb;
    @XmlElement(name = "Rcrd")
    protected List<CATaxRecord1> rcrd;

    /**
     * Ruft den Wert der cdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATaxParty1 }
     *     
     */
    public CATaxParty1 getCdtr() {
        return cdtr;
    }

    /**
     * Legt den Wert der cdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATaxParty1 }
     *     
     */
    public void setCdtr(CATaxParty1 value) {
        this.cdtr = value;
    }

    /**
     * Ruft den Wert der dbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATaxParty2 }
     *     
     */
    public CATaxParty2 getDbtr() {
        return dbtr;
    }

    /**
     * Legt den Wert der dbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATaxParty2 }
     *     
     */
    public void setDbtr(CATaxParty2 value) {
        this.dbtr = value;
    }

    /**
     * Ruft den Wert der admstnZn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdmstnZn() {
        return admstnZn;
    }

    /**
     * Legt den Wert der admstnZn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdmstnZn(String value) {
        this.admstnZn = value;
    }

    /**
     * Ruft den Wert der refNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefNb() {
        return refNb;
    }

    /**
     * Legt den Wert der refNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefNb(String value) {
        this.refNb = value;
    }

    /**
     * Ruft den Wert der mtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtd() {
        return mtd;
    }

    /**
     * Legt den Wert der mtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtd(String value) {
        this.mtd = value;
    }

    /**
     * Ruft den Wert der ttlTaxblBaseAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CAActiveOrHistoricCurrencyAndAmount getTtlTaxblBaseAmt() {
        return ttlTaxblBaseAmt;
    }

    /**
     * Legt den Wert der ttlTaxblBaseAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setTtlTaxblBaseAmt(CAActiveOrHistoricCurrencyAndAmount value) {
        this.ttlTaxblBaseAmt = value;
    }

    /**
     * Ruft den Wert der ttlTaxAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CAActiveOrHistoricCurrencyAndAmount getTtlTaxAmt() {
        return ttlTaxAmt;
    }

    /**
     * Legt den Wert der ttlTaxAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setTtlTaxAmt(CAActiveOrHistoricCurrencyAndAmount value) {
        this.ttlTaxAmt = value;
    }

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDt() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDt(XMLGregorianCalendar value) {
        this.dt = value;
    }

    /**
     * Ruft den Wert der seqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSeqNb() {
        return seqNb;
    }

    /**
     * Legt den Wert der seqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSeqNb(BigDecimal value) {
        this.seqNb = value;
    }

    /**
     * Gets the value of the rcrd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rcrd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRcrd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CATaxRecord1 }
     * 
     * 
     */
    public List<CATaxRecord1> getRcrd() {
        if (rcrd == null) {
            rcrd = new ArrayList<CATaxRecord1>();
        }
        return this.rcrd;
    }

}
