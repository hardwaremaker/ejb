package com.lp.service.edifact.visitor;

import com.lp.service.edifact.EdifactSegment;

public interface ILoggingVisitorFunction {
	void accept(EdifactSegment<?> segment);
}
