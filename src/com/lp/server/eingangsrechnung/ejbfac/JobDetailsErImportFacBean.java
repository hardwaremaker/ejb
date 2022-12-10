package com.lp.server.eingangsrechnung.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.eingangsrechnung.ejb.JobDetailsErImportEntity;
import com.lp.server.eingangsrechnung.service.JobDetailsErImportFac;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsErImportDto;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobDetailsErImportFacBean
		extends JobDetailsBaseFacBean<JobDetailsErImportDto, JobDetailsErImportEntity> implements JobDetailsErImportFac {

	public JobDetailsErImportFacBean() {
		super(JobDetailsErImportEntity.class, JobDetailsFac.AutoErImportfindByMandantCNr, PKConst.PK_AUTOMATIKERIMPORT);
	}

	@Override
	protected JobDetailsErImportDto assembleDto(JobDetailsErImportEntity entity) {
		JobDetailsErImportDto dto = new JobDetailsErImportDto();
		if (entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setCImportPfad(entity.getCImportPfad());
			dto.setCEmailErfolgreich(entity.getCEmailErfolgreich());
			dto.setCEmailFehler(entity.getCEmailFehler());
		}
		return dto;
	}

	@Override
	protected JobDetailsErImportEntity setEntityFromDto(JobDetailsErImportEntity entity,
			JobDetailsErImportDto dto) {
		if (entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setCImportPfad(dto.getCImportPfad());
			entity.setCEmailErfolgreich(dto.getCEmailErfolgreich());
			entity.setcCEmailFehler(dto.getCEmailFehler());
		}
		return entity;
	}

}
