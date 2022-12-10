//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse f�r AccountStatement4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AccountStatement4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text"/>
 *         &lt;element name="StmtPgntn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Pagination" minOccurs="0"/>
 *         &lt;element name="ElctrncSeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Number" minOccurs="0"/>
 *         &lt;element name="LglSeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Number" minOccurs="0"/>
 *         &lt;element name="CreDtTm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ISODateTime"/>
 *         &lt;element name="FrToDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DateTimePeriodDetails" minOccurs="0"/>
 *         &lt;element name="CpyDplctInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CopyDuplicate1Code" minOccurs="0"/>
 *         &lt;element name="RptgSrc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ReportingSource1Choice" minOccurs="0"/>
 *         &lt;element name="Acct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashAccount25"/>
 *         &lt;element name="RltdAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashAccount24" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}AccountInterest2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Bal" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashBalance3" maxOccurs="unbounded"/>
 *         &lt;element name="TxsSummry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TotalTransactions4" minOccurs="0"/>
 *         &lt;element name="Ntry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ReportEntry4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AddtlStmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max500Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountStatement4", propOrder = {
    "id",
    "stmtPgntn",
    "elctrncSeqNb",
    "lglSeqNb",
    "creDtTm",
    "frToDt",
    "cpyDplctInd",
    "rptgSrc",
    "acct",
    "rltdAcct",
    "intrst",
    "bal",
    "txsSummry",
    "ntry",
    "addtlStmtInf"
})
public class CA04AccountStatement4 {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "StmtPgntn")
    protected CA04Pagination stmtPgntn;
    @XmlElement(name = "ElctrncSeqNb")
    protected BigDecimal elctrncSeqNb;
    @XmlElement(name = "LglSeqNb")
    protected BigDecimal lglSeqNb;
    @XmlElement(name = "CreDtTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creDtTm;
    @XmlElement(name = "FrToDt")
    protected CA04DateTimePeriodDetails frToDt;
    @XmlElement(name = "CpyDplctInd")
    @XmlSchemaType(name = "string")
    protected CA04CopyDuplicate1Code cpyDplctInd;
    @XmlElement(name = "RptgSrc")
    protected CA04ReportingSource1Choice rptgSrc;
    @XmlElement(name = "Acct", required = true)
    protected CA04CashAccount25 acct;
    @XmlElement(name = "RltdAcct")
    protected CA04CashAccount24 rltdAcct;
    @XmlElement(name = "Intrst")
    protected List<CA04AccountInterest2> intrst;
    @XmlElement(name = "Bal", required = true)
    protected List<CA04CashBalance3> bal;
    @XmlElement(name = "TxsSummry")
    protected CA04TotalTransactions4 txsSummry;
    @XmlElement(name = "Ntry")
    protected List<CA04ReportEntry4> ntry;
    @XmlElement(name = "AddtlStmtInf")
    protected String addtlStmtInf;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der stmtPgntn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04Pagination }
     *     
     */
    public CA04Pagination getStmtPgntn() {
        return stmtPgntn;
    }

    /**
     * Legt den Wert der stmtPgntn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04Pagination }
     *     
     */
    public void setStmtPgntn(CA04Pagination value) {
        this.stmtPgntn = value;
    }

    /**
     * Ruft den Wert der elctrncSeqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getElctrncSeqNb() {
        return elctrncSeqNb;
    }

    /**
     * Legt den Wert der elctrncSeqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setElctrncSeqNb(BigDecimal value) {
        this.elctrncSeqNb = value;
    }

    /**
     * Ruft den Wert der lglSeqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLglSeqNb() {
        return lglSeqNb;
    }

    /**
     * Legt den Wert der lglSeqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLglSeqNb(BigDecimal value) {
        this.lglSeqNb = value;
    }

    /**
     * Ruft den Wert der creDtTm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreDtTm() {
        return creDtTm;
    }

    /**
     * Legt den Wert der creDtTm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreDtTm(XMLGregorianCalendar value) {
        this.creDtTm = value;
    }

    /**
     * Ruft den Wert der frToDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04DateTimePeriodDetails }
     *     
     */
    public CA04DateTimePeriodDetails getFrToDt() {
        return frToDt;
    }

    /**
     * Legt den Wert der frToDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04DateTimePeriodDetails }
     *     
     */
    public void setFrToDt(CA04DateTimePeriodDetails value) {
        this.frToDt = value;
    }

    /**
     * Ruft den Wert der cpyDplctInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CopyDuplicate1Code }
     *     
     */
    public CA04CopyDuplicate1Code getCpyDplctInd() {
        return cpyDplctInd;
    }

    /**
     * Legt den Wert der cpyDplctInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CopyDuplicate1Code }
     *     
     */
    public void setCpyDplctInd(CA04CopyDuplicate1Code value) {
        this.cpyDplctInd = value;
    }

    /**
     * Ruft den Wert der rptgSrc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04ReportingSource1Choice }
     *     
     */
    public CA04ReportingSource1Choice getRptgSrc() {
        return rptgSrc;
    }

    /**
     * Legt den Wert der rptgSrc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ReportingSource1Choice }
     *     
     */
    public void setRptgSrc(CA04ReportingSource1Choice value) {
        this.rptgSrc = value;
    }

    /**
     * Ruft den Wert der acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CashAccount25 }
     *     
     */
    public CA04CashAccount25 getAcct() {
        return acct;
    }

    /**
     * Legt den Wert der acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CashAccount25 }
     *     
     */
    public void setAcct(CA04CashAccount25 value) {
        this.acct = value;
    }

    /**
     * Ruft den Wert der rltdAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CashAccount24 }
     *     
     */
    public CA04CashAccount24 getRltdAcct() {
        return rltdAcct;
    }

    /**
     * Legt den Wert der rltdAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CashAccount24 }
     *     
     */
    public void setRltdAcct(CA04CashAccount24 value) {
        this.rltdAcct = value;
    }

    /**
     * Gets the value of the intrst property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the intrst property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntrst().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04AccountInterest2 }
     * 
     * 
     */
    public List<CA04AccountInterest2> getIntrst() {
        if (intrst == null) {
            intrst = new ArrayList<CA04AccountInterest2>();
        }
        return this.intrst;
    }

    /**
     * Gets the value of the bal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04CashBalance3 }
     * 
     * 
     */
    public List<CA04CashBalance3> getBal() {
        if (bal == null) {
            bal = new ArrayList<CA04CashBalance3>();
        }
        return this.bal;
    }

    /**
     * Ruft den Wert der txsSummry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TotalTransactions4 }
     *     
     */
    public CA04TotalTransactions4 getTxsSummry() {
        return txsSummry;
    }

    /**
     * Legt den Wert der txsSummry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TotalTransactions4 }
     *     
     */
    public void setTxsSummry(CA04TotalTransactions4 value) {
        this.txsSummry = value;
    }

    /**
     * Gets the value of the ntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNtry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04ReportEntry4 }
     * 
     * 
     */
    public List<CA04ReportEntry4> getNtry() {
        if (ntry == null) {
            ntry = new ArrayList<CA04ReportEntry4>();
        }
        return this.ntry;
    }

    /**
     * Ruft den Wert der addtlStmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlStmtInf() {
        return addtlStmtInf;
    }

    /**
     * Legt den Wert der addtlStmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlStmtInf(String value) {
        this.addtlStmtInf = value;
    }

}
