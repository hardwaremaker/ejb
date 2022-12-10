package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.util.TauscheISort;

public class AutomatikjobsISort extends TauscheISort<Automatikjobs> {

	public AutomatikjobsISort(EntityManager em) {
		super(em, Automatikjobs.class);
	}

	@Override
	protected Automatikjobs findNextEntityISort(Automatikjobs startEntity) {
		return null;
	}

	@Override
	protected Automatikjobs findPreviousEntityISort(Automatikjobs startEntity) {
		return null;
	}

	@Override
	protected List<Automatikjobs> findAllEntitiesISort() {
		return null;
	}

	@Override
	protected Automatikjobs findLastEntityISort() {
		return null;
	}

}
