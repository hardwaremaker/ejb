//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CardholderAuthentication2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardholderAuthentication2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthntcnMtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AuthenticationMethod1Code"/>
 *         &lt;element name="AuthntcnNtty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AuthenticationEntity1Code"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardholderAuthentication2", propOrder = {
    "authntcnMtd",
    "authntcnNtty"
})
public class CACardholderAuthentication2 {

    @XmlElement(name = "AuthntcnMtd", required = true)
    @XmlSchemaType(name = "string")
    protected CAAuthenticationMethod1Code authntcnMtd;
    @XmlElement(name = "AuthntcnNtty", required = true)
    @XmlSchemaType(name = "string")
    protected CAAuthenticationEntity1Code authntcnNtty;

    /**
     * Ruft den Wert der authntcnMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAuthenticationMethod1Code }
     *     
     */
    public CAAuthenticationMethod1Code getAuthntcnMtd() {
        return authntcnMtd;
    }

    /**
     * Legt den Wert der authntcnMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAuthenticationMethod1Code }
     *     
     */
    public void setAuthntcnMtd(CAAuthenticationMethod1Code value) {
        this.authntcnMtd = value;
    }

    /**
     * Ruft den Wert der authntcnNtty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAuthenticationEntity1Code }
     *     
     */
    public CAAuthenticationEntity1Code getAuthntcnNtty() {
        return authntcnNtty;
    }

    /**
     * Legt den Wert der authntcnNtty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAuthenticationEntity1Code }
     *     
     */
    public void setAuthntcnNtty(CAAuthenticationEntity1Code value) {
        this.authntcnNtty = value;
    }

}
