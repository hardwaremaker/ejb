package com.lp.server.eingangsrechnung.service;

import javax.ejb.Remote;

import com.lp.server.system.service.JobDetailsErImportDto;
import com.lp.server.system.service.JobDetailsFac;

@Remote
public interface JobDetailsErImportFac extends JobDetailsFac<JobDetailsErImportDto> {

}
