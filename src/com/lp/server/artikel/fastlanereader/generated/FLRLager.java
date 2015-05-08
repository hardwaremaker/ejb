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
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLager implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_nr;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String lagerart_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_konsignationslager;

    /** nullable persistent field */
    private Integer i_loslagersort;

    /** persistent field */
    private Set artikellagerset;

    /** full constructor */
    public FLRLager(String c_nr, String mandant_c_nr, String lagerart_c_nr, Short b_versteckt, Short b_konsignationslager, Integer i_loslagersort, Set artikellagerset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.lagerart_c_nr = lagerart_c_nr;
        this.b_versteckt = b_versteckt;
        this.b_konsignationslager = b_konsignationslager;
        this.i_loslagersort = i_loslagersort;
        this.artikellagerset = artikellagerset;
    }

    /** default constructor */
    public FLRLager() {
    }

    /** minimal constructor */
    public FLRLager(String c_nr, String mandant_c_nr, Set artikellagerset) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.artikellagerset = artikellagerset;
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

    public String getLagerart_c_nr() {
        return this.lagerart_c_nr;
    }

    public void setLagerart_c_nr(String lagerart_c_nr) {
        this.lagerart_c_nr = lagerart_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_konsignationslager() {
        return this.b_konsignationslager;
    }

    public void setB_konsignationslager(Short b_konsignationslager) {
        this.b_konsignationslager = b_konsignationslager;
    }

    public Integer getI_loslagersort() {
        return this.i_loslagersort;
    }

    public void setI_loslagersort(Integer i_loslagersort) {
        this.i_loslagersort = i_loslagersort;
    }

    public Set getArtikellagerset() {
        return this.artikellagerset;
    }

    public void setArtikellagerset(Set artikellagerset) {
        this.artikellagerset = artikellagerset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
