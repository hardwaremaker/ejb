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
package com.lp.server.eingangsrechnung.ejbfac;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.IVendidataImporterBeanServices;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class VendidataImporterBeanServices implements
		IVendidataImporterBeanServices {
	
	private TheClientDto theClientDto;
	private VendidataBeanHolder beanHolder;

	public VendidataImporterBeanServices(TheClientDto theClientDto, VendidataBeanHolder beanHolder) {
		this.theClientDto = theClientDto;
		this.beanHolder = beanHolder;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public KontoDto kontoFindByCNrKontotypMandant(String kontoCNr, String kontotyp) throws RemoteException {
		return beanHolder.getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(kontoCNr, kontotyp, 
				getTheClientDto().getMandant(), getTheClientDto());
	}

	@Override
	public List<KundeDto> kundeFindByKontoIIdDebitorenkonto(Integer kontoIIdDebitorenkonto) throws RemoteException {
		KundeDto[] kunden = beanHolder.getKundeFac().kundefindByKontoIIdDebitorenkonto(kontoIIdDebitorenkonto);
		return kunden == null ? new ArrayList<KundeDto>() : Arrays.asList(kunden);
	}

	@Override
	public LieferantDto lieferantFindByPartnerIId(Integer partnerIId) throws RemoteException {
		return beanHolder.getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partnerIId, 
				getTheClientDto().getMandant(), getTheClientDto());
	}

	@Override
	public EingangsrechnungDto createEingangsrechnung(EingangsrechnungDto eingangsrechnungDto)
			throws RemoteException {
		return beanHolder.getEingangsrechnungFac().createEingangsrechnung(eingangsrechnungDto, getTheClientDto());
	}

	@Override
	public EingangsrechnungKontierungDto createEingangsrechnungKontierung(
			EingangsrechnungKontierungDto kontierungDto) throws RemoteException {
		return beanHolder.getEingangsrechnungFac().createEingangsrechnungKontierung(kontierungDto, getTheClientDto());
	}

	@Override
	public MwstsatzDto mwstsatzFindByMwstsatzbezIIdAktuellster(
			Integer mwstSatzBezIId, Timestamp belegdatum) throws RemoteException {
		return beanHolder.getMandantFac().mwstsatzZuDatumValidate(
				mwstSatzBezIId, belegdatum, getTheClientDto());
//		return beanHolder.getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(mwstSatzBezIId, getTheClientDto());
	}

	@Override
	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI) {
		return beanHolder.getBenutzerServicesFac().getTextRespectUISpr(sTokenI, mandantCNr, loI);
	}
	
	@Override
	public List<KundeDto> kundeFindByFremdsystemnummer(String fremdsystemnummer) throws RemoteException {
		return beanHolder.getKundeFac().kundeFindByFremdsystemnummerMandantCnrOhneExc(fremdsystemnummer, getTheClientDto().getMandant());
	}
	
	@Override
	public LieferantDto createVerstecktenLieferantenAusKunden(Integer kundeIId) throws RemoteException {
		Integer lieferantIId = beanHolder.getLieferantFac().createVerstecktenLieferantAusKunden(kundeIId, getTheClientDto());
		return beanHolder.getLieferantFac().lieferantFindByPrimaryKeyOhneExc(lieferantIId, getTheClientDto());
	}
	
	@Override
	public RechnungDto createRechnung(RechnungDto rechnungDto) throws RemoteException {
		return beanHolder.getRechnungFac().createRechnung(rechnungDto, getTheClientDto());
	}
	
	@Override
	public RechnungPositionDto createRechnungposition(RechnungPositionDto positionDto, Integer lagerIId) throws RemoteException {
		return beanHolder.getRechnungFac().createRechnungPosition(positionDto, lagerIId, getTheClientDto());
	}
	
	@Override
	public RechnungDto setupDefaultRechnung(KundeDto kundeDto) throws EJBExceptionLP, RemoteException {
		return beanHolder.getRechnungFac().setupDefaultRechnung(kundeDto, getTheClientDto());
	}
	
	@Override
	public Integer findGeschaeftsjahrFuerDatum(Date datum) {
		return beanHolder.getBuchenFac().findGeschaeftsjahrFuerDatum(datum, getTheClientDto().getMandant());
	}
	
	@Override
	public Boolean get4VendingErArImportBruttoStattNetto() {
		return beanHolder.getParameterFac().get4VendingErArImportBruttoStattNetto(getTheClientDto().getMandant());
	}
	
	@Override
	public Integer getUINachkommastellenVK() throws RemoteException {
		ParametermandantDto parameter = beanHolder.getParameterFac()
				.getMandantparameter(getTheClientDto().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
		return parameter.asInteger();
	}
	
	@Override
	public MwstsatzDto[] mwstsatzfindAll(Timestamp time) {
		return beanHolder.getMandantFac().mwstsatzfindAllByMandant(getTheClientDto().getMandant(), time, true);
	}
}
