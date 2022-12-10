package com.lp.server.forecast.ejbfac;

import java.io.Serializable;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ILinienabrufImporterBeanService;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class LinienabrufImporterBeanService implements ILinienabrufImporterBeanService, Serializable {
	private static final long serialVersionUID = -2861292294375241194L;

	private TheClientDto theClientDto;
	private ForecastFac forecastFac;
	private ArtikelFac artikelFac;
	
	public LinienabrufImporterBeanService(TheClientDto theClientDto,
			ForecastFac forecastFac, ArtikelFac artikelFac) {
		this.theClientDto = theClientDto;
		this.forecastFac = forecastFac;
		this.artikelFac = artikelFac;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public List<ArtikelDto> artikelFindByKundenArtikelnummer(String kundenartikelnummer) {
		return artikelFac.artikelFindByCKBezOhneExc(kundenartikelnummer, getTheClientDto());
	}

	@Override
	public ForecastauftragDto forecastauftragFindByPrimaryKey(Integer forecastauftragIId) {
		return forecastFac.forecastauftragFindByPrimaryKey(forecastauftragIId);
	}

	public List<ForecastpositionDto> forecastpositionFindByForecastauftragIId(Integer forecastauftragIId) {
		return forecastFac.forecastpositionFindByForecastauftragIId(forecastauftragIId);
	}

	@Override
	public Integer createLinienabruf(LinienabrufDto dto) {
		return forecastFac.createLinienabruf(dto);
	}
	
	@Override
	public List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(
			Integer fclieferadresseIId, String statusCNr) {
		return forecastFac.forecastauftragFindByFclieferadresseIIdStatusCNr(fclieferadresseIId, statusCNr);
	}
	
	@Override
	public Integer createForecastposition(ForecastpositionDto fcPositionDto)
			throws EJBExceptionLP {
		return forecastFac.createForecastposition(fcPositionDto);
	}
}
