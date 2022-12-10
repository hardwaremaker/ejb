package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.PatInfo;

public class PatSegment extends EdifactSegment<PatInfo> {
	public PatSegment() throws EdifactException {
		this(new PatInfo());
	}
	
	public PatSegment(PatInfo patInfo) throws EdifactException {
		super("PAT");
		set(patInfo);
		
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setTypeQualifier(value);
			}
		});
		addData(17, new IValueSetter() {
			public void set(String value) {
				get().setIdentification(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setCodeQualifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setAgency(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setTerms(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				String s = get().getTerms() + value;
				get().setTerms(s);
			}
		});
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setCodedTime(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setCodedRelation(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setCodedPeriod(value);
			}
		});
		
		
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setTerms(value);
			}
		});

/*		
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				if (value.length() > 0) {
					get().setPeriods(Integer.parseInt(value));
				}
			}
		});
*/		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected PatInfo createImpl() {
		return new PatInfo();
	}
}
