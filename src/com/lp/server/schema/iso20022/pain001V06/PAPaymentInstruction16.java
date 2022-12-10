//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

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
 * <p>Java-Klasse fuer PaymentInstruction16 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentInstruction16">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtInfId" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max35Text"/>
 *         &lt;element name="PmtMtd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PaymentMethod3Code"/>
 *         &lt;element name="BtchBookg" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BatchBookingIndicator" minOccurs="0"/>
 *         &lt;element name="NbOfTxs" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max15NumericText" minOccurs="0"/>
 *         &lt;element name="CtrlSum" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}DecimalNumber" minOccurs="0"/>
 *         &lt;element name="PmtTpInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PaymentTypeInformation19" minOccurs="0"/>
 *         &lt;element name="ReqdExctnDt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ISODate"/>
 *         &lt;element name="PoolgAdjstmntDt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ISODate" minOccurs="0"/>
 *         &lt;element name="Dbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43"/>
 *         &lt;element name="DbtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24"/>
 *         &lt;element name="DbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5"/>
 *         &lt;element name="DbtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="InstrForDbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max140Text" minOccurs="0"/>
 *         &lt;element name="UltmtDbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="ChrgBr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="ChrgsAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="ChrgsAcctAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="CdtTrfTxInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CreditTransferTransaction20" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInstruction16", propOrder = {
    "pmtInfId",
    "pmtMtd",
    "btchBookg",
    "nbOfTxs",
    "ctrlSum",
    "pmtTpInf",
    "reqdExctnDt",
    "poolgAdjstmntDt",
    "dbtr",
    "dbtrAcct",
    "dbtrAgt",
    "dbtrAgtAcct",
    "instrForDbtrAgt",
    "ultmtDbtr",
    "chrgBr",
    "chrgsAcct",
    "chrgsAcctAgt",
    "cdtTrfTxInf"
})
public class PAPaymentInstruction16 {

    @XmlElement(name = "PmtInfId", required = true)
    protected String pmtInfId;
    @XmlElement(name = "PmtMtd", required = true)
    @XmlSchemaType(name = "string")
    protected PAPaymentMethod3Code pmtMtd;
    @XmlElement(name = "BtchBookg")
    protected Boolean btchBookg;
    @XmlElement(name = "NbOfTxs")
    protected String nbOfTxs;
    @XmlElement(name = "CtrlSum")
    protected BigDecimal ctrlSum;
    @XmlElement(name = "PmtTpInf")
    protected PAPaymentTypeInformation19 pmtTpInf;
    @XmlElement(name = "ReqdExctnDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar reqdExctnDt;
    @XmlElement(name = "PoolgAdjstmntDt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar poolgAdjstmntDt;
    @XmlElement(name = "Dbtr", required = true)
    protected PAPartyIdentification43 dbtr;
    @XmlElement(name = "DbtrAcct", required = true)
    protected PACashAccount24 dbtrAcct;
    @XmlElement(name = "DbtrAgt", required = true)
    protected PABranchAndFinancialInstitutionIdentification5 dbtrAgt;
    @XmlElement(name = "DbtrAgtAcct")
    protected PACashAccount24 dbtrAgtAcct;
    @XmlElement(name = "InstrForDbtrAgt")
    protected String instrForDbtrAgt;
    @XmlElement(name = "UltmtDbtr")
    protected PAPartyIdentification43 ultmtDbtr;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PAChargeBearerType1Code chrgBr;
    @XmlElement(name = "ChrgsAcct")
    protected PACashAccount24 chrgsAcct;
    @XmlElement(name = "ChrgsAcctAgt")
    protected PABranchAndFinancialInstitutionIdentification5 chrgsAcctAgt;
    @XmlElement(name = "CdtTrfTxInf", required = true)
    protected List<PACreditTransferTransaction20> cdtTrfTxInf;

    /**
     * Ruft den Wert der pmtInfId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPmtInfId() {
        return pmtInfId;
    }

    /**
     * Legt den Wert der pmtInfId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPmtInfId(String value) {
        this.pmtInfId = value;
    }

    /**
     * Ruft den Wert der pmtMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPaymentMethod3Code }
     *     
     */
    public PAPaymentMethod3Code getPmtMtd() {
        return pmtMtd;
    }

    /**
     * Legt den Wert der pmtMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPaymentMethod3Code }
     *     
     */
    public void setPmtMtd(PAPaymentMethod3Code value) {
        this.pmtMtd = value;
    }

    /**
     * Ruft den Wert der btchBookg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBtchBookg() {
        return btchBookg;
    }

    /**
     * Legt den Wert der btchBookg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBtchBookg(Boolean value) {
        this.btchBookg = value;
    }

    /**
     * Ruft den Wert der nbOfTxs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbOfTxs() {
        return nbOfTxs;
    }

    /**
     * Legt den Wert der nbOfTxs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbOfTxs(String value) {
        this.nbOfTxs = value;
    }

    /**
     * Ruft den Wert der ctrlSum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCtrlSum() {
        return ctrlSum;
    }

    /**
     * Legt den Wert der ctrlSum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCtrlSum(BigDecimal value) {
        this.ctrlSum = value;
    }

    /**
     * Ruft den Wert der pmtTpInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPaymentTypeInformation19 }
     *     
     */
    public PAPaymentTypeInformation19 getPmtTpInf() {
        return pmtTpInf;
    }

    /**
     * Legt den Wert der pmtTpInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPaymentTypeInformation19 }
     *     
     */
    public void setPmtTpInf(PAPaymentTypeInformation19 value) {
        this.pmtTpInf = value;
    }

    /**
     * Ruft den Wert der reqdExctnDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReqdExctnDt() {
        return reqdExctnDt;
    }

    /**
     * Legt den Wert der reqdExctnDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReqdExctnDt(XMLGregorianCalendar value) {
        this.reqdExctnDt = value;
    }

    /**
     * Ruft den Wert der poolgAdjstmntDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPoolgAdjstmntDt() {
        return poolgAdjstmntDt;
    }

    /**
     * Legt den Wert der poolgAdjstmntDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPoolgAdjstmntDt(XMLGregorianCalendar value) {
        this.poolgAdjstmntDt = value;
    }

    /**
     * Ruft den Wert der dbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getDbtr() {
        return dbtr;
    }

    /**
     * Legt den Wert der dbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setDbtr(PAPartyIdentification43 value) {
        this.dbtr = value;
    }

    /**
     * Ruft den Wert der dbtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getDbtrAcct() {
        return dbtrAcct;
    }

    /**
     * Legt den Wert der dbtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setDbtrAcct(PACashAccount24 value) {
        this.dbtrAcct = value;
    }

    /**
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setDbtrAgt(PABranchAndFinancialInstitutionIdentification5 value) {
        this.dbtrAgt = value;
    }

    /**
     * Ruft den Wert der dbtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getDbtrAgtAcct() {
        return dbtrAgtAcct;
    }

    /**
     * Legt den Wert der dbtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setDbtrAgtAcct(PACashAccount24 value) {
        this.dbtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der instrForDbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrForDbtrAgt() {
        return instrForDbtrAgt;
    }

    /**
     * Legt den Wert der instrForDbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrForDbtrAgt(String value) {
        this.instrForDbtrAgt = value;
    }

    /**
     * Ruft den Wert der ultmtDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getUltmtDbtr() {
        return ultmtDbtr;
    }

    /**
     * Legt den Wert der ultmtDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setUltmtDbtr(PAPartyIdentification43 value) {
        this.ultmtDbtr = value;
    }

    /**
     * Ruft den Wert der chrgBr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAChargeBearerType1Code }
     *     
     */
    public PAChargeBearerType1Code getChrgBr() {
        return chrgBr;
    }

    /**
     * Legt den Wert der chrgBr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAChargeBearerType1Code }
     *     
     */
    public void setChrgBr(PAChargeBearerType1Code value) {
        this.chrgBr = value;
    }

    /**
     * Ruft den Wert der chrgsAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getChrgsAcct() {
        return chrgsAcct;
    }

    /**
     * Legt den Wert der chrgsAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setChrgsAcct(PACashAccount24 value) {
        this.chrgsAcct = value;
    }

    /**
     * Ruft den Wert der chrgsAcctAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getChrgsAcctAgt() {
        return chrgsAcctAgt;
    }

    /**
     * Legt den Wert der chrgsAcctAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setChrgsAcctAgt(PABranchAndFinancialInstitutionIdentification5 value) {
        this.chrgsAcctAgt = value;
    }

    /**
     * Gets the value of the cdtTrfTxInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cdtTrfTxInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCdtTrfTxInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PACreditTransferTransaction20 }
     * 
     * 
     */
    public List<PACreditTransferTransaction20> getCdtTrfTxInf() {
        if (cdtTrfTxInf == null) {
            cdtTrfTxInf = new ArrayList<PACreditTransferTransaction20>();
        }
        return this.cdtTrfTxInf;
    }

}
