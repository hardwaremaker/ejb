package com.lp.server.forecast.service;

import java.sql.Timestamp;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.LiefermengenDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public interface ICallOffXlsImporterBeanService {

	TheClientDto getTheClientDto();
	
	List<ArtikelDto> artikelFindByXlsArtikelnummer(String xlsArtikelnummer);

	Integer createForecastauftrag(ForecastauftragDto fcAuftragDto) throws EJBExceptionLP;

	Integer createForecastposition(ForecastpositionDto fcPositionDto) throws EJBExceptionLP;
	
	List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(Integer forecastIId, 
			String statusCNr);
	
	boolean hasAngelegtenForecastauftrag(Integer forecastIId);
	
	FclieferadresseDto fclieferadresseFindByPrimaryKey(Integer iId);
	Integer createLiefermengen(LiefermengenDto dto);
	LiefermengenDto liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(
			Integer artikelIId, Integer kundeIIdLieferadresse, Timestamp tDatum);

	Integer createLiefermengenUnique(LiefermengenDto dto);
}
