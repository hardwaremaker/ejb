//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer Customer complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Customer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Matchcode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *               &lt;whiteSpace value="preserve"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;element name="CustomerId">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;minInclusive value="1"/>
 *                 &lt;maxInclusive value="2147483646"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="AutoGenerateCustomerId" type="{http://www.vendidata.com/XML/Schema/Customers}AutoGenerateId"/>
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
 *                 &lt;extension base="&lt;http://www.vendidata.com/XML/Schema/Customers>AutoGenerateId">
 *                   &lt;attribute name="updateIfExists" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
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
 *         &lt;element name="Postalcode" type="{http://www.vendidata.com/XML/Schema/Customers}Postalcode" minOccurs="0"/>
 *         &lt;element name="Group" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Language" type="{http://www.vendidata.com/XML/Schema/Customers}Language" minOccurs="0"/>
 *         &lt;element name="VatIdNumber" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Stand" type="{http://www.vendidata.com/XML/Schema/Customers}Stand"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Contact" type="{http://www.vendidata.com/XML/Schema/Customers}Contact"/>
 *         &lt;/sequence>
 *         &lt;element name="AdditionalFields" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFields" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isOperating" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="isCustomer" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="creationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="status" type="{http://www.vendidata.com/XML/Schema/Customers}Status" default="ACTIVE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Customer", propOrder = {
    "matchcode",
    "customerId",
    "autoGenerateCustomerId",
    "accountingId",
    "autoGenerateAccountingId",
    "name1",
    "name2",
    "name3",
    "street",
    "postalcode",
    "group",
    "language",
    "vatIdNumber",
    "phoneNumber",
    "mobilePhoneNumber",
    "faxNumber",
    "eMailAddress",
    "websiteAddress",
    "notes",
    "stand",
    "contact",
    "additionalFields"
})
public class XMLCustomer {

    @XmlElement(name = "Matchcode")
    protected String matchcode;
    @XmlElement(name = "CustomerId")
    protected Integer customerId;
    @XmlElement(name = "AutoGenerateCustomerId")
    @XmlSchemaType(name = "string")
    protected XMLAutoGenerateId autoGenerateCustomerId;
    @XmlElement(name = "AccountingId")
    protected Integer accountingId;
    @XmlElement(name = "AutoGenerateAccountingId")
    protected XMLCustomer.AutoGenerateAccountingId autoGenerateAccountingId;
    @XmlElement(name = "Name1", required = true)
    protected String name1;
    @XmlElement(name = "Name2")
    protected String name2;
    @XmlElement(name = "Name3")
    protected String name3;
    @XmlElement(name = "Street")
    protected String street;
    @XmlElement(name = "Postalcode")
    protected XMLPostalcode postalcode;
    @XmlElement(name = "Group")
    protected String group;
    @XmlElement(name = "Language")
    protected XMLLanguage language;
    @XmlElement(name = "VatIdNumber")
    protected String vatIdNumber;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "MobilePhoneNumber")
    protected String mobilePhoneNumber;
    @XmlElement(name = "FaxNumber")
    protected String faxNumber;
    @XmlElement(name = "EMailAddress")
    protected String eMailAddress;
    @XmlElement(name = "WebsiteAddress")
    protected String websiteAddress;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "Stand")
    protected List<XMLStand> stand;
    @XmlElement(name = "Contact")
    protected List<XMLContact> contact;
    @XmlElement(name = "AdditionalFields")
    protected XMLAdditionalFields additionalFields;
    @XmlAttribute(name = "isOperating", required = true)
    protected boolean isOperating;
    @XmlAttribute(name = "isCustomer", required = true)
    protected boolean isCustomer;
    @XmlAttribute(name = "creationDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    @XmlAttribute(name = "status")
    protected XMLStatus status;

    /**
     * Ruft den Wert der matchcode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchcode() {
        return matchcode;
    }

    /**
     * Legt den Wert der matchcode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchcode(String value) {
        this.matchcode = value;
    }

    /**
     * Ruft den Wert der customerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Legt den Wert der customerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCustomerId(Integer value) {
        this.customerId = value;
    }

    /**
     * Ruft den Wert der autoGenerateCustomerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAutoGenerateId }
     *     
     */
    public XMLAutoGenerateId getAutoGenerateCustomerId() {
        return autoGenerateCustomerId;
    }

    /**
     * Legt den Wert der autoGenerateCustomerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAutoGenerateId }
     *     
     */
    public void setAutoGenerateCustomerId(XMLAutoGenerateId value) {
        this.autoGenerateCustomerId = value;
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
     *     {@link XMLCustomer.AutoGenerateAccountingId }
     *     
     */
    public XMLCustomer.AutoGenerateAccountingId getAutoGenerateAccountingId() {
        return autoGenerateAccountingId;
    }

    /**
     * Legt den Wert der autoGenerateAccountingId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCustomer.AutoGenerateAccountingId }
     *     
     */
    public void setAutoGenerateAccountingId(XMLCustomer.AutoGenerateAccountingId value) {
        this.autoGenerateAccountingId = value;
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
     * Ruft den Wert der postalcode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLPostalcode }
     *     
     */
    public XMLPostalcode getPostalcode() {
        return postalcode;
    }

    /**
     * Legt den Wert der postalcode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLPostalcode }
     *     
     */
    public void setPostalcode(XMLPostalcode value) {
        this.postalcode = value;
    }

    /**
     * Ruft den Wert der group-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Legt den Wert der group-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
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
     * Gets the value of the stand property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stand property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLStand }
     * 
     * 
     */
    public List<XMLStand> getStand() {
        if (stand == null) {
            stand = new ArrayList<XMLStand>();
        }
        return this.stand;
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
     * {@link XMLContact }
     * 
     * 
     */
    public List<XMLContact> getContact() {
        if (contact == null) {
            contact = new ArrayList<XMLContact>();
        }
        return this.contact;
    }

    /**
     * Ruft den Wert der additionalFields-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFields }
     *     
     */
    public XMLAdditionalFields getAdditionalFields() {
        return additionalFields;
    }

    /**
     * Legt den Wert der additionalFields-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFields }
     *     
     */
    public void setAdditionalFields(XMLAdditionalFields value) {
        this.additionalFields = value;
    }

    /**
     * Ruft den Wert der isOperating-Eigenschaft ab.
     * 
     */
    public boolean isIsOperating() {
        return isOperating;
    }

    /**
     * Legt den Wert der isOperating-Eigenschaft fest.
     * 
     */
    public void setIsOperating(boolean value) {
        this.isOperating = value;
    }

    /**
     * Ruft den Wert der isCustomer-Eigenschaft ab.
     * 
     */
    public boolean isIsCustomer() {
        return isCustomer;
    }

    /**
     * Legt den Wert der isCustomer-Eigenschaft fest.
     * 
     */
    public void setIsCustomer(boolean value) {
        this.isCustomer = value;
    }

    /**
     * Ruft den Wert der creationDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * Legt den Wert der creationDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
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
     * <p>Java-Klasse fuer anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.vendidata.com/XML/Schema/Customers>AutoGenerateId">
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
