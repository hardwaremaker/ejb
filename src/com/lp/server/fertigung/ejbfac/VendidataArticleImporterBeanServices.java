package com.lp.server.fertigung.ejbfac;

import java.io.Serializable;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.IVendidataArticleImporterBeanServices;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;

public class VendidataArticleImporterBeanServices implements IVendidataArticleImporterBeanServices, Serializable {
	
	private static final long serialVersionUID = 3849296951880882915L;
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	
	private TheClientDto theClientDto;
	private MandantDto mandantDto;
	private VendidataArticleImportBeanHolder beanHolder;

	public VendidataArticleImporterBeanServices(TheClientDto theClientDto, VendidataArticleImportBeanHolder beanHolder) {
		this.theClientDto = theClientDto;
		this.beanHolder = beanHolder;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public ArtikelDto artikelFindBy4VendingId(Integer fourVendingId) throws RemoteException {
		return beanHolder.getArtikelFac().artikelFindBy4VendingIdOhneExc(fourVendingId.toString(), getTheClientDto());
	}

	@Override
	public LagerDto[] lagerFindAll() throws RemoteException {
		return beanHolder.getLagerFac().lagerFindAll();
	}

	@Override
	public LagerDto lagerFindByPrimaryKey(Integer lagerIId) {
		return beanHolder.getLagerFac().lagerFindByPrimaryKeyOhneExc(lagerIId);
	}

	@Override
	public FertigungsgruppeDto findFertigungsgruppe() throws RemoteException {
		try {
			FertigungsgruppeDto[] fertGruppen = beanHolder.getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(getTheClientDto().getMandant(), getTheClientDto());
			return fertGruppen != null && fertGruppen.length > 0 ? fertGruppen[0] : null;
		} catch (EJBExceptionLP ex) {
			myLogger.error("Error finding Fertigungsgruppe", ex);
			return null;
		}
	}

	@Override
	public MontageartDto findMontageart() throws RemoteException {
		try {
			MontageartDto[] montagearten = beanHolder.getStuecklisteFac().montageartFindByMandantCNr(getTheClientDto());
			return montagearten != null && montagearten.length > 0 ? montagearten[0] : null;
		} catch (EJBExceptionLP ex) {
			myLogger.error("Error finding Montagearten", ex);
			return null;
		}
	}

	@Override
	public MandantDto mandantFindByPrimaryKey(String cNr)
			throws RemoteException {
		if (mandantDto == null || !mandantDto.getCNr().equals(cNr)) {
			mandantDto = beanHolder.getMandantFac().mandantFindByPrimaryKey(cNr, getTheClientDto());
		}
		return mandantDto;
	}

	@Override
	public LosDto[] losFindByFertigungsortPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) throws RemoteException {
		return beanHolder.getFertigungFac().losFindByFertigungsortPartnerIIdMandantCNrOhneExc(partnerIId, mandantCNr);
	}
	
	@Override
	public LosDto getDefaultLos() throws RemoteException {
		return beanHolder.getFertigungFac().setupDefaultLos(getTheClientDto());
	}

	@Override
	public LosDto createLosDto(LosDto losDto) throws RemoteException {
		return beanHolder.getFertigungFac().createLos(losDto, getTheClientDto());
	}

	@Override
	public KostenstelleDto[] findKostenstelle() throws RemoteException {
		return beanHolder.getSystemFac().kostenstelleFindByMandant(getTheClientDto().getMandant());
	}

	@Override
	public LoslagerentnahmeDto[] loslagerentnahmeFindByLosIId(Integer losIId) throws RemoteException {
		return beanHolder.getFertigungFac().loslagerentnahmeFindByLosIId(losIId);
	}

	@Override
	public void removeLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto) throws RemoteException {
		beanHolder.getFertigungFac().removeLoslagerentnahme(loslagerentnahmeDto, getTheClientDto());
	}

	@Override
	public LoslagerentnahmeDto createLoslagerentnahme(LoslagerentnahmeDto loslagerentnahmeDto) throws RemoteException {
		return beanHolder.getFertigungFac().createLoslagerentnahme(loslagerentnahmeDto, getTheClientDto());
	}

	@Override
	public void gebeLosAus(Integer losIId) throws RemoteException {
		beanHolder.getFertigungFac().gebeLosAus(losIId, false, true, getTheClientDto(), null);
	}

	@Override
	public LossollmaterialDto getDefaultLossollmaterial(ArtikelDto artikelDto,
			Integer losIId, Integer lagerIId, Integer montageartIId) throws RemoteException {
		return beanHolder.getFertigungFac().setupDefaultLossollmaterial(artikelDto, losIId, 
				lagerIId, montageartIId, getTheClientDto());
	}

	@Override
	public void gebeMaterialNachtraeglichAus(LossollmaterialDto lossollmaterialDto,
			LosistmaterialDto losistmaterialDto) throws RemoteException {
		beanHolder.getFertigungFac().gebeMaterialNachtraeglichAus(lossollmaterialDto, 
				losistmaterialDto, null, false, getTheClientDto());
	}
	
	public void bucheMaterialAufLos(LossollmaterialDto lossollmaterialDto, LosDto losDto) throws RemoteException {
		lossollmaterialDto = beanHolder.getFertigungFac().createLossollmaterialWithMaxISort(lossollmaterialDto, getTheClientDto());
		beanHolder.getFertigungFac().bucheMaterialAufLos(losDto, new LossollmaterialDto[] {lossollmaterialDto}, 
				null, false, false, false, getTheClientDto(), null, false);
	}
	
}
