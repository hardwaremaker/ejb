package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRVerschleissteilwerkzeug implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer verschleissteil_i_id;

    /** nullable persistent field */
    private Integer werkzeug_i_id;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil flrverschleissteil;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRWerkzeug flrwerkzeug;

    /** full constructor */
    public FLRVerschleissteilwerkzeug(Integer verschleissteil_i_id, Integer werkzeug_i_id, com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil flrverschleissteil, com.lp.server.artikel.fastlanereader.generated.FLRWerkzeug flrwerkzeug) {
        this.verschleissteil_i_id = verschleissteil_i_id;
        this.werkzeug_i_id = werkzeug_i_id;
        this.flrverschleissteil = flrverschleissteil;
        this.flrwerkzeug = flrwerkzeug;
    }

    /** default constructor */
    public FLRVerschleissteilwerkzeug() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getVerschleissteil_i_id() {
        return this.verschleissteil_i_id;
    }

    public void setVerschleissteil_i_id(Integer verschleissteil_i_id) {
        this.verschleissteil_i_id = verschleissteil_i_id;
    }

    public Integer getWerkzeug_i_id() {
        return this.werkzeug_i_id;
    }

    public void setWerkzeug_i_id(Integer werkzeug_i_id) {
        this.werkzeug_i_id = werkzeug_i_id;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil getFlrverschleissteil() {
        return this.flrverschleissteil;
    }

    public void setFlrverschleissteil(com.lp.server.artikel.fastlanereader.generated.FLRVerschleissteil flrverschleissteil) {
        this.flrverschleissteil = flrverschleissteil;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRWerkzeug getFlrwerkzeug() {
        return this.flrwerkzeug;
    }

    public void setFlrwerkzeug(com.lp.server.artikel.fastlanereader.generated.FLRWerkzeug flrwerkzeug) {
        this.flrwerkzeug = flrwerkzeug;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
