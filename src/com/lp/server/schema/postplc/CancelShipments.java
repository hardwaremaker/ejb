
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shipments" type="{http://post.ondot.at}ArrayOfCancelShipmentRow" minOccurs="0"/>
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
    "shipments"
})
@XmlRootElement(name = "CancelShipments")
public class CancelShipments {

    @XmlElementRef(name = "shipments", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfCancelShipmentRow> shipments;

    /**
     * Ruft den Wert der shipments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCancelShipmentRow> getShipments() {
        return shipments;
    }

    /**
     * Legt den Wert der shipments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentRow }{@code >}
     *     
     */
    public void setShipments(JAXBElement<ArrayOfCancelShipmentRow> value) {
        this.shipments = value;
    }

}
