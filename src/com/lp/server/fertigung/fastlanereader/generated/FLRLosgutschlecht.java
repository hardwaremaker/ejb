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
package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.reklamation.fastlanereader.generated.FLRFehler;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLosgutschlecht implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer lossollarbeitsplan_i_id;

    /** nullable persistent field */
    private BigDecimal n_gut;

    /** nullable persistent field */
    private BigDecimal n_schlecht;

    /** nullable persistent field */
    private BigDecimal n_inarbeit;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private FLRZeitdaten flrzeitdaten;

    /** nullable persistent field */
    private FLRMaschinenzeitdaten flrmaschinenzeitdaten;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan;

    /** nullable persistent field */
    private FLRFehler flrfehler;

    /** full constructor */
    public FLRLosgutschlecht(Integer lossollarbeitsplan_i_id, BigDecimal n_gut, BigDecimal n_schlecht, BigDecimal n_inarbeit, String c_kommentar, FLRZeitdaten flrzeitdaten, FLRMaschinenzeitdaten flrmaschinenzeitdaten, com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan, FLRFehler flrfehler) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
        this.n_gut = n_gut;
        this.n_schlecht = n_schlecht;
        this.n_inarbeit = n_inarbeit;
        this.c_kommentar = c_kommentar;
        this.flrzeitdaten = flrzeitdaten;
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
        this.flrfehler = flrfehler;
    }

    /** default constructor */
    public FLRLosgutschlecht() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLossollarbeitsplan_i_id() {
        return this.lossollarbeitsplan_i_id;
    }

    public void setLossollarbeitsplan_i_id(Integer lossollarbeitsplan_i_id) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
    }

    public BigDecimal getN_gut() {
        return this.n_gut;
    }

    public void setN_gut(BigDecimal n_gut) {
        this.n_gut = n_gut;
    }

    public BigDecimal getN_schlecht() {
        return this.n_schlecht;
    }

    public void setN_schlecht(BigDecimal n_schlecht) {
        this.n_schlecht = n_schlecht;
    }

    public BigDecimal getN_inarbeit() {
        return this.n_inarbeit;
    }

    public void setN_inarbeit(BigDecimal n_inarbeit) {
        this.n_inarbeit = n_inarbeit;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public FLRZeitdaten getFlrzeitdaten() {
        return this.flrzeitdaten;
    }

    public void setFlrzeitdaten(FLRZeitdaten flrzeitdaten) {
        this.flrzeitdaten = flrzeitdaten;
    }

    public FLRMaschinenzeitdaten getFlrmaschinenzeitdaten() {
        return this.flrmaschinenzeitdaten;
    }

    public void setFlrmaschinenzeitdaten(FLRMaschinenzeitdaten flrmaschinenzeitdaten) {
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan getFlrlossollarbeitsplan() {
        return this.flrlossollarbeitsplan;
    }

    public void setFlrlossollarbeitsplan(com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    public FLRFehler getFlrfehler() {
        return this.flrfehler;
    }

    public void setFlrfehler(FLRFehler flrfehler) {
        this.flrfehler = flrfehler;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
