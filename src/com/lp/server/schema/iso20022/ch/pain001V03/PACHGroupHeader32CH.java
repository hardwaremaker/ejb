//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer GroupHeader32-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GroupHeader32-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max35Text-Swift"/>
 *         &lt;element name="CreDtTm" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ISODateTime"/>
 *         &lt;element name="NbOfTxs" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max15NumericText"/>
 *         &lt;element name="CtrlSum" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}DecimalNumber" minOccurs="0"/>
 *         &lt;element name="InitgPty" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH_NameAndId"/>
 *         &lt;element name="FwdgAgt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupHeader32-CH", propOrder = {
    "msgId",
    "creDtTm",
    "nbOfTxs",
    "ctrlSum",
    "initgPty",
    "fwdgAgt"
})
public class PACHGroupHeader32CH {

    @XmlElement(name = "MsgId", required = true)
    protected String msgId;
    @XmlElement(name = "CreDtTm", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creDtTm;
    @XmlElement(name = "NbOfTxs", required = true)
    protected String nbOfTxs;
    @XmlElement(name = "CtrlSum")
    protected BigDecimal ctrlSum;
    @XmlElement(name = "InitgPty", required = true)
    protected PACHPartyIdentification32CHNameAndId initgPty;
    @XmlElement(name = "FwdgAgt")
    protected PACHBranchAndFinancialInstitutionIdentification4 fwdgAgt;

    /**
     * Ruft den Wert der msgId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Legt den Wert der msgId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
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
     * Ruft den Wert der initgPty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32CHNameAndId }
     *     
     */
    public PACHPartyIdentification32CHNameAndId getInitgPty() {
        return initgPty;
    }

    /**
     * Legt den Wert der initgPty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32CHNameAndId }
     *     
     */
    public void setInitgPty(PACHPartyIdentification32CHNameAndId value) {
        this.initgPty = value;
    }

    /**
     * Ruft den Wert der fwdgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PACHBranchAndFinancialInstitutionIdentification4 getFwdgAgt() {
        return fwdgAgt;
    }

    /**
     * Legt den Wert der fwdgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setFwdgAgt(PACHBranchAndFinancialInstitutionIdentification4 value) {
        this.fwdgAgt = value;
    }

}
