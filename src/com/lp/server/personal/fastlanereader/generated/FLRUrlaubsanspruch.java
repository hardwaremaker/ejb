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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRUrlaubsanspruch implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer i_jahr;

    /** nullable persistent field */
    private Double f_tage;

    /** nullable persistent field */
    private Double f_jahresurlaubinwochen;

    /** nullable persistent field */
    private Double f_stunden;

    /** nullable persistent field */
    private Double f_tagezusaetzlich;

    /** nullable persistent field */
    private Double f_stundenzusaetzlich;

    /** full constructor */
    public FLRUrlaubsanspruch(Integer personal_i_id, Integer i_jahr, Double f_tage, Double f_jahresurlaubinwochen, Double f_stunden, Double f_tagezusaetzlich, Double f_stundenzusaetzlich) {
        this.personal_i_id = personal_i_id;
        this.i_jahr = i_jahr;
        this.f_tage = f_tage;
        this.f_jahresurlaubinwochen = f_jahresurlaubinwochen;
        this.f_stunden = f_stunden;
        this.f_tagezusaetzlich = f_tagezusaetzlich;
        this.f_stundenzusaetzlich = f_stundenzusaetzlich;
    }

    /** default constructor */
    public FLRUrlaubsanspruch() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getI_jahr() {
        return this.i_jahr;
    }

    public void setI_jahr(Integer i_jahr) {
        this.i_jahr = i_jahr;
    }

    public Double getF_tage() {
        return this.f_tage;
    }

    public void setF_tage(Double f_tage) {
        this.f_tage = f_tage;
    }

    public Double getF_jahresurlaubinwochen() {
        return this.f_jahresurlaubinwochen;
    }

    public void setF_jahresurlaubinwochen(Double f_jahresurlaubinwochen) {
        this.f_jahresurlaubinwochen = f_jahresurlaubinwochen;
    }

    public Double getF_stunden() {
        return this.f_stunden;
    }

    public void setF_stunden(Double f_stunden) {
        this.f_stunden = f_stunden;
    }

    public Double getF_tagezusaetzlich() {
        return this.f_tagezusaetzlich;
    }

    public void setF_tagezusaetzlich(Double f_tagezusaetzlich) {
        this.f_tagezusaetzlich = f_tagezusaetzlich;
    }

    public Double getF_stundenzusaetzlich() {
        return this.f_stundenzusaetzlich;
    }

    public void setF_stundenzusaetzlich(Double f_stundenzusaetzlich) {
        this.f_stundenzusaetzlich = f_stundenzusaetzlich;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
