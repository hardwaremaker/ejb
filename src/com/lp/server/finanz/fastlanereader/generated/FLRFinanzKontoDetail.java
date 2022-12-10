package com.lp.server.finanz.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatz;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFinanzKontoDetail implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String kontotyp_c_nr;

    /** nullable persistent field */
    private Integer ergebnisgruppe_i_id;

    /** nullable persistent field */
    private Date d_gueltigbis;

    /** nullable persistent field */
    private Short b_allgemeinsichtbar;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private String kontoart_c_nr;

    /** nullable persistent field */
    private Short b_manuellbebuchbar;

    /** nullable persistent field */
    private Integer uvaart_i_id;

    /** nullable persistent field */
    private Integer finanzamt_i_id;

    /** nullable persistent field */
    private String c_sortierung;

    /** nullable persistent field */
    private Date d_gueltigvon;

    /** nullable persistent field */
    private Short b_automeroeffnungsbuchung;

    /** nullable persistent field */
    private String waehrung_c_nr_druck;

    /** nullable persistent field */
    private Date d_eb_anlegen;

    /** nullable persistent field */
    private Integer i_eb_geschaeftsjahr;

    /** nullable persistent field */
    private String c_steuerart;

    /** nullable persistent field */
    private FLRMwstsatz flrmwstsatz;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoust;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoskonto;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKontoart flrkontoart;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzUVAArt flruvaart;

    /** persistent field */
    private Set flrfinanzbuchungdetail;

    /** full constructor */
    public FLRFinanzKontoDetail(String mandant_c_nr, String c_nr, String c_bez, String kontotyp_c_nr, Integer ergebnisgruppe_i_id, Date d_gueltigbis, Short b_allgemeinsichtbar, Short b_versteckt, String kontoart_c_nr, Short b_manuellbebuchbar, Integer uvaart_i_id, Integer finanzamt_i_id, String c_sortierung, Date d_gueltigvon, Short b_automeroeffnungsbuchung, String waehrung_c_nr_druck, Date d_eb_anlegen, Integer i_eb_geschaeftsjahr, String c_steuerart, FLRMwstsatz flrmwstsatz, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoust, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoskonto, FLRKostenstelle flrkostenstelle, com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKontoart flrkontoart, com.lp.server.finanz.fastlanereader.generated.FLRFinanzUVAArt flruvaart, Set flrfinanzbuchungdetail) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.kontotyp_c_nr = kontotyp_c_nr;
        this.ergebnisgruppe_i_id = ergebnisgruppe_i_id;
        this.d_gueltigbis = d_gueltigbis;
        this.b_allgemeinsichtbar = b_allgemeinsichtbar;
        this.b_versteckt = b_versteckt;
        this.kontoart_c_nr = kontoart_c_nr;
        this.b_manuellbebuchbar = b_manuellbebuchbar;
        this.uvaart_i_id = uvaart_i_id;
        this.finanzamt_i_id = finanzamt_i_id;
        this.c_sortierung = c_sortierung;
        this.d_gueltigvon = d_gueltigvon;
        this.b_automeroeffnungsbuchung = b_automeroeffnungsbuchung;
        this.waehrung_c_nr_druck = waehrung_c_nr_druck;
        this.d_eb_anlegen = d_eb_anlegen;
        this.i_eb_geschaeftsjahr = i_eb_geschaeftsjahr;
        this.c_steuerart = c_steuerart;
        this.flrmwstsatz = flrmwstsatz;
        this.flrkontoust = flrkontoust;
        this.flrkontoskonto = flrkontoskonto;
        this.flrkostenstelle = flrkostenstelle;
        this.flrergebnisgruppe = flrergebnisgruppe;
        this.flrkontoart = flrkontoart;
        this.flruvaart = flruvaart;
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
    }

    /** default constructor */
    public FLRFinanzKontoDetail() {
    }

    /** minimal constructor */
    public FLRFinanzKontoDetail(Set flrfinanzbuchungdetail) {
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
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

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getKontotyp_c_nr() {
        return this.kontotyp_c_nr;
    }

    public void setKontotyp_c_nr(String kontotyp_c_nr) {
        this.kontotyp_c_nr = kontotyp_c_nr;
    }

    public Integer getErgebnisgruppe_i_id() {
        return this.ergebnisgruppe_i_id;
    }

    public void setErgebnisgruppe_i_id(Integer ergebnisgruppe_i_id) {
        this.ergebnisgruppe_i_id = ergebnisgruppe_i_id;
    }

    public Date getD_gueltigbis() {
        return this.d_gueltigbis;
    }

    public void setD_gueltigbis(Date d_gueltigbis) {
        this.d_gueltigbis = d_gueltigbis;
    }

    public Short getB_allgemeinsichtbar() {
        return this.b_allgemeinsichtbar;
    }

    public void setB_allgemeinsichtbar(Short b_allgemeinsichtbar) {
        this.b_allgemeinsichtbar = b_allgemeinsichtbar;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public String getKontoart_c_nr() {
        return this.kontoart_c_nr;
    }

    public void setKontoart_c_nr(String kontoart_c_nr) {
        this.kontoart_c_nr = kontoart_c_nr;
    }

    public Short getB_manuellbebuchbar() {
        return this.b_manuellbebuchbar;
    }

    public void setB_manuellbebuchbar(Short b_manuellbebuchbar) {
        this.b_manuellbebuchbar = b_manuellbebuchbar;
    }

    public Integer getUvaart_i_id() {
        return this.uvaart_i_id;
    }

    public void setUvaart_i_id(Integer uvaart_i_id) {
        this.uvaart_i_id = uvaart_i_id;
    }

    public Integer getFinanzamt_i_id() {
        return this.finanzamt_i_id;
    }

    public void setFinanzamt_i_id(Integer finanzamt_i_id) {
        this.finanzamt_i_id = finanzamt_i_id;
    }

    public String getC_sortierung() {
        return this.c_sortierung;
    }

    public void setC_sortierung(String c_sortierung) {
        this.c_sortierung = c_sortierung;
    }

    public Date getD_gueltigvon() {
        return this.d_gueltigvon;
    }

    public void setD_gueltigvon(Date d_gueltigvon) {
        this.d_gueltigvon = d_gueltigvon;
    }

    public Short getB_automeroeffnungsbuchung() {
        return this.b_automeroeffnungsbuchung;
    }

    public void setB_automeroeffnungsbuchung(Short b_automeroeffnungsbuchung) {
        this.b_automeroeffnungsbuchung = b_automeroeffnungsbuchung;
    }

    public String getWaehrung_c_nr_druck() {
        return this.waehrung_c_nr_druck;
    }

    public void setWaehrung_c_nr_druck(String waehrung_c_nr_druck) {
        this.waehrung_c_nr_druck = waehrung_c_nr_druck;
    }

    public Date getD_eb_anlegen() {
        return this.d_eb_anlegen;
    }

    public void setD_eb_anlegen(Date d_eb_anlegen) {
        this.d_eb_anlegen = d_eb_anlegen;
    }

    public Integer getI_eb_geschaeftsjahr() {
        return this.i_eb_geschaeftsjahr;
    }

    public void setI_eb_geschaeftsjahr(Integer i_eb_geschaeftsjahr) {
        this.i_eb_geschaeftsjahr = i_eb_geschaeftsjahr;
    }

    public String getC_steuerart() {
        return this.c_steuerart;
    }

    public void setC_steuerart(String c_steuerart) {
        this.c_steuerart = c_steuerart;
    }

    public FLRMwstsatz getFlrmwstsatz() {
        return this.flrmwstsatz;
    }

    public void setFlrmwstsatz(FLRMwstsatz flrmwstsatz) {
        this.flrmwstsatz = flrmwstsatz;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkontoust() {
        return this.flrkontoust;
    }

    public void setFlrkontoust(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoust) {
        this.flrkontoust = flrkontoust;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkontoskonto() {
        return this.flrkontoskonto;
    }

    public void setFlrkontoskonto(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkontoskonto) {
        this.flrkontoskonto = flrkontoskonto;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe getFlrergebnisgruppe() {
        return this.flrergebnisgruppe;
    }

    public void setFlrergebnisgruppe(com.lp.server.finanz.fastlanereader.generated.FLRFinanzErgebnisgruppe flrergebnisgruppe) {
        this.flrergebnisgruppe = flrergebnisgruppe;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKontoart getFlrkontoart() {
        return this.flrkontoart;
    }

    public void setFlrkontoart(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKontoart flrkontoart) {
        this.flrkontoart = flrkontoart;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzUVAArt getFlruvaart() {
        return this.flruvaart;
    }

    public void setFlruvaart(com.lp.server.finanz.fastlanereader.generated.FLRFinanzUVAArt flruvaart) {
        this.flruvaart = flruvaart;
    }

    public Set getFlrfinanzbuchungdetail() {
        return this.flrfinanzbuchungdetail;
    }

    public void setFlrfinanzbuchungdetail(Set flrfinanzbuchungdetail) {
        this.flrfinanzbuchungdetail = flrfinanzbuchungdetail;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
