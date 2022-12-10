package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.orders.DeliveryEntry;
import com.lp.service.edifact.orders.OrdersPosition;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.PiaInfo;
import com.lp.util.Helper;

public class OrdersRepository implements IOrdersRepository {
	private static final ILPLogger log = LPLogService.getInstance().getLogger(OrdersRepository.class); 

	private OrdersPosition lastPosition = null;
	private DeliveryEntry lastDeliveryEntry = null;
	private List<OrdersPosition> positions = new ArrayList<OrdersPosition>();
	private List<KundeDto> buyerParties = new ArrayList<KundeDto>();
	private List<NadInfo> buyerNadParties = new ArrayList<NadInfo>();	
	private String buyerIdentifier = null;
	private List<KundeDto> deliveryParties = new ArrayList<KundeDto>();
	private String deliveryIdentifier = null;
	private List<KundeDto> invoiceParties = new ArrayList<KundeDto>();
	private String invoiceIdentifier = null;
	private List<NadInfo>  deliveryNadParties = new ArrayList<NadInfo>();
	private List<NadInfo>  invoiceNadParties = new ArrayList<NadInfo>();
	private HvOptional<String> orderNumber = HvOptional.empty();
	private HvOptional<String> orderCurrency = HvOptional.empty();
	private HvOptional<String> transportationCode = HvOptional.empty();
	private HvOptional<String> buyerContactDepartmentCode = HvOptional.empty();
	private HvOptional<String> buyerContactDepartmentName = HvOptional.empty();
	private HvOptional<String> buyerCommunicationNumber = HvOptional.empty();
	private HvOptional<String> buyerCommunicationChannel = HvOptional.empty();
	private HvOptional<String> contractInfo = HvOptional.empty();
	
	@Override
	public HvOptional<Date> orderDate() {
		return HvOptional.empty();
	}

	@Override
	public HvOptional<String> issuerOrderNumber() {
		return orderNumber;
	}

	public HvOptional<String> orderCurrency() {
		return orderCurrency;
	}
	
	public HvOptional<String> transportationCode() {
		return transportationCode;
	}
	
	@Override
	public List<KundeDto> buyerAddress() {
		return buyerParties;
	}

	@Override
	public String buyerIdentifier() {
		return buyerIdentifier;
	}
	
	@Override
	public List<KundeDto> deliveryAddress() {
		return deliveryParties;
	}

	@Override
	public String deliveryIdentifier() {
		return deliveryIdentifier;
	}
	
	@Override
	public List<KundeDto> invoiceAddress() {
		return invoiceParties;
	}

	@Override
	public String invoiceIdentifier() {
		return invoiceIdentifier;
	}
	

	@Override
	public List<OrdersPosition> positions() {
		return positions;
	}
	
	@Override
	public void applyOrderDate(Date orderDate) {
	}

	@Override
	public void applyIssuerOrderNumber(String orderNumber) {
		if (!Helper.isStringEmpty(orderNumber)) {
			this.orderNumber = HvOptional.of(orderNumber.trim());
		}
	}

	@Override
	public void applyOrderCurrency(String codedCurrency) {
		if (!Helper.isStringEmpty(codedCurrency)) {
			this.orderCurrency = HvOptional.of(codedCurrency.trim());
		}		
	}
	
	@Override
	public void applyTransportation(String transportationCode) {
		if (!Helper.isStringEmpty(transportationCode)) {
			this.transportationCode = HvOptional.of(transportationCode.trim());
		}
	}
	
	@Override
	public void applyBuyerAddress(
			List<KundeDto> buyerAddress, String identifier) {
		buyerParties.addAll(buyerAddress);
		buyerIdentifier = identifier;
	}

	@Override
	public void applyBuyerNadAddress(NadInfo nadInfo) {
		buyerNadParties.add(nadInfo);
		buyerIdentifier = nadInfo.getPartyIdentifier();
	}
	
	@Override
	public void applyDeliveryAddress(
			List<KundeDto> deliveryAddress, String identifier) {
		deliveryParties.addAll(deliveryAddress);
		deliveryIdentifier = identifier;
	}

	@Override
	public void applyDeliveryNadAddress(NadInfo nadInfo) {
		deliveryNadParties.add(nadInfo);
		deliveryIdentifier = nadInfo.getPartyIdentifier();
	}
	
	@Override
	public void applyInvoiceAddress(
			List<KundeDto> invoiceAddress, String identifier) {
		invoiceParties.addAll(invoiceAddress);
		invoiceIdentifier = identifier;
	}
	
	@Override
	public void applyInvoiceNadAddress(NadInfo nadInfo) {
		invoiceNadParties.add(nadInfo);
		invoiceIdentifier = nadInfo.getPartyIdentifier();
	}
	
	/**
	 * Den Artikel der (neuen) Position setzen</br>
	 * <p>Sofort eine neue Position anlegen, da es in dem
	 * Sinne kein explizites Ende einer Position (LIN) gibt.</p>
	 * 
	 * @param artikelDto
	 */
	@Override
	public void apply(ArtikelDto artikelDto, LinInfo linInfo) {
		lastPosition = new OrdersPosition();
		lastPosition.setItemDto(artikelDto);
		lastPosition.setLinInfo(linInfo);
		positions.add(lastPosition);
		
		lastDeliveryEntry = null;
	}
	
	@Override
	public void applyLinInfo(LinInfo linInfo) {
		lastPosition = new OrdersPosition();
		lastPosition.setLinInfo(linInfo);
		positions.add(lastPosition);
		
		lastDeliveryEntry = null;
	}
	
	@Override
	public void applyPiaInfo(PiaInfo piaInfo) {
		if (lastPosition == null) {
			String msg = "Cannot set pia Info '" +
					piaInfo.getItemInfo(0).getItemIdentifier() + ":" +
					piaInfo.getItemInfo(0).getIdentificationCode() + "' without item!";
			log.error(msg);
			return;
		}
		lastPosition.addPiaInfo(piaInfo);
	}
	
	@Override
	public void applyImdInfo(ImdInfo imdInfo) {
		if (lastPosition == null) {
			String msg = "Cannot set imd Info '" +
					imdInfo.getDescriptionCode() + "' without item!";
			log.error(msg);
			return;
		}
		lastPosition.addImdInfo(imdInfo);
	}
	
	/**
	 * Die bestellte Menge
	 * 
	 * @param quantity
	 */
	@Override
	public void applyQuantity(BigDecimal quantity, String unit) {
		if (lastPosition == null) {
			String msg = "Cannot set total quantity '" + quantity.toPlainString() + "' without item!";
			log.error(msg);
			return;
		}
		lastPosition.setQuantity(quantity, unit);
	}
	
	@Override
	public void applyPrice(BigDecimal price) {
		if (lastPosition == null) {
			String msg = "Cannot set price '" + price.toPlainString() + "' without item!";
			log.error(msg);
			return;
		}
		lastPosition.setNetPrice(price);
	}
	
	@Override
	public void applyText(String positionText) {
		if (lastPosition == null) {
			String msg = "Cannot set freetext '" + positionText + "' without item!";
			log.error(msg);
			return;			
		}
		lastPosition.addText(positionText);
	}
	
	@Override
	public void apply(Date deliveryDate) {
		if (lastPosition == null) {
			String msg = "Cannot set delivery date '" + deliveryDate.toString() + "' without item!";
			log.error(msg);
			return;
		}
		lastPosition.setDate(deliveryDate);
	}
	
	@Override
	public void pushDeliveryPosition() {
		if (lastPosition == null) {
			String msg = "Cannot set delivery information without item!";
			log.error(msg);;
			return;
		}
		
		lastDeliveryEntry = new DeliveryEntry();
		lastPosition.addDeliveryEntry(lastDeliveryEntry);
	}
	

	@Override
	public boolean hasDeliveryPosition() {
		return lastDeliveryEntry != null;
	}
	
	@Override
	public void applyDeliveryQuantity(BigDecimal quantity) {
		if (lastDeliveryEntry == null) {
			String msg = "Cannot set delivery quantity '" + quantity.toPlainString() + " without item!";
			log.error(msg);
			return;
		}
		
		lastDeliveryEntry.setQuantity(quantity);
	}
	
	@Override
	public void applyDeliveryDate(Date deliveryDate) {
		if (lastDeliveryEntry == null) {
			String msg = "Cannot set delivery date '" + deliveryDate.toString() + " without item!";
			log.error(msg);
			return;
		}
		
		lastDeliveryEntry.setDate(deliveryDate);
	}

	@Override
	public void applyBuyerContact(String departmentCode, String departmentName) {
		if (!Helper.isStringEmpty(departmentCode)) {
			buyerContactDepartmentCode = HvOptional.of(departmentCode.trim());
		}
		if (!Helper.isStringEmpty(departmentName)) {
			buyerContactDepartmentName = HvOptional.of(departmentName.trim());
		}
	}
	
	@Override
	public void applyBuyerCommunication(String number, String channel) {
		if (!Helper.isStringEmpty(number)) {
			buyerCommunicationNumber = HvOptional.of(number.trim());
		}
		if (!Helper.isStringEmpty(channel)) {
			buyerCommunicationChannel = HvOptional.of(channel.trim());
		}
	}
	
	@Override
	public HvOptional<String> buyerContactDepartmentCode() {
		return buyerContactDepartmentCode;
	}
	
	@Override
	public HvOptional<String> buyerContactDepartmentName() {
		return buyerContactDepartmentName;
	}
	
	@Override
	public HvOptional<String> buyerCommunicationNumber() {
		return buyerCommunicationNumber;
	}
	
	@Override
	public HvOptional<String> buyerCommunicationChannel() {
		return buyerCommunicationChannel;
	}
	
	public List<NadInfo> getDeliveryNadParties() {
		return deliveryNadParties;
	}

	public void setDeliveryNadParties(List<NadInfo> deliveryNadParties) {
		this.deliveryNadParties = deliveryNadParties;
	}

	public List<NadInfo> getInvoiceNadParties() {
		return invoiceNadParties;
	}

	public void setInvoiceNadParties(List<NadInfo> invoiceNadParties) {
		this.invoiceNadParties = invoiceNadParties;
	}
	
	@Override
	public void applyContractInfo(String contractInfo) {
		if (Helper.isStringEmpty(contractInfo)) {
			this.contractInfo = HvOptional.empty();
		} else {
			this.contractInfo = HvOptional.of(Helper.emptyString(contractInfo));
		}
	}
	
	@Override
	public HvOptional<String> contractInfo() {
		return contractInfo;
	}
}
