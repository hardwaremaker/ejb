package com.lp.server.fertigung.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.fertigung.ejb.JobDetailsBedarfsuebernahmeOffeneEntity;
import com.lp.server.fertigung.service.JobBedarfsuebernahmeOffeneFac;
import com.lp.server.fertigung.service.JobDetailsBedarfsuebernahmeOffeneDto;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobBedarfsuebernahmeOffeneFacBean
		extends JobDetailsBaseFacBean<JobDetailsBedarfsuebernahmeOffeneDto, JobDetailsBedarfsuebernahmeOffeneEntity>
		implements JobBedarfsuebernahmeOffeneFac {

	public JobBedarfsuebernahmeOffeneFacBean() {
		super(JobDetailsBedarfsuebernahmeOffeneEntity.class, JobDetailsFac.AutoBedarfsuebernahmeOffenefindByMandantCNr, PKConst.PK_BEDARFSUEBERNAHME);
	}

	@Override
	protected JobDetailsBedarfsuebernahmeOffeneDto assembleDto(JobDetailsBedarfsuebernahmeOffeneEntity entity) {
		JobDetailsBedarfsuebernahmeOffeneDto dto = new JobDetailsBedarfsuebernahmeOffeneDto();
		
		if (entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setCEmailEmpfaenger(entity.getCEmailEmpfaenger());
		}
		return dto;
	}

	@Override
	protected JobDetailsBedarfsuebernahmeOffeneEntity setEntityFromDto(JobDetailsBedarfsuebernahmeOffeneEntity entity,
			JobDetailsBedarfsuebernahmeOffeneDto dto) {
		if (entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setCEmailEmpfaenger(dto.getCEmailEmpfaenger());
		}
		return entity;
	}

}
