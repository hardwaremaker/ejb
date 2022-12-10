package com.lp.server.reklamation.service;

import com.lp.server.system.service.ReportJournalKriterienDto;

public class ReklamationFehlerartenJournalKriterienDto extends ReportJournalKriterienDto {
	private static final long serialVersionUID = 8069146610507581997L;

	public boolean mitKunde;
	public boolean mitLieferant;
	public boolean mitFertigung;
	public boolean nurBerechtigt;
	public int gruppierung;
}
