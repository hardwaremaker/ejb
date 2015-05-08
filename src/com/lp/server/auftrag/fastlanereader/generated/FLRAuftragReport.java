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
package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragReport implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String auftragart_c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_bestelldatum;

    /** nullable persistent field */
    private Date t_finaltermin;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private String auftragstatus_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_auftragswaehrung;

    /** nullable persistent field */
    private BigDecimal n_gesamtauftragswertinauftragswaehrung;

    /** nullable persistent field */
    private Double f_wechselkursmandantwaehrungzuauftragswaehrung;

    /** nullable persistent field */
    private Integer kunde_i_id_auftragsadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Integer kunde_i_id_rechnungsadresse;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer zahlungsziel_i_id;

    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Integer personal_i_id_erledigt;

    /** nullable persistent field */
    private Short b_teillieferungmoeglich;

    /** nullable persistent field */
    private Short b_poenale;

    /** nullable persistent field */
    private Short b_rohs;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private Short b_lieferterminunverbindlich;

    /** nullable persistent field */
    private Double f_erfuellungsgrad;

    /** nullable persistent field */
    private String x_internerkommentar;

    /** nullable persistent field */
    private Date t_verrechenbar;

    /** nullable persistent field */
    private FLRPersonal flrpersonalverrechenbar;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRAnsprechpartner flrkundeansprechpartner;

    /** nullable persistent field */
    private FLRKunde flrkunderechnungsadresse;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag;

    /** full constructor */
    public FLRAuftragReport(String mandant_c_nr, String c_nr, String auftragart_c_nr, String c_bez, String c_bestellnummer, Date t_liefertermin, Date t_bestelldatum, Date t_finaltermin, Date t_belegdatum, String auftragstatus_c_nr, String waehrung_c_nr_auftragswaehrung, BigDecimal n_gesamtauftragswertinauftragswaehrung, Double f_wechselkursmandantwaehrungzuauftragswaehrung, Integer kunde_i_id_auftragsadresse, Integer kunde_i_id_lieferadresse, Integer kunde_i_id_rechnungsadresse, Integer kostenstelle_i_id, Integer zahlungsziel_i_id, Integer vertreter_i_id, Date t_erledigt, Integer personal_i_id_erledigt, Short b_teillieferungmoeglich, Short b_poenale, Short b_rohs, Short b_versteckt, Short b_lieferterminunverbindlich, Double f_erfuellungsgrad, String x_internerkommentar, Date t_verrechenbar, FLRPersonal flrpersonalverrechenbar, FLRKunde flrkunde, FLRAnsprechpartner flrkundeansprechpartner, FLRKunde flrkunderechnungsadresse, FLRKostenstelle flrkostenstelle, FLRPersonal flrvertreter, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche, FLRProjekt flrprojekt, com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.auftragart_c_nr = auftragart_c_nr;
        this.c_bez = c_bez;
        this.c_bestellnummer = c_bestellnummer;
        this.t_liefertermin = t_liefertermin;
        this.t_bestelldatum = t_bestelldatum;
        this.t_finaltermin = t_finaltermin;
        this.t_belegdatum = t_belegdatum;
        this.auftragstatus_c_nr = auftragstatus_c_nr;
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
        this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.kunde_i_id_rechnungsadresse = kunde_i_id_rechnungsadresse;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.zahlungsziel_i_id = zahlungsziel_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.t_erledigt = t_erledigt;
        this.personal_i_id_erledigt = personal_i_id_erledigt;
        this.b_teillieferungmoeglich = b_teillieferungmoeglich;
        this.b_poenale = b_poenale;
        this.b_rohs = b_rohs;
        this.b_versteckt = b_versteckt;
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
        this.f_erfuellungsgrad = f_erfuellungsgrad;
        this.x_internerkommentar = x_internerkommentar;
        this.t_verrechenbar = t_verrechenbar;
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
        this.flrkunde = flrkunde;
        this.flrkundeansprechpartner = flrkundeansprechpartner;
        this.flrkunderechnungsadresse = flrkunderechnungsadresse;
        this.flrkostenstelle = flrkostenstelle;
        this.flrvertreter = flrvertreter;
        this.flrauftragtextsuche = flrauftragtextsuche;
        this.flrprojekt = flrprojekt;
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
    }

    /** default constructor */
    public FLRAuftragReport() {
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

    public String getAuftragart_c_nr() {
        return this.auftragart_c_nr;
    }

    public void setAuftragart_c_nr(String auftragart_c_nr) {
        this.auftragart_c_nr = auftragart_c_nr;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_bestelldatum() {
        return this.t_bestelldatum;
    }

    public void setT_bestelldatum(Date t_bestelldatum) {
        this.t_bestelldatum = t_bestelldatum;
    }

    public Date getT_finaltermin() {
        return this.t_finaltermin;
    }

    public void setT_finaltermin(Date t_finaltermin) {
        this.t_finaltermin = t_finaltermin;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public String getAuftragstatus_c_nr() {
        return this.auftragstatus_c_nr;
    }

    public void setAuftragstatus_c_nr(String auftragstatus_c_nr) {
        this.auftragstatus_c_nr = auftragstatus_c_nr;
    }

    public String getWaehrung_c_nr_auftragswaehrung() {
        return this.waehrung_c_nr_auftragswaehrung;
    }

    public void setWaehrung_c_nr_auftragswaehrung(String waehrung_c_nr_auftragswaehrung) {
        this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
    }

    public BigDecimal getN_gesamtauftragswertinauftragswaehrung() {
        return this.n_gesamtauftragswertinauftragswaehrung;
    }

    public void setN_gesamtauftragswertinauftragswaehrung(BigDecimal n_gesamtauftragswertinauftragswaehrung) {
        this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
    }

    public Double getF_wechselkursmandantwaehrungzuauftragswaehrung() {
        return this.f_wechselkursmandantwaehrungzuauftragswaehrung;
    }

    public void setF_wechselkursmandantwaehrungzuauftragswaehrung(Double f_wechselkursmandantwaehrungzuauftragswaehrung) {
        this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
    }

    public Integer getKunde_i_id_auftragsadresse() {
        return this.kunde_i_id_auftragsadresse;
    }

    public void setKunde_i_id_auftragsadresse(Integer kunde_i_id_auftragsadresse) {
        this.kunde_i_id_auftragsadresse = kunde_i_id_auftragsadresse;
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

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getZahlungsziel_i_id() {
        return this.zahlungsziel_i_id;
    }

    public void setZahlungsziel_i_id(Integer zahlungsziel_i_id) {
        this.zahlungsziel_i_id = zahlungsziel_i_id;
    }

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Integer getPersonal_i_id_erledigt() {
        return this.personal_i_id_erledigt;
    }

    public void setPersonal_i_id_erledigt(Integer personal_i_id_erledigt) {
        this.personal_i_id_erledigt = personal_i_id_erledigt;
    }

    public Short getB_teillieferungmoeglich() {
        return this.b_teillieferungmoeglich;
    }

    public void setB_teillieferungmoeglich(Short b_teillieferungmoeglich) {
        this.b_teillieferungmoeglich = b_teillieferungmoeglich;
    }

    public Short getB_poenale() {
        return this.b_poenale;
    }

    public void setB_poenale(Short b_poenale) {
        this.b_poenale = b_poenale;
    }

    public Short getB_rohs() {
        return this.b_rohs;
    }

    public void setB_rohs(Short b_rohs) {
        this.b_rohs = b_rohs;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public Short getB_lieferterminunverbindlich() {
        return this.b_lieferterminunverbindlich;
    }

    public void setB_lieferterminunverbindlich(Short b_lieferterminunverbindlich) {
        this.b_lieferterminunverbindlich = b_lieferterminunverbindlich;
    }

    public Double getF_erfuellungsgrad() {
        return this.f_erfuellungsgrad;
    }

    public void setF_erfuellungsgrad(Double f_erfuellungsgrad) {
        this.f_erfuellungsgrad = f_erfuellungsgrad;
    }

    public String getX_internerkommentar() {
        return this.x_internerkommentar;
    }

    public void setX_internerkommentar(String x_internerkommentar) {
        this.x_internerkommentar = x_internerkommentar;
    }

    public Date getT_verrechenbar() {
        return this.t_verrechenbar;
    }

    public void setT_verrechenbar(Date t_verrechenbar) {
        this.t_verrechenbar = t_verrechenbar;
    }

    public FLRPersonal getFlrpersonalverrechenbar() {
        return this.flrpersonalverrechenbar;
    }

    public void setFlrpersonalverrechenbar(FLRPersonal flrpersonalverrechenbar) {
        this.flrpersonalverrechenbar = flrpersonalverrechenbar;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRAnsprechpartner getFlrkundeansprechpartner() {
        return this.flrkundeansprechpartner;
    }

    public void setFlrkundeansprechpartner(FLRAnsprechpartner flrkundeansprechpartner) {
        this.flrkundeansprechpartner = flrkundeansprechpartner;
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

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche getFlrauftragtextsuche() {
        return this.flrauftragtextsuche;
    }

    public void setFlrauftragtextsuche(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragtextsuche flrauftragtextsuche) {
        this.flrauftragtextsuche = flrauftragtextsuche;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag getFlrauftrag_rahmenauftrag() {
        return this.flrauftrag_rahmenauftrag;
    }

    public void setFlrauftrag_rahmenauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag flrauftrag_rahmenauftrag) {
        this.flrauftrag_rahmenauftrag = flrauftrag_rahmenauftrag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
