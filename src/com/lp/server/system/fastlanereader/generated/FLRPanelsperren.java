package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPanelsperren implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String belegart_c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_ressource_unten;

    /** nullable persistent field */
    private String c_ressource_oben;

    /** full constructor */
    public FLRPanelsperren(String belegart_c_nr, String mandant_c_nr, String c_ressource_unten, String c_ressource_oben) {
        this.belegart_c_nr = belegart_c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.c_ressource_unten = c_ressource_unten;
        this.c_ressource_oben = c_ressource_oben;
    }

    /** default constructor */
    public FLRPanelsperren() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getBelegart_c_nr() {
        return this.belegart_c_nr;
    }

    public void setBelegart_c_nr(String belegart_c_nr) {
        this.belegart_c_nr = belegart_c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public String getC_ressource_unten() {
        return this.c_ressource_unten;
    }

    public void setC_ressource_unten(String c_ressource_unten) {
        this.c_ressource_unten = c_ressource_unten;
    }

    public String getC_ressource_oben() {
        return this.c_ressource_oben;
    }

    public void setC_ressource_oben(String c_ressource_oben) {
        this.c_ressource_oben = c_ressource_oben;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
