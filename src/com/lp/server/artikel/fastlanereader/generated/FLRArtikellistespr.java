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
package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.service.FLRArtikellistesprPK;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellistespr implements Serializable {

    /** identifier field */
    private FLRArtikellistesprPK Id;

    /** persistent field */
    private Integer personali_id_aendern;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private String c_kbez;

    /** nullable persistent field */
    private String c_zbez2;

    /** persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private String c_bez;

    /** persistent field */
    private String locale_c_nr;

    /** nullable persistent field */
    private String c_siwert;

    /** full constructor */
    public FLRArtikellistespr(FLRArtikellistesprPK Id, Integer personali_id_aendern, String c_zbez, String c_kbez, String c_zbez2, Date t_aendern, String c_bez, String locale_c_nr, String c_siwert) {
        this.Id = Id;
        this.personali_id_aendern = personali_id_aendern;
        this.c_zbez = c_zbez;
        this.c_kbez = c_kbez;
        this.c_zbez2 = c_zbez2;
        this.t_aendern = t_aendern;
        this.c_bez = c_bez;
        this.locale_c_nr = locale_c_nr;
        this.c_siwert = c_siwert;
    }

    /** default constructor */
    public FLRArtikellistespr() {
    }

    /** minimal constructor */
    public FLRArtikellistespr(FLRArtikellistesprPK Id, Integer personali_id_aendern, Date t_aendern, String locale_c_nr) {
        this.Id = Id;
        this.personali_id_aendern = personali_id_aendern;
        this.t_aendern = t_aendern;
        this.locale_c_nr = locale_c_nr;
    }

    public FLRArtikellistesprPK getId() {
        return this.Id;
    }

    public void setId(FLRArtikellistesprPK Id) {
        this.Id = Id;
    }

    public Integer getPersonali_id_aendern() {
        return this.personali_id_aendern;
    }

    public void setPersonali_id_aendern(Integer personali_id_aendern) {
        this.personali_id_aendern = personali_id_aendern;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public String getC_kbez() {
        return this.c_kbez;
    }

    public void setC_kbez(String c_kbez) {
        this.c_kbez = c_kbez;
    }

    public String getC_zbez2() {
        return this.c_zbez2;
    }

    public void setC_zbez2(String c_zbez2) {
        this.c_zbez2 = c_zbez2;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getLocale_c_nr() {
        return this.locale_c_nr;
    }

    public void setLocale_c_nr(String locale_c_nr) {
        this.locale_c_nr = locale_c_nr;
    }

    public String getC_siwert() {
        return this.c_siwert;
    }

    public void setC_siwert(String c_siwert) {
        this.c_siwert = c_siwert;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("Id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRArtikellistespr) ) return false;
        FLRArtikellistespr castOther = (FLRArtikellistespr) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
