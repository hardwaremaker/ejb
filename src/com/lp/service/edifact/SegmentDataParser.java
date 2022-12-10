package com.lp.service.edifact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lp.service.edifact.errors.EdifactEOFException;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ElementTooLongException;

public class SegmentDataParser implements ISegmentElement {
	private int maxlength;
	private IValueSetter setter;
	private List<SegmentCompositeParser> composites;
	
	public SegmentDataParser(int maxlength, IValueSetter setter) {
		this.maxlength = maxlength;
		this.setter = setter;
		
		this.composites = new ArrayList<SegmentCompositeParser>();
	}

	
	@Override
	public void parse(EdifactReader parser, EdifactSegment<?> grammar) throws EdifactException, IOException {
		setter.set(parseData(parser));
		if(hasComposite() && parser.isCompositeSeparator()) {
			parseComposites(parser, grammar);
		}
	}

	
	public SegmentDataParser addComposite(int maxlength, boolean optional, IValueSetter setter) {
		composites.add(new SegmentCompositeParser(maxlength, optional, setter));
		return this;
	}
	
	public boolean hasComposite() {
		return composites.size() > 0;
	}
	
	protected String parseData(EdifactReader parser) throws EdifactException, IOException {		
		StringBuffer sb = new StringBuffer();
		Character dataSeparator = parser.getSeparators().getData();
		Character compositeSeparator = parser.getSeparators().getComposite();
		Character escapeSeparator = parser.getSeparators().getEscape();
		Character end = parser.getSeparators().getSegment();
		Character c;
		boolean escaped = false;
		do {
			c = parser.read();
			if(c.equals(escapeSeparator)) {
				escaped = true;
				c = parser.read();
			}

			if(!escaped) {
				escaped = false;
				if(c.equals(dataSeparator)) {
					return sb.toString();
				}
				if(c.equals(compositeSeparator) && hasComposite()) {
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
	
	protected void parseComposites(EdifactReader parser, EdifactSegment<?> grammar) throws EdifactException, IOException {
		for(int index = 0; index < composites.size(); index++) {
			composites.get(index).parse(parser, grammar);
			if(parser.isSegmentSeparator() || parser.isDataSeparator()) {
				return;
			}
			if(parser.isCompositeSeparator() && (index + 1 > composites.size())) {
				throw new EdifactEOFException(parser.getStartposition(), parser.getPosition());
			}
		}
	}
}
