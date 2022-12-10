package com.lp.server.eingangsrechnung.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLREingangsrechnungKontierung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer eingangsrechnung_i_id;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_betrag_ust;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRFinanzKonto flrkonto;

    /** nullable persistent field */
    private com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** full constructor */
    public FLREingangsrechnungKontierung(Integer eingangsrechnung_i_id, BigDecimal n_betrag, BigDecimal n_betrag_ust, String c_kommentar, FLRKostenstelle flrkostenstelle, FLRFinanzKonto flrkonto, com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung, FLRArtikel flrartikel) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
        this.n_betrag = n_betrag;
        this.n_betrag_ust = n_betrag_ust;
        this.c_kommentar = c_kommentar;
        this.flrkostenstelle = flrkostenstelle;
        this.flrkonto = flrkonto;
        this.flreingangsrechnung = flreingangsrechnung;
        this.flrartikel = flrartikel;
    }

    /** default constructor */
    public FLREingangsrechnungKontierung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getEingangsrechnung_i_id() {
        return this.eingangsrechnung_i_id;
    }

    public void setEingangsrechnung_i_id(Integer eingangsrechnung_i_id) {
        this.eingangsrechnung_i_id = eingangsrechnung_i_id;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_betrag_ust() {
        return this.n_betrag_ust;
    }

    public void setN_betrag_ust(BigDecimal n_betrag_ust) {
        this.n_betrag_ust = n_betrag_ust;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung getFlreingangsrechnung() {
        return this.flreingangsrechnung;
    }

    public void setFlreingangsrechnung(com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung flreingangsrechnung) {
        this.flreingangsrechnung = flreingangsrechnung;
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
