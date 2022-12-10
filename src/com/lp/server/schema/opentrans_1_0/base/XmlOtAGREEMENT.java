//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}AGREEMENT_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}AGREEMENT_LINE_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}AGREEMENT_START_DATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}AGREEMENT_END_DATE" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.opentrans.org/XMLSchema/1.0}dtSTRING">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="50"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agreementid",
    "agreementlineid",
    "agreementstartdate",
    "agreementenddate"
})
@XmlRootElement(name = "AGREEMENT")
public class XmlOtAGREEMENT {

    @XmlElement(name = "AGREEMENT_ID", required = true)
    protected String agreementid;
    @XmlElement(name = "AGREEMENT_LINE_ID")
    protected String agreementlineid;
    @XmlElement(name = "AGREEMENT_START_DATE")
    protected String agreementstartdate;
    @XmlElement(name = "AGREEMENT_END_DATE")
    protected String agreementenddate;
    @XmlAttribute(name = "type", required = true)
    protected String type;

    /**
     * Ruft den Wert der agreementid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGREEMENTID() {
        return agreementid;
    }

    /**
     * Legt den Wert der agreementid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGREEMENTID(String value) {
        this.agreementid = value;
    }

    /**
     * Ruft den Wert der agreementlineid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGREEMENTLINEID() {
        return agreementlineid;
    }

    /**
     * Legt den Wert der agreementlineid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGREEMENTLINEID(String value) {
        this.agreementlineid = value;
    }

    /**
     * Ruft den Wert der agreementstartdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGREEMENTSTARTDATE() {
        return agreementstartdate;
    }

    /**
     * Legt den Wert der agreementstartdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGREEMENTSTARTDATE(String value) {
        this.agreementstartdate = value;
    }

    /**
     * Ruft den Wert der agreementenddate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGREEMENTENDDATE() {
        return agreementenddate;
    }

    /**
     * Legt den Wert der agreementenddate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGREEMENTENDDATE(String value) {
        this.agreementenddate = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
