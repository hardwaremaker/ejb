package com.lp.service.edifact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.MaxRepetitionsReachedException;
import com.lp.service.edifact.errors.UnexpectedProgrammEndException;
import com.lp.service.edifact.errors.UnexpectedSegmentException;


public class EdifactGroupOpcode extends EdifactOpcode<String> {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(EdifactGroupOpcode.class); 

	private List<EdifactOpcode<?>> ops;
	private int pc;
	private EdifactOpcode<?> lastOp;
	private EdifactInterpreter interpreter;
	private String groupName;
	
	public EdifactGroupOpcode(String groupName, EdifactInterpreter interpreter, int count, boolean optional) {
		this(groupName, interpreter, count, optional, null);
	}
	
	public EdifactGroupOpcode(String groupName, EdifactInterpreter interpreter, int count, boolean optional, IVisitor visitor) {
		setMaxCount(count);
		setOptional(optional);
		setVisitor(visitor);
		this.interpreter = interpreter;
		this.groupName = groupName;
		ops = new ArrayList<EdifactOpcode<?>>();
		lastOp = new OpcodeDummy();
		pc = -1;		
	}
	
	public EdifactGroupOpcode add(EdifactOpcode<?> op) {
		ops.add(op);
		return this;
	}
	
	@Override
	public String getSegmentCode() {
		return this.groupName;
	}
	
	@Override
	public boolean processIfRepetition(String segment, EdifactReader reader)
			throws EdifactException, IOException {
		log.info("Entered group " + getSegmentCode() + " processIfRepetition with segment '" + segment + "', pc=" + pc + ".");
				
		incCount();
		if(getCount() > getMaxCount()) {
			throw new MaxRepetitionsReachedException(this);
		}
		
		pc = -1;
		return processNew(segment, reader);
	}

	@Override
	public boolean processIfRepetition(String segment, EdifactReader reader, IVisitor visitor)
			throws EdifactException, IOException {
		log.info("Entered group " + getSegmentCode() + " processIfRepetition with segment '" + segment + "', pc=" + pc + ".");
		
		incCount();
		if(getCount() > getMaxCount()) {
			throw new MaxRepetitionsReachedException(this);
		}
		
		pc = -1;
		return processNew(segment, reader, visitor);
	}

	@Override
	public boolean processNew(String segment, EdifactReader reader)
			throws EdifactException, IOException {
		return processNew(segment, reader, getVisitor());
	}
	
	@Override
	public boolean processNew(String segment, 
			EdifactReader reader, IVisitor visitor) throws EdifactException, IOException {
		log.info("Entered group " + getSegmentCode() + " processNew with segment '" + segment + "', pc=" + pc + ".");

		if(getVisitor() != null) {
			visitor = getVisitor();
		}
		
		if(ops.size() == 0) {
			throw new UnexpectedProgrammEndException();
		}
		
		boolean read = false;
		boolean hasRead = false;
		if(pc < 0) {
			pc = 0;
			EdifactOpcode<?> op = ops.get(pc);
			if(op.processNew(segment, reader, visitor)) {
//				op.getOp().accept(getVisitor());
				lastOp = op;
				read = true;
			} else {
				if(op.isMandatory()) {
					// Das erste Element der Gruppe ist mandatory, 
					// aber nicht das gelesene Segment
					// Wenn die Gruppe optional ist, dann ignoriere die komplette Gruppe
					boolean b = getCount() > 0;
					if(isOptional() || b) {
						handleGroupEnd();
						log.info("Leaved group " + getSegmentCode() + " processNew with segment '" + segment + "', pc=" + pc + " (false).");
						return false;
					}
					
					throw new UnexpectedSegmentException(segment,
							new String[]{op.getSegmentCode()}, reader.getStartposition(), reader.getPosition());
				}
			}

			++pc;
		}

		do {			
			if(read && reader.isAvailable()) {
				segment = interpreter.readSegment();
				read = false;
				hasRead = true;				
			}
			
			if(isGroupEnd()) {
				log.warn("<<<<< isGroupEnd trigger");
				handleGroupEnd();
				return true;
			}
			
//			if(lastOp.isGroup()) {
				log.info("group calling processIfRepetition on segment '" + segment + "' with lastop '" + lastOp.getSegmentCode() + "'." );
				boolean rc = lastOp.processIfRepetition(segment, reader, visitor);
				log.info("group called processIfRepetition on segment '" + segment + "' with lastop '" + lastOp.getSegmentCode() + "', rc=" + rc + "." );
				if(rc) {
					read = true;
					hasRead = false;
					continue;
				}				
//			}
			
			EdifactOpcode<?> op = ops.get(pc);			
			if(op.processNew(segment, reader, visitor)) {
//				op.getOp().accept(getVisitor());
				lastOp = op;
				read = true;
				hasRead = false;
			} else {
				lastOp = new OpcodeDummy();
				if(op.isMandatory()) {
					throw new UnexpectedSegmentException(segment, 
							new String[]{op.getSegmentCode()}, reader.getStartposition(), reader.getPosition());			
				}				
			}			

			++pc;
			boolean groupEnd = isGroupEnd();
			if(groupEnd) {
				if(lastOp.isRepeating()) {
					boolean iterate = false;
					do {
						if(!hasRead) {
							if(reader.isAvailable()) {
								segment = interpreter.readSegment();
							} else {
								break;
							}
						}
						hasRead = true;
						
//						iterate = lastOp.processIfRepetition(segment, reader);
						iterate = lastOp.processIfRepetition(segment, reader, visitor);
						
						if(iterate) {
							hasRead = false;
						}
					} while(iterate);
				}
				handleGroupEnd();
				
				if(hasRead) {
					interpreter.unreadSegment(segment);
				}
				log.info("Leaved group " + getSegmentCode() + " processNew with segment '" + segment + "', pc=" + pc + " (true).");
				return true;
			}			
		} while(true);
	}
	
	
	private boolean isGroupEnd() {
		return pc >= ops.size();
	}
	
	private void handleGroupEnd() {
		pc = -1;

		for (EdifactOpcode<?> opcode: ops) {
			opcode.resetCount();
		}		
	}

	public boolean isGroup() {
		return true;
	}
	
	@Override
	public String toString() {
		return groupName + ": " + getCount() + " of " + getMaxCount() + " with " + ops.size() + " codes.";
	}
}
