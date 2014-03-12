/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.bestellung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BestellpositionFac {

	// ofxml:2 Hier ist das letzt gueltige HV-Orderitem-Feature-Schema.
	public static final String SCHEMA_HV_FEATURE_DRUCKEN = "Drucken";
	public static final String SCHEMA_HV_FEATURE_BESTELLPOSITIONSTATUSCNR = "BestellpositionstatusCNr";
	public static final String SCHEMA_HV_FEATURE_MWSTSATZUEBERSTEUERT = "MwstsatzUebersteuert";
	public static final String SCHEMA_HV_FEATURE_MWSTSATZIID = "MwstsatzIId";
	public static final String SCHEMA_HV_FEATURE_NNETTOGESAMTPREIS = "NNettogesamtpreis";

	// FLR
	// flrextradata: 0 An dieser Position steht...
	public static final int FLR_EXTRA_DATA_WARENEINGANGIID = 0;

	// Bestellpositionart
	public static final String BESTELLPOSITIONART_IDENT = LocaleFac.POSITIONSART_IDENT;
	public static final String BESTELLPOSITIONART_HANDEINGABE = LocaleFac.POSITIONSART_HANDEINGABE;
	public static final String BESTELLPOSITIONART_TEXTEINGABE = LocaleFac.POSITIONSART_TEXTEINGABE;
	public static final String BESTELLPOSITIONART_BETRIFFT = LocaleFac.POSITIONSART_BETRIFFT;
	public static final String BESTELLPOSITIONART_LEERZEILE = LocaleFac.POSITIONSART_LEERZEILE;
	public static final String BESTELLPOSITIONART_SEITENUMBRUCH = LocaleFac.POSITIONSART_SEITENUMBRUCH;
	public static final String BESTELLPOSITIONART_TEXTBAUSTEIN = LocaleFac.POSITIONSART_TEXTBAUSTEIN;

	// Bestellpositionstatus
	public static final String BESTELLPOSITIONSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String BESTELLPOSITIONSTATUS_GELIEFERT = LocaleFac.STATUS_GELIEFERT;
	public static final String BESTELLPOSITIONSTATUS_BESTAETIGT = LocaleFac.STATUS_BESTAETIGT;
	public static final String BESTELLPOSITIONSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String BESTELLPOSITIONSTATUS_TEILGELIEFERT = LocaleFac.STATUS_TEILGELIEFERT;
	public static final String BESTELLPOSITIONSTATUS_ABGERUFEN = LocaleFac.STATUS_ABGERUFEN;
	public static final String BESTELLPOSITIONSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;

	public static final double MAX_D_MENGE = 100000000.;
	public static final double MIN_D_MENGE = 0.;
	public static final double MAX_D_EINZELPREIS = 10000000.;
	public static final double MIN_D_EINZELPREIS = -10000000.;
	public static final double MAX_D_RABATTSATZ = 1000.;
	public static final double MIN_D_RABATTSATZ = -1000;

	// Bestellposition
	public static final String FLR_BESTELLPOSITION_N_MENGE = "n_menge";
	public static final String FLR_BESTELLPOSITION_EINHEIT_C_NR = "einheit_c_nr";
	public static final String FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR = "bestellpositionart_c_nr";
	public static final String FLR_BESTELLPOSITION_BESTELLUNG_I_ID = "bestellung_i_id";
	public static final String FLR_BESTELLPOSITION_BESTELLPOSITIONSTATUS_C_NR = "bestellpositionstatus_c_nr";
	public static final String FLR_BESTELLPOSITION_I_SORT = "i_sort";
	public static final String FLR_BESTELLPOSITION_FLRBESTELLUNG = "flrbestellung";
	public static final String FLR_BESTELLPOSITION_T_UEBERSTEUERTERLIEFERTERMIN = "t_uebersteuerterliefertermin";
	public static final String FLR_BESTELLPOSITION_T_AUFTRAGSBESTAETIGUNGSTERMIN = "t_auftragsbestaetigungstermin";
	public static final String FLR_BESTELLPOSITION_N_NETTOGESAMTPREIS = "n_nettogesamtpreis";
	public static final String FLR_BESTELLPOSITION_X_TEXTINHALT = "c_textinhalt";
	public static final String FLR_BESTELLPOSITION_FLRARTIKEL = "flrartikel";
	public static final String FLR_BESTELLPOSITION_N_OFFENEMENGE = "n_offenemenge";
	public static final String FLR_BESTELLPOSITION_C_BEZEICHNUNG = "c_bezeichnung";

	// Preispflege
	static public String PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT = "unveraendert lassen";
	static public String PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN = "Einzelpreis anlegen";
	static public String PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN = "Staffelpreis anlegen";

	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI)
			throws EJBExceptionLP, RemoteException;

	public Integer createBestellposition(
			BestellpositionDto oBestellpositionDtoI, TheClientDto theClientDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern) throws EJBExceptionLP, RemoteException;

	public void removeBestellposition(BestellpositionDto bestellpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellpositionOhneWeitereAktion(
			BestellpositionDto bestellpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void manuellAufVollstaendigGeliefertSetzen(
			Integer iIdBestellpositionI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void versucheBestellpositionAufErledigtZuSetzen(
			Integer iIdBestellpositionI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void manuellErledigungAufheben(Integer iIdBestellpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellposition(BestellpositionDto bestellpositionDto,
			TheClientDto theClientDto, String sPreispflegeI,Integer artikellieferantstaffelIId_ZuAendern) throws EJBExceptionLP,
			RemoteException;

	public void updateBestellpositions(
			BestellpositionDto[] bestellpositionDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BestellpositionDto bestellpositionFindByPrimaryKey(
			Integer iIdBestellpositionI) throws EJBExceptionLP, RemoteException;

	public BestellpositionDto bestellpositionFindByPrimaryKeyOhneExc(
			Integer iIdBestellpositionI);
	
	public void vertauscheBestellpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;

	public void vertauscheBestellpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ; 
	
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdBestellungI, int iSortierungNeuePositionI)
			throws EJBExceptionLP, RemoteException;

	public BestellpositionDto[] bestellpositionFindByBestellung(
			Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer getAnzahlBestellpositionen(Integer iIdBestellungI)
			throws EJBExceptionLP, RemoteException;

	public int berechneAnzahlMengenbehafteteBestellpositionen(
			Integer iIdBestellungI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BestellpositionDto[] bestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin(
			Integer iIdArtikelI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void updateBestellpositionBeiLieferterminAenderung(
			Integer iIdbestellpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void refreshStatusWennAbgerufen(Integer iIdBesI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erzeugeAbrufpositionen(Integer iIdBestellungI, int iDivisorI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	
	public boolean enthaeltBestellungMaterialzuschlaege(Integer bestellungIId);
	
	public double berechneOffeneMenge(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BestellpositionDto bestellpositionFindByBestellungIIdBestellpositionIIdRahmenposition(
			Integer iIdBestellungI, Integer iIdBestellpositionRahmenpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean bIstKopfartikelEinesArtikelSets(Integer bestellpositionIId);
	
	public void updateAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI, String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateAbrufbestellpositionSichtRahmen(
			BestellpositionDto abrufbestellpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI,String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAbrufbestellposition(
			BestellpositionDto abrufbestellpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BestellpositionDto[] bestellpositionFindByBestellpositionIIdRahmenposition(
			Integer iIdRahmenpositionI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void checkStatusAbrufBestellungenUndRahmenbestellung(
			BestellpositionDto bestellpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateBestellpositionMitABTermin(
			BestellpositionDto bestellpositionDto, TheClientDto theClientDto,
			String sPreispflegeI) throws EJBExceptionLP, RemoteException;

	public void removeABTerminVonBestellposition(
			BestellpositionDto bestellpositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void setForAllPositionenABTermin(Integer bestellungIId,
			Date abDatum, String abNummer, boolean selectAllOrEmpty,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public int getAnzahlMengenbehaftetBSPOS(Integer iIdBestellungI,
			TheClientDto theClientDto) throws RemoteException;

	public BigDecimal berechneOffeneMenge(BestellpositionDto bestposDtoI)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneOffeneMenge(Integer iIdbsposI)
			throws RemoteException;

	public String getPositionAsStringDocumentWS(Integer[] aIIdBelegPosI,
			String CnrUserI) throws RemoteException;
	
	public void erledigeAlleNichtMengenpositionen(BestellungDto bestellungDto,
			TheClientDto theClientDto) throws RemoteException;
	
	public String getRichtigenBestellpositionStatus(Integer bestellPosIId, TheClientDto theClientDto) 
 		throws EJBExceptionLP, RemoteException;
	
	public BestellpositionDto[] bestellpositionenFindAll();
	
	public String checkBestellpositionStati(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public Integer getPositionNummer(Integer bestellpositionIId, TheClientDto theClientDto);
	
	public Integer getPositionNummerReadOnly(Integer bestellpositionIId) ;
	
	public void updateBestellpositionNurABTermin(Integer bestellpositionIId,
			java.sql.Date abTerminNeu, TheClientDto theClientDto);
	
	public void updateBestellpositionNurLieferterminBestaetigt(Integer bestellpositionIId, TheClientDto theClientDto);
	public Map<?,?> getListeDerVerknuepftenBestellungen(Integer lossollmaterialIId,
			TheClientDto theClientDto);
	public void sortiereNachArtikelnummer(Integer iIdBestellung,
			TheClientDto theClientDto);
	
	public void preispflege(BestellpositionDto besPosDto,
			String sPreispflegeI, Integer artikellieferantstaffelIId_ZuAendern,
			TheClientDto theClientDto);
	
}
