package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRKundeStruktur implements Serializable {

    /** identifier field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private String typ;

    /** nullable persistent field */
    private String loop;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_gruppe;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_rechnungsadresse;

    /** full constructor */
    public FLRKundeStruktur(String typ, String loop, com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_gruppe, com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_rechnungsadresse) {
        this.typ = typ;
        this.loop = loop;
        this.flrkunde = flrkunde;
        this.flrpartner = flrpartner;
        this.flrpartner_gruppe = flrpartner_gruppe;
        this.flrpartner_rechnungsadresse = flrpartner_rechnungsadresse;
    }

    /** default constructor */
    public FLRKundeStruktur() {
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public String getTyp() {
        return this.typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getLoop() {
        return this.loop;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(com.lp.server.partner.fastlanereader.generated.FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner_gruppe() {
        return this.flrpartner_gruppe;
    }

    public void setFlrpartner_gruppe(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_gruppe) {
        this.flrpartner_gruppe = flrpartner_gruppe;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRPartner getFlrpartner_rechnungsadresse() {
        return this.flrpartner_rechnungsadresse;
    }

    public void setFlrpartner_rechnungsadresse(com.lp.server.partner.fastlanereader.generated.FLRPartner flrpartner_rechnungsadresse) {
        this.flrpartner_rechnungsadresse = flrpartner_rechnungsadresse;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("kunde_i_id", getKunde_i_id())
            .toString();
    }

}
