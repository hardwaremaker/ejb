package com.lp.server.fertigung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.fertigung.service.IVendidataArticleExportBeanServices;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;

public class VendidataArticleExportBeanServices implements IVendidataArticleExportBeanServices {
	
	private TheClientDto theClientDto;
	private VendidataArticleExportBeanHolder beanHolder;

	public VendidataArticleExportBeanServices(TheClientDto theClientDto, VendidataArticleExportBeanHolder beanHolder) {
		this.theClientDto = theClientDto;
		this.beanHolder = beanHolder;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public List<ArtikelDto> artikelFindAllWith4VendingId() throws RemoteException {
		return beanHolder.getArtikelFac().artikelFindByMandantCNr4VendingIdNotNull(
				getTheClientDto().getMandant(), getTheClientDto());
	}
	
	@Override
	public ArtgruDto artikelgruppeFindByArtikelIId(Integer artgruIId) throws RemoteException {
		return beanHolder.getArtikelFac().artgruFindByPrimaryKey(artgruIId, getTheClientDto());
	}

	@Override
	public VkPreisfindungEinzelverkaufspreisDto getArtikeleinzelverkaufspreis(
			Integer artikelIId) throws RemoteException {
		return beanHolder.getVkPreisfindungFac().getArtikeleinzelverkaufspreis(artikelIId, 
				Helper.cutDate(new Date(System.currentTimeMillis())), 
				getTheClientDto().getSMandantenwaehrung(), getTheClientDto());

	}

	@Override
	public VkpfartikelpreislisteDto[] getAlleAktivenPreislisten() throws RemoteException {
		return beanHolder.getVkPreisfindungFac().getAlleAktivenPreislisten(Helper.boolean2Short(true), getTheClientDto());
	}
	
	@Override
	public MwstsatzDto mwstSatzFindByPrimaryKey(Integer mwstSatzIId) throws RemoteException {
		return beanHolder.getMandantFac().mwstsatzFindByPrimaryKey(mwstSatzIId, getTheClientDto());
	}

	@Override
	public ArtikellieferantDto getArtikeleinkaufspreis(Integer artikelIId) throws RemoteException {
		return beanHolder.getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelIId, BigDecimal.ONE, 
				getTheClientDto().getSMandantenwaehrung(), getTheClientDto());
	}

	@Override
	public List<EinkaufseanDto> einkaufseanFindByArtikelIId(Integer artikelIId)
			throws RemoteException {
		return beanHolder.getArtikelFac().einkaufseanFindByArtikelIId(artikelIId);
	}
	
}
