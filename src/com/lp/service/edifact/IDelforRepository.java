package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.EJBLineNumberExceptionLP;

public interface IDelforRepository {
	
	CallOffXlsImporterResult getResult();
	void addError(EJBLineNumberExceptionLP errorInfo);
	
	void setBuyer(KundeDto kundeDto);
	void addHeaderFreeText(String freetext);
	
	void addDeliveryAddress(KundeDto lieferkundeDto);
	void addForecastPosition(KundeDto addressDto, DelforPosition position);
	void addCommitForOrderPosition(KundeDto addressDto, DelforPosition position);
	void addCommitForManufactoringPosition(KundeDto addressDto, DelforPosition position);

	void push(DelforPosition position);
	void apply(KundeDto lieferkundeDto);
	void apply(BigDecimal quantity);
	void apply(Date date);
	void applyOrderReference(String orderReference, String posReference);
	void applyBacklog(BigDecimal quantity);
	
	void addForecastPosition();
	void addCommitForOrderPosition();
	void addCommitForManufactoringPosition();

	void applyCumulativeDate(Date date);
	void applyCumulativeStartDate(Date date);
	void applyCumulativeAmount(BigDecimal quantity);
	void applyCumulativeReference(String reference, String posReference);
	void addCumulativePosition();
	boolean hasCumulativeInfo();

	boolean hasQuantity();
	
	DelforPosition peek() ;
	
	KundeDto getBuyer();
	List<String> getHeaderFreeTexts();
	List<KundeDto> getDeliveryAddresses();
	void setDeliveryAddresses(List<KundeDto> deliveryAddresses);

	List<DelforPosition> getPositions(KundeDto deliveryAddress); 
	void setPositions(KundeDto deliveryAddress, List<DelforPosition> positions);
	
	List<DelforCumulativePosition> getCumulativePositions(
			KundeDto deliveryAddress);
	void setCumulativePositions(KundeDto addressDto,
			List<DelforCumulativePosition> positions);
}
