//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Specifies the year and period numbner of the payment period this payment is for
 * 
 * <p>Java-Klasse fuer PaymentPeriod complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentPeriod">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="periodYear" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="periodNr" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentPeriod")
@XmlSeeAlso({
    XMLFormulaPaymentPeriod.class
})
public class XMLPaymentPeriod {

    @XmlAttribute(name = "periodYear", required = true)
    protected int periodYear;
    @XmlAttribute(name = "periodNr", required = true)
    protected int periodNr;

    /**
     * Ruft den Wert der periodYear-Eigenschaft ab.
     * 
     */
    public int getPeriodYear() {
        return periodYear;
    }

    /**
     * Legt den Wert der periodYear-Eigenschaft fest.
     * 
     */
    public void setPeriodYear(int value) {
        this.periodYear = value;
    }

    /**
     * Ruft den Wert der periodNr-Eigenschaft ab.
     * 
     */
    public int getPeriodNr() {
        return periodNr;
    }

    /**
     * Legt den Wert der periodNr-Eigenschaft fest.
     * 
     */
    public void setPeriodNr(int value) {
        this.periodNr = value;
    }

}
