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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
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
	private List<String> columnTypes;
	private String separator;
	private int fromRow = 1;
	private int stklIId;
	protected List<String> availableColumnTypes;
	
	public static final String UNDEFINED = "<undefined>";
	public static final String KUNDENARTIKELNUMMER = "Kundenartikelnummer";
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
	public static final String REFERENZBEMERKUNG = "Referenzbemerkung";
	
	protected static final String EXT_SEPARATOR = "\r" ;
	
	public static class SpezifikationsTyp {		
		public static final int FERTIGUNGSSTKL_SPEZ = 1;
		public static final int ANGEBOTSSTKL_SPEZ = 2;
		public static final int EINKAUFSANGEBOTSSTKL_SPEZ = 3;
	}
	
	private static Map<Integer, String> keyValueCGruppe = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -5655185103585548412L;
		{
			put(SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ, SystemServicesFac.KEYVALUE_STKL_IMPORT_SPEZ);
			put(SpezifikationsTyp.ANGEBOTSSTKL_SPEZ, SystemServicesFac.KEYVALUE_AGSTKL_IMPORT_SPEZ);
			put(SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ, SystemServicesFac.KEYVALUE_EINKAUFSAGSTKL_IMPORT_SPEZ);
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
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		BufferedReader br = new BufferedReader(new StringReader(in.readUTF()));
		readString(br);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		StringWriter sw = new StringWriter();
		writeString(sw);
		out.writeUTF(sw.toString());
	}

	/**
	 * Holt die Spezifikation aus dem BufferedReader und setzt die Variablen
	 * 
	 * @param br
	 * @throws IOException
	 */
	public void readString(BufferedReader br) throws IOException {
		readCommonValues(br);
		readIndividualValues(br);
		readColumnTypes(br);
	}

	/**
	 * Liest spezifikationsartspezifische Variablen aus der Spezifikation
	 * 
	 * @param br
	 * @throws IOException
	 */
	protected abstract void readIndividualValues(BufferedReader br) throws IOException;

	/**
	 * @param br
	 * @throws IOException
	 */
	private void readColumnTypes(BufferedReader br) throws IOException {
		int size = extGetInteger(br);
		setColumnTypes(new ArrayList<String>());
		for(int i = 0; i < size; i++) {
			getColumnTypes().add(extGetString(br));
		}
		size = extGetInteger(br);
		setWidths(new ArrayList<Integer>());
		for(int i = 0; i < size; i++) {
			getWidths().add(extGetInteger(br));
		}
	}

	/**
	 * @param br
	 * @throws IOException
	 */
	private void readCommonValues(BufferedReader br) throws IOException {
		String firstRow = extGetString(br);
		int separatorIndex = firstRow.lastIndexOf(',');
		setName(firstRow.substring(0, separatorIndex));
		setMandantCnr(firstRow.substring(separatorIndex + 1).trim());
		setFixedWidth(extGetBoolean(br));
		setSeparator(extGetString(br));
		setFromRow(extGetInteger(br));
		setStklIId(extGetInteger(br));
	}
	
	/**
	 * Schreibt die Spezifikation in den StringWriter
	 * 
	 * @param sw
	 */
	public void writeString(StringWriter sw) {
		writeCommonValues(sw);
		writeIndividualValues(sw);
		writeColumnsTypes(sw);
	}

	/**
	 * Schreibt spezifikationsartspezifische Variablen in die Spezifikation
	 * 
	 * @param sw
	 */
	protected abstract void writeIndividualValues(StringWriter sw);

	/**
	 * @param sw
	 */
	private void writeColumnsTypes(StringWriter sw) {
		extPutSize(getColumnTypes(), sw);
		if(getColumnTypes() != null) {
			for(String type : getColumnTypes()) {
				extPut(type, sw);
			}
		}
		
		extPutSize(getWidths(), sw);
		if(getWidths() != null) {
			for(Integer width : getWidths()) {
				extPut(width, sw) ;
			}
		}
	}

	/**
	 * @param sw
	 */
	private void writeCommonValues(StringWriter sw) {
		extPut(getKeyValueCKey(), sw) ;
		extPut(isFixedWidth(), sw) ;
		extPut(getSeparator(), sw) ;
		extPut(getFromRow(), sw);
		extPut(getStklIId(), sw);
	}

	protected String extGetString(BufferedReader br) throws IOException {
		return br.readLine();
	}

	protected Integer extGetInteger(BufferedReader br) throws IOException {
		return Integer.parseInt(br.readLine());
	}
	
	protected boolean extGetBoolean(BufferedReader br) throws IOException {
		return "1".equals(br.readLine());
	}
	
	protected void newLine(StringWriter sw) {
		sw.write(EXT_SEPARATOR);
	}
	
	protected void extPut(String value, StringWriter sw) {
		sw.write(value == null ? "" : value) ;
		newLine(sw) ;
	}

	protected void extPut(Integer value, StringWriter sw) {
		sw.write(value == null ? "0" : value.toString()) ;
		newLine(sw);
	}

	protected void extPut(boolean value, StringWriter sw) {
		sw.write(value ? "1" : "0") ;
		newLine(sw);
	}
	
	protected void extPutSize(Collection<?> c, StringWriter sw) {
		extPut(c == null ? 0 : c.size(), sw) ;
	}

	public StklImportSpezifikation getDeepCopy() {
		
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream(
						5 * 1024);
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
	 * L&ouml;scht die Kundenartikelnummer-Spalte von den f&uuml;r das
	 * Mapping der Spalten verf&uuml;gbaren ColumnTypes.
	 * Wird angewendet, wenn kein Kunde f&uuml;r die St&uuml;ckliste
	 * definiert ist.
	 */
	public void removeKundenartikelnrColumnType() {
		availableColumnTypes.remove(KUNDENARTIKELNUMMER);
		int index = columnTypes.indexOf(KUNDENARTIKELNUMMER);
		if(index >= 0) {
			columnTypes.set(index, UNDEFINED);
		}
	}
}
