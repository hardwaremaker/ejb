//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer TransactionAgents2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionAgents2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="RcvgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="DlvrgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IssgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="SttlmPlc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ProprietaryAgent2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionAgents2", propOrder = {
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
public class CATransactionAgents2 {

    @XmlElement(name = "DbtrAgt")
    protected CABranchAndFinancialInstitutionIdentification4 dbtrAgt;
    @XmlElement(name = "CdtrAgt")
    protected CABranchAndFinancialInstitutionIdentification4 cdtrAgt;
    @XmlElement(name = "IntrmyAgt1")
    protected CABranchAndFinancialInstitutionIdentification4 intrmyAgt1;
    @XmlElement(name = "IntrmyAgt2")
    protected CABranchAndFinancialInstitutionIdentification4 intrmyAgt2;
    @XmlElement(name = "IntrmyAgt3")
    protected CABranchAndFinancialInstitutionIdentification4 intrmyAgt3;
    @XmlElement(name = "RcvgAgt")
    protected CABranchAndFinancialInstitutionIdentification4 rcvgAgt;
    @XmlElement(name = "DlvrgAgt")
    protected CABranchAndFinancialInstitutionIdentification4 dlvrgAgt;
    @XmlElement(name = "IssgAgt")
    protected CABranchAndFinancialInstitutionIdentification4 issgAgt;
    @XmlElement(name = "SttlmPlc")
    protected CABranchAndFinancialInstitutionIdentification4 sttlmPlc;
    @XmlElement(name = "Prtry")
    protected List<CAProprietaryAgent2> prtry;

    /**
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setDbtrAgt(CABranchAndFinancialInstitutionIdentification4 value) {
        this.dbtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setCdtrAgt(CABranchAndFinancialInstitutionIdentification4 value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt1(CABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getIntrmyAgt2() {
        return intrmyAgt2;
    }

    /**
     * Legt den Wert der intrmyAgt2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt2(CABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt2 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getIntrmyAgt3() {
        return intrmyAgt3;
    }

    /**
     * Legt den Wert der intrmyAgt3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt3(CABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt3 = value;
    }

    /**
     * Ruft den Wert der rcvgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getRcvgAgt() {
        return rcvgAgt;
    }

    /**
     * Legt den Wert der rcvgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setRcvgAgt(CABranchAndFinancialInstitutionIdentification4 value) {
        this.rcvgAgt = value;
    }

    /**
     * Ruft den Wert der dlvrgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getDlvrgAgt() {
        return dlvrgAgt;
    }

    /**
     * Legt den Wert der dlvrgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setDlvrgAgt(CABranchAndFinancialInstitutionIdentification4 value) {
        this.dlvrgAgt = value;
    }

    /**
     * Ruft den Wert der issgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getIssgAgt() {
        return issgAgt;
    }

    /**
     * Legt den Wert der issgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIssgAgt(CABranchAndFinancialInstitutionIdentification4 value) {
        this.issgAgt = value;
    }

    /**
     * Ruft den Wert der sttlmPlc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public CABranchAndFinancialInstitutionIdentification4 getSttlmPlc() {
        return sttlmPlc;
    }

    /**
     * Legt den Wert der sttlmPlc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setSttlmPlc(CABranchAndFinancialInstitutionIdentification4 value) {
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
     * {@link CAProprietaryAgent2 }
     * 
     * 
     */
    public List<CAProprietaryAgent2> getPrtry() {
        if (prtry == null) {
            prtry = new ArrayList<CAProprietaryAgent2>();
        }
        return this.prtry;
    }

}
