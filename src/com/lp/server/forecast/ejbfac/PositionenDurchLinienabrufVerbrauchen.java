package com.lp.server.forecast.ejbfac;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.forecast.ejb.Forecastposition;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Validator;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class PositionenDurchLinienabrufVerbrauchen {
	private static final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			PositionenDurchLinienabrufVerbrauchen.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	private ForecastFac forecastFacBean;
	private ArtikelFac artikelFacBean;
	private EntityManager em;
	
	public PositionenDurchLinienabrufVerbrauchen(ForecastFac forecastFacBean, ArtikelFac artikelFacBean, EntityManager em) {
		Validator.notNull(forecastFacBean, "forecastFacBean");
		this.forecastFacBean = forecastFacBean;
		this.artikelFacBean = artikelFacBean;
		this.em = em;
	}

	public void doIt(List<ForecastpositionDto> positions, TheClientDto theClientDto) {
		Validator.notNull(positions, "positions");	
		
		List<ForecastpositionDtoEx> exPositions = createPositions(positions);
		iterate(exPositions, theClientDto);
		persist(exPositions);
	}
	

	protected List<ForecastpositionDtoEx> createPositions(List<ForecastpositionDto> positions) {
		List<ForecastpositionDtoEx> exPositions = new ArrayList<ForecastpositionDtoEx>();
		for (ForecastpositionDto pos : positions) {
			ForecastpositionDtoEx exPos = new ForecastpositionDtoEx(pos, forecastFacBean
					.linienabrufFindByForecastpositionId(pos.getIId()));
			exPositions.add(exPos);			
		}
		return exPositions;
	}
	
	protected Set<Integer> getArtikelIdsMitLinienabrufen(List<ForecastpositionDtoEx> positions) {
		Set<Integer> artikelIds = new HashSet<Integer>();
		for (ForecastpositionDtoEx posDto : positions) {
			if(posDto.hasAbrufe()) {
				artikelIds.add(posDto.getArtikelIId());
			}
		}
		return artikelIds;
	}
	
	protected void persist(List<ForecastpositionDtoEx> positions) {
		for (ForecastpositionDtoEx posDto : positions) {
			if(posDto.isModified()) {
				Forecastposition fp = em.find(Forecastposition.class, posDto.getIId());
				myLogger.warn("fcpos '" + posDto.getIId() + "' amount from " + fp.getNMenge() + " to " + posDto.getNMenge() + ".");
				fp.setNMenge(posDto.getNMenge());
				em.merge(fp);
				em.flush();
			}
		}
	}
	
	protected void iterate(Integer artikelId, 
			List<ForecastpositionDtoEx> positions, TheClientDto theClientDto) {
		BigDecimal pool = BigDecimal.ZERO;
		Date abrufDate = null;
		
		ArtikelDto artikelDto = artikelFacBean.artikelFindByPrimaryKey(artikelId, theClientDto);
		myLogger.warn("Item '" +  artikelDto.getCNr() + "' (id:" + artikelId + ")...");
//		if("A13140801".equals(artikelDto.getCNr()) || "A10871401".equals(artikelDto.getCNr()) || "A10871501".equals(artikelDto.getCNr())) {
//			myLogger.warn("halt");
//		}

		List<ForecastpositionDtoEx> filtered = filterItemId(artikelId, positions);
		if(!hasAbrufe(0, filtered)) {
			myLogger.warn("Item '" + artikelDto.getCNr() + "' (id:" + artikelId + ") has no LAs. Nothing to do.");
			return;
		}
		
		for (int i = 0; i < filtered.size(); i++) {	
			ForecastpositionDtoEx exPos = filtered.get(i);
			String exPosInfo = "FC Pos '" + exPos.getIId() + "' date '" + asDate(exPos.getTTermin()) + "' ";
			if(exPos.hasAbrufe()) {
				if(abrufDate != null) {
					myLogger.warn(exPosInfo + 
						"next LA without fullfilled old LA at date '" + asDate(abrufDate) + ".");
				}
				abrufDate = exPos.getTTermin();
				myLogger.warn(exPosInfo +
						"is LA at date '" + asDate(abrufDate) + "' with " + asBD(exPos.getAbrufMenge()) + ".");

// SP5967 Ob die Menge gleich ist, spielt keine Rolle
//				if(exPos.getNMenge().equals(exPos.getAbrufMenge())) {
//					myLogger.warn(exPosInfo + "skipped, Pos amount == LA amount.");
//					continue;
//				}
				
				exPos.setNMenge(exPos.getAbrufMenge());
				pool = pool.subtract(exPos.getNMenge());
				exPos.beModified();
				
				if(pool.signum() > 0) {
					Date nextDay = nextDay(abrufDate);
					if(i + 1 < filtered.size()) {
						ForecastpositionDtoEx nextPos = filtered.get(i+1);
						if(nextDay.equals(nextPos.getTTermin())) {
							myLogger.warn("found next position (id:" + nextPos.getIId() + ") on nextday '" + asDate(nextDay) + "' -> adding pool amount " 
									+ asBD(pool) + " to this position amount " + asBD(nextPos.getNMenge()) + ")");
							nextPos.setNMenge(nextPos.getNMenge().add(pool));
							nextPos.beModified();
						} else {
							myLogger.warn("creating new forecastposition with pool amount " + asBD(pool) + " (new position).");
							consumePool(pool, exPos.getDto());
						}
					} else {
						myLogger.warn("creating new forecastposition with pool amount " + asBD(pool) + " (last position).");
						consumePool(pool, exPos.getDto());
					}
					
					pool = BigDecimal.ZERO;
				}
				abrufDate = null;
			} else {
				if(abrufDate == null) {
					if(hasAbrufe(i, filtered)) {
						pool = pool.add(exPos.getNMenge());
						myLogger.warn(exPosInfo + 
								"unknown LA date, adding " + asBD(exPos.getNMenge()) + " to pool (now " + asBD(pool) + ").");
						exPos.setNMenge(BigDecimal.ZERO);
						exPos.beModified();						
					} else {
						myLogger.warn(exPosInfo +
								"no further LA available. Stopping.");
						break;
					}
				} else {
					if(pool.signum() == 0) {
						myLogger.warn(exPosInfo + "pool empty.");
						abrufDate = null;
					} else {
						String log = exPosInfo + "after LA '" + asDate(abrufDate);
						int diff = pool.compareTo(exPos.getNMenge());
						if(diff > 0) {
							myLogger.warn(log + " pool > pos_menge (" + asBD(pool) + "/" + asBD(exPos.getNMenge()) + "), setting pos to 0");
							pool = pool.subtract(exPos.getNMenge());
							exPos.setNMenge(BigDecimal.ZERO);
							exPos.beModified();
							myLogger.warn("  pool is now " + asBD(pool));
						} else {
							myLogger.warn(log + " pool < pos_menge (" + asBD(pool) + "/" + asBD(exPos.getNMenge()) + "), reducing with pool amount " + asBD(pool) + ".");
							BigDecimal d = exPos.getNMenge().min(pool.abs());
							exPos.setNMenge(exPos.getNMenge().subtract(d));
							exPos.beModified();
							pool = pool.signum() < 0 ? pool.add(d) : pool.subtract(d);
							if(pool.signum() == 0) {
								abrufDate = null;
							}
							myLogger.warn("  pool is now " + asBD(pool));
						}						
					}
				}
			}
		}		
		
		if(abrufDate != null) {
			myLogger.warn("Item '" + artikelDto.getCNr() + "' (id:" + artikelId + ") has LA date '" + asDate(abrufDate) + " with pool size " + asBD(pool) + ".");
			if(pool.signum() != 0) {
				myLogger.warn("  POOL " + asBD(pool) + " is NOT ZERO");
			}
			return;
		}
		
		if(pool.signum() != 0) {
			myLogger.warn("Pool is not ZERO! (" + asBD(pool) + ") for Item '" + artikelDto.getCNr() + "' (id:" + artikelId + "), ignored no LA");				
		}			
	}
	
	private Date nextDay(Date abrufDate) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(abrufDate);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
	
	private void consumePool(BigDecimal pool, ForecastpositionDto abrufposDto) {
		ForecastpositionDto fcPositionDto = new ForecastpositionDto();
		fcPositionDto.setForecastauftragIId(abrufposDto.getForecastauftragIId());
		fcPositionDto.setArtikelIId(abrufposDto.getArtikelIId());
		fcPositionDto.setCBestellnummer("");
		fcPositionDto
				.setForecastartCNr(ForecastFac.FORECASTART_NICHT_DEFINIERT);
		fcPositionDto.setNMenge(pool);
		fcPositionDto.setNMengeErfasst(BigDecimal.ZERO);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(abrufposDto.getTTermin());
		c.add(Calendar.DATE, 1);
		fcPositionDto.setTTermin(new Timestamp(nextDay(abrufposDto.getTTermin()).getTime()));
		fcPositionDto.setXKommentar("Pool aus Abruf vom " + asDate(abrufposDto.getTTermin()));
		fcPositionDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		forecastFacBean.createForecastposition(fcPositionDto);
	}
	
	private String asBD(BigDecimal d) {
		return "'" + d.toPlainString() + "'";
	}
	
	private String asDate(Date d) {
		return sdf.format(d);
	}
	
	protected void iterate(List<ForecastpositionDtoEx> positions, TheClientDto theClientDto) {		
		Set<Integer> artikelIds = getArtikelIdsMitLinienabrufen(positions);
		for (Integer artikelId : artikelIds) {
			iterate(artikelId, positions, theClientDto);
		}
	}

	private List<ForecastpositionDtoEx> filterItemId(Integer itemId, List<ForecastpositionDtoEx> collection) {
		List<ForecastpositionDtoEx> filtered = new ArrayList<ForecastpositionDtoEx>();
		for (ForecastpositionDtoEx exPos : collection) {
			if(itemId.equals(exPos.getArtikelIId())) {
				filtered.add(exPos);
			}
		}
		return filtered;
	}

	private boolean hasAbrufe(int startIndex, List<ForecastpositionDtoEx> exPositions) {
		for(int i = startIndex; i < exPositions.size(); i++) {
			ForecastpositionDtoEx exPos = exPositions.get(i);
			if(exPos.hasAbrufe()) return true;
		}
		
		return false;
	}
	
	public class ForecastpositionDtoEx extends ForecastpositionDto {
		private static final long serialVersionUID = 3995821460312624294L;
		
		private ForecastpositionDto pos;
		private boolean modified;
		private List<LinienabrufDto> abrufe;
		
		public ForecastpositionDtoEx(ForecastpositionDto pos, List<LinienabrufDto> abrufe) {
			this.pos = pos;
			this.abrufe = abrufe;
		}
		
		@Override
		public Integer getArtikelIId() {
			return pos.getArtikelIId();
		}
		
		@Override
		public Timestamp getTTermin() {
			return pos.getTTermin();
		}

		@Override
		public void setTTermin(Timestamp t_termin) {
			super.setTTermin(t_termin);
		}

		@Override
		public BigDecimal getNMenge() {
			return pos.getNMenge();
		}
		
		public void setNMenge(BigDecimal menge) {
			pos.setNMenge(menge);
		}
		
		@Override
		public Integer getIId() {
			return pos.getIId();
		}
 
		@Override
		public BigDecimal getNMengeErfasst() {
			return pos.getNMengeErfasst();
		}
		
		@Override
		public void setNMengeErfasst(BigDecimal nMengeErfasst) {
			pos.setNMengeErfasst(nMengeErfasst);
		}
		
		public boolean isModified() {
			return modified;
		}

		public void setModified(boolean modified) {
			this.modified = modified;
		}
		
		public void beModified() {
			setModified(true);
		}
		
		public List<LinienabrufDto> getAbrufe() {
			return abrufe;
		}
		
		public void setAbrufe(List<LinienabrufDto> abrufe) {
			this.abrufe = abrufe;
		}
		
		public boolean hasAbrufe() {
			return abrufe != null && !abrufe.isEmpty();
		}
		
		public BigDecimal getAbrufMenge() {
			BigDecimal d = BigDecimal.ZERO;
			if(!hasAbrufe()) return d;
			
			for (LinienabrufDto abrufDto : abrufe) {
				d = d.add(abrufDto.getNMenge());
			}
			return d;
		}
		
		public ForecastpositionDto getDto() {
			return pos;
		}
	}
	
	public class ArtikelMitLinienabrufen  {
		private Map<Integer, List<LinienabrufDto>> mapItems;
		
		public void put(Integer artikelId, List<LinienabrufDto> abrufe) {
			mapItems.put(artikelId, abrufe);
		}
		
		public List<LinienabrufDto> get(Integer artikelId) {
			return mapItems.get(artikelId);
		}
		
		public Set<Integer> getArtikels() {
			return mapItems.keySet();
		}
	}
}

