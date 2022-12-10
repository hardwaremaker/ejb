//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Party6Choice-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Party6Choice-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="OrgId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}OrganisationIdentification4-CH"/>
 *           &lt;element name="PrvtId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PersonIdentification5-CH"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party6Choice-CH", propOrder = {
    "orgId",
    "prvtId"
})
public class PACHParty6ChoiceCH {

    @XmlElement(name = "OrgId")
    protected PACHOrganisationIdentification4CH orgId;
    @XmlElement(name = "PrvtId")
    protected PACHPersonIdentification5CH prvtId;

    /**
     * Ruft den Wert der orgId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHOrganisationIdentification4CH }
     *     
     */
    public PACHOrganisationIdentification4CH getOrgId() {
        return orgId;
    }

    /**
     * Legt den Wert der orgId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHOrganisationIdentification4CH }
     *     
     */
    public void setOrgId(PACHOrganisationIdentification4CH value) {
        this.orgId = value;
    }

    /**
     * Ruft den Wert der prvtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPersonIdentification5CH }
     *     
     */
    public PACHPersonIdentification5CH getPrvtId() {
        return prvtId;
    }

    /**
     * Legt den Wert der prvtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPersonIdentification5CH }
     *     
     */
    public void setPrvtId(PACHPersonIdentification5CH value) {
        this.prvtId = value;
    }

}
