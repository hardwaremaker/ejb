package com.lp.server.fertigung.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.fertigung.ejb.JobDetailsArbeitszeitstatusEntity;
import com.lp.server.fertigung.service.JobArbeitszeitstatusFac;
import com.lp.server.fertigung.service.JobDetailsArbeitszeitstatusDto;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobArbeitszeitstatusFacBean extends JobDetailsBaseFacBean<JobDetailsArbeitszeitstatusDto, JobDetailsArbeitszeitstatusEntity>implements JobArbeitszeitstatusFac {

	public JobArbeitszeitstatusFacBean() {
		super(JobDetailsArbeitszeitstatusEntity.class, JobDetailsFac.AutoArbeitszeitstatusfindByMandantCNr, PKConst.PK_AUTOMATIKARBEITSZEITSTATUS);
	}

	@Override
	protected JobDetailsArbeitszeitstatusDto assembleDto(JobDetailsArbeitszeitstatusEntity entity) {
		JobDetailsArbeitszeitstatusDto dto = new JobDetailsArbeitszeitstatusDto();
		
		if (entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setCPfadPattern(entity.getCPfadPattern());
			dto.setCEmailEmpfaenger(entity.getCEmailEmpfaenger());
			dto.setITageBisStichtag(entity.getITageBisStichtag());
			dto.setIArchivierungstage(entity.getIArchivierungstage());
		}
		
		return dto;
	}

	@Override
	protected JobDetailsArbeitszeitstatusEntity setEntityFromDto(JobDetailsArbeitszeitstatusEntity entity,
			JobDetailsArbeitszeitstatusDto dto) {
		if (entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setCPfadPattern(dto.getCPfadPattern());
			entity.setCEmailEmpfaenger(dto.getCEmailEmpfaenger());
			entity.setITageBisStichtag(dto.getITageBisStichtag() != null 
					? dto.getITageBisStichtag() : 1);
			entity.setIArchivierungstage(dto.getIArchivierungstage());
		}
		
		return entity;
	}

}
