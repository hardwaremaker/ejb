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
 * <p>Java-Klasse fuer PaymentTypeInformation19-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentTypeInformation19-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstrPrty" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Priority2Code" minOccurs="0"/>
 *         &lt;element name="SvcLvl" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ServiceLevel8Choice" minOccurs="0"/>
 *         &lt;element name="LclInstrm" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}LocalInstrument2Choice" minOccurs="0"/>
 *         &lt;element name="CtgyPurp" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CategoryPurpose1-CH_Code" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentTypeInformation19-CH", propOrder = {
    "instrPrty",
    "svcLvl",
    "lclInstrm",
    "ctgyPurp"
})
public class PACHPaymentTypeInformation19CH {

    @XmlElement(name = "InstrPrty")
    @XmlSchemaType(name = "string")
    protected PACHPriority2Code instrPrty;
    @XmlElement(name = "SvcLvl")
    protected PACHServiceLevel8Choice svcLvl;
    @XmlElement(name = "LclInstrm")
    protected PACHLocalInstrument2Choice lclInstrm;
    @XmlElement(name = "CtgyPurp")
    protected PACHCategoryPurpose1CHCode ctgyPurp;

    /**
     * Ruft den Wert der instrPrty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPriority2Code }
     *     
     */
    public PACHPriority2Code getInstrPrty() {
        return instrPrty;
    }

    /**
     * Legt den Wert der instrPrty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPriority2Code }
     *     
     */
    public void setInstrPrty(PACHPriority2Code value) {
        this.instrPrty = value;
    }

    /**
     * Ruft den Wert der svcLvl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHServiceLevel8Choice }
     *     
     */
    public PACHServiceLevel8Choice getSvcLvl() {
        return svcLvl;
    }

    /**
     * Legt den Wert der svcLvl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHServiceLevel8Choice }
     *     
     */
    public void setSvcLvl(PACHServiceLevel8Choice value) {
        this.svcLvl = value;
    }

    /**
     * Ruft den Wert der lclInstrm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHLocalInstrument2Choice }
     *     
     */
    public PACHLocalInstrument2Choice getLclInstrm() {
        return lclInstrm;
    }

    /**
     * Legt den Wert der lclInstrm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHLocalInstrument2Choice }
     *     
     */
    public void setLclInstrm(PACHLocalInstrument2Choice value) {
        this.lclInstrm = value;
    }

    /**
     * Ruft den Wert der ctgyPurp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCategoryPurpose1CHCode }
     *     
     */
    public PACHCategoryPurpose1CHCode getCtgyPurp() {
        return ctgyPurp;
    }

    /**
     * Legt den Wert der ctgyPurp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCategoryPurpose1CHCode }
     *     
     */
    public void setCtgyPurp(PACHCategoryPurpose1CHCode value) {
        this.ctgyPurp = value;
    }

}
