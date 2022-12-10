/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.01.11 um 09:04:33 AM CET 
//


package com.lp.server.schema.vendidata.articleconsumption;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.lp.server.schema.vendidata.commonobjects.XMLMonthlyBalance;


/**
 * <p>Java-Klasse fuer ArticleConsumptions complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ArticleConsumptions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MonthlyBalance" type="{http://www.vendidata.com/XML/Schema/CommonObjects}MonthlyBalance" minOccurs="0"/>
 *         &lt;element name="ArticleConsumptions" type="{http://www.vendidata.com/XML/Schema/ArticleConsumption}ArticleConsumption" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleConsumptions", propOrder = {
    "monthlyBalance",
    "articleConsumptions"
})
public class XMLArticleConsumptions {

    @XmlElement(name = "MonthlyBalance")
    protected XMLMonthlyBalance monthlyBalance;
    @XmlElement(name = "ArticleConsumptions", required = true)
    protected List<XMLArticleConsumption> articleConsumptions;

    /**
     * Ruft den Wert der monthlyBalance-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLMonthlyBalance }
     *     
     */
    public XMLMonthlyBalance getMonthlyBalance() {
        return monthlyBalance;
    }

    /**
     * Legt den Wert der monthlyBalance-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLMonthlyBalance }
     *     
     */
    public void setMonthlyBalance(XMLMonthlyBalance value) {
        this.monthlyBalance = value;
    }

    /**
     * Gets the value of the articleConsumptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the articleConsumptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticleConsumptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLArticleConsumption }
     * 
     * 
     */
    public List<XMLArticleConsumption> getArticleConsumptions() {
        if (articleConsumptions == null) {
            articleConsumptions = new ArrayList<XMLArticleConsumption>();
        }
        return this.articleConsumptions;
    }

}
