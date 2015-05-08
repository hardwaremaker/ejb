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
package com.lp.server.angebot.fastlanereader.generated;

import com.lp.server.angebotstkl.fastlanereader.generated.FLRAgstkl;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRVerleih;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAngebotposition implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2163236060596193290L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String positionart_c_nr;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreisplusversteckteraufschlag;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlag;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer agstkl_i_id;

    /** nullable persistent field */
    private Short b_alternative;

    /** nullable persistent field */
    private Integer angebot_i_id;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot;

    /** nullable persistent field */
    private FLRVerleih flrverleih;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private FLRAgstkl flragstkl;

	private Integer zwsvonposition_i_id;

	private Integer zwsbisposition_i_id;

	private BigDecimal n_zwsnettosumme;
	
	private Short b_zwspositionspreiszeigen ;
 
    /** full constructor */
    public FLRAngebotposition(Integer i_sort, String positionart_c_nr, BigDecimal n_menge, String einheit_c_nr, String c_bez, String c_zbez, BigDecimal n_nettoeinzelpreisplusversteckteraufschlag, BigDecimal n_nettogesamtpreisplusversteckteraufschlag, BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte, BigDecimal n_nettogesamtpreis, String x_textinhalt, Integer agstkl_i_id, Short b_alternative, Integer angebot_i_id, Integer position_i_id, String typ_c_nr, Integer position_i_id_artikelset, com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot, FLRVerleih flrverleih, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, FLRAgstkl flragstkl) {
        this.i_sort = i_sort;
        this.positionart_c_nr = positionart_c_nr;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.n_nettoeinzelpreisplusversteckteraufschlag = n_nettoeinzelpreisplusversteckteraufschlag;
        this.n_nettogesamtpreisplusversteckteraufschlag = n_nettogesamtpreisplusversteckteraufschlag;
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.x_textinhalt = x_textinhalt;
        this.agstkl_i_id = agstkl_i_id;
        this.b_alternative = b_alternative;
        this.angebot_i_id = angebot_i_id;
        this.position_i_id = position_i_id;
        this.typ_c_nr = typ_c_nr;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.flrangebot = flrangebot;
        this.flrverleih = flrverleih;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flragstkl = flragstkl;
    }

    /** default constructor */
    public FLRAngebotposition() {
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

    public BigDecimal getN_nettoeinzelpreisplusversteckteraufschlag() {
        return this.n_nettoeinzelpreisplusversteckteraufschlag;
    }

    public void setN_nettoeinzelpreisplusversteckteraufschlag(BigDecimal n_nettoeinzelpreisplusversteckteraufschlag) {
        this.n_nettoeinzelpreisplusversteckteraufschlag = n_nettoeinzelpreisplusversteckteraufschlag;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlag() {
        return this.n_nettogesamtpreisplusversteckteraufschlag;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlag(BigDecimal n_nettogesamtpreisplusversteckteraufschlag) {
        this.n_nettogesamtpreisplusversteckteraufschlag = n_nettogesamtpreisplusversteckteraufschlag;
    }

    public BigDecimal getN_nettogesamtpreisplusversteckteraufschlagminusrabatte() {
        return this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public void setN_nettogesamtpreisplusversteckteraufschlagminusrabatte(BigDecimal n_nettogesamtpreisplusversteckteraufschlagminusrabatte) {
        this.n_nettogesamtpreisplusversteckteraufschlagminusrabatte = n_nettogesamtpreisplusversteckteraufschlagminusrabatte;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getAgstkl_i_id() {
        return this.agstkl_i_id;
    }

    public void setAgstkl_i_id(Integer agstkl_i_id) {
        this.agstkl_i_id = agstkl_i_id;
    }

    public Short getB_alternative() {
        return this.b_alternative;
    }

    public void setB_alternative(Short b_alternative) {
        this.b_alternative = b_alternative;
    }

    public Integer getAngebot_i_id() {
        return this.angebot_i_id;
    }

    public void setAngebot_i_id(Integer angebot_i_id) {
        this.angebot_i_id = angebot_i_id;
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

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public com.lp.server.angebot.fastlanereader.generated.FLRAngebot getFlrangebot() {
        return this.flrangebot;
    }

    public void setFlrangebot(com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot) {
        this.flrangebot = flrangebot;
    }

    public FLRVerleih getFlrverleih() {
        return this.flrverleih;
    }

    public void setFlrverleih(FLRVerleih flrverleih) {
        this.flrverleih = flrverleih;
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

    public FLRAgstkl getFlragstkl() {
        return this.flragstkl;
    }

    public void setFlragstkl(FLRAgstkl flragstkl) {
        this.flragstkl = flragstkl;
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
	
    public Short getB_zwspositionspreiszeigen() {
		return b_zwspositionspreiszeigen;
	}

	public void setB_zwspositionspreiszeigen(Short b_zwspositionspreiszeigen) {
		this.b_zwspositionspreiszeigen = b_zwspositionspreiszeigen;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
