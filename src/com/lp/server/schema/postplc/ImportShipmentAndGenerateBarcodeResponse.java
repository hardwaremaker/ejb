
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
 *         &lt;element name="ImportShipmentAndGenerateBarcodeResult" type="{http://post.ondot.at}ArrayOfColloRow" minOccurs="0"/>
 *         &lt;element name="zplLabelData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pdfData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipmentDocuments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qrCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="code128" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "importShipmentAndGenerateBarcodeResult",
    "zplLabelData",
    "pdfData",
    "shipmentDocuments",
    "qrCode",
    "code128",
    "errorCode",
    "errorMessage"
})
@XmlRootElement(name = "ImportShipmentAndGenerateBarcodeResponse")
public class ImportShipmentAndGenerateBarcodeResponse {

    @XmlElementRef(name = "ImportShipmentAndGenerateBarcodeResult", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloRow> importShipmentAndGenerateBarcodeResult;
    @XmlElementRef(name = "zplLabelData", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> zplLabelData;
    @XmlElementRef(name = "pdfData", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> pdfData;
    @XmlElementRef(name = "shipmentDocuments", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> shipmentDocuments;
    @XmlElementRef(name = "qrCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> qrCode;
    @XmlElementRef(name = "code128", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> code128;
    @XmlElementRef(name = "errorCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorCode;
    @XmlElementRef(name = "errorMessage", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorMessage;

    /**
     * Ruft den Wert der importShipmentAndGenerateBarcodeResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloRow> getImportShipmentAndGenerateBarcodeResult() {
        return importShipmentAndGenerateBarcodeResult;
    }

    /**
     * Legt den Wert der importShipmentAndGenerateBarcodeResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public void setImportShipmentAndGenerateBarcodeResult(JAXBElement<ArrayOfColloRow> value) {
        this.importShipmentAndGenerateBarcodeResult = value;
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
     * Ruft den Wert der qrCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getQrCode() {
        return qrCode;
    }

    /**
     * Legt den Wert der qrCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setQrCode(JAXBElement<String> value) {
        this.qrCode = value;
    }

    /**
     * Ruft den Wert der code128-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCode128() {
        return code128;
    }

    /**
     * Legt den Wert der code128-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCode128(JAXBElement<String> value) {
        this.code128 = value;
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
