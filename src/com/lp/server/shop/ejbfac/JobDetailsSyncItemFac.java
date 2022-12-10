package com.lp.server.shop.ejbfac;

import javax.ejb.Remote;

import com.lp.server.system.service.JobDetailsFac;

@Remote
public interface JobDetailsSyncItemFac extends JobDetailsFac<JobDetailsSyncItemDto> {

}
