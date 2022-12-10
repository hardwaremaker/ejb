package com.lp.service.edifact.parser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseDateFormatException;
import com.lp.service.edifact.errors.UnexpectedDataElement;
import com.lp.service.edifact.errors.UnknownDateFormatException;
import com.lp.service.edifact.schema.DtmInfo;

public class DtmParser extends AbstractSegmentParser {
	private DtmInfo dtmInfo ;
	
	public DtmParser(EdifactReader parser) {
		super(parser);
	}
	
	public DtmInfo getDtmInfo() {
		return dtmInfo;
	}
	
	@Override
	public void parse() throws EdifactException, IOException {
		dtmInfo = new DtmInfo();
		Character c = getParser().read();
		if(!getParser().isDataSeparator(c)) {
			throw new UnexpectedDataElement(c, 
				getParser().getSeparators().getData(), 
				getParser().getStartposition(), getParser().getPosition());
		}
		
		parseDtmInfo(dtmInfo);		
	}
	
	protected DtmInfo parseDtmInfo(DtmInfo dtmInfo) throws EdifactException, IOException {
		dtmInfo.setFunctionCode(parseComposite(3));
//		dtmInfo.beSuccess();
		if(getParser().isSegmentSeparator()) return dtmInfo;
		
		dtmInfo.setDateString(parseComposite(35, true));
		if(getParser().isSegmentSeparator()) return dtmInfo;

		dtmInfo.setFormatCode(parseComposite(3, true));
		convertDate(dtmInfo);
		return dtmInfo;
	}
	
	protected void convertDate(DtmInfo dtmInfo) throws EdifactException {
		SimpleDateFormat sdf = null;
		if("102".equals(dtmInfo.getFormatCode())) {
			sdf = new SimpleDateFormat("yyyyMMdd");
		}

		if("716".equals(dtmInfo.getFormatCode())) {
			sdf = new SimpleDateFormat("yyyyYY");
		}
		
		if(sdf != null) {			
			sdf.setLenient(false);
			try {
				Date d = sdf.parse(dtmInfo.getDateString());
				dtmInfo.setDate(d);
			} catch(ParseException e) {
				throw new ParseDateFormatException(e, 
						getParser().getStartposition(), getParser().getPosition());
			}

			return;
		}
				
		throw new UnknownDateFormatException(dtmInfo.getFormatCode(), 
				getParser().getStartposition(), getParser().getPosition());
	}
}
