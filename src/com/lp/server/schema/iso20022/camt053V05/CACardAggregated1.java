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
 * <p>Java-Klasse fuer CardAggregated1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardAggregated1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddtlSvc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardPaymentServiceType2Code" minOccurs="0"/>
 *         &lt;element name="TxCtgy" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ExternalCardTransactionCategory1Code" minOccurs="0"/>
 *         &lt;element name="SaleRcncltnId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="SeqNbRg" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardSequenceNumberRange1" minOccurs="0"/>
 *         &lt;element name="TxDtRg" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}DateOrDateTimePeriodChoice" minOccurs="0"/>
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
public class CACardAggregated1 {

    @XmlElement(name = "AddtlSvc")
    @XmlSchemaType(name = "string")
    protected CACardPaymentServiceType2Code addtlSvc;
    @XmlElement(name = "TxCtgy")
    protected String txCtgy;
    @XmlElement(name = "SaleRcncltnId")
    protected String saleRcncltnId;
    @XmlElement(name = "SeqNbRg")
    protected CACardSequenceNumberRange1 seqNbRg;
    @XmlElement(name = "TxDtRg")
    protected CADateOrDateTimePeriodChoice txDtRg;

    /**
     * Ruft den Wert der addtlSvc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardPaymentServiceType2Code }
     *     
     */
    public CACardPaymentServiceType2Code getAddtlSvc() {
        return addtlSvc;
    }

    /**
     * Legt den Wert der addtlSvc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardPaymentServiceType2Code }
     *     
     */
    public void setAddtlSvc(CACardPaymentServiceType2Code value) {
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
     *     {@link CACardSequenceNumberRange1 }
     *     
     */
    public CACardSequenceNumberRange1 getSeqNbRg() {
        return seqNbRg;
    }

    /**
     * Legt den Wert der seqNbRg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardSequenceNumberRange1 }
     *     
     */
    public void setSeqNbRg(CACardSequenceNumberRange1 value) {
        this.seqNbRg = value;
    }

    /**
     * Ruft den Wert der txDtRg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CADateOrDateTimePeriodChoice }
     *     
     */
    public CADateOrDateTimePeriodChoice getTxDtRg() {
        return txDtRg;
    }

    /**
     * Legt den Wert der txDtRg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CADateOrDateTimePeriodChoice }
     *     
     */
    public void setTxDtRg(CADateOrDateTimePeriodChoice value) {
        this.txDtRg = value;
    }

}
