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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MANUFACTURER_NAME"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MANUFACTURER_AID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MANUFACTURER_TYPE_DESCR" minOccurs="0"/>
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
    "manufacturername",
    "manufactureraid",
    "manufacturertypedescr"
})
@XmlRootElement(name = "MANUFACTURER_INFO")
public class XmlOtMANUFACTURERINFO {

    @XmlElement(name = "MANUFACTURER_NAME", required = true)
    protected String manufacturername;
    @XmlElement(name = "MANUFACTURER_AID", required = true)
    protected String manufactureraid;
    @XmlElement(name = "MANUFACTURER_TYPE_DESCR")
    protected String manufacturertypedescr;

    /**
     * Ruft den Wert der manufacturername-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMANUFACTURERNAME() {
        return manufacturername;
    }

    /**
     * Legt den Wert der manufacturername-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMANUFACTURERNAME(String value) {
        this.manufacturername = value;
    }

    /**
     * Ruft den Wert der manufactureraid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMANUFACTURERAID() {
        return manufactureraid;
    }

    /**
     * Legt den Wert der manufactureraid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMANUFACTURERAID(String value) {
        this.manufactureraid = value;
    }

    /**
     * Ruft den Wert der manufacturertypedescr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMANUFACTURERTYPEDESCR() {
        return manufacturertypedescr;
    }

    /**
     * Legt den Wert der manufacturertypedescr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMANUFACTURERTYPEDESCR(String value) {
        this.manufacturertypedescr = value;
    }

}
