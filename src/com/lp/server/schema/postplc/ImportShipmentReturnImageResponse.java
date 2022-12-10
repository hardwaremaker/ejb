
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
 *         &lt;element name="ImportShipmentReturnImageResult" type="{http://post.ondot.at}ArrayOfColloRow" minOccurs="0"/>
 *         &lt;element name="imageData" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
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
    "importShipmentReturnImageResult",
    "imageData",
    "shipmentDocuments",
    "errorCode",
    "errorMessage"
})
@XmlRootElement(name = "ImportShipmentReturnImageResponse")
public class ImportShipmentReturnImageResponse {

    @XmlElementRef(name = "ImportShipmentReturnImageResult", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloRow> importShipmentReturnImageResult;
    @XmlElementRef(name = "imageData", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> imageData;
    @XmlElementRef(name = "shipmentDocuments", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> shipmentDocuments;
    @XmlElementRef(name = "errorCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorCode;
    @XmlElementRef(name = "errorMessage", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> errorMessage;

    /**
     * Ruft den Wert der importShipmentReturnImageResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloRow> getImportShipmentReturnImageResult() {
        return importShipmentReturnImageResult;
    }

    /**
     * Legt den Wert der importShipmentReturnImageResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public void setImportShipmentReturnImageResult(JAXBElement<ArrayOfColloRow> value) {
        this.importShipmentReturnImageResult = value;
    }

    /**
     * Ruft den Wert der imageData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getImageData() {
        return imageData;
    }

    /**
     * Legt den Wert der imageData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setImageData(JAXBElement<ArrayOfstring> value) {
        this.imageData = value;
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
