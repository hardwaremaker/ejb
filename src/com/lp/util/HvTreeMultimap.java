/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2020 HELIUM V IT-Solutions GmbH
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
package com.lp.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.iterators.PeekingIterator;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

/**
 * A class similar to Google Guavas TreeMultimap, but the Values are not in a
 * sorted set, but instead stored in a List, so they preserve order of
 * insertion.
 * 
 * @author Alexander Daum
 *
 * @param <K> Key type, must either be comparable, or a comparator has to be
 *            provided
 * @param <V> Value type
 */
public class HvTreeMultimap<K, V> implements Multimap<K, V> {
	private TreeMap<K, Collection<V>> backingMap;

	private HvTreeMultimap(Comparator<? super K> comparator) {
		backingMap = new TreeMap<>(comparator);
	}

	private HvTreeMultimap() {
		backingMap = new TreeMap<>();
	}

	public static <K extends Comparable<K>, V> HvTreeMultimap<K, V> create() {
		return new HvTreeMultimap<K, V>();
	}

	public static <K, V> HvTreeMultimap<K, V> create(Comparator<? super K> comparator) {
		return new HvTreeMultimap<K, V>(comparator);
	}

	/**
	 * Add a Mapping to a key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(K key, V value) {
		Collection<V> values = backingMap.getOrDefault(key, new ArrayList<>());
		values.add(value);
		backingMap.put(key, values);
		return true;
	}

	/**
	 * Get all mappings to a specified key. If there are no mappings an empty
	 * collection is returned
	 */
	public Collection<V> get(K key) {
		return backingMap.getOrDefault(key, Collections.emptyList());
	}

	/**
	 * Remove a value from a key. If the value occurs multiple times for the same
	 * key, only the first occurance is removed
	 * 
	 * @param key
	 * @param value
	 * 
	 * @return if an element was removed
	 */
	@Override
	public boolean remove(Object key, Object value) {
		Collection<V> list = backingMap.get(key);
		if (list == null) {
			return false;
		}
		boolean removed = list.remove(value);
		if (list.isEmpty()) {
			backingMap.remove(key);
		}
		return removed;
	}

	/**
	 * Remove all mappings to the key.
	 * 
	 * @param key
	 * @return The previous mapping, or <code>null</code> if there was no mapping to
	 *         this key
	 */
	@Override
	public Collection<V> removeAll(Object key) {
		return backingMap.remove(key);
	}

	public int size() {
		return backingMap.values().stream().mapToInt(l -> l.size()).sum();
	}

	public Set<K> keySet() {
		return backingMap.keySet();
	}

	@Override
	public Map<K, Collection<V>> asMap() {
		return (TreeMap<K, Collection<V>>) backingMap;
	}

	@Override
	public void clear() {
		backingMap.clear();
	}

	@Override
	public boolean containsEntry(Object key, Object value) {
		return backingMap.getOrDefault(key, Collections.emptyList()).contains(value);
	}

	@Override
	public boolean containsKey(Object key) {
		return backingMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for (Collection<V> collection : backingMap.values())
			if (collection.contains(value))
				return true;
		return false;
	}

	@Override
	public Collection<Entry<K, V>> entries() {
		List<Entry<K, V>> entries = new ArrayList<>();
		for (Entry<K, Collection<V>> en : backingMap.entrySet()) {
			for (V v : en.getValue()) {
				entries.add(new AbstractMap.SimpleEntry<>(en.getKey(), v));
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return backingMap.isEmpty();
	}

	@Override
	public Multiset<K> keys() {
		Multiset<K> keys = TreeMultiset.create(backingMap.comparator());
		for (Entry<K, Collection<V>> en : backingMap.entrySet()) {
			keys.add(en.getKey(), en.getValue().size());
		}
		return keys;
	}

	@Override
	public boolean putAll(Multimap<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> entry : map.entries()) {
			put(entry.getKey(), entry.getValue());
		}
		return !map.isEmpty();
	}

	@Override
	public boolean putAll(K key, Iterable<? extends V> vals) {
		boolean changed = false;
		for (V v : vals) {
			put(key, v);
			changed = true;
		}
		return changed;
	}

	@Override
	public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
		Collection<V> vals = backingMap.get(key);
		if (vals == null) {
			return Collections.emptyList();
		}
		List<V> removed = new ArrayList<>();
		for (V toRemove : values) {
			if (vals.remove(toRemove)) {
				removed.add(toRemove);
			}
		}
		backingMap.put(key, vals);
		return removed;
	}

	@Override
	public Collection<V> values() {
		return backingMap.values().stream().flatMap(v -> v.stream()).collect(Collectors.toList());
	}

	/**
	 * Apply the accumulator function to all values this key is mapped to and return
	 * the result. An empty Optional is returned, if there are no values for this
	 * key. <br>
	 * For more detail on reducing functions see
	 * {@link Stream#reduce(BinaryOperator)}
	 * 
	 * @param key
	 * @param accumulator
	 * @return
	 */
	public Optional<V> getReduce(K key, BinaryOperator<V> accumulator) {
		return get(key).stream().reduce(accumulator);
	}

	/**
	 * Reduce the map, so that each key is only mapped to a single value afterwards.
	 * All values are combined according to the accumulator function <br>
	 * For more detail on reducing functions see
	 * {@link Stream#reduce(BinaryOperator)}
	 * 
	 * @param accumulator
	 */
	public void reduceMap(BinaryOperator<V> accumulator) {
		TreeMap<K, Collection<V>> newMap = new TreeMap<>(backingMap.comparator());
		for (K key : keySet()) {
			List<V> newValList = new ArrayList<>();
			getReduce(key, accumulator).ifPresent(newValList::add);
			newMap.put(key, newValList);
		}
		backingMap = newMap;
	}

	private <T> void remapImpl(HvTreeMultimap<T, V> newMap, BiFunction<? super K, ? super V, T> keyModifier) {
		for (Entry<K, Collection<V>> entry : backingMap.entrySet()) {
			for (V val : entry.getValue()) {
				K key = entry.getKey();
				newMap.put(keyModifier.apply(key, val), val);
			}
		}
	}
	
	/**
	 * Create a new Map, with the same values as this map, but compute a new key for
	 * each (key, value) pair
	 * 
	 * @param keyModifier a function that computes the new keys, (key, value) ->
	 *                    newKey
	 * @return The new Map
	 */
	public <T extends Comparable<T>> HvTreeMultimap<T, V> remapKey(BiFunction<? super K, ? super V, T> keyModifier) {
		HvTreeMultimap<T, V> newMap = HvTreeMultimap.create();
		remapImpl(newMap, keyModifier);
		return newMap;
	}

	/**
	 * Create a new Map, with the same values as this map, but compute a new key for
	 * each (key, value) pair
	 * 
	 * @param keyModifier a function that computes the new keys, (key, value) ->
	 *                    newKey
	 * @return The new Map
	 */
	public <T> HvTreeMultimap<T, V> remapKey(BiFunction<? super K, ? super V, T> keyModifier, Comparator<? super T> newComparator) {
		HvTreeMultimap<T, V> newMap = HvTreeMultimap.create(newComparator);
		remapImpl(newMap, keyModifier);
		return newMap;
	}
	/**
	 * Merge this map with another map to a new map. In the process the value type
	 * may change, but the key type has to be the same. <br>
	 * For defining how two collections mapped to the same key should change, the
	 * mergeFunction is invoked. If only one map has values associated to that key,
	 * the other collection is an empty collection <br>
	 * 
	 * In contrast to {@link Multimap#putAll(Multimap)} this function does not
	 * modify the map, but create a new map
	 * 
	 * @param <T>           The result type. In simple cases this will be V
	 * @param otherMap      The map to be merged. It has to be sorted by the same
	 *                      comparator, otherwise an
	 *                      {@link IllegalArgumentException} will be thrown
	 * @param mergeFunction
	 * @return
	 */
	public <T> HvTreeMultimap<K, T> merge(HvTreeMultimap<K, V> otherMap,
			BiFunction<Collection<V>, Collection<V>, Collection<T>> mergeFunction) {
		if (!Objects.equals(comparator(), otherMap.comparator())) {
			throw new IllegalArgumentException(
					"merge requires that both maps use the same comparator! Maybe your comparator needs an equals method");
		}

		HvTreeMultimap<K, T> newMap = HvTreeMultimap.create(comparator());

		PeekingIterator<K> keyIter1 = new PeekingIterator<>(keySet().iterator());
		PeekingIterator<K> keyIter2 = new PeekingIterator<>(otherMap.keySet().iterator());

		while (keyIter1.hasNext()) {
			Collection<V> vals1 = Collections.emptyList();
			Collection<V> vals2 = Collections.emptyList();
			K key = null;
			if (keyIter2.hasNext()) {
				// no iterator exhausted, get lower key
				int compare = compare(keyIter1.peek(), keyIter2.peek());
				if (compare <= 0) {
					key = keyIter1.next();
					vals1 = get(key);
				}
				if (compare >= 0) {
					key = keyIter2.next();
					vals2 = otherMap.get(key);
				}
			} else {
				key = keyIter1.next();
				vals1 = get(key);
			}
			Collection<T> newVals = mergeFunction.apply(vals1, vals2);
			newMap.putAll(key, newVals);
		}
		// all own values have been added, maybe othermap has some more
		while (keyIter2.hasNext()) {
			K key = keyIter2.next();
			Collection<T> newVals = mergeFunction.apply(Collections.emptyList(), otherMap.get(key));
			newMap.putAll(key, newVals);
		}
		return newMap;
	}

	/**
	 * Merge function that has the same effect as putAll, but the original maps will not be modified
	 * @param <V>
	 * @return
	 */
	public static <V> BiFunction<Collection<V>, Collection<V>, Collection<V>> mergeFunctionAddElements() {
		return (col1, col2) -> {
			List<V> vals = new ArrayList<>();
			vals.addAll(col1);
			vals.addAll(col2);
			return vals;
		};
	}

	/**
	 * Merge function for pairing elements. Elements will be put into new
	 * ImmutablePairs, when only one map has elements present, the corresponding
	 * value in the pair will be null
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> BiFunction<Collection<V>, Collection<V>, Collection<ImmutablePair<V>>> mergeFunctionPairUp() {
		return (col1, col2) -> {
			List<ImmutablePair<V>> vals = new ArrayList<>();
			Iterator<V> it1 = col1.iterator();
			Iterator<V> it2 = col2.iterator();
			while (it1.hasNext()) {
				if (it2.hasNext()) {
					vals.add(new ImmutablePair<V>(it1.next(), it2.next()));
				} else {
					vals.add(new ImmutablePair<V>(it1.next(), null));
				}
			}
			while (it2.hasNext()) {
				vals.add(new ImmutablePair<V>(null, it2.next()));
			}
			return vals;
		};
	}

    /**
     * Returns the comparator used to order the keys in this map, or
     * {@code null} if this map uses the {@linkplain Comparable
     * natural ordering} of its keys.
     *
     * @return the comparator used to order the keys in this map,
     *         or {@code null} if this map uses the natural ordering
     *         of its keys
     */
	public Comparator<? super K> comparator() {
		return backingMap.comparator();
	}
	
	@SuppressWarnings("unchecked")
	private int compare(K key1, K key2) {
		if(comparator() != null) {
			return comparator().compare(key1, key2);
		}
		else if(key1 instanceof Comparable && key2 instanceof Comparable) {
			return ((Comparable<K>) key1).compareTo(key2);
		} else {
			throw new IllegalStateException("No comparator and not comparable keys");
		}
	}

}
