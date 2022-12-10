
package com.lp.server.schema.postplc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ArrayOfColloRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfColloRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ColloRow" type="{http://post.ondot.at}ColloRow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfColloRow", propOrder = {
    "colloRow"
})
public class ArrayOfColloRow {

    @XmlElement(name = "ColloRow", nillable = true)
    protected List<ColloRow> colloRow;

    /**
     * Gets the value of the colloRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the colloRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColloRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ColloRow }
     * 
     * 
     */
    public List<ColloRow> getColloRow() {
        if (colloRow == null) {
            colloRow = new ArrayList<ColloRow>();
        }
        return this.colloRow;
    }

}
