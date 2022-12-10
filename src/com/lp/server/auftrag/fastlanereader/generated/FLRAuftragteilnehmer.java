package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRFunktion;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragteilnehmer implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private int i_sort;

    /** nullable persistent field */
    private int partner_i_id_auftragteilnehmer;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRFunktion flrfunktion;

    /** nullable persistent field */
    private FLRPersonal flrpersonal;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** full constructor */
    public FLRAuftragteilnehmer(int i_sort, int partner_i_id_auftragteilnehmer, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, FLRPartner flrpartner, FLRFunktion flrfunktion, FLRPersonal flrpersonal, FLRKostenstelle flrkostenstelle) {
        this.i_sort = i_sort;
        this.partner_i_id_auftragteilnehmer = partner_i_id_auftragteilnehmer;
        this.flrauftrag = flrauftrag;
        this.flrpartner = flrpartner;
        this.flrfunktion = flrfunktion;
        this.flrpersonal = flrpersonal;
        this.flrkostenstelle = flrkostenstelle;
    }

    /** default constructor */
    public FLRAuftragteilnehmer() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public int getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(int i_sort) {
        this.i_sort = i_sort;
    }

    public int getPartner_i_id_auftragteilnehmer() {
        return this.partner_i_id_auftragteilnehmer;
    }

    public void setPartner_i_id_auftragteilnehmer(int partner_i_id_auftragteilnehmer) {
        this.partner_i_id_auftragteilnehmer = partner_i_id_auftragteilnehmer;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRFunktion getFlrfunktion() {
        return this.flrfunktion;
    }

    public void setFlrfunktion(FLRFunktion flrfunktion) {
        this.flrfunktion = flrfunktion;
    }

    public FLRPersonal getFlrpersonal() {
        return this.flrpersonal;
    }

    public void setFlrpersonal(FLRPersonal flrpersonal) {
        this.flrpersonal = flrpersonal;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
