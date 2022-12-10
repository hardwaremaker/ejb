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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Cheque6-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Cheque6-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ChqTp" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ChequeType2Code" minOccurs="0"/>
 *         &lt;element name="DlvryMtd" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ChequeDeliveryMethod1Choice" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cheque6-CH", propOrder = {
    "chqTp",
    "dlvryMtd"
})
public class PACHCheque6CH {

    @XmlElement(name = "ChqTp")
    @XmlSchemaType(name = "string")
    protected PACHChequeType2Code chqTp;
    @XmlElement(name = "DlvryMtd")
    protected PACHChequeDeliveryMethod1Choice dlvryMtd;

    /**
     * Ruft den Wert der chqTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHChequeType2Code }
     *     
     */
    public PACHChequeType2Code getChqTp() {
        return chqTp;
    }

    /**
     * Legt den Wert der chqTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHChequeType2Code }
     *     
     */
    public void setChqTp(PACHChequeType2Code value) {
        this.chqTp = value;
    }

    /**
     * Ruft den Wert der dlvryMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHChequeDeliveryMethod1Choice }
     *     
     */
    public PACHChequeDeliveryMethod1Choice getDlvryMtd() {
        return dlvryMtd;
    }

    /**
     * Legt den Wert der dlvryMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHChequeDeliveryMethod1Choice }
     *     
     */
    public void setDlvryMtd(PACHChequeDeliveryMethod1Choice value) {
        this.dlvryMtd = value;
    }

}
