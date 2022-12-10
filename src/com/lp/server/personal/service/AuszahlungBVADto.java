
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class AuszahlungBVADto implements Serializable {
	private Integer iId;
	private Timestamp tDatum;
	private BigDecimal nGleitzeit;
	private BigDecimal nUestd50Gz;
	private BigDecimal nUestd50Gz_Zuschlag;
	private BigDecimal nUestd50;
	private BigDecimal nUestd50_Zuschlag;
	private BigDecimal nUestd100;
	private BigDecimal nUestd100_Zuschlag;
	private String cKommentar;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private Integer personalIId;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}
	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	public BigDecimal getNGleitzeit() {
		return nGleitzeit;
	}

	public void setNGleitzeit(BigDecimal nGleitzeit) {
		this.nGleitzeit = nGleitzeit;
	}

	public BigDecimal getNUestd50Gz() {
		return nUestd50Gz;
	}

	public void setNUestd50Gz(BigDecimal nUestd50Gz) {
		this.nUestd50Gz = nUestd50Gz;
	}

	public BigDecimal getNUestd50Gz_Zuschlag() {
		return nUestd50Gz_Zuschlag;
	}

	public void setNUestd50Gz_Zuschlag(BigDecimal nUestd50Gz_Zuschlag) {
		this.nUestd50Gz_Zuschlag = nUestd50Gz_Zuschlag;
	}

	public BigDecimal getNUestd50() {
		return nUestd50;
	}

	public void setNUestd50(BigDecimal nUestd50) {
		this.nUestd50 = nUestd50;
	}

	public BigDecimal getNUestd50_Zuschlag() {
		return nUestd50_Zuschlag;
	}

	public void setNUestd50_Zuschlag(BigDecimal nUestd50_Zuschlag) {
		this.nUestd50_Zuschlag = nUestd50_Zuschlag;
	}

	public BigDecimal getNUestd100() {
		return nUestd100;
	}

	public void setNUestd100(BigDecimal nUestd100) {
		this.nUestd100 = nUestd100;
	}

	public BigDecimal getnUestd100_Zuschlag() {
		return nUestd100_Zuschlag;
	}

	public void setNUestd100_Zuschlag(BigDecimal nUestd100_Zuschlag) {
		this.nUestd100_Zuschlag = nUestd100_Zuschlag;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}
}
