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
 * <p>Java-Klasse fuer PersonIdentification5-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentification5-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DtAndPlcOfBirth" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}DateAndPlaceOfBirth" minOccurs="0"/>
 *         &lt;element name="Othr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}GenericPersonIdentification1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentification5-CH", propOrder = {
    "dtAndPlcOfBirth",
    "othr"
})
public class PACHPersonIdentification5CH {

    @XmlElement(name = "DtAndPlcOfBirth")
    protected PACHDateAndPlaceOfBirth dtAndPlcOfBirth;
    @XmlElement(name = "Othr")
    protected PACHGenericPersonIdentification1 othr;

    /**
     * Ruft den Wert der dtAndPlcOfBirth-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHDateAndPlaceOfBirth }
     *     
     */
    public PACHDateAndPlaceOfBirth getDtAndPlcOfBirth() {
        return dtAndPlcOfBirth;
    }

    /**
     * Legt den Wert der dtAndPlcOfBirth-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHDateAndPlaceOfBirth }
     *     
     */
    public void setDtAndPlcOfBirth(PACHDateAndPlaceOfBirth value) {
        this.dtAndPlcOfBirth = value;
    }

    /**
     * Ruft den Wert der othr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHGenericPersonIdentification1 }
     *     
     */
    public PACHGenericPersonIdentification1 getOthr() {
        return othr;
    }

    /**
     * Legt den Wert der othr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHGenericPersonIdentification1 }
     *     
     */
    public void setOthr(PACHGenericPersonIdentification1 value) {
        this.othr = value;
    }

}
