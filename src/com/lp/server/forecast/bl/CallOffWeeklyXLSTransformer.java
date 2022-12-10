package com.lp.server.forecast.bl;

import java.sql.Date;

import jxl.Cell;

import com.lp.server.forecast.service.ForecastFac;

public class CallOffWeeklyXLSTransformer extends CallOffDailyXLSTransformer {
	private static final long serialVersionUID = -4657188446392107862L;

	private class Columnindex {
		static final int ORDER_NUMBER = 0;
		static final int ARTICLE_NUMBER = 1;
		static final int DELIVERY_DATE = 7;
		static final int QUANTITY = 8;
	}

	private String dateFormat = "w/yyyy";

	public CallOffWeeklyXLSTransformer() {
	}

	@Override
	public String getForecastartCnr() {
		return ForecastFac.FORECASTART_CALL_OFF_WOCHE;
	}
	
	@Override
	protected Date getDate(Cell[] cells, int column) {
		return getDate(cells, column, dateFormat);
	}
	
	@Override
	public int getColumnArtikelnummer() {
		return Columnindex.ARTICLE_NUMBER;
	}
	
	@Override
	public int getColumnBestellnummer() {
		return Columnindex.ORDER_NUMBER;
	}
	
	@Override
	public int getColumnLieferdatum() {
		return Columnindex.DELIVERY_DATE;
	}
	
	@Override
	public int getColumnMenge() {
		return Columnindex.QUANTITY;
	}
	
	@Override
	public int getDaysOfOffset() {
		return 7;
	}
	
	@Override
	protected void extractDeliveryFields(XlsForecastPosition position,
			Cell[] cells) {
		// Weekly Calloff hat keine Liefermengen
	}
}
