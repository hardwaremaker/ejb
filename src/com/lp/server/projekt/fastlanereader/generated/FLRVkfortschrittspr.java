package com.lp.server.projekt.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVkfortschrittspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt vkfortschritt;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRVkfortschrittspr(String c_bez, com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt vkfortschritt, FLRLocale locale) {
        this.c_bez = c_bez;
        this.vkfortschritt = vkfortschritt;
        this.locale = locale;
    }

    /** default constructor */
    public FLRVkfortschrittspr() {
    }

    /** minimal constructor */
    public FLRVkfortschrittspr(com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt vkfortschritt, FLRLocale locale) {
        this.vkfortschritt = vkfortschritt;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt getVkfortschritt() {
        return this.vkfortschritt;
    }

    public void setVkfortschritt(com.lp.server.projekt.fastlanereader.generated.FLRVkfortschritt vkfortschritt) {
        this.vkfortschritt = vkfortschritt;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("vkfortschritt", getVkfortschritt())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRVkfortschrittspr) ) return false;
        FLRVkfortschrittspr castOther = (FLRVkfortschrittspr) other;
        return new EqualsBuilder()
            .append(this.getVkfortschritt(), castOther.getVkfortschritt())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getVkfortschritt())
            .append(getLocale())
            .toHashCode();
    }

}
