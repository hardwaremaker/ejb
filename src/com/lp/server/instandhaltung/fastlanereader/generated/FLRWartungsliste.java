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
package com.lp.server.instandhaltung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWartungsliste implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_veraltet;

    /** nullable persistent field */
    private Date t_veraltet;

    /** nullable persistent field */
    private Integer geraet_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String x_bemerkung;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Short b_verrechenbar;

    /** nullable persistent field */
    private Short b_wartungsmaterial;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLRWartungsliste(String c_bez, String c_veraltet, Date t_veraltet, Integer geraet_i_id, Integer i_sort, String x_bemerkung, BigDecimal n_menge, Short b_verrechenbar, Short b_wartungsmaterial, com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet, FLRArtikel flrartikel) {
        this.c_bez = c_bez;
        this.c_veraltet = c_veraltet;
        this.t_veraltet = t_veraltet;
        this.geraet_i_id = geraet_i_id;
        this.i_sort = i_sort;
        this.x_bemerkung = x_bemerkung;
        this.n_menge = n_menge;
        this.b_verrechenbar = b_verrechenbar;
        this.b_wartungsmaterial = b_wartungsmaterial;
        this.flrgeraet = flrgeraet;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRWartungsliste() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_veraltet() {
        return this.c_veraltet;
    }

    public void setC_veraltet(String c_veraltet) {
        this.c_veraltet = c_veraltet;
    }

    public Date getT_veraltet() {
        return this.t_veraltet;
    }

    public void setT_veraltet(Date t_veraltet) {
        this.t_veraltet = t_veraltet;
    }

    public Integer getGeraet_i_id() {
        return this.geraet_i_id;
    }

    public void setGeraet_i_id(Integer geraet_i_id) {
        this.geraet_i_id = geraet_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getX_bemerkung() {
        return this.x_bemerkung;
    }

    public void setX_bemerkung(String x_bemerkung) {
        this.x_bemerkung = x_bemerkung;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Short getB_verrechenbar() {
        return this.b_verrechenbar;
    }

    public void setB_verrechenbar(Short b_verrechenbar) {
        this.b_verrechenbar = b_verrechenbar;
    }

    public Short getB_wartungsmaterial() {
        return this.b_wartungsmaterial;
    }

    public void setB_wartungsmaterial(Short b_wartungsmaterial) {
        this.b_wartungsmaterial = b_wartungsmaterial;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet getFlrgeraet() {
        return this.flrgeraet;
    }

    public void setFlrgeraet(com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet) {
        this.flrgeraet = flrgeraet;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
