
package com.lp.server.schema.postplc;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ColloArticleRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ColloArticleRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ArticleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ArticleNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConsumerUnitNetWeight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CountryOfOriginID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrencyID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomsOptionID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="HSTariffNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueOfGoodsPerUnit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColloArticleRow", propOrder = {
    "articleName",
    "articleNumber",
    "consumerUnitNetWeight",
    "countryOfOriginID",
    "currencyID",
    "customsOptionID",
    "hsTariffNumber",
    "quantity",
    "unitID",
    "valueOfGoodsPerUnit"
})
public class ColloArticleRow {

    @XmlElementRef(name = "ArticleName", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> articleName;
    @XmlElementRef(name = "ArticleNumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> articleNumber;
    @XmlElementRef(name = "ConsumerUnitNetWeight", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> consumerUnitNetWeight;
    @XmlElementRef(name = "CountryOfOriginID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> countryOfOriginID;
    @XmlElementRef(name = "CurrencyID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> currencyID;
    @XmlElementRef(name = "CustomsOptionID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> customsOptionID;
    @XmlElementRef(name = "HSTariffNumber", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> hsTariffNumber;
    @XmlElementRef(name = "Quantity", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> quantity;
    @XmlElementRef(name = "UnitID", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> unitID;
    @XmlElementRef(name = "ValueOfGoodsPerUnit", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> valueOfGoodsPerUnit;

    /**
     * Ruft den Wert der articleName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getArticleName() {
        return articleName;
    }

    /**
     * Legt den Wert der articleName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setArticleName(JAXBElement<String> value) {
        this.articleName = value;
    }

    /**
     * Ruft den Wert der articleNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getArticleNumber() {
        return articleNumber;
    }

    /**
     * Legt den Wert der articleNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setArticleNumber(JAXBElement<String> value) {
        this.articleNumber = value;
    }

    /**
     * Ruft den Wert der consumerUnitNetWeight-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getConsumerUnitNetWeight() {
        return consumerUnitNetWeight;
    }

    /**
     * Legt den Wert der consumerUnitNetWeight-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setConsumerUnitNetWeight(JAXBElement<BigDecimal> value) {
        this.consumerUnitNetWeight = value;
    }

    /**
     * Ruft den Wert der countryOfOriginID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCountryOfOriginID() {
        return countryOfOriginID;
    }

    /**
     * Legt den Wert der countryOfOriginID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCountryOfOriginID(JAXBElement<String> value) {
        this.countryOfOriginID = value;
    }

    /**
     * Ruft den Wert der currencyID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCurrencyID() {
        return currencyID;
    }

    /**
     * Legt den Wert der currencyID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCurrencyID(JAXBElement<String> value) {
        this.currencyID = value;
    }

    /**
     * Ruft den Wert der customsOptionID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getCustomsOptionID() {
        return customsOptionID;
    }

    /**
     * Legt den Wert der customsOptionID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setCustomsOptionID(JAXBElement<Integer> value) {
        this.customsOptionID = value;
    }

    /**
     * Ruft den Wert der hsTariffNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getHSTariffNumber() {
        return hsTariffNumber;
    }

    /**
     * Legt den Wert der hsTariffNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setHSTariffNumber(JAXBElement<String> value) {
        this.hsTariffNumber = value;
    }

    /**
     * Ruft den Wert der quantity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getQuantity() {
        return quantity;
    }

    /**
     * Legt den Wert der quantity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setQuantity(JAXBElement<BigDecimal> value) {
        this.quantity = value;
    }

    /**
     * Ruft den Wert der unitID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUnitID() {
        return unitID;
    }

    /**
     * Legt den Wert der unitID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUnitID(JAXBElement<String> value) {
        this.unitID = value;
    }

    /**
     * Ruft den Wert der valueOfGoodsPerUnit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getValueOfGoodsPerUnit() {
        return valueOfGoodsPerUnit;
    }

    /**
     * Legt den Wert der valueOfGoodsPerUnit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setValueOfGoodsPerUnit(JAXBElement<BigDecimal> value) {
        this.valueOfGoodsPerUnit = value;
    }

}
