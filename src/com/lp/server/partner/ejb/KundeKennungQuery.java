package com.lp.server.partner.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.system.service.KennungType;
import com.lp.server.util.KennungId;
import com.lp.server.util.KundeId;

public class KundeKennungQuery {
	public static final String ByKundeIdKennungIdValue = "KundeKennungKundeIdKennungIdValue";
	public static final String ByKundeIdKennungId      = "KundeKennungKundeIdKennungId";
	public static final String ByKundeIdKennungCnr     = "KundeKennungKundeIdKennungCnr";
	

	public static KundeKennung findByKundeIdKennungIdValue(
			EntityManager em, KundeId kundeId, KennungId kennungId, String value) {
		return new HvTypedQuery<KundeKennung>(
				em.createNamedQuery(ByKundeIdKennungIdValue))
				.setParameter("kundeid", kundeId.id())
				.setParameter("kennungid", kennungId.id())
				.setParameter("value", value)
				.getSingleResult();
	}
	
	public static List<KundeKennung> findByKundeIdKennungId(
			EntityManager em, KundeId kundeId, KennungId kennungId) {
		return new HvTypedQuery<KundeKennung>(
				em.createNamedQuery(ByKundeIdKennungId))
				.setParameter("kundeid", kundeId.id())
				.setParameter("kennungid", kennungId.id())
				.getResultList();
	}
	
	public static List<KundeKennung> findByKundeIdKennung(
			EntityManager em, KundeId kundeId, KennungType kennung) {
		return new HvTypedQuery<KundeKennung>(
				em.createNamedQuery(ByKundeIdKennungCnr))
				.setParameter("kundeid", kundeId.id())
				.setParameter("kennungcnr", kennung.getText())
				.getResultList();
	}
}
