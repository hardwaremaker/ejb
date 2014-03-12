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
package com.lp.server.inserat.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRInseratartikel implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer inserat_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_ek;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis_vk;

    /** nullable persistent field */
    private Integer bestellposition_i_id;

    /** nullable persistent field */
    private com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRBestellposition flrbestellposition;

    /** full constructor */
    public FLRInseratartikel(Integer inserat_i_id, BigDecimal n_menge, BigDecimal n_nettoeinzelpreis_ek, BigDecimal n_nettoeinzelpreis_vk, Integer bestellposition_i_id, com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat, FLRArtikel flrartikel, FLRBestellposition flrbestellposition) {
        this.inserat_i_id = inserat_i_id;
        this.n_menge = n_menge;
        this.n_nettoeinzelpreis_ek = n_nettoeinzelpreis_ek;
        this.n_nettoeinzelpreis_vk = n_nettoeinzelpreis_vk;
        this.bestellposition_i_id = bestellposition_i_id;
        this.flrinserat = flrinserat;
        this.flrartikel = flrartikel;
        this.flrbestellposition = flrbestellposition;
    }

    /** default constructor */
    public FLRInseratartikel() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getInserat_i_id() {
        return this.inserat_i_id;
    }

    public void setInserat_i_id(Integer inserat_i_id) {
        this.inserat_i_id = inserat_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_nettoeinzelpreis_ek() {
        return this.n_nettoeinzelpreis_ek;
    }

    public void setN_nettoeinzelpreis_ek(BigDecimal n_nettoeinzelpreis_ek) {
        this.n_nettoeinzelpreis_ek = n_nettoeinzelpreis_ek;
    }

    public BigDecimal getN_nettoeinzelpreis_vk() {
        return this.n_nettoeinzelpreis_vk;
    }

    public void setN_nettoeinzelpreis_vk(BigDecimal n_nettoeinzelpreis_vk) {
        this.n_nettoeinzelpreis_vk = n_nettoeinzelpreis_vk;
    }

    public Integer getBestellposition_i_id() {
        return this.bestellposition_i_id;
    }

    public void setBestellposition_i_id(Integer bestellposition_i_id) {
        this.bestellposition_i_id = bestellposition_i_id;
    }

    public com.lp.server.inserat.fastlanereader.generated.FLRInserat getFlrinserat() {
        return this.flrinserat;
    }

    public void setFlrinserat(com.lp.server.inserat.fastlanereader.generated.FLRInserat flrinserat) {
        this.flrinserat = flrinserat;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRBestellposition getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(FLRBestellposition flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
