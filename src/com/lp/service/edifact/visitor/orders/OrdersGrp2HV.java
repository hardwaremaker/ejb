package com.lp.service.edifact.visitor.orders;

import java.util.List;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.KennungType;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.IOrdersEjbService;
import com.lp.service.edifact.IOrdersRepository;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.CuxSegment;
import com.lp.service.edifact.parser.NadSegment;
import com.lp.service.edifact.parser.TodSegment;
import com.lp.service.edifact.schema.ComInfo;
import com.lp.service.edifact.schema.CtaInfo;
import com.lp.service.edifact.schema.CuxInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.TodInfo;
import com.lp.service.edifact.visitor.LoggingVisitorIgnored;

public class OrdersGrp2HV extends LoggingVisitorIgnored {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(OrdersGrp2HV.class); 

	private final IOrdersEjbService ejbService;
	private final IOrdersRepository repository;
	
	public OrdersGrp2HV(IOrdersEjbService ordersEjbService, IOrdersRepository ordersRepository) {
		this.ejbService = ordersEjbService;
		this.repository = ordersRepository;
	}
	
	public IOrdersRepository repository() {
		return this.repository;
	}
	
	@Override
	public void visit(CuxSegment cuxSegment) {
		CuxInfo cuxInfo = cuxSegment.get();
		log.info("visit cux " + cuxInfo.getDetailsQualifier() + 
				":'" + cuxInfo.getCodedCurrency() + "'.");
		if ("2".equals(cuxInfo.getDetailsQualifier())) {
			repository().applyOrderCurrency(cuxInfo.getCodedCurrency());
			return;
		}
		log.warn("Ignored cux "  + cuxInfo.getDetailsQualifier() + 
				":'" + cuxInfo.getCodedCurrency() + "'." );
	}
	
	@Override
	public void visit(NadSegment nadSegment) {
		log.info("visit nad " + nadSegment.get().getPartyFunctionCode() + ":" + nadSegment.get().getPartyIdentifier());
		
		findKunde(nadSegment);
	}
	
	private void findKunde(NadSegment nadSegment) {
		NadInfo nadInfo = nadSegment.get();
		if("SE".equals(nadInfo.getPartyFunctionCode())) {
			log.info("ignored 'SE' NAD Segment");
			return;
		}
		
		if ("BY".equals(nadInfo.getPartyFunctionCode())) {
			List<KundeDto> kundeDtos = ejbService.kundeFindByIdentification(
					KennungType.OrdersNadBuyer, nadInfo.getPartyIdentifier());
			if(kundeDtos.size() > 0) {
				log.info("  found " + kundeDtos.size() + 
						" possible Order-Parties by identification '" +
						nadInfo.getPartyIdentifier());
				repository.applyBuyerAddress(kundeDtos, nadInfo.getPartyIdentifier());
				return;
			} else {
				repository.applyBuyerNadAddress(nadInfo);
			}
			
/*			
			kundeDto = ejbService.kundeFindByAddress(
					nadInfo.getName(0), nadInfo.getStreet(0),
					nadInfo.getCityName(), nadInfo.getPostalIdentifcationCode(), nadInfo.getCountryIndentificationCode());
			if(kundeDto != null) {
				log.info("  found KundeDto.IId(" + kundeDto.getIId() + ") by address.");
				repository.applyBuyerAddress(kundeDto);
				return;
			}
*/
		}

		if ("DP".equals(nadInfo.getPartyFunctionCode())) {
			List<KundeDto> kundeDtos = ejbService.kundeFindByIdentification(
					KennungType.OrdersNadDelivery, nadInfo.getPartyIdentifier());
			if(kundeDtos.size() > 0) {
				log.info("  found " + kundeDtos.size() + 
						" possible Delivery-Parties by identification '" +
						nadInfo.getPartyIdentifier());
				repository.applyDeliveryAddress(kundeDtos, nadInfo.getPartyIdentifier());
				return;
			} else {
				repository.applyDeliveryNadAddress(nadInfo);
			}
		}
		
		if ("IV".equals(nadInfo.getPartyFunctionCode())) {
			List<KundeDto> kundeDtos = ejbService.kundeFindByIdentification(
					KennungType.OrdersNadInvoice, nadInfo.getPartyIdentifier());
			if(kundeDtos.size() > 0) {
				log.info("  found " + kundeDtos.size() + 
						" possible Invoice-Parties by identification '" +
						nadInfo.getPartyIdentifier());
				repository.applyInvoiceAddress(kundeDtos, nadInfo.getPartyIdentifier());
				return;
			} else {
				repository.applyInvoiceNadAddress(nadInfo);
			}
		}
		
		log.error("Could not find KundeDto for FunctionCode '" + 
				nadInfo.getPartyFunctionCode() + "' and Identification '" + nadInfo.getPartyIdentifier() + "'.");
	}
	
	@Override
	public void visit(TodSegment todSegment) {
		TodInfo todInfo = todSegment.get();
		log.info("visit tod " + todInfo.getFunctionCode() + " (" + todInfo.getTransportationCode() + ")");
		if ("6".equals(todInfo.getFunctionCode())) {
			repository.applyTransportation(todInfo.getTransportationCode());
			return;
		}
		
		log.error("Could not interpret todSegment, unknown FunctionCode");
	}
	
	@Override
	public void visit(CtaSegment ctaSegment) {
		CtaInfo ctaInfo = ctaSegment.get();
		log.info("visit cta " + ctaInfo.getFunctionCode() + "(" +
				ctaInfo.getDepartmentCode() + ":" + ctaInfo.getDepartmentName() + ")");
		if ("PD".equals(ctaInfo.getFunctionCode())) {
			repository.applyBuyerContact(ctaInfo.getDepartmentCode(), ctaInfo.getDepartmentName());
			return;
		}
		log.error("Could not interpret ctaSegment, unknown FunctionCode '" + ctaInfo.getFunctionCode() + "'.");
	}
	
	@Override
	public void visit(ComSegment comSegment) {
		ComInfo comInfo = comSegment.get();
		log.info("visit com " + comInfo.getAddressCode());
		repository.applyBuyerCommunication(comInfo.getAddressIdentifier(), comInfo.getAddressCode());
		log.error("Could not interpret comSegment, unknown FunctionCode '" + comInfo.getAddressCode() + "'.");
	}
}
