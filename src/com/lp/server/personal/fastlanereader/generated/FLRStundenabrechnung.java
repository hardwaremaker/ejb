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
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStundenabrechnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private BigDecimal n_mehrstunden;

    /** nullable persistent field */
    private BigDecimal n_normalstunden;

    /** nullable persistent field */
    private BigDecimal n_uestfrei50;

    /** nullable persistent field */
    private BigDecimal n_uestpflichtig50;

    /** nullable persistent field */
    private BigDecimal n_uestfrei100;

    /** nullable persistent field */
    private BigDecimal n_uest200;

    /** nullable persistent field */
    private BigDecimal n_uestpflichtig100;

    /** nullable persistent field */
    private BigDecimal n_gutstunden;

    /** nullable persistent field */
    private BigDecimal n_qualifikationspraemie;

    /** full constructor */
    public FLRStundenabrechnung(Integer personal_i_id, Date t_datum, BigDecimal n_mehrstunden, BigDecimal n_normalstunden, BigDecimal n_uestfrei50, BigDecimal n_uestpflichtig50, BigDecimal n_uestfrei100, BigDecimal n_uest200, BigDecimal n_uestpflichtig100, BigDecimal n_gutstunden, BigDecimal n_qualifikationspraemie) {
        this.personal_i_id = personal_i_id;
        this.t_datum = t_datum;
        this.n_mehrstunden = n_mehrstunden;
        this.n_normalstunden = n_normalstunden;
        this.n_uestfrei50 = n_uestfrei50;
        this.n_uestpflichtig50 = n_uestpflichtig50;
        this.n_uestfrei100 = n_uestfrei100;
        this.n_uest200 = n_uest200;
        this.n_uestpflichtig100 = n_uestpflichtig100;
        this.n_gutstunden = n_gutstunden;
        this.n_qualifikationspraemie = n_qualifikationspraemie;
    }

    /** default constructor */
    public FLRStundenabrechnung() {
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

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public BigDecimal getN_mehrstunden() {
        return this.n_mehrstunden;
    }

    public void setN_mehrstunden(BigDecimal n_mehrstunden) {
        this.n_mehrstunden = n_mehrstunden;
    }

    public BigDecimal getN_normalstunden() {
        return this.n_normalstunden;
    }

    public void setN_normalstunden(BigDecimal n_normalstunden) {
        this.n_normalstunden = n_normalstunden;
    }

    public BigDecimal getN_uestfrei50() {
        return this.n_uestfrei50;
    }

    public void setN_uestfrei50(BigDecimal n_uestfrei50) {
        this.n_uestfrei50 = n_uestfrei50;
    }

    public BigDecimal getN_uestpflichtig50() {
        return this.n_uestpflichtig50;
    }

    public void setN_uestpflichtig50(BigDecimal n_uestpflichtig50) {
        this.n_uestpflichtig50 = n_uestpflichtig50;
    }

    public BigDecimal getN_uestfrei100() {
        return this.n_uestfrei100;
    }

    public void setN_uestfrei100(BigDecimal n_uestfrei100) {
        this.n_uestfrei100 = n_uestfrei100;
    }

    public BigDecimal getN_uest200() {
        return this.n_uest200;
    }

    public void setN_uest200(BigDecimal n_uest200) {
        this.n_uest200 = n_uest200;
    }

    public BigDecimal getN_uestpflichtig100() {
        return this.n_uestpflichtig100;
    }

    public void setN_uestpflichtig100(BigDecimal n_uestpflichtig100) {
        this.n_uestpflichtig100 = n_uestpflichtig100;
    }

    public BigDecimal getN_gutstunden() {
        return this.n_gutstunden;
    }

    public void setN_gutstunden(BigDecimal n_gutstunden) {
        this.n_gutstunden = n_gutstunden;
    }

    public BigDecimal getN_qualifikationspraemie() {
        return this.n_qualifikationspraemie;
    }

    public void setN_qualifikationspraemie(BigDecimal n_qualifikationspraemie) {
        this.n_qualifikationspraemie = n_qualifikationspraemie;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
