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
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.KontoId;
import com.lp.util.EJBExceptionLP;

@Remote
public interface SepaImportFac {
	
	public static final Integer MAX_BELEGE_FUER_AUSWAHL = 10;
	public static final Integer MAX_OFFENE_BELEGE = 2000;
	
	// Sepakontoauszug Stati
	static class SepakontoauszugStatus {
		public static final String ANGELEGT = LocaleFac.STATUS_ANGELEGT;
		public static final String ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
		public static final String STORNIERT = LocaleFac.STATUS_STORNIERT;
	}

	public SepaImportTransformResult readAndTransformSepaKontoauszug(SepaImportProperties importProperties, 
			TheClientDto theClientDto) throws EJBExceptionLP;
	
	public List<ISepaImportResult> searchForImportMatches(KontoId kontoId, List<SepaKontoauszug> ktoauszuege, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public Integer importSepaImportResults(List<ISepaImportResult> results, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public Integer importSepaImportResults(SepakontoauszugDto sepakontoauszugDto, List<ISepaImportResult> results, String payloadReference,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public List<BelegAdapter> getAlleOffenenEingangsrechnungen(String waehrungCnr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
	public List<BelegAdapter> getAlleOffenenAusgangsrechnungen(String waehrungCnr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
	public Map<Integer, PartnerDto> getPartnerOfBelegeMap(List<BelegAdapter> belege, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BigDecimal getSaldoVonBankverbindungByAuszug(Integer kontoIId,
			Integer auszugNr, TheClientDto theClient)
			throws EJBExceptionLP, RemoteException;

	public void archiviereSepaKontoauszug(String xmlKontoauszug, String filename, 
			Integer bankverbindungIId, Integer auszugsNr, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
	public Integer createSepakontoauszug(SepakontoauszugDto sepakontoauszugDto, TheClientDto theClientDto);
	
	public void updateSepakontoauszug(SepakontoauszugDto sepakontoauszugDto, TheClientDto theClientDto);
	
	public void removeSepakontoauszug(Integer iId);
	
	public SepakontoauszugDto sepakontoauszugFindByPrimaryKey(Integer sepakontoauszugId, TheClientDto theClientDto);
	
	public SepakontoauszugDto sepakontoauszugFindByPrimaryKeySmall(Integer sepakontoauszugId, TheClientDto theClientDto);
	
	public SepakontoauszugDto sepakontoauszugFindByBankverbindungIIdIAuszug(
			Integer bankverbindungIId, Integer iAuszug, TheClientDto theClientDto);
	
	public void storniereSepakontoauszug(Integer sepakontoauszugIId, TheClientDto theClientDto);
	
	public SepakontoauszugDto getSepakontoauszugNiedrigsteAuszugsnummer(Integer bankverbindungIId, TheClientDto theClientDto);

	void pruefeSepakontoauszugAufVerbuchung(Integer sepakontoauszugIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	Iso20022PaymentsDto getIso20022PaymentsByStandard(Iso20022StandardEnum standard);

	Map<Iso20022StandardEnum, Iso20022PaymentsDto> getMapOfIso20022Payments();

	List<Iso20022StandardDto> iso20022standardsFindAll();

	Iso20022BankverbindungDto iso20022BankverbindungFindByBankverbindungIIdNoExc(BankverbindungId bankverbindungId);

	Integer createIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto);

	void updateIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto);

	void removeIso20022Bankverbindung(Integer iId);

	String getWaehrungOfKonto(KontoId kontoId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
}
