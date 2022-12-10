package com.lp.service.edifact.visitor.orders;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.IOrdersEjbService;
import com.lp.service.edifact.IOrdersRepository;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.parser.SccSegment;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.schema.SccInfo;
import com.lp.service.edifact.visitor.LoggingVisitorIgnored;
import com.lp.util.Helper;

public class OrdersGrp49HV extends LoggingVisitorIgnored {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(OrdersGrp49HV.class); 

	private final IOrdersEjbService ejbService;
	private final IOrdersRepository repository;
	

	public OrdersGrp49HV(IOrdersEjbService ordersEjbService, IOrdersRepository ordersRepository) {
		this.ejbService = ordersEjbService;
		this.repository = ordersRepository;
	}
	
	public IOrdersRepository repository() {
		return this.repository;
	}

	@Override
	public void visit(DtmSegment dtmSegment) {
		DtmInfo dtmInfo = dtmSegment.get();
		
		if ("2".equals(dtmInfo.getFunctionCode())) {
			if (repository().hasDeliveryPosition()) {
				log.info("\tdelivery position date " + dtmInfo.getDateString());
				repository().applyDeliveryDate(dtmInfo.getDate());
			} else {
				log.warn("\tdelivery date " + dtmInfo.getDateString() + 
						" without deliveryposition (SCC)");
			}
			return;
		}
		super.visit(dtmSegment);
	}
	


	@Override
	public void visit(QtySegment qtySegment) {
		QtyInfo qtyInfo = qtySegment.get();
		// Split bzw. Ordered Quantity hat die gleiche Wirkung
		if (Helper.isOneOf(qtyInfo.getTypeCode(), "11", "21")) {
//		if ("11".equals(qtySegment.get().getTypeCode())) {
			if (repository().hasDeliveryPosition()) {
				log.info("\tdelivery quantity " + qtyInfo.getAmount().toPlainString());
				
				repository().applyDeliveryQuantity(qtyInfo.getAmount());
			} else {
				log.warn("\tdelivery quantity " + qtyInfo.getAmount().toPlainString() +
						" without deliveryposition (SSC)!");
			}
			
			return;
		}
	
		super.visit(qtySegment);
	}

	@Override
	public void visit(SccSegment sccSegment) {
		SccInfo sccInfo = sccSegment.get();
		if ("1".equals(sccInfo.getPlanCommitmentCode())) {
			log.info("\tExpecting delivery infos...");
			repository().pushDeliveryPosition();
			return;
		}
		super.visit(sccSegment);
	}
}
