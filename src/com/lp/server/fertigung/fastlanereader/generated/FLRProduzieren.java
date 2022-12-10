package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRProduzieren implements Serializable {

    /** identifier field */
    private Integer los_i_id;

    /** nullable persistent field */
    private BigDecimal n_dauer_offen_personal;

    /** nullable persistent field */
    private BigDecimal n_dauer_offen_maschine;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer lossolllarbeitsplan_i_id_ersterarbeitsgang;

    /** nullable persistent field */
    private Integer i_maschinenversatztage;

    /** nullable persistent field */
    private Date t_agbeginn;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos;

    /** nullable persistent field */
    private com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan;

    /** full constructor */
    public FLRProduzieren(BigDecimal n_dauer_offen_personal, BigDecimal n_dauer_offen_maschine, Integer kunde_i_id, Integer lossolllarbeitsplan_i_id_ersterarbeitsgang, Integer i_maschinenversatztage, Date t_agbeginn, FLRKunde flrkunde, com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos, com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.n_dauer_offen_personal = n_dauer_offen_personal;
        this.n_dauer_offen_maschine = n_dauer_offen_maschine;
        this.kunde_i_id = kunde_i_id;
        this.lossolllarbeitsplan_i_id_ersterarbeitsgang = lossolllarbeitsplan_i_id_ersterarbeitsgang;
        this.i_maschinenversatztage = i_maschinenversatztage;
        this.t_agbeginn = t_agbeginn;
        this.flrkunde = flrkunde;
        this.flrlos = flrlos;
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    /** default constructor */
    public FLRProduzieren() {
    }

    public Integer getLos_i_id() {
        return this.los_i_id;
    }

    public void setLos_i_id(Integer los_i_id) {
        this.los_i_id = los_i_id;
    }

    public BigDecimal getN_dauer_offen_personal() {
        return this.n_dauer_offen_personal;
    }

    public void setN_dauer_offen_personal(BigDecimal n_dauer_offen_personal) {
        this.n_dauer_offen_personal = n_dauer_offen_personal;
    }

    public BigDecimal getN_dauer_offen_maschine() {
        return this.n_dauer_offen_maschine;
    }

    public void setN_dauer_offen_maschine(BigDecimal n_dauer_offen_maschine) {
        this.n_dauer_offen_maschine = n_dauer_offen_maschine;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getLossolllarbeitsplan_i_id_ersterarbeitsgang() {
        return this.lossolllarbeitsplan_i_id_ersterarbeitsgang;
    }

    public void setLossolllarbeitsplan_i_id_ersterarbeitsgang(Integer lossolllarbeitsplan_i_id_ersterarbeitsgang) {
        this.lossolllarbeitsplan_i_id_ersterarbeitsgang = lossolllarbeitsplan_i_id_ersterarbeitsgang;
    }

    public Integer getI_maschinenversatztage() {
        return this.i_maschinenversatztage;
    }

    public void setI_maschinenversatztage(Integer i_maschinenversatztage) {
        this.i_maschinenversatztage = i_maschinenversatztage;
    }

    public Date getT_agbeginn() {
        return this.t_agbeginn;
    }

    public void setT_agbeginn(Date t_agbeginn) {
        this.t_agbeginn = t_agbeginn;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLos getFlrlos() {
        return this.flrlos;
    }

    public void setFlrlos(com.lp.server.fertigung.fastlanereader.generated.FLRLos flrlos) {
        this.flrlos = flrlos;
    }

    public com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan getFlrlossollarbeitsplan() {
        return this.flrlossollarbeitsplan;
    }

    public void setFlrlossollarbeitsplan(com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan flrlossollarbeitsplan) {
        this.flrlossollarbeitsplan = flrlossollarbeitsplan;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("los_i_id", getLos_i_id())
            .toString();
    }

}
