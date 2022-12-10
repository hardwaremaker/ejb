package com.lp.server.partner.service;

import java.io.Serializable;

import com.lp.server.util.IIId;

public class KundeKennungDto implements Serializable, IIId {
	private static final long serialVersionUID = 3148955373558499881L;

	private Integer iId;
	private Integer kundeIId;
	private Integer kennungIId;
	private String  wert;
	
	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	public Integer getKennungIId() {
		return kennungIId;
	}

	public void setKennungIId(Integer kennungIId) {
		this.kennungIId = kennungIId;
	}

	public String getWert() {
		return wert;
	}

	public void setWert(String wert) {
		this.wert = wert;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((kennungIId == null) ? 0 : kennungIId.hashCode());
		result = prime * result + ((kundeIId == null) ? 0 : kundeIId.hashCode());
		result = prime * result + ((wert == null) ? 0 : wert.hashCode());
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
		KundeKennungDto other = (KundeKennungDto) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (kennungIId == null) {
			if (other.kennungIId != null)
				return false;
		} else if (!kennungIId.equals(other.kennungIId))
			return false;
		if (kundeIId == null) {
			if (other.kundeIId != null)
				return false;
		} else if (!kundeIId.equals(other.kundeIId))
			return false;
		if (wert == null) {
			if (other.wert != null)
				return false;
		} else if (!wert.equals(other.wert))
			return false;
		return true;
	}	
}
