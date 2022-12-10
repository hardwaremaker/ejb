//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer AmendmentInformationDetails6 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AmendmentInformationDetails6">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrgnlMndtId" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Max35Text" minOccurs="0"/>
 *         &lt;element name="OrgnlCdtrSchmeId" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="OrgnlCdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="OrgnlCdtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="OrgnlDbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="OrgnlDbtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="OrgnlDbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="OrgnlDbtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="OrgnlFnlColltnDt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}ISODate" minOccurs="0"/>
 *         &lt;element name="OrgnlFrqcy" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Frequency1Code" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmendmentInformationDetails6", propOrder = {
    "orgnlMndtId",
    "orgnlCdtrSchmeId",
    "orgnlCdtrAgt",
    "orgnlCdtrAgtAcct",
    "orgnlDbtr",
    "orgnlDbtrAcct",
    "orgnlDbtrAgt",
    "orgnlDbtrAgtAcct",
    "orgnlFnlColltnDt",
    "orgnlFrqcy"
})
public class PAAmendmentInformationDetails6 {

    @XmlElement(name = "OrgnlMndtId")
    protected String orgnlMndtId;
    @XmlElement(name = "OrgnlCdtrSchmeId")
    protected PAPartyIdentification32 orgnlCdtrSchmeId;
    @XmlElement(name = "OrgnlCdtrAgt")
    protected PABranchAndFinancialInstitutionIdentification4 orgnlCdtrAgt;
    @XmlElement(name = "OrgnlCdtrAgtAcct")
    protected PACashAccount16 orgnlCdtrAgtAcct;
    @XmlElement(name = "OrgnlDbtr")
    protected PAPartyIdentification32 orgnlDbtr;
    @XmlElement(name = "OrgnlDbtrAcct")
    protected PACashAccount16 orgnlDbtrAcct;
    @XmlElement(name = "OrgnlDbtrAgt")
    protected PABranchAndFinancialInstitutionIdentification4 orgnlDbtrAgt;
    @XmlElement(name = "OrgnlDbtrAgtAcct")
    protected PACashAccount16 orgnlDbtrAgtAcct;
    @XmlElement(name = "OrgnlFnlColltnDt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar orgnlFnlColltnDt;
    @XmlElement(name = "OrgnlFrqcy")
    @XmlSchemaType(name = "string")
    protected PAFrequency1Code orgnlFrqcy;

    /**
     * Ruft den Wert der orgnlMndtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgnlMndtId() {
        return orgnlMndtId;
    }

    /**
     * Legt den Wert der orgnlMndtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgnlMndtId(String value) {
        this.orgnlMndtId = value;
    }

    /**
     * Ruft den Wert der orgnlCdtrSchmeId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getOrgnlCdtrSchmeId() {
        return orgnlCdtrSchmeId;
    }

    /**
     * Legt den Wert der orgnlCdtrSchmeId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setOrgnlCdtrSchmeId(PAPartyIdentification32 value) {
        this.orgnlCdtrSchmeId = value;
    }

    /**
     * Ruft den Wert der orgnlCdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getOrgnlCdtrAgt() {
        return orgnlCdtrAgt;
    }

    /**
     * Legt den Wert der orgnlCdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setOrgnlCdtrAgt(PABranchAndFinancialInstitutionIdentification4 value) {
        this.orgnlCdtrAgt = value;
    }

    /**
     * Ruft den Wert der orgnlCdtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getOrgnlCdtrAgtAcct() {
        return orgnlCdtrAgtAcct;
    }

    /**
     * Legt den Wert der orgnlCdtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setOrgnlCdtrAgtAcct(PACashAccount16 value) {
        this.orgnlCdtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der orgnlDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getOrgnlDbtr() {
        return orgnlDbtr;
    }

    /**
     * Legt den Wert der orgnlDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setOrgnlDbtr(PAPartyIdentification32 value) {
        this.orgnlDbtr = value;
    }

    /**
     * Ruft den Wert der orgnlDbtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getOrgnlDbtrAcct() {
        return orgnlDbtrAcct;
    }

    /**
     * Legt den Wert der orgnlDbtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setOrgnlDbtrAcct(PACashAccount16 value) {
        this.orgnlDbtrAcct = value;
    }

    /**
     * Ruft den Wert der orgnlDbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getOrgnlDbtrAgt() {
        return orgnlDbtrAgt;
    }

    /**
     * Legt den Wert der orgnlDbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setOrgnlDbtrAgt(PABranchAndFinancialInstitutionIdentification4 value) {
        this.orgnlDbtrAgt = value;
    }

    /**
     * Ruft den Wert der orgnlDbtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getOrgnlDbtrAgtAcct() {
        return orgnlDbtrAgtAcct;
    }

    /**
     * Legt den Wert der orgnlDbtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setOrgnlDbtrAgtAcct(PACashAccount16 value) {
        this.orgnlDbtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der orgnlFnlColltnDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrgnlFnlColltnDt() {
        return orgnlFnlColltnDt;
    }

    /**
     * Legt den Wert der orgnlFnlColltnDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrgnlFnlColltnDt(XMLGregorianCalendar value) {
        this.orgnlFnlColltnDt = value;
    }

    /**
     * Ruft den Wert der orgnlFrqcy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAFrequency1Code }
     *     
     */
    public PAFrequency1Code getOrgnlFrqcy() {
        return orgnlFrqcy;
    }

    /**
     * Legt den Wert der orgnlFrqcy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAFrequency1Code }
     *     
     */
    public void setOrgnlFrqcy(PAFrequency1Code value) {
        this.orgnlFrqcy = value;
    }

}
