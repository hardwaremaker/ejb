package com.lp.server.artikel.service;

import javax.ejb.Remote;

import com.lp.server.system.service.JobDetailsFac;

@Remote
public interface JobDetailsWebabfrageArtikellieferantFac
		extends JobDetailsFac<JobDetailsWebabfrageArtikellieferantDto> {

}
