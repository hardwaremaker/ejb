package com.lp.server.util;

import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.bean.MandantBean;
import com.lp.service.BelegVerkaufDto;

public class MwstsatzCache extends HvCreatingCachingProvider<Integer, MwstsatzDto> {
	private final BelegVerkaufDto belegDto;
	private final TheClientDto theClientDto;
	private final MandantBean mandantBean = new MandantBean();
	
	public MwstsatzCache(final BelegVerkaufDto belegDto, final TheClientDto theClientDto) {
		this.belegDto = belegDto;
		this.theClientDto = theClientDto;
	}
	
	@Override
	protected MwstsatzDto provideValue(Integer mwstsatzBezId, Integer transformedKey) {
		return mandantBean.get().mwstsatzZuDatumValidate(
				mwstsatzBezId, belegDto.getTBelegdatum(), theClientDto);
	}
}
