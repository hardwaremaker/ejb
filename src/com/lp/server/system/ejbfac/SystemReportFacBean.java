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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

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
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.fastlanereader.service.FastLaneReader;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.BelegPositionDruckIdentDto;
import com.lp.server.util.BelegPositionDruckTextbausteinDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.PositionRpt;

@Stateless
@Interceptors(TimingInterceptor.class)
public class SystemReportFacBean extends LPReport implements SystemReportFac,
		JRDataSource {

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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printUseCaseHandler(String uuid, QueryParameters q,
			int iAnzahlZeilen, String ueberschrift, TheClientDto theClientDto) {
		index = -1;

		sAktuellerReport = SystemFac.REPORTXML_FLRDRUCK;
		String reportdir = "";
		JasperReport jasperReport = null;

		String reportuebersteuert = SystemFac.REPORTXML_FLRDRUCK.replaceFirst(
				".jrxml", q.getUseCaseId() + ".jasper");

		reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
				reportuebersteuert, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);

		if (reportdir == null) {
			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
					SystemFac.REPORTXML_FLRDRUCK, theClientDto.getMandant(),
					theClientDto.getLocUi(), null, theClientDto);
		} else {
			try {
				jasperReport = (JasperReport) JRLoader
						.loadObjectFromFile(reportdir);
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
		}

		FastLaneReader fastLaneReader = null;
		try {
			// fastLaneReader = (FastLaneReader)
			// getInitialContext().lookup("FastLaneReader");
			fastLaneReader = (FastLaneReader) getInitialContext().lookup(
					"lpserver/FastLaneReaderBean/remote");

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
		int pageSize = UseCaseHandler.PAGE_SIZE;
		try {

			HashMap hmParameter = new HashMap<Object, Object>();
			QueryResult qr = fastLaneReader.setQuery(uuid, q.getUseCaseId(), q,
					theClientDto);

			qr = fastLaneReader.getResults(uuid, q.getUseCaseId(),
					qr.getRowCount(), theClientDto);
			TableInfo ti = fastLaneReader.getTableInfo(uuid, q.getUseCaseId(),
					theClientDto);
			int iSpaltenAnzahl = ti.getColumnClasses().length;

			for (int i = 0; i < ti.getColumnClasses().length; i++) {
				if (ti.getColumnClasses()[i].equals(java.awt.Color.class)) {
					iSpaltenAnzahl--;
				}
			}

			if (jasperReport == null) {
				jasperReport = getJasperReport(iSpaltenAnzahl,
						ti.getColumnHeaderWidths(), ti.getColumnClasses(),
						reportdir, false);
			}
			// Spaltenueberschriften einfuegen
			for (int i = 0; i < iSpaltenAnzahl; i++) {
				hmParameter.put("P_SPALTE" + i,
						ti.getColumnHeaderValues()[i].toString());
			}

			hmParameter.put("P_UEBERSCHRIFT", ueberschrift);
			hmParameter.put("P_USECASE_ID", q.getUseCaseId() + "");

			data = new Object[(int) qr.getRowCount()][iSpaltenAnzahl];

			for (int x = 0; x < qr.getRowCount(); x++) {
				for (int i = 0; i < iSpaltenAnzahl; i++) {
					if (qr.getRowData()[x][i] instanceof BigDecimal
							&& ((BigDecimal) qr.getRowData()[x][i])
									.doubleValue() == 0) {
						continue;
					}
					data[x][i] = qr.getRowData()[x][i];
				}
			}

			initJRDS(hmParameter, jasperReport, reportdir, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			UseCaseHandler.PAGE_SIZE = pageSize;
			fastLaneReader = null;
			/*
			 * try { //Session wieder schliessen fastLaneReader.remove(); }
			 * catch (RemoveException ex1) { ex1.printStackTrace(); } catch
			 * (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); }
			 */
		}

		return getReportPrint();
	}

	public JasperPrintLP printEntitylog(String filterKey, String filterId,
			String cDatensatz, TheClientDto theClientDto) {

		sAktuellerReport = SystemReportFac.REPORT_ENTITYLOG;

		HashMap hmParameter = new HashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLREntitylog.class);
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
			FLREntitylog flrEntitylog = (FLREntitylog) resultListIterator
					.next();

			Object[] zeile = new Object[REPORT_ENTITYLOG_ANZAHL_SPALTEN];

			zeile[REPORT_ENTITYLOG_ENTITY] = flrEntitylog.getC_entity_key();
			zeile[REPORT_ENTITYLOG_I_ID] = flrEntitylog.getEntity_i_id();
			zeile[REPORT_ENTITYLOG_KEY] = flrEntitylog.getC_key();
			zeile[REPORT_ENTITYLOG_LOCALE] = flrEntitylog.getLocale_c_nr();
			zeile[REPORT_ENTITYLOG_NACH] = flrEntitylog.getC_nach();
			zeile[REPORT_ENTITYLOG_OPERATION] = flrEntitylog.getC_operation();
			zeile[REPORT_ENTITYLOG_PERSON] = flrEntitylog.getFlrpersonal()
					.getC_kurzzeichen();
			zeile[REPORT_ENTITYLOG_VON] = flrEntitylog.getC_von();
			zeile[REPORT_ENTITYLOG_ZEITPUNKT] = flrEntitylog.getT_aendern();

			alDaten.add(zeile);
		}

		Object[][] dataTemp = new Object[alDaten.size()][REPORT_ENTITYLOG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(dataTemp);

		initJRDS(hmParameter, SystemReportFac.REPORT_MODUL,
				SystemReportFac.REPORT_ENTITYLOG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printExtraliste(
			ExtralisteRueckgabeTabelleDto extralisteDto, Integer extralisteIId,
			TheClientDto theClientDto) {
		index = -1;

		sAktuellerReport = SystemFac.REPORTXML_FLRDRUCK;

		String reportdir = null;
		JasperReport jasperReport = null;

		HashMap hmParameter = new HashMap<Object, Object>();

		hmParameter.put("P_USECASE_ID", "EL" + extralisteIId);

		String reportuebersteuert = SystemFac.REPORTXML_FLRDRUCK.replaceFirst(
				".jrxml", "EL" + extralisteIId + ".jasper");

		reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
				reportuebersteuert, theClientDto.getMandant(),
				theClientDto.getLocUi(), null, theClientDto);

		if (reportdir == null) {
			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
					SystemFac.REPORTXML_FLRDRUCK, theClientDto.getMandant(),
					theClientDto.getLocUi(), null, theClientDto);
		}

		int iSpaltenAnzahl = extralisteDto.getColumnNames().length;

		if (jasperReport == null) {
			jasperReport = getJasperReport(iSpaltenAnzahl, null,
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

	private JasperReport getJasperReport(int iSpaltenanzahl,
			int[] columnheaderWidths, Class[] spaltentypen, String reportDir,
			boolean bMitErsterSpalte) {
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

		if (columnheaderWidths != null) {
			for (int i = 0; i < cols; i++) {
				if (columnheaderWidths[i] != QueryParameters.FLR_BREITE_SHARE_WITH_REST
						&& columnheaderWidths[i] < REPORT_FLR_SPALTENBREITE_MIN) {
					columnheaderWidths[i] = REPORT_FLR_SPALTENBREITE_MIN;
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
				iBreiteShareWithRest = 0;
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
				} else if (spaltentypen[i]
						.equals(com.lp.util.BigDecimal3.class)) {
					pattern = "#,##0.000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i]
						.equals(com.lp.util.BigDecimal4.class)) {
					pattern = "#,##0.0000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i]
						.equals(com.lp.util.BigDecimal6.class)) {
					pattern = "#,##0.00000";
					c = java.math.BigDecimal.class;
				} else if (spaltentypen[i]
						.equals(com.lp.util.BigDecimalFinanz.class)) {
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

					if (columnheaderWidths[i] == QueryParameters.FLR_BREITE_SHARE_WITH_REST) {

						iBreite = (int) (iBreiteShareWithRest * 5.5);
					} else {
						iBreite = (int) (columnheaderWidths[i] * 5.5);
					}

				} else {
					iBreite = 740 / (iSpaltenanzahl - 1);
				}

				parameter += "<parameter name=\"P_SPALTE"
						+ (i)
						+ "\" isForPrompting=\"false\" class=\"java.lang.String\"/>\r\n";

				felder += "<field name=\"Spalte" + i + "\" class=\""
						+ c.getName() + "\"/>\r\n";
				ueberschriften += "<textField isStretchWithOverflow=\"true\" pattern=\"\" isBlankWhenNull=\"true\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >"
						+ "<reportElement mode=\"Opaque\" x=\""
						+ iXPosition
						+ "\" y=\"0\" width=\""
						+ iBreite
						+ "\" height=\"10\" forecolor=\"#000000\" backcolor=\"#FFFFFF\" key=\"textField-5\"/>"
						+ "<box> "
						+ "<topPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<leftPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<bottomPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<rightPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>"
						+ "</box>"
						+ "<textElement textAlignment=\""
						+ ausrichtung
						+ "\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">"
						+ "<font fontName=\"Arial\" pdfFontName=\"Helvetica\" size=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"false\" pdfEncoding =\"Cp1252\" isStrikeThrough=\"false\" />"
						+ " </textElement> <textFieldExpression   class=\"java.lang.String\"><![CDATA[\\$P{P_SPALTE"
						+ i + "}]]></textFieldExpression>     </textField>\r\n";

				detail += "<textField isStretchWithOverflow=\"false\" pattern=\""
						+ pattern
						+ "\" isBlankWhenNull=\"true\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >"
						+ "<reportElement mode=\"Opaque\" x=\""
						+ iXPosition
						+ "\" y=\"1\" width=\""
						+ iBreite
						+ "\" height=\"10\" forecolor=\"#000000\" backcolor=\"#FFFFFF\" key=\"textField\"/>"
						+ "<box> "
						+ "<topPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<leftPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<bottomPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#000000\"/>"
						+ "<rightPen lineWidth=\"0.0\" lineStyle=\"Solid\" lineColor=\"#FFFFFF\"/>"
						+ "</box>"
						+ "<textElement textAlignment=\""
						+ ausrichtung
						+ "\" verticalAlignment=\"Top\" rotation=\"None\" lineSpacing=\"Single\">"
						+ "<font fontName=\"Arial\" pdfFontName=\"Helvetica\" size=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isPdfEmbedded =\"false\" pdfEncoding =\"Cp1252\" isStrikeThrough=\"false\" />"
						+ "</textElement>"
						+ "<textFieldExpression class=\""
						+ c.getName()
						+ "\"><![CDATA[\\$F{Spalte"
						+ i
						+ "}]]></textFieldExpression> </textField>\r\n";

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
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG,
					info, null);
		}
		if (s.contains(felderReplace)) {
			s = s.replaceFirst(felderReplace, felder);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(felderReplace);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG,
					info, null);
		}
		if (s.contains(ueberReplace)) {
			s = s.replaceFirst(ueberReplace, ueberschriften);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(ueberReplace);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG,
					info, null);
		}
		if (s.contains(detailReplace)) {
			s = s.replaceFirst(detailReplace, detail);
		} else {
			ArrayList<Object> info = new ArrayList<Object>();
			info.add(detailReplace);
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG,
					info, null);
		}

		System.out.println(s);

		JasperReport jasperReport = null;
		try {
			File x = File.createTempFile("temp", ".jrxml");
			FileWriter fw = new FileWriter(x);

			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();

			jasperReport = JasperCompileManager.compileReport(x
					.getAbsolutePath());

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

		if (!sAktuellerReport.equals(SystemReportFac.REPORT_ENTITYLOG)) {

			String spalte = jRField.getName().replaceFirst("Spalte", "");

			Integer i = new Integer(spalte);

			if (i >= data[index].length) {
				ArrayList al = new ArrayList();
				al.add(jRField.getName());

				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN,
						al, new Exception(jRField.getName()));
			}

			if (data[index][i] != null) {
				Object o = data[index][i];

				if (o instanceof java.lang.Boolean
						|| o instanceof java.lang.Byte
						|| o instanceof java.util.Date
						|| o instanceof java.sql.Timestamp
						|| o instanceof java.sql.Time
						|| o instanceof java.lang.Double
						|| o instanceof java.lang.Integer
						|| o instanceof java.lang.Float
						|| o instanceof java.lang.Long
						|| o instanceof java.lang.Short
						|| o instanceof java.math.BigDecimal
						|| o instanceof java.lang.Number
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
					 * if(o instanceof com.lp.util.BigDecimal3){ o=new
					 * java.math.BigDecimal
					 * (((java.math.BigDecimal)o).doubleValue()); } if(o
					 * instanceof com.lp.util.BigDecimal4){ o=new
					 * java.math.BigDecimal(((java.math
					 * .BigDecimal)o).doubleValue()); } if(o instanceof
					 * com.lp.util.BigDecimal6){ o=new
					 * java.math.BigDecimal(((java.math
					 * .BigDecimal)o).doubleValue());
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
			}
			return value;
		}

	}

	public JasperPrintLP[] printVersandAuftrag(
			VersandauftragDto versandauftragDto, Integer iAnzahlKopien,
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
		mapParameter.put("P_SENDEZEITPUNKT",
				versandauftragDto.getTSendezeitpunkt());
		mapParameter.put("P_SENDEZEITPUNKT_WUNSCH",
				versandauftragDto.getTSendezeitpunktwunsch());
		mapParameter.put("P_STATUS", versandauftragDto.getStatusCNr());
		mapParameter.put("P_TEXT", versandauftragDto.getCText());

		Integer cachedReportvariante = theClientDto.getReportvarianteIId();

		for (int iKopieNummer = 0; iKopieNummer < iAnzahlExemplare; iKopieNummer++) {
			if (iKopieNummer > 0) {
				mapParameter.put(LPReport.P_KOPIE_NUMMER, new Integer(
						iKopieNummer));
			}

			// Index zuruecksetzen
			this.index = -1;
			theClientDto.setReportvarianteIId(cachedReportvariante);
			initJRDS(mapParameter, SystemReportFac.REPORT_MODUL,
					SystemReportFac.REPORT_VERSANDAUFTRAG,
					theClientDto.getMandant(), theClientDto.getLocMandant(),
					theClientDto);
			prints[iKopieNummer] = getReportPrint();
		}
		return prints;
	}

	public PositionRpt getPositionForReport(String sBelegart,
			Integer iBelegpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getPositionForReport(sBelegart, iBelegpositionIId, false,
				theClientDto);
	}

	public PositionRpt getPositionForReport(String sBelegart,
			Integer iBelegpositionIId, boolean bLieferscheinDruckAusRechnung,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (LocaleFac.BELEGART_AGSTUECKLISTE.equals(sBelegart)) {
			return getAGStuecklisteposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_ANFRAGE.equals(sBelegart)) {
			return getAnfrageposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_ANGEBOT.equals(sBelegart)) {
			return getAngebotposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_ARTIKEL.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_AUFTRAG.equals(sBelegart)) {
			return getAuftragPositionRpt(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_BENUTZER.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_BESTELLUNG.equals(sBelegart)) {
			return getBestellposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_ERZAHLUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_FINANZBUCHHALTUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_GUTSCHRIFT.equals(sBelegart)) {
			return getRechnungposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_HAND.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_INVENTUR.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_KUNDE.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_LIEFERANT.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_LIEFERSCHEIN.equals(sBelegart)) {

			if (bLieferscheinDruckAusRechnung == true) {
				return getLIeferscheinposition(iBelegpositionIId,
						LocaleFac.BELEGART_RECHNUNG, theClientDto);
			} else {
				return getLIeferscheinposition(iBelegpositionIId,
						LocaleFac.BELEGART_LIEFERSCHEIN, theClientDto);
			}

		} else if (LocaleFac.BELEGART_LOS.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_LOSABLIEFERUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_PARTNER.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_PROFORMARECHNUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_PROJEKT.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_RECHNUNG.equals(sBelegart)) {
			return getRechnungposition(iBelegpositionIId, theClientDto);
		} else if (LocaleFac.BELEGART_REKLAMATION.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_REPARATUR.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_REZAHLUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_RUECKLIEFERUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_RUECKSCHEIN.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_STUECKLISTE.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_SYSTEM.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_WARENEINGANG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_ZEITERFASSUNG.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		} else if (LocaleFac.BELEGART_ZUTRITT.equals(sBelegart)) {
			// No need atm maybe implemented some time
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"");
		}
		return new PositionRpt();
	}

	private PositionRpt getLIeferscheinposition(
			Integer iLieferscheinpositionIId, String sBelegart,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		LieferscheinpositionDto lieferscheinpositionDto = getLieferscheinpositionFac()
				.lieferscheinpositionFindByPrimaryKey(iLieferscheinpositionIId,
						theClientDto);
		if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_BETRIFFT
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(lieferscheinpositionDto.getCBez());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					lieferscheinpositionDto.getArtikelIId(), theClientDto);
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							lieferscheinpositionDto.getBelegIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					lieferscheinpositionDto, sBelegart, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());

			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			positionRpt.setBdMenge(lieferscheinpositionDto.getNMenge());
			positionRpt.setSEinheit(lieferscheinpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(lieferscheinpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(lieferscheinpositionDto
					.getNNettoeinzelpreis());
			positionRpt.setSEccn(artikelDto.getCEccn());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							lieferscheinpositionDto.getBelegIId(), theClientDto);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					lieferscheinpositionDto.getArtikelIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					lieferscheinpositionDto, sBelegart, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(lieferscheinpositionDto.getNMenge());
			positionRpt.setSEinheit(lieferscheinpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(lieferscheinpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(lieferscheinpositionDto
					.getNNettoeinzelpreis());
			positionRpt.setBdMaterialzuschlag(lieferscheinpositionDto
					.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto
					.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_LEERZEILE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_SEITENUMBRUCH
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTBAUSTEIN
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							lieferscheinpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			positionRpt.setSText(lieferscheinpositionDto.getXTextinhalt());
		} else if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_URSPRUNGSLAND
				.equals(lieferscheinpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(lieferscheinpositionDto
					.getPositionsartCNr());
			positionRpt.setSIdent(lieferscheinpositionDto.getCBez());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getRechnungposition(Integer rechnungspositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		RechnungPositionDto rechnungpositionDto = getRechnungFac()
				.rechnungPositionFindByPrimaryKey(rechnungspositionIId);
		if (RechnungFac.POSITIONSART_RECHNUNG_IDENT.equals(rechnungpositionDto
				.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			RechnungDto rechnungDto = getRechnungFac()
					.rechnungFindByPrimaryKey(
							rechnungpositionDto.getRechnungIId());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					rechnungpositionDto.getArtikelIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					rechnungDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					rechnungpositionDto, LocaleFac.BELEGART_RECHNUNG,
					artikelDto, locDruck, kundeDto.getPartnerIId(),
					theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
			positionRpt.setSZusatzbezeichnung2(druckDto
					.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setBdMaterialzuschlag(rechnungpositionDto
					.getNMaterialzuschlag());
			positionRpt.setSEccn(artikelDto.getCEccn());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					rechnungpositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TEXTEINGABE
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSText(rechnungpositionDto.getXTextinhalt());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_BETRIFFT
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_POSITION
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto
					.getCZusatzbez());
			positionRpt.setSTypCNr(rechnungpositionDto.getTypCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TEXTBAUSTEIN
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							rechnungpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_ZWISCHENSUMME
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_TRANSPORTSPESEN
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_URSPRUNGSLAND
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_ANZAHLUNG
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_VORAUSZAHLUNG
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_LEERZEILE
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_SEITENUMBRUCH
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_PAUSCHALPOSITION
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_REPARATUR
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_RUECKSCHEIN
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_GUTSCHRIFT
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_FREMDARTIKEL
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_AUFTRAGSDATEN
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
		}
		if (RechnungFac.POSITIONSART_RECHNUNG_INTELLIGENTE_ZWISCHENSUMME
				.equals(rechnungpositionDto.getRechnungpositionartCNr())) {
			positionRpt.setSPositionsartCNr(rechnungpositionDto
					.getRechnungpositionartCNr());
			positionRpt.setSBezeichnung(rechnungpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(rechnungpositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(rechnungpositionDto.getNMenge());
			positionRpt.setSEinheit(rechnungpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(rechnungpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(rechnungpositionDto.getNNettoeinzelpreis());
		}

		return positionRpt;
	}

	private PositionRpt getAuftragPositionRpt(Integer iAuftragpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AuftragpositionDto auftragspositionDto = getAuftragpositionFac()
				.auftragpositionFindByPrimaryKey(iAuftragpositionIId);
		if (AuftragServiceFac.AUFTRAGPOSITIONART_BETRIFFT
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(auftragspositionDto.getCBez());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE
				.equals(auftragspositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					auftragspositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(auftragspositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(auftragspositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(auftragspositionDto.getNMenge());
			positionRpt.setSEinheit(auftragspositionDto.getEinheitCNr());
			positionRpt.setDRabatt(auftragspositionDto.getFRabattsatz());
			positionRpt.setBdPreis(auftragspositionDto.getNNettoeinzelpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setTTermin(auftragspositionDto
					.getTUebersteuerbarerLiefertermin());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_IDENT
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragspositionDto.getBelegIId());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					auftragspositionDto.getArtikelIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					auftragspositionDto, LocaleFac.BELEGART_AUFTRAG,
					artikelDto, locDruck, kundeDto.getPartnerIId(),
					theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(auftragspositionDto.getNMenge());
			positionRpt.setSEinheit(auftragspositionDto.getEinheitCNr());
			positionRpt.setDRabatt(auftragspositionDto.getFRabattsatz());
			positionRpt.setBdPreis(auftragspositionDto.getNNettoeinzelpreis());
			positionRpt.setBdMaterialzuschlag(auftragspositionDto
					.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto
					.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setTTermin(auftragspositionDto
					.getTUebersteuerbarerLiefertermin());
			positionRpt.setSEccn(artikelDto.getCEccn());

		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_LEERZEILE
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_SEITENUMBRUCH
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_TEXTBAUSTEIN
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							auftragspositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			positionRpt.setSText(auftragspositionDto.getXTextinhalt());
		} else if (AuftragServiceFac.AUFTRAGPOSITIONART_POSITION
				.equals(auftragspositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(auftragspositionDto
					.getPositionsartCNr());
			positionRpt.setSZusatzbezeichnung(auftragspositionDto
					.getCZusatzbez());
			positionRpt.setSTypCNr(auftragspositionDto.getTypCNr());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAngebotposition(Integer iAngebotpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AngebotpositionDto angebotPositionDto = getAngebotpositionFac()
				.angebotpositionFindByPrimaryKey(iAngebotpositionIId,
						theClientDto);
		if (AngebotServiceFac.ANGEBOTPOSITIONART_BETRIFFT
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(angebotPositionDto.getCBez());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_HANDEINGABE
				.equals(angebotPositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					angebotPositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(angebotPositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(angebotPositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_IDENT
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					angebotPositionDto.getArtikelIId(), theClientDto);
			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					angebotPositionDto.getBelegIId(), theClientDto);
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					angebotPositionDto, LocaleFac.BELEGART_ANGEBOT, artikelDto,
					locDruck, kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
			positionRpt.setBdMaterialzuschlag(angebotPositionDto
					.getNMaterialzuschlag());
			positionRpt.setSZusatzbezeichnung2(druckDto
					.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());

		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_LEERZEILE
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_SEITENUMBRUCH
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_TEXTBAUSTEIN
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							angebotPositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_TEXTEINGABE
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			positionRpt.setSText(angebotPositionDto.getXTextinhalt());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(
					angebotPositionDto.getAgstklIId());
			positionRpt.setSIdent(agstklDto.getCNr());
			positionRpt.setSBezeichnung(agstklDto.getCBez());
			positionRpt.setBdMenge(angebotPositionDto.getNMenge());
			positionRpt.setSEinheit(angebotPositionDto.getEinheitCNr());
			positionRpt.setDRabatt(angebotPositionDto.getFRabattsatz());
			positionRpt.setBdPreis(angebotPositionDto.getNNettoeinzelpreis());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_ENDSUMME
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
		} else if (AngebotServiceFac.ANGEBOTPOSITIONART_POSITION
				.equals(angebotPositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(angebotPositionDto
					.getPositionsartCNr());
			positionRpt.setSZusatzbezeichnung(angebotPositionDto
					.getCZusatzbez());
			positionRpt.setSTypCNr(angebotPositionDto.getTypCNr());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAnfrageposition(Integer iAnfragepositionIId,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		PositionRpt positionRpt = new PositionRpt();
		AnfragepositionDto anfragepositionDto = getAnfragepositionFac()
				.anfragepositionFindByPrimaryKey(iAnfragepositionIId,
						theClientDto);
		if (AnfrageServiceFac.ANFRAGEPOSITIONART_BETRIFFT
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(anfragepositionDto.getCBez());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE
				.equals(anfragepositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					anfragepositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(anfragepositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(anfragepositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(anfragepositionDto.getNMenge());
			positionRpt.setSEinheit(anfragepositionDto.getEinheitCNr());
			positionRpt.setBdPreis(anfragepositionDto.getNRichtpreis());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					anfragepositionDto.getArtikelIId(), theClientDto);
			AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(
					anfragepositionDto.getBelegIId(), theClientDto);
			if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(
								anfrageDto.getLieferantIIdAnfrageadresse(),
								theClientDto);
				Locale locDruck = Helper.string2Locale(lieferantDto
						.getPartnerDto().getLocaleCNrKommunikation());
				BelegPositionDruckIdentDto druckDto = printIdent(
						anfragepositionDto, LocaleFac.BELEGART_ANFRAGE,
						artikelDto, locDruck, lieferantDto.getPartnerIId(),
						theClientDto);
				positionRpt.setSIdent(druckDto.getSIdentnummer());
				positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
				positionRpt.setSZusatzbezeichnung(druckDto
						.getSZusatzBezeichnung());
				positionRpt.setSZusatzbezeichnung2(druckDto
						.getSArtikelZusatzBezeichnung2());
				positionRpt.setSText(druckDto.getSArtikelkommentar());
			}
			positionRpt.setBdMaterialzuschlag(anfragepositionDto
					.getNMaterialzuschlag());
			positionRpt.setBdMenge(anfragepositionDto.getNMenge());
			positionRpt.setSEinheit(anfragepositionDto.getEinheitCNr());
			positionRpt.setBdPreis(anfragepositionDto.getNRichtpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());

			ArtikellieferantDto artikellieferantDto = getArtikelFac()
					.getArtikelEinkaufspreis(
							anfragepositionDto.getArtikelIId(),
							anfrageDto.getLieferantIIdAnfrageadresse(),
							anfragepositionDto.getNMenge(),

							anfrageDto.getWaehrungCNr(),
							new java.sql.Date(anfrageDto
									.getTBelegdatum().getTime()),
							theClientDto);
			
			if (artikellieferantDto != null) {
				positionRpt.setSLieferantenArtikelnummer(artikellieferantDto
						.getCArtikelnrlieferant());
				positionRpt
						.setSLieferantenArtikelbezeichnung(artikellieferantDto
								.getCBezbeilieferant());
			}
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_LEERZEILE
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_SEITENUMBRUCH
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTBAUSTEIN
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							anfragepositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (AnfrageServiceFac.ANFRAGEPOSITIONART_TEXTEINGABE
				.equals(anfragepositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(anfragepositionDto
					.getPositionsartCNr());
			positionRpt.setSText(anfragepositionDto.getXTextinhalt());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getAGStuecklisteposition(
			Integer iAGStuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		AgstklpositionDto agstklpositionDto = getAngebotstklpositionFac()
				.agstklpositionFindByPrimaryKey(iAGStuecklistepositionIId,
						theClientDto);
		if (AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE
				.equals(agstklpositionDto.getAgstklpositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					agstklpositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());

			positionRpt.setSPositionsartCNr(agstklpositionDto
					.getAgstklpositionsartCNr());
			positionRpt.setSBezeichnung(agstklpositionDto.getCBez());
			positionRpt
					.setSZusatzbezeichnung(agstklpositionDto.getCZusatzbez());
			positionRpt.setBdMenge(agstklpositionDto.getNMenge());
			positionRpt.setSEinheit(agstklpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(agstklpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(agstklpositionDto.getNNettoeinzelpreis());
		} else if (AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT
				.equals(agstklpositionDto.getAgstklpositionsartCNr())) {
			positionRpt.setSPositionsartCNr(agstklpositionDto
					.getAgstklpositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					agstklpositionDto.getArtikelIId(), theClientDto);
			AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(
					agstklpositionDto.getAgstklIId());
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					agstklDto.getKundeIId(), theClientDto);
			Locale locDruck = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(agstklpositionDto,
					LocaleFac.BELEGART_AGSTUECKLISTE, artikelDto, locDruck,
					kundeDto.getPartnerIId(), theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setBdMenge(agstklpositionDto.getNMenge());
			positionRpt.setSEinheit(agstklpositionDto.getEinheitCNr());
			positionRpt.setDRabatt(agstklpositionDto.getFRabattsatz());
			positionRpt.setBdPreis(agstklpositionDto.getNNettoeinzelpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			positionRpt.setSEccn(artikelDto.getCEccn());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

	private PositionRpt getBestellposition(Integer iBestellpositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		PositionRpt positionRpt = new PositionRpt();
		BestellpositionDto bestellpositionDto = getBestellpositionFac()
				.bestellpositionFindByPrimaryKey(iBestellpositionIId);
		if (BestellpositionFac.BESTELLPOSITIONART_BETRIFFT
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(bestellpositionDto.getCBez());
		} else if (BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE
				.equals(bestellpositionDto.getPositionsartCNr())) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					bestellpositionDto.getArtikelIId(), theClientDto);
			positionRpt.setSIdent(artikelDto.getCNr());
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
			positionRpt.setSBezeichnung(bestellpositionDto.getCBez());
			positionRpt.setSZusatzbezeichnung(bestellpositionDto
					.getCZusatzbez());
			positionRpt.setBdMenge(bestellpositionDto.getNMenge());
			positionRpt.setSEinheit(bestellpositionDto.getEinheitCNr());
			positionRpt.setTTermin(bestellpositionDto
					.getTUebersteuerterLiefertermin());
			positionRpt.setDRabatt(bestellpositionDto.getDRabattsatz());
			positionRpt.setBdPreis(bestellpositionDto.getNNettoeinzelpreis());
			positionRpt.setSEccn(artikelDto.getCEccn());
		} else if (BestellpositionFac.BESTELLPOSITIONART_IDENT
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					bestellpositionDto.getArtikelIId(), theClientDto);
			BestellungDto bestellungDto = getBestellungFac()
					.bestellungFindByPrimaryKey(
							bestellpositionDto.getBestellungIId());
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(
							bestellungDto.getLieferantIIdBestelladresse(),
							theClientDto);
			Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			BelegPositionDruckIdentDto druckDto = printIdent(
					bestellpositionDto, LocaleFac.BELEGART_BESTELLUNG,
					artikelDto, locDruck, lieferantDto.getPartnerIId(),
					theClientDto);
			positionRpt.setSIdent(druckDto.getSIdentnummer());
			positionRpt.setSBezeichnung(druckDto.getSBezeichnung());
			positionRpt.setSZusatzbezeichnung(druckDto.getSZusatzBezeichnung());
			positionRpt.setSZusatzbezeichnung2(druckDto
					.getSArtikelZusatzBezeichnung2());
			positionRpt.setSText(druckDto.getSArtikelkommentar());
			positionRpt.setBdMaterialzuschlag(bestellpositionDto
					.getNMaterialzuschlag());
			positionRpt.setBdMenge(bestellpositionDto.getNMenge());
			positionRpt.setSEinheit(bestellpositionDto.getEinheitCNr());
			positionRpt.setTTermin(bestellpositionDto
					.getTUebersteuerterLiefertermin());
			positionRpt.setDRabatt(bestellpositionDto.getDRabattsatz());
			positionRpt.setBdPreis(bestellpositionDto.getNNettoeinzelpreis());
			positionRpt.setFVerpackungsmenge(artikelDto.getFVerpackungsmenge());
			
			ArtikellieferantDto artikellieferantDto = getArtikelFac()
					.getArtikelEinkaufspreis(
							bestellpositionDto.getArtikelIId(),
							bestellungDto
									.getLieferantIIdBestelladresse(),
									bestellpositionDto.getNMenge(),

							bestellungDto.getWaehrungCNr(),
							new java.sql.Date(bestellungDto
									.getDBelegdatum().getTime()),
							theClientDto);
			
			if (artikellieferantDto != null) {
				positionRpt.setSLieferantenArtikelnummer(artikellieferantDto
						.getCArtikelnrlieferant());
				positionRpt
						.setSLieferantenArtikelbezeichnung(artikellieferantDto
								.getCBezbeilieferant());
			}
		} else if (BestellpositionFac.BESTELLPOSITIONART_LEERZEILE
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
		} else if (BestellpositionFac.BESTELLPOSITIONART_SEITENUMBRUCH
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
		} else if (BestellpositionFac.BESTELLPOSITIONART_TEXTBAUSTEIN
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
			MediastandardDto oMediastandardDto = getMediaFac()
					.mediastandardFindByPrimaryKey(
							bestellpositionDto.getMediastandardIId());
			// zum Drucken vorbereiten
			BelegPositionDruckTextbausteinDto druckDto = LPReport
					.printTextbaustein(oMediastandardDto, theClientDto);
			positionRpt.setSText(druckDto.getSFreierText());
			positionRpt
					.setOImage(Helper.imageToByteArray(druckDto.getOImage()));
		} else if (BestellpositionFac.BESTELLPOSITIONART_TEXTEINGABE
				.equals(bestellpositionDto.getPositionsartCNr())) {
			positionRpt.setSPositionsartCNr(bestellpositionDto
					.getPositionsartCNr());
			positionRpt.setSText(bestellpositionDto.getXTextinhalt());
		} else {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,null);
		}
		return positionRpt;
	}

}
