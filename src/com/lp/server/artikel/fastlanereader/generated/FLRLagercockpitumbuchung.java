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

import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLagercockpitumbuchung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal diff;

    /** nullable persistent field */
    private BigDecimal lagerstand;

    /** nullable persistent field */
    private BigDecimal lager_i_id_lagerplatz;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager_lagerplatz;

    /** full constructor */
    public FLRLagercockpitumbuchung(BigDecimal n_menge, BigDecimal diff, BigDecimal lagerstand, BigDecimal lager_i_id_lagerplatz, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, FLRLos flrlos, com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager_lagerplatz) {
        this.n_menge = n_menge;
        this.diff = diff;
        this.lagerstand = lagerstand;
        this.lager_i_id_lagerplatz = lager_i_id_lagerplatz;
        this.flrartikel = flrartikel;
        this.flrlos = flrlos;
        this.flrlager_lagerplatz = flrlager_lagerplatz;
    }

    /** default constructor */
    public FLRLagercockpitumbuchung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getDiff() {
        return this.diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }

    public BigDecimal getLagerstand() {
        return this.lagerstand;
    }

    public void setLagerstand(BigDecimal lagerstand) {
        this.lagerstand = lagerstand;
    }

    public BigDecimal getLager_i_id_lagerplatz() {
        return this.lager_i_id_lagerplatz;
    }

    public void setLager_i_id_lagerplatz(BigDecimal lager_i_id_lagerplatz) {
        this.lager_i_id_lagerplatz = lager_i_id_lagerplatz;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager_lagerplatz() {
        return this.flrlager_lagerplatz;
    }

    public void setFlrlager_lagerplatz(com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager_lagerplatz) {
        this.flrlager_lagerplatz = flrlager_lagerplatz;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
