//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.order;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TOTAL_ITEM_NUM"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TOTAL_AMOUNT" minOccurs="0"/>
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
    "totalitemnum",
    "totalamount"
})
@XmlRootElement(name = "ORDER_SUMMARY")
public class XmlOtORDERSUMMARY {

    @XmlElement(name = "TOTAL_ITEM_NUM", required = true)
    protected BigInteger totalitemnum;
    @XmlElement(name = "TOTAL_AMOUNT")
    protected BigDecimal totalamount;

    /**
     * Ruft den Wert der totalitemnum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTOTALITEMNUM() {
        return totalitemnum;
    }

    /**
     * Legt den Wert der totalitemnum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTOTALITEMNUM(BigInteger value) {
        this.totalitemnum = value;
    }

    /**
     * Ruft den Wert der totalamount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTOTALAMOUNT() {
        return totalamount;
    }

    /**
     * Legt den Wert der totalamount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTOTALAMOUNT(BigDecimal value) {
        this.totalamount = value;
    }

}
