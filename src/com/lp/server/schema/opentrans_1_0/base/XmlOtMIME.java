//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import java.math.BigInteger;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_TYPE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_SOURCE"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_DESCR" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_ALT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_PURPOSE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_ORDER" minOccurs="0"/>
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
    "mimetype",
    "mimesource",
    "mimedescr",
    "mimealt",
    "mimepurpose",
    "mimeorder"
})
@XmlRootElement(name = "MIME")
public class XmlOtMIME {

    @XmlElement(name = "MIME_TYPE")
    protected String mimetype;
    @XmlElement(name = "MIME_SOURCE", required = true)
    protected String mimesource;
    @XmlElement(name = "MIME_DESCR")
    protected String mimedescr;
    @XmlElement(name = "MIME_ALT")
    protected String mimealt;
    @XmlElement(name = "MIME_PURPOSE")
    protected String mimepurpose;
    @XmlElement(name = "MIME_ORDER")
    protected BigInteger mimeorder;

    /**
     * Ruft den Wert der mimetype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMETYPE() {
        return mimetype;
    }

    /**
     * Legt den Wert der mimetype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMETYPE(String value) {
        this.mimetype = value;
    }

    /**
     * Ruft den Wert der mimesource-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMESOURCE() {
        return mimesource;
    }

    /**
     * Legt den Wert der mimesource-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMESOURCE(String value) {
        this.mimesource = value;
    }

    /**
     * Ruft den Wert der mimedescr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMEDESCR() {
        return mimedescr;
    }

    /**
     * Legt den Wert der mimedescr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMEDESCR(String value) {
        this.mimedescr = value;
    }

    /**
     * Ruft den Wert der mimealt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMEALT() {
        return mimealt;
    }

    /**
     * Legt den Wert der mimealt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMEALT(String value) {
        this.mimealt = value;
    }

    /**
     * Ruft den Wert der mimepurpose-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMIMEPURPOSE() {
        return mimepurpose;
    }

    /**
     * Legt den Wert der mimepurpose-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMIMEPURPOSE(String value) {
        this.mimepurpose = value;
    }

    /**
     * Ruft den Wert der mimeorder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMIMEORDER() {
        return mimeorder;
    }

    /**
     * Legt den Wert der mimeorder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMIMEORDER(BigInteger value) {
        this.mimeorder = value;
    }

}
