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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer CardIndividualTransaction2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardIndividualTransaction2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ICCRltdData" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max1025Text" minOccurs="0"/>
 *         &lt;element name="PmtCntxt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PaymentContext3" minOccurs="0"/>
 *         &lt;element name="AddtlSvc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardPaymentServiceType2Code" minOccurs="0"/>
 *         &lt;element name="TxCtgy" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ExternalCardTransactionCategory1Code" minOccurs="0"/>
 *         &lt;element name="SaleRcncltnId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="SaleRefNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="RePresntmntRsn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ExternalRePresentmentReason1Code" minOccurs="0"/>
 *         &lt;element name="SeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="TxId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionIdentifier1" minOccurs="0"/>
 *         &lt;element name="Pdct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Product2" minOccurs="0"/>
 *         &lt;element name="VldtnDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ISODate" minOccurs="0"/>
 *         &lt;element name="VldtnSeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardIndividualTransaction2", propOrder = {
    "iccRltdData",
    "pmtCntxt",
    "addtlSvc",
    "txCtgy",
    "saleRcncltnId",
    "saleRefNb",
    "rePresntmntRsn",
    "seqNb",
    "txId",
    "pdct",
    "vldtnDt",
    "vldtnSeqNb"
})
public class CACardIndividualTransaction2 {

    @XmlElement(name = "ICCRltdData")
    protected String iccRltdData;
    @XmlElement(name = "PmtCntxt")
    protected CAPaymentContext3 pmtCntxt;
    @XmlElement(name = "AddtlSvc")
    @XmlSchemaType(name = "string")
    protected CACardPaymentServiceType2Code addtlSvc;
    @XmlElement(name = "TxCtgy")
    protected String txCtgy;
    @XmlElement(name = "SaleRcncltnId")
    protected String saleRcncltnId;
    @XmlElement(name = "SaleRefNb")
    protected String saleRefNb;
    @XmlElement(name = "RePresntmntRsn")
    protected String rePresntmntRsn;
    @XmlElement(name = "SeqNb")
    protected String seqNb;
    @XmlElement(name = "TxId")
    protected CATransactionIdentifier1 txId;
    @XmlElement(name = "Pdct")
    protected CAProduct2 pdct;
    @XmlElement(name = "VldtnDt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar vldtnDt;
    @XmlElement(name = "VldtnSeqNb")
    protected String vldtnSeqNb;

    /**
     * Ruft den Wert der iccRltdData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICCRltdData() {
        return iccRltdData;
    }

    /**
     * Legt den Wert der iccRltdData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICCRltdData(String value) {
        this.iccRltdData = value;
    }

    /**
     * Ruft den Wert der pmtCntxt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPaymentContext3 }
     *     
     */
    public CAPaymentContext3 getPmtCntxt() {
        return pmtCntxt;
    }

    /**
     * Legt den Wert der pmtCntxt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPaymentContext3 }
     *     
     */
    public void setPmtCntxt(CAPaymentContext3 value) {
        this.pmtCntxt = value;
    }

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
     * Ruft den Wert der saleRefNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaleRefNb() {
        return saleRefNb;
    }

    /**
     * Legt den Wert der saleRefNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaleRefNb(String value) {
        this.saleRefNb = value;
    }

    /**
     * Ruft den Wert der rePresntmntRsn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRePresntmntRsn() {
        return rePresntmntRsn;
    }

    /**
     * Legt den Wert der rePresntmntRsn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRePresntmntRsn(String value) {
        this.rePresntmntRsn = value;
    }

    /**
     * Ruft den Wert der seqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeqNb() {
        return seqNb;
    }

    /**
     * Legt den Wert der seqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeqNb(String value) {
        this.seqNb = value;
    }

    /**
     * Ruft den Wert der txId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionIdentifier1 }
     *     
     */
    public CATransactionIdentifier1 getTxId() {
        return txId;
    }

    /**
     * Legt den Wert der txId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionIdentifier1 }
     *     
     */
    public void setTxId(CATransactionIdentifier1 value) {
        this.txId = value;
    }

    /**
     * Ruft den Wert der pdct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAProduct2 }
     *     
     */
    public CAProduct2 getPdct() {
        return pdct;
    }

    /**
     * Legt den Wert der pdct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAProduct2 }
     *     
     */
    public void setPdct(CAProduct2 value) {
        this.pdct = value;
    }

    /**
     * Ruft den Wert der vldtnDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVldtnDt() {
        return vldtnDt;
    }

    /**
     * Legt den Wert der vldtnDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVldtnDt(XMLGregorianCalendar value) {
        this.vldtnDt = value;
    }

    /**
     * Ruft den Wert der vldtnSeqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVldtnSeqNb() {
        return vldtnSeqNb;
    }

    /**
     * Legt den Wert der vldtnSeqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVldtnSeqNb(String value) {
        this.vldtnSeqNb = value;
    }

}
