package com.lp.server.util;

import java.util.Iterator;
import java.util.List;

public abstract class PositionNumberCachingAdapter extends PositionNumberAdapter {
	private Integer cachedHeadId;
//	private Iterator<?> positionIterator;
	private List<?> positionList;
	
	@Override
	public Iterator<?> getPositionsIteratorForHeadIId(Integer headIId) {
		if(headIId == null) return null;
		if(positionList == null || !headIId.equals(cachedHeadId)) {
			positionList = getPositionsListForHeadIIdImpl(headIId);
			cachedHeadId = headIId ;
		}
		
		return positionList.iterator();
//		if(positionIterator == null ||  !headIId.equals(cachedHeadId)) {
//			positionIterator = getPositionsIteratorForHeadIIdImpl(headIId) ;
//			cachedHeadId = headIId;
//		}
//		
//		return positionIterator ;
	}

	protected abstract Iterator<?> getPositionsIteratorForHeadIIdImpl(Integer headIIdNotNull);
	protected abstract List<?> getPositionsListForHeadIIdImpl(Integer headIIdNotNull);
}
