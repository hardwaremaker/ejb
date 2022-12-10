package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FB_USTUEBERSETZUNG")
@NamedQueries({
	@NamedQuery(name = UstUebersetzungQuery.ByMandant, query = "SELECT OBJECT (o) FROM UstUebersetzung o WHERE o.pk.mandantCNr = :mandant")
})
public class UstUebersetzung implements Serializable {
	private static final long serialVersionUID = -3121004694071227704L;

	@EmbeddedId
	private UstUebersetzungPK pk;

	@Column(name = "MWSTSATZBEZ_I_ID")
	private Integer mwstSatzBezIId;
	
	@Column(name = "B_IGERWERB")
	private Short bIgErwerb;

	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartId;

	public UstUebersetzung() {
	}
	
	public UstUebersetzung(String cNr, String mandantCNr, Integer mwstSatzBezIId, Short bIgErwerb, Integer reversechargeartId) {
		setPk(new UstUebersetzungPK(cNr, mandantCNr));
		setMwstSatzBezIId(mwstSatzBezIId);
		setBIgErwerb(bIgErwerb);
		setReversechargeartId(reversechargeartId);
	}

	public void setPk(UstUebersetzungPK pk) {
		this.pk = pk;
	}
	
	public UstUebersetzungPK getPk() {
		return pk;
	}
	
	public void setMwstSatzBezIId(Integer mwstSatzBezIId) {
		this.mwstSatzBezIId = mwstSatzBezIId;
	}
	
	public Integer getMwstSatzBezIId() {
		return mwstSatzBezIId;
	}
	
	public void setBIgErwerb(Short bIgErwerb) {
		this.bIgErwerb = bIgErwerb;
	}
	
	public Short getBIgErwerb() {
		return bIgErwerb;
	}
	
	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
	
	public Integer getReversechargeartId() {
		return reversechargeartId;
	}
}
