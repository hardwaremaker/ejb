package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRAuftragkostenstelle implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** full constructor */
    public FLRAuftragkostenstelle(Integer kostenstelle_i_id, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, FLRKostenstelle flrkostenstelle) {
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.flrauftrag = flrauftrag;
        this.flrkostenstelle = flrkostenstelle;
    }

    /** default constructor */
    public FLRAuftragkostenstelle() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
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
