package com.lp.server.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.EditorContentIId;

public class EditorContentDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditorContentIId id;
	private String locale;
	private List<EditorBaseBlockDto> blockDtos;

	public EditorContentDto(EditorContentIId id, String locale, List<EditorBaseBlockDto> blockDtos) {
		this.id = id;
		this.locale = locale;
		this.blockDtos = new ArrayList<>(blockDtos);
	}
	
	public EditorContentDto() {
		id = new EditorContentIId(null);
		this.blockDtos = new ArrayList<>();
	}

	public EditorContentIId getId() {
		return id;
	}

	public void setId(EditorContentIId id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<EditorBaseBlockDto> getBlockDtos() {
		return blockDtos;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EditorBaseBlockDto> List<T> getBlockDtosOfType(Class<T> clazz) {
		List<T> results = new ArrayList<>();
		for(EditorBaseBlockDto dto : getBlockDtos()) {
			if(clazz.isAssignableFrom(dto.getClass())) {
				results.add((T) dto);
			}
		}
		return results;
	}
	
	public void addBlockDto(EditorBaseBlockDto block) {
		blockDtos.add(block);
		block.setContentDto(this);
	}

}
