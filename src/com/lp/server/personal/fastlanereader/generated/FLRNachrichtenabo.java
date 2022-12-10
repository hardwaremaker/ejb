package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRNachrichtenabo implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer nachrichtenart_i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer nachrichtengruppe_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRNachrichtengruppe flrnachrichtengruppe;

    /** full constructor */
    public FLRNachrichtenabo(Integer nachrichtenart_i_id, Integer personal_i_id, Integer nachrichtengruppe_i_id, com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart, com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal, com.lp.server.personal.fastlanereader.generated.FLRNachrichtengruppe flrnachrichtengruppe) {
        this.nachrichtenart_i_id = nachrichtenart_i_id;
        this.personal_i_id = personal_i_id;
        this.nachrichtengruppe_i_id = nachrichtengruppe_i_id;
        this.flrnachrichtenart = flrnachrichtenart;
        this.flrpersonal = flrpersonal;
        this.flrnachrichtengruppe = flrnachrichtengruppe;
    }

    /** default constructor */
    public FLRNachrichtenabo() {
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

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public Integer getNachrichtengruppe_i_id() {
        return this.nachrichtengruppe_i_id;
    }

    public void setNachrichtengruppe_i_id(Integer nachrichtengruppe_i_id) {
        this.nachrichtengruppe_i_id = nachrichtengruppe_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart getFlrnachrichtenart() {
        return this.flrnachrichtenart;
    }

    public void setFlrnachrichtenart(com.lp.server.personal.fastlanereader.generated.FLRNachrichtenart flrnachrichtenart) {
        this.flrnachrichtenart = flrnachrichtenart;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(com.lp.server.personal.fastlanereader.generated.FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRNachrichtengruppe getFlrnachrichtengruppe() {
        return this.flrnachrichtengruppe;
    }

    public void setFlrnachrichtengruppe(com.lp.server.personal.fastlanereader.generated.FLRNachrichtengruppe flrnachrichtengruppe) {
        this.flrnachrichtengruppe = flrnachrichtengruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
