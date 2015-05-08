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
 *******************************************************************************/
package com.lp.server.media.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMediaEmailMeta implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** persistent field */
    private String c_from;

    /** persistent field */
    private String c_to;

    /** persistent field */
    private String c_cc;

    /** persistent field */
    private String c_subject;

    /** persistent field */
    private Date t_emaildate;

    /** persistent field */
    private Date t_anlegen;

    /** persistent field */
    private Short b_priority;

    /** nullable persistent field */
    private String x_content;

    /** full constructor */
    public FLRMediaEmailMeta(String c_from, String c_to, String c_cc, String c_subject, Date t_emaildate, Date t_anlegen, Short b_priority, String x_content) {
        this.c_from = c_from;
        this.c_to = c_to;
        this.c_cc = c_cc;
        this.c_subject = c_subject;
        this.t_emaildate = t_emaildate;
        this.t_anlegen = t_anlegen;
        this.b_priority = b_priority;
        this.x_content = x_content;
    }

    /** default constructor */
    public FLRMediaEmailMeta() {
    }

    /** minimal constructor */
    public FLRMediaEmailMeta(String c_from, String c_to, String c_cc, String c_subject, Date t_emaildate, Date t_anlegen, Short b_priority) {
        this.c_from = c_from;
        this.c_to = c_to;
        this.c_cc = c_cc;
        this.c_subject = c_subject;
        this.t_emaildate = t_emaildate;
        this.t_anlegen = t_anlegen;
        this.b_priority = b_priority;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_from() {
        return this.c_from;
    }

    public void setC_from(String c_from) {
        this.c_from = c_from;
    }

    public String getC_to() {
        return this.c_to;
    }

    public void setC_to(String c_to) {
        this.c_to = c_to;
    }

    public String getC_cc() {
        return this.c_cc;
    }

    public void setC_cc(String c_cc) {
        this.c_cc = c_cc;
    }

    public String getC_subject() {
        return this.c_subject;
    }

    public void setC_subject(String c_subject) {
        this.c_subject = c_subject;
    }

    public Date getT_emaildate() {
        return this.t_emaildate;
    }

    public void setT_emaildate(Date t_emaildate) {
        this.t_emaildate = t_emaildate;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Short getB_priority() {
        return this.b_priority;
    }

    public void setB_priority(Short b_priority) {
        this.b_priority = b_priority;
    }

    public String getX_content() {
        return this.x_content;
    }

    public void setX_content(String x_content) {
        this.x_content = x_content;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
