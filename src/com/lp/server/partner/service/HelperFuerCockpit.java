
package com.lp.server.partner.service;

import java.io.Serializable;

public class HelperFuerCockpit implements Serializable {
	private static final long serialVersionUID = -8323077784985412322L;

	public static String PARTNER_I_ID = "partner_i_id";
	public static String ANSPRECHPARTNER_I_ID = "ansprechpartner_i_id";

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ansprechpartnerIId == null) ? 0 : ansprechpartnerIId.hashCode());
		result = prime * result + ((partnerIId == null) ? 0 : partnerIId.hashCode());
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
		HelperFuerCockpit other = (HelperFuerCockpit) obj;

		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (partnerIId == null) {
			if (other.partnerIId != null)
				return false;
		} else if (!partnerIId.equals(other.partnerIId))
			return false;
		if (ansprechpartnerIId == null) {
			if (other.ansprechpartnerIId != null)
				return false;
		} else if (!ansprechpartnerIId.equals(other.ansprechpartnerIId))
			return false;
		return true;
	}

	public boolean equals0(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HelperFuerCockpit)) {
			return false;
		}
		HelperFuerCockpit that = (HelperFuerCockpit) obj;
		if (!(that.partnerIId == null ? this.partnerIId == null : that.partnerIId.equals(this.partnerIId))) {
			return false;
		}

		if (!(that.ansprechpartnerIId == null ? this.ansprechpartnerIId == null
				: that.ansprechpartnerIId.equals(this.ansprechpartnerIId))) {
			return false;
		}

		return true;

	}

	private Integer partnerIId = null;
	private Integer ansprechpartnerIId = null;

	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	Integer iId = null;

	public Integer getiId() {
		return iId;
	}

	public void setIId(Integer id) {
		this.iId = id;
	}

	public HelperFuerCockpit(Integer partnerIId, Integer ansprechpartnerIId) {
		this.partnerIId = partnerIId;
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	public boolean isAnsprechpartner() {
		if (ansprechpartnerIId != null) {
			return true;
		}
		return false;
	}

}
