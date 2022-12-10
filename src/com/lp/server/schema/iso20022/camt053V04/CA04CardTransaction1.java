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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r CardTransaction1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardTransaction1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Card" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}PaymentCard4" minOccurs="0"/>
 *         &lt;element name="POI" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}PointOfInteraction1" minOccurs="0"/>
 *         &lt;element name="Tx" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardTransaction1Choice" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardTransaction1", propOrder = {
    "card",
    "poi",
    "tx"
})
public class CA04CardTransaction1 {

    @XmlElement(name = "Card")
    protected CA04PaymentCard4 card;
    @XmlElement(name = "POI")
    protected CA04PointOfInteraction1 poi;
    @XmlElement(name = "Tx")
    protected CA04CardTransaction1Choice tx;

    /**
     * Ruft den Wert der card-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04PaymentCard4 }
     *     
     */
    public CA04PaymentCard4 getCard() {
        return card;
    }

    /**
     * Legt den Wert der card-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04PaymentCard4 }
     *     
     */
    public void setCard(CA04PaymentCard4 value) {
        this.card = value;
    }

    /**
     * Ruft den Wert der poi-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04PointOfInteraction1 }
     *     
     */
    public CA04PointOfInteraction1 getPOI() {
        return poi;
    }

    /**
     * Legt den Wert der poi-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04PointOfInteraction1 }
     *     
     */
    public void setPOI(CA04PointOfInteraction1 value) {
        this.poi = value;
    }

    /**
     * Ruft den Wert der tx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardTransaction1Choice }
     *     
     */
    public CA04CardTransaction1Choice getTx() {
        return tx;
    }

    /**
     * Legt den Wert der tx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardTransaction1Choice }
     *     
     */
    public void setTx(CA04CardTransaction1Choice value) {
        this.tx = value;
    }

}
