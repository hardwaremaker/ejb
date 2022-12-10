//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer SupplementaryData1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SupplementaryData1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PlcAndNm" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max350Text" minOccurs="0"/>
 *         &lt;element name="Envlp" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}SupplementaryDataEnvelope1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupplementaryData1", propOrder = {
    "plcAndNm",
    "envlp"
})
public class PASupplementaryData1 {

    @XmlElement(name = "PlcAndNm")
    protected String plcAndNm;
    @XmlElement(name = "Envlp", required = true)
    protected PASupplementaryDataEnvelope1 envlp;

    /**
     * Ruft den Wert der plcAndNm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlcAndNm() {
        return plcAndNm;
    }

    /**
     * Legt den Wert der plcAndNm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlcAndNm(String value) {
        this.plcAndNm = value;
    }

    /**
     * Ruft den Wert der envlp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PASupplementaryDataEnvelope1 }
     *     
     */
    public PASupplementaryDataEnvelope1 getEnvlp() {
        return envlp;
    }

    /**
     * Legt den Wert der envlp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PASupplementaryDataEnvelope1 }
     *     
     */
    public void setEnvlp(PASupplementaryDataEnvelope1 value) {
        this.envlp = value;
    }

}
