package com.lp.service.edifact.parser;

import java.math.BigDecimal;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.PriInfo;

public class PriSegment extends EdifactSegment<PriInfo> {

	public PriSegment() throws EdifactException {
		this(new PriInfo());
	}
	
	public PriSegment(PriInfo priInfo) throws EdifactException {
		super("PRI");
		set(priInfo);
		
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setQualifier(value);
			}
		});
		
		addComposite(15, true, new IValueSetter() {
			public void set(String value) {
				get().setPrice(new BigDecimal(value));
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setTypeCoded(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setTypeQualifier(value);
			}
		});
		addComposite(9, true, new IValueSetter() {
			public void set(String value) {
				get().setBasis(new BigDecimal(value));
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setUnitQualifier(value);
			}
		});
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setSublinePriceChangeType(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected PriInfo createImpl() {
		return new PriInfo();
	}
}
