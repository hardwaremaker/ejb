package com.lp.server.util.logger;

import org.apache.log4j.Logger;

import com.lp.server.system.ejbfac.EventLoggerCentralFacLocal;
import com.lp.util.LpLoggerCS;


public class EventLogger  {
	private Logger logger ;
	private EventLoggerCentralFacLocal logFac ;
	
	public EventLogger(EventLoggerCentralFacLocal fac, Class<?> clazz) {
		this.logger = LpLoggerCS.getInstance(clazz) ;
		this.logFac = fac ;
	}
	
	public void debug(LogEventDto eventDto) {
		if(logger.isDebugEnabled()) {
			logger.debug(eventDto.asString());
		}
		logFac.debug(eventDto); 
	}

	public void debug(String message, LogEventDto eventDto) {
		if(logger.isDebugEnabled()) {
			logger.debug(message + ": " + eventDto.asString());
		}
		logFac.debug(message, eventDto); 
	}

	public void debug(String message, LogEventDto eventDto, Throwable t) {
		if(logger.isDebugEnabled()) {
			logger.debug(message + ": " + eventDto.asString(), t);
		}
		logFac.debug(message, eventDto, t); 
	}

	public void info(LogEventDto eventDto) {
		if(logger.isInfoEnabled()) {
			logger.info(eventDto.asString());
		}
		logFac.info(eventDto); 
	}

	public void info(String message, LogEventDto eventDto) {
		if(logger.isInfoEnabled()) {
			logger.info(message + ": " + eventDto.asString());
		}
		logFac.info(message, eventDto); 
	}

	public void info(String message, LogEventDto eventDto, Throwable t) {
		if(logger.isInfoEnabled()) {
			logger.info(message + ": " + eventDto.asString(), t);
		}
		logFac.info(message, eventDto, t); 
	}

	public void warn(LogEventDto eventDto) {
		logger.warn(eventDto.asString());
		logFac.warn(eventDto); 
	}

	public void warn(String message, LogEventDto eventDto) {
		logger.warn(message + ": " + eventDto.asString());
		logFac.warn(message, eventDto); 
	}

	public void warn(String message, LogEventDto eventDto, Throwable t) {
		logger.warn(message + ": " + eventDto.asString(), t);
		logFac.warn(message, eventDto, t); 
	}
	
	public void error(LogEventDto eventDto) {
		logger.error(eventDto.asString());
		logFac.error(eventDto); 
	}

	public void error(String message, LogEventDto eventDto) {
		logger.error(message + ": " + eventDto.asString());
		logFac.error(message, eventDto); 
	}

	public void error(String message, LogEventDto eventDto, Throwable t) {
		logger.error(message + ": " + eventDto.asString(), t);
		logFac.error(message, eventDto, t); 
	}
}
