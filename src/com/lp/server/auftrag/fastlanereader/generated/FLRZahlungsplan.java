package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_termin;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_betrag_ursprung;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** persistent field */
    private Set flrzahlungsplanmeilenstein;

    /** full constructor */
    public FLRZahlungsplan(Date t_termin, BigDecimal n_betrag, BigDecimal n_betrag_ursprung, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, Set flrzahlungsplanmeilenstein) {
        this.t_termin = t_termin;
        this.n_betrag = n_betrag;
        this.n_betrag_ursprung = n_betrag_ursprung;
        this.flrauftrag = flrauftrag;
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    /** default constructor */
    public FLRZahlungsplan() {
    }

    /** minimal constructor */
    public FLRZahlungsplan(Set flrzahlungsplanmeilenstein) {
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_termin() {
        return this.t_termin;
    }

    public void setT_termin(Date t_termin) {
        this.t_termin = t_termin;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_betrag_ursprung() {
        return this.n_betrag_ursprung;
    }

    public void setN_betrag_ursprung(BigDecimal n_betrag_ursprung) {
        this.n_betrag_ursprung = n_betrag_ursprung;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public Set getFlrzahlungsplanmeilenstein() {
        return this.flrzahlungsplanmeilenstein;
    }

    public void setFlrzahlungsplanmeilenstein(Set flrzahlungsplanmeilenstein) {
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
