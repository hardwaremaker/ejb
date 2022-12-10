//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer DataSource complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DataSource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="MonthlyBalance" type="{http://www.vendidata.com/XML/Schema/CommonObjects}MonthlyBalance"/>
 *         &lt;element name="CashBalance" type="{http://www.vendidata.com/XML/Schema/CommonObjects}CashBalance"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSource", propOrder = {
    "monthlyBalance",
    "cashBalance"
})
public class XMLDataSource {

    @XmlElement(name = "MonthlyBalance")
    protected XMLMonthlyBalance monthlyBalance;
    @XmlElement(name = "CashBalance")
    protected XMLCashBalance cashBalance;

    /**
     * Ruft den Wert der monthlyBalance-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLMonthlyBalance }
     *     
     */
    public XMLMonthlyBalance getMonthlyBalance() {
        return monthlyBalance;
    }

    /**
     * Legt den Wert der monthlyBalance-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLMonthlyBalance }
     *     
     */
    public void setMonthlyBalance(XMLMonthlyBalance value) {
        this.monthlyBalance = value;
    }

    /**
     * Ruft den Wert der cashBalance-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLCashBalance }
     *     
     */
    public XMLCashBalance getCashBalance() {
        return cashBalance;
    }

    /**
     * Legt den Wert der cashBalance-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCashBalance }
     *     
     */
    public void setCashBalance(XMLCashBalance value) {
        this.cashBalance = value;
    }

}
