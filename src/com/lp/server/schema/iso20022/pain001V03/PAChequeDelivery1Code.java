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
 * <p>Java-Klasse fuer ChequeDelivery1Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="ChequeDelivery1Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MLDB"/>
 *     &lt;enumeration value="MLCD"/>
 *     &lt;enumeration value="MLFA"/>
 *     &lt;enumeration value="CRDB"/>
 *     &lt;enumeration value="CRCD"/>
 *     &lt;enumeration value="CRFA"/>
 *     &lt;enumeration value="PUDB"/>
 *     &lt;enumeration value="PUCD"/>
 *     &lt;enumeration value="PUFA"/>
 *     &lt;enumeration value="RGDB"/>
 *     &lt;enumeration value="RGCD"/>
 *     &lt;enumeration value="RGFA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ChequeDelivery1Code")
@XmlEnum
public enum PAChequeDelivery1Code {

    MLDB,
    MLCD,
    MLFA,
    CRDB,
    CRCD,
    CRFA,
    PUDB,
    PUCD,
    PUFA,
    RGDB,
    RGCD,
    RGFA;

    public String value() {
        return name();
    }

    public static PAChequeDelivery1Code fromValue(String v) {
        return valueOf(v);
    }

}
