//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r MessageIdentification2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="MessageIdentification2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgNmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *         &lt;element name="MsgId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageIdentification2", propOrder = {
    "msgNmId",
    "msgId"
})
public class CA04MessageIdentification2 {

    @XmlElement(name = "MsgNmId")
    protected String msgNmId;
    @XmlElement(name = "MsgId")
    protected String msgId;

    /**
     * Ruft den Wert der msgNmId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgNmId() {
        return msgNmId;
    }

    /**
     * Legt den Wert der msgNmId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgNmId(String value) {
        this.msgNmId = value;
    }

    /**
     * Ruft den Wert der msgId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Legt den Wert der msgId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

}
