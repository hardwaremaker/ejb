package com.lp.service.edifact;

import java.io.IOException;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ElementTooLongException;

public class SegmentCompositeParser implements ISegmentElement {
	private int maxlength;
	private IValueSetter setter;
	private boolean optional;
	
	public SegmentCompositeParser(int maxlength, boolean optional, IValueSetter setter) {
		this.maxlength = maxlength;
		this.optional = optional;
		this.setter = setter;
	}

	@Override
	public void parse(EdifactReader parser, EdifactSegment<?> grammar) throws EdifactException, IOException {
		setter.set(parseComposite(parser));
	}
	
	protected String parseComposite(EdifactReader parser) throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		Character dataSeparator = parser.getSeparators().getData();
		Character compositeSeparator = parser.getSeparators().getComposite();
		Character end = parser.getSeparators().getSegment();
		Character escapeSeparator = parser.getSeparators().getEscape();
		Character c;
		boolean escaped = false;
		do {
			c = parser.read();
			if(c.equals(escapeSeparator)) {
				escaped = true;
				c = parser.read();
			}

			if(!escaped) {
				if(c.equals(dataSeparator)) {
					return sb.toString();
				}
				if(c.equals(compositeSeparator)) {
					return sb.toString();
				}
				
				if(c.equals(end)) {
					return sb.toString();
				}				
			}
						
			sb.append(c);				
			if(sb.length() > maxlength) {
				throw new ElementTooLongException(maxlength, sb.toString());
			}
			escaped = false;
		} while(true);	
	}
}
