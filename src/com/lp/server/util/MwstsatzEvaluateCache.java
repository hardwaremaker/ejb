package com.lp.server.util;

import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.bean.MandantBean;
import com.lp.service.BelegVerkaufDto;

public class MwstsatzEvaluateCache extends HvCreatingCachingProvider<Integer, MwstsatzDto> {
	private final BelegVerkaufDto belegDto;
	private final TheClientDto theClientDto;
	private final MandantBean mandantBean = new MandantBean();
	
	public MwstsatzEvaluateCache(final BelegVerkaufDto belegDto, final TheClientDto theClientDto) {
		this.belegDto = belegDto;
		this.theClientDto = theClientDto;
	}
	
	@Override
	protected MwstsatzDto provideValue(Integer key, Integer transformedKey) {
		return mandantBean.get().mwstsatzZuDatumEvaluate(
				new MwstsatzId(key),
				belegDto.getTBelegdatum(), theClientDto);
	}
}
