package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFclieferadresse implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer forecast_i_id;

    /** nullable persistent field */
    private Integer kunde_i_id_lieferadresse;

    /** nullable persistent field */
    private Short b_kommissionieren;

    /** nullable persistent field */
    private Integer i_typ_rundung_position;

    /** nullable persistent field */
    private Integer i_typ_rundung_linienabruf;

    /** nullable persistent field */
    private com.lp.server.forecast.fastlanereader.generated.FLRForecast flrforecast;

    /** nullable persistent field */
    private FLRKunde flrkunde_lieferadresse;

    /** full constructor */
    public FLRFclieferadresse(Integer forecast_i_id, Integer kunde_i_id_lieferadresse, Short b_kommissionieren, Integer i_typ_rundung_position, Integer i_typ_rundung_linienabruf, com.lp.server.forecast.fastlanereader.generated.FLRForecast flrforecast, FLRKunde flrkunde_lieferadresse) {
        this.forecast_i_id = forecast_i_id;
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
        this.b_kommissionieren = b_kommissionieren;
        this.i_typ_rundung_position = i_typ_rundung_position;
        this.i_typ_rundung_linienabruf = i_typ_rundung_linienabruf;
        this.flrforecast = flrforecast;
        this.flrkunde_lieferadresse = flrkunde_lieferadresse;
    }

    /** default constructor */
    public FLRFclieferadresse() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getForecast_i_id() {
        return this.forecast_i_id;
    }

    public void setForecast_i_id(Integer forecast_i_id) {
        this.forecast_i_id = forecast_i_id;
    }

    public Integer getKunde_i_id_lieferadresse() {
        return this.kunde_i_id_lieferadresse;
    }

    public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
        this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
    }

    public Short getB_kommissionieren() {
        return this.b_kommissionieren;
    }

    public void setB_kommissionieren(Short b_kommissionieren) {
        this.b_kommissionieren = b_kommissionieren;
    }

    public Integer getI_typ_rundung_position() {
        return this.i_typ_rundung_position;
    }

    public void setI_typ_rundung_position(Integer i_typ_rundung_position) {
        this.i_typ_rundung_position = i_typ_rundung_position;
    }

    public Integer getI_typ_rundung_linienabruf() {
        return this.i_typ_rundung_linienabruf;
    }

    public void setI_typ_rundung_linienabruf(Integer i_typ_rundung_linienabruf) {
        this.i_typ_rundung_linienabruf = i_typ_rundung_linienabruf;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRForecast getFlrforecast() {
        return this.flrforecast;
    }

    public void setFlrforecast(com.lp.server.forecast.fastlanereader.generated.FLRForecast flrforecast) {
        this.flrforecast = flrforecast;
    }

    public FLRKunde getFlrkunde_lieferadresse() {
        return this.flrkunde_lieferadresse;
    }

    public void setFlrkunde_lieferadresse(FLRKunde flrkunde_lieferadresse) {
        this.flrkunde_lieferadresse = flrkunde_lieferadresse;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
