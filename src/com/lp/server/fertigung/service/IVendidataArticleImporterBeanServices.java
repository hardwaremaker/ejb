package com.lp.server.fertigung.service;

import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;

public interface IVendidataArticleImporterBeanServices {

	public TheClientDto getTheClientDto();
	
	public ArtikelDto artikelFindBy4VendingId(Integer fourVendingId) throws RemoteException;
	
	public LagerDto[] lagerFindAll() throws RemoteException;
	
	public LagerDto lagerFindByPrimaryKey(Integer lagerIIdZiel);

	public FertigungsgruppeDto findFertigungsgruppe() throws RemoteException;
	
	public MontageartDto findMontageart() throws RemoteException;
	
	public MandantDto mandantFindByPrimaryKey(String cNr) throws RemoteException;
	
	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNrOhneExc(Integer partnerIId, String mandantCNr) throws RemoteException;
	
	public LosDto getDefaultLos() throws RemoteException;
	
	public LosDto createLosDto(LosDto losDto) throws RemoteException;

	public KostenstelleDto[] findKostenstelle() throws RemoteException;
	
	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId) throws RemoteException;
	
	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto) throws RemoteException;
	
	public LoslagerentnahmeDto createLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto) throws RemoteException;

	public void gebeLosAus(Integer losIId) throws RemoteException;
	
	public LossollmaterialDto getDefaultLossollmaterial(ArtikelDto artikelDto, Integer losIId, 
			Integer lagerIId, Integer montageartIId) throws RemoteException;
	
	public void gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto, 
			LosistmaterialDto losistmaterialDto) throws RemoteException;

	public void bucheMaterialAufLos(LossollmaterialDto lossollmaterialDto, LosDto losDto) throws RemoteException;	
}
