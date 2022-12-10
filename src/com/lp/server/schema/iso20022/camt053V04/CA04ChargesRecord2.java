//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ChargesRecord2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ChargesRecord2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CreditDebitCode" minOccurs="0"/>
 *         &lt;element name="ChrgInclInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ChargeIncludedIndicator" minOccurs="0"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ChargeType3Choice" minOccurs="0"/>
 *         &lt;element name="Rate" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}PercentageRate" minOccurs="0"/>
 *         &lt;element name="Br" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="Agt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TaxCharges2" minOccurs="0"/>
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
public class CA04ChargesRecord2 {

    @XmlElement(name = "Amt", required = true)
    protected CA04ActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd")
    @XmlSchemaType(name = "string")
    protected CA04CreditDebitCode cdtDbtInd;
    @XmlElement(name = "ChrgInclInd")
    protected Boolean chrgInclInd;
    @XmlElement(name = "Tp")
    protected CA04ChargeType3Choice tp;
    @XmlElement(name = "Rate")
    protected BigDecimal rate;
    @XmlElement(name = "Br")
    @XmlSchemaType(name = "string")
    protected CA04ChargeBearerType1Code br;
    @XmlElement(name = "Agt")
    protected CA04BranchAndFinancialInstitutionIdentification5 agt;
    @XmlElement(name = "Tax")
    protected CA04TaxCharges2 tax;

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CA04ActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(CA04ActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der cdtDbtInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public CA04CreditDebitCode getCdtDbtInd() {
        return cdtDbtInd;
    }

    /**
     * Legt den Wert der cdtDbtInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public void setCdtDbtInd(CA04CreditDebitCode value) {
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
     *     {@link CA04ChargeType3Choice }
     *     
     */
    public CA04ChargeType3Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ChargeType3Choice }
     *     
     */
    public void setTp(CA04ChargeType3Choice value) {
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
     *     {@link CA04ChargeBearerType1Code }
     *     
     */
    public CA04ChargeBearerType1Code getBr() {
        return br;
    }

    /**
     * Legt den Wert der br-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ChargeBearerType1Code }
     *     
     */
    public void setBr(CA04ChargeBearerType1Code value) {
        this.br = value;
    }

    /**
     * Ruft den Wert der agt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getAgt() {
        return agt;
    }

    /**
     * Legt den Wert der agt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.agt = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TaxCharges2 }
     *     
     */
    public CA04TaxCharges2 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TaxCharges2 }
     *     
     */
    public void setTax(CA04TaxCharges2 value) {
        this.tax = value;
    }

}
