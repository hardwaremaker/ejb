package com.lp.server.rechnung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRPositionenSichtAuftrag;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVorkasseposition implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer rechnung_i_id;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private Integer auftragsposition_i_id;

    /** nullable persistent field */
    private com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung;

    /** nullable persistent field */
    private FLRPositionenSichtAuftrag flrpositionensichtauftrag;

    /** full constructor */
    public FLRVorkasseposition(Integer rechnung_i_id, BigDecimal n_betrag, Integer auftragsposition_i_id, com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung, FLRPositionenSichtAuftrag flrpositionensichtauftrag) {
        this.rechnung_i_id = rechnung_i_id;
        this.n_betrag = n_betrag;
        this.auftragsposition_i_id = auftragsposition_i_id;
        this.flrrechnung = flrrechnung;
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
    }

    /** default constructor */
    public FLRVorkasseposition() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getRechnung_i_id() {
        return this.rechnung_i_id;
    }

    public void setRechnung_i_id(Integer rechnung_i_id) {
        this.rechnung_i_id = rechnung_i_id;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public Integer getAuftragsposition_i_id() {
        return this.auftragsposition_i_id;
    }

    public void setAuftragsposition_i_id(Integer auftragsposition_i_id) {
        this.auftragsposition_i_id = auftragsposition_i_id;
    }

    public com.lp.server.rechnung.fastlanereader.generated.FLRRechnung getFlrrechnung() {
        return this.flrrechnung;
    }

    public void setFlrrechnung(com.lp.server.rechnung.fastlanereader.generated.FLRRechnung flrrechnung) {
        this.flrrechnung = flrrechnung;
    }

    public FLRPositionenSichtAuftrag getFlrpositionensichtauftrag() {
        return this.flrpositionensichtauftrag;
    }

    public void setFlrpositionensichtauftrag(FLRPositionenSichtAuftrag flrpositionensichtauftrag) {
        this.flrpositionensichtauftrag = flrpositionensichtauftrag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
