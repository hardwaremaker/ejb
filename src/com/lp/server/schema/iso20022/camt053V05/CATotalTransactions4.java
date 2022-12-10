//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer TotalTransactions4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TotalTransactions4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TtlNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}NumberAndSumOfTransactions4" minOccurs="0"/>
 *         &lt;element name="TtlCdtNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}NumberAndSumOfTransactions1" minOccurs="0"/>
 *         &lt;element name="TtlDbtNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}NumberAndSumOfTransactions1" minOccurs="0"/>
 *         &lt;element name="TtlNtriesPerBkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TotalsPerBankTransactionCode3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TotalTransactions4", propOrder = {
    "ttlNtries",
    "ttlCdtNtries",
    "ttlDbtNtries",
    "ttlNtriesPerBkTxCd"
})
public class CATotalTransactions4 {

    @XmlElement(name = "TtlNtries")
    protected CANumberAndSumOfTransactions4 ttlNtries;
    @XmlElement(name = "TtlCdtNtries")
    protected CANumberAndSumOfTransactions1 ttlCdtNtries;
    @XmlElement(name = "TtlDbtNtries")
    protected CANumberAndSumOfTransactions1 ttlDbtNtries;
    @XmlElement(name = "TtlNtriesPerBkTxCd")
    protected List<CATotalsPerBankTransactionCode3> ttlNtriesPerBkTxCd;

    /**
     * Ruft den Wert der ttlNtries-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CANumberAndSumOfTransactions4 }
     *     
     */
    public CANumberAndSumOfTransactions4 getTtlNtries() {
        return ttlNtries;
    }

    /**
     * Legt den Wert der ttlNtries-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CANumberAndSumOfTransactions4 }
     *     
     */
    public void setTtlNtries(CANumberAndSumOfTransactions4 value) {
        this.ttlNtries = value;
    }

    /**
     * Ruft den Wert der ttlCdtNtries-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CANumberAndSumOfTransactions1 }
     *     
     */
    public CANumberAndSumOfTransactions1 getTtlCdtNtries() {
        return ttlCdtNtries;
    }

    /**
     * Legt den Wert der ttlCdtNtries-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CANumberAndSumOfTransactions1 }
     *     
     */
    public void setTtlCdtNtries(CANumberAndSumOfTransactions1 value) {
        this.ttlCdtNtries = value;
    }

    /**
     * Ruft den Wert der ttlDbtNtries-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CANumberAndSumOfTransactions1 }
     *     
     */
    public CANumberAndSumOfTransactions1 getTtlDbtNtries() {
        return ttlDbtNtries;
    }

    /**
     * Legt den Wert der ttlDbtNtries-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CANumberAndSumOfTransactions1 }
     *     
     */
    public void setTtlDbtNtries(CANumberAndSumOfTransactions1 value) {
        this.ttlDbtNtries = value;
    }

    /**
     * Gets the value of the ttlNtriesPerBkTxCd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ttlNtriesPerBkTxCd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTtlNtriesPerBkTxCd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CATotalsPerBankTransactionCode3 }
     * 
     * 
     */
    public List<CATotalsPerBankTransactionCode3> getTtlNtriesPerBkTxCd() {
        if (ttlNtriesPerBkTxCd == null) {
            ttlNtriesPerBkTxCd = new ArrayList<CATotalsPerBankTransactionCode3>();
        }
        return this.ttlNtriesPerBkTxCd;
    }

}
