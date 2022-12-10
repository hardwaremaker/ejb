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
package com.lp.server.util.fastlanereader.service.query;

/**
 * <p></p>
 * <p>Copright Logistik Pur Software GmbH (c) 2004-2007</p>
 *
 * <p>Erstellungsdatum dd.mm.yy</p>
 *
 * @author Uli Walch
 *
 * @version 1.0
 */

import java.io.Serializable;
import java.math.BigDecimal;

public class FilterKriterium extends QueryKriterium implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Menge aller moeglichen Vergleichsoperatoren; nicht jeder Operator kann auf
	// jeden Typ angewendet werden
	public static final String OPERATOR_EQUAL = "=";
	public static final String OPERATOR_LIKE = "LIKE";
	public static final String OPERATOR_GT = ">";
	public static final String OPERATOR_LT = "<";
	public static final String OPERATOR_GTE = ">=";
	public static final String OPERATOR_LTE = "<=";
	public static final String OPERATOR_NOT_EQUAL = "<>";
	public static final String OPERATOR_IS = "IS";
	public static final String OPERATOR_IN = "IN";
	public static final String OPERATOR_NOT_IN = "NOT IN";
	public static final String OPERATOR_NULL = "NULL";
	public static final String OPERATOR_NOT_NULL = "NOT NULL";
	public static final String OPERATOR_NOT = "NOT";
	public static final String OPERATOR_NOT_LIKE = "NOT LIKE";
	public static final String OPENING_BRACKET = "(";
	public static final String CLOSING_BRACKET = ")";

	// Menge aller moeglichen boolschen Operatoren
	public static final String BOOLOPERATOR_AND = "AND";
	public static final String BOOLOPERATOR_OR = "OR";

	public boolean wrapWithSingleQuotes = false;

	public String operator;

	public int iTyp = TYP_STRING;

	public static final int TYP_STRING = 1;
	public static final int TYP_DECIMAL = 2;
	public static final int TYP_DATE = 3;
	public static final int TYP_BOOLEAN = 4;
	public static final int TYP_COMBOBOX = 5;

	public FilterKriterium(String pName, boolean pKrit, String pValue, String pOperator, boolean bIgnoreCaseI) {
		super(pName, pKrit, pValue, bIgnoreCaseI);

		this.operator = pOperator;
	}

	public FilterKriterium(String pName, boolean pKrit, String pValue, String pOperator, boolean bIgnoreCaseI,
			int iTyp) {
		super(pName, pKrit, pValue, bIgnoreCaseI);
		this.iTyp = iTyp;
		this.operator = pOperator;
	}

	public FilterKriterium(String pName, boolean pKrit, String pValue, String pOperator, boolean bIgnoreCaseI,
			boolean bWrapWithSinlgeQuotes) {
		super(pName, pKrit, pValue, bIgnoreCaseI);

		this.wrapWithSingleQuotes = bWrapWithSinlgeQuotes;
		this.operator = pOperator;
	}

	public FilterKriterium(String pName, boolean pKrit, BigDecimal bdValue, String pOperator, boolean bIgnoreCaseI) {
		super(pName, pKrit, bdValue, bIgnoreCaseI);

		this.operator = pOperator;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer("");
		buff.append(super.toString()).append(" operator is ").append(operator);
		return buff.toString();
	}

	public static String arrayToString(FilterKriterium[] array) {
		StringBuffer buff = new StringBuffer("\nFilterkriterien");

		if (array != null && array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				buff.append("\n  ").append(array[i].toString());
			}
		}

		return buff.toString();
	}

	public String formatFilterKriterium(String sPraefixI) {
		StringBuffer buff = new StringBuffer(sPraefixI);

		buff.append(" ").append(kritName);

		if (!value.equals(Boolean.TRUE.toString()) && !value.equals(Boolean.FALSE.toString())) {
			buff.append(" ").append(operator).append(" ").append(value);
		}

		return buff.toString();
	}

	public void wrapWithSingleQuotes() {
		if (wrapWithSingleQuotes) {
			StringBuffer sbWrappedValue = new StringBuffer("'").append(value).append("'");

			value = sbWrappedValue.toString();
		}
	}

	private boolean isOperatorInverse(String operator) {
		return operator.startsWith("NOT") || operator.equals(OPERATOR_NOT_EQUAL);
	}

	/**
	 * SP8427 Formatiert eine Query f&uuml;r dieses Filterkriterium, allerdings mit
	 * mehreren m&ouml;glichen Spalten. Verkn&uuml;pft beide Bedingungen mit OR bei
	 * normalen, bzw. AND bei invertierten Operatoren (wie NOT LIKE) <br>
	 * Falls dieses Kriterium ignoreCase ist, werden alle lower_case verglichen
	 * 
	 * @param columnNames Liste der Namen der Columns
	 * @param ignoreNull  falls true, dann wird null in Spalten ignoriert. Bei
	 *                    inversen Operatoren wird dazu &uuml;berall ein OR IS NULL
	 *                    hinzugef&uuml;gt
	 * @return
	 */
	public String getQueryWithMultipleColumns(boolean ignoreNull, String... columnNames) {
		if (columnNames.length == 0)
			return formatFilterKriterium("");
		StringBuilder sb = new StringBuilder();
		sb.append((" ( "));
		boolean first = true;
		boolean inverse = isOperatorInverse(operator);
		for (String s : columnNames) {
			if (!first) {
				sb.append(inverse ? " AND " : " OR ");
			}
			first = false;
			if (inverse) {
				sb.append("(");
			}
			sb.append(formatQueryForColumn(inverse, s));
			if (inverse) {
				sb.append(" OR ");
				sb.append(s);
				sb.append(" IS NULL");
				sb.append(")");
			}
		}

		sb.append(") ");

		return sb.toString();
	}

	private CharSequence formatQueryForColumn(boolean inverse, String s) {
		StringBuilder sb = new StringBuilder();
		if (this.isBIgnoreCase()) {
			sb.append("lower(");
		}
		sb.append(s);
		if (this.isBIgnoreCase()) {
			sb.append(")");
		}
		sb.append(" ");
		sb.append(operator);
		sb.append(" ");
		if (isBIgnoreCase()) {
			sb.append(value.toLowerCase());
		} else {
			sb.append(value);
		}
		return sb;
	}

	public static FilterKriterium[] concat(FilterKriterium[] a, FilterKriterium[] b) {
		if (a == null)
			throw new IllegalArgumentException("a == null");
		if (b == null)
			throw new IllegalArgumentException("b == null");

		int lenA = a.length;
		int lenB = b.length;
		FilterKriterium[] c = new FilterKriterium[lenA + lenB];

		System.arraycopy(a, 0, c, 0, lenA);
		System.arraycopy(b, 0, c, lenA, lenB);
		return c;
	}
}
