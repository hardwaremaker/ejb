//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r TaxParty2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TaxParty2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaxId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *         &lt;element name="RegnId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *         &lt;element name="TaxTp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *         &lt;element name="Authstn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TaxAuthorisation1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxParty2", propOrder = {
    "taxId",
    "regnId",
    "taxTp",
    "authstn"
})
public class CA04TaxParty2 {

    @XmlElement(name = "TaxId")
    protected String taxId;
    @XmlElement(name = "RegnId")
    protected String regnId;
    @XmlElement(name = "TaxTp")
    protected String taxTp;
    @XmlElement(name = "Authstn")
    protected CA04TaxAuthorisation1 authstn;

    /**
     * Ruft den Wert der taxId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Legt den Wert der taxId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxId(String value) {
        this.taxId = value;
    }

    /**
     * Ruft den Wert der regnId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegnId() {
        return regnId;
    }

    /**
     * Legt den Wert der regnId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegnId(String value) {
        this.regnId = value;
    }

    /**
     * Ruft den Wert der taxTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxTp() {
        return taxTp;
    }

    /**
     * Legt den Wert der taxTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxTp(String value) {
        this.taxTp = value;
    }

    /**
     * Ruft den Wert der authstn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TaxAuthorisation1 }
     *     
     */
    public CA04TaxAuthorisation1 getAuthstn() {
        return authstn;
    }

    /**
     * Legt den Wert der authstn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TaxAuthorisation1 }
     *     
     */
    public void setAuthstn(CA04TaxAuthorisation1 value) {
        this.authstn = value;
    }

}
