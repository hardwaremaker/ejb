
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ColloCodeRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ColloCodeRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NumberTypeID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OUCarrierThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColloCodeRow", propOrder = {
    "code",
    "numberTypeID",
    "ouCarrierThirdPartyID"
})
public class ColloCodeRow {

    @XmlElementRef(name = "Code", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> code;
    @XmlElement(name = "NumberTypeID")
    protected Integer numberTypeID;
    @XmlElementRef(name = "OUCarrierThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> ouCarrierThirdPartyID;

    /**
     * Ruft den Wert der code-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCode() {
        return code;
    }

    /**
     * Legt den Wert der code-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCode(JAXBElement<String> value) {
        this.code = value;
    }

    /**
     * Ruft den Wert der numberTypeID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberTypeID() {
        return numberTypeID;
    }

    /**
     * Legt den Wert der numberTypeID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberTypeID(Integer value) {
        this.numberTypeID = value;
    }

    /**
     * Ruft den Wert der ouCarrierThirdPartyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOUCarrierThirdPartyID() {
        return ouCarrierThirdPartyID;
    }

    /**
     * Legt den Wert der ouCarrierThirdPartyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOUCarrierThirdPartyID(JAXBElement<String> value) {
        this.ouCarrierThirdPartyID = value;
    }

}
