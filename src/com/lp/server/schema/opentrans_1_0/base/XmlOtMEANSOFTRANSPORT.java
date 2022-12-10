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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MEANS_OF_TRANSPORT_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MEANS_OF_TRANSPORT_NAME" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type">
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
    "meansoftransportid",
    "meansoftransportname"
})
@XmlRootElement(name = "MEANS_OF_TRANSPORT")
public class XmlOtMEANSOFTRANSPORT {

    @XmlElement(name = "MEANS_OF_TRANSPORT_ID", required = true)
    protected String meansoftransportid;
    @XmlElement(name = "MEANS_OF_TRANSPORT_NAME")
    protected String meansoftransportname;
    @XmlAttribute(name = "type")
    protected String type;

    /**
     * Ruft den Wert der meansoftransportid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEANSOFTRANSPORTID() {
        return meansoftransportid;
    }

    /**
     * Legt den Wert der meansoftransportid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEANSOFTRANSPORTID(String value) {
        this.meansoftransportid = value;
    }

    /**
     * Ruft den Wert der meansoftransportname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEANSOFTRANSPORTNAME() {
        return meansoftransportname;
    }

    /**
     * Legt den Wert der meansoftransportname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEANSOFTRANSPORTNAME(String value) {
        this.meansoftransportname = value;
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
