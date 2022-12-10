package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichtenempfaenger implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer nachrichten_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_empfaenger;

    /** nullable persistent field */
    private Date t_empfangen;

    /** nullable persistent field */
    private Date t_gelesen;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRNachrichten flrnachrichten;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_empfaenger;

    /** full constructor */
    public FLRNachrichtenempfaenger(Integer nachrichten_i_id, Integer personal_i_id_empfaenger, Date t_empfangen, Date t_gelesen, com.lp.server.personal.fastlanereader.generated.FLRNachrichten flrnachrichten, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_empfaenger) {
        this.nachrichten_i_id = nachrichten_i_id;
        this.personal_i_id_empfaenger = personal_i_id_empfaenger;
        this.t_empfangen = t_empfangen;
        this.t_gelesen = t_gelesen;
        this.flrnachrichten = flrnachrichten;
        this.flrpersonal_empfaenger = flrpersonal_empfaenger;
    }

    /** default constructor */
    public FLRNachrichtenempfaenger() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getNachrichten_i_id() {
        return this.nachrichten_i_id;
    }

    public void setNachrichten_i_id(Integer nachrichten_i_id) {
        this.nachrichten_i_id = nachrichten_i_id;
    }

    public Integer getPersonal_i_id_empfaenger() {
        return this.personal_i_id_empfaenger;
    }

    public void setPersonal_i_id_empfaenger(Integer personal_i_id_empfaenger) {
        this.personal_i_id_empfaenger = personal_i_id_empfaenger;
    }

    public Date getT_empfangen() {
        return this.t_empfangen;
    }

    public void setT_empfangen(Date t_empfangen) {
        this.t_empfangen = t_empfangen;
    }

    public Date getT_gelesen() {
        return this.t_gelesen;
    }

    public void setT_gelesen(Date t_gelesen) {
        this.t_gelesen = t_gelesen;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRNachrichten getFlrnachrichten() {
        return this.flrnachrichten;
    }

    public void setFlrnachrichten(com.lp.server.personal.fastlanereader.generated.FLRNachrichten flrnachrichten) {
        this.flrnachrichten = flrnachrichten;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_empfaenger() {
        return this.flrpersonal_empfaenger;
    }

    public void setFlrpersonal_empfaenger(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_empfaenger) {
        this.flrpersonal_empfaenger = flrpersonal_empfaenger;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
