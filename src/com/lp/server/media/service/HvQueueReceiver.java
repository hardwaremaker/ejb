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
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.naming.NamingException;

public class HvQueueReceiver extends HvQueue {
	private boolean created = false;
	private Queue queue = null;
	private QueueReceiver queueReceiver = null;

	public HvQueueReceiver(String queueName) {
		super(queueName);
	}

	public HvQueueReceiver(String queueName, MessageListener messageListener) {
		super(queueName);
		createReceiverQueue(messageListener);
	}

	public void installListener(MessageListener messageListener) {
		createReceiverQueue(messageListener);
	}

	protected boolean createReceiverQueue(MessageListener messageListener) {
		// ->>UMBAU-BEISPIEL FUER WILDFLY SIEHE KLASSE LPAsynchSubscriber

		created = false;
		try {
			try {
				try {

					queue = (Queue) getInitialContext().lookup(getQueueName());
				} catch (NamingException e1) {
					log.error("NAMING1", e1);
					e1.printStackTrace();
					queue = (Queue) getInitialContext().lookup("jms/" + getQueueName());
				}
			} catch (NamingException e) {
				log.error("NAMING2", e);
				e.printStackTrace();
				queue = getQueueSession().createQueue(getQueueName());
			}
			queueReceiver = getQueueSession().createReceiver(queue);
			queueReceiver.setMessageListener(messageListener);
			getQueueConnection().start();
			created = true;

			log.info("created receiving end of Queue " + getQueueName());
		} catch (JMSException e) {
			log.warn("JMSException: ", e);
		}

		return created;
	}

	public Message receiveNoWait() throws JMSException {
		return queueReceiver.receiveNoWait();
	}
}
