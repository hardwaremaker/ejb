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
package com.lp.server.partner.fastlanereader.generated.service;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSerienbriefselektionnegativPK implements Serializable {

    /** identifier field */
    private Integer serienbrief_i_id;

    /** identifier field */
    private Integer selektion_i_id;

    /** full constructor */
    public FLRSerienbriefselektionnegativPK(Integer serienbrief_i_id, Integer selektion_i_id) {
        this.serienbrief_i_id = serienbrief_i_id;
        this.selektion_i_id = selektion_i_id;
    }

    /** default constructor */
    public FLRSerienbriefselektionnegativPK() {
    }

    public Integer getSerienbrief_i_id() {
        return this.serienbrief_i_id;
    }

    public void setSerienbrief_i_id(Integer serienbrief_i_id) {
        this.serienbrief_i_id = serienbrief_i_id;
    }

    public Integer getSelektion_i_id() {
        return this.selektion_i_id;
    }

    public void setSelektion_i_id(Integer selektion_i_id) {
        this.selektion_i_id = selektion_i_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("serienbrief_i_id", getSerienbrief_i_id())
            .append("selektion_i_id", getSelektion_i_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRSerienbriefselektionnegativPK) ) return false;
        FLRSerienbriefselektionnegativPK castOther = (FLRSerienbriefselektionnegativPK) other;
        return new EqualsBuilder()
            .append(this.getSerienbrief_i_id(), castOther.getSerienbrief_i_id())
            .append(this.getSelektion_i_id(), castOther.getSelektion_i_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getSerienbrief_i_id())
            .append(getSelektion_i_id())
            .toHashCode();
    }

}
