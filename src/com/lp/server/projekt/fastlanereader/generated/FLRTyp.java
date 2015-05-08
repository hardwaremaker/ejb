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
package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTyp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private com.lp.server.projekt.fastlanereader.generated.FLRTypPK PK;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** persistent field */
    private Set typ_typ_set;

    /** full constructor */
    public FLRTyp(com.lp.server.projekt.fastlanereader.generated.FLRTypPK PK, String c_nr, String mandant_c_nr, Integer i_sort, Set typ_typ_set) {
        this.PK = PK;
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.i_sort = i_sort;
        this.typ_typ_set = typ_typ_set;
    }

    /** default constructor */
    public FLRTyp() {
    }

    /** minimal constructor */
    public FLRTyp(com.lp.server.projekt.fastlanereader.generated.FLRTypPK PK, Set typ_typ_set) {
        this.PK = PK;
        this.typ_typ_set = typ_typ_set;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRTypPK getPK() {
        return this.PK;
    }

    public void setPK(com.lp.server.projekt.fastlanereader.generated.FLRTypPK PK) {
        this.PK = PK;
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

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Set getTyp_typ_set() {
        return this.typ_typ_set;
    }

    public void setTyp_typ_set(Set typ_typ_set) {
        this.typ_typ_set = typ_typ_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("PK", getPK())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRTyp) ) return false;
        FLRTyp castOther = (FLRTyp) other;
        return new EqualsBuilder()
            .append(this.getPK(), castOther.getPK())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPK())
            .toHashCode();
    }

}
