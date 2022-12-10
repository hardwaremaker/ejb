package com.lp.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.server.schema.easydata.stm.XMLBaseStockInfo;

public class EasydataXMLFieldInstantiator<T extends XMLBaseStockInfo> extends FieldInstantiator<T> {

	public EasydataXMLFieldInstantiator() {
		super();
		
		excludedDatatypes.put(BigDecimal.class.getSimpleName(), 1);
		excludedDatatypes.put(String.class.getSimpleName(), 2);
		excludedDatatypes.put(XMLGregorianCalendar.class.getSimpleName(), 3);
		excludedDatatypes.put(Integer.class.getSimpleName(), 4);
	}

	@Override
	protected boolean meetExceptions(Method theMethod) {
		return theMethod.getReturnType().isEnum();
	}

}
