package com.lp.service.edifact;

import java.io.IOException;

import com.lp.service.edifact.errors.EdifactException;

public interface ISegmentElement {
	void parse(EdifactReader parser, EdifactSegment<?> grammar) throws EdifactException, IOException;
}
