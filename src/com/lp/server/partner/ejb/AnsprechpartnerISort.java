package com.lp.server.partner.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.service.AnsprechpartnerISortValues;
import com.lp.server.util.TauscheISort;

public class AnsprechpartnerISort extends TauscheISort<Ansprechpartner> implements AnsprechpartnerISortValues {

	private Integer partnerId = null ;

	public AnsprechpartnerISort(EntityManager em) {
		super(em, Ansprechpartner.class) ;
	}

	public AnsprechpartnerISort(EntityManager em, Integer partnerId) {
		super(em, Ansprechpartner.class) ;
		this.partnerId = partnerId ;
	}


	@Override
	protected Ansprechpartner findNextEntityISort(Ansprechpartner startEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Ansprechpartner findPreviousEntityISort(Ansprechpartner startEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Ansprechpartner> findAllEntitiesISort() {
		HvTypedQuery<Ansprechpartner> query = new HvTypedQuery<Ansprechpartner>(
				getEm().createNamedQuery(Ansprechpartner.QueryFindByPartnerId)) ;
		query.setParameter("partnerId", partnerId);
		List<Ansprechpartner> ansprechpartner = query.getResultList() ;
		return ansprechpartner ;
	}

	@Override
	protected Ansprechpartner findLastEntityISort() {
		// TODO Auto-generated method stub
		return null;
	}

}
