//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer TransactionAgents3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionAgents3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="RcvgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="DlvrgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IssgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="SttlmPlc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ProprietaryAgent3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionAgents3", propOrder = {
    "dbtrAgt",
    "cdtrAgt",
    "intrmyAgt1",
    "intrmyAgt2",
    "intrmyAgt3",
    "rcvgAgt",
    "dlvrgAgt",
    "issgAgt",
    "sttlmPlc",
    "prtry"
})
public class CATransactionAgents3 {

    @XmlElement(name = "DbtrAgt")
    protected CABranchAndFinancialInstitutionIdentification5 dbtrAgt;
    @XmlElement(name = "CdtrAgt")
    protected CABranchAndFinancialInstitutionIdentification5 cdtrAgt;
    @XmlElement(name = "IntrmyAgt1")
    protected CABranchAndFinancialInstitutionIdentification5 intrmyAgt1;
    @XmlElement(name = "IntrmyAgt2")
    protected CABranchAndFinancialInstitutionIdentification5 intrmyAgt2;
    @XmlElement(name = "IntrmyAgt3")
    protected CABranchAndFinancialInstitutionIdentification5 intrmyAgt3;
    @XmlElement(name = "RcvgAgt")
    protected CABranchAndFinancialInstitutionIdentification5 rcvgAgt;
    @XmlElement(name = "DlvrgAgt")
    protected CABranchAndFinancialInstitutionIdentification5 dlvrgAgt;
    @XmlElement(name = "IssgAgt")
    protected CABranchAndFinancialInstitutionIdentification5 issgAgt;
    @XmlElement(name = "SttlmPlc")
    protected CABranchAndFinancialInstitutionIdentification5 sttlmPlc;
    @XmlElement(name = "Prtry")
    protected List<CAProprietaryAgent3> prtry;

    /**
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setDbtrAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.dbtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setCdtrAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt1(CABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getIntrmyAgt2() {
        return intrmyAgt2;
    }

    /**
     * Legt den Wert der intrmyAgt2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt2(CABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt2 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getIntrmyAgt3() {
        return intrmyAgt3;
    }

    /**
     * Legt den Wert der intrmyAgt3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt3(CABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt3 = value;
    }

    /**
     * Ruft den Wert der rcvgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getRcvgAgt() {
        return rcvgAgt;
    }

    /**
     * Legt den Wert der rcvgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setRcvgAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.rcvgAgt = value;
    }

    /**
     * Ruft den Wert der dlvrgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getDlvrgAgt() {
        return dlvrgAgt;
    }

    /**
     * Legt den Wert der dlvrgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setDlvrgAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.dlvrgAgt = value;
    }

    /**
     * Ruft den Wert der issgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getIssgAgt() {
        return issgAgt;
    }

    /**
     * Legt den Wert der issgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIssgAgt(CABranchAndFinancialInstitutionIdentification5 value) {
        this.issgAgt = value;
    }

    /**
     * Ruft den Wert der sttlmPlc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification5 getSttlmPlc() {
        return sttlmPlc;
    }

    /**
     * Legt den Wert der sttlmPlc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setSttlmPlc(CABranchAndFinancialInstitutionIdentification5 value) {
        this.sttlmPlc = value;
    }

    /**
     * Gets the value of the prtry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prtry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrtry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CAProprietaryAgent3 }
     * 
     * 
     */
    public List<CAProprietaryAgent3> getPrtry() {
        if (prtry == null) {
            prtry = new ArrayList<CAProprietaryAgent3>();
        }
        return this.prtry;
    }

}
