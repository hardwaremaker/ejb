package com.lp.service.edifact.parser;

import java.io.IOException;

import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.UnexpectedDataElement;
import com.lp.service.edifact.schema.BgmInfo;

public class BgmParser extends AbstractSegmentParser {
	private BgmInfo bgmInfo;
	
	
	public BgmParser(EdifactReader parser) {
		super(parser);
	}
	
	public BgmInfo getBgmInfo() {
		return bgmInfo;
	}
	
	@Override
	public void parse() throws EdifactException, IOException {
		bgmInfo = new BgmInfo();
		Character c = getParser().read();
		if(!getParser().isDataSeparator(c)) {
			throw new UnexpectedDataElement(c, 
				getParser().getSeparators().getData(),
				getParser().getStartposition(), getParser().getPosition());
		}
		
		parseBeginningOfMessage(bgmInfo);
	}
	
	protected BgmInfo parseBeginningOfMessage(BgmInfo bgmInfo) throws EdifactException, IOException {
		bgmInfo.setDocumentNameCode(parseData(3));
		if(getParser().isSegmentSeparator()) return bgmInfo;
		
		bgmInfo.setDocumentIdentifier(parseData(35));
		if(getParser().isSegmentSeparator()) return bgmInfo;

		bgmInfo.setMessageFunction(parseData(3, true));
		if(getParser().isSegmentSeparator()) return bgmInfo;

		bgmInfo.setResponseTypeCode(parseData(3, true));
		return bgmInfo;
	}
}
