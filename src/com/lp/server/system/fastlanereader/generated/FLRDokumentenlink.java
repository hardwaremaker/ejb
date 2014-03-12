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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRDokumentenlink implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_ordner;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private String c_basispfad;

    /** nullable persistent field */
    private String c_menuetext;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_pfadabsolut;

    /** full constructor */
    public FLRDokumentenlink(String c_ordner, String belegart_c_nr, String c_basispfad, String c_menuetext, String mandant_c_nr, Short b_pfadabsolut) {
        this.c_ordner = c_ordner;
        this.belegart_c_nr = belegart_c_nr;
        this.c_basispfad = c_basispfad;
        this.c_menuetext = c_menuetext;
        this.mandant_c_nr = mandant_c_nr;
        this.b_pfadabsolut = b_pfadabsolut;
    }

    /** default constructor */
    public FLRDokumentenlink() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_ordner() {
        return this.c_ordner;
    }

    public void setC_ordner(String c_ordner) {
        this.c_ordner = c_ordner;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public String getC_basispfad() {
        return this.c_basispfad;
    }

    public void setC_basispfad(String c_basispfad) {
        this.c_basispfad = c_basispfad;
    }

    public String getC_menuetext() {
        return this.c_menuetext;
    }

    public void setC_menuetext(String c_menuetext) {
        this.c_menuetext = c_menuetext;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_pfadabsolut() {
        return this.b_pfadabsolut;
    }

    public void setB_pfadabsolut(Short b_pfadabsolut) {
        this.b_pfadabsolut = b_pfadabsolut;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
