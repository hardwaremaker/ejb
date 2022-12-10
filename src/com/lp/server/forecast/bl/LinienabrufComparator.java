package com.lp.server.forecast.bl;

import java.util.Comparator;

import com.lp.server.forecast.service.LinienabrufDto;

public class LinienabrufComparator implements Comparator<LinienabrufDto> {

	public LinienabrufComparator() {
	}

	@Override
	public int compare(LinienabrufDto dto1, LinienabrufDto dto2) {
		// PJ19775
		// BereichNr DESC
		// Linie DESC
		// Bestellnummer ASC
		int compare = dto2.getCBereichNr().compareTo(dto1.getCBereichNr());
		if (compare != 0) return compare;
		
		compare = dto2.getCLinie().compareTo(dto1.getCLinie());
		if (compare != 0) return compare;

		return dto1.getCBestellnummer().compareTo(dto2.getCBestellnummer());
	}

}
