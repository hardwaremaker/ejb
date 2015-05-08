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
 *******************************************************************************/
package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAgstklarbeitsplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_arbeitsgang;

    /** nullable persistent field */
    private Integer i_unterarbeitsgang;

    /** nullable persistent field */
    private Integer i_aufspannung;

    /** nullable persistent field */
    private Long l_stueckzeit;

    /** nullable persistent field */
    private Long l_ruestzeit;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private Short b_nurmaschinenzeit;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** full constructor */
    public FLRAgstklarbeitsplan(Integer i_arbeitsgang, Integer i_unterarbeitsgang, Integer i_aufspannung, Long l_stueckzeit, Long l_ruestzeit, String c_kommentar, Short b_nurmaschinenzeit, Integer maschine_i_id, Integer agstkl_i_id, com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl, FLRArtikel flrartikel, FLRMaschine flrmaschine) {
        this.i_arbeitsgang = i_arbeitsgang;
        this.i_unterarbeitsgang = i_unterarbeitsgang;
        this.i_aufspannung = i_aufspannung;
        this.l_stueckzeit = l_stueckzeit;
        this.l_ruestzeit = l_ruestzeit;
        this.c_kommentar = c_kommentar;
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
        this.maschine_i_id = maschine_i_id;
        this.agstkl_i_id = agstkl_i_id;
        this.flragstkl = flragstkl;
        this.flrartikel = flrartikel;
        this.flrmaschine = flrmaschine;
    }

    /** default constructor */
    public FLRAgstklarbeitsplan() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_arbeitsgang() {
        return this.i_arbeitsgang;
    }

    public void setI_arbeitsgang(Integer i_arbeitsgang) {
        this.i_arbeitsgang = i_arbeitsgang;
    }

    public Integer getI_unterarbeitsgang() {
        return this.i_unterarbeitsgang;
    }

    public void setI_unterarbeitsgang(Integer i_unterarbeitsgang) {
        this.i_unterarbeitsgang = i_unterarbeitsgang;
    }

    public Integer getI_aufspannung() {
        return this.i_aufspannung;
    }

    public void setI_aufspannung(Integer i_aufspannung) {
        this.i_aufspannung = i_aufspannung;
    }

    public Long getL_stueckzeit() {
        return this.l_stueckzeit;
    }

    public void setL_stueckzeit(Long l_stueckzeit) {
        this.l_stueckzeit = l_stueckzeit;
    }

    public Long getL_ruestzeit() {
        return this.l_ruestzeit;
    }

    public void setL_ruestzeit(Long l_ruestzeit) {
        this.l_ruestzeit = l_ruestzeit;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public Short getB_nurmaschinenzeit() {
        return this.b_nurmaschinenzeit;
    }

    public void setB_nurmaschinenzeit(Short b_nurmaschinenzeit) {
        this.b_nurmaschinenzeit = b_nurmaschinenzeit;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
