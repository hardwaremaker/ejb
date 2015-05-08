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
package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLROffeneags implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer maschine_i_id;

    /** nullable persistent field */
    private BigDecimal n_gesamtzeit;

    /** nullable persistent field */
    private Integer i_arbeitsgangsnummer;

    /** nullable persistent field */
    private Integer i_maschinenversatztage;

    /** nullable persistent field */
    private Integer i_maschinenversatz_ms;

    /** nullable persistent field */
    private Integer i_anzahlzeitdaten;

    /** nullable persistent field */
    private Date t_agbeginn;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_taetigkeit;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_sollmaterial;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel_stueckliste;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan;

    /** full constructor */
    public FLROffeneags(Integer kunde_i_id, String mandant_c_nr, Integer los_i_id, Integer maschine_i_id, BigDecimal n_gesamtzeit, Integer i_arbeitsgangsnummer, Integer i_maschinenversatztage, Integer i_maschinenversatz_ms, Integer i_anzahlzeitdaten, Date t_agbeginn, FLRArtikelliste flrartikel_taetigkeit, FLRArtikelliste flrartikel_sollmaterial, FLRArtikelliste flrartikel_stueckliste, FLRKunde flrkunde, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, FLRMaschine flrmaschine, com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.kunde_i_id = kunde_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.los_i_id = los_i_id;
        this.maschine_i_id = maschine_i_id;
        this.n_gesamtzeit = n_gesamtzeit;
        this.i_arbeitsgangsnummer = i_arbeitsgangsnummer;
        this.i_maschinenversatztage = i_maschinenversatztage;
        this.i_maschinenversatz_ms = i_maschinenversatz_ms;
        this.i_anzahlzeitdaten = i_anzahlzeitdaten;
        this.t_agbeginn = t_agbeginn;
        this.flrartikel_taetigkeit = flrartikel_taetigkeit;
        this.flrartikel_sollmaterial = flrartikel_sollmaterial;
        this.flrartikel_stueckliste = flrartikel_stueckliste;
        this.flrkunde = flrkunde;
        this.flrlos = flrlos;
        this.flrmaschine = flrmaschine;
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    /** default constructor */
    public FLROffeneags() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getMaschine_i_id() {
        return this.maschine_i_id;
    }

    public void setMaschine_i_id(Integer maschine_i_id) {
        this.maschine_i_id = maschine_i_id;
    }

    public BigDecimal getN_gesamtzeit() {
        return this.n_gesamtzeit;
    }

    public void setN_gesamtzeit(BigDecimal n_gesamtzeit) {
        this.n_gesamtzeit = n_gesamtzeit;
    }

    public Integer getI_arbeitsgangsnummer() {
        return this.i_arbeitsgangsnummer;
    }

    public void setI_arbeitsgangsnummer(Integer i_arbeitsgangsnummer) {
        this.i_arbeitsgangsnummer = i_arbeitsgangsnummer;
    }

    public Integer getI_maschinenversatztage() {
        return this.i_maschinenversatztage;
    }

    public void setI_maschinenversatztage(Integer i_maschinenversatztage) {
        this.i_maschinenversatztage = i_maschinenversatztage;
    }

    public Integer getI_maschinenversatz_ms() {
        return this.i_maschinenversatz_ms;
    }

    public void setI_maschinenversatz_ms(Integer i_maschinenversatz_ms) {
        this.i_maschinenversatz_ms = i_maschinenversatz_ms;
    }

    public Integer getI_anzahlzeitdaten() {
        return this.i_anzahlzeitdaten;
    }

    public void setI_anzahlzeitdaten(Integer i_anzahlzeitdaten) {
        this.i_anzahlzeitdaten = i_anzahlzeitdaten;
    }

    public Date getT_agbeginn() {
        return this.t_agbeginn;
    }

    public void setT_agbeginn(Date t_agbeginn) {
        this.t_agbeginn = t_agbeginn;
    }

    public FLRArtikelliste getFlrartikel_taetigkeit() {
        return this.flrartikel_taetigkeit;
    }

    public void setFlrartikel_taetigkeit(FLRArtikelliste flrartikel_taetigkeit) {
        this.flrartikel_taetigkeit = flrartikel_taetigkeit;
    }

    public FLRArtikelliste getFlrartikel_sollmaterial() {
        return this.flrartikel_sollmaterial;
    }

    public void setFlrartikel_sollmaterial(FLRArtikelliste flrartikel_sollmaterial) {
        this.flrartikel_sollmaterial = flrartikel_sollmaterial;
    }

    public FLRArtikelliste getFlrartikel_stueckliste() {
        return this.flrartikel_stueckliste;
    }

    public void setFlrartikel_stueckliste(FLRArtikelliste flrartikel_stueckliste) {
        this.flrartikel_stueckliste = flrartikel_stueckliste;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan getFlrlossollarbeitsplan() {
        return this.flrlossollarbeitsplan;
    }

    public void setFlrlossollarbeitsplan(com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
