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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.pkgenerator.PKConst;


@NamedQueries({
	@NamedQuery(name = ZeitdatenpruefenQuery.CountByPersonalId, query = "SELECT COUNT(c) FROM Zeitdatenpruefen c WHERE c.personalIId = :personalId")})
@Entity
@Table(name = "PERS_ZEITDATENPRUEFEN")
public class Zeitdatenpruefen implements Serializable {
	private static final long serialVersionUID = -3143375448380949296L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name=PKConst.PK_ZEITDATENPRUEFEN + "_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue=PKConst.PK_ZEITDATENPRUEFEN, valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator=PKConst.PK_ZEITDATENPRUEFEN + "_id")
	private Integer iId;

	@Column(name = "T_ZEIT")
	private Timestamp tZeit;

	@Column(name = "B_TAETIGKEITGEAENDERT")
	private Short bTaetigkeitgeaendert;

	@Column(name = "C_BELEGARTNR")
	private String cBelegartnr;

	@Column(name = "I_BELEGARTID")
	private Integer iBelegartid;

	@Column(name = "I_BELEGARTPOSITIONID")
	private Integer iBelegartpositionid;

	@Column(name = "C_BEMERKUNGZUBELEGART")
	private String cBemerkungzubelegart;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "B_AUTOMATIKBUCHUNG")
	private Short bAutomatikbuchung;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "C_WOWURDEGEBUCHT")
	private String cWowurdegebucht;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "TAETIGKEIT_I_ID")
	private Integer taetigkeitIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;
	
	@Column(name = "I_FEHLERCODE")
	private Integer fehlerCode;
	
	@Column(name = "X_FEHLERTEXT")
	private String xFehlertext;

	public Zeitdatenpruefen() {
		super();
	}

	public Zeitdatenpruefen(Integer id,
			Integer personalIId,
			Timestamp zeit,
			Short taetigkeitgeaendert,
			Integer personalIIdAnlegen2, Integer personalIIdAendern2,
			Short automatikbuchung) {
		setIId(id);
		setPersonalIId(personalIId);
		setTZeit(zeit);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		setBTaetigkeitgeaendert(taetigkeitgeaendert);
		setBAutomatikbuchung(automatikbuchung);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTZeit() {
		return this.tZeit;
	}

	public void setTZeit(Timestamp tZeit) {
		this.tZeit = tZeit;
	}

	public Short getBTaetigkeitgeaendert() {
		return this.bTaetigkeitgeaendert;
	}

	public void setBTaetigkeitgeaendert(Short bTaetigkeitgeaendert) {
		this.bTaetigkeitgeaendert = bTaetigkeitgeaendert;
	}

	public String getCBelegartnr() {
		return this.cBelegartnr;
	}

	public void setCBelegartnr(String cBelegartnr) {
		this.cBelegartnr = cBelegartnr;
	}

	public Integer getIBelegartid() {
		return this.iBelegartid;
	}

	public void setIBelegartid(Integer iBelegartid) {
		this.iBelegartid = iBelegartid;
	}

	public Integer getIBelegartpositionid() {
		return this.iBelegartpositionid;
	}

	public void setIBelegartpositionid(Integer iBelegartpositionid) {
		this.iBelegartpositionid = iBelegartpositionid;
	}

	public String getCBemerkungzubelegart() {
		return this.cBemerkungzubelegart;
	}

	public void setCBemerkungzubelegart(String cBemerkungzubelegart) {
		this.cBemerkungzubelegart = cBemerkungzubelegart;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Short getBAutomatikbuchung() {
		return this.bAutomatikbuchung;
	}

	public void setBAutomatikbuchung(Short bAutomatikbuchung) {
		this.bAutomatikbuchung = bAutomatikbuchung;
	}

	public String getXKommentar() {
		return this.xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public String getCWowurdegebucht() {
		return this.cWowurdegebucht;
	}

	public void setCWowurdegebucht(String cWowurdegebucht) {
		this.cWowurdegebucht = cWowurdegebucht;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getTaetigkeitIId() {
		return this.taetigkeitIId;
	}

	public void setTaetigkeitIId(Integer taetigkeitIId) {
		this.taetigkeitIId = taetigkeitIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getFehlerCode() {
		return this.fehlerCode;
	}
	
	public void setFehlerCode(Integer fehlerCode) {
		this.fehlerCode = fehlerCode;
	}
	
	public String getXFehlerText() {
		return this.xFehlertext;
	}
	
	public void setXFehlerText(String fehlerText) {
		this.xFehlertext = fehlerText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artikelIId == null) ? 0 : artikelIId.hashCode());
		result = prime * result + ((bAutomatikbuchung == null) ? 0 : bAutomatikbuchung.hashCode());
		result = prime * result + ((bTaetigkeitgeaendert == null) ? 0 : bTaetigkeitgeaendert.hashCode());
		result = prime * result + ((cBelegartnr == null) ? 0 : cBelegartnr.hashCode());
		result = prime * result + ((cBemerkungzubelegart == null) ? 0 : cBemerkungzubelegart.hashCode());
		result = prime * result + ((cWowurdegebucht == null) ? 0 : cWowurdegebucht.hashCode());
		result = prime * result + ((fehlerCode == null) ? 0 : fehlerCode.hashCode());
		result = prime * result + ((iBelegartid == null) ? 0 : iBelegartid.hashCode());
		result = prime * result + ((iBelegartpositionid == null) ? 0 : iBelegartpositionid.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((personalIId == null) ? 0 : personalIId.hashCode());
		result = prime * result + ((personalIIdAendern == null) ? 0 : personalIIdAendern.hashCode());
		result = prime * result + ((personalIIdAnlegen == null) ? 0 : personalIIdAnlegen.hashCode());
		result = prime * result + ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime * result + ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime * result + ((tZeit == null) ? 0 : tZeit.hashCode());
		result = prime * result + ((taetigkeitIId == null) ? 0 : taetigkeitIId.hashCode());
		result = prime * result + ((xFehlertext == null) ? 0 : xFehlertext.hashCode());
		result = prime * result + ((xKommentar == null) ? 0 : xKommentar.hashCode());
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
		Zeitdatenpruefen other = (Zeitdatenpruefen) obj;
		if (artikelIId == null) {
			if (other.artikelIId != null)
				return false;
		} else if (!artikelIId.equals(other.artikelIId))
			return false;
		if (bAutomatikbuchung == null) {
			if (other.bAutomatikbuchung != null)
				return false;
		} else if (!bAutomatikbuchung.equals(other.bAutomatikbuchung))
			return false;
		if (bTaetigkeitgeaendert == null) {
			if (other.bTaetigkeitgeaendert != null)
				return false;
		} else if (!bTaetigkeitgeaendert.equals(other.bTaetigkeitgeaendert))
			return false;
		if (cBelegartnr == null) {
			if (other.cBelegartnr != null)
				return false;
		} else if (!cBelegartnr.equals(other.cBelegartnr))
			return false;
		if (cBemerkungzubelegart == null) {
			if (other.cBemerkungzubelegart != null)
				return false;
		} else if (!cBemerkungzubelegart.equals(other.cBemerkungzubelegart))
			return false;
		if (cWowurdegebucht == null) {
			if (other.cWowurdegebucht != null)
				return false;
		} else if (!cWowurdegebucht.equals(other.cWowurdegebucht))
			return false;
		if (fehlerCode == null) {
			if (other.fehlerCode != null)
				return false;
		} else if (!fehlerCode.equals(other.fehlerCode))
			return false;
		if (iBelegartid == null) {
			if (other.iBelegartid != null)
				return false;
		} else if (!iBelegartid.equals(other.iBelegartid))
			return false;
		if (iBelegartpositionid == null) {
			if (other.iBelegartpositionid != null)
				return false;
		} else if (!iBelegartpositionid.equals(other.iBelegartpositionid))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (personalIId == null) {
			if (other.personalIId != null)
				return false;
		} else if (!personalIId.equals(other.personalIId))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (personalIIdAnlegen == null) {
			if (other.personalIIdAnlegen != null)
				return false;
		} else if (!personalIIdAnlegen.equals(other.personalIIdAnlegen))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		if (tZeit == null) {
			if (other.tZeit != null)
				return false;
		} else if (!tZeit.equals(other.tZeit))
			return false;
		if (taetigkeitIId == null) {
			if (other.taetigkeitIId != null)
				return false;
		} else if (!taetigkeitIId.equals(other.taetigkeitIId))
			return false;
		if (xFehlertext == null) {
			if (other.xFehlertext != null)
				return false;
		} else if (!xFehlertext.equals(other.xFehlertext))
			return false;
		if (xKommentar == null) {
			if (other.xKommentar != null)
				return false;
		} else if (!xKommentar.equals(other.xKommentar))
			return false;
		return true;
	}
}
