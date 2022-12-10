/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.partner.ejb.BranchesprPK;
import com.lp.server.partner.ejb.SerienbriefselektionPK;
import com.lp.server.partner.ejb.SerienbriefselektionnegativPK;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface PartnerServicesFac {

	public static final String FLR_PARTNERKOMMENTAR_DATENFORMAT_C_NR = "datenformat_c_nr";
	public static final String FLR_PARTNERKOMMENTAR_FLRPARTNERKOMMENTARART = "flrpartnerkommentarart";
	public static final String FLR_PARTNERKOMMENTAR_X_KOMMENTAR = "x_kommentar";
	public static final String FLR_PARTNERKOMMENTAR_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_PARTNERKOMMENTAR_B_KUNDE = "b_kunde";

	public static final int PARTNERKOMMENTARART_MITDRUCKEN = 0;
	public static final int PARTNERKOMMENTARART_HINWEIS = 1;
	public static final int PARTNERKOMMENTARART_ANHANG = 2;

	public String createKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKommunikationsart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateSerienbriefMailtext(SerienbriefDto serienbriefDto,
			TheClientDto theClientDto);

	public void updateKommunikationsart(
			KommunikationsartDto kommunikationsartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KommunikationsartDto kommunikationsartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KommunikationsartDto[] kommunikationsartFindAll()
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllKommunikationsArten(String cNrLocaleI,
			TheClientDto theClientDto) throws RemoteException;

	public Integer createBranche(BrancheDto brancheDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBranche(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBranche(BrancheDto brancheDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBranche(BrancheDto brancheDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BrancheDto brancheFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto);

	public BranchesprPK createBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBranchespr(Integer brancheIId, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBranchespr(BranchesprDto branchesprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BranchesprDto branchesprFindByPrimaryKey(Integer brancheIId,
			String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BranchesprDto[] branchesprFindByBrancheIId(Integer iIdBrancheI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KundeSelectCriteriaDto getSerienbriefSelektionsKriterien(
			Integer serienbriefIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getBriefanredeFuerBeleg(Integer iIdAnsprechpartnerI,
			Integer iIdPartnerI, Locale locBelegI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createSelektion(SelektionDto selektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeSelektion(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public SelektionDto selektionFindByCNrMandantCNr(String cNr,
			TheClientDto theClientDto) throws RemoteException;

	public void updateSelektion(SelektionDto selektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SelektionDto selektionFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createSelektionspr(SelektionsprDto selektionsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeSelektionspr(Integer selektionIId, String localeCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateSelektionspr(SelektionsprDto selektionsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SelektionsprDto selektionsprFindByPrimaryKey(Integer selektionIId,
			String localeCNr, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createSerienbrief(SerienbriefDto serienbriefDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeSerienbrief(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateSerienbrief(SerienbriefDto serienbriefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SerienbriefDto serienbriefFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public SerienbriefselektionPK createSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeSerienbriefselektion(Integer serienbriefIIdI,
			Integer selektionIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateSerienbriefselektion(
			SerienbriefselektionDto serienbriefselektionDtoI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public SerienbriefselektionDto serienbriefselektionFindByPrimaryKey(
			Integer serienbriefIIdI, Integer selektionIIdI,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public SerienbriefselektionDto[] serienbriefselektionFindBySerienbriefIId(
			Integer serienbriefIId) throws RemoteException;

	public KontaktartDto kontaktartFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public void updateKontaktart(KontaktartDto kontaktartDto)
			throws RemoteException;

	public void removeKontaktart(KontaktartDto dto) throws RemoteException;

	public Integer createKontaktart(KontaktartDto kontaktartDto)
			throws RemoteException;

	public SerienbriefselektionnegativDto serienbriefselektionnegativFindByPrimaryKey(
			Integer serienbriefIIdI, Integer selektionIIdI,
			TheClientDto theClientDto);

	public void updateSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDtoI,
			TheClientDto theClientDto);

	public void removeSerienbriefselektionnegativ(Integer serienbriefIIdI,
			Integer selektionIIdI, TheClientDto theClientDto);

	public SerienbriefselektionnegativPK createSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDtoI,
			TheClientDto theClientDto);

	public SerienbriefselektionnegativDto[] serienbriefselektionnegativFindBySerienbriefIId(
			Integer serienbriefIId);

	public PartnerkommentarartDto partnerkommentarartFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto);

	public Integer createPartnerkommentarart(PartnerkommentarartDto artDto,
			TheClientDto theClientDto);

	public void removePartnerkommentarart(PartnerkommentarartDto artDto);

	public void updatePartnerkommentarart(PartnerkommentarartDto artDto,
			TheClientDto theClientDto);

	public Integer createPartnerkommentar(
			PartnerkommentarDto partnerkommentarDto, TheClientDto theClientDto);

	public void updatePartnerkommentar(PartnerkommentarDto partnerkommentarDto,
			TheClientDto theClientDto);

	public PartnerkommentarDto partnerkommentarFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removePartnerkommentar(PartnerkommentarDto partnerkommentarDto);

	public ArrayList<PartnerkommentarDto> getPartnerhinweise(Integer partnerIId, boolean bKunde,
			String belegartCNr, TheClientDto theClientDto);

	public ArrayList<byte[]> getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(
			Integer partnerIId, boolean bKunde, String belegartCNr,
			Integer iArt, TheClientDto theClientDto);

	public void updateNewslettergrund(NewslettergrundDto dtoI,
			TheClientDto theClientDto);

	public void removeNewslettergrund(Integer iIdI, TheClientDto theClientDto);

	public Integer createNewslettergrund(NewslettergrundDto dto,
			TheClientDto theClientDto);

	public NewslettergrundDto newslettergrundFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto);

	public Map getAllNewslettergrund(TheClientDto theClientDto);

	public Integer createLiefermengen(LiefermengenDto dto);

	public void removeLiefermengen(LiefermengenDto dto);

	public LiefermengenDto liefermengenFindByPrimaryKey(Integer iId);

	public void updateLiefermengen(LiefermengenDto dto);

	public LiefermengenDto liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(
			Integer artikelIId, Integer kundeIIdLieferadresse, Timestamp tDatum);

	Integer createLiefermengenUnique(LiefermengenDto dto);

	public NewslettergrundDto newslettergrundFindByCBez(String cBez);

	public Integer createIdentifikation(IdentifikationDto dto,
			TheClientDto theClientDto);

	public IdentifikationDto identifikationFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removeIdentifikation(IdentifikationDto dto);

	public void updateIdentifikation(IdentifikationDto dto,
			TheClientDto theClientDto);

	public Integer createBeauskunftung(BeauskunftungDto dto,TheClientDto theClientDto);
	public void removeBeauskunftung(BeauskunftungDto dto);
	public BeauskunftungDto beauskunftungFindByPrimaryKey(Integer iId);
	public void updateBeauskunftung(BeauskunftungDto dto);
	public KontaktartDto getVorschlagFuerWiedervorlageAusSerienbrief();
	
	public DsgvokategorieDto dsgvokategorieFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto);
	public void updateDsgvokategorie(DsgvokategorieDto dto,
			TheClientDto theClientDto);
	public void removeDsgvokategorie(DsgvokategorieDto dto,
			TheClientDto theClientDto);
	public Integer createDsgvotext(DsgvotextDto dto, TheClientDto theClientDto);
	public void updateDsgvotext(DsgvotextDto dto);
	public DsgvotextDto dsgvotextFindByPrimaryKey(Integer iId);
	public void removeDsgvotext(DsgvotextDto dto);
	public Map getAllDSGVOKategorie(TheClientDto theClientDto);
	public void vertauscheDsgvotext(Integer id1,
			Integer id2);
	
	public Map getAllBranche(String cNrLocaleI, TheClientDto theClientDto);
	
	public byte[] partnerbildFindByPartnerIId(Integer partnerIId);
	public void updatePartnerbild(Integer partnerIId, byte[] bild);
	public Map getAllKontaktart(TheClientDto theClientDto);
	
	public ArrayList<PartnerkommentarDto> getPartnerkommentarAnhaenge(Integer partnerIId, boolean bKunde,
			String belegartCNr, TheClientDto theClientDto);
	
}
