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
 * <p>Java-Klasse fuer Customers complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Customers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UpdateLegacyId" type="{http://www.vendidata.com/XML/Schema/Customers}UpdateLegacyId" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Customer" type="{http://www.vendidata.com/XML/Schema/Customers}Customer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CustomerCluster" type="{http://www.vendidata.com/XML/Schema/Customers}CustomerCluster" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Customers", propOrder = {
    "updateLegacyId",
    "customer",
    "customerCluster"
})
public class XMLCustomers {

    @XmlElement(name = "UpdateLegacyId")
    protected List<XMLUpdateLegacyId> updateLegacyId;
    @XmlElement(name = "Customer")
    protected List<XMLCustomer> customer;
    @XmlElement(name = "CustomerCluster")
    protected List<XMLCustomerCluster> customerCluster;

    /**
     * Gets the value of the updateLegacyId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateLegacyId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateLegacyId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLUpdateLegacyId }
     * 
     * 
     */
    public List<XMLUpdateLegacyId> getUpdateLegacyId() {
        if (updateLegacyId == null) {
            updateLegacyId = new ArrayList<XMLUpdateLegacyId>();
        }
        return this.updateLegacyId;
    }

    /**
     * Gets the value of the customer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLCustomer }
     * 
     * 
     */
    public List<XMLCustomer> getCustomer() {
        if (customer == null) {
            customer = new ArrayList<XMLCustomer>();
        }
        return this.customer;
    }

    /**
     * Gets the value of the customerCluster property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerCluster property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerCluster().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLCustomerCluster }
     * 
     * 
     */
    public List<XMLCustomerCluster> getCustomerCluster() {
        if (customerCluster == null) {
            customerCluster = new ArrayList<XMLCustomerCluster>();
        }
        return this.customerCluster;
    }

}
