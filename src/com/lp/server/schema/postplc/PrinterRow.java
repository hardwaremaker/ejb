
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PrinterRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PrinterRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Encoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LabelFormatID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LanguageID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaperLayoutID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrinterRow", propOrder = {
    "encoding",
    "labelFormatID",
    "languageID",
    "paperLayoutID"
})
public class PrinterRow {

    @XmlElementRef(name = "Encoding", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> encoding;
    @XmlElementRef(name = "LabelFormatID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> labelFormatID;
    @XmlElementRef(name = "LanguageID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> languageID;
    @XmlElementRef(name = "PaperLayoutID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> paperLayoutID;

    /**
     * Ruft den Wert der encoding-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEncoding() {
        return encoding;
    }

    /**
     * Legt den Wert der encoding-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEncoding(JAXBElement<String> value) {
        this.encoding = value;
    }

    /**
     * Ruft den Wert der labelFormatID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLabelFormatID() {
        return labelFormatID;
    }

    /**
     * Legt den Wert der labelFormatID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLabelFormatID(JAXBElement<String> value) {
        this.labelFormatID = value;
    }

    /**
     * Ruft den Wert der languageID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLanguageID() {
        return languageID;
    }

    /**
     * Legt den Wert der languageID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLanguageID(JAXBElement<String> value) {
        this.languageID = value;
    }

    /**
     * Ruft den Wert der paperLayoutID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPaperLayoutID() {
        return paperLayoutID;
    }

    /**
     * Legt den Wert der paperLayoutID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPaperLayoutID(JAXBElement<String> value) {
        this.paperLayoutID = value;
    }

}
