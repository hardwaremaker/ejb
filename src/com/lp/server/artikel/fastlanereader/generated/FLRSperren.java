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

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSperren implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Short b_gesperrt;

    /** nullable persistent field */
    private Short b_gesperrteinkauf;

    /** nullable persistent field */
    private Short b_gesperrtverkauf;

    /** nullable persistent field */
    private Short b_gesperrtlos;

    /** nullable persistent field */
    private Short b_gesperrtstueckliste;

    /** nullable persistent field */
    private Short b_durchfertigung;

    /** full constructor */
    public FLRSperren(String mandant_c_nr, String c_bez, Short b_gesperrt, Short b_gesperrteinkauf, Short b_gesperrtverkauf, Short b_gesperrtlos, Short b_gesperrtstueckliste, Short b_durchfertigung) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_bez = c_bez;
        this.b_gesperrt = b_gesperrt;
        this.b_gesperrteinkauf = b_gesperrteinkauf;
        this.b_gesperrtverkauf = b_gesperrtverkauf;
        this.b_gesperrtlos = b_gesperrtlos;
        this.b_gesperrtstueckliste = b_gesperrtstueckliste;
        this.b_durchfertigung = b_durchfertigung;
    }

    /** default constructor */
    public FLRSperren() {
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Short getB_gesperrt() {
        return this.b_gesperrt;
    }

    public void setB_gesperrt(Short b_gesperrt) {
        this.b_gesperrt = b_gesperrt;
    }

    public Short getB_gesperrteinkauf() {
        return this.b_gesperrteinkauf;
    }

    public void setB_gesperrteinkauf(Short b_gesperrteinkauf) {
        this.b_gesperrteinkauf = b_gesperrteinkauf;
    }

    public Short getB_gesperrtverkauf() {
        return this.b_gesperrtverkauf;
    }

    public void setB_gesperrtverkauf(Short b_gesperrtverkauf) {
        this.b_gesperrtverkauf = b_gesperrtverkauf;
    }

    public Short getB_gesperrtlos() {
        return this.b_gesperrtlos;
    }

    public void setB_gesperrtlos(Short b_gesperrtlos) {
        this.b_gesperrtlos = b_gesperrtlos;
    }

    public Short getB_gesperrtstueckliste() {
        return this.b_gesperrtstueckliste;
    }

    public void setB_gesperrtstueckliste(Short b_gesperrtstueckliste) {
        this.b_gesperrtstueckliste = b_gesperrtstueckliste;
    }

    public Short getB_durchfertigung() {
        return this.b_durchfertigung;
    }

    public void setB_durchfertigung(Short b_durchfertigung) {
        this.b_durchfertigung = b_durchfertigung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
