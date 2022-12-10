
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
 *         &lt;element name="CancelShipmentsResult" type="{http://post.ondot.at}ArrayOfCancelShipmentResult" minOccurs="0"/>
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
    "cancelShipmentsResult"
})
@XmlRootElement(name = "CancelShipmentsResponse")
public class CancelShipmentsResponse {

    @XmlElementRef(name = "CancelShipmentsResult", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfCancelShipmentResult> cancelShipmentsResult;

    /**
     * Ruft den Wert der cancelShipmentsResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentResult }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCancelShipmentResult> getCancelShipmentsResult() {
        return cancelShipmentsResult;
    }

    /**
     * Legt den Wert der cancelShipmentsResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentResult }{@code >}
     *     
     */
    public void setCancelShipmentsResult(JAXBElement<ArrayOfCancelShipmentResult> value) {
        this.cancelShipmentsResult = value;
    }

}
