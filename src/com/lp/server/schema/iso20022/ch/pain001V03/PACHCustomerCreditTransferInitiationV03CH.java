//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CustomerCreditTransferInitiationV03-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CustomerCreditTransferInitiationV03-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrpHdr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}GroupHeader32-CH"/>
 *         &lt;element name="PmtInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PaymentInstructionInformation3-CH" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerCreditTransferInitiationV03-CH", propOrder = {
    "grpHdr",
    "pmtInf"
})
public class PACHCustomerCreditTransferInitiationV03CH {

    @XmlElement(name = "GrpHdr", required = true)
    protected PACHGroupHeader32CH grpHdr;
    @XmlElement(name = "PmtInf", required = true)
    protected List<PACHPaymentInstructionInformation3CH> pmtInf;

    /**
     * Ruft den Wert der grpHdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHGroupHeader32CH }
     *     
     */
    public PACHGroupHeader32CH getGrpHdr() {
        return grpHdr;
    }

    /**
     * Legt den Wert der grpHdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHGroupHeader32CH }
     *     
     */
    public void setGrpHdr(PACHGroupHeader32CH value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the pmtInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pmtInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPmtInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PACHPaymentInstructionInformation3CH }
     * 
     * 
     */
    public List<PACHPaymentInstructionInformation3CH> getPmtInf() {
        if (pmtInf == null) {
            pmtInf = new ArrayList<PACHPaymentInstructionInformation3CH>();
        }
        return this.pmtInf;
    }

}
