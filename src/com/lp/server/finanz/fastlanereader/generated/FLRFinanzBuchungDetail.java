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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class FLRFinanzBuchungDetail implements Serializable {

    /** identifier field */
    private Integer i_id;

    /** nullable persistent field */
    private Integer buchung_i_id;

    /** nullable persistent field */
    private Integer konto_i_id;

    /** nullable persistent field */
    private BigDecimal n_betrag;

    /** nullable persistent field */
    private BigDecimal n_ust;

    /** nullable persistent field */
    private Integer i_auszug;

    /** nullable persistent field */
    private Date t_anlegen;

    /** nullable persistent field */
    private String buchungdetailart_c_nr;

    /** nullable persistent field */
    private Integer i_ausziffern;

    /** nullable persistent field */
    private String c_kommentar;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung flrbuchung;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto;

    /** nullable persistent field */
    private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrgegenkonto;

    private Integer konto_i_id_gegenkonto ;
    
    /** full constructor */
    public FLRFinanzBuchungDetail(Integer buchung_i_id, Integer konto_i_id, BigDecimal n_betrag, BigDecimal n_ust, Integer i_auszug, Date t_anlegen, String buchungdetailart_c_nr, Integer i_ausziffern, com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung flrbuchung, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto, com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrgegenkonto) {
        this.buchung_i_id = buchung_i_id;
        this.konto_i_id = konto_i_id;
        this.n_betrag = n_betrag;
        this.n_ust = n_ust;
        this.i_auszug = i_auszug;
        this.t_anlegen = t_anlegen;
        this.buchungdetailart_c_nr = buchungdetailart_c_nr;
        this.i_ausziffern = i_ausziffern;
        this.flrbuchung = flrbuchung;
        this.flrkonto = flrkonto;
        this.flrgegenkonto = flrgegenkonto;
    }

    /** default constructor */
    public FLRFinanzBuchungDetail() {
    }

    public Integer getI_id() {
        return this.i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public Integer getBuchung_i_id() {
        return this.buchung_i_id;
    }

    public void setBuchung_i_id(Integer buchung_i_id) {
        this.buchung_i_id = buchung_i_id;
    }

    public Integer getKonto_i_id() {
        return this.konto_i_id;
    }

    public void setKonto_i_id(Integer konto_i_id) {
        this.konto_i_id = konto_i_id;
    }

    public BigDecimal getN_betrag() {
        return this.n_betrag;
    }

    public void setN_betrag(BigDecimal n_betrag) {
        this.n_betrag = n_betrag;
    }

    public BigDecimal getN_ust() {
        return this.n_ust;
    }

    public void setN_ust(BigDecimal n_ust) {
        this.n_ust = n_ust;
    }

    public Integer getI_auszug() {
        return this.i_auszug;
    }

    public void setI_auszug(Integer i_auszug) {
        this.i_auszug = i_auszug;
    }

    public Date getT_anlegen() {
        return this.t_anlegen;
    }

    public void setT_anlegen(Date t_anlegen) {
        this.t_anlegen = t_anlegen;
    }

    public String getBuchungdetailart_c_nr() {
        return this.buchungdetailart_c_nr;
    }

    public void setBuchungdetailart_c_nr(String buchungdetailart_c_nr) {
        this.buchungdetailart_c_nr = buchungdetailart_c_nr;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung getFlrbuchung() {
        return this.flrbuchung;
    }

    public void setFlrbuchung(com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung flrbuchung) {
        this.flrbuchung = flrbuchung;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkonto() {
        return this.flrkonto;
    }

    public void setFlrkonto(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto) {
        this.flrkonto = flrkonto;
    }

    public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrgegenkonto() {
        return this.flrgegenkonto;
    }

    public void setFlrgegenkonto(com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrgegenkonto) {
        this.flrgegenkonto = flrgegenkonto;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("i_id", getI_id())
            .toString();
    }

	public void setI_ausziffern(Integer i_ausziffern) {
		this.i_ausziffern = i_ausziffern;
	}

	public Integer getI_ausziffern() {
		return i_ausziffern;
	}

	public void setC_kommentar(String c_kommentar) {
		this.c_kommentar = c_kommentar;
	}

	public String getC_kommentar() {
		return c_kommentar;
	}

	public Integer getKonto_i_id_gegenkonto() {
		return konto_i_id_gegenkonto;
	}

	public void setKonto_i_id_gegenkonto(Integer konto_i_id_gegenkonto) {
		this.konto_i_id_gegenkonto = konto_i_id_gegenkonto;
	}

}
