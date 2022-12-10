package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.TodInfo;

public class TodSegment extends EdifactSegment<TodInfo> {
	public TodSegment() throws EdifactException {
		this(new TodInfo());
	}
	
	public TodSegment(TodInfo todInfo) throws EdifactException {
		super("TOD");
		set(todInfo);
		
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});

		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setPaymentCode(value);
			}
		});

		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setTransportationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setTransportationQualifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setResponsibleAgency(value);
			}
		});
		addComposite(70, true, new IValueSetter() {
			public void set(String value) {
				get().setTerms1(value);
			}
		});
		addComposite(70, true, new IValueSetter() {
			public void set(String value) {
				get().setTerms2(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected TodInfo createImpl() {
		return new TodInfo();
	}

}
