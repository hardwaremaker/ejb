package com.lp.server.system.ejbfac;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.artikel.service.WebPartSearchResult;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.OpenTransOrder;
import com.lp.server.bestellung.service.OpenTransOrderPosition;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SaldoInfoDto;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.service.HttpProxyConfig;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.util.BaseIntegerKey;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.service.HttpStatusCodeException;
import com.lp.service.plscript.ParameterNotFoundException;
import com.lp.service.plscript.ScriptFormula;
import com.lp.util.BuchungSteuersaetzeInKontenUnterschiedlichException;
import com.lp.util.BuchungenMitAuszugsnummerVorhandenException;
import com.lp.util.DebitorKontoException;
import com.lp.util.EJBExceptionLP;
import com.lp.util.FontNichtGefundenException;
import com.lp.util.FremdsystemnrNichtNummerischException;
import com.lp.util.GTINBasisnummerLaengeUngueltigException;
import com.lp.util.GTINGenerierungAlleArtikelnummernVergebenException;
import com.lp.util.Helper;
import com.lp.util.KreditorKontoException;
import com.lp.util.LinienabrufProduktionAbliefermengeKleinerEinsExc;
import com.lp.util.LinienabrufProduktionKeinLosGefundenException;
import com.lp.util.LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc;
import com.lp.util.LinienabrufProduktionStklNichtImMandantExc;
import com.lp.util.LsAlsRePosWaehrungenUnterschiedlichException;
import com.lp.util.SepaVerbuchungAuszugFalscherStatusException;
import com.lp.util.SepaZahlungenMitAuszugsnummerVorhandenException;
import com.lp.util.SepakontoauszugNichtImAktuellenGJException;
import com.lp.util.SteuerkategorieBasiskontoException;
import com.lp.util.SteuerkategorieDefinitionFehltException;
import com.lp.util.SteuerkategorieFehltException;
import com.lp.util.SteuerkategorieKontoException;
import com.lp.util.SteuerkategoriekontoDefinitionFehltException;
import com.lp.util.UnerwarteterStatusCodeBeiHttpLosExc;
import com.lp.util.VerbindungsfehlerBeiHttpPostLosExc;
import com.lp.util.WebabfrageArtikellieferantException;
import com.lp.util.report.UstVerprobungRow;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.sf.jasperreports.engine.util.JRFontNotFoundException;

public class EJBExcFactory {

	public static EJBExceptionLP respectOld(RemoteException e) {
		Throwable t = e.detail;
		if (t instanceof EJBExceptionLP) {
			return new EJBExceptionLP((EJBExceptionLP) t);
		} else {
			return new EJBExceptionLP(e);
		}		
	}

	public static EJBExceptionLP steuerkategorieDefinitionFehlt(KontoDto kreditorDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINITION_VORHANDEN,
				"Steuerkategoriedefinition fehlt", 
				new Object[]{"", kreditorDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieDefinitionFehltException(kreditorDto));
		return ex ;
	}
	
	public static EJBExceptionLP steuerkategorieDefinitionFehlt(EingangsrechnungDto erDto, KontoDto kreditorDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINITION_VORHANDEN,
				"Steuerkategoriedefinition fehlt", 
				new Object[]{erDto.getEingangsrechnungartCNr().trim() + " " + erDto.getCNr(), kreditorDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieDefinitionFehltException(erDto, kreditorDto));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieDefinitionFehlt(RechnungDto rechnungDto, KontoDto debitorDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINITION_VORHANDEN,
				"Steuerkategoriedefinition fehlt", 
				new Object[]{rechnungDto.getBelegartCNr().trim() + " " + rechnungDto.getCNr(), debitorDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieDefinitionFehltException(rechnungDto, debitorDto));
		return ex ;
	}
	
	public static EJBExceptionLP steuerkategorieFehlt(Integer finanzamtId, 
			Integer reversechargeartId, String steuerkategorieCnr ) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT, "Steuerkategorie fehlt",
				new Object[]{"", steuerkategorieCnr});
		ex.setExceptionData(new SteuerkategorieFehltException(finanzamtId, reversechargeartId, steuerkategorieCnr));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieFehlt(EingangsrechnungDto erDto, 
			Integer finanzamtId, Integer reversechargeartId, String steuerkategorieCnr ) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT, "Steuerkategorie fehlt",
				new Object[]{erDto.getEingangsrechnungartCNr().trim() + " " + erDto.getCNr(), steuerkategorieCnr});
		ex.setExceptionData(new SteuerkategorieFehltException(erDto, finanzamtId, reversechargeartId, steuerkategorieCnr));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieFehlt(RechnungDto rechnungDto, 
			Integer finanzamtId, Integer reversechargeartId, String steuerkategorieCnr ) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT, "Steuerkategorie fehlt",
				new Object[]{rechnungDto.getBelegartCNr().trim() + " " + rechnungDto.getCNr(), steuerkategorieCnr});
		ex.setExceptionData(new SteuerkategorieFehltException(rechnungDto, finanzamtId, reversechargeartId, steuerkategorieCnr));
		return ex ;
	}
	
	public static EJBExceptionLP steuerkategorieBasisKontoFehlt(int ejbFehlernummer, SteuerkategorieDto stkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategorie fehlt", 
				new Object[]{"", stkDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieBasiskontoException(stkDto));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieBasisKontoFehlt(int ejbFehlernummer, 
			EingangsrechnungDto erDto, SteuerkategorieDto stkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategorie fehlt", 
				new Object[]{erDto.getEingangsrechnungartCNr().trim() + " " + erDto.getCNr(), stkDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieBasiskontoException(erDto, stkDto));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieBasisKontoFehlt(int ejbFehlernummer,
			RechnungDto rechnungDto, SteuerkategorieDto stkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategorie fehlt",
				new Object[]{
						(rechnungDto.getBelegartCNr() == null 
								? "AR":rechnungDto.getBelegartCNr())
							.trim() + " " + rechnungDto.getCNr(), stkDto.getCNr()});
		ex.setExceptionData(new SteuerkategorieBasiskontoException(rechnungDto, stkDto)) ;
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieKontoDefinitionFehlt(
			SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIEKONTO_DEFINITION_VORHANDEN, 
				"Steuerkategoriekontodefinition fehlt", 
				new Object[]{"", stkDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategoriekontoDefinitionFehltException(stkDto, mwstsatzbezId));
		return ex ;
	}
	
	public static EJBExceptionLP steuerkategorieKontoDefinitionFehlt(
			EingangsrechnungDto erDto, SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIEKONTO_DEFINITION_VORHANDEN, 
				"Steuerkategoriekontodefinition fehlt", 
				new Object[]{erDto.getEingangsrechnungartCNr().trim() + " " + erDto.getCNr(), stkDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategoriekontoDefinitionFehltException(erDto, stkDto, mwstsatzbezId));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieKontoDefinitionFehlt(
			RechnungDto rechnungDto, SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIEKONTO_DEFINITION_VORHANDEN, 
				"Steuerkategoriekontodefinition fehlt", 
				new Object[]{rechnungDto.getBelegartCNr().trim() + " " + rechnungDto.getCNr(), stkDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategoriekontoDefinitionFehltException(rechnungDto, stkDto, mwstsatzbezId));
		return ex ;
	}
	
	public static EJBExceptionLP steuerkategorieKontoFehlt(int ejbFehlernummer, 
			Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategoriekonto fehlt", 
				new Object[]{""}) ;
		ex.setExceptionData(new SteuerkategorieKontoException(reversechargeartId, stkkDto));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieKontoFehlt(int ejbFehlernummer, 
			EingangsrechnungDto erDto, Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategoriekonto fehlt", 
				new Object[]{erDto.getEingangsrechnungartCNr().trim() + " " + erDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieKontoException(erDto, reversechargeartId, stkkDto));
		return ex ;
	}

	public static EJBExceptionLP steuerkategorieKontoFehlt(int ejbFehlernummer, 
			RechnungDto rechnungDto, Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		EJBExceptionLP ex = new EJBExceptionLP(ejbFehlernummer, 
				"Kontodefinition in Steuerkategoriekonto fehlt", 
				new Object[]{rechnungDto.getBelegartCNr().trim() + " " + rechnungDto.getCNr()}) ;
		ex.setExceptionData(new SteuerkategorieKontoException(rechnungDto, reversechargeartId, stkkDto));
		return ex ;
	}
	
	public static EJBExceptionLP debitorkontoFehlt(KundeDto kundeDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT,
				"Debitorkonto fehlt", 
				new Object[]{kundeDto.getIId()}) ;
		ex.setExceptionData(new DebitorKontoException(kundeDto));
		return ex ;
	}

	public static EJBExceptionLP buchungenMitAuszugsnummerExistieren(SepakontoauszugDto ktoauszugDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN, 
				"Buchungen mit Auszugsnummer " + ktoauszugDto.getIAuszug() + " bereits vorhanden", 
				new Object[] {ktoauszugDto.getIAuszug()}) ;
		ex.setExceptionData(new BuchungenMitAuszugsnummerVorhandenException(ktoauszugDto));
		return ex;
	}
	
	public static EJBExceptionLP sepaVerbuchungBuchungenMitAuszugsnummerExistieren(SepakontoauszugDto ktoauszugDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN, 
				"Buchungen mit Auszugsnummer " + ktoauszugDto.getIAuszug() + " bereits vorhanden", 
				new Object[] {ktoauszugDto.getIAuszug()}) ;
		ex.setExceptionData(new BuchungenMitAuszugsnummerVorhandenException(ktoauszugDto));
		return ex;
	}
	
	public static EJBExceptionLP keinOffenerSepakontoauszug() {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_KEIN_OFFENER_SEPAKONTOAUSZUG, 
				"Kein offener Sepakontoauszug");
		return ex;
	}
	
	public static EJBExceptionLP kreditorkontoFehlt(LieferantDto lieferantDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_KEIN_KREDITORENKONTO_DEFINIERT,
				"Kein Kreditorenkonto definiert. LieferantID=" + lieferantDto.getIId()
					+ (lieferantDto.getPartnerDto() != null ? ", Lieferant=" + lieferantDto.getPartnerDto().formatName() : ""),
				new Object[] {lieferantDto.getIId()} );
		ex.setExceptionData(new KreditorKontoException(lieferantDto));
		return ex;
	}
	
	public static EJBExceptionLP lieferantFremdsystemnrNichtNummerisch(LieferantDto lieferantDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LIEFERANT_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH, 
				"Fremdsystemnr des Lieferanten muss nummerisch sein", 
				new Object[] { lieferantDto.getIId() } );
		ex.setExceptionData(new FremdsystemnrNichtNummerischException(lieferantDto));
		return ex;
	}
	
	public static EJBExceptionLP steuerkontoInKeinerSteuerkategorie(Konto steuerkonto) {
		EJBExceptionLP ex = new EJBExceptionLP( 
				EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
				"Steuerkonto " + steuerkonto.getCNr() + " ist in keiner Steuerkategorie definiert",
				new Object[] {steuerkonto.getCNr(), steuerkonto.getIId()});
		return ex;
	}
	
	public static EJBExceptionLP steuerkontoMehrfachInSteuerkategorie(Konto steuerkonto) {
		EJBExceptionLP ex = new EJBExceptionLP( 
				EJBExceptionLP.FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET2,
				"Steuerkonto " + steuerkonto.getCNr() + " ist mehrfach in Steuerkategorien definiert",
				new Object[] {steuerkonto.getCNr(), steuerkonto.getIId()});
		return ex;		
	}
	
	public static EJBExceptionLP buchungSteuersaetzeInKontenUnterschiedlich(
			Date von, Date bis, UstVerprobungRow ustVp, BuchungDto buchungDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_STEUERSAETZE_IN_KONTEN_UNTERSCHIEDLICH,
				"Steuersaetze der Konten '" + ustVp.getKontoCNr() + "' und '" 
				+ ustVp.getGegenkontoCNr() + "' innerhalb Buchung unterschiedlich",
				new Object[] {von, bis, ustVp.getKontoCNr(), ustVp.getGegenkontoCNr(), 
						buchungDto.getBelegartCNr().trim(), buchungDto.getCBelegnummer().trim(), 
						buchungDto.getDBuchungsdatum(), buchungDto.getIId()});
		ex.setExceptionData(new BuchungSteuersaetzeInKontenUnterschiedlichException(
				von, bis, ustVp.getKontoDto(), ustVp.getGegenKontoDto(), buchungDto));
		return ex;
	}
	
	public static EJBExceptionLP waehrungVonLieferscheinUndRechnungUnterschiedlich(
			RechnungDto rechnungDto, LieferscheinDto lieferscheinDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LIEFERSCHEIN_IN_RECHNUNG_WAEHRUNGEN_UNTERSCHIEDLICH, 
				"Waehrungen des Lieferscheins '" + lieferscheinDto.getCNr() + "' (" 
						+ lieferscheinDto.getWaehrungCNr() + ") und der " + "Rechnung '" 
						+ rechnungDto.getCNr() + "' (" + rechnungDto.getWaehrungCNr() + ") "
						+ "sind unterschiedlich", 
				new Object[] {rechnungDto.getCNr(), rechnungDto.getWaehrungCNr(), 
			lieferscheinDto.getCNr(), lieferscheinDto.getWaehrungCNr()});
		ex.setExceptionData(new LsAlsRePosWaehrungenUnterschiedlichException(rechnungDto, lieferscheinDto));
		return ex;
	}
	
	public static EJBExceptionLP gtinBasisnummerLaengeUngueltig(String parametername, String value) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_LAENGE_BASISNUMMER_UNGUELTIG,
				"GTIN-Basisnummer (definiert in Parameter '" + parametername + "') mit Wert '"
					+ value + "' hat nicht Laenge von 7 oder 9", 
				new Object[] {parametername, value});
		ex.setExceptionData(new GTINBasisnummerLaengeUngueltigException(parametername, value));
		return ex;
	}
	
	public static EJBExceptionLP gtinGenerierungAlleArtikelnummernVergeben(
			String basisnummer, Integer hoechstmoeglicheArtikelnummer) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_ALLE_LAUEFENDEN_ARTIKELNUMMERN_VERGEBEN,
				"Alle moeglichen Artikelnummern (1 bis " + hoechstmoeglicheArtikelnummer + ") "
						+ "sind fuer GTIN-Basisnummer '" + basisnummer + "' vergeben", 
				new Object[] {basisnummer, hoechstmoeglicheArtikelnummer});
		ex.setExceptionData(new GTINGenerierungAlleArtikelnummernVergebenException(basisnummer, hoechstmoeglicheArtikelnummer));
		return ex;
	}
	
	public static EJBExceptionLP linienabrufProduktionKeinLosGefunden(
			String kundeKBezLieferadresse, String artikelCnr, Integer forecastpositionIId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_KEIN_ZUGEHOERIGES_LOS_GEFUNDEN,
				"FEHLER_LINIENABRUF_PRODUKTION_KEIN_ZUGEHOERIGES_LOS_GEFUNDEN f\u00FCr forecastpositionIId = "
						+ forecastpositionIId, new Object[] {kundeKBezLieferadresse, artikelCnr, forecastpositionIId});
		ex.setExceptionData(new LinienabrufProduktionKeinLosGefundenException(
				kundeKBezLieferadresse, artikelCnr, forecastpositionIId));
		return ex;
	}
	
	public static EJBExceptionLP linienabrufProduktionStklNichtImMandant(String stklArtikelCnr, String mandantCnr) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_STUECKLISTE_NICHT_IM_MANDANT,
				"Stueckliste '" + stklArtikelCnr + "' befindet sich nicht im Mandant '" + mandantCnr + "'");
		ex.setExceptionData(new LinienabrufProduktionStklNichtImMandantExc(stklArtikelCnr, mandantCnr));
		return ex;
	}
	
	public static EJBExceptionLP linienabrufProduktionBerechneteAbliefermengeKleinerEins(LosDto losDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_ABLIEFERMENGE_KLEINER_EINS,
				"FEHLER_LINIENABRUF_PRODUKTION_ABLIEFERMENGE_KLEINER_EINS fuer Los '" + losDto.getCNr() + "'");
		ex.setExceptionData(new LinienabrufProduktionAbliefermengeKleinerEinsExc(losDto));
		return ex;
	}
	
	public static EJBExceptionLP linienabrufProduktionSollsatzgroessenNichtGanzzahlig(LosDto losDto, String artikelCnr) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_SOLLSATZGROESSEN_NICHT_GANZZAHLIG,
				"Sollmaterialien im Los '" + losDto.getCNr() + "' nicht ganzzahlig");
		ex.setExceptionData(new LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc(
				losDto, artikelCnr));
		return ex;
	}
	
	public static EJBExceptionLP steuerkontoMehrfachInMwstsatz(Konto steuerkonto, List<MwstsatzbezDto> mwstsatzbezDtos) {
		Object[] cnrs = new String[2];
		cnrs[0] = steuerkonto.getCNr();
		String s = "";
		for (MwstsatzbezDto bezDto : mwstsatzbezDtos) {
			if(s.length() > 0) {
				s += ", ";
			}
			s += bezDto.getCBezeichnung();		
		}
		cnrs[1] = s;
		EJBExceptionLP ex = new EJBExceptionLP( 
				EJBExceptionLP.FEHLER_FINANZ_STEUERKONTO_MEHRFACH_IN_MWSTSATZ,
				"Steuerkonto " + steuerkonto.getCNr() + " ist in unterschiedlichen Mwstsaetzen definiert",
				cnrs);
		return ex;		
	}
	
	public static EJBExceptionLP verbindungsfehlerBeiHttpPostLosausgabe(LosDto losDto, HttpProxyConfig httpProxyConfig, IOException ioExc) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSAUSGABE_HTTP_POST_VERBINDUNGSFEHLER, ioExc);
		ex.setExceptionData(new VerbindungsfehlerBeiHttpPostLosExc(losDto, httpProxyConfig));
		return ex;
	}

	public static EJBExceptionLP unerwarteterStatusCodeBeiHttpPostLosausgabe(LosDto losDto, HttpStatusCodeException e) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSAUSGABE_HTTP_POST_STATUS_CODE, e);
		ex.setExceptionData(new UnerwarteterStatusCodeBeiHttpLosExc(
				losDto, e.getStatusCode(), e.getReasonPhrase(), e.getRequestConfig()));
		return ex;
	}
	
	
	public static EJBExceptionLP verbindungsfehlerBeiHttpPostLoserledigung(LosDto losDto, HttpProxyConfig httpProxyConfig, IOException ioExc) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSERLEDIGUNG_HTTP_POST_VERBINDUNGSFEHLER, ioExc);
		ex.setExceptionData(new VerbindungsfehlerBeiHttpPostLosExc(losDto, httpProxyConfig));
		return ex;
	}

	public static EJBExceptionLP unerwarteterStatusCodeBeiHttpPostLoserledigung(LosDto losDto, HttpStatusCodeException e) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_LOSERLEDIGUNG_HTTP_POST_STATUS_CODE, e);
		ex.setExceptionData(new UnerwarteterStatusCodeBeiHttpLosExc(
				losDto, e.getStatusCode(), e.getReasonPhrase(), e.getRequestConfig()));
		return ex;
	}
	
	public static EJBExceptionLP kundeFremdsystemnrNichtNummerisch(KundeDto kundeDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_KUNDE_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH, 
				"Fremdsystemnr des Kunden muss nummerisch sein", 
				new Object[] { kundeDto.getIId() } );
		ex.setExceptionData(new FremdsystemnrNichtNummerischException(kundeDto));
		return ex;
	}
	
	public static EJBExceptionLP scriptParameterBereitsBekannt(
			CannotCompileException e, StklparameterDto paramDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_PARAMETER_MEHRFACH_DEFINIERT, 
				"Parameter '" + paramDto.getCNr() + "' bereits bekannt", 
				paramDto.getIId(), paramDto.getCNr(), paramDto.getCTyp(), e.getMessage());
		return ex;
	}
	
	public static EJBExceptionLP scriptCompileFehlerFormel(
			CannotCompileException e, Integer stuecklisteId, ScriptFormula<?> formula) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_NICHT_UEBERSETZBAR,
				"Compile-Fehler in Stueckliste '" + stuecklisteId + "' bei StklPosition '" + formula.getId() + "'",
				stuecklisteId, formula.getId(), formula.getSrc(), e.getMessage());
		return ex;
	}
	
	public static EJBExceptionLP scriptCompileFehlerKlasseNochNichtErzeugt(
			NotFoundException e, Integer stuecklisteId, ScriptFormula<?> formula) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_NICHT_GEFUNDEN,
				"Compile-Fehler in Stueckliste '" + stuecklisteId + "' bei StklPosition '" + formula.getId() + "'. Klasse noch nicht erzeugt?",
				stuecklisteId, formula.getId(), formula.getSrc(), e.getMessage());
		return ex;		
	}
	
	public static EJBExceptionLP scriptCompileFehlerKlasse(
			CannotCompileException e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_NICHT_UEBERSETZBAR,
				"Compile-Fehler in Stueckliste '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;
	}

	public static EJBExceptionLP scriptInstanceKlasseNichtGefunden(
			ClassNotFoundException e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_KLASSE_NICHT_GEFUNDEN,
				"Klasse nicht gefunden beim Instanzieren der Stueckliste '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;	
	}
	
	public static EJBExceptionLP scriptInstanceNichtMoeglich(
			InstantiationException e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH,
				"Instanzierung der Stueckliste '" + stuecklisteId + "' fehlgeschlagen",
				stuecklisteId, e.getMessage());
		return ex;	
	}
	
	public static EJBExceptionLP scriptInstanceFehlerhafterZugriff(IllegalAccessException e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_FALSCHE_ZUGRIFFSRECHTE,
				"Falsche Zugriffsrechte der Stueckliste '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;		
	}
	
	public static EJBExceptionLP scriptKlasseNichtErzeugbar(IOException e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_IO,
				"Falsche Zugriffsrechte der Stueckliste '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;				
	}
	
	public static EJBExceptionLP scriptMethodeNichtGefunden(NoSuchMethodException e, Integer stuecklisteId, Integer positionId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_METHODE_NICHT_GEFUNDEN,
				"Methode/Formel fuer PositionsId '" + positionId + "' der Stueckliste '" + stuecklisteId + "' nicht gefunden",
				stuecklisteId, positionId, e.getMessage());
		return ex;					
	}
	
	public static EJBExceptionLP scriptMethodeNichtAufrufbar(InvocationTargetException e, Integer stuecklisteId, Integer positionId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_METHODE_NICHT_GEFUNDEN,
				"Methode/Formel fuer PositionsId '" + positionId + "' der Stueckliste '" + stuecklisteId + "' nicht aufrufbar",
				stuecklisteId, positionId, e.getMessage());
		return ex;							
	}
	
	public static EJBExceptionLP scriptParameterUnbekannt(ParameterNotFoundException e, Integer stuecklisteId, ScriptFormula<?> formula) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_PARAMETER_NICHT_GEFUNDEN,
				"Parameter '" + e.getParameterName() + "' nicht gefunden in Stueckliste '" + stuecklisteId + "' bei StklPosition '" + formula.getId() + "'",
				stuecklisteId, formula.getId(), formula.getSrc(), e.getParameterName(), e.getMessage());
		return ex;
	}
	
	public static EJBExceptionLP scriptInstanceVerifyError(VerifyError e, Integer stuecklisteId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_VERIFY,
				"Erwarteter Methode/Formel Datentyp falsch bei '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;				
	}

	public static EJBExceptionLP scriptNullPointerException(NullPointerException e, Integer stuecklisteId, Integer positionId) {
		e.fillInStackTrace();
		e.getStackTrace();
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_VERIFY,
				"Erwarteter Methode/Formel Datentyp falsch bei '" + stuecklisteId + "'",
				stuecklisteId, e.getMessage());
		return ex;				
	}

	public static EJBExceptionLP sepakontoauszugNichtImAktuellenGeschaeftsjahr(SepakontoauszugDto ktoauszugDto, Integer geschaeftsjahr) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_NICHT_IM_AKTUELLEN_GJ, 
				"Kontoauszug mit Auszugsnummer " + ktoauszugDto.getIAuszug() + " befindet sich nicht im aktuellen Geschaeftsjahr " + geschaeftsjahr, 
				new Object[] {ktoauszugDto.getIAuszug(), geschaeftsjahr}) ;
		ex.setExceptionData(new SepakontoauszugNichtImAktuellenGJException(ktoauszugDto, geschaeftsjahr));
		return ex;
	}

	public static EJBExceptionLP sepaVerbuchungAuszugFalscherStatus(SepakontoauszugDto ktoauszugDto, String verbuchungsstatus) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_FALSCHER_STATUS, 
				"Kontoauszug mit Auszugsnummer " + ktoauszugDto.getIAuszug() + " kann nicht verbucht werden. Status Soll: " + verbuchungsstatus
						+ ", Ist: " + ktoauszugDto.getStatusCNr(), 
				new Object[] {ktoauszugDto.getIAuszug(), verbuchungsstatus}) ;
		ex.setExceptionData(new SepaVerbuchungAuszugFalscherStatusException(ktoauszugDto, verbuchungsstatus));
		return ex;
	}

	public static EJBExceptionLP keinDruckernameHinterlegt(String pcName, String arbeitsplatzparameter) {
		EJBExceptionLP ex =  new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_KEIN_DRUCKERNAME, 
				pcName + ": " + arbeitsplatzparameter + " fehlt", 
				arbeitsplatzparameter, pcName);
		return ex;
	}
	
	public static EJBExceptionLP unbekannteSerienChargennummer(ArtikelDto artikelDto, Collection<String> snrChnr) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_UNBEKANNTE_SERIENCHARGENNUMMER, 
				"Fuer Artikel '" + artikelDto.getCNr() + "' sind folgende Serien- oder Chargennummern unbekannt: " 
						+ Helper.erzeugeStringAusStringArray(snrChnr.toArray(new String[] {})), 
				artikelDto, snrChnr);
		return ex;
	}

	public static EJBExceptionLP fontNichtGefunden(JRFontNotFoundException fontNotFoundExc) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_SERVER, fontNotFoundExc);
		ex.setExceptionData(new FontNichtGefundenException(extractFontName(fontNotFoundExc)));
		return ex;
	}
	
	private static String extractFontName(JRFontNotFoundException exc) {
		String message = exc.getMessage();
		String[] splitted = message.split("'");
		return splitted.length > 1 ? splitted[1] : null;
	}
	
	public static EJBExceptionLP losIstAusgegeben(LosDto losDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
				new Exception("Los '" + losDto.getCNr()
						+ "' ist bereits ausgegeben"));		
	}
	
	public static EJBExceptionLP losIstStorniert(LosDto losDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
				new Exception("Los '" + losDto.getCNr()
						+ "' ist storniert"));		
	}
	
	public static EJBExceptionLP losIstErledigt(LosDto losDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
				new Exception("Los '" + losDto.getCNr()
						+ "' ist bereits erledigt"));		
	}
	
	public static EJBExceptionLP losIstNichtAusgegeben(LosDto losDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
				new Exception("Los '" + losDto.getCNr()
						+ "' ist noch nicht ausgegeben"));		
	}

	public static EJBExceptionLP reportvarianteZuReportNichtGefunden(String report, String reportvariante) {
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_UNBEKANNTE_REPORTVARIANTE_ZU_REPORT, 
				"Zu Report '" + report + "' wurde Reportvariante '" + reportvariante + "' nicht gefunden.", 
				report, reportvariante);
	}
	

	public static EJBExceptionLP keinDruckernameHinterlegtMandantenparameter(String mandantenparameter) {
		EJBExceptionLP ex =  new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_MANDANTENPARAMETER_KEIN_DRUCKERNAME, 
				"Druckername in " + mandantenparameter + " nicht definiert.", 
				mandantenparameter);
		return ex;
	}

	public static EJBExceptionLP unbekannterDruckernameHinterlegtMandantenparameter(String mandantenparameter, String druckername) {
		EJBExceptionLP ex =  new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_MANDANTENPARAMETER_UNBEKANNTER_DRUCKERNAME, 
				"Drucker '" + druckername + "' aus " + mandantenparameter + " nicht gefunden.", 
				mandantenparameter, druckername);
		return ex;
	}

	public static EJBExceptionLP zugferdLieferantennummerFehlt(RechnungDto rechnungDto, KundeDto kundeDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUGFERD_LIEFERANTENNUMMER_FEHLT, 
				"Lieferantennummer von Kunde '" + kundeDto.getPartnerDto().formatFixName1Name2() + "' ist nicht definiert.", 
				rechnungDto, kundeDto);
		return ex;
	}

	public static EJBExceptionLP zugferdKundeAdressinfoFehlt(KundeDto kundeDto) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUGFERD_ADRESSINFO_FEHLT, 
				"Strasse oder Ort von Kunde '" + kundeDto.getPartnerDto().formatFixName1Name2() + "' ist nicht definiert.", 
				kundeDto);
		return ex;
	}

	public static EJBExceptionLP zugferdEinheitMappingNichtVorhanden(String artikel, String einheit) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_ZUGFERD_EINHEIT_MAPPING_NICHT_GEFUNDEN, 
				"Einheit '" + einheit + "' von Artikel '" + artikel + "' konnte nicht gemappt werden.", 
				artikel, einheit);
		return ex;
	}
	
	public static EJBExceptionLP sqlFehler(
			SQLException e, String reportConnectionUrl) {
		String exMessage = e.getMessage();
		String nextExMessage = e.getNextException() != null ? e.getNextException().getMessage() : "";
		
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SQL_EXCEPTION_MIT_INFO,
				"SQLFehler beim Herstellen der Verbindung", 
				reportConnectionUrl, exMessage, nextExMessage);
		return ex;
	}
	
	public static EJBExceptionLP rechnungUnterschiedlicheLieferarten(RechnungDto rechnungDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_RECHNUNG_LIEFERSCHEINE_LIEFERART_UNTERSCHIEDLICH,
				"Rechnung '" + rechnungDto.getCNr() + "' hat Lieferscheine mit unterschiedlichen Lieferarten",
				rechnungDto.getIId(), rechnungDto.getCNr());
		return ex;
	}

	public static EJBExceptionLP webabfrageArtikellieferantKeinErgebnis(ArtikelDto artikelDto, ArtikellieferantDto artliefDto, 
			WebPartSearchResult resultLiefNr, WebPartSearchResult resultHstNr) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_KEIN_ERGEBNIS, 
				"Webabfrage fuer Artikel '" + artikelDto.getCNr() + "' lieferte kein Ergebnis.", 
				artikelDto, artliefDto, resultLiefNr, resultHstNr);
		ex.setExceptionData(new WebabfrageArtikellieferantException(artikelDto, artliefDto, resultLiefNr, resultHstNr));
		return ex;
	}

	public static EJBExceptionLP webabfrageArtikellieferantMehrfacheErgebnisse(ArtikelDto artikelDto, ArtikellieferantDto artliefDto, 
			WebPartSearchResult resultLiefNr, WebPartSearchResult resultHstNr) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_MEHRFACHE_ERGEBNISSE, 
				"Webabfrage fuer Artikel '" + artikelDto.getCNr() + "' lieferte mehrfache Ergebnisse.", 
				artikelDto, artliefDto, resultLiefNr, resultHstNr);
		ex.setExceptionData(new WebabfrageArtikellieferantException(artikelDto, artliefDto, resultLiefNr, resultHstNr));
		return ex;
	}

	public static EJBExceptionLP webabfrageLiefertUnerwarteteResponse(ArtikelDto artikelDto, ArtikellieferantDto artliefDto, PartSearchUnexpectedResponseExc exc) {
		EJBExceptionLP ex = new EJBExceptionLP(
				exc.getEjbCode(), 
				"Webabfrage fuer Artikel '" + artikelDto.getCNr() + "' lieferte unerwartete Response: " + exc.getStatusLine(), 
				artikelDto, artliefDto, exc);
		ex.setExceptionData(new WebabfrageArtikellieferantException(artikelDto, artliefDto, null, null));
		return ex;
	}
	
	public static EJBExceptionLP empfaengerSonderzeitenAntragNichtGefunden(
			PersonalDto personalDto_Selektiert, PersonalDto personalDto_Angemeldet) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_EMAIL_SONDERZEITENANTRAG_NICHT_ERMITTELBAR,
				"Emailempfaenger fuer Sonderzeiten nicht ermittelbar '" +
				personalDto_Angemeldet.formatAnrede() + "', '" +
				personalDto_Angemeldet.formatAnrede() + "'",
				personalDto_Selektiert.getIId(), personalDto_Angemeldet.getIId());
		return ex;
	}
	
	private static EJBExceptionLP saldoInfoUngleichImpl(
			int errorCode, String msg, BigDecimal value, SaldoInfoDto[] saldoInfoDto,
			Integer kontoId, int periode) {
		EJBExceptionLP ex = new EJBExceptionLP(errorCode, msg  + " SALDO (" 
						+ value.toPlainString() + ") != SALDOSUMME [" 
						+ saldoInfoDto[0].getSaldo() + ", " 
						+ saldoInfoDto[1].getSaldo() + "]", 
						value.toPlainString(),
						saldoInfoDto[0].getSaldo().toPlainString(),
						saldoInfoDto[1].getSaldo().toPlainString(),
						kontoId, periode);
		return ex;		
	}

	public static EJBExceptionLP saldoInfoSollUngleich(BigDecimal value,
			SaldoInfoDto[] saldoInfoDto, Integer kontoId, int periode) {
		return saldoInfoUngleichImpl(
				EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_SOLL_UNGLEICH, "SOLL",
					value, saldoInfoDto, kontoId, periode);
	}

	public static EJBExceptionLP saldoInfoHabenUngleich(BigDecimal value, 
			SaldoInfoDto[] saldoInfoDto, Integer kontoId, int periode) {
		return saldoInfoUngleichImpl(
				EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_HABEN_UNGLEICH, "HABEN", 
					value, saldoInfoDto, kontoId, periode);
	}
	
	public static EJBExceptionLP saldoInfoSteuersatzUngleich(MwstsatzDto sollSatz, MwstsatzDto habenSatz) {
		Integer sollSatzId = sollSatz != null ? sollSatz.getIId() : null;
		String sollSatzS = sollSatz != null ? sollSatz.getFMwstsatz().toString() : "null";
		Integer habenSatzId = habenSatz != null ? habenSatz.getIId() : null;
		String habenSatzS = habenSatz != null ? habenSatz.getFMwstsatz().toString() : "null";
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_MWSTSATZ_UNGLEICH, 
				"Mwstsatz Id soll[" + sollSatzId + "] != haben [" + habenSatzId + "]", 
				sollSatzId, habenSatzId, sollSatzS, habenSatzS);
		return ex;				
	}
	
	public static EJBExceptionLP angebotPositionenOhneZws(List<Integer> positionNumbers) {
		String positions = "";
		int maxPositions = 5;
		if(positionNumbers.size() >= maxPositions) {
			positions = StringUtils.join(
					positionNumbers.subList(0, maxPositions - 1).iterator(), ", ");
			positions += " ... ";
			positions += positionNumbers.get(positionNumbers.size() - 1);
		} else {
			positions = StringUtils.join(positionNumbers.iterator(), ", ");
		}
		
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_ANGEBOT_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS, 
				"Fehler bei aktiviereAngebot. Es gibt mengenbehaftete Positionen die nicht in einer Zwischensumme enthalten sind.",
				positionNumbers.toArray(), positions);
		return ex;
	}
	
	public static EJBExceptionLP auftragPositionenOhneZws(List<Integer> positionNumbers) {
		String positions = "";
		int maxPositions = 5;
		if(positionNumbers.size() >= maxPositions) {
			positions = StringUtils.join(
					positionNumbers.subList(0, maxPositions - 1).iterator(), ", ");
			positions += " ... ";
			positions += positionNumbers.get(positionNumbers.size() - 1);
		} else {
			positions = StringUtils.join(positionNumbers.iterator(), ", ");
		}
		
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_AUFTRAG_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS, 
				"Fehler bei aktiviereAuftrag. Es gibt mengenbehaftete Positionen die nicht in einer Zwischensumme enthalten sind.",
				positionNumbers.toArray(), positions);
		return ex;
	}
	
	
	public static EJBExceptionLP duplicateUniqueKey(String tablename, Object... params) {
		String parameter = StringUtils.join(params, ", ");
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE_EXTENDED, 
				"Es gibt bereits einen Datensatz in Tabelle " + tablename + " mit den Schluesseldaten: " + parameter, 
				tablename, params, parameter);
	}

	public static EJBExceptionLP noUniqueResults(String tablename, Object... params) {
		String parameter = StringUtils.join(params, ", ");
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT_EXTENDED, 
				"Es gibt mehr als einen Datensatz in Tabelle " + tablename + " mit den selben Schluesseldaten: " + parameter, 
				tablename, params, parameter);
	}

	public static EJBExceptionLP fcLieferadresseMehrdeutig(int countFcLieferadressen, Integer lieferKundeId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FORECAST_MEHRERE_LIEFERADRESSEN_FUER_KUNDE,
				"Es gibt keine/mehrere Lieferadressen fuer den Kunden",
				countFcLieferadressen, lieferKundeId);
		return ex;
	}
	
	public static EJBExceptionLP keinFreigegebenerForecastauftrag(Integer fcLieferadresseId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FORECAST_KEIN_FREIGEGEBENER_FCAUFTRAG_VORHANDEN,
				"Es gibt keinen freigegebenen Forecast-Auftrag fuer die Forecast-Lieferadresse",
				fcLieferadresseId);
		return ex;
	}
	
	public static EJBExceptionLP keinForecastOderAuftragGefunden(Integer artikelId, Integer lieferkundeId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LIEFERSCHEIN_ARTIKEL_NICHT_IN_FORECAST_AUFTRAG,
				"Der Artikel (Id:" + artikelId + ") ist weder in einem freigegebenen Forecast " + 
				"noch in einem Auftrag, Lieferkunde (Id:" + lieferkundeId + ").",
				artikelId, lieferkundeId);
		return ex;
	}

	public static EJBExceptionLP bestellungXmlLieferantenartikelnummerFehlt(BestellungDto bestellungDto,
			OpenTransOrderPosition pos) {
		String artikelNr = pos != null ? pos.getItemNumber() : null;
		String posNr = pos != null ? pos.getLineItemId() : null;
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BESTELLUNG_XML_TRANSFORMATION_LIEFARTIKELNR_FEHLT, 
				"In Bestellung " + bestellungDto.getCNr() + " ist fuer Artikel '" + artikelNr + "' (Pos " + posNr + ")" +
				" keine Lieferantenartikelnummer hinterlegt.", 
				bestellungDto.getCNr(), artikelNr, posNr);
		return ex;
	}

	public static EJBExceptionLP bestellungXmlMarshalling(BestellungDto bestellungDto,
			OpenTransOrder openTransOrder, Exception e) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BESTELLUNG_XML_MARSHALLING_EXC, e);
		ex.setAlInfoForTheClient(Arrays.asList(new Object[] {bestellungDto, openTransOrder}));
		return ex;
	}

	public static EJBExceptionLP bestellungXmlFehlendesEinheitMapping(BestellungDto bestellungDto, 
			OpenTransOrderPosition pos, String sEinheit) {
		String artikelNr = pos != null ? pos.getItemNumber() : null;
		String posNr = pos != null ? pos.getLineItemId() : null;
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BESTELLUNG_XML_FEHLENDES_EINHEITENMAPPING, 
				"Fehlendes Unece Einheitenmapping fuer Einheit '" + sEinheit + "' in Bestellung " + bestellungDto.getCNr() + 
				" fuer Artikel '" + artikelNr + "' (Pos " + posNr + ")", 
				bestellungDto.getCNr(), artikelNr, posNr, sEinheit);
		return ex;
	}

	public static EJBExceptionLP bestellungXmlLieferantKundennummerFehlt(BestellungDto bestellungDto,
			LieferantDto lieferantDto) {
		String liefName = lieferantDto.getPartnerDto() != null ? lieferantDto.getPartnerDto().formatFixName1Name2() : "null";
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BESTELLUNG_XML_LIEFKUNDENNUMMER_FEHLT, 
				"Im Lieferanten '" + liefName + "' von Bestellung " + bestellungDto.getCNr() + 
				" keine Kundennummer hinterlegt.", 
				bestellungDto.getCNr(), lieferantDto.getIId(), liefName);
		return ex;
	}
	
	public static EJBExceptionLP bestellNummerInForecastpositionFehlt(
			LieferscheinDto lsDto, Integer lsposNr, Integer forecastpositionId) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FORECAST_BESTELLNUMMER_FEHLT,
				"In der Forecastposition ("
					+ forecastpositionId + ") der Lieferscheinposition '" 
					+ lsposNr + "' des Lieferscheins '"
					+ lsDto.getCNr() + "' fehlt die Bestellnummer.",
				lsDto.getCNr(), lsposNr, forecastpositionId
			);
		return ex;
	}
	
	public static EJBExceptionLP mailtextvorlageNichtGefunden(String modul, String mandant, String xslFile, Locale sprache) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_NICHT_GEFUNDEN,
				"Es konnte keine Vorlage des Mailtextes gefunden werden (mail.xsl" 
					+ (Helper.isStringEmpty(xslFile) ? "" : " od. " + xslFile + ".xsl") 
					+ "). Mandant: " + mandant + ", Sprache: " + sprache 
					+ (Helper.isStringEmpty(xslFile) ? "" : ", XSL-File: " + xslFile),
				mandant, sprache, modul, xslFile);
		
		return ex;
	}
	
	public static EJBExceptionLP mailtextvorlageMitSignaturNichtGefunden(String modul, String mandant, String xslFile, Locale sprache) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_MIT_SIGNATUR_NICHT_GEFUNDEN,
				"Es konnte keine Vorlage des HTML-Mailtextes mit Signatur gefunden werden (mail_signatur.xsl"
						+ (Helper.isStringEmpty(xslFile) ? ")" : " od. " + xslFile + "_signatur.xsl)") 
						+ "). Mandant: " + mandant + ", Sprache: " + sprache 
						+ (Helper.isStringEmpty(xslFile) ? "" : ", XSL-File: " + xslFile),
				mandant, sprache, modul, xslFile);
		
		return ex;
	}
	
	public static EJBExceptionLP mailtextvorlageHtmlNichtGefunden(String modul, String mandant, String xslFile, Locale sprache) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_HTML_NICHT_GEFUNDEN,
				"Es konnte keine Vorlage des HTML-Mailtextes gefunden werden (mail_html.xsl"
						+ (Helper.isStringEmpty(xslFile) ? "" : " od. " + xslFile + "_html.xsl") 
						+ "). Mandant: " + mandant + ", Sprache: " + sprache 
						+ (Helper.isStringEmpty(xslFile) ? "" : ", XSL-File: " + xslFile),
				mandant, sprache, modul, xslFile);
		
		return ex;
	}
	
	public static EJBExceptionLP formelUserException(String message, Integer stuecklisteId, Integer positionId) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FORMELSTUECKLISTE_ABBRUCH,
				"Abbruch durch Benutzer (StuecklisteId: " + stuecklisteId 
				+ ", PositionId: " + positionId + ").", message, stuecklisteId, positionId);
	}
	
	public static EJBExceptionLP absenderEMailZeitbestaetigungFehlt(PersonalDto personalDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_EMAIL_ZEITBESTAETIGUNG_NICHT_ERMITTELBAR, 
				"Email-Absender fuer Zeitbestaetigung nicht ermittelbar '" +
				personalDto.formatAnrede(), personalDto.getIId());
	}
	
	public static EJBExceptionLP systemRolleRestApiFehlt(
			String benutzer, Integer systemrolleId) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_RESTAPI_BENOETIGT_SYSTEMROLLE_RESTAPI,
				"Benutzermandantsystemrolle fuer '" + benutzer + "' hat keine Restapi-Rolle",
				benutzer, systemrolleId);
	}
	
	public static EJBExceptionLP systemRolleHvmaFehlt(
			String benutzer, Integer systemrolleId) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BENUTZER_SYSTEMROLLE_HVMA_NICHT_GESETZT,
				"Benutzermandantsystemrolle fuer '" + benutzer + "' hat keine HVMA-Rolle",
				benutzer, systemrolleId);
	}
	
	public static EJBExceptionLP plcApiKeyUngueltigesFormat(String paramvalue) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_PLC_APIKEY_FALSCHES_FORMAT, 
					"PARAMETER_POST_PLC_APIKEY hat falsches Format");
	}

	public static EJBExceptionLP lieferartUngueltigFuerPlc(
			LieferscheinDto lsDto, Lieferart lieferart) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_LIEFERSCHEIN_HAT_KEINE_PLC_LIEFERART,
				"Lieferschein '" + lsDto.getCNr() + "' hat keine gueltige Lieferart",
				lsDto.getCNr(), lieferart != null ? lieferart.getIId() : null, 
						lieferart != null ? lieferart.getCExtern() : null);
	}

	public static EJBExceptionLP sepaVerbuchungZahlungenMitAuszugsnummerExistieren(SepakontoauszugDto ktoauszugDto,
			List<RechnungDto> reDtos, List<EingangsrechnungDto> erDtos) {
		EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_ZAHLUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN, 
				"Zahlungen mit Auszugsnummer " + ktoauszugDto.getIAuszug() + " bereits vorhanden", 
				ktoauszugDto.getIAuszug(), reDtos, erDtos) ;
		ex.setExceptionData(new SepaZahlungenMitAuszugsnummerVorhandenException(
				ktoauszugDto, reDtos, erDtos));
		return ex;
	}


	public static EJBExceptionLP zwsPositionNichtUmschliessend(
			BelegpositionVerkaufDto verifyZwsPosition, 
			int myBeginnIndex, int myEndIndex) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_NICHT_UMSCHLIESSEND,
				"Zwischensumme nicht umschliessend", 
				verifyZwsPosition.getIId(), myBeginnIndex, myEndIndex);
		return ex;
	}

	public static EJBExceptionLP bilanzgruppendefinitionPositivNegativFalsch(Konto konto) {
		EJBExceptionLP ex;
		if (konto.getErgebnisgruppeIId() == null) {
			ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BILANZGRUPPENDEFINITON_POS_NEG,
					"Bilanzgruppendefinition falsch bei Konto " + konto.getCNr() + ": Bilanzgruppe fehlt.",
					konto.getCNr());
		} else {
			ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BILANZGRUPPENDEFINITON_NEGATIV,
					"Bilanzgruppendefinition falsch bei Konto " + konto.getCNr() + ": Negative Bilanzgruppe fehlt.",
					konto.getCNr());
		}

		return ex;
	}
	
	public static EJBExceptionLP ursprungslandFehlt(ArtikelDto artikelDto) {
		EJBExceptionLP ex = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_ARTIKEL_URSPRUNGSLAND_FEHLT, 
				"Im Artikel '" + artikelDto.getCNr() + "' fehlt das Ursprungsland.",
				artikelDto.getCNr(), artikelDto.getIId());
		return ex;
	}

	public static EJBExceptionLP maschineIdentifikationsnrExistiertBereits(Maschine maschine, String angemeldeterMandantCnr) {
		if (angemeldeterMandantCnr.equals(maschine.getMandantCNr())) {
			return new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR, 
					"Es existiert bereits eine Maschine mit Identfikationsnummer '" + maschine.getCIdentifikationsnr() + "'.",
					maschine.getCIdentifikationsnr());
		}
		
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR_ANDERER_MANDANT, 
				"Es existiert bereits eine Maschine mit Identfikationsnummer '" + maschine.getCIdentifikationsnr() 
					+ "' in Mandant '" + maschine.getMandantCNr() + "'.",
				maschine.getCIdentifikationsnr(), maschine.getMandantCNr());
	}
	
	public static EJBExceptionLP absenderEMailLieferscheinbestaetigungFehlt(PersonalDto personalDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_EMAIL_LIEFERSCHEINBESTAETIGUNG_NICHT_ERMITTELBAR, 
				"Email-Absender fuer Lieferscheinbestaetigung nicht ermittelbar '" +
				personalDto.formatAnrede(), personalDto.getIId());
	}

	public static EJBExceptionLP bankverbindungKeinIso20022StandardDefiniert(BankverbindungDto bankverbindungDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_SEPAEXPORT_KEIN_STANDARD_IN_BANKVERBINDUNG, 
				"Fuer Bankverbindung '" + bankverbindungDto.getCBez() + "' wurde kein ISO20022 Zahlungsstandard (" 
						+ StringUtils.join(Iso20022StandardEnum.values(), ", ") + ") definiert", 
				bankverbindungDto.getIId());
	}
	
	public static EJBExceptionLP mwstsatzFehlt(Integer mwstsatzbezId, Timestamp datum) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_MWSTSATZ_NICHT_GEFUNDEN,
				"Mehrwertsteuersatz zu Bezeichnung (Id " + mwstsatzbezId + ") " +
				"und Datum " + datum.toString() + " nicht gefunden.", 
				mwstsatzbezId, datum);
	}
	
	public static EJBExceptionLP primaryKeyNotFound(BaseIntegerKey key) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
				"Primary Key nicht gefunden", key.id().toString());
	}

	public static EJBExceptionLP fibuExportRZLUstLandfehlt(LandDto landDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KEIN_UST_LAND_CODE,
				"Kein USt Code fuer Land " + landDto.getCName() + 
				" (" + landDto.getCLkz() + ") definiert.", 
				landDto.getCLkz(), landDto.getCName());
	}

	public static EJBExceptionLP fibuExportRZLUstLandNichtNumerisch(LandDto landDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KEIN_NUMERISCHER_UST_LAND_CODE,
				"USt Code fuer Land " + landDto.getCName() + 
				" (" + landDto.getCLkz() + ") darf nur numerisch sein. Aktueller Wert: " + landDto.getCUstcode(), 
				landDto.getCLkz(), landDto.getCName(), landDto.getCUstcode());
	}
	
	public static EJBExceptionLP artikelBenoetigtGewicht(ArtikelDto artikelDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_ARTIKEL_BENOETIGT_GEWICHT,
				"Artikel " + artikelDto.getCNr() + 
				" (" + artikelDto.getIId() + ") benoetigt Gewicht", 
				artikelDto.getIId(), artikelDto.getCNr());		
	}
	
	public static EJBExceptionLP uidBenoetigt(PartnerDto partnerDto) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_UID_BENOETIGT,
				"UID fehlt " + partnerDto.getCKbez() + ", " + partnerDto.getCName1nachnamefirmazeile1() + 
				" (" + partnerDto.getIId() + ")", 
				partnerDto.getIId());				
	}
	
	public static EJBExceptionLP differentKeyTypes(Class keyClass, Class otherClass) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_KEY_KLASSEN, 
				"Unterschiedliche Klassen in der Keyliste '" + keyClass.getName() + "' vs. '" + otherClass.getName() + "'",
				keyClass.getName(), otherClass.getName());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg, ArtikelDto artikelDto) {
		return new EJBExceptionLP(ejbCode, errorMsg, artikelDto.getIId(), artikelDto.getCNr());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg, LosDto losDto) {
		return new EJBExceptionLP(ejbCode, errorMsg, losDto.getIId(), losDto.getCNr());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg,
			LossollmaterialDto sollmaterialDto, ArtikelDto sollmaterialArtikelDto) {
		return new EJBExceptionLP(ejbCode, errorMsg, 
				sollmaterialDto.getIId(), sollmaterialArtikelDto.getIId(), sollmaterialArtikelDto.getCNr());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg, KundeDto kundeDto) {
		return new EJBExceptionLP(ejbCode, errorMsg, kundeDto.getIId(), kundeDto.getPartnerDto().formatFixName1Name2());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg,
			ArtikelDto artikelDto, ArtikelDto artikelDto2) {
		return new EJBExceptionLP(ejbCode, errorMsg, artikelDto.getIId(), artikelDto.getCNr(), artikelDto2.getIId(), artikelDto2.getCNr());
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg,
			LossollarbeitsplanDto agDto) {
		return new EJBExceptionLP(ejbCode, errorMsg, agDto.getIId(), agDto.getArtikelIIdTaetigkeit(), agDto.getIArbeitsgangnummer());
	}

	public static EJBExceptionLP httpPostLosausgabeTopsUnbekannt(Integer ejbCode, String errorMsg) {
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_TOPS_UNBEKANNT, errorMsg);
	}

	public static EJBExceptionLP httpPostLosausgabeTopsFallbackMehrereCadfiles(String errorMsg,
			ArtikelDto artikelDto) {
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_TOPS_FALLBACK_MEHRERE_CADFILES_PRO_ARTIKEL, 
				errorMsg, artikelDto.getIId(), artikelDto.getCNr(), errorMsg);
	}

	public static EJBExceptionLP zwsPositionUnvollstaendig(
			Integer vonPosNummer, Integer bisPosNummer, 
			String zwsText, Integer zwsPosId, Integer zwsPosNummer) {
		return new EJBExceptionLP(EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
				"Position '" + zwsText + "' unvollst\u00E4ndig",
				zwsText, zwsPosId, vonPosNummer, bisPosNummer, zwsPosNummer);
		
	}
	public static EJBExceptionLP zwsPositionUnterschiedlicheMwstsatzIds(
			Integer zwsPosId, Integer vonPosNr, Integer bisPosNr) {
		return new EJBExceptionLP(
				EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH_ZWSPOS,
				"Zwischensumme MwstsatzId unterschiedlich", 
				zwsPosId, vonPosNr, bisPosNr);
	}

	public static EJBExceptionLP httpPostLosausgabeTops(Integer ejbCode, String errorMsg,
			ArtikelDto artikelDto, String path) {
		return new EJBExceptionLP(ejbCode, errorMsg, artikelDto.getIId(), artikelDto.getCNr(), path);
	}
}
