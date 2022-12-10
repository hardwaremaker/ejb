//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.07.30 um 10:14:02 AM CEST 
//


package com.lp.server.schema.iso20022.pain001V03;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ChequeType2Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="ChequeType2Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CCHQ"/>
 *     &lt;enumeration value="CCCH"/>
 *     &lt;enumeration value="BCHQ"/>
 *     &lt;enumeration value="DRFT"/>
 *     &lt;enumeration value="ELDR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ChequeType2Code")
@XmlEnum
public enum PAChequeType2Code {

    CCHQ,
    CCCH,
    BCHQ,
    DRFT,
    ELDR;

    public String value() {
        return name();
    }

    public static PAChequeType2Code fromValue(String v) {
        return valueOf(v);
    }

}
