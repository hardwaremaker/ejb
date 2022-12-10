package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class StklPosDtoSearchResult  implements Serializable{
	private StuecklistepositionDto stklPosDto;
	private BigDecimal zielmenge;

	public StuecklistepositionDto getStklPosDto() {
		return stklPosDto;
	}

	public void setStklPosDto(StuecklistepositionDto stklPosDto) {
		this.stklPosDto = stklPosDto;
	}

	public BigDecimal getZielmenge() {
		return zielmenge;
	}

	public void setZielmenge(BigDecimal zielmenge) {
		this.zielmenge = zielmenge;
	}

	public boolean hasResult() {
		return getStklPosDto() != null;
	}
}
