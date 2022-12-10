package com.lp.server.fertigung.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ZusatzstatusQuery {
	
	public static final String ByMandantCNrCBez = "ZusatzstatusfindByMandantCNrCBez";

	public static Zusatzstatus resultByMandantCNrCBezNoEx(EntityManager em, String bez, String mandant) {
		return HvTypedQuery.<Zusatzstatus>namedQuery(em, ByMandantCNrCBez, mandant, bez).getSingleResultNoEx();
	}
}
