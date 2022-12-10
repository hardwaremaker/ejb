package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class DelforRepository implements IDelforRepository {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(DelforRepository.class); 

	private KundeDto buyerDto;
	private List<String> headerTexts;
	private Map<Integer, DeliveryAddress> deliveryAddressMap;
	private DelforPosition lastDelforPosition;
	private KundeDto lastLieferkundeDto;
	private CallOffXlsImporterResult result;
	private CallOffXlsImportStats stats;
	private Date lastCumulativeDate;
	private Date lastCumulativeStartDate;
	private BigDecimal lastCumulativeQuantity;
	private String lastCumulativeReference;
	private BigDecimal backlogQuantity;
	private String name;
	private Date creationDate;
	private String controlReference;
	
	public DelforRepository() {
		this("");
	}
	
	public DelforRepository(String name) {
		this.name = name;
		headerTexts = new ArrayList<String>();
		deliveryAddressMap = new HashMap<Integer, DeliveryAddress>();
		stats = new CallOffXlsImportStats();
		result = new CallOffXlsImporterResult(new ArrayList<EJBLineNumberExceptionLP>(), stats);
	}
	
	@Override
	public CallOffXlsImporterResult getResult() {
		return result;
	}
	
	@Override
	public void addError(EJBLineNumberExceptionLP errorInfo) {
		List<EJBLineNumberExceptionLP> errors = result.getMessages();
		if(errors == null) {
			errors = new ArrayList<EJBLineNumberExceptionLP>();
			result.setMessages(errors);
		}
		
		errors.add(errorInfo);
		result.getStats().incrementErrorCounts();
	}
	
	@Override
	public void setBuyer(KundeDto kundeDto) {
		this.buyerDto = kundeDto;
	}

	@Override
	public KundeDto getBuyer() {
		return buyerDto;
	}
	
	@Override
	public void addHeaderFreeText(String freetext) {
		headerTexts.add(freetext);
	}


	@Override
	public void addDeliveryAddress(KundeDto lieferkundeDto) {
		if(!deliveryAddressMap.containsKey(lieferkundeDto.getIId())) {
			deliveryAddressMap.put(
					lieferkundeDto.getIId(), new DeliveryAddress(lieferkundeDto));			
		}
	}

	@Override
	public void addForecastPosition(KundeDto lieferkundeDto, DelforPosition position) {
		DeliveryAddress address = deliveryAddressMap.get(lieferkundeDto.getIId()); 
		if(address == null) {
			address = new DeliveryAddress(lieferkundeDto);
			deliveryAddressMap.put(lieferkundeDto.getIId(), address);
		}
		address.addPosition(createPosition(position));
		stats.incrementTotalExports();
		
		clearQuantityInfos(position);
	}

	private DelforPosition createPosition(DelforPosition position) {
		DelforPosition pos = new DelforPosition();
		pos.setItemDto(position.getItemDto());
		for (String text : position.getTexts()) {
			pos.addText("" + text);
		}
		pos.setOrderReference("" + position.getOrderReference());
		if(position.getQuantity() == null) {
			log.warn("No quantity defined!");
		}
		pos.setQuantity(position.getQuantity().add(BigDecimal.ZERO));
		pos.setDate(new Date(position.getDate().getTime()));
		if(position.getBacklogQuantity() != null) {
			pos.setBacklogQuantity(position.getBacklogQuantity().add(BigDecimal.ZERO));
		}
		return pos;
	}
	
	private void clearQuantityInfos(DelforPosition position) {
		position.setQuantity(null);
		position.setDate(null);
		position.setBacklogQuantity(null);
	}
	
	@Override
	public void addCommitForOrderPosition(KundeDto lieferkundeDto, DelforPosition position) {
		addForecastPosition(lieferkundeDto, position);
	}

	@Override
	public void addCommitForManufactoringPosition(KundeDto lieferkundeDto, DelforPosition position) {
		addForecastPosition(lieferkundeDto, position);
	}

	
	@Override
	public List<String> getHeaderFreeTexts() {
		return headerTexts;
	}
	
	@Override
	public List<KundeDto> getDeliveryAddresses() {
		List<KundeDto> addresses = new ArrayList<KundeDto>();
		for (DeliveryAddress address : deliveryAddressMap.values()) {
			addresses.add(address.getAddressDto());
		}
		return addresses;
	}
	
	public void setDeliveryAddresses(List<KundeDto> deliveryAddresses) {
		deliveryAddressMap.clear();
		for (KundeDto kundeDto : deliveryAddresses) {
			deliveryAddressMap.put(kundeDto.getIId(), new DeliveryAddress(kundeDto));
		}
	}
	
	@Override
	public List<DelforPosition> getPositions(KundeDto deliveryAddress) {
		DeliveryAddress address = deliveryAddressMap.get(deliveryAddress.getIId());
		return address != null ? address.getPositions() : null;
	}
	
	@Override
	public void setPositions(KundeDto addressDto,
			List<DelforPosition> positions) {
		DeliveryAddress address = deliveryAddressMap.get(addressDto.getIId());
		if(address == null) {
			address = deliveryAddressMap.put(addressDto.getIId(), new DeliveryAddress(addressDto));
		}
		address.setPositions(positions);
	}
	
	@Override
	public List<DelforCumulativePosition> getCumulativePositions(KundeDto deliveryAddress) {
		DeliveryAddress address = deliveryAddressMap.get(deliveryAddress.getIId());
		return address != null ? address.getCumulativePositions() : null;		
	}
	
	@Override
	public void setCumulativePositions(KundeDto addressDto,
			List<DelforCumulativePosition> positions) {
		DeliveryAddress address = deliveryAddressMap.get(addressDto.getIId());
		if(address == null) {
			address = deliveryAddressMap.put(addressDto.getIId(), new DeliveryAddress(addressDto));
		}
		address.setCumulativePositions(positions);
	}
	

	@Override
	public void push(DelforPosition position) {
		if(lastDelforPosition != null) {
			
		}
		lastDelforPosition = position;
		clearCumulativeInfos();
	}
	
	@Override
	public DelforPosition peek() {
		return lastDelforPosition;
	}
	
	@Override
	public void apply(KundeDto lieferkundeDto) {
		lastLieferkundeDto = lieferkundeDto;
	}

	@Override
	public void apply(BigDecimal quantity) {
		if(lastDelforPosition == null) {
			String msg = "Cannot set a quantity '" + quantity.toPlainString() + "' for undefined position";
			log.error(msg);
			return;
		}
		
		lastDelforPosition.setQuantity(quantity);
	}

	@Override
	public void apply(Date date) {
		if(lastDelforPosition == null) {
			String msg = "Cannot set a date '" + date.toString() + "' for undefined position";
			log.error(msg);
			return;
		}
		
		lastDelforPosition.setDate(date);
	}

	@Override
	public void applyOrderReference(String orderReference, String posReference) {
		if(lastDelforPosition == null) {
			String msg = "Can not set an orderreference '" + orderReference + "' for undefined position";
			log.error(msg);
			return;
		}
		String s = orderReference + 
				(Helper.isStringEmpty(posReference) ? "" : ("-" + posReference.trim()));
		lastDelforPosition.setOrderReference(s);
	}
	
	@Override
	public void applyBacklog(BigDecimal quantity) {
		if(lastDelforPosition == null) {
			String msg = "Cannot set a backlog quantity '" + quantity.toString() + "' for undefined position";
			log.error(msg);
			return;
		}
		
		lastDelforPosition.setBacklogQuantity(BigDecimal.ZERO.add(quantity));
	}
	
	@Override
	public void addForecastPosition() {
		if(lastDelforPosition == null) {
			String msg = "Cannot add forecast position, because DeliveryPosition is not set!";
			log.error(msg);
			return;
		}
		
		if(lastLieferkundeDto == null) {
			String msg = "Cannot add forecast position, because Lieferkunde is not set!";
			log.error(msg);
			return;			
		}
		
		addForecastPosition(lastLieferkundeDto, lastDelforPosition);
	}
	
	@Override
	public void addCommitForManufactoringPosition() {
		if(lastDelforPosition == null) {
			String msg = "Cannot add commitManufactoring position, because DeliveryPosition is not set!";
			log.error(msg);
			return;
		}
		
		if(lastLieferkundeDto == null) {
			String msg = "Cannot add commitManufactoring position, because Lieferkunde is not set!";
			log.error(msg);
			return;			
		}
		
		addCommitForManufactoringPosition(lastLieferkundeDto, lastDelforPosition);		
	}

	@Override
	public void addCommitForOrderPosition() {
		if(lastDelforPosition == null) {
			String msg = "Cannot add commitForOrder position, because DeliveryPosition is not set!";
			log.error(msg);
			return;
		}
		
		if(lastLieferkundeDto == null) {
			String msg = "Cannot add commitForOrder position, because Lieferkunde is not set!";
			log.error(msg);
			return;			
		}
		
		addCommitForOrderPosition(lastLieferkundeDto, lastDelforPosition);		
	}
	
	@Override
	public void applyCumulativeAmount(BigDecimal quantity) {
		this.lastCumulativeQuantity = quantity;
	}
	
	@Override
	public void applyCumulativeDate(Date date) {
		this.lastCumulativeDate = date;
	}
	
	@Override
	public void applyCumulativeStartDate(Date date) {
		this.lastCumulativeStartDate = date;
	}
	
	@Override
	public void applyCumulativeReference(String reference, String posReference) {
		this.lastCumulativeReference = reference +
				(Helper.isStringEmpty(posReference) ? "" : ("-" + posReference.trim()));
	}
	
	private boolean isCumulativeDateValid() {
		GregorianCalendar now = new GregorianCalendar();
		now.add(Calendar.DAY_OF_MONTH, 1);
		GregorianCalendar cumulativeDate = new GregorianCalendar();
		cumulativeDate.setTime(lastCumulativeDate);
		return cumulativeDate.compareTo(now) <= 0;	
	}
	
	@Override
	public void addCumulativePosition() {
		if(lastCumulativeQuantity == null) {
			String msg = "Cannot add cumulativePosition, because quantity is not set!";
			log.error(msg);
			return;			
		}
		
		if(lastCumulativeDate == null) {
			lastCumulativeDate = new Date();
			String msg = "Used '" + lastCumulativeDate + "' for cumulativePosition date, because date is not set!";
			log.warn(msg);
		}

		if(lastDelforPosition == null) {
			String msg = "Cannot add cumulativePosition, because DeliveryPosition is not set!";
			log.error(msg);
			return;
		}
		
		if(lastLieferkundeDto == null) {
			String msg = "Cannot add cumulativePosition, because Lieferkunde is not set!";
			log.error(msg);
			return;			
		}

		// SP6104 Auf gueltiges kumulatives Datum hinweisen (Zukunft, ...) 
		if(!isCumulativeDateValid()) {
			String msg = "Cumulative date '" + lastCumulativeDate + "' is in the future!";
			log.warn(msg);
			
			List<Object> clientInfo = new ArrayList<Object>();
			clientInfo.add(lastCumulativeDate.toString());
			clientInfo.add("");
			clientInfo.add(lastDelforPosition.getItemDto().getIId());
			clientInfo.add(lastCumulativeDate.getTime());
			clientInfo.add(lastCumulativeQuantity);
			
			addError(new EJBLineNumberExceptionLP(0, 
					EJBLineNumberExceptionLP.SEVERITY_WARNING, 
					new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FORECAST_IMPORT_LMFZ_UNGUELTIGES_DATUM, 
							clientInfo, null)));
		}

		DeliveryAddress address = deliveryAddressMap.get(lastLieferkundeDto.getIId()); 
		if(address == null) {
			address = new DeliveryAddress(lastLieferkundeDto);
			deliveryAddressMap.put(lastLieferkundeDto.getIId(), address);
		}
		
		address.addCumulativePosition(createCumulativePosition());
		clearCumulativeInfos(); 
	}
	
	@Override
	public boolean hasCumulativeInfo() {
		return lastCumulativeQuantity != null || backlogQuantity != null;
	}
	

	@Override
	public boolean hasQuantity() {
		if(lastDelforPosition == null) return false;
		return lastDelforPosition.getQuantity() != null;
	}
	
	public boolean hasItem(DelforPosition delforPosition, KundeDto deliveryAddress) {
		DeliveryAddress address = deliveryAddressMap.get(deliveryAddress.getIId());
		return address == null ? false : address.hasItem(delforPosition);
	}
	
	public void removeMine(DelforPosition template, KundeDto deliveryAddress) {
		DeliveryAddress address = deliveryAddressMap.get(deliveryAddress.getIId());
		address.removeMine(template);
	}
	
	private DelforCumulativePosition createCumulativePosition() {
		DelforCumulativePosition position = new DelforCumulativePosition();
		position.setItemDto(lastDelforPosition.getItemDto());

		position.setDate(new Date(lastCumulativeDate.getTime()));
		position.setQuantity(BigDecimal.ZERO.add(lastCumulativeQuantity));
		position.setReference("" + lastCumulativeReference);
		return position;
	}

	private void clearCumulativeInfos() {
		backlogQuantity = null;
		lastCumulativeQuantity = null;
		lastCumulativeReference = null;
		lastCumulativeStartDate = null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getControlReference() {
		return controlReference;
	}

	public void setControlReference(String controlReference) {
		this.controlReference = controlReference;
	}

	public class DeliveryAddress {
		private KundeDto addressDto;
		private List<DelforPosition> positions;
		private List<DelforCumulativePosition> cumulativePositions;
		private Map<String, List<DelforPosition>> posCache;
		
		public DeliveryAddress(KundeDto addressDto) {
			this.setAddressDto(addressDto);
			setPositions(new ArrayList<DelforPosition>());
			setCumulativePositions(new ArrayList<DelforCumulativePosition>());
		}

		public KundeDto getAddressDto() {
			return addressDto;
		}

		public void setAddressDto(KundeDto addressDto) {
			this.addressDto = addressDto;
		}

		public List<DelforPosition> getPositions() {
			return positions;
		}

		public void setPositions(List<DelforPosition> positions) {
			this.positions = positions;
		}
		
		public void addPosition(DelforPosition position) {
			positions.add(position);
			posCache = null;
		}

		public void addCumulativePosition(DelforCumulativePosition position) {
			cumulativePositions.add(position);
		}
		
		public List<DelforCumulativePosition> getCumulativePositions() {
			return cumulativePositions;
		}
		
		public void setCumulativePositions(List<DelforCumulativePosition> cumulativePositions) {
			this.cumulativePositions = cumulativePositions;
		}
	
		public void removeMine(DelforPosition template) {
			String templateKey = template.distinctKey();
			int index = 0;
			while(index < positions.size()) {
				if(templateKey.equals(positions.get(index).distinctKey())) {
					positions.remove(index);
				} else {
					++index;
				}
			}
		}
		
		public boolean hasItem(DelforPosition position) {
			if(posCache == null) {
				posCache = buildCache();
			}
			List<DelforPosition> p = posCache.get(position.distinctKey());
			if(p != null && p.size() > 0) {
				log.warn("Doppelte Forecast-Position: Artikel '" + 
						position.getItemDto().getCNr() + 
						"' mit Bestellnummer '" + position.getOrderReference() 
						+ "' (" + p.size() + " Eintraege vorhanden).");
			}
			return p != null && p.size() > 0;
		}
	
		private Map<String, List<DelforPosition>> buildCache() {
			Map<String, List<DelforPosition>> cache = new HashMap<String, List<DelforPosition>>();
			for (DelforPosition position : getPositions()) {
				String key = position.distinctKey();
				List<DelforPosition> p = cache.get(key);
				if(p == null) {
					p = new ArrayList<DelforPosition>();
					p.add(position);
					cache.put(key, p);
				} else {
					p.add(position);
				}
			}
			return cache;
		}	
	}
}
