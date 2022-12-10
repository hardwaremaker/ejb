package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRHvmalizenz implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_max_user;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private Set hvmabenutzerset;

    /** full constructor */
    public FLRHvmalizenz(Integer i_max_user, String c_nr, Set hvmabenutzerset) {
        this.i_max_user = i_max_user;
        this.c_nr = c_nr;
        this.hvmabenutzerset = hvmabenutzerset;
    }

    /** default constructor */
    public FLRHvmalizenz() {
    }

    /** minimal constructor */
    public FLRHvmalizenz(Set hvmabenutzerset) {
        this.hvmabenutzerset = hvmabenutzerset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_max_user() {
        return this.i_max_user;
    }

    public void setI_max_user(Integer i_max_user) {
        this.i_max_user = i_max_user;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Set getHvmabenutzerset() {
        return this.hvmabenutzerset;
    }

    public void setHvmabenutzerset(Set hvmabenutzerset) {
        this.hvmabenutzerset = hvmabenutzerset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
