//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lp.server.schema.vendidata.discounts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DiscountPayment_QNAME = new QName("http://www.vendidata.com/XML/Schema/Discounts", "DiscountPayment");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lp.server.schema.vendidata.discounts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLDiscountPayment }
     * 
     */
    public XMLDiscountPayment createXMLDiscountPayment() {
        return new XMLDiscountPayment();
    }

    /**
     * Create an instance of {@link XMLPaymentInfo }
     * 
     */
    public XMLPaymentInfo createXMLPaymentInfo() {
        return new XMLPaymentInfo();
    }

    /**
     * Create an instance of {@link XMLDiscountFormulaDetail }
     * 
     */
    public XMLDiscountFormulaDetail createXMLDiscountFormulaDetail() {
        return new XMLDiscountFormulaDetail();
    }

    /**
     * Create an instance of {@link XMLAmount }
     * 
     */
    public XMLAmount createXMLAmount() {
        return new XMLAmount();
    }

    /**
     * Create an instance of {@link XMLDiscountFormulaDetails }
     * 
     */
    public XMLDiscountFormulaDetails createXMLDiscountFormulaDetails() {
        return new XMLDiscountFormulaDetails();
    }

    /**
     * Create an instance of {@link XMLCustomerPaymentInfo }
     * 
     */
    public XMLCustomerPaymentInfo createXMLCustomerPaymentInfo() {
        return new XMLCustomerPaymentInfo();
    }

    /**
     * Create an instance of {@link XMLSettledPaymentInfo }
     * 
     */
    public XMLSettledPaymentInfo createXMLSettledPaymentInfo() {
        return new XMLSettledPaymentInfo();
    }

    /**
     * Create an instance of {@link XMLFormulaPaymentPeriod }
     * 
     */
    public XMLFormulaPaymentPeriod createXMLFormulaPaymentPeriod() {
        return new XMLFormulaPaymentPeriod();
    }

    /**
     * Create an instance of {@link XMLLocalizedDiscountFormulaDetail }
     * 
     */
    public XMLLocalizedDiscountFormulaDetail createXMLLocalizedDiscountFormulaDetail() {
        return new XMLLocalizedDiscountFormulaDetail();
    }

    /**
     * Create an instance of {@link XMLAliquotAmount }
     * 
     */
    public XMLAliquotAmount createXMLAliquotAmount() {
        return new XMLAliquotAmount();
    }

    /**
     * Create an instance of {@link XMLCombinedPaymentInfo }
     * 
     */
    public XMLCombinedPaymentInfo createXMLCombinedPaymentInfo() {
        return new XMLCombinedPaymentInfo();
    }

    /**
     * Create an instance of {@link XMLVirtualVendingmachinePaymentInfo }
     * 
     */
    public XMLVirtualVendingmachinePaymentInfo createXMLVirtualVendingmachinePaymentInfo() {
        return new XMLVirtualVendingmachinePaymentInfo();
    }

    /**
     * Create an instance of {@link XMLPaymentPeriod }
     * 
     */
    public XMLPaymentPeriod createXMLPaymentPeriod() {
        return new XMLPaymentPeriod();
    }

    /**
     * Create an instance of {@link XMLFormulaPaymentInfo }
     * 
     */
    public XMLFormulaPaymentInfo createXMLFormulaPaymentInfo() {
        return new XMLFormulaPaymentInfo();
    }

    /**
     * Create an instance of {@link XMLCustomerSettledPaymentInfo }
     * 
     */
    public XMLCustomerSettledPaymentInfo createXMLCustomerSettledPaymentInfo() {
        return new XMLCustomerSettledPaymentInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLDiscountPayment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.vendidata.com/XML/Schema/Discounts", name = "DiscountPayment")
    public JAXBElement<XMLDiscountPayment> createDiscountPayment(XMLDiscountPayment value) {
        return new JAXBElement<XMLDiscountPayment>(_DiscountPayment_QNAME, XMLDiscountPayment.class, null, value);
    }

}
