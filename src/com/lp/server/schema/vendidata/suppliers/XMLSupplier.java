//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:22:17 AM CEST 
//


package com.lp.server.schema.vendidata.suppliers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import com.lp.server.schema.vendidata.commonobjects.XMLAutoGenerateId;
import com.lp.server.schema.vendidata.commonobjects.XMLLanguage;
import com.lp.server.schema.vendidata.commonobjects.XMLPostalcode;
import com.lp.server.schema.vendidata.commonobjects.XMLStatus;


/**
 * <p>Java-Klasse fuer Supplier complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Supplier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="SupplierId">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;minInclusive value="1"/>
 *                 &lt;maxInclusive value="2147483646"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="AutoGenerateSupplierId" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AutoGenerateId"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="AccountingId">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;minInclusive value="1"/>
 *                 &lt;maxInclusive value="2147483646"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="AutoGenerateAccountingId">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.vendidata.com/XML/Schema/CommonObjects>AutoGenerateId">
 *                   &lt;attribute name="updateIfExists" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="VatIdNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Name1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Name2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Name3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Street" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PostalCode" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Postalcode" minOccurs="0"/>
 *         &lt;element name="PhoneNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MobilePhoneNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FaxNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EMailAddress" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="WebsiteAddress" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Language" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Language" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Contact" type="{http://www.vendidata.com/XML/Schema/Suppliers}SupplierContact" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="rowId">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="isVendor" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="isSupplier" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="status" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Status" default="ACTIVE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Supplier", propOrder = {
    "supplierId",
    "autoGenerateSupplierId",
    "accountingId",
    "autoGenerateAccountingId",
    "vatIdNumber",
    "name1",
    "name2",
    "name3",
    "street",
    "postalCode",
    "phoneNumber",
    "mobilePhoneNumber",
    "faxNumber",
    "notes",
    "eMailAddress",
    "websiteAddress",
    "language",
    "contact"
})
public class XMLSupplier {

    @XmlElement(name = "SupplierId")
    protected Integer supplierId;
    @XmlElement(name = "AutoGenerateSupplierId")
    @XmlSchemaType(name = "string")
    protected XMLAutoGenerateId autoGenerateSupplierId;
    @XmlElement(name = "AccountingId")
    protected Integer accountingId;
    @XmlElement(name = "AutoGenerateAccountingId")
    protected XMLSupplier.AutoGenerateAccountingId autoGenerateAccountingId;
    @XmlElement(name = "VatIdNumber")
    protected String vatIdNumber;
    @XmlElement(name = "Name1", required = true)
    protected String name1;
    @XmlElement(name = "Name2")
    protected String name2;
    @XmlElement(name = "Name3")
    protected String name3;
    @XmlElement(name = "Street")
    protected String street;
    @XmlElement(name = "PostalCode")
    protected XMLPostalcode postalCode;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "MobilePhoneNumber")
    protected String mobilePhoneNumber;
    @XmlElement(name = "FaxNumber")
    protected String faxNumber;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "EMailAddress")
    protected String eMailAddress;
    @XmlElement(name = "WebsiteAddress")
    protected String websiteAddress;
    @XmlElement(name = "Language")
    protected XMLLanguage language;
    @XmlElement(name = "Contact")
    protected List<XMLSupplierContact> contact;
    @XmlAttribute(name = "rowId")
    protected Integer rowId;
    @XmlAttribute(name = "isVendor", required = true)
    protected boolean isVendor;
    @XmlAttribute(name = "isSupplier", required = true)
    protected boolean isSupplier;
    @XmlAttribute(name = "status")
    protected XMLStatus status;

    /**
     * Ruft den Wert der supplierId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * Legt den Wert der supplierId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSupplierId(Integer value) {
        this.supplierId = value;
    }

    /**
     * Ruft den Wert der autoGenerateSupplierId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAutoGenerateId }
     *     
     */
    public XMLAutoGenerateId getAutoGenerateSupplierId() {
        return autoGenerateSupplierId;
    }

    /**
     * Legt den Wert der autoGenerateSupplierId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAutoGenerateId }
     *     
     */
    public void setAutoGenerateSupplierId(XMLAutoGenerateId value) {
        this.autoGenerateSupplierId = value;
    }

    /**
     * Ruft den Wert der accountingId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAccountingId() {
        return accountingId;
    }

    /**
     * Legt den Wert der accountingId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAccountingId(Integer value) {
        this.accountingId = value;
    }

    /**
     * Ruft den Wert der autoGenerateAccountingId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLSupplier.AutoGenerateAccountingId }
     *     
     */
    public XMLSupplier.AutoGenerateAccountingId getAutoGenerateAccountingId() {
        return autoGenerateAccountingId;
    }

    /**
     * Legt den Wert der autoGenerateAccountingId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSupplier.AutoGenerateAccountingId }
     *     
     */
    public void setAutoGenerateAccountingId(XMLSupplier.AutoGenerateAccountingId value) {
        this.autoGenerateAccountingId = value;
    }

    /**
     * Ruft den Wert der vatIdNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVatIdNumber() {
        return vatIdNumber;
    }

    /**
     * Legt den Wert der vatIdNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVatIdNumber(String value) {
        this.vatIdNumber = value;
    }

    /**
     * Ruft den Wert der name1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName1() {
        return name1;
    }

    /**
     * Legt den Wert der name1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName1(String value) {
        this.name1 = value;
    }

    /**
     * Ruft den Wert der name2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName2() {
        return name2;
    }

    /**
     * Legt den Wert der name2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName2(String value) {
        this.name2 = value;
    }

    /**
     * Ruft den Wert der name3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName3() {
        return name3;
    }

    /**
     * Legt den Wert der name3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName3(String value) {
        this.name3 = value;
    }

    /**
     * Ruft den Wert der street-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * Legt den Wert der street-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Ruft den Wert der postalCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLPostalcode }
     *     
     */
    public XMLPostalcode getPostalCode() {
        return postalCode;
    }

    /**
     * Legt den Wert der postalCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLPostalcode }
     *     
     */
    public void setPostalCode(XMLPostalcode value) {
        this.postalCode = value;
    }

    /**
     * Ruft den Wert der phoneNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Legt den Wert der phoneNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    /**
     * Ruft den Wert der mobilePhoneNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    /**
     * Legt den Wert der mobilePhoneNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhoneNumber(String value) {
        this.mobilePhoneNumber = value;
    }

    /**
     * Ruft den Wert der faxNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Legt den Wert der faxNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNumber(String value) {
        this.faxNumber = value;
    }

    /**
     * Ruft den Wert der notes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Legt den Wert der notes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Ruft den Wert der eMailAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * Legt den Wert der eMailAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMailAddress(String value) {
        this.eMailAddress = value;
    }

    /**
     * Ruft den Wert der websiteAddress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsiteAddress() {
        return websiteAddress;
    }

    /**
     * Legt den Wert der websiteAddress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsiteAddress(String value) {
        this.websiteAddress = value;
    }

    /**
     * Ruft den Wert der language-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLLanguage }
     *     
     */
    public XMLLanguage getLanguage() {
        return language;
    }

    /**
     * Legt den Wert der language-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLLanguage }
     *     
     */
    public void setLanguage(XMLLanguage value) {
        this.language = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLSupplierContact }
     * 
     * 
     */
    public List<XMLSupplierContact> getContact() {
        if (contact == null) {
            contact = new ArrayList<XMLSupplierContact>();
        }
        return this.contact;
    }

    /**
     * Ruft den Wert der rowId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRowId() {
        return rowId;
    }

    /**
     * Legt den Wert der rowId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRowId(Integer value) {
        this.rowId = value;
    }

    /**
     * Ruft den Wert der isVendor-Eigenschaft ab.
     * 
     */
    public boolean isIsVendor() {
        return isVendor;
    }

    /**
     * Legt den Wert der isVendor-Eigenschaft fest.
     * 
     */
    public void setIsVendor(boolean value) {
        this.isVendor = value;
    }

    /**
     * Ruft den Wert der isSupplier-Eigenschaft ab.
     * 
     */
    public boolean isIsSupplier() {
        return isSupplier;
    }

    /**
     * Legt den Wert der isSupplier-Eigenschaft fest.
     * 
     */
    public void setIsSupplier(boolean value) {
        this.isSupplier = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLStatus }
     *     
     */
    public XMLStatus getStatus() {
        if (status == null) {
            return XMLStatus.ACTIVE;
        } else {
            return status;
        }
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLStatus }
     *     
     */
    public void setStatus(XMLStatus value) {
        this.status = value;
    }


    /**
     * <p>Java-Klasse f�r anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.vendidata.com/XML/Schema/CommonObjects>AutoGenerateId">
     *       &lt;attribute name="updateIfExists" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class AutoGenerateAccountingId {

        @XmlValue
        protected XMLAutoGenerateId value;
        @XmlAttribute(name = "updateIfExists")
        protected Boolean updateIfExists;

        /**
         * Ruft den Wert der value-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link XMLAutoGenerateId }
         *     
         */
        public XMLAutoGenerateId getValue() {
            return value;
        }

        /**
         * Legt den Wert der value-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLAutoGenerateId }
         *     
         */
        public void setValue(XMLAutoGenerateId value) {
            this.value = value;
        }

        /**
         * Ruft den Wert der updateIfExists-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isUpdateIfExists() {
            if (updateIfExists == null) {
                return false;
            } else {
                return updateIfExists;
            }
        }

        /**
         * Legt den Wert der updateIfExists-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setUpdateIfExists(Boolean value) {
            this.updateIfExists = value;
        }

    }

}
