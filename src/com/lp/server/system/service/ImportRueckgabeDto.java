package com.lp.server.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ImportRueckgabeDto implements Serializable {

	public static String ART_ERROR = "ERROR";
	public static String ART_WARNING = "WARNING";
	public static String ART_INFO = "INFO";

	private HashMap<String, ArrayList<ImportRueckgabeZeile>> hmZeilen = new HashMap<String, ArrayList<ImportRueckgabeZeile>>();

	public void addZeile(String art, String text, int zeile) {

		ArrayList<ImportRueckgabeZeile> zeilen = null;
		if (hmZeilen.containsKey(art)) {
			zeilen = hmZeilen.get(art);
		} else {
			zeilen = new ArrayList<ImportRueckgabeZeile>();
		}

		zeilen.add(new ImportRueckgabeZeile(text, zeile));

		hmZeilen.put(art, zeilen);

	}

	public String getErrorsAsText() {
		return getText(ART_ERROR);
	}

	public String getWarningsAsText() {
		return getText(ART_WARNING);
	}

	public String getInfosAsText() {
		return getText(ART_INFO);
	}

	public String getAllAsText() {

		if (sindErrorsVorhanden() == false && sindWarningsVorhanden() == false && sindInfosVorhanden() == false) {

			return "Keine Fehler/Warnings/Infos vorhanden!";
		} else {

			String all = getText(ART_ERROR) + getText(ART_WARNING) +  getText(ART_INFO) ;
			return all;
		}

	}

	public Object[] getHeaderForJTable() {
		return new Object[] { "Art", "Text", "Zeile" };
	}

	public Object[][] getContentForJTable() {

		ArrayList alZeilen = new ArrayList();

		if (sindErrorsVorhanden() == false && sindWarningsVorhanden() == false && sindInfosVorhanden() == false) {

			Object[] zeile = new Object[] { ART_INFO, "Keine Fehler/Warnings/Infos vorhanden!", "" };
			alZeilen.add(zeile);

		} else {

			ArrayList<ImportRueckgabeZeile> zeilen = hmZeilen.get(ART_ERROR);

			if (zeilen != null) {
				Iterator it = zeilen.iterator();

				while (it.hasNext()) {
					ImportRueckgabeZeile temp = (ImportRueckgabeZeile) it.next();
					Object[] zeile = new Object[] { ART_ERROR, temp.getText(), temp.getZeile() };
					alZeilen.add(zeile);
				}
			}

			zeilen = hmZeilen.get(ART_WARNING);

			if (zeilen != null) {
				Iterator it = zeilen.iterator();
				while (it.hasNext()) {
					ImportRueckgabeZeile temp = (ImportRueckgabeZeile) it.next();
					Object[] zeile = new Object[] { ART_WARNING, temp.getText(), temp.getZeile() };
					alZeilen.add(zeile);
				}
			}
			zeilen = hmZeilen.get(ART_INFO);
			if (zeilen != null) {
				
				Iterator	it = zeilen.iterator();
				while (it.hasNext()) {
					ImportRueckgabeZeile temp = (ImportRueckgabeZeile) it.next();
					Object[] zeile = new Object[] { ART_INFO, temp.getText(), temp.getZeile() };
					alZeilen.add(zeile);
				}
			}
		}

		Object[][] oData = new Object[0][0];

		return (Object[][]) alZeilen.toArray(oData);
	}

	private String getText(String art) {
		ArrayList<ImportRueckgabeZeile> zeilen = hmZeilen.get(art);

		String s = "";

		if (zeilen != null) {
			Iterator it = zeilen.iterator();
			while (it.hasNext()) {

				ImportRueckgabeZeile zeile = (ImportRueckgabeZeile) it.next();

				s += art + " " + zeile.getText() + " Zeile: " + zeile.getZeile() + "\r\n";
			}
		}
		return s;
	}

	public boolean sindErrorsVorhanden() {
		ArrayList<ImportRueckgabeZeile> errors = hmZeilen.get(ART_ERROR);

		if (errors == null || errors.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean sindWarningsVorhanden() {
		ArrayList<ImportRueckgabeZeile> errors = hmZeilen.get(ART_WARNING);

		if (errors == null || errors.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean sindInfosVorhanden() {
		ArrayList<ImportRueckgabeZeile> errors = hmZeilen.get(ART_INFO);

		if (errors == null || errors.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

}
