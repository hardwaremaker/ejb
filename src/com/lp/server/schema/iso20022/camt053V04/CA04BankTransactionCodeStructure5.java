//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r BankTransactionCodeStructure5 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BankTransactionCodeStructure5">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ExternalBankTransactionDomain1Code"/>
 *         &lt;element name="Fmly" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BankTransactionCodeStructure6"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankTransactionCodeStructure5", propOrder = {
    "cd",
    "fmly"
})
public class CA04BankTransactionCodeStructure5 {

    @XmlElement(name = "Cd", required = true)
    protected String cd;
    @XmlElement(name = "Fmly", required = true)
    protected CA04BankTransactionCodeStructure6 fmly;

    /**
     * Ruft den Wert der cd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCd() {
        return cd;
    }

    /**
     * Legt den Wert der cd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCd(String value) {
        this.cd = value;
    }

    /**
     * Ruft den Wert der fmly-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BankTransactionCodeStructure6 }
     *     
     */
    public CA04BankTransactionCodeStructure6 getFmly() {
        return fmly;
    }

    /**
     * Legt den Wert der fmly-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BankTransactionCodeStructure6 }
     *     
     */
    public void setFmly(CA04BankTransactionCodeStructure6 value) {
        this.fmly = value;
    }

}
