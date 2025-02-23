//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 11:43:37 AM CEST 
//


package com.lp.server.schema.iso20022.camt052V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RemittanceLocation2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemittanceLocation2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RmtId" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}Max35Text" minOccurs="0"/>
 *         &lt;element name="RmtLctnMtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}RemittanceLocationMethod2Code" minOccurs="0"/>
 *         &lt;element name="RmtLctnElctrncAdr" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}Max2048Text" minOccurs="0"/>
 *         &lt;element name="RmtLctnPstlAdr" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}NameAndAddress10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemittanceLocation2", propOrder = {
    "rmtId",
    "rmtLctnMtd",
    "rmtLctnElctrncAdr",
    "rmtLctnPstlAdr"
})
public class CA052RemittanceLocation2 {

    @XmlElement(name = "RmtId")
    protected String rmtId;
    @XmlElement(name = "RmtLctnMtd")
    protected CA052RemittanceLocationMethod2Code rmtLctnMtd;
    @XmlElement(name = "RmtLctnElctrncAdr")
    protected String rmtLctnElctrncAdr;
    @XmlElement(name = "RmtLctnPstlAdr")
    protected CA052NameAndAddress10 rmtLctnPstlAdr;

    /**
     * Gets the value of the rmtId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmtId() {
        return rmtId;
    }

    /**
     * Sets the value of the rmtId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmtId(String value) {
        this.rmtId = value;
    }

    /**
     * Gets the value of the rmtLctnMtd property.
     * 
     * @return
     *     possible object is
     *     {@link CA052RemittanceLocationMethod2Code }
     *     
     */
    public CA052RemittanceLocationMethod2Code getRmtLctnMtd() {
        return rmtLctnMtd;
    }

    /**
     * Sets the value of the rmtLctnMtd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052RemittanceLocationMethod2Code }
     *     
     */
    public void setRmtLctnMtd(CA052RemittanceLocationMethod2Code value) {
        this.rmtLctnMtd = value;
    }

    /**
     * Gets the value of the rmtLctnElctrncAdr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmtLctnElctrncAdr() {
        return rmtLctnElctrncAdr;
    }

    /**
     * Sets the value of the rmtLctnElctrncAdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmtLctnElctrncAdr(String value) {
        this.rmtLctnElctrncAdr = value;
    }

    /**
     * Gets the value of the rmtLctnPstlAdr property.
     * 
     * @return
     *     possible object is
     *     {@link CA052NameAndAddress10 }
     *     
     */
    public CA052NameAndAddress10 getRmtLctnPstlAdr() {
        return rmtLctnPstlAdr;
    }

    /**
     * Sets the value of the rmtLctnPstlAdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052NameAndAddress10 }
     *     
     */
    public void setRmtLctnPstlAdr(CA052NameAndAddress10 value) {
        this.rmtLctnPstlAdr = value;
    }

}
