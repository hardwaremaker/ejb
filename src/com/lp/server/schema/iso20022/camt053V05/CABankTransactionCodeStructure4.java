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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer BankTransactionCodeStructure4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BankTransactionCodeStructure4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Domn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BankTransactionCodeStructure5" minOccurs="0"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ProprietaryBankTransactionCodeStructure1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankTransactionCodeStructure4", propOrder = {
    "domn",
    "prtry"
})
public class CABankTransactionCodeStructure4 {

    @XmlElement(name = "Domn")
    protected CABankTransactionCodeStructure5 domn;
    @XmlElement(name = "Prtry")
    protected CAProprietaryBankTransactionCodeStructure1 prtry;

    /**
     * Ruft den Wert der domn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABankTransactionCodeStructure5 }
     *     
     */
    public CABankTransactionCodeStructure5 getDomn() {
        return domn;
    }

    /**
     * Legt den Wert der domn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABankTransactionCodeStructure5 }
     *     
     */
    public void setDomn(CABankTransactionCodeStructure5 value) {
        this.domn = value;
    }

    /**
     * Ruft den Wert der prtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAProprietaryBankTransactionCodeStructure1 }
     *     
     */
    public CAProprietaryBankTransactionCodeStructure1 getPrtry() {
        return prtry;
    }

    /**
     * Legt den Wert der prtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAProprietaryBankTransactionCodeStructure1 }
     *     
     */
    public void setPrtry(CAProprietaryBankTransactionCodeStructure1 value) {
        this.prtry = value;
    }

}
