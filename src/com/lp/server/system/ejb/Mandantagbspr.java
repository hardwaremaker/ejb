package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "MandantagbsprFindByMandantCNrLocaleCnr", query = "SELECT OBJECT(o) FROM Mandantagbspr o WHERE o.mandantCNr=?1 AND o.localeCNr=?2") })
@Entity
@Table(name = "LP_MANDANTAGBSPR")
public class Mandantagbspr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getLocaleCNr() {
		return localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public byte[] getOPDF() {
		return oPDF;
	}

	public void setOPDF(byte[] oPDF) {
		this.oPDF = oPDF;
	}

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "LOCALE_C_NR")
	private String localeCNr;

	@Column(name = "O_PDF")
	private byte[] oPDF;


	public Mandantagbspr() {
		super();
	}
	
	public Mandantagbspr(Integer id, String mandantCNr, String locUiAsString, byte[] oPDF) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setLocaleCNr(locUiAsString);
		setOPDF(oPDF);

	}

}
