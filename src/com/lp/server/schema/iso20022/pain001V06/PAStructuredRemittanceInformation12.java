//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer StructuredRemittanceInformation12 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="StructuredRemittanceInformation12">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RfrdDocInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ReferredDocumentInformation6" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RfrdDocAmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}RemittanceAmount2" minOccurs="0"/>
 *         &lt;element name="CdtrRefInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CreditorReferenceInformation2" minOccurs="0"/>
 *         &lt;element name="Invcr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="Invcee" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="TaxRmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}TaxInformation4" minOccurs="0"/>
 *         &lt;element name="GrnshmtRmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Garnishment1" minOccurs="0"/>
 *         &lt;element name="AddtlRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max140Text" maxOccurs="3" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StructuredRemittanceInformation12", propOrder = {
    "rfrdDocInf",
    "rfrdDocAmt",
    "cdtrRefInf",
    "invcr",
    "invcee",
    "taxRmt",
    "grnshmtRmt",
    "addtlRmtInf"
})
public class PAStructuredRemittanceInformation12 {

    @XmlElement(name = "RfrdDocInf")
    protected List<PAReferredDocumentInformation6> rfrdDocInf;
    @XmlElement(name = "RfrdDocAmt")
    protected PARemittanceAmount2 rfrdDocAmt;
    @XmlElement(name = "CdtrRefInf")
    protected PACreditorReferenceInformation2 cdtrRefInf;
    @XmlElement(name = "Invcr")
    protected PAPartyIdentification43 invcr;
    @XmlElement(name = "Invcee")
    protected PAPartyIdentification43 invcee;
    @XmlElement(name = "TaxRmt")
    protected PATaxInformation4 taxRmt;
    @XmlElement(name = "GrnshmtRmt")
    protected PAGarnishment1 grnshmtRmt;
    @XmlElement(name = "AddtlRmtInf")
    protected List<String> addtlRmtInf;

    /**
     * Gets the value of the rfrdDocInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rfrdDocInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRfrdDocInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PAReferredDocumentInformation6 }
     * 
     * 
     */
    public List<PAReferredDocumentInformation6> getRfrdDocInf() {
        if (rfrdDocInf == null) {
            rfrdDocInf = new ArrayList<PAReferredDocumentInformation6>();
        }
        return this.rfrdDocInf;
    }

    /**
     * Ruft den Wert der rfrdDocAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PARemittanceAmount2 }
     *     
     */
    public PARemittanceAmount2 getRfrdDocAmt() {
        return rfrdDocAmt;
    }

    /**
     * Legt den Wert der rfrdDocAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PARemittanceAmount2 }
     *     
     */
    public void setRfrdDocAmt(PARemittanceAmount2 value) {
        this.rfrdDocAmt = value;
    }

    /**
     * Ruft den Wert der cdtrRefInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACreditorReferenceInformation2 }
     *     
     */
    public PACreditorReferenceInformation2 getCdtrRefInf() {
        return cdtrRefInf;
    }

    /**
     * Legt den Wert der cdtrRefInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACreditorReferenceInformation2 }
     *     
     */
    public void setCdtrRefInf(PACreditorReferenceInformation2 value) {
        this.cdtrRefInf = value;
    }

    /**
     * Ruft den Wert der invcr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getInvcr() {
        return invcr;
    }

    /**
     * Legt den Wert der invcr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setInvcr(PAPartyIdentification43 value) {
        this.invcr = value;
    }

    /**
     * Ruft den Wert der invcee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getInvcee() {
        return invcee;
    }

    /**
     * Legt den Wert der invcee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setInvcee(PAPartyIdentification43 value) {
        this.invcee = value;
    }

    /**
     * Ruft den Wert der taxRmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PATaxInformation4 }
     *     
     */
    public PATaxInformation4 getTaxRmt() {
        return taxRmt;
    }

    /**
     * Legt den Wert der taxRmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PATaxInformation4 }
     *     
     */
    public void setTaxRmt(PATaxInformation4 value) {
        this.taxRmt = value;
    }

    /**
     * Ruft den Wert der grnshmtRmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAGarnishment1 }
     *     
     */
    public PAGarnishment1 getGrnshmtRmt() {
        return grnshmtRmt;
    }

    /**
     * Legt den Wert der grnshmtRmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAGarnishment1 }
     *     
     */
    public void setGrnshmtRmt(PAGarnishment1 value) {
        this.grnshmtRmt = value;
    }

    /**
     * Gets the value of the addtlRmtInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addtlRmtInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddtlRmtInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddtlRmtInf() {
        if (addtlRmtInf == null) {
            addtlRmtInf = new ArrayList<String>();
        }
        return this.addtlRmtInf;
    }

}
