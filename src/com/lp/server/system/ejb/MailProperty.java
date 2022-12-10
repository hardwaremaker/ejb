package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries( {
	@NamedQuery(name = MailPropertyQuery.AllByMandant, query = "SELECT OBJECT(o) FROM MailProperty o WHERE o.pk.mandantCNr=?1"),
	@NamedQuery(name = MailPropertyQuery.ByMandantCWertNotNull, query = "SELECT OBJECT(o) FROM MailProperty o WHERE o.pk.mandantCNr=?1 AND cWert IS NOT NULL")
})

@Entity
@Table(name = ITablenames.LP_MAILPROPERTY)
public class MailProperty implements Serializable {
	private static final long serialVersionUID = -278994711997514455L;

	@EmbeddedId
	private MailPropertyPK pk;
	
	@Column(name = "C_WERT")
	private String cWert;
	
	@Column(name = "C_DEFAULTWERT")
	private String cDefaultWert;
	
	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	@Column(name = "T_AENDERN")
	private Timestamp tAendern;
	
	public MailProperty() {
	}
	
	public MailProperty(String cNr, String mandantCNr) {
		setPk(new MailPropertyPK(cNr, mandantCNr));
	}
	
	public MailPropertyPK getPk() {
		return pk;
	}
	public void setPk(MailPropertyPK pk) {
		this.pk = pk;
	}
	
	public String getCWert() {
		return cWert;
	}
	public void setCWert(String cWert) {
		this.cWert = cWert;
	}
	
	public String getCDefaultWert() {
		return cDefaultWert;
	}
	public void setCDefaultWert(String cDefaultWert) {
		this.cDefaultWert = cDefaultWert;
	}
	
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}
	
	public Timestamp getTAendern() {
		return tAendern;
	}
	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}
}
