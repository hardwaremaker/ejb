
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CarrierServiceRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CarrierServiceRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Contract" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="FeatureList" type="{http://post.ondot.at}ArrayOfAdditionalInformationResult" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarrierServiceRow", propOrder = {
    "contract",
    "featureList",
    "name",
    "orderID",
    "thirdPartyID"
})
public class CarrierServiceRow {

    @XmlElement(name = "Contract")
    protected Boolean contract;
    @XmlElementRef(name = "FeatureList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfAdditionalInformationResult> featureList;
    @XmlElementRef(name = "Name", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "OrderID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> orderID;
    @XmlElementRef(name = "ThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> thirdPartyID;

    /**
     * Ruft den Wert der contract-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isContract() {
        return contract;
    }

    /**
     * Legt den Wert der contract-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setContract(Boolean value) {
        this.contract = value;
    }

    /**
     * Ruft den Wert der featureList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationResult }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAdditionalInformationResult> getFeatureList() {
        return featureList;
    }

    /**
     * Legt den Wert der featureList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationResult }{@code >}
     *     
     */
    public void setFeatureList(JAXBElement<ArrayOfAdditionalInformationResult> value) {
        this.featureList = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der orderID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getOrderID() {
        return orderID;
    }

    /**
     * Legt den Wert der orderID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setOrderID(JAXBElement<Integer> value) {
        this.orderID = value;
    }

    /**
     * Ruft den Wert der thirdPartyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getThirdPartyID() {
        return thirdPartyID;
    }

    /**
     * Legt den Wert der thirdPartyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setThirdPartyID(JAXBElement<String> value) {
        this.thirdPartyID = value;
    }

}
