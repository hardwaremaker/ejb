package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSachbearbeiter implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Date t_gueltigab;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** full constructor */
    public FLRSachbearbeiter(Integer kunde_i_id, Date t_gueltigab, FLRPersonal flrpersonal, FLRKunde flrkunde) {
        this.kunde_i_id = kunde_i_id;
        this.t_gueltigab = t_gueltigab;
        this.flrpersonal = flrpersonal;
        this.flrkunde = flrkunde;
    }

    /** default constructor */
    public FLRSachbearbeiter() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Date getT_gueltigab() {
        return this.t_gueltigab;
    }

    public void setT_gueltigab(Date t_gueltigab) {
        this.t_gueltigab = t_gueltigab;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
