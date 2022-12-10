package com.lp.server.system.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.system.ejb.JobDetailsKpiReportEntity;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsKpiReportDto;
import com.lp.server.system.service.JobKpiReportFac;

@Stateless
public class JobKpiReportFacBean extends 
	JobDetailsBaseFacBean<JobDetailsKpiReportDto, JobDetailsKpiReportEntity>  implements JobKpiReportFac {
	protected JobKpiReportFacBean() {
		super(JobDetailsKpiReportEntity.class, 
				JobKpiReportFac.KpiReportfindByMandantCnr,
				PKConst.PK_AUTOMATIKKPIREPORT);
	}

	@Override
	protected JobDetailsKpiReportDto assembleDto(JobDetailsKpiReportEntity entity) {
		JobDetailsKpiReportDto dto = new JobDetailsKpiReportDto();
		
		if(entity != null) {
			dto.setcPfadPattern(entity.getcPfadPattern());
			dto.setiArchivierungstage(entity.getiArchivierungstage());
			dto.setiTage(entity.getiTage());
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setcEmailEmpfaenger(entity.getcEmailEmpfaenger());
		}
		
		return dto;
	}

	@Override
	protected JobDetailsKpiReportEntity setEntityFromDto(
			JobDetailsKpiReportEntity entity, JobDetailsKpiReportDto dto) {
		if(entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setcPfadPattern(dto.getcPfadPattern());
			entity.setiArchivierungstage(dto.getiArchivierungstage());
			entity.setiTage(dto.getiTage());
			entity.setcEmailEmpfaenger(dto.getcEmailEmpfaenger());
		}
		
		return entity;
	}
}
