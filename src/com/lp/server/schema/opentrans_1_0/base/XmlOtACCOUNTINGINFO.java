//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}COST_CATEGORY_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}COST_TYPE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}COST_ACCOUNT" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "costcategoryid",
    "costtype",
    "costaccount"
})
@XmlRootElement(name = "ACCOUNTING_INFO")
public class XmlOtACCOUNTINGINFO {

    @XmlElement(name = "COST_CATEGORY_ID", required = true)
    protected XmlOtCOSTCATEGORYID costcategoryid;
    @XmlElement(name = "COST_TYPE")
    protected String costtype;
    @XmlElement(name = "COST_ACCOUNT")
    protected String costaccount;

    /**
     * Ruft den Wert der costcategoryid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCOSTCATEGORYID }
     *     
     */
    public XmlOtCOSTCATEGORYID getCOSTCATEGORYID() {
        return costcategoryid;
    }

    /**
     * Legt den Wert der costcategoryid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCOSTCATEGORYID }
     *     
     */
    public void setCOSTCATEGORYID(XmlOtCOSTCATEGORYID value) {
        this.costcategoryid = value;
    }

    /**
     * Ruft den Wert der costtype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOSTTYPE() {
        return costtype;
    }

    /**
     * Legt den Wert der costtype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOSTTYPE(String value) {
        this.costtype = value;
    }

    /**
     * Ruft den Wert der costaccount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOSTACCOUNT() {
        return costaccount;
    }

    /**
     * Legt den Wert der costaccount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOSTACCOUNT(String value) {
        this.costaccount = value;
    }

}
