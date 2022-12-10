//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PartyIdentification32 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PartyIdentification32">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Max140Text" minOccurs="0"/>
 *         &lt;element name="PstlAdr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}PostalAddress6" minOccurs="0"/>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Party6Choice" minOccurs="0"/>
 *         &lt;element name="CtryOfRes" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CountryCode" minOccurs="0"/>
 *         &lt;element name="CtctDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ContactDetails2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartyIdentification32", propOrder = {
    "nm",
    "pstlAdr",
    "id",
    "ctryOfRes",
    "ctctDtls"
})
public class CAPartyIdentification32 {

    @XmlElement(name = "Nm")
    protected String nm;
    @XmlElement(name = "PstlAdr")
    protected CAPostalAddress6 pstlAdr;
    @XmlElement(name = "Id")
    protected CAParty6Choice id;
    @XmlElement(name = "CtryOfRes")
    protected String ctryOfRes;
    @XmlElement(name = "CtctDtls")
    protected CAContactDetails2 ctctDtls;

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
     * Ruft den Wert der pstlAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPostalAddress6 }
     *     
     */
    public CAPostalAddress6 getPstlAdr() {
        return pstlAdr;
    }

    /**
     * Legt den Wert der pstlAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPostalAddress6 }
     *     
     */
    public void setPstlAdr(CAPostalAddress6 value) {
        this.pstlAdr = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAParty6Choice }
     *     
     */
    public CAParty6Choice getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAParty6Choice }
     *     
     */
    public void setId(CAParty6Choice value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der ctryOfRes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtryOfRes() {
        return ctryOfRes;
    }

    /**
     * Legt den Wert der ctryOfRes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtryOfRes(String value) {
        this.ctryOfRes = value;
    }

    /**
     * Ruft den Wert der ctctDtls-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAContactDetails2 }
     *     
     */
    public CAContactDetails2 getCtctDtls() {
        return ctctDtls;
    }

    /**
     * Legt den Wert der ctctDtls-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAContactDetails2 }
     *     
     */
    public void setCtctDtls(CAContactDetails2 value) {
        this.ctctDtls = value;
    }

}
