package com.lp.server.shop.ejbfac;

import javax.ejb.Local;

import com.lp.server.util.AutomatikjobId;

@Local
public interface ShopTimerCronJobLocal {

	boolean startJob(AutomatikjobId jobId);
	boolean stopJob(AutomatikjobId jobId);
	boolean isActive(AutomatikjobId jobId);
}
