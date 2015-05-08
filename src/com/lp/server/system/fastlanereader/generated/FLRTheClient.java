/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.benutzer.fastlanereader.generated.FLRSystemrolle;

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

    /** nullable persistent field */
    private FLRSystemrolle flrsystemrolle;

    /** full constructor */
    public FLRTheClient(String c_benutzername, String c_mandant, Integer i_personal, String c_uisprache, String c_konzernsprache, Date t_loggedin, Date t_loggedout, FLRSystemrolle flrsystemrolle) {
        this.c_benutzername = c_benutzername;
        this.c_mandant = c_mandant;
        this.i_personal = i_personal;
        this.c_uisprache = c_uisprache;
        this.c_konzernsprache = c_konzernsprache;
        this.t_loggedin = t_loggedin;
        this.t_loggedout = t_loggedout;
        this.flrsystemrolle = flrsystemrolle;
    }

    /** default constructor */
    public FLRTheClient() {
    }

    /** minimal constructor */
    public FLRTheClient(String c_benutzername, String c_mandant, Integer i_personal, String c_uisprache, String c_konzernsprache, Date t_loggedin, Date t_loggedout) {
        this.c_benutzername = c_benutzername;
        this.c_mandant = c_mandant;
        this.i_personal = i_personal;
        this.c_uisprache = c_uisprache;
        this.c_konzernsprache = c_konzernsprache;
        this.t_loggedin = t_loggedin;
        this.t_loggedout = t_loggedout;
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

}
