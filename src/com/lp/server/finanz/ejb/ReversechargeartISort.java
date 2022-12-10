package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.util.TauscheISort;

public class ReversechargeartISort extends TauscheISort<Reversechargeart> {

	public ReversechargeartISort(EntityManager em) {
		super(em, Reversechargeart.class) ;
	}
	
	@Override
	protected Reversechargeart findNextEntityISort(Reversechargeart startEntity) {
		return null;
	}

	@Override
	protected Reversechargeart findPreviousEntityISort(
			Reversechargeart startEntity) {
		return null;
	}

	@Override
	protected List<Reversechargeart> findAllEntitiesISort() {
		return null;
	}

	@Override
	protected Reversechargeart findLastEntityISort() {
		return null;
	}

}
