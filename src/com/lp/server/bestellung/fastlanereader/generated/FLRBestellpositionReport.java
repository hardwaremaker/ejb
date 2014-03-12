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
package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBestellpositionReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private String einheit_c_nr;

    /** nullable persistent field */
    private String c_bezeichnung;

    /** nullable persistent field */
    private BigDecimal n_nettogesamtpreis;

    /** nullable persistent field */
    private String bestellpositionstatus_c_nr;

    /** nullable persistent field */
    private String bestellpositionart_c_nr;

    /** nullable persistent field */
    private java.sql.Timestamp t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Date t_lieferterminbestaetigt;

    /** nullable persistent field */
    private BigDecimal n_offenemenge;

    /** nullable persistent field */
    private java.sql.Timestamp t_auftragsbestaetigungstermin;

    /** nullable persistent field */
    private String c_abnummer;

    /** nullable persistent field */
    private String c_abkommentar;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** persistent field */
    private Set abrufpositionenset;

    /** full constructor */
    public FLRBestellpositionReport(Integer i_sort, BigDecimal n_menge, String einheit_c_nr, String c_bezeichnung, BigDecimal n_nettogesamtpreis, String bestellpositionstatus_c_nr, String bestellpositionart_c_nr, java.sql.Timestamp t_uebersteuerterliefertermin, Date t_lieferterminbestaetigt, BigDecimal n_offenemenge, java.sql.Timestamp t_auftragsbestaetigungstermin, String c_abnummer, String c_abkommentar, Integer position_i_id_artikelset, com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, Set abrufpositionenset) {
        this.i_sort = i_sort;
        this.n_menge = n_menge;
        this.einheit_c_nr = einheit_c_nr;
        this.c_bezeichnung = c_bezeichnung;
        this.n_nettogesamtpreis = n_nettogesamtpreis;
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
        this.n_offenemenge = n_offenemenge;
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
        this.c_abnummer = c_abnummer;
        this.c_abkommentar = c_abkommentar;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.flrbestellung = flrbestellung;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.abrufpositionenset = abrufpositionenset;
    }

    /** default constructor */
    public FLRBestellpositionReport() {
    }

    /** minimal constructor */
    public FLRBestellpositionReport(Integer i_sort, Set abrufpositionenset) {
        this.i_sort = i_sort;
        this.abrufpositionenset = abrufpositionenset;
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

    public String getC_bezeichnung() {
        return this.c_bezeichnung;
    }

    public void setC_bezeichnung(String c_bezeichnung) {
        this.c_bezeichnung = c_bezeichnung;
    }

    public BigDecimal getN_nettogesamtpreis() {
        return this.n_nettogesamtpreis;
    }

    public void setN_nettogesamtpreis(BigDecimal n_nettogesamtpreis) {
        this.n_nettogesamtpreis = n_nettogesamtpreis;
    }

    public String getBestellpositionstatus_c_nr() {
        return this.bestellpositionstatus_c_nr;
    }

    public void setBestellpositionstatus_c_nr(String bestellpositionstatus_c_nr) {
        this.bestellpositionstatus_c_nr = bestellpositionstatus_c_nr;
    }

    public String getBestellpositionart_c_nr() {
        return this.bestellpositionart_c_nr;
    }

    public void setBestellpositionart_c_nr(String bestellpositionart_c_nr) {
        this.bestellpositionart_c_nr = bestellpositionart_c_nr;
    }

    public java.sql.Timestamp getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(java.sql.Timestamp t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Date getT_lieferterminbestaetigt() {
        return this.t_lieferterminbestaetigt;
    }

    public void setT_lieferterminbestaetigt(Date t_lieferterminbestaetigt) {
        this.t_lieferterminbestaetigt = t_lieferterminbestaetigt;
    }

    public BigDecimal getN_offenemenge() {
        return this.n_offenemenge;
    }

    public void setN_offenemenge(BigDecimal n_offenemenge) {
        this.n_offenemenge = n_offenemenge;
    }

    public java.sql.Timestamp getT_auftragsbestaetigungstermin() {
        return this.t_auftragsbestaetigungstermin;
    }

    public void setT_auftragsbestaetigungstermin(java.sql.Timestamp t_auftragsbestaetigungstermin) {
        this.t_auftragsbestaetigungstermin = t_auftragsbestaetigungstermin;
    }

    public String getC_abnummer() {
        return this.c_abnummer;
    }

    public void setC_abnummer(String c_abnummer) {
        this.c_abnummer = c_abnummer;
    }

    public String getC_abkommentar() {
        return this.c_abkommentar;
    }

    public void setC_abkommentar(String c_abkommentar) {
        this.c_abkommentar = c_abkommentar;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellung getFlrbestellung() {
        return this.flrbestellung;
    }

    public void setFlrbestellung(com.lp.server.bestellung.fastlanereader.generated.FLRBestellung flrbestellung) {
        this.flrbestellung = flrbestellung;
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

    public Set getAbrufpositionenset() {
        return this.abrufpositionenset;
    }

    public void setAbrufpositionenset(Set abrufpositionenset) {
        this.abrufpositionenset = abrufpositionenset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
