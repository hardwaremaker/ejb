//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer TransactionParties3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionParties3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InitgPty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="Dbtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="DbtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccount24" minOccurs="0"/>
 *         &lt;element name="UltmtDbtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="Cdtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="CdtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccount24" minOccurs="0"/>
 *         &lt;element name="UltmtCdtr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="TradgPty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ProprietaryParty3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionParties3", propOrder = {
    "initgPty",
    "dbtr",
    "dbtrAcct",
    "ultmtDbtr",
    "cdtr",
    "cdtrAcct",
    "ultmtCdtr",
    "tradgPty",
    "prtry"
})
public class CATransactionParties3 {

    @XmlElement(name = "InitgPty")
    protected CAPartyIdentification43 initgPty;
    @XmlElement(name = "Dbtr")
    protected CAPartyIdentification43 dbtr;
    @XmlElement(name = "DbtrAcct")
    protected CACashAccount24 dbtrAcct;
    @XmlElement(name = "UltmtDbtr")
    protected CAPartyIdentification43 ultmtDbtr;
    @XmlElement(name = "Cdtr")
    protected CAPartyIdentification43 cdtr;
    @XmlElement(name = "CdtrAcct")
    protected CACashAccount24 cdtrAcct;
    @XmlElement(name = "UltmtCdtr")
    protected CAPartyIdentification43 ultmtCdtr;
    @XmlElement(name = "TradgPty")
    protected CAPartyIdentification43 tradgPty;
    @XmlElement(name = "Prtry")
    protected List<CAProprietaryParty3> prtry;

    /**
     * Ruft den Wert der initgPty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getInitgPty() {
        return initgPty;
    }

    /**
     * Legt den Wert der initgPty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setInitgPty(CAPartyIdentification43 value) {
        this.initgPty = value;
    }

    /**
     * Ruft den Wert der dbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getDbtr() {
        return dbtr;
    }

    /**
     * Legt den Wert der dbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setDbtr(CAPartyIdentification43 value) {
        this.dbtr = value;
    }

    /**
     * Ruft den Wert der dbtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount24 }
     *     
     */
    public CACashAccount24 getDbtrAcct() {
        return dbtrAcct;
    }

    /**
     * Legt den Wert der dbtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount24 }
     *     
     */
    public void setDbtrAcct(CACashAccount24 value) {
        this.dbtrAcct = value;
    }

    /**
     * Ruft den Wert der ultmtDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getUltmtDbtr() {
        return ultmtDbtr;
    }

    /**
     * Legt den Wert der ultmtDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setUltmtDbtr(CAPartyIdentification43 value) {
        this.ultmtDbtr = value;
    }

    /**
     * Ruft den Wert der cdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getCdtr() {
        return cdtr;
    }

    /**
     * Legt den Wert der cdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setCdtr(CAPartyIdentification43 value) {
        this.cdtr = value;
    }

    /**
     * Ruft den Wert der cdtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount24 }
     *     
     */
    public CACashAccount24 getCdtrAcct() {
        return cdtrAcct;
    }

    /**
     * Legt den Wert der cdtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount24 }
     *     
     */
    public void setCdtrAcct(CACashAccount24 value) {
        this.cdtrAcct = value;
    }

    /**
     * Ruft den Wert der ultmtCdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getUltmtCdtr() {
        return ultmtCdtr;
    }

    /**
     * Legt den Wert der ultmtCdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setUltmtCdtr(CAPartyIdentification43 value) {
        this.ultmtCdtr = value;
    }

    /**
     * Ruft den Wert der tradgPty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getTradgPty() {
        return tradgPty;
    }

    /**
     * Legt den Wert der tradgPty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setTradgPty(CAPartyIdentification43 value) {
        this.tradgPty = value;
    }

    /**
     * Gets the value of the prtry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prtry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrtry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CAProprietaryParty3 }
     * 
     * 
     */
    public List<CAProprietaryParty3> getPrtry() {
        if (prtry == null) {
            prtry = new ArrayList<CAProprietaryParty3>();
        }
        return this.prtry;
    }

}
