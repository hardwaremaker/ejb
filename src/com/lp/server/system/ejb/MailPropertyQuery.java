package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class MailPropertyQuery {
	
	public final static String AllByMandant = "MailPropertyfindAll";
	public final static String ByMandantCWertNotNull = "MailPropertyfindByCWertNotNull";
	
	public static List<MailProperty> listAllByMandant(EntityManager em, String mandantCNr) {
		return HvTypedQuery.<MailProperty>namedQuery(em, AllByMandant, mandantCNr).getResultList();
	}
	
	public static List<MailProperty> listByMandantCWertNotNull(EntityManager em, String mandantCNr) {
		return HvTypedQuery.<MailProperty>namedQuery(em, ByMandantCWertNotNull, mandantCNr).getResultList();
	}
}
