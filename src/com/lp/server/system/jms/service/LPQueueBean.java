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
 ******************************************************************************/
package com.lp.server.system.jms.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;

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

import com.lp.server.system.service.TheClientFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@MessageDriven (activationConfig= { 
		@ActivationConfigProperty(propertyName="messagingType",propertyValue="javax.jms.MessageListener"),
		@ActivationConfigProperty(propertyName="destination",propertyValue="queue/LPQueue"),
		@ActivationConfigProperty(propertyName="destinationType",propertyValue="javax.jms.Queue")
		} ) 
public class LPQueueBean
extends Facade implements MessageListener{

	static final public String DESTINATION_LPQUEUE_NAME = "queue/LPQueue";
	
	private MessageDrivenContext messageDrivenContext = null;
	private ConnectionFactory connectionFactory = null;

	/**
	 * Verarbeite die Aufgabe in der Message; Hier kommen alle LP Queueaufgaben vorbei.
	 * @param lpmI LPMessage
	 * @return Boolean
	 */
	private LPMessage process(LPMessage lpmI) {

		Boolean bbRet = Boolean.FALSE;

		try {

			//testing
			//Thread.sleep(20000);

			if (lpmI.getIiWhatIWant() == LPMessage.IIDELETE_THE_CLIENT_LOGGED_IN) {
				getTheClientFac().removeTheClientTVonTBis(
						(Timestamp) lpmI.getValue(TheClientFac.FLRSPALTE_T_LOGGEDIN),
						(Timestamp) lpmI.getValue(TheClientFac.FLRSPALTE_T_LOGGEDOUT));
				bbRet = Boolean.TRUE;
			}

			//jmsqueue: x hier wird erweitert.
			//      else if {
			//
			//      }
		}
		catch (RemoteException ex) {
			//exccatch: immer so!
			throwEJBExceptionLPRespectOld(ex);
		}
		catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}

		lpmI.setBbJobPositivProcessed(bbRet);
		return lpmI;
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


	private void closeConnection(Connection connnectionI)
	throws Exception {

		try {
			connnectionI.close();
		}
		catch (JMSException jmse) {
			throw jmse;
		}
	}


	public LPQueueBean() {
		try {
			InitialContext ic = new InitialContext();
			connectionFactory = (ConnectionFactory) ic.lookup("java:/JmsXA");
			ic.close();
		}
		catch (Exception e) {
			throw new EJBException("Failure to get connection factory: " + e.getMessage());
		}
	}


	/**
	 * Es kam was in die LPQueue, event rief onMessage auf; jetzt Message auspacken und abarbeiten.
	 * @param messageI Message
	 */
	public void onMessage(Message messageI) {
		Session session = null;
		Connection connection = null;
		ObjectMessage om = null;

		try {
			// jmsqueue: x wir verwenden die ObjectMessage und geben dieser unsere LPMessage mit
			om = (ObjectMessage) messageI;
			LPMessage lpm = (LPMessage) om.getObject();
			myLogger.logKritisch("LPQueue: "
					+ lpm.getSSender2stelligesModulKuerzel() + " | "
					+ lpm.getIiWhatIWant() + " | "
					+ lpm.getSUser() + " | "
					+ " received.");

			lpm = process(lpm);

			myLogger.logKritisch("LPQueue: "
					+ lpm.getSSender2stelligesModulKuerzel() + " | "
					+ lpm.getIiWhatIWant() + " | "
					+ lpm.getSUser() + " | "
					+ " processed.");

			/** @todo JO refac  PJ 4360 */
			connection = getConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination replyTo = messageI.getJMSReplyTo();
			MessageProducer producer = session.createProducer(replyTo);
			//An den Sender wird die LPMessage geschickt
			ObjectMessage reply = session.createObjectMessage(lpm);

			producer.send(reply);
			producer.close();
		}
		catch (UnsupportedOperationException uoe) {
			myLogger.error("JMS: " + uoe.getMessage());
		}
		catch (Exception e) {
			messageDrivenContext.setRollbackOnly();
			throw new EJBExceptionLP(e);
		}
		finally {
			if (connection != null) {
				try {
					closeConnection(connection);
				}
				catch (Exception e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
	}


	public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
		this.messageDrivenContext = messageDrivenContext;
	}


}
