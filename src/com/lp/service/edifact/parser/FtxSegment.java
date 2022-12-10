package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.FtxInfo;

public class FtxSegment extends EdifactSegment<FtxInfo> {	
	public FtxSegment() {
		this(new FtxInfo());
	}
	
	public FtxSegment(FtxInfo ftxInfo) {
		super("FTX");
		set(ftxInfo);
		
		/* 010 subject code qualifier */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setSubjectCode(value);
			}
		});

		/* 020 function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});

		/* 030 function code */
		addData(17, new IValueSetter() {
			public void set(String value) {
				get().setValueCode(value);
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

		/* 040 name and address */
		addData(512, new IValueSetter() {
			public void set(String value) {
				get().setText(0, value);
			}
		});
		addComposite(512, true, new IValueSetter() {
			public void set(String value) {
				get().setText(1, value);
			}
		});
		addComposite(512, true, new IValueSetter() {
			public void set(String value) {
				get().setText(2, value);
			}
		});
		addComposite(512, true, new IValueSetter() {
			public void set(String value) {
				get().setText(3, value);
			}
		});
		addComposite(512, true, new IValueSetter() {
			public void set(String value) {
				get().setText(4, value);
			}
		});

		/* 050 language name code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setLanguageNameCode(value);
			}
		});

		/* 060 language name code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFormatCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected FtxInfo createImpl() {
		return new FtxInfo();
	}
}
