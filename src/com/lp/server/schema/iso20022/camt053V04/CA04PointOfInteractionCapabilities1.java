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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PointOfInteractionCapabilities1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PointOfInteractionCapabilities1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardRdngCpblties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardDataReading1Code" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CrdhldrVrfctnCpblties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardholderVerificationCapability1Code" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="OnLineCpblties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}OnLineCapability1Code" minOccurs="0"/>
 *         &lt;element name="DispCpblties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DisplayCapabilities1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PrtLineWidth" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max3NumericText" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointOfInteractionCapabilities1", propOrder = {
    "cardRdngCpblties",
    "crdhldrVrfctnCpblties",
    "onLineCpblties",
    "dispCpblties",
    "prtLineWidth"
})
public class CA04PointOfInteractionCapabilities1 {

    @XmlElement(name = "CardRdngCpblties")
    @XmlSchemaType(name = "string")
    protected List<CA04CardDataReading1Code> cardRdngCpblties;
    @XmlElement(name = "CrdhldrVrfctnCpblties")
    @XmlSchemaType(name = "string")
    protected List<CA04CardholderVerificationCapability1Code> crdhldrVrfctnCpblties;
    @XmlElement(name = "OnLineCpblties")
    @XmlSchemaType(name = "string")
    protected CA04OnLineCapability1Code onLineCpblties;
    @XmlElement(name = "DispCpblties")
    protected List<CA04DisplayCapabilities1> dispCpblties;
    @XmlElement(name = "PrtLineWidth")
    protected String prtLineWidth;

    /**
     * Gets the value of the cardRdngCpblties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cardRdngCpblties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCardRdngCpblties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04CardDataReading1Code }
     * 
     * 
     */
    public List<CA04CardDataReading1Code> getCardRdngCpblties() {
        if (cardRdngCpblties == null) {
            cardRdngCpblties = new ArrayList<CA04CardDataReading1Code>();
        }
        return this.cardRdngCpblties;
    }

    /**
     * Gets the value of the crdhldrVrfctnCpblties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the crdhldrVrfctnCpblties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCrdhldrVrfctnCpblties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04CardholderVerificationCapability1Code }
     * 
     * 
     */
    public List<CA04CardholderVerificationCapability1Code> getCrdhldrVrfctnCpblties() {
        if (crdhldrVrfctnCpblties == null) {
            crdhldrVrfctnCpblties = new ArrayList<CA04CardholderVerificationCapability1Code>();
        }
        return this.crdhldrVrfctnCpblties;
    }

    /**
     * Ruft den Wert der onLineCpblties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04OnLineCapability1Code }
     *     
     */
    public CA04OnLineCapability1Code getOnLineCpblties() {
        return onLineCpblties;
    }

    /**
     * Legt den Wert der onLineCpblties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04OnLineCapability1Code }
     *     
     */
    public void setOnLineCpblties(CA04OnLineCapability1Code value) {
        this.onLineCpblties = value;
    }

    /**
     * Gets the value of the dispCpblties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dispCpblties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDispCpblties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04DisplayCapabilities1 }
     * 
     * 
     */
    public List<CA04DisplayCapabilities1> getDispCpblties() {
        if (dispCpblties == null) {
            dispCpblties = new ArrayList<CA04DisplayCapabilities1>();
        }
        return this.dispCpblties;
    }

    /**
     * Ruft den Wert der prtLineWidth-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrtLineWidth() {
        return prtLineWidth;
    }

    /**
     * Legt den Wert der prtLineWidth-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrtLineWidth(String value) {
        this.prtLineWidth = value;
    }

}
