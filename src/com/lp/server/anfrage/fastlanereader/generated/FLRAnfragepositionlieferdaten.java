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
package com.lp.server.anfrage.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAnfragepositionlieferdaten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_anlieferzeit;

    /** nullable persistent field */
    private BigDecimal n_anliefermenge;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisminusrabatt;

    /** nullable persistent field */
    private com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition;

    /** full constructor */
    public FLRAnfragepositionlieferdaten(Integer i_anlieferzeit, BigDecimal n_anliefermenge, BigDecimal n_nettogesamtpreis, BigDecimal n_nettogesamtpreisminusrabatt, com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition) {
        this.i_anlieferzeit = i_anlieferzeit;
        this.n_anliefermenge = n_anliefermenge;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_nettogesamtpreisminusrabatt = n_nettogesamtpreisminusrabatt;
        this.flranfrageposition = flranfrageposition;
    }

    /** default constructor */
    public FLRAnfragepositionlieferdaten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_anlieferzeit() {
        return this.i_anlieferzeit;
    }

    public void setI_anlieferzeit(Integer i_anlieferzeit) {
        this.i_anlieferzeit = i_anlieferzeit;
    }

    public BigDecimal getN_anliefermenge() {
        return this.n_anliefermenge;
    }

    public void setN_anliefermenge(BigDecimal n_anliefermenge) {
        this.n_anliefermenge = n_anliefermenge;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public BigDecimal getN_nettogesamtpreisminusrabatt() {
        return this.n_nettogesamtpreisminusrabatt;
    }

    public void setN_nettogesamtpreisminusrabatt(BigDecimal n_nettogesamtpreisminusrabatt) {
        this.n_nettogesamtpreisminusrabatt = n_nettogesamtpreisminusrabatt;
    }

    public com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition getFlranfrageposition() {
        return this.flranfrageposition;
    }

    public void setFlranfrageposition(com.lp.server.anfrage.fastlanereader.generated.FLRAnfrageposition flranfrageposition) {
        this.flranfrageposition = flranfrageposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
