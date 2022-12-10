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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CardAggregated1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardAggregated1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddtlSvc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardPaymentServiceType2Code" minOccurs="0"/>
 *         &lt;element name="TxCtgy" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ExternalCardTransactionCategory1Code" minOccurs="0"/>
 *         &lt;element name="SaleRcncltnId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *         &lt;element name="SeqNbRg" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardSequenceNumberRange1" minOccurs="0"/>
 *         &lt;element name="TxDtRg" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DateOrDateTimePeriodChoice" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardAggregated1", propOrder = {
    "addtlSvc",
    "txCtgy",
    "saleRcncltnId",
    "seqNbRg",
    "txDtRg"
})
public class CA04CardAggregated1 {

    @XmlElement(name = "AddtlSvc")
    @XmlSchemaType(name = "string")
    protected CA04CardPaymentServiceType2Code addtlSvc;
    @XmlElement(name = "TxCtgy")
    protected String txCtgy;
    @XmlElement(name = "SaleRcncltnId")
    protected String saleRcncltnId;
    @XmlElement(name = "SeqNbRg")
    protected CA04CardSequenceNumberRange1 seqNbRg;
    @XmlElement(name = "TxDtRg")
    protected CA04DateOrDateTimePeriodChoice txDtRg;

    /**
     * Ruft den Wert der addtlSvc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardPaymentServiceType2Code }
     *     
     */
    public CA04CardPaymentServiceType2Code getAddtlSvc() {
        return addtlSvc;
    }

    /**
     * Legt den Wert der addtlSvc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardPaymentServiceType2Code }
     *     
     */
    public void setAddtlSvc(CA04CardPaymentServiceType2Code value) {
        this.addtlSvc = value;
    }

    /**
     * Ruft den Wert der txCtgy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxCtgy() {
        return txCtgy;
    }

    /**
     * Legt den Wert der txCtgy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxCtgy(String value) {
        this.txCtgy = value;
    }

    /**
     * Ruft den Wert der saleRcncltnId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleRcncltnId() {
        return saleRcncltnId;
    }

    /**
     * Legt den Wert der saleRcncltnId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleRcncltnId(String value) {
        this.saleRcncltnId = value;
    }

    /**
     * Ruft den Wert der seqNbRg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardSequenceNumberRange1 }
     *     
     */
    public CA04CardSequenceNumberRange1 getSeqNbRg() {
        return seqNbRg;
    }

    /**
     * Legt den Wert der seqNbRg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardSequenceNumberRange1 }
     *     
     */
    public void setSeqNbRg(CA04CardSequenceNumberRange1 value) {
        this.seqNbRg = value;
    }

    /**
     * Ruft den Wert der txDtRg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04DateOrDateTimePeriodChoice }
     *     
     */
    public CA04DateOrDateTimePeriodChoice getTxDtRg() {
        return txDtRg;
    }

    /**
     * Legt den Wert der txDtRg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04DateOrDateTimePeriodChoice }
     *     
     */
    public void setTxDtRg(CA04DateOrDateTimePeriodChoice value) {
        this.txDtRg = value;
    }

}
