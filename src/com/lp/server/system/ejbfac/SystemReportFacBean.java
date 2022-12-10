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
package com.lp.server.system.ejbfac;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.fastlanereader.generated.FLRBelegart;
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MergePrintTypeParams;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.PositionRpt;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
@Interceptors(TimingInterceptor.class)
public class SystemReportFacBean extends LPReport implements SystemReportFac, SystemReportFacLocal, JRDataSource {

	private Object[][] data = null;
	private String sAktuellerReport = "";

	private static int REPORT_ENTITYLOG_ENTITY = 0;
	private static int REPORT_ENTITYLOG_OPERATION = 1;
	private static int REPORT_ENTITYLOG_I_ID = 2;
	private static int REPORT_ENTITYLOG_KEY = 3;
	private static int REPORT_ENTITYLOG_VON = 4;
	private static int REPORT_ENTITYLOG_NACH = 5;
	private static int REPORT_ENTITYLOG_LOCALE = 6;
	private static int REPORT_ENTITYLOG_PERSON = 7;
	private static int REPORT_ENTITYLOG_ZEITPUNKT = 8;
	private static int REPORT_ENTITYLOG_ANZAHL_SPALTEN = 9;

	private static int REPORT_MODULBERECHTIGUNG_BELEGART = 0;
	private static int REPORT_MODULBERECHTIGUNG_BERECHTIGT = 1;
	private static int REPORT_MODULBERECHTIGUNG_ANZAHL_SPALTEN = 2;

	private static int REPORT_STATISTIK_VON = 0;
	private static int REPORT_STATISTIK_BIS = 1;
	private static int REPORT_STATISTIK_VORJAHR_VON = 2;
	private static int REPORT_STATISTIK_VORJAHR_BIS = 3;
	private static int REPORT_STATISTIK_ANZAHL_SPALTEN = 4;

	private static int REPORT_AGB_PDF_SEITE_ALS_IMAGE = 0;

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printUseCaseHandler(String uuid, QueryParameters q, int iAnzahlZeilen, String ueberschrift,
			int[] columnHeaderWidthsFromClientPerspective, TheClientDto theClientDto) {
		index = -1;

		sAktuellerReport = SystemFac.REPORTXML_FLRDRUCK;
		String reportdir = "";
		JasperReport jasperReport = null;

		String reportuebersteuert = SystemFac.REPORTXML_FLRDRUCK.replaceFirst(".jrxml", q.getUseCaseId() + ".jasper");

		reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein", reportuebersteuert, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);

		if (reportdir == null) {
			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein", SystemFac.REPORTXML_FLRDRUCK,
					theClientDto.getMandant(), theClientDto.getLocUi(), null, theClientDto);
		} else {
			try {
				jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportdir);
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
		}

		FastLaneReader fastLaneReader = getFastLaneReader();

		int pageSize = UseCaseHandler.PAGE_SIZE;
		try {

			HashMap hmParameter = new HashMap<Object, Object>();
			QueryResult qr = fastLaneReader.setQuery(uuid, q.getUseCaseId(), q, theClientDto);

			qr = fastLaneReader.getResults(uuid, q.getUseCaseId(), qr.getRowCount(), theClientDto);
			TableInfo ti = fastLaneReader.getTableInfo(uuid, q.getUseCaseId(), null, theClientDto);

			// SP5540
			int[] columnHeaderWidths = ti.getColumnHeaderWidths();

			if (columnHeaderWidths == null && columnHeaderWidthsFromClientPerspective != null) {
				columnHeaderWidths = columnHeaderWidthsFromClientPerspective;
			} else {

				if (columnHeaderWidthsFromClientPerspective != null
						&& (columnHeaderWidthsFromClientPerspective.length == columnHeaderWidths.length)) {
					columnHeaderWidths = columnHeaderWidthsFromClientPerspective;
				}
			}

			int iSpaltenAnzahl = ti.getColumnClasses().length;

			for (int i = 0; i < ti.getColumnClasses().length; i++) {
				if (ti.getColumnClasses()[i].equals(java.awt.Color.class)) {
					iSpaltenAnzahl--;
				}
			}

			if (jasperReport == null) {
				jasperReport = getJasperReport(iSpaltenAnzahl, columnHeaderWidths, ti.getColumnClasses(), reportdir,
						false);
			}
			// Spaltenueberschriften einfuegen
			for (int i = 0; i < iSpaltenAnzahl; i++) {
				hmParameter.put("P_SPALTE" + i, ti.getColumnHeaderValues()[i].toString());
			}

			hmParameter.put("P_UEBERSCHRIFT", ueberschrift);
			hmParameter.put("P_USECASE_ID", q.getUseCaseId() + "");

			// SP5565
			final String remove = "\t";

			data = new Object[(int) qr.getRowCount()][iSpaltenAnzahl];

			for (int x = 0; x < qr.getRowCount(); x++) {
				for (int i = 0; i < iSpaltenAnzahl; i++) {
					if (qr.getRowData()[x][i] instanceof BigDecimal
							&& ((BigDecimal) qr.getRowData()[x][i]).doubleValue() == 0) {
						continue;
					}

					Object zelle = qr.getRowData()[x][i];

					if (zelle != null && zelle instanceof String) {
						if (((String) zelle).contains(remove)) {
							zelle = ((String) zelle).replaceAll(remove, " ");
						}

					}

					data[x][i] = zelle;
				}
			}

			initJRDS(hmParameter, jasperReport, reportdir, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			UseCaseHandler.PAGE_SIZE = pageSize;
			fastLaneReader = null;
			/*
			 * try { //Session wieder schliessen fastLaneReader.remove(); } catch
			 * (RemoveException ex1) { ex1.printStackTrace(); } catch (RemoteException ex) {
			 * throwEJBExceptionLPRespectOld(ex); }
			 */
		}

		return getReportPrint();
	}

	public JasperPrintLP printModulberechtigungen(TheClientDto theClientDto) {

		sAktuellerReport = SystemReportFac.REPORT_MODULBERECHTIGUNG;

		HashMap hmParameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRBelegart.class);

		crit.addOrder(Order.asc("c_nr"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRBelegart flrBelegart = (FLRBelegart) resultListIterator.next();

			Object[] zeile = new Object[REPORT_MODULBERECHTIGUNG_ANZAHL_SPALTEN];

			zeile[REPORT_MODULBERECHTIGUNG_BELEGART] = flrBelegart.getC_nr();

			try {
				ModulberechtigungDto mbDto = getMandantFac()
						.modulberechtigungFindByPrimaryKeyOhneExc(flrBelegart.getC_nr(), theClientDto.getMandant());

				if (mbDto != null) {
					zeile[REPORT_MODULBERECHTIGUNG_BERECHTIGT] = Boolean.TRUE;
				} else {
					zeile[REPORT_MODULBERECHTIGUNG_BERECHTIGT] = Boolean.FALSE;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_MODULBERECHTIGUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(hmParameter, SystemReportFac.REPORT_MODUL, SystemReportFac.REPORT_MODULBERECHTIGUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printEntitylog(String filterKey, String filterId, String cDatensatz,
			TheClientDto theClientDto) {

		sAktuellerReport = SystemReportFac.REPORT_ENTITYLOG;

		HashMap hmParameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session.createCriteria(FLREntitylog.class);
		if (filterKey != null) {
			crit.add(Restrictions.eq("c_filter_key", filterKey));
			hmParameter.put("P_FILTER_KEY", filterKey);
		}
		if (filterId != null) {
			crit.add(Restrictions.eq("filter_i_id", filterId));
			hmParameter.put("P_FILTER_I_ID", filterId);
		}

		hmParameter.put("P_DATENSATZ", cDatensatz);

		crit.addOrder(Order.desc("t_aendern"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList alDaten = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLREntitylog flrEntitylog = (FLREntitylog) resultListIterator.next();

			Object[] zeile = new Object[REPORT_ENTITYLOG_ANZAHL_SPALTEN];

			zeile[REPORT_ENTITYLOG_ENTITY] = flrEntitylog.getC_entity_key();
			zeile[REPORT_ENTITYLOG_I_ID] = flrEntitylog.getEntity_i_id();
			zeile[REPORT_ENTITYLOG_KEY] = flrEntitylog.getC_key();
			zeile[REPORT_ENTITYLOG_LOCALE] = flrEntitylog.getLocale_c_nr();
			zeile[REPORT_ENTITYLOG_NACH] = flrEntitylog.getC_nach();
			zeile[REPORT_ENTITYLOG_OPERATION] = flrEntitylog.getC_operation();
			zeile[REPORT_ENTITYLOG_PERSON] = flrEntitylog.getFlrpersonal().getC_kurzzeichen();
			zeile[REPORT_ENTITYLOG_VON] = flrEntitylog.getC_von();
			zeile[REPORT_ENTITYLOG_ZEITPUNKT] = flrEntitylog.getT_aendern();

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_ENTITYLOG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(hmParameter, SystemReportFac.REPORT_MODUL, SystemReportFac.REPORT_ENTITYLOG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printExtraliste(ExtralisteRueckgabeTabelleDto extralisteDto, Integer extralisteIId,
			TheClientDto theClientDto) {
		index = -1;

		sAktuellerReport = SystemFac.REPORTXML_FLRDRUCK;

		String reportdir = null;
		JasperReport jasperReport = null;

		HashMap hmParameter = new HashMap<Object, Object>();

		hmParameter.put("P_USECASE_ID", "EL" + extralisteIId);

		String reportuebersteuert = SystemFac.REPORTXML_FLRDRUCK.replaceFirst(".jrxml",
				"EL" + extralisteIId + ".jasper");

		reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein", reportuebersteuert, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);

		if (reportdir == null) {
			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein", SystemFac.REPORTXML_FLRDRUCK,
					theClientDto.getMandant(), theClientDto.getLocUi(), null, theClientDto);
		}

		int iSpaltenAnzahl = extralisteDto.getColumnNames().length;

		if (jasperReport == null) {
			jasperReport = getJasperReport(iSpaltenAnzahl, extralisteDto.getColumnWidths(),
					extralisteDto.getColumnClasses(), reportdir, true);
		}
		// Spaltenueberschriften einfuegen
		for (int i = 0; i < iSpaltenAnzahl; i++) {
			hmParameter.put("P_SPALTE" + i, extralisteDto.getColumnNames()[i]);
		}
		data = new Object[(int) extralisteDto.getData().length][iSpaltenAnzahl];

		for (int x = 0; x < extralisteDto.getData().length; x++) {
			for (int i = 0; i < iSpaltenAnzahl; i++) {
				data[x][i] = extralisteDto.getData()[x][i];
			}
		}

		initJRDS(hmParameter, jasperReport, reportdir, theClientDto);

		return getReportPrint();
	}

	private JasperReport getJasperReport(int iSpaltenanzahl, int[] columnheaderWidths, Class[] spaltentypen,
			String reportDir, boolean bMitErsterSpalte) {
		byte[] b = null;
		try {
			b = Helper.getBytesFromFile(new File(reportDir));
		} catch (IOException ex3) {
			ex3.printStackTrace();
		}

		String s = new String(b);

		String parameter = "";
		String felder = "";
		String ueberschriften = "";
		String detail = "";

		int iXPosition = 0;
		int cols = iSpaltenanzahl;
		int iAnzahlShareWithRest = 0;
		int iBreiteShareWithRest = 50;

		int gesamtbreiteColumnheaderWidth = 0;

		if (columnheaderWidths != null) {
			for (int i = 0; i < cols; i++) {
				if (columnheaderWidths[i] != QueryParameters.FLR_BREITE_SHARE_WITH_REST
						&& columnheaderWidths[i] < REPORT_FLR_SPALTENBREITE_MIN) {
					columnheaderWidths[i] = REPORT_FLR_SPALTENBREITE_MIN;

				}

				if (columnheaderWidths[i] != QueryParameters.FLR_BREITE_SHARE_WITH_REST) {
					gesamtbreiteColumnheaderWidth += columnheaderWidths[i];
				}

			}
			for (int k = 1; k < cols; k++) {
				if (columnheaderWidths[k] == QueryParameters.FLR_BREITE_SHARE_WITH_REST) {
					iAnzahlShareWithRest++;
				}
			}

			int iTempBreite = 140;
			for (int k = 1; k < cols; k++) {
				if (columnheaderWidths[k] != QueryParameters.FLR_BREITE_SHARE_WITH_REST) {
					iTempBreite -= columnheaderWidths[k];

				}
			}
			if (iAnzahlShareWithRest != 0) {
				iBreiteShareWithRest = iTempBreite / iAnzahlShareWithRest;
			} else {
				iBreiteShareWithRest = iTempBreite;
			}

			if (iBreiteShareWithRest < 0) {
				iBreiteShareWithRest = REPORT_FLR_SPALTENBREITE_MIN;
			}

		}

		for (int i = 0; i < cols; i++) {

			if (bMitErsterSpalte == false && i == 0) {
				// erste Spalte auslassen
			} else {

				String pattern = "";
				String ausrichtung = "Left";
				Class<?> c = spaltentypen[i];

				if (spaltentypen[i].equals(java.math.BigDecimal.class)
						|| spaltentypen[i].equals(java.lang.Double.class)) {
					pattern = "#,##0.00";
					ausrichtung = "Right";
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal0.class)) {
					pattern = "#,##0";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal3.class)) {
					pattern = "#,##0.000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal4.class)) {
					pattern = "#,##0.0000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal5.class)) {
					pattern = "#,##0.00000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal6.class)) {
					pattern = "#,##0.00000"; // TODO warum sind hier nur 5! Nachkommastellen? ghp, 10.7.2019
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimal13.class)) {
					pattern = "#,##0.0000000000000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(com.lp.util.BigDecimalFinanz.class)) {
					pattern = "#,##0.00000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i].equals(java.sql.Timestamp.class)) {
					pattern = "dd.MM.yyyy HH:mm:ss";
				} else if (spaltentypen[i].equals(java.util.Date.class)) {
					pattern = "dd.MM.yyyy";
				} else if (spaltentypen[i].equals(java.lang.Integer.class)) {
					pattern = "";
					ausrichtung = "Right";
				} else {
					c = java.lang.String.class;
				}
				int iBreite = 50;
				if (columnheaderWidths != null) {

					double faktor = 5.5;

					if (gesamtbreiteColumnheaderWidth > 0 && 740 / gesamtbreiteColumnheaderWidth < faktor) {
						faktor = (double) 740 / (double) gesamtbreiteColumnheaderWidth;
					}
					if (columnheaderWidths[i] == QueryParameters.FLR_BREITE_SHARE_WITH_REST) {

						iBreite = (int) (iBreiteShareWithRest * 5);
					} else {
						iBreite = (int) (columnheaderWidths[i] * faktor);
					}

				} else {
					iBreite = 740 / (iSpaltenanzahl - 1);
				}

				parameter += "<parameter name=\"P_SPALTE" + (i)
						+ "\" isForPrompting=\"false\" class=\"java.lang.String\"/>\r\n";

				felder += "<field name=\"Spalte" + i + "\" class=\"" + c.getName() + "\"/>\r\n";
				ueberschriften += "<textField isStretchWithOverflow=\"true\" pattern=\"\" isBlankWhenNull=\"true\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >"
						+ "<reportElement mode=\"Opaque\" x=\"" + iXPosition + "\" y=\"0\" width=\"" + iBreite
						+ "\" height=\"10\" forecolor=\"#000000\" backcolor=\"#FFFFFF\" key=\"textField-5\"/>"
						+ "<box> " + "<topPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<leftPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<bottomPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<rightPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>" + "</box>"
						+ "<textElement textAlignment=\"" + ausrichtung
						+ "\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">"
						+ "<font fontName=\"Arial\" pdfFontName=\"Helvetica\" size=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"false\" pdfEncoding =\"Cp1252\" isStrikeThrough=\"false\" />"
						+ " </textElement> <textFieldExpression   class=\"java.lang.String\"><![CDATA[\\$P{P_SPALTE" + i
						+ "}]]></textFieldExpression>     </textField>\r\n";

				detail += "<textField isStretchWithOverflow=\"false\" pattern=\"" + pattern
						+ "\" isBlankWhenNull=\"true\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >"
						+ "<reportElement mode=\"Opaque\" x=\"" + iXPosition + "\" y=\"1\" width=\"" + iBreite
						+ "\" height=\"10\" forecolor=\"#000000\" backcolor=\"#FFFFFF\" key=\"textField\"/>" + "<box> "
						+ "<topPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<leftPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<bottomPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<rightPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>" + "</box>"
						+ "<textElement textAlignment=\"" + ausrichtung
						+ "\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">"
						+ "<font fontName=\"Arial\" pdfFontName=\"Helvetica\" size=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"false\" pdfEncoding =\"Cp1252\" isStrikeThrough=\"false\" />"
						+ "</textElement>" + "<textFieldExpression class=\"" + c.getName() + "\"><![CDATA[\\$F{Spalte"
						+ i + "}]]></textFieldExpression> </textField>\r\n";

				if (ausrichtung.equals("Right")) {
					iXPosition += 5;
				}

				iXPosition += iBreite;
			}
		}

		// Parameter und Felder einbauen
		String paramReplace = "-PARAMETER-";
		String felderReplace = "-FELDER-";
		String ueberReplace = "-UEBER-";
		String detailReplace = "-DETAIL-";
		if (s.contains(paramReplace)) {
			s = s.replaceFirst(paramReplace, parameter);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(paramReplace);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG, info, null);
		}
		if (s.contains(felderReplace)) {
			s = s.replaceFirst(felderReplace, felder);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(felderReplace);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG, info, null);
		}
		if (s.contains(ueberReplace)) {
			s = s.replaceFirst(ueberReplace, ueberschriften);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(ueberReplace);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG, info, null);
		}
		if (s.contains(detailReplace)) {
			s = s.replaceFirst(detailReplace, detail);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(detailReplace);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG, info, null);
		}

		System.out.println(s);

		JasperReport jasperReport = null;
		try {
			File x = File.createTempFile("temp", ".jrxml");
			FileWriter fw = new FileWriter(x);

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();

			jasperReport = JasperCompileManager.compileReport(x.getAbsolutePath());

			x.delete();
		} catch (IOException ex1) {
			ex1.printStackTrace();

		} catch (JRException ex2) {
			ex2.printStackTrace();
		}

		return jasperReport;
	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {

		if (!sAktuellerReport.equals(SystemReportFac.REPORT_ENTITYLOG)
				&& !sAktuellerReport.equals(SystemReportFac.REPORT_MODULBERECHTIGUNG)
				&& !sAktuellerReport.equals(REPORT_AGB_PDF) && !sAktuellerReport.equals(REPORT_STATISTIK)) {

			String spalte = jRField.getName().replaceFirst("Spalte", "");

			Integer i = new Integer(spalte);

			if (i >= data[index].length) {
				ArrayList al = new ArrayList();
				al.add(jRField.getName());

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN, al,
						new Exception(jRField.getName()));
			}

			if (data[index][i] != null) {
				Object o = data[index][i];

				if (o instanceof java.lang.Boolean || o instanceof java.lang.Byte || o instanceof java.util.Date
						|| o instanceof java.sql.Timestamp || o instanceof java.sql.Time
						|| o instanceof java.lang.Double || o instanceof java.lang.Integer
						|| o instanceof java.lang.Float || o instanceof java.lang.Long || o instanceof java.lang.Short
						|| o instanceof java.math.BigDecimal || o instanceof java.lang.Number
						|| o instanceof java.sql.Date) {

					if (o instanceof java.sql.Date) {
						return new java.util.Date(((java.sql.Date) o).getTime());
					}
					if (o instanceof java.lang.Boolean) {
						if (((java.lang.Boolean) o) == true) {
							return "X";
						} else {
							return "";
						}

					}
					/*
					 * if(o instanceof com.lp.util.BigDecimal3){ o=new java.math.BigDecimal
					 * (((java.math.BigDecimal)o).doubleValue()); } if(o instanceof
					 * com.lp.util.BigDecimal4){ o=new java.math.BigDecimal(((java.math
					 * .BigDecimal)o).doubleValue()); } if(o instanceof com.lp.util.BigDecimal6){
					 * o=new java.math.BigDecimal(((java.math .BigDecimal)o).doubleValue());
					 * 
					 * }
					 */

					return o;
				} else {

					if (o instanceof Object[] && ((Object[]) o).length > 0) {
						Object[] oTemp = ((Object[]) o);
						if (oTemp[0] != null) {
							return oTemp[0].toString();
						} else {
							return null;
						}
					} else {
						return o.toString();
					}

				}

			} else {
				return null;
			}
		} else {
			Object value = null;
			String fieldName = jRField.getName();
			if (sAktuellerReport.equals(SystemReportFac.REPORT_ENTITYLOG)) {
				if ("Entity".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_ENTITY];
				} else if ("Operation".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_OPERATION];
				} else if ("Id".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_I_ID];
				} else if ("Key".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_KEY];
				} else if ("Von".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_VON];
				} else if ("Nach".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_NACH];
				} else if ("Locale".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_LOCALE];
				} else if ("Person".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_PERSON];
				} else if ("Zeitpunkt".equals(fieldName)) {
					value = data[index][REPORT_ENTITYLOG_ZEITPUNKT];
				}
			} else if (sAktuellerReport.equals(SystemReportFac.REPORT_MODULBERECHTIGUNG)) {
				if ("Belegart".equals(fieldName)) {
					value = data[index][REPORT_MODULBERECHTIGUNG_BELEGART];
				} else if ("Berechtigt".equals(fieldName)) {
					value = data[index][REPORT_MODULBERECHTIGUNG_BERECHTIGT];
				}
			} else if (sAktuellerReport.equals(SystemReportFac.REPORT_STATISTIK)) {
				if ("F_VON".equals(fieldName)) {
					value = data[index][REPORT_STATISTIK_VON];
				} else if ("F_BIS".equals(fieldName)) {
					value = data[index][REPORT_STATISTIK_BIS];
				} else if ("F_VORJAHR_VON".equals(fieldName)) {
					value = data[index][REPORT_STATISTIK_VORJAHR_VON];
				} else if ("F_VORJAHR_BIS".equals(fieldName)) {
					value = data[index][REPORT_STATISTIK_VORJAHR_BIS];
				}
			} else if (sAktuellerReport.equals(REPORT_AGB_PDF)) {
				if ("SeiteAlsImage".equals(fieldName)) {
					value = data[index][REPORT_AGB_PDF_SEITE_ALS_IMAGE];
				}
			}
			return value;
		}

	}

	public JasperPrintLP getABGReport(String belegartCNr, Locale locDruck, TheClientDto theClientDto) {
		if (belegartCNr != null) {
			try {
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				if (Helper.short2boolean(mandantDto.getBAgbAnhang()) == false) {
					if ((belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)
							&& Helper.short2boolean(mandantDto.getBAgbAuftrag()))
							|| (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)
									&& Helper.short2boolean(mandantDto.getBAgbAngebot()))
							|| (belegartCNr.equals(LocaleFac.BELEGART_ANFRAGE)
									&& Helper.short2boolean(mandantDto.getBAgbAnfrage()))
							|| (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)
									&& Helper.short2boolean(mandantDto.getBAgbLieferschein()))
							|| (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)
									&& Helper.short2boolean(mandantDto.getBAgbRechnung()))
							|| (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)
									&& Helper.short2boolean(mandantDto.getBAgbBestellung()))) {
						byte[] oPDF = getMandantFac().getAGBs_PDF(locDruck, theClientDto);

						if (oPDF != null) {
							byte[][] bilder = getSystemFac().konvertierePDFFileInEinzelneBilder(oPDF, 150);

							data = new Object[bilder.length][1];

							for (int i = 0; i < bilder.length; i++) {

								BufferedImage bi = Helper.byteArrayToImage(bilder[i]);

								data[i][REPORT_AGB_PDF_SEITE_ALS_IMAGE] = bi;
							}

							sAktuellerReport = REPORT_AGB_PDF;

							this.index = -1;

							Map<String, Object> mapParameter = new TreeMap<String, Object>();
							initJRDS(mapParameter, REPORT_MODUL_ALLGEMEIN, REPORT_AGB_PDF, theClientDto.getMandant(),
									theClientDto.getLocMandant(), theClientDto);

							return getReportPrint();

						}
					}

				}

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
		return null;
	}

	public JasperPrintLP printDashboard(TheClientDto theClientDto) {

		sAktuellerReport = SystemReportFac.REPORT_DASHBOARD;

		this.index = -1;

		data = new Object[0][0];

		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		initJRDS(mapParameter, SystemReportFac.REPORT_MODUL, SystemReportFac.REPORT_DASHBOARD,
				theClientDto.getMandant(), theClientDto.getLocMandant(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP printStatistik(DatumsfilterVonBis vonBis, Integer iOption, String sOption,
			TheClientDto theClientDto) {

		sAktuellerReport = SystemReportFac.REPORT_STATISTIK;

		this.index = -1;

		data = new Object[0][0];

		Map<String, Object> mapParameter = new TreeMap<String, Object>();

		mapParameter.put("P_VON", vonBis.getTimestampVon());
		mapParameter.put("P_BIS", vonBis.getTimestampBisUnveraendert());

		mapParameter.put("P_OPTION", sOption);

		ArrayList alDaten = new ArrayList();

		Calendar cTemp = Calendar.getInstance();
		cTemp.setTime(vonBis.getTimestampVon());

		Calendar cTempVorjahr = Calendar.getInstance();
		cTempVorjahr.setTime(vonBis.getTimestampVon());
		cTempVorjahr.add(Calendar.YEAR, -1);

		while (cTemp.getTime().before(vonBis.getTimestampBis())) {
			Object[] zeile = new Object[REPORT_STATISTIK_ANZAHL_SPALTEN];

			zeile[REPORT_STATISTIK_VON] = cTemp.getTime();
			zeile[REPORT_STATISTIK_VORJAHR_VON] = cTempVorjahr.getTime();

			if (iOption == STATISTIK_OPTION_TAEGLICH) {
				cTemp.add(Calendar.DAY_OF_MONTH, 1);
				cTempVorjahr.add(Calendar.DAY_OF_MONTH, 1);
			} else if (iOption == STATISTIK_OPTION_WOECHENTLICH) {
				cTemp.add(Calendar.DAY_OF_MONTH, 7);
				cTempVorjahr.add(Calendar.DAY_OF_MONTH, 7);
			} else if (iOption == STATISTIK_OPTION_MONATLICH) {
				cTemp.add(Calendar.MONTH, 1);
				cTempVorjahr.add(Calendar.MONTH, 1);
			} else if (iOption == STATISTIK_OPTION_QUARTAL) {
				cTemp.add(Calendar.MONTH, 3);
				cTempVorjahr.add(Calendar.MONTH, 3);
			} else if (iOption == STATISTIK_OPTION_JAEHRLICH) {
				cTemp.add(Calendar.YEAR, 1);
				cTempVorjahr.add(Calendar.YEAR, 1);
			} else {
				break;
			}
			if (cTemp.getTime().after(vonBis.getTimestampBis())) {
				zeile[REPORT_STATISTIK_BIS] = new java.util.Date(vonBis.getTimestampBis().getTime() - 100);

				Calendar cVJ = Calendar.getInstance();
				cVJ.setTimeInMillis(vonBis.getTimestampBis().getTime() - 100);
				cVJ.add(Calendar.YEAR, -1);

				zeile[REPORT_STATISTIK_VORJAHR_BIS] = cVJ.getTime();

			} else {
				zeile[REPORT_STATISTIK_BIS] = new java.util.Date(cTemp.getTime().getTime() - 100);
				zeile[REPORT_STATISTIK_VORJAHR_BIS] = new java.util.Date(cTempVorjahr.getTime().getTime() - 100);

			}

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_STATISTIK_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(mapParameter, SystemReportFac.REPORT_MODUL, SystemReportFac.REPORT_STATISTIK,
				theClientDto.getMandant(), theClientDto.getLocMandant(), theClientDto);
		return getReportPrint();
	}

	public JasperPrintLP[] printVersandAuftrag(VersandauftragDto versandauftragDto, Integer iAnzahlKopien,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Zurzeit keine Felder und nur eine Position
		int iAnzahlZeilen = 1; // Anzahl der Positionen
		int iAnzahlSpalten = 0; // Anzahl der Spalten in der Gruppe
		data = new Object[iAnzahlZeilen][iAnzahlSpalten];
		sAktuellerReport = SystemReportFac.REPORT_VERSANDAUFTRAG;
		if (versandauftragDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragDto == null"));
		}

		int iAnzahlExemplare;
		if (iAnzahlKopien == null || iAnzahlKopien.intValue() <= 0) {
			iAnzahlExemplare = 1;
		} else {
			iAnzahlExemplare = 1 + iAnzahlKopien.intValue();
		}
		JasperPrintLP[] prints = new JasperPrintLP[iAnzahlExemplare];
		// Parameter
		Map<String, Object> mapParameter = new TreeMap<String, Object>();
		mapParameter.put("P_EMPFAENGER", versandauftragDto.getCEmpfaenger());
		mapParameter.put("P_CC", versandauftragDto.getCCcempfaenger());
		mapParameter.put("P_BETREFF", versandauftragDto.getCBetreff());
		mapParameter.put("P_ABSENDER", versandauftragDto.getCAbsenderadresse());
		mapParameter.put("P_SENDEZEITPUNKT", versandauftragDto.getTSendezeitpunkt());
		mapParameter.put("P_SENDEZEITPUNKT_WUNSCH", versandauftragDto.getTSendezeitpunktwunsch());
		mapParameter.put("P_STATUS", versandauftragDto.getStatusCNr());
		mapParameter.put("P_TEXT", versandauftragDto.getCText());

		Integer cachedReportvariante = theClientDto.getReportvarianteIId();

		for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
			if (iKopieNummer > 0) {
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(iKopieNummer));
			}

			// Index zuruecksetzen
			this.index = -1;
			theClientDto.setReportvarianteIId(cachedReportvariante);
			initJRDS(mapParameter, SystemReportFac.REPORT_MODUL, SystemReportFac.REPORT_VERSANDAUFTRAG,
					theClientDto.getMandant(), theClientDto.getLocMandant(), theClientDto);
			prints[iKopieNummer] = getReportPrint();
		}
		return prints;
	}

	public PositionRpt getPositionForReport(String sBelegart, Integer iBelegpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getPositionForReport(sBelegart, iBelegpositionIId, false, false, theClientDto);
	}

	public PositionRpt getPositionForReport(String sBelegart, Integer iBelegpositionIId,
			boolean bLieferscheinDruckAusRechnung, boolean bAuftragAnzahlungsDruckAusRechnung,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (LocaleFac.BELEGART_AGSTUECKLISTE.equals(sBelegart)) {
				return getAGStuecklisteposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_ANFRAGE.equals(sBelegart)) {
				return getAnfrageposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_ANGEBOT.equals(sBelegart)) {
				return getAngebotposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_ARTIKEL.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_AUFTRAG.equals(sBelegart)) {
				if (bAuftragAnzahlungsDruckAusRechnung == true) {
					return getAuftragPositionRpt(iBelegpositionIId, LocaleFac.BELEGART_RECHNUNG, theClientDto);
				} else {
					return getAuftragPositionRpt(iBelegpositionIId, LocaleFac.BELEGART_AUFTRAG, theClientDto);
				}
			} else if (LocaleFac.BELEGART_BENUTZER.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_BESTELLUNG.equals(sBelegart)) {
				return getBestellposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_ERZAHLUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_FINANZBUCHHALTUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_GUTSCHRIFT.equals(sBelegart)) {
				return getRechnungposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_HAND.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_INVENTUR.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_KUNDE.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_LIEFERANT.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(sBelegart)) {

				if (bLieferscheinDruckAusRechnung == true) {
					return getLieferscheinposition(iBelegpositionIId, LocaleFac.BELEGART_RECHNUNG, theClientDto);
				} else {
					return getLieferscheinposition(iBelegpositionIId, LocaleFac.BELEGART_LIEFERSCHEIN, theClientDto);
				}

			} else if (LocaleFac.BELEGART_LOS.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_LOSABLIEFERUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_PARTNER.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_PROFORMARECHNUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_PROJEKT.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_RECHNUNG.equals(sBelegart)) {
				return getRechnungposition(iBelegpositionIId, theClientDto);
			} else if (LocaleFac.BELEGART_REKLAMATION.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_REPARATUR.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_REZAHLUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_RUECKLIEFERUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_RUECKSCHEIN.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_STUECKLISTE.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_SYSTEM.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_WARENEINGANG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_ZEITERFASSUNG.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			} else if (LocaleFac.BELEGART_ZUTRITT.equals(sBelegart)) {
				// No need atm maybe implemented some time
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "");
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return new PositionRpt();
	}

	public PositionRpt getLsPosReport(Integer lsposId, LieferscheinpositionDto aLsposDto,
			LieferscheinDto aLieferscheinDto, KundeDto aKundeDto, ArtikelDto aArtikelDto, boolean druckAusRechnung,
			TheClientDto theClientDto) throws RemoteException {
		LieferscheinpositionDto posDto = aLsposDto;
		if (posDto == null) {
			posDto = getLieferscheinpositionFac().lieferscheinpositionFindByPrimaryKey(lsposId, theClientDto);
		}

		PositionRpt rpt = new PositionRpt(posDto.getPositionsartCNr());

		rpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_LIEFERSCHEINPOSITION, lsposId, theClientDto));

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT.equals(posDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = aArtikelDto;
			if (artikelDto == null) {
				artikelDto = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);
			}

			LieferscheinDto lsDto = aLieferscheinDto;
			if (lsDto == null) {
				lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(posDto.getBelegIId(), theClientDto);
			}

			KundeDto kundeDto = aKundeDto;
			if (kundeDto == null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);
			}

			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(posDto,
					druckAusRechnung ? LocaleFac.BELEGART_RECHNUNG : LocaleFac.BELEGART_LIEFERSCHEIN, artikelDto,
					locDruck, kundeDto.getPartnerIId(), theClientDto);
			rpt.setSIdent(druckDto.getSIdentnummer());
			rpt.setSBezeichnung(druckDto.getSBezeichnung());
			rpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			rpt.setBdMenge(posDto.getNMenge());
			rpt.setSEinheit(posDto.getEinheitCNr());

			if (posDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(posDto.getEinheitCNr(),
						Helper.locale2String(locDruck));
				if (einheitDto != null) {
					rpt.setSEinheit(einheitDto.formatBez());
				}
			}

			rpt.setDRabatt(posDto.getFRabattsatz());
			rpt.setDZusatzrabatt(posDto.getFZusatzrabattsatz());
			rpt.setBdEinzelpreisPlusAufschlag(posDto.getNEinzelpreisplusversteckteraufschlag());
			rpt.setBdPreis(posDto.getNNettoeinzelpreis());
			rpt.setBdPreisPlusAufschlagMinusRabatt(posDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			rpt.setBdMaterialzuschlag(posDto.getNMaterialzuschlag());
			rpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			rpt.setSText(druckDto.getSArtikelkommentar());
			rpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			rpt.setSEccn(artikelDto.getCEccn());

			rpt.setNDimMenge(posDto.getNDimMenge());
			rpt.setNDimBreite(posDto.getNDimBreite());
			rpt.setNDimHoehe(posDto.getNDimHoehe());
			rpt.setNDimTiefe(posDto.getNDimTiefe());

			return rpt;
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE.equals(posDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = aArtikelDto;
			if (artikelDto == null) {
				artikelDto = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);

			}
			LieferscheinDto lsDto = aLieferscheinDto;
			if (lsDto == null) {
				lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(posDto.getBelegIId(), theClientDto);
			}
			KundeDto kundeDto = aKundeDto;
			if (kundeDto == null) {
				kundeDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);
			}

			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(posDto,
					druckAusRechnung ? LocaleFac.BELEGART_RECHNUNG : LocaleFac.BELEGART_LIEFERSCHEIN, artikelDto,
					locDruck, kundeDto.getPartnerIId(), theClientDto);
			rpt.setSIdent(druckDto.getSIdentnummer());
			rpt.setSBezeichnung(druckDto.getSBezeichnung());
			rpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());

			rpt.setBdMenge(posDto.getNMenge());
			rpt.setSEinheit(posDto.getEinheitCNr());
			rpt.setDRabatt(posDto.getFRabattsatz());
			rpt.setDZusatzrabatt(posDto.getFZusatzrabattsatz());
			rpt.setBdEinzelpreisPlusAufschlag(posDto.getNEinzelpreisplusversteckteraufschlag());
			rpt.setBdPreis(posDto.getNNettoeinzelpreis());
			rpt.setBdPreisPlusAufschlagMinusRabatt(posDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			rpt.setSEccn(artikelDto.getCEccn());
			return rpt;
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_BETRIFFT.equals(posDto.getPositionsartCNr())) {
			return setBetrifft(rpt, posDto);
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_LEERZEILE.equals(posDto.getPositionsartCNr())
				|| LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_SEITENUMBRUCH.equals(posDto.getPositionsartCNr())) {
			return rpt;
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_URSPRUNGSLAND.equals(posDto.getPositionsartCNr())) {
			return setUrsprungsland(rpt, posDto);
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTBAUSTEIN.equals(posDto.getPositionsartCNr())) {
			return setTextbaustein(rpt, posDto, theClientDto);
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE.equals(posDto.getPositionsartCNr())) {
			return setTexteingabe(rpt, posDto);
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
				.equals(posDto.getPositionsartCNr())) {
			return setIntZwischensumme(rpt, posDto);
		}

		return rpt;
	}

	private PositionRpt setBetrifft(PositionRpt rpt, BelegpositionDto posDto) {
		rpt.setSBezeichnung(posDto.getCBez());
		return rpt;
	}

	private PositionRpt setUrsprungsland(PositionRpt rpt, BelegpositionDto posDto) {
		rpt.setSIdent(posDto.getCBez());
		return rpt;
	}

	private PositionRpt setTexteingabe(PositionRpt rpt, BelegpositionDto posDto) {
		rpt.setSText(posDto.getXTextinhalt());
		return rpt;
	}

	private PositionRpt setTextbaustein(PositionRpt rpt, BelegpositionDto posDto, TheClientDto theClientDto)
			throws RemoteException {
		MediastandardDto oMediastandardDto = getMediaFac().mediastandardFindByPrimaryKey(posDto.getMediastandardIId());

		BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
		rpt.setSText(druckDto.getSFreierText());
		rpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		return rpt;
	}

	private PositionRpt setIntZwischensumme(PositionRpt rpt, BelegpositionVerkaufDto posDto) {
		rpt.setSBezeichnung(posDto.getCBez());
		rpt.setSZusatzbezeichnung(posDto.getCZusatzbez());
		rpt.setBdMenge(posDto.getNMenge());
		rpt.setSEinheit(posDto.getEinheitCNr());
		rpt.setDRabatt(posDto.getFRabattsatz());
		rpt.setDZusatzrabatt(posDto.getFZusatzrabattsatz());
		rpt.setBdPreis(posDto.getNNettoeinzelpreis());
		return rpt;
	}

	private PositionRpt getLieferscheinposition(Integer iLieferscheinpositionIId, String sBelegart,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		LieferscheinpositionDto lieferscheinpositionDto = getLieferscheinpositionFac()
				.lieferscheinpositionFindByPrimaryKey(iLieferscheinpositionIId, theClientDto);
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_BETRIFFT
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(lieferscheinpositionDto.getCBez());
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lieferscheinpositionDto.getArtikelIId(),
					theClientDto);
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDto.getBelegIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse(),
					theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(lieferscheinpositionDto, sBelegart, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());

			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			positionRpt.setBdMenge(lieferscheinpositionDto.getNMenge());
			positionRpt.setSEinheit(lieferscheinpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(lieferscheinpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(lieferscheinpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(lieferscheinpositionDto.getNNettoeinzelpreis());
			positionRpt.setSEccn(artikelDto.getCEccn());
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDto.getBelegIId(), theClientDto);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lieferscheinpositionDto.getArtikelIId(),
					theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse(),
					theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(lieferscheinpositionDto, sBelegart, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(lieferscheinpositionDto.getNMenge());
			positionRpt.setSEinheit(lieferscheinpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(lieferscheinpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(lieferscheinpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(lieferscheinpositionDto.getNNettoeinzelpreis());
			positionRpt.setBdMaterialzuschlag(lieferscheinpositionDto.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());

			positionRpt.setArtikelsetType(getLieferscheinReportFac().getArtikelsetType(lieferscheinpositionDto));

			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_LEERZEILE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_SEITENUMBRUCH
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTBAUSTEIN
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(lieferscheinpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			positionRpt.setSText(lieferscheinpositionDto.getXTextinhalt());
			return positionRpt;
		}
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_URSPRUNGSLAND
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			positionRpt.setSIdent(lieferscheinpositionDto.getCBez());
			return positionRpt;
		}

		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(lieferscheinpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(lieferscheinpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(lieferscheinpositionDto.getNMenge());
			positionRpt.setSEinheit(lieferscheinpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(lieferscheinpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(lieferscheinpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(lieferscheinpositionDto.getNNettoeinzelpreis());
			return positionRpt;
		}

		return positionRpt;
	}

	private PositionRpt getRechnungposition(Integer rechnungspositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		RechnungPositionDto rechnungpositionDto = getRechnungFac()
				.rechnungPositionFindByPrimaryKey(rechnungspositionIId);

		positionRpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_RECHNUNGPOSITION, rechnungspositionIId, theClientDto));

		if (RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungpositionDto.getRechnungIId());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(rechnungpositionDto.getArtikelIId(),
					theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(rechnungpositionDto, LocaleFac.BELEGART_RECHNUNG,
					artikelDto, locDruck, kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());

			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			if (rechnungpositionDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(
						rechnungpositionDto.getEinheitCNr(), Helper.locale2String(locDruck));
				if (einheitDto != null) {
					positionRpt.setSEinheit(einheitDto.formatBez());
				}
			}

			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(rechnungpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdEinzelpreisPlusAufschlag(rechnungpositionDto.getNEinzelpreisplusversteckteraufschlag());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					rechnungpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setBdMaterialzuschlag(rechnungpositionDto.getNMaterialzuschlag());
			positionRpt.setSEccn(artikelDto.getCEccn());

			positionRpt.setNDimMenge(rechnungpositionDto.getNDimMenge());
			positionRpt.setNDimBreite(rechnungpositionDto.getNDimBreite());
			positionRpt.setNDimHoehe(rechnungpositionDto.getNDimHoehe());
			positionRpt.setNDimTiefe(rechnungpositionDto.getNDimTiefe());

			positionRpt.setArtikelsetType(getRechnungReportFac().getArtikelsetType(rechnungpositionDto));

		}
		if (RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(rechnungpositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(rechnungpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdEinzelpreisPlusAufschlag(rechnungpositionDto.getNEinzelpreisplusversteckteraufschlag());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					rechnungpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSText(rechnungpositionDto.getXTextinhalt());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_BETRIFFT.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_POSITION.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto.getCZusatzbez());
			positionRpt.setSTypCNr(rechnungpositionDto.getTypCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(rechnungpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TEXTBAUSTEIN.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(rechnungpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_ZWISCHENSUMME.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TRANSPORTSPESEN.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_URSPRUNGSLAND.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_ANZAHLUNG.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_VORAUSZAHLUNG.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_LEERZEILE.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_SEITENUMBRUCH.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_PAUSCHALPOSITION
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_REPARATUR.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_RUECKSCHEIN.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_GUTSCHRIFT.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_FREMDARTIKEL.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_AUFTRAGSDATEN.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(rechnungpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
		}

		return positionRpt;
	}

	private PositionRpt getAuftragPositionRpt(Integer iAuftragpositionIId, String sBelegart, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AuftragpositionDto auftragspositionDto = getAuftragpositionFac()
				.auftragpositionFindByPrimaryKey(iAuftragpositionIId);

		positionRpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_AUFTRAGPOSITION, iAuftragpositionIId, theClientDto));

		if (AuftragServiceFac.AUFTRAGPOSITIONART_BETRIFFT.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(auftragspositionDto.getCBez());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE.equals(auftragspositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragspositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(auftragspositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(auftragspositionDto.getCZusatzbez());
			positionRpt.setBdMenge(auftragspositionDto.getNMenge());
			positionRpt.setSEinheit(auftragspositionDto.getEinheitCNr());
			positionRpt.setDRabatt(auftragspositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(auftragspositionDto.getFZusatzrabattsatz());
			positionRpt.setBdEinzelpreisPlusAufschlag(auftragspositionDto.getNEinzelpreisplusversteckteraufschlag());
			positionRpt.setBdPreis(auftragspositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					auftragspositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setTTermin(auftragspositionDto.getTUebersteuerbarerLiefertermin());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_IDENT.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragspositionDto.getBelegIId());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(auftragspositionDto.getArtikelIId(),
					theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(),
					theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(auftragspositionDto, sBelegart, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(auftragspositionDto.getNMenge());
			positionRpt.setSEinheit(auftragspositionDto.getEinheitCNr());

			if (auftragspositionDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(
						auftragspositionDto.getEinheitCNr(), Helper.locale2String(locDruck));
				if (einheitDto != null) {
					positionRpt.setSEinheit(einheitDto.formatBez());
				}
			}

			positionRpt.setDRabatt(auftragspositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(auftragspositionDto.getFZusatzrabattsatz());

			positionRpt.setBdEinzelpreisPlusAufschlag(auftragspositionDto.getNEinzelpreisplusversteckteraufschlag());

			positionRpt.setBdPreis(auftragspositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					auftragspositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

			positionRpt.setBdMaterialzuschlag(auftragspositionDto.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setTTermin(auftragspositionDto.getTUebersteuerbarerLiefertermin());
			positionRpt.setSEccn(artikelDto.getCEccn());

			positionRpt.setNDimMenge(auftragspositionDto.getNDimMenge());
			positionRpt.setNDimBreite(auftragspositionDto.getNDimBreite());
			positionRpt.setNDimHoehe(auftragspositionDto.getNDimHoehe());
			positionRpt.setNDimTiefe(auftragspositionDto.getNDimTiefe());

			/// SP9481
			positionRpt.setArtikelsetType(getAuftragReportFac().getArtikelsetType(auftragspositionDto));

		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_LEERZEILE.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_SEITENUMBRUCH
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_TEXTBAUSTEIN.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(auftragspositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			positionRpt.setSText(auftragspositionDto.getXTextinhalt());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_POSITION.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto.getPositionsartCNr());
			positionRpt.setSZusatzbezeichnung(auftragspositionDto.getCZusatzbez());
			positionRpt.setSTypCNr(auftragspositionDto.getTypCNr());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAngebotposition(Integer iAngebotpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AngebotpositionDto angebotPositionDto = getAngebotpositionFac()
				.angebotpositionFindByPrimaryKey(iAngebotpositionIId, theClientDto);

		positionRpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_ANGEBOTPOSITION, iAngebotpositionIId, theClientDto));

		if (AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(angebotPositionDto.getCBez());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE.equals(angebotPositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(angebotPositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(angebotPositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(angebotPositionDto.getCZusatzbez());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(angebotPositionDto.getFZusatzrabattsatz());
			positionRpt.setBdEinzelpreisPlusAufschlag(angebotPositionDto.getNEinzelpreisplusversteckteraufschlag());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					angebotPositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_IDENT.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(angebotPositionDto.getArtikelIId(),
					theClientDto);
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotPositionDto.getBelegIId(),
					theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(),
					theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(angebotPositionDto, LocaleFac.BELEGART_ANGEBOT, artikelDto,
					locDruck, kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			if (angebotPositionDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(
						angebotPositionDto.getEinheitCNr(), Helper.locale2String(locDruck));
				if (einheitDto != null) {
					positionRpt.setSEinheit(einheitDto.formatBez());
				}
			}

			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(angebotPositionDto.getFZusatzrabattsatz());
			positionRpt.setBdEinzelpreisPlusAufschlag(angebotPositionDto.getNEinzelpreisplusversteckteraufschlag());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(
					angebotPositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			positionRpt.setBdMaterialzuschlag(angebotPositionDto.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());

			positionRpt.setNDimMenge(angebotPositionDto.getNDimMenge());
			positionRpt.setNDimBreite(angebotPositionDto.getNDimBreite());
			positionRpt.setNDimHoehe(angebotPositionDto.getNDimHoehe());
			positionRpt.setNDimTiefe(angebotPositionDto.getNDimTiefe());
			
			
			positionRpt.setArtikelsetType(getAngebotReportFac().getArtikelsetType(angebotPositionDto));
			

		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(angebotPositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			positionRpt.setSText(angebotPositionDto.getXTextinhalt());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(angebotPositionDto.getAgstklIId());
			positionRpt.setSIdent(agstklDto.getCNr());
			positionRpt.setSBezeichnung(agstklDto.getCBez());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(angebotPositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_POSITION.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto.getPositionsartCNr());
			positionRpt.setSZusatzbezeichnung(angebotPositionDto.getCZusatzbez());
			positionRpt.setSTypCNr(angebotPositionDto.getTypCNr());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAnfrageposition(Integer iAnfragepositionIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		PositionRpt positionRpt = new PositionRpt();
		AnfragepositionDto anfragepositionDto = getAnfragepositionFac()
				.anfragepositionFindByPrimaryKey(iAnfragepositionIId, theClientDto);

		positionRpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_ANFRAGEPOSITION, iAnfragepositionIId, theClientDto));

		if (AnfrageServiceFac.ANFRAGEPOSITIONART_BETRIFFT.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(anfragepositionDto.getCBez());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE.equals(anfragepositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(anfragepositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(anfragepositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(anfragepositionDto.getCZusatzbez());
			positionRpt.setBdMenge(anfragepositionDto.getNMenge());
			positionRpt.setSEinheit(anfragepositionDto.getEinheitCNr());
			positionRpt.setBdPreis(anfragepositionDto.getNRichtpreis());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(anfragepositionDto.getArtikelIId(),
					theClientDto);
			AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(anfragepositionDto.getBelegIId(),
					theClientDto);

			Integer partnerIId = null;
			Locale locDruck = theClientDto.getLocUi();
			if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto);
				locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
				partnerIId = lieferantDto.getPartnerIId();
			}

			BelegPositionDruckIdentDto druckDto = printIdent(anfragepositionDto, LocaleFac.BELEGART_ANFRAGE, artikelDto,
					locDruck, partnerIId, theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());

			positionRpt.setBdMaterialzuschlag(anfragepositionDto.getNMaterialzuschlag());
			positionRpt.setBdMenge(anfragepositionDto.getNMenge());
			positionRpt.setSEinheit(anfragepositionDto.getEinheitCNr());

			if (anfragepositionDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(
						anfragepositionDto.getEinheitCNr(), Helper.locale2String(locDruck));
				if (einheitDto != null) {
					positionRpt.setSEinheit(einheitDto.formatBez());
				}
			}

			positionRpt.setBdPreis(anfragepositionDto.getNRichtpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());

			ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
					anfragepositionDto.getArtikelIId(), anfrageDto.getLieferantIIdAnfrageadresse(),
					anfragepositionDto.getNMenge(),

					anfrageDto.getWaehrungCNr(), new java.sql.Date(anfrageDto.getTBelegdatum().getTime()),
					theClientDto);

			if (artikellieferantDto != null) {
				positionRpt.setSLieferantenArtikelnummer(artikellieferantDto.getCArtikelnrlieferant());
				positionRpt.setSLieferantenArtikelbezeichnung(artikellieferantDto.getCBezbeilieferant());
			}
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_LEERZEILE.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_SEITENUMBRUCH.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTBAUSTEIN.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(anfragepositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTEINGABE.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto.getPositionsartCNr());
			positionRpt.setSText(anfragepositionDto.getXTextinhalt());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAGStuecklisteposition(Integer iAGStuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AgstklpositionDto agstklpositionDto = getAngebotstklpositionFac()
				.agstklpositionFindByPrimaryKey(iAGStuecklistepositionIId, theClientDto);

		if (AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE.equals(agstklpositionDto.getAgstklpositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(agstklpositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());

			positionRpt.setSPositionsartCNr(agstklpositionDto.getAgstklpositionsartCNr());
			positionRpt.setSBezeichnung(agstklpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(agstklpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(agstklpositionDto.getNMenge());
			positionRpt.setSEinheit(agstklpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(agstklpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(agstklpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(agstklpositionDto.getNNettoeinzelpreis());
			positionRpt.setBMitPreisen(Helper.short2Boolean(agstklpositionDto.getBMitPreisen()));
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(agstklpositionDto.getNNettogesamtmitaufschlag());
			positionRpt.setBdEinzelpreisPlusAufschlag(agstklpositionDto.getNNettogesamtmitaufschlag());
		} else if (AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT.equals(agstklpositionDto.getAgstklpositionsartCNr())) {
			positionRpt.setSPositionsartCNr(agstklpositionDto.getAgstklpositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(agstklpositionDto.getArtikelIId(),
					theClientDto);
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklpositionDto.getAgstklIId());
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(agstklDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(agstklpositionDto, LocaleFac.BELEGART_AGSTUECKLISTE,
					artikelDto, locDruck, kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(agstklpositionDto.getNMenge());
			positionRpt.setSEinheit(agstklpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(agstklpositionDto.getFRabattsatz());
			positionRpt.setDZusatzrabatt(agstklpositionDto.getFZusatzrabattsatz());
			positionRpt.setBdPreis(agstklpositionDto.getNNettoeinzelpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());
			positionRpt.setBMitPreisen(Helper.short2Boolean(agstklpositionDto.getBMitPreisen()));
			positionRpt.setBdPreisPlusAufschlagMinusRabatt(agstklpositionDto.getNNettogesamtmitaufschlag());
			positionRpt.setBdEinzelpreisPlusAufschlag(agstklpositionDto.getNNettogesamtmitaufschlag());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getBestellposition(Integer iBestellpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		BestellpositionDto bestellpositionDto = getBestellpositionFac()
				.bestellpositionFindByPrimaryKey(iBestellpositionIId);

		positionRpt.setSubreportBelegartmedia(
				getSubreportBelegartmedia(QueryParameters.UC_ID_BESTELLPOSITION, iBestellpositionIId, theClientDto));

		if (BestellpositionFac.BESTELLPOSITIONART_BETRIFFT.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(bestellpositionDto.getCBez());
		} else if (BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE.equals(bestellpositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bestellpositionDto.getArtikelIId(),
					theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
			positionRpt.setSBezeichnung(bestellpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(bestellpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(bestellpositionDto.getNMenge());
			positionRpt.setSEinheit(bestellpositionDto.getEinheitCNr());
			positionRpt.setTTermin(bestellpositionDto.getTUebersteuerterLiefertermin());
			positionRpt.setDRabatt(bestellpositionDto.getDRabattsatz());
			positionRpt.setBdPreis(bestellpositionDto.getNNettoeinzelpreis());
			positionRpt.setSEccn(artikelDto.getCEccn());
		} else if (BestellpositionFac.BESTELLPOSITIONART_IDENT.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(bestellpositionDto.getArtikelIId(),
					theClientDto);
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(bestellpositionDto, LocaleFac.BELEGART_BESTELLUNG,
					artikelDto, locDruck, lieferantDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setSZusatzbezeichnung2(druckDto.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setBdMaterialzuschlag(bestellpositionDto.getNMaterialzuschlag());
			positionRpt.setBdMenge(bestellpositionDto.getNMenge());
			positionRpt.setSEinheit(bestellpositionDto.getEinheitCNr());

			if (bestellpositionDto.getEinheitCNr() != null) {
				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(
						bestellpositionDto.getEinheitCNr(), Helper.locale2String(locDruck));
				if (einheitDto != null) {
					positionRpt.setSEinheit(einheitDto.formatBez());
				}
			}

			positionRpt.setTTermin(bestellpositionDto.getTUebersteuerterLiefertermin());
			positionRpt.setDRabatt(bestellpositionDto.getDRabattsatz());
			positionRpt.setBdPreis(bestellpositionDto.getNNettoeinzelpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());

			ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
					bestellpositionDto.getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
					bestellpositionDto.getNMenge(),

					bestellungDto.getWaehrungCNr(), new java.sql.Date(bestellungDto.getDBelegdatum().getTime()),
					theClientDto);

			if (artikellieferantDto != null) {
				positionRpt.setSLieferantenArtikelnummer(artikellieferantDto.getCArtikelnrlieferant());
				positionRpt.setSLieferantenArtikelbezeichnung(artikellieferantDto.getCBezbeilieferant());
			}
		} else if (BestellpositionFac.BESTELLPOSITIONART_LEERZEILE.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
		} else if (BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
		} else if (BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(bestellpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto.getPositionsartCNr());
			positionRpt.setSText(bestellpositionDto.getXTextinhalt());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	@Override
	public List<JRPrintElement> getReportCopy(JasperPrint jrPrint, String modul, TheClientDto theClientDto) {
		data = new Object[1][0];
		return super.getReportCopy(jrPrint, modul, theClientDto);
	}

	@Override
	public JasperPrint mergeWithPrintTypePrint(MergePrintTypeParams model, TheClientDto theClientDto) {
		Validator.notNull(model, "MergePrintTypeParams");

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(P_DRUCKTYPE, model.getDruckType());
		data = new Object[1][0];
		return mergePrints(model.getJrPrint(), "allgemein", "ausdruckmedium.jasper", parameter, theClientDto);
	}
}
