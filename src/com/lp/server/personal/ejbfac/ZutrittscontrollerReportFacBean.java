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
package com.lp.server.personal.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.personal.fastlanereader.generated.FLRZutrittdaueroffen;
import com.lp.server.personal.fastlanereader.generated.FLRZutrittsklasseobjekt;
import com.lp.server.personal.fastlanereader.generated.FLRZutrittslog;
import com.lp.server.personal.fastlanereader.generated.FLRZutrittsmodelltagdetail;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittscontrollerReportFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;

@Stateless
@Interceptors(TimingInterceptor.class)
public class ZutrittscontrollerReportFacBean extends LPReport implements
		ZutrittscontrollerReportFac, JRDataSource {

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_ZUTRITTSJOURNAL_PERSON = 0;
	private static int REPORT_ZUTRITTSJOURNAL_ZEITPUNKT = 1;
	private static int REPORT_ZUTRITTSJOURNAL_AUSWEIS = 2;
	private static int REPORT_ZUTRITTSJOURNAL_OBJEKT = 3;
	private static int REPORT_ZUTRITTSJOURNAL_CONTROLLER = 4;
	private static int REPORT_ZUTRITTSJOURNAL_MANDANT = 5;
	private static int REPORT_ZUTRITTSJOURNAL_ERLAUBT = 6;

	private static int REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE = 0;
	private static int REPORT_ZUTRITTDEFINITION_MANDANT = 1;
	private static int REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT = 2;
	private static int REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL = 3;
	private static int REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG = 4;
	private static int REPORT_ZUTRITTDEFINITION_OFFENVON = 5;
	private static int REPORT_ZUTRITTDEFINITION_OFFENBIS = 6;

	public JasperPrintLP printZutrittsjournal(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = ZutrittscontrollerReportFac.REPORT_ZUTRITTSJOURNAL;

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(
						com.lp.server.personal.fastlanereader.generated.FLRZutrittslog.class)
				.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.ge(
				ZutrittscontrollerFac.FLR_ZUTRITTSLOG_T_ZEITPUNKT, tVon));
		crit.add(Restrictions.le(
				ZutrittscontrollerFac.FLR_ZUTRITTSLOG_T_ZEITPUNKT, tBis));

		// Wenn Hauptmandant
		try {
			String hauptmandant = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getAnwenderDto()
					.getMandantCNrHauptmandant();
			if (!hauptmandant.equals(theClientDto.getMandant())) {

				crit
						.add(Restrictions
								.or(
										Restrictions
												.isNull(ZutrittscontrollerFac.FLR_ZUTRITTSLOG_MANDANT_C_NR_OBJEKT),
										Restrictions
												.or(
														Restrictions
																.eq(
																		ZutrittscontrollerFac.FLR_ZUTRITTSLOG_MANDANT_C_NR_OBJEKT,
																		theClientDto
																				.getMandant()),
														Restrictions
																.eq(
																		ZutrittscontrollerFac.FLR_ZUTRITTSLOG_FLRMANDANT
																				+ ".c_nr",
																				theClientDto
																				.getMandant()))));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		crit.addOrder(Order
				.desc(ZutrittscontrollerFac.FLR_ZUTRITTSLOG_T_ZEITPUNKT));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();
		data = new Object[results.size()][7];
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRZutrittslog log = (FLRZutrittslog) resultListIterator.next();
			data[row][REPORT_ZUTRITTSJOURNAL_AUSWEIS] = log.getC_ausweis();
			data[row][REPORT_ZUTRITTSJOURNAL_CONTROLLER] = log
					.getC_zutrittscontroller();
			data[row][REPORT_ZUTRITTSJOURNAL_ERLAUBT] = log.getB_erlaubt();
			data[row][REPORT_ZUTRITTSJOURNAL_MANDANT] = log.getMandant_c_nr();
			data[row][REPORT_ZUTRITTSJOURNAL_OBJEKT] = log
					.getC_zutrittsobjekt();
			data[row][REPORT_ZUTRITTSJOURNAL_PERSON] = log.getC_person();
			data[row][REPORT_ZUTRITTSJOURNAL_ZEITPUNKT] = new java.sql.Timestamp(
					log.getT_zeitpunkt().getTime());
			row++;
		}
		session.close();
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("P_VON", tVon);
		parameter.put("P_BIS", tBis);
		initJRDS(parameter, ZutrittscontrollerReportFac.REPORT_MODUL,
				ZutrittscontrollerReportFac.REPORT_ZUTRITTSJOURNAL, theClientDto
						.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printZutrittsdefinition(TheClientDto theClientDto) {

		index = -1;
		sAktuellerReport = ZutrittscontrollerReportFac.REPORT_ZUTRITTDEFINITION;
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRZutrittsklasseobjekt.class);
		crit
				.createAlias(
						ZutrittscontrollerFac.FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSKLASSE,
						"k");

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			String mandant = mandantDto.getCNr();
			if (mandantDto.getCKbez() != null) {
				mandant += " " + mandantDto.getCKbez();
			}
			if (!theClientDto.getMandant().equals(
					mandantDto.getAnwenderDto().getMandantCNrHauptmandant())) {
				crit.add(Restrictions.eq("k.mandant_c_nr", theClientDto
						.getMandant()));

				parameter.put("P_MANDANT", mandant);
			} else {
				parameter.put("P_MANDANT", "Alle");
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		crit.addOrder(Order.asc("k.c_nr"));

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();
		// data = new Object[results.size()][7];
		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();
		while (resultListIterator.hasNext()) {
			FLRZutrittsklasseobjekt klasseobjekt = (FLRZutrittsklasseobjekt) resultListIterator
					.next();

			// MODELL + TAGESARTEN + ZEITEN
			String sQuery = "from FLRZutrittsmodelltagdetail zutrittmodelltagdetail WHERE zutrittmodelltagdetail.flrzutrittsmodelltag.flrzutrittsmodell.i_id="
					+ klasseobjekt.getFlrzutrittsmodell().getI_id()
					+ " ORDER BY zutrittmodelltagdetail.flrzutrittsmodelltag.flrtagesart.i_sort ASC ";
			Session session2 = FLRSessionFactory.getFactory().openSession();

			Query modellzeiten = session2.createQuery(sQuery);

			List<?> resultListModellzeiten = modellzeiten.list();

			Iterator<?> resultListIteratorModellzeiten = resultListModellzeiten
					.iterator();
			while (resultListIteratorModellzeiten.hasNext()) {
				FLRZutrittsmodelltagdetail modelltagdetail = (FLRZutrittsmodelltagdetail) resultListIteratorModellzeiten
						.next();

				Object[] o = new Object[7];

				String klasse = klasseobjekt.getFlrzutrittsklasse().getC_nr();
				if (klasseobjekt.getFlrzutrittsklasse().getC_bez() != null) {
					klasse += "- "
							+ klasseobjekt.getFlrzutrittsklasse().getC_bez();
				}
				o[REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE] = klasse;
				o[REPORT_ZUTRITTDEFINITION_MANDANT] = klasseobjekt
						.getFlrzutrittsklasse().getMandant_c_nr();

				String objekt = klasseobjekt.getFlrzutrittsobjekt().getC_nr();
				if (klasseobjekt.getFlrzutrittsobjekt().getC_bez() != null) {
					objekt += " "
							+ klasseobjekt.getFlrzutrittsobjekt().getC_bez();
				}

				o[REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT] = objekt;

				o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL] = klasseobjekt
						.getFlrzutrittsmodell().getC_nr();

				o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG] = modelltagdetail
						.getFlrzutrittsmodelltag().getFlrtagesart().getC_nr();
				o[REPORT_ZUTRITTDEFINITION_OFFENVON] = modelltagdetail
						.getU_offenvon();
				o[REPORT_ZUTRITTDEFINITION_OFFENBIS] = modelltagdetail
						.getU_offenbis();
				alDaten.add(o);
			}
			session2.close();
		}
		session.close();

		// DAUEROFFEN einfuegen

		session = FLRSessionFactory.getFactory().openSession();

		crit = session.createCriteria(FLRZutrittdaueroffen.class);
		crit.createAlias(
				ZutrittscontrollerFac.FLR_ZUTRITTDAUEROFFEN_FLRZUTRITTSOBJEKT,
				"o");

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			String mandant = mandantDto.getCNr();
			if (mandantDto.getCKbez() != null) {
				mandant += " " + mandantDto.getCKbez();
			}
			if (!theClientDto.getMandant().equals(
					mandantDto.getAnwenderDto().getMandantCNrHauptmandant())) {
				crit.add(Restrictions.eq("o.mandant_c_nr", theClientDto
						.getMandant()));

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		crit.addOrder(Order.asc("o.c_nr"));

		results = crit.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRZutrittdaueroffen daueroffen = (FLRZutrittdaueroffen) resultListIterator
					.next();

			Object[] o = new Object[7];
			o[REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE] = "Daueroffen";
			if (daueroffen.getFlrzutrittsobjekt().getMandant_c_nr() != null) {
				o[REPORT_ZUTRITTDEFINITION_MANDANT] = daueroffen
						.getFlrzutrittsobjekt().getMandant_c_nr();
			} else {
				o[REPORT_ZUTRITTDEFINITION_MANDANT] = "Allgemein";
			}

			String objekt = daueroffen.getFlrzutrittsobjekt().getC_nr();
			if (daueroffen.getFlrzutrittsobjekt().getC_bez() != null) {
				objekt += " " + daueroffen.getFlrzutrittsobjekt().getC_bez();
			}

			o[REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT] = objekt;
			o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL] = "---";
			o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG] = daueroffen
					.getFlrtagesart().getC_nr();
			o[REPORT_ZUTRITTDEFINITION_OFFENVON] = daueroffen.getU_offenvon();
			o[REPORT_ZUTRITTDEFINITION_OFFENBIS] = daueroffen.getU_offenbis();
			alDaten.add(o);
		}
		session.close();

		data = new Object[alDaten.size()][7];
		for (int i = 0; i < alDaten.size(); i++) {
			Object[] o = (Object[]) alDaten.get(i);
			data[i][REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE] = o[REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE];
			data[i][REPORT_ZUTRITTDEFINITION_MANDANT] = o[REPORT_ZUTRITTDEFINITION_MANDANT];
			data[i][REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT] = o[REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT];
			data[i][REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL] = o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL];
			data[i][REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG] = o[REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG];
			data[i][REPORT_ZUTRITTDEFINITION_OFFENVON] = o[REPORT_ZUTRITTDEFINITION_OFFENVON];
			data[i][REPORT_ZUTRITTDEFINITION_OFFENBIS] = o[REPORT_ZUTRITTDEFINITION_OFFENBIS];
		}

		initJRDS(parameter, ZeiterfassungReportFac.REPORT_MODUL,
				ZutrittscontrollerReportFac.REPORT_ZUTRITTDEFINITION, theClientDto
						.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();

	}

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();
		if (sAktuellerReport
				.equals(ZutrittscontrollerReportFac.REPORT_ZUTRITTSJOURNAL)) {
			if ("Ausweis".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_AUSWEIS];
			} else if ("Controller".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_CONTROLLER];
			} else if ("Erlaubt".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_ERLAUBT];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_MANDANT];
			} else if ("Objekt".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_OBJEKT];
			} else if ("Person".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_PERSON];
			} else if ("Zeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTSJOURNAL_ZEITPUNKT];
			}
		} else if (sAktuellerReport
				.equals(ZutrittscontrollerReportFac.REPORT_ZUTRITTDEFINITION)) {
			if ("Zutrittsklasse".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_ZUTRITTSKLASSE];
			} else if ("Mandant".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_MANDANT];
			} else if ("Zutrittsobjekt".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_ZUTRITTSOBJEKT];
			} else if ("Zutrittsmodell".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELL];
			} else if ("Zutrittsmodelltag".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_ZUTRITTSMODELLTAG];
			} else if ("Offenvon".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_OFFENVON];
			} else if ("Offenbis".equals(fieldName)) {
				value = data[index][REPORT_ZUTRITTDEFINITION_OFFENBIS];
			}
		}

		return value;
	}

}
