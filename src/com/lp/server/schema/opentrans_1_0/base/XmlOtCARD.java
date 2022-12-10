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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_NUM"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_AUTH_CODE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_REF_NUM" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_EXPIRATION_DATE"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_TYPE"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_TYPE_OTHER" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD_HOLDER_NAME"/>
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
    "cardnum",
    "cardauthcode",
    "cardrefnum",
    "cardexpirationdate",
    "cardtype",
    "cardtypeother",
    "cardholdername"
})
@XmlRootElement(name = "CARD")
public class XmlOtCARD {

    @XmlElement(name = "CARD_NUM", required = true)
    protected String cardnum;
    @XmlElement(name = "CARD_AUTH_CODE")
    protected String cardauthcode;
    @XmlElement(name = "CARD_REF_NUM")
    protected String cardrefnum;
    @XmlElement(name = "CARD_EXPIRATION_DATE", required = true)
    protected String cardexpirationdate;
    @XmlElement(name = "CARD_TYPE", required = true)
    protected String cardtype;
    @XmlElement(name = "CARD_TYPE_OTHER")
    protected String cardtypeother;
    @XmlElement(name = "CARD_HOLDER_NAME", required = true)
    protected String cardholdername;

    /**
     * Ruft den Wert der cardnum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDNUM() {
        return cardnum;
    }

    /**
     * Legt den Wert der cardnum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDNUM(String value) {
        this.cardnum = value;
    }

    /**
     * Ruft den Wert der cardauthcode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDAUTHCODE() {
        return cardauthcode;
    }

    /**
     * Legt den Wert der cardauthcode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDAUTHCODE(String value) {
        this.cardauthcode = value;
    }

    /**
     * Ruft den Wert der cardrefnum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDREFNUM() {
        return cardrefnum;
    }

    /**
     * Legt den Wert der cardrefnum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDREFNUM(String value) {
        this.cardrefnum = value;
    }

    /**
     * Ruft den Wert der cardexpirationdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDEXPIRATIONDATE() {
        return cardexpirationdate;
    }

    /**
     * Legt den Wert der cardexpirationdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDEXPIRATIONDATE(String value) {
        this.cardexpirationdate = value;
    }

    /**
     * Ruft den Wert der cardtype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDTYPE() {
        return cardtype;
    }

    /**
     * Legt den Wert der cardtype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDTYPE(String value) {
        this.cardtype = value;
    }

    /**
     * Ruft den Wert der cardtypeother-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDTYPEOTHER() {
        return cardtypeother;
    }

    /**
     * Legt den Wert der cardtypeother-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDTYPEOTHER(String value) {
        this.cardtypeother = value;
    }

    /**
     * Ruft den Wert der cardholdername-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCARDHOLDERNAME() {
        return cardholdername;
    }

    /**
     * Legt den Wert der cardholdername-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCARDHOLDERNAME(String value) {
        this.cardholdername = value;
    }

}
