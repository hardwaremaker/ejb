package com.lp.server.forecast.ejbfac;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ICallOffXlsImporterBeanService;
import com.lp.server.partner.service.LiefermengenDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class CallOffXlsImporterBeanService implements ICallOffXlsImporterBeanService, Serializable {
	private static final long serialVersionUID = 1522318652218356924L;

	private ForecastFac forecastFac;
	private ArtikelFac artikelFac;
	private TheClientDto theClientDto;
	private PartnerServicesFac partnerServicesFac;
	
	public CallOffXlsImporterBeanService(TheClientDto theClientDto, ForecastFac forecastFac, ArtikelFac artikelFac, PartnerServicesFac partnerServicesFac) {
		this.artikelFac = artikelFac;
		this.forecastFac = forecastFac;
		this.theClientDto = theClientDto;
		this.partnerServicesFac = partnerServicesFac;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public List<ArtikelDto> artikelFindByXlsArtikelnummer(String xlsArtikelnummer) {
		return artikelFac.artikelFindByCKBezOhneExc(xlsArtikelnummer, getTheClientDto());
	}

	@Override
	public Integer createForecastauftrag(ForecastauftragDto fcAuftragDto) throws EJBExceptionLP {
		return forecastFac.createForecastauftrag(fcAuftragDto);
	}

	@Override
	public Integer createForecastposition(ForecastpositionDto fcPositionDto) throws EJBExceptionLP {
		return forecastFac.createForecastposition(fcPositionDto);
	}

	@Override
	public List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(
			Integer fclieferadresseIId, String statusCNr) {
		return forecastFac.forecastauftragFindByFclieferadresseIIdStatusCNr(fclieferadresseIId, statusCNr);
	}

	@Override
	public boolean hasAngelegtenForecastauftrag(Integer fclieferadresseIId) {
		List<ForecastauftragDto> dtos = forecastauftragFindByFclieferadresseIIdStatusCNr(
				fclieferadresseIId, LocaleFac.STATUS_ANGELEGT);
		return dtos != null && !dtos.isEmpty();
	}
	
	@Override
	public Integer createLiefermengen(LiefermengenDto dto) {
		return partnerServicesFac.createLiefermengen(dto);
	}
	
	@Override
	public FclieferadresseDto fclieferadresseFindByPrimaryKey(Integer iId) {
		return forecastFac.fclieferadresseFindByPrimaryKey(iId);
	}
	
	@Override
	public LiefermengenDto liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(
			Integer artikelIId, Integer kundeIIdLieferadresse, Timestamp tDatum) {
		return partnerServicesFac
				.liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(artikelIId, kundeIIdLieferadresse, tDatum);
	}
	
	@Override
	public Integer createLiefermengenUnique(LiefermengenDto dto) {
		return partnerServicesFac.createLiefermengenUnique(dto);
	}
	
}
