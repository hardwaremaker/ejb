package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.ITablenames;
import com.lp.util.Helper;

public class PaneldatenQuery {

	public static final String ByPanelCNrPanelbeschreibungIIdCKey= "PaneldatenfindByPanelCNrPanelbeschreibungIIdCKey";
	
	public static HvTypedQuery<Paneldaten> byPanelCNrPanelbeschreibungIIdCKey(EntityManager em, String panelCnr, Integer panelbeschreibungIId, String cKey) {
		return HvTypedQuery.namedQuery(em, ByPanelCNrPanelbeschreibungIIdCKey, panelCnr, panelbeschreibungIId, cKey);
	}
	
	public static Paneldaten resultByPanelCNrPanelbeschreibungIIdCKeyNoEx(EntityManager em, String panelCnr, Integer panelbeschreibungIId, String cKey) {
		return byPanelCNrPanelbeschreibungIIdCKey(em, panelCnr, panelbeschreibungIId, cKey).getSingleResultNoEx();
	}
	
	public static void validatePaneldatenExistsExc(EntityManager em, String panelCnr, Integer panelbeschreibungIId, String cKey) {
		EntityValidator.validateEntityExists(ITablenames.LP_PANELDATEN, em, ByPanelCNrPanelbeschreibungIIdCKey, panelCnr, panelbeschreibungIId, cKey);
	}
	
	public static Paneldaten validatePaneldatenExistsIgnoringDefaultsExc(EntityManager em, String panelCnr, Integer panelbeschreibungIId, String cKey) {
		HvTypedQuery<Paneldaten> hvQuery = HvTypedQuery.namedQuery(em, ByPanelCNrPanelbeschreibungIIdCKey, panelCnr, panelbeschreibungIId, cKey);
		List<Paneldaten> entities = hvQuery.getResultList();
		
		if (entities.isEmpty())
			return null;
		
		if (entities.size() == 1) {
			Paneldaten paneldaten = entities.get(0);
			Panelbeschreibung beschreibung = em.find(Panelbeschreibung.class, paneldaten.getPanelbeschreibungIId());
			if (isEqualNullAsEmpty(beschreibung.getCDefault(), paneldaten.getXInhalt())) {
				return paneldaten;
			}
			throw EJBExcFactory.duplicateUniqueKey(ITablenames.LP_PANELDATEN, panelCnr, panelbeschreibungIId, cKey);
		}
		
		throw EJBExcFactory.noUniqueResults(ITablenames.LP_PANELDATEN, panelCnr, panelbeschreibungIId, cKey);
	}
	
	private static boolean isEqualNullAsEmpty(String one, String two) {
		if (Helper.isStringEmpty(one) && Helper.isStringEmpty(two))
			return true;
		
		if (one != null && two != null && one.equals(two)) {
			return true;
		}
		
		return false;
	}
}
