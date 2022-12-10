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
 * <p>Java-Klasse fuer CreditorReferenceType2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreditorReferenceType2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CdOrPrtry" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CreditorReferenceType1Choice"/>
 *         &lt;element name="Issr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditorReferenceType2", propOrder = {
    "cdOrPrtry",
    "issr"
})
public class PACHCreditorReferenceType2 {

    @XmlElement(name = "CdOrPrtry", required = true)
    protected PACHCreditorReferenceType1Choice cdOrPrtry;
    @XmlElement(name = "Issr")
    protected String issr;

    /**
     * Ruft den Wert der cdOrPrtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCreditorReferenceType1Choice }
     *     
     */
    public PACHCreditorReferenceType1Choice getCdOrPrtry() {
        return cdOrPrtry;
    }

    /**
     * Legt den Wert der cdOrPrtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCreditorReferenceType1Choice }
     *     
     */
    public void setCdOrPrtry(PACHCreditorReferenceType1Choice value) {
        this.cdOrPrtry = value;
    }

    /**
     * Ruft den Wert der issr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssr() {
        return issr;
    }

    /**
     * Legt den Wert der issr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssr(String value) {
        this.issr = value;
    }

}
