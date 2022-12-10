package com.lp.server.rechnung.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejb.RechnungpositionQuery;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Validator;
import com.lp.server.util.logger.EventLogger;
import com.lp.server.util.logger.LogEventProducer;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

@Stateless
public class RechnungFacBean extends RechnungPruefbarFacBean implements RechnungFac {

	@PersistenceContext
	private EntityManager em;

	private EventLogger evLog = new EventLogger(getEventLoggerFac(), RechnungFacBean.class) ;
	
	private enum Modification {
		NEW,
		UPDATE,
		REMOVE,
		CANCEL;
	}
	private Map<Modification, String> hmModificationLogText; 

	private Map<Modification, String> getModificationLogTextMap() {
		if (hmModificationLogText == null) {
			hmModificationLogText = new HashMap<Modification, String>();
			hmModificationLogText.put(Modification.NEW, "Neue");
			hmModificationLogText.put(Modification.UPDATE, "Update");
			hmModificationLogText.put(Modification.REMOVE, "Loesche");
			hmModificationLogText.put(Modification.CANCEL, "Storniere");
		}
		return hmModificationLogText;
	}
	
	@Override
	protected RechnungPositionDto createRechnungPositionImpl(
			RechnungPositionDto rePosDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities,
			boolean artikelsetAufloesen,boolean bNichtMengenbehafteteAuftragspositionErledigen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		
		RechnungPositionDto repositionDto = super.createRechnungPositionImpl(rePosDto, 
				lagerIId, identities, artikelsetAufloesen,bNichtMengenbehafteteAuftragspositionErledigen, theClientDto);
		
		if (!RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(
				repositionDto.getRechnungpositionartCNr())) {
			return repositionDto;
		}

		Rechnung rechnung = em.find(Rechnung.class, repositionDto.getRechnungIId());
		String erwarteterStatus = RechnungFac.RECHNUNGART_PROFORMARECHNUNG.equals(rechnung.getRechnungartCNr()) ?
				LieferscheinFac.LSSTATUS_GELIEFERT : LieferscheinFac.LSSTATUS_VERRECHNET;
		checkAndLogLieferscheinStatusAfterModification(Modification.NEW,
				erwarteterStatus, repositionDto);
		return repositionDto;
	}
	
	@Override
	protected RechnungPositionDto updateRechnungPositionImpl(
			RechnungPositionDto rechnungPositionDto, boolean bucheAmLager,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			Artikelset artikelset, boolean recalculatePosition,
			boolean aktualisiereAuftragStatus, boolean artikelsetAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		RechnungPositionDto repositionDto = super.updateRechnungPositionImpl(rechnungPositionDto, bucheAmLager,
				notyetUsedIdentities, artikelset, recalculatePosition,
				aktualisiereAuftragStatus, artikelsetAufloesen, theClientDto);

		if (!RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(
				repositionDto.getRechnungpositionartCNr())) {
			return repositionDto;
		}
		
		Rechnung rechnung = em.find(Rechnung.class, repositionDto.getRechnungIId());
		String erwarteterStatus = RechnungFac.RECHNUNGART_PROFORMARECHNUNG.equals(rechnung.getRechnungartCNr()) ?
				LieferscheinFac.LSSTATUS_GELIEFERT : LieferscheinFac.LSSTATUS_VERRECHNET;
		checkAndLogLieferscheinStatusAfterModification(Modification.UPDATE,
				erwarteterStatus, repositionDto);
		return repositionDto;
	}
	
	@Override
	public void removeRechnungPosition(RechnungPositionDto rechnungPositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super.removeRechnungPosition(rechnungPositionDto, theClientDto);
		
		if (!RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN.equals(
				rechnungPositionDto.getRechnungpositionartCNr())) {
			return;
		}
		checkAndLogLieferscheinStatusAfterModification(Modification.REMOVE,
				LieferscheinFac.LSSTATUS_GELIEFERT, rechnungPositionDto);
	}
	
	@Override
	public void storniereRechnung(Integer rechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		List<Rechnungposition> lspositionen = 
				RechnungpositionQuery.listByRechnungIdLieferscheinpositionen(em, rechnungIId);
		List<RechnungPositionDto> posDtos = new ArrayList<RechnungPositionDto>();
		for (Rechnungposition lspos : lspositionen) {
			RechnungPositionDto rechposDto = new RechnungPositionDto();
			rechposDto.setIId(lspos.getIId());
			rechposDto.setLieferscheinIId(lspos.getLieferscheinIId());
			rechposDto.setRechnungIId(lspos.getRechnungIId());
			posDtos.add(rechposDto);
		}
		
		super.storniereRechnung(rechnungIId, theClientDto);
		
		for (RechnungPositionDto rechposDto : posDtos) {
			checkAndLogLieferscheinStatusAfterModification(Modification.CANCEL, 
					LieferscheinFac.LSSTATUS_GELIEFERT, rechposDto);
		}
	}

	private void checkAndLogLieferscheinStatusAfterModification(Modification modification,
			String statusToCheck, RechnungPositionDto rechposDto) {
		LieferscheinDto lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(
				rechposDto.getLieferscheinIId());
		
		if (statusToCheck.equals(lieferscheinDto.getStatusCNr())) {
			evLog.info(getModificationLogTextMap().get(modification) + " Rechnungsposition-Lieferschein ("
					+ "iId=" + rechposDto.getIId() + ", rechnungIId=" + rechposDto.getRechnungIId() + ")", 
						LogEventProducer.create(lieferscheinDto));
		} else {
			evLog.error(getModificationLogTextMap().get(modification) + " Rechnungsposition-Lieferschein ("
					+ "iId=" + rechposDto.getIId() + ", rechnungIId=" + rechposDto.getRechnungIId() + ") "
					+ " erwarteter Status = " + statusToCheck
					+ ")\n>>>Stacktrace\n" + getLPStackTrace(Thread.currentThread().getStackTrace()) + "\n", 
						LogEventProducer.create(lieferscheinDto));
		}
	}
	
	@Override
	public Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		checkLogLieferscheinpositionen(iid, theClientDto);

		return super.berechneBeleg(iid, theClientDto);
	}
	
	private void checkLogLieferscheinpositionen(Integer rechnungIId, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException {
		Rechnung rechnung = em.find(Rechnung.class, rechnungIId);
		if (RechnungFac.RECHNUNGART_PROFORMARECHNUNG.equals(rechnung.getRechnungartCNr()))
			return;
		
		List<Rechnungposition> lspositionen = 
				RechnungpositionQuery.listByRechnungIdLieferscheinpositionen(em, rechnungIId);
		
		for (Rechnungposition lspos : lspositionen) {
			Validator.notNull(lspos.getLieferscheinIId(), 
					"lieferscheinIId von Lieferscheinrechnungsposition mit iId=" + String.valueOf(lspos.getIId()) 
							+ " aus Rechnung '" + rechnung.getCNr() + "'");
			
			Lieferschein lieferschein = em.find(Lieferschein.class, lspos.getLieferscheinIId());
			if (lieferschein.getRechnungIId() == null) {
				evLog.error("Aktivierung von Rechnung (iId=" + rechnung.getIId() + "): "
						+ "RechnungIId im Lieferschein (rechnungpositionIId="
						+ lspos.getIId() + ") wurde nachgetragen!", 
						LogEventProducer.create(
						getLieferscheinFac().lieferscheinFindByPrimaryKey(lspos.getLieferscheinIId())));
				lieferschein.setRechnungIId(rechnungIId);
				em.merge(lieferschein);
			}

			if (!LieferscheinFac.LSSTATUS_VERRECHNET.equals(lieferschein.getLieferscheinstatusCNr())) {
				evLog.error("Aktivierung von Rechnung (iId=" + rechnung.getIId() + "): "
						+ "Status des Lieferscheins (rechnungpositionIId=" 
						+ lspos.getIId() + ") wurde von '" + lieferschein.getLieferscheinstatusCNr() 
						+ "auf '" + LieferscheinFac.LSSTATUS_VERRECHNET + "' korrigiert!", 
						LogEventProducer.create(
						getLieferscheinFac().lieferscheinFindByPrimaryKey(lspos.getLieferscheinIId())));
				lieferschein.setLieferscheinstatusCNr(LieferscheinFac.LSSTATUS_VERRECHNET);
				em.merge(lieferschein);
			}
		}
	}
}
