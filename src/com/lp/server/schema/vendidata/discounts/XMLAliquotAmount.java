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
import javax.xml.bind.annotation.XmlType;


/**
 * Payments for flat charges can be split between two periods.
 * This amount contains the aliquot value to be used for this payment and also information about the full amount.
 * 
 * <p>Java-Klasse fuer AliquotAmount complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AliquotAmount">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.vendidata.com/XML/Schema/Discounts}Amount">
 *       &lt;attribute name="valueFull" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="originalValueFull" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AliquotAmount")
public class XMLAliquotAmount
    extends XMLAmount
{

    @XmlAttribute(name = "valueFull", required = true)
    protected double valueFull;
    @XmlAttribute(name = "originalValueFull", required = true)
    protected double originalValueFull;

    /**
     * Ruft den Wert der valueFull-Eigenschaft ab.
     * 
     */
    public double getValueFull() {
        return valueFull;
    }

    /**
     * Legt den Wert der valueFull-Eigenschaft fest.
     * 
     */
    public void setValueFull(double value) {
        this.valueFull = value;
    }

    /**
     * Ruft den Wert der originalValueFull-Eigenschaft ab.
     * 
     */
    public double getOriginalValueFull() {
        return originalValueFull;
    }

    /**
     * Legt den Wert der originalValueFull-Eigenschaft fest.
     * 
     */
    public void setOriginalValueFull(double value) {
        this.originalValueFull = value;
    }

}
