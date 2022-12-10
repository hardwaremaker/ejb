package com.lp.server.lieferschein.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;

public interface IEasydataBeanServices {

	EinkaufseanDto einkaufseanFindByEan(String barcode) throws RemoteException;

	LagerDto lagerFindByCNr(String lagerCnr);

	PersonalDto personalFindByCPersonalnr(String personalnummer) throws EJBExceptionLP, RemoteException;

	AnsprechpartnerDto[] ansprechpartnerFindByPartnerIId(Integer partnerIId) throws EJBExceptionLP, RemoteException;

	MandantDto mandant() throws EJBExceptionLP, RemoteException;

	LieferscheinDto setupDefaultLieferschein(KundeDto kundeDto) throws RemoteException;

	LieferscheinpositionDto setupDefaultLieferscheinposition(LieferscheinDto lieferscheinDto, ArtikelDto artikelDto,
			BigDecimal menge) throws EJBExceptionLP, RemoteException;

	KundeDto kundeFindByPartnerIId(Integer partnerIId) throws RemoteException;

	PartnerDto partnerFindByPrimaryKey(Integer partnerIId);

	LagerDto[] lagerFindAll() throws EJBExceptionLP, RemoteException;

	LieferscheinDto createLieferschein(LieferscheinDto lieferscheinDto) throws EJBExceptionLP, RemoteException;

	void createLieferscheinposition(LieferscheinpositionDto lsPosDto) throws EJBExceptionLP, RemoteException;

	ArtikelDto artikelFindByPrimaryKey(Integer artikelIId);

	Boolean getParameterPositionskontierung();

}
