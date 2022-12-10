package com.lp.service.edifact.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.errors.EdifactEOFException;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ElementTooLongException;

public abstract class AbstractSegmentParser {
	private EdifactReader parser;

	protected AbstractSegmentParser(EdifactReader parser) {
		this.parser = parser;
	}
	
	protected EdifactReader getParser() {
		return parser;
	}
	
	public void parse(String stream) throws EdifactException, IOException {
		parse(new ByteArrayInputStream(stream.getBytes()));
	}
	
	public void parse(InputStream inputStream) throws EdifactException, IOException{
		parser.setInputStream(inputStream);
		parse();
	}
	
	public abstract void parse() throws EdifactException, IOException;
	
	protected String parseData(int maxlength) throws EdifactException, IOException  {
		return parseData(maxlength, false);
	}
	
	protected String parseData(int maxlength, boolean optional) throws EdifactException, IOException {		
		StringBuffer sb = new StringBuffer();
		Character separator = parser.getSeparators().getData();
		Character end = parser.getSeparators().getSegment();
		int length = 0;
		Character c;
		do {
			c = parser.read();
			if(c.equals(separator)) {
				return sb.toString();
			}
			if(c.equals(end)) {
				if(optional) {
					return sb.toString();
				} else {
					throw new EdifactEOFException(parser.getStartposition(), parser.getPosition());
				}				
			}
			
			sb.append(c);				
			if(++length > maxlength) {
				throw new ElementTooLongException(maxlength, sb.toString());
			}
		} while(!c.equals(separator));
		
		return sb.toString();
	}	
	
	protected String parseComposite(int maxlength) throws EdifactException, IOException {
		return parseComposite(maxlength, false);
	}
	
	protected String parseComposite(int maxlength, boolean optional) throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		Character separator = parser.getSeparators().getComposite();
		Character end = parser.getSeparators().getSegment();
		Character c;
		int length = 0;
		do {
			c = parser.read();
			if(c.equals(separator)) {
				return sb.toString();
			}
			if(c.equals(end)) {
				if(optional) {
					return sb.toString();					
				} else {
					throw new EdifactEOFException(parser.getStartposition(), parser.getPosition());
				}
			}
			sb.append(c);
			if(++length > maxlength) {
				throw new ElementTooLongException(maxlength, sb.toString());
			}
		} while(!c.equals(separator));
		
		return sb.toString();		
	}
}
