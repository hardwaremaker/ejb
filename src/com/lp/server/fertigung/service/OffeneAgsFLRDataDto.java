package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class OffeneAgsFLRDataDto implements IOffeneAgsFLRData, Serializable {
	private static final long serialVersionUID = 746578699538364588L;
	private String auftragCnr ;
	private Integer auftragId ;
	private boolean overdue ;
	private Long finalTermin ;
	private String losProjekt ;
	private String taetigkeitArtikelKbez ;
	private String partlistItemCnr ;
	private String partlistItemDescription ;
	private String partlistItemShortDescription ;
	private BigDecimal actualTime;
	private boolean starttimeMoveable;
	private boolean finishtimeMoveable;
	private BigDecimal openQuantity;
	private Long losEndeTermin;
	
	public OffeneAgsFLRDataDto() {
	}
	
	public OffeneAgsFLRDataDto(Integer auftragId, String auftragCnr, long finalTermin, long losEndeTermin, boolean overdue) {
		this.auftragId = auftragId ;
		this.auftragCnr = auftragCnr ;
		this.finalTermin = finalTermin ;
		this.losEndeTermin = losEndeTermin;
		this.overdue = overdue ;
	}
	
	public OffeneAgsFLRDataDto(long losEndeTermin, boolean overdue) {
		this.losEndeTermin = losEndeTermin ;
		this.overdue = overdue ;		
	}
	
	@Override
	public Integer getAuftragId() {
		return auftragId ;
	}
	
	@Override
	public String getAuftragCnr() {
		return auftragCnr ;
	}
	@Override
	public boolean isOverdue() {
		return overdue ;
	}
	
	@Override
	public Long getFinalTermin() {
		return finalTermin ;
	}

	@Override
	public String getLosProjekt() {
		return losProjekt ;
	}

	@Override
	public String getTaetigkeitArtikelKBez() {
		return taetigkeitArtikelKbez ;
	}
	
	public void setLosProjekt(String losProjekt) {
		this.losProjekt = losProjekt ;
	}

	public void setTaetigkeitArtikelKBez(String c_kbez) {
		this.taetigkeitArtikelKbez = c_kbez ;
	}

	public String getPartlistItemCnr() {
		return partlistItemCnr;
	}

	public void setPartlistItemCnr(String partlistItemCnr) {
		this.partlistItemCnr = partlistItemCnr;
	}

	public String getPartlistItemDescription() {
		return partlistItemDescription;
	}

	public void setPartlistItemDescription(String partlistItemDescription) {
		this.partlistItemDescription = partlistItemDescription;
	}

	public String getPartlistItemShortDescription() {
		return partlistItemShortDescription;
	}

	public void setPartlistItemShortDescription(
			String partlistItemShortDescription) {
		this.partlistItemShortDescription = partlistItemShortDescription;
	}

	public BigDecimal getActualTime() {
		return actualTime;
	}

	public void setActualTime(BigDecimal actualTime) {
		this.actualTime = actualTime;
	}
	
	@Override
	public boolean isStartTimeMoveable() {
		return starttimeMoveable;
	}
	
	public void setStartTimeMoveable(boolean moveable) {
		this.starttimeMoveable = moveable;
	}

	public boolean isFinishTimeMoveable() {
		return finishtimeMoveable;
	}

	public void setFinishTimeMoveable(boolean finishtimeMoveable) {
		this.finishtimeMoveable = finishtimeMoveable;
	}

	public BigDecimal getOpenQuantity() {
		return openQuantity;
	}

	public void setOpenQuantity(BigDecimal openQuantity) {
		this.openQuantity = openQuantity;
	}
	
	@Override
	public Long getLosEndeTermin() {
		return losEndeTermin;
	}
}
