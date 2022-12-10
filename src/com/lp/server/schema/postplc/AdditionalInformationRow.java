
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AdditionalInformationRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalInformationRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Value4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalInformationRow", propOrder = {
    "name",
    "thirdPartyID",
    "value1",
    "value2",
    "value3",
    "value4"
})
public class AdditionalInformationRow {

    @XmlElementRef(name = "Name", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "ThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> thirdPartyID;
    @XmlElementRef(name = "Value1", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> value1;
    @XmlElementRef(name = "Value2", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> value2;
    @XmlElementRef(name = "Value3", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> value3;
    @XmlElementRef(name = "Value4", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> value4;

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

    /**
     * Ruft den Wert der value1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValue1() {
        return value1;
    }

    /**
     * Legt den Wert der value1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValue1(JAXBElement<String> value) {
        this.value1 = value;
    }

    /**
     * Ruft den Wert der value2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValue2() {
        return value2;
    }

    /**
     * Legt den Wert der value2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValue2(JAXBElement<String> value) {
        this.value2 = value;
    }

    /**
     * Ruft den Wert der value3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValue3() {
        return value3;
    }

    /**
     * Legt den Wert der value3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValue3(JAXBElement<String> value) {
        this.value3 = value;
    }

    /**
     * Ruft den Wert der value4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValue4() {
        return value4;
    }

    /**
     * Legt den Wert der value4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValue4(JAXBElement<String> value) {
        this.value4 = value;
    }

}
