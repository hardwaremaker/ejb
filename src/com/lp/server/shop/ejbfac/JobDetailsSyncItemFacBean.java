package com.lp.server.shop.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.shop.ejb.JobDetailsSyncItemEntity;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobDetailsSyncItemFacBean
		extends JobDetailsBaseFacBean<JobDetailsSyncItemDto, JobDetailsSyncItemEntity> 
		implements JobDetailsSyncItemFac {
	public JobDetailsSyncItemFacBean() {
		super(JobDetailsSyncItemEntity.class, 
				JobDetailsFac.AutoSyncArtikelfindByMandantCNr, 
				PKConst.PK_AUTOMATIKSYNCITEM);
	}

	@Override
	protected JobDetailsSyncItemDto assembleDto(JobDetailsSyncItemEntity entity) {
		JobDetailsSyncItemDto dto = new JobDetailsSyncItemDto();
		if(entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setWebshopIId(entity.getWebshopIId());
			dto.setCEmailErfolgreich(entity.getCEmailErfolgreich());
			dto.setCEmailFehler(entity.getCEmailFehler());
			dto.setTGeaendert(entity.getTGeaendert());
			dto.setTVollstaendig(entity.getTVollstaendig());
		}
		return dto;
	}

	@Override
	protected JobDetailsSyncItemEntity setEntityFromDto(JobDetailsSyncItemEntity entity, JobDetailsSyncItemDto dto) {
		if(entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setWebshopIId(dto.getWebshopIId());
			entity.setCEmailErfolgreich(dto.getCEmailErfolgreich());
			entity.setCEmailFehler(dto.getCEmailFehler());
			entity.setTGeaendert(dto.getTGeaendert());
			entity.setTVollstaendig(dto.getTVollstaendig());
		}
		return entity;
	}
}
