//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SUPPLIER_AID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}INTERNATIONAL_AID" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BUYER_AID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DESCRIPTION_SHORT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DESCRIPTION_LONG" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MANUFACTURER_INFO" minOccurs="0"/>
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
    "supplieraid",
    "internationalaid",
    "buyeraid",
    "descriptionshort",
    "descriptionlong",
    "manufacturerinfo"
})
@XmlRootElement(name = "ARTICLE_ID")
public class XmlOtARTICLEID {

    @XmlElement(name = "SUPPLIER_AID", required = true)
    protected String supplieraid;
    @XmlElement(name = "INTERNATIONAL_AID")
    protected XmlOtINTERNATIONALAID internationalaid;
    @XmlElement(name = "BUYER_AID")
    protected List<XmlOtBUYERAID> buyeraid;
    @XmlElement(name = "DESCRIPTION_SHORT")
    protected String descriptionshort;
    @XmlElement(name = "DESCRIPTION_LONG")
    protected String descriptionlong;
    @XmlElement(name = "MANUFACTURER_INFO")
    protected XmlOtMANUFACTURERINFO manufacturerinfo;

    /**
     * Ruft den Wert der supplieraid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUPPLIERAID() {
        return supplieraid;
    }

    /**
     * Legt den Wert der supplieraid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUPPLIERAID(String value) {
        this.supplieraid = value;
    }

    /**
     * Ruft den Wert der internationalaid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtINTERNATIONALAID }
     *     
     */
    public XmlOtINTERNATIONALAID getINTERNATIONALAID() {
        return internationalaid;
    }

    /**
     * Legt den Wert der internationalaid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtINTERNATIONALAID }
     *     
     */
    public void setINTERNATIONALAID(XmlOtINTERNATIONALAID value) {
        this.internationalaid = value;
    }

    /**
     * Gets the value of the buyeraid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the buyeraid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBUYERAID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtBUYERAID }
     * 
     * 
     */
    public List<XmlOtBUYERAID> getBUYERAID() {
        if (buyeraid == null) {
            buyeraid = new ArrayList<XmlOtBUYERAID>();
        }
        return this.buyeraid;
    }

    /**
     * Ruft den Wert der descriptionshort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTIONSHORT() {
        return descriptionshort;
    }

    /**
     * Legt den Wert der descriptionshort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTIONSHORT(String value) {
        this.descriptionshort = value;
    }

    /**
     * Ruft den Wert der descriptionlong-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTIONLONG() {
        return descriptionlong;
    }

    /**
     * Legt den Wert der descriptionlong-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTIONLONG(String value) {
        this.descriptionlong = value;
    }

    /**
     * Ruft den Wert der manufacturerinfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtMANUFACTURERINFO }
     *     
     */
    public XmlOtMANUFACTURERINFO getMANUFACTURERINFO() {
        return manufacturerinfo;
    }

    /**
     * Legt den Wert der manufacturerinfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtMANUFACTURERINFO }
     *     
     */
    public void setMANUFACTURERINFO(XmlOtMANUFACTURERINFO value) {
        this.manufacturerinfo = value;
    }

}
