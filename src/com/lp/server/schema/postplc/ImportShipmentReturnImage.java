
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
 *         &lt;element name="row" type="{http://post.ondot.at}ShipmentRow" minOccurs="0"/>
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
    "row"
})
@XmlRootElement(name = "ImportShipmentReturnImage")
public class ImportShipmentReturnImage {

    @XmlElementRef(name = "row", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ShipmentRow> row;

    /**
     * Ruft den Wert der row-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}
     *     
     */
    public JAXBElement<ShipmentRow> getRow() {
        return row;
    }

    /**
     * Legt den Wert der row-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}
     *     
     */
    public void setRow(JAXBElement<ShipmentRow> value) {
        this.row = value;
    }

}
