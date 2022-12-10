//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="CstmrCdtTrfInitn" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CustomerCreditTransferInitiationV06"/>
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
    "cstmrCdtTrfInitn"
})
public class PADocument {

    @XmlElement(name = "CstmrCdtTrfInitn", required = true)
    protected PACustomerCreditTransferInitiationV06 cstmrCdtTrfInitn;

    /**
     * Ruft den Wert der cstmrCdtTrfInitn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACustomerCreditTransferInitiationV06 }
     *     
     */
    public PACustomerCreditTransferInitiationV06 getCstmrCdtTrfInitn() {
        return cstmrCdtTrfInitn;
    }

    /**
     * Legt den Wert der cstmrCdtTrfInitn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACustomerCreditTransferInitiationV06 }
     *     
     */
    public void setCstmrCdtTrfInitn(PACustomerCreditTransferInitiationV06 value) {
        this.cstmrCdtTrfInitn = value;
    }

}
