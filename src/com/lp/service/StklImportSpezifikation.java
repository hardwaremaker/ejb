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
package com.lp.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.system.service.SystemServicesFac;

public abstract class StklImportSpezifikation implements Externalizable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String mandant_cnr;
	private boolean fixedWidth;
	private List<Integer> widths;
	protected List<String> columnTypes;
	private String separator;
	private int fromRow = 1;
	private boolean withHiddenArticles;
	private int stklIId;
	protected List<String> availableColumnTypes;
	
	public static final String UNDEFINED = "<undefined>";
	public static final String KUNDENARTIKELNUMMER = "Kundenartikelnummer";
	public static final String LIEFERANTENARTIKELNUMMER = "Lieferantenartikelnummer";
	public static final String HERSTELLERARTIKELNUMMER = "Herstellerartikelnummer";
	public static final String HERSTELLERBEZ = "Herstellerbezeichnung";
	public static final String BEZEICHNUNG = "Bezeichnung";
	public static final String BEZEICHNUNG1 = "Bezeichnung1";
	public static final String BEZEICHNUNG2 = "Bezeichnung2";
	public static final String BEZEICHNUNG3 = "Bezeichnung3";
	public static final String MENGE = "Menge";
	public static final String ARTIKELNUMMER = "Artikelnummer";
	public static final String KOMMENTAR = "Kommentar";
	public static final String POSITION = "Position";
	public static final String SI_WERT = "SI-Wert";
	public static final String BAUFORM = "Bauform";
	public static final String REFERENZBEZEICHNUNG = "Referenzbezeichnung";
	public static final String BESTELLPREIS = "Importpreis";
	public static final String INTERNE_BEMERKUNG = "Interne Bemerkung";
	public static final String HERSTELLER = "Hersteller";
	public static final String LIEFPREIS = "Lieferantenpreis";
	public static final String DIM_BREITE = "Breite";
	public static final String DIM_HOEHE = "H\u00f6he";
	public static final String DIM_TIEFE = "Tiefe";
	public static final String LAUFENDE_NUMMER = "Lfd. Nummer";
	
	protected static final String EXT_SEPARATOR = "\r" ;
	
	public static class SpezifikationsTyp {		
		public static final int FERTIGUNGSSTKL_SPEZ = 1;
		public static final int ANGEBOTSSTKL_SPEZ = 2;
		public static final int EINKAUFSANGEBOTSSTKL_SPEZ = 3;
		public static final int BESTELLUNGSTKL_SPEZ = 4;
	}
	
	public static class JsonProperties {
		public static final String NAME = "name";
		public static final String MANDANT_CNR = "mandant";
		public static final String FIXED_WIDTH = "fixedWidth";
		public static final String WIDTHS = "widths";
		public static final String COLUMN_TYPES = "columnTypes";
		public static final String SEPARATOR = "separator";
		public static final String FROM_ROW = "fromRow";
		public static final String WITH_HIDDEN_ARTICLES = "withHiddenArticles";
		public static final String STKL_IID = "stklIId";
		public static final String MONTAGEART_IID = "montageartIId";
	}
	
	private static Map<Integer, String> keyValueCGruppe = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -5655185103585548412L;
		{
			put(SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ, SystemServicesFac.KEYVALUE_STKL_IMPORT_SPEZ);
			put(SpezifikationsTyp.ANGEBOTSSTKL_SPEZ, SystemServicesFac.KEYVALUE_AGSTKL_IMPORT_SPEZ);
			put(SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ, SystemServicesFac.KEYVALUE_EINKAUFSAGSTKL_IMPORT_SPEZ);
			put(SpezifikationsTyp.BESTELLUNGSTKL_SPEZ, SystemServicesFac.KEYVALUE_BESTELLUNGSTKL_IMPORT_SPEZ);
		}
	};

	public StklImportSpezifikation() {
		setSeparator("");
		setWidths(new ArrayList<Integer>());
		setColumnTypes(new ArrayList<String>());
		setAvailableColumnTypes();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMandantCnr() {
		return mandant_cnr;
	}

	public void setMandantCnr(String mandant_cnr) {
		this.mandant_cnr = mandant_cnr;
	}

	public boolean isFixedWidth() {
		return fixedWidth;
	}

	public void setFixedWidth(boolean fixedWidth) {
		this.fixedWidth = fixedWidth;
	}

	public boolean isWithHiddenArticles() {
		return withHiddenArticles;
	}

	public void setWithHiddenArticles(boolean withHiddenArticles) {
		this.withHiddenArticles = withHiddenArticles;
	}

	public List<Integer> getWidths() {
		return widths;
	}

	public void setWidths(List<Integer> widths) {
		this.widths = widths;
	}

	public List<String> getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(List<String> columnType) {
		this.columnTypes = columnType;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public int getFromRow() {
		return fromRow;
	}

	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}

	public int getStklIId() {
		return stklIId;
	}

	public void setStklIId(int stklIId) {
		this.stklIId = stklIId;
	}
	
	/**
	 * Liefert alle ColumnTypes f&uuml;r das Mapping der Spalten, die
	 * f&uuml;r diese Spezifikation zur Verf&uuml;gung stehen sollen.
	 * 
	 * @return alle m&ouml;glichen ColumnTypes
	 */
	public List<String> getAllImportColumnTypes() {
		return availableColumnTypes;
	}

	/**
	 * Setter f&uuml;r die Auswahlm&ouml;glichkeiten der ColumnTypes beim
	 * Mapping der Spalten. Kann sich nach Spezifikationsart unterscheiden.
	 */
	protected abstract void setAvailableColumnTypes();

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		IReader reader = new JsonReader(in.readUTF());
		readString(reader);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(writeString());
	}

	/**
	 * Holt die Spezifikation aus dem BufferedReader und setzt die Variablen
	 * 
	 * @param reader
	 * @throws IOException
	 */
	public void readString(IReader reader) throws IOException {
		if (reader instanceof DefaultReader) {
			readCommonValues(reader);
			readIndividualValues(reader);
			readColumnTypes(reader);
		} else if (reader instanceof JsonReader) {
			readCommonValuesJson(reader);
			readIndividualValuesJson(reader);
			readColumnTypesJson(reader);
		}
	}

	/**
	 * Liest spezifikationsartspezifische Variablen aus der Spezifikation
	 * 
	 * @param reader
	 * @throws IOException
	 */
	protected abstract void readIndividualValues(IReader reader) throws IOException;

	protected abstract void readIndividualValuesJson(IReader reader) throws IOException;
	
	/**
	 * @param br
	 * @throws IOException
	 */
	private void readColumnTypes(IReader reader) throws IOException {
		int size = reader.getInteger();
		setColumnTypes(new ArrayList<String>());
		for(int i = 0; i < size; i++) {
			getColumnTypes().add(reader.getString());
		}
		size = reader.getInteger();
		setWidths(new ArrayList<Integer>());
		for(int i = 0; i < size; i++) {
			getWidths().add(reader.getInteger());
		}
	}

	private void readColumnTypesJson(IReader reader) throws IOException {
		setColumnTypes(reader.<String>getList(JsonProperties.COLUMN_TYPES));
		setWidths(reader.<Integer>getList(JsonProperties.WIDTHS));
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	private void readCommonValues(IReader reader) throws IOException {
		String firstRow = reader.getString();
		int separatorIndex = firstRow.lastIndexOf(',');
		setName(firstRow.substring(0, separatorIndex));
		setMandantCnr(firstRow.substring(separatorIndex + 1).trim());
		setFixedWidth(reader.getBoolean());
		setSeparator(reader.getString());
		setFromRow(reader.getInteger());
		setStklIId(reader.getInteger());
	}
	
	/**
	 * @param reader
	 * @throws IOException
	 */
	private void readCommonValuesJson(IReader reader) throws IOException {
		setName(reader.getString(JsonProperties.NAME));
		setMandantCnr(reader.getString(JsonProperties.MANDANT_CNR));
		setFixedWidth(reader.getBoolean(JsonProperties.FIXED_WIDTH));
		setSeparator(reader.getString(JsonProperties.SEPARATOR));
		setFromRow(reader.getInteger(JsonProperties.FROM_ROW));
		setStklIId(reader.getInteger(JsonProperties.STKL_IID));
		setWithHiddenArticles(reader.getBoolean(JsonProperties.WITH_HIDDEN_ARTICLES));
	}

	/**
	 * Schreibt die Spezifikation in den StringWriter
	 * 
	 * @return JsonString
	 * @throws IOException 
	 */
	public String writeString() throws IOException {
		JsonWriter writer = new JsonWriter();
		writeCommonValues(writer);
		writeIndividualValues(writer);
		writeColumnsTypes(writer);
		
		return writer.getJsonString();
	}

	/**
	 * Schreibt spezifikationsartspezifische Variablen in die Spezifikation
	 * 
	 * @param writer
	 */
	protected abstract void writeIndividualValues(JsonWriter writer);

	/**
	 * @param sw
	 */
	private void writeColumnsTypes(JsonWriter writer) {
		writer.putList(JsonProperties.COLUMN_TYPES, getColumnTypes());
		writer.putList(JsonProperties.WIDTHS, getWidths());
	}

	/**
	 * @param sw
	 */
	private void writeCommonValues(JsonWriter writer) {
		writer.putString(JsonProperties.NAME, getName());
		writer.putString(JsonProperties.MANDANT_CNR, getMandantCnr());
		writer.putBoolean(JsonProperties.FIXED_WIDTH, isFixedWidth());
		writer.putString(JsonProperties.SEPARATOR, getSeparator());
		writer.putInteger(JsonProperties.FROM_ROW, getFromRow());
		writer.putInteger(JsonProperties.STKL_IID, getStklIId());
		writer.putBoolean(JsonProperties.WITH_HIDDEN_ARTICLES, isWithHiddenArticles());
	}

	public StklImportSpezifikation getDeepCopy() {
		
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream(5 * 1024);
		ObjectOutputStream objectOutStream = null;
		ByteArrayInputStream byteInStream = null;
		ObjectInputStream objectInStream = null;
		try {
			objectOutStream = new ObjectOutputStream(byteOutStream);
			objectOutStream.writeObject(this);
			objectOutStream.flush();

			byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
			objectInStream = new ObjectInputStream(byteInStream);

			StklImportSpezifikation returnPath = (StklImportSpezifikation) objectInStream.readObject();
			objectInStream.close();
			return returnPath;

		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} finally {
			try {
				if (objectOutStream != null) {
					objectOutStream.flush();
					objectOutStream.close();
				}
				if (byteOutStream != null) {
					byteOutStream.flush();
					byteOutStream.close();
				}
				if (byteInStream != null) {
					byteInStream.close();
				}
				if (objectInStream != null) {
					objectInStream.close();
				}
			} catch (IOException e) {
			}
		}

	}
	
	/**
	 * Liefert den CGruppe String f&uuml;r den betreffenden Spezifikationstyp
	 * f&uuml;r die Eintragung in die KeyValue Tabelle
	 * 
	 * @param stklTyp
	 * @return cgruppe
	 */
	public static String getKeyValueCGruppeByStklTyp(int stklTyp) {
		return keyValueCGruppe.get(stklTyp);
	}
	
	/**
	 * Liefert den CGruppe String der aktuellen Spezifikation
	 * f&uuml;r die Eintragung in die KeyValue Tabelle
	 * 
	 * @return cgruppe
	 */
	public abstract String getKeyValueCGruppe();
	
	public String getKeyValueCKey() {
		return getName() + ", " + getMandantCnr();
	}

	/**
	 * L&ouml;scht die f&uuml;r das Mapping der Artikel zu Kunden- oder 
	 * Lieferantenartikelnummern verwendete Spalte. Wird angewendet, wenn 
	 * kein Kunde oder Lieferant f&uuml;r die verwendete St&uuml;ckliste
	 * definiert ist.
	 */
	public abstract void removeMappingColumnType();
	
	/**
	 * Gibt Info, ob die St&uuml;ckliste einen Einkaufs- oder Verkaufsbezug
	 * hat.
	 * 
	 * @return true, wenn die St&uuml;ckliste einen Einkaufsbezug (Lieferant) 
	 * hat und false, wenn die St&uuml;ckliste einen Verkaufsbezug (Kunde) hat.
	 */
	public abstract boolean isStuecklisteMitBezugVerkauf();
}
