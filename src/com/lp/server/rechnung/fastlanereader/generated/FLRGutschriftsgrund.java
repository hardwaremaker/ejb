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
package com.lp.server.rechnung.fastlanereader.generated;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRGutschriftsgrund implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** persistent field */
    private Set gutschriftsgrund_gutschriftsgrundspr_set;

    /** full constructor */
    public FLRGutschriftsgrund(String c_nr, Set gutschriftsgrund_gutschriftsgrundspr_set) {
        this.c_nr = c_nr;
        this.gutschriftsgrund_gutschriftsgrundspr_set = gutschriftsgrund_gutschriftsgrundspr_set;
    }

    /** default constructor */
    public FLRGutschriftsgrund() {
    }

    /** minimal constructor */
    public FLRGutschriftsgrund(Set gutschriftsgrund_gutschriftsgrundspr_set) {
        this.gutschriftsgrund_gutschriftsgrundspr_set = gutschriftsgrund_gutschriftsgrundspr_set;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public Set getGutschriftsgrund_gutschriftsgrundspr_set() {
        return this.gutschriftsgrund_gutschriftsgrundspr_set;
    }

    public void setGutschriftsgrund_gutschriftsgrundspr_set(Set branche_branchespr_set) {
        this.gutschriftsgrund_gutschriftsgrundspr_set = branche_branchespr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
