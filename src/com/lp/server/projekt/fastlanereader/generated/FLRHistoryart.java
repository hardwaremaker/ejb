package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHistoryart implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Integer i_rot;

    /** nullable persistent field */
    private Integer i_gruen;

    /** nullable persistent field */
    private Integer i_blau;

    /** nullable persistent field */
    private Short b_aktualisierezieltermin;

    /** nullable persistent field */
    private Short b_in_auswahlliste_anzeigen;

    /** full constructor */
    public FLRHistoryart(String c_bez, Integer i_rot, Integer i_gruen, Integer i_blau, Short b_aktualisierezieltermin, Short b_in_auswahlliste_anzeigen) {
        this.c_bez = c_bez;
        this.i_rot = i_rot;
        this.i_gruen = i_gruen;
        this.i_blau = i_blau;
        this.b_aktualisierezieltermin = b_aktualisierezieltermin;
        this.b_in_auswahlliste_anzeigen = b_in_auswahlliste_anzeigen;
    }

    /** default constructor */
    public FLRHistoryart() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Integer getI_rot() {
        return this.i_rot;
    }

    public void setI_rot(Integer i_rot) {
        this.i_rot = i_rot;
    }

    public Integer getI_gruen() {
        return this.i_gruen;
    }

    public void setI_gruen(Integer i_gruen) {
        this.i_gruen = i_gruen;
    }

    public Integer getI_blau() {
        return this.i_blau;
    }

    public void setI_blau(Integer i_blau) {
        this.i_blau = i_blau;
    }

    public Short getB_aktualisierezieltermin() {
        return this.b_aktualisierezieltermin;
    }

    public void setB_aktualisierezieltermin(Short b_aktualisierezieltermin) {
        this.b_aktualisierezieltermin = b_aktualisierezieltermin;
    }

    public Short getB_in_auswahlliste_anzeigen() {
        return this.b_in_auswahlliste_anzeigen;
    }

    public void setB_in_auswahlliste_anzeigen(Short b_in_auswahlliste_anzeigen) {
        this.b_in_auswahlliste_anzeigen = b_in_auswahlliste_anzeigen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
