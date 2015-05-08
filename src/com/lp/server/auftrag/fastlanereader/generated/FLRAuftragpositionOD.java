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
package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragpositionOD implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String auftragpositionart_c_nr;

    /** nullable persistent field */
    private String auftragpositionstatus_c_nr;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private BigDecimal n_offenerahmenmenge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private BigDecimal n_einkaufpreis;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOD flrauftragpositionrahmen;

    /** full constructor */
    public FLRAuftragpositionOD(Integer i_sort, String auftragpositionart_c_nr, String auftragpositionstatus_c_nr, BigDecimal n_menge, String c_bez, String c_zbez, BigDecimal n_offenemenge, BigDecimal n_offenerahmenmenge, String einheit_c_nr, Integer artikel_i_id, Date t_uebersteuerterliefertermin, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte, Integer position_i_id, String typ_c_nr, BigDecimal n_einkaufpreis, FLRArtikel flrartikel, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOD flrauftragpositionrahmen) {
        this.i_sort = i_sort;
        this.auftragpositionart_c_nr = auftragpositionart_c_nr;
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
        this.n_menge = n_menge;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_offenemenge = n_offenemenge;
        this.n_offenerahmenmenge = n_offenerahmenmenge;
        this.einheit_c_nr = einheit_c_nr;
        this.artikel_i_id = artikel_i_id;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
        this.position_i_id = position_i_id;
        this.typ_c_nr = typ_c_nr;
        this.n_einkaufpreis = n_einkaufpreis;
        this.flrartikel = flrartikel;
        this.flrauftrag = flrauftrag;
        this.flrauftragpositionrahmen = flrauftragpositionrahmen;
    }

    /** default constructor */
    public FLRAuftragpositionOD() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getAuftragpositionart_c_nr() {
        return this.auftragpositionart_c_nr;
    }

    public void setAuftragpositionart_c_nr(String auftragpositionart_c_nr) {
        this.auftragpositionart_c_nr = auftragpositionart_c_nr;
    }

    public String getAuftragpositionstatus_c_nr() {
        return this.auftragpositionstatus_c_nr;
    }

    public void setAuftragpositionstatus_c_nr(String auftragpositionstatus_c_nr) {
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public BigDecimal getN_offenerahmenmenge() {
        return this.n_offenerahmenmenge;
    }

    public void setN_offenerahmenmenge(BigDecimal n_offenerahmenmenge) {
        this.n_offenerahmenmenge = n_offenerahmenmenge;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() {
        return this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlagminusrabatte(BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte) {
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public BigDecimal getN_einkaufpreis() {
        return this.n_einkaufpreis;
    }

    public void setN_einkaufpreis(BigDecimal n_einkaufpreis) {
        this.n_einkaufpreis = n_einkaufpreis;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOD getFlrauftragpositionrahmen() {
        return this.flrauftragpositionrahmen;
    }

    public void setFlrauftragpositionrahmen(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionOD flrauftragpositionrahmen) {
        this.flrauftragpositionrahmen = flrauftragpositionrahmen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
