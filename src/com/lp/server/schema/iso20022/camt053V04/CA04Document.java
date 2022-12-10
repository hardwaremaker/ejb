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
 * <p>Java-Klasse f�r Document complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BkToCstmrStmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BankToCustomerStatementV04"/>
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
public class CA04Document {

    @XmlElement(name = "BkToCstmrStmt", required = true)
    protected CA04BankToCustomerStatementV04 bkToCstmrStmt;

    /**
     * Ruft den Wert der bkToCstmrStmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BankToCustomerStatementV04 }
     *     
     */
    public CA04BankToCustomerStatementV04 getBkToCstmrStmt() {
        return bkToCstmrStmt;
    }

    /**
     * Legt den Wert der bkToCstmrStmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BankToCustomerStatementV04 }
     *     
     */
    public void setBkToCstmrStmt(CA04BankToCustomerStatementV04 value) {
        this.bkToCstmrStmt = value;
    }

}
