package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRSpediteur;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundespediteur implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer spediteur_i_id;

    /** nullable persistent field */
    private BigDecimal n_gewichtinkg;

    /** nullable persistent field */
    private String c_accounting;

    /** nullable persistent field */
    private FLRSpediteur flrspediteur;

    /** full constructor */
    public FLRKundespediteur(Integer kunde_i_id, Integer spediteur_i_id, BigDecimal n_gewichtinkg, String c_accounting, FLRSpediteur flrspediteur) {
        this.kunde_i_id = kunde_i_id;
        this.spediteur_i_id = spediteur_i_id;
        this.n_gewichtinkg = n_gewichtinkg;
        this.c_accounting = c_accounting;
        this.flrspediteur = flrspediteur;
    }

    /** default constructor */
    public FLRKundespediteur() {
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

    public Integer getSpediteur_i_id() {
        return this.spediteur_i_id;
    }

    public void setSpediteur_i_id(Integer spediteur_i_id) {
        this.spediteur_i_id = spediteur_i_id;
    }

    public BigDecimal getN_gewichtinkg() {
        return this.n_gewichtinkg;
    }

    public void setN_gewichtinkg(BigDecimal n_gewichtinkg) {
        this.n_gewichtinkg = n_gewichtinkg;
    }

    public String getC_accounting() {
        return this.c_accounting;
    }

    public void setC_accounting(String c_accounting) {
        this.c_accounting = c_accounting;
    }

    public FLRSpediteur getFlrspediteur() {
        return this.flrspediteur;
    }

    public void setFlrspediteur(FLRSpediteur flrspediteur) {
        this.flrspediteur = flrspediteur;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
