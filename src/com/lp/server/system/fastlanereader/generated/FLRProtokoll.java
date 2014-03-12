/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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


/** @author Hibernate CodeGenerator */
public class FLRProtokoll implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_art;

    /** nullable persistent field */
    private String c_typ;

    /** nullable persistent field */
    private String c_text;

    /** nullable persistent field */
    private String c_langtext;

    /** nullable persistent field */
    private Date t_quelle;

    /** nullable persistent field */
    private Date t_anlegen;

    /** full constructor */
    public FLRProtokoll(String c_art, String c_typ, String c_text, String c_langtext, Date t_quelle, Date t_anlegen) {
        this.c_art = c_art;
        this.c_typ = c_typ;
        this.c_text = c_text;
        this.c_langtext = c_langtext;
        this.t_quelle = t_quelle;
        this.t_anlegen = t_anlegen;
    }

    /** default constructor */
    public FLRProtokoll() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_art() {
        return this.c_art;
    }

    public void setC_art(String c_art) {
        this.c_art = c_art;
    }

    public String getC_typ() {
        return this.c_typ;
    }

    public void setC_typ(String c_typ) {
        this.c_typ = c_typ;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public String getC_langtext() {
        return this.c_langtext;
    }

    public void setC_langtext(String c_langtext) {
        this.c_langtext = c_langtext;
    }

    public Date getT_quelle() {
        return this.t_quelle;
    }

    public void setT_quelle(Date t_quelle) {
        this.t_quelle = t_quelle;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
