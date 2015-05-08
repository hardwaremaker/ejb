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
package com.lp.server.bestellung.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWareneingangspositionen implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_geliefertemenge;

    /** nullable persistent field */
    private BigDecimal n_gelieferterpreis;

    /** nullable persistent field */
    private BigDecimal n_einstandspreis;

    /** nullable persistent field */
    private Integer bestellposition_i_id;

    /** nullable persistent field */
    private Integer wareneingang_i_id;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private Short b_preiseerfasst;

    /** nullable persistent field */
    private Short b_verraeumt;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang flrwareneingang;

    /** full constructor */
    public FLRWareneingangspositionen(BigDecimal n_geliefertemenge, BigDecimal n_gelieferterpreis, BigDecimal n_einstandspreis, Integer bestellposition_i_id, Integer wareneingang_i_id, Date t_manuellerledigt, Short b_preiseerfasst, Short b_verraeumt, String x_internerkommentar, com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition, com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang flrwareneingang) {
        this.n_geliefertemenge = n_geliefertemenge;
        this.n_gelieferterpreis = n_gelieferterpreis;
        this.n_einstandspreis = n_einstandspreis;
        this.bestellposition_i_id = bestellposition_i_id;
        this.wareneingang_i_id = wareneingang_i_id;
        this.t_manuellerledigt = t_manuellerledigt;
        this.b_preiseerfasst = b_preiseerfasst;
        this.b_verraeumt = b_verraeumt;
        this.x_internerkommentar = x_internerkommentar;
        this.flrbestellposition = flrbestellposition;
        this.flrwareneingang = flrwareneingang;
    }

    /** default constructor */
    public FLRWareneingangspositionen() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_geliefertemenge() {
        return this.n_geliefertemenge;
    }

    public void setN_geliefertemenge(BigDecimal n_geliefertemenge) {
        this.n_geliefertemenge = n_geliefertemenge;
    }

    public BigDecimal getN_gelieferterpreis() {
        return this.n_gelieferterpreis;
    }

    public void setN_gelieferterpreis(BigDecimal n_gelieferterpreis) {
        this.n_gelieferterpreis = n_gelieferterpreis;
    }

    public BigDecimal getN_einstandspreis() {
        return this.n_einstandspreis;
    }

    public void setN_einstandspreis(BigDecimal n_einstandspreis) {
        this.n_einstandspreis = n_einstandspreis;
    }

    public Integer getBestellposition_i_id() {
        return this.bestellposition_i_id;
    }

    public void setBestellposition_i_id(Integer bestellposition_i_id) {
        this.bestellposition_i_id = bestellposition_i_id;
    }

    public Integer getWareneingang_i_id() {
        return this.wareneingang_i_id;
    }

    public void setWareneingang_i_id(Integer wareneingang_i_id) {
        this.wareneingang_i_id = wareneingang_i_id;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
    }

    public Short getB_preiseerfasst() {
        return this.b_preiseerfasst;
    }

    public void setB_preiseerfasst(Short b_preiseerfasst) {
        this.b_preiseerfasst = b_preiseerfasst;
    }

    public Short getB_verraeumt() {
        return this.b_verraeumt;
    }

    public void setB_verraeumt(Short b_verraeumt) {
        this.b_verraeumt = b_verraeumt;
    }

    public String getX_internerkommentar() {
        return this.x_internerkommentar;
    }

    public void setX_internerkommentar(String x_internerkommentar) {
        this.x_internerkommentar = x_internerkommentar;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang getFlrwareneingang() {
        return this.flrwareneingang;
    }

    public void setFlrwareneingang(com.lp.server.bestellung.fastlanereader.generated.FLRWareneingang flrwareneingang) {
        this.flrwareneingang = flrwareneingang;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
