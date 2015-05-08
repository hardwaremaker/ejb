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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.lp.service.StklImportSpezifikation;

public class CondensedResultList implements List<IStklImportResult>,
		Serializable {

	private static final long serialVersionUID = 6518825614737433906L;

	private List<CondensedResultListItem> heads;

	public CondensedResultList(List<IStklImportResult> uncondensedList) {
		heads = new ArrayList<CondensedResultListItem>();
		for (IStklImportResult stklImportResult : uncondensedList) {
			add(stklImportResult);
		}
	}

	protected CondensedResultListItem findMatchingHead(IStklImportResult result) {
		for (IStklImportResult head : heads) {
			if (matches(head, result)) {
				return (CondensedResultListItem) head;
			}
		}
		return null;
	}

	/**
	 * Vergleicht zwei {@link IStklImportResult}s miteinander. Es m&uuml;ssen
	 * &uuml;bereinstimmen: <li>die Values bis auf
	 * <code>{@link StklImportSpezifikation}.POSITION</code> und
	 * <code>{@link StklImportSpezifikation}.MENGE</code></li> <li>der
	 * ausgew&auml;hlte Artikel ({@link IStklImportResult#getSelectedIndex()})</li>
	 * <li>{@link IStklImportResult#isTotalMatch()}</li> <li>
	 * <code>{@link IStklImportResult#getFoundItems()}.size()</code></li>
	 * 
	 * @param res1
	 *            1. zu vergleichendes <code>StklImportResult</code>
	 * @param res2
	 *            2. zu vergleichendes <code>StklImportResult</code>
	 * @return <code>true</code>, wenn <code>res1</code> und <code>res2</code>
	 *         laut den oben genannten Regeln &uuml;bereinstimmen
	 */
	protected boolean matches(IStklImportResult res1, IStklImportResult res2) {

		if (!(res1.getSelectedIndex() == null ?
				res2.getSelectedIndex() == null :
					res1.getSelectedIndex().equals(res2.getSelectedIndex())))
			return false;
		if (res1.getFoundItems().size() != res2.getFoundItems().size())
			return false;
		if (res1.isTotalMatch() != res2.isTotalMatch())
			return false;

		Set<String> types1 = res1.getValues().keySet();
		Set<String> types2 = res2.getValues().keySet();

		if (types1.size() != types2.size())
			return false;
		if (!types1.containsAll(types2))
			return false;

		for (String type : types1) {
			if (StklImportSpezifikation.POSITION.equals(type))
				continue;
			if (StklImportSpezifikation.MENGE.equals(type))
				continue;
			String v1 = res1.getValues().get(type);
			String v2 = res2.getValues().get(type);
			if (!(v1 == null ? v2 == null : v1.equals(v2)))
				return false;
		}
		return true;
	}

	public boolean add(IStklImportResult e) {
		if (e instanceof CondensedResultListItem) {
			heads.add((CondensedResultListItem) e);
			return true;
		}
		CondensedResultListItem head = findMatchingHead(e);
		if (head == null) {
			head = new CondensedResultListItem(e);
			heads.add(head);
		} else {
			head.getList().add(e);
		}
		return true;
	}

	public List<IStklImportResult> convertToNormalList() {
		List<IStklImportResult> list = new ArrayList<IStklImportResult>();
		for (CondensedResultListItem head : heads) {
			list.addAll(head.getList());
		}
		return list;
	}

	@Override
	public void add(int index, IStklImportResult element) {
		throw new UnsupportedOperationException("use add(IStklImportResult e)");
	}

	@Override
	public boolean addAll(Collection<? extends IStklImportResult> c) {
		for (IStklImportResult res : c) {
			add(res);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends IStklImportResult> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		heads.clear();
	}

	@Override
	public boolean contains(Object o) {
		return heads.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return heads.containsAll(c);
	}

	@Override
	public IStklImportResult get(int index) {
		return heads.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return heads.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return heads.isEmpty();
	}

	@Override
	public Iterator<IStklImportResult> iterator() {
		return new ArrayList<IStklImportResult>(heads).iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return heads.lastIndexOf(o);
	}

	@Override
	public ListIterator<IStklImportResult> listIterator() {
		return new ArrayList<IStklImportResult>(heads).listIterator();
	}

	@Override
	public ListIterator<IStklImportResult> listIterator(int index) {
		return new ArrayList<IStklImportResult>(heads).listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return heads.remove(o);
	}

	@Override
	public IStklImportResult remove(int index) {
		return heads.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return heads.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return heads.retainAll(c);
	}

	@Override
	public IStklImportResult set(int index, IStklImportResult element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return heads.size();
	}

	@Override
	public List<IStklImportResult> subList(int fromIndex, int toIndex) {
		return new ArrayList<IStklImportResult>(heads.subList(fromIndex,
				toIndex));
	}

	@Override
	public Object[] toArray() {
		return heads.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return heads.toArray(a);
	}
}
