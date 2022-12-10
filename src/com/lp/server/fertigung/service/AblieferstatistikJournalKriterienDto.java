package com.lp.server.fertigung.service;

import com.lp.server.system.service.ReportJournalKriterienDto;

public class AblieferstatistikJournalKriterienDto extends ReportJournalKriterienDto{
	private static final long serialVersionUID = 3002707806180997940L;
	
	/* not null => diesen Artikel selektieren */
	public Integer artikelIId;
	
	/* danach sortieren */
	public int sort = FertigungReportFac.ABLIEFERSTATISTIK_OPTION_SORTIERUNG_ARTIKEL;
	
	public int optionArtikel = FertigungReportFac.ABLIEFERSTATISTIK_OPTION_ALLE_ARTIKEL;
	public String optionArtikelText;
	
	public boolean nurMaterialwerte;
	
}
