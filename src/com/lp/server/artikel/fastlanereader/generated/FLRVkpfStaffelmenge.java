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
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVkpfStaffelmenge implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Short b_allepreislisten;

    /** nullable persistent field */
    private Double f_rabattsatz;

    /** nullable persistent field */
    private BigDecimal n_fixpreis;

    /** nullable persistent field */
    private Date t_preisgueltigab;

    /** nullable persistent field */
    private Date t_preisgueltigbis;

    /** nullable persistent field */
    private Integer vkpfartikelpreisliste_i_id;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrvkpfartikelpreisliste;

    /** full constructor */
    public FLRVkpfStaffelmenge(Integer artikel_i_id, BigDecimal n_menge, Short b_allepreislisten, Double f_rabattsatz, BigDecimal n_fixpreis, Date t_preisgueltigab, Date t_preisgueltigbis, Integer vkpfartikelpreisliste_i_id, com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrvkpfartikelpreisliste) {
        this.artikel_i_id = artikel_i_id;
        this.n_menge = n_menge;
        this.b_allepreislisten = b_allepreislisten;
        this.f_rabattsatz = f_rabattsatz;
        this.n_fixpreis = n_fixpreis;
        this.t_preisgueltigab = t_preisgueltigab;
        this.t_preisgueltigbis = t_preisgueltigbis;
        this.vkpfartikelpreisliste_i_id = vkpfartikelpreisliste_i_id;
        this.flrvkpfartikelpreisliste = flrvkpfartikelpreisliste;
    }

    /** default constructor */
    public FLRVkpfStaffelmenge() {
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

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Short getB_allepreislisten() {
        return this.b_allepreislisten;
    }

    public void setB_allepreislisten(Short b_allepreislisten) {
        this.b_allepreislisten = b_allepreislisten;
    }

    public Double getF_rabattsatz() {
        return this.f_rabattsatz;
    }

    public void setF_rabattsatz(Double f_rabattsatz) {
        this.f_rabattsatz = f_rabattsatz;
    }

    public BigDecimal getN_fixpreis() {
        return this.n_fixpreis;
    }

    public void setN_fixpreis(BigDecimal n_fixpreis) {
        this.n_fixpreis = n_fixpreis;
    }

    public Date getT_preisgueltigab() {
        return this.t_preisgueltigab;
    }

    public void setT_preisgueltigab(Date t_preisgueltigab) {
        this.t_preisgueltigab = t_preisgueltigab;
    }

    public Date getT_preisgueltigbis() {
        return this.t_preisgueltigbis;
    }

    public void setT_preisgueltigbis(Date t_preisgueltigbis) {
        this.t_preisgueltigbis = t_preisgueltigbis;
    }

    public Integer getVkpfartikelpreisliste_i_id() {
        return this.vkpfartikelpreisliste_i_id;
    }

    public void setVkpfartikelpreisliste_i_id(Integer vkpfartikelpreisliste_i_id) {
        this.vkpfartikelpreisliste_i_id = vkpfartikelpreisliste_i_id;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste getFlrvkpfartikelpreisliste() {
        return this.flrvkpfartikelpreisliste;
    }

    public void setFlrvkpfartikelpreisliste(com.lp.server.artikel.fastlanereader.generated.FLRVkpfartikelpreisliste flrvkpfartikelpreisliste) {
        this.flrvkpfartikelpreisliste = flrvkpfartikelpreisliste;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
