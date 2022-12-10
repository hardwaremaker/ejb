package com.lp.server.forecast.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ICallOffXlsImporterBeanService;
import com.lp.server.partner.service.LiefermengenDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class CallOffXLSImporter implements Serializable {
	private static final long serialVersionUID = 7452365672529735549L;

//	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(CallOffXLSImporter.class);
	
	private ICallOffXlsImporterBeanService beanService;
	private ICallOffXLSTransformer coTransformer;
	private List<XlsForecastPosition> importPositions;
	private List<EJBLineNumberExceptionLP> importErrors;
	private CallOffXlsImportStats stats;
	private int lineNumber;
	
	protected class CallOfRangeDates implements Serializable {
		private static final long serialVersionUID = 1417987090319182982L;

		private Date minDate;
		private Date maxDate;
		public Date getMinDate() {
			return minDate;
		}
		public Date getMaxDate() {
			return maxDate;
		}
		public void setMinDate(Date minDate) {
			this.minDate = minDate;
		}
		public void setMaxDate(Date maxDate) {
			this.maxDate = maxDate;
		}
	}
	
	public CallOffXLSImporter(ICallOffXlsImporterBeanService beanService, ICallOffXLSTransformer coTransformer) {
		this.beanService = beanService;
		this.coTransformer = coTransformer;
		importPositions = new ArrayList<XlsForecastPosition>();
	}
	
	public List<EJBLineNumberExceptionLP> getImportErrors() {
		if (importErrors == null) {
			importErrors = new ArrayList<EJBLineNumberExceptionLP>();
		}
		return importErrors;
	}
	
	public ICallOffXlsImporterBeanService getBeanService() {
		return beanService;
	}
	
	public List<XlsForecastPosition> getImportPositions() {
		if (importPositions == null) {
			importPositions = new ArrayList<XlsForecastPosition>();
		}
		return importPositions;
	}
	
	public CallOffXlsImportStats getStats() {
		if (stats == null) {
			stats = new CallOffXlsImportStats();
		}
		return stats;
	}
	
	public CallOffXlsImporterResult checkXLSDaten(byte[] xlsDatei, final Integer startRow, Integer fclieferadresseIId) {
		try {
			final Sheet sheet = getSheet(xlsDatei);

			if (startRow.compareTo(sheet.getRows()) > 0) {
				getStats().incrementErrorCounts();
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add(startRow);
				clientInfo.add(sheet.getRows());
				getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ANZAHL_FELDER, 
								clientInfo, null)));
			}
			
			importErrors = null;
			checkForecastauftrag(fclieferadresseIId);
			importXLSDatenImpl(sheet, startRow);
		} catch (BiffException e) {
			getStats().incrementErrorCounts();
			getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, e)));
		} catch (IOException e) {
			getStats().incrementErrorCounts();
			getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR, e)));
		}

		return new CallOffXlsImporterResult(getImportErrors(), getStats());
	}
	
	private void checkForecastauftrag(Integer fclieferadresseIId) {
		if (getBeanService().hasAngelegtenForecastauftrag(fclieferadresseIId)) {
			getStats().incrementErrorCounts();
			getImportErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR,  
					new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_AUFTRAG_MIT_STATUS_ANGELEGT, "")));
		}
	}

	public CallOffXlsImporterResult importXLSDaten(byte[] xlsDatei, Integer startRow, Integer fclieferadresseIId) {
		CallOffXlsImporterResult result = checkXLSDaten(xlsDatei, startRow, fclieferadresseIId);
		result.getMessages().addAll(importCallOffs(fclieferadresseIId));
		
		return result;
	}
	
	private List<EJBLineNumberExceptionLP> importCallOffs(Integer fclieferadresseIId) {
		List<EJBLineNumberExceptionLP> errors = new ArrayList<EJBLineNumberExceptionLP>();
		
		try {
			FclieferadresseDto adrDto = getBeanService()
					.fclieferadresseFindByPrimaryKey(fclieferadresseIId);
			
			ForecastauftragDto fcAuftragDto = new ForecastauftragDto();
			fcAuftragDto.setFclieferadresseIId(fclieferadresseIId);
			fcAuftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
			
			Integer forecastauftragIId = getBeanService().createForecastauftrag(fcAuftragDto);
			
			for (XlsForecastPosition fcPosition : getImportPositions()) {
				if (fcPosition.getOffsets().isEmpty()) continue;
				
				for (XlsForecastOffset fcOffset : fcPosition.getOffsets()) {
					ForecastpositionDto fcPositionDto = new ForecastpositionDto();
					fcPositionDto.setForecastauftragIId(forecastauftragIId);
					fcPositionDto.setArtikelIId(fcPosition.getArtikelIId());
					fcPositionDto.setCBestellnummer(fcPosition.getBestellnummer());
					fcPositionDto.setForecastartCNr(coTransformer.getForecastartCnr());
					fcPositionDto.setNMenge(fcOffset.getMenge());
					fcPositionDto.setTTermin(new Timestamp(fcOffset.getDatum().getTime()));
					fcPositionDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
					
					getBeanService().createForecastposition(fcPositionDto);
				}

				if(fcPosition.hasCumulativeDate()) {
					LiefermengenDto lmDto = createLiefermengenDto(
							adrDto.getKundeIIdLieferadresse(), fcPosition);
					getBeanService().createLiefermengenUnique(lmDto); 
				}
			}
		} catch (EJBExceptionLP ex) {
			getStats().incrementErrorCounts();
			errors.add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, ex));
		}
		
		return errors;
	}

	private LiefermengenDto createLiefermengenDto(Integer kundenlieferadresseId, XlsForecastPosition position) {
		LiefermengenDto lmDto = new LiefermengenDto();
		lmDto.setArtikelIId(position.getArtikelIId());
		lmDto.setCLstext(position.getCumulativeReference());
		lmDto.setKundeIIdLieferadresse(kundenlieferadresseId);
		lmDto.setNMenge(position.getCumulativeQuantity());
		lmDto.setTDatum(new Timestamp(position.getCumulativeDate().getTime()));

		return lmDto;
	}
	
	protected void importXLSDatenImpl(Sheet sheet, Integer startRow) {
		IProcessingState state = new StateInitial();
		importPositions = null;
		lineNumber = startRow - 1;
		
		while (lineNumber < sheet.getRows()) {
			try {
				Cell[] row = sheet.getRow(lineNumber);
				state = state.process(row);
				continue;
			} catch (EJBExceptionLP e) {
				if (e instanceof EJBLineNumberExceptionLP) {
					getStats().incrementErrorCounts();
					EJBLineNumberExceptionLP le = (EJBLineNumberExceptionLP) e;
					le.setLinenumber(lineNumber);
					getImportErrors().add(le);
				} else {
					getStats().incrementErrorCounts();
					getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, e));
				}
			}
			
			state = new StateCheckArticle();
			lineNumber++;
		}
		setStatsTotalImports();
	}
	
	protected void setStatsTotalImports() {
		int sum = 0;
		for (XlsForecastPosition fcPosition : getImportPositions()) {
			sum = sum + fcPosition.getOffsets().size();
		}
		getStats().setTotalExports(sum);
	}

	private Sheet getSheet(byte[] xlsDatei) throws BiffException, IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");

		Workbook workbook = Workbook.getWorkbook(is, ws);

		return workbook.getSheet(0);
	}
	
	private void addXlsForecastPosition(XlsForecastPosition xlsPosition) {
		getImportPositions().add(xlsPosition);
	}
	
	private XlsForecastPosition getLastXlsForecastPosition() {
		if (getImportPositions().isEmpty()) {
			// error!!
			return null;
		}
		
		return getImportPositions().get(importPositions.size() - 1);
	}
	
	private void addXlsForecastOffset(XlsForecastOffset xlsOffset) {
		XlsForecastPosition xlsPosition = getLastXlsForecastPosition();
		xlsPosition.getOffsets().add(xlsOffset);
	}
	

	public interface IProcessingState extends Serializable{
		IProcessingState process(Cell[] cells) throws EJBExceptionLP;
	}
	
	public class StateInitial implements IProcessingState {
		private static final long serialVersionUID = 3283327376700536006L;

		public IProcessingState process(Cell[] cells) throws EJBExceptionLP {
			return new StateCheckArticle();
		}
	}
	
	public class StateCheckArticle implements IProcessingState {
		private static final long serialVersionUID = -4731156996974410636L;

		public IProcessingState process(Cell[] cells) throws EJBExceptionLP {
			XlsForecastPosition xlsPosition = coTransformer.getPosition(cells);
			if (xlsPosition == null || Helper.isStringEmpty(xlsPosition.getArtikelnummer())) {
				lineNumber++;
				return this;
			}

			if (Helper.isStringEmpty(xlsPosition.getBestellnummer())) {
				getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_WARNING, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_BESTELLNUMMER_LEER, "")));
			}
			
			addXlsForecastPosition(xlsPosition);
			return new StateFetchArticle();
		}
	}
	
	public class StateFetchArticle implements IProcessingState {
		private static final long serialVersionUID = -3595627779749875045L;

		@Override
		public IProcessingState process(Cell[] cells) throws EJBExceptionLP {
			final XlsForecastPosition xlsPosition = getLastXlsForecastPosition();
			List<ArtikelDto> artikelDtos = getBeanService().artikelFindByXlsArtikelnummer(xlsPosition.getArtikelnummer());
			if (artikelDtos == null || artikelDtos.isEmpty()) {
				getStats().incrementErrorCounts();
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add(xlsPosition.getArtikelnummer());
				getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_ARTIKEL_NICHT_GEFUNDEN, 
								clientInfo, null)));
				lineNumber++;
				return new StateCheckArticle();
			}
			
			if (artikelDtos.size() > 1) {
				getStats().incrementErrorCounts();
				List<Object> clientInfo = new ArrayList<Object>();
				clientInfo.add(xlsPosition.getArtikelnummer());
				getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR,
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN, 
								clientInfo, null)));
				lineNumber++;
				return new StateCheckArticle();
			}

			xlsPosition.setArtikelIId(artikelDtos.get(0).getIId());
			
			return new StateCheckCallOf();
		}
	}
	
	public class StateCheckCallOf implements IProcessingState {
		private static final long serialVersionUID = 7721607719562668411L;

		@Override
		public IProcessingState process(Cell[] cells) throws EJBExceptionLP {
			XlsForecastOffset xlsOffset = coTransformer.getOffset(cells);
			if (xlsOffset == null || xlsOffset.getDatum() == null && xlsOffset.getMenge() == null) {
				lineNumber++;
				return new StateCheckArticle();
			}
			
			if (xlsOffset.getDatum() == null) {
				getStats().incrementErrorCounts();
				getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR,  
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATUM_LEER, "")));
				lineNumber++;
				return new StateCheckArticle();
			}
			
			if (xlsOffset.getMenge() == null) {
				getStats().incrementErrorCounts();
				getImportErrors().add(new EJBLineNumberExceptionLP(lineNumber, EJBLineNumberExceptionLP.SEVERITY_ERROR,  
						new EJBExceptionLP(EJBExceptionLP.FEHLER_FORECAST_IMPORT_MENGE_LEER, "")));
				lineNumber++;
				return new StateCheckArticle();
			}
			
			lineNumber++;
			addXlsForecastOffset(xlsOffset);
			return new StateCheckCallOf();
		}
	}
}
