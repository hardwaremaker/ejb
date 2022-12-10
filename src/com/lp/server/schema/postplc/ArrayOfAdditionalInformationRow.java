
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfAdditionalInformationRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfAdditionalInformationRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AdditionalInformationRow" type="{http://post.ondot.at}AdditionalInformationRow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfAdditionalInformationRow", propOrder = {
    "additionalInformationRow"
})
public class ArrayOfAdditionalInformationRow {

    @XmlElement(name = "AdditionalInformationRow", nillable = true)
    protected List<AdditionalInformationRow> additionalInformationRow;

    /**
     * Gets the value of the additionalInformationRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalInformationRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalInformationRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalInformationRow }
     * 
     * 
     */
    public List<AdditionalInformationRow> getAdditionalInformationRow() {
        if (additionalInformationRow == null) {
            additionalInformationRow = new ArrayList<AdditionalInformationRow>();
        }
        return this.additionalInformationRow;
    }

}
