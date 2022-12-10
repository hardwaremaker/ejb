package com.lp.server.angebotstkl.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREkaglieferant implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer einkaufsangebot_i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private String waehrung_c_nr;

    /** nullable persistent field */
    private BigDecimal n_aufschlag;

    /** nullable persistent field */
    private Date t_import;

    /** nullable persistent field */
    private Date t_versand;

    /** nullable persistent field */
    private String c_angebotsnummer;

    /** nullable persistent field */
    private com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot;

    /** nullable persistent field */
    private FLRLieferant flrlieferant;

    /** full constructor */
    public FLREkaglieferant(Integer einkaufsangebot_i_id, Integer lieferant_i_id, String waehrung_c_nr, BigDecimal n_aufschlag, Date t_import, Date t_versand, String c_angebotsnummer, com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot, FLRLieferant flrlieferant) {
        this.einkaufsangebot_i_id = einkaufsangebot_i_id;
        this.lieferant_i_id = lieferant_i_id;
        this.waehrung_c_nr = waehrung_c_nr;
        this.n_aufschlag = n_aufschlag;
        this.t_import = t_import;
        this.t_versand = t_versand;
        this.c_angebotsnummer = c_angebotsnummer;
        this.flreinkaufsangebot = flreinkaufsangebot;
        this.flrlieferant = flrlieferant;
    }

    /** default constructor */
    public FLREkaglieferant() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEinkaufsangebot_i_id() {
        return this.einkaufsangebot_i_id;
    }

    public void setEinkaufsangebot_i_id(Integer einkaufsangebot_i_id) {
        this.einkaufsangebot_i_id = einkaufsangebot_i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public String getWaehrung_c_nr() {
        return this.waehrung_c_nr;
    }

    public void setWaehrung_c_nr(String waehrung_c_nr) {
        this.waehrung_c_nr = waehrung_c_nr;
    }

    public BigDecimal getN_aufschlag() {
        return this.n_aufschlag;
    }

    public void setN_aufschlag(BigDecimal n_aufschlag) {
        this.n_aufschlag = n_aufschlag;
    }

    public Date getT_import() {
        return this.t_import;
    }

    public void setT_import(Date t_import) {
        this.t_import = t_import;
    }

    public Date getT_versand() {
        return this.t_versand;
    }

    public void setT_versand(Date t_versand) {
        this.t_versand = t_versand;
    }

    public String getC_angebotsnummer() {
        return this.c_angebotsnummer;
    }

    public void setC_angebotsnummer(String c_angebotsnummer) {
        this.c_angebotsnummer = c_angebotsnummer;
    }

    public com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot getFlreinkaufsangebot() {
        return this.flreinkaufsangebot;
    }

    public void setFlreinkaufsangebot(com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebot flreinkaufsangebot) {
        this.flreinkaufsangebot = flreinkaufsangebot;
    }

    public FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
