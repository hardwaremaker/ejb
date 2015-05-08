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

import com.lp.server.artikel.fastlanereader.generated.FLRHersteller;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGeraet implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String c_standort;

    /** nullable persistent field */
    private String x_bemerkung;

    /** nullable persistent field */
    private BigDecimal n_grenzwertmin;

    /** nullable persistent field */
    private BigDecimal n_grenzwertmax;

    /** nullable persistent field */
    private BigDecimal n_grenzwert;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRStandort flrstandort;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRHalle flrhalle;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRAnlage flranlage;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRIsmaschine flrismaschine;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraetetyp flrgeraetetyp;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRGewerk flrgewerk;

    /** nullable persistent field */
    private FLRHersteller flrhersteller;

    /** persistent field */
    private Set wartungslisteset;

    /** full constructor */
    public FLRGeraet(String c_bez, String mandant_c_nr, Short b_versteckt, String c_standort, String x_bemerkung, BigDecimal n_grenzwertmin, BigDecimal n_grenzwertmax, BigDecimal n_grenzwert, com.lp.server.instandhaltung.fastlanereader.generated.FLRStandort flrstandort, com.lp.server.instandhaltung.fastlanereader.generated.FLRHalle flrhalle, com.lp.server.instandhaltung.fastlanereader.generated.FLRAnlage flranlage, com.lp.server.instandhaltung.fastlanereader.generated.FLRIsmaschine flrismaschine, com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraetetyp flrgeraetetyp, com.lp.server.instandhaltung.fastlanereader.generated.FLRGewerk flrgewerk, FLRHersteller flrhersteller, Set wartungslisteset) {
        this.c_bez = c_bez;
        this.mandant_c_nr = mandant_c_nr;
        this.b_versteckt = b_versteckt;
        this.c_standort = c_standort;
        this.x_bemerkung = x_bemerkung;
        this.n_grenzwertmin = n_grenzwertmin;
        this.n_grenzwertmax = n_grenzwertmax;
        this.n_grenzwert = n_grenzwert;
        this.flrstandort = flrstandort;
        this.flrhalle = flrhalle;
        this.flranlage = flranlage;
        this.flrismaschine = flrismaschine;
        this.flrgeraetetyp = flrgeraetetyp;
        this.flrgewerk = flrgewerk;
        this.flrhersteller = flrhersteller;
        this.wartungslisteset = wartungslisteset;
    }

    /** default constructor */
    public FLRGeraet() {
    }

    /** minimal constructor */
    public FLRGeraet(Set wartungslisteset) {
        this.wartungslisteset = wartungslisteset;
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

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getC_standort() {
        return this.c_standort;
    }

    public void setC_standort(String c_standort) {
        this.c_standort = c_standort;
    }

    public String getX_bemerkung() {
        return this.x_bemerkung;
    }

    public void setX_bemerkung(String x_bemerkung) {
        this.x_bemerkung = x_bemerkung;
    }

    public BigDecimal getN_grenzwertmin() {
        return this.n_grenzwertmin;
    }

    public void setN_grenzwertmin(BigDecimal n_grenzwertmin) {
        this.n_grenzwertmin = n_grenzwertmin;
    }

    public BigDecimal getN_grenzwertmax() {
        return this.n_grenzwertmax;
    }

    public void setN_grenzwertmax(BigDecimal n_grenzwertmax) {
        this.n_grenzwertmax = n_grenzwertmax;
    }

    public BigDecimal getN_grenzwert() {
        return this.n_grenzwert;
    }

    public void setN_grenzwert(BigDecimal n_grenzwert) {
        this.n_grenzwert = n_grenzwert;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRStandort getFlrstandort() {
        return this.flrstandort;
    }

    public void setFlrstandort(com.lp.server.instandhaltung.fastlanereader.generated.FLRStandort flrstandort) {
        this.flrstandort = flrstandort;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRHalle getFlrhalle() {
        return this.flrhalle;
    }

    public void setFlrhalle(com.lp.server.instandhaltung.fastlanereader.generated.FLRHalle flrhalle) {
        this.flrhalle = flrhalle;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRAnlage getFlranlage() {
        return this.flranlage;
    }

    public void setFlranlage(com.lp.server.instandhaltung.fastlanereader.generated.FLRAnlage flranlage) {
        this.flranlage = flranlage;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRIsmaschine getFlrismaschine() {
        return this.flrismaschine;
    }

    public void setFlrismaschine(com.lp.server.instandhaltung.fastlanereader.generated.FLRIsmaschine flrismaschine) {
        this.flrismaschine = flrismaschine;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraetetyp getFlrgeraetetyp() {
        return this.flrgeraetetyp;
    }

    public void setFlrgeraetetyp(com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraetetyp flrgeraetetyp) {
        this.flrgeraetetyp = flrgeraetetyp;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRGewerk getFlrgewerk() {
        return this.flrgewerk;
    }

    public void setFlrgewerk(com.lp.server.instandhaltung.fastlanereader.generated.FLRGewerk flrgewerk) {
        this.flrgewerk = flrgewerk;
    }

    public FLRHersteller getFlrhersteller() {
        return this.flrhersteller;
    }

    public void setFlrhersteller(FLRHersteller flrhersteller) {
        this.flrhersteller = flrhersteller;
    }

    public Set getWartungslisteset() {
        return this.wartungslisteset;
    }

    public void setWartungslisteset(Set wartungslisteset) {
        this.wartungslisteset = wartungslisteset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
