package com.lp.server.stueckliste.ejb;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class StuecklistearbeitsplanQuery {
	public static final String ByStuecklisteIIdIArbeitsgangnummer = "StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer";
	public static final String ByStuecklisteIId = "StuecklistearbeitsplanfindByStuecklisteIId";
	public static final String ByStuecklistepositionIId = "StuecklistearbeitsplanfindByStuecklistepositionIId";
	public static final String ByArtikelIId = "StuecklistearbeitsplanfindByArtikelIId";
	public static final String ByMaschineIId = "StuecklistearbeitsplanfindByMaschineIId";

	public static HvTypedQuery<Stuecklistearbeitsplan> byStuecklisteIIdArbeitsgangnummer(EntityManager em,
			Integer stuecklisteIId, Integer arbeitsgang) {
		HvTypedQuery<Stuecklistearbeitsplan> query = new HvTypedQuery<>(
				em.createNamedQuery(ByStuecklisteIIdIArbeitsgangnummer));
		query.setParameter(1, stuecklisteIId);
		query.setParameter(2, arbeitsgang);
		return query;
	}

	public static HvTypedQuery<Stuecklistearbeitsplan> byStuecklisteIId(EntityManager em, Integer stuecklisteIId) {
		HvTypedQuery<Stuecklistearbeitsplan> query = new HvTypedQuery<>(em.createNamedQuery(ByStuecklisteIId));
		query.setParameter(1, stuecklisteIId);
		return query;
	}

	public static HvTypedQuery<Stuecklistearbeitsplan> byStuecklistepositionIId(EntityManager em,
			Integer stuecklistepositionIId) {
		HvTypedQuery<Stuecklistearbeitsplan> query = new HvTypedQuery<>(em.createNamedQuery(ByStuecklistepositionIId));
		query.setParameter(1, stuecklistepositionIId);
		return query;
	}

	public static HvTypedQuery<Stuecklistearbeitsplan> byArtikelIId(EntityManager em, Integer artikelIId) {
		HvTypedQuery<Stuecklistearbeitsplan> query = new HvTypedQuery<>(em.createNamedQuery(ByArtikelIId));
		query.setParameter(1, artikelIId);
		return query;
	}

	public static HvTypedQuery<Stuecklistearbeitsplan> byMaschineIId(EntityManager em, Integer maschineIId) {
		HvTypedQuery<Stuecklistearbeitsplan> query = new HvTypedQuery<>(em.createNamedQuery(ByMaschineIId));
		query.setParameter(1, maschineIId);
		return query;
	}

	public static List<Stuecklistearbeitsplan> listByStuecklisteIIdArbeitsgangnummer(EntityManager em,
			Integer stuecklisteIId, Integer arbeitsgang) {
		return byStuecklisteIIdArbeitsgangnummer(em, stuecklisteIId, arbeitsgang).getResultList();
	}

	public static List<Stuecklistearbeitsplan> listByStuecklisteIId(EntityManager em, Integer stuecklisteIId) {
		return byStuecklisteIId(em, stuecklisteIId).getResultList();
	}

	public static List<Stuecklistearbeitsplan> listByStuecklistepositionIId(EntityManager em,
			Integer stuecklistepositionIId) {
		return byStuecklistepositionIId(em, stuecklistepositionIId).getResultList();
	}

	public static List<Stuecklistearbeitsplan> listByArtikelIId(EntityManager em, Integer artikelIId) {
		return byArtikelIId(em, artikelIId).getResultList();
	}

	public static List<Stuecklistearbeitsplan> listByMaschineIId(EntityManager em, Integer maschineIId) {
		return byMaschineIId(em, maschineIId).getResultList();
	}
}
