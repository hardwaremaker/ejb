package com.lp.server.fertigung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.IVerbrauchsartikelImporterBeanServices;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;

public class VerbrauchsartikelImporterBeanServices implements
		IVerbrauchsartikelImporterBeanServices {

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private TheClientDto theClientDto;
	private VerbrauchsartikelImporterBeanHolder beanHolder;
	
	public VerbrauchsartikelImporterBeanServices(TheClientDto theClientDto, 
			VerbrauchsartikelImporterBeanHolder beanHolder) {
		this.theClientDto = theClientDto;
		this.beanHolder = beanHolder;
	}
	
	@Override
	public FertigungsgruppeDto findFertigungsgruppe() throws RemoteException {
		try {
			FertigungsgruppeDto[] fertGruppen = beanHolder.getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);
			return fertGruppen != null && fertGruppen.length > 0 ? fertGruppen[0] : null;
		} catch (EJBExceptionLP ex) {
			myLogger.error("Error finding Fertigungsgruppe", ex);
			return null;
		}
	}

	@Override
	public MontageartDto findMontageart() throws RemoteException {
		try {
			MontageartDto[] montagearten = beanHolder.getStuecklisteFac().montageartFindByMandantCNr(theClientDto);
			return montagearten != null && montagearten.length > 0 ? montagearten[0] : null;
		} catch (EJBExceptionLP ex) {
			myLogger.error("Error finding Montagearten", ex);
			return null;
		}
	}

	@Override
	public LosDto getDefaultLos() throws RemoteException {
		return beanHolder.getFertigungFac().setupDefaultLos(theClientDto);
	}

	@Override
	public LosDto createLosDto(LosDto losDto) throws RemoteException {
		return beanHolder.getFertigungFac().createLos(losDto, theClientDto);
	}

	@Override
	public KostenstelleDto findKostenstelle() throws RemoteException {
		try {
			KostenstelleDto[] kostenstellen = beanHolder.getSystemFac().kostenstelleFindByMandant(theClientDto.getMandant());
			if (kostenstellen == null) return null;
			
			for (KostenstelleDto kostenstelle : kostenstellen) {
				if (kostenstelle.getKontoIId() != null) return kostenstelle;
			}
		} catch (EJBExceptionLP ex) {
			myLogger.error("Error finding Kostenstellen", ex);
		}
		return null;
	}

	@Override
	public ArtikelDto artikelFindByCnr(String artikelCnr) throws RemoteException {
		return beanHolder.getArtikelFac().artikelFindByCNrMandantCNrOhneExc(artikelCnr, theClientDto.getMandant());
	}

	@Override
	public StuecklisteDto getStueckliste(Integer artikelIId) throws RemoteException {
		StuecklisteDto stuecklisteDto = beanHolder.getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
				artikelIId, theClientDto.getMandant());
		
		if (stuecklisteDto == null || 
				!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stuecklisteDto.getStuecklisteartCNr())) 
			return null;
		
		stuecklisteDto.setArtikelDto(beanHolder.getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(), theClientDto));
		return stuecklisteDto;
	}

	@Override
	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(
			Integer stuecklisteIId) throws RemoteException {
		return beanHolder.getStuecklisteFac().stuecklistepositionFindByStuecklisteIIdAllData(stuecklisteIId, theClientDto);
	}

	@Override
	public LagerDto findLoslager() throws RemoteException {
		LagerDto[] laeger = beanHolder.getLagerFac().lagerFindByMandantCNrOrderByILoslagersort(theClientDto.getMandant());
		if (laeger == null || laeger.length < 1) return null;
		return laeger[0];
	}

	@Override
	public BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId) throws RemoteException {
		return beanHolder.getLagerFac().getLagerstandOhneExc(artikelIId, lagerIId, theClientDto);
	}
	
	@Override
	public void gebeMaterialNachtraeglichAus(
			LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto) throws RemoteException {
		beanHolder.getFertigungFac().gebeMaterialNachtraeglichAus(
				lossollmaterialDto, losistmaterialDto, null, false, theClientDto);
	}
	
	@Override
	public LossollmaterialDto getDefaultLossollmaterial(ArtikelDto artikelDto,
			Integer losIId, Integer lagerIIdZiel, Integer montageartIId)
			throws RemoteException {
		return beanHolder.getFertigungFac().setupDefaultLossollmaterial(artikelDto, losIId, lagerIIdZiel, montageartIId, theClientDto);
	}

	@Override
	public void createLossollmaterial(LossollmaterialDto lossollmaterialDto) throws RemoteException {
		beanHolder.getFertigungFac().createLossollmaterial(lossollmaterialDto, theClientDto);
	}

	@Override
	public void gebeLosAus(Integer losIId) throws RemoteException {
		beanHolder.getFertigungFac().gebeLosAus(losIId, false, false, theClientDto, null);
	}
	
	@Override
	public void losErledigen(Integer losIId) throws RemoteException {
		beanHolder.getFertigungFac().manuellErledigen(losIId, false, theClientDto);
	}
	
	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}
	
	@Override
	public String getDefaultArtikelnummerLeereArtikelnummer() {
		return beanHolder.getParameterFac().getKassenimportLeereArtikelnummerDefaultArtikel(theClientDto.getMandant());
	}
}
