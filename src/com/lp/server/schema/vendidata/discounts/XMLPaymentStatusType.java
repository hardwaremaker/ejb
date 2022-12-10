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
 * <p>Java-Klasse fuer PaymentStatusType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="PaymentStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PAY"/>
 *     &lt;enumeration value="DONT_PAY"/>
 *     &lt;enumeration value="POSTPONE"/>
 *     &lt;enumeration value="MANUALLY_ADJUSTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PaymentStatusType")
@XmlEnum
public enum XMLPaymentStatusType {

    PAY,
    DONT_PAY,
    POSTPONE,
    MANUALLY_ADJUSTED;

    public String value() {
        return name();
    }

    public static XMLPaymentStatusType fromValue(String v) {
        return valueOf(v);
    }

}
