//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ContactDetails2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContactDetails2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NmPrfx" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}NamePrefix1Code" minOccurs="0"/>
 *         &lt;element name="Nm" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max140Text" minOccurs="0"/>
 *         &lt;element name="PhneNb" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PhoneNumber" minOccurs="0"/>
 *         &lt;element name="MobNb" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PhoneNumber" minOccurs="0"/>
 *         &lt;element name="FaxNb" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PhoneNumber" minOccurs="0"/>
 *         &lt;element name="EmailAdr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max2048Text" minOccurs="0"/>
 *         &lt;element name="Othr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactDetails2", propOrder = {
    "nmPrfx",
    "nm",
    "phneNb",
    "mobNb",
    "faxNb",
    "emailAdr",
    "othr"
})
public class PACHContactDetails2 {

    @XmlElement(name = "NmPrfx")
    @XmlSchemaType(name = "string")
    protected PACHNamePrefix1Code nmPrfx;
    @XmlElement(name = "Nm")
    protected String nm;
    @XmlElement(name = "PhneNb")
    protected String phneNb;
    @XmlElement(name = "MobNb")
    protected String mobNb;
    @XmlElement(name = "FaxNb")
    protected String faxNb;
    @XmlElement(name = "EmailAdr")
    protected String emailAdr;
    @XmlElement(name = "Othr")
    protected String othr;

    /**
     * Ruft den Wert der nmPrfx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHNamePrefix1Code }
     *     
     */
    public PACHNamePrefix1Code getNmPrfx() {
        return nmPrfx;
    }

    /**
     * Legt den Wert der nmPrfx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHNamePrefix1Code }
     *     
     */
    public void setNmPrfx(PACHNamePrefix1Code value) {
        this.nmPrfx = value;
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
     * Ruft den Wert der phneNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhneNb() {
        return phneNb;
    }

    /**
     * Legt den Wert der phneNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhneNb(String value) {
        this.phneNb = value;
    }

    /**
     * Ruft den Wert der mobNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobNb() {
        return mobNb;
    }

    /**
     * Legt den Wert der mobNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobNb(String value) {
        this.mobNb = value;
    }

    /**
     * Ruft den Wert der faxNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNb() {
        return faxNb;
    }

    /**
     * Legt den Wert der faxNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNb(String value) {
        this.faxNb = value;
    }

    /**
     * Ruft den Wert der emailAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAdr() {
        return emailAdr;
    }

    /**
     * Legt den Wert der emailAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAdr(String value) {
        this.emailAdr = value;
    }

    /**
     * Ruft den Wert der othr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOthr() {
        return othr;
    }

    /**
     * Legt den Wert der othr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOthr(String value) {
        this.othr = value;
    }

}
