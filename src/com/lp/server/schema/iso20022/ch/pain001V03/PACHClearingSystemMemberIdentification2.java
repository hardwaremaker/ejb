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
 * <p>Java-Klasse fuer ClearingSystemMemberIdentification2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ClearingSystemMemberIdentification2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClrSysId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ClearingSystemIdentification2Choice" minOccurs="0"/>
 *         &lt;element name="MmbId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max35Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClearingSystemMemberIdentification2", propOrder = {
    "clrSysId",
    "mmbId"
})
public class PACHClearingSystemMemberIdentification2 {

    @XmlElement(name = "ClrSysId")
    protected PACHClearingSystemIdentification2Choice clrSysId;
    @XmlElement(name = "MmbId", required = true)
    protected String mmbId;

    /**
     * Ruft den Wert der clrSysId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHClearingSystemIdentification2Choice }
     *     
     */
    public PACHClearingSystemIdentification2Choice getClrSysId() {
        return clrSysId;
    }

    /**
     * Legt den Wert der clrSysId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHClearingSystemIdentification2Choice }
     *     
     */
    public void setClrSysId(PACHClearingSystemIdentification2Choice value) {
        this.clrSysId = value;
    }

    /**
     * Ruft den Wert der mmbId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMmbId() {
        return mmbId;
    }

    /**
     * Legt den Wert der mmbId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMmbId(String value) {
        this.mmbId = value;
    }

}
