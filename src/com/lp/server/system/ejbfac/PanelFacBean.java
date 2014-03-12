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
package com.lp.server.system.ejbfac;

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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lp.server.system.ejb.Panel;
import com.lp.server.system.ejb.Panelbeschreibung;
import com.lp.server.system.ejb.Paneldaten;
import com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.PanelDto;
import com.lp.server.system.service.PanelDtoAssembler;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PanelbeschreibungDtoAssembler;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.PaneldatenDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Stateless
public class PanelFacBean extends LPReport implements PanelFac, JRDataSource {
	@PersistenceContext
	private EntityManager em;

	private int index = -1; // bei jedem Druck zuruecksetzen!!!
	private Object[][] data = null;

	private String[] felder = new String[0];

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

	public Integer createPanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto,
			boolean bFuegeNeuePositionVorDerSelektiertenEin)
			throws EJBExceptionLP {
		if (panelbeschreibungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("paneldatenDto == null"));
		}
		if (panelbeschreibungDto.getCName() == null
				|| panelbeschreibungDto.getCTyp() == null
				|| panelbeschreibungDto.getPanelCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"panelbeschreibungDto.getCName() == null || panelbeschreibungDto.getCTyp() == null || panelbeschreibungDto.getPanelCNr() == null"));
		}

		if (bFuegeNeuePositionVorDerSelektiertenEin) {
			Query query = em
					.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrIGridy");
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
			Query query = em
					.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCName");
			query.setParameter(1, panelbeschreibungDto.getPanelCNr());
			query.setParameter(2, panelbeschreibungDto.getMandantCNr());
			query.setParameter(3, panelbeschreibungDto.getCName());
			Panelbeschreibung panelbeschreibung = (Panelbeschreibung) query
					.getSingleResult();
			if (panelbeschreibung != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_PANELBESCHREIBUNG.UK"));
			}
		} catch (NoResultException ex) {
			//
		}
		try {

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PANELBESCHREIBUNG);
			panelbeschreibungDto.setIId(pk);

			Panelbeschreibung panelbeschreibung = new Panelbeschreibung(
					panelbeschreibungDto.getIId(),
					panelbeschreibungDto.getPanelCNr(),
					panelbeschreibungDto.getMandantCNr(),
					panelbeschreibungDto.getCName(),
					panelbeschreibungDto.getCTyp(),
					panelbeschreibungDto.getIGridx(),
					panelbeschreibungDto.getIGridy(),
					panelbeschreibungDto.getIGridwidth(),
					panelbeschreibungDto.getIGridheigth(),
					panelbeschreibungDto.getCFill(),
					panelbeschreibungDto.getCAnchor(),
					panelbeschreibungDto.getIInsetsleft(),
					panelbeschreibungDto.getIInsetsright(),
					panelbeschreibungDto.getIInsetstop(),
					panelbeschreibungDto.getIInsetsbottom(),
					panelbeschreibungDto.getIIpadx(),
					panelbeschreibungDto.getIIpady(),
					panelbeschreibungDto.getBMandatory(),
					panelbeschreibungDto.getFWeighty(),
					panelbeschreibungDto.getFWeightx(),
					panelbeschreibungDto.getCDruckname(),
					panelbeschreibungDto.getArtgruIId());
			em.persist(panelbeschreibung);
			em.flush();
			setPanelbeschreibungFromPanelbeschreibungDto(panelbeschreibung,
					panelbeschreibungDto);
			return panelbeschreibungDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removePanelbeschreibung(Integer iId) throws EJBExceptionLP {
		Panelbeschreibung toRemove = em.find(Panelbeschreibung.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto) throws EJBExceptionLP {
		if (panelbeschreibungDto != null) {
			Integer iId = panelbeschreibungDto.getIId();
			removePanelbeschreibung(iId);
		}
	}

	public void updatePanelbeschreibung(
			PanelbeschreibungDto panelbeschreibungDto) throws EJBExceptionLP {
		if (panelbeschreibungDto != null) {
			Integer iId = panelbeschreibungDto.getIId();
			// try {
			Panelbeschreibung panelbeschreibung = em.find(
					Panelbeschreibung.class, iId);
			if (panelbeschreibung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			try {
				Query query = em
						.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNrCName");
				query.setParameter(1, panelbeschreibungDto.getPanelCNr());
				query.setParameter(2, panelbeschreibungDto.getMandantCNr());
				query.setParameter(3, panelbeschreibungDto.getCName());
				Panelbeschreibung duplicateHelper = (Panelbeschreibung) query
						.getSingleResult();
				Integer iIdVorhanden = duplicateHelper.getIId();
				if (iId.equals(iIdVorhanden) == false) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("LP_PANELBESCHREIBUNG.UK"));
				}

			} catch (NoResultException ex) {
				//
			}

			setPanelbeschreibungFromPanelbeschreibungDto(panelbeschreibung,
					panelbeschreibungDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public PanelbeschreibungDto panelbeschreibungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Panelbeschreibung panelbeschreibung = em.find(Panelbeschreibung.class,
				iId);
		if (panelbeschreibung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePanelbeschreibungDto(panelbeschreibung);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNr(
			String panelCNr, String mandantCNr, Integer artgruIId)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator
						.next();

				if (panelbeschreibung.getCTyp().startsWith("WrapperComboBox")) {
					int u = 0;
				}

				if (artgruIId != null
						&& panelbeschreibung.getArtgruIId() != null
						&& !artgruIId.equals(panelbeschreibung.getArtgruIId())) {
					continue;
				}

				if (artgruIId == null
						&& panelbeschreibung.getArtgruIId() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list
				.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}
	public PanelbeschreibungDto[] panelbeschreibungFindByPanelCNrMandantCNrPartnerklasseIId(
			String panelCNr, String mandantCNr, Integer partnerklasseIId) {
		// try {
		Query query = em
				.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (cl != null) {
			Iterator<?> iterator = cl.iterator();
			while (iterator.hasNext()) {

				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator
						.next();

				if (panelbeschreibung.getCTyp().startsWith("WrapperComboBox")) {
					int u = 0;
				}

				if (partnerklasseIId != null
						&& panelbeschreibung.getPartnerklasseIId() != null
						&& !partnerklasseIId.equals(panelbeschreibung.getPartnerklasseIId())) {
					continue;
				}

				if (partnerklasseIId == null
						&& panelbeschreibung.getPartnerklasseIId() != null) {
					continue;
				}

				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list
				.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public boolean panelbeschreibungVorhanden(String panelCNr, String mandantCNr)
			throws EJBExceptionLP {

		Query query = em
				.createNamedQuery("PanelbeschreibungfindByPanelCNrMandantCNr");
		query.setParameter(1, panelCNr);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		if (cl.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public JasperPrintLP printPanel(String panelCNr, String report,
			String cKey, TheClientDto theClientDto) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		index = -1;

		Session session = FLRSessionFactory.getFactory().openSession();

		String[] typen = new String[3];
		typen[0] = PanelFac.TYP_WRAPPERCHECKBOX;
		typen[1] = PanelFac.TYP_WRAPPEREDITOR;
		typen[2] = PanelFac.TYP_WRAPPERTEXTFIELD;

		org.hibernate.Criteria crit = session
				.createCriteria(FLRPanelbeschreibung.class);
		crit.add(Restrictions.in(PanelFac.FLR_PANELBESCHREIBUNG_C_TYP, typen));
		crit.add(Restrictions.eq(PanelFac.FLR_PANELBESCHREIBUNG_PANEL_C_NR,
				panelCNr));
		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		felder = new String[results.size()];
		data = new Object[1][results.size()];

		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRPanelbeschreibung flrPanelbeschreibung = (FLRPanelbeschreibung) resultListIterator
					.next();
			felder[row] = flrPanelbeschreibung.getC_name();

			// try {
			Query query = em
					.createNamedQuery("PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
			query.setParameter(1, panelCNr);
			query.setParameter(2, flrPanelbeschreibung.getI_id());
			query.setParameter(3, cKey);
			Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
			if (paneldaten == null) {
				data[0][row] = null;
			} else {
				data[0][row] = new String(paneldaten.getOInhalt());
			}

			// }
			// catch (FinderException ex) {
			// data[0][row] = null;
			// }
			row++;
		}
		session.close();

		initJRDS(parameter, PanelFac.REPORT_MODUL, report,
				theClientDto.getMandant(), theClientDto.getLocUi(),
				theClientDto);

		return getReportPrint();
	}

	private void setPanelbeschreibungFromPanelbeschreibungDto(
			Panelbeschreibung panelbeschreibung,
			PanelbeschreibungDto panelbeschreibungDto) {
		panelbeschreibung.setPanelCNr(panelbeschreibungDto.getPanelCNr());
		panelbeschreibung.setCName(panelbeschreibungDto.getCName());
		panelbeschreibung.setCTyp(panelbeschreibungDto.getCTyp());
		panelbeschreibung.setCTokeninresourcebundle(panelbeschreibungDto
				.getCTokeninresourcebundle());
		panelbeschreibung.setIGridx(panelbeschreibungDto.getIGridx());
		panelbeschreibung.setIGridy(panelbeschreibungDto.getIGridy());
		panelbeschreibung.setIGridwidth(panelbeschreibungDto.getIGridwidth());
		panelbeschreibung.setIGridheigth(panelbeschreibungDto.getIGridheigth());
		panelbeschreibung.setCFill(panelbeschreibungDto.getCFill());
		panelbeschreibung.setCAnchor(panelbeschreibungDto.getCAnchor());
		panelbeschreibung.setIInsetsleft(panelbeschreibungDto.getIInsetsleft());
		panelbeschreibung.setIInsetsright(panelbeschreibungDto
				.getIInsetsright());
		panelbeschreibung.setIInsetstop(panelbeschreibungDto.getIInsetstop());
		panelbeschreibung.setIInsetsbottom(panelbeschreibungDto
				.getIInsetsbottom());
		panelbeschreibung.setIIpadx(panelbeschreibungDto.getIIpadx());
		panelbeschreibung.setIIpady(panelbeschreibungDto.getIIpady());
		panelbeschreibung.setFWeightx(panelbeschreibungDto.getFWeightx());
		panelbeschreibung.setFWeighty(panelbeschreibungDto.getFWeighty());
		panelbeschreibung.setBMandatory(panelbeschreibungDto.getBMandatory());
		panelbeschreibung.setMandantCNr(panelbeschreibungDto.getMandantCNr());
		panelbeschreibung.setArtgruIId(panelbeschreibungDto.getArtgruIId());
		panelbeschreibung.setCDruckname(panelbeschreibungDto.getCDruckname());
		panelbeschreibung.setPartnerklasseIId(panelbeschreibungDto.getPartnerklasseIId());
		em.merge(panelbeschreibung);
		em.flush();
	}

	private PanelbeschreibungDto assemblePanelbeschreibungDto(
			Panelbeschreibung panelbeschreibung) {
		return PanelbeschreibungDtoAssembler.createDto(panelbeschreibung);
	}

	private PanelbeschreibungDto[] assemblePanelbeschreibungDtos(
			Collection<?> panelbeschreibungs) {
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (panelbeschreibungs != null) {
			Iterator<?> iterator = panelbeschreibungs.iterator();
			while (iterator.hasNext()) {
				Panelbeschreibung panelbeschreibung = (Panelbeschreibung) iterator
						.next();
				list.add(assemblePanelbeschreibungDto(panelbeschreibung));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list
				.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}

	public void createPaneldaten(PaneldatenDto[] paneldatenDto)
			throws EJBExceptionLP {
		if (paneldatenDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("paneldatenDto == null"));
		}

		// Zuerst alle loeschen
		if (paneldatenDto.length > 0) {
			Query query = em.createNamedQuery("PaneldatenfindByPanelCNrCKey");
			query.setParameter(1, paneldatenDto[0].getPanelCNr());
			query.setParameter(2, paneldatenDto[0].getCKey());
			Collection<?> cl = query.getResultList();
			Iterator it = cl.iterator();

			while (it.hasNext()) {
				Paneldaten p = (Paneldaten) it.next();
				em.remove(p);
				em.flush();
			}
		}

		for (int i = 0; i < paneldatenDto.length; i++) {
			if (paneldatenDto[i].getPanelbeschreibungIId() != null) {

				try {
					Paneldaten paneldaten;
					try {
						Query query = em
								.createNamedQuery("PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
						query.setParameter(1, paneldatenDto[i].getPanelCNr());
						query.setParameter(2,
								paneldatenDto[i].getPanelbeschreibungIId());
						query.setParameter(3, paneldatenDto[i].getCKey());
						paneldaten = (Paneldaten) query.getSingleResult();
						setPaneldatenFromPaneldatenDto(paneldaten,
								paneldatenDto[i]);
					} catch (NoResultException e) {
						PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
						Integer pk = pkGen
								.getNextPrimaryKey(PKConst.PK_PANELDATEN);
						paneldatenDto[i].setIId(pk);
						paneldaten = new Paneldaten(paneldatenDto[i].getIId(),
								paneldatenDto[i].getPanelCNr(),
								paneldatenDto[i].getPanelbeschreibungIId(),
								paneldatenDto[i].getCKey(),
								paneldatenDto[i].getCDatentypkey());
						em.persist(paneldaten);
						em.flush();
						setPaneldatenFromPaneldatenDto(paneldaten,
								paneldatenDto[i]);
					} catch (NonUniqueResultException ex1) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
					}

				} catch (EntityExistsException e) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
				}
			}
		}
	}

	public void removePaneldaten(Integer iId) throws EJBExceptionLP {
		Paneldaten toRemove = em.find(Paneldaten.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removePaneldaten(PaneldatenDto paneldatenDto)
			throws EJBExceptionLP {
		if (paneldatenDto != null) {
			Integer iId = paneldatenDto.getIId();
			removePaneldaten(iId);
		}
	}

	public void updatePaneldaten(PaneldatenDto paneldatenDto)
			throws EJBExceptionLP {
		if (paneldatenDto != null) {
			Integer iId = paneldatenDto.getIId();
			// try {
			Paneldaten paneldaten = em.find(Paneldaten.class, iId);
			if (paneldaten == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPaneldatenFromPaneldatenDto(paneldaten, paneldatenDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public PaneldatenDto paneldatenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Paneldaten paneldaten = em.find(Paneldaten.class, iId);
		if (paneldaten == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePaneldatenDto(paneldaten);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PaneldatenDto findByPanelCNrPanelbeschreibungIIdCKey(
			String panelCNr, Integer panelbeschreibungIId, String cKey)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey");
		query.setParameter(1, panelCNr);
		query.setParameter(2, panelbeschreibungIId);
		query.setParameter(3, cKey);
		Paneldaten paneldaten = (Paneldaten) query.getSingleResult();
		if (paneldaten == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePaneldatenDto(paneldaten);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public PaneldatenDto[] paneldatenFindByPanelCNrCKey(String panelCNr,
			String cKey) throws EJBExceptionLP {
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

	private void setPaneldatenFromPaneldatenDto(Paneldaten paneldaten,
			PaneldatenDto paneldatenDto) {
		paneldaten.setPanelCNr(paneldatenDto.getPanelCNr());
		paneldaten.setPanelbeschreibungIId(paneldatenDto
				.getPanelbeschreibungIId());
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

	public void removePanel(String cNr) throws EJBExceptionLP {
		Panel toRemove = em.find(Panel.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setPanelFromPanelDto(panel, panelDto);
			// }
			// catch (FinderException e) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
			// }
		}
	}

	public PanelDto panelFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		Panel panel = em.find(Panel.class, cNr);
		if (panel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
}
