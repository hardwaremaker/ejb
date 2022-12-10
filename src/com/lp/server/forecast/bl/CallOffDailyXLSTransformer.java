package com.lp.server.forecast.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;

import com.lp.server.forecast.service.ForecastFac;
import com.lp.util.EJBExceptionLP;

public class CallOffDailyXLSTransformer implements ICallOffXLSTransformer, Serializable {
	private static final long serialVersionUID = -572313035082067437L;

	private class Columnindex {
		static final int ORDER_NUMBER = 0;
		static final int ARTICLE_NUMBER = 1;
		static final int CALLOF_DAILY_DELIVERY_DATE = 8;
		static final int CALLOF_DAILY_QUANTITY = 9;
		static final int CUMULATIVE_DELIVERY_REFERENCE = 4;
		static final int CUMULATIVE_DELIVERY_DATE = 6;
		static final int CUMULATIVE_DELIVERY_QUANTITY = 7;		
	}
	
	private String dateFormat = "dd.MM.yyyy";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
	
	public CallOffDailyXLSTransformer() {
	}

	public XlsForecastPosition getPosition(Cell[] cells) throws EJBExceptionLP {
		try {
			checkCells(cells);
		} catch (EJBExceptionLP ex) {
			return null;
		}
		
		XlsForecastPosition position = new XlsForecastPosition();
		position.setArtikelnummer(getWithoutSpaces(cells, getColumnArtikelnummer()));
		position.setBestellnummer(getTrimmed(cells, getColumnBestellnummer()));
		
		extractDeliveryFields(position, cells);
		return position;
	}

	protected void extractDeliveryFields(XlsForecastPosition position, Cell[] cells) {
		String s = getTrimmed(cells, Columnindex.CUMULATIVE_DELIVERY_DATE);
		if(s == null || s.length() == 0) return;
		
		Date d = getDate(cells, Columnindex.CUMULATIVE_DELIVERY_DATE);
		BigDecimal q = getBigDecimal(cells, Columnindex.CUMULATIVE_DELIVERY_QUANTITY);
		if(d != null && q != null) {
			position.setCumulativeDate(d);
			position.setCumulativeQuantity(q);
			position.setCumulativeReference(
					getTrimmed(cells, Columnindex.CUMULATIVE_DELIVERY_REFERENCE));
		}
	}
	
	public XlsForecastOffset getOffset(Cell[] cells) throws EJBExceptionLP {
		try {
			checkCells(cells);
		} catch (EJBExceptionLP ex) {
			return null;
		}
		
		XlsForecastOffset offset = new XlsForecastOffset();
		offset.setDatum(getDate(cells, getColumnLieferdatum()));
		offset.setMenge(getBigDecimal(cells, getColumnMenge()));
		return offset;
	}

	protected void checkCells(Cell[] cells) {
		if (null == cells) 
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ANZAHL_FELDER, 
					new IllegalArgumentException("null"));
		if (cells.length < 9) 
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ANZAHL_FELDER, 
					new IllegalArgumentException(String.valueOf(cells.length)));
	}

	protected BigDecimal getBigDecimal(Cell[] cells, int column) {
		if (cells.length < column + 1) return null;
		
		Cell cell = cells[column];
		if (cell == null || cell.getContents() == null) return null;
		try {
			BigDecimal bd = new BigDecimal(cell.getContents());
			return bd;
		} catch (NumberFormatException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGE_ZAHL, ex);
		}
	}

	protected Date getDate(Cell[] cells, int column) {
		return getDate(cells, column, dateFormat);
	}

	protected Date getDate(Cell[] cells, int column, final String dateFormat) {
		final Cell cell = cells[column];
		if (cell == null || cell.getContents() == null) return null;
		
		try {
			if (CellType.DATE.equals(cell.getType())) {
				return new Date(((jxl.DateCell) cell).getDate().getTime());
			}
			dateFormatter.applyPattern(dateFormat);
			java.util.Date date = (java.util.Date) dateFormatter.parse(cell.getContents().trim());
			return new Date(date.getTime());
		} catch (ParseException ex) {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(cell.getContents());
			clientInfo.add(dateFormat);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGES_DATUM, 
					clientInfo, ex);
		}
	}
	
	protected String getTrimmed(Cell[] cells, int column) {
		String content = getString(cells, column);
		return content != null ? content.trim() : content;
	}

	private String getString(Cell[] cells, int column) {
		Cell cell = cells[column];
		return cell.getContents();
	}

	private String getWithoutSpaces(Cell[] cells, int column) {
		String content = getString(cells, column);
		return content != null ? content.replaceAll("\\s+","") : content;
	}

	@Override
	public String getForecastartCnr() {
		return ForecastFac.FORECASTART_CALL_OFF_TAG;
	}

	@Override
	public int getColumnBestellnummer() {
		return Columnindex.ORDER_NUMBER;
	}

	@Override
	public int getColumnArtikelnummer() {
		return Columnindex.ARTICLE_NUMBER;
	}

	@Override
	public int getColumnLieferdatum() {
		return Columnindex.CALLOF_DAILY_DELIVERY_DATE;
	}

	@Override
	public int getColumnMenge() {
		return Columnindex.CALLOF_DAILY_QUANTITY;
	}
	
	@Override
	public int getDaysOfOffset() {
		return 1;
	}
}
