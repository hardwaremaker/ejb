//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 11:43:37 AM CEST 
//


package com.lp.server.schema.iso20022.camt052V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BankToCustomerAccountReportV02 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BankToCustomerAccountReportV02">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrpHdr" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}GroupHeader42"/>
 *         &lt;element name="Rpt" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}AccountReport11" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankToCustomerAccountReportV02", propOrder = {
    "grpHdr",
    "rpt"
})
public class CA052BankToCustomerAccountReportV02 {

    @XmlElement(name = "GrpHdr", required = true)
    protected CA052GroupHeader42 grpHdr;
    @XmlElement(name = "Rpt", required = true)
    protected List<CA052AccountReport11> rpt;

    /**
     * Gets the value of the grpHdr property.
     * 
     * @return
     *     possible object is
     *     {@link CA052GroupHeader42 }
     *     
     */
    public CA052GroupHeader42 getGrpHdr() {
        return grpHdr;
    }

    /**
     * Sets the value of the grpHdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052GroupHeader42 }
     *     
     */
    public void setGrpHdr(CA052GroupHeader42 value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the rpt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rpt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRpt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA052AccountReport11 }
     * 
     * 
     */
    public List<CA052AccountReport11> getRpt() {
        if (rpt == null) {
            rpt = new ArrayList<CA052AccountReport11>();
        }
        return this.rpt;
    }

}
