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
package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsplanmeilenstein implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer zahlungsplan_i_id;

    /** nullable persistent field */
    private Integer meilenstein_i_id;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private String x_text;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein;

    /** full constructor */
    public FLRZahlungsplanmeilenstein(Integer zahlungsplan_i_id, Integer meilenstein_i_id, String c_kommentar, String x_text, Date t_erledigt, com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein) {
        this.zahlungsplan_i_id = zahlungsplan_i_id;
        this.meilenstein_i_id = meilenstein_i_id;
        this.c_kommentar = c_kommentar;
        this.x_text = x_text;
        this.t_erledigt = t_erledigt;
        this.flrmeilenstein = flrmeilenstein;
    }

    /** default constructor */
    public FLRZahlungsplanmeilenstein() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getZahlungsplan_i_id() {
        return this.zahlungsplan_i_id;
    }

    public void setZahlungsplan_i_id(Integer zahlungsplan_i_id) {
        this.zahlungsplan_i_id = zahlungsplan_i_id;
    }

    public Integer getMeilenstein_i_id() {
        return this.meilenstein_i_id;
    }

    public void setMeilenstein_i_id(Integer meilenstein_i_id) {
        this.meilenstein_i_id = meilenstein_i_id;
    }

    public String getC_kommentar() {
        return this.c_kommentar;
    }

    public void setC_kommentar(String c_kommentar) {
        this.c_kommentar = c_kommentar;
    }

    public String getX_text() {
        return this.x_text;
    }

    public void setX_text(String x_text) {
        this.x_text = x_text;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein getFlrmeilenstein() {
        return this.flrmeilenstein;
    }

    public void setFlrmeilenstein(com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein flrmeilenstein) {
        this.flrmeilenstein = flrmeilenstein;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
