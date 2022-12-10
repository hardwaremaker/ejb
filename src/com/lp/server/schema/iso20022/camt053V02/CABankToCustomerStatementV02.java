//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer BankToCustomerStatementV02 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="BankToCustomerStatementV02">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrpHdr" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}GroupHeader42"/>
 *         &lt;element name="Stmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}AccountStatement2" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankToCustomerStatementV02", propOrder = {
    "grpHdr",
    "stmt"
})
public class CABankToCustomerStatementV02 {

    @XmlElement(name = "GrpHdr", required = true)
    protected CAGroupHeader42 grpHdr;
    @XmlElement(name = "Stmt", required = true)
    protected List<CAAccountStatement2> stmt;

    /**
     * Ruft den Wert der grpHdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAGroupHeader42 }
     *     
     */
    public CAGroupHeader42 getGrpHdr() {
        return grpHdr;
    }

    /**
     * Legt den Wert der grpHdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAGroupHeader42 }
     *     
     */
    public void setGrpHdr(CAGroupHeader42 value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the stmt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stmt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStmt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CAAccountStatement2 }
     * 
     * 
     */
    public List<CAAccountStatement2> getStmt() {
        if (stmt == null) {
            stmt = new ArrayList<CAAccountStatement2>();
        }
        return this.stmt;
    }

}
