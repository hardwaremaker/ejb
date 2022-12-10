//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.07.30 um 10:14:02 AM CEST 
//


package com.lp.server.schema.iso20022.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer Cheque6 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Cheque6">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ChqTp" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}ChequeType2Code" minOccurs="0"/>
 *         &lt;element name="ChqNb" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max35Text" minOccurs="0"/>
 *         &lt;element name="ChqFr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}NameAndAddress10" minOccurs="0"/>
 *         &lt;element name="DlvryMtd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}ChequeDeliveryMethod1Choice" minOccurs="0"/>
 *         &lt;element name="DlvrTo" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}NameAndAddress10" minOccurs="0"/>
 *         &lt;element name="InstrPrty" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Priority2Code" minOccurs="0"/>
 *         &lt;element name="ChqMtrtyDt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}ISODate" minOccurs="0"/>
 *         &lt;element name="FrmsCd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max35Text" minOccurs="0"/>
 *         &lt;element name="MemoFld" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max35Text" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="RgnlClrZone" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max35Text" minOccurs="0"/>
 *         &lt;element name="PrtLctn" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cheque6", propOrder = {
    "chqTp",
    "chqNb",
    "chqFr",
    "dlvryMtd",
    "dlvrTo",
    "instrPrty",
    "chqMtrtyDt",
    "frmsCd",
    "memoFld",
    "rgnlClrZone",
    "prtLctn"
})
public class PACheque6 {

    @XmlElement(name = "ChqTp")
    @XmlSchemaType(name = "string")
    protected PAChequeType2Code chqTp;
    @XmlElement(name = "ChqNb")
    protected String chqNb;
    @XmlElement(name = "ChqFr")
    protected PANameAndAddress10 chqFr;
    @XmlElement(name = "DlvryMtd")
    protected PAChequeDeliveryMethod1Choice dlvryMtd;
    @XmlElement(name = "DlvrTo")
    protected PANameAndAddress10 dlvrTo;
    @XmlElement(name = "InstrPrty")
    @XmlSchemaType(name = "string")
    protected PAPriority2Code instrPrty;
    @XmlElement(name = "ChqMtrtyDt")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar chqMtrtyDt;
    @XmlElement(name = "FrmsCd")
    protected String frmsCd;
    @XmlElement(name = "MemoFld")
    protected List<String> memoFld;
    @XmlElement(name = "RgnlClrZone")
    protected String rgnlClrZone;
    @XmlElement(name = "PrtLctn")
    protected String prtLctn;

    /**
     * Ruft den Wert der chqTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAChequeType2Code }
     *     
     */
    public PAChequeType2Code getChqTp() {
        return chqTp;
    }

    /**
     * Legt den Wert der chqTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAChequeType2Code }
     *     
     */
    public void setChqTp(PAChequeType2Code value) {
        this.chqTp = value;
    }

    /**
     * Ruft den Wert der chqNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChqNb() {
        return chqNb;
    }

    /**
     * Legt den Wert der chqNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChqNb(String value) {
        this.chqNb = value;
    }

    /**
     * Ruft den Wert der chqFr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public PANameAndAddress10 getChqFr() {
        return chqFr;
    }

    /**
     * Legt den Wert der chqFr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public void setChqFr(PANameAndAddress10 value) {
        this.chqFr = value;
    }

    /**
     * Ruft den Wert der dlvryMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAChequeDeliveryMethod1Choice }
     *     
     */
    public PAChequeDeliveryMethod1Choice getDlvryMtd() {
        return dlvryMtd;
    }

    /**
     * Legt den Wert der dlvryMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAChequeDeliveryMethod1Choice }
     *     
     */
    public void setDlvryMtd(PAChequeDeliveryMethod1Choice value) {
        this.dlvryMtd = value;
    }

    /**
     * Ruft den Wert der dlvrTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public PANameAndAddress10 getDlvrTo() {
        return dlvrTo;
    }

    /**
     * Legt den Wert der dlvrTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public void setDlvrTo(PANameAndAddress10 value) {
        this.dlvrTo = value;
    }

    /**
     * Ruft den Wert der instrPrty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPriority2Code }
     *     
     */
    public PAPriority2Code getInstrPrty() {
        return instrPrty;
    }

    /**
     * Legt den Wert der instrPrty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPriority2Code }
     *     
     */
    public void setInstrPrty(PAPriority2Code value) {
        this.instrPrty = value;
    }

    /**
     * Ruft den Wert der chqMtrtyDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getChqMtrtyDt() {
        return chqMtrtyDt;
    }

    /**
     * Legt den Wert der chqMtrtyDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setChqMtrtyDt(XMLGregorianCalendar value) {
        this.chqMtrtyDt = value;
    }

    /**
     * Ruft den Wert der frmsCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrmsCd() {
        return frmsCd;
    }

    /**
     * Legt den Wert der frmsCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrmsCd(String value) {
        this.frmsCd = value;
    }

    /**
     * Gets the value of the memoFld property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the memoFld property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMemoFld().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMemoFld() {
        if (memoFld == null) {
            memoFld = new ArrayList<String>();
        }
        return this.memoFld;
    }

    /**
     * Ruft den Wert der rgnlClrZone-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRgnlClrZone() {
        return rgnlClrZone;
    }

    /**
     * Legt den Wert der rgnlClrZone-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRgnlClrZone(String value) {
        this.rgnlClrZone = value;
    }

    /**
     * Ruft den Wert der prtLctn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrtLctn() {
        return prtLctn;
    }

    /**
     * Legt den Wert der prtLctn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrtLctn(String value) {
        this.prtLctn = value;
    }

}
