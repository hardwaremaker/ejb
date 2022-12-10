package com.lp.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.persistence.Transient;

import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.ReversechargeartDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class LPFibuDetailsSubreport extends LPDatenSubreport {
	private static final long serialVersionUID = -2682590595369949740L;
	private enum Fields {
		KontoCnr("F_KONTO_C_NR"),
		GegenkontoCnr("F_GEGENKONTO_C_NR"),
		KontoCBez("F_KONTO_C_BEZ"),
		GegenkontoCBez("F_GEGENKONTO_C_BEZ"),
		SollBetrag("F_SOLLBETRAG"),
		HabenBetrag("F_HABENBETRAG"),
		SteuerBetrag("F_STEUERBETRAG"),
		MwstSatz("F_MWSTSATZ"),
		OpNummer("F_OPNUMMER"),
		Kommentar("F_KOMMENTAR"),
		ReversechargeartCnr("F_REVERSECHARGEART_C_NR"),
		ExceptionCode("F_EXCEPTION_CODE"),
		ExceptionMsg("F_EXCEPTION_TEXT");
		
		Fields(String value) {
			this.value = value;
		}
		
//		public String getText() { 
//			return value;
//		}
		
		public static Fields from(String text) {
			if (text != null) {
				for (Fields status : Fields.values()) {
					if (text.equalsIgnoreCase(status.value)) {
						return status;
					}
				}
			}
			throw new IllegalArgumentException("No enum value '" + text + "'");
		}

		private String value;
	};
	

	@Transient
	private transient List<EnumMap<Fields, Object>> rows;

	public LPFibuDetailsSubreport() {
		rows = new ArrayList<EnumMap<Fields, Object>>();
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		String fieldname = field.getName().trim();
		return rows.get(index).get(Fields.from(fieldname));
	}
	
	public boolean next() throws JRException {
		if(++index >= rows.size()){
			index = -1;
			return false;
		} else {
			return true;
		}		
	}
	
	public void add(FibuexportDto detail, ReversechargeartDto rcartDto) {
		rows.add(transform(detail, rcartDto));
	}

	public void add(EJBExceptionLP e) {
		EnumMap<Fields, Object> m = 
				new EnumMap<Fields, Object>(Fields.class);
		m.put(Fields.ExceptionCode, new Integer(e.getCode()));
		m.put(Fields.ExceptionMsg, e.getMessage());
		rows.add(m);
	}
	
	private EnumMap<Fields, Object> transform(
			FibuexportDto detail, ReversechargeartDto rcartDto) {
		EnumMap<Fields, Object> m = 
				new EnumMap<Fields, Object>(Fields.class);
		m.put(Fields.KontoCnr, detail.getKontoDto() != null 
				? detail.getKontoDto().getCNr() : null);
		m.put(Fields.KontoCBez, detail.getKontoDto() != null 
				? detail.getKontoDto().getCBez() : null);
		m.put(Fields.GegenkontoCnr, detail.getGegenkontoDto() != null 
				? detail.getGegenkontoDto().getCNr() : null);
		m.put(Fields.GegenkontoCBez, detail.getGegenkontoDto() != null 
				? detail.getGegenkontoDto().getCBez() : null);
		m.put(Fields.SollBetrag, detail.getSollbetragBD());
		m.put(Fields.HabenBetrag, detail.getHabenbetragBD());
		m.put(Fields.SteuerBetrag, detail.getSteuerBD());
		m.put(Fields.MwstSatz, detail.getMwstsatz() != null
				? detail.getMwstsatz().getFMwstsatz() : null);
		m.put(Fields.Kommentar, detail.getCKommentar());
		m.put(Fields.OpNummer, detail.getOPNummer());
		m.put(Fields.ReversechargeartCnr, rcartDto != null
				? rcartDto.getCNr() : null);
		return m;
	}
}
