package com.lp.server.util.logger;

import java.math.BigDecimal;

import com.lp.server.artikel.ejbfac.LagerbewegungPreisaenderung;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.bl.ErKontierungsDifferenz;
import com.lp.server.finanz.bl.LogEventErKontierungsdifferenz;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.util.report.UstVerprobungRow;

public class LogEventProducer {
	public static LogEventDto<EingangsrechnungDto> create(EingangsrechnungDto erDto) {
		return new LogEventEingangsrechnung(erDto) ;
	}
	
	public static LogEventDto<KontoDto> create(KontoDto kontoDto) {
		return new LogEventKonto(kontoDto) ;
	}
	
	public static LogEventDto<ErKontierungsDifferenz> create(FibuexportDto[] exportDtos, BigDecimal differenz) {
		return new LogEventErKontierungsdifferenz(new ErKontierungsDifferenz(exportDtos, differenz)) ;
	}
	
	public static LogEventDto<UstVerprobungRow> create(UstVerprobungRow ustRow) {
		return new LogEventUstVerprobungRow(ustRow) ;
	}  
	
	public static LogEventDto<RechnungzahlungDto> create(RechnungzahlungDto rechnungZahlungDto) {
		return new LogEventRechnungZahlung(rechnungZahlungDto);
	}
	
	public static LogEventDto<LagerbewegungPreisaenderung> create(
			LagerbewegungDto lagerbewegungDto, BigDecimal neuerPreis, Integer personalId) {
		return new LogEventLagerbewegungPreisaenderung(new LagerbewegungPreisaenderung(lagerbewegungDto, neuerPreis, personalId));
	}
	
	public static LogEventDto<LieferscheinDto> create(LieferscheinDto lieferscheinDto) {
		return new LogEventLieferschein(lieferscheinDto);
	}
	
	public static LogEventDto<BelegbuchungDto> create(BelegbuchungDto belegbuchungDto) {
		return new LogEventBelegbuchung(belegbuchungDto);
	}
}
