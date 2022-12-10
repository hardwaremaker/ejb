package com.lp.server.forecast.bl;

import java.sql.Date;
import java.util.List;

import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ILinienabrufImporterBeanService;

public class LinienabrufMultipleTxtImporter extends
		LinienabrufEluxDeTxtImporter {
	private static final long serialVersionUID = -6525457416773396138L;
	
	public LinienabrufMultipleTxtImporter(
			ILinienabrufImporterBeanService beanService,
			Integer fclieferadresseIId, Date deliveryDate) {
		super(beanService, fclieferadresseIId, deliveryDate);
	}
	
	@Override
	protected boolean checkForecastAuftrag(List<ForecastauftragDto> dtos, boolean verifyMode) {
		if(verifyMode) {
			/*
			 * Wir sind im Verify-Modus, das heisst, es wurden physisch
			 * noch keine Forecast-Auftrag erzeugt - obwohl vielleicht 
			 * Forecast-Importe solche Forecast-Auftraege erzeugt haetten.
			 */
			if(dtos == null || dtos.isEmpty()) return true;
			
			/*
			 * Es gibt jetzt schon mindestens schon einen Auftrag, das ist 
			 * ein Fehler, da wir im Rahmen des Multi-File-Imports auf jeden
			 * Fall einen neuen Forecast-Auftrag anlegen werden. 
			 */
			return false;
		} else {
			return super.checkForecastAuftrag(dtos, verifyMode);			
		}
	}
}
