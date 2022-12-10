package com.lp.server.benutzer.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRMandant;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRBenutzermandantsystemrolle implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_restapi;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_hvma;

    /** nullable persistent field */
    private FLRMandant flrmandant;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_zugeordnet;

    /** full constructor */
    public FLRBenutzermandantsystemrolle(com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer, com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle, com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_restapi, com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_hvma, FLRMandant flrmandant, FLRPersonal flrpersonal_zugeordnet) {
        this.flrbenutzer = flrbenutzer;
        this.flrsystemrolle = flrsystemrolle;
        this.flrsystemrolle_restapi = flrsystemrolle_restapi;
        this.flrsystemrolle_hvma = flrsystemrolle_hvma;
        this.flrmandant = flrmandant;
        this.flrpersonal_zugeordnet = flrpersonal_zugeordnet;
    }

    /** default constructor */
    public FLRBenutzermandantsystemrolle() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer getFlrbenutzer() {
        return this.flrbenutzer;
    }

    public void setFlrbenutzer(com.lp.server.benutzer.fastlanereader.generated.FLRBenutzer flrbenutzer) {
        this.flrbenutzer = flrbenutzer;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle getFlrsystemrolle() {
        return this.flrsystemrolle;
    }

    public void setFlrsystemrolle(com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle) {
        this.flrsystemrolle = flrsystemrolle;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle getFlrsystemrolle_restapi() {
        return this.flrsystemrolle_restapi;
    }

    public void setFlrsystemrolle_restapi(com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_restapi) {
        this.flrsystemrolle_restapi = flrsystemrolle_restapi;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle getFlrsystemrolle_hvma() {
        return this.flrsystemrolle_hvma;
    }

    public void setFlrsystemrolle_hvma(com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle_hvma) {
        this.flrsystemrolle_hvma = flrsystemrolle_hvma;
    }

    public FLRMandant getFlrmandant() {
        return this.flrmandant;
    }

    public void setFlrmandant(FLRMandant flrmandant) {
        this.flrmandant = flrmandant;
    }

    public FLRPersonal getFlrpersonal_zugeordnet() {
        return this.flrpersonal_zugeordnet;
    }

    public void setFlrpersonal_zugeordnet(FLRPersonal flrpersonal_zugeordnet) {
        this.flrpersonal_zugeordnet = flrpersonal_zugeordnet;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
