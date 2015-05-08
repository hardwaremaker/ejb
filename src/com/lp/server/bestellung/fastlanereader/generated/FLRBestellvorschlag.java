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
package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRBelegart;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellvorschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_zubestellendemenge;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private BigDecimal n_nettoeinzelpreis;

    /** nullable persistent field */
    private BigDecimal n_rabattbetrag;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreisminusrabatte;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Short b_nettopreisuebersteuert;

    /** nullable persistent field */
    private Short b_vormerkung;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private FLRBelegart flrbelegart;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** full constructor */
    public FLRBestellvorschlag(BigDecimal n_zubestellendemenge, Date t_liefertermin, Integer artikel_i_id, Integer lieferant_i_id, BigDecimal n_nettoeinzelpreis, BigDecimal n_rabattbetrag, BigDecimal n_nettogesamtpreisminusrabatte, BigDecimal n_nettogesamtpreis, String belegart_c_nr, Integer i_belegartid, Integer i_belegartpositionid, String mandant_c_nr, Short b_nettopreisuebersteuert, Short b_vormerkung, Integer projekt_i_id, String x_textinhalt, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, FLRLieferant flrlieferant, FLRBelegart flrbelegart, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRProjekt flrprojekt) {
        this.n_zubestellendemenge = n_zubestellendemenge;
        this.t_liefertermin = t_liefertermin;
        this.artikel_i_id = artikel_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
        this.n_rabattbetrag = n_rabattbetrag;
        this.n_nettogesamtpreisminusrabatte = n_nettogesamtpreisminusrabatte;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.belegart_c_nr = belegart_c_nr;
        this.i_belegartid = i_belegartid;
        this.i_belegartpositionid = i_belegartpositionid;
        this.mandant_c_nr = mandant_c_nr;
        this.b_nettopreisuebersteuert = b_nettopreisuebersteuert;
        this.b_vormerkung = b_vormerkung;
        this.projekt_i_id = projekt_i_id;
        this.x_textinhalt = x_textinhalt;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.flrlieferant = flrlieferant;
        this.flrbelegart = flrbelegart;
        this.flrbestellung = flrbestellung;
        this.flrprojekt = flrprojekt;
    }

    /** default constructor */
    public FLRBestellvorschlag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_zubestellendemenge() {
        return this.n_zubestellendemenge;
    }

    public void setN_zubestellendemenge(BigDecimal n_zubestellendemenge) {
        this.n_zubestellendemenge = n_zubestellendemenge;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public BigDecimal getN_nettoeinzelpreis() {
        return this.n_nettoeinzelpreis;
    }

    public void setN_nettoeinzelpreis(BigDecimal n_nettoeinzelpreis) {
        this.n_nettoeinzelpreis = n_nettoeinzelpreis;
    }

    public BigDecimal getN_rabattbetrag() {
        return this.n_rabattbetrag;
    }

    public void setN_rabattbetrag(BigDecimal n_rabattbetrag) {
        this.n_rabattbetrag = n_rabattbetrag;
    }

    public BigDecimal getN_nettogesamtpreisminusrabatte() {
        return this.n_nettogesamtpreisminusrabatte;
    }

    public void setN_nettogesamtpreisminusrabatte(BigDecimal n_nettogesamtpreisminusrabatte) {
        this.n_nettogesamtpreisminusrabatte = n_nettogesamtpreisminusrabatte;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public Integer getI_belegartid() {
        return this.i_belegartid;
    }

    public void setI_belegartid(Integer i_belegartid) {
        this.i_belegartid = i_belegartid;
    }

    public Integer getI_belegartpositionid() {
        return this.i_belegartpositionid;
    }

    public void setI_belegartpositionid(Integer i_belegartpositionid) {
        this.i_belegartpositionid = i_belegartpositionid;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Short getB_nettopreisuebersteuert() {
        return this.b_nettopreisuebersteuert;
    }

    public void setB_nettopreisuebersteuert(Short b_nettopreisuebersteuert) {
        this.b_nettopreisuebersteuert = b_nettopreisuebersteuert;
    }

    public Short getB_vormerkung() {
        return this.b_vormerkung;
    }

    public void setB_vormerkung(Short b_vormerkung) {
        this.b_vormerkung = b_vormerkung;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public FLRBelegart getFlrbelegart() {
        return this.flrbelegart;
    }

    public void setFlrbelegart(FLRBelegart flrbelegart) {
        this.flrbelegart = flrbelegart;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
