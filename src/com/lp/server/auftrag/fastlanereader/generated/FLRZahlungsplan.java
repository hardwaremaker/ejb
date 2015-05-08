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
import java.math.BigDecimal;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRZahlungsplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_tage_vor_liefertermin;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** persistent field */
    private Set flrzahlungsplanmeilenstein;

    /** full constructor */
    public FLRZahlungsplan(Integer i_tage_vor_liefertermin, BigDecimal n_betrag, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag, Set flrzahlungsplanmeilenstein) {
        this.i_tage_vor_liefertermin = i_tage_vor_liefertermin;
        this.n_betrag = n_betrag;
        this.flrauftrag = flrauftrag;
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    /** default constructor */
    public FLRZahlungsplan() {
    }

    /** minimal constructor */
    public FLRZahlungsplan(Set flrzahlungsplanmeilenstein) {
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_tage_vor_liefertermin() {
        return this.i_tage_vor_liefertermin;
    }

    public void setI_tage_vor_liefertermin(Integer i_tage_vor_liefertermin) {
        this.i_tage_vor_liefertermin = i_tage_vor_liefertermin;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public Set getFlrzahlungsplanmeilenstein() {
        return this.flrzahlungsplanmeilenstein;
    }

    public void setFlrzahlungsplanmeilenstein(Set flrzahlungsplanmeilenstein) {
        this.flrzahlungsplanmeilenstein = flrzahlungsplanmeilenstein;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
