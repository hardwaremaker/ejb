package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KennungsprPK implements Serializable {
	private static final long serialVersionUID = -7538928475799625518L;

	@Column(name = "KENNUNG_I_ID", insertable = false, updatable=false)
	private Integer kennungIId;

	@Column(name = "LOCALE_C_NR", insertable = false, updatable = false)
	private String localeCNr;

	public KennungsprPK() {
	}
	
	public KennungsprPK(Integer kennungId, String localeCNr) {
		setKennungIId(kennungId);
		setLocaleCNr(localeCNr);
	}

	public Integer getKennungIId() {
		return kennungIId;
	}

	public void setKennungIId(Integer kennungIId) {
		this.kennungIId = kennungIId;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kennungIId == null) ? 0 : kennungIId.hashCode());
		result = prime * result + ((localeCNr == null) ? 0 : localeCNr.hashCode());
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
		KennungsprPK other = (KennungsprPK) obj;
		if (kennungIId == null) {
			if (other.kennungIId != null)
				return false;
		} else if (!kennungIId.equals(other.kennungIId))
			return false;
		if (localeCNr == null) {
			if (other.localeCNr != null)
				return false;
		} else if (!localeCNr.equals(other.localeCNr))
			return false;
		return true;
	}
}
