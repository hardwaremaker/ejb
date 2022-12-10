package com.lp.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Hilfklasse um schnellen Lookup von verwendeteten Seriennummern zu Artikel zu ermoeglichen
 * @author Alexander Daum
 *
 */
public class VerwendeteSeriennummerLookup {
	private Map<Integer, Set<String>> data = new HashMap<>();
	
	public void addSeriennummer(Integer artikelId, String snr) {
		data.computeIfAbsent(artikelId, i -> new HashSet<>());
		Set<String> zuArtikel = data.get(artikelId);
		zuArtikel.add(snr);
	}
	
	public boolean isSeriennummerVerwendet(Integer artikelId, String snr) {
		return data.getOrDefault(artikelId, Collections.emptySet()).contains(snr);
	}
	
	public boolean hasArtikel(Integer artikelId) {
		return data.containsKey(artikelId);
	}
}
