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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}HOLDER"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BANK_NAME"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BANK_COUNTRY"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BANK_CODE"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BANK_ACCOUNT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PAYMENT_TERM" minOccurs="0"/>
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
    "holder",
    "bankname",
    "bankcountry",
    "bankcode",
    "bankaccount",
    "paymentterm"
})
@XmlRootElement(name = "ACCOUNT")
public class XmlOtACCOUNT {

    @XmlElement(name = "HOLDER", required = true)
    protected String holder;
    @XmlElement(name = "BANK_NAME", required = true)
    protected String bankname;
    @XmlElement(name = "BANK_COUNTRY", required = true)
    protected String bankcountry;
    @XmlElement(name = "BANK_CODE", required = true)
    protected String bankcode;
    @XmlElement(name = "BANK_ACCOUNT", required = true)
    protected String bankaccount;
    @XmlElement(name = "PAYMENT_TERM")
    protected XmlOtPAYMENTTERM paymentterm;

    /**
     * Ruft den Wert der holder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHOLDER() {
        return holder;
    }

    /**
     * Legt den Wert der holder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHOLDER(String value) {
        this.holder = value;
    }

    /**
     * Ruft den Wert der bankname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKNAME() {
        return bankname;
    }

    /**
     * Legt den Wert der bankname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKNAME(String value) {
        this.bankname = value;
    }

    /**
     * Ruft den Wert der bankcountry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKCOUNTRY() {
        return bankcountry;
    }

    /**
     * Legt den Wert der bankcountry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKCOUNTRY(String value) {
        this.bankcountry = value;
    }

    /**
     * Ruft den Wert der bankcode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKCODE() {
        return bankcode;
    }

    /**
     * Legt den Wert der bankcode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKCODE(String value) {
        this.bankcode = value;
    }

    /**
     * Ruft den Wert der bankaccount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKACCOUNT() {
        return bankaccount;
    }

    /**
     * Legt den Wert der bankaccount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKACCOUNT(String value) {
        this.bankaccount = value;
    }

    /**
     * Ruft den Wert der paymentterm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtPAYMENTTERM }
     *     
     */
    public XmlOtPAYMENTTERM getPAYMENTTERM() {
        return paymentterm;
    }

    /**
     * Legt den Wert der paymentterm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtPAYMENTTERM }
     *     
     */
    public void setPAYMENTTERM(XmlOtPAYMENTTERM value) {
        this.paymentterm = value;
    }

}
