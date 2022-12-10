package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungAuftragszuordnung;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRMaschinenzeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAbrechnungsvorschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr_rechnung;

    /** nullable persistent field */
    private BigDecimal n_betrag_verrechenbar;

    /** nullable persistent field */
    private BigDecimal n_stunden_verrechenbar;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private BigDecimal n_betrag_offen;

    /** nullable persistent field */
    private BigDecimal n_stunden_offen;

    /** nullable persistent field */
    private BigDecimal n_betrag_gesamt;

    /** nullable persistent field */
    private BigDecimal n_stunden_gesamt;

    /** nullable persistent field */
    private BigDecimal n_kilometer_gesamt;

    /** nullable persistent field */
    private BigDecimal n_kilometer_verrechenbar;

    /** nullable persistent field */
    private BigDecimal n_kilometer_offen;

    /** nullable persistent field */
    private BigDecimal n_spesen_gesamt;

    /** nullable persistent field */
    private BigDecimal n_spesen_verrechenbar;

    /** nullable persistent field */
    private BigDecimal n_spesen_offen;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer zeitdaten_i_id;

    /** nullable persistent field */
    private Integer reise_i_id;

    /** nullable persistent field */
    private Integer auftragszuordnung_i_id;

    /** nullable persistent field */
    private Integer telefonzeiten_i_id;

    /** nullable persistent field */
    private Integer maschinenzeitdaten_i_id;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Short b_verrechnet;

    /** nullable persistent field */
    private Date t_von;

    /** nullable persistent field */
    private Date t_bis;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private FLRMaschinenzeitdaten flrmaschinenzeitdaten;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRZeitdaten flrzeitdaten;

    /** nullable persistent field */
    private FLRReise flrreise;

    /** nullable persistent field */
    private FLRTelefonzeiten flrtelefonzeiten;

    /** nullable persistent field */
    private FLREingangsrechnungAuftragszuordnung flrauftragszuordnung;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRLos flrlos;

    /** nullable persistent field */
    private FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** full constructor */
    public FLRAbrechnungsvorschlag(String mandant_c_nr, String waehrung_c_nr_rechnung, BigDecimal n_betrag_verrechenbar, BigDecimal n_stunden_verrechenbar, Integer auftragposition_i_id, BigDecimal n_betrag_offen, BigDecimal n_stunden_offen, BigDecimal n_betrag_gesamt, BigDecimal n_stunden_gesamt, BigDecimal n_kilometer_gesamt, BigDecimal n_kilometer_verrechenbar, BigDecimal n_kilometer_offen, BigDecimal n_spesen_gesamt, BigDecimal n_spesen_verrechenbar, BigDecimal n_spesen_offen, Integer auftrag_i_id, Integer projekt_i_id, Integer personal_i_id, Integer los_i_id, Integer zeitdaten_i_id, Integer reise_i_id, Integer auftragszuordnung_i_id, Integer telefonzeiten_i_id, Integer maschinenzeitdaten_i_id, Date t_anlegen, Short b_verrechnet, Date t_von, Date t_bis, Double f_verrechenbar, FLRMaschinenzeitdaten flrmaschinenzeitdaten, FLRPersonal flrpersonal, FLRZeitdaten flrzeitdaten, FLRReise flrreise, FLRTelefonzeiten flrtelefonzeiten, FLREingangsrechnungAuftragszuordnung flrauftragszuordnung, FLRProjekt flrprojekt, FLRLos flrlos, FLRAuftragReport flrauftrag, FLRKunde flrkunde, FLRAuftragposition flrauftragposition) {
        this.mandant_c_nr = mandant_c_nr;
        this.waehrung_c_nr_rechnung = waehrung_c_nr_rechnung;
        this.n_betrag_verrechenbar = n_betrag_verrechenbar;
        this.n_stunden_verrechenbar = n_stunden_verrechenbar;
        this.auftragposition_i_id = auftragposition_i_id;
        this.n_betrag_offen = n_betrag_offen;
        this.n_stunden_offen = n_stunden_offen;
        this.n_betrag_gesamt = n_betrag_gesamt;
        this.n_stunden_gesamt = n_stunden_gesamt;
        this.n_kilometer_gesamt = n_kilometer_gesamt;
        this.n_kilometer_verrechenbar = n_kilometer_verrechenbar;
        this.n_kilometer_offen = n_kilometer_offen;
        this.n_spesen_gesamt = n_spesen_gesamt;
        this.n_spesen_verrechenbar = n_spesen_verrechenbar;
        this.n_spesen_offen = n_spesen_offen;
        this.auftrag_i_id = auftrag_i_id;
        this.projekt_i_id = projekt_i_id;
        this.personal_i_id = personal_i_id;
        this.los_i_id = los_i_id;
        this.zeitdaten_i_id = zeitdaten_i_id;
        this.reise_i_id = reise_i_id;
        this.auftragszuordnung_i_id = auftragszuordnung_i_id;
        this.telefonzeiten_i_id = telefonzeiten_i_id;
        this.maschinenzeitdaten_i_id = maschinenzeitdaten_i_id;
        this.t_anlegen = t_anlegen;
        this.b_verrechnet = b_verrechnet;
        this.t_von = t_von;
        this.t_bis = t_bis;
        this.f_verrechenbar = f_verrechenbar;
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
        this.flrpersonal = flrpersonal;
        this.flrzeitdaten = flrzeitdaten;
        this.flrreise = flrreise;
        this.flrtelefonzeiten = flrtelefonzeiten;
        this.flrauftragszuordnung = flrauftragszuordnung;
        this.flrprojekt = flrprojekt;
        this.flrlos = flrlos;
        this.flrauftrag = flrauftrag;
        this.flrkunde = flrkunde;
        this.flrauftragposition = flrauftragposition;
    }

    /** default constructor */
    public FLRAbrechnungsvorschlag() {
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

    public String getWaehrung_c_nr_rechnung() {
        return this.waehrung_c_nr_rechnung;
    }

    public void setWaehrung_c_nr_rechnung(String waehrung_c_nr_rechnung) {
        this.waehrung_c_nr_rechnung = waehrung_c_nr_rechnung;
    }

    public BigDecimal getN_betrag_verrechenbar() {
        return this.n_betrag_verrechenbar;
    }

    public void setN_betrag_verrechenbar(BigDecimal n_betrag_verrechenbar) {
        this.n_betrag_verrechenbar = n_betrag_verrechenbar;
    }

    public BigDecimal getN_stunden_verrechenbar() {
        return this.n_stunden_verrechenbar;
    }

    public void setN_stunden_verrechenbar(BigDecimal n_stunden_verrechenbar) {
        this.n_stunden_verrechenbar = n_stunden_verrechenbar;
    }

    public Integer getAuftragposition_i_id() {
        return this.auftragposition_i_id;
    }

    public void setAuftragposition_i_id(Integer auftragposition_i_id) {
        this.auftragposition_i_id = auftragposition_i_id;
    }

    public BigDecimal getN_betrag_offen() {
        return this.n_betrag_offen;
    }

    public void setN_betrag_offen(BigDecimal n_betrag_offen) {
        this.n_betrag_offen = n_betrag_offen;
    }

    public BigDecimal getN_stunden_offen() {
        return this.n_stunden_offen;
    }

    public void setN_stunden_offen(BigDecimal n_stunden_offen) {
        this.n_stunden_offen = n_stunden_offen;
    }

    public BigDecimal getN_betrag_gesamt() {
        return this.n_betrag_gesamt;
    }

    public void setN_betrag_gesamt(BigDecimal n_betrag_gesamt) {
        this.n_betrag_gesamt = n_betrag_gesamt;
    }

    public BigDecimal getN_stunden_gesamt() {
        return this.n_stunden_gesamt;
    }

    public void setN_stunden_gesamt(BigDecimal n_stunden_gesamt) {
        this.n_stunden_gesamt = n_stunden_gesamt;
    }

    public BigDecimal getN_kilometer_gesamt() {
        return this.n_kilometer_gesamt;
    }

    public void setN_kilometer_gesamt(BigDecimal n_kilometer_gesamt) {
        this.n_kilometer_gesamt = n_kilometer_gesamt;
    }

    public BigDecimal getN_kilometer_verrechenbar() {
        return this.n_kilometer_verrechenbar;
    }

    public void setN_kilometer_verrechenbar(BigDecimal n_kilometer_verrechenbar) {
        this.n_kilometer_verrechenbar = n_kilometer_verrechenbar;
    }

    public BigDecimal getN_kilometer_offen() {
        return this.n_kilometer_offen;
    }

    public void setN_kilometer_offen(BigDecimal n_kilometer_offen) {
        this.n_kilometer_offen = n_kilometer_offen;
    }

    public BigDecimal getN_spesen_gesamt() {
        return this.n_spesen_gesamt;
    }

    public void setN_spesen_gesamt(BigDecimal n_spesen_gesamt) {
        this.n_spesen_gesamt = n_spesen_gesamt;
    }

    public BigDecimal getN_spesen_verrechenbar() {
        return this.n_spesen_verrechenbar;
    }

    public void setN_spesen_verrechenbar(BigDecimal n_spesen_verrechenbar) {
        this.n_spesen_verrechenbar = n_spesen_verrechenbar;
    }

    public BigDecimal getN_spesen_offen() {
        return this.n_spesen_offen;
    }

    public void setN_spesen_offen(BigDecimal n_spesen_offen) {
        this.n_spesen_offen = n_spesen_offen;
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

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getZeitdaten_i_id() {
        return this.zeitdaten_i_id;
    }

    public void setZeitdaten_i_id(Integer zeitdaten_i_id) {
        this.zeitdaten_i_id = zeitdaten_i_id;
    }

    public Integer getReise_i_id() {
        return this.reise_i_id;
    }

    public void setReise_i_id(Integer reise_i_id) {
        this.reise_i_id = reise_i_id;
    }

    public Integer getAuftragszuordnung_i_id() {
        return this.auftragszuordnung_i_id;
    }

    public void setAuftragszuordnung_i_id(Integer auftragszuordnung_i_id) {
        this.auftragszuordnung_i_id = auftragszuordnung_i_id;
    }

    public Integer getTelefonzeiten_i_id() {
        return this.telefonzeiten_i_id;
    }

    public void setTelefonzeiten_i_id(Integer telefonzeiten_i_id) {
        this.telefonzeiten_i_id = telefonzeiten_i_id;
    }

    public Integer getMaschinenzeitdaten_i_id() {
        return this.maschinenzeitdaten_i_id;
    }

    public void setMaschinenzeitdaten_i_id(Integer maschinenzeitdaten_i_id) {
        this.maschinenzeitdaten_i_id = maschinenzeitdaten_i_id;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Short getB_verrechnet() {
        return this.b_verrechnet;
    }

    public void setB_verrechnet(Short b_verrechnet) {
        this.b_verrechnet = b_verrechnet;
    }

    public Date getT_von() {
        return this.t_von;
    }

    public void setT_von(Date t_von) {
        this.t_von = t_von;
    }

    public Date getT_bis() {
        return this.t_bis;
    }

    public void setT_bis(Date t_bis) {
        this.t_bis = t_bis;
    }

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public FLRMaschinenzeitdaten getFlrmaschinenzeitdaten() {
        return this.flrmaschinenzeitdaten;
    }

    public void setFlrmaschinenzeitdaten(FLRMaschinenzeitdaten flrmaschinenzeitdaten) {
        this.flrmaschinenzeitdaten = flrmaschinenzeitdaten;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRZeitdaten getFlrzeitdaten() {
        return this.flrzeitdaten;
    }

    public void setFlrzeitdaten(FLRZeitdaten flrzeitdaten) {
        this.flrzeitdaten = flrzeitdaten;
    }

    public FLRReise getFlrreise() {
        return this.flrreise;
    }

    public void setFlrreise(FLRReise flrreise) {
        this.flrreise = flrreise;
    }

    public FLRTelefonzeiten getFlrtelefonzeiten() {
        return this.flrtelefonzeiten;
    }

    public void setFlrtelefonzeiten(FLRTelefonzeiten flrtelefonzeiten) {
        this.flrtelefonzeiten = flrtelefonzeiten;
    }

    public FLREingangsrechnungAuftragszuordnung getFlrauftragszuordnung() {
        return this.flrauftragszuordnung;
    }

    public void setFlrauftragszuordnung(FLREingangsrechnungAuftragszuordnung flrauftragszuordnung) {
        this.flrauftragszuordnung = flrauftragszuordnung;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
