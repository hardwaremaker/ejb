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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer GenericIdentification32 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GenericIdentification32">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyType3Code" minOccurs="0"/>
 *         &lt;element name="Issr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PartyType4Code" minOccurs="0"/>
 *         &lt;element name="ShrtNm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenericIdentification32", propOrder = {
    "id",
    "tp",
    "issr",
    "shrtNm"
})
public class CAGenericIdentification32 {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "Tp")
    @XmlSchemaType(name = "string")
    protected CAPartyType3Code tp;
    @XmlElement(name = "Issr")
    @XmlSchemaType(name = "string")
    protected CAPartyType4Code issr;
    @XmlElement(name = "ShrtNm")
    protected String shrtNm;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyType3Code }
     *     
     */
    public CAPartyType3Code getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyType3Code }
     *     
     */
    public void setTp(CAPartyType3Code value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der issr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPartyType4Code }
     *     
     */
    public CAPartyType4Code getIssr() {
        return issr;
    }

    /**
     * Legt den Wert der issr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPartyType4Code }
     *     
     */
    public void setIssr(CAPartyType4Code value) {
        this.issr = value;
    }

    /**
     * Ruft den Wert der shrtNm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShrtNm() {
        return shrtNm;
    }

    /**
     * Legt den Wert der shrtNm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShrtNm(String value) {
        this.shrtNm = value;
    }

}
