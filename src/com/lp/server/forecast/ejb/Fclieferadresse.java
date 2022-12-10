package com.lp.server.forecast.ejb;

/***************************************************************************
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "FclieferadresseFindByForecastIIdKundeIIdLieferadresse", query = "SELECT OBJECT(o) FROM Fclieferadresse o WHERE o.forecastIId=?1 AND o.kundeIIdLieferadresse=?2"),
		@NamedQuery(name = "FclieferadresseFindByForecastIId", query = "SELECT OBJECT(o) FROM Fclieferadresse o WHERE o.forecastIId=?1"),
		@NamedQuery(name = FclieferadresseQuery.ByKundeIId, query = "SELECT OBJECT(o) FROM Fclieferadresse o WHERE o.kundeIIdLieferadresse=:kundeId")})
@Entity
@Table(name = "FC_FCLIEFERADRESSE")
public class Fclieferadresse implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getiId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@Column(name = "FORECAST_I_ID")
	private Integer forecastIId;
	
	@Column(name = "KOMMDRUCKER_I_ID")
	private Integer kommdruckerIId;
	
	
	public Integer getKommdruckerIId() {
		return kommdruckerIId;
	}

	public void setKommdruckerIId(Integer kommdruckerIId) {
		this.kommdruckerIId = kommdruckerIId;
	}

	@Column(name = "B_LIEFERMENGENBERUECKSICHTIGEN")
	private Short bLiefemengenberuecksichtigen;
	
	
	public Short getBLiefermengenberuecksichtigen() {
		return bLiefemengenberuecksichtigen;
	}

	public void setBLiefermengenberuecksichtigen(Short bLiefemengenberuecksichtigen) {
		this.bLiefemengenberuecksichtigen = bLiefemengenberuecksichtigen;
	}

	@Column(name = "I_TYP_RUNDUNG_POSITION")
	private Integer iTypRundungPosition;
	
	public Integer getITypRundungPosition() {
		return iTypRundungPosition;
	}

	public void setITypRundungPosition(Integer iTypRundungPosition) {
		this.iTypRundungPosition = iTypRundungPosition;
	}

	public Integer getITypRundungLinienabruf() {
		return iTypRundungLinienabruf;
	}

	public void setITypRundungLinienabruf(Integer iTypRundungLinienabruf) {
		this.iTypRundungLinienabruf = iTypRundungLinienabruf;
	}

	@Column(name = "I_TYP_RUNDUNG_LINIENABRUF")
	private Integer iTypRundungLinienabruf;
	
	public Integer getForecastIId() {
		return forecastIId;
	}

	public void setForecastIId(Integer forecastIId) {
		this.forecastIId = forecastIId;
	}

	@Column(name = "B_KOMMISSIONIEREN")
	private Short bKommissionieren;
	
	public Short getBKommissionieren() {
		return bKommissionieren;
	}

	public void setBKommissionieren(Short bKommissionieren) {
		this.bKommissionieren = bKommissionieren;
	}
	
	
	@Column(name = "B_ZUSAMMENZIEHEN")
	private Short bZusammenziehen;
	
	public Short getBZusammenziehen() {
		return bZusammenziehen;
	}

	public void setBZusammenziehen(Short bZusammenziehen) {
		this.bZusammenziehen = bZusammenziehen;
	}

	@Column(name = "C_PFAD_IMPORT")
	private String cPfadImport;
	
	public String getCPfadImport() {
		return cPfadImport;
	}

	public void setCPfadImport(String cPfadImport) {
		this.cPfadImport = cPfadImport;
	}

	@Column(name = "IMPORTDEF_C_NR_PFAD")
	private String importdefCNrPfad;
	
	public String getImportdefCNrPfad() {
		return importdefCNrPfad;
	}

	public void setImportdefCNrPfad(String importdefCNrPfad) {
		this.importdefCNrPfad = importdefCNrPfad;
	}

	@Column(name = "KUNDE_I_ID_LIEFERADRESSE")
	private Integer kundeIIdLieferadresse;

	private static final long serialVersionUID = 1L;

	public Fclieferadresse(Integer iId, Integer forecastIId,Integer kundeIIdLieferadresse, Short bZusammenziehen,Short bKommissionieren, Integer iTypRundungPosition, Integer iTypRundungLinienabruf,Short bLiefermengenberuecksichtigen) {
		setIId(iId);
		setForecastIId(forecastIId);
		setKundeIIdLieferadresse(kundeIIdLieferadresse);
		setBZusammenziehen(bZusammenziehen);
		setBKommissionieren(bKommissionieren);
		setITypRundungLinienabruf(iTypRundungLinienabruf);
		setITypRundungPosition(iTypRundungPosition);
		setBLiefermengenberuecksichtigen(bLiefermengenberuecksichtigen);
	}
	
	public Fclieferadresse() {
		
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}


}
