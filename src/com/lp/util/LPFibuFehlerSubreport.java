package com.lp.util;

import java.util.List;

import com.lp.server.finanz.service.FibuFehlerDto;

public class LPFibuFehlerSubreport extends LPDatenSubreportDto<FibuFehlerDto> {
	private static final long serialVersionUID = 4464770285554178361L;

	public LPFibuFehlerSubreport(List<FibuFehlerDto> fibuFehler) {
		super(fibuFehler);
	}
	
	protected Object getEntityValue(FibuFehlerDto dto, String fieldname) {
		if("F_BELEGART_CNR".equals(fieldname)) {
			return dto.getBelegartCNr();
		}
		if("F_BELEG_CNR".equals(fieldname)) {
			return dto.getBelegCNr();
		}
		if("F_BELEG_IID".equals(fieldname)) {
			return dto.getBelegIId();
		}
		if("F_FEHLERCODE".equals(fieldname)) {
			return new Integer(dto.getFehlercode());
		}
		if("F_FEHLERTEXT".equals(fieldname)) {
			return dto.getFehlerText();
		}
		return "Unknown field <" + fieldname + ">";
	}
}
