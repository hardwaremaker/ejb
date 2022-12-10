//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PRICE_AMOUNT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PRICE_FLAG" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PRICE_LINE_AMOUNT"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TAX" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TAX_AMOUNT" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PRICE_QUANTITY" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;pattern value="net_list|gros_list|net_customer|nrp|net_customer_exp|udp_\w{1,16}"/>
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
    "priceamount",
    "priceflag",
    "pricelineamount",
    "tax",
    "taxamount",
    "pricequantity"
})
@XmlRootElement(name = "ARTICLE_PRICE")
public class XmlOtARTICLEPRICE {

    @XmlElement(name = "PRICE_AMOUNT", required = true)
    protected BigDecimal priceamount;
    @XmlElement(name = "PRICE_FLAG")
    protected List<XmlOtPRICEFLAG> priceflag;
    @XmlElement(name = "PRICE_LINE_AMOUNT", required = true)
    protected BigDecimal pricelineamount;
    @XmlElement(name = "TAX")
    protected BigDecimal tax;
    @XmlElement(name = "TAX_AMOUNT")
    protected BigDecimal taxamount;
    @XmlElement(name = "PRICE_QUANTITY")
    protected BigDecimal pricequantity;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * Ruft den Wert der priceamount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRICEAMOUNT() {
        return priceamount;
    }

    /**
     * Legt den Wert der priceamount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRICEAMOUNT(BigDecimal value) {
        this.priceamount = value;
    }

    /**
     * Gets the value of the priceflag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priceflag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPRICEFLAG().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtPRICEFLAG }
     * 
     * 
     */
    public List<XmlOtPRICEFLAG> getPRICEFLAG() {
        if (priceflag == null) {
            priceflag = new ArrayList<XmlOtPRICEFLAG>();
        }
        return this.priceflag;
    }

    /**
     * Ruft den Wert der pricelineamount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRICELINEAMOUNT() {
        return pricelineamount;
    }

    /**
     * Legt den Wert der pricelineamount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRICELINEAMOUNT(BigDecimal value) {
        this.pricelineamount = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTAX() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTAX(BigDecimal value) {
        this.tax = value;
    }

    /**
     * Ruft den Wert der taxamount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTAXAMOUNT() {
        return taxamount;
    }

    /**
     * Legt den Wert der taxamount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTAXAMOUNT(BigDecimal value) {
        this.taxamount = value;
    }

    /**
     * Ruft den Wert der pricequantity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRICEQUANTITY() {
        return pricequantity;
    }

    /**
     * Legt den Wert der pricequantity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRICEQUANTITY(BigDecimal value) {
        this.pricequantity = value;
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
        return type;
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
