//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ChargesInformation6 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ChargesInformation6">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TtlChrgsAndTaxAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CreditDebitCode" minOccurs="0"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ChargeType2Choice" minOccurs="0"/>
 *         &lt;element name="Rate" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}PercentageRate" minOccurs="0"/>
 *         &lt;element name="Br" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="Pty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TaxCharges2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChargesInformation6", propOrder = {
    "ttlChrgsAndTaxAmt",
    "amt",
    "cdtDbtInd",
    "tp",
    "rate",
    "br",
    "pty",
    "tax"
})
public class CAChargesInformation6 {

    @XmlElement(name = "TtlChrgsAndTaxAmt")
    protected CAActiveOrHistoricCurrencyAndAmount ttlChrgsAndTaxAmt;
    @XmlElement(name = "Amt", required = true)
    protected CAActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd")
    @XmlSchemaType(name = "string")
    protected CACreditDebitCode cdtDbtInd;
    @XmlElement(name = "Tp")
    protected CAChargeType2Choice tp;
    @XmlElement(name = "Rate")
    protected BigDecimal rate;
    @XmlElement(name = "Br")
    @XmlSchemaType(name = "string")
    protected CAChargeBearerType1Code br;
    @XmlElement(name = "Pty")
    protected CABranchAndFinancialInstitutionIdentification4 pty;
    @XmlElement(name = "Tax")
    protected CATaxCharges2 tax;

    /**
     * Ruft den Wert der ttlChrgsAndTaxAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CAActiveOrHistoricCurrencyAndAmount getTtlChrgsAndTaxAmt() {
        return ttlChrgsAndTaxAmt;
    }

    /**
     * Legt den Wert der ttlChrgsAndTaxAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setTtlChrgsAndTaxAmt(CAActiveOrHistoricCurrencyAndAmount value) {
        this.ttlChrgsAndTaxAmt = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CAActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(CAActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der cdtDbtInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACreditDebitCode }
     *     
     */
    public CACreditDebitCode getCdtDbtInd() {
        return cdtDbtInd;
    }

    /**
     * Legt den Wert der cdtDbtInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACreditDebitCode }
     *     
     */
    public void setCdtDbtInd(CACreditDebitCode value) {
        this.cdtDbtInd = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAChargeType2Choice }
     *     
     */
    public CAChargeType2Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAChargeType2Choice }
     *     
     */
    public void setTp(CAChargeType2Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der rate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Legt den Wert der rate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRate(BigDecimal value) {
        this.rate = value;
    }

    /**
     * Ruft den Wert der br-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAChargeBearerType1Code }
     *     
     */
    public CAChargeBearerType1Code getBr() {
        return br;
    }

    /**
     * Legt den Wert der br-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAChargeBearerType1Code }
     *     
     */
    public void setBr(CAChargeBearerType1Code value) {
        this.br = value;
    }

    /**
     * Ruft den Wert der pty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getPty() {
        return pty;
    }

    /**
     * Legt den Wert der pty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setPty(CABranchAndFinancialInstitutionIdentification4 value) {
        this.pty = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATaxCharges2 }
     *     
     */
    public CATaxCharges2 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATaxCharges2 }
     *     
     */
    public void setTax(CATaxCharges2 value) {
        this.tax = value;
    }

}
