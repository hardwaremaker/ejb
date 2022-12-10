package com.lp.server.personal.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRArbeitsplatz;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPersonalterminal implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private FLRArbeitsplatz flrarbeitsplatz;

    /** full constructor */
    public FLRPersonalterminal(Integer personal_i_id, FLRArbeitsplatz flrarbeitsplatz) {
        this.personal_i_id = personal_i_id;
        this.flrarbeitsplatz = flrarbeitsplatz;
    }

    /** default constructor */
    public FLRPersonalterminal() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getPersonal_i_id() {
        return this.personal_i_id;
    }

    public void setPersonal_i_id(Integer personal_i_id) {
        this.personal_i_id = personal_i_id;
    }

    public FLRArbeitsplatz getFlrarbeitsplatz() {
        return this.flrarbeitsplatz;
    }

    public void setFlrarbeitsplatz(FLRArbeitsplatz flrarbeitsplatz) {
        this.flrarbeitsplatz = flrarbeitsplatz;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
