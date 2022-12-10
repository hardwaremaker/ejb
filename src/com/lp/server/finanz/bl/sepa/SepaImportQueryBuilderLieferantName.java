package com.lp.server.finanz.bl.sepa;

import java.util.List;

import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.util.Helper;

/**
 * Sucht nach dem Namen des Zahlungsempfaengers in den Partnerdaten der 
 * Eingangsrechnungskunden
 * 
 * @author andi
 *
 */
public class SepaImportQueryBuilderLieferantName extends
		SepaImportQueryBuilderEr {

	public SepaImportQueryBuilderLieferantName(String mandantCNr,
			List<String> stati) {
		super(mandantCNr, stati);
	}

	@Override
	protected void buildFromQuery() {
		froms.add(SELECT_EINGANGSRECHNUNG_I_ID + FROM_EINGANGSRECHNUNG);
		froms.add(JOIN_LIEFERANT_PARTNER);
	}

	@Override
	protected void buildWhereCriterion() {
		addWhere("(lower(" + Partner + ".c_name1nachnamefirmazeile1) LIKE lower(?1) OR "
				+ "(lower(" + Partner + ".c_kbez) LIKE lower(?1)))", getCritValueWithProzent());
	}

	@Override
	protected boolean setValueForCriterionImpl(SepaZahlung payment, SepaBuchung booking) {
		if (payment.getBeteiligter() == null) 
			return false;
		
		String name = payment.getBeteiligter().getName();
		if (Helper.isStringEmpty(name))
			return false;
		
		setCritValue(name.trim());
		return true;
	}

	@Override
	public boolean isTotalMatch() {
		return true;
	}

}
