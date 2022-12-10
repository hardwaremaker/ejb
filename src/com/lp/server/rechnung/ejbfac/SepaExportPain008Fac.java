package com.lp.server.rechnung.ejbfac;

import java.util.List;

import javax.ejb.Local;

import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.sepa.errors.SepaException;
import com.lp.server.system.service.TheClientDto;

@Local
public interface SepaExportPain008Fac {

	Object transform(List<LastschriftvorschlagDto> dtos, TheClientDto theClientDto) throws SepaException;
	
	List<SepaException> checkData(List<LastschriftvorschlagDto> dtos, TheClientDto theClientDto);
}
