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
package com.lp.server.lieferschein.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;
import javax.naming.NamingException;

import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LieferscheinFac extends IAktivierbarControlled {

	// Tabellennamen
	public static final String LS_LIEFERSCHEIN = "LS_LIEFERSCHEIN";

	// Lieferscheinstatus
	public static final String LSSTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String LSSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String LSSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String LSSTATUS_GELIEFERT = LocaleFac.STATUS_GELIEFERT;
	public static final String LSSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String LSSTATUS_VERRECHNET = LocaleFac.STATUS_VERRECHNET;

	// Lieferscheinart
	public static final String LSART_AUFTRAG = "Auftrag        ";
	public static final String LSART_FREI = "Frei           ";
	public static final String LSART_LIEFERANT = "Lieferant      ";

	public static final String LSART_LIEFERANT_SHORT = "L";
	// Konstanten fuer die Uebersichtspanels im Lieferschein
	public static final String KRIT_JAHRESZAHL = "Jahreszahl";
	public static final String KRIT_MONAT = "Monat";
	public static final String KRIT_TAG = "Tag";
	public static final String KRIT_WOCHE = "Woche";

	public static final int ANZAHL_KRITERIEN = 2;
	public static final int IDX_KRIT_JAHRESZAHL = 0;
	public static final int IDX_KRIT_ZEITEINHEIT = 1;

	public static final int LS_UMSATZ_ANZAHL_KRITERIEN = 1;
	public static final int LS_UMSATZ_IDX_KRIT_ZEITEINHEIT = 0;

	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_LIEFERSCHEIN_I_ID = "i_id";
	public static final String FLR_LIEFERSCHEIN_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_LIEFERSCHEIN_C_NR = "c_nr";
	public static final String FLR_LIEFERSCHEINART_C_NR = "lieferscheinart_c_nr";
	public static final String FLR_LIEFERSCHEIN_C_BEZ_PROJEKTBEZEICHNUNG = "c_bez_projektbezeichnung";
	public static final String FLR_LIEFERSCHEIN_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_LIEFERSCHEIN_D_BELEGDATUM = "d_belegdatum";
	public static final String FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR = "lieferscheinstatus_status_c_nr";
	public static final String FLR_LIEFERSCHEIN_LAGER_I_ID = "lager_i_id";
	public static final String FLR_LIEFERSCHEIN_ZIELLAGER_I_ID = "ziellager_i_id";
	public static final String FLR_LIEFERSCHEIN_WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG = "waehrung_c_nr_lieferscheinwaehrung";
	public static final String FLR_LIEFERSCHEIN_B_VERRECHENBAR = "b_verrechenbar";
	public static final String FLR_LIEFERSCHEIN_FLRRECHNUNG = "flrrechnung";
	public static final String FLR_LIEFERSCHEIN_FLRKUNDE = "flrkunde";
	public static final String FLR_LIEFERSCHEIN_FLRKUNDERECHNUNGSADRESSE = "flrkunderechnungsadresse";
	public static final String FLR_LIEFERSCHEIN_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_LIEFERSCHEIN_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_LIEFERSCHEIN_KUNDE_I_ID_LIEFERADRESSE = "kunde_i_id_lieferadresse";
	public static final String FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE = "kunde_i_id_rechnungsadresse";
	public static final String FLR_LIEFERSCHEIN_FLRPERSONALANLEGEN = "flrpersonalanlegen";
	public static final String FLR_LIEFERSCHEIN_F_WECHSELKURSMANDANTWAEHRUNGZULIEFERSCHEINWAEHRUNG = "f_wechselkursmandantwaehrungzulieferscheinwaehrung";
	public static final String FLR_LIEFERSCHEIN_FLRVERTRETER = "flrvertreter";
	public static final String FLR_LIEFERSCHEIN_FLRPERSONALANLEGER = "flrpersonalanleger";
	public static final String FLR_LIEFERSCHEIN_PERSONAL_I_ID_VERTRETER ="personal_i_id_vertreter";
	public static final String FLR_LIEFERSCHEIN_AUFTRAG_I_ID ="auftrag_i_id";
	

	public Integer createLieferschein(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLieferschein(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateTAendern(Integer lieferscheinIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean updateLieferschein(LieferscheinDto lieferscheinDtoI,
			Integer[] aAuftragIIdI, String waehrungOriCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinDto lieferscheinFindByPrimaryKey(
			Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public LieferscheinDto lieferscheinFindByPrimaryKey(
			Integer iIdLieferscheinI) throws EJBExceptionLP ;
	
	public LieferscheinDto lieferscheinFindByCNrMandantCNr(String cNr, String mandantCNr);
	
	public LieferscheinDto lieferscheinFindByPrimaryKeyOhneExc(
			Integer iIdLieferscheinI);

	public LieferscheinDto[] lieferscheinFindByAuftrag(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinDto[] lieferscheinFindByKundeIIdLieferadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinDto[] lieferscheinFindByKundeIIdRechnungsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	// public LieferscheinauftragmappingDto[]
	// lieferscheinauftragmappingFindByLieferscheinIId(
	// Integer iIdLieferscheinI, TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException;

	public String getLieferscheinstatus(String pStatus, Locale locale1,
			Locale locale2) throws EJBExceptionLP, RemoteException;
	
	public String getLieferscheinCNr(Object[] keys) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getLieferscheinpositionart(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getLieferscheinart(Locale locale1, Locale locale2)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinpositionDto getLieferscheinpositionByLieferscheinAuftragposition(
			Integer iIdLieferscheinI, Integer iIdAuftragpositionI)
			throws EJBExceptionLP, RemoteException;
	
	public void aktiviereLieferschein(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeStatusLieferschein(Integer iIdLieferscheinI,
			String sStatusI, Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGestehungswert(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGestehungswertEinerLieferscheinposition(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGestehungswertEinerLieferscheinposition(
			Integer iIdlieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGesamtwertLieferschein(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneGesamtwertLieferscheinAusPositionen(
			Integer iIdLieferscheinI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLieferscheintext(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheintext(LieferscheintextDto lieferscheintextDto)
			throws EJBExceptionLP, RemoteException;

	public void updateLieferscheintexts(
			LieferscheintextDto[] lieferscheintextDtos) throws EJBExceptionLP,
			RemoteException;

	public LieferscheintextDto lieferscheintextFindByPrimaryKey(Integer iiIIdI)
			throws EJBExceptionLP, RemoteException;

	public LieferscheintextDto lieferscheintextFindByMandantLocaleCNr(
			String sLocaleI, String sCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public LieferscheinDto[] lieferscheinFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public int berechneAnzahlDerOffenenLieferscheine() throws EJBExceptionLP,
			RemoteException;

	public void pruefeUndSetzeLieferscheinstatusBeiAenderung(
			Integer iIdLieferscheinI) throws EJBExceptionLP, RemoteException;

	public void updateLieferscheinKonditionen(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer zaehleLieferscheinFuerUmsatz(GregorianCalendar gcVonI,
			GregorianCalendar gcBisI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal berechneUmsatz(GregorianCalendar gcVonI,
			GregorianCalendar gcBisI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void manuellFreigeben(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean enthaeltLieferscheinNullPreise(Integer lieferscheinIId);
	
	public void verrechnen(Integer iIdLieferscheinI, Integer iIdRechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void stornieren(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneVerkaufswertIst(Integer iIdLieferscheinI,HashMap lieferscheinpositionIIds,
			String sArtikelartI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal berechneGestehungswertIst(Integer iIdLieferscheinI,HashMap lieferscheinpositionIIds,
			String sArtikelartI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal berechneOffenenLieferscheinwert(Integer kundeIId, TheClientDto theClientDto);
	
	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktion(
			Integer iIdLieferscheinI, Integer auftragIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew (
			Integer iIdLieferscheinI, Integer auftragIIdI, List<Artikelset> artikelsets,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;

	public void updateLieferscheinOhneWeitereAktion(
			LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setzeVersandzeitpunktAufJetzt(Integer iLieferscheinIId, String sDruckart);
	
	public void updateLieferscheinBegruendung(Integer lieferscheinIId, Integer begruendungIId, TheClientDto theClientDto);
	public void toggleZollexportpapiereErhalten(Integer lieferscheinIId,
			String cZollexportpapier, Integer eingangsrechnungIId_Zollexport,
			TheClientDto theClientDto);
	
	ILieferscheinAviso createLieferscheinAviso(
			Integer lieferscheinIId, TheClientDto theClientDto) throws NamingException, RemoteException ;	

	String lieferscheinAvisoToString(
			LieferscheinDto lieferscheinDto, ILieferscheinAviso lieferscheinAviso, 
			TheClientDto theClientDto) throws RemoteException, NamingException ;
	
	/**
	 * Ein LieferscheinAviso erzeugen und dessen Inhalt als String liefern
	 * 
	 * @param lieferscheinIId
	 * @param theClientDto
	 * @return das Aviso"Dokument" als (XML) String
	 * @throws RemoteException
	 * @throws NamingException
	 */
	String createLieferscheinAvisoToString(
			Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException, NamingException ;

	/**
	 * Den Zeitstempel eines Lieferscheinavisos zuruecksetzen</br>
	 * <p>Damit ist die Erzeugung eines neuen Aviso moeglich</p>
	 * @param lieferscheinIId
	 * @param theClientDto
	 * @throws RemoteException
	 */
	void resetLieferscheinAviso(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException ;

	/**
	 * Ein LieferscheinAviso erzeugen und versenden 
	 * 
	 * @param lieferscheinIId
	 * @param theClientDto
	 * @throws RemoteException
	 * @throws NamingException
	 * @return den Aviso Inhalt als "String"
	 */
	String createLieferscheinAvisoPost(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException, NamingException ;
	
	/**
	 * Gibt es fuer den Empfaenger der Lieferung einen externen Versandweg?
	 *
	 * @param lieferscheinDto
	 * @param theClientDto
	 * @return true wenn es einen externen Versandweg (Clevercure, EDI, ...) gibt
	 * 
	 * @throws RemoteException
	 */
	boolean hatLieferscheinVersandweg(LieferscheinDto lieferscheinDto, TheClientDto theClientDto) throws RemoteException ;	

	boolean hatLieferscheinVersandweg(Integer lieferscheinIId, TheClientDto theClientDto) throws RemoteException ;	

	List<Integer> repairLieferscheinZws2276GetList(TheClientDto theClientDto) ;
	void repairLieferscheinZws2276(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException ;
}
