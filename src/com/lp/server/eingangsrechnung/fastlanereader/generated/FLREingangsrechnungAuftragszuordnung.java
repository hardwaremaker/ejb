package com.lp.server.eingangsrechnung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungAuftragszuordnung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private String c_text;

    /** nullable persistent field */
    private Short b_keine_auftragswertung;

    /** nullable persistent field */
    private Double f_verrechenbar;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_erledigt;

    /** nullable persistent field */
    private FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung;

    /** full constructor */
    public FLREingangsrechnungAuftragszuordnung(Integer eingangsrechnung_i_id, BigDecimal n_betrag, String c_text, Short b_keine_auftragswertung, Double f_verrechenbar, Date t_erledigt, FLRPersonal flrpersonal_erledigt, FLRAuftragReport flrauftrag, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.n_betrag = n_betrag;
        this.c_text = c_text;
        this.b_keine_auftragswertung = b_keine_auftragswertung;
        this.f_verrechenbar = f_verrechenbar;
        this.t_erledigt = t_erledigt;
        this.flrpersonal_erledigt = flrpersonal_erledigt;
        this.flrauftrag = flrauftrag;
        this.flreingangsrechnung = flreingangsrechnung;
    }

    /** default constructor */
    public FLREingangsrechnungAuftragszuordnung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public Short getB_keine_auftragswertung() {
        return this.b_keine_auftragswertung;
    }

    public void setB_keine_auftragswertung(Short b_keine_auftragswertung) {
        this.b_keine_auftragswertung = b_keine_auftragswertung;
    }

    public Double getF_verrechenbar() {
        return this.f_verrechenbar;
    }

    public void setF_verrechenbar(Double f_verrechenbar) {
        this.f_verrechenbar = f_verrechenbar;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public FLRPersonal getFlrpersonal_erledigt() {
        return this.flrpersonal_erledigt;
    }

    public void setFlrpersonal_erledigt(FLRPersonal flrpersonal_erledigt) {
        this.flrpersonal_erledigt = flrpersonal_erledigt;
    }

    public FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
