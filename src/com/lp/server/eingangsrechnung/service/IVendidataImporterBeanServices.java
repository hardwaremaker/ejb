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
package com.lp.server.eingangsrechnung.service;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public interface IVendidataImporterBeanServices {
	
	public TheClientDto getTheClientDto();
	
	public KontoDto kontoFindByCNrKontotypMandant(String kontoCNr, String kontotyp) throws RemoteException;
	
	public List<KundeDto> kundeFindByKontoIIdDebitorenkonto(Integer kontoIIdDebitorenkonto) throws RemoteException;
	
	public LieferantDto lieferantFindByPartnerIId(Integer partnerIId) throws RemoteException;
	
	public EingangsrechnungDto createEingangsrechnung(EingangsrechnungDto eingangsrechnungDto) throws RemoteException;
	
	public EingangsrechnungKontierungDto createEingangsrechnungKontierung(EingangsrechnungKontierungDto kontierungDto) throws RemoteException;
	
	public MwstsatzDto mwstsatzFindByMwstsatzbezIIdAktuellster(Integer mwstSatzIId, Timestamp belegdatum) throws RemoteException;	
	
	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI);

	List<KundeDto> kundeFindByFremdsystemnummer(String fremdsystemnummer) throws RemoteException;

	LieferantDto createVerstecktenLieferantenAusKunden(Integer kundeIId) throws RemoteException;

	RechnungDto createRechnung(RechnungDto rechnungDto) throws RemoteException;

	RechnungPositionDto createRechnungposition(RechnungPositionDto positionDto,
			Integer lagerIId) throws RemoteException;

	RechnungDto setupDefaultRechnung(KundeDto kundeDto) throws EJBExceptionLP, RemoteException;

	Integer findGeschaeftsjahrFuerDatum(Date datum);

	Boolean get4VendingErArImportBruttoStattNetto();

	Integer getUINachkommastellenVK() throws RemoteException;

	MwstsatzDto[] mwstsatzfindAll(Timestamp time);
}
