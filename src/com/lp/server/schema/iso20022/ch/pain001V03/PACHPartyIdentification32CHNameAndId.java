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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PartyIdentification32-CH_NameAndId complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PartyIdentification32-CH_NameAndId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nm" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max70Text" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Party6Choice-CH" minOccurs="0"/>
 *         &lt;element name="CtctDtls" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ContactDetails2-CH" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartyIdentification32-CH_NameAndId", propOrder = {
    "nm",
    "id",
    "ctctDtls"
})
public class PACHPartyIdentification32CHNameAndId {

    @XmlElement(name = "Nm")
    protected String nm;
    @XmlElement(name = "Id")
    protected PACHParty6ChoiceCH id;
    @XmlElement(name = "CtctDtls")
    protected PACHContactDetails2CH ctctDtls;

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
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHParty6ChoiceCH }
     *     
     */
    public PACHParty6ChoiceCH getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHParty6ChoiceCH }
     *     
     */
    public void setId(PACHParty6ChoiceCH value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der ctctDtls-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHContactDetails2CH }
     *     
     */
    public PACHContactDetails2CH getCtctDtls() {
        return ctctDtls;
    }

    /**
     * Legt den Wert der ctctDtls-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHContactDetails2CH }
     *     
     */
    public void setCtctDtls(PACHContactDetails2CH value) {
        this.ctctDtls = value;
    }

}
