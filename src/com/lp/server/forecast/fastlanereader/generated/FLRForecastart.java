package com.lp.server.forecast.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRForecastart implements Serializable {

    /** identifier field */
    private String c_nr;

    /** nullable persistent field */
    private Integer i_sort;

    /** persistent field */
    private Set forecastart_forecastart_set;

    /** full constructor */
    public FLRForecastart(Integer i_sort, Set forecastart_forecastart_set) {
        this.i_sort = i_sort;
        this.forecastart_forecastart_set = forecastart_forecastart_set;
    }

    /** default constructor */
    public FLRForecastart() {
    }

    /** minimal constructor */
    public FLRForecastart(Set forecastart_forecastart_set) {
        this.forecastart_forecastart_set = forecastart_forecastart_set;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public Set getForecastart_forecastart_set() {
        return this.forecastart_forecastart_set;
    }

    public void setForecastart_forecastart_set(Set forecastart_forecastart_set) {
        this.forecastart_forecastart_set = forecastart_forecastart_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("c_nr", getC_nr())
            .toString();
    }

}
