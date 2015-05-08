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
package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BuchenFac {

	public final static String HabenBuchung = "HABEN          ";
	public final static String SollBuchung = "SOLL           ";

	// nicht zulaessig!
	// public void removeBuchung(BuchungDto buchungDto, TheClientDto
	// theClientDto)
	// throws EJBExceptionLP, RemoteException;

	public void updateBuchung(BuchungDto buchungDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BuchungDto buchungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	// nicht zulaessig
	// public void removeBuchungdetail(BuchungdetailDto buchungdetailDto,
	// TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBuchungdetail(BuchungdetailDto buchungdetailDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BuchungdetailDto buchungdetailFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BuchungdetailDto[] buchungdetailFindByKontoIId(Integer kontoIId)
			throws EJBExceptionLP, RemoteException;

	public Boolean hatPartnerBuchungenAufKonto(Integer partnerIId,
			Integer kontoIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public String getBelegnummerUmbuchung(Integer geschaeftsjahr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getBelegnummerKassenbuch(Integer geschaeftsjahr,
			TheClientDto theClientDto) throws EJBExceptionLP;

	public BigDecimal getHabenVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, TheClientDto theClientDto);

	public BigDecimal getSollVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, TheClientDto theClientDto);

	public BigDecimal getHabenVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, boolean kummuliert, TheClientDto theClientDto);

	public BigDecimal getSollVonKonto(Integer kontoIId, int geschaeftsjahr,
			int periode, boolean kummuliert, TheClientDto theClientDto);

	public BigDecimal getSaldoOhneEBVonKonto(Integer kontoIId,
			int geschaftsjahr, int periode, TheClientDto theClientDto);

	public BigDecimal getSaldoVonKontoMitEB(Integer kontoIId,
			int geschaftsjahr, int periode, TheClientDto theClientDto);

	public BigDecimal getSaldoOhneEBVonKonto(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			TheClientDto theClientDto);

	public BigDecimal getHabenVonKontoByAuszug(Integer kontoIId,
			int geschaeftsjahr, Integer iAuszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto);

	public BigDecimal getSollVonKontoByAuszug(Integer kontoIId,
			int geschaeftsjahr, Integer iAuszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto);

	public BigDecimal getSaldoVonKontoByAuszug(Integer kontoIId,
			int geschaftsjahr, Integer iAuszugBisInklusiv, boolean kummuliert,
			boolean inklEB, TheClientDto theClientDto);

	public BigDecimal getSaldoVonKontoInWaehrung(Integer kontoIId,
			int geschaftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBuchung(BuchungDto buchungDto,
			BuchungdetailDto[] buchungdetailDtos, boolean pruefeBuchungsregeln)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBuchungswerte(BuchungdetailDto[] buchungDetailDtos)
			throws EJBExceptionLP, RemoteException;

	public void pruefeBuchungsregeln(BuchungDto buchungDto,
			BuchungdetailDto[] buchungDetailDtos) throws RemoteException;

	public BuchungDto verbucheUmbuchung(BuchungDto buchungDto,
			BuchungdetailDto[] buchungen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BuchungDto buchen(BuchungDto buchungDto,
			BuchungdetailDto[] buchungdetailDtos, boolean pruefeBuchungsregeln,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getListeDerGegenkonten(Integer buchungdetailIId,
			TheClientDto theClientDto);

	public BigDecimal getSummeEroeffnungKontoIId(Integer kontoIId,
			int geschaeftsjahr, int periode, boolean kummuliert,
			TheClientDto theClientDto);

	public BigDecimal getSummeEroeffnungKontoIIdInWaehrung(Integer kontoIId,
			int geschaeftsjahr, int periode, boolean kummuliert,
			String waehrungCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void storniereBuchung(Integer buchungIId, TheClientDto theClientDto);

	public Integer findGeschaeftsjahrFuerDatum(Date zahldatum, String mandantCnr);

	public Timestamp[] getDatumbereichPeriodeGJ(Integer geschaeftsjahr,
			int periode, TheClientDto theClientDto);

	public Timestamp[] getDatumVonBisGeschaeftsjahr(Integer geschaeftsjahr,
			TheClientDto theClientDto);

	public Integer getPeriodeImGJFuerDatum(Date date, String mandantCnr);

	public BuchungDto verbucheKassenbuchung(BuchungDto buchungDto,
			BuchungdetailDto buchungdetailDto, Integer kassenKontoIId,
			KassenbuchungsteuerartDto kstDto, TheClientDto theClient);

	public BuchungdetailDto[] buchungdetailsFindByBuchungIId(Integer buchungIId);

	public List<BuchungdetailDto> buchungdetailsFindByKontoIIdBuchungIId(
			Integer kontoIId, Integer buchungIId);

	public KassenbuchungsteuerartDto getKassenbuchungSteuerart(
			Integer buchungIId, TheClientDto theClientDto);

	public BigDecimal getSaldoVonKontoByAusziffern(Integer kontoIId,
			int geschaeftsjahr, Integer auszifferkennzeichen,
			TheClientDto theClient);

	public List<Integer> getOffenePosten(Integer kontoIId, String kontotypCNr,
			int geschaeftsjahr, TheClientDto theClientDto);

	/**
	 * Erzeugen eines Saldovortrags/Perioden&uuml;bernahme f&uuml;r ein Personenkonto</br>
	 * Die vorzutragenden Buchungsdetails(iids) m&uuml;ssen im saldovortragModel
	 * &uuml;bergeben werden.
	 *
	 * @param saldovortragModel
	 * @param theClientDto
	 * @throws RemoteException
	 */
	public void createSaldovortragsBuchung(
			SaldovortragModelPersonenKonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Erzeugen eines Saldovortrags/Perioden&uuml;bernahme f&uuml;r ein Personenkonto</br>
	 * Die vorzutragenden Buchungsdetails werden automatisch ermittelt.
	 *
	 * @param saldovortragModel
	 * @param theClientDto
	 * @throws RemoteException
	 */
	public void createSaldovortragsBuchungErmittleOP(
			SaldovortragModelPersonenKonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Erzeuge fuer alle Personenkonten des angegebenen Typs die
	 * Vortragsbuchungen</br> Jedes Konto wird in einer eigenen Transaktion
	 * ausgef&uuml;hrt.
	 *
	 * @param kontotyp
	 *            FinanzServiceFac.KONTOTYP_DEBITOR bzw. KONTOTYP_KREDITOR
	 * @param geschaeftsJahr
	 *            die &UUml;bernahme f&uuml;r das betreffende Gesch&auml;ftsjahr durchf&uuml;hren
	 * @param deleteManualEB
	 *            true, wenn vom Anwender erzeugte EB gel&ouml;scht werden d&uuml;rfen
	 * @param theClientDto
	 * @throws RemoteException
	 */
	public void createSaldovortragsBuchungErmittleOP(String kontotyp,
			int geschaeftsJahr, boolean deleteManualEB,
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Erzeugen eines Saldovortrags/Perioden&uuml;bernahme f&uuml;r ein Sachkonto
	 *
	 * @param saldovortragModel
	 * @param theClientDto
	 * @throws RemoteException
	 */
	public void createSaldovortragsBuchung(
			SaldovortragModelSachkonto saldovortragModel,
			TheClientDto theClientDto) throws RemoteException;

	/**
	 * Nur f&uuml;r die Interne Verwendung
	 *
	 * @param vortragModel
	 * @param theClientDto
	 * @throws RemoteException
	 */
	public void eachCreateSaldovortragsBuchungErmittleOP(
			SaldovortragModelPersonenKonto vortragModel,
			TheClientDto theClientDto) throws RemoteException;

	public boolean hatKontoBuchungen(Integer kontoIId);

	public boolean isUvaVerprobt(Integer buchungIId);

	public BuchungdetailDto[] buchungdetailsFindByBuchungIIdOhneMitlaufende(
			Integer buchungIId, TheClientDto theClientDto);

	public HashMap<String,Integer> storniereFinanzamtsbuchungen(int geschaeftsjahr,
			Date buchungsDatum, int finanzamtIId, TheClientDto theClientDto);

	public void setAuszifferungenFinanzamtsbuchungen(int geschaeftsjahr,
			Date buchungsDatum, int finanzamtIId, HashMap<String, Integer> hmAZ, TheClientDto theClientDto);

	public boolean isKontoMitEBKonsistent(Integer kontoIId, int geschaeftsjahr,
			TheClientDto theClientDto);

	public BigDecimal getSaldoUVAOhneEBVonKonto(Integer i_id,
			int iGeschaeftsjahr, int iPeriode, TheClientDto theClientDto);

	/**
	 * Holt den Saldo eines Kontos und ber&uuml;cksichtigt <b>alle</b> Buchungen,
	 * deren Buchungsdatum vor dem aktuellen Zeitpunkt liegt.
	 * @param kontoIId
	 * @return den Saldo
	 */
	public BigDecimal getAktuellenSaldoVonKonto(Integer kontoIId);

	/**
	 * Holt den Saldo eines Kontos f&uuml;r ein Gesch&auml;ftsjahr
	 * deren Buchungsdatum vor dem aktuellen Zeitpunkt liegt.
	 * @param kontoIId
	 * @param geschaftsjahrIId
	 * @return der aktuelle Saldo des Kontos
	 */
	public BigDecimal getAktuellenSaldoVonKontoFuerGeschaeftsjahr(Integer kontoIId, Integer geschaftsjahrIId);
}
