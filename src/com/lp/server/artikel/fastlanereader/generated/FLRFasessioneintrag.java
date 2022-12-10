package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFasessioneintrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer los_i_id;

    /** full constructor */
    public FLRFasessioneintrag(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    /** default constructor */
    public FLRFasessioneintrag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
