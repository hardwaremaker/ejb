package com.lp.server.forecast.service;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public interface ILinienabrufImporterBeanService {

	TheClientDto getTheClientDto();
	
	List<ArtikelDto> artikelFindByKundenArtikelnummer(String kundenartikelnummer);

	ForecastauftragDto forecastauftragFindByPrimaryKey(Integer forecastauftragIId);
	
	List<ForecastpositionDto> forecastpositionFindByForecastauftragIId(Integer forecastauftragIId);

	Integer createLinienabruf(LinienabrufDto dto);
	
	List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(Integer forecastIId, 
			String statusCNr);

	Integer createForecastposition(ForecastpositionDto fcPositionDto) throws EJBExceptionLP;	
}
