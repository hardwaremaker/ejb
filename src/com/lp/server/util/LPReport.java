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
package com.lp.server.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentardruck;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.SystemServicesFacBean;
import com.lp.server.system.service.BelegartmediaDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPdfReport;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;
import com.lp.util.report.PositionRpt;
import com.lp.util.report.ReportPatch;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.util.JRFontNotFoundException;
import net.sf.jasperreports.engine.util.JRLoader;

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
	public final static String F_ARTIKEL_AUFSCHLAG_BETRAG = "F_ARTIKEL_AUFSCHLAG_BETRAG";
	public final static String F_ARTIKEL_AUFSCHLAG_PROZENT = "F_ARTIKEL_AUFSCHLAG_PROZENT";
	public final static String F_ARTIKEL_BREITE = "F_ARTIKEL_BREITE";
	public final static String F_ARTIKEL_HOEHE = "F_ARTIKEL_HOEHE";
	public final static String F_ARTIKEL_TIEFE = "F_ARTIKEL_TIEFE";
	public final static String F_HERSTELLER_NAME = "F_HERSTELLER_NAME";
	// F_KUNDEARTIKELNR: Kundeartikelnummer aus aktuell gueltiger Kunde -
	// Sonderkondition.
	public final static String F_KUNDEARTIKELNR = "F_KUNDEARTIKELNR";
	public final static String F_KUNDEARTIKELBEZEICHNUNG = "F_KUNDEARTIKELBEZEICHNUNG";
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
	public final static String P_SQLEXEC = "P_SQLEXEC";
	public final static String P_SUBREPORTCONNECTION = "P_SUBREPORTCONNECTION";
	public final static String P_REPORT_DATENSAETZE = "P_REPORT_DATENSAETZE";
	public final static String P_KPI_VARIABLEN = "P_KPI_VARIABLEN";

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
	public final static String P_VERTRETERTELEFON = "P_VERTRETERTELEFON";
	public final static String P_VERTRETER_TELEFON_FIRMA_MIT_DW = "P_VERTRETER_TELEFON_FIRMA_MIT_DW";
	public final static String P_VERTRETER2_TELEFON_FIRMA_MIT_DW = "P_VERTRETER2_TELEFON_FIRMA_MIT_DW";
	public final static String P_AUFTRAGEIGENSCHAFT_FA = "AUFTRAGEIGENSCHAFT_FA";
	public final static String P_AUFTRAGEIGENSCHAFT_CLUSTER = "AUFTRAGEIGENSCHAFT_CLUSTER";
	public final static String P_AUFTRAGEIGENSCHAFT_EQNR = "AUFTRAGEIGENSCHAFT_EQNR";
	public final static String P_ARTIKEL_BREITE = "P_ARTIKEL_BREITE";
	public final static String P_ARTIKEL_HOEHE = "P_ARTIKEL_HOEHE";
	public final static String P_ARTIKEL_TIEFE = "P_ARTIKEL_TIEFE";
	public final static String P_STUECKLISTENEIGENSCHAFT_INDEX = "P_STUECKLISTENEIGENSCHAFT_INDEX";
	public final static String P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ = "P_STUECKLISTENEIGENSCHAFT_MATERIALPLATZ";

	public final static String P_LAENDERART_UEBERSTEUERT = "P_LAENDERART_UEBERSTEUERT";

	// Allgemeine Reports
	public final static String REPORT_GANZSEITIGESBILD = "ganzseitigesbild.jasper";

	public final static String REPORT_AGB_PDF = "agb_pdf.jasper";

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
	 * @param parameterI   Map
	 * @param modul        String
	 * @param reportname   String
	 * @param mandantCNr   String
	 * @param sprache      Locale
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI, String modul, String reportname, String mandantCNr,
			Locale sprache, TheClientDto theClientDto) throws EJBExceptionLP {
		initJRDS(parameterI, modul, reportname, mandantCNr, sprache, theClientDto, true, null);
	}

	/**
	 * 
	 * @param parameterI      Map
	 * @param modul           String
	 * @param reportname      String
	 * @param mandantCNr      String
	 * @param sprache         Locale
	 * @param theClientDto    String
	 * @param bMitLogo        boolean
	 * @param kostenstelleIId Integer: Kostenstellenspezifisches Reportverzeichnis
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI, String modul, String reportname, String mandantCNr,
			Locale sprache, TheClientDto theClientDto, boolean bMitLogo, Integer kostenstelleIId)
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("parameterI == null"));
			}
			// Kostenstellenspezifisches Reportverzeichnis (optional)
			String cSubdirectory = getReportSubdirectoryByKostenstelleIId(kostenstelleIId);

			// PJ 15185
			if (theClientDto.getReportvarianteIId() != null) {
				ReportvarianteDto rvpDto = getDruckerFac()
						.reportvarianteFindByPrimaryKey(theClientDto.getReportvarianteIId());
				if (rvpDto.getCReportname().equals(reportname)) {
					reportname = rvpDto.getCReportnamevariante();
					// theClientDto.setReportvarianteIId(null);
				}
			}

			parameterI.put("P_SUBDIRECTORY", cSubdirectory);
			parameterI.put("P_MODUL", modul);

			// Verzeichnis, in dem der Report liegt
			String reportdir = SystemServicesFacBean.getPathFromLPDir(modul, reportname, mandantCNr, sprache,
					cSubdirectory, theClientDto);
			if (reportdir == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
						new Exception("reportdir == null: report=" + modul + "|" + reportname));
			}

			// Verzeichnis, in dem der Report liegt
			String reportdirDemoWasserzeichen = SystemServicesFacBean.getPathFromLPDir("allgemein",
					"demo_wasserzeichen.jasper", mandantCNr, sprache, cSubdirectory, theClientDto);

			// Report Locale
			parameterI.put("REPORT_LOCALE", sprache);
			parameterI.put(P_REPORT_INFORMATION, getReportInformation(reportdir, theClientDto));
			// Mandant
			parameterI.put("MANDANT_C_NR", mandantCNr);
			File f = new File(reportdir);
			// Reportverzeichnis
			parameterI.put("REPORT_DIRECTORY", reportdir.substring(0, reportdir.length() - f.getName().length()));
			parameterI.put(P_MANDANT_OBJ, getMandantFac().createReportMandantDto(theClientDto, sprache));
			// Report Root Verzeichnis
			parameterI.put("REPORT_ROOT_DIRECTORY", new java.io.File(getReportDir()).getAbsolutePath());
			// per Mandantenparameter kann das Logo auch IMMER drauf sein
			ParametermandantDto parameter = getParameterFac().getMandantparameter(mandantCNr,
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_LOGO_IMMER_DRUCKEN);
			short iValue = Short.parseShort(parameter.getCWert());
			boolean bLogoImmerDrucken = Helper.short2boolean(iValue);
			parameterI.put("P_LOGO_IMAGE", SystemServicesFacBean.getPathFromLPDir("allgemein", "logo.png", mandantCNr,
					sprache, cSubdirectory, theClientDto));
			parameterI.put("P_MITLOGO", new Boolean(bMitLogo || bLogoImmerDrucken));

			parameterI.put("P_LOGO_SUBREPORT", SystemServicesFacBean.getPathFromLPDir("allgemein", "logo.jasper",
					mandantCNr, sprache, cSubdirectory, theClientDto));
			parameterI.put("P_FUSSZEILEN_SUBREPORT", SystemServicesFacBean.getPathFromLPDir("allgemein", "fuss.jasper",
					mandantCNr, sprache, cSubdirectory, theClientDto));

			JasperReport jasperReportWasserzeichen = null;

			// java.sql.Connection sqlcon;
			//
			// try {
			// String url = getParameterFac().getAnwenderparameter(
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL)
			// .getCWert();
			// // "jdbc:postgresql://" + "localhost" + "/" + "LP",
			// if (url.trim().length() > 0) {
			// if (url.contains("postgresql")) {
			// Class.forName("org.postgresql.Driver");
			// }
			// if (url.contains("jtds")) {
			// Class.forName("net.sourceforge.jtds.jdbc.Driver");
			// }
			// sqlcon = java.sql.DriverManager.getConnection(url,
			// "hvguest", "h4gzfdavfs");
			// parameterI.put("P_SUBREPORTCONNECTION", sqlcon);
			// }
			// } catch (SQLException ex) {
			// System.out.println(ex.getMessage());
			// myLogger.error(ex.getMessage(), ex);
			// } catch (ClassNotFoundException ex) {
			// System.out.println(ex.getMessage());
			// myLogger.error(ex.getMessage(), ex);
			// }

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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN, ex);
				} else if (eCause instanceof InvalidClassException) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION, ex);
				} else {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
				}
			}

			// Report fuellen

			index = -1;

			MandantDto mandanDto = getMandantFac().mandantFindByPrimaryKey(mandantCNr, theClientDto);

			ReportSqlExecutor sqlExecutor = null;
			try {
				Connection c = getReportConnectionFacLocal()
//						.getConnection(theClientDto.getMandant());
						.getConnection(theClientDto.getIDUser());
//				myLogger.warn("got connection '" + c.hashCode() + "' -> '" + c.toString());
				sqlExecutor = new ReportSqlExecutor(c);
				parameterI.put(P_SQLEXEC, sqlExecutor);
				parameterI.put(P_SUBREPORTCONNECTION, c);
			} catch (SQLException e) {
				myLogger.warn("Beim Initialisieren der Report_Connection_Url gab es den Fehler: ", e);
			}

			int iAnzahl = 0;
			try {
				while (next() == true) {
					iAnzahl++;
				}

				parameterI.put(P_REPORT_DATENSAETZE, iAnzahl);

			} catch (JRException e) {
				index = -1;
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
			}
			index = -1;

			try {
				// Connection c = reportConnectionFacBean
				// .getConnection(theClientDto.getMandant());
				// sqlExecutor = new ReportSqlExecutor(c);
				// parameterI.put(P_SQLEXEC, sqlExecutor);
				// parameterI.put(P_SUBREPORTCONNECTION, c);

				// SP6875 Image_Dpi auf 600 setzen wegen OpenJDK Chart Problem im Querformat
				JasperReportsContext context = DefaultJasperReportsContext.getInstance();
				JRPropertiesUtil.getInstance(context).setProperty(Renderable.PROPERTY_IMAGE_DPI, "600");
				JRPropertiesUtil.getInstance(context)
						.setProperty("net.sf.jasperreports.chart.pie.ignore.duplicated.key", "true");

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterI, this);

				if (jasperReportWasserzeichen != null && Helper.short2boolean(mandanDto.getBDemo())) {

					try {
						// PJ 14141
						JasperPrint jasperPrintDemoWasserzeichen = JasperFillManager
								.fillReport(jasperReportWasserzeichen, parameterI, this);

						JRPrintElement bild = null;

						Iterator<?> iterator = jasperPrintDemoWasserzeichen.getPages().iterator();
						if (iterator.hasNext()) {
							JRPrintPage zeitdaten = (JRPrintPage) iterator.next();
							if (zeitdaten.getElements() != null && zeitdaten.getElements().size() > 0) {
								bild = (JRPrintElement) zeitdaten.getElements().get(0);
							}

						}

						Iterator<?> iterator2 = jasperPrint.getPages().iterator();
						int iPage = 0;
						while (iterator2.hasNext()) {
							JRPrintPage x = (JRPrintPage) iterator2.next();
							x.addElement(bild);
							jasperPrint.getPages().set(iPage, x);
							iPage++;
						}

					} catch (Exception e) {
						System.out.println("Exception e:" + e.getMessage());
						// dann auslassen
					}
				}

				ReportPatch reportPatch = new ReportPatch(jasperPrint);
				reportPatch.apply();
				jasperPrintLP.setPrintLP(jasperPrint);
			} catch (JRException ex) {
				// File not found abfangen, damit eine eigene meldung kommt
				// tritt z.b. auf, wenn ein subreport fehlt
				if (ex.getCause() instanceof FileNotFoundException) {
					EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND, ex);
					ArrayList<Object> a = new ArrayList<Object>();
					a.add(ex.getMessage());
					e.setAlInfoForTheClient(a);
					throw e;
				} else {
					if (ex.getCause() instanceof EJBExceptionLP) {
						// Jasper hat die EJBException gewrapped -> EJBException weitergeben
						throw (EJBExceptionLP) ex.getCause();
					} else {
						EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
						ArrayList<Object> a = new ArrayList<Object>();
						a.add(reportdir);
						a.add(ex.getMessage());
						e.setAlInfoForTheClient(a);
						throw e;
					}
				}
			} catch (JRRuntimeException ex) {
				if (ex instanceof JRFontNotFoundException) {
					throw EJBExcFactory.fontNichtGefunden((JRFontNotFoundException) ex);
				}
				EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(reportdir);
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
				// } catch (SQLException e) {
				// myLogger.error("SQLException:", e);
			} finally {
				if (sqlExecutor != null) {
					sqlExecutor.close();
					try {
						getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
					} catch (SQLException e) {
						myLogger.error("Closing physical connection", e);
					}
				}
			}

			// CSV-Daten mitgeben

			index = -1;

			// System.out.println(this.hashCode()+ " " + reportname);

			ArrayList al = new ArrayList();

			// Zuerst Ueberschrift
			int fieldLength = jasperReport.getFields() == null ? 0 : jasperReport.getFields().length;
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

						Object o = this.getFieldValue(jasperReport.getFields()[j]);

						if (o != null && (o instanceof Image || o instanceof Map)) {
							// Bilder und HashMap auslassen
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

			Object[][] rohdaten = (Object[][]) al.toArray(returnArray);

			// Subreports entfernen
			for (int i = 0; i < rohdaten.length; i++) {
				Object[] rohdatenZeile = rohdaten[i];
				if (rohdatenZeile != null) {
					for (int j = 0; j < rohdatenZeile.length; j++) {

						if (rohdatenZeile[j] instanceof LPDatenSubreport) {
							rohdatenZeile[j] = null;
						}
						if (rohdatenZeile[j] instanceof PositionRpt) {
							rohdatenZeile[j] = null;
						}

					}
				}
			}

			jasperPrintLP.setDatenMitUeberschrift(rohdaten);

			// Name ergaenzen, damit er am client richtig angezeigt werden kann
			/**
			 * @todo MB->MB hart codiertes raus!!! PJ 4368
			 */
			String s = "helium" + File.separator + "report" + File.separator;
			int index = reportdir.indexOf(s);
			String reportName = reportdir.substring(index + s.length());
			jasperPrintLP.setSReportName(reportName);
			jasperPrintLP.setBMitLogo(bMitLogo);

			jasperPrintLP.setMapParameters(filterParameters(parameterI));
			// jasperPrintLP.setMapParameters(parameterI);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * @param kostenstelleIId
	 * @return
	 */
	private String getReportSubdirectoryByKostenstelleIId(Integer kostenstelleIId) {
		if (kostenstelleIId == null)
			return null;

		// Kostenstellenspezifisches Reportverzeichnis (optional)
		KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kostenstelleIId);
		if (kstDto.getCSubdirectory() != null && kstDto.getCSubdirectory().length() > 0) {
			return kstDto.getCSubdirectory();
		}

		return null;
	}

	/**
	 * Liefert das Resultat des Zusammenf&uuml;hrens des &uuml;bergebenen Reports
	 * und des in den Reportverzeichnissen hinterlegten Kopienreports. Dieser wird
	 * nach g&auml;ngiger Reportvorgehensweise gesucht nach dem Parameter modul.
	 * Zuletzt wird im Ordner allgemein gesucht. Wird kein Kopienreport gefunden,
	 * wird der originale wieder zur&uuml;ckgegeben.<br \> Beim Zusammenf&uuml;hren
	 * werden alle Printelemente des Kopienreports (der ersten Seite) in den
	 * originalen hinzugef&uuml;gt.
	 * 
	 * @param jrPrint      jener JasperPrint Report, der mit dem Kopienreport
	 *                     zusammengef&uuml;hrt werden soll
	 * @param modul        gibt das Modul an, in dem der abgelegte Kopienreport
	 *                     zuerst gesucht werden soll
	 * @param theClientDto
	 * @return
	 */
	protected List<JRPrintElement> getReportCopy(JasperPrint jrPrint, String modul, TheClientDto theClientDto) {
		String reportDirKopien = SystemServicesFacBean.getCopyPrintPathFromLPDir(modul, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);
		JasperReport reportKopien = null;
		try {
			if (reportDirKopien != null) {
				reportKopien = (JasperReport) JRLoader.loadObjectFromFile(reportDirKopien);
			}
		} catch (Exception e) {
		}

		if (reportKopien == null)
			return null;

		Map<String, Object> parameterI = new HashMap<String, Object>();
		parameterI.put("REPORT_LOCALE", theClientDto.getLocUi());
		// Mandant
		parameterI.put("MANDANT_C_NR", theClientDto.getMandant());
		File f = new File(reportDirKopien);
		// Reportverzeichnis
		parameterI.put("REPORT_DIRECTORY",
				reportDirKopien.substring(0, reportDirKopien.length() - f.getName().length()));
		parameterI.put(P_MANDANT_OBJ,
				getMandantFac().createReportMandantDto(theClientDto, Locale.forLanguageTag(jrPrint.getLocaleCode())));
		// Report Root Verzeichnis
		parameterI.put("REPORT_ROOT_DIRECTORY", new java.io.File(getReportDir()).getAbsolutePath());
		ReportSqlExecutor sqlExecutor = null;
		try {
			Connection c = getReportConnectionFacLocal().getConnection(theClientDto.getIDUser());
//					.getConnection(theClientDto.getMandant());
//			myLogger.warn("got connection '" + c.hashCode() + "' -> '" + c.toString());
			sqlExecutor = new ReportSqlExecutor(c);
			parameterI.put(P_SQLEXEC, sqlExecutor);
			parameterI.put(P_SUBREPORTCONNECTION, c);
		} catch (SQLException e) {
			myLogger.warn("Beim Initialisieren der Report_Connection_Url gab es den Fehler: ", e);
			return null;
		}
		try {
			JasperPrint printKopien = JasperFillManager.fillReport(reportKopien, parameterI, this);

			if (printKopien.getPages().size() < 1)
				return null;

			List<JRPrintElement> elementsToCopy = printKopien.getPages().get(0).getElements();
			if (elementsToCopy.size() < 1)
				return null;

			return elementsToCopy;

		} catch (Exception e) {
			myLogger.warn("Elemente des Kopienreports konnten nicht erfolgreich in Originalreport" + " kopiert werden",
					e);
		} finally {
			if (sqlExecutor != null) {
				sqlExecutor.close();
				try {
					getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
				} catch (SQLException e) {
					myLogger.error("Closing physical connection", e);
				}
			}
		}

		return null;
	}

	private Map<String, Object> filterParameters(Map<String, Object> map) {
		Map<String, Object> m = new HashMap<String, Object>();
		for (Entry<String, Object> entry : map.entrySet()) {

			if ("P_SQLEXEC".equals(entry.getKey()))
				continue;
			if (entry.getKey().startsWith("P_SUBREPORT"))
				continue;

			if (entry.getValue() != null && entry.getValue() instanceof BufferedImage)
				continue;
			// if("P_SUBREPORTCONNECTION".equals(entry.getKey())) continue ;

			if (entry.getKey().startsWith("P_")) {
				m.put(entry.getKey(), entry.getValue());
			} else {
				if (entry.getValue() != null && entry.getValue() instanceof String) {
					m.put(entry.getKey(), entry.getValue());
				}
			}

		}

		return m;
	}

	/**
	 * 
	 * @param parameterI   Map
	 * @param jasperReport
	 * @param reportDir
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	protected final void initJRDS(Map<String, Object> parameterI, JasperReport jasperReport, String reportDir,
			TheClientDto theClientDto) throws EJBExceptionLP {
		jasperPrintLP = new JasperPrintLP();
		if (parameterI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("parameterI == null"));
		}

		parameterI.put(P_REPORT_INFORMATION, getReportInformation(reportDir, theClientDto));
		File f = new File(reportDir);
		parameterI.put("REPORT_DIRECTORY", reportDir.substring(0, reportDir.length() - f.getName().length()));
		parameterI.put(P_MANDANT_OBJ, getMandantFac().createReportMandantDto(theClientDto, null));
		// Report Root Verzeichnis
		parameterI.put("REPORT_ROOT_DIRECTORY", new java.io.File(getReportDir()).getAbsolutePath());

		// SP5507
		ReportSqlExecutor sqlExecutor = null;
		try {
			Connection c = getReportConnectionFacLocal().getConnection(theClientDto.getIDUser());
//			Connection c = getReportConnectionFacLocal()
//					.getConnection(theClientDto.getMandant());
//			myLogger.warn("got connection '" + c.hashCode() + "' -> '" + c.toString());
			sqlExecutor = new ReportSqlExecutor(c);
			parameterI.put(P_SQLEXEC, sqlExecutor);
			parameterI.put(P_SUBREPORTCONNECTION, c);
		} catch (SQLException e) {
			myLogger.warn("Beim Initialisieren der Report_Connection_Url gab es den Fehler: ", e);
		}

		int iAnzahl = -1;
		try {
			while (next() == true) {
				iAnzahl++;
			}

			parameterI.put(P_REPORT_DATENSAETZE, iAnzahl);

		} catch (JRException e) {
			index = -1;
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, e);
		}
		index = -1;

		// Kostenstellenspezifisches Reportverzeichnis (optional)
		// Report fuellen
		try {

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterI, this);
			jasperPrintLP.setPrintLP(jasperPrint);
		} catch (JRException ex) {
			// File not found abfangen, damit eine eigene meldung kommt
			// tritt z.b. auf, wenn ein subreport fehlt
			if (ex.getCause() != null && ex.getCause() instanceof FileNotFoundException) {
				EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
			} else {
				EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
				ArrayList<Object> a = new ArrayList<Object>();
				a.add("");
				a.add(ex.getMessage());
				e.setAlInfoForTheClient(a);
				throw e;
			}
		} catch (JRRuntimeException ex) {
			EJBExceptionLP e = new EJBExceptionLP(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, ex);
			ArrayList<Object> a = new ArrayList<Object>();
			a.add("");
			a.add(ex.getMessage());
			e.setAlInfoForTheClient(a);
			throw e;
		} finally {
			if (sqlExecutor != null) {
				sqlExecutor.close();
				try {
					getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
				} catch (SQLException e) {
					myLogger.error("Closing physical connection", e);
				}
			}
		}

		// Name ergaenzen, damit er am client richtig angezeigt werden kann
		// CSV-Daten mitgeben

		index = -1;

		// System.out.println(this.hashCode()+ " " + reportname);

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
		Object[][] returnArray = new Object[jasperReport.getFields().length][al.size()];

		Object[][] rohdaten = (Object[][]) al.toArray(returnArray);

		// Subreports entfernen
		for (int i = 0; i < rohdaten.length; i++) {
			Object[] rohdatenZeile = rohdaten[i];
			if (rohdatenZeile != null) {
				for (int j = 0; j < rohdatenZeile.length; j++) {

					if (rohdatenZeile[j] instanceof LPDatenSubreport) {
						rohdatenZeile[j] = null;
					}
				}
			}
		}

		jasperPrintLP.setDatenMitUeberschrift(rohdaten);
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

	protected String getLieferartOrt(Integer lieferartIId, String lieferortAusKonditionen,
			PartnerDto partnerDtoLieferadresse, TheClientDto theClientDto) throws RemoteException {
		// PJ19705
		LieferartDto lieferartDto = getLocaleFac().lieferartFindByPrimaryKey(lieferartIId, theClientDto);

		if (lieferartDto.getILieferort().equals(LocaleFac.LIEFERART_LIEFERORT_AUS_KONDITIONEN)) {
			return lieferortAusKonditionen;
		} else {

			LandplzortDto landplzortDto = null;
			if (lieferartDto.getILieferort().equals(LocaleFac.LIEFERART_LIEFERORT_AUS_MANDANTENADRESSE)) {
				MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

				landplzortDto = mDto.getPartnerDto().getLandplzortDto();

			} else if (lieferartDto.getILieferort().equals(LocaleFac.LIEFERART_LIEFERORT_AUS_LIEFERADRESSE)) {

				landplzortDto = partnerDtoLieferadresse.getLandplzortDto();
			}
			if (landplzortDto != null) {
				return landplzortDto.getLandDto().getCLkz() + " " + landplzortDto.getCPlz() + " "
						+ landplzortDto.getOrtDto().getCName();
			} else {
				return null;
			}

		}
	}

	protected String getXML() {
		String s = null;
		try {
			s = JasperExportManager.exportReportToXml(this.jasperPrintLP.getPrint());
		} catch (Throwable t) {
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */

		}
		return s;
	}

	protected void writeHtml(String filename) {
		try {
			JasperExportManager.exportReportToHtmlFile(this.jasperPrintLP.getPrint(), filename);
		} catch (Throwable t) {
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new
			 *       Exception(t));
			 */

		}
	}

	/**
	 * Bauen der ReportInformation zur optionalen Anzeige am Report.
	 * 
	 * @param filename     String
	 * @param theClientDto String
	 * @return String
	 */
	private String getReportInformation(String filename, TheClientDto theClientDto) {
		StringBuffer reportInformation = new StringBuffer();
		PersonalDto userPersonal = null;
		try {
			Locale locale = theClientDto.getLocUi();
			userPersonal = getPersonalFac().personalFindByPrimaryKeySmall(theClientDto.getIDPersonal());
			String sKurzzeichen;
			if (userPersonal.getCKurzzeichen() != null) {
				sKurzzeichen = userPersonal.getCKurzzeichen();
			} else {
				sKurzzeichen = "";
			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
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
			reportInformation.append(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, locale)
					.format(new java.sql.Date(System.currentTimeMillis())));
		} catch (Throwable t) {
			/**
			 * @todo throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
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
		return ServerConfiguration.getReportDir();
	}

	/**
	 * Aufbereiten eines Mediastandards zum Ausdruck auf einem Beleg.
	 * 
	 * @param mediastandardDto MediastandardDto
	 * @param theClientDto     String
	 * @return BelegPositionDruckTextbausteinDto
	 * @throws EJBExceptionLP
	 */
	public static BelegPositionDruckTextbausteinDto printTextbaustein(MediastandardDto mediastandardDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BelegPositionDruckTextbausteinDto dto = new BelegPositionDruckTextbausteinDto();
		if (mediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			dto.setSFreierText(mediastandardDto.getOMediaText());
		} else if (mediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
				|| mediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
				|| mediastandardDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
			BufferedImage image = Helper.byteArrayToImage(mediastandardDto.getOMediaImage());
			dto.setOImage(image);
		}
		return dto;
	}

	/**
	 * Aufbereiten einer Ident-Belegposition zum Ausdruck auf einem Beleg.
	 * 
	 * @param belegpositionDto BelegpositionDto
	 * @param belegartCNr      String
	 * @param artikelDto       MediastandardDto
	 * @param locale           Locale
	 * @param theClientDto     String
	 * @return BelegPositionDruckIdentDto
	 */

	public BelegPositionDruckIdentDto printIdent(BelegpositionDto belegpositionDto, String belegartCNr,
			ArtikelDto artikelDto, Locale locale, TheClientDto theClientDto) {
		return printIdent(belegpositionDto, belegartCNr, artikelDto, locale, null, theClientDto);
	}

	public void duplizierePositionenWegenZusammenfassung(BelegDto belegDto, ArrayList<Object> dataList,
			int iSpaltePositionsart, int iSpalteSeitenumbruch) {
		if (Helper.short2boolean(belegDto.getBMitzusammenfassung())) {
			if (dataList != null && dataList.size() > 0) {
				ArrayList dataListHelp = (ArrayList) dataList.clone();
				Object[] oZeileZusammenfassung = ((Object[]) dataList.get(0)).clone();
				oZeileZusammenfassung = new Object[oZeileZusammenfassung.length];
				oZeileZusammenfassung[iSpaltePositionsart] = LocaleFac.POSITIONSART_ZUSAMMENFASSUNG_NUR_REPORT;
				oZeileZusammenfassung[iSpalteSeitenumbruch] = ((Object[]) dataList.get(0))[iSpalteSeitenumbruch];
				dataList.add(oZeileZusammenfassung);

				for (int i = 0; i < dataListHelp.size(); i++) {
					dataList.add(dataListHelp.get(i));
				}
			}
		}
	}

	public Object[][] duplizierePositionenWegenZusammenfassung(BelegDto belegDto, Object[][] data,
			int iSpaltePositionsart, int iSpalteSeitenumbruch) {
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
				oZeileZusammenfassung[iSpalteSeitenumbruch] = ((Object[]) data[0])[iSpalteSeitenumbruch];
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

	public LPDatenSubreport getSubreportBelegartmedia(Integer usecaseId, Integer iKey, TheClientDto theClientDto) {

		ArrayList<BelegartmediaDto> alDaten = getBelegartmediaFac().getBelegartMediaDtos(usecaseId, iKey, theClientDto);

		String[] fieldnames = new String[] { "F_MIMETYPE", "F_BILD", "F_KOMMENTAR", "F_AUSRICHTUNG" };

		int iFeld_Subreport_Mimetype = 0;
		int iFeld_Subreport_Bild = 1;
		int iFeld_Subreport_Kommentar = 2;
		int iFeld_Subreport_Ausrichtung = 3;
		int iFeld_Subreport_iAnzahlSpalten = 4;

		if (alDaten != null && alDaten.size() > 0) {

			ArrayList<Object[]> subreportBelegartmedia = new ArrayList<Object[]>();

			for (int j = 0; j < alDaten.size(); j++) {

				BelegartmediaDto bmDto = alDaten.get(j);
				if (bmDto.getOMedia() != null) {

					Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];

					oZeileVorlage[iFeld_Subreport_Mimetype] = bmDto.getDatenformatCNr();
					oZeileVorlage[iFeld_Subreport_Kommentar] = bmDto.getXText();
					oZeileVorlage[iFeld_Subreport_Ausrichtung] = bmDto.getIAusrichtung();

					// Text Kommentar
					if (bmDto.getDatenformatCNr().trim().equals(MediaFac.DATENFORMAT_MIMETYPEART_TEXT)) {

						Object[] oZeile = oZeileVorlage.clone();
						subreportBelegartmedia.add(oZeile);

					} else if (bmDto.getDatenformatCNr().trim().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| bmDto.getDatenformatCNr().trim().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| bmDto.getDatenformatCNr().trim().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
						byte[] bild = bmDto.getOMedia();
						if (bild != null) {
							java.awt.image.BufferedImage myImage = Helper.byteArrayToImage(bild);

							Object[] oZeile = oZeileVorlage.clone();
							oZeile[iFeld_Subreport_Bild] = myImage;

							subreportBelegartmedia.add(oZeile);

						}
					} else if (bmDto.getDatenformatCNr().trim().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

						byte[] bild = bmDto.getOMedia();

						java.awt.image.BufferedImage[] tiffs = Helper.tiffToImageArray(bild);
						if (tiffs != null) {
							for (int k = 0; k < tiffs.length; k++) {

								Object[] oZeile = oZeileVorlage.clone();
								oZeile[iFeld_Subreport_Bild] = tiffs[k];

								subreportBelegartmedia.add(oZeile);

							}
						}

					}

				}
			}

			Object[][] dataSub = new Object[subreportBelegartmedia.size()][fieldnames.length];
			dataSub = (Object[][]) subreportBelegartmedia.toArray(dataSub);

			return new LPDatenSubreport(dataSub, fieldnames);
		} else {
			return null;
		}

	}

	public LPDatenSubreport getSubreportArtikelkommentar(Integer artikelIId, String belegartCNr,
			TheClientDto theClientDto) {
		try {
			ArtikelkommentarDto[] artikelkommentarDto = getArtikelkommentarFac()
					.artikelkommentardruckFindByArtikelIIdBelegartCNr(artikelIId, belegartCNr,
							theClientDto.getLocUiAsString(), theClientDto);

			if (artikelkommentarDto != null && artikelkommentarDto.length > 0) {

				ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

				String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR", };

				int iFeld_Subreport_Kommentarart = 0;
				int iFeld_Subreport_Mimetype = 1;
				int iFeld_Subreport_Bild = 2;
				int iFeld_Subreport_Kommentar = 3;
				int iFeld_Subreport_iAnzahlSpalten = 4;

				for (int j = 0; j < artikelkommentarDto.length; j++) {
					if (artikelkommentarDto[j].getArtikelkommentarsprDto() != null) {

						Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];

						ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
								.artikelkommentarartFindByPrimaryKey(artikelkommentarDto[j].getArtikelkommentarartIId(),
										theClientDto);

						oZeileVorlage[iFeld_Subreport_Kommentarart] = artikelkommentarartDto.getCNr();

						oZeileVorlage[iFeld_Subreport_Mimetype] = artikelkommentarDto[j].getDatenformatCNr();
						oZeileVorlage[iFeld_Subreport_Kommentar] = artikelkommentarDto[j].getArtikelkommentarsprDto()
								.getXKommentar();

						// Text Kommentar
						if (artikelkommentarDto[j].getDatenformatCNr().trim()
								.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {

							Object[] oZeile = oZeileVorlage.clone();
							subreportArtikelkommentare.add(oZeile);

						} else if (artikelkommentarDto[j].getDatenformatCNr()
								.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
								|| artikelkommentarDto[j].getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
								|| artikelkommentarDto[j].getDatenformatCNr()
										.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
							byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();
							if (bild != null) {
								java.awt.Image myImage = Helper.byteArrayToImage(bild);

								Object[] oZeile = oZeileVorlage.clone();
								oZeile[iFeld_Subreport_Bild] = myImage;

								subreportArtikelkommentare.add(oZeile);

							}
						} else if (artikelkommentarDto[j].getDatenformatCNr()
								.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

							byte[] bild = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

							java.awt.Image[] tiffs = Helper.tiffToImageArray(bild);
							if (tiffs != null) {
								for (int k = 0; k < tiffs.length; k++) {
									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = tiffs[k];

									subreportArtikelkommentare.add(oZeile);
								}
							}

						} else if (artikelkommentarDto[j].getDatenformatCNr()
								.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

							byte[] pdf = artikelkommentarDto[j].getArtikelkommentarsprDto().getOMedia();

							PDDocument document = null;

							try {

								InputStream myInputStream = new ByteArrayInputStream(pdf);

								document = PDDocument.load(myInputStream);
								int numPages = document.getNumberOfPages();
								PDFRenderer renderer = new PDFRenderer(document);

								for (int p = 0; p < numPages; p++) {

									BufferedImage image = renderer.renderImageWithDPI(p, 150); // Windows
									Object[] oZeile = oZeileVorlage.clone();
									oZeile[iFeld_Subreport_Bild] = image;

									subreportArtikelkommentare.add(oZeile);

								}
							} catch (IOException e) {
								e.printStackTrace();
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

							} finally {
								if (document != null) {

									try {
										document.close();
									} catch (IOException e) {
										e.printStackTrace();
										throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

									}
								}

							}

						}
					}
				}

				Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
				dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

				return new LPDatenSubreport(dataSub, fieldnames);

			} else {
				return null;
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public LPDatenSubreport getSubreportPartnerkommentar(Integer partnerIId, String belegartCNr, boolean bKunde,
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT pkd FROM FLRPartnerkommentardruck pkd WHERE pkd.belegart_c_nr='" + belegartCNr
				+ "' AND pkd.flrpartnerkommentar.partner_i_id=" + partnerIId;

		if (bKunde == true) {
			sQuery += " AND pkd.flrpartnerkommentar.b_kunde=1 ";
		} else {
			sQuery += " AND pkd.flrpartnerkommentar.b_kunde=0 ";
		}

		sQuery += " ORDER BY  pkd.flrpartnerkommentar.i_sort ";

		org.hibernate.Query querylagerbewegungen = session.createQuery(sQuery);
		List<?> results = querylagerbewegungen.list();
		Iterator<?> resultListIterator = results.iterator();

		BigDecimal bdMenge = new BigDecimal(0);

		ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

		String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR", "F_HINWEIS", };

		int iFeld_Subreport_Kommentarart = 0;
		int iFeld_Subreport_Mimetype = 1;
		int iFeld_Subreport_Bild = 2;
		int iFeld_Subreport_Kommentar = 3;
		int iFeld_Subreport_Hinweis = 4;
		int iFeld_Subreport_iAnzahlSpalten = 5;

		while (resultListIterator.hasNext()) {
			FLRPartnerkommentardruck pkd = (FLRPartnerkommentardruck) resultListIterator.next();

			PartnerkommentarDto partnerkommentarDto = getPartnerServicesFac()
					.partnerkommentarFindByPrimaryKey(pkd.getFlrpartnerkommentar().getI_id(), theClientDto);

			Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];

			oZeileVorlage[iFeld_Subreport_Kommentarart] = pkd.getFlrpartnerkommentar().getFlrpartnerkommentarart()
					.getC_bez();

			oZeileVorlage[iFeld_Subreport_Mimetype] = partnerkommentarDto.getDatenformatCNr();
			oZeileVorlage[iFeld_Subreport_Kommentar] = partnerkommentarDto.getXKommentar();

			if (partnerkommentarDto.getIArt() == PartnerServicesFac.PARTNERKOMMENTARART_HINWEIS) {
				oZeileVorlage[iFeld_Subreport_Hinweis] = Boolean.TRUE;
			} else {
				oZeileVorlage[iFeld_Subreport_Hinweis] = Boolean.FALSE;
			}

			// Text Kommentar
			if (partnerkommentarDto.getDatenformatCNr().trim().indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {

				Object[] oZeile = oZeileVorlage.clone();
				subreportArtikelkommentare.add(oZeile);

			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
					|| partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
					|| partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
				byte[] bild = partnerkommentarDto.getOMedia();
				if (bild != null) {
					java.awt.Image myImage = Helper.byteArrayToImage(bild);

					Object[] oZeile = oZeileVorlage.clone();
					oZeile[iFeld_Subreport_Bild] = myImage;

					subreportArtikelkommentare.add(oZeile);

				}
			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

				byte[] bild = partnerkommentarDto.getOMedia();

				java.awt.Image[] tiffs = Helper.tiffToImageArray(bild);
				if (tiffs != null) {
					for (int k = 0; k < tiffs.length; k++) {
						Object[] oZeile = oZeileVorlage.clone();
						oZeile[iFeld_Subreport_Bild] = tiffs[k];

						subreportArtikelkommentare.add(oZeile);
					}
				}

			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {

				byte[] pdf = partnerkommentarDto.getOMedia();

				PDDocument document = null;

				try {

					InputStream myInputStream = new ByteArrayInputStream(pdf);

					document = PDDocument.load(myInputStream);
					int numPages = document.getNumberOfPages();
					PDFRenderer renderer = new PDFRenderer(document);

					for (int p = 0; p < numPages; p++) {

						BufferedImage image = renderer.renderImageWithDPI(p, 150); // Windows
						Object[] oZeile = oZeileVorlage.clone();
						oZeile[iFeld_Subreport_Bild] = image;

						subreportArtikelkommentare.add(oZeile);

					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

				} finally {
					if (document != null) {

						try {
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e.getMessage());

						}
					}

				}

			}

		}

		Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
		dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

		return new LPDatenSubreport(dataSub, fieldnames);

	}

	public BelegPositionDruckIdentDto printIdent(BelegpositionDto belegpositionDto, String belegartCNr,
			ArtikelDto artikelDto, Locale locale, Integer partnerIId, TheClientDto theClientDto) {
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
					artsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelDto.getIId(),
							Helper.locale2String(locale), theClientDto);
					/**
					 * @todo hier noch einfuegen: falls z.b. enUS nicht moeglich, dann enGB suchen
					 */
				}
				// 2. Wenn keine gefunden wurde, dann zieht die
				// Mandantensprache.
				if (artsprDto == null) {
					artsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelDto.getIId(),
							theClientDto.getLocMandantAsString(), theClientDto);
				}
				// 3. Wenn noch immer keine gefunden, dann in der Sprache des
				// Konzerns.
				if (artsprDto == null) {
					artsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelDto.getIId(),
							theClientDto.getLocKonzernAsString(), theClientDto);
				}
				// 4. Wenn eins gefunden wurde, dann an den Artikel haengen.
				if (artsprDto != null) {
					artikelDto.setArtikelsprDto(artsprDto);
				}

				StringBuffer sbArtikelInfo = new StringBuffer();
				if (!(artikelDto.getArtikelartCNr() == null)
						&& !artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					// Ident nur fuer "echte" Artikel
					dto.setSIdentnummer(artikelDto.getCNr());

					sbArtikelInfo.append(artikelDto.getCNr());
					sbArtikelInfo.append("\n");

					// Warenverkehrsnummer fuer Artikel
					// SP5307
					if (artikelDto.getCWarenverkehrsnummer() != null
							&& !artikelDto.getCWarenverkehrsnummer().equals(ArtikelFac.WARENVERKEHRSNUMMER_NULL)) {
						dto.setSWarenverkehrsnummer(artikelDto.getCWarenverkehrsnummer());
					}
					// Eccn
					dto.setSEccn(artikelDto.getCEccn());
					// Verpackungs und Verkaufs EAN Nr.
					dto.setSVerpackungseannr(artikelDto.getCVerpackungseannr());
					dto.setSVerkaufseannr(artikelDto.getCVerkaufseannr());
					dto.setSReferenznr(artikelDto.getCReferenznr());
				}

				// Artikel Zusatzbezeichnung 2
				if (artikelDto.getArtikelsprDto() != null) {
					dto.setSArtikelZusatzBezeichnung2(artikelDto.getArtikelsprDto().getCZbez2());
				}
				// Text
				if (belegpositionDto != null) {
					if (belegpositionDto.getXTextinhalt() != null) {
						dto.setSIdentTexteingabe(belegpositionDto.getXTextinhalt());
					}
				}

				// Bezeichnung
				String sBezeichnung;
				String sZusatzBezeichnung = null;
				// Bezeichnung des Artikels uebersteuert?
				if (belegpositionDto != null && (belegpositionDto.getBArtikelbezeichnunguebersteuert() == null
						|| Helper.short2boolean(belegpositionDto.getBArtikelbezeichnunguebersteuert()))) {
					if (belegpositionDto.getCBez() != null) {
						sBezeichnung = belegpositionDto.getCBez();
					} else {
						if (artikelDto.getArtikelsprDto() != null && artikelDto.getArtikelsprDto().getCBez() != null) {
							sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
						} else {
							// keine Bezeichnung gefunden.
							sBezeichnung = "";
						}
					}

					// Zusatzbezeichnung als eigenen Wert.
					if (belegpositionDto.getCZusatzbez() != null) {
						sZusatzBezeichnung = belegpositionDto.getCZusatzbez();
					} else {
						if (artikelDto.getArtikelsprDto() != null && artikelDto.getArtikelsprDto().getCZbez() != null) {
							sZusatzBezeichnung = artikelDto.getArtikelsprDto().getCZbez();
						}
					}
				}
				// nicht uebersteuert: Bezeichnung aus dem Artikel
				else {
					if (artikelDto.getArtikelsprDto() != null) {
						sBezeichnung = artikelDto.getArtikelsprDto().getCBez();
						if (artikelDto.getArtikelsprDto().getCZbez() != null) {
							sZusatzBezeichnung = artikelDto.getArtikelsprDto().getCZbez();
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

					// SP5647 Kommentar kommt nun immer aus Druck-Locale, wenn n
					// icht vorhanden, dann kommt das Konzern Locale
					String sKommentarLocale = Helper.locale2String(locale);

					ArtikelkommentarDto[] aKommentarDto = getArtikelkommentarFac()
							.artikelkommentardruckFindByArtikelIIdBelegartCNr(artikelDto.getIId(), belegartCNr,
									sKommentarLocale, theClientDto);
					if (aKommentarDto != null) {
						boolean bKommentarBranche = false;
						PartnerDto partnerDto = null;
						if (partnerIId != null) {
							partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
							ParametermandantDto parameter = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.PARAMETER_PARTNERBRANCHE_DEFINIERT_KOMMENTAR);

							if ((Boolean) parameter.getCWertAsObject() == true) {
								bKommentarBranche = true;
							}
						}

						for (int k = 0; k < aKommentarDto.length; k++) {
							String cDatenformat = aKommentarDto[k].getDatenformatCNr().trim();

							if (bKommentarBranche == true && partnerDto != null) {
								ArtikelkommentarartDto artikelkommentarartDto = getArtikelkommentarFac()
										.artikelkommentarartFindByPrimaryKey(
												aKommentarDto[k].getArtikelkommentarartIId(), theClientDto);

								if (partnerDto.getBrancheIId() == null
										&& artikelkommentarartDto.getBrancheId() != null) {
									continue;
								}
								if (artikelkommentarartDto.getBrancheId() == null
										|| !artikelkommentarartDto.getBrancheId().equals(partnerDto.getBrancheIId())) {

									continue;
								}
							}

							if (aKommentarDto[k].getArtikelkommentarsprDto() != null) {

								// Kommentar kann Text oder Bild sein
								if (cDatenformat.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {
									if (aKommentarDto[k].getArtikelkommentarsprDto() != null) {
										if (k > 0) {
											sArtikelKommentar += "\n";
										}
										sArtikelKommentar += aKommentarDto[k].getArtikelkommentarsprDto()
												.getXKommentar();
									}
								} else if (cDatenformat.indexOf(MediaFac.DATENFORMAT_MIMETYPEART_IMAGE) != -1) {
									// es wird hoechstens 1 Bild pro Belegart
									// gedruckt
									BufferedImage imageKommentar = Helper
											.byteArrayToImage(aKommentarDto[k].getArtikelkommentarsprDto().getOMedia());
									dto.setOImageKommentar(imageKommentar);
								}
							}
						}
					}
				}
				if (sBezeichnung != null) {
					sbArtikelInfo.append(Helper.formatSimpleTextForJasper(sBezeichnung));
				}
				// Bezeichnungen
				dto.setSBezeichnung(sBezeichnung);
				if (artikelDto.getArtikelsprDto() != null) {
					dto.setSKurzbezeichnung(artikelDto.getArtikelsprDto().getCKbez());
				}

				// Zusatzbezeichnung
				if (sZusatzBezeichnung != null) {
					dto.setSZusatzBezeichnung(sZusatzBezeichnung);
					sbArtikelInfo.append("\n" + Helper.formatSimpleTextForJasper(sZusatzBezeichnung));
				}

				// PJ 17267
				ParametermandantDto parameter = null;
				parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_ARTIKELLANGTEXTE_UEBERSTEUERBAR);

				boolean bArtikellangtexteUebersteuerbar = ((Boolean) parameter.getCWertAsObject()).booleanValue();

				if (bArtikellangtexteUebersteuerbar == true && belegpositionDto != null
						&& belegpositionDto.getXTextinhalt() != null
						&& belegpositionDto.getXTextinhalt().length() > 0) {

					dto.setSArtikelkommentar(belegpositionDto.getXTextinhalt());
					sbArtikelInfo.append("\n" + belegpositionDto.getXTextinhalt());

				} else {
					// Artikelkommentar
					if (sArtikelKommentar != null && !sArtikelKommentar.equals("")) {
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

	protected JasperPrint mergePrintsFirstToAllPages(JasperPrint target, JasperPrint source) {
		if (source.getPages().size() < 1)
			return target;

		List<JRPrintElement> elements = source.getPages().get(0).getElements();
		if (elements.size() < 1)
			return target;

		for (JRPrintPage page : target.getPages()) {
			for (JRPrintElement element : elements) {
				page.addElement(element);
			}
		}

		return target;
	}

	protected JasperPrint mergePrints(JasperPrint target, String modul, String reportname, TheClientDto theClientDto) {
		return mergePrints(target, modul, reportname, new HashMap<String, Object>(), theClientDto);
	}

	protected JasperPrint mergePrints(JasperPrint jrPrint, String modul, String reportname,
			Map<String, Object> sourceParameterI, TheClientDto theClientDto) {
		String reportDirSource = SystemServicesFacBean.getPathFromLPDir(modul, reportname, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);
		JasperReport reportSource = null;
		try {
			if (reportDirSource != null) {
				reportSource = (JasperReport) JRLoader.loadObjectFromFile(reportDirSource);
			}
		} catch (Exception e) {
		}

		if (reportSource == null)
			return jrPrint;

		Map<String, Object> parameterI = sourceParameterI != null ? sourceParameterI : new HashMap<String, Object>();
		parameterI.put("REPORT_LOCALE", theClientDto.getLocUi());
		// Mandant
		parameterI.put("MANDANT_C_NR", theClientDto.getMandant());
		File f = new File(reportDirSource);
		// Reportverzeichnis
		parameterI.put("REPORT_DIRECTORY",
				reportDirSource.substring(0, reportDirSource.length() - f.getName().length()));
		parameterI.put(P_MANDANT_OBJ,
				getMandantFac().createReportMandantDto(theClientDto, Locale.forLanguageTag(jrPrint.getLocaleCode())));
		// Report Root Verzeichnis
		parameterI.put("REPORT_ROOT_DIRECTORY", new java.io.File(getReportDir()).getAbsolutePath());
		ReportSqlExecutor sqlExecutor = null;
		try {
			Connection c = getReportConnectionFacLocal().getConnection(theClientDto.getIDUser());
//					.getConnection(theClientDto.getMandant());
//			myLogger.warn("got connection '" + c.hashCode() + "' -> '" + c.toString());
			sqlExecutor = new ReportSqlExecutor(c);
			parameterI.put(P_SQLEXEC, sqlExecutor);
			parameterI.put(P_SUBREPORTCONNECTION, c);
		} catch (SQLException e) {
			myLogger.warn("Beim Initialisieren der Report_Connection_Url gab es den Fehler: ", e);
		}

		try {
			JasperPrint printSource = JasperFillManager.fillReport(reportSource, parameterI, this);

			jrPrint = mergePrintsFirstToAllPages(jrPrint, printSource);
		} catch (Exception e) {
			myLogger.warn("Elemente des Reports '" + reportname
					+ "' konnten nicht erfolgreich in Originalreport kopiert werden", e);
		} finally {
			if (sqlExecutor != null) {
				sqlExecutor.close();
				try {
					getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
				} catch (SQLException e) {
					myLogger.error("Closing physical connection", e);
				}
			}
		}

		return jrPrint;
	}
}
