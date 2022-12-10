package com.lp.service.edifact;

import java.io.IOException;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.UnhInfo;

public class EdifactOpcodeLoadProgramm extends EdifactOpcode<String> {
	private List<UnhInfo> unhInfos;
	private EdifactInterpreter interpreter;
	
	public EdifactOpcodeLoadProgramm(EdifactInterpreter interpreter, List<UnhInfo> unhInfo) {
		this.interpreter = interpreter;
		this.unhInfos = unhInfo;
	}
	
	@Override
	public String getSegmentCode() {
		return "[LOAD]";
	}
	
	@Override
	public boolean processIfRepetition(String segment, EdifactReader reader)
			throws EdifactException, IOException {
		return false;
	}
	
	@Override
	public boolean processNew(String segment, EdifactReader reader)
			throws EdifactException, IOException {
		interpreter.loadUserProgram(segment, unhInfos);
		return true;
	}
}
