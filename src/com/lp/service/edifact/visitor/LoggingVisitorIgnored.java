package com.lp.service.edifact.visitor;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.EdifactSegment;

public class LoggingVisitorIgnored extends LoggingVisitor {
	protected static final ILPLogger log = LPLogService.getInstance().getLogger(LoggingVisitorIgnored.class); 
	
	public LoggingVisitorIgnored() {
		super(new ILoggingVisitorFunction() {
			@Override
			public void accept(EdifactSegment<?> segment) {
				log.warn("visit ignored '" + segment.getSegmentCode() + "'.");
			}
		});
	}
}
