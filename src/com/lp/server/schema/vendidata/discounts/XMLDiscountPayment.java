//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.lp.server.schema.vendidata.commonobjects.XMLEmployee;


/**
 * <p>Java-Klasse fuer DiscountPayment complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DiscountPayment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.vendidata.com/XML/Schema/Discounts}CombinedPaymentInfo">
 *       &lt;sequence>
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CreationEmployee" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Employee"/>
 *         &lt;element name="DiscountPeriod" type="{http://www.vendidata.com/XML/Schema/Discounts}PaymentPeriod"/>
 *         &lt;element name="CustomerPaymentInfo" type="{http://www.vendidata.com/XML/Schema/Discounts}CustomerPaymentInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiscountPayment", propOrder = {
    "creationDate",
    "creationEmployee",
    "discountPeriod",
    "customerPaymentInfo"
})
public class XMLDiscountPayment
    extends XMLCombinedPaymentInfo
{

    @XmlElement(name = "CreationDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    @XmlElement(name = "CreationEmployee", required = true)
    protected XMLEmployee creationEmployee;
    @XmlElement(name = "DiscountPeriod", required = true)
    protected XMLPaymentPeriod discountPeriod;
    @XmlElement(name = "CustomerPaymentInfo")
    protected List<XMLCustomerPaymentInfo> customerPaymentInfo;

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
     * Ruft den Wert der creationEmployee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLEmployee }
     *     
     */
    public XMLEmployee getCreationEmployee() {
        return creationEmployee;
    }

    /**
     * Legt den Wert der creationEmployee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLEmployee }
     *     
     */
    public void setCreationEmployee(XMLEmployee value) {
        this.creationEmployee = value;
    }

    /**
     * Ruft den Wert der discountPeriod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLPaymentPeriod }
     *     
     */
    public XMLPaymentPeriod getDiscountPeriod() {
        return discountPeriod;
    }

    /**
     * Legt den Wert der discountPeriod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLPaymentPeriod }
     *     
     */
    public void setDiscountPeriod(XMLPaymentPeriod value) {
        this.discountPeriod = value;
    }

    /**
     * Gets the value of the customerPaymentInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerPaymentInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerPaymentInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLCustomerPaymentInfo }
     * 
     * 
     */
    public List<XMLCustomerPaymentInfo> getCustomerPaymentInfo() {
        if (customerPaymentInfo == null) {
            customerPaymentInfo = new ArrayList<XMLCustomerPaymentInfo>();
        }
        return this.customerPaymentInfo;
    }

}
