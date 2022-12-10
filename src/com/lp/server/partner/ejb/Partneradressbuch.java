package com.lp.server.partner.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = Partneradressbuch.QueryFindByCEmailadresseAnsprechpartnerIId, query = "SELECT OBJECT(o) FROM Partneradressbuch o WHERE o.cEmailadresse=?1 AND o.partnerIId=?2"),
		@NamedQuery(name = Partneradressbuch.QueryFindByCEmailadresseExchangeId, query = "SELECT OBJECT(o) FROM Partneradressbuch o WHERE o.cEmailadresse=?1 AND o.cExchangeid=?2") })
@Entity
@Table(name = "PART_PARTNERADRESSBUCH")
public class Partneradressbuch implements Serializable {

	public final static String QueryFindByCEmailadresseAnsprechpartnerIId = "PartneradressbuchFindByCEmailadressePartnerIId";
	public final static String QueryFindByCEmailadresseExchangeId = "PartneradressbuchFindByCEmailadresseExchangeId";

	public Partneradressbuch() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@Column(name = "C_EMAILADRESSE")
	private String cEmailadresse;

	public String getCEmailadresse() {
		return cEmailadresse;
	}

	public void setCEmailadresse(String cEmailadresse) {
		this.cEmailadresse = cEmailadresse;
	}

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	public String getCExchangeid() {
		return cExchangeid;
	}

	public void setCExchangeid(String cExchangeid) {
		this.cExchangeid = cExchangeid;
	}

	public Timestamp getTZuletztExportiert() {
		return tZuletztExportiert;
	}

	public void setTZuletztExportiert(Timestamp tZuletztExportiert) {
		this.tZuletztExportiert = tZuletztExportiert;
	}

	@Column(name = "PARTNER_I_ID")
	private Integer partnerIId;
	@Column(name = "C_EXCHANGEID")
	private String cExchangeid;
	@Column(name = "T_ZULETZT_EXPORTIERT")
	private Timestamp tZuletztExportiert;

	private static final long serialVersionUID = 1L;

	public Partneradressbuch(Integer iId, String cEmailadresse, Integer partnerIId, String cExchangeid,
			Timestamp tZuletztExportiert) {
		setIId(iId);
		setCEmailadresse(cEmailadresse);
		setPartnerIId(partnerIId);
		setCExchangeid(cExchangeid);
		setTZuletztExportiert(tZuletztExportiert);
	}

}
