package com.lp.server.fertigung.service;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;

public interface IVendidataArticleExportBeanServices {

	public TheClientDto getTheClientDto();
	
	public List<ArtikelDto> artikelFindAllWith4VendingId() throws RemoteException;
	
	public ArtgruDto artikelgruppeFindByArtikelIId(Integer artgruIId) throws RemoteException;
	
	public VkPreisfindungEinzelverkaufspreisDto getArtikeleinzelverkaufspreis(Integer artikelIId) throws RemoteException;
	
	public VkpfartikelpreislisteDto[] getAlleAktivenPreislisten() throws RemoteException;
	
	public MwstsatzDto mwstSatzFindByPrimaryKey(Integer mwstSatzIId) throws RemoteException;
	
	public ArtikellieferantDto getArtikeleinkaufspreis(Integer artikelIId) throws RemoteException;
	
	public List<EinkaufseanDto> einkaufseanFindByArtikelIId(Integer artikelIId) throws RemoteException;
}
