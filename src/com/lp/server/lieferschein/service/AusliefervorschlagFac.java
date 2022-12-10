package com.lp.server.lieferschein.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AusliefervorschlagFac {

	public final static String REPORT_MODUL = "lieferschein";

	public static final String REPORT_AUSLIEFERVORSCHLAG = "ls_ausliefervorschlag.jasper";
	
	public static final String LOCKME_AUSLIEFERVORSCHLAG_TP = "lockme_ausliefervorschlag_tp";
	

	
	public static final int SORT_AUSLIEFERVORSCHLAG_ARTIKEL = 0;
	public static final int SORT_AUSLIEFERVORSCHLAG_KUNDE_LIEFERADRESSE = 1;
	
	
	public static final String AUSLIEFERVORSCHLAGHANDLER_VERFUEGBARKEITEN = "VERFUEGBARKEITEN";
	public static final String AUSLIEFERVORSCHLAGHANDLER_FILTER_ALLE = "ALLE";
	public static final String AUSLIEFERVORSCHLAGHANDLER_FILTER_AUSREICHEND_VERFUEGBAR = "VERFUEGBAR";
	public static final String AUSLIEFERVORSCHLAGHANDLER_FILTER_NICHT_VERFUEGBAR = "NICHT_VERFUEGBAR";
	public static final String AUSLIEFERVORSCHLAGHANDLER_FILTER_TEILMENGE_VERFUEGBAR = "TEILMENGE_VERFUEGBAR";

	
	public Integer createAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto,
			TheClientDto theClientDto);

	public AusliefervorschlagDto ausliefervorschlagFindByPrimaryKey(Integer iId);

	public void removeAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto);

	public void updateAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto,
			TheClientDto theClientDto);

	public void erstelleAusliefervorschlag(java.sql.Date dAusliefertermin,
			TheClientDto theClientDto);

	public String ausliefervorschlagUeberleiten(Set<Integer> iids,
			Integer kundeIId, Integer kundeIIdLieferadresse,
			TheClientDto theClientDto);

	public JasperPrintLP printAusliefervorschlag(Integer kundeIId,
			Integer kundeIId_Lieferadresse, int iSortierung,TheClientDto theClientDto);
	
	public void verfuegbareMengeBerechnen(TheClientDto theClientDto);
	
	public Set<Integer> getKundenIIsEinesAusliefervorschlages(
			Set<Integer> iids, boolean bLieferadresse, TheClientDto theClientDto);
	public Timestamp umKundenlieferdauerVersetzen(TheClientDto theClientDto,
			java.sql.Date dEintrefftermin, int kundenlieferdauer);
	
	public Timestamp umKundenlieferdauerVersetzen(TheClientDto theClientDto,
			Integer tagesartIId_Feiertag, Integer tagesartIId_Halbtag,Integer tagesartIId_Betriebsurlaub,
			java.sql.Date dEintrefftermin, int kundenlieferdauer);
	
	public void removeLockDesAusliefervorschlagesWennIchIhnSperre(
			TheClientDto theClientDto);
	public void pruefeBearbeitenDesAusliefervorschlagsErlaubt(
			TheClientDto theClientDto) throws EJBExceptionLP;

}
