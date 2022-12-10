package com.lp.server.forecast.fastlanereader.generated;

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecastartspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.forecast.fastlanereader.generated.FLRForecastart forecastart;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRForecastartspr(String c_bez, com.lp.server.forecast.fastlanereader.generated.FLRForecastart forecastart, FLRLocale locale) {
        this.c_bez = c_bez;
        this.forecastart = forecastart;
        this.locale = locale;
    }

    /** default constructor */
    public FLRForecastartspr() {
    }

    /** minimal constructor */
    public FLRForecastartspr(com.lp.server.forecast.fastlanereader.generated.FLRForecastart forecastart, FLRLocale locale) {
        this.forecastart = forecastart;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.forecast.fastlanereader.generated.FLRForecastart getForecastart() {
        return this.forecastart;
    }

    public void setForecastart(com.lp.server.forecast.fastlanereader.generated.FLRForecastart forecastart) {
        this.forecastart = forecastart;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("forecastart", getForecastart())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRForecastartspr) ) return false;
        FLRForecastartspr castOther = (FLRForecastartspr) other;
        return new EqualsBuilder()
            .append(this.getForecastart(), castOther.getForecastart())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getForecastart())
            .append(getLocale())
            .toHashCode();
    }

}
