package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.RffInfo;

public class RffSegment extends EdifactSegment<RffInfo> {
	public RffSegment() {
		this(new RffInfo());
	}
	
	public RffSegment(RffInfo rffInfo) {
		super("RFF");
		set(rffInfo);
		
		/* 010 reference */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});

// PJ22499 Besteller verwendet RFF+CT missbraeuchlich fuer Bestellinformation
// deswegen Erhoehung auf eine (hoffentlich) ausreichend hohe Laenge		
//		addComposite(35, true, new IValueSetter() {
		addComposite(3000, true, new IValueSetter() {
			public void set(String value) {
				get().setIdentifier(value);
			}
		});
// PJ22499 Wenn zuvor im Identifier ein ':' verwendet wird (weil man ja Text schreibt)
// landet der Teil danach hier und damit ist auch dieses Element zu lang		
//		addComposite(6, true, new IValueSetter() {
		addComposite(3000, true, new IValueSetter() {
			public void set(String value) {
				get().setDocumentLineIdentifier(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setVersionIdentifier(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setRevisionIdentifier(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);		
	}
	
	@Override
	protected RffInfo createImpl() {
		return new RffInfo();
	}
}
