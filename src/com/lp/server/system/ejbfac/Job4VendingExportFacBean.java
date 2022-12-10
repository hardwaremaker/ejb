package com.lp.server.system.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.system.ejb.JobDetails4VendingExportEntity;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.Job4VendingExportFac;
import com.lp.server.system.service.JobDetails4VendingExportDto;
import com.lp.server.system.service.JobDetailsFac;
import com.lp.util.Helper;

@Stateless
public class Job4VendingExportFacBean extends JobDetailsBaseFacBean<JobDetails4VendingExportDto, JobDetails4VendingExportEntity> implements Job4VendingExportFac {

	public Job4VendingExportFacBean() {
		super(JobDetails4VendingExportEntity.class, JobDetailsFac.Auto4VendingExportfindByMandantCNr, PKConst.PK_AUTOMATIK4VENDINGEXPORT);
	}

	@Override
	protected JobDetails4VendingExportDto assembleDto(JobDetails4VendingExportEntity entity) {
		JobDetails4VendingExportDto dto = new JobDetails4VendingExportDto();
		
		if (entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setBArtikel(Helper.short2Boolean(entity.getBArtikel()));
			dto.setBKunden(Helper.short2Boolean(entity.getBKunden()));
			dto.setBLieferanten(Helper.short2Boolean(entity.getBLieferanten()));
			dto.setCPfadPatternArtikel(entity.getCPfadPatternArtikel());
			dto.setCPfadPatternKunden(entity.getCPfadPatternKunden());
			dto.setCPfadPatternLieferanten(entity.getCPfadPatternLieferanten());
			dto.setCEmailErfolgreich(entity.getCEmailErfolgreich());
			dto.setCEmailFehler(entity.getCEmailFehler());
		}
		return dto;
	}

	@Override
	protected JobDetails4VendingExportEntity setEntityFromDto(
			JobDetails4VendingExportEntity entity, JobDetails4VendingExportDto dto) {
		if (entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setBArtikel(dto.getBArtikel() == null ? Helper.getShortFalse() : Helper.boolean2Short(dto.getBArtikel()));
			entity.setBKunden(dto.getBKunden() == null ? Helper.getShortFalse() : Helper.boolean2Short(dto.getBKunden()));
			entity.setBLieferanten(dto.getBLieferanten() == null ? Helper.getShortFalse() : Helper.boolean2Short(dto.getBLieferanten()));
			entity.setCPfadPatternKunden(dto.getCPfadPatternKunden());
			entity.setCPfadPatternLieferanten(dto.getCPfadPatternLieferanten());
			entity.setCPfadPatternArtikel(dto.getCPfadPatternArtikel());
			entity.setCEmailErfolgreich(dto.getCEmailErfolgreich());
			entity.setCEmailFehler(dto.getCEmailFehler());
		}
		return entity;
	}

}
