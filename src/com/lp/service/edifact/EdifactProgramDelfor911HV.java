package com.lp.service.edifact;

import com.lp.service.edifact.visitor.Grp11VisitorHV;
import com.lp.service.edifact.visitor.Grp12VisitorHV;
import com.lp.service.edifact.visitor.Grp17VisitorHV;
import com.lp.service.edifact.visitor.Grp2VisitorHV;
import com.lp.service.edifact.visitor.Grp4VisitorHV;
import com.lp.service.edifact.visitor.Grp8VisitorHV;

public class EdifactProgramDelfor911HV extends EdifactProgramDelfor911 {
	private IEjbService ejbService;
	private IDelforRepository repository;

	private IVisitor grp2Visitor;
	private IVisitor grp4Visitor;
	private IVisitor grp8Visitor;
	private IVisitor grp11Visitor;
	private IVisitor grp12Visitor;
	private IVisitor grp17Visitor;
	
	public EdifactProgramDelfor911HV(IEjbService ejbService, IDelforRepository repository) {
		this.ejbService = ejbService;
		this.repository = repository;
		
		grp2Visitor = new Grp2VisitorHV(this.ejbService, this.repository);
		grp4Visitor = new Grp4VisitorHV(this.ejbService, this.repository);
		grp8Visitor = new Grp8VisitorHV(this.ejbService, this.repository);
		grp11Visitor = new Grp11VisitorHV(this.ejbService, this.repository);
		grp12Visitor = new Grp12VisitorHV(this.ejbService, this.repository);
		grp17Visitor = new Grp17VisitorHV(this.ejbService, this.repository);
	}
	
	
	@Override
	protected IVisitor getGrp2Visitor() {
		return grp2Visitor;
	}
	
	@Override
	protected IVisitor getGrp4Visitor() {
		return grp4Visitor;
	}
	
	@Override
	protected IVisitor getGrp8Visitor() {
		return grp8Visitor;
	}
	
	@Override
	protected IVisitor getGrp11Visitor() {
		return grp11Visitor;
	}
	
	@Override
	protected IVisitor getGrp12Visitor() {
		return grp12Visitor;
	}
	
	@Override
	protected IVisitor getGrp17Visitor() {
		return grp17Visitor;
	}
}
