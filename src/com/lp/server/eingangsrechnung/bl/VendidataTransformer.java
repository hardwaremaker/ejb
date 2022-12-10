/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.eingangsrechnung.service.PaymentInfoCustomerSettled;
import com.lp.server.eingangsrechnung.service.PaymentInfoVVM;
import com.lp.server.eingangsrechnung.service.PaymentInfoVVMFormula;
import com.lp.server.eingangsrechnung.service.PaymentInfoVVMSettled;
import com.lp.server.eingangsrechnung.service.VendidataDiscountPayment;
import com.lp.server.eingangsrechnung.service.VendidataPaymentInfo;
import com.lp.server.eingangsrechnung.service.VendidataDiscountPayment.Type;
import com.lp.server.eingangsrechnung.service.VendidataPaymentInfo.SettledPaymentType;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.schema.vendidata.discounts.XMLCustomerPaymentInfo;
import com.lp.server.schema.vendidata.discounts.XMLCustomerSettledPaymentInfo;
import com.lp.server.schema.vendidata.discounts.XMLDiscountPayment;
import com.lp.server.schema.vendidata.discounts.XMLFormulaPaymentInfo;
import com.lp.server.schema.vendidata.discounts.XMLPaymentPeriod;
import com.lp.server.schema.vendidata.discounts.XMLSettledPaymentInfo;
import com.lp.server.schema.vendidata.discounts.XMLSettledPaymentType;
import com.lp.server.schema.vendidata.discounts.XMLVirtualVendingmachinePaymentInfo;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataImportExceptionLP;
import com.lp.util.Helper;
import com.lp.util.VendidataXMLFieldInstantiator;

public class VendidataTransformer {
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private VendidataXmlUnmarshaller unmarshaller;
	private XMLDiscountPayment xmlHead;
	private ParseNoneEmptyString stringParser;

	public VendidataTransformer() {
		unmarshaller = new VendidataXmlUnmarshaller();
		xmlHead = new XMLDiscountPayment();
		stringParser = new ParseNoneEmptyString();
	}

	public List<VendidataDiscountPayment> transform(String xmlDaten) throws EJBExceptionLP {
		try {
			xmlHead = unmarshaller.unmarshal(xmlDaten);
		} catch (JAXBException e) {
			myLogger.error("JAXBException", e);
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_UNMARSHAL_FEHLGESCHLAGEN, e);
		} catch (SAXException e) {
			myLogger.error("SAXException", e);
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_UNMARSHAL_FEHLGESCHLAGEN, e);
		} 
		
		try {
			VendidataXMLFieldInstantiator<XMLDiscountPayment> fieldInstantiator = new VendidataXMLFieldInstantiator<XMLDiscountPayment>();
			xmlHead = fieldInstantiator.instantiateNullFields(xmlHead);
		} catch (Exception e) {
			myLogger.error("Exception", e);
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_FIELD_INSTANZIERUNG_FEHLGESCHLAGEN, e);
		}			

		try {
			return transformImpl();
		} catch (Exception e) {
			myLogger.error("Exception", e);
			throw new EJBVendidataImportExceptionLP(EJBVendidataImportExceptionLP.SEVERITY_ERROR, 
					EJBExceptionLP.FEHLER_ER_IMPORT_XML_KLASSEN_TRANSFORMATION, e);
		}			

	}

	private List<VendidataDiscountPayment> transformImpl() {
		List<VendidataDiscountPayment> list = new ArrayList<VendidataDiscountPayment>();
		
		for (XMLCustomerPaymentInfo info : xmlHead.getCustomerPaymentInfo()) {
			List<VendidataPaymentInfo> positionen = new ArrayList<VendidataPaymentInfo>();
			positionen.addAll(transformVirtualVendingmachinePaymentInfo(info.getVirtualVendingmachinePaymentInfo()));
			positionen.addAll(transformCustomerSettledPaymentInfo(info.getSettledPaymentInfo()));
			
			createDiscountPayments(info, list, positionen);
		}
		
		return list;
	}
	
	private void createDiscountPayments(XMLCustomerPaymentInfo info, List<VendidataDiscountPayment> list, List<VendidataPaymentInfo> positionen) {
		VendidataDiscountPayment itemPos = createDefaultDiscountPaymentItem(info);
		itemPos.setType(Type.AUSGANGSGUTSCHRIFT);
		VendidataDiscountPayment itemNeg = createDefaultDiscountPaymentItem(info);
		itemNeg.setType(Type.RECHNUNG);
		
		for (VendidataPaymentInfo pos : positionen) {
			if (pos.getBetrag() == null || pos.getBetrag().signum() == 0) continue;
			if (pos.getBetrag().signum() > 0) {
				itemPos.addPosition(pos);
			} else {
				pos.setBetrag(pos.getBetrag().abs());
				itemNeg.addPosition(pos);
			}
		}
		
		if (!itemPos.isEmpty()) 
			list.add(itemPos);
		
		if (!itemNeg.isEmpty()) 
			list.add(itemNeg);
	}

	private Date createBelegdatum(XMLPaymentPeriod discountPeriod) {
		Calendar cal = new GregorianCalendar();
		cal.set(discountPeriod.getPeriodYear(), discountPeriod.getPeriodNr() - 1, 1);
		cal.add(Calendar.DATE, -1);
		
		return new Date(cal.getTimeInMillis());
	}

	private List<VendidataPaymentInfo> transformVirtualVendingmachinePaymentInfo(List<XMLVirtualVendingmachinePaymentInfo> infoList) {
		List<VendidataPaymentInfo> list = new ArrayList<VendidataPaymentInfo>();
		
		for (XMLVirtualVendingmachinePaymentInfo vvpInfo : infoList) {
			list.addAll(transformFormulaPaymentInfo(vvpInfo));
			list.addAll(transformSettledPaymentInfo(vvpInfo));
		}
		return list;
	}

	private List<VendidataPaymentInfo> transformSettledPaymentInfo(XMLVirtualVendingmachinePaymentInfo vvpInfo) {
		List<VendidataPaymentInfo> list = new ArrayList<VendidataPaymentInfo>();
		
		for (XMLSettledPaymentInfo cspInfo : vvpInfo.getSettledPaymentInfo()) {
			PaymentInfoVVMSettled item = new PaymentInfoVVMSettled(getSettledPaymentType(cspInfo.getPaymentType()));
			item.setBetrag(Helper.rundeKaufmaennisch(new BigDecimal(cspInfo.getTotal().getValue()), 
					FinanzFac.NACHKOMMASTELLEN));
			item.setPeriodenJahr(Integer.toString(cspInfo.getPaymentPeriod().getPeriodYear()));
			item.setPeriodenNummer(Integer.toString(cspInfo.getPaymentPeriod().getPeriodNr()));
			if (cspInfo.getPeriodType() != null) {
				item.setPeriodenTyp(cspInfo.getPeriodType().value());
			}
			setVVPInfo(item, vvpInfo);
			if (!Helper.isStringEmpty(cspInfo.getPaymentRemark()))
				item.setUebersteuerteBezeichnung(cspInfo.getPaymentRemark());
			
			list.add(item);
		}
		
		return list;
	}

	private List<VendidataPaymentInfo> transformFormulaPaymentInfo(XMLVirtualVendingmachinePaymentInfo vvpInfo) {
		List<VendidataPaymentInfo> list = new ArrayList<VendidataPaymentInfo>();
		
		for (XMLFormulaPaymentInfo formulaInfo : vvpInfo.getFormulaPaymentInfo()) {
			PaymentInfoVVMFormula item = new PaymentInfoVVMFormula();
			item.setBetrag(Helper.rundeKaufmaennisch(new BigDecimal(formulaInfo.getTotal().getValue()), 
					FinanzFac.NACHKOMMASTELLEN));
			item.setFormelMitWerten(stringParser.parse(formulaInfo
					.getDiscountFormulaDetails().getLocalizedFormulaDetail().get(0)
					.getFormulaWithValues(), "formulaWithValues"));
			item.setPeriodenJahr(Integer.toString(formulaInfo.getPaymentPeriod().getPeriodYear()));
			item.setPeriodenNummer(Integer.toString(formulaInfo.getPaymentPeriod().getPeriodNr()));
			if (formulaInfo.getPeriodType() != null) {
				item.setPeriodenTyp(formulaInfo.getPeriodType().value());
			}
			setVVPInfo(item, vvpInfo);
			if (!Helper.isStringEmpty(formulaInfo.getPaymentRemark()))
				item.setUebersteuerteBezeichnung(formulaInfo.getPaymentRemark());
			
			list.add(item);
		}
		return list;
	}

	private void setVVPInfo(PaymentInfoVVM item, XMLVirtualVendingmachinePaymentInfo vvpInfo) {
		item.setVvmInfoText(vvpInfo.getVirtualVendingmachineInfoText());
		item.setUebersteuerteBezeichnung(vvpInfo.getVirtualVendingmachine().getAdditionalFields().getCode3().getValue());
		item.setSteuersatz(vvpInfo.getVirtualVendingmachine().getAdditionalFields().getCode6().getValue());
	}
	
	private List<VendidataPaymentInfo> transformCustomerSettledPaymentInfo(List<XMLCustomerSettledPaymentInfo> infoList) {
		List<VendidataPaymentInfo> list = new ArrayList<VendidataPaymentInfo>();
		
		for (XMLCustomerSettledPaymentInfo cspInfo : infoList) {
			PaymentInfoCustomerSettled item = new PaymentInfoCustomerSettled(getSettledPaymentType(cspInfo.getPaymentType()));
			item.setBetrag(Helper.rundeKaufmaennisch(new BigDecimal(cspInfo.getTotal().getValue()), 
					FinanzFac.NACHKOMMASTELLEN));
			item.setPeriodenJahr(Integer.toString(cspInfo.getPaymentPeriod().getPeriodYear()));
			item.setPeriodenNummer(Integer.toString(cspInfo.getPaymentPeriod().getPeriodNr()));
			if (cspInfo.getPeriodType() != null) {
				item.setPeriodenTyp(cspInfo.getPeriodType().value());
			}
			if (!Helper.isStringEmpty(cspInfo.getPaymentRemark()))
				item.setUebersteuerteBezeichnung(cspInfo.getPaymentRemark());

			list.add(item);
		}
		
		return list;
	}

	private SettledPaymentType getSettledPaymentType(XMLSettledPaymentType paymentType) {
		if (XMLSettledPaymentType.SINGLE_PAYMENT.equals(paymentType)) {
			return VendidataPaymentInfo.SettledPaymentType.SINGLE_PAYMENT;
		} else if (XMLSettledPaymentType.FLATCHARGE_PAYMENT.equals(paymentType)) {
			return VendidataPaymentInfo.SettledPaymentType.FLATCHARGE_PAYMENT;
		} else {
			return VendidataPaymentInfo.SettledPaymentType.PAYMENT;
		}
	}

	private VendidataDiscountPayment createDefaultDiscountPaymentItem(XMLCustomerPaymentInfo info) {
		VendidataDiscountPayment item = new VendidataDiscountPayment();
		item.setBelegdatum(createBelegdatum(xmlHead.getDiscountPeriod()));
		item.setKundennummer(Integer.toString(info.getBeneficiaryCustomer().getCustomerId()));
		item.setDebitorennummer(Integer.toString(info.getBeneficiaryCustomer().getFinancialAccountingId()));
		item.setBezeichnungKunde(stringParser.parse(info.getBeneficiaryCustomerInfoText(), "beneficiaryCustomerInfoText"));
		return item;
	}
}
