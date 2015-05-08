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
package com.lp.server.instandhaltung.fastlanereader.generated;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRInstandhaltung implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer kunde_i_id;

    /** nullable persistent field */
    private Integer kategorie_i_id;

    /** nullable persistent field */
    private Short b_versteckt;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRIskategorie flriskategorie;

    /** nullable persistent field */
    private FLRKunde flrkunde;

    /** persistent field */
    private Set standortset;

    /** full constructor */
    public FLRInstandhaltung(Integer kunde_i_id, Integer kategorie_i_id, Short b_versteckt, FLRIskategorie flriskategorie, FLRKunde flrkunde, Set standortset) {
        this.kunde_i_id = kunde_i_id;
        this.kategorie_i_id = kategorie_i_id;
        this.b_versteckt = b_versteckt;
        this.flriskategorie = flriskategorie;
        this.flrkunde = flrkunde;
        this.standortset = standortset;
    }

    /** default constructor */
    public FLRInstandhaltung() {
    }

    /** minimal constructor */
    public FLRInstandhaltung(Set standortset) {
        this.standortset = standortset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getKunde_i_id() {
        return this.kunde_i_id;
    }

    public void setKunde_i_id(Integer kunde_i_id) {
        this.kunde_i_id = kunde_i_id;
    }

    public Integer getKategorie_i_id() {
        return this.kategorie_i_id;
    }

    public void setKategorie_i_id(Integer kategorie_i_id) {
        this.kategorie_i_id = kategorie_i_id;
    }

    public Short getB_versteckt() {
        return this.b_versteckt;
    }

    public void setB_versteckt(Short b_versteckt) {
        this.b_versteckt = b_versteckt;
    }

    public FLRIskategorie getFlriskategorie() {
        return this.flriskategorie;
    }

    public void setFlriskategorie(FLRIskategorie flriskategorie) {
        this.flriskategorie = flriskategorie;
    }

    public FLRKunde getFlrkunde() {
        return this.flrkunde;
    }

    public void setFlrkunde(FLRKunde flrkunde) {
        this.flrkunde = flrkunde;
    }

    public Set getStandortset() {
        return this.standortset;
    }

    public void setStandortset(Set standortset) {
        this.standortset = standortset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
