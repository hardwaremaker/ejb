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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.service.StklImportSpezifikation;

/**
 * Diese Klasse implementiert das {@link IStklImportResult}
 * Interface.
 * Sie dient dazu, mehrere <code>IStklImportResult</code>s
 * zusammenzufassen und diese zu gemeinsam zu verwalten,
 * ohne dass beim (lesenden) Zugriff darauf geachtet werden muss.
 * Die Methoden werden an alle verwalteten
 * <code>IStklImportResult</code>s weiter delegiert.<br>
 * Von den Elementen zur&uuml;ckgegebene Collections sind
 * immer unmodifizierbar.
 * @author robert
 * @see CondensedResultList
 */
public class CondensedResultListItem implements IStklImportResult {
	
	private List<IStklImportResult> list;
	
	/**
	 * Erzeugt ein {@link CondensedResultListItem}, welches zur 
	 * Zusammenfassung von mehreren <code>IStklImportResult</code>s dient.<br>
	 * Es wird <b>nicht</b> gepr&uuml;ft ob die Items in der Liste <code>list</code>
	 * die vorausgesetzte &Auml;hnlichkeit haben, wie mit 
	 * {@link CondensedResultList#matches(IStklImportResult, IStklImportResult) CondensedResultList.matches()} gepr&uuml;ft wird!
	 * @param list Liste mit {@link IStklImportResult}s, welche zusammengefasst werden sollen.
	 * @see CondensedResultList
	 */
	public CondensedResultListItem(List<IStklImportResult> list) {
		if(list == null) throw new NullPointerException("list == null");
		if(list.size() == 0) throw new IllegalArgumentException("list.size()==0");
		setList(new ArrayList<IStklImportResult>(list));
	}
	
	/**
	 * Ruft {@link #CondensedResultListItem(List)} und ist gleichbedeutend mit:<br>
	 * <code>new CondensedResultListItem(Arrays.asList(item));</code>
	 * @param item das erste Item in der Zusammenfassung
	 */
	public CondensedResultListItem(IStklImportResult item) {
		this(Arrays.asList(item));
	}
	
	protected IStklImportResult getFirstItem() {
		return getList().get(0);
	}
	
	@Override
	public List<ArtikelDto> getFoundItems() {
		return Collections.unmodifiableList(getFirstItem().getFoundItems());
	}
	
	@Override
	public void setFoundItems(List<ArtikelDto> foundItems) {
		for (IStklImportResult res : list) {
			res.setFoundItems(new ArrayList<ArtikelDto>(foundItems));
		}
	}

	@Override
	public boolean isTotalMatch() {
		return getFirstItem().isTotalMatch();
	}

	@Override
	public void setTotalMatch(boolean totalMatch) {
		for(IStklImportResult res : list) {
			res.setTotalMatch(totalMatch);
		}
	}
	
	protected String getMenge() {
		//TODO Eventuell cachen, falls zu langsam
		BigDecimal menge = BigDecimal.ZERO;
		for(IStklImportResult res : list) {
			String s = res.getValues().get(FertigungsStklImportSpezifikation.MENGE);
			if(s == null) {
				menge = menge.add(BigDecimal.ONE);
			} else {
				try {
					menge = menge.add(new BigDecimal(s));
				} catch (NumberFormatException e) {
					menge = menge.add(BigDecimal.ONE);
				}
			}
		}
		return menge.toString();
	}
	
	protected String getPositionen() {
		//TODO Eventuell cachen, falls zu langsam
		StringBuffer sb = new StringBuffer();
		for(IStklImportResult res : list) {
			String s = res.getValues().get(StklImportSpezifikation.POSITION);
			if(s != null) sb.append(s + "; ");
		}
		return sb.toString();
	}

	@Override
	public Map<String, String> getValues() {
		Map<String, String> map = new HashMap<String, String>(getFirstItem().getValues());
		map.put(StklImportSpezifikation.MENGE, getMenge());
//		map.put(StklImportSpezifikation.POSITION, getPositionen());
		return Collections.unmodifiableMap(map);
	}

	@Override
	public void setValues(Map<String, String> values) {
		for(IStklImportResult res : list) {
			String menge = res.getValues().get(StklImportSpezifikation.MENGE);
			res.setValues(new HashMap<String, String>(values));
			res.getValues().put(StklImportSpezifikation.MENGE, menge);
		}
	}

	@Override
	public Integer getSelectedIndex() {
		return getFirstItem().getSelectedIndex();
	}

	@Override
	public void setSelectedIndex(Integer selectedIndex) {
		if(selectedIndex == null) selectedIndex = -1;
		for(IStklImportResult res : list) {
			res.setSelectedIndex(selectedIndex);
		}
	}

	/**
	 * Gibt alle Items zur&uuml;ck, welche durch dieses {@link CondensedResultListItem}
	 * zusammengefasst werden.
	 * @return eine List mit {@link IStklImportResult}s
	 */
	public List<IStklImportResult> getList() {
		return list;
	}
	
	protected void setList(List<IStklImportResult> list) {
		this.list = list;
	}

	@Override
	public ArtikelDto getSelectedArtikelDto() {
		if(getSelectedIndex() == null || getSelectedIndex() == -1) return null;
		return getFoundItems().get(getSelectedIndex());
	}

	@Override
	public void setUpdateArtikelnummerMapping(boolean updateArtikelnummerMapping) {
		for(IStklImportResult res : list) {
			res.setUpdateArtikelnummerMapping(updateArtikelnummerMapping);
		}
	}

	@Override
	public boolean isUpdateArtikelnummerMapping() {
		return getFirstItem().isUpdateArtikelnummerMapping();
	}

	@Override
	public void setFoundTooManyArticles(boolean foundTooManyArticles) {
		for(IStklImportResult res : list) {
			res.setFoundTooManyArticles(foundTooManyArticles);
		}
	}

	@Override
	public boolean foundTooManyArticles() {
		return getFirstItem().foundTooManyArticles();
	}

	@Override
	public boolean uebernehmeLiefPreisInBestellung() {
		return getFirstItem().uebernehmeLiefPreisInBestellung();
	}

	@Override
	public void setUebernehmeLiefPreisInBestellung(boolean uebernehmen) {
		for(IStklImportResult res : list) {
			res.setUebernehmeLiefPreisInBestellung(uebernehmen);
		}
	}
	
}
