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
package com.lp.server.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidClassException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejbfac.SystemServicesFacBean;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Util Klasse f&uuml;r Reports mit Jasperreport.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Adi Daum; 14.11.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/21 16:33:27 $
 */
public abstract class LPReport extends Facade implements JRDataSource {
	// Mehrfach verwendete Feldnamen
	public final static String F_IDENTNUMMER = "F_IDENTNUMMER";
	public final static String F_BEZEICHNUNG = "F_BEZEICHNUNG";
	public final static String F_ZUSATZBEZEICHNUNG = "F_ZUSATZBEZEICHNUNG";
	public final static String F_KURZBEZEICHNUNG = "F_KURZBEZEICHNUNG";
	public final static String F_REFERENZNUMMER = "F_REFERENZNUMMER";
	public final static String F_ZUSATZBEZEICHNUNG2 = "F_ARTIKELCZBEZ2";
	public final static String F_ARTIKELKOMMENTAR = "F_ARTIKELKOMMENTAR";
	public final static String F_ARTIKEL_BAUFORM = "F_ARTIKEL_BAUFORM";
	public final static String F_ARTIKEL_VERPACKUNGSART = "F_ARTIKEL_VERPACKUNGSART";
	public final static String F_ARTIKEL_MATERIAL = "F_ARTIKEL_MATERIAL";
	public final static String F_ARTIKEL_MATERIALGEWICHT = "F_ARTIKEL_MATERIALGEWICHT";
	public final static String F_ARTIKEL_KURS_MATERIALZUSCHLAG = "F_ARTIKEL_KURS_MATERIALZUSCHLAG";
	public final static String F_ARTIKEL_DATUM_MATERIALZUSCHLAG = "F_ARTIKEL_DATUM_MATERIALZUSCHLAG";
	public final static String F_ARTIKEL_BREITE = "F_ARTIKEL_BREITE";
	public final static String F_ARTIKEL_HOEHE = "F_ARTIKEL_HOEHE";
	public final static String F_ARTIKEL_TIEFE = "F_ARTIKEL_TIEFE";
	public final static String F_HERSTELLER_NAME = "F_HERSTELLER_NAME";
	// F_KUNDEARTIKELNR: Kundeartikelnummer aus aktuell gueltiger Kunde -
	// Sonderkondition.
	public final static String F_KUNDEARTIKELNR = "F_KUNDEARTIKELNR";
	public final static String F_ARTIKELLIEFERANT_ARTIKELNR = "F_ARTIKELLIEFERANT_ARTIKELNR";
	public final static String F_ARTIKELLIEFERANT_STDMENGE = "F_ARTIKELLIEFERANT_STDMENGE";
	public final static String F_ARTIKELLIEFERANT_BEZ = "F_ARTIKELLIEFERANT_BEZ";
	public final static String F_ARTIKEL_MONTAGE_RASTERSTEHEND = "F_ARTIKEL_RASTERSTEHEND";
	public final static String F_ARTIKEL_GEWICHTKG = "F_ARTIKEL_GEWICHTKG";
	public final static String F_AUFTRAGEIGENSCHAFT_FA = "F_AUFTRAGEIGENSCHAFT_FA";
	public final static String F_AUFTRAGEIGENSCHAFT_CLUSTER = "F_AUFTRAGEIGENSCHAFT_CLUSTER";
	public final static String F_AUFTRAGEIGENSCHAFT_EQNR = "F_AUFTRAGEIGENSCHAFT_EQNR";
	public final static String F_ARTIKEL_RAHMENDETAILBEDARF = "F_ARTIKEL_RAHMENDETAILBEDARF";
	// Artikel: Detail AnzahlBestellt
	public final static String F_ARTIKELMENGE_OFFEN = "F_ARTIKELMENGE_OFFEN";
	// Artikel: Detail AnzahlRahmenbestellt
	public final static String F_ARTIKELRAHMENMENGE_OFFEN = "F_ARTIKELRAHMENMENGE_OFFEN";
	public final static String F_ARTIKEL_URSPRUNGSLAND = "F_ARTIKEL_URSPRUNGSLAND";

	// Allgemeine Parameter
	public final static String P_MANDANT_OBJ = "P_MANDANT_OBJ";
	public final static String P_REPORT_INFORMATION = "REPORT_INFORMATION";
	public final static String P_WAEHRUNG = "P_WAEHRUNG";
	public final static String P_KOSTENSTELLE = "P_KOSTENSTELLE";
	public final static String P_SORTIERUNG = "P_SORTIERUNG";
	public final static String P_FILTER = "P_FILTER";
	public final static String P_MANDANTWAEHRUNG = "P_MANDANTWAEHRUNG";
	public final static String P_DRUCKTYPE = "P_DRUCKTYPE";
	// Belegspezifische Parameter
	public final static String P_EIGENTUMSVORBEHALT = "P_EIGENTUMSVORBEHALT";
	public final static String P_LIEFERBEDINGUNGEN = "P_LIEFERBEDINGUNGEN";
	public final static String P_UNSERZEICHEN = "P_UNSERZEICHEN";
	public final static String P_MANDANTADRESSE = "P_MANDANTADRESSE";
	public final static String P_ADRESSBLOCK = "P_ADRESSBLOCK";
	public final static String P_KOPIE_NUMMER = "P_KOPIE_NUMMER";
	public final static String P_ANSPRECHPARTNEREMAIL = "P_ANSPRECHPARTNEREMAIL";
	public final static String P_ANSPRECHPARTNERFAX = "P_ANSPRECHPARTNERFAX";
	public final static String P_ANSPRECHPARTNERTELEFON = "P_ANSPRECHPARTNERTELEFON";
	public final static String P_ANSPRECHPARTNER = "P_ANSPRECHPARTNER";
	public final static String P_ANSPRECHPARTNERHANDY = "P_ANSPRECHPARTNERHANDY";
	public final static String P_ANSPRECHPARTNERDW = "P_ANSPRECHPARTNERDW";
	public final static String P_VERTRETER = "P_VERTRETER";
	public final static String P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME = "P_VERTRETER_UNTERSCHRIFTSFUNKTION_UND_NAME";
	public final static String P_MANDANT_ANREDE_UND_NAME = "P_MANDANT_ANREDE_UND_NAME";
	public final static String P_VERTRETER_UNTERSCHRIFTSTEXT = "P_VERTRETER_UNTERSCHRIFTSTEXT";
	public final static String P_EXTERNERKOMMENTAR = "P_EXTERNERKOMMENTAR";
	public final static String P_VERTRETEREMAIL = "P_VERTRETEREMAIL";
	public final static String P_VERTRETERFAX = "P_VERTRETERFAX";
	public final static String P_VERTRETEERTELEFON = "P_VERTRETERTELEFON";
	public final static String P_AUFTRAGEIGENSCHAFT_FA = "AUFTRAGEIGENSCHAFT_FA";
	public final static String P_AUFTRAGEIGENSCHAFT_CLUSTER = "AUFTRAGEIGENSCHAFT_CLUSTER";
	public final static String P_AUFTRAGEIGENSCHAFT_EQNR = "AUFTRAGEIGENSCHAFT_EQNR";
	public final static String P_ARTIKEL_BREITE = "P_ARTIKEL_BREITE";
	public final static String P_ARTIKEL_HOEHE = "P_ARTIKEL_HOEHE";
	public final static String P_ARTIKEL_TIEFE = "P_ARTIKEL_TIEFE";
	public final static String P_STUECKLISTENEIGENSCHAFT_INDEX = "P_STUECKLISTENEIGENSCHAFT_INDEX";
	public final static String P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ = "P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ";

	// Allgemeine Reports
	public final static String REPORT_GANZSEITIGESBILD = "ganzseitigesbild.jasper";
	public final static String REPORT_MODUL_ALLGEMEIN = "allgemein";

	// Journalspezifische Parameter
	public final static String P_SORTIERENACHKOSTENSTELLE = "P_SORTIERENACHKOSTENSTELLE";
	public final static String P_SORTIERENACHKUNDE = "P_SORTIERENACHKUNDE";
	public final static String P_SORTIERENACHLIEFERANT = "P_SORTIERENACHLIEFERANT";
	public final static String P_SORTIERENACHPERSONAL = "P_SORTIERENACHPERSONAL";
	public final static String P_SORTIERENACHVERTRETER = "P_SORTIERENACHVERTRETER";
	public final static String P_MITDETAILS = "P_MITDETAILS";

	protected int index = -1;
	protected int sAktuellerReport = -1;

	private JasperReport jasperReport = null;
	private JasperPrintLP jasperPrintLP = null;

	public final static int MWST_TABELLE_LINKS = 0;
	public final static int MWST_TABELLE_SUMME_POSITIONEN = 1;
	public final static int MWST_MWST_TABELLE_WAEHRUNG = 2;
	public final static int MWST_TABELLE_RECHTS = 3;
	public final static int MWST_ENDBETRAGMITMWST = 4;

	public LPReport() {
		// ???
	}

	/**
	 * 
	 * @param parameterI
	 *            Map
	 * @param modul
	 *            String
	 * @param reportname
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param sprache
	 *            Locale
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI, String modul,
			String reportname, String mandantCNr, Locale sprache,
			TheClientDto theClientDto) throws EJBExceptionLP {
		initJRDS(parameterI, modul, reportname, mandantCNr, sprache,
				theClientDto, true, null);
	}

	/**
	 * 
	 * @param parameterI
	 *            Map
	 * @param modul
	 *            String
	 * @param reportname
	 *            String
	 * @param mandantCNr
	 *            String
	 * @param sprache
	 *            Locale
	 * @param theClientDto
	 *            String
	 * @param bMitLogo
	 *            boolean
	 * @param kostenstelleIId
	 *            Integer: Kostenstellenspezifisches Reportverzeichnis
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI, String modul,
			String reportname, String mandantCNr, Locale sprache,
			TheClientDto theClientDto, boolean bMitLogo, Integer kostenstelleIId)
			throws EJBExceptionLP {
		// LPReportParameter p = new LPReportParameter(parameterI, modul,
		// reportname, mandantCNr,
		// sprache, idUser, bMitLogo, kostenstelleIId);
		// initJRDS(p);
		// ----------------------------------------------------------------------
		// -------
		try {
			jasperPrintLP = new JasperPrintLP();
			if (parameterI == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception(
								"parameterI == null"));
			}
			// Kostenstellenspezifisches Reportverzeichnis (optional)
			String cSubdirectory = null;
			if (kostenstelleIId != null) {
				KostenstelleDto kstDto = getSystemFac()
						.kostenstelleFindByPrimaryKey(kostenstelleIId);
				if (kstDto.getCSubdirectory() != null
						&& kstDto.getCSubdirectory().length() > 0) {
					cSubdirectory = kstDto.getCSubdirectory();
				}
			}

			// PJ 15185
			if (theClientDto.getReportvarianteIId() != null) {
				ReportvarianteDto rvpDto = getDruckerFac()
						.reportvarianteFindByPrimaryKey(
								theClientDto.getReportvarianteIId());
				if (rvpDto.getCReportname().equals(reportname)) {
					reportname = rvpDto.getCReportnamevariante();
					theClientDto.setReportvarianteIId(null);
				}
			}

			// Verzeichnis, in dem der Report liegt
			String reportdir = SystemServicesFacBean.getPathFromLPDir(modul,
					reportname, mandantCNr, sprache, cSubdirectory,
					theClientDto);
			if (reportdir == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
						new Exception("reportdir == null: report=" + modul
								+ "|" + reportname));
			}

			// Verzeichnis, in dem der Report liegt
			String reportdirDemoWasserzeichen = SystemServicesFacBean
					.getPathFromLPDir("allgemein", "demo_wasserzeichen.jasper",
							mandantCNr, sprache, cSubdirectory, theClientDto);

			// Report Locale
			parameterI.put("REPORT_LOCALE", sprache);
			parameterI.put(P_REPORT_INFORMATION,
					getReportInformation(reportdir, theClientDto));
			// Mandant
			parameterI.put("MANDANT_C_NR", mandantCNr);
			File f = new File(reportdir);
			// Reportverzeichnis
			parameterI.put(
					"REPORT_DIRECTORY",
					reportdir.substring(0, reportdir.length()
							- f.getName().length()));
			parameterI.put(P_MANDANT_OBJ, getMandantFac().createReportMandantDto(theClientDto));
			// Report Root Verzeichnis
			parameterI.put("REPORT_ROOT_DIRECTORY", new java.io.File(
					getReportDir()).getAbsolutePath());
			// per Mandantenparameter kann das Logo auch IMMER drauf sein
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(mandantCNr,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN);
			short iValue = Short.parseShort(parameter.getCWert());
			boolean bLogoImmerDrucken = Helper.short2boolean(iValue);
			parameterI.put("P_LOGO_IMAGE", SystemServicesFacBean
					.getPathFromLPDir("allgemein", "logo.png", mandantCNr,
							sprache, cSubdirectory, theClientDto));
			;
			parameterI.put("P_MITLOGO", new Boolean(bMitLogo
					|| bLogoImmerDrucken));

			parameterI.put("P_LOGO_SUBREPORT", SystemServicesFacBean
					.getPathFromLPDir("allgemein", "logo.jasper", mandantCNr,
							sprache, cSubdirectory, theClientDto));
			parameterI.put("P_FUSSZEILEN_SUBREPORT", SystemServicesFacBean
					.getPathFromLPDir("allgemein", "fuss.jasper", mandantCNr,
							sprache, cSubdirectory, theClientDto));

			JasperReport jasperReportWasserzeichen = null;

			java.sql.Connection sqlcon;

			try {
				String url = getParameterFac().getAnwenderparameter(
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL)
						.getCWert();
				// "jdbc:postgresql://" + "localhost" + "/" + "LP",
				if (url.trim().length() > 0) {
					if (url.contains("postgresql")) {
						Class.forName("org.postgresql.Driver");
					}
					if (url.contains("jtds")) {
						Class.forName("net.sourceforge.jtds.jdbc.Driver");
					}
					sqlcon = java.sql.DriverManager.getConnection(url,
							"hvguest", "h4gzfdavfs");
					parameterI.put("P_SUBREPORTCONNECTION", sqlcon);
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				myLogger.error(ex.getMessage(), ex);
			} catch (ClassNotFoundException ex) {
				System.out.println(ex.getMessage());
				myLogger.error(ex.getMessage(), ex);
			}

			try {
				// Report laden
				jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportdir);
				try {
					if (reportdirDemoWasserzeichen != null) {
						jasperReportWasserzeichen = (JasperReport) JRLoader
								.loadObjectFromFile(reportdirDemoWasserzeichen);
					}
				} catch (Exception e) {
					// dann auslassen
				}
			} catch (JRException ex) {
				Throwable eCause = ex.getCause();
				if (eCause instanceof FileNotFoundException) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
							ex);
				} else if (eCause instanceof InvalidClassException) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION, ex);
				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
				}
			}

			// Report fuellen

			index = -1;

			MandantDto mandanDto = getMandantFac().mandantFindByPrimaryKey(
					mandantCNr, theClientDto);

			try {
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameterI, this);

				if (jasperReportWasserzeichen != null
						&& Helper.short2boolean(mandanDto.getBDemo())) {

					try {
						// PJ 14141
						JasperPrint jasperPrintDemoWasserzeichen = JasperFillManager
								.fillReport(jasperReportWasserzeichen,
										parameterI, this);

						JRPrintElement bild = null;

						Iterator<?> iterator = jasperPrintDemoWasserzeichen
								.getPages().iterator();
						if (iterator.hasNext()) {
							JRPrintPage zeitdaten = (JRPrintPage) iterator
									.next();
							if (zeitdaten.getElements() != null
									&& zeitdaten.getElements().size() > 0) {
								bild = (JRPrintElement) zeitdaten.getElements()
										.get(0);
							}

						}

						Iterator<?> iterator2 = jasperPrint.getPages()
								.iterator();
						while (iterator2.hasNext()) {
							JRPrintPage x = (JRPrintPage) iterator2.next();
							x.addElement(bild);
							jasperPrint.getPages().set(0, x);
						}

					} catch (Exception e) {
						System.out.println("Exception e:" + e.getMessage()) ;
						// dann auslassen
					}
				}

				jasperPrintLP.setPrint(jasperPrint);
			} catch (JRException ex) {
				// File not found abfangen, damit eine eigene meldung kommt
				// tritt z.b. auf, wenn ein subreport fehlt
				if (ex.getCause() != null
						&& ex.getCause() instanceof FileNotFoundException) {
					EJBExceptionLP e = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND, ex);
					ArrayList<Object> a = new ArrayList<Object>();
					a.add(ex.getMessage());
					e.setAlInfoForTheClient(a);
					throw e;
				} else {
					EJBExceptionLP e = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
					ArrayList<Object> a = new ArrayList<Object>();
					a.add(reportdir);
					a.add(ex.getMessage());
					e.setAlInfoForTheClient(a);
					throw e;
				}
			} catch (JRRuntimeException ex) {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(reportdir);
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
			}

			// CSV-Daten mitgeben

			index = -1;

			// System.out.println(this.hashCode()+ "  " + reportname);

			ArrayList al = new ArrayList();

			// Zuerst Ueberschrift
			int fieldLength = jasperReport.getFields() == null ? 0
					: jasperReport.getFields().length;
			Object[] oZeile = new Object[fieldLength];

			for (int j = 0; j < fieldLength; j++) {

				oZeile[j] = jasperReport.getFields()[j].getName();

			}
			al.add(oZeile);

			// Dann Daten
			try {
				while (this.next() == true) {

					oZeile = new Object[fieldLength];

					for (int j = 0; j < fieldLength; j++) {

						Object o = this
								.getFieldValue(jasperReport.getFields()[j]);

						if (o != null && o instanceof Image) {
							// Bilder auslassen
						} else {
							if (o != null && o instanceof String) {

								String s = (String) o;
								s = s.replaceAll("\n", " ");

								oZeile[j] = s;
							} else {
								oZeile[j] = o;
							}

						}

					}
					al.add(oZeile);
				}
			} catch (JRException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
			}

			Object[][] returnArray = new Object[al.size()][fieldLength]; // [fieldLength][al.size()];
			jasperPrintLP.setDatenMitUeberschrift(((Object[][]) al
					.toArray(returnArray)));

			// Name ergaenzen, damit er am client richtig angezeigt werden kann
			/**
			 * @todo MB->MB hart codiertes raus!!! PJ 4368
			 */
			String s = "helium" + File.separator + "report" + File.separator;
			int index = reportdir.indexOf(s);
			String reportName = reportdir.substring(index + s.length());
			jasperPrintLP.setSReportName(reportName);
			jasperPrintLP.setBMitLogo(bMitLogo);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * 
	 * @param parameterI
	 *            Map
	 * @param jasperReport 
	 * @param reportDir 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI,
			JasperReport jasperReport, String reportDir,
			TheClientDto theClientDto) throws EJBExceptionLP {
		jasperPrintLP = new JasperPrintLP();
		if (parameterI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("parameterI == null"));
		}

		parameterI.put(P_REPORT_INFORMATION,
				getReportInformation(reportDir, theClientDto));

		// Kostenstellenspezifisches Reportverzeichnis (optional)
		// Report fuellen
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameterI, this);
			jasperPrintLP.setPrint(jasperPrint);
		} catch (JRException ex) {
			// File not found abfangen, damit eine eigene meldung kommt
			// tritt z.b. auf, wenn ein subreport fehlt
			if (ex.getCause() != null
					&& ex.getCause() instanceof FileNotFoundException) {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
			} else {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add("");
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
			}
		} catch (JRRuntimeException ex) {
			EJBExceptionLP e = new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
			ArrayList<Object> a = new ArrayList<Object>();
			a.add("");
			a.add(ex.getMessage());
			e.setAlInfoForTheClient(a);
			throw e;
		}

		// Name ergaenzen, damit er am client richtig angezeigt werden kann
		// CSV-Daten mitgeben

		index = -1;

		// System.out.println(this.hashCode()+ "  " + reportname);

		ArrayList al = new ArrayList();

		// Zuerst Ueberschrift
		Object[] oZeile = new Object[jasperReport.getFields().length];

		for (int j = 0; j < jasperReport.getFields().length; j++) {

			oZeile[j] = jasperReport.getFields()[j].getName();

		}
		al.add(oZeile);

		// Dann Daten
		try {
			while (this.next() == true) {

				oZeile = new Object[jasperReport.getFields().length];

				for (int j = 0; j < jasperReport.getFields().length; j++) {

					Object o = this.getFieldValue(jasperReport.getFields()[j]);

					if (o != null && o instanceof Image) {
						// Bilder auslassen
					} else {
						oZeile[j] = o;
					}
				}
				al.add(oZeile);
			}
		} catch (JRException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
		}
		Object[][] returnArray = new Object[jasperReport.getFields().length][al
				.size()];
		jasperPrintLP.setDatenMitUeberschrift(((Object[][]) al
				.toArray(returnArray)));
		jasperPrintLP.setSReportName("");
		jasperPrintLP.setBMitLogo(false);

	}

	// /**
	// *
	// * @throws EJBExceptionLP
	// * @param p LPReportParameter
	// */
	// protected final void initJRDS(LPReportParameter p)
	// throws EJBExceptionLP {
	// try {
	// jasperPrintLP = new JasperPrintLP();
	// if (p.getParameterI() == null) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
	// new Exception("parameterI == null"));
	// }
	// // Kostenstellenspezifisches Reportverzeichnis (optional)
	// String cSubdirectory = null;
	// if (p.getKostenstelleIId() != null) {
	// KostenstelleDto kstDto =
	// getSystemFac().kostenstelleFindByPrimaryKey(p.getKostenstelleIId());
	// if (kstDto.getCSubdirectory() != null &&
	// kstDto.getCSubdirectory().length() > 0) {
	// cSubdirectory = kstDto.getCSubdirectory();
	// }
	// }
	// // Verzeichnis, in dem der Report liegt
	// String reportdir = SystemServicesFacBean.getPathFromLPDir(p.getModul(),
	// p.getReportname(), p.getMandantCNr(), p.getSprache(), cSubdirectory,
	// p.getIdUser());
	// if (reportdir == null) {
	// throw new
	// EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
	// new Exception("reportdir == null"));
	// }
	// // Report Locale
	// p.getParameterI().put("REPORT_LOCALE", p.getSprache());
	// p.getParameterI().put(P_REPORT_INFORMATION,
	// getReportInformation(reportdir, p.getIdUser()));
	// // Mandant
	// p.getParameterI().put("MANDANT_C_NR", p.getMandantCNr());
	// File f = new File(reportdir);
	// // Reportverzeichnis
	// p.getParameterI().put("REPORT_DIRECTORY", reportdir.substring(
	// 0, reportdir.length() - f.getName().length()));
	// // Report Root Verzeichnis
	// p.getParameterI().put("REPORT_ROOT_DIRECTORY",
	// new java.io.File(getReportDir()).getAbsolutePath());
	// // per Mandantenparameter kann das Logo auch IMMER drauf sein
	// ParametermandantDto parameter = getParameterFac().getMandantparameter(
	// p.getMandantCNr(), ParameterFac.KATEGORIE_ALLGEMEIN,
	// ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN);
	// short iValue = Short.parseShort(parameter.getCWert());
	// boolean bLogoImmerDrucken = Helper.short2boolean(iValue);
	// // Logo heisst logo.png
	// p.getParameterI().put("P_LOGO_IMAGE",
	// SystemServicesFacBean.getPathFromLPDir("allgemein", "logo.png",
	// p.getMandantCNr(), p.getSprache(), cSubdirectory, p.getIdUser()));
	// // soll das logo drauf sein?
	// p.setBMitLogo(p.isBMitLogo() || bLogoImmerDrucken);
	// p.getParameterI().put("P_MITLOGO", new Boolean(p.isBMitLogo()));
	// // Pfad zum passenden logo-subreport
	// p.getParameterI().put("P_LOGO_SUBREPORT",
	// SystemServicesFacBean.getPathFromLPDir("allgemein", "logo.jasper",
	// p.getMandantCNr(), p.getSprache(), cSubdirectory, p.getIdUser()));
	// p.getParameterI().put("P_FUSSZEILEN_SUBREPORT",
	// SystemServicesFacBean.getPathFromLPDir("allgemein", "fuss.jasper",
	// p.getMandantCNr(), p.getSprache(), cSubdirectory, p.getIdUser()));
	// try {
	// // Report laden
	// jasperReport = (JasperReport) JRLoader.loadObject(reportdir);
	// }
	// catch (JRException ex) {
	// Throwable eCause = ex.getCause();
	// if (eCause instanceof FileNotFoundException) {
	// throw new
	// EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
	// ex);
	// }
	// else if (eCause instanceof InvalidClassException) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION,
	// ex);
	// }
	// else {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
	// }
	// }
	// // Report fuellen
	// try {
	// JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
	// p.getParameterI(), this);
	// jasperPrintLP.setPrint(jasperPrint);
	// }
	// catch (JRException ex) {
	// // File not found abfangen, damit eine eigene meldung kommt
	// // tritt z.b. auf, wenn ein subreport fehlt
	// if (ex.getCause() != null &&
	// ex.getCause() instanceof FileNotFoundException) {
	// EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.
	// FEHLER_DRUCKEN_FILE_NOT_FOUND, ex);
	// ArrayList<Object> a = new ArrayList<Object>();
	// a.add(ex.getMessage());
	// e.setAlInfoForTheClient(a);
	// throw e;
	// }
	// else {
	// EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.
	// FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
	// ArrayList<Object> a = new ArrayList<Object>();
	// a.add(reportdir);
	// a.add(ex.getMessage());
	// e.setAlInfoForTheClient(a);
	// throw e;
	// }
	// }
	// catch (JRRuntimeException ex) {
	// EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.
	// FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
	// ArrayList<Object> a = new ArrayList<Object>();
	// a.add(reportdir);
	// a.add(ex.getMessage());
	// e.setAlInfoForTheClient(a);
	// throw e;
	// }
	// // Name ergaenzen, damit er am client richtig angezeigt werden kann
	// /**
	// * @todo MB->MB hart codiertes raus!!! PJ 4368
	// */
	// String s = "helium" + File.separator + "report" + File.separator;
	// int index = reportdir.indexOf(s);
	// String reportName = reportdir.substring(index + s.length());
	// jasperPrintLP.setSReportName(reportName);
	// jasperPrintLP.setLpReportParameter(p);
	// }
	// catch (RemoteException ex) {
	// throwEJBExceptionLPRespectOld(ex);
	// }
	// }

	protected JasperPrintLP getReportPrint() {
		return this.jasperPrintLP;
	}

	protected byte[] getPDF() {
		byte[] ba = null;
		try {
			ba = JasperExportManager.exportReportToPdf(this.jasperPrintLP
					.getPrint());
		} catch (Throwable t) {
			/**
			 * @todo throw new
			 *       EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */
		}
		return ba;
	}

	protected String getXML() {
		String s = null;
		try {
			s = JasperExportManager.exportReportToXml(this.jasperPrintLP
					.getPrint());
		} catch (Throwable t) {
			/**
			 * @todo throw new
			 *       EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */

		}
		return s;
	}

	protected void writeHtml(String filename) {
		try {
			JasperExportManager.exportReportToHtmlFile(
					this.jasperPrintLP.getPrint(), filename);
		} catch (Throwable t) {
			/**
			 * @todo throw new
			 *       EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */

		}
	}

	protected void writePdf(String filename) {
		try {
			JasperExportManager.exportReportToPdfFile(
					this.jasperPrintLP.getPrint(), filename);
		} catch (Throwable t) {
			/**
			 * @todo throw new
			 *       EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */

		}
	}

	/**
	 * Bauen der ReportInformation zur optionalen Anzeige am Report.
	 * 
	 * @param filename
	 *            String
	 * @param theClientDto
	 *            String
	 * @return String
	 */
	private String getReportInformation(String filename,
			TheClientDto theClientDto) {
		StringBuffer reportInformation = new StringBuffer();
		PersonalDto userPersonal = null;
		try {
			Locale locale = theClientDto.getLocUi();
			userPersonal = getPersonalFac().personalFindByPrimaryKeySmall(
					theClientDto.getIDPersonal());
			String sKurzzeichen;
			if (userPersonal.getCKurzzeichen() != null) {
				sKurzzeichen = userPersonal.getCKurzzeichen();
			} else {
				sKurzzeichen = "";
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			reportInformation.append(theClientDto.getMandant() + " ");
			// Mandantbezeichnung aus System, Mandant, Lieferkonditionen
			reportInformation.append(mandantDto.getCKbez().trim());
			reportInformation.append("| ");
			reportInformation.append(sKurzzeichen);
			reportInformation.append("| ");
			String[] aSplitReport = filename.split("helium");
			if (aSplitReport != null && (aSplitReport.length > 0)) {
				reportInformation.append(aSplitReport[aSplitReport.length - 1] + // nur
						// den
						// teil
						// nach
						// dem
						// letzten
						// "helium"
						"|" + Helper.locale2String(locale).trim());
			}
			reportInformation.append("| ");
			reportInformation.append(DateFormat.getDateTimeInstance(
					DateFormat.MEDIUM, DateFormat.LONG, locale).format(
					new java.sql.Date(System.currentTimeMillis())));
		} catch (Throwable t) {
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new
			 *       Exception(t));
			 */
		}
		return reportInformation.toString();
	}

	/**
	 * getReportDir
	 * 
	 * @return String
	 */
	static public String getReportDir() {
		return HelperServer.getLPResourceBundle().getString(
				"drucken.root.formular.dir");
	}

	/**
	 * Aufbereiten eines Mediastandards zum Ausdruck auf einem Beleg.
	 * 
	 * @param mediastandardDto
	 *            MediastandardDto
	 * @param theClientDto
	 *            String
	 * @return BelegPositionDruckTextbausteinDto
	 * @throws EJBExceptionLP
	 */
	public static BelegPositionDruckTextbausteinDto printTextbaustein(
			MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BelegPositionDruckTextbausteinDto dto = new BelegPositionDruckTextbausteinDto();
		if (mediastandardDto.getDatenformatCNr().equals(
				MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			dto.setSFreierText(mediastandardDto.getOMediaText());
		} else if (mediastandardDto.getDatenformatCNr().equals(
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
				|| mediastandardDto.getDatenformatCNr().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
				|| mediastandardDto.getDatenformatCNr().equals(
						MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
			BufferedImage image = Helper.byteArrayToImage(mediastandardDto
					.getOMediaImage());
			dto.setOImage(image);
		}
		return dto;
	}

	/**
	 * Aufbereiten einer Ident-Belegposition zum Ausdruck auf einem Beleg.
	 * 
	 * @param belegpositionDto
	 *            BelegpositionDto
	 * @param belegartCNr
	 *            String
	 * @param artikelDto
	 *            MediastandardDto
	 * @param locale
	 *            Locale
	 * @param theClientDto
	 *            String
	 * @return BelegPositionDruckIdentDto
	 */

	public BelegPositionDruckIdentDto printIdent(
			BelegpositionDto belegpositionDto, String belegartCNr,
			ArtikelDto artikelDto, Locale locale, TheClientDto theClientDto) {
		return printIdent(belegpositionDto, belegartCNr, artikelDto, locale,
				null, theClientDto);
	}

	public void duplizierePositionenWegenZusammenfassung(BelegDto belegDto,
			ArrayList<Object> dataList, int iSpaltePositionsart) {
		if (Helper.short2boolean(belegDto.getBMitzusammenfassung())) {
			if (dataList != null && dataList.size() > 0) {
				ArrayList dataListHelp = (ArrayList) dataList.clone();
				Object[] oZeileZusammenfassung = ((Object[]) dataList.get(0))
						.clone();
				oZeileZusammenfassung = new Object[oZeileZusammenfassung.length];
				oZeileZusammenfassung[iSpaltePositionsart] = LocaleFac.POSITIONSART_ZUSAMMENFASSUNG_NUR_REPORT;
				dataList.add(oZeileZusammenfassung);

				for (int i = 0; i < dataListHelp.size(); i++) {
					dataList.add(dataListHelp.get(i));
				}
			}
		}
	}

	public Object[][] duplizierePositionenWegenZusammenfassung(
			BelegDto belegDto, Object[][] data, int iSpaltePositionsart) {
		if (Helper.short2boolean(belegDto.getBMitzusammenfassung())) {

			if (data != null && data.length > 0) {

				Object[][] dataNew = new Object[(data.length * 2) + 1][data[0].length];

				int iAktuelleZeile = 0;
				for (int i = 0; i < data.length; i++) {
					dataNew[iAktuelleZeile] = data[i];
					iAktuelleZeile++;
				}

				Object[] oZeileZusammenfassung = ((Object[]) data[0]).clone();
				oZeileZusammenfassung = new Object[oZeileZusammenfassung.length];
				oZeileZusammenfassung[iSpaltePositionsart] = LocaleFac.POSITIONSART_ZUSAMMENFASSUNG_NUR_REPORT;
				dataNew[iAktuelleZeile] = oZeileZusammenfassung;
				iAktuelleZeile++;

				for (int i = 0; i < data.length; i++) {
					dataNew[iAktuelleZeile] = data[i];
					iAktuelleZeile++;
				}

				return dataNew;
			} else {
				return data;
			}
		} else {
			return data;
		}
	}

	public BelegPositionDruckIdentDto printIdent(
			BelegpositionDto belegpositionDto, String belegartCNr,
			ArtikelDto artikelDto, Locale locale, Integer partnerIId,
			TheClientDto theClientDto) {
		BelegPositionDruckIdentDto dto = new BelegPositionDruckIdentDto();
		try {
			// echte Artikel bzw. Handartikel
			if (artikelDto != null) {
				// Der Artikel hat ein Spr-Dto im UI-Locale mit.
				// Wenn die Bezeichnung nicht im UI-Locale gedruckt werden soll,
				// muss ich das andere SPR-Dto holen.
				ArtikelsprDto artsprDto = artikelDto.getArtikelsprDto();
				if (!theClientDto.getLocUi().equals(locale)) {
					// 1. suche nach einer Bezeichnung in der uebergebenen
					// Sprache.
					artsprDto = getArtikelFac()
							.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
									artikelDto.getIId(),
									Helper.locale2String(locale), theClientDto);
					/**
					 * @todo hier noch einfuegen: falls z.b. enUS nicht
					 *       moeglich, dann enGB suchen
					 */
				}
				// 2. Wenn keine gefunden wurde, dann zieht die
				// Mandantensprache.
				if (artsprDto == null) {
					artsprDto = getArtikelFac()
							.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
									artikelDto.getIId(),
									theClientDto.getLocMandantAsString(),
									theClientDto);
				}
				// 3. Wenn noch immer keine gefunden, dann in der Sprache des
				// Konzerns.
				if (artsprDto == null) {
					artsprDto = getArtikelFac()
							.artikelsprFindByArtikelIIdLocaleCNrOhneExc(
									artikelDto.getIId(),
									theClientDto.getLocKonzernAsString(),
									theClientDto);
				}
				// 4. Wenn eins gefunden wurde, dann an den Artikel haengen.
				if (artsprDto != null) {
					artikelDto.setArtikelsprDto(artsprDto);
				}

				StringBuffer sbArtikelInfo = new StringBuffer();
				if (!(artikelDto.getArtikelartCNr() == null)
						&& !artikelDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					// Ident nur fuer "echte" Artikel
					dto.setSIdentnummer(artikelDto.getCNr());

					sbArtikelInfo.append(artikelDto.getCNr());
					sbArtikelInfo.append("\n");

					// Warenverkehrsnummer fuer Artikel
					dto.setSWarenverkehrsnummer(artikelDto
							.getCWarenverkehrsnummer());
					// Verpackungs und Verkaufs EAN Nr.
					dto.setSVerpackungseannr(artikelDto.getCVerpackungseannr());
					dto.setSVerkaufseannr(artikelDto.getCVerkaufseannr());
					dto.setSReferenznr(artikelDto.getCReferenznr());
				}

				// Artikel Zusatzbezeichnung 2
				if (artikelDto.getArtikelsprDto() != null) {
					dto.setSArtikelZusatzBezeichnung2(artikelDto
							.getArtikelsprDto().getCZbez2());
				}
				// Text
				if (belegpositionDto != null) {
					if (belegpositionDto.getXTextinhalt() != null) {
						dto.setSIdentTexteingabe(belegpositionDto
								.getXTextinhalt());
					}
				}

				// Bezeichnung
				String sBezeichnung;
				String sZusatzBezeichnung = null;
				// Bezeichnung des Artikels uebersteuert?
				if (belegpositionDto != null
						&& (belegpositionDto
								.getBArtikelbezeichnunguebersteuert() == null || Helper
								.short2boolean(belegpositionDto
										.getBArtikelbezeichnunguebersteuert()))) {
					if (belegpositionDto.getCBez() != null) {
						sBezeichnung = belegpositionDto.getCBez();
					} else {
						if (artikelDto.getArtikelsprDto() != null
								&& artikelDto.getArtikelsprDto().getCBez() != null) {
							sBezeichnung = artikelDto.getArtikelsprDto()
									.getCBez();
						} else {
							// keine Bezeichnung gefunden.
							sBezeichnung = "";
						}
					}

					// Zusatzbezeichnung als eigenen Wert.
					if (belegpositionDto.getCZusatzbez() != null) {
						sZusatzBezeichnung = belegpositionDto.getCZusatzbez();
					} else {
						if (artikelDto.getArtikelsprDto() != null
								&& artikelDto.getArtikelsprDto().getCZbez() != null) {
							sZusatzBezeichnung = artikelDto.getArtikelsprDto()
									.getCZbez();
						}
					}
				}
				// nicht uebersteuert: Bezeichnung aus dem Artikel
				else {
					if (artikelDto.getArtikelsprDto() != null) {
						sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
						if (artikelDto.getArtikelsprDto().getCZbez() != null) {
							sZusatzBezeichnung = artikelDto.getArtikelsprDto()
									.getCZbez();
						}
					} else {
						// keine Bezeichnung gefunden.
						sBezeichnung = "";
					}
				}

				String sArtikelKommentar = "";
				// Artikelkommentar Text und Bild
				// Artikelkommentar in Sprache des artikelDto.getArtikelsprDto
				// eingebaut
				if (artikelDto.getIId() != null) {
					String sKommentarLocale = theClientDto.getLocUiAsString();
					if (artikelDto.getArtikelsprDto() != null) {
						sKommentarLocale = artikelDto.getArtikelsprDto()
								.getLocaleCNr();
					}
					ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(
									artikelDto.getIId(), belegartCNr,
									sKommentarLocale, theClientDto);
					if (aKommentarDto != null) {
						boolean bKommentarBranche = false;
						PartnerDto partnerDto = null;
						if (partnerIId != null) {
							partnerDto = getPartnerFac()
									.partnerFindByPrimaryKey(partnerIId,
											theClientDto);
							ParametermandantDto parameter = getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_ALLGEMEIN,
											ParameterFac.PARAMETER_PARTNERBRANCHE_DEFINIERT_KOMMENTAR);

							if ((Boolean) parameter.getCWertAsObject() == true) {
								bKommentarBranche = true;
							}
						}

						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k]
									.getDatenformatCNr().trim();

							if (bKommentarBranche == true && partnerDto != null) {
								ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
										.artikelkommentarartFindByPrimaryKey(
												aKommentarDto[k]
														.getArtikelkommentarartIId(),
												theClientDto);

								if (partnerDto.getBrancheIId() == null
										&& artikelkommentarartDto
												.getBrancheId() != null) {
									continue;
								}
								if (artikelkommentarartDto.getBrancheId() == null
										|| !artikelkommentarartDto
												.getBrancheId()
												.equals(partnerDto
														.getBrancheIId())) {

									continue;
								}
							}

							if (aKommentarDto[k].getArtikelkommentarsprDto() != null) {

								// Kommentar kann Text oder Bild sein
								if (cDatenformat
										.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
									if (aKommentarDto[k]
											.getArtikelkommentarsprDto() != null) {
										if (k > 0) {
											sArtikelKommentar += "\n";
										}
										sArtikelKommentar += aKommentarDto[k]
												.getArtikelkommentarsprDto()
												.getXKommentar();
									}
								} else if (cDatenformat
										.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
									// es wird hoechstens 1 Bild pro Belegart
									// gedruckt
									BufferedImage imageKommentar = Helper
											.byteArrayToImage(aKommentarDto[k]
													.getArtikelkommentarsprDto()
													.getOMedia());
									dto.setOImageKommentar(imageKommentar);
								}
							}
						}
					}
				}
				if (sBezeichnung != null) {
					sbArtikelInfo.append(sBezeichnung);
				}
				// Bezeichnungen
				dto.setSBezeichnung(sBezeichnung);
				if (artikelDto.getArtikelsprDto() != null) {
					dto.setSKurzbezeichnung(artikelDto.getArtikelsprDto()
							.getCKbez());
				}

				// Zusatzbezeichnung
				if (sZusatzBezeichnung != null) {
					dto.setSZusatzBezeichnung(sZusatzBezeichnung);
					sbArtikelInfo.append("\n" + sZusatzBezeichnung);
				}

				// PJ 17267
				ParametermandantDto parameter = null;
				parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ARTIKELLANGTEXTE_UEBERSTEUERBAR);

				boolean bArtikellangtexteUebersteuerbar = ((Boolean) parameter
						.getCWertAsObject()).booleanValue();

				if (bArtikellangtexteUebersteuerbar == true
						&& belegpositionDto != null
						&& belegpositionDto.getXTextinhalt() != null
						&& belegpositionDto.getXTextinhalt().length() > 0) {

					dto.setSArtikelkommentar(belegpositionDto.getXTextinhalt());
					sbArtikelInfo.append("\n"
							+ belegpositionDto.getXTextinhalt());

				} else {
					// Artikelkommentar
					if (sArtikelKommentar != null
							&& !sArtikelKommentar.equals("")) {
						dto.setSArtikelkommentar(sArtikelKommentar);
						sbArtikelInfo.append("\n" + sArtikelKommentar);
					}
				}

				dto.setSArtikelInfo(sbArtikelInfo.toString());

			}
			// Handeingabe ohne angelegten Handartikel.
			else {
				// Bezeichnung
				StringBuffer sbBezeichnung = new StringBuffer();
				if (belegpositionDto.getCBez() != null) {
					sbBezeichnung.append(belegpositionDto.getCBez());
				}
				if (belegpositionDto.getCZusatzbez() != null) {
					dto.setSZusatzBezeichnung(belegpositionDto.getCZusatzbez());
				}

				// ArtikelInfo besteht aus Bezeichnung und Zusatzbezeichnung
				String sArtikelInfo = sbBezeichnung.toString();
				if (dto.getSZusatzBezeichnung() != null) {
					sArtikelInfo += "\n" + dto.getSZusatzBezeichnung();
				}
				dto.setSArtikelInfo(sArtikelInfo);
				dto.setSBezeichnung(sbBezeichnung.toString());
				dto.setSIdentnummer("");
				dto.setSKurzbezeichnung("");
				dto.setSWarenverkehrsnummer("");
			}
		} catch (RemoteException ex) {

			/**
			 * @todo throwEJBExceptionLPRespectOld(ex);
			 */

		}
		return dto;
	}
}
