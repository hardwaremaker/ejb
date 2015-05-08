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

import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtikellager implements Serializable {

    /** identifier field */
    private WwArtikellagerPK compId;

    /** persistent field */
    private BigDecimal n_lagerstand;

    /** persistent field */
    private BigDecimal n_gestehungspreis;

    /** persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste;

    /** full constructor */
    public FLRArtikellager(WwArtikellagerPK compId, BigDecimal n_lagerstand, BigDecimal n_gestehungspreis, String mandant_c_nr, com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager, com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel, com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste) {
        this.compId = compId;
        this.n_lagerstand = n_lagerstand;
        this.n_gestehungspreis = n_gestehungspreis;
        this.mandant_c_nr = mandant_c_nr;
        this.flrlager = flrlager;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
    }

    /** default constructor */
    public FLRArtikellager() {
    }

    /** minimal constructor */
    public FLRArtikellager(WwArtikellagerPK compId, BigDecimal n_lagerstand, BigDecimal n_gestehungspreis, String mandant_c_nr) {
        this.compId = compId;
        this.n_lagerstand = n_lagerstand;
        this.n_gestehungspreis = n_gestehungspreis;
        this.mandant_c_nr = mandant_c_nr;
    }

    public WwArtikellagerPK getCompId() {
        return this.compId;
    }

    public void setCompId(WwArtikellagerPK compId) {
        this.compId = compId;
    }

    public BigDecimal getN_lagerstand() {
        return this.n_lagerstand;
    }

    public void setN_lagerstand(BigDecimal n_lagerstand) {
        this.n_lagerstand = n_lagerstand;
    }

    public BigDecimal getN_gestehungspreis() {
        return this.n_gestehungspreis;
    }

    public void setN_gestehungspreis(BigDecimal n_gestehungspreis) {
        this.n_gestehungspreis = n_gestehungspreis;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(com.lp.server.artikel.fastlanereader.generated.FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(com.lp.server.artikel.fastlanereader.generated.FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compId", getCompId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRArtikellager) ) return false;
        FLRArtikellager castOther = (FLRArtikellager) other;
        return new EqualsBuilder()
            .append(this.getCompId(), castOther.getCompId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCompId())
            .toHashCode();
    }

}
