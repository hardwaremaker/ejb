package com.lp.service.edifact;

import com.lp.service.edifact.visitor.Sg12VisitorHV;
import com.lp.service.edifact.visitor.Sg22VisitorHV;
import com.lp.service.edifact.visitor.Sg27VisitorHV;
import com.lp.service.edifact.visitor.Sg2VisitorHV;
import com.lp.service.edifact.visitor.Sg7VisitorHV;

public class EdifactProgramDelfor99bHV extends EdifactProgramDelfor99b {
	private IEjbService ejbService;
	private IDelforRepository repository;
	
	private IVisitor sg2Visitor;
	private IVisitor sg7Visitor;
	private IVisitor sg12Visitor;
	private IVisitor sg22Visitor;
	private IVisitor sg27Visitor;
	
	public EdifactProgramDelfor99bHV(IEjbService ejbService, IDelforRepository repository) {
		this.ejbService = ejbService;
		this.repository = repository;
		
		sg2Visitor = new Sg2VisitorHV(this.ejbService, this.repository);
		sg7Visitor = new Sg7VisitorHV(this.ejbService, this.repository);
		sg12Visitor = new Sg12VisitorHV(this.ejbService, this.repository);
		sg22Visitor = new Sg22VisitorHV(this.ejbService, this.repository);
		sg27Visitor = new Sg27VisitorHV(this.ejbService, this.repository);
	}
	
	@Override
	protected IVisitor getSg2Visitor() {
		return sg2Visitor;
	}	
	
	@Override
	protected IVisitor getSg7Visitor() {
		return sg7Visitor;
	}
	
	@Override
	protected IVisitor getSg12Visitor() {
		return sg12Visitor; 
	}

	@Override
	protected IVisitor getSg22Visitor() {
		return sg22Visitor;
	}
	
	@Override
	protected IVisitor getSg27Visitor() {
		return sg27Visitor;
	}
}
