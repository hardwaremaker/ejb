//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r TransactionAgents3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionAgents3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="RcvgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="DlvrgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IssgAgt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="SttlmPlc" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ProprietaryAgent3" maxOccurs="unbounded" minOccurs="0"/>
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
public class CA04TransactionAgents3 {

    @XmlElement(name = "DbtrAgt")
    protected CA04BranchAndFinancialInstitutionIdentification5 dbtrAgt;
    @XmlElement(name = "CdtrAgt")
    protected CA04BranchAndFinancialInstitutionIdentification5 cdtrAgt;
    @XmlElement(name = "IntrmyAgt1")
    protected CA04BranchAndFinancialInstitutionIdentification5 intrmyAgt1;
    @XmlElement(name = "IntrmyAgt2")
    protected CA04BranchAndFinancialInstitutionIdentification5 intrmyAgt2;
    @XmlElement(name = "IntrmyAgt3")
    protected CA04BranchAndFinancialInstitutionIdentification5 intrmyAgt3;
    @XmlElement(name = "RcvgAgt")
    protected CA04BranchAndFinancialInstitutionIdentification5 rcvgAgt;
    @XmlElement(name = "DlvrgAgt")
    protected CA04BranchAndFinancialInstitutionIdentification5 dlvrgAgt;
    @XmlElement(name = "IssgAgt")
    protected CA04BranchAndFinancialInstitutionIdentification5 issgAgt;
    @XmlElement(name = "SttlmPlc")
    protected CA04BranchAndFinancialInstitutionIdentification5 sttlmPlc;
    @XmlElement(name = "Prtry")
    protected List<CA04ProprietaryAgent3> prtry;

    /**
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setDbtrAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.dbtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setCdtrAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt1(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getIntrmyAgt2() {
        return intrmyAgt2;
    }

    /**
     * Legt den Wert der intrmyAgt2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt2(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt2 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getIntrmyAgt3() {
        return intrmyAgt3;
    }

    /**
     * Legt den Wert der intrmyAgt3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt3(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt3 = value;
    }

    /**
     * Ruft den Wert der rcvgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getRcvgAgt() {
        return rcvgAgt;
    }

    /**
     * Legt den Wert der rcvgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setRcvgAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.rcvgAgt = value;
    }

    /**
     * Ruft den Wert der dlvrgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getDlvrgAgt() {
        return dlvrgAgt;
    }

    /**
     * Legt den Wert der dlvrgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setDlvrgAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.dlvrgAgt = value;
    }

    /**
     * Ruft den Wert der issgAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getIssgAgt() {
        return issgAgt;
    }

    /**
     * Legt den Wert der issgAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIssgAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.issgAgt = value;
    }

    /**
     * Ruft den Wert der sttlmPlc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getSttlmPlc() {
        return sttlmPlc;
    }

    /**
     * Legt den Wert der sttlmPlc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setSttlmPlc(CA04BranchAndFinancialInstitutionIdentification5 value) {
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
     * {@link CA04ProprietaryAgent3 }
     * 
     * 
     */
    public List<CA04ProprietaryAgent3> getPrtry() {
        if (prtry == null) {
            prtry = new ArrayList<CA04ProprietaryAgent3>();
        }
        return this.prtry;
    }

}
