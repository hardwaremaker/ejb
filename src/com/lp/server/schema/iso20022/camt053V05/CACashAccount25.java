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
 * <p>Java-Klasse fuer CashAccount25 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CashAccount25">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AccountIdentification4Choice"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccountType2Choice" minOccurs="0"/>
 *         &lt;element name="Ccy" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveOrHistoricCurrencyCode" minOccurs="0"/>
 *         &lt;element name="Nm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max70Text" minOccurs="0"/>
 *         &lt;element name="Ownr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="Svcr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CashAccount25", propOrder = {
    "id",
    "tp",
    "ccy",
    "nm",
    "ownr",
    "svcr"
})
public class CACashAccount25 {

    @XmlElement(name = "Id", required = true)
    protected CAAccountIdentification4Choice id;
    @XmlElement(name = "Tp")
    protected CACashAccountType2Choice tp;
    @XmlElement(name = "Ccy")
    protected String ccy;
    @XmlElement(name = "Nm")
    protected String nm;
    @XmlElement(name = "Ownr")
    protected CAPartyIdentification43 ownr;
    @XmlElement(name = "Svcr")
    protected CABranchAndFinancialInstitutionIdentification5 svcr;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAccountIdentification4Choice }
     *     
     */
    public CAAccountIdentification4Choice getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAccountIdentification4Choice }
     *     
     */
    public void setId(CAAccountIdentification4Choice value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccountType2Choice }
     *     
     */
    public CACashAccountType2Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccountType2Choice }
     *     
     */
    public void setTp(CACashAccountType2Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der ccy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcy() {
        return ccy;
    }

    /**
     * Legt den Wert der ccy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcy(String value) {
        this.ccy = value;
    }

    /**
     * Ruft den Wert der nm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNm() {
        return nm;
    }

    /**
     * Legt den Wert der nm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNm(String value) {
        this.nm = value;
    }

    /**
     * Ruft den Wert der ownr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public CAPartyIdentification43 getOwnr() {
        return ownr;
    }

    /**
     * Legt den Wert der ownr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyIdentification43 }
     *     
     */
    public void setOwnr(CAPartyIdentification43 value) {
        this.ownr = value;
    }

    /**
     * Ruft den Wert der svcr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getSvcr() {
        return svcr;
    }

    /**
     * Legt den Wert der svcr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setSvcr(CABranchAndFinancialInstitutionIdentification5 value) {
        this.svcr = value;
    }

}
