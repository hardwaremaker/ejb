
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfCarrierServiceRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCarrierServiceRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CarrierServiceRow" type="{http://post.ondot.at}CarrierServiceRow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCarrierServiceRow", propOrder = {
    "carrierServiceRow"
})
public class ArrayOfCarrierServiceRow {

    @XmlElement(name = "CarrierServiceRow", nillable = true)
    protected List<CarrierServiceRow> carrierServiceRow;

    /**
     * Gets the value of the carrierServiceRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the carrierServiceRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCarrierServiceRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CarrierServiceRow }
     * 
     * 
     */
    public List<CarrierServiceRow> getCarrierServiceRow() {
        if (carrierServiceRow == null) {
            carrierServiceRow = new ArrayList<CarrierServiceRow>();
        }
        return this.carrierServiceRow;
    }

}
