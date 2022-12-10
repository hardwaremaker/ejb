package com.lp.service.edifact;

public class OpcodeDummy extends EdifactOpcode<DummyInfo> {
	public OpcodeDummy() {
	}
	
	@Override
	public String getSegmentCode() {
		return "<dummySegment>";
	}
}
