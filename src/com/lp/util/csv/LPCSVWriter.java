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
package com.lp.util.csv;

/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.lp.util.Helper;

/**
 * A very simple CSV writer released under a commercial-friendly license.
 * 
 * @author Glen Smith
 * 
 */
public class LPCSVWriter implements AutoCloseable {

	private Writer rawWriter;

	private PrintWriter pw;

	private char separator;

	private char quotechar;

	private String lineEnd;

	/** The character used for escaping quotes. */
	public static final char ESCAPE_CHARACTER = '"';

	/** The default separator to use if none is supplied to the constructor. */
	public static final char DEFAULT_SEPARATOR = ',';

	/**
	 * The default quote character to use if none is supplied to the
	 * constructor.
	 */
	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	/** The quote constant to use when you wish to suppress all quoting. */
	public static final char NO_QUOTE_CHARACTER = '\u0000';

	/** Default line terminator uses platform encoding. */
	public static final String DEFAULT_LINE_END = "\n";

	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat(
			"dd-MMM-yyyy HH:mm:ss");

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"dd-MMM-yyyy");

	/**
	 * Constructs CSVWriter using a comma for the separator.
	 * 
	 * @param writer
	 *            the writer to an underlying CSV source.
	 */
	public LPCSVWriter(Writer writer) {
		this(writer, DEFAULT_SEPARATOR);
	}

	/**
	 * Constructs CSVWriter with supplied separator.
	 * 
	 * @param writer
	 *            the writer to an underlying CSV source.
	 * @param separator
	 *            the delimiter to use for separating entries.
	 */
	public LPCSVWriter(Writer writer, char separator) {
		this(writer, separator, DEFAULT_QUOTE_CHARACTER);
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 * 
	 * @param writer
	 *            the writer to an underlying CSV source.
	 * @param separator
	 *            the delimiter to use for separating entries
	 * @param quotechar
	 *            the character to use for quoted elements
	 */
	public LPCSVWriter(Writer writer, char separator, char quotechar) {
		this(writer, separator, quotechar, "\r\n");
	}

	/**
	 * Constructs CSVWriter with supplied separator and quote char.
	 * 
	 * @param writer
	 *            the writer to an underlying CSV source.
	 * @param separator
	 *            the delimiter to use for separating entries
	 * @param quotechar
	 *            the character to use for quoted elements
	 * @param lineEnd
	 *            the line feed terminator to use
	 */
	public LPCSVWriter(Writer writer, char separator, char quotechar,
			String lineEnd) {
		this.rawWriter = writer;
		this.pw = new PrintWriter(writer);
		this.separator = separator;
		this.quotechar = quotechar;
		this.lineEnd = lineEnd;
	}

	/**
	 * Writes the entire list to a CSV file. The list is assumed to be a
	 * String[]
	 * 
	 * @param allLines
	 *            a List of String[], with each String[] representing a line of
	 *            the file.
	 */
	public void writeAll(List<?> allLines) {

		for (Iterator<?> iter = allLines.iterator(); iter.hasNext();) {
			String[] nextLine = (String[]) iter.next();
			writeNext(nextLine);
		}

	}

	protected void writeColumnNames(ResultSetMetaData metadata)
			throws SQLException {

		int columnCount = metadata.getColumnCount();

		String[] nextLine = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			nextLine[i] = metadata.getColumnName(i + 1);
		}
		writeNext(nextLine);
	}

	/**
	 * Writes the entire ResultSet to a CSV file.
	 * 
	 * The caller is responsible for closing the ResultSet.
	 * 
	 * @param rs
	 *            the recordset to write
	 * @param includeColumnNames
	 *            true if you want column names in the output, false otherwise
	 * 
	 */
	public void writeAll(java.sql.ResultSet rs, boolean includeColumnNames)
			throws SQLException, IOException {

		ResultSetMetaData metadata = rs.getMetaData();

		if (includeColumnNames) {
			writeColumnNames(metadata);
		}

		int columnCount = metadata.getColumnCount();

		while (rs.next()) {
			String[] nextLine = new String[columnCount];

			for (int i = 0; i < columnCount; i++) {
				nextLine[i] = getColumnValue(rs, metadata.getColumnType(i + 1),
						i + 1);
			}

			writeNext(nextLine);
		}
	}

	private static String getColumnValue(ResultSet rs, int colType, int colIndex)
			throws SQLException, IOException {

		String value = "";

		switch (colType) {
		case Types.BIT:
			Object bit = rs.getObject(colIndex);
			if (bit != null) {
				value = String.valueOf(bit);
			}
			break;
		case Types.BOOLEAN:
			boolean b = rs.getBoolean(colIndex);
			if (!rs.wasNull()) {
				value = Boolean.valueOf(b).toString();
			}
			break;
		case Types.CLOB:
			Clob c = rs.getClob(colIndex);
			if (c != null) {
				value = read(c);
			}
			break;
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.REAL:
		case Types.NUMERIC:
			BigDecimal bd = rs.getBigDecimal(colIndex);
			if (bd != null) {
				value = "" + bd.doubleValue();
			}
			break;
		case Types.INTEGER:
		case Types.TINYINT:
		case Types.SMALLINT:
			int intValue = rs.getInt(colIndex);
			if (!rs.wasNull()) {
				value = "" + intValue;
			}
			break;
		case Types.JAVA_OBJECT:
			Object obj = rs.getObject(colIndex);
			if (obj != null) {
				value = String.valueOf(obj);
			}
			break;
		case Types.DATE:
			java.sql.Date date = rs.getDate(colIndex);
			if (date != null) {
				value = DATE_FORMATTER.format(date);
				;
			}
			break;
		case Types.TIME:
			Time t = rs.getTime(colIndex);
			if (t != null) {
				value = t.toString();
			}
			break;
		case Types.TIMESTAMP:
			Timestamp tstamp = rs.getTimestamp(colIndex);
			if (tstamp != null) {
				value = TIMESTAMP_FORMATTER.format(tstamp);
			}
			break;
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
		case Types.CHAR:
			value = rs.getString(colIndex);
			break;
		default:
			value = "";
		}

		if (value == null) {
			value = "";
		}

		return value;

	}

	private static String read(Clob c) throws SQLException, IOException {
		StringBuffer sb = new StringBuffer((int) c.length());
		Reader r = c.getCharacterStream();
		char[] cbuf = new char[2048];
		int n = 0;
		while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
			if (n > 0) {
				sb.append(cbuf, 0, n);
			}
		}
		return sb.toString();
	}

	/**
	 * Writes the next line to the file.
	 * 
	 * @param nextLine
	 *            a string array with each comma-separated element as a separate
	 *            entry.
	 */
	public void writeNext(String[] nextLine) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nextLine.length; i++) {

			if (i != 0) {
				sb.append(separator);
			}

			String nextElement = nextLine[i];
			if (nextElement == null)
				continue;
			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);
			for (int j = 0; j < nextElement.length(); j++) {
				char nextChar = nextElement.charAt(j);
				if (nextChar == quotechar) {
					sb.append(ESCAPE_CHARACTER).append(nextChar);
				} else if (nextChar == ESCAPE_CHARACTER) {
					sb.append(ESCAPE_CHARACTER).append(nextChar);
				} else {
					sb.append(nextChar);
				}
			}
			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);
		}

		sb.append(lineEnd);
		pw.write(sb.toString());

	}

	public void writeNext(Object[] nextLine, Locale locale) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nextLine.length; i++) {

			if (i != 0) {
				sb.append(separator);
			}

			Object nextObject = nextLine[i];

			if (nextObject == null)
				continue;

			if (nextObject instanceof java.lang.String
					&& quotechar != NO_QUOTE_CHARACTER) {
				sb.append(quotechar);
			}

			String nextElement = "";

			if (nextObject instanceof Number) {
				nextElement = Helper.formatZahl((Number) nextObject, locale);
			} else if (nextObject instanceof java.sql.Timestamp) {
				try {
					nextElement = Helper.formatTimestamp(
							(java.sql.Timestamp) nextObject, locale);
				} catch (Throwable e1) {
					//
				}
			} else if (nextObject instanceof java.util.Date) {
				try {
					nextElement = Helper.formatDatum(
							(java.util.Date) nextObject, locale);
				} catch (Throwable e1) {
					//
				}
			}else {
				nextElement=nextObject.toString();
			}
			
			nextElement = nextElement == null ? "" : nextElement;
			for (int j = 0; j < nextElement.length(); j++) {
				char nextChar = nextElement.charAt(j);
				if (nextChar == quotechar) {
					sb.append(ESCAPE_CHARACTER).append(nextChar);
				} else if (nextChar == ESCAPE_CHARACTER) {
					sb.append(ESCAPE_CHARACTER).append(nextChar);
				} else {
					sb.append(nextChar);
				}
			}
			if (nextObject instanceof java.lang.String
					&& quotechar != NO_QUOTE_CHARACTER) {
				sb.append(quotechar);
			}

		}

		sb.append(lineEnd);
		pw.write(sb.toString());

	}

	/**
	 * Close the underlying stream writer flushing any buffered content.
	 * 
	 * @throws IOException
	 *             if bad things happen
	 * 
	 */
	public void close() throws IOException {
		pw.flush();
		pw.close();
		rawWriter.close();
	}
}
