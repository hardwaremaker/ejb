package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMaschinengruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_kbez;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Short b_auslastungsanzeige;

    /** nullable persistent field */
    private Integer fertigungsgruppe_i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrfertigungsgruppe;

    /** full constructor */
    public FLRMaschinengruppe(String c_kbez, String c_bez, Integer i_sort, Short b_auslastungsanzeige, Integer fertigungsgruppe_i_id, String mandant_c_nr, FLRFertigungsgruppe flrfertigungsgruppe) {
        this.c_kbez = c_kbez;
        this.c_bez = c_bez;
        this.i_sort = i_sort;
        this.b_auslastungsanzeige = b_auslastungsanzeige;
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
        this.mandant_c_nr = mandant_c_nr;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    /** default constructor */
    public FLRMaschinengruppe() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_kbez() {
        return this.c_kbez;
    }

    public void setC_kbez(String c_kbez) {
        this.c_kbez = c_kbez;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Short getB_auslastungsanzeige() {
        return this.b_auslastungsanzeige;
    }

    public void setB_auslastungsanzeige(Short b_auslastungsanzeige) {
        this.b_auslastungsanzeige = b_auslastungsanzeige;
    }

    public Integer getFertigungsgruppe_i_id() {
        return this.fertigungsgruppe_i_id;
    }

    public void setFertigungsgruppe_i_id(Integer fertigungsgruppe_i_id) {
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
