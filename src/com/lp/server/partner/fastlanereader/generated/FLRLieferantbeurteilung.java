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
package com.lp.server.partner.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLieferantbeurteilung implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer lieferant_i_id;

    /** nullable persistent field */
    private Integer i_punkte;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private Short b_gesperrt;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRLieferant flrlieferant;

    /** full constructor */
    public FLRLieferantbeurteilung(Integer lieferant_i_id, Integer i_punkte, Date t_datum, Short b_gesperrt, com.lp.server.partner.fastlanereader.generated.FLRLieferant flrlieferant) {
        this.lieferant_i_id = lieferant_i_id;
        this.i_punkte = i_punkte;
        this.t_datum = t_datum;
        this.b_gesperrt = b_gesperrt;
        this.flrlieferant = flrlieferant;
    }

    /** default constructor */
    public FLRLieferantbeurteilung() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public Integer getI_punkte() {
        return this.i_punkte;
    }

    public void setI_punkte(Integer i_punkte) {
        this.i_punkte = i_punkte;
    }

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public Short getB_gesperrt() {
        return this.b_gesperrt;
    }

    public void setB_gesperrt(Short b_gesperrt) {
        this.b_gesperrt = b_gesperrt;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRLieferant getFlrlieferant() {
        return this.flrlieferant;
    }

    public void setFlrlieferant(com.lp.server.partner.fastlanereader.generated.FLRLieferant flrlieferant) {
        this.flrlieferant = flrlieferant;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
