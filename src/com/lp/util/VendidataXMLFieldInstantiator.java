package com.lp.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

public class VendidataXMLFieldInstantiator<T> extends FieldInstantiator<T> {

	public VendidataXMLFieldInstantiator() {
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
