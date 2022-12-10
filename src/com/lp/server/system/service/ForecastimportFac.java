package com.lp.server.system.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.EdiFileInfo;
import com.lp.server.forecast.service.ForecastImportFehlerDto;
import com.lp.server.forecast.service.ForecastImportZeissDto;

@Remote
public interface ForecastimportFac {
	public String pruefeUndImportiereForecastpositionXLS(byte[] xlsDatei, Integer forecastauftragIId,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto);

	CallOffXlsImporterResult importCallOffDailyXls(Integer forecastIId, byte[] xlsDatei, boolean checkOnly,
			Integer startRow, TheClientDto theClientDto);

	CallOffXlsImporterResult importCallOffWeeklyXls(Integer forecastIId, byte[] xlsDatei, boolean checkOnly,
			Integer startRow, TheClientDto theClientDto);

	CallOffXlsImporterResult importLinienabrufEluxDeTxt(Integer forecastIId, List<String> inputLines, boolean checkOnly,
			Date deliveryDate, TheClientDto theClientDto);

	CallOffXlsImporterResult importDelforEdifact(Integer forecastId, List<EdiFileInfo> ediContent, boolean checkOnly,
			TheClientDto theClientDto);

	CallOffXlsImporterResult importFiles(List<IForecastImportFile<?>> files, Integer fclieferadresseId,
			boolean checkOnly, TheClientDto theClientDto);

	public int importiereVMI_XLS(byte[] xlsDatei, TheClientDto theClientDto);

	public int importiereEpsilon_XLS(byte[] xlsDatei, Integer forecastauftragIId, TheClientDto theClientDto);

	public ForecastImportFehlerDto importiereRollierendePlanung_XLS(byte[] xlsDatei, Integer forecastauftragIId,
			boolean referenznummer, boolean bImportierenWennKeinFehler, TheClientDto theClientDto);

	public int importiereVMI_CC_XLS(byte[] xlsDatei, Integer forecastauftragIId, TheClientDto theClientDto);

	public String pruefeUndImportiereForecastpositionXLS_VAT(byte[] xlsDatei, Integer forecastauftragIId,
			boolean bImportierenWennKeinFehler, TheClientDto theClientDto);

	public void importiereForecastpositionZEISS_CSV(ArrayList<ForecastImportZeissDto> alZeilenCsv,
			Integer forecastauftragIId, TheClientDto theClientDto);

}
