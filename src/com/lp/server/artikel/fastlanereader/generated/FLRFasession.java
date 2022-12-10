package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFasession implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Date t_beginn;

    /** nullable persistent field */
    private Date t_gedruckt;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** full constructor */
    public FLRFasession(Date t_beginn, Date t_gedruckt, FLRPersonal flrpersonal) {
        this.t_beginn = t_beginn;
        this.t_gedruckt = t_gedruckt;
        this.flrpersonal = flrpersonal;
    }

    /** default constructor */
    public FLRFasession() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Date getT_beginn() {
        return this.t_beginn;
    }

    public void setT_beginn(Date t_beginn) {
        this.t_beginn = t_beginn;
    }

    public Date getT_gedruckt() {
        return this.t_gedruckt;
    }

    public void setT_gedruckt(Date t_gedruckt) {
        this.t_gedruckt = t_gedruckt;
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
