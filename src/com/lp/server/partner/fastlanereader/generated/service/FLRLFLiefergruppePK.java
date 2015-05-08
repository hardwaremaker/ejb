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
package com.lp.server.partner.fastlanereader.generated.service;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLFLiefergruppePK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer lieferant_i_id;

    /** identifier field */
    private Integer lfliefergruppe_i_id;

    /** full constructor */
    public FLRLFLiefergruppePK(Integer lieferant_i_id, Integer lfliefergruppe_i_id) {
        this.lieferant_i_id = lieferant_i_id;
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
    }

    /** default constructor */
    public FLRLFLiefergruppePK() {
    }

    public Integer getLieferant_i_id() {
        return this.lieferant_i_id;
    }

    public void setLieferant_i_id(Integer lieferant_i_id) {
        this.lieferant_i_id = lieferant_i_id;
    }

    public Integer getLfliefergruppe_i_id() {
        return this.lfliefergruppe_i_id;
    }

    public void setLfliefergruppe_i_id(Integer lfliefergruppe_i_id) {
        this.lfliefergruppe_i_id = lfliefergruppe_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("lieferant_i_id", getLieferant_i_id())
            .append("lfliefergruppe_i_id", getLfliefergruppe_i_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRLFLiefergruppePK) ) return false;
        FLRLFLiefergruppePK castOther = (FLRLFLiefergruppePK) other;
        return new EqualsBuilder()
            .append(this.getLieferant_i_id(), castOther.getLieferant_i_id())
            .append(this.getLfliefergruppe_i_id(), castOther.getLfliefergruppe_i_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLieferant_i_id())
            .append(getLfliefergruppe_i_id())
            .toHashCode();
    }

}
