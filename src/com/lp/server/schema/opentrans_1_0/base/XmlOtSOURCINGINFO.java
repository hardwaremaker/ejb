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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}QUOTATION_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}AGREEMENT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CATALOG_REFERENCE" minOccurs="0"/>
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
    "quotationid",
    "agreement",
    "catalogreference"
})
@XmlRootElement(name = "SOURCING_INFO")
public class XmlOtSOURCINGINFO {

    @XmlElement(name = "QUOTATION_ID")
    protected String quotationid;
    @XmlElement(name = "AGREEMENT")
    protected List<XmlOtAGREEMENT> agreement;
    @XmlElement(name = "CATALOG_REFERENCE")
    protected XmlOtCATALOGREFERENCE catalogreference;

    /**
     * Ruft den Wert der quotationid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQUOTATIONID() {
        return quotationid;
    }

    /**
     * Legt den Wert der quotationid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQUOTATIONID(String value) {
        this.quotationid = value;
    }

    /**
     * Gets the value of the agreement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agreement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAGREEMENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtAGREEMENT }
     * 
     * 
     */
    public List<XmlOtAGREEMENT> getAGREEMENT() {
        if (agreement == null) {
            agreement = new ArrayList<XmlOtAGREEMENT>();
        }
        return this.agreement;
    }

    /**
     * Ruft den Wert der catalogreference-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCATALOGREFERENCE }
     *     
     */
    public XmlOtCATALOGREFERENCE getCATALOGREFERENCE() {
        return catalogreference;
    }

    /**
     * Legt den Wert der catalogreference-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCATALOGREFERENCE }
     *     
     */
    public void setCATALOGREFERENCE(XmlOtCATALOGREFERENCE value) {
        this.catalogreference = value;
    }

}
