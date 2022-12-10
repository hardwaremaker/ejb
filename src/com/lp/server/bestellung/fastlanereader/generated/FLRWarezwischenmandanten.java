package com.lp.server.bestellung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWarezwischenmandanten implements Serializable {

    /** identifier field */
    private String c_nr;

    /** nullable persistent field */
    private Integer bestellposition_i_id;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private Integer lieferscheinposition_i_id;

    /** nullable persistent field */
    private com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste flrbestellposition;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** nullable persistent field */
    private FLRLieferscheinposition flrlieferscheinposition;

    /** full constructor */
    public FLRWarezwischenmandanten(Integer bestellposition_i_id, Integer auftragposition_i_id, Integer lieferscheinposition_i_id, com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste flrbestellposition, FLRAuftragposition flrauftragposition, FLRLieferscheinposition flrlieferscheinposition) {
        this.bestellposition_i_id = bestellposition_i_id;
        this.auftragposition_i_id = auftragposition_i_id;
        this.lieferscheinposition_i_id = lieferscheinposition_i_id;
        this.flrbestellposition = flrbestellposition;
        this.flrauftragposition = flrauftragposition;
        this.flrlieferscheinposition = flrlieferscheinposition;
    }

    /** default constructor */
    public FLRWarezwischenmandanten() {
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Integer getBestellposition_i_id() {
        return this.bestellposition_i_id;
    }

    public void setBestellposition_i_id(Integer bestellposition_i_id) {
        this.bestellposition_i_id = bestellposition_i_id;
    }

    public Integer getAuftragposition_i_id() {
        return this.auftragposition_i_id;
    }

    public void setAuftragposition_i_id(Integer auftragposition_i_id) {
        this.auftragposition_i_id = auftragposition_i_id;
    }

    public Integer getLieferscheinposition_i_id() {
        return this.lieferscheinposition_i_id;
    }

    public void setLieferscheinposition_i_id(Integer lieferscheinposition_i_id) {
        this.lieferscheinposition_i_id = lieferscheinposition_i_id;
    }

    public com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste getFlrbestellposition() {
        return this.flrbestellposition;
    }

    public void setFlrbestellposition(com.lp.server.bestellung.fastlanereader.generated.FLRBestellpositionMitArtikelliste flrbestellposition) {
        this.flrbestellposition = flrbestellposition;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public FLRLieferscheinposition getFlrlieferscheinposition() {
        return this.flrlieferscheinposition;
    }

    public void setFlrlieferscheinposition(FLRLieferscheinposition flrlieferscheinposition) {
        this.flrlieferscheinposition = flrlieferscheinposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .toString();
    }

}
