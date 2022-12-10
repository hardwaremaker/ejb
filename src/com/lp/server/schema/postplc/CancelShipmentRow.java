
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CancelShipmentRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CancelShipmentRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClientID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ColloCodeList" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrgUnitGuid" type="{http://schemas.microsoft.com/2003/10/Serialization/}guid" minOccurs="0"/>
 *         &lt;element name="OrgUnitID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelShipmentRow", propOrder = {
    "clientID",
    "colloCodeList",
    "number",
    "orgUnitGuid",
    "orgUnitID"
})
public class CancelShipmentRow {

    @XmlElement(name = "ClientID")
    protected Integer clientID;
    @XmlElementRef(name = "ColloCodeList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> colloCodeList;
    @XmlElementRef(name = "Number", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> number;
    @XmlElement(name = "OrgUnitGuid")
    protected String orgUnitGuid;
    @XmlElement(name = "OrgUnitID")
    protected Integer orgUnitID;

    /**
     * Ruft den Wert der clientID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getClientID() {
        return clientID;
    }

    /**
     * Legt den Wert der clientID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setClientID(Integer value) {
        this.clientID = value;
    }

    /**
     * Ruft den Wert der colloCodeList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getColloCodeList() {
        return colloCodeList;
    }

    /**
     * Legt den Wert der colloCodeList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setColloCodeList(JAXBElement<ArrayOfstring> value) {
        this.colloCodeList = value;
    }

    /**
     * Ruft den Wert der number-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumber() {
        return number;
    }

    /**
     * Legt den Wert der number-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumber(JAXBElement<String> value) {
        this.number = value;
    }

    /**
     * Ruft den Wert der orgUnitGuid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgUnitGuid() {
        return orgUnitGuid;
    }

    /**
     * Legt den Wert der orgUnitGuid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgUnitGuid(String value) {
        this.orgUnitGuid = value;
    }

    /**
     * Ruft den Wert der orgUnitID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrgUnitID() {
        return orgUnitID;
    }

    /**
     * Legt den Wert der orgUnitID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrgUnitID(Integer value) {
        this.orgUnitID = value;
    }

}
