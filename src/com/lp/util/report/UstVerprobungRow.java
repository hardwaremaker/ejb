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
package com.lp.util.report;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UstVerprobungRow {

	private BigDecimal betrag;
	private BigDecimal steuer;
	private String steuerart;
	private Integer mwstSatzBezIId;
	private BigDecimal steuersatz;
	private String mwstSatzBezCBez;
	private String kontoCBez;
	private String kontoCNr;
	private String gegenkontoCBez;
	private String gegenkontoCNr;
	private String steuerkategorieCBez;
	private int steuerkategorieISort;
	private Timestamp datum;
	
	public BigDecimal getBetrag() {
		return betrag;
	}
	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}
	
	public void setSteuer(BigDecimal steuer) {
		this.steuer = steuer;
	}
	public BigDecimal getSteuer() {
		return steuer;
	}
	public void setSteuerart(String steuerart) {
		this.steuerart = steuerart;
	}
	public String getSteuerart() {
		return steuerart;
	}
	public Integer getMwstSatzBezIId() {
		return mwstSatzBezIId;
	}
	public void setMwstSatzBezIId(Integer mwstSatzBezIId) {
		this.mwstSatzBezIId = mwstSatzBezIId;
	}
	public String getMwstSatzBezCBez() {
		return mwstSatzBezCBez;
	}
	public void setMwstSatzBezCBez(String mwstSatzBezCBez) {
		this.mwstSatzBezCBez = mwstSatzBezCBez;
	}
	public String getKontoCNr() {
		return kontoCNr;
	}
	public void setKontoCNr(String kontoCNr) {
		this.kontoCNr = kontoCNr;
	}
	public String getGegenkontoCNr() {
		return gegenkontoCNr;
	}
	public void setGegenkontoCNr(String gegenkontoCNr) {
		this.gegenkontoCNr = gegenkontoCNr;
	}
	public String getKontoCBez() {
		return kontoCBez;
	}
	public void setKontoCBez(String kontoCBez) {
		this.kontoCBez = kontoCBez;
	}
	public String getGegenkontoCBez() {
		return gegenkontoCBez;
	}
	public void setGegenkontoCBez(String gegenkontoCBez) {
		this.gegenkontoCBez = gegenkontoCBez;
	}
	public BigDecimal getSteuersatz() {
		return steuersatz;
	}
	public void setSteuersatz(BigDecimal steuersatz) {
		this.steuersatz = steuersatz;
	}
	public String getSteuerkategorieCBez() {
		return steuerkategorieCBez;
	}
	public void setSteuerkategorieCBez(String steuerkategorieCBez) {
		this.steuerkategorieCBez = steuerkategorieCBez;
	}
	public int getSteuerkategorieISort() {
		return steuerkategorieISort;
	}
	public void setSteuerkategorieISort(int steuerkategorieISort) {
		this.steuerkategorieISort = steuerkategorieISort;
	}
	public Timestamp getDatum() {
		return datum;
	}
	public void setDatum(Timestamp datum) {
		this.datum = datum;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UstVerprobungRow other = (UstVerprobungRow) obj;
		if (betrag == null) {
			if (other.betrag != null)
				return false;
		} else if (!betrag.equals(other.betrag))
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (gegenkontoCBez == null) {
			if (other.gegenkontoCBez != null)
				return false;
		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
			return false;
		if (gegenkontoCNr == null) {
			if (other.gegenkontoCNr != null)
				return false;
		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
			return false;
		if (kontoCBez == null) {
			if (other.kontoCBez != null)
				return false;
		} else if (!kontoCBez.equals(other.kontoCBez))
			return false;
		if (kontoCNr == null) {
			if (other.kontoCNr != null)
				return false;
		} else if (!kontoCNr.equals(other.kontoCNr))
			return false;
		if (mwstSatzBezCBez == null) {
			if (other.mwstSatzBezCBez != null)
				return false;
		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
			return false;
		if (mwstSatzBezIId == null) {
			if (other.mwstSatzBezIId != null)
				return false;
		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
			return false;
		if (steuer == null) {
			if (other.steuer != null)
				return false;
		} else if (!steuer.equals(other.steuer))
			return false;
		if (steuerart == null) {
			if (other.steuerart != null)
				return false;
		} else if (!steuerart.equals(other.steuerart))
			return false;
		if (steuerkategorieCBez == null) {
			if (other.steuerkategorieCBez != null)
				return false;
		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
			return false;
		if (steuerkategorieISort != other.steuerkategorieISort)
			return false;
		if (steuersatz == null) {
			if (other.steuersatz != null)
				return false;
		} else if (!steuersatz.equals(other.steuersatz))
			return false;
		return true;
	}
	public boolean equalsAusgenommenBetraege(UstVerprobungRow obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UstVerprobungRow other = (UstVerprobungRow) obj;
		if (gegenkontoCBez == null) {
			if (other.gegenkontoCBez != null)
				return false;
		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
			return false;
		if (gegenkontoCNr == null) {
			if (other.gegenkontoCNr != null)
				return false;
		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
			return false;
		if (kontoCBez == null) {
			if (other.kontoCBez != null)
				return false;
		} else if (!kontoCBez.equals(other.kontoCBez))
			return false;
		if (kontoCNr == null) {
			if (other.kontoCNr != null)
				return false;
		} else if (!kontoCNr.equals(other.kontoCNr))
			return false;
		if (mwstSatzBezCBez == null) {
			if (other.mwstSatzBezCBez != null)
				return false;
		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
			return false;
		if (mwstSatzBezIId == null) {
			if (other.mwstSatzBezIId != null)
				return false;
		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
			return false;
		if (steuerart == null) {
			if (other.steuerart != null)
				return false;
		} else if (!steuerart.equals(other.steuerart))
			return false;
		if (steuerkategorieCBez == null) {
			if (other.steuerkategorieCBez != null)
				return false;
		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
			return false;
		if (steuerkategorieISort != other.steuerkategorieISort)
			return false;
		if (steuersatz == null) {
			if (other.steuersatz != null)
				return false;
		} else if (!steuersatz.equals(other.steuersatz))
			return false;
		return true;
	}

}
