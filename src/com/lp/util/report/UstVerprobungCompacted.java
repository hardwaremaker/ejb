package com.lp.util.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UstVerprobungCompacted {

	private UstVerprobungRow compacted;
	private List<UstVerprobungRow> details;
	
	public UstVerprobungCompacted(UstVerprobungRow row) {
		initialize(row);
		details = new ArrayList<UstVerprobungRow>();
		addDetail(row);
	}
	
	private void initialize(UstVerprobungRow row) {
		compacted = new UstVerprobungRow();
		compacted.setBetrag(BigDecimal.ZERO);
		compacted.setBuchungDetailId(row.getBuchungDetailId());
		compacted.setBuchungId(row.getBuchungId());
		compacted.setDatum(row.getDatum());
		compacted.setGegenKontoDto(row.getGegenKontoDto());
		compacted.setKontoDto(row.getKontoDto());
		compacted.setMwstSatzBezCBez(row.getMwstSatzBezCBez());
		compacted.setMwstSatzBezIId(row.getMwstSatzBezIId());
		compacted.setReversechargeartCBez(row.getReversechargeartCBez());
		compacted.setReversechargeartCnr(row.getReversechargeartCnr());
		compacted.setSteuer(BigDecimal.ZERO);
		compacted.setSteuerart(row.getSteuerart());
		compacted.setSteuerkategorieCBez(row.getSteuerkategorieCBez());
		compacted.setSteuerkategorieISort(row.getSteuerkategorieISort());
		compacted.setSteuersatz(row.getSteuersatz());
		compacted.setUvaartCnr(row.getUvaartCnr());
		compacted.setBelegDatum(row.getBelegDatum());
		compacted.setKontoMwstsatzId(row.getKontoMwstsatzId());
		compacted.setKontoSteuersatz(row.getKontoSteuersatz());
		compacted.setGegenkontoMwstsatzId(row.getGegenkontoMwstsatzId());
		compacted.setGegenkontoSteuersatz(row.getGegenkontoSteuersatz());
	}
	
	public void addDetail(UstVerprobungRow detail) {
		details.add(detail);
		if(detail.getBetrag() != null) {
			compacted.setBetrag(
				compacted.getBetrag().add(detail.getBetrag()));
		}
		compacted.setSteuer(
			compacted.getSteuer().add(detail.getSteuer()));
	}
	
	public UstVerprobungRow getCompacted() {
		return compacted;
	}
	
	public List<UstVerprobungRow> getDetails() {
		return details;
	}
	
	public boolean equalsAusgenommenBetraege(UstVerprobungRow row) {
		if(details.isEmpty()) return false;
		
		return details.get(details.size() - 1).equalsAusgenommenBetraege(row);
	}
}
