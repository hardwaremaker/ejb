package com.lp.server.system.ejbfac;


public abstract class HvCreatingCachingProvider<K, V> extends HvCachingProvider<K, V> {
//	private int readRequests ;
//	private int writeRequests ;

	@Override
	public V getValueOfKey(K key) {
//		++readRequests ;

		K transformedKey = transformKey(key) ;
		if(containsKey(transformedKey)) {
			return super.getValueOfKey(transformedKey) ;
		}

		V value = provideValue(key, transformedKey) ;
		put(key, value);
		return value ;
	}

	@Override
	public void put(K key, V value) {
//		++writeRequests ;
		super.put(transformKey(key), value);
	}

	@Override
	public boolean containsKey(K key) {
		K transformedKey = transformKey(key) ;
		return super.containsKey(transformedKey);
	}

	protected K transformKey(K key) {
		return key ;
	}

	protected abstract V provideValue(K key, K transformedKey) ;
}
