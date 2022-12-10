package com.lp.server.forecast.bl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ILinienabrufImporterBeanService;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class LinienabrufEluxDeTxtImporter implements Serializable {
	private static final long serialVersionUID = 6255660089227760392L;
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(LinienabrufEluxDeTxtImporter.class);
	
	protected class TransformedLinienabruf implements Serializable {
		private static final long serialVersionUID = -4239592649565396891L;

		private String linie;
		private Date produktionstermin;
		private String bestellnummer;
		private BigDecimal menge;
		private String artikelnummer;
		private Integer fcPositionIId;
		private String bereichNr;
		
		public String getLinie() {
			return linie;
		}
		public void setLinie(String linie) {
			this.linie = linie;
		}
		public Date getProduktionstermin() {
			return produktionstermin;
		}
		public void setProduktionstermin(Date produktionstermin) {
			this.produktionstermin = produktionstermin;
		}
		public String getBestellnummer() {
			return bestellnummer;
		}
		public void setBestellnummer(String bestellnummer) {
			this.bestellnummer = bestellnummer;
		}
		public BigDecimal getMenge() {
			return menge;
		}
		public void setMenge(BigDecimal menge) {
			this.menge = menge;
		}
		public String getArtikelnummer() {
			return artikelnummer;
		}
		public void setArtikelnummer(String artikelnummer) {
			this.artikelnummer = artikelnummer;
		}
		public Integer getFcPositionIId() {
			return fcPositionIId;
		}
		public void setFcPositionIId(Integer fcPositionIId) {
			this.fcPositionIId = fcPositionIId;
		}
		public String getBereichNr() {
			return bereichNr;
		}
		public void setBereichNr(String bereichNr) {
			this.bereichNr = bereichNr;
		}
	}

	private ILinienabrufImporterBeanService beanService;
	private List<EJBLineNumberExceptionLP> errors;
	private CallOffXlsImportStats stats;
	private List<TransformedLinienabruf> transformedLines;
	private Integer fclieferadresseIId;
	private ForecastauftragDto forecastauftragDto;
	private List<ForecastpositionDto> forecastpositonDtos;
	private Date fcPositionstermin;
	
	public LinienabrufEluxDeTxtImporter(ILinienabrufImporterBeanService beanService, 
			Integer fclieferadresseIId, Date deliveryDate) {
		this.beanService = beanService;
		setFclieferadresseIId(fclieferadresseIId);
		setFcPositionstermin(deliveryDate);
	}

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}
	
	public void setFclieferadresseIId(Integer fclieferadresseIId) {
		this.fclieferadresseIId = fclieferadresseIId;
	}
	
	public Date getFcPositionstermin() {
		return fcPositionstermin;
	}
	
	public void setFcPositionstermin(Date fcPositionstermin) {
		this.fcPositionstermin = fcPositionstermin;
	}
	
	protected List<EJBLineNumberExceptionLP> getErrors() {
		if (errors == null) {
			errors = new ArrayList<EJBLineNumberExceptionLP>();
		}
		return errors;
	}
	
	protected CallOffXlsImportStats getStats() {
		if (stats == null) {
			stats = new CallOffXlsImportStats();
		}
		return stats;
	}
	
	public List<TransformedLinienabruf> getTransformedLines() {
		if (transformedLines == null) {
			transformedLines = new ArrayList<TransformedLinienabruf>();
		}
		return transformedLines;
	}
	
	public void setForecastauftragDto(ForecastauftragDto forecastauftragDto) {
		this.forecastauftragDto = forecastauftragDto;
	}
	
	public ForecastauftragDto getForecastauftragDto() {
		return forecastauftragDto;
	}
	
	public void setForecastpositonDtos(
			List<ForecastpositionDto> forecastpositonDtos) {
		this.forecastpositonDtos = forecastpositonDtos;
	}
	
	public List<ForecastpositionDto> getForecastpositonDtos() {
		return forecastpositonDtos;
	}
	
	public CallOffXlsImporterResult checkContent(List<String> lines) {
		return checkContent(lines, true);
	}
	
	public CallOffXlsImporterResult importContent(List<String> lines) {
		CallOffXlsImporterResult result = checkContent(lines, false);
		if (hasErrors()) return result;
		
		importContent();
		return new CallOffXlsImporterResult(getErrors(), getStats());
	}
	
	protected CallOffXlsImporterResult checkContent(List<String> lines, boolean verifyMode) {
		try {
			if (lines.size() < 2) {
				getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ZUWENIG_ZEILEN, "less than two lines")));
				getStats().incrementErrorCounts();
				return new CallOffXlsImporterResult(getErrors(), getStats());
			}
			
			transform(lines);
			if (hasErrors()) return new CallOffXlsImporterResult(getErrors(), getStats());
			
			List<ForecastauftragDto> fcAuftragDtos = beanService.forecastauftragFindByFclieferadresseIIdStatusCNr(
					getFclieferadresseIId(), LocaleFac.STATUS_ANGELEGT);
			if(!checkForecastAuftrag(fcAuftragDtos, verifyMode)) {
				return new CallOffXlsImporterResult(getErrors(), getStats());
			}
			if(!verifyMode) {
				setForecastAuftragDtoImpl(fcAuftragDtos.get(0));				
			}
			checkLines(verifyMode);
//			if (!checkSetForecastauftrag()) return new CallOffXlsImporterResult(getErrors(), getStats());
	
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		return new CallOffXlsImporterResult(getErrors(), getStats());		
	}
	
	private void importContent() {
		for (TransformedLinienabruf linienabruf : getTransformedLines()) {
			LinienabrufDto dto = new LinienabrufDto();
			String bereichBez = "";
			if ("10".equals(linienabruf.getBereichNr())) {
				bereichBez = "Herde";
			} else if ("20".equals(linienabruf.getBereichNr())) {
				bereichBez = "Mulde";
			}
			dto.setCBereichBez(bereichBez);
			dto.setCBereichNr(linienabruf.getBereichNr());
			dto.setCBestellnummer(linienabruf.getBestellnummer());
			dto.setCLinie(linienabruf.getLinie());
			dto.setNMenge(linienabruf.getMenge());
			dto.setTProduktionstermin(new Timestamp(linienabruf.getProduktionstermin().getTime()));
			dto.setForecastpositionIId(linienabruf.getFcPositionIId());
			
			beanService.createLinienabruf(dto);
		}
	}

	private void checkLines(boolean verifyMode) {	
		for (int linenumber = 0; linenumber < getTransformedLines().size(); linenumber++) {
			try {
				TransformedLinienabruf linienabruf = getTransformedLines().get(linenumber);
				Integer artikelIId = checkAndGetArtikelnummer(linenumber+1, linienabruf.getArtikelnummer());
				if(!verifyMode) {
					linienabruf.setFcPositionIId(
							findForecastauftrag(artikelIId, linienabruf.getArtikelnummer()));					
				}			
			} catch (EJBLineNumberExceptionLP le) {
				le.setLinenumber(linenumber + 2);
				getErrors().add(le);
			}
		}
	}

	private Integer checkAndGetArtikelnummer(int linenumber, final String artikelnummer) {
		List<ArtikelDto> artikelDtos = beanService.artikelFindByKundenArtikelnummer(artikelnummer);
		if (artikelDtos == null || artikelDtos.isEmpty()) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(artikelnummer);
			throw new EJBLineNumberExceptionLP(linenumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_GEFUNDEN, 
							clientInfo, null));
		}
		
		if (artikelDtos.size() > 1) {
			getStats().incrementErrorCounts();
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(artikelnummer);
			throw new EJBLineNumberExceptionLP(linenumber, EJBLineNumberExceptionLP.SEVERITY_ERROR,
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN, 
							clientInfo, null));
		}
		
		if(Helper.short2boolean(artikelDtos.get(0).getBKommissionieren())) {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(artikelnummer);
			getErrors().add(new EJBLineNumberExceptionLP(linenumber, EJBLineNumberExceptionLP.SEVERITY_WARNING, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ARTIKEL_IM_KOMMISSIONIERSTATUS, clientInfo, null)));
			
			myLogger.warn("Artikel '" + artikelnummer + "' hat Linienabruf und gesetztes Kommissionierflag.");
		}
		return artikelDtos.get(0).getIId();
	}

	private Integer findForecastauftrag(Integer artikelIId, final String artikelnummer) {
		for (ForecastpositionDto dto : getForecastpositonDtos()) {
			if (artikelIId.equals(dto.getArtikelIId()) && 
					getFcPositionstermin().equals(Helper.extractDate(dto.getTTermin()))) {
				return dto.getIId();
			}
		}
		
//		myLogger.warn("Couldn't find artikelid '" + artikelIId.toString() 
//				+ "' cnr: '" + artikelnummer + "' and date '" + getFcPositionstermin() + "' in forecastpositions. Creating new one...");
		
		ForecastpositionDto posDto = new ForecastpositionDto();
		posDto.setArtikelIId(artikelIId);
		posDto.setNMenge(BigDecimal.ZERO);
		posDto.setTTermin(new Timestamp(getFcPositionstermin().getTime()));
		posDto.setForecastauftragIId(getForecastauftragDto().getIId());
		posDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		posDto.setForecastartCNr(ForecastFac.FORECASTART_NICHT_DEFINIERT);
		
//		getStats().incrementErrorCounts();
		List<Object> clientInfo = new ArrayList<Object>();
		clientInfo.add(artikelnummer);
		getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_WARNING,
				new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_IM_FC_AUFTRAG, 
						clientInfo, null)));
		
		Integer posId = beanService.createForecastposition(posDto);
		posDto.setIId(posId);
		getForecastpositonDtos().add(posDto);

		myLogger.info("Created foreceastposition with artikelid '" + artikelIId.toString() 
				+ "' cnr: '" + artikelnummer + "' and date '" + getFcPositionstermin() + "' in forecastpositions as id '" + posId.toString() + "'.");
		
		return posId;
	}

	
	private boolean checkSetForecastauftrag() {
		List<ForecastauftragDto> fcAuftragDtos = beanService.forecastauftragFindByFclieferadresseIIdStatusCNr(
				getFclieferadresseIId(), LocaleFac.STATUS_ANGELEGT);
		if (fcAuftragDtos == null || fcAuftragDtos.isEmpty()) {
			getStats().incrementErrorCounts();
			getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_KEINE_AUFTRAEGE_VORHANDEN, "")));
			return false;
		}
		
		if (fcAuftragDtos.size() > 1) {
			getStats().incrementErrorCounts();
			getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MEHRERE_AUFTRAEGE_MIT_STATUS_ANGELEGT, "")));
			return false;
		}

		setForecastAuftragDtoImpl(fcAuftragDtos.get(0));
		return true;
	}

	protected boolean checkForecastAuftrag(List<ForecastauftragDto> dtos, boolean verifyMode) {
		if (dtos == null || dtos.isEmpty()) {
			getStats().incrementErrorCounts();
			getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_KEINE_AUFTRAEGE_VORHANDEN, "")));
			return false;
		}
		
		if (dtos.size() > 1) {
			getStats().incrementErrorCounts();
			getErrors().add(new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MEHRERE_AUFTRAEGE_MIT_STATUS_ANGELEGT, "")));
			return false;
		}
		
		return true;
	}
	
	protected void setForecastAuftragDtoImpl(ForecastauftragDto fcAuftragDto) {
		setForecastauftragDto(fcAuftragDto);
		
		List<ForecastpositionDto> fcPositonDtos = beanService
				.forecastpositionFindByForecastauftragIId(getForecastauftragDto().getIId());
		setForecastpositonDtos(fcPositonDtos);
	}
	
	private boolean hasErrors() {
		for (EJBLineNumberExceptionLP le : getErrors()) {
			if (EJBLineNumberExceptionLP.SEVERITY_ERROR.equals(le.getSeverity()))
				return true;
		}
		return false;
	}

	private void transform(List<String> lines) {
		getTransformedLines().clear();
		
		for (int linenumber = 1; linenumber < lines.size(); linenumber++) {
			String line = lines.get(linenumber);
			if (line == null || line.length() < 100) {
				getErrors().add(new EJBLineNumberExceptionLP(linenumber, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
						new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ZEILENLAENGE_ZU_KLEIN, "line length too small")));
				getStats().incrementErrorCounts();
				continue;
			}
			
			try {
				TransformedLinienabruf linienabruf = new TransformedLinienabruf();
				linienabruf.setLinie(getContent(line, 0, 2));
				linienabruf.setProduktionstermin(getDate(getContent(line, 3, 9)));
				linienabruf.setBestellnummer(getContent(line, 73, 82));
				linienabruf.setBereichNr(getContent(line, 74, 75));
				linienabruf.setMenge(getBigDecimal(getContent(line, 83, 89)));
				linienabruf.setArtikelnummer(getContent(line, 91, 99));
				getTransformedLines().add(linienabruf);
				getStats().incrementTotalExports();
			} catch (EJBLineNumberExceptionLP le) {
				le.setLinenumber(linenumber);
				getErrors().add(le);
				getStats().incrementErrorCounts();
			}
		}
		
	}

	private BigDecimal getBigDecimal(final String numberString) {
		try {
			BigDecimal bd = new BigDecimal(numberString);
			return bd;
		} catch (NumberFormatException ex) {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(numberString);
			throw new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MENGE_NICHT_NUMERISCH, 
							clientInfo, ex));
		}
	}

	private Date getDate(String dateString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MMyy");
		try {
			java.util.Date date = (java.util.Date) dateFormatter.parse(dateString);
			return new Date(date.getTime());
		} catch (ParseException ex) {
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add("dd.MMyy");
			throw new EJBLineNumberExceptionLP(-1, EJBLineNumberExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_UNGUELTIGES_DATUM, 
							clientInfo, ex));
		}
	}

	private String getContent(String line, int from, int to) {
		return line.substring(from, to + 1);
	}
}
