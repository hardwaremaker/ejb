package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class LagerstandInfoDto implements Serializable {
	private static final long serialVersionUID = -4065389562286416471L;
	
	private Integer lagerId;
	private Collection<LagerstandInfoEntryDto> entries;
	
	public LagerstandInfoDto(Integer lagerId) {
		this(lagerId, new ArrayList<LagerstandInfoEntryDto>());
	}

	public LagerstandInfoDto(Integer lagerId, Collection<LagerstandInfoEntryDto> entries) {
		this.lagerId = lagerId;
		this.entries = entries;
	}
		
	public Integer getLagerId() {
		return lagerId;
	}
	public void setLagerId(Integer lagerId) {
		this.lagerId = lagerId;
	}
	
	public Collection<LagerstandInfoEntryDto> getEntries() {
		return entries;
	}
	public void setEntries(Collection<LagerstandInfoEntryDto> entries) {
		this.entries = entries;
	}
	
	public void addEntry(LagerstandInfoEntryDto entry) {
		this.entries.add(entry);
	}
}
