package com.lp.server.angebotstkl.service;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.system.service.TheClientDto;

public interface IEkpositionWandlerBeanServices {

	TheClientDto gettheClientDto();
	
	ArtikelDto artikelFindByPrimaryKey(Integer artikelIId);
	
	EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(Integer positionIId) throws RemoteException;
	
	List<Integer> einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(Integer einkaufsangebotIId);
	
	void updateEinkaufsangebotposition(EinkaufsangebotpositionDto positionDto) throws RemoteException;
	
	void removeEinkaufsangebotposition(EinkaufsangebotpositionDto positionDto) throws RemoteException;
	
	Integer createArtikel(ArtikelDto artikelDto) throws RemoteException;
	
	EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer einkaufsangebotIId) throws RemoteException;
	
	Integer createArtikellieferant(ArtikellieferantDto artikellieferantDto) throws RemoteException;
	
	Integer createArtikellieferantstaffel(ArtikellieferantstaffelDto staffelDto) throws RemoteException;
	
	List<EinkaufsangebotpositionDto> einkaufsangebotpositionenFindByPrimaryKeys(Integer[] iIds);
	
	ArtikelDto artikelFindByCNr(String cNr) throws RemoteException;
}
