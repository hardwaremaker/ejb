package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.util.Locale;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LieferscheinId;

/**
 * Die Attribute zum Druck eines Lieferscheins.
 * 
 * Defaultwerte sind:
 *   - keine Kopien
 *   - ohne Logo
 *   - Belegart Lieferschein
 *   - keine Seriennummer vorhanden
 *   - Locale aus dem Kunden
 * @author gerold
 */
public class PrintLieferscheinAttribute implements Serializable {
	private static final long serialVersionUID = 3701661010519700565L;

	private LieferscheinId lieferscheinId;
	private HvOptional<Integer> anzahlKopien = HvOptional.empty();
	private boolean mitLogo;
	private String belegart;
	private HvOptional<Locale> locDruckUebersteuert = HvOptional.empty();
	private HvOptional<Integer> serialNr = HvOptional.empty();

	public PrintLieferscheinAttribute(LieferscheinId lieferscheinId) {
		this.setLieferscheinId(lieferscheinId);
		this.setBelegart(LocaleFac.BELEGART_LIEFERSCHEIN);
	}

	public LieferscheinId getLieferscheinId() {
		return lieferscheinId;
	}

	public PrintLieferscheinAttribute setLieferscheinId(LieferscheinId lieferscheinId) {
		this.lieferscheinId = lieferscheinId;
		return this;
	}

	public HvOptional<Integer> getAnzahlKopien() {
		return anzahlKopien;
	}

	public PrintLieferscheinAttribute setAnzahlKopien(Integer anzahlKopien) {
		this.anzahlKopien = HvOptional.ofNullable(anzahlKopien);
		return this;
	}

	public boolean isMitLogo() {
		return mitLogo;
	}

	public PrintLieferscheinAttribute setMitLogo(boolean mitLogo) {
		this.mitLogo = mitLogo;
		return this;
	}

	public String getBelegart() {
		return belegart;
	}

	public PrintLieferscheinAttribute setBelegart(String belegart) {
		this.belegart = belegart;
		return this;
	}

	public HvOptional<Locale> getLocDruckUebersteuert() {
		return locDruckUebersteuert;
	}

	public PrintLieferscheinAttribute setLocDruckUebersteuert(Locale locDruckUebersteuert) {
		this.locDruckUebersteuert = HvOptional.ofNullable(locDruckUebersteuert);
		return this;
	}

	public HvOptional<Integer> getSerialNr() {
		return serialNr;
	}

	public PrintLieferscheinAttribute setSerialNr(Integer serialNumber) {
		this.serialNr = HvOptional.ofNullable(serialNumber);
		return this;
	}
}
