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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r DisplayCapabilities1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DisplayCapabilities1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DispTp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}UserInterface2Code"/>
 *         &lt;element name="NbOfLines" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max3NumericText"/>
 *         &lt;element name="LineWidth" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max3NumericText"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisplayCapabilities1", propOrder = {
    "dispTp",
    "nbOfLines",
    "lineWidth"
})
public class CA04DisplayCapabilities1 {

    @XmlElement(name = "DispTp", required = true)
    @XmlSchemaType(name = "string")
    protected CA04UserInterface2Code dispTp;
    @XmlElement(name = "NbOfLines", required = true)
    protected String nbOfLines;
    @XmlElement(name = "LineWidth", required = true)
    protected String lineWidth;

    /**
     * Ruft den Wert der dispTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04UserInterface2Code }
     *     
     */
    public CA04UserInterface2Code getDispTp() {
        return dispTp;
    }

    /**
     * Legt den Wert der dispTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04UserInterface2Code }
     *     
     */
    public void setDispTp(CA04UserInterface2Code value) {
        this.dispTp = value;
    }

    /**
     * Ruft den Wert der nbOfLines-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbOfLines() {
        return nbOfLines;
    }

    /**
     * Legt den Wert der nbOfLines-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbOfLines(String value) {
        this.nbOfLines = value;
    }

    /**
     * Ruft den Wert der lineWidth-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineWidth() {
        return lineWidth;
    }

    /**
     * Legt den Wert der lineWidth-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineWidth(String value) {
        this.lineWidth = value;
    }

}
