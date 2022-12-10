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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface VkPreisfindungFac {

	// fuer PK Generator
	public static final String VKPFARTIKELPREISLISTE = "Vkpfartikelpreisliste";

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_VKPFARTIKELPREISLISTE_I_ID = "i_id";
	public static final String FLR_VKPFARTIKELPREISLISTE_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_VKPFARTIKELPREISLISTE_I_SORT = "i_sort";
	public static final String FLR_VKPFARTIKELPREISLISTE_C_NR = "c_nr";
	public static final String FLR_VKPFARTIKELPREISLISTE_WAEHRUNG_C_NR = "waehrung_c_nr";
	public static final String FLR_VKPFARTIKELPREISLISTE_B_PREISLISTEAKTIV = "b_preislisteaktiv";
	public static final String FLR_VKPFARTIKELPREISLISTE_N_STANDARDRABATTSATZ = "n_standardrabattsatz";

	public static final String FLR_VKPFSTAFFELMENGE_N_MENGE = "n_menge";
	public static final String FLR_VKPFSTAFFELMENGE_N_FIXPREIS = "n_fixpreis";
	public static final String FLR_VKPFSTAFFELMENGE_F_RABATTSATZ = "f_rabattsatz";
	public static final String FLR_VKPFSTAFFELMENGE_N_BERECHNETERPREIS = "n_berechneterpreis";
	public static final String FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGAB = "t_preisgueltigab";
	public static final String FLR_VKPFSTAFFELMENGE_T_PREISGUELTIGBIS = "t_preisgueltigbis";
	public static final String FLR_VKPFSTAFFELMENGE_FLRVKPFARTIKELPREISLISTE = "flrvkpfartikelpreisliste";

	// Laengen.
	public static int MAX_CNR = 40; // Preislisten

	// Testaufbau
	public static String PREISLISTE_FOO = "Endkunde";

	public Integer createVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeVkPreisfindungPreisliste(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public void removeVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto)
			throws EJBExceptionLP, RemoteException;

	public void updateVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VkPreisfindungPreislisteDto vkPreisfindungPreislisteFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public VkPreisfindungPreislisteDto[] vkPreisfindungPreislisteFindByArtikelIId(
			Integer artikelIId) throws EJBExceptionLP, RemoteException;

	public VkPreisfindungPreislisteDto getAktuellePreislisteByArtikelIIdPreislisteIId(
			Integer iIdArtikelI, Integer iIdVkpfartikelpreislisteI,
			Date datGueltigabI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VkPreisfindungPreislisteDto getNeuestePreislisteByArtikelPreislistenname(
			Integer iiArtikelI, Integer iiNameI) throws EJBExceptionLP,
			RemoteException;

	public Integer createVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void pflegeRabattsaetzeNachpflegen(Integer preislisteIId,
			Date tPreisgueltigab, TheClientDto thClientDto);

	public void updateVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkpfartikelverkaufspreisbasisDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void pflegeVkpreise(Integer artikelgruppeIId,
			Integer vkpfartikelpreislisteIId, Date tGueltigab,
			BigDecimal prozent, String cBegruendung, TheClientDto theClientDto)
			throws RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByPrimaryKey(
			Integer iIdVkpfartikelverkaufspreisbasisI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto[] einzelverkaufspreisFindByMandantCNrArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByMandantCNrArtikelIIdGueltigab(
			Integer iIdArtikelI, Date datGueltigabI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto getNeuestenArtikeleinzelverkaufspreis(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkPreisfindungPreislisteDto getAktuellePreislisteByPreislistenname(
			Integer pkPreislistenname) throws EJBExceptionLP, RemoteException;

	public VerkaufspreisDto getVerkaufspreisInFremdwaehrung(
			VerkaufspreisDto pPreisInMandantenwhg, BigDecimal pKurs)
			throws RemoteException;

	public VerkaufspreisDto getVerkaufspreisInMandantenwaehrung(
			VerkaufspreisDto pPreisInFremdwhg, BigDecimal pKurs)
			throws RemoteException;

	public VkPreisfindungPreislisteDto preislisteFindByUniqueKey(
			Integer iiPreislisteI, Integer iiArtikelI, Date datGueltigabI)
			throws RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto getArtikeleinzelverkaufspreis(
			Integer iIdArtikelI, java.sql.Date datGueltikeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer getMaxISort(String sMandantI) throws EJBExceptionLP,
			RemoteException;

	public void vertauscheVkpfartikelpreisliste(Integer iIdPreisliste1I,
			Integer iIdPreisliste2I) throws EJBExceptionLP, RemoteException;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			String sMandantI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public boolean liegtVerkaufspreisUnterMinVerkaufspreis(Integer iIdArtikelI,
			Integer iIdLagerI, BigDecimal bdPreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMenge,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			boolean bRabattsatzAendern, java.sql.Date datumGueltigNeu,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public VkpfartikelpreislisteDto[] vkpfartikelpreislisteFindByMandantCNr(
			String mandantCNrI) throws EJBExceptionLP, RemoteException;

	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByMandantCNrAndCNr(
			String mandantCNrI, String cNrI) throws EJBExceptionLP,
			RemoteException;

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislisten(
			Short bPreislisteaktivI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislistenMitHinterlegtemArtikelpreis(
			Integer iIdArtikelI, Short bPreislisteaktivI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VkPreisfindungEinzelverkaufspreisDto[] vkpfartikelverkaufspreisbasisFindByArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VerkaufspreisDto berechnePreisfelder(BigDecimal bdEinzelpreisI,
			Double ddRabattsatzI, Double ddZusatzrabattsatzI,
			Integer iIdMwstsatzI, int iAnzahlStellenI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws RemoteException;

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByUniqueKey(
			Integer iIdArtikelI, BigDecimal nMengeI, Date datGueltigAbI,
			Integer vkpfartikelpreislisteIId) throws EJBExceptionLP,
			RemoteException;

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdNMenge(
			Integer iIdArtikelI, BigDecimal nMengeI, Integer preislisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
			Integer iIdArtikelI, BigDecimal nMengeI,
			Date datGueltigkeitsdatumI, Integer preislisteIId,
			TheClientDto theClientDto) throws RemoteException;

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
			Integer iIdArtikelI, Date datGueltigkeitI, Integer preislisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, Integer preislisteIId,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, BigDecimal nMengeI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal ermittlePreisbasisStaffelmenge(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, BigDecimal nMengeI,
			TheClientDto theClientDto) throws RemoteException;

	public VkpreisfindungDto verkaufspreisfindung(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto);

	public VkpreisfindungDto verkaufspreisfindung(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung,
			boolean bMitMaterialzuschlagImNettopreis, TheClientDto theClientDto);

	public VkpreisfindungDto verkaufspreisfindungStufe1(Integer artikelIId,
			Date tGueltigkeitsdatumI, Integer iIdPreislisteI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			BigDecimal nMengeI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto);

	public VkpreisfindungDto verkaufspreisfindungStufe2(ArtikelDto artikelDtoI,
			BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto);

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneEinzelVkpreisEinerMengenstaffel(
			Integer vkpfstaffelmengeIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) throws RemoteException;

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI, BigDecimal nMaterialzuschlag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI, BigDecimal nMaterialzuschlag);

	public VkpreisfindungDto verkaufspreisfindungStufe3(ArtikelDto artikelDtoI,
			Integer iIdKundeI, BigDecimal nMengeI, Date tGueltigkeitsdatumI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public KundesokomengenstaffelDto ermittleKundesokomengenstaffel(
			ArtikelDto artikelDtoI, Integer iIdKundeI, BigDecimal nMengeI,
			Date tGueltigkeitsdatumI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean liegtVerkaufpreisUnterMinVerkaufpsreis(Integer iIdArtikelI,
			BigDecimal bdVerkaufspreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMenge,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void pruefeVkpfStaffelmenge(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map getAlleAktivenPreislistenMitVkPreisbasis(
			TheClientDto theClientDto);

	public VkPreisfindungPreislisteDto getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
			Integer artikelIId, Integer preislisteIId,
			Date datGueltikeitsdatumI, TheClientDto theClientDto);

	public VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab(
			Integer artikelIId, Date tGueltigab, TheClientDto theClientDto);

	/**
	 * Die Verkaufspreisfindung f&uuml;r das Web ber&uuml;cksichtigt einen
	 * eventuell nicht vorhandenen Kunden (iIdKundeI == null) und
	 * l&auml;&szlig;t in diesem Falle die Ermittlung der vkStufe3 aus.
	 * 
	 * @param iIdArtikelI
	 * @param iIdKundeI
	 * @param nMengeI
	 * @param datGueltigkeitsdatumI
	 * @param iIdPreislisteI
	 * @param iIdMwstsatzBezI
	 * @param waehrungCNrZielwaehrung
	 * @param theClientDto
	 * @return die Preisinfo
	 */
	public VkpreisfindungDto verkaufspreisfindungWeb(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzBezI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws RemoteException;

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdFuerVKPreisentwicklung(
			Integer iIdArtikelI, TheClientDto theClientDto);

	public void fixpreiseAllerMengenstaffelnEinesArtikelErhoehen(
			Integer artikelIId, Date tGueltigab, BigDecimal nProzent,
			TheClientDto theClientDto);

	public VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisfindByPrimaryKey(
			Integer iId, TheClientDto theClientDto);
	
	public VkpreisfindungDto verkaufspreisfindungOhneExc(Integer iIdArtikelI, Integer iIdKundeI, BigDecimal nMengeI,
			Date datGueltigkeitsdatumI, Integer iIdPreislisteI, Integer iIdMwstsatzI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto);
	
}
