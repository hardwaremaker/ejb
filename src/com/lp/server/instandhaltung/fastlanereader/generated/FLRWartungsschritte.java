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

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.personal.fastlanereader.generated.FLRPersonalgruppe;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRWartungsschritte implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer geraet_i_id;

    /** nullable persistent field */
    private Integer i_sort;

    /** nullable persistent field */
    private String auftragwiederholungsintervall_c_nr;

    /** nullable persistent field */
    private Date t_abdurchfuehren;

    /** nullable persistent field */
    private Long l_dauer;

    /** nullable persistent field */
    private com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet;

    /** nullable persistent field */
    private FLRArtikel flrartikel;

    /** nullable persistent field */
    private FLRPersonalgruppe flrpersonalgruppe;

    /** full constructor */
    public FLRWartungsschritte(Integer geraet_i_id, Integer i_sort, String auftragwiederholungsintervall_c_nr, Date t_abdurchfuehren, Long l_dauer, com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet, FLRArtikel flrartikel, FLRPersonalgruppe flrpersonalgruppe) {
        this.geraet_i_id = geraet_i_id;
        this.i_sort = i_sort;
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
        this.t_abdurchfuehren = t_abdurchfuehren;
        this.l_dauer = l_dauer;
        this.flrgeraet = flrgeraet;
        this.flrartikel = flrartikel;
        this.flrpersonalgruppe = flrpersonalgruppe;
    }

    /** default constructor */
    public FLRWartungsschritte() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getGeraet_i_id() {
        return this.geraet_i_id;
    }

    public void setGeraet_i_id(Integer geraet_i_id) {
        this.geraet_i_id = geraet_i_id;
    }

    public Integer getI_sort() {
        return this.i_sort;
    }

    public void setI_sort(Integer i_sort) {
        this.i_sort = i_sort;
    }

    public String getAuftragwiederholungsintervall_c_nr() {
        return this.auftragwiederholungsintervall_c_nr;
    }

    public void setAuftragwiederholungsintervall_c_nr(String auftragwiederholungsintervall_c_nr) {
        this.auftragwiederholungsintervall_c_nr = auftragwiederholungsintervall_c_nr;
    }

    public Date getT_abdurchfuehren() {
        return this.t_abdurchfuehren;
    }

    public void setT_abdurchfuehren(Date t_abdurchfuehren) {
        this.t_abdurchfuehren = t_abdurchfuehren;
    }

    public Long getL_dauer() {
        return this.l_dauer;
    }

    public void setL_dauer(Long l_dauer) {
        this.l_dauer = l_dauer;
    }

    public com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet getFlrgeraet() {
        return this.flrgeraet;
    }

    public void setFlrgeraet(com.lp.server.instandhaltung.fastlanereader.generated.FLRGeraet flrgeraet) {
        this.flrgeraet = flrgeraet;
    }

    public FLRArtikel getFlrartikel() {
        return this.flrartikel;
    }

    public void setFlrartikel(FLRArtikel flrartikel) {
        this.flrartikel = flrartikel;
    }

    public FLRPersonalgruppe getFlrpersonalgruppe() {
        return this.flrpersonalgruppe;
    }

    public void setFlrpersonalgruppe(FLRPersonalgruppe flrpersonalgruppe) {
        this.flrpersonalgruppe = flrpersonalgruppe;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

}
