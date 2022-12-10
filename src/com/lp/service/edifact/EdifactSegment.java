package com.lp.service.edifact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;

public abstract class EdifactSegment<T> implements IVisitable {
	private List<SegmentDataParser> grammar;
	private int index;
	private String segmentCode;
	private T data;
	
	public EdifactSegment(String segmentCode) {
		this.segmentCode = segmentCode;
		grammar = new ArrayList<SegmentDataParser>();
	}
	
	public String getSegmentCode() {
		return segmentCode;
	}
	
	public void addData(int maxlength, IValueSetter setter) {
		grammar.add(new SegmentDataParser(maxlength, setter)); 
	}
	
	public void addComposite(int maxlength, boolean optional, IValueSetter setter) {
		if(grammar.size() == 0) {
			throw new IllegalArgumentException("need data for composite");
		}
		grammar.get(grammar.size() - 1).addComposite(maxlength, optional, setter); 
	}
	
	public void parse(EdifactReader parser) throws EdifactException, IOException {
		for(index = 0; index < grammar.size(); index++) {
			grammar.get(index).parse(parser, this);
			if(parser.isSegmentSeparator()) {
				break;
			}
		}
	}
	
	public void set(T data) {
		this.data = data;
	}
	
	public T get() {
		return data;
	}
	
	public T create() {
		set(createImpl());
		return get();
	}

	@Override
	public abstract void accept(IVisitor visitor); 
	
	protected abstract T createImpl(); 
}
