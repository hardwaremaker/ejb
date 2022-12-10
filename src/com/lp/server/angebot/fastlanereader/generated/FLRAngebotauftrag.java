package com.lp.server.angebot.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAngebotauftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer angebot_i_id;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_bestellnummer;

    /** nullable persistent field */
    private Date t_belegdatum;

    /** nullable persistent field */
    private Date t_angebot_nachfasstermin;

    /** nullable persistent field */
    private Date t_auftrag_liefertermin;

    /** nullable persistent field */
    private Date t_positionsliefertermin;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private BigDecimal n_wert;

    /** nullable persistent field */
    private Integer kunde_i_id;
    /** nullable persistent field */
    private Integer vertreter_i_id;

    /** nullable persistent field */
    private com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot;

    /** nullable persistent field */
    private FLRAuftrag flrauftrag;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRPersonal flrvertreter;

    /** full constructor */
    public FLRAngebotauftrag(Integer angebot_i_id, Integer auftrag_i_id, String mandant_c_nr, String c_nr, String c_bez, String c_bestellnummer, Date t_belegdatum, Date t_angebot_nachfasstermin, Date t_auftrag_liefertermin, Date t_positionsliefertermin, String status_c_nr, String waehrung_c_nr, BigDecimal n_wert, Integer kunde_i_id, Integer vertreter_i_id, com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot, FLRAuftrag flrauftrag, FLRKunde flrkunde, FLRPersonal flrvertreter) {
        this.angebot_i_id = angebot_i_id;
        this.auftrag_i_id = auftrag_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.c_bez = c_bez;
        this.c_bestellnummer = c_bestellnummer;
        this.t_belegdatum = t_belegdatum;
        this.t_angebot_nachfasstermin = t_angebot_nachfasstermin;
        this.t_auftrag_liefertermin = t_auftrag_liefertermin;
        this.t_positionsliefertermin = t_positionsliefertermin;
        this.status_c_nr = status_c_nr;
        this.waehrung_c_nr = waehrung_c_nr;
        this.n_wert = n_wert;
        this.kunde_i_id = kunde_i_id;
        this.vertreter_i_id = vertreter_i_id;
        this.flrkunde = flrkunde;
        this.flrangebot = flrangebot;
        this.flrauftrag = flrauftrag;
        this.flrvertreter = flrvertreter;
    }

    /** default constructor */
    public FLRAngebotauftrag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getAngebot_i_id() {
        return this.angebot_i_id;
    }

    public void setAngebot_i_id(Integer angebot_i_id) {
        this.angebot_i_id = angebot_i_id;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
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

    public String getC_bestellnummer() {
        return this.c_bestellnummer;
    }

    public void setC_bestellnummer(String c_bestellnummer) {
        this.c_bestellnummer = c_bestellnummer;
    }

    public Date getT_belegdatum() {
        return this.t_belegdatum;
    }

    public void setT_belegdatum(Date t_belegdatum) {
        this.t_belegdatum = t_belegdatum;
    }

    public Date getT_angebot_nachfasstermin() {
        return this.t_angebot_nachfasstermin;
    }

    public void setT_angebot_nachfasstermin(Date t_angebot_nachfasstermin) {
        this.t_angebot_nachfasstermin = t_angebot_nachfasstermin;
    }

    public Date getT_auftrag_liefertermin() {
        return this.t_auftrag_liefertermin;
    }

    public void setT_auftrag_liefertermin(Date t_auftrag_liefertermin) {
        this.t_auftrag_liefertermin = t_auftrag_liefertermin;
    }

    public Date getT_positionsliefertermin() {
        return this.t_positionsliefertermin;
    }

    public void setT_positionsliefertermin(Date t_positionsliefertermin) {
        this.t_positionsliefertermin = t_positionsliefertermin;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public BigDecimal getN_wert() {
        return this.n_wert;
    }

    public void setN_wert(BigDecimal n_wert) {
        this.n_wert = n_wert;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getVertreter_i_id() {
        return this.vertreter_i_id;
    }

    public void setVertreter_i_id(Integer vertreter_i_id) {
        this.vertreter_i_id = vertreter_i_id;
    }

    public com.lp.server.angebot.fastlanereader.generated.FLRAngebot getFlrangebot() {
        return this.flrangebot;
    }

    public void setFlrangebot(com.lp.server.angebot.fastlanereader.generated.FLRAngebot flrangebot) {
        this.flrangebot = flrangebot;
    }

    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRPersonal getFlrvertreter() {
        return this.flrvertreter;
    }

    public void setFlrvertreter(FLRPersonal flrvertreter) {
        this.flrvertreter = flrvertreter;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
