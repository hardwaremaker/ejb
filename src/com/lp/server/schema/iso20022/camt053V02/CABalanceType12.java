//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer BalanceType12 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BalanceType12">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CdOrPrtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BalanceType5Choice"/>
 *         &lt;element name="SubTp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BalanceSubType1Choice" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BalanceType12", propOrder = {
    "cdOrPrtry",
    "subTp"
})
public class CABalanceType12 {

    @XmlElement(name = "CdOrPrtry", required = true)
    protected CABalanceType5Choice cdOrPrtry;
    @XmlElement(name = "SubTp")
    protected CABalanceSubType1Choice subTp;

    /**
     * Ruft den Wert der cdOrPrtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABalanceType5Choice }
     *     
     */
    public CABalanceType5Choice getCdOrPrtry() {
        return cdOrPrtry;
    }

    /**
     * Legt den Wert der cdOrPrtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABalanceType5Choice }
     *     
     */
    public void setCdOrPrtry(CABalanceType5Choice value) {
        this.cdOrPrtry = value;
    }

    /**
     * Ruft den Wert der subTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABalanceSubType1Choice }
     *     
     */
    public CABalanceSubType1Choice getSubTp() {
        return subTp;
    }

    /**
     * Legt den Wert der subTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABalanceSubType1Choice }
     *     
     */
    public void setSubTp(CABalanceSubType1Choice value) {
        this.subTp = value;
    }

}
