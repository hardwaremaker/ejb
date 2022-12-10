
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ImportShipmentResult" type="{http://post.ondot.at}ArrayOfColloRow" minOccurs="0"/>
 *         &lt;element name="zplLabelData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pdfData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipmentDocuments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "importShipmentResult",
    "zplLabelData",
    "pdfData",
    "shipmentDocuments",
    "errorCode",
    "errorMessage"
})
@XmlRootElement(name = "ImportShipmentResponse")
public class ImportShipmentResponse {

    @XmlElementRef(name = "ImportShipmentResult", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloRow> importShipmentResult;
    @XmlElementRef(name = "zplLabelData", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> zplLabelData;
    @XmlElementRef(name = "pdfData", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> pdfData;
    @XmlElementRef(name = "shipmentDocuments", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> shipmentDocuments;
    @XmlElementRef(name = "errorCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorCode;
    @XmlElementRef(name = "errorMessage", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorMessage;

    /**
     * Ruft den Wert der importShipmentResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloRow> getImportShipmentResult() {
        return importShipmentResult;
    }

    /**
     * Legt den Wert der importShipmentResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public void setImportShipmentResult(JAXBElement<ArrayOfColloRow> value) {
        this.importShipmentResult = value;
    }

    /**
     * Ruft den Wert der zplLabelData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getZplLabelData() {
        return zplLabelData;
    }

    /**
     * Legt den Wert der zplLabelData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setZplLabelData(JAXBElement<String> value) {
        this.zplLabelData = value;
    }

    /**
     * Ruft den Wert der pdfData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPdfData() {
        return pdfData;
    }

    /**
     * Legt den Wert der pdfData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPdfData(JAXBElement<String> value) {
        this.pdfData = value;
    }

    /**
     * Ruft den Wert der shipmentDocuments-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getShipmentDocuments() {
        return shipmentDocuments;
    }

    /**
     * Legt den Wert der shipmentDocuments-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setShipmentDocuments(JAXBElement<String> value) {
        this.shipmentDocuments = value;
    }

    /**
     * Ruft den Wert der errorCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getErrorCode() {
        return errorCode;
    }

    /**
     * Legt den Wert der errorCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setErrorCode(JAXBElement<String> value) {
        this.errorCode = value;
    }

    /**
     * Ruft den Wert der errorMessage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Legt den Wert der errorMessage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setErrorMessage(JAXBElement<String> value) {
        this.errorMessage = value;
    }

}
