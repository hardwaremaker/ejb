package com.lp.server.system.ejbfac;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HvCachingProvider<K,V> {

	private Map<K, V> cache = new HashMap<K, V>();

	public void put(K key,V value)  {
		cache.put(key, value);
	}

	public V getValueOfKey(K key) {
		return cache.get(key);
	}

	public void clear() {
		cache.clear();
	}

	public boolean containsKey(K key) {
		return cache.containsKey(key) ;
	}

	public int getSize(){
		return cache.size();
	}
	
	public Collection<V> values() {
		return cache.values() ;
	}
	
	public Set<Entry<K,V>> entrySet() {
		return cache.entrySet() ;
	}
	
	public Set<K> keySet() {
		return cache.keySet() ;
	}
}

