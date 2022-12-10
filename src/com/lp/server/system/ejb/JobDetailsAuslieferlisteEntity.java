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
package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoAuslieferlistefindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsAuslieferlisteEntity o WHERE o.mandantCNr=?1") 
	})

@Entity
@Table(name = "AUTO_AUSLIEFERLISTE")
public class JobDetailsAuslieferlisteEntity extends JobDetailsEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7873444894643683069L;
	
	@Column(name = "C_PFADPATTERN")
	private String cPfadPattern;
	
	@Column(name = "I_TAGEBISSTICHTAG")
	private Integer iTageBisStichtag;

	@Column(name = "I_ARCHIVIERUNGSTAGE")
	private Integer iArchivierungstage;
	
	@Column(name = "B_NUR_LOSE_NACH_ENDETERMIN")
	private Short bNurLoseNachEndetermin;

	public Short getBNurLoseNachEndetermin() {
		return bNurLoseNachEndetermin;
	}

	public void setBNurLoseNachEndetermin(Short bNurLoseNachEndetermin) {
		this.bNurLoseNachEndetermin = bNurLoseNachEndetermin;
	}

	public JobDetailsAuslieferlisteEntity() {
		
	}
	

	public String getcPfadPattern() {
		return cPfadPattern;
	}

	public void setcPfadPattern(String cPfadPattern) {
		this.cPfadPattern = cPfadPattern;
	}

	public Integer getiTageBisStichtag() {
		return iTageBisStichtag;
	}

	public void setiTageBisStichtag(Integer iTageBisStichtag) {
		this.iTageBisStichtag = iTageBisStichtag;
	}

	public Integer getiArchivierungstage() {
		return iArchivierungstage;
	}

	public void setiArchivierungstage(Integer iArchivierungstage) {
		this.iArchivierungstage = iArchivierungstage;
	}

}
