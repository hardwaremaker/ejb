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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * For formula payment, this also specifies the exact date range for which the payment formula was evaluated
 * 
 * <p>Java-Klasse fuer FormulaPaymentPeriod complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FormulaPaymentPeriod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.vendidata.com/XML/Schema/Discounts}PaymentPeriod">
 *       &lt;attribute name="formulaPeriodStartDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="formulaPeriodEndDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormulaPaymentPeriod")
public class XMLFormulaPaymentPeriod
    extends XMLPaymentPeriod
{

    @XmlAttribute(name = "formulaPeriodStartDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar formulaPeriodStartDate;
    @XmlAttribute(name = "formulaPeriodEndDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar formulaPeriodEndDate;

    /**
     * Ruft den Wert der formulaPeriodStartDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFormulaPeriodStartDate() {
        return formulaPeriodStartDate;
    }

    /**
     * Legt den Wert der formulaPeriodStartDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFormulaPeriodStartDate(XMLGregorianCalendar value) {
        this.formulaPeriodStartDate = value;
    }

    /**
     * Ruft den Wert der formulaPeriodEndDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFormulaPeriodEndDate() {
        return formulaPeriodEndDate;
    }

    /**
     * Legt den Wert der formulaPeriodEndDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFormulaPeriodEndDate(XMLGregorianCalendar value) {
        this.formulaPeriodEndDate = value;
    }

}
