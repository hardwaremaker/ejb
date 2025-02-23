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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Stand complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Stand">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;sequence>
 *           &lt;element name="VirtualVendingmachine" type="{http://www.vendidata.com/XML/Schema/Customers}VirtualVendingmachine" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="waterSupplyExists" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Stand", propOrder = {
    "name",
    "virtualVendingmachine"
})
public class XMLStand {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "VirtualVendingmachine")
    protected List<XMLVirtualVendingmachine> virtualVendingmachine;
    @XmlAttribute(name = "waterSupplyExists", required = true)
    protected boolean waterSupplyExists;

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the virtualVendingmachine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualVendingmachine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualVendingmachine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLVirtualVendingmachine }
     * 
     * 
     */
    public List<XMLVirtualVendingmachine> getVirtualVendingmachine() {
        if (virtualVendingmachine == null) {
            virtualVendingmachine = new ArrayList<XMLVirtualVendingmachine>();
        }
        return this.virtualVendingmachine;
    }

    /**
     * Ruft den Wert der waterSupplyExists-Eigenschaft ab.
     * 
     */
    public boolean isWaterSupplyExists() {
        return waterSupplyExists;
    }

    /**
     * Legt den Wert der waterSupplyExists-Eigenschaft fest.
     * 
     */
    public void setWaterSupplyExists(boolean value) {
        this.waterSupplyExists = value;
    }

}
