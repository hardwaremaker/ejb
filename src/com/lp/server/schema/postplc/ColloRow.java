
package com.lp.server.schema.postplc;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ColloRow complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ColloRow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ColloArticleList" type="{http://post.ondot.at}ArrayOfColloArticleRow" minOccurs="0"/>
 *         &lt;element name="ColloCodeList" type="{http://post.ondot.at}ArrayOfColloCodeRow" minOccurs="0"/>
 *         &lt;element name="Height" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Width" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColloRow", propOrder = {
    "colloArticleList",
    "colloCodeList",
    "height",
    "length",
    "weight",
    "width"
})
public class ColloRow {

    @XmlElementRef(name = "ColloArticleList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloArticleRow> colloArticleList;
    @XmlElementRef(name = "ColloCodeList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfColloCodeRow> colloCodeList;
    @XmlElementRef(name = "Height", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> height;
    @XmlElementRef(name = "Length", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> length;
    @XmlElementRef(name = "Weight", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<BigDecimal> weight;
    @XmlElementRef(name = "Width", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<Integer> width;

    /**
     * Ruft den Wert der colloArticleList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloArticleRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloArticleRow> getColloArticleList() {
        return colloArticleList;
    }

    /**
     * Legt den Wert der colloArticleList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloArticleRow }{@code >}
     *     
     */
    public void setColloArticleList(JAXBElement<ArrayOfColloArticleRow> value) {
        this.colloArticleList = value;
    }

    /**
     * Ruft den Wert der colloCodeList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloCodeRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfColloCodeRow> getColloCodeList() {
        return colloCodeList;
    }

    /**
     * Legt den Wert der colloCodeList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfColloCodeRow }{@code >}
     *     
     */
    public void setColloCodeList(JAXBElement<ArrayOfColloCodeRow> value) {
        this.colloCodeList = value;
    }

    /**
     * Ruft den Wert der height-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getHeight() {
        return height;
    }

    /**
     * Legt den Wert der height-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setHeight(JAXBElement<Integer> value) {
        this.height = value;
    }

    /**
     * Ruft den Wert der length-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getLength() {
        return length;
    }

    /**
     * Legt den Wert der length-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setLength(JAXBElement<Integer> value) {
        this.length = value;
    }

    /**
     * Ruft den Wert der weight-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public JAXBElement<BigDecimal> getWeight() {
        return weight;
    }

    /**
     * Legt den Wert der weight-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     *     
     */
    public void setWeight(JAXBElement<BigDecimal> value) {
        this.weight = value;
    }

    /**
     * Ruft den Wert der width-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getWidth() {
        return width;
    }

    /**
     * Legt den Wert der width-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setWidth(JAXBElement<Integer> value) {
        this.width = value;
    }

}
