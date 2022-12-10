//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Document complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CstmrDrctDbtInitn" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CustomerDirectDebitInitiationV02"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
    "cstmrDrctDbtInitn"
})
@XmlRootElement(name="Document")
public class PADocument {

    @XmlElement(name = "CstmrDrctDbtInitn", required = true)
    protected PACustomerDirectDebitInitiationV02 cstmrDrctDbtInitn;

    /**
     * Ruft den Wert der cstmrDrctDbtInitn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACustomerDirectDebitInitiationV02 }
     *     
     */
    public PACustomerDirectDebitInitiationV02 getCstmrDrctDbtInitn() {
        return cstmrDrctDbtInitn;
    }

    /**
     * Legt den Wert der cstmrDrctDbtInitn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACustomerDirectDebitInitiationV02 }
     *     
     */
    public void setCstmrDrctDbtInitn(PACustomerDirectDebitInitiationV02 value) {
        this.cstmrDrctDbtInitn = value;
    }

}
