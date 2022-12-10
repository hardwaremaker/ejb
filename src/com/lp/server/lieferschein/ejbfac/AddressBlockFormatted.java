package com.lp.server.lieferschein.ejbfac;

public class AddressBlockFormatted {
	public enum Tag {
		UNKNOWN,
		ANREDE,
		NAME1,
		NAME2,
		NAME3,
		ABTEILUNG,
		STRASSE,
		POSTFACH,
		PLZORT,
		LAND,
		ANSPRECHPARTNER
	}
	
	private final String[] lines;
	private final Tag[] tags;
	
	public AddressBlockFormatted() {
		this.lines = new String[8];
		this.tags = new Tag[8];
	}
	
	public AddressBlockFormatted setLine(int linenr, Tag tag, String content) {
		this.lines[linenr] = content;
		this.tags[linenr] = tag;
		return this;
	}
	
	public String getContentFor(int linenr) {
		return linenr < lines.length ? lines[linenr] : null;
	}

	public String getContentFor(Tag tag) {
		for(int i = 0; i < tags.length; i++) {
			if(tag.equals(tags[i])) return lines[i]; 
		}
		
		return null;
	}
	
	public boolean hasContentFor(Tag tag) {
		for(int i = 0; i < tags.length; i++) {
			if(tag.equals(tags[i])) {
				return lines[i] != null; 
			}
		}
		
		return false;		
	}

	public String asPrintBlock() {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] != null && lines[i].length() > 0) {
				sb.append(lines[i] + "\n");
			}
		}
		
		return sb.toString();	
	}
}
