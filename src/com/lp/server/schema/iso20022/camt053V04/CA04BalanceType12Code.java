//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r BalanceType12Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="BalanceType12Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="XPCD"/>
 *     &lt;enumeration value="OPAV"/>
 *     &lt;enumeration value="ITAV"/>
 *     &lt;enumeration value="CLAV"/>
 *     &lt;enumeration value="FWAV"/>
 *     &lt;enumeration value="CLBD"/>
 *     &lt;enumeration value="ITBD"/>
 *     &lt;enumeration value="OPBD"/>
 *     &lt;enumeration value="PRCD"/>
 *     &lt;enumeration value="INFO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BalanceType12Code")
@XmlEnum
public enum CA04BalanceType12Code {

    XPCD,
    OPAV,
    ITAV,
    CLAV,
    FWAV,
    CLBD,
    ITBD,
    OPBD,
    PRCD,
    INFO;

    public String value() {
        return name();
    }

    public static CA04BalanceType12Code fromValue(String v) {
        return valueOf(v);
    }

}
