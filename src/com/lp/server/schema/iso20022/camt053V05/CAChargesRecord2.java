//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ChargesRecord2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ChargesRecord2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CreditDebitCode" minOccurs="0"/>
 *         &lt;element name="ChrgInclInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ChargeIncludedIndicator" minOccurs="0"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ChargeType3Choice" minOccurs="0"/>
 *         &lt;element name="Rate" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PercentageRate" minOccurs="0"/>
 *         &lt;element name="Br" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="Agt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TaxCharges2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChargesRecord2", propOrder = {
    "amt",
    "cdtDbtInd",
    "chrgInclInd",
    "tp",
    "rate",
    "br",
    "agt",
    "tax"
})
public class CAChargesRecord2 {

    @XmlElement(name = "Amt", required = true)
    protected CAActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd")
    @XmlSchemaType(name = "string")
    protected CACreditDebitCode cdtDbtInd;
    @XmlElement(name = "ChrgInclInd")
    protected Boolean chrgInclInd;
    @XmlElement(name = "Tp")
    protected CAChargeType3Choice tp;
    @XmlElement(name = "Rate")
    protected BigDecimal rate;
    @XmlElement(name = "Br")
    @XmlSchemaType(name = "string")
    protected CAChargeBearerType1Code br;
    @XmlElement(name = "Agt")
    protected CABranchAndFinancialInstitutionIdentification5 agt;
    @XmlElement(name = "Tax")
    protected CATaxCharges2 tax;

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
     * Ruft den Wert der chrgInclInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isChrgInclInd() {
        return chrgInclInd;
    }

    /**
     * Legt den Wert der chrgInclInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChrgInclInd(Boolean value) {
        this.chrgInclInd = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAChargeType3Choice }
     *     
     */
    public CAChargeType3Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAChargeType3Choice }
     *     
     */
    public void setTp(CAChargeType3Choice value) {
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
     * Ruft den Wert der agt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getAgt() {
        return agt;
    }

    /**
     * Legt den Wert der agt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.agt = value;
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
