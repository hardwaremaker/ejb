package com.lp.service.edifact.visitor.orders;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.IOrdersEjbService;
import com.lp.service.edifact.IOrdersRepository;
import com.lp.service.edifact.parser.RffSegment;
import com.lp.service.edifact.schema.RffInfo;
import com.lp.service.edifact.visitor.LoggingVisitorIgnored;

public class OrdersGrp1HV extends LoggingVisitorIgnored {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(OrdersGrp2HV.class); 

	private final IOrdersEjbService ejbService;
	private final IOrdersRepository repository;
	
	public OrdersGrp1HV(IOrdersEjbService ordersEjbService, IOrdersRepository ordersRepository) {
		this.ejbService = ordersEjbService;
		this.repository = ordersRepository;
	}
	
	public IOrdersRepository repository() {
		return this.repository;
	}

	@Override
	public void visit(RffSegment rffSegment) {
		RffInfo rffInfo = rffSegment.get();
		log.info("visit rff " + rffInfo.getFunctionCode());
		if ("CT".equals(rffInfo.getFunctionCode())) {
			repository().applyContractInfo(rffInfo.getIdentifier());
			return;
		}

		log.warn("Ignored rff "  + rffInfo.getFunctionCode());
	}
}
