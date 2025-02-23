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
 * <p>Java-Klasse fuer PaymentContext3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentContext3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardPres" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="CrdhldrPres" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="OnLineCntxt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="AttndncCntxt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AttendanceContext1Code" minOccurs="0"/>
 *         &lt;element name="TxEnvt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionEnvironment1Code" minOccurs="0"/>
 *         &lt;element name="TxChanl" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionChannel1Code" minOccurs="0"/>
 *         &lt;element name="AttndntMsgCpbl" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="AttndntLang" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ISO2ALanguageCode" minOccurs="0"/>
 *         &lt;element name="CardDataNtryMd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardDataReading1Code"/>
 *         &lt;element name="FllbckInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="AuthntcnMtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardholderAuthentication2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentContext3", propOrder = {
    "cardPres",
    "crdhldrPres",
    "onLineCntxt",
    "attndncCntxt",
    "txEnvt",
    "txChanl",
    "attndntMsgCpbl",
    "attndntLang",
    "cardDataNtryMd",
    "fllbckInd",
    "authntcnMtd"
})
public class CAPaymentContext3 {

    @XmlElement(name = "CardPres")
    protected Boolean cardPres;
    @XmlElement(name = "CrdhldrPres")
    protected Boolean crdhldrPres;
    @XmlElement(name = "OnLineCntxt")
    protected Boolean onLineCntxt;
    @XmlElement(name = "AttndncCntxt")
    @XmlSchemaType(name = "string")
    protected CAAttendanceContext1Code attndncCntxt;
    @XmlElement(name = "TxEnvt")
    @XmlSchemaType(name = "string")
    protected CATransactionEnvironment1Code txEnvt;
    @XmlElement(name = "TxChanl")
    @XmlSchemaType(name = "string")
    protected CATransactionChannel1Code txChanl;
    @XmlElement(name = "AttndntMsgCpbl")
    protected Boolean attndntMsgCpbl;
    @XmlElement(name = "AttndntLang")
    protected String attndntLang;
    @XmlElement(name = "CardDataNtryMd", required = true)
    @XmlSchemaType(name = "string")
    protected CACardDataReading1Code cardDataNtryMd;
    @XmlElement(name = "FllbckInd")
    protected Boolean fllbckInd;
    @XmlElement(name = "AuthntcnMtd")
    protected CACardholderAuthentication2 authntcnMtd;

    /**
     * Ruft den Wert der cardPres-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCardPres() {
        return cardPres;
    }

    /**
     * Legt den Wert der cardPres-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCardPres(Boolean value) {
        this.cardPres = value;
    }

    /**
     * Ruft den Wert der crdhldrPres-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCrdhldrPres() {
        return crdhldrPres;
    }

    /**
     * Legt den Wert der crdhldrPres-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCrdhldrPres(Boolean value) {
        this.crdhldrPres = value;
    }

    /**
     * Ruft den Wert der onLineCntxt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOnLineCntxt() {
        return onLineCntxt;
    }

    /**
     * Legt den Wert der onLineCntxt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOnLineCntxt(Boolean value) {
        this.onLineCntxt = value;
    }

    /**
     * Ruft den Wert der attndncCntxt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAttendanceContext1Code }
     *     
     */
    public CAAttendanceContext1Code getAttndncCntxt() {
        return attndncCntxt;
    }

    /**
     * Legt den Wert der attndncCntxt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAttendanceContext1Code }
     *     
     */
    public void setAttndncCntxt(CAAttendanceContext1Code value) {
        this.attndncCntxt = value;
    }

    /**
     * Ruft den Wert der txEnvt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionEnvironment1Code }
     *     
     */
    public CATransactionEnvironment1Code getTxEnvt() {
        return txEnvt;
    }

    /**
     * Legt den Wert der txEnvt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionEnvironment1Code }
     *     
     */
    public void setTxEnvt(CATransactionEnvironment1Code value) {
        this.txEnvt = value;
    }

    /**
     * Ruft den Wert der txChanl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionChannel1Code }
     *     
     */
    public CATransactionChannel1Code getTxChanl() {
        return txChanl;
    }

    /**
     * Legt den Wert der txChanl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionChannel1Code }
     *     
     */
    public void setTxChanl(CATransactionChannel1Code value) {
        this.txChanl = value;
    }

    /**
     * Ruft den Wert der attndntMsgCpbl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAttndntMsgCpbl() {
        return attndntMsgCpbl;
    }

    /**
     * Legt den Wert der attndntMsgCpbl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAttndntMsgCpbl(Boolean value) {
        this.attndntMsgCpbl = value;
    }

    /**
     * Ruft den Wert der attndntLang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttndntLang() {
        return attndntLang;
    }

    /**
     * Legt den Wert der attndntLang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttndntLang(String value) {
        this.attndntLang = value;
    }

    /**
     * Ruft den Wert der cardDataNtryMd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardDataReading1Code }
     *     
     */
    public CACardDataReading1Code getCardDataNtryMd() {
        return cardDataNtryMd;
    }

    /**
     * Legt den Wert der cardDataNtryMd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardDataReading1Code }
     *     
     */
    public void setCardDataNtryMd(CACardDataReading1Code value) {
        this.cardDataNtryMd = value;
    }

    /**
     * Ruft den Wert der fllbckInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFllbckInd() {
        return fllbckInd;
    }

    /**
     * Legt den Wert der fllbckInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFllbckInd(Boolean value) {
        this.fllbckInd = value;
    }

    /**
     * Ruft den Wert der authntcnMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardholderAuthentication2 }
     *     
     */
    public CACardholderAuthentication2 getAuthntcnMtd() {
        return authntcnMtd;
    }

    /**
     * Legt den Wert der authntcnMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardholderAuthentication2 }
     *     
     */
    public void setAuthntcnMtd(CACardholderAuthentication2 value) {
        this.authntcnMtd = value;
    }

}
