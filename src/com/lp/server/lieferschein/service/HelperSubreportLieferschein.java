package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Transient;

import com.lp.server.lieferschein.ejbfac.LieferscheinReportFacBean;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.FacLookup;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

public class HelperSubreportLieferschein implements Serializable {	
	private static final long serialVersionUID = -9116360513282362442L;

	private Integer auftragId ;
	private Integer statusTeillieferung;
	private LieferscheinDto lieferscheinDto ;
	private TheClientDto theClientDto ;
	private HelperSubreportLieferscheinData subreportData ;
	
	@Transient 
	private transient LieferscheinReportFac lieferscheinReportFac ;
	
	public HelperSubreportLieferschein(Integer auftragId, Integer statusTeillieferung, LieferscheinDto lieferscheinDto, TheClientDto theClientDto) {
		this.auftragId = auftragId ;
		this.statusTeillieferung = statusTeillieferung;
		this.lieferscheinDto = lieferscheinDto;
		this.theClientDto = theClientDto;
	}
	
	private LieferscheinReportFac getLieferscheinReportFac()
			throws EJBExceptionLP {
		try {
			if (lieferscheinReportFac == null) {
				Context context = new InitialContext();
				
				lieferscheinReportFac = FacLookup.lookup(context,
						LieferscheinReportFacBean.class, LieferscheinReportFac.class);
//				lieferscheinReportFac = (LieferscheinReportFac) context
//						.lookup("lpserver/LieferscheinReportFacBean/remote");
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return lieferscheinReportFac;
	}
	
	private HelperSubreportLieferscheinData getSubreportData() throws NamingException, RemoteException {
		if(subreportData == null) {
			if(auftragId == null) {
				subreportData = new HelperSubreportLieferscheinData();
				subreportData.setStatusTeillieferung(statusTeillieferung);
			} else {
				subreportData = getLieferscheinReportFac().createSubreportOffeneABPositionen(
						auftragId, statusTeillieferung, lieferscheinDto, theClientDto); 
			}
		}
		return subreportData ;
	}
	
	public LPDatenSubreport getOffeneABPositionen() throws NamingException, RemoteException {
		return getSubreportData().getSubreportDaten();
	}
	
	public Integer getTeillieferungsStatus() throws NamingException, RemoteException {
		return getSubreportData().getStatusTeillieferung();
	}
}
