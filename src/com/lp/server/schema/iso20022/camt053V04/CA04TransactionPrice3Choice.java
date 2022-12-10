//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für TransactionPrice3Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionPrice3Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="DealPric" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Price2"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ProprietaryPrice2" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionPrice3Choice", propOrder = {
    "dealPric",
    "prtry"
})
public class CA04TransactionPrice3Choice {

    @XmlElement(name = "DealPric")
    protected CA04Price2 dealPric;
    @XmlElement(name = "Prtry")
    protected List<CA04ProprietaryPrice2> prtry;

    /**
     * Ruft den Wert der dealPric-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04Price2 }
     *     
     */
    public CA04Price2 getDealPric() {
        return dealPric;
    }

    /**
     * Legt den Wert der dealPric-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04Price2 }
     *     
     */
    public void setDealPric(CA04Price2 value) {
        this.dealPric = value;
    }

    /**
     * Gets the value of the prtry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prtry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrtry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04ProprietaryPrice2 }
     * 
     * 
     */
    public List<CA04ProprietaryPrice2> getPrtry() {
        if (prtry == null) {
            prtry = new ArrayList<CA04ProprietaryPrice2>();
        }
        return this.prtry;
    }

}
