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
package com.lp.server.kueche.fastlanereader.generated;

import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRSpeiseplan implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private Date t_datum;

    /** nullable persistent field */
    private BigDecimal n_menge;

    /** nullable persistent field */
    private com.lp.server.kueche.fastlanereader.generated.FLRKassaartikel flrkassaartikel;

    /** nullable persistent field */
    private FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrfertigungsgruppe;

    /** persistent field */
    private Set speiseplanpositionset;

    /** full constructor */
    public FLRSpeiseplan(String mandant_c_nr, Date t_datum, BigDecimal n_menge, com.lp.server.kueche.fastlanereader.generated.FLRKassaartikel flrkassaartikel, FLRStueckliste flrstueckliste, FLRFertigungsgruppe flrfertigungsgruppe, Set speiseplanpositionset) {
        this.mandant_c_nr = mandant_c_nr;
        this.t_datum = t_datum;
        this.n_menge = n_menge;
        this.flrkassaartikel = flrkassaartikel;
        this.flrstueckliste = flrstueckliste;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
        this.speiseplanpositionset = speiseplanpositionset;
    }

    /** default constructor */
    public FLRSpeiseplan() {
    }

    /** minimal constructor */
    public FLRSpeiseplan(Set speiseplanpositionset) {
        this.speiseplanpositionset = speiseplanpositionset;
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getMandant_c_nr() {
        return this.mandant_c_nr;
    }

    public void setMandant_c_nr(String mandant_c_nr) {
        this.mandant_c_nr = mandant_c_nr;
    }

    public Date getT_datum() {
        return this.t_datum;
    }

    public void setT_datum(Date t_datum) {
        this.t_datum = t_datum;
    }

    public BigDecimal getN_menge() {
        return this.n_menge;
    }

    public void setN_menge(BigDecimal n_menge) {
        this.n_menge = n_menge;
    }

    public com.lp.server.kueche.fastlanereader.generated.FLRKassaartikel getFlrkassaartikel() {
        return this.flrkassaartikel;
    }

    public void setFlrkassaartikel(com.lp.server.kueche.fastlanereader.generated.FLRKassaartikel flrkassaartikel) {
        this.flrkassaartikel = flrkassaartikel;
    }

    public FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public Set getSpeiseplanpositionset() {
        return this.speiseplanpositionset;
    }

    public void setSpeiseplanpositionset(Set speiseplanpositionset) {
        this.speiseplanpositionset = speiseplanpositionset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
