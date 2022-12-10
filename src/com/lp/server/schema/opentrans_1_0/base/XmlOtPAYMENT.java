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
 *       &lt;choice>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CARD"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ACCOUNT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DEBIT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CHECK"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CASH"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "card",
    "account",
    "debit",
    "check",
    "cash"
})
@XmlRootElement(name = "PAYMENT")
public class XmlOtPAYMENT {

    @XmlElement(name = "CARD")
    protected XmlOtCARD card;
    @XmlElement(name = "ACCOUNT")
    protected XmlOtACCOUNT account;
    @XmlElement(name = "DEBIT")
    protected XmlOtDEBIT debit;
    @XmlElement(name = "CHECK")
    protected XmlOtCHECK check;
    @XmlElement(name = "CASH")
    protected XmlOtCASH cash;

    /**
     * Ruft den Wert der card-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCARD }
     *     
     */
    public XmlOtCARD getCARD() {
        return card;
    }

    /**
     * Legt den Wert der card-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCARD }
     *     
     */
    public void setCARD(XmlOtCARD value) {
        this.card = value;
    }

    /**
     * Ruft den Wert der account-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtACCOUNT }
     *     
     */
    public XmlOtACCOUNT getACCOUNT() {
        return account;
    }

    /**
     * Legt den Wert der account-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtACCOUNT }
     *     
     */
    public void setACCOUNT(XmlOtACCOUNT value) {
        this.account = value;
    }

    /**
     * Ruft den Wert der debit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtDEBIT }
     *     
     */
    public XmlOtDEBIT getDEBIT() {
        return debit;
    }

    /**
     * Legt den Wert der debit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtDEBIT }
     *     
     */
    public void setDEBIT(XmlOtDEBIT value) {
        this.debit = value;
    }

    /**
     * Ruft den Wert der check-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCHECK }
     *     
     */
    public XmlOtCHECK getCHECK() {
        return check;
    }

    /**
     * Legt den Wert der check-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCHECK }
     *     
     */
    public void setCHECK(XmlOtCHECK value) {
        this.check = value;
    }

    /**
     * Ruft den Wert der cash-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCASH }
     *     
     */
    public XmlOtCASH getCASH() {
        return cash;
    }

    /**
     * Legt den Wert der cash-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCASH }
     *     
     */
    public void setCASH(XmlOtCASH value) {
        this.cash = value;
    }

}
