//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r ProprietaryAgent3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ProprietaryAgent3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text"/>
 *         &lt;element name="Agt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BranchAndFinancialInstitutionIdentification5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProprietaryAgent3", propOrder = {
    "tp",
    "agt"
})
public class CA04ProprietaryAgent3 {

    @XmlElement(name = "Tp", required = true)
    protected String tp;
    @XmlElement(name = "Agt", required = true)
    protected CA04BranchAndFinancialInstitutionIdentification5 agt;

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTp(String value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der agt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public CA04BranchAndFinancialInstitutionIdentification5 getAgt() {
        return agt;
    }

    /**
     * Legt den Wert der agt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setAgt(CA04BranchAndFinancialInstitutionIdentification5 value) {
        this.agt = value;
    }

}
