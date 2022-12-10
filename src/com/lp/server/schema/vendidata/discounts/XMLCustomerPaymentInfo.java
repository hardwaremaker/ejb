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
import javax.xml.bind.annotation.XmlType;
import com.lp.server.schema.vendidata.commonobjects.XMLCustomer;
import com.lp.server.schema.vendidata.commonobjects.XMLCustomerContact;


/**
 * <p>Java-Klasse fuer CustomerPaymentInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CustomerPaymentInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.vendidata.com/XML/Schema/Discounts}CombinedPaymentInfo">
 *       &lt;sequence>
 *         &lt;element name="BeneficiaryCustomer" type="{http://www.vendidata.com/XML/Schema/CommonObjects}Customer"/>
 *         &lt;element name="BeneficiaryCustomerInfoText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BeneficiaryContact" type="{http://www.vendidata.com/XML/Schema/CommonObjects}CustomerContact" minOccurs="0"/>
 *         &lt;element name="SettledPaymentInfo" type="{http://www.vendidata.com/XML/Schema/Discounts}CustomerSettledPaymentInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="VirtualVendingmachinePaymentInfo" type="{http://www.vendidata.com/XML/Schema/Discounts}VirtualVendingmachinePaymentInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPaymentInfo", propOrder = {
    "beneficiaryCustomer",
    "beneficiaryCustomerInfoText",
    "beneficiaryContact",
    "settledPaymentInfo",
    "virtualVendingmachinePaymentInfo"
})
public class XMLCustomerPaymentInfo
    extends XMLCombinedPaymentInfo
{

    @XmlElement(name = "BeneficiaryCustomer", required = true)
    protected XMLCustomer beneficiaryCustomer;
    @XmlElement(name = "BeneficiaryCustomerInfoText", required = true)
    protected String beneficiaryCustomerInfoText;
    @XmlElement(name = "BeneficiaryContact")
    protected XMLCustomerContact beneficiaryContact;
    @XmlElement(name = "SettledPaymentInfo")
    protected List<XMLCustomerSettledPaymentInfo> settledPaymentInfo;
    @XmlElement(name = "VirtualVendingmachinePaymentInfo")
    protected List<XMLVirtualVendingmachinePaymentInfo> virtualVendingmachinePaymentInfo;

    /**
     * Ruft den Wert der beneficiaryCustomer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLCustomer }
     *     
     */
    public XMLCustomer getBeneficiaryCustomer() {
        return beneficiaryCustomer;
    }

    /**
     * Legt den Wert der beneficiaryCustomer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCustomer }
     *     
     */
    public void setBeneficiaryCustomer(XMLCustomer value) {
        this.beneficiaryCustomer = value;
    }

    /**
     * Ruft den Wert der beneficiaryCustomerInfoText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryCustomerInfoText() {
        return beneficiaryCustomerInfoText;
    }

    /**
     * Legt den Wert der beneficiaryCustomerInfoText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryCustomerInfoText(String value) {
        this.beneficiaryCustomerInfoText = value;
    }

    /**
     * Ruft den Wert der beneficiaryContact-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLCustomerContact }
     *     
     */
    public XMLCustomerContact getBeneficiaryContact() {
        return beneficiaryContact;
    }

    /**
     * Legt den Wert der beneficiaryContact-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCustomerContact }
     *     
     */
    public void setBeneficiaryContact(XMLCustomerContact value) {
        this.beneficiaryContact = value;
    }

    /**
     * Gets the value of the settledPaymentInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the settledPaymentInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSettledPaymentInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLCustomerSettledPaymentInfo }
     * 
     * 
     */
    public List<XMLCustomerSettledPaymentInfo> getSettledPaymentInfo() {
        if (settledPaymentInfo == null) {
            settledPaymentInfo = new ArrayList<XMLCustomerSettledPaymentInfo>();
        }
        return this.settledPaymentInfo;
    }

    /**
     * Gets the value of the virtualVendingmachinePaymentInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualVendingmachinePaymentInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualVendingmachinePaymentInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLVirtualVendingmachinePaymentInfo }
     * 
     * 
     */
    public List<XMLVirtualVendingmachinePaymentInfo> getVirtualVendingmachinePaymentInfo() {
        if (virtualVendingmachinePaymentInfo == null) {
            virtualVendingmachinePaymentInfo = new ArrayList<XMLVirtualVendingmachinePaymentInfo>();
        }
        return this.virtualVendingmachinePaymentInfo;
    }

}
