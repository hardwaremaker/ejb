
package com.lp.server.schema.postplc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AddressRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AddressRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EORINumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Homepage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HouseNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PersonalTaxNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProvinceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tel1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tel2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ThirdPartyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VATID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressRow", propOrder = {
    "addressLine1",
    "addressLine2",
    "city",
    "countryID",
    "eoriNumber",
    "email",
    "fax",
    "homepage",
    "houseNumber",
    "name1",
    "name2",
    "name3",
    "name4",
    "personalTaxNumber",
    "postalCode",
    "provinceCode",
    "tel1",
    "tel2",
    "thirdPartyID",
    "vatid"
})
public class AddressRow {

    @XmlElementRef(name = "AddressLine1", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> addressLine1;
    @XmlElementRef(name = "AddressLine2", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> addressLine2;
    @XmlElementRef(name = "City", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> city;
    @XmlElementRef(name = "CountryID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> countryID;
    @XmlElementRef(name = "EORINumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> eoriNumber;
    @XmlElementRef(name = "Email", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> email;
    @XmlElementRef(name = "Fax", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> fax;
    @XmlElementRef(name = "Homepage", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> homepage;
    @XmlElementRef(name = "HouseNumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> houseNumber;
    @XmlElementRef(name = "Name1", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name1;
    @XmlElementRef(name = "Name2", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name2;
    @XmlElementRef(name = "Name3", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name3;
    @XmlElementRef(name = "Name4", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> name4;
    @XmlElementRef(name = "PersonalTaxNumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> personalTaxNumber;
    @XmlElementRef(name = "PostalCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> postalCode;
    @XmlElementRef(name = "ProvinceCode", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> provinceCode;
    @XmlElementRef(name = "Tel1", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> tel1;
    @XmlElementRef(name = "Tel2", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> tel2;
    @XmlElementRef(name = "ThirdPartyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> thirdPartyID;
    @XmlElementRef(name = "VATID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> vatid;

    /**
     * Ruft den Wert der addressLine1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAddressLine1() {
        return addressLine1;
    }

    /**
     * Legt den Wert der addressLine1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAddressLine1(JAXBElement<String> value) {
        this.addressLine1 = value;
    }

    /**
     * Ruft den Wert der addressLine2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAddressLine2() {
        return addressLine2;
    }

    /**
     * Legt den Wert der addressLine2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAddressLine2(JAXBElement<String> value) {
        this.addressLine2 = value;
    }

    /**
     * Ruft den Wert der city-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCity() {
        return city;
    }

    /**
     * Legt den Wert der city-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCity(JAXBElement<String> value) {
        this.city = value;
    }

    /**
     * Ruft den Wert der countryID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCountryID() {
        return countryID;
    }

    /**
     * Legt den Wert der countryID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCountryID(JAXBElement<String> value) {
        this.countryID = value;
    }

    /**
     * Ruft den Wert der eoriNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEORINumber() {
        return eoriNumber;
    }

    /**
     * Legt den Wert der eoriNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEORINumber(JAXBElement<String> value) {
        this.eoriNumber = value;
    }

    /**
     * Ruft den Wert der email-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * Legt den Wert der email-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = value;
    }

    /**
     * Ruft den Wert der fax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFax() {
        return fax;
    }

    /**
     * Legt den Wert der fax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFax(JAXBElement<String> value) {
        this.fax = value;
    }

    /**
     * Ruft den Wert der homepage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getHomepage() {
        return homepage;
    }

    /**
     * Legt den Wert der homepage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setHomepage(JAXBElement<String> value) {
        this.homepage = value;
    }

    /**
     * Ruft den Wert der houseNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getHouseNumber() {
        return houseNumber;
    }

    /**
     * Legt den Wert der houseNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setHouseNumber(JAXBElement<String> value) {
        this.houseNumber = value;
    }

    /**
     * Ruft den Wert der name1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName1() {
        return name1;
    }

    /**
     * Legt den Wert der name1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName1(JAXBElement<String> value) {
        this.name1 = value;
    }

    /**
     * Ruft den Wert der name2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName2() {
        return name2;
    }

    /**
     * Legt den Wert der name2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName2(JAXBElement<String> value) {
        this.name2 = value;
    }

    /**
     * Ruft den Wert der name3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName3() {
        return name3;
    }

    /**
     * Legt den Wert der name3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName3(JAXBElement<String> value) {
        this.name3 = value;
    }

    /**
     * Ruft den Wert der name4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName4() {
        return name4;
    }

    /**
     * Legt den Wert der name4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName4(JAXBElement<String> value) {
        this.name4 = value;
    }

    /**
     * Ruft den Wert der personalTaxNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPersonalTaxNumber() {
        return personalTaxNumber;
    }

    /**
     * Legt den Wert der personalTaxNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPersonalTaxNumber(JAXBElement<String> value) {
        this.personalTaxNumber = value;
    }

    /**
     * Ruft den Wert der postalCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPostalCode() {
        return postalCode;
    }

    /**
     * Legt den Wert der postalCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPostalCode(JAXBElement<String> value) {
        this.postalCode = value;
    }

    /**
     * Ruft den Wert der provinceCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProvinceCode() {
        return provinceCode;
    }

    /**
     * Legt den Wert der provinceCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProvinceCode(JAXBElement<String> value) {
        this.provinceCode = value;
    }

    /**
     * Ruft den Wert der tel1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTel1() {
        return tel1;
    }

    /**
     * Legt den Wert der tel1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTel1(JAXBElement<String> value) {
        this.tel1 = value;
    }

    /**
     * Ruft den Wert der tel2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTel2() {
        return tel2;
    }

    /**
     * Legt den Wert der tel2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTel2(JAXBElement<String> value) {
        this.tel2 = value;
    }

    /**
     * Ruft den Wert der thirdPartyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getThirdPartyID() {
        return thirdPartyID;
    }

    /**
     * Legt den Wert der thirdPartyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setThirdPartyID(JAXBElement<String> value) {
        this.thirdPartyID = value;
    }

    /**
     * Ruft den Wert der vatid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVATID() {
        return vatid;
    }

    /**
     * Legt den Wert der vatid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVATID(JAXBElement<String> value) {
        this.vatid = value;
    }

}
