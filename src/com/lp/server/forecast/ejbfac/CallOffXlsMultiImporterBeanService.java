package com.lp.server.forecast.ejbfac;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class CallOffXlsMultiImporterBeanService extends
		CallOffXlsImporterBeanService {
	private static final long serialVersionUID = 7833556110349067388L;
	private Integer myForecastAuftragId = null;
	private Integer myfclieferadresseId = null;
	
	public CallOffXlsMultiImporterBeanService(TheClientDto theClientDto,
			ForecastFac forecastFac, ArtikelFac artikelFac, PartnerServicesFac partnerServicesFac) {
		super(theClientDto, forecastFac, artikelFac, partnerServicesFac);
	}

	@Override
	public Integer createForecastauftrag(ForecastauftragDto fcAuftragDto)
			throws EJBExceptionLP {
		if(myForecastAuftragId == null) {
			myForecastAuftragId = super.createForecastauftrag(fcAuftragDto);
			myfclieferadresseId = fcAuftragDto.getFclieferadresseIId();
		}
		
		return myForecastAuftragId; 
	}
	
	@Override
	public boolean hasAngelegtenForecastauftrag(Integer fclieferadresseIId) {
		if(myfclieferadresseId == null || !myfclieferadresseId.equals(fclieferadresseIId)) {
			return super.hasAngelegtenForecastauftrag(fclieferadresseIId);
		}
		
		// Wir tun so, als haetten wir noch keinen forecastauftrag angelegt, 
		// weil wir den Forecastauftrag ja selbst erzeugt haben
		return false;
	}
}
