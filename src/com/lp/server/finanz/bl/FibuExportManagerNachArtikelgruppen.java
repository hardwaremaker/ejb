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
package com.lp.server.finanz.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReportErloeskontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.UstWarnungDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejbfac.CoinRoundingService;
import com.lp.server.rechnung.ejbfac.CoinRoundingServiceFac;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.fastlanereader.generated.FLRMwstsatz;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzCodeDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.FacLookup;
import com.lp.server.util.HvOptional;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.EventLogger;
import com.lp.server.util.logger.LogEventProducer;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.AddableHashMap;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den FibuExport nach Kostenstellen
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 26.01.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/30 13:28:06 $
 */
class FibuExportManagerNachArtikelgruppen extends FibuExportManager {
	private EventLogger evtLogger = null;

	private CoinRoundingServiceFac coinRoundingService;
	private Context context;

	private CoinRoundingServiceFac getCoinRoundingService() {
		if (coinRoundingService == null) {
			try {
				context = new InitialContext();
				coinRoundingService = FacLookup.lookupLocal(context, CoinRoundingService.class,
						CoinRoundingServiceFac.class);
			} catch (Throwable t) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
			}
		}
		return coinRoundingService;
	}

	private class ReversechargeartCache extends HvCreatingCachingProvider<Integer, ReversechargeartDto> {
		private TheClientDto theClientDto;

		public ReversechargeartCache(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}

		@Override
		protected ReversechargeartDto provideValue(Integer key, Integer transformedKey) {
			try {
				return getFinanzServiceFac().reversechargeartFindByPrimaryKey(key, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
				return null;
			}
		}
	};

	private ReversechargeartCache rcCache;
	
	private class MwstsatzCodeCache extends HvCreatingCachingProvider<SteuerfallKey, HvOptional<MwstsatzCodeDto>> {
		@Override
		protected HvOptional<MwstsatzCodeDto> provideValue(SteuerfallKey key, SteuerfallKey transformedKey) {
			return getMandantFac().mwstsatzCodeFindByMwstsatzReversechargeart(key.getMwstsatzId(), key.getRcartId());
		}
		
		public String getCodeAr(SteuerfallKey key) {
			HvOptional<MwstsatzCodeDto> codeOpt = getValueOfKey(key);
			return codeOpt.isPresent() ? codeOpt.get().getCSteuercodeAr() : null;
		}
		
		public String getCodeEr(SteuerfallKey key) {
			HvOptional<MwstsatzCodeDto> codeOpt = getValueOfKey(key);
			return codeOpt.isPresent() ? codeOpt.get().getCSteuercodeEr() : null;
		}
	}
	
	private MwstsatzCodeCache steuercodeCache;

	FibuExportManagerNachArtikelgruppen(FibuExportKriterienDto exportKriterienDto, TheClientDto theClientDto) {
		super(exportKriterienDto, theClientDto);
		evtLogger = new EventLogger(getEventLoggerFac(), FibuExportManagerNachArtikelgruppen.class);
	}

	public FibuexportDto[] getExportdatenEingangsrechnung(Integer eingangsrechnungIId, Date stichtag)
			throws EJBExceptionLP {
		myLogger.logData(eingangsrechnungIId, theClientDto.getIDUser());

		return getExportER(eingangsrechnungIId, stichtag);
//		return getExportEROriginal(eingangsrechnungIId, stichtag) ;
	}

	private void verifyERAktiviert(EingangsrechnungDto erDto) {
		// PJ19169
		if (Helper.short2boolean(erDto.getBMitpositionen()) == true && erDto.getTGedruckt() == null) {
			ArrayList al = new ArrayList();
			al.add(erDto.getCNr());

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ER_MIT_POSITIONEN_NOCH_NICHT_GEDRUCKT, al,
					new Exception("ER mit Positionen noch nicht aktiviert/gedruckt. " + erDto.getCNr()));
		}
	}

	private boolean isSameUstSatz(EingangsrechnungKontierungDto[] erKontierungDtos) {
		if (erKontierungDtos == null)
			return true;

		for (int i = 0; i < erKontierungDtos.length - 1; i++) {
			if (!erKontierungDtos[i].getMwstsatzIId().equals(erKontierungDtos[i + 1].getMwstsatzIId())) {
				return false;
			}
		}
		return true;
	}

	private int getAnzahlGegenkonten(EingangsrechnungDto erDto, EingangsrechnungKontierungDto[] erKontoDto) {
		if (erDto.getKontoIId() != null) {
			return 1;
		}

		if (erDto.isAnzahlung()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());
		}

		return erKontoDto.length;
	}

	private FibuexportDto[] getExportER(Integer eingangsrechnungIId, Date stichtag) {
		FibuexportDto[] exportDtos = null;
		try {
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			boolean bAnzahlung = erDto.isAnzahlung();

			verifyERAktiviert(erDto);
			if (erDto.getNBetrag().signum() == 0) {
				return new FibuexportDto[0];
			}

			EingangsrechnungKontierungDto[] erKontoDto = pruefeEingangsrechnungKontierung(erDto);
			boolean bUSTSatzGleich = isSameUstSatz(erKontoDto);
			int iAnzahlGegenkonten = getAnzahlGegenkonten(erDto, erKontoDto);

			LieferantDto lieferantDto = pruefeEingangsrechnungLieferant(erDto);
			KontoDto kreditorenkontoDto = getFinanzFac()
					.kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto());

			// Anzahl der Buchungszeilen
			int iZeilen = 1 + iAnzahlGegenkonten;
			// jetzt wird das Daten-Array zusammengebaut
			exportDtos = new FibuexportDto[iZeilen];
			boolean isErReversecharge = isEingangsrechnungDtoReversecharge(erDto);
			for (int i = 0; i < exportDtos.length; i++) {
				exportDtos[i] = new FibuexportDto();
				exportDtos[i].setIGeschaeftsjahr(erDto.getIGeschaeftsjahr());
				exportDtos[i].setBReverseCharge(isErReversecharge);
			}
			// die Buchung auf Kreditorenkonto ist immer die erste.
			int iZeilePersonenkonto = 0;

			// Die Buchungszeile fuers Kreditorenkonto
			exportDtos[iZeilePersonenkonto].setKonto(kreditorenkontoDto);
			Integer kontoIIdAnzahlungVerr = null;
			if (bAnzahlung) {
				// bei Anzahlungsrechnung auf Konto
				// "Anzahlung Gegeben Verrechnet" buchen

				FinanzamtDto finanzamtDto = getFinanzamt(kreditorenkontoDto.getFinanzamtIId());
				kontoIIdAnzahlungVerr = finanzamtDto.getKontoIIdAnzahlungGegebenVerr();
				if (kontoIIdAnzahlungVerr == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT,
							"Verrechnungskonto Geleistete Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung "
									+ erDto.getCNr());
				}

				// SP3930 Das Anzahlungskonto muss uebersetzt werden (mehrere Finanzaemter)
				Timestamp belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
				String laenderartCnr = getLaenderartZuPartner(
						lieferantDto.getPartnerDto(), belegDatum);
				KontoDto kDto = uebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungVerr, laenderartCnr,
						kreditorenkontoDto.getFinanzamtIId(), erDto.getMandantCNr(), erDto,
						lieferantDto.getPartnerDto().getLandplzortDto().getIlandID(), 
						erDto.getReversechargeartId(), belegDatum);
				kontoIIdAnzahlungVerr = kDto.getIId();
			}

			rcCache = new ReversechargeartCache(theClientDto);
			steuercodeCache = new MwstsatzCodeCache();
			
			// Gegenkonto / Kostenstelle / MWST-Satz
			if (iAnzahlGegenkonten > 1) {
				// wenn es mehrere Gegenkonten gibt
				exportDtos[iZeilePersonenkonto].setGkto(null);
				// wenn alle Mehrfachkontierungen den gleichen UST-Satz
				// haben,
				// dann kommt auch hier der MWST-Satz rein
				if (bUSTSatzGleich) {
					MwstsatzDto mwstDto = getMwstsatz(erKontoDto[0].getMwstsatzIId());
					exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
				}
			} else {
				// nur ein Gegenkonto, aber 2 Moeglichkeiten:
				// 1. Das Gegenkonto steht in den Kopfdaten
				KontoDto kontoDtoGegenkonto = null;
				if (erKontoDto == null) {
					if (bAnzahlung) {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
					} else {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
					}
					exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
					// Kostenstelle
					Integer kstIId = erDto.getKostenstelleIId();
					KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kstIId);
					exportDtos[iZeilePersonenkonto].setKost(kstDto);
					// MWST-Satz
					Integer mwstIId = erDto.getMwstsatzIId();
					MwstsatzDto mwstDto = getMwstsatz(mwstIId);
					exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
					// MWST-Betrag
					exportDtos[iZeilePersonenkonto].setSteuer(erDto.getNUstBetrag());
					exportDtos[iZeilePersonenkonto].setSteuerFW(erDto.getNUstBetragfw());
					setupExportSteuercode(exportDtos[iZeilePersonenkonto], erDto);
				}
				// 2. Es gibt nur einen Mehrfachkontierungseintrag mit dem
				// ganzen ER-Wert
				else {
					Integer kontoId = bAnzahlung ? kontoIIdAnzahlungVerr : erKontoDto[0].getKontoIId();
					kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoId);
					exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
					/**
					 * @todo unterscheiden: alle mit gleicher UST oder mit verschiedener PJ 4270
					 */
					exportDtos[iZeilePersonenkonto].setSteuer(BigDecimal.ZERO);
					exportDtos[iZeilePersonenkonto].setSteuerFW(BigDecimal.ZERO);
					setupExportSteuercode(exportDtos[iZeilePersonenkonto], erDto, erKontoDto[0]);
				}
			}
			exportDtos[iZeilePersonenkonto].setOPNummer(erDto.getCLieferantenrechnungsnummer());
			exportDtos[iZeilePersonenkonto].setBelegnr(erDto.getCNr());
			exportDtos[iZeilePersonenkonto].setBelegdatum(erDto.getDBelegdatum());

			// PJ 17006
			// exportDtos[iZeilePersonenkonto].setValutadatum(erDto.getDFreigabedatum());
			exportDtos[iZeilePersonenkonto].setValutadatum(stichtag);

			exportDtos[iZeilePersonenkonto].setWaehrung(theClientDto.getSMandantenwaehrung());
			// Sollbetrag ist null
			exportDtos[iZeilePersonenkonto].setSollbetrag(null);
			exportDtos[iZeilePersonenkonto].setSollbetragFW(null);
			// Bruttobetrag im Haben
			exportDtos[iZeilePersonenkonto].setHabenbetrag(erDto.getNBetrag());
			exportDtos[iZeilePersonenkonto].setHabenbetragFW(erDto.getNBetragfw());
			// Steuer ist null
			exportDtos[iZeilePersonenkonto].setSteuer(null);
			exportDtos[iZeilePersonenkonto].setSteuerFW(null);
			// MWST-Betrag
			exportDtos[iZeilePersonenkonto].setFremdwaehrung(erDto.getWaehrungCNr());
			exportDtos[iZeilePersonenkonto].setBelegart(BELEGART_ER);
			exportDtos[iZeilePersonenkonto].setPartnerDto(lieferantDto.getPartnerDto());
			String laenderartCNr = null;
			boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG,
					theClientDto);
			Timestamp belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
			if (hatFibu) {
				laenderartCNr = getLaenderartZuKonto(lieferantDto.getPartnerDto(),
						lieferantDto.getKontoIIdKreditorenkonto(), belegDatum);
			} else {
				laenderartCNr = getLaenderartZuPartner(
						lieferantDto.getPartnerDto(), belegDatum);
			}
			exportDtos[iZeilePersonenkonto].setLaenderartCNr(laenderartCNr);

			ZahlungszielDto zzDto = getZahlungsziel(erDto.getZahlungszielIId());
			setupExportFromZahlungsziel(exportDtos[iZeilePersonenkonto], zzDto);
			exportDtos[iZeilePersonenkonto].setText(erDto.getCText());
			exportDtos[iZeilePersonenkonto].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
			exportDtos[iZeilePersonenkonto].setUidNummer(lieferantDto.getPartnerDto().getCUid());
			// Gegenbuchung, wenn keine Mehrfachkontierung
			if (erKontoDto == null) {
				final int iZeileGegenbuchung = 1;

				// Kst nicht pruefen, da nur das konto zaehlt
				KostenstelleDto kstDtoHW = getSystemFac().kostenstelleFindByPrimaryKey(erDto.getKostenstelleIId());
				KontoDto kontoDtoGegenkonto = null;
				if (bAnzahlung) {
					kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
				} else {
					kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
				}
				// buchung aufs HW-Konto
				exportDtos[iZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
				exportDtos[iZeileGegenbuchung].setGkto(kreditorenkontoDto);
				exportDtos[iZeileGegenbuchung].setOPNummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeileGegenbuchung].setBelegnr(erDto.getCNr());
				exportDtos[iZeileGegenbuchung].setBelegdatum(erDto.getDBelegdatum());

				// PJ 17006
				// exportDtos[iZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
				exportDtos[iZeileGegenbuchung].setValutadatum(stichtag);

				exportDtos[iZeileGegenbuchung].setWaehrung(theClientDto.getSMandantenwaehrung());
				if (Helper.short2boolean(erDto.getBIgErwerb()) || isEingangsrechnungDtoReversecharge(erDto)) {
					// die Steuer ist nur theoretisch, daher Betrag ohne
					// Steuer
					exportDtos[iZeileGegenbuchung].setSollbetrag(erDto.getNBetrag());
					exportDtos[iZeileGegenbuchung].setSollbetragFW(erDto.getNBetragfw());
				} else {
					// Sollbetrag ist brutto
					exportDtos[iZeileGegenbuchung].setSollbetrag(erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
					exportDtos[iZeileGegenbuchung]
							.setSollbetragFW(erDto.getNBetragfw().subtract(erDto.getNUstBetragfw()));
				}
				BigDecimal kurs = new BigDecimal(1).divide(erDto.getNKurs(), 6, BigDecimal.ROUND_HALF_EVEN);
				exportDtos[iZeileGegenbuchung].setKurs(kurs);
				// Habenbetrag ist null
				exportDtos[iZeileGegenbuchung].setHabenbetrag(null);
				exportDtos[iZeileGegenbuchung].setHabenbetragFW(null);
				// Steuerbetrag ist hier
				exportDtos[iZeileGegenbuchung].setSteuer(erDto.getNUstBetrag());
				exportDtos[iZeileGegenbuchung].setSteuerFW(erDto.getNUstBetragfw());
				exportDtos[iZeileGegenbuchung].setFremdwaehrung(erDto.getWaehrungCNr());
				exportDtos[iZeileGegenbuchung].setKost(kstDtoHW);
				exportDtos[iZeileGegenbuchung].setBelegart(BELEGART_ER);
				exportDtos[iZeileGegenbuchung].setPartnerDto(lieferantDto.getPartnerDto());
				// mwst-satz
				Integer mwstIId = erDto.getMwstsatzIId();
				MwstsatzDto mwstDto = getMwstsatz(mwstIId);
				exportDtos[iZeileGegenbuchung].setMwstsatz(mwstDto);
				exportDtos[iZeileGegenbuchung].setLaenderartCNr(laenderartCNr);
				exportDtos[iZeileGegenbuchung].setText(erDto.getCText());
				exportDtos[iZeileGegenbuchung].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeileGegenbuchung].setUidNummer(lieferantDto.getPartnerDto().getCUid());
				setupExportFromZahlungsziel(exportDtos[iZeileGegenbuchung], zzDto);
				if (exportDtos[iZeileGegenbuchung].isBReverseCharge()) {
					exportDtos[iZeileGegenbuchung].setReversechargeartId(erDto.getReversechargeartId());
				}
				setupExportSteuercode(exportDtos[iZeileGegenbuchung], erDto);
			} else {
				updateGegenkontenMehrfachkontierung(stichtag, exportDtos, erDto, erKontoDto, lieferantDto,
						kreditorenkontoDto, laenderartCNr, zzDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return exportDtos;
	}

	private void setupExportSteuercode(FibuexportDto exportDto, EingangsrechnungDto erDto) throws RemoteException {
		if (erDto.getMwstsatzIId() == null 
				|| erDto.getReversechargeartId() == null)
			return;
		
		ReversechargeartId rcId = Helper.isTrue(erDto.getBIgErwerb())
				? new ReversechargeartId(getFinanzServiceFac().reversechargeartFindIg(theClientDto).getIId())
				: new ReversechargeartId(erDto.getReversechargeartId());
				
		exportDto.setSteucod(steuercodeCache.getCodeEr(new SteuerfallKey(new MwstsatzId(erDto.getMwstsatzIId()), rcId)));
	}

	private void setupExportSteuercode(FibuexportDto exportDto, EingangsrechnungDto erDto, EingangsrechnungKontierungDto kontierungDto) throws RemoteException {
		if (kontierungDto.getMwstsatzIId() == null
				|| kontierungDto.getReversechargeartId() == null)
			return;
		
		ReversechargeartId rcId = new ReversechargeartId(kontierungDto.getReversechargeartId());
		if (FinanzServiceFac.ReversechargeArt.OHNE.equals(
					rcCache.getValueOfKey(kontierungDto.getReversechargeartId()).getCNr())
				&& Helper.isTrue(erDto.getBIgErwerb())) {
			// TODO Ist das so korrekt, dass IG-Erwerb Flag nur fuer RC-Art OHNE wirkt?
			 rcId = new ReversechargeartId(getFinanzServiceFac().reversechargeartFindIg(theClientDto).getIId());
		}
		
		exportDto.setSteucod(steuercodeCache.getCodeEr(new SteuerfallKey(new MwstsatzId(kontierungDto.getMwstsatzIId()), rcId)));
	}

	private void updateGegenkontenMehrfachkontierung(Date stichtag, FibuexportDto[] exportDtos,
			EingangsrechnungDto erDto, EingangsrechnungKontierungDto[] erKontoDto, LieferantDto lieferantDto,
			KontoDto kreditorenkontoDto, String laenderartCNr, ZahlungszielDto zzDto) throws RemoteException {

		if (erDto.isAnzahlung()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());
		}

		for (int i = 0; i < erKontoDto.length; i++) {
			FibuexportDto exportDto = exportDtos[i + 1];

			setupExport(exportDto, kreditorenkontoDto);
			setupExportFromER(exportDto, erDto);
			setupExportFromLieferant(exportDto, lieferantDto);
			setupExportFromLaenderart(exportDto, laenderartCNr);
			setupExportFromZahlungsziel(exportDto, zzDto);
			setupExportFromKontierung(exportDto, erKontoDto[i]);
			setupExportFromSteuerbetrag(exportDto, erKontoDto[i], erDto.getNKurs());
			setupExportFromNettobetrag(exportDto, erKontoDto[i], Helper.short2boolean(erDto.getBIgErwerb()),
					erDto.getNKurs());
			setupExportSteuercode(exportDto, erDto, erKontoDto[i]);

			// PJ 17006
			// exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
			exportDto.setValutadatum(stichtag);
			exportDto.setWaehrung(theClientDto.getSMandantenwaehrung());
		}

		setupExportKontierungsDifferenz(exportDtos);
	}

	private void setupExport(FibuexportDto exportDto, KontoDto kreditorenkontoDto) {
		exportDto.setHabenbetrag(null);
		exportDto.setHabenbetragFW(null);
		exportDto.setBelegart(BELEGART_ER);
		exportDto.setGkto(kreditorenkontoDto);
	}

	private void setupExportKontierungsDifferenz(FibuexportDto[] exportDtos, BigDecimal summekontierung) {
		if (summekontierung.compareTo(exportDtos[0].getHabenbetragBD()) == 0) {
			return;
		}

		BigDecimal diff = summekontierung.subtract(exportDtos[0].getHabenbetragBD());
		myLogger.info("Kontierungsdifferenz: " + diff.toPlainString());

		evtLogger.debug("Kontierungsdifferenz", LogEventProducer.create(exportDtos, diff));

		if (diff.abs().compareTo(new BigDecimal("0.011")) >= 0) {
			myLogger.info("Kontierungsdifferenz " + diff.toPlainString() + " > 0.011. Ignoriert.");
			return;
		}

		BigDecimal maxBetrag = BigDecimal.ZERO;
		int maxIndex = -1;
		for (int i = 0; i < exportDtos.length; i++) {
			if (exportDtos[i].getSollbetragBD().compareTo(maxBetrag) > 0) {
				maxIndex = i;
				maxBetrag = exportDtos[i].getSollbetragBD();
			}
		}
		if (maxIndex != -1) {
			exportDtos[maxIndex].setSollbetrag(exportDtos[maxIndex].getSollbetragBD().subtract(diff));
		}
	}

	private void setupExportKontierungsDifferenz(FibuexportDto[] exportDtos) {
		BigDecimal summeKontierung = summeKontierung(exportDtos);
		BigDecimal diff = summeKontierung.subtract(exportDtos[0].getHabenbetragBD());
		if (diff.signum() == 0) {
			return;
		}

		evtLogger.info("Kontierungsdifferenz", LogEventProducer.create(exportDtos, diff));

		if (diff.abs().compareTo(new BigDecimal("0.011")) >= 0) {
			return;
		}

		int maxIndex = -1;
		BigDecimal maxBetrag = BigDecimal.ZERO;
		for (int i = 1; i < exportDtos.length; i++) {
			if (exportDtos[i].getSollbetragBD().abs().compareTo(maxBetrag) > 0) {
				maxIndex = i;
				maxBetrag = exportDtos[i].getSollbetragBD().abs();
			}
		}

		if (maxIndex != -1) {
			exportDtos[maxIndex].setSollbetrag(exportDtos[maxIndex].getSollbetragBD().subtract(diff));
		}
	}

	private BigDecimal summeKontierung(FibuexportDto[] exportDtos) {
		BigDecimal summeKontierung = BigDecimal.ZERO;

		for (int i = 1; i < exportDtos.length; i++) {
			boolean isReversecharge = exportDtos[i].getReversechargeartId() == null ? false
					: !FinanzServiceFac.ReversechargeArt.OHNE
							.equals(rcCache.getValueOfKey(exportDtos[i].getReversechargeartId()).getCNr());
			if (!isReversecharge) {
				summeKontierung = summeKontierung.add(exportDtos[i].getSteuerBD());
			}
			summeKontierung = summeKontierung.add(exportDtos[i].getSollbetragBD());
		}

		return summeKontierung;
	}

	private void setupExportFromLieferant(FibuexportDto exportDto, LieferantDto lieferantDto) {
		exportDto.setPartnerDto(lieferantDto.getPartnerDto());
		exportDto.setUidNummer(lieferantDto.getPartnerDto().getCUid());
	}

	private void setupExportFromLaenderart(FibuexportDto exportDto, String laenderartCnr) {
		exportDto.setLaenderartCNr(laenderartCnr);
	}

	private void setupExportFromZahlungsziel(FibuexportDto exportDto, ZahlungszielDto zzDto) {
		if (zzDto == null)
			return;
		
		exportDto.setZahlungszielDto(zzDto);
		Date tFaelligkeit = Helper.addiereTageZuDatum(exportDto.getBelegdatum(), zzDto.getAnzahlZieltageFuerNetto());
		exportDto.setFaelligkeitsdatum(tFaelligkeit);
	}

	private void setupExportFromER(FibuexportDto exportDto, EingangsrechnungDto erDto) {
		exportDto.setOPNummer(erDto.getCLieferantenrechnungsnummer());
		exportDto.setBelegnr(erDto.getCNr());
		exportDto.setBelegdatum(erDto.getDBelegdatum());
		exportDto.setKurs(erDto.getNKurs());
		exportDto.setFremdwaehrung(erDto.getWaehrungCNr());
		exportDto.setText(erDto.getCText());
		exportDto.setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
	}

	private void setupExportFromKostenstelle(FibuexportDto exportDto, Integer kostenstellenId) {
		KostenstelleDto kstDtoHW = getSystemFac().kostenstelleFindByPrimaryKey(kostenstellenId);
		exportDto.setKost(kstDtoHW);
	}

	private void setupExportFromKonto(FibuexportDto exportDto, Integer kontoId) throws RemoteException {
		KontoDto kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoId);
		exportDto.setKonto(kontoDtoGegenkonto);
	}

	private void setupExportFromMwstsatz(FibuexportDto exportDto, Integer mwstsatzId) {
		MwstsatzDto mwstDto = getMwstsatz(mwstsatzId);
		exportDto.setMwstsatz(mwstDto);
	}

	private BigDecimal setupExportFromSteuerbetrag(FibuexportDto exportDto, EingangsrechnungKontierungDto kontierungDto,
			BigDecimal kurs) {
		// Steuerbetrag ist der UST-Betrag
		BigDecimal bdBetragUstFW = kontierungDto.getNBetragUst();
		BigDecimal bdBetragUst = Helper.rundeKaufmaennisch(bdBetragUstFW.multiply(kurs), FinanzFac.NACHKOMMASTELLEN);
		exportDto.setSteuer(bdBetragUst);
		exportDto.setSteuerFW(bdBetragUstFW);
		return bdBetragUst;
	}

	/**
	 * Den Nettobetrag ermitteln und setzen
	 * <p>
	 * <b>Sollbetrag ist netto. Achtung: Kontierung ist immer in
	 * Rechnungswaehrung!</b>
	 * </p>
	 * 
	 * @param exportDto
	 * @param kontierungDto
	 * @param kurs
	 * @return
	 */
	private BigDecimal setupExportFromNettobetrag(FibuexportDto exportDto, EingangsrechnungKontierungDto kontierungDto,
			boolean isIgErwerb, BigDecimal kurs) {
		BigDecimal bdNettobetragFW;

		boolean isReversecharge = kontierungDto.getReversechargeartId() == null ? false
				: !FinanzServiceFac.ReversechargeArt.OHNE
						.equals(rcCache.getValueOfKey(kontierungDto.getReversechargeartId()).getCNr());
		if (isIgErwerb || isReversecharge) {
			// die Steuer ist nur theoretisch, daher Betrag ohne Steuer
			bdNettobetragFW = kontierungDto.getNBetrag();
		} else {
			bdNettobetragFW = kontierungDto.getNBetrag().subtract(kontierungDto.getNBetragUst());
		}

		BigDecimal bdNettobetrag = Helper.rundeKaufmaennisch(bdNettobetragFW.multiply(kurs),
				FinanzFac.NACHKOMMASTELLEN);

		exportDto.setSollbetrag(bdNettobetrag);
		exportDto.setSollbetragFW(bdNettobetragFW);
		return bdNettobetrag;
	}

	private void setupExportFromKontierung(FibuexportDto exportDto, EingangsrechnungKontierungDto kontierungDto)
			throws RemoteException {
		setupExportFromKostenstelle(exportDto, kontierungDto.getKostenstelleIId());
		setupExportFromKonto(exportDto, kontierungDto.getKontoIId());
		setupExportFromMwstsatz(exportDto, kontierungDto.getMwstsatzIId());
		exportDto.setCKommentar(kontierungDto.getCKommentar());
		exportDto.setReversechargeartId(kontierungDto.getReversechargeartId());
		boolean isReversecharge = false;
		if (exportDto.getReversechargeartId() != null) {
			isReversecharge = !FinanzServiceFac.ReversechargeArt.OHNE
					.equals(rcCache.getValueOfKey(kontierungDto.getReversechargeartId()).getCNr());
		}
		exportDto.setBReverseCharge(isReversecharge);
	}

	private FibuexportDto[] getExportEROriginal(Integer eingangsrechnungIId, Date stichtag) {
		FibuexportDto[] exportDtos = null;
		try {
			int iZeilen = 1; // buchung auf kreditorenkonto
			int iAnzahlGegenkonten;
			EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			boolean bAnzahlung = erDto.isAnzahlung();

			// PJ19169
			if (Helper.short2boolean(erDto.getBMitpositionen()) == true && erDto.getTGedruckt() == null) {

				ArrayList al = new ArrayList();
				al.add(erDto.getCNr());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_ER_MIT_POSITIONEN_NOCH_NICHT_GEDRUCKT, al,
						new Exception("ER mit Positionen noch nicht aktiviert/gedruckt. " + erDto.getCNr()));
			}

			// Nur Belege mit Wert ungleich 0 exportieren
			if (erDto.getNBetrag().signum() != 0) {
				// Pruefen und Laden der eventuell vorhandenen
				// Mehrfachkontierung
				EingangsrechnungKontierungDto[] erKontoDto = super.pruefeEingangsrechnungKontierung(erDto);
				// pruefen, ob die Mehrfachkontierungen den gleichen MWST-Satz
				// haben
				boolean bUSTSatzGleich = true;
				if (erKontoDto != null) {
					for (int i = 0; i < erKontoDto.length - 1; i++) {
						if (!erKontoDto[i].getMwstsatzIId().equals(erKontoDto[i + 1].getMwstsatzIId())) {
							bUSTSatzGleich = false;
							break;
						}
					}
				}
				// Lieferant holen und Daten pruefen
				LieferantDto lieferantDto = super.pruefeEingangsrechnungLieferant(erDto);
				// Holen des Lieferantenkontos
				KontoDto kreditorenkontoDto = getFinanzFac()
						.kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto());
				// Einfache Kontierung?
				if (erDto.getKontoIId() != null) {
					iAnzahlGegenkonten = 1;
				}
				// sonst Mehrfachkontierung
				else {
					if (bAnzahlung)
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
								"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());
					iAnzahlGegenkonten = erKontoDto.length;
				}
				// Anzahl der Buchungszeilen
				iZeilen = 1 + iAnzahlGegenkonten;
				// jetzt wird das Daten-Array zusammengebaut
				exportDtos = new FibuexportDto[iZeilen];
				boolean isErReversecharge = isEingangsrechnungDtoReversecharge(erDto);
				for (int i = 0; i < exportDtos.length; i++) {
					exportDtos[i] = new FibuexportDto();
					exportDtos[i].setIGeschaeftsjahr(erDto.getIGeschaeftsjahr());
					exportDtos[i].setBReverseCharge(isErReversecharge);
				}
				// die Buchung auf Kreditorenkonto ist immer die erste.
				int iZeilePersonenkonto = 0;

				// Die Buchungszeile fuers Kreditorenkonto
				exportDtos[iZeilePersonenkonto].setKonto(kreditorenkontoDto);
				Integer kontoIIdAnzahlungVerr = null;
				if (bAnzahlung) {
					// bei Anzahlungsrechnung auf Konto
					// "Anzahlung Gegeben Verrechnet" buchen

					FinanzamtDto finanzamtDto = getFinanzamt(kreditorenkontoDto.getFinanzamtIId());
//					kontoIIdAnzahlungVerr = Helper.short2boolean(erDto
//							.getBReversecharge()) ? finanzamtDto
//							.getKontoIIdRCAnzahlungGegebenVerr() : finanzamtDto
//							.getKontoIIdAnzahlungGegebenVerr();
					kontoIIdAnzahlungVerr = finanzamtDto.getKontoIIdAnzahlungGegebenVerr();
					if (kontoIIdAnzahlungVerr == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT,
								"Verrechnungskonto Geleistete Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung "
										+ erDto.getCNr());
					}

					// SP3930 Das Anzahlungskonto muss uebersetzt werden (mehrere Finanzaemter)
					Timestamp belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
					String laenderartCnr = getLaenderartZuPartner(
							lieferantDto.getPartnerDto(), belegDatum);
					KontoDto kDto = uebersetzeKontoNachLandBzwLaenderart(kontoIIdAnzahlungVerr, laenderartCnr,
							kreditorenkontoDto.getFinanzamtIId(), erDto.getMandantCNr(), erDto,
							lieferantDto.getPartnerDto().getLandplzortDto().getIlandID(),
							erDto.getReversechargeartId(), belegDatum);
					kontoIIdAnzahlungVerr = kDto.getIId();
				}

				// Gegenkonto / Kostenstelle / MWST-Satz
				if (iAnzahlGegenkonten > 1) {
					// wenn es mehrere Gegenkonten gibt
					exportDtos[iZeilePersonenkonto].setGkto(null);
					// wenn alle Mehrfachkontierungen den gleichen UST-Satz
					// haben,
					// dann kommt auch hier der MWST-Satz rein
					if (bUSTSatzGleich) {
						MwstsatzDto mwstDto = getMwstsatz(erKontoDto[0].getMwstsatzIId());
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
					}
				} else {
					// nur ein Gegenkonto, aber 2 Moeglichkeiten:
					// 1. Das Gegenkonto steht in den Kopfdaten
					KontoDto kontoDtoGegenkonto = null;
					if (erKontoDto == null) {
						if (bAnzahlung) {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
						} else {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
						}
						exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
						// Kostenstelle
						Integer kstIId = erDto.getKostenstelleIId();
						KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kstIId);
						exportDtos[iZeilePersonenkonto].setKost(kstDto);
						// MWST-Satz
						Integer mwstIId = erDto.getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iZeilePersonenkonto].setMwstsatz(mwstDto);
						// MWST-Betrag
						exportDtos[iZeilePersonenkonto].setSteuer(erDto.getNUstBetrag());
						exportDtos[iZeilePersonenkonto].setSteuerFW(erDto.getNUstBetragfw());
					}
					// 2. Es gibt nur einen Mehrfachkontierungseintrag mit dem
					// ganzen ER-Wert
					else {
						if (bAnzahlung) {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
						} else {
							kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erKontoDto[0].getKontoIId());
						}
						exportDtos[iZeilePersonenkonto].setGkto(kontoDtoGegenkonto);
						/**
						 * @todo unterscheiden: alle mit gleicher UST oder mit verschiedener PJ 4270
						 */
						exportDtos[iZeilePersonenkonto].setSteuer(new BigDecimal(0));
						exportDtos[iZeilePersonenkonto].setSteuerFW(new BigDecimal(0));
					}
				}
				exportDtos[iZeilePersonenkonto].setOPNummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setBelegnr(erDto.getCNr());
				exportDtos[iZeilePersonenkonto].setBelegdatum(erDto.getDBelegdatum());

				// PJ 17006
				// exportDtos[iZeilePersonenkonto].setValutadatum(erDto.getDFreigabedatum());
				exportDtos[iZeilePersonenkonto].setValutadatum(stichtag);

				exportDtos[iZeilePersonenkonto].setWaehrung(theClientDto.getSMandantenwaehrung());
				// Sollbetrag ist null
				exportDtos[iZeilePersonenkonto].setSollbetrag(null);
				exportDtos[iZeilePersonenkonto].setSollbetragFW(null);
				// Bruttobetrag im Haben
				exportDtos[iZeilePersonenkonto].setHabenbetrag(erDto.getNBetrag());
				exportDtos[iZeilePersonenkonto].setHabenbetragFW(erDto.getNBetragfw());
				// Steuer ist null
				exportDtos[iZeilePersonenkonto].setSteuer(null);
				exportDtos[iZeilePersonenkonto].setSteuerFW(null);
				// MWST-Betrag
				exportDtos[iZeilePersonenkonto].setFremdwaehrung(erDto.getWaehrungCNr());
				exportDtos[iZeilePersonenkonto].setBelegart(BELEGART_ER);
				exportDtos[iZeilePersonenkonto].setPartnerDto(lieferantDto.getPartnerDto());
				String laenderartCNr = null;
				boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG,
						theClientDto);
				Timestamp belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
				if (hatFibu) {
					laenderartCNr = getLaenderartZuKonto(lieferantDto.getPartnerDto(),
							lieferantDto.getKontoIIdKreditorenkonto(), belegDatum);
				} else {
					laenderartCNr = super.getLaenderartZuPartner(
							lieferantDto.getPartnerDto(), belegDatum);
				}
				exportDtos[iZeilePersonenkonto].setLaenderartCNr(laenderartCNr);

				ZahlungszielDto zzDto = getZahlungsziel(erDto.getZahlungszielIId());
				exportDtos[iZeilePersonenkonto].setZahlungszielDto(zzDto);
				exportDtos[iZeilePersonenkonto].setText(erDto.getCText());
				exportDtos[iZeilePersonenkonto].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
				exportDtos[iZeilePersonenkonto].setUidNummer(lieferantDto.getPartnerDto().getCUid());
				// Gegenbuchung, wenn keine Mehrfachkontierung
				if (erKontoDto == null) {
					final int iZeileGegenbuchung = 1;

					// Kst nicht pruefen, da nur das konto zaehlt
					KostenstelleDto kstDtoHW = getSystemFac().kostenstelleFindByPrimaryKey(erDto.getKostenstelleIId());
					KontoDto kontoDtoGegenkonto = null;
					if (bAnzahlung) {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(kontoIIdAnzahlungVerr);
					} else {
						kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erDto.getKontoIId());
					}
					// buchung aufs HW-Konto
					exportDtos[iZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
					exportDtos[iZeileGegenbuchung].setGkto(kreditorenkontoDto);
					exportDtos[iZeileGegenbuchung].setOPNummer(erDto.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setBelegnr(erDto.getCNr());
					exportDtos[iZeileGegenbuchung].setBelegdatum(erDto.getDBelegdatum());

					// PJ 17006
					// exportDtos[iZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
					exportDtos[iZeileGegenbuchung].setValutadatum(stichtag);

					exportDtos[iZeileGegenbuchung].setWaehrung(theClientDto.getSMandantenwaehrung());
					if (Helper.short2boolean(erDto.getBIgErwerb()) || isEingangsrechnungDtoReversecharge(erDto)) {
						// die Steuer ist nur theoretisch, daher Betrag ohne
						// Steuer
						exportDtos[iZeileGegenbuchung].setSollbetrag(erDto.getNBetrag());
						exportDtos[iZeileGegenbuchung].setSollbetragFW(erDto.getNBetragfw());
					} else {
						// Sollbetrag ist brutto
						exportDtos[iZeileGegenbuchung]
								.setSollbetrag(erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
						exportDtos[iZeileGegenbuchung]
								.setSollbetragFW(erDto.getNBetragfw().subtract(erDto.getNUstBetragfw()));
					}
					BigDecimal kurs = new BigDecimal(1).divide(erDto.getNKurs(), 6, BigDecimal.ROUND_HALF_EVEN);
					exportDtos[iZeileGegenbuchung].setKurs(kurs);
					// Habenbetrag ist null
					exportDtos[iZeileGegenbuchung].setHabenbetrag(null);
					exportDtos[iZeileGegenbuchung].setHabenbetragFW(null);
					// Steuerbetrag ist hier
					exportDtos[iZeileGegenbuchung].setSteuer(erDto.getNUstBetrag());
					exportDtos[iZeileGegenbuchung].setSteuerFW(erDto.getNUstBetragfw());
					exportDtos[iZeileGegenbuchung].setFremdwaehrung(erDto.getWaehrungCNr());
					exportDtos[iZeileGegenbuchung].setKost(kstDtoHW);
					exportDtos[iZeileGegenbuchung].setBelegart(BELEGART_ER);
					exportDtos[iZeileGegenbuchung].setPartnerDto(lieferantDto.getPartnerDto());
					// mwst-satz
					Integer mwstIId = erDto.getMwstsatzIId();
					MwstsatzDto mwstDto = getMwstsatz(mwstIId);
					exportDtos[iZeileGegenbuchung].setMwstsatz(mwstDto);
					exportDtos[iZeileGegenbuchung].setLaenderartCNr(laenderartCNr);
					exportDtos[iZeileGegenbuchung].setZahlungszielDto(zzDto);
					exportDtos[iZeileGegenbuchung].setText(erDto.getCText());
					exportDtos[iZeileGegenbuchung].setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
					exportDtos[iZeileGegenbuchung].setUidNummer(lieferantDto.getPartnerDto().getCUid());
				} else {
					// Mehrfachkontierung
					// Kst nicht pruefen, da nur das konto zaehlt
					if (bAnzahlung)
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
								"Anzahlung kann nicht mehrfachkontiert sein! ER " + erDto.getCNr());

					BigDecimal summeKontierung = new BigDecimal(0);
					int maxIndex = 0;
					BigDecimal maxBetrag = new BigDecimal(0);
					for (int i = 0; i < erKontoDto.length; i++) {
						final int iAktuelleZeileGegenbuchung = i + 1; // in der
																		// ersten
																		// steht
																		// die
																		// sammelbuchung
						KostenstelleDto kstDtoHW = getSystemFac()
								.kostenstelleFindByPrimaryKey(erKontoDto[i].getKostenstelleIId());
						KontoDto kontoDtoGegenkonto = getFinanzFac().kontoFindByPrimaryKey(erKontoDto[i].getKontoIId());

						exportDtos[iAktuelleZeileGegenbuchung].setCKommentar(erKontoDto[i].getCKommentar());

						// buchung aufs HW-Konto
						exportDtos[iAktuelleZeileGegenbuchung].setKonto(kontoDtoGegenkonto);
						exportDtos[iAktuelleZeileGegenbuchung].setGkto(kreditorenkontoDto);
						exportDtos[iAktuelleZeileGegenbuchung].setOPNummer(erDto.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung].setBelegnr(erDto.getCNr());
						exportDtos[iAktuelleZeileGegenbuchung].setBelegdatum(erDto.getDBelegdatum());

						// PJ 17006
						// exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(erDto.getDFreigabedatum());
						exportDtos[iAktuelleZeileGegenbuchung].setValutadatum(stichtag);

						exportDtos[iAktuelleZeileGegenbuchung].setWaehrung(theClientDto.getSMandantenwaehrung());
						// Sollbetrag ist netto
						// Achtung: Kontierung ist immer in Rechnungswaehrung!
						BigDecimal bdNettobetragFW;
						BigDecimal bdNettobetrag;
						if (Helper.short2boolean(erDto.getBIgErwerb()) || isEingangsrechnungDtoReversecharge(erDto)) {
							// die Steuer ist nur theoretisch, daher Betrag ohne
							// Steuer
							bdNettobetragFW = erKontoDto[i].getNBetrag();
							bdNettobetrag = Helper.rundeKaufmaennisch(bdNettobetragFW.multiply(erDto.getNKurs()),
									FinanzFac.NACHKOMMASTELLEN);
						} else {
							bdNettobetragFW = erKontoDto[i].getNBetrag().subtract(erKontoDto[i].getNBetragUst());
							bdNettobetrag = Helper.rundeKaufmaennisch(bdNettobetragFW.multiply(erDto.getNKurs()),
									FinanzFac.NACHKOMMASTELLEN);
						}
						if (bdNettobetrag.compareTo(maxBetrag) > 0) {
							maxIndex = iAktuelleZeileGegenbuchung;
							maxBetrag = bdNettobetrag;
						}

						exportDtos[iAktuelleZeileGegenbuchung].setKurs(erDto.getNKurs());

						exportDtos[iAktuelleZeileGegenbuchung].setSollbetrag(bdNettobetrag);
						exportDtos[iAktuelleZeileGegenbuchung].setSollbetragFW(bdNettobetragFW);
						// Habenbetrag ist null
						exportDtos[iAktuelleZeileGegenbuchung].setHabenbetrag(null);
						exportDtos[iAktuelleZeileGegenbuchung].setHabenbetragFW(null);
						// Steuerbetrag ist der UST-Betrag
						BigDecimal bdBetragUstFW = erKontoDto[i].getNBetragUst();
						BigDecimal bdBetragUst = Helper.rundeKaufmaennisch(bdBetragUstFW.multiply(erDto.getNKurs()),
								FinanzFac.NACHKOMMASTELLEN);
						exportDtos[iAktuelleZeileGegenbuchung].setSteuer(bdBetragUst);
						exportDtos[iAktuelleZeileGegenbuchung].setSteuerFW(bdBetragUstFW);
						summeKontierung = summeKontierung.add(bdNettobetrag).add(bdBetragUst);
						// Fremdwaehrung
						exportDtos[iAktuelleZeileGegenbuchung].setFremdwaehrung(erDto.getWaehrungCNr());
						exportDtos[iAktuelleZeileGegenbuchung].setKost(kstDtoHW);
						exportDtos[iAktuelleZeileGegenbuchung].setBelegart(BELEGART_ER);
						exportDtos[iAktuelleZeileGegenbuchung].setPartnerDto(lieferantDto.getPartnerDto());
						// mwst-satz
						Integer mwstIId = erKontoDto[i].getMwstsatzIId();
						MwstsatzDto mwstDto = getMwstsatz(mwstIId);
						exportDtos[iAktuelleZeileGegenbuchung].setMwstsatz(mwstDto);
						exportDtos[iAktuelleZeileGegenbuchung].setLaenderartCNr(laenderartCNr);
						exportDtos[iAktuelleZeileGegenbuchung].setZahlungszielDto(zzDto);
						exportDtos[iAktuelleZeileGegenbuchung].setText(erDto.getCText());
						exportDtos[iAktuelleZeileGegenbuchung]
								.setSExterneBelegnummer(erDto.getCLieferantenrechnungsnummer());
						exportDtos[iAktuelleZeileGegenbuchung].setUidNummer(lieferantDto.getPartnerDto().getCUid());
					}
					if (summeKontierung.compareTo(exportDtos[0].getHabenbetragBD()) != 0) {
						BigDecimal diff = summeKontierung.subtract(exportDtos[0].getHabenbetragBD());
						System.out.println("Differenz: " + diff.toString());
						if (diff.abs().compareTo(new BigDecimal("0.011")) < 0)
							exportDtos[maxIndex].setSollbetrag(exportDtos[maxIndex].getSollbetragBD().subtract(diff));
					}
				}
			} else {
				// Beleg mit Wert 0
				exportDtos = new FibuexportDto[0];
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return exportDtos;
	}

	private class FinanceDataEvaluator implements RechnungExportVisitable {
		private String laenderartCnr;
		private RechnungDto rechnungDto;
		private boolean fibu;
		private KundeDto kundeDto;
		private Integer landId;
		private Integer finanzamtId;
		private Integer debitorenKontoUebersteuertId;
		private boolean legeDebitorAn;
		private RechnungVisitor visitor;
		private Integer lieferlandId;
		private ReversechargeartId steuerfallRcId;

		public FinanceDataEvaluator() {
			this(false);
		}

		public FinanceDataEvaluator(boolean legeDebitorAn) {
			this.legeDebitorAn = legeDebitorAn;
		}

		@Override
		public void accept(RechnungVisitor visitor) {
			this.visitor = visitor;
		}

		protected RechnungVisitor getVisitor() {
			if (visitor == null) {
				visitor = new FibuExportRechnungVisitorDummy();
			}
			return visitor;
		}

		public void evaluate(RechnungDto rechnungDto) throws RemoteException {
			this.rechnungDto = rechnungDto;
			laenderartCnr = null;
			kundeDto = null;
			finanzamtId = null;
			debitorenKontoUebersteuertId = null;
			landId = null;
			rcCache = new ReversechargeartCache(theClientDto);

			setFibuEnabled(getMandantFac().hatModulFinanzbuchhaltung(theClientDto));
			evaluateHead();
			evaluatePositions();
		}

		public boolean hasFibu() {
			return fibu;
		}

		public boolean isAnzahlungsRechnung() {
			return RechnungFac.RECHNUNGART_ANZAHLUNG.equals(getRechnungDto().getRechnungartCNr());
		}

		public boolean isSchlussrechnung() {
			return RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG.equals(getRechnungDto().getRechnungartCNr());
		}
		/**
		 * Der Rechnungs-Kunde (ist immer der Rechnungskunde, egal, an welchen Kunden
		 * eine eventuell enthaltene Lieferscheinposition geschickt wird)
		 * 
		 * @return der Rechnungs-Kunde
		 */
		public KundeDto getKundeDto() {
			return kundeDto;
		}

		public RechnungDto getRechnungDto() {
			return rechnungDto;
		}

		/**
		 * Das Land des Rechnungs-Kunden (ist immer der Rechnungskunde, egal, an welchen
		 * Kunden eine eventuell enthaltene Lieferscheinposition geschickt wird)
		 * 
		 * @return Id des Rechnungs-Kunden
		 */
		public Integer getLandId() {
			return landId;
		}

		/**
		 * Hat die Rechnung einen Wert != 0.0000
		 * 
		 * @return true wenn die Rechnung einen (positiven/negativen) Wert hat, false
		 *         wenn noch kein Wert berechnet wurde, oder dieser genau 0.00000 ist
		 */
		public boolean hatRechnungWert() {
			return getRechnungDto().getNWert() != null && getRechnungDto().getNWert().signum() != 0;
		}

		public Integer getFinanzamtId() {
			return finanzamtId;
		}

		public Integer getDebitorenKontoUebersteuertId() {
			return debitorenKontoUebersteuertId;
		}

		public String getLaenderartCnr() {
			// SP4303 Bei Anzahlungsrechnungen gilt die Laenderart (Steuerkategorie) des
			// Debitors, aber nur bei integrierter Fibu (SP8654) 
			if (isAnzahlungsRechnung() && hasFibu()) {
				Integer debitorenId = getDebitorenKontoUebersteuertId();
				if (debitorenId == null) {
					debitorenId = getKundeDto().getIidDebitorenkonto();
				}
				try {
					KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(debitorenId);
					return getFinanzFac().getLaenderartFuerSteuerkategorie(kontoDto.getSteuerkategorieCnr());
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}

			return laenderartCnr;
		}

		protected void setKundeDto(KundeDto kundeDto) {
			this.kundeDto = kundeDto;
		}

		protected void setFibuEnabled(boolean fibuEnabled) {
			this.fibu = fibuEnabled;
		}

		protected void setFinanzamtId(Integer finanzamtId) {
			this.finanzamtId = finanzamtId;
		}

		protected void setLaenderartCnr(String laenderartCnr) {
			this.laenderartCnr = laenderartCnr;
		}

		protected void setLandId(Integer landId) {
			this.landId = landId;
		}

		protected void setLieferlandId(Integer landId) {
			this.lieferlandId = landId;
		}
		
		protected void setSteuerfallRcId(ReversechargeartId steuerfallRcId) {
			this.steuerfallRcId = steuerfallRcId;
		}
		
		public ReversechargeartId getSteuerfallRcId() {
			return steuerfallRcId;
		}

		/**
		 * Das Land des Lieferkunden (dorthin wurde geliefert)
		 * 
		 * @return Id des Lieferkunden-Kunden
		 */
		@Override
		public Integer getLieferlandId() {
			return lieferlandId;
		}

		protected void setDebitorenKontoUebersteuertId(Integer kontoId) {
			this.debitorenKontoUebersteuertId = kontoId;
		}

		protected void evaluateHead() throws RemoteException {
			setKundeDto(pruefeRechnungKunde(getRechnungDto()));
			if (hasFibu()) {
				setLaenderartCnr(
						getLaenderartZuKonto(
								getKundeDto().getPartnerDto(), 
								getKundeDto().getIidDebitorenkonto(),
								getRechnungDto().getTBelegdatum()));
//				setLaenderartCnr(getLaenderartZuKonto(
//						getKundeDto().getPartnerDto(),
//						getKundeDto().getIidDebitorenkonto(),
//						getRechnungDto().isReverseCharge()));
			} else {
				setLaenderartCnr(getLaenderartZuPartner(
						getKundeDto().getPartnerDto(),
						getRechnungDto().getTBelegdatum()));
			}
			Integer relandId = getKundeDto()
					.getPartnerDto().getLandplzortDto().getIlandID();
			setLandId(relandId);
			setLieferlandId(relandId);
			setSteuerfallRcId(verifySteuerfall());

			getVisitor().visit(this);
		}

		protected ReversechargeartId verifySteuerfall() throws RemoteException {
			if (FinanzServiceFac.ReversechargeArt.OHNE.equals(
					rcCache.getValueOfKey(getRechnungDto().getReversechargeartId()).getCNr())
				&& FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID.equals(getLaenderartCnr())) {
					return new ReversechargeartId(getFinanzServiceFac().reversechargeartFindIg(theClientDto).getIId());
			}
			
			return new ReversechargeartId(getRechnungDto().getReversechargeartId());
		}

		protected void evaluatePositions() throws RemoteException {
			if (!hatRechnungWert())
				return;

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungPosition.class);
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNGPOSITION_RECHNUNG_I_ID, getRechnungDto().getIId()));
			for (FLRRechnungPosition rePos : (List<FLRRechnungPosition>) c.list()) {
				if (RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(rePos.getPositionsart_c_nr())) {
					evaluateLieferscheinposition(rePos);
				} else {
					evaluateOtherPosition(rePos);
				}
			}

			if (getFinanzamtId() == null && getKundeDto().getIidDebitorenkonto() != null) {
				KontoDto debitorenKontoDto = getFinanzFac().kontoFindByPrimaryKey(getKundeDto().getIidDebitorenkonto());
				setFinanzamtId(debitorenKontoDto.getFinanzamtIId());
			}
		}

		protected void evaluateOtherPosition(FLRRechnungPosition rePos) {
			getVisitor().visitOtherPosition(this, rePos);
		}

		protected void evaluateLieferscheinposition(FLRRechnungPosition rePos) throws RemoteException {
			if (isAnzahlungsRechnung()) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNGSRECHNUNG_LIEFERSCHEIN_NICHT_ERLAUBT,
						"Lieferscheinposition nicht erlaubt in Anzahlungsrechnung " + getRechnungDto().getCNr());
			}

			FLRLieferschein flrLieferschein = (FLRLieferschein) rePos.getFlrlieferschein();
			if (!flrLieferschein.getFlrkunde().getI_id()
					.equals(flrLieferschein.getFlrkunderechnungsadresse().getI_id())) {
				verifyLieferscheinKunde(flrLieferschein);
			}
			getVisitor().visitLieferscheinPosition(this, rePos);
		}

		protected void verifyLieferscheinKunde(FLRLieferschein flrLieferschein) throws RemoteException {
			if (hasFibu()) {
				verifyLieferscheinKundeFibu(flrLieferschein);
			} else {
				verifyLieferscheinKundeOhneFibu(flrLieferschein);
			}
		}

		protected void verifyLieferscheinKundeOhneFibu(FLRLieferschein flrLieferschein) {
			String laenderartCnr = flrLieferschein.getLaenderart_c_nr();
			if (laenderartCnr != null) {
				// PJ20219 Lieferschein-Laenderart uebersteuert ermittelte
				setLaenderartCnr(laenderartCnr);
			}

			// PJ 17385 nur uebersteuern wenn Laender
			// unterschiedlich
			if (getFinanzServiceFac().isFibuLandunterschiedlich(getKundeDto().getPartnerDto().getIId(),
					flrLieferschein.getFlrkunde().getFlrpartner().getI_id())) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKeyOhneExc(
						flrLieferschein.getFlrkunde().getFlrpartner().getI_id(), theClientDto);
				if (laenderartCnr == null) {
					setLaenderartCnr(getLaenderartZuPartner(partnerDto, 
							Helper.asTimestamp(flrLieferschein.getD_belegdatum())));
				}
				setLieferlandId(partnerDto.getLandplzortDto().getIlandID());
			}
		}

		// SP3575
		// Spezialbehandlung wenn Lieferscheindebitor im
		// gleichen Land wie der "RechnungsDebitor" ist:
		// Eigentlich zieht immer die
		// Lieferscheinadresse (-> der
		// Lieferscheindebitor).
		// Aber wenn ich Rechnungen schreibe und immer
		// nur die andere Lieferadresse habe, dann
		// unterscheiden sich die OP-Liste der Rechnung
		// von
		// Fibu-Saldenliste, weil ich nach
		// RechnungsDebitor suche, aber nur den
		// LieferscheinDebitor
		// finde. Bisher wurde immer an den
		// RechnungsDebitor gebucht, ausser es war ein
		// anderes
		// Land. Jetzt buchen wir immer an den
		// LieferscheinDebitor, ausser halt wenn das
		// Land gleich ist,
		// dann wird wieder der RechnungsDebitor
		// hergenommen.
		protected void verifyLieferscheinKundeFibu(FLRLieferschein flrLieferschein) throws RemoteException {
			String reLaenderartCnr = getLaenderartCnr();

			String laenderartCnr = flrLieferschein.getLaenderart_c_nr();
			if (laenderartCnr != null) {
				// PJ20219 Lieferschein-Laenderart uebersteuert ermittelte
				setLaenderartCnr(laenderartCnr);
			}

			KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(flrLieferschein.getFlrkunde().getI_id(),
					theClientDto);
//			boolean sameCountry = isSameCountry(getKundeDto(),
//					lieferscheinKundeDto);
//			if(!sameCountry) {
			boolean sameLaenderart = laenderartCnr == null ? isSameCountry(getKundeDto(), lieferscheinKundeDto)
					: reLaenderartCnr.equals(getLaenderartCnr());
			if (!sameLaenderart) {
				KontoDto reDebitorenKonto = getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto());
				KontoDto lsDebitorenKonto = lieferscheinKundeDto.getIidDebitorenkonto() == null ? reDebitorenKonto
						: getFinanzFac().kontoFindByPrimaryKey(lieferscheinKundeDto.getIidDebitorenkonto());
				if (!reDebitorenKonto.getFinanzamtIId().equals(lsDebitorenKonto.getFinanzamtIId())) {
					evtLogger.debug("rechnungsDebitor", LogEventProducer.create(reDebitorenKonto));
					evtLogger.debug("lieferscheinDebitor", LogEventProducer.create(lsDebitorenKonto));

					throw getExceptionFinanzamIIdUnterschiedlich(reDebitorenKonto, lsDebitorenKonto);
				}

				lieferscheinKundeDto = verifyDebitorenKonto(flrLieferschein, lieferscheinKundeDto);

				if (laenderartCnr == null) {
					setLaenderartCnr(getLaenderartZuKonto(lieferscheinKundeDto.getPartnerDto(),
							lieferscheinKundeDto.getIidDebitorenkonto(),
							new Timestamp(flrLieferschein.getD_belegdatum().getTime())));
				}

				// PJ20219 Konto-Landzuweisung aus dem Lieferland
				setLieferlandId(lieferscheinKundeDto.getPartnerDto().getLandplzortDto().getIlandID());

				// PJ 17120
				KontoDto lieferscheinDebitorenkonto = getFinanzFac()
						.kontoFindByPrimaryKey(lieferscheinKundeDto.getIidDebitorenkonto());
				setFinanzamtId(lieferscheinDebitorenkonto.getFinanzamtIId());
				setDebitorenKontoUebersteuertId(lieferscheinKundeDto.getIidDebitorenkonto());
				return;
			}

// SP9625: Kein konto anlegen, es wird sowieso der RE-Debitor verwendet			
//			verifyDebitorenKonto(flrLieferschein, lieferscheinKundeDto);
		}

		protected KundeDto verifyDebitorenKonto(FLRLieferschein flrLieferschein, KundeDto lsKundeDto)
				throws RemoteException {
			if (lsKundeDto.getIidDebitorenkonto() != null) {
				return lsKundeDto;
			}

			return createDebitorenKonto(flrLieferschein, lsKundeDto);
		}

		protected KundeDto createDebitorenKonto(FLRLieferschein flrLieferschein, KundeDto lsKundeDto)
				throws RemoteException {
			if (!legeDebitorAn) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT,
						"Debitorenkonto f\u00FCr Lieferschein nicht definiert. ",
						lsKundeDto.getPartnerDto().formatName() + ", Lieferschein: " + flrLieferschein.getC_nr());
			}

			KontoDto ktoDto = getKundeFac().createDebitorenkontoZuKundenAutomatisch(lsKundeDto.getIId(), false, null,
					theClientDto);
			lsKundeDto.setIDebitorenkontoAsIntegerNotiId(new Integer(ktoDto.getCNr()));
			getKundeFac().updateKunde(lsKundeDto, theClientDto);
			lsKundeDto = getKundeFac().kundeFindByPrimaryKey(flrLieferschein.getFlrkunde().getI_id(), theClientDto);
			return lsKundeDto;
		}
	}

	public interface RechnungExportVisitable {
		void accept(RechnungVisitor visitor);

		KundeDto getKundeDto();

		RechnungDto getRechnungDto();

		Integer getFinanzamtId();

		Integer getDebitorenKontoUebersteuertId();

		String getLaenderartCnr();

		Integer getLandId();

		Integer getLieferlandId();

		boolean hasFibu();

		boolean isAnzahlungsRechnung();
		
		ReversechargeartId getSteuerfallRcId();
		
		boolean isSchlussrechnung();
	}

	public interface RechnungVisitor {
		void visit(RechnungExportVisitable visitable);

		void visitLieferscheinPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos);

		void visitOtherPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos);
	}

	public class FibuExportRechnungVisitor implements RechnungVisitor {
		// Die Werte zu den Konten werden in HashMaps gesammelt.
		private AddableHashMap<Integer, BigDecimal> hmNettoFW = new AddableHashMap<Integer, BigDecimal>();
		private AddableHashMap<Integer, BigDecimal> hmBruttoFW = new AddableHashMap<Integer, BigDecimal>();
		private AddableHashMap<Integer, String> hmArtikelGruppen = new AddableHashMap<Integer, String>();
		private AddableHashMap<Integer, MwstsatzDto> hmMwstsatz = new AddableHashMap<Integer, MwstsatzDto>();
		private WaehrungDto uebersteuerteMandantenwaherung = null;
		private boolean bAllgemeinerRabattExtraBuchen = false;
		private KontoDto kontoDtoSonstige = null;
		private KontoDto kontoDtoAllgemeinerRabatt = null;
//		private RechnungDto rechnungDto = null ;
		private RechnungExportVisitable theVisitable = null;

		private BigDecimal fibuDifferenzErlaubt = null;

		public FibuExportRechnungVisitor() throws RemoteException {
			ParametermandantDto parameterKontoRabatt = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_RABATT_KONTO);
			if (parameterKontoRabatt.getCWert().trim().length() > 0) {
				kontoDtoAllgemeinerRabatt = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(
						parameterKontoRabatt.getCWert().trim(), FinanzServiceFac.KONTOTYP_SACHKONTO,
						theClientDto.getMandant(), theClientDto);
				if (kontoDtoAllgemeinerRabatt == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEFAULT_RABATT_KONTO_FEHLT,
							new Exception("Konto " + parameterKontoRabatt.getCWert()
									+ " f\u00FCr Allgemeinen Rabatt ist nicht vorhanden"));
				}
				bAllgemeinerRabattExtraBuchen = true;
			}

			ParametermandantDto parameterKontoSonstige = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR);
			kontoDtoSonstige = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(parameterKontoSonstige.getCWert(),
					FinanzServiceFac.KONTOTYP_SACHKONTO, theClientDto.getMandant(), theClientDto);
			if (kontoDtoSonstige == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR_FEHLT,
						new Exception(
								"Konto f\u00FCr Positionen ohne Artikelgruppen ist nicht definiert\nDefinieren Sie den Parameter "
										+ ParameterFac.PARAMETER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR + "."));
			}

			// Holen einer eventuell vorhandenen uebersteuerten Waehrung
			ParametermandantDto paramterWaehrungUebersteuert = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
					ParameterFac.PARAMETER_FINANZ_EXPORT_UEBERSTEUERTE_MANDANTENWAEHRUNG);
			String sWaehrung = paramterWaehrungUebersteuert.getCWert();
			if (sWaehrung != null && !"".equals(sWaehrung) && !sWaehrung.equals(theClientDto.getSMandantenwaehrung())) {
				// Mandantenwaehrung wird uebersteuert
				try {
					uebersteuerteMandantenwaherung = getLocaleFac().waehrungFindByPrimaryKey(sWaehrung);
				} catch (Exception e) {
					if (e.getCause() instanceof EJBExceptionLP) {
						// Wenn Waehrung nicht vorhanden dann Fehler
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN,
								new Exception("\u00DCbersteuerte W\u00E4hrung nicht gefunden:"));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(sWaehrung);
						ex.setAlInfoForTheClient(a);
						throw ex;
					} else {
						throw new EJBExceptionLP(e);
					}
				}
			}

			fibuDifferenzErlaubt = getParameterFac().getErlaubteFibuDifferenz(theClientDto.getMandant());
		}

		@Override
		public void visit(RechnungExportVisitable visitable) {
			this.theVisitable = visitable;
		}

		@Override
		public void visitLieferscheinPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos) {
			try {
				visitLieferscheinPositionThrow(visitable, rePos);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		protected void visitLieferscheinPositionThrow(RechnungExportVisitable visitable, FLRRechnungPosition rePos)
				throws RemoteException {

			/**
			 * @todo MR->MR:passt das? TODO: SK: die Lieferscheinpositionen holen
			 */
			FLRLieferscheinposition[] flrLieferscheinpos = (FLRLieferscheinposition[]) rePos.getFlrlieferschein()
					.getFlrlieferscheinpositionen().toArray(new FLRLieferscheinposition[0]);
			for (int x = 0; x < flrLieferscheinpos.length; x++) {
				BigDecimal bdNetto = BigDecimal.ZERO;
				if (flrLieferscheinpos[x].getSetartikel_set().size() != 0)
					continue; // Kopf eines Sets NICHT BUCHEN!, in
								// die Fibu gehen nur die Teile
				
				if(flrLieferscheinpos[x].getN_menge() != null) {
					BigDecimal p = useAllgemeinerRabatt() 
							? flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlag()
							: flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlagminusrabatt();
					if(p != null) {
						bdNetto = p.multiply(flrLieferscheinpos[x].getN_menge());
					}				
				}
				
//				if (bAllgemeinerRabattExtraBuchen) {
//					if (flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlag() != null) {
//						bdNetto = flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlag()
//								.multiply(flrLieferscheinpos[x].getN_menge());
//					} else {
//						bdNetto = BigDecimal.ZERO;
//					}
//				} else {
//					if (flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlagminusrabatt() != null) {
//						bdNetto = flrLieferscheinpos[x].getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()
//								.multiply(flrLieferscheinpos[x].getN_menge());
//					} else {
//						bdNetto = BigDecimal.ZERO;
//					}
//				}
				
				bdNetto = Helper.rundeGeldbetrag(bdNetto);
				BigDecimal steuerbetrag = calcSteuerbetrag(bdNetto, flrLieferscheinpos[x].getFlrmwstsatz());
				BigDecimal bdBrutto = Helper.rundeGeldbetrag(bdNetto.add(steuerbetrag));
				// bdNetto = rePos.getN_nettoeinzelpreis();
				// bdBrutto = rePos.getN_bruttoeinzelpreis();
				// Wenn eine Handeingabe dann Default Konto
				// if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE.
				// equals(flrLieferscheinpos[x].getPositionsart_c_nr())){
				// //Das Default Konto auch uebersetzen
				// kontoDtoSonstige =
				// uebersetzeKontoNachLandBzwLaenderart(
				// kontoDtoSonstige.getIId(), sLaenderart, reDto,
				// iLand);
				// }
				if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
						.equals(flrLieferscheinpos[x].getPositionsart_c_nr())) {
					if (flrLieferscheinpos[x].getFlrartikel() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
								new Exception("Keine Artikelgruppe f\u00FCr Artikel definiert:"
										+ flrLieferscheinpos[x].getC_bez()));
						ArrayList<Object> al = new ArrayList<Object>();
						al.add(flrLieferscheinpos[x].getC_bez());
						ex.setAlInfoForTheClient(al);
						throw ex;
					}
					if (flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
								new Exception("Keine Artikelgruppe f\u00FCr Artikel definiert:"
										+ flrLieferscheinpos[x].getFlrartikel().getC_nr()));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add("Rechnung: " + rePos.getFlrrechnung().getC_nr() + " \n" + "Lieferschein: "
								+ flrLieferscheinpos[x].getFlrlieferschein().getC_nr() + " \n" + "Artikel: "
								+ flrLieferscheinpos[x].getFlrartikel().getC_nr());
						ex.setAlInfoForTheClient(a);
						throw ex;
					}
					Integer artgruId = flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe().getI_id();
					ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artgruId, theClientDto);
					if (artgruDto.getKontoIId() == null) {
//					if (flrLieferscheinpos[x].getFlrartikel()
//							.getFlrartikelgruppe().getFlrkonto() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
								new Exception("Kein Konto f\u00FCr Artikelgruppe definiert: \n"
										+ flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe().getC_nr()));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(flrLieferscheinpos[x].getFlrartikel().getC_nr());
						a.add(flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe().getC_nr());
						ex.setAlInfoForTheClient(a);
						throw ex;

					}
				}
				if ((LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE
						.equals(flrLieferscheinpos[x].getPositionsart_c_nr()))
						|| (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
								.equals(flrLieferscheinpos[x].getPositionsart_c_nr()))) {
					Integer iLieferscheinPosKontoIId = null;
					if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE
							.equals(flrLieferscheinpos[x].getPositionsart_c_nr())) {
						iLieferscheinPosKontoIId = kontoDtoSonstige.getIId();
					} else {
						Integer artgruId = flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe().getI_id();
						ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artgruId, theClientDto);
						iLieferscheinPosKontoIId = artgruDto.getKontoIId();
//						iLieferscheinPosKontoIId = flrLieferscheinpos[x]
//								.getFlrartikel()
//								.getFlrartikelgruppe()
//								.getFlrkonto().getI_id();
					}
					if (hmNettoFW.get(iLieferscheinPosKontoIId) != null) {
						bdNetto = bdNetto.add(hmNettoFW.get(iLieferscheinPosKontoIId));
					}
					hmNettoFW.put(iLieferscheinPosKontoIId, bdNetto);
					// Zu jedem Konto Artikelgruppenbezeichnung
					// merken
					if (hmArtikelGruppen.get(iLieferscheinPosKontoIId) == null) {
						if (flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe() == null) {
							if (iLieferscheinPosKontoIId.equals(kontoDtoSonstige.getIId())) {
								hmArtikelGruppen.put(iLieferscheinPosKontoIId, kontoDtoSonstige.getCBez());
							} else {
								KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(iLieferscheinPosKontoIId);
								hmArtikelGruppen.put(iLieferscheinPosKontoIId, kontoDto.getCBez());
							}
						} else {
							hmArtikelGruppen.put(iLieferscheinPosKontoIId,
									flrLieferscheinpos[x].getFlrartikel().getFlrartikelgruppe().getC_nr());
						}
					}
					// MWST-Satz Merken
					MwstsatzDto mwst = getMandantFac()
							.mwstsatzFindByPrimaryKey(flrLieferscheinpos[x].getFlrmwstsatz().getI_id(), theClientDto);
// PJ20219 Kann nicht null werden, ghp 15.05.2018
//					if (mwst == null) {
//						EJBExceptionLP ex = new EJBExceptionLP(
//								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
//								new Exception(
//										"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
//												+ visitable.getRechnungDto().getCNr()));
//						ArrayList<Object> a = new ArrayList<Object>();
//						a.add("Rechnung: " + visitable.getRechnungDto().getCNr());
//						a.add("Lieferschein: "
//								+ flrLieferscheinpos[x]
//										.getFlrlieferschein()
//										.getC_nr());
//						ex.setAlInfoForTheClient(a);
//						throw ex;
//					}
					if (hmMwstsatz.get(iLieferscheinPosKontoIId) == null) {
						// Neu einfuegen
						hmMwstsatz.put(iLieferscheinPosKontoIId, mwst);
					} else {
						if (!hmMwstsatz.get(iLieferscheinPosKontoIId).equals(mwst)) {
							EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
									new Exception("Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
											+ visitable.getRechnungDto().getCNr()));
							ArrayList<Object> a = new ArrayList<Object>();
							a.add("Rechnung: " + visitable.getRechnungDto().getCNr());
							a.add("Lieferschein: " + flrLieferscheinpos[x].getFlrlieferschein().getC_nr());
							ex.setAlInfoForTheClient(a);
							throw ex;
						}
					}
					if (hmBruttoFW.get(iLieferscheinPosKontoIId) != null) {
						bdBrutto = bdBrutto.add(hmBruttoFW.get(iLieferscheinPosKontoIId));
					}
					hmBruttoFW.put(iLieferscheinPosKontoIId, bdBrutto);
				}
			}
		}

		private BigDecimal calcSteuerbetrag(BigDecimal netto, FLRMwstsatz flrMwstsatz) {
			// SP6402 7,7 => 0,077 und nicht 0,08 wegen Rundung
			if (flrMwstsatz != null && flrMwstsatz.getF_mwstsatz() != null) {
				BigDecimal steuersatz = new BigDecimal(flrMwstsatz.getF_mwstsatz().toString());

				return netto.multiply(steuersatz).movePointLeft(2).setScale(FinanzFac.NACHKOMMASTELLEN,
						BigDecimal.ROUND_HALF_EVEN);
			} else {
				return BigDecimal.ZERO.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			}
		}

		@Override
		public void visitOtherPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos) {
			try {
				visitOtherPositionThrow(visitable, rePos);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		
		private boolean useAllgemeinerRabatt() {
			boolean b = bAllgemeinerRabattExtraBuchen;
			if (theVisitable.isAnzahlungsRechnung()) {
				b = false;
			}
			return b;			
		}
		
		protected void visitOtherPositionThrow(RechnungExportVisitable visitable, FLRRechnungPosition rePos)
				throws RemoteException {
			BigDecimal bdNetto = BigDecimal.ZERO;

			if(rePos.getN_menge() != null) {
				BigDecimal p = useAllgemeinerRabatt() 
						? rePos.getN_nettoeinzelpreis_plus_aufschlag()
						: rePos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt();
				if(p != null) {
					bdNetto = p.multiply(rePos.getN_menge());
				}
			}

			myLogger.error("Repos-ID:" + rePos.getI_id() + " " + bdNetto.toPlainString());
			bdNetto = bdNetto.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal steuerbetrag = calcSteuerbetrag(bdNetto, rePos.getFlrmwstsatz());
			BigDecimal bdBrutto = (bdNetto.add(steuerbetrag))
					.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
			// haben die auch eine Artikelgruppe mit Konto
			// if(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr())){
			// //Default Artikelgruppenkonto
			// kontoDtoSonstige =
			// uebersetzeKontoNachLandBzwLaenderart(
			// kontoDtoSonstige.getIId(), sLaenderart, reDto,
			// iLand);
			// }
			if (RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rePos.getPositionsart_c_nr())) {
				if (!visitable.isAnzahlungsRechnung()) {
					// nur pruefen wenn keine Anzahlungsrechnung
					if (rePos.getFlrartikel() == null || rePos.getFlrartikel().getFlrartikelgruppe() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE,
								new Exception("Keine Artikelgruppe f\u00FCr Artikel definiert:"
										+ rePos.getFlrartikel().getC_nr()));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add("Rechnung: " + rePos.getFlrrechnung().getC_nr() + " \n" +

								"Artikel: " + rePos.getFlrartikel().getC_nr());
						ex.setAlInfoForTheClient(a);
						throw ex;
					}
					Integer artgruId = rePos.getFlrartikel().getFlrartikelgruppe().getI_id();
					ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artgruId, theClientDto);
					if (artgruDto.getKontoIId() == null) {
//					if (rePos.getFlrartikel().getFlrartikelgruppe()
//							.getFlrkonto() == null) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
								new Exception("Kein Konto f\u00FCr Artikelgruppe definiert: \n"
										+ rePos.getFlrartikel().getFlrartikelgruppe().getC_nr()));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(rePos.getFlrartikel().getC_nr());
						a.add(rePos.getFlrartikel().getFlrartikelgruppe().getC_nr());
						ex.setAlInfoForTheClient(a);
						throw ex;
					}
				}
			}
			if ((RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rePos.getPositionsart_c_nr()))
					|| (RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr()))) {
				if (rePos.getSetartikel_set().size() == 0) {
					// keine Kopfposition eines Artikelsets
					Integer kontoIId = null;
					if (visitable.isAnzahlungsRechnung()) {
						KontoDto debitorenkontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(visitable.getKundeDto().getIidDebitorenkonto());
						FinanzamtDto finanzamtDto = getFinanzamt(debitorenkontoDto.getFinanzamtIId());
						kontoIId = finanzamtDto.getKontoIIdAnzahlungErhaltVerr();
						if (kontoIId == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT,
									"Verrechnungskonto Erhaltene Anzahlungen nicht definiert f\u00FCr Anzahlungsrechnung "
											+ visitable.getRechnungDto().getCNr());
						}
					} else if (RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rePos.getPositionsart_c_nr())) {
						kontoIId = kontoDtoSonstige.getIId();
						if (hmArtikelGruppen.get(kontoIId) == null) {
							hmArtikelGruppen.put(kontoIId, kontoDtoSonstige.getCBez());
						}
					} else {
						Integer artgruId = rePos.getFlrartikel().getFlrartikelgruppe().getI_id();
						ArtgruDto artgruDto = getArtikelFac().artgruFindByPrimaryKey(artgruId, theClientDto);
						kontoIId = artgruDto.getKontoIId();
//						kontoIId = rePos.getFlrartikel()
//								.getFlrartikelgruppe()
//								.getFlrkonto().getI_id();
						// Zu jedem Konto Artikelgruppenbezeichnung
						// merken
						if (hmArtikelGruppen.get(kontoIId) == null) {
							hmArtikelGruppen.put(kontoIId, rePos.getFlrartikel().getFlrartikelgruppe().getC_nr());
						}
					}
					if (hmNettoFW.get(kontoIId) != null) {
						bdNetto = bdNetto.add(hmNettoFW.get(kontoIId));
					}
					hmNettoFW.put(kontoIId, bdNetto);
					if (hmBruttoFW.get(kontoIId) != null) {
						bdBrutto = bdBrutto.add(hmBruttoFW.get(kontoIId));
					}
					hmBruttoFW.put(kontoIId, bdBrutto);

					// MWST-Satz Merken
					MwstsatzDto mwst = getMandantFac().mwstsatzFindByPrimaryKey(rePos.getFlrmwstsatz().getI_id(),
							theClientDto);
// PJ20219 Kann nicht null werden, ghp 15.05.2018					
//					if (mwst == null) {
//						EJBExceptionLP ex = new EJBExceptionLP(
//								EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
//								new Exception(
//										"Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
//												+ visitable.getRechnungDto().getCNr()));
//						ArrayList<Object> a = new ArrayList<Object>();
//						a.add("Rechnung: " + visitable.getRechnungDto().getCNr());
//						ex.setAlInfoForTheClient(a);
//						throw ex;
//					}
					if (hmMwstsatz.get(kontoIId) == null) {
						// Neu einfuegen
						hmMwstsatz.put(kontoIId, mwst);
					} else {
						if (!hmMwstsatz.get(kontoIId).equals(mwst)) {
							EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
									new Exception("Kein einzigartiger FIBU-MWSTCode definiert bei Beleg:"
											+ visitable.getRechnungDto().getCNr()));
							ArrayList<Object> a = new ArrayList<Object>();
							a.add("Rechnung: " + visitable.getRechnungDto().getCNr());
							// a.add("Lieferschein: " +
							// rePos.getFlrlieferschein().getC_nr());
							ex.setAlInfoForTheClient(a);
							throw ex;
						}
					}
					
					myLogger.error("      ID:" + rePos.getI_id() + " kontoId:" + kontoIId +
							" netto:" + hmNettoFW.get(kontoIId) + 
							" mwst:" + hmMwstsatz.get(kontoIId));
				}
			}
		}

		private RechnungDto getReDto() {
			return theVisitable.getRechnungDto();
		}

		private KundeDto getKundeDto() {
			return theVisitable.getKundeDto();
		}

		private String getLaenderartCnr() {
			return theVisitable.getLaenderartCnr();
		}

		private Integer getFinanzamtId() {
			return theVisitable.getFinanzamtId();
		}

		private Integer getDebitorenKontoUebersteuertId() {
			return theVisitable.getDebitorenKontoUebersteuertId();
		}

		private Integer getLandId() {
			return theVisitable.getLandId();
		}

		private Integer getLieferlandId() {
			return theVisitable.getLieferlandId();
		}
		
		private ReversechargeartId getSteuerfallRcId() {
			return theVisitable.getSteuerfallRcId();
		}

		/**
		 * Liefert die zu exportierenden Daten</br>
		 * <p>
		 * Geht davon aus, dass dieser Visitor ueber den FinanceDataEvaluator aktiviert
		 * worden ist.
		 * 
		 * @return
		 */
		public FibuexportDto[] getExportDaten() {
			try {
				return getExportDatenThrow();
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			return null;
		}

		private void processRabatt() {
			// Netto- und Brutto-Summe bestimmen.
			BigDecimal bdSummeNetto = BigDecimal.ZERO;
			BigDecimal bdSummeBrutto = BigDecimal.ZERO;

			Integer iKontoIIdMaxBetrag = 0;
			for (Iterator<Integer> iter = hmNettoFW.keySet().iterator(); iter.hasNext();) {
				Integer kontoIId = (Integer) iter.next();
				BigDecimal bdBetragNetto = hmNettoFW.get(kontoIId);
				BigDecimal bdBetragBrutto = hmBruttoFW.get(kontoIId);
				bdSummeNetto = bdSummeNetto.add(bdBetragNetto);
				bdSummeBrutto = bdSummeBrutto.add(bdBetragBrutto);
				BigDecimal bdBetragMax = hmNettoFW.get(iKontoIIdMaxBetrag);
				if (bdBetragMax == null) {
					iKontoIIdMaxBetrag = kontoIId;
				} else {
					if (bdBetragMax.compareTo(bdBetragNetto) < 0) {
						iKontoIIdMaxBetrag = kontoIId;
					}
				}
			}

			BigDecimal bdWertNettoFW = getReDto().getNWertfw();
			BigDecimal bdWertBruttoFW = getReDto().getNWertfw().add(getReDto().getNWertustfw());

			if (bdSummeNetto.compareTo(bdWertNettoFW) != 0 || bdSummeBrutto.compareTo(bdWertBruttoFW) != 0) {
				// PJ 15015: Allgemeinen Rabatt buchen
				if (getReDto().getFAllgemeinerRabattsatz().doubleValue() != 0) {
					if (useAllgemeinerRabatt()) {
						BigDecimal bdHelper = hmNettoFW.get(kontoDtoAllgemeinerRabatt.getIId());
						if (bdHelper == null) {
							bdHelper = BigDecimal.ZERO;
						}
						bdHelper = bdHelper.add(bdWertNettoFW.subtract(bdSummeNetto));
						hmNettoFW.put(kontoDtoAllgemeinerRabatt.getIId(), bdHelper);

						bdHelper = hmBruttoFW.get(kontoDtoAllgemeinerRabatt.getIId());
						if (bdHelper == null) {
							bdHelper = BigDecimal.ZERO;
						}
						bdHelper = bdHelper.add(bdWertBruttoFW.subtract(bdSummeBrutto));
						hmBruttoFW.put(kontoDtoAllgemeinerRabatt.getIId(), bdHelper);
					} else {
						BigDecimal bdHelper = hmNettoFW.get(iKontoIIdMaxBetrag);
						bdHelper = bdHelper.add(bdWertNettoFW.subtract(bdSummeNetto));
						hmNettoFW.put(iKontoIIdMaxBetrag, bdHelper);
						bdHelper = hmBruttoFW.get(iKontoIIdMaxBetrag);
						bdHelper = bdHelper.add(bdWertBruttoFW.subtract(bdSummeBrutto));
						hmBruttoFW.put(iKontoIIdMaxBetrag, bdHelper);
					}
				} else {
					// SP5940 Differenz, die nicht durch Rabatt entstand
					BigDecimal diffNetto = bdWertNettoFW.abs().subtract(bdSummeNetto.abs()).abs();
					if (diffNetto.compareTo(fibuDifferenzErlaubt) > 0) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_NETTODIFFERENZ_ZUHOCH,
								new Exception("Korrekturwert zu hoch"));
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(getReDto().getCNr());
						a.add(Helper.formatZahl(diffNetto, FinanzFac.NACHKOMMASTELLEN, theClientDto.getLocUi()));
						ex.setAlInfoForTheClient(a);
						throw ex;
					}
				}
			}

//			if (bAllgemeinerRabattExtraBuchen
//					&& getReDto().getFAllgemeinerRabattsatz().doubleValue() != 0) {
//				// PJ 15015: Allgemeinen Rabatt buchen
//				if (bdSummeNetto.compareTo(bdWertNettoFW) != 0
//						|| bdSummeBrutto.compareTo(bdWertBruttoFW) != 0) {
//					BigDecimal bdHelper = hmNettoFW
//							.get(kontoDtoAllgemeinerRabatt.getIId());
//					if (bdHelper == null)
//						bdHelper = new BigDecimal(0);
//					bdHelper = bdHelper.add(bdWertNettoFW
//							.subtract(bdSummeNetto));
//					hmNettoFW.put(kontoDtoAllgemeinerRabatt.getIId(),
//							bdHelper);
//					bdHelper = hmBruttoFW.get(kontoDtoAllgemeinerRabatt
//							.getIId());
//					if (bdHelper == null)
//						bdHelper = new BigDecimal(0);
//					bdHelper = bdHelper.add(bdWertBruttoFW
//							.subtract(bdSummeBrutto));
//					hmBruttoFW.put(kontoDtoAllgemeinerRabatt.getIId(),
//							bdHelper);
//				}
//			} else {
//				if (bdSummeNetto.compareTo(bdWertNettoFW) != 0
//						|| bdSummeBrutto.compareTo(bdWertBruttoFW) != 0) {
//					BigDecimal bdHelper = hmNettoFW.get(iKontoIIdMaxBetrag);
//					bdHelper = bdHelper.add(bdWertNettoFW
//							.subtract(bdSummeNetto));
//					hmNettoFW.put(iKontoIIdMaxBetrag, bdHelper);
//					bdHelper = hmBruttoFW.get(iKontoIIdMaxBetrag);
//					bdHelper = bdHelper.add(bdWertBruttoFW
//							.subtract(bdSummeBrutto));
//					hmBruttoFW.put(iKontoIIdMaxBetrag, bdHelper);
//				}
//			}			
		}

		private FibuexportDto[] getExportDatenThrow() throws RemoteException {
			processRabatt();
			steuercodeCache = new MwstsatzCodeCache();
			
			// --------------------------------------------------------------
			// Daten zusammenstellen.
			// --------------------------------------------------------------
			final int iAnzahlGegenkonten = hmNettoFW.size();
			final int iZeilen = 1 + iAnzahlGegenkonten; // buchung auf
			// debitorenkonto

			// Holen des Kundenkontos
			KontoDto debitorenkontoDto = getFinanzFac().kontoFindByPrimaryKey(getKundeDto().getIidDebitorenkonto());

			boolean isArReversecharge = isRechnungDtoReversecharge(getReDto());
			// jetzt wird das Daten-Array zusammengebaut
			FibuexportDto[] exportDtos = new FibuexportDto[iZeilen];
			for (int i = 0; i < exportDtos.length; i++) {
				exportDtos[i] = new FibuexportDto();
				// exportDtos[i].setMwstsatz(mwstSatz);
				exportDtos[i].setIGeschaeftsjahr(getReDto().getIGeschaeftsjahr());
				exportDtos[i].setReversechargeartId(getReDto().getReversechargeartId());
				exportDtos[i].setBReverseCharge(isArReversecharge);
			}
			// Die Buchungszeile fuers Debitorenkonto
			exportDtos[0].setKonto(debitorenkontoDto);
			// Gegenkonto / Kostenstelle / MWST-Satz
			if (iAnzahlGegenkonten > 1) {
				// wenn es mehrere Gegenkonten gibt
				exportDtos[0].setGkto(null);
			} else {
				/**
				 * @todo MR->MR: Achtung in einem Fall ist iAnzahlGegenkonten = 0!!
				 */
				if (hmNettoFW.keySet().iterator().hasNext()) {
					Integer kontoIId = hmNettoFW.keySet().iterator().next(); // es
					// ist
					// das
					// einzige
					// nur ein Gegenkonto, aber 2 Moeglichkeiten:
					// 1. Das Gegenkonto steht in den Kopfdaten
					KontoDto kontoDtoGegenkontoUebersetzt = null;
					if (getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG,
							theClientDto)) {

					} else {
						kontoDtoGegenkontoUebersetzt = uebersetzeKontoNachLandBzwLaenderart(kontoIId,
								getLaenderartCnr(), debitorenkontoDto.getFinanzamtIId(),
								debitorenkontoDto.getMandantCNr(), getReDto(), getLieferlandId(),
								getReDto().getReversechargeartId(), getReDto().getTBelegdatum());
					}
					exportDtos[0].setGkto(kontoDtoGegenkontoUebersetzt);
					// MWST-Betrag
					// Wg. Drittlandsgutschrift auf 0 statt NULL geaendert
					exportDtos[0].setSteuer(new BigDecimal(0));
					setupExportSteuercode(exportDtos[0], kontoIId);
					
				}
			}
			// die OP-Nummer ist die Belegnummer, aber ohne GJ und
			// Trennzeichen
			exportDtos[0].setOPNummer(extractLaufendeNummerAusBelegnummer(getReDto().getCNr(), theClientDto));
			exportDtos[0].setBelegnr(getReDto().getCNr());
			exportDtos[0].setBelegdatum(getReDto().getTBelegdatum());
			exportDtos[0].setValutadatum(getReDto().getTBelegdatum());
			if (uebersteuerteMandantenwaherung != null) {
				exportDtos[0].setWaehrung(uebersteuerteMandantenwaherung.getCNr());
			} else {
				exportDtos[0].setWaehrung(theClientDto.getSMandantenwaehrung());
			}
			// Haben ist 0
			exportDtos[0].setHabenbetrag(null);
			exportDtos[0].setHabenbetragFW(null);
			// Bruttobetrag im Soll
			if (uebersteuerteMandantenwaherung != null) {
				BigDecimal bdSollBetrMandantenwaehrung = getReDto().getNWert().add(getReDto().getNWertust());
				/*
				 * WechselkursDto bdKurs = getLocaleFac().getKursZuDatum(
				 * uebersteuerteMandantenwaherung.getCNr(),
				 * theClientDto.getSMandantenwaehrung(), new Date(10), theClientDto);
				 * if(bdKurs==null){ EJBExceptionLP ex = new EJBExceptionLP(
				 * EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEINKURS_ZUDATUM, new
				 * Exception("Kein Kurs f\u00FCr Stichtag eingetragen")); ArrayList<Object> a =
				 * new ArrayList<Object>(); a.add("Von: "
				 * +uebersteuerteMandantenwaherung.getCNr()); a.add("Nach: " +
				 * theClientDto.getSMandantenwaehrung()); a.add("Datum: "+ new Date(10));
				 * a.add("Rechnung: " + reDto.getCNr()); ex.setAlInfoForTheClient(a); throw ex;
				 * }
				 */
				// Wenn Rechnungswaehrung gleich uebersteuerte Waehrung dann
				// ist sollbetrag gleich Rechnungsbetrag
				// in Fremdwaehrung
				if (uebersteuerteMandantenwaherung.getCNr().equals(getReDto().getWaehrungCNr())) {
					exportDtos[0].setSollbetrag(getReDto().getNWertfw().add(getReDto().getNWertustfw()));
				} else {
					exportDtos[0].setSollbetrag(bdSollBetrMandantenwaehrung.divide(
							getLocaleFac().getWechselkurs2(uebersteuerteMandantenwaherung.getCNr(),
									theClientDto.getSMandantenwaehrung(), theClientDto),
							FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
				}

			} else {
				exportDtos[0].setSollbetrag(getReDto().getNWert().add(getReDto().getNWertust()));
			}
			exportDtos[0].setSollbetragFW(getReDto().getNWertfw().add(getReDto().getNWertustfw()));
			// MWST-Betrag
			exportDtos[0].setFremdwaehrung(getReDto().getWaehrungCNr());
			exportDtos[0].setBelegart(BELEGART_AR);
			exportDtos[0].setPartnerDto(getKundeDto().getPartnerDto());
			exportDtos[0].setLaenderartCNr(getLaenderartCnr());
			if (uebersteuerteMandantenwaherung != null) {
				if (uebersteuerteMandantenwaherung.getCNr().equals(getReDto().getWaehrungCNr())) {
					// Wenn Beleg gleich uebersteuerter Mandant explizit auf
					// 1 gegen Rundungsfehler
					exportDtos[0].setKurs(new BigDecimal(1));
				} else {
					exportDtos[0].setKurs(getLocaleFac().getWechselkurs2(uebersteuerteMandantenwaherung.getCNr(),
							getReDto().getWaehrungCNr(), theClientDto));
				}
			} else {
				exportDtos[0].setKurs(getReDto().getNKurs());
			}

			ZahlungszielDto zzDto = getZahlungsziel(getReDto().getZahlungszielIId());
			exportDtos[0].setZahlungszielDto(zzDto);
			exportDtos[0].setText(getKundeDto().getPartnerDto().formatFixTitelName1Name2());
			exportDtos[0].setUidNummer(getKundeDto().getPartnerDto().getCUid());
			Date tFaelligkeit = Helper.addiereTageZuDatum(getReDto().getTBelegdatum(),
					zzDto.getAnzahlZieltageFuerNetto());
			exportDtos[0].setFaelligkeitsdatum(tFaelligkeit);
			exportDtos[0].setBestellnummer(getReDto().getCBestellnummer());
			setupExportFromAuftrag(exportDtos[0]);
			setupExportFromRechnungsart(exportDtos[0]);
			
			// Gegenbuchungen
			// Cent-Differenzen muessen vermieden werden
			BigDecimal bdRestNetto = exportDtos[0].getSollbetragBD();
			/**
			 * @todo MR->MR: Achtung Array IndexOutOfBounds wenn von i = 1 gestartet wird.
			 */
			int iIndexDesGrNettowerts = 0;
			int i = 0;
			for (Iterator<Integer> iter = hmNettoFW.keySet().iterator(); iter.hasNext(); i++) {
				// Pruefen der Kostenstelle
				Integer kontoIId = (Integer) iter.next();
				// PJ 17120
				Integer finanzamtIId = getFinanzamtId();
				if (finanzamtIId == null) {
					// nicht durch Lieferschein uebersteuert
					finanzamtIId = debitorenkontoDto.getFinanzamtIId();
				}
				
				KontoDto kontoDtoGegenkonto = uebersetzeKontoNachLandBzwLaenderart(kontoIId, getLaenderartCnr(),
						finanzamtIId, debitorenkontoDto.getMandantCNr(), getReDto(), getLieferlandId(),
						getReDto().getReversechargeartId(), getReDto().getTBelegdatum());
				// buchung aufs HW-Konto
				exportDtos[1 + i].setDebitorenKontoIIdUebersteuert(getDebitorenKontoUebersteuertId());
				exportDtos[1 + i].setKonto(kontoDtoGegenkonto);
				exportDtos[1 + i].setGkto(debitorenkontoDto);
				exportDtos[1 + i].setOPNummer(extractLaufendeNummerAusBelegnummer(getReDto().getCNr(), theClientDto));
				exportDtos[1 + i].setBelegnr(getReDto().getCNr());
				exportDtos[1 + i].setBelegdatum(getReDto().getTBelegdatum());
				exportDtos[1 + i].setValutadatum(getReDto().getTBelegdatum());
				if (uebersteuerteMandantenwaherung != null) {
					exportDtos[1 + i].setWaehrung(uebersteuerteMandantenwaherung.getCNr());
				} else {
					exportDtos[1 + i].setWaehrung(theClientDto.getSMandantenwaehrung());
				}

				// Habenbetrag ist netto
				BigDecimal bdNettobetragFW = hmNettoFW.get(kontoIId);
				BigDecimal bdNettobetrag = null;
				if (uebersteuerteMandantenwaherung != null) {
					if (uebersteuerteMandantenwaherung.getCNr().equals(getReDto().getWaehrungCNr())) {
						// Kurs ist 1 also FW = Rechnungswaehrung
						bdNettobetrag = bdNettobetragFW;
					} else {
						bdNettobetrag = bdNettobetragFW.divide(
								getLocaleFac().getWechselkurs2(uebersteuerteMandantenwaherung.getCNr(),
										getReDto().getWaehrungCNr(), theClientDto),
								FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
					}
				} else {
					bdNettobetrag = bdNettobetragFW.divide(getReDto().getNKurs(), FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
				}

				bdNettobetrag = bdNettobetrag.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
				bdNettobetragFW = bdNettobetragFW.setScale(FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
				exportDtos[1 + i].setHabenbetrag(bdNettobetrag);
				exportDtos[1 + i].setHabenbetragFW(bdNettobetragFW);
				// Merken an welcher Position der groesste Betrag ist
				if (iIndexDesGrNettowerts == 0) {
					iIndexDesGrNettowerts = 1 + i;
				} else {
					if (bdNettobetrag.compareTo(exportDtos[iIndexDesGrNettowerts].getHabenbetragBD()) > 0) {
						iIndexDesGrNettowerts = 1 + i;
					}
				}

				exportDtos[1 + i].setSollbetrag(null);
				exportDtos[1 + i].setSollbetragFW(null);
				
				// Steuerbetrag ist der UST-Betrag
				BigDecimal bdUstFW = hmBruttoFW.get(kontoIId).subtract(hmNettoFW.get(kontoIId));
				bdUstFW = getCoinRoundingService().roundUst(getReDto(), bdUstFW, theClientDto);
				exportDtos[1 + i].setSteuerFW(bdUstFW);
				
				BigDecimal bdBrutto = null;
				if (uebersteuerteMandantenwaherung != null) {
					bdBrutto = hmBruttoFW.get(kontoIId)
							.divide(getLocaleFac().getWechselkurs2(uebersteuerteMandantenwaherung.getCNr(),
									getReDto().getWaehrungCNr(), theClientDto), FinanzFac.NACHKOMMASTELLEN,
									BigDecimal.ROUND_HALF_EVEN);
				} else {
					bdBrutto = hmBruttoFW.get(kontoIId).divide(getReDto().getNKurs(), FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
				}
				BigDecimal bdUST = bdBrutto.subtract(bdNettobetrag);
				bdUST = getCoinRoundingService().roundUst(getReDto(), bdUST, theClientDto);
				exportDtos[1 + i].setSteuer(bdUST);
				exportDtos[1 + i].setMwstsatz(hmMwstsatz.get(kontoIId));

				// Fremdwaehrung
				exportDtos[1 + i].setFremdwaehrung(getReDto().getWaehrungCNr());
				KostenstelleDto kstDto = null;
				if (kontoDtoGegenkonto.getKostenstelleIId() != null) {
					kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kontoDtoGegenkonto.getKostenstelleIId());
				}
				exportDtos[1 + i].setKost(kstDto);
				exportDtos[1 + i].setBelegart(BELEGART_AR);
				exportDtos[1 + i].setPartnerDto(getKundeDto().getPartnerDto());
				exportDtos[1 + i].setLaenderartCNr(getLaenderartCnr());
				exportDtos[1 + i].setZahlungszielDto(zzDto);
				exportDtos[1 + i].setFaelligkeitsdatum(tFaelligkeit);
				exportDtos[1 + i].setText(hmArtikelGruppen.get(kontoIId));
				exportDtos[1 + i].setUidNummer(getKundeDto().getPartnerDto().getCUid());
				
				if (uebersteuerteMandantenwaherung != null) {
					exportDtos[1 + i].setKurs(getLocaleFac().getWechselkurs2(uebersteuerteMandantenwaherung.getCNr(),
							getReDto().getWaehrungCNr(), theClientDto));
				} else {
					exportDtos[1 + i].setKurs(getReDto().getNKurs());
				}

				// SP8877 Bei Anzahlung ist der Betrag brutto zu sehen.
				// Heisst: Es gibt keinen Steuerbetrag. Es kann nur eine
				// steuerbehaftete Rechnungsposition geben						
				if (theVisitable.isAnzahlungsRechnung() && i == 0) {
					exportDtos[1 + i].setHabenbetrag(
							exportDtos[1+i].getHabenbetragBD()
								.add(exportDtos[1+i].getSteuerBD()));
					exportDtos[1 + i].setHabenbetragFW(
							exportDtos[1+i].getHabenbetragFWBD()
								.add(exportDtos[1+i].getSteuerFWBD()));
					exportDtos[1 + i].setSteuer(BigDecimal.ZERO);
					exportDtos[1 + i].setSteuerFW(BigDecimal.ZERO);
				}
				setupExportSteuercode(exportDtos[1 + i], kontoIId);
				exportDtos[1 + i].setBestellnummer(getReDto().getCBestellnummer());
				setupExportFromAuftrag(exportDtos[1 + i]);
				setupExportFromRechnungsart(exportDtos[1 + i]);
				pruefeUst(exportDtos[i + 1], kontoIId, kontoDtoGegenkonto);
				bdRestNetto = bdRestNetto.subtract(exportDtos[1 + i].getPositionsbetragLeitwaehrungBD());
			}

			// jetzt eventuelle Cent-Differenzen bereinigen
			if (bdRestNetto.compareTo(new BigDecimal(10)) > 0) {
				// Wenn Korrekturwert >= 10 dann Fehler
				EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH,
						new Exception("Korrekturwert zu hoch"));
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(getReDto().getCNr());
				a.add(Helper.formatZahl(bdRestNetto, FinanzFac.NACHKOMMASTELLEN, theClientDto.getLocUi()));
				ex.setAlInfoForTheClient(a);
				throw ex;
			} else {
				exportDtos[iIndexDesGrNettowerts]
						.setHabenbetrag(exportDtos[iIndexDesGrNettowerts].getHabenbetragBD().add(bdRestNetto));
			}

			return exportDtos;
		}

		private void setupExportSteuercode(FibuexportDto exportDto, Integer kontoIId) {
			if (kontoIId == null || hmMwstsatz.get(kontoIId) == null || getSteuerfallRcId() == null) {
				myLogger.error("FibuExportManager:setupExportSteuercode(): RE" + getReDto().getCNr());
				return;
			}
			exportDto.setSteucod(steuercodeCache.getCodeAr(
					new SteuerfallKey(new MwstsatzId(hmMwstsatz.get(kontoIId).getIId()), getSteuerfallRcId())));
		}

		private void setupExportFromRechnungsart(FibuexportDto exportDto) {
			exportDto.setAnzahlungsbeleg(theVisitable.isAnzahlungsRechnung());
			exportDto.setSchlussrechnungsbeleg(theVisitable.isSchlussrechnung());
		}
		
		private void setupExportFromAuftrag(FibuexportDto exportDto) {
			if (getReDto().getAuftragIId() == null)
				return;
			
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(getReDto().getAuftragIId());
			exportDto.setAuftnr(auftragDto.getCNr());
		}

		private void pruefeUst(FibuexportDto exportDto, Integer kontoId, KontoDto gegenkontoDto)
				throws RemoteException {
			MwstsatzDto satzDto = hmMwstsatz.get(kontoId);
			UvaartDto uvaartDto = getFinanzServiceFac().uvaartFindByPrimaryKey(gegenkontoDto.getUvaartIId(),
					theClientDto);
			BigDecimal steuer = exportDto.getSteuerBD();
			BigDecimal netto = exportDto.getHabenbetragBD();
			UstWarnungDto warnungDto = exportDto.getUstWarnungDto();
			if (uvaartDto.isSteuerfrei()) {
				if (steuer != null && steuer.signum() != 0) {
					warnungDto.beSteuerbetragObwohlNichtErwartet();
					warnungDto.setKontoId(kontoId);
					warnungDto.setKontoUebersetztId(gegenkontoDto.getIId());
				}
			} else if (uvaartDto.isNormalsteuer()) {
				if (netto.signum() != 0 && steuer != null && steuer.signum() == 0) {
					warnungDto.beKeineSteuerObwohlErwartet();
					warnungDto.setKontoId(kontoId);
					warnungDto.setKontoUebersetztId(gegenkontoDto.getIId());

					if (satzDto.getMwstsatzbezDto() == null) {
						satzDto.setMwstsatzbezDto(getMandantFac()
								.mwstsatzbezFindByPrimaryKey(satzDto.getIIMwstsatzbezId(), theClientDto));
					}
					if (!FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTNORMAL
							.equals(satzDto.getMwstsatzbezDto().getCDruckname())) {
						warnungDto.beSteuersatzFalsch();
					}
				}
			} else if (uvaartDto.isReduziertsteuer()) {
				if (netto.signum() != 0 && steuer != null && steuer.signum() == 0) {
					warnungDto.beKeineSteuerObwohlErwartet();
					warnungDto.setKontoId(kontoId);
					warnungDto.setKontoUebersetztId(gegenkontoDto.getIId());
					if (satzDto.getMwstsatzbezDto() == null) {
						satzDto.setMwstsatzbezDto(getMandantFac()
								.mwstsatzbezFindByPrimaryKey(satzDto.getIIMwstsatzbezId(), theClientDto));
					}
					if (!FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTRED
							.equals(satzDto.getMwstsatzbezDto().getCDruckname())) {
						warnungDto.beSteuersatzFalsch();
					}
				}
			}
		}
	}

	public class FibuExportRechnungVisitorDummy implements RechnungVisitor {
		@Override
		public void visit(RechnungExportVisitable visitable) {
		}

		@Override
		public void visitLieferscheinPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos) {
		}

		@Override
		public void visitOtherPosition(RechnungExportVisitable visitable, FLRRechnungPosition rePos) {
		}
	}

	public FibuexportDto[] getExportdatenRechnung(Integer rechnungIId, Date dStichtag) throws EJBExceptionLP {
		try {
			ParametermandantDto parametermandantautoDebitDto = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_AUTOMATISCHE_DEBITORENNUMMER);
			boolean legeDebitorAn = (Boolean) parametermandantautoDebitDto.getCWertAsObject();
			FinanceDataEvaluator fde = new FinanceDataEvaluator(legeDebitorAn);
			FibuExportRechnungVisitor exporter = new FibuExportRechnungVisitor();
			fde.accept(exporter);

			RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
			fde.evaluate(reDto);
			return exporter.getExportDaten();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return null;
	}

	private boolean isSameCountry(KundeDto rechnungsKundeDto, KundeDto lieferscheinKundeDto) {
		LandplzortDto reLandDto = rechnungsKundeDto.getPartnerDto().getLandplzortDto();
		LandplzortDto lsLandDto = lieferscheinKundeDto.getPartnerDto().getLandplzortDto();

		verifyLandIstDefiniert(LocaleFac.BELEGART_RECHNUNG, reLandDto);
		verifyLandIstDefiniert(LocaleFac.BELEGART_LIEFERSCHEIN, lsLandDto);

		if (reLandDto.getIlandID().equals(lsLandDto.getIlandID())) {
			return true;
		}

		if (reLandDto.getIlandID().equals(lsLandDto.getLandDto().getLandIIdGemeinsamespostland())) {
			return true;
		}

		return false;
	}

	private void verifyLandIstDefiniert(String belegartCnr, LandplzortDto landplzOrtDto) {
		if (landplzOrtDto == null || landplzOrtDto.getIlandID() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEIN_LAND_IM_KUNDEN, belegartCnr);
		}
	}

	private EJBExceptionLP getExceptionFinanzamIIdUnterschiedlich(KontoDto rechnungsDebitorenKonto,
			KontoDto lieferscheinDebitorenKonto) {
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAEMTER_UNTERSCHIEDLICH,
				"Debitorenkonto Rechnungsadresse: " + rechnungsDebitorenKonto.getCNr() + " (FA-Id "
						+ rechnungsDebitorenKonto.getFinanzamtIId() + ")" + ", Debitorenkonto Lieferadresse: "
						+ lieferscheinDebitorenKonto.getCNr() + " (FA-Id "
						+ lieferscheinDebitorenKonto.getFinanzamtIId() + ")",
				rechnungsDebitorenKonto.getCNr(), rechnungsDebitorenKonto.getFinanzamtIId(),
				lieferscheinDebitorenKonto.getCNr(), lieferscheinDebitorenKonto.getFinanzamtIId());
	}

	public FibuexportDto[] getExportdatenGutschrift(Integer rechnungIId, Date dStichtag) throws EJBExceptionLP {
		// wie die Ausgangsrechnung, aber mit kleinen Aenderungen:
		// 1. Reihenfolge der Buchungszeilen aendern
		// 2. Belegkreis wird GS
		// 3. Betraege negieren
		FibuexportDto[] exportAR = getExportdatenRechnung(rechnungIId, dStichtag);
		if (exportAR == null || exportAR.length == 0)
			return null;
		FibuexportDto[] exportGS = new FibuexportDto[exportAR.length];
		for (int i = 0; i < exportAR.length; i++) {
			// Reihenfolge
			exportGS[i] = exportAR[exportAR.length - 1 - i];
			// Belegkreis
			exportGS[i].setBelegart(BELEGART_GS);
			// Werte negieren
			if (exportGS[i].getSollbetragBD() != null) {
				exportGS[i].setSollbetrag(exportGS[i].getSollbetragBD().negate());
			}
			if (exportGS[i].getSollbetragFWBD() != null) {
				exportGS[i].setSollbetragFW(exportGS[i].getSollbetragFWBD().negate());
			}
			if (exportGS[i].getHabenbetragBD() != null) {
				exportGS[i].setHabenbetrag(exportGS[i].getHabenbetragBD().negate());
			}
			if (exportGS[i].getHabenbetragFWBD() != null) {
				exportGS[i].setHabenbetragFW(exportGS[i].getHabenbetragFWBD().negate());
			}
			if (exportGS[i].getSteuerBD() != null) {
				exportGS[i].setSteuer(exportGS[i].getSteuerBD().negate());
			}
		}
		return exportGS;
	}

	@Override
	public ReportErloeskontoDto getErloeskonto(Integer rechnungId, BelegpositionVerkaufDto positionDto)
			throws EJBExceptionLP {
		throw new IllegalArgumentException("should not be called!");
	}

	@Override
	public KontoDto getUebersetzeKontoNachLandBzwLaenderart(Integer kontoIId, Integer rechnungId) {
		try {
			if (LocaleFac.BELEGART_RECHNUNG.equals(exportKriterienDto.getSBelegartCNr())) {
				return uebersetzeARKontoNachLandBzwLaenderart(kontoIId, rechnungId);
			}
			if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(exportKriterienDto.getSBelegartCNr())) {
				return uebersetzeERKontoNachLandBzwLaenderart(kontoIId, rechnungId);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_RECHNUNGTYP,
				exportKriterienDto.getSBelegartCNr());
	}

	protected KontoDto uebersetzeARKontoNachLandBzwLaenderart(Integer kontoId, Integer rechnungId)
			throws RemoteException {
		RechnungDto reDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungId);
		FinanceDataEvaluator fde = new FinanceDataEvaluator(false);
		fde.evaluate(reDto);

		return uebersetzeKontoNachLandBzwLaenderart(kontoId,
				fde.getLaenderartCnr(), fde.getFinanzamtId(),
				reDto.getMandantCNr(), reDto, fde.getLieferlandId(),
				reDto.getReversechargeartId(), reDto.getTBelegdatum());
	}

	protected KontoDto uebersetzeERKontoNachLandBzwLaenderart(Integer kontoId, Integer rechnungId)
			throws RemoteException {
		EingangsrechnungDto erDto = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(rechnungId);

		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(erDto.getLieferantIId(), theClientDto);
		boolean hasFibu = getMandantFac().hatModulFinanzbuchhaltung(theClientDto);
		Timestamp belegDatum = Helper.asTimestamp(erDto.getDBelegdatum());
		String laenderartCnr = null;
		if (hasFibu) {
			laenderartCnr = getLaenderartZuKonto(lieferantDto.getPartnerDto(),
					lieferantDto.getKontoIIdKreditorenkonto(), belegDatum);
		} else {
			laenderartCnr = getLaenderartZuPartner(
					lieferantDto.getPartnerDto(), belegDatum);
		}

		Integer landId = lieferantDto.getPartnerDto().getLandplzortDto().getIlandID();

		KontoDto kreditorKontoDto = getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto());
		return uebersetzeKontoNachLandBzwLaenderart(kontoId, laenderartCnr,
				kreditorKontoDto.getFinanzamtIId(), erDto.getMandantCNr(),
				erDto, landId, erDto.getReversechargeartId(), belegDatum);
	}
}
