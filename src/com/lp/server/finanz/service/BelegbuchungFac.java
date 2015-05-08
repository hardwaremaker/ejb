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
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BelegbuchungFac {
	public BelegbuchungDto belegbuchungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiid(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP,
			RemoteException;

	public BelegbuchungDto belegbuchungFindByBelegartCNrBelegiidOhneExc(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP,
			RemoteException;

	public BelegbuchungDto belegbuchungFindByBuchungIId(Integer buchungIId)
			throws EJBExceptionLP, RemoteException;

	public BelegbuchungDto belegbuchungFindByBuchungIIdOhneExc(
			Integer buchungIId);

	public BuchungDto verbucheRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void verbucheRechnungRueckgaengig(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void verbucheZahlungRueckgaengig(RechnungzahlungDto zahlungDto, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public BuchungDto verbucheZahlung(Integer zahlungIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BuchungDto verbucheEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void verbucheEingangsrechnungRueckgaengig(Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public BuchungDto verbucheZahlungEr(Integer zahlungIId, TheClientDto theClientDto)
	  		throws EJBExceptionLP, RemoteException;
	
	public void verbucheBelegePeriode(Integer geschaeftsjahr, int periode, boolean alleNeu, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;

	public void verbucheGutschriftRueckgaengig(Integer rechnungIId, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;

	public void pruefeUvaVerprobung(String belegartCNr, Integer iBelegiid, TheClientDto theClientDto)
			throws EJBExceptionLP;

	public BigDecimal getBuchungsbetragInWahrung(Integer buchungdetailIId, String waehrungCNr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;

	public void pruefeUvaVerprobung(Date buchungsdatum, Integer kontoIId, TheClientDto theClientDto);

	public void verbucheZahlungErRueckgaengig(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			TheClientDto theClientDto);

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenAR(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public void belegbuchungenAusziffernWennNoetig(Integer kontoIId,
			List<BelegbuchungDto> belegbuchungDtos) throws EJBExceptionLP, RemoteException;

	public List<BelegbuchungDto> getAlleBelegbuchungenInklZahlungenER(
			Integer iId) throws EJBExceptionLP, RemoteException;

}
