package com.lp.server.shop.ejbfac;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.Transient;

import com.lp.server.shop.service.ShopTimerFac;
import com.lp.server.util.AutomatikjobId;
import com.lp.server.util.Facade;

@Singleton
@Startup
public class ShopTimerCronJobs extends Facade implements ShopTimerCronJobLocal {
	@Transient
	private transient Set<AutomatikjobId> activeJobs;

	@PostConstruct
    public void init() {
		myLogger.info("Init ShopTimer");
		
		//Delete 0 Byte Persistence-Files
		File dir = new File("../standalone/data/timer-service-data/lpserver.ejb.ShopTimerFacBean");
		if (dir != null) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (!file.isDirectory()) {
						if (file.length()==0) {
							try {
								file.delete();
								myLogger.info("0-Byte Timer removed: )" + file.getName());
							} catch (Exception ex) {
								myLogger.info("0-Byte Timer NOT removed: )" + file.getName());
							}
						}
							
					}
                }
            }
        }
        
		getShopTimerFac().setTimer(ShopTimerFac.DEFAULT_DURATION);
	}
	
	@PreDestroy
    private void shutdown() {
		myLogger.info("Remove ShopTimer");
		getShopTimerFac().setTimer(0);
    }
	
	public ShopTimerCronJobs() {
		activeJobs = new HashSet<AutomatikjobId>();
	}
	
	@Override
	public boolean startJob(AutomatikjobId jobId) {
		synchronized (jobId) {
			if(activeJobs.contains(jobId)) {
				return false;
			}
			
			activeJobs.add(jobId);
			return true;			
		}
	}
	
	@Override
	public boolean stopJob(AutomatikjobId jobId) {
		return activeJobs.remove(jobId);
	}
	
	@Override
	public boolean isActive(AutomatikjobId jobId) {
		return activeJobs.contains(jobId);
	}
}
