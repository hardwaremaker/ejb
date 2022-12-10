package com.lp.server.bestellung.service;

import javax.ejb.Remote;

import com.lp.server.system.service.JobDetailsFac;

@Remote
public interface JobWEJournalFac extends JobDetailsFac<JobDetailsWEJournalDto> {

}
