//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PriceLineUpdateMode.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="PriceLineUpdateMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="REPLACE"/>
 *     &lt;enumeration value="ADD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PriceLineUpdateMode")
@XmlEnum
public enum XMLPriceLineUpdateMode {

    REPLACE,
    ADD;

    public String value() {
        return name();
    }

    public static XMLPriceLineUpdateMode fromValue(String v) {
        return valueOf(v);
    }

}
