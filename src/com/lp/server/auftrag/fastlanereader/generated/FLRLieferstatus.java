package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferstatus implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private Integer lieferscheinposition_i_id;

    /** nullable persistent field */
    private Integer rechnungposition_i_id;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition;

    /** nullable persistent field */
    private FLRLieferscheinposition flrlieferscheinposition;

    /** nullable persistent field */
    private FLRRechnungPosition flrrechnungposition;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLRLieferstatus(Integer auftragposition_i_id, Integer lieferscheinposition_i_id, Integer rechnungposition_i_id, String status_c_nr, Integer artikel_i_id, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition, FLRLieferscheinposition flrlieferscheinposition, FLRRechnungPosition flrrechnungposition, FLRArtikel flrartikel) {
        this.auftragposition_i_id = auftragposition_i_id;
        this.lieferscheinposition_i_id = lieferscheinposition_i_id;
        this.rechnungposition_i_id = rechnungposition_i_id;
        this.status_c_nr = status_c_nr;
        this.artikel_i_id = artikel_i_id;
        this.flrauftragposition = flrauftragposition;
        this.flrlieferscheinposition = flrlieferscheinposition;
        this.flrrechnungposition = flrrechnungposition;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLRLieferstatus() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
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

    public Integer getRechnungposition_i_id() {
        return this.rechnungposition_i_id;
    }

    public void setRechnungposition_i_id(Integer rechnungposition_i_id) {
        this.rechnungposition_i_id = rechnungposition_i_id;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public FLRLieferscheinposition getFlrlieferscheinposition() {
        return this.flrlieferscheinposition;
    }

    public void setFlrlieferscheinposition(FLRLieferscheinposition flrlieferscheinposition) {
        this.flrlieferscheinposition = flrlieferscheinposition;
    }

    public FLRRechnungPosition getFlrrechnungposition() {
        return this.flrrechnungposition;
    }

    public void setFlrrechnungposition(FLRRechnungPosition flrrechnungposition) {
        this.flrrechnungposition = flrrechnungposition;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
