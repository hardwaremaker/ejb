package com.lp.server.system.ejbfac;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VerfuegbareHostsDto;

@Local
public interface TheClientFacLocal {

	public VerfuegbareHostsDto getVerfuegbarenHost(TheClientDto theClientDto);
}
