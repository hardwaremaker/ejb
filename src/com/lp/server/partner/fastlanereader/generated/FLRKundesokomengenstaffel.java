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
package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundesokomengenstaffel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kundesoko_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Double f_rabattsatz;

    /** nullable persistent field */
    private BigDecimal n_fixpreis;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKundesoko flrkundesoko;

    /** full constructor */
    public FLRKundesokomengenstaffel(Integer kundesoko_i_id, BigDecimal n_menge, Double f_rabattsatz, BigDecimal n_fixpreis, com.lp.server.partner.fastlanereader.generated.FLRKundesoko flrkundesoko) {
        this.kundesoko_i_id = kundesoko_i_id;
        this.n_menge = n_menge;
        this.f_rabattsatz = f_rabattsatz;
        this.n_fixpreis = n_fixpreis;
        this.flrkundesoko = flrkundesoko;
    }

    /** default constructor */
    public FLRKundesokomengenstaffel() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKundesoko_i_id() {
        return this.kundesoko_i_id;
    }

    public void setKundesoko_i_id(Integer kundesoko_i_id) {
        this.kundesoko_i_id = kundesoko_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
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

    public com.lp.server.partner.fastlanereader.generated.FLRKundesoko getFlrkundesoko() {
        return this.flrkundesoko;
    }

    public void setFlrkundesoko(com.lp.server.partner.fastlanereader.generated.FLRKundesoko flrkundesoko) {
        this.flrkundesoko = flrkundesoko;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
