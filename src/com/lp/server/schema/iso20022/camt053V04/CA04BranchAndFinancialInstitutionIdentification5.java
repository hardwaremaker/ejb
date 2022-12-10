//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BranchAndFinancialInstitutionIdentification5 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BranchAndFinancialInstitutionIdentification5">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FinInstnId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}FinancialInstitutionIdentification8"/>
 *         &lt;element name="BrnchId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchData2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BranchAndFinancialInstitutionIdentification5", propOrder = {
    "finInstnId",
    "brnchId"
})
public class CA04BranchAndFinancialInstitutionIdentification5 {

    @XmlElement(name = "FinInstnId", required = true)
    protected CA04FinancialInstitutionIdentification8 finInstnId;
    @XmlElement(name = "BrnchId")
    protected CA04BranchData2 brnchId;

    /**
     * Ruft den Wert der finInstnId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04FinancialInstitutionIdentification8 }
     *     
     */
    public CA04FinancialInstitutionIdentification8 getFinInstnId() {
        return finInstnId;
    }

    /**
     * Legt den Wert der finInstnId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04FinancialInstitutionIdentification8 }
     *     
     */
    public void setFinInstnId(CA04FinancialInstitutionIdentification8 value) {
        this.finInstnId = value;
    }

    /**
     * Ruft den Wert der brnchId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchData2 }
     *     
     */
    public CA04BranchData2 getBrnchId() {
        return brnchId;
    }

    /**
     * Legt den Wert der brnchId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchData2 }
     *     
     */
    public void setBrnchId(CA04BranchData2 value) {
        this.brnchId = value;
    }

}
