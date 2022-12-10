package com.lp.server.lieferschein.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.lieferschein.service.IEasydataBeanServices;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class EasydataBeanServices implements IEasydataBeanServices {

	private TheClientDto theClientDto;
	private EasydataBeanHolder beanHolder;
	
	public EasydataBeanServices(EasydataBeanHolder beanHolder, TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		this.beanHolder = beanHolder;
	}
	
	@Override
	public EinkaufseanDto einkaufseanFindByEan(String barcode) throws RemoteException {
		return beanHolder.getArtikelFac().einkaufseanFindByCEan(barcode);
	}

	@Override
	public LagerDto lagerFindByCNr(String lagerCnr) {
		return beanHolder.getLagerFac().lagerFindByCNrByMandantCNrOhneExc(lagerCnr, theClientDto.getMandant());
	}
	
	@Override
	public PersonalDto personalFindByCPersonalnr(String personalnummer) throws EJBExceptionLP, RemoteException {
		return beanHolder.getPersonalFac().personalFindByCPersonalnrMandantCNrOhneExc(personalnummer, theClientDto.getMandant());
	}
	
	@Override
	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIId(Integer partnerIId) throws EJBExceptionLP, RemoteException {
		return beanHolder.getAnsprechpartnerFac().ansprechpartnerFindByPartnerIIdOhneExc(partnerIId, theClientDto);		
	}
	
	@Override
	public MandantDto mandant() throws EJBExceptionLP, RemoteException {
		return beanHolder.getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
	}
	
	@Override
	public LieferscheinDto setupDefaultLieferschein(KundeDto kundeDto) throws RemoteException {
		return beanHolder.getLieferscheinFac().setupDefaultLieferschein(kundeDto, theClientDto);
	}
	
	@Override
	public LieferscheinpositionDto setupDefaultLieferscheinposition(LieferscheinDto lieferscheinDto, ArtikelDto artikelDto, BigDecimal menge) throws EJBExceptionLP, RemoteException {
		return beanHolder.getLieferscheinPosFac().setupLieferscheinpositionDto(lieferscheinDto, artikelDto, menge, theClientDto);
	}
	
	@Override
	public KundeDto kundeFindByPartnerIId(Integer partnerIId) throws RemoteException {
		return beanHolder.getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerIId, theClientDto.getMandant(), theClientDto);
	}
	
	@Override
	public PartnerDto partnerFindByPrimaryKey(Integer partnerIId) {
		return beanHolder.getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
	}
	
	@Override
	public LagerDto[] lagerFindAll() throws EJBExceptionLP, RemoteException {
		return beanHolder.getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());
	}
	
	@Override
	public LieferscheinDto createLieferschein(LieferscheinDto lieferscheinDto) throws EJBExceptionLP, RemoteException {
		Integer lieferscheinIId = beanHolder.getLieferscheinFac().createLieferschein(lieferscheinDto, theClientDto);
		return beanHolder.getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinIId);
	}
	
	@Override
	public void createLieferscheinposition(LieferscheinpositionDto lsPosDto) throws EJBExceptionLP, RemoteException {
		beanHolder.getLieferscheinPosFac().createLieferscheinposition(lsPosDto, false, theClientDto);
	}
	
	@Override
	public ArtikelDto artikelFindByPrimaryKey(Integer artikelIId) {
		return beanHolder.getArtikelFac().artikelFindByPrimaryKeySmallOhneExc(artikelIId, theClientDto);
	}
	
	@Override
	public Boolean getParameterPositionskontierung() {
		return beanHolder.getParameterFac().getPositionskontierung(theClientDto.getMandant());
	}
}
