package com.lp.server.artikel.ejbfac;

import javax.ejb.Stateless;

import com.lp.server.artikel.ejb.JobDetailsWebabfrageArtikellieferantEntity;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantDto;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantFac;
import com.lp.server.system.ejbfac.JobDetailsBaseFacBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.JobDetailsFac;

@Stateless
public class JobDetailsWebabfrageArtikellieferantFacBean extends
		JobDetailsBaseFacBean<JobDetailsWebabfrageArtikellieferantDto, JobDetailsWebabfrageArtikellieferantEntity>
		implements JobDetailsWebabfrageArtikellieferantFac {

	public JobDetailsWebabfrageArtikellieferantFacBean() {
		super(JobDetailsWebabfrageArtikellieferantEntity.class, JobDetailsFac.AutoArtikellieferantWebabfragefindByMandantCNr, PKConst.PK_AUTOMATIKWEBABFRAGEARTIKELLIEFERANT);
	}

	@Override
	protected JobDetailsWebabfrageArtikellieferantDto assembleDto(JobDetailsWebabfrageArtikellieferantEntity entity) {
		JobDetailsWebabfrageArtikellieferantDto dto = new JobDetailsWebabfrageArtikellieferantDto();
		if (entity != null) {
			dto.setiId(entity.getiId());
			dto.setMandantCNr(entity.getMandantCNr());
			dto.setCEmailErfolgreich(entity.getCEmailErfolgreich());
			dto.setCEmailFehler(entity.getCEmailFehler());
		}
		return dto;
	}

	@Override
	protected JobDetailsWebabfrageArtikellieferantEntity setEntityFromDto(
			JobDetailsWebabfrageArtikellieferantEntity entity, JobDetailsWebabfrageArtikellieferantDto dto) {
		if (entity != null && dto != null) {
			entity.setMandantCNr(dto.getMandantCNr());
			entity.setCEmailErfolgreich(dto.getCEmailErfolgreich());
			entity.setCEmailFehler(dto.getCEmailFehler());
		}
		return entity;
	}

}
