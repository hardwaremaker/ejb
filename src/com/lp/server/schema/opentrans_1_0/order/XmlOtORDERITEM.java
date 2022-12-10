//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.lp.server.schema.opentrans_1_0.base.XmlOtACCOUNTINGINFO;
import com.lp.server.schema.opentrans_1_0.base.XmlOtARTICLEID;
import com.lp.server.schema.opentrans_1_0.base.XmlOtARTICLEPRICE;
import com.lp.server.schema.opentrans_1_0.base.XmlOtDELIVERYDATE;
import com.lp.server.schema.opentrans_1_0.base.XmlOtINTERNATIONALRESTRICTIONS;
import com.lp.server.schema.opentrans_1_0.base.XmlOtMIMEINFO;
import com.lp.server.schema.opentrans_1_0.base.XmlOtREMARK;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSHIPMENTPARTIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSOURCINGINFO;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSPECIALTREATMENTCLASS;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}LINE_ITEM_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ARTICLE_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}QUANTITY"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_UNIT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ARTICLE_PRICE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DELIVERY_DATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SOURCING_INFO" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SHIPMENT_PARTIES" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ACCOUNTING_INFO" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PARTIAL_SHIPMENT_ALLOWED" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TRANSPORT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}INTERNATIONAL_RESTRICTIONS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SPECIAL_TREATMENT_CLASS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}MIME_INFO" minOccurs="0"/>
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
    "lineitemid",
    "articleid",
    "quantity",
    "orderunit",
    "articleprice",
    "deliverydate",
    "sourcinginfo",
    "shipmentparties",
    "accountinginfo",
    "partialshipmentallowed",
    "transport",
    "internationalrestrictions",
    "specialtreatmentclass",
    "mimeinfo",
    "remark"
})
@XmlRootElement(name = "ORDER_ITEM")
public class XmlOtORDERITEM {

    @XmlElement(name = "LINE_ITEM_ID", required = true)
    protected String lineitemid;
    @XmlElement(name = "ARTICLE_ID", required = true)
    protected XmlOtARTICLEID articleid;
    @XmlElement(name = "QUANTITY", required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "ORDER_UNIT", required = true)
    protected String orderunit;
    @XmlElement(name = "ARTICLE_PRICE")
    protected XmlOtARTICLEPRICE articleprice;
    @XmlElement(name = "DELIVERY_DATE")
    protected XmlOtDELIVERYDATE deliverydate;
    @XmlElement(name = "SOURCING_INFO")
    protected XmlOtSOURCINGINFO sourcinginfo;
    @XmlElement(name = "SHIPMENT_PARTIES")
    protected XmlOtSHIPMENTPARTIES shipmentparties;
    @XmlElement(name = "ACCOUNTING_INFO")
    protected XmlOtACCOUNTINGINFO accountinginfo;
    @XmlElement(name = "PARTIAL_SHIPMENT_ALLOWED")
    protected String partialshipmentallowed;
    @XmlElement(name = "TRANSPORT")
    protected XmlOtTRANSPORT transport;
    @XmlElement(name = "INTERNATIONAL_RESTRICTIONS")
    protected List<XmlOtINTERNATIONALRESTRICTIONS> internationalrestrictions;
    @XmlElement(name = "SPECIAL_TREATMENT_CLASS")
    protected List<XmlOtSPECIALTREATMENTCLASS> specialtreatmentclass;
    @XmlElement(name = "MIME_INFO")
    protected XmlOtMIMEINFO mimeinfo;
    @XmlElement(name = "REMARK")
    protected List<XmlOtREMARK> remark;

    /**
     * Ruft den Wert der lineitemid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLINEITEMID() {
        return lineitemid;
    }

    /**
     * Legt den Wert der lineitemid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLINEITEMID(String value) {
        this.lineitemid = value;
    }

    /**
     * Ruft den Wert der articleid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtARTICLEID }
     *     
     */
    public XmlOtARTICLEID getARTICLEID() {
        return articleid;
    }

    /**
     * Legt den Wert der articleid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtARTICLEID }
     *     
     */
    public void setARTICLEID(XmlOtARTICLEID value) {
        this.articleid = value;
    }

    /**
     * Ruft den Wert der quantity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQUANTITY() {
        return quantity;
    }

    /**
     * Legt den Wert der quantity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQUANTITY(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Ruft den Wert der orderunit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERUNIT() {
        return orderunit;
    }

    /**
     * Legt den Wert der orderunit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERUNIT(String value) {
        this.orderunit = value;
    }

    /**
     * Ruft den Wert der articleprice-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtARTICLEPRICE }
     *     
     */
    public XmlOtARTICLEPRICE getARTICLEPRICE() {
        return articleprice;
    }

    /**
     * Legt den Wert der articleprice-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtARTICLEPRICE }
     *     
     */
    public void setARTICLEPRICE(XmlOtARTICLEPRICE value) {
        this.articleprice = value;
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
     * Ruft den Wert der sourcinginfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtSOURCINGINFO }
     *     
     */
    public XmlOtSOURCINGINFO getSOURCINGINFO() {
        return sourcinginfo;
    }

    /**
     * Legt den Wert der sourcinginfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtSOURCINGINFO }
     *     
     */
    public void setSOURCINGINFO(XmlOtSOURCINGINFO value) {
        this.sourcinginfo = value;
    }

    /**
     * Ruft den Wert der shipmentparties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtSHIPMENTPARTIES }
     *     
     */
    public XmlOtSHIPMENTPARTIES getSHIPMENTPARTIES() {
        return shipmentparties;
    }

    /**
     * Legt den Wert der shipmentparties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtSHIPMENTPARTIES }
     *     
     */
    public void setSHIPMENTPARTIES(XmlOtSHIPMENTPARTIES value) {
        this.shipmentparties = value;
    }

    /**
     * Ruft den Wert der accountinginfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtACCOUNTINGINFO }
     *     
     */
    public XmlOtACCOUNTINGINFO getACCOUNTINGINFO() {
        return accountinginfo;
    }

    /**
     * Legt den Wert der accountinginfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtACCOUNTINGINFO }
     *     
     */
    public void setACCOUNTINGINFO(XmlOtACCOUNTINGINFO value) {
        this.accountinginfo = value;
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
     * Gets the value of the specialtreatmentclass property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialtreatmentclass property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPECIALTREATMENTCLASS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtSPECIALTREATMENTCLASS }
     * 
     * 
     */
    public List<XmlOtSPECIALTREATMENTCLASS> getSPECIALTREATMENTCLASS() {
        if (specialtreatmentclass == null) {
            specialtreatmentclass = new ArrayList<XmlOtSPECIALTREATMENTCLASS>();
        }
        return this.specialtreatmentclass;
    }

    /**
     * Ruft den Wert der mimeinfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtMIMEINFO }
     *     
     */
    public XmlOtMIMEINFO getMIMEINFO() {
        return mimeinfo;
    }

    /**
     * Legt den Wert der mimeinfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtMIMEINFO }
     *     
     */
    public void setMIMEINFO(XmlOtMIMEINFO value) {
        this.mimeinfo = value;
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
