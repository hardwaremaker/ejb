package com.lp.service.edifact;

import java.util.List;

import com.lp.service.edifact.errors.EdifactException;

public abstract class EdifactProgram {
	public EdifactProgram() {
	}
	
	public abstract List<EdifactOpcode<?>> getProgram(EdifactInterpreter interpreter) throws EdifactException;
}
