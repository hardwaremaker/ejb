package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.projekt.fastlanereader.generated.FLRProjekt;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjekttechniker implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private FLRProjekt flrprojekt;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** full constructor */
    public FLRProjekttechniker(Integer projekt_i_id, Integer personal_i_id, FLRProjekt flrprojekt, FLRPersonal flrpersonal) {
        this.projekt_i_id = projekt_i_id;
        this.personal_i_id = personal_i_id;
        this.flrprojekt = flrprojekt;
        this.flrpersonal = flrpersonal;
    }

    /** default constructor */
    public FLRProjekttechniker() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public FLRProjekt getFlrprojekt() {
        return this.flrprojekt;
    }

    public void setFlrprojekt(FLRProjekt flrprojekt) {
        this.flrprojekt = flrprojekt;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
