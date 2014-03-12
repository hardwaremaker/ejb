/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
 ******************************************************************************/
package com.lp.server.system.jms.service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

@MessageDriven(activationConfig= { 
		@ActivationConfigProperty(propertyName="messagingType",propertyValue="javax.jms.MessageListener"),
		@ActivationConfigProperty(propertyName="destination",propertyValue="topic/ManageTopic"),
		@ActivationConfigProperty(propertyName="destinationType",propertyValue="javax.jms.Topic")
		} ) 
public class LPTopicManageBean implements MessageListener {
	private MessageDrivenContext messageDrivenContext = null;
	private ConnectionFactory connectionFactory = null;

	public final static String DESTINATION_TOPIC_NAME_MANAGE = "topic/ManageTopic";
	public final static String TOPIC_NAME_MANAGE = "TopicManage";
	
	public LPTopicManageBean() {
		try {
			InitialContext ic = new InitialContext();
			connectionFactory = (ConnectionFactory) ic.lookup("java:/JmsXA");
			ic.close();
		}
		catch (Exception e) {
			throw new EJBException("Failure to get connection factory: " + e.getMessage());
		}

	}	

	@Override
	public void onMessage(Message messageI) {
		Session session = null;
		Connection connection = null;
		ObjectMessage om = null;
		try {
			connection = getConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination replyTo = messageI.getJMSReplyTo();
			if (replyTo != null) {
				MessageProducer producer = session.createProducer(replyTo);

				LPMessage lpm = new LPMessage();
				ObjectMessage reply = session.createObjectMessage(lpm);

				producer.send(reply);
				producer.close();
			}
		}
		catch (Exception ex) {
		}
	}

	private void closeConnection(Connection connnectionI)
	throws Exception {
		try {
			connnectionI.close();
		}
		catch (JMSException jmse) {
			throw jmse;
		}
	}

	private Connection getConnection()
	throws Exception {
		Connection connection = null;

		try {
			connection = connectionFactory.createConnection();
			connection.start();

		}
		catch (Exception e) {
			if (connection != null) {
				closeConnection(connection);
			}
			throw e;
		}

		return connection;
	}


	public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
		this.messageDrivenContext = messageDrivenContext;
	}
}
