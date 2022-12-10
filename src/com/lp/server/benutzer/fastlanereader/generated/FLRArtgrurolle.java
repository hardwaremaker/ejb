package com.lp.server.benutzer.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelgruppe;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRArtgrurolle implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private FLRArtikelgruppe flrartikelgruppe;

    /** nullable persistent field */
    private com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle;

    /** full constructor */
    public FLRArtgrurolle(FLRArtikelgruppe flrartikelgruppe, com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle) {
        this.flrartikelgruppe = flrartikelgruppe;
        this.flrsystemrolle = flrsystemrolle;
    }

    /** default constructor */
    public FLRArtgrurolle() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public FLRArtikelgruppe getFlrartikelgruppe() {
        return this.flrartikelgruppe;
    }

    public void setFlrartikelgruppe(FLRArtikelgruppe flrartikelgruppe) {
        this.flrartikelgruppe = flrartikelgruppe;
    }

    public com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle getFlrsystemrolle() {
        return this.flrsystemrolle;
    }

    public void setFlrsystemrolle(com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle flrsystemrolle) {
        this.flrsystemrolle = flrsystemrolle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
