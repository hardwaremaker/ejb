package com.lp.server.system.ejbfac;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.lp.server.util.HvOptional;
import com.lp.service.BelegpositionDto;
import com.lp.service.edifact.orders.DeliveryEntry;
import com.lp.service.edifact.orders.OrdersPosition;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.util.Helper;

public class EdifactOrderResponseProducer {
	private DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") ;
	
	public BelegpositionDto setPositionReference(
			BelegpositionDto positionDto, OrdersPosition ediPos) {
		LinInfo linInfo = ediPos.getLinInfo().get();

		positionDto.setXTextinhalt(
				"{\"lineitemid\" : \"" +
				linInfo.getLineItemIdentifier() + "\",\n" +
				"\"lineitemcnr\" : \"" + orderItemInfo(linInfo) + "\",\n" +
				"\"totalquantity\" : " + 
				ediPos.getQuantity().toPlainString() + "}") ;
		return positionDto;
	}

	public BelegpositionDto setPositionReference(
			BelegpositionDto positionDto,
			OrdersPosition ediPos, DeliveryEntry deliveryEntry) {
		LinInfo linInfo = ediPos.getLinInfo().get();
		positionDto.setXTextinhalt(
				"{\"lineitemid\" : \"" +
				ediPos.getLinInfo().get().getLineItemIdentifier() + "\",\n" +
				"\"lineitemcnr\" : \"" + orderItemInfo(linInfo) + "\",\n" +
				"\"totalquantity\" : " + 
				ediPos.getQuantity().toPlainString() + ",\n" +
				"\"sccquantity\" : " +
				deliveryEntry.getQuantity() + ",\n" +
				"\"sccdate\" : \"" + isoFormat.format(deliveryEntry.getDate()) + "\"" + 
				"}") ;
		return positionDto;
	}
	
	public String getOrderItemInfo(BelegpositionDto positionDto) {
		if (Helper.isStringEmpty(positionDto.getXTextinhalt())) return "";
		String text = unjasper(positionDto.getXTextinhalt());
	
		String startToken = "\"lineitemcnr\" : \"";
		int start = text.indexOf(startToken);
		if (start == -1) return "";
		
		int stop = text.indexOf("\",", start);
		if (stop == -1) return "";
		
		return text.substring(start + startToken.length(), stop);
	}
	
	public boolean isSplitPosition(BelegpositionDto positionDto) {
		String text = positionDto.getXTextinhalt();
		if (Helper.isStringEmpty(text)) return false;
		text = unjasper(text);
		
		String startToken = "\"sccquantity\" : ";
		int startIndex = text.indexOf(startToken);
		return startIndex != -1;
	}
	
	public HvOptional<String> getLineItemId(BelegpositionDto positionDto) {
		String text = positionDto.getXTextinhalt();
		if (Helper.isStringEmpty(text)) return HvOptional.empty();
		text = unjasper(text);
		
		String startToken = "\"lineitemid\" : \"";
		int startIndex = text.indexOf(startToken);
		if (startIndex == -1) return HvOptional.empty();
		startIndex += startToken.length();
		
		int endIndex = text.indexOf("\",", startIndex);
		if (endIndex == -1) return HvOptional.empty();
		
		return HvOptional.of(text.substring(startIndex, endIndex));
	}
	
	private String unjasper(String content) {
		return content.replaceAll("&quot;", "\"");
	}
	
	private String orderItemInfo(LinInfo linInfo) {
		String orderItemInfo = linInfo.getItemIdentifier() 
				+ ":" + linInfo.getItemTypeIdentificationCode()
				+ ":" + linInfo.getIdentificationCode() 
				+ ":" + linInfo.getResponsibleAgencyCode();
		return orderItemInfo;
	}
}
