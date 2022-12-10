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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.partner.ejb.PartnerartsprPK;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface PartnerFac {

	// FLR Spaltennamen aus Hibernate
	// Mapping*************************************
	public static final String FLR_PARTNER_FLRLANDPLZORT = "flrlandplzort";
	public static final String FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 = "c_name1nachnamefirmazeile1";
	public static final String FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2 = "c_name2vornamefirmazeile2";
	public static final String FLR_PARTNER_LANDPLZORT_ORT_NAME = FLR_PARTNER_FLRLANDPLZORT
			+ "." + SystemFac.FLR_LP_FLRORT + "." + SystemFac.FLR_LP_ORTNAME;
	public static final String FLR_PARTNER_I_ID = "i_id";
	public static final String FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1 = "c_name1nachnamefirmazeile1";
	public static final String FLR_PARTNER_C_NAME2VORNAMEFIRMAZEILE2 = "c_name2vornamefirmazeile2";
	public static final String FLR_PARTNER_C_NAME3VORNAME2ABTEILUNG = "c_name3vorname2abteilung";
	public static final String FLR_PARTNER_C_STRASSE = "c_strasse";
	public static final String FLR_PARTNER_C_ADRESSART = "c_adressart";
	public static final String FLR_PARTNER_PARTNERART = "partnerart_c_nr";
	public static final String FLR_PARTNER_ANREDE = "anrede_c_nr";
	public static final String FLR_PARTNER_VERSTECKT = "b_versteckt";
	public static final String FLR_PARTNER_TITEL = "c_titel";
	public static final String FLR_PARTNER_NTITEL = "c_ntitel";
	public static final String FLR_PARTNER_C_KBEZ = "c_kbez";
	public static final String FLR_PARTNER_C_UID = "c_uid";
	public static final String FLR_PARTNER_F_GMTVERSATZ = "f_gmtversatz";
	public static final String FLR_PARTNER_LOCALE_C_NR_KOMMUNIKATION = "locale_c_nr_kommunikation";
	public static final String FLR_PARTNER_PARTNER_PASELEKTION_SET = "partner_paselektion_set";

	public static final String PARTNERQP1_ERWEITERTE_SUCHE = "ERWEITERTE_SUCHE";
	public static final String PARTNERQP1_TELEFONNUMMERN_SUCHE = "TELEFONNUMMERN_SUCHE";

	public static final String FLR_PARTNER_KOMMUNIKATION_FLRPARTNER = "flrpartner";
	public static final String FLR_PARTNER_KOMMUNIKATION_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_PARTNER_KOMMUNIKATION_C_BEZ = "c_bez";
	public static final String FLR_PARTNER_KOMMUNIKATION_B_PRIVAT = "b_privat";
	public static final String FLR_PARTNER_KOMMUNIKATION_C_INHALT = "c_inhalt";
	public static final String FLR_PARTNER_KOMMUNIKATION_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_PARTNER_KOMMUNIKATION_FLRKOMMUNIKATIONSART = "flrkommunikationsart";

	public static final String FLR_PARTNER_SELEKTION_C_BEMERKUNG = "c_bemerkung";

	public static final String FLR_PARTNER_KURZBRIEF_C_BETREFF = "c_betreff";
	public static final String FLR_PARTNER_KURZBRIEF_WER = "flrpartner.c_name1nachnamefirmazeile1";
	public static final String FLR_PARTNER_KURZBRIEF_WANN = "t_aendern";
	public static final String FLR_PARTNER_KURZBRIEF_ANSPRECHPARTNER = "ansprechpartner";
	public static final String FLR_PARTNER_KURZBRIEF_PERSONAL_I_ID_AENDERN = "personal_i_id_aendern";
	public static final String FLR_PARTNER_KURZBRIEF_T_AENDERN = "t_aendern";
	public static final String FLR_PARTNER_KURZBRIEF_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_PARTNER_KURZBRIEF_FLRPERSONAL = "flrpersonal";
	public static final String FLR_PARTNER_KURZBRIEF_FLRPARTNER = "flrpartner";
	public static final String FLR_PARTNER_KURZBRIEF_FLRANSPRECHPARTNER = "flransprechpartner";

	public static final String FLR_PARTNER_SERIENBRIEF_C_BEZ = "c_bez";
	public static final String FLR_PARTNER_SERIENBRIEF_C_WANN = "t_aendern";
	
	public static final String FLR_NEWSLETTERGRUND_C_BEZ = "c_bez";
	public static final String FLR_NEWSLETTERGRUND_B_ANGEMELDET = "b_angemeldet";

	public static final String FLR_SERIENBRIEFSELEKTION_SERIENBRIEF_I_ID = "serienbrief_i_id";
	public static final String FLR_SERIENBRIEFSELEKTION_SELEKTION_I_ID = "selektion_i_id";
	public static final String FLR_SERIENBRIEFSELEKTION_C_BEMERKUNG = "c_bemerkung";
	public static final String FLR_SERIENBRIEFSELEKTION_FLRSERIENBRIEF = "flrserienbrief";
	public static final String FLR_SERIENBRIEFSELEKTION_FLRSELEKTION = "flrselektion";

	public static final String FLR_PASELEKTION_PARTNER_I_ID = "partner_i_id";
	public static final String FLR_PASELEKTION_SELEKTION_I_ID = "selektion_i_id";
	public static final String FLR_PASELEKTION_C_BEMERKUNG = "c_bemerkung";
	public static final String FLR_PASELEKTION_FLRSELEKTION = "flrpaselektion";

	public static final String FLR_FLRSELEKTION_SELEKTION_SELEKTIONSPR_SET = "selektion_selektionspr_set";
	public static final String FLR_FLRSELEKTION_SELEKTION_I_ID = "selektion_i_id";

	//Testaufbau****************************************************************
	// *
	// Anrede.
	public static final String PARTNER_ANREDE_HERR = "Herr           ";
	public static final String PARTNER_ANREDE_FRAU = "Frau           ";
	public static final String PARTNER_ANREDE_FIRMA = "Firma          ";
	public static final String PARTNER_ANREDE_FAMILIE = "Familie        ";

	// Partnerart.
	public static final String PARTNERART_ADRESSE = "Adresse        ";
	public static final String PARTNERART_ANSPRECHPARTNER = "Ansprechpartner";
	public static final String PARTNERART_FIRMA =   "Firma          ";
	public static final String PARTNERART_PERSON = "Person         ";
	public static final String PARTNERART_SONSTIGES = "Sonstiges      ";
	public static final String PARTNERART_VEREIN = "Verein         ";

	// Kommunikationsarten
	public static final String KOMMUNIKATIONSART_EMAIL = "E-Mail         ";
	public static final String KOMMUNIKATIONSART_FAX = "Fax            ";
	public static final String KOMMUNIKATIONSART_DIREKTFAX = "Direktfax      ";
	public static final String KOMMUNIKATIONSART_HOMEPAGE = "Homepage       ";
	public static final String KOMMUNIKATIONSART_KURZWAHL = "Kurzwahl       ";
	public static final String KOMMUNIKATIONSART_HANDY = "Handy          ";
	public static final String KOMMUNIKATIONSART_TELEFON = "Telefon        ";

	// Branche
	public static final String BRANCHE_ELEKTRO = "Elektro";
	public static final String BRANCHE_FERTIGUNG = "Fertigung";
	public static final String BRANCHE_EDV = "EDV";
	public static final String BRANCHE_PHARMA = "Pharma";
	// Partnerklasse
	public static final String KLASSE_KUNDE = "Kunde";
	public static final String KLASSE_KONSULENT = "Konsulent";
	public static final String KLASSE_PRIVAT = "Privat";
	public static final String KLASSE_HAENDLER = "H\u00E4ndler";
	
	public static final String IMPORTART_SHOP = "Shop";
	public static final String IMPORTART_ESEENSAUSGABE = "Essensausgabe";

	// Adressart
	public static final String ADRESSART_LIEFERADRESSE = "L";
	public static final String ADRESSART_FILIALADRESSE = "F";
	public static final String ADRESSART_RECHNUNGSADRESSE = "R";

	// Feldlaengen: Kommunikationsart
	public static int MAX_KOMMART_BEZEICHNUNG = 80;
	public static int MAX_KOMMART_INHALT = 80;
	public static int MAX_KOMMART_C_NR = 15;

	// Feldlaengen: Partner
	public static int MAX_UID = 20;
	public static int MAX_KBEZ = 25;
	public static int MAX_NAME = 40; // alle Namen
	public static int MAX_STRASSE = 80;
	public static int MAX_TITEL = 80;
	public static int MAX_POSTFACH = 15;
	public static int MAX_FIRMENBUCHNR = 50;
	public static int MAX_GERICHTSSTAND = 40;
	public static int MAX_BRANCHE = 120;
	public static int MAX_ILN = 15;

	public static final String PART_ZUSAMMENFUEHREN_MODUS_PARTNER = "partner";
	public static final String PART_ZUSAMMENFUEHREN_MODUS_ANSPRECHPARTNER = "ansprechpartner";

	public Integer createPartner(PartnerDto partnerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePartner(PartnerDto partnerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updatePartner(PartnerDto partnerDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PartnerDto partnerFindByPrimaryKey(Integer iIdPartnerI,
			TheClientDto theClientDto) ;

	public PartnerDto partnerFindByPrimaryKeyOhneExc(Integer iIdPartnerI,
			TheClientDto theClientDto);

	public Integer createPartnerkommunikation(
			PartnerkommunikationDto partnerkommunikationDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePartnerkommunikation(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePartnerkommunikation(
			PartnerkommunikationDto partnerkommunikationDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto partnerkommunikationFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto partnerkommunikationFindByPrimaryKeyOhneExc(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllPartnerArten(String cNrLocaleI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public TreeMap<?, ?> getAllAnreden(Locale locAnredeI)
			throws EJBExceptionLP, RemoteException;

	public String createAnrede(AnredeDto anredeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAnrede(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAnrede(AnredeDto anredeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AnredeDto anredeFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createPartnerklasse(PartnerklasseDto partnerklasseDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePartnerklasse(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updatePartnerklasse(PartnerklasseDto partnerklasseDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerklasseDto partnerklasseFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto);

	public String createPartnerart(PartnerartDto partnerartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePartnerart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePartnerart(PartnerartDto partnerartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignPartnerkommunikationBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignVersandauftragBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignHerstellerBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignPartnerbankBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignBankBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, TheClientDto theClientDto,
			boolean bBankMitverdichten) throws EJBExceptionLP, RemoteException;

	public void reassignLieferantBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			boolean bLieferantMitverdichten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignRechnungBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignStuecklisteBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignKundeBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			boolean bKundeMitverdichten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignFinanzamtBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignMandantBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignAuftragteilnehmerBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignPersonalBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			boolean bPersonalMitverdichten, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignBestellungBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignLosBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignProjektBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignAnsprechpartnerBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignPartnerkurzbriefBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignReiseBeimZusammenfuehren(PartnerDto partnerZielDto,
			PartnerDto partnerQuellDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void reassignTelefonzeitBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void reassignPaSelektionBeimZusammenfuehren(
			PartnerDto partnerZielDto, PartnerDto partnerQuellDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void zusammenfuehrenPartner(PartnerDto partnerZielDto,
			int partnerQuellDtoIid, boolean kundeMitverdichten,
			boolean lieferantMitverdichten, boolean bankMitverdichten,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePartnerart(PartnerartDto partnerartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerartDto partnerartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerartDto[] partnerartFindAll() throws EJBExceptionLP,
			RemoteException;

	public PartnerartsprPK createPartnerartspr(
			PartnerartsprDto partnerartsprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePartnerartspr(String partnerartCNrI, String cNrLocaleI)
			throws EJBExceptionLP, RemoteException;

	public String enrichNumber(Integer iIdPartnerI,
			String cNrKommunikationsartI, TheClientDto theClientDto,
			String cTelefon,
			boolean bNurNummerAbschneiden);

	public void removePartnerartspr(PartnerartsprDto partnerartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePartnerartspr(PartnerartsprDto partnerartsprDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerartsprDto partnerartsprFindByPrimaryKey(String partnerartCNr,
			String cNrLocaleI) throws EJBExceptionLP, RemoteException;

	public PartnerDto[] partnerFindByName1(String sName1I,
			TheClientDto theClientDto);
	
	public PartnerDtoSmall partnerFindByPrimaryKeySmall(Integer iIdPartnerI,
			TheClientDto theClientDto) throws RemoteException;
	
	public PartnerDtoSmall partnerFindByPrimaryKeySmallOhneExc(Integer iIdPartnerI);

	public String formatBriefAnrede(PartnerDto partnerDto, Locale loI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createPASelektion(PASelektionDto pASelektionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePASelektion(PASelektionDto pASelektionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updatePASelektion(PASelektionDto pASelektionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;


	public PASelektionDto pASelektionFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PASelektionDto[] pASelektionFindByPartnerIId(Integer partnerIId)
			throws EJBExceptionLP, RemoteException;

	public Map getAllPersonenWiedervorlage(TheClientDto theClientDto)
			throws RemoteException;

	public PASelektionDto[] pASelektionFindByPartnerIIdOhneExc(
			Integer partnerIId) throws EJBExceptionLP, RemoteException;

	public Integer createKurzbrief(KurzbriefDto kurzbriefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKurzbrief(Integer iIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKurzbrief(KurzbriefDto kurzbriefDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KurzbriefDto kurzbriefFindByPrimaryKey(Integer iIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KurzbriefDto kurzbriefFindByPrimaryKeyOhneExc(Integer iIdI,
			TheClientDto theClientDto) throws RemoteException;

	public KurzbriefDto[] kurzbriefFindByPartnerIId(Integer iPartnerIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KurzbriefDto[] kurzbriefFindByPartnerIIdOhneExc(Integer iPartnerIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KurzbriefDto[] kurzbriefFindByAnsprechpartnerIId(
			Integer iAnsprechartnerIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KurzbriefDto[] kurzbriefFindByAnsprechpartnerIIdOhneExc(
			Integer iAnsprechartnerIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String partnerkommFindOhneExec(Integer iIdPartnerI,
			Integer iIdPartnerAnsprechpartnerI, String cNrKommunikationsartI,
			String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public String partnerkommFindOhneAnpassungOhneExec(Integer iIdPartnerI,
			Integer iIdPartnerAnsprechpartnerI, String cNrKommunikationsartI,
			String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public String passeInlandsAuslandsVorwahlAn(
			Integer iIdPartnerI, String cNrMandantI,
			String cTelefonnummer,
			TheClientDto theClientDto);

	public String partnerkommFindRespectPartnerAsStringOhneExec(
			Integer iIdPartnerAnsprechpartnerI, PartnerDto partnerDtoI,
			String cNrKommunikationsartI, String cNrMandantI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdKommunikationsartPAiIdKommArtMandant(
			Integer iIdPartnerI, String cNrKommunikationsartI,
			String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto[] partnerkommFindByPartnerIId(
			Integer iIdPartnerI) throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdMandantCNr(
			Integer iIdPartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PartnerkommunikationDto[] partnerkommFindByPartnerIIdMandantCNrOhneExc(
			Integer iIdPartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String formatFixAnredeTitelName2Name1(PartnerDto partnerDto,
			Locale loc, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createKontakt(KontaktDto kontaktDto,
			TheClientDto theClientDto) throws RemoteException;

	public void removeKontakt(KontaktDto dto) throws RemoteException;

	public KontaktDto kontaktFindByPrimaryKey(Integer iId)
			throws RemoteException;

	public void updateKontakt(KontaktDto kontaktDto) throws RemoteException;

	public String formatFixAnredeTitelName2Name1FuerAdresskopf(PartnerDto partnerDto,
			Locale loc, TheClientDto theClientDto);
	public AnredesprDto anredesprFindByAnredeCNrLocaleCNrOhneExc(
			String anredeCNr, String localeCNr);
	
	/**
	 * Einen Partner anhand seiner UID Nummer finden
	 * @param UidNummer die gesuchte UID Nummer
	 * @return PartnerDto passend zur UID oder null
	 */
	PartnerDto partnerFindByUIDNull(String UidNummer) ;

	/**
	 * Partner anhand der UID finden</br>
	 * <p>Bei schlechten Stammdaten gibt es die gleiche UID f&uuml;r mehrere Partner.
	 * Damit man herausbekommt welche das sind, gibt es alle Partner als Ergebnis</p>
	 * 
	 * @param uidNummer die gesuchte UID Nummer
	 * @return eine (leere) Liste von Partnern, die die angeforderte UID haben
	 */
	PartnerDto[] partnerFindByUID(String uidNummer) ;

	/**
	 * Alle Partner die case-insensitive im cname1 dem angegebenen Namen entsprechen
	 * @param sName1I
	 * @return PartnerDto dess Name1 dem gesuchten entspricht, oder null
	 * @throws EJBExceptionLP
	 */
	PartnerDto[] partnerFindByName1Lower(String sName1I) throws EJBExceptionLP ;
	
	/**
	 * Hat der Partner einen externen Versandweg - wie beispielsweise Clevercure / EDI - definiert?</br>
	 * 
	 * @param partnerIId
	 * @return true wenn fuer den Kunden ein Versandweg definiert ist
	 * @throws RemoteException
	 */
	boolean hatPartnerVersandweg(Integer partnerIId) throws RemoteException ;	

	/**
	 * Hat der Partner einen externen Versandweg - wie beispielsweise Clevercure / EDI - definiert?</br>
	 * 
	 * @param partnerDto
	 * @return true wenn fuer den Kunden ein Versandweg definiert ist
	 * @throws RemoteException
	 */
	boolean hatPartnerVersandweg(PartnerDto partnerDto) throws RemoteException ;	
	
	/**
	 * Alle Partner die case-insensitive die gesuchte E-Mail Adresse haben
	 * @param email
	 * @return PartnerDtos deren EMail der gesuchten entspricht, oder null
	 * @throws EJBExceptionLP
	 */
	public PartnerDto[] partnerFindByEmail(String email) throws EJBExceptionLP ;
	
	public void kontoFuerPartnerImportAnlegen(String sKontonummer,
			String kontotypCNr, Integer kundeIId, Integer lieferantIId,
			TheClientDto theClientDto);

	/**
	 * Die PartnerId f&uuml;r einen bekannten Ansprechpartner ermitteln</br>
	 * 
	 * @param ansprechpartnerId
	 * @return null oder die partnerId
	 */
	Integer partnerIdFindByAnsprechpartnerId(Integer ansprechpartnerId) ;
	
	/**
	 * Den Partner f&uuuml;r einen bekannten Ansprechpartner ermitteln</br>
	 * <p>Es handelt sich um das PartnerDto dem dieser Ansprechpartner zugeordnet ist.
	 * Nicht um das PartnerDto des Ansprechpartners selbst</p> 
	 * 
	 * @param ansprechpartnerId
	 * @param theClientDto
	 * @return das dazugehoerige PartnerDto des Partners
	 * @throws EJBExceptionLP wenn zur ansprechpartnerId kein PartnerDto ermittelt 
	 * werden kann
	 */
	PartnerDto partnerFindByAnsprechpartnerId(Integer ansprechpartnerId, 
			TheClientDto theClientDto) throws EJBExceptionLP ;	
	
	public Integer getDefaultMWSTSatzIIdAnhandLand(LandDto landDto,
			TheClientDto theClientDto);

	Integer createGeodaten(GeodatenDto geodatenDto);

	void removeGeodaten(Integer geodatenIId);

	void updateGeodaten(GeodatenDto geodatenDto);

	GeodatenDto geodatenFindByPartnerIIdOhneExc(Integer partnerIId);

	void createGeodaten(List<GeodatenDto> createList);
	
	public void empfaengerlisteAlsKontakteBeiPartnernEintragen(String titel,
			SerienbriefEmpfaengerDto[] empfaenger, Timestamp tKontakt,
			Timestamp tWiedervorlage, Integer kontaktartIId,Integer personalIIdZugewiesener,
			TheClientDto theClientDto);
	
	public void telefonnummerFuerTapiSynchronisieren(Integer partnerIId, Integer ansprechpartnerIId,
			TheClientDto theClientDto);
	
	/**
	 * Ermittelt alle Partner die die angegebene Telefonnummer haben
	 * 
	 * @param telefonnummer die gesuchte Telefonnummer
	 * @param modified wurde die Telefonnummer von extern veraendert? Falls true, wird
	 * die interne Logik zur Suche der Telefonnummer nicht verwendet
	 * @param theClientDto
	 * @return eine (leere) Liste von Partnerinformationen zur gesuchten Telefonnummer
	 */
	List<TelefonSuchergebnisDto> findeTelefonnummer(
			String telefonnummer, boolean modified, TheClientDto theClientDto);
	
	public void reassignPartnerkommentarBeimZusammenfuehren(Integer partnerIId_Ziel, Integer partnerIId_Quelle,
			boolean bKunde, TheClientDto theClientDto) ;
	
	public ArrayList<AdressbuchExportDto> getDatenFuerAdressbuchExport(int iMaximaleAanzahl, TheClientDto theClientDto);
	
	public ArrayList<AdressbuchExportDto> getDatenFuerAdressbuchExport(int iMaximaleAanzahl, String cEmailAdresse, TheClientDto theClientDto);
	
	public void adressdatensatzEinesPartnersAlsExportiertMarkieren(Integer partnerIId,Integer ansprechpartnerIId, String cEmailAdresse, String exchangeId);
	
	public void testAdressdatenexport(TheClientDto theClientDto);
	
	public void testAdressdatenexport(String cEmailAdresse, TheClientDto theClientDto);
	
}
