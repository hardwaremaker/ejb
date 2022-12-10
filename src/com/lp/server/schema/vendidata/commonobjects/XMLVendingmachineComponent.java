//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer VendingmachineComponent complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VendingmachineComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RowId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="InventoryId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AssetTracked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Class" type="{http://www.vendidata.com/XML/Schema/CommonObjects}VendingmachineComponentClass"/>
 *         &lt;element name="Vendor" type="{http://www.vendidata.com/XML/Schema/CommonObjects}SupplierInformation" minOccurs="0"/>
 *         &lt;element name="Denomination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VendingmachineComponent", propOrder = {
    "rowId",
    "inventoryId",
    "serialNumber",
    "deviceId",
    "assetTracked",
    "clazz",
    "vendor",
    "denomination"
})
public class XMLVendingmachineComponent {

    @XmlElement(name = "RowId")
    protected int rowId;
    @XmlElement(name = "InventoryId")
    protected Integer inventoryId;
    @XmlElement(name = "SerialNumber")
    protected String serialNumber;
    @XmlElement(name = "DeviceId")
    protected String deviceId;
    @XmlElement(name = "AssetTracked")
    protected boolean assetTracked;
    @XmlElement(name = "Class", required = true)
    @XmlSchemaType(name = "string")
    protected XMLVendingmachineComponentClass clazz;
    @XmlElement(name = "Vendor")
    protected XMLSupplierInformation vendor;
    @XmlElement(name = "Denomination", required = true)
    protected String denomination;

    /**
     * Ruft den Wert der rowId-Eigenschaft ab.
     * 
     */
    public int getRowId() {
        return rowId;
    }

    /**
     * Legt den Wert der rowId-Eigenschaft fest.
     * 
     */
    public void setRowId(int value) {
        this.rowId = value;
    }

    /**
     * Ruft den Wert der inventoryId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInventoryId() {
        return inventoryId;
    }

    /**
     * Legt den Wert der inventoryId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInventoryId(Integer value) {
        this.inventoryId = value;
    }

    /**
     * Ruft den Wert der serialNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Legt den Wert der serialNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Ruft den Wert der deviceId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Legt den Wert der deviceId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceId(String value) {
        this.deviceId = value;
    }

    /**
     * Ruft den Wert der assetTracked-Eigenschaft ab.
     * 
     */
    public boolean isAssetTracked() {
        return assetTracked;
    }

    /**
     * Legt den Wert der assetTracked-Eigenschaft fest.
     * 
     */
    public void setAssetTracked(boolean value) {
        this.assetTracked = value;
    }

    /**
     * Ruft den Wert der clazz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLVendingmachineComponentClass }
     *     
     */
    public XMLVendingmachineComponentClass getClazz() {
        return clazz;
    }

    /**
     * Legt den Wert der clazz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLVendingmachineComponentClass }
     *     
     */
    public void setClazz(XMLVendingmachineComponentClass value) {
        this.clazz = value;
    }

    /**
     * Ruft den Wert der vendor-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLSupplierInformation }
     *     
     */
    public XMLSupplierInformation getVendor() {
        return vendor;
    }

    /**
     * Legt den Wert der vendor-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSupplierInformation }
     *     
     */
    public void setVendor(XMLSupplierInformation value) {
        this.vendor = value;
    }

    /**
     * Ruft den Wert der denomination-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * Legt den Wert der denomination-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomination(String value) {
        this.denomination = value;
    }

}
