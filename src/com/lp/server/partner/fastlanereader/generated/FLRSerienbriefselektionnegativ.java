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
package com.lp.server.partner.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.service.FLRSerienbriefselektionnegativPK;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSerienbriefselektionnegativ implements Serializable {

    /** identifier field */
    private FLRSerienbriefselektionnegativPK id_comp;

    /** nullable persistent field */
    private String c_bemerkung;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRSerienbrief flrserienbrief;

    /** nullable persistent field */
    private com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion;

    /** full constructor */
    public FLRSerienbriefselektionnegativ(FLRSerienbriefselektionnegativPK id_comp, String c_bemerkung, com.lp.server.partner.fastlanereader.generated.FLRSerienbrief flrserienbrief, com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion) {
        this.id_comp = id_comp;
        this.c_bemerkung = c_bemerkung;
        this.flrserienbrief = flrserienbrief;
        this.flrselektion = flrselektion;
    }

    /** default constructor */
    public FLRSerienbriefselektionnegativ() {
    }

    /** minimal constructor */
    public FLRSerienbriefselektionnegativ(FLRSerienbriefselektionnegativPK id_comp) {
        this.id_comp = id_comp;
    }

    public FLRSerienbriefselektionnegativPK getId_comp() {
        return this.id_comp;
    }

    public void setId_comp(FLRSerienbriefselektionnegativPK id_comp) {
        this.id_comp = id_comp;
    }

    public String getC_bemerkung() {
        return this.c_bemerkung;
    }

    public void setC_bemerkung(String c_bemerkung) {
        this.c_bemerkung = c_bemerkung;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRSerienbrief getFlrserienbrief() {
        return this.flrserienbrief;
    }

    public void setFlrserienbrief(com.lp.server.partner.fastlanereader.generated.FLRSerienbrief flrserienbrief) {
        this.flrserienbrief = flrserienbrief;
    }

    public com.lp.server.partner.fastlanereader.generated.FLRSelektion getFlrselektion() {
        return this.flrselektion;
    }

    public void setFlrselektion(com.lp.server.partner.fastlanereader.generated.FLRSelektion flrselektion) {
        this.flrselektion = flrselektion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id_comp", getId_comp())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRSerienbriefselektionnegativ) ) return false;
        FLRSerienbriefselektionnegativ castOther = (FLRSerienbriefselektionnegativ) other;
        return new EqualsBuilder()
            .append(this.getId_comp(), castOther.getId_comp())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId_comp())
            .toHashCode();
    }

}
