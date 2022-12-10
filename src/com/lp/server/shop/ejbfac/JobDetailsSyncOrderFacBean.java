package com.lp.server.shop.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.shop.ejb.JobDetailsSyncOrderEntity;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobDetailsSyncOrderFacBean
		extends JobDetailsBaseFacBean<JobDetailsSyncOrderDto, JobDetailsSyncOrderEntity> 
		implements JobDetailsSyncOrderFac {
	public JobDetailsSyncOrderFacBean() {
		super(JobDetailsSyncOrderEntity.class, 
				JobDetailsFac.AutoSyncOrderfindByMandantCNr, 
				PKConst.PK_AUTOMATIKSYNCORDER);
	}

	@Override
	protected JobDetailsSyncOrderDto assembleDto(JobDetailsSyncOrderEntity entity) {
		JobDetailsSyncOrderDto dto = new JobDetailsSyncOrderDto();
		if(entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setWebshopIId(entity.getWebshopIId());
		}
		return dto;
	}

	@Override
	protected JobDetailsSyncOrderEntity setEntityFromDto(JobDetailsSyncOrderEntity entity, JobDetailsSyncOrderDto dto) {
		if(entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setWebshopIId(dto.getWebshopIId());
		}
		return entity;
	}
}
