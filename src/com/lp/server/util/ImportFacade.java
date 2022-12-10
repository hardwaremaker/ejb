package com.lp.server.util;

import java.math.BigDecimal;
import java.util.HashMap;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;

import com.lp.util.Helper;

public class ImportFacade extends Facade {

	protected String fehlerZeileXLSImport = "";

	protected java.util.Date getDateAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getType() == CellType.DATE
							|| c.getType() == CellType.DATE_FORMULA) {

						return ((jxl.DateCell) c).getDate();
					} else {

						fehlerZeileXLSImport += feldname
								+ " muss vom Typ 'Date' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}
	
	protected String getStringAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iLaenge, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						fehlerZeileXLSImport += feldname + " ist zu lang (>"
								+ iLaenge + ") Zeile " + iZeile
								+ new String(CRLFAscii);

					}

					return c.getContents();
				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	protected Short getShortAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c.getType() == CellType.BOOLEAN
						|| c.getType() == CellType.BOOLEAN_FORMULA) {
					boolean b = ((jxl.BooleanCell) c).getValue();
					return Helper.boolean2Short(b);

				} else {

					if (c != null && c.getContents() != null
							&& c.getContents().length() > 0) {

						if (c.getContents().trim().equals("0")) {
							return new Short((short) 0);
						} else if (c.getContents().trim().equals("1")) {
							return new Short((short) 1);
						} else {

							fehlerZeileXLSImport += feldname
									+ " darf nur die Werte 0 bzw. 1 enthalten. Zeile "
									+ iZeile + new String(CRLFAscii);

							return null;
						}

					} else {
						return null;
					}
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	protected BigDecimal getBigDecimalAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER
							|| c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return Helper.rundeKaufmaennisch(new BigDecimal(d),4);

					} else {

						fehlerZeileXLSImport += feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	protected Double getDoubleAusXLS(jxl.Cell[] zeilen,
			HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null
						&& c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER
							|| c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new Double(d);

					} else {

						fehlerZeileXLSImport += feldname
								+ " muss vom Typ 'Zahl' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

}
