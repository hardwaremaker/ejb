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
package com.lp.server.system.service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PingPacket implements Externalizable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1335879111579712280L;
	
	private Integer requestNumber ;
	private long pingTimeReceiver ;
	private long pingTimeSenderStart ;
	private long pingTimeSenderStop ;
	
	/**
	 * Get the Request Number received
	 * 
	 * @return the received request number
	 */
	public Integer getRequestNumber() {
		return requestNumber;
	}
	
	/**
	 * Set the Request Number
	 * 
	 * @param requestNumber is the request number. Set the value as you like,
	 * it is just for your reference
	 */
	public void setRequestNumber(Integer requestNumber) {
		this.requestNumber = requestNumber;
	}
	
	/**
	 * Returns the time (ms) the packet received
	 * 
	 * @return the time received this packet
	 */
	public long getPingTimeReceiver() {
		return pingTimeReceiver;
	}
	
	public void setPingTimeReceiver(long pingTime) {
		this.pingTimeReceiver = pingTime;
	}

	public long getPingTimeSender() {
		return pingTimeSenderStart;
	}

	public void setPingTimeSender(long pingTimeSender) {
		this.pingTimeSenderStart = pingTimeSender;
	}

	public long getPingTimeSenderStop() {
		return pingTimeSenderStop;
	}

	public void setPingTimeSenderStop(long pingTimeSenderStop) {
		this.pingTimeSenderStop = pingTimeSenderStop;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(requestNumber) ;
		out.writeLong(pingTimeReceiver) ;
		out.writeLong(pingTimeSenderStart) ;
		out.writeLong(pingTimeSenderStop) ;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		requestNumber = in.readInt() ;
		pingTimeReceiver = in.readLong();
		pingTimeSenderStart = in.readLong();
		pingTimeSenderStop = in.readLong() ;
	}
}
