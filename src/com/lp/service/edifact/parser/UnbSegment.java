package com.lp.service.edifact.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseDateFormatException;
import com.lp.service.edifact.schema.UnbInfo;


public class UnbSegment extends EdifactSegment<UnbInfo> {
	public UnbSegment() throws EdifactException {
		this(new UnbInfo());
	}
	
	public UnbSegment(UnbInfo unbInfo) throws EdifactException {
		super("UNB");
		set(unbInfo);
		
		/* 010 syntax identifier */
		addData(4, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setSyntaxIdentifier(value);
			}
		});
		addComposite(1, false, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setSyntaxVersionNumber(value);
			}
		});
		
		/* 020 interchange sender */
		addData(35, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setSenderIdentification(value);
			}
		});
		
		/* 030 interchange recipient */
		addData(35, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setReceiverIdentification(value);
			}
		});
		
		/* 040 date/time of preparation */
		addData(8, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setDateString(value);
			}
		});
		addComposite(4, false, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setTimeString(value);
				convertDate();
			}
		});
		
		/* 050 control reference */
		addData(14, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setControlReference(value);
			}
		});

		/* 050 control reference */
		addData(14, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setRecipientReference(value);
			}
		});

		
		/* 050 application reference */
		addData(14, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setApplicationReference(value);
			}
		});
	}
	
	protected void convertDate() throws ParseDateFormatException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		sdf.setLenient(false);
		try {
			String ds = get().getDateString();
			String ts = get().getTimeString();
			Date d = sdf.parse(ds + ts);
			get().setDate(d);
		} catch(ParseException e) {
			throw new ParseDateFormatException(e, 0, 0);
		}
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);	
	}
	
	@Override
	protected UnbInfo createImpl() {
		return new UnbInfo();
	}
}
