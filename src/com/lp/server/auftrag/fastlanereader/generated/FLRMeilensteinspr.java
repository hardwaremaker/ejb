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

import com.lp.server.system.fastlanereader.generated.FLRLocale;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRMeilensteinspr implements Serializable {

    /** nullable persistent field */
    private String c_bez;

    /** identifier field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein meilenstein;

    /** identifier field */
    private FLRLocale locale;

    /** full constructor */
    public FLRMeilensteinspr(String c_bez, com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein meilenstein, FLRLocale locale) {
        this.c_bez = c_bez;
        this.meilenstein = meilenstein;
        this.locale = locale;
    }

    /** default constructor */
    public FLRMeilensteinspr() {
    }

    /** minimal constructor */
    public FLRMeilensteinspr(com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein meilenstein, FLRLocale locale) {
        this.meilenstein = meilenstein;
        this.locale = locale;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein getMeilenstein() {
        return this.meilenstein;
    }

    public void setMeilenstein(com.lp.server.auftrag.fastlanereader.generated.FLRMeilenstein meilenstein) {
        this.meilenstein = meilenstein;
    }

    public FLRLocale getLocale() {
        return this.locale;
    }

    public void setLocale(FLRLocale locale) {
        this.locale = locale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("meilenstein", getMeilenstein())
            .append("locale", getLocale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRMeilensteinspr) ) return false;
        FLRMeilensteinspr castOther = (FLRMeilensteinspr) other;
        return new EqualsBuilder()
            .append(this.getMeilenstein(), castOther.getMeilenstein())
            .append(this.getLocale(), castOther.getLocale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getMeilenstein())
            .append(getLocale())
            .toHashCode();
    }

}
