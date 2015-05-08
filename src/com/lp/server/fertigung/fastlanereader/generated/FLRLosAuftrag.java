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
package com.lp.server.fertigung.fastlanereader.generated;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragOD;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRLosAuftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String c_nr;

    /** nullable persistent field */
    private String status_c_nr;

    /** nullable persistent field */
    private String c_projekt;

    /** nullable persistent field */
    private Integer stueckliste_i_id;

    /** nullable persistent field */
    private Date t_produktionsende;

    /** nullable persistent field */
    private Date t_produktionsbeginn;

    /** nullable persistent field */
    private Date t_ausgabe;

    /** nullable persistent field */
    private Date t_erledigt;

    /** nullable persistent field */
    private Date t_manuellerledigt;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private Date t_produktionsstop;

    /** nullable persistent field */
    private BigDecimal n_losgroesse;

    /** nullable persistent field */
    private Double f_bewertung;

    /** nullable persistent field */
    private Integer kostenstelle_i_id;

    /** nullable persistent field */
    private Integer fertigungsgruppe_i_id;

    /** nullable persistent field */
    private Integer projekt_i_id;

    /** nullable persistent field */
    private FLRStueckliste flrstueckliste;

    /** nullable persistent field */
    private FLRKostenstelle flrkostenstelle;

    /** nullable persistent field */
    private FLRAuftragposition flrauftragposition;

    /** nullable persistent field */
    private FLRAuftragOD flrauftrag;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrfertigungsgruppe;

    /** nullable persistent field */
    private FLRFertigungsgruppe flrwiederholendelose;

    /** persistent field */
    private Set ablieferungset;

    /** full constructor */
    public FLRLosAuftrag(String mandant_c_nr, String c_nr, String status_c_nr, String c_projekt, Integer stueckliste_i_id, Date t_produktionsende, Date t_produktionsbeginn, Date t_ausgabe, Date t_erledigt, Date t_manuellerledigt, Date t_anlegen, Date t_produktionsstop, BigDecimal n_losgroesse, Double f_bewertung, Integer kostenstelle_i_id, Integer fertigungsgruppe_i_id, Integer projekt_i_id, FLRStueckliste flrstueckliste, FLRKostenstelle flrkostenstelle, FLRAuftragposition flrauftragposition, FLRAuftragOD flrauftrag, FLRFertigungsgruppe flrfertigungsgruppe, FLRFertigungsgruppe flrwiederholendelose, Set ablieferungset) {
        this.mandant_c_nr = mandant_c_nr;
        this.c_nr = c_nr;
        this.status_c_nr = status_c_nr;
        this.c_projekt = c_projekt;
        this.stueckliste_i_id = stueckliste_i_id;
        this.t_produktionsende = t_produktionsende;
        this.t_produktionsbeginn = t_produktionsbeginn;
        this.t_ausgabe = t_ausgabe;
        this.t_erledigt = t_erledigt;
        this.t_manuellerledigt = t_manuellerledigt;
        this.t_anlegen = t_anlegen;
        this.t_produktionsstop = t_produktionsstop;
        this.n_losgroesse = n_losgroesse;
        this.f_bewertung = f_bewertung;
        this.kostenstelle_i_id = kostenstelle_i_id;
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
        this.projekt_i_id = projekt_i_id;
        this.flrstueckliste = flrstueckliste;
        this.flrkostenstelle = flrkostenstelle;
        this.flrauftragposition = flrauftragposition;
        this.flrauftrag = flrauftrag;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
        this.flrwiederholendelose = flrwiederholendelose;
        this.ablieferungset = ablieferungset;
    }

    /** default constructor */
    public FLRLosAuftrag() {
    }

    /** minimal constructor */
    public FLRLosAuftrag(Set ablieferungset) {
        this.ablieferungset = ablieferungset;
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

    public String getC_nr() {
        return this.c_nr;
    }

    public void setC_nr(String c_nr) {
        this.c_nr = c_nr;
    }

    public String getStatus_c_nr() {
        return this.status_c_nr;
    }

    public void setStatus_c_nr(String status_c_nr) {
        this.status_c_nr = status_c_nr;
    }

    public String getC_projekt() {
        return this.c_projekt;
    }

    public void setC_projekt(String c_projekt) {
        this.c_projekt = c_projekt;
    }

    public Integer getStueckliste_i_id() {
        return this.stueckliste_i_id;
    }

    public void setStueckliste_i_id(Integer stueckliste_i_id) {
        this.stueckliste_i_id = stueckliste_i_id;
    }

    public Date getT_produktionsende() {
        return this.t_produktionsende;
    }

    public void setT_produktionsende(Date t_produktionsende) {
        this.t_produktionsende = t_produktionsende;
    }

    public Date getT_produktionsbeginn() {
        return this.t_produktionsbeginn;
    }

    public void setT_produktionsbeginn(Date t_produktionsbeginn) {
        this.t_produktionsbeginn = t_produktionsbeginn;
    }

    public Date getT_ausgabe() {
        return this.t_ausgabe;
    }

    public void setT_ausgabe(Date t_ausgabe) {
        this.t_ausgabe = t_ausgabe;
    }

    public Date getT_erledigt() {
        return this.t_erledigt;
    }

    public void setT_erledigt(Date t_erledigt) {
        this.t_erledigt = t_erledigt;
    }

    public Date getT_manuellerledigt() {
        return this.t_manuellerledigt;
    }

    public void setT_manuellerledigt(Date t_manuellerledigt) {
        this.t_manuellerledigt = t_manuellerledigt;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public Date getT_produktionsstop() {
        return this.t_produktionsstop;
    }

    public void setT_produktionsstop(Date t_produktionsstop) {
        this.t_produktionsstop = t_produktionsstop;
    }

    public BigDecimal getN_losgroesse() {
        return this.n_losgroesse;
    }

    public void setN_losgroesse(BigDecimal n_losgroesse) {
        this.n_losgroesse = n_losgroesse;
    }

    public Double getF_bewertung() {
        return this.f_bewertung;
    }

    public void setF_bewertung(Double f_bewertung) {
        this.f_bewertung = f_bewertung;
    }

    public Integer getKostenstelle_i_id() {
        return this.kostenstelle_i_id;
    }

    public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
        this.kostenstelle_i_id = kostenstelle_i_id;
    }

    public Integer getFertigungsgruppe_i_id() {
        return this.fertigungsgruppe_i_id;
    }

    public void setFertigungsgruppe_i_id(Integer fertigungsgruppe_i_id) {
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
    }

    public Integer getProjekt_i_id() {
        return this.projekt_i_id;
    }

    public void setProjekt_i_id(Integer projekt_i_id) {
        this.projekt_i_id = projekt_i_id;
    }

    public FLRStueckliste getFlrstueckliste() {
        return this.flrstueckliste;
    }

    public void setFlrstueckliste(FLRStueckliste flrstueckliste) {
        this.flrstueckliste = flrstueckliste;
    }

    public FLRKostenstelle getFlrkostenstelle() {
        return this.flrkostenstelle;
    }

    public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
        this.flrkostenstelle = flrkostenstelle;
    }

    public FLRAuftragposition getFlrauftragposition() {
        return this.flrauftragposition;
    }

    public void setFlrauftragposition(FLRAuftragposition flrauftragposition) {
        this.flrauftragposition = flrauftragposition;
    }

    public FLRAuftragOD getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftragOD flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public FLRFertigungsgruppe getFlrwiederholendelose() {
        return this.flrwiederholendelose;
    }

    public void setFlrwiederholendelose(FLRFertigungsgruppe flrwiederholendelose) {
        this.flrwiederholendelose = flrwiederholendelose;
    }

    public Set getAblieferungset() {
        return this.ablieferungset;
    }

    public void setAblieferungset(Set ablieferungset) {
        this.ablieferungset = ablieferungset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
