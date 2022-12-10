package com.lp.service.edifact;

import java.io.IOException;
import java.io.InputStream;

import com.lp.service.edifact.errors.EdifactEOFException;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.UnexpectedDataElement;
import com.lp.service.edifact.errors.UnexpectedSegmentException;

public class EdifactReader {
	private Separators separators;
	private InputStream inputStream;
	private int position;
	private int startposition;
	private Character lastRead;
	
	public EdifactReader() {
		setSeparators(new DefaultSeparators());
	}
	
	public Separators getSeparators() {
		return separators;
	}

	public void setSeparators(Separators separators) {
		this.separators = separators;
	}
	
	public Character getLastRead() {
		return lastRead;
	}
	
	public int getStartposition() {
		return startposition;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void parse(InputStream inputStream) throws EdifactException, IOException {
		this.setInputStream(inputStream);
		parseInterchange();
	}
	
	/**
	 * Interchange Header parsen</br>
	 * <p>Interchange besteht aus mandatory UNB/UNZ, optional UNA</p>
	 * @throws EdifactException
	 * @throws IOException
	 */
	public String parseInterchange() throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(read()).append(read()).append(read());
		
		String segment = sb.toString();
		if("UNA".equals(segment)) {
			parseUnaData();
//			segment = requireSegment("UNB");
			
			sb = new StringBuffer();
			sb.append(readIgnoreLineendings()).append(read()).append(read());
			return sb.toString(); 
		}
		
		if("UNB".equals(segment)) {
//			parseUnbData();
//			sb = new StringBuffer();
//			sb.append(readIgnoreLineendings()).append(read()).append(read());
//			return sb.toString(); 
			
			return sb.toString();
		} else {
			throw new UnexpectedSegmentException(segment, 
					new String[]{"UNA", "UNB"}, startposition, position);			
		}
	}

	public String requireSegment(String segment) throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < segment.length(); i++) {
			sb.append(i == 0 ? readIgnoreLineendings() : read());
			markStartposition();
		}

		return sb.toString();
	}
	
	protected void parseUnaData() throws EdifactException, IOException {
		Separators s = new Separators();
		s.setComposite(read());
		s.setData(read());
		s.setDecimal(read()); 
		s.setEscape(read());
		read();
		s.setSegment(read());
		setSeparators(s);
	}
	
	protected void parseUnbData() throws EdifactException, IOException {
		Character c = read();
		if(!c.equals(getSeparators().getData())) {
			throw new UnexpectedDataElement(c, getSeparators().getData(), startposition, position);
		}
		StringBuffer sb = new StringBuffer();
		do {
			c = read();
			sb.append(c);
		} while(!c.equals(getSeparators().getSegment()));
	}
	
	/**
	 * Einen Message Header parsen</br>
	 * <p>Eine Message besteht aus UNH/UNT, optional einem GroupHeader UNG/UNE</p> 
	 * @throws EdifactException
	 * @throws IOException
	 */
	protected void parseMessage() throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(readIgnoreLineendings()).append(read()).append(read());
		
		String segment = sb.toString();
		if("UNG".equals(segment)) {
			parseGroupHeader();
			segment = requireSegment("UNH");
		}
		
		if("UNH".equals(segment)) {
			parseUnhData();
		} else {
			throw new UnexpectedSegmentException(segment, 
					new String[]{"UNG", "UNH"}, startposition, position);			
		}
		
	}
	
	protected void parseGroupHeader() throws EdifactException, IOException {
		
	}
	
	protected void parseUnhData() throws EdifactException, IOException {
		
	}
	
	protected void markStartposition() {
		this.startposition = position;
	}
	
	public boolean isAvailable() throws IOException {
		return getInputStream().available() > 0;
	}
	
	public Character read() throws EdifactException, IOException {
		int c = getInputStream().read();
		if(c == -1) throw new EdifactEOFException(startposition, position);
		
		++position;
		lastRead = new Character((char) c);
		return lastRead;
	}
	
	protected Character peekingRead() throws IOException {
		int c = getInputStream().read();
		if(c == -1) return null;
		
		++position;
		lastRead = new Character((char) c);
		return lastRead;		
	}
	
	public Character readIgnoreLineendings() throws EdifactException, IOException {
		boolean hasReadLineending = false;
		Character c;
		do {
			c = peekingRead();
			if(c == null) {
				if(hasReadLineending) {
					lastRead = getSeparators().getSegment();
					return lastRead;
				} else {
					throw new EdifactEOFException(getStartposition(), getPosition());
				}
			}
			hasReadLineending = (c == 13 || c == 10); 
		} while (c == 13 || c == 10);
		
		return c;
	}
	
	public String skipSegment() throws EdifactException, IOException {
		StringBuffer sb = new StringBuffer();
		Character end = getSeparators().getSegment();
		Character c;
		do {
			c = read();
			if(!c.equals(end)) {
				sb.append(c);
			}
		} while(!c.equals(end));
		return sb.toString();
	}
	
	protected InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public boolean isDataSeparator() {
		return isDataSeparator(getLastRead());
	}

	public boolean isDataSeparator(Character c) {
		return getSeparators().getData().equals(c);
	}

	public boolean isCompositeSeparator() {
		return isCompositeSeperator(getLastRead());
	}
	
	public boolean isCompositeSeperator(Character c) {
		return getSeparators().getComposite().equals(c);
	}
	
	public boolean isSegmentSeparator() {
		return isSegmentSeparator(getLastRead());
	}
	
	public boolean isSegmentSeparator(Character c) {
		return getSeparators().getSegment().equals(c);
	}
}
