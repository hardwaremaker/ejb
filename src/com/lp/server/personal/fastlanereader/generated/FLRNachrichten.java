package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichten implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer nachrichtenart_i_id;

    /** nullable persistent field */
    private Integer personal_i_id_absender;

    /** nullable persistent field */
    private Integer personal_i_id_erledigt;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private String c_betreff;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_absender;

    /** full constructor */
    public FLRNachrichten(Integer nachrichtenart_i_id, Integer personal_i_id_absender, Integer personal_i_id_erledigt, Date t_anlegen, Date t_erledigt, String c_betreff, String x_text, com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_absender) {
        this.nachrichtenart_i_id = nachrichtenart_i_id;
        this.personal_i_id_absender = personal_i_id_absender;
        this.personal_i_id_erledigt = personal_i_id_erledigt;
        this.t_anlegen = t_anlegen;
        this.t_erledigt = t_erledigt;
        this.c_betreff = c_betreff;
        this.x_text = x_text;
        this.flrnachrichtenart = flrnachrichtenart;
        this.flrpersonal_absender = flrpersonal_absender;
    }

    /** default constructor */
    public FLRNachrichten() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getNachrichtenart_i_id() {
        return this.nachrichtenart_i_id;
    }

    public void setNachrichtenart_i_id(Integer nachrichtenart_i_id) {
        this.nachrichtenart_i_id = nachrichtenart_i_id;
    }

    public Integer getPersonal_i_id_absender() {
        return this.personal_i_id_absender;
    }

    public void setPersonal_i_id_absender(Integer personal_i_id_absender) {
        this.personal_i_id_absender = personal_i_id_absender;
    }

    public Integer getPersonal_i_id_erledigt() {
        return this.personal_i_id_erledigt;
    }

    public void setPersonal_i_id_erledigt(Integer personal_i_id_erledigt) {
        this.personal_i_id_erledigt = personal_i_id_erledigt;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public String getC_betreff() {
        return this.c_betreff;
    }

    public void setC_betreff(String c_betreff) {
        this.c_betreff = c_betreff;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart getFlrnachrichtenart() {
        return this.flrnachrichtenart;
    }

    public void setFlrnachrichtenart(com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart) {
        this.flrnachrichtenart = flrnachrichtenart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal_absender() {
        return this.flrpersonal_absender;
    }

    public void setFlrpersonal_absender(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal_absender) {
        this.flrpersonal_absender = flrpersonal_absender;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
