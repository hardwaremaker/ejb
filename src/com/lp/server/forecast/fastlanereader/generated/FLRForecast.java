package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecast implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private Integer i_tage_cod;

    /** nullable persistent field */
    private Integer i_wochen_cow;

    /** nullable persistent field */
    private Integer i_monate_fca;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** full constructor */
    public FLRForecast(String c_nr, String c_projekt, String status_c_nr, Integer i_tage_cod, Integer i_wochen_cow, Integer i_monate_fca, FLRKunde flrkunde) {
        this.c_nr = c_nr;
        this.c_projekt = c_projekt;
        this.status_c_nr = status_c_nr;
        this.i_tage_cod = i_tage_cod;
        this.i_wochen_cow = i_wochen_cow;
        this.i_monate_fca = i_monate_fca;
        this.flrkunde = flrkunde;
    }

    /** default constructor */
    public FLRForecast() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getC_projekt() {
        return this.c_projekt;
    }

    public void setC_projekt(String c_projekt) {
        this.c_projekt = c_projekt;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public Integer getI_tage_cod() {
        return this.i_tage_cod;
    }

    public void setI_tage_cod(Integer i_tage_cod) {
        this.i_tage_cod = i_tage_cod;
    }

    public Integer getI_wochen_cow() {
        return this.i_wochen_cow;
    }

    public void setI_wochen_cow(Integer i_wochen_cow) {
        this.i_wochen_cow = i_wochen_cow;
    }

    public Integer getI_monate_fca() {
        return this.i_monate_fca;
    }

    public void setI_monate_fca(Integer i_monate_fca) {
        this.i_monate_fca = i_monate_fca;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
