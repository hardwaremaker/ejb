package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;

import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.util.logger.LogEventPayload;

public class LagerbewegungPreisaenderung implements LogEventPayload {

	private LagerbewegungDto lagerbewegungDto;
	private BigDecimal neuerPreis;
	private Integer personalId;
	
	public LagerbewegungPreisaenderung(LagerbewegungDto lagerbewegungDto, BigDecimal neuerPreis, Integer personalId){
		this.lagerbewegungDto = lagerbewegungDto;
		this.neuerPreis = neuerPreis;
		this.personalId = personalId;
	}
	
	@Override
	public String asString() {
		StringBuffer s = new StringBuffer("Lagerbewegung Preisaenderung ["); 
		s.append("ArtikelId: ").append(lagerbewegungDto.getArtikelIId().toString());
		s.append(", Beleg: (")
			.append(lagerbewegungDto.getCBelegartnr())
			.append(" Id: ").append(nullable(lagerbewegungDto.getIBelegartid()))
			.append(" PosId: ").append(nullable(lagerbewegungDto.getIBelegartpositionid()))
			.append(")");
			
		s.append(", Eingetragener VK-Preis: ").append(lagerbewegungDto.getNVerkaufspreis().toPlainString());
		s.append(", Neuer VK-Preis: ").append(neuerPreis.toPlainString());
		s.append(", PersonalId: ").append(personalId.toString());
		s.append("]");
		return s.toString() ;
	}
	
	private String nullable(Integer id) {
		return id == null ? "null" : id.toString();
	}
}
