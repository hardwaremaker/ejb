//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * A payment containing a combined value of all possible amount types
 * 
 * <p>Java-Klasse fuer CombinedPaymentInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CombinedPaymentInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.vendidata.com/XML/Schema/Discounts}PaymentInfo">
 *       &lt;sequence>
 *         &lt;element name="TotalFormulaPayments" type="{http://www.vendidata.com/XML/Schema/Discounts}Amount"/>
 *         &lt;element name="TotalFlatchargePayments" type="{http://www.vendidata.com/XML/Schema/Discounts}AliquotAmount"/>
 *         &lt;element name="TotalSinglePayments" type="{http://www.vendidata.com/XML/Schema/Discounts}Amount"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CombinedPaymentInfo", propOrder = {
    "totalFormulaPayments",
    "totalFlatchargePayments",
    "totalSinglePayments"
})
@XmlSeeAlso({
    XMLDiscountPayment.class,
    XMLCustomerPaymentInfo.class,
    XMLVirtualVendingmachinePaymentInfo.class
})
public class XMLCombinedPaymentInfo
    extends XMLPaymentInfo
{

    @XmlElement(name = "TotalFormulaPayments", required = true)
    protected XMLAmount totalFormulaPayments;
    @XmlElement(name = "TotalFlatchargePayments", required = true)
    protected XMLAliquotAmount totalFlatchargePayments;
    @XmlElement(name = "TotalSinglePayments", required = true)
    protected XMLAmount totalSinglePayments;

    /**
     * Ruft den Wert der totalFormulaPayments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAmount }
     *     
     */
    public XMLAmount getTotalFormulaPayments() {
        return totalFormulaPayments;
    }

    /**
     * Legt den Wert der totalFormulaPayments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAmount }
     *     
     */
    public void setTotalFormulaPayments(XMLAmount value) {
        this.totalFormulaPayments = value;
    }

    /**
     * Ruft den Wert der totalFlatchargePayments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAliquotAmount }
     *     
     */
    public XMLAliquotAmount getTotalFlatchargePayments() {
        return totalFlatchargePayments;
    }

    /**
     * Legt den Wert der totalFlatchargePayments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAliquotAmount }
     *     
     */
    public void setTotalFlatchargePayments(XMLAliquotAmount value) {
        this.totalFlatchargePayments = value;
    }

    /**
     * Ruft den Wert der totalSinglePayments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAmount }
     *     
     */
    public XMLAmount getTotalSinglePayments() {
        return totalSinglePayments;
    }

    /**
     * Legt den Wert der totalSinglePayments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAmount }
     *     
     */
    public void setTotalSinglePayments(XMLAmount value) {
        this.totalSinglePayments = value;
    }

}
