//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}STOP_AUTOMATIC_PROCESSING" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}GENERATOR_INFO" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}GENERATION_DATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_ROOT" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stopautomaticprocessing",
    "generatorinfo",
    "generationdate",
    "mimeroot"
})
@XmlRootElement(name = "CONTROL_INFO")
public class XmlOtCONTROLINFO {

    @XmlElement(name = "STOP_AUTOMATIC_PROCESSING")
    protected String stopautomaticprocessing;
    @XmlElement(name = "GENERATOR_INFO")
    protected String generatorinfo;
    @XmlElement(name = "GENERATION_DATE")
    protected String generationdate;
    @XmlElement(name = "MIME_ROOT")
    protected String mimeroot;

    /**
     * Ruft den Wert der stopautomaticprocessing-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTOPAUTOMATICPROCESSING() {
        return stopautomaticprocessing;
    }

    /**
     * Legt den Wert der stopautomaticprocessing-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTOPAUTOMATICPROCESSING(String value) {
        this.stopautomaticprocessing = value;
    }

    /**
     * Ruft den Wert der generatorinfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGENERATORINFO() {
        return generatorinfo;
    }

    /**
     * Legt den Wert der generatorinfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGENERATORINFO(String value) {
        this.generatorinfo = value;
    }

    /**
     * Ruft den Wert der generationdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGENERATIONDATE() {
        return generationdate;
    }

    /**
     * Legt den Wert der generationdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGENERATIONDATE(String value) {
        this.generationdate = value;
    }

    /**
     * Ruft den Wert der mimeroot-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMEROOT() {
        return mimeroot;
    }

    /**
     * Legt den Wert der mimeroot-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMEROOT(String value) {
        this.mimeroot = value;
    }

}
