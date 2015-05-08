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
 *******************************************************************************/
package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjektstatus implements Serializable {

    /** identifier field */
    private String status_c_nr;

    /** identifier field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_erledigt;

    /** nullable persistent field */
    private Integer i_sort;

    /** full constructor */
    public FLRProjektstatus(String status_c_nr, String mandant_c_nr, Short b_erledigt, Integer i_sort) {
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.b_erledigt = b_erledigt;
        this.i_sort = i_sort;
    }

    /** default constructor */
    public FLRProjektstatus() {
    }

    /** minimal constructor */
    public FLRProjektstatus(String status_c_nr, String mandant_c_nr) {
        this.status_c_nr = status_c_nr;
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_erledigt() {
        return this.b_erledigt;
    }

    public void setB_erledigt(Short b_erledigt) {
        this.b_erledigt = b_erledigt;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("status_c_nr", getStatus_c_nr())
            .append("mandant_c_nr", getMandant_c_nr())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRProjektstatus) ) return false;
        FLRProjektstatus castOther = (FLRProjektstatus) other;
        return new EqualsBuilder()
            .append(this.getStatus_c_nr(), castOther.getStatus_c_nr())
            .append(this.getMandant_c_nr(), castOther.getMandant_c_nr())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getStatus_c_nr())
            .append(getMandant_c_nr())
            .toHashCode();
    }

}
