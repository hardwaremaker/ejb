//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:22:17 AM CEST 
//


package com.lp.server.schema.vendidata.suppliers;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lp.server.schema.vendidata.suppliers package. 
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

    private final static QName _Suppliers_QNAME = new QName("http://www.vendidata.com/XML/Schema/Suppliers", "Suppliers");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lp.server.schema.vendidata.suppliers
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLSupplier }
     * 
     */
    public XMLSupplier createXMLSupplier() {
        return new XMLSupplier();
    }

    /**
     * Create an instance of {@link XMLSuppliers }
     * 
     */
    public XMLSuppliers createXMLSuppliers() {
        return new XMLSuppliers();
    }

    /**
     * Create an instance of {@link XMLSupplierContact }
     * 
     */
    public XMLSupplierContact createXMLSupplierContact() {
        return new XMLSupplierContact();
    }

    /**
     * Create an instance of {@link XMLSupplier.AutoGenerateAccountingId }
     * 
     */
    public XMLSupplier.AutoGenerateAccountingId createXMLSupplierAutoGenerateAccountingId() {
        return new XMLSupplier.AutoGenerateAccountingId();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLSuppliers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.vendidata.com/XML/Schema/Suppliers", name = "Suppliers")
    public JAXBElement<XMLSuppliers> createSuppliers(XMLSuppliers value) {
        return new JAXBElement<XMLSuppliers>(_Suppliers_QNAME, XMLSuppliers.class, null, value);
    }

}
