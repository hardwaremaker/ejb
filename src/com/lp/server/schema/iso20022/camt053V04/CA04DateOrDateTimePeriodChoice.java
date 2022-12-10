//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DateOrDateTimePeriodChoice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DateOrDateTimePeriodChoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DatePeriodDetails"/>
 *         &lt;element name="DtTm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DateTimePeriodDetails"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateOrDateTimePeriodChoice", propOrder = {
    "dt",
    "dtTm"
})
public class CA04DateOrDateTimePeriodChoice {

    @XmlElement(name = "Dt")
    protected CA04DatePeriodDetails dt;
    @XmlElement(name = "DtTm")
    protected CA04DateTimePeriodDetails dtTm;

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04DatePeriodDetails }
     *     
     */
    public CA04DatePeriodDetails getDt() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04DatePeriodDetails }
     *     
     */
    public void setDt(CA04DatePeriodDetails value) {
        this.dt = value;
    }

    /**
     * Ruft den Wert der dtTm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04DateTimePeriodDetails }
     *     
     */
    public CA04DateTimePeriodDetails getDtTm() {
        return dtTm;
    }

    /**
     * Legt den Wert der dtTm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04DateTimePeriodDetails }
     *     
     */
    public void setDtTm(CA04DateTimePeriodDetails value) {
        this.dtTm = value;
    }

}
