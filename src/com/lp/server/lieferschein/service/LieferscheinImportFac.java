package com.lp.server.lieferschein.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.ejb.Remote;

import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.system.service.ImportRueckgabeDto;
import com.lp.server.system.service.TheClientDto;

@Remote
public interface LieferscheinImportFac {
	public ImportRueckgabeDto importiereAmazon_CSV(LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>> hmNachBestellnummerGruppiert, Integer lagerIIdAbbuchungslager, boolean bImportiereWennKeinFehler,
			TheClientDto theClientDto);
	
	public ImportRueckgabeDto importiereShopify_CSV(
			LinkedHashMap<String, ArrayList<ImportShopifyCsvDto>> hmNachBestellnummerGruppiert, Integer lagerIIdAbbuchungslager, boolean bImportiereWennKeinFehler,
			TheClientDto theClientDto);
}
