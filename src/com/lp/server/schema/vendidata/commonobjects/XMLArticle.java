//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Article complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Article">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RowId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="BaseArticleData" type="{http://www.vendidata.com/XML/Schema/CommonObjects}BaseArticleData"/>
 *           &lt;element name="SalesArticleData" type="{http://www.vendidata.com/XML/Schema/CommonObjects}SalesArticleData"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Article", propOrder = {
    "rowId",
    "baseArticleDataOrSalesArticleData"
})
public class XMLArticle {

    @XmlElement(name = "RowId")
    protected int rowId;
    @XmlElements({
        @XmlElement(name = "BaseArticleData", type = XMLBaseArticleData.class),
        @XmlElement(name = "SalesArticleData", type = XMLSalesArticleData.class)
    })
    protected List<Object> baseArticleDataOrSalesArticleData;

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
     * Gets the value of the baseArticleDataOrSalesArticleData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the baseArticleDataOrSalesArticleData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBaseArticleDataOrSalesArticleData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLBaseArticleData }
     * {@link XMLSalesArticleData }
     * 
     * 
     */
    public List<Object> getBaseArticleDataOrSalesArticleData() {
        if (baseArticleDataOrSalesArticleData == null) {
            baseArticleDataOrSalesArticleData = new ArrayList<Object>();
        }
        return this.baseArticleDataOrSalesArticleData;
    }

}
