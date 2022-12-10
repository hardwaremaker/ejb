package com.lp.server.system.ejb;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ReportvarianteQuery {
	
	public static final String ByCReportnameCReportnamevariante = "ReportvarianteFindByCReportnameCReportnamevariante";

	public static HvTypedQuery<Reportvariante> byCReportnameCReportnamevariante(EntityManager em, String reportname, String reportnameVariante) {
		HvTypedQuery<Reportvariante> theQuery = new HvTypedQuery<Reportvariante>(em.createNamedQuery(ByCReportnameCReportnamevariante));
		theQuery.setParameter(1, reportname);
		theQuery.setParameter(2, reportnameVariante);
		return theQuery;
	}

	public static Reportvariante resultByCReportnameCReportnamevariante(EntityManager em, String reportname, String reportnameVariante) {
		return byCReportnameCReportnamevariante(em, reportname, reportnameVariante).getSingleResultNoEx();
	}
}
