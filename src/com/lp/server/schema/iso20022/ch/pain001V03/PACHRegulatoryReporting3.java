//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer RegulatoryReporting3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RegulatoryReporting3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DbtCdtRptgInd" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}RegulatoryReportingType1Code" minOccurs="0"/>
 *         &lt;element name="Authrty" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}RegulatoryAuthority2" minOccurs="0"/>
 *         &lt;element name="Dtls" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}StructuredRegulatoryReporting3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegulatoryReporting3", propOrder = {
    "dbtCdtRptgInd",
    "authrty",
    "dtls"
})
public class PACHRegulatoryReporting3 {

    @XmlElement(name = "DbtCdtRptgInd")
    @XmlSchemaType(name = "string")
    protected PACHRegulatoryReportingType1Code dbtCdtRptgInd;
    @XmlElement(name = "Authrty")
    protected PACHRegulatoryAuthority2 authrty;
    @XmlElement(name = "Dtls")
    protected List<PACHStructuredRegulatoryReporting3> dtls;

    /**
     * Ruft den Wert der dbtCdtRptgInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHRegulatoryReportingType1Code }
     *     
     */
    public PACHRegulatoryReportingType1Code getDbtCdtRptgInd() {
        return dbtCdtRptgInd;
    }

    /**
     * Legt den Wert der dbtCdtRptgInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHRegulatoryReportingType1Code }
     *     
     */
    public void setDbtCdtRptgInd(PACHRegulatoryReportingType1Code value) {
        this.dbtCdtRptgInd = value;
    }

    /**
     * Ruft den Wert der authrty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHRegulatoryAuthority2 }
     *     
     */
    public PACHRegulatoryAuthority2 getAuthrty() {
        return authrty;
    }

    /**
     * Legt den Wert der authrty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHRegulatoryAuthority2 }
     *     
     */
    public void setAuthrty(PACHRegulatoryAuthority2 value) {
        this.authrty = value;
    }

    /**
     * Gets the value of the dtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PACHStructuredRegulatoryReporting3 }
     * 
     * 
     */
    public List<PACHStructuredRegulatoryReporting3> getDtls() {
        if (dtls == null) {
            dtls = new ArrayList<PACHStructuredRegulatoryReporting3>();
        }
        return this.dtls;
    }

}
