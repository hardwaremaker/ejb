package com.lp.server.bestellung.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.bestellung.ejb.JobDetailsWEJournalEntity;
import com.lp.server.bestellung.service.JobDetailsWEJournalDto;
import com.lp.server.bestellung.service.JobWEJournalFac;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobWEJournalFacBean extends JobDetailsBaseFacBean<JobDetailsWEJournalDto, JobDetailsWEJournalEntity> implements JobWEJournalFac {

	public JobWEJournalFacBean() {
		super(JobDetailsWEJournalEntity.class, JobDetailsFac.AutoWEJournalfindByMandantCNr, PKConst.PK_AUTOMATIKWEJOURNAL);
	}

	@Override
	protected JobDetailsWEJournalDto assembleDto(JobDetailsWEJournalEntity entity) {
		JobDetailsWEJournalDto dto = new JobDetailsWEJournalDto();
		
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
	protected JobDetailsWEJournalEntity setEntityFromDto(JobDetailsWEJournalEntity entity, JobDetailsWEJournalDto dto) {
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
