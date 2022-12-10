package com.lp.server.personal.service;

import com.lp.server.system.service.ReportJournalKriterienDto;

public class ArbeitszeitstatistikJournalKriterienDto extends ReportJournalKriterienDto {
	private static final long serialVersionUID = 2920441494066389622L;

	public int sortierung = ZeiterfassungReportFac.REPORT_ARBEITSZEITSTATISTIK_OPTION_SORTIERUNG_ARTIKEL;
	public String belegartCnr;
	public Integer belegartIId;
	public String belegnummerFuerReport;
	public Integer artikelIId;
	public Integer artikelgruppeIId;
	public Integer artikelklasseIId;
	public Integer partnerIId;
	public boolean verdichtet;
	public boolean mitErledigtenProjekten;
	public boolean projektInterneErledigungBeruecksichtigen;
}
