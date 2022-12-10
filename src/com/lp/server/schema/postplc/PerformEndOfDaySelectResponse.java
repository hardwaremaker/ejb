
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
 *         &lt;element name="PerformEndOfDaySelectResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorList" type="{http://post.ondot.at}ArrayOfErrorRow" minOccurs="0"/>
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
    "performEndOfDaySelectResult",
    "errorList"
})
@XmlRootElement(name = "PerformEndOfDaySelectResponse")
public class PerformEndOfDaySelectResponse {

    @XmlElementRef(name = "PerformEndOfDaySelectResult", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<String> performEndOfDaySelectResult;
    @XmlElementRef(name = "errorList", namespace = "http://post.ondot.at", type = JAXBElement.class)
    protected JAXBElement<ArrayOfErrorRow> errorList;

    /**
     * Ruft den Wert der performEndOfDaySelectResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPerformEndOfDaySelectResult() {
        return performEndOfDaySelectResult;
    }

    /**
     * Legt den Wert der performEndOfDaySelectResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPerformEndOfDaySelectResult(JAXBElement<String> value) {
        this.performEndOfDaySelectResult = value;
    }

    /**
     * Ruft den Wert der errorList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErrorRow }{@code >}
     *     
     */
    public JAXBElement<ArrayOfErrorRow> getErrorList() {
        return errorList;
    }

    /**
     * Legt den Wert der errorList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErrorRow }{@code >}
     *     
     */
    public void setErrorList(JAXBElement<ArrayOfErrorRow> value) {
        this.errorList = value;
    }

}
