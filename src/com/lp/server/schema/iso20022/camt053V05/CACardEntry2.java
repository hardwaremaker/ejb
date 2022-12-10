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
 * <p>Java-Klasse fuer CardEntry2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardEntry2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Card" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PaymentCard4" minOccurs="0"/>
 *         &lt;element name="POI" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PointOfInteraction1" minOccurs="0"/>
 *         &lt;element name="AggtdNtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardAggregated1" minOccurs="0"/>
 *         &lt;element name="PrePdAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccount24" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardEntry2", propOrder = {
    "card",
    "poi",
    "aggtdNtry",
    "prePdAcct"
})
public class CACardEntry2 {

    @XmlElement(name = "Card")
    protected CAPaymentCard4 card;
    @XmlElement(name = "POI")
    protected CAPointOfInteraction1 poi;
    @XmlElement(name = "AggtdNtry")
    protected CACardAggregated1 aggtdNtry;
    @XmlElement(name = "PrePdAcct")
    protected CACashAccount24 prePdAcct;

    /**
     * Ruft den Wert der card-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPaymentCard4 }
     *     
     */
    public CAPaymentCard4 getCard() {
        return card;
    }

    /**
     * Legt den Wert der card-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPaymentCard4 }
     *     
     */
    public void setCard(CAPaymentCard4 value) {
        this.card = value;
    }

    /**
     * Ruft den Wert der poi-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPointOfInteraction1 }
     *     
     */
    public CAPointOfInteraction1 getPOI() {
        return poi;
    }

    /**
     * Legt den Wert der poi-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPointOfInteraction1 }
     *     
     */
    public void setPOI(CAPointOfInteraction1 value) {
        this.poi = value;
    }

    /**
     * Ruft den Wert der aggtdNtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardAggregated1 }
     *     
     */
    public CACardAggregated1 getAggtdNtry() {
        return aggtdNtry;
    }

    /**
     * Legt den Wert der aggtdNtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardAggregated1 }
     *     
     */
    public void setAggtdNtry(CACardAggregated1 value) {
        this.aggtdNtry = value;
    }

    /**
     * Ruft den Wert der prePdAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount24 }
     *     
     */
    public CACashAccount24 getPrePdAcct() {
        return prePdAcct;
    }

    /**
     * Legt den Wert der prePdAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount24 }
     *     
     */
    public void setPrePdAcct(CACashAccount24 value) {
        this.prePdAcct = value;
    }

}
