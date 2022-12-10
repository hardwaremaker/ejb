package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_termin;

    /** nullable persistent field */
    private BigDecimal n_material;

    /** nullable persistent field */
    private BigDecimal n_material_ursprung;

    /** nullable persistent field */
    private BigDecimal n_dauer;

    /** nullable persistent field */
    private BigDecimal n_dauer_ursprung;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private Date t_material_erledigt;

    /** nullable persistent field */
    private Date t_dauer_erledigt;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRPersonal flrpersonalmaterialerledigt;

    /** nullable persistent field */
    private FLRPersonal flrpersonaldauererledigt;

    /** full constructor */
    public FLRZeitplan(Date t_termin, BigDecimal n_material, BigDecimal n_material_ursprung, BigDecimal n_dauer, BigDecimal n_dauer_ursprung, String c_kommentar, String x_text, Date t_material_erledigt, Date t_dauer_erledigt, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, FLRPersonal flrpersonalmaterialerledigt, FLRPersonal flrpersonaldauererledigt) {
        this.t_termin = t_termin;
        this.n_material = n_material;
        this.n_material_ursprung = n_material_ursprung;
        this.n_dauer = n_dauer;
        this.n_dauer_ursprung = n_dauer_ursprung;
        this.c_kommentar = c_kommentar;
        this.x_text = x_text;
        this.t_material_erledigt = t_material_erledigt;
        this.t_dauer_erledigt = t_dauer_erledigt;
        this.flrauftrag = flrauftrag;
        this.flrpersonalmaterialerledigt = flrpersonalmaterialerledigt;
        this.flrpersonaldauererledigt = flrpersonaldauererledigt;
    }

    /** default constructor */
    public FLRZeitplan() {
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

    public BigDecimal getN_material() {
        return this.n_material;
    }

    public void setN_material(BigDecimal n_material) {
        this.n_material = n_material;
    }

    public BigDecimal getN_material_ursprung() {
        return this.n_material_ursprung;
    }

    public void setN_material_ursprung(BigDecimal n_material_ursprung) {
        this.n_material_ursprung = n_material_ursprung;
    }

    public BigDecimal getN_dauer() {
        return this.n_dauer;
    }

    public void setN_dauer(BigDecimal n_dauer) {
        this.n_dauer = n_dauer;
    }

    public BigDecimal getN_dauer_ursprung() {
        return this.n_dauer_ursprung;
    }

    public void setN_dauer_ursprung(BigDecimal n_dauer_ursprung) {
        this.n_dauer_ursprung = n_dauer_ursprung;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public Date getT_material_erledigt() {
        return this.t_material_erledigt;
    }

    public void setT_material_erledigt(Date t_material_erledigt) {
        this.t_material_erledigt = t_material_erledigt;
    }

    public Date getT_dauer_erledigt() {
        return this.t_dauer_erledigt;
    }

    public void setT_dauer_erledigt(Date t_dauer_erledigt) {
        this.t_dauer_erledigt = t_dauer_erledigt;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRPersonal getFlrpersonalmaterialerledigt() {
        return this.flrpersonalmaterialerledigt;
    }

    public void setFlrpersonalmaterialerledigt(FLRPersonal flrpersonalmaterialerledigt) {
        this.flrpersonalmaterialerledigt = flrpersonalmaterialerledigt;
    }

    public FLRPersonal getFlrpersonaldauererledigt() {
        return this.flrpersonaldauererledigt;
    }

    public void setFlrpersonaldauererledigt(FLRPersonal flrpersonaldauererledigt) {
        this.flrpersonaldauererledigt = flrpersonaldauererledigt;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
