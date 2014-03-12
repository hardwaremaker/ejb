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
public class FLRPanelbeschreibung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_name;

    /** nullable persistent field */
    private String c_typ;

    /** nullable persistent field */
    private String c_fill;

    /** nullable persistent field */
    private String c_anchor;

    /** nullable persistent field */
    private String panel_c_nr;

    /** nullable persistent field */
    private Integer i_gridx;

    /** nullable persistent field */
    private Integer i_gridy;

    /** nullable persistent field */
    private Short b_mandantory;

    /** nullable persistent field */
    private Integer artgru_i_id;

    /** full constructor */
    public FLRPanelbeschreibung(String mandant_c_nr, String c_name, String c_typ, String c_fill, String c_anchor, String panel_c_nr, Integer i_gridx, Integer i_gridy, Short b_mandantory, Integer artgru_i_id) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_name = c_name;
        this.c_typ = c_typ;
        this.c_fill = c_fill;
        this.c_anchor = c_anchor;
        this.panel_c_nr = panel_c_nr;
        this.i_gridx = i_gridx;
        this.i_gridy = i_gridy;
        this.b_mandantory = b_mandantory;
        this.artgru_i_id = artgru_i_id;
    }

    /** default constructor */
    public FLRPanelbeschreibung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getC_fill() {
        return this.c_fill;
    }

    public void setC_fill(String c_fill) {
        this.c_fill = c_fill;
    }

    public String getC_anchor() {
        return this.c_anchor;
    }

    public void setC_anchor(String c_anchor) {
        this.c_anchor = c_anchor;
    }

    public String getPanel_c_nr() {
        return this.panel_c_nr;
    }

    public void setPanel_c_nr(String panel_c_nr) {
        this.panel_c_nr = panel_c_nr;
    }

    public Integer getI_gridx() {
        return this.i_gridx;
    }

    public void setI_gridx(Integer i_gridx) {
        this.i_gridx = i_gridx;
    }

    public Integer getI_gridy() {
        return this.i_gridy;
    }

    public void setI_gridy(Integer i_gridy) {
        this.i_gridy = i_gridy;
    }

    public Short getB_mandantory() {
        return this.b_mandantory;
    }

    public void setB_mandantory(Short b_mandantory) {
        this.b_mandantory = b_mandantory;
    }

    public Integer getArtgru_i_id() {
        return this.artgru_i_id;
    }

    public void setArtgru_i_id(Integer artgru_i_id) {
        this.artgru_i_id = artgru_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
