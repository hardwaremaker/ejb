package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProjektgruppe implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer projekt_i_id_vater;

    /** nullable persistent field */
    private Integer projekt_i_id_kind;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_vater;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_kind;

    /** full constructor */
    public FLRProjektgruppe(Integer projekt_i_id_vater, Integer projekt_i_id_kind, com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_vater, com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_kind) {
        this.projekt_i_id_vater = projekt_i_id_vater;
        this.projekt_i_id_kind = projekt_i_id_kind;
        this.flrprojekt_vater = flrprojekt_vater;
        this.flrprojekt_kind = flrprojekt_kind;
    }

    /** default constructor */
    public FLRProjektgruppe() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getProjekt_i_id_vater() {
        return this.projekt_i_id_vater;
    }

    public void setProjekt_i_id_vater(Integer projekt_i_id_vater) {
        this.projekt_i_id_vater = projekt_i_id_vater;
    }

    public Integer getProjekt_i_id_kind() {
        return this.projekt_i_id_kind;
    }

    public void setProjekt_i_id_kind(Integer projekt_i_id_kind) {
        this.projekt_i_id_kind = projekt_i_id_kind;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekt getFlrprojekt_vater() {
        return this.flrprojekt_vater;
    }

    public void setFlrprojekt_vater(com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_vater) {
        this.flrprojekt_vater = flrprojekt_vater;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRProjekt getFlrprojekt_kind() {
        return this.flrprojekt_kind;
    }

    public void setFlrprojekt_kind(com.lp.server.projekt.fastlanereader.generated.FLRProjekt flrprojekt_kind) {
        this.flrprojekt_kind = flrprojekt_kind;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
