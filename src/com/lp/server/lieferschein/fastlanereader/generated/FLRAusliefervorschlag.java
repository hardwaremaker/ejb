package com.lp.server.lieferschein.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.system.fastlanereader.generated.FLRBelegart;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAusliefervorschlag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private BigDecimal n_verfuegbar;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_ausliefertermin;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private Integer i_belegartid;

    /** nullable persistent field */
    private Integer i_belegartpositionid;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRKunde flrkunde_lieferadresse;

    /** nullable persistent field */
    private FLRBelegart flrbelegart;

    /** full constructor */
    public FLRAusliefervorschlag(BigDecimal n_verfuegbar, BigDecimal n_menge, Date t_ausliefertermin, Integer artikel_i_id, Integer kunde_i_id, Integer kunde_i_id_lieferadresse, String belegart_c_nr, Integer i_belegartid, Integer i_belegartpositionid, String mandant_c_nr, FLRArtikel flrartikel, FLRArtikelliste flrartikelliste, FLRKunde flrkunde, FLRKunde flrkunde_lieferadresse, FLRBelegart flrbelegart) {
        this.n_verfuegbar = n_verfuegbar;
        this.n_menge = n_menge;
        this.t_ausliefertermin = t_ausliefertermin;
        this.artikel_i_id = artikel_i_id;
        this.kunde_i_id = kunde_i_id;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.belegart_c_nr = belegart_c_nr;
        this.i_belegartid = i_belegartid;
        this.i_belegartpositionid = i_belegartpositionid;
        this.mandant_c_nr = mandant_c_nr;
        this.flrartikel = flrartikel;
        this.flrartikelliste = flrartikelliste;
        this.flrkunde = flrkunde;
        this.flrkunde_lieferadresse = flrkunde_lieferadresse;
        this.flrbelegart = flrbelegart;
    }

    /** default constructor */
    public FLRAusliefervorschlag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public BigDecimal getN_verfuegbar() {
        return this.n_verfuegbar;
    }

    public void setN_verfuegbar(BigDecimal n_verfuegbar) {
        this.n_verfuegbar = n_verfuegbar;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Date getT_ausliefertermin() {
        return this.t_ausliefertermin;
    }

    public void setT_ausliefertermin(Date t_ausliefertermin) {
        this.t_ausliefertermin = t_ausliefertermin;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getKunde_i_id_lieferadresse() {
        return this.kunde_i_id_lieferadresse;
    }

    public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
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

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRKunde getFlrkunde_lieferadresse() {
        return this.flrkunde_lieferadresse;
    }

    public void setFlrkunde_lieferadresse(FLRKunde flrkunde_lieferadresse) {
        this.flrkunde_lieferadresse = flrkunde_lieferadresse;
    }

    public FLRBelegart getFlrbelegart() {
        return this.flrbelegart;
    }

    public void setFlrbelegart(FLRBelegart flrbelegart) {
        this.flrbelegart = flrbelegart;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
