package com.lp.server.system.ejbfac;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.report.PositionRpt;

@Local
public interface SystemReportFacLocal {

	public PositionRpt getPositionForReport(String sBelegart, Integer iBelegpositionIId, TheClientDto theClientDto);

}
