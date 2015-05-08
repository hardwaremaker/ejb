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
package com.lp.server.personal.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerDtoSmall;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.report.PersonRpt;

@Remote
public interface PersonalFac {

	public static final String FLR_PERSONAL_C_PERSONALNUMMER = "c_personalnummer";
	public static final String FLR_PERSONAL_C_AUSWEIS = "c_ausweis";
	public static final String FLR_PERSONAL_PERSONALART_C_NR = "personalart_c_nr";
	public static final String FLR_PERSONAL_FLRPARTNER = "flrpartner";
	public static final String FLR_PERSONAL_FLRPERSONALGRUPPE = "flrpersonalgruppe";
	public static final String FLR_PERSONAL_FLRKOSTENSTELLEABTEILUNG = "flrkostenstelleabteilung";
	public static final String FLR_PERSONAL_FLRKOSTENSTELLESTAMM = "flrkostenstellestamm";
	public static final String FLR_PERSONAL_B_VERSTECKT = "b_versteckt";
	public static final String FLR_PERSONAL_C_KURZZEICHEN = "c_kurzzeichen";
	public static final String FLR_PERSONAL_T_GEBURTSDATUM = "t_geburtsdatum";

	public static final String FLR_BERUF_C_BEZ = "c_bez";

	public static final String FLR_RELIGION_RELIGIONSPRSET = "religionsprset";
	public static final String FLR_TAGESART_TAGESARTSPRSET = "tagesartsprset";

	public static final String FLR_KOLLEKTIV_C_BEZ = "c_bez";
	public static final String FLR_KOLLEKTIV_N_NORMALSTUNDEN = "n_normalstunden";

	public static final String FLR_KOLLEKTIVUESTD_U_AB = "u_ab";
	public static final String FLR_KOLLEKTIVUESTD_U_BIS = "u_bis";
	public static final String FLR_KOLLEKTIVUESTD_B_RESTDESTAGES = "b_restdestages";
	public static final String FLR_KOLLEKTIVUESTD_B_UNTERIGNORIEREN = "b_unterignorieren";
	public static final String FLR_KOLLEKTIVUESTD_FLRKOLLEKTIV = "flrkollektiv";
	public static final String FLR_KOLLEKTIVUESTD_FLRTAGESART = "flrtagesart";

	public static final String FLR_KOLLEKTIVUESTD50_U_VON = "u_von";
	public static final String FLR_KOLLEKTIVUESTD50_U_BIS = "u_bis";
	public static final String FLR_KOLLEKTIVUESTD50_B_RESTDESTAGES = "b_restdestages";
	public static final String FLR_KOLLEKTIVUESTD50_B_UNTERIGNORIEREN = "b_unterignorieren";
	public static final String FLR_KOLLEKTIVUESTD50_FLRKOLLEKTIV = "flrkollektiv";
	public static final String FLR_KOLLEKTIVUESTD50_FLRTAGESART = "flrtagesart";

	public static final String FLR_ZULAGE_C_BEZ = "c_bez";

	public static final String FLR_ARTKELZULAGE_FLRZULAGE = "flrzulage";
	public static final String FLR_ARTKELZULAGE_FLRARTIKEL = "flrartikel";

	public static final String FLR_KOLLEKTIV_B_VERBRAUCHEUESTD = "b_verbraucheuestd";

	public static final String FLR_LOHNGRUPPE_C_BEZ = "c_bez";

	public static final String FLR_PENDLERPAUSCHALE_C_BEZ = "c_bez";

	public static final String FLR_PERSONALANGEHOERIGE_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALANGEHOERIGE_C_VORNAME = "c_vorname";
	public static final String FLR_PERSONALANGEHOERIGE_C_NAME = "c_name";
	public static final String FLR_PERSONALANGEHOERIGE_C_SOZIALVERSNR = "c_sozialversnr";
	public static final String FLR_PERSONALANGEHOERIGE_D_GEBURTSDATUM = "t_geburtsdatum";
	public static final String FLR_PERSONALANGEHOERIGE_FLRANGEHOERIGENART = "flrangehoerigenart";

	public static final String FLR_EINTRITTAUSTRITT_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_EINTRITTAUSTRITT_D_EINTRITT = "t_eintritt";
	public static final String FLR_EINTRITTAUSTRITT_D_AUSTRITT = "t_austritt";
	public static final String FLR_EINTRITTAUSTRITT_C_AUTRITTSGRUND = "c_austrittsgrund";

	public static final String FLR_PERSONALZEITEN_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALZEITEN_D_VON = "t_von";
	public static final String FLR_PERSONALZEITEN_D_BIS = "t_bis";
	public static final String FLR_PERSONALZEITEN_C_BEMERKUNG = "c_bemerkung";

	public static final String FLR_PERSONALZEITMODELL_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALZEITMODELL_D_GUELTIGAB = "t_gueltigab";
	public static final String FLR_PERSONALZEITMODELL_FLRZEITMODELL = "flrzeitmodell";

	public static final String FLR_SCHICHTZEITMODELL_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_SCHICHTZEITMODELL_FLRZEITMODELL = "flrzeitmodell";

	public static final String FLR_BETRIEBSKALENDER_D_DATUM = "t_datum";
	public static final String FLR_BETRIEBSKALENDER_C_BEZ = "c_bez";
	public static final String FLR_BETRIEBSKALENDER_FLRTAGESART = "flrtagesart";
	public static final String FLR_BETRIEBSKALENDER_FLRRELIGION = "flrreligion";

	public static final String FLR_FEIERTAG_I_TAG = "i_tag";
	public static final String FLR_FEIERTAG_I_MONAT = "i_monat";
	public static final String FLR_FEIERTAG_C_BEZ = "c_bez";
	public static final String FLR_FEIERTAG_I_OFFSET_OSTERSONNTAG = "i_offset_ostersonntag";
	public static final String FLR_FEIERTAG_FLRTAGESART = "flrtagesart";
	public static final String FLR_FEIERTAG_FLRRELIGION = "flrreligion";

	public static final String FLR_URLAUBSANSPRUCH_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_URLAUBSANSPRUCH_I_JAHR = "i_jahr";
	public static final String FLR_URLAUBSANSPRUCH_F_STUNDEN = "f_stunden";
	public static final String FLR_URLAUBSANSPRUCH_F_TAGE = "f_tage";
	public static final String FLR_URLAUBSANSPRUCH_F_STUNDENZUSAETZLICH = "f_stundenzusaetzlich";
	public static final String FLR_URLAUBSANSPRUCH_F_TAGEZUSAETZLICH = "f_tagezusaetzlich";
	public static final String FLR_URLAUBSANSPRUCH_F_JAHRESURLAUBINWOCHEN = "f_jahresurlaubinwochen";

	public static final String FLR_PERSONALGEHALT_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALGEHALT_I_JAHR = "i_jahr";
	public static final String FLR_PERSONALGEHALT_I_MONAT = "i_monat";
	public static final String FLR_PERSONALGEHALT_N_GEHALT = "n_gehalt";
	public static final String FLR_PERSONALGEHALT_F_UESTPAUSCHALE = "f_uestpauschale";
	public static final String FLR_PERSONALGEHALT_N_STUNDENSATZ = "n_stundensatz";

	public static final String FLR_PERSONALVERFUEGBARKEIT_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALVERFUEGBARKEIT_ARTIKEL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALVERFUEGBARKEIT_F_ANTEILPROZENT = "f_anteilprozent";
	public static final String FLR_PERSONALVERFUEGBARKEIT_FLRARTIKEL = "flrartikel";

	public static final String FLR_GLEITZEITSALDO_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_GLEITZEITSALDO_I_JAHR = "i_jahr";
	public static final String FLR_GLEITZEITSALDO_I_MONAT = "i_monat";
	public static final String FLR_GLEITZEITSALDO_N_SALDO = "n_saldo";
	public static final String FLR_GLEITZEITSALDO_N_SALDOMEHRST = "n_saldomehrstunden";
	public static final String FLR_GLEITZEITSALDO_N_SALDOUESTFREI50 = "n_saldouestfrei50";
	public static final String FLR_GLEITZEITSALDO_N_SALDOUESTPFLICHTIG50 = "n_saldouestpflichtig50";
	public static final String FLR_GLEITZEITSALDO_N_SALDOUESTFREI100 = "n_saldouestfrei100";
	public static final String FLR_GLEITZEITSALDO_N_SALDOUESTPFLICHTIG100 = "n_saldouestpflichtig100";
	public static final String FLR_GLEITZEITSALDO_N_SALDOUEST200 = "n_saldouest200";

	public static final String FLR_STUNDENABRECHNUNG_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_STUNDENABRECHNUNG_D_DATUM = "t_datum";
	public static final String FLR_STUNDENABRECHNUNG_N_MEHRSTUNDEN = "n_mehrstunden";
	public static final String FLR_STUNDENABRECHNUNG_N_UESTFREI50 = "n_uestfrei50";
	public static final String FLR_STUNDENABRECHNUNG_N_UESTPFLICHTIG50 = "n_uestpflichtig50";
	public static final String FLR_STUNDENABRECHNUNG_N_UESTFREI100 = "n_uestfrei100";
	public static final String FLR_STUNDENABRECHNUNG_N_UESTPFLICHTIG100 = "n_uestpflichtig100";
	public static final String FLR_STUNDENABRECHNUNG_N_UEST200 = "n_uest200";
	public static final String FLR_STUNDENABRECHNUNG_N_GUTSTUNDEN = "n_gutstunden";
	public static final String FLR_STUNDENABRECHNUNG_N_QUALIFIKATIONSPRAEMIE = "n_qualifikationspraemie";
	public static final String FLR_STUNDENABRECHNUNG_N_NORMALSTUNDEN = "n_normalstunden";

	public static final String PERSONALART_ANGESTELLTER = "Angestellter   ";
	public static final String PERSONALART_ARBEITER = "Arbeiter       ";
	public static final String PERSONALART_LEHRLING_ANGESTELLTER = "Lehrling Angest";
	public static final String PERSONALART_LEHRLING_ARBEITER = "Lehrling Arbeit";

	public static final String PERSONALFUNKTION_BUCHHALTUNG = "Buchhaltung         ";
	public static final String PERSONALFUNKTION_GESCHAEFTSFUEHRER = "Geschaeftsfuehrer   ";
	public static final String PERSONALFUNKTION_LOHNVERRECHNUNG = "Lohnverrechnung     ";
	public static final String PERSONALFUNKTION_ABTEILUNGSLEITER = "Abteilungsleiter    ";

	public static final String FAMILIENSTAND_LEDIG = "Ledig          ";
	public static final String FAMILIENSTAND_VERHEIRATET = "Verheiratet    ";
	public static final String FAMILIENSTAND_GESCHIEDEN = "Geschieden     ";
	public static final String FAMILIENSTAND_VERWITWET = "Verwitwet      ";

	public static final String ANGEHOERIGENART_EHEPARTNER = "Ehepartner     ";
	public static final String ANGEHOERIGENART_KIND = "Kind           ";
	public static final String ANGEHOERIGENART_LEBENSPARTNER = "Lebenspartner  ";

	public final static String REPORT_MODUL = "personal";

	public final static String REPORT_PERSONALLISTE = "pers_personalliste.jasper";
	public final static int REPORT_PERSONALLISTE_OPTION_SORTIERUNG_NAME = 0;
	public final static int REPORT_PERSONALLISTE_OPTION_SORTIERUNG_AUSWEIS = 1;
	public final static int REPORT_PERSONALLISTE_OPTION_SORTIERUNG_PERSONALNUMMER = 2;
	public final static int REPORT_PERSONALLISTE_OPTION_SORTIERUNG_ZUTRITTSKLASSE = 3;

	public static final String LOHNSTUNDENART_GST = "GST            ";
	public static final String LOHNSTUNDENART_NST = "NST            ";
	public static final String LOHNSTUNDENART_SOLL = "SOLL           ";
	public static final String LOHNSTUNDENART_MST = "MST            ";
	public static final String LOHNSTUNDENART_FTG = "FTG            ";
	public static final String LOHNSTUNDENART_U50F = "U50F           ";
	public static final String LOHNSTUNDENART_U50P = "U50P           ";
	public static final String LOHNSTUNDENART_U100F = "U100F          ";
	public static final String LOHNSTUNDENART_U100P = "U100P          ";
	public static final String LOHNSTUNDENART_GUT = "GUT            ";

	public static final String LOHNART_TYP_STUNDEN = "Stunden";

	public final static int REPORT_PERSONALLISTE_OPTION_SORTIERUNG_GEBURTSTAG = 4;

	public final static String REPORT_BARCODELISTE = "pers_barcodeliste.jasper";

	// Feldlaengen
	public static final int MAX_RELIGION_KENNUNG = 15;
	public static final int MAX_RELIGION_BEZEICHNUNG = 40;

	public static final int MAX_PENDLERPAUSCHALE_BEZEICHNUNG = 40;

	public static final int MAX_BERUF_BEZEICHNUNG = 40;

	public static final int MAX_KOLLEKTIV_BEZEICHNUNG = 40;

	public static final int MAX_LOHNGRUPPE_BEZEICHNUNG = 40;

	public static final int MAX_BETRIEBSKALENDER_BEZEICHNUNG = 80;

	public static final int MAX_PERSONALGEHALT_GRUNDKKSBEFREIT = 80;

	public static final int MAX_EINTRITTAUSTRITT_AUSTRITTSGRUND = 80;

	public static final int MAX_PERSONALZEITEN_BEMERKUNG = 80;

	public static final int MAX_PERSONALANGEHOERIGE_VORNAME = 40;
	public static final int MAX_PERSONALANGEHOERIGE_NACHNAME = 40;
	public static final int MAX_PERSONALANGEHOERIGE_SOZIALVERSNR = 30;

	public static final int MAX_PERSONAL_AUSWEIS = 80;
	public static final int MAX_PERSONAL_KURZZEICHEN = 3;
	public static final int MAX_PERSONAL_SOZIALVERSNR = 50;

	public static final int MAX_TAGESART_KENNUNG = 15;
	public static final int MAX_TAGESART_BEZEICHNUNG = 40;

	public Integer createPersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updatePersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException,
			EJBExceptionLP;

	public void updatePersonalKommentar(PersonalDto personalDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PartnerDtoSmall partnerDtoSmallFindByPersonalIId(
			Integer personalIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PersonalDto personalFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public PersonalDto personalFindByPrimaryKeySmall(Integer iId)
			throws EJBExceptionLP;

	public PersonalDto personalFindByPrimaryKeySmallOhneExc(Integer iId);

	public Integer createLohngruppe(LohngruppeDto lohngruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLohngruppe(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateLohngruppe(LohngruppeDto lohngruppeDto)
			throws EJBExceptionLP, RemoteException;

	public LohngruppeDto lohngruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createReligion(ReligionDto religionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeReligion(ReligionDto religionDto) throws EJBExceptionLP,
			RemoteException;

	public void updateReligion(ReligionDto religionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public ReligionDto religionFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PersonRpt getPersonRpt(Integer personalIId, TheClientDto theClientDto)
			throws RemoteException;

	public Integer createPersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePersonalangehoerige(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updatePersonalangehoerige(
			PersonalangehoerigeDto personalangehoerigeDto)
			throws EJBExceptionLP, RemoteException;

	public PersonalangehoerigeDto personalangehoerigeFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public Integer createKollektiv(KollektivDto kollektivDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKollektiv(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateKollektiv(KollektivDto kollektivDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprFamilienstaende(String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public HashMap<?, ?> getAllKurzzeichen() throws EJBExceptionLP,
			RemoteException;

	public Map<?, ?> getAllSprangehoerigenarten(String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprPersonalarten(String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public PersonalDto[] getAllPersonenOhneEintragInEintrittAustritt(
			TheClientDto theClientDto, Boolean bPlusVersteckte, Integer iOption)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllSprPersonalfunktionen(String cNrSpracheI)
			throws EJBExceptionLP, RemoteException;

	public KollektivDto kollektivFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createPendlerpauschale(
			PendlerpauschaleDto pendlerpauschaleDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePendlerpauschale(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updatePendlerpauschale(PendlerpauschaleDto pendlerpauschaleDto)
			throws EJBExceptionLP, RemoteException;

	public PendlerpauschaleDto pendlerpauschaleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createBeruf(BerufDto berufDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeBeruf(Integer iId) throws EJBExceptionLP, RemoteException;

	public void updateBeruf(BerufDto berufDto) throws EJBExceptionLP,
			RemoteException;

	public BerufDto berufFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public Integer createEintrittaustritt(
			EintrittaustrittDto eintrittaustrittDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map getAllLohnstundenarten();

	public void removeEintrittaustritt(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void updateEintrittaustritt(EintrittaustrittDto eintrittaustrittDto)
			throws EJBExceptionLP, RemoteException;

	public EintrittaustrittDto eintrittaustrittFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Boolean istPersonalAusgetreten(Integer personalIId,
			Timestamp tsZeitpunkt, TheClientDto theClientDto)
			throws RemoteException;

	public EintrittaustrittDto eintrittaustrittFindLetztenEintrittBisDatum(
			Integer personalIId, Timestamp dSucheBis) throws EJBExceptionLP,
			RemoteException;

	public EintrittaustrittDto eintrittaustrittFindByPersonalIIdDEintritt(
			Integer personalIId, Timestamp dEintritt) throws EJBExceptionLP,
			RemoteException;

	public PersonalDto personalFindByCPersonalnrMandantCNr(String cPersonalnr,
			String mandantCNr) throws RemoteException, EJBExceptionLP;

	public PersonalDto[] personalFindByCAusweisSortiertNachPersonalnr()
			throws EJBExceptionLP, RemoteException;

	public PersonalDto personalFindByCAusweis(String cAusweis)
			throws EJBExceptionLP, RemoteException;

	public PersonalDto[] personalFindByCAusweisSortiertNachCAusweis()
			throws EJBExceptionLP, RemoteException;

	public PersonalDto personalFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr) throws RemoteException, EJBExceptionLP;

	public PersonalDto personalFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr);

	public PersonalDto[] personalFindBySozialversichererPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public PersonalDto[] personalFindBySozialversichererPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public PersonalDto[] personalFindByFirmaPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public PersonalDto[] personalFindByFirmaPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr) throws RemoteException,
			EJBExceptionLP;

	public void removePersonal(PersonalDto personalDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void createPersonalart(PersonalartDto personalartDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void createPersonalfunktion(PersonalfunktionDto personalfunktionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createFamilienstand(FamilienstandDto familienstandDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void createAngehoerigenart(AngehoerigenartDto angehoerigenartDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePersonalart(String cNr) throws RemoteException,
			EJBExceptionLP;

	public void removePersonalart(PersonalartDto personalartDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalart(PersonalartDto personalartDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalarts(PersonalartDto[] personalartDtos)
			throws RemoteException, EJBExceptionLP;

	public void createPersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws RemoteException, EJBExceptionLP;

	public void removePersonalfunktion(String cNr) throws RemoteException,
			EJBExceptionLP;

	public void removePersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalfunktion(PersonalfunktionDto personalfunktionDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalfunktions(
			PersonalfunktionDto[] personalfunktionDtos) throws RemoteException,
			EJBExceptionLP;

	public PersonalfunktionDto personalfunktionFindByPrimaryKey(String cNr)
			throws RemoteException, EJBExceptionLP;

	public PersonalfunktionDto[] personalfunktionFindAll()
			throws RemoteException, EJBExceptionLP;

	public String getNextPersonalnummer(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
			Integer personaIId) throws EJBExceptionLP, RemoteException;

	public PersonalDto[] personalFindByMandantCNrPersonalfunktionCNr(
			String mandantCNr, String personalfunktionCNr)
			throws EJBExceptionLP, RemoteException;

	public PersonalDto[] personalFindByMandantCNr(String mandantCNr,
			Boolean bPlusVersteckte) throws EJBExceptionLP, RemoteException;

	public PersonalartDto personalartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto);

	public PersonalDto[] personalFindAllAngestellteEinesMandanten(
			String mandantCNr, Boolean bPlusVersteckte) throws EJBExceptionLP,
			RemoteException;

	public PersonalDto[] personalFindAllPersonenMeinerAbteilung(
			Integer kostenstelleIIdAbteilung, String mandantCNr,
			Boolean bPlusVersteckte);

	public PersonalDto[] personalFindAllArbeiterEinesMandanten(
			String mandantCNr, Boolean bPlusVersteckte) throws EJBExceptionLP,
			RemoteException;

	public Integer createPersonalzeiten(PersonalzeitenDto personalzeitenDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removePersonalzeiten(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalzeiten(PersonalzeitenDto personalzeitenDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalzeitens(PersonalzeitenDto[] personalzeitenDtos)
			throws RemoteException, EJBExceptionLP;

	public PersonalzeitenDto personalzeitenFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public Integer createPersonalgehalt(PersonalgehaltDto personalgehaltDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removePersonalgehalt(PersonalgehaltDto personalgehaltDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalgehalt(PersonalgehaltDto personalgehaltDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public PersonalgehaltDto personalgehaltFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public Integer createPersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto)
			throws RemoteException, EJBExceptionLP;

	public Integer createSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto);

	public SchichtzeitmodellDto schichtzeitmodellFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removeSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto);

	public void updateSchichtzeitmodell(
			SchichtzeitmodellDto schichtzeitmodellDto);

	public void removePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalzeitmodell(
			PersonalzeitmodellDto personalzeitmodellDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalzeitmodells(
			PersonalzeitmodellDto[] personalzeitmodellDtos)
			throws RemoteException, EJBExceptionLP;

	public PersonalzeitmodellDto personalzeitmodellFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP;

	public PersonalzeitmodellDto personalzeitmodellFindByPersonalIIdTDatumOhneExc(
			Integer personalIId, Timestamp tDatum) throws EJBExceptionLP,
			RemoteException;

	public PersonalzeitmodellDto personalzeitmodellFindByPersonalIIdTDatum(
			Integer personalIId, Timestamp tDatum) throws EJBExceptionLP,
			RemoteException;

	public PersonalzeitmodellDto personalzeitmodellFindZeitmodellZuDatum(
			Integer personalIId, Timestamp dDatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createBetriebskalender(
			BetriebskalenderDto betriebskalenderDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeBetriebskalender(BetriebskalenderDto betriebskalenderDto)
			throws RemoteException, EJBExceptionLP;

	public void updateBetriebskalender(BetriebskalenderDto betriebskalenderDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public BetriebskalenderDto betriebskalenderFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public BetriebskalenderDto betriebskalenderFindByMandantCNrDDatum(
			Timestamp dDatum, String mandantCNr, TheClientDto theClientDto);

	public BetriebskalenderDto[] betriebskalenderFindByMandantCNrTagesartCNr(
			String tagesartCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BetriebskalenderDto betriebskalenderFindByMandantCNrDDatumReligionIId(
			Timestamp dDatum, String mandantCNr, Integer religionIId)
			throws EJBExceptionLP, RemoteException;

	public Integer createUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto)
			throws RemoteException, EJBExceptionLP;

	public void updateUrlaubsanspruch(UrlaubsanspruchDto urlaubsanspruchDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public UrlaubsanspruchDto urlaubsanspruchFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public UrlaubsanspruchDto[] urlaubsanspruchFindLetztenUrlaubsanspruch(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP,
			RemoteException;

	public UrlaubsanspruchDto urlaubsanspruchFindByPersonalIIdIJahr(
			Integer personalIId, Integer iJahr) throws RemoteException,
			EJBExceptionLP;

	public UrlaubsanspruchDto[] urlaubsanspruchFindByPersonalIIdIJahrGroesser(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP,
			RemoteException;

	public UrlaubsanspruchDto[] urlaubsanspruchFindByPersonalIIdIJahrKleiner(
			Integer personalIId, Integer iJahr) throws EJBExceptionLP,
			RemoteException;

	public Integer createGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto)
			throws RemoteException, EJBExceptionLP;

	public void updateGleitzeitsaldo(GleitzeitsaldoDto gleitzeitsaldoDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public GleitzeitsaldoDto gleitzeitsaldoFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public GleitzeitsaldoDto gleitzeitsaldoFindByPersonalIIdIJahrIMonat(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws RemoteException, EJBExceptionLP;

	public GleitzeitsaldoDto gleitzeitsaldoFindLetztenGleitzeitsaldo(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP, RemoteException;

	public Integer createStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto) throws RemoteException,
			EJBExceptionLP;

	public void updateStundenabrechnung(
			StundenabrechnungDto stundenabrechnungDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP, EJBExceptionLP;

	public StundenabrechnungDto stundenabrechnungFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public StundenabrechnungDto stundenabrechnungFindByPersonalIIdDDatum(
			Integer personalIId, Timestamp dDatum) throws RemoteException,
			EJBExceptionLP;

	public StundenabrechnungDto[] stundenabrechnungFindByPersonalIIdIJahrIMonat(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP, RemoteException;

	public PersonalgehaltDto personalgehaltFindByPersonalIIdDGueltigab(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws RemoteException, EJBExceptionLP;

	public PersonalgehaltDto personalgehaltFindLetztePersonalgehalt(
			Integer personalIId, Integer iJahr, Integer iMonat)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printPersonalliste(Timestamp tsStichtag,
			boolean bMitBarcodes, boolean bMitVersteckten,
			int iOptionSortierung, TheClientDto theClientDto)
			throws RemoteException;

	public Integer createZulage(ZulageDto zulageDto) throws EJBExceptionLP,
			RemoteException;

	public void removeZulage(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeZulage(ZulageDto zulageDto) throws EJBExceptionLP,
			RemoteException;

	public void updateZulage(ZulageDto zulageDto) throws EJBExceptionLP,
			RemoteException;

	public int getAnzahlDerZeitmodelleEinerPerson(Integer personalIId);

	public ZulageDto zulageFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public Integer createArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP, RemoteException;

	public void removeArtikelzulage(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP, RemoteException;

	public void schichtzeitenVorausplanen(Integer personalIId,
			Integer zeitmodellIId1, Integer zeitmodellIId2,
			Integer zeitmodellIId3, Integer zeitmodellIId4, Integer iKwVon,
			Integer iKwBis, Integer iJahrVon, Integer iJahrBis,
			String tagesartCNrSchichtwechsel) throws RemoteException;

	public void updateArtikelzulage(ArtikelzulageDto artikelzulageDto)
			throws EJBExceptionLP, RemoteException;

	public ArtikelzulageDto artikelzulageFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createPersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws RemoteException, EJBExceptionLP;

	public Double getAuslastungEinerPerson(Integer personalIId)
			throws EJBExceptionLP, RemoteException;

	public void removePersonalverfuegbarkeit(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public void removePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws RemoteException, EJBExceptionLP;

	public void updatePersonalverfuegbarkeit(
			PersonalverfuegbarkeitDto personalverfuegbarkeitDto)
			throws RemoteException, EJBExceptionLP;

	public PersonalverfuegbarkeitDto personalverfuegbarkeitFindByPrimaryKey(
			Integer iId) throws RemoteException, EJBExceptionLP;

	public PersonalverfuegbarkeitDto[] personalverfuegbarkeitFindByPersonalIId(
			Integer personalIId) throws EJBExceptionLP, RemoteException;

	public Integer createKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws RemoteException, EJBExceptionLP;

	public void removeKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws RemoteException, EJBExceptionLP;

	public void updateKollektivuestd(KollektivuestdDto kollektivuestdDto)
			throws RemoteException, EJBExceptionLP;

	public KollektivuestdDto kollektivuestdFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public KollektivuestdDto[] kollektivuestdFindByKollektivIId(
			Integer kollektivId) throws EJBExceptionLP, RemoteException;

	public Integer createLohnart(LohnartDto lohnartDto);

	public void createLohnstundenart(LohnstundenartDto lohnstundenartDto);

	public void removeLohnart(Integer iId);

	public void removeLohnstundenart(String cNr);

	public void updateLohnstundenart(LohnstundenartDto lohnstundenartDto);

	public void updateLohnart(LohnartDto lohnartDto);

	public LohnartDto lohnartFindByPrimaryKey(Integer iId);

	public LohnstundenartDto lohnstundenartFindByPrimaryKey(Integer iId);

	public Integer createLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto);

	public LohnartstundenfaktorDto lohnartstundenfaktorFindByPrimaryKey(
			Integer iId);

	public void removeLohnartstundenfaktor(Integer iId);

	public void updateLohnartstundenfaktor(
			LohnartstundenfaktorDto lohnartstundenfaktorDto);

	public Integer createKollektivuestd50(
			Kollektivuestd50Dto kollektivuestd50Dto) throws EJBExceptionLP,
			RemoteException;

	public void removeKollektivuestd50(Kollektivuestd50Dto kollektivuestd50Dto)
			throws EJBExceptionLP, RemoteException;

	public void updateKollektivuestd50(Kollektivuestd50Dto kollektivuestd50Dto)
			throws EJBExceptionLP, RemoteException;

	public Kollektivuestd50Dto kollektivuestd50FindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Kollektivuestd50Dto kollektivuestd50FindByKollektivIIdTagesartIId(
			Integer kollektivIId, Integer tagesartIId) throws EJBExceptionLP,
			RemoteException;

	public Kollektivuestd50Dto[] kollektivuestd50FindByKollektivIId(
			Integer kollektivIId) throws EJBExceptionLP, RemoteException;

	public PersonalDto personalFindByCPersonalnrMandantCNrOhneExc(
			String cPersonalnr, String mandantCNr) throws EJBExceptionLP,
			RemoteException;

	public Integer createPersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto);

	public void removePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto);

	public void updatePersonalgruppekosten(
			PersonalgruppekostenDto personalgruppekostenDto);

	public PersonalgruppekostenDto personalgruppekostenFindByPrimaryKey(
			Integer iId);

	public BigDecimal getPersonalgruppeKostenZumZeitpunkt(
			Integer personalgruppeIId, java.sql.Timestamp tDatum);

	public PersonalgruppekostenDto personalgruppekostenFindByPersonalgruppeIIdTGueltigab(
			Integer personalgruppeIId, Timestamp tGueltigab);

	public Integer createPersonalgruppe(PersonalgruppeDto personalgruppeDto);

	public PersonalgruppeDto personalgruppeFindByPrimaryKey(Integer iId);

	public void updatePersonalgruppe(PersonalgruppeDto personalgruppeDto);

	public void removePersonalgruppe(PersonalgruppeDto personalgruppeDto);

	public PersonalDto[] personalFindByPersonalgruppeIdMandantCNr(
			Integer personalgruppe, String mandantCNr, boolean bPlusVersteckte);

	public Integer createBereitschaftart(BereitschaftartDto dto);

	public BereitschaftartDto bereitschaftartFindByPrimaryKey(Integer iId);

	public void updateBereitschaftart(BereitschaftartDto dto);

	public void removeBereitschaftart(BereitschaftartDto dto);

	public Integer createBereitschafttag(BereitschafttagDto dto);

	public BereitschafttagDto bereitschafttagFindByPrimaryKey(Integer iId);

	public void updateBereitschafttag(BereitschafttagDto dto);

	public void removeBereitschafttag(BereitschafttagDto dto);

	public void removeFeiertag(FeiertagDto feiertagDto);

	public void updateFeiertag(FeiertagDto feiertagDto,
			TheClientDto theClientDto);

	public Integer createFeiertag(FeiertagDto feiertagDto,
			TheClientDto theClientDto);

	public FeiertagDto feiertagFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void feiertageAusVorlageFuerJahrEintragen(Integer iJahr,
			TheClientDto theClientDto);

	public Integer createFahrzeug(FahrzeugDto dto, TheClientDto theClientDto);

	public void updateFahrzeug(FahrzeugDto dto, TheClientDto theClientDto);

	public FahrzeugDto fahrzeugFindByPrimaryKey(Integer iId);

	public void removeFahrzeug(FahrzeugDto dto);

	public Integer createFahrzeugkosten(FahrzeugkostenDto dto);

	public void updateFahrzeugkosten(FahrzeugkostenDto dto);

	public FahrzeugkostenDto fahrzeugkostenFindByPrimaryKey(Integer iId);

	public void removeFahrzeugkosten(FahrzeugkostenDto dto);

	public BigDecimal getKMKostenInZielwaehrung(Integer fahrzeugIId,
			java.util.Date datGueltigkeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto);

	public String getSignatur(Integer personalIId, String locale);

	public void updateSignatur(Integer personalIId, String xSignatur,
			TheClientDto theClientDto);

	public PersonalDto getPersonalDto_Vorgesetzter(Integer personalIId,
			TheClientDto theClientDto);

	public Integer createZeitabschluss(ZeitabschlussDto dto,
			TheClientDto theClientDto);

	public void updateZeitabschluss(ZeitabschlussDto zeitabschlussDto,
			TheClientDto theClientDto);

	public ZeitabschlussDto zeitabschlussFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto);

	public void removeZeitabschluss(ZeitabschlussDto zeitabschlussDto,
			TheClientDto theClientDto);

	PersonalDto[] personalFindByMandantCNrWithEmail(String mandantCNr,
			boolean bPlusVersteckte) throws EJBExceptionLP;
}
