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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGleitzeitsaldo implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer i_jahr;

    /** nullable persistent field */
    private Integer i_monat;

    /** nullable persistent field */
    private BigDecimal n_saldo;

    /** nullable persistent field */
    private BigDecimal n_saldomehrstunden;

    /** nullable persistent field */
    private BigDecimal n_saldouestfrei50;

    /** nullable persistent field */
    private BigDecimal n_saldouestpflichtig50;

    /** nullable persistent field */
    private BigDecimal n_saldouestfrei100;

    /** nullable persistent field */
    private BigDecimal n_saldouestpflichtig100;

    /** nullable persistent field */
    private BigDecimal n_saldouest200;

    /** full constructor */
    public FLRGleitzeitsaldo(Integer personal_i_id, Integer i_jahr, Integer i_monat, BigDecimal n_saldo, BigDecimal n_saldomehrstunden, BigDecimal n_saldouestfrei50, BigDecimal n_saldouestpflichtig50, BigDecimal n_saldouestfrei100, BigDecimal n_saldouestpflichtig100, BigDecimal n_saldouest200) {
        this.personal_i_id = personal_i_id;
        this.i_jahr = i_jahr;
        this.i_monat = i_monat;
        this.n_saldo = n_saldo;
        this.n_saldomehrstunden = n_saldomehrstunden;
        this.n_saldouestfrei50 = n_saldouestfrei50;
        this.n_saldouestpflichtig50 = n_saldouestpflichtig50;
        this.n_saldouestfrei100 = n_saldouestfrei100;
        this.n_saldouestpflichtig100 = n_saldouestpflichtig100;
        this.n_saldouest200 = n_saldouest200;
    }

    /** default constructor */
    public FLRGleitzeitsaldo() {
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

    public Integer getI_monat() {
        return this.i_monat;
    }

    public void setI_monat(Integer i_monat) {
        this.i_monat = i_monat;
    }

    public BigDecimal getN_saldo() {
        return this.n_saldo;
    }

    public void setN_saldo(BigDecimal n_saldo) {
        this.n_saldo = n_saldo;
    }

    public BigDecimal getN_saldomehrstunden() {
        return this.n_saldomehrstunden;
    }

    public void setN_saldomehrstunden(BigDecimal n_saldomehrstunden) {
        this.n_saldomehrstunden = n_saldomehrstunden;
    }

    public BigDecimal getN_saldouestfrei50() {
        return this.n_saldouestfrei50;
    }

    public void setN_saldouestfrei50(BigDecimal n_saldouestfrei50) {
        this.n_saldouestfrei50 = n_saldouestfrei50;
    }

    public BigDecimal getN_saldouestpflichtig50() {
        return this.n_saldouestpflichtig50;
    }

    public void setN_saldouestpflichtig50(BigDecimal n_saldouestpflichtig50) {
        this.n_saldouestpflichtig50 = n_saldouestpflichtig50;
    }

    public BigDecimal getN_saldouestfrei100() {
        return this.n_saldouestfrei100;
    }

    public void setN_saldouestfrei100(BigDecimal n_saldouestfrei100) {
        this.n_saldouestfrei100 = n_saldouestfrei100;
    }

    public BigDecimal getN_saldouestpflichtig100() {
        return this.n_saldouestpflichtig100;
    }

    public void setN_saldouestpflichtig100(BigDecimal n_saldouestpflichtig100) {
        this.n_saldouestpflichtig100 = n_saldouestpflichtig100;
    }

    public BigDecimal getN_saldouest200() {
        return this.n_saldouest200;
    }

    public void setN_saldouest200(BigDecimal n_saldouest200) {
        this.n_saldouest200 = n_saldouest200;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
