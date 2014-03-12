/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.artikel.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.lp.server.util.CsvimportDto;
import com.lp.util.Helper;
import com.lp.util.HelperImport;

public class ArtikellieferantImportDto extends ArtikellieferantDto implements
		CsvimportDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1053535241310163408L;

	public final static int _CSV_IDENT = 0;
	public final static int _CSV_LIEFERANTENARTIKELNUMMER = 1;
	public final static int _CSV_LIEFERANTENARTIKELBEZEICHNUNG = 2;
	public final static int _CSV_EINZELPREIS = 3;
	public final static int _CSV_RABATT = 4;
	public final static int _CSV_NETTOPREIS = 5;
	public final static int _CSV_GUELTIGAB = 6;
	public final static int _CSV_MENGE = 7;
	public final static int _CSV_MINBESTELLMENGE = 8;
	public final static int _CSV_VPE = 9;
	public final static int _CSV_RABATTGRUPPE = 10;
	public final static int _CSV_HERSTELLERVERWENDEN = 11;
	public final static int _CSV_WEBSHOP = 12;
	public final static int _CSV_VKPREISBASIS = 13;
	public final static int _CSV_VKPREISBASIS_GUELTIGAB = 14;
	public final static int _CSV_STAFFEL_MENGE = 15;
	public final static int _CSV_STAFFEL_RABATT = 16;
	public final static int _CSV_STAFFEL_NETTOPREIS = 17;
	public final static int _CSV_STAFFEL_GUELTIGAB = 18;

	public BigDecimal vkPreisbasis = null;
	public Timestamp vkPreisbasisGuelitgab = null;
	private final static double RUNDUNGSTOLERANZ = 0.0002;

	private ArtikellieferantstaffelDto alStaffelDto = null;

	public ArtikellieferantstaffelDto getArtikellieferantstaffelDto() {
		return alStaffelDto;
	}

	private StringBuffer err = new StringBuffer();

	public String getErrors() {
		return err.toString();
	}

	private boolean error = false;

	public boolean isError() {
		return error;
	}

	public ArtikellieferantImportDto(Integer artikelIId, Integer lieferantIId,
			String mandantCNr, Integer personalId, String[] csv, int zeile) {
		HelperImport hi = new HelperImport();
		if (csv.length >= _CSV_VKPREISBASIS) {
			super.setArtikelIId(artikelIId);
			super.setBHerstellerbez(new Integer(csv[_CSV_HERSTELLERVERWENDEN].isEmpty() ? "0" : csv[_CSV_HERSTELLERVERWENDEN])
					.shortValue());
			super.setBRabattbehalten(csv[_CSV_RABATT].length() > 0 ? (short) 1
					: (short) 0);
			super.setBWebshop(new Integer(csv[_CSV_WEBSHOP].isEmpty() ? "0" : csv[_CSV_WEBSHOP]).shortValue());
			super.setCArtikelnrlieferant(hi.getStringCsv(
					csv[_CSV_LIEFERANTENARTIKELNUMMER], zeile,
					"Artikelnummer Lieferant", 40, true, false));
			super.setCBezbeilieferant(hi.getStringCsv(
					csv[_CSV_LIEFERANTENARTIKELBEZEICHNUNG], zeile,
					"Bezeichnung Lieferant", 80, true, false));
			super.setCRabattgruppe(hi.getStringCsv(csv[_CSV_RABATTGRUPPE],
					zeile, "Rabattgruppe", 5, true, false));
			super.setFMindestbestelmenge(hi.getDoubleCsv(
					csv[_CSV_MINBESTELLMENGE], zeile, "Mindestbestellmenge",
					true));

			// super.setNFixkosten(nFixkosten);
			// super.setFStandardmenge(fStandardmenge);
			// super.setIId(iId);
			// super.setISort(iSort);
			// super.setIWiederbeschaffungszeit(iWiederbeschaffungszeit);
			// super.setLieferantDto(lieferantDto);
			super.setLieferantIId(lieferantIId);
			super.setMandantCNr(mandantCNr);

			if (csv[_CSV_NETTOPREIS] == null
					|| csv[_CSV_NETTOPREIS].length() == 0) {
				super.setNEinzelpreis(hi.getBigDecimalCsv(
						csv[_CSV_EINZELPREIS], zeile, "Einzelpreis", false));
				super.setFRabatt(hi.getDoubleCsv(csv[_CSV_RABATT], zeile,
						"Rabatt", false));
				if (super.getNEinzelpreis() != null
						&& super.getFRabatt() != null) {
					BigDecimal prozentwert = Helper.getProzentWert(
							super.getNEinzelpreis(),
							new BigDecimal(super.getFRabatt()), 4);
					super.setNNettopreis(super.getNEinzelpreis().subtract(
							prozentwert));
					super.setBRabattbehalten(Helper.boolean2Short(true));
				}
			} else if (csv[_CSV_RABATT] == null
					|| csv[_CSV_RABATT].length() == 0) {

				super.setNEinzelpreis(hi.getBigDecimalCsv(
						csv[_CSV_EINZELPREIS], zeile, "Einzelpreis", false));
				super.setNNettopreis(hi.getBigDecimalCsv(csv[_CSV_NETTOPREIS],
						zeile, "Nettopreis", false));
				super.setBRabattbehalten(Helper.boolean2Short(false));

				if (super.getNEinzelpreis() != null
						&& super.getNNettopreis() != null) {
					super.setFRabatt(Helper.getProzentsatz(super
							.getNEinzelpreis(), super.getNEinzelpreis()
							.subtract(super.getNNettopreis()), 6));
				}

			} else {

				super.setNEinzelpreis(hi.getBigDecimalCsv(
						csv[_CSV_EINZELPREIS], zeile, "Einzelpreis", false));
				super.setFRabatt(hi.getDoubleCsv(csv[_CSV_RABATT], zeile,
						"Rabatt", false));
				super.setNNettopreis(hi.getBigDecimalCsv(csv[_CSV_NETTOPREIS],
						zeile, "Nettopreis", false));
			}

			super.setNVerpackungseinheit(hi.getBigDecimalCsv(csv[_CSV_VPE],
					zeile, "Verpackungseinheit", true));
			super.setFStandardmenge(hi.getDoubleCsv(csv[_CSV_MENGE], zeile,
					"Menge", true));
			super.setPersonalIIdAendern(personalId);
			super.setTAendern(new Timestamp(System.currentTimeMillis()));
			super.setTPreisgueltigab(hi.getTimestampCsv(csv[_CSV_GUELTIGAB],
					zeile, "Preis g\u00FCltigab", false));
			// super.setTPreisgueltigbis(tPreisgueltigbis);

			if (csv.length > _CSV_STAFFEL_MENGE) {

				if ((csv[_CSV_STAFFEL_MENGE] == null || csv[_CSV_STAFFEL_MENGE]
						.length() == 0)
						&& (csv[_CSV_STAFFEL_RABATT] == null || csv[_CSV_STAFFEL_RABATT]
								.length() == 0)
						&& (csv[_CSV_STAFFEL_NETTOPREIS] == null || csv[_CSV_STAFFEL_NETTOPREIS]
								.length() == 0)
						&& (csv[_CSV_STAFFEL_GUELTIGAB] == null || csv[_CSV_STAFFEL_GUELTIGAB]
								.length() == 0)) {

				} else {
					alStaffelDto = new ArtikellieferantstaffelDto();
					alStaffelDto.setNMenge(hi.getBigDecimalCsv(
							csv[_CSV_STAFFEL_MENGE], zeile, "Staffelmenge",
							false));
					alStaffelDto.setFRabatt(hi.getDoubleCsv(
							csv[_CSV_STAFFEL_RABATT], zeile, "Staffelrabatt",
							false));
					alStaffelDto.setNNettopreis(hi.getBigDecimalCsv(
							csv[_CSV_STAFFEL_NETTOPREIS], zeile,
							"Staffelnettopreis", false));
					alStaffelDto.setTPreisgueltigab(hi.getTimestampCsv(
							csv[_CSV_STAFFEL_GUELTIGAB], zeile,
							"Staffelpreis g\u00FCltigab", false));
				}

			}

			if (csv.length > _CSV_VKPREISBASIS_GUELTIGAB) {
				vkPreisbasis = hi.getBigDecimalCsv(csv[_CSV_VKPREISBASIS],
						zeile, "VKPreibasis", true);
				vkPreisbasisGuelitgab = hi.getTimestampCsv(
						csv[_CSV_VKPREISBASIS_GUELTIGAB], zeile,
						"VKpreisbasis g\u00FCltigab", true);

			}

			if (hi.isError()) {
				error = hi.isError();
				err.append(hi.getErrors());
			}
			validate(zeile);
		} else {
			error = true;
			err.append("Zeile:" + zeile + " Feldanzahl falsch (" + csv.length
					+ "/" + _CSV_STAFFEL_MENGE + ")\r\n");
		}
	}

	public void validate(int zeile) {
		try {
			if (super.getFRabatt().floatValue() != 0) {
				BigDecimal n = super.getNEinzelpreis()
						.subtract(
								Helper.getProzentWert(super.getNEinzelpreis(),
										new BigDecimal(super.getFRabatt()
												.doubleValue()), 4));
				n = n.subtract(super.getNNettopreis());
				if (n.abs().floatValue() > RUNDUNGSTOLERANZ) {
					err.append("Zeile:" + zeile + " Betr\u00E4ge stimmen nicht\n\r");
					error = true;
				}
			}
		} catch (Exception e) {
			err.append("Zeile:" + zeile + " Fehler Betragpr\u00FCfung: "
					+ e.getMessage() + "\n\r");
			error = true;
		}
		if (alStaffelDto != null) {
			try {
				if (alStaffelDto.getFRabatt().floatValue() != 0) {
					BigDecimal n = super.getNEinzelpreis().subtract(
							Helper.getProzentWert(super.getNEinzelpreis(),
									new BigDecimal(alStaffelDto.getFRabatt()
											.doubleValue()), 4));
					n = n.subtract(alStaffelDto.getNNettopreis());
					if (n.abs().floatValue() > RUNDUNGSTOLERANZ) {
						err.append("Zeile:" + zeile
								+ " Staffelbetr\u00E4ge stimmen nicht\n\r");
						error = true;
					}
				}
			} catch (Exception e) {
				err.append("Zeile:" + zeile + " Fehler Betragpr\u00FCfung: "
						+ e.getMessage() + "\n\r");
				error = true;
			}
		}
	}

}
