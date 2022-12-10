//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer VendingmachineComponentClass.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="VendingmachineComponentClass">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADDON"/>
 *     &lt;enumeration value="BASEMACHINE"/>
 *     &lt;enumeration value="BILLVALIDATOR"/>
 *     &lt;enumeration value="COINCONTROLUNIT"/>
 *     &lt;enumeration value="PAYMENTSYSTEM"/>
 *     &lt;enumeration value="QUICK"/>
 *     &lt;enumeration value="WEARING_PART"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VendingmachineComponentClass")
@XmlEnum
public enum XMLVendingmachineComponentClass {

    ADDON,
    BASEMACHINE,
    BILLVALIDATOR,
    COINCONTROLUNIT,
    PAYMENTSYSTEM,
    QUICK,
    WEARING_PART;

    public String value() {
        return name();
    }

    public static XMLVendingmachineComponentClass fromValue(String v) {
        return valueOf(v);
    }

}
