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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Automatikjobtype;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatikjobtypeDtoAssembler;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
@Stateless
public class AutomatikjobtypeFacBean extends Facade implements AutomatikjobtypeFac{

@PersistenceContext
private EntityManager em;

	
	public AutomatikjobtypeDto automatikjobtypeFindByCJobType(String jobType)
			throws RemoteException {
		Query query = em.createNamedQuery("AutomatikjobtypefindByCJobType");
		query.setParameter(1, jobType);
		return assembleAutomatikjobtypeDto((Automatikjobtype) query.getSingleResult());
	}

	
	public AutomatikjobtypeDto automatikjobtypeFindByPrimaryKey(Integer id)
			throws RemoteException {
		Automatikjobtype automatikjobtype = em.find(Automatikjobtype.class, id);
		if(automatikjobtype==null){
			return null;
		}
		return assembleAutomatikjobtypeDto(automatikjobtype);
	}

	
	public void createAutomatikjobtype(AutomatikjobtypeDto automatikjobtypeDto)
			throws RemoteException {
		Automatikjobtype automatikjobtype = new Automatikjobtype();
		automatikjobtype = setAutomatikjobtypeFromAutomatikjobtypeDto(automatikjobtype,
				automatikjobtypeDto);
      em.persist(automatikjobtype);
  em.flush();
		
	}

	
	public void removeAutomatikjobtype(Integer id) throws RemoteException {
Automatikjobtype toRemove = em.find(Automatikjobtype.class, id);
if (toRemove == null) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
}
try {
  em.remove(toRemove);
  em.flush();
} catch (EntityExistsException er) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
}
	}

	
	public void removeAutomatikjobtype(AutomatikjobtypeDto automatikjobtypeDto)
			throws RemoteException {
		removeAutomatikjobtype(automatikjobtypeDto.getIId());
		
	}

	
	public void updateAutomatikjobtype(AutomatikjobtypeDto automatikjobtypeDto)
			throws RemoteException {
		Automatikjobtype automatikjobtype = new Automatikjobtype();
		automatikjobtype = setAutomatikjobtypeFromAutomatikjobtypeDto(automatikjobtype,
				automatikjobtypeDto);
      em.persist(automatikjobtype);
  em.flush();
		
	}

	
	public void updateAutomatikjobtypes(
			AutomatikjobtypeDto[] automatikjobtypeDtos) throws RemoteException {
		for(int i=0;i<automatikjobtypeDtos.length;i++){
		Automatikjobtype automatikjobtype = new Automatikjobtype();
		automatikjobtype = setAutomatikjobtypeFromAutomatikjobtypeDto(automatikjobtype,
				automatikjobtypeDtos[i]);
      em.persist(automatikjobtype);
  em.flush();
		}
		
	}
	
	  private AutomatikjobtypeDto assembleAutomatikjobtypeDto(Automatikjobtype
		      automatikjobtype) {
		    return AutomatikjobtypeDtoAssembler.createDto(automatikjobtype);
		  }
	  
	  private Automatikjobtype setAutomatikjobtypeFromAutomatikjobtypeDto(Automatikjobtype
		      automatikjobtype, AutomatikjobtypeDto automatikjobtypeDto) {
		    automatikjobtype.setCJobtype(automatikjobtypeDto.getCJobtype());
		    automatikjobtype.setCBeschreibung(automatikjobtypeDto.getCBeschreibung());
		    return automatikjobtype;
		  }

}
