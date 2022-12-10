package com.lp.server.projekt.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVkfortschritt implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private com.lp.server.projekt.fastlanereader.generated.FLRLeadstatus flrleadstatus;

    /** persistent field */
    private Set vkfortschrittspr_set;

    /** full constructor */
    public FLRVkfortschritt(String c_nr, String mandant_c_nr, Integer i_sort, com.lp.server.projekt.fastlanereader.generated.FLRLeadstatus flrleadstatus, Set vkfortschrittspr_set) {
        this.c_nr = c_nr;
        this.mandant_c_nr = mandant_c_nr;
        this.i_sort = i_sort;
        this.flrleadstatus = flrleadstatus;
        this.vkfortschrittspr_set = vkfortschrittspr_set;
    }

    /** default constructor */
    public FLRVkfortschritt() {
    }

    /** minimal constructor */
    public FLRVkfortschritt(Set vkfortschrittspr_set) {
        this.vkfortschrittspr_set = vkfortschrittspr_set;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRLeadstatus getFlrleadstatus() {
        return this.flrleadstatus;
    }

    public void setFlrleadstatus(com.lp.server.projekt.fastlanereader.generated.FLRLeadstatus flrleadstatus) {
        this.flrleadstatus = flrleadstatus;
    }

    public Set getVkfortschrittspr_set() {
        return this.vkfortschrittspr_set;
    }

    public void setVkfortschrittspr_set(Set vkfortschrittspr_set) {
        this.vkfortschrittspr_set = vkfortschrittspr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
