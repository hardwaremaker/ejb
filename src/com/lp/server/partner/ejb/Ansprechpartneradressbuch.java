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
		@NamedQuery(name = Ansprechpartneradressbuch.QueryFindByCEmailadresseAnsprechpartnerIId, query = "SELECT OBJECT(o) FROM Ansprechpartneradressbuch o WHERE o.cEmailadresse=?1 AND o.ansprechpartnerIId=?2"),
		@NamedQuery(name = Ansprechpartneradressbuch.QueryFindByCEmailadresseExchangeId, query = "SELECT OBJECT(o) FROM Ansprechpartneradressbuch o WHERE o.cEmailadresse=?1 AND o.cExchangeid=?2")}
		)
@Entity
@Table(name = "PART_ANSPRECHPARTNERADRESSBUCH")
public class Ansprechpartneradressbuch implements Serializable {

	public final static String QueryFindByCEmailadresseAnsprechpartnerIId = "AnsprechpartneradressbuchFindByCEmailadresseAnsprechpartnerIId" ;
	public final static String QueryFindByCEmailadresseExchangeId = "AnsprechpartneradressbuchFindByCEmailadresseExchangeId" ;

	public Ansprechpartneradressbuch() {
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

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}

	@Column(name = "C_EXCHANGEID")
	private String cExchangeid;
	@Column(name = "T_ZULETZT_EXPORTIERT")
	private Timestamp tZuletztExportiert;

	private static final long serialVersionUID = 1L;

	public Ansprechpartneradressbuch(Integer iId, String cEmailadresse, Integer ansprechpartnerIId, String cExchangeid,
			Timestamp tZuletztExportiert) {
		setIId(iId);
		setCEmailadresse(cEmailadresse);
		setAnsprechpartnerIId(ansprechpartnerIId);
		setCExchangeid(cExchangeid);
		setTZuletztExportiert(tZuletztExportiert);
	}

}
