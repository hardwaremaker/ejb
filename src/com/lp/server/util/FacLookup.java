package com.lp.server.util;

import javax.naming.Context;
import javax.naming.NamingException;

import com.lp.util.EJBExceptionLP;

public class FacLookup {
	public static <T> T lookup(Context context, Class<? extends Facade> bean, Class<T> callInterface) {
		try {
			return lookupWildflyFac(context, bean, callInterface);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	public static <T> T lookupLocal(Context context, Class<? extends Facade> bean, Class<T> callInterface) {
		try {

			return lookupWildflyFac(context, bean, callInterface);

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	public static <T> T lookupBeanless(Context context, Class<?> bean, Class<T> callInterface) {
		try {

			return lookupBeanlessWildflyFac(context, bean, callInterface);

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	public static <T> T lookupLocalBeanless(Context context, Class<?> bean, Class<T> callInterface) {
		try {

			return lookupBeanlessWildflyFac(context, bean, callInterface);

		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}
	}

	private static <T> T lookupWildflyFac(Context context, Class<? extends Facade> bean, Class<T> callInterface)
			throws NamingException {
		return (T) context.lookup("java:global/lpserver/ejb/" + bean.getSimpleName() + "!" + callInterface.getName());
	}

	private static <T> T lookupBeanlessWildflyFac(Context context, Class<?> bean, Class<T> callInterface)
			throws NamingException {
		return (T) context.lookup("java:global/lpserver/ejb/" + bean.getSimpleName() + "!" + callInterface.getName());
	}
}
