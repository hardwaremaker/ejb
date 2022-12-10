package com.lp.server.fertigung.bl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.lp.server.fertigung.service.CsvVerbrauchsartikel;
import com.lp.server.fertigung.service.FertigungImportFac;
import com.lp.server.fertigung.service.errors.ImportException;
import com.lp.server.fertigung.service.errors.NoSuchColumnException;
import com.lp.server.fertigung.service.errors.NumberOfColumnsException;
import com.lp.server.fertigung.service.errors.ParseDateFormatException;
import com.lp.server.fertigung.service.errors.ParseNumberFormatException;
import com.lp.server.util.DefaultFieldnameIndexer;
import com.lp.server.util.IFieldnameIndexer;
import com.lp.util.Helper;

public class CsvVerbrauchsartikelTransformer {

	private IFieldnameIndexer fieldIndex = new DefaultFieldnameIndexer(new HashMap<String, Integer>());
	private DateFormat dateFormatter;
	private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	
	public DateFormat getDateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = new SimpleDateFormat(dateTimeFormat);
		}
		return dateFormatter;
	}
	
	public void setFieldIndex(IFieldnameIndexer fieldIndex) {
		this.fieldIndex = fieldIndex;
	}
	
	public void buildFieldIndexer(String[] csvFields) {
		Map<String, Integer> map = new HashMap<String, Integer>() ;
		
		for (int i = 0; i < csvFields.length; i++) {
			String s =  csvFields[i].trim() ;
			if(s.length() == 0) continue ;
			
			map.put(s, i) ;
		}
		
		fieldIndex.setMap(map) ;
	}
	
	protected String getValue(String[] csvFields, String fieldname) throws ImportException {
		try {
			Integer index = fieldIndex.getIndex(fieldname);
			if (index >= csvFields.length) {
				throw new NumberOfColumnsException(index, csvFields.length);
			}
			return csvFields[index];
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchColumnException(fieldname);
		}
	}
	
	public CsvVerbrauchsartikel getVerbrauchsartikel(String[] csvFields) throws ImportException {
		if (csvFields == null) {
			throw new NumberOfColumnsException();
		}
		
		CsvVerbrauchsartikel verbrauchsartikel = new CsvVerbrauchsartikel();
		verbrauchsartikel.setArtikelnummer(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.ARTIKELNUMMER)));
		verbrauchsartikel.setMenge(getBigDecimal(FertigungImportFac.GSKassenFieldnames.MENGE, 
				getValue(csvFields, FertigungImportFac.GSKassenFieldnames.MENGE)));
		verbrauchsartikel.setPosition(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.POSITIONSNUMMER)));
		verbrauchsartikel.setRechnungsdatum(getDate(FertigungImportFac.GSKassenFieldnames.RECHNUNGSDATUM, 
				getValue(csvFields, FertigungImportFac.GSKassenFieldnames.RECHNUNGSDATUM)));
		verbrauchsartikel.setRechnungsnummer(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.RECHNUNGSNUMMER)));
		verbrauchsartikel.setArtikelbezeichnung(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.ARTIKELBEZEICHNUNG)));
		verbrauchsartikel.setAbschlussdatum(getDate(FertigungImportFac.GSKassenFieldnames.ABSCHLUSSDATUM, 
				getValue(csvFields, FertigungImportFac.GSKassenFieldnames.ABSCHLUSSDATUM)));
		verbrauchsartikel.setEinzelpreis(getBigDecimal(FertigungImportFac.GSKassenFieldnames.EINZELPREIS, 
				getValue(csvFields, FertigungImportFac.GSKassenFieldnames.EINZELPREIS)));
		verbrauchsartikel.setGesamtpreis(getBigDecimal(FertigungImportFac.GSKassenFieldnames.GESAMTPREIS, 
				getValue(csvFields, FertigungImportFac.GSKassenFieldnames.GESAMTPREIS)));
		verbrauchsartikel.setZahlungsart(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.ZAHLUNGSART)));
		verbrauchsartikel.setPlu(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.PLU)));
		verbrauchsartikel.setArtikelhauptgruppe(getTrimmed(getValue(csvFields, FertigungImportFac.GSKassenFieldnames.HAUPTWARENGRUPPE)));
		
		return verbrauchsartikel;
	}

	private Timestamp getDate(String column, String value) throws ImportException {
		try {
			return new Timestamp(getDateFormatter().parse(value).getTime()) ;
		} catch(ParseException ex) {
			throw new ParseDateFormatException(column, dateTimeFormat, value, ex);
		}
	}

	private BigDecimal getBigDecimal(String column, String value) throws ImportException {
		try {
			return Helper.toBigDecimal(value);
		} catch (NumberFormatException ex) {
			throw new ParseNumberFormatException(column, value, ex);
		}
	}

	private String getTrimmed(String value) {
		return value == null ? "" : value.trim();
	}
}
