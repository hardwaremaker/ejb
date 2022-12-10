package com.lp.server.system.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.server.system.service.JxlImportErgebnis;
import com.lp.util.EJBExceptionLP;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class JxlImportBeanSevices {

	public int FEHLER_ZEILE_ALLGEMEIN = -1;

	private Sheet sheet = null;

	private HashMap<String, Integer> hmVorhandeneSpalten = null;

	public HashMap<String, Integer> getHmVorhandeneSpalten() {
		return hmVorhandeneSpalten;
	}

	private int iAnzahlErfolgreichImportiert = 0;

	private TreeMap<Integer, ArrayList<String>> fehler = new TreeMap<Integer, ArrayList<String>>();

	private TreeMap<Integer, Throwable> exceptions = new TreeMap<Integer, Throwable>();

	public TreeMap<Integer, ArrayList<String>> getFehler() {
		return fehler;
	}

	public ArrayList<String> getFehlerInZeile(int iZeile) {

		if (fehler.containsKey(iZeile)) {
			return fehler.get(iZeile);
		} else {
			return null;
		}

	}

	public Throwable getExceptionInZeile(int iZeile) {

		if (exceptions.containsKey(iZeile)) {
			return exceptions.get(iZeile);
		} else {
			return null;
		}

	}


	public int getAnzahlZeilenOhneUeberschrift() {
		return sheet.getRows() - 1;
	}

	public Cell[] getZeile(int iZeile) {
		return sheet.getRow(iZeile + 1);
	}

	public JxlImportBeanSevices(byte[] xlsDatei) {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			Workbook workbook = Workbook.getWorkbook(is, ws);

			sheet = workbook.getSheet(0);

			hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

	}

	public String getStringAusXLS(String feldname, int iLaenge, int iZeile) {
		iZeile = iZeile + 1;

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Cell[] zeilen = sheet.getRow(iZeile);

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						addFehler(feldname, " ist zu lang (>" + iLaenge + ")", iZeile);
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

	public Short getShortAusXLS(String feldname, int iZeile) {
		iZeile = iZeile + 1;

		if (hmVorhandeneSpalten.containsKey(feldname)) {
			Cell[] zeilen = sheet.getRow(iZeile);
			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().trim().equals("0")) {
						return new Short((short) 0);
					} else if (c.getContents().trim().equals("1")) {
						return new Short((short) 1);
					} else {

						addFehler(feldname, " darf nur die Werte 0 bzw. 1 enthalten. ", iZeile);

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

	public java.util.Date getDateAusXLS(String feldname, int iZeile) {
		iZeile = iZeile + 1;

		if (hmVorhandeneSpalten.containsKey(feldname)) {
			Cell[] zeilen = sheet.getRow(iZeile);
			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.DATE || c.getType() == CellType.DATE) {

						return ((jxl.DateCell) c).getDate();
					} else {

						addFehler(feldname, " muss vom Typ 'Date' sein.", iZeile);

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

	public BigDecimal getBigDecimalAusXLS(String feldname, int iZeile) {
		iZeile = iZeile + 1;

		if (hmVorhandeneSpalten.containsKey(feldname)) {
			Cell[] zeilen = sheet.getRow(iZeile);
			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new BigDecimal(d);

					} else {

						addFehler(feldname, " muss vom Typ 'Zahl' sein.", iZeile);

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

	public Double getDoubleAusXLS(String feldname, int iZeile) {
		iZeile = iZeile + 1;

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);
			Cell[] zeilen = sheet.getRow(iZeile);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new Double(d);

					} else {

						addFehler(feldname, " muss vom Typ 'Zahl' sein.", iZeile);

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

	public void addFehlerAllgemein(String text) {

		addFehler(null, text, FEHLER_ZEILE_ALLGEMEIN);

	}

	public void erhoeheAnzahlErfoglreichImportiert() {
		iAnzahlErfolgreichImportiert++;
	}

	public int getIAnzahlErfolgreichImportiert() {
		return iAnzahlErfolgreichImportiert;
	}

	public void addFehler(String feldname, String text, int iZeile) {

		ArrayList<String> alFehler = null;

		if (fehler.containsKey(iZeile)) {
			alFehler = fehler.get(iZeile);
		} else {
			alFehler = new ArrayList<String>();
		}

		if (feldname != null) {
			alFehler.add(feldname + " " + text);
		} else {
			alFehler.add(text);
		}

		fehler.put(iZeile, alFehler);

	}

	public void addException(Throwable exception, int iZeile) {

		exceptions.put(iZeile, exception);

	}

	public JxlImportErgebnis getInfosFuerClient() {

		JxlImportErgebnis erg = new JxlImportErgebnis(fehler, exceptions, iAnzahlErfolgreichImportiert);
		return erg;

	}

}
