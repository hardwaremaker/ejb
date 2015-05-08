/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 *  
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 *
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *   
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * Contact: developers@heliumv.com
 *******************************************************************************/
package com.lp.server.media.service;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class HvQueue {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());
	
	private String queueName ;
	private boolean ignoreJMSExceptions = false ;
	
	private Context context;
	private QueueConnectionFactory queueFactory = null;
	private QueueConnection queueConnection = null;
	private QueueSession queueSession = null;
	
	public HvQueue(String queueName) {
		this.queueName = queueName ;
		try {
			queueFactory = (QueueConnectionFactory) getInitialContext().lookup("ConnectionFactory") ;
			queueConnection = queueFactory.createQueueConnection() ;
			queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE) ;
		} catch(NamingException e) {
			log.warn("NamingException: ", e);
		} catch(JMSException e) {
			log.warn("JMSException", e) ;
		}
	}
	
	public void beIgnoreJMSExceptions() {
		ignoreJMSExceptions = true ;
	}
	
	public boolean isIgnoreJMSExceptions() {
		return ignoreJMSExceptions ;
	}
	
	public String getQueueName() {
		return queueName ;
	}
	
	protected Context getInitialContext() {
		if(context != null) return context ;
		try {
			context = new InitialContext() ;
		} catch(NamingException e) {
			log.error("NamingException creating initialContext", e);
		}
		return context ;
	}
	
	protected QueueSession getQueueSession() {
		return queueSession ;
	}
	
	protected QueueConnection getQueueConnection() {
		return queueConnection ;
	}

}
