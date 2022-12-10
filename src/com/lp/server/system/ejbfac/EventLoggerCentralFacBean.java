package com.lp.server.system.ejbfac;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;

import com.lp.server.util.Facade;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.logger.LogEventDto;

@Singleton
public class EventLoggerCentralFacBean extends Facade implements EventLoggerCentralFacLocal {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			this.getClass());
	private List<LogEventDto> events ;

	protected List<LogEventDto> getEvents() {
		if(events == null) {
			events = Collections.synchronizedList(new LinkedList<LogEventDto>()) ;
		}
		return events ;
	}	
	
	protected void add(Priority priority, LogEventDto eventDto) {
		synchronized(getEvents()) {
			getEvents().add(eventDto) ;
			if(getEvents().size() > 1000) {
				getEvents().remove(0) ;
			}			
		}
	}
	
	
	@Override
	public void debug(LogEventDto eventDto) {
		add(Priority.DEBUG, eventDto) ;
	}

	@Override
	public void debug(String message, LogEventDto eventDto) {
		add(Priority.DEBUG, eventDto) ;
	}

	@Override
	public void debug(String message, LogEventDto eventDto, Throwable t) {
		add(Priority.DEBUG, eventDto) ;
	}

	@Override
	public void info(LogEventDto eventDto) {
		add(Priority.INFO, eventDto) ;
	}

	@Override
	public void info(String message, LogEventDto eventDto) {
		add(Priority.INFO, eventDto) ;
	}

	@Override
	public void info(String message, LogEventDto eventDto, Throwable t) {
		add(Priority.INFO, eventDto) ;
	}
	
	@Override
	public void warn(LogEventDto eventDto) {
		add(Priority.WARN, eventDto) ;
	}

	@Override
	public void warn(String message, LogEventDto eventDto) {
		add(Priority.WARN, eventDto) ;
	}

	@Override
	public void warn(String message, LogEventDto eventDto, Throwable t) {
		add(Priority.WARN, eventDto) ;
	}

	@Override
	public void error(LogEventDto eventDto) {
		add(Priority.ERROR, eventDto) ;
	}

	@Override
	public void error(String message, LogEventDto eventDto) {
		add(Priority.ERROR, eventDto) ;
	}

	@Override
	public void error(String message, LogEventDto eventDto, Throwable t) {
		add(Priority.ERROR, eventDto) ;
	}	
}
