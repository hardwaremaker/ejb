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

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellog implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private String c_key;

    /** nullable persistent field */
    private String c_von;

    /** nullable persistent field */
    private String c_nach;

    /** nullable persistent field */
    private String locale_c_nr;

    /** nullable persistent field */
    private java.sql.Timestamp t_aendern;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** full constructor */
    public FLRArtikellog(Integer artikel_i_id, String c_key, String c_von, String c_nach, String locale_c_nr, java.sql.Timestamp t_aendern, FLRPersonal flrpersonal) {
        this.artikel_i_id = artikel_i_id;
        this.c_key = c_key;
        this.c_von = c_von;
        this.c_nach = c_nach;
        this.locale_c_nr = locale_c_nr;
        this.t_aendern = t_aendern;
        this.flrpersonal = flrpersonal;
    }

    /** default constructor */
    public FLRArtikellog() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public String getC_key() {
        return this.c_key;
    }

    public void setC_key(String c_key) {
        this.c_key = c_key;
    }

    public String getC_von() {
        return this.c_von;
    }

    public void setC_von(String c_von) {
        this.c_von = c_von;
    }

    public String getC_nach() {
        return this.c_nach;
    }

    public void setC_nach(String c_nach) {
        this.c_nach = c_nach;
    }

    public String getLocale_c_nr() {
        return this.locale_c_nr;
    }

    public void setLocale_c_nr(String locale_c_nr) {
        this.locale_c_nr = locale_c_nr;
    }

    public java.sql.Timestamp getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(java.sql.Timestamp t_aendern) {
        this.t_aendern = t_aendern;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
