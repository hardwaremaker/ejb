//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

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
 * <p>Java-Klasse fuer AccountStatement5 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AccountStatement5">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text"/>
 *         &lt;element name="StmtPgntn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Pagination" minOccurs="0"/>
 *         &lt;element name="ElctrncSeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Number" minOccurs="0"/>
 *         &lt;element name="LglSeqNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Number" minOccurs="0"/>
 *         &lt;element name="CreDtTm" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ISODateTime"/>
 *         &lt;element name="FrToDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}DateTimePeriodDetails" minOccurs="0"/>
 *         &lt;element name="CpyDplctInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CopyDuplicate1Code" minOccurs="0"/>
 *         &lt;element name="RptgSrc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ReportingSource1Choice" minOccurs="0"/>
 *         &lt;element name="Acct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccount25"/>
 *         &lt;element name="RltdAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashAccount24" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AccountInterest3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Bal" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashBalance3" maxOccurs="unbounded"/>
 *         &lt;element name="TxsSummry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TotalTransactions4" minOccurs="0"/>
 *         &lt;element name="Ntry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ReportEntry7" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AddtlStmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max500Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountStatement5", propOrder = {
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
public class CAAccountStatement5 {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "StmtPgntn")
    protected CAPagination stmtPgntn;
    @XmlElement(name = "ElctrncSeqNb")
    protected BigDecimal elctrncSeqNb;
    @XmlElement(name = "LglSeqNb")
    protected BigDecimal lglSeqNb;
    @XmlElement(name = "CreDtTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creDtTm;
    @XmlElement(name = "FrToDt")
    protected CADateTimePeriodDetails frToDt;
    @XmlElement(name = "CpyDplctInd")
    @XmlSchemaType(name = "string")
    protected CACopyDuplicate1Code cpyDplctInd;
    @XmlElement(name = "RptgSrc")
    protected CAReportingSource1Choice rptgSrc;
    @XmlElement(name = "Acct", required = true)
    protected CACashAccount25 acct;
    @XmlElement(name = "RltdAcct")
    protected CACashAccount24 rltdAcct;
    @XmlElement(name = "Intrst")
    protected List<CAAccountInterest3> intrst;
    @XmlElement(name = "Bal", required = true)
    protected List<CACashBalance3> bal;
    @XmlElement(name = "TxsSummry")
    protected CATotalTransactions4 txsSummry;
    @XmlElement(name = "Ntry")
    protected List<CAReportEntry7> ntry;
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
     *     {@link CAPagination }
     *     
     */
    public CAPagination getStmtPgntn() {
        return stmtPgntn;
    }

    /**
     * Legt den Wert der stmtPgntn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPagination }
     *     
     */
    public void setStmtPgntn(CAPagination value) {
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
     *     {@link CADateTimePeriodDetails }
     *     
     */
    public CADateTimePeriodDetails getFrToDt() {
        return frToDt;
    }

    /**
     * Legt den Wert der frToDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CADateTimePeriodDetails }
     *     
     */
    public void setFrToDt(CADateTimePeriodDetails value) {
        this.frToDt = value;
    }

    /**
     * Ruft den Wert der cpyDplctInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACopyDuplicate1Code }
     *     
     */
    public CACopyDuplicate1Code getCpyDplctInd() {
        return cpyDplctInd;
    }

    /**
     * Legt den Wert der cpyDplctInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACopyDuplicate1Code }
     *     
     */
    public void setCpyDplctInd(CACopyDuplicate1Code value) {
        this.cpyDplctInd = value;
    }

    /**
     * Ruft den Wert der rptgSrc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAReportingSource1Choice }
     *     
     */
    public CAReportingSource1Choice getRptgSrc() {
        return rptgSrc;
    }

    /**
     * Legt den Wert der rptgSrc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAReportingSource1Choice }
     *     
     */
    public void setRptgSrc(CAReportingSource1Choice value) {
        this.rptgSrc = value;
    }

    /**
     * Ruft den Wert der acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount25 }
     *     
     */
    public CACashAccount25 getAcct() {
        return acct;
    }

    /**
     * Legt den Wert der acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount25 }
     *     
     */
    public void setAcct(CACashAccount25 value) {
        this.acct = value;
    }

    /**
     * Ruft den Wert der rltdAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount24 }
     *     
     */
    public CACashAccount24 getRltdAcct() {
        return rltdAcct;
    }

    /**
     * Legt den Wert der rltdAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount24 }
     *     
     */
    public void setRltdAcct(CACashAccount24 value) {
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
     * {@link CAAccountInterest3 }
     * 
     * 
     */
    public List<CAAccountInterest3> getIntrst() {
        if (intrst == null) {
            intrst = new ArrayList<CAAccountInterest3>();
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
     * {@link CACashBalance3 }
     * 
     * 
     */
    public List<CACashBalance3> getBal() {
        if (bal == null) {
            bal = new ArrayList<CACashBalance3>();
        }
        return this.bal;
    }

    /**
     * Ruft den Wert der txsSummry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATotalTransactions4 }
     *     
     */
    public CATotalTransactions4 getTxsSummry() {
        return txsSummry;
    }

    /**
     * Legt den Wert der txsSummry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATotalTransactions4 }
     *     
     */
    public void setTxsSummry(CATotalTransactions4 value) {
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
     * {@link CAReportEntry7 }
     * 
     * 
     */
    public List<CAReportEntry7> getNtry() {
        if (ntry == null) {
            ntry = new ArrayList<CAReportEntry7>();
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
