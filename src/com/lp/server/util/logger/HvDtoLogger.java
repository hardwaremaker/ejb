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
package com.lp.server.util.logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.lp.server.system.ejb.EntityLog;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.HvDtoLogAlways;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.HvDtoLogComplex;
import com.lp.server.system.service.HvDtoLogIdCBez;
import com.lp.server.system.service.HvDtoLogIdCnr;
import com.lp.server.system.service.HvDtoLogIdEmail;
import com.lp.server.system.service.HvDtoLogIgnore;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ICBez;
import com.lp.server.util.ICEmail;
import com.lp.server.util.ICNr;
import com.lp.server.util.IIId;
import com.lp.server.util.IMultipleKeyfields;

public class HvDtoLogger<T> {
	private EntityManager em;
	private TheClientDto theClientDto;
	private String cachedBaseId;
	private String operationMode;
	private String filterId;

	public HvDtoLogger(EntityManager em, TheClientDto theClientDto) {
		initialize(em, null, theClientDto);
	}

	public HvDtoLogger(EntityManager em, Integer filterIId,
			TheClientDto theClientDto) {
		initialize(em, filterIId.toString(), theClientDto);
	}

	public HvDtoLogger(EntityManager em, String headIId,
			TheClientDto theClientDto) {
		initialize(em, headIId, theClientDto);
	}

	protected void initialize(EntityManager em, String filterIId,
			TheClientDto theClientDto) {
		this.em = em;
		this.filterId = filterIId;
		this.theClientDto = theClientDto;
		this.operationMode = "UPDATE";
	}

	private String getPKName() {
		return PKConst.PK_ENTITYLOG;
	}

	public void log(T value1, T value2) {
		cachedBaseId = null;
		operationMode = "UPDATE";
		logImpl(value1, value2);
	}

	public void logDelete(T value1) {
		HvDtoLogClass logClassAnnotation = getLoggable(value1);
		if (logClassAnnotation == null)
			return;

		cachedBaseId = null;
		operationMode = "DELETE";
		logValues(logClassAnnotation, "", getBaseId(value1), "", "");
	}

	public void logInsert(T value1) {
		cachedBaseId = null;
		operationMode = "INSERT";

		try {
			Object value2 = Class.forName(value1.getClass().getName())
					.getConstructor().newInstance();
			logImpl(value1, value2);
		} catch (InvocationTargetException e) {
			System.out.println("Invocation " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccess " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("Instantiation " + e.getMessage());
		} catch (NoSuchMethodException e) {
			System.out.println("NoSuchMethod " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound " + e.getMessage());
		}
	}

	public String getHeadId() {
		return filterId;
	}

	public void setHeadId(String headId) {
		this.filterId = headId;
	}

	private void logImpl(Object object1, Object object2) {
		if (object1 == null)
			return;
		HvDtoLogClass logClassAnnotation = getLoggable(object1);
		if (logClassAnnotation == null)
			return;

		Method[] methods = object1.getClass().getMethods();
		for (Method theMethod : methods) {
			if (!theMethod.getName().startsWith("get"))
				continue;
			if (theMethod.getName().startsWith("getClass"))
				continue;
			// if(theMethod.getName().endsWith("Dto")) continue ;

			if (theMethod.isAnnotationPresent(Transient.class))
				continue;
			if (theMethod.isAnnotationPresent(HvDtoLogIgnore.class))
				continue;

			findDifference(logClassAnnotation, theMethod, object1, object2);
		}
	}

	private HvDtoLogClass getLoggable(Object object1) {
		return object1.getClass().getAnnotation(HvDtoLogClass.class);
	}

	private String getBaseId(Object o) {
		setBaseId(o);
		return cachedBaseId;
	}

	private void setBaseId(Object o) {
		if (cachedBaseId == null) {
			if (o instanceof IIId) {
				cachedBaseId = ((IIId) o).getIId().toString();
			} else {
				if (o instanceof IMultipleKeyfields) {
					cachedBaseId = StringUtils.join(
							((IMultipleKeyfields) o).getMKValue(), "|");
				}
			}
		}
	}

	private void findDifference(HvDtoLogClass logAnnotation, Method theMethod,
			Object object1, Object object2) {
		if (object1 == null || object2 == null)
			return;

		try {
			Object v1 = theMethod.invoke(object1, (Object[]) null);
			Object v2 = theMethod.invoke(object2, (Object[]) null);

			if (isDifferent(v1, v2)
					|| theMethod.isAnnotationPresent(HvDtoLogAlways.class)) {
				setBaseId(object1);

				logSpecializedAnnotationValue(logAnnotation, theMethod,
						object1, v1, v2);
				// if(theMethod.isAnnotationPresent(HvDtoLogComplex.class)) {
				// logImpl(v1, v2);
				// } else {
				// if(theMethod.isAnnotationPresent(HvDtoLogIdCnr.class)) {
				// v1 = getEntityCnr(theMethod, v1) ;
				// v2 = getEntityCnr(theMethod, v2) ;
				// } else {
				// if(theMethod.isAnnotationPresent(HvDtoLogIdCBez.class)) {
				// v1 = getEntityCBez(theMethod, v1) ;
				// v2 = getEntityCBez(theMethod, v2) ;
				// }
				// }
				//
				// logValues(logAnnotation, getKeyFromMethod(theMethod),
				// getBaseId(object1), v1, v2) ;
				// }
			}
		} catch (InvocationTargetException e) {
			System.out.println("invocation" + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("access" + e.getMessage());
		}
	}

	private void logSpecializedAnnotationValue(HvDtoLogClass logAnnotation,
			Method theMethod, Object object1, Object v1, Object v2) {
		if (theMethod.isAnnotationPresent(HvDtoLogComplex.class)) {
			logImpl(v1, v2);
			return;
		}

		if (theMethod.isAnnotationPresent(HvDtoLogIdCnr.class)) {
			v1 = getEntityCnr(theMethod, v1);
			v2 = getEntityCnr(theMethod, v2);
			logValues(logAnnotation, getKeyFromMethod(theMethod),
					getBaseId(object1), v1, v2);
			return;
		}

		if (theMethod.isAnnotationPresent(HvDtoLogIdCBez.class)) {
			v1 = getEntityCBez(theMethod, v1);
			v2 = getEntityCBez(theMethod, v2);
			logValues(logAnnotation, getKeyFromMethod(theMethod),
					getBaseId(object1), v1, v2);
			return;
		}

		if (theMethod.isAnnotationPresent(HvDtoLogIdEmail.class)) {
			v1 = getEntityEmail(theMethod, v1);
			v2 = getEntityEmail(theMethod, v2);
			logValues(logAnnotation, getKeyFromMethod(theMethod),
					getBaseId(object1), v1, v2);
			return;
		}

		logValues(logAnnotation, getKeyFromMethod(theMethod),
				getBaseId(object1), v1, v2);
	}

	private String getKeyFromMethod(Method theMethod) {
		String name = theMethod.getName();
		return name.startsWith("get") ? name.substring(3) : name;
	}

	private Object getEntityCnr(Method theMethod, Object primarykey) {
		if (primarykey == null)
			return primarykey;

		HvDtoLogIdCnr annotationIdCnr = theMethod
				.getAnnotation(HvDtoLogIdCnr.class);
		Object o = em.find(annotationIdCnr.entityClass(), primarykey);
		if (o != null) {
			o = ((ICNr) o).getCNr();
		}

		return o;
	}

	private Object getEntityCBez(Method theMethod, Object primarykey) {
		if (primarykey == null)
			return primarykey;

		HvDtoLogIdCBez annotationId = theMethod
				.getAnnotation(HvDtoLogIdCBez.class);
		Object o = em.find(annotationId.entityClass(), primarykey);
		if (o != null) {
			o = ((ICBez) o).getCBez();
		}

		return o;
	}

	private Object getEntityEmail(Method theMethod, Object primarykey) {
		if (primarykey == null)
			return primarykey;

		HvDtoLogIdEmail annotationId = theMethod
				.getAnnotation(HvDtoLogIdEmail.class);
		Object o = em.find(annotationId.entityClass(), primarykey);
		if (o != null) {
			o = ((ICEmail) o).getCEmail();
		}

		return o;
	}

	private boolean isDifferent(Object value1, Object value2) {
		if (value1 == null && value2 == null)
			return false;
		if (value1 == null || value2 == null)
			return true;
		return !value1.equals(value2);
	}

	private void logValues(HvDtoLogClass logAnnotation, String key,
			String theId, Object value1, Object value2) {
		PKGeneratorObj pkGen = new PKGeneratorObj();

		String s1 = getDbValue(value1);
		String s2 = getDbValue(value2);

		// PJ20637
		int iBlockgroesse = 3000;

		int iBloeckeMax = s1.length() / iBlockgroesse + 1;

		int iBlockgroesseS2 = s2.length() / iBlockgroesse + 1;

		if (iBlockgroesseS2 > iBloeckeMax) {
			iBloeckeMax = iBlockgroesseS2;
		}

		java.sql.Timestamp tAendern = new java.sql.Timestamp(
				System.currentTimeMillis());

		for (int i = 0; i < iBloeckeMax; i++) {

			String partS1 = null;
			String partS2 = null;

			if (s1.length() > iBlockgroesse) {
				partS1 = s1.substring(0, 2999);
				s1 = s1.substring(2999);
			} else {
				partS1 = s1;
				s1 = "";
			}

			if (s2.length() > iBlockgroesse) {
				partS2 = s2.substring(0, 2999);
				s2 = s2.substring(2999);
			} else {
				partS2 = s2;
				s2 = "";
			}

			Integer pk = pkGen.getNextPrimaryKey(getPKName());
			EntityLog log = new EntityLog(pk, logAnnotation.name(),
					operationMode, theId, key, partS1, partS2,
					theClientDto.getLocUiAsString(), tAendern,
					theClientDto.getIDPersonal());
			log.setFilterIId(filterId == null ? cachedBaseId : filterId);
			log.setCFilterKey(logAnnotation.filtername().length() == 0 ? logAnnotation
					.name() : logAnnotation.filtername());
			try {
				em.persist(log);
				em.flush();
			} catch (Throwable t) {
				System.out.println("t");
			}

			tAendern = new java.sql.Timestamp(tAendern.getTime() - (i * 10));

		}
	}

	private String getDbValue(Object value) {
		if (value == null)
			return "";
		String s = value.toString();
		return s;
	}
}
