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
 * <p>Java-Klasse fuer Document complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BkToCstmrStmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BankToCustomerStatementV02"/>
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
    "bkToCstmrStmt"
})
public class CADocument {

    @XmlElement(name = "BkToCstmrStmt", required = true)
    protected CABankToCustomerStatementV02 bkToCstmrStmt;

    /**
     * Ruft den Wert der bkToCstmrStmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABankToCustomerStatementV02 }
     *     
     */
    public CABankToCustomerStatementV02 getBkToCstmrStmt() {
        return bkToCstmrStmt;
    }

    /**
     * Legt den Wert der bkToCstmrStmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABankToCustomerStatementV02 }
     *     
     */
    public void setBkToCstmrStmt(CABankToCustomerStatementV02 value) {
        this.bkToCstmrStmt = value;
    }

}
