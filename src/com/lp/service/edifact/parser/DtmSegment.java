package com.lp.service.edifact.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseDateFormatException;
import com.lp.service.edifact.errors.UnknownDateFormatException;
import com.lp.service.edifact.schema.DtmInfo;

public class DtmSegment extends EdifactSegment<DtmInfo> {
	public DtmSegment() throws EdifactException {
		this(new DtmInfo());
	}
	
	public DtmSegment(DtmInfo dtmInfo) throws EdifactException {
		super("DTM");
		set(dtmInfo);
		
		/* 010 party function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setDateString(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setFormatCode(value);
				convertDate();
			}
		});
	}
	
	protected void convertDate() throws ParseDateFormatException, UnknownDateFormatException {
		SimpleDateFormat sdf = null;
		String datestring = get().getDateString();
		if("102".equals(get().getFormatCode())) {
			sdf = new SimpleDateFormat("yyyyMMdd");
			datestring = get().getDateString();
		}

		if("716".equals(get().getFormatCode())) {
			sdf = new SimpleDateFormat("yyyyww");
			String[]s = get().getDateString().split("-");
			datestring = s[0];
		}
		
		if("401".equals(get().getFormatCode())) {
			sdf = new SimpleDateFormat("HHmm");
			datestring = get().getDateString();
		}
		
		if(sdf != null) {			
			sdf.setLenient(false);
			try {
				Date d = sdf.parse(datestring);
				get().setDate(d);
			} catch(ParseException e) {
				throw new ParseDateFormatException(e, 0, 0);
			}

			return;
		}
				
		throw new UnknownDateFormatException(get().getFormatCode(), 0, 0);	
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected DtmInfo createImpl() {
		return new DtmInfo();
	}
}
