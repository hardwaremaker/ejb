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
package com.lp.server.angebot.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.ejbfac.IAktivierbar;
import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface AngebotFac extends IAktivierbarControlled {
	// FLR Spaltennamen aus Hibernate Mapping
	public static final String FLR_ANGEBOT_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_ANGEBOT_T_NACHFASSTERMIN = "t_nachfasstermin";
	public static final String FLR_ANGEBOT_T_REALISIERUNGSTERMIN = "t_realisierungstermin";
	public static final String FLR_ANGEBOT_T_MANUELLERLEDIGT = "t_manuellerledigt";
	public static final String FLR_ANGEBOT_ANGEBOTSTATUS_C_NR = "angebotstatus_c_nr";
	public static final String FLR_ANGEBOT_WAEHRUNG_C_NR_ANGEBOTSWAEHRUNG = "waehrung_c_nr_angebotswaehrung";
	public static final String FLR_ANGEBOT_N_GESAMTANGEBOTSWERTINANGEBOTSWAEHRUNG = "n_gesamtangebotswertinangebotswaehrung";
	public static final String FLR_ANGEBOT_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_ANGEBOT_VERTRETER_I_ID = "vertreter_i_id";
	public static final String FLR_ANGEBOT_KUNDE_I_ID_ANGEBOTSADRESSE = "kunde_i_id_angebotsadresse";
	public static final String FLR_ANGEBOT_ANGEBOTERLEDIGUNGSGRUND_C_NR = "angeboterledigungsgrund_c_nr";
	public static final String FLR_ANGEBOT_F_AUFTRAGSWAHRSCHEINLICHEKEIT = "f_auftragswahrscheinlichkeit";
	public static final String FLR_ANGEBOT_FLRKUNDE = "flrkunde";
	public static final String FLR_ANGEBOT_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_ANGEBOT_FLRVERTRETER = "flrvertreter";
	public static final String FLR_ANGEBOT_FLRANGEBOTTEXTSUCHE = "flrangebottextsuche";
	public static final String FLR_ANGEBOT_F_WECHSELKURSMANDANTWAEHRUNGZUANGEBOTSWAEHRUNG = "f_wechselkursmandantwaehrungzuangebotswaehrung";
	public static final String FLR_ANGEBOT_FLRPERSONALANLEGER = "flrpersonalanleger";
	
	
	// Arten von Angebotsgueltigkeit
	public static final int ANGEBOTSGUELTIGKEIT_ENDEGESCHAEFTSJAHR = 0;
	public static final int ANGEBOTSGUELTIGKEIT_PARAMETERMANDANT = 1;

	/** Die Auswertung kann erfolgen nach ... */
	public static String KRIT_UEBERSICHT_GESCHAEFTSJAHR = "Geschaeftsjahr";
	public static String KRIT_UEBERSICHT_KALENDERJAHR= "Kalenderjahr";
	public static String KRIT_UEBERSICHT_VERTRETER_I_ID = "Vertreter";

	/** Die Eigenschaften fuer die FilterKriterien mussen festsetzen. */
	public static final int ANZAHL_KRITERIEN = 2;
	public static final int IDX_KRIT_GESCHAEFTSJAHR = 0;
	public static final int IDX_KRIT_VERTRETER_I_ID = 1;

	public Integer createAngebot(AngebotDto angebotDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public static final int IDX_KRIT_AUSWERTUNG = 0;
	public static final int IDX_KRIT_ANGEBOT = 1;

	public static final String KRIT_IDENT = "Ident";
	public static final String KRIT_PERSONAL = "Personal";
	public static final String KRIT_ANGEBOT_I_ID = "Angebot";
	public static final int ANZAHL_KRITERIEN_ANGEBOTZEITEN = 2;

	public void storniereAngebot(AngebotDto angebotDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void stornoAufheben(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void angebotErledigen(Integer iIdAngebotI,
			String cNrAngeboterledigungsgrundI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void erledigungAufheben(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void angebotManuellErledigen(Integer iIdAngebotI,
			String cNrAngeboterledigungsgrundI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean angebotManuellErledigendurchAuftrag(Integer iIdAngebotI,
			Integer iIdAuftragI, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void manuelleErledigungAufheben(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public boolean updateAngebot(AngebotDto angebotDtoI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAngebotOhneWeitereAktion(AngebotDto angebotDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	
	public BigDecimal berechneVerkaufswertSoll(Integer iIAngebotI,
			String sArtikelartI, TheClientDto theClientDto);
	
	public BigDecimal berechneGestehungswertSoll(Integer iIdAngebotI,
			String sArtikelartI, boolean bMitEigengefertigtenStuecklisten,
			TheClientDto theClientDto);
	
	/**
	 * @deprecated MB:
	 * @param iIdAngebotI
	 *            Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void pruefeUndSetzeAngebotstatusBeiAenderung(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getAngebotkennung(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public void aktiviereAngebot(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotDto angebotFindByPrimaryKey(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public AngebotDto angebotFindByPrimaryKeyOhneExec(Integer iIdAngebotI);

	public AngebotDto angebotFindByCNrMandantCNrOhneEx(String cnr, String mandantCnr);
	
	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNr(
			Integer iIdKundeI, String cNrMandantI,TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotDto[] angebotFindByKundeIIdAngebotsadresseMandantCNrOhneExc(
			Integer iIdKundeI, String cNrMandantI, TheClientDto theClientDto)
			throws RemoteException;

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNr(
			Integer iIdAnsprechpartnerI, String cNrMandantI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public AngebotDto[] angebotFindByAnsprechpartnerKundeIIdMandantCNrOhneExc(
			Integer iIdAnsprechpartnerI, String cNrMandantI,TheClientDto theClientDto)
			throws RemoteException;

	public BigDecimal berechneNettowertGesamt(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateAngebotKonditionen(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer erzeugeAngebotAusAngebot(Integer iIdAngebotI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer erzeugeAuftragAusAngebot(Integer iIdAngebotI,
			boolean bMitZeitDaten, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer erzeugeLieferscheinAusAngebot(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer erzeugeRechnungAusAngebot(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void setzeVersandzeitpunktAufJetzt(Integer iAngebotIId, String sDruckart);

}
