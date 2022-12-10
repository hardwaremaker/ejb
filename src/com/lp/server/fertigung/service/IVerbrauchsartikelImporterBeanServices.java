package com.lp.server.fertigung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;

public interface IVerbrauchsartikelImporterBeanServices {

	public FertigungsgruppeDto findFertigungsgruppe() throws RemoteException;
	
	public MontageartDto findMontageart() throws RemoteException;
	
	public LosDto getDefaultLos() throws RemoteException;
	
	public LosDto createLosDto(LosDto losDto) throws RemoteException;

	public KostenstelleDto findKostenstelle() throws RemoteException;
	
	public ArtikelDto artikelFindByCnr(String artikelCnr) throws RemoteException;
	
	public StuecklisteDto getStueckliste(Integer artikelIId) throws RemoteException;
	
	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(Integer stuecklisteIId) throws RemoteException;

	public LagerDto findLoslager() throws RemoteException;
	
	public BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId) throws RemoteException;

	public LossollmaterialDto getDefaultLossollmaterial(ArtikelDto artikelDto,
			Integer losIId, Integer lagerIIdZiel, Integer montageartIId) throws RemoteException;

	public void gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto) throws RemoteException;
	
	public void createLossollmaterial(LossollmaterialDto lossollmaterialDto) throws RemoteException;

	void gebeLosAus(Integer losIId) throws RemoteException;

	void losErledigen(Integer losIId) throws RemoteException;
	
	TheClientDto getTheClientDto();

	String getDefaultArtikelnummerLeereArtikelnummer();
}
