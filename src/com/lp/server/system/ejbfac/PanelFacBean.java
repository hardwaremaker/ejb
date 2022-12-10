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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.projekt.ejb.Historyart;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.ejb.Panel;
import com.lp.server.system.ejb.Panelbeschreibung;
import com.lp.server.system.ejb.Paneldaten;
import com.lp.server.system.ejb.PaneldatenQuery;
import com.lp.server.system.ejb.Panelsperren;
import com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.CreatePaneldatenResult;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.PanelDto;
import com.lp.server.system.service.PanelDtoAssembler;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PanelbeschreibungDtoAssembler;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.PaneldatenDtoAssembler;
import com.lp.server.system.service.PanelsperrenDto;
import com.lp.server.system.service.PanelsperrenDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class PanelFacBean extends LPReport implements PanelFac, JRDataSource {
	@PersistenceContext
	private EntityManager em;

	private Object[][] data = null;

	private String[] felder = new String[0];

	public static String XLS_IMPORT_SPALTE_WERT = "Wert";

	private String fehlerZeileXLSImport = "";

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();

		for (int i = 0; i < felder.length; i++) {
			if (felder[i].equals(fieldName)) {
				value = data[index][i];
			}
		}
		return value;
	}

	public Integer createPanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto,
			boolean bFuegeNeuePositionVorDerSelektiertenEin) throws EJBExceptionLP {
		if (panelbeschreibungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("paneldatenDto == null"));
		}
		if (panelbeschreibungDto.getCName() == null || panelbeschreibungDto.getCTyp() == null
				|| panelbeschreibungDto.getPanelCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"panelbeschreibungDto.getCName() == null || panelbeschreibungDto.getCTyp() == null || panelbeschreibungDto.getPanelCNr() == null"));
		}

		if (bFuegeNeuePositionVorDerSelektiertenEin) {
			Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrIGridy");
			query.setParameter(1, panelbeschreibungDto.getPanelCNr());
			query.setParameter(2, panelbeschreibungDto.getMandantCNr());
			query.setParameter(3, panelbeschreibungDto.getIGridy());
			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Panelbeschreibung p = (Panelbeschreibung) it.next();
				p.setIGridy(p.getIGridy() + 1);
				em.merge(p);
				em.flush();
			}

		}

		try {
			Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCName");
			query.setParameter(1, panelbeschreibungDto.getPanelCNr());
			query.setParameter(2, panelbeschreibungDto.getMandantCNr());
			query.setParameter(3, panelbeschreibungDto.getCName());
			Panelbeschreibung panelbeschreibung = (Panelbeschreibung) query.getSingleResult();
			if (panelbeschreibung != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("LP_PANELBESCHREIBUNG.UK"));
			}
		} catch (NoResultException ex) {
			//
		}
		try {

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELBESCHREIBUNG);
			panelbeschreibungDto.setIId(pk);

			Panelbeschreibung panelbeschreibung = new Panelbeschreibung(panelbeschreibungDto.getIId(),
					panelbeschreibungDto.getPanelCNr(), panelbeschreibungDto.getMandantCNr(),
					panelbeschreibungDto.getCName(), panelbeschreibungDto.getCTyp(), panelbeschreibungDto.getIGridx(),
					panelbeschreibungDto.getIGridy(), panelbeschreibungDto.getIGridwidth(),
					panelbeschreibungDto.getIGridheigth(), panelbeschreibungDto.getCFill(),
					panelbeschreibungDto.getCAnchor(), panelbeschreibungDto.getIInsetsleft(),
					panelbeschreibungDto.getIInsetsright(), panelbeschreibungDto.getIInsetstop(),
					panelbeschreibungDto.getIInsetsbottom(), panelbeschreibungDto.getIIpadx(),
					panelbeschreibungDto.getIIpady(), panelbeschreibungDto.getBMandatory(),
					panelbeschreibungDto.getFWeighty(), panelbeschreibungDto.getFWeightx(),
					panelbeschreibungDto.getCDruckname(), panelbeschreibungDto.getArtgruIId(),
					panelbeschreibungDto.getBUeberschrift());
			em.persist(panelbeschreibung);
			em.flush();
			setPanelbeschreibungFromPanelbeschreibungDto(panelbeschreibung, panelbeschreibungDto);
			return panelbeschreibungDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePanelbeschreibung(Integer iId) throws EJBExceptionLP {
		Panelbeschreibung toRemove = em.find(Panelbeschreibung.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePanelsperren(Integer iId) {
		Panelsperren toRemove = em.find(Panelsperren.class, iId);

		em.remove(toRemove);
		em.flush();

	}

	public void removePanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto) throws EJBExceptionLP {
		if (panelbeschreibungDto != null) {
			Integer iId = panelbeschreibungDto.getIId();
			removePanelbeschreibung(iId);
		}
	}

	public void updatePanelbeschreibung(PanelbeschreibungDto panelbeschreibungDto) throws EJBExceptionLP {
		if (panelbeschreibungDto != null) {
			Integer iId = panelbeschreibungDto.getIId();
			// try {
			Panelbeschreibung panelbeschreibung = em.find(Panelbeschreibung.class, iId);
			if (panelbeschreibung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			try {
				Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCName");
				query.setParameter(1, panelbeschreibungDto.getPanelCNr());
				query.setParameter(2, panelbeschreibungDto.getMandantCNr());
				query.setParameter(3, panelbeschreibungDto.getCName());
				Panelbeschreibung duplicateHelper = (Panelbeschreibung) query.getSingleResult();
				Integer iIdVorhanden = duplicateHelper.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("LP_PANELBESCHREIBUNG.UK"));
				}

			} catch (NoResultException ex) {
				//
			}

			setPanelbeschreibungFromPanelbeschreibungDto(panelbeschreibung, panelbeschreibungDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Panelbeschreibung panelbeschreibung = em.find(Panelbeschreibung.class, iId);
		if (panelbeschreibung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePanelbeschreibungDto(panelbeschreibung);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNr(String panelCNr, String mandantCNr,
			Integer artgruIId) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator.next();

				if (panelbeschreibung.getCTyp().startsWith("WrapperComboBox")) {
					int u = 0;
				}

				if (artgruIId != null && panelbeschreibung.getArtgruIId() != null
						&& !artgruIId.equals(panelbeschreibung.getArtgruIId())) {
					continue;
				}

				if (artgruIId == null && panelbeschreibung.getArtgruIId() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public PanelbeschreibungDto panelbeschreibungFindByPanelCNrMandantCNrCNameOhneExc(String panelCNr,
			String mandantCNr, String cName) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCName");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		query.setParameter(3, cName);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {
			return assemblePanelbeschreibungDto((Panelbeschreibung) cl.iterator().next());
		} else {
			return null;
		}
	}

	public PanelbeschreibungDto panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(String panelCNr,
			String mandantCNr, String cName) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCDruckname");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		query.setParameter(3, cName);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {
			return assemblePanelbeschreibungDto((Panelbeschreibung) cl.iterator().next());
		} else {
			return null;
		}
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(String panelCNr,
			String mandantCNr, Integer partnerklasseIId) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator.next();

				if (panelbeschreibung.getCTyp().startsWith("WrapperComboBox")) {
					int u = 0;
				}

				if (partnerklasseIId != null && panelbeschreibung.getPartnerklasseIId() != null
						&& !partnerklasseIId.equals(panelbeschreibung.getPartnerklasseIId())) {
					continue;
				}

				if (partnerklasseIId == null && panelbeschreibung.getPartnerklasseIId() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrKostenstelleIId(String panelCNr,
			String mandantCNr, Integer kostenstelleIId, String projekttypCNr) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator.next();

				if (panelbeschreibung.getCTyp().startsWith("WrapperComboBox")) {
					int u = 0;
				}

				if (kostenstelleIId != null && panelbeschreibung.getKostenstelleIId() != null
						&& !kostenstelleIId.equals(panelbeschreibung.getKostenstelleIId())) {
					continue;
				}

				if (kostenstelleIId == null && panelbeschreibung.getKostenstelleIId() != null) {
					continue;
				}

				// PJ20263
				if (projekttypCNr != null && panelbeschreibung.getProjekttypCNr() != null
						&& !projekttypCNr.equals(panelbeschreibung.getProjekttypCNr())) {
					continue;
				}

				if (projekttypCNr == null && panelbeschreibung.getProjekttypCNr() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrBereichIId(String panelCNr,
			String mandantCNr, Integer bereichIId, String projekttypCNr) {
		// try {
		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator.next();

				if (bereichIId != null && panelbeschreibung.getBereichIId() != null
						&& !bereichIId.equals(panelbeschreibung.getBereichIId())) {
					continue;
				}

				if (bereichIId == null && panelbeschreibung.getBereichIId() != null) {
					continue;
				}

				// PJ20263

				if (projekttypCNr != null && panelbeschreibung.getProjekttypCNr() != null
						&& !projekttypCNr.equals(panelbeschreibung.getProjekttypCNr())) {
					continue;
				}

				if (projekttypCNr == null && panelbeschreibung.getProjekttypCNr() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr) throws EJBExceptionLP {

		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr, Integer artgruIId)
			throws EJBExceptionLP {

		Query query = em.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrArtgruIId");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		query.setParameter(3, artgruIId);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public JasperPrintLP printPanel(String panelCNr, String report, String cKey, TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();

		parameter.put("P_KEY", cKey);
		parameter.put("P_PANEL_C_NR", panelCNr);

		index = -1;

		Session session = FLRSessionFactory.getFactory().openSession();

		String[] typen = new String[5];
		typen[0] = PanelFac.TYP_WRAPPERCHECKBOX;
		typen[1] = PanelFac.TYP_WRAPPEREDITOR;
		typen[2] = PanelFac.TYP_WRAPPERTEXTFIELD;
		typen[3] = PanelFac.TYP_WRAPPERCOMBOBOX;
		typen[4] = PanelFac.TYP_WRAPPERTEXTAREA;

		org.hibernate.Criteria crit = session.createCriteria(FLRPanelbeschreibung.class);
		crit.add(Restrictions.in(PanelFac.FLR_PANELBESCHREIBUNG_C_TYP, typen));
		crit.add(Restrictions.eq(PanelFac.FLR_PANELBESCHREIBUNG_PANEL_C_NR, panelCNr));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		felder = new String[results.size()];
		data = new Object[1][results.size()];

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRPanelbeschreibung flrPanelbeschreibung = (FLRPanelbeschreibung) resultListIterator.next();
			felder[row] = flrPanelbeschreibung.getC_name();

			try {
				Query query = em.createNamedQuery("PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
				query.setParameter(1, panelCNr);
				query.setParameter(2, flrPanelbeschreibung.getI_id());
				query.setParameter(3, cKey);
				Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
				if (paneldaten == null) {
					data[0][row] = flrPanelbeschreibung.getC_default();
				} else {
					if (paneldaten.getOInhalt() != null) {
						data[0][row] = new String(paneldaten.getOInhalt());
					} else if (paneldaten.getXInhalt() != null) {
						data[0][row] = new String(paneldaten.getXInhalt());
					}
				}

			} catch (NoResultException ex) {
				data[0][row] = flrPanelbeschreibung.getC_default();
			}
			row++;
		}
		session.close();

		initJRDS(parameter, PanelFac.REPORT_MODUL, report, theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private void setPanelbeschreibungFromPanelbeschreibungDto(Panelbeschreibung panelbeschreibung,
			PanelbeschreibungDto panelbeschreibungDto) {
		panelbeschreibung.setPanelCNr(panelbeschreibungDto.getPanelCNr());
		panelbeschreibung.setCName(panelbeschreibungDto.getCName());
		panelbeschreibung.setCTyp(panelbeschreibungDto.getCTyp());
		panelbeschreibung.setCTokeninresourcebundle(panelbeschreibungDto.getCTokeninresourcebundle());
		panelbeschreibung.setIGridx(panelbeschreibungDto.getIGridx());
		panelbeschreibung.setIGridy(panelbeschreibungDto.getIGridy());
		panelbeschreibung.setIGridwidth(panelbeschreibungDto.getIGridwidth());
		panelbeschreibung.setIGridheigth(panelbeschreibungDto.getIGridheigth());
		panelbeschreibung.setCFill(panelbeschreibungDto.getCFill());
		panelbeschreibung.setCAnchor(panelbeschreibungDto.getCAnchor());
		panelbeschreibung.setIInsetsleft(panelbeschreibungDto.getIInsetsleft());
		panelbeschreibung.setIInsetsright(panelbeschreibungDto.getIInsetsright());
		panelbeschreibung.setIInsetstop(panelbeschreibungDto.getIInsetstop());
		panelbeschreibung.setIInsetsbottom(panelbeschreibungDto.getIInsetsbottom());
		panelbeschreibung.setIIpadx(panelbeschreibungDto.getIIpadx());
		panelbeschreibung.setIIpady(panelbeschreibungDto.getIIpady());
		panelbeschreibung.setFWeightx(panelbeschreibungDto.getFWeightx());
		panelbeschreibung.setFWeighty(panelbeschreibungDto.getFWeighty());
		panelbeschreibung.setBMandatory(panelbeschreibungDto.getBMandatory());
		panelbeschreibung.setMandantCNr(panelbeschreibungDto.getMandantCNr());
		panelbeschreibung.setArtgruIId(panelbeschreibungDto.getArtgruIId());
		panelbeschreibung.setCDruckname(panelbeschreibungDto.getCDruckname());
		panelbeschreibung.setPartnerklasseIId(panelbeschreibungDto.getPartnerklasseIId());
		panelbeschreibung.setKostenstelleIId(panelbeschreibungDto.getKostenstelleIId());
		panelbeschreibung.setBereichIId(panelbeschreibungDto.getBereichIId());
		panelbeschreibung.setCDefault(panelbeschreibungDto.getCDefault());
		panelbeschreibung.setProjekttypCNr(panelbeschreibungDto.getProjekttypCNr());
		panelbeschreibung.setBUeberschrift(panelbeschreibungDto.getBUeberschrift());
		em.merge(panelbeschreibung);
		em.flush();
	}

	private PanelbeschreibungDto assemblePanelbeschreibungDto(Panelbeschreibung panelbeschreibung) {
		return PanelbeschreibungDtoAssembler.createDto(panelbeschreibung);
	}

	private PanelbeschreibungDto[] assemblePanelbeschreibungDtos(Collection<?> panelbeschreibungs) {
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (panelbeschreibungs != null) {
			Iterator<?> iterator = panelbeschreibungs.iterator();
			while (iterator.hasNext()) {
				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator.next();
				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	@Override
	/**
	 * Erzeugen der Paneldaten, mit &Uuml;berpr&uuml;fung ob die Panel-Beschreibung-Id
	 * der Paneleigenschaft (ARTIKELEIGENSCHAFT, CHARGENEIGENSCHAFT, ...)
	 * entspricht</br>
	 * 
	 * @param panelCnr
	 * @param panelDtos
	 * @param theClientDto
	 */
	public void createPaneldaten(String panelCnr, 
			PaneldatenDto[] panelDtos, TheClientDto theClientDto) {
		Validator.dtoNotNull(panelDtos, "PaneldatenDto");
		Validator.notEmpty(panelCnr, "panelCnr");

		for (PaneldatenDto panelDto : panelDtos) {
			Validator.notNull(panelDto.getPanelbeschreibungIId(), "beschreibungIId");
			PanelbeschreibungDto beschreibungDto = 
					panelbeschreibungFindByPrimaryKey(panelDto.getPanelbeschreibungIId());
			if (beschreibungDto.getPanelCNr().equals(panelCnr)) {
				panelDto.setPanelCNr(panelCnr);
			} else {
				Validator.entityFound(null, panelDto.getPanelbeschreibungIId());
			}
		}
		createPaneldaten(panelDtos, theClientDto);
	}
	
	@Override
	public void updatePaneldaten(String panelCnr,
			PaneldatenDto[] panelDtos, TheClientDto theClientDto) {
		Validator.dtoNotNull(panelDtos, "PaneldatenDto");
		Validator.notEmpty(panelCnr, "panelCnr");
		
		for (PaneldatenDto panelDto : panelDtos) {
			Validator.notNull(panelDto.getPanelbeschreibungIId(), "beschreibungIId");
			PanelbeschreibungDto beschreibungDto = 
					panelbeschreibungFindByPrimaryKey(panelDto.getPanelbeschreibungIId());
			if (beschreibungDto.getPanelCNr().equals(panelCnr)) {
				panelDto.setPanelCNr(panelCnr);
			} else {
				Validator.entityFound(null, panelDto.getPanelbeschreibungIId());
			}
			PaneldatenDto dto = paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
					panelCnr, beschreibungDto.getIId(), panelDto.getCKey());
			if (dto != null) {
				dto.setXInhalt(Helper.isStringEmpty(panelDto.getXInhalt()) ? "" : panelDto.getXInhalt());
				dto.setOInhalt(panelDto.getOInhalt());
				dto.setCDatentypkey(Helper.isStringEmpty(
						panelDto.getCDatentypkey()) ? "java.lang.String" : panelDto.getCDatentypkey());
				updatePaneldaten(dto);
			} else {
				myLogger.warn("Paneldaten not found for: " + panelDto.getPanelCNr() + 
						", beschreibungId: " + beschreibungDto.getIId() +
						", cKey: " + panelDto.getCKey());
			}
		}
	}
	
	
	public void createPaneldaten(PaneldatenDto[] paneldatenDto, TheClientDto theClientDto) {
		if (paneldatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("paneldatenDto == null"));
		}

		// Zuerst alle loeschen

		PaneldatenDto[] paneldatenDtoVorhanden = null;

		if (paneldatenDto.length > 0) {
			Query query = em.createNamedQuery("PaneldatenfindByPanelCNrCKey");
			query.setParameter(1, paneldatenDto[0].getPanelCNr());
			query.setParameter(2, paneldatenDto[0].getCKey());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();

			paneldatenDtoVorhanden = assemblePaneldatenDtos(cl);

		}

		// Wenn gleich, dann nichts machen
		if (paneldatenDtoVorhanden != null && paneldatenDto != null) {

			if (paneldatenDtoVorhanden.length == paneldatenDto.length) {

				boolean bGleich = true;

				for (int i = 0; i < paneldatenDtoVorhanden.length; i++) {
					if (!paneldatenDtoVorhanden[i].equals(paneldatenDto[i])) {
						bGleich = false;
					}
				}

				if (bGleich == true) {
					return;
				}
			}

		}

		if (paneldatenDtoVorhanden != null && paneldatenDtoVorhanden.length > 0) {
			for (int i = 0; i < paneldatenDtoVorhanden.length; i++) {

				Paneldaten p = em.find(Paneldaten.class, paneldatenDtoVorhanden[i].getIId());

				HvDtoLogger<PaneldatenDto> hvLogger = new HvDtoLogger<PaneldatenDto>(em, p.getCKey(), theClientDto);
				hvLogger.logDelete(paneldatenDtoVorhanden[i]);

				em.remove(p);
				em.flush();
			}
		}

		for (int i = 0; i < paneldatenDto.length; i++) {
			if (paneldatenDto[i].getPanelbeschreibungIId() != null) {

				/*
				 * ArtikelDto artikelDto_Vorher = artikelFindByPrimaryKey(
				 * artikelDto_Aktuell.getIId(), theClientDto);
				 * 
				 * HvDtoLogger<ArtikelDto> artikelLogger = new HvDtoLogger<ArtikelDto>(em,
				 * theClientDto); artikelLogger.log(artikelDto_Vorher, artikelDto_Aktuell);
				 */

				try {
					Paneldaten paneldaten;
					try {
						Query query = em.createNamedQuery("PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
						query.setParameter(1, paneldatenDto[i].getPanelCNr());
						query.setParameter(2, paneldatenDto[i].getPanelbeschreibungIId());
						query.setParameter(3, paneldatenDto[i].getCKey());
						paneldaten = (Paneldaten) query.getSingleResult();
						setPaneldatenFromPaneldatenDto(paneldaten, paneldatenDto[i]);
					} catch (NoResultException e) {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELDATEN);
						paneldatenDto[i].setIId(pk);
						paneldaten = new Paneldaten(paneldatenDto[i].getIId(), paneldatenDto[i].getPanelCNr(),
								paneldatenDto[i].getPanelbeschreibungIId(), paneldatenDto[i].getCKey(),
								paneldatenDto[i].getCDatentypkey());
						em.persist(paneldaten);
						em.flush();
						setPaneldatenFromPaneldatenDto(paneldaten, paneldatenDto[i]);
					} catch (NonUniqueResultException ex1) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
					}

					PaneldatenDto pdDto = assemblePaneldatenDto(paneldaten);

					HvDtoLogger<PaneldatenDto> hvLogger = new HvDtoLogger<PaneldatenDto>(em, pdDto.getCKey(),
							theClientDto);
					hvLogger.logInsert(pdDto);

				} catch (EntityExistsException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
				}
			}
		}
	}

	public void removePaneldaten(Integer iId) throws EJBExceptionLP {
		Paneldaten toRemove = em.find(Paneldaten.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePaneldaten(PaneldatenDto paneldatenDto) throws EJBExceptionLP {
		if (paneldatenDto != null) {
			Integer iId = paneldatenDto.getIId();
			removePaneldaten(iId);
		}
	}

	public void updatePaneldaten(PaneldatenDto paneldatenDto) throws EJBExceptionLP {
		if (paneldatenDto != null) {
			Integer iId = paneldatenDto.getIId();
			// try {
			Paneldaten paneldaten = em.find(Paneldaten.class, iId);
			if (paneldaten == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPaneldatenFromPaneldatenDto(paneldaten, paneldatenDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public PaneldatenDto paneldatenFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Paneldaten paneldaten = em.find(Paneldaten.class, iId);
		if (paneldaten == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePaneldatenDto(paneldaten);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PanelsperrenDto panelsperrenFindByPrimaryKey(Integer iId) {
		Panelsperren panelsperren = em.find(Panelsperren.class, iId);
		return PanelsperrenDtoAssembler.createDto(panelsperren);
	}

	public ArrayList<PanelsperrenDto> panelsperrenFindByBelegartCNrMandantCNr(String belegartCNr, String mandantCNr) {

		ArrayList<PanelsperrenDto> al = new ArrayList<PanelsperrenDto>();

		Query query = em.createNamedQuery("PanelsperrenfindByBelegartCNrMandantCNr");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, mandantCNr);
		Collection c = query.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Panelsperren p = (Panelsperren) it.next();
			al.add(PanelsperrenDtoAssembler.createDto(p));
		}

		return al;
	}

	public PaneldatenDto findByPanelCNrPanelbeschreibungIIdCKey(String panelCNr, Integer panelbeschreibungIId,
			String cKey) throws EJBExceptionLP {
		// try {
		PaneldatenDto paneldatenDto = paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(panelCNr,
				panelbeschreibungIId, cKey);
		if (paneldatenDto == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return paneldatenDto;

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public String pruefUndImportierePaneldaten(String panelCNr, byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {

		MandantDto mandantDto = null;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e2) {
			throwEJBExceptionLPRespectOld(e2);
		}

		fehlerZeileXLSImport = "";

		byte[] CRLFAscii = { 13, 10 };
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);

			if (panelCNr.equals(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {

				// Artikelnummer muss immer vorhanden sein
				String XLS_IMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";

				HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

				if (sheet.getRows() > 1) {
					Cell[] sZeile = sheet.getRow(0);

					for (int i = 0; i < sZeile.length; i++) {

						if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {

							String druckname = sZeile[i].getContents().trim();

							if (!druckname.equals(XLS_IMPORT_SPALTE_ARTIKELNUMMER)) {
								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto == null) {
									fehlerZeileXLSImport += "Der Druckname '" + druckname
											+ "' konnte nicht gefunden werden. " + new String(CRLFAscii);
									return fehlerZeileXLSImport;
								}
							}

							hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
						}

					}

				}

				if (!hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_ARTIKELNUMMER)) {
					return "Es muss zumindest die Spalte 'Artikelnummer' vorhanden sein" + new String(CRLFAscii);
				}

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					String artikelnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_ARTIKELNUMMER,
							ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER, i);

					if (artikelnummer == null || artikelnummer.length() == 0) {
						fehlerZeileXLSImport += "Die Artikelnummer darf nicht leer sein. Zeile " + i
								+ new String(CRLFAscii);
						continue;
					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);

					if (artikelDto != null) {

					} else {
						fehlerZeileXLSImport += "Die Artikelnummer '" + artikelnummer
								+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;
					}

					Iterator itSpalten = hmVorhandeneSpalten.keySet().iterator();
					while (itSpalten.hasNext()) {

						String druckname = (String) itSpalten.next();

						if (!druckname.equals(XLS_IMPORT_SPALTE_ARTIKELNUMMER)) {
							Integer iSpalte = hmVorhandeneSpalten.get(druckname);

							if (sZeile.length >= iSpalte) {

								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto != null) {

									String wert = getStringAusXLS(sZeile, hmVorhandeneSpalten, druckname, 3000, i);

									if (wert != null && wert.length() > 0) {

										if (bImportierenWennKeinFehler == true) {

											try {
												Query query = em.createNamedQuery(
														"PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
												query.setParameter(1, panelCNr);
												query.setParameter(2, panelbeschreibungDto.getIId());
												query.setParameter(3, artikelDto.getIId() + "");
												Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();
											} catch (NoResultException e) {
												PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
												Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELDATEN);
												Paneldaten paneldaten = new Paneldaten(pk, panelCNr,
														panelbeschreibungDto.getIId(), artikelDto.getIId() + "",
														"java.lang.Integer");
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();

											}

										}
									}

								}
							}

						}

					}

				}

			} else if (panelCNr.equals(PanelFac.PANEL_PROJEKTEIGENSCHAFTEN)) {

				// Projektnummer muss immer vorhanden sein
				String XLS_IMPORT_SPALTE_BEREICH = "Bereich";
				String XLS_IMPORT_SPALTE_PROJEKTNUMMER = "Projektnummer";

				HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

				if (sheet.getRows() > 1) {
					Cell[] sZeile = sheet.getRow(0);

					for (int i = 0; i < sZeile.length; i++) {

						if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {

							String druckname = sZeile[i].getContents().trim();

							if (druckname.equals(XLS_IMPORT_SPALTE_BEREICH)
									|| druckname.equals(XLS_IMPORT_SPALTE_PROJEKTNUMMER)) {

							} else {

								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto == null) {
									fehlerZeileXLSImport += "Der Druckname '" + druckname
											+ "' konnte nicht gefunden werden. " + new String(CRLFAscii);
									return fehlerZeileXLSImport;
								}

							}

							hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
						}

					}

				}

				if (!(hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BEREICH)
						&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_PROJEKTNUMMER))) {
					return "Es muss zumindest die Spalte 'Bereich' und die Spalte 'Projektnummer' vorhanden sein"
							+ new String(CRLFAscii);
				}

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					String bereich = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BEREICH, 80, i);

					String projektnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_PROJEKTNUMMER,
							15, i);

					if (bereich == null || bereich.length() == 0 || projektnummer == null
							|| projektnummer.length() == 0) {
						fehlerZeileXLSImport += "Die Spalte Bereich bzw. die Spalte Projektnummer darf nicht leer sein. Zeile "
								+ i + new String(CRLFAscii);
						continue;
					}

					ProjektDto[] projektDtos = getProjektFac().projektFindByCNrMandantCNr(projektnummer,
							theClientDto.getMandant());

					ProjektDto projektDto = null;

					for (int k = 0; k < projektDtos.length; k++) {

						Integer bereichIId = projektDtos[k].getBereichIId();
						BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(bereichIId);

						if (bereichDto.getCBez().equals(bereich)) {
							projektDto = projektDtos[k];
							break;
						}

					}

					if (projektDto != null) {

					} else {
						fehlerZeileXLSImport += "Das Projekt '" + projektnummer + "' im Bereich '" + bereich
								+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;
					}

					Iterator itSpalten = hmVorhandeneSpalten.keySet().iterator();
					while (itSpalten.hasNext()) {

						String druckname = (String) itSpalten.next();

						if (!(druckname.equals(XLS_IMPORT_SPALTE_BEREICH)
								|| druckname.equals(XLS_IMPORT_SPALTE_PROJEKTNUMMER))) {
							Integer iSpalte = hmVorhandeneSpalten.get(druckname);

							if (sZeile.length >= iSpalte) {

								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto != null) {

									String wert = getStringAusXLS(sZeile, hmVorhandeneSpalten, druckname, 3000, i);

									if (wert != null && wert.length() > 0) {

										if (bImportierenWennKeinFehler == true) {

											try {
												Query query = em.createNamedQuery(
														"PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
												query.setParameter(1, panelCNr);
												query.setParameter(2, panelbeschreibungDto.getIId());
												query.setParameter(3, projektDto.getIId() + "");
												Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();
											} catch (NoResultException e) {
												PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
												Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELDATEN);
												Paneldaten paneldaten = new Paneldaten(pk, panelCNr,
														panelbeschreibungDto.getIId(), projektDto.getIId() + "",
														"java.lang.Integer");
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();

											}

										}
									}

								}
							}

						}

					}

				}

			} else if (panelCNr.equals(PanelFac.PANEL_PROJEKTHISTORY)) {

				// Projektnummer muss immer vorhanden sein
				String XLS_IMPORT_SPALTE_BEREICH = "Bereich";
				String XLS_IMPORT_SPALTE_PROJEKTNUMMER = "Projektnummer";
				String XLS_IMPORT_SPALTE_TITEL = "Titel";
				String XLS_IMPORT_SPALTE_TEXTINHALT = "Textinhalt";
				String XLS_IMPORT_SPALTE_HISTORYART = "Historyart";

				HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

				if (sheet.getRows() > 1) {
					Cell[] sZeile = sheet.getRow(0);

					for (int i = 0; i < sZeile.length; i++) {

						if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {

							String druckname = sZeile[i].getContents().trim();

							if (druckname.equals(XLS_IMPORT_SPALTE_BEREICH)
									|| druckname.equals(XLS_IMPORT_SPALTE_PROJEKTNUMMER)
									|| druckname.equals(XLS_IMPORT_SPALTE_TITEL)
									|| druckname.equals(XLS_IMPORT_SPALTE_HISTORYART)
									|| druckname.equals(XLS_IMPORT_SPALTE_TEXTINHALT)) {

							} else {

								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto == null) {
									fehlerZeileXLSImport += "Der Druckname '" + druckname
											+ "' konnte nicht gefunden werden. " + new String(CRLFAscii);
									return fehlerZeileXLSImport;
								}

							}

							hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
						}

					}

				}

				if (!(hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_BEREICH)
						&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_PROJEKTNUMMER)
						&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_TITEL)
						&& hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_TEXTINHALT))) {
					return "Es muss zumindest die Spalte 'Bereich','Projektnummer','Textinhalt' und die Spalte 'Titel' vorhanden sein"
							+ new String(CRLFAscii);
				}

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] sZeile = sheet.getRow(i);

					String bereich = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_BEREICH, 80, i);

					String projektnummer = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_PROJEKTNUMMER,
							15, i);

					String titel = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_TITEL, 120, i);

					String textinhalt = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_TEXTINHALT, 3000,
							i);

					if (bereich == null || bereich.length() == 0 || projektnummer == null || projektnummer.length() == 0
							|| textinhalt == null || textinhalt.length() == 0) {
						fehlerZeileXLSImport += "Die Spalte Bereich/Projektnummer/Textinhalt darf nicht leer sein. Zeile "
								+ i + new String(CRLFAscii);
						continue;
					}

					ProjektDto[] projektDtos = getProjektFac().projektFindByCNrMandantCNr(projektnummer,
							theClientDto.getMandant());

					ProjektDto projektDto = null;

					for (int k = 0; k < projektDtos.length; k++) {

						Integer bereichIId = projektDtos[k].getBereichIId();
						BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(bereichIId);

						if (bereichDto.getCBez().equals(bereich)) {
							projektDto = projektDtos[k];
							break;
						}

					}

					if (projektDto != null) {

					} else {
						fehlerZeileXLSImport += "Das Projekt '" + projektnummer + "' im Bereich '" + bereich
								+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;
					}

					// Historyart
					Integer historyartIId = null;
					if (hmVorhandeneSpalten.containsKey(XLS_IMPORT_SPALTE_HISTORYART)) {
						String historyart = getStringAusXLS(sZeile, hmVorhandeneSpalten, XLS_IMPORT_SPALTE_HISTORYART, 120,
								i);

						if (historyart != null && historyart.length() > 0) {
							try {
								Query query = em.createNamedQuery("HistoryartFindByCBez");
								query.setParameter(1, historyart);
								// @todo getSingleResult oder getResultList ?
								Historyart doppelt = (Historyart) query.getSingleResult();
								historyartIId = doppelt.getIId();
							} catch (NoResultException ex1) {
								fehlerZeileXLSImport += "Historyart '" + historyart
										+ "' konnte nicht gefunden werden. Zeile " + i + new String(CRLFAscii);
								continue;
							}
						}
					}

					// History-Eintrag erzeugen

					HistoryDto historyDto = new HistoryDto();
					historyDto.setProjektIId(projektDto.getIId());
					historyDto.setCTitel(titel);
					historyDto.setXText(textinhalt);
					historyDto.setHistoryartIId(historyartIId);

					Integer historyIId=getProjektFac().createHistory(historyDto, theClientDto);

					Iterator itSpalten = hmVorhandeneSpalten.keySet().iterator();
					while (itSpalten.hasNext()) {

						String druckname = (String) itSpalten.next();

						if (!(druckname.equals(XLS_IMPORT_SPALTE_BEREICH)
								|| druckname.equals(XLS_IMPORT_SPALTE_PROJEKTNUMMER))) {
							Integer iSpalte = hmVorhandeneSpalten.get(druckname);

							if (sZeile.length >= iSpalte) {

								PanelbeschreibungDto panelbeschreibungDto = getPanelFac()
										.panelbeschreibungFindByPanelCNrMandantCNrCDrucknameOhneExc(panelCNr,
												theClientDto.getMandant(), druckname);

								if (panelbeschreibungDto != null) {

									String wert = getStringAusXLS(sZeile, hmVorhandeneSpalten, druckname, 3000, i);

									if (wert != null && wert.length() > 0) {

										if (bImportierenWennKeinFehler == true) {

											try {
												Query query = em.createNamedQuery(
														"PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
												query.setParameter(1, panelCNr);
												query.setParameter(2, panelbeschreibungDto.getIId());
												query.setParameter(3, historyIId + "");
												Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();
											} catch (NoResultException e) {
												PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
												Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELDATEN);
												Paneldaten paneldaten = new Paneldaten(pk, panelCNr,
														panelbeschreibungDto.getIId(), historyIId + "",
														"java.lang.Integer");
												paneldaten.setXInhalt(wert);
												em.persist(paneldaten);
												em.flush();

											}

										}
									}

								}
							}

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return fehlerZeileXLSImport;
	}

	private String getStringAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iLaenge, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						fehlerZeileXLSImport += feldname + " ist zu lang (>" + iLaenge + ") Zeile " + iZeile
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

	public PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr, String cKey) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("PaneldatenfindByPanelCNrCKey");
		query.setParameter(1, panelCNr);
		query.setParameter(2, cKey);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assemblePaneldatenDtos(cl);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPaneldatenFromPaneldatenDto(Paneldaten paneldaten, PaneldatenDto paneldatenDto) {
		paneldaten.setPanelCNr(paneldatenDto.getPanelCNr());
		paneldaten.setPanelbeschreibungIId(paneldatenDto.getPanelbeschreibungIId());
		paneldaten.setCKey(paneldatenDto.getCKey());
		paneldaten.setCDatentypkey(paneldatenDto.getCDatentypkey());
		paneldaten.setOInhalt(paneldatenDto.getOInhalt());
		paneldaten.setXInhalt(paneldatenDto.getXInhalt());
		em.merge(paneldaten);
		em.flush();
	}

	private PaneldatenDto assemblePaneldatenDto(Paneldaten paneldaten) {
		return PaneldatenDtoAssembler.createDto(paneldaten);
	}

	private PaneldatenDto[] assemblePaneldatenDtos(Collection<?> paneldatens) {
		List<PaneldatenDto> list = new ArrayList<PaneldatenDto>();
		if (paneldatens != null) {
			Iterator<?> iterator = paneldatens.iterator();
			while (iterator.hasNext()) {
				Paneldaten paneldaten = (Paneldaten) iterator.next();
				list.add(assemblePaneldatenDto(paneldaten));
			}
		}
		PaneldatenDto[] returnArray = new PaneldatenDto[list.size()];
		return (PaneldatenDto[]) list.toArray(returnArray);
	}

	public void createPanel(PanelDto panelDto) throws EJBExceptionLP {
		if (panelDto == null) {
			return;
		}
		try {
			Panel panel = new Panel(panelDto.getCNr());
			em.persist(panel);
			em.flush();
			setPanelFromPanelDto(panel, panelDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createPanelsperren(PanelsperrenDto panelsperrenDto) {
		try {

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELSPERREN);
			panelsperrenDto.setIId(pk);

			Panelsperren panelsperren = new Panelsperren(panelsperrenDto.getIId(), panelsperrenDto.getBelegartCNr(),
					panelsperrenDto.getMandantCNr(), panelsperrenDto.getCRessourceUnten(),
					panelsperrenDto.getCRessourceOben());
			em.persist(panelsperren);
			em.flush();
			setPanelsperrenFromPanelsperrenDto(panelsperren, panelsperrenDto);

			return pk;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePanel(String cNr) throws EJBExceptionLP {
		Panel toRemove = em.find(Panel.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePanel(PanelDto panelDto) throws EJBExceptionLP {
		if (panelDto != null) {
			String cNr = panelDto.getCNr();
			removePanel(cNr);
		}
	}

	public void updatePanel(PanelDto panelDto) throws EJBExceptionLP {
		if (panelDto != null) {
			String cNr = panelDto.getCNr();
			// try {
			Panel panel = em.find(Panel.class, cNr);
			if (panel == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPanelFromPanelDto(panel, panelDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public void updatePanelsperren(PanelsperrenDto panelsperrenDto) {
		if (panelsperrenDto != null) {
			Panelsperren panelsperren = em.find(Panelsperren.class, panelsperrenDto.getIId());
			setPanelsperrenFromPanelsperrenDto(panelsperren, panelsperrenDto);
		}
	}

	public PanelDto panelFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		Panel panel = em.find(Panel.class, cNr);
		if (panel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePanelDto(panel);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setPanelFromPanelDto(Panel panel, PanelDto panelDto) {
		panel.setCBez(panelDto.getCBez());
		em.merge(panel);
		em.flush();
	}

	private void setPanelsperrenFromPanelsperrenDto(Panelsperren panelsperren, PanelsperrenDto panelsperrenDto) {
		panelsperren.setBelegartCNr(panelsperrenDto.getBelegartCNr());
		panelsperren.setMandantCNr(panelsperrenDto.getMandantCNr());
		panelsperren.setCRessourceUnten(panelsperrenDto.getCRessourceUnten());
		panelsperren.setCRessourceOben(panelsperrenDto.getCRessourceOben());
		em.merge(panelsperren);
		em.flush();
	}

	private PanelDto assemblePanelDto(Panel panel) {
		return PanelDtoAssembler.createDto(panel);
	}

	private PanelDto[] assemblePanelDtos(Collection<?> panels) {
		List<PanelDto> list = new ArrayList<PanelDto>();
		if (panels != null) {
			Iterator<?> iterator = panels.iterator();
			while (iterator.hasNext()) {
				Panel panel = (Panel) iterator.next();
				list.add(assemblePanelDto(panel));
			}
		}
		PanelDto[] returnArray = new PanelDto[list.size()];
		return (PanelDto[]) list.toArray(returnArray);
	}

	@Override
	public PaneldatenDto paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(String panelCNr,
			Integer panelbeschreibungIId, String cKey) {
		Paneldaten paneldaten = PaneldatenQuery.resultByPanelCNrPanelbeschreibungIIdCKeyNoEx(em, panelCNr,
				panelbeschreibungIId, cKey);
		return paneldaten != null ? assemblePaneldatenDto(paneldaten) : null;
	}

	@Override
	public PaneldatenDto paneldatenFindByPrimaryKeyOhneExc(Integer paneldatenIId) {
		Paneldaten paneldaten = em.find(Paneldaten.class, paneldatenIId);
		return paneldaten != null ? assemblePaneldatenDto(paneldaten) : null;
	}

	@Override
	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKeyOhneExc(Integer panelbeschreibungIId) {
		Panelbeschreibung panelbeschreibung = em.find(Panelbeschreibung.class, panelbeschreibungIId);
		return panelbeschreibung != null ? assemblePanelbeschreibungDto(panelbeschreibung) : null;
	}

	@Override
	public CreatePaneldatenResult createPaneldaten(PaneldatenDto paneldatenDto) {
		Validator.dtoNotNull(paneldatenDto, "paneldatenDto");
		Validator.notNull(paneldatenDto.getPanelCNr(), "paneldatenDto.panelCNr");
		Validator.notNull(paneldatenDto.getPanelbeschreibungIId(), "paneldatenDto.panelbeschreibungIId");
		Validator.notNull(paneldatenDto.getCKey(), "paneldatenDto.cKey");
		Validator.notNull(paneldatenDto.getCDatentypkey(), "paneldatenDto.cDatentypkey");

		try {
			Paneldaten paneldaten = PaneldatenQuery.validatePaneldatenExistsIgnoringDefaultsExc(em,
					paneldatenDto.getPanelCNr(), paneldatenDto.getPanelbeschreibungIId(), paneldatenDto.getCKey());

			if (paneldaten == null) {
				PKGeneratorObj pkGen = new PKGeneratorObj();
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELDATEN);
				paneldatenDto.setIId(pk);
				paneldaten = new Paneldaten(paneldatenDto.getIId(), paneldatenDto.getPanelCNr(),
						paneldatenDto.getPanelbeschreibungIId(), paneldatenDto.getCKey(),
						paneldatenDto.getCDatentypkey());
			}

			setPaneldatenFromPaneldatenDto(paneldaten, paneldatenDto);
			return new CreatePaneldatenResult(paneldaten.getIId());
		} catch (EJBExceptionLP exc) {
			CreatePaneldatenResult createResult = new CreatePaneldatenResult();
			createResult.getEjbExceptions().add(exc);
			return createResult;
		}
	}

	@Override
	public PaneldatenDto setupDefaultPaneldaten(String panelCnr, String cKey, PanelbeschreibungDto panelbeschreibung) {
		PaneldatenDto paneldaten = new PaneldatenDto();
		paneldaten.setPanelCNr(panelCnr);
		paneldaten.setCKey(cKey);
		paneldaten.setCDatentypkey("java.lang.String");
		if (PanelFac.TYP_WRAPPERCHECKBOX.equals(panelbeschreibung.getCTyp())) {
			paneldaten.setCDatentypkey("java.lang.Short");
		}
		paneldaten.setPanelbeschreibungIId(panelbeschreibung.getIId());
		paneldaten.setXInhalt(panelbeschreibung.getCDefault());

		return paneldaten;
	}

}
