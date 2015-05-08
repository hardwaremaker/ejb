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
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.lp.server.media.ejbfac.HvCancelSignal;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class HvCancelQueueReceiver extends HvQueueReceiver implements MessageListener {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private HvCancelSignal cancelSignal ;
	
	public HvCancelQueueReceiver(String queueName) {
		super(queueName) ;
		installListener(this);
		myLogger.warn("created new HvCancelQueueReceiver for " + this);
	}

	public HvCancelQueueReceiver(String queueName, HvCancelSignal cancelSignal) {
		super(queueName) ;
		this.cancelSignal = cancelSignal ;
		installListener(this);
		myLogger.warn("created new HvCancelQueueReceiver for " + this + " with Signal " + this.cancelSignal);
	}
	
	public HvCancelQueueReceiver(String queueName,
			MessageListener messageListener) {
		super(queueName, messageListener);
	}
	
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage msg = (TextMessage) message;
			String command = msg.getText() ;
			myLogger.warn("received command: " + command);
			msg.acknowledge();

			if (command.startsWith("cancel:")) {
				if(command.endsWith(EmailMediaFac.CancelMethod.RETRIEVE_EMAIL)) {
					cancelSignal.trigger() ; 
					myLogger.warn("cancelRetrieveEmail acknowledged " + cancelSignal.isTriggered() + ". " + cancelSignal);
				}
			}
		} catch (JMSException e) {
			myLogger.error("JMSException", e);
		}				
	}
}

