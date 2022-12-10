//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lp.server.schema.vendidata.customers package. 
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

    private final static QName _Customers_QNAME = new QName("http://www.vendidata.com/XML/Schema/Customers", "Customers");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lp.server.schema.vendidata.customers
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLCustomer }
     * 
     */
    public XMLCustomer createXMLCustomer() {
        return new XMLCustomer();
    }

    /**
     * Create an instance of {@link XMLCustomers }
     * 
     */
    public XMLCustomers createXMLCustomers() {
        return new XMLCustomers();
    }

    /**
     * Create an instance of {@link XMLAdditionalFieldDoubleValue }
     * 
     */
    public XMLAdditionalFieldDoubleValue createXMLAdditionalFieldDoubleValue() {
        return new XMLAdditionalFieldDoubleValue();
    }

    /**
     * Create an instance of {@link XMLSalesPriceList }
     * 
     */
    public XMLSalesPriceList createXMLSalesPriceList() {
        return new XMLSalesPriceList();
    }

    /**
     * Create an instance of {@link XMLUpdateLegacyId }
     * 
     */
    public XMLUpdateLegacyId createXMLUpdateLegacyId() {
        return new XMLUpdateLegacyId();
    }

    /**
     * Create an instance of {@link XMLCustomerCluster }
     * 
     */
    public XMLCustomerCluster createXMLCustomerCluster() {
        return new XMLCustomerCluster();
    }

    /**
     * Create an instance of {@link XMLSalesPrice }
     * 
     */
    public XMLSalesPrice createXMLSalesPrice() {
        return new XMLSalesPrice();
    }

    /**
     * Create an instance of {@link XMLLanguage }
     * 
     */
    public XMLLanguage createXMLLanguage() {
        return new XMLLanguage();
    }

    /**
     * Create an instance of {@link XMLPostalcode }
     * 
     */
    public XMLPostalcode createXMLPostalcode() {
        return new XMLPostalcode();
    }

    /**
     * Create an instance of {@link XMLPriceLines }
     * 
     */
    public XMLPriceLines createXMLPriceLines() {
        return new XMLPriceLines();
    }

    /**
     * Create an instance of {@link XMLAdditionalFieldChoiceValue }
     * 
     */
    public XMLAdditionalFieldChoiceValue createXMLAdditionalFieldChoiceValue() {
        return new XMLAdditionalFieldChoiceValue();
    }

    /**
     * Create an instance of {@link XMLAdditionalFields }
     * 
     */
    public XMLAdditionalFields createXMLAdditionalFields() {
        return new XMLAdditionalFields();
    }

    /**
     * Create an instance of {@link XMLContact }
     * 
     */
    public XMLContact createXMLContact() {
        return new XMLContact();
    }

    /**
     * Create an instance of {@link XMLVirtualVendingmachine }
     * 
     */
    public XMLVirtualVendingmachine createXMLVirtualVendingmachine() {
        return new XMLVirtualVendingmachine();
    }

    /**
     * Create an instance of {@link XMLStand }
     * 
     */
    public XMLStand createXMLStand() {
        return new XMLStand();
    }

    /**
     * Create an instance of {@link XMLCountry }
     * 
     */
    public XMLCountry createXMLCountry() {
        return new XMLCountry();
    }

    /**
     * Create an instance of {@link XMLAdditionalFieldStringValue }
     * 
     */
    public XMLAdditionalFieldStringValue createXMLAdditionalFieldStringValue() {
        return new XMLAdditionalFieldStringValue();
    }

    /**
     * Create an instance of {@link XMLCustomer.AutoGenerateAccountingId }
     * 
     */
    public XMLCustomer.AutoGenerateAccountingId createXMLCustomerAutoGenerateAccountingId() {
        return new XMLCustomer.AutoGenerateAccountingId();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLCustomers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.vendidata.com/XML/Schema/Customers", name = "Customers")
    public JAXBElement<XMLCustomers> createCustomers(XMLCustomers value) {
        return new JAXBElement<XMLCustomers>(_Customers_QNAME, XMLCustomers.class, null, value);
    }

}
