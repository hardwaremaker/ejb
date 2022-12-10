package com.lp.service.edifact;

import java.io.IOException;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.MaxRepetitionsReachedException;

public class EdifactOpcode<T> {
	private EdifactSegment<T> op;
	private int count;
	private int maxCount;
	private boolean optional;
	private List<T> data;
	private IVisitor visitor;
	
	protected EdifactOpcode() {
	}
	
	public EdifactOpcode(EdifactSegment<T> op, int count, boolean optional, List<T> opdata) {
		this(op, count, optional, opdata, null);
	}
	
	public EdifactOpcode(EdifactSegment<T> op, int count, boolean optional, List<T> opdata, IVisitor visitor) {
		this.op = op;
		this.data = opdata;		
		setMaxCount(count);
		setOptional(optional); 
		setVisitor(visitor);
	}
	
	protected EdifactSegment<T> getOp() {
		return this.op;
	}
	
	public String getSegmentCode() {
		return op.getSegmentCode();
	}
	
	public List<T> getData() {
		return data;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incCount() {
		++count;
	}
	
	public void resetCount() {
		setCount(0);
	}
	
	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isMandatory() {
		return !optional;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public IVisitor getVisitor() {
		return visitor;
	}
	
	public void setVisitor(IVisitor visitor) {
		this.visitor = visitor;
	}
	
	protected void process(EdifactReader reader, IVisitor visitor) throws EdifactException, IOException {
		if(getCount() >= getMaxCount()) {
			throw new MaxRepetitionsReachedException(this);
		}
		
		T dataElement = op.create();
		data.add(dataElement);
		op.parse(reader);
		if(visitor != null) {
			op.accept(visitor);
		}
		incCount();
	}
	
	public boolean processIfRepetition(String segment, EdifactReader reader) throws EdifactException, IOException {
		if(getSegmentCode().equals(segment) && isRepeating()) {
			process(reader, getVisitor());
			return true;
		}
		
		return false;
	}

	public boolean processIfRepetition(String segment, EdifactReader reader, IVisitor visitor) throws EdifactException, IOException {
		if(getSegmentCode().equals(segment) && isRepeating()) {
			process(reader, visitor);
			return true;
		}
		
		return false;
	}

	
	public boolean processNew(String segment, EdifactReader reader) throws EdifactException, IOException {
		return processNew(segment, reader, getVisitor());
//		if(getSegmentCode().equals(segment)) {
//			process(reader, getVisitor());
//			return true;
//		}
//		return false;
	}
	
	public boolean processNew(String segment, EdifactReader reader, IVisitor visitor) throws EdifactException, IOException {
		if(getSegmentCode().equals(segment)) {
			process(reader, visitor);
			return true;
		}
		return false;		
	}
	
	public boolean isGroup() {
		return false;
	}

	/**
	 * Darf Element wiederholt werden?
	 * 
	 * @return true wenn das Element wiederholt werden darf
	 */
	public boolean isRepeating() {
		return getMaxCount() > 1;
	}
	
	/**
	 * Gibt es schon Wiederholungen von diesem Element?
	 * 
	 * @return true wenn das Element schon mehr als einmal aufgetreten ist
	 */
	public boolean hasRepetitions() {
		return getCount() > 1;
	}
	
	@Override
	public String toString() {
		return getSegmentCode() + ": " + getCount() + " of " + getMaxCount() + ".";
	}
}
