package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecastauftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String x_kommentar;

    /** nullable persistent field */
    private Date t_freigabe;

    /** nullable persistent field */
    private com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse flrfclieferadresse;

    /** nullable persistent field */
    private FLRPersonal flrpersonalfreigabe;

    /** full constructor */
    public FLRForecastauftrag(Date t_anlegen, String c_bemerkung, String status_c_nr, String x_kommentar, Date t_freigabe, com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse flrfclieferadresse, FLRPersonal flrpersonalfreigabe) {
        this.t_anlegen = t_anlegen;
        this.c_bemerkung = c_bemerkung;
        this.status_c_nr = status_c_nr;
        this.x_kommentar = x_kommentar;
        this.t_freigabe = t_freigabe;
        this.flrfclieferadresse = flrfclieferadresse;
        this.flrpersonalfreigabe = flrpersonalfreigabe;
    }

    /** default constructor */
    public FLRForecastauftrag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getX_kommentar() {
        return this.x_kommentar;
    }

    public void setX_kommentar(String x_kommentar) {
        this.x_kommentar = x_kommentar;
    }

    public Date getT_freigabe() {
        return this.t_freigabe;
    }

    public void setT_freigabe(Date t_freigabe) {
        this.t_freigabe = t_freigabe;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse getFlrfclieferadresse() {
        return this.flrfclieferadresse;
    }

    public void setFlrfclieferadresse(com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse flrfclieferadresse) {
        this.flrfclieferadresse = flrfclieferadresse;
    }

    public FLRPersonal getFlrpersonalfreigabe() {
        return this.flrpersonalfreigabe;
    }

    public void setFlrpersonalfreigabe(FLRPersonal flrpersonalfreigabe) {
        this.flrpersonalfreigabe = flrpersonalfreigabe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
