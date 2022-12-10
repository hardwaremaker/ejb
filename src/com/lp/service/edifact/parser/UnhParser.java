package com.lp.service.edifact.parser;

import java.io.IOException;

import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.UnexpectedDataElement;
import com.lp.service.edifact.schema.MessageHeader;

public class UnhParser extends AbstractSegmentParser {
	private MessageHeader msgHeader;
	
	public UnhParser(EdifactReader parser) {
		super(parser);
	}
	
	public MessageHeader getMsgHeader() {
		return msgHeader;
	}
		
	public void parse() throws EdifactException, IOException {
		msgHeader = new MessageHeader();
		Character c = getParser().read();
		if(!getParser().isDataSeparator(c)) {
			throw new UnexpectedDataElement(c, 
				getParser().getSeparators().getData(), 
				getParser().getStartposition(), getParser().getPosition());
		}
		
		msgHeader.setReferenceCnr(parseData(14));
		parseMessageIdentifier(msgHeader);
	}	
		
	protected MessageHeader parseMessageIdentifier(MessageHeader header) throws EdifactException, IOException {
		header.setType(parseComposite(6));
		header.setVersion(parseComposite(3));
		header.setRelease(parseComposite(3));
		header.setAgency(parseComposite(3, true));
		header.beSuccess();

		if(getParser().isSegmentSeparator(getParser().getLastRead())) {
			return header;
		}

		getParser().skipSegment();
		return header;
	}
}
