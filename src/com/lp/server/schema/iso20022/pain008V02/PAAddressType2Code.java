//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer AddressType2Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="AddressType2Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADDR"/>
 *     &lt;enumeration value="PBOX"/>
 *     &lt;enumeration value="HOME"/>
 *     &lt;enumeration value="BIZZ"/>
 *     &lt;enumeration value="MLTO"/>
 *     &lt;enumeration value="DLVY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AddressType2Code")
@XmlEnum
public enum PAAddressType2Code {

    ADDR,
    PBOX,
    HOME,
    BIZZ,
    MLTO,
    DLVY;

    public String value() {
        return name();
    }

    public static PAAddressType2Code fromValue(String v) {
        return valueOf(v);
    }

}
