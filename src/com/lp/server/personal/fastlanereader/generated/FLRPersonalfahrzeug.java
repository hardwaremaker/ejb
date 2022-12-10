package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPersonalfahrzeug implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer personal_i_id;

    /** nullable persistent field */
    private Integer fahrzeug_i_id;

    /** nullable persistent field */
    private com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug;

    /** full constructor */
    public FLRPersonalfahrzeug(Integer personal_i_id, Integer fahrzeug_i_id, com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug) {
        this.personal_i_id = personal_i_id;
        this.fahrzeug_i_id = fahrzeug_i_id;
        this.flrfahrzeug = flrfahrzeug;
    }

    /** default constructor */
    public FLRPersonalfahrzeug() {
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

    public Integer getFahrzeug_i_id() {
        return this.fahrzeug_i_id;
    }

    public void setFahrzeug_i_id(Integer fahrzeug_i_id) {
        this.fahrzeug_i_id = fahrzeug_i_id;
    }

    public com.lp.server.personal.fastlanereader.generated.FLRFahrzeug getFlrfahrzeug() {
        return this.flrfahrzeug;
    }

    public void setFlrfahrzeug(com.lp.server.personal.fastlanereader.generated.FLRFahrzeug flrfahrzeug) {
        this.flrfahrzeug = flrfahrzeug;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
