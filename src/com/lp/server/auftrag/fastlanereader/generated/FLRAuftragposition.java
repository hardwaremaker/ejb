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
import com.lp.server.artikel.fastlanereader.generated.FLRVerleih;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragposition implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6339105959017486542L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String positionart_c_nr;

    /** nullable persistent field */
    private String auftragpositionstatus_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private BigDecimal n_offenerahmenmenge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_bruttogesamtpreis;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer auftragposition_i_id_rahmenposition;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private Integer mwstsatz_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRVerleih flrverleih;

    private Integer zwsvonposition_i_id ;
    
    private Integer zwsbisposition_i_id ;
    
    private BigDecimal n_zwsnettosumme ;
 
    /** full constructor */
    public FLRAuftragposition(Integer i_sort, String positionart_c_nr, String auftragpositionstatus_c_nr, String c_bez, String c_zbez, BigDecimal n_menge, BigDecimal n_offenemenge, BigDecimal n_offenerahmenmenge, String einheit_c_nr, BigDecimal n_nettogesamtpreis, BigDecimal n_bruttogesamtpreis, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte, String x_textinhalt, Integer auftragposition_i_id_rahmenposition, Integer position_i_id, Integer position_i_id_artikelset, Integer mwstsatz_i_id, String typ_c_nr, Date t_uebersteuerterliefertermin, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, FLRVerleih flrverleih) {
        this.i_sort = i_sort;
        this.positionart_c_nr = positionart_c_nr;
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_menge = n_menge;
        this.n_offenemenge = n_offenemenge;
        this.n_offenerahmenmenge = n_offenerahmenmenge;
        this.einheit_c_nr = einheit_c_nr;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
        this.x_textinhalt = x_textinhalt;
        this.auftragposition_i_id_rahmenposition = auftragposition_i_id_rahmenposition;
        this.position_i_id = position_i_id;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.mwstsatz_i_id = mwstsatz_i_id;
        this.typ_c_nr = typ_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.flrauftrag = flrauftrag;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrverleih = flrverleih;
    }

    /** default constructor */
    public FLRAuftragposition() {
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

    public String getPositionart_c_nr() {
        return this.positionart_c_nr;
    }

    public void setPositionart_c_nr(String positionart_c_nr) {
        this.positionart_c_nr = positionart_c_nr;
    }

    public String getAuftragpositionstatus_c_nr() {
        return this.auftragpositionstatus_c_nr;
    }

    public void setAuftragpositionstatus_c_nr(String auftragpositionstatus_c_nr) {
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
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

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
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

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public BigDecimal getN_bruttogesamtpreis() {
        return this.n_bruttogesamtpreis;
    }

    public void setN_bruttogesamtpreis(BigDecimal n_bruttogesamtpreis) {
        this.n_bruttogesamtpreis = n_bruttogesamtpreis;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() {
        return this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlagminusrabatte(BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte) {
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getAuftragposition_i_id_rahmenposition() {
        return this.auftragposition_i_id_rahmenposition;
    }

    public void setAuftragposition_i_id_rahmenposition(Integer auftragposition_i_id_rahmenposition) {
        this.auftragposition_i_id_rahmenposition = auftragposition_i_id_rahmenposition;
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

    public Integer getMwstsatz_i_id() {
        return this.mwstsatz_i_id;
    }

    public void setMwstsatz_i_id(Integer mwstsatz_i_id) {
        this.mwstsatz_i_id = mwstsatz_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMediastandard getFlrmediastandard() {
        return this.flrmediastandard;
    }

    public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
        this.flrmediastandard = flrmediastandard;
    }

    public FLRVerleih getFlrverleih() {
        return this.flrverleih;
    }

    public void setFlrverleih(FLRVerleih flrverleih) {
        this.flrverleih = flrverleih;
    }

	public Integer getZwsvonposition_i_id() {
		return zwsvonposition_i_id;
	}

	public void setZwsvonposition_i_id(Integer zwsvonposition_i_id) {
		this.zwsvonposition_i_id = zwsvonposition_i_id;
	}

	public Integer getZwsbisposition_i_id() {
		return zwsbisposition_i_id;
	}

	public void setZwsbisposition_i_id(Integer zwsbisposition_i_id) {
		this.zwsbisposition_i_id = zwsbisposition_i_id;
	}

	public BigDecimal getZwsnettosumme() {
		return n_zwsnettosumme;
	}

	public void setZwsnettosumme(BigDecimal n_zwsnettosumme) {
		this.n_zwsnettosumme = n_zwsnettosumme;
	}
  
    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
