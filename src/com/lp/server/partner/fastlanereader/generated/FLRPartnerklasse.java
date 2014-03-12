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
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPartnerklasse implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String c_importart;

    /** persistent field */
    private Set partnerklasse_partnerklassespr_set;

    /** full constructor */
    public FLRPartnerklasse(String c_nr, String c_importart, Set partnerklasse_partnerklassespr_set) {
        this.c_nr = c_nr;
        this.c_importart = c_importart;
        this.partnerklasse_partnerklassespr_set = partnerklasse_partnerklassespr_set;
    }

    /** default constructor */
    public FLRPartnerklasse() {
    }

    /** minimal constructor */
    public FLRPartnerklasse(Set partnerklasse_partnerklassespr_set) {
        this.partnerklasse_partnerklassespr_set = partnerklasse_partnerklassespr_set;
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

    public String getC_importart() {
        return this.c_importart;
    }

    public void setC_importart(String c_importart) {
        this.c_importart = c_importart;
    }

    public Set getPartnerklasse_partnerklassespr_set() {
        return this.partnerklasse_partnerklassespr_set;
    }

    public void setPartnerklasse_partnerklassespr_set(Set partnerklasse_partnerklassespr_set) {
        this.partnerklasse_partnerklassespr_set = partnerklasse_partnerklassespr_set;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
