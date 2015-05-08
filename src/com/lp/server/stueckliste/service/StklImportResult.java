/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
 package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.ArtikelDto;

public class StklImportResult implements Serializable, IStklImportResult{
	
	private static final long serialVersionUID = -2483389935241793617L;

	private List<ArtikelDto> foundItems;
	
	private boolean totalMatch = false;
	private boolean foundTooManyArticles = false;
	private Integer selectedIndex = null;
	
	private Map<String, String> values;
	
	private boolean sokoUpdate = true;

	@Override
	public List<ArtikelDto> getFoundItems() {
		return foundItems;
	}

	@Override
	public void setFoundItems(List<ArtikelDto> foundItems) {
		this.foundItems = foundItems;
	}

	@Override
	public boolean isTotalMatch() {
		return totalMatch;
	}

	@Override
	public void setTotalMatch(boolean totalMatch) {
		sokoUpdate = !totalMatch;
		this.totalMatch = totalMatch;
	}

	@Override
	public Map<String, String> getValues() {
		return values;
	}

	@Override
	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	@Override
	public Integer getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public void setSelectedIndex(Integer selectedIndex) {
		if(selectedIndex == null) selectedIndex = -1;
		if(selectedIndex < foundItems.size())
			this.selectedIndex = selectedIndex;
		else
			throw new IndexOutOfBoundsException();
	}
	
	@Override
	public ArtikelDto getSelectedArtikelDto() {
		if(getSelectedIndex() == null || getSelectedIndex() == -1) return null;
		return getFoundItems().get(getSelectedIndex());
	}

	@Override
	public void setSokoUpdate(boolean sokoUpdate) {
		this.sokoUpdate = sokoUpdate;
	}

	@Override
	public boolean getSokoUpdate() {
		return sokoUpdate;
	}

	@Override
	public void setFoundTooManyArticles(boolean foundTooManyArticles) {
		this.foundTooManyArticles = foundTooManyArticles;		
	}

	@Override
	public boolean foundTooManyArticles() {
		return foundTooManyArticles;
	}
}
