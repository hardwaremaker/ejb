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
package com.lp.server.lieferschein.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRLager;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferschein implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String lieferscheinart_c_nr;

    /** nullable persistent field */
    private String c_bez_projektbezeichnung;

    /** nullable persistent field */
    private String c_lieferartort;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date d_belegdatum;

    /** nullable persistent field */
    private String lieferscheinstatus_status_c_nr;

    /** nullable persistent field */
    private Integer lager_i_id;

    /** nullable persistent field */
    private Integer ziellager_i_id;

    /** nullable persistent field */
    private String waehrung_c_nr_lieferscheinwaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzulieferscheinwaehrung;

    /** nullable persistent field */
    private Short b_verrechenbar;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_rechnungsadresse;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer spediteur_i_id;

    /** nullable persistent field */
    private Integer lieferart_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_vertreter;

    /** nullable persistent field */
    private BigDecimal n_gesamtwertinlieferscheinwaehrung;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private String c_kommission;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Date t_versandzeitpunkt;

    /** nullable persistent field */
    private String c_versandtype;

    /** nullable persistent field */
    private Date t_aendern;

    /** nullable persistent field */
    private Date t_zollexportpapier;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRRechnung flrrechnung;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRKunde flrkunderechnungsadresse;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private FLRPersonal flrpersonalanleger;

    /** nullable persistent field */
    private FLRPersonal flrpersonalaenderer;

    /** nullable persistent field */
    private FLRLager flrlager;

    /** nullable persistent field */
    private FLRLager flrziellager;

    /** nullable persistent field */
    private com.lp.server.lieferschein.fastlanereader.generated.FLRBegruendung flrbegruendung;

    /** persistent field */
    private Set flrlieferscheinpositionen;

    /** persistent field */
    private Set flrverkettet;

    /** persistent field */
    private Set flrverkettet2;

    /** full constructor */
    public FLRLieferschein(String mandant_c_nr, String c_nr, String lieferscheinart_c_nr, String c_bez_projektbezeichnung, String c_lieferartort, Date t_liefertermin, Date d_belegdatum, String lieferscheinstatus_status_c_nr, Integer lager_i_id, Integer ziellager_i_id, String waehrung_c_nr_lieferscheinwaehrung, Double f_wechselkursmandantwaehrungzulieferscheinwaehrung, Short b_verrechenbar, Integer kunde_i_id_lieferadresse, Integer kunde_i_id_rechnungsadresse, Integer zahlungsziel_i_id, Integer kostenstelle_i_id, Integer spediteur_i_id, Integer lieferart_i_id, Integer personal_i_id_vertreter, BigDecimal n_gesamtwertinlieferscheinwaehrung, String c_bestellnummer, String c_kommission, Integer auftrag_i_id, Integer projekt_i_id, Date t_versandzeitpunkt, String c_versandtype, Date t_aendern, Date t_zollexportpapier, FLRAuftrag flrauftrag, FLRRechnung flrrechnung, FLRKunde flrkunde, FLRKunde flrkunderechnungsadresse, FLRKostenstelle flrkostenstelle, FLRPersonal flrvertreter, FLRPersonal flrpersonalanleger, FLRPersonal flrpersonalaenderer, FLRLager flrlager, FLRLager flrziellager, com.lp.server.lieferschein.fastlanereader.generated.FLRBegruendung flrbegruendung, Set flrlieferscheinpositionen, Set flrverkettet, Set flrverkettet2) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.lieferscheinart_c_nr = lieferscheinart_c_nr;
        this.c_bez_projektbezeichnung = c_bez_projektbezeichnung;
        this.c_lieferartort = c_lieferartort;
        this.t_liefertermin = t_liefertermin;
        this.d_belegdatum = d_belegdatum;
        this.lieferscheinstatus_status_c_nr = lieferscheinstatus_status_c_nr;
        this.lager_i_id = lager_i_id;
        this.ziellager_i_id = ziellager_i_id;
        this.waehrung_c_nr_lieferscheinwaehrung = waehrung_c_nr_lieferscheinwaehrung;
        this.f_wechselkursmandantwaehrungzulieferscheinwaehrung = f_wechselkursmandantwaehrungzulieferscheinwaehrung;
        this.b_verrechenbar = b_verrechenbar;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.spediteur_i_id = spediteur_i_id;
        this.lieferart_i_id = lieferart_i_id;
        this.personal_i_id_vertreter = personal_i_id_vertreter;
        this.n_gesamtwertinlieferscheinwaehrung = n_gesamtwertinlieferscheinwaehrung;
        this.c_bestellnummer = c_bestellnummer;
        this.c_kommission = c_kommission;
        this.auftrag_i_id = auftrag_i_id;
        this.projekt_i_id = projekt_i_id;
        this.t_versandzeitpunkt = t_versandzeitpunkt;
        this.c_versandtype = c_versandtype;
        this.t_aendern = t_aendern;
        this.t_zollexportpapier = t_zollexportpapier;
        this.flrauftrag = flrauftrag;
        this.flrrechnung = flrrechnung;
        this.flrkunde = flrkunde;
        this.flrkunderechnungsadresse = flrkunderechnungsadresse;
        this.flrkostenstelle = flrkostenstelle;
        this.flrvertreter = flrvertreter;
        this.flrpersonalanleger = flrpersonalanleger;
        this.flrpersonalaenderer = flrpersonalaenderer;
        this.flrlager = flrlager;
        this.flrziellager = flrziellager;
        this.flrbegruendung = flrbegruendung;
        this.flrlieferscheinpositionen = flrlieferscheinpositionen;
        this.flrverkettet = flrverkettet;
        this.flrverkettet2 = flrverkettet2;
    }

    /** default constructor */
    public FLRLieferschein() {
    }

    /** minimal constructor */
    public FLRLieferschein(Set flrlieferscheinpositionen, Set flrverkettet, Set flrverkettet2) {
        this.flrlieferscheinpositionen = flrlieferscheinpositionen;
        this.flrverkettet = flrverkettet;
        this.flrverkettet2 = flrverkettet2;
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

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getLieferscheinart_c_nr() {
        return this.lieferscheinart_c_nr;
    }

    public void setLieferscheinart_c_nr(String lieferscheinart_c_nr) {
        this.lieferscheinart_c_nr = lieferscheinart_c_nr;
    }

    public String getC_bez_projektbezeichnung() {
        return this.c_bez_projektbezeichnung;
    }

    public void setC_bez_projektbezeichnung(String c_bez_projektbezeichnung) {
        this.c_bez_projektbezeichnung = c_bez_projektbezeichnung;
    }

    public String getC_lieferartort() {
        return this.c_lieferartort;
    }

    public void setC_lieferartort(String c_lieferartort) {
        this.c_lieferartort = c_lieferartort;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getD_belegdatum() {
        return this.d_belegdatum;
    }

    public void setD_belegdatum(Date d_belegdatum) {
        this.d_belegdatum = d_belegdatum;
    }

    public String getLieferscheinstatus_status_c_nr() {
        return this.lieferscheinstatus_status_c_nr;
    }

    public void setLieferscheinstatus_status_c_nr(String lieferscheinstatus_status_c_nr) {
        this.lieferscheinstatus_status_c_nr = lieferscheinstatus_status_c_nr;
    }

    public Integer getLager_i_id() {
        return this.lager_i_id;
    }

    public void setLager_i_id(Integer lager_i_id) {
        this.lager_i_id = lager_i_id;
    }

    public Integer getZiellager_i_id() {
        return this.ziellager_i_id;
    }

    public void setZiellager_i_id(Integer ziellager_i_id) {
        this.ziellager_i_id = ziellager_i_id;
    }

    public String getWaehrung_c_nr_lieferscheinwaehrung() {
        return this.waehrung_c_nr_lieferscheinwaehrung;
    }

    public void setWaehrung_c_nr_lieferscheinwaehrung(String waehrung_c_nr_lieferscheinwaehrung) {
        this.waehrung_c_nr_lieferscheinwaehrung = waehrung_c_nr_lieferscheinwaehrung;
    }

    public Double getF_wechselkursmandantwaehrungzulieferscheinwaehrung() {
        return this.f_wechselkursmandantwaehrungzulieferscheinwaehrung;
    }

    public void setF_wechselkursmandantwaehrungzulieferscheinwaehrung(Double f_wechselkursmandantwaehrungzulieferscheinwaehrung) {
        this.f_wechselkursmandantwaehrungzulieferscheinwaehrung = f_wechselkursmandantwaehrungzulieferscheinwaehrung;
    }

    public Short getB_verrechenbar() {
        return this.b_verrechenbar;
    }

    public void setB_verrechenbar(Short b_verrechenbar) {
        this.b_verrechenbar = b_verrechenbar;
    }

    public Integer getKunde_i_id_lieferadresse() {
        return this.kunde_i_id_lieferadresse;
    }

    public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
    }

    public Integer getKunde_i_id_rechnungsadresse() {
        return this.kunde_i_id_rechnungsadresse;
    }

    public void setKunde_i_id_rechnungsadresse(Integer kunde_i_id_rechnungsadresse) {
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
    }

    public Integer getZahlungsziel_i_id() {
        return this.zahlungsziel_i_id;
    }

    public void setZahlungsziel_i_id(Integer zahlungsziel_i_id) {
        this.zahlungsziel_i_id = zahlungsziel_i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getSpediteur_i_id() {
        return this.spediteur_i_id;
    }

    public void setSpediteur_i_id(Integer spediteur_i_id) {
        this.spediteur_i_id = spediteur_i_id;
    }

    public Integer getLieferart_i_id() {
        return this.lieferart_i_id;
    }

    public void setLieferart_i_id(Integer lieferart_i_id) {
        this.lieferart_i_id = lieferart_i_id;
    }

    public Integer getPersonal_i_id_vertreter() {
        return this.personal_i_id_vertreter;
    }

    public void setPersonal_i_id_vertreter(Integer personal_i_id_vertreter) {
        this.personal_i_id_vertreter = personal_i_id_vertreter;
    }

    public BigDecimal getN_gesamtwertinlieferscheinwaehrung() {
        return this.n_gesamtwertinlieferscheinwaehrung;
    }

    public void setN_gesamtwertinlieferscheinwaehrung(BigDecimal n_gesamtwertinlieferscheinwaehrung) {
        this.n_gesamtwertinlieferscheinwaehrung = n_gesamtwertinlieferscheinwaehrung;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public String getC_kommission() {
        return this.c_kommission;
    }

    public void setC_kommission(String c_kommission) {
        this.c_kommission = c_kommission;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Date getT_versandzeitpunkt() {
        return this.t_versandzeitpunkt;
    }

    public void setT_versandzeitpunkt(Date t_versandzeitpunkt) {
        this.t_versandzeitpunkt = t_versandzeitpunkt;
    }

    public String getC_versandtype() {
        return this.c_versandtype;
    }

    public void setC_versandtype(String c_versandtype) {
        this.c_versandtype = c_versandtype;
    }

    public Date getT_aendern() {
        return this.t_aendern;
    }

    public void setT_aendern(Date t_aendern) {
        this.t_aendern = t_aendern;
    }

    public Date getT_zollexportpapier() {
        return this.t_zollexportpapier;
    }

    public void setT_zollexportpapier(Date t_zollexportpapier) {
        this.t_zollexportpapier = t_zollexportpapier;
    }

    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRRechnung getFlrrechnung() {
        return this.flrrechnung;
    }

    public void setFlrrechnung(FLRRechnung flrrechnung) {
        this.flrrechnung = flrrechnung;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRKunde getFlrkunderechnungsadresse() {
        return this.flrkunderechnungsadresse;
    }

    public void setFlrkunderechnungsadresse(FLRKunde flrkunderechnungsadresse) {
        this.flrkunderechnungsadresse = flrkunderechnungsadresse;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
    }

    public FLRPersonal getFlrpersonalanleger() {
        return this.flrpersonalanleger;
    }

    public void setFlrpersonalanleger(FLRPersonal flrpersonalanleger) {
        this.flrpersonalanleger = flrpersonalanleger;
    }

    public FLRPersonal getFlrpersonalaenderer() {
        return this.flrpersonalaenderer;
    }

    public void setFlrpersonalaenderer(FLRPersonal flrpersonalaenderer) {
        this.flrpersonalaenderer = flrpersonalaenderer;
    }

    public FLRLager getFlrlager() {
        return this.flrlager;
    }

    public void setFlrlager(FLRLager flrlager) {
        this.flrlager = flrlager;
    }

    public FLRLager getFlrziellager() {
        return this.flrziellager;
    }

    public void setFlrziellager(FLRLager flrziellager) {
        this.flrziellager = flrziellager;
    }

    public com.lp.server.lieferschein.fastlanereader.generated.FLRBegruendung getFlrbegruendung() {
        return this.flrbegruendung;
    }

    public void setFlrbegruendung(com.lp.server.lieferschein.fastlanereader.generated.FLRBegruendung flrbegruendung) {
        this.flrbegruendung = flrbegruendung;
    }

    public Set getFlrlieferscheinpositionen() {
        return this.flrlieferscheinpositionen;
    }

    public void setFlrlieferscheinpositionen(Set flrlieferscheinpositionen) {
        this.flrlieferscheinpositionen = flrlieferscheinpositionen;
    }

    public Set getFlrverkettet() {
        return this.flrverkettet;
    }

    public void setFlrverkettet(Set flrverkettet) {
        this.flrverkettet = flrverkettet;
    }

    public Set getFlrverkettet2() {
        return this.flrverkettet2;
    }

    public void setFlrverkettet2(Set flrverkettet2) {
        this.flrverkettet2 = flrverkettet2;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
