package com.lp.server.forecast.bl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ICallOffXlsImporterBeanService;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class CallOffXLSImporterMonthly extends CallOffXLSImporter {
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	private static final long serialVersionUID = -8067550287096213665L;
	private static int HEADER_DATE_COLUMN = 3;
	
	public CallOffXLSImporterMonthly(ICallOffXlsImporterBeanService beanService) {
		super(beanService, new ICallOffXLSTransformer() {			
			@Override
			public XlsForecastPosition getPosition(Cell[] cells) throws EJBExceptionLP {
				return null;
			}
			
			@Override
			public XlsForecastOffset getOffset(Cell[] cells) throws EJBExceptionLP {
				return null;
			}
			
			@Override
			public String getForecastartCnr() {
				return ForecastFac.FORECASTART_FORECASTAUFTRAG;
			}
			
			@Override
			public int getDaysOfOffset() {
				return 0;
			}
			
			@Override
			public int getColumnMenge() {
				return 0;
			}
			
			@Override
			public int getColumnLieferdatum() {
				return 0;
			}
			
			@Override
			public int getColumnBestellnummer() {
				return 0;
			}
			
			@Override
			public int getColumnArtikelnummer() {
				return 0;
			}
		});
	}
	
	@Override
	protected void importXLSDatenImpl(Sheet sheet, Integer headerRow) {
		int lineNumber = headerRow - 1;
		
		List<Date> dates = getForecastDates(sheet, lineNumber);
		if(dates == null || dates.size() == 0) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(headerRow.toString());
			clientInfo.add(new Integer(sheet.getRows()).toString());
			getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ANZAHL_FELDER, 
							clientInfo, null)));			
		}


		while(++lineNumber < sheet.getRows()) {
			Cell[] cells = sheet.getRow(lineNumber);
			createPositions(cells, lineNumber, dates);
		}
		
		setStatsTotalImports();
	}
	
	private void createPositions(Cell[] cells, int lineNumber, List<Date> dates) {
		if(cells.length == 0 || isEmpty(cells[0])) return;
		String itemCnr = getItemNumber(cells, lineNumber);
		XlsForecastPosition pos = createPosition(itemCnr, lineNumber) ;
		
		for(int i = 0; i < dates.size() && (i + HEADER_DATE_COLUMN < cells.length); i++) {
			BigDecimal amount = getBigDecimal(cells[HEADER_DATE_COLUMN + i]);
			if(amount != null) {
				XlsForecastOffset off = new XlsForecastOffset();
				off.setDatum(dates.get(i));
				off.setMenge(amount);
				pos.getOffsets().add(off);
			}
		}
		
		if(pos.getOffsets().size() > 0) {
			getImportPositions().add(pos);
		}
	}
	
	private XlsForecastPosition createPosition(String itemCnr, int lineNumber) {
		XlsForecastPosition pos = new XlsForecastPosition();
		pos.setArtikelnummer(itemCnr);
		setupItem(pos, lineNumber);	
		return pos;
	}
	
	private void setupItem(XlsForecastPosition pos, int lineNumber) {
		List<ArtikelDto> artikelDtos = getBeanService().artikelFindByXlsArtikelnummer(pos.getArtikelnummer());
		if (artikelDtos == null || artikelDtos.isEmpty()) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(pos.getArtikelnummer());
			getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ARTIKEL_NICHT_GEFUNDEN, 
							clientInfo, null)));
			return;
		}
		
		if (artikelDtos.size() > 1) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(pos.getArtikelnummer());
			getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR,
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN, 
							clientInfo, null)));
		}

		pos.setArtikelIId(artikelDtos.get(0).getIId());		
	}
	
	private BigDecimal getBigDecimal(Cell cell) {
		if (cell == null || Helper.isStringEmpty(cell.getContents())) return null;
		try {
			BigDecimal bd = new BigDecimal(cell.getContents());
			return bd;
		} catch (NumberFormatException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGE_ZAHL, ex);
		}
	}
	
	/**
	 * Es ist bereits sichergestellt, dass wir bei einem Monthly Import sind.
	 * 
	 * @param sheet
	 * @param headerRow
	 * @return eine Liste von Forecast-Datum
	 */
	private List<Date> getForecastDates(Sheet sheet, int headerRow0) {
		List<Date> dates = new ArrayList<Date>();
		Cell[] cells = sheet.getRow(headerRow0);
		for(int i = HEADER_DATE_COLUMN; i < cells.length && !isEmpty(cells[i]); i++) {
			Date d = getDate(cells[i]);
			if(d == null) {
				myLogger.warn("Unexpected content '" + cells[i].getContents() + "' in cell " + i + " (should be a date).");
			} else {
				dates.add(d);
			}			
		}

		return dates;
	}
	
	private boolean isEmpty(Cell cell) {
		return Helper.isStringEmpty(cell.getContents());
	}
		
	private Date getDate(Cell cell) {
		CellType ct = cell.getType();
		if(CellType.DATE.equals(ct) || CellType.DATE_FORMULA.equals(ct)) {
			return new Date(((DateCell)cell).getDate().getTime());
		}
		String s = cell.getContents();
		if(s.startsWith("'")) {
			s = s.substring(1);
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			return new Date(sdf.parse(s).getTime());
		} catch(ParseException e) {
			myLogger.info("Kann '" + s + "' nicht in ein Datum wandeln");
		}

		return null;
	}
	
	private String getItemNumber(Cell[] cells, int row) {
		if(cells.length < 1) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(row);
			getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ANZAHL_FELDER, 
							clientInfo, null)));
			return null;
		}
		
		String s = cells[0].getContents();
		return s != null ? s.replaceAll("\\s+","") : s;		
	}
}
