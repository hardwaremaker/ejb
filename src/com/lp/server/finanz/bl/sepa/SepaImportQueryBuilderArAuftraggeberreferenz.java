package com.lp.server.finanz.bl.sepa;

import java.util.List;

import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaZahlung;

public class SepaImportQueryBuilderArAuftraggeberreferenz extends SepaImportQueryBuilderAr {

	public SepaImportQueryBuilderArAuftraggeberreferenz(String mandantCNr, List<String> stati) {
		super(mandantCNr, stati);
	}

	@Override
	protected void buildFromQuery() {
		froms.add(SELECT_AUSGANGSRECHNUNG_I_ID + FROM_LASTSCHRIFTVORSCHLAG);
		froms.add(JOIN_RECHNUNGREPORT);
	}

	@Override
	protected void buildWhereCriterion() {
//		wheres.add(Lastschriftvorschlag + ".c_auftraggeberreferenz=" + getCritValueWithSingleQuotes());
		addWhere(Lastschriftvorschlag + ".c_auftraggeberreferenz=?1", getCritValue());
	}

	@Override
	protected boolean setValueForCriterionImpl(SepaZahlung payment, SepaBuchung booking) {
		if (payment.getAuftraggeberreferenz() == null) return false;
		
		setCritValue(payment.getAuftraggeberreferenz().trim());
		return true;
	}

	@Override
	public boolean isTotalMatch() {
		return true;
	}

}
