package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRKennung;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundekennung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer kennung_i_id;

    /** nullable persistent field */
    private String c_wert;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde;

    /** nullable persistent field */
    private FLRKennung flrkennung;

    /** full constructor */
    public FLRKundekennung(Integer kunde_i_id, Integer kennung_i_id, String c_wert, com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde, FLRKennung flrkennung) {
        this.kunde_i_id = kunde_i_id;
        this.kennung_i_id = kennung_i_id;
        this.c_wert = c_wert;
        this.flrkunde = flrkunde;
        this.flrkennung = flrkennung;
    }

    /** default constructor */
    public FLRKundekennung() {
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

    public Integer getKennung_i_id() {
        return this.kennung_i_id;
    }

    public void setKennung_i_id(Integer kennung_i_id) {
        this.kennung_i_id = kennung_i_id;
    }

    public String getC_wert() {
        return this.c_wert;
    }

    public void setC_wert(String c_wert) {
        this.c_wert = c_wert;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public FLRKennung getFlrkennung() {
        return this.flrkennung;
    }

    public void setFlrkennung(FLRKennung flrkennung) {
        this.flrkennung = flrkennung;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
