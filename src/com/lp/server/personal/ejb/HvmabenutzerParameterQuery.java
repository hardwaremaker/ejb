package com.lp.server.personal.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvmabenutzerId;

public class HvmabenutzerParameterQuery {
	public final static String ByCnrKategorie = "HvmabenutzerParameterfindByCnrKategorie";
	public final static String ByBenutzerIdMobil = "HvmabenutzerParameterfindByBenutzerId";
	
	public static HvTypedQuery<HvmabenutzerParameter> byCnrKategorie(
			EntityManager em, HvmabenutzerId benutzerId,
			String cnr, String kategorie) {
		return new HvTypedQuery<HvmabenutzerParameter>(
				em.createNamedQuery(ByCnrKategorie))
				.setParameter("benutzerId", benutzerId.id())
				.setParameter("cnr", cnr)
				.setParameter("kategorie", kategorie);
	}
	
	public static HvTypedQuery<HvmabenutzerParameter> byBenutzerId(EntityManager em, HvmabenutzerId benutzerId) {
		return new HvTypedQuery<HvmabenutzerParameter>(
				em.createNamedQuery(ByBenutzerIdMobil))
				.setParameter("benutzerId", benutzerId.id());
	}
	
	public static HvmabenutzerParameter findCnrKategorie(EntityManager em,
			HvmabenutzerId benutzerId, String cnr, String kategorie) {
		return byCnrKategorie(em, benutzerId, cnr, kategorie).getSingleResultNoEx();
	}
	
	public static List<HvmabenutzerParameter> listBenutzerId(EntityManager em, HvmabenutzerId benutzerId) {
		return byBenutzerId(em, benutzerId).getResultList();
	}
}
