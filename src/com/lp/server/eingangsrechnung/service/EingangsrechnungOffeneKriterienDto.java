package com.lp.server.eingangsrechnung.service;

import java.sql.Date;

import com.lp.server.system.service.ReportJournalKriterienDto;

public class EingangsrechnungOffeneKriterienDto extends ReportJournalKriterienDto {
	private static final long serialVersionUID = 4769098656773874957L;

	/**
	 *  danach sortieren 
	 */
	public int sort = EingangsrechnungReportFac.REPORT_OFFENE_SORT_RECHNUNGSNUMMER;

	/**
	 * Stichtag wird mit aktuellem Tag vorbelegt.
	 * Es gibt keine M&ouml;glichkeit ohne Stichtag zu arbeiten
	 */
	public Date stichtag;
	
	/**
	 * false ... Belegdatum wird verwendet
	 * true  ... Freigabedatum wird verwendet
	 */
	public boolean stichtagIsFreigabedatum = true;

	/**
	 * Zugeordnete Belege sollten nur dann true sein, wenn nach Lieferanten sortiert wird
	 */
	public boolean mitNichtZugeordnetenBelegen = false;

	/**
	 * false ... "normale" Eingangsrechnungen verarbeiten
	 * true  ... die Zusatzkosten verarbeiten. Zusatzkosten ben&ouml;tigen 
	 *  eine Zusatzfunktionsberechtigung. Sie sollten nicht auf true gesetzt
	 *  werden, wenn es keine Zusatzkosten gibt
	 */
	public boolean zusatzkosten = false;
	
	/**
	 * Einschr&auml;nkung auf diesen Lieferanten
	 * 
	 * @param lieferantId auf diesen Lieferanten einschr&auml;ken
	 */
	public void setLieferantId(Integer lieferantId) {
		// auf den Lieferanten delegieren
		this.lieferantIId = lieferantId;
	}
}
