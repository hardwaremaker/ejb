package com.lp.server.system.ejbfac;

import java.sql.Timestamp;

import javax.persistence.EntityManager;

import com.lp.server.util.IVersionable;

public class Versionizer {
	
	private EntityManager em;
	
	public Versionizer(EntityManager em) {
		this.em = em;
	}

	public void incrementVersion(IVersionable entity) {
		entity.setTVersion(new Timestamp(System.currentTimeMillis()));
		if (entity.hasVersion()) {
			entity.setIVersion(entity.getIVersion() + 1);
		} else {
			entity.setIVersion(1);
		}
		em.merge(entity);
		em.flush();
	}

}
