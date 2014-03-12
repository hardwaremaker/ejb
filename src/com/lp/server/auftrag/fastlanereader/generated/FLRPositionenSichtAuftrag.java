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
package com.lp.server.auftrag.fastlanereader.generated;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.system.fastlanereader.generated.FLRMediastandard;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRPositionenSichtAuftrag implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String c_bez;

    /** nullable persistent field */
    private String c_zbez;

    /** nullable persistent field */
    private String x_textinhalt;

    /** nullable persistent field */
    private Integer auftrag_i_id;

    /** nullable persistent field */
    private BigDecimal n_offene_menge;

    /** nullable persistent field */
    private String positionart_c_nr;

    /** nullable persistent field */
    private String auftragpositionstatus_c_nr;

    /** nullable persistent field */
    private Date t_uebersteuerterliefertermin;

    /** nullable persistent field */
    private Integer position_i_id;

    /** nullable persistent field */
    private String typ_c_nr;

    /** nullable persistent field */
    private Integer position_i_id_artikelset;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRMediastandard flrmediastandard;

    /** nullable persistent field */
    private com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag;

    /** full constructor */
    public FLRPositionenSichtAuftrag(Integer i_sort, String c_bez, String c_zbez, String x_textinhalt, Integer auftrag_i_id, BigDecimal n_offene_menge, String positionart_c_nr, String auftragpositionstatus_c_nr, Date t_uebersteuerterliefertermin, Integer position_i_id, String typ_c_nr, Integer position_i_id_artikelset, FLRArtikel flrartikel, FLRMediastandard flrmediastandard, com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.i_sort = i_sort;
        this.c_bez = c_bez;
        this.c_zbez = c_zbez;
        this.x_textinhalt = x_textinhalt;
        this.auftrag_i_id = auftrag_i_id;
        this.n_offene_menge = n_offene_menge;
        this.positionart_c_nr = positionart_c_nr;
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
        this.position_i_id = position_i_id;
        this.typ_c_nr = typ_c_nr;
        this.position_i_id_artikelset = position_i_id_artikelset;
        this.flrartikel = flrartikel;
        this.flrmediastandard = flrmediastandard;
        this.flrauftrag = flrauftrag;
    }

    /** default constructor */
    public FLRPositionenSichtAuftrag() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getC_bez() {
        return this.c_bez;
    }

    public void setC_bez(String c_bez) {
        this.c_bez = c_bez;
    }

    public String getC_zbez() {
        return this.c_zbez;
    }

    public void setC_zbez(String c_zbez) {
        this.c_zbez = c_zbez;
    }

    public String getX_textinhalt() {
        return this.x_textinhalt;
    }

    public void setX_textinhalt(String x_textinhalt) {
        this.x_textinhalt = x_textinhalt;
    }

    public Integer getAuftrag_i_id() {
        return this.auftrag_i_id;
    }

    public void setAuftrag_i_id(Integer auftrag_i_id) {
        this.auftrag_i_id = auftrag_i_id;
    }

    public BigDecimal getN_offene_menge() {
        return this.n_offene_menge;
    }

    public void setN_offene_menge(BigDecimal n_offene_menge) {
        this.n_offene_menge = n_offene_menge;
    }

    public String getPositionart_c_nr() {
        return this.positionart_c_nr;
    }

    public void setPositionart_c_nr(String positionart_c_nr) {
        this.positionart_c_nr = positionart_c_nr;
    }

    public String getAuftragpositionstatus_c_nr() {
        return this.auftragpositionstatus_c_nr;
    }

    public void setAuftragpositionstatus_c_nr(String auftragpositionstatus_c_nr) {
        this.auftragpositionstatus_c_nr = auftragpositionstatus_c_nr;
    }

    public Date getT_uebersteuerterliefertermin() {
        return this.t_uebersteuerterliefertermin;
    }

    public void setT_uebersteuerterliefertermin(Date t_uebersteuerterliefertermin) {
        this.t_uebersteuerterliefertermin = t_uebersteuerterliefertermin;
    }

    public Integer getPosition_i_id() {
        return this.position_i_id;
    }

    public void setPosition_i_id(Integer position_i_id) {
        this.position_i_id = position_i_id;
    }

    public String getTyp_c_nr() {
        return this.typ_c_nr;
    }

    public void setTyp_c_nr(String typ_c_nr) {
        this.typ_c_nr = typ_c_nr;
    }

    public Integer getPosition_i_id_artikelset() {
        return this.position_i_id_artikelset;
    }

    public void setPosition_i_id_artikelset(Integer position_i_id_artikelset) {
        this.position_i_id_artikelset = position_i_id_artikelset;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRMediastandard getFlrmediastandard() {
        return this.flrmediastandard;
    }

    public void setFlrmediastandard(FLRMediastandard flrmediastandard) {
        this.flrmediastandard = flrmediastandard;
    }

    public com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(com.lp.server.auftrag.fastlanereader.generated.FLRAuftragReport flrauftrag) {
        this.flrauftrag = flrauftrag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
