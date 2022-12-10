package com.lp.server.finanz.bl;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.assembler.KontoDtoAssembler;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.util.Validator;
import com.lp.util.Helper;

public class FinanzValidator {

	/**
	 * Die SteuerkategorieCnr muss vorhanden sein
	 * 
	 * @param konto das zu pr&uuml;fende Konto
	 */
	public static void steuerkategorieDefinition(Konto konto) {
		Validator.notNull(konto, "konto") ;
		if(Helper.isStringEmpty(konto.getSteuerkategorieCNr())) {
			KontoDto kontoDto = KontoDtoAssembler.createDto(konto) ;
			throw EJBExcFactory.steuerkategorieDefinitionFehlt(kontoDto) ;
		}
	}

	/**
	 * Die SteuerkategorieCnr muss vorhanden sein
	 * 
	 * @param konto das zu pr&uuml;fende Konto
	 * @param rechnungDto die Rechnung als "Nutzlast" 
	 */
	public static void steuerkategorieDefinition(Konto konto, RechnungDto rechnungDto) {
		Validator.notNull(konto, "konto") ;
		if(Helper.isStringEmpty(konto.getSteuerkategorieCNr())) {
			KontoDto kontoDto = KontoDtoAssembler.createDto(konto) ;
			throw EJBExcFactory.steuerkategorieDefinitionFehlt(rechnungDto, kontoDto) ;
		}
	}

	/**
	 * Die SteuerkategorieCnr muss vorhanden sein
	 * 
	 * @param konto das zu pr&uuml;fende Konto
	 * @param erDto die Eingangsrechnung als "Nutzlast" 
	 */
	public static void steuerkategorieDefinition(Konto konto, EingangsrechnungDto erDto) {
		Validator.notNull(konto, "konto") ;
		if(Helper.isStringEmpty(konto.getSteuerkategorieCNr())) {
			KontoDto kontoDto = KontoDtoAssembler.createDto(konto) ;
			throw EJBExcFactory.steuerkategorieDefinitionFehlt(erDto, kontoDto) ;
		}
	}
	
	public static void debitorkontoDefinition(KundeDto kundeDto) {
		Validator.notNull(kundeDto, "kundeDto");
		if(kundeDto.getIidDebitorenkonto() == null) {
			throw EJBExcFactory.debitorkontoFehlt(kundeDto) ;
		}
	}
	
	public static void kreditorkontoDefinition(LieferantDto lieferantDto) {
		Validator.notNull(lieferantDto, "lieferantDto");
		if (lieferantDto.getKontoIIdKreditorenkonto() == null) {
			throw EJBExcFactory.kreditorkontoFehlt(lieferantDto);
		}
	}
}
