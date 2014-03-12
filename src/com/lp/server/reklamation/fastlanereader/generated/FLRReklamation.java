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
package com.lp.server.reklamation.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.personal.fastlanereader.generated.FLRMaschine;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRReklamation implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String reklamationart_c_nr;

    /** nullable persistent field */
    private String c_grund;

    /** nullable persistent field */
    private String c_seriennrchargennr;

    /** nullable persistent field */
    private String c_handartikel;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String x_analyse;

    /** persistent field */
    private Date t_belegdatum;

    /** persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Short b_berechtigt;

    /** nullable persistent field */
    private BigDecimal n_kostenmaterial;

    /** nullable persistent field */
    private BigDecimal n_kostenarbeitszeit;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer bestellung_i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id;

    /** nullable persistent field */
    private Integer lieferschein_i_id;

    /** nullable persistent field */
    private String c_kdreklanr;

    /** nullable persistent field */
    private Integer i_kundeunterart;

    /** nullable persistent field */
    private com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe;

    /** nullable persistent field */
    private FLRPersonal flrverursacher;

    /** nullable persistent field */
    private FLRMaschine flrmaschine;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** nullable persistent field */
    private com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler;

    /** nullable persistent field */
    private FLRLos flrlos;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** full constructor */
    public FLRReklamation(String c_nr, String mandant_c_nr, String reklamationart_c_nr, String c_grund, String c_seriennrchargennr, String c_handartikel, String c_projekt, String status_c_nr, String x_analyse, Date t_belegdatum, Date t_erledigt, BigDecimal n_menge, Short b_berechtigt, BigDecimal n_kostenmaterial, BigDecimal n_kostenarbeitszeit, Integer kostenstelle_i_id, Integer kunde_i_id, Integer lieferant_i_id, Integer los_i_id, Integer bestellung_i_id, Integer rechnung_i_id, Integer lieferschein_i_id, String c_kdreklanr, Integer i_kundeunterart, com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe, FLRPersonal flrverursacher, FLRMaschine flrmaschine, FLRKunde flrkunde, FLRLieferant flrlieferant, com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler, FLRLos flrlos, FLRArtikelliste flrartikel) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.reklamationart_c_nr = reklamationart_c_nr;
        this.c_grund = c_grund;
        this.c_seriennrchargennr = c_seriennrchargennr;
        this.c_handartikel = c_handartikel;
        this.c_projekt = c_projekt;
        this.status_c_nr = status_c_nr;
        this.x_analyse = x_analyse;
        this.t_belegdatum = t_belegdatum;
        this.t_erledigt = t_erledigt;
        this.n_menge = n_menge;
        this.b_berechtigt = b_berechtigt;
        this.n_kostenmaterial = n_kostenmaterial;
        this.n_kostenarbeitszeit = n_kostenarbeitszeit;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.kunde_i_id = kunde_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.los_i_id = los_i_id;
        this.bestellung_i_id = bestellung_i_id;
        this.rechnung_i_id = rechnung_i_id;
        this.lieferschein_i_id = lieferschein_i_id;
        this.c_kdreklanr = c_kdreklanr;
        this.i_kundeunterart = i_kundeunterart;
        this.flrfehlerangabe = flrfehlerangabe;
        this.flrverursacher = flrverursacher;
        this.flrmaschine = flrmaschine;
        this.flrkunde = flrkunde;
        this.flrlieferant = flrlieferant;
        this.flrfehler = flrfehler;
        this.flrlos = flrlos;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRReklamation() {
    }

    /** minimal constructor */
    public FLRReklamation(Date t_belegdatum, Date t_erledigt) {
        this.t_belegdatum = t_belegdatum;
        this.t_erledigt = t_erledigt;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getReklamationart_c_nr() {
        return this.reklamationart_c_nr;
    }

    public void setReklamationart_c_nr(String reklamationart_c_nr) {
        this.reklamationart_c_nr = reklamationart_c_nr;
    }

    public String getC_grund() {
        return this.c_grund;
    }

    public void setC_grund(String c_grund) {
        this.c_grund = c_grund;
    }

    public String getC_seriennrchargennr() {
        return this.c_seriennrchargennr;
    }

    public void setC_seriennrchargennr(String c_seriennrchargennr) {
        this.c_seriennrchargennr = c_seriennrchargennr;
    }

    public String getC_handartikel() {
        return this.c_handartikel;
    }

    public void setC_handartikel(String c_handartikel) {
        this.c_handartikel = c_handartikel;
    }

    public String getC_projekt() {
        return this.c_projekt;
    }

    public void setC_projekt(String c_projekt) {
        this.c_projekt = c_projekt;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getX_analyse() {
        return this.x_analyse;
    }

    public void setX_analyse(String x_analyse) {
        this.x_analyse = x_analyse;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Short getB_berechtigt() {
        return this.b_berechtigt;
    }

    public void setB_berechtigt(Short b_berechtigt) {
        this.b_berechtigt = b_berechtigt;
    }

    public BigDecimal getN_kostenmaterial() {
        return this.n_kostenmaterial;
    }

    public void setN_kostenmaterial(BigDecimal n_kostenmaterial) {
        this.n_kostenmaterial = n_kostenmaterial;
    }

    public BigDecimal getN_kostenarbeitszeit() {
        return this.n_kostenarbeitszeit;
    }

    public void setN_kostenarbeitszeit(BigDecimal n_kostenarbeitszeit) {
        this.n_kostenarbeitszeit = n_kostenarbeitszeit;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getBestellung_i_id() {
        return this.bestellung_i_id;
    }

    public void setBestellung_i_id(Integer bestellung_i_id) {
        this.bestellung_i_id = bestellung_i_id;
    }

    public Integer getRechnung_i_id() {
        return this.rechnung_i_id;
    }

    public void setRechnung_i_id(Integer rechnung_i_id) {
        this.rechnung_i_id = rechnung_i_id;
    }

    public Integer getLieferschein_i_id() {
        return this.lieferschein_i_id;
    }

    public void setLieferschein_i_id(Integer lieferschein_i_id) {
        this.lieferschein_i_id = lieferschein_i_id;
    }

    public String getC_kdreklanr() {
        return this.c_kdreklanr;
    }

    public void setC_kdreklanr(String c_kdreklanr) {
        this.c_kdreklanr = c_kdreklanr;
    }

    public Integer getI_kundeunterart() {
        return this.i_kundeunterart;
    }

    public void setI_kundeunterart(Integer i_kundeunterart) {
        this.i_kundeunterart = i_kundeunterart;
    }

    public com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe getFlrfehlerangabe() {
        return this.flrfehlerangabe;
    }

    public void setFlrfehlerangabe(com.lp.server.reklamation.fastlanereader.generated.FLRFehlerangabe flrfehlerangabe) {
        this.flrfehlerangabe = flrfehlerangabe;
    }

    public FLRPersonal getFlrverursacher() {
        return this.flrverursacher;
    }

    public void setFlrverursacher(FLRPersonal flrverursacher) {
        this.flrverursacher = flrverursacher;
    }

    public FLRMaschine getFlrmaschine() {
        return this.flrmaschine;
    }

    public void setFlrmaschine(FLRMaschine flrmaschine) {
        this.flrmaschine = flrmaschine;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public com.lp.server.reklamation.fastlanereader.generated.FLRFehler getFlrfehler() {
        return this.flrfehler;
    }

    public void setFlrfehler(com.lp.server.reklamation.fastlanereader.generated.FLRFehler flrfehler) {
        this.flrfehler = flrfehler;
    }

    public FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
