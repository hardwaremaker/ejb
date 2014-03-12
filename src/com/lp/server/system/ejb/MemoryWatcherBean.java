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
package com.lp.server.system.ejb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jboss.annotation.ejb.Service;

@Service
public class MemoryWatcherBean implements MemoryWatcherFacLocal {

	private MemoryWatcherThread watcherThread ;
	
	public MemoryWatcherBean() {
		watcherThread = new MemoryWatcherThread() ;
	}

	@Override
	public String view() {
		return "<html><h1>Memory Overview</h1></html>";
	}

	@Override
	public List<TimestampMemory> getTimestamps() {
		return watcherThread.getTimestamps() ;
	}
}



class MemoryWatcherThread implements Runnable {

	public final static int MAX_ELEMENTS = 2000 ;
	
	private Thread runner;
	private List<TimestampMemory> values = new LinkedList<TimestampMemory>() ;
	
	public MemoryWatcherThread() {
		this("Default-MemoryWatcherThread") ;
	}
	
	public MemoryWatcherThread(String threadName) {
		runner = new Thread(this, threadName); 
		runner.start(); 
	}
	
	public List<TimestampMemory> getTimestamps() {
		synchronized (values) {
			return new ArrayList<TimestampMemory>(values) ;
		}
	}
	
	public void run() {
		try {
			do {
				TimestampMemory stamp = new TimestampMemory() ;
//				System.out.println("free @" + stamp.getTimestamp() + ":" + stamp.getFreeMemory() );
				
				synchronized(values) {
					values.add(stamp) ;
					if(values.size() > MAX_ELEMENTS) {
						values.remove(0) ;
					}					
				}

				Thread.sleep(5000);		
			} while(true) ;		
		} catch(InterruptedException e) {			
		}
	}
}
