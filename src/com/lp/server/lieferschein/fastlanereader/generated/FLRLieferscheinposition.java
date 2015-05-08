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
package com.lp.server.lieferschein.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRVerleih;
import com.lp.server.auftrag.fastlanereader.generated.FLRPositionenSichtAuftrag;
import com.lp.server.system.fastlanereader.generated.FLRKostentraeger;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatz;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferscheinposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String positionsart_c_nr;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_materialzuschlag;

    /** nullable persistent field */
    private BigDecimal n_bruttogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlag;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatt;

    /** nullable persistent field */
    private String c_textinhalt;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Short b_artikelbezeichnunguebersteuert;

    /** nullable persistent field */
    private String c_snrchnr_mig;

    /** nullable persistent field */
    private Integer zwsvonposition_i_id;

    /** nullable persistent field */
    private Integer zwsbisposition_i_id;

    /** nullable persistent field */
    private BigDecimal zwsnettosumme;

    /** nullable persistent field */
    private FLRPositionenSichtAuftrag flrpositionensichtauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRMwstsatz flrmwstsatz;

    /** nullable persistent field */
    private FLRVerleih flrverleih;

    /** nullable persistent field */
    private FLRKostentraeger flrkostentraeger;

    /** persistent field */
    private Set setartikel_set;

    private Integer lieferschein_i_id ;
    
	/** full constructor */
    public FLRLieferscheinposition(Integer i_sort, String positionsart_c_nr, Integer auftragposition_i_id, BigDecimal n_menge, String einheit_c_nr, String c_bez, String c_zbez, BigDecimal n_nettogesamtpreis, BigDecimal n_materialzuschlag, BigDecimal n_bruttogesamtpreis, BigDecimal n_nettogesamtpreisplusversteckteraufschlag, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatt, String c_textinhalt, Integer position_i_id, Integer position_i_id_artikelset, String typ_c_nr, Short b_artikelbezeichnunguebersteuert, String c_snrchnr_mig, Integer zwsvonposition_i_id, Integer zwsbisposition_i_id, BigDecimal zwsnettosumme, FLRPositionenSichtAuftrag flrpositionensichtauftrag, FLRArtikel flrartikel, com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein, FLRMediastandard flrmediastandard, FLRMwstsatz flrmwstsatz, FLRVerleih flrverleih, FLRKostentraeger flrkostentraeger, Set setartikel_set, Integer lieferschein_i_id) {
        this.i_sort = i_sort;
        this.positionsart_c_nr = positionsart_c_nr;
        this.auftragposition_i_id = auftragposition_i_id;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_materialzuschlag = n_materialzuschlag;
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
        this.n_nettogesamtpreisplusversteckteraufschlag = n_nettogesamtpreisplusversteckteraufschlag;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatt = n_nettogesamtpreisplusversteckteraufschlagminusrabatt;
        this.c_textinhalt = c_textinhalt;
        this.position_i_id = position_i_id;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.typ_c_nr = typ_c_nr;
        this.b_artikelbezeichnunguebersteuert = b_artikelbezeichnunguebersteuert;
        this.c_snrchnr_mig = c_snrchnr_mig;
        this.zwsvonposition_i_id = zwsvonposition_i_id;
        this.zwsbisposition_i_id = zwsbisposition_i_id;
        this.zwsnettosumme = zwsnettosumme;
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
        this.flrartikel = flrartikel;
        this.flrlieferschein = flrlieferschein;
        this.flrmediastandard = flrmediastandard;
        this.flrmwstsatz = flrmwstsatz;
        this.flrverleih = flrverleih;
        this.flrkostentraeger = flrkostentraeger;
        this.setartikel_set = setartikel_set;
        this.lieferschein_i_id = lieferschein_i_id ;
    }

    /** default constructor */
    public FLRLieferscheinposition() {
    }

    /** minimal constructor */
    public FLRLieferscheinposition(Set setartikel_set) {
        this.setartikel_set = setartikel_set;
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

    public String getPositionsart_c_nr() {
        return this.positionsart_c_nr;
    }

    public void setPositionsart_c_nr(String positionsart_c_nr) {
        this.positionsart_c_nr = positionsart_c_nr;
    }

    public Integer getAuftragposition_i_id() {
        return this.auftragposition_i_id;
    }

    public void setAuftragposition_i_id(Integer auftragposition_i_id) {
        this.auftragposition_i_id = auftragposition_i_id;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public String getEinheit_c_nr() {
        return this.einheit_c_nr;
    }

    public void setEinheit_c_nr(String einheit_c_nr) {
        this.einheit_c_nr = einheit_c_nr;
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

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public BigDecimal getN_materialzuschlag() {
        return this.n_materialzuschlag;
    }

    public void setN_materialzuschlag(BigDecimal n_materialzuschlag) {
        this.n_materialzuschlag = n_materialzuschlag;
    }

    public BigDecimal getN_bruttogesamtpreis() {
        return this.n_bruttogesamtpreis;
    }

    public void setN_bruttogesamtpreis(BigDecimal n_bruttogesamtpreis) {
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlag() {
        return this.n_nettogesamtpreisplusversteckteraufschlag;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlag(BigDecimal n_nettogesamtpreisplusversteckteraufschlag) {
        this.n_nettogesamtpreisplusversteckteraufschlag = n_nettogesamtpreisplusversteckteraufschlag;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlagminusrabatt() {
        return this.n_nettogesamtpreisplusversteckteraufschlagminusrabatt;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlagminusrabatt(BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatt) {
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatt = n_nettogesamtpreisplusversteckteraufschlagminusrabatt;
    }

    public String getC_textinhalt() {
        return this.c_textinhalt;
    }

    public void setC_textinhalt(String c_textinhalt) {
        this.c_textinhalt = c_textinhalt;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Short getB_artikelbezeichnunguebersteuert() {
        return this.b_artikelbezeichnunguebersteuert;
    }

    public void setB_artikelbezeichnunguebersteuert(Short b_artikelbezeichnunguebersteuert) {
        this.b_artikelbezeichnunguebersteuert = b_artikelbezeichnunguebersteuert;
    }

    public String getC_snrchnr_mig() {
        return this.c_snrchnr_mig;
    }

    public void setC_snrchnr_mig(String c_snrchnr_mig) {
        this.c_snrchnr_mig = c_snrchnr_mig;
    }

    public Integer getZwsvonposition_i_id() {
        return this.zwsvonposition_i_id;
    }

    public void setZwsvonposition_i_id(Integer zwsvonposition_i_id) {
        this.zwsvonposition_i_id = zwsvonposition_i_id;
    }

    public Integer getZwsbisposition_i_id() {
        return this.zwsbisposition_i_id;
    }

    public void setZwsbisposition_i_id(Integer zwsbisposition_i_id) {
        this.zwsbisposition_i_id = zwsbisposition_i_id;
    }

    public BigDecimal getZwsnettosumme() {
        return this.zwsnettosumme;
    }

    public void setZwsnettosumme(BigDecimal zwsnettosumme) {
        this.zwsnettosumme = zwsnettosumme;
    }

    public FLRPositionenSichtAuftrag getFlrpositionensichtauftrag() {
        return this.flrpositionensichtauftrag;
    }

    public void setFlrpositionensichtauftrag(FLRPositionenSichtAuftrag flrpositionensichtauftrag) {
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein getFlrlieferschein() {
        return this.flrlieferschein;
    }

    public void setFlrlieferschein(com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein flrlieferschein) {
        this.flrlieferschein = flrlieferschein;
    }

    public FLRMediastandard getFlrmediastandard() {
        return this.flrmediastandard;
    }

    public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
        this.flrmediastandard = flrmediastandard;
    }

    public FLRMwstsatz getFlrmwstsatz() {
        return this.flrmwstsatz;
    }

    public void setFlrmwstsatz(FLRMwstsatz flrmwstsatz) {
        this.flrmwstsatz = flrmwstsatz;
    }

    public FLRVerleih getFlrverleih() {
        return this.flrverleih;
    }

    public void setFlrverleih(FLRVerleih flrverleih) {
        this.flrverleih = flrverleih;
    }

    public FLRKostentraeger getFlrkostentraeger() {
        return this.flrkostentraeger;
    }

    public void setFlrkostentraeger(FLRKostentraeger flrkostentraeger) {
        this.flrkostentraeger = flrkostentraeger;
    }

    public Set getSetartikel_set() {
        return this.setartikel_set;
    }

    public void setSetartikel_set(Set setartikel_set) {
        this.setartikel_set = setartikel_set;
    }

    public Integer getLieferschein_i_id() {
		return lieferschein_i_id;
	}

	public void setLieferschein_i_id(Integer lieferschein_i_id) {
		this.lieferschein_i_id = lieferschein_i_id;
	}

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }
}
