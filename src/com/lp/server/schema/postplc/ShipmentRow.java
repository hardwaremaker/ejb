
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für ShipmentRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ShipmentRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AlternativeReturnOrgUnitAddress" type="{http://post.ondot.at}AddressRow" minOccurs="0"/>
 *         &lt;element name="BusinessDocumentEntryList" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="ClientID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ColloList" type="{http://post.ondot.at}ArrayOfColloRow" minOccurs="0"/>
 *         &lt;element name="CostCenterThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomDataBit1" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CustomDataBit2" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CustomerProduct" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomsDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeliveryInstruction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeliveryServiceThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FeatureList" type="{http://post.ondot.at}ArrayOfAdditionalInformationRow" minOccurs="0"/>
 *         &lt;element name="MovementReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OURecipientAddress" type="{http://post.ondot.at}AddressRow" minOccurs="0"/>
 *         &lt;element name="OUShipperAddress" type="{http://post.ondot.at}AddressRow" minOccurs="0"/>
 *         &lt;element name="OUShipperReference1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OUShipperReference2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrgUnitGuid" type="{http://schemas.microsoft.com/2003/10/Serialization/}guid" minOccurs="0"/>
 *         &lt;element name="OrgUnitID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PrinterObject" type="{http://post.ondot.at}PrinterRow" minOccurs="0"/>
 *         &lt;element name="RefBarcodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReturnDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ReturnModeID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ReturnOptionID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ShipmentDocumentEntryList" type="{http://Core.Model}ArrayOfShipmentDocumentEntry" minOccurs="0"/>
 *         &lt;element name="ShippingDateTimeFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ShippingDateTimeTo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentRow", propOrder = {
    "alternativeReturnOrgUnitAddress",
    "businessDocumentEntryList",
    "clientID",
    "colloList",
    "costCenterThirdPartyID",
    "customDataBit1",
    "customDataBit2",
    "customerProduct",
    "customsDescription",
    "deliveryInstruction",
    "deliveryServiceThirdPartyID",
    "featureList",
    "movementReferenceNumber",
    "number",
    "ouRecipientAddress",
    "ouShipperAddress",
    "ouShipperReference1",
    "ouShipperReference2",
    "orgUnitGuid",
    "orgUnitID",
    "printerObject",
    "refBarcodeType",
    "returnDays",
    "returnModeID",
    "returnOptionID",
    "shipmentDocumentEntryList",
    "shippingDateTimeFrom",
    "shippingDateTimeTo"
})
@XmlRootElement
public class ShipmentRow {

    @XmlElementRef(name = "AlternativeReturnOrgUnitAddress", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<AddressRow> alternativeReturnOrgUnitAddress;
    @XmlElementRef(name = "BusinessDocumentEntryList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> businessDocumentEntryList;
    @XmlElement(name = "ClientID")
    protected Integer clientID;
    @XmlElementRef(name = "ColloList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloRow> colloList;
    @XmlElementRef(name = "CostCenterThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> costCenterThirdPartyID;
    @XmlElement(name = "CustomDataBit1")
    protected Boolean customDataBit1;
    @XmlElement(name = "CustomDataBit2")
    protected Boolean customDataBit2;
    @XmlElementRef(name = "CustomerProduct", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> customerProduct;
    @XmlElementRef(name = "CustomsDescription", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> customsDescription;
    @XmlElementRef(name = "DeliveryInstruction", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> deliveryInstruction;
    @XmlElementRef(name = "DeliveryServiceThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> deliveryServiceThirdPartyID;
    @XmlElementRef(name = "FeatureList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfAdditionalInformationRow> featureList;
    @XmlElementRef(name = "MovementReferenceNumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> movementReferenceNumber;
    @XmlElementRef(name = "Number", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> number;
    @XmlElementRef(name = "OURecipientAddress", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<AddressRow> ouRecipientAddress;
    @XmlElementRef(name = "OUShipperAddress", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<AddressRow> ouShipperAddress;
    @XmlElementRef(name = "OUShipperReference1", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> ouShipperReference1;
    @XmlElementRef(name = "OUShipperReference2", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> ouShipperReference2;
    @XmlElement(name = "OrgUnitGuid")
    protected String orgUnitGuid;
    @XmlElement(name = "OrgUnitID")
    protected Integer orgUnitID;
    @XmlElementRef(name = "PrinterObject", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<PrinterRow> printerObject;
    @XmlElementRef(name = "RefBarcodeType", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> refBarcodeType;
    @XmlElementRef(name = "ReturnDays", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> returnDays;
    @XmlElementRef(name = "ReturnModeID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> returnModeID;
    @XmlElementRef(name = "ReturnOptionID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> returnOptionID;
    @XmlElementRef(name = "ShipmentDocumentEntryList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfShipmentDocumentEntry> shipmentDocumentEntryList;
    @XmlElementRef(name = "ShippingDateTimeFrom", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> shippingDateTimeFrom;
    @XmlElementRef(name = "ShippingDateTimeTo", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> shippingDateTimeTo;

    /**
     * Ruft den Wert der alternativeReturnOrgUnitAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public JAXBElement<AddressRow> getAlternativeReturnOrgUnitAddress() {
        return alternativeReturnOrgUnitAddress;
    }

    /**
     * Legt den Wert der alternativeReturnOrgUnitAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public void setAlternativeReturnOrgUnitAddress(JAXBElement<AddressRow> value) {
        this.alternativeReturnOrgUnitAddress = value;
    }

    /**
     * Ruft den Wert der businessDocumentEntryList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getBusinessDocumentEntryList() {
        return businessDocumentEntryList;
    }

    /**
     * Legt den Wert der businessDocumentEntryList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setBusinessDocumentEntryList(JAXBElement<ArrayOfstring> value) {
        this.businessDocumentEntryList = value;
    }

    /**
     * Ruft den Wert der clientID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getClientID() {
        return clientID;
    }

    /**
     * Legt den Wert der clientID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setClientID(Integer value) {
        this.clientID = value;
    }

    /**
     * Ruft den Wert der colloList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloRow> getColloList() {
        return colloList;
    }

    /**
     * Legt den Wert der colloList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}
     *     
     */
    public void setColloList(JAXBElement<ArrayOfColloRow> value) {
        this.colloList = value;
    }

    /**
     * Ruft den Wert der costCenterThirdPartyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCostCenterThirdPartyID() {
        return costCenterThirdPartyID;
    }

    /**
     * Legt den Wert der costCenterThirdPartyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCostCenterThirdPartyID(JAXBElement<String> value) {
        this.costCenterThirdPartyID = value;
    }

    /**
     * Ruft den Wert der customDataBit1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCustomDataBit1() {
        return customDataBit1;
    }

    /**
     * Legt den Wert der customDataBit1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCustomDataBit1(Boolean value) {
        this.customDataBit1 = value;
    }

    /**
     * Ruft den Wert der customDataBit2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCustomDataBit2() {
        return customDataBit2;
    }

    /**
     * Legt den Wert der customDataBit2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCustomDataBit2(Boolean value) {
        this.customDataBit2 = value;
    }

    /**
     * Ruft den Wert der customerProduct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerProduct() {
        return customerProduct;
    }

    /**
     * Legt den Wert der customerProduct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerProduct(JAXBElement<String> value) {
        this.customerProduct = value;
    }

    /**
     * Ruft den Wert der customsDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomsDescription() {
        return customsDescription;
    }

    /**
     * Legt den Wert der customsDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomsDescription(JAXBElement<String> value) {
        this.customsDescription = value;
    }

    /**
     * Ruft den Wert der deliveryInstruction-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDeliveryInstruction() {
        return deliveryInstruction;
    }

    /**
     * Legt den Wert der deliveryInstruction-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDeliveryInstruction(JAXBElement<String> value) {
        this.deliveryInstruction = value;
    }

    /**
     * Ruft den Wert der deliveryServiceThirdPartyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDeliveryServiceThirdPartyID() {
        return deliveryServiceThirdPartyID;
    }

    /**
     * Legt den Wert der deliveryServiceThirdPartyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDeliveryServiceThirdPartyID(JAXBElement<String> value) {
        this.deliveryServiceThirdPartyID = value;
    }

    /**
     * Ruft den Wert der featureList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfAdditionalInformationRow> getFeatureList() {
        return featureList;
    }

    /**
     * Legt den Wert der featureList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationRow }{@code >}
     *     
     */
    public void setFeatureList(JAXBElement<ArrayOfAdditionalInformationRow> value) {
        this.featureList = value;
    }

    /**
     * Ruft den Wert der movementReferenceNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMovementReferenceNumber() {
        return movementReferenceNumber;
    }

    /**
     * Legt den Wert der movementReferenceNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMovementReferenceNumber(JAXBElement<String> value) {
        this.movementReferenceNumber = value;
    }

    /**
     * Ruft den Wert der number-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumber() {
        return number;
    }

    /**
     * Legt den Wert der number-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumber(JAXBElement<String> value) {
        this.number = value;
    }

    /**
     * Ruft den Wert der ouRecipientAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public JAXBElement<AddressRow> getOURecipientAddress() {
        return ouRecipientAddress;
    }

    /**
     * Legt den Wert der ouRecipientAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public void setOURecipientAddress(JAXBElement<AddressRow> value) {
        this.ouRecipientAddress = value;
    }

    /**
     * Ruft den Wert der ouShipperAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public JAXBElement<AddressRow> getOUShipperAddress() {
        return ouShipperAddress;
    }

    /**
     * Legt den Wert der ouShipperAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AddressRow }{@code >}
     *     
     */
    public void setOUShipperAddress(JAXBElement<AddressRow> value) {
        this.ouShipperAddress = value;
    }

    /**
     * Ruft den Wert der ouShipperReference1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOUShipperReference1() {
        return ouShipperReference1;
    }

    /**
     * Legt den Wert der ouShipperReference1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOUShipperReference1(JAXBElement<String> value) {
        this.ouShipperReference1 = value;
    }

    /**
     * Ruft den Wert der ouShipperReference2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOUShipperReference2() {
        return ouShipperReference2;
    }

    /**
     * Legt den Wert der ouShipperReference2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOUShipperReference2(JAXBElement<String> value) {
        this.ouShipperReference2 = value;
    }

    /**
     * Ruft den Wert der orgUnitGuid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgUnitGuid() {
        return orgUnitGuid;
    }

    /**
     * Legt den Wert der orgUnitGuid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgUnitGuid(String value) {
        this.orgUnitGuid = value;
    }

    /**
     * Ruft den Wert der orgUnitID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrgUnitID() {
        return orgUnitID;
    }

    /**
     * Legt den Wert der orgUnitID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrgUnitID(Integer value) {
        this.orgUnitID = value;
    }

    /**
     * Ruft den Wert der printerObject-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PrinterRow }{@code >}
     *     
     */
    public JAXBElement<PrinterRow> getPrinterObject() {
        return printerObject;
    }

    /**
     * Legt den Wert der printerObject-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PrinterRow }{@code >}
     *     
     */
    public void setPrinterObject(JAXBElement<PrinterRow> value) {
        this.printerObject = value;
    }

    /**
     * Ruft den Wert der refBarcodeType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRefBarcodeType() {
        return refBarcodeType;
    }

    /**
     * Legt den Wert der refBarcodeType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRefBarcodeType(JAXBElement<String> value) {
        this.refBarcodeType = value;
    }

    /**
     * Ruft den Wert der returnDays-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getReturnDays() {
        return returnDays;
    }

    /**
     * Legt den Wert der returnDays-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setReturnDays(JAXBElement<Integer> value) {
        this.returnDays = value;
    }

    /**
     * Ruft den Wert der returnModeID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getReturnModeID() {
        return returnModeID;
    }

    /**
     * Legt den Wert der returnModeID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setReturnModeID(JAXBElement<Integer> value) {
        this.returnModeID = value;
    }

    /**
     * Ruft den Wert der returnOptionID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getReturnOptionID() {
        return returnOptionID;
    }

    /**
     * Legt den Wert der returnOptionID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setReturnOptionID(JAXBElement<Integer> value) {
        this.returnOptionID = value;
    }

    /**
     * Ruft den Wert der shipmentDocumentEntryList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfShipmentDocumentEntry }{@code >}
     *     
     */
    public JAXBElement<ArrayOfShipmentDocumentEntry> getShipmentDocumentEntryList() {
        return shipmentDocumentEntryList;
    }

    /**
     * Legt den Wert der shipmentDocumentEntryList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfShipmentDocumentEntry }{@code >}
     *     
     */
    public void setShipmentDocumentEntryList(JAXBElement<ArrayOfShipmentDocumentEntry> value) {
        this.shipmentDocumentEntryList = value;
    }

    /**
     * Ruft den Wert der shippingDateTimeFrom-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getShippingDateTimeFrom() {
        return shippingDateTimeFrom;
    }

    /**
     * Legt den Wert der shippingDateTimeFrom-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setShippingDateTimeFrom(JAXBElement<XMLGregorianCalendar> value) {
        this.shippingDateTimeFrom = value;
    }

    /**
     * Ruft den Wert der shippingDateTimeTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getShippingDateTimeTo() {
        return shippingDateTimeTo;
    }

    /**
     * Legt den Wert der shippingDateTimeTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setShippingDateTimeTo(JAXBElement<XMLGregorianCalendar> value) {
        this.shippingDateTimeTo = value;
    }

}
