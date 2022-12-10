
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfShipmentDocumentEntry complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfShipmentDocumentEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShipmentDocumentEntry" type="{http://Core.Model}ShipmentDocumentEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfShipmentDocumentEntry", namespace = "http://Core.Model", propOrder = {
    "shipmentDocumentEntry"
})
public class ArrayOfShipmentDocumentEntry {

    @XmlElement(name = "ShipmentDocumentEntry", nillable = true)
    protected List<ShipmentDocumentEntry> shipmentDocumentEntry;

    /**
     * Gets the value of the shipmentDocumentEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shipmentDocumentEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShipmentDocumentEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShipmentDocumentEntry }
     * 
     * 
     */
    public List<ShipmentDocumentEntry> getShipmentDocumentEntry() {
        if (shipmentDocumentEntry == null) {
            shipmentDocumentEntry = new ArrayList<ShipmentDocumentEntry>();
        }
        return this.shipmentDocumentEntry;
    }

}
