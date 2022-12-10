package com.lp.service.edifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DistinctDelforItemIterator implements Iterator<DelforPosition>, Iterable<DelforPosition> {
	private List<DelforPosition> positions;
	private Iterator<DelforPosition> it;
	
	public DistinctDelforItemIterator(List<DelforPosition> positions) {
		this.positions = distinct(positions);
		it = this.positions.iterator();
	}
	
	private List<DelforPosition> distinct(List<DelforPosition> positions) {
		Map<String, DelforPosition> map = new HashMap<String, DelforPosition>();
		for(DelforPosition pos : positions) {
			String key = pos.distinctKey();
			if(map.get(key) == null) {
				map.put(key, pos);
			}
		}
		List<DelforPosition> distinctList = new ArrayList<DelforPosition>(map.values());
		return distinctList;
	}
	
	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public DelforPosition next() {
		return it.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Iterator<DelforPosition> iterator() {
		return this;
	}
}