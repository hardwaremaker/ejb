package com.lp.server.forecast.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRImportdef implements Serializable {

    /** identifier field */
    private String c_nr;

    /** persistent field */
    private Set importdef_importdefspr_set;

    /** full constructor */
    public FLRImportdef(Set importdef_importdefspr_set) {
        this.importdef_importdefspr_set = importdef_importdefspr_set;
    }

    /** default constructor */
    public FLRImportdef() {
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Set getImportdef_importdefspr_set() {
        return this.importdef_importdefspr_set;
    }

    public void setImportdef_importdefspr_set(Set importdef_importdefspr_set) {
        this.importdef_importdefspr_set = importdef_importdefspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .toString();
    }

}
