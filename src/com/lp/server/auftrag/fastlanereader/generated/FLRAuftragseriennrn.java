package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragseriennrn implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer auftragposition_i_id;

    /** nullable persistent field */
    private String c_seriennr;

    /** nullable persistent field */
    private String version_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition;

    /** full constructor */
    public FLRAuftragseriennrn(Integer auftragposition_i_id, String c_seriennr, String version_nr, Integer i_sort, Integer artikel_i_id, String c_kommentar, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition) {
        this.auftragposition_i_id = auftragposition_i_id;
        this.c_seriennr = c_seriennr;
        this.version_nr = version_nr;
        this.i_sort = i_sort;
        this.artikel_i_id = artikel_i_id;
        this.c_kommentar = c_kommentar;
        this.flrauftragposition = flrauftragposition;
    }

    /** default constructor */
    public FLRAuftragseriennrn() {
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

    public String getC_seriennr() {
        return this.c_seriennr;
    }

    public void setC_seriennr(String c_seriennr) {
        this.c_seriennr = c_seriennr;
    }

    public String getVersion_nr() {
        return this.version_nr;
    }

    public void setVersion_nr(String version_nr) {
        this.version_nr = version_nr;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
