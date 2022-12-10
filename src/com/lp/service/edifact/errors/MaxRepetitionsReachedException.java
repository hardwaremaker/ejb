package com.lp.service.edifact.errors;

import com.lp.service.edifact.EdifactOpcode;

public class MaxRepetitionsReachedException extends EdifactException {
	private static final long serialVersionUID = 612936756577278248L;

	private EdifactOpcode<?> opcode;
	
	public MaxRepetitionsReachedException(EdifactOpcode<?> opcode) {
		super("To many repetitions (" + opcode.getCount() + " of " + opcode.getMaxCount() + ")");
		this.setOpcode(opcode);
	}

	public EdifactOpcode<?> getOpcode() {
		return opcode;
	}

	public void setOpcode(EdifactOpcode<?> opcode) {
		this.opcode = opcode;
	}
}
