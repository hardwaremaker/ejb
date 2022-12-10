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
package com.lp.server.artikel.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.service.FLRFehlmengeReservierungPK;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFehlmengeReservierung implements Serializable {

    /** identifier field */
    private FLRFehlmengeReservierungPK compId;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private Date t_liefertermin;

    /** nullable persistent field */
    private Date t_produktionsende;

    /** nullable persistent field */
    private com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste;

    /** nullable persistent field */
    private FLRLossollmaterial flrlossollmaterial;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** full constructor */
    public FLRFehlmengeReservierung(FLRFehlmengeReservierungPK compId, BigDecimal n_menge, Date t_liefertermin, Date t_produktionsende, com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste, FLRLossollmaterial flrlossollmaterial, FLRAuftragposition flrauftragposition) {
        this.compId = compId;
        this.n_menge = n_menge;
        this.t_liefertermin = t_liefertermin;
        this.t_produktionsende = t_produktionsende;
        this.flrartikelliste = flrartikelliste;
        this.flrlossollmaterial = flrlossollmaterial;
        this.flrauftragposition = flrauftragposition;
    }

    /** default constructor */
    public FLRFehlmengeReservierung() {
    }

    /** minimal constructor */
    public FLRFehlmengeReservierung(FLRFehlmengeReservierungPK compId) {
        this.compId = compId;
    }

    public FLRFehlmengeReservierungPK getCompId() {
        return this.compId;
    }

    public void setCompId(FLRFehlmengeReservierungPK compId) {
        this.compId = compId;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public Date getT_liefertermin() {
        return this.t_liefertermin;
    }

    public void setT_liefertermin(Date t_liefertermin) {
        this.t_liefertermin = t_liefertermin;
    }

    public Date getT_produktionsende() {
        return this.t_produktionsende;
    }

    public void setT_produktionsende(Date t_produktionsende) {
        this.t_produktionsende = t_produktionsende;
    }

    public com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste getFlrartikelliste() {
        return this.flrartikelliste;
    }

    public void setFlrartikelliste(com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste flrartikelliste) {
        this.flrartikelliste = flrartikelliste;
    }

    public FLRLossollmaterial getFlrlossollmaterial() {
        return this.flrlossollmaterial;
    }

    public void setFlrlossollmaterial(FLRLossollmaterial flrlossollmaterial) {
        this.flrlossollmaterial = flrlossollmaterial;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("compId", getCompId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRFehlmengeReservierung) ) return false;
        FLRFehlmengeReservierung castOther = (FLRFehlmengeReservierung) other;
        return new EqualsBuilder()
            .append(this.getCompId(), castOther.getCompId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCompId())
            .toHashCode();
    }

}
