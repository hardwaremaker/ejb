package com.lp.service.edifact;

import com.lp.service.edifact.visitor.orders.OrdersGrp1HV;
import com.lp.service.edifact.visitor.orders.OrdersGrp25HV;
import com.lp.service.edifact.visitor.orders.OrdersGrp2HV;
import com.lp.service.edifact.visitor.orders.OrdersGrp49HV;

public class EdifactProgramOrders96aHV extends EdifactProgramOrders96a {

	private final IOrdersRepository repository;
	
	private final IVisitor grp1Visitor;
	private final IVisitor grp2Visitor;
	private final IVisitor grp25Visitor;
	private final IVisitor grp29Visitor = super.getGrp29Visitor();
	private final IVisitor grp49Visitor;
	
	public EdifactProgramOrders96aHV(IOrdersEjbService ejbService, IOrdersRepository repository) {
		this.repository = repository;
		
		grp1Visitor = new OrdersGrp1HV(ejbService, repository);
		grp2Visitor = new OrdersGrp2HV(ejbService, repository);
		grp25Visitor = new OrdersGrp25HV(ejbService, repository);
		grp49Visitor = new OrdersGrp49HV(ejbService, repository);
	}
	
	public IOrdersRepository getRepository() {
		return repository;
	}
	
	@Override
	protected IVisitor getGrp1Visitor() {
		return grp1Visitor;
	}
	
	@Override
	protected IVisitor getGrp2Visitor() {
		return grp2Visitor;
	}
	
	@Override
	protected IVisitor getGrp25Visitor() {
		return grp25Visitor;
	}
	
	@Override
	protected IVisitor getGrp29Visitor() {
		return grp29Visitor;
	}
	
	@Override
	protected IVisitor getGrp49Visitor() {
		return grp49Visitor;
	}
}
