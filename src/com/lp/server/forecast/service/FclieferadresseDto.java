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
package com.lp.server.forecast.service;

import java.io.Serializable;

public class FclieferadresseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iId;
	private Integer forecastIId;
	private Integer kundeIIdLieferadresse;

	public Integer getiId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private Integer kommdruckerIId;

	public Integer getKommdruckerIId() {
		return kommdruckerIId;
	}

	public void setKommdruckerIId(Integer kommdruckerIId) {
		this.kommdruckerIId = kommdruckerIId;
	}

	private Short bLiefeengenberuecksichtigen;

	public Short getBLiefermengenberuecksichtigen() {
		return bLiefeengenberuecksichtigen;
	}

	public void setBLiefermengenberuecksichtigen(Short bLiefeengenberuecksichtigen) {
		this.bLiefeengenberuecksichtigen = bLiefeengenberuecksichtigen;
	}

	private Short bKommissionieren;

	public Short getBKommissionieren() {
		return bKommissionieren;
	}

	public void setBKommissionieren(Short bKommissionieren) {
		this.bKommissionieren = bKommissionieren;
	}

	private Short bZusammenziehen;

	public Short getBZusammenziehen() {
		return bZusammenziehen;
	}

	public void setBZusammenziehen(Short bZusammenziehen) {
		this.bZusammenziehen = bZusammenziehen;
	}

	public Integer getForecastIId() {
		return forecastIId;
	}

	public void setForecastIId(Integer forecastIId) {
		this.forecastIId = forecastIId;
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeIIdLieferadresse;
	}

	public void setKundeIIdLieferadresse(Integer kundeIIdLieferadresse) {
		this.kundeIIdLieferadresse = kundeIIdLieferadresse;
	}

	private String cPfadImport;

	public String getCPfadImport() {
		return cPfadImport;
	}

	public void setCPfadImport(String cPfadImport) {
		this.cPfadImport = cPfadImport;
	}

	private String importdefCNrPfad;

	public String getImportdefCNrPfad() {
		return importdefCNrPfad;
	}

	public void setImportdefCNrPfad(String importdefCNrPfad) {
		this.importdefCNrPfad = importdefCNrPfad;
	}

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

	private Integer iTypRundungLinienabruf;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bKommissionieren == null) ? 0 : bKommissionieren.hashCode());
		result = prime * result + ((bZusammenziehen == null) ? 0 : bZusammenziehen.hashCode());
		result = prime * result + ((cPfadImport == null) ? 0 : cPfadImport.hashCode());
		result = prime * result + ((forecastIId == null) ? 0 : forecastIId.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((importdefCNrPfad == null) ? 0 : importdefCNrPfad.hashCode());
		result = prime * result + ((kundeIIdLieferadresse == null) ? 0 : kundeIIdLieferadresse.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FclieferadresseDto other = (FclieferadresseDto) obj;
		if (bKommissionieren == null) {
			if (other.bKommissionieren != null)
				return false;
		} else if (!bKommissionieren.equals(other.bKommissionieren))
			return false;
		if (bZusammenziehen == null) {
			if (other.bZusammenziehen != null)
				return false;
		} else if (!bZusammenziehen.equals(other.bZusammenziehen))
			return false;
		if (cPfadImport == null) {
			if (other.cPfadImport != null)
				return false;
		} else if (!cPfadImport.equals(other.cPfadImport))
			return false;
		if (forecastIId == null) {
			if (other.forecastIId != null)
				return false;
		} else if (!forecastIId.equals(other.forecastIId))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (importdefCNrPfad == null) {
			if (other.importdefCNrPfad != null)
				return false;
		} else if (!importdefCNrPfad.equals(other.importdefCNrPfad))
			return false;
		if (kundeIIdLieferadresse == null) {
			if (other.kundeIIdLieferadresse != null)
				return false;
		} else if (!kundeIIdLieferadresse.equals(other.kundeIIdLieferadresse))
			return false;
		return true;
	}

}
