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
package com.lp.server.finanz.ejbfac;

public class KontoQueryBuilder {

	private static String OHNE_MITLAUFENDE_KONTEN = " ({ALIASKONTO}.i_id NOT IN" +
			"(" +
				"SELECT stk.konto_i_id_forderungen FROM FLRSteuerkategorie stk WHERE stk.konto_i_id_forderungen IS NOT NULL" +
			") " +
			"AND {ALIASKONTO}.i_id NOT IN" +
			"(" +
				"SELECT stk.konto_i_id_verbindlichkeiten FROM FLRSteuerkategorie stk WHERE stk.konto_i_id_verbindlichkeiten IS NOT NULL" +
			")) ";

	private static String OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZ = " ({ALIASKONTO}.i_id NOT IN" +
			"(SELECT stkk.kontoiidek FROM FLRSteuerkategoriekonto stkk " +
			"WHERE stkk.flrmwstsatzbez.i_id != {ALIASMWSTBEZIID} " +
			"AND stkk.kontoiidek IS NOT NULL) "+
		"AND {ALIASKONTO}.i_id NOT IN" +
			"(SELECT stkk.kontoiidvk FROM FLRSteuerkategoriekonto stkk " +
			"WHERE stkk.flrmwstsatzbez.i_id != {ALIASMWSTBEZIID} " +
			"AND stkk.kontoiidvk IS NOT NULL)) ";

	public static String buildOhneMitlaufendeKonten(String flrkontoName) {
		return OHNE_MITLAUFENDE_KONTEN.replaceAll("\\{ALIASKONTO\\}", flrkontoName);
	}

	public static String buildOhneUstVstKontenAusserMwstsatzBez(String flrkontoName, Integer mwstSatzBezIId) {
		return OHNE_UST_VST_KONTEN_AUSSER_MWSTSATZ.replaceAll("\\{ALIASKONTO\\}", flrkontoName)
				.replaceAll("\\{ALIASMWSTBEZIID\\}", mwstSatzBezIId.toString());
	}

	public static String findKonto(String searchString) {
		return " (konto.c_nr LIKE " + searchString + " OR lower(konto.c_bez) LIKE " + searchString + ")";
	}

	public static String showKontenMitBuchungen(String aktuellesGeschaeftsjahr) {
		if (aktuellesGeschaeftsjahr != null) {
			return " flr_buchung.geschaeftsjahr_i_geschaeftsjahr = " + aktuellesGeschaeftsjahr
					+ " AND flr_buchung.t_storniert IS NULL";
		} else {
			return " flr_buchung.geschaeftsjahr_i_geschaeftsjahr IS NOT NULL"
					+ " AND flr_buchung.t_storniert IS NULL";
		}
	}
}
