package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = Iso20022Query.BankverbindungByBankverbindungIId, query = "SELECT OBJECT (o) FROM Iso20022Bankverbindung o WHERE o.bankverbindungIId = ?1")
})

@Entity
@Table(name = ITablenames.FB_ISO20022BANKVERBINDUNG)
public class Iso20022Bankverbindung implements Serializable {
	private static final long serialVersionUID = -4847681866444895066L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name="iso20022bankverbindung_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue="iso20022bankverbindung", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="iso20022bankverbindung_id")
	private Integer iId;

	@Column(name = "BANKVERBINDUNG_I_ID")
	private Integer bankverbindungIId;
	
	@Column(name = "ZAHLUNGSAUFTRAGSCHEMA_I_ID")
	private Integer zahlungsauftragschemaIId;

	@Column(name = "LASTSCHRIFTSCHEMA_I_ID")
	private Integer lastschriftschemaIId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZAHLUNGSAUFTRAGSCHEMA_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	private Iso20022ZahlungsauftragSchema zahlungsauftragschema;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LASTSCHRIFTSCHEMA_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	private Iso20022LastschriftSchema lastschriftschema;

	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}
	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}
	
	public Integer getZahlungsauftragschemaIId() {
		return zahlungsauftragschemaIId;
	}
	public void setZahlungsauftragschemaIId(Integer zahlungsauftragschemaIId) {
		this.zahlungsauftragschemaIId = zahlungsauftragschemaIId;
	}
	
	public Integer getLastschriftschemaIId() {
		return lastschriftschemaIId;
	}
	public void setLastschriftschemaIId(Integer lastschriftschemaIId) {
		this.lastschriftschemaIId = lastschriftschemaIId;
	}
	
	public Iso20022LastschriftSchema getLastschriftschema() {
		return lastschriftschema;
	}
	
	public Iso20022ZahlungsauftragSchema getZahlungsauftragschema() {
		return zahlungsauftragschema;
	}
}
