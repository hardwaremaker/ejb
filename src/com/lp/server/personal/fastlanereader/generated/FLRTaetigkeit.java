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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTaetigkeit implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String taetigkeitart_c_nr;

    /** nullable persistent field */
    private Short b_tagbuchbar;

    /** nullable persistent field */
    private Double f_bezahlt;

    /** nullable persistent field */
    private Short b_bdebuchbar;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_importkennzeichen;

    /** persistent field */
    private Set taetigkeitsprset;

    /** full constructor */
    public FLRTaetigkeit(String c_nr, String taetigkeitart_c_nr, Short b_tagbuchbar, Double f_bezahlt, Short b_bdebuchbar, Short b_versteckt, String c_importkennzeichen, Set taetigkeitsprset) {
        this.c_nr = c_nr;
        this.taetigkeitart_c_nr = taetigkeitart_c_nr;
        this.b_tagbuchbar = b_tagbuchbar;
        this.f_bezahlt = f_bezahlt;
        this.b_bdebuchbar = b_bdebuchbar;
        this.b_versteckt = b_versteckt;
        this.c_importkennzeichen = c_importkennzeichen;
        this.taetigkeitsprset = taetigkeitsprset;
    }

    /** default constructor */
    public FLRTaetigkeit() {
    }

    /** minimal constructor */
    public FLRTaetigkeit(Set taetigkeitsprset) {
        this.taetigkeitsprset = taetigkeitsprset;
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

    public String getTaetigkeitart_c_nr() {
        return this.taetigkeitart_c_nr;
    }

    public void setTaetigkeitart_c_nr(String taetigkeitart_c_nr) {
        this.taetigkeitart_c_nr = taetigkeitart_c_nr;
    }

    public Short getB_tagbuchbar() {
        return this.b_tagbuchbar;
    }

    public void setB_tagbuchbar(Short b_tagbuchbar) {
        this.b_tagbuchbar = b_tagbuchbar;
    }

    public Double getF_bezahlt() {
        return this.f_bezahlt;
    }

    public void setF_bezahlt(Double f_bezahlt) {
        this.f_bezahlt = f_bezahlt;
    }

    public Short getB_bdebuchbar() {
        return this.b_bdebuchbar;
    }

    public void setB_bdebuchbar(Short b_bdebuchbar) {
        this.b_bdebuchbar = b_bdebuchbar;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_importkennzeichen() {
        return this.c_importkennzeichen;
    }

    public void setC_importkennzeichen(String c_importkennzeichen) {
        this.c_importkennzeichen = c_importkennzeichen;
    }

    public Set getTaetigkeitsprset() {
        return this.taetigkeitsprset;
    }

    public void setTaetigkeitsprset(Set taetigkeitsprset) {
        this.taetigkeitsprset = taetigkeitsprset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
