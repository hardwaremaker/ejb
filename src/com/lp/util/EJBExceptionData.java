package com.lp.util;

import java.io.Serializable;

// Interface geht leider nicht, weil KuecheFacBeanWS (JAXB) das nicht kann
public class EJBExceptionData implements Serializable {
	private static final long serialVersionUID = -524376312772905494L;

	protected EJBExceptionData() {
	}
//	Object[] asArray() ;
}
