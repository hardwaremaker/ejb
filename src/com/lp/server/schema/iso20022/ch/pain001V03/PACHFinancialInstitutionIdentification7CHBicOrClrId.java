//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer FinancialInstitutionIdentification7-CH_BicOrClrId complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FinancialInstitutionIdentification7-CH_BicOrClrId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BIC" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BICIdentifier" minOccurs="0"/>
 *         &lt;element name="ClrSysMmbId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ClearingSystemMemberIdentification2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FinancialInstitutionIdentification7-CH_BicOrClrId", propOrder = {
    "bic",
    "clrSysMmbId"
})
public class PACHFinancialInstitutionIdentification7CHBicOrClrId {

    @XmlElement(name = "BIC")
    protected String bic;
    @XmlElement(name = "ClrSysMmbId")
    protected PACHClearingSystemMemberIdentification2 clrSysMmbId;

    /**
     * Ruft den Wert der bic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIC() {
        return bic;
    }

    /**
     * Legt den Wert der bic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIC(String value) {
        this.bic = value;
    }

    /**
     * Ruft den Wert der clrSysMmbId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHClearingSystemMemberIdentification2 }
     *     
     */
    public PACHClearingSystemMemberIdentification2 getClrSysMmbId() {
        return clrSysMmbId;
    }

    /**
     * Legt den Wert der clrSysMmbId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHClearingSystemMemberIdentification2 }
     *     
     */
    public void setClrSysMmbId(PACHClearingSystemMemberIdentification2 value) {
        this.clrSysMmbId = value;
    }

}
