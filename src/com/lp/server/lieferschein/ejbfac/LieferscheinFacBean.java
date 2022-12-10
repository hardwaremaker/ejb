package com.lp.server.lieferschein.ejbfac;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.EventLogger;
import com.lp.server.util.logger.LogEventProducer;
import com.lp.util.EJBExceptionLP;

@Stateless
public class LieferscheinFacBean extends LieferscheinPruefbarFacBean implements LieferscheinFac, LieferscheinFacLocal {

	@PersistenceContext
	private EntityManager em;
	
	private EventLogger evLog = new EventLogger(getEventLoggerFac(), LieferscheinFacBean.class) ;

	@Override
	public void setzeStatusLieferschein(Integer iIdLieferscheinI,
			String sStatusI, Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdLieferscheinI == null) {
			super.setzeStatusLieferschein(iIdLieferscheinI, sStatusI, iIdRechnungI, theClientDto);
			return;
		}

		String statusVorher = getLieferscheinStatus(iIdLieferscheinI);
		super.setzeStatusLieferschein(iIdLieferscheinI, sStatusI, iIdRechnungI, theClientDto);
		
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(iIdLieferscheinI);
		if (sStatusI.equals(lieferscheinDto.getStatusCNr())) {
			evLog.info("Statusaenderung Lieferschein ('" + statusVorher + "' auf '" + sStatusI + "')", 
				LogEventProducer.create(lieferscheinDto));
			
		} else {
			evLog.error("Statusaenderung Lieferschein ('" + statusVorher + "' auf '" + sStatusI + "')\n" 
					+ ">>>Stacktrace\n" + getLPStackTrace(Thread.currentThread().getStackTrace()), 
					LogEventProducer.create(lieferscheinDto));
		}
	}
	
	private String getLieferscheinStatus(Integer lieferscheinIId) {
		Lieferschein lieferschein = em.find(Lieferschein.class, lieferscheinIId);
		return lieferschein.getLieferscheinstatusCNr();
	}
	
	@Override
	public void updateLieferscheinOhneWeitereAktion(
			LieferscheinDto lieferscheinDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (lieferscheinDtoI == null || lieferscheinDtoI.getIId() == null) {
			super.updateLieferscheinOhneWeitereAktion(lieferscheinDtoI, theClientDto);
			return;
		}
		
		String statusVorher = getLieferscheinStatus(lieferscheinDtoI.getIId());
		super.updateLieferscheinOhneWeitereAktion(lieferscheinDtoI, theClientDto);
		
		checkLogIfStatusChanged(lieferscheinDtoI.getIId(), statusVorher);
	}
	
	@Override
	public boolean updateLieferschein(LieferscheinDto lieferscheinDtoI,
			Integer[] aAuftragIIdI, String waehrungOriCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lieferscheinDtoI == null || lieferscheinDtoI.getIId() == null) {
			return super.updateLieferschein(lieferscheinDtoI, aAuftragIIdI,
					waehrungOriCNrI, theClientDto);
		}

		String statusVorher = getLieferscheinStatus(lieferscheinDtoI.getIId());
		boolean updateResult = super.updateLieferschein(lieferscheinDtoI, aAuftragIIdI,
				waehrungOriCNrI, theClientDto);

		checkLogIfStatusChanged(lieferscheinDtoI.getIId(), statusVorher);
		return updateResult;
	}
	
	@Override
	public void updateLieferschein(LieferscheinDto lieferscheinDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (lieferscheinDtoI == null || lieferscheinDtoI.getIId() == null) {
			super.updateLieferschein(lieferscheinDtoI, theClientDto);
			return;
		}

		String statusVorher = getLieferscheinStatus(lieferscheinDtoI.getIId());
		super.updateLieferschein(lieferscheinDtoI, theClientDto);
		
		checkLogIfStatusChanged(lieferscheinDtoI.getIId(), statusVorher);
	}
	
	private void checkLogIfStatusChanged(Integer lieferscheinIId, String statusVorher) {
		LieferscheinDto lieferscheinDto = lieferscheinFindByPrimaryKey(lieferscheinIId);
		if (!statusVorher.equals(lieferscheinDto.getStatusCNr())) {
			evLog.info("Update Lieferschein mit Statusaenderung ('" + statusVorher + "' auf '" 
					+ lieferscheinDto.getStatusCNr() + "')\n" 
					+ ">>>Stacktrace\n" + getLPStackTrace(Thread.currentThread().getStackTrace()), 
					LogEventProducer.create(lieferscheinDto));
		}
	}
}
