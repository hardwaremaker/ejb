package com.lp.server.system.ejbfac;

import javax.ejb.Local;

import com.lp.server.util.logger.LogEventDto;

@Local
public interface EventLoggerCentralFacLocal {
	enum Priority {
		TRACE, DEBUG, INFO, WARN, ERROR
	}
	
	void debug(LogEventDto eventDto) ;
	void debug(String message, LogEventDto eventDto) ;
	void debug(String message, LogEventDto eventDto, Throwable t) ;

	void info(LogEventDto eventDto) ;
	void info(String message, LogEventDto eventDto) ;
	void info(String message, LogEventDto eventDto, Throwable t) ;

	void warn(LogEventDto eventDto) ;
	void warn(String message, LogEventDto eventDto) ;
	void warn(String message, LogEventDto eventDto, Throwable t) ;

	void error(LogEventDto eventDto) ;
	void error(String message, LogEventDto eventDto) ;
	void error(String message, LogEventDto eventDto, Throwable t) ;
}
