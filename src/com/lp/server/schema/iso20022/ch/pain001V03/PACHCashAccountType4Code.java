//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CashAccountType4Code.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="CashAccountType4Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CASH"/>
 *     &lt;enumeration value="CHAR"/>
 *     &lt;enumeration value="COMM"/>
 *     &lt;enumeration value="TAXE"/>
 *     &lt;enumeration value="CISH"/>
 *     &lt;enumeration value="TRAS"/>
 *     &lt;enumeration value="SACC"/>
 *     &lt;enumeration value="CACC"/>
 *     &lt;enumeration value="SVGS"/>
 *     &lt;enumeration value="ONDP"/>
 *     &lt;enumeration value="MGLD"/>
 *     &lt;enumeration value="NREX"/>
 *     &lt;enumeration value="MOMA"/>
 *     &lt;enumeration value="LOAN"/>
 *     &lt;enumeration value="SLRY"/>
 *     &lt;enumeration value="ODFT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CashAccountType4Code")
@XmlEnum
public enum PACHCashAccountType4Code {

    CASH,
    CHAR,
    COMM,
    TAXE,
    CISH,
    TRAS,
    SACC,
    CACC,
    SVGS,
    ONDP,
    MGLD,
    NREX,
    MOMA,
    LOAN,
    SLRY,
    ODFT;

    public String value() {
        return name();
    }

    public static PACHCashAccountType4Code fromValue(String v) {
        return valueOf(v);
    }

}
