//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer StructuredRemittanceInformation7 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="StructuredRemittanceInformation7">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RfrdDocInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ReferredDocumentInformation3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RfrdDocAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}RemittanceAmount1" minOccurs="0"/>
 *         &lt;element name="CdtrRefInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CreditorReferenceInformation2" minOccurs="0"/>
 *         &lt;element name="Invcr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="Invcee" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="AddtlRmtInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max140Text" maxOccurs="3" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StructuredRemittanceInformation7", propOrder = {
    "rfrdDocInf",
    "rfrdDocAmt",
    "cdtrRefInf",
    "invcr",
    "invcee",
    "addtlRmtInf"
})
public class PACHStructuredRemittanceInformation7 {

    @XmlElement(name = "RfrdDocInf")
    protected List<PACHReferredDocumentInformation3> rfrdDocInf;
    @XmlElement(name = "RfrdDocAmt")
    protected PACHRemittanceAmount1 rfrdDocAmt;
    @XmlElement(name = "CdtrRefInf")
    protected PACHCreditorReferenceInformation2 cdtrRefInf;
    @XmlElement(name = "Invcr")
    protected PACHPartyIdentification32 invcr;
    @XmlElement(name = "Invcee")
    protected PACHPartyIdentification32 invcee;
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
     * {@link PACHReferredDocumentInformation3 }
     * 
     * 
     */
    public List<PACHReferredDocumentInformation3> getRfrdDocInf() {
        if (rfrdDocInf == null) {
            rfrdDocInf = new ArrayList<PACHReferredDocumentInformation3>();
        }
        return this.rfrdDocInf;
    }

    /**
     * Ruft den Wert der rfrdDocAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHRemittanceAmount1 }
     *     
     */
    public PACHRemittanceAmount1 getRfrdDocAmt() {
        return rfrdDocAmt;
    }

    /**
     * Legt den Wert der rfrdDocAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHRemittanceAmount1 }
     *     
     */
    public void setRfrdDocAmt(PACHRemittanceAmount1 value) {
        this.rfrdDocAmt = value;
    }

    /**
     * Ruft den Wert der cdtrRefInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCreditorReferenceInformation2 }
     *     
     */
    public PACHCreditorReferenceInformation2 getCdtrRefInf() {
        return cdtrRefInf;
    }

    /**
     * Legt den Wert der cdtrRefInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCreditorReferenceInformation2 }
     *     
     */
    public void setCdtrRefInf(PACHCreditorReferenceInformation2 value) {
        this.cdtrRefInf = value;
    }

    /**
     * Ruft den Wert der invcr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32 }
     *     
     */
    public PACHPartyIdentification32 getInvcr() {
        return invcr;
    }

    /**
     * Legt den Wert der invcr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32 }
     *     
     */
    public void setInvcr(PACHPartyIdentification32 value) {
        this.invcr = value;
    }

    /**
     * Ruft den Wert der invcee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32 }
     *     
     */
    public PACHPartyIdentification32 getInvcee() {
        return invcee;
    }

    /**
     * Legt den Wert der invcee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32 }
     *     
     */
    public void setInvcee(PACHPartyIdentification32 value) {
        this.invcee = value;
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
