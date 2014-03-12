/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRShopgruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe;

    /** persistent field */
    private Set shopgruppesprset;

    private Integer i_sort ;
    
    /** full constructor */
    public FLRShopgruppe(Integer i_sort, String c_nr, String mandant_c_nr, com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe, Set shopgruppesprset) {
        this.i_sort = i_sort ;
    	this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.flrshopgruppe = flrshopgruppe;
        this.shopgruppesprset = shopgruppesprset;
    }

    /** default constructor */
    public FLRShopgruppe() {
    }

    /** minimal constructor */
    public FLRShopgruppe(String mandant_c_nr, Set shopgruppesprset) {
        this.mandant_c_nr = mandant_c_nr;
        this.shopgruppesprset = shopgruppesprset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe getFlrshopgruppe() {
        return this.flrshopgruppe;
    }

    public void setFlrshopgruppe(com.lp.server.artikel.fastlanereader.generated.FLRShopgruppe flrshopgruppe) {
        this.flrshopgruppe = flrshopgruppe;
    }

    public Set getShopgruppesprset() {
        return this.shopgruppesprset;
    }

    public void setShopgruppesprset(Set shopgruppesprset) {
        this.shopgruppesprset = shopgruppesprset;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
