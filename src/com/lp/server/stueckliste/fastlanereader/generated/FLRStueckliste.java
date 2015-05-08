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
package com.lp.server.stueckliste.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRStueckliste implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private String mandant_c_nr;

    /** nullable persistent field */
    private String stuecklisteart_c_nr;

    /** nullable persistent field */
    private Date t_aendernposition;

    /** nullable persistent field */
    private Date t_freigabe;

    /** nullable persistent field */
    private Date t_aendernarbeitsplan;

    /** nullable persistent field */
    private Integer fertigungsgruppe_i_id;

    /** nullable persistent field */
    private Integer i_erfassungsfaktor;

    /** nullable persistent field */
    private Integer artikel_i_id;

    /** nullable persistent field */
    private Short b_fremdfertigung;

    /** nullable persistent field */
    private Short b_materialbuchungbeiablieferung;

    /** nullable persistent field */
    private Short b_ausgabeunterstueckliste;

    /** nullable persistent field */
    private BigDecimal n_losgroesse;

    /** nullable persistent field */
    private BigDecimal n_defaultdurchlaufzeit;

    /** nullable persistent field */
    private Integer partner_i_id;

    /** nullable persistent field */
    private FLRArtikelliste flrartikel;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe flrfertigungsgruppe;

    /** nullable persistent field */
    private com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistetextsuche flrstuecklistetextsuche;

    /** nullable persistent field */
    private FLRPartner flrpartner;

    /** nullable persistent field */
    private FLRPersonal flrpersonal_freigabe;

    /** full constructor */
    public FLRStueckliste(String mandant_c_nr, String stuecklisteart_c_nr, Date t_aendernposition, Date t_freigabe, Date t_aendernarbeitsplan, Integer fertigungsgruppe_i_id, Integer i_erfassungsfaktor, Integer artikel_i_id, Short b_fremdfertigung, Short b_materialbuchungbeiablieferung, Short b_ausgabeunterstueckliste, BigDecimal n_losgroesse, BigDecimal n_defaultdurchlaufzeit, Integer partner_i_id, FLRArtikelliste flrartikel, com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe flrfertigungsgruppe, com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistetextsuche flrstuecklistetextsuche, FLRPartner flrpartner, FLRPersonal flrpersonal_freigabe) {
        this.mandant_c_nr = mandant_c_nr;
        this.stuecklisteart_c_nr = stuecklisteart_c_nr;
        this.t_aendernposition = t_aendernposition;
        this.t_freigabe = t_freigabe;
        this.t_aendernarbeitsplan = t_aendernarbeitsplan;
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
        this.i_erfassungsfaktor = i_erfassungsfaktor;
        this.artikel_i_id = artikel_i_id;
        this.b_fremdfertigung = b_fremdfertigung;
        this.b_materialbuchungbeiablieferung = b_materialbuchungbeiablieferung;
        this.b_ausgabeunterstueckliste = b_ausgabeunterstueckliste;
        this.n_losgroesse = n_losgroesse;
        this.n_defaultdurchlaufzeit = n_defaultdurchlaufzeit;
        this.partner_i_id = partner_i_id;
        this.flrartikel = flrartikel;
        this.flrfertigungsgruppe = flrfertigungsgruppe;
        this.flrstuecklistetextsuche = flrstuecklistetextsuche;
        this.flrpartner = flrpartner;
        this.flrpersonal_freigabe = flrpersonal_freigabe;
    }

    /** default constructor */
    public FLRStueckliste() {
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

    public String getStuecklisteart_c_nr() {
        return this.stuecklisteart_c_nr;
    }

    public void setStuecklisteart_c_nr(String stuecklisteart_c_nr) {
        this.stuecklisteart_c_nr = stuecklisteart_c_nr;
    }

    public Date getT_aendernposition() {
        return this.t_aendernposition;
    }

    public void setT_aendernposition(Date t_aendernposition) {
        this.t_aendernposition = t_aendernposition;
    }

    public Date getT_freigabe() {
        return this.t_freigabe;
    }

    public void setT_freigabe(Date t_freigabe) {
        this.t_freigabe = t_freigabe;
    }

    public Date getT_aendernarbeitsplan() {
        return this.t_aendernarbeitsplan;
    }

    public void setT_aendernarbeitsplan(Date t_aendernarbeitsplan) {
        this.t_aendernarbeitsplan = t_aendernarbeitsplan;
    }

    public Integer getFertigungsgruppe_i_id() {
        return this.fertigungsgruppe_i_id;
    }

    public void setFertigungsgruppe_i_id(Integer fertigungsgruppe_i_id) {
        this.fertigungsgruppe_i_id = fertigungsgruppe_i_id;
    }

    public Integer getI_erfassungsfaktor() {
        return this.i_erfassungsfaktor;
    }

    public void setI_erfassungsfaktor(Integer i_erfassungsfaktor) {
        this.i_erfassungsfaktor = i_erfassungsfaktor;
    }

    public Integer getArtikel_i_id() {
        return this.artikel_i_id;
    }

    public void setArtikel_i_id(Integer artikel_i_id) {
        this.artikel_i_id = artikel_i_id;
    }

    public Short getB_fremdfertigung() {
        return this.b_fremdfertigung;
    }

    public void setB_fremdfertigung(Short b_fremdfertigung) {
        this.b_fremdfertigung = b_fremdfertigung;
    }

    public Short getB_materialbuchungbeiablieferung() {
        return this.b_materialbuchungbeiablieferung;
    }

    public void setB_materialbuchungbeiablieferung(Short b_materialbuchungbeiablieferung) {
        this.b_materialbuchungbeiablieferung = b_materialbuchungbeiablieferung;
    }

    public Short getB_ausgabeunterstueckliste() {
        return this.b_ausgabeunterstueckliste;
    }

    public void setB_ausgabeunterstueckliste(Short b_ausgabeunterstueckliste) {
        this.b_ausgabeunterstueckliste = b_ausgabeunterstueckliste;
    }

    public BigDecimal getN_losgroesse() {
        return this.n_losgroesse;
    }

    public void setN_losgroesse(BigDecimal n_losgroesse) {
        this.n_losgroesse = n_losgroesse;
    }

    public BigDecimal getN_defaultdurchlaufzeit() {
        return this.n_defaultdurchlaufzeit;
    }

    public void setN_defaultdurchlaufzeit(BigDecimal n_defaultdurchlaufzeit) {
        this.n_defaultdurchlaufzeit = n_defaultdurchlaufzeit;
    }

    public Integer getPartner_i_id() {
        return this.partner_i_id;
    }

    public void setPartner_i_id(Integer partner_i_id) {
        this.partner_i_id = partner_i_id;
    }

    public FLRArtikelliste getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikelliste flrartikel) {
        this.flrartikel = flrartikel;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe getFlrfertigungsgruppe() {
        return this.flrfertigungsgruppe;
    }

    public void setFlrfertigungsgruppe(com.lp.server.stueckliste.fastlanereader.generated.FLRFertigungsgruppe flrfertigungsgruppe) {
        this.flrfertigungsgruppe = flrfertigungsgruppe;
    }

    public com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistetextsuche getFlrstuecklistetextsuche() {
        return this.flrstuecklistetextsuche;
    }

    public void setFlrstuecklistetextsuche(com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistetextsuche flrstuecklistetextsuche) {
        this.flrstuecklistetextsuche = flrstuecklistetextsuche;
    }

    public FLRPartner getFlrpartner() {
        return this.flrpartner;
    }

    public void setFlrpartner(FLRPartner flrpartner) {
        this.flrpartner = flrpartner;
    }

    public FLRPersonal getFlrpersonal_freigabe() {
        return this.flrpersonal_freigabe;
    }

    public void setFlrpersonal_freigabe(FLRPersonal flrpersonal_freigabe) {
        this.flrpersonal_freigabe = flrpersonal_freigabe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
