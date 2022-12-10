package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.BgmInfo;

public class BgmSegment extends EdifactSegment<BgmInfo> {
	public BgmSegment() throws EdifactException {
		this(new BgmInfo());
	}
	
	public BgmSegment(BgmInfo bgmInfo) throws EdifactException {
		super("BGM");
		set(bgmInfo);
		
		/* 010 document/message name */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setDocumentNameCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setResponsibleAgencyCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setDocumentName(value);
			}
		});

		/* 020 document/message identification */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setDocumentIdentifier(value);
			}
		});
		addComposite(9, true, new IValueSetter() {
			public void set(String value) {
				get().setVersionIdentifier(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setRevisionIdentifier(value);
			}
		});

		/* 030 document/message identification */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setMessageFunction(value);
			}
		});

		/* 040 document/message identification */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setResponseTypeCode(value);
			}
		});		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected BgmInfo createImpl() {
		return new BgmInfo();
	}
}
