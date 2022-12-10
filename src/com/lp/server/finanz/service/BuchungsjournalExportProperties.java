package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;

public class BuchungsjournalExportProperties implements Serializable {
	private static final long serialVersionUID = 8453822939648819437L;

	private String format;
	private BuchungsjournalExportDatumsart datumsart;
	private Date von;
	private Date bis;
	private boolean mitAutoEroeffnungsbuchungen;
	private boolean mitAutoBuchungen;
	private boolean mitManEroeffnungsbuchungen;
	private boolean mitStornierte;
	private String bezeichnung;
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public BuchungsjournalExportDatumsart getDatumsart() {
		return datumsart;
	}
	public void setDatumsart(BuchungsjournalExportDatumsart datumsart) {
		this.datumsart = datumsart;
	}
	public Date getVon() {
		return von;
	}
	public void setVon(Date von) {
		this.von = von;
	}
	public Date getBis() {
		return bis;
	}
	public void setBis(Date bis) {
		this.bis = bis;
	}
	public boolean isMitAutoEroeffnungsbuchungen() {
		return mitAutoEroeffnungsbuchungen;
	}
	public void setMitAutoEroeffnungsbuchungen(boolean mitAutoEroeffnungsbuchungen) {
		this.mitAutoEroeffnungsbuchungen = mitAutoEroeffnungsbuchungen;
	}
	public boolean isMitAutoBuchungen() {
		return mitAutoBuchungen;
	}
	public void setMitAutoBuchungen(boolean mitAutoBuchungen) {
		this.mitAutoBuchungen = mitAutoBuchungen;
	}
	public boolean isMitManEroeffnungsbuchungen() {
		return mitManEroeffnungsbuchungen;
	}
	public void setMitManEroeffnungsbuchungen(boolean mitManEroeffnungsbuchungen) {
		this.mitManEroeffnungsbuchungen = mitManEroeffnungsbuchungen;
	}
	public boolean isMitStornierte() {
		return mitStornierte;
	}
	public void setMitStornierte(boolean mitStornierte) {
		this.mitStornierte = mitStornierte;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
}
