//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.order;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.lp.server.schema.opentrans_1_0.base.XmlOtDELIVERYDATE;
import com.lp.server.schema.opentrans_1_0.base.XmlOtDtCURRENCIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtINTERNATIONALRESTRICTIONS;
import com.lp.server.schema.opentrans_1_0.base.XmlOtORDERPARTIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtPAYMENT;
import com.lp.server.schema.opentrans_1_0.base.XmlOtREMARK;
import com.lp.server.schema.opentrans_1_0.base.XmlOtTRANSPORT;


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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ALT_CUSTOMER_ORDER_ID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_DATE"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DELIVERY_DATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_PARTIES"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PRICE_CURRENCY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PAYMENT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TERMS_AND_CONDITIONS" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PARTIAL_SHIPMENT_ALLOWED" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TRANSPORT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}INTERNATIONAL_RESTRICTIONS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}REMARK" maxOccurs="unbounded" minOccurs="0"/>
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
    "orderid",
    "altcustomerorderid",
    "orderdate",
    "deliverydate",
    "orderparties",
    "pricecurrency",
    "payment",
    "termsandconditions",
    "partialshipmentallowed",
    "transport",
    "internationalrestrictions",
    "remark"
})
@XmlRootElement(name = "ORDER_INFO")
public class XmlOtORDERINFO {

    @XmlElement(name = "ORDER_ID", required = true)
    protected String orderid;
    @XmlElement(name = "ALT_CUSTOMER_ORDER_ID")
    protected List<String> altcustomerorderid;
    @XmlElement(name = "ORDER_DATE", required = true)
    protected String orderdate;
    @XmlElement(name = "DELIVERY_DATE")
    protected XmlOtDELIVERYDATE deliverydate;
    @XmlElement(name = "ORDER_PARTIES", required = true)
    protected XmlOtORDERPARTIES orderparties;
    @XmlElement(name = "PRICE_CURRENCY")
    @XmlSchemaType(name = "string")
    protected XmlOtDtCURRENCIES pricecurrency;
    @XmlElement(name = "PAYMENT")
    protected XmlOtPAYMENT payment;
    @XmlElement(name = "TERMS_AND_CONDITIONS")
    protected String termsandconditions;
    @XmlElement(name = "PARTIAL_SHIPMENT_ALLOWED")
    protected String partialshipmentallowed;
    @XmlElement(name = "TRANSPORT")
    protected XmlOtTRANSPORT transport;
    @XmlElement(name = "INTERNATIONAL_RESTRICTIONS")
    protected List<XmlOtINTERNATIONALRESTRICTIONS> internationalrestrictions;
    @XmlElement(name = "REMARK")
    protected List<XmlOtREMARK> remark;

    /**
     * Ruft den Wert der orderid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERID() {
        return orderid;
    }

    /**
     * Legt den Wert der orderid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERID(String value) {
        this.orderid = value;
    }

    /**
     * Gets the value of the altcustomerorderid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the altcustomerorderid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALTCUSTOMERORDERID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getALTCUSTOMERORDERID() {
        if (altcustomerorderid == null) {
            altcustomerorderid = new ArrayList<String>();
        }
        return this.altcustomerorderid;
    }

    /**
     * Ruft den Wert der orderdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERDATE() {
        return orderdate;
    }

    /**
     * Legt den Wert der orderdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERDATE(String value) {
        this.orderdate = value;
    }

    /**
     * Ruft den Wert der deliverydate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtDELIVERYDATE }
     *     
     */
    public XmlOtDELIVERYDATE getDELIVERYDATE() {
        return deliverydate;
    }

    /**
     * Legt den Wert der deliverydate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtDELIVERYDATE }
     *     
     */
    public void setDELIVERYDATE(XmlOtDELIVERYDATE value) {
        this.deliverydate = value;
    }

    /**
     * Ruft den Wert der orderparties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtORDERPARTIES }
     *     
     */
    public XmlOtORDERPARTIES getORDERPARTIES() {
        return orderparties;
    }

    /**
     * Legt den Wert der orderparties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtORDERPARTIES }
     *     
     */
    public void setORDERPARTIES(XmlOtORDERPARTIES value) {
        this.orderparties = value;
    }

    /**
     * Ruft den Wert der pricecurrency-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtDtCURRENCIES }
     *     
     */
    public XmlOtDtCURRENCIES getPRICECURRENCY() {
        return pricecurrency;
    }

    /**
     * Legt den Wert der pricecurrency-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtDtCURRENCIES }
     *     
     */
    public void setPRICECURRENCY(XmlOtDtCURRENCIES value) {
        this.pricecurrency = value;
    }

    /**
     * Ruft den Wert der payment-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtPAYMENT }
     *     
     */
    public XmlOtPAYMENT getPAYMENT() {
        return payment;
    }

    /**
     * Legt den Wert der payment-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtPAYMENT }
     *     
     */
    public void setPAYMENT(XmlOtPAYMENT value) {
        this.payment = value;
    }

    /**
     * Ruft den Wert der termsandconditions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTERMSANDCONDITIONS() {
        return termsandconditions;
    }

    /**
     * Legt den Wert der termsandconditions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTERMSANDCONDITIONS(String value) {
        this.termsandconditions = value;
    }

    /**
     * Ruft den Wert der partialshipmentallowed-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPARTIALSHIPMENTALLOWED() {
        return partialshipmentallowed;
    }

    /**
     * Legt den Wert der partialshipmentallowed-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPARTIALSHIPMENTALLOWED(String value) {
        this.partialshipmentallowed = value;
    }

    /**
     * Ruft den Wert der transport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtTRANSPORT }
     *     
     */
    public XmlOtTRANSPORT getTRANSPORT() {
        return transport;
    }

    /**
     * Legt den Wert der transport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtTRANSPORT }
     *     
     */
    public void setTRANSPORT(XmlOtTRANSPORT value) {
        this.transport = value;
    }

    /**
     * Gets the value of the internationalrestrictions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the internationalrestrictions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getINTERNATIONALRESTRICTIONS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtINTERNATIONALRESTRICTIONS }
     * 
     * 
     */
    public List<XmlOtINTERNATIONALRESTRICTIONS> getINTERNATIONALRESTRICTIONS() {
        if (internationalrestrictions == null) {
            internationalrestrictions = new ArrayList<XmlOtINTERNATIONALRESTRICTIONS>();
        }
        return this.internationalrestrictions;
    }

    /**
     * Gets the value of the remark property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remark property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getREMARK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtREMARK }
     * 
     * 
     */
    public List<XmlOtREMARK> getREMARK() {
        if (remark == null) {
            remark = new ArrayList<XmlOtREMARK>();
        }
        return this.remark;
    }

}
