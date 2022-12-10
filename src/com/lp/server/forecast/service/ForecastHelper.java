package com.lp.server.forecast.service;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import com.lp.util.Helper;

public class ForecastHelper {

	private ForecastHelper() {
		// nothing here
	}

	public static String getBeschriftungInAbhaengigkeitDerForecastart( String forecastartCNr, java.util.Date dTermin, Locale locale) {

		Timestamp tHeute=Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
		if(dTermin.before(tHeute)){
			dTermin=tHeute;
		}
		
		String beschriftung = null;

		DateFormatSymbols symbols = new DateFormatSymbols(locale);

		String[] defaultMonths = symbols.getMonths();
		String[] defaultDays = symbols.getShortWeekdays();

		Calendar c = Calendar.getInstance();
		c.setTime(dTermin);


			if (forecastartCNr.equals(ForecastFac.FORECASTART_FORECASTAUFTRAG)) {

				
					return defaultMonths[c.get(Calendar.MONTH)] + " "+c.get(Calendar.YEAR);
				
			} else if (forecastartCNr
					.equals(ForecastFac.FORECASTART_CALL_OFF_TAG)) {
				
					return defaultDays[c.get(Calendar.DAY_OF_WEEK)] + " "
							+ Helper.formatDatum(c.getTime(), locale);

				

			} else if (forecastartCNr
					.equals(ForecastFac.FORECASTART_CALL_OFF_WOCHE)) {
				
					return "KW "+ c.get(Calendar.WEEK_OF_YEAR) + "";
				
			}

		

		return beschriftung;
	}

}
