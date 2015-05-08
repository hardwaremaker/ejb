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

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReportErloeskontoDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Validator;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

public class FibuExportManagerNachArtikelgruppenErloeskonto extends FibuExportManager {	
//	private KontoDto allgemeinerRabattKontoDto ;
	private KontoDto sonstigeKontoDto ;
//	private WaehrungDto uebersteuerteMandantenWaehrung ;
//	private boolean uebersteuerteMandantenWaehrungLoaded = false;
	private RechnungDto rechnungDto ;
//	private boolean allgemeinerRabattExtrabuchen = false ;
	private String laenderartCnr ;
	private Integer finanzamtUebersteuertId ;
	private Integer debitorenKontoId ;
//	private Integer debitorenKontoUebersteuertId ;
	private Integer rechnungId ;
	private RechnungPositionDto[] positionDtos ;
	private KundeDto kundeDto ;

	
	public FibuExportManagerNachArtikelgruppenErloeskonto(
			TheClientDto theClientDto) throws EJBExceptionLP {
		super(new FibuExportKriterienDto(true), new FibuExportValidatorErloeskonten(theClientDto), theClientDto) ; 
	}

	@Override
	public FibuexportDto[] getExportdatenEingangsrechnung(
			Integer eingangsrechnungIId, Date dStichtag) throws EJBExceptionLP {
		throw new IllegalArgumentException("should not be called") ;
	}

	@Override
	public FibuexportDto[] getExportdatenGutschrift(Integer rechnungIId,
			Date dStichtag) throws EJBExceptionLP {
		throw new IllegalArgumentException("should not be called") ;
	}

	@Override
	public FibuexportDto[] getExportdatenRechnung(Integer rechnungIId,
			Date dStichtag) throws EJBExceptionLP {
		throw new IllegalArgumentException("should not be called") ;
	}
	
	private void setRechnungId(Integer rechnungId) {
		if(rechnungId.equals(this.rechnungId)) return ;
		
		this.rechnungId = rechnungId ;
		positionDtos = null ;
		sonstigeKontoDto = null ;
		finanzamtUebersteuertId = null ;
		debitorenKontoId = null ;
		laenderartCnr = null ;
		kundeDto = null ;
		
//		uebersteuerteMandantenWaehrungLoaded = false ;
//		uebersteuerteMandantenWaehrung = null ;
//		debitorenKontoUebersteuertId = null ;
//		allgemeinerRabattExtrabuchen = false ;
//		allgemeinerRabattKontoDto = null ;
	}
	
	
//	private KontoDto getKontoAllgemeinerRabatt() throws RemoteException {
//		if(allgemeinerRabattKontoDto == null) {
//			ParametermandantDto parameterKontoRabatt = getParameterFac()
//			.getMandantparameter(theClientDto.getMandant(),
//					ParameterFac.KATEGORIE_FINANZ,
//					ParameterFac.PARAMETER_FINANZ_RABATT_KONTO);
//			if (parameterKontoRabatt.getCWert().trim().length()>0) {
//				allgemeinerRabattKontoDto = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
//						parameterKontoRabatt.getCWert().trim(),
//						FinanzServiceFac.KONTOTYP_SACHKONTO, theClientDto.getMandant(), theClientDto);
//				if (allgemeinerRabattKontoDto == null) {
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER,
//							new Exception("Konto " + parameterKontoRabatt.getCWert() 
//									+ " f\u00FCr Allgemeinen Rabatt ist nicht vorhanden"));
//				}
//
//				allgemeinerRabattExtrabuchen = true;
//			}			
//		}
//		
//		return allgemeinerRabattKontoDto ;
//	}
	
	
	private KontoDto getKontoSonstige() throws RemoteException {
		if(sonstigeKontoDto == null) {
			ParametermandantDto parameterKontoSonstige = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR);
			sonstigeKontoDto = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
					parameterKontoSonstige.getCWert(),
					FinanzServiceFac.KONTOTYP_SACHKONTO, 
					theClientDto.getMandant(), theClientDto);
			if (sonstigeKontoDto == null) {
				getValidator().addFailedValidation(
						new EJBExceptionLP(EJBExceptionLP.FEHLER,
								new Exception(
										"Konto f\u00FCr Positionen ohne Artikelgruppen ist nicht definiert\nDefinieren Sie den Paramter FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR")));
			}			
		}
		
		return sonstigeKontoDto ;
	}
	
//	private WaehrungDto getUebersteuerteMandantenWaehrung() throws RemoteException {
//		if(!uebersteuerteMandantenWaehrungLoaded) {
//			//Holen einer eventuell vorhandenen uebersteuerten Waehrung
//			ParametermandantDto paramterWaehrungUebersteuert = getParameterFac().
//					getMandantparameter(theClientDto.getMandant(), 
//							ParameterFac.KATEGORIE_FINANZ,
//							ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSTEUERTE_MANDANTENWAEHRUNG);
//			String sWaehrung = paramterWaehrungUebersteuert.getCWert();
//			if(sWaehrung != null && !"".equals(sWaehrung) && !sWaehrung.equals(theClientDto.getSMandantenwaehrung())){
//				//Mandantenwaehrung wird uebersteuert
//				try{
//					uebersteuerteMandantenWaehrung = getLocaleFac().waehrungFindByPrimaryKey(sWaehrung);
//				} catch (Exception e){
//					if(e.getCause() instanceof EJBExceptionLP){
//						//Wenn Waehrung nicht vorhanden dann Fehler
//						EJBExceptionLP ex = new EJBExceptionLP(
//								EJBExceptionLP.FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN,
//								new Exception("\u00DCbersteuerte W\u00E4hrung nicht gefunden:"));
//						ArrayList<Object> a = new ArrayList<Object>();
//						a.add(sWaehrung);
//						ex.setAlInfoForTheClient(a);
//						throw ex;
//					} else {
//						throw new EJBExceptionLP(e);
//					}
//				}
//			}
//
//			uebersteuerteMandantenWaehrungLoaded = true ;		
//		}
//		
//		return uebersteuerteMandantenWaehrung ;
//	}
	
	private RechnungDto getRechnungDto() throws EJBExceptionLP, RemoteException {
		if(rechnungDto == null) {
			Validator.notNull(rechnungId, "rechnungId");
			rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungId);
		}
		
		return rechnungDto ;
	}

	private RechnungPositionDto[] getRechnungPositionen(Integer rechnungId) throws RemoteException {
		if(positionDtos == null) {
			positionDtos = getRechnungFac()
					.rechnungPositionFindByRechnungIId(getRechnungDto().getIId()) ;			
		}
		
		return positionDtos ;
	}

	private KundeDto getKundeDto() throws RemoteException {
		if(kundeDto == null) {
			kundeDto = pruefeRechnungKunde(getRechnungDto()) ;
		}
		return kundeDto ;
	}
	
	private void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto ;
	}
	
	
	private Integer getDebitorenKontoId() {
		return debitorenKontoId ;
	}
	
	private String getLaenderartRechnung(RechnungDto rechnungDto) throws RemoteException {
		if(laenderartCnr == null) {
			setKundeDto(pruefeRechnungKunde(rechnungDto));
			if(getValidator().hasFailedValidations()) return null ;
			
			debitorenKontoId = kundeDto.getIidDebitorenkonto() ;
			String sLaenderartRechnung = null;
			
			boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(
					LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);
			if (hatFibu) {
				sLaenderartRechnung = getLaenderartZuKonto(
					kundeDto.getPartnerDto(), debitorenKontoId, rechnungDto.isReverseCharge());
			} else {
				sLaenderartRechnung = getLaenderartZuPartner(kundeDto.getPartnerDto());
			}
			
			laenderartCnr = sLaenderartRechnung;
			
			RechnungPositionDto[] positionDtos = getRechnungPositionen(rechnungDto.getIId());
			for (RechnungPositionDto rechnungPositionDto : positionDtos) {
				if(!rechnungPositionDto.isLieferschein()) continue ;
				
				if(rechnungDto.isAnzahlungsRechnung()) {
					getValidator().addFailedValidation(
							new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNGSRECHNUNG_LIEFERSCHEIN_NICHT_ERLAUBT, 
							"Lieferscheinposition nicht erlaubt in Anzahlungsrechnung " + rechnungDto.getCNr()));
					return laenderartCnr ;
				}
				
				LieferscheinDto lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(
						rechnungPositionDto.getLieferscheinIId()) ;
				if(!lieferscheinDto.getKundeIIdLieferadresse()
						.equals(lieferscheinDto.getKundeIIdRechnungsadresse())){
					if(hatFibu) {
						// bei Fibu ist die Basis der Laenderart das Finanzamt des Debitorenkontos
						KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(
								lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
						if (lieferscheinKundeDto.getIidDebitorenkonto() == null) {
							createDebitorenKontoIfNeeded(lieferscheinKundeDto, lieferscheinDto);
							lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(
									lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
						}
						if(getValidator().hasFailedValidations()) {
							return laenderartCnr ;
						}
						
						laenderartCnr = getLaenderartZuKonto(
								lieferscheinKundeDto.getPartnerDto(), lieferscheinKundeDto.getIidDebitorenkonto());

						//PJ 17120
						KontoDto lieferscheinDebitorenkonto = getFinanzFac()
								.kontoFindByPrimaryKey(lieferscheinKundeDto.getIidDebitorenkonto());
						finanzamtUebersteuertId = lieferscheinDebitorenkonto.getFinanzamtIId();
//						debitorenKontoUebersteuertId = lieferscheinKundeDto.getIidDebitorenkonto();
					} else {
						// PJ 17385 nur uebersteuern wenn Laender unterschiedlich
						KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(
								lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
						if (getFinanzServiceFac().isFibuLandunterschiedlich(kundeDto.getPartnerDto().getIId(), 
								lieferscheinKundeDto.getPartnerDto().getIId())) {
//								KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(
//									lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
								laenderartCnr = getLaenderartZuPartner(getPartnerFac()
									.partnerFindByPrimaryKeyOhneExc(lieferscheinKundeDto.getPartnerIId(), theClientDto));
						}
					}
				}
			}
		}

		return laenderartCnr ;
	}
	
	protected void createDebitorenKontoIfNeeded(KundeDto lieferscheinKundeDto, LieferscheinDto lieferscheinDto) {
		getValidator().addFailedValidation(
				new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT, 
				"Debitorenkonto f\u00FCr Lieferschein nicht definiert. ", lieferscheinKundeDto.getPartnerDto().formatName()
				+ ", Lieferschein: " + lieferscheinDto.getCNr()));
//		if(legeDebitorAn) {
//			KontoDto ktoDto = getKundeFac()
//					.createDebitorenkontoZuKundenAutomatisch(
//							lieferscheinKundeDto.getIId(), false,
//							null, theClientDto);
//			lieferscheinKundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(
//					ktoDto.getCNr()));
//			getKundeFac().updateKunde(lieferscheinKundeDto, theClientDto);
//			lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(flrLieferschein.getFlrkunde().getI_id(), theClientDto);
//		} else {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT, 
//					"Debitorenkonto f\u00FCr Lieferschein nicht definiert. ", lieferscheinKundeDto.getPartnerDto().formatName()
//					+ ", Lieferschein: " + lieferscheinDto.getCNr());
//		}			
	}

	public ReportErloeskontoDto getErloeskonto(Integer rechnungId, BelegpositionVerkaufDto positionDto) throws EJBExceptionLP {
		try {
			KontoDto erloeskontoDto = getErloeskontoIdsImpl(rechnungId, positionDto) ;
	
			if(getValidator().hasFailedValidations()) {
				EJBExceptionLP firstEx = getValidator().getValidations().get(0) ;
				return new ReportErloeskontoDto(firstEx.getCode(), firstEx.getMessage()) ;
			}

			if(erloeskontoDto == null) {
				return new ReportErloeskontoDto() ;
			}
			
			return new ReportErloeskontoDto(erloeskontoDto.getIId(), erloeskontoDto.getCNr()) ;
		} catch(RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);			
		}

		return null ;
	}
	
	protected KontoDto getErloeskontoIdsImpl(Integer rechnungId, BelegpositionVerkaufDto positionDto) throws RemoteException {
		setRechnungId(rechnungId);
		RechnungDto reDto = getRechnungDto() ;
		String laenderart = getLaenderartRechnung(reDto) ;

		Integer kontoId = getErloeskontoIdForPosition(positionDto) ;
		if(kontoId == null) return null ;
		
		Integer finanzId = finanzamtUebersteuertId ;
		if(finanzId == null) {
			if(validator.hasFailedValidations()) {
				return null ; 				
			}
			KontoDto debitorenKonto = getFinanzFac().kontoFindByPrimaryKey(getDebitorenKontoId()) ;
			finanzId = debitorenKonto.getFinanzamtIId() ;
		}
		
		KontoDto kontoDto = uebersetzeKontoNachLandBzwLaenderart(
				kontoId, laenderart, finanzId, theClientDto.getMandant(), reDto, 
				getKundeDto().getPartnerDto().getLandplzortDto().getIlandID());	
		return kontoDto ;
	}


	protected Integer getErloeskontoIdForPosition(BelegpositionVerkaufDto positionDto) throws RemoteException {
		// Erloeskonten nur fuer Handeingabe oder Ident-Artikel
		if(! (positionDto.isHandeingabe() || positionDto.isIdent())) return null ;

		// Kopfartikel hat kein Erloeskonto
		if(isKopfArtikel(positionDto)) return null ;

		if(getRechnungDto().isAnzahlungsRechnung()) {
			if(getDebitorenKontoId() == null) return null ;

			KontoDto debitorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(getDebitorenKontoId()) ;
			FinanzamtDto finanzamtDto = getFinanzamt(debitorenKontoDto.getFinanzamtIId()) ;
			Integer kontoId = getRechnungDto().isReverseCharge() 
					? finanzamtDto.getKontoIIdRCAnzahlungErhaltVerr()
					: finanzamtDto.getKontoIIdAnzahlungErhaltVerr() ;
			if(kontoId == null) {
				getValidator().addFailedValidation(new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT, 
						"Verrechnungskonto Erhaltene Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung " + getRechnungDto().getCNr())) ;				
			}
			
			return kontoId ;
		}

		if(positionDto.isIdent()) {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(positionDto.getArtikelIId(), theClientDto) ;
			if(artikelDto.getArtgruIId() == null) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
						new Exception(
								"Keine Artikelgruppe f\u00FCr Artikel definiert:"
								+ artikelDto.getCNr()));
				ArrayList<Object> a = new ArrayList<Object>();
				a.add("Rechnung: " + rechnungDto.getCNr() + " \n" + 
						"Artikel: " + artikelDto.getCNr());
				ex.setAlInfoForTheClient(a);
				getValidator().addFailedValidation(ex);
				return null ;
			}
			ArtgruDto artgruDto = getArtikelFac()
					.artgruFindByPrimaryKey(artikelDto.getArtgruIId(), theClientDto) ;
			if(artgruDto.getKontoIId() == null) {
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
						new Exception(
								"Kein Konto f\u00FCr Artikelgruppe definiert: \n"
								+ artgruDto.getCNr()));
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(artikelDto.getCNr());
				a.add(artgruDto.getCNr());
				ex.setAlInfoForTheClient(a);				
				getValidator().addFailedValidation(ex);
			}
			
			return artgruDto.getKontoIId() ;			
		}
		
		if(positionDto.isHandeingabe()) {
			KontoDto kontoSonstigeDto = getKontoSonstige() ;
			return kontoSonstigeDto == null ? null : getKontoSonstige().getIId() ;			
		}

		throw new IllegalArgumentException("Weder Handeingabe noch Identposition?") ;
	}
	
	private boolean isKopfArtikel(BelegpositionVerkaufDto positionDto) {
		if(positionDto.getPositioniIdArtikelset() != null) return false ;
		
		StuecklisteDto stuecklisteDto = getStuecklisteFac()
				.stuecklisteFindByArtikelIIdMandantCNrOhneExc(positionDto.getArtikelIId(), theClientDto.getMandant()) ;
		if(stuecklisteDto == null) return false ;
		
		return StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stuecklisteDto.getStuecklisteartCNr()) ;
	}
}
