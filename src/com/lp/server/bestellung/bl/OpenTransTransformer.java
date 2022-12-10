package com.lp.server.bestellung.bl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.bestellung.service.OpenTransOrder;
import com.lp.server.bestellung.service.OpenTransOrderHead;
import com.lp.server.bestellung.service.OpenTransOrderPosition;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.schema.opentrans_1_0.OpenTransOrderMarshaller;
import com.lp.server.schema.opentrans_1_0.base.XmlOtADDRESS;
import com.lp.server.schema.opentrans_1_0.base.XmlOtARTICLEID;
import com.lp.server.schema.opentrans_1_0.base.XmlOtARTICLEPRICE;
import com.lp.server.schema.opentrans_1_0.base.XmlOtBUYERAID;
import com.lp.server.schema.opentrans_1_0.base.XmlOtBUYERPARTY;
import com.lp.server.schema.opentrans_1_0.base.XmlOtCONTACT;
import com.lp.server.schema.opentrans_1_0.base.XmlOtDELIVERYDATE;
import com.lp.server.schema.opentrans_1_0.base.XmlOtDELIVERYPARTY;
import com.lp.server.schema.opentrans_1_0.base.XmlOtDtCURRENCIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtINVOICEPARTY;
import com.lp.server.schema.opentrans_1_0.base.XmlOtORDERPARTIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtPARTY;
import com.lp.server.schema.opentrans_1_0.base.XmlOtPARTYID;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSHIPMENTPARTIES;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSUPPLIERPARTY;
import com.lp.server.schema.opentrans_1_0.order.ObjectFactory;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDER;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDERHEADER;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDERINFO;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDERITEM;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDERITEMLIST;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDERSUMMARY;
import com.lp.server.system.service.LandplzortDto;
import com.lp.util.Helper;

public class OpenTransTransformer {
	private ObjectFactory orderFactory;
	private com.lp.server.schema.opentrans_1_0.base.ObjectFactory baseFactory;
	private SimpleDateFormat dateFormatter;

	private ObjectFactory getOrderFactory() {
		if (orderFactory == null) {
			orderFactory = new ObjectFactory();
		}
		return orderFactory;
	}
	
	private com.lp.server.schema.opentrans_1_0.base.ObjectFactory getBaseFactory() {
		if (baseFactory == null) {
			baseFactory = new com.lp.server.schema.opentrans_1_0.base.ObjectFactory();
		}
		return baseFactory;
	}
	
	private SimpleDateFormat getDateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		}
		return dateFormatter;
	}
	
	public String orderXmlString(OpenTransOrder otOrder) throws JAXBException, SAXException {
		XmlOtORDER xmlOrder = orderXml(otOrder);
		OpenTransOrderMarshaller marshaller = new OpenTransOrderMarshaller();
		return marshaller.marshal(xmlOrder);
	}

	public XmlOtORDER orderXml(OpenTransOrder otOrder) {
		return transformImpl(otOrder);
	}
	
	private XmlOtORDER transformImpl(OpenTransOrder otOrder) {
		XmlOtORDER xmlOrder = getOrderFactory().createORDER();
		if (otOrder.getHead() == null) return xmlOrder;
		
		transformHead(xmlOrder, otOrder.getHead());
		transformPositions(xmlOrder, otOrder.getPositions());
		transformSummary(xmlOrder, otOrder);
		
		return xmlOrder;
	}

	private void transformSummary(XmlOtORDER xmlOrder, OpenTransOrder order) {
		XmlOtORDERSUMMARY xmlOrderSummary = getOrderFactory().createORDERSUMMARY();
		xmlOrderSummary.setTOTALITEMNUM(BigInteger.valueOf(order.getNumberOfPositions()));
		//xmlOrderSummary.setTOTALAMOUNT(value);
		xmlOrder.setORDERSUMMARY(xmlOrderSummary);
	}

	private void transformPositions(XmlOtORDER xmlOrder, List<OpenTransOrderPosition> positions) {
		XmlOtORDERITEMLIST xmlItemList = getOrderFactory().createORDERITEMLIST();
		for (OpenTransOrderPosition pos : positions) {
			addOrderItem(xmlItemList.getORDERITEM(), pos);
		}
		xmlOrder.setORDERITEMLIST(xmlItemList);
	}

	private void addOrderItem(List<XmlOtORDERITEM> orderitems, OpenTransOrderPosition pos) {
		XmlOtORDERITEM xmlItem = getOrderFactory().createORDERITEM();
		xmlItem.setLINEITEMID(pos.getLineItemId());
		xmlItem.setQUANTITY(pos.getQuantity());
		xmlItem.setORDERUNIT(pos.getOrderUnit() != null ? pos.getOrderUnit().getCode() : null);
		xmlItem.setARTICLEID(createArticleId(pos));
		xmlItem.setARTICLEPRICE(createArticlePrice(pos));
		// TODO xmlItem.setDELIVERYDATE(createDeliveryDate(pos));
		
		orderitems.add(xmlItem);
	}

	private XmlOtDELIVERYDATE createDeliveryDate(OpenTransOrderPosition pos) {
		if (pos.getDeliveryDate() == null) return null;
		
		XmlOtDELIVERYDATE xmlDeliveryDate = getBaseFactory().createDELIVERYDATE();
		return xmlDeliveryDate;
	}

	private XmlOtARTICLEPRICE createArticlePrice(OpenTransOrderPosition pos) {
		XmlOtARTICLEPRICE xmlArticlePrice = getBaseFactory().createARTICLEPRICE();
		xmlArticlePrice.setType("net_customer");
		xmlArticlePrice.setPRICEAMOUNT(pos.getAmount());
		xmlArticlePrice.setPRICELINEAMOUNT(pos.getLineAmount());
		
		return xmlArticlePrice;
	}

	private XmlOtARTICLEID createArticleId(OpenTransOrderPosition pos) {
		XmlOtARTICLEID xmlArticleId = getBaseFactory().createARTICLEID();
		xmlArticleId.setSUPPLIERAID(pos.getSupplierItemNumber());
		XmlOtBUYERAID xmlBuyerAId = getBaseFactory().createBUYERAID();
		xmlBuyerAId.setType("meerkat");
		xmlBuyerAId.setValue(pos.getItemNumber());
		xmlArticleId.getBUYERAID().add(xmlBuyerAId);
		xmlArticleId.setDESCRIPTIONSHORT(pos.getDescriptionShort());
		
		return xmlArticleId;
	}

	private void transformHead(XmlOtORDER xmlOrder, OpenTransOrderHead head) {
		XmlOtORDERHEADER xmlOrderHeader = getOrderFactory().createORDERHEADER();
		addOrderInfo(xmlOrderHeader, head);
		
		xmlOrder.setORDERHEADER(xmlOrderHeader);
	}

	private void addOrderInfo(XmlOtORDERHEADER xmlOrderHeader, OpenTransOrderHead head) {
		XmlOtORDERINFO xmlOrderInfo = getOrderFactory().createORDERINFO();
		xmlOrderInfo.setORDERID(head.getOrderNumber());
		xmlOrderInfo.setORDERDATE(getDateFormatter().format(head.getOrderDate()));
		xmlOrderInfo.setPRICECURRENCY(XmlOtDtCURRENCIES.fromValue(head.getCurrency()));
		xmlOrderInfo.setTERMSANDCONDITIONS(head.getTermsAndConditions());
		addOrderParties(xmlOrderInfo, head);
		
		xmlOrderHeader.setORDERINFO(xmlOrderInfo);
	}

	private void addOrderParties(XmlOtORDERINFO xmlOrderInfo, OpenTransOrderHead head) {
		XmlOtORDERPARTIES xmlOrderParties = getBaseFactory().createORDERPARTIES();

		XmlOtBUYERPARTY xmlBuyerParty = getBaseFactory().createBUYERPARTY();
		xmlBuyerParty.setPARTY(createSupplierSpecificParty(head.getBuyerAddress(), head.getBuyerSupplierId()));
		xmlOrderParties.setBUYERPARTY(xmlBuyerParty);
		addContact(xmlBuyerParty.getPARTY(), head.getBuyerContact());
		
		XmlOtSUPPLIERPARTY xmlSupplierParty = getBaseFactory().createSUPPLIERPARTY();
		xmlSupplierParty.setPARTY(createBuyerSpecificParty(head.getSupplier(), head.getSupplierId()));
		xmlOrderParties.setSUPPLIERPARTY(xmlSupplierParty);
		
		if (head.getInvoiceAddress() != null) {
			XmlOtINVOICEPARTY xmlInvoiceParty = getBaseFactory().createINVOICEPARTY();
			xmlInvoiceParty.setPARTY(createSupplierSpecificParty(head.getInvoiceAddress(), head.getBuyerSupplierId()));
			xmlOrderParties.setINVOICEPARTY(xmlInvoiceParty);
		}

		addShipmentParties(xmlOrderParties, head);
		xmlOrderInfo.setORDERPARTIES(xmlOrderParties);
	}

	private void addShipmentParties(XmlOtORDERPARTIES xmlOrderParties, OpenTransOrderHead head) {
		if (head.getDeliveryAddress() == null) return;
		
		XmlOtDELIVERYPARTY xmlDeliveryParty = getBaseFactory().createDELIVERYPARTY();
		xmlDeliveryParty.setPARTY(createParty(head.getDeliveryAddress()));
		XmlOtSHIPMENTPARTIES xmlShipmentParties = getBaseFactory().createSHIPMENTPARTIES();
		xmlShipmentParties.setDELIVERYPARTY(xmlDeliveryParty);
		
		xmlOrderParties.setSHIPMENTPARTIES(xmlShipmentParties);
	}

	private XmlOtPARTY createBuyerSpecificParty(PartnerDto partner, String id) {
		if (!Helper.isStringEmpty(id)) {
			XmlOtPARTYID xmlPartyId = getBaseFactory().createPARTYID();
			xmlPartyId.setType("buyer_specific");
			xmlPartyId.setValue(id);
			return createParty(partner, xmlPartyId);
		}
		return createParty(partner);
	}

	private XmlOtPARTY createSupplierSpecificParty(PartnerDto partner, String id) {
		if (!Helper.isStringEmpty(id)) {
			XmlOtPARTYID xmlPartyId = getBaseFactory().createPARTYID();
			xmlPartyId.setType("supplier_specific");
			xmlPartyId.setValue(id);
			return createParty(partner, xmlPartyId);
		}
		
		return createParty(partner);
	}
	
	private XmlOtPARTY createParty(PartnerDto partner) {
		XmlOtPARTY xmlParty = getBaseFactory().createPARTY();
		xmlParty.setADDRESS(createAddress(partner));
		return xmlParty;
	}

	private XmlOtPARTY createParty(PartnerDto partner, XmlOtPARTYID xmlPartyId) {
		XmlOtPARTY xmlParty = createParty(partner);
		xmlParty.setPARTYID(xmlPartyId);
		return xmlParty;
	}

	private XmlOtADDRESS createAddress(PartnerDto partner) {
		XmlOtADDRESS xmlAddress = getBaseFactory().createADDRESS();
		xmlAddress.setNAME(partner.getCName1nachnamefirmazeile1());
		xmlAddress.setSTREET(partner.getCStrasse());
		LandplzortDto landplzortDto = partner.getLandplzortDto();
		if (landplzortDto != null) {
			xmlAddress.setZIP(landplzortDto.getCPlz());
			xmlAddress.setCITY(landplzortDto.getOrtDto() != null ? landplzortDto.getOrtDto().getCName() : null);
			xmlAddress.setCOUNTRY(landplzortDto.getLandDto() != null ? landplzortDto.getLandDto().getCLkz() : null);
		}
		xmlAddress.setVATID(partner.getCUid());
		xmlAddress.setEMAIL(partner.getCEmail());
		return xmlAddress;
	}
	
	private void addContact(XmlOtPARTY xmlParty, PartnerDto partner) {
		if (xmlParty.getADDRESS() == null)
			return;
		
		addContact(xmlParty.getADDRESS(), partner);
	}
	
	private void addContact(XmlOtADDRESS xmlAddress, PartnerDto partner) {
		if (partner == null)
			return;
		
		XmlOtCONTACT xmlContact = getBaseFactory().createCONTACT();
		xmlContact.setCONTACTNAME(partner.formatFixName2Name1());
		xmlContact.setEMAIL(partner.getCEmail());
		
		xmlAddress.getCONTACT().add(xmlContact);
	}
}
