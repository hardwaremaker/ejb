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
package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLossollmaterial implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer i_beginnterminoffset;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_sollpreis;

    /** nullable persistent field */
    private Short b_nachtraeglich;

    /** nullable persistent field */
    private Integer montageart_i_id;

    /** nullable persistent field */
    private Integer lossollmaterial_i_id_original;

    /** nullable persistent field */
    private Integer i_lfdnummer;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** persistent field */
    private Set istmaterialset;

    /** full constructor */
    public FLRLossollmaterial(Integer los_i_id, Integer i_sort, Integer i_beginnterminoffset, BigDecimal n_menge, BigDecimal n_sollpreis, Short b_nachtraeglich, Integer montageart_i_id, Integer lossollmaterial_i_id_original, Integer i_lfdnummer, Date t_aendern, FLRArtikel flrartikel, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, Set istmaterialset) {
        this.los_i_id = los_i_id;
        this.i_sort = i_sort;
        this.i_beginnterminoffset = i_beginnterminoffset;
        this.n_menge = n_menge;
        this.n_sollpreis = n_sollpreis;
        this.b_nachtraeglich = b_nachtraeglich;
        this.montageart_i_id = montageart_i_id;
        this.lossollmaterial_i_id_original = lossollmaterial_i_id_original;
        this.i_lfdnummer = i_lfdnummer;
        this.t_aendern = t_aendern;
        this.flrartikel = flrartikel;
        this.flrlos = flrlos;
        this.istmaterialset = istmaterialset;
    }

    /** default constructor */
    public FLRLossollmaterial() {
    }

    /** minimal constructor */
    public FLRLossollmaterial(Set istmaterialset) {
        this.istmaterialset = istmaterialset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getI_beginnterminoffset() {
        return this.i_beginnterminoffset;
    }

    public void setI_beginnterminoffset(Integer i_beginnterminoffset) {
        this.i_beginnterminoffset = i_beginnterminoffset;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_sollpreis() {
        return this.n_sollpreis;
    }

    public void setN_sollpreis(BigDecimal n_sollpreis) {
        this.n_sollpreis = n_sollpreis;
    }

    public Short getB_nachtraeglich() {
        return this.b_nachtraeglich;
    }

    public void setB_nachtraeglich(Short b_nachtraeglich) {
        this.b_nachtraeglich = b_nachtraeglich;
    }

    public Integer getMontageart_i_id() {
        return this.montageart_i_id;
    }

    public void setMontageart_i_id(Integer montageart_i_id) {
        this.montageart_i_id = montageart_i_id;
    }

    public Integer getLossollmaterial_i_id_original() {
        return this.lossollmaterial_i_id_original;
    }

    public void setLossollmaterial_i_id_original(Integer lossollmaterial_i_id_original) {
        this.lossollmaterial_i_id_original = lossollmaterial_i_id_original;
    }

    public Integer getI_lfdnummer() {
        return this.i_lfdnummer;
    }

    public void setI_lfdnummer(Integer i_lfdnummer) {
        this.i_lfdnummer = i_lfdnummer;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public Set getIstmaterialset() {
        return this.istmaterialset;
    }

    public void setIstmaterialset(Set istmaterialset) {
        this.istmaterialset = istmaterialset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
