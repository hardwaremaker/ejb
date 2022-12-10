package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class VendidataArticleConsumption implements Serializable {
	
	private static final long serialVersionUID = -2008651811874521621L;

	private Integer fourVendingIId;
	private String artikelName;
	private BigDecimal anzahlArtikel;
	private Integer tourNr;
	private String debitorennummer;
	private Timestamp buchungsdatum;

	//Metadaten
	private Integer rowId;
	private Integer customerId;
	private Integer employeeId;
	private String employeeName;
	
	public VendidataArticleConsumption() {
	}

	public Integer getFourVendingIId() {
		return fourVendingIId;
	}

	public void setFourVendingIId(Integer fourVendingIId) {
		this.fourVendingIId = fourVendingIId;
	}

	public BigDecimal getAnzahlArtikel() {
		return anzahlArtikel;
	}

	public void setAnzahlArtikel(BigDecimal anzahlArtikel) {
		this.anzahlArtikel = anzahlArtikel;
	}

	public Integer getTourNr() {
		return tourNr;
	}

	public void setTourNr(Integer tourNr) {
		this.tourNr = tourNr;
	}

	public String getDebitorennummer() {
		return debitorennummer;
	}

	public void setDebitorennummer(String debitorennummer) {
		this.debitorennummer = debitorennummer;
	}

	public Timestamp getBuchungsdatum() {
		return buchungsdatum;
	}

	public void setBuchungsdatum(Timestamp buchungsdatum) {
		this.buchungsdatum = buchungsdatum;
	}

	public String getArtikelName() {
		return artikelName;
	}

	public void setArtikelName(String artikelName) {
		this.artikelName = artikelName;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
