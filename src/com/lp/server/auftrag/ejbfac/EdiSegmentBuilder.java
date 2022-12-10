package com.lp.server.auftrag.ejbfac;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EdiSegmentBuilder {
	private static final String eol = "'\n";
	private final DateFormat controlFormat = new SimpleDateFormat("yyMMddHHmmss") ;

	private StringBuffer ediContent = 
			new StringBuffer("UNA:+.? ").append(eol);
	private int segmentCount = 0;
	private String controlReference = "";
	private boolean hasFooter = false;
	
	public EdiSegmentBuilder() {
		controlReference = controlFormat.format(new Date());
	}
	 
	public EdiSegmentBuilder addSegment(String segment, String content) {
		ediContent.append(segment).append("+").append(content).append(eol);
		segmentCount++;
		return this;
	}
	
	public EdiSegmentBuilder addSegment(String segment, StringBuffer content) {
		return addSegment(segment, content.toString());
	}
	
	public String asEdiContent() {
		appendFooter();
		return ediContent.toString();
	}
		
	private void appendFooter() {
		if (!hasFooter) {
			addSegment("UNS", "S");
			ediContent.append("UNT+").append(segmentCount)
				.append("+").append(controlReference).append(eol);
			ediContent.append("UNZ+1+").append(controlReference).append(eol);
			
			hasFooter = true;
		}
	}
}
