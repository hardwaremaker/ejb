package com.lp.server.system.fastlanereader.generated;

import com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRTheClient implements Serializable {

    /** identifier field */
    private String cnr;

    /** persistent field */
    private String c_benutzername;

    /** persistent field */
    private String c_mandant;

    /** persistent field */
    private Integer i_personal;

    /** persistent field */
    private String c_uisprache;

    /** persistent field */
    private String c_konzernsprache;

    /** persistent field */
    private Date t_loggedin;

    /** persistent field */
    private Date t_loggedout;

    /** persistent field */
    private Integer i_rmiport;

    /** nullable persistent field */
    private FLRSystemrolle flrsystemrolle;

    private Integer hvmalizenz_IId;
    
    /** full constructor */
    public FLRTheClient(String c_benutzername, String c_mandant, Integer i_personal, String c_uisprache, String c_konzernsprache, Date t_loggedin, Date t_loggedout, Integer i_rmiport, Integer hvmalizenz_i_id, FLRSystemrolle flrsystemrolle) {
        this.c_benutzername = c_benutzername;
        this.c_mandant = c_mandant;
        this.i_personal = i_personal;
        this.c_uisprache = c_uisprache;
        this.c_konzernsprache = c_konzernsprache;
        this.t_loggedin = t_loggedin;
        this.t_loggedout = t_loggedout;
        this.i_rmiport = i_rmiport;
        this.flrsystemrolle = flrsystemrolle;
        this.hvmalizenz_IId = hvmalizenz_i_id;
    }

    /** default constructor */
    public FLRTheClient() {
    }

    /** minimal constructor */
    public FLRTheClient(String c_benutzername, String c_mandant, Integer i_personal, String c_uisprache, String c_konzernsprache, Date t_loggedin, Date t_loggedout, Integer i_rmiport) {
        this.c_benutzername = c_benutzername;
        this.c_mandant = c_mandant;
        this.i_personal = i_personal;
        this.c_uisprache = c_uisprache;
        this.c_konzernsprache = c_konzernsprache;
        this.t_loggedin = t_loggedin;
        this.t_loggedout = t_loggedout;
        this.i_rmiport = i_rmiport;
    }

    public String getCnr() {
        return this.cnr;
    }

    public void setCnr(String cnr) {
        this.cnr = cnr;
    }

    public String getC_benutzername() {
        return this.c_benutzername;
    }

    public void setC_benutzername(String c_benutzername) {
        this.c_benutzername = c_benutzername;
    }

    public String getC_mandant() {
        return this.c_mandant;
    }

    public void setC_mandant(String c_mandant) {
        this.c_mandant = c_mandant;
    }

    public Integer getI_personal() {
        return this.i_personal;
    }

    public void setI_personal(Integer i_personal) {
        this.i_personal = i_personal;
    }

    public String getC_uisprache() {
        return this.c_uisprache;
    }

    public void setC_uisprache(String c_uisprache) {
        this.c_uisprache = c_uisprache;
    }

    public String getC_konzernsprache() {
        return this.c_konzernsprache;
    }

    public void setC_konzernsprache(String c_konzernsprache) {
        this.c_konzernsprache = c_konzernsprache;
    }

    public Date getT_loggedin() {
        return this.t_loggedin;
    }

    public void setT_loggedin(Date t_loggedin) {
        this.t_loggedin = t_loggedin;
    }

    public Date getT_loggedout() {
        return this.t_loggedout;
    }

    public void setT_loggedout(Date t_loggedout) {
        this.t_loggedout = t_loggedout;
    }

    public Integer getI_rmiport() {
        return this.i_rmiport;
    }

    public void setI_rmiport(Integer i_rmiport) {
        this.i_rmiport = i_rmiport;
    }

    public FLRSystemrolle getFlrsystemrolle() {
        return this.flrsystemrolle;
    }

    public void setFlrsystemrolle(FLRSystemrolle flrsystemrolle) {
        this.flrsystemrolle = flrsystemrolle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cnr", getCnr())
            .toString();
    }

	public Integer getHvmalizenz_IId() {
		return hvmalizenz_IId;
	}

	public void setHvmalizenz_IId(Integer hvmalizenz_i_id) {
		this.hvmalizenz_IId = hvmalizenz_i_id;
	}
}
