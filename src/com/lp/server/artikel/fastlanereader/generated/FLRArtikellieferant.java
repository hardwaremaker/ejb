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
package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private String c_artikelnrlieferant;

    /** nullable persistent field */
    private Date t_preisgueltigab;

    /** nullable persistent field */
    private String c_bezbeilieferant;

    /** nullable persistent field */
    private BigDecimal n_einzelpreis;

    /** nullable persistent field */
    private BigDecimal n_nettopreis;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer i_wiederbeschaffungszeit;

    /** nullable persistent field */
    private Short b_webshop;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** persistent field */
    private Set staffelset;

    /** full constructor */
    public FLRArtikellieferant(Integer artikel_i_id, Integer lieferant_i_id, String c_artikelnrlieferant, Date t_preisgueltigab, String c_bezbeilieferant, BigDecimal n_einzelpreis, BigDecimal n_nettopreis, Integer i_sort, Integer i_wiederbeschaffungszeit, Short b_webshop, FLRLieferant flrlieferant, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, Set staffelset) {
        this.artikel_i_id = artikel_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.c_artikelnrlieferant = c_artikelnrlieferant;
        this.t_preisgueltigab = t_preisgueltigab;
        this.c_bezbeilieferant = c_bezbeilieferant;
        this.n_einzelpreis = n_einzelpreis;
        this.n_nettopreis = n_nettopreis;
        this.i_sort = i_sort;
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
        this.b_webshop = b_webshop;
        this.flrlieferant = flrlieferant;
        this.flrartikel = flrartikel;
        this.staffelset = staffelset;
    }

    /** default constructor */
    public FLRArtikellieferant() {
    }

    /** minimal constructor */
    public FLRArtikellieferant(Set staffelset) {
        this.staffelset = staffelset;
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

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public String getC_artikelnrlieferant() {
        return this.c_artikelnrlieferant;
    }

    public void setC_artikelnrlieferant(String c_artikelnrlieferant) {
        this.c_artikelnrlieferant = c_artikelnrlieferant;
    }

    public Date getT_preisgueltigab() {
        return this.t_preisgueltigab;
    }

    public void setT_preisgueltigab(Date t_preisgueltigab) {
        this.t_preisgueltigab = t_preisgueltigab;
    }

    public String getC_bezbeilieferant() {
        return this.c_bezbeilieferant;
    }

    public void setC_bezbeilieferant(String c_bezbeilieferant) {
        this.c_bezbeilieferant = c_bezbeilieferant;
    }

    public BigDecimal getN_einzelpreis() {
        return this.n_einzelpreis;
    }

    public void setN_einzelpreis(BigDecimal n_einzelpreis) {
        this.n_einzelpreis = n_einzelpreis;
    }

    public BigDecimal getN_nettopreis() {
        return this.n_nettopreis;
    }

    public void setN_nettopreis(BigDecimal n_nettopreis) {
        this.n_nettopreis = n_nettopreis;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getI_wiederbeschaffungszeit() {
        return this.i_wiederbeschaffungszeit;
    }

    public void setI_wiederbeschaffungszeit(Integer i_wiederbeschaffungszeit) {
        this.i_wiederbeschaffungszeit = i_wiederbeschaffungszeit;
    }

    public Short getB_webshop() {
        return this.b_webshop;
    }

    public void setB_webshop(Short b_webshop) {
        this.b_webshop = b_webshop;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public Set getStaffelset() {
        return this.staffelset;
    }

    public void setStaffelset(Set staffelset) {
        this.staffelset = staffelset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
