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
 *******************************************************************************/
package com.lp.server.finanz.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuExportValidator extends Facade implements IFibuExportValidation {
	private List<EJBExceptionLP> validations ;
	private boolean validationThrows ;
	private TheClientDto theClientDto ;
	
	public FibuExportValidator(TheClientDto theClientDto) {
		this.theClientDto = theClientDto ;
		validationThrows = true ;
		clearValidations(); 
	}
	
	/**
	 * Soll eine fehlerhafte Pr&uuml;fung zu einer Exception f&uuml;hren?
	 * 
	 * @param enable true wenn eine fehlgeschlagene Pr&uuml;fung der Daten zu einer
	 *  Exception f&uuml;hren soll
	 */
	public void setValidationThrowsException(boolean enable) {
		validationThrows = enable ;
	}

	/**
	 * Nur Validieren, keine Exceptions.</br>
	 * <p>Die Fehlgeschlagenenen Pr&uuml;fungen k&ouml;nnen mittels {@link #getValidations()}
	 * ermittelt werden</p>
	 */
	public void beValidationOnly() {
		validationThrows = false ;
	}

	public List<EJBExceptionLP> getValidations() {
		return validations ;
	}

	@Override
	public boolean hasFailedValidations() {
		return validations.size() > 0 ;
	}
	
	public void addFailedValidation(EJBExceptionLP e) {
		validations.add(e) ;
		if(validationThrows) throw e ;
	}
	
	public void clearValidations() {
		validations = new ArrayList<EJBExceptionLP>() ;
	}	

	protected ArrayList<Object> getAllInfoForTheClient(PartnerDto partnerDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(partnerDto.formatFixTitelName1Name2());
		list.add(partnerDto.formatAdresse());
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(RechnungDto rechnungDto) {
		try {
			ArrayList<Object> list = new ArrayList<Object>();
			RechnungartDto rechnungartDto = getRechnungServiceFac()
					.rechnungartFindByPrimaryKey(
							rechnungDto.getRechnungartCNr(), theClientDto);
			if (rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				list.add(getTextRespectUISpr("lp.kuerzel.rechnung",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " " + rechnungDto.getCNr());
			} else if (rechnungartDto.getRechnungtypCNr().equals(
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				list.add(getTextRespectUISpr("lp.kuerzel.gutschrift",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " " + rechnungDto.getCNr());
			}
			return list;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected ArrayList<Object> getAllInfoForTheClient(EingangsrechnungDto erDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		String sBelegartBelegnummer = getTextRespectUISpr(
				"er.eingangsrechnungsnummer", theClientDto.getMandant(),
				theClientDto.getLocUi())
				+ " " + erDto.getCNr();
		list.add(sBelegartBelegnummer);
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(KontoDto kontoDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(kontoDto.getCNr() + " " + kontoDto.getCBez());
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(KostenstelleDto kstDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(kstDto.getCNr() + " " + kstDto.getCBez());
		return list;
	}
	
	@Override
	public boolean isValidFinanzamt(FinanzamtDto finanzamtDto) {
		if(!theClientDto.getMandant().equals(finanzamtDto.getMandantCNr()) ||
			finanzamtDto.getPartnerDto().getLandplzortDto() == null) {

			ArrayList<Object> a = new ArrayList<Object>();
			a.add(finanzamtDto.getPartnerDto().getCName1nachnamefirmazeile1());
			addFailedValidation(new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT,
					a,
					new Exception(
							"FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT "
							+ finanzamtDto.getPartnerDto()
							.getCName1nachnamefirmazeile1())));
			return false ;
		}

		return true ;
	}
	
	@Override
	public boolean isValidRechnungStatus(RechnungDto rechnungDto) {
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
					new Exception("RE oder GS mit I_ID="
							+ rechnungDto.getIId() + " ist nicht aktiviert"));
			// Kunden holen
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getAllInfoForTheClient(rechnungDto));
			a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
			ex.setAlInfoForTheClient(a);
			addFailedValidation(ex) ;
			return false ;
		}
		
		return true ;
	}
	
	@Override
	public boolean isValidLaenderartCnr(String laenderartCnr, Integer partnerId, Object belegDto) {
		if (laenderartCnr == null) {
			PartnerDto partnerDto = getPartnerFac()
					.partnerFindByPrimaryKey(partnerId, theClientDto);
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_LAENDERART_NICHT_FESTSTELLBAR_FUER_PARTNER,
					new Exception(
							"Laenderart nicht feststellbar partner i_id"
									+ partnerDto.getIId()
									+ ","
									+ partnerDto
											.getCName1nachnamefirmazeile1()));
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getBelegInfo(belegDto)) ;
			a.addAll(getAllInfoForTheClient(partnerDto));
			ex.setAlInfoForTheClient(a);
			addFailedValidation(ex) ;
			return false ;
		}
		
		return true ;
	}
	
	@Override
	public boolean isValidKundeDebitorenKonto(KundeDto kundeDto, RechnungDto rechnungDto) {
		if (kundeDto.getIidDebitorenkonto() == null) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT,
					new Exception(kundeDto.getIId()	+ "," + kundeDto.getPartnerDto().getCName1nachnamefirmazeile1()));
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getAllInfoForTheClient(rechnungDto));
			a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
			ex.setAlInfoForTheClient(a);
			addFailedValidation(ex) ;
			return false ;
		}
		
		return true ;
	}
	
	@Override
	public boolean isRechnungVollstaendigKontiert(RechnungDto rechnungDto, KundeDto kundeDto) throws RemoteException {
		// pruefen, ob die Rechnung vollstaendig kontiert ist
		BigDecimal bdKontiert = getRechnungFac().getProzentsatzKontiert(
				rechnungDto.getIId(), theClientDto);
		if (bdKontiert.compareTo(new BigDecimal(0)) != 0
				&& bdKontiert.compareTo(new BigDecimal(100)) != 0) {
			EJBExceptionLP ex = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT,
					new Exception("Debitorenkonto fuer Kunde "
							+ kundeDto.getIId()
							+ ","
							+ kundeDto.getPartnerDto()
									.getCName1nachnamefirmazeile1()
							+ " ist nicht definiert"));
			ArrayList<Object> a = new ArrayList<Object>();
			a.addAll(getAllInfoForTheClient(rechnungDto));
			a.addAll(getAllInfoForTheClient(kundeDto.getPartnerDto()));
			ex.setAlInfoForTheClient(a);
			addFailedValidation(ex) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Der Beleg muss im Monat des Stichtages liegen. lt. Definition von WH
	 * 28.03.06
	 * @param exportKriterienDto
	 * @param belegDto
	 * @param partnerDto
	 */	
	@Override
	public boolean isValidExportZeitraumFuerBeleg(
			FibuExportKriterienDto exportKriterienDto, Object belegDto,
			PartnerDto partnerDto) {
		if(!exportKriterienDto.isBAuchBelegeAusserhalbGueltigkeitszeitraum()) {
			if(!liegtBelegdatumInnerhalbGueltigemExportZeitraum(exportKriterienDto, belegDto)) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM,
						new Exception(
								"Der Beleg liegt ausserhalb des g\u00FCltigen Zeitraumes"
										+ belegDto.toString()));
				ArrayList<Object> a = new ArrayList<Object>();
				// Gueltigen Zeitraum angeben
				GregorianCalendar cBeginn = new GregorianCalendar();
				cBeginn.setTime(exportKriterienDto.getDStichtag());
				cBeginn.set(Calendar.DATE, 1);
				a.add(Helper.formatDatum(cBeginn.getTime(),
						theClientDto.getLocUi())
						+ " - "
						+ Helper.formatDatum(exportKriterienDto.getDStichtag(),
								theClientDto.getLocUi()));
				// Belegart + Belegnummer
				if (belegDto instanceof RechnungDto) {
					a.addAll(getAllInfoForTheClient((RechnungDto) belegDto));
				} else if (belegDto instanceof EingangsrechnungDto) {
					a.addAll(getAllInfoForTheClient((EingangsrechnungDto) belegDto));
				}
				// Belegdatum
				java.sql.Date tBelegdatum = null;
				if (belegDto instanceof RechnungDto) {
					tBelegdatum = new java.sql.Date(((RechnungDto) belegDto)
							.getTBelegdatum().getTime());
				} else if (belegDto instanceof EingangsrechnungDto) {
					tBelegdatum = new java.sql.Date(
							((EingangsrechnungDto) belegDto).getDBelegdatum()
									.getTime());
				}
				a.add(getTextRespectUISpr("lp.datum",
						theClientDto.getMandant(), theClientDto.getLocUi())
						+ " "
						+ Helper.formatDatum(tBelegdatum,
								theClientDto.getLocUi()));
				a.addAll(getAllInfoForTheClient(partnerDto));
				ex.setAlInfoForTheClient(a);
				addFailedValidation(ex) ;
				return false ;
			}
		}
		
		return true ;
	}
	
	private boolean liegtBelegdatumInnerhalbGueltigemExportZeitraum(
			FibuExportKriterienDto exportKriterienDto, Object oBelegDto) {
		java.util.Date tBelegdatum = null;
		if (oBelegDto instanceof RechnungDto) {
			tBelegdatum = ((RechnungDto) oBelegDto).getTBelegdatum();
		} else if (oBelegDto instanceof FLRRechnung) {
			tBelegdatum = ((FLRRechnung) oBelegDto).getD_belegdatum();
		} else if (oBelegDto instanceof EingangsrechnungDto) {
			tBelegdatum = ((EingangsrechnungDto) oBelegDto).getDBelegdatum();
		} else if (oBelegDto instanceof FLREingangsrechnung) {
			tBelegdatum = ((FLREingangsrechnung) oBelegDto).getT_belegdatum();
		}
		GregorianCalendar cStichtag = new GregorianCalendar();
		cStichtag.setTime(exportKriterienDto.getDStichtag());
		GregorianCalendar cBelegdatum = new GregorianCalendar();
		cBelegdatum.setTime(tBelegdatum);
		if (cStichtag.get(Calendar.YEAR) == cBelegdatum.get(Calendar.YEAR)
				&& cStichtag.get(Calendar.MONTH) == cBelegdatum
						.get(Calendar.MONTH)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isValidKontolaenderart(
			KontolaenderartDto kontolaenderartDto, Integer kontoId, String laenderartCnr, Integer finanzamtId,
			Object belegDto, Integer mandantFinanzamtId) throws RemoteException {
		if(kontolaenderartDto != null) return true ;
	
		KontoDto kontoDtoHelp = getFinanzFac().kontoFindByPrimaryKey(kontoId);
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT,
				new Exception("Kontolaenderart fuer Konto i_id "
						+ kontoDtoHelp.getIId() + ","
						+ kontoDtoHelp.getCNr() + " "
						+ kontoDtoHelp.getCBez() + ", "
						+ laenderartCnr
						+ " ist nicht definiert"));
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(kontoDtoHelp.getCNr() + " "
				+ kontoDtoHelp.getCBez() + ", " + laenderartCnr);
		a.add("Finanzamt Mandant I_ID='" + (mandantFinanzamtId == null ? "" : mandantFinanzamtId.toString()) + "'") ;
		a.add("Finanzamt I_ID='" + (finanzamtId == null ? "" : finanzamtId.toString()) + "'");
		a.addAll(getBelegInfo(belegDto)) ;
		ex.setAlInfoForTheClient(a);
		addFailedValidation(ex) ;
		
		return false;
	}
	
	protected List<Object> getBelegInfo(Object belegDto) {
		if (belegDto instanceof RechnungDto) {
			return getAllInfoForTheClient((RechnungDto) belegDto) ;
		} else if (belegDto instanceof EingangsrechnungDto) {
			return getAllInfoForTheClient((EingangsrechnungDto) belegDto);
		}
		
		List<Object> lo = new ArrayList<Object>();
		lo.add("Unbekannte Belegart") ;
		return lo ;
	}
}
