package com.lp.server.system.service;

import javax.ejb.Remote;

@Remote
public interface JobKpiReportFac extends JobDetailsFac<JobDetailsKpiReportDto> {
	public final static String KpiReportfindByMandantCnr = "AutoReportKpifindByMandantCnr";
}
