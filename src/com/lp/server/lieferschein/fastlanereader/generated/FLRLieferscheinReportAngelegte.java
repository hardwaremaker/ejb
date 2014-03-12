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
package com.lp.server.lieferschein.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.personal.fastlanereader.generated.FLRPersonal;
import com.lp.server.system.fastlanereader.generated.FLRKostenstelle;

/** @author Hibernate CodeGenerator */
public class FLRLieferscheinReportAngelegte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String lieferscheinstatus_status_c_nr;

	/** nullable persistent field */
	private Integer kunde_i_id_lieferadresse;

	/** nullable persistent field */
	private Integer kostenstelle_i_id;

	/** nullable persistent field */
	private Date t_anlegen;

	/** nullable persistent field */
	private FLRPersonal flrpersonalanlegen;

	/** nullable persistent field */
	private FLRKunde flrkunde;

	/** nullable persistent field */
	private FLRKostenstelle flrkostenstelle;
	
	private FLRAuftrag flrauftrag;

	/** full constructor */
	public FLRLieferscheinReportAngelegte(String mandant_c_nr, String c_nr,
			String lieferscheinstatus_status_c_nr,
			Integer kunde_i_id_lieferadresse, Integer kostenstelle_i_id,
			Date t_anlegen, FLRPersonal flrpersonalanlegen, FLRKunde flrkunde,
			FLRKostenstelle flrkostenstelle,
			FLRAuftrag flrauftrag) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_nr = c_nr;
		this.lieferscheinstatus_status_c_nr = lieferscheinstatus_status_c_nr;
		this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
		this.kostenstelle_i_id = kostenstelle_i_id;
		this.t_anlegen = t_anlegen;
		this.flrpersonalanlegen = flrpersonalanlegen;
		this.flrkunde = flrkunde;
		this.flrkostenstelle = flrkostenstelle;
		this.flrauftrag = flrauftrag;
	}

	/** default constructor */
	public FLRLieferscheinReportAngelegte() {
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

	public String getLieferscheinstatus_status_c_nr() {
		return this.lieferscheinstatus_status_c_nr;
	}

	public void setLieferscheinstatus_status_c_nr(
			String lieferscheinstatus_status_c_nr) {
		this.lieferscheinstatus_status_c_nr = lieferscheinstatus_status_c_nr;
	}

	public Integer getKunde_i_id_lieferadresse() {
		return this.kunde_i_id_lieferadresse;
	}

	public void setKunde_i_id_lieferadresse(Integer kunde_i_id_lieferadresse) {
		this.kunde_i_id_lieferadresse = kunde_i_id_lieferadresse;
	}

	public Integer getKostenstelle_i_id() {
		return this.kostenstelle_i_id;
	}

	public void setKostenstelle_i_id(Integer kostenstelle_i_id) {
		this.kostenstelle_i_id = kostenstelle_i_id;
	}

	public Date getT_anlegen() {
		return this.t_anlegen;
	}

	public void setT_anlegen(Date t_anlegen) {
		this.t_anlegen = t_anlegen;
	}

	public FLRPersonal getFlrpersonalanlegen() {
		return this.flrpersonalanlegen;
	}

	public void setFlrpersonalanlegen(FLRPersonal flrpersonalanlegen) {
		this.flrpersonalanlegen = flrpersonalanlegen;
	}

	public FLRKunde getFlrkunde() {
		return this.flrkunde;
	}

	public void setFlrkunde(FLRKunde flrkunde) {
		this.flrkunde = flrkunde;
	}

	public FLRKostenstelle getFlrkostenstelle() {
		return this.flrkostenstelle;
	}

	public void setFlrkostenstelle(FLRKostenstelle flrkostenstelle) {
		this.flrkostenstelle = flrkostenstelle;
	}
	
    public FLRAuftrag getFlrauftrag() {
        return this.flrauftrag;
    }

    public void setFlrauftrag(FLRAuftrag flrauftrag) {
        this.flrauftrag = flrauftrag;
    }


	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
