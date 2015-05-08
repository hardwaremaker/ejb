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
package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String n_mindestbestellwert;

    /** nullable persistent field */
    private String n_kredit;

    /** nullable persistent field */
    private String n_rabatt;

    /** nullable persistent field */
    private String c_hinweisintern;

    /** nullable persistent field */
    private String c_hinweisextern;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private Short b_moeglicherlieferant;

    /** nullable persistent field */
    private Short b_zollimportpapier;

    /** nullable persistent field */
    private Short b_beurteilen;

    /** nullable persistent field */
    private Date t_bestellsperream;

    /** nullable persistent field */
    private Date t_freigabe;

    /** nullable persistent field */
    private Integer konto_i_id_kreditorenkonto;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRFinanzKonto flrkonto;

    /** persistent field */
    private Set set_liefergruppen;

    /** full constructor */
    public FLRLieferant(String mandant_c_nr, String n_mindestbestellwert, String n_kredit, String n_rabatt, String c_hinweisintern, String c_hinweisextern, String waehrung_c_nr, String x_kommentar, Short b_moeglicherlieferant, Short b_zollimportpapier, Short b_beurteilen, Date t_bestellsperream, Date t_freigabe, Integer konto_i_id_kreditorenkonto, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, FLRFinanzKonto flrkonto, Set set_liefergruppen) {
        this.mandant_c_nr = mandant_c_nr;
        this.n_mindestbestellwert = n_mindestbestellwert;
        this.n_kredit = n_kredit;
        this.n_rabatt = n_rabatt;
        this.c_hinweisintern = c_hinweisintern;
        this.c_hinweisextern = c_hinweisextern;
        this.waehrung_c_nr = waehrung_c_nr;
        this.x_kommentar = x_kommentar;
        this.b_moeglicherlieferant = b_moeglicherlieferant;
        this.b_zollimportpapier = b_zollimportpapier;
        this.b_beurteilen = b_beurteilen;
        this.t_bestellsperream = t_bestellsperream;
        this.t_freigabe = t_freigabe;
        this.konto_i_id_kreditorenkonto = konto_i_id_kreditorenkonto;
        this.flrpartner = flrpartner;
        this.flrkonto = flrkonto;
        this.set_liefergruppen = set_liefergruppen;
    }

    /** default constructor */
    public FLRLieferant() {
    }

    /** minimal constructor */
    public FLRLieferant(Set set_liefergruppen) {
        this.set_liefergruppen = set_liefergruppen;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getN_mindestbestellwert() {
        return this.n_mindestbestellwert;
    }

    public void setN_mindestbestellwert(String n_mindestbestellwert) {
        this.n_mindestbestellwert = n_mindestbestellwert;
    }

    public String getN_kredit() {
        return this.n_kredit;
    }

    public void setN_kredit(String n_kredit) {
        this.n_kredit = n_kredit;
    }

    public String getN_rabatt() {
        return this.n_rabatt;
    }

    public void setN_rabatt(String n_rabatt) {
        this.n_rabatt = n_rabatt;
    }

    public String getC_hinweisintern() {
        return this.c_hinweisintern;
    }

    public void setC_hinweisintern(String c_hinweisintern) {
        this.c_hinweisintern = c_hinweisintern;
    }

    public String getC_hinweisextern() {
        return this.c_hinweisextern;
    }

    public void setC_hinweisextern(String c_hinweisextern) {
        this.c_hinweisextern = c_hinweisextern;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public Short getB_moeglicherlieferant() {
        return this.b_moeglicherlieferant;
    }

    public void setB_moeglicherlieferant(Short b_moeglicherlieferant) {
        this.b_moeglicherlieferant = b_moeglicherlieferant;
    }

    public Short getB_zollimportpapier() {
        return this.b_zollimportpapier;
    }

    public void setB_zollimportpapier(Short b_zollimportpapier) {
        this.b_zollimportpapier = b_zollimportpapier;
    }

    public Short getB_beurteilen() {
        return this.b_beurteilen;
    }

    public void setB_beurteilen(Short b_beurteilen) {
        this.b_beurteilen = b_beurteilen;
    }

    public Date getT_bestellsperream() {
        return this.t_bestellsperream;
    }

    public void setT_bestellsperream(Date t_bestellsperream) {
        this.t_bestellsperream = t_bestellsperream;
    }

    public Date getT_freigabe() {
        return this.t_freigabe;
    }

    public void setT_freigabe(Date t_freigabe) {
        this.t_freigabe = t_freigabe;
    }

    public Integer getKonto_i_id_kreditorenkonto() {
        return this.konto_i_id_kreditorenkonto;
    }

    public void setKonto_i_id_kreditorenkonto(Integer konto_i_id_kreditorenkonto) {
        this.konto_i_id_kreditorenkonto = konto_i_id_kreditorenkonto;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public Set getSet_liefergruppen() {
        return this.set_liefergruppen;
    }

    public void setSet_liefergruppen(Set set_liefergruppen) {
        this.set_liefergruppen = set_liefergruppen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
