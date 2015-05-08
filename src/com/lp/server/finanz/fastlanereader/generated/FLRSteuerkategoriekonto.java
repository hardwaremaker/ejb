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
package com.lp.server.finanz.fastlanereader.generated;

import com.lp.server.finanz.ejb.SteuerkategoriekontoPK;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatzbez;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSteuerkategoriekonto implements Serializable {

    /** identifier field */
    private SteuerkategoriekontoPK id_comp;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidvk;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidek;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategorie flrsteuerkategorie;

    /** nullable persistent field */
    private FLRMwstsatzbez flrmwstsatzbez;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontovk;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontoek;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiideinfuhrust;

    /** full constructor */
    public FLRSteuerkategoriekonto(SteuerkategoriekontoPK id_comp, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidvk, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidek, com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategorie flrsteuerkategorie, FLRMwstsatzbez flrmwstsatzbez, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontovk, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontoek, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiideinfuhrust) {
        this.id_comp = id_comp;
        this.kontoiidvk = kontoiidvk;
        this.kontoiidek = kontoiidek;
        this.flrsteuerkategorie = flrsteuerkategorie;
        this.flrmwstsatzbez = flrmwstsatzbez;
        this.kontoiidskontovk = kontoiidskontovk;
        this.kontoiidskontoek = kontoiidskontoek;
        this.kontoiideinfuhrust = kontoiideinfuhrust;
    }

    /** default constructor */
    public FLRSteuerkategoriekonto() {
    }

    /** minimal constructor */
    public FLRSteuerkategoriekonto(SteuerkategoriekontoPK id_comp) {
        this.id_comp = id_comp;
    }

    public SteuerkategoriekontoPK getId_comp() {
        return this.id_comp;
    }

    public void setId_comp(SteuerkategoriekontoPK id_comp) {
        this.id_comp = id_comp;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getKontoiidvk() {
        return this.kontoiidvk;
    }

    public void setKontoiidvk(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidvk) {
        this.kontoiidvk = kontoiidvk;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getKontoiidek() {
        return this.kontoiidek;
    }

    public void setKontoiidek(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidek) {
        this.kontoiidek = kontoiidek;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategorie getFlrsteuerkategorie() {
        return this.flrsteuerkategorie;
    }

    public void setFlrsteuerkategorie(com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategorie flrsteuerkategorie) {
        this.flrsteuerkategorie = flrsteuerkategorie;
    }

    public FLRMwstsatzbez getFlrmwstsatzbez() {
        return this.flrmwstsatzbez;
    }

    public void setFlrmwstsatzbez(FLRMwstsatzbez flrmwstsatzbez) {
        this.flrmwstsatzbez = flrmwstsatzbez;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getKontoiidskontovk() {
        return this.kontoiidskontovk;
    }

    public void setKontoiidskontovk(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontovk) {
        this.kontoiidskontovk = kontoiidskontovk;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getKontoiidskontoek() {
        return this.kontoiidskontoek;
    }

    public void setKontoiidskontoek(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiidskontoek) {
        this.kontoiidskontoek = kontoiidskontoek;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getKontoiideinfuhrust() {
        return this.kontoiideinfuhrust;
    }

    public void setKontoiideinfuhrust(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto kontoiideinfuhrust) {
        this.kontoiideinfuhrust = kontoiideinfuhrust;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id_comp", getId_comp())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof FLRSteuerkategoriekonto) ) return false;
        FLRSteuerkategoriekonto castOther = (FLRSteuerkategoriekonto) other;
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
