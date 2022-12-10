//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PaymentCard4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentCard4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PlainCardData" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PlainCardData1" minOccurs="0"/>
 *         &lt;element name="CardCtryCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Exact3NumericText" minOccurs="0"/>
 *         &lt;element name="CardBrnd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}GenericIdentification1" minOccurs="0"/>
 *         &lt;element name="AddtlCardData" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max70Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentCard4", propOrder = {
    "plainCardData",
    "cardCtryCd",
    "cardBrnd",
    "addtlCardData"
})
public class CAPaymentCard4 {

    @XmlElement(name = "PlainCardData")
    protected CAPlainCardData1 plainCardData;
    @XmlElement(name = "CardCtryCd")
    protected String cardCtryCd;
    @XmlElement(name = "CardBrnd")
    protected CAGenericIdentification1 cardBrnd;
    @XmlElement(name = "AddtlCardData")
    protected String addtlCardData;

    /**
     * Ruft den Wert der plainCardData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPlainCardData1 }
     *     
     */
    public CAPlainCardData1 getPlainCardData() {
        return plainCardData;
    }

    /**
     * Legt den Wert der plainCardData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPlainCardData1 }
     *     
     */
    public void setPlainCardData(CAPlainCardData1 value) {
        this.plainCardData = value;
    }

    /**
     * Ruft den Wert der cardCtryCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardCtryCd() {
        return cardCtryCd;
    }

    /**
     * Legt den Wert der cardCtryCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardCtryCd(String value) {
        this.cardCtryCd = value;
    }

    /**
     * Ruft den Wert der cardBrnd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAGenericIdentification1 }
     *     
     */
    public CAGenericIdentification1 getCardBrnd() {
        return cardBrnd;
    }

    /**
     * Legt den Wert der cardBrnd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAGenericIdentification1 }
     *     
     */
    public void setCardBrnd(CAGenericIdentification1 value) {
        this.cardBrnd = value;
    }

    /**
     * Ruft den Wert der addtlCardData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlCardData() {
        return addtlCardData;
    }

    /**
     * Legt den Wert der addtlCardData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlCardData(String value) {
        this.addtlCardData = value;
    }

}
