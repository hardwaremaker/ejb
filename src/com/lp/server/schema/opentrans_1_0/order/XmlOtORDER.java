//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_HEADER"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_ITEM_LIST"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_SUMMARY"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.opentrans.org/XMLSchema/1.0}dtSTRING" fixed="1.0" />
 *       &lt;attribute name="type" default="standard">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="standard"/>
 *             &lt;enumeration value="express"/>
 *             &lt;enumeration value="release"/>
 *             &lt;enumeration value="consignment"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orderheader",
    "orderitemlist",
    "ordersummary"
})
@XmlRootElement(name = "ORDER")
public class XmlOtORDER {

    @XmlElement(name = "ORDER_HEADER", required = true)
    protected XmlOtORDERHEADER orderheader;
    @XmlElement(name = "ORDER_ITEM_LIST", required = true)
    protected XmlOtORDERITEMLIST orderitemlist;
    @XmlElement(name = "ORDER_SUMMARY", required = true)
    protected XmlOtORDERSUMMARY ordersummary;
    @XmlAttribute(name = "version")
    protected String version;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * Ruft den Wert der orderheader-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtORDERHEADER }
     *     
     */
    public XmlOtORDERHEADER getORDERHEADER() {
        return orderheader;
    }

    /**
     * Legt den Wert der orderheader-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtORDERHEADER }
     *     
     */
    public void setORDERHEADER(XmlOtORDERHEADER value) {
        this.orderheader = value;
    }

    /**
     * Ruft den Wert der orderitemlist-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtORDERITEMLIST }
     *     
     */
    public XmlOtORDERITEMLIST getORDERITEMLIST() {
        return orderitemlist;
    }

    /**
     * Legt den Wert der orderitemlist-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtORDERITEMLIST }
     *     
     */
    public void setORDERITEMLIST(XmlOtORDERITEMLIST value) {
        this.orderitemlist = value;
    }

    /**
     * Ruft den Wert der ordersummary-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtORDERSUMMARY }
     *     
     */
    public XmlOtORDERSUMMARY getORDERSUMMARY() {
        return ordersummary;
    }

    /**
     * Legt den Wert der ordersummary-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtORDERSUMMARY }
     *     
     */
    public void setORDERSUMMARY(XmlOtORDERSUMMARY value) {
        this.ordersummary = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "standard";
        } else {
            return type;
        }
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
