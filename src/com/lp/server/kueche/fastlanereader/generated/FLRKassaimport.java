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
package com.lp.server.kueche.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKassaimport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer speiseplan_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_preis;

    /** nullable persistent field */
    private Date t_kassa;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplan flrspeiseplan;

    /** full constructor */
    public FLRKassaimport(Integer artikel_i_id, Integer speiseplan_i_id, Integer kunde_i_id, BigDecimal n_menge, BigDecimal n_preis, Date t_kassa, FLRArtikelliste flrartikel, FLRKunde flrkunde, com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplan flrspeiseplan) {
        this.artikel_i_id = artikel_i_id;
        this.speiseplan_i_id = speiseplan_i_id;
        this.kunde_i_id = kunde_i_id;
        this.n_menge = n_menge;
        this.n_preis = n_preis;
        this.t_kassa = t_kassa;
        this.flrartikel = flrartikel;
        this.flrkunde = flrkunde;
        this.flrspeiseplan = flrspeiseplan;
    }

    /** default constructor */
    public FLRKassaimport() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getSpeiseplan_i_id() {
        return this.speiseplan_i_id;
    }

    public void setSpeiseplan_i_id(Integer speiseplan_i_id) {
        this.speiseplan_i_id = speiseplan_i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public BigDecimal getN_preis() {
        return this.n_preis;
    }

    public void setN_preis(BigDecimal n_preis) {
        this.n_preis = n_preis;
    }

    public Date getT_kassa() {
        return this.t_kassa;
    }

    public void setT_kassa(Date t_kassa) {
        this.t_kassa = t_kassa;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplan getFlrspeiseplan() {
        return this.flrspeiseplan;
    }

    public void setFlrspeiseplan(com.lp.server.kueche.fastlanereader.generated.FLRSpeiseplan flrspeiseplan) {
        this.flrspeiseplan = flrspeiseplan;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
