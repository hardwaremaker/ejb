//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PeriodicType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="PeriodicType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MONTHLY"/>
 *     &lt;enumeration value="QUARTERLY"/>
 *     &lt;enumeration value="BIANNUAL"/>
 *     &lt;enumeration value="ANNUAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PeriodicType")
@XmlEnum
public enum XMLPeriodicType {

    MONTHLY,
    QUARTERLY,
    BIANNUAL,
    ANNUAL;

    public String value() {
        return name();
    }

    public static XMLPeriodicType fromValue(String v) {
        return valueOf(v);
    }

}
