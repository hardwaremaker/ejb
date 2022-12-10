package com.lp.server.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PositionNumberCachingHandler extends PositionNumberHandler {
//	private Map<Integer, Iterator<?>> adapterCache = new HashMap<Integer, Iterator<?>>();
	private Map<Integer, List<?>> adapterCache = new HashMap<Integer, List<?>>();

	protected Iterator<?> getPositionsIteratorForHeadId(
			Integer headId, PositionNumberAdapter adapter) {
		List<?> cachedList = adapterCache.get(headId);
		if(cachedList == null) {
			System.out.println("Creating positionsIteratorForHead(" + headId + ").");
			cachedList = adapter.getPositionsListForHeadIId(headId);
			adapterCache.put(headId, cachedList);
		}

		return cachedList.iterator(); 
	}
}
