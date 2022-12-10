package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZeitverteilung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_zeit;

    /** nullable persistent field */
    private Integer los_i_id;

    /** nullable persistent field */
    private Integer lossollarbeitsplan_i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** full constructor */
    public FLRZeitverteilung(Date t_zeit, Integer los_i_id, Integer lossollarbeitsplan_i_id, Integer personal_i_id) {
        this.t_zeit = t_zeit;
        this.los_i_id = los_i_id;
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
        this.personal_i_id = personal_i_id;
    }

    /** default constructor */
    public FLRZeitverteilung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_zeit() {
        return this.t_zeit;
    }

    public void setT_zeit(Date t_zeit) {
        this.t_zeit = t_zeit;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public Integer getLossollarbeitsplan_i_id() {
        return this.lossollarbeitsplan_i_id;
    }

    public void setLossollarbeitsplan_i_id(Integer lossollarbeitsplan_i_id) {
        this.lossollarbeitsplan_i_id = lossollarbeitsplan_i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
