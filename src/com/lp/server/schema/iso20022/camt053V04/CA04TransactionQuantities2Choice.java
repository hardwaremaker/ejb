//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für TransactionQuantities2Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransactionQuantities2Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Qty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}FinancialInstrumentQuantityChoice"/>
 *         &lt;element name="OrgnlAndCurFaceAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}OriginalAndCurrentQuantities1"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ProprietaryQuantity1"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionQuantities2Choice", propOrder = {
    "qty",
    "orgnlAndCurFaceAmt",
    "prtry"
})
public class CA04TransactionQuantities2Choice {

    @XmlElement(name = "Qty")
    protected CA04FinancialInstrumentQuantityChoice qty;
    @XmlElement(name = "OrgnlAndCurFaceAmt")
    protected CA04OriginalAndCurrentQuantities1 orgnlAndCurFaceAmt;
    @XmlElement(name = "Prtry")
    protected CA04ProprietaryQuantity1 prtry;

    /**
     * Ruft den Wert der qty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04FinancialInstrumentQuantityChoice }
     *     
     */
    public CA04FinancialInstrumentQuantityChoice getQty() {
        return qty;
    }

    /**
     * Legt den Wert der qty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04FinancialInstrumentQuantityChoice }
     *     
     */
    public void setQty(CA04FinancialInstrumentQuantityChoice value) {
        this.qty = value;
    }

    /**
     * Ruft den Wert der orgnlAndCurFaceAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04OriginalAndCurrentQuantities1 }
     *     
     */
    public CA04OriginalAndCurrentQuantities1 getOrgnlAndCurFaceAmt() {
        return orgnlAndCurFaceAmt;
    }

    /**
     * Legt den Wert der orgnlAndCurFaceAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04OriginalAndCurrentQuantities1 }
     *     
     */
    public void setOrgnlAndCurFaceAmt(CA04OriginalAndCurrentQuantities1 value) {
        this.orgnlAndCurFaceAmt = value;
    }

    /**
     * Ruft den Wert der prtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04ProprietaryQuantity1 }
     *     
     */
    public CA04ProprietaryQuantity1 getPrtry() {
        return prtry;
    }

    /**
     * Legt den Wert der prtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ProprietaryQuantity1 }
     *     
     */
    public void setPrtry(CA04ProprietaryQuantity1 value) {
        this.prtry = value;
    }

}
