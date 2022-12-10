//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Represents a set of customers, grouped togethter in a so called "Customer Cluster". Each customer cluster must have one master customer. A customer cluster itself lack's a separated defined identity attribute, it can only be identified by it's members.
 * 
 * <p>Java-Klasse fuer CustomerCluster complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CustomerCluster">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MasterCustomer" type="{http://www.vendidata.com/XML/Schema/Customers}Customer"/>
 *         &lt;element name="MemberCustomer" type="{http://www.vendidata.com/XML/Schema/Customers}Customer" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerCluster", propOrder = {
    "masterCustomer",
    "memberCustomer"
})
public class XMLCustomerCluster {

    @XmlElement(name = "MasterCustomer", required = true)
    protected XMLCustomer masterCustomer;
    @XmlElement(name = "MemberCustomer")
    protected List<XMLCustomer> memberCustomer;

    /**
     * Ruft den Wert der masterCustomer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLCustomer }
     *     
     */
    public XMLCustomer getMasterCustomer() {
        return masterCustomer;
    }

    /**
     * Legt den Wert der masterCustomer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCustomer }
     *     
     */
    public void setMasterCustomer(XMLCustomer value) {
        this.masterCustomer = value;
    }

    /**
     * Gets the value of the memberCustomer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the memberCustomer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMemberCustomer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLCustomer }
     * 
     * 
     */
    public List<XMLCustomer> getMemberCustomer() {
        if (memberCustomer == null) {
            memberCustomer = new ArrayList<XMLCustomer>();
        }
        return this.memberCustomer;
    }

}
