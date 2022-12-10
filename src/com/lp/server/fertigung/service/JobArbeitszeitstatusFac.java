package com.lp.server.fertigung.service;

import javax.ejb.Remote;

import com.lp.server.system.service.JobDetailsFac;

@Remote
public interface JobArbeitszeitstatusFac extends JobDetailsFac<JobDetailsArbeitszeitstatusDto> {

}
