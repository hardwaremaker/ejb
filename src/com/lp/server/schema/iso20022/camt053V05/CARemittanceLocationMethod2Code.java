//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer RemittanceLocationMethod2Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="RemittanceLocationMethod2Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FAXI"/>
 *     &lt;enumeration value="EDIC"/>
 *     &lt;enumeration value="URID"/>
 *     &lt;enumeration value="EMAL"/>
 *     &lt;enumeration value="POST"/>
 *     &lt;enumeration value="SMSM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RemittanceLocationMethod2Code")
@XmlEnum
public enum CARemittanceLocationMethod2Code {

    FAXI,
    EDIC,
    URID,
    EMAL,
    POST,
    SMSM;

    public String value() {
        return name();
    }

    public static CARemittanceLocationMethod2Code fromValue(String v) {
        return valueOf(v);
    }

}
