
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfCancelShipmentRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCancelShipmentRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CancelShipmentRow" type="{http://post.ondot.at}CancelShipmentRow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCancelShipmentRow", propOrder = {
    "cancelShipmentRow"
})
public class ArrayOfCancelShipmentRow {

    @XmlElement(name = "CancelShipmentRow", nillable = true)
    protected List<CancelShipmentRow> cancelShipmentRow;

    /**
     * Gets the value of the cancelShipmentRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cancelShipmentRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCancelShipmentRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CancelShipmentRow }
     * 
     * 
     */
    public List<CancelShipmentRow> getCancelShipmentRow() {
        if (cancelShipmentRow == null) {
            cancelShipmentRow = new ArrayList<CancelShipmentRow>();
        }
        return this.cancelShipmentRow;
    }

}
