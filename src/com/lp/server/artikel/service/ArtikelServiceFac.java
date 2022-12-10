package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.system.service.TheClientDto;

@Remote
public interface ArtikelServiceFac {
	public void zusammenfuehrenArtikel(Integer artikelIId_Quelle, Integer artikelIId_Ziel, TheClientDto theClientDto);
	
	public Integer createDateiverweis(DateiverweisDto dto);
	public void updateDateiverweis(DateiverweisDto dto);
	public void removeDateiverweis(DateiverweisDto dto);
	public DateiverweisDto dateiverweisFindByPrimaryKey(Integer iId);
	
	public Integer createWaffenkaliber(WaffenkaliberDto dto);
	public void updateWaffenkaliber(WaffenkaliberDto dto);
	public void removeWaffenkaliber(WaffenkaliberDto dto);
	public WaffenkaliberDto waffenkaliberFindByPrimaryKey(Integer iId);
	
	public Integer createWaffenkategorie(WaffenkategorieDto dto);
	public Integer createWaffentyp(WaffentypDto dto);
	public Integer createWaffentypFein(WaffentypFeinDto dto);
	public Integer createWaffenzusatz(WaffenzusatzDto dto);
	public Integer createWaffenausfuehrung(WaffenausfuehrungDto dto);
	
	public void removeWaffenausfuehrung(WaffenausfuehrungDto dto);
	public void removeWaffenkategorie(WaffenkategorieDto dto);
	public void removeWaffentyp(WaffentypDto dto);
	public void removeWaffentypFein(WaffentypFeinDto dto);
	public void removeWaffenzusatz(WaffenzusatzDto dto);
	
	public void updateWaffenausfuehrung(WaffenausfuehrungDto dto);
	public void updateWaffenkategorie(WaffenkategorieDto dto);
	public void updateWaffentyp(WaffentypDto dto);
	public void updateWaffentypFein(WaffentypFeinDto dto);
	public void updateWaffenzusatz(WaffenzusatzDto dto) ;
	
	public WaffenausfuehrungDto waffenausfuehrungFindByPrimaryKey(Integer iId) ;
	public WaffenkategorieDto waffenkategorieFindByPrimaryKey(Integer iId);
	public WaffentypFeinDto waffentypFeinFindByPrimaryKey(Integer iId);
	public WaffentypDto waffentypFindByPrimaryKey(Integer iId);
	public WaffenzusatzDto waffenzusatzFindByPrimaryKey(Integer iId);
	
	public Map getAllWaffenkaliber(TheClientDto theClientDto);
	public Map getAllWaffenausfuehrung(TheClientDto theClientDto);
	public Map getAllWaffentyp(TheClientDto theClientDto);
	public Map getAllWaffentypFein(TheClientDto theClientDto);
	public Map getAllWaffenkategorie(TheClientDto theClientDto);
	public Map getAllWaffenzusatz(TheClientDto theClientDto);
	
	public void zusammenfuehrenHersteller(Integer herstellerIId_Quelle, Integer herstellerIId_Ziel, TheClientDto theClientDto);
	
	public List<AuftragseriennrnDto> zusammenfuehrenArtikelPruefeAuftragsnnr(Integer artikelIId_Quelle,
			Integer artikelIId_Ziel, TheClientDto theClientDto);
	
	public void pflegeWiederbeschaffungszeiten(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis,TheClientDto theClientDto);
	
	public ArrayList<LumiQuoteArtikelDto> getArtikelDatenForLumiquote(String artikelFilter, TheClientDto theClientDto);
	
	
}
