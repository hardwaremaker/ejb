
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfCancelShipmentResult complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCancelShipmentResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CancelShipmentResult" type="{http://post.ondot.at}CancelShipmentResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCancelShipmentResult", propOrder = {
    "cancelShipmentResult"
})
public class ArrayOfCancelShipmentResult {

    @XmlElement(name = "CancelShipmentResult", nillable = true)
    protected List<CancelShipmentResult> cancelShipmentResult;

    /**
     * Gets the value of the cancelShipmentResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cancelShipmentResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCancelShipmentResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CancelShipmentResult }
     * 
     * 
     */
    public List<CancelShipmentResult> getCancelShipmentResult() {
        if (cancelShipmentResult == null) {
            cancelShipmentResult = new ArrayList<CancelShipmentResult>();
        }
        return this.cancelShipmentResult;
    }

}
